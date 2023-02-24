package com.inov8.microbank.server.service.portal.mfsaccountmodule;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.BlinkDefaultLimitModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.app.dao.AppInfoDAO;
import com.inov8.microbank.app.model.AppInfoModel;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.ImplementationNotSupportedException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.*;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerPermissionViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.account.BulkCustomerAccountVo;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.debitcard.dao.DebitCardMailingAddressDAO;
import com.inov8.microbank.debitcard.dao.DebitCardRequestsViewModelDAO;
import com.inov8.microbank.debitcard.model.DebitCardMailingAddressModel;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.debitcard.model.DebitCardRequestsViewModel;
import com.inov8.microbank.ivr.IvrRequestHandler;
import com.inov8.microbank.server.dao.addressmodule.AddressDAO;
import com.inov8.microbank.server.dao.addressmodule.CustomerAddressesDAO;
import com.inov8.microbank.server.dao.agenthierarchymodule.SalesHierarchyDAO;
import com.inov8.microbank.server.dao.appusermobilehistorymodule.AppUserMobileHistoryDAO;
import com.inov8.microbank.server.dao.appuserpartnergroupmodule.AppUserPartnerGroupDAO;
import com.inov8.microbank.server.dao.bankmodule.BankDAO;
import com.inov8.microbank.server.dao.customermodule.*;
import com.inov8.microbank.server.dao.fetchcardtype.FetchCardTypeDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.portal.agentmerchantdetailmodule.AgentMerchantDetailDAO;
import com.inov8.microbank.server.dao.portal.agentmerchantdetailmodule.AgentMerchantDetailViewDAO;
import com.inov8.microbank.server.dao.portal.citymodule.CityDAO;
import com.inov8.microbank.server.dao.portal.kycmodule.ACOwnerShipDAO;
import com.inov8.microbank.server.dao.portal.kycmodule.ApplicantTxModeDAO;
import com.inov8.microbank.server.dao.portal.kycmodule.KYCDAO;
import com.inov8.microbank.server.dao.portal.linkpaymentmodemodule.LinkPaymentModeDAO;
import com.inov8.microbank.server.dao.portal.mfsaccountmodule.GoldenNosDAO;
import com.inov8.microbank.server.dao.portal.mfsaccountmodule.MinorUserListViewDAO;
import com.inov8.microbank.server.dao.portal.mfsaccountmodule.UserInfoListViewDAO;
import com.inov8.microbank.server.dao.portal.occupationmodule.OccupationDAO;
import com.inov8.microbank.server.dao.portal.ola.OlaCustomerAccountTypeDao;
import com.inov8.microbank.server.dao.portal.professionmodule.ProfessionDAO;
import com.inov8.microbank.server.dao.retailermodule.BulkAgentCreationDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import com.inov8.microbank.server.dao.rootedmobilehistory.RootedMobileHistoryDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailDAO;
import com.inov8.microbank.server.facade.CoreAdviceQueingPreProcessor;
import com.inov8.microbank.server.facade.portal.partnergroupmodule.PartnerGroupFacade;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.fileloader.ArbitraryResourceLoader;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.jms.JmsProducer;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import com.inov8.microbank.server.service.xml.XmlMarshaller;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import com.inov8.microbank.webapp.action.handler.HandlerBackingBean;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.dao.account.AccountDAO;
import com.inov8.ola.server.dao.accountholder.AccountHolderDAO;
import com.inov8.ola.server.dao.blinkcustomerlimit.BlinkCustomerDAO;
import com.inov8.ola.server.dao.limit.BlinkDefaultLimitDAO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.LimitTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;
import com.inov8.verifly.common.constants.ConfigurationConstants;
import com.inov8.verifly.common.constants.FailureReasonConstants;
import com.inov8.verifly.common.des.EncryptionHandler;
import com.inov8.verifly.common.encryption.AESEncryption;
import com.inov8.verifly.common.encryption.Encryption;
import com.inov8.verifly.common.exceptions.InvalidDataException;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.model.VeriflyConfigurationModel;
import com.inov8.verifly.common.model.VfFailureReasonModel;
import com.inov8.verifly.common.util.ConfigurationContainer;
import com.inov8.verifly.common.util.VariflyValidator;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.dao.mainmodule.AccountInfoDAO;
import com.inov8.verifly.server.dao.mainmodule.FailureReasonDAO;
import com.inov8.verifly.server.dao.mainmodule.VeriflyConfigurationDAO;
import com.inov8.verifly.server.service.logmodule.LogManager;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.JDBCException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.oxm.XmlMappingException;
import org.springframework.web.context.ContextLoader;
import sun.misc.BASE64Decoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyPair;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class MfsAccountManagerImpl implements MfsAccountManager {

    private final static Log logger = LogFactory.getLog(MfsAccountManagerImpl.class);
    protected AccountInfoDAO accountInfoDao;
    protected LogManager logManager;
    protected EncryptionHandler encryptionHandler;
    protected ConfigurationContainer keysObj;
    protected VariflyValidator variflyValidator;
    FinancialInstitution olaVeriflyFinancialInstitution;
    private ArbitraryResourceLoader arbitraryResourceLoader;
    private CustTransManager custTransManager;
    private AppUserDAO appUserDAO;
    private BlinkCustomerModelDAO blinkCustomerModelDAO;
    private UserDeviceAccountsDAO userDeviceAccountsDAO;
    private UserInfoListViewDAO userInfoListViewDAO;
    private MinorUserListViewDAO minorUserInfoListViewDAO;
    private GoldenNosDAO goldenNosDAO;
    private OracleSequenceGeneratorJdbcDAO sequenceGenerator;
    private SmsSender smsSender;
    private ActionLogManager actionLogManager;
    private MessageSource messageSource;
    private BankDAO bankDAO;
    private FinancialIntegrationManager financialIntegrationManager;
    private LinkPaymentModeDAO linkPaymentModeDAO;
    private AddressDAO addressDAO;
    private CustomerAddressesDAO customerAddressesDAO;
    private OracleSequenceGeneratorJdbcDAO deviceApplicationNoGenerator;
    private SegmentDAO segmentDAO;
    private LanguageDAO languageDAO;
    private CustomerTypeDAO customerTypeDAO;
    private OlaCustomerAccountTypeDao olaCustomerAccountTypeDao;
    private FundSourceDAO fundSourceDAO;
    private SmartMoneyAccountDAO smartMoneyAccountDAO;
    private WalkinCustomerDAO walkinCustomerDAO;
    private CustomerPictureDAO customerPictureDAO;
    private BlinkCustomerPictureDAO blinkCustomerPictureDAO;
    private JmsProducer jmsProducer;
    private XmlMarshaller<BulkCustomerAccountVo> xmlMarshaller;
    private FailureReasonDAO failureReasonDao;
    private SalesHierarchyDAO salesHierarchyDAO;
    private HashMap configurationHashMap = new HashMap();
    private HashMap failureReasonHashMap = new HashMap();
    private VeriflyConfigurationDAO veriflyConfigurationDao;
    private ApplicantDetailDAO applicantDetailDAO;
    private BusinessDetailDAO businessDetailDAO;
    private HandlerManager handlerManager;
    private CommissionManager commissionManager;
    private TransactionDetailDAO transactionDetailDAO;
    private TransactionModuleManager transactionManager;
    private SettlementManager settlementManager;
    private AppUserMobileHistoryDAO appUserMobileHistoryDAO;
    private CustomerFundSourceDAO customerFundSourceDAO;
    private IvrRequestHandler ivrRequestHandler;
    private KYCDAO kycDAO;
    private ApplicantTxModeDAO transactionModeDAO;
    private AgentMerchantDetailDAO agentMerchantDetailDAO;
    private AgentMerchantDetailViewDAO agentMerchantDetailViewDAO;
    private ACOwnerShipDAO acOwnerShipDetailDAO;
    private RetailerContactDAO retailerContactDAO;
    private AgentHierarchyManager agentHierarchyManager;
    private PartnerGroupFacade partnerGroupFacade;
    private ReferenceDataManager referenceDataManager;
    private RetailerContactManager retailerContactManager;
    private AppUserPartnerGroupDAO appUserPartnerGroupDAO;
    private AccountHolderDAO accountHolderDAO;
    private AccountDAO accountDAO;
    private CityDAO cityDAO;
    private OccupationDAO occupationDAO;
    private ProfessionDAO professionDAO;
    private TransactionDetailMasterManager transactionDetailMasterManager;
    private RootedMobileHistoryDAO rootedMobileHistoryDAO;
    private AccountControlManager accountControlManager;
    private BlinkCustomerDAO blinkCustomerDAO;
    private BlinkDefaultLimitDAO blinkDefaultLimitDAO;
    private DebitCardMailingAddressDAO debitCardMailingAddressDAO;
    private DebitCardRequestsViewModelDAO debitCardRequestsViewModelDAO;
    private FetchCardTypeDAO fetchCardTypeDAO;
    private ESBAdapter esbAdapter;

    @Autowired
    private AppInfoDAO appInfoDAO;

    private BulkAgentCreationDAO bulkAgentCreationDAO;
    private TransactionReversalManager transactionReversalManager;
    private CoreAdviceQueingPreProcessor coreAdviceQueingPreProcessor;


    public SearchBaseWrapper loadAppUserByMobileNumberAndTypeHQL(SearchBaseWrapper searchBaseWrapper) {
        AppUserModel user = (AppUserModel) searchBaseWrapper.getBasePersistableModel();
        user = appUserDAO.loadAppUserByQuery(user.getMobileNo(), user.getAppUserTypeId());
        searchBaseWrapper.setBasePersistableModel(user);
        return searchBaseWrapper;

    }

    public void setOlaVeriflyFinancialInstitution(FinancialInstitution olaVeriflyFinancialInstitution) {
        this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
    }

    public void setLinkPaymentModeDAO(LinkPaymentModeDAO linkPaymentModeDAO) {
        this.linkPaymentModeDAO = linkPaymentModeDAO;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setBankDAO(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }


    /**
     * Method creates the new MWallet Account, enters data in
     * tables. Customer, App_User and User_Device_Accounts
     */
    public BaseWrapper createMfsAccount(BaseWrapper baseWrapper)
            throws FrameworkCheckedException {
        String actionLogHandler = (String) baseWrapper.getObject(CommandFieldConstants.KEY_ACTION_LOG_HANDLER);

        /**
         * Loging the data
         */
        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        AppUserModel appUserModel = new AppUserModel();
        CustomerModel customerModel = new CustomerModel();
        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        MfsAccountModel mfsAccountModel =
                (MfsAccountModel) baseWrapper.getObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY);

        if (!this.isMobileNumUnique(mfsAccountModel.getMobileNo(), mfsAccountModel.getAppUserId(), baseWrapper)) {
            throw new FrameworkCheckedException("MobileNumUniqueException");
        }
        if (!this.isNICUnique(mfsAccountModel.getNic(), mfsAccountModel.getAppUserId(), baseWrapper)) {
            throw new FrameworkCheckedException("NICUniqueException");
        }

        Date nowDate = new Date();

        customerModel.setRegister(true);
        customerModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        customerModel.setCreatedOn(mfsAccountModel.getCreatedOn());
        customerModel.setUpdatedOn(mfsAccountModel.getCreatedOn());
        customerModel.setApplicationN0(deviceApplicationNoGenerator.nextLongValue().toString());

        customerModel.setName(mfsAccountModel.getName());
        customerModel.setCustomerAccountTypeId(mfsAccountModel.getCustomerAccountTypeId());
        customerModel.setMobileNo(mfsAccountModel.getMobileNo());
        customerModel.setSegmentId(mfsAccountModel.getSegmentId());
        customerModel.setCurrency(mfsAccountModel.getCurrency());
        customerModel.setGender(mfsAccountModel.getGender());
        customerModel.setCustomerTypeId(mfsAccountModel.getCustomerTypeId());
        customerModel.setFatherHusbandName(mfsAccountModel.getFatherHusbandName());
        customerModel.setNokName(mfsAccountModel.getNokName());
        customerModel.setNokRelationship(mfsAccountModel.getNokRelationship());
        customerModel.setNokMobile(mfsAccountModel.getNokMobile());
        customerModel.setNokNic(mfsAccountModel.getNokNic());
        customerModel.setBirthPlace(mfsAccountModel.getBirthPlace());
        customerModel.setEmail(mfsAccountModel.getEmail());
        customerModel.setRegStateComments(mfsAccountModel.getRegStateComments());
        customerModel.setComments(mfsAccountModel.getComments());
        customerModel.setInitialDeposit(mfsAccountModel.getInitialDeposit());
        customerModel.setIsCnicSeen(mfsAccountModel.isCnicSeen());
        customerModel.setScreeningPerformed(mfsAccountModel.getScreeningPerformed());
        customerModel.setTaxRegimeId(mfsAccountModel.getTaxRegimeId());
        customerModel.setFed(mfsAccountModel.getFed());
        customerModel.setIsMPINGenerated(Boolean.FALSE);
        customerModel.setWebServiceEnabled(false);
        // Populating Present Home Address

        AddressModel presentHomeAddress = new AddressModel();
        presentHomeAddress.setFullAddress(mfsAccountModel.getPresentAddress());

        if (mfsAccountModel.getCity() != null) {
            presentHomeAddress.setCityId(Long.parseLong(mfsAccountModel.getCity()));
        }

        AddressModel nokHomeAddress = new AddressModel();
        if (mfsAccountModel.getNokMailingAdd() != null) {
            nokHomeAddress.setFullAddress(mfsAccountModel.getNokMailingAdd());
        }

        /**
         * Populating the AppUserModel Model
         */

        String[] nameArray = mfsAccountModel.getName().split(" ");
        appUserModel.setFirstName(nameArray[0]);
        if (nameArray.length > 1) {
            appUserModel.setLastName(mfsAccountModel.getName().substring(appUserModel.getFirstName().length() + 1));
        } else {
            appUserModel.setLastName(nameArray[0]);
        }
        appUserModel.setRegistrationStateId(mfsAccountModel.getRegistrationStateId());
        appUserModel.setAddress1(" ");
        appUserModel.setAddress2(" ");
        appUserModel.setMobileNo(mfsAccountModel.getMobileNo());
        String nicWithoutHyphins = mfsAccountModel.getNic().replace("-", "");
        appUserModel.setNic(nicWithoutHyphins);
        appUserModel.setNicExpiryDate(mfsAccountModel.getNicExpiryDate());
        appUserModel.setCity(" ");
        appUserModel.setCountry(" ");
        appUserModel.setState(" ");
        appUserModel.setDob(mfsAccountModel.getDob());
        appUserModel.setMobileTypeId(1L);
        appUserModel.setPasswordChangeRequired(true);
        appUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        appUserModel.setCreatedOn(nowDate);
        appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        appUserModel.setUpdatedOn(nowDate);
        appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        if ((mfsAccountModel.getMotherMaidenName() == null
                || (mfsAccountModel.getMotherMaidenName() != null && mfsAccountModel.getMotherMaidenName().equals("")))
                && mfsAccountModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0))
            mfsAccountModel.setMotherMaidenName("Mother");
        appUserModel.setMotherMaidenName(mfsAccountModel.getMotherMaidenName());

        appUserModel.setAccountEnabled(true);
        appUserModel.setAccountExpired(false);
        appUserModel.setAccountLocked(false);
        appUserModel.setCredentialsExpired(false);
        appUserModel.setAccountClosedUnsettled(false);
        appUserModel.setAccountClosedSettled(false);
        appUserModel.setFiler(mfsAccountModel.getFiler());

        ///////// Set Hot State in case of Bulk upload
//		if(null!= mfsAccountModel.getCustomerPic() || null!= mfsAccountModel.getCustomerPicByte()){
//			appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);}
//		else{
//			appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_HOT);}


        /**
         * Populating the UserDeviceAccountsModel
         */
        userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setCommissioned(false);
        userDeviceAccountsModel.setAccountEnabled(true);
        userDeviceAccountsModel.setAccountExpired(false);
        userDeviceAccountsModel.setAccountLocked(false);
        userDeviceAccountsModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        userDeviceAccountsModel.setCreatedOn(nowDate);
        userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        userDeviceAccountsModel.setUpdatedOn(nowDate);
        userDeviceAccountsModel.setPinChangeRequired(true);
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
        userDeviceAccountsModel.setCredentialsExpired(false);
        //userDeviceAccountsModel.setProdCatalogId(PortalConstants.CUSTOMER_DEFAULT_CATALOG);
        userDeviceAccountsModel.setProdCatalogId(mfsAccountModel.getProductCatalogId());

        Boolean bvs = mfsAccountModel.getBvsAccount();
        if (bvs != null && bvs) {
            appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
            appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
            appUserModel.setVerified(true);
            customerModel.setVerisysDone(true);
            customerModel.setAccountMethodId(AccountOpeningMethodConstantsInterface.BVS);
        } else {
            appUserModel.setRegistrationStateId(mfsAccountModel.getRegistrationStateId());
            appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);
            appUserModel.setVerified(false);
            customerModel.setVerisysDone(mfsAccountModel.getVerisysDone());
            customerModel.setAccountMethodId(AccountOpeningMethodConstantsInterface.CONVENTIONAL);
        }

        /**
         * Here creating the mfsId/usernaem , pin/password
         */
        String mfsId = computeMfsId();
        String username = mfsId;

        String randomPin = RandomUtils.generateRandom(4, false, true);
        String password = randomPin;

        /**
         * Saving the CustomerModel
         */
        baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(customerModel);
        baseWrapper = this.custTransManager.saveOrUpdate(baseWrapper);
        customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();

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

        /***
         * Saving Customer Source of Funds
         */
        if (null != mfsAccountModel.getFundsSourceId()) {
            for (String fundSourceId : mfsAccountModel.getFundsSourceId()) {
                CustomerFundSourceModel customerFundSourceModel = new CustomerFundSourceModel();
                customerFundSourceModel.setCustomerId(customerModel.getCustomerId());
                customerFundSourceModel.setFundSourceId(Long.parseLong(fundSourceId));
                customerFundSourceDAO.saveOrUpdate(customerFundSourceModel);
            }
        }

        /***
         * Saving Customer Pics
         */
        if (null != mfsAccountModel.getCustomerPic() || null != mfsAccountModel.getCustomerPicByte()) {
            CustomerPictureModel customerPictureModel = new CustomerPictureModel();
            try {
                File file = arbitraryResourceLoader.loadImage("images/no_photo_icon.png");

                customerPictureModel.setCustomerId(customerModel.getCustomerId());
                if (null != mfsAccountModel.getCustomerPic() && mfsAccountModel.getCustomerPic().getSize() > 1)
                    customerPictureModel.setPicture(mfsAccountModel.getCustomerPic().getBytes());
                else if (null != mfsAccountModel.getCustomerPicByte() && mfsAccountModel.getCustomerPicByte().length > 1)
                    customerPictureModel.setPicture(mfsAccountModel.getCustomerPicByte());
                customerPictureModel.setPictureTypeId(PictureTypeConstants.CUSTOMER_PHOTO);
                customerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel.setCreatedOn(nowDate);
                customerPictureModel.setUpdatedOn(nowDate);
                customerPictureDAO.saveOrUpdate(customerPictureModel);

                customerPictureModel = new CustomerPictureModel();
                customerPictureModel.setCustomerId(customerModel.getCustomerId());
                if (null != mfsAccountModel.getTncPic() && mfsAccountModel.getTncPic().getSize() > 1)
                    customerPictureModel.setPicture(mfsAccountModel.getTncPic().getBytes());
                else if (null != mfsAccountModel.getTncPicByte() && mfsAccountModel.getTncPicByte().length > 1)
                    customerPictureModel.setPicture(mfsAccountModel.getTncPicByte());
                customerPictureModel.setPictureTypeId(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY);
                customerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel.setCreatedOn(nowDate);
                customerPictureModel.setUpdatedOn(nowDate);
                customerPictureDAO.saveOrUpdate(customerPictureModel);

                customerPictureModel = new CustomerPictureModel();
                customerPictureModel.setCustomerId(customerModel.getCustomerId());
                if (null != mfsAccountModel.getSignPic() && mfsAccountModel.getSignPic().getSize() > 1)
                    customerPictureModel.setPicture(mfsAccountModel.getSignPic().getBytes());
                else if (null != mfsAccountModel.getSignPicByte() && mfsAccountModel.getSignPicByte().length > 1)
                    customerPictureModel.setPicture(mfsAccountModel.getSignPicByte());
                else
                    customerPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                customerPictureModel.setPictureTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT);
                customerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel.setCreatedOn(nowDate);
                customerPictureModel.setUpdatedOn(nowDate);
                customerPictureDAO.saveOrUpdate(customerPictureModel);

                customerPictureModel = new CustomerPictureModel();
                customerPictureModel.setCustomerId(customerModel.getCustomerId());
                if (null != mfsAccountModel.getCnicFrontPic() && mfsAccountModel.getCnicFrontPic().getSize() > 1)
                    customerPictureModel.setPicture(mfsAccountModel.getCnicFrontPic().getBytes());
                else if (null != mfsAccountModel.getCnicFrontPicByte() && mfsAccountModel.getCnicFrontPicByte().length > 1)
                    customerPictureModel.setPicture(mfsAccountModel.getCnicFrontPicByte());
                customerPictureModel.setPictureTypeId(PictureTypeConstants.ID_FRONT_SNAPSHOT);
                customerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel.setCreatedOn(nowDate);
                customerPictureModel.setUpdatedOn(nowDate);
                customerPictureDAO.saveOrUpdate(customerPictureModel);

                customerPictureModel = new CustomerPictureModel();
                customerPictureModel.setCustomerId(customerModel.getCustomerId());
                if (null != mfsAccountModel.getCnicBackPic() && mfsAccountModel.getCnicBackPic().getSize() > 1)
                    customerPictureModel.setPicture(mfsAccountModel.getCnicBackPic().getBytes());
                else if (null != mfsAccountModel.getCnicBackPicByte() && mfsAccountModel.getCnicBackPicByte().length > 1)
                    customerPictureModel.setPicture(mfsAccountModel.getCnicBackPicByte());
                else
                    customerPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                customerPictureModel.setPictureTypeId(PictureTypeConstants.ID_BACK_SNAPSHOT);
                customerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerPictureModel.setCreatedOn(nowDate);
                customerPictureModel.setUpdatedOn(nowDate);
                customerPictureDAO.saveOrUpdate(customerPictureModel);

                if (mfsAccountModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
                    customerPictureModel = new CustomerPictureModel();
                    customerPictureModel.setCustomerId(customerModel.getCustomerId());
                    if (null != mfsAccountModel.getLevel1FormPic() && mfsAccountModel.getLevel1FormPic().getSize() > 1)
                        customerPictureModel.setPicture(mfsAccountModel.getLevel1FormPic().getBytes());
                    else if (null != mfsAccountModel.getLevel1FormPicByte() && mfsAccountModel.getLevel1FormPicByte().length > 1)
                        customerPictureModel.setPicture(mfsAccountModel.getLevel1FormPicByte());
                    customerPictureModel.setPictureTypeId(PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT);
                    customerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                    customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    customerPictureModel.setCreatedOn(nowDate);
                    customerPictureModel.setUpdatedOn(nowDate);
                    customerPictureDAO.saveOrUpdate(customerPictureModel);
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }


        /**
         * Saving the AppUserModel
         */
        baseWrapper = new BaseWrapperImpl();
        appUserModel.setCustomerId(customerModel.getCustomerId());
        appUserModel.setUsername(username);
        appUserModel.setPassword(com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, password));
        appUserModel = this.appUserDAO.saveOrUpdate(appUserModel);

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

        /**
         * Saving the UserDeviceAccountsModel
         */
        userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
        userDeviceAccountsModel.setUserId(mfsId);
        userDeviceAccountsModel.setPin(com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, password));

        /* if number is post paid then change
         * expiry_date = current year + 100 years
         */
        if (MobileTypeConstantsInterface.MOBILE_TYPE_ID_POST_PAID.equals(mfsAccountModel.getConnectionType())) {
            userDeviceAccountsModel.setExpiryDate(PortalDateUtils.addYears(new Date(), CommandConstants.DEFAULT_YEARS_TO_ADD));
            userDeviceAccountsModel.setAccountLocked(Boolean.FALSE);
        }
        userDeviceAccountsModel.setPasswordChangeRequired(Boolean.FALSE);
        userDeviceAccountsModel = this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);

        // step 3 ----- smart money account
        AbstractFinancialInstitution abstractFinancialInstitution = null;
        VeriflyBaseWrapper veriflyBaseWrapper = null;
        SmartMoneyAccountModel smartMoneyAccountModel = null;
        boolean phoenixSuccessful = false;
        boolean firstSmartMoneyAccount = false;
        try { // Start TRY block
            BaseWrapper baseWrapperUserDevice = new BaseWrapperImpl();

            BankModel bankModel = getOlaBankMadal();
            bankModel.setBankId(bankModel.getBankId());

            BaseWrapper baseWrapperBank = new BaseWrapperImpl();
            baseWrapperBank.setBasePersistableModel(bankModel);
            abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);

            String bankName = bankModel.getName();

            smartMoneyAccountModel = null;
            {
                smartMoneyAccountModel = new SmartMoneyAccountModel();
                smartMoneyAccountModel.setCustomerId(customerModel.getCustomerId());
                smartMoneyAccountModel.setBankId(getOlaBankMadal().getBankId());
            }

            smartMoneyAccountModel.setRegistrationStateId(RegistrationStateConstants.REQUEST_RECEIVED);
            smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            smartMoneyAccountModel.setBankId(bankModel.getBankId());
            smartMoneyAccountModel.setCreatedOn(new Date());
            smartMoneyAccountModel.setUpdatedOn(new Date());
            smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
            smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            smartMoneyAccountModel.setActive(true);

            if (abstractFinancialInstitution.isVeriflyRequired()) {
                if (abstractFinancialInstitution.isVeriflyLite()) {

                    smartMoneyAccountModel.setChangePinRequired(false);
                } else {
                    //for testing
                    smartMoneyAccountModel.setChangePinRequired(false);
                }
            } else {
                smartMoneyAccountModel.setChangePinRequired(false);
            }

            smartMoneyAccountModel.setDefAccount(true);
            smartMoneyAccountModel.setDeleted(false);

            smartMoneyAccountModel.setName("i8_bb_" + userDeviceAccountsModel.getUserId());
            baseWrapper.setBasePersistableModel(this.linkPaymentModeDAO.saveOrUpdate(smartMoneyAccountModel));


        } // End TRY Block
        catch (ImplementationNotSupportedException inse) {
            throw new FrameworkCheckedException(
                    "implementationNotSupportedException");
        } catch (Exception ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constrainViolationException = (ConstraintViolationException) ex.getCause();
                String constraintName = "UK_USER_DEVICE_TYPE";
                if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1) {
                    throw new FrameworkCheckedException("Allpay Id already exists");
                }
                constraintName = "UK_SM_ACCOUNT";
                if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1) {
                    throw new FrameworkCheckedException("Account nick already exists");
                }
            }
            ex.printStackTrace();
            String errMessage = ex.getMessage();

            baseWrapper.putObject("ErrMessage", errMessage);

            // This is a tuch to handle the smartmoney account created in case
            // of verifly exception.
            // this is because the transaction is not getting rolled back
            if (smartMoneyAccountModel != null
                    && smartMoneyAccountModel.getSmartMoneyAccountId() != null) {
                System.out.println("<<<<<<<<<< Delete the smartmoeny account as exception has occured >>>>>>>>>");
                this.linkPaymentModeDAO.deleteByPrimaryKey(smartMoneyAccountModel.getSmartMoneyAccountId());
                System.out.println("<<<<<<<<<< Smartmoeny account is Deleted >>>>>>>>>");
            }
            if (/* firstSmartMoneyAccount && */!phoenixSuccessful) {
                // phoenix had a problem, verifly was hit, so rollback verifly.
                // TODO: here code to delete the verifly account
                try {
                    abstractFinancialInstitution.deleteAccount(veriflyBaseWrapper);
                } catch (Exception exp) {
                    ex = exp;
                }
                System.out.println("Phoenix had a problem");
            }

            throw new FrameworkCheckedException(ex.getMessage(), ex);
        }

        // step 4 ------ OLA Account Creation
        baseWrapper.putObject("bankModel", getOlaBankMadal());

        OLAVO olaVo = new OLAVO();
        olaVo.setFirstName(appUserModel.getFirstName());
        olaVo.setMiddleName(" ");
        olaVo.setLastName(appUserModel.getLastName());
        olaVo.setFatherName(appUserModel.getLastName());
        olaVo.setCnic(appUserModel.getNic());
        olaVo.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());

        if (appUserModel.getAddress1() == null)
            olaVo.setAddress("Lahore");
        else
            olaVo.setAddress(appUserModel.getAddress1());


        olaVo.setLandlineNumber(appUserModel.getMobileNo());
        olaVo.setMobileNumber(appUserModel.getMobileNo());
        if (appUserModel.getDob() == null)
            olaVo.setDob(new Date());
        else
            olaVo.setDob(appUserModel.getDob());
        olaVo.setStatusId(1l);
        // olaVo.setBalance(dcm.getBalance()); //balance is yet to be decided
        BankModel bankModel = (BankModel) baseWrapper.getObject("bankModel");

        SwitchWrapper sWrapper = new SwitchWrapperImpl();
        sWrapper.setOlavo(olaVo);
        sWrapper.setBankId(bankModel.getBankId());

        try {
            sWrapper = olaVeriflyFinancialInstitution.createAccount(sWrapper);
        } catch (Exception e) {
            e.printStackTrace();

            throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
        }

        if ("07".equals(olaVo.getResponseCode())) {
            throw new FrameworkCheckedException("NIC already exisits in the OLA accounts");
        }

        olaVo = sWrapper.getOlavo();

        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setAccountNo(olaVo.getPayingAccNo().toString());
        accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
        accountInfoModel.setActive(smartMoneyAccountModel.getActive());

        //Added after HSM Integration
        accountInfoModel.setPan(PanGenerator.generatePAN());
        // End HSM Integration Change


        accountInfoModel.setCreatedOn(smartMoneyAccountModel.getCreatedOn());
        accountInfoModel.setUpdatedOn(smartMoneyAccountModel.getUpdatedOn());
        accountInfoModel.setCustomerId(customerModel.getCustomerId());
        accountInfoModel.setCustomerMobileNo(appUserModel.getMobileNo());
        accountInfoModel.setFirstName(appUserModel.getFirstName());
        accountInfoModel.setLastName(appUserModel.getLastName());
        accountInfoModel.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
        accountInfoModel.setDeleted(Boolean.FALSE);
        accountInfoModel.setIsMigrated(1L);

        veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        // VeriflyManager veriflyMgr =
        // veriflyManagerService.getVeriflyMgrByBankId(bankModel);
        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        LogModel logmodel = new LogModel();
        logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
        veriflyBaseWrapper.setLogModel(logmodel);
        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,
                DeviceTypeConstantsInterface.WEB);
        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,
                DeviceTypeConstantsInterface.WEB);
        try {
            veriflyBaseWrapper = abstractFinancialInstitution.generatePin(veriflyBaseWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (veriflyBaseWrapper.isErrorStatus()) {

            Object[] args = {
                    veriflyBaseWrapper.getAccountInfoModel().getAccountNick(),
                    veriflyBaseWrapper.getAccountInfoModel().getAccountNick(),
                    veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin()};

            /**
             //			 * Phoenix call if verifly Lite (bank) is required. And First
             //			 * Account is being created.
             //			 */
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new FrameworkCheckedException(veriflyErrorMessage);
        }

        {
            /**
             * Sending SMS
             */

            //Updated against CRF-28.
            String messageString = null;
            String brandName = MessageUtil.getMessage("jsbl.brandName");

            AppInfoModel appInfoModel = new AppInfoModel();
            appInfoModel.setAppId(AppConstants.CONSUMER_APP);
            List<AppInfoModel> appInfoModelList = new ArrayList<>();
            appInfoModelList = appInfoDAO.findByExample(appInfoModel).getResultsetList();
            ArrayList<String> urls = new ArrayList<>();
            if (appInfoModel != null) {
                for (AppInfoModel model : appInfoModelList
                ) {
                    urls.add(model.getUrl());
                }
            }


            if (null == mfsAccountModel.getInitialDeposit()) {
                //Object[] args1 = {mfsId,MessageUtil.getMessage("mfsDownloadURL")};
                messageString = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_fonepay", new Object[]{username, password}, null);
            } else {
                messageString = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_fonepay", new Object[]{username, password}, null);
            }

            SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), messageString);
            smsSender.send(smsMessage);
        }
        ///////initiate IVR call to customer to generate user pin //////////
//		IvrRequestDTO ivrDTO = new IvrRequestDTO();
//		ivrDTO.setCustomerMobileNo(appUserModel.getMobileNo());
//		ivrDTO.setPin(veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin());
//		this.initiateUserGeneratedPinIvrCall(ivrDTO);

        /**
         * Logging Information, Ending Status
         */

        //Start Zindigi Customer sync call
        MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
        middlewareAdviceVO.setStan(String.valueOf((100000 + new Random().nextInt(900000))));
        middlewareAdviceVO.setRequestTime(new Date());
        middlewareAdviceVO.setDateTimeLocalTransaction(new Date());
        middlewareAdviceVO.setTransmissionTime(new Date());
        middlewareAdviceVO.setAdviceType(CoreAdviceUtil.ACCOUNNT_OPENING_ADVICE);
        middlewareAdviceVO.setProductId(ProductConstantsInterface.PORTAL_ACCOUNT_OPENING);
        middlewareAdviceVO.setConsumerNo(appUserModel.getMobileNo());
        middlewareAdviceVO.setCnicNo(appUserModel.getNic());

        transactionReversalManager.sendCoreReversalRequiresNewTransaction(middlewareAdviceVO);

//                        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
//                        switchWrapper.setBaseWrapper(baseWrapper);

        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

        workFlowWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);
        workFlowWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);
        workFlowWrapper.putObject(CommandFieldConstants.KEY_SMART_MONEY_ACCOUNT_MODEL, smartMoneyAccountModel);
        workFlowWrapper.setProductId(ProductConstantsInterface.PORTAL_ACCOUNT_OPENING);

        try {
            loadAndForwardAccountToQueue(workFlowWrapper);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //End Zindigi Customer sync call

        if (actionLogHandler == null) {
            actionLogModel.setCustomField1(appUserModel.getAppUserId().toString());
            actionLogModel.setCustomField11(appUserModel.getUsername());
            this.actionLogManager.completeActionLog(actionLogModel);
        }

        baseWrapper.putObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY, mfsAccountModel);
        baseWrapper.putObject(PortalConstants.KEY_MFS_ACCOUNT_ID, username);
        baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID, appUserModel.getAppUserId());
        baseWrapper.putObject(PortalConstants.KEY_MFS_ID, mfsId);

        return baseWrapper;
    }

    /**
     * @param mfsAccountModel
     * @throws CommandException
     */
    public void verifyAgentBalanceForInitialDeposit(double initialDeposit) throws Exception {
        ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());
        try {
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            SmartMoneyAccountModel smartMoneyAccountModel = this.getOLASmartMoneyAccountForAgent(UserUtils.getCurrentUser().getRetailerContactId());

            baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
            AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
            switchWrapper.setBasePersistableModel(smartMoneyAccountModel);
            switchWrapper.setWorkFlowWrapper(new WorkFlowWrapperImpl());

            ActionLogModel actionLogModel = new ActionLogModel();
            XStream xstream = new XStream();

            xstream.toXML("");
            actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALLPAY_WEB);
            actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(""),
                    XPathConstants.actionLogInputXMLLocationSteps));
            actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
            actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));

            baseWrapper.setBasePersistableModel(actionLogModel);
            baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
            actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
            if (actionLogModel.getActionLogId() != null) {
                ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
            }
            switchWrapper = abstractFinancialInstitution.checkBalanceWithoutPin(switchWrapper);

            actionLogModel.setCustomField1(String.valueOf(UserUtils.getCurrentUser().getAppUserId()));
            this.actionLogManager.completeActionLog(actionLogModel);

            Double balance = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getBalance());
            if (balance < initialDeposit) {
                logger.error("[MfsAccountManagerImpl.checkAgentBalanceForInitialDeposit] Your balance in insufficient to make this transaction.");
                throw new CommandException(this.getMessageSource().getMessage(WorkFlowErrorCodeConstants.INSUFFICIENT_BALANCE, null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalActionLog.remove();
        }
    }


    /**
     * @param mfsAccountModel
     * @throws CommandException
     */
    public void makeInitialDeposit(MfsAccountModel mfsAccountModel) throws CommandException {
        UserDeviceAccountsModel userDeviceAccountsModel;
        ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());

        CustomList<UserDeviceAccountsModel> userDeviceAccountsList = new CustomList<UserDeviceAccountsModel>();
        userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
        userDeviceAccountsList = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel);
        userDeviceAccountsModel = userDeviceAccountsList.getResultsetList().get(0);
        ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(userDeviceAccountsModel);

        // execute initial deposit command
        BaseWrapper idWrapper = new BaseWrapperImpl();
        idWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALL_PAY);
        idWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.ACCOUNT_OPENING);
        idWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
        idWrapper.putObject("AMOB", UserUtils.getCurrentUser().getMobileNo());
        idWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mfsAccountModel.getMobileNo());
        idWrapper.putObject(CommandFieldConstants.KEY_TXAM, mfsAccountModel.getInitialDeposit());
        idWrapper.putObject("PIN", userDeviceAccountsModel.getPin());

        getCommandManager().executeCommand(idWrapper, CommandFieldConstants.CMD_INITIAL_DEPOSIT);
    }

    @Override
    public BaseWrapper createLevel2Account(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        String actionLogHandler = (String) baseWrapper.getObject(CommandFieldConstants.KEY_ACTION_LOG_HANDLER);

        /**
         * Loging the data
         */
        ActionLogModel actionLogModel = new ActionLogModel();
        if (actionLogHandler == null) {
            actionLogModel.setActionId((Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID));
            actionLogModel.setUsecaseId((Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID));
            actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);  // the process is starting
            actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
            actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
            actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
            actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
            actionLogModel = logAction(actionLogModel);
            ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
        }
        AppUserModel appUserModel = new AppUserModel();
        CustomerModel customerModel = new CustomerModel();

        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        Level2AccountModel accountModel =
                (Level2AccountModel) baseWrapper.getObject(Level2AccountModel.LEVEL2_ACCOUNT_MODEL_KEY);

        if (!this.isMobileNumUnique(accountModel.getMobileNo(), accountModel.getAppUserId(), baseWrapper)) {
            throw new FrameworkCheckedException("MobileNumUniqueException");
        }
        ApplicantDetailModel applicant1DetailModel = accountModel.getApplicant1DetailModel();
        if (!this.isNICUnique(applicant1DetailModel.getIdNumber(), accountModel.getAppUserId(), baseWrapper)) {
            throw new FrameworkCheckedException("NICUniqueException");
        }

        //***************************************************************************************************************************
        //									Check if sender or receiver cnic is blacklisted
        //***************************************************************************************************************************
        if (accountControlManager.isCnicBlacklisted(applicant1DetailModel.getIdNumber())) {
            throw new FrameworkCheckedException("ID Document Number is Black Listed");
        }
        //***************************************************************************************************************************

        //check if it is bulk request!! create KYC for this as well
        if (accountModel.getIsBulkRequest() != null && accountModel.getIsBulkRequest()) {
            KYCModel kycModel = new KYCModel();
            kycModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
            kycModel.setCreatedOn(new Date());
            kycModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            kycModel.setUpdatedOn(new Date());
            kycModel.setInitialAppFormNo(accountModel.getInitialAppFormNo());
            kycModel.setAcType(CustomerAccountTypeConstants.LEVEL_2);
            BaseWrapper kycBaseWrapper = new BaseWrapperImpl();
            kycBaseWrapper.putObject(KYCModel.KYC_MODEL_KEY, kycModel);
            this.createKYC(kycBaseWrapper);
        }

        Date nowDate = new Date();
        customerModel.setInitialApplicationFormNumber(accountModel.getInitialAppFormNo());
        customerModel.setRegister(true);
        customerModel.setFed(accountModel.getFed());
        customerModel.setTaxRegimeId(accountModel.getTaxRegimeId());
        customerModel.setVerisysDone(accountModel.getIsVeriSysDone());
        customerModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        customerModel.setCreatedOn(accountModel.getCreatedOn());
        customerModel.setUpdatedOn(accountModel.getCreatedOn());
        customerModel.setApplicationN0(deviceApplicationNoGenerator.nextLongValue().toString());

        customerModel.setName(accountModel.getCustomerAccountName());
        customerModel.setCustomerAccountTypeId(accountModel.getCustomerAccountTypeId());
        customerModel.setBusinessTypeId(accountModel.getBusinessTypeId());
        customerModel.setMobileNo(accountModel.getMobileNo());
        customerModel.setSegmentId(accountModel.getSegmentId());
        customerModel.setCurrency(accountModel.getCurrency());
        customerModel.setGender(applicant1DetailModel.getGender());
        customerModel.setCustomerTypeId(accountModel.getCustomerTypeId());
        customerModel.setFundSourceId(accountModel.getFundsSourceId());
        customerModel.setFatherHusbandName(applicant1DetailModel.getFatherHusbandName());
        customerModel.setFundsSourceNarration(accountModel.getFundSourceNarration());
        customerModel.setReferringName1(accountModel.getRefferedBy());
        customerModel.setIsMPINGenerated(Boolean.FALSE);

        NokDetailVOModel nokDetailVOModel = accountModel.getNokDetailVOModel();
        if (nokDetailVOModel != null) {
            customerModel.setNokName(nokDetailVOModel.getNokName());
            customerModel.setNokContactNo(nokDetailVOModel.getNokContactNo());
            customerModel.setNokRelationship(nokDetailVOModel.getNokRelationship());
            customerModel.setNokMobile(nokDetailVOModel.getNokMobile());
            customerModel.setNokIdType(nokDetailVOModel.getNokIdType());
            customerModel.setNokIdNumber(nokDetailVOModel.getNokIdNumber());
        }

        customerModel.setBirthPlace(applicant1DetailModel.getBirthPlace());
        customerModel.setEmail(applicant1DetailModel.getEmail());
        customerModel.setAcNature(accountModel.getAcNature());
        customerModel.setFax(applicant1DetailModel.getFax());
        customerModel.setLandLineNo(applicant1DetailModel.getLandLineNo());
        customerModel.setContactNo(applicant1DetailModel.getContactNo());
        customerModel.setPublicFigure(accountModel.getPublicFigure());
        customerModel.setTransactionModeId(accountModel.getTransactionModeId());
        customerModel.setAccountPurposeId(accountModel.getAccountPurposeId());
        customerModel.setRegStateComments(accountModel.getRegStateComments());
        customerModel.setComments(accountModel.getComments());
        customerModel.setReferringName1(accountModel.getRefferedBy());
        customerModel.setOtherTransactionMode(accountModel.getOtherTransactionMode());
        customerModel.setSalary(accountModel.getSalary());
        customerModel.setBusinessIncome(accountModel.getBuisnessIncome());
        customerModel.setOtherIncome(accountModel.getOtherIncome());
        customerModel.setOtherBankName(accountModel.getOtherBankName());
        customerModel.setOtherBankAddress(accountModel.getOtherBankAddress());
        customerModel.setOtherBankACNo(accountModel.getOtherBankACNo());

		/*AdditionalDetailVOModel additionalDetailVOModel = accountModel.getAdditionalDetailVOModel();
		if (additionalDetailVOModel != null)
		{
			customerModel.setSalesTaxRegNo(additionalDetailVOModel.getSalesTaxRegNo());
			customerModel.setMembershipNoTradeBody(additionalDetailVOModel.getMembershipNoTradeBody());
			customerModel.setIncorporationDate(additionalDetailVOModel.getIncorporationDate());
			customerModel.setSecpRegNo(additionalDetailVOModel.getSecpRegNo());
			customerModel.setRegistrationPlace(additionalDetailVOModel.getRegistrationPlace());
		}*/
        /*added by atif hussain*/
        customerModel.setIsCnicSeen(accountModel.isCnicSeen());
        customerModel.setScreeningPerformed(accountModel.getScreeningPerformed());

        if (accountModel.getModeOfTx() != null && !accountModel.getModeOfTx().equals(""))
            customerModel.setTransactionModeId(Long.parseLong(accountModel.getModeOfTx()));

        customerModel.setAccountReasonId(accountModel.getAccountReason());


        /**
         * Saving the CustomerModel
         */
        baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(customerModel);
        baseWrapper = this.custTransManager.saveOrUpdate(baseWrapper);
        customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();

        //ApplicantDetailModel applicant2DetailModel = accountModel.getApplicant2DetailModel();
        //turab adding applicants other than one through loop
        for (ApplicantDetailModel applicant2DetailModel : accountModel.getApplicantDetailModelList()) {
            if (applicant2DetailModel != null && null != applicant2DetailModel.getName()) {

                if (applicant2DetailModel != null && null != applicant2DetailModel.getName()) {
                    applicant2DetailModel.setCustomerId(customerModel.getCustomerId());
                    applicant2DetailModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    applicant2DetailModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    applicant2DetailModel.setUpdatedOn(nowDate);
                    applicant2DetailModel.setCreatedOn(nowDate);
                    applicant2DetailModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_2);
                    applicant2DetailModel = this.applicantDetailDAO.saveOrUpdate(applicant2DetailModel);
                }

                AddressModel presentHomeAddressAp2 = new AddressModel();
                if (null != applicant2DetailModel.getResidentialAddress()) {
                    presentHomeAddressAp2.setHouseNo(applicant2DetailModel.getResidentialAddress());
                    presentHomeAddressAp2.setCityId(applicant2DetailModel.getResidentialCity());
                }
                AddressModel businessAddressAp2 = new AddressModel();
                if (null != applicant2DetailModel.getBuisnessAddress()) {
                    businessAddressAp2.setStreetAddress(applicant2DetailModel.getBuisnessAddress());
                    businessAddressAp2.setCityId(applicant2DetailModel.getBuisnessCity());
                }
                presentHomeAddressAp2 = this.addressDAO.saveOrUpdate(presentHomeAddressAp2);
                businessAddressAp2 = this.addressDAO.saveOrUpdate(businessAddressAp2);

                CustomerAddressesModel presentCustomerAddressesModelAp2 = new CustomerAddressesModel();
                presentCustomerAddressesModelAp2.setAddressId(presentHomeAddressAp2.getAddressId());
                presentCustomerAddressesModelAp2.setAddressTypeId(1L);
                presentCustomerAddressesModelAp2.setCustomerId(customerModel.getCustomerId());
                presentCustomerAddressesModelAp2.setApplicantTypeId(2L);
                presentCustomerAddressesModelAp2.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());

                CustomerAddressesModel businessCustomerAddressesModelAp2 = new CustomerAddressesModel();
                businessCustomerAddressesModelAp2.setAddressId(businessAddressAp2.getAddressId());
                businessCustomerAddressesModelAp2.setAddressTypeId(3L);
                businessCustomerAddressesModelAp2.setCustomerId(customerModel.getCustomerId());
                businessCustomerAddressesModelAp2.setApplicantTypeId(2L);
                businessCustomerAddressesModelAp2.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());

                presentCustomerAddressesModelAp2 = this.customerAddressesDAO.saveOrUpdate(presentCustomerAddressesModelAp2);
                businessCustomerAddressesModelAp2 = this.customerAddressesDAO.saveOrUpdate(businessCustomerAddressesModelAp2);

            }
        }

        //save AccountOwnerShip list now
        if (CollectionUtils.isNotEmpty(accountModel.getAcOwnershipDetailModelList())) {
            List<ACOwnershipDetailModel> accountOwnerShipModelList = new ArrayList<ACOwnershipDetailModel>(0);
            for (ACOwnershipDetailModel accountOwnerShipDetailModel : accountModel.getAcOwnershipDetailModelList()) {
                if (accountOwnerShipDetailModel.getAcOwnershipTypeId() != null) {
                    accountOwnerShipDetailModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    accountOwnerShipDetailModel.setCreatedOn(new Date());
                    accountOwnerShipDetailModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    accountOwnerShipDetailModel.setUpdatedOn(new Date());
                    accountOwnerShipDetailModel.setCustomerId(customerModel.getCustomerId());
                    accountOwnerShipDetailModel.setIsDeleted(false);
                    accountOwnerShipModelList.add(accountOwnerShipDetailModel);
                }
            }
            acOwnerShipDetailDAO.saveOrUpdateCollection(accountOwnerShipModelList);
        }

        AddressModel nokHomeAddress = new AddressModel();
        if (nokDetailVOModel != null && nokDetailVOModel.getNokMailingAdd() != null) {
            nokHomeAddress.setHouseNo(nokDetailVOModel.getNokMailingAdd());
        }
        nokHomeAddress = this.addressDAO.saveOrUpdate(nokHomeAddress);
        /**
         * Populating the AppUserModel Model
         */

        /**
         * Here creating the mfsId/usernaem , pin/password
         */
        String mfsId = computeMfsId();
        String username = mfsId;

        String randomPin = RandomUtils.generateRandom(4, false, true);
        //String password = randomPin;

        String password = EncoderUtils.encodeToSha(randomPin);
        String randomPinEncrypted = EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, randomPin);


        String[] nameArray = applicant1DetailModel.getName().split(" ");
        appUserModel.setFirstName(nameArray[0]);
        if (nameArray.length > 1) {
            appUserModel.setLastName(applicant1DetailModel.getName().substring(appUserModel.getFirstName().length() + 1));
        } else {
            appUserModel.setLastName(nameArray[0]);
        }
        appUserModel.setRegistrationStateId(accountModel.getRegistrationStateId());
        appUserModel.setAddress1(" ");
        appUserModel.setAddress2(" ");
        appUserModel.setMobileNo(accountModel.getMobileNo());
        String nicWithoutHyphins = applicant1DetailModel.getIdNumber().replace("-", "");
        appUserModel.setNic(nicWithoutHyphins);
        appUserModel.setNicExpiryDate(applicant1DetailModel.getIdExpiryDate());
        appUserModel.setCity(" ");
        appUserModel.setCountry(" ");
        appUserModel.setState(" ");
        appUserModel.setDob(applicant1DetailModel.getDob());
        appUserModel.setMobileTypeId(1L);
        appUserModel.setPasswordChangeRequired(true);
        appUserModel.setMotherMaidenName(" ");
        appUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        appUserModel.setCreatedOn(nowDate);
        appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        appUserModel.setUpdatedOn(nowDate);
        appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);

        appUserModel.setVerified(true);
        appUserModel.setAccountEnabled(true);
        appUserModel.setAccountExpired(false);
        appUserModel.setAccountLocked(false);
        appUserModel.setCredentialsExpired(false);
        appUserModel.setAccountClosedUnsettled(false);
        appUserModel.setAccountClosedSettled(false);
        appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);

        /**
         * Populating the UserDeviceAccountsModel
         */
        userDeviceAccountsModel.setCommissioned(false);
        userDeviceAccountsModel.setAccountEnabled(true);
        userDeviceAccountsModel.setAccountExpired(false);
        userDeviceAccountsModel.setAccountLocked(false);
        userDeviceAccountsModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        userDeviceAccountsModel.setCreatedOn(nowDate);
        userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        userDeviceAccountsModel.setUpdatedOn(nowDate);
        userDeviceAccountsModel.setPinChangeRequired(true);
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
        userDeviceAccountsModel.setCredentialsExpired(false);
        //userDeviceAccountsModel.setProdCatalogId(PortalConstants.CUSTOMER_DEFAULT_CATALOG);
        userDeviceAccountsModel.setProdCatalogId(accountModel.getProductCatalogId());


        BusinessDetailModel businessDetailModel = accountModel.getBusinessDetailModel();
        if (businessDetailModel != null) {
            businessDetailModel.setCustomerId(customerModel.getCustomerId());
        }
        applicant1DetailModel.setCustomerId(customerModel.getCustomerId());
        applicant1DetailModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
        applicant1DetailModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
        applicant1DetailModel.setUpdatedOn(nowDate);
        applicant1DetailModel.setCreatedOn(nowDate);
        applicant1DetailModel = this.applicantDetailDAO.saveOrUpdate(applicant1DetailModel);


        if (null != businessDetailModel) {
            this.businessDetailDAO.saveOrUpdate(businessDetailModel);
        }

        // Populating Present Home Address for applicant 1

        AddressModel presentHomeAddressAp1 = new AddressModel();
        presentHomeAddressAp1.setHouseNo(applicant1DetailModel.getResidentialAddress());
        presentHomeAddressAp1.setCityId(applicant1DetailModel.getResidentialCity());

        AddressModel businessAddressAp1 = new AddressModel();
        businessAddressAp1.setHouseNo(applicant1DetailModel.getBuisnessAddress());
        businessAddressAp1.setCityId(applicant1DetailModel.getBuisnessCity());

        presentHomeAddressAp1 = this.addressDAO.saveOrUpdate(presentHomeAddressAp1);
        businessAddressAp1 = this.addressDAO.saveOrUpdate(businessAddressAp1);

        /**
         * Saving CustomerAddressesModels
         */
        CustomerAddressesModel presentCustomerAddressesModelAp1 = new CustomerAddressesModel();
        presentCustomerAddressesModelAp1.setAddressId(presentHomeAddressAp1.getAddressId());
        presentCustomerAddressesModelAp1.setAddressTypeId(1L);
        presentCustomerAddressesModelAp1.setCustomerId(customerModel.getCustomerId());
        presentCustomerAddressesModelAp1.setApplicantTypeId(1L);
        presentCustomerAddressesModelAp1.setApplicantDetailId(applicant1DetailModel.getApplicantDetailId());

        CustomerAddressesModel businessCustomerAddressesModelAp1 = new CustomerAddressesModel();
        businessCustomerAddressesModelAp1.setAddressId(businessAddressAp1.getAddressId());
        businessCustomerAddressesModelAp1.setAddressTypeId(3L);
        businessCustomerAddressesModelAp1.setCustomerId(customerModel.getCustomerId());
        businessCustomerAddressesModelAp1.setApplicantTypeId(1L);
        businessCustomerAddressesModelAp1.setApplicantDetailId(applicant1DetailModel.getApplicantDetailId());

        CustomerAddressesModel nokCustomerAddressesModel = new CustomerAddressesModel();
        nokCustomerAddressesModel.setAddressId(nokHomeAddress.getAddressId());
        nokCustomerAddressesModel.setAddressTypeId(4L);
        nokCustomerAddressesModel.setCustomerId(customerModel.getCustomerId());


        presentCustomerAddressesModelAp1 = this.customerAddressesDAO.saveOrUpdate(presentCustomerAddressesModelAp1);
        businessCustomerAddressesModelAp1 = this.customerAddressesDAO.saveOrUpdate(businessCustomerAddressesModelAp1);
        nokCustomerAddressesModel = this.customerAddressesDAO.saveOrUpdate(nokCustomerAddressesModel);

        /***
         * Saving applicant 1 Pics
         * commented saving pictures as this is no more required by JS
         */
		/*if(applicant1DetailModel.getIdFrontPic() != null)
		{
			 CustomerPictureModel customerPictureModel = new CustomerPictureModel();
			try {
				 customerPictureModel.setApplicantTypeId(1L);
				 customerPictureModel.setCustomerId(customerModel.getCustomerId());
			     customerPictureModel.setPicture(applicant1DetailModel.getFatcaFormPic() == null ? null : applicant1DetailModel.getFatcaFormPic().getBytes());
			     customerPictureModel.setPictureTypeId(PictureTypeConstants.CUSTOMER_PHOTO);
			     customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			     customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			     customerPictureModel.setCreatedOn(nowDate);
			     customerPictureModel.setUpdatedOn(nowDate);
			     customerPictureModel.setApplicantDetailId(applicant1DetailModel.getApplicantDetailId());
			     if(customerPictureModel.getPicture() != null){
			    	 customerPictureDAO.saveOrUpdate(customerPictureModel);
			     }

			     customerPictureModel = new CustomerPictureModel();
			     customerPictureModel.setApplicantTypeId(1L);
			     customerPictureModel.setCustomerId(customerModel.getCustomerId());
			     customerPictureModel.setPicture(applicant1DetailModel.getTncPic().getBytes());
			     customerPictureModel.setPictureTypeId(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY);
			     customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			     customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			     customerPictureModel.setCreatedOn(nowDate);
			     customerPictureModel.setUpdatedOn(nowDate);
			     customerPictureModel.setApplicantDetailId(applicant1DetailModel.getApplicantDetailId());
			     customerPictureDAO.saveOrUpdate(customerPictureModel);

			     customerPictureModel = new CustomerPictureModel();
			     customerPictureModel.setApplicantTypeId(1L);
			     customerPictureModel.setCustomerId(customerModel.getCustomerId());
			     customerPictureModel.setPicture(applicant1DetailModel.getSignPic().getBytes());
			     customerPictureModel.setPictureTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT);
			     customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			     customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			     customerPictureModel.setCreatedOn(nowDate);
			     customerPictureModel.setUpdatedOn(nowDate);
			     customerPictureModel.setApplicantDetailId(applicant1DetailModel.getApplicantDetailId());
			     customerPictureDAO.saveOrUpdate(customerPictureModel);

			     customerPictureModel = new CustomerPictureModel();
			     customerPictureModel.setApplicantTypeId(1L);
			     customerPictureModel.setCustomerId(customerModel.getCustomerId());
			     customerPictureModel.setPicture(applicant1DetailModel.getIdFrontPic().getBytes());
			     customerPictureModel.setPictureTypeId(PictureTypeConstants.ID_FRONT_SNAPSHOT);
			     customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			     customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			     customerPictureModel.setCreatedOn(nowDate);
			     customerPictureModel.setUpdatedOn(nowDate);
			     customerPictureModel.setApplicantDetailId(applicant1DetailModel.getApplicantDetailId());
			     customerPictureDAO.saveOrUpdate(customerPictureModel);

			     customerPictureModel = new CustomerPictureModel();
			     customerPictureModel.setApplicantTypeId(1L);
			     customerPictureModel.setCustomerId(customerModel.getCustomerId());
			     customerPictureModel.setPicture(applicant1DetailModel.getIdBackPic().getBytes());
			     customerPictureModel.setPictureTypeId(PictureTypeConstants.ID_BACK_SNAPSHOT);
			     customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			     customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			     customerPictureModel.setCreatedOn(nowDate);
			     customerPictureModel.setUpdatedOn(nowDate);
			     customerPictureModel.setApplicantDetailId(applicant1DetailModel.getApplicantDetailId());
			     customerPictureDAO.saveOrUpdate(customerPictureModel);

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}*/
        /**
         * Saving the AppUserModel
         */
        baseWrapper = new BaseWrapperImpl();
        appUserModel.setCustomerId(customerModel.getCustomerId());
        appUserModel.setUsername(username);
        appUserModel.setPassword(password);
        appUserModel = this.appUserDAO.saveOrUpdate(appUserModel);

        /**
         * Saving appUserMobileHistoryModel
         * */

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

        /**
         * Saving the UserDeviceAccountsModel
         */
        userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
        userDeviceAccountsModel.setUserId(mfsId);
        userDeviceAccountsModel.setPin(randomPinEncrypted);
        //userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(randomPin));
        userDeviceAccountsModel.setPasswordChangeRequired(Boolean.FALSE);
        userDeviceAccountsModel = this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);

        // step 3 ----- smart money account
        AbstractFinancialInstitution abstractFinancialInstitution = null;
        VeriflyBaseWrapper veriflyBaseWrapper = null;
        SmartMoneyAccountModel smartMoneyAccountModel = null;
        boolean phoenixSuccessful = false;
        boolean firstSmartMoneyAccount = false;
        try { // Start TRY block
            BaseWrapper baseWrapperUserDevice = new BaseWrapperImpl();

            BankModel bankModel = getOlaBankMadal();
            bankModel.setBankId(bankModel.getBankId());

            BaseWrapper baseWrapperBank = new BaseWrapperImpl();
            baseWrapperBank.setBasePersistableModel(bankModel);
            abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);

            String bankName = bankModel.getName();

            smartMoneyAccountModel = null;
            {
                smartMoneyAccountModel = new SmartMoneyAccountModel();
                smartMoneyAccountModel.setCustomerId(customerModel.getCustomerId());
                smartMoneyAccountModel.setBankId(getOlaBankMadal().getBankId());
            }

            smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            smartMoneyAccountModel.setBankId(bankModel.getBankId());
            smartMoneyAccountModel.setCreatedOn(new Date());
            smartMoneyAccountModel.setUpdatedOn(new Date());
            smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
            smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            smartMoneyAccountModel.setActive(true);

            smartMoneyAccountModel.setChangePinRequired(false);

            smartMoneyAccountModel.setDefAccount(true);
            smartMoneyAccountModel.setDeleted(false);

            smartMoneyAccountModel.setName("i8_bb_" + userDeviceAccountsModel.getUserId());
            baseWrapper.setBasePersistableModel(this.linkPaymentModeDAO.saveOrUpdate(smartMoneyAccountModel));

        } // End TRY Block
        catch (ImplementationNotSupportedException inse) {
            throw new FrameworkCheckedException(
                    "implementationNotSupportedException");
        } catch (Exception ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constrainViolationException = (ConstraintViolationException) ex.getCause();
                String constraintName = "UK_USER_DEVICE_TYPE";
                if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1) {
                    throw new FrameworkCheckedException("Allpay Id already exists");
                }
                constraintName = "UK_SM_ACCOUNT";
                if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1) {
                    throw new FrameworkCheckedException("Account nick already exists");
                }
            }
            ex.printStackTrace();
            String errMessage = ex.getMessage();

            baseWrapper.putObject("ErrMessage", errMessage);

            // This is a tuch to handle the smartmoney account created in case
            // of verifly exception.
            // this is because the transaction is not getting rolled back
            if (smartMoneyAccountModel != null
                    && smartMoneyAccountModel.getSmartMoneyAccountId() != null) {
                System.out.println("<<<<<<<<<< Delete the smartmoeny account as exception has occured >>>>>>>>>");
                this.linkPaymentModeDAO.deleteByPrimaryKey(smartMoneyAccountModel.getSmartMoneyAccountId());
                System.out.println("<<<<<<<<<< Smartmoeny account is Deleted >>>>>>>>>");
            }
            if (/* firstSmartMoneyAccount && */!phoenixSuccessful) {
                // phoenix had a problem, verifly was hit, so rollback verifly.
                // TODO: here code to delete the verifly account
                try {
                    abstractFinancialInstitution.deleteAccount(veriflyBaseWrapper);
                } catch (Exception exp) {
                    ex = exp;
                }
                System.out.println("Phoenix had a problem");
            }

            throw new FrameworkCheckedException(ex.getMessage(), ex);
        }

        // step 4 ------ OLA Account Creation
        baseWrapper.putObject("bankModel", getOlaBankMadal());

        OLAVO olaVo = new OLAVO();
        olaVo.setFirstName(appUserModel.getFirstName());
        olaVo.setMiddleName(" ");
        olaVo.setLastName(appUserModel.getLastName());
        olaVo.setFatherName(appUserModel.getLastName());
        olaVo.setCnic(appUserModel.getNic());
        olaVo.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());

        if (appUserModel.getAddress1() == null)
            olaVo.setAddress("Lahore");
        else
            olaVo.setAddress(appUserModel.getAddress1());


        olaVo.setLandlineNumber(appUserModel.getMobileNo());
        olaVo.setMobileNumber(appUserModel.getMobileNo());
        if (appUserModel.getDob() == null)
            olaVo.setDob(new Date());
        else
            olaVo.setDob(appUserModel.getDob());
        olaVo.setStatusId(1l);
        // olaVo.setBalance(dcm.getBalance()); //balance is yet to be decided
        BankModel bankModel = (BankModel) baseWrapper.getObject("bankModel");

        SwitchWrapper sWrapper = new SwitchWrapperImpl();
        sWrapper.setOlavo(olaVo);
        sWrapper.setBankId(bankModel.getBankId());

        try {

            sWrapper = olaVeriflyFinancialInstitution.createAccount(sWrapper);
        } catch (Exception e) {
            e.printStackTrace();

            throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
        }

        if ("07".equals(olaVo.getResponseCode())) {
            throw new FrameworkCheckedException("NIC already exisits in the OLA accounts");
        }

        olaVo = sWrapper.getOlavo();

        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setAccountNo(olaVo.getPayingAccNo().toString());
        accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
        accountInfoModel.setActive(smartMoneyAccountModel.getActive());

        //Added after HSM Integration
        accountInfoModel.setPan(PanGenerator.generatePAN());
        // End HSM Integration Change


        accountInfoModel.setCreatedOn(smartMoneyAccountModel.getCreatedOn());
        accountInfoModel.setUpdatedOn(smartMoneyAccountModel.getUpdatedOn());
        accountInfoModel.setCustomerId(customerModel.getCustomerId());

        accountInfoModel.setCustomerMobileNo(appUserModel
                .getMobileNo());
        accountInfoModel.setFirstName(appUserModel
                .getFirstName());
        accountInfoModel.setLastName(appUserModel
                .getLastName());
        accountInfoModel.setPaymentModeId(smartMoneyAccountModel
                .getPaymentModeId());
        accountInfoModel.setDeleted(Boolean.FALSE);
        accountInfoModel.setIsMigrated(1L);

        veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        // VeriflyManager veriflyMgr =
        // veriflyManagerService.getVeriflyMgrByBankId(bankModel);
        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        LogModel logmodel = new LogModel();
        logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
        veriflyBaseWrapper.setLogModel(logmodel);
        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,
                DeviceTypeConstantsInterface.WEB);
        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,
                DeviceTypeConstantsInterface.WEB);
        veriflyBaseWrapper.setSkipPanCheck(Boolean.TRUE);

        try {
            veriflyBaseWrapper = abstractFinancialInstitution.generatePin(veriflyBaseWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!veriflyBaseWrapper.isErrorStatus()) {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            if (null == veriflyBaseWrapper.getErrorMessage())
                veriflyErrorMessage = "Pay Shield Service is not available. Please try again later.";
            throw new FrameworkCheckedException(veriflyErrorMessage);
        }


        ///***************************************************************************************************
        ///***************************************************************************************************
        {
            /**
             * Sending SMS
             */

            //Updated against CRF-28.
            String messageString = null;
            String brandName = MessageUtil.getMessage("jsbl.brandName");

            AppInfoModel appInfoModel = new AppInfoModel();
            appInfoModel.setAppId(AppConstants.CONSUMER_APP);
            List<AppInfoModel> appInfoModelList = new ArrayList<>();
            appInfoModelList = appInfoDAO.findByExample(appInfoModel).getResultsetList();
            ArrayList<String> urls = new ArrayList<>();
            if (appInfoModel != null) {
                for (AppInfoModel model : appInfoModelList
                ) {
                    urls.add(model.getUrl());
                }
            }


            if (null == accountModel.getInitialDeposit()) {
                //Object[] args1 = {mfsId,MessageUtil.getMessage("mfsDownloadURL")};
                messageString = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_fonepay", new Object[]{username, randomPin}, null);
            } else {
                messageString = this.getMessageSource().getMessage("smsCommand.act_sms_jsbl_fonepay", new Object[]{username, randomPin}, null);
            }

            SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), messageString);
            smsSender.send(smsMessage);
        }

        //*************************************************************************************************************
        //*************************************************************************************************************


        ///////initiate IVR call to customer to generate user pin //////////
//		IvrRequestDTO ivrDTO = new IvrRequestDTO();
//		ivrDTO.setCustomerMobileNo(appUserModel.getMobileNo());
//		ivrDTO.setPin(veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin());
//		this.initiateUserGeneratedPinIvrCall(ivrDTO);
        /**
         * Logging Information, Ending Status
         */

        if (actionLogHandler == null) {
            actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
            actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END); // for process ends
            actionLogModel.setCustomField1(String.valueOf(appUserModel.getAppUserId()));
            actionLogModel.setCustomField11(appUserModel.getUsername());
            actionLogModel = logAction(actionLogModel);
        }

        baseWrapper.putObject(Level2AccountModel.LEVEL2_ACCOUNT_MODEL_KEY, accountModel);
        baseWrapper.putObject(PortalConstants.KEY_MFS_ACCOUNT_ID, username);
        baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID, appUserModel.getAppUserId());
        baseWrapper.putObject(PortalConstants.KEY_MFS_ID, mfsId);

        return baseWrapper;
    }

    /**
     * Method searches the userInfoListViewDAO
     */
    public SearchBaseWrapper searchUserInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        CustomList<UserInfoListViewModel>
                list = this.userInfoListViewDAO.findByExample((UserInfoListViewModel)
                        searchBaseWrapper.getBasePersistableModel(),
                searchBaseWrapper.getPagingHelperModel(),
                searchBaseWrapper.getSortingOrderMap(),
                searchBaseWrapper.getDateRangeHolderModelList());
        searchBaseWrapper.setCustomList(list);
        return searchBaseWrapper;
    }

    @Override
    public SearchBaseWrapper searchMinorUserInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        CustomList<MinorUserInfoListViewModel>
                list = this.minorUserInfoListViewDAO.findByExample((MinorUserInfoListViewModel)
                        searchBaseWrapper.getBasePersistableModel(),
                searchBaseWrapper.getPagingHelperModel(),
                searchBaseWrapper.getSortingOrderMap(),
                searchBaseWrapper.getDateRangeHolderModelList());
        searchBaseWrapper.setCustomList(list);
        return searchBaseWrapper;
    }

    @Override
    public BankModel getOlaBankMadal() {
        BankModel bankModel = new BankModel();
        bankModel.setFinancialIntegrationId(FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION);
        CustomList bankList = this.bankDAO.findByExample(bankModel);
        List bankL = bankList.getResultsetList();
        bankModel = (BankModel) bankL.get(0);
        return bankModel;
    }

    public AppUserModel getAppUserModelByPrimaryKey(Long appUserId) {

        return appUserDAO.findByPrimaryKey(appUserId);
    }

    @Override
    public AppUserModel getAppUserModelByMobileNumber(String mobileNo) {
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setMobileNo(mobileNo);
        return appUserDAO.loadAppUserByMobileAndTypeForCustomer(mobileNo);
    }

    /**
     * Method search the App_User on the bases of the primary key
     */
    public BaseWrapper searchAppUserByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        AppUserModel appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
        appUserModel = this.getAppUserModelByPrimaryKey(appUserModel.getAppUserId());
        CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
        try {
            if (customerModel != null) {
                if (customerModel.getCustomerIdCustomerAddressesModelList() != null) {
                    for (CustomerAddressesModel custAdd : customerModel.getCustomerIdCustomerAddressesModelList()) {
                        custAdd = this.customerAddressesDAO.findByPrimaryKey(custAdd.getCustomerAddressesId());
                        if (custAdd.getAddressId() != null) {
                            custAdd.setAddressIdAddressModel(this.addressDAO.findByPrimaryKey(custAdd.getAddressId()));
                        }
                    }
                }
                if (customerModel.getLanguageId() != null) {
                    customerModel.setLanguageIdLanguageModel(languageDAO.findByPrimaryKey(customerModel.getLanguageId()));
                }
                if (customerModel.getSegmentId() != null) {
                    customerModel.setSegmentIdSegmentModel(segmentDAO.findByPrimaryKey(customerModel.getSegmentId()));
                }
                if (customerModel.getCustomerTypeId() != null) {
                    customerModel.setCustomerTypeIdCustomerTypeModel(customerTypeDAO.findByPrimaryKey(customerModel.getCustomerTypeId()));
                }
                if (customerModel.getCustomerAccountTypeId() != null) {
                    customerModel.setCustomerAccountTypeIdCustomerAccountTypeModel(olaCustomerAccountTypeDao.findByPrimaryKey(customerModel.getCustomerAccountTypeId()));
                }
                if (customerModel.getFundSourceId() != null) {
                    customerModel.setFundSourceIdFundSourceModel(fundSourceDAO.findByPrimaryKey(customerModel.getFundSourceId()));
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        baseWrapper.setBasePersistableModel(appUserModel);
        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
        userDeviceAccountsModel = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel).getResultsetList().get(0);
        baseWrapper.putObject("userId", userDeviceAccountsModel.getUserId());
        return baseWrapper;
    }

    @Override
    public void logPrintAction(Long customerId) throws FrameworkCheckedException {
        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setActionId(PortalConstants.ACTION_RETRIEVE);
        actionLogModel.setUsecaseId(PortalConstants.PRINT_FORM_USECASE_ID);
        actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
        actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
        actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
        actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
        actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
        actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
        actionLogModel.setCustomField1(String.valueOf(customerId));
        actionLogModel = logAction(actionLogModel);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
    }

    @Override
    public void saveClsPendignAccount(BaseWrapper baseWrapper, I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO) throws FrameworkCheckedException {
        MfsAccountModel mfsAccountModel = (MfsAccountModel) baseWrapper.getObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY);
        AppUserModel appUserModel = this.appUserDAO.findByPrimaryKey(mfsAccountModel.getAppUserId());
        CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
        ClsPendingBlinkCustomerModel clsPendingBlinkCustomerModel = new ClsPendingBlinkCustomerModel();
        clsPendingBlinkCustomerModel.setMobileNo(mfsAccountModel.getMobileNo());
        clsPendingBlinkCustomerModel.setCnic(mfsAccountModel.getNic());
        clsPendingBlinkCustomerModel.setConsumerName(mfsAccountModel.getName());
        clsPendingBlinkCustomerModel.setFatherHusbandName(mfsAccountModel.getFatherHusbandName());
        clsPendingBlinkCustomerModel.setCustomerAccountTypeId(mfsAccountModel.getCustomerAccountTypeId());
        clsPendingBlinkCustomerModel.setGender(mfsAccountModel.getGender());
        clsPendingBlinkCustomerModel.setCnicIssuanceDate(mfsAccountModel.getCnicIssuanceDate());
        clsPendingBlinkCustomerModel.setDob(mfsAccountModel.getDob());
        clsPendingBlinkCustomerModel.setBirthPlace(mfsAccountModel.getBirthPlace());
        clsPendingBlinkCustomerModel.setMotherMaidenName(mfsAccountModel.getMotherMaidenName());
        clsPendingBlinkCustomerModel.setEmailAddress(mfsAccountModel.getEmail());
        clsPendingBlinkCustomerModel.setMailingAddress(mfsAccountModel.getPresentAddress());
        clsPendingBlinkCustomerModel.setPermanentAddress(mfsAccountModel.getPresentAddress());
        clsPendingBlinkCustomerModel.setPurposeOfAccount(mfsAccountModel.getAccountPurposeName());
        clsPendingBlinkCustomerModel.setSourceOfIncome(mfsAccountModel.getFundSourceStr());
        clsPendingBlinkCustomerModel.setExpectedMonthlyTurnOver(mfsAccountModel.getExpectedMonthlyTurnOver());
        clsPendingBlinkCustomerModel.setNextOfKin(mfsAccountModel.getNokName());
        clsPendingBlinkCustomerModel.setLatitude(mfsAccountModel.getLatitude());
        clsPendingBlinkCustomerModel.setLongitude(mfsAccountModel.getLongitude());
        clsPendingBlinkCustomerModel.setDualNationality(mfsAccountModel.getDualNationality());
        clsPendingBlinkCustomerModel.setUsCitizen(mfsAccountModel.getUsCitizen());
        clsPendingBlinkCustomerModel.setAccUpdate(1L);
        clsPendingBlinkCustomerModel.setCustomerId(customerModel.getCustomerId());
        clsPendingBlinkCustomerModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
        clsPendingBlinkCustomerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
        clsPendingBlinkCustomerModel.setUpdatedOn(new Date());
        clsPendingBlinkCustomerModel.setCreatedOn(new Date());
        clsPendingBlinkCustomerModel.setCaseStatus(i8SBSwitchControllerResponseVO.getCaseStatus());
        clsPendingBlinkCustomerModel.setCaseID(i8SBSwitchControllerResponseVO.getCaseId());
        clsPendingBlinkCustomerModel.setAppUserId(appUserModel.getAppUserId());
        clsPendingBlinkCustomerModel.setRiskLevelStatus(mfsAccountModel.getRiskLevel());
        clsPendingBlinkCustomerModel.setIsCompleted("0");
        getCommonCommandManager().createClsPendingBlinkCustomerModel(clsPendingBlinkCustomerModel);
        BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
        blinkCustomerModel.setCustomerId(clsPendingBlinkCustomerModel.getCustomerId());
        blinkCustomerModel = getCommonCommandManager().loadBlinkCustomerByMobileAndAccUpdate(mfsAccountModel.getMobileNo(), 1L);
        blinkCustomerModel.setUpdatedOn(new Date());
        blinkCustomerModel.setClsResponseCode("pending at cls");
        blinkCustomerModelDAO.saveOrUpdate(blinkCustomerModel);


    }

    /**
     * Method updates the MWallet Account
     */
    public BaseWrapper updateMfsAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        /**
         * Logging the information
         */
        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        MfsAccountModel mfsAccountModel = (MfsAccountModel) baseWrapper.getObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY);
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getObject("smartMoneyAccountModel");

        Date nowDate = new Date();
        BlinkCustomerLimitModel blinkCustomerLimitModel = new BlinkCustomerLimitModel();
        AppUserModel appUserModel = new AppUserModel();
        appUserModel = this.appUserDAO.findByPrimaryKey(mfsAccountModel.getAppUserId());


        if (smartMoneyAccountModel == null) {
            SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
            sma.setCustomerId(appUserModel.getCustomerId());
            sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            smartMoneyAccountModel = this.getSmartMoneyAccountByExample(sma);
        }
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
        if (mfsAccountModel.getUsecaseId() != PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {
            if (!mfsAccountModel.getNicExpiryDate().equals(appUserModel.getNicExpiryDate())) {
                //CNIC expiry date is changed.
                appUserModel.setCnicExpiryMsgSent(Boolean.FALSE);
            }
        }


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
            if (mfsAccountModel.getUsecaseId() != PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {
                if (mfsAccountModel.getProductCatalogId() != null) {
                    userDeviceAccountsModel.setProdCatalogId(mfsAccountModel.getProductCatalogId());
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
            // Setting Image
            try {
                File file = arbitraryResourceLoader.loadImage("images/no_photo_icon.png");
                /**
                 * ******************************************************************************************************
                 * Updated by Soofia Faruq
                 * Customer's Picture is migrated from CustomerModel to CustomerPictureModel
                 */
                CustomerPictureModel customerPictureModel = this.getCustomerPictureByTypeId(
                        PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());

                if (mfsAccountModel.getUsecaseId() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                    customerPictureModel = this.getCustomerPictureByTypeIdAndStatus(
                            PictureTypeConstants.CUSTOMER_PHOTO, customerModel.getCustomerId().longValue());
                }
                if (null != customerPictureModel && customerPictureModel.getPictureTypeId() != null) {

                    if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.CUSTOMER_PHOTO) {

                        if (null != mfsAccountModel.getCustomerPic() && mfsAccountModel.getCustomerPic().getSize() > 1) {
                            customerPictureModel.setPicture(mfsAccountModel.getCustomerPic().getBytes());
                        } else if (null != mfsAccountModel.getCustomerPicByte() && mfsAccountModel.getCustomerPicByte().length > 1) {
                            customerPictureModel.setPicture(mfsAccountModel.getCustomerPicByte());
                        }
                        customerPictureModel.setDiscrepant(mfsAccountModel.getCustomerPicDiscrepant());
                        customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        customerPictureModel.setUpdatedOn(nowDate);
                        customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
                    }

                    // commenting it out as now there is no requirement of these pictures due to AgentMATE changes

                    //**************************************************************************************************
			    		/*  customerPictureModel = this.getCustomerPictureByTypeId(
						    		  PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, customerModel.getCustomerId().longValue());

						    if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.TERMS_AND_CONDITIONS_COPY ) {

						    	 if (null!=mfsAccountModel.getTncPic() && mfsAccountModel.getTncPic().getSize()>1){
						    		 customerPictureModel.setPicture(mfsAccountModel.getTncPic().getBytes());
						    	 }else if (null!=mfsAccountModel.getTncPicByte() && mfsAccountModel.getTncPicByte().length>1){
				    				 customerPictureModel.setPicture(mfsAccountModel.getTncPicByte());
						    	 }
						    	 customerPictureModel.setDiscrepant(mfsAccountModel.getTncPicDiscrepant());
						    	 customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
						    	customerPictureModel.setUpdatedOn(nowDate);
						    	customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
					    	}

				    	customerPictureModel = this.getCustomerPictureByTypeId(
						    		  PictureTypeConstants.SIGNATURE_SNAPSHOT, customerModel.getCustomerId().longValue());

						    if (customerPictureModel!=null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SIGNATURE_SNAPSHOT )
						    {
						    	 if (null!=mfsAccountModel.getSignPic() && mfsAccountModel.getSignPic().getSize()>1)
						    	 {
						    		 customerPictureModel.setPicture(mfsAccountModel.getSignPic().getBytes());
						    	 }
						    	 else if (null!=mfsAccountModel.getSignPicByte() && mfsAccountModel.getSignPicByte().length>1)
						    	 {
				    				 customerPictureModel.setPicture(mfsAccountModel.getSignPicByte());
						    	 }
						    	 else if((null==mfsAccountModel.getSignPic() || null==mfsAccountModel.getSignPicByte()) && customerPictureModel.getPicture()==null)
						    	 {
						    		 customerPictureModel.setPicture(Files.readAllBytes(file.toPath()));
						    	 }
						    	 	customerPictureModel.setDiscrepant(mfsAccountModel.getSignPicDiscrepant());
						    		customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
						    		customerPictureModel.setUpdatedOn(nowDate);
						    		customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
					    	}
						    */

                    //**************************************************************************************************
                    if (mfsAccountModel.getUsecaseId() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                        customerPictureModel = this.getCustomerPictureByTypeIdAndStatus(
                                PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());
                    } else {
                        customerPictureModel = this.getCustomerPictureByTypeId(
                                PictureTypeConstants.ID_FRONT_SNAPSHOT, customerModel.getCustomerId().longValue());
                    }
                    if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_FRONT_SNAPSHOT) {
                        if (null != mfsAccountModel.getCnicFrontPic() && mfsAccountModel.getCnicFrontPic().getSize() > 1) {
                            customerPictureModel.setPicture(mfsAccountModel.getCnicFrontPic().getBytes());
                        } else if (null != mfsAccountModel.getCnicFrontPicByte() && mfsAccountModel.getCnicFrontPicByte().length > 1) {
                            customerPictureModel.setPicture(mfsAccountModel.getCnicFrontPicByte());
                        }
                        customerPictureModel.setDiscrepant(mfsAccountModel.getCnicFrontPicDiscrepant());
                        customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        customerPictureModel.setUpdatedOn(nowDate);
                        customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
                    }

                    if (mfsAccountModel.getUsecaseId() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {

                        customerPictureModel = this.getCustomerPictureByTypeIdAndStatus(
                                PictureTypeConstants.PARENT_CNIC_SNAPSHOT, customerModel.getCustomerId().longValue());

                        if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PARENT_CNIC_SNAPSHOT) {
                            if (null != mfsAccountModel.getParentCnicPic() && mfsAccountModel.getParentCnicPic().getSize() > 1) {
                                customerPictureModel.setPicture(mfsAccountModel.getParentCnicPic().getBytes());
                            } else if (null != mfsAccountModel.getParentCnicPicByte() && mfsAccountModel.getParentCnicPicByte().length > 1) {
                                customerPictureModel.setPicture(mfsAccountModel.getParentCnicPicByte());
                            }
                            customerPictureModel.setDiscrepant(mfsAccountModel.getParentCnicPicDiscrepant());
                            customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                            customerPictureModel.setUpdatedOn(nowDate);
                            customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
                        }

                        customerPictureModel = this.getCustomerPictureByTypeIdAndStatus(
                                PictureTypeConstants.B_FORM_SNAPSHOT, customerModel.getCustomerId().longValue());

                        if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.B_FORM_SNAPSHOT) {
                            if (null != mfsAccountModel.getbFormPic() && mfsAccountModel.getbFormPic().getSize() > 1) {
                                customerPictureModel.setPicture(mfsAccountModel.getbFormPic().getBytes());
                            } else if (null != mfsAccountModel.getbFormPicByte() && mfsAccountModel.getbFormPicByte().length > 1) {
                                customerPictureModel.setPicture(mfsAccountModel.getbFormPicByte());
                            }
                            customerPictureModel.setDiscrepant(mfsAccountModel.getbFormPicDiscrepant());
                            customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                            customerPictureModel.setUpdatedOn(nowDate);
                            customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
                        }

                        customerPictureModel = this.getCustomerPictureByTypeIdAndStatus(
                                PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());

                        if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT) {
                            if (null != mfsAccountModel.getParentCnicBackPic() && mfsAccountModel.getParentCnicBackPic().getSize() > 1) {
                                customerPictureModel.setPicture(mfsAccountModel.getbFormPic().getBytes());
                            } else if (null != mfsAccountModel.getParentCnicBackPicByte() && mfsAccountModel.getParentCnicBackPicByte().length > 1) {
                                customerPictureModel.setPicture(mfsAccountModel.getParentCnicBackPicByte());
                            }
                            customerPictureModel.setDiscrepant(mfsAccountModel.getParentCnicBackPicDiscrepant());
                            customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                            customerPictureModel.setUpdatedOn(nowDate);
                            customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
                        }
                    }

                    if (mfsAccountModel.getUsecaseId() != PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                        customerPictureModel = this.getCustomerPictureByTypeId(
                                PictureTypeConstants.TERMS_AND_CONDITIONS_COPY, customerModel.getCustomerId().longValue());
                        if (null != customerPictureModel && customerPictureModel.getPictureTypeId() != null) {

                            if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.TERMS_AND_CONDITIONS_COPY) {
                                if (null != mfsAccountModel.getTncPic() && mfsAccountModel.getTncPic().getSize() > 1) {
                                    customerPictureModel.setPicture(mfsAccountModel.getTncPic().getBytes());
                                } else if (null != mfsAccountModel.getTncPicByte() && mfsAccountModel.getTncPicByte().length > 1) {
                                    customerPictureModel.setPicture(mfsAccountModel.getTncPicByte());
                                }
                                customerPictureModel.setDiscrepant(mfsAccountModel.getTncPicDiscrepant());
                                customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                                customerPictureModel.setUpdatedOn(nowDate);
                                customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
                            }
                        }
                    }
                    //****************************************************************************

                    if (mfsAccountModel.getUsecaseId() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                        customerPictureModel = this.getCustomerPictureByTypeIdAndStatus(
                                PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());
                    } else {
                        customerPictureModel = this.getCustomerPictureByTypeId(
                                PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());
                    }
                    if (customerPictureModel != null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_BACK_SNAPSHOT) {
                        if (null != mfsAccountModel.getCnicBackPic() && mfsAccountModel.getCnicBackPic().getSize() > 1) {
                            customerPictureModel.setPicture(mfsAccountModel.getCnicBackPic().getBytes());
                        } else if (null != mfsAccountModel.getCnicBackPicByte() && mfsAccountModel.getCnicBackPicByte().length > 1) {
                            customerPictureModel.setPicture(mfsAccountModel.getCnicBackPicByte());
                        }
                        customerPictureModel.setDiscrepant(mfsAccountModel.getCnicBackPicDiscrepant());
                        customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        customerPictureModel.setUpdatedOn(nowDate);
                        customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
                    }

                    if (mfsAccountModel.getUsecaseId() != PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
                        //****************************************************************************
                        //Customer's Picture is migrated from BlinkCustomerPictureModel to CustomerPictureModel
                        if (mfsAccountModel.getUsecaseId() == PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {
                            BlinkCustomerPictureModel pictureModel = this.getBlinkCustomerPictureByTypeId(
                                    PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT, customerModel.getCustomerId().longValue());
                            if (pictureModel != null) {
                                if (pictureModel.getPictureTypeId().longValue() == PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT) {
                                    if (null != mfsAccountModel.getProofOfProfessionPic() && mfsAccountModel.getProofOfProfessionPic().getSize() > 1) {
                                        customerPictureModel.setPicture(mfsAccountModel.getProofOfProfessionPic().getBytes());
                                    } else if (null != mfsAccountModel.getProofOfProfessionPicByte() && mfsAccountModel.getProofOfProfessionPicByte().length > 1) {
                                        customerPictureModel.setPicture(mfsAccountModel.getProofOfProfessionPicByte());
                                    }
                                    customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                                    customerPictureModel.setUpdatedOn(nowDate);
                                    customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
                                }
                            }

                            pictureModel = this.getBlinkCustomerPictureByTypeId(PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT, customerModel.getCustomerId().longValue());
                            if (pictureModel != null) {
                                if (pictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT) {
                                    if (null != mfsAccountModel.getSourceOfIncomePic() && mfsAccountModel.getSourceOfIncomePic().getSize() > 1) {
                                        customerPictureModel.setPicture(mfsAccountModel.getSourceOfIncomePic().getBytes());
                                    } else if (null != mfsAccountModel.getSourceOfIncomeByte() && mfsAccountModel.getSourceOfIncomeByte().length > 1) {
                                        customerPictureModel.setPicture(mfsAccountModel.getSourceOfIncomeByte());
                                    }
                                    customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                                    customerPictureModel.setUpdatedOn(nowDate);
                                    customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
                                }
                            }

                            pictureModel = this.getBlinkCustomerPictureByTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT, customerModel.getCustomerId().longValue());
                            if (pictureModel != null) {
                                if (pictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SIGNATURE_SNAPSHOT) {
                                    if (null != mfsAccountModel.getSignPic() && mfsAccountModel.getSignPic().getSize() > 1) {
                                        customerPictureModel.setPicture(mfsAccountModel.getSignPic().getBytes());
                                    } else if (null != mfsAccountModel.getSignPicByte() && mfsAccountModel.getSignPicByte().length > 1) {
                                        customerPictureModel.setPicture(mfsAccountModel.getSignPicByte());
                                    }
                                    customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                                    customerPictureModel.setUpdatedOn(nowDate);
                                    customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
                                }
                            }
                        }
                    }

                    // commenting it out as now there is no requirement of these pictures due to AgentMATE changes
						   /* customerPictureModel = this.getCustomerPictureByTypeId(
						    		  PictureTypeConstants.ID_BACK_SNAPSHOT, customerModel.getCustomerId().longValue());

						    if (customerPictureModel!=null && customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_BACK_SNAPSHOT ) {
						    	 if (null!=mfsAccountModel.getCnicBackPic() && mfsAccountModel.getCnicBackPic().getSize()>1){
						    		 customerPictureModel.setPicture(mfsAccountModel.getCnicBackPic().getBytes());
						    	 }else if(null!=mfsAccountModel.getCnicBackPicByte() && mfsAccountModel.getCnicBackPicByte().length>1){
				    				 customerPictureModel.setPicture(mfsAccountModel.getCnicBackPicByte());
						    	 }
						    	 else if((null==mfsAccountModel.getCnicBackPic() || null==mfsAccountModel.getCnicBackPicByte()) && customerPictureModel.getPicture()==null)
						    	 {
						    		 customerPictureModel.setPicture(Files.readAllBytes(file.toPath()));
						    	 }
						    	 	customerPictureModel.setDiscrepant(mfsAccountModel.getCnicBackPicDiscrepant());
						    		customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
						    		customerPictureModel.setUpdatedOn(nowDate);
						    		customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
					    	}

						    if(mfsAccountModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)){
						    	customerPictureModel = this.getCustomerPictureByTypeId(
						    			PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT, customerModel.getCustomerId().longValue());

						    	if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT ) {
						    		if (null!=mfsAccountModel.getLevel1FormPic() && mfsAccountModel.getLevel1FormPic().getSize()>1){
						    			customerPictureModel.setPicture(mfsAccountModel.getLevel1FormPic().getBytes());
						    		}else if(null!=mfsAccountModel.getLevel1FormPicByte() && mfsAccountModel.getLevel1FormPicByte().length>1){
						    			customerPictureModel.setPicture(mfsAccountModel.getLevel1FormPicByte());
						    		}
						    		customerPictureModel.setDiscrepant(mfsAccountModel.getLevel1FormPicDiscrepant());
						    		customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
						    		customerPictureModel.setUpdatedOn(nowDate);
						    		customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
						    	}
						    }*/

                    /**
                     * End
                     * *********************************************************************************************************
                     */
                } else {
                    customerPictureModel = new CustomerPictureModel();
                    customerPictureModel.setCustomerId(customerModel.getCustomerId());
                    if (null != mfsAccountModel.getCustomerPic() && mfsAccountModel.getCustomerPic().getSize() > 1) {
                        customerPictureModel.setPicture(mfsAccountModel.getCustomerPic().getBytes());
                    } else if (null != mfsAccountModel.getCustomerPicByte() && mfsAccountModel.getCustomerPicByte().length > 1) {
                        customerPictureModel.setPicture(mfsAccountModel.getCustomerPicByte());
                    }

                    customerPictureModel.setPictureTypeId(PictureTypeConstants.CUSTOMER_PHOTO);
                    customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    customerPictureModel.setCreatedOn(nowDate);
                    customerPictureModel.setUpdatedOn(nowDate);
                    customerPictureModel.setDiscrepant(mfsAccountModel.getCustomerPicDiscrepant());
                    customerPictureDAO.saveOrUpdate(customerPictureModel);

                    customerPictureModel = new CustomerPictureModel();
                    customerPictureModel.setCustomerId(customerModel.getCustomerId());
                    if (null != mfsAccountModel.getTncPic() && mfsAccountModel.getTncPic().getSize() > 1)
                        customerPictureModel.setPicture(mfsAccountModel.getTncPic().getBytes());
                    else if (null != mfsAccountModel.getTncPicByte() && mfsAccountModel.getTncPicByte().length > 1)
                        customerPictureModel.setPicture(mfsAccountModel.getTncPicByte());
                    customerPictureModel.setPictureTypeId(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY);
                    customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    customerPictureModel.setCreatedOn(nowDate);
                    customerPictureModel.setUpdatedOn(nowDate);
                    customerPictureModel.setDiscrepant(mfsAccountModel.getTncPicDiscrepant());
                    customerPictureDAO.saveOrUpdate(customerPictureModel);

                    customerPictureModel = new CustomerPictureModel();
                    customerPictureModel.setCustomerId(customerModel.getCustomerId());
                    if (null != mfsAccountModel.getSignPic() && mfsAccountModel.getSignPic().getSize() > 1) {
                        customerPictureModel.setPicture(mfsAccountModel.getSignPic().getBytes());
                    } else if (null != mfsAccountModel.getSignPicByte() && mfsAccountModel.getSignPicByte().length > 1) {
                        customerPictureModel.setPicture(mfsAccountModel.getSignPicByte());
                    } else {
                        customerPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                    }
                    customerPictureModel.setPictureTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT);
                    customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    customerPictureModel.setCreatedOn(nowDate);
                    customerPictureModel.setUpdatedOn(nowDate);
                    customerPictureModel.setDiscrepant(mfsAccountModel.getSignPicDiscrepant());
                    customerPictureDAO.saveOrUpdate(customerPictureModel);

                    customerPictureModel = new CustomerPictureModel();
                    customerPictureModel.setCustomerId(customerModel.getCustomerId());
                    if (null != mfsAccountModel.getCnicFrontPic() && mfsAccountModel.getCnicFrontPic().getSize() > 1)
                        customerPictureModel.setPicture(mfsAccountModel.getCnicFrontPic().getBytes());
                    else if (null != mfsAccountModel.getCnicFrontPicByte() && mfsAccountModel.getCnicFrontPicByte().length > 1)
                        customerPictureModel.setPicture(mfsAccountModel.getCnicFrontPicByte());
                    customerPictureModel.setPictureTypeId(PictureTypeConstants.ID_FRONT_SNAPSHOT);
                    customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    customerPictureModel.setCreatedOn(nowDate);
                    customerPictureModel.setUpdatedOn(nowDate);
                    customerPictureModel.setDiscrepant(mfsAccountModel.getCnicFrontPicDiscrepant());
                    customerPictureDAO.saveOrUpdate(customerPictureModel);

                    customerPictureModel = new CustomerPictureModel();
                    customerPictureModel.setCustomerId(customerModel.getCustomerId());
                    if (null != mfsAccountModel.getCnicBackPic() && mfsAccountModel.getCnicBackPic().getSize() > 1) {
                        customerPictureModel.setPicture(mfsAccountModel.getCnicBackPic().getBytes());
                    } else if (null != mfsAccountModel.getCnicBackPicByte() && mfsAccountModel.getCnicBackPicByte().length > 1) {
                        customerPictureModel.setPicture(mfsAccountModel.getCnicBackPicByte());
                    } else {
                        customerPictureModel.setPicture(Files.readAllBytes(file.toPath()));
                    }

                    customerPictureModel.setPictureTypeId(PictureTypeConstants.ID_BACK_SNAPSHOT);
                    customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    customerPictureModel.setCreatedOn(nowDate);
                    customerPictureModel.setUpdatedOn(nowDate);
                    customerPictureModel.setDiscrepant(mfsAccountModel.getCnicBackPicDiscrepant());
                    customerPictureDAO.saveOrUpdate(customerPictureModel);

                    if (mfsAccountModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
                        customerPictureModel = new CustomerPictureModel();
                        customerPictureModel.setCustomerId(customerModel.getCustomerId());
                        if (null != mfsAccountModel.getLevel1FormPic() && mfsAccountModel.getLevel1FormPic().getSize() > 1)
                            customerPictureModel.setPicture(mfsAccountModel.getLevel1FormPic().getBytes());
                        else if (null != mfsAccountModel.getLevel1FormPicByte() && mfsAccountModel.getLevel1FormPicByte().length > 1)
                            customerPictureModel.setPicture(mfsAccountModel.getLevel1FormPicByte());
                        customerPictureModel.setPictureTypeId(PictureTypeConstants.AC_OPENING_FORM_SNAPSHOT);
                        customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        customerPictureModel.setCreatedOn(nowDate);
                        customerPictureModel.setUpdatedOn(nowDate);
                        customerPictureModel.setDiscrepant(mfsAccountModel.getLevel1FormPicDiscrepant());
                        customerPictureDAO.saveOrUpdate(customerPictureModel);
                    }
                    if (mfsAccountModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK)) {
                        customerPictureModel = new CustomerPictureModel();
                        customerPictureModel.setCustomerId(customerModel.getCustomerId());
                        if (null != mfsAccountModel.getProofOfProfessionPic() && mfsAccountModel.getProofOfProfessionPic().getSize() > 1)
                            customerPictureModel.setPicture(mfsAccountModel.getProofOfProfessionPic().getBytes());
                        else if (null != mfsAccountModel.getProofOfProfessionPicByte() && mfsAccountModel.getProofOfProfessionPicByte().length > 1)
                            customerPictureModel.setPicture(mfsAccountModel.getProofOfProfessionPicByte());
                        customerPictureModel.setPictureTypeId(PictureTypeConstants.PROOF_OF_PROFESSION_SNAPSHOT);
                        customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        customerPictureModel.setCreatedOn(nowDate);
                        customerPictureModel.setUpdatedOn(nowDate);
                        customerPictureModel.setDiscrepant(mfsAccountModel.getProofOfProfessionPicDiscrepant());
                        customerPictureDAO.saveOrUpdate(customerPictureModel);

                        customerPictureModel = new CustomerPictureModel();
                        customerPictureModel.setCustomerId(customerModel.getCustomerId());
                        if (null != mfsAccountModel.getSourceOfIncomePic() && mfsAccountModel.getSourceOfIncomePic().getSize() > 1)
                            customerPictureModel.setPicture(mfsAccountModel.getSourceOfIncomePic().getBytes());
                        else if (null != mfsAccountModel.getSourceOfIncomeByte() && mfsAccountModel.getSourceOfIncomeByte().length > 1)
                            customerPictureModel.setPicture(mfsAccountModel.getSourceOfIncomeByte());
                        customerPictureModel.setPictureTypeId(PictureTypeConstants.SOURCE_OF_INCOME_SNAPSHOT);
                        customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        customerPictureModel.setCreatedOn(nowDate);
                        customerPictureModel.setUpdatedOn(nowDate);
                        customerPictureModel.setDiscrepant(mfsAccountModel.getSourceOfIncomePicDiscrepant());
                        customerPictureDAO.saveOrUpdate(customerPictureModel);

                        customerPictureModel = new CustomerPictureModel();
                        customerPictureModel.setCustomerId(customerModel.getCustomerId());
                        if (null != mfsAccountModel.getSignPic() && mfsAccountModel.getSignPic().getSize() > 1)
                            customerPictureModel.setPicture(mfsAccountModel.getSignPic().getBytes());
                        else if (null != mfsAccountModel.getSignPicByte() && mfsAccountModel.getSignPicByte().length > 1)
                            customerPictureModel.setPicture(mfsAccountModel.getSignPicByte());
                        customerPictureModel.setPictureTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT);
                        customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                        customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        customerPictureModel.setCreatedOn(nowDate);
                        customerPictureModel.setUpdatedOn(nowDate);
                        customerPictureModel.setDiscrepant(mfsAccountModel.getSignPicDiscrepant());
                        customerPictureDAO.saveOrUpdate(customerPictureModel);
                    }


                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

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

            if (mfsAccountModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
                if (oldAccountTypeId.equals(CustomerAccountTypeConstants.LEVEL_1)) {
                    customerModel.setCustomerAccountTypeId(oldAccountTypeId);
                } else {
                    customerModel.setCustomerAccountTypeId(oldAccountTypeId);
                }
            } else {

                customerModel.setCustomerAccountTypeId(mfsAccountModel.getCustomerAccountTypeId());
            }
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
//            if(mfsAccountModel.getUsecaseId() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
//                customerModel.setCustPicMakerComments(mfsAccountModel.getCustPicMakerComments());
//                customerModel.setpNicPicMakerComments(mfsAccountModel.getpNicPicMakerComments());
//                customerModel.setbFormPicMakerComments(mfsAccountModel.getbFormPicMakerComments());
//                customerModel.setNicFrontPicMakerComments(mfsAccountModel.getNicFrontPicMakerComments());
//                customerModel.setNicBackPicMakerComments(mfsAccountModel.getNicBackPicMakerComments());
//                customerModel.setpNicBackPicMakerComments(mfsAccountModel.getpNicBackPicMakerComments());
//            }
            customerModel.setComments(mfsAccountModel.getComments());
            if (mfsAccountModel.getCustomerAccountTypeId() != 53) {
                //segment id update disable as per waqar-ul hassan and azher
//                customerModel.setSegmentId(mfsAccountModel.getSegmentId());
                customerModel.setTaxRegimeId(mfsAccountModel.getTaxRegimeId());
                customerModel.setFed(mfsAccountModel.getFed());
                customerModel.setCurrency(mfsAccountModel.getCurrency());
                customerModel.setVerisysDone(mfsAccountModel.getVerisysDone());
                customerModel.setScreeningPerformed(mfsAccountModel.getScreeningPerformed());
            }
            customerModel.setLatitude(mfsAccountModel.getLatitude());
            customerModel.setLongitude(mfsAccountModel.getLongitude());
            customerModel.setMonthlyTurnOver(mfsAccountModel.getExpectedMonthlyTurnOver());
            if (mfsAccountModel.getUsecaseId() != null && !mfsAccountModel.getUsecaseId().equals(PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID)) {
                customerModel.setClsResponseCode(mfsAccountModel.getClsResponseCode());
            }
            customerModel.setDualNationality(mfsAccountModel.getDualNationality());
            customerModel.setUsCitizen(mfsAccountModel.getUsCitizen());
            if (mfsAccountModel.getUsecaseId() != null && mfsAccountModel.getUsecaseId().equals(PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID)) {
                customerModel.setCustPicCheckerComments(mfsAccountModel.getCustPicCheckerComments());
                customerModel.setpNicPicCheckerComments(mfsAccountModel.getpNicPicCheckerComments());
                customerModel.setbFormPicCheckerComments(mfsAccountModel.getbFormPicCheckerComments());
                customerModel.setNicBackPicCheckerComments(mfsAccountModel.getNicBackPicCheckerComments());
                customerModel.setNicFrontPicCheckerComments(mfsAccountModel.getNicFrontPicCheckerComments());
                customerModel.setpNicBackPicCheckerComments(mfsAccountModel.getpNicBackPicCheckerComments());
                customerModel.setCustPicMakerComments(mfsAccountModel.getCustPicMakerComments());
                customerModel.setbFormPicMakerComments(mfsAccountModel.getbFormPicMakerComments());
                customerModel.setpNicPicMakerComments(mfsAccountModel.getpNicPicMakerComments());
                customerModel.setNicBackPicMakerComments(mfsAccountModel.getNicBackPicMakerComments());
                customerModel.setNicFrontPicMakerComments(mfsAccountModel.getNicFrontPicMakerComments());
                customerModel.setpNicBackPicMakerComments(mfsAccountModel.getpNicBackPicMakerComments());
            }
            /**
             * Saving the CustomerModel
             */
            baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(customerModel);
            baseWrapper = this.custTransManager.saveOrUpdate(baseWrapper);
        }

        /***
         * Updating Customer Source of Funds
         */
        CustomerFundSourceModel customerFundSourceModel = new CustomerFundSourceModel();
        if (mfsAccountModel.getUsecaseId() != PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {
            customerFundSourceModel.setCustomerId(customerModel.getCustomerId());
            List<CustomerFundSourceModel> list = this.customerFundSourceDAO.findByExample(customerFundSourceModel).getResultsetList();
            for (CustomerFundSourceModel customerFundSourceModel1 : list) {
                this.customerFundSourceDAO.delete(customerFundSourceModel1);
            }
        }
        if (mfsAccountModel.getUsecaseId() == PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {
            BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
            blinkCustomerModel.setCustomerId(customerModel.getCustomerId());
            blinkCustomerModel = getCommonCommandManager().loadBlinkCustomerByMobileAndAccUpdate(mfsAccountModel.getMobileNo(), 1L);
            if (blinkCustomerModel.getAccUpdate() == 1) {
                blinkCustomerModel.setAccUpdate(0L);
                blinkCustomerModel.setUpdatedOn(new Date());
                blinkCustomerModel.setCustomerAccountTypeId(53L);
                blinkCustomerModel.setRegistrationStatus(String.valueOf(mfsAccountModel.getRegistrationStateId()));
                blinkCustomerModel.setActionStatusId(mfsAccountModel.getActionId());
                blinkCustomerModelDAO.saveOrUpdate(blinkCustomerModel);
            }
        }
        if (mfsAccountModel.getFundsSourceId() != null) {
            for (String fundSourceId : mfsAccountModel.getFundsSourceId()) {
                customerFundSourceModel = new CustomerFundSourceModel();
                customerFundSourceModel.setCustomerId(customerModel.getCustomerId());
                customerFundSourceModel.setFundSourceId(Long.parseLong(fundSourceId));
                customerFundSourceDAO.saveOrUpdate(customerFundSourceModel);
            }
        }


        // Updating Address Fields
        boolean isNokAddressAlreadySaved = false;
        try {
            if (mfsAccountModel.getUsecaseId() != PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {
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
        if (mfsAccountModel.getUsecaseId() != PortalConstants.UPDATE_ACCOUNT_TO_BLINK_USECASE_ID) {
            appUserModel.setFiler(mfsAccountModel.getFiler());
            appUserModel.setAddress1(mfsAccountModel.getAddress1());
            appUserModel.setAddress2(mfsAccountModel.getAddress2());
            appUserModel.setNicExpiryDate(mfsAccountModel.getNicExpiryDate());
        }
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

        appUserModel.setAccountClosedUnsettled(mfsAccountModel.getAccountClosedUnsettled());
        if (mfsAccountModel.getClosedBy() != null) {
            appUserModel.setClosedByAppUserModel(this.appUserDAO.findByPrimaryKey(Long.valueOf(mfsAccountModel.getClosedBy())));
        }
        appUserModel.setClosedOn(mfsAccountModel.getClosedOn());
        appUserModel.setClosingComments(mfsAccountModel.getClosingComments());

        appUserModel.setAccountClosedSettled(mfsAccountModel.getAccountClosedSettled());
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

        if (mfsAccountModel.getUsecaseId() == PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID) {
            if (mfsAccountModel.getCustomerPicDiscrepant().equals(true) || mfsAccountModel.getCnicFrontPicDiscrepant().equals(true) ||
                    mfsAccountModel.getParentCnicPicDiscrepant().equals(true) || mfsAccountModel.getbFormPicDiscrepant().equals(true) ||
                    mfsAccountModel.getParentCnicBackPicDiscrepant().equals(true) || mfsAccountModel.getCnicBackPicDiscrepant().equals(true)) {
                smartMoneyAccountModel.setIsDebitBlocked(true);
                smartMoneyAccountModel.setDebitBlockAmount(99999999d);

                appUserModel.setRegistrationStateId(RegistrationStateConstants.DISCREPANT);
            } else {
                smartMoneyAccountModel.setIsDebitBlocked(false);
                smartMoneyAccountModel.setDebitBlockAmount(0d);
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

        String accountOpeningMethod = "";
        if (customerModel.getAccountMethodId() != null && !"".equals(customerModel.getAccountMethodId())) {
            if (customerModel.getAccountMethodId().equals(AccountOpeningMethodConstantsInterface.SELF_REGISTERATION)) {
                accountOpeningMethod = "Consumer App";
            } else if (customerModel.getAccountMethodId().equals(AccountOpeningMethodConstantsInterface.FONEPAY)) {
                accountOpeningMethod = "FonePay app";
            }
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


            //Settle Account Opening commission - if registration state is updated to VERIFIED
            if ((newRegistrationStateId != null && newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.VERIFIED)
                    && (oldRegistrationStateId == null || oldRegistrationStateId.longValue() != RegistrationStateConstantsInterface.VERIFIED)) {
                transactionIDForSAF = settleAccountOpeningCommission(customerModel.getCustomerId());
            }
        }


        Long checkBlink = mfsAccountModel.getCustomerAccountTypeId();
        if (checkBlink.equals(CustomerAccountTypeConstants.BLINK)) {
            Long accountId = accountDAO.getAccountIdByCnic(mfsAccountModel.getNic(), mfsAccountModel.getCustomerAccountTypeId());

            if (mfsAccountModel.getDailyCheck().equals(true) && mfsAccountModel.getDebitLimitDaily() != null) {
                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.DEBIT, LimitTypeConstants.DAILY);

                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getDebitLimitDaily()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
                    blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

                } else {
                    blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.DEBIT);
                    blinkCustomerLimitModel.setLimitType(LimitTypeConstants.DAILY);
                    blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                    blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setCreatedOn(nowDate);
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
                    blinkCustomerLimitModel.setMaximum(Long.valueOf(mfsAccountModel.getDebitLimitDaily()));
                    blinkCustomerLimitModel.setMinimum(1l);
                    blinkCustomerLimitModel.setIsApplicable(1l);
                    blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setAccountId(accountId);
                    blinkCustomerLimitModel.setUpdatedOn(nowDate);
                    blinkCustomerDAO.insertData(blinkCustomerLimitModel);
                }

            } else {

                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.DEBIT, LimitTypeConstants.DAILY);

                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getDebitLimitDaily()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
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
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
                    blinkCustomerLimitModel.setMaximum(blinkDefaultLimitModel.getMaximum().longValue());
                    blinkCustomerLimitModel.setMinimum(blinkDefaultLimitModel.getMinimum().longValue());
                    blinkCustomerLimitModel.setIsApplicable(1l);
                    blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setAccountId(accountId);
                    blinkCustomerLimitModel.setUpdatedOn(nowDate);
                    blinkCustomerDAO.insertData(blinkCustomerLimitModel);
                }

            }
            if (mfsAccountModel.getDailyCheck().equals(true) && mfsAccountModel.getCreditLimitDaily() != null) {
                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.CREDIT, LimitTypeConstants.DAILY);

                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getCreditLimitDaily()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
                    blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

                } else {
                    blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.CREDIT);
                    blinkCustomerLimitModel.setLimitType(LimitTypeConstants.DAILY);
                    blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                    blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setCreatedOn(nowDate);
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
                    blinkCustomerLimitModel.setMaximum(Long.valueOf(mfsAccountModel.getCreditLimitDaily()));
                    blinkCustomerLimitModel.setMinimum(1l);
                    blinkCustomerLimitModel.setIsApplicable(1l);
                    blinkCustomerLimitModel.setAccountId(accountId);
                    blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setUpdatedOn(nowDate);
                    blinkCustomerDAO.insertData(blinkCustomerLimitModel);
                }
            } else {

                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.CREDIT, LimitTypeConstants.DAILY);

                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getCreditLimitDaily()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
                    blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

                } else {
                    BlinkDefaultLimitModel blinkDefaultLimitModel = blinkDefaultLimitDAO.getLimitByTransactionType(mfsAccountModel.getCustomerAccountTypeId(), LimitTypeConstants.DAILY, TransactionTypeConstants.CREDIT);

                    blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.CREDIT);
                    blinkCustomerLimitModel.setLimitType(LimitTypeConstants.DAILY);
                    blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                    blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setCreatedOn(nowDate);
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
                    blinkCustomerLimitModel.setMaximum(blinkDefaultLimitModel.getMaximum().longValue());
                    blinkCustomerLimitModel.setMinimum(1l);
                    blinkCustomerLimitModel.setIsApplicable(1l);
                    blinkCustomerLimitModel.setAccountId(accountId);
                    blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setUpdatedOn(nowDate);
                    blinkCustomerDAO.insertData(blinkCustomerLimitModel);
                }

            }


            if (mfsAccountModel.getMonthlyCheck().equals(true) && mfsAccountModel.getDebitLimitMonthly() != null) {
                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.DEBIT, LimitTypeConstants.MONTHLY);
                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getDebitLimitMonthly()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
                    blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

                } else {
                    blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.DEBIT);
                    blinkCustomerLimitModel.setLimitType(LimitTypeConstants.MONTHLY);
                    blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                    blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setCreatedOn(nowDate);
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
                    blinkCustomerLimitModel.setMaximum(Long.valueOf(mfsAccountModel.getDebitLimitMonthly()));
                    blinkCustomerLimitModel.setMinimum(1l);
                    blinkCustomerLimitModel.setIsApplicable(1l);
                    blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setUpdatedOn(nowDate);
                    blinkCustomerLimitModel.setAccountId(accountId);
                    blinkCustomerDAO.insertData(blinkCustomerLimitModel);
                }
            } else {
                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.DEBIT, LimitTypeConstants.MONTHLY);
                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getDebitLimitMonthly()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
                    blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

                } else {
                    BlinkDefaultLimitModel blinkDefaultLimitModel = blinkDefaultLimitDAO.getLimitByTransactionType(mfsAccountModel.getCustomerAccountTypeId(), LimitTypeConstants.MONTHLY, TransactionTypeConstants.DEBIT);
                    blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.DEBIT);
                    blinkCustomerLimitModel.setLimitType(LimitTypeConstants.MONTHLY);
                    blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                    blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setCreatedOn(nowDate);
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
                    blinkCustomerLimitModel.setMaximum(blinkDefaultLimitModel.getMaximum().longValue());
                    blinkCustomerLimitModel.setMinimum(blinkDefaultLimitModel.getMinimum().longValue());
                    blinkCustomerLimitModel.setIsApplicable(1l);
                    blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setUpdatedOn(nowDate);
                    blinkCustomerLimitModel.setAccountId(accountId);
                    blinkCustomerDAO.insertData(blinkCustomerLimitModel);
                }
            }
            if (mfsAccountModel.getMonthlyCheck().equals(true) && mfsAccountModel.getCreditLimitMonthly() != null) {
                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.CREDIT, LimitTypeConstants.MONTHLY);
                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getCreditLimitMonthly()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
                    blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

                } else {
                    blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.CREDIT);
                    blinkCustomerLimitModel.setLimitType(LimitTypeConstants.MONTHLY);
                    blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                    blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setCreatedOn(nowDate);
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
                    blinkCustomerLimitModel.setMaximum(Long.valueOf(mfsAccountModel.getCreditLimitMonthly()));
                    blinkCustomerLimitModel.setMinimum(1l);
                    blinkCustomerLimitModel.setIsApplicable(1l);
                    blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setUpdatedOn(nowDate);
                    blinkCustomerLimitModel.setAccountId(accountId);
                    blinkCustomerDAO.insertData(blinkCustomerLimitModel);
                }
            } else {
                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.CREDIT, LimitTypeConstants.MONTHLY);
                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getCreditLimitMonthly()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
                    blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

                } else {
                    BlinkDefaultLimitModel blinkDefaultLimitModel = blinkDefaultLimitDAO.getLimitByTransactionType(mfsAccountModel.getCustomerAccountTypeId(), LimitTypeConstants.MONTHLY, TransactionTypeConstants.CREDIT);
                    blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.CREDIT);
                    blinkCustomerLimitModel.setLimitType(LimitTypeConstants.MONTHLY);
                    blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                    blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setCreatedOn(nowDate);
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
                    blinkCustomerLimitModel.setMaximum(blinkDefaultLimitModel.getMaximum().longValue());
                    blinkCustomerLimitModel.setMinimum(blinkDefaultLimitModel.getMinimum().longValue());
                    blinkCustomerLimitModel.setIsApplicable(1l);
                    blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setUpdatedOn(nowDate);
                    blinkCustomerLimitModel.setAccountId(accountId);
                    blinkCustomerDAO.insertData(blinkCustomerLimitModel);
                }
            }
            if (mfsAccountModel.getYearlyCheck().equals(true) && mfsAccountModel.getDebitLimitYearly() != null) {
                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.DEBIT, LimitTypeConstants.YEARLY);
                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getDebitLimitYearly()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
                    blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

                } else {
                    blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.DEBIT);
                    blinkCustomerLimitModel.setLimitType(LimitTypeConstants.YEARLY);
                    blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                    blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setCreatedOn(nowDate);
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
                    blinkCustomerLimitModel.setMaximum(Long.valueOf(mfsAccountModel.getDebitLimitYearly()));
                    blinkCustomerLimitModel.setMinimum(1l);
                    blinkCustomerLimitModel.setIsApplicable(1l);
                    blinkCustomerLimitModel.setAccountId(accountId);
                    blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setUpdatedOn(nowDate);
                    blinkCustomerDAO.insertData(blinkCustomerLimitModel);

                }
            } else {
                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.DEBIT, LimitTypeConstants.YEARLY);
                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getDebitLimitYearly()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
                    blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

                } else {
                    BlinkDefaultLimitModel blinkDefaultLimitModel = blinkDefaultLimitDAO.getLimitByTransactionType(mfsAccountModel.getCustomerAccountTypeId(), LimitTypeConstants.YEARLY, TransactionTypeConstants.DEBIT);
                    blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.DEBIT);
                    blinkCustomerLimitModel.setLimitType(LimitTypeConstants.YEARLY);
                    blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                    blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setCreatedOn(nowDate);
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
                    blinkCustomerLimitModel.setMaximum(blinkDefaultLimitModel.getMaximum().longValue());
                    blinkCustomerLimitModel.setMinimum(1l);
                    blinkCustomerLimitModel.setIsApplicable(1l);
                    blinkCustomerLimitModel.setAccountId(accountId);
                    blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setUpdatedOn(nowDate);
                    blinkCustomerDAO.insertData(blinkCustomerLimitModel);

                }

            }
            if (mfsAccountModel.getYearlyCheck().equals(true) && mfsAccountModel.getCreditLimitYearly() != null) {
                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.CREDIT, LimitTypeConstants.YEARLY);
                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getCreditLimitYearly()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
                    blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

                } else {
                    blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.CREDIT);
                    blinkCustomerLimitModel.setLimitType(LimitTypeConstants.YEARLY);
                    blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                    blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setCreatedOn(nowDate);
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
                    blinkCustomerLimitModel.setMaximum(Long.valueOf(mfsAccountModel.getCreditLimitYearly()));
                    blinkCustomerLimitModel.setMinimum(1l);
                    blinkCustomerLimitModel.setIsApplicable(1l);
                    blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setUpdatedOn(nowDate);
                    blinkCustomerLimitModel.setAccountId(accountId);
                    blinkCustomerDAO.insertData(blinkCustomerLimitModel);
                }

            } else {
                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.CREDIT, LimitTypeConstants.YEARLY);
                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getCreditLimitYearly()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
                    blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

                } else {
                    BlinkDefaultLimitModel blinkDefaultLimitModel = blinkDefaultLimitDAO.getLimitByTransactionType(mfsAccountModel.getCustomerAccountTypeId(), LimitTypeConstants.YEARLY, TransactionTypeConstants.CREDIT);
                    blinkCustomerLimitModel.setTransactionType(TransactionTypeConstants.CREDIT);
                    blinkCustomerLimitModel.setLimitType(LimitTypeConstants.YEARLY);
                    blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                    blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setCreatedOn(nowDate);
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
                    blinkCustomerLimitModel.setMaximum(blinkDefaultLimitModel.getMaximum().longValue());
                    blinkCustomerLimitModel.setMinimum(1l);
                    blinkCustomerLimitModel.setIsApplicable(1l);
                    blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setUpdatedOn(nowDate);
                    blinkCustomerLimitModel.setAccountId(accountId);
                    blinkCustomerDAO.insertData(blinkCustomerLimitModel);
                }
            }

            if (mfsAccountModel.getMaximumBalanceCheck().equals(true) && mfsAccountModel.getMaximumCreditLimit() != null) {
                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.CREDIT, LimitTypeConstants.MAXIMUM);
                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getMaximumCreditLimit()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
                    blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

                } else {

                    blinkCustomerLimitModel.setTransactionType(2l);
                    blinkCustomerLimitModel.setLimitType(LimitTypeConstants.MAXIMUM);
                    blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                    blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setCreatedOn(nowDate);
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
                    blinkCustomerLimitModel.setMaximum(Long.valueOf(mfsAccountModel.getMaximumCreditLimit()));
                    blinkCustomerLimitModel.setMinimum(1l);
                    blinkCustomerLimitModel.setIsApplicable(1l);
                    blinkCustomerLimitModel.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setUpdatedOn(nowDate);
                    blinkCustomerLimitModel.setAccountId(accountId);
                    blinkCustomerDAO.insertData(blinkCustomerLimitModel);
                }

            } else {

                BlinkCustomerLimitModel blinkCustomerLimitModels = blinkCustomerDAO.getLimitsByCustomerAccountType(mfsAccountModel.getCustomerAccountTypeId(), accountId, TransactionTypeConstants.CREDIT, LimitTypeConstants.MAXIMUM);
                if (blinkCustomerLimitModels != null) {
                    blinkCustomerLimitModels.setMaximum(Long.valueOf(mfsAccountModel.getMaximumCreditLimit()));
                    blinkCustomerLimitModel.setUpdatedOn(new Date());
                    blinkCustomerLimitModels.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModels.setUpdatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerDAO.saveOrUpdate(blinkCustomerLimitModels);

                } else {
                    BlinkDefaultLimitModel blinkDefaultLimitModel = blinkDefaultLimitDAO.getLimitByTransactionType(mfsAccountModel.getCustomerAccountTypeId(), LimitTypeConstants.MAXIMUM, TransactionTypeConstants.CREDIT);

                    blinkCustomerLimitModel.setTransactionType(2l);
                    blinkCustomerLimitModel.setLimitType(LimitTypeConstants.MAXIMUM);
                    blinkCustomerLimitModel.setCustomerId(Long.valueOf(appUserModel.getCustomerId()));
                    blinkCustomerLimitModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                    blinkCustomerLimitModel.setCreatedOn(nowDate);
                    blinkCustomerLimitModel.setCustomerAccTypeId(checkBlink);
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

        /**
         * Logging the information process ends
         */
        actionLogModel.setCustomField1(appUserModel.getAppUserId().toString());
        actionLogModel.setCustomField11(appUserModel.getUsername());
        this.actionLogManager.completeActionLog(actionLogModel);

        baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(appUserModel);

        //used in SAF Repo
        baseWrapper.putObject(PortalConstants.ACC_OPENING_COMM_TRANSACTION_ID, transactionIDForSAF);

        return baseWrapper;
    }

    @Override
    public BaseWrapper updateMfsDebitCard(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        /**
         * Logging the information
         */
        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        MfsDebitCardModel mfsDebitCardModel = (MfsDebitCardModel) baseWrapper.getObject(MfsDebitCardModel.MFS_DEBIT_CARD_MODEL_KEY);
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getObject("smartMoneyAccountModel");

        Date nowDate = new Date();
        BlinkCustomerLimitModel blinkCustomerLimitModel = new BlinkCustomerLimitModel();
        AppUserModel appUserModel = new AppUserModel();
        appUserModel = this.appUserDAO.findByPrimaryKey(mfsDebitCardModel.getAppUserId());


        if (smartMoneyAccountModel == null) {
            SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
            sma.setCustomerId(appUserModel.getCustomerId());
            sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            smartMoneyAccountModel = this.getSmartMoneyAccountByExample(sma);
        }
        String oldFirstName = appUserModel.getFirstName();
        String oldLastName = appUserModel.getLastName();
        String[] nameArray = mfsDebitCardModel.getNadraName().split(" ");
        appUserModel.setFirstName(nameArray[0]);

        if (nameArray.length > 1) {
            appUserModel.setLastName(mfsDebitCardModel.getNadraName().substring(appUserModel.getFirstName().length() + 1));
        } else {
            appUserModel.setLastName(nameArray[0]);
        }
        Long oldRegistrationStateId = appUserModel.getRegistrationStateId();


        Long oldAccountTypeId = null;

        boolean OLA_ACCOUNT_INFO_CHANGE_FLAG = false;

        /**
         * Checking if mobile Number is unique
         */
        boolean MOBILE_NUM_CHANGE_FLAG = false;
        if (!appUserModel.getMobileNo().equals(mfsDebitCardModel.getMobileNo())) {
            MOBILE_NUM_CHANGE_FLAG = true;
        }

        /**
         * Checking if CNIC is unique
         */
        boolean NIC_CHANGE_FLAG = false;
        String withoutDashesNIC = (mfsDebitCardModel.getCnic() != null) ? mfsDebitCardModel.getCnic().replace("-", "") : "";
        if (!appUserModel.getNic().equals(withoutDashesNIC)) {
            NIC_CHANGE_FLAG = true;
        }

        if (MOBILE_NUM_CHANGE_FLAG) {
            if (!this.isMobileNumUnique(mfsDebitCardModel.getMobileNo(), mfsDebitCardModel.getAppUserId())) {
                throw new FrameworkCheckedException("MobileNumUniqueException");
            }
        }

        if (NIC_CHANGE_FLAG) {
            if (!this.isNICUnique(mfsDebitCardModel.getCnic(), mfsDebitCardModel.getAppUserId())) {
                throw new FrameworkCheckedException("NICUniqueException");
            }
        }


        /**
         * Populating the First/Last Name
         */
        if (null != mfsDebitCardModel.getFirstName())
            appUserModel.setFirstName(mfsDebitCardModel.getFirstName());

        if (null != mfsDebitCardModel.getLastName())
            appUserModel.setLastName(mfsDebitCardModel.getLastName());

        if (!oldFirstName.equals(appUserModel.getFirstName()) || !oldLastName.equals(appUserModel.getLastName())) {
            OLA_ACCOUNT_INFO_CHANGE_FLAG = true;
        }

        CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
        if (customerModel != null) {

            customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            customerModel.setUpdatedOn(new Date());
            customerModel.setName(mfsDebitCardModel.getNadraName());

            customerModel.setMobileNo(mfsDebitCardModel.getMobileNo());

//            customerModel.setSegmentId(mfsDebitCardModel.getSegmentId());
            /**
             * Saving the CustomerModel
             */
            baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(customerModel);
            baseWrapper = this.custTransManager.saveOrUpdate(baseWrapper);
        }

        // Updating Address Fields
        try {
            boolean isNokAddressAlreadySaved = false;

            Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
            if (customerAddresses != null && customerAddresses.size() > 0) {
                for (CustomerAddressesModel custAdd : customerAddresses) {
                    AddressModel addressModel = custAdd.getAddressIdAddressModel();
                    if (custAdd.getAddressTypeId() == 1) {
                        if (mfsDebitCardModel.getCity() != null) {
                            if (mfsDebitCardModel.getCity().equals("Islamabad")){
                                addressModel.setCityId(271L);

                            }else {
                                addressModel.setCityId(109L);

                            }
                        }
                        this.addressDAO.saveOrUpdate(addressModel);
                    }
                }
            }

//            DebitCardMailingAddressModel debitCardMailingAddressModel = new DebitCardMailingAddressModel();
////            debitCardMailingAddressModel.setMailingAddressId(mfsDebitCardModel.getMailingAddressId());
            Criterion criteriaOne = Restrictions.eq("mailingAddressId", mfsDebitCardModel.getMailingAddressId());
            List<DebitCardMailingAddressModel> debitCardMailingAddressModelList =
                    this.getCommonCommandManager().getDebitCardMailingAddressDAO().findByCriteria(criteriaOne).getResultsetList();
            // Populating Address Fields
            DebitCardMailingAddressModel addressModel = debitCardMailingAddressModelList.get(0);

            addressModel.setMailingAddress(mfsDebitCardModel.getMailingAddress());
            addressModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            addressModel.setUpdatedOn(new Date());
            baseWrapper.putObject(CommandFieldConstants.KEY_DEBIT_CARD_MAILING_ADDRESS_MODEL, addressModel);

            addressModel = debitCardMailingAddressDAO.saveOrUpdate(addressModel);


            appUserModel.setNic(mfsDebitCardModel.getCnic());
            appUserModel.setMobileNo(mfsDebitCardModel.getMobileNo());
            nameArray = mfsDebitCardModel.getNadraName().split(" ");
            appUserModel.setFirstName(nameArray[0]);
            if (nameArray.length > 1) {
                appUserModel.setLastName(mfsDebitCardModel.getNadraName().substring(appUserModel.getFirstName().length() + 1));
            } else {
                appUserModel.setLastName(nameArray[0]);
            }
            appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            appUserModel.setUpdatedOn(nowDate);

            DebitCardModel debitCardModel = this.getCommonCommandManager().getDebitCardModelDao().getDebitCradModelByNicAndState
                    (mfsDebitCardModel.getCnic(), CardConstantsInterface.CARD_STATUS_IN_PROCESS);

            debitCardModel.setDebitCardEmbosingName(mfsDebitCardModel.getEmbossingName());
            debitCardModel.setCardNo(mfsDebitCardModel.getCardNumber());
            debitCardModel.setMobileNo(mfsDebitCardModel.getMobileNo());
            debitCardModel.setCnic(mfsDebitCardModel.getCnic());
//            debitCardModel.setCardStateId(mfsDebitCardModel.getCardStateId());
//            debitCardModel.setCardStatusId(mfsDebitCardModel.getCardStatusId());
            debitCardModel.setMailingAddressId(mfsDebitCardModel.getMailingAddressId());
            if (mfsDebitCardModel.getWithAuthFlag() == null) {
                debitCardModel.setCheckedById(UserUtils.getCurrentUser().getAppUserId());
                debitCardModel.setCheckedByName(UserUtils.getCurrentUser().getFirstName());
            }
//            debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_APPROVED);
//            debitCardModel.setIsApproved("1");
            debitCardModel.setUpdatedOn(new Date());

//            if (debitCardModel.getReissuance() != null && !debitCardModel.getReissuance().equals("")) {
//                if (debitCardModel.getReissuance().equals("1")) {
//                    I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForDebitCardReissuance
//                            (I8SBConstants.RequestType_JSDEBITCARD_Import_Card);
//                    requestVO.setCardNumber(debitCardModel.getCardNo());
//                    requestVO.setRelationshipNumber(debitCardModel.getCnic());
//                    requestVO.setMobileNumber(debitCardModel.getMobileNo());
//                    requestVO.setCardEmborsingName(debitCardModel.getDebitCardEmbosingName());
//                    requestVO.setCardBranchCode(MessageUtil.getMessage("debit.card.branch.code"));
//                    requestVO.setIssuedDate(String.valueOf(debitCardModel.getIssuanceDate()));
//                    requestVO.setExpiryDate(String.valueOf(debitCardModel.getExpiryDate()));
//
//                    long smaId = debitCardModel.getSmartMoneyAccountId();
//
//                    SmartMoneyAccountModel sma = smartMoneyAccountDAO.findByPrimaryKey(smaId);
//
//                    CardProdCodeModel cardProdCodeModel = fetchCardTypeDAO.findByPrimaryKey(sma.getCardProdId());
//
//                    requestVO.setCardTypeCode(cardProdCodeModel.getCardTypeCode());
//                    requestVO.setCardProdCode(cardProdCodeModel.getCardProductCode());
//                    requestVO.setRequestId(CardConstantsInterface.DEBIT_CARD_ONLINE_RQST_TYPE_CODE);
//
//                    SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
//
//                    i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//                    requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();
//                    I8SBSwitchControllerResponseVO responseVO = requestVO.getI8SBSwitchControllerResponseVO();
//                    i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
//                    responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();
//                    ESBAdapter.processI8sbResponseCode(responseVO, false);
//                }
//            }
            debitCardModel = this.getCommonCommandManager().getDebitCardModelDao().saveOrUpdate(debitCardModel);

            DebitCardRequestsViewModel debitCardRequestsViewModel = new DebitCardRequestsViewModel();
            debitCardRequestsViewModel = debitCardRequestsViewModelDAO.loadDebitCardRequestsByAppUserId(debitCardModel.getAppUserId(),
                    debitCardModel.getMobileNo());
            debitCardRequestsViewModel.setSegmentName(mfsDebitCardModel.getSegmentName());
            debitCardRequestsViewModel.setCardProductCodeId(mfsDebitCardModel.getCardProductCodeId());
            debitCardRequestsViewModel.setCardStateId(mfsDebitCardModel.getCardStateId());
            debitCardRequestsViewModel = debitCardRequestsViewModelDAO.saveOrUpdate(debitCardRequestsViewModel);

            smartMoneyAccountModel.setCardProdId(mfsDebitCardModel.getCardProductCodeId());

//        smartMoneyAccountModel.setCardTypeProdIdCardProdCodeModel(mfsDebitCardModel.getCardTypeCode());
            smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);
            appUserModel = this.appUserDAO.saveOrUpdate(appUserModel);

        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(e.getMessage(), e);
        }
        /**
         * Logging the information process ends
         */
        actionLogModel.setCustomField1(appUserModel.getAppUserId().toString());
        actionLogModel.setCustomField11(appUserModel.getUsername());
        this.actionLogManager.completeActionLog(actionLogModel);

        baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(appUserModel);

        return baseWrapper;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BaseWrapper updateLevel2Account(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        /**
         * Logging the information
         */
        ActionLogModel actionLogModel = new ActionLogModel();
        Long actionAuthId = (Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_AUTH_ID);
        actionLogModel.setActionAuthorizationId(actionAuthId);
        actionLogModel.setActionId((Long) baseWrapper.getObject("actionId"));
        actionLogModel.setUsecaseId((Long) baseWrapper.getObject("usecaseId"));
        actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
        actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
        actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
        actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
        actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
        actionLogModel = logAction(actionLogModel);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        Level2AccountModel accountModel =
                (Level2AccountModel) baseWrapper.getObject(Level2AccountModel.LEVEL2_ACCOUNT_MODEL_KEY);

        Date nowDate = new Date();
        AppUserModel appUserModel = new AppUserModel();
        appUserModel = this.appUserDAO.findByPrimaryKey(accountModel.getAppUserId());
        String oldFirstName = appUserModel.getFirstName();
        String oldLastName = appUserModel.getLastName();

        String[] nameArray = accountModel.getApplicant1DetailModel().getName().split(" ");
        appUserModel.setFirstName(nameArray[0]);
        if (nameArray.length > 1) {
            appUserModel.setLastName(accountModel.getApplicant1DetailModel().getName().substring(appUserModel.getFirstName().length() + 1));
        } else {
            appUserModel.setLastName(nameArray[0]);
        }
        Long oldRegistrationStateId = appUserModel.getRegistrationStateId();

        if (accountModel.getRegistrationStateId() != null && accountModel.getRegistrationStateId().equals(3L)) {
            UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
            userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
            CustomList<UserDeviceAccountsModel> userDeviceAccountsModelList = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel);
            userDeviceAccountsModel = userDeviceAccountsModelList.getResultsetList().get(0);
            userDeviceAccountsModel.setAccountEnabled(Boolean.TRUE);
            userDeviceAccountsModel.setProdCatalogId(accountModel.getProductCatalogId());
            this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
        }

        String oldMobileNo = appUserModel.getMobileNo();
        Long dbMobileType = appUserModel.getMobileTypeId();

        Long oldAccountTypeId = null;

        boolean OLA_ACCOUNT_INFO_CHANGE_FLAG = false;

        /**
         * Checking if mobile Number is unique
         */
        boolean MOBILE_NUM_CHANGE_FLAG = false;
        if (!appUserModel.getMobileNo().equals(accountModel.getMobileNo())) {
            MOBILE_NUM_CHANGE_FLAG = true;
        }

        /**
         * Checking if CNIC is unique
         */


        if (MOBILE_NUM_CHANGE_FLAG) {
            if (!this.isMobileNumUnique(accountModel.getMobileNo(), accountModel.getAppUserId())) {
                throw new FrameworkCheckedException("MobileNumUniqueException");
            }
        }

        if (!oldFirstName.equals(appUserModel.getFirstName()) || !oldLastName.equals(appUserModel.getLastName())) {
            OLA_ACCOUNT_INFO_CHANGE_FLAG = true;
        }
        BankModel bankModel = getOlaBankMadal();


        CustomerModel customerModel = appUserModel.getCustomerIdCustomerModel();
        if (customerModel != null) {
            // Setting Image

            oldAccountTypeId = customerModel.getCustomerAccountTypeId();

            customerModel.setCustomerAccountTypeId(accountModel.getCustomerAccountTypeId());

            if (!oldAccountTypeId.equals(customerModel.getCustomerAccountTypeId())) {
                OLA_ACCOUNT_INFO_CHANGE_FLAG = true;
            }
            customerModel.setInitialApplicationFormNumber(accountModel.getInitialAppFormNo());
            customerModel.setFed(accountModel.getFed());
            customerModel.setAcNature(accountModel.getAcNature());
            customerModel.setTaxRegimeId(accountModel.getTaxRegimeId());
            customerModel.setVerisysDone(accountModel.getIsVeriSysDone());
            customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            customerModel.setName(accountModel.getCustomerAccountName());
            customerModel.setCustomerAccountTypeId(accountModel.getCustomerAccountTypeId());
            customerModel.setBusinessTypeId(accountModel.getBusinessTypeId());
            customerModel.setMobileNo(accountModel.getMobileNo());
            customerModel.setSegmentId(accountModel.getSegmentId());
            customerModel.setCurrency(accountModel.getCurrency());
            customerModel.setGender(accountModel.getApplicant1DetailModel().getGender());
            customerModel.setCustomerTypeId(accountModel.getCustomerTypeId());
            customerModel.setFundSourceId(accountModel.getFundsSourceId());
            customerModel.setFatherHusbandName(accountModel.getApplicant1DetailModel().getFatherHusbandName());
            customerModel.setFundsSourceNarration(accountModel.getFundSourceNarration());
            customerModel.setReferringName1(accountModel.getRefferedBy());
            customerModel.setNokName(accountModel.getNokDetailVOModel().getNokName());
            customerModel.setNokContactNo(accountModel.getNokDetailVOModel().getNokContactNo());
            customerModel.setNokRelationship(accountModel.getNokDetailVOModel().getNokRelationship());
            customerModel.setNokMobile(accountModel.getNokDetailVOModel().getNokMobile());
            customerModel.setNokIdType(accountModel.getNokDetailVOModel().getNokIdType());
            customerModel.setNokIdNumber(accountModel.getNokDetailVOModel().getNokIdNumber());
            customerModel.setBirthPlace(accountModel.getApplicant1DetailModel().getBirthPlace());
            customerModel.setEmail(accountModel.getApplicant1DetailModel().getEmail());
            customerModel.setFax(accountModel.getApplicant1DetailModel().getFax());
            customerModel.setLandLineNo(accountModel.getApplicant1DetailModel().getLandLineNo());
            customerModel.setContactNo(accountModel.getApplicant1DetailModel().getContactNo());
            customerModel.setPublicFigure(accountModel.getPublicFigure());
            customerModel.setTransactionModeId(accountModel.getTransactionModeId());
            customerModel.setAccountPurposeId(accountModel.getAccountPurposeId());
            customerModel.setRegStateComments(accountModel.getRegStateComments());
            customerModel.setComments(accountModel.getComments());
            customerModel.setReferringName1(accountModel.getRefferedBy());
            customerModel.setOtherTransactionMode(accountModel.getOtherTransactionMode());
            customerModel.setSalary(accountModel.getSalary());
            customerModel.setBusinessIncome(accountModel.getBuisnessIncome());
            customerModel.setOtherIncome(accountModel.getOtherIncome());
            customerModel.setOtherBankName(accountModel.getOtherBankName());
            customerModel.setOtherBankAddress(accountModel.getOtherBankAddress());
            customerModel.setOtherBankACNo(accountModel.getOtherBankACNo());
            customerModel.setScreeningPerformed(accountModel.getScreeningPerformed());
//            if (accountModel.getCustomerAccountTypeId().equals(53l)) {
//                customerModel.setAccountUpdate(false);
//            }

			/*customerModel.setSalesTaxRegNo(accountModel.getAdditionalDetailVOModel().getSalesTaxRegNo());
			customerModel.setMembershipNoTradeBody(accountModel.getAdditionalDetailVOModel().getMembershipNoTradeBody());
			customerModel.setIncorporationDate(accountModel.getAdditionalDetailVOModel().getIncorporationDate());
			customerModel.setSecpRegNo(accountModel.getAdditionalDetailVOModel().getSecpRegNo());*/
            /*added by atif hussain*/
//			customerModel.setRegistrationPlace(accountModel.getAdditionalDetailVOModel().getRegistrationPlace());
            /*added by atif hussain*/
            customerModel.setScreeningPerformed(accountModel.getScreeningPerformed());

            if (accountModel.getModeOfTx() != null && !accountModel.getModeOfTx().equals(""))
                customerModel.setTransactionModeId(Long.parseLong(accountModel.getModeOfTx()));

			/*accountModel.getBusinessDetailModel().setCustomerId(customerModel.getCustomerId());
			this.businessDetailDAO.saveOrUpdate(accountModel.getBusinessDetailModel());*/

            accountModel.getApplicant1DetailModel().setCustomerId(customerModel.getCustomerId());
            accountModel.getApplicant1DetailModel().setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            accountModel.getApplicant1DetailModel().setUpdatedOn(nowDate);
            ApplicantDetailModel applicant1DetailModel = this.applicantDetailDAO.saveOrUpdate(accountModel.getApplicant1DetailModel());

            //commenting below image saving, as this is no more required by JS
				/*List<CustomerPictureModel> customer1PictureList = this.searchAllCustomerPictures(customerModel.getCustomerId().longValue(), ApplicantTypeConstants.APPLICANT_TYPE_1, accountModel.getApplicant1DetailModel().getApplicantDetailId());

				for(CustomerPictureModel customerPictureModel : customer1PictureList) {
					if(customerPictureModel.getApplicantTypeId().longValue() == ApplicantTypeConstants.APPLICANT_TYPE_1) {
						try{
			    		  if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.CUSTOMER_PHOTO && customerPictureModel.getApplicantDetailId().equals(accountModel.getApplicant1DetailModel().getApplicantDetailId())) {
			    			  if (null!= accountModel.getApplicant1DetailModel().getFatcaFormPic() && accountModel.getApplicant1DetailModel().getFatcaFormPic().getSize()>1)
			    				  customerPictureModel.setPicture(accountModel.getApplicant1DetailModel().getFatcaFormPic().getBytes());
			    			  else if(null!= accountModel.getApplicant1DetailModel().getFatcaFormPicByte() &&  accountModel.getApplicant1DetailModel().getFatcaFormPicByte().length>1)
			    				  customerPictureModel.setPicture(accountModel.getApplicant1DetailModel().getFatcaFormPicByte());
			    			  customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			    			  customerPictureModel.setUpdatedOn(nowDate);
			    			  customerPictureModel.setApplicantDetailId(applicant1DetailModel.getApplicantDetailId());
			    			  customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
		    			  }
			    		  else if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.TERMS_AND_CONDITIONS_COPY && customerPictureModel.getApplicantDetailId().equals(accountModel.getApplicant1DetailModel().getApplicantDetailId())) {
			    			  if (null!= accountModel.getApplicant1DetailModel().getTncPic() && accountModel.getApplicant1DetailModel().getTncPic().getSize()>1)
			    				  customerPictureModel.setPicture(accountModel.getApplicant1DetailModel().getTncPic().getBytes());
			    			  else if(null!= accountModel.getApplicant1DetailModel().getTncPicByte() &&  accountModel.getApplicant1DetailModel().getTncPicByte().length>1)
			    				  customerPictureModel.setPicture(accountModel.getApplicant1DetailModel().getTncPicByte());
			    			  customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			    			  customerPictureModel.setUpdatedOn(nowDate);
			    			  customerPictureModel.setApplicantDetailId(applicant1DetailModel.getApplicantDetailId());
			    			  customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
		    			  }
			    		  else if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SIGNATURE_SNAPSHOT && customerPictureModel.getApplicantDetailId().equals(accountModel.getApplicant1DetailModel().getApplicantDetailId())) {
			    			  if (null!= accountModel.getApplicant1DetailModel().getSignPic() && accountModel.getApplicant1DetailModel().getSignPic().getSize()>1)
			    				  customerPictureModel.setPicture(accountModel.getApplicant1DetailModel().getSignPic().getBytes());
			    			  else if(null!= accountModel.getApplicant1DetailModel().getSignPicByte() &&  accountModel.getApplicant1DetailModel().getSignPicByte().length>1)
			    				  customerPictureModel.setPicture(accountModel.getApplicant1DetailModel().getSignPicByte());
			    			  customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			    			  customerPictureModel.setUpdatedOn(nowDate);
			    			  customerPictureModel.setApplicantDetailId(applicant1DetailModel.getApplicantDetailId());
			    			  customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
		    			  }
			    		  else if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_FRONT_SNAPSHOT && customerPictureModel.getApplicantDetailId().equals(accountModel.getApplicant1DetailModel().getApplicantDetailId())) {
			    			  if (null!= accountModel.getApplicant1DetailModel().getIdFrontPic() && accountModel.getApplicant1DetailModel().getIdFrontPic().getSize()>1)
			    				  customerPictureModel.setPicture(accountModel.getApplicant1DetailModel().getIdFrontPic().getBytes());
			    			  else if(null!= accountModel.getApplicant1DetailModel().getIdFrontPicByte() &&  accountModel.getApplicant1DetailModel().getIdFrontPicByte().length>1)
			    				  customerPictureModel.setPicture(accountModel.getApplicant1DetailModel().getIdFrontPicByte());
			    			  customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			    			  customerPictureModel.setUpdatedOn(nowDate);
			    			  customerPictureModel.setApplicantDetailId(applicant1DetailModel.getApplicantDetailId());
			    			  customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
		    			  }
			    		  else if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_BACK_SNAPSHOT && customerPictureModel.getApplicantDetailId().equals(accountModel.getApplicant1DetailModel().getApplicantDetailId())) {
			    			  if (null!= accountModel.getApplicant1DetailModel().getIdBackPic() && accountModel.getApplicant1DetailModel().getIdBackPic().getSize()>1)
			    				  customerPictureModel.setPicture(accountModel.getApplicant1DetailModel().getIdBackPic().getBytes());
			    			  else if(null!= accountModel.getApplicant1DetailModel().getIdBackPicByte() &&  accountModel.getApplicant1DetailModel().getIdBackPicByte().length>1)
			    				  customerPictureModel.setPicture(accountModel.getApplicant1DetailModel().getIdBackPicByte());
			    			  customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			    			  customerPictureModel.setUpdatedOn(nowDate);
			    			  customerPictureModel.setApplicantDetailId(applicant1DetailModel.getApplicantDetailId());
			    			  customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
		    			  }
						}catch(Exception e){
							e.printStackTrace();
						}

					}



				}*/

            //updating applicant information other than type 1 through loop; by turab
            for (ApplicantDetailModel applicant2DetailModel : accountModel.getApplicantDetailModelList()) {
                if (null != applicant2DetailModel.getName()) {
                    applicant2DetailModel.setCustomerId(customerModel.getCustomerId());
                    if (null == applicant2DetailModel.getCreatedOn()) {
                        applicant2DetailModel.setCreatedOn(nowDate);
                        applicant2DetailModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    }
                    applicant2DetailModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    applicant2DetailModel.setUpdatedOn(nowDate);
                    applicant2DetailModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_2);
                    applicant2DetailModel = this.applicantDetailDAO.saveOrUpdate(applicant2DetailModel);

                    //updating applicant2 addresses
                    try {
                        Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
                        AddressModel presentHomeAddressAp2 = new AddressModel();
                        AddressModel businessAddressAp2 = new AddressModel();

                        ///check if customerAddresses does not contains the applicantdetailId add new customer addresses
                        if (!this.isContainCustomerAddress(customerAddresses, applicant2DetailModel.getApplicantDetailId())) {
                            if (null != applicant2DetailModel.getResidentialAddress()) {
                                presentHomeAddressAp2.setHouseNo(applicant2DetailModel.getResidentialAddress());
                                presentHomeAddressAp2.setCityId(applicant2DetailModel.getResidentialCity());
                            }
                            if (null != applicant2DetailModel.getBuisnessAddress()) {
                                businessAddressAp2.setStreetAddress(applicant2DetailModel.getBuisnessAddress());
                                businessAddressAp2.setCityId(applicant2DetailModel.getBuisnessCity());
                            }
                            presentHomeAddressAp2 = this.addressDAO.saveOrUpdate(presentHomeAddressAp2);
                            businessAddressAp2 = this.addressDAO.saveOrUpdate(businessAddressAp2);

                            CustomerAddressesModel presentCustomerAddressesModelAp2 = new CustomerAddressesModel();
                            presentCustomerAddressesModelAp2.setAddressId(presentHomeAddressAp2.getAddressId());
                            presentCustomerAddressesModelAp2.setAddressTypeId(1L);
                            presentCustomerAddressesModelAp2.setCustomerId(customerModel.getCustomerId());
                            presentCustomerAddressesModelAp2.setApplicantTypeId(2L);
                            presentCustomerAddressesModelAp2.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());

                            CustomerAddressesModel businessCustomerAddressesModelAp2 = new CustomerAddressesModel();
                            businessCustomerAddressesModelAp2.setAddressId(businessAddressAp2.getAddressId());
                            businessCustomerAddressesModelAp2.setAddressTypeId(3L);
                            businessCustomerAddressesModelAp2.setCustomerId(customerModel.getCustomerId());
                            businessCustomerAddressesModelAp2.setApplicantTypeId(2L);
                            businessCustomerAddressesModelAp2.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());

                            presentCustomerAddressesModelAp2 = this.customerAddressesDAO.saveOrUpdate(presentCustomerAddressesModelAp2);
                            businessCustomerAddressesModelAp2 = this.customerAddressesDAO.saveOrUpdate(businessCustomerAddressesModelAp2);
                        }

                        ///end of new customer addresses

                        if (customerAddresses != null && customerAddresses.size() > 0) {
                            for (CustomerAddressesModel custAdd : customerAddresses) {
                                AddressModel addressModel = custAdd.getAddressIdAddressModel();
                                if (null == custAdd.getApplicantTypeId() && custAdd.getAddressTypeId().longValue() == 4L) {
                                    if (accountModel.getNokDetailVOModel().getNokMailingAdd() != null) {
                                        addressModel.setStreetAddress(accountModel.getNokDetailVOModel().getNokMailingAdd());
                                    }
                                    this.addressDAO.saveOrUpdate(addressModel);
                                }
                                if (null != custAdd.getApplicantTypeId() && custAdd.getApplicantTypeId().longValue() == 1L) {
                                    if (custAdd.getAddressTypeId() == 1) {
                                        if (applicant1DetailModel.getResidentialAddress() != null) {
                                            addressModel.setHouseNo(applicant1DetailModel.getResidentialAddress());
                                        }
                                        if (applicant1DetailModel.getResidentialCity() != null) {
                                            addressModel.setCityId(applicant1DetailModel.getResidentialCity());
                                        }
                                        this.addressDAO.saveOrUpdate(addressModel);
                                    } else if (custAdd.getAddressTypeId() == 3) {
                                        if (applicant1DetailModel.getBuisnessAddress() != null) {
                                            addressModel.setStreetAddress(applicant1DetailModel.getBuisnessAddress());
                                        }
                                        if (applicant1DetailModel.getBuisnessCity() != null) {
                                            addressModel.setCityId(applicant1DetailModel.getBuisnessCity());
                                        }
                                        this.addressDAO.saveOrUpdate(addressModel);
                                    }

                                }
                                if (null != applicant2DetailModel.getName() && applicant2DetailModel.getApplicantDetailId() != null && custAdd.getApplicantTypeId() != null && custAdd.getApplicantTypeId() == 2L
                                        && custAdd.getApplicantDetailId() != null && custAdd.getApplicantDetailId().equals(applicant2DetailModel.getApplicantDetailId())) {
                                    if (custAdd.getAddressTypeId() == 1) {
                                        if (applicant2DetailModel.getResidentialAddress() != null) {
                                            addressModel.setHouseNo(applicant2DetailModel.getResidentialAddress());
                                        }
                                        if (applicant2DetailModel.getResidentialCity() != null) {
                                            addressModel.setCityId(applicant2DetailModel.getResidentialCity());
                                        }
                                        presentHomeAddressAp2 = this.addressDAO.saveOrUpdate(addressModel);
                                    } else if (custAdd.getAddressTypeId() == 3) {
                                        if (applicant2DetailModel.getBuisnessAddress() != null) {
                                            addressModel.setStreetAddress(applicant2DetailModel.getBuisnessAddress());
                                        }
                                        if (applicant2DetailModel.getBuisnessCity() != null) {
                                            addressModel.setCityId(applicant2DetailModel.getBuisnessCity());
                                        }
                                        businessAddressAp2 = this.addressDAO.saveOrUpdate(addressModel);
                                    }
                                }


                            }
                        } else {
                            if (null != applicant2DetailModel.getResidentialAddress()) {
                                presentHomeAddressAp2.setHouseNo(applicant2DetailModel.getResidentialAddress());
                                presentHomeAddressAp2.setCityId(applicant2DetailModel.getResidentialCity());
                            }
                            if (null != applicant2DetailModel.getBuisnessAddress()) {
                                businessAddressAp2.setStreetAddress(applicant2DetailModel.getBuisnessAddress());
                                businessAddressAp2.setCityId(applicant2DetailModel.getBuisnessCity());
                            }
                            presentHomeAddressAp2 = this.addressDAO.saveOrUpdate(presentHomeAddressAp2);
                            businessAddressAp2 = this.addressDAO.saveOrUpdate(businessAddressAp2);


                            CustomerAddressesModel presentCustomerAddressesModelAp2 = new CustomerAddressesModel();
                            presentCustomerAddressesModelAp2.setAddressId(presentHomeAddressAp2.getAddressId());
                            presentCustomerAddressesModelAp2.setAddressTypeId(1L);
                            presentCustomerAddressesModelAp2.setCustomerId(customerModel.getCustomerId());
                            presentCustomerAddressesModelAp2.setApplicantTypeId(2L);
                            presentCustomerAddressesModelAp2.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());

                            CustomerAddressesModel businessCustomerAddressesModelAp2 = new CustomerAddressesModel();
                            businessCustomerAddressesModelAp2.setAddressId(businessAddressAp2.getAddressId());
                            businessCustomerAddressesModelAp2.setAddressTypeId(3L);
                            businessCustomerAddressesModelAp2.setCustomerId(customerModel.getCustomerId());
                            businessCustomerAddressesModelAp2.setApplicantTypeId(2L);
                            businessCustomerAddressesModelAp2.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());

                            presentCustomerAddressesModelAp2 = this.customerAddressesDAO.saveOrUpdate(presentCustomerAddressesModelAp2);
                            businessCustomerAddressesModelAp2 = this.customerAddressesDAO.saveOrUpdate(businessCustomerAddressesModelAp2);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //commenting below updating images as images are no more required by JS
			/*List<CustomerPictureModel> customerPictureList = this.searchAllCustomerPictures(customerModel.getCustomerId().longValue() , ApplicantTypeConstants.APPLICANT_TYPE_2,  applicant2DetailModel.getApplicantDetailId() );
			if(customerPictureList == null || customerPictureList.size() == 0){
				CustomerPictureModel customerPictureModel = new CustomerPictureModel();
  				if(null!= applicant2DetailModel.getFatcaFormPicByte() &&  applicant2DetailModel.getFatcaFormPicByte().length>1){
	  				customerPictureModel.setApplicantTypeId(2L);
					customerPictureModel.setPicture(applicant2DetailModel.getFatcaFormPicByte());
					customerPictureModel.setPictureTypeId(PictureTypeConstants.CUSTOMER_PHOTO);
					customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
					customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					customerPictureModel.setCreatedOn(nowDate);
					customerPictureModel.setUpdatedOn(nowDate);
					customerPictureModel.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());
					customerPictureModel.setCustomerId(applicant2DetailModel.getCustomerId());
					if(customerPictureModel.getPicture()!= null && customerPictureModel.getPicture().length > 0){
						customerPictureDAO.saveOrUpdate(customerPictureModel);
					}
	  				}

	  				if(null!= applicant2DetailModel.getIdFrontPicByte() &&  applicant2DetailModel.getIdFrontPicByte().length>1){
					customerPictureModel = new CustomerPictureModel();
					customerPictureModel.setApplicantTypeId(2L);
					customerPictureModel.setPicture(applicant2DetailModel.getIdFrontPicByte());
					customerPictureModel.setPictureTypeId(PictureTypeConstants.ID_FRONT_SNAPSHOT);
					customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
					customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					customerPictureModel.setCreatedOn(nowDate);
					customerPictureModel.setUpdatedOn(nowDate);
					customerPictureModel.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());
					customerPictureModel.setCustomerId(applicant2DetailModel.getCustomerId());
					if(customerPictureModel.getPicture()!= null && customerPictureModel.getPicture().length > 0){
						customerPictureDAO.saveOrUpdate(customerPictureModel);
					}
	  				}
	  				if(null!= applicant2DetailModel.getIdBackPicByte() &&  applicant2DetailModel.getIdBackPicByte().length>1){
					customerPictureModel = new CustomerPictureModel();
					customerPictureModel.setApplicantTypeId(2L);
					customerPictureModel.setPicture(applicant2DetailModel.getIdBackPicByte());
					customerPictureModel.setPictureTypeId(PictureTypeConstants.ID_BACK_SNAPSHOT);
					customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
					customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
					customerPictureModel.setCreatedOn(nowDate);
					customerPictureModel.setUpdatedOn(nowDate);
					customerPictureModel.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());
					customerPictureModel.setCustomerId(applicant2DetailModel.getCustomerId());
					if(customerPictureModel.getPicture()!= null && customerPictureModel.getPicture().length > 0){
						customerPictureDAO.saveOrUpdate(customerPictureModel);
					}
	  				}
			}
			for(CustomerPictureModel customerPictureModel : customerPictureList){
			if(null != customerPictureModel.getApplicantTypeId() && null != applicant2DetailModel.getApplicantDetailId() && customerPictureModel.getApplicantTypeId().longValue() == ApplicantTypeConstants.APPLICANT_TYPE_2 && customerPictureModel.getApplicantDetailId().equals(applicant2DetailModel.getApplicantDetailId())) {
				if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.CUSTOMER_PHOTO && customerPictureModel.getApplicantDetailId().equals(applicant2DetailModel.getApplicantDetailId())) {
	    			if(null!= applicant2DetailModel.getFatcaFormPicByte() &&  applicant2DetailModel.getFatcaFormPicByte().length>1){
	    			  customerPictureModel.setPicture(applicant2DetailModel.getFatcaFormPicByte());
	    			  customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	    			  customerPictureModel.setUpdatedOn(nowDate);
	    			  customerPictureModel.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());
	    			  customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
	    			}
  			  	}
				else if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.TERMS_AND_CONDITIONS_COPY && customerPictureModel.getApplicantDetailId().equals(applicant2DetailModel.getApplicantDetailId())) {
	    			  if(null!= applicant2DetailModel.getTncPicByte() &&  applicant2DetailModel.getTncPicByte().length>1){
	    				customerPictureModel.setPicture(applicant2DetailModel.getTncPicByte());
	    				customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	    				customerPictureModel.setUpdatedOn(nowDate);
	    				customerPictureModel.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());
	    				customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
	    			  }
  			  	}
	    		else if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.SIGNATURE_SNAPSHOT && customerPictureModel.getApplicantDetailId().equals(applicant2DetailModel.getApplicantDetailId())) {
	    			  if(null!= applicant2DetailModel.getSignPicByte() &&  applicant2DetailModel.getSignPicByte().length>1){
	    				customerPictureModel.setPicture(applicant2DetailModel.getSignPicByte());
	    			  	customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	    			  	customerPictureModel.setUpdatedOn(nowDate);
	    			  	customerPictureModel.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());
	    			  	customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
	    			  }
  			  	}
	    		else if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_FRONT_SNAPSHOT && customerPictureModel.getApplicantDetailId().equals(applicant2DetailModel.getApplicantDetailId())) {
	    			  if(null!= applicant2DetailModel.getIdFrontPicByte() &&  applicant2DetailModel.getIdFrontPicByte().length>1){
	    				customerPictureModel.setPicture(applicant2DetailModel.getIdFrontPicByte());
	    			  	customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	    			  	customerPictureModel.setUpdatedOn(nowDate);
	    			  	customerPictureModel.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());
	    			  	customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
	    			  }
  			  	}
	    		else if (customerPictureModel.getPictureTypeId().longValue() == PictureTypeConstants.ID_BACK_SNAPSHOT && customerPictureModel.getApplicantDetailId().equals(applicant2DetailModel.getApplicantDetailId())) {
	    			  if(null!= applicant2DetailModel.getIdBackPicByte() &&  applicant2DetailModel.getIdBackPicByte().length>1){
	    				customerPictureModel.setPicture(applicant2DetailModel.getIdBackPicByte());
	    			  	customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
	    			  	customerPictureModel.setUpdatedOn(nowDate);
	    			  	customerPictureModel.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());
	    			  	customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);
	    			  }

  			  	}
  			  }else{//new applicant is being add save it with applicantDetailId
  				if(null!= applicant2DetailModel.getFatcaFormPicByte() &&  applicant2DetailModel.getFatcaFormPicByte().length>1){
  				customerPictureModel.setApplicantTypeId(2L);
				customerPictureModel.setCustomerId(customerModel.getCustomerId());
				customerPictureModel.setPicture(applicant2DetailModel.getFatcaFormPicByte());
				customerPictureModel.setPictureTypeId(PictureTypeConstants.CUSTOMER_PHOTO);
				customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				customerPictureModel.setCreatedOn(nowDate);
				customerPictureModel.setUpdatedOn(nowDate);
				customerPictureModel.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());
				customerPictureDAO.saveOrUpdate(customerPictureModel);
  				}
  				if(null!= applicant2DetailModel.getTncPicByte() &&  applicant2DetailModel.getTncPicByte().length>1){
				customerPictureModel = new CustomerPictureModel();
				customerPictureModel.setApplicantTypeId(2L);
				customerPictureModel.setCustomerId(customerModel.getCustomerId());
				customerPictureModel.setPicture(applicant2DetailModel.getTncPicByte());
				customerPictureModel.setPictureTypeId(PictureTypeConstants.TERMS_AND_CONDITIONS_COPY);
				customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				customerPictureModel.setCreatedOn(nowDate);
				customerPictureModel.setUpdatedOn(nowDate);
				customerPictureModel.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());
				customerPictureDAO.saveOrUpdate(customerPictureModel);
  				}
  				if(null!= applicant2DetailModel.getSignPicByte() &&  applicant2DetailModel.getSignPicByte().length>1){
				customerPictureModel = new CustomerPictureModel();
				customerPictureModel.setApplicantTypeId(2L);
				customerPictureModel.setCustomerId(customerModel.getCustomerId());
				customerPictureModel.setPicture(applicant2DetailModel.getSignPicByte());
				customerPictureModel.setPictureTypeId(PictureTypeConstants.SIGNATURE_SNAPSHOT);
				customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				customerPictureModel.setCreatedOn(nowDate);
				customerPictureModel.setUpdatedOn(nowDate);
				customerPictureModel.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());
				customerPictureDAO.saveOrUpdate(customerPictureModel);
  				}
  				if(null!= applicant2DetailModel.getIdFrontPicByte() &&  applicant2DetailModel.getIdFrontPicByte().length>1){
				customerPictureModel = new CustomerPictureModel();
				customerPictureModel.setApplicantTypeId(2L);
				customerPictureModel.setCustomerId(customerModel.getCustomerId());
				customerPictureModel.setPicture(applicant2DetailModel.getIdFrontPicByte());
				customerPictureModel.setPictureTypeId(PictureTypeConstants.ID_FRONT_SNAPSHOT);
				customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				customerPictureModel.setCreatedOn(nowDate);
				customerPictureModel.setUpdatedOn(nowDate);
				customerPictureModel.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());
				customerPictureDAO.saveOrUpdate(customerPictureModel);
  				}
  				if(null!= applicant2DetailModel.getIdBackPicByte() &&  applicant2DetailModel.getIdBackPicByte().length>1){
				customerPictureModel = new CustomerPictureModel();
				customerPictureModel.setApplicantTypeId(2L);
				customerPictureModel.setCustomerId(customerModel.getCustomerId());
				customerPictureModel.setPicture(applicant2DetailModel.getIdBackPicByte());
				customerPictureModel.setPictureTypeId(PictureTypeConstants.ID_BACK_SNAPSHOT);
				customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
				customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
				customerPictureModel.setCreatedOn(nowDate);
				customerPictureModel.setUpdatedOn(nowDate);
				customerPictureModel.setApplicantDetailId(applicant2DetailModel.getApplicantDetailId());
				customerPictureDAO.saveOrUpdate(customerPictureModel);
  				}

  			 }

			}*/

            }// loop end for updating through loop applicants

            //save/update AccountOwnerShip list now
            if (CollectionUtils.isNotEmpty(accountModel.getAcOwnershipDetailModelList())) {
                List<ACOwnershipDetailModel> accountOwnerShipModelList = new ArrayList<ACOwnershipDetailModel>(0);
                for (ACOwnershipDetailModel accountOwnerShipDetailModel : accountModel.getAcOwnershipDetailModelList()) {
                    if (accountOwnerShipDetailModel.getAcOwnershipTypeId() != null) {
                        if (accountOwnerShipDetailModel.getAcOwnershipDetailId() == null) {
                            accountOwnerShipDetailModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                            accountOwnerShipDetailModel.setCreatedOn(new Date());
                        }
                        accountOwnerShipDetailModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        accountOwnerShipDetailModel.setUpdatedOn(new Date());
                        accountOwnerShipDetailModel.setCustomerId(customerModel.getCustomerId());
                        accountOwnerShipDetailModel.setIsDeleted(Boolean.FALSE);
                        accountOwnerShipModelList.add(accountOwnerShipDetailModel);
                    }
                }

                SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                ACOwnershipDetailModel accountOwnerShipModel = new ACOwnershipDetailModel();
                accountOwnerShipModel.setCustomerId(customerModel.getCustomerId());
                searchBaseWrapper.setBasePersistableModel(accountOwnerShipModel);
                List<ACOwnershipDetailModel> existingOwnerShips = (List<ACOwnershipDetailModel>) baseWrapper.getObject("existingOwners");/*this.loadAccountOwnerShipDetails(searchBaseWrapper);*/
                List<ACOwnershipDetailModel> mergedList = new ArrayList<ACOwnershipDetailModel>(0);

                if (accountOwnerShipModelList.size() != existingOwnerShips.size()) {
                    for (ACOwnershipDetailModel existingModel : existingOwnerShips) {
                        boolean isExist = false;
                        for (ACOwnershipDetailModel model : accountOwnerShipModelList) {
                            if (existingModel.getAcOwnershipDetailId().longValue() == model.getAcOwnershipDetailId().longValue()) {
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            if (existingModel.getAcOwnershipTypeId() != null) {
                                existingModel.setIsDeleted(true);
                                existingModel.setVersionNo(existingModel.getVersionNo());
                                mergedList.add(existingModel);
                            }
                            isExist = false;
                        }
                    }
                }

                mergedList.addAll(accountOwnerShipModelList);
                acOwnerShipDetailDAO.saveOrUpdateCollection(mergedList);
            }

            /**
             * Saving the CustomerModel
             */
            baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(customerModel);
            baseWrapper = this.custTransManager.saveOrUpdate(baseWrapper);
        }


        // Updating Address Fields
        try {

            Boolean SKIP_AP1_ADD = false;
            Boolean SKIP_AP2_ADD = false;
            Boolean SKIP_AP3_ADD = false;

            // Populating Address Fields
            Collection<CustomerAddressesModel> customerAddresses = customerModel.getCustomerIdCustomerAddressesModelList();
            if (customerAddresses != null && customerAddresses.size() > 0) {
                for (CustomerAddressesModel custAdd : customerAddresses) {
                    AddressModel addressModel = custAdd.getAddressIdAddressModel();

                    if (null == custAdd.getApplicantTypeId()) {
                        if (accountModel.getNokDetailVOModel().getNokMailingAdd() != null) {
                            addressModel.setHouseNo(accountModel.getNokDetailVOModel().getNokMailingAdd());
                        }
                        this.addressDAO.saveOrUpdate(addressModel);
                    } else if (custAdd.getApplicantTypeId() == 1L) {
                        if (custAdd.getAddressTypeId() == 1) {
                            if (accountModel.getApplicant1DetailModel().getResidentialAddress() != null) {
                                addressModel.setHouseNo(accountModel.getApplicant1DetailModel().getResidentialAddress());
                            }
                            if (accountModel.getApplicant1DetailModel().getResidentialCity() != null) {
                                addressModel.setCityId(accountModel.getApplicant1DetailModel().getResidentialCity());
                            }
                            this.addressDAO.saveOrUpdate(addressModel);
                        } else if (custAdd.getAddressTypeId() == 3) {
                            if (accountModel.getApplicant1DetailModel().getBuisnessAddress() != null) {
                                addressModel.setHouseNo(accountModel.getApplicant1DetailModel().getBuisnessAddress());
                            }
                            if (accountModel.getApplicant1DetailModel().getBuisnessCity() != null) {
                                addressModel.setCityId(accountModel.getApplicant1DetailModel().getBuisnessCity());
                            }
                            this.addressDAO.saveOrUpdate(addressModel);
                        }
                        SKIP_AP1_ADD = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(e.getMessage(), e);
        }

        appUserModel.setNic(accountModel.getApplicant1DetailModel().getIdNumber());
        appUserModel.setNicExpiryDate(accountModel.getApplicant1DetailModel().getIdExpiryDate());
        appUserModel.setDob(accountModel.getApplicant1DetailModel().getDob());
        appUserModel.setMobileNo(accountModel.getMobileNo());
        nameArray = accountModel.getApplicant1DetailModel().getName().split(" ");
        appUserModel.setFirstName(nameArray[0]);
        if (nameArray.length > 1) {
            appUserModel.setLastName(accountModel.getApplicant1DetailModel().getName().substring(appUserModel.getFirstName().length() + 1));
        } else {
            appUserModel.setLastName(nameArray[0]);
        }
        appUserModel.setRegistrationStateId(accountModel.getRegistrationStateId());
        appUserModel.setMobileTypeId(1L);
        appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        appUserModel.setUpdatedOn(nowDate);

        appUserModel.setAccountClosedUnsettled(accountModel.getAccountClosedUnsettled());
        if (accountModel.getClosedBy() != null) {
            appUserModel.setClosedByAppUserModel(this.appUserDAO.findByPrimaryKey(Long.valueOf(accountModel.getClosedBy())));
        }
        appUserModel.setClosedOn(accountModel.getClosedOn());
        appUserModel.setClosingComments(accountModel.getClosingComments());

        appUserModel.setAccountClosedSettled(accountModel.getAccountClosedSettled());
        if (accountModel.getSettledBy() != null) {
            appUserModel.setSettledByAppUserModel(this.appUserDAO.findByPrimaryKey(Long.valueOf(accountModel.getSettledBy())));
        }
        appUserModel.setSettledOn(appUserModel.getSettledOn());
        appUserModel.setSettlementComments(appUserModel.getSettlementComments());

        if (accountModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DECLINE)) {
            appUserModel.setAccountStateId(1L);
        } else if (accountModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.REJECTED)) {
            appUserModel.setAccountStateId(1L);
        } else if (accountModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DISCREPANT)) {
            appUserModel.setAccountStateId(1L);
        } else if (accountModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.VERIFIED)) {
            appUserModel.setAccountStateId(3L);
        }

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

        //Send notification if Registration State changed
        Long newRegistrationStateId = accountModel.getRegistrationStateId();
        sendRegistationStateNotification(oldRegistrationStateId, newRegistrationStateId, appUserModel.getMobileNo());

        /**
         * Logging the information process ends
         */
        actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
        actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
        actionLogModel.setCustomField1(String.valueOf(appUserModel.getAppUserId()));
        actionLogModel.setCustomField11(appUserModel.getUsername());
        actionLogModel = logAction(actionLogModel);

        baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(appUserModel);

        return baseWrapper;
    }


    /**
     * This method computes the next mfsId / username, it checks
     * the table App_User, and Golden_Nums are checked.
     */
    public String computeMfsId() {
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
            int countAppUser = this.appUserDAO.countByExample(appUserModel);
            int countGoldenNos = this.goldenNosDAO.countByExample(goldenNosModel);
            if (countAppUser == 0 && countGoldenNos == 0) {
                flag = false;
            }
        }

        return String.valueOf(nextLongValue);
    }

    private String getActionValue(SmartMoneyAccountModel smartMoneyAccountModel, UserDeviceAccountsModel userDeviceAccountsModel, Boolean isLockOrUnLock) {
        String action = null;
        if (isLockOrUnLock) {
            if (userDeviceAccountsModel.getAccountLocked() == Boolean.TRUE || (smartMoneyAccountModel.getStatusId() != null && smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED)))
                action = "UN-BLOCK";
            else if (userDeviceAccountsModel.getAccountLocked() == Boolean.FALSE || (smartMoneyAccountModel.getStatusId() != null && smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE)))
                action = "BLOCK";
            else
                action = "BLOCK";
        } else {
            if (userDeviceAccountsModel.getAccountEnabled() == Boolean.TRUE || (smartMoneyAccountModel.getStatusId() != null && smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE)))
                action = "DE-ACTIVE";
            else if (userDeviceAccountsModel.getAccountEnabled() == Boolean.FALSE || (smartMoneyAccountModel.getStatusId() != null && smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE)))
                action = "ACTIVE";
            else
                action = "DE-ACTIVE";
        }
        return action;
    }

    private Long setAccountState(Boolean isLockUnlock, Boolean isActive) {
        Long accountStateId = null;

        if (!isLockUnlock) {
            if (isActive.booleanValue())
                accountStateId = AccountStateConstantsInterface.ACCOUNT_STATE_COLD;
            else
                accountStateId = AccountStateConstantsInterface.ACCOUNT_STATE_WARM;
        } else {
            if (isActive.booleanValue())
                accountStateId = AccountStateConstantsInterface.ACCOUNT_STATE_COLD;
            else
                accountStateId = AccountStateConstantsInterface.ACCOUNT_STATE_WARM;
        }

        return accountStateId;
    }

    private BaseWrapper setModelProperties(BaseWrapper baseWrapper) {
        boolean isLockUnlock = (Boolean) baseWrapper.getObject("isLockUnlock");
        UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getObject("userDeviceAccountsModel");
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
        String action = (String) baseWrapper.getObject("action");
        if (action == null)
            action = getActionValue(smartMoneyAccountModel, userDeviceAccountsModel, isLockUnlock);
        Long paymentModeId = (Long) baseWrapper.getObject("paymentModeId");
        if (action.equalsIgnoreCase("BLOCK")) {
            if (paymentModeId.equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT) || paymentModeId.equals(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT))
                userDeviceAccountsModel.setAccountLocked(Boolean.TRUE);
            baseWrapper.putObject(KEY_IS_ACTIVE, Boolean.FALSE);
            smartMoneyAccountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED);
            baseWrapper.putObject("activate", false);
        } else if (action.equalsIgnoreCase("UN-BLOCK")) {
            if (paymentModeId.equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT) || paymentModeId.equals(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT))
                userDeviceAccountsModel.setAccountLocked(Boolean.FALSE);
            baseWrapper.putObject(KEY_IS_ACTIVE, Boolean.TRUE);
            smartMoneyAccountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
            baseWrapper.putObject("activate", true);
        } else if (action.equalsIgnoreCase("ACTIVE")) {
            if (paymentModeId.equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT) || paymentModeId.equals(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT))
                userDeviceAccountsModel.setAccountEnabled(Boolean.TRUE);
            baseWrapper.putObject(KEY_IS_ACTIVE, Boolean.TRUE);
            smartMoneyAccountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
            baseWrapper.putObject("activate", true);
        } else if (action.equalsIgnoreCase("DE-ACTIVE")) {
            if (paymentModeId.equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT) || paymentModeId.equals(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT))
                userDeviceAccountsModel.setAccountEnabled(Boolean.FALSE);
            baseWrapper.putObject(KEY_IS_ACTIVE, Boolean.FALSE);
            smartMoneyAccountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE);
            baseWrapper.putObject("activate", false);
        }

        baseWrapper.putObject("userDeviceAccountsModel", userDeviceAccountsModel);
        baseWrapper.setBasePersistableModel(smartMoneyAccountModel);

        return baseWrapper;
    }

    public BaseWrapper activateDeactivateMfsAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        String actionLogHandler = (String) baseWrapper.getObject(CommandFieldConstants.KEY_ACTION_LOG_HANDLER);
        Long actionAuthId = (Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_AUTH_ID);
        boolean isLockUnlock = (Boolean) baseWrapper.getObject("isLockUnlock");
        ActionLogModel actionLogModel = new ActionLogModel();
        Date nowDate = new Date();
        baseWrapper.putObject(PortalConstants.KEY_UPDATION_TIME, nowDate);
        String action = (String) baseWrapper.getObject("action");// used in complaint.created_on

        /**
         * Logging the information process
         */
        if (actionLogHandler == null && actionAuthId == null) {
            actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null, baseWrapper, null);
            if (baseWrapper.getObject("mfsId") != null)
                actionLogModel.setCustomField11(baseWrapper.getObject("mfsId").toString());
            ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
        }
        SmartMoneyAccountModel smartMoneyAccountModel = null;

        String randomPin = RandomUtils.generateRandom(4, false, true);
        boolean activate = false;
        boolean originalACEnableStatus = false;
        AppUserModel appUserModel = new AppUserModel();
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setEnableLike(Boolean.FALSE);
        Long paymentMode = (Long) baseWrapper.getObject("paymentModeId");
        Long accountStateId = null;
        Boolean isActive = false;

        if (paymentMode.equals(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT)) {
            baseWrapper = setModelProperties(baseWrapper);
            appUserModel = (AppUserModel) baseWrapper.getObject("appUserModel");
        }
        smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();


        if (paymentMode.equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT) || paymentMode.equals(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT)) {
            CustomList<UserDeviceAccountsModel> results = this.userDeviceAccountsDAO.findByExample(
                    (UserDeviceAccountsModel) baseWrapper.getObject("userDeviceAccountsModel"), null, null, exampleHolder);
            List<UserDeviceAccountsModel> deviceAccList = results.getResultsetList();
            if (isLockUnlock) {
                System.out.println("#####LOCK/UNLOCK#######");
            } else {
                System.out.println("#####Act/Deact#######");
            }

            Set<UserDeviceAccountsModel> deviceAccSet = new HashSet<UserDeviceAccountsModel>(deviceAccList);

            System.out.println("deviceAccList size: " + deviceAccList.size() + " = " + deviceAccSet.size());

            Iterator<UserDeviceAccountsModel> it = deviceAccSet.iterator();
            while (it.hasNext()) {
                UserDeviceAccountsModel userDeviceAccountsModel = it.next();
                appUserModel = userDeviceAccountsModel.getAppUserIdAppUserModel();

                baseWrapper.putObject("userDeviceAccountsModel", userDeviceAccountsModel);
                baseWrapper = setModelProperties(baseWrapper);
                userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getObject("userDeviceAccountsModel");

                // For customer: updating device_type_id(1) will automatically update device_type_id(9) via trigger
                if ((appUserModel.getAppUserTypeId().longValue() != UserTypeConstantsInterface.CUSTOMER.longValue()
                        && userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.MOBILE.longValue())
                        || (appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()
                        && userDeviceAccountsModel.getDeviceTypeId().longValue() == DeviceTypeConstantsInterface.MOBILE.longValue())) {


                    System.out.println("#######" + userDeviceAccountsModel.getUserDeviceAccountsId());
                    System.out.println("************* old val: accEnabld:" + userDeviceAccountsModel.getAccountEnabled());
                    System.out.println("************* old val: accLocked:" + userDeviceAccountsModel.getAccountLocked());

                    originalACEnableStatus = userDeviceAccountsModel.getAccountEnabled();

					/*if(!isLockUnlock){
						if(action.equalsIgnoreCase("ACTIVE")){
							userDeviceAccountsModel.setAccountEnabled(Boolean.TRUE);
							baseWrapper.putObject(KEY_IS_ACTIVE, Boolean.TRUE);
							activate = true;
						}
						else if(action.equalsIgnoreCase("DE-ACTIVE")){
							userDeviceAccountsModel.setAccountEnabled(Boolean.FALSE);
							baseWrapper.putObject(KEY_IS_ACTIVE, Boolean.FALSE);
						}
					}
					else
					{
						if(action.equalsIgnoreCase("BLOCK")){
							userDeviceAccountsModel.setAccountLocked(Boolean.TRUE);
							baseWrapper.putObject(KEY_IS_ACTIVE, Boolean.FALSE);
						}
						else if(action.equalsIgnoreCase("UN-BLOCK")){
							userDeviceAccountsModel.setAccountLocked(Boolean.FALSE);
							baseWrapper.putObject(KEY_IS_ACTIVE, Boolean.TRUE);
							activate = true;
						}
					}*/
                    System.out.println("************* new val: accEnabld:" + userDeviceAccountsModel.getAccountEnabled());
                    System.out.println("************* new val: accLocked:" + userDeviceAccountsModel.getAccountLocked());

                    /**
                     * Updating the user userDeviceAccountsModel
                     */
                    userDeviceAccountsModel.setUpdatedOn(nowDate);
                    userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                    userDeviceAccountsModel.setLoginAttemptCount(new Integer(0));
                    this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
                }//end if
            }
        }

        activate = (Boolean) baseWrapper.getObject("activate");
        isActive = (Boolean) baseWrapper.getObject(MfsAccountManager.KEY_IS_ACTIVE);
        String acctStatus = (String) baseWrapper.getObject("accountStatus");
        //if activate account request change the dormant  state in app user and smart money
        if (acctStatus != null && acctStatus.equalsIgnoreCase("01") && appUserModel.getAccountStateId().equals(AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT)) {
            appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
            appUserModel.setPrevRegistrationStateId(appUserModel.getRegistrationStateId());
            appUserModel.setRegistrationStateId(RegistrationStateConstantsInterface.VERIFIED);
            appUserModel.setDormancyRemovedOn(new Date());
            appUserModel.setUpdatedOn(new Date());
            appUserModel.setUpdatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
            appUserModel.setDormancyRemovedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
            this.appUserDAO.saveOrUpdate(appUserModel);
            smartMoneyAccountModel.setPreviousRegStateId(smartMoneyAccountModel.getRegistrationStateId());
            smartMoneyAccountModel.setRegistrationStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
            smartMoneyAccountModel.setDormancyRemovedOn(new Date());
        }
        accountStateId = setAccountState(isLockUnlock, isActive);
        smartMoneyAccountModel.setAccountStateId(accountStateId);
        this.smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);
        baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        if (actionAuthId == null)
            actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null, baseWrapper, actionLogModel);
        //if(isSendSms)
        //{
        boolean isAgent = false, isHandler = false, isCustomer = false;
        if (null != appUserModel) {
            long userTypeId = appUserModel.getAppUserTypeId().longValue();
            if (UserTypeConstantsInterface.RETAILER.longValue() == userTypeId) {
                isAgent = true;
            } else if (UserTypeConstantsInterface.HANDLER.longValue() == userTypeId) {
                isHandler = true;
            } else if (UserTypeConstantsInterface.CUSTOMER.longValue() == userTypeId) {
                isCustomer = true;
            }
        }

        String messageString = "";
        String accountType = (String) baseWrapper.getObject("acType");
        //if(isActivationACSMS)
        if (!isLockUnlock) {
            if (activate) {
                Object[] args = {randomPin};
                if (isAgent) {
                    //args removed as there is no requirement of Pin. Bug # 20584
                    //messageString = MessageUtil.getMessage("smsCommand.act_sms12", args);
                    messageString = MessageUtil.getMessage("smsCommand.act_sms12");
                } else if (isHandler) {
                    //args removed as there is no requirement of Pin. Bug # 20584app
                    //messageString = MessageUtil.getMessage("smsCommand.act_sms14", args);
                    messageString = MessageUtil.getMessage("smsCommand.act_sms14");

                } else if (isCustomer) {
                    //messageString = "Dear Customer, your " + accountType + " Account has been activated.";
                    messageString = "Dear Customer, your  Account has been activated.";
                }
            } else {
                if (isAgent) {
                    messageString = MessageUtil.getMessage("smsCommand.dct_sms5");
                } else if (isHandler) {
                    messageString = MessageUtil.getMessage("smsCommand.dct_sms13");
                } else if (isCustomer) {
                    //messageString = "Dear Customer, your " + accountType + " Account has been deactivated.";//MessageUtil.getMessage("smsCommand.dct_sms6");
                    messageString = "Dear Customer, your Account has been deactivated.";
                }
            }
        } else {
            if (activate) {
                if (isAgent) {
                    messageString = MessageUtil.getMessage("smsCommand.unlock_agent");
                } else if (isHandler) {
                    messageString = MessageUtil.getMessage("smsCommand.unlock_handler");
                } else if (isCustomer) {
                    //messageString = "Dear Customer, your " + accountType + " Account has been Unblocked successfully.";//MessageUtil.getMessage("smsCommand.unlock_customer");
                    messageString = "Dear Customer, your  Account has been Unblocked successfully.";
                }
            } else {
                if (isAgent) {
                    messageString = MessageUtil.getMessage("smsCommand.lock_agent");
                } else if (isHandler) {
                    messageString = MessageUtil.getMessage("smsCommand.lock_handler");
                } else if (isCustomer) {
                    //messageString = messageString = "Dear Customer, your " + accountType + " Account has been Blocked successfully."; //MessageUtil.getMessage("smsCommand.lock_customer");
                    messageString = messageString = "Dear Customer, your sAccount has been Blocked successfully.";
                }
            }
        }

        SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), messageString);
        //if(null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()){
        //	smsMessage.setMessageText(MessageParsingUtils.parseMessageForIpos(smsMessage.getMessageText()));
        //}
        smsSender.send(smsMessage);
        //}

        /**
         * Logging the information process ends
         */
        if (actionLogHandler == null) {
            actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
            actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
            actionLogModel.setCustomField1(String.valueOf(appUserModel.getAppUserId()));
            actionLogModel = logAction(actionLogModel);
        }

        //******* baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        return baseWrapper;
    }


    /**
     * Method Logs the action performed in the action log table
     */
    private ActionLogModel logAction(ActionLogModel actionLogModel)
            throws FrameworkCheckedException {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(actionLogModel);
        if (null == actionLogModel.getActionLogId()) {
            baseWrapper =
                    this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
        } else {
            baseWrapper =
                    this.actionLogManager.createOrUpdateActionLog(baseWrapper);
        }
        return (ActionLogModel) baseWrapper.getBasePersistableModel();
    }

    @Override
    public void isUniqueCNICMobile(AppUserModel appUserModel, BaseWrapper baseWrapper) throws FrameworkCheckedException {

        if (!this.isMobileNumUnique(appUserModel.getMobileNo(), appUserModel.getAppUserId(), baseWrapper)) {
            throw new FrameworkCheckedException("MobileNumUniqueException");
        }

        if (!this.isNICUnique(appUserModel.getNic(), appUserModel.getAppUserId(), baseWrapper)) {
            throw new FrameworkCheckedException("NICUniqueException");
        }
    }

    /**
     * Method checks if mobile number is Unique, if
     * unique returns true , else return false
     */
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

    //check if Initial Application Form Number already Exists or not?
    private boolean isInitialAppFormNoUnique(String initialAppFormNoUnique, Long accType) {
        boolean result = true;


        KYCModel queryObject = new KYCModel();
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        queryObject.setInitialAppFormNo(initialAppFormNoUnique);
        queryObject.setAcType(accType);

        List<KYCModel> kycModelList = new ArrayList<KYCModel>();
        searchBaseWrapper.setBasePersistableModel(queryObject);

        try {

            kycModelList = this.findKycModel(searchBaseWrapper);

        } catch (FrameworkCheckedException e) {
            logger.error("[MfsAccountManagerImpl.isInitialAppFormNoUnique]", e);
            result = false;
        }

        if (kycModelList != null && kycModelList.size() > 0) {
            result = false;
        }

        AgentMerchantDetailModel agentMerchantDetailModel = new AgentMerchantDetailModel();
        agentMerchantDetailModel.setInitialAppFormNo(initialAppFormNoUnique);

        ExampleConfigHolderModel config = new ExampleConfigHolderModel();
        config.setMatchMode(MatchMode.EXACT);

        CustomList<AgentMerchantDetailModel> list = this.agentMerchantDetailDAO.
                findByExample(agentMerchantDetailModel, null, null, config);

        if (list != null && list.getResultsetList() != null && list.getResultsetList().size() > 0) {
            return false;
        }

        return result;

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

    public BaseWrapper createMfsAccountThroughSMS(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        AppUserModel appUserModel = new AppUserModel();
        CustomerModel customerModel = new CustomerModel();
        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        MfsAccountModel mfsAccountModel =
                (MfsAccountModel) baseWrapper.getObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY);

        /**
         * Checking if mobile Number is unique
         * 3. Checks local database to see if a MWallet account has been issued to the mobile number initiating the
         * 	  SMS or the status is active
         * 3a. If condition in 3 is true then CSR will communicate to customer and ask for reactivation.
         */
        if (!this.isMobileNumUnique(mfsAccountModel.getZongNo(), mfsAccountModel.getAppUserId())) {
            throw new FrameworkCheckedException("MobileNumUniqueException");
        }

        if (!this.isNICUnique(mfsAccountModel.getNic(), mfsAccountModel.getAppUserId())) {
            throw new FrameworkCheckedException("NICUniqueException");
        }


        Date nowDate = new Date();
        /**
         * Populating the Customer Model
         */
        customerModel.setRegister(true);
        customerModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        customerModel.setCreatedOn(nowDate);
        customerModel.setUpdatedOn(nowDate);

        /**
         * Populating the AppUserModel Model
         */
        appUserModel.setFirstName(mfsAccountModel.getFirstName());
        appUserModel.setLastName(mfsAccountModel.getLastName());
        appUserModel.setAddress1(mfsAccountModel.getAddress1());
        appUserModel.setAddress2(mfsAccountModel.getAddress2());
        appUserModel.setMobileNo(mfsAccountModel.getZongNo());
        appUserModel.setNic(mfsAccountModel.getNic());
        appUserModel.setCity(mfsAccountModel.getCity());
        appUserModel.setCountry(mfsAccountModel.getCountry());
        appUserModel.setState(mfsAccountModel.getState());
        appUserModel.setDob(mfsAccountModel.getDob());
        appUserModel.setMobileTypeId(mfsAccountModel.getConnectionType());


        appUserModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        appUserModel.setCreatedOn(nowDate);
        appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        appUserModel.setUpdatedOn(nowDate);
        appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);

        appUserModel.setVerified(true);
        appUserModel.setAccountEnabled(false);
        appUserModel.setAccountExpired(false);
        appUserModel.setAccountLocked(false);
        appUserModel.setCredentialsExpired(false);
        appUserModel.setPasswordChangeRequired(false);
        appUserModel.setAccountClosedSettled(false);
        appUserModel.setAccountClosedUnsettled(false);

        /**
         * Populating the UserDeviceAccountsModel
         */
        userDeviceAccountsModel.setAccountEnabled(true);
        userDeviceAccountsModel.setAccountExpired(false);
        userDeviceAccountsModel.setPasswordChangeRequired(false);

        System.out.println("****************************In MfsAccountManagerImpl*****************************");


        System.out.println("Connection Type : " + mfsAccountModel.getConnectionType());


//		if(mfsAccountModel.getConnectionType() != null && mfsAccountModel.getConnectionType().equals(CommandFieldConstants.KENNEL_PREPAID_VALUE))
//		{
//			System.out.println("In Prepaid Condition");
//			userDeviceAccountsModel.setAccountLocked(false);
//		}
//		else if(mfsAccountModel.getConnectionType() != null && mfsAccountModel.getConnectionType().equals(CommandFieldConstants.KENNEL_POSTPAID_VALUE))
        {
            userDeviceAccountsModel.setAccountLocked(false);
            userDeviceAccountsModel.setExpiryDate(PortalDateUtils.addYears(new Date(), CommandConstants.DEFAULT_YEARS_TO_ADD));
        }

        userDeviceAccountsModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        userDeviceAccountsModel.setCreatedOn(nowDate);
        userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        userDeviceAccountsModel.setUpdatedOn(nowDate);
        userDeviceAccountsModel.setPinChangeRequired(true);
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
        userDeviceAccountsModel.setCredentialsExpired(false);

        /**
         * Here creating the mfsId/usernaem , pin/password
         */
        String mfsId = computeMfsId();
        String username = mfsId;

        String randomPin = RandomUtils.generateRandom(4, false, true);
        String password = randomPin;

        /**
         * Saving the CustomerModel
         */
        baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(customerModel);
        baseWrapper = this.custTransManager.saveOrUpdate(baseWrapper);
        customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();

        /**
         * Saving the AppUserModel
         */
        baseWrapper = new BaseWrapperImpl();
        appUserModel.setCustomerId(customerModel.getCustomerId());
        appUserModel.setUsername(username);
        appUserModel.setPassword(EncoderUtils.encodeToSha(password));
        appUserModel = this.appUserDAO.saveOrUpdate(appUserModel);

        /**
         * Saving the UserDeviceAccountsModel
         */
        userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
        userDeviceAccountsModel.setCommissioned(false);
        userDeviceAccountsModel.setUserId(mfsId);
        userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(randomPin));
        userDeviceAccountsModel = this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);

        // step 3 ----- smart money account
        AbstractFinancialInstitution abstractFinancialInstitution = null;
        VeriflyBaseWrapper veriflyBaseWrapper = null;
        SmartMoneyAccountModel smartMoneyAccountModel = null;
        boolean phoenixSuccessful = false;
        boolean firstSmartMoneyAccount = false;
        try { // Start TRY block
            BaseWrapper baseWrapperUserDevice = new BaseWrapperImpl();

            BankModel bankModel = getOlaBankMadal();
            bankModel.setBankId(bankModel.getBankId());

            BaseWrapper baseWrapperBank = new BaseWrapperImpl();
            baseWrapperBank.setBasePersistableModel(bankModel);
            abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);

            String bankName = bankModel.getName();

            smartMoneyAccountModel = null;

            {
                smartMoneyAccountModel = new SmartMoneyAccountModel();
                smartMoneyAccountModel.setCustomerId(customerModel.getCustomerId());
                smartMoneyAccountModel.setBankId(getOlaBankMadal().getBankId());

            }

            smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            smartMoneyAccountModel.setBankId(bankModel.getBankId());

            smartMoneyAccountModel.setCreatedOn(new Date());
            smartMoneyAccountModel.setUpdatedOn(new Date());
            smartMoneyAccountModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
            smartMoneyAccountModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            smartMoneyAccountModel.setActive(true);

            if (abstractFinancialInstitution.isVeriflyRequired()) {

                if (abstractFinancialInstitution.isVeriflyLite()) {

                    smartMoneyAccountModel.setChangePinRequired(false);
                } else {
                    smartMoneyAccountModel.setChangePinRequired(false);
                }
            } else {
                smartMoneyAccountModel.setChangePinRequired(false);
            }

//			if (distributorContactFormModel.getCurrencyCode() != null
//					&& distributorContactFormModel.getAccountType() != null) {
//				smartMoneyAccountModel
//						.setCurrencyCodeId(distributorContactFormModel
//								.getCurrencyCode());
//				smartMoneyAccountModel.setAccountTypeId(distributorContactFormModel
//						.getAccountType());
//			}


            smartMoneyAccountModel.setDefAccount(true);
            smartMoneyAccountModel.setDeleted(false);


            smartMoneyAccountModel.setName("i8_bb_" + userDeviceAccountsModel.getUserId());
            baseWrapper.setBasePersistableModel(this.linkPaymentModeDAO.saveOrUpdate(smartMoneyAccountModel));


        } // End TRY Block
        catch (ImplementationNotSupportedException inse) {
            throw new FrameworkCheckedException(
                    "implementationNotSupportedException");
        } catch (Exception ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constrainViolationException = (ConstraintViolationException) ex.getCause();
                String constraintName = "UK_USER_DEVICE_TYPE";
                if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1) {
                    throw new FrameworkCheckedException("Allpay Id already exists");
                }
                constraintName = "UK_SM_ACCOUNT";
                if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1) {
                    throw new FrameworkCheckedException("Account nick already exists");
                }
            }
            ex.printStackTrace();
            String errMessage = ex.getMessage();

            baseWrapper.putObject("ErrMessage", errMessage);

            // This is a tuch to handle the smartmoney account created in case
            // of verifly exception.
            // this is because the transaction is not getting rolled back
            if (smartMoneyAccountModel != null
                    && smartMoneyAccountModel.getSmartMoneyAccountId() != null) {
                System.out.println("<<<<<<<<<< Delete the smartmoeny account as exception has occured >>>>>>>>>");
                this.linkPaymentModeDAO.deleteByPrimaryKey(smartMoneyAccountModel.getSmartMoneyAccountId());
                System.out.println("<<<<<<<<<< Smartmoeny account is Deleted >>>>>>>>>");
            }
            if (/* firstSmartMoneyAccount && */!phoenixSuccessful) {
                // phoenix had a problem, verifly was hit, so rollback verifly.
                // TODO: here code to delete the verifly account
                try {
                    abstractFinancialInstitution.deleteAccount(veriflyBaseWrapper);
                } catch (Exception exp) {
                    ex = exp;
                }
                System.out.println("Phoenix had a problem");
            }

            throw new FrameworkCheckedException(ex.getMessage(), ex);
        }

        // step 4 ------ OLA Account Creation
        baseWrapper.putObject("bankModel", getOlaBankMadal());


        OLAVO olaVo = new OLAVO();


        olaVo.setFirstName(appUserModel.getFirstName());
        olaVo.setMiddleName(" ");
        olaVo.setLastName(appUserModel.getLastName());
        olaVo.setFatherName(appUserModel.getLastName());


        olaVo.setCnic(appUserModel.getNic());


        if (appUserModel.getAddress1() == null)
            olaVo.setAddress("Lahore");
        else
            olaVo.setAddress(appUserModel.getAddress1());


        olaVo.setLandlineNumber(appUserModel.getMobileNo());
        olaVo.setMobileNumber(appUserModel.getMobileNo());
        if (appUserModel.getDob() == null)
            olaVo.setDob(new Date());
        else
            olaVo.setDob(appUserModel.getDob());
        olaVo.setStatusId(1l);
        // olaVo.setBalance(dcm.getBalance()); //balance is yet to be decided
        BankModel bankModel = (BankModel) baseWrapper.getObject("bankModel");

        SwitchWrapper sWrapper = new SwitchWrapperImpl();
        sWrapper.setOlavo(olaVo);
        sWrapper.setBankId(bankModel.getBankId());

        try {

            sWrapper = olaVeriflyFinancialInstitution.createAccount(sWrapper);
        } catch (Exception e) {
            e.printStackTrace();

            throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);


        }

        if ("07".equals(olaVo.getResponseCode())) {
            throw new FrameworkCheckedException("NIC already exisits in the OLA accounts");
        }

        olaVo = sWrapper.getOlavo();

        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setAccountNo(olaVo.getPayingAccNo().toString());
        accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
        accountInfoModel.setActive(smartMoneyAccountModel.getActive());

        //Added after HSM Integration
        accountInfoModel.setPan(PanGenerator.generatePAN());
        // End HSM Integration Change

        accountInfoModel.setCreatedOn(smartMoneyAccountModel.getCreatedOn());
        accountInfoModel.setUpdatedOn(smartMoneyAccountModel.getUpdatedOn());
        accountInfoModel.setCustomerId(customerModel.getCustomerId());

        accountInfoModel.setCustomerMobileNo(appUserModel
                .getMobileNo());
        accountInfoModel.setFirstName(appUserModel
                .getFirstName());
        accountInfoModel.setLastName(appUserModel
                .getLastName());
        accountInfoModel.setPaymentModeId(smartMoneyAccountModel
                .getPaymentModeId());


        accountInfoModel.setDeleted(Boolean.FALSE);


        veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        // VeriflyManager veriflyMgr =
        // veriflyManagerService.getVeriflyMgrByBankId(bankModel);
        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        LogModel logmodel = new LogModel();
        logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
        veriflyBaseWrapper.setLogModel(logmodel);
        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,
                DeviceTypeConstantsInterface.WEB);
        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,
                DeviceTypeConstantsInterface.WEB);

        try {
            veriflyBaseWrapper = abstractFinancialInstitution.generatePin(veriflyBaseWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (veriflyBaseWrapper.isErrorStatus()) {

            Object[] args = {
                    veriflyBaseWrapper.getAccountInfoModel().getAccountNick(),
                    veriflyBaseWrapper.getAccountInfoModel().getAccountNick(),
                    veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin()};
            // messageString =
            // MessageUtil.getMessage("linkPaymentMode.successMessage",
            // args);
            /**
             * Phoenix call if verifly Lite (bank) is required. And First
             * Account is being created.
             */
        }

        /**
         * Sending SMS
         */

//TODO URL will handle later...........

//		String messageString = this.messageSource.getMessage("smsCommand.act_sms6", null, null)+" "+
//								mfsId+" and PIN is: "+randomPin+". Point browser to "+this.mfsURL;
        String messageString = "";
//		if(mfsAccountModel.getConnectionType() != null && mfsAccountModel.getConnectionType().equals(CommandFieldConstants.KENNEL_PREPAID_VALUE))
//		{
//			//changed by Maqsood Shahzad (13th Aug 2007)
//			messageString = this.messageSource.getMessage("smsCommand.act_sms8", null, null);
//			SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), messageString, SMSConstants.Sender_1611);
//			smsSender.send(smsMessage);
//		}
//		else if(mfsAccountModel.getConnectionType() != null && mfsAccountModel.getConnectionType().equals(CommandFieldConstants.KENNEL_POSTPAID_VALUE))
        {
            //changed by Maqsood Shahzad (13th Aug 2007)
            Object[] args = {mfsId, randomPin, MessageUtil.getMessage("mfsDownloadURL")};
            messageString = MessageUtil.getMessage("smsCommand.act_sms6", args);
            SmsMessage smsMessage = new SmsMessage(appUserModel.getMobileNo(), messageString);
            smsSender.send(smsMessage);

//			messageString = MessageUtil.getMessage("smsCommand.bankInfo", args);
//			smsMessage.setMessageText(messageString);
//			smsSender.sendDelayed(smsMessage);

//			messageString = this.messageSource.getMessage("smsCommand.act_sms6", null, null)+" "+
//			mfsId+" and PIN is: "+randomPin+". Point browser to "+this.messageSource.getMessage("mfsDownloadURL", null, null);
        }


        /**
         * Logging Information, Ending Status
         */

        baseWrapper.putObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY, mfsAccountModel);
        return baseWrapper;
    }


    public BaseWrapper searchUserInfoByPrimaryKey(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        UserInfoListViewModel userInfoListViewModel = (UserInfoListViewModel) baseWrapper.getBasePersistableModel();
        userInfoListViewModel = this.userInfoListViewDAO.findByPrimaryKey(userInfoListViewModel.getAppUserId());
        baseWrapper.setBasePersistableModel(userInfoListViewModel);
        return baseWrapper;
    }

    public AppUserModel getAppUserModel(AppUserModel exampleInstance) throws FrameworkCheckedException {

        CustomList<AppUserModel> customList = appUserDAO.findByExample(exampleInstance, null);

        if (customList != null && !customList.getResultsetList().isEmpty()) {
            logger.info("Found Total AppUsers = " + customList.getResultsetList().size());
            return customList.getResultsetList().get(0);
        }

        return null;
    }


    @Override
    public boolean isAppUserExist(String userName)
            throws FrameworkCheckedException {
        boolean flag = false;
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setUsername(userName);
        CustomList<AppUserModel> customList = appUserDAO.findByExample(appUserModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        if (null != customList && customList.getResultsetList().size() > 0) {
            flag = true;
        }

        return flag;
    }

    /**
     * Setter Methods
     */
    public void setAppUserDAO(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

    public void setCustTransManager(CustTransManager custTransManager) {
        this.custTransManager = custTransManager;
    }

    public void setUserInfoListViewDAO(UserInfoListViewDAO userInfoListViewDAO) {
        this.userInfoListViewDAO = userInfoListViewDAO;
    }

    public void setSequenceGenerator(
            OracleSequenceGeneratorJdbcDAO sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    public void setGoldenNosDAO(GoldenNosDAO goldenNosDAO) {
        this.goldenNosDAO = goldenNosDAO;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }

    public void setAddressDAO(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public void setCustomerAddressesDAO(CustomerAddressesDAO customerAddressesDAO) {
        this.customerAddressesDAO = customerAddressesDAO;
    }

    public void setSegmentDAO(SegmentDAO segmentDAO) {
        this.segmentDAO = segmentDAO;
    }

    public void setLanguageDAO(LanguageDAO languageDAO) {
        this.languageDAO = languageDAO;
    }

    public void setCustomerTypeDAO(CustomerTypeDAO customerTypeDAO) {
        this.customerTypeDAO = customerTypeDAO;
    }

    public void setOlaCustomerAccountTypeDao(OlaCustomerAccountTypeDao olaCustomerAccountTypeDao) {
        this.olaCustomerAccountTypeDao = olaCustomerAccountTypeDao;
    }

    public void setFundSourceDAO(FundSourceDAO fundSourceDAO) {
        this.fundSourceDAO = fundSourceDAO;
    }

    public OLAVO getAccountInfoFromOLA(String cnic, Long bankId)
            throws FrameworkCheckedException {

        SwitchWrapper sWrapper = new SwitchWrapperImpl();

        OLAVO olaVo = new OLAVO();
        olaVo.setCnic(cnic);

        sWrapper.setOlavo(olaVo);
        sWrapper.setBankId(bankId);

        OLAVO result = null;
        try {
            sWrapper = olaVeriflyFinancialInstitution.getAccountInfo(sWrapper);
            if (sWrapper != null) {
                result = sWrapper.getOlavo();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
        }

        return result;
    }

    public SmartMoneyAccountModel getSmartMoneyAccountByCustomerId(Long customerId)
            throws FrameworkCheckedException {
        SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
        smartMoneyAccountModel.setCustomerId(customerId);
        //smartMoneyAccountModel.setPaymentModeId(paymentModeId);
        smartMoneyAccountModel.setActive(true);
        smartMoneyAccountModel.setDeleted(false);
        CustomList<SmartMoneyAccountModel> results = smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
        if (results != null && results.getResultsetList().size() > 0) {
            return results.getResultsetList().get(0);
        }
        return null;
    }

    @Override
    public void accountBlock(BaseWrapper baseWrapper) {
        MfsAccountModel mfsAccountModel = (MfsAccountModel) baseWrapper.getObject(MfsAccountModel.MFS_ACCOUNT_MODEL_KEY);

        AppUserModel appUserModel = this.appUserDAO.findByPrimaryKey(mfsAccountModel.getAppUserId());
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();

        try {
            baseWrapper.setBasePersistableModel(appUserModel);
            baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
            uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
            uda.setAccountLocked(true);
            baseWrapper.setBasePersistableModel(uda);
            this.getCommonCommandManager().updateUserDeviceAccounts(baseWrapper);
            appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_WARM);
            baseWrapper.setBasePersistableModel(appUserModel);
            this.getCommonCommandManager().updateAppUser(baseWrapper);
            BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
            blinkCustomerModel = getCommonCommandManager().loadBlinkCustomerByMobileAndAccUpdate(mfsAccountModel.getMobileNo(), 1L);
            blinkCustomerModel.setClsResponseCode("True Match-Compliance");
            blinkCustomerModel.setUpdatedOn(new Date());
            blinkCustomerModelDAO.saveOrUpdate(blinkCustomerModel);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<SmartMoneyAccountModel> getLastClosedSMAAccount(SmartMoneyAccountModel smartMoneyAccountModel) {
        return smartMoneyAccountDAO.getLastClosedSMAAccount(smartMoneyAccountModel);
    }

    @Override
    public AccountModel getLastClosedAccountModel(String cnic, Long customerAccountTypeId) {
        return accountDAO.getLastClosedAccountModel(cnic, customerAccountTypeId);
    }

    @Override
    public AccountModel getAccountModelByCnicAndCustomerAccountTypeAndStatusId(String cnic, Long customerAccountTypeId, Long statusId) {
        AccountModel accountModel = accountDAO.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(cnic, customerAccountTypeId, statusId);
        return accountModel;
    }

    @Override
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

    public SmartMoneyAccountModel getOLASmartMoneyAccountForAgent(Long retailerContactId) throws FrameworkCheckedException {
        SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
        smartMoneyAccountModel.setRetailerContactId(retailerContactId);
        smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        smartMoneyAccountModel.setActive(true);
        smartMoneyAccountModel.setDeleted(false);
        CustomList<SmartMoneyAccountModel> results = smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
        if (results != null && results.getResultsetList().size() > 0) {
            return results.getResultsetList().get(0);
        }
        return null;
    }

    /**
     * @param _smartMoneyAccountModel
     * @return
     * @throws FrameworkCheckedException
     */
    public SmartMoneyAccountModel getSmartMoneyAccount(SmartMoneyAccountModel _smartMoneyAccountModel) throws FrameworkCheckedException {

        SmartMoneyAccountModel smartMoneyAccountModel = null;

        _smartMoneyAccountModel.setActive(true);
        _smartMoneyAccountModel.setDeleted(false);

        smartMoneyAccountModel = smartMoneyAccountDAO.getSmartMoneyAccountByWalkinCustomerId(_smartMoneyAccountModel);

//		if(results != null && results.getResultsetList().size() > 0) {
//
//			smartMoneyAccountModel = results.getResultsetList().get(0);
//		}

        return smartMoneyAccountModel;
    }

    public UserDeviceAccountsModel getDeviceAccountByAppUserId(Long appUserId, Long deviceTypeId)
            throws FrameworkCheckedException {
        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(appUserId);
        if (deviceTypeId != null) {
            userDeviceAccountsModel.setDeviceTypeId(deviceTypeId);
        }
        CustomList<UserDeviceAccountsModel> results = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel);
        if (results != null && results.getResultsetList().size() > 0) {
            return results.getResultsetList().get(0);
        }
        return null;
    }

    /**
     * @param _walkinCustomerModel
     * @return
     */
    public WalkinCustomerModel getWalkinCustomerModel(WalkinCustomerModel _walkinCustomerModel) {

        WalkinCustomerModel walkinCustomerModel = null;
        //add exampleConfigHOlder to avoid like operator in searching
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setMatchMode(MatchMode.EXACT);
        exampleHolder.setEnableLike(Boolean.FALSE);

        CustomList<WalkinCustomerModel> customList = (CustomList<WalkinCustomerModel>)
                walkinCustomerDAO.findByExample(_walkinCustomerModel, null, null, exampleHolder);

        if (customList != null && !customList.getResultsetList().isEmpty()) {
            walkinCustomerModel = customList.getResultsetList().get(0);
        }

        return walkinCustomerModel;
    }

    public List<BulkDisbursementsModel> validateMobileNos(List<BulkDisbursementsModel> bulkList) throws FrameworkCheckedException {
        return this.userInfoListViewDAO.validateMobileNos(bulkList);
    }

    public Boolean isAlreadyRegistered(String CNIC, String mobileNumber) throws FrameworkCheckedException {

        Long appUserTypeId = -1L;

        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setNic(CNIC);
        appUserModel = getAppUserModel(appUserModel);

        if (appUserModel != null && appUserModel.getAppUserTypeId() != null) {

            appUserTypeId = appUserModel.getAppUserTypeId();
            logger.info("[MfsAccountManagerImpl.isAlreadyRegistered] AppUser Found, Name = " + appUserModel.getFirstName() + " Id = " + appUserModel.getAppUserId() + " Type " + appUserTypeId + " CNIC " + appUserModel.getNic() + " Mobile " + appUserModel.getMobileNo());
        }

        if (UserTypeConstantsInterface.CUSTOMER.longValue() == appUserTypeId.longValue()) {
            throw new FrameworkCheckedException("Against Sender/Receiver CNIC BB Customer already registered. Cannot proceed with the transaction.");
        }

        if (UserTypeConstantsInterface.RETAILER.longValue() == appUserTypeId.longValue()) {
            throw new FrameworkCheckedException("Against Sender/Receiver CNIC an Agent already registered. Cannot proceed with the transaction.");
        }

        return Boolean.FALSE;
    }

    public Boolean isAlreadyRegistered(String CNIC) throws FrameworkCheckedException {
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setNic(CNIC);

        CustomList<AppUserModel> customList = appUserDAO.findByExample(appUserModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        if (customList != null && !customList.getResultsetList().isEmpty()) {
            List<AppUserModel> appUserModelList = customList.getResultsetList();

            for (AppUserModel appUser : appUserModelList) {
                if (appUser.getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
                    return Boolean.TRUE;
                }
            }
        }

        return Boolean.FALSE;
    }

    @Override
    public List<BulkDisbursementsModel> isAlreadyRegistered(List<BulkDisbursementsModel> bulkList) throws FrameworkCheckedException {
        logger.info("Bulk Payments: Validating if any BB Customer/Agent is already registered with the CNICs provided in CSV file.");
        long start = System.currentTimeMillis();
        bulkList = appUserDAO.isAlreadyRegistered(bulkList, UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER);
        long end = System.currentTimeMillis();
        logger.info("Bulk Payments: Time(ms) Taken to validate " + bulkList.size() + " records from DB: " + (end - start));
        return bulkList;
    }

    @Override
    public List<BulkCustomerAccountVo> isCustomerOrAgent(List<BulkCustomerAccountVo> bulkCustomerAccountVoList) throws FrameworkCheckedException {
        logger.info("Bulk Customers Upload: Validating if any BB Customer/Agent is already registered with the Mobile Numbers, CNICs provided in CSV file.");
        long start = System.currentTimeMillis();
        for (BulkCustomerAccountVo bulkCustomerAccountVo : bulkCustomerAccountVoList) {
            if (!this.isMobileNumUnique(bulkCustomerAccountVo.getMobileNo(), null)) {
                bulkCustomerAccountVo.setValidRecord(Boolean.FALSE);
            }
            if (!this.isNICUnique(bulkCustomerAccountVo.getCnic(), null)) {
                bulkCustomerAccountVo.setValidRecord(Boolean.FALSE);
            }

            if (bulkCustomerAccountVo.getIsLevel2() != null && bulkCustomerAccountVo.getIsLevel2().booleanValue() && !this.isInitialAppFormNoUnique(bulkCustomerAccountVo.getInitialAppFormNo(), CustomerAccountTypeConstants.LEVEL_2)) {
                bulkCustomerAccountVo.setValidRecord(Boolean.FALSE);
            }

        }
        long end = System.currentTimeMillis();
        logger.info("Bulk Customers Upload: Time(ms) Taken to validate " + bulkCustomerAccountVoList.size() + " records from DB: " + (end - start));
        return bulkCustomerAccountVoList;
    }

    @Override
    public void enqueueBulkCustomerAccountsCreation(List<BulkCustomerAccountVo> bulkCustomerAccountVoList) throws FrameworkCheckedException {
        for (BulkCustomerAccountVo bulkCustomerAccountVo : bulkCustomerAccountVoList) {
            String xml = null;
            try {
                xml = xmlMarshaller.marshal(bulkCustomerAccountVo);
                jmsProducer.produce(xml, DestinationConstants.BULK_CUSTOMER_DESTINATION);
            } catch (XmlMappingException | IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * @param cnic
     * @return
     */
    public Boolean updateWalkinCustomerIfExists(String walkInCNIC, String walkInMobileNumber, String senderMobileNumber) throws FrameworkCheckedException {

        logger.info("[MfsAccountManagerImpl.isWalkinCustomerExists] walkInCNIC " + walkInCNIC + " walkInMobileNumber " + walkInMobileNumber);

        Boolean isWalkinCustomerExist = Boolean.FALSE;

        if (!StringUtils.isEmpty(walkInCNIC) && !StringUtils.isEmpty(walkInMobileNumber)) {

            WalkinCustomerModel existingCnicWalkinCustomer = walkinCustomerDAO.getWalkinCustomerByCNIC(walkInCNIC);

            if (existingCnicWalkinCustomer != null) {

                isWalkinCustomerExist = Boolean.TRUE;
                //Check if OLA Account exists for this walkin Customer
                OLAVO olaVO = new OLAVO();
                olaVO.setCnic(CommonUtils.maskWalkinCustomerCNIC(walkInCNIC));
                olaVO.setCustomerAccountTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);

                SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                switchWrapper.setBankId(50110L);
                switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                switchWrapper.setOlavo(olaVO);
                logger.info("[MfsAccountManagerImpl.isWalkinCustomerExist] : Getting OLA AccountInfo to check if account already exists in OLA or not. CNIC:" + olaVO.getCnic());
                switchWrapper = olaVeriflyFinancialInstitution.getAccountInfo(switchWrapper);

                if (switchWrapper.getOlavo().getResponseCode().equals("03")) {//Account not found in OLA, Create new account for this walkin customer

                    AppUserModel appUserModelWalkin = new AppUserModel();
                    appUserModelWalkin.setFirstName("-");
                    appUserModelWalkin.setLastName("-");
                    appUserModelWalkin.setAddress1("-");
                    appUserModelWalkin.setMobileNo(walkInMobileNumber);
                    appUserModelWalkin.setNic(walkInCNIC);
                    appUserModelWalkin.setAppUserTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);
                    createWalkinCustomerOLAAccount(appUserModelWalkin, null);
                }

                if (!existingCnicWalkinCustomer.getMobileNumber().equals(walkInMobileNumber)) {
                    logger.info("[MfsAccountManagerImpl.isWalkinCustomerExist] : Mobile # " + walkInMobileNumber + " Updation Space available.");

                    updateWalkinCustomerAccount(existingCnicWalkinCustomer, walkInMobileNumber);
                    updateWalkInAppUser(existingCnicWalkinCustomer.getCnic(), existingCnicWalkinCustomer.getMobileNumber(), walkInMobileNumber);
                    AccountHolderModel accountHolderModel = new AccountHolderModel();
                    accountHolderModel.setCnic(olaVO.getCnic());

                    List<AccountHolderModel> accountHolderModelList = accountHolderDAO.findByExample(accountHolderModel).getResultsetList();
                    if (accountHolderModelList != null && accountHolderModelList.size() != 0) {
                        accountHolderModel = accountHolderModelList.get(0);
                        accountHolderModel.setMobileNumber(walkInMobileNumber);
                        accountHolderDAO.saveOrUpdate(accountHolderModel);
                    }
                }

            }
			/*else if(existingCnicWalkinCustomer == null) {	//THIS CODE IS MOVED UP IN HIERARCHY IN CommonCommandManagerImpl to make creation transactional.

				 existingCnicWalkinCustomer = walkinCustomerDAO.getWalkinCustomerByMobile(walkInMobileNumber);
				if(existingCnicWalkinCustomer != null) {

					isWalkinCustomerExist = Boolean.TRUE;
					//Check if OLA Account exists for this walkin Customer
					OLAVO olaVO = new OLAVO();
					olaVO.setCnic(CommonUtils.maskWalkinCustomerCNIC(walkInCNIC));
					olaVO.setCustomerAccountTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);

					SwitchWrapper switchWrapper = new SwitchWrapperImpl();
					switchWrapper.setBankId(50110L);
					switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
					switchWrapper.setOlavo(olaVO);

//					baseWrapper = this.accountFacade.loadAccount(baseWrapper) ;


					if (!existingCnicWalkinCustomer.getCnic().equals(walkInCNIC)) {
						logger.info("[MfsAccountManagerImpl.isWalkinCustomerExist] : Mobile # " + walkInMobileNumber + " Updation Space available.");

						String oldCnic=existingCnicWalkinCustomer.getCnic();
						updateWalkinCustomerAccountByCnic(existingCnicWalkinCustomer, walkInCNIC);

						updateWalkInAppUserByCnic(oldCnic, existingCnicWalkinCustomer.getMobileNumber(), walkInCNIC);
						logger.info("[MfsAccountManagerImpl.isWalkinCustomerExist] : Getting OLA AccountInfo to check if account already exists in OLA or not. CNIC:" + olaVO.getCnic());
						switchWrapper = olaVeriflyFinancialInstitution.getAccountInfo(switchWrapper);
						if(switchWrapper.getOlavo().getResponseCode().equals("03")){//Account not found in OLA, Create new account for this walkin customer

							accountHolderDAO.updateCnic(existingCnicWalkinCustomer.getCnic()+"-W",existingCnicWalkinCustomer.getMobileNumber());
						}

					}
				}
			}*/
        }

        return isWalkinCustomerExist;
    }

    /**
     * @param cnic
     * @param mobileNumer
     * @param mobileNumberUpdated
     */
    public void updateWalkInAppUser(String cnic, String mobileNumer, String mobileNumberUpdated) {

        logger.info("[MfsAccountManagerImpl.updateWalkInAppUser] Going to update AppUserModel for Walkin Customer details for cnic " + cnic + ". Old mobileNumer " + mobileNumer + " replaced with updated mobileNumber: " + mobileNumberUpdated);

        AppUserModel customerModel = new AppUserModel();
        customerModel.setAppUserTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);
//		customerModel.setMobileNo(mobileNumer);
        customerModel.setNic(cnic);

        SearchBaseWrapper _searchBaseWrapper = new SearchBaseWrapperImpl();
        _searchBaseWrapper.setBasePersistableModel(customerModel);

        SearchBaseWrapper searchBaseWrapper = loadAppUserByMobileNumberAndType(_searchBaseWrapper);

        AppUserModel appUserModel = (AppUserModel) searchBaseWrapper.getBasePersistableModel();

        if (appUserModel != null) {

            appUserModel.setMobileNo(mobileNumberUpdated);
            appUserModel.setUpdatedOn(new Date());
            appUserDAO.saveOrUpdate(appUserModel);
            logger.info("[MfsAccountManagerImpl.updateWalkInAppUser] Update successful for CNIC: " + cnic + ". Old Mobile # " + mobileNumer + " New Mobile # " + mobileNumberUpdated);
        }
    }

    public void updateWalkInAppUserByCnic(String cnic, String mobileNumer, String cnicUpdated) {

        logger.info("[MfsAccountManagerImpl.updateWalkInAppUser] Going to update AppUserModel for Walkin Customer details for cnic " + cnic + ". Old mobileNumer " + mobileNumer + " replaced with updated mobileNumber: " + cnicUpdated);

        AppUserModel customerModel = new AppUserModel();
        customerModel.setAppUserTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);
//		customerModel.setMobileNo(mobileNumer);
        customerModel.setNic(cnic);

        SearchBaseWrapper _searchBaseWrapper = new SearchBaseWrapperImpl();
        _searchBaseWrapper.setBasePersistableModel(customerModel);

        SearchBaseWrapper searchBaseWrapper = loadAppUserByMobileNumberAndType(_searchBaseWrapper);

        AppUserModel appUserModel = (AppUserModel) searchBaseWrapper.getBasePersistableModel();

        if (appUserModel != null) {

            appUserModel.setNic(cnicUpdated);
            appUserModel.setUpdatedOn(new Date());
            appUserDAO.saveOrUpdate(appUserModel);
            logger.info("[MfsAccountManagerImpl.updateWalkInAppUser] Update successful for CNIC: " + cnic + ". Old Mobile # " + mobileNumer + " New Mobile # " + cnicUpdated);
        }
    }

    public SearchBaseWrapper loadAppUserByMobileNumberAndType(SearchBaseWrapper searchBaseWrapper) {

        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setEnableLike(Boolean.FALSE);
        exampleHolder.setMatchMode(MatchMode.EXACT);
        exampleHolder.setIgnoreCase(false);

        AppUserModel appUserModel = (AppUserModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<AppUserModel> list = this.appUserDAO.findByExample(appUserModel, null, null, exampleHolder);

        SearchBaseWrapper _searchBaseWrapper = new SearchBaseWrapperImpl();

        if (null != list.getResultsetList() && list.getResultsetList().size() > 0) {

            _searchBaseWrapper.setBasePersistableModel(list.getResultsetList().get(0));

        } else {

            _searchBaseWrapper.setBasePersistableModel(null);
        }

        return _searchBaseWrapper;
    }

    /**
     * @param walkinCustomerModel
     * @param baseWrapper
     * @return
     * @throws FrameworkCheckedException
     */
    private Boolean updateWalkinCustomerAccount(WalkinCustomerModel walkinCustomerModel, String walkInMobileNumber) throws FrameworkCheckedException {

        logger.info("[MfsAccountManager.updateWalkinCustomerAccount] updating walkin customer mobile no to " + walkInMobileNumber);

        Date nowDate = new Date();
        walkinCustomerModel.setUpdatedOn(nowDate);
        walkinCustomerModel.setDescription("Walkin Customer");
        walkinCustomerModel.setMobileNumber(walkInMobileNumber);

        walkinCustomerModel = this.walkinCustomerDAO.saveOrUpdate(walkinCustomerModel);

        return walkinCustomerModel.getWalkinCustomerId() != null;

    }

    private Boolean updateWalkinCustomerAccountByCnic(WalkinCustomerModel walkinCustomerModel, String walkInCnic) throws FrameworkCheckedException {

        logger.info("[MfsAccountManager.updateWalkinCustomerAccount] updating walkin customer mobile no to " + walkInCnic);

        Date nowDate = new Date();
        walkinCustomerModel.setUpdatedOn(nowDate);
        walkinCustomerModel.setDescription("Walkin Customer");
        walkinCustomerModel.setCnic(walkInCnic);

        walkinCustomerModel = this.walkinCustomerDAO.saveOrUpdate(walkinCustomerModel);

        return walkinCustomerModel.getWalkinCustomerId() != null;

    }

    /**
     * @param baseWrapper
     * @return
     * @throws FrameworkCheckedException
     */
    public BaseWrapper createWalkinCustomerAccount(String walkInCnic, String walkInMobileNumber, String senderMobileNumber) throws FrameworkCheckedException {

        logger.info("[MfsAccountManager.createWalkinCustomerAccount] Loggedin AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Walkin customer CNIC: " + walkInCnic + ". Walkin Custoemr Mobile No:" + walkInMobileNumber + ". Sender Mobile No:" + senderMobileNumber);

        String username = String.valueOf(System.currentTimeMillis());
        String password = username;

        Date nowDate = new Date();
        Date expiryDate = PortalDateUtils.addYears(nowDate, CommandConstants.ADD_ONE_YEARS_IN_CURRENT_DATE);
        Date dateOfBirth = PortalDateUtils.subtractYears(nowDate, 18);
        SmartMoneyAccountModel smartMoneyAccountModel = null;
        AppUserModel appUserModelWalkin = null;
        AppUserModel createdUpdatedByAppUserModel = ThreadLocalAppUser.getAppUserModel();
        BankModel bankModel = null;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        AbstractFinancialInstitution abstractFinancialInstitution = null;
        OLAVO olaVo = null;

        try {

            if (createdUpdatedByAppUserModel == null) {

                createdUpdatedByAppUserModel = new AppUserModel();
                createdUpdatedByAppUserModel.setMobileNo(senderMobileNumber);
                createdUpdatedByAppUserModel = getAppUserModel(createdUpdatedByAppUserModel);
                logger.info("creating createdUpdatedByAppUserModel (Name " + createdUpdatedByAppUserModel.getFirstName() + ")");
            }

            WalkinCustomerModel walkinCustomerModel = new WalkinCustomerModel();
            walkinCustomerModel.setUpdatedByAppUserModel(createdUpdatedByAppUserModel);
            walkinCustomerModel.setCreatedByAppUserModel(createdUpdatedByAppUserModel);
            walkinCustomerModel.setCreatedOn(nowDate);
            walkinCustomerModel.setUpdatedOn(nowDate);
            walkinCustomerModel.setDescription("Walkin Customer");
            walkinCustomerModel.setCnic(walkInCnic);
            walkinCustomerModel.setMobileNumber(walkInMobileNumber);

            logger.info("creating walkinCustomerModel (cnic " + walkInCnic + ")");

            walkinCustomerModel = this.walkinCustomerDAO.saveOrUpdate(walkinCustomerModel);

            appUserModelWalkin = new AppUserModel();
            appUserModelWalkin.setFirstName("-");
            appUserModelWalkin.setLastName("-");
            appUserModelWalkin.setAddress1("-");
            appUserModelWalkin.setAddress2("-");
            appUserModelWalkin.setCity("-");
            appUserModelWalkin.setCountry("-");
            appUserModelWalkin.setState("-");
            appUserModelWalkin.setMotherMaidenName("-");
            appUserModelWalkin.setDob(dateOfBirth);
            appUserModelWalkin.setMobileTypeId(1L);
            appUserModelWalkin.setMobileNo(walkInMobileNumber);
            appUserModelWalkin.setNic(walkInCnic);
            appUserModelWalkin.setNicExpiryDate(expiryDate);
            appUserModelWalkin.setCreatedByAppUserModel(createdUpdatedByAppUserModel);
            appUserModelWalkin.setUpdatedByAppUserModel(createdUpdatedByAppUserModel);
            appUserModelWalkin.setCreatedOn(nowDate);
            appUserModelWalkin.setUpdatedOn(nowDate);
            appUserModelWalkin.setAppUserTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);
            appUserModelWalkin.setVerified(Boolean.TRUE);
            appUserModelWalkin.setAccountEnabled(Boolean.TRUE);
            appUserModelWalkin.setAccountExpired(Boolean.FALSE);
            appUserModelWalkin.setAccountLocked(Boolean.FALSE);
            appUserModelWalkin.setCredentialsExpired(Boolean.FALSE);
            appUserModelWalkin.setPasswordChangeRequired(Boolean.FALSE);
            appUserModelWalkin.setWalkinCustomerModel(walkinCustomerModel);
            appUserModelWalkin.setUsername(username);
            appUserModelWalkin.setPassword(password);

            logger.info("creating appUserModelWalkin (cnic " + walkInCnic + ")");

            appUserModelWalkin = this.appUserDAO.saveOrUpdate(appUserModelWalkin);

            bankModel = getOlaBankMadal();

            baseWrapper.setBasePersistableModel(bankModel);

            logger.info("loading loadFinancialInstitution (...)");
            abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

            smartMoneyAccountModel = new SmartMoneyAccountModel();
            smartMoneyAccountModel.setWalkinCustomerModel(walkinCustomerModel);
            smartMoneyAccountModel.setBankId(getOlaBankMadal().getBankId());
            smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            smartMoneyAccountModel.setBankId(bankModel.getBankId());
            smartMoneyAccountModel.setCreatedOn(nowDate);
            smartMoneyAccountModel.setUpdatedOn(nowDate);
            smartMoneyAccountModel.setCreatedByAppUserModel(createdUpdatedByAppUserModel);
            smartMoneyAccountModel.setUpdatedByAppUserModel(createdUpdatedByAppUserModel);
            smartMoneyAccountModel.setActive(Boolean.TRUE);
            smartMoneyAccountModel.setDefAccount(Boolean.TRUE);
            smartMoneyAccountModel.setDeleted(Boolean.FALSE);
            smartMoneyAccountModel.setName("i8_wc_" + username);

            if (abstractFinancialInstitution.isVeriflyRequired()) {

                if (abstractFinancialInstitution.isVeriflyLite()) {

                    smartMoneyAccountModel.setChangePinRequired(Boolean.FALSE);
                } else {
                    smartMoneyAccountModel.setChangePinRequired(Boolean.TRUE);
                }

            } else {
                smartMoneyAccountModel.setChangePinRequired(Boolean.FALSE);
            }

            logger.info("[MfsAccountManager.createWalkinCustomerAccount] creating smartMoneyAccountModel for Walkin Customer ID:" + walkinCustomerModel.getWalkinCustomerId() + ". Loggedin AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Walkin customer CNIC: " + walkInCnic + ". Walkin Custoemr Mobile No:" + walkInMobileNumber + ". Sender Mobile No:" + senderMobileNumber);

            smartMoneyAccountModel = this.linkPaymentModeDAO.saveOrUpdate(smartMoneyAccountModel);

            baseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            // step 4 ------ OLA Account Creation
            baseWrapper.putObject("bankModel", bankModel);

            logger.info("creating createWalkinCustomerOLAAccount (appUserModelWalkin " + appUserModelWalkin.getNic() + ")");

            olaVo = createWalkinCustomerOLAAccount(appUserModelWalkin, bankModel);

        } catch (ImplementationNotSupportedException | JDBCException sqlExcetion) {

            logger.error(ExceptionProcessorUtility.getStackTrace(sqlExcetion));
            baseWrapper.putObject("ErrMessage", sqlExcetion.getMessage());
            throw new FrameworkCheckedException(sqlExcetion.getMessage());

        } catch (Exception exception) {

            logger.error(ExceptionProcessorUtility.getStackTrace(exception));
            baseWrapper.putObject("ErrMessage", exception.getMessage());

            if (exception.getCause() instanceof ConstraintViolationException) {

                ConstraintViolationException constrainViolationException = (ConstraintViolationException) exception.getCause();

                if (constrainViolationException.getConstraintName().indexOf("UK_SM_ACCOUNT") != -1) {
                    throw new FrameworkCheckedException("Account nick already exists");
                }
            }

            // This is a tuch to handle the smartmoney account created in case of verifly exception. this is because the transaction is not getting rolled back
            if (smartMoneyAccountModel != null && smartMoneyAccountModel.getSmartMoneyAccountId() != null) {

                try {

                    this.linkPaymentModeDAO.deleteByPrimaryKey(smartMoneyAccountModel.getSmartMoneyAccountId());

                } catch (Exception e) {

                    logger.error(ExceptionProcessorUtility.getStackTrace(e));
                    throw new FrameworkCheckedException(e.getMessage());
                }
            }

            throw new FrameworkCheckedException(exception.getMessage(), exception);
        }

        return baseWrapper;
    }

    /**
     * @param baseWrapper
     * @return
     * @throws FrameworkCheckedException
     */
    public BaseWrapper createWalkinCustomerAccount(String walkInCnic, String walkInMobileNumber, AppUserModel createdByAppUserModel) throws FrameworkCheckedException {
        logger.info("[MfsAccountManager.createWalkinCustomerAccount] Walkin customer CNIC: " + walkInCnic + ". Walkin Custoemr Mobile No:" + walkInMobileNumber + ". Created By App User ID:" + createdByAppUserModel.getAppUserId());
        //check whether this CNIC or MobileNo already associated to someone else without closed-settled status - turab
        if (!this.isMobileNumUnique(walkInMobileNumber, null)) {
            throw new FrameworkCheckedException("MobileNumUniqueException");
        }
        if (!this.isNICUnique(walkInCnic, null)) {
            throw new FrameworkCheckedException("NICUniqueException");
        }

        ThreadLocalAppUser.setAppUserModel(createdByAppUserModel);
        String username = String.valueOf(System.currentTimeMillis()) + walkInCnic.substring(8);//Last 4 digits
        String password = username;

        Date nowDate = new Date();
        Date expiryDate = PortalDateUtils.addYears(nowDate, CommandConstants.ADD_ONE_YEARS_IN_CURRENT_DATE);
        Date dateOfBirth = PortalDateUtils.subtractYears(nowDate, 18);
        SmartMoneyAccountModel smartMoneyAccountModel = null;
        AppUserModel appUserModelWalkin = null;
        BankModel bankModel = null;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        AbstractFinancialInstitution abstractFinancialInstitution = null;
        OLAVO olaVo = null;

        try {

            WalkinCustomerModel walkinCustomerModel = new WalkinCustomerModel();
            walkinCustomerModel.setUpdatedByAppUserModel(createdByAppUserModel);
            walkinCustomerModel.setCreatedByAppUserModel(createdByAppUserModel);
            walkinCustomerModel.setCreatedOn(nowDate);
            walkinCustomerModel.setUpdatedOn(nowDate);
            walkinCustomerModel.setDescription("Walkin Customer");
            walkinCustomerModel.setCnic(walkInCnic);
            walkinCustomerModel.setMobileNumber(walkInMobileNumber);

            logger.info("creating walkinCustomerModel (cnic " + walkInCnic + ")");

            walkinCustomerModel = this.walkinCustomerDAO.saveOrUpdate(walkinCustomerModel);

            appUserModelWalkin = new AppUserModel();
            appUserModelWalkin.setFirstName("-");
            appUserModelWalkin.setLastName("-");
            appUserModelWalkin.setAddress1("-");
            appUserModelWalkin.setAddress2("-");
            appUserModelWalkin.setCity("-");
            appUserModelWalkin.setCountry("-");
            appUserModelWalkin.setState("-");
            appUserModelWalkin.setMotherMaidenName("-");
            appUserModelWalkin.setDob(dateOfBirth);
            appUserModelWalkin.setMobileTypeId(1L);
            appUserModelWalkin.setMobileNo(walkInMobileNumber);
            appUserModelWalkin.setNic(walkInCnic);
            appUserModelWalkin.setNicExpiryDate(expiryDate);
            appUserModelWalkin.setCreatedByAppUserModel(createdByAppUserModel);
            appUserModelWalkin.setUpdatedByAppUserModel(createdByAppUserModel);
            appUserModelWalkin.setCreatedOn(nowDate);
            appUserModelWalkin.setUpdatedOn(nowDate);
            appUserModelWalkin.setAppUserTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);
            appUserModelWalkin.setVerified(Boolean.TRUE);
            appUserModelWalkin.setAccountEnabled(Boolean.TRUE);
            appUserModelWalkin.setAccountExpired(Boolean.FALSE);
            appUserModelWalkin.setAccountLocked(Boolean.FALSE);
            appUserModelWalkin.setCredentialsExpired(Boolean.FALSE);
            appUserModelWalkin.setPasswordChangeRequired(Boolean.FALSE);
            appUserModelWalkin.setWalkinCustomerModel(walkinCustomerModel);
            appUserModelWalkin.setUsername(username);
            appUserModelWalkin.setPassword(password);

            logger.info("creating appUserModelWalkin (cnic " + walkInCnic + ")");

            appUserModelWalkin = this.appUserDAO.saveOrUpdate(appUserModelWalkin);
            baseWrapper.putObject("appUserModel", appUserModelWalkin);

            bankModel = getOlaBankMadal();

            baseWrapper.setBasePersistableModel(bankModel);

            logger.info("loading loadFinancialInstitution (...)");
            abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

            smartMoneyAccountModel = new SmartMoneyAccountModel();
            smartMoneyAccountModel.setWalkinCustomerModel(walkinCustomerModel);
            smartMoneyAccountModel.setBankId(getOlaBankMadal().getBankId());
            smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            smartMoneyAccountModel.setBankId(bankModel.getBankId());
            smartMoneyAccountModel.setCreatedOn(nowDate);
            smartMoneyAccountModel.setUpdatedOn(nowDate);
            smartMoneyAccountModel.setCreatedByAppUserModel(createdByAppUserModel);
            smartMoneyAccountModel.setUpdatedByAppUserModel(createdByAppUserModel);
            smartMoneyAccountModel.setActive(Boolean.TRUE);
            smartMoneyAccountModel.setDefAccount(Boolean.TRUE);
            smartMoneyAccountModel.setDeleted(Boolean.FALSE);
            smartMoneyAccountModel.setName("i8_wc_" + username);

            if (abstractFinancialInstitution.isVeriflyRequired()) {

                if (abstractFinancialInstitution.isVeriflyLite()) {

                    smartMoneyAccountModel.setChangePinRequired(Boolean.FALSE);
                } else {
                    smartMoneyAccountModel.setChangePinRequired(Boolean.TRUE);
                }

            } else {
                smartMoneyAccountModel.setChangePinRequired(Boolean.FALSE);
            }

            logger.info("[MfsAccountManager.createWalkinCustomerAccount] creating smartMoneyAccountModel for Walkin Customer ID:" + walkinCustomerModel.getWalkinCustomerId() + ". Walkin customer CNIC: " + walkInCnic + ". Walkin Custoemr Mobile No:" + walkInMobileNumber + ". Created By App User ID:" + createdByAppUserModel.getAppUserId());

            smartMoneyAccountModel = this.linkPaymentModeDAO.saveOrUpdate(smartMoneyAccountModel);

            baseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            // step 4 ------ OLA Account Creation
            baseWrapper.putObject("bankModel", bankModel);

            logger.info("creating createWalkinCustomerOLAAccount (appUserModelWalkin " + appUserModelWalkin.getNic() + ")");

            olaVo = createWalkinCustomerOLAAccount(appUserModelWalkin, bankModel);

        } catch (ImplementationNotSupportedException | SQLException | JDBCException sqlExcetion) {

            logger.error(ExceptionProcessorUtility.getStackTrace(sqlExcetion));
            baseWrapper.putObject("ErrMessage", sqlExcetion.getMessage());
            throw new FrameworkCheckedException(sqlExcetion.getMessage());

        } catch (Exception exception) {

            logger.error(ExceptionProcessorUtility.getStackTrace(exception));
            baseWrapper.putObject("ErrMessage", exception.getMessage());

            if (exception.getCause() instanceof ConstraintViolationException) {

                ConstraintViolationException constrainViolationException = (ConstraintViolationException) exception.getCause();

                if (constrainViolationException.getConstraintName().indexOf("UK_SM_ACCOUNT") != -1) {
                    throw new FrameworkCheckedException("Account nick already exists");
                }
            }

            // This is a tuch to handle the smartmoney account created in case of verifly exception. this is because the transaction is not getting rolled back
            if (smartMoneyAccountModel != null && smartMoneyAccountModel.getSmartMoneyAccountId() != null) {

                try {

                    this.linkPaymentModeDAO.deleteByPrimaryKey(smartMoneyAccountModel.getSmartMoneyAccountId());

                } catch (Exception e) {

                    logger.error(ExceptionProcessorUtility.getStackTrace(e));
                    throw new FrameworkCheckedException(e.getMessage());
                }
            }

            throw new FrameworkCheckedException(exception.getMessage(), exception);
        }

        return baseWrapper;
    }

    private OLAVO createWalkinCustomerOLAAccount(AppUserModel appUserModelWalkin, BankModel bankModel) throws FrameworkCheckedException {

        Date dateOfBirth = PortalDateUtils.subtractYears(new Date(), 18);

        OLAVO olaVo = new OLAVO();

        olaVo.setFirstName(appUserModelWalkin.getFirstName());
        olaVo.setMiddleName(" ");
        olaVo.setLastName(appUserModelWalkin.getLastName());
        olaVo.setFatherName(appUserModelWalkin.getLastName());
        olaVo.setCnic(CommonUtils.maskWalkinCustomerCNIC(appUserModelWalkin.getNic()));
        olaVo.setAddress(appUserModelWalkin.getAddress1());
        olaVo.setLandlineNumber(appUserModelWalkin.getMobileNo());
        olaVo.setMobileNumber(appUserModelWalkin.getMobileNo());
        olaVo.setDob(dateOfBirth);
        olaVo.setStatusId(Long.valueOf(1l));
        olaVo.setCustomerAccountTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);

        SwitchWrapper sWrapper = new SwitchWrapperImpl();
        sWrapper.setOlavo(olaVo);

        try {

            logger.info("[MfsAccountManager.createWalkinCustomerAccount] creating OLA Account for Walkin CNIC:" + olaVo.getCnic() + ". Loggedin AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());

            if (bankModel == null) {
                bankModel = getOlaBankMadal();
            }

            sWrapper.setBankId(bankModel.getBankId());

            sWrapper = olaVeriflyFinancialInstitution.createAccount(sWrapper);

        } catch (Exception e) {

            logger.error(ExceptionProcessorUtility.getStackTrace(e));
            logger.error("[MfsAccountManager.createWalkinCustomerOLAAccount] Exception occured for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + e.getMessage());

            throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
        }

        if ("07".equals(olaVo.getResponseCode())) {
            logger.error("[MfsAccountManager.createWalkinCustomerOLAAccount] NIC " + olaVo.getCnic() + " already exisits in the OLA accounts.Throwing Exception for Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". OLA Response Code:" + olaVo.getResponseCode());

            throw new FrameworkCheckedException("NIC already exisits in the OLA accounts");
        }

        olaVo = sWrapper.getOlavo();

        logger.info("[MfsAccountManager.createWalkinCustomerOLAAccount] Done creating OLA Account for Walkin Customer CNIC: " + olaVo.getCnic() + ". AccountNo:" + olaVo.getPayingAccNo().toString());

        return olaVo;
    }

    public BaseWrapper createHandlerAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        HandlerBackingBean handlerBackingBean = (HandlerBackingBean) baseWrapper.getObject(HandlerBackingBean.BEAN_NAME);

        HandlerModel handlerModel = (HandlerModel) handlerBackingBean.getHandlerModel();
        baseWrapper.setBasePersistableModel(handlerModel);

        Boolean isEditable = (handlerModel.getHandlerId() == null || handlerModel.getHandlerId() == 0) ? Boolean.FALSE : Boolean.TRUE;

        try {

            if (!isEditable) {

                handlerModel.setPrimaryKey(null);
            }

            handlerManager.createOrUpdateHandler(baseWrapper);
            handlerModel = (HandlerModel) baseWrapper.getBasePersistableModel();

            AppUserModel appUserModel = handlerBackingBean.getAppUserModel();
            appUserModel.setHandlerIdHandlerModel(handlerModel);
            appUserModel.setRegistrationStateId(RegistrationStateConstantsInterface.VERIFIED);
            appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
            this.appUserDAO.saveOrUpdate(appUserModel);

            if (!isEditable) {
                /**
                 * Saving appUserMobileHistoryModel
                 * */

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

                UserDeviceAccountsModel userDeviceAccountsModel = handlerBackingBean.getUserDeviceAccountsModel();
                userDeviceAccountsModel.setAppUserIdAppUserModel(appUserModel);
                userDeviceAccountsModel.setPrimaryKey(null);
                this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);

                SmartMoneyAccountModel smartMoneyAccountModel = handlerBackingBean.getSmartMoneyAccountModel();
                smartMoneyAccountModel.setHandlerId(handlerModel.getHandlerId());
                smartMoneyAccountModel.setPrimaryKey(null);
                this.smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);


                //Setting Partner Group in case of sub agent
                PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
                partnerGroupModel.setPrimaryKey(1000L); //HANDLER_PARTNER_GROUP
                BaseWrapper bWrapper = new BaseWrapperImpl();
                bWrapper.setBasePersistableModel(partnerGroupModel);
                bWrapper = partnerGroupFacade.loadPartnerGroup(bWrapper);
                partnerGroupModel = (PartnerGroupModel) bWrapper.getBasePersistableModel();

                if (partnerGroupModel != null) {
                    AppUserPartnerGroupModel appUserPartnerGroupModel = new AppUserPartnerGroupModel();
                    appUserPartnerGroupModel.setPartnerGroupId(partnerGroupModel.getPartnerGroupId());
                    appUserPartnerGroupModel.setUpdatedOn(new Date());
                    appUserPartnerGroupModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                    appUserPartnerGroupModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                    appUserPartnerGroupModel.setCreatedOn(new Date());
                    appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
                    appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
                }

                AccountInfoModel accountInfoModel = handlerBackingBean.getAccountInfoModel();
                accountInfoModel.setCustomerId(appUserModel.getAppUserId());
                accountInfoModel.setPrimaryKey(null);

                VeriflyBaseWrapper veriflyBaseWrapper = this.generateHandlerAccountInfoPin(accountInfoModel, smartMoneyAccountModel);

                handlerBackingBean.setPlainTransactionPin(veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin());

                Object[] args1 = {MessageUtil.getMessage("agentweb.url"), appUserModel.getUsername(), handlerBackingBean.getPassword()};
                Object[] args2 = {MessageUtil.getMessage("agentmate.download.url"), userDeviceAccountsModel.getUserId(), handlerBackingBean.getPlainLoginPin()};

                String messageString1 = MessageUtil.getMessage("handlerAccountCreated.Confirmation.SMS.part1", args1);
                String messageString2 = MessageUtil.getMessage("handlerAccountCreated.Confirmation.SMS.part2", args2);

                SmsMessage smsMessage1 = new SmsMessage(appUserModel.getMobileNo(), messageString1);
                SmsMessage smsMessage2 = new SmsMessage(appUserModel.getMobileNo(), messageString2);

                smsSender.send(smsMessage1);
                smsSender.send(smsMessage2);

                IvrRequestDTO ivrDTO = new IvrRequestDTO();
                ivrDTO.setHandlerMobileNo(appUserModel.getMobileNo());
                ivrDTO.setCustomerMobileNo(appUserModel.getMobileNo());
                ivrDTO.setRetryCount(0);
                ivrDTO.setProductId(new Long(CommandFieldConstants.CREATE_PIN_IVR));
                try {
                    ivrRequestHandler.makeIvrRequest(ivrDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new FrameworkCheckedException(e.getLocalizedMessage());
                }

            } else {
                UserDeviceAccountsModel userDeviceAccountsModel = userDeviceAccountsDAO.findUserDeviceAccountByAppUserId(appUserModel.getAppUserId());
                userDeviceAccountsModel.setAppUserIdAppUserModel(appUserModel);
                userDeviceAccountsModel.setProdCatalogId(handlerBackingBean.getProductCatalogId());
                this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
            }

        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(e.getLocalizedMessage());
        }

        return baseWrapper;
    }

    private VeriflyBaseWrapper generateHandlerAccountInfoPin(AccountInfoModel accountInfoModel, SmartMoneyAccountModel smartMoneyAccountModel) {

        LogModel logmodel = new LogModel();
        logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logmodel);
        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB);

        try {

            BankModel bankModel = new BankModel();
            bankModel.setFinancialIntegrationId(FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION); // 4 is for OLA bank
            CustomList<BankModel> bankList = this.bankDAO.findByExample(bankModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
            List<BankModel> bankL = bankList.getResultsetList();
            bankModel = (BankModel) bankL.get(0);

            ThreadLocalActionLog.setActionLogId(PortalConstants.ACTION_CREATE);

            BaseWrapper baseWrapperBank = new BaseWrapperImpl();
            baseWrapperBank.setBasePersistableModel(bankModel);
            AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
            veriflyBaseWrapper = abstractFinancialInstitution.generatePin(veriflyBaseWrapper);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return veriflyBaseWrapper;
    }

    public void setWalkinCustomerDAO(WalkinCustomerDAO walkinCustomerDAO) {
        this.walkinCustomerDAO = walkinCustomerDAO;
    }

    public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
        this.smartMoneyAccountDAO = smartMoneyAccountDAO;
    }

    public String getAreaByAppUserId(Long appUserId) {
        String area = "";
        try {
            area = userInfoListViewDAO.getAreaByAppUserId(appUserId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return area;
    }

    public BaseWrapper updateAppUserModel(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setActionId((Long) baseWrapper.getObject("actionId"));
        actionLogModel.setUsecaseId((Long) baseWrapper.getObject("usecaseId"));
        actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
        actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
        actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
        actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
        actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
        //actionLogModel.setCustomField2("Updating the User Information");
        actionLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());


        AppUserModel appUserModel;
        appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();

        appUserModel = this.appUserDAO.closeCustomerAccount(appUserModel);
        baseWrapper.setBasePersistableModel(appUserModel);

        actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
        actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
        actionLogModel.setCustomField1(String.valueOf(appUserModel.getAppUserId()));
        actionLogModel.setCustomField11(appUserModel.getUsername());

        return baseWrapper;
    }

    public BaseWrapper closeHandlerAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setActionId((Long) baseWrapper.getObject("actionId"));
        actionLogModel.setUsecaseId((Long) baseWrapper.getObject("usecaseId"));
        actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_START);
        actionLogModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
        actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
        actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
        actionLogModel.setDeviceTypeId(PortalConstants.WEB_TYPE_DEVICE);
        //actionLogModel.setCustomField2("Updating the User Information");
        actionLogModel = logAction(actionLogModel);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());


        AppUserModel appUserModel;
        appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();

        appUserModel = this.appUserDAO.closeHandlerAccount(appUserModel);
        baseWrapper.setBasePersistableModel(appUserModel);

        actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
        actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
        actionLogModel.setCustomField1(String.valueOf(appUserModel.getAppUserId()));
        String referenceId = "";
        if (baseWrapper.getObject("retailerId") != null) {
            referenceId = baseWrapper.getObject("retailerId").toString();
        }
        actionLogModel.setCustomField11(referenceId);
        actionLogModel = logAction(actionLogModel);

        return baseWrapper;
    }

    public double getMfsAccountBalance(Long appUserId, Long paymentModeId) throws Exception {
        //Commented by Sheheryaar
        //AppUserModel appUserModel = new AppUserModel();
        AppUserModel appUserModel = this.getAppUserModelByPrimaryKey(appUserId);
        ThreadLocalAppUser.setAppUserModel(appUserModel);
        try {
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            //Commented and added by Sheheryaar
            //SmartMoneyAccountModel smartMoneyAccountModel =this.getSmartMoneyAccountByCustomerId(appUserModel.getCustomerId());
            SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
            sma.setCustomerId(appUserModel.getCustomerId());
            sma.setPaymentModeId(paymentModeId);

            SmartMoneyAccountModel smartMoneyAccountModel = this.getSmartMoneyAccountByExample(sma);
            //End
            baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
            AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
            switchWrapper.setBasePersistableModel(smartMoneyAccountModel);
            switchWrapper.setWorkFlowWrapper(new WorkFlowWrapperImpl());

            ActionLogModel actionLogModel = new ActionLogModel();
            XStream xstream = new XStream();

            xstream.toXML("");
            actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALLPAY_WEB);
            actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(""),
                    XPathConstants.actionLogInputXMLLocationSteps));
            actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
            actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));

            baseWrapper.setBasePersistableModel(actionLogModel);
            baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
            actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
            if (actionLogModel.getActionLogId() != null) {
                ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
            }
            switchWrapper = abstractFinancialInstitution.checkBalanceWithoutPin(switchWrapper);
            Double balance = switchWrapper.getBalance();
            return balance;
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalActionLog.remove();
        }
    }

    @Override
    public SearchBaseWrapper searchCustomerPictures(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        CustomerPictureModel customerPictureModel = (CustomerPictureModel) wrapper.getBasePersistableModel();
        CustomList<CustomerPictureModel> customList = customerPictureDAO.findByExample(customerPictureModel);
        wrapper.setCustomList(customList);
        return wrapper;
    }

    /***************************************************************************************************************************
     * Added by Soofia
     */

    @Override
    public CustomerPictureModel getCustomerPictureByTypeId(Long pictureTypeId, Long customerId) throws FrameworkCheckedException {
        CustomerPictureModel customerPictureModel = customerPictureDAO.getCustomerPictureByTypeId(pictureTypeId, customerId);
        return customerPictureModel;
    }

    @Override
    public CustomerPictureModel getCustomerPictureByTypeIdAndStatus(Long pictureTypeId, Long customerId) throws FrameworkCheckedException {
        CustomerPictureModel customerPictureModel = customerPictureDAO.getCustomerPictureByTypeIdAndStatus(pictureTypeId, customerId);
        return customerPictureModel;
    }

    @Override
    public BlinkCustomerPictureModel getBlinkCustomerPictureByTypeId(Long pictureTypeId, Long customerId) throws FrameworkCheckedException {
        BlinkCustomerPictureModel customerPictureModel = blinkCustomerPictureDAO.getBlinkCustomerPictureByTypeId(pictureTypeId, customerId);
        return customerPictureModel;
    }

    @Override
    public List<CustomerPictureModel> getAllCustomerPictures(Long customerId)
            throws FrameworkCheckedException {
        List<CustomerPictureModel> customerPictureList = customerPictureDAO.getAllCustomerPictures(customerId);
        return customerPictureList;
    }

    private List<CustomerPictureModel> searchAllCustomerPictures(Long customerId, Long applicantTypeId, Long applicantDetailId) throws FrameworkCheckedException {
        List<CustomerPictureModel> customerPictureModelList = null;
        CustomerPictureModel customerPictureModel = new CustomerPictureModel();
        customerPictureModel.setCustomerId(customerId);
        customerPictureModel.setApplicantTypeId(applicantTypeId);
        customerPictureModel.setApplicantDetailId(applicantDetailId);
        CustomList<CustomerPictureModel> customList = customerPictureDAO.findByExample(customerPictureModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
        if (customList != null) {
            customerPictureModelList = customList.getResultsetList();
        }
        return customerPictureModelList;
    }

    /***************************************************************************************************************************/

    public void setCustomerPictureDAO(CustomerPictureDAO customerPictureDAO) {
        this.customerPictureDAO = customerPictureDAO;
    }

    public void setJmsProducer(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

    public void setXmlMarshaller(XmlMarshaller<BulkCustomerAccountVo> xmlMarshaller) {
        this.xmlMarshaller = xmlMarshaller;
    }

    public VeriflyBaseWrapper changePIN(VeriflyBaseWrapper wrapper) throws Exception {

        if (logger.isDebugEnabled())
            logger.debug("<--Start changePin()---->");

        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();

        XStream xstream = new XStream(new PureJavaReflectionProvider());
        Encryption encryption = (Encryption) keysObj.getEncrptionClassObject();

        /*********************************************************************************
         * Updated by Soofia Faruq AES Encryption Support
         */
        KeyPair keypair = null;
        SecretKey aesKey = null;
        if (encryption instanceof AESEncryption) {
            String strKey = keysObj.getValue(ConfigurationConstants.AES_KEY);
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] encodedKey = decoder.decodeBuffer(strKey);
            aesKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        } else {
            String keyPairValue = keysObj
                    .getValue(ConfigurationConstants.KEY_PAIR);
            keypair = (KeyPair) xstream.fromXML(keyPairValue);
        }
        /**********************************************************************************/


        try {
            if (logger.isDebugEnabled())
                logger.debug("<--Going to validate financial info---->");
            if (!variflyValidator.isValidDataForChangePin(wrapper)) {
                logger.error("<--Insufficient Finnancial data---->");
                throw new InvalidDataException(String.valueOf(FailureReasonConstants.INSUFFICIENT_DATA));
            }

            if (!wrapper.getAccountInfoModel().getNewPin().equals(wrapper.getAccountInfoModel().getConfirmNewPin())) {
                logger.error("<--New and Confirm new pins dont match---->");
                throw new InvalidDataException(String.valueOf(FailureReasonConstants.NEW_AND_CONFIRM_NEW_PINS_ARE_NOT_MATCHED));
            }

            String decryptedPin = null;
            /*********************************************************************************
             * Updated by Soofia Faruq
             */
            if (keypair != null) {
                decryptedPin = encryption.decrypt(keypair.getPrivate(), wrapper
                        .getAccountInfoModel().getNewPin());
            } else {
                decryptedPin = encryption.decrypt(aesKey, wrapper
                        .getAccountInfoModel().getNewPin());
            }
            /**********************************************************************************/

            int minPinLength = 4;
            int maxPinLength = 4;


            if (decryptedPin.length() < minPinLength) {
                logger.error("<--Pin length is shorter than minimum---->");
                throw new InvalidDataException(String.valueOf(FailureReasonConstants.PIN_LENGTH_IS_SHORTER));
            }
            if (decryptedPin.length() > maxPinLength) {
                logger.error("<--Pin length is greated than maximum---->");
                throw new InvalidDataException(String.valueOf(FailureReasonConstants.PIN_LENGTH_IS_GREATER));
            }

            wrapper.setErrorStatus(false);
            userDeviceAccountsModel = validatePIN(wrapper, encryption, keypair);

            if (userDeviceAccountsModel == null) {
                logger.error("<--Incorrect Pin given-->");
                throw new InvalidDataException(String.valueOf(FailureReasonConstants.INVALID_BANK_PIN_PLEASE_ENTER_VALID_BANK_PIN));
            }

//        userDeviceAccountsModel.setPin(encodedPin);
            userDeviceAccountsModel.setUpdatedOn(new Date());
            if (logger.isDebugEnabled())
                logger.debug("<--Going to update records---->");
            userDeviceAccountsModel.setPin(wrapper.getAccountInfoModel().getNewPin());
            userDeviceAccountsModel.setPinChangeRequired(Boolean.FALSE);
            userDeviceAccountsModel = this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);

            wrapper.setErrorStatus(true);

            if (logger.isDebugEnabled())
                logger.debug("<--Pin changed successfully---->");
        } catch (InvalidDataException exp) {
            Long errorCode = Long.parseLong(exp.getMessage());
            //logger.error(StringUtils.prepareExceptionStackTrace(exp));

            wrapper = new VeriflyBaseWrapperImpl();
            wrapper.setErrorStatus(false);
            String errorMessage = null;
            if (errorCode == 18) {
                errorMessage = "Incorrect Old Login PIN.";
            } else {
                errorMessage = this.getErrorMessages(new Long(errorCode).toString());
            }
            wrapper.setErrorMessage(errorMessage);

        }
        if (logger.isDebugEnabled())
            logger.debug("<--End pinChange ()---->");
        return wrapper;
    }

    /**
     * This method validates if the pin recieved is correct pin or not.
     * The recieved pin is first deincrypted using the private key and then
     * hashed using the SHA algo and finally it is matched with the pin stored in DB.
     *
     * @param wrapper VeriflyBaseWrapper
     * @return AccountInfoModel
     */
    protected UserDeviceAccountsModel validatePIN(VeriflyBaseWrapper wrapper, Encryption encryption, KeyPair keypair) throws Exception {
        //log activities
        if (logger.isDebugEnabled())
            logger.debug("validatePIN start");

        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setEnableLike(Boolean.FALSE);

        //log activities
        if (logger.isDebugEnabled())
            logger.debug("validating pin");

        String encryptedPin = wrapper.getAccountInfoModel().getOldPin();

        userDeviceAccountsModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
        userDeviceAccountsModel.setDeviceTypeId(5L);

        CustomList<UserDeviceAccountsModel> list = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel, null, null, exampleHolder);
        List resultSetList = list.getResultsetList();

        if (resultSetList.size() > 0) {

            //log activities
            if (logger.isDebugEnabled())
                logger.debug("record exist in db for verification");

            UserDeviceAccountsModel localUserDeviceAccountsModel = new UserDeviceAccountsModel();
            localUserDeviceAccountsModel = (UserDeviceAccountsModel) resultSetList.get(0);
            String pinFromDB = localUserDeviceAccountsModel.getPin();

            if (logger.isDebugEnabled()) {
                logger.debug("DB PIN " + pinFromDB + " \n\nEncryptedPIN " + encryptedPin);
            }

            if ((pinFromDB != null) && (pinFromDB.equals(encryptedPin))) {
                //log activities
                if (logger.isDebugEnabled())
                    logger.debug("both pins match correctly");

                userDeviceAccountsModel = localUserDeviceAccountsModel;
            } else {
                if (logger.isDebugEnabled())
                    logger.debug("pins are not matched");
                userDeviceAccountsModel = null;

            }
        } else {
            //log activities
            if (logger.isDebugEnabled())
                logger.debug("pins are not matched");

            userDeviceAccountsModel = null;
        }
        //log activities
        if (logger.isDebugEnabled())
            logger.debug("validatePIN end ");

        return userDeviceAccountsModel;
    }

    /**
     * This methid is used for getting the encoded class object depending on the
     * information provided in DB.
     *
     * @return Object
     */
    protected Object getEncoderClassObject() {
        Object object = null;
        String encoderClassName = this.getConfiguration(new Long(
                ConfigurationConstants.ENCODER_CLASS).toString());
        if (encoderClassName == null)
            encoderClassName = "com.inov8.verifly.common.encoder.NullEncoder";

        try {
            Class classDefinition = Class.forName(encoderClassName);
            object = classDefinition.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;

    }

    /**
     * This methid is used for getting the encrypted class object depending on the
     * information provided in DB.
     *
     * @return Object
     */
    private Object getEncrptionClassObject() {
        Object object = null;
        String encrptorClassName = this.getConfiguration(new Long(
                ConfigurationConstants.ENCRYPTOR_CLASS).toString());
        if (encrptorClassName == null)
            encrptorClassName =
                    "com.inov8.verifly.common.encoder.NullEncryption";

        try {
            Class classDefinition = Class.forName(encrptorClassName);
            object = classDefinition.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * This method is used for populating a Hash Map with all the Configuration
     * values present in data base.
     */
    private void prepareConfiguration() {
        CustomList<VeriflyConfigurationModel>
                customlist = this.veriflyConfigurationDao.findAll();
        List<VeriflyConfigurationModel> list = customlist.getResultsetList();
        for (VeriflyConfigurationModel veriflyConfigurationModel : list) {
            configurationHashMap.put(veriflyConfigurationModel.getPrimaryKey(),
                    veriflyConfigurationModel.getValue());
        }
    }

    /**
     * This method is used for returning the required configuration from the populated
     * hash map depending upon the key passed
     *
     * @param key The key of the required configuration.
     * @return String
     */
    private String getConfiguration(String key) {
        this.prepareConfiguration();
        return configurationHashMap.get(Long.valueOf(key)).toString();
    }

    /**
     * This method is used for populating a Hash Map with all the Error Messages
     * present in data base.
     */
    private void prepareErrorMessages() {
        CustomList<VfFailureReasonModel>
                customlist = this.failureReasonDao.findAll();
        List<VfFailureReasonModel> list = customlist.getResultsetList();
        for (VfFailureReasonModel failureReasonModel : list) {
            failureReasonHashMap.put(failureReasonModel.getPrimaryKey(),
                    failureReasonModel.getName());
        }
    }

    /**
     * This method is used for returning the required failure reason from the populated
     * hash map depending upon the key passed
     *
     * @param key The key of the required configuration.
     * @return Required failure reason.
     */
    protected String getErrorMessages(String key) {
        if (this.failureReasonHashMap == null ||
                this.failureReasonHashMap.size() == 0) {
            this.prepareErrorMessages();
        }
        return failureReasonHashMap.get(Long.valueOf(key)).toString();
    }

    /**
     * Sends SMS to mobileNo if newRegistrationStateId is different than oldRegistrationStateId.
     *
     * @param oldRegistrationStateId
     * @param newRegistrationStateId
     * @param mobileNo
     * @throws FrameworkCheckedException
     */
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

    /* (non-Javadoc)
     * @see com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager#createKYC(com.inov8.framework.common.wrapper.BaseWrapper)
     */
    @Override
    public BaseWrapper createKYC(BaseWrapper baseWrapper)
            throws FrameworkCheckedException {
        String actionLogHandler = (String) baseWrapper.getObject(CommandFieldConstants.KEY_ACTION_LOG_HANDLER);

        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        KYCModel kycModel = (KYCModel) baseWrapper.getObject(KYCModel.KYC_MODEL_KEY);

        if (kycModel.getAcType().longValue() != CustomerAccountTypeConstants.LEVEL_3 && !isInitialAppFormNoUnique(kycModel.getInitialAppFormNo(), kycModel.getAcType())) {
            baseWrapper.putObject(PortalConstants.KEY_KYC_IAFN, kycModel.getInitialAppFormNo());
            throw new FrameworkCheckedException("applicationNumberNotUniqueException");
        }

        //save KYC model
        kycModel = kycDAO.saveOrUpdate(kycModel);

        //save expected mode of transactions against the KYC Model's applicationFormNo
        if (kycModel.getExpectedModeOfTransaction() != null) {
            List<ApplicantTxModeModel> applicantTxModeModels = new ArrayList<ApplicantTxModeModel>();
            for (Long modeOfTx : kycModel.getExpectedModeOfTransaction()) {
                if (modeOfTx != null) {
                    ApplicantTxModeModel applicantTxModeModel = new ApplicantTxModeModel();
                    applicantTxModeModel.setCreatedBy(kycModel.getCreatedBy());
                    applicantTxModeModel.setCreatedOn(kycModel.getCreatedOn());
                    applicantTxModeModel.setInitialAppFormNo(kycModel.getInitialAppFormNo());
                    applicantTxModeModel.setTxModeId(modeOfTx);
                    applicantTxModeModel.setUpdatedBy(kycModel.getUpdatedBy());
                    applicantTxModeModel.setUpdatedOn(kycModel.getUpdatedOn());
                    applicantTxModeModels.add(applicantTxModeModel);
                }
            }
            if (CollectionUtils.isNotEmpty(applicantTxModeModels)) {
                transactionModeDAO.saveOrUpdateCollection(applicantTxModeModels);
            }
        }

        if (actionLogHandler == null) {
            actionLogModel.setCustomField1(kycModel.getInitialAppFormNo()
                    .toString());
            actionLogModel.setCustomField11(UserUtils.getCurrentUser().getUsername());
            this.actionLogManager.completeActionLog(actionLogModel);
        }

        baseWrapper.putObject(kycModel.KYC_MODEL_KEY, kycModel);
        baseWrapper.putObject(PortalConstants.KEY_KYC_IAFN, kycModel.getInitialAppFormNo());

        return baseWrapper;
    }

    @Override
    public BaseWrapper updateKYC(BaseWrapper baseWrapper)
            throws FrameworkCheckedException {

        String actionLogHandler = (String) baseWrapper.getObject(CommandFieldConstants.KEY_ACTION_LOG_HANDLER);

        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        KYCModel kycModel = (KYCModel) baseWrapper.getObject(KYCModel.KYC_MODEL_KEY);
        kycModel = kycDAO.saveOrUpdate(kycModel);

        //delete existing mode of transactions first
        ApplicantTxModeModel deleteObject = new ApplicantTxModeModel();
        deleteObject.setInitialAppFormNo(kycModel.getInitialAppFormNo());
        SearchBaseWrapper searchWrapper = new SearchBaseWrapperImpl();
        searchWrapper.setBasePersistableModel(deleteObject);
        transactionModeDAO.deleteApplicantTransactionMode(searchWrapper);

        //save expected mode of transactions against the KYC Model's applicationFormNo
        if (kycModel.getExpectedModeOfTransaction() != null) {
            List<ApplicantTxModeModel> applicantTxModeModels = new ArrayList<ApplicantTxModeModel>();
            for (Long modeOfTx : kycModel.getExpectedModeOfTransaction()) {
                if (modeOfTx != null) {
                    ApplicantTxModeModel applicantTxModeModel = new ApplicantTxModeModel();
                    applicantTxModeModel.setCreatedBy(kycModel.getCreatedBy());
                    applicantTxModeModel.setCreatedOn(kycModel.getCreatedOn());
                    applicantTxModeModel.setInitialAppFormNo(kycModel.getInitialAppFormNo());
                    applicantTxModeModel.setTxModeId(modeOfTx);
                    applicantTxModeModel.setUpdatedBy(kycModel.getUpdatedBy());
                    applicantTxModeModel.setUpdatedOn(kycModel.getUpdatedOn());
                    applicantTxModeModels.add(applicantTxModeModel);
                }
            }
            if (CollectionUtils.isNotEmpty(applicantTxModeModels)) {
                transactionModeDAO.saveOrUpdateCollection(applicantTxModeModels);
            }
        }

        if (actionLogHandler == null) {
            actionLogModel.setCustomField1(kycModel.getInitialAppFormNo().toString());
            actionLogModel.setCustomField11(UserUtils.getCurrentUser().getUsername());
            this.actionLogManager.completeActionLog(actionLogModel);
        }

        baseWrapper.putObject(KYCModel.KYC_MODEL_KEY, kycModel);
        baseWrapper.putObject(PortalConstants.KEY_KYC_IAFN, kycModel.getInitialAppFormNo());

        return baseWrapper;
    }

    public AccountInfoDAO getAccountInfoDao() {
        return accountInfoDao;
    }

    public void setAccountInfoDao(AccountInfoDAO accountInfoDao) {
        this.accountInfoDao = accountInfoDao;
    }

    public HashMap getConfigurationHashMap() {
        return configurationHashMap;
    }

    public void setConfigurationHashMap(HashMap configurationHashMap) {
        this.configurationHashMap = configurationHashMap;
    }

    public LogManager getLogManager() {
        return logManager;
    }

    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    public VeriflyConfigurationDAO getVeriflyConfigurationDao() {
        return veriflyConfigurationDao;
    }

    public void setVeriflyConfigurationDao(
            VeriflyConfigurationDAO veriflyConfigurationDao) {
        this.veriflyConfigurationDao = veriflyConfigurationDao;
    }

    public EncryptionHandler getEncryptionHandler() {
        return encryptionHandler;
    }

    public void setEncryptionHandler(EncryptionHandler encryptionHandler) {
        this.encryptionHandler = encryptionHandler;
    }

    public ConfigurationContainer getKeysObj() {
        return keysObj;
    }

    public void setKeysObj(ConfigurationContainer keysObj) {
        this.keysObj = keysObj;
    }

    public VariflyValidator getVariflyValidator() {
        return variflyValidator;
    }

    public void setVariflyValidator(VariflyValidator variflyValidator) {
        this.variflyValidator = variflyValidator;
    }

    public UserDeviceAccountsDAO getUserDeviceAccountsDAO() {
        return userDeviceAccountsDAO;
    }

    public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
        this.userDeviceAccountsDAO = userDeviceAccountsDAO;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public FailureReasonDAO getFailureReasonDao() {
        return failureReasonDao;
    }

    public void setFailureReasonDao(FailureReasonDAO failureReasonDao) {
        this.failureReasonDao = failureReasonDao;
    }

    public void setApplicantDetailDAO(ApplicantDetailDAO applicantDetailDAO) {
        this.applicantDetailDAO = applicantDetailDAO;
    }

    public BusinessDetailDAO getBusinessDetailDAO() {
        return businessDetailDAO;
    }

    public void setBusinessDetailDAO(BusinessDetailDAO businessDetailDAO) {
        this.businessDetailDAO = businessDetailDAO;
    }

    @Override
    public ApplicantDetailModel loadApplicantDetail(ApplicantDetailModel model) throws FrameworkCheckedException {
        ApplicantDetailModel applicantModel = new ApplicantDetailModel();
        List<ApplicantDetailModel> list = applicantDetailDAO.findByExample(model).getResultsetList();
        if (!list.isEmpty()) {
            applicantModel = list.get(0);

            for (IDDocumentTypeEnum enumItem : IDDocumentTypeEnum.values()) {
                if (enumItem.getIndex() == applicantModel.getIdType().intValue()) {
                    applicantModel.setIdTypeName(enumItem.getName());
                    break;
                }
            }
        }
        return applicantModel;
    }

    @Override
    public BusinessDetailModel loadBusinessDetail(BusinessDetailModel model) throws FrameworkCheckedException {
        BusinessDetailModel businessModel = new BusinessDetailModel();
        List<BusinessDetailModel> list = businessDetailDAO.findByExample(model).getResultsetList();
        if (list.size() > 0) {
            businessModel = list.get(0);
        }
        return businessModel;
    }

    @Override
    public List<OlaCustomerAccountTypeModel> loadCustomerACTypes(Long[] typesId) throws FrameworkCheckedException {
        return olaCustomerAccountTypeDao.loadCustomerACTypes(typesId);
    }

    @Override
    public ApplicantDetailModel loadApplicantDetailById(Long applicantDetailId) throws FrameworkCheckedException {
        return applicantDetailDAO.findByPrimaryKey(applicantDetailId);
    }

    private CommandManager getCommandManager() {
        ApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        CommandManager commandManager = (CommandManager) webApplicationContext.getBean("cmdManager");
        return commandManager;
    }

    @Override
    public List<ApplicantDetailModel> loadApplicantDetails(ApplicantDetailModel model) throws FrameworkCheckedException {
        List<ApplicantDetailModel> list = applicantDetailDAO.findByExample(model).getResultsetList();
        for (ApplicantDetailModel model1 : list) {
            Long idType = model1.getIdType();
            for (IDDocumentTypeEnum value : IDDocumentTypeEnum.values()) {
                if (null != idType && value.getIndex() == idType.longValue()) {
                    model1.setIdTypeName(value.getName());
                }
            }
        }

        return list;
    }

    private boolean isContainCustomerAddress(Collection<CustomerAddressesModel> customerAddresses, Long applicantDetailId) {
        boolean isExists = false;
        for (CustomerAddressesModel customerAddressModel : customerAddresses) {
            if (customerAddressModel.getApplicantDetailId() != null) {
                if (customerAddressModel.getApplicantDetailId().equals(applicantDetailId)) {
                    isExists = true;
                    break;
                }
            }
        }
        return isExists;
    }

    public void setHandlerManager(HandlerManager handlerManager) {
        this.handlerManager = handlerManager;
    }

    public void setDeviceApplicationNoGenerator(OracleSequenceGeneratorJdbcDAO deviceApplicationNoGenerator) {
        this.deviceApplicationNoGenerator = deviceApplicationNoGenerator;
    }

    @Override
    public void validateUniqueness(MfsAccountModel mfsAccountModel) throws FrameworkCheckedException {

        String errorMsg = "";

        /**
         * Checking if mobile Number is unique
         */
        if (!this.isMobileNumUnique(mfsAccountModel.getMobileNo(), mfsAccountModel.getAppUserId())) {
            errorMsg = errorMsg + MessageUtil.getMessage("newMfsAccount.mobileNumNotUnique") + "<br>";
        }
        /**
         * Checking if CNIC is unique
         */
        if (null != mfsAccountModel.getNic() && !this.isCnicUnique(mfsAccountModel.getNic(), mfsAccountModel.getAppUserId())) {
            errorMsg = errorMsg + MessageUtil.getMessage("newMfsAccount.nicNotUnique") + "<br>";
        }

        if (null != errorMsg && !errorMsg.isEmpty()) {
            throw new FrameworkCheckedException(errorMsg);
        }
    }

    /*
	private boolean isMobileNumUnique(String mobileNo,Long appUserId)
	{
		AppUserModel appUserModel = new AppUserModel();
		appUserModel.setMobileNo(mobileNo);
		appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		appUserModel.setAppUserId(appUserId);

		return this.appUserDAO.isMobileNumUnique(appUserModel);
	}*/
    private boolean isCnicUnique(String cnic, Long appUserId) {
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setAppUserId(appUserId);
        appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        appUserModel.setNic(cnic);
        return this.appUserDAO.isCnicUnique(appUserModel);
    }

    public void setAppUserMobileHistoryDAO(
            AppUserMobileHistoryDAO appUserMobileHistoryDAO) {
        this.appUserMobileHistoryDAO = appUserMobileHistoryDAO;
    }


    @Override
    public void initiateUserGeneratedPinIvrCall(IvrRequestDTO ivrDTO) throws FrameworkCheckedException {
        ivrDTO.setRetryCount(0);
        ivrDTO.setProductId(new Long(CommandFieldConstants.CMD_CUSTOMER_CREATE_PIN));
        try {
            ivrRequestHandler.makeIvrRequest(ivrDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(e.getLocalizedMessage());
        }

    }

    public String settleAccountOpeningCommission(Long customerId) throws FrameworkCheckedException {
        String transactionID = "";
        logger.info("[MfsAccountManagerImpl.settleAccountOpeningCommission] Going to settle pending commission of Acc Opening... customerId: " + customerId);

        try {
            ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());

            TransactionDetailModel transactionDetailModel = transactionDetailDAO.loadAccOpeningTransactionDetailModel(customerId);
            if (transactionDetailModel != null) {

                CommissionAmountsHolder commissionAmountsHolder = commissionManager.loadCommissionDetails(transactionDetailModel.getTransactionDetailId());

                if (commissionAmountsHolder != null && commissionAmountsHolder.getTotalCommissionAmount() > 0) {
                    WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
                    workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);


                    ProductModel productModel = new ProductModel();
                    productModel.setProductId(transactionDetailModel.getProductId());
                    workFlowWrapper.setProductModel(productModel);

                    // loading trx detail master for 3rd party inclusive charges handling
                    BaseWrapper bWrapper = new BaseWrapperImpl();
                    TransactionDetailMasterModel txMastermodel = new TransactionDetailMasterModel();
                    txMastermodel.setTransactionDetailId(transactionDetailModel.getTransactionDetailId());
                    bWrapper.setBasePersistableModel(txMastermodel);
                    bWrapper = transactionDetailMasterManager.loadTransactionDetailMasterModel(bWrapper);
                    txMastermodel = (TransactionDetailMasterModel) bWrapper.getBasePersistableModel();

                    if (txMastermodel != null) {
                        productModel.setInclChargesCheck(txMastermodel.getThirdPartyCheck());
                        productModel.setName(txMastermodel.getProductName());
                        commissionAmountsHolder.setTotalInclusiveAmount(txMastermodel.getInclusiveCharges());
                    } else {
                        logger.error("Unable to settle Account Opening Commission - Reason: Unable to load TransactionDetailModel for transactionDetailId:" + transactionDetailModel.getTransactionDetailId());
                        throw new FrameworkCheckedException("Unable to settle Account Opening Commission");
                    }

                    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                    TransactionModel transactionModel = new TransactionModel();
                    transactionModel.setTransactionId(transactionDetailModel.getTransactionId());
                    searchBaseWrapper.setBasePersistableModel(transactionModel);
                    searchBaseWrapper = this.transactionManager.loadTransaction(searchBaseWrapper);

                    if (searchBaseWrapper.getBasePersistableModel() != null) {
                        transactionModel = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
                        workFlowWrapper.setTransactionModel(transactionModel);
                        workFlowWrapper.setTransactionCodeModel(transactionModel.getTransactionCodeIdTransactionCodeModel());
                        workFlowWrapper.setTransactionDetailModel(transactionDetailModel);
                    } else {
                        logger.error("Unable to settle Account Opening Commission - Reason: Unable to load TransactionModel for transactionId:" + transactionDetailModel.getTransactionId());
                        throw new FrameworkCheckedException("Unable to settle Account Opening Commission");
                    }

                    SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                    switchWrapper.setWorkFlowWrapper(workFlowWrapper);

                    switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
                    switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                    switchWrapper = olaVeriflyFinancialInstitution.settleAccountOpeningCommission(switchWrapper);

                    settlementManager.updateCommissionTransactionSettled(transactionDetailModel.getTransactionDetailId());

                    workFlowWrapper.setOLASwitchWrapper(switchWrapper);

                    settlementManager.prepareDataForDayEndSettlement(workFlowWrapper);

                    transactionID = workFlowWrapper.getTransactionCodeModel().getCode();

                } else {
                    logger.info("Acc Opening Commission for settlement not found for customerId: " + customerId);
                }

            } else {
                logger.info("No transaction found for Acc Opening Commission for customerId: " + customerId);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new FrameworkCheckedException("AccountOpeningCommissionException");
        }

        return transactionID;
    }

    @Override
    public void saveBulkAgentData(List<BulkAgentDataHolderModel> list) throws FrameworkCheckedException {
        bulkAgentCreationDAO.saveOrUpdateCollection(list);
    }

    @Override
    public List<KYCModel> findKycModel(SearchBaseWrapper baseWrapper) throws FrameworkCheckedException {
        KYCModel queryObject = (KYCModel) baseWrapper.getBasePersistableModel();
        List<KYCModel> list = new ArrayList<KYCModel>();

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setIgnoreCase(true);
        exampleConfigHolderModel.setEnableLike(false);

        CustomList<KYCModel> customList = kycDAO.findByExample(queryObject, null, null, exampleConfigHolderModel);

        if (customList != null) {
            list.addAll(customList.getResultsetList());
        }

        return list;

    }

    @Override
    public KYCModel findKycByPrimaryKey(Long kycId) throws FrameworkCheckedException {
        KYCModel queryObject = kycDAO.findByPrimaryKey(kycId);
        return queryObject;

    }

    @Override
    public SearchBaseWrapper searchKYC(SearchBaseWrapper baseWrapper) throws FrameworkCheckedException {
        KYCModel queryObject = (KYCModel) baseWrapper.getBasePersistableModel();
        CustomList<KYCModel> list;
        list = kycDAO.findByExample(queryObject, baseWrapper.getPagingHelperModel(), baseWrapper.getSortingOrderMap(), baseWrapper.getDateRangeHolderModel());
        baseWrapper.setCustomList(list);
        return baseWrapper;

    }

    @Override
    public List<ACOwnershipDetailModel> loadAccountOwnerShipDetails(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        ACOwnershipDetailModel queryObject = (ACOwnershipDetailModel) searchBaseWrapper.getBasePersistableModel();
        List<ACOwnershipDetailModel> list = new ArrayList<ACOwnershipDetailModel>();
        list = acOwnerShipDetailDAO.loadAccountOwnerShipsByCustomerId(queryObject);
        if (CollectionUtils.isEmpty(list)) {
            list.add(new ACOwnershipDetailModel()); //add empty row to display on screen
        }
        return list;

    }

    @Override
    public List<ACOwnershipDetailModel> loadL3AccountOwnerShipDetails(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        ACOwnershipDetailModel queryObject = (ACOwnershipDetailModel) searchBaseWrapper.getBasePersistableModel();
        List<ACOwnershipDetailModel> list = new ArrayList<ACOwnershipDetailModel>();
        list = acOwnerShipDetailDAO.loadAccountOwnerShipsByRetailerContactId(queryObject);
        return list;

    }

    @Override
    public String[] getFundSourceName(String[] fundSourceIds) {
        String[] fundSourceNames = new String[fundSourceIds.length];
        FundSourceModel fundSourceModel = null;
        int i = 0;
        for (String fundSourceId : fundSourceIds) {
            fundSourceModel = new FundSourceModel();
            fundSourceModel = this.fundSourceDAO.findByPrimaryKey(Long.valueOf(fundSourceId));
            fundSourceNames[i] = fundSourceModel.getName();
            i++;
        }
        return fundSourceNames;
    }

    @Override
    public BaseWrapper createAgentMerchant(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        /**
         * Loging the data
         */

        String actionLogHandler = (String) baseWrapper.getObject(CommandFieldConstants.KEY_ACTION_LOG_HANDLER);
        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        AgentMerchantDetailModel agentMerchantDetailModel = (AgentMerchantDetailModel) baseWrapper.getBasePersistableModel();

        if (null != agentMerchantDetailModel.getParentAgentId()) {

            //Setting Partner Group in case of sub agent
            SearchBaseWrapper wrapper = agentHierarchyManager.findPartnerGroupsByRetailer(agentMerchantDetailModel.getRetailerId());

            CustomList<PartnerGroupModel> customList = wrapper.getCustomList();
            if (customList != null && customList.getResultsetList().size() > 0) {
                PartnerGroupModel partnerGroupModel = customList.getResultsetList().get(0);
                agentMerchantDetailModel.setPartnerGroupId(partnerGroupModel.getPartnerGroupId());
            }
            //End Setting Partner Group in case of sub agent
            agentMerchantDetailModel = agentMerchantDetailDAO.saveOrUpdate(agentMerchantDetailModel);
            baseWrapper.setBasePersistableModel(agentMerchantDetailModel);
        } else {
            RetailerModel retailerModel = new RetailerModel();
            retailerModel.setActive(Boolean.TRUE);
            retailerModel.setAddress1("auto address1");
            retailerModel.setAddress2("auto address2");
            retailerModel.setAreaId(agentMerchantDetailModel.getAreaId());
            retailerModel.setContactName("auto contact");
            retailerModel.setCountry("Pakistan");
            retailerModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
            retailerModel.setCreatedOn(new Date());
            retailerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            retailerModel.setUpdatedOn(new Date());
            retailerModel.setDistributorId(agentMerchantDetailModel.getDistributorId());
            retailerModel.setName(agentMerchantDetailModel.getUserName() + "_" + "Franchise");
            retailerModel.setProductCatalogueId(agentMerchantDetailModel.getProductCatalogId());
            retailerModel.setRegionModelId(agentMerchantDetailModel.getRegionId());
            retailerModel.setEditMode(Boolean.FALSE);
            BaseWrapper baseWrapperFranchise = new BaseWrapperImpl();
            baseWrapperFranchise.setBasePersistableModel(retailerModel);

            baseWrapperFranchise.putObject("permissionGroupId", PermissionGroupConstants.RETAILOR);
            try {
                baseWrapperFranchise = agentHierarchyManager.saveOrUpdateFranchise(baseWrapperFranchise);
                retailerModel = (RetailerModel) baseWrapperFranchise.getBasePersistableModel();
            } catch (Exception e) {
                e.getMessage();
            }


            Set<Long> permission = new HashSet<Long>();
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.USER_GROUP_CREATE_USECASE_ID));

            PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
            PartnerModel partnerGroupPermissionListViewModel = new PartnerModel();
            partnerGroupPermissionListViewModel = agentHierarchyManager.findPartnerByRetailerId(retailerModel.getRetailerId());
            partnerGroupModel.setName(agentMerchantDetailModel.getUserName() + "_" + "PartnerGroup");

            PartnerPermissionViewModel partnerPermissionViewModel = new PartnerPermissionViewModel();
            partnerPermissionViewModel.setPartnerId(partnerGroupModel.getPartnerId());
            partnerPermissionViewModel.setIsDefault(true);//Load Default permissions like home page & change password
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(partnerPermissionViewModel);
            searchBaseWrapper = this.partnerGroupFacade.searchDefaultPartnerPermission(searchBaseWrapper);
            Iterator<PartnerPermissionViewModel> itrDP = searchBaseWrapper.getCustomList().getResultsetList().iterator();
            while (itrDP.hasNext()) {
                PartnerPermissionViewModel ppModel = itrDP.next();
                PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
                partnerGroupDetailModel.setUserPermissionId(ppModel.getUserPermissionId());
                partnerGroupDetailModel.setReadAllowed(ppModel.getReadAvailable());
                partnerGroupDetailModel.setUpdateAllowed(ppModel.getUpdateAvailable());
                partnerGroupDetailModel.setDeleteAllowed(ppModel.getDeleteAvailable());
                partnerGroupDetailModel.setCreateAllowed(ppModel.getCreateAvailable());
                partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                partnerGroupDetailModel.setCreatedOn(new Date());
                partnerGroupDetailModel.setUpdatedOn(new Date());

                if (permission.add(partnerGroupDetailModel.getUserPermissionId())) {
                    partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);//add default permissions like home page and change password
                }
            }

            //select retailer default permissions and add them- turab Jan 09 2015
            PermissionGroupDetailModel retailerPermissionGroupDetail = new PermissionGroupDetailModel();
            retailerPermissionGroupDetail.setPermissionGroupId(PermissionGroupConstants.RETAILOR);
            ReferenceDataWrapper permissionGrpDtlReferenceDataWrapper = new ReferenceDataWrapperImpl(retailerPermissionGroupDetail, "permissionGroupDetailId", SortingOrder.ASC);
            permissionGrpDtlReferenceDataWrapper.setBasePersistableModel(retailerPermissionGroupDetail);
            referenceDataManager.getReferenceData(permissionGrpDtlReferenceDataWrapper);
            if (permissionGrpDtlReferenceDataWrapper.getReferenceDataList() != null && permissionGrpDtlReferenceDataWrapper.getReferenceDataList().size() > 0) {
                List<PermissionGroupDetailModel> list = permissionGrpDtlReferenceDataWrapper.getReferenceDataList();
                for (PermissionGroupDetailModel permissionGrpDtl : list) {
                    PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
                    partnerGroupDetailModel.setUserPermissionId(permissionGrpDtl.getUserPermissionId());
                    partnerGroupDetailModel.setReadAllowed(false);
                    partnerGroupDetailModel.setUpdateAllowed(false);
                    partnerGroupDetailModel.setDeleteAllowed(false);
                    partnerGroupDetailModel.setCreateAllowed(false);

                    partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                    partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                    partnerGroupDetailModel.setCreatedOn(new Date());
                    partnerGroupDetailModel.setUpdatedOn(new Date());

                    if (permission.add(partnerGroupDetailModel.getUserPermissionId())) {
                        partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);//add default permissions like home page and change password
                    }
                }
            }
            //end by turab Jan 09 2015

            partnerGroupModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
            partnerGroupModel.setCreatedOn(new Date());
            partnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            partnerGroupModel.setUpdatedOn(new Date());
            partnerGroupModel.setActive(Boolean.TRUE);
            partnerGroupModel.setEditable(Boolean.TRUE);
            partnerGroupModel.setPartnerId(partnerGroupPermissionListViewModel.getPartnerId());

            baseWrapper.setBasePersistableModel(partnerGroupModel);
            try {
                baseWrapper = this.partnerGroupFacade.createPartnerGroup(baseWrapper);
                partnerGroupModel = (PartnerGroupModel) baseWrapper.getBasePersistableModel();
            } catch (FrameworkCheckedException fce) {
                fce.printStackTrace();
                throw fce;
            }

            partnerGroupModel = (PartnerGroupModel) baseWrapper.getBasePersistableModel();
            agentMerchantDetailModel.setRetailerId(retailerModel.getRetailerId());
            agentMerchantDetailModel.setPartnerGroupId(partnerGroupModel.getPartnerGroupId());
            agentMerchantDetailModel.setParentAgentIdRetailerContactModel(null);
            agentMerchantDetailModel.setUltimateParentAgentIdRetailerContactModel(null);
            agentMerchantDetailModel = agentMerchantDetailDAO.saveOrUpdate(agentMerchantDetailModel);
            baseWrapper.setBasePersistableModel(agentMerchantDetailModel);
        }

        /**
         * Logging Information, Ending Status
         */

        if (actionLogHandler == null) {
            AppUserModel currentUser = UserUtils.getCurrentUser();
            actionLogModel.setCustomField1(currentUser.getAppUserId().toString());
            actionLogModel.setCustomField11(currentUser.getUsername());
            this.actionLogManager.completeActionLog(actionLogModel);
        }
        return baseWrapper;
    }

    @Override
    public Boolean isAlreadyExistInitAppFormNumber(String initialApplicationFormNo) throws FrameworkCheckedException {
        AgentMerchantDetailModel agentMerchantDetailModel = new AgentMerchantDetailModel();
        agentMerchantDetailModel.setInitialAppFormNo(initialApplicationFormNo);

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setEnableLike(false);
        exampleConfigHolderModel.setIgnoreCase(true);


        CustomList<AgentMerchantDetailModel> list = agentMerchantDetailDAO.findByExample(agentMerchantDetailModel, null, null, exampleConfigHolderModel);

        KYCModel kycModel = new KYCModel();
        kycModel.setInitialAppFormNo(initialApplicationFormNo);
        CustomList<KYCModel> kycList = kycDAO.findByExample(kycModel, null, null, exampleConfigHolderModel);

        boolean flag = true;

        if (list.getResultsetList().size() > 0) {
            flag = false;
        }
        if (kycList.getResultsetList().size() > 0) {
            flag = false;
        }

        return flag;
    }

    @Override
    public Boolean isAlreadyExistReferenceNumber(String referenceNo, String initalAppFormNo) throws FrameworkCheckedException {
        AgentMerchantDetailModel agentMerchantDetailModel = new AgentMerchantDetailModel();
        agentMerchantDetailModel.setReferenceNo(referenceNo);

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setEnableLike(false);
        exampleConfigHolderModel.setIgnoreCase(true);


        CustomList<AgentMerchantDetailModel> list = agentMerchantDetailDAO.findByExample(agentMerchantDetailModel, null, null, exampleConfigHolderModel);

        boolean flag = true;

        if (list.getResultsetList().size() > 0) {

            if (list.getResultsetList().size() == 1) {
                AgentMerchantDetailModel agentMerchantDetailModeltemp = list.getResultsetList().get(0);

                if (agentMerchantDetailModeltemp.getInitialAppFormNo() != null && agentMerchantDetailModeltemp.getInitialAppFormNo().equals(initalAppFormNo))
                    return true;
                else
                    return false;
            } else {
                return false;
            }

        }
        return flag;
    }

    @Override
    public BaseWrapper findAgentMerchantByInitialAppFormNo(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        AgentMerchantDetailModel agentMerchantDetailModel = (AgentMerchantDetailModel) baseWrapper.getBasePersistableModel();

        List<AgentMerchantDetailModel> list = agentMerchantDetailDAO.getAgentMerchantDetailModel(agentMerchantDetailModel);

        if (list != null && !list.isEmpty()) {
            baseWrapper.setBasePersistableModel(list.get(0));
        }

        return baseWrapper;
    }

    @Override
    public BaseWrapper updateAgentMerchant(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        AgentMerchantDetailModel agentMerchantDetailModel = (AgentMerchantDetailModel) baseWrapper.getBasePersistableModel();

        ExampleConfigHolderModel configModel = new ExampleConfigHolderModel();
        configModel.setMatchMode(MatchMode.EXACT);
        configModel.setIgnoreCase(true);
        configModel.setEnableLike(false);

        BaseWrapper retailerContactWrapper = retailerContactManager.loadRetailerContactByIAF(agentMerchantDetailModel.getInitialAppFormNo());
        if (null != retailerContactWrapper.getBasePersistableModel()) {
            RetailerContactModel retailerContactModel = (RetailerContactModel) retailerContactWrapper.getBasePersistableModel();
            if (null != agentMerchantDetailModel.getDirectChangedToSub() && agentMerchantDetailModel.getDirectChangedToSub()) {
                retailerContactModel.setHead(Boolean.FALSE);
                retailerContactModel.setParentRetailerContactModelId(agentMerchantDetailModel.getParentAgentId());
                retailerContactModel.setDistributorLevelId(agentMerchantDetailModel.getDistributorLevelId());
            }
            retailerContactModel.setRetailerId(agentMerchantDetailModel.getRetailerId());
            retailerContactModel.setAreaId(agentMerchantDetailModel.getAreaId());
            retailerContactModel.setUpdatedOn(new Date());
            retailerContactModel.setParentRetailerContactModelId(agentMerchantDetailModel.getParentAgentId());
            retailerContactModel.setRetailerId(agentMerchantDetailModel.getRetailerId());
            retailerContactModel.setProductCatalogId(agentMerchantDetailModel.getProductCatalogId());
            retailerContactModel.setDistributorLevelId(agentMerchantDetailModel.getDistributorLevelId());
            retailerContactModel.setOlaCustomerAccountTypeModelId(agentMerchantDetailModel.getAcLevelQualificationId());
            retailerContactModel.setBusinessName(agentMerchantDetailModel.getBusinessName());

            AppUserModel appUserModel = new AppUserModel();
            appUserModel.setEmployeeId(agentMerchantDetailModel.getEmpId());

            CustomList<AppUserModel> appUserList = appUserDAO.findByExample(appUserModel, null, null, configModel);
            if (appUserList != null && appUserList.getResultsetList().size() > 0) {
                appUserModel = appUserList.getResultsetList().get(0);
                SalesHierarchyModel salesHierarchyModel = salesHierarchyDAO.findSaleUserByBankUserId(appUserModel.getAppUserId());
                retailerContactModel.setSalesHierarchyId(salesHierarchyModel.getSalesHierarchyId());
            }

            retailerContactDAO.saveOrUpdate(retailerContactModel);

            appUserModel = new AppUserModel();
            appUserModel.setRetailerContactId(retailerContactModel.getRetailerContactId());
            CustomList<AppUserModel> appusCustomList = appUserDAO.findByExample(appUserModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
            if (null != appusCustomList && null != appusCustomList.getResultsetList() && appusCustomList.getResultsetList().size() > 0) {
                appUserModel = appusCustomList.getResultsetList().get(0);
                appUserModel.setUsername(agentMerchantDetailModel.getUserName());
                appUserDAO.saveOrUpdate(appUserModel);
            }

            UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
            userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
            CustomList<UserDeviceAccountsModel> userDeviceAccountsModelList = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
            userDeviceAccountsModel = userDeviceAccountsModelList.getResultsetList().get(0);
            userDeviceAccountsModel.setProdCatalogId(agentMerchantDetailModel.getProductCatalogId());
            this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
            /**
             * Data migration support
             */
            if (agentMerchantDetailModel.getRetailerId() == null) {
                if (null != agentMerchantDetailModel.getParentAgentId()) {
                    agentMerchantDetailModel = agentMerchantDetailDAO.saveOrUpdate(agentMerchantDetailModel);
                    baseWrapper.setBasePersistableModel(agentMerchantDetailModel);
                } else {
                    RetailerModel retailerModel = new RetailerModel();
                    retailerModel.setActive(Boolean.TRUE);
                    retailerModel.setAddress1("auto address1");
                    retailerModel.setAddress2("auto address2");
                    retailerModel.setAreaId(agentMerchantDetailModel.getAreaId());
                    retailerModel.setContactName("auto contact");
                    retailerModel.setCountry("Pakistan");
                    retailerModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    retailerModel.setCreatedOn(new Date());
                    retailerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    retailerModel.setUpdatedOn(new Date());
                    retailerModel.setDistributorId(agentMerchantDetailModel.getDistributorId());
                    retailerModel.setName(agentMerchantDetailModel.getInitialAppFormNo() + "_" + "Franchise");
                    retailerModel.setProductCatalogueId(agentMerchantDetailModel.getProductCatalogId());
                    retailerModel.setRegionModelId(agentMerchantDetailModel.getRegionId());
                    retailerModel.setEditMode(Boolean.FALSE);
                    BaseWrapper baseWrapperFranchise = new BaseWrapperImpl();
                    baseWrapperFranchise.setBasePersistableModel(retailerModel);

                    baseWrapperFranchise.putObject("permissionGroupId", PermissionGroupConstants.RETAILOR);
                    try {
                        baseWrapperFranchise = agentHierarchyManager.saveOrUpdateFranchise(baseWrapperFranchise);
                        retailerModel = (RetailerModel) baseWrapperFranchise.getBasePersistableModel();
                    } catch (Exception e) {
                        e.getMessage();
                    }


                    Set<Long> permission = new HashSet<Long>();
                    baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
                    baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.USER_GROUP_CREATE_USECASE_ID));

                    PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
                    PartnerModel partnerGroupPermissionListViewModel = new PartnerModel();
                    partnerGroupPermissionListViewModel = agentHierarchyManager.findPartnerByRetailerId(retailerModel.getRetailerId());
                    partnerGroupModel.setName(agentMerchantDetailModel.getInitialAppFormNo() + "_" + "PartnerGroup");

                    PartnerPermissionViewModel partnerPermissionViewModel = new PartnerPermissionViewModel();
                    partnerPermissionViewModel.setPartnerId(partnerGroupModel.getPartnerId());
                    partnerPermissionViewModel.setIsDefault(true);//Load Default permissions like home page & change password
                    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                    searchBaseWrapper.setBasePersistableModel(partnerPermissionViewModel);
                    searchBaseWrapper = this.partnerGroupFacade.searchDefaultPartnerPermission(searchBaseWrapper);
                    Iterator<PartnerPermissionViewModel> itrDP = searchBaseWrapper.getCustomList().getResultsetList().iterator();
                    while (itrDP.hasNext()) {
                        PartnerPermissionViewModel ppModel = itrDP.next();
                        PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
                        partnerGroupDetailModel.setUserPermissionId(ppModel.getUserPermissionId());
                        partnerGroupDetailModel.setReadAllowed(ppModel.getReadAvailable());
                        partnerGroupDetailModel.setUpdateAllowed(ppModel.getUpdateAvailable());
                        partnerGroupDetailModel.setDeleteAllowed(ppModel.getDeleteAvailable());
                        partnerGroupDetailModel.setCreateAllowed(ppModel.getCreateAvailable());
                        partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                        partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                        partnerGroupDetailModel.setCreatedOn(new Date());
                        partnerGroupDetailModel.setUpdatedOn(new Date());

                        if (permission.add(partnerGroupDetailModel.getUserPermissionId())) {
                            partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);//add default permissions like home page and change password
                        }
                    }

                    //select retailer default permissions and add them- turab Jan 09 2015
                    PermissionGroupDetailModel retailerPermissionGroupDetail = new PermissionGroupDetailModel();
                    retailerPermissionGroupDetail.setPermissionGroupId(PermissionGroupConstants.RETAILOR);
                    ReferenceDataWrapper permissionGrpDtlReferenceDataWrapper = new ReferenceDataWrapperImpl(retailerPermissionGroupDetail, "permissionGroupDetailId", SortingOrder.ASC);
                    permissionGrpDtlReferenceDataWrapper.setBasePersistableModel(retailerPermissionGroupDetail);
                    referenceDataManager.getReferenceData(permissionGrpDtlReferenceDataWrapper);
                    if (permissionGrpDtlReferenceDataWrapper.getReferenceDataList() != null && permissionGrpDtlReferenceDataWrapper.getReferenceDataList().size() > 0) {
                        List<PermissionGroupDetailModel> list = permissionGrpDtlReferenceDataWrapper.getReferenceDataList();
                        for (PermissionGroupDetailModel permissionGrpDtl : list) {
                            PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
                            partnerGroupDetailModel.setUserPermissionId(permissionGrpDtl.getUserPermissionId());
                            partnerGroupDetailModel.setReadAllowed(false);
                            partnerGroupDetailModel.setUpdateAllowed(false);
                            partnerGroupDetailModel.setDeleteAllowed(false);
                            partnerGroupDetailModel.setCreateAllowed(false);

                            partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
                            partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                            partnerGroupDetailModel.setCreatedOn(new Date());
                            partnerGroupDetailModel.setUpdatedOn(new Date());

                            if (permission.add(partnerGroupDetailModel.getUserPermissionId())) {
                                partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);//add default permissions like home page and change password
                            }
                        }
                    }

                    partnerGroupModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    partnerGroupModel.setCreatedOn(new Date());
                    partnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    partnerGroupModel.setUpdatedOn(new Date());
                    partnerGroupModel.setActive(Boolean.TRUE);
                    partnerGroupModel.setEditable(Boolean.TRUE);
                    partnerGroupModel.setPartnerId(partnerGroupPermissionListViewModel.getPartnerId());

                    baseWrapper.setBasePersistableModel(partnerGroupModel);
                    try {
                        baseWrapper = this.partnerGroupFacade.createPartnerGroup(baseWrapper);
                        partnerGroupModel = (PartnerGroupModel) baseWrapper.getBasePersistableModel();
                    } catch (FrameworkCheckedException fce) {
                        fce.printStackTrace();
                        throw fce;
                    }

                    partnerGroupModel = (PartnerGroupModel) baseWrapper.getBasePersistableModel();
                    agentMerchantDetailModel.setRetailerId(retailerModel.getRetailerId());
                    agentMerchantDetailModel.setPartnerGroupId(partnerGroupModel.getPartnerGroupId());
                    agentMerchantDetailModel = agentMerchantDetailDAO.saveOrUpdate(agentMerchantDetailModel);
                }
            } else {

                RetailerModel retailerModel = agentHierarchyManager.loadFranchise(agentMerchantDetailModel.getRetailerId());

                retailerModel.setAreaId(agentMerchantDetailModel.getAreaId());
                retailerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                retailerModel.setUpdatedOn(new Date());
                retailerModel.setDistributorId(agentMerchantDetailModel.getDistributorId());
				/*if(null != agentMerchantDetailModel.getDirectChangedToSub() && !agentMerchantDetailModel.getDirectChangedToSub()) {
					retailerModel.setName(agentMerchantDetailModel.getInitialAppFormNo() + "_Franchise");
				}*/
                retailerModel.setProductCatalogueId(agentMerchantDetailModel.getProductCatalogId());
                retailerModel.setRegionModelId(agentMerchantDetailModel.getRegionId());

                retailerModel = agentHierarchyManager.saveOrUpdateRetailerModel(retailerModel);
            }

            /**
             * end of data migration support
             */

            //Setting Partner Group dynamically on the bases of Retailer Id
            SearchBaseWrapper wrapper = agentHierarchyManager.findPartnerGroupsByRetailer(agentMerchantDetailModel.getRetailerId());

            CustomList<PartnerGroupModel> customList = wrapper.getCustomList();
            if (customList != null && customList.getResultsetList().size() > 0) {
                PartnerGroupModel partnerGroupModel = customList.getResultsetList().get(0);
                agentMerchantDetailModel.setPartnerGroupId(partnerGroupModel.getPartnerGroupId());
            }
            //End Setting Partner Group dynamically on the bases of Retailer Id

            AppUserPartnerGroupModel appUserPartnerGroupModel = new AppUserPartnerGroupModel();
            appUserPartnerGroupModel.setAppUserId(appUserModel.getAppUserId());
            //appUserPartnerGroupModel.setPartnerGroupId(mnoUserFormModel.getPartnerGroupId());
            CustomList list = appUserPartnerGroupDAO.findByExample(appUserPartnerGroupModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
            if (list.getResultsetList().size() > 0) {

                appUserPartnerGroupModel = (AppUserPartnerGroupModel) list.getResultsetList().get(0);
                appUserPartnerGroupModel.setPartnerGroupId(agentMerchantDetailModel.getPartnerGroupId());
                appUserPartnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                appUserPartnerGroupModel.setUpdatedOn(new Date());
                appUserPartnerGroupDAO.saveOrUpdate(appUserPartnerGroupModel);
            }

//			String encryptedNic	=	com.inov8.ola.util.EncryptionUtil.encryptPin(appUserModel.getNic());
            String encryptedNic = appUserModel.getNic();

            AccountHolderModel holderModel = new AccountHolderModel();
            holderModel.setActive(Boolean.TRUE);
            holderModel.setCnic(encryptedNic);

            CustomList<AccountHolderModel> accountHolderCustomList =
                    accountHolderDAO.findByExample(holderModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

            if (accountHolderCustomList != null && accountHolderCustomList.getResultsetList().size() > 0) {
                holderModel = accountHolderCustomList.getResultsetList().get(0);
                try {
                    AccountModel accountModel = (AccountModel) (holderModel.getAccountHolderIdAccountModelList().toArray())[0];
                    accountModel.setCustomerAccountTypeId(agentMerchantDetailModel.getAcLevelQualificationId());
                    accountDAO.saveOrUpdate(accountModel);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        ////////turab
        //by turab, if head agent selected add its franchise
        /*if(null!=agentMerchantDetailModel.getParentAgentId()){*/

        if (null == agentMerchantDetailModel.getParentAgentId()) {
            agentMerchantDetailModel.setParentAgentIdRetailerContactModel(null);
            agentMerchantDetailModel.setUltimateParentAgentIdRetailerContactModel(null);
        }

        agentMerchantDetailModel = agentMerchantDetailDAO.saveOrUpdate(agentMerchantDetailModel);
        baseWrapper.setBasePersistableModel(agentMerchantDetailModel);

	      /*}else{
			RetailerModel retailerModel = new RetailerModel();
			retailerModel.setActive(Boolean.TRUE);
			retailerModel.setAddress1("auto address1");
			retailerModel.setAddress2("auto address2");
			retailerModel.setAreaId(agentMerchantDetailModel.getAreaId());
			retailerModel.setContactName("auto contact");
			retailerModel.setCountry("Pakistan");
			retailerModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			retailerModel.setCreatedOn(new Date());
			retailerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			retailerModel.setUpdatedOn(new Date());
			retailerModel.setDistributorId(agentMerchantDetailModel.getDistributorId());
			retailerModel.setName(agentMerchantDetailModel.getBusinessName()+"_"+"Franchise");
			retailerModel.setProductCatalogueId(agentMerchantDetailModel.getProductCatalogId());
			retailerModel.setRegionModelId(agentMerchantDetailModel.getRegionId());
			retailerModel.setEditMode(Boolean.FALSE);
			BaseWrapper baseWrapperFranchise = new BaseWrapperImpl();
			baseWrapperFranchise.setBasePersistableModel(retailerModel);

			baseWrapperFranchise.putObject("permissionGroupId", PermissionGroupConstants.RETAILOR);
			try{
				baseWrapperFranchise = agentHierarchyManager.saveOrUpdateFranchise(baseWrapperFranchise);
			}catch(Exception e){
				e.getMessage();
				throw new FrameworkCheckedException("Frachnise already exists with this name.");
			}
			retailerModel = (RetailerModel) baseWrapperFranchise.getBasePersistableModel();
			agentMerchantDetailModel.setRetailerId(retailerModel.getRetailerId());

			Set<Long> permission = new HashSet<Long>();
			BaseWrapper baseWrapperPartnerGroup = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.USER_GROUP_CREATE_USECASE_ID));

			PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
			PartnerModel partnerGroupPermissionListViewModel = new PartnerModel();
			partnerGroupPermissionListViewModel = agentHierarchyManager.findPartnerByRetailerId(agentMerchantDetailModel.getRetailerId());
			partnerGroupModel.setName(agentMerchantDetailModel.getBusinessName()+"_"+"PartnerGroup");

			PartnerPermissionViewModel partnerPermissionViewModel = new PartnerPermissionViewModel();
			partnerPermissionViewModel.setPartnerId(partnerGroupModel.getPartnerId());
			partnerPermissionViewModel.setIsDefault(true);//Load Default permissions like home page & change password
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(partnerPermissionViewModel);
			searchBaseWrapper = this.partnerGroupFacade.searchDefaultPartnerPermission(searchBaseWrapper);
			Iterator<PartnerPermissionViewModel> itrDP = searchBaseWrapper.getCustomList().getResultsetList().iterator();
			while(itrDP.hasNext())
			{
				PartnerPermissionViewModel ppModel = itrDP.next();
				PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
				partnerGroupDetailModel.setUserPermissionId(ppModel.getUserPermissionId());
				partnerGroupDetailModel.setReadAllowed(ppModel.getReadAvailable());
				partnerGroupDetailModel.setUpdateAllowed(ppModel.getUpdateAvailable());
				partnerGroupDetailModel.setDeleteAllowed(ppModel.getDeleteAvailable());
				partnerGroupDetailModel.setCreateAllowed(ppModel.getCreateAvailable());
				partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
				partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				partnerGroupDetailModel.setCreatedOn(new Date());
				partnerGroupDetailModel.setUpdatedOn(new Date());

				if(permission.add(partnerGroupDetailModel.getUserPermissionId()))
				{
					partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);//add default permissions like home page and change password
				}
			}

			//select retailer default permissions and add them- turab Jan 09 2015
			PermissionGroupDetailModel retailerPermissionGroupDetail = new PermissionGroupDetailModel();
			retailerPermissionGroupDetail.setPermissionGroupId(PermissionGroupConstants.RETAILOR);
			ReferenceDataWrapper permissionGrpDtlReferenceDataWrapper = new ReferenceDataWrapperImpl(retailerPermissionGroupDetail, "permissionGroupDetailId", SortingOrder.ASC);
			permissionGrpDtlReferenceDataWrapper.setBasePersistableModel(retailerPermissionGroupDetail);
		    referenceDataManager.getReferenceData(permissionGrpDtlReferenceDataWrapper);
		    if(permissionGrpDtlReferenceDataWrapper.getReferenceDataList() != null && permissionGrpDtlReferenceDataWrapper.getReferenceDataList().size()>0){
		    	List<PermissionGroupDetailModel> list = permissionGrpDtlReferenceDataWrapper.getReferenceDataList();
		    	for(PermissionGroupDetailModel permissionGrpDtl : list){
		    		PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
					partnerGroupDetailModel.setUserPermissionId(permissionGrpDtl.getUserPermissionId());
					partnerGroupDetailModel.setReadAllowed(false);
					partnerGroupDetailModel.setUpdateAllowed(false);
					partnerGroupDetailModel.setDeleteAllowed(false);
					partnerGroupDetailModel.setCreateAllowed(false);

					partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
					partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
					partnerGroupDetailModel.setCreatedOn(new Date());
					partnerGroupDetailModel.setUpdatedOn(new Date());

					if(permission.add(partnerGroupDetailModel.getUserPermissionId()))
					{
						partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);//add default permissions like home page and change password
					}
		    	}
		    }
			//end by turab Jan 09 2015

			partnerGroupModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			partnerGroupModel.setCreatedOn(new Date());
			partnerGroupModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			partnerGroupModel.setUpdatedOn(new Date());
			partnerGroupModel.setActive(Boolean.TRUE);
			partnerGroupModel.setEditable(Boolean.TRUE);
			partnerGroupModel.setPartnerId(partnerGroupPermissionListViewModel.getPartnerId());

			baseWrapper.setBasePersistableModel(partnerGroupModel);
			try
			{
				baseWrapper = this.partnerGroupFacade.createPartnerGroup(baseWrapper);
			}catch(FrameworkCheckedException fce)
			{
				fce.printStackTrace();
		        throw fce;
			}

			partnerGroupModel = (PartnerGroupModel) baseWrapper.getBasePersistableModel();
			agentMerchantDetailModel.setPartnerGroupId(partnerGroupModel.getPartnerGroupId());

	        agentMerchantDetailModel = agentMerchantDetailDAO.saveOrUpdate(agentMerchantDetailModel);
	        baseWrapper.setBasePersistableModel(agentMerchantDetailModel);
		}*/
        return baseWrapper;
    }

    @Override
    public List<ApplicantTxModeModel> loadApplicantTxModeModelByInitialApplicationFormNo(String formNumber) throws FrameworkCheckedException {
        ApplicantTxModeModel modeOfTx = new ApplicantTxModeModel();
        modeOfTx.setInitialAppFormNo(formNumber);
        List<ApplicantTxModeModel> modeList = transactionModeDAO.findByExample(modeOfTx).getResultsetList();
        return modeList;
    }

    @Override
    public BaseWrapper searchCustomerByInitialAppFormNo(BaseWrapper wrapper) throws FrameworkCheckedException {
        return custTransManager.searchCustomerByInitialAppFormNo(wrapper);
    }

    @Override
    public BaseWrapper loadCustomer(BaseWrapper wrapper) throws FrameworkCheckedException {
        return custTransManager.loadCustomer(wrapper);
    }

    @Override
    public SearchBaseWrapper searchAgentMerchant(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        AgentMerchantDetailViewModel agentMerchantDetailViewModel = (AgentMerchantDetailViewModel) searchBaseWrapper.getBasePersistableModel();

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);

        CustomList<AgentMerchantDetailViewModel> customList = agentMerchantDetailViewDAO.findByExample(agentMerchantDetailViewModel,
                searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(),
                searchBaseWrapper.getDateRangeHolderModel(), exampleConfigHolderModel);

        searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    @Override
    public AgentMerchantDetailModel loadAgentMerchantDetailModelByUserName(String userName)
            throws FrameworkCheckedException {

        AgentMerchantDetailModel agentMerchantDetailModel = new AgentMerchantDetailModel();
        agentMerchantDetailModel.setUserName(userName);
        CustomList<AgentMerchantDetailModel> customList = agentMerchantDetailDAO.findByExample(agentMerchantDetailModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        if (customList != null && customList.getResultsetList().size() > 0) {
            agentMerchantDetailModel = customList.getResultsetList().get(0);
            return agentMerchantDetailModel;
        }
        return null;
    }

    public void saveRootedMobileHistory(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        RootedMobileHistoryModel model = (RootedMobileHistoryModel) baseWrapper.getBasePersistableModel();
        rootedMobileHistoryDAO.saveOrUpdate(model);
    }

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }

    public void setTransactionDetailDAO(TransactionDetailDAO transactionDetailDAO) {
        this.transactionDetailDAO = transactionDetailDAO;
    }

    public void setTransactionManager(TransactionModuleManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setSettlementManager(SettlementManager settlementManager) {
        this.settlementManager = settlementManager;
    }

    public void setCustomerFundSourceDAO(CustomerFundSourceDAO customerFundSourceDAO) {
        if (customerFundSourceDAO != null) {
            this.customerFundSourceDAO = customerFundSourceDAO;
        }
    }

    public void setIvrRequestHandler(IvrRequestHandler ivrRequestHandler) {
        this.ivrRequestHandler = ivrRequestHandler;
    }

    public void setKycDAO(KYCDAO kycDAO) {
        this.kycDAO = kycDAO;
    }

    public void setTransactionModeDAO(ApplicantTxModeDAO transactionModeDAO) {
        this.transactionModeDAO = transactionModeDAO;
    }

    public void setAgentMerchantDetailDAO(AgentMerchantDetailDAO agentMerchantDetailDAO) {
        this.agentMerchantDetailDAO = agentMerchantDetailDAO;
    }

    public void setAcOwnerShipDetailDAO(ACOwnerShipDAO acOwnerShipDetailDAO) {
        this.acOwnerShipDetailDAO = acOwnerShipDetailDAO;
    }

    public void setRetailerContactDAO(RetailerContactDAO retailerContactDAO) {
        this.retailerContactDAO = retailerContactDAO;
    }

    public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
        if (agentHierarchyManager != null) {
            this.agentHierarchyManager = agentHierarchyManager;
        }
    }

    public void setPartnerGroupFacade(PartnerGroupFacade partnerGroupFacade) {
        if (partnerGroupFacade != null) {
            this.partnerGroupFacade = partnerGroupFacade;
        }
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        if (referenceDataManager != null) {
            this.referenceDataManager = referenceDataManager;
        }
    }

    @Override
    public AgentMerchantDetailModel loadAgentMerchantDetailModel(Long agentMerchantDetailId)
            throws FrameworkCheckedException {
        return agentMerchantDetailDAO.findByPrimaryKey(agentMerchantDetailId);
    }

    @Override
    public CityModel loadCityModel(Long cityId) throws FrameworkCheckedException {
        CityModel cityModel = cityDAO.findByPrimaryKey(cityId);
        return cityModel;
    }

    @Override
    public OccupationModel loadOccupationModel(Long occupationId) throws FrameworkCheckedException {
        OccupationModel occupationModel = occupationDAO.findByPrimaryKey(occupationId);
        return occupationModel;
    }

    @Override
    public ProfessionModel loadProfessionModel(Long professionId) throws FrameworkCheckedException {
        ProfessionModel professionModel = professionDAO.findByPrimaryKey(professionId);
        return professionModel;
    }


    public void setRetailerContactManager(
            RetailerContactManager retailerContactManager) {
        if (retailerContactManager != null) {
            this.retailerContactManager = retailerContactManager;
        }
    }

    public void setAgentMerchantDetailViewDAO(AgentMerchantDetailViewDAO agentMerchantDetailViewDAO) {
        this.agentMerchantDetailViewDAO = agentMerchantDetailViewDAO;
    }

    public void setAppUserPartnerGroupDAO(AppUserPartnerGroupDAO appUserPartnerGroupDAO) {
        this.appUserPartnerGroupDAO = appUserPartnerGroupDAO;
    }

    public void setAccountHolderDAO(AccountHolderDAO accountHolderDAO) {
        this.accountHolderDAO = accountHolderDAO;
    }

    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void setCityDAO(CityDAO cityDAO) {
        if (cityDAO != null) {
            this.cityDAO = cityDAO;
        }
    }

    public void setOccupationDAO(OccupationDAO occupationDAO) {
        if (occupationDAO != null) {
            this.occupationDAO = occupationDAO;
        }
    }

    public void setProfessionDAO(ProfessionDAO professionDAO) {
        if (professionDAO != null) {
            this.professionDAO = professionDAO;
        }
    }

    public void setSalesHierarchyDAO(SalesHierarchyDAO salesHierarchyDAO) {
        this.salesHierarchyDAO = salesHierarchyDAO;
    }

    public void setArbitraryResourceLoader(ArbitraryResourceLoader arbitraryResourceLoader) {
        this.arbitraryResourceLoader = arbitraryResourceLoader;
    }

    public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
        this.transactionDetailMasterManager = transactionDetailMasterManager;
    }

    public void setRootedMobileHistoryDAO(
            RootedMobileHistoryDAO rootedMobileHistoryDAO) {
        this.rootedMobileHistoryDAO = rootedMobileHistoryDAO;
    }

    public void setAccountControlManager(AccountControlManager accountControlManager) {
        this.accountControlManager = accountControlManager;
    }

    @Override
    public int validateCNIC(String cnic, String mobile) throws Exception {

        int result = 2;
        List<Object[]> users = appUserDAO.loadUserByCNIC(cnic, mobile);

        if (CollectionUtils.isEmpty(users))
            return result;

        for (Object[] o : users) {
            long autid = Long.valueOf(o[0].toString());
            String foundCNIC = (String) o[1];
            String foundMobile = (String) o[2];
            long regId = -1l;
            boolean blacklisted = false;

            if (o.length == 4)
                blacklisted = Boolean.valueOf(o[3].toString());

            if (o.length == 5)
                regId = Long.valueOf(o[3].toString());

            if (blacklisted || RegistrationStateConstants.BLACK_LISTED == regId) {
                return 1;
            }

            // agent/customer/handler already exists
            else if ((UserTypeConstantsInterface.CUSTOMER == autid || UserTypeConstantsInterface.RETAILER == autid || UserTypeConstantsInterface.HANDLER == autid) &&
                    (RegistrationStateConstants.BULK_REQUEST_RECEIVED == regId || RegistrationStateConstants.REQUEST_RECEIVED == regId || RegistrationStateConstants.VERIFIED == regId)) {

                return 0;
            }

            // walk in already exists
            else if (UserTypeConstantsInterface.WALKIN_CUSTOMER == autid) {
                if (cnic.equals(foundCNIC)) { // walk-in exists with same CNIC
                    result = 4;//givenMobile.equals(foundMobile) ? 4 : 3; // walk-in foundMobile no is also match
                }
            }
        }

        return result;
    }

    @Override
    public int validateMobileNo(String mobileNo, Long appUserTypeId) throws Exception {
        List<Object[]> users = appUserDAO.loadUserByMobileNo(mobileNo, appUserTypeId);
        int result = 0;

        if (CollectionUtils.isEmpty(users))
            return result;

        for (Object[] o : users) {
            long autid = Long.valueOf(o[0].toString());
            String foundMobile = (String) o[1];
            long regId = Long.valueOf(o[2].toString());
            boolean settled = CommonUtils.convertToBoolean(o[3].toString());
            boolean unsettled = CommonUtils.convertToBoolean(o[4].toString());

            if (RegistrationStateConstants.BLACK_LISTED == regId) {
                return 1;
            } else if (unsettled || settled) {
                return 2;
            } else if (!(RegistrationStateConstants.BULK_REQUEST_RECEIVED == regId ||
                    RegistrationStateConstants.REQUEST_RECEIVED == regId ||
                    RegistrationStateConstants.VERIFIED == regId)) {

                return 3;
            } else if (UserTypeConstantsInterface.RETAILER.longValue() == autid &&
                    (RegistrationStateConstants.REQUEST_RECEIVED == regId || RegistrationStateConstants.BULK_REQUEST_RECEIVED == regId))
                return 3;

            else
                return 4;
        }

        return result;
    }

    public void setAppInfoDAO(AppInfoDAO appInfoDAO) {
        this.appInfoDAO = appInfoDAO;
    }

    public boolean isAppUserUsernameUniqueForUpdate(String userName, String initAppFormNo) throws FrameworkCheckedException {
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setUsername(userName);
        CustomList<AppUserModel> customList = appUserDAO.findByExample(appUserModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        boolean flag = false;
        if (customList.getResultsetList().size() == 0) {
            flag = true;
        } else if (customList.getResultsetList().size() == 1) {
            appUserModel = customList.getResultsetList().get(0);
            if (appUserModel.getRetailerContactId() != null && appUserModel.getRetailerContactIdRetailerContactModel().getInitialAppFormNo().equals(initAppFormNo)) {
                flag = true;
            }
        }

        return flag;
    }

    public void setBulkAgentCreationDAO(BulkAgentCreationDAO bulkAgentCreationDAO) {
        this.bulkAgentCreationDAO = bulkAgentCreationDAO;
    }

    public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
        this.transactionReversalManager = transactionReversalManager;
    }

    private void loadAndForwardAccountToQueue(final WorkFlowWrapper workFlowWrapper) throws InterruptedException {
        coreAdviceQueingPreProcessor.startProcessing(workFlowWrapper);
    }

    public void setCoreAdviceQueingPreProcessor(CoreAdviceQueingPreProcessor coreAdviceQueingPreProcessor) {
        this.coreAdviceQueingPreProcessor = coreAdviceQueingPreProcessor;
    }

    public void setBlinkCustomerDAO(BlinkCustomerDAO blinkCustomerDAO) {
        this.blinkCustomerDAO = blinkCustomerDAO;
    }

    public void setBlinkCustomerPictureDAO(BlinkCustomerPictureDAO blinkCustomerPictureDAO) {
        this.blinkCustomerPictureDAO = blinkCustomerPictureDAO;
    }

    public void setBlinkCustomerModelDAO(BlinkCustomerModelDAO blinkCustomerModelDAO) {
        this.blinkCustomerModelDAO = blinkCustomerModelDAO;
    }

    public void setDebitCardMailingAddressDAO(DebitCardMailingAddressDAO debitCardMailingAddressDAO) {
        this.debitCardMailingAddressDAO = debitCardMailingAddressDAO;
    }

    public void setFetchCardTypeDAO(FetchCardTypeDAO fetchCardTypeDAO) {
        this.fetchCardTypeDAO = fetchCardTypeDAO;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public void setDebitCardRequestsViewModelDAO(DebitCardRequestsViewModelDAO debitCardRequestsViewModelDAO) {
        this.debitCardRequestsViewModelDAO = debitCardRequestsViewModelDAO;
    }

    public void setBlinkDefaultLimitDAO(BlinkDefaultLimitDAO blinkDefaultLimitDAO) {
        this.blinkDefaultLimitDAO = blinkDefaultLimitDAO;
    }

    public void setMinorUserInfoListViewDAO(MinorUserListViewDAO minorUserInfoListViewDAO) {
        this.minorUserInfoListViewDAO = minorUserInfoListViewDAO;
    }
}
