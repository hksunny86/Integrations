package com.inov8.microbank.server.service.mfsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.app.service.AppManager;
import com.inov8.microbank.cardconfiguration.service.CardConfigurationManager;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.ExtendedTransactionDetailPortalListModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.MiniStatementListViewModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.model.productmodule.CategoryModel;
//import com.inov8.microbank.common.model.schedulemodule.ScheduleFundsTransferDetailModel;
//import com.inov8.microbank.common.model.schedulemodule.ScheduleFundsTransferDetailModel;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.debitcard.dao.DebitCardMailingAddressDAO;
import com.inov8.microbank.debitcard.dao.DebitCardModelDAO;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.debitcard.model.DebitCardRequestsViewModel;
import com.inov8.microbank.demographics.service.DemographicsManager;
import com.inov8.microbank.faq.service.FaqManager;
import com.inov8.microbank.fonepay.dao.hibernate.VirtualCardHibernateDAO;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.hra.airtimetopup.dao.RemitanceInfoDAO;
import com.inov8.microbank.hra.paymtnc.dao.PayMtncRequestDAO;
import com.inov8.microbank.nadraVerisys.dao.VerisysDataDAO;
import com.inov8.microbank.otp.service.MiniTransactionManager;
import com.inov8.microbank.server.dao.actionlogmodule.ActionLogDAO;
import com.inov8.microbank.server.dao.addressmodule.CustomerAddressesDAO;
import com.inov8.microbank.server.dao.bankmodule.BankSegmentsDAO;
import com.inov8.microbank.server.dao.bankmodule.MemberBankDAO;
import com.inov8.microbank.server.dao.bispcustnadraverification.BISPCustNadraVerificationDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerPictureDAO;
import com.inov8.microbank.server.dao.customermodule.SegmentDAO;
import com.inov8.microbank.server.dao.fetchcardtype.FetchCardTypeDAO;
import com.inov8.microbank.server.dao.geolocationmodule.GeoLocationDAO;
import com.inov8.microbank.server.dao.jsloansmodule.JSLoansDAO;
import com.inov8.microbank.server.dao.mnomodule.MnoUserDAO;
import com.inov8.microbank.server.dao.portal.apicitymodule.ApiCityDAO;
import com.inov8.microbank.server.dao.portal.authorizationmodule.ActionAuthorizationModelDAO;
import com.inov8.microbank.server.dao.portal.citymodule.CityDAO;
import com.inov8.microbank.server.dao.portal.ola.OlaCustomerAccountTypeDao;
import com.inov8.microbank.server.dao.portal.taxregimemodule.TaxRegimeDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerDAO;
import com.inov8.microbank.server.dao.safrepo.WalletSafRepoDAO;
import com.inov8.microbank.server.dao.thirdpartcashoutmodule.FetchThirdPartySegmentsDAO;
import com.inov8.microbank.server.dao.thirdpartcashoutmodule.ThirdPartyAcOpeningDAO;
import com.inov8.microbank.server.dao.transactionmodule.MiniTransactionDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionPurposeDAO;
import com.inov8.microbank.server.dao.usecasemodule.UsecaseDAO;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import com.inov8.microbank.server.service.agentbvsstate.AgentBvsStatManager;
import com.inov8.microbank.server.service.agentlocationstat.AgentLocationStatManager;
import com.inov8.microbank.server.service.commandmodule.minicommandmodule.BlinkBVSVerificationInquiryCommand;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.dispenser.ProductDispenser;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface CommonCommandManager {
    public List<CustomerPictureModel> getDiscrepantCustomerPictures(Long customerId) throws FrameworkCheckedException;

    public WorkFlowWrapper makeCustomerTrxByTransactionCode(String transactionId, String cnicCustomer) throws FrameworkCheckedException;

    public void loadAndForwardAccountToQueue(String transactionId) throws Exception;

    /*added by muhammad atif*/
    public int countCustomerPendingTrx(String cnic) throws FrameworkCheckedException;

    public BaseWrapper saveOrUpdateAccountOpeningL0Request(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    ;

    public int countByExample(BasePersistableModel basePersistableModel);

    public OracleSequenceGeneratorJdbcDAO getSequenceGenerator();

    public File loadImage(String path) throws IOException;

    public OracleSequenceGeneratorJdbcDAO getDeviceApplicationNoGenerator();

    public ActionLogModel logAction(ActionLogModel actionLogModel) throws FrameworkCheckedException;

    public RegistrationStateModel getRegistrationStateById(Long registrationStateId) throws FrameworkCheckedException;

    public CustomList<RegistrationStateModel> getRegistrationStateByIds(Long[] registrationStateIds) throws FrameworkCheckedException;

    public CustomList<BlinkCustomerRegistrationStateModel> getBlinkRegistrationStateByIds(Long[] registrationStateIds) throws FrameworkCheckedException;

    public AppUserModel getAppUserWithRegistrationState(String mobileNo, String cnic, Long registrationState) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByCnicAndType(String cnic) throws FrameworkCheckedException;

    public AppUserModel loadAppUserModelByEmailAddress(String emailAddress) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByCnicAndMobileAndAppUserType(String cnic, String mobileNo, Long appUserTypeId) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByMobileAndType(String mobileNo) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByMobileAndTypeForCustomer(String mobileNo) throws FrameworkCheckedException;

    public BlinkCustomerModel loadBlinkCustomerByMobileAndAccUpdate(String mobileNo, Long accUpdate) throws FrameworkCheckedException;

    public BlinkCustomerModel loadBlinkCustomerByBlinkCustomerId(Long accType) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByMobileAndType(String mobileNo, Long... appUserTypes) throws FrameworkCheckedException;

    public OLAVO loadAccountInfoFromOLA(String cnic, Long bankId) throws FrameworkCheckedException;

    public CustomList<CategoryModel> fetchAllProductCatalogCategories() throws FrameworkCheckedException;

    public AbstractFinancialInstitution loadFinancialInstitutionByClassName(String className) throws FrameworkCheckedException;

    public boolean isAmountWithinTransactionLimits(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public BaseWrapper loadTransactionCodeByCode(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public BaseWrapper generateTransactionCode();

    public SearchBaseWrapper loadAppUserByMobileNumberAndType(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    //	public List<ScheduleFundsTransferDetailModel> fetchScheduleFundsTransferDetailList(Date date) throws FrameworkCheckedException;
    public abstract AppUserModel loadAppUserModelByCustomerId(Long customerId);

    public abstract UserDeviceAccountsManager getUserDeviceAccountsManager();

    public void sendM3SMS(String mobileNo, String SMSText) throws FrameworkCheckedException, Exception;

    public void sendM3SMS(String mobileNo, String SMSText, String pin) throws FrameworkCheckedException, Exception;

    public AbstractFinancialInstitution loadAbstractFinancialInstitution(SwitchWrapper switchWrapper) throws FrameworkCheckedException;
//	public void updateScheduleFundsTransferList(List<ScheduleFundsTransferDetailModel> sftList) throws FrameworkCheckedException;

    BaseWrapper updateMfsPin(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    public BaseWrapper saveOrUpdateDescrepentAccountOpening(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    ;

    public BankModel getOlaBankMadal();


    BaseWrapper checkAppVersion(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper loadUserDeviceAccountByMobileNumber(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper getLatestAppVersion(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    SearchBaseWrapper loadTickerString(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper updateUserDeviceAccounts(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper loadDefaultTickerString() throws
            FrameworkCheckedException;

    SearchBaseWrapper getFavoriteNumbers(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper createFavoriteNumbers(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper removeFavoriteNumbers(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper loadCommand(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    SearchBaseWrapper checkLogin(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper loadAppUser(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    AgentBvsStatModel loadAgentBvsStat(AgentBvsStatModel agentBvsStatModel) throws
            FrameworkCheckedException;

    BaseWrapper loadAllPayDefaultTickerString();

    BaseWrapper loadVerifly(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    SearchBaseWrapper loadServices(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    SearchBaseWrapper loadCatalogVersion(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    List<AdsModel> findAds(AdsModel adsModel) throws FrameworkCheckedException;

    SearchBaseWrapper loadProductCatalog(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    VeriflyBaseWrapper changeVeriflyPin(VeriflyManager veriflyManager, VeriflyBaseWrapper veriflyBaseWrapper) throws
            Exception;

    VeriflyBaseWrapper verifyVeriflyPin(VeriflyManager veriflyManager, VeriflyBaseWrapper veriflyBaseWrapper) throws
            Exception;

    SwitchWrapper verifyPIN(AppUserModel appUserModel, String encryptedPin) throws FrameworkCheckedException;

    SwitchWrapper verifyPIN(AppUserModel appUserModel, String encryptedPin, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper loadSmartMoneyAccountInfo(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper setDefaultSmartMoneyAccount(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper loadDistributorContact(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper loadDistributor(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    DistributorModel loadDistributor(AppUserModel appUserModel) throws FrameworkCheckedException;

    BaseWrapper loadRetailerContact(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper loadRetailer(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper loadMfs(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    SearchBaseWrapper loadSmartMoneyAccount(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper loadSmartMoneyAccount(BaseWrapper BaseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper loadCustomer(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper loadPaymentMode(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    BaseWrapper updateSmartMoneyAccount(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    public BaseWrapper updateAppUser(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    VeriflyManager loadVeriflyManagerByAccountId(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    SearchBaseWrapper searchTransaction(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    SearchBaseWrapper fetchTransactions(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException;

    WorkFlowWrapper executeSaleCreditTransaction(WorkFlowWrapper workFlowWrapper) throws
            Exception;

    ValidationErrors checkUserCredentials(UserDeviceAccountsModel userDeviceAccountsModel);

    ValidationErrors checkUserCredentials(BaseWrapper baseWrapper) throws FrameworkCheckedException;


    ValidationErrors checkActiveAppUser(AppUserModel appUserModel) throws FrameworkCheckedException;

    ValidationErrors checkSmartMoneyAccount(BaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    ValidationErrors checkSmartMoneyAccount(SearchBaseWrapper baseWrapper) throws
            FrameworkCheckedException;

    //	SwitchWrapper checkAccountBalance(SwitchWrapper switchWrapper) throws FrameworkCheckedException;

    BaseWrapper loadProduct(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    ProductModel loadProductByProductCodeAndAppUserTypeId(String productCode, Long appUserTypeId) throws FrameworkCheckedException;

    ProductVO loadProductVO(BaseWrapper baseWrapper) throws FrameworkCheckedException;

//	CommissionWrapper calculateCommission(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

    ProductDispenser loadProductDispense(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

    WorkFlowWrapper getBillInfo(BillPaymentProductDispenser billSale, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper loadProdCatalogForDiscAndVar(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper loadProdCatalogForBillPayment(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    /*
     * Methods added by Jawwad ..
     * For loading retailers catalog
     */

    SearchBaseWrapper loadCatalogProductsForRetailers(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper loadCatalogServiceForRetailers(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper loadCatalogServiceForCustomer(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    public BaseWrapper loadOLASmartMoneyAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    // ----------------------------------------------------------------------------
    BaseWrapper loadOLAAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    BaseWrapper loadOperator(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public SearchBaseWrapper fetchSalesTransactions(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

//SMSCommand ..................


    BaseWrapper sendSMSToUser(BaseWrapper baseWrapper) throws FrameworkCheckedException;


    SearchBaseWrapper loadUserDeviceAccounts(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;


    SearchBaseWrapper loadUserDeviceAccountsListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;


    BaseWrapper updateMfsAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    AbstractFinancialInstitution loadFinancialInstitution(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    boolean checkExpiryDate(Date expiryDate);

    boolean checkTPin(BaseWrapper baseWrapper);

    boolean isAccountValidationRequired(BaseWrapper baseWrapper);

    BaseWrapper updateFavoriteNumber(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    void deleteFavoriteNumber(BaseWrapper baseWrapper);

    public BaseWrapper addFavoriteNumbers(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public SearchBaseWrapper loadCatalogServiceAndProductsForRetailers(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    public List<TransactionDetailPortalListModel> getMiniStatementTransactionList(ExtendedTransactionDetailPortalListModel model, Integer fetchSize) throws FrameworkCheckedException;

    // ------------------------------ MIni -----------------------------------------------------------------------------
    SearchBaseWrapper loadMiniUserDeviceAccountsListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    BaseWrapper loadUserDeviceAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper loadMiniCommand(SearchBaseWrapper baseWrapper) throws FrameworkCheckedException;

    BaseWrapper loadMiniCommand(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper loadSMAExactMatch(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper getAppUserPeers(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper loadMiniHelpKeyword(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper getUserRegServices(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper getUserService(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    BaseWrapper saveMiniTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    BaseWrapper updateMiniTransactionRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    Boolean isCVVReqForSMA(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    BaseWrapper modifyPINSentMiniTransToExpired(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper loadMiniTransaction(SearchBaseWrapper baseWrapper) throws FrameworkCheckedException;

    VeriflyBaseWrapper generateOneTimeVeriflyPIN(VeriflyManager veriflyManager, VeriflyBaseWrapper veriflyBaseWrapper) throws Exception;

    VeriflyBaseWrapper verifyVeriflyOneTimePin(VeriflyManager veriflyManager, VeriflyBaseWrapper veriflyBaseWrapper) throws Exception;

    BaseWrapper saveMiniCommandLogRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    SearchBaseWrapper searchAppUserByMobile(SearchBaseWrapper searchBaseWrapper);

    BaseWrapper searchProduct(BaseWrapper baseWrapper) throws FrameworkCheckedException;
    // ----------------------------------------------------------------------------------------------------------------------

    /*Added by mudassir - Account to Cash*/
    public Boolean createUpdateWalkinCustomerIfExists(String walkInCNIC, String walkInMobileNumber, String senderMobileNumber) throws FrameworkCheckedException;

    public WalkinCustomerModel getWalkinCustomerModel(WalkinCustomerModel _walkinCustomerModel) throws FrameworkCheckedException;

    public SmartMoneyAccountModel getSmartMoneyAccountByWalkinCustomerId(SmartMoneyAccountModel smartMoneyAccountModel);

    public SearchBaseWrapper loadTransactionByTransactionCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    public SearchBaseWrapper loadTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    public Boolean titleFetch(SwitchWrapper switchWrapper) throws FrameworkCheckedException;

    Boolean titleFetch(AppUserModel appUserModel) throws FrameworkCheckedException;

    Boolean isAlreadyRegistered(String CNIC, String mobileNumber, String senderMobileNumber) throws FrameworkCheckedException;

    Boolean isCustomerAlreadyRegistered(String CNIC) throws FrameworkCheckedException;

    TransactionDetailModel saveTransactionDetailModel(TransactionDetailModel transactionDetailModel);

    TransactionModel saveTransactionModel(TransactionModel transactionModel);

    TransactionDetailMasterModel saveTransactionDetailMasterModel(TransactionDetailMasterModel transactionDetailMasterModel);

    public SwitchWrapper checkAgentBalance() throws WorkFlowException, FrameworkCheckedException, Exception;

    public BaseWrapper saveTillBalance(BaseWrapper baseWrapper) throws WorkFlowException, FrameworkCheckedException, Exception;

    public SearchBaseWrapper checkTillBalanceRequired(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    public SearchBaseWrapper searchAppUserByExample(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    public AppUserModel loadAppUserByMobileByQuery(String mobileNo) throws Exception;

    public AppUserModel loadAppUserByQuery(String mobileNo, Long appUserTypeId) throws Exception;

    public BaseWrapper loadAgentCommission(BaseWrapper baseWrapper) throws Exception;

    public WorkFlowWrapper getAccToAccInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

    public Double getCommissionRate(ProductModel productModel);

    public SearchBaseWrapper getCommissionRateData(CommissionRateModel commissionRateModel) throws FrameworkCheckedException;

    public List<MiniStatementListViewModel> getMiniStatementListViewModelList(MiniStatementListViewModel model, Integer fetchSize) throws FrameworkCheckedException;

    public boolean validateManualOTPin(String pin);

    SearchBaseWrapper loadTransactionDetailMaster(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    public Double getCommissionAmount(WorkFlowWrapper _workFlowWrapper);

    TransactionDetailModel loadTransactionDetailModel(String cnic, Long productId, Long supProcessingStatus) throws FrameworkCheckedException;

    public CommissionWrapper calculateCommission(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

    public CommissionWrapper calculateCommissionInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

    public Long getReasonType(Long productId);

    public boolean checkVelocityCondition(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public long getCustomerSegmentIdByMobileNo(String customerMobileNo) throws FrameworkCheckedException;

    void validateHRA(String mobileNo) throws CommandException;

    public void checkProductLimit(Long segmentId, Long productId, String mobileNumber, Long deviceTypeId, Double amount, ProductModel _productModel, DistributorModel _distributorModel, HandlerModel handlerModel) throws Exception;

    WorkFlowWrapper getBBToCoreAccInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException, CommandException, Exception;

    WorkFlowWrapper getCnicToCoreAccInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException, CommandException, Exception;

    public CustomerModel loadCustomer(CustomerModel _customerModel) throws FrameworkCheckedException;

    SearchBaseWrapper loadStakeHolderBankInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    SupplierBankInfoModel loadSupplierBankInfo(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    WorkFlowWrapper getAccountToCashInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException, CommandException, Exception;

    Integer updateTransactionProcessingStatus(Long transactionProcessingStatus, List<Long> transactionIds);

    public SwitchWrapper checkCustomerBalance(String customerMobileNumber, double totalAmount) throws WorkFlowException, FrameworkCheckedException, Exception;

    public SwitchWrapper checkCustomerBalanceForHra(String customerMobileNumber, double totalAmount) throws WorkFlowException, FrameworkCheckedException, Exception;

    public VeriflyBaseWrapper changePIN(VeriflyBaseWrapper wrapper) throws Exception;

    Boolean createOrUpdateWalkinCustomer(String CNIC, String mobileNumber, String senderMobileNumber) throws FrameworkCheckedException;

    public AppUserModel loadWalkinCustomerAppUserByCnic(String cnic) throws FrameworkCheckedException;

    ValidationErrors checkCustomerCredentials(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    OLAVO loadAccountInfoFromOLA(AppUserModel appUserModel, SmartMoneyAccountModel smartMoneyAccountModel) throws FrameworkCheckedException, Exception;

    AccountInfoModel getAccountInfoModel(Long customerId, Long paymentModeId) throws Exception;

    int updateAccountInfoModel(Long customerId, Long paymentModeId, Long isMigrated) throws Exception;

    public String fetchAccountTitle(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

    public AppUserModel checkAppUserTypeAsWalkinCustoemr(String mobileNo) throws FrameworkCheckedException;

    BaseWrapper loadHandler(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public void loadHandlerData(Long handlerId, WorkFlowWrapper wrapper) throws FrameworkCheckedException;

    AppUserModel loadAppUserByRetailerContractId(long retailerContactId) throws FrameworkCheckedException;

    public AccountInfoModel getAccountInfoModel(Long customerId, String accountNick) throws Exception;

    public AccountInfoModel loadAccountInfoModel(AccountInfoModel accountInfoModel) throws Exception;

    public void isUniqueCNICMobile(AppUserModel appUserModel, BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public abstract void blockSmartMoneyAccount(AppUserModel appUserModel) throws Exception;

    public void sendSMS(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public void novaAlertMessage(BaseWrapper baseWrapper) throws FrameworkCheckedException;


    public void initiateUserGeneratedPinIvrCall(IvrRequestDTO ivrDTO) throws FrameworkCheckedException;

    int countCustomerPendingTrxByMobile(String mobile) throws FrameworkCheckedException;

    boolean checkP2PTransactionsOnCNIC(String cnic, String mobile) throws FrameworkCheckedException;

    public CustomerModel saveOrUpdateCustomerModel(CustomerModel _customerModel) throws FrameworkCheckedException;

    public void saveOrUpdateCustomerPictureModel(List<CustomerPictureModel> list) throws FrameworkCheckedException;

    public void saveOrUpdateBlinkCustomerPictureModel(List<BlinkCustomerPictureModel> list) throws FrameworkCheckedException;

    public void saveOrUpdateBlinkCustomerPictureModel(BlinkCustomerPictureModel blinkCustomerPictureModel) throws FrameworkCheckedException;

    public RetailerContactModel saveOrUpdateRetailerContactModel(RetailerContactModel retailerContactModel) throws FrameworkCheckedException;

    public void saveRootedMobileHistory(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public ValidationErrors checkRecipientAgentCredentials(AppUserModel agentAppUserModel, Long deviceTypeId) throws FrameworkCheckedException;

    public void setDemographicsManager(DemographicsManager demographicsManager);

    public DemographicsManager getDemographicsManager();

    public AgentBvsStatManager getAgentBvsStatManager();

    public AgentLocationStatManager getAgentLocationStatManager();

    public AppManager getAppManager();

    public AppUserManager getAppUserManager();

    public MiniTransactionDAO getMiniTransactionDAO();

    public void setMiniTransactionManager(MiniTransactionManager miniTransactionManager);

    public MiniTransactionManager getMiniTransactionManager();

    public AgentTransferRuleModel findAgentTransferRule(Long deviceTypeId, Double transactionAmount, Long senderAppUserId, Long recipientAppUserId) throws FrameworkCheckedException;

    public void verifyWalkinCustomerThroughputLimits(WorkFlowWrapper workFlowWrapper, String transactionTypeId, String walkInCNIC) throws FrameworkCheckedException;

    public List<BasePersistableModel> findBasePersistableModel(BasePersistableModel persistableModel);

    public Boolean isCnicBlacklisted(String cnicNo);

    public CustomerPictureDAO getCustomerPictureDAO();

    public AccountManager getAccountManager();

    public void setAccountManager(AccountManager accountManager);

    public WorkFlowWrapper makeOTPGeneration(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

    public WorkFlowWrapper makeOTPValidation(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

    UserDeviceAccountsModel loadUserDeviceAccountByUserId(String userId) throws FrameworkCheckedException;

    public AppUserModel loadRetailerAppUserModelByAppUserId(Long appUserId) throws FrameworkCheckedException;

    public MiniTransactionModel loadMiniTransactionByTransactionCode(String transactionCode) throws FrameworkCheckedException;

    public Boolean createNewWalkinCustomer(String CNIC, String mobileNumber, String senderMobileNumber) throws FrameworkCheckedException;

    public FonePayManager getFonePayManager();

    public AgentSegmentRestrictionManager getAgentSegmentRestriction();

    public void updateOpenCustomerL0Request(WebServiceVO webServiceVO, AppUserModel appUsermodel, ArrayList<CustomerPictureModel> arrayCustomerPictures, boolean isConsumerApp) throws FrameworkCheckedException;

    public SmsSender getSmsSender();

    FaqManager getFaqManager();

    public void makeUserBlocked(AppUserModel appUserModel) throws Exception;

    public VerisysDataDAO getVerisysDataHibernateDAO();

    public VirtualCardHibernateDAO getVirtualCardHibernateDAO();

    public UserDeviceAccountListViewManager getUserDeviceAccountListViewManager();

    void makeIVRCallForPinRegenerate(IvrRequestDTO ivrRequestDTO) throws FrameworkCheckedException;

    public SwitchWrapper makeI8SBCall(SwitchWrapper switchWrapper) throws FrameworkCheckedException;

    public boolean validateChallanParams(String consumerNumber, String productCode) throws CommandException;
	public SmartMoneyAccountModel getInActiveSMA(AppUserModel appUserModel,Long paymentModeId) throws CommandException;
    public int getCustomerChallanCount(String mobileNo) throws FrameworkCheckedException;
	public boolean removeCustomerDormancy(AppUserModel appUserModel, SmartMoneyAccountModel smartMoneyAccountModel) throws  CommandException;
    public long getPaidChallan(String consumerNo, String productId) throws FrameworkCheckedException;

    public boolean getChallanStatus(String consumerNo, String productCode) throws FrameworkCheckedException;

    public void throwsChallanException(Long paidCount) throws FrameworkCheckedException;

    public void addChallanToQueue(String consumerNo, String productCode) throws FrameworkCheckedException;

    public void removeChallanFromQueue(String consumerNo, String productCode) throws FrameworkCheckedException;

    public BaseWrapper saveOrUpdateAccountOpeningHRARequest(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public void saveOrUpdateCustomerAccountL0ToL1(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public CustomerModel getCustomerModelById(Long customerId) throws CommandException;

    public CustomerModel getCustomerModelByMobileNumber(String mobileNo) throws CommandException;

    public SmartMoneyAccountModel getSmartMoneyAccountByCustomerIdAndPaymentModeId(SmartMoneyAccountModel smartMoneyAccountModel) throws CommandException;

    public AppUserModel getAppUserModelByCNIC(String cnic) throws CommandException;

    public SmartMoneyAccountModel getSmartMoneyAccountByAppUserModelAndPaymentModId(AppUserModel appUserModel, Long paymentModeId) throws CommandException;

    OlaCustomerAccountTypeModel loadCustomerAccountTypeModelById(Long customerAccountTypeId) throws CommandException;

    public AppUserModel loadAppUserByCNICAndAccountType(String cnic, Long[] appUserTypes) throws FrameworkCheckedException;
    public AppUserModel loadAppUserByCNIC(String cnic) throws FrameworkCheckedException;

    List getCustomerDetails(AppUserModel appUserModel) throws FrameworkCheckedException;
    List getAllBlinkData(BlinkCustomerModel blinkCustomerModel) throws FrameworkCheckedException;
    List getAllCustomerData(CustomerModel customerModel) throws FrameworkCheckedException;
    BlinkCustomerModel getAllBlinkDataByBlinkID(Long blinkId) throws FrameworkCheckedException;
    BlinkCustomerModel saveAllBlinkData(BlinkCustomerModel blinkCustomerModel) throws FrameworkCheckedException;

    CustomerModel getAllCustomerDataByCustomerID(Long customerId) throws FrameworkCheckedException;

    AccountModel getAccountModelByCnicAndCustomerAccountTypeAndStatusId(String cnic, Long customerAccountTypeId, Long statusId);

    Double getDailyConsumedBalance(Long accountId, Long transactionTypeId, Date date, Long handlerId);

    Double getDailyConsumedBalanceForIBFT(Long accountId, Long transactionTypeId, Date date, Long handlerId);

    Double getDailyConsumedBalanceForAgentIBFT(Long accountId, Long transactionTypeId, Date date, Long handlerId);

    Double getConsumedBalanceByDateRange(Long accountId, Long transactionTypeId, Date date, Date end);

    Double getConsumedBalanceByDateRangeForIBFT(Long accountId, Long transactionTypeId, Date startDate, Date endDate);

    Double getConsumedBalanceByDateRangeForAgentIBFT(Long accountId, Long transactionTypeId, Date startDate, Date endDate);

    List<LimitModel> getLimitsByCustomerAccountType(Long customerAccountTypeId) throws FrameworkCheckedException;

    public ApiCityDAO getApiCityDAO();

    public void intimateAppInSnapForSendMoneyRequiresNewTransaction(WorkFlowWrapper workFlowWrapper);

    String getAccountBalance(AccountInfoModel accountInfoModel, SmartMoneyAccountModel smartMoneyAccountModel) throws Exception;

    //Debit Card Module
    public DebitCardModelDAO getDebitCardModelDao();

    public DebitCardMailingAddressDAO getDebitCardMailingAddressDAO();

    public ActionAuthorizationModelDAO getActionAuthorizationModelDAO();

    public ActionAuthorizationFacade getActionAuthorizationFacade();

//    public BaseWrapper saveOrUpdateDebitCardIssuenceRequestWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public BaseWrapper saveOrUpdateDebitCardIssuenceRequest(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public BaseWrapper initiateBISPReversalRequestWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    Boolean checkActiveAppUserForOpenAPI(WebServiceVO webServiceVO, AppUserModel appUserModel) throws Exception;

    Boolean checkActiveAppUserAndAccountAuthenticationForOpenAPI(WebServiceVO webServiceVO, AppUserModel appUserModel) throws Exception;

    Boolean checkActiveAgentAppUserForOpenAPI(WebServiceVO webServiceVO, AppUserModel appUserModel) throws Exception;

    //Agent Network and SCO
    MnoUserDAO getMnoUserDAO();

    UsecaseDAO getUseCaseDAO();

    //HRA/Debit-Card Phase-2
    PayMtncRequestDAO getPayMtncRequestDao();

    RemitanceInfoDAO getRemitanceInfoDao();

    CardConfigurationManager getCardConfigurationManager();

    CityDAO getCityDAO();

    CustomerAddressesDAO getCustomerAddressesDao();

    ESBAdapter getEsbAdapter();

    WorkFlowWrapper calculateDebitCardFee(String mobileNo, String cNic, AppUserModel appUserModel, CustomerModel customerModel, ProductModel productModel, Long ProductId, Long cardFeeTypeId, Long deviceTypeId, DebitCardModel cardModel) throws FrameworkCheckedException;

    WorkFlowWrapper calculateCardFee(String mobileNo, String cNic, AppUserModel appUserModel, CustomerModel customerModel, ProductModel productModel, Long productId, Long cardFeeTypeId, Long deviceTypeId, DebitCardModel cardModel) throws FrameworkCheckedException;

    WorkFlowWrapper calculateDebitCardFeeForAPI(String mobileNo, String cNic, AppUserModel appUserModel, CustomerModel customerModel, ProductModel productModel, Long ProductId,
                                                Long cardFeeTypeId, Long cardProductCode, Long deviceTypeId, DebitCardModel cardModel) throws FrameworkCheckedException;

    public TaxRegimeDAO getTaxRegimeDAO();

    TransactionPurposeDAO getTransactionPurposeDao();

    GeoLocationDAO getGeoLocationDao();

    BISPCustNadraVerificationDAO getBispCustNadraVerificationDAO();

    RetailerContactDAO getRetailerContactDao();

    void updateSmartMoneyAccountCardType(SmartMoneyAccountModel smartMoneyAccountModel) throws FrameworkCheckedException;

    String saveOrUpdateBVSEntryRequiresNewTransaction(UserDeviceAccountsModel userDeviceAccountsModel, AppUserModel appUserModel,
                                                      String cNic, SwitchWrapper sWrapper, String transactionCode) throws FrameworkCheckedException;

    FetchThirdPartySegmentsDAO getFetchThirdPartySegmentsDao();

    FetchCardTypeDAO getCardTypeDao();

    SegmentDAO getSegmentDao();

    OlaCustomerAccountTypeDao getOlaCustomerAccountTypeDao();

    ThirdPartyAcOpeningDAO getThirdPartyAcOpeningDao();

    MemberBankDAO getMemberBankDao();

    BankSegmentsDAO getBankSegmentsDao();

    ActionLogDAO getActionLogDao();

    RetailerDAO getRetailerDao();

    WalletSafRepoDAO getWalletSafRepoDAO();

    DebitCardPendingSafRepo debitCardPendingSafRepo(DebitCardPendingSafRepo debitCardPendingSafRepo);

    ClsPendingAccountOpeningModel clsPendingAccountOpening(ClsPendingAccountOpeningModel clsPendingAccountOpeningModel);

    AdvanceSalaryLoanModel saveOrUpdateAdvanceSalaryLoan(AdvanceSalaryLoanModel adavceSalaryLoanModel);

    JSLoansModel saveOrUpdateJSLoansModel(JSLoansModel jsLoansModel);

    BlinkCustomerModel createBlinkCustomerModel(BlinkCustomerModel blinkCustomerModel);

    ClsPendingBlinkCustomerModel createClsPendingBlinkCustomerModel(ClsPendingBlinkCustomerModel blinkCustomerModel);


    AccountOpeningPendingSafRepoModel getAccountOpeningPendingSafRepoModel(AccountOpeningPendingSafRepoModel accountOpeningPendingSafRepoModel);

    public DebitCardPendingSafRepo loadExistingDebitCardSafRepo(DebitCardPendingSafRepo debitCardPendingSafRepo) throws FrameworkCheckedException;

    public DebitCardPendingSafRepo loadDebitCardSafRepo(DebitCardPendingSafRepo debitCardPendingSafRepo) throws FrameworkCheckedException;

    void debitCardReissuance(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    //Added by Abubakar
    SwitchWrapper validateBalance(AppUserModel appUserModel, SmartMoneyAccountModel smaModel, Double transactionAmount, boolean checkDebitBlock) throws Exception;

    public abstract SwitchWrapper validateBalance(AppUserModel appUserModel, SmartMoneyAccountModel smaModel, Double transactionAmount) throws WorkFlowException, FrameworkCheckedException, Exception;

    SmartMoneyAccountModel loadSmartMoneyAccountModel(AppUserModel appUserModel) throws FrameworkCheckedException;

    SwitchWrapper checkBalance(AppUserModel appUserModel, SmartMoneyAccountModel smaModel) throws FrameworkCheckedException;

    AdvanceSalaryLoanDAO getAdvanceSalaryLoanDAO();
    JSLoansDAO getJSLoansDAO();

    public DebitCardRequestsViewModel getDebitCardRequestsViewModelByAppUserId(Long appUserId, String mobileNo) throws CommandException;
    public DebitCardRequestsViewModel getDebitCardRequestsViewModelByDebitCardId(Long debitCardId) throws CommandException;

    public BlinkCustomerPictureModel getBlinkCustomerPictureByTypeId(Long pictureTypeId, Long customerId) throws FrameworkCheckedException;

    List<ClsDebitCreditBlockModel> loadClsDebitCreditModel() throws FrameworkCheckedException;

    public abstract OfflineBillersConfigModel loadOfflineBillersModelByProductId(String productId);

    TasdeeqDataModel saveOrUpdateTasdeeqDataModel(TasdeeqDataModel tasdeeqDataModel);

    public TasdeeqDataModel loadTasdeeqDataModelByMobile(String mobileNo) throws FrameworkCheckedException;

    public String verifyDailyLimitForCredit(Date transactionDateTime, Double amountToAdd, Long accountId, Long customerAccountTypeId, Long handlerId)
            throws FrameworkCheckedException;

    public String verifyMonthlyLimitForCredit(Date transactionDateTime, Double amountToAdd, Long accountId, Long customerAccountTypeId, Long handlerId)
            throws FrameworkCheckedException;
}