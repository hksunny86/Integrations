package com.inov8.microbank.webapp.action.allpaymodule;

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
import com.inov8.microbank.common.model.AllpayUserInfoListViewModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ResendSmsRefDataModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ResetPortalPasswordRefDataModel;
import com.inov8.microbank.common.model.portal.linkpaymentmodemodule.LinkPaymentModeModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderService;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class AllpayLinkPaymentModeAuthorizationController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( AllpayLinkPaymentModeAuthorizationController.class );
	private LinkPaymentModeManager	linkPaymentModeManager;
	private FinancialIntegrationManager financialIntegrationManager;
		
	public AllpayLinkPaymentModeAuthorizationController() {
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
			LinkPaymentModeModel LinkPaymentModeModel = (LinkPaymentModeModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			request.setAttribute("mfsId",LinkPaymentModeModel.getMfsId());
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
		BaseWrapper baseWrapper = new BaseWrapperImpl();
	try{
		ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
		baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
		boolean isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());
		long currentUserId= UserUtils.getCurrentUser().getAppUserId();

		UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
		
	
		if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
			if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().longValue()==currentUserId)){
				throw new FrameworkCheckedException("You are not authorized to update action status.");
			}
			
			long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(PortalConstants.LINK_PAYMENT_MODE_USECASE_ID,actionAuthorizationModel.getEscalationLevel());
			if(nextAuthorizationLevel<1){
				
				XStream xstream = new XStream();
				LinkPaymentModeModel linkPaymentModeModel = (LinkPaymentModeModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
				
				long useCaseId = 0;
				long actionId = 0;
										
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.LINK_PAYMENT_MODE_USECASE_ID);
			    baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
				baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
				baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
				baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
				///////////////////				
//				check is an account other than OLA is already linked 
				if (!this.linkPaymentModeManager.isFirstAccountOtherThanOla(linkPaymentModeModel.getMfsId())){
					String messageString = MessageUtil.getMessage(
							"customer.allpayAccountLinkingError");
				
					request.setAttribute("message", messageString);
					request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
					return super.showForm(request, response, errors);
				}
				if (request.getParameter(PortalConstants.KEY_USECASE_ID) != null)
				{
					useCaseId = Long.parseLong(request.getParameter(PortalConstants.KEY_USECASE_ID));
					System.out.println("usecase id " + useCaseId);
					baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);

				}
				if (request.getParameter(PortalConstants.KEY_ACTION_ID) != null)
				{
					actionId = Long.parseLong(request.getParameter(PortalConstants.KEY_ACTION_ID));
					System.out.println("action id " + actionId);
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
				}

				baseWrapper.putObject("linkPaymentModeModel", linkPaymentModeModel);

				baseWrapper = linkPaymentModeManager.createLinkPaymentMode(baseWrapper);

				if (baseWrapper != null)
				{

					String sucessMessage = "", failureMessage = "";
					if (baseWrapper.getObject("ErrorMessage") != null)
					{
						failureMessage = (String) baseWrapper.getObject("ErrorMessage"); // verifly   message
		
						request.setAttribute("message", failureMessage);
						request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
						return super.showForm(request, response, errors);

					}
					else
					{
						sucessMessage = getText("linkpaymentmodemodule.paymend.mode.linked.success", request.getLocale());
					}
					
					//if appuserid exist mean we need to navigate to mnonewmfsaccount page
					String eappUserId = ServletRequestUtils.getStringParameter(request, "eappUserId"); 
					request.setAttribute(PortalConstants.KEY_APP_USER_ID, eappUserId);
					String returnView = "";
					request.setAttribute("message", sucessMessage);
				}

				else
				{
				
					request.setAttribute("message","Account cannot be linked. Kindly contact administrator.");
					request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
					return super.showForm(request, response, errors);
				}
							
				///////////////////		
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
		String errorMessage = ex.getMessage();
		
		if (baseWrapper.getObject("ErrMessage") != null)
		{
			errorMessage = (String) baseWrapper.getObject("ErrMessage"); 
		}
		
		if(ex != null && ex.getMessage() != null) {
			
			if(ex.getMessage().equals("implementationNotSupportedException")){
				errorMessage = getText("linkpaymentmodemodule.featureNotSupported", request.getLocale());
			}
			
			if(ex.getMessage().equals("linkPaymentMode.customerprofiledoesnotexist")){
				errorMessage = getText("linkPaymentMode.customerprofiledoesnotexist", request.getLocale());
			}
			
			if(ex.getMessage().equals(WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED)){
				errorMessage = WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED;
			}

			if(ex.getMessage().equals("This Payment Mode is already linked.")){
				errorMessage = "This Payment Mode is already linked.";
			}
			if(ex.getMessage().contains("already exist"))
				errorMessage = ex.getMessage();
			
		}
		
		if (errorMessage != null && errorMessage.equalsIgnoreCase("Service unavailable due to technical difficulties, please try again or contact service provider.")) {
			
		} else if(errorMessage != null && errorMessage.equalsIgnoreCase("")) {
			
			errorMessage = "Smart Money Account Could Not Be Saved";
		}
		
		
		LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
		request.setAttribute("message", errorMessage);
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
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try{
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());	
			baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
			UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
	
			XStream xstream = new XStream();
			LinkPaymentModeModel linkPaymentModeModel = (LinkPaymentModeModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			
			long useCaseId = 0;
			long actionId = 0;
									
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.LINK_PAYMENT_MODE_USECASE_ID);
		    baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
			baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
			baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
			baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
			///////////////////				
