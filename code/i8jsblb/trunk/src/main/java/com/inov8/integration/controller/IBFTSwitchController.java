package com.inov8.integration.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.ibft.controller.HostTransactionController;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.commissionmodule.CommissionTransactionViewModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.ola.BbStatementAllViewModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionReportModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.transactionreversal.ManualReversalVO;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.debitcard.util.DebitCardUtill;
import com.inov8.microbank.debitcard.vo.DebitCardReversalVO;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.fonepay.common.FonePayLogModel;
import com.inov8.microbank.fonepay.common.FonePayResponseCodes;
import com.inov8.microbank.fonepay.common.FonePayUtils;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.server.dao.messagingmodule.IBFTRetryAdviceDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.postedtransactionreportmodule.PostedTransactionReportDAO;
import com.inov8.microbank.server.dao.productmodule.hibernate.IBFTStatusHibernateDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionCodeDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionReversalDAO;
import com.inov8.microbank.server.facade.ReversalAdviceQueingPreProcessor;
import com.inov8.microbank.server.facade.portal.commissionmodule.CommissionTransactionViewFacade;
import com.inov8.microbank.server.facade.portal.ola.PortalOlaFacade;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeCommandManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualReversalManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.web.context.ContextLoader;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class IBFTSwitchController implements HostTransactionController {

    protected final Log logger = LogFactory.getLog(getClass());
    private MessageSource messageSource;
    private FonePaySwitchController fonePaySwitchController;
    private ManualReversalManager manualReversalFacade;
    private TransactionDetailMasterManager transactionDetailMasterManager;
    private CommissionTransactionViewFacade commissionTransactionViewFacade;
    private PortalOlaFacade portalOlaFacade;
    private ManualAdjustmentManager manualAdjustmentManager;
    private PostedTransactionReportDAO postedTransactionReportDAO;
    private TransactionCodeDAO transactionCodeDAO;
    private UserDeviceAccountsDAO userDeviceAccountsDAO;
    private TransactionReversalDAO transactionReversalDAO;
    private ReversalAdviceQueingPreProcessor reversalAdviceQueingPreProcessor;
    private IBFTRetryAdviceDAO ibftRetryAdviceDAO;
    private IBFTStatusHibernateDAO ibftStatusHibernateDAO;
    private ESBAdapter esbAdapter;
    @Autowired
    private SmsSender smsSender;

    public FonePayManager getFonePayManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (FonePayManager) applicationContext.getBean("fonePayFacade");
    }

    private void loadAndForwardCoreAdviceToQueue(final Object workFlowWrapper) throws InterruptedException {
        reversalAdviceQueingPreProcessor.startProcessing(workFlowWrapper);
    }

    @Override
    public MiddlewareMessageVO titleFetch(MiddlewareMessageVO integrationMessageVO) {
        logger.info("[IBFTSwitchController.titleFetch] (In Start) Account Number 1: " + integrationMessageVO.getAccountNo1() + " - Account Number 2: " + integrationMessageVO.getAccountNo2());
        logger.info("[IBFTSwitchController.titleFetch] (Trx Amount) " + integrationMessageVO.getTransactionAmount());
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        ActionLogModel actionLogModel = new ActionLogModel();

        String username = "0010787";
//		String password = "qOjtTfAzFuEIK6JOfoZa2A==";
        String password = "6tBH5Et3C3b9p7Xzr1YVIQ==";

        try {

            this.actionLogBeforeStart(integrationMessageVO, actionLogModel, true);

            DeviceTypeCommandModel deviceTypeCommandModel = new DeviceTypeCommandModel();
            deviceTypeCommandModel.setDeviceTypeId(DeviceTypeConstantsInterface.BANKING_MIDDLEWARE);
            deviceTypeCommandModel.setCommandId(Long.valueOf(CommandFieldConstants.TITLE_FETCH_COMMAND));
            baseWrapper.setBasePersistableModel(deviceTypeCommandModel);
            CustomList<DeviceTypeCommandModel> customList = getDeviceTypeCommandManager().loadDeviceTypeCommand(baseWrapper);
            if (null != customList) {
                List<DeviceTypeCommandModel> list = customList.getResultsetList();
                if (list.isEmpty()) {
                    logger.error("[IBFT TitleFetch] Unable to Load Device Type Command - ErrorCode:" + IBFTErrorCodes.GENERAL_ERROR);
                    return processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.GENERAL_ERROR);
                } else {
                    deviceTypeCommandModel = list.get(0);

                    baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.BANKING_MIDDLEWARE);
                    baseWrapper.putObject(CommandFieldConstants.KEY_U_ID, username);
                    baseWrapper.putObject(CommandFieldConstants.KEY_PIN, password);
//					baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, null);
                    getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_LOGIN);

                    AppUserModel appUserModel = getAllPayWebResponseDataPopulator().getAppUserModel(username);
                    baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.BANKING_MIDDLEWARE);
                    baseWrapper.setBasePersistableModel(appUserModel);
                    ValidationErrors validationErrors = getCommonCommandManager().checkUserCredentials(baseWrapper);
                    if (validationErrors.hasValidationErrors()) {
                        logger.error("[IBFT TitleFetch] Invalid credentials for default User. Error:" + validationErrors.getErrors());
                        return processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.GENERAL_ERROR);
                    }
                }
            }
        } catch (Exception e) {
            this.logger.error("[IBFT TitleFetch] Exception occurred while trying to Login default User. Details: ", e);
            return processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.GENERAL_ERROR);
        }

        baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, integrationMessageVO.getAccountNo2());
        baseWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, integrationMessageVO.getTransactionAmount());
        baseWrapper.putObject(CommandFieldConstants.KEY_ST_TX_NO, integrationMessageVO.getStan());
        baseWrapper.putObject(CommandFieldConstants.KEY_RRN, integrationMessageVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TX_DATE, integrationMessageVO.getRequestTime());
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALL_PAY);
        String response = null;

        try {
            response = this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.TITLE_FETCH_COMMAND);
            if (!StringUtil.isNullOrEmpty(response)) {
                String[] responseArr = response.split(",");
                if (responseArr != null && responseArr[0] != null) {
                    String responseCode = responseArr[0];

                    logger.info("[IBFT TitleFetch] Resp Code from TitleFetchCommand: " + responseCode);

                    if (responseCode.equalsIgnoreCase(IBFTErrorCodes.SUCCESS)) {
                        processSuccessResponse(integrationMessageVO, responseArr);
                    } else {
                        integrationMessageVO = processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.GENERAL_ERROR);
                    }
                }
            } else {
                logger.error("[IBFT TitleFetch] Response recieved from TitleFetchCommand in null");
                integrationMessageVO = processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.GENERAL_ERROR);
            }

            this.actionLogAfterEnd(actionLogModel);

        } catch (CommandException e) {
            if (e.getErrorCode() == ErrorCodes.INVALID_USER_ACCOUNT.longValue()) {
                logger.error("[IBFTSwitchController.titleFetch] Invalid User Account Error occured: ", e);
                integrationMessageVO = processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.ACCOUNT_INVALID);

            } else if (e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.DAILY_CREDIT_LIMIT_BUSTED.toString())
                    || e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.MONTHLY_CREDIT_LIMIT_BUSTED.toString())
                    || e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.YEARLY_CREDIT_LIMIT_BUSTED.toString())
                    || e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.BALANCE_LIMIT_BUSTED.toString())) {

                logger.error("[IBFTSwitchController.titleFetch] Account Credit Limit Exceeds Error occured: ", e);
                integrationMessageVO = processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.LIMIT_ERROR);

            } else if (e.getMessage().equalsIgnoreCase(IBFTErrorCodes.ACCOUNT_CLOSED.toString())) {

                logger.error("[IBFTSwitchController.titleFetch] (Your account has been closed. Please contact service provider)", e);
                integrationMessageVO = processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.ACCOUNT_INACTIVE);

            } else if (e.getMessage().equalsIgnoreCase(IBFTErrorCodes.ACCOUNT_DEACTIVATED.toString())) {

                logger.error("[IBFTSwitchController.titleFetch] (Your account has been deactivated.", e);
                integrationMessageVO = processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.ACCOUNT_INACTIVE);

            } else if (e.getMessage().equalsIgnoreCase(IBFTErrorCodes.CHANGE_PIN_REQUIRED.toString())) {

                logger.error("[IBFTSwitchController.titleFetch] (Account Pin Change is Required)", e);
                integrationMessageVO = processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.ACCOUNT_INACTIVE);

            } else if (e.getMessage().equalsIgnoreCase(IBFTErrorCodes.NO_CREDENTIALS_EXIST.toString())) {

                logger.error("[IBFTSwitchController.titleFetch] (No Credentials Exists Against that User)", e);
                integrationMessageVO = processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.ACCOUNT_INVALID);

            } else if (e.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.HRA_ERROR_MSG.toString())) {

                logger.error("[IBFTSwitchController.titleFetch] (Transfer of funds to HRA Account is not allowed.)", e);
                integrationMessageVO = processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.ACCOUNT_INVALID);

            } else if (StringUtils.contains(e.getMessage(), "is less than the minimum Product Limit") ||
                    StringUtils.contains(e.getMessage(), "is greater than the maximum Product Limit")) {

                logger.error("[IBFTSwitchController.titleFetch] Account Credit Limit Exceeds Error occured: ", e);
                integrationMessageVO = processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.LIMIT_ERROR);

            } else {

                logger.error("[IBFTSwitchController.titleFetch] Error occured: ", e);
                integrationMessageVO = processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.GENERAL_ERROR);

            }

        } catch (Exception e) {
            logger.error("[IBFTSwitchController.titleFetch] Error occured: ", e);
            integrationMessageVO = processErrorResponseCode(integrationMessageVO, IBFTErrorCodes.GENERAL_ERROR);
        }

        logger.info("[IBFTSwitchController.titleFetch] (In End) Response Code: " + integrationMessageVO.getResponseCode());

        return integrationMessageVO;

    }


    /**
     * @param integrationMessageVO
     * @param responseArr
     */
    private void processSuccessResponse(MiddlewareMessageVO integrationMessageVO, String[] responseArr) {
        integrationMessageVO.setResponseCode(IBFTErrorCodes.SUCCESS);
        StringBuilder titleOfTheAccount = new StringBuilder();
        if (responseArr[1] != null) {
            integrationMessageVO.setAccountBalance(responseArr[1]);
        }
        if (responseArr[2] != null) {
            titleOfTheAccount.append(responseArr[2]);
        }
        if (responseArr[3] != null) {
            titleOfTheAccount.append(" ");
            titleOfTheAccount.append(responseArr[3]);
        }
        integrationMessageVO.setAccountTitle(titleOfTheAccount.toString());
    }

    @Override
    public MiddlewareMessageVO creditAdvice(MiddlewareMessageVO integrationMessageVO) throws RuntimeException {
        MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();

        middlewareAdviceVO.setAccountNo1(integrationMessageVO.getAccountNo1());
        middlewareAdviceVO.setAccountNo2(integrationMessageVO.getAccountNo2());
        middlewareAdviceVO.setTransactionAmount(integrationMessageVO.getTransactionAmount());
        middlewareAdviceVO.setRequestTime(integrationMessageVO.getRequestTime());
        middlewareAdviceVO.setStan(integrationMessageVO.getStan());
        middlewareAdviceVO.setRetrievalReferenceNumber(integrationMessageVO.getRetrievalReferenceNumber());
        middlewareAdviceVO.setAdviceType(PortalConstants.IBFT_ADVICE_TYPE); // Used in DLQ
        middlewareAdviceVO.setBankIMD(integrationMessageVO.getBankIMD());

        try {
            logger.info("IBFTSwitchController.creditAdvice checking if already STAN exists: ");
            boolean isAlreadyExists = this.checkAlreadyExists(integrationMessageVO.getStan(), integrationMessageVO.getRequestTime());
            boolean existsInIbftTable = this.ibftStatusHibernateDAO.CheckIBFTStatus(integrationMessageVO.getStan(), integrationMessageVO.getRequestTime().toString());
            if (isAlreadyExists || existsInIbftTable) {
                logger.info("IBFTSwitchController.creditAdvice Error occured while checking STAN: ");
                integrationMessageVO.setResponseCode(IBFTErrorCodes.SUCCESS);
                logger.info("IBFTSwitchController.creditAdvice Response: " + integrationMessageVO.getResponseCode());
//				return integrationMessageVO;
            }
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }

        logger.info("IBFTSwitchController.creditAdvice (In Start) Account Number 1: " + integrationMessageVO.getAccountNo1() + " - Account Number 2: " + integrationMessageVO.getAccountNo2());
        logger.info("IBFTSwitchController.creditAdvice (In Start) Trx Amount: " + integrationMessageVO.getTransactionAmount());
        ActionLogModel actionLogModel = new ActionLogModel();
        IBFTIncomingRequestQueue ibftIncomingRequestQueue = getIBFTIncomingRequestQueue();
        try {
            this.actionLogBeforeStart(integrationMessageVO, actionLogModel, false);

            /* save new IBFT record in IBFT_RETRY_ADVICE table (status = 'Pushed to SAF')
             * - will be marked as 'Successful' in IBFTTransaction.doSale
             * - will be marked as 'Failed' in DlqMessageListener.onMessage
             */
            boolean isAlreadyExists = this.checkAlreadyExists(integrationMessageVO.getStan(), integrationMessageVO.getRequestTime());
            boolean existsInIbftTable = this.ibftStatusHibernateDAO.CheckIBFTStatus(integrationMessageVO.getStan(), integrationMessageVO.getRequestTime().toString());
            //if no transaction exists with same stan and reqTime
            if (!isAlreadyExists && !existsInIbftTable) {
                getTransactionReversalManager().saveNewIBFTRecord(middlewareAdviceVO);
                ibftStatusHibernateDAO.AddToProcessing(integrationMessageVO.getStan(), integrationMessageVO.getRequestTime().toString());

            }
            //if no transaction exists with same stan and reqTime in ibft_status table then send queue request
            if (!existsInIbftTable)
                ibftIncomingRequestQueue.sentIBFTRequest(middlewareAdviceVO);

            integrationMessageVO.setResponseCode(IBFTErrorCodes.SUCCESS);
            integrationMessageVO.setAccountBalance("0.0");

            this.actionLogAfterEnd(actionLogModel);
        } catch (Exception e) {
            logger.error("IBFTSwitchController.creditAdvice Error occured: ", e);
            integrationMessageVO.setResponseCode(IBFTErrorCodes.GENERAL_ERROR);
        }
        logger.info("IBFTSwitchController.creditAdvice (In End) Response: " + integrationMessageVO.getResponseCode());
        return integrationMessageVO;

    }


    @Override
    public MiddlewareMessageVO coreToWalletAdvice(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException {
        MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();

        middlewareAdviceVO.setAccountNo1(middlewareMessageVO.getAccountNo1());
        middlewareAdviceVO.setAccountNo2(middlewareMessageVO.getAccountNo2());
        middlewareAdviceVO.setTransactionAmount(middlewareMessageVO.getTransactionAmount());
        middlewareAdviceVO.setRequestTime(middlewareMessageVO.getRequestTime());
        middlewareAdviceVO.setStan(middlewareMessageVO.getStan());
        middlewareAdviceVO.setRetrievalReferenceNumber(middlewareMessageVO.getRetrievalReferenceNumber());
        middlewareAdviceVO.setAdviceType(PortalConstants.IBFT_ADVICE_TYPE); // Used in DLQ
        middlewareAdviceVO.setBankIMD(middlewareMessageVO.getBankIMD());
        middlewareAdviceVO.setProductId(ProductConstantsInterface.CORE_TO_WALLET);
        try {
            logger.info("IBFTSwitchController.coreToWalletAdvice checking if already STAN exists: ");
            boolean isAlreadyExists = this.checkAlreadyExists(middlewareMessageVO.getStan(), middlewareMessageVO.getRequestTime());
            if (isAlreadyExists) {
                logger.info("IBFTSwitchController.coreToWalletAdvice Error occured while checking STAN: ");
                middlewareMessageVO.setResponseCode(IBFTErrorCodes.SUCCESS);
                logger.info("IBFTSwitchController.coreToWalletAdvice Response: " + middlewareMessageVO.getResponseCode());
//				return integrationMessageVO;
            }
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }

        logger.info("IBFTSwitchController.coreToWalletAdvice (In Start) Account Number 1: " + middlewareMessageVO.getAccountNo1() + " - Account Number 2: " + middlewareMessageVO.getAccountNo2());
        logger.info("IBFTSwitchController.coreToWalletAdvice (In Start) Trx Amount: " + middlewareMessageVO.getTransactionAmount());
        ActionLogModel actionLogModel = new ActionLogModel();
        WalletIncomingRequestQueue walletIncomingRequestQueue = getWalletIncomingRequestQueue();
        try {
            this.actionLogBeforeStart(middlewareMessageVO, actionLogModel, false);

            /* save new IBFT record in IBFT_RETRY_ADVICE table (status = 'Pushed to SAF')
             * - will be marked as 'Successful' in IBFTTransaction.doSale
             * - will be marked as 'Failed' in DlqMessageListener.onMessage
             */
            boolean isAlreadyExists = this.checkAlreadyExists(middlewareMessageVO.getStan(), middlewareMessageVO.getRequestTime());
            if (!isAlreadyExists) {
                getTransactionReversalManager().saveNewIBFTRecord(middlewareAdviceVO);
            }
            walletIncomingRequestQueue.sentWalletRequest(middlewareAdviceVO);

            middlewareMessageVO.setResponseCode(IBFTErrorCodes.SUCCESS);
            middlewareMessageVO.setAccountBalance("0.0");

            this.actionLogAfterEnd(actionLogModel);
        } catch (Exception e) {
            logger.error("IBFTSwitchController.coreToWalletAdvice Error occured: ", e);
            middlewareMessageVO.setResponseCode(IBFTErrorCodes.GENERAL_ERROR);
        }
        logger.info("IBFTSwitchController.coreToWalletAdvice (In End) Response: " + middlewareMessageVO.getResponseCode());
        return middlewareMessageVO;
    }

    @Override
    public MiddlewareMessageVO cardLessCashWithDrawal(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException {
        logger.info("*** Debit Card CashWith Transaction Received with Stan ***" + middlewareMessageVO.getStan());

        FonePayLogModel fonePayLogModel = null;
        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        AppUserModel appUserModel1 = new AppUserModel();
        try {


                logger.info("*** Load App User against Mobile No***" + middlewareMessageVO.getAccountNo1());

                appUserModel1 = commonCommandManager.loadAppUserByMobileAndType(middlewareMessageVO.getAccountNo1(), UserTypeConstantsInterface.CUSTOMER);
                if (appUserModel1==null) {
                middlewareMessageVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND.toString());
                middlewareMessageVO.setResponseDescription(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, middlewareMessageVO.getResponseCode());
                return middlewareMessageVO;
            }

        } catch (FrameworkCheckedException e) {
            e.getMessage();
        }
        CustomerModel customerModel = new CustomerModel();
        try {
            customerModel = commonCommandManager.getCustomerModelById(appUserModel1.getCustomerId());
        } catch (CommandException e) {
            e.printStackTrace();
        }

        if (customerModel != null && customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
            middlewareMessageVO.setResponseCode("07");
            middlewareMessageVO.setResponseDescription("Upgrade Account L0 to L1 to perform Transaction.");
            FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, "07");
            return middlewareMessageVO;
        }
        try {
            String productId = null;
            String reqType = null;
            String terminalId = null;
            if (middlewareMessageVO.getCurrencyCode() == null || middlewareMessageVO.getCurrencyCode().equals("") || middlewareMessageVO.getCurrencyCode().equals("586")) {
                        productId = ProductConstantsInterface.DEBIT_CARD_LESS_CASH_WITHDRAWAL.toString();
                        reqType = FonePayConstants.REQ_DEBIT_CARD_LESS_CASH_WITHDRAWL;
                        terminalId = "ATM";


                }

            //below check add against product id to send merchant Camping Transaction validation  and Transaction Update
                ActionLogModel actionLogModel = new ActionLogModel();
                actionLogBeforeStart(middlewareMessageVO, actionLogModel, false);
                fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModelForDebitCardReq(middlewareMessageVO, reqType);
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                WebServiceVO webServiceVO = new WebServiceVO();
//                DebitCardModel debitCardModel = commonCommandManager.getDebitCardModelDao().getDebitCardModelByCardNumber(middlewareMessageVO.getPAN());
//                    DebitCardUtill.verifyDebitCard(webServiceVO, debitCardModel);
//                    if (webServiceVO.getResponseCode().equals("00")) {
                    Boolean isBlackListed = commonCommandManager.isCnicBlacklisted(appUserModel1.getNic());
                    if (isBlackListed) {
                        middlewareMessageVO.setResponseCode(FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
                        FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
                        getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                        return middlewareMessageVO;
                    }
            Long[] productIds = new Long[]{ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US,
                    ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_ON_US,
                    ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL
                    , ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US, ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL,
                    ProductConstantsInterface.DEBIT_CARD_LESS_CASH_WITHDRAWAL
            };
                    UserDeviceAccountsModel uda = userDeviceAccountsDAO.findUserDeviceAccountByAppUserId(appUserModel1.getAppUserId());
                    Boolean isValid = postedTransactionReportDAO.validateDuplicateStan(productIds, middlewareMessageVO.getStan(), uda.getUserId());
                    if (!isValid) {
                        logger.error("Debit Card Transaction with Stan " + middlewareMessageVO.getStan() + " already performed on date " + new Date().toString() +
                                " against the Recipient USER_ID: " + uda.getUserId());
                        logger.info("*** Duplicate Stan ***");
                        middlewareMessageVO.setResponseCode(FonePayResponseCodes.STAN_ALREADY_EXISTS.toString());
                        middlewareMessageVO.setResponseDescription(FonePayResponseCodes.STAN_ALREADY_EXISTS_DESCRIPTION);
                        getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                        return middlewareMessageVO;
                    }
                    webServiceVO = this.prepareWebServiceVOForCardLessBalanceInquiry(middlewareMessageVO, webServiceVO);
                    webServiceVO = fonePaySwitchController.balanceInquiry(webServiceVO);
                    if (!webServiceVO.getResponseCode().equals("00")) {
                        middlewareMessageVO.setResponseCode(webServiceVO.getResponseCode());
                        FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, middlewareMessageVO.getResponseCode());
                        getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                        return middlewareMessageVO;
                    }
                    String paymentMode = null;
                    Double balance = Double.parseDouble(webServiceVO.getBalance());
                    if (Double.parseDouble(middlewareMessageVO.getTransactionAmount()) > balance) {
                        SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel1, PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
                        if (smartMoneyAccountModel != null) {
                            webServiceVO = this.prepareWebServiceVOForCardLessBalanceInquiry(middlewareMessageVO, webServiceVO);
                            webServiceVO.setPaymentMode("HRA");
                            logger.info("*** Debit Card Balance check");
                            webServiceVO = fonePaySwitchController.balanceInquiry(webServiceVO);
                            logger.info("*** Debit Card Balance check Time");
                            balance = Double.parseDouble(webServiceVO.getBalance());
                            if (Double.parseDouble(middlewareMessageVO.getTransactionAmount()) > balance) {
                                middlewareMessageVO.setResponseCode(FonePayResponseCodes.LOW_BALANCE);
                                middlewareMessageVO.setResponseDescription("Low Balance in HRA Account.");
                                FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.LOW_BALANCE);
                                //getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                                return middlewareMessageVO;
                            }
                            paymentMode = "HRA";
                        } else {
                            middlewareMessageVO.setResponseCode(FonePayResponseCodes.LOW_BALANCE);
                            FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.LOW_BALANCE);
                            //getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                            return middlewareMessageVO;
                        }
                    } else
                        paymentMode = "";
                    webServiceVO.setRetrievalReferenceNumber(middlewareMessageVO.getRetrievalReferenceNumber());
                    baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, appUserModel1.getMobileNo());
                    baseWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, middlewareMessageVO.getTransactionAmount());
                    String response = null;
                    baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
                    baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ATM);
                    baseWrapper.putObject(CommandFieldConstants.KEY_TXAM, middlewareMessageVO.getTransactionAmount());
                    baseWrapper.putObject(CommandFieldConstants.KEY_TPAM, "0");
                    baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, appUserModel1.getMobileNo());
                    baseWrapper.putObject(CommandFieldConstants.KEY_TAMT, middlewareMessageVO.getTransactionAmount());
                    baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, terminalId);
                    baseWrapper.putObject(CommandFieldConstants.KEY_STAN, middlewareMessageVO.getStan());
                    baseWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, paymentMode);
                    baseWrapper.putObject("RRN", middlewareMessageVO.getRetrievalReferenceNumber());
                    baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "2");
                    baseWrapper.putObject("ACCEPTOR_DETAILS", middlewareMessageVO.getCardAcceptorNameAndLocation());
                    response = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_DEBIT_CARD_CW);
                    middlewareMessageVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    middlewareMessageVO.setResponseDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                    middlewareMessageVO.setResponseContentXML(response);
                    logger.info("Remaining Balance: " + middlewareMessageVO.getAccountBalance());
