package com.inov8.microbank.server.service.transactionreversal;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.integration.controller.IBFTSwitchController;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.transactionreversal.TransactionReversalVo;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.debitcard.vo.DebitCardReversalVO;
import com.inov8.microbank.hra.paymtnc.dao.PayMtncRequestDAO;
import com.inov8.microbank.hra.paymtnc.model.PayMtncRequestModel;
import com.inov8.microbank.server.dao.messagingmodule.*;
import com.inov8.microbank.server.dao.safrepo.SafRepoCoreDao;
import com.inov8.microbank.server.dao.thirdpartcashoutmodule.ThirdPartyRetryAdviceDao;
import com.inov8.microbank.server.dao.transactionmodule.MiniTransactionDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionReversalDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.olamodule.OLAManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.web.context.ContextLoader;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 8, 2012 10:27:43 AM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class TransactionReversalManagerImpl extends ApplicationObjectSupport implements TransactionReversalManager {
    private static Logger logger = Logger.getLogger(TransactionReversalManagerImpl.class);

    private CommissionManager commissionManager;
    private TransactionModuleManager transactionManager;
    private ActionLogManager actionLogManager;
    private SmsSender smsSender;
    private TransactionDetailMasterManager transactionDetailMasterManager;
    private CoreAdviceSender coreAdviceSender;
    private IBFTIncomingRequestQueue iBFTIncomingRequestQueue;
    private WalletIncomingRequestQueue walletIncomingRequestQueue;
//    private CreditPaymentRequestQueue creditPaymentRequestQueue;
//    private DebitPaymentRequestQueue debitPaymentRequestQueue;

    private MiddlewareRetryAdviceDAO middlewareRetryAdviceDAO;
    private IBFTRetryAdviceDAO ibftRetryAdviceDAO;
    private PayMtncRequestDAO payMtncRequestDAO;
    private IBFTRetryAdviceListViewDAO iBFTRetryAdviceListViewDAO;
    private RetryAdviceListSummaryViewDAO retryAdviceListSummaryViewDAO;
    private RetryAdviceListHistoryViewDAO retryAdviceListHistoryViewDAO;
    private OLAManager olaManager;
    private RetryCreditQueueViewDAO retryCreditQueueViewDAO;
    private AccountCreditQueueLogDAO accountCreditQueueLogDAO;
    private MiniTransactionDAO miniTransactionDAO;
    private SafRepoCoreDao safRepoCoreDao;
    private FinancialIntegrationManager financialIntegrationManager;
    private SettlementManager settlementManager;
    private ESBAdapter esbAdapter;
    private ThirdPartyRetryAdviceDao thirdPartyRetryAdviceDAO;
    private TransactionReversalDAO transactionReversalDAO;
    private ReversalAdviceSender reversalAdviceSender;
    private GenericDao genericDao;

    private DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
    private DateTimeFormatter tf = DateTimeFormat.forPattern("h:mm a");

    public TransactionReversalVo findTransactionReversalDetail(Long transactionCodeId) throws FrameworkCheckedException {
        TransactionReversalVo txReversalVo = null;

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
        transactionCodeModel.setTransactionCodeId(transactionCodeId);
        searchBaseWrapper.setBasePersistableModel(transactionCodeModel);
        searchBaseWrapper = transactionManager.loadTransactionByTransactionCode(searchBaseWrapper);
        TransactionModel transaction = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
        String supProcessingStaus = transaction.getSupProcessingStatusIdSupplierProcessingStatusModel().getName();

        if (SupplierProcessingStatusConstants.REVERSED_NAME.equals(supProcessingStaus)) {
            txReversalVo = new TransactionReversalVo();
            txReversalVo.setUpdatedOn(transaction.getUpdatedOn());
            if (transaction.getUpdatedBy() == 2L) {
                txReversalVo.setUpdatedBy("Reversed By System");
            } else {
                AppUserModel txUpdatedByAppUserModel = transaction.getUpdatedByAppUserModel();
                if (txUpdatedByAppUserModel != null) {
                    txReversalVo.setUpdatedBy(txUpdatedByAppUserModel.getUsername());
                }
            }

            TransactionCodeModel txCodeModel = transaction.getTransactionCodeIdTransactionCodeModel();
            if (txCodeModel != null) {
                try {
                    List<MiniTransactionModel> list = (List<MiniTransactionModel>) txCodeModel.getTransactionCodeIdMiniTransactionModelList();
                    if (list != null && !list.isEmpty()) {
                        MiniTransactionModel miniTransactionModel = list.get(0);
                        txReversalVo.setComments(miniTransactionModel.getComments());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return txReversalVo;
    }

    public SwitchWrapper updateTransactionReversed(Long transactionCodeId, String comments) throws FrameworkCheckedException {
        //
        TransactionReversalVo vo = new TransactionReversalVo();
        vo.setTransactionCodeId(transactionCodeId);
        vo.setComments(comments);
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject("model", vo);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.KEY_SENDER_REDEEM_REVERSAL_USECASE_ID);
        //
        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null, baseWrapper, actionLogModel);
        //
        SearchBaseWrapper txCodeSearchBaseWrapper = new SearchBaseWrapperImpl();
        TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
        String smsText = "";
        String senderMobileNo = "";
        transactionCodeModel.setTransactionCodeId(transactionCodeId);
        txCodeSearchBaseWrapper.setBasePersistableModel(transactionCodeModel);
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();

        try {
            txCodeSearchBaseWrapper = transactionManager.loadTransactionByTransactionCode(txCodeSearchBaseWrapper);
            TransactionModel transactionModel = (TransactionModel) txCodeSearchBaseWrapper.getBasePersistableModel();
            actionLogModel.setCustomField11(transactionModel.getMfsId());
            TransactionCodeModel transactionCodeModel1 = transactionModel.getTransactionCodeIdTransactionCodeModel();
            actionLogModel.setTrxData(transactionCodeModel1.getCode() + "|||||||||||||");
            if (SupplierProcessingStatusConstants.REVERSED.longValue() == transactionModel.getSupProcessingStatusId()) {

                throw new FrameworkCheckedException("Transaction status is already reversed");

            } else if (SupplierProcessingStatusConstants.REVERSE_COMPLETED.longValue() == transactionModel.getSupProcessingStatusId()) {

                throw new FrameworkCheckedException("Transaction is already reversed completed");

            } else if (SupplierProcessingStatusConstants.IN_PROGRESS.longValue() != transactionModel.getSupProcessingStatusId()
                    && SupplierProcessingStatusConstants.UNCLAIMED.longValue() != transactionModel.getSupProcessingStatusId()
                    && SupplierProcessingStatusConstants.PENDING_ACTION_AUTH.longValue() != transactionModel.getSupProcessingStatusId()) {

                throw new FrameworkCheckedException("Invalid transaction status.");

            }

            Boolean isExpired = false;
            if (SupplierProcessingStatusConstants.UNCLAIMED.longValue() == transactionModel.getSupProcessingStatusId()) {
                isExpired = true;
            }

    		/*XStream xstream = new XStream();
    		String xml = xstream.toXML("");*/
            //actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml,XPathConstants.actionLogInputXMLLocationSteps));
            //this.actionLogBeforeStart(actionLogModel);

            // Update Transaction Model STATUS to Reversed
            SupplierProcessingStatusModel status = new SupplierProcessingStatusModel();
            status.setSupProcessingStatusId(SupplierProcessingStatusConstants.REVERSED);
            transactionModel.setSupProcessingStatusIdSupplierProcessingStatusModel(status);

            String firstName = UserUtils.getCurrentUser().getFirstName();
            String lastName = UserUtils.getCurrentUser().getLastName();
            lastName = ((StringUtil.isNullOrEmpty(lastName)) ? "" : " " + lastName);
            String appUserName = firstName + lastName;
            Long appUserId = UserUtils.getCurrentUser().getAppUserId();

            AppUserModel appUserModel = new AppUserModel();
            appUserModel.setAppUserId(appUserId);
            ThreadLocalAppUser.setAppUserModel(appUserModel);

            Date reversedOn = new Date();

            transactionModel.setUpdatedOn(reversedOn);
            transactionModel.setUpdatedBy(appUserId);

            baseWrapper.setBasePersistableModel(transactionModel);
            transactionManager.updateTransaction(baseWrapper);

            //Now Update TransactionDetailMaster for reporting....
            baseWrapper = new BaseWrapperImpl();
            TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel();
            transactionDetailMasterModel.setTransactionCodeId(transactionCodeId);
            baseWrapper.setBasePersistableModel(transactionDetailMasterModel);

            baseWrapper = transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
            transactionDetailMasterModel = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
            transactionDetailMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.REVERSED);
            transactionDetailMasterModel.setProcessingStatusName(SupplierProcessingStatusConstants.REVERSED_NAME);
            transactionDetailMasterModel.setReversalFlag(true);
            transactionDetailMasterModel.setFullReversalAllowed((isExpired) ? Boolean.FALSE : Boolean.TRUE);
            transactionDetailMasterModel.setReversedByAppUserId(appUserId);
            transactionDetailMasterModel.setReversedByName(appUserName);
            transactionDetailMasterModel.setReversedDate(reversedOn);
            transactionDetailMasterModel.setReversedComments(comments);
            transactionDetailMasterManager.saveTransactionDetailMaster(transactionDetailMasterModel);

            CommissionAmountsHolder commissionAmountsHolder = commissionManager.loadCommissionDetailsUnsettled(transactionDetailMasterModel.getTransactionDetailId());

            WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();
            wrapper.setTransactionDetailMasterModel(transactionDetailMasterModel);
            wrapper.setProductModel(new ProductModel(transactionDetailMasterModel.getProductId(), transactionDetailMasterModel.getProductName()));
            wrapper.setCommissionAmountsHolder(commissionAmountsHolder);
            TransactionCodeModel trxCodeModel = new TransactionCodeModel(transactionDetailMasterModel.getTransactionCode());
            transactionCodeModel.setTransactionCodeId(transactionDetailMasterModel.getTransactionCodeId());
            wrapper.setTransactionCodeModel(trxCodeModel);
            wrapper.setTransactionModel(transactionModel);

            switchWrapper.setWorkFlowWrapper(wrapper);
            switchWrapper.setBankId(transactionDetailMasterModel.getBankId());
            switchWrapper.setPaymentModeId(transactionDetailMasterModel.getPaymentModeId());

            switchWrapper.putObject("isExpired", isExpired);

            BaseWrapper bWrapper = new BaseWrapperImpl();
            BankModel bankModel = new BankModel();
            bankModel.setBankId(BankConstantsInterface.OLA_BANK_ID);
            bWrapper.setBasePersistableModel(bankModel);
            OLAVeriflyFinancialInstitutionImpl olaFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) this.financialIntegrationManager.loadFinancialInstitution(bWrapper);

            switchWrapper = olaFinancialInstitution.reverseTransaction(switchWrapper);
            wrapper.setOLASwitchWrapper(switchWrapper);

            settlementManager.prepareDataForDayEndSettlement(wrapper);

            senderMobileNo = transactionDetailMasterModel.getSaleMobileNo();
        	
            /*actionLogModel.setAppUserId( appUserId );
            actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath( xstream.toXML(""), XPathConstants.actionLogInputXMLLocationSteps) );
            this.actionLogAfterEnd(actionLogModel);*/
            actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null, baseWrapper, actionLogModel);

            String brandName = MessageUtil.getMessage("jsbl.brandName");

            smsText = getMessageSourceAccessor().getMessage("REDEMPTION.REQUEST.NOTIFICATION",
                    new Object[]{brandName, transactionDetailMasterModel.getTransactionCode()});

            SmsMessage message = new SmsMessage(senderMobileNo, smsText);
            smsSender.send(message);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new FrameworkCheckedException(MessageUtil.getMessage(WorkFlowErrorCodeConstants.GENERAL_ERROR));
        }

        return switchWrapper;
    }

    public WorkFlowWrapper updateTransactionRedeemed(TransactionReversalVo txReversalVo) throws FrameworkCheckedException {
        WorkFlowWrapper wrapper = new WorkFlowWrapperImpl();
        Long transactionCodeId = txReversalVo.getTransactionCodeId();
        Integer redemptionType = txReversalVo.getRedemptionType();
        SearchBaseWrapper txCodeSearchBaseWrapper = new SearchBaseWrapperImpl();
        TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
        String smsText = "";
        String senderMobileNo = "";
        transactionCodeModel.setTransactionCodeId(transactionCodeId);
        txCodeSearchBaseWrapper.setBasePersistableModel(transactionCodeModel);
        txCodeSearchBaseWrapper = transactionManager.loadTransactionByTransactionCode(txCodeSearchBaseWrapper);
        TransactionModel transaction = (TransactionModel) txCodeSearchBaseWrapper.getBasePersistableModel();

        Double txAmount = transaction.getTransactionAmount();

        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setTrxData(txReversalVo.getTransactionCode() + "|||||||||||||");
        actionLogModel.setUsecaseId(PortalConstants.KEY_SENDER_REDEEM_TRX_USECASE_ID);
        actionLogModel.setUserName(UserUtils.getCurrentUser().getUsername());
        XStream xstream = new XStream();
        try {
            xstream.toXML(new String("dumy"));
            actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.WEB);
            actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(new String("dumy")),
                    XPathConstants.actionLogInputXMLLocationSteps));
            this.actionLogBeforeStart(actionLogModel);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        try {

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel();
            transactionDetailMasterModel.setTransactionCodeId(transactionCodeId);
            baseWrapper.setBasePersistableModel(transactionDetailMasterModel);

            baseWrapper = transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
            transactionDetailMasterModel = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
            transactionDetailMasterModel.setUpdatedOn(new Date());
            transactionDetailMasterModel.setRedemptionType(redemptionType);

            transactionDetailMasterManager.saveTransactionDetailMaster(transactionDetailMasterModel);

            senderMobileNo = transactionDetailMasterModel.getSaleMobileNo();

            Date updatedOn = new Date();
            Long appUserId = UserUtils.getCurrentUser().getAppUserId();
            TransactionCodeModel txCodeModel = transaction.getTransactionCodeIdTransactionCodeModel();
            if (txCodeModel != null) {
                List<MiniTransactionModel> list = (List<MiniTransactionModel>) txCodeModel.getTransactionCodeIdMiniTransactionModelList();
                if (list != null && !list.isEmpty()) {
                    Iterator<MiniTransactionModel> it = list.iterator();
                    while (it.hasNext()) {
                        MiniTransactionModel miniTransactionModel = it.next();
                        if (miniTransactionModel.getMiniTransactionStateId() == MiniTransactionStateConstant.PIN_SENT.longValue()) {
                            miniTransactionModel.setComments(txReversalVo.getComments());
                            miniTransactionModel.setUpdatedOn(updatedOn);
                            miniTransactionModel.setUpdatedBy(appUserId);
                            miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.EXPIRED);
                            this.miniTransactionDAO.saveOrUpdate(miniTransactionModel);
                        }
                    }
                }
            }

            //generate 5 digit PIN and save it in MiniTransactionModel and send SMS to Sender
            MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
            String originalPin = "";
            String encryptedPin = "";
            originalPin = CommonUtils.generateOneTimePin(5);//RandomUtils.generateRandom(4, false, true);
            encryptedPin = EncoderUtils.encodeToSha(originalPin);
            //this will be only cash to cash
            miniTransactionModel.setCommandId(Long.valueOf(CommandFieldConstants.CMD_CASH_TO_CASH_INFO));

            miniTransactionModel.setMobileNo(transaction.getNotificationMobileNo());
            miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
            miniTransactionModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
            miniTransactionModel.setOneTimePin(encryptedPin);
            miniTransactionModel.setTAMT(txAmount);
            miniTransactionModel.setTransactionCodeId(transaction.getTransactionCodeId());
            miniTransactionModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
            miniTransactionModel.setCreatedOn(new Date());
            miniTransactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            miniTransactionModel.setUpdatedOn(new Date());
            miniTransactionModel.setTimeDate(new Date());
            miniTransactionModel.setAppUserId(appUserId);

            String brandName = MessageUtil.getMessage("jsbl.brandName");
            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
            DateTimeFormatter tf = DateTimeFormat.forPattern("h:mm a");

            String msgKey = "smsCommand.acc2cash.reverse";
            smsText = getMessageSourceAccessor().getMessage(
                    msgKey,
                    new Object[]{
                            brandName,
                            transaction.getTransactionCodeIdTransactionCodeModel().getCode(),
                            tf.print(new LocalTime()),
                            dtf.print(new DateTime()),});

            String msgTxt = getMessageSourceAccessor().getMessage(
                    msgKey,
                    new Object[]{
                            brandName,
                            transaction.getTransactionCodeIdTransactionCodeModel().getCode(),
                            tf.print(new LocalTime()),
                            dtf.print(new DateTime()),
                    });

            miniTransactionModel.setSmsText(msgTxt);
            this.miniTransactionDAO.saveOrUpdate(miniTransactionModel);

            try {
                actionLogModel.setAppUserId(appUserId);
                actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(new String("dumy")), XPathConstants.actionLogInputXMLLocationSteps));
                this.actionLogAfterEnd(actionLogModel);
            } catch (Exception e) {
                throw new FrameworkCheckedException(e.getMessage(), e);
            }
        } catch (FrameworkCheckedException e) {
            throw new FrameworkCheckedException(e.getMessage(), e);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage(), e);
        }

        ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(3);
        SmsMessage message = new SmsMessage(senderMobileNo, smsText);
        messageList.add(message);
        wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);

        return wrapper;
    }

    public boolean updateTransaction(Long transactionCodeId, String comments) throws FrameworkCheckedException {
        boolean isReversed = false;
        SearchBaseWrapper txCodeSearchBaseWrapper = new SearchBaseWrapperImpl();
        TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
        String smsText = "";
        transactionCodeModel.setTransactionCodeId(transactionCodeId);
        txCodeSearchBaseWrapper.setBasePersistableModel(transactionCodeModel);
        txCodeSearchBaseWrapper = transactionManager.loadTransactionByTransactionCode(txCodeSearchBaseWrapper);
        TransactionModel transaction = (TransactionModel) txCodeSearchBaseWrapper.getBasePersistableModel();

        Double txAmount = transaction.getTransactionAmount();
        List<TransactionDetailModel> txdetails = (List<TransactionDetailModel>) transaction.getTransactionIdTransactionDetailModelList();
        if (txdetails != null && !txdetails.isEmpty()) {
            TransactionDetailModel transactionDetailModel = txdetails.get(0);
            // Calculate effective transaction amount Tx_effective = Tx -
            // Fees + (agent2_fee + franchise2_fee)(modified by mudassir)
            // txAmount = txAmount - transaction.getTotalCommissionAmount();
            CommissionTransactionModel agentCommissoinTxModel = new CommissionTransactionModel();
            agentCommissoinTxModel.setCommissionStakeholderId(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID);
            agentCommissoinTxModel.setTransactionDetailId(transactionDetailModel.getTransactionDetailId());

            SearchBaseWrapper commissionTxSearchBaseWrapper = new SearchBaseWrapperImpl();
            commissionTxSearchBaseWrapper.setBasePersistableModel(agentCommissoinTxModel);
            commissionTxSearchBaseWrapper = commissionManager.getCommissionTransactionModel(commissionTxSearchBaseWrapper);

            agentCommissoinTxModel = (CommissionTransactionModel) commissionTxSearchBaseWrapper.getBasePersistableModel();
            double agentPendingCommission = 0d;
            if (agentCommissoinTxModel != null && agentCommissoinTxModel.getCommissionAmount() != null) {
                agentPendingCommission = agentCommissoinTxModel.getCommissionAmount();
            }

            /*Now Find Franchise 2 commission and add it in tx amount*/
            CommissionTransactionModel franchise2CommissoinTxModel = new CommissionTransactionModel();
            franchise2CommissoinTxModel.setCommissionStakeholderId(CommissionConstantsInterface.FRANCHISE2_STAKE_HOLDER_ID);
            franchise2CommissoinTxModel.setTransactionDetailId(transactionDetailModel.getTransactionDetailId());

            SearchBaseWrapper franchise2CommissionTxSearchBaseWrapper = new SearchBaseWrapperImpl();
            franchise2CommissionTxSearchBaseWrapper.setBasePersistableModel(franchise2CommissoinTxModel);
            franchise2CommissionTxSearchBaseWrapper = commissionManager.getCommissionTransactionModel(franchise2CommissionTxSearchBaseWrapper);

            franchise2CommissoinTxModel = (CommissionTransactionModel) franchise2CommissionTxSearchBaseWrapper.getBasePersistableModel();

            double franchise2PendingCommission = 0D;
            if (franchise2CommissoinTxModel != null && franchise2CommissoinTxModel.getCommissionAmount() != null) {

                franchise2PendingCommission = franchise2CommissoinTxModel.getCommissionAmount();

            }


            if (agentPendingCommission > 0 || franchise2PendingCommission > 0) {
                txAmount = txAmount + agentPendingCommission + franchise2PendingCommission;
            }
        }

        // Reverse Customer Transaction
        ActionLogModel actionLogModel = new ActionLogModel();
        XStream xstream = new XStream();
        try {
            xstream.toXML(new String("dumy"));
            actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.WEB);
            actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(new String("dumy")),
                    XPathConstants.actionLogInputXMLLocationSteps));
            this.actionLogBeforeStart(actionLogModel);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        try {
            // Update Transaction Model STATUS to Reversed
            SupplierProcessingStatusModel status = new SupplierProcessingStatusModel();
            status.setSupProcessingStatusId(SupplierProcessingStatusConstants.REVERSED);
            transaction.setSupProcessingStatusIdSupplierProcessingStatusModel(status);
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            Date updatedOn = new Date();
            Long appUserId = UserUtils.getCurrentUser().getAppUserId();
            transaction.setUpdatedOn(updatedOn);
            transaction.setUpdatedBy(appUserId);

            baseWrapper.setBasePersistableModel(transaction);

            TransactionCodeModel txCodeModel = transaction.getTransactionCodeIdTransactionCodeModel();
            if (txCodeModel != null) {
                List<MiniTransactionModel> list = (List<MiniTransactionModel>) txCodeModel.getTransactionCodeIdMiniTransactionModelList();
                if (list != null && !list.isEmpty()) {
                    MiniTransactionModel miniTransactionModel = list.get(0);
                    miniTransactionModel.setComments(comments);
                    miniTransactionModel.setUpdatedOn(updatedOn);
                    miniTransactionModel.setUpdatedBy(appUserId);
                    miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.EXPIRED);
                }
            }
            transactionManager.updateTransaction(baseWrapper);

            //Now Update TransactionDetailMaster for reporting....
            List<Long> reversedTransactionIds = new ArrayList<Long>();
            reversedTransactionIds.add(transaction.getTransactionId());

            try {
                transactionDetailMasterManager.updateTransactionProcessingStatus(reversedTransactionIds, SupplierProcessingStatusConstants.REVERSED, SupplierProcessingStatusConstants.REVERSED_NAME);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            try {
                actionLogModel.setAppUserId(appUserId);
                actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xstream.toXML(new String("dumy")), XPathConstants.actionLogInputXMLLocationSteps));
                this.actionLogAfterEnd(actionLogModel);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            //generate 5 digit PIN and save it in MiniTransactionModel and send SMS to Sender
            MiniTransactionModel miniTransactionModel = new MiniTransactionModel();
            String originalPin = "";
            String encryptedPin = "";
            originalPin = CommonUtils.generateOneTimePin(5);//RandomUtils.generateRandom(4, false, true);
            encryptedPin = EncoderUtils.encodeToSha(originalPin);
            //this will be only cash to cash
            miniTransactionModel.setCommandId(Long.valueOf(CommandFieldConstants.CMD_CASH_TO_CASH_INFO));

            miniTransactionModel.setMobileNo(transaction.getNotificationMobileNo());
            miniTransactionModel.setMiniTransactionStateId(MiniTransactionStateConstant.PIN_SENT);
            miniTransactionModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
            miniTransactionModel.setOneTimePin(encryptedPin);
            miniTransactionModel.setTAMT(txAmount);
            miniTransactionModel.setTransactionCodeId(transaction.getTransactionCodeId());
            miniTransactionModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
            miniTransactionModel.setCreatedOn(new Date());
            miniTransactionModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            miniTransactionModel.setUpdatedOn(new Date());
            miniTransactionModel.setTimeDate(new Date());
            miniTransactionModel.setAppUserId(appUserId);

            String brandName = MessageUtil.getMessage("jsbl.brandName");
            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
            DateTimeFormatter tf = DateTimeFormat.forPattern("h:mm a");

            String msgKey = "smsCommand.acc2cash.reverse";
            smsText = getMessageSourceAccessor().getMessage(
                    msgKey,
                    new Object[]{
                            brandName,
                            transaction.getTransactionCodeIdTransactionCodeModel().getCode(),
                            originalPin,
                            dtf.print(new DateTime()),
                            tf.print(new LocalTime()),});

            String msgTxt = getMessageSourceAccessor().getMessage(
                    msgKey,
                    new Object[]{
                            brandName,
                            transaction.getTransactionCodeIdTransactionCodeModel().getCode(),
                            "*****",
                            dtf.print(new DateTime()),
                            tf.print(new LocalTime()),});

            miniTransactionModel.setSmsText(msgTxt);
            this.miniTransactionDAO.saveOrUpdate(miniTransactionModel);
            isReversed = true;
        } catch (FrameworkCheckedException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        SmsMessage message = new SmsMessage(transaction.getNotificationMobileNo(), smsText);

        try {
            smsSender.send(message);
        } catch (FrameworkCheckedException e) {
            logger.error(e.getMessage(), e);
        }
        return isReversed;
    }

    private void actionLogBeforeStart(ActionLogModel actionLogModel) {
        actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
        actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));
        actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
        if (actionLogModel.getActionLogId() != null) {
            ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
        }
    }

    private void actionLogAfterEnd(ActionLogModel actionLogModel) {
        actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
        actionLogModel.setEndTime(new Timestamp(new java.util.Date().getTime()));
        insertActionLogRequiresNewTransaction(actionLogModel);
    }

    private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel) {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(actionLogModel);
        try {
            baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
            actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
        } catch (Exception ex) {
            logger.error("Exception occurred while processing", ex);
        }
        return actionLogModel;
    }

    private Double roundTwoDecimals(Double value) {
        Double roundedValue = new Double(0.0);
        if (value != null) {
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            roundedValue = Double.valueOf(twoDForm.format(value));
        }

        return roundedValue;
    }


    public void sendCoreReversalRequiresNewTransaction(MiddlewareAdviceVO middlewareAdviceVO) throws FrameworkCheckedException {
        saveSafRepoCore(middlewareAdviceVO);
    }

    public void makeCoreCreditAdvice(MiddlewareAdviceVO middlewareAdviceVO) throws FrameworkCheckedException {
        saveSafRepoCore(middlewareAdviceVO);
    }

    private void saveSafRepoCore(MiddlewareAdviceVO adviceVO) throws FrameworkCheckedException {

        try {
            SafRepoCoreModel safRepoCoreModel = new SafRepoCoreModel();
            //
            safRepoCoreModel.setSenderBankImd(adviceVO.getSenderBankImd());
            safRepoCoreModel.setSenderIBAN(adviceVO.getSenderIBAN());
            safRepoCoreModel.setSenderName(adviceVO.getSenderName());
            safRepoCoreModel.setBeneAccountTitle(adviceVO.getAccountTitle());
            safRepoCoreModel.setBeneBankImd(adviceVO.getBankIMD());
            safRepoCoreModel.setBeneBankName(adviceVO.getBeneBankName());
            safRepoCoreModel.setBeneBranchName(adviceVO.getBeneBranchName());
            safRepoCoreModel.setBeneIBAN(adviceVO.getBeneIBAN());
            safRepoCoreModel.setAgentId(adviceVO.getAgentId());
            safRepoCoreModel.setCrDr(adviceVO.getCrDr());
            safRepoCoreModel.setCardAcceptorNameAndLocation(adviceVO.getCardAcceptorNameAndLocation());
            safRepoCoreModel.setPurposeOfPayment(adviceVO.getPurposeOfPayment());
            safRepoCoreModel.setTransactionCode(adviceVO.getMicrobankTransactionCode());
            //
            safRepoCoreModel.setProductId(adviceVO.getProductId());
            safRepoCoreModel.setIntgTransactionTypeId(adviceVO.getIntgTransactionTypeId());
            safRepoCoreModel.setTransactionCodeId(adviceVO.getMicrobankTransactionCodeId());
            safRepoCoreModel.setTransactionCode(adviceVO.getMicrobankTransactionCode());
            safRepoCoreModel.setFromAccount(adviceVO.getAccountNo1());
            safRepoCoreModel.setToAccount(adviceVO.getAccountNo2());
            if (adviceVO.getTransactionAmount() != null) {
                safRepoCoreModel.setTransactionAmount(Double.parseDouble(adviceVO.getTransactionAmount()));
            }
            safRepoCoreModel.setStan(adviceVO.getStan());
            safRepoCoreModel.setReversalStan(adviceVO.getReversalSTAN());
            safRepoCoreModel.setResponseCode(adviceVO.getResponseCode());
            safRepoCoreModel.setTransmissionTime(adviceVO.getTransmissionTime());
            safRepoCoreModel.setReversalRequestTime(adviceVO.getReversalRequestTime());
            safRepoCoreModel.setRequestTime(adviceVO.getRequestTime());
            safRepoCoreModel.setAdviceType(CoreAdviceUtil.getAdviceTypeName(adviceVO));
            safRepoCoreModel.setBillAggregator(adviceVO.getBillAggregator());
            safRepoCoreModel.setCnicNo(adviceVO.getCnicNo());
            safRepoCoreModel.setConsumerNo(adviceVO.getConsumerNo());
            safRepoCoreModel.setBillCategoryCode(adviceVO.getBillCategoryId());
            safRepoCoreModel.setCompnayCode(adviceVO.getCompnayCode());
            safRepoCoreModel.setTransactionId((Long) adviceVO.getDataMap().get("TRANSACTION_ID"));
            safRepoCoreModel.setActionLogId((Long) adviceVO.getDataMap().get("ACTION_LOG_ID"));
            safRepoCoreModel.setRetrievalReferenceNumber(adviceVO.getRetrievalReferenceNumber());
            safRepoCoreModel.setUbpBBStan(adviceVO.getUbpStan());
            //safRepoCoreModel.setRrnKey(adviceVO.getRrnKey());

            safRepoCoreModel.setStatus(PortalConstants.SAF_STATUS_INITIATED);

            if (ThreadLocalAppUser.getAppUserModel() != null || ThreadLocalAppUser.getAppUserModel().getAppUserId() != null) {
                safRepoCoreModel.setCreatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
            } else {
                safRepoCoreModel.setCreatedBy(2L);
            }
            safRepoCoreModel.setCreatedOn(new Date());
            safRepoCoreModel.setUpdatedOn(new Date());

            logger.info("[AccountManagerImpl.saveSafRepoCore] saving SafRepoCore with Transaction Code: " + adviceVO.getMicrobankTransactionCode());
            this.safRepoCoreDao.saveOrUpdate(safRepoCoreModel);

        } catch (Exception e) {
            logger.error("[TransactionReversalManagerImpl.saveSafRepoCore] Exception occured on saving trx to SafRepoCoreModel. Transaction Code: " + adviceVO.getMicrobankTransactionCode(), e);
            throw new FrameworkCheckedException("Unable to save data in SAF Repo");
        }
    }


    /**
     * Added by atif hussain
     */
    @Override
    public SearchBaseWrapper findRetryAdviceSummaryListView(
            SearchBaseWrapper wrapper)
            throws FrameworkCheckedException {

        RetryAdviceListSummaryViewModel retryAdviceListSummaryViewModel = (RetryAdviceListSummaryViewModel) wrapper.getBasePersistableModel();

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);

        CustomList<RetryAdviceListSummaryViewModel> customList = retryAdviceListSummaryViewDAO.findByExample(
                retryAdviceListSummaryViewModel, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel(),
                exampleConfigHolderModel);

        wrapper.setCustomList(customList);

        return wrapper;
    }

    @Override
    public SearchBaseWrapper findRetryCreditQueueViewModel(SearchBaseWrapper wrapper) throws FrameworkCheckedException {

        RetryCreditQueueViewModel retryCreditQueueViewModel = (RetryCreditQueueViewModel) wrapper.getBasePersistableModel();

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);

        CustomList<RetryCreditQueueViewModel> customList =
                retryCreditQueueViewDAO.findByExample(retryCreditQueueViewModel, wrapper.getPagingHelperModel(),
                        wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel(), exampleConfigHolderModel);

        wrapper.setCustomList(customList);

        return wrapper;
    }


    @Override
    public AccountCreditQueueLogModel findAccountCreditQueueLogModel(AccountCreditQueueLogModel _accountCreditQueueLogModel) {

        AccountCreditQueueLogModel accountCreditQueueLogModel = accountCreditQueueLogDAO.findByPrimaryKey(_accountCreditQueueLogModel.getPrimaryKey());

        return accountCreditQueueLogModel;
    }


    @Override
    public void createSaveAccountCreditQueueLogModel(AccountCreditQueueLogModel accountCreditQueueLogModel) {

        accountCreditQueueLogDAO.saveOrUpdate(accountCreditQueueLogModel);
    }

    /**
     * Added by atif hussain
     */
    @Override
    public SearchBaseWrapper findRetryAdviceHistoryListView(
            SearchBaseWrapper wrapper)
            throws FrameworkCheckedException {

        RetryAdviceListHistoryViewModel retryAdviceListHistoryViewModel =
                (RetryAdviceListHistoryViewModel) wrapper.getBasePersistableModel();

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);

        CustomList<RetryAdviceListHistoryViewModel> customList = retryAdviceListHistoryViewDAO.
                findByExample(
                        retryAdviceListHistoryViewModel, wrapper.getPagingHelperModel(),
                        wrapper.getSortingOrderMap(), wrapper.getDateRangeHolderModel(),
                        exampleConfigHolderModel);

        wrapper.setCustomList(customList);

        return wrapper;
    }

    /**
     * Added by atif hussain
     */
    @Override
    public BaseWrapper loadRetryAdviceSummary(BaseWrapper baseWrapper)
            throws FrameworkCheckedException {

        MiddlewareRetryAdviceModel middlewareRetryAdviceModel = (MiddlewareRetryAdviceModel) baseWrapper.getBasePersistableModel();
        middlewareRetryAdviceModel = middlewareRetryAdviceDAO.findByPrimaryKey(middlewareRetryAdviceModel.getMiddlewareRetryAdviceId());
        baseWrapper.setBasePersistableModel(middlewareRetryAdviceModel);
        return baseWrapper;
    }

    /**
     * Added by atif hussain
     */
    @Override
    public BaseWrapper makeCoreRetryAdvice(BaseWrapper baseWrapper)
            throws FrameworkCheckedException {

        MiddlewareRetryAdviceModel model = (MiddlewareRetryAdviceModel) baseWrapper.getBasePersistableModel();

        AppUserModel appUserModel = this.getCommonCommandManager().loadAppUserByMobileAndType(model.getConsumerNo());
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

        if (appUserModel != null) {
            CustomerModel customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
            workFlowWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MODEL, customerModel);
        }

        workFlowWrapper.putObject(CommandFieldConstants.KEY_APP_USER_MODEL, appUserModel);

        if (model.getStatus() == null || !model.getStatus().equals("Failed")) {
            throw new FrameworkCheckedException("You cannot retry this advice.");
        }
        model = (MiddlewareRetryAdviceModel) baseWrapper.getBasePersistableModel();
        if (model.getProductId() == null || (model.getProductId() != null && model.getProductId().equals(ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US)
                || model.getProductId().equals(ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_ON_US)
                || model.getProductId().equals(ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL)
                || model.getProductId().equals(ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US)
                || model.getProductId().equals(ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL))) {
            DebitCardReversalVO debitCardReversalVO = new DebitCardReversalVO();
            debitCardReversalVO.setCardPan(model.getConsumerNo());
            debitCardReversalVO.setOriginalStan(model.getStan());
            debitCardReversalVO.setReversalStan(model.getReversalStan());
            debitCardReversalVO.setRrn(model.getRetrievalReferenceNumber());
            debitCardReversalVO.setReversalAmount(Double.valueOf(model.getTransactionAmount()));
            debitCardReversalVO.setRetryCount(0L);
            debitCardReversalVO.setReversalRequestTime(model.getRequestTime().toString());
            debitCardReversalVO.setAdviceType(CoreAdviceUtil.ADVICE_TYPE_REVERSAL);
            debitCardReversalVO.setTransactionCodeId(model.getTransactionCodeId());
            debitCardReversalVO.setTransactionCode(model.getTransactionCode());
            debitCardReversalVO.setProductId(model.getProductId());
            reversalAdviceSender.send(debitCardReversalVO);
        } else {
            MiddlewareAdviceVO middlewareAdviceVO = CoreAdviceUtil.prepareMiddlewareAdviceVO(model);
            if (model.getProductId() == null || (model.getProductId() != null && model.getProductId().equals(ProductConstantsInterface.ACCOUNT_OPENING)
                    || model.getProductId().equals(ProductConstantsInterface.CUST_ACCOUNT_OPENING)
                    || model.getProductId().equals(ProductConstantsInterface.PORTAL_ACCOUNT_OPENING))) {
                middlewareAdviceVO.setWorkFlowWrapper(workFlowWrapper);
            }
            coreAdviceSender.send(middlewareAdviceVO);
        }
        model.setStatus("Pushed to SAF");

        middlewareRetryAdviceDAO.saveOrUpdate(model);

        return baseWrapper;
    }

    @Override
    public BaseWrapper makeCoreRetryAdviceSkipped(BaseWrapper baseWrapper) throws FrameworkCheckedException {

        MiddlewareRetryAdviceModel model = (MiddlewareRetryAdviceModel) baseWrapper.getBasePersistableModel();

        if (model == null) {
            logger.error("MiddlewareRetryAdviceModel is null... Throwing Exception");
            throw new FrameworkCheckedException("Unable to change status to Skipped. Please retry");
        }
        if (model.getStatus() == null) {
            logger.error("MiddlewareRetryAdviceModel.status is null... Throwing Exception");
            throw new FrameworkCheckedException("Unable to change status to Skipped. Please retry");
        }
        if (!model.getStatus().equals("Failed")) {
            logger.error("MiddlewareRetryAdviceModel.status is " + model.getStatus() + "... which should be Failed. So throwing Exception...");
            throw new FrameworkCheckedException("Unable to change status to Skipped. Please retry");
        }

        model.setStatus("Skipped");
        model.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
        model.setUpdatedOn(new Date());

        middlewareRetryAdviceDAO.saveOrUpdate(model);
        return baseWrapper;
    }

    @Override
    public BaseWrapper makeIBFTRetryAdvice(Long ibftRetryAdviceId) throws FrameworkCheckedException {

        IBFTRetryAdviceModel model = ibftRetryAdviceDAO.findByPrimaryKey(ibftRetryAdviceId);
//        String names = MessageUtil.getMessage("Credit.Payment.advice.Product.Ids");
//        List<String> creditPaymentProductIds = Arrays.asList(names.split("\\s*,\\s*"));
//        String debitnames = MessageUtil.getMessage("debit.Payment.advice.Product.Ids");
//        List<String> debitPaymentProductIds = Arrays.asList(debitnames.split("\\s*,\\s*"));
        if (model.getStatus() == null) {
            throw new FrameworkCheckedException("You cannot retry this advice.");
        } else if (model.getStatus().equals(PortalConstants.IBFT_STATUS_IN_PROGRESS)) {
            throw new FrameworkCheckedException("Already pushed to SAF, you cannot retry this advice.");
        } else if (model.getStatus().equals(PortalConstants.IBFT_STATUS_SUCCESS)) {
            throw new FrameworkCheckedException("Already Successful, you cannot retry this advice.");
        }

        MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
        if (model.getProductId() != null && model.getProductId().equals(ProductConstantsInterface.AGENT_BB_TO_IBFT)) {
            middlewareAdviceVO.setAccountNo1(model.getMobileNo());
            middlewareAdviceVO.setAccountNo2(model.getAccountNo());
            IBFTRetryAdviceModel ibftRetryAdviceModel = new IBFTRetryAdviceModel();
            ibftRetryAdviceModel.setStan(model.getStan());
            SafRepoCoreModel safRepoCoreModel = new SafRepoCoreModel();
            safRepoCoreModel.setStan(model.getStan());
            safRepoCoreModel.setProductId(ProductConstantsInterface.AGENT_BB_TO_IBFT);
            List<SafRepoCoreModel> list = safRepoCoreDao.findByExample(safRepoCoreModel).getResultsetList();
            if (list != null && !list.isEmpty()) {
                SafRepoCoreModel coreModel = list.get(0);
                logger.info("Microbank Transaction Code in SAF_REPO_CORE in TransactionReversalManagerImpl.makeIBFTRetryAdvice():: " + coreModel.getTransactionCode());
                middlewareAdviceVO.setProductId(coreModel.getProductId());
                middlewareAdviceVO.setMicrobankTransactionCode(coreModel.getTransactionCode());
                middlewareAdviceVO.setAccountTitle(coreModel.getBeneAccountTitle());
                middlewareAdviceVO.setSenderIBAN(coreModel.getSenderIBAN());
                middlewareAdviceVO.setAccountNo1(coreModel.getFromAccount());
                middlewareAdviceVO.setSenderName(coreModel.getSenderName());
                middlewareAdviceVO.setCardAcceptorNameAndLocation(coreModel.getCardAcceptorNameAndLocation());
                middlewareAdviceVO.setSenderBankImd(coreModel.getSenderBankImd());
                middlewareAdviceVO.setAgentId(coreModel.getAgentId());
                middlewareAdviceVO.setAccountNo2(coreModel.getToAccount());
                middlewareAdviceVO.setPAN(coreModel.getCnicNo());
                middlewareAdviceVO.setBankIMD(coreModel.getBeneBankImd());
                middlewareAdviceVO.setPurposeOfPayment(coreModel.getPurposeOfPayment());
                middlewareAdviceVO.setCrDr(coreModel.getCrDr());
                middlewareAdviceVO.setBeneIBAN(coreModel.getBeneIBAN());
                middlewareAdviceVO.setBeneBankName(coreModel.getBeneBankName());
                middlewareAdviceVO.setBeneBranchName(coreModel.getBeneBranchName());
                middlewareAdviceVO.setIsCreditAdvice(Boolean.TRUE);
            }
        } else {
            middlewareAdviceVO.setAccountNo1(model.getMobileNo());
            middlewareAdviceVO.setAccountNo2(model.getAccountNo());
            middlewareAdviceVO.setProductId(model.getProductId());
        }
        middlewareAdviceVO.setTransactionAmount(model.getTransactionAmount().toString());
        middlewareAdviceVO.setRequestTime(model.getRequestTime());
        middlewareAdviceVO.setStan(model.getStan());
        middlewareAdviceVO.setRetrievalReferenceNumber(model.getRetrievalReferenceNumber());
        middlewareAdviceVO.setAdviceType(PortalConstants.IBFT_ADVICE_TYPE); // Used in DLQ

        logger.info("TransactionReversalManagerImpl.makeIBFTRetryAdvice. Account Number: " + middlewareAdviceVO.getAccountNo1() + ", Trx Amount: " + middlewareAdviceVO.getTransactionAmount());
        ActionLogModel actionLogModel = new ActionLogModel();
        try {
            this.actionLogBeforeStartIBFT(middlewareAdviceVO, actionLogModel);
            if (model.getProductId() != null && (model.getProductId().equals(ProductConstantsInterface.AGENT_BB_TO_IBFT) ||
                    model.getProductId().equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT))) {
                coreAdviceSender.send(middlewareAdviceVO);
            } else if (model.getProductId() != null && (model.getProductId().equals(ProductConstantsInterface.CORE_TO_WALLET))) {
                middlewareAdviceVO.setAccountNo1(model.getAccountNo());
                middlewareAdviceVO.setAccountNo2(model.getMobileNo());
                saveNewIBFTRecord(middlewareAdviceVO);
                this.walletIncomingRequestQueue.sentWalletRequest(middlewareAdviceVO);
            }
//            else if (model.getProductId()!=null&& creditPaymentProductIds.contains(model.getProductId().toString())){
//                middlewareAdviceVO.setAccountNo1(model.getAccountNo());
//                middlewareAdviceVO.setAccountNo2(model.getMobileNo());
//                saveNewIBFTRecord(middlewareAdviceVO);
//                this.creditPaymentRequestQueue.sentWalletRequest(middlewareAdviceVO);
//            }
//            else if (model.getProductId()!=null&& debitPaymentProductIds.contains(model.getProductId().toString())){
//                middlewareAdviceVO.setAccountNo1(model.getAccountNo());
//                middlewareAdviceVO.setAccountNo2(model.getMobileNo());
//                saveNewIBFTRecord(middlewareAdviceVO);
//                this.debitPaymentRequestQueue.sentWalletRequest(middlewareAdviceVO);
//            }
            else {
                saveNewIBFTRecord(middlewareAdviceVO);
                this.iBFTIncomingRequestQueue.sentIBFTRequest(middlewareAdviceVO);
            }

            this.actionLogAfterEndIBFT(actionLogModel);
        } catch (Exception e) {
            logger.error("TransactionReversalManagerImpl.makeIBFTRetryAdvice Error occured: ", e);
            throw e;
        }

        return new BaseWrapperImpl();
    }


    public void saveNewIBFTRecord(MiddlewareAdviceVO messageVO) throws FrameworkCheckedException {

        IBFTRetryAdviceModel model = new IBFTRetryAdviceModel();
        if (messageVO.getProductId() != null && (messageVO.getProductId().equals(ProductConstantsInterface.AGENT_BB_TO_IBFT)
                || messageVO.getProductId().equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT))) {
            model.setAccountNo(messageVO.getAccountNo2());
            model.setMobileNo(messageVO.getAccountNo1());
            model.setProductId(messageVO.getProductId());
            model.setTransactionCode(messageVO.getMicrobankTransactionCode());
        } else {
            model.setAccountNo(messageVO.getAccountNo1());
            model.setMobileNo(messageVO.getAccountNo2());
            model.setTransactionCode(messageVO.getMicrobankTransactionCode());
        }
        model.setTransactionAmount(Double.parseDouble(messageVO.getTransactionAmount()));
        model.setRequestTime(messageVO.getRequestTime());
        model.setStan(messageVO.getStan());
        model.setRetrievalReferenceNumber(messageVO.getRetrievalReferenceNumber());
        if (messageVO.getProductId() != null) {
            model.setProductId(messageVO.getProductId());
        }

        model.setCreatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
        model.setUpdatedBy(PortalConstants.SCHEDULER_APP_USER_ID);
        model.setCreatedOn(new Date());
        model.setUpdatedOn(new Date());
        model.setBankImd(messageVO.getBankIMD());
        model.setStatus(PortalConstants.IBFT_STATUS_IN_PROGRESS);

        ibftRetryAdviceDAO.saveOrUpdate(model);
    }


    private void actionLogBeforeStartIBFT(MiddlewareAdviceVO messageVO, ActionLogModel actionLogModel) {
        actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
        actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BANKING_MIDDLEWARE.longValue());
        actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));
        actionLogModel.setAppUserId(PortalConstants.SCHEDULER_APP_USER_ID);

        actionLogModel.setCommandId(Long.valueOf(CommandFieldConstants.CREDIT_ADVICE_COMMAND));
        actionLogModel.setCustomField1(messageVO.getAccountNo2());

        actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);

        if (actionLogModel.getActionLogId() != null) {
            ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
        }
    }

    private void actionLogAfterEndIBFT(ActionLogModel actionLogModel) {
        BaseWrapper baseWrapper = new BaseWrapperImpl();

        try {
            actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
            actionLogModel.setEndTime(new Timestamp(new java.util.Date().getTime()));

            baseWrapper.setBasePersistableModel(actionLogModel);

            this.actionLogManager.createOrUpdateActionLog(baseWrapper);

        } catch (Exception e) {
            logger.error("TransactionReversalManagerImpl.actionLogAfterEnd - Error occured: ", e);
        }
    }

    public void saveFailedAdviceRequiresNewTransaction(MiddlewareRetryAdviceModel model) throws FrameworkCheckedException {
        middlewareRetryAdviceDAO.saveOrUpdate(model);
    }

    public void saveFailedAdviceRequiresNewTransaction(ThirdPartyRetryAdviceModel model) throws FrameworkCheckedException {
        thirdPartyRetryAdviceDAO.saveOrUpdate(model);
    }

    public void updateRetryAdviceReportStatus(Long trxCodeId) throws FrameworkCheckedException {
        try {
            middlewareRetryAdviceDAO.updateRetryAdviceReportStatus(trxCodeId);
        } catch (Exception ex) {
            // Not Throwing Exception if Advice is successful but status is not updated.
            logger.error("Failed to updateRetryAdviceReportStatus to 'Successful' for  trxCodeId" + trxCodeId);
            logger.error(ex.getMessage(), ex);
        }
    }

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }

    public void setTransactionManager(TransactionModuleManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }

    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    public void setTransactionDetailMasterManager(
            TransactionDetailMasterManager transactionDetailMasterManager) {
        this.transactionDetailMasterManager = transactionDetailMasterManager;
    }

    public void setCoreAdviceSender(CoreAdviceSender coreAdviceSender) {
        this.coreAdviceSender = coreAdviceSender;
    }

    public void setMiddlewareRetryAdviceDAO(
            MiddlewareRetryAdviceDAO middlewareRetryAdviceDAO) {
        this.middlewareRetryAdviceDAO = middlewareRetryAdviceDAO;
    }

    public void setIbftRetryAdviceDAO(IBFTRetryAdviceDAO ibftRetryAdviceDAO) {
        this.ibftRetryAdviceDAO = ibftRetryAdviceDAO;
    }

    public void setRetryAdviceListSummaryViewDAO(
            RetryAdviceListSummaryViewDAO retryAdviceListSummaryViewDAO) {
        this.retryAdviceListSummaryViewDAO = retryAdviceListSummaryViewDAO;
    }

    public void setRetryAdviceListHistoryViewDAO(
            RetryAdviceListHistoryViewDAO retryAdviceListHistoryViewDAO) {
        this.retryAdviceListHistoryViewDAO = retryAdviceListHistoryViewDAO;
    }


    /*
     * get IBFT Credit advice request History (by STAN, Date and status)
     */
    public List<IBFTRetryAdviceModel> getIbftRetryAdviceList(String stan, Date requestTime, String status) throws FrameworkCheckedException {

        IBFTRetryAdviceModel iBFTRetryAdviceModel = new IBFTRetryAdviceModel();
        iBFTRetryAdviceModel.setStan(stan);
        iBFTRetryAdviceModel.setStatus(status);

        Calendar c = Calendar.getInstance();
        c.setTime(requestTime);
//		c.set(Calendar.HOUR_OF_DAY, 0);
//		c.set(Calendar.MINUTE, 0);
//		c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("requestTime", c.getTime(), c.getTime());

        LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
        sortingOrderMap.put("ibftRetryAdviceId", SortingOrder.DESC);


        CustomList<IBFTRetryAdviceModel> customList = ibftRetryAdviceDAO.findByExample(
                iBFTRetryAdviceModel, null, sortingOrderMap, dateRangeHolderModel, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        List<IBFTRetryAdviceModel> list = customList.getResultsetList();

        return list;
    }

    /*
     * To check whether the IBFT Credit advice request (with certian STAN and Date(upto seconds)) is already performed or not
     */
    public boolean checkAlreadySuccessful(String stan, Date requestTime) throws FrameworkCheckedException {
        boolean result = false;

        if (StringUtil.isNullOrEmpty(stan) || requestTime == null) {
            throw new FrameworkCheckedException("Invalid Input for IBFT Credit Advice. STAN:" + stan + " , Request Time:" + requestTime);
        }

        IBFTRetryAdviceModel iBFTRetryAdviceModel = new IBFTRetryAdviceModel();
        iBFTRetryAdviceModel.setStan(stan);
//        iBFTRetryAdviceModel.setStatus(portalConstant);
        iBFTRetryAdviceModel.setStatus("Successful");

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


    /*
     * update status of Credit advice request
     */
    public void updateIBFTStatus(String stan, Date requestTime, String status, String transactionCode) throws FrameworkCheckedException {

        logger.info("Start of updateIBFTStatus... stan:" + stan + " , requestTime:" + requestTime + " , trxCode:" + transactionCode + " , new Status:" + status);

        IBFTRetryAdviceModel iBFTRetryAdviceModel = new IBFTRetryAdviceModel();
        iBFTRetryAdviceModel.setStan(stan);
        iBFTRetryAdviceModel.setStatus(PortalConstants.IBFT_STATUS_IN_PROGRESS);

        LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
        sortingOrderMap.put("ibftRetryAdviceId", SortingOrder.DESC);

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("requestTime", requestTime, requestTime);

        CustomList<IBFTRetryAdviceModel> customList = ibftRetryAdviceDAO.findByExample(
                iBFTRetryAdviceModel, null, sortingOrderMap, dateRangeHolderModel, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        List<IBFTRetryAdviceModel> list = customList.getResultsetList();
        if (list != null && list.size() > 0) {
            iBFTRetryAdviceModel = list.get(0);
        }

        if (iBFTRetryAdviceModel != null && iBFTRetryAdviceModel.getIbftRetryAdviceId() != null) {
            logger.info("Going to updateIBFTStatus stan:" + stan + " , requestTime:" + requestTime + " , IbftRetryAdviceId:" + iBFTRetryAdviceModel.getIbftRetryAdviceId());
            iBFTRetryAdviceModel.setStatus(status);
            iBFTRetryAdviceModel.setTransactionCode(transactionCode);

            ibftRetryAdviceDAO.saveOrUpdate(iBFTRetryAdviceModel);
        } else {
            throw new FrameworkCheckedException("Unable to update status of iBFTRetryAdviceModel to (" + status + ") stan:" + stan + " , requestTime:" + requestTime + " , trxCode:" + transactionCode);
        }

        logger.info("End of updateIBFTStatus... stan:" + stan + " , requestTime:" + requestTime + " , trxCode:" + transactionCode + " , Status:" + status);

    }

    public void saveOrUpdateAccountOpeningStatus(String mobileNo, String cnic, Long accountOpeningStatus, Long isValid, String rrn, String responseCode) throws FrameworkCheckedException {

        logger.info("Start of updateAccountOpeningStatus... mobileNo:" + mobileNo + " , cnic:" + cnic);

        PayMtncRequestModel payMtncRequestModel = new PayMtncRequestModel();
        payMtncRequestModel.setMobileNo(mobileNo);
        payMtncRequestModel.setCnic(cnic);

        LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
        sortingOrderMap.put("payMtncReqId", SortingOrder.DESC);

//        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "requestTime", requestTime,requestTime);

        CustomList<PayMtncRequestModel> customList = payMtncRequestDAO.findByExample(
                payMtncRequestModel, null, sortingOrderMap, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        List<PayMtncRequestModel> list = customList.getResultsetList();

        if (list != null && list.size() == 0) {
            payMtncRequestModel = new PayMtncRequestModel();
            payMtncRequestModel.setCnic(cnic);
            payMtncRequestModel.setMobileNo(mobileNo);
            payMtncRequestModel.setRrn(rrn);
            payMtncRequestModel.setThirdPartyResponseCode(responseCode);
            payMtncRequestModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
            payMtncRequestModel.setCreatedOn(new Date());
            payMtncRequestModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            payMtncRequestModel.setUpdatedOn(new Date());
            payMtncRequestModel.setIsValid(isValid);
            payMtncRequestModel.setIsAccountOpening(accountOpeningStatus);
            this.genericDao.createEntity(payMtncRequestModel);
        }
//        switchWrapper.putObject("payMtncRequestModel", payMtncRequestModel);
    }

    @Override
    public SearchBaseWrapper findIBFTRetryAdviceListView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {

        IBFTRetryAdviceListViewModel iBFTRetryAdviceListViewModel = (IBFTRetryAdviceListViewModel) wrapper.getBasePersistableModel();

        CustomList<IBFTRetryAdviceListViewModel> customList = iBFTRetryAdviceListViewDAO.findByExample(
                iBFTRetryAdviceListViewModel,
                wrapper.getPagingHelperModel(),
                wrapper.getSortingOrderMap(),
                wrapper.getDateRangeHolderModel(),
                PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        wrapper.setCustomList(customList);

        return wrapper;
    }

    @Override
    public void makeCoreAdvice(SwitchWrapper switchWrapper, MiddlewareAdviceVO middlewareAdviceVO) throws Exception {
        Long productId = null;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        BankModel bankModel = new BankModel();
        bankModel.setBankId(BankConstantsInterface.ASKARI_BANK_ID);
        baseWrapper.setBasePersistableModel(bankModel);
        AbstractFinancialInstitution abstractFinancialInstitution = null;
        TransactionDetailMasterModel model = null;
        String errorDescription = null;
        Boolean isValidExciseTransaction = Boolean.FALSE;
        try {
            abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
            productId = middlewareAdviceVO.getProductId();
            if (middlewareAdviceVO.getIsCreditAdvice() != null && middlewareAdviceVO.getIsCreditAdvice()) {

                abstractFinancialInstitution.sendCreditAdvice(switchWrapper);

            } else if (middlewareAdviceVO.getIsBillPaymentRequest()) {

                abstractFinancialInstitution.billPayment(switchWrapper);

            } else if (productId.equals(ProductConstantsInterface.AGENT_ET_COLLECTION)
                    || productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION)
//                    || productId.equals(ProductConstantsInterface.AGENT_VALLENCIA_COLLECTION)

                    || productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION_BY_ACCOUNT)
                    || productId.equals(ProductConstantsInterface.AGENT_LICENSE_FEE_COLLECTION)
                    || productId.equals(ProductConstantsInterface.AGENT_BALUCHISTAN_ET_COLLECTION)
                    || productId.equals(ProductConstantsInterface.AGENT_E_LEARNING_MANAGEMENT_SYSTEM)
                    || productId.equals(ProductConstantsInterface.CUSTOMER_ET_COLLECTION)
                    || productId.equals(ProductConstantsInterface.CUSTOMER_KP_CHALLAN_COLLECTION)
//                    || productId.equals(ProductConstantsInterface.CUSTOMER_VALLENCIA_COLLECTION)
                    || productId.equals(ProductConstantsInterface.CUSTOMER_BALUCHISTAN_ET_COLLECTION)
                    || productId.equals(ProductConstantsInterface.CUSTOMER_E_LEARNING_MANAGEMENT_SYSTEM)
                    || productId.equals(ProductConstantsInterface.CUST_IHL_ISLAMABAD_CAMP)
                    || productId.equals(ProductConstantsInterface.CUST_SARHAD_UNIVERSITY_COLLECTION)
                    || productId.equals(ProductConstantsInterface.CUST_IHL_Gujranawala_CAMP)
                    || productId.equals(ProductConstantsInterface.IHL_Gujranawala_CAMP)
                    || productId.equals(ProductConstantsInterface.SARHAD_UNIVERSITY_COLLECTION)
                    || productId.equals(ProductConstantsInterface.IHL_ISLAMABAD_CAMP)
                    || productId.equals(ProductConstantsInterface.LICENSE_FEE_COLLECTION)
                    || productId.equals(50055L) || productId.equals(50056L)) {
                I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareCollectionPaymentRequest(switchWrapper.getMiddlewareIntegrationMessageVO().getConsumerNo(),
                        switchWrapper.getMiddlewareIntegrationMessageVO().getTransactionAmount(), switchWrapper.getWorkFlowWrapper().getProductModel());
                SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
                requestVO.setRRN(middlewareAdviceVO.getStan());
                i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);

                i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);

                I8SBSwitchControllerResponseVO responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();

                ESBAdapter.processI8sbResponseCode(responseVO, false);

            } else if (productId.equals(ProductConstantsInterface.ACCOUNT_OPENING) || productId.equals(ProductConstantsInterface.CUST_ACCOUNT_OPENING)
                    || productId.equals(ProductConstantsInterface.PORTAL_ACCOUNT_OPENING)) {
                I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareZindigiCustomerSyncRequest(
                        String.valueOf(((AppUserModel) middlewareAdviceVO.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_APP_USER_MODEL)).getFullName()),
                        String.valueOf(((AppUserModel) middlewareAdviceVO.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_APP_USER_MODEL)).getMobileNo()),
                        String.valueOf(((AppUserModel) middlewareAdviceVO.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_APP_USER_MODEL)).getNic()),
                        String.valueOf(((AppUserModel) middlewareAdviceVO.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_APP_USER_MODEL)).getCnicIssuanceDate()),
                        String.valueOf(((AppUserModel) middlewareAdviceVO.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_APP_USER_MODEL)).getNicExpiryDate()),
                        String.valueOf(((CustomerModel) middlewareAdviceVO.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_CUSTOMER_MODEL)).getGender()),
                        String.valueOf(((AppUserModel) middlewareAdviceVO.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_APP_USER_MODEL)).getDob()),
                        String.valueOf(((AppUserModel) middlewareAdviceVO.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_APP_USER_MODEL)).getAddress1()),
                        String.valueOf(((AppUserModel) middlewareAdviceVO.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_APP_USER_MODEL)).getEmail()),
                        String.valueOf(((AppUserModel) middlewareAdviceVO.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_APP_USER_MODEL)).getLastName()),
                        String.valueOf(((AppUserModel) middlewareAdviceVO.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_APP_USER_MODEL)).getMotherMaidenName()),
                        String.valueOf(((AppUserModel) middlewareAdviceVO.getWorkFlowWrapper().getObject(CommandFieldConstants.KEY_APP_USER_MODEL)).getCustomerMobileNetwork()));

                SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
