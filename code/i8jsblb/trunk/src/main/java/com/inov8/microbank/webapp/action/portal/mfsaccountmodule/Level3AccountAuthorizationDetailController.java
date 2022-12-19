package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

/*
 * Author : Hassan Javaid
 * Date   : 27-08-2014
 * Module : Action Authorization
 * Project: Mircobank	
 * */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

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
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.ACOwnershipDetailModel;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BusinessTypeModel;
import com.inov8.microbank.common.model.CityModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.Level3AccountModel;
import com.inov8.microbank.common.model.OccupationModel;
import com.inov8.microbank.common.model.ProfessionModel;
import com.inov8.microbank.common.model.RegistrationStateModel;
import com.inov8.microbank.common.model.RetailerContactAddressesModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.NokDetailVOModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.AddressTypeConstants;
import com.inov8.microbank.common.util.ApplicantTypeConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.RegistrationStateConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.RetailerFacade;
import com.inov8.microbank.server.facade.portal.level3account.Level3AccountFacade;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountFacade;
import com.inov8.microbank.server.facade.portal.usermanagementmodule.UserManagementFacade;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.des.EncryptionHandler;
import com.thoughtworks.xstream.XStream;

public class Level3AccountAuthorizationDetailController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( Level3AccountAuthorizationDetailController.class );
	private Level3AccountFacade level3AccountFacade;
	private RetailerFacade retailerFacade; 
	private MfsAccountFacade mfsAccountFacade;
	private CommonCommandManager commonCommandManager;
	private ReferenceDataManager referenceDataManager;
	private AgentHierarchyManager agentHierarchyManager;
	private UserManagementFacade userManagementFacade;
	private DistributorManager distributorManager;
	private SmartMoneyAccountManager	smartMoneyAccountManager;
	private EncryptionHandler encryptionHandler;
	private LinkPaymentModeManager linkPaymentModeManager;
		
	public Level3AccountAuthorizationDetailController() {
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
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.intValue())						
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
			Level3AccountModel level3AccountModel = (Level3AccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			request.setAttribute("level3AccountModel",level3AccountModel);
	       //End Populating authorization pictures
			
			if(actionAuthorizationModel.getUsecaseId().longValue() == PortalConstants.UPDATE_L3_USECASE_ID)
			{
				Level3AccountModel oldL3AccountModel = (Level3AccountModel) xstream.fromXML(actionAuthorizationModel.getOldReferenceData());	
				request.setAttribute("currentL3AccountModel",oldL3AccountModel);
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
				Level3AccountModel level3AccountModel = (Level3AccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
				
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, level3AccountModel.getActionId());
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, level3AccountModel.getUsecaseId());
				baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
				baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
				baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
				
				baseWrapper.putObject(Level3AccountModel.LEVEL3_ACCOUNT_MODEL_KEY, level3AccountModel);
				
			    if(level3AccountModel.getUsecaseId().longValue()== PortalConstants.CREATE_L3_USECASE_ID){
			    	baseWrapper = this.level3AccountFacade.createLevel3Account(baseWrapper);
			    } else {
			    	baseWrapper = this.level3AccountFacade.updateLevel3Account(baseWrapper);			
			    }
				
				if(actionAuthorizationModel.getEscalationLevel().intValue()< usecaseModel.getEscalationLevels().intValue()) {
					approvedWithIntimationLevelsNext(actionAuthorizationModel,model, usecaseModel,request);
				} else {
					approvedAtMaxLevel(actionAuthorizationModel, model);
				}
								
			} else {				
				escalateToNextLevel(actionAuthorizationModel,model, nextAuthorizationLevel, usecaseModel.getUsecaseId(),request);
			}
		}	
		else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){

			if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
				throw new FrameworkCheckedException("You are not authorized to update action status.");
			}	
			actionDeniedOrCancelled(actionAuthorizationModel, model,request);
		} else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.longValue() 
				&& (actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)
				|| actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK))) {
			
			if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
				throw new FrameworkCheckedException("You are not authorized to update action status.");
			}
			actionDeniedOrCancelled(actionAuthorizationModel,model,request);
		} else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue() 
				&& actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {
			isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());

			if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
				throw new FrameworkCheckedException("You are not authorized to update action status.");
			}
			requestAssignedBack(actionAuthorizationModel,model,request);
		} else {
			
			throw new FrameworkCheckedException("Invalid status marked");
		}
	} catch (FrameworkCheckedException ex) {	
		ex.printStackTrace();
		if("MobileNumUniqueException".equals(ex.getMessage())){
			request.setAttribute("message",super.getText("newMfsAccount.mobileNumNotUnique", request.getLocale() ));
		}else if("NICUniqueException".equals(ex.getMessage())){
			request.setAttribute("message",super.getText("newMfsAccount.nicNotUnique", request.getLocale() ));
		}else if("AccountOpeningCommissionException".equals(ex.getMessage())){
			request.setAttribute("message",super.getText("newMfsAccount.commission.failed", request.getLocale() ));
		}else if(ex.getMessage().contains("You are not authorized")){
			request.setAttribute("message",ex.getMessage());
		} else {
				request.setAttribute("message",MessageUtil.getMessage("6075"));
		}	
		LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
		request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
    	return super.showForm(request, response, errors);
	}catch (Exception ex) {	
		ex.printStackTrace();
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
			Level3AccountModel level3AccountModel = (Level3AccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());

			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, level3AccountModel.getActionId());
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, level3AccountModel.getUsecaseId());
			baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
			baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
			baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
			baseWrapper.putObject(Level3AccountModel.LEVEL3_ACCOUNT_MODEL_KEY, level3AccountModel);
			
			if(level3AccountModel.getUsecaseId().longValue()== PortalConstants.CREATE_L2_USECASE_ID) {
		    	baseWrapper = this.level3AccountFacade.createLevel3Account(baseWrapper);
			} else {
		    	
		    	baseWrapper = this.level3AccountFacade.updateLevel3Account(baseWrapper);
		    }
			resolveWithIntimation(actionAuthorizationModel,model, usecaseModel,request);
		} catch (FrameworkCheckedException ex) {			
			if("MobileNumUniqueException".equals(ex.getMessage())){
				request.setAttribute("message",super.getText("newMfsAccount.mobileNumNotUnique", request.getLocale() ));
			}else if("NICUniqueException".equals(ex.getMessage())){
				request.setAttribute("message",super.getText("newMfsAccount.nicNotUnique", request.getLocale() ));
			}else if("AccountOpeningCommissionException".equals(ex.getMessage())){
				request.setAttribute("message",super.getText("newMfsAccount.commission.failed", request.getLocale() ));
			}else {
				if(null!=ex.getMessage())
					request.setAttribute("message",ex.getMessage().replaceAll("<br>",""));
				else
					request.setAttribute("message",MessageUtil.getMessage("6075"));
			}
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}catch (Exception ex) {	
			
			request.setAttribute("message",MessageUtil.getMessage("6075"));
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}
		request.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
	    modelAndView = super.showForm(request, response, errors);	
		return modelAndView;	
	}
	
	private void removeSpecialAccountTypes(CopyOnWriteArrayList<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList){

    	for (OlaCustomerAccountTypeModel model : olaCustomerAccountTypeModelList) {
    		if(model.getCustomerAccountTypeId().longValue() == CustomerAccountTypeConstants.SETTLEMENT
    				|| model.getCustomerAccountTypeId().longValue() == UserTypeConstantsInterface.WALKIN_CUSTOMER){
    			olaCustomerAccountTypeModelList.remove(model);
    		}
    	}
    }
		private Level3AccountModel populateCurrentInfoModel(String initialAppFormNo) throws Exception {
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			Level3AccountModel accountModel = new Level3AccountModel();
			RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactModel.setInitialAppFormNo(initialAppFormNo);
			searchBaseWrapper.setBasePersistableModel(retailerContactModel);

			retailerFacade.loadRetailerByInitialAppFormNo(searchBaseWrapper);
	      
			if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
			{
				retailerContactModel = (RetailerContactModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
				accountModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
				//Account Relationship Information
				accountModel.setInitialAppFormNo(initialAppFormNo);
				accountModel.setAccountPurpose(retailerContactModel.getAccountPurposeIdAccountPurposeModel().getName());
				accountModel.setAcNature(retailerContactModel.getAcNature());
				accountModel.setAcTitle(retailerContactModel.getName());
				accountModel.setCurrencyId(retailerContactModel.getCurrency());
				accountModel.setTaxRegimeName(retailerContactModel.getTaxRegimeIdTaxRegimeModel().getName());
				accountModel.setFed(retailerContactModel.getFed());
				//Business Information
				accountModel.setBusinessName(retailerContactModel.getBusinessName());
				accountModel.setCommencementDate(retailerContactModel.getCommencementDate());
				accountModel.setIncorporationDate(retailerContactModel.getIncorporationDate());
				accountModel.setSecpRegNo(retailerContactModel.getSecpRegNo());
				accountModel.setSecpRegDate(retailerContactModel.getSecpRegDate());
				accountModel.setNtn(retailerContactModel.getNtn());
				accountModel.setSalesTaxRegNo(retailerContactModel.getSalesTaxRegNo());
				accountModel.setMembershipNoTradeBody(retailerContactModel.getMembershipNoTradeBody());
				accountModel.setTradeBody(retailerContactModel.getTradeBody());
				accountModel.setBusinessNatureName(retailerContactModel.getBusinessNatureIdBusinessNatureModel().getName());
				accountModel.setLocationTypeName(retailerContactModel.getLocationTypeIdLocationTypeModel().getName());
				accountModel.setLocationSizeName(retailerContactModel.getLocationSizeIdLocationSizeModel().getName());
				accountModel.setEstSince(retailerContactModel.getEstSince());
				accountModel.setLandLineNo(retailerContactModel.getLandLineNo());
				accountModel.setRegStateComments(retailerContactModel.getRegStateComments());
				accountModel.setCustomerAccountTypeId(retailerContactModel.getOlaCustomerAccountTypeModelId());
				accountModel.setCreatedOn(retailerContactModel.getCreatedOn());
				accountModel.setSalary(retailerContactModel.getSalary());
				accountModel.setBuisnessIncome(retailerContactModel.getBusinessIncome());
				accountModel.setOtherIncome(retailerContactModel.getOtherIncome());
				accountModel.setCustomerTypeId(retailerContactModel.getCustomerTypeId());
				accountModel.setFundsSourceId(retailerContactModel.getFundSourceId());
				accountModel.setTransactionModeId(retailerContactModel.getTransactionModeId());
				accountModel.setOtherTransactionMode(retailerContactModel.getOtherTransactionMode());
				accountModel.setAccountReason(retailerContactModel.getAccountReasonId());
				accountModel.setScreeningPerformed(retailerContactModel.isScreeningPerformed());
				accountModel.setComments(retailerContactModel.getNokComments());
				accountModel.setAccountOpeningDate(retailerContactModel.getAccountOpeningDate());
				accountModel.setAccounttypeName(retailerContactModel.getOlaCustomerAccountTypeModel().getName());
				
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				DistributorModel distributorModel = new DistributorModel();
		  	    distributorModel.setDistributorId(retailerContactModel.getRetailerIdRetailerModel().getDistributorId());
		  	    baseWrapper.setBasePersistableModel(distributorModel);
		  	    baseWrapper = distributorManager.loadDistributor(baseWrapper);
		  	    distributorModel = (DistributorModel) baseWrapper.getBasePersistableModel();
		  	    accountModel.setDistributorName(distributorModel.getName()); 
				
				//NOK
				NokDetailVOModel nokDetailVOModel = new NokDetailVOModel();
				nokDetailVOModel.setNokName(retailerContactModel.getNokName());
				nokDetailVOModel.setNokRelationship(retailerContactModel.getNokRelationship());
				nokDetailVOModel.setNokMobile(retailerContactModel.getNokMobile());
				nokDetailVOModel.setNokMobile(retailerContactModel.getNokMobile());
				nokDetailVOModel.setNokIdNumber(retailerContactModel.getNokIdNumber());
				nokDetailVOModel.setNokIdType(retailerContactModel.getNokIdType());
				accountModel.setNokDetailVOModel(nokDetailVOModel);
				
				AppUserModel appUserModel = new AppUserModel();
				appUserModel = userManagementFacade.getAppUserByRetailer(retailerContactModel.getRetailerContactId());
				
				accountModel.setMobileNo(appUserModel.getMobileNo());
				accountModel.setAppUserId(appUserModel.getAppUserId());
				accountModel.setRegistrationStateId(appUserModel.getRegistrationStateId());
				accountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
				accountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
				accountModel.setMfsId(appUserModel.getUsername());
				
				try {
					UserDeviceAccountsModel	userDeviceAccountsModel=	mfsAccountFacade.getDeviceAccountByAppUserId(appUserModel.getAppUserId(), DeviceTypeConstantsInterface.ALL_PAY );
					
					if (!this.linkPaymentModeManager.isFirstAccountOtherThanOla(userDeviceAccountsModel.getUserId())){
						
						SmartMoneyAccountModel smartMoneyAccountModel	=	smartMoneyAccountManager.getSMALinkedWithCoreAccount(retailerContactModel.getRetailerContactId());
						if(smartMoneyAccountModel!=null) {
							String accountNumner=level3AccountFacade.getLinkedCoreAccountNo(appUserModel.getUsername());
							accountModel.setCoreAccountNumber(encryptionHandler.decrypt(accountNumner));
							accountModel.setCoreAccountTitle(smartMoneyAccountModel.getName());
							accountModel.setAccountLinked(Boolean.TRUE);
						}
					}
				} catch (Exception ex) {
					log.warn("Exception while getting customer info from OLA: " + ex.getStackTrace());
				}
				
				
				ApplicantDetailModel applicantModel = new ApplicantDetailModel();
				applicantModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
				applicantModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
				accountModel.setApplicant1DetailModel(mfsAccountFacade.loadApplicantDetail(applicantModel));
				
				BusinessTypeModel businessTypeModel = new BusinessTypeModel();
				businessTypeModel = level3AccountFacade.loadBusinessTypeModel(retailerContactModel.getBusinessTypeId());
			    accountModel.setBusinessTypeName(businessTypeModel.getName()); 
				
			    if(null != accountModel.getApplicant1DetailModel().getProfession()){
			  	    ProfessionModel professionModel = new ProfessionModel();
			  	    professionModel = level3AccountFacade.loadProfessionModel(accountModel.getApplicant1DetailModel().getProfession());
			  	    accountModel.getApplicant1DetailModel().setProfessionName(professionModel.getName()); 
			    }
			    
			    if(null != accountModel.getApplicant1DetailModel().getOccupation())
			    {
			  	    OccupationModel occupationModel = new OccupationModel(); 
			  	    occupationModel = level3AccountFacade.loadOccupationModel(accountModel.getApplicant1DetailModel().getOccupation());
			  	    accountModel.getApplicant1DetailModel().setOccupationName(occupationModel.getName()); 
			    }
			    
			    if(null != accountModel.getApplicant1DetailModel().getBirthPlace())
			    {
			    	CityModel birthCityModel = new CityModel();
				    birthCityModel = level3AccountFacade.loadCityModel(Long.parseLong(accountModel.getApplicant1DetailModel().getBirthPlace()));
				    accountModel.getApplicant1DetailModel().setBirthPlaceName(birthCityModel.getName());
			    }
				
				SearchBaseWrapper	searchWrapper = new SearchBaseWrapperImpl();
				ACOwnershipDetailModel queryAccountOwnerShipDetailModel = new ACOwnershipDetailModel();
				queryAccountOwnerShipDetailModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
				queryAccountOwnerShipDetailModel.setIsDeleted(Boolean.FALSE);
				searchWrapper.setBasePersistableModel(queryAccountOwnerShipDetailModel);
				accountModel.setAcOwnershipDetailModelList(mfsAccountFacade.loadL3AccountOwnerShipDetails(searchWrapper));

				// Populating Address Fields
				List<RetailerContactAddressesModel> retailerContactAddresses = findRetailContactAddresses(retailerContactModel);
				if (retailerContactAddresses != null && retailerContactAddresses.size() > 0)
				{
					for (RetailerContactAddressesModel retailerContactAdd : retailerContactAddresses)
					{
						AddressModel addressModel = retailerContactAdd.getAddressIdAddressModel();
						if (null == retailerContactAdd.getApplicantTypeId())
						{
							if (addressModel.getStreetAddress() != null && !addressModel.getStreetAddress().isEmpty())
							{
								accountModel.getNokDetailVOModel().setNokMailingAdd(addressModel.getStreetAddress());
							}
							if(retailerContactAdd.getAddressTypeId()==AddressTypeConstants.CORRESSPONDANCE_ADDRESS.longValue()
									&& retailerContactAdd.getApplicantDetailId()==null){
								
								accountModel.setCorresspondanceAddress(addressModel.getStreetAddress());
								accountModel.setCorresspondancePostalCode(addressModel.getPostalCode());
								
								if(addressModel.getCityId()!=null){
									CityModel corrCityModel = new CityModel();
									corrCityModel = level3AccountFacade.loadCityModel(addressModel.getCityId());
								    accountModel.setCorresspondanceAddCityName(corrCityModel.getName());
								}
							}
							if(retailerContactAdd.getAddressTypeId()==AddressTypeConstants.BUSINESS_ADDRESS.longValue()
									&& retailerContactAdd.getApplicantTypeId()==null){
								
								accountModel.setBusinessAddress(addressModel.getStreetAddress());
								accountModel.setBusinessPostalCode(addressModel.getPostalCode());
								
								CityModel corrCityModel = new CityModel();
								corrCityModel = level3AccountFacade.loadCityModel(addressModel.getCityId());
							    accountModel.setBusinessAddCityName(corrCityModel.getName());
							}
						} else if (retailerContactAdd.getApplicantTypeId().longValue() == ApplicantTypeConstants.APPLICANT_TYPE_1) {
							if (retailerContactAdd.getAddressTypeId().longValue() == AddressTypeConstants.PRESENT_HOME_ADDRESS)
							{
								if (addressModel.getStreetAddress() != null && !addressModel.getStreetAddress().isEmpty()) {
									accountModel.getApplicant1DetailModel().setResidentialAddress(addressModel.getStreetAddress());
								}
								if(null != addressModel.getCityId()){
									CityModel corrCityModel = new CityModel();
									corrCityModel = level3AccountFacade.loadCityModel(addressModel.getCityId());
								    accountModel.getApplicant1DetailModel().setResidentialCityName(corrCityModel.getName());
								}
							} else if (retailerContactAdd.getAddressTypeId().longValue() == AddressTypeConstants.BUSINESS_ADDRESS) {
								if (addressModel.getStreetAddress() != null && !addressModel.getStreetAddress().isEmpty()) {
									accountModel.getApplicant1DetailModel().setBuisnessAddress(addressModel.getStreetAddress());
								}
								if(null != addressModel.getCityId()){
									CityModel corrCityModel = new CityModel();
									corrCityModel = level3AccountFacade.loadCityModel(addressModel.getCityId());
								    accountModel.getApplicant1DetailModel().setBuisnessCityName(corrCityModel.getName());
								}
							}
						}
					}
				}
			}
	      
	      /////Load Reference Data
	      
	      Map referenceDataMap = new HashMap();
		  try{  
			    Long[] regStateList = {RegistrationStateConstantsInterface.RQST_RCVD,RegistrationStateConstantsInterface.DECLINE,RegistrationStateConstantsInterface.DISCREPANT,RegistrationStateConstantsInterface.VERIFIED};
			    List<RegistrationStateModel> regStates= commonCommandManager.getRegistrationStateByIds(regStateList).getResultsetList();
			    
			    for (RegistrationStateModel registrationStateModel : regStates) {
					if(null!=accountModel.getRegistrationStateId() && (accountModel.getRegistrationStateId().longValue()==registrationStateModel.getRegistrationStateId().longValue()))
						accountModel.setRegStateName(registrationStateModel.getName());
				}
		  }
		  catch(Exception e){
			  logger.error(e.getMessage(), e);
			  throw new Exception();
		  }
	      
	      return accountModel;
	}

		private List<RetailerContactAddressesModel> findRetailContactAddresses(RetailerContactModel retailerContactModel)
				throws FrameworkCheckedException
		{
			List<RetailerContactAddressesModel> retailerContactAddressesModelList = null;
			SearchBaseWrapper retailerContactAddressesWrapper = new SearchBaseWrapperImpl();
			RetailerContactAddressesModel retailerContactAddressesModel = new RetailerContactAddressesModel();
			retailerContactAddressesModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
			retailerContactAddressesWrapper.setBasePersistableModel(retailerContactAddressesModel);
			retailerContactAddressesWrapper = agentHierarchyManager.findRetailContactAddresses(retailerContactAddressesWrapper);
			if (retailerContactAddressesWrapper.getCustomList() != null)
			{
				retailerContactAddressesModelList = retailerContactAddressesWrapper.getCustomList().getResultsetList();
			}
			return retailerContactAddressesModelList;
		}	
		
	public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
		this.commonCommandManager = commonCommandManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setLevel3AccountFacade(Level3AccountFacade level3AccountFacade) {
		if (level3AccountFacade != null) {
			this.level3AccountFacade = level3AccountFacade;
		}
	}

	public void setRetailerFacade(RetailerFacade retailerFacade) {
		if (retailerFacade != null) {
			this.retailerFacade = retailerFacade;
		}
	}

	public void setMfsAccountFacade(MfsAccountFacade mfsAccountFacade) {
		if (mfsAccountFacade != null) {
			this.mfsAccountFacade = mfsAccountFacade;
		}
	}

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		if (agentHierarchyManager != null) {
			this.agentHierarchyManager = agentHierarchyManager;
		}
	}

	public void setUserManagementFacade(UserManagementFacade userManagementFacade) {
		if (userManagementFacade != null) {
			this.userManagementFacade = userManagementFacade;
		}
	}

	public DistributorManager getDistributorManager() {
		return distributorManager;
	}

	public void setDistributorManager(DistributorManager distributorManager) {
		if (distributorManager != null) {
			this.distributorManager = distributorManager;
		}
	}

	public void setSmartMoneyAccountManager(
			SmartMoneyAccountManager smartMoneyAccountManager) {
		if (smartMoneyAccountManager != null) {
			this.smartMoneyAccountManager = smartMoneyAccountManager;
		}
	}

	public void setEncryptionHandler(EncryptionHandler encryptionHandler) {
		if (encryptionHandler != null) {
			this.encryptionHandler = encryptionHandler;
		}
	}

	public void setLinkPaymentModeManager(
			LinkPaymentModeManager linkPaymentModeManager) {
		if (linkPaymentModeManager != null) {
			this.linkPaymentModeManager = linkPaymentModeManager;
		}
	}
}