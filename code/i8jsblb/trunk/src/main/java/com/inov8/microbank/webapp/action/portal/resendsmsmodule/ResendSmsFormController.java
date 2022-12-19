package com.inov8.microbank.webapp.action.portal.resendsmsmodule;

/*
 * Author : Hassan Javaid
 * Date   : 07-07-2014
 * Module : Action Authorization
 * Project: Mircobank	
 * */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ResendSmsRefDataModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderService;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class ResendSmsFormController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( ResendSmsFormController.class );
	private SmsSenderService smsSenderService;
	private ComplaintManager complaintManager;
	
	
	public ResendSmsFormController() {
		setCommandName("actionAuthorizationModel");
		setCommandClass(ActionAuthorizationModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {
		Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();
		boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
		boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);
		
		if (escalateRequest || resolveRequest){				
			ActionStatusModel actionStatusModel = new ActionStatusModel();
			ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl( actionStatusModel, "name", SortingOrder.ASC );
			referenceDataManager.getReferenceData( refDataWrapper );
			List<ActionStatusModel> actionStatusModelList;
			actionStatusModelList=refDataWrapper.getReferenceDataList();
			List<ActionStatusModel> tempActionStatusModelList = new ArrayList<>();
			
			for (ActionStatusModel actionStatusModel2 :  actionStatusModelList) {
				if(((actionStatusModel2.getActionStatusId().intValue()== ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.intValue())
						||(actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL.intValue()) 
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.intValue())						
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.intValue())) && escalateRequest )
					tempActionStatusModelList.add(actionStatusModel2);
				else if((actionStatusModel2.getActionStatusId().intValue()== ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED.intValue()) && resolveRequest)
					tempActionStatusModelList.add(actionStatusModel2);
			}		
			referenceDataMap.put( "actionStatusModel", tempActionStatusModelList);
			
			////// Action Authorization history////
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel = new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
			
			List<ActionAuthorizationHistoryModel> actionAuthorizationHistoryModelList;
			
			refDataWrapper = new ReferenceDataWrapperImpl( actionAuthorizationHistoryModel, "escalationLevel", SortingOrder.ASC );
			referenceDataManager.getReferenceData( refDataWrapper );
			
			actionAuthorizationHistoryModelList=refDataWrapper.getReferenceDataList();
			
			referenceDataMap.put( "actionAuthorizationHistoryModelList",actionAuthorizationHistoryModelList );		
		}
		return referenceDataMap;		
	}


	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		
		boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
		boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);
		
		if (escalateRequest || resolveRequest){
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			XStream xstream = new XStream();
			ResendSmsRefDataModel resendSmsRefDataModel = (ResendSmsRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			request.setAttribute("transactionCode",resendSmsRefDataModel.getTransactionCode());
			
			return actionAuthorizationModel;
		}
		else 
			return new ActionAuthorizationModel();
	}


	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception
	{
		return null;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object,
			BindException errors) throws Exception
	{
				
		ModelAndView modelAndView = null;
		try
		{	
			Long transactionId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getRequiredStringParameter(request,"transactionId")));
			String resendSmsStrategy = ServletRequestUtils.getRequiredStringParameter( request, "resendSmsStrategy" );
			String appUserIdParam = ServletRequestUtils.getStringParameter(request, "appUserId");

			Long appUserId = 0L;
			if(appUserIdParam != null && appUserIdParam.length()>0){
				appUserId = new Long(EncryptionUtil.decryptWithDES(appUserIdParam));
			}

			LOGGER.info("Sending sms for Transaction Id: "+ transactionId);
			smsSenderService.resendSmsUsingStrategy( transactionId, resendSmsStrategy );

			if(appUserId > 0)
			{
				complaintManager.createComplaint( ComplaintsModuleConstants.CATEGORY_RESEND_SMS, appUserId );					
			}			

		}
		catch (FrameworkCheckedException ex)
		{	
			LOGGER.error("Exception occured while resending sms : " +ex.getMessage());
			request.setAttribute("message","SMS cannot be resend at moment. Kindly contact administrator.");
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}
		catch (Exception ex)
		{	
			LOGGER.error("Exception occured while resending sms : " +ex.getMessage());
			request.setAttribute("message",MessageUtil.getMessage("6075"));
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status",IssueTypeStatusConstantsInterface.SUCCESS);
		map.put("message","SMS resend successfully");
	    modelAndView = new ModelAndView(this.getSuccessView(),map);	
		return modelAndView;
	}
	
	@Override
	protected ModelAndView onEscalate(HttpServletRequest request,HttpServletResponse response, Object command, BindException errors) throws Exception {
	
		ModelAndView modelAndView = null;
		ActionAuthorizationModel model = (ActionAuthorizationModel) command;
	try{
		ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
		boolean isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());
		long currentUserId= UserUtils.getCurrentUser().getAppUserId();
			
		UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
		if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
			if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().longValue()==currentUserId)){
				throw new FrameworkCheckedException("You are not authorized to update action status.");
			}
			
			long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(PortalConstants.RESEND_SMS_USECASE_ID,actionAuthorizationModel.getEscalationLevel());
			if(nextAuthorizationLevel<1){
				
				XStream xstream = new XStream();
				ResendSmsRefDataModel resendSmsRefDataModel = (ResendSmsRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
				
				Long transactionId = resendSmsRefDataModel.getTransactionId();
				String resendSmsStrategy  = resendSmsRefDataModel.getResendSmsStrategy();
				String appUserIdParam  = resendSmsRefDataModel.getAppUserId();
						
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.RESEND_SMS_USECASE_ID);
			    baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
				baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
				baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
				
				baseWrapper.putObject("transactionId",transactionId);
				baseWrapper.putObject("resendSmsStrategy",resendSmsStrategy);
				
				Long appUserId = 0L;
				if(appUserIdParam != null && appUserIdParam.length()>0){
					appUserId = new Long(EncryptionUtil.decryptWithDES(appUserIdParam));
				}

				LOGGER.info("Sending sms for Transaction Id: "+ transactionId);
				smsSenderService.resendSmsUsingStrategy(baseWrapper);

				if(appUserId > 0)
				{
					complaintManager.createComplaint( ComplaintsModuleConstants.CATEGORY_RESEND_SMS, appUserId );					
				}				
					
				
				if(actionAuthorizationModel.getEscalationLevel().intValue()< usecaseModel.getEscalationLevels().intValue()){
					approvedWithIntimationLevelsNext(actionAuthorizationModel,model, usecaseModel,request);
				}
				else
				{
					approvedAtMaxLevel(actionAuthorizationModel, model);
				}
			}
			else 
			{				
				escalateToNextLevel(actionAuthorizationModel,model, nextAuthorizationLevel, usecaseModel.getUsecaseId(),request);
			}
		}	
		else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
			isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());

			if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
				throw new FrameworkCheckedException("You are not authorized to update action status.");
			}	
			actionDeniedOrCancelled(actionAuthorizationModel, model,request);
		}
		else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.longValue() 
				&& (actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)
				|| actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK))){
			
			if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
				throw new FrameworkCheckedException("You are not authorized to update action status.");
			}
			actionDeniedOrCancelled(actionAuthorizationModel,model,request);
		}
		else{
			
			throw new FrameworkCheckedException("Invalid status marked");
		}
		
	}
	catch (FrameworkCheckedException ex)
	{	
		
		LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
		request.setAttribute("message", ex.getMessage());
		request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
    	return super.showForm(request, response, errors);
	}
	catch (Exception ex)
	{	
		LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
		request.setAttribute("message",MessageUtil.getMessage("6075"));
		request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
    	return super.showForm(request, response, errors);
	}
	Map<String, String> map = new HashMap<String, String>();
	map.put("status",IssueTypeStatusConstantsInterface.SUCCESS);
    modelAndView = new ModelAndView(this.getSuccessView(),map);	
    return modelAndView; 
	}
	

	@Override
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
		ModelAndView modelAndView = null;
		String message=null;
		try
		{	
			String comments= ServletRequestUtils.getStringParameter(request,"comments");
			Long transactionId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getRequiredStringParameter(request,"transactionId")));
			String resendSmsStrategy = ServletRequestUtils.getRequiredStringParameter( request, "resendSmsStrategy" );
			String appUserIdParam = ServletRequestUtils.getStringParameter(request, "appUserId");
			String transactionCode = ServletRequestUtils.getStringParameter(request, "transactionCode");
			
			
			XStream xstream = new XStream();
			ResendSmsRefDataModel resendSmsRefDataModel = new ResendSmsRefDataModel(transactionId,resendSmsStrategy,appUserIdParam,transactionCode);
			String refDataModelString= xstream.toXML(resendSmsRefDataModel);
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(PortalConstants.RESEND_SMS_USECASE_ID);
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(PortalConstants.RESEND_SMS_USECASE_ID,new Long(0));
				
			if(nextAuthorizationLevel.intValue()<1){
				
				Long appUserId = 0L;
				if(appUserIdParam != null && appUserIdParam.length()>0){
					appUserId = new Long(EncryptionUtil.decryptWithDES(appUserIdParam));
				}

				LOGGER.info("Sending sms for Transaction Id: "+ transactionId);
				smsSenderService.resendSmsUsingStrategy( transactionId, resendSmsStrategy );

				if(appUserId > 0)
				{
					complaintManager.createComplaint( ComplaintsModuleConstants.CATEGORY_RESEND_SMS, appUserId );					
				}			
				
				Long actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel, comments, refDataModelString,null,usecaseModel,null,request);
				message = "Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId;
				
			}
			else
			{									
				Long actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel, comments, refDataModelString,null,usecaseModel.getUsecaseId(),"",false,null,request);
				message = "Action is pending for approval against reference Action ID : "+actionAuthorizationId;
			}
		
		}
		catch (FrameworkCheckedException ex)
		{	
			LOGGER.error("Exception occured while resending sms : " +ex.getMessage());
			if(ex.getMessage().contains("already exist"))
				request.setAttribute("message",ex.getMessage());
			else
			request.setAttribute("message","Request cannont be processed at the moment. Kindly contact administrator.");
			
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}
		catch (Exception ex)
		{	
			LOGGER.error("Exception occured while resending sms : " +ex.getMessage());
			request.setAttribute("message",MessageUtil.getMessage("6075"));
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status",IssueTypeStatusConstantsInterface.SUCCESS);
		map.put("message",message);
	    modelAndView = new ModelAndView(this.getSuccessView(),map);	
		return modelAndView;
	}
	@Override
	protected ModelAndView onResolve(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		ModelAndView modelAndView = null;
		ActionAuthorizationModel model = (ActionAuthorizationModel) command;
		try{
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());			
			UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
			XStream xstream = new XStream();
			ResendSmsRefDataModel resendSmsRefDataModel = (ResendSmsRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			
			Long transactionId = resendSmsRefDataModel.getTransactionId();
			String resendSmsStrategy  = resendSmsRefDataModel.getResendSmsStrategy();
			String appUserIdParam  = resendSmsRefDataModel.getAppUserId();
					
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.RESEND_SMS_USECASE_ID);
		    baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
			baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
			baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
			
			baseWrapper.putObject("transactionId",transactionId);
			baseWrapper.putObject("resendSmsStrategy",resendSmsStrategy);
			
			Long appUserId = 0L;
			if(appUserIdParam != null && appUserIdParam.length()>0){
				appUserId = new Long(EncryptionUtil.decryptWithDES(appUserIdParam));
			}

			LOGGER.info("Sending sms for Transaction Id: "+ transactionId);
			smsSenderService.resendSmsUsingStrategy(baseWrapper);

			if(appUserId > 0)
			{
				complaintManager.createComplaint( ComplaintsModuleConstants.CATEGORY_RESEND_SMS, appUserId );					
			}				
			
			resolveWithIntimation(actionAuthorizationModel,model, usecaseModel,request);
			
		}
		catch (FrameworkCheckedException ex)
		{			
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			request.setAttribute("message", ex.getMessage());
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}
		catch (Exception ex)
		{	
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			request.setAttribute("message",MessageUtil.getMessage("6075"));
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status",IssueTypeStatusConstantsInterface.SUCCESS);
		map.put("message","SMS resend successfully.");
	    modelAndView = new ModelAndView(this.getSuccessView(),map);	
		return modelAndView;	
	}
	
	public void setSmsSenderService(SmsSenderService smsSenderService) {
		this.smsSenderService = smsSenderService;
	}

	public void setComplaintManager(ComplaintManager complaintManager) {
		this.complaintManager = complaintManager;
	}

}