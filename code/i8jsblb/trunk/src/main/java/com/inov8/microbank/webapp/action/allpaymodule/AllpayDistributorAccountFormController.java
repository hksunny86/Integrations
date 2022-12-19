package com.inov8.microbank.webapp.action.allpaymodule;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.MobileTypeModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.distributormodule.DistributorContactFormModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.DistributorConstants;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.AllpayModule.AllpayDistributorAccountManager;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.distributormodule.DistributorLevelManager;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialInstitution;
import com.inov8.microbank.server.service.olamodule.OLAManager;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.ola.integration.vo.OLAVO;

public class AllpayDistributorAccountFormController extends AdvanceFormController {
	
	private FinancialInstitution olaVeriflyFinancialInstitution;
	private DistributorLevelManager distributorLevelManager;
	private ReferenceDataManager referenceDataManager;	
	private PartnerGroupManager partnerGroupManager;
	private AppUserManager appUserManager;
	private OLAManager olaManager;
	private UserDeviceAccountsDAO userDeviceAccountsDAO;
	private ActionLogManager actionLogManager;
	private AllpayDistributorAccountManager allpayDistributorAccountManager;
	private SmartMoneyAccountDAO smartMoneyAccountDAO;
	private Long id = null;
	public AllpayDistributorAccountFormController() {
		setCommandName("distributorContactModel");
		setCommandClass(DistributorContactFormModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		// load data for distributor contact
		id = ServletRequestUtils.getLongParameter(httpServletRequest,
				"distributorContactId");
		if (null != id) {
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
				
			}			
			DistributorContactFormModel distributorContactFormModel = new DistributorContactFormModel();
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			DistributorContactModel distributorContactModel = new DistributorContactModel();

			distributorContactModel.setDistributorContactId(id);
			searchBaseWrapper.setBasePersistableModel(distributorContactModel);
			AppUserModel appUserModel = UserUtils.getCurrentUser();
			ActionLogModel actionLogModel = this.logBeforeAction(ServletRequestUtils.getLongParameter(
					httpServletRequest, PortalConstants.KEY_ACTION_ID),
					PortalConstants.OLA_SEARCH_ACCOUNT, appUserModel
							.getAppUserId());
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
			distributorContactModel = (DistributorContactModel) this.allpayDistributorAccountManager
					.loadAccount(searchBaseWrapper)
					.getBasePersistableModel();
			logAfterAction(actionLogModel,String.valueOf(appUserModel.getAppUserId()));
			//loadDistributorContact
			Double balance = distributorContactModel.getBalance();
			if(balance ==0)
			{
				balance=0.0;
				
			}
			DecimalFormat myFormatter = new DecimalFormat("###.##");
			String bal = myFormatter.format(balance);
			httpServletRequest.setAttribute("balance", bal);
			distributorContactModel.setBalance(Double.valueOf(bal));
			appUserModel = new AppUserModel();
			appUserModel.setAppUserId(ServletRequestUtils.getLongParameter(
					httpServletRequest, "appUserId"));
			baseWrapper.setBasePersistableModel(appUserModel);
			appUserModel = (AppUserModel) this.appUserManager.loadAppUser(
					baseWrapper).getBasePersistableModel();

			BeanUtils.copyProperties(distributorContactFormModel, appUserModel);
			BeanUtils.copyProperties(distributorContactFormModel,
					distributorContactModel);

			distributorContactFormModel.setAreaName(distributorContactModel
					.getAreaIdAreaModel().getName());
			distributorContactFormModel
					.setDistributorName(distributorContactModel
							.getDistributorIdDistributorModel().getName());
			distributorContactFormModel.setLevelName(distributorContactModel
					.getDistributorLevelIdDistributorLevelModel().getName());

			if (distributorContactModel.getManagingContactId() != null) {
				AppUserModel tempAppUserModel = new AppUserModel();
				tempAppUserModel
						.setDistributorContactId(distributorContactModel
								.getManagingContactId());
				searchBaseWrapper.setBasePersistableModel(tempAppUserModel);
				tempAppUserModel = (AppUserModel) this.appUserManager
						.searchAppUser(searchBaseWrapper)
						.getBasePersistableModel();

				distributorContactFormModel
						.setManagingContactName(tempAppUserModel.getFirstName()
								+ " " + tempAppUserModel.getLastName());
			}

			distributorContactFormModel.setMobileTypeId(String
					.valueOf(appUserModel.getMobileTypeId()));
			distributorContactFormModel
					.setDistributorContactModelVersionNo(distributorContactModel
							.getVersionNo());
			distributorContactFormModel.setAppUserModelVersionNo(appUserModel
					.getVersionNo());

			// ******************** Load Reference data
			// ***********************************************
			ReferenceDataWrapper referenceDataWrapper;

			DistributorModel distributorModel = new DistributorModel();
			distributorModel.setAreaId(distributorContactModel.getAreaId());
			referenceDataWrapper = new ReferenceDataWrapperImpl(
					distributorModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(distributorModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<DistributorModel> distributorModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				distributorModelList = referenceDataWrapper
						.getReferenceDataList();
			}
			httpServletRequest.setAttribute("distributorModelList",
					distributorModelList);

			DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
			// commented by Rashid Starts
			/*distributorLevelModel.setDistributorId(distributorContactModel.getDistributorId());*/
			// commented by Rashid Ends
			referenceDataWrapper = new ReferenceDataWrapperImpl(
					distributorLevelModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(distributorLevelModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			List<DistributorLevelModel> distributorLevelModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				distributorLevelModelList = referenceDataWrapper
						.getReferenceDataList();
			}
			httpServletRequest.setAttribute("distributorLevelModelList",
					distributorLevelModelList);
			httpServletRequest.setAttribute("distributor_Id",
					distributorContactModel.getDistributorId());
			// =========================================================================================
		    distributorContactFormModel.setAccountNick(loadSmartMoneyAccountModel(id).getName());
			//step 2 ---load data for allpay id account nick
			
			OLAVO olaVo = new OLAVO ();
			SwitchWrapper sWrapper = new SwitchWrapperImpl ();			
			olaVo.setCnic(appUserModel.getNic()	);			
			sWrapper.setBankId(this.allpayDistributorAccountManager.getOlaBankMadal().getBankId());
			sWrapper.setOlavo(olaVo);
			 sWrapper= olaVeriflyFinancialInstitution.getAccountInfo(sWrapper);
			 olaVo =sWrapper.getOlavo();
			 distributorContactFormModel.setFatherName(olaVo.getFatherName());
			 distributorContactFormModel.setMiddleName(olaVo.getMiddleName());
			 distributorContactFormModel.setLandlineNumber(olaVo.getLandlineNumber());
			 distributorContactFormModel.setAccountStatusId(olaVo.getStatusId());
			 distributorContactFormModel.setOldNic(olaVo.getCnic());
			//step 3 ---load data for OLA account
			appUserModel = getAppUserModel (id);
			UserDeviceAccountsModel userDeviceAccountsModel =   getUserDeviceAccountModel(appUserModel);
			distributorContactFormModel.setCommissioned(userDeviceAccountsModel.getCommissioned());
			distributorContactFormModel.setAllpayId(userDeviceAccountsModel.getUserId());///get allpay id
			distributorContactFormModel.setPartnerGroupId(this.allpayDistributorAccountManager.getAppUserPartnerGroupId(appUserModel.getAppUserId()));
			return distributorContactFormModel;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}
			return new DistributorContactFormModel();
		}
	
	}
	private SmartMoneyAccountModel loadSmartMoneyAccountModel (Long distributorContactId){
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
		smartMoneyAccountModel.setBankId(this.allpayDistributorAccountManager.getOlaBankMadal().getBankId());
		smartMoneyAccountModel.setDistributorContactId(distributorContactId);
		List moneyMadel = this.smartMoneyAccountDAO.findByExample(smartMoneyAccountModel).getResultsetList();
		if (moneyMadel.size() == 0){
			return new SmartMoneyAccountModel ();
		}
		return (SmartMoneyAccountModel)moneyMadel.get(0);		
	}
	private UserDeviceAccountsModel getUserDeviceAccountModel(AppUserModel appUserModel) {
		
		
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserIdAppUserModel(appUserModel);
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		CustomList list = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel);
		int size = list.getResultsetList().size();
		if (size != 0)
			userDeviceAccountsModel=(UserDeviceAccountsModel)list.getResultsetList().get(0);
		return userDeviceAccountsModel;
	
		
	}

