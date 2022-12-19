/**
 * 
 */
package com.inov8.microbank.server.service.portal.level3account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.exception.ConstraintViolationException;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;
import com.inov8.microbank.common.exception.ImplementationNotSupportedException;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.level3account.Level3AccountsViewModel;
import com.inov8.microbank.common.model.portal.linkpaymentmodemodule.LinkPaymentModeModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.BusinessDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.NokDetailVOModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.ivr.IvrRequestHandler;
import com.inov8.microbank.server.dao.addressmodule.AddressDAO;
import com.inov8.microbank.server.dao.addressmodule.RetailerContactAddressesDAO;
import com.inov8.microbank.server.dao.agenthierarchymodule.SalesHierarchyDAO;
import com.inov8.microbank.server.dao.appusermobilehistorymodule.AppUserMobileHistoryDAO;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;
import com.inov8.microbank.server.dao.bankmodule.BankDAO;
import com.inov8.microbank.server.dao.customermodule.ApplicantDetailDAO;
import com.inov8.microbank.server.dao.customermodule.BusinessDetailDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerPictureDAO;
import com.inov8.microbank.server.dao.geolocationmodule.GeoLocationDAO;
import com.inov8.microbank.server.dao.mfsmodule.SMAcctInfoListViewDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.portal.agentmerchantdetailmodule.AgentMerchantDetailDAO;
import com.inov8.microbank.server.dao.portal.allpaymodule.AllpayDelinkRelinkPaymentModeViewDAO;
import com.inov8.microbank.server.dao.portal.businesstype.BusinessTypeDAO;
import com.inov8.microbank.server.dao.portal.citymodule.CityDAO;
import com.inov8.microbank.server.dao.portal.kycmodule.ACOwnerShipDAO;
import com.inov8.microbank.server.dao.portal.kycmodule.KYCDAO;
import com.inov8.microbank.server.dao.portal.level3account.Level3AccountsViewDao;
import com.inov8.microbank.server.dao.portal.linkpaymentmodemodule.LinkPaymentModeDAO;
import com.inov8.microbank.server.dao.portal.occupationmodule.OccupationDAO;
import com.inov8.microbank.server.dao.portal.professionmodule.ProfessionDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.portal.linkpaymentmodemodule.LinkPaymentModeManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.dao.mainmodule.AccountInfoDAO;

/**
 * @author NaseerUl 
 *
 */ 
public class Level3AccountManagerImpl implements Level3AccountManager
{
	private OracleSequenceGeneratorJdbcDAO retailerSequenceGenerator;
	private OracleSequenceGeneratorJdbcDAO allpaySequenceGenerator;
	private ActionLogManager actionLogManager;
	private RetailerContactDAO retailerContactDAO;
	private AddressDAO addressDAO;
	private RetailerContactAddressesDAO retailerContactAddressesDAO;
	private AppUserDAO appUserDAO;
	private UserDeviceAccountsDAO userDeviceAccountsDAO;
	private ApplicantDetailDAO applicantDetailDAO;
	private BusinessDetailDAO businessDetailDAO;
	private CustomerPictureDAO customerPictureDAO;
	private BankDAO bankDAO;
	private LinkPaymentModeDAO linkPaymentModeDAO;
	private FinancialIntegrationManager financialIntegrationManager;
	private LinkPaymentModeManager linkPaymentModeManager;
	private FinancialInstitution olaVeriflyFinancialInstitution;
	private Level3AccountsViewDao level3AccountsViewDao;
	private SmsSender smsSender;
	private ACOwnerShipDAO acOwnerShipDetailDAO;
	private AgentMerchantDetailDAO	agentMerchantDetailDAO;
	private AgentHierarchyManager	agentHierarchyManager;
	private SalesHierarchyDAO	salesHierarchyDAO;
	private AppUserPartnerGroupDAO	appUserPartnerGroupDAO;
	private SmartMoneyAccountManager	smartMoneyAccountManager;
	private KYCDAO					kycDAO;
	private AllpayDelinkRelinkPaymentModeViewDAO	allpayDelinkRelinkPaymentModeViewDAO;
	private SMAcctInfoListViewDAO	smAcctInfoListViewDAO;
	private BusinessTypeDAO	businessTypeDAO;
	private CityDAO	cityDAO;
	private ProfessionDAO	professionDAO;
	private OccupationDAO	occupationDAO;
	private IvrRequestHandler		ivrRequestHandler;
	private AppUserMobileHistoryDAO	appUserMobileHistoryDAO;
	private AccountInfoDAO			accountInfoDao;
	private GeoLocationDAO geoLocationDAO;
	
	public Level3AccountManagerImpl()
	{
	}

	@Override
	public SearchBaseWrapper searchLevel3AccountsView(SearchBaseWrapper wrapper) throws FrameworkCheckedException
	{
		Level3AccountsViewModel level3AccountsViewModel = (Level3AccountsViewModel) wrapper.getBasePersistableModel();
		CustomList<Level3AccountsViewModel> customList = level3AccountsViewDao.findByExample(level3AccountsViewModel, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel());
		wrapper.setCustomList(customList);
		return wrapper;
	}

	private Long getLevel3AccountLevel(String initialAppFormNo)
	{
		AgentMerchantDetailModel	agentMerchantDetailModel=new AgentMerchantDetailModel();
		agentMerchantDetailModel.setInitialAppFormNo(initialAppFormNo);
		
		ExampleConfigHolderModel configHolderModel=new ExampleConfigHolderModel();
		configHolderModel.setMatchMode(MatchMode.EXACT);
		
		CustomList<AgentMerchantDetailModel>	customList=agentMerchantDetailDAO.findByExample(agentMerchantDetailModel, null,null,configHolderModel, "");
		if(customList!=null && customList.getResultsetList().size()>0)
			agentMerchantDetailModel	=	customList.getResultsetList().get(0);
		
		return agentMerchantDetailModel.getAcLevelQualificationId();
	}
	
