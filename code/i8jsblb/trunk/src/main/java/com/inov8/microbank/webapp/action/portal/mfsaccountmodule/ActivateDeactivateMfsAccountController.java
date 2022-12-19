package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

/*
 * Author : Hassan Javaid
 * Date   : 07-07-2014
 * Module : Action Authorization
 * Project: Mircobank	
 * */
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.ActivateDeactivateMfsAccountRefDataModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


public class ActivateDeactivateMfsAccountController extends AdvanceAuthorizationFormController  {
	
	private static final Logger LOGGER = Logger.getLogger( ActivateDeactivateMfsAccountController.class );
	private MfsAccountManager	mfsAccountManager;
	private AppUserManager	appUserManager;
	private ComplaintManager complaintManager;
	private SmartMoneyAccountManager smartMoneyAccountManager ;

	public ActivateDeactivateMfsAccountController() {
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
			ActivateDeactivateMfsAccountRefDataModel activateDeactivateMfsAccountRefDataModel = (ActivateDeactivateMfsAccountRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			request.setAttribute("mfsId",activateDeactivateMfsAccountRefDataModel.getMfsId());
			request.setAttribute("isAgent",activateDeactivateMfsAccountRefDataModel.getIsAgent());
			/*request.setAttribute("isHandler",activateDeactivateMfsAccountRefDataModel.getIsHandler());
			request.setAttribute("usecaseId",activateDeactivateMfsAccountRefDataModel.getUsecaseId());*/
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

	private Long getPaymentModeId(String acType, String action, Boolean isAgent, Boolean isHandler, Long id)
	{
		Long paymentModeId = null;
		if(action == null && acType == null && (isAgent || isHandler) && id != null)
			paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
		else if(action != null && acType != null && acType.equals("HRA"))
			paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
		else
			paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;

		return paymentModeId;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object, BindException errors) throws Exception {
		ModelAndView modelAndView = null;
		Long id = null;
		String appUserId = null;
		String mfsId = null;
		String comments = null;
		boolean isLockUnlock = false;
		Long usecaseId = null;
		Long actionId = null;
		String acType = null;
		Long paymentModeId = null;
		SmartMoneyAccountModel smartMoneyAccountModel = null;
		AppUserModel appUserModel = null;
		String action = null;
		String strUseCaseId= ServletRequestUtils.getStringParameter(request, "usecaseId");
		long longUseCaseId = Long.parseLong(strUseCaseId);
		if(longUseCaseId == PortalConstants.BLOCK_CUSTOMER_USECASE_ID)
			action = "BLOCK";
		else if(longUseCaseId == PortalConstants.UNBLOCK_CUSTOMER_USECASE_ID)
			action = "UN-BLOCK";
		else if(longUseCaseId == PortalConstants.REACTIVATE_CUSTOMER_USECASE_ID)
			action = "ACTIVE";
		else if(longUseCaseId == PortalConstants.DEACTIVATE_CUSTOMER_USECASE_ID)
			action = "DE-ACTIVE";

		Boolean isAgent = Boolean.parseBoolean(ServletRequestUtils.getStringParameter(request, "isAgent"));
		Boolean isHandler = Boolean.parseBoolean(ServletRequestUtils.getStringParameter(request, "isHandler"));
		try
		{
			appUserId = ServletRequestUtils.getStringParameter(request, "appUserId");
			acType=ServletRequestUtils.getStringParameter(request, "acType");
			if (null != appUserId && appUserId.trim().length() > 0 )
				id = new Long(EncryptionUtil.decryptForAppUserId(appUserId));
			paymentModeId = this.getPaymentModeId(acType,action,isAgent,isHandler,id);

			mfsId = ServletRequestUtils.getStringParameter(request, "mfsId");
			comments = ServletRequestUtils.getStringParameter(request, "comments");

			if(action == null)
				usecaseId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_USECASE_ID);
			else
				usecaseId = this.setUseCaseId(action);
			actionId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_ACTION_ID);
			String lockUnlockVal = ServletRequestUtils.getStringParameter(request, "isLockUnlock");
			Long accountStateId = 1L;

			if(lockUnlockVal !=null && lockUnlockVal.equals("true")){
				isLockUnlock = true;
			}

			if (null != appUserId && appUserId.trim().length() > 0 )
			{
				id = new Long(EncryptionUtil.decryptForAppUserId(appUserId));

				if (logger.isDebugEnabled())
				{
					logger.debug("id is not null....retrieving object from DB and then updating it");
				}

				BaseWrapper baseWrapper = this.prepareBaseWrapperForAction(acType,action,new Boolean(isAgent),new Boolean(isHandler),id,usecaseId,actionId,isLockUnlock,mfsId);

				smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();

				if(smartMoneyAccountModel == null)
					throw new FrameworkCheckedException("noExist");

				baseWrapper = this.mfsAccountManager.activateDeactivateMfsAccount(baseWrapper);
				Boolean isActive = (Boolean)baseWrapper.getObject(MfsAccountManager.KEY_IS_ACTIVE);
				Date updationTime = (Date)baseWrapper.getObject(PortalConstants.KEY_UPDATION_TIME);
				if(paymentModeId == null)
					paymentModeId = (Long) baseWrapper.getObject("paymentModeId");
				createComplaint(isActive,isLockUnlock,id,comments,updationTime,paymentModeId);
			}
		}
		catch (FrameworkCheckedException ex)
		{
			LOGGER.error("Exception occured : " +ex.getMessage());
			if("noExist".equals(ex.getMessage()))
				if(acType == null)
					request.setAttribute("message","Account does not Exist");
				else
					request.setAttribute("message",acType+" Account does not Exist");
			else
				request.setAttribute("message","Request cannot be processed. Kindly contact administrator.");
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(request, response, errors);
		}
		catch (Exception ex)
		{
			LOGGER.error("Exception occured : " +ex.getMessage());
			request.setAttribute("message",MessageUtil.getMessage("6075"));
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			return super.showForm(request, response, errors);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status",IssueTypeStatusConstantsInterface.SUCCESS);
		map.put("message","Record updated sucessfully");
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
		if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){

			if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().longValue()==currentUserId)){
				throw new FrameworkCheckedException("You are not authorized to update action status.");
			}
			
