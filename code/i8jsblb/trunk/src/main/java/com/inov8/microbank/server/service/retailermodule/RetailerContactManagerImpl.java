package com.inov8.microbank.server.service.retailermodule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AddressModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;
import com.inov8.microbank.common.model.GoldenNosModel;
import com.inov8.microbank.common.model.RetailerContactAddressesModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactSearchViewModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.SMSConstants;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.vo.RetailerContactDetailVO;
import com.inov8.microbank.server.dao.addressmodule.AddressDAO;
import com.inov8.microbank.server.dao.addressmodule.RetailerContactAddressesDAO;
import com.inov8.microbank.server.dao.agenthierarchymodule.RetailerContactSearchViewDAO;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.portal.mfsaccountmodule.GoldenNosDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactListViewDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

/**
 * <p>
 * Title: Microbank
 * </p>
 * 
 * <p>
 * Description: Backend Application for POS terminals.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: Inov8 Limited
 * </p>
 * 
 * @author Asad Hayat
 * @version 1.0
 */
public class RetailerContactManagerImpl implements RetailerContactManager {
	private RetailerContactDAO retailerContactDAO;

	private RetailerContactListViewDAO retailerContactListViewDAO;

	private AppUserDAO appUserDAO;

	private UserDeviceAccountsDAO userDeviceAccountsDAO;

	private SmsSender smsSender;

	private GoldenNosDAO goldenNosDAO;

	private OracleSequenceGeneratorJdbcDAO sequenceGenerator;

	private AppUserManager appUserManager;



	private ActionLogManager actionLogManager;

	private AppUserPartnerGroupDAO appUserPartnerGroupDAO;
	
	private AddressDAO addressDAO;
	
	private RetailerContactAddressesDAO retailerContactAddressesDAO;
	
	private OracleSequenceGeneratorJdbcDAO retailerSequenceGenerator;
	
	
	private RetailerContactSearchViewDAO retailerContactSearchViewDAO;
	