//                } else {
//                    middlewareMessageVO.setResponseCode(webServiceVO.getResponseCode());
//                    FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, webServiceVO.getResponseCode());
//                    getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
//                    return middlewareMessageVO;
//                }
//            }
        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else {
                logger.error("[FonePaySwitchController.CashIn] Command Exception Error occured:" + e.getMessage(), e);
                middlewareMessageVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                middlewareMessageVO.setResponseDescription(e.getMessage());
            }
        } catch (Exception e) {

            this.logger.error("[FonePaySwitchController.CashIn] Error occured: " + e.getMessage(), e);
            middlewareMessageVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            middlewareMessageVO.setResponseDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                middlewareMessageVO = FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
        }
        this.logger.info("Debit Card Cash With Drawl => Response Code: " + middlewareMessageVO.getResponseCode() + ", Description: " +
                middlewareMessageVO.getResponseDescription() + ", Mobile No: " + middlewareMessageVO.getMobileNo() +
                ", PAN: " + middlewareMessageVO.getPAN() + ", Amount: " + middlewareMessageVO.getTransactionAmount());
        return middlewareMessageVO;
    }

    private boolean checkAlreadyExists(String stan, Date requestTime) throws FrameworkCheckedException {
        boolean result = false;

        if (StringUtil.isNullOrEmpty(stan) || requestTime == null) {
            throw new FrameworkCheckedException("Invalid Input for IBFT Credit Advice. STAN:" + stan + " , Request Time:" + requestTime);
        }

        IBFTRetryAdviceModel iBFTRetryAdviceModel = new IBFTRetryAdviceModel();
        iBFTRetryAdviceModel.setStan(stan);
        iBFTRetryAdviceModel.setRequestTime(requestTime);

        Calendar c = Calendar.getInstance();
        c.setTime(requestTime);
        c.set(Calendar.MILLISECOND, 0);

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("requestTime", c.getTime(), c.getTime());

        LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
        sortingOrderMap.put("ibftRetryAdviceId", SortingOrder.DESC);


        CustomList<IBFTRetryAdviceModel> customList = ibftRetryAdviceDAO.findByExample(
                iBFTRetryAdviceModel, null, sortingOrderMap, dateRangeHolderModel, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        List<IBFTRetryAdviceModel> list = customList.getResultsetList();

        if (list != null && list.size() > 0) {
            result = true;
        }

        return result;
    }

    private WebServiceVO prepareWebServiceVOForBalanceInquiry(MiddlewareMessageVO middlewareMessageVO, WebServiceVO webServiceVO) {
        webServiceVO.setChannelId(FonePayConstants.DEBIT_CARD_CHANNEL);
        webServiceVO.setTerminalId(FonePayConstants.DEBIT_CARD_CHANNEL);
        webServiceVO.setCardNo(middlewareMessageVO.getPAN());
        webServiceVO.setRetrievalReferenceNumber(middlewareMessageVO.getRetrievalReferenceNumber());
        webServiceVO.setResponseCode("");
        webServiceVO.setResponseCodeDescription("");
        return webServiceVO;
    }

    private WebServiceVO prepareWebServiceVOForCardLessBalanceInquiry(MiddlewareMessageVO middlewareMessageVO, WebServiceVO webServiceVO) {
        webServiceVO.setChannelId(FonePayConstants.DEBIT_CARD_CARDLESS_CHANNEL);
        webServiceVO.setTerminalId(FonePayConstants.DEBIT_CARD_CARDLESS_CHANNEL);
        webServiceVO.setMobileNo(middlewareMessageVO.getAccountNo1());
        webServiceVO.setRetrievalReferenceNumber(middlewareMessageVO.getRetrievalReferenceNumber());
        webServiceVO.setResponseCode("");
        webServiceVO.setResponseCodeDescription("");
        return webServiceVO;
    }

    private MiddlewareMessageVO debitCardCashWithDrawlTransaction(MiddlewareMessageVO middlewareMessageVO, Boolean isPosTrx) {
        logger.info("*** Debit Card CashWith Transaction Received with Stan ***" + middlewareMessageVO.getStan());

        FonePayLogModel fonePayLogModel = null;
        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        AppUserModel appUserModel1 = new AppUserModel();
        try {
            logger.info("*** Debit Card Load Against Card No***" + middlewareMessageVO.getPAN());

            DebitCardModel debitCardModel = commonCommandManager.getDebitCardModelDao().getDebitCardModelByCardNumber(middlewareMessageVO.getPAN());

            if (debitCardModel != null) {
                logger.info("*** Load App User against Mobile No***" + debitCardModel.getMobileNo());

                appUserModel1 = commonCommandManager.loadAppUserByMobileAndType(debitCardModel.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            } else {
                middlewareMessageVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND.toString());
                middlewareMessageVO.setResponseDescription(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, middlewareMessageVO.getResponseCode());
                return middlewareMessageVO;
            }

        } catch (FrameworkCheckedException e) {
            e.getMessage();
        }
        CustomerModel customerModel = new CustomerModel();
        try {
            customerModel = commonCommandManager.getCustomerModelById(appUserModel1.getCustomerId());
        } catch (CommandException e) {
            e.printStackTrace();
        }

        if (customerModel != null && customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
            middlewareMessageVO.setResponseCode("07");
            middlewareMessageVO.setResponseDescription("Upgrade Account L0 to L1 to perform Transaction.");
            FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, "07");
            return middlewareMessageVO;
        }
        try {
            String productId = null;
            String reqType = null;
            String terminalId = null;
            if (middlewareMessageVO.getCurrencyCode() == null || middlewareMessageVO.getCurrencyCode().equals("") || middlewareMessageVO.getCurrencyCode().equals("586")) {
                if (middlewareMessageVO.getMerchantType().equals("0000") && !isPosTrx) {
                    if (middlewareMessageVO.getTerminalId() != null && middlewareMessageVO.getTerminalId().equals("CARD_LESS_DEBIT_CARD")) {

                    } else {
                        productId = ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_ON_US.toString();
                        reqType = FonePayConstants.REQ_DEBIT_CARD_CASH_WITHDRAWL_ON_US;
                        terminalId = "ATM";
                    }
                } else if (!isPosTrx) {
                    if (middlewareMessageVO.getTerminalId() != null && middlewareMessageVO.getTerminalId().equals("CARD_LESS_DEBIT_CARD")) {

                    } else {
                        productId = ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US.toString();
                        reqType = FonePayConstants.REQ_DEBIT_CARD_CASH_WITHDRAWL_OFF_US;
                        terminalId = "ATM";
                    }

                } else {
                    productId = ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL.toString();
                    reqType = FonePayConstants.REQ_POS_DEBIT_CARD_CASH_WITHDRAWL;
                    terminalId = "POS";
                }
            } else {
                if (customerModel != null && (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK) || customerModel.getCustomerAccountTypeId().equals(56L))) {

                    if (!isPosTrx) {
                        productId = ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US.toString();
                        reqType = FonePayConstants.REQ_INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWL_OFF_US;
                        middlewareMessageVO.setTransactionAmount(middlewareMessageVO.getAmountCardHolderBilling());
                        terminalId = "ATM";
                    } else {
                        productId = ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL.toString();
                        reqType = FonePayConstants.REQ_INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWL_OFF_US;
                        middlewareMessageVO.setTransactionAmount(middlewareMessageVO.getAmountCardHolderBilling());
                        terminalId = "POS";

                    }
                } else {
                    middlewareMessageVO.setResponseCode(FonePayResponseCodes.INTERNATIONAL_TRANSACTION_NOT_ALLOWED.toString());
                    middlewareMessageVO.setResponseDescription(FonePayResponseCodes.INTERNATIONAL_TRANSACTION_NOT_ALLOWED_DESCRIPTION);
                    FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, "79");
                    return middlewareMessageVO;
                }
            }

            //below check add against product id to send merchant Camping Transaction validation  and Transaction Update

            if (productId.equals(ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL.toString())) {
                //below line of code send Transaction validation Call to Validate Transaction Against Merchant

                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                requestVO = ESBAdapter.prepareMerchantCampingRequest(I8SBConstants.RequestType_TransactionInquiry);
                requestVO.setUserId(String.valueOf(appUserModel1.getAppUserId()));
                requestVO.setCNIC(appUserModel1.getNic());
                requestVO.setCardNumber(middlewareMessageVO.getPAN());
                requestVO.setMerchantName(middlewareMessageVO.getCardAcceptorNameAndLocation());
                requestVO.setSTAN(middlewareMessageVO.getStan());
                requestVO.setRRN(middlewareMessageVO.getRetrievalReferenceNumber());
                requestVO.setMobileNumber(appUserModel1.getMobileNo());
                requestVO.setTransactionAmount(middlewareMessageVO.getTransactionAmount());
                requestVO.setTransactionCodeDesc("POS PURCHASE");
                requestVO.setTransactionType("C");
                requestVO.setSegmentId(String.valueOf(customerModel.getSegmentId()));

                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                if (!responseVO.getResponseCode().equals("I8SB-200"))
                    throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                ActionLogModel actionLogModel = new ActionLogModel();
                actionLogBeforeStart(middlewareMessageVO, actionLogModel, false);
                fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModelForDebitCardReq(middlewareMessageVO, reqType);
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                WebServiceVO webServiceVO = new WebServiceVO();
                DebitCardModel debitCardModel = commonCommandManager.getDebitCardModelDao().getDebitCardModelByCardNumber(middlewareMessageVO.getPAN());
                DebitCardUtill.verifyDebitCard(webServiceVO, debitCardModel);
                if (webServiceVO.getResponseCode().equals("00")) {
                    Boolean isBlackListed = commonCommandManager.isCnicBlacklisted(debitCardModel.getCnic());
                    if (isBlackListed) {
                        middlewareMessageVO.setResponseCode(FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
                        FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
                        getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                        return middlewareMessageVO;
                    }
                    Long[] productIds = new Long[]{ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US,
                            ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_ON_US,
                            ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL
                            , ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US, ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL
                    };
                    AppUserModel appUserModel = commonCommandManager.loadAppUserByMobileAndType(debitCardModel.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
                    UserDeviceAccountsModel uda = userDeviceAccountsDAO.findUserDeviceAccountByAppUserId(appUserModel.getAppUserId());
                    Boolean isValid = postedTransactionReportDAO.validateDuplicateStan(productIds, middlewareMessageVO.getStan(), uda.getUserId());
                    if (!isValid) {
                        logger.error("Debit Card Transaction with Stan " + middlewareMessageVO.getStan() + " already performed on date " + new Date().toString() +
                                " against the Recipient USER_ID: " + uda.getUserId());
                        logger.info("*** Duplicate Stan ***");
                        //below line if Stan is Duplicate send update Transaction Status Call
                        requestVO = new I8SBSwitchControllerRequestVO();
                        responseVO = new I8SBSwitchControllerResponseVO();
                        requestVO = ESBAdapter.prepareMerchantCampingRequest(I8SBConstants.RequestType_TransactionStatus);
                        requestVO.setUserId(String.valueOf(appUserModel1.getAppUserId()));
                        requestVO.setCNIC(appUserModel1.getNic());
                        requestVO.setSTAN(middlewareMessageVO.getStan());
                        requestVO.setRRN(middlewareMessageVO.getRetrievalReferenceNumber());
                        requestVO.setStatus("U");
                        sWrapper = new SwitchWrapperImpl();
                        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                        sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                        middlewareMessageVO.setResponseCode(FonePayResponseCodes.STAN_ALREADY_EXISTS.toString());
                        middlewareMessageVO.setResponseDescription(FonePayResponseCodes.STAN_ALREADY_EXISTS_DESCRIPTION);
                        getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                        return middlewareMessageVO;
                    }
                    webServiceVO = this.prepareWebServiceVOForBalanceInquiry(middlewareMessageVO, webServiceVO);
                    logger.info("*** Debit Card Balance check");

                    webServiceVO = fonePaySwitchController.balanceInquiry(webServiceVO);
                    logger.info("*** Debit Card Balance check Time");

                    if (!webServiceVO.getResponseCode().equals("00")) {
                        middlewareMessageVO.setResponseCode(webServiceVO.getResponseCode());
                        FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, middlewareMessageVO.getResponseCode());
                        getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                        return middlewareMessageVO;
                    }
                    String paymentMode = null;
                    Double balance = Double.parseDouble(webServiceVO.getBalance());
                    if (Double.parseDouble(middlewareMessageVO.getTransactionAmount()) > balance) {
                        SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel, PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
                        if (smartMoneyAccountModel != null) {
                            webServiceVO = this.prepareWebServiceVOForBalanceInquiry(middlewareMessageVO, webServiceVO);
                            webServiceVO.setPaymentMode("HRA");
                            webServiceVO = fonePaySwitchController.balanceInquiry(webServiceVO);
                            balance = Double.parseDouble(webServiceVO.getBalance());
                            if (Double.parseDouble(middlewareMessageVO.getTransactionAmount()) > balance) {
                                middlewareMessageVO.setResponseCode(FonePayResponseCodes.LOW_BALANCE);
                                middlewareMessageVO.setResponseDescription("Low Balance in HRA Account.");
                                FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.LOW_BALANCE);
                                //getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                                return middlewareMessageVO;
                            }
                            paymentMode = "HRA";
                        } else {
                            middlewareMessageVO.setResponseCode(FonePayResponseCodes.LOW_BALANCE);
                            FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.LOW_BALANCE);
                            //getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                            return middlewareMessageVO;
                        }
                    } else
                        paymentMode = "";
                    webServiceVO.setRetrievalReferenceNumber(middlewareMessageVO.getRetrievalReferenceNumber());
                    baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, debitCardModel.getMobileNo());
                    baseWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, middlewareMessageVO.getTransactionAmount());
                    String response = null;
                    baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
                    baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ATM);
                    baseWrapper.putObject(CommandFieldConstants.KEY_TXAM, middlewareMessageVO.getTransactionAmount());
                    baseWrapper.putObject(CommandFieldConstants.KEY_TPAM, "0");
                    baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, debitCardModel.getMobileNo());
                    baseWrapper.putObject(CommandFieldConstants.KEY_TAMT, middlewareMessageVO.getTransactionAmount());
                    baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, terminalId);
                    baseWrapper.putObject(CommandFieldConstants.KEY_STAN, middlewareMessageVO.getStan());
                    baseWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, paymentMode);
                    baseWrapper.putObject("RRN", middlewareMessageVO.getRetrievalReferenceNumber());
                    baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "2");
                    baseWrapper.putObject("ACCEPTOR_DETAILS", middlewareMessageVO.getCardAcceptorNameAndLocation());
                    response = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_DEBIT_CARD_CW);
                    String transactionCode = MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.TRANS_ID_NODEREF);
                    String balanceAfterTransaction = MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.TRAN_BAL_NODEREF);
