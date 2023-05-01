package com.inov8.microbank.server.service.mfsmodule;


import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.jdbc.OracleSequenceGeneratorJdbcDAO;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.framework.server.dao.framework.v2.support.CriteriaConfiguration;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.app.service.AppManager;
import com.inov8.microbank.authenticationmethod.service.OTPAuthentication;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.cardconfiguration.model.CardFeeRuleModel;
import com.inov8.microbank.cardconfiguration.service.CardConfigurationManager;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.exception.WorkFlowExceptionTranslator;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.appversionmodule.AppVersionListViewModel;
import com.inov8.microbank.common.model.customermodule.BlinkCustomerPictureModel;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.customermodule.MerchantAccountPictureModel;
import com.inov8.microbank.common.model.favoritenumbermodule.FavoriteNumberListViewModel;
import com.inov8.microbank.common.model.messagemodule.NovaAlertMessage;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.ExtendedTransactionDetailPortalListModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.MiniStatementListViewModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.model.productmodule.*;
import com.inov8.microbank.common.model.smartmoneymodule.SmAcctInfoListViewModel;
import com.inov8.microbank.common.model.transactionmodule.FetchTransactionListViewModel;
import com.inov8.microbank.common.model.transactionmodule.SalesSummaryListViewModel;
import com.inov8.microbank.common.model.userdeviceaccountmodule.UserDeviceAccountListViewModel;
import com.inov8.microbank.common.model.velocitymodule.VelocityRuleViewModel;
import com.inov8.microbank.common.model.veriflymodule.VeriflyListViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.transactionreversal.TransactionReversalVo;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.debitcard.dao.DebitCardMailingAddressDAO;
import com.inov8.microbank.debitcard.dao.DebitCardModelDAO;
import com.inov8.microbank.debitcard.dao.DebitCardRequestsViewModelDAO;
import com.inov8.microbank.debitcard.model.DebitCardMailingAddressModel;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.debitcard.model.DebitCardRequestsViewModel;
import com.inov8.microbank.debitcard.service.DebitCardManager;
import com.inov8.microbank.debitcard.vo.DebitCardVO;
import com.inov8.microbank.demographics.service.DemographicsManager;
import com.inov8.microbank.faq.service.FaqManager;
import com.inov8.microbank.fonepay.common.FonePayMessageVO;
import com.inov8.microbank.fonepay.common.FonePayResponseCodes;
import com.inov8.microbank.fonepay.common.FonePayUtils;
import com.inov8.microbank.fonepay.dao.hibernate.VirtualCardHibernateDAO;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.fonepay.vo.NADRADataVO;
import com.inov8.microbank.hra.airtimetopup.dao.RemitanceInfoDAO;
import com.inov8.microbank.hra.paymtnc.dao.PayMtncRequestDAO;
import com.inov8.microbank.nadraVerisys.dao.VerisysDataDAO;
import com.inov8.microbank.nadraVerisys.model.VerisysDataModel;
import com.inov8.microbank.otp.service.MiniTransactionManager;
import com.inov8.microbank.server.dao.actionlogmodule.ActionLogDAO;
import com.inov8.microbank.server.dao.addressmodule.AddressDAO;
import com.inov8.microbank.server.dao.addressmodule.CustomerAddressesDAO;
import com.inov8.microbank.server.dao.agentgroup.AgentTransferRuleDAO;
import com.inov8.microbank.server.dao.bankmodule.BankDAO;
import com.inov8.microbank.server.dao.bankmodule.BankSegmentsDAO;
import com.inov8.microbank.server.dao.bankmodule.MemberBankDAO;
import com.inov8.microbank.server.dao.bispcustnadraverification.BISPCustNadraVerificationDAO;
import com.inov8.microbank.server.dao.commandmodule.MiniCommandKeywordDAO;
import com.inov8.microbank.server.dao.commandmodule.MiniCommandLogDAO;
import com.inov8.microbank.server.dao.commandmodule.MiniHelpKeywordDAO;
import com.inov8.microbank.server.dao.commissionmodule.CommissionRateDAO;
import com.inov8.microbank.server.dao.customermodule.*;
import com.inov8.microbank.server.dao.debitCardChargesmodule.DebitCardChargesDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorContactDAO;
import com.inov8.microbank.server.dao.distributormodule.DistributorDAO;
import com.inov8.microbank.server.dao.favoritenumbermodule.FavoriteNumberListViewDAO;
import com.inov8.microbank.server.dao.fetchcardtype.FetchCardTypeDAO;
import com.inov8.microbank.server.dao.geolocationmodule.GeoLocationDAO;
import com.inov8.microbank.server.dao.handlermodule.HandlerDAO;
import com.inov8.microbank.server.dao.jsloansmodule.JSLoansDAO;
import com.inov8.microbank.server.dao.mfsmodule.*;
import com.inov8.microbank.server.dao.mnomodule.MnoUserDAO;
import com.inov8.microbank.server.dao.operatinghoursmodule.OperatingHoursRuleModelDAO;
import com.inov8.microbank.server.dao.operatormodule.OperatorDAO;
import com.inov8.microbank.server.dao.portal.apicitymodule.ApiCityDAO;
import com.inov8.microbank.server.dao.portal.authorizationmodule.ActionAuthorizationModelDAO;
import com.inov8.microbank.server.dao.portal.citymodule.CityDAO;
import com.inov8.microbank.server.dao.portal.linkpaymentmodemodule.LinkPaymentModeDAO;
import com.inov8.microbank.server.dao.portal.mfsaccountmodule.GoldenNosDAO;
import com.inov8.microbank.server.dao.portal.ola.OlaCustomerAccountTypeDao;
import com.inov8.microbank.server.dao.portal.taxregimemodule.TaxRegimeDAO;
import com.inov8.microbank.server.dao.productmodule.*;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerDAO;
import com.inov8.microbank.server.dao.safrepo.WalletSafRepoDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.dao.thirdpartcashoutmodule.FetchThirdPartySegmentsDAO;
import com.inov8.microbank.server.dao.thirdpartcashoutmodule.ThirdPartyAcOpeningDAO;
import com.inov8.microbank.server.dao.transactiondetailinfomodule.AllPayTransactionInfoListViewDAO;
import com.inov8.microbank.server.dao.transactiondetailinfomodule.MiniStatementListViewDAO;
import com.inov8.microbank.server.dao.transactionmodule.*;
import com.inov8.microbank.server.dao.usecasemodule.UsecaseDAO;
import com.inov8.microbank.server.dao.userdeviceaccount.UserDeviceAccountListViewDAO;
import com.inov8.microbank.server.dao.velocitymodule.VelocityRuleViewDAO;
import com.inov8.microbank.server.facade.CreditAccountQueingPreProcessor;
import com.inov8.microbank.server.facade.ThirdPartyCashOutQueingPreProcessor;
import com.inov8.microbank.server.facade.WorkFlowFacade;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import com.inov8.microbank.server.service.agentbvsstate.AgentBvsStatManager;
import com.inov8.microbank.server.service.agentbvsstate.dao.AgentBvsStatDAO;
import com.inov8.microbank.server.service.agentlocationstat.AgentLocationStatManager;
import com.inov8.microbank.server.service.bulkdisbursements.CustomerPendingTrxManager;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsDebitCreditDAO;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.fileloader.ArbitraryResourceLoader;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.dispenser.ProductDispenser;
import com.inov8.microbank.server.service.integration.vo.AccountToAccountVO;
import com.inov8.microbank.server.service.integration.vo.BBToCoreVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.pendingaccountopeningmodule.dao.PendingAccountOpeningDAO;
import com.inov8.microbank.server.service.pendingaccountopeningmodule.dao.PendingDebitCardSafRepoDAO;
import com.inov8.microbank.server.service.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionManager;
import com.inov8.microbank.server.service.portal.authorizationmodule.ActionAuthorizationManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.microbank.server.service.tillbalancemodule.TillBalanceManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import com.inov8.microbank.tax.dao.OfflineBillersConfigDAO;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.dao.ledger.LedgerDAO;
import com.inov8.ola.server.service.account.AccountManager;
import com.inov8.ola.server.service.limit.LimitManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.LimitTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;
import com.inov8.verifly.common.constants.CardTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.dao.mainmodule.AccountInfoDAO;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.criterion.MatchMode;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.web.context.ContextLoader;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.inov8.microbank.common.model.schedulemodule.ScheduleFundsTransferDetailModel;
//import com.inov8.microbank.common.model.schedulemodule.ScheduleLoanPaymentModel;
//import com.inov8.microbank.server.dao.schedulemodule.ScheduleFundsTransferDetailDao;
//import com.inov8.microbank.server.dao.schedulemodule.ScheduleBillPaymentDao;
//import com.sun.xml.registry.common.tools.bindings_v3.Command;


public class CommonCommandManagerImpl implements CommonCommandManager {

    protected final Log logger = LogFactory.getLog(CommonCommandManagerImpl.class);
    FinancialInstitution olaVeriflyFinancialInstitution;
    private MemberBankDAO memberBankDAO;
    private BankSegmentsDAO bankSegmentsDAO;
    private ThirdPartyAcOpeningDAO thirdPartyAcOpeningDAO;
    private FetchThirdPartySegmentsDAO fetchThirdPartySegmentsDAO;
    private FetchCardTypeDAO fetchCardTypeDAO;
    private SegmentDAO segmentDAO;
    private ThirdPartyCashOutQueingPreProcessor thirdPartyCashOutQueingPreProcessor;
    private TransactionPurposeDAO transactionPurposeDAO;
    private MessageSource messageSource;
    private WorkFlowFacade workFlowFacade;
    private UserDeviceAccountsDAO userDeviceAccountsDAO;
    private AppVersionDAO appVersionDAO;
    private TickerDAO tickerDAO;
    private FavoriteNumbersDAO favoriteNumbersDAO;
    private CommandDAO commandDAO;
    private AppUserDAO appUserDAO;
    private AppUserManager appUserManager;
    private VeriflyListViewDAO veriflyListViewDAO;
    private CatalogServiceListViewDAO catalogServiceListViewDAO;
    private GenericDao genericDao;
    private ProdCatalogDetailListViewDAO prodCatalogDetailListViewDAO;
    private VeriflyManagerService veriflyController;
    private SMAcctInfoListViewDAO smAcctInfoListViewDAO;
    private SmartMoneyAccountDAO smartMoneyAccountDAO;
    private AppVersionListViewDAO appVersionListViewDAO;
    private DistributorContactDAO distributorContactDAO;
    private RetailerContactDAO retailerContactDAO;
    private FetchTransactionListViewDAO fetchTransactionListViewDAO;
    private CustomerDAO customerDAO;
    private AccountInfoDAO accountInfoDAO;
    private DebitCardChargesDAO debitCardChargesDAO;
    private RetailerDAO retailerDAO;
    private DistributorDAO distributorDAO;
    private SwitchController switchController;
    private ProductDAO productDAO;
    private ProductDispenseController productDispenseController;
    private CommissionManager commissionManager;
    private PaymentModeDAO paymentModeDAO;
    private WorkFlowExceptionTranslator workflowExceptionTranslator;
    private OperatorDAO operatorDAO;
    private MfsAccountManager mfsAccountManager;
    private SmsSender smsSender;
    private UserDeviceAccountListViewDAO userDeviceAccountListViewDAO;
    private FavoriteNumberListViewDAO favoriteNumberListViewDAO;
    private FinancialIntegrationManager financialIntegrationManager;
    private SalesSummaryListViewDAO salesSummaryListViewDAO;
    private MiniCommandKeywordDAO miniCommandKeywordDAO;
    private MiniHelpKeywordDAO miniHelpKeywordDAO;
    private MiniTransactionDAO miniTransactionDAO;
    private TransactionModuleManager transactionManager;
    private TransactionDAO transactionDAO;
    private TransactionDetailDAO transactionDetailDAO;
    private AllPayTransactionInfoListViewDAO allPayTransactionInfoListViewDAO;
    private AbstractFinancialInstitution phoenixFinancialInstitution;
    private TillBalanceManager tillBalanceManager;
    private VeriflyManager veriflyManager;
    private CommissionRateDAO commissionRateDAO;
    private MiniStatementListViewDAO miniStatementListViewDAO;
    private TransactionDetailMasterDAO transactionDetailMasterDAO;
    private ProductManager productManager;
    private OperatingHoursRuleModelDAO operatingHoursRuleModelDAO;
    private VelocityRuleViewDAO velocityRuleViewDAO;
    private CategoryDAO categoryDAO;
    private RegistrationStateDAO registrationStateDAO;
    private BlinkCustomerRegistrationStateDAO blinkCustomerRegistrationStateDAO;
    private StakeholderBankInfoManager stakeholderBankInfoManager;
    private SupplierBankInfoManager supplierBankInfoManager;
    private ActionLogManager actionLogManager;
    private OracleSequenceGeneratorJdbcDAO sequenceGenerator;
    private OracleSequenceGeneratorJdbcDAO deviceApplicationNoGenerator;
    private ArbitraryResourceLoader arbitraryResourceLoader;
    private GoldenNosDAO goldenNosDAO;
    private CustomerPictureDAO customerPictureDAO;
    private BlinkCustomerModelDAO blinkCustomerModelDAO;
    private MerchantAccountModelDAO merchantAccountModelDAO;
    private BlinkCustomerPictureDAO blinkCustomerPictureDAO;
    private MerchantAccountPictureDAO merchantAccountPictureDAO;
    private ClsDebitCreditDAO clsDebitCreditDAO;
    private OfflineBillersConfigDAO offlineBillersConfigDAO;
    private BankDAO bankDAO;
    private LinkPaymentModeDAO linkPaymentModeDAO;
    private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;
    private AccountManager accountManager;
    private CustomerPendingTrxManager customerPendingTrxManager;
    private HandlerDAO handlerDAO;
    private FaqManager faqManager;
    private SmartMoneyAccountManager smartMoneyAccountManager;
    private IvrAuthenticationRequestQueue ivrAuthenticationRequestQueueSender;
    private DemographicsManager demographicsManager;
    private AppManager appManager;
    private MiniTransactionManager miniTransactionManager;
    private AgentTransferRuleDAO agentTransferRuleDAO;
    private OTPAuthentication otpAuthentication;
    private AccountControlManager accountControlManager;
    private AddressDAO addressDAO;
    private CustomerAddressesDAO customerAddressesDAO;
    private FonePayManager fonePayManager;
    private AgentSegmentRestrictionManager agentSegmentRestrictionManager;
    private VerisysDataDAO verisysDataHibernateDAO;
    private VirtualCardHibernateDAO virtualCardHibernateDAO;
    private CustTransManager custTransManager;
    private UserDeviceAccountListViewManager userDeviceAccountListViewManager;
    private ESBAdapter esbAdapter;
    private OlaCustomerAccountTypeDao olaCustomerAccountTypeDao;
    private CustomerDetailsCommandDAO customerDetailsCommandDAO;
    private ApiCityDAO apiCityDAO;
    private CityDAO cityDAO;
    private TaxRegimeDAO taxRegimeDAO;
    private BillStatusDAO billStatusDAO;
    //Debit Card
    private DebitCardModelDAO debitCardModelDAO;
    private DebitCardMailingAddressDAO debitCardMailingAddressDAO;
    private ActionAuthorizationModelDAO actionAuthorizationModelDAO;
    private ActionAuthorizationFacade actionAuthorizationFacade;
    //Agent Network and SCO
    private MnoUserDAO mnoUserDAO;
    private UsecaseDAO usecaseDAO;
    //HRA/Debit-Card Phase-2
    private CardConfigurationManager cardConfigurationManager;
    private PayMtncRequestDAO payMtncRequestDAO;
    private RemitanceInfoDAO remitanceInfoDAO;
    private GeoLocationDAO geoLocationDAO;
    private BISPCustNadraVerificationDAO bispCustNadraVerificationDAO;
    private ActionLogDAO actionLogDAO;
    private AbstractFinancialInstitution abstractFinancialInstitution;
    //	private ScheduleFundsTransferDetailDao scheduleFundsTransferDetailDao;
    private DebitCardManager debitCardManager;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private WalletSafRepoDAO walletSafRepoDAO;
    private PendingDebitCardSafRepoDAO pendingDebitCardSafRepoDAO;
    private PendingAccountOpeningDAO pendingAccountOpeningDAO;
    private AdvanceSalaryLoanDAO advanceSalaryLoanDAO;
//    private JSLoansDAO jsLoansDAO;
    private ActionAuthorizationManager actionAuthorizationManager;
    private AgentBvsStatManager agentBvsStatManager;
    private AgentBvsStatDAO agentBvsStatDAO;
    private AgentLocationStatManager agentLocationStatManager;
    private DebitCardRequestsViewModelDAO debitCardRequestsViewModelDAO;
    private TasdeeqDataDAO tasdeeqDataDAO;
    private LimitManager limitManager;
    private LedgerDAO ledgerDAO;
    private MiniCommandLogDAO miniCommandLogDAO;
    private int minExpiryYear;
    private int maxExpiryYear;

    public static String escapeUnicode(String input) {
        StringBuilder b = new StringBuilder(input.length());
        java.util.Formatter f = new java.util.Formatter(b);
        for (char c : input.toCharArray()) {
            if (c < 128) {
                b.append(c);
            } else {
                f.format("\\%04x", (int) c);
            }
        }
        return b.toString();
    }

    //private AgentLocationStatDAO agentLocationStatDAO;
    //	private ScheduleBillPaymentDao scheduleBillPaymentDao;
    /*private FonePayManager fonePayManager;
     */
    public BankModel getOlaBankMadal() {
        BankModel bankModel = new BankModel();
        bankModel.setFinancialIntegrationId(FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION);
        CustomList bankList = this.bankDAO.findByExample(bankModel);
        List bankL = bankList.getResultsetList();
        bankModel = (BankModel) bankL.get(0);
        return bankModel;
    }

//	@Override
//	public void updateScheduleFundsTransferList(List<ScheduleFundsTransferDetailModel> sftList) throws FrameworkCheckedException {
//
//	}

    public List<CustomerPictureModel> getDiscrepantCustomerPictures(Long customerId) throws FrameworkCheckedException {
        List<CustomerPictureModel> customerPictureModelList = customerPictureDAO.getDiscrepantCustomerPictures(customerId);
        return customerPictureModelList;
    }
    //*******************************************************************************

    public OracleSequenceGeneratorJdbcDAO getDeviceApplicationNoGenerator() {
        return deviceApplicationNoGenerator;
    }

    public void setDeviceApplicationNoGenerator(
            OracleSequenceGeneratorJdbcDAO deviceApplicationNoGenerator) {
        this.deviceApplicationNoGenerator = deviceApplicationNoGenerator;
    }

    public WorkFlowWrapper makeCustomerTrxByTransactionCode(String transactionId, String cnicCustomer) throws FrameworkCheckedException {
        return customerPendingTrxManager.makeCustomerTrxByTransactionCode(transactionId, cnicCustomer);
    }

    public void loadAndForwardAccountToQueue(String transactionId) throws Exception {
        creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(transactionId);
    }

    @Override
    public AbstractFinancialInstitution loadAbstractFinancialInstitution(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
        return this.financialIntegrationManager.loadFinancialInstitution(switchWrapper);
    }

    // Discrepant Save or Update Method
    //*******************************************************************************
    public BaseWrapper saveOrUpdateDescrepentAccountOpening(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        Date nowDate = new Date();
        AddressModel finalAddress = new AddressModel();
        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        Boolean isBvsAccount = false;
        if (null != baseWrapper.getObject("isBvsAccount")) {
            isBvsAccount = (Boolean) baseWrapper.getObject("isBvsAccount");
        }
        AppUserModel discrepantAppUserModel = (AppUserModel) baseWrapper.getObject(CommandFieldConstants.KEY_APP_USER_MODEL);
        Long oldRegistrationStateId = discrepantAppUserModel.getRegistrationStateId();

        //Saving AppUser
        if (isBvsAccount == true) {
            discrepantAppUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
            discrepantAppUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
        } else {
            discrepantAppUserModel.setRegistrationStateId(RegistrationStateConstants.REQUEST_RECEIVED);
            discrepantAppUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);
        }

        this.appUserDAO.saveOrUpdate(discrepantAppUserModel);
        baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, discrepantAppUserModel);

        CustomerModel discrepantCustomerModel = new CustomerModel();
        Long customerId = discrepantAppUserModel.getCustomerId();
        //discrepantCustomerModel.setIsCnicSeen();

        discrepantCustomerModel = (CustomerModel) baseWrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MODEL);
        //Saving Customer
        customerDAO.saveOrUpdate(discrepantCustomerModel);
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, discrepantCustomerModel);

        //Setting values in UserDeviceAccount

        userDeviceAccountsModel = this.userDeviceAccountsDAO.loadUserDeviceAccountByUserId(discrepantAppUserModel.getUsername());
        if (isBvsAccount == true) {
            userDeviceAccountsModel.setAccountEnabled(true); // In case of BVS customer should be created in activated state
        } else {
            userDeviceAccountsModel.setAccountEnabled(false); // by default customer should be created in de-activated state
        }
        if (userDeviceAccountsModel != null) {
            userDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            userDeviceAccountsModel.setUpdatedOn(nowDate);
            userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
            userDeviceAccountsModel.setUserId(discrepantAppUserModel.getUsername());
        }
        baseWrapper.putObject(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNT_MODEL, userDeviceAccountsModel);
        //Saving UserDeviceAccounts
        UserDeviceAccountsModel userDeviceAccountModel = (UserDeviceAccountsModel) baseWrapper.getObject(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNT_MODEL);
        userDeviceAccountModel.setAppUserId(discrepantAppUserModel.getAppUserId());
        this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountModel);
        baseWrapper.putObject(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNT_MODEL, userDeviceAccountModel);

        //Setting and saving OLAVO
        OLAVO olaVo = new OLAVO();
        olaVo = (OLAVO) baseWrapper.getObject(CommandFieldConstants.KEY_ONLINE_ACCOUNT_MODEL);
        if (isBvsAccount == true) {
            olaVo.setCustomerAccountTypeId(2L);
        } else {
            olaVo.setCustomerAccountTypeId(1L);
        }
        olaVo.setCnic(discrepantAppUserModel.getNic());
        olaVo.setMobileNumber(discrepantAppUserModel.getMobileNo());

        SwitchWrapper sWrapper = new SwitchWrapperImpl();
        BankModel bankModel = getOlaBankMadal();
        sWrapper.setOlavo(olaVo);
        sWrapper.setBankId(bankModel.getBankId());
        //Saving OLAVO
        try {
            sWrapper = olaVeriflyFinancialInstitution.changeAccountDetails(sWrapper);

            //*********************************************************************
            Long newRegistrationStateId = discrepantAppUserModel.getRegistrationStateId();
            String transactionIDForSAF = "";
            //Settle Account Opening commission - if registration state is updated to VERIFIED
            if ((newRegistrationStateId != null && newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.VERIFIED)
                    && (oldRegistrationStateId == null || oldRegistrationStateId.longValue() != RegistrationStateConstantsInterface.VERIFIED)) {
                transactionIDForSAF = this.mfsAccountManager.settleAccountOpeningCommission(discrepantAppUserModel.getCustomerId());
            }


            if (null != baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR)) {
                AddressModel customerPresentAddressModel = (AddressModel) baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR);
                CustomerAddressesModel customerPresentAddressesModel = new CustomerAddressesModel();
                customerPresentAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                customerPresentAddressesModel.setAddressTypeId(AddressTypeConstants.PRESENT_HOME_ADDRESS);
                customerPresentAddressesModel.setCustomerId(discrepantCustomerModel.getCustomerId());
                AddressModel cAddress = customerAddressesDAO.findByExample(customerPresentAddressesModel).getResultsetList().get(0).getAddressIdAddressModel();
                cAddress.setHouseNo(customerPresentAddressModel.getHouseNo());
                cAddress.setFullAddress(customerPresentAddressModel.getFullAddress());
                addressDAO.saveOrUpdate(cAddress);
                finalAddress = customerPresentAddressModel;


				 /*customerPresentAddressesModel.setAddressId(customerPresentAddressModel.getAddressId());
				 customerAddressesDAO.saveOrUpdate(customerPresentAddressesModel);*/
            }

            if (null != baseWrapper.getObject(CommandFieldConstants.KEY_PERMANENT_ADDR)) {
                AddressModel customerPermanentAddressModel = (AddressModel) baseWrapper.getObject(CommandFieldConstants.KEY_PERMANENT_ADDR);


                CustomerAddressesModel customerPermanentAddressesModel = new CustomerAddressesModel();
                customerPermanentAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                customerPermanentAddressesModel.setAddressTypeId(AddressTypeConstants.PERMANENT_HOME_ADDRESS);
                customerPermanentAddressesModel.setCustomerId(discrepantCustomerModel.getCustomerId());
                customerPermanentAddressModel.setAddressId(customerAddressesDAO.findByExample(customerPermanentAddressesModel).getResultsetList().get(0).getAddressId());
                AddressModel cAddress = customerAddressesDAO.findByExample(customerPermanentAddressesModel).getResultsetList().get(0).getAddressIdAddressModel();
                cAddress.setHouseNo(customerPermanentAddressModel.getHouseNo());
                cAddress.setFullAddress(customerPermanentAddressModel.getFullAddress());
                addressDAO.saveOrUpdate(cAddress);
                finalAddress = customerPermanentAddressModel;

            }

            //*********************************************************************
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
        }

        //***********************************************************************************************************//
        //			Addresses for the time being now commented, two two records are showing on search customer screen.//
        //***********************************************************************************************************//

		 /*if(isBvsAccount == true){
	 		if(null != baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR))
    		{
    			AddressModel customerPresentAddressModel = (AddressModel) baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR);
    			addressDAO.saveOrUpdate(customerPresentAddressModel);
    			CustomerAddressesModel customerPresentAddressesModel = new CustomerAddressesModel();
    			customerPresentAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
    			customerPresentAddressesModel.setAddressTypeId(AddressTypeConstants.PRESENT_HOME_ADDRESS);
    			customerPresentAddressesModel.setCustomerId(discrepantCustomerModel.getCustomerId());
    			customerPresentAddressesModel.setAddressId(customerPresentAddressModel.getAddressId());
    			customerAddressesDAO.saveOrUpdate(customerPresentAddressesModel);
    		}
	 		if(null != baseWrapper.getObject(CommandFieldConstants.KEY_PERMANENT_ADDR))
    		{
    			AddressModel customerPermanentAddressModel = (AddressModel) baseWrapper.getObject(CommandFieldConstants.KEY_PERMANENT_ADDR);
    			addressDAO.saveOrUpdate(customerPermanentAddressModel);
    			CustomerAddressesModel customerPermanentAddressesModel = new CustomerAddressesModel();
    			customerPermanentAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
    			customerPermanentAddressesModel.setAddressTypeId(AddressTypeConstants.PERMANENT_HOME_ADDRESS);
    			customerPermanentAddressesModel.setCustomerId(discrepantCustomerModel.getCustomerId());
    			customerPermanentAddressesModel.setAddressId(customerPermanentAddressModel.getAddressId());
    			customerAddressesDAO.saveOrUpdate(customerPermanentAddressesModel);
    		}
		 }*/

        //Saving Pictures in case of L0 Request
        if (isBvsAccount == false) {
            List<CustomerPictureModel> arrayCustomerPictures = (ArrayList<CustomerPictureModel>) baseWrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_PICTURES_COLLECTION);
            List<CustomerPictureModel> customerPictureModelList = getDiscrepantCustomerPictures(customerId);

            for (CustomerPictureModel customerPictureModel : arrayCustomerPictures) {
                for (CustomerPictureModel discrepantedCustomerPictureModel : customerPictureModelList) {
                    if (customerPictureModel.getPictureTypeId().equals(discrepantedCustomerPictureModel.getPictureTypeId())) {
                        discrepantedCustomerPictureModel.setPicture(customerPictureModel.getPicture());
                        discrepantedCustomerPictureModel.setDiscrepant(Boolean.FALSE);
                        break;
                    }
                }
            }
            customerPictureDAO.saveOrUpdateCollection(customerPictureModelList);
        }

        if (isBvsAccount) {
            VerisysDataModel verisysDataModel = (VerisysDataModel) baseWrapper.getObject(CommandFieldConstants.KEY_VARISYS_DATA_MODEL);
            VerisysDataModel vo = (VerisysDataModel) baseWrapper.getObject(CommandFieldConstants.KEY_VARISYS_DATA_MODEL);
            vo.setName(CommonUtils.escapeUnicode(vo.getName()));
            vo.setMotherMaidenName(CommonUtils.escapeUnicode(vo.getMotherMaidenName()));
            vo.setCurrentAddress(CommonUtils.escapeUnicode(vo.getCurrentAddress()));
            vo.setPlaceOfBirth(CommonUtils.escapeUnicode(vo.getPlaceOfBirth()));
            vo.setCnic(vo.getCnic());
            vo.setAccountClosed(false);
            vo.setAppUserId(((AppUserModel) baseWrapper.getObject(CommandFieldConstants.KEY_APP_USER_MODEL)).getAppUserId());
            vo.setCreatedOn(new Date());
            vo.setUpdatedOn(new Date());
            vo.setTranslated(false);
            vo.setPermanentAddress(CommonUtils.escapeUnicode(vo.getPermanentAddress()));
            getVerisysDataHibernateDAO().saveNadraData(vo);
            AccountInfoModel accountInfoModel = new AccountInfoModel();
            try {
                accountInfoModel = getAccountInfoModel(discrepantCustomerModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            } catch (Exception e) {
                e.printStackTrace();
                throw new FrameworkCheckedException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG));
            }
            NADRADataVO nadraDataVO = new NADRADataVO();
            nadraDataVO.setAccountInfoId(accountInfoModel.getAccountInfoId());
            nadraDataVO.setAccountHolderId(olaVo.getAccountHolderId());
            nadraDataVO.setAddressId(finalAddress.getAddressId());
            nadraDataVO.setAppUserId(discrepantAppUserModel.getAppUserId());
            nadraDataVO.setCustomerId(discrepantCustomerModel.getCustomerId());
            nadraDataVO.setfName(CommonUtils.escapeUnicode(vo.getName()));
            nadraDataVO.setlName(CommonUtils.escapeUnicode(vo.getName()));
            nadraDataVO.setAddress(CommonUtils.escapeUnicode(vo.getCurrentAddress()));
            nadraDataVO.setMotherMaidenName(CommonUtils.escapeUnicode(vo.getMotherMaidenName()));
            nadraDataVO.setBirthPlace(CommonUtils.escapeUnicode(vo.getPlaceOfBirth()));
            try {
                this.virtualCardHibernateDAO.updateRegData(nadraDataVO);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new FrameworkCheckedException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG));

            }

        }

        return baseWrapper;
    }

    public BaseWrapper saveOrUpdateAccountOpeningL0Request(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.saveOrUpdateAccountOpeningL0Request()");
        }

        AppUserModel appUserModel = (AppUserModel) baseWrapper.getObject(CommandFieldConstants.KEY_APP_USER_MODEL);
        CustomerModel customerModel = (CustomerModel) baseWrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MODEL);
        Boolean isBvsAccount = false;
        if (null != baseWrapper.getObject("isBvsAccount")) {
            isBvsAccount = (Boolean) baseWrapper.getObject("isBvsAccount");
        }

        AppUserModel discrepantAppUserModel = null;
        CustomerModel discrepantCustomerModel = null;

        String fname = appUserModel.getFirstName();
        String lName = appUserModel.getLastName();
        String address = "";
        String birthPlace = "";
        String motherMaidenName = "";
        if (null != appUserModel.getMotherMaidenName()) {
            motherMaidenName = appUserModel.getMotherMaidenName();
        }
        if (null != customerModel.getBirthPlace()) {
            birthPlace = customerModel.getBirthPlace();
        }

        Long customerId = null;

        Long regState = appUserModel.getRegistrationStateId();

        if (regState.equals(RegistrationStateConstants.REQUEST_RECEIVED) || isBvsAccount || regState.equals(RegistrationStateConstants.VERIFIED) ||
                regState.equals(RegistrationStateConstants.CLSPENDING) || regState.equals(RegistrationStateConstants.REJECTED))

//                ||
//                regState.equals(RegistrationStateConstants.CLSPENDING))
        {
            customerDAO.saveOrUpdate(customerModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);
            appUserModel.setCustomerId(customerModel.getCustomerId());
            customerId = customerModel.getCustomerId();
            this.appUserDAO.saveOrUpdate(appUserModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_REGISTRATION_STATE_ID, appUserModel.getRegistrationStateId());


            //***************************************************************************
            //					Saving Customer Remitter

    		/*List<CustomerRemitterModel> customerRemitterModelList = new ArrayList<CustomerRemitterModel>();

            customerRemitterModelList = (List<CustomerRemitterModel>) baseWrapper.getObject(CommandFieldConstants.CUSTOMER_REMITTENCE_KEY);

    		CustomerRemitterModel custRemitterModel = new CustomerRemitterModel();
    		if(customerRemitterModelList != null){
    			custRemitterModel = customerRemitterModelList.get(0);
    		}
    		custRemitterModel.setCustomerId(customerId);
    		custRemitterModel.setRemittanceLocation(custRemitterModel.getRemittanceLocation());
    		custRemitterModel.setRelationship(custRemitterModel.getRelationship());*/

            Date nowDate = new Date();
            //CustomerModel custModel = customerModelMapper(baseWrapper, mfsAccountModel);
            List<CustomerRemitterModel> customerRemitterModelList = null;
            try {
                customerRemitterModelList = (List<CustomerRemitterModel>) baseWrapper.getObject(CommandFieldConstants.CUSTOMER_REMITTENCE_KEY);
                if ((null != customerRemitterModelList) && (customerRemitterModelList.size() > 0)) {
                    for (CustomerRemitterModel customerRemitterModel : customerRemitterModelList) {
                        customerRemitterModel.setCustomerIdCustomerModel(customerModel);
                        customerModel.addCustomerIdCustomerRemitterModel(customerRemitterModel);
                    }
                }
                //customerModel.setCustomerIdCustomerRemitterModelList(customerRemitterModelList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /**
             * Saving the CustomerModel
             */
            // baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(customerModel);
            baseWrapper = this.custTransManager.saveOrUpdate(baseWrapper);
            if (null != customerRemitterModelList && customerRemitterModelList.size() > 0) {
                this.custTransManager.saveOrUpdateCustomerRemitter(customerRemitterModelList);
            }


            //***************************************************************************


            UserDeviceAccountsModel userDeviceAccountModel = (UserDeviceAccountsModel) baseWrapper.getObject(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNT_MODEL);
            userDeviceAccountModel.setAppUserId(appUserModel.getAppUserId());
            this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNT_MODEL, userDeviceAccountModel);

            SmartMoneyAccountModel smartMoneyAccountModel = null;
            smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getObject(CommandFieldConstants.KEY_SMART_MONEY_ACCOUNT_MODEL);

            SmartMoneyAccountModel smartMoneyAccount = null;
            try {
                smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getObject(CommandFieldConstants.KEY_SMART_MONEY_ACCOUNT_MODEL);
                smartMoneyAccountModel.setCustomerId(customerModel.getCustomerId());
                this.linkPaymentModeDAO.saveOrUpdate(smartMoneyAccountModel);
                baseWrapper.putObject(CommandFieldConstants.KEY_SMART_MONEY_ACCOUNT_MODEL, smartMoneyAccountModel);
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
                    constraintName = "FN_UNIQUE_IDX";
                    if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1) {
                        throw new FrameworkCheckedException("Customer already exists");
                    }

                }
                ex.printStackTrace();
                throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
            }

            //***********************************************************************************************
            //***********************************************************************************************
            OLAVO olaVo = (OLAVO) baseWrapper.getObject(CommandFieldConstants.KEY_ONLINE_ACCOUNT_MODEL);

            SwitchWrapper sWrapper = new SwitchWrapperImpl();
            sWrapper.setOlavo(olaVo);
            sWrapper.setBankId(smartMoneyAccountModel.getBankId());

            try {
                sWrapper = olaVeriflyFinancialInstitution.createAccount(sWrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
            }

            if ("07".equals(olaVo.getResponseCode())) {
                throw new FrameworkCheckedException("NIC already exisits in the OLA accounts");
            }

            AccountInfoModel accountInfoModel = (AccountInfoModel) baseWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_INFO_MODEL);

            accountInfoModel.setCustomerId(customerModel.getCustomerId());
            accountInfoModel.setAccountNo(olaVo.getPayingAccNo().toString());

            VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

            veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
            LogModel logmodel = new LogModel();
            logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
            logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
            veriflyBaseWrapper.setLogModel(logmodel);
            veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
            veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB);

            AbstractFinancialInstitution abstractFinancialInstitution = null;
            try {
                BaseWrapper baseWrapperBank = new BaseWrapperImpl();
                BankModel bankModel = new BankModel();
                bankModel.setBankId(smartMoneyAccountModel.getBankId());
                baseWrapperBank.setBasePersistableModel(bankModel);
                abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
                veriflyBaseWrapper = abstractFinancialInstitution.generatePin(veriflyBaseWrapper);
                if (!veriflyBaseWrapper.isErrorStatus()) {
                    String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
                    throw new FrameworkCheckedException(veriflyErrorMessage);
                }
                baseWrapper.putObject(CommandFieldConstants.KEY_PIN, veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin());
            } catch (Exception e) {
                logger.error("Exception Occurred while generating pin of following customer " + customerId);
                //e.printStackTrace();
                throw new FrameworkCheckedException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG));
            }

            if (null != baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR)) {
                AddressModel customerPresentAddressModel = (AddressModel) baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR);
                addressDAO.saveOrUpdate(customerPresentAddressModel);
                CustomerAddressesModel customerPresentAddressesModel = new CustomerAddressesModel();
                customerPresentAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                customerPresentAddressesModel.setAddressTypeId(AddressTypeConstants.PRESENT_HOME_ADDRESS);
                customerPresentAddressesModel.setCustomerId(customerModel.getCustomerId());
                customerPresentAddressesModel.setAddressId(customerPresentAddressModel.getAddressId());
                customerAddressesDAO.saveOrUpdate(customerPresentAddressesModel);
            }

            if (null != baseWrapper.getObject(CommandFieldConstants.KEY_PERMANENT_ADDR)) {
                AddressModel customerPermanentAddressModel = (AddressModel) baseWrapper.getObject(CommandFieldConstants.KEY_PERMANENT_ADDR);
                addressDAO.saveOrUpdate(customerPermanentAddressModel);
                CustomerAddressesModel customerPermanentAddressesModel = new CustomerAddressesModel();
                customerPermanentAddressesModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                customerPermanentAddressesModel.setAddressTypeId(AddressTypeConstants.PERMANENT_HOME_ADDRESS);
                customerPermanentAddressesModel.setCustomerId(customerModel.getCustomerId());
                customerPermanentAddressesModel.setAddressId(customerPermanentAddressModel.getAddressId());
                customerAddressesDAO.saveOrUpdate(customerPermanentAddressesModel);
            }

//            if(regState.equals(RegistrationStateConstants.CLSPENDING)){
//                AccountOpeningPendingSafRepoModel model = new AccountOpeningPendingSafRepoModel();
//                model.setMobileNo(appUserModel.getMobileNo());
//                model.setCnic(appUserModel.getNic());
//                model.setAccountStateId(appUserModel.getAccountStateId());
//                model.setRegistrationStateId(appUserModel.getRegistrationStateId());
//                if(baseWrapper.getObject(CommandFieldConstants.KEY_CASE_STATUS).equals("True Match-Compliance") ||
//                        baseWrapper.getObject(CommandFieldConstants.KEY_CASE_STATUS).equals("Revert to Branch")) {
//                    model.setCompleted(String.valueOf(1));
//                }
//                else {
//                    model.setCompleted(String.valueOf(0));
//                }
//                model.setClsResponseCode(String.valueOf(baseWrapper.getObject(CommandFieldConstants.KEY_CASE_STATUS)));
//                model.setProductId(ProductConstantsInterface.ACCOUNT_OPENING);
//                model.setCreatedBy(ThreadLocalAppUser.getAppUserModel().getCreatedBy());
//                model.setUpdatedBy(2L);
//                model.setCreatedOn(new Date());
//                model.setUpdatedOn(new Date());
//                if(baseWrapper.getObject(CommandFieldConstants.KEY_APP_ID) != null) {
//                    if (baseWrapper.getObject(CommandFieldConstants.KEY_APP_ID).equals("2")) {
//                        model.setAppId(2L);
//                    }
//                    else{
//                        model.setAppId(1L);
//                    }
//                }
//                else{
//                    model.setAppId(14L);
//                }
//                if(baseWrapper.getObject(CommandFieldConstants.KEY_DEPOSIT_AMT) != null) {
//                    model.setInitialDeposit(String.valueOf(baseWrapper.getObject(CommandFieldConstants.KEY_DEPOSIT_AMT)));
//                }
//                else{
//                    model.setInitialDeposit("");
//                }
//                if(baseWrapper.getObject(CommandFieldConstants.KEY_AGENT_MOBILE ) != null){
//                    model.setAgentMobileNo(String.valueOf(baseWrapper.getObject(CommandFieldConstants.KEY_AGENT_MOBILE)));
//                }
//                else{
//                    model.setAgentMobileNo("");
//                }
//                model = this.genericDao.createEntity(model);
//                if(model == null){
//                    logger.info("Exception occured on saving data in AccountPendingSafRepo :");
//                }
//            }


        } else if (regState.equals(RegistrationStateConstants.DISCREPANT) || regState.equals(RegistrationStateConstants.BULK_REQUEST_RECEIVED)) {
            discrepantAppUserModel = getAppUserWithRegistrationState(appUserModel.getMobileNo(), appUserModel.getNic(), RegistrationStateConstants.BULK_REQUEST_RECEIVED);
            discrepantAppUserModel.setFirstName(appUserModel.getFirstName());
            discrepantAppUserModel.setLastName(appUserModel.getLastName());
            discrepantAppUserModel.setNicExpiryDate(appUserModel.getNicExpiryDate());
            discrepantAppUserModel.setDob(appUserModel.getDob());
            discrepantAppUserModel.setRegistrationStateId(RegistrationStateConstants.REQUEST_RECEIVED);
            this.appUserDAO.saveOrUpdate(discrepantAppUserModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, discrepantAppUserModel);

            customerId = discrepantAppUserModel.getCustomerId();
            discrepantCustomerModel = new CustomerModel();
            discrepantCustomerModel.setCustomerId(discrepantAppUserModel.getCustomerId());
            baseWrapper.setBasePersistableModel(discrepantCustomerModel);
            loadCustomer(baseWrapper);
            discrepantCustomerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
            discrepantCustomerModel.setName(customerModel.getName());
            discrepantCustomerModel.setInitialDeposit(customerModel.getInitialDeposit());
            discrepantCustomerModel.setIsCnicSeen(customerModel.getIsCnicSeen());

            customerDAO.saveOrUpdate(discrepantCustomerModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, discrepantCustomerModel);
        }

        if (isBvsAccount || regState.equals(RegistrationStateConstants.REQUEST_RECEIVED) || regState.equals(RegistrationStateConstants.BULK_REQUEST_RECEIVED) || regState.equals(RegistrationStateConstants.CLSPENDING)) {
            List<CustomerPictureModel> arrayCustomerPictures = (ArrayList<CustomerPictureModel>) baseWrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_PICTURES_COLLECTION);

            for (CustomerPictureModel customerPictureModel : arrayCustomerPictures) {
                customerPictureModel.setCustomerId(customerId);
            }
            customerPictureDAO.saveOrUpdateCollection(arrayCustomerPictures);
        } else if (regState.equals(RegistrationStateConstants.DISCREPANT)) {
            List<CustomerPictureModel> arrayCustomerPictures = (ArrayList<CustomerPictureModel>) baseWrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_PICTURES_COLLECTION);
            List<CustomerPictureModel> customerPictureModelList = getDiscrepantCustomerPictures(customerId);

            for (CustomerPictureModel customerPictureModel : arrayCustomerPictures) {
                for (CustomerPictureModel discrepantedCustomerPictureModel : customerPictureModelList) {
                    if (customerPictureModel.getPictureTypeId().equals(discrepantedCustomerPictureModel.getPictureTypeId())) {
                        discrepantedCustomerPictureModel.setPicture(customerPictureModel.getPicture());
                        discrepantedCustomerPictureModel.setDiscrepant(Boolean.FALSE);
                        break;
                    }
                }
            }
            customerPictureDAO.saveOrUpdateCollection(customerPictureModelList);
        }

        //Code to save urdu data on 62 server

        if (isBvsAccount) {
            VerisysDataModel verisysDataModel = (VerisysDataModel) baseWrapper.getObject(CommandFieldConstants.KEY_VARISYS_DATA_MODEL);
            verisysDataModel.setAppUserId(appUserModel.getAppUserId());
            try {
                this.verisysDataHibernateDAO.saveOrUpdate(verisysDataModel);
            } catch (Exception e) {
                e.printStackTrace();
                throw new FrameworkCheckedException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG));
            }

            fname = escapeUnicode(fname);
            lName = escapeUnicode(lName);

            if (null != baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR)) {
                address = ((AddressModel) baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR)).getHouseNo();
            }
            if (null != baseWrapper.getObject(CommandFieldConstants.KEY_PERMANENT_ADDR)) {
                address = ((AddressModel) baseWrapper.getObject(CommandFieldConstants.KEY_PERMANENT_ADDR)).getHouseNo();
            }
            fname = CommonUtils.escapeUnicode(fname);
            lName = CommonUtils.escapeUnicode(lName);
            motherMaidenName = CommonUtils.escapeUnicode(motherMaidenName);
            address = CommonUtils.escapeUnicode(address);
            birthPlace = CommonUtils.escapeUnicode(birthPlace);


            CustomerModel cModel = (CustomerModel) baseWrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MODEL);
            AppUserModel aModel = (AppUserModel) baseWrapper.getObject(CommandFieldConstants.KEY_APP_USER_MODEL);
            AccountInfoModel aInfoModel = (AccountInfoModel) baseWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_INFO_MODEL);
            OLAVO olaModel = (OLAVO) baseWrapper.getObject(CommandFieldConstants.KEY_ONLINE_ACCOUNT_MODEL);
            AddressModel addressModel = new AddressModel();
            if (null != baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR)) {
                addressModel = (AddressModel) baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR);
            } else {
                addressModel = (AddressModel) baseWrapper.getObject(CommandFieldConstants.KEY_PERMANENT_ADDR);
            }

            NADRADataVO nadraDataVO = new NADRADataVO();
            nadraDataVO.setAccountInfoId(aInfoModel.getAccountInfoId());
            nadraDataVO.setAccountHolderId(olaModel.getAccountHolderId());
            nadraDataVO.setAddressId(addressModel.getAddressId());
            nadraDataVO.setAppUserId(aModel.getAppUserId());
            nadraDataVO.setCustomerId(cModel.getCustomerId());
            nadraDataVO.setfName(fname);
            nadraDataVO.setlName(lName);
            nadraDataVO.setAddress(address);
            nadraDataVO.setMotherMaidenName(motherMaidenName);
            nadraDataVO.setBirthPlace(birthPlace);
            try {
                this.virtualCardHibernateDAO.updateRegData(nadraDataVO);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new FrameworkCheckedException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG));

            }

        }


        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.saveOrUpdateAccountOpeningL0Request");
        }
        return baseWrapper;
    }


    public BaseWrapper saveOrUpdateMerchantRequest(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.saveOrUpdateAccountOpeningL0Request()");
        }

        AppUserModel appUserModel = (AppUserModel) baseWrapper.getObject(CommandFieldConstants.KEY_APP_USER_MODEL);
        CustomerModel customerModel = (CustomerModel) baseWrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MODEL);

        AppUserModel discrepantAppUserModel = null;
        CustomerModel discrepantCustomerModel = null;



        Long customerId = null;

        Long regState = appUserModel.getRegistrationStateId();

        if (regState.equals(RegistrationStateConstants.REQUEST_RECEIVED) ||  regState.equals(RegistrationStateConstants.VERIFIED) ||
                regState.equals(RegistrationStateConstants.CLSPENDING) || regState.equals(RegistrationStateConstants.REJECTED))

        {
            customerDAO.saveOrUpdate(customerModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);
            appUserModel.setCustomerId(customerModel.getCustomerId());
            customerId = customerModel.getCustomerId();
            this.appUserDAO.saveOrUpdate(appUserModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_REGISTRATION_STATE_ID, appUserModel.getRegistrationStateId());


            //***************************************************************************
            //					Saving Customer Remitter


            Date nowDate = new Date();
            //CustomerModel custModel = customerModelMapper(baseWrapper, mfsAccountModel);
            List<CustomerRemitterModel> customerRemitterModelList = null;
            try {
                customerRemitterModelList = (List<CustomerRemitterModel>) baseWrapper.getObject(CommandFieldConstants.CUSTOMER_REMITTENCE_KEY);
                if ((null != customerRemitterModelList) && (customerRemitterModelList.size() > 0)) {
                    for (CustomerRemitterModel customerRemitterModel : customerRemitterModelList) {
                        customerRemitterModel.setCustomerIdCustomerModel(customerModel);
                        customerModel.addCustomerIdCustomerRemitterModel(customerRemitterModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            /**
             * Saving the CustomerModel
             */
            // baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(customerModel);
            baseWrapper = this.custTransManager.saveOrUpdate(baseWrapper);
            if (null != customerRemitterModelList && customerRemitterModelList.size() > 0) {
                this.custTransManager.saveOrUpdateCustomerRemitter(customerRemitterModelList);
            }



            //***********************************************************************************************
            //***********************************************************************************************
            OLAVO olaVo = (OLAVO) baseWrapper.getObject(CommandFieldConstants.KEY_ONLINE_ACCOUNT_MODEL);

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
                throw new FrameworkCheckedException("NIC already exisits in the OLA accounts");
            }

        } else if (regState.equals(RegistrationStateConstants.DISCREPANT) || regState.equals(RegistrationStateConstants.BULK_REQUEST_RECEIVED)) {
            discrepantAppUserModel = getAppUserWithRegistrationState(appUserModel.getMobileNo(), appUserModel.getNic(), RegistrationStateConstants.BULK_REQUEST_RECEIVED);
            discrepantAppUserModel.setFirstName(appUserModel.getFirstName());
            discrepantAppUserModel.setLastName(appUserModel.getLastName());
            discrepantAppUserModel.setNicExpiryDate(appUserModel.getNicExpiryDate());
            discrepantAppUserModel.setDob(appUserModel.getDob());
            discrepantAppUserModel.setRegistrationStateId(RegistrationStateConstants.REQUEST_RECEIVED);
            this.appUserDAO.saveOrUpdate(discrepantAppUserModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, discrepantAppUserModel);

            customerId = discrepantAppUserModel.getCustomerId();
            discrepantCustomerModel = new CustomerModel();
            discrepantCustomerModel.setCustomerId(discrepantAppUserModel.getCustomerId());
            baseWrapper.setBasePersistableModel(discrepantCustomerModel);
            loadCustomer(baseWrapper);
            discrepantCustomerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
            discrepantCustomerModel.setName(customerModel.getName());
            discrepantCustomerModel.setInitialDeposit(customerModel.getInitialDeposit());
            discrepantCustomerModel.setIsCnicSeen(customerModel.getIsCnicSeen());

            customerDAO.saveOrUpdate(discrepantCustomerModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, discrepantCustomerModel);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.saveOrUpdateMerchantAccountRequest");
        }
        return baseWrapper;
    }







    public int countByExample(BasePersistableModel basePersistableModel) {
        if (basePersistableModel instanceof AppUserModel) {
            return appUserDAO.countByExample((AppUserModel) basePersistableModel);
        } else if (basePersistableModel instanceof GoldenNosModel) {
            return goldenNosDAO.countByExample((GoldenNosModel) basePersistableModel);
        } else return 0;
    }

    public OracleSequenceGeneratorJdbcDAO getSequenceGenerator() {
        return sequenceGenerator;
    }

    public void setSequenceGenerator(
            OracleSequenceGeneratorJdbcDAO sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    public File loadImage(String path) throws IOException {
        if (this.arbitraryResourceLoader != null) {
            return this.arbitraryResourceLoader.loadImage(path);
        }
        return null;
    }

    public ActionLogModel logAction(ActionLogModel actionLogModel) throws FrameworkCheckedException {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(actionLogModel);
        if (null == actionLogModel.getActionLogId()) {
            baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
        } else {
            baseWrapper = this.actionLogManager.createOrUpdateActionLog(baseWrapper);
        }
        return (ActionLogModel) baseWrapper.getBasePersistableModel();
    }

    @Override
    public RegistrationStateModel getRegistrationStateById(Long registrationStateId) throws FrameworkCheckedException {
        RegistrationStateModel regStateModel = registrationStateDAO.getRegistrationStateById(registrationStateId);
        return regStateModel;
    }

    @Override
    public AppUserModel getAppUserWithRegistrationState(String mobileNo, String cnic, Long registrationState) throws FrameworkCheckedException {
        AppUserModel appUserModel = appUserDAO.getAppUserWithRegistrationState(mobileNo, cnic, registrationState);
        return appUserModel;
    }

    @Override
    public AppUserModel loadAppUserByCnicAndType(String cnic) throws FrameworkCheckedException {
        AppUserModel appUserModel = appUserManager.loadAppUserByCnicAndType(cnic);
        return appUserModel;
    }

    @Override
    public AppUserModel loadAppUserModelByEmailAddress(String emailAddress) throws FrameworkCheckedException {
        AppUserModel appUserModel = appUserManager.loadAppUserByEmail(emailAddress);
        return appUserModel;
    }

    // CNIC and Mobile
    @Override
    public AppUserModel loadAppUserByCnicAndMobileAndAppUserType(String cnic, String mobileNo, Long appUserTypeId) throws FrameworkCheckedException {
        AppUserModel appUserModel = appUserManager.loadAppUserByCnicAndMobileAndAppUserType(cnic, mobileNo, appUserTypeId);
        return appUserModel;
    }

    @Override
    public AppUserModel loadWalkinCustomerAppUserByCnic(String cnic) throws FrameworkCheckedException {
        AppUserModel appUserModel = appUserManager.loadWalkinCustomerAppUserByCnic(cnic);
        return appUserModel;
    }

    @Override
    public AppUserModel loadAppUserByMobileAndType(String mobileNo) throws FrameworkCheckedException {
        AppUserModel appUserModel = appUserManager.loadAppUserByMobileAndType(mobileNo);
        return appUserModel;
    }

    @Override
    public AppUserModel loadAppUserByMobileAndTypeForCustomer(String mobileNo) throws FrameworkCheckedException {
        AppUserModel appUserModel = appUserManager.loadAppUserByMobileAndTypeForCustomer(mobileNo);
        return appUserModel;
    }

    @Override
    public BlinkCustomerModel loadBlinkCustomerByMobileAndAccUpdate(String mobileNo, Long accUpdate) throws FrameworkCheckedException {
        BlinkCustomerModel blinkCustomerModel = blinkCustomerModelDAO.loadBlinkCustomerModelByMobileAndAccUpdate(mobileNo, accUpdate);
        return blinkCustomerModel;
    }

    @Override
    public MerchantAccountModel loadMerchantCustomerByMobileAndAccUpdate(String mobileNo, Long accUpdate) throws FrameworkCheckedException {
        MerchantAccountModel merchantAccountModel = merchantAccountModelDAO.loadMerchantCustomerModelByMobileAndAccUpdate(mobileNo, accUpdate);
        return merchantAccountModel;
    }

    @Override
    public BlinkCustomerModel loadBlinkCustomerByBlinkCustomerId(Long accType) throws FrameworkCheckedException {
        BlinkCustomerModel blinkCustomerModel = blinkCustomerModelDAO.loadBlinkCustomerModelByBlinkCustomerId(accType);
        return blinkCustomerModel;
    }


    @Override
    public MerchantAccountModel loadMerchantCustomerByBlinkCustomerId(Long accType) throws FrameworkCheckedException {
        MerchantAccountModel merchantAccountModel = merchantAccountModelDAO.loadMerchantModelByBlinkCustomerId(accType);
        return merchantAccountModel;
    }

    @Override
    public AppUserModel loadAppUserByMobileAndType(String mobileNo, Long... appUserTypes) throws FrameworkCheckedException {

        return appUserManager.loadAppUserByMobileAndType(mobileNo, appUserTypes);
    }

    public OLAVO loadAccountInfoFromOLA(String cnic, Long bankId) throws FrameworkCheckedException {
        return this.mfsAccountManager.getAccountInfoFromOLA(cnic, bankId);
    }

    public OLAVO loadAccountInfoFromOLA(AppUserModel appUserModel, SmartMoneyAccountModel smartMoneyAccountModel) throws FrameworkCheckedException, Exception {
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setBasePersistableModel(smartMoneyAccountModel);
        switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
        OLAVO olaVo = new OLAVO();
        Long customerId = null;
        if (appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
            customerId = appUserModel.getCustomerId();
        } else {
            customerId = appUserModel.getAppUserId();
        }
        AccountInfoModel accountInfo = olaVeriflyFinancialInstitution.getAccountInfoModelBySmartMoneyAccount(smartMoneyAccountModel, customerId, null);
        olaVo.setPayingAccNo(accountInfo.getAccountNo());
        switchWrapper.setOlavo(olaVo);
        switchWrapper = this.olaVeriflyFinancialInstitution.getAccountInfo(switchWrapper);

        return switchWrapper.getOlavo();
    }

    public CustomList<CategoryModel> fetchAllProductCatalogCategories() throws FrameworkCheckedException {
        return this.categoryDAO.findAll();
    }

    public void setMiniCommandKeywordDAO(MiniCommandKeywordDAO miniCommandKeywordDAO) {
        this.miniCommandKeywordDAO = miniCommandKeywordDAO;
    }

    public void setMiniHelpKeywordDAO(MiniHelpKeywordDAO miniHelpKeywordDAO) {
        this.miniHelpKeywordDAO = miniHelpKeywordDAO;
    }

    public void setCategoryDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public MiniTransactionDAO getMiniTransactionDAO() {
        return miniTransactionDAO;
    }

    public void setMiniTransactionDAO(MiniTransactionDAO miniTransactionDAO) {
        this.miniTransactionDAO = miniTransactionDAO;
    }

    public void setMiniCommandLogDAO(MiniCommandLogDAO miniCommandLogDAO) {
        this.miniCommandLogDAO = miniCommandLogDAO;
    }

    public BaseWrapper loadOLAAccount(BaseWrapper baseWrapper) {
        SmartMoneyAccountModel sma = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
        sma.setBankIdBankModel(new BankModel());
        sma.setActive(true);
        sma.getBankIdBankModel().setFinancialIntegrationId(FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION);

//		List<SmartMoneyAccountModel> list = this.smartMoneyAccountDAO.loadOLASmartMoneyAccount(sma.getRetailerContactId(), sma.getDistributorContactId());
        List<SmartMoneyAccountModel> list = this.smartMoneyAccountDAO.loadOLASmartMoneyAccount(sma.getCustomerId());

        if (list != null && list.size() > 0) {
            sma = list.get(0);
            baseWrapper.setBasePersistableModel(sma);
        } else
            baseWrapper.setBasePersistableModel(null);

        return baseWrapper;
    }

    public BaseWrapper loadOLASmartMoneyAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        SmartMoneyAccountModel sma = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
        Long retailerContactId = sma.getRetailerContactId();
        Long distributorContactId = sma.getDistributorContactId();

        List<SmartMoneyAccountModel> list = this.smartMoneyAccountDAO.loadOLASmartMoneyAccount(retailerContactId, distributorContactId);

        if (list != null && list.size() > 0) {
            sma = list.get(0);
            baseWrapper.setBasePersistableModel(sma);
        } else
            baseWrapper.setBasePersistableModel(null);

        return baseWrapper;
    }

    public boolean checkTPin(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.checkTPin()");
        }
        boolean tPinFlag = false;

        Long accountId = Long.parseLong(baseWrapper.getObject(CommandFieldConstants.KEY_ACC_ID).toString());

//		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();

        if (accountId != null) {
            SmAcctInfoListViewModel smAcctInfoListViewModel = this.smAcctInfoListViewDAO.findByPrimaryKey(accountId);
            if (smAcctInfoListViewModel != null) {
                if (smAcctInfoListViewModel.getIsTpinRequired()) {
                    tPinFlag = true;
                } else if (smAcctInfoListViewModel.getIsMpinRequired()) {
                    tPinFlag = true;
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.checkTPin()");
        }
        return tPinFlag;
    }

    public boolean validateManualOTPin(String pin) {
        boolean isValid = true;
        Set<String> invalidPinSet = new HashSet<String>();
        invalidPinSet.add("0000");
        invalidPinSet.add("1111");
        invalidPinSet.add("2222");
        invalidPinSet.add("3333");
        invalidPinSet.add("4444");
        invalidPinSet.add("5555");
        invalidPinSet.add("6666");
        invalidPinSet.add("7777");
        invalidPinSet.add("8888");
        invalidPinSet.add("9999");
        invalidPinSet.add("1234");
        invalidPinSet.add("2345");
        invalidPinSet.add("3456");
        invalidPinSet.add("4567");
        invalidPinSet.add("5678");
        invalidPinSet.add("6789");
        invalidPinSet.add("9876");
        invalidPinSet.add("8765");
        invalidPinSet.add("7654");
        invalidPinSet.add("6543");
        invalidPinSet.add("5432");
        invalidPinSet.add("4321");
        invalidPinSet.add("3210");

        if (invalidPinSet.contains(pin)) {
            isValid = false;
        }

        return isValid;
    }

    public ValidationErrors checkSmartMoneyAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.checkSmartMoneyAccount()");
        }
        ValidationErrors validationError = new ValidationErrors();
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();

        if (!smartMoneyAccountModel.getActive())
            validationError.getStringBuilder().append("Account is Closed.");

        if (smartMoneyAccountModel.getCustomerId() != null && smartMoneyAccountModel.getActive()) {
            if (smartMoneyAccountModel.getStatusId() != null && smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED))
                validationError.getStringBuilder().append("Account is Blocked.");
            else if (smartMoneyAccountModel.getStatusId() != null && smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE))
                validationError.getStringBuilder().append("Account is not Active.");
        }
        //In case of handler, ignore PIN change required flag for parent agent
        HandlerModel handlerModel = (HandlerModel) baseWrapper.getObject(CommandFieldConstants.KEY_HANDLER_MODEL);
        boolean checkPinChange = true;
        if (handlerModel != null && smartMoneyAccountModel.getRetailerContactId() != null) {
            checkPinChange = false;
        }

        if (smartMoneyAccountModel.getChangePinRequired() && checkPinChange) {
            validationError.getStringBuilder().append("Account Pin Change is Required");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.checkSmartMoneyAccount()");
        }
        return validationError;
    }

    public ValidationErrors checkSmartMoneyAccount(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.checkSmartMoneyAccount()");
        }
        ValidationErrors validationError = new ValidationErrors();
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);

        if (!smartMoneyAccountModel.getActive()) {
            validationError.getStringBuilder().append(this.getMessageSource().getMessage("ACCOUNT.INACTIVE", null, null));
        }

        //In case of handler, ignore PIN change required flag for parent agent
        HandlerModel handlerModel = (HandlerModel) searchBaseWrapper.getObject(CommandFieldConstants.KEY_HANDLER_MODEL);
        boolean checkPinChange = true;
        if (handlerModel != null && smartMoneyAccountModel.getRetailerContactId() != null) {
            checkPinChange = false;
        }

        if (smartMoneyAccountModel.getChangePinRequired() && checkPinChange) {
            validationError.getStringBuilder().append(this.getMessageSource().getMessage("ACCOUNT.PIN.CHANGE.REQ", null, null));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.checkSmartMoneyAccount()");
        }
        return validationError;
    }

    public ValidationErrors checkRecipientAgentCredentials(AppUserModel agentAppUserModel, Long deviceTypeId) throws FrameworkCheckedException {
        ValidationErrors validationError = new ValidationErrors();
        Long appUserId = agentAppUserModel.getAppUserId();
        if ((null != agentAppUserModel.getAccountClosedSettled() && agentAppUserModel.getAccountClosedSettled())
                || (null != agentAppUserModel.getAccountClosedUnsettled() && agentAppUserModel.getAccountClosedUnsettled())) {

            validationError.getStringBuilder().append("Recipient agent account is closed");
            return validationError;
        }

        UserDeviceAccountsModel userDeviceAccountModel = new UserDeviceAccountsModel();
        userDeviceAccountModel.setAppUserId(appUserId);
        userDeviceAccountModel.setDeviceTypeId(deviceTypeId);

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(userDeviceAccountModel);
        searchBaseWrapper = checkLogin(searchBaseWrapper);
        List<UserDeviceAccountsModel> list = searchBaseWrapper.getCustomList().getResultsetList();

        if (list != null && list.size() > 0) {

            userDeviceAccountModel = list.get(0);
            if (userDeviceAccountModel.getUserId() != null) {
                if (userDeviceAccountModel.getAccountLocked()) {
                    validationError.getStringBuilder().append(this.getMessageSource().getMessage("recipient.agent.accountLocked", null, null));
                } else if (!userDeviceAccountModel.getAccountEnabled()) {
                    validationError.getStringBuilder().append(this.getMessageSource().getMessage("recipient.agent.accountDisabled", null, null));
                }
            } else {
                validationError.getStringBuilder().append("No Credentials Exists Against Recipient Mobile Number");
            }
        } else {
            validationError.getStringBuilder().append("No Credentials Exists Against Recipient Mobile Number");
        }

        return validationError;

    }

    public ValidationErrors checkUserCredentials(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.checkUserCredentials()");
        }
        ValidationErrors validationError = new ValidationErrors();
        AppUserModel appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
        UserDeviceAccountsModel userDeviceAccountModel = new UserDeviceAccountsModel();
        userDeviceAccountModel.setAppUserId(appUserModel.getAppUserId());
        if (appUserModel == null || (appUserModel != null && (appUserModel.getAccountClosedUnsettled() || appUserModel.getAccountClosedSettled()))) {
            validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.accountClosed", null, null));
        } else {

            if (Long.parseLong(baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID).toString()) == DeviceTypeConstantsInterface.MFS_WEB.longValue())
                userDeviceAccountModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
            else if (Long.parseLong(baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID).toString()) == DeviceTypeConstantsInterface.ALLPAY_WEB.longValue()) {
                userDeviceAccountModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);// Commented by Maqsood to introduce USSD agent
//				userDeviceAccountModel.setDeviceTypeId( DeviceTypeConstantsInterface.USSD );
            } else if (Long.parseLong(baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID).toString()) == DeviceTypeConstantsInterface.SMS_GATEWAY.longValue()) {
                if (UserTypeConstantsInterface.CUSTOMER.longValue() == appUserModel.getAppUserTypeId().longValue()) {
                    userDeviceAccountModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
                } else if (UserTypeConstantsInterface.RETAILER.longValue() == appUserModel.getAppUserTypeId().longValue()) {
                    userDeviceAccountModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
                }

            } else if (Long.parseLong(baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID).toString()) == DeviceTypeConstantsInterface.WEB_SERVICE.longValue()) {
                userDeviceAccountModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
            } else {

                userDeviceAccountModel.setDeviceTypeId(Long.parseLong(baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID).toString()));
            }

            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(userDeviceAccountModel);
            searchBaseWrapper = checkLogin(searchBaseWrapper);
            List<UserDeviceAccountsModel> list = searchBaseWrapper.getCustomList().getResultsetList();

            if (list != null && list.size() > 0) {
                userDeviceAccountModel = list.get(0);
                if (userDeviceAccountModel.getUserId() != null) {
                    boolean messageSeparatorFlag = false;

                    if (userDeviceAccountModel.getAccountExpired()) {
                        validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.accountExpired", null, null));
                        messageSeparatorFlag = true;
                    }
                    if (userDeviceAccountModel.getAccountLocked()) {
                        if (messageSeparatorFlag) {
                            validationError.getStringBuilder().append(";");
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.accountLocked", null, null));
                        } else {
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.accountLocked", null, null));
                            messageSeparatorFlag = true;
                        }
                    }
                    if (userDeviceAccountModel.getCredentialsExpired()) {
                        if (messageSeparatorFlag) {
                            validationError.getStringBuilder().append(";");
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.credentialsExpired", null, null));
                        } else {
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.credentialsExpired", null, null));
                            messageSeparatorFlag = true;
                            throw new CommandException(null, ErrorCodes.CREDENTIALS_EXPIRED, ErrorLevel.HIGH, new Throwable());

                        }
                    }
                    if (!userDeviceAccountModel.getAccountEnabled()) {
                        if (messageSeparatorFlag) {
                            validationError.getStringBuilder().append(";");
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.accountDisabled", null, null));
                        } else {
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.accountDisabled", null, null));
                        }
                    }
                } else {
                    validationError.getStringBuilder().append("No Credentials Exists Against that User");
                }
            } else {
                validationError.getStringBuilder().append("No Credentials Exists Against that User");
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.checkUserCredentials()");
        }
        return validationError;
    }

    public ValidationErrors checkUserCredentials(UserDeviceAccountsModel userDeviceAccountModel) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.checkUserCredentials()");
        }
        boolean messageSeparatorFlag = false;
        ValidationErrors validationError = new ValidationErrors();

        if (userDeviceAccountModel.getAccountExpired()) {
            validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.accountExpired", null, null));
            messageSeparatorFlag = true;
        }
        if (userDeviceAccountModel.getAccountLocked()) {
            if (messageSeparatorFlag) {
                validationError.getStringBuilder().append(";");
                validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.accountLocked", null, null));
            } else {
                validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.accountLocked", null, null));
                messageSeparatorFlag = true;
            }
        }
        if (userDeviceAccountModel.getCredentialsExpired()) {
            if (messageSeparatorFlag) {
                validationError.getStringBuilder().append(";");
                validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.credentialsExpired", null, null));
            } else {
                validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.credentialsExpired", null, null));
                messageSeparatorFlag = true;
            }
        }
        if (!userDeviceAccountModel.getAccountEnabled()) {
            if (messageSeparatorFlag) {
                validationError.getStringBuilder().append(";");
                validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.accountDisabled", null, null));
            } else {
                validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.accountDisabled", null, null));
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.checkUserCredentials()");
        }
        return validationError;
    }

    @SuppressWarnings("unchecked")
    public ValidationErrors checkCustomerCredentials(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.checkCustomerCredentials()");
        }

        ValidationErrors validationError = new ValidationErrors();
        AppUserModel appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
        UserDeviceAccountsModel userDeviceAccountModel = new UserDeviceAccountsModel();
        userDeviceAccountModel.setAppUserId(appUserModel.getAppUserId());
        String errorPrefix = StringUtils.defaultString((String) baseWrapper.getObject(CommandFieldConstants.KEY_CUST_ERROR_PREFIX)) + " ";

        logger.info("[CommonComandManagerImpl.checkCustomerCredentials] Checking UserDeviceAccounts flags. AppUserID: " + appUserModel.getAppUserId());

        if (appUserModel.getAccountClosedUnsettled() || appUserModel.getAccountClosedSettled()) {
            validationError.getStringBuilder().append(this.getMessageSource().getMessage("CustomerAccountClosed", null, null));
        } else {

            if (Long.parseLong(baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID).toString()) == DeviceTypeConstantsInterface.ALL_PAY.longValue()
                    && appUserModel.getAppUserTypeId() == 2) {
                userDeviceAccountModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
            } else {

                userDeviceAccountModel.setDeviceTypeId(Long.parseLong(baseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID).toString()));
            }

            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(userDeviceAccountModel);
            searchBaseWrapper = checkLogin(searchBaseWrapper);
            List<UserDeviceAccountsModel> list = searchBaseWrapper.getCustomList().getResultsetList();

            long stateId = (appUserModel.getRegistrationStateId() == null) ? 0L : appUserModel.getRegistrationStateId();
            boolean checkActiveRequired = true;
            if (stateId == RegistrationStateConstants.REQUEST_RECEIVED) {
                checkActiveRequired = false;
            }

            if (list != null && list.size() > 0) {
                userDeviceAccountModel = list.get(0);
                if (userDeviceAccountModel.getUserId() != null) {
                    boolean messageSeparatorFlag = false;

                    if (userDeviceAccountModel.getAccountExpired()) {
                        validationError.getStringBuilder().append(errorPrefix);
                        validationError.getStringBuilder().append(this.getMessageSource().getMessage("CustomerAccountExpired", null, null));
                        messageSeparatorFlag = true;

                    } else if (userDeviceAccountModel.getAccountLocked()) {

                        if (messageSeparatorFlag) {
                            validationError.getStringBuilder().append(" and ");
                            validationError.getStringBuilder().append(errorPrefix);
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("CustomerAccountLocked", null, null));
                        } else {
                            validationError.getStringBuilder().append(errorPrefix);
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("CustomerAccountLocked", null, null));
                            messageSeparatorFlag = true;
                        }

                    } else if (userDeviceAccountModel.getCredentialsExpired()) {

                        if (messageSeparatorFlag) {
                            validationError.getStringBuilder().append(" and ");
                            validationError.getStringBuilder().append(errorPrefix);
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("CustomerCredentialsExpired", null, null));
                        } else {
                            validationError.getStringBuilder().append(errorPrefix);
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("CustomerCredentialsExpired", null, null));
                            messageSeparatorFlag = true;
                        }

                    } else if (!userDeviceAccountModel.getAccountEnabled() && checkActiveRequired) {

                        if (messageSeparatorFlag) {
                            validationError.getStringBuilder().append(" and ");
                            validationError.getStringBuilder().append(errorPrefix);
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("CustomerAccountDisabled", null, null));
                        } else {
                            validationError.getStringBuilder().append(errorPrefix);
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("CustomerAccountDisabled", null, null));
                        }
                    }
                } else {
                    validationError.getStringBuilder().append("No Credentials Exist Against Customer");
                }
            } else {
                validationError.getStringBuilder().append("No Credentials Exist Against Customer");
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.checkCustomerCredentials()");
        }
        return validationError;
    }

    private WebServiceVO validateUserCredentials(WebServiceVO webServiceVO, AppUserModel appUserModel) {
        return webServiceVO;
    }


    //	@Override
//	public BaseWrapper updateDebitBlock(BaseWrapper baseWrapper) throws FrameworkCheckedException {
//		AppUserModel appUserModel = null;
//		CustomerModel customerModel = null;
//		try{
//			customerModel = getCommonCommandManager().loadCustomer(customerModel);
//			appUserModel = appUserManager.loadAppUser(appUserModel.getPrimaryKey());
//		}
//		catch (Exception ex){}
//		return baseWrapper;
//	}

    @Override
    public Boolean checkActiveAgentAppUserForOpenAPI(WebServiceVO webServiceVO, AppUserModel appUserModel) throws Exception {
        Boolean isValid = Boolean.TRUE;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        if (appUserModel != null) {
//            if(webServiceVO.getCnicNo()!= null && !webServiceVO.getCnicNo().equals(appUserModel.getNic())){
//                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_CNIC);
//                isValid = Boolean.FALSE;
//                return isValid;
//            }
            logger.info("[CommonCommandManagerImpl.checkActiveAppUser] checking activation status for AppUserID:" + appUserModel.getAppUserId());
            if (appUserModel.getRetailerContactId() != null) {
                webServiceVO.setCnicNo(appUserModel.getNic());
                if (appUserModel.getRegistrationStateId() == null
                        || (appUserModel.getRegistrationStateId() != null && (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DISCREPANT)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.REJECTED)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DECLINE)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DORMANT)))) {
                    logger.info("[CommonCommandManagerImpl.checkActiveAppUser] customer is in Registration state :" + appUserModel.getRegistrationStateId());
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_INVALID_STATE);
                    isValid = Boolean.FALSE;
                    return isValid;
                }
                if (appUserModel.getRegistrationStateId() == null || appUserModel.getAccountStateId().equals(AccountStateConstants.ACCOUNT_STATE_CLOSED)) {
                    logger.info("[CommonCommandManagerImpl.checkActiveAppUser] Agent is in Registration state :" + appUserModel.getRegistrationStateId());
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.AGENT_NOT_FOUND);
                    isValid = Boolean.FALSE;
                    return isValid;
                }
                RetailerContactModel retailerContactModel = new RetailerContactModel();
                retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
                baseWrapper.setBasePersistableModel(retailerContactModel);
                baseWrapper = loadRetailerContact(baseWrapper);
                retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
                if (retailerContactModel.getActive()) {
                    RetailerModel retailerModel = new RetailerModel();
                    if (retailerContactModel.getRetailerId() != null) {
                        retailerModel.setRetailerId(retailerContactModel.getRetailerId());
                        baseWrapper.setBasePersistableModel(retailerModel);
                        baseWrapper = loadRetailer(baseWrapper);
                        retailerModel = (RetailerModel) baseWrapper.getBasePersistableModel();
                        if (!retailerModel.getActive()) {
                            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_INVALID_STATE);
                            isValid = Boolean.FALSE;
                            return isValid;
                        }
                    }
                }

            }
            ThreadLocalAppUser.setAppUserModel(appUserModel);
            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.setBasePersistableModel(appUserModel);
            bWrapper = loadUserDeviceAccountByMobileNumber(bWrapper);
            UserDeviceAccountsModel uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();

            if (uda.getAccountLocked()) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_BLOCKED);
                isValid = Boolean.FALSE;
                return isValid;
            }

            if (!uda.getAccountEnabled()) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_DEACTIVATED);
                isValid = Boolean.FALSE;
                return isValid;
            }
            ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
            webServiceVO.setCnicNo(appUserModel.getNic());
            String cnic = webServiceVO.getCnicNo();

            if (this.getCommonCommandManager().isCnicBlacklisted(cnic)) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CNIC_BLACKLISTED);
                isValid = Boolean.FALSE;
                return isValid;
            }
        } else {
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.AGENT_NOT_FOUND);
            isValid = Boolean.FALSE;
            return isValid;
        }
        return isValid;
    }

    public Boolean checkActiveAppUserForOpenAPI(WebServiceVO webServiceVO, AppUserModel appUserModel) throws Exception {
        Boolean isValid = Boolean.TRUE;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        if (appUserModel != null) {
            if (webServiceVO.getCnicNo() != null && !webServiceVO.getCnicNo().equals(appUserModel.getNic())) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_CNIC);
                isValid = Boolean.FALSE;
                return isValid;
            }
            CustomerModel customerModel = new CustomerModel();
            customerModel.setCustomerId(appUserModel.getCustomerId());
            baseWrapper.setBasePersistableModel(customerModel);
            baseWrapper = loadCustomer(baseWrapper);
            customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
            logger.info("[CommonCommandManagerImpl.checkActiveAppUser] checking activation status for AppUserID:" + appUserModel.getAppUserId());
            if (appUserModel.getCustomerId() != null) {
                webServiceVO.setCnicNo(appUserModel.getNic());
                if (appUserModel.getRegistrationStateId() == null
                        || (appUserModel.getRegistrationStateId() != null && (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DISCREPANT)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.REJECTED)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DECLINE)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DORMANT)))) {
                    if ((customerModel.getSegmentId().equals(Long.parseLong(MessageUtil.getMessage("Minor_segment_id")))) &&
                            appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DISCREPANT)) {
                        isValid = Boolean.TRUE;
                        FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    } else {
                        isValid = Boolean.FALSE;
                        FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_INVALID_STATE);
                    }
                    logger.info("[CommonCommandManagerImpl.checkActiveAppUser] customer is in Registration state :" + appUserModel.getRegistrationStateId());
                    return isValid;
                }
                if (appUserModel.getRegistrationStateId() == null || appUserModel.getAccountStateId().equals(AccountStateConstants.ACCOUNT_STATE_CLOSED)) {
                    logger.info("[CommonCommandManagerImpl.checkActiveAppUser] customer is in Registration state :" + appUserModel.getRegistrationStateId());
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CLOSED);
                    isValid = Boolean.FALSE;
                    return isValid;
                }

                if (!customerModel.getRegister()) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_INVALID_STATE);
                    isValid = Boolean.FALSE;
                    return isValid;
                }
            }
            ThreadLocalAppUser.setAppUserModel(appUserModel);
            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.setBasePersistableModel(appUserModel);
            bWrapper = loadUserDeviceAccountByMobileNumber(bWrapper);
            UserDeviceAccountsModel uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();

            if (uda.getAccountLocked()) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_BLOCKED);
                isValid = Boolean.FALSE;
                return isValid;
            }

            if (!uda.getAccountEnabled()) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_DEACTIVATED);
                isValid = Boolean.FALSE;
                return isValid;
            }
            ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
            webServiceVO.setCnicNo(appUserModel.getNic());
            String cnic = webServiceVO.getCnicNo();

            if (this.getCommonCommandManager().isCnicBlacklisted(cnic)) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
                isValid = Boolean.FALSE;
                return isValid;
            }
        } else {
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_NOT_FOUND);
            isValid = Boolean.FALSE;
            return isValid;
        }
        return isValid;
    }

    @Override
    public Boolean checkActiveAppUserAndAccountAuthenticationForOpenAPI(WebServiceVO webServiceVO, AppUserModel appUserModel) throws Exception {
        Boolean isValid = Boolean.TRUE;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        if (appUserModel != null) {
            if (webServiceVO.getCnicNo() != null && !webServiceVO.getCnicNo().equals(appUserModel.getNic())) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_CNIC);
                isValid = Boolean.FALSE;
                return isValid;
            }
            logger.info("[CommonCommandManagerImpl.checkActiveAppUser] checking activation status for AppUserID:" + appUserModel.getAppUserId());
            if (appUserModel.getCustomerId() != null) {
                webServiceVO.setCnicNo(appUserModel.getNic());
                if (appUserModel.getRegistrationStateId() == null
                        || (appUserModel.getRegistrationStateId() != null && (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DISCREPANT)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.REJECTED)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DECLINE)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DORMANT)))) {
                    logger.info("[CommonCommandManagerImpl.checkActiveAppUser] customer is in Registration state :" + appUserModel.getRegistrationStateId());
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_INVALID_STATE);
                    isValid = Boolean.FALSE;
                    return isValid;
                }
                CustomerModel customerModel = new CustomerModel();
                customerModel.setCustomerId(appUserModel.getCustomerId());
                baseWrapper.setBasePersistableModel(customerModel);
                baseWrapper = loadCustomer(baseWrapper);
                customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
                if (!customerModel.getRegister()) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_INVALID_STATE);
                    isValid = Boolean.FALSE;
                    return isValid;
                }
            }
            ThreadLocalAppUser.setAppUserModel(appUserModel);
            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.setBasePersistableModel(appUserModel);
            bWrapper = loadUserDeviceAccountByMobileNumber(bWrapper);
            UserDeviceAccountsModel uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();

            if (uda.getAccountLocked()) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.ACCOUNT_BLOCKED);
                isValid = Boolean.FALSE;
                return isValid;
            }
            String pin = EncryptionUtil.decryptWithAES("682ede816988e58fb6d057d9d85605e0", uda.getPin());
            if (!pin.equals(webServiceVO.getMobilePin())) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_LOGIN_PIN);
                isValid = Boolean.FALSE;
                return isValid;
            }
            if (!uda.getAccountEnabled()) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_DEACTIVATED);
                isValid = Boolean.FALSE;
                return isValid;
            }
            ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
            webServiceVO.setCnicNo(appUserModel.getNic());
            String cnic = webServiceVO.getCnicNo();

            if (this.getCommonCommandManager().isCnicBlacklisted(cnic)) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
                isValid = Boolean.FALSE;
                return isValid;
            }
        } else {
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_NOT_FOUND);
            isValid = Boolean.FALSE;
            return isValid;
        }
        return isValid;
    }

    public ValidationErrors checkActiveAppUser(AppUserModel appUserModel) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.checkActiveAppUser()");
        }
        ValidationErrors validationError = new ValidationErrors();

        BaseWrapper baseWrapper = new BaseWrapperImpl();


        if (appUserModel != null) {
            logger.info("[CommonCommandManagerImpl.checkActiveAppUser] checking activation status for AppUserID:" + appUserModel.getAppUserId());
            if (appUserModel.getCustomerId() != null) {
                if (appUserModel.getRegistrationStateId() == null
                        || (appUserModel.getRegistrationStateId() != null && (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DISCREPANT)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.REJECTED)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DECLINE)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DORMANT)))) {
                    logger.info("[CommonCommandManagerImpl.checkActiveAppUser] customer is in Registration state :" + appUserModel.getRegistrationStateId());
                    throw new CommandException("Customer must be in active state to perform this transaction.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                }
                CustomerModel customerModel = new CustomerModel();
                customerModel.setCustomerId(appUserModel.getCustomerId());
                baseWrapper.setBasePersistableModel(customerModel);
                baseWrapper = loadCustomer(baseWrapper);
                customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
                if (!customerModel.getRegister()) {
                    validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.customerInactive", null, null));
                }
            } else if (appUserModel.getRetailerContactId() != null) {
                if (appUserModel.getRegistrationStateId() == null
                        || (appUserModel.getRegistrationStateId() != null && (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DISCREPANT)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.REJECTED)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.DECLINE)
                        || appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DORMANT)))) {
                    logger.info("[CommonCommandManagerImpl.checkActiveAppUser] Retailer is in Registration state :" + appUserModel.getRegistrationStateId());
                    throw new CommandException("Account must be in active state.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                }
                RetailerContactModel retailerContactModel = new RetailerContactModel();
                retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
                baseWrapper.setBasePersistableModel(retailerContactModel);
                baseWrapper = loadRetailerContact(baseWrapper);
                retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
                if (retailerContactModel.getActive()) {
                    RetailerModel retailerModel = new RetailerModel();
                    if (retailerContactModel.getRetailerId() != null) {
                        retailerModel.setRetailerId(retailerContactModel.getRetailerId());
                        baseWrapper.setBasePersistableModel(retailerModel);
                        baseWrapper = loadRetailer(baseWrapper);
                        retailerModel = (RetailerModel) baseWrapper.getBasePersistableModel();
                        if (!retailerModel.getActive()) {
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.retailerInactive", null, null));
                        }
                    }
                } else {
                    validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.retailerContactInactive", null, null));
                }
            } else if (appUserModel.getDistributorContactId() != null) {
                DistributorContactModel distributorContactModel = new DistributorContactModel();
                distributorContactModel.setDistributorContactId(appUserModel.getDistributorContactId());
                baseWrapper.setBasePersistableModel(distributorContactModel);
                baseWrapper = loadDistributorContact(baseWrapper);
                distributorContactModel = (DistributorContactModel) baseWrapper.getBasePersistableModel();
                distributorContactModel = (DistributorContactModel) baseWrapper.getBasePersistableModel();
                if (distributorContactModel.getActive()) {
                    DistributorModel distributorModel = new DistributorModel();
                    if (distributorContactModel.getDistributorId() != null) {
                        distributorModel.setDistributorId(distributorContactModel.getDistributorId());
                        baseWrapper.setBasePersistableModel(distributorModel);
                        baseWrapper = loadDistributor(baseWrapper);
                        distributorModel = (DistributorModel) baseWrapper.getBasePersistableModel();
                        if (!distributorModel.getActive()) {
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.distributorInactive", null, null));
                        }
                    }

                } else if (appUserModel.getHandlerId() != null) {

                    HandlerModel handlerModel = this.handlerDAO.findByPrimaryKey(appUserModel.getHandlerId());

                    RetailerContactModel retailerContactModel = new RetailerContactModel();
                    retailerContactModel.setRetailerContactId(handlerModel.getRetailerContactId());
                    baseWrapper.setBasePersistableModel(retailerContactModel);
                    baseWrapper = loadRetailerContact(baseWrapper);
                    retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();

                    if (retailerContactModel.getRetailerId() != null) {

                        RetailerModel retailerModel = new RetailerModel();
                        retailerModel.setRetailerId(retailerContactModel.getRetailerId());
                        baseWrapper.setBasePersistableModel(retailerModel);
                        baseWrapper = loadRetailer(baseWrapper);
                        retailerModel = (RetailerModel) baseWrapper.getBasePersistableModel();

                        if (!retailerModel.getActive()) {
                            logger.error("Retailer of Handler " + appUserModel.getUsername() + " is not active.");
                            validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.retailerInactive", null, null));
                        }
                    } else {
                        logger.error("Retailer Contact of Handler " + appUserModel.getUsername() + " is not active.");
                        validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.retailerContactInactive", null, null));
                    }
                } else {
                    validationError.getStringBuilder().append(this.getMessageSource().getMessage("LoginCommand.distributorContactInactive", null, null));
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.checkActiveAppUser()");
        }
        return validationError;
    }

    public BaseWrapper updateMfsPin(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.updateMfsPin()");
        }
        UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
        if (userDeviceAccountsModel != null) {
            baseWrapper.setBasePersistableModel(this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.updateMfsPin()");
        }
        return baseWrapper;
    }

    public BaseWrapper loadMfs(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadMfs()");

        }
        UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
        List<UserDeviceAccountsModel> list = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL).getResultsetList();
        if (list != null && list.size() > 0) {
            userDeviceAccountsModel = list.get(0);
            baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadMfs()");
        }
        return baseWrapper;
    }

    public BaseWrapper checkAppVersion(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.checkAppVersion()");
        }
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setEnableLike(Boolean.FALSE);

        AppVersionModel appVersionModel = (AppVersionModel) baseWrapper.getBasePersistableModel();
        if (appVersionModel != null) {
            CustomList<AppVersionModel> customList = this.appVersionDAO.findByExample(appVersionModel, null, null, exampleHolder);
            List<AppVersionModel> list = customList.getResultsetList();
            if (list != null && list.size() > 0) {
                appVersionModel = list.get(0);
            }
            baseWrapper.setBasePersistableModel(appVersionModel);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.checkAppVersion()");
        }
        return baseWrapper;
    }

    public BaseWrapper getLatestAppVersion(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.getLatestAppVersion()");
        }
        AppVersionListViewModel appVersionListViewModel = (AppVersionListViewModel) baseWrapper.getBasePersistableModel();
        List<AppVersionListViewModel> list = this.appVersionListViewDAO.findByExample(appVersionListViewModel, null).getResultsetList();
        if (!list.isEmpty()) {
            appVersionListViewModel = list.get(0);
            baseWrapper.setBasePersistableModel(appVersionListViewModel);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.getLatestAppVersion()");
        }
        return baseWrapper;
    }

    public SearchBaseWrapper loadTickerString(SearchBaseWrapper searchBaseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadTickerString()");
        }
        TickerModel tickerModel = (TickerModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<TickerModel> list = this.tickerDAO.findByExample(tickerModel);
        searchBaseWrapper.setCustomList(list);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadTickerString()");
        }
        return searchBaseWrapper;
    }

    public BaseWrapper loadDefaultTickerString() {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadDefaultTickerString()");
        }
        BaseWrapper baseWrapper = this.tickerDAO.loadDefaultTicker();
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadDefaultTickerString()");
        }
        return baseWrapper;
    }

    public BaseWrapper loadAllPayDefaultTickerString() {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadAllPayDefaultTickerString()");
        }
        BaseWrapper baseWrapper = new BaseWrapperImpl();

        TickerModel tickerModel = this.tickerDAO.findByPrimaryKey(CommandConstants.ALLPAY_TICKER_STRING_ID);
        baseWrapper.setBasePersistableModel(tickerModel);

        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadAllPayDefaultTickerString()");
        }
        return baseWrapper;
    }

    public SearchBaseWrapper getFavoriteNumbers(SearchBaseWrapper searchBaseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.getFavoriteNumbers()");
        }
        //FavoriteNumbersModel favoriteNumbersModel = (FavoriteNumbersModel)searchBaseWrapper.getBasePersistableModel();
        FavoriteNumberListViewModel favoriteNumbersModel = (FavoriteNumberListViewModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<FavoriteNumberListViewModel> list = this.favoriteNumberListViewDAO.findByExample(favoriteNumbersModel, null, searchBaseWrapper.getSortingOrderMap());
        searchBaseWrapper.setCustomList(list);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.getFavoriteNumbers()");
        }
        return searchBaseWrapper;
    }

    public BaseWrapper createFavoriteNumbers(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.createFavoriteNumbers()");
        }
        ArrayList<FavoriteNumbersModel> favoriteNumbersList = (ArrayList) baseWrapper.getObject("FavoriteNumberList");
        removeFavoriteNumbers(baseWrapper);
        this.favoriteNumbersDAO.saveOrUpdateCollection(favoriteNumbersList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.createFavoriteNumbers()");
        }
        return baseWrapper;
    }

    public SearchBaseWrapper loadProdCatalogForDiscAndVar(SearchBaseWrapper searchBaseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadProdCatalogForDiscAndVar()");
        }
        ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = (ProdCatalogDetailListViewModel) searchBaseWrapper.getBasePersistableModel();
        List<ProdCatalogDetailListViewModel> list = this.prodCatalogDetailListViewDAO.loadProductCatalogForVariableAndDiscrete(prodCatalogDetailListViewModel.getProductCatalogId());
        CustomList<ProdCatalogDetailListViewModel> customList = new CustomList();
        customList.setResultsetList(list);
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadProdCatalogForDiscAndVar()");
        }
        return searchBaseWrapper;
    }

    public SearchBaseWrapper loadProdCatalogForBillPayment(SearchBaseWrapper searchBaseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadProdCatalogForBillPayment()");
        }
        ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = (ProdCatalogDetailListViewModel) searchBaseWrapper.getBasePersistableModel();
        List<ProdCatalogDetailListViewModel> list = this.prodCatalogDetailListViewDAO.loadProductCatalogForBillPayment(prodCatalogDetailListViewModel.getProductCatalogId());
        CustomList<ProdCatalogDetailListViewModel> customList = new CustomList();
        customList.setResultsetList(list);
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadProdCatalogForBillPayment()");
        }
        return searchBaseWrapper;
    }

    public SearchBaseWrapper loadCatalogProductsForRetailers(SearchBaseWrapper searchBaseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadCatalogProductsForRetailers()");
        }
        ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = (ProdCatalogDetailListViewModel) searchBaseWrapper.getBasePersistableModel();
        List<ProdCatalogDetailListViewModel> list = this.prodCatalogDetailListViewDAO.loadCatalogProductsForRetailers(prodCatalogDetailListViewModel.getProductCatalogId());
        CustomList<ProdCatalogDetailListViewModel> customList = new CustomList();
        customList.setResultsetList(list);
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadCatalogProductsForRetailers()");
        }
        return searchBaseWrapper;
    }

    public SearchBaseWrapper loadCatalogServiceForRetailers(SearchBaseWrapper searchBaseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadCatalogServiceForRetailers()");
        }
        ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = (ProdCatalogDetailListViewModel) searchBaseWrapper.getBasePersistableModel();
        List<ProdCatalogDetailListViewModel> list = this.prodCatalogDetailListViewDAO.loadCatalogServiceForRetailers(prodCatalogDetailListViewModel.getProductCatalogId());
        CustomList<ProdCatalogDetailListViewModel> customList = new CustomList();
        customList.setResultsetList(list);
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadCatalogServiceForRetailers()");
        }
        return searchBaseWrapper;
    }

    public SearchBaseWrapper loadCatalogServiceForCustomer(SearchBaseWrapper searchBaseWrapper) {
        ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = (ProdCatalogDetailListViewModel) searchBaseWrapper.getBasePersistableModel();
        List<ProdCatalogDetailListViewModel> list = this.prodCatalogDetailListViewDAO.loadCatalogServiceForCustomer(prodCatalogDetailListViewModel.getProductCatalogId());
        CustomList<ProdCatalogDetailListViewModel> customList = new CustomList();
        customList.setResultsetList(list);
        searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    public SearchBaseWrapper loadCatalogServiceAndProductsForRetailers(SearchBaseWrapper searchBaseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadCatalogServiceForRetailers()");
        }
        ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = (ProdCatalogDetailListViewModel) searchBaseWrapper.getBasePersistableModel();
        List<ProdCatalogDetailListViewModel> list = this.prodCatalogDetailListViewDAO.loadCatalogProductsForAllPayWeb(prodCatalogDetailListViewModel.getProductCatalogId());
        CustomList<ProdCatalogDetailListViewModel> customList = new CustomList();
        customList.setResultsetList(list);
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadCatalogServiceForRetailers()");
        }
        return searchBaseWrapper;
    }

    public BaseWrapper updateSmartMoneyAccount(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.updateSmartMoneyAccount()");
        }
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
        this.smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.updateSmartMoneyAccount()");
        }
        return baseWrapper;
    }

    public BaseWrapper updateAppUser(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.updateAppUser()");
        }
        AppUserModel appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
        this.appUserDAO.saveOrUpdate(appUserModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.updateAppUser");
        }
        return baseWrapper;
    }

    public BaseWrapper removeFavoriteNumbers(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.removeFavoriteNumbers()");
        }
        FavoriteNumbersModel favoriteNumbersModel = (FavoriteNumbersModel) baseWrapper.getBasePersistableModel();
        this.favoriteNumbersDAO.deteleFavoriteNumbers(favoriteNumbersModel.getAppUserId());
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.removeFavoriteNumbers()");
        }
        return baseWrapper;
    }

    public BaseWrapper loadCommand(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadCommand()");
        }
        CommandModel commandModel = (CommandModel) baseWrapper.getBasePersistableModel();
        commandModel = this.commandDAO.findByPrimaryKey(commandModel.getCommandId());
        baseWrapper.setBasePersistableModel(commandModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadCommand()");
        }
        return baseWrapper;
    }

//	@Override
//	public List<ScheduleFundsTransferDetailModel> fetchScheduleFundsTransferDetailList(Date date) throws FrameworkCheckedException {
//		return null;
//	}

    public BaseWrapper loadAppUser(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadAppUser()");
        }
        AppUserModel appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
        appUserModel = this.appUserDAO.findByPrimaryKey(appUserModel.getAppUserId());

        //Start : Do this due to Lazy loading technique.

        appUserModel.getCustomerId();
        appUserModel.getRetailerContactId();
        appUserModel.getDistributorContactId();

        //End

        baseWrapper.setBasePersistableModel(appUserModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadAppUser()");
        }
        return baseWrapper;
    }

    @Override
    public AgentBvsStatModel loadAgentBvsStat(AgentBvsStatModel agentBvsStatModel) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadAgentBvsStat()");
        }

        agentBvsStatModel = this.agentBvsStatDAO.loadAgentBvsStatByAgentId((agentBvsStatModel.getAgentId()));


        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadAgentBvsStat()");
        }
        return agentBvsStatModel;

    }

    public SearchBaseWrapper loadAppUserByMobileNumberAndType(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {

        AppUserModel appUserModel = (AppUserModel) searchBaseWrapper.getBasePersistableModel();
        appUserModel = appUserManager.loadAppUserByQuery(appUserModel.getMobileNo(), appUserModel.getAppUserTypeId().longValue());
        searchBaseWrapper.setBasePersistableModel(appUserModel);
        return searchBaseWrapper;
    }

    public SearchBaseWrapper searchAppUserByExample(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {

        return mfsAccountManager.loadAppUserByMobileNumberAndType(searchBaseWrapper);
    }

    public boolean isAmountWithinTransactionLimits(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.isAmountWithinTransactionLimits()");
        }
        boolean result = false;
        ProductModel productModel = (ProductModel) baseWrapper.getBasePersistableModel();
        productModel = this.productDAO.findByPrimaryKey(productModel.getPrimaryKey());
        baseWrapper.setBasePersistableModel(productModel);
        Double transactionAmount = (Double) baseWrapper.getObject(CommandFieldConstants.KEY_TXAM);
        if (null != productModel && null != productModel.getMinLimit() && transactionAmount >= productModel.getMinLimit()) {
            if (null != productModel.getMaxLimit() && transactionAmount <= productModel.getMaxLimit()) {
                result = true;
            } else {
                result = false;
            }

        } else {
            result = false;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.isAmountWithinTransactionLimits()");
        }
        return result;

    }

    public SearchBaseWrapper checkLogin(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.checkLogin()");
        }
        UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<UserDeviceAccountsModel> list = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        //*********************************************************************************************
        //							General check if CNIC is blacklisted							 //
        //*********************************************************************************************
        if (list.getResultsetList() != null && list.getResultsetList().size() > 0) {
            String cnic = list.getResultsetList().get(0).getAppUserIdAppUserModel().getNic();
            if (cnic != null) {
                if (accountControlManager.isCnicBlacklisted(cnic)) {
                    throw new FrameworkCheckedException(this.getMessageSource().getMessage("LoginCommand.accountBlacklisted", new Object[]{cnic}, null));
                }
            }
        }
        //**********************************************************************************************

        searchBaseWrapper.setCustomList(list);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.checkLogin()");
        }
        return searchBaseWrapper;
    }

    public BaseWrapper loadVerifly(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadVerifly()");
        }
        VeriflyListViewModel veriflyListViewModel = (VeriflyListViewModel) baseWrapper.getBasePersistableModel();
        veriflyListViewModel = this.veriflyListViewDAO.findByPrimaryKey(veriflyListViewModel.getAppUserBankUserId());
        baseWrapper.setBasePersistableModel(veriflyListViewModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadVerifly()");
        }
        return baseWrapper;
    }

    public SearchBaseWrapper loadServices(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadServices()");
        }
        CatalogServiceListViewModel catalogServiceListViewModel = (CatalogServiceListViewModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<CatalogServiceListViewModel> list = this.catalogServiceListViewDAO.findByExample(catalogServiceListViewModel);
        searchBaseWrapper.setCustomList(list);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadServices()");
        }
        return searchBaseWrapper;
    }

    public SearchBaseWrapper loadCatalogVersion(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadCatalogVersion()");
        }
        CriteriaConfiguration criteriaConfiguration = new CriteriaConfiguration();
        criteriaConfiguration.setEnableLike(false);
        criteriaConfiguration.setIgnoreCase(false);
        CatalogVersionListViewModel catalogVersionListViewModel = (CatalogVersionListViewModel) searchBaseWrapper.getBasePersistableModel();
        List<CatalogVersionListViewModel> list =
                this.genericDao.findEntityByExample(catalogVersionListViewModel, criteriaConfiguration);

        //(catalogVersionListViewModel, null, null, exampleConfigHolderModel,null);
//		CustomList<CatalogVersionListViewModel> list = this.catalogVersionListViewDAO.findByExample(catalogVersionListViewModel);

        CustomList<CatalogVersionListViewModel> customList = new CustomList<CatalogVersionListViewModel>(list);
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadCatalogVersion()");
        }
        return searchBaseWrapper;
    }

    public List<AdsModel> findAds(AdsModel adsModel) throws FrameworkCheckedException {
        CriteriaConfiguration criteriaConfiguration = new CriteriaConfiguration();
        criteriaConfiguration.setEnableLike(false);
        criteriaConfiguration.setIgnoreCase(false);
        criteriaConfiguration.setMatchMode(MatchMode.EXACT);
        List<AdsModel> list = this.genericDao.findEntityByExample(adsModel, criteriaConfiguration);
        return list;
    }

    public SearchBaseWrapper loadProductCatalog(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadProductCatalog()");
        }
        ProdCatalogDetailListViewModel prodCatalogDetailListViewModel = (ProdCatalogDetailListViewModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<ProdCatalogDetailListViewModel> list = this.prodCatalogDetailListViewDAO.findByExample(prodCatalogDetailListViewModel, null, searchBaseWrapper.getSortingOrderMap());
        searchBaseWrapper.setCustomList(list);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadProductCatalog()");
        }
        return searchBaseWrapper;
    }

    public VeriflyManager loadVeriflyManagerByAccountId(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadVeriflyManagerByAccountId()");
        }
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
        VeriflyManager veriflyManager = this.veriflyController.getVeriflyMgrByAccountId(smartMoneyAccountModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadVeriflyManagerByAccountId()");
        }
        return veriflyManager;
    }

    public VeriflyBaseWrapper changeVeriflyPin(VeriflyManager veriflyManager, VeriflyBaseWrapper veriflyBaseWrapper) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.changeVeriflyPin()");
        }
        veriflyBaseWrapper = veriflyManager.changePIN(veriflyBaseWrapper);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.changeVeriflyPin()");
        }
        return veriflyBaseWrapper;
    }

    public VeriflyBaseWrapper verifyVeriflyPin(VeriflyManager veriflyManager, VeriflyBaseWrapper veriflyBaseWrapper) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.verifyVeriflyPin()");
        }
        veriflyBaseWrapper = veriflyManager.verifyPIN(veriflyBaseWrapper);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.verifyVeriflyPin()");
        }
        return veriflyBaseWrapper;
    }

    /**
     * @param appUserModel
     * @param bankPin
     * @param transactionCodeModel
     * @param productModel
     * @param fetchTitle
     * @throws FrameworkCheckedException
     */
    public SwitchWrapper verifyPIN(AppUserModel appUserModel, String encryptedPin) throws FrameworkCheckedException {
        return verifyPIN(appUserModel, encryptedPin, null);
    }

    public SwitchWrapper verifyPIN(AppUserModel appUserModel, String encryptedPin, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        boolean isHandler = workFlowWrapper != null && workFlowWrapper.getHandlerAppUserModel() != null && workFlowWrapper.getHandlerAppUserModel().getHandlerId() != null;
        AppUserModel handlerAppUser = isHandler ? workFlowWrapper.getHandlerAppUserModel() : null;

        if (isHandler)
            logger.info("CommonCommandManagerImpl.verifyPIN > Handler APP USER:" + handlerAppUser.getUsername() + ". HandlerId:" + handlerAppUser.getHandlerId());
        else
            logger.info("CommonCommandManagerImpl.verifyPIN > APP USER:" + appUserModel.getUsername() + ". RetailerContactId:" + appUserModel.getRetailerContactId());

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();

        try {

            SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
//			smartMoneyAccountModel.setBankId(Long.parseLong(bankId));
            smartMoneyAccountModel.setRetailerContactId(appUserModel.getRetailerContactId());
            smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            smartMoneyAccountModel.setActive(Boolean.TRUE);
            smartMoneyAccountModel.setDeleted(Boolean.FALSE);
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

            searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            searchBaseWrapper = loadSmartMoneyAccount(searchBaseWrapper);
            smartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
            switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);

            BaseWrapper baseWrapperTemp = new BaseWrapperImpl();
            baseWrapperTemp.setBasePersistableModel(smartMoneyAccountModel);

            AbstractFinancialInstitution abstractFinancialInstitution = loadFinancialInstitution(baseWrapperTemp);

            if (isHandler) {
                SmartMoneyAccountModel handlerSmartMoneyAccountModel = new SmartMoneyAccountModel();
                //			smartMoneyAccountModel.setBankId(Long.parseLong(bankId));
                handlerSmartMoneyAccountModel.setHandlerId(handlerAppUser.getHandlerId());
                handlerSmartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                handlerSmartMoneyAccountModel.setActive(Boolean.TRUE);
                handlerSmartMoneyAccountModel.setDeleted(Boolean.FALSE);

                searchBaseWrapper.setBasePersistableModel(handlerSmartMoneyAccountModel);
                searchBaseWrapper = loadSmartMoneyAccount(searchBaseWrapper);

                if (searchBaseWrapper.getCustomList() != null && searchBaseWrapper.getCustomList().getResultsetList() != null && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
                    handlerSmartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
                } else {
                    throw new FrameworkCheckedException("Unable to load Handler SmartMoneyAccountModel. Not found.");
                }

                workFlowWrapper.setHandlerSMAModel(handlerSmartMoneyAccountModel);

                switchWrapper.setWorkFlowWrapper(workFlowWrapper);
            }

            switchWrapper.putObject(CommandFieldConstants.KEY_PIN, encryptedPin);
//		    switchWrapper.putObject(CommandFieldConstants.KEY_PIN , encryptedPin.encryptPin(bankPin)) ;

            switchWrapper = abstractFinancialInstitution.verifyPIN(switchWrapper);

            logger.info("Bank PIN Verified" + (switchWrapper != null));

            switchWrapper.getAccountInfoModel().setOldPin(encryptedPin);
            switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);

        } catch (FrameworkCheckedException e) {
            throw e;
//			throw new FrameworkCheckedException(e.getMessage());

        } catch (Exception e) {

            throw new FrameworkCheckedException(e.getMessage());
        }

        return switchWrapper;
    }

    public SearchBaseWrapper loadSmartMoneyAccountInfo(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadSmartMoneyAccountInfo()");
        }
        SmAcctInfoListViewModel sMAcctInfoListViewModel = (SmAcctInfoListViewModel) searchBaseWrapper.getBasePersistableModel();
        List<SmAcctInfoListViewModel> list = this.genericDao.findEntityByExample(sMAcctInfoListViewModel, null);
        CustomList<SmAcctInfoListViewModel> customList = new CustomList<SmAcctInfoListViewModel>(list);
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadSmartMoneyAccountInfo()");
        }
        return searchBaseWrapper;
    }

    public SearchBaseWrapper loadSmartMoneyAccount(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadSmartMoneyAccount()");
        }
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<SmartMoneyAccountModel> customList = this.smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadSmartMoneyAccount()");
        }
        return searchBaseWrapper;
    }

    public BaseWrapper loadSmartMoneyAccount(BaseWrapper baseWrapper) throws
            FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadSmartMoneyAccount()");
        }
        SmartMoneyAccountModel smartMoneyAccountModel = this.smartMoneyAccountDAO.findByPrimaryKey(
                baseWrapper.getBasePersistableModel().getPrimaryKey());
        baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadSmartMoneyAccount()");
        }
        return baseWrapper;
    }

    public BaseWrapper setDefaultSmartMoneyAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.setDefaultSmartMoneyAccount()");
        }
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
        smartMoneyAccountModel.setDefAccount(Boolean.TRUE);
        SmartMoneyAccountModel newSmartMoneyAccountModel = new SmartMoneyAccountModel();
        newSmartMoneyAccountModel.setCustomerId(smartMoneyAccountModel.getCustomerId());
        newSmartMoneyAccountModel.setDefAccount(Boolean.TRUE);

        CustomList<SmartMoneyAccountModel> customList = this.smartMoneyAccountDAO.findByExample(newSmartMoneyAccountModel);
        List<SmartMoneyAccountModel> list = customList.getResultsetList();
        for (SmartMoneyAccountModel localSmartMoneyAccountModel : list) {
            if (!localSmartMoneyAccountModel.getSmartMoneyAccountId().equals(smartMoneyAccountModel.getSmartMoneyAccountId())) {
                localSmartMoneyAccountModel.setDefAccount(Boolean.FALSE);
                this.smartMoneyAccountDAO.saveOrUpdate(localSmartMoneyAccountModel);
            }
        }
        baseWrapper.setBasePersistableModel(this.smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel));
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.setDefaultSmartMoneyAccount()");
        }
        return baseWrapper;
    }

    public SmartMoneyAccountModel getSmartMoneyAccountByWalkinCustomerId(SmartMoneyAccountModel smartMoneyAccountModel) {

        return smartMoneyAccountDAO.getSmartMoneyAccountByWalkinCustomerId(smartMoneyAccountModel);
    }

    public BaseWrapper loadDistributorContact(BaseWrapper baseWrapper) throws
            FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadDistributorContact()");
        }
        DistributorContactModel distributorContactModel = this.distributorContactDAO.findByPrimaryKey(
                baseWrapper.getBasePersistableModel().getPrimaryKey());
        baseWrapper.setBasePersistableModel(distributorContactModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadDistributorContact()");
        }
        return baseWrapper;

    }

    public BaseWrapper loadRetailerContact(BaseWrapper baseWrapper) throws
            FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadRetailerContact()");
        }
        RetailerContactModel retailerContactModel = this.retailerContactDAO.
                findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
        baseWrapper.setBasePersistableModel(retailerContactModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadRetailerContact()");
        }
        return baseWrapper;
    }

    public BaseWrapper loadHandler(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadHandler()");
        }

        HandlerModel HandlerModel = this.handlerDAO.findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
        baseWrapper.setBasePersistableModel(HandlerModel);

        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadHandler()");
        }

        return baseWrapper;
    }

    public void loadHandlerData(Long handlerId, WorkFlowWrapper wrapper) throws FrameworkCheckedException {

        HandlerModel handlerModel = this.handlerDAO.findByPrimaryKey(handlerId);
        wrapper.setHandlerModel(handlerModel);


        AppUserModel appUserModel = this.appUserDAO.loadAppUserByHandlerByQuery(handlerId);
        wrapper.setHandlerAppUserModel(appUserModel);

    }

    public AppUserModel loadAppUserByRetailerContractId(long retailerContactId) throws FrameworkCheckedException {
        return appUserDAO.findByRetailerContractId(retailerContactId);
    }

    public BaseWrapper loadRetailerContactOrHandlerByAppUserId(Long appUserId) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadRetailerContact()");
        }
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        RetailerContactModel retailerContactModel = this.retailerContactDAO.findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
        baseWrapper.setBasePersistableModel(retailerContactModel);

        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadRetailerContact()");
        }

        return baseWrapper;
    }

    public SearchBaseWrapper searchTransaction(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.searchTransaction()");
        }
        FetchTransactionListViewModel fetchTransactionListViewModel = (FetchTransactionListViewModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<FetchTransactionListViewModel> customList = this.fetchTransactionListViewDAO.findByExample(fetchTransactionListViewModel);
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.searchTransaction()");
        }
        return searchBaseWrapper;
    }

    public WorkFlowWrapper executeSaleCreditTransaction(WorkFlowWrapper workFlowWrapper) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.executeSaleCreditTransaction()");
        }
        workFlowWrapper = workFlowFacade.workflowProcess(workFlowWrapper);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.executeSaleCreditTransaction()");
        }
        return workFlowWrapper;
    }

    public SearchBaseWrapper fetchTransactions(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.fetchTransactions()");
        }
        FetchTransactionListViewModel fetTranModel = (FetchTransactionListViewModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<FetchTransactionListViewModel> customList;
        customList = this.fetchTransactionListViewDAO.findByExample(fetTranModel,
                searchBaseWrapper.
                        getPagingHelperModel(),
                searchBaseWrapper.getSortingOrderMap());
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.fetchTransactions()");
        }
        return searchBaseWrapper;
    }

    public SearchBaseWrapper fetchSalesTransactions(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.fetchSalesTransactions()");
        }
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(searchBaseWrapper.getBasePersistableModel());

        SalesSummaryListViewModel catalogVersionListViewModel = (SalesSummaryListViewModel) searchBaseWrapper.getBasePersistableModel();
        List<SalesSummaryListViewModel> list =
                this.salesSummaryListViewDAO.getSalesSummary(baseWrapper);

        System.out.println("------> list size : " + list.size());
        System.out.println("------> app user id : " + catalogVersionListViewModel.getAppUserId());
        System.out.println("------> date : " + catalogVersionListViewModel.getTransactionDate());

        CustomList<SalesSummaryListViewModel> customList = new CustomList<SalesSummaryListViewModel>(list);
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.fetchSalesTransactions()");
        }
        return searchBaseWrapper;
    }

    public BaseWrapper loadCustomer(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadCustomer()");
        }
        CustomerModel customerModel = this.customerDAO.
                findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
        baseWrapper.setBasePersistableModel(customerModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadCustomer()");
        }
        return baseWrapper;
    }

    public CustomerModel loadCustomer(CustomerModel _customerModel) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.k()");
        }

        CustomerModel customerModel = null;

        CustomList<CustomerModel> customList = this.customerDAO.findByExample(_customerModel, null);

        if (!customList.getResultsetList().isEmpty()) {

            customerModel = (CustomerModel) customList.getResultsetList().get(0);
        }

        return customerModel;
    }

    public CustomerModel saveOrUpdateCustomerModel(CustomerModel _customerModel) throws FrameworkCheckedException {

        return this.customerDAO.saveOrUpdate(_customerModel);
    }

    @Override
    public void saveOrUpdateCustomerPictureModel(List<CustomerPictureModel> list) throws FrameworkCheckedException {
        customerPictureDAO.saveOrUpdateCollection(list);
    }

    @Override
    public void saveOrUpdateBlinkCustomerPictureModel(List<BlinkCustomerPictureModel> list) throws FrameworkCheckedException {
        blinkCustomerPictureDAO.saveOrUpdateCollection(list);
    }

    @Override
    public void saveOrUpdateBlinkCustomerPictureModel(BlinkCustomerPictureModel blinkCustomerPictureModel) throws FrameworkCheckedException {
        blinkCustomerPictureDAO.saveOrUpdate(blinkCustomerPictureModel);
    }

    @Override
    public void saveOrUpdateMerchantAccountPictureModel(List<MerchantAccountPictureModel> list) throws FrameworkCheckedException {
        merchantAccountPictureDAO.saveOrUpdateCollection(list);
    }

    @Override
    public void saveOrUpdateMerchantAccountPictureModel(MerchantAccountPictureModel merchantAccountPictureModel) throws FrameworkCheckedException {
        merchantAccountPictureDAO.saveOrUpdate(merchantAccountPictureModel);
    }

//	public SwitchWrapper checkAccountBalance(SwitchWrapper switchWrapper) throws FrameworkCheckedException
//	{
//		if(logger.isDebugEnabled())
//		{
//			logger.debug("Start of CommonCommandManagerImpl.checkAccountBalance()");
//		}
//		try
//        {
//			switchWrapper = this.switchController.checkBalance(switchWrapper);
//        }
//        catch (WorkFlowException ex)
//        {
//        	if (ex instanceof WorkFlowException)
//            {
//        		throw new FrameworkCheckedException( this.workflowExceptionTranslator.translateWorkFlowException(ex,
//                this.workflowExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION).getMessage() );
//                  }
//
//                }
//                if(logger.isDebugEnabled())
//        		{
//        			logger.debug("End of CommonCommandManagerImpl.checkAccountBalance()");
//        		}
//		return switchWrapper;
//	}

    public RetailerContactModel saveOrUpdateRetailerContactModel(RetailerContactModel retailerContactModel) throws FrameworkCheckedException {

        return this.retailerContactDAO.saveOrUpdate(retailerContactModel);
    }

    public BaseWrapper loadDistributor(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadDistributor()");
        }
        DistributorModel distributorModel = this.distributorDAO.
                findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
        baseWrapper.setBasePersistableModel(distributorModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadDistributor()");
        }
        return baseWrapper;
    }

    public BaseWrapper loadRetailer(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadRetailer()");
        }
        RetailerModel retailerModel = this.retailerDAO.
                findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
        baseWrapper.setBasePersistableModel(retailerModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadRetailer()");
        }
        return baseWrapper;
    }

    public BaseWrapper searchProduct(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadProduct()");
        }

        List<ProductModel> list = this.productDAO.findByExample((ProductModel) baseWrapper.getBasePersistableModel(), null).getResultsetList();
        if (list != null && list.size() > 0) {
            baseWrapper.setBasePersistableModel(list.get(0));
        } else
            baseWrapper.setBasePersistableModel(null);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadProduct()");
        }
        return baseWrapper;
    }

    public BaseWrapper loadProduct(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadProduct()");
        }
        ProductModel productModel = this.productDAO.
                findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
        baseWrapper.setBasePersistableModel(productModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadProduct()");
        }
        return baseWrapper;
    }

    @Override
    public ProductModel loadProductByProductCodeAndAppUserTypeId(String productCode, Long appUserTypeId) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadProduct()");
        }
        ProductModel productModel = this.productDAO.loadProductByProductCode(productCode, appUserTypeId);

        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadProduct()");
        }
        return productModel;
    }

    public Double getCommissionAmount(WorkFlowWrapper _workFlowWrapper) {

        Long segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;

        if (_workFlowWrapper.getSegmentModel() != null && _workFlowWrapper.getSegmentModel().getSegmentId() != null) {

            segmentId = _workFlowWrapper.getSegmentModel().getSegmentId();
        }

        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setSegmentId(segmentId);
        _workFlowWrapper.setSegmentModel(segmentModel);

        Double _commissionAmount = 0.0D;
        Long _commissionReasonId = 4L;

        try {

            CommissionWrapper commissionWrapper = calculateCommission(_workFlowWrapper);

            List<CommissionRateModel> commissionRateModelArray = (ArrayList<CommissionRateModel>) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);

            if (commissionRateModelArray != null && !commissionRateModelArray.isEmpty()) {

                CommissionRateModel commissionRateModel = commissionRateModelArray.get(0);

                if (commissionRateModel.getActive()) {
                    _commissionAmount = commissionRateModelArray.get(0).getRate();
                    _commissionReasonId = commissionRateModel.getCommissionReasonId();
                }

            } else {
                logger.error("No Commission Amount Defined or Check Transaction Amount vs Commission Rate's range");
            }

            _workFlowWrapper.setCommissionWrapper(commissionWrapper);

        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }

        _workFlowWrapper.putObject("REASON_ID", _commissionReasonId);

        return _commissionAmount;
    }

    public SearchBaseWrapper getCommissionRateData(CommissionRateModel commissionRateModel) throws FrameworkCheckedException {

        return commissionManager.getCommissionRateData(commissionRateModel);
    }

    public ProductVO loadProductVO(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start/End of CommonCommandManagerImpl.loadProductVO()");
        }
        return this.productDispenseController.loadProductVO(baseWrapper);
    }

    public ProductDispenser loadProductDispense(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start/End of CommonCommandManagerImpl.loadProductDispense()");
        }
        return this.productDispenseController.loadProductDispenser(workFlowWrapper);
    }

    public WorkFlowWrapper getBillInfo(BillPaymentProductDispenser billSale, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.getBillInfo()");
        }
        workFlowWrapper = billSale.getBillInfo(workFlowWrapper);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.getBillInfo()");
        }
        return workFlowWrapper;
    }

    public WorkFlowWrapper loadBillInfo(ProductDispenser productDispenser) {
        return null;
    }

    public BaseWrapper loadPaymentMode(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadPaymentMode()");
        }
        PaymentModeModel paymentModeModel = this.paymentModeDAO.
                findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey());
        baseWrapper.setBasePersistableModel(paymentModeModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadPaymentMode()");
        }
        return baseWrapper;
    }

    public BaseWrapper loadOperator(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadOperator()");
        }
        OperatorModel operatorModel = (OperatorModel) baseWrapper.getBasePersistableModel();
        CustomList<OperatorModel> customList = this.operatorDAO.findAll();
        operatorModel = customList.getResultsetList().get(0);
        baseWrapper.setBasePersistableModel(operatorModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadOperator()");
        }
        return baseWrapper;
    }

    public BaseWrapper createMfsAccountThroughSMS(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.createMfsAccountThroughSMS()");
        }
        baseWrapper = this.mfsAccountManager.createMfsAccountThroughSMS(baseWrapper);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.createMfsAccountThroughSMS()");
        }
        return baseWrapper;
    }

    public BaseWrapper sendSMSToUser(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.sendSMSToUser()");
        }
        SmsMessage smsMessage = (SmsMessage) baseWrapper.getObject(CommandFieldConstants.KEY_SMS_MESSAGE);
        this.smsSender.send(smsMessage);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.sendSMSToUser()");
        }
        return baseWrapper;
    }

    public SearchBaseWrapper loadUserDeviceAccounts(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadUserDeviceAccounts()");
        }
        UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<UserDeviceAccountsModel> customList = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel, null, null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadUserDeviceAccounts()");
        }
        return searchBaseWrapper;
    }

    public SearchBaseWrapper loadUserDeviceAccountsListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.loadUserDeviceAccountsListView()");
        }
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setMatchMode(MatchMode.EXACT);
        UserDeviceAccountListViewModel userDeviceAccountListViewModel = (UserDeviceAccountListViewModel) searchBaseWrapper.getBasePersistableModel();

        CustomList<UserDeviceAccountListViewModel> customList = this.userDeviceAccountListViewDAO.findByExample(userDeviceAccountListViewModel, null, null, exampleHolder);
        //CustomList<UserDeviceAccountListViewModel> customList = this.userDeviceAccountListViewDAO.findByExample(userDeviceAccountListViewModel, null);
        searchBaseWrapper.setCustomList(customList);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.loadUserDeviceAccountsListView()");
        }
        return searchBaseWrapper;
    }

    public BaseWrapper updateMfsAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.updateMfsAccount()");
        }
        Date nowDate = new Date();
        String password = "";
        Date minYearFutureDate = PortalDateUtils.addYears(nowDate, this.minExpiryYear);
        Date maxYearFutureDate = PortalDateUtils.addYears(nowDate, this.maxExpiryYear);

        UserDeviceAccountListViewModel userDeviceAccountListViewModel = (UserDeviceAccountListViewModel) baseWrapper.getBasePersistableModel();

        UserDeviceAccountsModel userDeviceAccountsModel = this.userDeviceAccountsDAO.findByPrimaryKey(userDeviceAccountListViewModel.getUserDeviceAccountsId());

        if (userDeviceAccountsModel != null) {
            //System.out.println("userDeviceAccountsModel.getAppUserId() : "+userDeviceAccountsModel.getAppUserId());
            //System.out.println("userDeviceAccountsModel.getUserId() : "+userDeviceAccountsModel.getUserId());
            //System.out.println("userDeviceAccountsModel.getPin() : "+userDeviceAccountsModel.getPin());

            if (userDeviceAccountListViewModel.getExpiryDate() == null) {
                String randomPin = RandomUtils.generateRandom(4, false, true);
                password = randomPin;
                userDeviceAccountsModel.setPin(EncoderUtils.encodeToSha(password));

            }
            userDeviceAccountsModel.setAccountLocked(Boolean.FALSE);
            userDeviceAccountsModel.setUpdatedOn(new Date());

            if (userDeviceAccountListViewModel.getExpiryDate() == null) {
                userDeviceAccountsModel.setExpiryDate(minYearFutureDate);
            } else if (userDeviceAccountListViewModel.getAccountLocked() && userDeviceAccountListViewModel.getExpiryDate().getTime() <= nowDate.getTime()) {
                userDeviceAccountsModel.setExpiryDate(minYearFutureDate);
            } else if (userDeviceAccountListViewModel.getExpiryDate().getTime() >= nowDate.getTime() && userDeviceAccountListViewModel.getExpiryDate().getTime() <= minYearFutureDate.getTime()) {
                userDeviceAccountsModel.setExpiryDate(PortalDateUtils.addYears(userDeviceAccountListViewModel.getExpiryDate(), CommandConstants.PREPAID_YEARS_TO_ADD));
            } else if (userDeviceAccountListViewModel.getExpiryDate().getTime() > maxYearFutureDate.getTime()) {
                userDeviceAccountsModel.setExpiryDate(minYearFutureDate);
            } else if (userDeviceAccountListViewModel.getExpiryDate().getTime() > minYearFutureDate.getTime() && userDeviceAccountListViewModel.getExpiryDate().getTime() <= maxYearFutureDate.getTime()) {
                System.out.println("Greater Than One Year and Less Than Two Year");
                userDeviceAccountsModel.setExpiryDate(userDeviceAccountListViewModel.getExpiryDate());
            }
            userDeviceAccountsModel = this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);

            if (userDeviceAccountListViewModel.getExpiryDate() == null) {
                baseWrapper.putObject("Password", password);
//				System.out.println("Password in CommonCommandManager : "+password);
            }
            baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.updateMfsAccount()");
        }
        return baseWrapper;
    }

    public boolean checkExpiryDate(Date expiryDate) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.checkExpiryDate()");
        }
        Date nowDate = new Date();
        Date minYearFutureDate = PortalDateUtils.addYears(nowDate, this.minExpiryYear);
        Date maxYearFutureDate = PortalDateUtils.addYears(nowDate, this.maxExpiryYear);
        boolean expiryDateFlag = false;

        if (expiryDate == null) {
            expiryDateFlag = true;
        } else if (expiryDate.getTime() <= nowDate.getTime()) {
            expiryDateFlag = true;
        } else if (expiryDate.getTime() <= minYearFutureDate.getTime()) {
            expiryDateFlag = true;
        } else if (expiryDate.getTime() > maxYearFutureDate.getTime()) {
            expiryDateFlag = true;
        } else if (expiryDate.getTime() > minYearFutureDate.getTime() && expiryDate.getTime() <= maxYearFutureDate.getTime()) {
            expiryDateFlag = false;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.checkExpiryDate()");
        }
        return expiryDateFlag;
    }

    //	public void updateScheduleFundsTransferList(List<ScheduleFundsTransferDetailModel> sftList) throws FrameworkCheckedException
//	{
//		if(logger.isDebugEnabled())
//		{
//			logger.debug("Start of CommonCommandManagerImpl.updateScheduleFundsTransferList()");
//		}
//		scheduleFundsTransferDetailDao.updateScheduleFundsTransferList(sftList);
//		if(logger.isDebugEnabled())
//		{
//			logger.debug("End of CommonCommandManagerImpl.updateScheduleFundsTransferList()");
//		}
//	}
    public AbstractFinancialInstitution loadFinancialInstitution(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        AbstractFinancialInstitution abstractFinancialInstitution = null;
        if (baseWrapper.getBasePersistableModel() != null) {
            abstractFinancialInstitution = (AbstractFinancialInstitution) this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
        }
        return abstractFinancialInstitution;
    }

    @Override
    public void updateSmartMoneyAccountCardType(SmartMoneyAccountModel smartMoneyAccountModel) throws FrameworkCheckedException {
        smartMoneyAccountDAO.updateSmartMoneyAccountCardTypeId(smartMoneyAccountModel);
    }

    public AbstractFinancialInstitution loadFinancialInstitutionByClassName(String className) throws FrameworkCheckedException {

        return financialIntegrationManager.loadFinancialInstitutionByClassName(className);
    }

    public void setVeriflyController(VeriflyManagerService veriflyController) {
        this.veriflyController = veriflyController;
    }

    public void setProdCatalogDetailListViewDAO(ProdCatalogDetailListViewDAO prodCatalogDetailListViewDAO) {
        this.prodCatalogDetailListViewDAO = prodCatalogDetailListViewDAO;
    }

//	public void setAppVersionDAO(AppVersionDAO appVersionDAO)
//	{
//		this.appVersionDAO = appVersionDAO;
//	}

    public void setGenericDao(GenericDao genericDao) {
        this.genericDao = genericDao;
    }

    public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
        this.userDeviceAccountsDAO = userDeviceAccountsDAO;
    }

    public void setVeriflyListViewDAO(VeriflyListViewDAO veriflyListViewDAO) {
        this.veriflyListViewDAO = veriflyListViewDAO;
    }

    public void setTickerDAO(TickerDAO tickerDAO) {
        this.tickerDAO = tickerDAO;
    }

    public void setCommandDAO(CommandDAO commandDAO) {
        this.commandDAO = commandDAO;
    }

    public void setFavoriteNumbersDAO(FavoriteNumbersDAO favoriteNumbersDAO) {
        this.favoriteNumbersDAO = favoriteNumbersDAO;
    }

    public void setAppUserDAO(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

    public void setCatalogServiceListViewDAO(CatalogServiceListViewDAO catalogServiceListViewDAO) {
        this.catalogServiceListViewDAO = catalogServiceListViewDAO;
    }

    public void setSmAcctInfoListViewDAO(SMAcctInfoListViewDAO smAcctInfoListViewDAO) {
        this.smAcctInfoListViewDAO = smAcctInfoListViewDAO;
    }

    public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
        this.smartMoneyAccountDAO = smartMoneyAccountDAO;
    }

    public void setAppVersionListViewDAO(AppVersionListViewDAO appVersionListViewDAO) {
        this.appVersionListViewDAO = appVersionListViewDAO;
    }

    public void setDistributorContactDAO(DistributorContactDAO distributorContactDAO) {
        this.distributorContactDAO = distributorContactDAO;
    }

    public void setRetailerContactDAO(RetailerContactDAO retailerContactDAO) {
        this.retailerContactDAO = retailerContactDAO;
    }

    public void setWorkFlowFacade(WorkFlowFacade workFlowFacade) {
        this.workFlowFacade = workFlowFacade;
    }

    public void setFetchTransactionListViewDAO(FetchTransactionListViewDAO fetchTransactionListViewDAO) {
        this.fetchTransactionListViewDAO = fetchTransactionListViewDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void setDistributorDAO(DistributorDAO distributorDAO) {
        this.distributorDAO = distributorDAO;
    }

    public void setRetailerDAO(RetailerDAO retailerDAO) {
        this.retailerDAO = retailerDAO;
    }

    public void setSwitchController(SwitchController switchController) {
        this.switchController = switchController;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void setProductDispenseController(ProductDispenseController productDispenseController) {
        this.productDispenseController = productDispenseController;
    }

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }

    public void setPaymentModeDAO(PaymentModeDAO paymentModeDAO) {
        this.paymentModeDAO = paymentModeDAO;
    }

    public void setWorkflowExceptionTranslator(WorkFlowExceptionTranslator workflowExceptionTranslator) {
        this.workflowExceptionTranslator = workflowExceptionTranslator;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setOperatorDAO(OperatorDAO operatorDAO) {
        this.operatorDAO = operatorDAO;
    }

    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public SmsSender getSmsSender() {
        return smsSender;
    }

    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    public void setUserDeviceAccountListViewDAO(UserDeviceAccountListViewDAO userDeviceAccountListViewDAO) {
        this.userDeviceAccountListViewDAO = userDeviceAccountListViewDAO;
    }

    public void setFavoriteNumberListViewDAO(FavoriteNumberListViewDAO favoriteNumberListViewDAO) {
        this.favoriteNumberListViewDAO = favoriteNumberListViewDAO;
    }

    public void setMaxExpiryYear(int maxExpiryYear) {
        this.maxExpiryYear = maxExpiryYear;
    }

    public void setMinExpiryYear(int minExpiryYear) {
        this.minExpiryYear = minExpiryYear;
    }

    public void setAppVersionDAO(AppVersionDAO appVersionDAO) {
        this.appVersionDAO = appVersionDAO;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public boolean isAccountValidationRequired(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.isAccountValidationRequired()");
        }
        boolean accountValidityFlag = false;

        Long accountId = Long.parseLong(baseWrapper.getObject(CommandFieldConstants.KEY_ACC_ID).toString());

        if (accountId != null) {
            SmAcctInfoListViewModel smAcctInfoListViewModel = this.smAcctInfoListViewDAO.findByPrimaryKey(accountId);
            if (smAcctInfoListViewModel != null) {
                if (null == smAcctInfoListViewModel.getVeriflyId()) {
                    accountValidityFlag = true;
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.isAccountValidationRequired()");
        }
        return accountValidityFlag;
    }

    public BaseWrapper updateFavoriteNumber(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.updateFavoriteNumber()");
        }

        ExampleConfigHolderModel example = new ExampleConfigHolderModel();
        example.setMatchMode(MatchMode.EXACT);


        FavoriteNumbersModel favoriteNumbersModel = (FavoriteNumbersModel) baseWrapper.getBasePersistableModel();
        FavoriteNumbersModel localFavoriteNumbersModel = new FavoriteNumbersModel();
        localFavoriteNumbersModel.setName(favoriteNumbersModel.getName());
        localFavoriteNumbersModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());

        // Check Favorite number's Tag Uniqueness
        List<FavoriteNumbersModel> favList = this.favoriteNumbersDAO.findByExample(localFavoriteNumbersModel, null, null, example).getResultsetList();
        if (favList.size() > 0) {
            localFavoriteNumbersModel = favList.get(0);
            if (localFavoriteNumbersModel.getFavoriteNumbersId().longValue() != favoriteNumbersModel.getFavoriteNumbersId().longValue()) {
                throw new FrameworkCheckedException("Favorite Name should be Unique.");
            }
        }

        localFavoriteNumbersModel = new FavoriteNumbersModel();
        localFavoriteNumbersModel.setFavoriteNumber(favoriteNumbersModel.getFavoriteNumber());
        localFavoriteNumbersModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());

        // Check Favorite number Uniqueness

        favList = this.favoriteNumbersDAO.findByExample(localFavoriteNumbersModel, null, null, example).getResultsetList();
        if (favList.size() > 0) {
            localFavoriteNumbersModel = favList.get(0);
            if (localFavoriteNumbersModel.getFavoriteNumbersId().longValue() != favoriteNumbersModel.getFavoriteNumbersId().longValue()) {
                throw new FrameworkCheckedException("Favorite Number should be Unique.");
            }
        }


        if (favoriteNumbersModel != null) {
            localFavoriteNumbersModel = this.favoriteNumbersDAO.findByPrimaryKey(favoriteNumbersModel.getPrimaryKey());
            localFavoriteNumbersModel.setName(favoriteNumbersModel.getName());
            localFavoriteNumbersModel.setFavoriteNumber(favoriteNumbersModel.getFavoriteNumber());
            localFavoriteNumbersModel.setUpdatedOn(new Date());
            localFavoriteNumbersModel.setUpdatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());

            try {
                localFavoriteNumbersModel = this.favoriteNumbersDAO.saveOrUpdate(localFavoriteNumbersModel);
            } catch (DataAccessException e) {
                e.printStackTrace();
            } catch (ConstraintViolationException e) {
                e.printStackTrace();
            }
            baseWrapper.setBasePersistableModel(localFavoriteNumbersModel);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.updateFavoriteNumber()");
        }
        return baseWrapper;
    }

    public BaseWrapper addFavoriteNumbers(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.createFavoriteNumbers()");
        }

        ExampleConfigHolderModel example = new ExampleConfigHolderModel();
        example.setMatchMode(MatchMode.EXACT);


        FavoriteNumbersModel favoriteNumbersModel = (FavoriteNumbersModel) baseWrapper.getBasePersistableModel();
        FavoriteNumbersModel localFavoriteNumbersModel = new FavoriteNumbersModel();
        localFavoriteNumbersModel.setName(favoriteNumbersModel.getName());
        localFavoriteNumbersModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());

        // Check Favorite number's Tag Uniqueness
        List<FavoriteNumbersModel> favList = this.favoriteNumbersDAO.findByExample(localFavoriteNumbersModel, null, null, example).getResultsetList();
        if (favList.size() > 0) {
            localFavoriteNumbersModel = favList.get(0);
            if (null != localFavoriteNumbersModel) {
                throw new FrameworkCheckedException("Favorite Name should be Unique.");
            }
        }

        localFavoriteNumbersModel = new FavoriteNumbersModel();
        localFavoriteNumbersModel.setFavoriteNumber(favoriteNumbersModel.getFavoriteNumber());
        localFavoriteNumbersModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());

        // Check Favorite number Uniqueness

        favList = this.favoriteNumbersDAO.findByExample(localFavoriteNumbersModel, null, null, example).getResultsetList();
        if (favList.size() > 0) {
            localFavoriteNumbersModel = favList.get(0);
            if (null != localFavoriteNumbersModel) {
                throw new FrameworkCheckedException("Favorite Number should be Unique.");
            }
        }

        FavoriteNumbersModel favNumModel = (FavoriteNumbersModel) baseWrapper.getBasePersistableModel();
        favNumModel.setCreatedBy(favNumModel.getAppUserId());
        favNumModel.setUpdatedBy(favNumModel.getAppUserId());
        favNumModel.setCreatedOn(new Date());
        favNumModel.setUpdatedOn(new Date());
        favNumModel.setSequenceNumber(favoriteNumbersDAO.getLatestSequenceNumber(favNumModel.getAppUserId()) + 1);
        favoriteNumbersDAO.saveOrUpdate(favNumModel);
        return baseWrapper;
    }

    public void deleteFavoriteNumber(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.deleteFavoriteNumber()");
        }

        FavoriteNumbersModel favoriteNumbersModel = (FavoriteNumbersModel) baseWrapper.getBasePersistableModel();

        if (favoriteNumbersModel != null) {
            this.favoriteNumbersDAO.deleteByPrimaryKey(favoriteNumbersModel.getFavoriteNumbersId());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.deleteFavoriteNumber()");
        }
    }

    public void setSalesSummaryListViewDAO(SalesSummaryListViewDAO salesSummaryListViewDAO) {
        this.salesSummaryListViewDAO = salesSummaryListViewDAO;
    }

    public SearchBaseWrapper loadMiniUserDeviceAccountsListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
//		((UserDeviceAccountListViewModel)searchBaseWrapper.getBasePersistableModel()).setDeviceTypeId( DeviceTypeConstantsInterface.USSD ) ;
        return this.loadUserDeviceAccountsListView(searchBaseWrapper);
    }

    public BaseWrapper loadUserDeviceAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
        userDeviceAccountsModel = this.userDeviceAccountsDAO.findByPrimaryKey(userDeviceAccountsModel.getPrimaryKey());
        baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        return baseWrapper;
    }

    public BaseWrapper loadUserDeviceAccountByMobileNumber(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel((AppUserModel) baseWrapper.getBasePersistableModel());
        searchBaseWrapper = this.loadAppUserByMobileNumberAndType(searchBaseWrapper);
        AppUserModel appUserModel = (AppUserModel) searchBaseWrapper.getBasePersistableModel();
        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
        CustomList<UserDeviceAccountsModel> list = this.userDeviceAccountsDAO.findByExample(userDeviceAccountsModel);
        if (null != list && null != list.getResultsetList()) {
            userDeviceAccountsModel = list.getResultsetList().get(0);
        }

        baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        return baseWrapper;
    }

    public SearchBaseWrapper loadMiniCommand(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setIgnoreCase(true);
        exampleHolder.setMatchMode(MatchMode.EXACT);
        CustomList<MiniCommandKeywordModel> customList = this.miniCommandKeywordDAO.findByExample((MiniCommandKeywordModel) searchBaseWrapper.getBasePersistableModel(), null, null, exampleHolder);
        searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    public BaseWrapper loadMiniCommand(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        MiniCommandKeywordModel miniCommandKeywordModel = this.miniCommandKeywordDAO.findByPrimaryKey(((MiniCommandKeywordModel) baseWrapper.getBasePersistableModel()).getPrimaryKey());
        baseWrapper.setBasePersistableModel(miniCommandKeywordModel);
        return baseWrapper;
    }

    public SearchBaseWrapper loadMiniHelpKeyword(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setMatchMode(MatchMode.EXACT);
        CustomList<MiniHelpKeywordModel> customList = this.miniHelpKeywordDAO.findByExample((MiniHelpKeywordModel) searchBaseWrapper.getBasePersistableModel(), null, null, exampleHolder);
        searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    public SearchBaseWrapper loadSMAExactMatch(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException {
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setMatchMode(MatchMode.EXACT);
        exampleHolder.setEnableLike(Boolean.FALSE);
        exampleHolder.setIgnoreCase(Boolean.FALSE);
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<SmartMoneyAccountModel> customList = this.smartMoneyAccountDAO.findByExample(smartMoneyAccountModel, null, null, exampleHolder);
        searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    public SearchBaseWrapper getAppUserPeers(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException {
//		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
//		exampleHolder.setIgnoreCase(true);
//		exampleHolder.setMatchMode(MatchMode.EXACT);
//		AppUserPeerModel appUserPeerModel = (AppUserPeerModel)searchBaseWrapper.getBasePersistableModel();
//		CustomList<AppUserPeerModel> customList = this.appUserPeerDAO.findByExample(appUserPeerModel,null,searchBaseWrapper.getSortingOrderMap(),exampleHolder);
//		searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    public SearchBaseWrapper getUserRegServices(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException {
//		UserServiceModel userServiceModel = (UserServiceModel)searchBaseWrapper.getBasePersistableModel();
//		CustomList<UserServiceModel> customList = this.userServiceDAO.findByExample(userServiceModel,null,searchBaseWrapper.getSortingOrderMap());
//		searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    public SearchBaseWrapper getUserService(SearchBaseWrapper searchBaseWrapper) throws
            FrameworkCheckedException {
//		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
//		exampleHolder.setMatchMode(MatchMode.EXACT);
//		UserServiceCustFldViewModel userServiceCustFldViewModel = (UserServiceCustFldViewModel)searchBaseWrapper.getBasePersistableModel();
//		CustomList<UserServiceCustFldViewModel> customList = this.userServiceCustFldViewDAO.findByExample(userServiceCustFldViewModel,null,null,exampleHolder);
//		searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    public BaseWrapper saveMiniTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        MiniTransactionModel miniTransactionModel = (MiniTransactionModel) baseWrapper.getBasePersistableModel();

        miniTransactionModel.setCreatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
        miniTransactionModel.setUpdatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
        miniTransactionModel.setCreatedOn(new Date());
        miniTransactionModel.setUpdatedOn(new Date());

        baseWrapper.setBasePersistableModel(this.miniTransactionDAO.saveOrUpdate(miniTransactionModel));
        return baseWrapper;
    }

    public BaseWrapper updateMiniTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        MiniTransactionModel miniTransactionModel = (MiniTransactionModel) baseWrapper.getBasePersistableModel();

        miniTransactionModel.setUpdatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
        miniTransactionModel.setUpdatedOn(new Date());

        baseWrapper.setBasePersistableModel(this.miniTransactionDAO.saveOrUpdate(miniTransactionModel));
        return baseWrapper;
    }

    public BaseWrapper modifyPINSentMiniTransToExpired(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        MiniTransactionModel miniTransactionModel = (MiniTransactionModel) baseWrapper.getBasePersistableModel();
        CustomList<MiniTransactionModel> miniTransactionModelList = this.miniTransactionDAO.findByExample(miniTransactionModel);
        Iterator<MiniTransactionModel> ite = miniTransactionModelList.getResultsetList().iterator();

        while (ite.hasNext()) {
            miniTransactionModel = ite.next();
            miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.EXPIRED);
            miniTransactionModel.setUpdatedOn(new Date());
            this.miniTransactionDAO.saveOrUpdate(miniTransactionModel);
        }
        return baseWrapper;
    }

    public SearchBaseWrapper loadMiniTransaction(SearchBaseWrapper baseWrapper) throws FrameworkCheckedException {
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setMatchMode(MatchMode.EXACT);

        MiniTransactionModel miniTransactionModel = (MiniTransactionModel) baseWrapper.getBasePersistableModel();
        baseWrapper.setCustomList(this.miniTransactionDAO.findByExample(miniTransactionModel, null, null, exampleHolder));
        return baseWrapper;
    }

    public BaseWrapper loadTransactionCodeByCode(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        return this.transactionManager.loadTransactionCodeByCode(baseWrapper);

    }

    public SearchBaseWrapper loadTransactionByTransactionCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside loadTransactionByTransactionCode method of TransactionManagerImpl...");
        }
        TransactionCodeModel model = (TransactionCodeModel) searchBaseWrapper.getBasePersistableModel();
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setTransactionCodeId(model.getTransactionCodeId());
        CustomList customList = transactionDAO.findByExample(transactionModel);
        if (customList != null && customList.getResultsetList() != null && !customList.getResultsetList().isEmpty()) {
            transactionModel = (TransactionModel) customList.getResultsetList().get(0);
            searchBaseWrapper.setBasePersistableModel(transactionModel);
        } else {
            searchBaseWrapper.setBasePersistableModel(null);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Ending loadTransactionByTransactionCode method of TransactionManagerImpl...");
        }
        return searchBaseWrapper;

    }

    public SearchBaseWrapper loadMiniTransactionByTransactionCode(SearchBaseWrapper baseWrapper) throws FrameworkCheckedException {


        MiniTransactionModel miniTransactionModel = (MiniTransactionModel) baseWrapper.getBasePersistableModel();
        baseWrapper.setCustomList(this.miniTransactionDAO.findByExample(miniTransactionModel));
        return baseWrapper;
    }

    @Override
    public MiniTransactionModel loadMiniTransactionByTransactionCode(String transactionCode) throws FrameworkCheckedException {
        return miniTransactionDAO.loadMiniTransactionByTransactionCode(transactionCode);
    }

    public Boolean isCVVReqForSMA(BaseWrapper baseWrapper) throws FrameworkCheckedException {
//		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();
//
//		SwitchFinderModel switchFinderModel = new SwitchFinderModel();
//		switchFinderModel.setBankId( smartMoneyAccountModel.getBankId() ) ;
//		switchFinderModel.setPaymentModeId( smartMoneyAccountModel.getPaymentModeId() ) ;
//		CustomList<SwitchFinderModel> customList = this.switchFinderDAO.findByExample(switchFinderModel, null);
//
//		if( customList.getResultsetList().size() > 0 )
//		{
//			switchFinderModel = customList.getResultsetList().get(0) ;
//			SwitchModel switchModel = this.switchDAO.findByPrimaryKey( switchFinderModel.getSwitchId() ) ;
//			return switchModel.getCvvRequired() ;
//		}
//		throw new FrameworkCheckedException("");

        return false;
    }

    public VeriflyBaseWrapper verifyVeriflyOneTimePin(VeriflyManager veriflyManager, VeriflyBaseWrapper veriflyBaseWrapper) throws Exception {
        veriflyBaseWrapper = veriflyManager.verifyOneTimePin(veriflyBaseWrapper);
        return veriflyBaseWrapper;
    }

    public VeriflyBaseWrapper generateOneTimeVeriflyPIN(VeriflyManager veriflyManager, VeriflyBaseWrapper veriflyBaseWrapper) throws Exception {
        veriflyBaseWrapper = veriflyManager.generateOneTimePin(veriflyBaseWrapper);
        return veriflyBaseWrapper;
    }

    public BaseWrapper saveMiniCommandLogRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        MiniCommandLogModel miniCommandLogModel = (MiniCommandLogModel) baseWrapper.getBasePersistableModel();
        baseWrapper.setBasePersistableModel(this.miniCommandLogDAO.saveOrUpdate(miniCommandLogModel));
        return baseWrapper;
    }

    public BaseWrapper updateMiniTransactionRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        MiniTransactionModel miniTransactionModel = (MiniTransactionModel) baseWrapper.getBasePersistableModel();

        if (ThreadLocalAppUser.getAppUserModel() != null && ThreadLocalAppUser.getAppUserModel().getAppUserId() != null)
            miniTransactionModel.setUpdatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
        miniTransactionModel.setUpdatedOn(new Date());

        baseWrapper.setBasePersistableModel(this.miniTransactionDAO.saveOrUpdate(miniTransactionModel));
        return baseWrapper;
    }

    public SearchBaseWrapper searchAppUserByMobile(SearchBaseWrapper searchBaseWrapper) {
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setMatchMode(MatchMode.EXACT);

        CustomList<AppUserModel> list = this.appUserDAO.findByExample((AppUserModel) searchBaseWrapper.getBasePersistableModel(), searchBaseWrapper
                .getPagingHelperModel(), null, exampleHolder);
        searchBaseWrapper.setCustomList(list);
        return searchBaseWrapper;
    }

    public void setTransactionManager(TransactionModuleManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    //Added by atiq butt

    public BaseWrapper generateTransactionCode() {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
        deviceTypeModel.setDeviceTypeId(DeviceTypeConstantsInterface.USSD);
        WorkFlowWrapper workflowWrapper = new WorkFlowWrapperImpl();
        workflowWrapper.setDeviceTypeModel(deviceTypeModel);
        try {
            WorkFlowWrapper wrapper = transactionManager.generateTransactionCodeRequiresNewTransaction(workflowWrapper);
            if (null != wrapper && null != wrapper.getTransactionCodeModel()) {
                TransactionCodeModel transactionCodeModel = wrapper.getTransactionCodeModel();
                baseWrapper.setBasePersistableModel(transactionCodeModel);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return baseWrapper;

    }

    public BaseWrapper updateUserDeviceAccounts(BaseWrapper baseWrapper)
            throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.updateUserDeviceAccounts()");
        }
        UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
        this.userDeviceAccountsDAO.saveOrUpdate(userDeviceAccountsModel);
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.updateUserDeviceAccounts()");
        }
        return baseWrapper;
    }

    /**
     * @param CNIC
     * @param mobileNumber
     * @param senderMobileNumber
     * @return
     * @throws FrameworkCheckedException
     */
    public Boolean isAlreadyRegistered(String CNIC, String mobileNumber, String senderMobileNumber) throws FrameworkCheckedException {

        Boolean isAlreadyRegistered = mfsAccountManager.isAlreadyRegistered(CNIC, mobileNumber);

        if (!isAlreadyRegistered) {

            isAlreadyRegistered = createUpdateWalkinCustomerIfExists(CNIC, mobileNumber, senderMobileNumber);
        }

        return isAlreadyRegistered;
    }

    public Boolean isAlreadyRegisteredWalkIn(String CNIC, String mobileNumber, String senderMobileNumber) throws FrameworkCheckedException {

        Boolean isAlreadyRegistered = mfsAccountManager.isAlreadyRegistered(CNIC, mobileNumber);

        if (!isAlreadyRegistered) {

            isAlreadyRegistered = createWalkinCustomer(CNIC, mobileNumber, senderMobileNumber);
        }

        return isAlreadyRegistered;
    }

    /**
     * @param CNIC
     * @return
     * @throws FrameworkCheckedException
     */
    public Boolean isCustomerAlreadyRegistered(String CNIC) throws FrameworkCheckedException {

        return mfsAccountManager.isAlreadyRegistered(CNIC);
    }

    //Added by atiq butt

    /**
     * This method is copy of sAlreadyRegistered(..) to make solve readOnly transaction issue ( <prop key="is*">PROPAGATION_REQUIRED,readOnly</prop> in conf.)
     *
     * @param CNIC
     * @param mobileNumber
     * @param senderMobileNumber
     * @return
     * @throws FrameworkCheckedException
     */
    public Boolean createOrUpdateWalkinCustomer(String CNIC, String mobileNumber, String senderMobileNumber) throws FrameworkCheckedException {

        return isAlreadyRegistered(CNIC, mobileNumber, senderMobileNumber);

    }

    //Added by atiq butt
    public Boolean createNewWalkinCustomer(String CNIC, String mobileNumber, String senderMobileNumber) throws FrameworkCheckedException {

        return isAlreadyRegisteredWalkIn(CNIC, mobileNumber, senderMobileNumber);

    }

    /* (non-Javadoc)
     * @see com.inov8.microbank.server.service.mfsmodule.CommonCommandManager#isWalkinCustomerExists(com.inov8.framework.common.wrapper.BaseWrapper)
     */
    public Boolean createUpdateWalkinCustomerIfExists(String walkInCNIC, String walkInMobileNumber, String senderMobileNumber) throws FrameworkCheckedException {

        Boolean isWalkinCustomerExists = null;

        try {

            isWalkinCustomerExists = mfsAccountManager.updateWalkinCustomerIfExists(walkInCNIC, walkInMobileNumber, senderMobileNumber);

            if (isWalkinCustomerExists == false) {//need to create new walkin customer account.
                mfsAccountManager.createWalkinCustomerAccount(walkInCNIC, walkInMobileNumber, senderMobileNumber);

            }
            //TODO: this dirty little logic is to be rectified as this flag is being used in UssdValidator/Cash2CashController for checking if it is already registered as BB Customer/Retailer.
            //This flag is being used as BB customer/Retailer existance.
            isWalkinCustomerExists = Boolean.FALSE;

        } catch (Exception e) {
            logger.error(e);
            throw new FrameworkCheckedException(e.getLocalizedMessage());
        }

        return isWalkinCustomerExists;
    }

    public Boolean createWalkinCustomer(String walkInCNIC, String walkInMobileNumber, String senderMobileNumber) throws FrameworkCheckedException {

        Boolean isWalkinCustomerExists = null;

        try {

            mfsAccountManager.createWalkinCustomerAccount(walkInCNIC, walkInMobileNumber, senderMobileNumber);

            //TODO: this dirty little logic is to be rectified as this flag is being used in UssdValidator/Cash2CashController for checking if it is already registered as BB Customer/Retailer.
            //This flag is being used as BB customer/Retailer existance.
            isWalkinCustomerExists = Boolean.FALSE;

        } catch (Exception e) {
            logger.error(e);
            throw new FrameworkCheckedException(e.getLocalizedMessage());
        }

        return isWalkinCustomerExists;
    }

    public WalkinCustomerModel getWalkinCustomerModel(WalkinCustomerModel _walkinCustomerModel) throws FrameworkCheckedException {

        return mfsAccountManager.getWalkinCustomerModel(_walkinCustomerModel);
    }

    /**
     * @param AllpayTransInfoListViewModel
     * @return CustomList
     * @throws FrameworkCheckedException
     */
    public List<TransactionDetailPortalListModel> getMiniStatementTransactionList(ExtendedTransactionDetailPortalListModel model, Integer fetchSize) throws FrameworkCheckedException {

        return allPayTransactionInfoListViewDAO.getMiniStatementTransactionList(model, fetchSize);
    }

    /**
     * @param model
     * @param fetchSize
     * @return
     * @throws FrameworkCheckedException
     */
    public List<MiniStatementListViewModel> getMiniStatementListViewModelList(MiniStatementListViewModel model, Integer fetchSize) throws FrameworkCheckedException {

        return miniStatementListViewDAO.getMiniStatementListViewModelList(model, fetchSize);
    }

    public TransactionDetailModel saveTransactionDetailModel(TransactionDetailModel transactionDetailModel) {

        transactionDetailDAO.saveOrUpdate(transactionDetailModel);
        return transactionDetailModel;

    }

    public TransactionDetailMasterModel saveTransactionDetailMasterModel(TransactionDetailMasterModel transactionDetailMasterModel) {
        transactionDetailMasterDAO.saveOrUpdate(transactionDetailMasterModel);
        return transactionDetailMasterModel;
    }

    public SearchBaseWrapper loadTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside loadTransactionDetail method of CommonCommandManagerImpl...");
        }
        TransactionModel txModel = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
        TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
        transactionDetailModel.setTransactionId(txModel.getTransactionId());
        CustomList list = transactionDetailDAO.findByExample(transactionDetailModel);
        if (null != list && null != list.getResultsetList()) {
            transactionDetailModel = (TransactionDetailModel) list.getResultsetList().get(0);
            searchBaseWrapper.setBasePersistableModel(transactionDetailModel);
        } else {
            searchBaseWrapper.setBasePersistableModel(null);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Ending loadTransactionDetail method of CommonCommandManagerImpl...");
        }
        return searchBaseWrapper;

    }

    public TransactionDetailModel loadTransactionDetailModel(String cnic, Long productId, Long supProcessingStatus) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside loadTransactionDetail method of CommonCommandManagerImpl...");
        }

        return transactionDetailDAO.loadTransactionDetailModel(cnic, productId, supProcessingStatus);

		/*TransactionDetailModel transactionDetailModel = (TransactionDetailModel)searchBaseWrapper.getBasePersistableModel();
		ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
		exampleHolder.setMatchMode(MatchMode.EXACT);
		exampleHolder.setEnableLike(Boolean.FALSE);
		exampleHolder.setIgnoreCase(Boolean.FALSE);

		CustomList<TransactionDetailModel> list = transactionDetailDAO.findByExample(transactionDetailModel);
		if (null != list && null != list.getResultsetList())
		{
			transactionDetailModel = (TransactionDetailModel) list.getResultsetList().get(0);
			searchBaseWrapper.setBasePersistableModel(transactionDetailModel);
		}
		else
		{
			searchBaseWrapper.setBasePersistableModel(null);
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending loadTransactionDetail method of CommonCommandManagerImpl...");
		}
		return searchBaseWrapper;*/

    }

    public SearchBaseWrapper loadTransactionDetailMaster(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside loadTransactionDetail method of CommonCommandManagerImpl...");
        }
        TransactionDetailMasterModel txMasterModel = (TransactionDetailMasterModel) searchBaseWrapper.getBasePersistableModel();
        CustomList list = transactionDetailMasterDAO.findByExample(txMasterModel);
        if (null != list && null != list.getResultsetList() && list.getResultsetList().size() > 0) {
            txMasterModel = (TransactionDetailMasterModel) list.getResultsetList().get(0);
            searchBaseWrapper.setBasePersistableModel(txMasterModel);
        } else {
            searchBaseWrapper.setBasePersistableModel(null);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Ending loadTransactionDetail method of CommonCommandManagerImpl...");
        }
        return searchBaseWrapper;

    }

    /**
     * @param appUserModel
     * @return
     * @throws FrameworkCheckedException
     */
    public Boolean titleFetch(AppUserModel appUserModel) throws FrameworkCheckedException {

        logger.info("titleFetch()");

        Boolean titleFetch = Boolean.FALSE;

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setBankId(CommissionConstantsInterface.BANK_ID);
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
        switchWrapper.setWorkFlowWrapper(new WorkFlowWrapperImpl());
        switchWrapper.setCustomerAccount(new CustomerAccount());
        switchWrapper.getCustomerAccount().setType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.getCustomerAccount().setCurrency(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);

        Long customerId = null;
        String titleOfTheAccount = null;

        if (appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {

            customerId = appUserModel.getAppUserId();

        } else if (appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

            customerId = appUserModel.getCustomerId();
        }

        try {

            String accountNumber = veriflyManager.getAccountNumber(customerId);
            switchWrapper.getCustomerAccount().setNumber(accountNumber);
            phoenixFinancialInstitution.titleFetch(switchWrapper);
            titleOfTheAccount = switchWrapper.getCustomerAccount().getTitleOfTheAccount();

            if (titleOfTheAccount == null) {
                throw new FrameworkCheckedException("Agent (" + appUserModel.getMobileNo() + ") is not Linked with Core Banking.");
            }

            titleFetch = Boolean.TRUE;

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
            throw new FrameworkCheckedException(e.getMessage());
        }

        return titleFetch;
    }

    public String fetchAccountTitle(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

        logger.info("[CommonCommandManagerImpl.fetchAccountTitle] fetching account title ");

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setBankId(CommissionConstantsInterface.BANK_ID);
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
        switchWrapper.setWorkFlowWrapper(workFlowWrapper);
        switchWrapper.setCustomerAccount(workFlowWrapper.getCustomerAccount());
        switchWrapper.getCustomerAccount().setType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.getCustomerAccount().setCurrency(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);

        String titleOfTheAccount = null;


        try {

            phoenixFinancialInstitution.titleFetch(switchWrapper);
            titleOfTheAccount = switchWrapper.getCustomerAccount().getTitleOfTheAccount();

            if (titleOfTheAccount == null) {
//				throw new FrameworkCheckedException("Agent ("+ThreadLocalAppUser.getAppUserModel().getMobileNo()+") is not Linked with Core Banking.");
            }

            if (workFlowWrapper.getSwitchWrapper() != null) {
                workFlowWrapper.getSwitchWrapper().setMiddlewareIntegrationMessageVO(switchWrapper.getMiddlewareIntegrationMessageVO());
            } else {
                workFlowWrapper.setSwitchWrapper(switchWrapper);
            }

        } catch (Exception e) {
            logger.error(e);
            throw new FrameworkCheckedException(e.getMessage());
        }

        return titleOfTheAccount;
    }

    public AccountInfoModel getAccountInfoModel(Long customerId, Long paymentModeId) throws Exception {

        return veriflyManager.getAccountInfoModel(customerId, paymentModeId);
    }

    @Override
    public int updateAccountInfoModel(Long customerId, Long paymentModeId, Long isMigrated) throws Exception {
        return this.accountInfoDAO.updateAccountInfoModel(customerId, paymentModeId, isMigrated);
    }

    public AccountInfoModel getAccountInfoModel(Long customerId, String accountNick) throws Exception {

        return veriflyManager.getAccountInfoModel(customerId, accountNick);
    }

    @Override
    public AccountInfoModel loadAccountInfoModel(AccountInfoModel accountInfoModel) throws Exception {
        ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
        exampleHolder.setEnableLike(Boolean.FALSE);
        exampleHolder.setMatchMode(MatchMode.EXACT);

        CustomList<AccountInfoModel> customList = this.accountInfoDAO.findByExample(accountInfoModel, null, null, exampleHolder);

        if (customList != null && customList.getResultsetList().isEmpty()) {
            throw new FrameworkCheckedException("BLB Account Not found.");
        }

        accountInfoModel = customList.getResultsetList().get(0);
        return accountInfoModel;
    }

    @Override
    public AppUserModel loadAppUserByMobileByQuery(String mobileNo)
            throws Exception {

        AppUserModel appUserModel = appUserManager.loadAppUserByMobileByQuery(mobileNo);
        return appUserModel;

    }

    @Override
    public AppUserModel loadAppUserByQuery(String mobileNo, Long appUserTypeId) throws Exception {
        if (null == appUserTypeId)
            return null;

        long id = appUserTypeId.longValue();
        if (id < 1)
            return null;

        return appUserManager.loadAppUserByQuery(mobileNo, id);
    }

    @Override
    public BaseWrapper loadAgentCommission(BaseWrapper baseWrapper)
            throws Exception {
        return commissionManager.loadAgentCommission(baseWrapper);
    }

    /* (non-Javadoc)
     * @see com.inov8.microbank.server.service.mfsmodule.CommonCommandManager#titleFetch(java.lang.String, com.inov8.microbank.common.model.TransactionCodeModel, com.inov8.microbank.common.model.ProductModel)
     */
    public Boolean titleFetch(SwitchWrapper switchWrapper) throws FrameworkCheckedException {

        logger.info("[CommonCommandManager.titleFetch] Going to Fetch Title for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());

        switchWrapper.setBankId(CommissionConstantsInterface.BANK_ID);
        switchWrapper.setPaymentModeId(6L);
        switchWrapper.setCustomerAccount(new CustomerAccount());
        switchWrapper.getCustomerAccount().setNumber(switchWrapper.getAccountInfoModel().getAccountNo());
        switchWrapper.getCustomerAccount().setType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.getCustomerAccount().setCurrency(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);

        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();

        try {

            phoenixFinancialInstitution.titleFetch(switchWrapper);

        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(e.getMessage());
        }

        String accountTitle = switchWrapper.getCustomerAccount().getTitleOfTheAccount();

        return StringUtil.isNullOrEmpty(accountTitle);
    }

    public TransactionModel saveTransactionModel(TransactionModel transactionModel) {

        transactionDAO.saveOrUpdate(transactionModel);
        return transactionModel;
    }

    public SwitchWrapper checkAgentBalance() throws WorkFlowException, FrameworkCheckedException, Exception {

        boolean isFetched = false;

        SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        sma.setRetailerContactId(appUserModel.getRetailerContactId());
        sma.setDeleted(false);
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(sma);
        searchBaseWrapper = this.loadSmartMoneyAccount(searchBaseWrapper);
        if (null != searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
            sma = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
        }
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setCustomerId(appUserModel.getAppUserId());
        accountInfoModel.setAccountNick(sma.getName());
//			accountInfoModel.setOldPin(pin);

        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(sma);
        BaseWrapper bWrapper = new BaseWrapperImpl();
        bWrapper.setBasePersistableModel(sma);
        veriflyBaseWrapper.setBasePersistableModel(null);
        VeriflyManager veriflyManager = this.loadVeriflyManagerByAccountId(bWrapper);
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        try {
            veriflyBaseWrapper = veriflyManager.verifyCredentials(veriflyBaseWrapper);
            boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

            if (errorMessagesFlag) {

                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();

                switchWrapper.setAccountInfoModel(accountInfoModel);
                switchWrapper.setWorkFlowWrapper(workFlowWrapper);

                switchWrapper.setBankId(sma.getBankId());
                switchWrapper.setPaymentModeId(sma.getPaymentModeId());
                switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
                switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);

                switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());

                if (switchWrapper.getWorkFlowWrapper() != null) {
                    switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
                }

                logger.info("[CommonCommandManager.checkAgentBalance] Going to Check Balance for AppUserID:" + appUserModel.getAppUserId());

                switchWrapper = this.switchController.checkBalance(switchWrapper);


            }

        } catch (Exception e) {
            throw new CommandException("Your account cannot be contacted. Please try again later.\n", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
        }

        logger.info("checkAgentBalance() Ended");

        return switchWrapper;

    }

    public SwitchWrapper checkCustomerBalance(String customerMobileNumber, double totalAmount) throws WorkFlowException, FrameworkCheckedException, Exception {

        AppUserModel customerAppUserModel = new AppUserModel();
        customerAppUserModel.setMobileNo(customerMobileNumber);
        customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        customerAppUserModel = appUserManager.getAppUserModel(customerAppUserModel);

        if (customerAppUserModel == null) {
            logger.error("[CommonCommandManager.checkCustomerBalance] Invalid customer mobile number. customerMobileNumber:" + customerMobileNumber);
            throw new CommandException("Unable to check customer balance. Invalid customer mobile number.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
        }

        SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
        sma.setCustomerId(customerAppUserModel.getCustomerId());
        sma.setDefAccount(true);
        sma.setDeleted(false);
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(sma);
        searchBaseWrapper = this.loadSmartMoneyAccount(searchBaseWrapper);
        if (null != searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
            sma = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
        } else {
            logger.error("[CommonCommandManager.checkCustomerBalance] Unable to load smart money account. customerAppUserId:" + customerAppUserModel.getAppUserId());
            throw new CommandException("Unable to check customer balance. Unable to load smart money account.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
        }

        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setCustomerId(customerAppUserModel.getCustomerId());
        accountInfoModel.setAccountNick(sma.getName());

        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(sma);
        BaseWrapper bWrapper = new BaseWrapperImpl();
        bWrapper.setBasePersistableModel(sma);
        veriflyBaseWrapper.setBasePersistableModel(null);
        VeriflyManager veriflyManager = this.loadVeriflyManagerByAccountId(bWrapper);
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        try {
            veriflyBaseWrapper = veriflyManager.verifyCredentials(veriflyBaseWrapper);
            boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

            if (errorMessagesFlag) {
                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();

                switchWrapper.setAccountInfoModel(accountInfoModel);
                switchWrapper.setWorkFlowWrapper(workFlowWrapper);

                switchWrapper.setBankId(sma.getBankId());
                switchWrapper.setPaymentModeId(sma.getPaymentModeId());
                switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
                switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);

                switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, customerAppUserModel.getNic());

                if (switchWrapper.getWorkFlowWrapper() != null) {
                    switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
                }

                logger.info("[CommonCommandManager.checkCustomerBalance] Going to Check Customer Balance for AppUserID:" + customerAppUserModel.getAppUserId());

                switchWrapper = this.switchController.checkBalance(switchWrapper);

                double balance = switchWrapper.getBalance();

                if (balance < totalAmount) {
                    logger.error("[CommonCommandManager.checkCustomerBalance] Insufficient customer balance. customerAppUserId:" + customerAppUserModel.getAppUserId() + " Total Amount:" + totalAmount);
                    throw new CommandException("Insufficient customer balance.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                }

            } else {

            }
        } catch (CommandException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CommandException("Your account cannot be contacted. Please try again later.\n", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
        }


        return switchWrapper;
    }

    public SwitchWrapper checkCustomerBalanceForHra(String customerMobileNumber, double totalAmount) throws WorkFlowException, FrameworkCheckedException, Exception {

        AppUserModel customerAppUserModel = new AppUserModel();
        customerAppUserModel.setMobileNo(customerMobileNumber);
        customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        customerAppUserModel = appUserManager.getAppUserModel(customerAppUserModel);

        if (customerAppUserModel == null) {
            logger.error("[CommonCommandManager.checkCustomerBalance] Invalid customer mobile number. customerMobileNumber:" + customerMobileNumber);
            throw new CommandException("Unable to check customer balance. Invalid customer mobile number.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
        }

        SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
        sma.setCustomerId(customerAppUserModel.getCustomerId());
        sma.setDefAccount(true);
        sma.setDeleted(false);
        sma.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
        sma.setActive(Boolean.TRUE);
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(sma);
        searchBaseWrapper = this.loadSmartMoneyAccount(searchBaseWrapper);
        if (null != searchBaseWrapper.getCustomList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
            sma = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
        } else {
            logger.error("[CommonCommandManager.checkCustomerBalance] Unable to load smart money account. customerAppUserId:" + customerAppUserModel.getAppUserId());
            throw new CommandException("Unable to check customer balance. Unable to load smart money account.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
        }

        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setCustomerId(customerAppUserModel.getCustomerId());
        accountInfoModel.setAccountNick(sma.getName());
        accountInfoModel.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);

        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(sma);
        BaseWrapper bWrapper = new BaseWrapperImpl();
        bWrapper.setBasePersistableModel(sma);
        veriflyBaseWrapper.setBasePersistableModel(null);
        VeriflyManager veriflyManager = this.loadVeriflyManagerByAccountId(bWrapper);
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        try {
            veriflyBaseWrapper = veriflyManager.verifyCredentials(veriflyBaseWrapper);
            boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

            if (errorMessagesFlag) {
                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();

                switchWrapper.setAccountInfoModel(accountInfoModel);
                switchWrapper.setWorkFlowWrapper(workFlowWrapper);

                switchWrapper.setBankId(sma.getBankId());
                switchWrapper.setPaymentModeId(sma.getPaymentModeId());
                switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
                switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);

                switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, customerAppUserModel.getNic());

                if (switchWrapper.getWorkFlowWrapper() != null) {
                    switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
                }

                logger.info("[CommonCommandManager.checkCustomerBalance] Going to Check Customer Balance for AppUserID:" + customerAppUserModel.getAppUserId());

                switchWrapper = this.switchController.checkBalance(switchWrapper);

                double balance = switchWrapper.getBalance();

                if (balance < totalAmount) {
                    logger.error("[CommonCommandManager.checkCustomerBalance] Insufficient customer balance. customerAppUserId:" + customerAppUserModel.getAppUserId() + " Total Amount:" + totalAmount);
                    throw new CommandException("Insufficient customer balance.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                }
                CustomerModel customerModel = customerAppUserModel.getCustomerIdCustomerModel();
                AccountModel accountModel = accountManager.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(customerAppUserModel.getNic(), CustomerAccountTypeConstants.HRA,
                        OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
                ProductModel productModel = this.productDAO.loadProductByProductName("HRA Cash Withdrwal");
                OLAVO olavo = new OLAVO();
                olavo.setTransactionTypeId(TransactionTypeConstants.DEBIT.toString());
                olavo.setReasonId(ReasonConstants.CASH_WITHDRAWAL);
                olavo.setToSegmentId(customerModel.getSegmentId());
                olavo.setAccountId(accountModel.getAccountId());
                olavo.setProductId(productModel.getProductId());
                olavo.setCustomerAccountTypeId(CustomerAccountTypeConstants.HRA);
                olavo.setTransactionDateTime(new Date());
                olavo.setBalance(totalAmount);
                olavo = accountManager.verifyDebitLimits(olavo);
                if (olavo.getResponseCode().equals("17"))
                    throw new CommandException("Per Day Limit of customer exceeded.\n", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                else if (olavo.getResponseCode().equals("19"))
                    throw new CommandException("Per month limit of Sender exceeded.\n", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                else if (olavo.getResponseCode().equals("21"))
                    throw new CommandException("Per year limit of Sender exceeded.\n", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);

            } else {
            }
        } catch (CommandException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CommandException("Your account cannot be contacted. Please try again later.\n", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
        }


        return switchWrapper;
    }

    @Override
    public void blockSmartMoneyAccount(AppUserModel appUserModel) throws Exception {
        //block smart money account of given user
        smartMoneyAccountManager.blockSmartMoneyAccount(appUserModel);

        logger.info("[CommonCommandManagerImpl.blockSmartMoneyAccount] smart money account is blocked. AppUserId: " + appUserModel.getAppUserId());
    }

    public void setAllPayTransactionInfoListViewDAO(AllPayTransactionInfoListViewDAO allPayTransactionInfoListViewDAO) {
        this.allPayTransactionInfoListViewDAO = allPayTransactionInfoListViewDAO;
    }

    public void setTransactionDAO(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    public void setTransactionDetailDAO(TransactionDetailDAO transactionDetailDAO) {
        this.transactionDetailDAO = transactionDetailDAO;
    }

    public void setPhoenixFinancialInstitution(
            AbstractFinancialInstitution phoenixFinancialInstitution) {
        this.phoenixFinancialInstitution = phoenixFinancialInstitution;
    }

    @Override
    public BaseWrapper saveTillBalance(BaseWrapper baseWrapper)
            throws FrameworkCheckedException {
        baseWrapper = this.tillBalanceManager.agentOpeningBalanceRequiresNewTransaction(baseWrapper);
        return baseWrapper;
    }

    public void setTillBalanceManager(TillBalanceManager tillBalanceManager) {
        this.tillBalanceManager = tillBalanceManager;
    }

    @Override
    public SearchBaseWrapper checkTillBalanceRequired(SearchBaseWrapper searchBaseWrapper)
            throws FrameworkCheckedException {

        searchBaseWrapper = this.tillBalanceManager.searchAgentOpeningBalanceByExample(searchBaseWrapper);
        return searchBaseWrapper;
    }

    public void setVeriflyManager(VeriflyManager veriflyManager) {
        this.veriflyManager = veriflyManager;
    }

    public void setCommissionRateDAO(CommissionRateDAO commissionRateDAO) {
        this.commissionRateDAO = commissionRateDAO;
    }

    public WorkFlowWrapper getAccToAccInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        AccountToAccountVO a2aVO = (AccountToAccountVO) workFlowWrapper.getProductVO();

//		if( ThreadLocalAppUser.getAppUserModel().getMobileNo().equals( a2aVO.getMobileNo() ) ){
//			logger.error("[CommonCommandManagerImpl.getAccToAccInfo] Own account transfer is not allowed. Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
//			throw new CommandException( "Own account transfer is not allowed." ,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM, null ) ;
//		}

        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        //loading recipient customer details
        if (!workFlowWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID).equals(DeviceTypeConstantsInterface.WEB_SERVICE)) {
            AppUserModel appUser = new AppUserModel();
            appUser.setMobileNo(a2aVO.getRecipientCustomerMobileNo());
            appUser.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);

            wrapper.setBasePersistableModel(appUser);
            wrapper = mfsAccountManager.loadAppUserByMobileNumberAndTypeHQL(wrapper);
            if (null != wrapper && wrapper.getBasePersistableModel() != null) {
                appUser = (AppUserModel) wrapper.getBasePersistableModel();

                a2aVO.setRecipientName(appUser.getFirstName() + " " + appUser.getLastName());
                a2aVO.setRecipientCustomerId(appUser.getCustomerId());
                a2aVO.setRecipientAppUserId(appUser.getAppUserId());

//			try{
//				a2aVO.setRecipientMFSID(mfsAccountManager.getDeviceAccountByAppUserId(appUser.getAppUserId(),  DeviceTypeConstantsInterface.MOBILE).getUserId() ) ;
//			}catch (Exception e){
//				logger.error("[CommonCommandManagerImpl.getAccToAccInfo] Exception Occured for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Details:" + e.getMessage());
//			}

            } else {
                logger.error("[CommonCommandManagerImpl.getAccToAccInfo] Invalid recipient mobile number or branchless banking account. Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                throw new FrameworkCheckedException("Invalid recipient mobile number or branchless banking account. Please enter the correct number.");
            }
        }
        //loading sender customer details
        wrapper = new SearchBaseWrapperImpl();
        AppUserModel senderAppUser = new AppUserModel();
        senderAppUser.setMobileNo(a2aVO.getSenderCustomerMobileNo());
        senderAppUser.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);

        wrapper.setBasePersistableModel(senderAppUser);
        wrapper = mfsAccountManager.loadAppUserByMobileNumberAndTypeHQL(wrapper);
        if (null != wrapper && wrapper.getBasePersistableModel() != null) {
            senderAppUser = (AppUserModel) wrapper.getBasePersistableModel();
            a2aVO.setSenderCustomerId(senderAppUser.getCustomerId());
            a2aVO.setSenderAppUserId(senderAppUser.getAppUserId());

//			try{
//				a2aVO.setSenderMFSID(mfsAccountManager.getDeviceAccountByAppUserId(senderAppUser.getAppUserId(),  DeviceTypeConstantsInterface.MOBILE).getUserId() ) ;
//			}catch (Exception e){
//				logger.error("[CommonCommandManagerImpl.getAccToAccInfo] Exception Occured for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Details:" + e.getMessage());
//			}

        } else {
            logger.error("[CommonCommandManagerImpl.getAccToAccInfo] Invalid sender mobile number or branchless banking account. Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new FrameworkCheckedException("Invalid sender mobile number or branchless banking account. Please enter the correct number.");
        }

        return workFlowWrapper;
    }

    public WorkFlowWrapper getBBToCoreAccInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException, CommandException, Exception {
        BBToCoreVO bbVo = (BBToCoreVO) workFlowWrapper.getProductVO();
        String mobileNo = bbVo.getCustomerMobileNo();

//		if (verifyPinRequired) {
//
//			//validate Agent pin
//			LogModel logModel = new LogModel();
//			logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
//			logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
//
//			SmartMoneyAccountModel sma = workFlowWrapper.getSmartMoneyAccountModel();
//
//			VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
//
//			veriflyBaseWrapper.setAccountInfoModel(workFlowWrapper.getAccountInfoModel());
//			veriflyBaseWrapper.setLogModel(logModel);
//
//			veriflyBaseWrapper.setBasePersistableModel(sma);
//			BaseWrapper baseWrapper = new BaseWrapperImpl();
//			baseWrapper.setBasePersistableModel(sma);
//			veriflyBaseWrapper.setBasePersistableModel(null);
//			VeriflyManager veriflyManager = loadVeriflyManagerByAccountId(baseWrapper);
//			veriflyBaseWrapper = verifyVeriflyPin(veriflyManager, veriflyBaseWrapper);
//			boolean isError = veriflyBaseWrapper.isErrorStatus() == false;
//			if (isError) {
//				String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
//				throw new CommandException(veriflyErrorMessage,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//			}
//		}


        //TODO: Title Fetch
        String title = fetchAccountTitle(workFlowWrapper);
        bbVo.setAccountTitle(title);

        if (workFlowWrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT) {
            return workFlowWrapper;
        }

        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        AppUserModel appUser = new AppUserModel();
        appUser.setMobileNo(mobileNo);
        appUser.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);

        wrapper.setBasePersistableModel(appUser);
        wrapper = mfsAccountManager.loadAppUserByMobileNumberAndTypeHQL(wrapper);
        if (null != wrapper && wrapper.getBasePersistableModel() != null) {
            appUser = (AppUserModel) wrapper.getBasePersistableModel();

            bbVo.setCustomerName(appUser.getFirstName() + " " + appUser.getLastName());
            bbVo.setCustomerId(appUser.getCustomerId());
            bbVo.setAppUserId(appUser.getAppUserId());

            try {
                bbVo.setMfsId(mfsAccountManager.getDeviceAccountByAppUserId(appUser.getAppUserId(), DeviceTypeConstantsInterface.MOBILE).getUserId());
            } catch (Exception e) {
                logger.error("[CommonCommandManagerImpl.getBBToCoreInfo] Exception Occured while setting MFS Id for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Details:" + e.getMessage());
            }

        } else {
            logger.error("[CommonCommandManagerImpl.getBBToCoreInfo] Invalid mobile number or branchless banking account. Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new FrameworkCheckedException("Invalid mobile number or branchless banking account. Please enter the correct number.");
        }

        return workFlowWrapper;
    }

    public WorkFlowWrapper getCnicToCoreAccInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException, CommandException, Exception {
        BBToCoreVO vo = (BBToCoreVO) workFlowWrapper.getProductVO();
        String mobileNo = vo.getCustomerMobileNo();

        //validate Agent pin
//		LogModel logModel = new LogModel();
//		logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
//		logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
//
//		SmartMoneyAccountModel sma = workFlowWrapper.getSmartMoneyAccountModel();
//
//		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
//
//		veriflyBaseWrapper.setAccountInfoModel(workFlowWrapper.getAccountInfoModel());
//		veriflyBaseWrapper.setLogModel(logModel);
//
//		veriflyBaseWrapper.setBasePersistableModel(sma);
//		BaseWrapper baseWrapper = new BaseWrapperImpl();
//		baseWrapper.setBasePersistableModel(sma);
//		veriflyBaseWrapper.setBasePersistableModel(null);
//		VeriflyManager veriflyManager = loadVeriflyManagerByAccountId(baseWrapper);
//			veriflyBaseWrapper = verifyVeriflyPin(veriflyManager, veriflyBaseWrapper);
//			boolean isError = veriflyBaseWrapper.isErrorStatus() == false;
//			if (isError) {
//				String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
//				throw new CommandException(veriflyErrorMessage,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//			}

        String title = fetchAccountTitle(workFlowWrapper);
        vo.setAccountTitle(title);

		/*SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		AppUserModel appUser = new AppUserModel() ;
		appUser.setMobileNo(mobileNo);
		appUser.setAppUserTypeId( UserTypeConstantsInterface.CUSTOMER );

		wrapper.setBasePersistableModel(appUser);
		wrapper = mfsAccountManager.loadAppUserByMobileNumberAndTypeHQL(wrapper);
		if (null != wrapper && wrapper.getBasePersistableModel() != null) {
			appUser = (AppUserModel)wrapper.getBasePersistableModel();

			vo.setCustomerName( appUser.getFirstName() + " " + appUser.getLastName() ) ;
			vo.setCustomerId( appUser.getCustomerId() ) ;
			vo.setAppUserId( appUser.getAppUserId() ) ;

			try{
				vo.setMfsId( mfsAccountManager.getDeviceAccountByAppUserId(appUser.getAppUserId(),  DeviceTypeConstantsInterface.MOBILE).getUserId() ) ;
			}catch (Exception e){
				logger.error("[CommonCommandManagerImpl.getAccToAccInfo] Exception Occured while setting MFS Id for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Details:" + e.getMessage());
			}

		}else{
			logger.error("[CommonCommandManagerImpl.getAccToAccInfo] Invalid mobile number or branchless banking account. Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
			throw new FrameworkCheckedException( "Invalid mobile number or branchless banking account. Please enter the correct number." ) ;
		}*/

        return workFlowWrapper;
    }

    public WorkFlowWrapper getAccountToCashInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException, CommandException, Exception {

        //validate Agent pin
        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

        SmartMoneyAccountModel sma = workFlowWrapper.getSmartMoneyAccountModel();

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        workFlowWrapper.getAccountInfoModel().setCustomerId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
        workFlowWrapper.getAccountInfoModel().setAccountNick(sma.getName());
        veriflyBaseWrapper.setAccountInfoModel(workFlowWrapper.getAccountInfoModel());
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(sma);
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(sma);
        veriflyBaseWrapper.setBasePersistableModel(null);
        VeriflyManager veriflyManager = loadVeriflyManagerByAccountId(baseWrapper);
        veriflyBaseWrapper = verifyVeriflyPin(veriflyManager, veriflyBaseWrapper);
        boolean isError = veriflyBaseWrapper.isErrorStatus() == false;
        if (isError) {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }

        return workFlowWrapper;
    }

    public Double getCommissionRate(ProductModel productModel) {

        Double rate = 0.0D;

        CommissionRateModel example = new CommissionRateModel();
        example.setProductIdProductModel(productModel);

        CustomList<CommissionRateModel> commissionRateList = commissionRateDAO.findByExample(example, null);

        for (CommissionRateModel commissionRate : commissionRateList.getResultsetList()) {

            if (commissionRate.getActive()) {
                rate = commissionRate.getRate();
                break;
            }
        }

        return rate;
    }

    public CommissionWrapper calculateCommission(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start/End of CommonCommandManagerImpl.calculateCommission()");
        }
        return this.commissionManager.calculateCommission(workFlowWrapper);
    }

    public CommissionWrapper calculateCommissionInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
        workFlowWrapper.setSegmentModel(segmentModel);

        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
        commissionWrapper.setPaymentModeModel(workFlowWrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
        commissionWrapper.setTransactionModel(workFlowWrapper.getTransactionModel());
        commissionWrapper.setTransactionTypeModel(workFlowWrapper.getTransactionTypeModel());
        commissionWrapper.setProductModel(workFlowWrapper.getProductModel());

        commissionWrapper = this.calculateCommission(workFlowWrapper);
        CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

        workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);

        AGENT_FRANCHISE_ONE_COMMISSION:
        {


            //if agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction

            if (workFlowWrapper.getFromRetailerContactModel() != null && workFlowWrapper.getFromRetailerContactModel().getHead()) {

                commissionAmountsHolder.setAgent1CommissionAmount(commissionAmountsHolder.getAgent1CommissionAmount() + commissionAmountsHolder.getFranchise1CommissionAmount());
                commissionAmountsHolder.setFranchise1CommissionAmount(0.0d);
            }
        }

        return commissionWrapper;
    }

    public void setMiniStatementListViewDAO(MiniStatementListViewDAO miniStatementListViewDAO) {
        this.miniStatementListViewDAO = miniStatementListViewDAO;
    }

    /**
     * @param productModel
     * @return
     * @throws FrameworkCheckedException
     */
    public Long getReasonType(Long productId) {

        Long reasonId = -1L;

        CommissionRateModel commissionRateModel = new CommissionRateModel();
        commissionRateModel.setProductId(productId);
        commissionRateModel.setActive(Boolean.TRUE);

        SearchBaseWrapper searchBaseWrapper;
        try {

            searchBaseWrapper = getCommissionRateData(commissionRateModel);

            List<CommissionRateModel> resultSetList = searchBaseWrapper.getCustomList().getResultsetList();

            for (CommissionRateModel _commissionRateModel : resultSetList) {

                reasonId = _commissionRateModel.getCommissionReasonId();

                if (reasonId.longValue() == 5) {
                    break;
                }
            }

        } catch (FrameworkCheckedException ex) {

            logger.error(ex);
        }

        return reasonId;
    }

    /*
     * this method intended for CW product, we will load segment id of customer and send here, will incorporate it with other product as well.
     * */
    public DistributorModel loadDistributor(AppUserModel appUserModel) throws FrameworkCheckedException {

        RetailerContactModel retailerContactModel = this.retailerContactDAO.findByPrimaryKey(appUserModel.getRetailerContactId());
        RetailerModel retailerModel = this.retailerDAO.findByPrimaryKey(retailerContactModel.getRetailerId());
        DistributorModel distributorModel = this.distributorDAO.findByPrimaryKey(retailerModel.getDistributorId());

        return distributorModel;
    }

    public void checkProductLimit(Long segmentId, Long productId, String mobileNumber, Long deviceTypeId, Double amount, ProductModel _productModel, DistributorModel _distributorModel, HandlerModel handlerModel) throws Exception {

        Long distributorId = null;
        Long handlerAccountTypeId = null;

        ProductModel productModel = null;
        AppUserModel appUserModel = this.loadAppUserByMobileAndType(mobileNumber, UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER);
        DistributorModel distributorModel = null;

        if (handlerModel != null) {
            handlerAccountTypeId = handlerModel.getAccountTypeId();
        }

        if (_distributorModel != null) {
            distributorId = _distributorModel.getDistributorId();
            distributorModel = _distributorModel;
        }

        if (_productModel != null) {
            productModel = _productModel;
        } else if (productId != null) {
            productModel = productDAO.findByPrimaryKey(productId);
        }

        if (appUserModel == null) {

            appUserModel = ThreadLocalAppUser.getAppUserModel();
        }

        if (segmentId == null && appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

            CustomerModel customerModel = this.customerDAO.findByPrimaryKey(appUserModel.getCustomerId());
            segmentId = customerModel.getSegmentId();

        }

        if (appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {

            if (distributorModel == null) {

                distributorModel = loadDistributor(appUserModel);
                distributorId = distributorModel.getDistributorId();
            }

            if (segmentId == null) {
                segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
            }

        }


        Double minLimit = null;
        Double maxLimit = null;
        Long mnoId = null;
        if (UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L)) {
            mnoId = 50028L;
            //deviceTypeId = DeviceTypeConstantsInterface.SCO_APP;
        }

        ProductLimitRuleViewModel productLimitRuleViewModel = productManager.getProductLimitRuleViewModel(new ProductLimitRuleViewModel(productId, deviceTypeId, segmentId, distributorId, handlerAccountTypeId, mnoId));

        if (productLimitRuleViewModel != null) {

            minLimit = productLimitRuleViewModel.getMinLimit();
            maxLimit = productLimitRuleViewModel.getMaxLimit();

        } else {

            minLimit = productModel.getMinLimit();
            maxLimit = productModel.getMaxLimit();
        }

        if (minLimit == null) {

            throw new CommandException("Undefined Minimum Product Limit for (" + productModel.getName() + ")", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
        }

        if (maxLimit == null) {

            throw new CommandException("Undefined Maximum Product Limit for (" + productModel.getName() + ")", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
        }

        if (amount < minLimit) {

            throw new CommandException("The amount provided (" + amount + ") is less than the minimum Product Limit (" + minLimit + ").", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);

        } else if (amount > maxLimit) {
            if (!deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE)) {
                throw new CommandException("The amount provided (" + amount + ") is greater than the maximum Product Limit (" + maxLimit + ").", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
            } else
                throw new CommandException("The amount provided (" + amount + ") is greater than the maximum Product Limit (" + maxLimit + ").", ErrorCodes.TRXN_AMOUNT_GREATER_THAN_LIMIT, ErrorLevel.MEDIUM, null);
        }

    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setVelocityRuleViewDAO(VelocityRuleViewDAO velocityRuleViewDAO) {
        this.velocityRuleViewDAO = velocityRuleViewDAO;
    }

    public void setOperatingHoursRuleModelDAO(OperatingHoursRuleModelDAO operatingHoursRuleModelDAO) {
        this.operatingHoursRuleModelDAO = operatingHoursRuleModelDAO;
    }

    public boolean checkVelocityCondition(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        //Operating hours module is implemented similarly to velocity
        // Populating operating rule parameters and checking conditions in the end of method.
        OperatingHoursRuleModel operatingHoursRuleModel = new OperatingHoursRuleModel();
        List<OperatingHoursRuleModel> operatingHoursRulesList = new ArrayList<OperatingHoursRuleModel>();
        VelocityRuleViewModel velocityModel = new VelocityRuleViewModel();
        List<VelocityRuleViewModel> rulesList = new ArrayList<VelocityRuleViewModel>();
        Double trxAmount = (Double) baseWrapper.getObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT);

        if (null != baseWrapper.getObject(CommandConstants.VELOCITY_PRODUCT_ID)) {
            velocityModel.setProductId((Long) baseWrapper.getObject(CommandConstants.VELOCITY_PRODUCT_ID));
            operatingHoursRuleModel.setProductId(velocityModel.getProductId());
        }
        if (null != baseWrapper.getObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID)) {
            velocityModel.setDeviceTypeId((Long) baseWrapper.getObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID));
            operatingHoursRuleModel.setDeviceTypeId(velocityModel.getDeviceTypeId());
        }
        if (null != baseWrapper.getObject(CommandConstants.VELOCITY_SEGMENT_ID)) {
            velocityModel.setSegmentId((Long) baseWrapper.getObject(CommandConstants.VELOCITY_SEGMENT_ID));
            operatingHoursRuleModel.setSegmentId(velocityModel.getSegmentId());
        }
        if (null != baseWrapper.getObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID)) {
            velocityModel.setDistributorId((Long) baseWrapper.getObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID));
            operatingHoursRuleModel.setDistributorId(velocityModel.getDistributorId());
        }
        if (null != baseWrapper.getObject(CommandConstants.VELOCITY_AGENT_TYPE)) {
            velocityModel.setAgentType((Long) baseWrapper.getObject(CommandConstants.VELOCITY_AGENT_TYPE));
            //operatingHoursRuleModel.setag
        }
        //Account Type Limit
        if (null != baseWrapper.getObject(CommandConstants.VELOCITY_ACCOUNT_TYPE)) {
            velocityModel.setCustomerAccountTypeId((Long) baseWrapper.getObject(CommandConstants.VELOCITY_ACCOUNT_TYPE));
            operatingHoursRuleModel.setCustomerAccountTypeId(velocityModel.getCustomerAccountTypeId());
        }
        if (null != baseWrapper.getObject(CommandConstants.VELOCITY_CUSTOMER_ID) && !baseWrapper.getObject(CommandConstants.VELOCITY_CUSTOMER_ID).equals("")) {
            velocityModel.setCustomerId(Long.parseLong((String) baseWrapper.getObject(CommandConstants.VELOCITY_CUSTOMER_ID)));
            //operatingHoursRuleModel.setCustomer
        }
        try {
            rulesList = this.velocityRuleViewDAO.loadVelocityRules(velocityModel);
        } catch (Exception ex) {
            logger.error("[CommonCommandManagerImpl.checkVelocityCondition] Exception while loading velocity rules...");
            ex.printStackTrace();
        }

        for (VelocityRuleViewModel ruleModel : rulesList) {

            // Check for Max no of Transaction
            if (ruleModel.getMaxNoOfTransaction() != null && ruleModel.getMaxNoOfTransaction() > 0) {
                if (ruleModel.getTotalNoOfTransaction() >= ruleModel.getMaxNoOfTransaction()) {
                    logger.error("[CommonCommandManagerImpl.checkVelocityCondition] Max Total No of Transactions check voilated for velocityRuleId:" + ruleModel.getVelocityRuleId() + ". Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    String errorMsg = StringUtil.isNullOrEmpty(ruleModel.getErrorMessage()) ? "Max Total No of Transactions check voilated" : ruleModel.getErrorMessage();
                    throw new CommandException(errorMsg, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                }
            }

            // Check for Total combine amount of Transactions
            if (ruleModel.getMaxAmountOfTransaction() != null && ruleModel.getMaxAmountOfTransaction() > 0) {
                if ((ruleModel.getTotalAmountOfTransaction() + trxAmount) > ruleModel.getMaxAmountOfTransaction()) {
                    logger.error("[CommonCommandManagerImpl.checkVelocityCondition] Max Total Amount of Transactions check voilated for velocityRuleId:" + ruleModel.getVelocityRuleId() + ". Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    String errorMsg = StringUtil.isNullOrEmpty(ruleModel.getErrorMessage()) ? "Max Total Amount of Transactions check voilated" : ruleModel.getErrorMessage();
                    throw new CommandException(errorMsg, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                }
            }
        }

        //Checking operating hours rule it is implemented like velocity model and applied on all transactions hence checking operating hours rule here
        //This module should be disabled if no G2P transactions in future
        //Along with disabling of data saving in Agent_Location_Stat in BISPAgentVerificationCommand
        try {
            operatingHoursRulesList = this.operatingHoursRuleModelDAO.loadOperatingHoursRules(operatingHoursRuleModel);
        } catch (Exception ex) {
            logger.error("[CommonCommandManagerImpl.checkVelocityCondition--OperatingHoursRules] Exception while loading Operating rules...");
            ex.printStackTrace();

        }
        for (OperatingHoursRuleModel ruleModel : operatingHoursRulesList) {

            if (ruleModel.getAllowedStartingHours() != null && !ruleModel.getAllowedStartingHours().isEmpty()
                    && ruleModel.getAllowedEndingHours() != null && !ruleModel.getAllowedEndingHours().isEmpty()) {
                DateTime dt = new DateTime();
                int hours = dt.getHourOfDay();
                if (hours < Integer.parseInt(ruleModel.getAllowedStartingHours()) ||
                        hours >= Integer.parseInt(ruleModel.getAllowedEndingHours())) {
                    logger.error("[CommonCommandManagerImpl.checkVelocityCondition:OperatingHoursCheck] Operating Hours voilation  for OperatingHoursRuleId:" + operatingHoursRuleModel.getOperatingHoursRuleId() + ". Throwing Exception for AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    String errorMsg = StringUtil.isNullOrEmpty(ruleModel.getErrorMsg()) ? "Operating Hours  check voilated" : ruleModel.getErrorMsg();
                    throw new CommandException(errorMsg, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                }

            }
            //Check for agent location validations if enabled
            if (ruleModel.getIsLocVrfReq() != null && ruleModel.getIsLocVrfReq()) {
                AppUserModel appUserModel = new AppUserModel();
                appUserModel = ThreadLocalAppUser.getAppUserModel();
                UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
                uda = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
                AgentLocationStatModel agentLocationStatModel = new AgentLocationStatModel();
                if (appUserModel != null && appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
                    if (uda != null) {
                        if (uda.getAppUserId().equals(appUserModel.getAppUserId())) {

                            agentLocationStatModel.setAgentId(Long.parseLong(uda.getUserId()));
                            agentLocationStatModel = this.getCommonCommandManager().getAgentLocationStatManager().getAgentLocationStat(agentLocationStatModel);

                        }
                    } else {
                        uda = userDeviceAccountsDAO.findUserDeviceAccountByAppUserId(appUserModel.getAppUserId());
                        if (uda != null) {
                            agentLocationStatModel.setAgentId(Long.parseLong(uda.getUserId()));
                            agentLocationStatModel = this.getCommonCommandManager().getAgentLocationStatManager().getAgentLocationStat(agentLocationStatModel);


                        } else {
                            logger.info("Unable to get userdeviceaccountsmodel for app user id :" + appUserModel.getAppUserId());
                        }
                    }

                    if (agentLocationStatModel != null && agentLocationStatModel.getIsValidLocation().equals(0L)) {
                        throw new CommandException("Agent is not at his registered Location, Please try again from registered location", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                    } else if (agentLocationStatModel == null) {
                        throw new CommandException("Agent Location is not configured.Please contact your service provider", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                    }
                }

            }

        }


        return true;
    }

    public VeriflyBaseWrapper changePIN(VeriflyBaseWrapper wrapper) throws
            Exception {
        return mfsAccountManager.changePIN(wrapper);
    }

    public Integer updateTransactionProcessingStatus(Long transactionProcessingStatus, List<Long> transactionIds) {
        return transactionManager.updateTransactionProcessingStatus(transactionProcessingStatus, transactionIds);
    }

    public SearchBaseWrapper loadStakeHolderBankInfo(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {

        return stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
    }

    public SupplierBankInfoModel loadSupplierBankInfo(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        return (SupplierBankInfoModel) this.supplierBankInfoManager.loadSupplierBankInfo(baseWrapper).getBasePersistableModel();
    }

    public long getCustomerSegmentIdByMobileNo(String customerMobileNo) throws FrameworkCheckedException {
        return appUserDAO.loadSegmentIdByMobileNo(customerMobileNo);
    }

    public void validateHRA(String mobileNo) throws CommandException {
        Long customerAccountTypeId = appUserDAO.loadCustomerAccountTypeByMobileNo(mobileNo);
        logger.info("HRA validation check -->  mobile:" + mobileNo + " custAccType:" + customerAccountTypeId);
        if (customerAccountTypeId != null && customerAccountTypeId.longValue() == CustomerAccountTypeConstants.HRA) {
            logger.error(WorkFlowErrorCodeConstants.HRA_ERROR_MSG + " -->  mobile:" + mobileNo);
            throw new CommandException(WorkFlowErrorCodeConstants.HRA_ERROR_MSG, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
        }
    }

    @Override
    public AppUserModel checkAppUserTypeAsWalkinCustoemr(String mobileNo) throws FrameworkCheckedException {

        AppUserModel appUserModel = appUserManager.checkAppUserTypeAsWalkinCustoemr(mobileNo);
        return appUserModel;

    }

    public void setTransactionDetailMasterDAO(
            TransactionDetailMasterDAO transactionDetailMasterDAO) {
        this.transactionDetailMasterDAO = transactionDetailMasterDAO;
    }

    public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
        this.stakeholderBankInfoManager = stakeholderBankInfoManager;
    }

    public void setSupplierBankInfoManager(SupplierBankInfoManager supplierBankInfoManager) {
        this.supplierBankInfoManager = supplierBankInfoManager;
    }

    public void setRegistrationStateDAO(RegistrationStateDAO registrationStateDAO) {
        this.registrationStateDAO = registrationStateDAO;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }

    public void setArbitraryResourceLoader(
            ArbitraryResourceLoader arbitraryResourceLoader) {
        this.arbitraryResourceLoader = arbitraryResourceLoader;
    }

    public void setGoldenNosDAO(GoldenNosDAO goldenNosDAO) {
        this.goldenNosDAO = goldenNosDAO;
    }

    public void setBankDAO(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }

    public void setOlaVeriflyFinancialInstitution(
            FinancialInstitution olaVeriflyFinancialInstitution) {
        this.olaVeriflyFinancialInstitution = olaVeriflyFinancialInstitution;
    }

    public void setLinkPaymentModeDAO(LinkPaymentModeDAO linkPaymentModeDAO) {
        this.linkPaymentModeDAO = linkPaymentModeDAO;
    }

    @Override
    public CustomList<RegistrationStateModel> getRegistrationStateByIds(Long[] registrationStateIds) throws FrameworkCheckedException {
        CustomList<RegistrationStateModel> regStateModelList = registrationStateDAO.getRegistrationStateByIds(registrationStateIds);
        return regStateModelList;
    }

    @Override
    public CustomList<BlinkCustomerRegistrationStateModel> getBlinkRegistrationStateByIds(Long[] registrationStateIds) throws FrameworkCheckedException {
        CustomList<BlinkCustomerRegistrationStateModel> regStateModelList = blinkCustomerRegistrationStateDAO.getRegistrationStateByIds(registrationStateIds);
        return regStateModelList;
    }

    @Override
    public void sendSMS(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        if (logger.isDebugEnabled()) {

            logger.debug("Start of CommonCommandManagerImpl.sendSMS() / By Commands");
        }

        @SuppressWarnings("unchecked")
        ArrayList<SmsMessage> messageList = (ArrayList<SmsMessage>) baseWrapper.getObject(CommandFieldConstants.KEY_SMS_MESSAGES);

        if (messageList != null && !messageList.isEmpty()) {
            for (SmsMessage message : messageList) {
                this.smsSender.send(message);
//                message.setMessageType("ZINDIGI");
//                message.setTitle("Push Notification");
//                this.smsSender.pushNotification(message);
//                if(!StringUtil.isNullOrEmpty(baseWrapper.getObject("RCMobileNo").toString())
//                        && baseWrapper.getObject("RCMobileNo") == 1L){
//                    message.setMobileNo((String) baseWrapper.getObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE));
//                    this.smsSender.send(message);
//                    message.setMessageType("ZINDIGI");
//                    message.setTitle("Push Notification");
//                    this.smsSender.pushNotification(message);
//                }
                message.setMessageType("ZINDIGI");
                message.setTitle("Push Notification");
                this.smsSender.pushNotification(message);
                if (!StringUtil.isNullOrEmpty(String.valueOf(baseWrapper.getObject("RCMobileNo")))
                        && baseWrapper.getObject("RCMobileNo") != null
                        && baseWrapper.getObject("RCMobileNo").equals(true)) {
                    message.setMobileNo((String) baseWrapper.getObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE));
                    this.smsSender.send(message);
                    message.setMessageType("ZINDIGI");
                    message.setTitle("Push Notification");
                    this.smsSender.pushNotification(message);
                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.sendSMS()");
        }
    }

    @Override
    public void novaAlertMessage(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        @SuppressWarnings("unchecked")
        ArrayList<NovaAlertMessage> messageList = (ArrayList<NovaAlertMessage>) baseWrapper.getObject(CommandFieldConstants.KEY_NOVA_ALERT_SMS_MESSAGES);

        if (messageList != null && !messageList.isEmpty()) {
            for (NovaAlertMessage message : messageList) {
                this.smsSender.alertNovaMessage(message);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.sendSMS()");
        }
    }

    @Override
    public void initiateUserGeneratedPinIvrCall(IvrRequestDTO ivrDTO) throws FrameworkCheckedException {
        ivrDTO.setRetryCount(0);
        // ProductId should be already set
        // ivrDTO.setProductId(new Long(CommandFieldConstants.CREATE_PIN_IVR));
        try {
            ivrAuthenticationRequestQueueSender.sentAuthenticationRequest(ivrDTO);
        } catch (Exception e) {
            logger.error("[CommonCommandManagerImpl.initiateUserGeneratedPinIvrCall] Exception while calling customer...");
            e.printStackTrace();
            throw new CommandException(e.getLocalizedMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
        }

    }

    @Override
    public int countCustomerPendingTrx(String cnic) throws FrameworkCheckedException {
        try {
            return this.customerPendingTrxManager.countCustomerPendingTrx(cnic);
        } catch (Exception e) {
            logger.error("[CommonCommandManagerImpl.countCustomerPendingTrx] Exception while counting customer pending Trx...");
            e.printStackTrace();
            throw new CommandException(e.getLocalizedMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
        }
    }

    @Override
    public int countCustomerPendingTrxByMobile(String mobile) throws FrameworkCheckedException {
        try {
            return this.customerPendingTrxManager.countCustomerPendingTrxByMobile(mobile);
        } catch (Exception e) {
            logger.error("[CommonCommandManagerImpl.countCustomerPendingTrxByMobile] Exception while counting customer pending Trx...");
            e.printStackTrace();
            throw new CommandException(e.getLocalizedMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
        }
    }

    @Override
    public boolean checkP2PTransactionsOnCNIC(String cnic, String mobile) throws FrameworkCheckedException {
        boolean result = false;

        TransactionDetailMasterModel txMasterModel = new TransactionDetailMasterModel();
        txMasterModel.setRecipientCnic(cnic);
        txMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.IN_PROGRESS);
        txMasterModel.setProductId(ProductConstantsInterface.CASH_TRANSFER);

        CustomList list = transactionDetailMasterDAO.findByExample(txMasterModel);
        if (null != list && null != list.getResultsetList() && list.getResultsetList().size() > 0) {
            for (TransactionDetailMasterModel model : (List<TransactionDetailMasterModel>) list.getResultsetList()) {
                if (!StringUtil.isNullOrEmpty(model.getRecipientMobileNo()) && !model.getRecipientMobileNo().equals(mobile)) {
                    result = true;
                    logger.error("[CommonCommandManagerImpl.checkP2PTransactionsOnCNIC] Existing In-Process P2P Transaction (TrxId:" + model.getTransactionCode() + ", cnic:" + model.getRecipientCnic() + ", mobile:" + model.getRecipientMobileNo() + ")  -- provided mobile:" + mobile);
                    break;
                }
            }
        }

        return result;
    }

    public AgentTransferRuleModel findAgentTransferRule(Long deviceTypeId, Double transactionAmount, Long senderAppUserId, Long recipientAppUserId) throws FrameworkCheckedException {
        AgentTransferRuleModel ruleModel = null;
        String criteriaString = " [ senderAppUserId:" + senderAppUserId
                + " recipientAppUserId:" + recipientAppUserId
                + " deviceTypeId:" + deviceTypeId
                + " trxAmount:" + transactionAmount + " ]";

        logger.error("******* Going to fetch AgentTransferRule against criteria: " + criteriaString);

        try {
            ruleModel = agentTransferRuleDAO.findAgentTransferRule(deviceTypeId, transactionAmount, senderAppUserId, recipientAppUserId);
        } catch (Exception ex) {
            if (StringUtil.isFailureReasonId(ex.getMessage()) && ex.getMessage().equals(WorkFlowErrorCodeConstants.AGENT_TO_AGENT_TRNSFR_RANGE_ERROR)) {
                throw new CommandException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.AGENT_TO_AGENT_TRNSFR_RANGE_ERROR), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
            } else {
                logger.error("Exception occurred while loading Agent Transfer Rule. Exception:", ex);
                throw new CommandException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
            }
        }

        if (ruleModel == null) {
            logger.error("No AgentTransferRule found against given criteria");
            throw new CommandException("Agent to Agent transfer is not allowed.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
        } else {
            logger.info("Agent Transfer Rule to be applied [AgentTransferRuleId:" + ruleModel.getAgentTransferRuleId()
                    + " SenderGroupId:" + ruleModel.getSenderGroupId()
                    + " ReceiverGroupId:" + ruleModel.getReceiverGroupId()
                    + " ExclusiveFixAmount:" + ruleModel.getExclusiveFixAmount()
                    + " ExclusivePercentAmount:" + ruleModel.getExclusivePercentAmount()
                    + " InclusiveFixAmount:" + ruleModel.getInclusiveFixAmount()
                    + " InclusivePercentAmount:" + ruleModel.getInclusivePercentAmount()
                    + " RangeStart:" + ruleModel.getRangeStarts()
                    + " RangeEnd:" + ruleModel.getRangeEnds()
                    + " ThirdPartyCheck:" + ruleModel.getThirdPartyCheck()
                    + " CreatedBy AppUserId:" + ruleModel.getCreatedBy() + "]");
        }

        return ruleModel;
    }

    public UserDeviceAccountsModel loadUserDeviceAccountByUserId(String userId) throws FrameworkCheckedException {
        return userDeviceAccountsDAO.loadUserDeviceAccountByUserId(userId);
    }

    @Override
    public AppUserModel loadRetailerAppUserModelByAppUserId(Long appUserId) throws FrameworkCheckedException {
        AppUserModel appUserModel = appUserManager.loadRetailerAppUserModelByAppUserId(appUserId);
        return appUserModel;
    }

    public void verifyWalkinCustomerThroughputLimits(WorkFlowWrapper workFlowWrapper, String transactionTypeId, String walkInCNIC) throws FrameworkCheckedException {
        //verify throughput limits for walkin customer
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
        switchWrapper.setWorkFlowWrapper(workFlowWrapper);

        OLAVO olavo = new OLAVO();
        olavo.setCustomerAccountTypeId(UserTypeConstantsInterface.WALKIN_CUSTOMER);//Walk-in
        olavo.setBalance(workFlowWrapper.getTransactionAmount());
        olavo.setTransactionDateTime(new Date());
        switchWrapper.setOlavo(olavo);

        // Check Throughput Limit of Sender
//		switchWrapper = verifyWalkinCustomerThroughputLimits(switchWrapper, workFlowWrapper.getSenderWalkinCustomerModel().getCnic(), TransactionTypeConstantsInterface.OLA_DEBIT);
        switchWrapper.getOlavo().setTransactionTypeId(transactionTypeId);
        switchWrapper.getOlavo().setCnic(CommonUtils.maskWalkinCustomerCNIC(walkInCNIC));

        try {
            switchWrapper.putObject("EXCLUDE_INPROCESS_TX", workFlowWrapper.getObject("EXCLUDE_INPROCESS_TX"));
            switchWrapper = olaVeriflyFinancialInstitution.verifyWalkinCustomerThroughputLimits(switchWrapper);
            workFlowWrapper.putObject(CommandFieldConstants.IS_RECEIVER_BVS_REQUIRED, (Boolean) switchWrapper.getOlavo().getResponseCodeMap().get(TransactionTypeConstants.KEY_BVS_VAL) ? "1" : "0");
        } catch (WorkFlowException e) {
            throw new WorkFlowException(e.getMessage());
        } catch (FrameworkCheckedException e) {
            throw new WorkFlowException(e.getMessage());
        } catch (Exception e) {
            throw new WorkFlowException(e.getMessage());
        }

        boolean isSenderBvsRequired = (Boolean) switchWrapper.getOlavo().getResponseCodeMap().get(TransactionTypeConstants.KEY_BVS_VAL);
        if (isSenderBvsRequired && workFlowWrapper.getDeviceTypeModel().getDeviceTypeId() == DeviceTypeConstantsInterface.USSD && ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue() != workFlowWrapper.getProductModel().getServiceId().longValue()) {
            throw new CommandException("This transaction can only be performed by agent having Biometric Device", ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.HIGH, new Throwable());
        }

        workFlowWrapper.putObject(CommandFieldConstants.IS_SENDER_BVS_REQUIRED, isSenderBvsRequired ? "1" : "0");
    }

    public List<BasePersistableModel> findBasePersistableModel(BasePersistableModel persistableModel) {

        List<BasePersistableModel> list = this.genericDao.findEntityByExample(persistableModel, null);

        return list;
    }

    @Override
    public Boolean isCnicBlacklisted(String cnicNo) {
        return accountControlManager.isCnicBlacklisted(cnicNo);
    }

    @Override
    public CustomerPictureDAO getCustomerPictureDAO() {
        return customerPictureDAO;
    }

    public void setCustomerPictureDAO(CustomerPictureDAO customerPictureDAO) {
        this.customerPictureDAO = customerPictureDAO;
    }

    public void saveRootedMobileHistory(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        mfsAccountManager.saveRootedMobileHistory(baseWrapper);
    }

    @Override
    public void isUniqueCNICMobile(AppUserModel appUserModel, BaseWrapper baseWrapper) throws FrameworkCheckedException {
        mfsAccountManager.isUniqueCNICMobile(appUserModel, baseWrapper);
    }

    @Override
    public void updateOpenCustomerL0Request(WebServiceVO webServiceVO, AppUserModel appUserModel, ArrayList<CustomerPictureModel> arrayCustomerPictures, boolean isConsumerApp) throws FrameworkCheckedException {

        boolean isConventional = false;
        Long oldRegistrationStateId = appUserModel.getRegistrationStateId();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = new Date();
        FonePayMessageVO messageVO = new FonePayMessageVO();
        long accountType = NumberUtils.toLong(webServiceVO.getAccountType());
        if (accountType == CustomerAccountTypeConstants.LEVEL_0) {
            isConventional = true;
            if (isConsumerApp) {
                logger.info("Account Opening Method id is : " + AccountOpeningMethodConstantsInterface.SELF_REGISTERATION);
                logger.info("[CommonCommandManagerImpl.updateOpenCustomerL0Request] Conventional Account Opening Flow started based on given accountType:" + webServiceVO.getAccountType());
            } else {
                logger.info("Account Opening Method id is : " + AccountOpeningMethodConstantsInterface.FONEPAY);
                logger.info("[CommonCommandManagerImpl.updateOpenCustomerL0Request] Conventional Account Opening Flow started based on given accountType:" + webServiceVO.getAccountType());

            }
        } else {
            if (isConsumerApp) {
                logger.info("Account Opening Method id is : " + AccountOpeningMethodConstantsInterface.SELF_REGISTERATION);
                logger.info("[CommonCommandManagerImpl.updateOpenCustomerL0Request] Paysys Account Opening Flow started based on given accountType:" + webServiceVO.getAccountType());
            } else {
                logger.info("Account Opening Method id is : " + AccountOpeningMethodConstantsInterface.FONEPAY);
                logger.info("[CommonCommandManagerImpl.updateOpenCustomerL0Request] Paysys Account Opening Flow started based on given accountType:" + webServiceVO.getAccountType());
            }
        }

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();

        messageVO.setCustomerAccountyType(String.valueOf(accountType));
        messageVO.setCnic(webServiceVO.getCnicNo());
        messageVO.setMobileNo(webServiceVO.getMobileNo());
        messageVO.setCustomerName(webServiceVO.getConsumerName());
        messageVO.setDob(webServiceVO.getDateOfBirth());
        messageVO.setCnicExpiry(webServiceVO.getCnicExpiry());

        if (!isConventional) {
            messageVO.setBirthPlace(webServiceVO.getBirthPlace());
            messageVO.setMotherName(webServiceVO.getMotherMaiden());
            messageVO.setPresentAddress(webServiceVO.getPresentAddress());
            //messageVO.setPermanentAddress(webServiceVO.getPermanentAddress());
        }

        try {
            if (!isConventional) {
                //** loading and updating  Customer Model
                CustomerModel customerModel = new CustomerModel();

                customerModel.setCustomerId(appUserModel.getCustomerId());
                baseWrapper.setBasePersistableModel(customerModel);
                customerModel = (CustomerModel) this.loadCustomer(baseWrapper).getBasePersistableModel();
                customerModel.setCustomerAccountTypeId(Long.valueOf(messageVO.getCustomerAccountyType()));
                customerModel.setUpdatedOn(nowDate);
                customerModel.setContactNo(messageVO.getMobileNo());
                customerModel.setMobileNo(messageVO.getMobileNo());
                customerModel.setName(messageVO.getCustomerName());
                customerModel.setBirthPlace(messageVO.getBirthPlace());
                if (!isConsumerApp) {
                    customerModel.setWebServiceEnabled(false);
                    customerModel.setFonePayEnabled(true);
                }

                customerDAO.saveOrUpdate(customerModel);

                //	baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);


                //***loading and updating App User Model
                AppUserModel apUserModel = new AppUserModel();

                String[] nameArray = messageVO.getCustomerName().split(" ");
                appUserModel.setFirstName(nameArray[0]);
                if (nameArray.length > 1) {
                    appUserModel.setLastName(messageVO.getCustomerName().substring(
                            appUserModel.getFirstName().length() + 1));
                } else {
                    appUserModel.setLastName(nameArray[0]);
                }
                appUserModel.setCountryId(1L);
                appUserModel.setMobileNo(messageVO.getMobileNo());
                String nicWithoutHyphins = messageVO.getCnic().replace("-", "");
                appUserModel.setNic(nicWithoutHyphins);
                appUserModel.setDob(dateFormat.parse(messageVO.getDob()));
                appUserModel.setMotherMaidenName(messageVO.getMotherName());

                appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);

                appUserModel.setUpdatedOn(nowDate);

                appUserDAO.saveOrUpdate(appUserModel);

                //baseWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, apUserModel);


                //*** loading and updating User Device Accounts Model
                UserDeviceAccountsModel uDeviceAccountsModel = new UserDeviceAccountsModel();

                uDeviceAccountsModel.setUserId(appUserModel.getUsername());
                //sBaseWrapper.setBasePersistableModel(uDeviceAccountsModel);
                uDeviceAccountsModel = (UserDeviceAccountsModel) this.loadUserDeviceAccountByUserId(appUserModel.getUsername());

                uDeviceAccountsModel.setAccountEnabled(true);
                uDeviceAccountsModel.setAccountExpired(false);
                uDeviceAccountsModel.setAccountLocked(false);
                uDeviceAccountsModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                uDeviceAccountsModel.setUpdatedOn(nowDate);
                uDeviceAccountsModel.setCredentialsExpired(false);

                userDeviceAccountsDAO.saveOrUpdate(uDeviceAccountsModel);

                //	baseWrapper.putObject(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNT_MODEL, uDeviceAccountsModel);

                //*** loading and updating Customer Address Model

                AddressModel addressModel = new AddressModel();

                addressModel.setFullAddress(messageVO.getPresentAddress());
                addressModel.setHouseNo(messageVO.getPresentAddress());

                addressDAO.saveOrUpdate(addressModel);

                //baseWrapper.putObject(CommandFieldConstants.KEY_PRESENT_ADDR, addressModel);


                //*** loading and updating Account Model
                OLAVO olaVo = new OLAVO();
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

                //*********************************************************************
                Long newRegistrationStateId = appUserModel.getRegistrationStateId();
                String transactionIDForSAF = "";
                //Settle Account Opening commission - if registration state is updated to VERIFIED
                if ((newRegistrationStateId != null && newRegistrationStateId.longValue() == RegistrationStateConstantsInterface.VERIFIED)
                        && (oldRegistrationStateId == null || oldRegistrationStateId.longValue() != RegistrationStateConstantsInterface.VERIFIED)) {
                    transactionIDForSAF = this.mfsAccountManager.settleAccountOpeningCommission(appUserModel.getCustomerId());
                }
                //*********************************************************************


            } else {
                List<CustomerPictureModel> customerPictureModelList = new ArrayList<>();
                CustomerPictureModel customerPictureModel = new CustomerPictureModel();
                CustomerPictureModel customerPicModel = new CustomerPictureModel();
                CustomerModel customerModelList = new CustomerModel();
                if (!isConsumerApp) {
                    customerModelList = this.customerDAO.findByPrimaryKey(appUserModel.getCustomerId());
                    if (customerModelList != null) {
                        customerModelList.setFonePayEnabled(true);
                        this.customerDAO.saveOrUpdate(customerModelList);
                    }
                }
                customerPictureModel.setCustomerId(appUserModel.getCustomerId());
                customerPictureModel.setDiscrepant(true);
                customerPictureModelList = this.customerPictureDAO.findByExample(customerPictureModel).getResultsetList();
                if (customerPictureModelList != null && customerPictureModelList.size() > 0) {
                    if (customerPictureModelList.size() >= 2) {
                        customerPicModel = customerPictureDAO.getCustomerPictureByTypeId(PictureTypeConstants.CUSTOMER_PHOTO.longValue(), appUserModel.getCustomerId());


                        customerPicModel.setPicture(arrayCustomerPictures.get(0).getPicture());
                        customerPicModel.setPictureTypeId(arrayCustomerPictures.get(0).getPictureTypeId());
                        customerPicModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                        customerPicModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        customerPicModel.setDiscrepant(false);
                        customerPicModel.setUpdatedOn(nowDate);

                        customerPictureDAO.saveOrUpdate(customerPicModel);

                        customerPictureModel = new CustomerPictureModel();

                        customerPicModel = customerPictureDAO.getCustomerPictureByTypeId(PictureTypeConstants.ID_FRONT_SNAPSHOT.longValue(), appUserModel.getCustomerId());

                        customerPicModel.setPicture(arrayCustomerPictures.get(1).getPicture());
                        customerPicModel.setPictureTypeId(arrayCustomerPictures.get(1).getPictureTypeId());
                        customerPicModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                        customerPicModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                        customerPicModel.setDiscrepant(false);
                        customerPicModel.setUpdatedOn(nowDate);

                        customerPictureDAO.saveOrUpdate(customerPicModel);

                        //baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_PICTURES_COLLECTION, arrayCustomerPictures);
                    } else {
                        if (PictureTypeConstants.ID_FRONT_SNAPSHOT.equals(customerPictureModelList.get(0).getPictureTypeId())) {
                            customerPicModel = new CustomerPictureModel();
                            customerPicModel = customerPictureDAO.getCustomerPictureByTypeId(PictureTypeConstants.ID_FRONT_SNAPSHOT.longValue(), appUserModel.getCustomerId());

                            customerPicModel.setPicture(arrayCustomerPictures.get(0).getPicture());
                            customerPicModel.setPictureTypeId(arrayCustomerPictures.get(0).getPictureTypeId());
                            customerPicModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                            customerPicModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                            customerPicModel.setDiscrepant(false);
                            customerPicModel.setUpdatedOn(nowDate);

                            customerPictureDAO.saveOrUpdate(customerPicModel);
                        } else {
                            customerPicModel = new CustomerPictureModel();
                            customerPicModel = customerPictureDAO.getCustomerPictureByTypeId(PictureTypeConstants.CUSTOMER_PHOTO.longValue(), appUserModel.getCustomerId());

                            customerPicModel.setPicture(arrayCustomerPictures.get(0).getPicture());
                            customerPicModel.setPictureTypeId(arrayCustomerPictures.get(0).getPictureTypeId());
                            customerPicModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
                            customerPicModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                            customerPicModel.setDiscrepant(false);
                            customerPicModel.setUpdatedOn(nowDate);

                            customerPictureDAO.saveOrUpdate(customerPicModel);
                        }
                    }
                }

                appUserModel.setRegistrationStateId(RegistrationStateConstants.REQUEST_RECEIVED);
                appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);

                appUserModel.setUpdatedOn(nowDate);
                appUserDAO.saveOrUpdate(appUserModel);


            }

        } catch (Exception exp) {
            exp.printStackTrace();
            throw new FrameworkCheckedException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG));
        }
    }

    public void setCustomerPendingTrxManager(CustomerPendingTrxManager customerPendingTrxManager) {
        this.customerPendingTrxManager = customerPendingTrxManager;
    }

    public HandlerDAO getHandlerDAO() {
        return handlerDAO;
    }

    public void setHandlerDAO(HandlerDAO handlerDAO) {
        this.handlerDAO = handlerDAO;
    }

    public void setSmartMoneyAccountManager(
            SmartMoneyAccountManager smartMoneyAccountManager) {
        this.smartMoneyAccountManager = smartMoneyAccountManager;
    }

    public void setIvrAuthenticationRequestQueueSender(IvrAuthenticationRequestQueue ivrAuthenticationRequestQueueSender) {
        this.ivrAuthenticationRequestQueueSender = ivrAuthenticationRequestQueueSender;
    }

    public void setCreditAccountQueingPreProcessor(CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
        this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
    }

    @Override
    public DemographicsManager getDemographicsManager() {
        return demographicsManager;
    }

    public void setDemographicsManager(DemographicsManager demographicsManager) {
        this.demographicsManager = demographicsManager;
    }

    @Override
    public AgentBvsStatManager getAgentBvsStatManager() {
        return agentBvsStatManager;
    }

    public void setAgentBvsStatManager(AgentBvsStatManager agentBvsStatManager) {
        this.agentBvsStatManager = agentBvsStatManager;
    }

    public AppManager getAppManager() {
        return appManager;
    }

    public void setAppManager(AppManager appManager) {
        this.appManager = appManager;
    }

    @Override
    public AppUserManager getAppUserManager() {
        return appUserManager;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    @Override
    public MiniTransactionManager getMiniTransactionManager() {
        return miniTransactionManager;
    }

    @Override
    public void setMiniTransactionManager(MiniTransactionManager miniTransactionManager) {
        this.miniTransactionManager = miniTransactionManager;
    }

    public void setAgentTransferRuleDAO(AgentTransferRuleDAO agentTransferRuleDAO) {
        this.agentTransferRuleDAO = agentTransferRuleDAO;
    }

    public void setAddressDAO(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public void setAccountControlManager(AccountControlManager accountControlManager) {
        this.accountControlManager = accountControlManager;
    }

    public void setCustomerAddressesDAO(CustomerAddressesDAO customerAddressesDAO) {
        this.customerAddressesDAO = customerAddressesDAO;
    }

    @Override
    public FonePayManager getFonePayManager() {
        // TODO Auto-generated method stub
        return fonePayManager;
    }

    public void setFonePayManager(FonePayManager fonePayManager) {
        this.fonePayManager = fonePayManager;
    }

    @Override
    public AgentSegmentRestrictionManager getAgentSegmentRestriction() {
        return agentSegmentRestrictionManager;
    }

    public void setAgentSegmentRestrictionManager(AgentSegmentRestrictionManager agentSegmentRestrictionManager) {
        this.agentSegmentRestrictionManager = agentSegmentRestrictionManager;
    }

    @Override
    public AccountManager getAccountManager() {
        return accountManager;
    }

    @Override
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    public FaqManager getFaqManager() {
        return faqManager;
    }

    public void setFaqManager(FaqManager faqManager) {
        this.faqManager = faqManager;
    }

    public void setCustTransManager(CustTransManager custTransManager) {
        this.custTransManager = custTransManager;
    }

    public void setOtpAuthentication(OTPAuthentication otpAuthentication) {
        this.otpAuthentication = otpAuthentication;
    }

    public WorkFlowWrapper makeOTPGeneration(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        otpAuthentication.initiate(workFlowWrapper);
        return workFlowWrapper;
    }

    public WorkFlowWrapper makeOTPValidation(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        otpAuthentication.validate(workFlowWrapper);
        return workFlowWrapper;
    }

    @Override
    public void makeUserBlocked(AppUserModel appUserModel) throws Exception {

        this.blockSmartMoneyAccount(appUserModel);

        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel(); // UserDeviceAccount is updated when MPIN Retry Limit EXHAUSTED
        userDeviceAccountsModel.setAppUserId(appUserModel.getAppUserId());
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
        SearchBaseWrapper sbWrapper = new SearchBaseWrapperImpl();
        sbWrapper.setBasePersistableModel(userDeviceAccountsModel);

        sbWrapper = this.loadUserDeviceAccounts(sbWrapper);
        if (null != sbWrapper.getBasePersistableModel()) {
            if (sbWrapper.getCustomList() != null && sbWrapper.getCustomList().getResultsetList().size() > 0) {
                userDeviceAccountsModel = (UserDeviceAccountsModel) sbWrapper.getCustomList().getResultsetList().get(0);
                userDeviceAccountsModel.setCredentialsExpired(true);
                userDeviceAccountsModel.setAccountLocked(true);
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                baseWrapper.setBasePersistableModel(userDeviceAccountsModel);

                logger.info("[Blocking User Device Account after retry count exhausted. Mobile No:" + appUserModel.getMobileNo());

                this.updateUserDeviceAccounts(baseWrapper);

            } else {
                logger.error("No Entry found in User Device Accounts against Mobile No: " + appUserModel.getMobileNo());
            }
        } else {
            logger.error("No Entry found in User Device Accounts against Mobile No: " + appUserModel.getMobileNo());
        }

    }

    @Override
    public VerisysDataDAO getVerisysDataHibernateDAO() {
        return verisysDataHibernateDAO;
    }

    public void setVerisysDataHibernateDAO(VerisysDataDAO verisysDataHibernateDAO) {
        this.verisysDataHibernateDAO = verisysDataHibernateDAO;
    }

    @Override
    public VirtualCardHibernateDAO getVirtualCardHibernateDAO() {
        return virtualCardHibernateDAO;
    }

    public void setVirtualCardHibernateDAO(VirtualCardHibernateDAO virtualCardHibernateDAO) {
        this.virtualCardHibernateDAO = virtualCardHibernateDAO;
    }

    @Override
    public void makeIVRCallForPinRegenerate(IvrRequestDTO ivrRequestDTO) throws FrameworkCheckedException {
        this.mfsAccountManager.initiateUserGeneratedPinIvrCall(ivrRequestDTO);
    }

    @Override
    public SwitchWrapper makeI8SBCall(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
        try {
            new ESBAdapter().makeI8SBCall(switchWrapper);
        } catch (Exception ex) {
            throw new FrameworkCheckedException(this.workflowExceptionTranslator.translateWorkFlowException(
                    ex, this.workflowExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION).getMessage());
        }
        return switchWrapper;
    }

    @Override
    public boolean validateChallanParams(String consumerNumber, String productCode) throws CommandException {
        //consumerNumber and productCode validations
        //Not null checks are already appplied in calling class
        if (consumerNumber.isEmpty() || productCode.isEmpty()) {
            logger.error("[CommonCommandManagerImpl.validateChallanParams  param(s) are empty. Consumer No : " + consumerNumber + " product Code: " + productCode);
            throw new CommandException("Consumer No or Product Code is empty", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
        }
        //Check for sql injection
        String[] matches = new String[]{"SELECT", "DROP", "TABLE"};
        boolean found = false;
        for (int i = 0; i < matches.length; i++) {
            if (consumerNumber.contains(matches[i]) || productCode.contains(matches[i])) {
                logger.error("[CommonCommandManagerImpl.validateChallanParams bad params Input. Consumer No : " + consumerNumber + " product Code: " + productCode);
                throw new CommandException("Consumer No or Product ID contains bad values", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);

            }
        }

        return true;
    }

    @Override
    public int getCustomerChallanCount(String mobileNo) throws FrameworkCheckedException {

        List<TransactionDetailMasterModel> listmodel = new ArrayList<>();
        try {
            return this.transactionDetailMasterDAO.getCustomerChallanCount(mobileNo);
        } catch (Exception ex) {
            throw new FrameworkCheckedException(this.workflowExceptionTranslator.translateWorkFlowException(
                    ex, this.workflowExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION).getMessage());
        }

    }

    @Override
    public long getPaidChallan(String consumerNo, String productCode) throws FrameworkCheckedException {
        try {
            return this.transactionDetailMasterDAO.getPaidChallan(consumerNo, productCode);
        } catch (Exception ex) {
            throw new FrameworkCheckedException(this.workflowExceptionTranslator.translateWorkFlowException(
                    ex, this.workflowExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION).getMessage());
        }

    }

    @Override
    public boolean getChallanStatus(String consumerNo, String productCode) throws FrameworkCheckedException {
        try {
            return this.billStatusDAO.CheckBillStatus(consumerNo, productCode);
        } catch (Exception e) {
            throw new FrameworkCheckedException(this.workflowExceptionTranslator.translateWorkFlowException(
                    e, this.workflowExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION).getMessage());
        }
    }

    //Challan payment is already in process or paid throws exception according to paidCound
    @Override
    public void throwsChallanException(Long paidCount) throws FrameworkCheckedException {
        if (SupplierProcessingStatusConstants.COMPLETED.equals(paidCount))
            throw new CommandException(MessageUtil.getMessage("i8sb.response.payment.03"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
        else if (SupplierProcessingStatusConstants.PROCESSING.equals(paidCount))
            throw new CommandException("Transaction is already Processing.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
        else if (SupplierProcessingStatusConstants.IN_PROGRESS.equals(paidCount))
            throw new CommandException("Transaction is already in Progress.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
    }

    @Override
    public void addChallanToQueue(String consumerNo, String productCode) throws FrameworkCheckedException {
        try {
            this.billStatusDAO.AddToProcessing(consumerNo, productCode);
        } catch (Exception e) {
            throw new FrameworkCheckedException(this.workflowExceptionTranslator.translateWorkFlowException(
                    e, this.workflowExceptionTranslator.INSERT_ACTION).getMessage());
        }

    }

    @Override
    public void removeChallanFromQueue(String consumerNo, String productCode) throws FrameworkCheckedException {
        try {
            this.billStatusDAO.DeleteFromProcessing(consumerNo, productCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserDeviceAccountListViewManager getUserDeviceAccountListViewManager() {
        return userDeviceAccountListViewManager;
    }

    public void setUserDeviceAccountListViewManager(UserDeviceAccountListViewManager userDeviceAccountListViewManager) {
        this.userDeviceAccountListViewManager = userDeviceAccountListViewManager;
    }

    @Override
    public BaseWrapper saveOrUpdateAccountOpeningHRARequest(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.saveOrUpdateAccountOpeningHRARequest()");
        }

        VerisysDataModel vo = (VerisysDataModel) baseWrapper.getObject(CommandFieldConstants.KEY_VARISYS_DATA_MODEL);

        AppUserModel appUserModel = (AppUserModel) baseWrapper.getObject(CommandFieldConstants.KEY_APP_USER_MODEL);

        CustomerModel customerModel = (CustomerModel) baseWrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MODEL);

        this.customerDAO.saveOrUpdate(customerModel);

        String currentAddress = null;
        String permanentAddress = null;
        String birthPlace = null;
        String motherMaidenName = null;
        String fname = CommonUtils.escapeUnicode(appUserModel.getFirstName());
        String lName = CommonUtils.escapeUnicode(appUserModel.getLastName());
        if (vo.getCurrentAddress() != null)
            currentAddress = CommonUtils.escapeUnicode(vo.getCurrentAddress());
        if (vo.getPermanentAddress() != null)
            permanentAddress = CommonUtils.escapeUnicode(vo.getPermanentAddress());
        if (vo.getPlaceOfBirth() != null)
            birthPlace = CommonUtils.escapeUnicode(vo.getPlaceOfBirth());
        if (vo.getMotherMaidenName() != null)
            motherMaidenName = CommonUtils.escapeUnicode(vo.getMotherMaidenName());

        vo.setName(fname);
        vo.setCurrentAddress(currentAddress);
        vo.setPlaceOfBirth(birthPlace);
        vo.setMotherMaidenName(motherMaidenName);
        vo.setPermanentAddress(permanentAddress);

        SmartMoneyAccountModel smartMoneyAccountModel = null;
        // (SmartMoneyAccountModel) baseWrapper.getObject(CommandFieldConstants.KEY_SMART_MONEY_ACCOUNT_MODEL);
        try {
            smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getObject(CommandFieldConstants.KEY_SMART_MONEY_ACCOUNT_MODEL);
            smartMoneyAccountModel.setCustomerIdCustomerModel(customerModel);
            this.linkPaymentModeDAO.saveOrUpdate(smartMoneyAccountModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_SMART_MONEY_ACCOUNT_MODEL, smartMoneyAccountModel);
        } catch (Exception ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constrainViolationException = (ConstraintViolationException) ex.getCause();

                String constraintName = "UK_SM_ACCOUNT";
                if (constrainViolationException.getConstraintName().indexOf(constraintName) != -1) {
                    throw new FrameworkCheckedException("Account nick already exists");
                }
            }
            ex.printStackTrace();
            throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        }
        OLAVO olaVo = (OLAVO) baseWrapper.getObject(CommandFieldConstants.KEY_ONLINE_ACCOUNT_MODEL);

        SwitchWrapper sWrapper = new SwitchWrapperImpl();
        sWrapper.setOlavo(olaVo);
        sWrapper.setBankId(smartMoneyAccountModel.getBankId());
        sWrapper.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);

        try {
            sWrapper = olaVeriflyFinancialInstitution.createAccount(sWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
        }

        if ("07".equals(olaVo.getResponseCode())) {
            throw new FrameworkCheckedException("NIC already exisits in the OLA accounts");
        }

        AccountInfoModel accountInfoModel = (AccountInfoModel) baseWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_INFO_MODEL);

        accountInfoModel.setCustomerId(appUserModel.getCustomerId());
        accountInfoModel.setAccountNo(olaVo.getPayingAccNo().toString());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        LogModel logmodel = new LogModel();
        logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
        veriflyBaseWrapper.setLogModel(logmodel);
        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB);

        AbstractFinancialInstitution abstractFinancialInstitution = null;
        try {
            BaseWrapper baseWrapperBank = new BaseWrapperImpl();
            BankModel bankModel = new BankModel();
            bankModel.setBankId(smartMoneyAccountModel.getBankId());
            baseWrapperBank.setBasePersistableModel(bankModel);
            abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapperBank);
            veriflyBaseWrapper = abstractFinancialInstitution.generatePin(veriflyBaseWrapper);
            if (!veriflyBaseWrapper.isErrorStatus()) {
                String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
                throw new FrameworkCheckedException(veriflyErrorMessage);
            }
            baseWrapper.putObject(CommandFieldConstants.KEY_PIN, veriflyBaseWrapper.getAccountInfoModel().getGeneratedPin());
        } catch (Exception e) {
            logger.error("Exception Occurred while generating pin of following customer " + appUserModel.getCustomerId());
            //e.printStackTrace();
            throw new FrameworkCheckedException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG));
        }
        //baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);

        List<CustomerRemitterModel> customerRemitterModelList = null;
        try {
            customerRemitterModelList = (List<CustomerRemitterModel>) baseWrapper.getObject(CommandFieldConstants.CUSTOMER_REMITTENCE_KEY);
            if ((null != customerRemitterModelList) && (customerRemitterModelList.size() > 0)) {
                for (CustomerRemitterModel customerRemitterModel : customerRemitterModelList) {
                    customerRemitterModel.setCustomerId(customerModel.getCustomerId());
//					customerModel.addCustomerIdCustomerRemitterModel(customerRemitterModel);
                }
            }
            //customerModel.setCustomerIdCustomerRemitterModelList(customerRemitterModelList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != customerRemitterModelList && customerRemitterModelList.size() > 0) {
            this.custTransManager.saveOrUpdateCustomerRemitter(customerRemitterModelList);
        }


        vo.setAppUserId(appUserModel.getAppUserId());
			/*try{
				this.verisysDataHibernateDAO.saveOrUpdate(vo);
			}
			catch(Exception e){
				e.printStackTrace();
				throw new FrameworkCheckedException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG));
			}*/
        if (null != baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR)) {
            currentAddress = ((AddressModel) baseWrapper.getObject(CommandFieldConstants.KEY_PRESENT_ADDR)).getHouseNo();
        }
        if (null != baseWrapper.getObject(CommandFieldConstants.KEY_PERMANENT_ADDR)) {
            currentAddress = ((AddressModel) baseWrapper.getObject(CommandFieldConstants.KEY_PERMANENT_ADDR)).getHouseNo();
        }
        OLAVO olaModel = (OLAVO) sWrapper.getOlavo();//baseWrapper.getObject(CommandFieldConstants.KEY_ONLINE_ACCOUNT_MODEL);
        //}
        accountInfoModel = (AccountInfoModel) veriflyBaseWrapper.getAccountInfoModel();
        CustomerAddressesModel customerAddressesModel = this.customerAddressesDAO.findAddressByCustomerIdAndAddressType(
                customerModel.getCustomerId(), AddressTypeConstants.PRESENT_HOME_ADDRESS);
        Long addressId = null;
        if (customerAddressesModel != null)
            addressId = customerAddressesModel.getAddressId();
        else
            addressId = 242987L;
        AddressModel addressModel = addressDAO.findAddressById(addressId);
        NADRADataVO nadraDataVO = new NADRADataVO();
        nadraDataVO.setAccountInfoId(accountInfoModel.getAccountInfoId());
        nadraDataVO.setAccountHolderId(olaModel.getAccountHolderId());
        nadraDataVO.setAddressId(addressId);
        nadraDataVO.setAppUserId(appUserModel.getAppUserId());
        nadraDataVO.setCustomerId(customerModel.getCustomerId());
        nadraDataVO.setfName(fname);
        nadraDataVO.setlName(lName);
        if (currentAddress != null)
            nadraDataVO.setAddress(currentAddress);
        else if (permanentAddress != null)
            nadraDataVO.setAddress(currentAddress);
        nadraDataVO.setMotherMaidenName(motherMaidenName);
        nadraDataVO.setBirthPlace(birthPlace);
        nadraDataVO.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
        try {
            this.virtualCardHibernateDAO.updateRegData(nadraDataVO);
        } catch (SQLException e) {
            logger.error("Error Occurred in HRA Account Opening :: " + e + " :: " + e.getMessage());
            throw new FrameworkCheckedException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG));
        }
        try {
            String customerCity = null;
            if (addressModel.getCityIdCityModel() != null && addressModel.getCityIdCityModel().getName() != null)
                customerCity = addressModel.getCityIdCityModel().getName();
            I8SBSwitchControllerRequestVO requestVO = ESBAdapter.preparePayMTNCRequest(appUserModel.getNic(), appUserModel.getMobileNo(),
                    customerAddressesModel.getAddressId().toString(), appUserModel.getFirstName(), appUserModel.getLastName(), customerCity);
            SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
            i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
            i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
            I8SBSwitchControllerResponseVO responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();
            String responseCode = ESBAdapter.processI8sbResponseCode(responseVO, false);
        } catch (Exception ex) {
            logger.error("Parsing response code has some error with error message :: " + ex.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.saveOrUpdateAccountOpeningHRARequest");
        }
        return baseWrapper;
    }

    @Override
    public void saveOrUpdateCustomerAccountL0ToL1(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            AppUserModel appUserModel = (AppUserModel) baseWrapper.getObject(CommandFieldConstants.KEY_APP_USER_MODEL);

            Long customerId = appUserModel.getCustomerId();

            CustomerModel customerModel = (CustomerModel) baseWrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MODEL);

            VerisysDataModel vo = (VerisysDataModel) baseWrapper.getObject(CommandFieldConstants.KEY_VARISYS_DATA_MODEL);

            String address = null;
            String[] nameArray = vo.getName().split(" ");
            String fName = nameArray[0];
            String lName = "";
            if (nameArray.length > 1) {
                lName = vo.getName().substring(
                        fName.length() + 1);
            } else {
                lName = " ";
            }

            fName = escapeUnicode(fName);
            if (!(lName.equals(" "))) {
                lName = escapeUnicode(lName);
            }
            String motherMaidenName = escapeUnicode(vo.getMotherMaidenName());
            if (appUserModel.getAddress1() != null)
                address = escapeUnicode(vo.getCurrentAddress());
            String birthPlace = escapeUnicode(vo.getPlaceOfBirth());

            appUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            appUserModel.setCountryId(1L);

            Long stateId = appUserModel.getAccountStateId();
            if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT)) {
                appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_COLD);
                appUserModel.setPrevRegistrationStateId(appUserModel.getRegistrationStateId());
                appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                appUserModel.setUpdatedOn(new Date());
                baseWrapper.setBasePersistableModel(appUserModel);
            }

            this.appUserDAO.saveOrUpdate(appUserModel);

            customerDAO.updateCustomerToUpgradeCustomerAccount(customerId);

            SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();

            if (smartMoneyAccountModel.getIsDebitBlocked() != null && smartMoneyAccountModel.getIsDebitBlocked().equals(true)) {
                smartMoneyAccountModel.setIsDebitBlocked(false);
                smartMoneyAccountModel.setDebitBlockAmount(0.0);
                smartMoneyAccountModel.setUpdatedOn(new Date());
                baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
            }
            this.linkPaymentModeDAO.saveOrUpdate(smartMoneyAccountModel);

            vo.setName(CommonUtils.escapeUnicode(vo.getName()));
            if (appUserModel.getMotherMaidenName() != null)
                vo.setMotherMaidenName(CommonUtils.escapeUnicode(vo.getMotherMaidenName()));
            if (appUserModel.getAddress1() != null)
                vo.setCurrentAddress(CommonUtils.escapeUnicode(vo.getCurrentAddress()));
            if (customerModel.getBirthPlace() != null)
                vo.setPlaceOfBirth(CommonUtils.escapeUnicode(vo.getPlaceOfBirth()));
            vo.setCnic(appUserModel.getNic());
            vo.setAccountClosed(false);
            vo.setAppUserId(appUserModel.getAppUserId());
            vo.setCreatedOn(new Date());
            vo.setUpdatedOn(new Date());
            vo.setTranslated(false);
            if (appUserModel.getAddress2() != null)
                vo.setPermanentAddress(CommonUtils.escapeUnicode(vo.getPermanentAddress()));
            vo.setAppUserId(appUserModel.getAppUserId());
            getVerisysDataHibernateDAO().saveNadraData(vo);

            BaseWrapper accountWrapper = new BaseWrapperImpl();
            AccountModel accountModel = this.accountManager.getAccountModelByCNIC(appUserModel.getNic());
            accountModel.setCustomerAccountTypeId(CustomerAccountTypeConstants.LEVEL_1);

            accountWrapper.setBasePersistableModel(accountModel);
            this.accountManager.updateAccount(accountWrapper);


            AccountInfoModel accountInfoModel = this.getAccountInfoModel(customerId, smartMoneyAccountModel.getPaymentModeId());
            OLAVO olavo = this.loadAccountInfoFromOLA(appUserModel, smartMoneyAccountModel);
            CustomerAddressesModel customerAddressesModel = this.customerAddressesDAO.findAddressByCustomerIdAndAddressType(customerId, AddressTypeConstants.PRESENT_HOME_ADDRESS);

            NADRADataVO nadraDataVO = new NADRADataVO();
            nadraDataVO.setAccountInfoId(accountInfoModel.getAccountInfoId());
            nadraDataVO.setAccountHolderId(olavo.getAccountHolderId());
            nadraDataVO.setAddressId(customerAddressesModel.getAddressId());
            nadraDataVO.setAppUserId(appUserModel.getAppUserId());
            nadraDataVO.setCustomerId(customerId);
            nadraDataVO.setfName(fName);
            nadraDataVO.setlName(lName);
            nadraDataVO.setAddress(address);
            nadraDataVO.setMotherMaidenName(motherMaidenName);
            nadraDataVO.setBirthPlace(birthPlace);
            try {
                this.virtualCardHibernateDAO.updateRegData(nadraDataVO);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new FrameworkCheckedException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG));

            }
        } catch (Exception ex) {
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
    }

    @Override
    public CustomerModel getCustomerModelById(Long customerId) throws CommandException {

        CustomerModel customerModel = this.customerDAO.findByPrimaryKey(customerId);
        return customerModel;
    }

    @Override
    public CustomerModel getCustomerModelByMobileNumber(String mobileNo) throws CommandException {
        CustomerModel customerModel = this.customerDAO.loadCustomerModelByMobileNo(mobileNo);
        return customerModel;
    }

    @Override
    public SmartMoneyAccountModel getSmartMoneyAccountByCustomerIdAndPaymentModeId(SmartMoneyAccountModel smartMoneyAccountModel) throws CommandException {
        CustomList<SmartMoneyAccountModel> customList = this.smartMoneyAccountDAO.findByExample(smartMoneyAccountModel);
        SmartMoneyAccountModel sma = null;
        if (customList.getResultsetList().size() > 0) {
            sma = customList.getResultsetList().get(0);
        }
        return sma;
    }

    @Override
    public void intimateAppInSnapForSendMoneyRequiresNewTransaction(WorkFlowWrapper workFlowWrapper) {
        try {
            I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
            requestVO = esbAdapter.prepareRequestVoForAppInSnap(I8SBConstants.RequestType_AppInSnap_CustomerDataSet);
            requestVO.setSenderMobile(workFlowWrapper.getSenderWalkinCustomerModel().getMobileNumber());
            requestVO.setSenderCnic(workFlowWrapper.getSenderWalkinCustomerModel().getCnic());
            requestVO.setRecieverMobileNo(workFlowWrapper.getRecipientWalkinCustomerModel().getMobileNumber());
            requestVO.setRecieverCnic(workFlowWrapper.getRecipientWalkinCustomerModel().getCnic());
            requestVO.setTransactionId(workFlowWrapper.getTransactionDetailMasterModel().getTransactionId().toString());
            requestVO.setTransactionAmount(workFlowWrapper.getTransactionAmount().toString());
            requestVO.setTransactionDateTime((new Date()).toString());
            requestVO.setSenderCity(workFlowWrapper.getObject(CommandFieldConstants.KEY_SENDER_CITY).toString());
            requestVO.setRecieverCity(workFlowWrapper.getObject(CommandFieldConstants.KEY_RECEIVER_CITY).toString());
            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
            switchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
            esbAdapter.makeI8SBCall(switchWrapper);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public TaxRegimeDAO getTaxRegimeDAO() {
        return taxRegimeDAO;
    }

    public void setTaxRegimeDAO(TaxRegimeDAO taxRegimeDAO) {
        this.taxRegimeDAO = taxRegimeDAO;
    }

    @Override
    public String getAccountBalance(AccountInfoModel accountInfoModel, SmartMoneyAccountModel smartMoneyAccountModel) throws Exception {
        AccountModel accountModel = new AccountModel();
        accountModel.setAccountNumber(com.inov8.ola.util.EncryptionUtil.encryptAccountNo(accountInfoModel.getAccountNo()));
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(accountModel);
        baseWrapper = this.getAccountManager().loadAccount(baseWrapper);
        accountModel = (AccountModel) baseWrapper.getBasePersistableModel();
        String balance = accountModel.getBalance();
        return balance;
    }

    @Override
    public WorkFlowWrapper calculateDebitCardFee(String mobileNo, String cNic, AppUserModel appUserModel, CustomerModel customerModel,
                                                 ProductModel productModel, Long productId, Long cardFeeTypeId, Long deviceTypeId, DebitCardModel debitCardModel) throws FrameworkCheckedException {
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        Long distributorId = null;
        CommissionWrapper commissionWrapper;
        if (appUserModel == null) {
            appUserModel = getAppUserManager().getAppUserWithRegistrationState(mobileNo, cNic, RegistrationStateConstantsInterface.VERIFIED);
        }
        if (customerModel == null) {
            CustomerModel cModel = new CustomerModel();
            cModel.setCustomerId(appUserModel.getCustomerId());
            customerModel = getCustomerModelById(appUserModel.getCustomerId());
        }
        if (productModel == null || (productModel != null && productModel.getProductId() == null)) {
            BaseWrapper baseWrapper1 = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(productId);
            baseWrapper1.setBasePersistableModel(productModel);
            baseWrapper1 = loadProduct(baseWrapper1);
            productModel = (ProductModel) baseWrapper1.getBasePersistableModel();
        }
        if (productId.equals(ProductConstantsInterface.DEBIT_CARD_ISSUANCE)) {
            RetailerContactModel retailerContactModel = retailerContactDAO.findByPrimaryKey(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
            RetailerModel retailerModel = retailerDAO.findByPrimaryKey(retailerContactModel.getRetailerId());
            distributorId = retailerModel.getDistributorId();
        }
        workFlowWrapper.setProductModel(productModel);
        AccountInfoModel accountInfoModel = null;
        try {
            accountInfoModel = getAccountInfoModel(appUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }
        SmartMoneyAccountModel smartMoneyAccountModel = getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel,
                PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        String customerBalance = null;
        try {
            customerBalance = getAccountBalance(accountInfoModel, smartMoneyAccountModel);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }
        Double fee = 0.0D;
        CardFeeRuleModel cardFeeRuleModel = new CardFeeRuleModel(CardTypeConstants.DEBIT_CARD, UserTypeConstantsInterface.CUSTOMER,
                customerModel.getSegmentId(), distributorId, cardFeeTypeId, customerModel.getCustomerAccountTypeId());
        if (UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
            cardFeeRuleModel.setMnoId(50028L);
        else
            cardFeeRuleModel.setMnoId(50027L);
        CardFeeRuleModel model = getCardConfigurationManager().loadCardFeeRuleModel(cardFeeRuleModel);
        if (model != null) {
            fee = model.getAmount();
//            if (model.getIsInstallments() != null && model.getIsInstallments()) {
//                fee = model.getInstallmentAmount();
//            } else {
//                fee = model.getAmount();
//            }
        }

//		if(Double.parseDouble(customerBalance) < fee)
//			throw new CommandException(MessageUtil.getMessage("debit.card.req.low.balance"),ErrorCodes.INSUFFICIENT_BALANCE_FOR_DEBIT_CARD_ISSUANCE, ErrorLevel.MEDIUM, new Throwable());
        workFlowWrapper.setTransactionAmount(fee);
        workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        TransactionModel transactionModel = new TransactionModel();
        workFlowWrapper.setProductModel(productModel);
        TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
        transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.DEBIT_CARD_CW_TX);
        workFlowWrapper.setProductModel(productModel);
        workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setSegmentId(customerModel.getSegmentId());
        workFlowWrapper.setSegmentModel(segmentModel);
        DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
        deviceTypeModel.setDeviceTypeId(deviceTypeId);
        workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
        workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
        workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(deviceTypeId);
        transactionModel.setTransactionAmount(Double.valueOf(fee));
        workFlowWrapper.setTransactionModel(transactionModel);
        workFlowWrapper.setTaxRegimeModel(customerModel.getTaxRegimeIdTaxRegimeModel());
        if (ThreadLocalAppUser.getAppUserModel() == null)
            ThreadLocalAppUser.setAppUserModel(appUserModel);
        commissionWrapper = this.calculateCommission(workFlowWrapper);
        CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap()
                .get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);
        Double totalAmount = commissionAmountsHolder.getTotalAmount();
        Double transactionAmount = null;
//        if (workFlowWrapper.getCommissionAmountsHolder().getExclusivePercentAmount() > 0.0 || workFlowWrapper.getCommissionAmountsHolder().getExclusiveFixAmount() > 0.0) {
//
//            if (commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID) != null) {
//                transactionAmount = commissionAmountsHolder.getTransactionAmount() + commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
//            }
//        }else {
        transactionAmount = commissionAmountsHolder.getTransactionAmount();
//        }
        if (Double.parseDouble(customerBalance) < transactionAmount && Double.parseDouble(customerBalance) < fee) {
            if (!cardFeeTypeId.equals(CardConstantsInterface.CARD_FEE_TYPE_ISSUANCE)) {
                getDebitCardManager().saveDebitCardChargesSafRepoRequiresNewTransaction(debitCardModel, cardFeeTypeId, productId, transactionAmount);
            }
            throw new CommandException(MessageUtil.getMessage("debit.card.req.low.balance"), ErrorCodes.INSUFFICIENT_BALANCE_FOR_DEBIT_CARD_ISSUANCE, ErrorLevel.MEDIUM, new Throwable());
        }
        return workFlowWrapper;
    }

    @Override
    public WorkFlowWrapper calculateCardFee(String mobileNo, String cNic, AppUserModel appUserModel, CustomerModel customerModel, ProductModel productModel, Long productId, Long cardFeeTypeId, Long deviceTypeId, DebitCardModel cardModel) throws FrameworkCheckedException {
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        Long distributorId = null;
        CommissionWrapper commissionWrapper;
        if (appUserModel == null) {
            appUserModel = getAppUserManager().getAppUserWithRegistrationState(mobileNo, cNic, RegistrationStateConstantsInterface.VERIFIED);
        }
        if (customerModel == null) {
            CustomerModel cModel = new CustomerModel();
            cModel.setCustomerId(appUserModel.getCustomerId());
            customerModel = getCustomerModelById(appUserModel.getCustomerId());
        }
        if (productModel == null || (productModel != null && productModel.getProductId() == null)) {
            BaseWrapper baseWrapper1 = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(productId);
            baseWrapper1.setBasePersistableModel(productModel);
            baseWrapper1 = loadProduct(baseWrapper1);
            productModel = (ProductModel) baseWrapper1.getBasePersistableModel();
        }

        workFlowWrapper.setProductModel(productModel);
        AccountInfoModel accountInfoModel = null;
        try {
            accountInfoModel = getAccountInfoModel(appUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }
        SmartMoneyAccountModel smartMoneyAccountModel = getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel,
                PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        String customerBalance = null;
        try {
            customerBalance = getAccountBalance(accountInfoModel, smartMoneyAccountModel);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }
        Double fee = 0.0D;
        CardFeeRuleModel cardFeeRuleModel = new CardFeeRuleModel(CardTypeConstants.DEBIT_CARD, UserTypeConstantsInterface.CUSTOMER,
                customerModel.getSegmentId(), distributorId, cardFeeTypeId, customerModel.getCustomerAccountTypeId());
        if (UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
            cardFeeRuleModel.setMnoId(50028L);
        else
            cardFeeRuleModel.setMnoId(50027L);
        CardFeeRuleModel model = getCardConfigurationManager().loadCardFeeRuleModel(cardFeeRuleModel);
        if (model != null)
            fee = model.getAmount();
//		if(Double.parseDouble(customerBalance) < fee)
//			throw new CommandException(MessageUtil.getMessage("debit.card.req.low.balance"),ErrorCodes.INSUFFICIENT_BALANCE_FOR_DEBIT_CARD_ISSUANCE, ErrorLevel.MEDIUM, new Throwable());
        workFlowWrapper.setTransactionAmount(fee);
        workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        TransactionModel transactionModel = new TransactionModel();
        workFlowWrapper.setProductModel(productModel);
        TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
        transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.FEE_PAYMENT_TX);
        workFlowWrapper.setProductModel(productModel);
        workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setSegmentId(customerModel.getSegmentId());
        workFlowWrapper.setSegmentModel(segmentModel);
        DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
        deviceTypeModel.setDeviceTypeId(deviceTypeId);
        workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
        workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
        workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(deviceTypeId);
        transactionModel.setTransactionAmount(Double.valueOf(fee));
        workFlowWrapper.setTransactionModel(transactionModel);
        workFlowWrapper.setTaxRegimeModel(customerModel.getTaxRegimeIdTaxRegimeModel());
        if (ThreadLocalAppUser.getAppUserModel() == null)
            ThreadLocalAppUser.setAppUserModel(appUserModel);
        commissionWrapper = this.calculateCommission(workFlowWrapper);
        CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap()
                .get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);
        Double totalAmount = commissionAmountsHolder.getTotalAmount();
        Double transactionAmount = null;
        transactionAmount = commissionAmountsHolder.getTransactionAmount();
        if (Double.parseDouble(customerBalance) < transactionAmount && Double.parseDouble(customerBalance) < fee) {
            throw new CommandException(MessageUtil.getMessage("debit.card.req.low.balance"), ErrorCodes.INSUFFICIENT_BALANCE_FOR_FEE_PAYMENT_API, ErrorLevel.MEDIUM, new Throwable());
        }
        return workFlowWrapper;
    }

    @Override
    public WorkFlowWrapper calculateDebitCardFeeForAPI(String mobileNo, String cNic, AppUserModel appUserModel, CustomerModel customerModel, ProductModel productModel,
                                                       Long productId, Long cardFeeTypeId, Long cardProductCode, Long deviceTypeId, DebitCardModel debitCardModel) throws FrameworkCheckedException {
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        Long distributorId = null;
        CommissionWrapper commissionWrapper;
        if (appUserModel == null) {
            appUserModel = getAppUserManager().getAppUserWithRegistrationState(mobileNo, cNic, RegistrationStateConstantsInterface.VERIFIED);
        }
        if (customerModel == null) {
            CustomerModel cModel = new CustomerModel();
            cModel.setCustomerId(appUserModel.getCustomerId());
            customerModel = getCustomerModelById(appUserModel.getCustomerId());
        }
        if (productModel == null || (productModel != null && productModel.getProductId() == null)) {
            BaseWrapper baseWrapper1 = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(productId);
            baseWrapper1.setBasePersistableModel(productModel);
            baseWrapper1 = loadProduct(baseWrapper1);
            productModel = (ProductModel) baseWrapper1.getBasePersistableModel();
        }
        if (productId.equals(ProductConstantsInterface.DEBIT_CARD_ISSUANCE)) {
            RetailerContactModel retailerContactModel = retailerContactDAO.findByPrimaryKey(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
            RetailerModel retailerModel = retailerDAO.findByPrimaryKey(retailerContactModel.getRetailerId());
            distributorId = retailerModel.getDistributorId();
        }
        workFlowWrapper.setProductModel(productModel);
        AccountInfoModel accountInfoModel = null;
        try {
            accountInfoModel = getAccountInfoModel(appUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }
        SmartMoneyAccountModel smartMoneyAccountModel = getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel,
                PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        String customerBalance = null;
        try {
            customerBalance = getAccountBalance(accountInfoModel, smartMoneyAccountModel);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }
        Double fee = 0.0D;
        CardFeeRuleModel cardFeeRuleModel = new CardFeeRuleModel(CardTypeConstants.DEBIT_CARD, UserTypeConstantsInterface.CUSTOMER,
                customerModel.getSegmentId(), distributorId, cardFeeTypeId, customerModel.getCustomerAccountTypeId(), cardProductCode);
        if (UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
            cardFeeRuleModel.setMnoId(50028L);
        else
            cardFeeRuleModel.setMnoId(50027L);
        CardFeeRuleModel model = getCardConfigurationManager().loadCardFeeRuleModel(cardFeeRuleModel);
        if (model != null) {
            fee = model.getAmount();
//            if (model.getIsInstallments() != null && model.getIsInstallments()) {
//                fee = model.getInstallmentAmount();
//                workFlowWrapper.setCardFeeRuleModel(model);
//            } else {
//                fee = model.getAmount();
//            }
        }
//		if(Double.parseDouble(customerBalance) < fee)
//			throw new CommandException(MessageUtil.getMessage("debit.card.req.low.balance"),ErrorCodes.INSUFFICIENT_BALANCE_FOR_DEBIT_CARD_ISSUANCE, ErrorLevel.MEDIUM, new Throwable());
        workFlowWrapper.setTransactionAmount(fee);
        workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        TransactionModel transactionModel = new TransactionModel();
        workFlowWrapper.setProductModel(productModel);
        TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
        transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.DEBIT_CARD_CW_TX);
        workFlowWrapper.setProductModel(productModel);
        workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setSegmentId(customerModel.getSegmentId());
        workFlowWrapper.setSegmentModel(segmentModel);
        DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
        deviceTypeModel.setDeviceTypeId(deviceTypeId);
        workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
        workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
        workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(deviceTypeId);
        transactionModel.setTransactionAmount(Double.valueOf(fee));
        workFlowWrapper.setTransactionModel(transactionModel);
        workFlowWrapper.setTaxRegimeModel(customerModel.getTaxRegimeIdTaxRegimeModel());
        if (ThreadLocalAppUser.getAppUserModel() == null)
            ThreadLocalAppUser.setAppUserModel(appUserModel);
        commissionWrapper = this.calculateCommission(workFlowWrapper);
        CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap()
                .get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);
        Double totalAmount = commissionAmountsHolder.getTotalAmount();
        Double transactionAmount = null;
//        if (workFlowWrapper.getCommissionAmountsHolder().getExclusivePercentAmount() > 0.0 || workFlowWrapper.getCommissionAmountsHolder().getExclusiveFixAmount() > 0.0) {
//
//            if (commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID) != null) {
//                transactionAmount = commissionAmountsHolder.getTransactionAmount() + commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
//            }
//        }else {
        transactionAmount = commissionAmountsHolder.getTransactionAmount();
//        }
        if (Double.parseDouble(customerBalance) < transactionAmount && Double.parseDouble(customerBalance) < fee) {
            if (!(cardFeeTypeId.equals(CardConstantsInterface.CARD_FEE_TYPE_ISSUANCE) || cardFeeTypeId.equals(CardConstantsInterface.CARD_FEE_TYPE_RE_ISSUANCE))) {
                getDebitCardManager().saveDebitCardChargesSafRepoRequiresNewTransaction(debitCardModel, cardFeeTypeId, productId, transactionAmount);
            }
            throw new CommandException(MessageUtil.getMessage("debit.card.req.low.balance"), ErrorCodes.INSUFFICIENT_BALANCE_FOR_DEBIT_CARD_ISSUANCE, ErrorLevel.MEDIUM, new Throwable());
        }
        return workFlowWrapper;
    }

    @Override
    public DebitCardModelDAO getDebitCardModelDao() {
        return debitCardModelDAO;
    }

    @Override
    public DebitCardMailingAddressDAO getDebitCardMailingAddressDAO() {
        return debitCardMailingAddressDAO;
    }

    public void setDebitCardMailingAddressDAO(DebitCardMailingAddressDAO debitCardMailingAddressDAO) {
        this.debitCardMailingAddressDAO = debitCardMailingAddressDAO;
    }

    @Override
    public ActionAuthorizationModelDAO getActionAuthorizationModelDAO() {
        return actionAuthorizationModelDAO;
    }

    public void setActionAuthorizationModelDAO(ActionAuthorizationModelDAO actionAuthorizationModelDAO) {
        this.actionAuthorizationModelDAO = actionAuthorizationModelDAO;
    }

    @Override
    public ActionAuthorizationFacade getActionAuthorizationFacade() {
        return actionAuthorizationFacade;
    }

    public void setActionAuthorizationFacade(ActionAuthorizationFacade actionAuthorizationFacade) {
        this.actionAuthorizationFacade = actionAuthorizationFacade;
    }

    @Override
    public SmartMoneyAccountModel getSmartMoneyAccountByAppUserModelAndPaymentModId(AppUserModel appUserModel, Long paymentModeId) throws CommandException {
        SmartMoneyAccountModel smartMoneyAccountModel = null;
        try {
            smartMoneyAccountModel = smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel, paymentModeId);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        return smartMoneyAccountModel;
    }

    @Override
    public SmartMoneyAccountModel getInActiveSMA(AppUserModel appUserModel, Long paymentModeId) throws CommandException {
        SmartMoneyAccountModel smartMoneyAccountModel = null;
        smartMoneyAccountModel = smartMoneyAccountManager.getInActiveSMA(appUserModel, paymentModeId, OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE);

        return smartMoneyAccountModel;
    }

    @Override
    public boolean removeCustomerDormancy(AppUserModel appUserModel, SmartMoneyAccountModel smartMoneyAccountModel) throws CommandException {
        boolean flag = true;
        if (smartMoneyAccountModel != null)
            smartMoneyAccountModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);
        try {
            UserDeviceAccountsModel uda = this.getUserDeviceAccountListViewManager().findUserDeviceByAppUserId(appUserModel.getAppUserId());
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
            UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
            userDeviceAccountsModel.setAppUserId(uda.getAppUserId());
            CustomerModel customerModel = new CustomerModel();
            Long usecaseId = PortalConstants.REACTIVATE_CUSTOMER_USECASE_ID;
            String action = "ACTIVE";
            boolean isLockUnlock = false;
            customerModel = getCustomerModelById(appUserModel.getCustomerId());
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, usecaseId);
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, 1L);
            baseWrapper.putObject("isLockUnlock", new Boolean(isLockUnlock));
            baseWrapper.putObject("accountStatus", "01");
            baseWrapper.putObject("mfsId", uda.getUserId());
            baseWrapper.putObject("paymentModeId", smartMoneyAccountModel.getPaymentModeId());
            baseWrapper.putObject("userDeviceAccountsModel", userDeviceAccountsModel);
            baseWrapper.putObject("appUserModel", appUserModel);
            baseWrapper.putObject("acType", customerModel.getCustomerAccountTypeId().toString());
            baseWrapper.putObject("action", action);

            mfsAccountManager.activateDeactivateMfsAccount(baseWrapper);

        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @Override
    public AppUserModel getAppUserModelByCNIC(String cnic) throws CommandException {
        AppUserModel appUserModel = null;
        try {
            appUserModel = appUserManager.loadAppUserByCNIC(cnic);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        return appUserModel;
    }

    @Override
    public OlaCustomerAccountTypeModel loadCustomerAccountTypeModelById(Long customerAccountTypeId) throws CommandException {
        Long[] customerAccountTypeIds = {customerAccountTypeId};
        OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = null;
        try {
            List<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList = olaCustomerAccountTypeDao.loadCustomerACTypes(customerAccountTypeIds);
            if (olaCustomerAccountTypeModelList.size() > 0)
                olaCustomerAccountTypeModel = (OlaCustomerAccountTypeModel) olaCustomerAccountTypeModelList.get(0);

        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        return olaCustomerAccountTypeModel;
    }

    @Override
    public AppUserModel loadAppUserByCNICAndAccountType(String cnic, Long[] appUserTypes) throws FrameworkCheckedException {
        AppUserModel appUserModel = appUserDAO.loadAppUserByCNICAndType(cnic, appUserTypes);
        return appUserModel;
    }

    @Override
    public AppUserModel loadAppUserByCNIC(String cnic) throws FrameworkCheckedException {
        AppUserModel appUserModel = appUserDAO.loadAppUserByCNIC(cnic);
        return appUserModel;
    }

    @Override
    public List getCustomerDetails(AppUserModel appUserModel) throws FrameworkCheckedException {
        return customerDetailsCommandDAO.getCustomerDetails(appUserModel);
    }

    @Override
    public List getAllBlinkData(BlinkCustomerModel blinkCustomerModel) throws FrameworkCheckedException {
        if (blinkCustomerModel != null) {
            return blinkCustomerModelDAO.findByExample(blinkCustomerModel).getResultsetList();
        } else {
            return null;
        }
    }

    @Override
    public List getAllCustomerData(CustomerModel customerModel) throws FrameworkCheckedException {
        if (customerModel != null) {
            return customerDAO.findByExample(customerModel).getResultsetList();
        } else {
            return null;
        }
    }

    @Override
    public BlinkCustomerModel getAllBlinkDataByBlinkID(Long blinkId) throws FrameworkCheckedException {
        BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
        blinkCustomerModel = blinkCustomerModelDAO.loadBlinkCustomerModelByBlinkCustomerId(blinkId);
        return blinkCustomerModel;
    }

    @Override
    public BlinkCustomerModel saveAllBlinkData(BlinkCustomerModel blinkCustomerModel) throws FrameworkCheckedException {
        return blinkCustomerModelDAO.saveOrUpdate(blinkCustomerModel);
    }

    @Override
    public CustomerModel getAllCustomerDataByCustomerID(Long customerId) throws FrameworkCheckedException {
        CustomerModel customerModel = new CustomerModel();
        customerModel = customerDAO.loadCustomerModelByCustomerId(customerId);
        return customerModel;
    }

    @Override
    public AccountModel getAccountModelByCnicAndCustomerAccountTypeAndStatusId(String cnic, Long customerAccountTypeId, Long statusId) {
        return accountManager.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(cnic, customerAccountTypeId, statusId);
    }

    @Override
    public Double getDailyConsumedBalance(Long accountId, Long transactionTypeId, Date date, Long handlerId) {
        Double consumed = 0.0;
        try {
            consumed = customerDetailsCommandDAO.getDailyConsumedBalance(accountId, transactionTypeId, date, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return consumed;
    }

    @Override
    public Double getDailyConsumedBalanceForIBFT(Long accountId, Long transactionTypeId, Date date, Long handlerId) {
        Double consumed = 0.0;
        try {
            consumed = customerDetailsCommandDAO.getDailyConsumedBalanceForIBFT(accountId, transactionTypeId, date, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return consumed;
    }

    @Override
    public Double getDailyConsumedBalanceForAgentIBFT(Long accountId, Long transactionTypeId, Date date, Long handlerId) {
        Double consumed = 0.0;
        try {
            consumed = customerDetailsCommandDAO.getDailyConsumedBalanceForAgentIBFT(accountId, transactionTypeId, date, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return consumed;
    }

    @Override
    public Double getConsumedBalanceByDateRange(Long accountId, Long transactionTypeId, Date staryDate, Date endDate) {
        Double consumed = 0.0;
        try {
            consumed = customerDetailsCommandDAO.getConsumedBalanceByDateRange(accountId, transactionTypeId, staryDate, endDate, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return consumed;
    }

    @Override
    public Double getConsumedBalanceByDateRangeForIBFT(Long accountId, Long transactionTypeId, Date startDate, Date endDate) {
        Double consumed = 0.0;
        try {
            consumed = customerDetailsCommandDAO.getConsumedBalanceByDateRangeForIBFT(accountId, transactionTypeId, startDate, endDate, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return consumed;
    }

    @Override
    public Double getConsumedBalanceByDateRangeForAgentIBFT(Long accountId, Long transactionTypeId, Date startDate, Date endDate) {
        Double consumed = 0.0;
        try {
            consumed = customerDetailsCommandDAO.getConsumedBalanceByDateRangeForAgentIBFT(accountId, transactionTypeId, startDate, endDate, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return consumed;
    }

    @Override
    public List<LimitModel> getLimitsByCustomerAccountType(Long customerAccountTypeId) throws FrameworkCheckedException {
        return customerDetailsCommandDAO.getLimitsByCustomerAccountType(customerAccountTypeId);
    }

    public void setCustomerDetailsCommandDAO(CustomerDetailsCommandDAO customerDetailsCommandDAO) {
        this.customerDetailsCommandDAO = customerDetailsCommandDAO;
    }

    public ApiCityDAO getApiCityDAO() {
        return apiCityDAO;
    }

    public void setApiCityDAO(ApiCityDAO apiCityDAO) {
        this.apiCityDAO = apiCityDAO;
    }

    @Override
    public BaseWrapper saveOrUpdateDebitCardIssuenceRequest(BaseWrapper baseWrapper) throws FrameworkCheckedException {
//        Calendar date = Calendar.getInstance();
//        Date startDate = null;
//        date.setTime(new Date());
//        date.add(Calendar.YEAR, 1);
//        startDate = date.getTime();
//
//        DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
//        String dateStr = format.format(startDate);

        if (baseWrapper.getObject(CommandFieldConstants.KEY_TRANSACTION_TYPE).equals("02")) {
            baseWrapper = this.convertModelToVO(baseWrapper);
            DebitCardVO debitCardVo = (DebitCardVO) baseWrapper.getObject("debitCardVo");
            DebitCardModel debitCardModel = getDebitCardModelDao().getDebitCardModelByCardNumber(debitCardVo.getCardNo());

            DebitCardMailingAddressModel debitCardMailingAddressModel = (DebitCardMailingAddressModel)
                    baseWrapper.getObject(CommandFieldConstants.KEY_DEBIT_CARD_MAILING_ADDRESS_MODEL);

//            DebitCardMailingAddressModel debitCardMailingAddressModel = new DebitCardMailingAddressModel();
//            debitCardMailingAddressModel.setMailingAddressId(debitCardModel.getMailingAddressId());
//            debitCardMailingAddressDAO.findByExample(debitCardMailingAddressModel);

            debitCardMailingAddressModel.setMailingAddress(debitCardVo.getMailingAddress());
            debitCardMailingAddressModel = debitCardMailingAddressDAO.saveOrUpdate(debitCardMailingAddressModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_DEBIT_CARD_MAILING_ADDRESS_MODEL, debitCardMailingAddressModel);

            AppUserModel appUserModel = (AppUserModel)
                    baseWrapper.getObject(CommandFieldConstants.KEY_APP_USER_MODEL);

            debitCardModel.setMailingAddressId(debitCardMailingAddressModel.getMailingAddressId());
//            debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_IN_PROCESS);
            baseWrapper.setBasePersistableModel(debitCardModel);
            debitCardModel.setReissuance("1");
//            if (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.CLSPENDING)){
//                debitCardModel.setReIssuanceStatus(CardConstantsInterface.CARD_STATUS_PENDING);
//            }
//            else{
            debitCardModel.setReIssuanceStatus(CardConstantsInterface.CARD_STATUS_INTITATED);
//            }
            debitCardModel.setReissuanceRequestDate(new Date());
            debitCardModel.setDebitCardEmbosingName(debitCardVo.getDebitCardEmbosingName());
            debitCardModel.setTransactionCode(debitCardVo.getTransactionCode());
            debitCardModel.setFee(debitCardVo.getFee());
            debitCardModel.setUpdatedOn(new Date());
//            debitCardModel.setAnnualFeeDate(startDate);
//            debitCardModel.setReIssuanceDate(new Date());

//            if(baseWrapper.getObject("cardFeeRuleModel") != null){
//                if(String.valueOf(((CardFeeRuleModel) baseWrapper.getObject("cardFeeRuleModel")).getInstallmentPlan()).equals("QUARTERLY")){
//                    date.setTime(new Date());
//                    date.add(Calendar.MONTH, 3);
//                    startDate = date.getTime();
////                    format = new SimpleDateFormat("dd/MMM/yyyy");
////                    String dateStr1 = null;
////                    dateStr1 = format.format(startDate);
//
//                    debitCardModel.setNewInstallmentDateForReIssuance(startDate);
//                }
//                else if(String.valueOf(((CardFeeRuleModel) baseWrapper.getObject("cardFeeRuleModel")).getInstallmentPlan()).equals("BI-ANNUAL")){
//                    date.setTime(new Date());
//                    date.add(Calendar.MONTH, 6);
//                    startDate = date.getTime();
////                    format = new SimpleDateFormat("dd/MMM/yyyy");
////                    String dateStr1 = null;
////                    dateStr1 = format.format(dateStr1);
//
//                    debitCardModel.setNewInstallmentDateForReIssuance(startDate);
//                }
//                else{
//                    date.setTime(new Date());
//                    date.add(Calendar.MONTH, 12);
//                    startDate = date.getTime();
////                    format = new SimpleDateFormat("dd/MMM/yyyy");
////                    String dateStr1 = null;
////
////                    dateStr1 = format.format(dateStr1);
//
//                    debitCardModel.setNewInstallmentDateForReIssuance(startDate);
//                }
//
//                debitCardModel.setIsInstallments(Boolean.valueOf(String.valueOf(baseWrapper.getObject("isInstallments"))));
//                debitCardModel.setNoOfInstallments(Long.valueOf((String.valueOf(baseWrapper.getObject("noOfInstallments")))));
//            if (baseWrapper.getObject("cardFeeRuleModel") != null) {
//                if (String.valueOf(((CardFeeRuleModel) baseWrapper.getObject("cardFeeRuleModel")).getInstallmentPlan()).equals("QUARTERLY")) {
//                    date.setTime(new Date());
//                    date.add(Calendar.MONTH, 3);
//                    startDate = date.getTime();
//                    format = new SimpleDateFormat("dd/MMM/yyyy");
//                    String dateStr1 = null;
//                    dateStr1 = format.format(startDate);

//                    debitCardModel.setNewInstallmentDateForReIssuance(startDate);
//                } else if (String.valueOf(((CardFeeRuleModel) baseWrapper.getObject("cardFeeRuleModel")).getInstallmentPlan()).equals("BI-ANNUAL")) {
//                    date.setTime(new Date());
//                    date.add(Calendar.MONTH, 6);
//                    startDate = date.getTime();
//                    format = new SimpleDateFormat("dd/MMM/yyyy");
//                    String dateStr1 = null;
//                    dateStr1 = format.format(dateStr1);

//                    debitCardModel.setNewInstallmentDateForReIssuance(startDate);
//                } else {
//                    date.setTime(new Date());
//                    date.add(Calendar.MONTH, 12);
//                    startDate = date.getTime();
//                    format = new SimpleDateFormat("dd/MMM/yyyy");
//                    String dateStr1 = null;
//
//                debitCardModel.setRemainingNoOfInstallments(Long.parseLong((String.valueOf(baseWrapper.getObject("noOfInstallments")))) - 1);
//                debitCardModel.setLastInstallmentDateForReIssuance(new Date());
//            }

            debitCardModel = debitCardModelDAO.saveOrUpdate(debitCardModel);
            baseWrapper.setBasePersistableModel(debitCardModel);
            String msgToText = MessageUtil.getMessage("debit.card.reissuance.req.successful");
            BaseWrapper msgBaseWrapper = new BaseWrapperImpl();
            msgBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(debitCardModel.getMobileNo(), msgToText));
            try {
                this.sendSMSToUser(msgBaseWrapper);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
            }
        } else {
            baseWrapper = this.convertModelToVO(baseWrapper);
//            DebitCardVO debitCardVo = (DebitCardVO) baseWrapper.getObject("debitCardVo");
            DebitCardModel debitCardModel = (DebitCardModel) baseWrapper.getBasePersistableModel();
            DebitCardMailingAddressModel debitCardMailingAddressModel = (DebitCardMailingAddressModel)
                    baseWrapper.getObject(CommandFieldConstants.KEY_DEBIT_CARD_MAILING_ADDRESS_MODEL);
            debitCardMailingAddressModel = debitCardMailingAddressDAO.saveOrUpdate(debitCardMailingAddressModel);
            baseWrapper.putObject(CommandFieldConstants.KEY_DEBIT_CARD_MAILING_ADDRESS_MODEL, debitCardMailingAddressModel);
            AppUserModel appUserModel = (AppUserModel)
                    baseWrapper.getObject(CommandFieldConstants.KEY_APP_USER_MODEL);
            debitCardModel.setMailingAddressId(debitCardMailingAddressModel.getMailingAddressId());
//            if (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.CLSPENDING)) {
//                debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_PENDING);
//            } else {
            debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_INTITATED);
//            debitCardModel.setCardStateId(CardConstantsInterface.CARD_STATE_WARM);
//            debitCardModel.setIssuanceDate(new Date());
//            }
            debitCardModel.setTransactionCode(debitCardModel.getTransactionCode());
            debitCardModel.setFee(debitCardModel.getFee());
//            debitCardModel.setAnnualFeeDate(startDate);
//            if(baseWrapper.getObject("productId").equals(ProductConstantsInterface.DEBIT_CARD_ISSUANCE)){
//                debitCardModel.setIssuanceByAgent("1");
//            }
            if (baseWrapper.getObject("productId").equals(ProductConstantsInterface.DEBIT_CARD_ISSUANCE)) {
                debitCardModel.setIssuanceByAgent("1");
            }

//            if (baseWrapper.getObject("cardFeeRuleModel") != null) {
//                if (String.valueOf(((CardFeeRuleModel) baseWrapper.getObject("cardFeeRuleModel")).getInstallmentPlan()).equals("QUARTERLY")) {
//                    date.setTime(new Date());
//                    date.add(Calendar.MONTH, 3);
//                    startDate = date.getTime();
//                    format = new SimpleDateFormat("dd/MMM/yyyy");
//                    String dateStr1 = null;

//                    dateStr1 = format.format(startDate);

//                    debitCardModel.setNewInstallmentDateForIssuance(startDate);
//                } else if (String.valueOf(((CardFeeRuleModel) baseWrapper.getObject("cardFeeRuleModel")).getInstallmentPlan()).equals("BI-ANNUAL")) {
//                    date.setTime(new Date());
//                    date.add(Calendar.MONTH, 6);
//                    startDate = date.getTime();
//                    format = new SimpleDateFormat("dd/MMM/yyyy");
//                    String dateStr1 = null;
//                    dateStr1 = format.format(String.valueOf(startDate));

//                    debitCardModel.setNewInstallmentDateForIssuance(startDate);
//                } else {
//                    date.setTime(new Date());
//                    date.add(Calendar.MONTH, 12);
//                    startDate = date.getTime();
//                    format = new SimpleDateFormat("dd/MMM/yyyy");
//                    String dateStr1 = null;
//                    dateStr1 = format.format(startDate);

//            if(baseWrapper.getObject("cardFeeRuleModel") != null){
//                if(String.valueOf(((CardFeeRuleModel) baseWrapper.getObject("cardFeeRuleModel")).getInstallmentPlan()).equals("QUARTERLY")){
//                    date.setTime(new Date());
//                    date.add(Calendar.MONTH, 3);
//                    startDate = date.getTime();
//                    format = new SimpleDateFormat("dd/MMM/yyyy");
//                    String dateStr1 = null;
//
////                    dateStr1 = format.format(startDate);
//
//                    debitCardModel.setNewInstallmentDateForIssuance(startDate);
//                }
//                else if(String.valueOf(((CardFeeRuleModel) baseWrapper.getObject("cardFeeRuleModel")).getInstallmentPlan()).equals("BI-ANNUAL")){
//                    date.setTime(new Date());
//                    date.add(Calendar.MONTH, 6);
//                    startDate = date.getTime();
//                    format = new SimpleDateFormat("dd/MMM/yyyy");
//                    String dateStr1 = null;
////                    dateStr1 = format.format(String.valueOf(startDate));
//
//                    debitCardModel.setNewInstallmentDateForIssuance(startDate);
//                }
//                else{
//                    date.setTime(new Date());
//                    date.add(Calendar.MONTH, 12);
//                    startDate = date.getTime();
//                    format = new SimpleDateFormat("dd/MMM/yyyy");
//                    String dateStr1 = null;
////                    dateStr1 = format.format(startDate);
//
//                    debitCardModel.setNewInstallmentDateForIssuance(startDate);
//                }
//
//                debitCardModel.setIsInstallments(Boolean.valueOf(String.valueOf(baseWrapper.getObject("isInstallments"))));
//                debitCardModel.setNoOfInstallments(Long.valueOf((String.valueOf(baseWrapper.getObject("noOfInstallments")))));
//
//                debitCardModel.setRemainingNoOfInstallments(Long.parseLong((String.valueOf(baseWrapper.getObject("noOfInstallments")))) - 1);
//                debitCardModel.setLastInstallmentDateForIssuance(new Date());
//            }

            debitCardModel = debitCardModelDAO.saveOrUpdate(debitCardModel);
            baseWrapper.setBasePersistableModel(debitCardModel);

            String msgToText = MessageUtil.getMessage("debit.card.req.submitted");
            BaseWrapper msgBaseWrapper = new BaseWrapperImpl();
            msgBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(debitCardModel.getMobileNo(), msgToText));
            try {
                this.sendSMSToUser(msgBaseWrapper);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
            }
        }
        return baseWrapper;
    }

    @Override
    public BaseWrapper initiateBISPReversalRequestWithAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        TransactionReversalVo reversalVo = (TransactionReversalVo) baseWrapper.getBasePersistableModel();
        BISPCustNadraVerificationModel custNadraVerificationModel = new BISPCustNadraVerificationModel();
        custNadraVerificationModel.setTransactionCode(reversalVo.getTransactionCode());
        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        List<BISPCustNadraVerificationModel> list = getBispCustNadraVerificationDAO().findByExample(custNadraVerificationModel, null, null,
                exampleConfigHolderModel).getResultsetList();
        if (list != null && !list.isEmpty()) {
            custNadraVerificationModel = list.get(0);
        }
        if (custNadraVerificationModel != null && custNadraVerificationModel.getPk() != null) {
            if (custNadraVerificationModel.getRetryCount() == null)
                custNadraVerificationModel.setRetryCount(0L);
            else {
                Long retryCount = custNadraVerificationModel.getRetryCount() + 1;
                custNadraVerificationModel.setRetryCount(retryCount);
            }
            try {
                custNadraVerificationModel.setBusinessDate(PortalDateUtils.formatDate(custNadraVerificationModel.getBusinessDate().split(" ")[0], "yyyyy-MM-dd",
                        "dd-MMM-yyyy"));
            } catch (ParseException e) {
                logger.error("Error while Inserting Business Date in BISP NADRA Verification");
                e.printStackTrace();
            }
            getBispCustNadraVerificationDAO().saveOrUpdate(custNadraVerificationModel);
            WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();
            TransactionCodeModel transactionCodeModel = new TransactionCodeModel(reversalVo.getTransactionCode());
            wrapper.setTransactionCodeModel(transactionCodeModel);
            wrapper.putObject("IS_MANUAL_REVERSAL", "1");
            wrapper.putObject("BISP_MODEL", custNadraVerificationModel);
            thirdPartyCashOutQueingPreProcessor.startProcessing(wrapper);//.loadAndForwardAdviceToQueue(reversalVo.getTransactionCode());
            logger.info("Done");
        }
        return null;
    }

    @Override
    public MnoUserDAO getMnoUserDAO() {
        return mnoUserDAO;
    }

    @Override
    public UsecaseDAO getUseCaseDAO() {
        return usecaseDAO;
    }

    @Override
    public PayMtncRequestDAO getPayMtncRequestDao() {
        return null;
    }

    @Override
    public RemitanceInfoDAO getRemitanceInfoDao() {
        return remitanceInfoDAO;
    }

    @Override
    public CardConfigurationManager getCardConfigurationManager() {
        return cardConfigurationManager;
    }

    public void setCardConfigurationManager(CardConfigurationManager cardConfigurationManager) {
        this.cardConfigurationManager = cardConfigurationManager;
    }

    @Override
    public CityDAO getCityDAO() {
        return cityDAO;
    }

    public void setCityDAO(CityDAO cityDAO) {
        this.cityDAO = cityDAO;
    }

    @Override
    public CustomerAddressesDAO getCustomerAddressesDao() {
        return customerAddressesDAO;
    }

    @Override
    public ESBAdapter getEsbAdapter() {
        return esbAdapter;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    @Override
    public TransactionPurposeDAO getTransactionPurposeDao() {
        return transactionPurposeDAO;
    }

    @Override
    public GeoLocationDAO getGeoLocationDao() {
        return geoLocationDAO;
    }

    @Override
    public BISPCustNadraVerificationDAO getBispCustNadraVerificationDAO() {
        return bispCustNadraVerificationDAO;
    }

    public void setBispCustNadraVerificationDAO(BISPCustNadraVerificationDAO bispCustNadraVerificationDAO) {
        this.bispCustNadraVerificationDAO = bispCustNadraVerificationDAO;
    }

    @Override
    public RetailerContactDAO getRetailerContactDao() {
        return retailerContactDAO;
    }

    @Override
    public String saveOrUpdateBVSEntryRequiresNewTransaction(UserDeviceAccountsModel userDeviceAccountsModel, AppUserModel appUserModel, String cNic, SwitchWrapper sWrapper, String transactionCode) throws FrameworkCheckedException {
        return bispCustNadraVerificationDAO.saveOrUpdateBVSEntryRequiresNewTransaction(userDeviceAccountsModel, appUserModel, cNic, sWrapper, transactionCode);
    }

    @Override
    public FetchThirdPartySegmentsDAO getFetchThirdPartySegmentsDao() {
        return fetchThirdPartySegmentsDAO;
    }

    @Override
    public FetchCardTypeDAO getCardTypeDao() {
        return fetchCardTypeDAO;
    }

    @Override
    public SegmentDAO getSegmentDao() {
        return segmentDAO;
    }

    @Override
    public ThirdPartyAcOpeningDAO getThirdPartyAcOpeningDao() {
        return thirdPartyAcOpeningDAO;
    }

    @Override
    public MemberBankDAO getMemberBankDao() {
        return memberBankDAO;
    }

    @Override
    public BankSegmentsDAO getBankSegmentsDao() {
        return bankSegmentsDAO;
    }

    @Override
    public ActionLogDAO getActionLogDao() {
        return actionLogDAO;
    }

    @Override
    public RetailerDAO getRetailerDao() {
        return retailerDAO;
    }

    @Override
    public WalletSafRepoDAO getWalletSafRepoDAO() {
        return walletSafRepoDAO;
    }

    public void setWalletSafRepoDAO(WalletSafRepoDAO walletSafRepoDAO) {
        this.walletSafRepoDAO = walletSafRepoDAO;
    }

    @Override
    public DebitCardPendingSafRepo debitCardPendingSafRepo(DebitCardPendingSafRepo debitCardPendingSafRepo) {
        return this.genericDao.createEntity(debitCardPendingSafRepo);
    }

    @Override
    public ClsPendingAccountOpeningModel clsPendingAccountOpening(ClsPendingAccountOpeningModel clsPendingAccountOpeningModel) {
        return this.genericDao.createEntity(clsPendingAccountOpeningModel);
    }

    @Override
    public AdvanceSalaryLoanModel saveOrUpdateAdvanceSalaryLoan(AdvanceSalaryLoanModel adavceSalaryLoanModel) {
        return this.genericDao.createEntity(adavceSalaryLoanModel);
    }

//    @Override
//    public JSLoansModel saveOrUpdateJSLoansModel(JSLoansModel jsLoansModel) {
//        return null;
//    }

    @Override
    public BlinkCustomerModel createBlinkCustomerModel(BlinkCustomerModel blinkCustomerModel) {
        return this.genericDao.createEntity(blinkCustomerModel);
    }

    @Override
    public MerchantAccountModel createMerchantAccountModel(MerchantAccountModel merchantAccountModel) {
        return this.genericDao.createEntity(merchantAccountModel);
    }

    @Override
    public ClsPendingBlinkCustomerModel createClsPendingBlinkCustomerModel(ClsPendingBlinkCustomerModel blinkCustomerModel) {
        return this.genericDao.createEntity(blinkCustomerModel);
    }


//	@Override
//	public List<ScheduleLoanPaymentModel> loadAllDebitBlockRequired() throws FrameworkCheckedException {
////		return scheduleBillPaymentDao.loadAllDebitBlockRequired();
//		return null;
//	}

    @Override
    public AccountOpeningPendingSafRepoModel getAccountOpeningPendingSafRepoModel(AccountOpeningPendingSafRepoModel accountOpeningPendingSafRepoModel) {
        try {
            accountOpeningPendingSafRepoModel = pendingAccountOpeningDAO.loadExistingPendingAccountOpeningSafRepo(accountOpeningPendingSafRepoModel);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        return accountOpeningPendingSafRepoModel;
    }

    @Override
    public DebitCardPendingSafRepo loadExistingDebitCardSafRepo(DebitCardPendingSafRepo debitCardPendingSafRepo) throws FrameworkCheckedException {
        debitCardPendingSafRepo = pendingDebitCardSafRepoDAO.loadDebitCardSafRepoByMobileNoAndCnic(debitCardPendingSafRepo);
        return debitCardPendingSafRepo;
    }

    @Override
    public DebitCardPendingSafRepo loadDebitCardSafRepo(DebitCardPendingSafRepo debitCardPendingSafRepo) throws FrameworkCheckedException {
        debitCardPendingSafRepo = pendingDebitCardSafRepoDAO.loadDebitCardSafRepo(debitCardPendingSafRepo);
        return debitCardPendingSafRepo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void debitCardReissuance(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        BasePersistableModel bpm;
        DebitCardVO debitCardModel = null;
        DebitCardModel debitCardModel1 = null;
        SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();

        String VOModelString = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING);
        String modelClassName = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME);

        try {
            bpm = (BasePersistableModel) mapper.readValue(VOModelString, Class.forName(modelClassName));
            baseWrapper.setBasePersistableModel(bpm);
            debitCardModel = (DebitCardVO) baseWrapper.getBasePersistableModel();
            List<DebitCardModel> list = null;
            try {
                list = this.getDebitCardModelDao().getDebitCardModelByMobileAndNIC(debitCardModel.getMobileNo(), debitCardModel.getcNic());
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
            }
            I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForDebitCardReissuance(I8SBConstants.RequestType_JSDEBITCARD_CardReissuance);
            requestVO.setCardNumber(debitCardModel.getCardNo());
            requestVO.setRelationshipNumber(debitCardModel.getcNic());
            requestVO.setMobileNumber(debitCardModel.getMobileNo());
            requestVO.setCardEmborsingName(debitCardModel.getDebitCardEmbosingName());
            requestVO.setCardBranchCode(MessageUtil.getMessage("debit.card.branch.code"));
            requestVO.setIssuedDate(String.valueOf(debitCardModel.getCardIssuanceDate()));
            if (list != null && !list.isEmpty()) {
                debitCardModel1 = list.get(0);
                requestVO.setExpiryDate(String.valueOf(debitCardModel1.getExpiryDate()));

                long smaId = list.get(0).getSmartMoneyAccountId();

                SmartMoneyAccountModel sma = smartMoneyAccountDAO.findByPrimaryKey(smaId);

//                requestVO.setCardProdCode(String.valueOf(sma.getCardProdId()));

                CardProdCodeModel cardProdCodeModel = fetchCardTypeDAO.findByPrimaryKey(sma.getCardProdId());

                requestVO.setCardTypeCode(cardProdCodeModel.getCardTypeCode());
                requestVO.setCardProdCode(cardProdCodeModel.getCardProductCode());
                requestVO.setRequestId(CardConstantsInterface.DEBIT_CARD_ONLINE_RQST_TYPE_CODE);

            }

            i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
            requestVO = i8sbSwitchWrapper.getI8SBSwitchControllerRequestVO();
            I8SBSwitchControllerResponseVO responseVO = requestVO.getI8SBSwitchControllerResponseVO();
            i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
            responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();
            ESBAdapter.processI8sbResponseCode(responseVO, false);
            if (responseVO.getResponseCode().equals(WorkFlowErrorCodeConstants.I8SB_SUCCESS_CODE)) {
                actionAuthorizationManager.performAuthorization(baseWrapper);
                debitCardModel1.setReIssuanceStatus(CardConstantsInterface.CARD_STATUS_IN_PROCESS);
                debitCardModelDAO.saveOrUpdate(debitCardModel1);
            }

        } catch (IllegalArgumentException
                | SecurityException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(e.getCause().getMessage());
        }


    }

    @Override
    public SwitchWrapper validateBalance(AppUserModel appUserModel, SmartMoneyAccountModel smaModel, Double transactionAmount, boolean checkDebitBlock) throws Exception {
        long start = System.currentTimeMillis();

        SwitchWrapper switchWrapper = validateBalance(appUserModel, smaModel, transactionAmount);


        if (checkDebitBlock) {
            smartMoneyAccountManager.validateDebitBlock(smaModel, transactionAmount, switchWrapper.getBalance());
        }

        ThreadLocalProcessingTime.append(start, "CCM", checkDebitBlock ? "Validate Balance & Debit block" : "Validate Balance");
        return switchWrapper;
    }

    @Override
    public SwitchWrapper validateBalance(AppUserModel appUserModel, SmartMoneyAccountModel smaModel, Double transactionAmount) throws WorkFlowException, FrameworkCheckedException, Exception {
        if (appUserModel == null) {
            throw new CommandException("User not found", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }

        if (smaModel == null) {
            smaModel = loadSmartMoneyAccountModel(appUserModel);
        }

        SwitchWrapper switchWrapper = checkBalance(appUserModel, smaModel);
        Double balance = switchWrapper.getBalance();
        if (smaModel.getPaymentModeId().longValue() == PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT) {

            balance = Double.parseDouble(switchWrapper.getMiddlewareIntegrationMessageVO().getAccountBalance());
        }
        boolean isNegativeBalanceAllowed = switchWrapper.getOlavo() == null ? false : switchWrapper.getOlavo().isNegativeBalanceAllowed();

//        if (balance == null || (balance < Double.valueOf(transactionAmount) && !isNegativeBalanceAllowed)) {
//            logger.error("[CommonCommandManagerImpl.validateBalance] Your balance in insufficient to make this transaction.");
////            if(appUserModel.getCustomerIdCustomerModel() != null && appUserModel.getCustomerIdCustomerModel().getCustomerAccountTypeId() != null)
////                throw new CommandException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.INSUFFICIENT_BALANCE), 51L,ErrorLevel.MEDIUM,new Throwable());
////            else
//            throw new CommandException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.INSUFFICIENT_BALANCE), Long.valueOf(WorkFlowErrorCodeConstants.INSUFFICIENT_BALANCE), ErrorLevel.MEDIUM, new Throwable());
//        }

        return switchWrapper;
    }

    @Override
    public SmartMoneyAccountModel loadSmartMoneyAccountModel(AppUserModel appUserModel) throws FrameworkCheckedException {
        return smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel);
    }

    @Override
    public SwitchWrapper checkBalance(AppUserModel appUserModel, SmartMoneyAccountModel smaModel) throws FrameworkCheckedException {
        Long customerId = appUserModel.getAppUserId();
        if (UserTypeConstantsInterface.CUSTOMER.longValue() == appUserModel.getAppUserTypeId()) {
            customerId = appUserModel.getCustomerId();
        }

        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setCustomerId(customerId);
        accountInfoModel.setAccountNick(smaModel.getName());
        accountInfoModel.setPaymentModeId(smaModel.getPaymentModeId());

        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(smaModel);
        BaseWrapper bWrapper = new BaseWrapperImpl();
        bWrapper.setBasePersistableModel(smaModel);
        veriflyBaseWrapper.setBasePersistableModel(null);
        VeriflyManager veriflyManager = this.loadVeriflyManagerByAccountId(bWrapper);
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

        try {
            veriflyBaseWrapper = veriflyManager.verifyCredentials(veriflyBaseWrapper);
            boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

            if (errorMessagesFlag) {
                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();

                switchWrapper.setAccountInfoModel(accountInfoModel);
                switchWrapper.setWorkFlowWrapper(workFlowWrapper);
                switchWrapper.setBankId(smaModel.getBankId());
                switchWrapper.setPaymentModeId(smaModel.getPaymentModeId());
                switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
                //switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
                switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());

                if (switchWrapper.getWorkFlowWrapper() != null) {
                    switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
                }

                switchWrapper = this.switchController.checkBalance(switchWrapper);
            }
        } catch (CommandException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CommandException("Your account cannot be contacted. Please try again later.\n", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
        }

        return switchWrapper;
    }

    @Override
    public OlaCustomerAccountTypeDao getOlaCustomerAccountTypeDao() {
        return olaCustomerAccountTypeDao;
    }

    public void setOlaCustomerAccountTypeDao(OlaCustomerAccountTypeDao olaCustomerAccountTypeDao) {
        this.olaCustomerAccountTypeDao = olaCustomerAccountTypeDao;
    }

    private BaseWrapper convertModelToVO(BaseWrapper baseWrapper) {
        DebitCardVO debitCardVO = (DebitCardVO) baseWrapper.getBasePersistableModel();
        DebitCardModel debitCardModel = new DebitCardModel();
        debitCardModel.setCardNo(debitCardVO.getCardNo());
        debitCardModel.setMobileNo(debitCardVO.getMobileNo());
        debitCardModel.setCnic(debitCardVO.getcNic());
        debitCardModel.setCardStatusId(CardConstantsInterface.CARD_STATUS_PENDING);
        debitCardModel.setDebitCardEmbosingName(debitCardVO.getDebitCardEmbosingName());
        debitCardModel.setCreatedBy(debitCardVO.getCreatedByAppUserId());
        debitCardModel.setCreatedOn(debitCardVO.getCreatedOn());
        debitCardModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
        debitCardModel.setUpdatedOn(new Date());
        debitCardModel.setTransactionCode(debitCardVO.getTransactionCode());
        debitCardModel.setFee(debitCardVO.getFee());
        debitCardModel.setSmartMoneyAccountId(debitCardVO.getSmartMoneyAccountId());
        if (debitCardVO.getAppId() != null && !debitCardVO.getAppId().equals("")) {
            if (debitCardVO.getAppId().equals(AppConstants.WEB_SERVICE.toString())) {
                debitCardModel.setAppId(Long.parseLong(debitCardVO.getAppId()));
            } else {
                debitCardModel.setAppId(Long.parseLong(debitCardVO.getAppId()));
            }
        }
        debitCardModel.setAppUserId(debitCardVO.getCustomerAppUserId());
        baseWrapper.setBasePersistableModel(debitCardModel);
        //
        DebitCardMailingAddressModel addressModel = new DebitCardMailingAddressModel();
        addressModel.setMailingAddress(debitCardVO.getMailingAddress());
        addressModel.setCreatedBy(debitCardVO.getCreatedByAppUserId());
        addressModel.setCreatedOn(debitCardVO.getCreatedOn());
        addressModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
        addressModel.setUpdatedOn(new Date());
        baseWrapper.putObject(CommandFieldConstants.KEY_DEBIT_CARD_MAILING_ADDRESS_MODEL, addressModel);
        return baseWrapper;
    }

    //	public List<ScheduleFundsTransferDetailModel>  fetchScheduleFundsTransferDetailList(Date date) throws FrameworkCheckedException
//	{
//		if(logger.isDebugEnabled())
//		{
//			logger.debug("Start of CommonCommandManagerImpl.fetchScheduleFundsTransferList()");
//		}
///*
//        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
//		exampleConfigHolderModel.setEnableLike(false);
//		exampleConfigHolderModel.setIgnoreCase(true);
//		exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);*/
//		SearchBaseWrapper searchBaseWrapper= new SearchBaseWrapperImpl();
//		List<ScheduleFundsTransferDetailModel> scheduleFundsTransferCustomList=
//				this.scheduleFundsTransferDetailDao.getActiveFundTransferSchedule(date);
//		//searchBaseWrapper.setCustomList(scheduleFundsTransferCustomList);
//		if(logger.isDebugEnabled())
//		{
//			logger.debug("End of CommonCommandManagerImpl.fetchScheduleFundsTransferList()");
//		}
//		return scheduleFundsTransferCustomList;
//	}
    @Override
    public void sendM3SMS(String mobileNo, String SMSText) throws FrameworkCheckedException, Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.sendSMS()");
        }

        MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
        middlewareMessageVO.setMobileNo(mobileNo);
        middlewareMessageVO.setText(SMSText);
        middlewareMessageVO.setBehaviorFlag("No");
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setBankId(Long.valueOf(MessageUtil.getMessage("falconlitebankid", null)));
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.INTERNET_BANKING_ACCOUNT);
        switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);
        try {
            abstractFinancialInstitution = loadAbstractFinancialInstitution(switchWrapper);
            abstractFinancialInstitution.sendM3SMS(switchWrapper);
        } catch (WorkFlowException ex) {
            ex.printStackTrace();
            /*if (ex instanceof WorkFlowException)
            {
                Long financialIntegrationId = switchWrapper.getFinancialIntegrationModel().getFinancialIntegrationId();
                throw new FrameworkCheckedException( this.workflowExceptionTranslator.translateWorkFlowException(
                        ex,this.workflowExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION, financialIntegrationId ).getMessage());
            }*/
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.sendSMS()");
        }
    }

    @Override
    public void sendM3SMS(String mobileNo, String SMSText, String pin) throws FrameworkCheckedException, Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CommonCommandManagerImpl.sendSMS()");
        }

        MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
        middlewareMessageVO.setMobileNo(mobileNo);
        middlewareMessageVO.setText(SMSText);
        middlewareMessageVO.setBehaviorFlag("Yes");
        middlewareMessageVO.setMapCode(pin);
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setBankId(Long.valueOf(MessageUtil.getMessage("falconlitebankid", null)));
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.INTERNET_BANKING_ACCOUNT);
        switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

        try {
            abstractFinancialInstitution = loadAbstractFinancialInstitution(switchWrapper);
            abstractFinancialInstitution.sendM3SMS(switchWrapper);
        } catch (WorkFlowException ex) {
            ex.printStackTrace();
            /*if (ex instanceof WorkFlowException)
            {
                Long financialIntegrationId = switchWrapper.getFinancialIntegrationModel().getFinancialIntegrationId();
                throw new FrameworkCheckedException( this.workflowExceptionTranslator.translateWorkFlowException(
                        ex,this.workflowExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION, financialIntegrationId ).getMessage());
            }*/
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CommonCommandManagerImpl.sendSMS()");
        }

    }

    @Override
    public AppUserModel loadAppUserModelByCustomerId(Long customerId) {
        return appUserManager.loadAppUserModelByCustomerId(customerId);
    }

    @Override
    public UserDeviceAccountsManager getUserDeviceAccountsManager() {
        return userDeviceAccountsManager;
    }

    public void setDebitCardModelDAO(DebitCardModelDAO debitCardModelDAO) {
        this.debitCardModelDAO = debitCardModelDAO;
    }

    public void setUsecaseDAO(UsecaseDAO usecaseDAO) {
        this.usecaseDAO = usecaseDAO;
    }

    public void setPayMtncRequestDAO(PayMtncRequestDAO payMtncRequestDAO) {
        this.payMtncRequestDAO = payMtncRequestDAO;
    }

    public void setRemitanceInfoDAO(RemitanceInfoDAO remitanceInfoDAO) {
        this.remitanceInfoDAO = remitanceInfoDAO;
    }

    public void setTransactionPurposeDAO(TransactionPurposeDAO transactionPurposeDAO) {
        this.transactionPurposeDAO = transactionPurposeDAO;
    }

    public void setGeoLocationDAO(GeoLocationDAO geoLocationDAO) {
        this.geoLocationDAO = geoLocationDAO;
    }

    public void setThirdPartyCashOutQueingPreProcessor(ThirdPartyCashOutQueingPreProcessor thirdPartyCashOutQueingPreProcessor) {
        this.thirdPartyCashOutQueingPreProcessor = thirdPartyCashOutQueingPreProcessor;
    }

    public void setFetchThirdPartySegmentsDAO(FetchThirdPartySegmentsDAO fetchThirdPartySegmentsDAO) {
        this.fetchThirdPartySegmentsDAO = fetchThirdPartySegmentsDAO;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public void setThirdPartyAcOpeningDAO(ThirdPartyAcOpeningDAO thirdPartyAcOpeningDAO) {
        this.thirdPartyAcOpeningDAO = thirdPartyAcOpeningDAO;
    }

    public void setMemberBankDAO(MemberBankDAO memberBankDAO) {
        this.memberBankDAO = memberBankDAO;
    }

    public void setBankSegmentsDAO(BankSegmentsDAO bankSegmentsDAO) {
        this.bankSegmentsDAO = bankSegmentsDAO;
    }

    public void setActionLogDAO(ActionLogDAO actionLogDAO) {
        this.actionLogDAO = actionLogDAO;
    }

    public void setDebitCardChargesDAO(DebitCardChargesDAO debitCardChargesDAO) {
        this.debitCardChargesDAO = debitCardChargesDAO;
    }

    public void setFetchCardTypeDAO(FetchCardTypeDAO fetchCardTypeDAO) {
        this.fetchCardTypeDAO = fetchCardTypeDAO;
    }

    public void setSegmentDAO(SegmentDAO segmentDAO) {
        this.segmentDAO = segmentDAO;
    }

    public void setAccountInfoDAO(AccountInfoDAO accountInfoDAO) {
        this.accountInfoDAO = accountInfoDAO;
    }

    public DebitCardManager getDebitCardManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (DebitCardManager) applicationContext.getBean("debitCardManager");
    }

    public void setDebitCardManager(DebitCardManager debitCardManager) {
        this.debitCardManager = debitCardManager;
    }

    public void setPendingAccountOpeningDAO(PendingAccountOpeningDAO pendingAccountOpeningDAO) {
        this.pendingAccountOpeningDAO = pendingAccountOpeningDAO;
    }

    public void setPendingDebitCardSafRepoDAO(PendingDebitCardSafRepoDAO pendingDebitCardSafRepoDAO) {
        this.pendingDebitCardSafRepoDAO = pendingDebitCardSafRepoDAO;
    }

    public AdvanceSalaryLoanDAO getAdvanceSalaryLoanDAO() {
        return advanceSalaryLoanDAO;
    }

    @Override
    public SmartMoneyAccountDAO getSmartMoneyAccountDAO() {
        return smartMoneyAccountDAO;
    }

//    @Override
//    public JSLoansDAO getJSLoansDAO() {
//        return null;
//    }

    @Override
    public DebitCardRequestsViewModel getDebitCardRequestsViewModelByAppUserId(Long appUserId, String mobileNo) throws CommandException {
        DebitCardRequestsViewModel debitCardRequestsViewModel = debitCardRequestsViewModelDAO.loadDebitCardRequestsByAppUserId(appUserId, mobileNo);
        return debitCardRequestsViewModel;
    }

    @Override
    public DebitCardRequestsViewModel getDebitCardRequestsViewModelByDebitCardId(Long debitCardId) throws CommandException {
        DebitCardRequestsViewModel debitCardRequestsViewModel = debitCardRequestsViewModelDAO.loadDebitCardRequestsByDebitCardId(debitCardId);
        return debitCardRequestsViewModel;
    }

    @Override
    public BlinkCustomerPictureModel getBlinkCustomerPictureByTypeId(Long pictureTypeId, Long customerId) throws FrameworkCheckedException {
        BlinkCustomerPictureModel customerPictureModel = blinkCustomerPictureDAO.getBlinkCustomerPictureByTypeId(pictureTypeId, customerId);
        return customerPictureModel;
    }

    @Override
    public MerchantAccountPictureModel getMerchantAccountPictureByTypeId(Long pictureTypeId, Long customerId) throws FrameworkCheckedException {
        MerchantAccountPictureModel customerPictureModel = merchantAccountPictureDAO.getMerchantAccountPictureByTypeId(pictureTypeId, customerId);
        return customerPictureModel;


    }

    @Override
    public List<ClsDebitCreditBlockModel> loadClsDebitCreditModel() throws FrameworkCheckedException {
        return clsDebitCreditDAO.loadClsDebitCreditModel();
    }

    @Override
    public OfflineBillersConfigModel loadOfflineBillersModelByProductId(String productId) {
        return offlineBillersConfigDAO.loadOfflineBillersModelByProductId(productId);
    }

    @Override
    public TasdeeqDataModel saveOrUpdateTasdeeqDataModel(TasdeeqDataModel tasdeeqDataModel) {
        return this.genericDao.createEntity(tasdeeqDataModel);
    }

    @Override
    public TasdeeqDataModel loadTasdeeqDataModelByMobile(String mobileNo) throws FrameworkCheckedException {
        return tasdeeqDataDAO.loadTasdeeqDataByMobile(mobileNo);
    }

    @Override
    public String verifyDailyLimitForCredit(Date transactionDateTime, Double amountToAdd, Long accountId, Long customerAccountTypeId, Long handlerId) throws FrameworkCheckedException {
        logger.info("Start of verifyDailyLimitForCredit at Time :: " + new Date());
        String responseCode = "";
        try {
            LimitModel limitModel = new LimitModel();
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.BLINK)) {
                BlinkCustomerLimitModel blinkCustomerLimitModel = this.limitManager.getBlinkCustomerLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.DAILY, customerAccountTypeId, accountId);
                if (blinkCustomerLimitModel != null) {
                    limitModel.setMaximum(Double.valueOf(blinkCustomerLimitModel.getMaximum()));
                    if (blinkCustomerLimitModel.getIsApplicable() == 1) {
                        limitModel.setIsApplicable(true);
                    }
                    limitModel.setCustomerAccountTypeId(blinkCustomerLimitModel.getCustomerAccTypeId());
                }
            } else {
                limitModel = this.limitManager.getLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.DAILY, customerAccountTypeId);
            }
            if (limitModel != null) {

                if (limitModel.getIsApplicable() && limitModel.getMaximum() != null) {
                    Double consumedBalance = ledgerDAO.getDailyConsumedBalance(accountId, TransactionTypeConstants.CREDIT, transactionDateTime, handlerId);
                    if (consumedBalance != null) {
                        if (consumedBalance + amountToAdd > limitModel.getMaximum()) {
                            responseCode = "09"; // Your entered amount will exceed the customer's Maximum transaction Credit Limit per Day, please try again.
                            logger.error("Your entered amount will exceed the customer's Maximum transaction Credit Limit per Day, please try again.");
                        } else {
                            responseCode = "00"; //Success Message
                        }
                    }
                } else {
                    responseCode = "00"; //Success Message when limit is not applicable
                }
            } else {
                responseCode = "08"; // No Limit is defined for this data (Daily Limit for Credit).
                logger.error("No Limit is defined for this data (Daily Limit for Credit).");
            }
        } catch (Exception e) {
            logger.error("Error in AccountManagerImpl.verifyDailyLimitForCredit() :: " + e.getMessage() + " :: Exception " + e);
            responseCode = "25";
        }
        logger.info("End of verifyDailyLimitForCredit at Time :: " + new Date());
        return responseCode;
    }

    @Override
    public String verifyMonthlyLimitForCredit(Date transactionDateTime, Double amountToAdd, Long accountId, Long customerAccountTypeId, Long handlerId) throws FrameworkCheckedException {
        String responseCode = "";
        try {
            LimitModel limitModel = new LimitModel();
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.BLINK)) {
                BlinkCustomerLimitModel blinkCustomerLimitModel = this.limitManager.getBlinkCustomerLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.MONTHLY, customerAccountTypeId, accountId);
                if (blinkCustomerLimitModel != null) {
                    limitModel.setMaximum(Double.valueOf(blinkCustomerLimitModel.getMaximum()));
                    if (blinkCustomerLimitModel.getIsApplicable() == 1) {
                        limitModel.setIsApplicable(true);
                    }
                    limitModel.setCustomerAccountTypeId(blinkCustomerLimitModel.getCustomerAccTypeId());
                }
            } else {

                limitModel = this.limitManager.getLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.MONTHLY, customerAccountTypeId);
            }
            if (limitModel != null) {
                if (limitModel.getIsApplicable() && limitModel.getMaximum() != null) {
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(transactionDateTime);
                    startCalendar.set(Calendar.DAY_OF_MONTH, 1);
                    PortalDateUtils.resetTime(startCalendar);
                    Date startDate = startCalendar.getTime();
                    Double consumedBalance = ledgerDAO.getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.CREDIT, startDate, transactionDateTime, handlerId);
                    if (consumedBalance != null) {
                        if (consumedBalance + amountToAdd > limitModel.getMaximum()) {
                            responseCode = "11";// Your entered amount will exceed the customer's Maximum transaction Credit Limit per Month, please try again.
                            logger.error("Your entered amount will exceed the customer's Maximum transaction Credit Limit per Month, please try again.");
                        } else {
                            responseCode = "00";//Success Message
                        }
                    }
                } else {
                    responseCode = "00"; //Success Message when limit is not applicable
                }
            } else {
                responseCode = "10"; // No Limit is defined for this data (Monthly Limit for Credit).
                logger.error("No Limit is defined for this data (Monthly Limit for Credit.");
            }
        } catch (Exception ex) {
            logger.error("Error in AccountManagerImpl.verifyMonthlyLimitForCredit() :: " + ex.getMessage() + " :: Exception " + ex);
            responseCode = "26";
        }
        return responseCode;
    }

    public void setAdvanceSalaryLoanDAO(AdvanceSalaryLoanDAO advanceSalaryLoanDAO) {
        this.advanceSalaryLoanDAO = advanceSalaryLoanDAO;
    }

    public ActionAuthorizationManager getActionAuthorizationManager() {
        return actionAuthorizationManager;
    }

    public void setActionAuthorizationManager(ActionAuthorizationManager actionAuthorizationManager) {
        this.actionAuthorizationManager = actionAuthorizationManager;
    }

    public void setBillStatusDAO(BillStatusDAO billStatusDAO) {

        this.billStatusDAO = billStatusDAO;
    }

    public void setAgentBvsStatDAO(AgentBvsStatDAO agentBvsStatDAO) {
        this.agentBvsStatDAO = agentBvsStatDAO;
    }

    @Override
    public AgentLocationStatManager getAgentLocationStatManager() {
        return agentLocationStatManager;
    }

    public void setAgentLocationStatManager(AgentLocationStatManager agentLocationStatManager) {
        this.agentLocationStatManager = agentLocationStatManager;
    }

    public void setBlinkCustomerModelDAO(BlinkCustomerModelDAO blinkCustomerModelDAO) {
        this.blinkCustomerModelDAO = blinkCustomerModelDAO;
    }

    public void setBlinkCustomerPictureDAO(BlinkCustomerPictureDAO blinkCustomerPictureDAO) {
        this.blinkCustomerPictureDAO = blinkCustomerPictureDAO;
    }

    public void setMerchantAccountPictureDAO(MerchantAccountPictureDAO merchantAccountPictureDAO) {
        this.merchantAccountPictureDAO = merchantAccountPictureDAO;
    }

    public void setDebitCardRequestsViewModelDAO(DebitCardRequestsViewModelDAO debitCardRequestsViewModelDAO) {
        this.debitCardRequestsViewModelDAO = debitCardRequestsViewModelDAO;
    }

    public void setBlinkCustomerRegistrationStateDAO(BlinkCustomerRegistrationStateDAO blinkCustomerRegistrationStateDAO) {
        this.blinkCustomerRegistrationStateDAO = blinkCustomerRegistrationStateDAO;
    }

    public void setClsDebitCreditDAO(ClsDebitCreditDAO clsDebitCreditDAO) {
        this.clsDebitCreditDAO = clsDebitCreditDAO;
    }

    public void setOfflineBillersConfigDAO(OfflineBillersConfigDAO offlineBillersConfigDAO) {
        this.offlineBillersConfigDAO = offlineBillersConfigDAO;
    }

    public void setTasdeeqDataDAO(TasdeeqDataDAO tasdeeqDataDAO) {
        this.tasdeeqDataDAO = tasdeeqDataDAO;
    }

    public void setMerchantAccountModelDAO(MerchantAccountModelDAO merchantAccountModelDAO) {
        this.merchantAccountModelDAO = merchantAccountModelDAO;
    }

    public void setLimitManager(LimitManager limitManager) {
        this.limitManager = limitManager;
    }

    public void setLedgerDAO(LedgerDAO ledgerDAO) {
        this.ledgerDAO = ledgerDAO;
    }

//    public void setJsLoansDAO(JSLoansDAO jsLoansDAO) {
//        this.jsLoansDAO = jsLoansDAO;
//    }
}