	public void setRetailerSequenceGenerator(
			OracleSequenceGeneratorJdbcDAO retailerSequenceGenerator) {
		this.retailerSequenceGenerator = retailerSequenceGenerator;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public SearchBaseWrapper loadRetailerContactListView(SearchBaseWrapper searchBaseWrapper) 
	{
		try
		{
			RetailerContactListViewModel retailerContactListViewModel = this.retailerContactListViewDAO.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel().getPrimaryKey());
			searchBaseWrapper.setBasePersistableModel(retailerContactListViewModel);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return searchBaseWrapper;
	}

	@Override
	public SearchBaseWrapper findExactRetailerContactListViewModel(SearchBaseWrapper wrapper) throws FrameworkCheckedException
	{
		RetailerContactListViewModel model = (RetailerContactListViewModel)wrapper.getBasePersistableModel();
		ExampleConfigHolderModel configHolderModel = new ExampleConfigHolderModel(false, false, false, MatchMode.EXACT);
		CustomList<RetailerContactListViewModel> customList = retailerContactListViewDAO.findByExample(model, null, null, configHolderModel);
		wrapper.setCustomList(customList);
		return wrapper;
	}

	public SearchBaseWrapper loadRetailerContact(
			SearchBaseWrapper searchBaseWrapper) {
		RetailerContactModel retailerContactModel = this.retailerContactDAO
				.findByPrimaryKey(searchBaseWrapper.getBasePersistableModel()
						.getPrimaryKey());
		try{
		//populating address
		if(retailerContactModel != null){
			if(retailerContactModel.getRetailerContactIdRetailerContactAddressesModelList() != null){
				for(RetailerContactAddressesModel retAdd : retailerContactModel.getRetailerContactIdRetailerContactAddressesModelList()){
					retAdd = this.retailerContactAddressesDAO.findByPrimaryKey(retAdd.getRetailerContactAddressesId());
					if(retAdd.getAddressId() != null){
						retAdd.setAddressIdAddressModel(this.addressDAO.findByPrimaryKey(retAdd.getAddressId()));
					}
				}
			}
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		searchBaseWrapper.setBasePersistableModel(retailerContactModel);
		return searchBaseWrapper;
	}

	/**
	 * This is meant specifically for the deactivate case
	 * 
	 * @param baseWrapper
	 *            BaseWrapper
	 * @return BaseWrapper
	 */
	public BaseWrapper loadRetailerContact(BaseWrapper baseWrapper) {
		RetailerContactModel retailerModel = this.retailerContactDAO
				.findByPrimaryKey(baseWrapper.getBasePersistableModel()
						.getPrimaryKey());
		baseWrapper.setBasePersistableModel(retailerModel);
		return baseWrapper;
	}

	public SearchBaseWrapper searchRetailerContact(
			SearchBaseWrapper searchBaseWrapper) {
		CustomList<RetailerContactListViewModel> list = this.retailerContactListViewDAO
				.findByExample((RetailerContactListViewModel) searchBaseWrapper
						.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper
						.getSortingOrderMap());
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
	
	@Override
	public BaseWrapper loadRetailerContactByIAF(String initialAppFormNo) throws FrameworkCheckedException {
		RetailerContactModel retailerContactModel = new RetailerContactModel();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		retailerContactModel.setInitialAppFormNo(initialAppFormNo);
		
		ExampleConfigHolderModel configModel = new ExampleConfigHolderModel();
		configModel.setMatchMode(MatchMode.EXACT);
		configModel.setIgnoreCase(true);
		configModel.setEnableLike(false);
		
		
		CustomList<RetailerContactModel> list = retailerContactDAO.findByExample(retailerContactModel,null,null,configModel);
		if (null != list && null != list.getResultsetList() && list.getResultsetList().size() > 0) {
			retailerContactModel = list.getResultsetList().get(0);
			baseWrapper.setBasePersistableModel(retailerContactModel);
		}
		return baseWrapper;
	}
	
	
	public SearchBaseWrapper searchRetailerContactByMobileNo(
			SearchBaseWrapper searchBaseWrapper) {
		AppUserModel appUser = (AppUserModel) searchBaseWrapper
				.getBasePersistableModel();
		CustomList<AppUserModel> appUserList = this.appUserDAO
				.findByExample(appUser);

		if (null != appUserList && null != appUserList.getResultsetList()
				&& appUserList.getResultsetList().size() > 0) {
			appUser = appUserList.getResultsetList().get(0);
			if (appUser.getRetailerContactId() != null
					&& appUser.getRetailerContactId() > 0) {
				searchBaseWrapper
						.setBasePersistableModel(this.retailerContactDAO
								.findByPrimaryKey(appUser
										.getRetailerContactId()));
				return searchBaseWrapper;
			}
		}
		searchBaseWrapper.setBasePersistableModel(new RetailerContactModel());
		return searchBaseWrapper;

	}

	public BaseWrapper createRetailerContact(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		RetailerContactListViewFormModel retailerContactListViewFormModel = (RetailerContactListViewFormModel) baseWrapper
				.getObject("retailerContactListViewFormModel");
		int recordCount = 0;
		RetailerContactModel retailerContactModel = new RetailerContactModel();
		RetailerContactModel newRetailerContactModel = new RetailerContactModel();
		RetailerContactModel tempRetailerContactModel = new RetailerContactModel();

		if (retailerContactListViewFormModel.getHead() != null
				&& retailerContactListViewFormModel.getHead() == true) {
			newRetailerContactModel
					.setRetailerId(retailerContactListViewFormModel
							.getRetailerId());
			newRetailerContactModel.setHead(retailerContactListViewFormModel
					.getHead());
			ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setMatchMode(MatchMode.EXACT);
			recordCount = retailerContactDAO.countByExample(
					newRetailerContactModel, exampleHolder);
			if (recordCount != 0) {
				throw new FrameworkCheckedException("HeadUniqueException");
			}

		}
		
		retailerContactModel.setRetailerId(retailerContactListViewFormModel
				.getRetailerId());
		retailerContactModel.setAreaId(retailerContactListViewFormModel
				.getAreaId());

		retailerContactModel
				.setHead(retailerContactListViewFormModel.getHead() == null ? false
						: retailerContactListViewFormModel.getHead());
		/*
		 * retailerContactModel.setActive(retailerContactListViewFormModel
		 * .getActive() == null ? false : retailerContactListViewFormModel
		 * .getActive());
		 */

		retailerContactModel.setActive(true);
		retailerContactModel.setComments(null);
		retailerContactModel.setDescription(null);
		/*
		 * retailerContactModel.setBalance(retailerContactListViewFormModel
		 * .getBalance());
		 */
		retailerContactModel.setBalance(0.0);
		retailerContactModel.setUpdatedOn(new Date());

		retailerContactModel.setUpdatedByAppUserModel(UserUtils
				.getCurrentUser());
		retailerContactModel.setCreatedByAppUserModel(UserUtils
				.getCurrentUser());
		retailerContactModel.setCreatedOn(new Date());
		
		//New fields added by fahad
		retailerContactModel.setApplicationNo(retailerSequenceGenerator.nextLongValue().toString());
		retailerContactModel.setZongMsisdn(retailerContactListViewFormModel.getZongMsisdn());
		retailerContactModel.setAccountOpeningDate(retailerContactListViewFormModel.getAccountOpeningDate());
		retailerContactModel.setNtn(retailerContactListViewFormModel.getNtnNo());
		retailerContactModel.setContactNo(retailerContactListViewFormModel.getContactNo());
		retailerContactModel.setLandLineNo(retailerContactListViewFormModel.getLandLineNo());
		retailerContactModel.setMobileNo(retailerContactListViewFormModel.getMobileNumber());
//		retailerContactModel.setOperatorsNo(retailerContactListViewFormModel.getOperatorNo());
		retailerContactModel.setBusinessName(retailerContactListViewFormModel.getBusinessName());
		retailerContactModel.setNatureOfBusinessId(retailerContactListViewFormModel.getNatureOfBusiness());
		
		retailerContactModel = this.retailerContactDAO
				.saveOrUpdate(retailerContactModel);
		
		AddressModel businessAddress = new AddressModel();
		businessAddress.setHouseNo(retailerContactListViewFormModel.getShopNo());
		businessAddress.setStreetNo(retailerContactListViewFormModel.getMarketPlaza());
		businessAddress.setDistrictId(retailerContactListViewFormModel.getDistrictTehsilTown());
		businessAddress.setCityId(retailerContactListViewFormModel.getCityVillage());
		businessAddress.setPostalOfficeId(retailerContactListViewFormModel.getPostOffice());
		businessAddress.setNtn(retailerContactListViewFormModel.getNtnNumber());
		businessAddress.setNearestLandMark(retailerContactListViewFormModel.getNearestLandmark());
		
		businessAddress = this.addressDAO.saveOrUpdate(businessAddress);
		
		RetailerContactAddressesModel retailerContactAddressesModel = new RetailerContactAddressesModel();
		retailerContactAddressesModel.setAddressId(businessAddress.getAddressId());
		retailerContactAddressesModel.setAddressTypeId(3L);
		retailerContactAddressesModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		
		retailerContactAddressesModel = this.retailerContactAddressesDAO.saveOrUpdate(retailerContactAddressesModel);
		
		
		retailerContactListViewFormModel
				.setRetailerContactId(retailerContactModel
						.getRetailerContactId());

		baseWrapper.putObject("retailerContactListViewFormModel",
				retailerContactListViewFormModel);

		createAppUserForRetailerContact(baseWrapper);
		baseWrapper.setBasePersistableModel(retailerContactModel);
		
		// completing action log
		AppUserModel appUsrMod = (AppUserModel)baseWrapper.getObject("savedAppUserModel");
		actionLogModel.setEndTime( new Timestamp(new Date().getTime()) );
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // for process ends
		actionLogModel.setCustomField11(retailerContactModel.getRetailerContactId().toString());
		actionLogModel.setCustomField1(String.valueOf(appUsrMod.getAppUserId()) );
		actionLogModel = logAction(actionLogModel);

		return baseWrapper;
	}

	public RetailerContactModel findRetailerContactByMobileNumber(
			SearchBaseWrapper searchBaseWrapper) {
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);

		AppUserModel appUser = (AppUserModel) searchBaseWrapper
				.getBasePersistableModel();
		CustomList<AppUserModel> appUserList = this.appUserDAO.findByExample(
				appUser, null, null, exampleHolder);

		if (null != appUserList && null != appUserList.getResultsetList()
				&& appUserList.getResultsetList().size() > 0) {
			appUser = getRetailerContactFrmList(appUserList.getResultsetList());
			if (appUser != null) {
				return this.retailerContactDAO.findByPrimaryKey(appUser
						.getRetailerContactId());
			}
		}
		return null;

	}
	
	public RetailerContactModel findHeadRetailerContactByRetailerId(Long RetailerId) {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		
		RetailerContactModel headRetailerContactModel = new RetailerContactModel();
	    headRetailerContactModel.setRetailerId(RetailerId);
	    headRetailerContactModel.setHead(Boolean.TRUE);
	    
	    searchBaseWrapper.setBasePersistableModel(headRetailerContactModel);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);

		AppUserModel appUser = (AppUserModel) searchBaseWrapper.getBasePersistableModel();
		CustomList<AppUserModel> appUserList = this.appUserDAO.findByExample(appUser, null, null, exampleHolder);

		if (null != appUserList && null != appUserList.getResultsetList() && appUserList.getResultsetList().size() > 0) {
			appUser = getRetailerContactFrmList(appUserList.getResultsetList());
			if (appUser != null) {
				return this.retailerContactDAO.findByPrimaryKey(appUser.getRetailerContactId());
			}
		}
		
		return null;

	}

	private AppUserModel getRetailerContactFrmList(
			List<AppUserModel> appUserList) {
		for (AppUserModel appUserModel : appUserList) {
			if (appUserModel.getRetailerContactId() != null
					&& appUserModel.getRetailerContactId() > 0)
				return appUserModel;
		}
		return null;
	}

	public BaseWrapper loadRetailerContactByAppUser(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		AppUserModel appUser = (AppUserModel) baseWrapper
				.getBasePersistableModel();
		appUser = this.appUserDAO.findByPrimaryKey(appUser.getAppUserId());

		if (appUser.getRetailerContactId() != null
				&& appUser.getRetailerContactId() > 0) {
			baseWrapper.setBasePersistableModel(this.retailerContactDAO
					.findByPrimaryKey(appUser.getRetailerContactId()));
			return baseWrapper;
		}
		baseWrapper.setBasePersistableModel(new RetailerContactModel());
		return baseWrapper;
	}

	public BaseWrapper createOrUpdateRetailerContact(BaseWrapper baseWrapper) {
		RetailerContactModel retailerContactModel = this.retailerContactDAO
				.saveOrUpdate((RetailerContactModel) baseWrapper
						.getBasePersistableModel());
		baseWrapper.setBasePersistableModel(retailerContactModel);
		return baseWrapper;
	}

	public RetailerContactModel findRetailerHeadContact(
			SearchBaseWrapper searchBaseWrapper) {
		RetailerContactModel retailerContactModel = (RetailerContactModel) searchBaseWrapper
				.getBasePersistableModel();
		retailerContactModel.setHead(true);
		CustomList<RetailerContactModel> list = this.retailerContactDAO
				.findByExample(retailerContactModel);
		if (null != list && null != list.getResultsetList()
				&& list.getResultsetList().size() > 0) {
			retailerContactModel = list.getResultsetList().get(0);
		} else
			retailerContactModel = new RetailerContactModel();

		return retailerContactModel;
	}

	public BaseWrapper updateRetailerContact(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {

		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setActionId((Long) baseWrapper
				.getObject(PortalConstants.KEY_ACTION_ID));
		actionLogModel.setUsecaseId((Long) baseWrapper
				.getObject(PortalConstants.KEY_USECASE_ID));
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
		actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
		actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
		actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
		actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
		// actionLogModel.setCustomField2("Updating the User Information");
		actionLogModel = logAction(actionLogModel);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		RetailerContactListViewFormModel retailerContactListViewFormModel = (RetailerContactListViewFormModel) baseWrapper
				.getObject("retailerContactListViewFormModel");
		RetailerContactModel newRetailerContactModel = new RetailerContactModel();
		RetailerContactModel retailerContactModel = retailerContactDAO
				.findByPrimaryKey(retailerContactListViewFormModel
						.getRetailerContactId());
		int recordCount = 0;
		RetailerContactListViewModel retailerContactListViewModel_1 = new RetailerContactListViewModel();
		retailerContactListViewModel_1 = retailerContactListViewDAO.findByPrimaryKey(retailerContactListViewFormModel.getRetailerContactId());
		AppUserModel appUserModel = new AppUserModel();
		appUserModel = appUserManager.getUser(String.valueOf(retailerContactListViewModel_1.getAppUserId()));

		String dbMobileNo = appUserModel.getMobileNo();

		/*
		if (!appUserModel.getMobileNo().equals(
				retailerContactListViewFormModel.getZongMsisdn())) {
			if (!this.isMobileNumUnique(retailerContactListViewFormModel
					.getZongMsisdn()))
				throw new FrameworkCheckedException("MobileNumUniqueException");
			
		}

		String oldMobileNo = appUserModel.getMobileNo();
		*/

		if (!appUserModel.getUsername().equals(retailerContactListViewFormModel.getUsername())) {
			if (!this.isUserNameUnique(retailerContactListViewFormModel.getUsername()))
				throw new FrameworkCheckedException("UserNameUniqueException");
		}

//		appUserModel.setVerified(retailerContactListViewFormModel.getVerified() == null ? false : retailerContactListViewFormModel.getVerified());
		retailerContactModel.setRetailerId(retailerContactListViewFormModel.getRetailerId());
		
		/*
		if (!retailerContactModel.getHead().equals(
				retailerContactListViewFormModel.getHead())) {
			if (retailerContactListViewFormModel.getHead() != null
					&& retailerContactListViewFormModel.getHead() == true) {
				newRetailerContactModel
						.setRetailerId(retailerContactListViewFormModel
								.getRetailerId());
				newRetailerContactModel
						.setHead(retailerContactListViewFormModel.getHead());
				ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
				exampleHolder.setMatchMode(MatchMode.EXACT);
				recordCount = retailerContactDAO.countByExample(
						newRetailerContactModel, exampleHolder);
				if (recordCount != 0) {
					throw new FrameworkCheckedException("HeadUniqueException");
				}

				retailerContactModel.setHead(retailerContactListViewFormModel
						.getHead());

			} else {
				retailerContactModel.setHead(retailerContactListViewFormModel
						.getHead() == null ? false
						: retailerContactListViewFormModel.getHead());
			}

		}
		*/

		/*
		 * retailerContactModel.setActive(retailerContactListViewFormModel
		 * .getActive() == null ? false : appUserModel .getAccountEnabled());
		retailerContactModel.setBalance(retailerContactListViewFormModel
				.getBalance());
		 */

		retailerContactModel.setRetailerId(retailerContactListViewFormModel.getRetailerId());
		retailerContactModel.setAreaId(retailerContactListViewFormModel.getAreaId());
//		retailerContactModel.setHead(retailerContactListViewFormModel.getHead() == null ? false : retailerContactListViewFormModel.getHead());
		
		/*
		 * retailerContactModel.setActive(retailerContactListViewFormModel
		 * .getActive() == null ? false : retailerContactListViewFormModel
		 * .getActive());
		 */

		retailerContactModel.setZongMsisdn(retailerContactListViewFormModel.getZongMsisdn());
		retailerContactModel.setAccountOpeningDate(retailerContactListViewFormModel.getAccountOpeningDate());
		retailerContactModel.setNtn(retailerContactListViewFormModel.getNtnNo());
		retailerContactModel.setContactNo(retailerContactListViewFormModel.getContactNo());
		retailerContactModel.setLandLineNo(retailerContactListViewFormModel.getLandLineNo());
		//retailerContactModel.setMobileNo(retailerContactListViewFormModel.getMobileNumber());
		retailerContactModel.setBusinessName(retailerContactListViewFormModel.getBusinessName());
		retailerContactModel.setNatureOfBusinessId(retailerContactListViewFormModel.getNatureOfBusiness());

		retailerContactModel.setComments(retailerContactListViewFormModel.getComments());
		retailerContactModel.setDescription(retailerContactListViewFormModel.getDescription());
		retailerContactModel.setUpdatedOn(new Date());
		retailerContactModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		
		try {
			if(retailerContactModel.getRetailerContactIdRetailerContactAddressesModelList() != null){
				for(RetailerContactAddressesModel retAdd : retailerContactModel.getRetailerContactIdRetailerContactAddressesModelList()){
					AddressModel businessAddress = retAdd.getAddressIdAddressModel();
					businessAddress.setHouseNo(retailerContactListViewFormModel.getShopNo());
					businessAddress.setStreetNo(retailerContactListViewFormModel.getMarketPlaza());
					businessAddress.setDistrictId(retailerContactListViewFormModel.getDistrictTehsilTown());
					businessAddress.setCityId(retailerContactListViewFormModel.getCityVillage());
					businessAddress.setPostalOfficeId(retailerContactListViewFormModel.getPostOffice());
					businessAddress.setNtn(retailerContactListViewFormModel.getNtnNumber());
					businessAddress.setNearestLandMark(retailerContactListViewFormModel.getNearestLandmark());
					businessAddress = this.addressDAO.saveOrUpdate(businessAddress);

					//retAdd = this.retailerContactAddressesDAO.findByPrimaryKey(retAdd.getRetailerContactAddressesId());
					//if(retAdd.getAddressId() != null){
					//	retAdd.setAddressIdAddressModel(this.addressDAO.findByPrimaryKey(retAdd.getAddressId()));
					//}
				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
			throw new FrameworkCheckedException(exp.getMessage());
		}
		
/*		RetailerContactAddressesModel retailerContactAddressesModel = new RetailerContactAddressesModel();
		retailerContactAddressesModel.setAddressId(businessAddress.getAddressId());
		retailerContactAddressesModel.setAddressTypeId(3L);
		retailerContactAddressesModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		
		retailerContactAddressesModel = this.retailerContactAddressesDAO.saveOrUpdate(retailerContactAddressesModel);
*/
		
		/*
		 * retailerContactModel.setActive(retailerContactListViewFormModel
		 * .getAccountEnabled() == null ? false :
		 * retailerContactListViewFormModel.getAccountEnabled());
		 */

		appUserModel.setFirstName(retailerContactListViewFormModel.getFirstName());
		appUserModel.setLastName(retailerContactListViewFormModel.getLastName());
		appUserModel.setAddress1(retailerContactListViewFormModel.getAddress1());
		appUserModel.setAddress2(retailerContactListViewFormModel.getAddress2());
		appUserModel.setCity(retailerContactListViewFormModel.getCity());
		appUserModel.setState(retailerContactListViewFormModel.getState());
		appUserModel.setCountry(retailerContactListViewFormModel.getCountry());
		appUserModel.setZip(retailerContactListViewFormModel.getZip());
		appUserModel.setNic(retailerContactListViewFormModel.getCnicNo());
		appUserModel.setNicExpiryDate(retailerContactListViewFormModel.getCnicExpiryDate());

		//appUserModel.setDob(retailerContactListViewFormModel.getDob());
		//appUserModel.setEmail(retailerContactListViewFormModel.getEmail());
		//appUserModel.setFax(retailerContactListViewFormModel.getFax());
		//appUserModel.setMotherMaidenName(retailerContactListViewFormModel.getMotherMaidenName());
		//appUserModel.setPasswordHint(retailerContactListViewFormModel.getPasswordHint());
		//if (retailerContactListViewFormModel.getPassword() != null){
		//	appUserModel.setPassword(EncoderUtils.encodeToSha(retailerContactListViewFormModel.getPassword()));
		//}
		//appUserModel.setMobileNo(retailerContactListViewFormModel.getMobileNumber());
		//appUserModel.setMobileTypeId(1L);
		//appUserModel.setAccountEnabled(retailerContactListViewFormModel.getAccountEnabled() == null ? false : retailerContactListViewFormModel.getAccountEnabled());
		//appUserModel.setAccountExpired(retailerContactListViewFormModel.getAccountExpired() == null ? false : retailerContactListViewFormModel.getAccountExpired());
		//appUserModel.setAccountLocked(retailerContactListViewFormModel.getAccountLocked() == null ? false : retailerContactListViewFormModel.getAccountLocked());
		//appUserModel.setCredentialsExpired(retailerContactListViewFormModel.getCredentialsExpired() == null ? false : retailerContactListViewFormModel.getCredentialsExpired());
		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setUpdatedOn(new Date());

/*
		// check appuser userDeviceAccount
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserIdAppUserModel(appUserModel);
		userDeviceAccountsModel
				.setDeviceTypeId(DeviceTypeConstantsInterface.USSD);
		int recordCountUserDevice = userDeviceAccountsDAO
				.countByExample(userDeviceAccountsModel);

*/
		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel.setCustomField1(String.valueOf(appUserModel.getAppUserId()));
		actionLogModel.setCustomField11(retailerContactModel.getRetailerContactId().toString());
		actionLogModel = logAction(actionLogModel);

		retailerContactModel = retailerContactDAO.saveOrUpdate(retailerContactModel);

		try{
			appUserDAO.saveUser(appUserModel);
		}catch (Exception e) {
			if(e.getMessage().contains("i8-20001")){
				throw new FrameworkCheckedException("MobileNumUniqueException");
			}else{
				throw new FrameworkCheckedException(e.getMessage());
			}
	  }
		
		AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
	    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
	    //appUserPartnerGroupModel.setPartnerGroupId(mnoUserFormModel.getPartnerGroupId());
	    CustomList list=appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
	    if(list.getResultsetList().size()>0)
	    {
	    	
	    	appUserPartnerGroupModel=(AppUserPartnerGroupModel)list.getResultsetList().get(0);
	    	appUserPartnerGroupModel.setPartnerGroupId(retailerContactListViewFormModel.getPartnerGroupId());
	    	appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		    appUserPartnerGroupModel.setUpdatedOn(new Date());
		    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
	    }

	    baseWrapper.setBasePersistableModel(retailerContactModel);
		return baseWrapper;
	}

	public BaseWrapper createAppUserForRetailerContact(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {

		AppUserModel appUserModel = new AppUserModel();

		RetailerContactListViewFormModel retailerContactListViewFormModel = (RetailerContactListViewFormModel) baseWrapper
				.getObject("retailerContactListViewFormModel");

		if (!this.isMobileNumUnique(retailerContactListViewFormModel
				.getMobileNumber()))
			throw new FrameworkCheckedException("MobileNumUniqueException");
		
		if (!this.isUserNameUnique(retailerContactListViewFormModel
				.getUsername()))
			throw new FrameworkCheckedException("UserNameUniqueException");

		Date nowDate = new Date();

		Long id;
		id = retailerContactListViewFormModel.getRetailerContactId();
		appUserModel.setFirstName(retailerContactListViewFormModel.getFirstName());
		appUserModel
				.setLastName(retailerContactListViewFormModel.getLastName());
		appUserModel
				.setAddress1(null);
		appUserModel
				.setAddress2(null);

		appUserModel
				.setMobileNo(retailerContactListViewFormModel.getMobileNumber());
		appUserModel.setNic(retailerContactListViewFormModel.getCnicNo());
		appUserModel.setNicExpiryDate(retailerContactListViewFormModel.getCnicExpiryDate());
		appUserModel.setRetailerContactId(id);
		appUserModel.setCity(null);
		appUserModel.setCountry(null);
		appUserModel.setState(null);
		appUserModel.setDob(null);
		appUserModel.setMobileTypeId(1L);
		appUserModel.setEmail(null);
		appUserModel.setFax(null);
		appUserModel.setMotherMaidenName(null);
		appUserModel
				.setUsername(retailerContactListViewFormModel.getUsername());
		appUserModel.setPassword(EncoderUtils
				.encodeToSha(retailerContactListViewFormModel.getPassword()));
		appUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setPasswordHint(null);
		appUserModel.setZip(null);
		appUserModel.setCreatedOn(nowDate);
		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setUpdatedOn(nowDate);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);

		appUserModel
				.setVerified(true);
		appUserModel.setAccountEnabled(true);
		appUserModel.setAccountExpired(retailerContactListViewFormModel
				.getAccountExpired() == null ? false
				: retailerContactListViewFormModel.getAccountExpired());
		appUserModel.setAccountLocked(retailerContactListViewFormModel
				.getAccountLocked() == null ? false
				: retailerContactListViewFormModel.getAccountLocked());
		appUserModel.setCredentialsExpired(retailerContactListViewFormModel
				.getCredentialsExpired() == null ? false
				: retailerContactListViewFormModel.getCredentialsExpired());
		appUserModel.setPasswordChangeRequired(false);
		try
		{
			this.appUserDAO.saveUser(appUserModel);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			if(e.getMessage().contains("i8-20001"))
			{
				throw new FrameworkCheckedException("MobileNumUniqueException");
			}
			else
			{
				throw new FrameworkCheckedException(e.getMessage());
			}
	  }
		
		AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
	    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
	    appUserPartnerGroupModel.setPartnerGroupId(retailerContactListViewFormModel.getPartnerGroupId());
	    appUserPartnerGroupModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
	    appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	    appUserPartnerGroupModel.setCreatedOn(new Date());
	    appUserPartnerGroupModel.setUpdatedOn(new Date());
	    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);

		// createUserDeviceAccount(appUserModel,retailerContactListViewFormModel);
	   baseWrapper.putObject("savedAppUserModel", appUserModel);
		return baseWrapper;
	}

	private void createUserDeviceAccount(AppUserModel appUserModel,
			RetailerContactListViewFormModel retailerContactListViewFormModel)
			throws FrameworkCheckedException {

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		Date nowDate = new Date();

		userDeviceAccountsModel
				.setAccountEnabled(retailerContactListViewFormModel
						.getAccountEnabled() == null ? false
						: retailerContactListViewFormModel.getAccountEnabled());
		userDeviceAccountsModel
				.setAccountExpired(retailerContactListViewFormModel
						.getAccountExpired() == null ? false
						: retailerContactListViewFormModel.getAccountExpired());
		userDeviceAccountsModel
				.setAccountLocked(retailerContactListViewFormModel
						.getAccountLocked() == null ? false
						: retailerContactListViewFormModel.getAccountLocked());
		userDeviceAccountsModel
				.setCredentialsExpired(retailerContactListViewFormModel
						.getCredentialsExpired() == null ? false
						: retailerContactListViewFormModel
								.getCredentialsExpired());
		userDeviceAccountsModel.setCreatedByAppUserModel(UserUtils
				.getCurrentUser());
		userDeviceAccountsModel.setCreatedOn(nowDate);
		userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils
				.getCurrentUser());
		userDeviceAccountsModel.setUpdatedOn(nowDate);
		userDeviceAccountsModel.setPinChangeRequired(true);
		userDeviceAccountsModel
				.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);

		String mfsId = computeMfsId();

		String randomPin = RandomUtils.generateRandom(4, false, true);

		userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
		userDeviceAccountsModel.setUserId(mfsId);
		userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(randomPin));
		userDeviceAccountsModel = this.userDeviceAccountsDAO
				.saveOrUpdate(userDeviceAccountsModel);

		//Updated against CRF-28.
		
		Object[] args = {mfsId, randomPin};
		
		String messageString = MessageUtil.getMessage("customer.mfsAccountCreated", args);
		
//		String messageString = "Dear Customer Your New MWallet Account is created Your MFSID is:"
//				+ mfsId + " and Pin is: " + randomPin;
		SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(),
				messageString, SMSConstants.Sender_1611);
		smsSender.send(smsMessage);

	}