//                requestVO.setRRN(middlewareAdviceVO.getStan());
                i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);

                i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);

                I8SBSwitchControllerResponseVO responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();

                middlewareAdviceVO.setResponseCode(i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO().getResponseCode());
                middlewareAdviceVO.setRetrievalReferenceNumber(requestVO.getRRN());
                middlewareAdviceVO.setStan(requestVO.getSTAN());

                ESBAdapter.processI8sbResponseCode(responseVO, false);
            } else if (productId.equals(ProductConstantsInterface.AGENT_EXCISE_AND_TAXATION)) {
                model = switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel();
                ProductModel pModel = new ProductModel();
                pModel.setProductId(productId);
                BaseWrapper wrapper = new BaseWrapperImpl();
                wrapper.setBasePersistableModel(pModel);
                ProductModel productModel = (ProductModel) switchWrapper.getWorkFlowWrapper().getProductModel();
                I8SBSwitchControllerRequestVO requestVO = null;
                I8SBSwitchControllerResponseVO responseVO = null;
                SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
                if (model.getExciseChallanNo() == null) {
                    requestVO = ESBAdapter.prepareGenerateChallanRequest(middlewareAdviceVO.getExciseAssessmentNumber(),
                            middlewareAdviceVO.getVehicleRegNumber(), middlewareAdviceVO.getExciseAssessmentTotalAmount(), "PAID",
                            productModel.getProductCode(), middlewareAdviceVO.getMicrobankTransactionCode(), model.getRecipientMobileNo());
                    requestVO.setRRN(middlewareAdviceVO.getStan());
                    i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                    i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);
                    responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();
                    ESBAdapter.processI8sbResponseCode(responseVO, false);
                    model.setExciseChallanNo(responseVO.getVctChallanNumber());
                    model.setUpdatedOn(new Date());
                    switchWrapper.getWorkFlowWrapper().setTransactionDetailMasterModel(model);
                    BaseWrapper tdmWrapper = new BaseWrapperImpl();
                    tdmWrapper.setBasePersistableModel(model);
                    this.transactionDetailMasterManager.saveTransactionDetailMasterRequiresNewTransaction(tdmWrapper);
                    //
                    String date = dtf.print(new DateTime());
                    String time = tf.print(new LocalTime());
                    Object[] agentSMSParam = new Object[]{model.getTransactionCode(), productModel.getName(), responseVO.getVctChallanNumber(),
                            model.getAgent1MobileNo(), model.getTotalAmount(), time, date};
                    Object[] customerSMSParam = new Object[]{model.getTransactionCode(), model.getTotalAmount(), productModel.getName(), date, time};
                    BaseWrapper smsBaseWrapper = new BaseWrapperImpl();
                    SmsMessage agentSmsMessage = new SmsMessage(model.getRecipientMobileNo(),
                            MessageUtil.getMessage("excise.tax.agent", agentSMSParam));
                    SmsMessage customerSmsMessage = new SmsMessage(model.getRecipientMobileNo(),
                            MessageUtil.getMessage("excise.tax.customer", customerSMSParam));
                    ArrayList<SmsMessage> smsList = new ArrayList<>(0);
                    smsList.add(customerSmsMessage);
                    smsList.add(agentSmsMessage);

                    smsBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, agentSmsMessage);
                    smsSender.send(smsList);
                }
                model = switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel();
                requestVO = ESBAdapter.prepareChallanStatusRequest(middlewareAdviceVO.getVehicleRegNumber(), model.getExciseChallanNo(), productModel.getProductCode());
                i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);

                responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();
                String msgKey = "i8sb.response." + "payment" + "." + responseVO.getResponseCode();
                errorDescription = MessageUtil.getMessage(msgKey);
                ESBAdapter.processI8sbResponseCode(responseVO, false);
                model.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
                model.setProcessingStatusName(SupplierProcessingStatusConstants.COMPLETE_NAME);
                model.setUpdatedOn(new Date());
                model.setExciseChallanNo(responseVO.getConsumerNumber());
                switchWrapper.getWorkFlowWrapper().setTransactionDetailMasterModel(model);
                isValidExciseTransaction = Boolean.TRUE;
            } else {
                // Needs fresh time in case of Reversals
                switchWrapper.getMiddlewareIntegrationMessageVO().setRequestTime(null);
                abstractFinancialInstitution.sendReversalAdvice(switchWrapper);
            }

            if (switchWrapper.getWorkFlowWrapper() != null && switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel() != null) {
                this.transactionDetailMasterManager.saveTransactionDetailMaster(switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel());
            }

            // Update status from Failed to Successful
            if (!productId.equals(ProductConstantsInterface.AGENT_EXCISE_AND_TAXATION) && isValidExciseTransaction)
                this.updateRetryAdviceReportStatus(middlewareAdviceVO.getMicrobankTransactionCodeId());

        } catch (Exception ex) {
            logger.error("Failed to send core Advice for trxCode:" + middlewareAdviceVO.getMicrobankTransactionCode());
            logger.error(ex.getMessage(), ex);
            throw ex; // will be converted to RuntimeException in CoreAdviceListener
        }
    }

    @Override
    public void makeThirdPartyCashOutReversalAdvice(SwitchWrapper switchWrapper) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        BankModel bankModel = new BankModel();
        bankModel.setBankId(BankConstantsInterface.ASKARI_BANK_ID);
        baseWrapper.setBasePersistableModel(bankModel);
        try {

            switchWrapper.getI8SBSwitchControllerRequestVO().setAreaName("SINDH");
            switchWrapper = this.esbAdapter.makeI8SBCall(switchWrapper);

            I8SBSwitchControllerResponseVO responseVO = switchWrapper.getI8SBSwitchControllerResponseVO();

            ESBAdapter.processI8sbResponseCode(responseVO, false);


            // Update status from Failed to Successful
            // this.updateRetryAdviceReportStatus(middlewareAdviceVO.getMicrobankTransactionCodeId());

        } catch (Exception ex) {
            //  logger.error("Failed to send core Advice for trxCode:" + middlewareAdviceVO.getMicrobankTransactionCode());
            logger.error(ex.getMessage(), ex);
            throw ex; // will be converted to RuntimeException in CoreAdviceListener
        }

    }

    @Override
    public TransactionDetailMasterModel loadTDMForReversal(BaseWrapper baseWrapper) throws Exception {
        return (TransactionDetailMasterModel) transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper).getBasePersistableModel();
    }

    @Override
    public TransactionDetailMasterModel loadTDMForReversalByTransactionCode(String transactionCode) throws Exception {
        return transactionDetailMasterManager.loadTransactionDetailMasterModel(transactionCode);
    }

    @Override
    public void updateTransactionDetailMaster(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        transactionDetailMasterManager.saveTransactionDetailMasterRequiresNewTransaction(baseWrapper);
    }

    @Override
    public IBFTRetryAdviceModel findIBFTRetryByStanAndStatus(MiddlewareAdviceVO adviceVO) throws FrameworkCheckedException {
        logger.info("Start of updateIBFTStatus... stan:" + adviceVO.getStan() + " , new Status:" + PortalConstants.IBFT_STATUS_IN_PROGRESS);
        IBFTRetryAdviceModel model = null;
        IBFTRetryAdviceModel iBFTRetryAdviceModel = new IBFTRetryAdviceModel();
        iBFTRetryAdviceModel.setStan(adviceVO.getStan());
        iBFTRetryAdviceModel.setStatus(PortalConstants.IBFT_STATUS_IN_PROGRESS);
        LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
        sortingOrderMap.put("ibftRetryAdviceId", SortingOrder.DESC);
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("requestTime", adviceVO.getRequestTime(), adviceVO.getRequestTime());
        CustomList<IBFTRetryAdviceModel> customList = ibftRetryAdviceDAO.findByExample(
                iBFTRetryAdviceModel, null, sortingOrderMap, dateRangeHolderModel, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
        List<IBFTRetryAdviceModel> list = customList.getResultsetList();
        if (list != null && list.size() > 0) {
            model = list.get(0);
        }
        return model;
    }

    public IBFTRetryAdviceListViewDAO getiBFTRetryAdviceListViewDAO() {
        return iBFTRetryAdviceListViewDAO;
    }

    public void setiBFTRetryAdviceListViewDAO(
            IBFTRetryAdviceListViewDAO iBFTRetryAdviceListViewDAO) {
        this.iBFTRetryAdviceListViewDAO = iBFTRetryAdviceListViewDAO;
    }

    public void setIBFTIncomingRequestQueue(IBFTIncomingRequestQueue iBFTIncomingRequestQueue) {
        this.iBFTIncomingRequestQueue = iBFTIncomingRequestQueue;
    }

    public void setWalletIncomingRequestQueue(WalletIncomingRequestQueue walletIncomingRequestQueue) {
        this.walletIncomingRequestQueue = walletIncomingRequestQueue;
    }


//    public void setCreditPaymentRequestQueue(CreditPaymentRequestQueue creditPaymentRequestQueue) {
//        this.creditPaymentRequestQueue = creditPaymentRequestQueue;
//    }
//
//    public void setDebitPaymentRequestQueue(DebitPaymentRequestQueue debitPaymentRequestQueue) {
//        this.debitPaymentRequestQueue = debitPaymentRequestQueue;
//    }

    public void setOlaManager(OLAManager olaManager) {
        this.olaManager = olaManager;
    }

    public void setRetryCreditQueueViewDAO(RetryCreditQueueViewDAO retryCreditQueueViewDAO) {
        this.retryCreditQueueViewDAO = retryCreditQueueViewDAO;
    }

    public void setAccountCreditQueueLogDAO(AccountCreditQueueLogDAO accountCreditQueueLogDAO) {
        this.accountCreditQueueLogDAO = accountCreditQueueLogDAO;
    }

    public void setMiniTransactionDAO(MiniTransactionDAO miniTransactionDAO) {
        this.miniTransactionDAO = miniTransactionDAO;
    }

    public void setSafRepoCoreDao(SafRepoCoreDao safRepoCoreDao) {
        this.safRepoCoreDao = safRepoCoreDao;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setSettlementManager(SettlementManager settlementManager) {
        this.settlementManager = settlementManager;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public void setThirdPartyRetryAdviceDAO(ThirdPartyRetryAdviceDao thirdPartyRetryAdviceDao) {
        this.thirdPartyRetryAdviceDAO = thirdPartyRetryAdviceDao;
    }

    public void setTransactionReversalDAO(TransactionReversalDAO transactionReversalDAO) {
        this.transactionReversalDAO = transactionReversalDAO;
    }

    public void setReversalAdviceSender(ReversalAdviceSender reversalAdviceSender) {
        this.reversalAdviceSender = reversalAdviceSender;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public void setGenericDao(GenericDao genericDao) {
        this.genericDao = genericDao;
    }

    public void setPayMtncRequestDAO(PayMtncRequestDAO payMtncRequestDAO) {
        this.payMtncRequestDAO = payMtncRequestDAO;
    }
}