			long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(actionAuthorizationModel.getUsecaseId(),actionAuthorizationModel.getEscalationLevel());
			if(nextAuthorizationLevel<1){
				
				XStream xstream = new XStream();
				ActivateDeactivateMfsAccountRefDataModel activateDeactivateMfsAccountRefDataModel = (ActivateDeactivateMfsAccountRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
					
				String appUserId = activateDeactivateMfsAccountRefDataModel.getEncryptedAppUserId();
				Long id = null;
				boolean isLockUnlock = false;
				Long usecaseId = new Long(activateDeactivateMfsAccountRefDataModel.getUsecaseId());
				Long actionId = new Long(activateDeactivateMfsAccountRefDataModel.getActionId());
				String comments = activateDeactivateMfsAccountRefDataModel.getComments();
				String lockUnlockVal =  activateDeactivateMfsAccountRefDataModel.getIslockUnlock();
				Long paymentModeId = activateDeactivateMfsAccountRefDataModel.getPaymentModeId();
				String isAgent = activateDeactivateMfsAccountRefDataModel.getIsAgent();
				String isHandler = activateDeactivateMfsAccountRefDataModel.getIsHandler();
				String mfsId = activateDeactivateMfsAccountRefDataModel.getMfsId();
				String acType = activateDeactivateMfsAccountRefDataModel.getAccountType();
				String action = activateDeactivateMfsAccountRefDataModel.getAction();

				if(lockUnlockVal !=null && lockUnlockVal.equals("true")){
					isLockUnlock = true;
				}

				if (null != appUserId && appUserId.trim().length() > 0 )
				{
					id = new Long(EncryptionUtil.decryptForAppUserId(appUserId));
					
					if (logger.isDebugEnabled())
					{
						logger.debug("id is not null....retrieving object from DB and then updating it");
					}
					baseWrapper = this.prepareBaseWrapperForAction(acType,action,new Boolean(isAgent),new Boolean(isHandler),id,usecaseId,actionId,isLockUnlock,mfsId);

					baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
					baseWrapper = this.mfsAccountManager.activateDeactivateMfsAccount(baseWrapper);
			
					Boolean isActive = (Boolean)baseWrapper.getObject(MfsAccountManager.KEY_IS_ACTIVE);
					Date updationTime = (Date)baseWrapper.getObject(PortalConstants.KEY_UPDATION_TIME);
					if(paymentModeId == null)
						paymentModeId = (Long) baseWrapper.getObject("paymentModeId");
					createComplaint(isActive,isLockUnlock,id,comments,updationTime,paymentModeId);
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
	map.put("status",IssueTypeStatusConstantsInterface.SUCCESS);
    modelAndView = new ModelAndView(this.getSuccessView(),map);	
    return modelAndView; 
	}

	private BaseWrapper prepareBaseWrapperForAction(String acType, String action, Boolean isAgent, Boolean isHandler, Long id,Long usecaseId,
													Long actionId,Boolean isLockUnlock,String mfsId) throws FrameworkCheckedException
	{
		BaseWrapper baseWrapper = null;
		try
		{
			baseWrapper = new BaseWrapperImpl();
			AppUserModel appUserModel=appUserManager.loadAppUser(id);
			Long paymentModeId = this.getPaymentModeId(acType,action,new Boolean(isAgent),new Boolean(isHandler),id);
			SmartMoneyAccountModel smartMoneyAccountModel=smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel,paymentModeId);
			if(smartMoneyAccountModel == null && (isAgent || isHandler))
			{
				paymentModeId = PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT;
				smartMoneyAccountModel=smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel,paymentModeId);
			}

			if(action != null && action.equalsIgnoreCase("ACTIVE") && smartMoneyAccountModel == null)
				smartMoneyAccountModel = smartMoneyAccountManager.getInActiveSMA(appUserModel,paymentModeId,OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE);

			if(smartMoneyAccountModel != null)
				smartMoneyAccountModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);

			UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
			userDeviceAccountsModel.setAppUserId(id);
			baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, usecaseId);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
			baseWrapper.putObject("isLockUnlock", new Boolean(isLockUnlock));
			baseWrapper.putObject("mfsId", mfsId);
			baseWrapper.putObject("paymentModeId", paymentModeId);
			baseWrapper.putObject("userDeviceAccountsModel", userDeviceAccountsModel);
			baseWrapper.putObject("appUserModel", appUserModel);
			baseWrapper.putObject("acType", acType);
			baseWrapper.putObject("action", action);
		}
		catch (FrameworkCheckedException ex)
		{
			throw new FrameworkCheckedException("An unexpected error occurred.");
		}
		return baseWrapper;
	}

	private Long setUseCaseId(String action){
		Long usecaseId = null;
		if(action != null)
		{
			if(action.equals("ACTIVE"))
				usecaseId = 1106L;
			else if(action.equals("DE-ACTIVE"))
				usecaseId = 1005L;
			else if(action.equals("BLOCK"))
				usecaseId = 1036L;
			else if(action.equals("UN-BLOCK"))
				usecaseId = 1110L;
		}
		return usecaseId;
	}

	@Override
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
		ModelAndView modelAndView = null;
		String message=null;
		try
		{	
			String appUserIdParam = ServletRequestUtils.getStringParameter(request, "appUserId");
			String action = ServletRequestUtils.getStringParameter(request, "action");
			Long usecaseId = null;
			if(action == null)
				usecaseId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_USECASE_ID);
			else
				usecaseId = this.setUseCaseId(action);
			Long actionId = ServletRequestUtils.getLongParameter(request, PortalConstants.KEY_ACTION_ID);
			String lockUnlockVal = ServletRequestUtils.getStringParameter(request, "isLockUnlock");
			String isAgent = ServletRequestUtils.getStringParameter(request, "isAgent");
			String isHandler = ServletRequestUtils.getStringParameter(request, "isHandler");
			String mfsId = ServletRequestUtils.getStringParameter(request, "mfsId");
			String comments =  ServletRequestUtils.getStringParameter(request, "comments");
			String acType = ServletRequestUtils.getStringParameter(request, "acType");
			Long id = null;
			Long paymentModeId = null;
			BaseWrapper baseWrapper = null;
			boolean isLockUnlock = false;
			if(lockUnlockVal !=null && lockUnlockVal.equals("true"))
				isLockUnlock = true;
			if (null != appUserIdParam && appUserIdParam.trim().length() > 0 )
			{
				id = new Long(EncryptionUtil.decryptForAppUserId(appUserIdParam));
				baseWrapper = this.prepareBaseWrapperForAction(acType,action,new Boolean(isAgent),new Boolean(isHandler),id,usecaseId,actionId,isLockUnlock,mfsId);
				paymentModeId = (Long) baseWrapper.getObject("paymentModeId");
			}

			XStream xstream = new XStream();
			ActivateDeactivateMfsAccountRefDataModel activateDeactivateMfsAccountRefDataModel = new ActivateDeactivateMfsAccountRefDataModel(mfsId,usecaseId,appUserIdParam,actionId,lockUnlockVal,isAgent
					,null,null,comments,paymentModeId,acType,action,isHandler);
			String refDataModelString= xstream.toXML(activateDeactivateMfsAccountRefDataModel);
						
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(usecaseId);
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(usecaseId,new Long(0));
			if(nextAuthorizationLevel.intValue()<1){

				if (null != appUserIdParam && appUserIdParam.trim().length() > 0 )
				{
					id = new Long(EncryptionUtil.decryptForAppUserId(appUserIdParam));
					
					if (logger.isDebugEnabled())
					{
						logger.debug("id is not null....retrieving object from DB and then updating it");
					}

					baseWrapper = this.mfsAccountManager.activateDeactivateMfsAccount(baseWrapper);
			
					Boolean isActive = (Boolean)baseWrapper.getObject(MfsAccountManager.KEY_IS_ACTIVE);
					Date updationTime = (Date)baseWrapper.getObject(PortalConstants.KEY_UPDATION_TIME);
					if(paymentModeId == null)
						paymentModeId = (Long) baseWrapper.getObject("paymentModeId");
					createComplaint(isActive,isLockUnlock,id,comments,updationTime,paymentModeId);
				}			
				
				
				////////////////////////////////// Generic /////
				Long actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel, comments, refDataModelString,null,usecaseModel,null,request);
				message = "Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId;
				
			}
			else
			{
				baseWrapper.putObject("model",activateDeactivateMfsAccountRefDataModel);
				Long actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel, comments, refDataModelString,null,usecaseModel.getUsecaseId(),mfsId,false,null,request);
				message = "Action is pending for approval against reference Action ID : "+actionAuthorizationId;
			}
		
		}
		catch (FrameworkCheckedException ex)
		{	
			LOGGER.error("Exception occured : " +ex.getMessage());
			if(ex.getMessage().contains("already exist"))
				request.setAttribute("message",ex.getMessage());
			else
			request.setAttribute("message","Request cannont be processed at the moment. Kindly contact administrator.");
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}
		catch (Exception ex)
		{	
			LOGGER.error("Exception occured : " +ex.getMessage());
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
			ActivateDeactivateMfsAccountRefDataModel activateDeactivateMfsAccountRefDataModel = (ActivateDeactivateMfsAccountRefDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
				
			String appUserId = activateDeactivateMfsAccountRefDataModel.getEncryptedAppUserId();
			Long id = null;
			if (null != appUserId && appUserId.trim().length() > 0 )
				id = new Long(EncryptionUtil.decryptForAppUserId(appUserId));
			boolean isLockUnlock = false;
			Long usecaseId = new Long(activateDeactivateMfsAccountRefDataModel.getUsecaseId());
			Long actionId = new Long(activateDeactivateMfsAccountRefDataModel.getActionId());
			String comments = activateDeactivateMfsAccountRefDataModel.getComments();
			String lockUnlockVal =  activateDeactivateMfsAccountRefDataModel.getIslockUnlock();
			Long paymentModeId = activateDeactivateMfsAccountRefDataModel.getPaymentModeId();
			String isAgent = activateDeactivateMfsAccountRefDataModel.getIsAgent();
			String isHandler = activateDeactivateMfsAccountRefDataModel.getIsHandler();
			String mfsId = activateDeactivateMfsAccountRefDataModel.getMfsId();
			String acType = activateDeactivateMfsAccountRefDataModel.getAccountType();
			String action = activateDeactivateMfsAccountRefDataModel.getAction();
										
			if(lockUnlockVal !=null && lockUnlockVal.equals("true")){
				isLockUnlock = true;
			}

			if (null != appUserId && appUserId.trim().length() > 0 )
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("id is not null....retrieving object from DB and then updating it");
				}
				baseWrapper = this.prepareBaseWrapperForAction(acType,action,new Boolean(isAgent),new Boolean(isHandler),id,usecaseId,actionId,isLockUnlock,mfsId);
				if(paymentModeId == null)
					paymentModeId = (Long) baseWrapper.getObject("paymentModeId");

				baseWrapper = this.mfsAccountManager.activateDeactivateMfsAccount(baseWrapper);


				Boolean isActive = (Boolean)baseWrapper.getObject(MfsAccountManager.KEY_IS_ACTIVE);
				Date updationTime = (Date)baseWrapper.getObject(PortalConstants.KEY_UPDATION_TIME);

				/*if(!isLockUnlock){
					if (isActive.booleanValue())
					{
						complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_ACTIVATE, id, comments, updationTime);
						accountStateId = 3L;
					}
					else
					{
						complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_DEACTIVATE, id, comments, updationTime);
						accountStateId = 1L;
					}
				}else{
					if (isActive.booleanValue()){
						
						complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_UNBLOCK, id, comments, updationTime);
						accountStateId = 3L;
					}else{
						
						complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_BLOCK, id, comments, updationTime);
						accountStateId = 1L;
					}
				}
				AppUserModel appUser = new AppUserModel();
				appUser.setAppUserId(id);
				appUser = this.mfsAccountManager.getAppUserModelByPrimaryKey(id);
				appUser.setAccountStateId(accountStateId);
				baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(appUser);
				this.appUserManager.saveOrUpdateAppUser(baseWrapper);*/
				createComplaint(isActive,isLockUnlock,id,comments,updationTime,paymentModeId);
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
		map.put("message","Record updated successfully.");
	    modelAndView = new ModelAndView(this.getSuccessView(),map);	
		return modelAndView;	
	}

	private void createComplaint(Boolean isActive,boolean isLockUnlock,Long id,String comments,Date updationTime,Long paymentModeId)
	{
		Long accountStateId = null;

		try {
			if(!isLockUnlock){
				if (isActive.booleanValue())
				{
					complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_ACTIVATE, id, comments, updationTime);
					accountStateId = AccountStateConstantsInterface.ACCOUNT_STATE_COLD;
				}
				else
				{
					complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_DEACTIVATE, id, comments, updationTime);
					accountStateId = AccountStateConstantsInterface.ACCOUNT_STATE_WARM;
				}
			}else{
				if (isActive.booleanValue()){

					complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_UNBLOCK, id, comments, updationTime);
					accountStateId = AccountStateConstantsInterface.ACCOUNT_STATE_COLD;
				}else{

					complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_BLOCK, id, comments, updationTime);
					accountStateId = AccountStateConstantsInterface.ACCOUNT_STATE_WARM;
				}
			}
			if(paymentModeId.equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT))
			{
				AppUserModel appUser = new AppUserModel();
				appUser.setAppUserId(id);
				appUser = this.mfsAccountManager.getAppUserModelByPrimaryKey(id);
				appUser.setAccountStateId(accountStateId);
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(appUser);
				this.appUserManager.saveOrUpdateAppUser(baseWrapper);
			}
		}
		catch(FrameworkCheckedException ex) {
		}
	}
	
	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setComplaintManager(ComplaintManager complaintManager) {
		this.complaintManager = complaintManager;
	}

	public AppUserManager getAppUserManager() {
		return appUserManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}


	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

}