	private boolean isMobileNumUnique(String mobileNo) {
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(mobileNo);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int count = this.appUserDAO.countByExample(appUserModel, exampleHolder);
		if (count == 0){
			appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			count = this.appUserDAO.countByExample(appUserModel,null,exampleHolder);
			if(count == 0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	private boolean isUserNameUnique(String userName) {
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setUsername(userName);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		int count = this.appUserDAO.countByExample(appUserModel, exampleHolder);
		if (count == 0)
			return true;
		else
			return false;
	}

	private String computeMfsId() {
		AppUserModel appUserModel;
		GoldenNosModel goldenNosModel;
		Long nextLongValue = null;
		boolean flag = true;

		while (flag) {
			nextLongValue = this.sequenceGenerator.nextLongValue();
			appUserModel = new AppUserModel();
			goldenNosModel = new GoldenNosModel();
			appUserModel.setUsername(String.valueOf(nextLongValue));
			goldenNosModel.setGoldenNumber(String.valueOf(nextLongValue));
			ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setMatchMode(MatchMode.EXACT);
			int countAppUser = this.appUserDAO.countByExample(appUserModel,
					exampleHolder);
			int countGoldenNos = this.goldenNosDAO.countByExample(
					goldenNosModel, exampleHolder);
			if (countAppUser == 0 && countGoldenNos == 0) {
				flag = false;
			}
		}

		return String.valueOf(nextLongValue);
	}

	private ActionLogModel logAction(ActionLogModel actionLogModel)
			throws FrameworkCheckedException {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		if (null == actionLogModel.getActionLogId()) {
			baseWrapper = this.actionLogManager
					.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
		} else {
			baseWrapper = this.actionLogManager
					.createOrUpdateActionLog(baseWrapper);
		}
		return (ActionLogModel) baseWrapper.getBasePersistableModel();
	}

	
	
	
	public  Long getAppUserPartnerGroupId(Long appUserId)throws FrameworkCheckedException 
	{
		AppUserPartnerGroupModel appUserPartnerGroupModel = new	AppUserPartnerGroupModel();
		appUserPartnerGroupModel.setAppUserId(appUserId);
		
		

		CustomList list = this.appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
		if(list!=null && list.getResultsetList().size()>0)
		{
		appUserPartnerGroupModel =(AppUserPartnerGroupModel)list.getResultsetList().get(0);
		return appUserPartnerGroupModel.getPartnerGroupId();
		}
		else
			throw new FrameworkCheckedException("User doest not belong to any partner group");	
		
		
	}
	public AppUserPartnerGroupModel getAppUserPartnetGroupByAppUserId(Long appUserId)throws FrameworkCheckedException{
		AppUserPartnerGroupModel appUserPartnerGroupModel = new	AppUserPartnerGroupModel();
		appUserPartnerGroupModel.setAppUserId(appUserId);
		
		

		CustomList list = this.appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel);
		if(list!=null && list.getResultsetList().size()>0)
		{
		appUserPartnerGroupModel =(AppUserPartnerGroupModel)list.getResultsetList().get(0);
		return appUserPartnerGroupModel;
		}
		else
			throw new FrameworkCheckedException("User doest not belong to any partner group");	
	}
	
	@Override
	 public BaseWrapper loadHeadRetailerContactAppUser( Long retailerId ) throws FrameworkCheckedException, Exception
	 {
		BaseWrapper baseWrapper = new BaseWrapperImpl(); 
		AppUserModel appUser = null;
//		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		
		RetailerContactModel headRetailerContactModel = new RetailerContactModel();
	    headRetailerContactModel.setRetailerId(retailerId);
	    headRetailerContactModel.setHead(Boolean.TRUE);
	    
//	    searchBaseWrapper.setBasePersistableModel(headRetailerContactModel);
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);

//		appUser = (AppUserModel) searchBaseWrapper.getBasePersistableModel();
		CustomList<RetailerContactModel> retContactList = this.retailerContactDAO.findByExample(headRetailerContactModel, null, null, exampleHolder);

		if (null != retContactList && null != retContactList.getResultsetList() && retContactList.getResultsetList().size() > 0) {
//			appUser = getRetailerContactFrmList(retContactList.getResultsetList());
			headRetailerContactModel = retContactList.getResultsetList().get(0);
			
			if (headRetailerContactModel != null && headRetailerContactModel.getRetailerContactId() > 0) {
				appUser = new AppUserModel();
				appUser.setRetailerContactId(headRetailerContactModel.getRetailerContactId());
				
				CustomList<AppUserModel> appUserList  = this.appUserDAO.findByExample(appUser, null, null, exampleHolder);
				if (null != appUserList && null != appUserList.getResultsetList() && appUserList.getResultsetList().size() > 0) {
					appUser = getRetailerContactFrmList(appUserList.getResultsetList());
				}
				 if(appUser != null && appUser.getRetailerContactId() != null){
//					 appUser.setRelationRetailerContactIdRetailerContactModel(headRetailerContactModel);
					 baseWrapper.putObject("headRetailerContactModel", headRetailerContactModel);
					 baseWrapper.putObject("appUserModel", appUser);
				 }
				 
				 UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
				 userDeviceAccountsModel.setAppUserIdAppUserModel(appUser);
				 userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.USSD);
				 CustomList<UserDeviceAccountsModel> deviceAcctsList = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel, null, null, exampleHolder);
				 Collection<UserDeviceAccountsModel> userDeviceAccountsList = (Collection<UserDeviceAccountsModel>)deviceAcctsList.getResultsetList();
				 if(deviceAcctsList != null && deviceAcctsList.getResultsetList() != null && deviceAcctsList.getResultsetList().size() > 0){
					 for(UserDeviceAccountsModel model: userDeviceAccountsList){
						 baseWrapper.putObject("userDeviceAccountsModel", model);
					 }
//					 appUser.setAppUserIdUserDeviceAccountsModelList(userDeviceAccountsList);
				 }
				 
				 
			}
		}
		
