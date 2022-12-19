package com.inov8.microbank.webapp.action.portal.usermanagementmodule;
/*
 * Author : Hassan Javaid
 * Date   : 13-08-2014
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
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.AllpayUserInfoListViewModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ResendSmsRefDataModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ResetPortalPasswordRefDataModel;
import com.inov8.microbank.common.model.portal.linkpaymentmodemodule.LinkPaymentModeModel;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.FinancialInstitutionConstants;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderService;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.ola.integration.vo.OLAVO;
import com.thoughtworks.xstream.XStream;

public class UserManagementAuthorizationFormController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( UserManagementAuthorizationFormController.class );
	private UserManagementManager	userManagementManager;
	private PartnerGroupManager partnerGroupManager;
		
	public UserManagementAuthorizationFormController() {
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
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.intValue()) 
						//|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_RE_SUBMIT.intValue())
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.intValue()))
						&& escalateRequest )
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
			
			if(actionAuthorizationModel.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
					&& actionAuthorizationModel.getCreatedById().longValue()== UserUtils.getCurrentUser().getAppUserId()){
				boolean isAssignedBack=false;
				isAssignedBack=true;
				request.setAttribute( "isAssignedBack",isAssignedBack );
			}
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
			UserManagementModel userManagementModel = (UserManagementModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			request.setAttribute("userModel",userManagementModel);
			
			if(actionAuthorizationModel.getUsecaseId().longValue() == PortalConstants.I8_USER_MANAGEMENT_UPDATE_USECASE_ID)
			{
				UserManagementModel oldUserManagementModel = (UserManagementModel) xstream.fromXML(actionAuthorizationModel.getOldReferenceData());			
				request.setAttribute("currentUserModel",oldUserManagementModel);
			}
			
			return actionAuthorizationModel;
		}
		else 
			return new ActionAuthorizationModel();
	}


	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object object,
			BindException errors) throws Exception
	{
		return null;		
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object,
			BindException errors) throws Exception
	{
				
		return null;
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
				UserManagementModel userManagementModel = (UserManagementModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
				
				baseWrapper.putObject(UserManagementModel.USER_MANAGEMENT_MODEL_KEY, userManagementModel);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, userManagementModel.getActionId());
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, userManagementModel.getUsecaseId());
				baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
				baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
				baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
				userManagementManager.validateUser(userManagementModel);
				
				if (actionAuthorizationModel.getUsecaseId() == PortalConstants.I8_USER_MANAGEMENT_CREATE_USECASE_ID){
					baseWrapper = this.userManagementManager.createNewUser(baseWrapper);
					String msg = this.getText("userManagementModule.recordSaveSuccessful", request.getLocale());
					this.saveMessage(request, msg);
				}else{	
					baseWrapper = this.userManagementManager.updateUser(baseWrapper);
					String msg = this.getText("userManagementModule.recordUpdateSuccessful", request.getLocale());
					this.saveMessage(request, msg);
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
		else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue() 
				&& actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
			isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());

			if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
				throw new FrameworkCheckedException("You are not authorized to update action status.");
			}
			requestAssignedBack(actionAuthorizationModel,model,request);
		} 
		else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_RE_SUBMIT.longValue()
				&& actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK)){
			
			if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
				throw new FrameworkCheckedException("You are not authorized to update action status.");
			}
			
			
		
		}
		else{
			
			throw new FrameworkCheckedException("Invalid status marked");
		}
		
	}
	catch (FrameworkCheckedException ex)
	{	
		
		if ("MobileNumUniqueException".equals(ex.getMessage()))
		{		
			request.setAttribute("message","User with same mobile number already exists. Kindly assigned back or close request");
		}
		else if ("PasswordConfirmPasswordMissMatchException".equals(ex.getMessage()))
		{
			request.setAttribute("message","Passwords provided do not match. Kindly assigned back or close request");
		}
		else if ("ConstraintViolationException".equals(ex.getMessage()) || "DataIntegrityViolationException".equals(ex.getMessage()))
		{
			request.setAttribute("message","User with same User ID already exists. Kindly assigned back or close request");
		}
		
		else if (ex.getMessage().contains("Employee with same Employee ID already exist."))
		{
			request.setAttribute("message","User with same Employee ID already exists. Kindly assigned back or close request");
		}
		else{
			if(null!=ex.getMessage())
				request.setAttribute("message",ex.getMessage().replaceAll("<br>",""));
			else
				request.setAttribute("message",MessageUtil.getMessage("6075"));
		}
			
		LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
		request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
    	return super.showForm(request, response, errors);
    	
	}
	catch (Exception ex)
	{	
		request.setAttribute("message",MessageUtil.getMessage("6075"));			
		LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
		request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
    	return super.showForm(request, response, errors);
    	
	}
	request.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
    modelAndView = super.showForm(request, response, errors);
    return modelAndView; 
	}
	

	@Override
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
		
		return null;
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
			UserManagementModel userManagementModel = (UserManagementModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
					
			baseWrapper.putObject(UserManagementModel.USER_MANAGEMENT_MODEL_KEY, userManagementModel);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, userManagementModel.getActionId());
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, userManagementModel.getUsecaseId());
			baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
			baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
			baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
			userManagementManager.validateUser(userManagementModel);
			
			if (actionAuthorizationModel.getUsecaseId() == PortalConstants.I8_USER_MANAGEMENT_CREATE_USECASE_ID){
				baseWrapper = this.userManagementManager.createNewUser(baseWrapper);
				String msg = this.getText("userManagementModule.recordSaveSuccessful", request.getLocale());
				this.saveMessage(request, msg);
			}else{	
				baseWrapper = this.userManagementManager.updateUser(baseWrapper);
				String msg = this.getText("userManagementModule.recordUpdateSuccessful", request.getLocale());
				this.saveMessage(request, msg);
			}
		
			resolveWithIntimation(actionAuthorizationModel,model, usecaseModel,request);
			
		}
		catch (FrameworkCheckedException ex)
		{	
			if ("MobileNumUniqueException".equals(ex.getMessage()))
			{		
				request.setAttribute("message","User with same mobile number already exists. Kindly assigned back or close request");
			}
			else if ("PasswordConfirmPasswordMissMatchException".equals(ex.getMessage()))
			{
				request.setAttribute("message","Passwords provided do not match. Kindly assigned back or close request");
			}
			else if ("ConstraintViolationException".equals(ex.getMessage()) || "DataIntegrityViolationException".equals(ex.getMessage()))
			{
				request.setAttribute("message","User with same User ID already exists. Kindly assigned back or close request");
			}
			
			else if (ex.getMessage().contains("Employee ID already exist"))
			{
				request.setAttribute("message","User with same Employee ID already exists. Kindly assigned back or close request");
			}
			else{
				if(null!=ex.getMessage())
					request.setAttribute("message",ex.getMessage().replaceAll("<br>",""));
				else
					request.setAttribute("message",MessageUtil.getMessage("6075"));
			}
			
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}
		catch (Exception ex)
		{	
			request.setAttribute("message",MessageUtil.getMessage("6075"));
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}
	
		request.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
	    modelAndView = super.showForm(request, response, errors);	
		return modelAndView;	
	}

	public void setUserManagementManager(UserManagementManager userManagementManager) {
		this.userManagementManager = userManagementManager;
	}

	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}
	
}