//
//                    MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
//                    middlewareAdviceVO.setMicrobankTransactionCode(transactionCode);
//                    middlewareAdviceVO.setAccountBalance(balanceAfterTransaction);
//                    middlewareAdviceVO.setDateTimeLocalTransaction(new Date());
//                    middlewareAdviceVO.setAccountNo1(appUserModel1.getMobileNo());
//                    middlewareAdviceVO.setStan(middlewareMessageVO.getStan());
//                    middlewareAdviceVO.setRetrievalReferenceNumber(middlewareMessageVO.getRetrievalReferenceNumber());
//                    middlewareAdviceVO.setBillStatus("S");
//                    middlewareAdviceVO.setAccountNo1(String.valueOf(appUserModel1.getAppUserId()));
//                    middlewareAdviceVO.setAdviceType("TransactionStatusUpdate");
                    //below Line transaction Status Update Call
                    requestVO = new I8SBSwitchControllerRequestVO();
                    responseVO = new I8SBSwitchControllerResponseVO();
                    requestVO = ESBAdapter.prepareMerchantCampingRequest(I8SBConstants.RequestType_TransactionStatus);
                    requestVO.setUserId(String.valueOf(appUserModel1.getAppUserId()));
                    requestVO.setTransactionCode(transactionCode);
                    requestVO.setAvailableBalance(balanceAfterTransaction);
                    requestVO.setMobileNumber(appUserModel1.getMobileNo());
                    requestVO.setTransactionDate(String.valueOf(new Date()));
                    requestVO.setRRN(middlewareMessageVO.getRetrievalReferenceNumber());
                    requestVO.setSTAN(middlewareMessageVO.getStan());
                    requestVO.setStatus("S");
                    requestVO.setTransactionAmount(middlewareMessageVO.getTransactionAmount());
                    requestVO.setTransactionType("C");
                    sWrapper = new SwitchWrapperImpl();
                    sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                    sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                    sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                    responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                    middlewareMessageVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    middlewareMessageVO.setResponseDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                    middlewareMessageVO.setResponseContentXML(response);
                    logger.info("Remaining Balance: " + middlewareMessageVO.getAccountBalance());
                } else {
                    middlewareMessageVO.setResponseCode(webServiceVO.getResponseCode());
                    FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, webServiceVO.getResponseCode());
                    getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                    return middlewareMessageVO;
                }


            } else {

                ActionLogModel actionLogModel = new ActionLogModel();
                actionLogBeforeStart(middlewareMessageVO, actionLogModel, false);
                fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModelForDebitCardReq(middlewareMessageVO, reqType);
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                WebServiceVO webServiceVO = new WebServiceVO();
                DebitCardModel debitCardModel = commonCommandManager.getDebitCardModelDao().getDebitCardModelByCardNumber(middlewareMessageVO.getPAN());
                DebitCardUtill.verifyDebitCard(webServiceVO, debitCardModel);
                if (webServiceVO.getResponseCode().equals("00")) {
                    Boolean isBlackListed = commonCommandManager.isCnicBlacklisted(debitCardModel.getCnic());
                    if (isBlackListed) {
                        middlewareMessageVO.setResponseCode(FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
                        FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
                        getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                        return middlewareMessageVO;
                    }
                    Long[] productIds = new Long[]{ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US,
                            ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_ON_US,
                            ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL
                            , ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US, ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL
                    };
                    AppUserModel appUserModel = commonCommandManager.loadAppUserByMobileAndType(debitCardModel.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
                    UserDeviceAccountsModel uda = userDeviceAccountsDAO.findUserDeviceAccountByAppUserId(appUserModel.getAppUserId());
                    Boolean isValid = postedTransactionReportDAO.validateDuplicateStan(productIds, middlewareMessageVO.getStan(), uda.getUserId());
                    if (!isValid) {
                        logger.error("Debit Card Transaction with Stan " + middlewareMessageVO.getStan() + " already performed on date " + new Date().toString() +
                                " against the Recipient USER_ID: " + uda.getUserId());
                        logger.info("*** Duplicate Stan ***");
                        middlewareMessageVO.setResponseCode(FonePayResponseCodes.STAN_ALREADY_EXISTS.toString());
                        middlewareMessageVO.setResponseDescription(FonePayResponseCodes.STAN_ALREADY_EXISTS_DESCRIPTION);
                        getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                        return middlewareMessageVO;
                    }
                    webServiceVO = this.prepareWebServiceVOForBalanceInquiry(middlewareMessageVO, webServiceVO);
                    webServiceVO = fonePaySwitchController.balanceInquiry(webServiceVO);
                    if (!webServiceVO.getResponseCode().equals("00")) {
                        middlewareMessageVO.setResponseCode(webServiceVO.getResponseCode());
                        FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, middlewareMessageVO.getResponseCode());
                        getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                        return middlewareMessageVO;
                    }
                    String paymentMode = null;
                    Double balance = Double.parseDouble(webServiceVO.getBalance());
                    if (Double.parseDouble(middlewareMessageVO.getTransactionAmount()) > balance) {
                        SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel, PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
                        if (smartMoneyAccountModel != null) {
                            webServiceVO = this.prepareWebServiceVOForBalanceInquiry(middlewareMessageVO, webServiceVO);
                            webServiceVO.setPaymentMode("HRA");
                            logger.info("*** Debit Card Balance check");
                            webServiceVO = fonePaySwitchController.balanceInquiry(webServiceVO);
                            logger.info("*** Debit Card Balance check Time");
                            balance = Double.parseDouble(webServiceVO.getBalance());
                            if (Double.parseDouble(middlewareMessageVO.getTransactionAmount()) > balance) {
                                middlewareMessageVO.setResponseCode(FonePayResponseCodes.LOW_BALANCE);
                                middlewareMessageVO.setResponseDescription("Low Balance in HRA Account.");
                                FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.LOW_BALANCE);
                                //getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                                return middlewareMessageVO;
                            }
                            paymentMode = "HRA";
                        } else {
                            middlewareMessageVO.setResponseCode(FonePayResponseCodes.LOW_BALANCE);
                            FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.LOW_BALANCE);
                            //getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                            return middlewareMessageVO;
                        }
                    } else
                        paymentMode = "";
                    webServiceVO.setRetrievalReferenceNumber(middlewareMessageVO.getRetrievalReferenceNumber());
                    baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, debitCardModel.getMobileNo());
                    baseWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, middlewareMessageVO.getTransactionAmount());
                    String response = null;
                    baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
                    baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ATM);
                    baseWrapper.putObject(CommandFieldConstants.KEY_TXAM, middlewareMessageVO.getTransactionAmount());
                    baseWrapper.putObject(CommandFieldConstants.KEY_TPAM, "0");
                    baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, debitCardModel.getMobileNo());
                    baseWrapper.putObject(CommandFieldConstants.KEY_TAMT, middlewareMessageVO.getTransactionAmount());
                    baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, terminalId);
                    baseWrapper.putObject(CommandFieldConstants.KEY_STAN, middlewareMessageVO.getStan());
                    baseWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, paymentMode);
                    baseWrapper.putObject("RRN", middlewareMessageVO.getRetrievalReferenceNumber());
                    baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "2");
                    baseWrapper.putObject("ACCEPTOR_DETAILS", middlewareMessageVO.getCardAcceptorNameAndLocation());
                    response = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_DEBIT_CARD_CW);
                    middlewareMessageVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    middlewareMessageVO.setResponseDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                    middlewareMessageVO.setResponseContentXML(response);
                    logger.info("Remaining Balance: " + middlewareMessageVO.getAccountBalance());
                } else {
                    middlewareMessageVO.setResponseCode(webServiceVO.getResponseCode());
                    FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, webServiceVO.getResponseCode());
                    getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                    return middlewareMessageVO;
                }
            }
        } catch (CommandException e) {
            //below transaction Status Update Call
            I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
            I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
            requestVO = new I8SBSwitchControllerRequestVO();
            responseVO = new I8SBSwitchControllerResponseVO();
            requestVO = ESBAdapter.prepareMerchantCampingRequest(I8SBConstants.RequestType_TransactionStatus);
            requestVO.setUserId(String.valueOf(appUserModel1.getAppUserId()));
            requestVO.setMobileNumber(appUserModel1.getMobileNo());
            requestVO.setTransactionDate(String.valueOf(new Date()));
            requestVO.setRRN(middlewareMessageVO.getRetrievalReferenceNumber());
            requestVO.setSTAN(middlewareMessageVO.getStan());
            requestVO.setStatus("U");
            requestVO.setTransactionAmount(middlewareMessageVO.getTransactionAmount());
            requestVO.setTransactionType("C");
            SwitchWrapper sWrapper = new SwitchWrapperImpl();
            sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
            sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
            sWrapper = esbAdapter.makeI8SBCall(sWrapper);
            responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else {
                logger.error("[FonePaySwitchController.CashIn] Command Exception Error occured:" + e.getMessage(), e);
                middlewareMessageVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                middlewareMessageVO.setResponseDescription(e.getMessage());
            }
        } catch (Exception e) {

            this.logger.error("[FonePaySwitchController.CashIn] Error occured: " + e.getMessage(), e);
            middlewareMessageVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            middlewareMessageVO.setResponseDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                middlewareMessageVO = FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
        }
        this.logger.info("Debit Card Cash With Drawl => Response Code: " + middlewareMessageVO.getResponseCode() + ", Description: " +
                middlewareMessageVO.getResponseDescription() + ", Mobile No: " + middlewareMessageVO.getMobileNo() +
                ", PAN: " + middlewareMessageVO.getPAN() + ", Amount: " + middlewareMessageVO.getTransactionAmount());
        return middlewareMessageVO;
    }

    @Override
    public MiddlewareMessageVO cashWithDrawal(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException {
        return debitCardCashWithDrawlTransaction(middlewareMessageVO, Boolean.FALSE);
    }

    private void prepareVOFromMasterModel(ManualReversalVO vo, TransactionDetailMasterModel transactionDetailMasterModel) {
        vo.setExclusiveCharges(transactionDetailMasterModel.getExclusiveCharges());
        vo.setInclusiveCharges(transactionDetailMasterModel.getInclusiveCharges());
        vo.setProductId(transactionDetailMasterModel.getProductId());
        vo.setProductName(transactionDetailMasterModel.getProductName());
        vo.setSupProcessingStatusId(transactionDetailMasterModel.getSupProcessingStatusId());
        vo.setSupProcessingStatusName(transactionDetailMasterModel.getProcessingStatusName());
        vo.setTransactionAmount(transactionDetailMasterModel.getTransactionAmount());
        vo.setTransactionDate(transactionDetailMasterModel.getCreatedOn());
    }

    private ManualReversalVO prepareReversalVO(String trxCode, ManualReversalVO manualReversalVO) {
        try {
            if (!StringUtil.isNullOrEmpty(trxCode)) {
                logger.info("Going to load details against transactionCode:" + trxCode);

                TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel();
                transactionDetailMasterModel.setTransactionCode(trxCode);
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                baseWrapper.setBasePersistableModel(transactionDetailMasterModel);
                transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
                transactionDetailMasterModel = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
                if (transactionDetailMasterModel == null || transactionDetailMasterModel.getTransactionId() == null) {
                    manualReversalVO.setTransactionCode("00");
                    return manualReversalVO;
                }
                prepareVOFromMasterModel(manualReversalVO, transactionDetailMasterModel);
                CommissionTransactionViewModel commissionTransactionViewModel = new CommissionTransactionViewModel();
                commissionTransactionViewModel.setTransactionId(trxCode);
                List<CommissionTransactionViewModel> list = commissionTransactionViewFacade.searchCommissionTransactionView(commissionTransactionViewModel);
                manualReversalVO.setCommissionTransactionList(list);
                BbStatementAllViewModel bbStatementAllViewModel = new BbStatementAllViewModel();
                bbStatementAllViewModel.setTransactionCode(trxCode);
                LinkedHashMap<String, SortingOrder> sortingMap = new LinkedHashMap<String, SortingOrder>();
                sortingMap.put("category", SortingOrder.ASC);
                sortingMap.put("ledgerId", SortingOrder.ASC);
                SearchBaseWrapper sbWrapper = new SearchBaseWrapperImpl();
                sbWrapper.setSortingOrderMap(sortingMap);
                sbWrapper.setBasePersistableModel(bbStatementAllViewModel);
                List<BbStatementAllViewModel> settlementBBStatementList = portalOlaFacade.searchBbStatementAllView(sbWrapper).getResultsetList();
                Boolean isReversal = Boolean.TRUE;
                if (null != settlementBBStatementList && settlementBBStatementList.size() > 0) {
                    for (BbStatementAllViewModel modelInList : settlementBBStatementList) {
                        modelInList.setAccountNumber(com.inov8.ola.util.EncryptionUtil.decryptAccountNo(modelInList.getAccountNumber()));
                        boolean isDebit = (modelInList.getDebitAmount() != null && modelInList.getDebitAmount() > 0) ? true : false;
                        if (isDebit) {
                            modelInList.setAmountStr(isReversal ? Formatter.formatDouble(modelInList.getDebitAmount()) : "");
                            modelInList.setCreditAmount(null);
                            modelInList.setTransactionType(isReversal ? TransactionTypeConstants.CREDIT : null); // reverse the action for reversal only
                        } else {
                            modelInList.setAmountStr(isReversal ? Formatter.formatDouble(modelInList.getCreditAmount()) : "");
                            modelInList.setDebitAmount(null);
                            modelInList.setTransactionType(isReversal ? TransactionTypeConstants.DEBIT : null); // reverse the action for reversal only
                        }
                    }
                }
                manualReversalVO.setFundTransferEntryList(settlementBBStatementList);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return manualReversalVO;
    }

    private Double parseAmount(String amountStr) {
        if (StringUtil.isNullOrEmpty(amountStr)) {
            return 0.0D;
        } else {
            return Double.parseDouble(amountStr);
        }
    }

    private Double formatAmount(Double amount) {
        if (amount == null) {
            return 0.0D;
        } else {
            String amt = Formatter.formatDouble(amount);
            return Double.parseDouble(amt);
        }
    }

    private ManualReversalVO validateManualReversalForm(ManualReversalVO manualReversalVO) {
        if (!StringUtil.isNullOrEmpty(manualReversalVO.getTransactionCode())) {
            manualReversalVO = prepareReversalVO(manualReversalVO.getTransactionCode(), manualReversalVO);
            double totalDebit = 0;
            double totalCredit = 0;
            double fmtAmount = 0;
            TransactionDetailMasterModel tdmModel = null;
            try {
                tdmModel = this.manualReversalFacade.getTransactionDetailMasterModel(manualReversalVO.getTransactionCode());
                if (tdmModel == null) {
                    manualReversalVO.setComments("No");
                    return manualReversalVO;
                } else if (SupplierProcessingStatusConstants.REVERSE_COMPLETED.longValue() == tdmModel.getSupProcessingStatusId()) {
                    manualReversalVO.setComments("1");
                    return manualReversalVO;
                } else {
                    List<BbStatementAllViewModel> entryList = manualReversalVO.getFundTransferEntryList();
                    if (entryList == null || entryList.size() == 0)
                        return manualReversalVO;
                    for (BbStatementAllViewModel entry : entryList) {
                        BBAccountsViewModel model = new BBAccountsViewModel();
                        model.setAccountNumber(com.inov8.ola.util.EncryptionUtil.encryptWithDES(entry.getAccountNumber()));
                        model = manualAdjustmentManager.getBBAccountsViewModel(model);
                        entry.setAmount(parseAmount(entry.getAmountStr()));
                        fmtAmount = formatAmount(entry.getAmount());
                        entry.setAmount(fmtAmount);
                        if (entry.getTransactionType().longValue() == TransactionTypeConstants.DEBIT) {
                            totalDebit += fmtAmount;
                        } else {
                            totalCredit += fmtAmount;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (totalDebit == 0) {
                logger.error("Total debit amount is 0");
                return manualReversalVO;
            } else if (totalCredit == 0) {
                logger.error("Total credit amount is 0");
                return manualReversalVO;
            } else if (formatAmount(totalDebit).longValue() != formatAmount(totalCredit).longValue()) {
                logger.error("Total debit and credit amount is not equal... Total Debit:" + totalDebit + " Total Credit:" + totalCredit);
                return manualReversalVO;
            }
            manualReversalVO.setTotalAmount(totalDebit);
        }
        return manualReversalVO;
    }

    @Override
    public MiddlewareMessageVO cashWithDrawalReversal(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException {
        FonePayLogModel fonePayLogModel = null;
        try {

            String requestType = null;
            Long productId = middlewareMessageVO.getProductId();
            String codeId = middlewareMessageVO.getReserved1();
            Long transactionCodeId = null;
            TransactionCodeModel transactionCodeModel = null;
            TransactionDetailMasterModel transactionDetailMasterModel = null;
            PostedTransactionReportModel postedTransactionReportModel = null;
            if (codeId != null) {
                requestType = "Transaction Reversal";
                ActionLogModel actionLogModel = new ActionLogModel();
//                actionLogBeforeStart(middlewareMessageVO, actionLogModel, false);
//                fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModelForDebitCardReq(middlewareMessageVO, requestType);
            } else if (codeId == null) {
                if (middlewareMessageVO.getMerchantType().equals("0000"))
                    productId = ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_ON_US;
                else if (middlewareMessageVO.getMerchantType().equals("0003"))
                    productId = ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US;
                else
                    productId = ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL;
                requestType = "Debit Card Cash WithDrawl Reversal";
                ActionLogModel actionLogModel = new ActionLogModel();
//                actionLogBeforeStart(middlewareMessageVO, actionLogModel, false);
//                fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModelForDebitCardReq(middlewareMessageVO, requestType);
//                DebitCardModel debitCardModel = getCommonCommandManager().getDebitCardModelDao().getDebitCardModelByCardNumber(middlewareMessageVO.getPAN());
//                DebitCardUtill.verifyDebitCard(new WebServiceVO(), debitCardModel);
                Long[] productIds = new Long[]{ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US,
                        ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_ON_US,
                        ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL,
                        ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL,
                        ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US
                ,ProductConstantsInterface.DEBIT_CARD_LESS_CASH_WITHDRAWAL};
//                AppUserModel appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(debitCardModel.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
//                UserDeviceAccountsModel uda = userDeviceAccountsDAO.findUserDeviceAccountByAppUserId(appUserModel.getAppUserId());
//                postedTransactionReportModel = postedTransactionReportDAO.getTransactionCodeIdForReversalByStanAndUserId(middlewareMessageVO, productIds, uda.getUserId());
                if (postedTransactionReportModel != null)
                    transactionCodeId = postedTransactionReportModel.getTransactionCodeId();
                DebitCardReversalVO debitCardReversalVO = new DebitCardReversalVO();

                debitCardReversalVO.setCardPan(middlewareMessageVO.getPAN());
                debitCardReversalVO.setOriginalStan(middlewareMessageVO.getOrignalStan());
                debitCardReversalVO.setReversalStan(middlewareMessageVO.getReversalSTAN());
                debitCardReversalVO.setRrn(middlewareMessageVO.getRetrievalReferenceNumber());
                debitCardReversalVO.setReversalAmount(Double.valueOf(middlewareMessageVO.getTransactionAmount()));
                debitCardReversalVO.setRetryCount(0L);
                debitCardReversalVO.setTransactionCodeId(transactionCodeId);
                debitCardReversalVO.setReversalRequestTime(middlewareMessageVO.getOrignalTransactionDateTime());
                debitCardReversalVO.setAdviceType(CoreAdviceUtil.ADVICE_TYPE_REVERSAL);
                debitCardReversalVO.setProductId(productId);
                this.loadAndForwardCoreAdviceToQueue(debitCardReversalVO);
                logger.info("Transaction with STAN: " + middlewareMessageVO.getOrignalStan() + " is saved and will be reversed Via Queue.");
                middlewareMessageVO.setResponseDescription("Transaction Reversal Request saved and will be reversed Via Queue.");
                return middlewareMessageVO;
            }
            if (codeId != null && !codeId.equals(""))
                transactionCodeId = Long.valueOf(codeId);
            if (transactionCodeId != null && !transactionCodeId.equals(""))
                transactionCodeModel = transactionCodeDAO.findByPrimaryKey(transactionCodeId);
            if (transactionCodeModel != null) {
                transactionDetailMasterModel = this.getTransactionReversalManager().loadTDMForReversalByTransactionCode(String.valueOf(transactionCodeModel.getCode()));
                String trxCode = transactionCodeModel.getCode();
                ManualReversalVO manualReversalVO = new ManualReversalVO();
                manualReversalVO.setInitiatorAppUserId(UserUtils.getCurrentUser().getAppUserId());
                manualReversalVO.setTransactionCode(trxCode);
                manualReversalVO.setAdjustmentType(1);
                manualReversalVO = validateManualReversalForm(manualReversalVO);
                if (manualReversalVO.getComments() != null && manualReversalVO.getComments().equalsIgnoreCase("No")) {
                    logger.info("No transaction Found against the STAN: " + middlewareMessageVO.getOrignalStan());
                    middlewareMessageVO.setResponseDescription("No transaction Found against the STAN: " + middlewareMessageVO.getOrignalStan());
                } else if (manualReversalVO.getComments() != null && manualReversalVO.getComments().equalsIgnoreCase("1")) {
                    logger.info("Transaction with STAN: " + middlewareMessageVO.getOrignalStan() + " is already Reversed.");
                    middlewareMessageVO.setResponseDescription("Transaction is already Reversed.");
                } else {
                    if (postedTransactionReportModel != null)
                        manualReversalVO.setProductId(postedTransactionReportModel.getProductId());
                    else
                        manualReversalVO.setProductId(productId);
                    manualReversalFacade.makeReversal(manualReversalVO);
//					middlewareMessageVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    String smsText = MessageUtil.getMessage("REVERSAL_CUSTOMER_WITHDRAWAL_DEBIT_CARD", new String[]{
                            String.valueOf(manualReversalVO.getTransactionAmount()),
                            transactionDetailMasterModel.getRecipientMobileNo(),
                            transactionDetailMasterModel.getTransactionCode(),});
                    SmsMessage smsMessage = new SmsMessage(transactionDetailMasterModel.getRecipientMobileNo(), smsText);
                    smsSender.send(smsMessage);
                    middlewareMessageVO.setResponseDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                    if (productId.equals(ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL)) {
                        I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                        requestVO = new I8SBSwitchControllerRequestVO();
                        responseVO = new I8SBSwitchControllerResponseVO();
                        requestVO = ESBAdapter.prepareMerchantCampingRequest(I8SBConstants.RequestType_TransactionStatus);
                        requestVO.setUserId(String.valueOf(manualReversalVO.getInitiatorAppUserId()));
                        requestVO.setTransactionCode(manualReversalVO.getTransactionCode());
                        requestVO.setAvailableBalance("");
                        requestVO.setMobileNumber(middlewareMessageVO.getMobileNo());
                        requestVO.setTransactionDate(String.valueOf(new Date()));
                        requestVO.setRRN(middlewareMessageVO.getOrignalStan());
                        requestVO.setSTAN(middlewareMessageVO.getStan());
                        requestVO.setStatus("R");
                        requestVO.setTransactionAmount("1");
                        requestVO.setTransactionType("C");
                        SwitchWrapper sWrapper = new SwitchWrapperImpl();
                        sWrapper = new SwitchWrapperImpl();
                        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                        sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                        ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
//                        middlewareMessageVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
//                        middlewareMessageVO.setResponseDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
//                        middlewareMessageVO.setResponseContentXML(response);
                    }
                }
            } else {
                logger.info("No transaction Found against the STAN: " + middlewareMessageVO.getOrignalStan() + " for PAN() " + middlewareMessageVO.getPAN());
                middlewareMessageVO.setResponseDescription("No transaction Found against the STAN: " + middlewareMessageVO.getOrignalStan());
            }

        } catch (Exception e) {
            logger.error("Error in Debit Card CashWithdrawal Reversal Issue :: " + e.getMessage() + " :: " + e);
        } finally {
            middlewareMessageVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
        }
        return middlewareMessageVO;
    }

    @Override
    public MiddlewareMessageVO posTransaction(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException {

        return debitCardCashWithDrawlTransaction(middlewareMessageVO, Boolean.TRUE);
    }

    @Override
    public MiddlewareMessageVO posRefundTransaction(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException {
        FonePayLogModel fonePayLogModel = null;
        WebServiceVO webServiceVO = new WebServiceVO();
        DebitCardModel debitCardModel = null;
        try {
            ActionLogModel actionLogModel = new ActionLogModel();
            actionLogBeforeStart(middlewareMessageVO, actionLogModel, false);
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModelForDebitCardReq(middlewareMessageVO, FonePayConstants.REQ_POS_DEBIT_CARD_REFUND);
            debitCardModel = getCommonCommandManager().getDebitCardModelDao().getDebitCardModelByCardNumber(middlewareMessageVO.getPAN());
            DebitCardUtill.verifyDebitCard(webServiceVO, debitCardModel);
            if (webServiceVO.getResponseCode().equals("00")) {
                Boolean isBlackListed = getCommonCommandManager().isCnicBlacklisted(debitCardModel.getCnic());
                if (isBlackListed) {
                    middlewareMessageVO.setResponseCode(FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
                    FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
                    getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                    return middlewareMessageVO;
                }


                BaseWrapper baseWrapper = new BaseWrapperImpl();
                baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
                baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.POS_DEBIT_CARD_REFUND);
                baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, middlewareMessageVO.getTransactionAmount());
                baseWrapper.putObject(CommandFieldConstants.KEY_STAN, middlewareMessageVO.getStan());
                baseWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER, middlewareMessageVO.getPAN());
                baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, debitCardModel.getMobileNo());
                baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, middlewareMessageVO.getTerminalId());

                String responseXML = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_POS_REFUND);
                logger.info("POS Refund Response XML : " + responseXML);
                middlewareMessageVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                middlewareMessageVO.setResponseDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                middlewareMessageVO.setResponseContentXML(responseXML);

            } else {
                middlewareMessageVO.setResponseCode(webServiceVO.getResponseCode());
                FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, webServiceVO.getResponseCode());
                getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
                return middlewareMessageVO;
            }
        } catch (CommandException e) {
            logger.error(e);
        } catch (FrameworkCheckedException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.POS Refund] Error occured: " + e.getMessage(), e);
            middlewareMessageVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            middlewareMessageVO.setResponseDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                middlewareMessageVO = FonePayUtils.prepareErrorResponseForDebitCard(middlewareMessageVO, FonePayResponseCodes.GENERAL_ERROR.toString());

            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModelForDebitCard(fonePayLogModel, middlewareMessageVO);
        }

        this.logger.info("Debit Card Cash With Drawl => Response Code: " + middlewareMessageVO.getResponseCode() + ", Description: " +
                middlewareMessageVO.getResponseDescription() + ", Mobile No: " + middlewareMessageVO.getMobileNo() +
                ", PAN: " + middlewareMessageVO.getPAN() + ", Amount: " + middlewareMessageVO.getTransactionAmount());

        return middlewareMessageVO;
    }

    private MiddlewareMessageVO processErrorResponseCode(MiddlewareMessageVO messageVO, String responseCode) {
        logger.info("[IBFTSwitchController.processErrorResponseCode] ==> Resp Code:" + responseCode);

        messageVO.setResponseCode(responseCode);
        messageVO.setAccountTitle("x");
        messageVO.setAccountBalance("0.0");

        return messageVO;
    }

    private void actionLogBeforeStart(MiddlewareMessageVO integrationMessageVO, ActionLogModel actionLogModel, boolean isTitleFetch) {
        actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
        actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BANKING_MIDDLEWARE.longValue());
        actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));
        actionLogModel.setAppUserId(PortalConstants.SCHEDULER_APP_USER_ID);

        if (isTitleFetch) {
            actionLogModel.setCommandId(Long.valueOf(CommandFieldConstants.TITLE_FETCH_COMMAND));
            actionLogModel.setCustomField1(integrationMessageVO.getAccountNo1());
        } else {
            actionLogModel.setCommandId(Long.valueOf(CommandFieldConstants.CREDIT_ADVICE_COMMAND));
            actionLogModel.setCustomField1(integrationMessageVO.getAccountNo2());
        }

        actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);

        if (actionLogModel.getActionLogId() != null) {
            ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
        }
    }

    private void actionLogAfterEnd(ActionLogModel actionLogModel) {
        BaseWrapper baseWrapper = new BaseWrapperImpl();

        try {
            actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
            actionLogModel.setEndTime(new Timestamp(new java.util.Date().getTime()));

            baseWrapper.setBasePersistableModel(actionLogModel);

            this.getActionLogManager().createOrUpdateActionLog(baseWrapper);

        } catch (Exception e) {
            logger.error("IBFTSwitchController.actionLogAfterEnd - Error occured: ", e);
        }
    }

    private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel) {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(actionLogModel);
        try {
            baseWrapper = this.getActionLogManager().createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
            actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
        } catch (Exception ex) {
            logger.error("[IBFTSwitchController]Exception occurred while insertActionLogRequiresNewTransaction() ", ex);
        }
        return actionLogModel;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public TransactionReversalManager getTransactionReversalManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (TransactionReversalManager) applicationContext.getBean("transactionReversalManager");
    }

    public IBFTIncomingRequestQueue getIBFTIncomingRequestQueue() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (IBFTIncomingRequestQueue) applicationContext.getBean("iBFTIncomingRequestQueue");
    }

    public WalletIncomingRequestQueue getWalletIncomingRequestQueue() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (WalletIncomingRequestQueue) applicationContext.getBean("walletIncomingRequestQueue");
    }

    public CommandManager getCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommandManager) applicationContext.getBean("cmdManager");
    }

    public DeviceTypeCommandManager getDeviceTypeCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (DeviceTypeCommandManager) applicationContext.getBean("deviceTypeCommandManager");
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public AllPayWebResponseDataPopulator getAllPayWebResponseDataPopulator() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (AllPayWebResponseDataPopulator) applicationContext.getBean("allPayWebResponseDataPopulator");
    }

    public ActionLogManager getActionLogManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (ActionLogManager) applicationContext.getBean("actionLogManager");
    }

    public void setFonePaySwitchController(FonePaySwitchController fonePaySwitchController) {
        this.fonePaySwitchController = fonePaySwitchController;
    }

    public void setManualReversalFacade(ManualReversalManager manualReversalFacade) {
        this.manualReversalFacade = manualReversalFacade;
    }

    public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
        this.transactionDetailMasterManager = transactionDetailMasterManager;
    }

    public void setCommissionTransactionViewFacade(CommissionTransactionViewFacade commissionTransactionViewFacade) {
        this.commissionTransactionViewFacade = commissionTransactionViewFacade;
    }

    public void setPortalOlaFacade(PortalOlaFacade portalOlaFacade) {
        this.portalOlaFacade = portalOlaFacade;
    }

    public void setManualAdjustmentManager(ManualAdjustmentManager manualAdjustmentManager) {
        this.manualAdjustmentManager = manualAdjustmentManager;
    }

    public void setPostedTransactionReportDAO(PostedTransactionReportDAO postedTransactionReportDAO) {
        this.postedTransactionReportDAO = postedTransactionReportDAO;
    }

    public void setTransactionCodeDAO(TransactionCodeDAO transactionCodeDAO) {
        this.transactionCodeDAO = transactionCodeDAO;
    }

    public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
        this.userDeviceAccountsDAO = userDeviceAccountsDAO;
    }

    public void setTransactionReversalDAO(TransactionReversalDAO transactionReversalDAO) {
        this.transactionReversalDAO = transactionReversalDAO;
    }

    public void setReversalAdviceQueingPreProcessor(ReversalAdviceQueingPreProcessor reversalAdviceQueingPreProcessor) {
        this.reversalAdviceQueingPreProcessor = reversalAdviceQueingPreProcessor;
    }

    public void setIbftRetryAdviceDAO(IBFTRetryAdviceDAO ibftRetryAdviceDAO) {
        this.ibftRetryAdviceDAO = ibftRetryAdviceDAO;
    }

    public void setIbftStatusHibernateDAO(IBFTStatusHibernateDAO ibftStatusHibernateDAO) {
        this.ibftStatusHibernateDAO = ibftStatusHibernateDAO;
    }

    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
}
