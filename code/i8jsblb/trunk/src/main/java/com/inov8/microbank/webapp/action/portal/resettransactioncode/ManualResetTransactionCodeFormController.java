package com.inov8.microbank.webapp.action.portal.resettransactioncode;
/*
 * Author : Hassan Javaid
 * Date   : 13-06-2014
 * Module : Action Authorization
 * Project: Mircobank	
 * */
import java.util.ArrayList;
import java.util.Date;
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
import com.inov8.framework.common.model.messagemodule.EmailMessage;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.exception.EmailServiceSendFailureException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ManualResetTxCodeRefDataModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.EmailServiceConstants;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.service.portal.transactiondetail.TransactionDetailManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class ManualResetTransactionCodeFormController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( ManualResetTransactionCodeFormController.class );
	private TransactionDetailManager transactionDetailManager;
	
	
	public ManualResetTransactionCodeFormController() {
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
			ManualResetTxCodeRefDataModel manualResetTxCodeRefDataModel = (ManualResetTxCodeRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			request.setAttribute("transactionId",manualResetTxCodeRefDataModel.getTransactionId());
			
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
			BaseWrapper baseWrapper = new BaseWrapperImpl();		
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.ONE_TIME_PIN_RESET_USECASE_ID));

			String transactionId= ServletRequestUtils.getStringParameter(request,"transactionId");
			String oneTimePin= ServletRequestUtils.getStringParameter(request,"transactionCode");
			String comments= ServletRequestUtils.getStringParameter(request,"comments");
			
			
				if (null != transactionId && null!=oneTimePin)
				{	
					MiniTransactionModel miniTransactionModel;		 
					miniTransactionModel= transactionDetailManager.LoadMiniTransactionModel(transactionId);
					
					if(null!=miniTransactionModel){			
						miniTransactionModel.setOneTimePin(EncoderUtils.encodeToSha(oneTimePin));
						miniTransactionModel.setUpdatedOn(new Date());
						miniTransactionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
						miniTransactionModel.setComments(comments);
						baseWrapper.setBasePersistableModel(miniTransactionModel);
						baseWrapper = this.transactionDetailManager.updateMiniTransactionModel(baseWrapper);
						this.saveMessage(request, "Transaction Code updated successfully.");
						modelAndView = new ModelAndView(this.getSuccessView());
						
					}
					else{
						
						this.saveMessage(request, "No Record found");
						modelAndView = super.showForm(request, response, errors);
					}
				}
				else
				{
					
					this.saveMessage(request, "Trnasaction ID cannot be Null");
					modelAndView = super.showForm(request, response, errors);
				}

		}
		catch (Exception ex)
		{	
			super.saveMessage(request, "Request cannot be processed. Kinldy contact system administrator");
			modelAndView = super.showForm(request, response, errors);
			LOGGER.error("Exception occured while updating transaction code : ", ex);
		}
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
			
			long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(PortalConstants.ONE_TIME_PIN_RESET_USECASE_ID,actionAuthorizationModel.getEscalationLevel());
			if(nextAuthorizationLevel<1){
				
				XStream xstream = new XStream();
				ManualResetTxCodeRefDataModel manualResetTxCodeRefDataModel = (ManualResetTxCodeRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.ONE_TIME_PIN_RESET_USECASE_ID));
				baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
				baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
				baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
				
				MiniTransactionModel miniTransactionModel;		 
				miniTransactionModel= transactionDetailManager.LoadMiniTransactionModel(manualResetTxCodeRefDataModel.getTransactionId());
				
				if(null!=miniTransactionModel){			
					miniTransactionModel.setOneTimePin(EncoderUtils.encodeToSha(manualResetTxCodeRefDataModel.getOneTimePin()));
					miniTransactionModel.setUpdatedOn(actionAuthorizationModel.getCreatedOn());
					miniTransactionModel.setUpdatedByAppUserModel(actionAuthorizationModel.getCreatedByAppUserModel());
					miniTransactionModel.setComments(manualResetTxCodeRefDataModel.getComments());
					baseWrapper.setBasePersistableModel(miniTransactionModel);
					baseWrapper = this.transactionDetailManager.updateMiniTransactionModel(baseWrapper);

				}
				else{
					
					throw new FrameworkCheckedException("Transaction ID do not exists"); 
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
		request.setAttribute("message", MessageUtil.getMessage("6075"));
		request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
    	return super.showForm(request, response, errors);
	}
	Map<String, String> map = new HashMap<String, String>();
	map.put("status","success");
    modelAndView = new ModelAndView(this.getSuccessView(),map);	
    return modelAndView; 
	}
	

	@Override
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
		ModelAndView modelAndView = null;
		try
		{
			BaseWrapper baseWrapper = new BaseWrapperImpl();		
		
			String transactionId= ServletRequestUtils.getStringParameter(request,"transactionId");
			String oneTimePin= ServletRequestUtils.getStringParameter(request,"transactionCode");
			String comments= ServletRequestUtils.getStringParameter(request,"comments");
			
			XStream xstream = new XStream();
			ManualResetTxCodeRefDataModel manualResetTxCodeRefDataModel = new ManualResetTxCodeRefDataModel(transactionId,oneTimePin,comments);
			String refDataModelString= xstream.toXML(manualResetTxCodeRefDataModel);

			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(PortalConstants.ONE_TIME_PIN_RESET_USECASE_ID);
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(PortalConstants.ONE_TIME_PIN_RESET_USECASE_ID,new Long(0));

			if (null != transactionId && null!=oneTimePin){
				if(nextAuthorizationLevel.intValue()<1){
					baseWrapper = new BaseWrapperImpl();		
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
					baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.ONE_TIME_PIN_RESET_USECASE_ID));
					
					MiniTransactionModel miniTransactionModel;		 
					miniTransactionModel= transactionDetailManager.LoadMiniTransactionModel(transactionId);
					
					if(null!=miniTransactionModel){			
						miniTransactionModel.setOneTimePin(EncoderUtils.encodeToSha(oneTimePin));
						miniTransactionModel.setUpdatedOn(new Date());
						miniTransactionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
						miniTransactionModel.setComments(comments);
						baseWrapper.setBasePersistableModel(miniTransactionModel);
						baseWrapper = this.transactionDetailManager.updateMiniTransactionModel(baseWrapper);
					}
					else{
						
						this.saveMessage(request, "No Record found");
						modelAndView = super.showForm(request, response, errors);
					}
					
						Long actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel, comments, refDataModelString,null,usecaseModel,null,request);
						this.saveMessage(request, "Transaction Code updated successfully.Reference Action ID :"+actionAuthorizationId);
						modelAndView = new ModelAndView(this.getSuccessView());
						
				}
				else
				{									
					Long actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel, comments, refDataModelString,null,usecaseModel.getUsecaseId(),transactionId,false,null,request);
					this.saveMessage(request, "Action is pending for approval. Reference Action ID :"+actionAuthorizationId);
					modelAndView = new ModelAndView(this.getSuccessView());
				}
			}
			else
			{
				this.saveMessage(request, "Trnasaction ID cannot be Null");
				modelAndView = super.showForm(request, response, errors);
			}
		}
		catch (FrameworkCheckedException ex)
		{	
			if(ex.getMessage().contains("already exist"))
				super.saveMessage(request,ex.getMessage());				
			else
			super.saveMessage(request, "Request cannot be processed. Kinldy contact system administrator");
			modelAndView = super.showForm(request, response, errors);
			LOGGER.error("Exception occured while updating transaction code : ", ex);
		}catch (Exception ex)
		{	
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			modelAndView = super.showForm(request, response, errors);
			LOGGER.error("Exception occured while updating transaction code : ", ex);
		}
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
			ManualResetTxCodeRefDataModel manualResetTxCodeRefDataModel = (ManualResetTxCodeRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.ONE_TIME_PIN_RESET_USECASE_ID));
			baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
			baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
			baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
			
			MiniTransactionModel miniTransactionModel;		 
			miniTransactionModel= transactionDetailManager.LoadMiniTransactionModel(manualResetTxCodeRefDataModel.getTransactionId());
			
			if(null!=miniTransactionModel){			
				miniTransactionModel.setOneTimePin(EncoderUtils.encodeToSha(manualResetTxCodeRefDataModel.getOneTimePin()));
				miniTransactionModel.setUpdatedOn(actionAuthorizationModel.getCreatedOn());
				miniTransactionModel.setUpdatedByAppUserModel(actionAuthorizationModel.getCreatedByAppUserModel());
				miniTransactionModel.setComments(manualResetTxCodeRefDataModel.getComments());
				baseWrapper.setBasePersistableModel(miniTransactionModel);
				baseWrapper = this.transactionDetailManager.updateMiniTransactionModel(baseWrapper);
			}
			else{
				
				throw new FrameworkCheckedException("Transaction ID do not exists"); 
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
		request.setAttribute("message", MessageUtil.getMessage("6075"));
		request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
    	return super.showForm(request, response, errors);
	}	
	Map<String, String> map = new HashMap<String, String>();
	map.put("status","success");
    modelAndView = new ModelAndView(this.getSuccessView(),map);	
    return modelAndView; 	
	}
	
	public void setTransactionDetailManager(TransactionDetailManager transactionDetailManager){
		this.transactionDetailManager = transactionDetailManager;
	}

}