	private AppUserModel getAppUserModel(Long id2) {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	
		AppUserModel appUserModel=new AppUserModel();
		appUserModel.setDistributorContactId(id2);
		searchBaseWrapper.setBasePersistableModel(appUserModel);
		try {
			searchBaseWrapper  = (SearchBaseWrapper)appUserManager.searchAppUser(searchBaseWrapper);
		appUserModel = (AppUserModel)	searchBaseWrapper.getBasePersistableModel();
			return appUserModel;
		} catch (FrameworkCheckedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return appUserModel;
		
		
	}

	private ActionLogModel logBeforeAction(Long actionId, Long useCaseId,
			Long appUserId) throws FrameworkCheckedException {
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId(actionId);
		actionLogModel.setUsecaseId(useCaseId);
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START); // the
		// process
		// is
		// starting
		actionLogModel.setCustomField1(String.valueOf(appUserId));
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();
		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		baseWrapperActionLog = this.actionLogManager
				.createOrUpdateActionLogRequiresNewTransaction(baseWrapperActionLog);
		return actionLogModel;
	}

	private void logAfterAction(ActionLogModel actionLogModel, String appUserId)
			throws FrameworkCheckedException {
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // the
		// process
		// is
		// starting

		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		baseWrapperActionLog = this.actionLogManager
				.createOrUpdateActionLog(baseWrapperActionLog);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside reference data");
		}

