package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

/*
 * Author : Hassan Javaid
 * Date   : 27-08-2014
 * Module : Action Authorization
 * Project: Mircobank	
 * */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import com.inov8.microbank.common.model.CityModel;
import com.inov8.microbank.common.model.CustomerAddressesModel;
import com.inov8.microbank.common.model.CustomerFundSourceModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.OccupationModel;
import com.inov8.microbank.common.model.ProfessionModel;
import com.inov8.microbank.common.model.RegistrationStateModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.AdditionalDetailVOModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.Level2AccountModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.NokDetailVOModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.ApplicantTypeConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.RegistrationStateConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.bulkdisbursements.CustomerPendingTrxManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.thoughtworks.xstream.XStream;

public class Level2AccountAuthorizationDetailController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( Level2AccountAuthorizationDetailController.class );
	private MfsAccountManager mfsAccountManager;
	private CustomerPendingTrxManager customerPendingTrxManager;
	private CommonCommandManager commonCommandManager;
	private ReferenceDataManager referenceDataManager;
		
	public Level2AccountAuthorizationDetailController() {
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
			Level2AccountModel level2AccountModel = (Level2AccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			request.setAttribute("level2AccountModel",level2AccountModel);
	       //End Populating authorization pictures
			
			if(actionAuthorizationModel.getUsecaseId().longValue() == PortalConstants.UPDATE_L2_USECASE_ID)
			{
				Level2AccountModel currentL2AccountModel = populateCurrentInfoModel(level2AccountModel.getAppUserId());  	
				request.setAttribute("currentL2AccountModel",currentL2AccountModel);
				
				BaseWrapper baseWrapper = new BaseWrapperImpl();
			    AppUserModel appUserModel = new AppUserModel();
		        appUserModel.setAppUserId(level2AccountModel.getAppUserId());
		        baseWrapper.setBasePersistableModel(appUserModel);
			    baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
			    appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
			    CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
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
				Level2AccountModel level2AccountModel = (Level2AccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
				
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, level2AccountModel.getActionId());
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, level2AccountModel.getUsecaseId());
				baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
				baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
				baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
				
				baseWrapper.putObject(Level2AccountModel.LEVEL2_ACCOUNT_MODEL_KEY, level2AccountModel);
				
				//this.mfsAccountManager.validateUniqueness(level2AccountModel);//Validate Uniqueness
			    
			    if(level2AccountModel.getUsecaseId().longValue()== PortalConstants.CREATE_L2_USECASE_ID){
			    	baseWrapper = this.mfsAccountManager.createLevel2Account(baseWrapper);
			    } else {
			    	//setting current Data for history
			    	Level2AccountModel oldLevel2AccountModel = populateCurrentInfoModel(level2AccountModel.getAppUserId());
					SearchBaseWrapper searchWrapper = new SearchBaseWrapperImpl();
					ACOwnershipDetailModel acOwnerShipDetailModel = new ACOwnershipDetailModel();
					acOwnerShipDetailModel.setIsDeleted(false);
					acOwnerShipDetailModel.setCustomerId(Long.parseLong(oldLevel2AccountModel.getCustomerId()));
					searchWrapper.setBasePersistableModel(acOwnerShipDetailModel);
					List<ACOwnershipDetailModel> existingOwners = mfsAccountManager.loadAccountOwnerShipDetails(searchWrapper);
					baseWrapper.putObject("existingOwners", (Serializable) existingOwners);
			    	baseWrapper = this.mfsAccountManager.updateLevel2Account(baseWrapper);
			    	
					
					//End setting current Data for history				
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
		if("MobileNumUniqueException".equals(ex.getMessage())){
			request.setAttribute("message",super.getText("newMfsAccount.mobileNumNotUnique", request.getLocale() ));
		}else if("NICUniqueException".equals(ex.getMessage())){
			request.setAttribute("message",super.getText("newMfsAccount.nicNotUnique", request.getLocale() ));
		}else if("AccountOpeningCommissionException".equals(ex.getMessage())){
			request.setAttribute("message",super.getText("newMfsAccount.commission.failed", request.getLocale() ));
		} else {
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
			Level2AccountModel level2AccountModel = (Level2AccountModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());

			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, level2AccountModel.getActionId());
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, level2AccountModel.getUsecaseId());
			baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
			baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
			baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
			baseWrapper.putObject(Level2AccountModel.LEVEL2_ACCOUNT_MODEL_KEY, level2AccountModel);
			
			if(level2AccountModel.getUsecaseId().longValue()== PortalConstants.CREATE_L2_USECASE_ID) {
		    	baseWrapper = this.mfsAccountManager.createLevel2Account(baseWrapper);
			} else {
		    	//setting current Data for history
		    	Level2AccountModel oldLevel2AccountModel = this.populateCurrentInfoModel(level2AccountModel.getAppUserId());
		    	
				SearchBaseWrapper searchWrapper = new SearchBaseWrapperImpl();
				baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
				ACOwnershipDetailModel acOwnerShipDetailModel = new ACOwnershipDetailModel();
				acOwnerShipDetailModel.setIsDeleted(false);
				acOwnerShipDetailModel.setCustomerId(Long.parseLong(oldLevel2AccountModel.getMfsId()));
				searchWrapper.setBasePersistableModel(acOwnerShipDetailModel);
				List<ACOwnershipDetailModel> existingOwners = mfsAccountManager.loadAccountOwnerShipDetails(searchWrapper);
				baseWrapper.putObject("existingOwners", (Serializable) existingOwners);
		    	
		    	baseWrapper = this.mfsAccountManager.updateLevel2Account(baseWrapper);
				//End setting current Data for history				
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
			request.setAttribute("message", MessageUtil.getMessage("6075"));
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
	private Level2AccountModel populateCurrentInfoModel(Long appUserId) throws Exception {
		 BaseWrapper baseWrapper = new BaseWrapperImpl();
	     AppUserModel appUserModel = new AppUserModel();
	     appUserModel.setAppUserId(appUserId);
	     baseWrapper.setBasePersistableModel(appUserModel);
	     baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
	      
	      appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
	      baseWrapper = new BaseWrapperImpl();
	      CustomerModel customer = new CustomerModel();
	      customer.setCustomerId(appUserModel.getCustomerId());
	      baseWrapper.setBasePersistableModel(customer);
	      baseWrapper = mfsAccountManager.loadCustomer(baseWrapper);
	      customer = (CustomerModel) baseWrapper.getBasePersistableModel();
	      String initialAppFormNo = customer.getInitialApplicationFormNumber();
	      
	      
	      //Get Product Catalog Id
	      UserDeviceAccountsModel userDeviceModel = mfsAccountManager.getDeviceAccountByAppUserId(appUserModel.getAppUserId(), DeviceTypeConstantsInterface.ALL_PAY);
	      
	      
	      Long catalogId = null;
	      String catalogName = "";
	      if(userDeviceModel != null){
	    	  catalogId = userDeviceModel.getProdCatalogId(); 
	    	  catalogName = userDeviceModel.getProdCatalogModel().getName();
	      }
			
	      Level2AccountModel level2AccountModel = new Level2AccountModel();
	      level2AccountModel.setInitialAppFormNo(initialAppFormNo);
	      level2AccountModel.setMobileNo(appUserModel.getMobileNo());
	      level2AccountModel.setAppUserId(appUserModel.getAppUserId());
	      level2AccountModel.setRegStateName(appUserModel.getRegistrationStateModel().getName());
	      level2AccountModel.setAccountClosedUnsettled(appUserModel.getAccountClosedUnsettled());
	      level2AccountModel.setAccountClosedSettled(appUserModel.getAccountClosedSettled());
	      level2AccountModel.setMfsId(appUserModel.getUsername());
	      level2AccountModel.setProductCatalogId(catalogId);
	      level2AccountModel.setProductCatalogName(catalogName);
	      
	      try{
	    	  OLAVO olaVo = this.mfsAccountManager.getAccountInfoFromOLA(appUserModel.getNic(),CommissionConstantsInterface.BANK_ID);
		      if(olaVo != null){
			      level2AccountModel.setAccountNo(olaVo.getPayingAccNo());
		      }
	      }catch(Exception ex){
	    	  log.warn("Exception while getting customer info from OLA: "+ex.getStackTrace());
	      }
	         
	      if(null!=appUserModel.getClosedByAppUserModel())
	      {
		      level2AccountModel.setClosedBy(appUserModel.getClosedByAppUserModel().getUsername());
	      }
	      level2AccountModel.setClosedOn(appUserModel.getClosedOn());
	      if(null!=appUserModel.getSettledByAppUserModel())
	      {
	    	  level2AccountModel.setSettledBy(appUserModel.getSettledByAppUserModel().getUsername());
	      }
	      level2AccountModel.setSettledOn(appUserModel.getSettledOn());
	      level2AccountModel.setClosingComments(appUserModel.getClosingComments());
	      level2AccountModel.setSettlementComments(appUserModel.getSettlementComments());
	      
	      CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel(); 	 
	      if(customerModel != null){
	    	  
	    	  ApplicantDetailModel applicantModel = new ApplicantDetailModel();
	    	  applicantModel.setCustomerId(customerModel.getCustomerId());
	    	  applicantModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
	    	  level2AccountModel.setApplicant1DetailModel(mfsAccountManager.loadApplicantDetail(applicantModel));
	    	  
	    	 if(null!=level2AccountModel.getApplicant1DetailModel().getProfession()){
	    		 ProfessionModel professionModel = new ProfessionModel();
	    		 professionModel = mfsAccountManager.loadProfessionModel(level2AccountModel.getApplicant1DetailModel().getProfession());
	    		 level2AccountModel.getApplicant1DetailModel().setProfessionName(professionModel.getName()); 
	    	 }
		    
	    	 if(level2AccountModel.getApplicant1DetailModel().getOccupation()!=null){
	 		    OccupationModel occupationModel = new OccupationModel();
			    occupationModel = mfsAccountManager.loadOccupationModel(level2AccountModel.getApplicant1DetailModel().getOccupation());
			    level2AccountModel.getApplicant1DetailModel().setOccupationName(occupationModel.getName());
	    	 }
		    
	    	 if(level2AccountModel.getApplicant1DetailModel().getBirthPlace()!=null){
	 		    CityModel birthCityModel = new CityModel();
			    birthCityModel = mfsAccountManager.loadCityModel(Long.parseLong(level2AccountModel.getApplicant1DetailModel().getBirthPlace()));
			    level2AccountModel.getApplicant1DetailModel().setBirthPlaceName(birthCityModel.getName());
	    	 }
	    	  
	      	  applicantModel = new ApplicantDetailModel();
	    	  applicantModel.setCustomerId(customerModel.getCustomerId());
	    	  applicantModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_2);
	    	  level2AccountModel.setApplicantDetailModelList(mfsAccountManager.loadApplicantDetails(applicantModel));
	    	  
	    	  level2AccountModel.setCustomerId(customerModel.getCustomerId().toString());
	    	  level2AccountModel.setCustomerAccountName(customerModel.getName());
	    	  level2AccountModel.setAccounttypeName(customerModel.getCustomerAccountTypeIdCustomerAccountTypeModel().getName());
	    	  level2AccountModel.setAccountNature(customerModel.getAccountNature());
	    	  level2AccountModel.setTypeOfAccount(customerModel.getTypeOfAccount());
	    	  level2AccountModel.setSegmentName(customerModel.getSegmentIdSegmentModel().getName());
	    	  level2AccountModel.setCurrency(customerModel.getCurrency());
	    	  level2AccountModel.setCustomerTypeId(customerModel.getCustomerTypeId());		
	    	  level2AccountModel.setRegStateComments(customerModel.getRegStateComments());
	    	  level2AccountModel.setCreatedOn(customerModel.getCreatedOn());
	    	  level2AccountModel.setBusinessTypeId(customerModel.getBusinessTypeId());
	    	  level2AccountModel.setBuisnessIncome(customerModel.getBusinessIncome());
	    	  level2AccountModel.setNokDetailVOModel(new NokDetailVOModel());
	    	  level2AccountModel.getNokDetailVOModel().setNokName(customerModel.getNokName());
	    	  level2AccountModel.getNokDetailVOModel().setNokContactNo(customerModel.getNokContactNo());
	    	  level2AccountModel.getNokDetailVOModel().setNokRelationship(customerModel.getNokRelationship());  
	    	  level2AccountModel.getNokDetailVOModel().setNokMobile(customerModel.getNokMobile());
	    	  level2AccountModel.getNokDetailVOModel().setNokIdType(customerModel.getNokIdType());
	    	  level2AccountModel.getNokDetailVOModel().setNokIdNumber(customerModel.getNokIdNumber());
	    	  level2AccountModel.setComments(customerModel.getComments());
	    	  level2AccountModel.setAdditionalDetailVOModel(new AdditionalDetailVOModel());
	    	  level2AccountModel.getAdditionalDetailVOModel().setSecpRegNo(customerModel.getSecpRegNo());
	    	  level2AccountModel.getAdditionalDetailVOModel().setIncorporationDate(customerModel.getIncorporationDate());
	    	  level2AccountModel.getAdditionalDetailVOModel().setMembershipNoTradeBody(customerModel.getMembershipNoTradeBody());
	    	  level2AccountModel.getAdditionalDetailVOModel().setSalesTaxRegNo(customerModel.getSalesTaxRegNo());
	    	  level2AccountModel.getAdditionalDetailVOModel().setRegistrationPlace(customerModel.getRegistrationPlace());
	    	  level2AccountModel.setCnicSeen(customerModel.getIsCnicSeen());
	    	  level2AccountModel.setScreeningPerformed(customerModel.isScreeningPerformed());
	    	  level2AccountModel.setModeOfTx(customerModel.getTransactionModeId()+"");
	    	  level2AccountModel.setAccountPurpose(customerModel.getAccountPurposeIdAccountPurposeModel() == null? "" : customerModel.getAccountPurposeIdAccountPurposeModel().getName());
	    	  level2AccountModel.setTaxRegimeName(customerModel.getTaxRegimeIdTaxRegimeModel()==null ? "" : customerModel.getTaxRegimeIdTaxRegimeModel().getName());
	    	  level2AccountModel.setFed(customerModel.getFed());
	    	  level2AccountModel.setProductCatalogName(catalogName);
	    	  
	    	  SearchBaseWrapper searchWrapper = new SearchBaseWrapperImpl();
		      ACOwnershipDetailModel queryAccountOwnerShipDetailModel = new ACOwnershipDetailModel();
		      queryAccountOwnerShipDetailModel.setCustomerId(customerModel.getCustomerId());
		      queryAccountOwnerShipDetailModel.setIsDeleted(Boolean.FALSE);
		      searchWrapper.setBasePersistableModel(queryAccountOwnerShipDetailModel);
		      level2AccountModel.setAcOwnershipDetailModelList(mfsAccountManager.loadAccountOwnerShipDetails(searchWrapper));
		      
	    	  /***
			     * Populating Customer Source of Funds
			    */
			    	  
			    CustomerFundSourceModel customerFundSourceModel = new CustomerFundSourceModel();
			    customerFundSourceModel.setCustomerId(customerModel.getCustomerId());
			    ReferenceDataWrapper customerFundSourceReferenceDataWrapper = new ReferenceDataWrapperImpl(customerFundSourceModel, "customerFundSourceId", SortingOrder.ASC);
			    customerFundSourceReferenceDataWrapper.setBasePersistableModel(customerFundSourceModel);
			  	try {
			  		referenceDataManager.getReferenceData(customerFundSourceReferenceDataWrapper);
			  	} catch (Exception e) {
			  		logger.error(e.getMessage(), e);
			  	}

			  // Populating Address Fields
		    	  Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
		    	  if(customerAddresses != null && customerAddresses.size() > 0){
		    		  for(CustomerAddressesModel custAdd : customerAddresses){
		    			  AddressModel addressModel = custAdd.getAddressIdAddressModel();
		    			  	if(null == custAdd.getApplicantTypeId()){
			    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
			    					  level2AccountModel.getNokDetailVOModel().setNokMailingAdd(addressModel.getHouseNo()); 
			    				  }
		    		  		}else if(custAdd.getApplicantTypeId() == 1L){
		    				  if(custAdd.getAddressTypeId() == 1){
			    				  if(addressModel.getCityId() != null){
			    					  level2AccountModel.getApplicant1DetailModel().setResidentialCityName(addressModel.getCityIdCityModel().getName());
			    				  }
			    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
			    					  level2AccountModel.getApplicant1DetailModel().setResidentialAddress(addressModel.getHouseNo()); 
			    				  }
			    			  }else if(custAdd.getAddressTypeId() == 3){
			    				  if(addressModel.getCityId() != null){
			    					  level2AccountModel.getApplicant1DetailModel().setBuisnessCityName(addressModel.getCityIdCityModel().getName());
			    				  }
			    				  if(addressModel.getHouseNo() != null && !addressModel.getHouseNo().isEmpty()){
			    					  level2AccountModel.getApplicant1DetailModel().setBuisnessAddress(addressModel.getHouseNo()); 
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
					if(null!=level2AccountModel.getRegistrationStateId() && (level2AccountModel.getRegistrationStateId().longValue()==registrationStateModel.getRegistrationStateId().longValue()))
						level2AccountModel.setRegStateName(registrationStateModel.getName());
				}
			    
		  }
		  catch(Exception e){
			  logger.error(e.getMessage(), e);
			  throw new Exception();
		  }
	      
	      return level2AccountModel;
	}
	
	public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
		this.mfsAccountManager = mfsAccountManager;
	}

	public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
		this.commonCommandManager = commonCommandManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setCustomerPendingTrxManager(
			CustomerPendingTrxManager customerPendingTrxManager) {
		this.customerPendingTrxManager = customerPendingTrxManager;
	}
}