package com.inov8.microbank.webapp.action.portal.delinkrelinkpaymentmode;

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
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.RelinkDelinkPaymentModeRefDataModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.delinkrelinkpaymentmode.DelinkRelinkPaymentModeManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class DelinkRelinkPaymentModeFormController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( DelinkRelinkPaymentModeFormController.class );
	private DelinkRelinkPaymentModeManager delinkRelinkPaymentModeManager;
	private FinancialIntegrationManager financialIntegrationManager;
		
	public DelinkRelinkPaymentModeFormController() {
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
			RelinkDelinkPaymentModeRefDataModel relinkDelinkPaymentModeRefDataModel = (RelinkDelinkPaymentModeRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			request.setAttribute("mfsId",relinkDelinkPaymentModeRefDataModel.getMfsId());		
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
		BankModel bankModel = new BankModel();
		Boolean isBank = false;
		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
		baseWrapperBank.setBasePersistableModel(bankModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
		if (abstractFinancialInstitution.isVeriflyLite()) {
			isBank = true;
		}
		Boolean isRelink = false;
		String isDeleted = "";
		String message=null;
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try
		{				
			Long smartMoneyAccountId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "smAcId")));
			Long appUserId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "apUsrId")));
			// getting log information from the request
			Long useCaseId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_USECASE_ID);
			Long actionId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_ACTION_ID);

			// putting log information into wrapper for further used

			baseWrapper.putObject(DelinkRelinkPaymentModeManager.KEY_SMART_MONEY_ACC_ID, smartMoneyAccountId);
			baseWrapper.putObject(DelinkRelinkPaymentModeManager.KEY_APP_USER_ID, appUserId);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);

			isDeleted = ServletRequestUtils.getStringParameter(request, "isDeleted");
			boolean isRetailerOrDistributor = this.delinkRelinkPaymentModeManager.isRetailerOrDistributor(appUserId);
			if (isDeleted != null && isDeleted.equals("true")) {
				if (isRetailerOrDistributor) {
					logger.info("Executing delete for allpay");
					baseWrapper = this.delinkRelinkPaymentModeManager.allpayDeleteAccount(baseWrapper);
				} else {
					logger.info("Executing delete for MFS");
					baseWrapper = this.delinkRelinkPaymentModeManager.deleteAccount(baseWrapper);
				}
				message =  getText("smartMoneyAccountModel.delete.success", request.getLocale());
			} 
			else 
			{
				String status = "De-Linked";
				if (isRetailerOrDistributor) {
					baseWrapper = this.delinkRelinkPaymentModeManager.updateAllPayDelinkRelinkVeriflyPin(baseWrapper);
				} else {
					baseWrapper = this.delinkRelinkPaymentModeManager.updateDelinkRelinkVeriflyPin(baseWrapper);
				}
				isRelink = (Boolean) baseWrapper.getObject(DelinkRelinkPaymentModeManager.KEY_IS_RELINK);
				String moreThanTwoAccounts = (String) baseWrapper.getObject("moreThanTwoAccounts");
				
					if (isRelink.booleanValue()) // relink scenerio
					{
						status = "Re-Linked";
					} else // delink scenario
					{
						status = "De-Linked";
					}
				if ("y".equals(moreThanTwoAccounts)) {
					throw new FrameworkCheckedException(getText("customer.allpayAccountLinkingError", request.getLocale()));
				} else {
					message = getText("smartMoneyAccountModel.success",status, request.getLocale());
				}

			}

		}
		catch (FrameworkCheckedException ex)
		{	
			LOGGER.error("Exception occured while resending sms : " +ex.getMessage());
			request.setAttribute("message",ex.getMessage());
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
	protected ModelAndView onEscalate(HttpServletRequest request,HttpServletResponse response, Object command, BindException errors) throws Exception {
	
		ModelAndView modelAndView = null;
		ActionAuthorizationModel model = (ActionAuthorizationModel) command;
		String message=null;
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
				RelinkDelinkPaymentModeRefDataModel relinkDelinkPaymentModeRefDataModel = (RelinkDelinkPaymentModeRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
				
				BankModel bankModel = new BankModel();
				Boolean isBank = false;
				BaseWrapper baseWrapperBank = new BaseWrapperImpl();
				bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
				baseWrapperBank.setBasePersistableModel(bankModel);
				AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
				if (abstractFinancialInstitution.isVeriflyLite()) {
					isBank = true;
				}
				Boolean isRelink = false;
				String isDeleted = relinkDelinkPaymentModeRefDataModel.getIsDelete();
											
				Long smartMoneyAccountId = new Long(EncryptionUtil.decryptWithDES(relinkDelinkPaymentModeRefDataModel.getSmaAccountId()));
				Long appUserId = new Long(EncryptionUtil.decryptWithDES(relinkDelinkPaymentModeRefDataModel.getEncryptedAppUserId()));
				// getting log information from the request
				Long useCaseId = relinkDelinkPaymentModeRefDataModel.getUsecaseId();
				Long actionId = relinkDelinkPaymentModeRefDataModel.getActionId();

				// putting log information into wrapper for further used

				baseWrapper.putObject(DelinkRelinkPaymentModeManager.KEY_SMART_MONEY_ACC_ID, smartMoneyAccountId);
				baseWrapper.putObject(DelinkRelinkPaymentModeManager.KEY_APP_USER_ID, appUserId);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);

				boolean isRetailerOrDistributor = this.delinkRelinkPaymentModeManager.isRetailerOrDistributor(appUserId);
				if (isDeleted != null && isDeleted.equals("true")) {
					if (isRetailerOrDistributor) {
						logger.info("Executing delete for allpay");
						baseWrapper = this.delinkRelinkPaymentModeManager.allpayDeleteAccount(baseWrapper);
					} else {
						logger.info("Executing delete for MFS");
						baseWrapper = this.delinkRelinkPaymentModeManager.deleteAccount(baseWrapper);
					}
					message =  getText("smartMoneyAccountModel.delete.success", request.getLocale());
				} 
				else 
				{
					String status = "De-Linked";
					if (isRetailerOrDistributor) {
						baseWrapper = this.delinkRelinkPaymentModeManager.updateAllPayDelinkRelinkVeriflyPin(baseWrapper);
					} else {
						baseWrapper = this.delinkRelinkPaymentModeManager.updateDelinkRelinkVeriflyPin(baseWrapper);
					}
					isRelink = (Boolean) baseWrapper.getObject(DelinkRelinkPaymentModeManager.KEY_IS_RELINK);
					String moreThanTwoAccounts = (String) baseWrapper.getObject("moreThanTwoAccounts");
					
						if (isRelink.booleanValue()) // relink scenerio
						{
							status = "Re-Linked";
						} else // delink scenario
						{
							status = "De-Linked";
						}
					if ("y".equals(moreThanTwoAccounts)) {
						throw new FrameworkCheckedException(getText("customer.allpayAccountLinkingError", request.getLocale()));
					} else {
						message = getText("smartMoneyAccountModel.success",status, request.getLocale());
					}

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
		LOGGER.error("Exception occured while resending sms : " +ex.getMessage());
		ex.printStackTrace();
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
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
		ModelAndView modelAndView = null;
		String message=null;
		try
		{	
			String smartMoneyAccountId = ServletRequestUtils.getStringParameter(request, "smAcId");
			String appUserId = ServletRequestUtils.getStringParameter(request, "apUsrId");
			Long useCaseId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_USECASE_ID);
			Long actionId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_ACTION_ID);
			String isDeleted = ServletRequestUtils.getStringParameter(request, "isDeleted");
			String mfsId = ServletRequestUtils.getStringParameter(request, "mfsId");
			String comments= ServletRequestUtils.getStringParameter(request,"comments");
						
			XStream xstream = new XStream();
			RelinkDelinkPaymentModeRefDataModel relinkDelinkPaymentModeRefDataModel = new RelinkDelinkPaymentModeRefDataModel(mfsId,useCaseId,appUserId,smartMoneyAccountId,actionId,isDeleted,comments);
			String refDataModelString= xstream.toXML(relinkDelinkPaymentModeRefDataModel);
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(useCaseId);
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(useCaseId,new Long(0));
				
			if(nextAuthorizationLevel.intValue()<1){
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				BankModel bankModel = new BankModel();
				Boolean isBank = false;
				BaseWrapper baseWrapperBank = new BaseWrapperImpl();
				bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
				baseWrapperBank.setBasePersistableModel(bankModel);
				AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
				if (abstractFinancialInstitution.isVeriflyLite()) {
					isBank = true;
				}
				Boolean isRelink = false;
				
				baseWrapper.putObject(DelinkRelinkPaymentModeManager.KEY_SMART_MONEY_ACC_ID,new Long(EncryptionUtil.decryptWithDES(smartMoneyAccountId)));
				baseWrapper.putObject(DelinkRelinkPaymentModeManager.KEY_APP_USER_ID, new Long(EncryptionUtil.decryptWithDES(appUserId)));
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);

				boolean isRetailerOrDistributor = this.delinkRelinkPaymentModeManager.isRetailerOrDistributor(new Long(EncryptionUtil.decryptWithDES(appUserId)));
				if (isDeleted != null && isDeleted.equals("true")) {
					if (isRetailerOrDistributor) {
						logger.info("Executing delete for allpay");
						baseWrapper = this.delinkRelinkPaymentModeManager.allpayDeleteAccount(baseWrapper);
					} else {
						logger.info("Executing delete for MFS");
						baseWrapper = this.delinkRelinkPaymentModeManager.deleteAccount(baseWrapper);
					}
					message =  getText("smartMoneyAccountModel.delete.success", request.getLocale());
				} 
				else 
				{
					String status = "De-Linked";
					if (isRetailerOrDistributor) {
						baseWrapper = this.delinkRelinkPaymentModeManager.updateAllPayDelinkRelinkVeriflyPin(baseWrapper);
					} else {
						baseWrapper = this.delinkRelinkPaymentModeManager.updateDelinkRelinkVeriflyPin(baseWrapper);
					}
					isRelink = (Boolean) baseWrapper.getObject(DelinkRelinkPaymentModeManager.KEY_IS_RELINK);
					String moreThanTwoAccounts = (String) baseWrapper.getObject("moreThanTwoAccounts");
					
						if (isRelink.booleanValue()) // relink scenerio
						{
							status = "Re-Linked";
						} else // delink scenario
						{
							status = "De-Linked";
						}
					if ("y".equals(moreThanTwoAccounts)) {
						throw new FrameworkCheckedException(getText("customer.allpayAccountLinkingError", request.getLocale()));
					} else {
						message = getText("smartMoneyAccountModel.success",status, request.getLocale());
					}

				}
				
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
			if(ex.getMessage().contains("already exist"))
				request.setAttribute("message",ex.getMessage());
			else
			request.setAttribute("message",ex.getMessage());
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
		String message =null;
		try{
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());			
			UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
			XStream xstream = new XStream();
			RelinkDelinkPaymentModeRefDataModel relinkDelinkPaymentModeRefDataModel = (RelinkDelinkPaymentModeRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			
			BankModel bankModel = new BankModel();
			Boolean isBank = false;
			BaseWrapper baseWrapperBank = new BaseWrapperImpl();
			bankModel.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
			baseWrapperBank.setBasePersistableModel(bankModel);
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
			if (abstractFinancialInstitution.isVeriflyLite()) {
				isBank = true;
			}
			Boolean isRelink = false;
			String isDeleted = relinkDelinkPaymentModeRefDataModel.getIsDelete();;
										
			Long smartMoneyAccountId = new Long(EncryptionUtil.decryptWithDES(relinkDelinkPaymentModeRefDataModel.getSmaAccountId()));
			Long appUserId = new Long(EncryptionUtil.decryptWithDES(relinkDelinkPaymentModeRefDataModel.getEncryptedAppUserId()));
			// getting log information from the request
			Long useCaseId = relinkDelinkPaymentModeRefDataModel.getUsecaseId();
			Long actionId = relinkDelinkPaymentModeRefDataModel.getActionId();

			// putting log information into wrapper for further used

			baseWrapper.putObject(DelinkRelinkPaymentModeManager.KEY_SMART_MONEY_ACC_ID, smartMoneyAccountId);
			baseWrapper.putObject(DelinkRelinkPaymentModeManager.KEY_APP_USER_ID, appUserId);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);	
			baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
			baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
			baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());

			boolean isRetailerOrDistributor = this.delinkRelinkPaymentModeManager.isRetailerOrDistributor(appUserId);
			if (isDeleted != null && isDeleted.equals("true")) {
				if (isRetailerOrDistributor) {
					logger.info("Executing delete for allpay");
					baseWrapper = this.delinkRelinkPaymentModeManager.allpayDeleteAccount(baseWrapper);
				} else {
					logger.info("Executing delete for MFS");
					baseWrapper = this.delinkRelinkPaymentModeManager.deleteAccount(baseWrapper);
				}
				message =  getText("smartMoneyAccountModel.delete.success", request.getLocale());
			} 
			else 
			{
				String status = "De-Linked";
				if (isRetailerOrDistributor) {
					baseWrapper = this.delinkRelinkPaymentModeManager.updateAllPayDelinkRelinkVeriflyPin(baseWrapper);
				} else {
					baseWrapper = this.delinkRelinkPaymentModeManager.updateDelinkRelinkVeriflyPin(baseWrapper);
				}
				isRelink = (Boolean) baseWrapper.getObject(DelinkRelinkPaymentModeManager.KEY_IS_RELINK);
				String moreThanTwoAccounts = (String) baseWrapper.getObject("moreThanTwoAccounts");
				
					if (isRelink.booleanValue()) // relink scenerio
					{
						status = "Re-Linked";
					} else // delink scenario
					{
						status = "De-Linked";
					}
				if ("y".equals(moreThanTwoAccounts)) {
					throw new FrameworkCheckedException(getText("customer.allpayAccountLinkingError", request.getLocale()));
				} else {
					message = getText("smartMoneyAccountModel.success",status, request.getLocale());
				}

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
		map.put("message",message);
	    modelAndView = new ModelAndView(this.getSuccessView(),map);	
		return modelAndView;	
	}

	public void setDelinkRelinkPaymentModeManager(
			DelinkRelinkPaymentModeManager delinkRelinkPaymentModeManager) {
		this.delinkRelinkPaymentModeManager = delinkRelinkPaymentModeManager;
	}

	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}
	
}
