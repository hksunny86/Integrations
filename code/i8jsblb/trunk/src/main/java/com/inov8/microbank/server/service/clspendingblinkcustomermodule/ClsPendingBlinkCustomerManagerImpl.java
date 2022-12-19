package com.inov8.microbank.server.service.clspendingblinkcustomermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.integration.common.model.BlinkDefaultLimitModel;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.dao.addressmodule.AddressDAO;
import com.inov8.microbank.server.dao.addressmodule.CustomerAddressesDAO;
import com.inov8.microbank.server.dao.bankmodule.BankDAO;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerModelDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerPictureDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsPendingBlinkCustomerDAO;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.dao.account.AccountDAO;
import com.inov8.ola.server.dao.blinkcustomerlimit.BlinkCustomerDAO;
import com.inov8.ola.server.dao.limit.BlinkDefaultLimitDAO;
import com.inov8.ola.util.LimitTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.server.dao.mainmodule.AccountInfoDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.web.context.ContextLoader;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ClsPendingBlinkCustomerManagerImpl implements ClsPendingBlinkCustomerManager {
    private final static Log logger = LogFactory.getLog(ClsPendingBlinkCustomerManagerImpl.class);
    FinancialInstitution olaVeriflyFinancialInstitution;
    private MessageSource messageSource;
    private SmsSender smsSender;
    private ClsPendingBlinkCustomerDAO clsPendingBlinkCustomerDAO;
    private MfsAccountManager mfsAccountManager;
    private BaseWrapper baseWrapper = new BaseWrapperImpl();
    private AppUserDAO appUserDAO;
    private UserDeviceAccountsDAO userDeviceAccountsDAO;
    private AddressDAO addressDAO;
    private CustTransManager custTransManager;
    private CustomerAddressesDAO customerAddressesDAO;
    private SmartMoneyAccountDAO smartMoneyAccountDAO;
    private AccountInfoDAO accountInfoDao;
    private BankDAO bankDAO;
    private CustomerPictureDAO customerPictureDAO;
    private BlinkCustomerModelDAO blinkCustomerModelDAO;
    private ESBAdapter esbAdapter;
    private AccountDAO accountDAO;
    private BlinkCustomerDAO blinkCustomerDAO;
    private BlinkDefaultLimitDAO blinkDefaultLimitDAO;

    @Override
    public List<ClsPendingBlinkCustomerModel> loadAllClsPendingBlinkCustomer() throws FrameworkCheckedException {
        return clsPendingBlinkCustomerDAO.loadAllPendingAccount();
    }

    @Override
    public void makePendingAccountOpeningCommand(ClsPendingBlinkCustomerModel clsPendingBlinkCustomerModel) throws FrameworkCheckedException {

        MfsAccountModel mfsAccountModel = new MfsAccountModel();
        Date nowDate = new Date();
        SmartMoneyAccountModel smartMoneyAccountModel = null;

        mfsAccountModel.setCustomerId(clsPendingBlinkCustomerModel.getCustomerId().toString());
        mfsAccountModel.setMobileNo(clsPendingBlinkCustomerModel.getMobileNo());
        mfsAccountModel.setNic(clsPendingBlinkCustomerModel.getCnic());
        mfsAccountModel.setName(clsPendingBlinkCustomerModel.getConsumerName());
        mfsAccountModel.setFatherHusbandName(clsPendingBlinkCustomerModel.getFatherHusbandName());
        mfsAccountModel.setGender(clsPendingBlinkCustomerModel.getGender());
        mfsAccountModel.setCnicIssuanceDate(clsPendingBlinkCustomerModel.getCnicIssuanceDate());
        mfsAccountModel.setDob(clsPendingBlinkCustomerModel.getDob());
        mfsAccountModel.setBirthPlace(clsPendingBlinkCustomerModel.getBirthPlace());
        mfsAccountModel.setMotherMaidenName(clsPendingBlinkCustomerModel.getMotherMaidenName());
        mfsAccountModel.setEmail(clsPendingBlinkCustomerModel.getEmailAddress());
        mfsAccountModel.setPresentAddress(clsPendingBlinkCustomerModel.getMailingAddress());
        mfsAccountModel.setPermanentAddress(clsPendingBlinkCustomerModel.getPermanentAddress());
        mfsAccountModel.setAccountPurposeName(clsPendingBlinkCustomerModel.getPurposeOfAccount());
        mfsAccountModel.setSourceOfIncome(clsPendingBlinkCustomerModel.getSourceOfIncome());
        mfsAccountModel.setExpectedMonthlyTurnOver(clsPendingBlinkCustomerModel.getExpectedMonthlyTurnOver());
        mfsAccountModel.setNokName(clsPendingBlinkCustomerModel.getNextOfKin());
        mfsAccountModel.setLongitude(clsPendingBlinkCustomerModel.getLongitude());
        mfsAccountModel.setLatitude(clsPendingBlinkCustomerModel.getLatitude());
        mfsAccountModel.setDualNationality(clsPendingBlinkCustomerModel.getDualNationality());
        mfsAccountModel.setUsCitizen(clsPendingBlinkCustomerModel.getUsCitizen());
        mfsAccountModel.setCreatedBy(clsPendingBlinkCustomerModel.getCreatedBy().toString());
        mfsAccountModel.setCreatedOn(clsPendingBlinkCustomerModel.getCreatedOn());
        mfsAccountModel.setCustomerAccountTypeId(clsPendingBlinkCustomerModel.getCustomerAccountTypeId());
        mfsAccountModel.setAppUserId(clsPendingBlinkCustomerModel.getAppUserId());
        baseWrapper.putObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY, mfsAccountModel);

        if (clsPendingBlinkCustomerModel.getCaseStatus().equals("True Match-Compliance")) {
            mfsAccountManager.accountBlock(baseWrapper);
        } else if (clsPendingBlinkCustomerModel.getCaseStatus().equalsIgnoreCase("No Matches") || clsPendingBlinkCustomerModel.getCaseStatus().equalsIgnoreCase("Passed By Rule") || clsPendingBlinkCustomerModel.getCaseStatus().equalsIgnoreCase("False Positive Match") || clsPendingBlinkCustomerModel.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-False Positive|Private-False Positive")
                || clsPendingBlinkCustomerModel.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-False Positive") ||
                clsPendingBlinkCustomerModel.getCaseStatus().equalsIgnoreCase("GWL-Passed by Rules|PEP/EDD-Passed by Rules|Private-Passed by Rules")) {
            AppUserModel appUserModel = new AppUserModel();
            appUserModel = this.appUserDAO.findByPrimaryKey(mfsAccountModel.getAppUserId());
            if (smartMoneyAccountModel == null) {
                SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
                sma.setCustomerId(appUserModel.getCustomerId());
                sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                smartMoneyAccountModel = this.getSmartMoneyAccountByExample(sma);
            }
            mfsAccountModel.setRegistrationStateId(appUserModel.getRegistrationStateId());
            String oldFirstName = appUserModel.getFirstName();
            String oldLastName = appUserModel.getLastName();
            String[] nameArray = mfsAccountModel.getName().split(" ");
            appUserModel.setFirstName(nameArray[0]);

            if (nameArray.length > 1) {
                appUserModel.setLastName(mfsAccountModel.getName().substring(appUserModel.getFirstName().length() + 1));
            } else {
                appUserModel.setLastName(nameArray[0]);
            }
            Long oldRegistrationStateId = appUserModel.getRegistrationStateId();
            UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
            userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
            CustomList<UserDeviceAccountsModel> userDeviceAccountsModelList = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel);
            if (userDeviceAccountsModelList != null) {
                userDeviceAccountsModel = userDeviceAccountsModelList.getResultsetList().get(0);
                if (mfsAccountModel.getRegistrationStateId() != null) {
                    if (mfsAccountModel.getRegistrationStateId().equals(3L)) {
                        userDeviceAccountsModel.setAccountEnabled(Boolean.TRUE);
                    }
                }

                this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
            }

            Long oldAccountTypeId = null;

            boolean OLA_ACCOUNT_INFO_CHANGE_FLAG = false;

            /**
             * Checking if mobile Number is unique
             */
            boolean MOBILE_NUM_CHANGE_FLAG = false;
            if (!appUserModel.getMobileNo().equals(mfsAccountModel.getMobileNo())) {
                MOBILE_NUM_CHANGE_FLAG = true;
            }

            /**
             * Checking if CNIC is unique
             */
            boolean NIC_CHANGE_FLAG = false;
            String withoutDashesNIC = (mfsAccountModel.getNic() != null) ? mfsAccountModel.getNic().replace("-", "") : "";
            if (!appUserModel.getNic().equals(withoutDashesNIC)) {
                NIC_CHANGE_FLAG = true;
            }

            if (MOBILE_NUM_CHANGE_FLAG) {
                if (!this.isMobileNumUnique(mfsAccountModel.getMobileNo(), mfsAccountModel.getAppUserId())) {
                    throw new FrameworkCheckedException("MobileNumUniqueException");
                }
            }

            if (NIC_CHANGE_FLAG) {
                if (!this.isNICUnique(mfsAccountModel.getNic(), mfsAccountModel.getAppUserId())) {
                    throw new FrameworkCheckedException("NICUniqueException");
                }
            }


            /**
             * Populating the First/Last Name
             */
            if (null != mfsAccountModel.getFirstName())
                appUserModel.setFirstName(mfsAccountModel.getFirstName());

            if (null != mfsAccountModel.getLastName())
                appUserModel.setLastName(mfsAccountModel.getLastName());

            if (null != mfsAccountModel.getMotherMaidenName() || null == mfsAccountModel.getMotherMaidenName())
                appUserModel.setMotherMaidenName(mfsAccountModel.getMotherMaidenName());

            if (null != mfsAccountModel.getCnicIssuanceDate() || null == mfsAccountModel.getCnicIssuanceDate())
                appUserModel.setCnicIssuanceDate(mfsAccountModel.getCnicIssuanceDate());

            if (!oldFirstName.equals(appUserModel.getFirstName()) || !oldLastName.equals(appUserModel.getLastName())) {
                OLA_ACCOUNT_INFO_CHANGE_FLAG = true;
            }
            CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
            if (customerModel != null) {
                customerModel.getCustomerId();


                oldAccountTypeId = customerModel.getCustomerAccountTypeId();

                customerModel.setCustomerAccountTypeId(mfsAccountModel.getCustomerAccountTypeId());

                if (!oldAccountTypeId.equals(customerModel.getCustomerAccountTypeId())) {
                    OLA_ACCOUNT_INFO_CHANGE_FLAG = true;
                }

                boolean CNIC_SEEN_FLAG = false;
                if (customerModel.getIsCnicSeen().equals(mfsAccountModel.isCnicSeen())) {
                    CNIC_SEEN_FLAG = true;
                }

                customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                customerModel.setUpdatedOn(new Date());
                customerModel.setName(mfsAccountModel.getName());
                customerModel.setCustomerAccountTypeId(mfsAccountModel.getCustomerAccountTypeId());
                customerModel.setMobileNo(mfsAccountModel.getMobileNo());
                customerModel.setGender(mfsAccountModel.getGender());
                customerModel.setFatherHusbandName(mfsAccountModel.getFatherHusbandName());
                customerModel.setNokName(mfsAccountModel.getNokName());
                customerModel.setNokRelationship(mfsAccountModel.getNokRelationship());
                customerModel.setNokMobile(mfsAccountModel.getNokMobile());
                customerModel.setNokNic(mfsAccountModel.getNokNic());
                customerModel.setBirthPlace(mfsAccountModel.getBirthPlace());
                customerModel.setEmail(mfsAccountModel.getEmail());
                customerModel.setRegStateComments(mfsAccountModel.getRegStateComments());
                customerModel.setComments(mfsAccountModel.getComments());
                customerModel.setLatitude(mfsAccountModel.getLatitude());
                customerModel.setLongitude(mfsAccountModel.getLongitude());
                customerModel.setMonthlyTurnOver(mfsAccountModel.getExpectedMonthlyTurnOver());
                customerModel.setClsResponseCode(mfsAccountModel.getClsResponseCode());
                customerModel.setDualNationality(mfsAccountModel.getDualNationality());
                customerModel.setUsCitizen(mfsAccountModel.getUsCitizen());

                /**
                 * Saving the CustomerModel
                 */
                baseWrapper = new BaseWrapperImpl();
                baseWrapper.setBasePersistableModel(customerModel);
                baseWrapper = this.custTransManager.saveOrUpdate(baseWrapper);
            }


            BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
            blinkCustomerModel.setCustomerId(customerModel.getCustomerId());
            blinkCustomerModel = this.getCommonCommandManager().loadBlinkCustomerByMobileAndAccUpdate(mfsAccountModel.getMobileNo(), 1L);
            if (blinkCustomerModel.getAccUpdate() == 1) {
                blinkCustomerModel.setAccUpdate(0L);
                blinkCustomerModel.setClsResponseCode("Blink Approved");
                blinkCustomerModel.setCustomerAccountTypeId(53L);
                blinkCustomerModel.setUpdatedOn(new Date());
                blinkCustomerModelDAO.saveOrUpdate(blinkCustomerModel);
            }
            ClsPendingBlinkCustomerModel clsPendingBlinkCustomerModel1 = new ClsPendingBlinkCustomerModel();
            clsPendingBlinkCustomerModel1.setClsBlinkCustomerId(clsPendingBlinkCustomerModel.getClsBlinkCustomerId());
            clsPendingBlinkCustomerModel1 = clsPendingBlinkCustomerDAO.findByPrimaryKey(clsPendingBlinkCustomerModel1.getClsBlinkCustomerId());
            if (clsPendingBlinkCustomerModel1.getIsCompleted().equals("0")) {
                clsPendingBlinkCustomerModel1.setIsCompleted("1");
                clsPendingBlinkCustomerModel1.setClsComments("Blink Approved");
                clsPendingBlinkCustomerModel.setUpdatedOn(new Date());
                clsPendingBlinkCustomerDAO.saveOrUpdate(clsPendingBlinkCustomerModel1);
            }


            // Updating Address Fields
            boolean isNokAddressAlreadySaved = false;
            try {
                // Populating Address Fields
                Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
                if (customerAddresses != null && customerAddresses.size() > 0) {
                    for (CustomerAddressesModel custAdd : customerAddresses) {
                        AddressModel addressModel = custAdd.getAddressIdAddressModel();
                        if (custAdd.getAddressTypeId() == 1) {
                            if (mfsAccountModel.getPresentAddress() != null) {
                                addressModel.setFullAddress(mfsAccountModel.getPresentAddress());
                                if (mfsAccountModel.getCity() != null) {
                                    addressModel.setCityId(Long.parseLong(mfsAccountModel.getCity()));
                                }

                            }
                            this.addressDAO.saveOrUpdate(addressModel);
                        } else if (custAdd.getAddressTypeId() == 4) {
                            isNokAddressAlreadySaved = true;
                            if (mfsAccountModel.getNokMailingAdd() == null) {
                                addressModel.setFullAddress("");
                            } else {
                                addressModel.setFullAddress(mfsAccountModel.getNokMailingAdd());
                            }


                            this.addressDAO.saveOrUpdate(addressModel);
                        }
                    }
                } else//When LO/1 account created via Mobile App is updated on portal, we have to add addresses in DB
                {
                    AddressModel presentHomeAddress = new AddressModel();
                    presentHomeAddress.setFullAddress(mfsAccountModel.getPresentAddress());
                    presentHomeAddress.setCityId(Long.parseLong(mfsAccountModel.getCity()));

                    AddressModel nokHomeAddress = new AddressModel();
                    if (mfsAccountModel.getNokMailingAdd() != null) {
                        nokHomeAddress.setFullAddress(mfsAccountModel.getNokMailingAdd());
                    }

                    /**
                     * Saving AddressModels
                     */
                    presentHomeAddress = this.addressDAO.saveOrUpdate(presentHomeAddress);
                    nokHomeAddress = this.addressDAO.saveOrUpdate(nokHomeAddress);

                    /**
                     * Saving CustomerAddressesModels
                     */
                    CustomerAddressesModel presentCustomerAddressesModel = new CustomerAddressesModel();
                    presentCustomerAddressesModel.setAddressId(presentHomeAddress.getAddressId());
                    presentCustomerAddressesModel.setAddressTypeId(1L);
                    presentCustomerAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                    presentCustomerAddressesModel.setCustomerId(customerModel.getCustomerId());

                    CustomerAddressesModel nokCustomerAddressesModel = new CustomerAddressesModel();
                    nokCustomerAddressesModel.setAddressId(nokHomeAddress.getAddressId());
                    nokCustomerAddressesModel.setAddressTypeId(4L);
                    nokCustomerAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                    nokCustomerAddressesModel.setCustomerId(customerModel.getCustomerId());

                    presentCustomerAddressesModel = this.customerAddressesDAO.saveOrUpdate(presentCustomerAddressesModel);
                    nokCustomerAddressesModel = this.customerAddressesDAO.saveOrUpdate(nokCustomerAddressesModel);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new FrameworkCheckedException(e.getMessage(), e);
            }

            //When NOK Address not saved already
            if (!isNokAddressAlreadySaved) {
                try {
                    AddressModel nokAddress = new AddressModel();
                    if (mfsAccountModel.getNokMailingAdd() != null) {
                        nokAddress.setFullAddress(mfsAccountModel.getNokMailingAdd());
                    }

                    nokAddress = this.addressDAO.saveOrUpdate(nokAddress);       //saving NOK Address

                    CustomerAddressesModel nokAddressesModel = new CustomerAddressesModel();
                    nokAddressesModel.setAddressId(nokAddress.getAddressId());
                    nokAddressesModel.setAddressTypeId(4L);
                    nokAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                    nokAddressesModel.setCustomerId(customerModel.getCustomerId());

                    nokAddressesModel = this.customerAddressesDAO.saveOrUpdate(nokAddressesModel);    //saving customer address
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new FrameworkCheckedException(e.getMessage(), e);
                }
            }
            appUserModel.setNic(mfsAccountModel.getNic());
            appUserModel.setDob(mfsAccountModel.getDob());
            appUserModel.setMobileNo(mfsAccountModel.getMobileNo());
            nameArray = mfsAccountModel.getName().split(" ");
            appUserModel.setFirstName(nameArray[0]);
            if (nameArray.length > 1) {
                appUserModel.setLastName(mfsAccountModel.getName().substring(appUserModel.getFirstName().length() + 1));
            } else {
                appUserModel.setLastName(nameArray[0]);
            }
            appUserModel.setRegistrationStateId(mfsAccountModel.getRegistrationStateId());
            appUserModel.setMobileTypeId(1L);
            appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            appUserModel.setUpdatedOn(nowDate);

            if (mfsAccountModel.getClosedBy() != null) {
                appUserModel.setClosedByAppUserModel(this.appUserDAO.findByPrimaryKey(Long.valueOf(mfsAccountModel.getClosedBy())));
            }
            if (mfsAccountModel.getSettledBy() != null) {
                appUserModel.setSettledByAppUserModel(this.appUserDAO.findByPrimaryKey(Long.valueOf(mfsAccountModel.getSettledBy())));
            }
            appUserModel.setSettledOn(appUserModel.getSettledOn());
            appUserModel.setSettlementComments(appUserModel.getSettlementComments());

            if (mfsAccountModel.getRegistrationStateId() != null) {
                if (mfsAccountModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DECLINE)) {
                    appUserModel.setAccountStateId(1L);
                    smartMoneyAccountModel.setRegistrationStateId(RegistrationStateConstantsInterface.DECLINE);
                } else if (mfsAccountModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.REJECTED)) {
                    appUserModel.setAccountStateId(1L);
                    smartMoneyAccountModel.setRegistrationStateId(RegistrationStateConstantsInterface.DECLINE);
                } else if (mfsAccountModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DISCREPANT)) {
                    appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_HOT);
                    smartMoneyAccountModel.setRegistrationStateId(RegistrationStateConstantsInterface.DISCREPANT);
                } else if (mfsAccountModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.RQST_RCVD)) {
                    appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);
                    smartMoneyAccountModel.setRegistrationStateId(RegistrationStateConstantsInterface.RQST_RCVD);
                } else if (mfsAccountModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.VERIFIED)) {
                    appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
                    smartMoneyAccountModel.setRegistrationStateId(RegistrationStateConstantsInterface.VERIFIED);
                }
            }

            smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);
            appUserModel = this.appUserDAO.saveOrUpdate(appUserModel);
            //*** Start - updating Customer Account type in OLA Account

            if (OLA_ACCOUNT_INFO_CHANGE_FLAG) {
                OLAVO olaVo = new OLAVO();
                olaVo.setFirstName(appUserModel.getFirstName());
                olaVo.setLastName(appUserModel.getLastName());
                olaVo.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
                olaVo.setCnic(appUserModel.getNic());
                olaVo.setMobileNumber(appUserModel.getMobileNo());

                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                BankModel bankModel = getOlaBankMadal();
                sWrapper.setOlavo(olaVo);
                sWrapper.setBankId(bankModel.getBankId());

                try {
                    sWrapper = olaVeriflyFinancialInstitution.changeAccountDetails(sWrapper);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
                }

                if ("07".equals(olaVo.getResponseCode())) {
                    throw new FrameworkCheckedException("CNIC already exisits in the OLA accounts");
                }

                AccountInfoModel accountInfoModel = new AccountInfoModel();
                accountInfoModel.setCustomerId(customerModel.getCustomerId());
                CustomList<AccountInfoModel> customList = accountInfoDao.findByExample(accountInfoModel);
                if (customList != null && CollectionUtils.isNotEmpty(customList.getResultsetList())) {
                    accountInfoModel = customList.getResultsetList().get(0);
                    accountInfoModel.setFirstName(appUserModel.getFirstName());
                    accountInfoModel.setLastName(appUserModel.getLastName());
                    accountInfoDao.saveOrUpdate(accountInfoModel);
                } else {
                    logger.info("No record found in AccountInfoModel to update firstName and lastName");
                }
            }
            //*** End - updating Customer Account type in OLA Account

            String discripentNo = "";
            if (mfsAccountModel.getCustomerPicDiscrepant() != null && mfsAccountModel.getCnicFrontPicDiscrepant() != null) {
                if (mfsAccountModel.getCnicFrontPicDiscrepant().equals(true) && mfsAccountModel.getCustomerPicDiscrepant().equals(true)) {
                    discripentNo = "01";
                } else if (mfsAccountModel.getCnicFrontPicDiscrepant().equals(true)) {
                    discripentNo = "02";
                } else if (mfsAccountModel.getCustomerPicDiscrepant().equals(true)) {
                    discripentNo = "03";
                }
            }

            BlinkCustomerPictureModel customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());
            CustomerPictureModel customerPictureModel1 = new CustomerPictureModel();
            if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                customerPictureModel1.setPicture(customerPictureModel.getPicture());
                customerPictureModel1.setPictureTypeId(customerPictureModel.getPictureTypeId());
                customerPictureModel1.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel1.setCreatedOn(new Date());
                customerPictureModel1.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel1.setUpdatedOn(new Date());
                customerPictureModel1.setCustomerId(Long.valueOf(mfsAccountModel.getCustomerId()));
                customerPictureDAO.saveOrUpdate(customerPictureModel1);
            }

            customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());
            customerPictureModel1 = new CustomerPictureModel();
            if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                customerPictureModel1.setPicture(customerPictureModel.getPicture());
                customerPictureModel1.setPictureTypeId(customerPictureModel.getPictureTypeId());
                customerPictureModel1.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel1.setCreatedOn(new Date());
                customerPictureModel1.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel1.setUpdatedOn(new Date());
                customerPictureModel1.setCustomerId(Long.valueOf(mfsAccountModel.getCustomerId()));
                customerPictureDAO.saveOrUpdate(customerPictureModel1);
            }
            customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());
            customerPictureModel1 = new CustomerPictureModel();
            if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                customerPictureModel1.setPicture(customerPictureModel.getPicture());
                customerPictureModel1.setPictureTypeId(customerPictureModel.getPictureTypeId());
                customerPictureModel1.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel1.setCreatedOn(new Date());
                customerPictureModel1.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel1.setUpdatedOn(new Date());
                customerPictureModel1.setCustomerId(Long.valueOf(mfsAccountModel.getCustomerId()));
                customerPictureDAO.saveOrUpdate(customerPictureModel1);
            }
            customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT, customerModel.getCustomerId().longValue());
            customerPictureModel1 = new CustomerPictureModel();
            if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                customerPictureModel1.setPicture(customerPictureModel.getPicture());
                customerPictureModel1.setPictureTypeId(customerPictureModel.getPictureTypeId());
                customerPictureModel1.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel1.setCreatedOn(new Date());
                customerPictureModel1.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel1.setUpdatedOn(new Date());
                customerPictureModel1.setCustomerId(Long.valueOf(mfsAccountModel.getCustomerId()));
                customerPictureDAO.saveOrUpdate(customerPictureModel1);
            }

            customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, customerModel.getCustomerId().longValue());
            customerPictureModel1 = new CustomerPictureModel();
            if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                customerPictureModel1.setPicture(customerPictureModel.getPicture());
                customerPictureModel1.setPictureTypeId(customerPictureModel.getPictureTypeId());
                customerPictureModel1.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel1.setCreatedOn(new Date());
                customerPictureModel1.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel1.setUpdatedOn(new Date());
                customerPictureModel1.setCustomerId(Long.valueOf(mfsAccountModel.getCustomerId()));
                customerPictureDAO.saveOrUpdate(customerPictureModel1);
            }

            customerPictureModel = this.mfsAccountManager.getBlinkCustomerPictureByTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT, customerModel.getCustomerId().longValue());
            customerPictureModel1 = new CustomerPictureModel();
            if (customerPictureModel != null && customerPictureModel.getPicture() != null) {
                customerPictureModel1.setPicture(customerPictureModel.getPicture());
                customerPictureModel1.setPictureTypeId(customerPictureModel.getPictureTypeId());
                customerPictureModel1.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel1.setCreatedOn(new Date());
                customerPictureModel1.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel1.setUpdatedOn(new Date());
                customerPictureModel1.setCustomerId(Long.valueOf(mfsAccountModel.getCustomerId()));
                customerPictureDAO.saveOrUpdate(customerPictureModel1);
            }

            String accountOpeningMethod = "";
            if (customerModel.getAccountMethodId() != null && !"".equals(customerModel.getAccountMethodId())) {
                if (customerModel.getAccountMethodId().equals(AccountOpeningMethodConstantsInterface.SELF_REGISTERATION)) {
                    accountOpeningMethod = "Consumer App";
                } else if (customerModel.getAccountMethodId().equals(AccountOpeningMethodConstantsInterface.FONEPAY)) {
                    accountOpeningMethod = "FonePay app";
                }
            }

            SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
            I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForUpdateAccountToBlink(I8SBConstants.RequestType_L2_ACCOUNT_UPGRADE_VALIDATION);
            I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
            requestVO.setCNIC(mfsAccountModel.getNic());

            requestVO.setAddress("Approved");
            requestVO.setPurposeOfAccount("Approved");
            requestVO.setMobileNumber("Approved");
            requestVO.setFatherName("Approved");
            requestVO.setEmail("Approved");
            requestVO.setMailingAddress("Approved");
            requestVO.setSourceOfIncomePic("Approved");
            requestVO.setExpectedMonthlyTurnOver("Approved");
            requestVO.setProofOfProfession("Approved");
            requestVO.setCnicFrontPic("Approved");
            requestVO.setCnicBackPic("Approved");
            requestVO.setCustomerPic("Approved");
            requestVO.setSignaturePic("Approved");
            requestVO.setSourceOfIncome("Approved");
            if (mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.APPROVED)) {
                requestVO.setStatus("Approved");
            } else if (mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.DISCREPANT)) {
                requestVO.setStatus("Discripent");
            } else if (mfsAccountModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.REJECTED)) {
                requestVO.setStatus("Rejected");
            }
            i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
            try {
                i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
                requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();
                responseVO = requestVO.getI8SBSwitchControllerResponseVO();

                if (responseVO.getResponseCode().equals("I8SB-200")) {
                    logger.info("Successfully your Discrepant call to I8SB at " + new Date());
                }
            } catch (Exception ex) {
                throw new FrameworkCheckedException("Error during call to I8SB ::" + responseVO.getDescription());
            }
            /*added by atif hussain*/
            String transactionIDForSAF = "";
            if (mfsAccountModel.getRegistrationStateId() != null) {
                Long newRegistrationStateId = mfsAccountModel.getRegistrationStateId();
                if (customerModel.getAccountMethodId() != null && (customerModel.getAccountMethodId().equals(AccountOpeningMethodConstantsInterface.FONEPAY) ||
                        customerModel.getAccountMethodId().equals(AccountOpeningMethodConstantsInterface.SELF_REGISTERATION))) {
                    sendRegistationStateNotificationL0L1(oldRegistrationStateId, newRegistrationStateId, appUserModel.getMobileNo(), accountOpeningMethod, discripentNo);
                } else {
                    sendRegistationStateNotification(oldRegistrationStateId, newRegistrationStateId, appUserModel.getMobileNo());
                }
            }


            Long accountId = accountDAO.getAccountIdByCnic(mfsAccountModel.getNic(), mfsAccountModel.getCustomerAccountTypeId());
            BlinkCustomerLimitModel blinkCustomerLimitModel = new BlinkCustomerLimitModel();
            BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.DEBIT, LimitTypeConstants.DAILY);

            if (blinkCustomerLimitModels != null) {
                blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getDebitLimitDaily()));
                blinkCustomerLimitModels.setUpdatedOn(new Date());
                blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

            } else {
                BlinkDefaultLimitModel blinkDefaultLimitModel = blinkDefaultLimitDAO.getLimitByTransactionType(mfsAccountModel.getCustomerAccountTypeId(), LimitTypeConstants.DAILY, TransactionTypeConstants.DEBIT);

                blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.DEBIT);
                blinkCustomerLimitModel.setLimitType(LimitTypeConstants.DAILY);
                blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setCreatedOn(nowDate);
                blinkCustomerLimitModel.setCustomerAccTypeId(53L);
                blinkCustomerLimitModel.setMaximum(blinkDefaultLimitModel.getMaximum().longValue());
                blinkCustomerLimitModel.setMinimum(blinkDefaultLimitModel.getMinimum().longValue());
                blinkCustomerLimitModel.setIsApplicable(1l);
                blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setAccountId(accountId);
                blinkCustomerLimitModel.setUpdatedOn(nowDate);
                blinkCustomerDAO.insertData(blinkCustomerLimitModel);
            }

            BlinkCustomerLimitModel blinkCustomerLimitModelDCR = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.CREDIT, LimitTypeConstants.DAILY);
            if (blinkCustomerLimitModelDCR != null) {
                blinkCustomerLimitModelDCR.setMaximum(Long.valueOf(mfsAccountModel.getCreditLimitDaily()));
                blinkCustomerLimitModelDCR.setUpdatedOn(new Date());
                blinkCustomerLimitModelDCR.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModelDCR.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModelDCR);

            } else {
                BlinkDefaultLimitModel blinkDefaultLimitModel = blinkDefaultLimitDAO.getLimitByTransactionType(mfsAccountModel.getCustomerAccountTypeId(), LimitTypeConstants.DAILY, TransactionTypeConstants.CREDIT);
                blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.CREDIT);
                blinkCustomerLimitModel.setLimitType(LimitTypeConstants.DAILY);
                blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setCreatedOn(nowDate);
                blinkCustomerLimitModel.setCustomerAccTypeId(53L);
                blinkCustomerLimitModel.setMaximum(blinkDefaultLimitModel.getMaximum().longValue());
                blinkCustomerLimitModel.setMinimum(1l);
                blinkCustomerLimitModel.setIsApplicable(1l);
                blinkCustomerLimitModel.setAccountId(accountId);
                blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setUpdatedOn(nowDate);
                blinkCustomerDAO.insertData(blinkCustomerLimitModel);
            }
            BlinkCustomerLimitModel blinkCustomerLimitModelsMDr = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.DEBIT, LimitTypeConstants.MONTHLY);
            if (blinkCustomerLimitModelsMDr != null) {
                blinkCustomerLimitModelsMDr.setMaximum(Long.valueOf(mfsAccountModel.getDebitLimitMonthly()));
                blinkCustomerLimitModelsMDr.setUpdatedOn(new Date());
                blinkCustomerLimitModelsMDr.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModelsMDr.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModelsMDr);

            } else {
                BlinkDefaultLimitModel blinkDefaultLimitModel = blinkDefaultLimitDAO.getLimitByTransactionType(mfsAccountModel.getCustomerAccountTypeId(), LimitTypeConstants.MONTHLY, TransactionTypeConstants.DEBIT);
                blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.DEBIT);
                blinkCustomerLimitModel.setLimitType(LimitTypeConstants.MONTHLY);
                blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setCreatedOn(nowDate);
                blinkCustomerLimitModel.setCustomerAccTypeId(53L);
                blinkCustomerLimitModel.setMaximum(blinkDefaultLimitModel.getMaximum().longValue());
                blinkCustomerLimitModel.setMinimum(blinkDefaultLimitModel.getMinimum().longValue());
                blinkCustomerLimitModel.setIsApplicable(1l);
                blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setUpdatedOn(nowDate);
                blinkCustomerLimitModel.setAccountId(accountId);
                blinkCustomerDAO.insertData(blinkCustomerLimitModel);
            }


            BlinkCustomerLimitModel blinkCustomerLimitModelsMCr = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.CREDIT, LimitTypeConstants.MONTHLY);
            if (blinkCustomerLimitModelsMCr != null) {
                blinkCustomerLimitModelsMCr.setMaximum(Long.valueOf(mfsAccountModel.getCreditLimitMonthly()));
                blinkCustomerLimitModelsMCr.setUpdatedOn(new Date());
                blinkCustomerLimitModelsMCr.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModelsMCr.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModelsMCr);

            } else {
                BlinkDefaultLimitModel blinkDefaultLimitModel = blinkDefaultLimitDAO.getLimitByTransactionType(mfsAccountModel.getCustomerAccountTypeId(), LimitTypeConstants.MONTHLY, TransactionTypeConstants.CREDIT);
                blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.CREDIT);
                blinkCustomerLimitModel.setLimitType(LimitTypeConstants.MONTHLY);
                blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setCreatedOn(nowDate);
                blinkCustomerLimitModel.setCustomerAccTypeId(53L);
                blinkCustomerLimitModel.setMaximum(blinkDefaultLimitModel.getMaximum().longValue());
                blinkCustomerLimitModel.setMinimum(blinkDefaultLimitModel.getMinimum().longValue());
                blinkCustomerLimitModel.setIsApplicable(1l);
                blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setUpdatedOn(nowDate);
                blinkCustomerLimitModel.setAccountId(accountId);
                blinkCustomerDAO.insertData(blinkCustomerLimitModel);
            }

            BlinkCustomerLimitModel blinkCustomerLimitModelsYDr = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.DEBIT, LimitTypeConstants.YEARLY);
            if (blinkCustomerLimitModelsYDr != null) {
                blinkCustomerLimitModelsYDr.setMaximum(Long.valueOf(mfsAccountModel.getDebitLimitYearly()));
                blinkCustomerLimitModelsYDr.setUpdatedOn(new Date());
                blinkCustomerLimitModelsYDr.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModelsYDr.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModelsYDr);

            } else {
                BlinkDefaultLimitModel blinkDefaultLimitModel = blinkDefaultLimitDAO.getLimitByTransactionType(mfsAccountModel.getCustomerAccountTypeId(), LimitTypeConstants.YEARLY, TransactionTypeConstants.DEBIT);
                blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.DEBIT);
                blinkCustomerLimitModel.setLimitType(LimitTypeConstants.YEARLY);
                blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setCreatedOn(nowDate);
                blinkCustomerLimitModel.setCustomerAccTypeId(53L);
                blinkCustomerLimitModel.setMaximum(blinkDefaultLimitModel.getMaximum().longValue());
                blinkCustomerLimitModel.setMinimum(1l);
                blinkCustomerLimitModel.setIsApplicable(1l);
                blinkCustomerLimitModel.setAccountId(accountId);
                blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setUpdatedOn(nowDate);
                blinkCustomerDAO.insertData(blinkCustomerLimitModel);

            }


            BlinkCustomerLimitModel blinkCustomerLimitModelsYCr = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.CREDIT, LimitTypeConstants.YEARLY);
            if (blinkCustomerLimitModelsYCr != null) {
                blinkCustomerLimitModelsYCr.setMaximum(Long.valueOf(mfsAccountModel.getCreditLimitYearly()));
                blinkCustomerLimitModelsYCr.setUpdatedOn(new Date());
                blinkCustomerLimitModelsYCr.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModelsYCr.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModelsYCr);

            } else {
                BlinkDefaultLimitModel blinkDefaultLimitModel = blinkDefaultLimitDAO.getLimitByTransactionType(mfsAccountModel.getCustomerAccountTypeId(), LimitTypeConstants.YEARLY, TransactionTypeConstants.CREDIT);
                blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.CREDIT);
                blinkCustomerLimitModel.setLimitType(LimitTypeConstants.YEARLY);
                blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setCreatedOn(nowDate);
                blinkCustomerLimitModel.setCustomerAccTypeId(53L);
                blinkCustomerLimitModel.setMaximum(blinkDefaultLimitModel.getMaximum().longValue());
                blinkCustomerLimitModel.setMinimum(1l);
                blinkCustomerLimitModel.setIsApplicable(1l);
                blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setUpdatedOn(nowDate);
                blinkCustomerLimitModel.setAccountId(accountId);
                blinkCustomerDAO.insertData(blinkCustomerLimitModel);
            }

            BlinkCustomerLimitModel blinkCustomerLimitModelMax = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.CREDIT, LimitTypeConstants.MAXIMUM);
            if (blinkCustomerLimitModelMax != null) {
                blinkCustomerLimitModelMax.setMaximum(Long.valueOf(mfsAccountModel.getMaximumCreditLimit()));
                blinkCustomerLimitModelMax.setUpdatedOn(new Date());
                blinkCustomerLimitModelMax.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModelMax.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModelMax);

            } else {
                BlinkDefaultLimitModel blinkDefaultLimitModel = blinkDefaultLimitDAO.getLimitByTransactionType(mfsAccountModel.getCustomerAccountTypeId(), LimitTypeConstants.MAXIMUM, TransactionTypeConstants.CREDIT);

                blinkCustomerLimitModel.setTransactionType(2l);
                blinkCustomerLimitModel.setLimitType(LimitTypeConstants.MAXIMUM);
                blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setCreatedOn(nowDate);
                blinkCustomerLimitModel.setCustomerAccTypeId(53L);
                blinkCustomerLimitModel.setMaximum(blinkDefaultLimitModel.getMaximum().longValue());
                blinkCustomerLimitModel.setMinimum(1l);
                blinkCustomerLimitModel.setIsApplicable(1l);
                blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                blinkCustomerLimitModel.setUpdatedOn(nowDate);
                blinkCustomerLimitModel.setAccountId(accountId);
                blinkCustomerDAO.insertData(blinkCustomerLimitModel);
            }


        }
    }

    @Override
    public List<ClsPendingBlinkCustomerModel> searchBlinkAccountCLSData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        CustomList<ClsPendingBlinkCustomerModel> customList = clsPendingBlinkCustomerDAO.findByExample((ClsPendingBlinkCustomerModel) searchBaseWrapper.getBasePersistableModel(),
                searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),
                searchBaseWrapper.getDateRangeHolderModel());

        List<ClsPendingBlinkCustomerModel> list = customList.getResultsetList();
        return list;
    }

    private void sendRegistationStateNotification(Long oldRegistrationStateId, Long newRegistrationStateId, String mobileNo) throws FrameworkCheckedException {
        if (newRegistrationStateId != null && oldRegistrationStateId != null
                && oldRegistrationStateId.longValue() != newRegistrationStateId
                && newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.VERIFIED
                || newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.DISCREPANT
                || newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.DECLINE
                || newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.REJECTED) {
            Object[] args = {RegistrationStateConstantsInterface.REG_STATE_MAP.get(newRegistrationStateId)};

            String messageString = null;

            if (newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.DISCREPANT.longValue()) {
                messageString = MessageUtil.getMessage("smsCommand.dct_sms12discrepant", args);
            }
            if (newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.VERIFIED.longValue()) {
                messageString = MessageUtil.getMessage("smsCommand.dct_sms12activate", args);
            } else if (newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.DECLINE.longValue() ||
                    newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.REJECTED.longValue()) {
                messageString = MessageUtil.getMessage("smsCommand.dct_sms12", args);
            }

            SmsMessage smsMessage = new SmsMessage(mobileNo, messageString);
            smsSender.send(smsMessage);
        }
    }

    private void sendRegistationStateNotificationL0L1(Long oldRegistrationStateId, Long newRegistrationStateId, String mobileNo, String accountMethod, String discripentValue) throws FrameworkCheckedException {
        if (newRegistrationStateId != null && oldRegistrationStateId != null
                && oldRegistrationStateId.longValue() != newRegistrationStateId
                && newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.VERIFIED
                || newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.DISCREPANT
                || newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.DECLINE
                || newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.REJECTED) {
            Object[] args = {RegistrationStateConstantsInterface.REG_STATE_MAP.get(newRegistrationStateId)};

            String messageString = null;

            if (newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.DISCREPANT.longValue()) {
                Object[] args1 = {RegistrationStateConstantsInterface.REG_STATE_MAP.get(newRegistrationStateId), "", accountMethod};
                if (discripentValue.equals("01")) {
                    args1[1] = "1. CNIC not visible/Original CNIC photo required \n" +
                            "2. Original Photo/Selfie required.\n";
                } else if (discripentValue.equals("02")) {
                    args1[1] = "1. CNIC not visible/Original CNIC photo required \n";
                } else if (discripentValue.equals("03")) {
                    args1[1] = "1. Original Photo/Selfie required.\n";
                }

                messageString = MessageUtil.getMessage("smsCommand.dct_sms12discrepantl0", args1);
            }
            if (newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.VERIFIED.longValue()) {
                Object[] args2 = {RegistrationStateConstantsInterface.REG_STATE_MAP.get(newRegistrationStateId), mobileNo};
                messageString = MessageUtil.getMessage("smsCommand.dct_sms12activatel0", args2);
            } else if (newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.DECLINE.longValue() ||
                    newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.REJECTED.longValue()) {
                messageString = MessageUtil.getMessage("smsCommand.dct_sms12", args);
            }

            SmsMessage smsMessage = new SmsMessage(mobileNo, messageString);
            smsSender.send(smsMessage);
        }
    }

    public SmartMoneyAccountModel getSmartMoneyAccountByExample(SmartMoneyAccountModel _smartMoneyAccountModel) throws FrameworkCheckedException {
        SmartMoneyAccountModel smartMoneyAccountModel = null;

        _smartMoneyAccountModel.setActive(true);
        _smartMoneyAccountModel.setDeleted(false);

        CustomList<SmartMoneyAccountModel> results = smartMoneyAccountDAO.findByExample(_smartMoneyAccountModel);

        if (results != null && results.getResultsetList().size() > 0) {

            smartMoneyAccountModel = results.getResultsetList().get(0);
        }

        return smartMoneyAccountModel;
    }


    public BankModel getOlaBankMadal() {
        BankModel bankModel = new BankModel();
        bankModel.setFinancialIntegrationId(FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION);
        CustomList bankList = this.bankDAO.findByExample(bankModel);
        List bankL = bankList.getResultsetList();
        bankModel = (BankModel) bankL.get(0);
        return bankModel;
    }

    private boolean isNICUnique(String nic, Long appUserId) {

        return isNICUnique(nic, appUserId, null);
    }

    private boolean isNICUnique(String nic, Long appUserId, BaseWrapper baseWrapper) {
        boolean returnValue = Boolean.TRUE;
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setNic(nic);
        appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
        CustomList<AppUserModel> customAppUserModelList = this.appUserDAO.findByExample(appUserModel);
        if (customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0) {
            if (appUserId != null && appUserId > 0) {
                for (AppUserModel model : customAppUserModelList.getResultsetList()) {
                    if (!model.getAccountClosedSettled() && !model.getAccountClosedSettled() && !model.getAppUserId().equals(appUserId)) {
                        returnValue = Boolean.FALSE;
                        appUserModel = model;
                        break;
                    }
                }
            } else {
                //returnValue = Boolean.FALSE;
                for (AppUserModel model : customAppUserModelList.getResultsetList()) {
                    if (!model.getAccountClosedSettled()) {
                        returnValue = Boolean.FALSE;
                        appUserModel = model;
                        break;
                    }
                }
            }
        }

        if (returnValue) {
            appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
            customAppUserModelList = this.appUserDAO.findByExample(appUserModel);
            if (customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0) {
                if (appUserId != null && appUserId > 0) {
                    for (AppUserModel model : customAppUserModelList.getResultsetList()) {
                        if (!model.getAccountClosedSettled() && !model.getAccountClosedSettled() && !model.getAppUserId().equals(appUserId)) {
                            returnValue = Boolean.FALSE;
                            appUserModel = model;
                            break;
                        }
                    }
                } else {
                    //returnValue = Boolean.FALSE;
                    for (AppUserModel model : customAppUserModelList.getResultsetList()) {
                        if (!model.getAccountClosedSettled()) {
                            returnValue = Boolean.FALSE;
                            appUserModel = model;
                            break;
                        }
                    }
                }
            }
        }
        //adding handler case as well
        if (returnValue) {
            appUserModel.setAppUserTypeId(UserTypeConstantsInterface.HANDLER);
            customAppUserModelList = this.appUserDAO.findByExample(appUserModel);
            if (customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0) {
                if (appUserId != null && appUserId > 0) {
                    for (AppUserModel model : customAppUserModelList.getResultsetList()) {
                        if (!model.getAccountClosedSettled() && !model.getAppUserId().equals(appUserId)) {
                            returnValue = Boolean.FALSE;
                            appUserModel = model;
                            break;
                        }
                    }
                } else {
                    //returnValue = Boolean.FALSE;
                    for (AppUserModel model : customAppUserModelList.getResultsetList()) {
                        if (!model.getAccountClosedSettled()) {
                            returnValue = Boolean.FALSE;
                            appUserModel = model;
                            break;
                        }
                    }
                }
            }
        }

        if (baseWrapper != null && appUserModel.getAppUserId() != null) {

            try {

                UserDeviceAccountsModel userDeviceAccountsModel = userDeviceAccountsDAO.findUserDeviceAccountByAppUserId(appUserModel);

                if (userDeviceAccountsModel != null) {

                    baseWrapper.putObject(PortalConstants.KEY_MFS_ID, userDeviceAccountsModel.getUserId());
                }

                if (appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

                    baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, "Customer");

                } else if (appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {

                    baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, "Retailer");

                } else if (appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue()) {

                    baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, "Handler");

                } else {

                    baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, "User");
                }

            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
            }
        }

        return returnValue;

    }


    private boolean isMobileNumUnique(String mobileNo, Long appUserId) {

        return this.isMobileNumUnique(mobileNo, appUserId, null);
    }

    private boolean isMobileNumUnique(String mobileNo, Long appUserId, BaseWrapper baseWrapper) {
		/*AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(mobileNo);
		appUserModel.setAccountClosedUnsettled(false);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		ExampleConfigHolderModel configModel = new ExampleConfigHolderModel();
		configModel.setMatchMode(MatchMode.EXACT);
		Integer size = this.appUserDAO.countByExample(appUserModel,null,configModel);
		if(size == 0){
			appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
			size = this.appUserDAO.countByExample(appUserModel,null,configModel);
			if(size == 0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}*/
        boolean returnValue = Boolean.TRUE;
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setMobileNo(mobileNo);
        appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
        CustomList<AppUserModel> customAppUserModelList = this.appUserDAO.findByExample(appUserModel);
        if (customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0) {
            if (appUserId != null && appUserId > 0) {
                for (AppUserModel model : customAppUserModelList.getResultsetList()) {
                    if (!model.getAccountClosedSettled() && !model.getAppUserId().equals(appUserId)) {
                        returnValue = Boolean.FALSE;
                        appUserModel = model;
                        break;
                    }
                }
            } else {
                //returnValue = Boolean.FALSE;
                for (AppUserModel model : customAppUserModelList.getResultsetList()) {
                    if (!model.getAccountClosedSettled()) {
                        returnValue = Boolean.FALSE;
                        appUserModel = model;
                        break;
                    }
                }
            }
        }

        if (returnValue) {
            appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
            customAppUserModelList = this.appUserDAO.findByExample(appUserModel);
            if (customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0) {
                if (appUserId != null && appUserId > 0) {
                    for (AppUserModel model : customAppUserModelList.getResultsetList()) {
                        if (!model.getAccountClosedSettled() && !model.getAppUserId().equals(appUserId)) {
                            returnValue = Boolean.FALSE;
                            appUserModel = model;
                            break;
                        }
                    }
                } else {
                    //returnValue = Boolean.FALSE;
                    for (AppUserModel model : customAppUserModelList.getResultsetList()) {
                        if (!model.getAccountClosedSettled()) {
                            returnValue = Boolean.FALSE;
                            appUserModel = model;
                            break;
                        }
                    }
                }
            }
        }
        //adding handler case as well
        if (returnValue) {
            appUserModel.setAppUserTypeId(UserTypeConstantsInterface.HANDLER);
            customAppUserModelList = this.appUserDAO.findByExample(appUserModel);
            if (customAppUserModelList.getResultsetList() != null && customAppUserModelList.getResultsetList().size() > 0) {
                if (appUserId != null && appUserId > 0) {
                    for (AppUserModel model : customAppUserModelList.getResultsetList()) {
                        if (!model.getAccountClosedSettled() && !model.getAppUserId().equals(appUserId)) {
                            returnValue = Boolean.FALSE;
                            appUserModel = model;
                            break;
                        }
                    }
                } else {
                    //returnValue = Boolean.FALSE;
                    for (AppUserModel model : customAppUserModelList.getResultsetList()) {
                        if (!model.getAccountClosedSettled()) {
                            returnValue = Boolean.FALSE;
                            appUserModel = model;
                            break;
                        }
                    }
                }
            }
        }

        if (baseWrapper != null && appUserModel.getAppUserId() != null) {

            try {

                UserDeviceAccountsModel userDeviceAccountsModel = userDeviceAccountsDAO.findUserDeviceAccountByAppUserId(appUserModel);

                if (userDeviceAccountsModel != null) {

                    baseWrapper.putObject(PortalConstants.KEY_MFS_ID, userDeviceAccountsModel.getUserId());
                }

                if (appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

                    baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, "Customer");

                } else if (appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {

                    baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, "Agent");

                } else if (appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.HANDLER.longValue()) {

                    baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, "Handler");

                } else {

                    baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, "User");
                }

            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
            }
        }

        return returnValue;

    }


    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setClsPendingBlinkCustomerDAO(ClsPendingBlinkCustomerDAO clsPendingBlinkCustomerDAO) {
        this.clsPendingBlinkCustomerDAO = clsPendingBlinkCustomerDAO;
    }

    public void setAppUserDAO(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

    public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
        this.userDeviceAccountsDAO = userDeviceAccountsDAO;
    }

    public void setAddressDAO(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public void setCustTransManager(CustTransManager custTransManager) {
        this.custTransManager = custTransManager;
    }

    public void setCustomerAddressesDAO(CustomerAddressesDAO customerAddressesDAO) {
        this.customerAddressesDAO = customerAddressesDAO;
    }

    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    public void setOlaVeriflyFinancialInstitution(FinancialInstitution olaVeriflyFinancialInstitution) {
        this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
    }

    public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
        this.smartMoneyAccountDAO = smartMoneyAccountDAO;
    }

    public void setAccountInfoDao(AccountInfoDAO accountInfoDao) {
        this.accountInfoDao = accountInfoDao;
    }

    public void setBankDAO(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }

    public void setCustomerPictureDAO(CustomerPictureDAO customerPictureDAO) {
        this.customerPictureDAO = customerPictureDAO;
    }

    public void setBlinkCustomerModelDAO(BlinkCustomerModelDAO blinkCustomerModelDAO) {
        this.blinkCustomerModelDAO = blinkCustomerModelDAO;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void setBlinkCustomerDAO(BlinkCustomerDAO blinkCustomerDAO) {
        this.blinkCustomerDAO = blinkCustomerDAO;
    }

    public void setBlinkDefaultLimitDAO(BlinkDefaultLimitDAO blinkDefaultLimitDAO) {
        this.blinkDefaultLimitDAO = blinkDefaultLimitDAO;
    }
}