//			check is an account other than OLA is already linked 
			if (!this.linkPaymentModeManager.isFirstAccountOtherThanOla(linkPaymentModeModel.getMfsId())){
				String messageString = MessageUtil.getMessage(
						"customer.allpayAccountLinkingError");
			
				request.setAttribute("message", messageString);
				request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
				return super.showForm(request, response, errors);
			}
			if (request.getParameter(PortalConstants.KEY_USECASE_ID) != null)
			{
				useCaseId = Long.parseLong(request.getParameter(PortalConstants.KEY_USECASE_ID));
				System.out.println("usecase id " + useCaseId);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);

			}
			if (request.getParameter(PortalConstants.KEY_ACTION_ID) != null)
			{
				actionId = Long.parseLong(request.getParameter(PortalConstants.KEY_ACTION_ID));
				System.out.println("action id " + actionId);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
			}

			baseWrapper.putObject("linkPaymentModeModel", linkPaymentModeModel);

			baseWrapper = linkPaymentModeManager.createLinkPaymentMode(baseWrapper);

			if (baseWrapper != null)
			{

				String sucessMessage = "", failureMessage = "";
				if (baseWrapper.getObject("ErrorMessage") != null)
				{
					failureMessage = (String) baseWrapper.getObject("ErrorMessage"); // verifly   message
	
					request.setAttribute("message", failureMessage);
					request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
					return super.showForm(request, response, errors);

				}
				else
				{
					sucessMessage = getText("linkpaymentmodemodule.paymend.mode.linked.success", request.getLocale());
				}
				
				//if appuserid exist mean we need to navigate to mnonewmfsaccount page
				String eappUserId = ServletRequestUtils.getStringParameter(request, "eappUserId"); 
				request.setAttribute(PortalConstants.KEY_APP_USER_ID, eappUserId);
				String returnView = "";
				request.setAttribute("message", sucessMessage);
			}

			else
			{
			
				request.setAttribute("message","Account cannot be linked. Kindly contact administrator.");
				request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
				return super.showForm(request, response, errors);
			}
						
			//////
			resolveWithIntimation(actionAuthorizationModel,model, usecaseModel,request);
			
		}
		catch (FrameworkCheckedException ex)
		{			
			String errorMessage = ex.getMessage();
			
			if (baseWrapper.getObject("ErrMessage") != null)
			{
				errorMessage = (String) baseWrapper.getObject("ErrMessage"); 
			}
			
			if(ex != null && ex.getMessage() != null) {
				
				if(ex.getMessage().equals("implementationNotSupportedException")){
					errorMessage = getText("linkpaymentmodemodule.featureNotSupported", request.getLocale());
				}
				
				if(ex.getMessage().equals("linkPaymentMode.customerprofiledoesnotexist")){
					errorMessage = getText("linkPaymentMode.customerprofiledoesnotexist", request.getLocale());
				}
				
				if(ex.getMessage().equals(WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED)){
					errorMessage = WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED;
				}

				if(ex.getMessage().equals("This Payment Mode is already linked.")){
					errorMessage = "This Payment Mode is already linked.";
				}
				if(ex.getMessage().contains("already exist"))
					errorMessage = ex.getMessage();
				
			}
			
			if (errorMessage != null && errorMessage.equalsIgnoreCase("Service unavailable due to technical difficulties, please try again or contact service provider.")) {
				
			} else if(errorMessage != null && errorMessage.equalsIgnoreCase("")) {
				
				errorMessage = "Smart Money Account Could Not Be Saved";
			}
			
			
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			request.setAttribute("message", errorMessage);
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
		request.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
	    modelAndView = super.showForm(request, response, errors);	
		return modelAndView;	
	}

	public void setLinkPaymentModeManager(
			LinkPaymentModeManager linkPaymentModeManager) {
		this.linkPaymentModeManager = linkPaymentModeManager;
	}
	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}
	
}