		ReferenceDataWrapper referenceDataWrapper;
		Map referenceDataMap = new HashMap();

		/**
		 * code fragment to load reference data for MobileTypeModel
		 */

		MobileTypeModel mobileTypeModel = new MobileTypeModel();

		referenceDataWrapper = new ReferenceDataWrapperImpl(mobileTypeModel,
				"name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(mobileTypeModel);

		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex) {
		}

		List<MobileTypeModel> mobileTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			mobileTypeModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("mobileTypeModelList", mobileTypeModelList);

		AreaModel areaModel = new AreaModel();
		referenceDataWrapper = new ReferenceDataWrapperImpl(areaModel, "name",
				SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(areaModel);

		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<AreaModel> areaModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			areaModelList = referenceDataWrapper.getReferenceDataList();
		}
		referenceDataMap.put("areaModelList", areaModelList);
		List<DistributorModel> distributorModelList = null;
		if (id == null) {
			DistributorModel distributorModel = new DistributorModel();
			if (httpServletRequest.getParameter("areaId") != null) {

				distributorModel.setAreaId(Long.getLong(httpServletRequest
						.getParameter("areaId")));
			} else {
				if (areaModelList.size()>0)
				{
				distributorModel.setAreaId(((AreaModel) areaModelList.get(0))
						.getAreaId());
				}
			}
			distributorModel.setActive(true);
			referenceDataWrapper = new ReferenceDataWrapperImpl(
					distributorModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(distributorModel);

			referenceDataManager.getReferenceData(referenceDataWrapper);
			
			if (referenceDataWrapper.getReferenceDataList() != null) {
				distributorModelList = referenceDataWrapper
						.getReferenceDataList();
			}
			if (distributorModelList!=null)
			{	
				//removeNationalDistributor(distributorModelList);
			referenceDataMap.put("distributorModelList", distributorModelList);
			}

			
			String distributorLevelHQL = " select distributorLevelId,distributorLevelName from DistributorLevelListViewModel dl "
				+ "where dl.distributorId = ? "
				+ " order by distributorLevelName " ;
			List<DistributorLevelModel> distributorLevelModelList = null;
			if (httpServletRequest.getParameter("distributorId") != null) {
			
				Long dist= 0L ;
				try {
					dist = ServletRequestUtils.getLongParameter(httpServletRequest,"distributorId");
				} catch (ServletRequestBindingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				distributorLevelModelList=distributorLevelManager.searchDistributorLevelModels(dist,distributorLevelHQL);
				referenceDataMap.put("distributorLevelModelList",
						distributorLevelModelList);
			} else {
				if (!distributorModelList.isEmpty()
						&& distributorModelList != null) {
					
			
					distributorLevelModelList=distributorLevelManager.searchDistributorLevelModels(((DistributorModel) distributorModelList.get(0)).getDistributorId(),distributorLevelHQL);
					referenceDataMap.put("distributorLevelModelList",
							distributorLevelModelList);
				}
			}
		
		}	

			PartnerModel partnerModel = new PartnerModel();
			partnerModel.setAppUserTypeId(UserTypeConstantsInterface.DISTRIBUTOR);
			if(null!=id)
			{
				DistributorContactModel distributorContactModel = new DistributorContactModel();
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				distributorContactModel.setDistributorContactId(id);
				searchBaseWrapper.setBasePersistableModel(distributorContactModel);
				distributorContactModel = (DistributorContactModel) this.allpayDistributorAccountManager
						.loadAccount(searchBaseWrapper)
						.getBasePersistableModel();
				
				if(distributorContactModel!=null)
				{	
				partnerModel.setDistributerId(distributorContactModel.getDistributorId());
				}
			}
			else
			{
			if(distributorModelList!=null && distributorModelList.size()>0)
			{
				
				
				Long distributorId = (Long)httpServletRequest.getAttribute("distributorId");
				
				if(null!=distributorId)
				{
					
					partnerModel.setDistributerId(distributorId);
				}
				else
				{
					partnerModel.setDistributerId(distributorModelList.get(0).getDistributorId());
				}
				
			}
		}
			
			
			referenceDataWrapper = new ReferenceDataWrapperImpl(
					partnerModel, "name", SortingOrder.ASC);
			referenceDataWrapper.setBasePersistableModel(partnerModel);
			try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
			} catch (FrameworkCheckedException ex1) {
				ex1.getStackTrace();
			}
			List<PartnerModel> partnerModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) 
			{
				partnerModelList = referenceDataWrapper.getReferenceDataList();
			}
			
			PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
			List<PartnerGroupModel> partnerGroupModelList = null;
			if(partnerModelList!=null && partnerModelList.size()>0)
			{
				partnerGroupModel.setActive(true);
				partnerGroupModel.setPartnerId(partnerModelList.get(0).getPartnerId());
				partnerGroupModelList =partnerGroupManager.getPartnerGroups(partnerGroupModel,true);
			}
			referenceDataMap.put("partnerGroupModelList", partnerGroupModelList);

		

		return referenceDataMap;
	}
//	private void removeNationalDistributor(List<DistributorModel> distributorModelList) {
//		for (Iterator iter = distributorModelList.iterator(); iter.hasNext();) {
//			DistributorModel distributor =(DistributorModel)  iter.next();
//			if (true == distributor.getNational()){
//				iter.remove();
//			}
//			
//		}
//		
//	}
	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception {
		
		try {
			allpayDistributorAccountManager.createAccount(httpServletRequest,
					httpServletResponse, object, bindException);
			httpServletRequest.getSession().setAttribute("messages", new ArrayList());
			String olaAccountNo =(String) httpServletRequest.getAttribute("olaAccountNo");
			String allpayId= ((DistributorContactFormModel)object).getAllpayId();
			String message = "Distributor contact with ID: "+allpayId+" successfully created. OLA Account: "+olaAccountNo;
			super.saveMessage(httpServletRequest,message);
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
			
	        return modelAndView;
		} catch (Exception e) {
			((DistributorContactFormModel)object).setHead(httpServletRequest.getParameter("originalHeadValue"));
			((DistributorContactFormModel)object).setDistributorContactId(null);
			httpServletRequest.getSession().setAttribute("messages", new ArrayList());
			//if (e instanceof  MobileNumUniqueException)
			String exceptionMessage =(String) httpServletRequest.getAttribute("exceptionMessage");
			String olaExceptionMessage= (String)httpServletRequest.getAttribute("olaExceptionMessage");
			super.saveMessage(httpServletRequest,
            "Record could not be saved.");
			
			if (DistributorConstants.HEAD_CHK.equals(e.getMessage())){
				super.saveMessage(httpServletRequest, e.getMessage());
			}
				if ("NIC already exisits in the OLA accounts".equals(olaExceptionMessage)){
					super.saveMessage(httpServletRequest, olaExceptionMessage);
				}
				if ("MobileNumUniqueException".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,
		            "Mobile number already exists.");
				}else if("UsernameUniqueException".equals(exceptionMessage)) {
					super.saveMessage(httpServletRequest,
		            "Username already exists.");
				}else if("HeadUniqueException".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,
		            "Head contact  is already defined.");
				}
				if (null != httpServletRequest.getAttribute("UK_USER_DEVICE_TYPE")){
					super.saveMessage(httpServletRequest,(String)
							httpServletRequest.getAttribute("UK_USER_DEVICE_TYPE"));
				}
				if ("Allpay ID does not exist".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,exceptionMessage);
				}
				if ("Account is disabled.".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,exceptionMessage);
				}
				if ("Account is expired".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,exceptionMessage);
				}
				if ("Account is locked".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,exceptionMessage);
				}
				if ("Account credential is expired".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,exceptionMessage);
				}
				//loadFormBackingObject(httpServletRequest);
				//loadReferenceData(httpServletRequest);
				return super.showForm(httpServletRequest, httpServletResponse, bindException);
		}		
	}

	

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object, BindException bindException)
			throws Exception {
		try {
			httpServletRequest.setAttribute("isUpdate", "y");
			allpayDistributorAccountManager.updateAccount(httpServletRequest,
					httpServletResponse, object, bindException);
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
			httpServletRequest.getSession().setAttribute("messages", new ArrayList());
			super.saveMessage(httpServletRequest,
            "Record updated successfully.");	
	        return modelAndView;
		} catch (Exception e) {
			//reset head value
			((DistributorContactFormModel)object).setHead(httpServletRequest.getParameter("originalHeadValue"));
			httpServletRequest.getSession().setAttribute("messages", new ArrayList());
			//if (e instanceof  MobileNumUniqueException)
			String exceptionMessage =(String) httpServletRequest.getAttribute("exceptionMessage");	
			String olaExceptionMessage= (String)httpServletRequest.getAttribute("olaExceptionMessage");
			
			super.saveMessage(httpServletRequest,
            "Record could not be saved.");	
			if ("NIC already exisits in the OLA accounts".equals(olaExceptionMessage)){
				super.saveMessage(httpServletRequest, olaExceptionMessage);
			}
				if ("MobileNumUniqueException".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,
		            "Mobile number already exists.");
				}else if("UsernameUniqueException".equals(exceptionMessage)) {
					super.saveMessage(httpServletRequest,
		            "Username already exists.");
				}
				else if("HeadUniqueException".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,
		            "Head contact is already defined.");
				}
				else if (!"".equals(exceptionMessage) || exceptionMessage != null){
					super.saveMessage(httpServletRequest, exceptionMessage);
				}
				if (null != httpServletRequest.getAttribute("UK_USER_DEVICE_TYPE")){
					super.saveMessage(httpServletRequest,(String)
							httpServletRequest.getAttribute("UK_USER_DEVICE_TYPE"));
				}
				if ("Allpay ID does not exist".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,exceptionMessage);
				}
				if ("Account is disabled.".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,exceptionMessage);
				}
				if ("Account is expired".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,exceptionMessage);
				}
				if ("Account is locked".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,exceptionMessage);
				}
				if ("Account credential is expired".equals(exceptionMessage)){
					super.saveMessage(httpServletRequest,exceptionMessage);
				}
				loadFormBackingObject(httpServletRequest);
				loadReferenceData(httpServletRequest);
				return super.showForm(httpServletRequest, httpServletResponse, bindException);
		}		

	
	}

	public AppUserManager getAppUserManager() {
		return appUserManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}



	public DistributorLevelManager getDistributorLevelManager() {
		return distributorLevelManager;
	}

	public void setDistributorLevelManager(
			DistributorLevelManager distributorLevelManager) {
		this.distributorLevelManager = distributorLevelManager;
	}

	public PartnerGroupManager getPartnerGroupManager() {
		return partnerGroupManager;
	}

	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}

	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public OLAManager getOlaManager() {
		return olaManager;
	}

	public void setOlaManager(OLAManager olamanager) {
		this.olaManager = olamanager;
	}

	public AllpayDistributorAccountManager getAllpayDistributorAccountManager() {
		return allpayDistributorAccountManager;
	}

	public void setAllpayDistributorAccountManager(
			AllpayDistributorAccountManager allpayDistributorAccountManager) {
		this.allpayDistributorAccountManager = allpayDistributorAccountManager;
	}

	public FinancialInstitution getOlaVeriflyFinancialInstitution() {
		return olaVeriflyFinancialInstitution;
	}

	public void setOlaVeriflyFinancialInstitution(
			FinancialInstitution olaVeriflyFinancialInstitution) {
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}

	public ActionLogManager getActionLogManager() {
		return actionLogManager;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public UserDeviceAccountsDAO getUserDeviceAccountsDAO() {
		return userDeviceAccountsDAO;
	}

	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}


	public SmartMoneyAccountDAO getSmartMoneyAccountDAO() {
		return smartMoneyAccountDAO;
	}

	public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
		this.smartMoneyAccountDAO = smartMoneyAccountDAO;
	}



}