		return baseWrapper;
	 }
	
	@Override
	public RetailerContactModel findHeadRetailerContactModelByRetailerId( Long retailerId ) throws FrameworkCheckedException
	 {
//	     return retailerContactDAO.findHeadRetailerContactModelByRetailerId( retailerId );
		return null;
	 }
	
	@Override
	public SearchBaseWrapper loadRetailerByInitialAppFormNo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		RetailerContactModel	retailerContactModel=(RetailerContactModel)searchBaseWrapper.getBasePersistableModel();
		
		CustomList<RetailerContactModel>list=retailerContactDAO.findByExample(retailerContactModel, null,null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		searchBaseWrapper.setCustomList(list);
		return searchBaseWrapper;
	}
		
	public void setRetailerContactDAO(RetailerContactDAO retailerContactDAO) {
		this.retailerContactDAO = retailerContactDAO;
	}

	public void setRetailerContactListViewDAO(
			RetailerContactListViewDAO retailerContactListViewDAO) {
		this.retailerContactListViewDAO = retailerContactListViewDAO;
	}

	public void setAppUserDAO(AppUserDAO appUserDAO) {
		this.appUserDAO = appUserDAO;
	}

	public void setGoldenNosDAO(GoldenNosDAO goldenNosDAO) {
		this.goldenNosDAO = goldenNosDAO;
	}

	public void setSequenceGenerator(
			OracleSequenceGeneratorJdbcDAO sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public void setUserDeviceAccountsDAO(
			UserDeviceAccountsDAO userDeviceAccountsDAO) {
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}




	public void setAppUserPartnerGroupDAO(
			AppUserPartnerGroupDAO appUserPartnerGroupDAO) {
		this.appUserPartnerGroupDAO = appUserPartnerGroupDAO;
	}

	public void setAddressDAO(AddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}

	public void setRetailerContactAddressesDAO(
			RetailerContactAddressesDAO retailerContactAddressesDAO) {
		this.retailerContactAddressesDAO = retailerContactAddressesDAO;
	}
	
	@Override
	public int countByExample(RetailerContactModel agentModel,
			ExampleConfigHolderModel exampleConfigHolder) {
		
		return retailerContactDAO.countByExample(agentModel, exampleConfigHolder);
	}

	@Override
	public SearchBaseWrapper loadRetailerContactAddresses(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		RetailerContactAddressesModel	retailerContactAddressesModel=(RetailerContactAddressesModel)searchBaseWrapper.getBasePersistableModel();
		
		ExampleConfigHolderModel exampleConfigHolderModel=new ExampleConfigHolderModel();
		exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
		
		RetailerContactAddressesModel retailerContactAddressesModelResult=	retailerContactAddressesDAO.findRetailerContactAddress(retailerContactAddressesModel.getRetailerContactId(),retailerContactAddressesModel.getAddressTypeId(), retailerContactAddressesModel.getApplicantDetailId());
		
		List<AddressModel> addressList=new ArrayList<AddressModel>();
		
		if(null !=retailerContactAddressesModelResult){			
			AddressModel	addressModel	=	addressDAO.findByPrimaryKey(retailerContactAddressesModelResult.getAddressId());
			addressList.add(addressModel);
		}
		
		searchBaseWrapper.setCustomList(new CustomList<>(addressList));
		return searchBaseWrapper;
	}

	@Override
	public List<RetailerContactDetailVO> findRetailerContactModelList()
			throws FrameworkCheckedException {
		return retailerContactSearchViewDAO.findRetailerContactModelList();
	}

	public void setRetailerContactSearchViewDAO(
			RetailerContactSearchViewDAO retailerContactSearchViewDAO) {
		this.retailerContactSearchViewDAO = retailerContactSearchViewDAO;
	}

	@Override
	public RetailerContactModel loadRetailerContactByRetailerContactId(Long retailerContactId) throws FrameworkCheckedException {
		
		RetailerContactModel  model = new RetailerContactModel();
		model = retailerContactDAO.findByPrimaryKey(retailerContactId);
		if(model == null){
			throw new FrameworkCheckedException("RetailerContactModelNotFoundException");
		}
		return model;
	}

	@Override
	public RetailerContactModel saveOrUpdateRetailerContactModel(RetailerContactModel rcModel) throws FrameworkCheckedException {
		rcModel = this.retailerContactDAO.saveOrUpdate(rcModel);
		return rcModel;
	}

}