	public BaseWrapper createLevel3Account(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		Date nowDate = new Date();
		Level3AccountModel accountModel = (Level3AccountModel) baseWrapper.getObject(Level3AccountModel.LEVEL3_ACCOUNT_MODEL_KEY);

		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

		if (!this.isNICUnique(accountModel.getApplicant1DetailModel().getIdNumber(), accountModel.getAppUserId()))
		{
			throw new FrameworkCheckedException("NICUniqueException");
		}

		/**
		 * Check if agent merchant is saved 
		 */
		AgentMerchantDetailModel agentMerchantDetailModel=new AgentMerchantDetailModel();
		if(baseWrapper.getObject("is_scheduler") == null)
		{
			agentMerchantDetailModel.setInitialAppFormNo(accountModel.getInitialAppFormNo());

			CustomList<AgentMerchantDetailModel>	customeList	=	agentMerchantDetailDAO.findByExample(agentMerchantDetailModel, "");

			if(customeList!=null && customeList.getResultsetList().size()>0){

				agentMerchantDetailModel	=	customeList.getResultsetList().get(0);
			}
			else{
				throw new FrameworkCheckedException("No Agent/Merchant found against initial application no. "+ accountModel.getInitialAppFormNo());
			}
		}
		else
		{
			agentMerchantDetailModel = (AgentMerchantDetailModel) baseWrapper.getBasePersistableModel();
		}
		/**
		 * Check if Kyc is saved 
		 */
		
		KYCModel kycModel=new KYCModel();
		kycModel.setInitialAppFormNo(accountModel.getInitialAppFormNo());
		CustomList<KYCModel>	kYCModelCustomList	=	kycDAO.findByExample(kycModel);
		
		if((kYCModelCustomList==null || kYCModelCustomList.getResultsetList().size()==0) && baseWrapper.getObject("is_scheduler") == null)
		{
			throw new FrameworkCheckedException("No Kyc record found against initial application no. "+ accountModel.getInitialAppFormNo());
		}
		
		
		/**
		 * saving GeoLocationModel
		 */
		
		GeoLocationModel geoLocationModel = new GeoLocationModel();
		geoLocationModel.setLatitude(accountModel.getLatitude());
		geoLocationModel.setLongitude(accountModel.getLongitude());
		geoLocationModel.setCreatedBy(2L);
		geoLocationModel.setUpdatedBy(2L);
		geoLocationModel.setCreatedOn(new Date());
		geoLocationModel.setUpdatedOn(new Date());
		
		geoLocationDAO.saveOrUpdate(geoLocationModel);
		
		/**
		 * Saving the RetailerContactModel
		 */
		RetailerContactModel retailerContactModel = extractRetailerContactModel(accountModel, agentMerchantDetailModel);
		retailerContactModel.setParentRetailerContactModelId(agentMerchantDetailModel.getParentAgentId());
		retailerContactModel.setGeoLocationId(geoLocationModel.getGeoLocationId());
		retailerContactDAO.saveOrUpdate(retailerContactModel);
		accountModel.setRetailerContactId(retailerContactModel.getRetailerContactId());

		AppUserModel appUserModel = extractAppUserForRetailerContact(accountModel);
		appUserModel.setUsername(agentMerchantDetailModel.getUserName());
		appUserModel.setPassword(agentMerchantDetailModel.getPassword());
		appUserModel.setFiler(accountModel.getFiler());
		//appUserModel.setEmployeeId(Long.parseLong(agentMerchantDetailModel.getEmpId()));
		appUserDAO.saveUser(appUserModel);
		
		/**
		 * Saving appUserMobileHistoryModel
		 */
		AppUserMobileHistoryModel appUserMobileHistoryModel = new AppUserMobileHistoryModel();
		appUserMobileHistoryModel.setAppUserId(appUserModel.getAppUserId());
		appUserMobileHistoryModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		appUserMobileHistoryModel.setCreatedOn(new Date());
		appUserMobileHistoryModel.setDescription("New record added.");
		appUserMobileHistoryModel.setFromDate(new Date());
		appUserMobileHistoryModel.setMobileNo(appUserModel.getMobileNo());
		appUserMobileHistoryModel.setNic(appUserModel.getNic());
		appUserMobileHistoryModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		appUserMobileHistoryModel.setUpdatedOn(new Date());
		appUserMobileHistoryDAO.saveOrUpdate(appUserMobileHistoryModel);
		
		accountModel.setAppUserId(appUserModel.getAppUserId());

		UserDeviceAccountsModel userDeviceAccountsModel = extractUserDeviceAccount(accountModel);
		userDeviceAccountsModel.setProdCatalogId(agentMerchantDetailModel.getProductCatalogId());
		userDeviceAccountsModel = this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
		accountModel.setMfsId(userDeviceAccountsModel.getUserId());

		AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
	    appUserPartnerGroupModel.setPartnerGroupId(agentMerchantDetailModel.getPartnerGroupId());
    	appUserPartnerGroupModel.setUpdatedOn(new Date());
    	appUserPartnerGroupModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
    	appUserPartnerGroupModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
    	appUserPartnerGroupModel.setCreatedOn(new Date());
		appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
		appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
		
		ApplicantDetailModel applicantDetail1Model = extractApplicantDetailModel(nowDate, accountModel.getApplicant1DetailModel(),
				retailerContactModel);
		applicantDetail1Model.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
		this.applicantDetailDAO.saveOrUpdate(applicantDetail1Model);

		this.saveL3Addresses(accountModel, retailerContactModel.getRetailerContactId(), applicantDetail1Model.getApplicantDetailId());

		for (ApplicantDetailModel applicantDetailModel : accountModel.getApplicantDetailModelList())
		{
			if (null != applicantDetailModel.getName())
			{
				if (applicantDetailModel.getCreatedBy() == null || applicantDetailModel.getCreatedOn() == null)
				{
					applicantDetailModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
					applicantDetailModel.setCreatedOn(new Date());
					applicantDetailModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					applicantDetailModel.setUpdatedOn(new Date());
					applicantDetailModel.setVersionNo(null);
				}
				applicantDetailModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_2);
				applicantDetailModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
				applicantDetailModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				applicantDetailModel.setUpdatedOn(new Date());
				this.applicantDetailDAO.saveOrUpdate(applicantDetailModel);
				
				try
				{
					Collection<RetailerContactAddressesModel> retailerAddresses = retailerContactModel.getRetailerContactIdRetailerContactAddressesModelList();
					AddressModel presentHomeAddressAp2 = new AddressModel();
		    		AddressModel businessAddressAp2 = new AddressModel();
		    		
		    		if(!this.isContainRetailerContactAddress(retailerAddresses, applicantDetailModel.getApplicantDetailId()))
		    		{
		    			if(null != applicantDetailModel.getResidentialAddress()) 
		    			{
		    				presentHomeAddressAp2.setStreetAddress(applicantDetailModel.getResidentialAddress());
							presentHomeAddressAp2.setCityId(applicantDetailModel.getResidentialCity());
		    			}
						if(null != applicantDetailModel.getBuisnessAddress()) 
						{
							businessAddressAp2.setStreetAddress(applicantDetailModel.getBuisnessAddress());
							businessAddressAp2.setCityId(applicantDetailModel.getBuisnessCity());
						}
						presentHomeAddressAp2 = this.addressDAO.saveOrUpdate(presentHomeAddressAp2);
						businessAddressAp2 = this.addressDAO.saveOrUpdate(businessAddressAp2);

						RetailerContactAddressesModel retailerContactAddressesModel= new RetailerContactAddressesModel();
						retailerContactAddressesModel.setAddressId(presentHomeAddressAp2.getAddressId());
						retailerContactAddressesModel.setAddressTypeId(AddressTypeConstants.PRESENT_HOME_ADDRESS);
						retailerContactAddressesModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
						retailerContactAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_2);
						retailerContactAddressesModel.setApplicantDetailId(applicantDetailModel.getApplicantDetailId());
						retailerContactAddressesDAO.saveOrUpdate(retailerContactAddressesModel);
							
						retailerContactAddressesModel= new RetailerContactAddressesModel();
						retailerContactAddressesModel.setAddressId(businessAddressAp2.getAddressId());
						retailerContactAddressesModel.setAddressTypeId(AddressTypeConstants.BUSINESS_ADDRESS);
						retailerContactAddressesModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
						retailerContactAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_2);
						retailerContactAddressesModel.setApplicantDetailId(applicantDetailModel.getApplicantDetailId());
						retailerContactAddressesDAO.saveOrUpdate(retailerContactAddressesModel);
		    		}
		    		
		    		if(retailerAddresses != null && retailerAddresses.size() > 0)
			    	{
		    			for(RetailerContactAddressesModel retailAdd : retailerAddresses)
		    			{
			    			if(null == retailAdd.getApplicantTypeId() && retailAdd.getApplicantTypeId() == ApplicantTypeConstants.APPLICANT_TYPE_2)
			    			{  
			    				if(null != applicantDetailModel.getResidentialAddress())
			    				{
										presentHomeAddressAp2.setStreetAddress(applicantDetailModel.getResidentialAddress());
										presentHomeAddressAp2.setCityId(applicantDetailModel.getResidentialCity());
								}
								if(null != applicantDetailModel.getBuisnessAddress())
								{
									businessAddressAp2.setStreetAddress(applicantDetailModel.getBuisnessAddress());
									businessAddressAp2.setCityId(applicantDetailModel.getBuisnessCity());
								}
								presentHomeAddressAp2 = this.addressDAO.saveOrUpdate(presentHomeAddressAp2);
								businessAddressAp2 = this.addressDAO.saveOrUpdate(businessAddressAp2);
								
								RetailerContactAddressesModel retailerContactAddressesModel= new RetailerContactAddressesModel();
								retailerContactAddressesModel.setAddressId(presentHomeAddressAp2.getAddressId());
								retailerContactAddressesModel.setAddressTypeId(AddressTypeConstants.PRESENT_HOME_ADDRESS);
								retailerContactAddressesModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
								retailerContactAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_2);
								retailerContactAddressesModel.setApplicantDetailId(applicantDetailModel.getApplicantDetailId());
								retailerContactAddressesDAO.saveOrUpdate(retailerContactAddressesModel);
							
								retailerContactAddressesModel = new RetailerContactAddressesModel();
								retailerContactAddressesModel.setAddressId(businessAddressAp2.getAddressId());
								retailerContactAddressesModel.setAddressTypeId(AddressTypeConstants.BUSINESS_ADDRESS);
								retailerContactAddressesModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
								retailerContactAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_2);
								retailerContactAddressesModel.setApplicantDetailId(applicantDetailModel.getApplicantDetailId());
								retailerContactAddressesDAO.saveOrUpdate(retailerContactAddressesModel);
			    			}
		    			}
			    	}
				}
				catch(Exception e){
					throw new FrameworkCheckedException(e.getLocalizedMessage());
				}
			}
		}
		
		// save AccountOwnerShip list now
		if (CollectionUtils.isNotEmpty(accountModel.getAcOwnershipDetailModelList()))
		{
			List<ACOwnershipDetailModel> accountOwnerShipModelList = new ArrayList<ACOwnershipDetailModel>();
			for (ACOwnershipDetailModel accountOwnerShipDetailModel : accountModel.getAcOwnershipDetailModelList())
			{
				if(accountOwnerShipDetailModel.getAcOwnershipTypeId()==null)
					continue;
				accountOwnerShipDetailModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				accountOwnerShipDetailModel.setCreatedOn(new Date());
				accountOwnerShipDetailModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				accountOwnerShipDetailModel.setUpdatedOn(new Date());
				accountOwnerShipDetailModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
				accountOwnerShipDetailModel.setIsDeleted(false);
				accountOwnerShipModelList.add(accountOwnerShipDetailModel);
			}
			acOwnerShipDetailDAO.saveOrUpdateCollection(accountOwnerShipModelList);
		}

		// Agents BB account(OLA) - change start
		BankModel bankModel = new BankModel();
		bankModel.setFinancialIntegrationId(FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION); // 4 is for OLA bank
		CustomList<BankModel> bankList = bankDAO.findByExample(bankModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		List<BankModel> bankL = bankList.getResultsetList();
		bankModel = (BankModel) bankL.get(0);
		
		OLAVO olaVo = new OLAVO();
		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
		SwitchWrapper sWrapper = new SwitchWrapperImpl();

		BaseWrapper baseWrapperBank = new BaseWrapperImpl();
		baseWrapperBank.setBasePersistableModel(bankModel);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
		SmartMoneyAccountModel smaModel = extractSmartMoneyModel(retailerContactModel.getRetailerContactId(), userDeviceAccountsModel.getUserId(),
				bankModel, abstractFinancialInstitution);
		this.linkPaymentModeDAO.saveOrUpdate(smaModel);

		olaVo.setFirstName(appUserModel.getFirstName());
		olaVo.setLastName(appUserModel.getLastName());
		olaVo.setMiddleName("-");
		olaVo.setFatherName("-");
		olaVo.setCnic(appUserModel.getNic());
		olaVo.setLandlineNumber(appUserModel.getMobileNo());
		olaVo.setMobileNumber(appUserModel.getMobileNo());
		olaVo.setStatusId(1l);
		olaVo.setCustomerAccountTypeId(agentMerchantDetailModel.getAcLevelQualificationId());

		if (appUserModel.getAddress1() == null)
		{
			olaVo.setAddress("-");
		} else
		{
			olaVo.setAddress(appUserModel.getAddress1());
		}

		if (appUserModel.getDob() == null)
		{
			olaVo.setDob(new Date());
		} else
		{
			olaVo.setDob(appUserModel.getDob());
		}

		sWrapper.setOlavo(olaVo);
		sWrapper.setBankId(bankModel.getBankId());

		try
		{
			sWrapper = olaVeriflyFinancialInstitution.createAccount(sWrapper);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
		}

		if ("07".equals(olaVo.getResponseCode()))
		{
			throw new FrameworkCheckedException("CNIC already exists in the OLA accounts");
		}

		AccountInfoModel accountInfoModel = new AccountInfoModel();
		accountInfoModel.setAccountNo(olaVo.getPayingAccNo().toString());
		accountInfoModel.setAccountNick(smaModel.getName());
		accountInfoModel.setActive(smaModel.getActive());

		// Added after HSM Integration
		accountInfoModel.setPan(PanGenerator.generatePAN());
		// End HSM Integration Change

		accountInfoModel.setCreatedOn(smaModel.getCreatedOn());
		accountInfoModel.setUpdatedOn(smaModel.getUpdatedOn());
		accountInfoModel.setCustomerId(appUserModel.getAppUserId());
		accountInfoModel.setCustomerMobileNo(appUserModel.getMobileNo());
		accountInfoModel.setFirstName(appUserModel.getFirstName());
		accountInfoModel.setLastName(appUserModel.getLastName());
		accountInfoModel.setPaymentModeId(smaModel.getPaymentModeId());
		accountInfoModel.setDeleted(Boolean.FALSE);

		veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
		LogModel logmodel = new LogModel();
		logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
		logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
		veriflyBaseWrapper.setLogModel(logmodel);
		veriflyBaseWrapper.setBasePersistableModel(smaModel);
		veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB);
		veriflyBaseWrapper.setSkipPanCheck(Boolean.TRUE);

		try
		{
			veriflyBaseWrapper = abstractFinancialInstitution.generatePin(veriflyBaseWrapper);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		if (!veriflyBaseWrapper.isErrorStatus())
		{
			String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
			if(null == veriflyBaseWrapper.getErrorMessage())
				veriflyErrorMessage ="GenPinServiceDown";
			throw new FrameworkCheckedException(veriflyErrorMessage);
		}
			
		// Send SMS to Agent
		String message = MessageUtil.getMessage("level3Account.requestReceived");
		SmsMessage smsMessage = new SmsMessage(accountModel.getApplicant1DetailModel().getMobileNo(), message);
		smsSender.send(smsMessage);

		actionLogModel.setCustomField1(retailerContactModel.getRetailerContactId().toString());
		actionLogModel.setCustomField11(userDeviceAccountsModel.getUserId());
		this.actionLogManager.completeActionLog(actionLogModel);

		baseWrapper.putObject(Level3AccountModel.LEVEL3_ACCOUNT_MODEL_KEY, accountModel);
		baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID, appUserModel.getAppUserId());

		return baseWrapper;
	}

	private void linkPaymentMode(LinkPaymentModeModel linkPaymentModeModel) throws FrameworkCheckedException{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		String responceMsg = "";
		try {
			baseWrapper.putObject("linkPaymentModeModel", linkPaymentModeModel);
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.LINK_PAYMENT_MODE_USECASE_ID);
			baseWrapper = linkPaymentModeManager.createLinkPaymentModeForL3(baseWrapper);
	
			if (baseWrapper != null) {
				if (baseWrapper.getObject("ErrorMessage") != null) {
					responceMsg = (String) baseWrapper.getObject("ErrorMessage"); // verifly   message
					throw new FrameworkCheckedException(responceMsg);
				}
			}
		} catch (FrameworkCheckedException ex) {
			responceMsg = ex.getMessage();
			if (baseWrapper.getObject("ErrMessage") != null){
				responceMsg = (String) baseWrapper.getObject("ErrMessage"); // Customer does not exits
			}
			if(ex != null && ex.getMessage() != null) {
				if(ex.getMessage().equals("implementationNotSupportedException")){
					responceMsg = "linkpaymentmodemodule.featureNotSupported";
				}
				if(ex.getMessage().equals("linkPaymentMode.customerprofiledoesnotexist")){
					responceMsg = "linkPaymentMode.customerprofiledoesnotexist";
				}
				if(ex.getMessage().equals(WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED)){
					responceMsg = WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED;
				}
				if(ex.getMessage().equals("This Payment Mode is already linked.")){
					responceMsg = "This Payment Mode is already linked.";
				}
				if(ex.getMessage().equals("The Account Number is already Linked. Kindly Select Another Account Number and Try Again.")){
					responceMsg = "AccountNumberAlreadyLinked";
				}
			}
			if (responceMsg != null && responceMsg.equalsIgnoreCase("Service unavailable due to technical difficulties, please try again or contact service provider.")) {
				
			} else if(responceMsg != null && responceMsg.equalsIgnoreCase("")) {
				responceMsg = "Smart Money Account Could Not Be Saved";
			}
			throw new FrameworkCheckedException(responceMsg);
		}
	}
	
	
	
	private void updateL3Addresses(Level3AccountModel accountModel, long retailerContactId, long applicantDetailId)
	{
		RetailerContactAddressesModel retailerContactAddressesModel=new RetailerContactAddressesModel();
		retailerContactAddressesModel.setRetailerContactId(retailerContactId);
		ExampleConfigHolderModel configHolderModel=new ExampleConfigHolderModel();
		configHolderModel.setMatchMode(MatchMode.EXACT);
		CustomList<RetailerContactAddressesModel> customList=
				retailerContactAddressesDAO.findByExample(retailerContactAddressesModel, null,null,configHolderModel,"");
		if(customList!=null && customList.getResultsetList().size()>0){
			
			AddressModel addressModel=null;
			
			List<RetailerContactAddressesModel>	retailerContactAddressesModelList=	customList.getResultsetList();
			
			for (RetailerContactAddressesModel model : retailerContactAddressesModelList)
			{
				if(model.getApplicantTypeId()==null && model.getApplicantDetailId()==null)
				{
					//BUSINESS_ADDRESS
					if(model.getAddressTypeId()==AddressTypeConstants.BUSINESS_ADDRESS.longValue()
							&& model.getApplicantDetailId()==null 
							&& model.getApplicantTypeId()==null
							&& model.getRetailerContactId()==retailerContactId){
						addressModel=addressDAO.findByPrimaryKey(model.getAddressId());
						addressModel.setStreetAddress(accountModel.getBusinessAddress());
						addressModel.setCityId(accountModel.getBusinessAddCity());
						addressModel.setPostalCode(accountModel.getBusinessPostalCode());
						addressDAO.saveOrUpdate(addressModel);
					}
					//CORRESSPONDANCE_ADDRESS
					if(model.getAddressTypeId()==AddressTypeConstants.CORRESSPONDANCE_ADDRESS.longValue()
							&& model.getApplicantDetailId()==null 
							&& model.getApplicantTypeId()==null
							&& model.getRetailerContactId()==retailerContactId){
						addressModel=addressDAO.findByPrimaryKey(model.getAddressId());
						addressModel.setStreetAddress(accountModel.getCorresspondanceAddress());
						addressModel.setCityId(accountModel.getCorresspondanceAddCity());
						addressModel.setPostalCode(accountModel.getCorresspondancePostalCode());
						addressDAO.saveOrUpdate(addressModel);
					}
				}
				else{
					//APPLICANT_TYPE_1 BUSINESS_ADDRESS
					if(model.getAddressTypeId()==AddressTypeConstants.BUSINESS_ADDRESS.longValue()
							&& model.getApplicantDetailId()==applicantDetailId 
							&& model.getApplicantTypeId()==ApplicantTypeConstants.APPLICANT_TYPE_1.longValue()
							&& model.getRetailerContactId()==retailerContactId){
						
						addressModel=addressDAO.findByPrimaryKey(model.getAddressId());
						addressModel.setStreetAddress(accountModel.getApplicant1DetailModel().getBuisnessAddress());
						addressModel.setCityId(accountModel.getApplicant1DetailModel().getBuisnessCity());
						addressDAO.saveOrUpdate(addressModel);
					}
					//APPLICANT_TYPE_1 PRESENT_HOME_ADDRESS
					if(model.getAddressTypeId()==AddressTypeConstants.PRESENT_HOME_ADDRESS.longValue()
							&& model.getApplicantDetailId()==applicantDetailId 
							&& model.getApplicantTypeId()==ApplicantTypeConstants.APPLICANT_TYPE_1.longValue()
							&& model.getRetailerContactId()==retailerContactId){
						
						addressModel=addressDAO.findByPrimaryKey(model.getAddressId());
						addressModel.setStreetAddress(accountModel.getApplicant1DetailModel().getResidentialAddress());
						addressModel.setCityId(accountModel.getApplicant1DetailModel().getResidentialCity());
						addressDAO.saveOrUpdate(addressModel);
					}
					//APPLICANT_TYPE_1 NOK_ADDRESS
					if(model.getAddressTypeId()==AddressTypeConstants.NOK_ADDRESS.longValue()
							&& model.getApplicantDetailId()==applicantDetailId 
							&& model.getApplicantTypeId()==ApplicantTypeConstants.APPLICANT_TYPE_1.longValue()
							&& model.getRetailerContactId()==retailerContactId){
						addressModel=addressDAO.findByPrimaryKey(model.getAddressId());
						addressModel.setStreetAddress(accountModel.getNokDetailVOModel().getNokMailingAdd());
						addressDAO.saveOrUpdate(addressModel);
					}
				}
			}
		}
	}
	
	private void saveL3Addresses(Level3AccountModel accountModel, Long retailerContactId, Long applicantDetailId)
	{
		AddressModel addressModel=new AddressModel();
		addressModel.setStreetAddress(accountModel.getBusinessAddress());
		addressModel.setCityId(accountModel.getBusinessAddCity());
		addressModel.setPostalCode(accountModel.getBusinessPostalCode());
		addressDAO.saveOrUpdate(addressModel);
		RetailerContactAddressesModel contactAddressesModel	=	new RetailerContactAddressesModel();
		contactAddressesModel.setAddressId(addressModel.getAddressId());
		contactAddressesModel.setAddressTypeId(AddressTypeConstants.BUSINESS_ADDRESS);
		contactAddressesModel.setRetailerContactId(retailerContactId);
		retailerContactAddressesDAO.saveOrUpdate(contactAddressesModel); 
		
		addressModel=new AddressModel();
		addressModel.setStreetAddress(accountModel.getCorresspondanceAddress());
		addressModel.setCityId(accountModel.getCorresspondanceAddCity());
		addressModel.setPostalCode(accountModel.getCorresspondancePostalCode());
		addressDAO.saveOrUpdate(addressModel);
		contactAddressesModel=new RetailerContactAddressesModel();
		contactAddressesModel.setAddressId(addressModel.getAddressId());
		contactAddressesModel.setAddressTypeId(AddressTypeConstants.CORRESSPONDANCE_ADDRESS);
		contactAddressesModel.setRetailerContactId(retailerContactId);
		retailerContactAddressesDAO.saveOrUpdate(contactAddressesModel);

		addressModel=new AddressModel();
		addressModel.setStreetAddress(accountModel.getApplicant1DetailModel().getBuisnessAddress());
		addressModel.setCityId(accountModel.getApplicant1DetailModel().getBuisnessCity());
		addressDAO.saveOrUpdate(addressModel);
		contactAddressesModel=new RetailerContactAddressesModel();
		contactAddressesModel.setAddressId(addressModel.getAddressId());
		contactAddressesModel.setAddressTypeId(AddressTypeConstants.BUSINESS_ADDRESS);
		contactAddressesModel.setApplicantDetailId(applicantDetailId);
		contactAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
		contactAddressesModel.setRetailerContactId(retailerContactId);
		retailerContactAddressesDAO.saveOrUpdate(contactAddressesModel);
		
		addressModel=new AddressModel();
		addressModel.setStreetAddress(accountModel.getApplicant1DetailModel().getResidentialAddress());
		addressModel.setCityId(accountModel.getApplicant1DetailModel().getResidentialCity());
		addressDAO.saveOrUpdate(addressModel);
		contactAddressesModel=new RetailerContactAddressesModel();
		contactAddressesModel.setAddressId(addressModel.getAddressId());
		contactAddressesModel.setAddressTypeId(AddressTypeConstants.PRESENT_HOME_ADDRESS);
		contactAddressesModel.setApplicantDetailId(applicantDetailId);
		contactAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
		contactAddressesModel.setRetailerContactId(retailerContactId);
		retailerContactAddressesDAO.saveOrUpdate(contactAddressesModel);
		
		addressModel=new AddressModel();
		if(accountModel.getNokDetailVOModel() != null)
			addressModel.setStreetAddress(accountModel.getNokDetailVOModel().getNokMailingAdd());
		else
			addressModel.setStreetAddress("");
		addressDAO.saveOrUpdate(addressModel);
		contactAddressesModel=new RetailerContactAddressesModel();
		contactAddressesModel.setAddressId(addressModel.getAddressId());
		contactAddressesModel.setAddressTypeId(AddressTypeConstants.NOK_ADDRESS);
		contactAddressesModel.setApplicantDetailId(applicantDetailId);
		contactAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
		contactAddressesModel.setRetailerContactId(retailerContactId);
		retailerContactAddressesDAO.saveOrUpdate(contactAddressesModel);
	}
	
	@Override
	public BaseWrapper updateLevel3Account(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		
		Level3AccountModel accountModel = (Level3AccountModel) baseWrapper.getObject(Level3AccountModel.LEVEL3_ACCOUNT_MODEL_KEY);
		
		if(!this.isNICUnique(accountModel.getApplicant1DetailModel().getIdNumber(), accountModel.getAppUserId())){
			throw new FrameworkCheckedException("NICUniqueException");
		}

		AgentMerchantDetailModel agentMerchantDetailModel=new AgentMerchantDetailModel();
		
		agentMerchantDetailModel.setInitialAppFormNo(accountModel.getInitialAppFormNo());

		CustomList<AgentMerchantDetailModel>	customeList	=	agentMerchantDetailDAO.findByExample(agentMerchantDetailModel, "");
		
		if(customeList!=null && customeList.getResultsetList().size()>0){
			
			agentMerchantDetailModel	=	customeList.getResultsetList().get(0);
		}
		else{
			throw new FrameworkCheckedException("No Agent/Merchant found against initial application no. "+ accountModel.getInitialAppFormNo());
		}
		
		AppUserModel appUserModel = new AppUserModel();
		appUserModel = this.appUserDAO.findByPrimaryKey( accountModel.getAppUserId() );
		String oldFirstName = appUserModel.getFirstName();
		String oldLastName = appUserModel.getLastName();
		Long oldAccountTypeId = null;
		String[] nameArray = accountModel.getApplicant1DetailModel().getName().split(" ");
		appUserModel.setFirstName(nameArray[0]);
		
		if(nameArray.length > 1){
			appUserModel.setLastName(accountModel.getApplicant1DetailModel().getName().substring(appUserModel.getFirstName().length()+1));
		}else{
			appUserModel.setLastName(nameArray[0]);
		}
		Long oldRegistrationStateId = appUserModel.getRegistrationStateId();
		appUserModel = updateAppUserModel(appUserModel, accountModel);
		//appUserModel.setPassword(agentMerchantDetailModel.getPassword());
		appUserModel.setFiler(accountModel.getFiler());
		this.appUserDAO.saveOrUpdate(appUserModel);

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
		CustomList<UserDeviceAccountsModel> userDeviceAccountsModelList = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel);
		userDeviceAccountsModel = userDeviceAccountsModelList.getResultsetList().get(0);
		accountModel.setMfsId(userDeviceAccountsModel.getUserId());
		if(accountModel.getRegistrationStateId() != null && RegistrationStateConstants.VERIFIED == accountModel.getRegistrationStateId().longValue())
		{
			userDeviceAccountsModel.setAccountEnabled(Boolean.TRUE);
			//userDeviceAccountsModel.setProdCatalogId(agentMerchantDetailModel.getProductCatalogId());
			this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
		}
		
		RetailerContactModel retailerContactModel = appUserModel.getRetailerContactIdRetailerContactModel();				
		retailerContactModel = updateRetailerContactModel(accountModel,retailerContactModel);
		retailerContactModel.setOlaCustomerAccountTypeModelId(getLevel3AccountLevel(retailerContactModel.getInitialAppFormNo()));
		retailerContactModel.setParentRetailerContactModelId(agentMerchantDetailModel.getParentAgentId());

		AppUserModel appUserModelForSales=new AppUserModel();
		appUserModelForSales.setEmployeeId(agentMerchantDetailModel.getEmpId());
		
		CustomList<AppUserModel>	appUserList=appUserDAO.findByExample(appUserModelForSales);
		
		if(appUserList!=null && appUserList.getResultsetList().size()>0){
			appUserModelForSales=	appUserList.getResultsetList().get(0);
			SalesHierarchyModel salesHierarchyModel = salesHierarchyDAO.findSaleUserByBankUserId(appUserModelForSales.getAppUserId());
			retailerContactModel.setSalesHierarchyId(salesHierarchyModel.getSalesHierarchyId());
		}
		

		/**
		 *  updating GeoLocation Model
		 */
		
		GeoLocationModel geoLocationModel = new GeoLocationModel();
		
		if(retailerContactModel.getGeoLocationId() !=null){
			geoLocationModel = geoLocationDAO.findByPrimaryKey(retailerContactModel.getGeoLocationId());
			geoLocationModel.setLatitude(accountModel.getLatitude());
			geoLocationModel.setLongitude(accountModel.getLongitude());
			geoLocationModel.setUpdatedOn(new Date());
			geoLocationDAO.saveOrUpdate(geoLocationModel);
			retailerContactDAO.saveOrUpdate(retailerContactModel);
		}else{
			GeoLocationModel saveGeoLocationModel = new GeoLocationModel();
			saveGeoLocationModel.setLatitude(accountModel.getLatitude());
			saveGeoLocationModel.setLongitude(accountModel.getLongitude());
			saveGeoLocationModel.setCreatedBy(2L);
			saveGeoLocationModel.setUpdatedBy(2L);
			saveGeoLocationModel.setCreatedOn(new Date());
			saveGeoLocationModel.setUpdatedOn(new Date());
			
			geoLocationDAO.saveOrUpdate(saveGeoLocationModel);
			retailerContactModel.setGeoLocationId(saveGeoLocationModel.getGeoLocationId());
			retailerContactDAO.saveOrUpdate(retailerContactModel);
		}
				
		
		
		accountModel.setRetailerContactId(retailerContactModel.getRetailerContactId());

		AppUserPartnerGroupModel appUserPartnerGroupModel =new AppUserPartnerGroupModel();
		appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
		
		CustomList<AppUserPartnerGroupModel>	appUserPartnerGroupModelCustomList=appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel, "");
		if(appUserPartnerGroupModelCustomList!=null && appUserPartnerGroupModelCustomList.getResultsetList().size()>0)
		{
			appUserPartnerGroupModel	=	appUserPartnerGroupModelCustomList.getResultsetList().get(0);
			appUserPartnerGroupModel.setPartnerGroupId(agentMerchantDetailModel.getPartnerGroupId());
	    	appUserPartnerGroupModel.setUpdatedOn(new Date());
	    	appUserPartnerGroupModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
		}
		
		ApplicantDetailModel newApplicant1DetailModel = accountModel.getApplicant1DetailModel();
		ApplicantDetailModel oldApplicant1DetailModel = searchApplicantDetail(retailerContactModel, ApplicantTypeConstants.APPLICANT_TYPE_1);
		newApplicant1DetailModel = this.mergeApplicantDetail(oldApplicant1DetailModel, newApplicant1DetailModel );
		newApplicant1DetailModel = this.applicantDetailDAO.saveOrUpdate(newApplicant1DetailModel);

		this.updateL3Addresses(accountModel, retailerContactModel.getRetailerContactId(), newApplicant1DetailModel.getApplicantDetailId());
		
		Long newRegistrationStateId	=	accountModel.getRegistrationStateId();		
		
		for (ApplicantDetailModel applicant2DetailModel : accountModel.getApplicantDetailModelList())
		{
			if (null != applicant2DetailModel.getName())
			{
				applicant2DetailModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
				if (null == applicant2DetailModel.getCreatedOn())
				{
					applicant2DetailModel.setCreatedOn(new Date());
					applicant2DetailModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				}
				applicant2DetailModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				applicant2DetailModel.setUpdatedOn(new Date());
				applicant2DetailModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_2);
				applicant2DetailModel = this.applicantDetailDAO.saveOrUpdate(applicant2DetailModel);

				// updating applicant2 addresses
				try
				{
					Collection<RetailerContactAddressesModel> retailerAddresses = retailerContactModel.getRetailerContactIdRetailerContactAddressesModelList();
					AddressModel presentHomeAddressAp2 = new AddressModel();
					AddressModel businessAddressAp2 = new AddressModel();

					// /check if customerAddresses does not contains the
					// applicantdetailId add new customer addresses
					if (!this.isContainRetailerContactAddress(retailerAddresses, applicant2DetailModel.getApplicantDetailId()))
					{
						if (null != applicant2DetailModel.getResidentialAddress())
						{
							presentHomeAddressAp2.setStreetAddress(applicant2DetailModel.getResidentialAddress());
							presentHomeAddressAp2.setCityId(applicant2DetailModel.getResidentialCity());
						}
						if (null != applicant2DetailModel.getBuisnessAddress())
						{
							businessAddressAp2.setStreetAddress(applicant2DetailModel.getBuisnessAddress());
							businessAddressAp2.setCityId(applicant2DetailModel.getBuisnessCity());
						}
						presentHomeAddressAp2 = this.addressDAO.saveOrUpdate(presentHomeAddressAp2);
						businessAddressAp2 = this.addressDAO.saveOrUpdate(businessAddressAp2);

						RetailerContactAddressesModel presentCustomerAddressesModelAp2 = new RetailerContactAddressesModel();
						presentCustomerAddressesModelAp2.setAddressId(presentHomeAddressAp2.getAddressId());
						presentCustomerAddressesModelAp2.setAddressTypeId(1L);
						presentCustomerAddressesModelAp2.setRetailerContactId(retailerContactModel.getRetailerContactId());
						presentCustomerAddressesModelAp2.setApplicantTypeId(2L);
						presentCustomerAddressesModelAp2.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());

						RetailerContactAddressesModel businessCustomerAddressesModelAp2 = new RetailerContactAddressesModel();
						businessCustomerAddressesModelAp2.setAddressId(businessAddressAp2.getAddressId());
						businessCustomerAddressesModelAp2.setAddressTypeId(3L);
						businessCustomerAddressesModelAp2.setRetailerContactId(retailerContactModel.getRetailerContactId());
						businessCustomerAddressesModelAp2.setApplicantTypeId(2L);
						businessCustomerAddressesModelAp2.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());

						presentCustomerAddressesModelAp2 = this.retailerContactAddressesDAO.saveOrUpdate(presentCustomerAddressesModelAp2);
						businessCustomerAddressesModelAp2 = this.retailerContactAddressesDAO.saveOrUpdate(businessCustomerAddressesModelAp2);
					}

					// /end of new customer addresses

					if (retailerAddresses != null && retailerAddresses.size() > 0)
					{
						for (RetailerContactAddressesModel custAdd : retailerAddresses)
						{
							AddressModel addressModel = custAdd.getAddressIdAddressModel();
							if (null == custAdd.getApplicantTypeId() && custAdd.getAddressTypeId().longValue() == 4L)
							{
								if (accountModel.getNokDetailVOModel().getNokMailingAdd() != null)
								{
									addressModel.setStreetAddress(accountModel.getNokDetailVOModel().getNokMailingAdd());
								}
								this.addressDAO.saveOrUpdate(addressModel);
							}
							if (null != custAdd.getApplicantTypeId() && custAdd.getApplicantTypeId().longValue() == 1L)
							{
								if (custAdd.getAddressTypeId() == 1)
								{
									if (newApplicant1DetailModel.getResidentialAddress() != null)
									{
										addressModel.setStreetAddress(newApplicant1DetailModel.getResidentialAddress());
									}
									if (newApplicant1DetailModel.getResidentialCity() != null)
									{
										addressModel.setCityId(newApplicant1DetailModel.getResidentialCity());
									}
									this.addressDAO.saveOrUpdate(addressModel);
								} else if (custAdd.getAddressTypeId() == 3)
								{
									if (newApplicant1DetailModel.getBuisnessAddress() != null)
									{
										addressModel.setStreetAddress(newApplicant1DetailModel.getBuisnessAddress());
									}
									if (newApplicant1DetailModel.getBuisnessCity() != null)
									{
										addressModel.setCityId(newApplicant1DetailModel.getBuisnessCity());
									}
									this.addressDAO.saveOrUpdate(addressModel);
								}

							}
							if (null != applicant2DetailModel.getName() && applicant2DetailModel.getApplicantDetailId() != null
									&& custAdd.getApplicantTypeId() != null && custAdd.getApplicantTypeId() == 2L
									&& custAdd.getApplicantDetailId() != null
									&& custAdd.getApplicantDetailId().equals(applicant2DetailModel.getApplicantDetailId()))
							{
								if (custAdd.getAddressTypeId() == 1)
								{
									if (applicant2DetailModel.getResidentialAddress() != null)
									{
										addressModel.setStreetAddress(applicant2DetailModel.getResidentialAddress());
									}
									if (applicant2DetailModel.getResidentialCity() != null)
									{
										addressModel.setCityId(applicant2DetailModel.getResidentialCity());
									}
									presentHomeAddressAp2 = this.addressDAO.saveOrUpdate(addressModel);
								} else if (custAdd.getAddressTypeId() == 3)
								{
									if (applicant2DetailModel.getBuisnessAddress() != null)
									{
										addressModel.setStreetAddress(applicant2DetailModel.getBuisnessAddress());
									}
									if (applicant2DetailModel.getBuisnessCity() != null)
									{
										addressModel.setCityId(applicant2DetailModel.getBuisnessCity());
									}
									businessAddressAp2 = this.addressDAO.saveOrUpdate(addressModel);
								}
							}

						}
					} 
					else
					{
						if (null != applicant2DetailModel.getResidentialAddress())
						{
							presentHomeAddressAp2.setStreetAddress(applicant2DetailModel.getResidentialAddress());
							presentHomeAddressAp2.setCityId(applicant2DetailModel.getResidentialCity());
						}
						if (null != applicant2DetailModel.getBuisnessAddress())
						{
							businessAddressAp2.setStreetAddress(applicant2DetailModel.getBuisnessAddress());
							businessAddressAp2.setCityId(applicant2DetailModel.getBuisnessCity());
						}
						presentHomeAddressAp2 = this.addressDAO.saveOrUpdate(presentHomeAddressAp2);
						businessAddressAp2 = this.addressDAO.saveOrUpdate(businessAddressAp2);

						RetailerContactAddressesModel presentCustomerAddressesModelAp2 = new RetailerContactAddressesModel();
						presentCustomerAddressesModelAp2.setAddressId(presentHomeAddressAp2.getAddressId());
						presentCustomerAddressesModelAp2.setAddressTypeId(1L);
						presentCustomerAddressesModelAp2.setRetailerContactId(retailerContactModel.getRetailerContactId());
						presentCustomerAddressesModelAp2.setApplicantTypeId(2L);
						presentCustomerAddressesModelAp2.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());

						RetailerContactAddressesModel businessCustomerAddressesModelAp2 = new RetailerContactAddressesModel();
						businessCustomerAddressesModelAp2.setAddressId(businessAddressAp2.getAddressId());
						businessCustomerAddressesModelAp2.setAddressTypeId(3L);
						businessCustomerAddressesModelAp2.setRetailerContactId(retailerContactModel.getRetailerContactId());
						businessCustomerAddressesModelAp2.setApplicantTypeId(2L);
						businessCustomerAddressesModelAp2.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());

						presentCustomerAddressesModelAp2 = this.retailerContactAddressesDAO.saveOrUpdate(presentCustomerAddressesModelAp2);
						businessCustomerAddressesModelAp2 = this.retailerContactAddressesDAO.saveOrUpdate(businessCustomerAddressesModelAp2);
					}

				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		//save/update AccountOwnerShip list now
		if(CollectionUtils.isNotEmpty(accountModel.getAcOwnershipDetailModelList()))
		{
			List<ACOwnershipDetailModel> accountOwnerShipModelList = new ArrayList<ACOwnershipDetailModel>(0);
			
			ACOwnershipDetailModel acOwnershipDetailModelNew=null;
			
			for(ACOwnershipDetailModel model : accountModel.getAcOwnershipDetailModelList())
			{
				if(model.getAcOwnershipTypeId()==null)
					continue;
				
				acOwnershipDetailModelNew=new ACOwnershipDetailModel();
				
				if(model.getAcOwnershipTypeId() != null )
				{
					if(model.getAcOwnershipDetailId() == null)
					{
						acOwnershipDetailModelNew.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
						acOwnershipDetailModelNew.setCreatedOn(new Date());
					}
					else{
						acOwnershipDetailModelNew=acOwnerShipDetailDAO.findByPrimaryKey(model.getAcOwnershipDetailId());
					}
					acOwnershipDetailModelNew.setAcOwnershipTypeId(model.getAcOwnershipTypeId());
					acOwnershipDetailModelNew.setDateOfBirth(model.getDateOfBirth());
					acOwnershipDetailModelNew.setIdDocumentNo(model.getIdDocumentNo());
					acOwnershipDetailModelNew.setIdDocumentType(model.getIdDocumentType());
					acOwnershipDetailModelNew.setName(model.getName());
					acOwnershipDetailModelNew.setOfac(model.getOfac());
					acOwnershipDetailModelNew.setPep(model.getPep());
					acOwnershipDetailModelNew.setScreeningPerformed(model.getScreeningPerformed());
					acOwnershipDetailModelNew.setVerisysDone(model.getVerisysDone());
					acOwnershipDetailModelNew.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					acOwnershipDetailModelNew.setUpdatedOn(new Date());
					acOwnershipDetailModelNew.setRetailerContactId(retailerContactModel.getRetailerContactId());
					acOwnershipDetailModelNew.setIsDeleted(Boolean.FALSE);
					accountOwnerShipModelList.add(acOwnershipDetailModelNew);
				}
			}
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			ACOwnershipDetailModel accountOwnerShipModel = new ACOwnershipDetailModel();
			accountOwnerShipModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
			searchBaseWrapper.setBasePersistableModel(accountOwnerShipModel);
			List<ACOwnershipDetailModel> existingOwnerShips = (List<ACOwnershipDetailModel>) baseWrapper.getObject("existingOwners");/*this.loadAccountOwnerShipDetails(searchBaseWrapper);*/
	    	List<ACOwnershipDetailModel> mergedList = new ArrayList<ACOwnershipDetailModel>(0);
	    	
	    	if(existingOwnerShips!=null && accountOwnerShipModelList.size() != existingOwnerShips.size()){
	    		for(ACOwnershipDetailModel existingModel : existingOwnerShips){
	    			boolean isExist = false;
	    			for(ACOwnershipDetailModel model : accountOwnerShipModelList){
	    				if(existingModel.getAcOwnershipDetailId().longValue() == model.getAcOwnershipDetailId().longValue()){
	    					isExist = true;
	    					break;
	    				}
	    			}
	    			if(!isExist){
	    				existingModel.setIsDeleted(true);
	    				existingModel.setVersionNo(existingModel.getVersionNo());
	    				mergedList.add(existingModel);
	    				isExist = false;
	    			}
	    		}
	    	}
    		
	    	mergedList.addAll(accountOwnerShipModelList);
	    	acOwnerShipDetailDAO.saveOrUpdateCollection(mergedList);
		}
		
		//update OLA account
		
			OLAVO olaVo = new OLAVO();
	        olaVo.setFirstName(appUserModel.getFirstName());
	        olaVo.setLastName(appUserModel.getLastName());
	        olaVo.setCustomerAccountTypeId(agentMerchantDetailModel.getAcLevelQualificationId());
	        olaVo.setCnic(appUserModel.getNic());
	        olaVo.setMobileNumber(appUserModel.getMobileNo());

	        SwitchWrapper sWrapper = new SwitchWrapperImpl();
	        BankModel bankModel = getOlaBankModel();
	        sWrapper.setOlavo(olaVo);   
	        sWrapper.setBankId(bankModel.getBankId());

	        try{
	            sWrapper = olaVeriflyFinancialInstitution.changeAccountDetails(sWrapper);
	        }catch(Exception e){
	            e.printStackTrace();
	            throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
	        }
	        
	        if ("07".equals(olaVo.getResponseCode())){
	            throw new FrameworkCheckedException("CNIC already exisits in the OLA accounts");
	        }
	        
	        AccountInfoModel accountInfoModel = new AccountInfoModel();
	        accountInfoModel.setCustomerId(appUserModel.getAppUserId());
	        CustomList<AccountInfoModel> customList = accountInfoDao.findByExample(accountInfoModel);
	        if(customList != null && CollectionUtils.isNotEmpty(customList.getResultsetList())){
	        	accountInfoModel = customList.getResultsetList().get(0);
	        	accountInfoModel.setFirstName(appUserModel.getFirstName());
	        	accountInfoModel.setLastName(appUserModel.getLastName());
	        	accountInfoDao.saveOrUpdate(accountInfoModel);
	        }
		
		/**
		 * Link core account
		 */
	  
		if(accountModel.getCoreAccountNumber()!=null)
		{
			if (this.linkPaymentModeManager.isFirstAccountOtherThanOla(userDeviceAccountsModel.getUserId())){
				
				LinkPaymentModeModel linkPaymentModeModel = new LinkPaymentModeModel();
				linkPaymentModeModel.setMfsId(userDeviceAccountsModel.getUserId());
				linkPaymentModeModel.setAccountNo(accountModel.getCoreAccountNumber());
				linkPaymentModeModel.setName(accountModel.getCoreAccountTitle());
				linkPaymentModeModel.setPaymentMode(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
				linkPaymentModeModel.setNic(accountModel.getApplicant1DetailModel().getIdNumber());
				linkPaymentMode(linkPaymentModeModel);
			}
		}
		
		/**
		 * END Link core account
		 */
		
		sendRegistationStateNotification(oldRegistrationStateId, newRegistrationStateId, appUserModel.getMobileNo());

		if(newRegistrationStateId!=null && oldRegistrationStateId != null)
		{	
			if(oldRegistrationStateId.longValue() != newRegistrationStateId)
			{
				if(newRegistrationStateId.longValue()==RegistrationStateConstantsInterface.VERIFIED)
				{
					String password = CommonUtils.generateNumber(9);
					appUserModel.setPassword(EncoderUtils.encodeToSha(password));
					this.appUserDAO.saveOrUpdate(appUserModel);

					/*BankModel bankModel = getOlaBankModel();
			        BaseWrapper baseWrapperBank = new BaseWrapperImpl();
			        baseWrapperBank.setBasePersistableModel(bankModel);
			        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);

			        SmartMoneyAccountModel smaModel = new SmartMoneyAccountModel();
					smaModel.setRetailerContactId(retailerContactModel.getRetailerContactId()) ;
					
					BaseWrapper bWrapper = new BaseWrapperImpl();
					bWrapper.setBasePersistableModel(smaModel);
					bWrapper = this.smartMoneyAccountManager.loadOLASmartMoneyAccount(bWrapper);
					smaModel = (SmartMoneyAccountModel) bWrapper.getBasePersistableModel();
					
			        AccountInfoModel accountInfo;
			        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
					try
					{			
						accountInfo = abstractFinancialInstitution.getAccountInfoModelBySmartMoneyAccount(smaModel, appUserModel.getAppUserId(), null);
				        veriflyBaseWrapper.setAccountInfoModel(accountInfo);
				        LogModel logmodel = new LogModel();
				        logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
				        logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
				        veriflyBaseWrapper.setLogModel(logmodel);
				        veriflyBaseWrapper.setBasePersistableModel(smaModel);
				        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.WEB);
					} catch (Exception e1)
					{
						e1.printStackTrace();
						throw new FrameworkCheckedException(veriflyErrorMessage);
					}*/
					///////initiate IVR call to agent to generate user pin //////////
					IvrRequestDTO ivrDTO = new IvrRequestDTO();
					ivrDTO.setAgentMobileNo(appUserModel.getMobileNo());
					ivrDTO.setCustomerMobileNo(appUserModel.getMobileNo());
					this.initiateUserGeneratedPinIvrCall(ivrDTO);
/*			        try
			        {
			            veriflyBaseWrapper = abstractFinancialInstitution.resetPin(veriflyBaseWrapper);
			        }
			        catch(Exception e)
			        {            
			        	e.printStackTrace();
			            
			            if(!veriflyBaseWrapper.isErrorStatus())
			            {
				            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
							if(null == veriflyBaseWrapper.getErrorMessage())
								veriflyErrorMessage ="Generate pin service in not available.";
							throw new FrameworkCheckedException(veriflyErrorMessage);
			            }
			        }*/

					String portalURL = null;
					String appURL = null;
					if(appUserModel.getMnoId() != null && appUserModel.getMnoId().equals(50028L))
					{
						appURL = "agentmate.sco.download.url";
						portalURL = "agentweb.sco.url";
					}
					else
					{
						appURL = "agentmate.download.url";
						portalURL = "agentweb.url";
					}
					Object[] args1 = {
							userDeviceAccountsModel.getUserId(),
							MessageUtil.getMessage(portalURL),
							appUserModel.getUsername(),
							password
					};

					Object[] args2 = {
							MessageUtil.getMessage(appURL),
							userDeviceAccountsModel.getUserId(),
							EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY,userDeviceAccountsModel.getPin())
					};
					String messageString1 = "";
					String messageString2 = "";

					messageString1 = MessageUtil.getMessage("AgentAccountCreated.Confirmation.SMS.part1", args1);
					messageString2 = MessageUtil.getMessage("AgentAccountCreated.Confirmation.SMS.part2", args2);

					/*if (agentMerchantDetailModel.getParentAgentId()==null)
					{
						messageString = MessageUtil.getMessage("ussd.agentAccountCreated", args);
					}
					else
					{
						messageString = MessageUtil.getMessage("ussd.directAgentAccountCreated", args);
					}
					*/

					SmsMessage smsMessage1 = new SmsMessage(appUserModel.getMobileNo(), messageString1,SMSConstants.Sender_1611);
					SmsMessage smsMessage2 = new SmsMessage(appUserModel.getMobileNo(), messageString2,SMSConstants.Sender_1611);
					smsSender.send(smsMessage1);
					smsSender.send(smsMessage2);
				}
			}
		}
		
		actionLogModel.setCustomField1(retailerContactModel.getRetailerContactId().toString());
		actionLogModel.setCustomField11(userDeviceAccountsModel.getUserId());
		this.actionLogManager.completeActionLog(actionLogModel);
		
		baseWrapper.putObject(Level3AccountModel.LEVEL3_ACCOUNT_MODEL_KEY, accountModel);
		baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID  , appUserModel.getAppUserId());
		
		return baseWrapper;
	}

	private List<CustomerPictureModel> searchCustomerPictures(RetailerContactModel retailerContactModel, Long applicantTypeId, Long applicantDetailId)
	{
		List<CustomerPictureModel> customerPictureModelList = null;
		CustomerPictureModel customerPictureModel = new CustomerPictureModel();
		customerPictureModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		customerPictureModel.setApplicantTypeId(applicantTypeId);
		customerPictureModel.setApplicantDetailId(applicantDetailId);
		CustomList<CustomerPictureModel> customList = customerPictureDAO.findByExample(customerPictureModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL );
		if(customList != null)
		{
			customerPictureModelList = customList.getResultsetList();
		}
		return customerPictureModelList;
	}

	private BusinessDetailModel findBusinessDetail(RetailerContactModel retailerContactModel)
	{
		BusinessDetailModel businessDetailModel = new BusinessDetailModel();
		businessDetailModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		CustomList<BusinessDetailModel> customList = businessDetailDAO.findByExample(businessDetailModel);
		if(customList != null)
		{
			List<BusinessDetailModel> list = customList.getResultsetList();
			if(CollectionUtils.isNotEmpty(list))
			{
				businessDetailModel = list.get(0);
			}
		}
		return businessDetailModel;
	}

	private ApplicantDetailModel searchApplicantDetail(RetailerContactModel retailerContactModel, Long applicantTypeId)
	{
		List<ApplicantDetailModel> applicantDetailModelList = null;

		ApplicantDetailModel applicantDetailModel = new ApplicantDetailModel();
		applicantDetailModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		applicantDetailModel.setApplicantTypeId(applicantTypeId);
		CustomList<ApplicantDetailModel> customList = applicantDetailDAO.findByExample(applicantDetailModel);
		if(customList != null)
		{
			applicantDetailModelList = customList.getResultsetList();
			if( CollectionUtils.isNotEmpty(applicantDetailModelList) )
			{
				applicantDetailModel = applicantDetailModelList.get(0);
			}
		}

		return applicantDetailModel;
	}
	
	private List<ApplicantDetailModel> searchApplicantDetails(RetailerContactModel retailerContactModel, Long applicantTypeId)
	{
		List<ApplicantDetailModel> applicantDetailModelList = null;

		ApplicantDetailModel applicantDetailModel = new ApplicantDetailModel();
		applicantDetailModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		applicantDetailModel.setApplicantTypeId(applicantTypeId);
		CustomList<ApplicantDetailModel> customList = applicantDetailDAO.findByExample(applicantDetailModel);
		applicantDetailModelList = new ArrayList<ApplicantDetailModel>();
		if(customList!=null && customList.getResultsetList()!=null){
			applicantDetailModelList.addAll(customList.getResultsetList());
		}
		return applicantDetailModelList;
	}

	/**
	 * @param nowDate
	 * @param accountModel
	 * @param retailerContactModel
	 * @return
	 */
	private ApplicantDetailModel extractApplicantDetailModel(Date nowDate, ApplicantDetailModel applicantDetailModel, RetailerContactModel retailerContactModel)
	{
		applicantDetailModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		applicantDetailModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		applicantDetailModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		applicantDetailModel.setUpdatedOn(nowDate);
		applicantDetailModel.setCreatedOn(nowDate);
		applicantDetailModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
		return applicantDetailModel;
	}

	private ApplicantDetailModel mergeApplicantDetail(ApplicantDetailModel oldApplicantDetailModel, ApplicantDetailModel newApplicantDetailModel)
	{
		oldApplicantDetailModel.setTitle(newApplicantDetailModel.getTitle());
		oldApplicantDetailModel.setGender(newApplicantDetailModel.getGender());
		oldApplicantDetailModel.setName(newApplicantDetailModel.getName());
		oldApplicantDetailModel.setIdType(newApplicantDetailModel.getIdType());
		oldApplicantDetailModel.setIdNumber(newApplicantDetailModel.getIdNumber());
		oldApplicantDetailModel.setIdExpiryDate(newApplicantDetailModel.getIdExpiryDate());
		oldApplicantDetailModel.setVisaExpiryDate(newApplicantDetailModel.getVisaExpiryDate());
		oldApplicantDetailModel.setDob(newApplicantDetailModel.getDob());
		oldApplicantDetailModel.setBirthPlace(newApplicantDetailModel.getBirthPlace());
		oldApplicantDetailModel.setNtn(newApplicantDetailModel.getNtn());
		oldApplicantDetailModel.setMotherMaidenName(newApplicantDetailModel.getMotherMaidenName());
		oldApplicantDetailModel.setResidentialStatus(newApplicantDetailModel.getResidentialStatus());
		oldApplicantDetailModel.setUsCitizen(newApplicantDetailModel.getUsCitizen());
		oldApplicantDetailModel.setNationality(newApplicantDetailModel.getNationality());
		oldApplicantDetailModel.setMobileNo(newApplicantDetailModel.getMobileNo());
		oldApplicantDetailModel.setLandLineNo(newApplicantDetailModel.getLandLineNo());
		oldApplicantDetailModel.setFax(newApplicantDetailModel.getFax());
		oldApplicantDetailModel.setContactNo(newApplicantDetailModel.getContactNo());
		oldApplicantDetailModel.setEmail(newApplicantDetailModel.getEmail());
		oldApplicantDetailModel.setFatherHusbandName(newApplicantDetailModel.getFatherHusbandName());
		oldApplicantDetailModel.setEmployerName(newApplicantDetailModel.getEmployerName());
		oldApplicantDetailModel.setOccupation(newApplicantDetailModel.getOccupation());
		oldApplicantDetailModel.setProfession(newApplicantDetailModel.getProfession());
		oldApplicantDetailModel.setMailingAddressType(newApplicantDetailModel.getMailingAddressType());
		oldApplicantDetailModel.setMaritalStatus(newApplicantDetailModel.getMaritalStatus());
		oldApplicantDetailModel.setVerisysDone(newApplicantDetailModel.getVerisysDone());
		oldApplicantDetailModel.setScreeningPerformed(newApplicantDetailModel.getScreeningPerformed());
		oldApplicantDetailModel.setUpdatedOn(new Date());
		oldApplicantDetailModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		oldApplicantDetailModel.setCreatedOn(new Date());
		oldApplicantDetailModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
		oldApplicantDetailModel.setApplicantTypeId(newApplicantDetailModel.getApplicantTypeId());
		
		return oldApplicantDetailModel;
	}

	/**
	 * @param retailerContactModel
	 * @param addressModel
	 * @return @link RetailerContactAddressesModel
	 */
	private RetailerContactAddressesModel extractRetailerContactAddress(RetailerContactModel retailerContactModel,
			AddressModel addressModel, Long addressTypeId, Long applicantTypeId, Long applicantDetailId)
	{
		RetailerContactAddressesModel retailerContactAddressesModel = new RetailerContactAddressesModel();
		retailerContactAddressesModel.setAddressId(addressModel.getAddressId());
		retailerContactAddressesModel.setAddressTypeId(addressTypeId);
		retailerContactAddressesModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
		retailerContactAddressesModel.setApplicantTypeId(applicantTypeId);
		retailerContactAddressesModel.setApplicantDetailId(applicantDetailId);
		return retailerContactAddressesModel;
	}

	private AppUserModel extractAppUserForRetailerContact(Level3AccountModel level3AccountModel) throws FrameworkCheckedException 
	{
		ApplicantDetailModel applicant1DetailModel = level3AccountModel.getApplicant1DetailModel();
		
		AppUserModel appUserModel = new AppUserModel();

		//appUserModel.setAppUserId(level3AccountModel.getAppUserId());
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
		appUserModel.setRetailerContactId(level3AccountModel.getRetailerContactId());
		appUserModel.setRegistrationStateId(level3AccountModel.getRegistrationStateId());
		if(level3AccountModel.getAccountStateId() != null)
			appUserModel.setAccountStateId(level3AccountModel.getAccountStateId());
		appUserModel.setMobileNo(level3AccountModel.getApplicant1DetailModel().getMobileNo());
		appUserModel.setNicExpiryDate(applicant1DetailModel.getIdExpiryDate());
		appUserModel.setDob(applicant1DetailModel.getDob());
		appUserModel.setNic(applicant1DetailModel.getIdNumber());
		appUserModel.setVerified(true);
		appUserModel.setAccountEnabled(true);
		appUserModel.setAccountExpired(false);
		appUserModel.setAccountLocked(false);
		appUserModel.setCredentialsExpired(false);
		appUserModel.setAccountClosedUnsettled(false);
		appUserModel.setAccountClosedSettled(false);
		appUserModel.setUpdatedOn(new Date());
		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		appUserModel.setCreatedOn(new Date());
		appUserModel.setPasswordChangeRequired(Boolean.TRUE);
		appUserModel.setAccountClosedUnsettled(false);
		appUserModel.setAccountClosedSettled(false);
		appUserModel.setMobileTypeId(Constants.MOBILE_TYPE_POSTPAID);
		
		String[] nameArray = applicant1DetailModel.getName().split(" ");
		appUserModel.setFirstName(nameArray[0]);
		if (nameArray.length > 1)
		{
			appUserModel.setLastName(applicant1DetailModel.getName().substring(appUserModel.getFirstName().length() + 1));
		} else
		{
			appUserModel.setLastName(nameArray[0]);
		}
		
		if (applicant1DetailModel.getEmail() != null)
		{
			appUserModel.setEmail(applicant1DetailModel.getEmail().trim());
		}
		
		String nicWithoutHyphins = applicant1DetailModel.getIdNumber().replace("-", "");
		appUserModel.setNic(nicWithoutHyphins);

		if(level3AccountModel.getServiceOperatorId() != null)
			appUserModel.setMnoId(level3AccountModel.getServiceOperatorId());
		else
			appUserModel.setMnoId(50027L);

		return appUserModel;
	}

	private AppUserModel updateAppUserModel(AppUserModel appUserModel, Level3AccountModel level3AccountModel)
	{
		ApplicantDetailModel applicant1DetailModel = level3AccountModel.getApplicant1DetailModel();
		appUserModel.setRegistrationStateId(level3AccountModel.getRegistrationStateId());
		appUserModel.setMobileNo(level3AccountModel.getApplicant1DetailModel().getMobileNo());
		appUserModel.setNicExpiryDate(applicant1DetailModel.getIdExpiryDate());
		appUserModel.setDob(applicant1DetailModel.getDob());
		appUserModel.setNic(applicant1DetailModel.getIdNumber());
		appUserModel.setUpdatedOn(new Date());
		appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			
		if (applicant1DetailModel.getEmail() != null)
		{
			appUserModel.setEmail(applicant1DetailModel.getEmail().trim());
		}
		
		String nicWithoutHyphins = applicant1DetailModel.getIdNumber().replace("-", "");
		appUserModel.setNic(nicWithoutHyphins);
		
		String[] nameArray = applicant1DetailModel.getName().split(" ");
		appUserModel.setFirstName(nameArray[0]);
		if(nameArray.length > 1)
		{
			appUserModel.setLastName(applicant1DetailModel.getName().substring(appUserModel.getFirstName().length()+1));
		}
		else
		{
			appUserModel.setLastName(nameArray[0]);
		}
		if(applicant1DetailModel.getEmail() != null)
		{
			appUserModel.setEmail(applicant1DetailModel.getEmail().trim());
		}
		RetailerContactModel retailerContactModel = appUserModel.getRetailerContactIdRetailerContactModel();
		if(retailerContactModel != null )
		{
			try {
				RetailerModel retailerModel = retailerContactDAO.getRetailerModelByRetailerId(retailerContactModel.getRetailerId());
				DistributorModel distributorModel = retailerModel.getRelationDistributorIdDistributorModel();
				if(distributorModel.getMnoId() == null)
					appUserModel.setMnoId(50027L);
				else
					appUserModel.setMnoId(distributorModel.getMnoId());
			} catch (FrameworkCheckedException e) {
				e.printStackTrace();
			}
		}
		return appUserModel;
	}

	private RetailerContactModel extractRetailerContactModel(Level3AccountModel level3AccountModel,AgentMerchantDetailModel agentMerchantDetailModel) throws FrameworkCheckedException
	{
		RetailerContactModel retailerContactModel = new RetailerContactModel();
		
		retailerContactModel.setInitialAppFormNo(level3AccountModel.getInitialAppFormNo());
		retailerContactModel.setName(level3AccountModel.getAcTitle());
		retailerContactModel.setCurrency(level3AccountModel.getCurrencyId());
		retailerContactModel.setTaxRegimeId(level3AccountModel.getTaxRegimeId());
		retailerContactModel.setFed(level3AccountModel.getFed());
		retailerContactModel.setCnic(level3AccountModel.getApplicant1DetailModel().getIdNumber());
		retailerContactModel.setCnicExpiry(level3AccountModel.getApplicant1DetailModel().getIdExpiryDate());
		retailerContactModel.setZongMsisdn(level3AccountModel.getApplicant1DetailModel().getMobileNo());
		//business info
		retailerContactModel.setAccountPurposeId(level3AccountModel.getAccountPurposeId());
		retailerContactModel.setAcNature(level3AccountModel.getAcNature());
		retailerContactModel.setBusinessName(level3AccountModel.getBusinessName());
		retailerContactModel.setCommencementDate(level3AccountModel.getCommencementDate());
		retailerContactModel.setIncorporationDate(level3AccountModel.getIncorporationDate());
		retailerContactModel.setSecpRegNo(level3AccountModel.getSecpRegNo());
		retailerContactModel.setSecpRegDate(level3AccountModel.getSecpRegDate());
		retailerContactModel.setNtn(level3AccountModel.getNtn());
		retailerContactModel.setSalesTaxRegNo(level3AccountModel.getSalesTaxRegNo());
		retailerContactModel.setMembershipNoTradeBody(level3AccountModel.getMembershipNoTradeBody());
		retailerContactModel.setTradeBody(level3AccountModel.getTradeBody());
		retailerContactModel.setBusinessTypeId(level3AccountModel.getBusinessTypeId());
		retailerContactModel.setBusinessNatureId(level3AccountModel.getBusinessNatureId());
		retailerContactModel.setLocationTypeId(level3AccountModel.getLocationTypeId());
		retailerContactModel.setLocationSizeId(level3AccountModel.getLocationSizeId());
		retailerContactModel.setEstSince(level3AccountModel.getEstSince());
		retailerContactModel.setLandLineNo(level3AccountModel.getLandLineNo());
		
		retailerContactModel.setAreaId(agentMerchantDetailModel.getActionId());
		retailerContactModel.setRetailerId(agentMerchantDetailModel.getRetailerId());
		retailerContactModel.setProductCatalogId(agentMerchantDetailModel.getProductCatalogId());
		retailerContactModel.setDistributorLevelId(agentMerchantDetailModel.getDistributorLevelId());
		retailerContactModel.setAreaId(agentMerchantDetailModel.getAreaId());
		retailerContactModel.setBvsEnable(level3AccountModel.getBvsEnable());

		AppUserModel appUserModel=new AppUserModel();
		appUserModel.setEmployeeId(agentMerchantDetailModel.getEmpId());
		
		CustomList<AppUserModel>	appUserList=appUserDAO.findByExample(appUserModel);
		
		if(appUserList!=null && appUserList.getResultsetList().size()>0){
			appUserModel=	appUserList.getResultsetList().get(0);
			SalesHierarchyModel salesHierarchyModel = salesHierarchyDAO.findSaleUserByBankUserId(appUserModel.getAppUserId());
			retailerContactModel.setSalesHierarchyId(salesHierarchyModel.getSalesHierarchyId());
		}
		
		
//		retailerContactModel.setParentRetailerContactModelId(?);
//		retailerContactModel.setSalesHierarchyId(?);
//		retailerContactModel.setDistributorLevelId(?);
		
		//NOK Information
		NokDetailVOModel nokDetailVOModel = level3AccountModel.getNokDetailVOModel();
		if(nokDetailVOModel != null)
		{
			retailerContactModel.setNokName(nokDetailVOModel.getNokName());
			retailerContactModel.setNokRelationship(nokDetailVOModel.getNokRelationship());
			retailerContactModel.setNokContactNo(nokDetailVOModel.getNokContactNo());
			retailerContactModel.setNokMobile(nokDetailVOModel.getNokMobile());
			retailerContactModel.setNokIdNumber(nokDetailVOModel.getNokIdNumber());
			retailerContactModel.setNokIdType(nokDetailVOModel.getNokIdType());
		}
		//Relationship Details
		retailerContactModel.setAccountOpeningDate(level3AccountModel.getAccountOpeningDate());

		
		retailerContactModel.setUpdatedOn(new Date());
		retailerContactModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		retailerContactModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		retailerContactModel.setCreatedOn(new Date());
		retailerContactModel.setApplicationNo(retailerSequenceGenerator.nextLongValue().toString());
		retailerContactModel.setRegStateComments(level3AccountModel.getRegStateComments());
		retailerContactModel.setActive(Boolean.FALSE);
		retailerContactModel.setRso(false); // Temporarily commented as RSO value is not provided from screen
		retailerContactModel.setHead(agentMerchantDetailModel.getParentAgentId()==null ? true:false);
		retailerContactModel.setBalance(Double.valueOf(0));		 //  TODO 	This field need to be remove
		retailerContactModel.setBusinessTypeId(level3AccountModel.getBusinessTypeId());
		retailerContactModel.setCustomerTypeId(level3AccountModel.getCustomerTypeId());
		retailerContactModel.setFundSourceId(level3AccountModel.getFundsSourceId());
		retailerContactModel.setTransactionModeId(level3AccountModel.getTransactionModeId());
		retailerContactModel.setOtherTransactionMode(level3AccountModel.getOtherTransactionMode());
		retailerContactModel.setAccountReasonId(level3AccountModel.getAccountReason());
		retailerContactModel.setSalary(level3AccountModel.getSalary());
		retailerContactModel.setBusinessIncome(level3AccountModel.getBuisnessIncome());
		retailerContactModel.setOtherIncome(level3AccountModel.getOtherIncome());
		retailerContactModel.setScreeningPerformed(level3AccountModel.isScreeningPerformed());
		retailerContactModel.setVerisysDone(level3AccountModel.getVerisysDone());
		retailerContactModel.setOlaCustomerAccountTypeModelId(getLevel3AccountLevel(retailerContactModel.getInitialAppFormNo()));
		return retailerContactModel;
	}

	private RetailerContactModel updateRetailerContactModel(Level3AccountModel level3AccountModel, RetailerContactModel retailerContactModel)
	{
		retailerContactModel.setUpdatedOn(new Date());
		retailerContactModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		
		retailerContactModel.setInitialAppFormNo(level3AccountModel.getInitialAppFormNo());
		retailerContactModel.setName(level3AccountModel.getAcTitle());
		retailerContactModel.setCurrency(level3AccountModel.getCurrencyId());
		retailerContactModel.setTaxRegimeId(level3AccountModel.getTaxRegimeId());
		retailerContactModel.setFed(level3AccountModel.getFed());
		retailerContactModel.setCnic(level3AccountModel.getApplicant1DetailModel().getIdNumber());
		retailerContactModel.setCnicExpiry(level3AccountModel.getApplicant1DetailModel().getIdExpiryDate());
		retailerContactModel.setZongMsisdn(level3AccountModel.getApplicant1DetailModel().getMobileNo());
		//business info
		retailerContactModel.setAccountPurposeId(level3AccountModel.getAccountPurposeId());
		retailerContactModel.setAcNature(level3AccountModel.getAcNature());
		retailerContactModel.setBusinessName(level3AccountModel.getBusinessName());
		retailerContactModel.setCommencementDate(level3AccountModel.getCommencementDate());
		retailerContactModel.setIncorporationDate(level3AccountModel.getIncorporationDate());
		retailerContactModel.setSecpRegNo(level3AccountModel.getSecpRegNo());
		retailerContactModel.setSecpRegDate(level3AccountModel.getSecpRegDate());
		retailerContactModel.setNtn(level3AccountModel.getNtn());
		retailerContactModel.setSalesTaxRegNo(level3AccountModel.getSalesTaxRegNo());
		retailerContactModel.setMembershipNoTradeBody(level3AccountModel.getMembershipNoTradeBody());
		retailerContactModel.setTradeBody(level3AccountModel.getTradeBody());
		retailerContactModel.setBusinessTypeId(level3AccountModel.getBusinessTypeId());
		retailerContactModel.setBusinessNatureId(level3AccountModel.getBusinessNatureId());
		retailerContactModel.setLocationTypeId(level3AccountModel.getLocationTypeId());
		retailerContactModel.setLocationSizeId(level3AccountModel.getLocationSizeId());
		retailerContactModel.setEstSince(level3AccountModel.getEstSince());
		retailerContactModel.setLandLineNo(level3AccountModel.getLandLineNo());
		//NOK Information
		NokDetailVOModel nokDetailVOModel = level3AccountModel.getNokDetailVOModel();
		retailerContactModel.setNokName(nokDetailVOModel.getNokName());
		retailerContactModel.setNokRelationship(nokDetailVOModel.getNokRelationship());
		retailerContactModel.setNokContactNo(nokDetailVOModel.getNokContactNo());
		retailerContactModel.setNokMobile(nokDetailVOModel.getNokMobile());
		retailerContactModel.setNokIdNumber(nokDetailVOModel.getNokIdNumber());
		retailerContactModel.setNokIdType(nokDetailVOModel.getNokIdType());
		//Relationship Details
		retailerContactModel.setAccountOpeningDate(level3AccountModel.getAccountOpeningDate());
		
		retailerContactModel.setRegStateComments(level3AccountModel.getRegStateComments());	
		retailerContactModel.setBusinessTypeId(level3AccountModel.getBusinessTypeId());
		retailerContactModel.setCustomerTypeId(level3AccountModel.getCustomerTypeId());
		retailerContactModel.setFundSourceId(level3AccountModel.getFundsSourceId());
		retailerContactModel.setTransactionModeId(level3AccountModel.getTransactionModeId());
		retailerContactModel.setOtherTransactionMode(level3AccountModel.getOtherTransactionMode());
		retailerContactModel.setAccountReasonId(level3AccountModel.getAccountReason());
		retailerContactModel.setSalary(level3AccountModel.getSalary());
		retailerContactModel.setBusinessIncome(level3AccountModel.getBuisnessIncome());
		retailerContactModel.setOtherIncome(level3AccountModel.getOtherIncome());
		retailerContactModel.setScreeningPerformed(level3AccountModel.isScreeningPerformed());
		retailerContactModel.setVerisysDone(level3AccountModel.getVerisysDone());
		retailerContactModel.setBvsEnable(level3AccountModel.getBvsEnable());

		if(level3AccountModel.getRegistrationStateId()==RegistrationStateConstants.VERIFIED.longValue()){
			retailerContactModel.setActive(Boolean.TRUE);
		}
		
		return retailerContactModel;
	}

	private BusinessDetailModel mergeBusinessDetailModels(BusinessDetailModel newBusinessDetailModel, BusinessDetailModel oldBusinessDetailModel )
	{
		oldBusinessDetailModel.setAvgMonthlyIncome(newBusinessDetailModel.getAvgMonthlyIncome());
		oldBusinessDetailModel.setMinMonthlyIncome(newBusinessDetailModel.getMinMonthlyIncome());
		oldBusinessDetailModel.setMaxMonthlyIncome(newBusinessDetailModel.getMaxMonthlyIncome());
		oldBusinessDetailModel.setAnnualMonthlyTurnover(newBusinessDetailModel.getAnnualMonthlyTurnover());
		oldBusinessDetailModel.setMajorBuyer(newBusinessDetailModel.getMajorBuyer());
		oldBusinessDetailModel.setSupplierCustomer(newBusinessDetailModel.getSupplierCustomer());
		oldBusinessDetailModel.setOtherACwithJSBL(newBusinessDetailModel.getOtherACwithJSBL());
		return oldBusinessDetailModel;
	}

	private UserDeviceAccountsModel extractUserDeviceAccount(Level3AccountModel level3AccountModel)
	{
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();

		userDeviceAccountsModel.setAccountEnabled(true);
		userDeviceAccountsModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		userDeviceAccountsModel.setCreatedOn(new Date());
		userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		userDeviceAccountsModel.setUpdatedOn(new Date());
		userDeviceAccountsModel.setAppUserId(level3AccountModel.getAppUserId());
		String userId = String.valueOf(this.allpaySequenceGenerator.nextLongValue());
		userDeviceAccountsModel.setUserId(userId);
		
		userDeviceAccountsModel.setPinChangeRequired(true);
		userDeviceAccountsModel.setPasswordChangeRequired(false);
		String randomPin = RandomUtils.generateRandom(4, false, true);
		userDeviceAccountsModel.setPin(EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, randomPin));

		userDeviceAccountsModel.setCommissioned(false);
		userDeviceAccountsModel.setAccountExpired(false);
		userDeviceAccountsModel.setAccountLocked(false);
		userDeviceAccountsModel.setDeviceTypeId( DeviceTypeConstantsInterface.ALL_PAY ); 
		userDeviceAccountsModel.setCredentialsExpired(false);
		return userDeviceAccountsModel;
	}

	public BankModel getOlaBankModel()
	{
		BankModel bankModel = new BankModel();
		bankModel.setFinancialIntegrationId( FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION); // 4 is for OLA bank
		CustomList<BankModel> bankList = this.bankDAO.findByExample(bankModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		List<BankModel> bankL = bankList.getResultsetList();
		bankModel = (BankModel) bankL.get(0);
		return bankModel;
	}

	private SmartMoneyAccountModel extractSmartMoneyModel(Long retailerContactId, String userIdUDA, BankModel bankModel,AbstractFinancialInstitution abstractFinancialInstitution) throws FrameworkCheckedException{
		SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
		try {

			smartMoneyAccountModel = new SmartMoneyAccountModel (); 
			smartMoneyAccountModel.setRetailerContactId(retailerContactId);
			smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			smartMoneyAccountModel.setName( "i8_ret_" + userIdUDA);
			smartMoneyAccountModel.setBankId(bankModel.getBankId());
			smartMoneyAccountModel.setCreatedOn(new Date());
			smartMoneyAccountModel.setUpdatedOn(new Date());
			smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			smartMoneyAccountModel.setActive(true);
			smartMoneyAccountModel.setDefAccount(true);
			smartMoneyAccountModel.setDeleted(false);
			
			if (abstractFinancialInstitution.isVeriflyRequired()) {
				if (abstractFinancialInstitution.isVeriflyLite()) {
					smartMoneyAccountModel.setChangePinRequired(false);
				} else {
					smartMoneyAccountModel.setChangePinRequired(true);
				}
			} else {
				smartMoneyAccountModel.setChangePinRequired(false);
			}
			
//			baseWrapper.setBasePersistableModel();
			
		}catch (ImplementationNotSupportedException inse) {
			throw new FrameworkCheckedException(
					"implementationNotSupportedException");
		} catch (Exception ex) {
			if (ex.getCause() instanceof ConstraintViolationException){
				ConstraintViolationException constrainViolationException = (ConstraintViolationException)ex.getCause();
				String constraintName = "UK_USER_DEVICE_TYPE";
				if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1){
					throw new FrameworkCheckedException("Allpay Id already exists");
				}
				constraintName = "UK_SM_ACCOUNT";
				if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1){
					throw new FrameworkCheckedException("Account nick already exists");
				}
			}
			ex.printStackTrace();
			throw new FrameworkCheckedException(ex.getMessage(), ex);
		}
		return smartMoneyAccountModel;
	}
	
	@Override
	public List<CustomerPictureModel> getAllRetailerContactPictures(Long retailerContactId)
			throws FrameworkCheckedException {
		List<CustomerPictureModel> customerPictureList = customerPictureDAO.getAllRetailerContactPictures(retailerContactId);
		return customerPictureList;
	}

	private void sendRegistationStateNotification(Long oldRegistrationStateId, Long newRegistrationStateId, String mobileNo) throws FrameworkCheckedException
    {
		if(newRegistrationStateId!=null && oldRegistrationStateId != null)
		{	
			if(oldRegistrationStateId.longValue() != newRegistrationStateId)
			{
				if(newRegistrationStateId.longValue()==RegistrationStateConstantsInterface.VERIFIED
					|| newRegistrationStateId.longValue()==RegistrationStateConstantsInterface.DISCREPANT
					|| newRegistrationStateId.longValue()==RegistrationStateConstantsInterface.DECLINE
					|| newRegistrationStateId.longValue()==RegistrationStateConstantsInterface.REJECTED)
				{
					Object[] args = {RegistrationStateConstantsInterface.REG_STATE_MAP.get(newRegistrationStateId)};
					String messageString = MessageUtil.getMessage("level3Account.registrationStateChanged", args);
					SmsMessage smsMessage = new SmsMessage(mobileNo, messageString);
					smsSender.send(smsMessage);
				}
			}
		}
    }
	
	private boolean isContainRetailerContactAddress(Collection<RetailerContactAddressesModel> retailerContactAddresses, Long applicantDetailId){
		boolean isExists = false;
		for(RetailerContactAddressesModel retailerContactAddressesModel : retailerContactAddresses){
			if(retailerContactAddressesModel.getApplicantDetailId() != null){
				if(retailerContactAddressesModel.getApplicantDetailId().equals(applicantDetailId)){
					isExists = true;
					break;
				}
			}
		}
		return isExists;
	}
	
	private boolean isNICUnique(String nic, Long appUserId){
		/*AppUserModel appUserModel = new AppUserModel();
		appUserModel.setNic(nic.replace("-", ""));
		appUserModel.setAccountClosedUnsettled(false);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		ExampleConfigHolderModel configModel = new ExampleConfigHolderModel();
		configModel.setMatchMode(MatchMode.EXACT);
		Integer size = this.appUserDAO.countByExample(appUserModel,null,configModel);
		if(size == 0)
			return true;
		else
			return false;
		*/
		boolean returnValue = Boolean.TRUE;
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setNic(nic);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
		appUserModel.setAppUserId(appUserId);
		CustomList<AppUserModel> customAppUserModelList =  this.appUserDAO.findByExample(appUserModel);
		if(customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0)
		{
			if(appUserId != null && appUserId > 0)
			{
				for(AppUserModel model:customAppUserModelList.getResultsetList())
				{
					if(!model.getAccountClosedSettled() && !model.getAppUserId().equals(appUserId))
					{
						returnValue = Boolean.FALSE;
						break;
					}
				}
			}
			else
			{
				//returnValue = Boolean.FALSE;
				for(AppUserModel model:customAppUserModelList.getResultsetList())
				{
					if(!model.getAccountClosedSettled())
					{
						returnValue = Boolean.FALSE;
						break;
					}
				}
			}
		}
		
		if(returnValue)
		{
			appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			customAppUserModelList =  this.appUserDAO.findByExample(appUserModel);
			if(customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0)
			{
				if(appUserId != null && appUserId > 0)
				{
					for(AppUserModel model:customAppUserModelList.getResultsetList())
					{
						if(!model.getAccountClosedSettled() && !model.getAppUserId().equals(appUserId))
						{
							returnValue = Boolean.FALSE;
							break;
						}
					}
				}
				else
				{
					//returnValue = Boolean.FALSE;
					for(AppUserModel model:customAppUserModelList.getResultsetList())
					{
						if(!model.getAccountClosedSettled())
						{
							returnValue = Boolean.FALSE;
							break;
						}
					}
				}
			}	
		}
		//adding handler case as well
  		if(returnValue)
  		{
  			appUserModel.setAppUserTypeId(UserTypeConstantsInterface.HANDLER);
  			customAppUserModelList =  this.appUserDAO.findByExample(appUserModel);
  			if(customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0)
  			{
  				if(appUserId != null && appUserId > 0)
  				{
  					for(AppUserModel model:customAppUserModelList.getResultsetList())
  					{
  						if(!model.getAccountClosedSettled() && !model.getAppUserId().equals(appUserId) )
  						{
  							returnValue = Boolean.FALSE;
  							break;
  						}
  					}
  				}
  				else
  				{
  					//returnValue = Boolean.FALSE;
  					for(AppUserModel model:customAppUserModelList.getResultsetList())
  					{
  						if(!model.getAccountClosedSettled())
  						{
  							returnValue = Boolean.FALSE;
  							break;
  						}
  					}
  				}
  			}	
  		}

		return returnValue;
	}
	
	@Override
	public SearchBaseWrapper searchLevel3Account(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		RetailerContactModel retailerContactModel = (RetailerContactModel) searchBaseWrapper.getBasePersistableModel();
		CustomList<RetailerContactModel> customList = retailerContactDAO.findByExample(retailerContactModel,"");
		
		if(null != customList && customList.getResultsetList().size()>0)
		{
			retailerContactModel	=	customList.getResultsetList().get(0);
		}
		
		searchBaseWrapper.setCustomList(customList);
		return searchBaseWrapper;
	}
	
	@Override
	public CityModel loadCityModel(Long cityId) throws FrameworkCheckedException
	{
		CityModel cityModel = cityDAO.findByPrimaryKey(cityId);
		return cityModel;
	}
	
	@Override
	public ProfessionModel loadProfessionModel(Long professionId) throws FrameworkCheckedException
	{
		ProfessionModel professionModel = professionDAO.findByPrimaryKey(professionId);
		return professionModel;
	}
	
	@Override
	public OccupationModel loadOccupationModel(Long occupationId) throws FrameworkCheckedException
	{
		OccupationModel occupationModel = occupationDAO.findByPrimaryKey(occupationId);
		return occupationModel;
	}
	
	@Override
	public BusinessTypeModel loadBusinessTypeModel(Long businessTypeId) throws FrameworkCheckedException
	{
		BusinessTypeModel businessTypeModel = businessTypeDAO.findByPrimaryKey(businessTypeId);
		return businessTypeModel;
	}

	@Override
	public boolean isIdDocumentNumberAlreadyExist(String initialAppFormNumber, Long idDocumentType, 
			String idDocumentNumber) throws FrameworkCheckedException
	{
		
		RetailerContactModel	retailerContactModel=new RetailerContactModel();
		retailerContactModel.setInitialAppFormNo(initialAppFormNumber);
		CustomList<RetailerContactModel>retailerContactModelList = retailerContactDAO.findByExample(retailerContactModel);
		
		if(retailerContactModelList!=null && retailerContactModelList.getResultsetList().size()>0)
		{
			/*if(retailerContactDAO.isKinIdDocumentNumberAlreadyExist(
					initialAppFormNumber, idDocumentType, idDocumentNumber))
			{
				throw new FrameworkCheckedException("Duplicate_Applicant_Nok_Id");
			}
			else*/{
				retailerContactModel=	retailerContactModelList.getResultsetList().get(0);
				if(applicantDetailDAO.isIdDocumentNumberAleardyExist(retailerContactModel.getRetailerContactId(), 
						idDocumentType, idDocumentNumber)){
					throw new FrameworkCheckedException("Duplicate_Applicant_Id");
				}
			}
		}
		
		return false;
	}
	
	@Override
	public String getLinkedCoreAccountNo(String appUserUserName)
	{
		AllpayDeReLinkListViewModel	allpayDeReLinkListViewModel=new AllpayDeReLinkListViewModel();
		allpayDeReLinkListViewModel.setUsername(appUserUserName);
		CustomList<AllpayDeReLinkListViewModel>customList=	allpayDelinkRelinkPaymentModeViewDAO.findByExample(allpayDeReLinkListViewModel);

		if(customList!=null && customList.getResultsetList().size()>0){
			return customList.getResultsetList().get(0).getAccountNo();
		}
		
		return null;
	}
	
	/**
	 * @author Abu Turab
	 * @param ivrDTO
	 * @throws FrameworkCheckedException
	 */
	private void initiateUserGeneratedPinIvrCall(IvrRequestDTO ivrDTO) throws FrameworkCheckedException{
		ivrDTO.setRetryCount(0);
		ivrDTO.setProductId(new Long(CommandFieldConstants.CREATE_PIN_IVR));
		try {
			ivrRequestHandler.makeIvrRequest(ivrDTO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FrameworkCheckedException(e.getLocalizedMessage());
		}
		
	}
	
	@Override
	public boolean isCoreAccountLinkedToOtherAgent(String accountNumber, Long retailerContact)throws FrameworkCheckedException
	{
		return 	allpayDelinkRelinkPaymentModeViewDAO.
				isCoreAccountLinkedToOtherAgent(accountNumber, retailerContact);
	}
	
	public void setRetailerSequenceGenerator(OracleSequenceGeneratorJdbcDAO retailerSequenceGenerator)
	{
		this.retailerSequenceGenerator = retailerSequenceGenerator;
	}

	public void setAllpaySequenceGenerator(OracleSequenceGeneratorJdbcDAO allpaySequenceGenerator)
	{
		this.allpaySequenceGenerator = allpaySequenceGenerator;
	}

	public void setActionLogManager(ActionLogManager actionLogManager)
	{
		this.actionLogManager = actionLogManager;
	}

	public void setRetailerContactDAO(RetailerContactDAO retailerContactDAO)
	{
		this.retailerContactDAO = retailerContactDAO;
	}

	public void setAddressDAO(AddressDAO addressDAO)
	{
		this.addressDAO = addressDAO;
	}

	public void setRetailerContactAddressesDAO(RetailerContactAddressesDAO retailerContactAddressesDAO)
	{
		this.retailerContactAddressesDAO = retailerContactAddressesDAO;
	}

	public void setAppUserDAO(AppUserDAO appUserDAO)
	{
		this.appUserDAO = appUserDAO;
	}

	public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO)
	{
		this.userDeviceAccountsDAO = userDeviceAccountsDAO;
	}

	public void setApplicantDetailDAO(ApplicantDetailDAO applicantDetailDAO)
	{
		this.applicantDetailDAO = applicantDetailDAO;
	}

	public void setBusinessDetailDAO(BusinessDetailDAO businessDetailDAO)
	{
		this.businessDetailDAO = businessDetailDAO;
	}

	public void setCustomerPictureDAO(CustomerPictureDAO customerPictureDAO)
	{
		this.customerPictureDAO = customerPictureDAO;
	}

	public void setBankDAO(BankDAO bankDAO)
	{
		this.bankDAO = bankDAO;
	}

	public void setLinkPaymentModeDAO(LinkPaymentModeDAO linkPaymentModeDAO)
	{
		this.linkPaymentModeDAO = linkPaymentModeDAO;
	}

	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
	{
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public void setOlaVeriflyFinancialInstitution(FinancialInstitution olaVeriflyFinancialInstitution)
	{
		this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
	}

	public void setLevel3AccountsViewDao(Level3AccountsViewDao level3AccountsViewDao)
	{
		this.level3AccountsViewDao = level3AccountsViewDao;
	}

	public void setSmsSender(SmsSender smsSender) 
	{
		this.smsSender = smsSender;
	}

	public void setAcOwnerShipDetailDAO(ACOwnerShipDAO acOwnerShipDetailDAO)
	{
		this.acOwnerShipDetailDAO = acOwnerShipDetailDAO;
	}

	public void setAgentMerchantDetailDAO(AgentMerchantDetailDAO agentMerchantDetailDAO)
	{
		this.agentMerchantDetailDAO = agentMerchantDetailDAO;
	}


	public void setLinkPaymentModeManager(
			LinkPaymentModeManager linkPaymentModeManager) {
		if (linkPaymentModeManager != null) {
			this.linkPaymentModeManager = linkPaymentModeManager;
		}
	}


	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager)
	{
		this.agentHierarchyManager = agentHierarchyManager;
	}

	public void setSalesHierarchyDAO(SalesHierarchyDAO salesHierarchyDAO)
	{
		this.salesHierarchyDAO = salesHierarchyDAO;
	}

	public void setAppUserPartnerGroupDAO(AppUserPartnerGroupDAO appUserPartnerGroupDAO)
	{
		this.appUserPartnerGroupDAO = appUserPartnerGroupDAO;
	}

	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager)
	{
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setKycDAO(KYCDAO kycDAO)
	{
		this.kycDAO = kycDAO;
	}

	public void setAllpayDelinkRelinkPaymentModeViewDAO(AllpayDelinkRelinkPaymentModeViewDAO allpayDelinkRelinkPaymentModeViewDAO)
	{
		this.allpayDelinkRelinkPaymentModeViewDAO = allpayDelinkRelinkPaymentModeViewDAO;
	}

	public void setSmAcctInfoListViewDAO(SMAcctInfoListViewDAO smAcctInfoListViewDAO)
	{
		this.smAcctInfoListViewDAO = smAcctInfoListViewDAO;
	}

	public void setBusinessTypeDAO(BusinessTypeDAO businessTypeDAO) {
		if (businessTypeDAO != null) {
			this.businessTypeDAO = businessTypeDAO;
		}
	}

	public void setCityDAO(CityDAO cityDAO) {
		if (cityDAO != null) {
			this.cityDAO = cityDAO;
		}
	}

	public void setProfessionDAO(ProfessionDAO professionDAO) {
		if (professionDAO != null) {
			this.professionDAO = professionDAO;
		}
	}

	public void setOccupationDAO(OccupationDAO occupationDAO) {
		if (occupationDAO != null) {
			this.occupationDAO = occupationDAO;
		}
	}

	public void setIvrRequestHandler(IvrRequestHandler ivrRequestHandler) {
		this.ivrRequestHandler = ivrRequestHandler;
	}

	public void setAppUserMobileHistoryDAO(AppUserMobileHistoryDAO appUserMobileHistoryDAO)
	{
		this.appUserMobileHistoryDAO = appUserMobileHistoryDAO;
	}

	public void setAccountInfoDao(AccountInfoDAO accountInfoDao) {
		this.accountInfoDao = accountInfoDao;
	}

	public void setGeoLocationDAO(GeoLocationDAO geoLocationDAO) {
		this.geoLocationDAO = geoLocationDAO;
	}
}