package com.inov8.microbank.webapp.action.portal.resetportalpasswordmodule;

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
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ResetPortalPasswordRefDataModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class ResetPortalPasswordFormController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( ResetPortalPasswordFormController.class );
	private UserManagementManager	userManagementManager;
		
	public ResetPortalPasswordFormController() {
		setCommandName("actionAuthorizationModel");
		setCommandClass(ActionAuthorizationModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {
		Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();
		boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
		boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);
		Boolean isHandler = ServletRequestUtils.getBooleanParameter(request, "isHandler", false);
		if(isHandler)
		{
			request.setAttribute("username",request.getParameter("username"));
			request.setAttribute("isAgent","false");
		}
		
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
			ResetPortalPasswordRefDataModel resetPortalPasswordRefDataModel = (ResetPortalPasswordRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			
			if(actionAuthorizationModel.getUsecaseId().longValue()==PortalConstants.RESET_AGENT_PASSWORD_PORTAL_USECASE_ID){
				request.setAttribute("mfsId",resetPortalPasswordRefDataModel.getMfsId());
				request.setAttribute("isAgent","true");
			}	
			else{
				request.setAttribute("username",resetPortalPasswordRefDataModel.getUsername());
				request.setAttribute("isAgent","false");
			}		
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
			Long appUserId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request,"appUserId")));
			String mfsId = new String(ServletRequestUtils.getStringParameter(request,"mfsId"));
			String notifyViaSMS = ServletRequestUtils.getStringParameter(request,PortalConstants.KEY_NOTIFY_BY_SMS);
			Long usecaseId= ServletRequestUtils.getLongParameter(request,"usecaseId");
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID,usecaseId);
			baseWrapper.putObject(PortalConstants.KEY_NOTIFY_BY_SMS, notifyViaSMS);
			baseWrapper.putObject("appUserId", appUserId);
			baseWrapper.putObject("mfsId", mfsId);
				
			baseWrapper = this.userManagementManager.changeAgentPasswordBySMSEmail(baseWrapper);

		}
		catch (FrameworkCheckedException ex)
		{	
			LOGGER.error("Exception occured while resending sms : " +ex.getMessage());
			request.setAttribute("message","Password cannot be reset at moment. Kindly contact administrator.");
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
		map.put("message","Password reset successfully");
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
			
			long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(actionAuthorizationModel.getUsecaseId(),actionAuthorizationModel.getEscalationLevel());
			if(nextAuthorizationLevel<1){
				
				XStream xstream = new XStream();
				ResetPortalPasswordRefDataModel resetPortalPasswordRefDataModel = (ResetPortalPasswordRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
				
		
				Long appUserId = new Long(EncryptionUtil.decryptWithDES(resetPortalPasswordRefDataModel.getEncriptedAppUserId()));
				String mfsId = resetPortalPasswordRefDataModel.getMfsId();
				String notifyViaSMS = resetPortalPasswordRefDataModel.getNotifyBySms();
						
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, actionAuthorizationModel.getUsecaseId());
			    baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
				baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
				baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
				
				baseWrapper.putObject(PortalConstants.KEY_NOTIFY_BY_SMS, notifyViaSMS);
				baseWrapper.putObject("appUserId", appUserId);
				baseWrapper.putObject("mfsId", mfsId);
					
				baseWrapper = this.userManagementManager.changeAgentPasswordBySMSEmail(baseWrapper);
				
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
			Long usecaseId= ServletRequestUtils.getLongParameter(request,"usecaseId");
			String comments= ServletRequestUtils.getStringParameter(request,"comments");
			String appUserIdParam = ServletRequestUtils.getStringParameter(request, "appUserId");
			String mfsId = ServletRequestUtils.getStringParameter(request, "mfsId");
			String username = ServletRequestUtils.getStringParameter(request, "username");
			String notifyViaSMS = ServletRequestUtils.getStringParameter(request,PortalConstants.KEY_NOTIFY_BY_SMS,"false");
						
			XStream xstream = new XStream();
			ResetPortalPasswordRefDataModel resetPortalPasswordRefDataModel = new ResetPortalPasswordRefDataModel(appUserIdParam,notifyViaSMS,mfsId,username);	
			String refDataModelString= xstream.toXML(resetPortalPasswordRefDataModel);
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(usecaseId);
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(usecaseId,new Long(0));
				
			if(nextAuthorizationLevel.intValue()<1){
				
				Long appUserId = new Long(EncryptionUtil.decryptWithDES(appUserIdParam));
				
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, usecaseId);
				baseWrapper.putObject(PortalConstants.KEY_NOTIFY_BY_SMS, notifyViaSMS);
				baseWrapper.putObject("appUserId", appUserId);
				baseWrapper.putObject("mfsId", mfsId);
					
				baseWrapper = this.userManagementManager.changeAgentPasswordBySMSEmail(baseWrapper);
				
				if(null!=baseWrapper.getObject("errorMessage"))
					throw new FrameworkCheckedException(baseWrapper.getObject("errorMessage").toString());
				
				Long actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel, comments, refDataModelString,null,usecaseModel,null,request);
				message = "Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId;
				
			}
			else
			{									
				Long actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel, comments, refDataModelString,null,usecaseModel.getUsecaseId(),mfsId,false,null,request);
				message = "Action is pending for approval against reference Action ID : "+actionAuthorizationId;
			}
		
		}
		catch (FrameworkCheckedException ex)
		{	
			LOGGER.error("Exception occured while resending sms : " +ex.getMessage());
			if(ex.getMessage().contains("already exist") || ex.getMessage().contains("reset your password"))
				request.setAttribute("message",ex.getMessage());		
			else	
				request.setAttribute("message","Request cannont be processed at the moment. Kindly contact administrator.");
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}catch (Exception ex)
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
			ResetPortalPasswordRefDataModel resetPortalPasswordRefDataModel = (ResetPortalPasswordRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
				
			Long appUserId = new Long(EncryptionUtil.decryptWithDES(resetPortalPasswordRefDataModel.getEncriptedAppUserId()));
			String mfsId = resetPortalPasswordRefDataModel.getMfsId();
			String notifyViaSMS = resetPortalPasswordRefDataModel.getNotifyBySms();
					
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, actionAuthorizationModel.getUsecaseId());
		    baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
			baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
			baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
			
			baseWrapper.putObject(PortalConstants.KEY_NOTIFY_BY_SMS, notifyViaSMS);
			baseWrapper.putObject("appUserId", appUserId);
			baseWrapper.putObject("mfsId", mfsId);
				
			baseWrapper = this.userManagementManager.changeAgentPasswordBySMSEmail(baseWrapper);
			
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
		map.put("message","Password is successfully reset, email is sent.");
	    modelAndView = new ModelAndView(this.getSuccessView(),map);	
		return modelAndView;	
	}
	
	public void setUserManagementManager(UserManagementManager userManagementManager) {
		this.userManagementManager = userManagementManager;
	}	

}