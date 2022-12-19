package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.bankmodule.MemberBankModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.dispenser.TransferInDispenser;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.microbank.server.service.workflow.credittransfer.CreditTransferTransaction;
import com.inov8.ola.integration.vo.OLAInfo;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;

public class AgentWalletToCoreTransaction extends CreditTransferTransaction{

    private final String OLA_TRANSFER_IN_SWITCH = "OLA_TRANSFER_IN_SWITCH";
    private final String OLA_TRANSFER_IN_RECON_SWITCH = "OLA_TRANSFER_IN_RECON_SWITCH";
    private final String CORE_TRANSFER_IN_SWITCH = "CORE_TRANSFER_IN_SWITCH";

    protected final Log log = LogFactory.getLog(getClass());
    private DateTimeFormatter dateTimeFormat =  DateTimeFormat.forPattern("dd/MM/yyyy");
    private DateTimeFormatter timeFormat =  DateTimeFormat.forPattern("h:mm a");

    private AbstractFinancialInstitution phoenixFinancialInstitution;
    private FinancialIntegrationManager financialIntegrationManager;
    private StakeholderBankInfoManager stakeholderBankInfoManager;
    private ProductDispenseController productDispenseController;
    private CommissionManager commissionManager;
    private MessageSource messageSource;
    private GenericDao genericDAO;
    private String rrnPrefix;
    private RetailerContactManager retailerContactManager;
    private TransferInDispenser transferInDispenser = null;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private SmartMoneyAccountManager smartMoneyAccountManager;


    @Override
    protected WorkFlowWrapper doValidate(WorkFlowWrapper workFlowWrapper) throws Exception {

        if (workFlowWrapper.getUserDeviceAccountModel() == null) {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
        }

        if (workFlowWrapper.getProductModel() != null) {

            if (!workFlowWrapper.getProductModel().getActive()) {

                throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);
            }
        } else {

            throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
        }

        return workFlowWrapper;
    }


    /**
     * This method calls the commission module to calculate the commission on this product and transaction.
     * The wrapper should have product,payment mode and principal amount that is passed onto the commission module
     *
     * @param wrapper WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    public void calculateCommission(WorkFlowWrapper workFlowWrapper) throws Exception {
        logger.info("Calculating commission for Agent IBFT");
        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
        workFlowWrapper.setSegmentModel(segmentModel);
        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
        commissionWrapper.setPaymentModeModel(workFlowWrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
        commissionWrapper.setTransactionModel(workFlowWrapper.getTransactionModel());
        commissionWrapper.setTransactionTypeModel(workFlowWrapper.getTransactionTypeModel());
        commissionWrapper.setProductModel(workFlowWrapper.getProductModel());
        workFlowWrapper.setTaxRegimeModel(workFlowWrapper.getFromRetailerContactModel().getTaxRegimeIdTaxRegimeModel());
        commissionWrapper = this.commissionManager.calculateCommission(workFlowWrapper);
        workFlowWrapper.setCommissionWrapper(commissionWrapper);
    }


    /**
     *
     * @param commissionHolder CommissionAmountsHolder
     * @param calculatedCommissionHolder CommissionAmountsHolder
     * @throws FrameworkCheckedException
     */

    @Override
    public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper _workFlowWrapper) throws Exception {
        logger.info("AgentWalletToCoreTransaction.doCreditTransfer()");
        TransactionModel transactionModel = _workFlowWrapper.getTransactionModel();
        transactionModel.setTransactionAmount(_workFlowWrapper.getTransactionAmount());
        transactionModel.setConfirmationMessage(" _ ");
        transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
        transactionModel.setDiscountAmount(0.0D);
        transactionModel.setNotificationMobileNo(_workFlowWrapper.getAppUserModel().getMobileNo());
        transactionModel.setDeviceTypeId(_workFlowWrapper.getDeviceTypeModel().getDeviceTypeId());
        transactionModel.setTransactionTypeId(_workFlowWrapper.getTransactionTypeModel().getTransactionTypeId());
        transactionModel.setFromRetContactId(_workFlowWrapper.getAppUserModel().getRetailerContactId());
        transactionModel.setMfsId(_workFlowWrapper.getUserDeviceAccountModel().getUserId());
        transactionModel.setFromRetContactName(_workFlowWrapper.getAppUserModel().getFirstName() +" "+ _workFlowWrapper.getAppUserModel().getLastName());
        transactionModel.setFromRetContactMobNo(_workFlowWrapper.getAppUserModel().getMobileNo());
        transactionModel.setCustomerMobileNo(_workFlowWrapper.getAppUserModel().getMobileNo());
        transactionModel.setSaleMobileNo(_workFlowWrapper.getAppUserModel().getMobileNo());
        transactionModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);
        transactionModel.setFromDistContactId(_workFlowWrapper.getSmartMoneyAccountModel().getDistributorContactId());
        transactionModel.setToDistContactId(_workFlowWrapper.getSmartMoneyAccountModel().getDistributorContactId());
        transactionModel.setDistributorId(_workFlowWrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
        transactionModel.setRetailerId(_workFlowWrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getRetailerId());
        transactionModel.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
        transactionModel.setSmartMoneyAccountId(_workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
        TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
        transactionDetailModel.setProductIdProductModel(_workFlowWrapper.getProductModel());
        transactionDetailModel.setSettled(Boolean.TRUE);
        transactionDetailModel.setConsumerNo(_workFlowWrapper.getAppUserModel().getMobileNo());
        transactionDetailModel.setSettled(Boolean.TRUE);
        transactionDetailModel.setCustomField1(String.valueOf(_workFlowWrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId()));
        transactionDetailModel.setCustomField3("8");
        _workFlowWrapper.setTransactionDetailModel(transactionDetailModel);
        computeCommission(_workFlowWrapper);
        CommissionAmountsHolder commissionAmountsHolder = _workFlowWrapper.getCommissionAmountsHolder();
        transactionModel.setTotalCommissionAmount(commissionAmountsHolder.getTotalCommissionAmount() );
        _workFlowWrapper.getTransactionDetailModel().setActualBillableAmount(commissionAmountsHolder.getBillingOrganizationAmount());
        Double totalCommissionAmount = 0.0D;
        if(commissionAmountsHolder.getTotalCommissionAmount() != null) {
            totalCommissionAmount = commissionAmountsHolder.getTotalCommissionAmount();
        }
        transactionModel.setTransactionAmount(commissionAmountsHolder.getTransactionAmount());
        transactionModel.setTotalAmount(transactionModel.getTransactionAmount() +commissionAmountsHolder.getExclusiveFixAmount()+commissionAmountsHolder.getExclusivePercentAmount());
        transactionModel.setTotalCommissionAmount(totalCommissionAmount);
        _workFlowWrapper.setTransactionModel(transactionModel);
        _workFlowWrapper.getTransactionDetailMasterModel().setSendingRegion(_workFlowWrapper.getRetailerModel().getRegionModel().getRegionId());
        _workFlowWrapper.getTransactionDetailMasterModel().setSendingRegionName(_workFlowWrapper.getRetailerModel().getRegionModel().getRegionName());
        _workFlowWrapper.getTransactionDetailMasterModel().setSenderAreaId(_workFlowWrapper.getRetailerContactModel().getAreaId());
        _workFlowWrapper.getTransactionDetailMasterModel().setSenderAreaName(_workFlowWrapper.getRetailerContactModel().getAreaName());
        _workFlowWrapper.getTransactionDetailMasterModel().setSenderDistributorId(_workFlowWrapper.getDistributorModel().getDistributorId());
        _workFlowWrapper.getTransactionDetailMasterModel().setSenderDistributorName(_workFlowWrapper.getDistributorModel().getName());
        if (null != _workFlowWrapper.getDistributorModel().getMnoId()) {
            _workFlowWrapper.getTransactionDetailMasterModel().setSenderServiceOPId(_workFlowWrapper.getDistributorModel().getMnoId());
            _workFlowWrapper.getTransactionDetailMasterModel().setSenderServiceOPName(_workFlowWrapper.getDistributorModel().getMnoModel().getName());
        }
        if(_workFlowWrapper.getHandlerModel() != null){
            logger.info("Setting Handler Model in AgentWalletToCoreTransaction.doCreditTransfer()");
            _workFlowWrapper.getTransactionModel().setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
            _workFlowWrapper.getTransactionModel().setHandlerMfsId(_workFlowWrapper.getHandlerUserDeviceAccountModel().getUserId());
            _workFlowWrapper.getTransactionDetailMasterModel().setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
            _workFlowWrapper.getTransactionDetailMasterModel().setHandlerMfsId(_workFlowWrapper.getHandlerUserDeviceAccountModel().getUserId());
        }
        if(transferInDispenser == null) {
            transferInDispenser = (TransferInDispenser) this.productDispenseController.loadProductDispenser(_workFlowWrapper);
        }
        Double coreBalance = 0.0D ;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(_workFlowWrapper.getOlaSmartMoneyAccountModel());
        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
        _workFlowWrapper.setTransactionModel(transactionModel);
        this.transferOLA(_workFlowWrapper);
        this.transferCore(_workFlowWrapper);
        _workFlowWrapper.putObject("CORE_BALANCE", coreBalance.toString());
        this.sendSMS(_workFlowWrapper);
        _workFlowWrapper.getTransactionModel().addTransactionIdTransactionDetailModel(transactionDetailModel);
        this.saveTransaction(_workFlowWrapper); // save the transaction
        SETTLE_COMMISSION_BLOCK : {
            logger.info("Going to settle commissions using SettlementManager in AgentWalletToCoreTransaction.doCreditTransfer()");
            this.settlementManager.settleCommission(_workFlowWrapper.getCommissionWrapper(), _workFlowWrapper);
        }
        return _workFlowWrapper;
    }


    /**
     * @param _workFlowWrapper
     */
    void sendSMS(WorkFlowWrapper _workFlowWrapper) {
        logger.info("Preparing SMS in AgentWalletToCoreTransaction.sendSMS()");
        MemberBankModel memberBankModel = null;
        String bankShortName = "";
        if(_workFlowWrapper.getObject("MEMBER_BANK") != null)
            memberBankModel = (MemberBankModel) _workFlowWrapper.getObject("MEMBER_BANK");
        if(memberBankModel != null)
            bankShortName = memberBankModel.getBankShortName();
        Double agentBalance = (Double) _workFlowWrapper.getObject("OLA_BALANCE");
        String olaBalance = Formatter.formatDoubleByPattern(agentBalance, "#,###.00");
        String coreAccountNumber = (String) _workFlowWrapper.getObject("CORE_ACCOUNT_NO");
        coreAccountNumber = coreAccountNumber.replaceAll("(\\d+)(\\d{5})","**$2");
        String mobileNo = _workFlowWrapper.getAppUserModel().getMobileNo();
        mobileNo = mobileNo.replaceAll("(\\d+)(\\d{5})","**$2");
        TransactionModel transactionModel = _workFlowWrapper.getTransactionModel();
        ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
        if(_workFlowWrapper.getHandlerModel() == null  ||
                (_workFlowWrapper.getHandlerModel() != null && _workFlowWrapper.getHandlerModel().getSmsToAgent())){
            Object[] smsParams = new Object[] {
                    mobileNo,
                    _workFlowWrapper.getAppUserModel().getFirstName() + " " + _workFlowWrapper.getAppUserModel().getLastName(),
                    transactionModel.getTransactionAmount().toString(),
                    coreAccountNumber,
                    _workFlowWrapper.getCustomerAccount().getTitleOfTheAccount(),
                    bankShortName,
                    timeFormat.print(new LocalTime()),
                    dateTimeFormat.print(new DateTime()),
                    olaBalance
            };
            String agentSMS = this.messageSource.getMessage("AGENT.IBFT.SMS", smsParams, null);
            transactionModel.setNotificationMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
            transactionModel.setConfirmationMessage(agentSMS);
            _workFlowWrapper.getTransactionDetailModel().setCustomField4(agentSMS);
            messageList.add(new SmsMessage(ThreadLocalAppUser.getAppUserModel().getMobileNo(), agentSMS));
        }
        /*if(_workFlowWrapper.getHandlerModel() != null && _workFlowWrapper.getHandlerModel().getSmsToHandler()){
            Object[] smsParams = new Object[] {brandName,
                    transactionModel.getTransactionCodeIdTransactionCodeModel().getCode(),
                    transactionModel.getTransactionAmount().toString(),
                    timeFormat.print(new LocalTime()),
                    dateTimeFormat.print(new DateTime()),
                    coreAccountNumber,
                    brandName,
                    olaBalance
            };
            String handlerSMS = this.messageSource.getMessage("TransferInPayment.SMS.Notification", smsParams, null);
            messageList.add(new SmsMessage(_workFlowWrapper.getHandlerAppUserModel().getMobileNo(), handlerSMS));
        }*/
        _workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
    }

    private OLAInfo getStakeholderOLAInfo(String trxCode, Long reasonId, String trxType, Long olaStakeholderBankInfoId, Long coreStakeholderBankInfoId, Double amount) throws FrameworkCheckedException {
        logger.info("AgentWalletToCoreTransaction.getStakeholderOLAInfo()");
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel bankInfoModel = new StakeholderBankInfoModel();
        bankInfoModel.setPrimaryKey(olaStakeholderBankInfoId);
        searchBaseWrapper.setBasePersistableModel(bankInfoModel);
        bankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setBalanceAfterTrxRequired(Boolean.FALSE);
        olaInfo.setMicrobankTransactionCode(trxCode);
        olaInfo.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
        olaInfo.setReasonId(reasonId);
        olaInfo.setTransactionTypeId(trxType);
        olaInfo.setPayingAccNo(bankInfoModel.getAccountNo());
        olaInfo.setCoreStakeholderBankInfoId(coreStakeholderBankInfoId);
        olaInfo.setBalance(Double.parseDouble(Formatter.formatDouble(amount)));

        return olaInfo;
    }


    /**
     * @param _workFlowWrapper
     * @param transactionModel
     * @param olaAccountInfoModel
     * @throws Exception
     */
    void transferOLA(WorkFlowWrapper _workFlowWrapper) throws Exception {
        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>(0);
        List<OLAInfo> creditList = new ArrayList<OLAInfo>(0);
        String oneLink = "";
        Double oneLinkCharges = null;
        if(oneLink != null && !oneLink.equals(""))
        {
            oneLinkCharges = Double.valueOf(oneLink);
            if(oneLinkCharges > 0){
                OLAInfo olaInfo = getStakeholderOLAInfo(_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(),
                        ReasonConstants.AGENT_IBFT,
                        // These constant will be changed to a new Third party account
                        TransactionTypeConstantsInterface.OLA_CREDIT,
                        PoolAccountConstantsInterface.ONE_LINK_CHARGES_POOL_ACCOUNT,
                        PoolAccountConstantsInterface.ONE_LINK_CHARGES_GL, oneLinkCharges);
                creditList.add(olaInfo);
            }
        }
        CommissionAmountsHolder commissionAmountsHolder = _workFlowWrapper.getCommissionAmountsHolder();
        Double agent1CommissionAmount = commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID);
        StakeholderBankInfoModel iftPoolAccountStakeHolder = this.getStakeholderBankInfoModel(PoolAccountConstantsInterface.AGENT_IBFT_POOL_ACCOUNT);
        Boolean isThirdPartyIncluded = _workFlowWrapper.getProductModel().getInclChargesCheck();
        isThirdPartyIncluded = isThirdPartyIncluded == null ? Boolean.FALSE : isThirdPartyIncluded;
        String iftPoolAccount = iftPoolAccountStakeHolder.getAccountNo();
        String agentBBAccount = _workFlowWrapper.getAccountInfoModel().getAccountNo();
        Double olaBalance = 0.0D;
        Double iftPoolAccountCreditAmount = commissionAmountsHolder.getTransactionAmount() +
                commissionAmountsHolder.getExclusiveFixAmount() +
                commissionAmountsHolder.getExclusivePercentAmount();
        iftPoolAccountCreditAmount = iftPoolAccountCreditAmount == null ? 0.0D : iftPoolAccountCreditAmount;
        Double creditAmount = 0.0D;
        if(isThirdPartyIncluded){
            creditAmount = commissionAmountsHolder.getTransactionAmount();
        }
        else
            creditAmount = commissionAmountsHolder.getTransactionAmount() - commissionAmountsHolder.getInclusiveFixAmount();
        OLAInfo olaInfo = getStakeholderOLAInfo(_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(),
                ReasonConstants.AGENT_IBFT,
                // These constant will be changed to a new Third party account
                TransactionTypeConstantsInterface.OLA_CREDIT,
                PoolAccountConstantsInterface.AGENT_IBFT_POOL_ACCOUNT,
                PoolAccountConstantsInterface.AGENT_IBFT_GL, creditAmount);
        creditList.add(olaInfo);
        /*OLAInfo iftPoolAccountCreditFT = new OLAInfo();//	FT 1
        iftPoolAccountCreditFT.setReasonId(ReasonConstants.AGENT_IBFT);
        iftPoolAccountCreditFT.setBalanceAfterTrxRequired(Boolean.FALSE);
        iftPoolAccountCreditFT.setIsAgent(Boolean.FALSE);
        iftPoolAccountCreditFT.setMicrobankTransactionCode(_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode());
        iftPoolAccountCreditFT.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
        iftPoolAccountCreditFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        iftPoolAccountCreditFT.setReceivingAccNo(iftPoolAccount);
        iftPoolAccountCreditFT.setPayingAccNo(iftPoolAccount);
        iftPoolAccountCreditFT.setBalance(iftPoolAccountCreditAmount);
        if(!StringUtil.isNullOrEmpty(iftPoolAccount)) {
            creditList.add(iftPoolAccountCreditFT);
        }*/
        Double inclusiveCharges = (commissionAmountsHolder.getInclusiveFixAmount() + commissionAmountsHolder.getInclusivePercentAmount());
        inclusiveCharges = inclusiveCharges == null ? 0.0D : inclusiveCharges;
        Double agentCreditAmount = (commissionAmountsHolder.getTransactionAmount() + commissionAmountsHolder.getTransactionProcessingAmount() - agent1CommissionAmount);
        agentCreditAmount = agentCreditAmount == null ? 0.0D : agentCreditAmount;
        if(!isThirdPartyIncluded && inclusiveCharges.equals("")) {
            agentCreditAmount -= inclusiveCharges;
        }
        agentCreditAmount = agentCreditAmount == null ? 0.0D : agentCreditAmount;
        if(agentCreditAmount != null && oneLinkCharges != null && oneLinkCharges > 0)
            agentCreditAmount += oneLinkCharges;
        OLAInfo agentAccountDebitFT = new OLAInfo();												//	FT 2
        agentAccountDebitFT.setReasonId(ReasonConstants.AGENT_IBFT);
        agentAccountDebitFT.setMicrobankTransactionCode(_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode());
        agentAccountDebitFT.setCustomerAccountTypeId(_workFlowWrapper.getRetailerContactModel().getOlaCustomerAccountTypeModelId());
        agentAccountDebitFT.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        agentAccountDebitFT.setPayingAccNo(agentBBAccount);
        agentAccountDebitFT.setReceivingAccNo(agentBBAccount);
        agentAccountDebitFT.setBalance(agentCreditAmount);
        agentAccountDebitFT.setAgentBalanceAfterTrxRequired(Boolean.TRUE);
        agentAccountDebitFT.setBalanceAfterTrxRequired(Boolean.TRUE);
        agentAccountDebitFT.setIsAgent(Boolean.TRUE);
        if((_workFlowWrapper.getHandlerModel() != null)){
            agentAccountDebitFT.setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
            agentAccountDebitFT.setHandlerAccountTypeId(_workFlowWrapper.getHandlerModel().getAccountTypeId());
        }
        if(!StringUtil.isNullOrEmpty(agentBBAccount)) {
            debitList.add(agentAccountDebitFT);
        }
        olaVO.setCreditAccountList(creditList);
        olaVO.setDebitAccountList(debitList);
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
        switchWrapper.setBasePersistableModel(_workFlowWrapper.getOlaSmartMoneyAccountModel());
        switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        switchWrapper.setTransactionAmount(_workFlowWrapper.getTransactionModel().getTransactionAmount() + _workFlowWrapper.getTransactionModel().getTotalCommissionAmount());
        switchWrapper.setOlavo(olaVO);
        _workFlowWrapper.setSwitchWrapper(switchWrapper);

        /*
         *** 	SENDING TRANSFER IN TO OLA BANKING.
         */
        logger.info("OLA > SENDING TRANSFER-IN TO OLA BANKING.");

        if(transferInDispenser == null) {
            transferInDispenser = (TransferInDispenser) this.productDispenseController.loadProductDispenser(_workFlowWrapper);
        }
        _workFlowWrapper = transferInDispenser.doSale(_workFlowWrapper);
        olaBalance = _workFlowWrapper.getSwitchWrapper().getOlavo().getAgentBalanceAfterTransaction();
        _workFlowWrapper.getTransactionDetailModel().setCustomField2(_workFlowWrapper.getCustomerAccount().getNumber());
        _workFlowWrapper.putObject("OLA_BALANCE", olaBalance);
    }


    /**
     * @param _workFlowWrapper
     * @throws Exception
     */
    void transferCore(WorkFlowWrapper _workFlowWrapper) throws Exception {
        logger.info("going to Transfer to Core A/C in AgentWalletToCoreTransaction.transferCore(WorkFlowWrapper _workFlowWrapper)");
        Boolean isThirdPartyIncluded = _workFlowWrapper.getProductModel().getInclChargesCheck();
        isThirdPartyIncluded = isThirdPartyIncluded == null ? Boolean.FALSE : isThirdPartyIncluded;
        String coreAccountNumber = _workFlowWrapper.getCustomerAccount().getNumber();
        StakeholderBankInfoModel inwardFundTransferStakeholderBankInfoModel = new StakeholderBankInfoModel();
        inwardFundTransferStakeholderBankInfoModel.setStakeholderBankInfoId(PoolAccountConstantsInterface.AGENT_IBFT_GL);
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(inwardFundTransferStakeholderBankInfoModel);
        searchBaseWrapper = this.stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
        inwardFundTransferStakeholderBankInfoModel = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();
        CommissionAmountsHolder commissionAmountsHolder = _workFlowWrapper.getCommissionAmountsHolder();
        Double amount = commissionAmountsHolder.getTransactionAmount();
        Double exclusiveAmount = commissionAmountsHolder.getExclusiveFixAmount() + commissionAmountsHolder.getExclusivePercentAmount();
        Boolean isExclusiveAmountApplied = (exclusiveAmount != null && exclusiveAmount > 0D) ? Boolean.TRUE : Boolean.FALSE;
        if(isExclusiveAmountApplied || isThirdPartyIncluded) {
            amount += exclusiveAmount;
        }
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());
        switchWrapper.setBankId(BankConstantsInterface.ASKARI_BANK_ID);
        switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
        switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        switchWrapper.setMiddlewareIntegrationMessageVO(new MiddlewareMessageVO());
        switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
        switchWrapper.setTransactionAmount(amount);
        switchWrapper.setFromAccountNo(inwardFundTransferStakeholderBankInfoModel.getAccountNo());//
        switchWrapper.setFromAccountType("20");
        switchWrapper.setFromCurrencyCode("586");
        switchWrapper.setToAccountNo(_workFlowWrapper.getCustomerAccount().getNumber());
        switchWrapper.setToAccountType("20");
        switchWrapper.setToCurrencyCode("586");
        switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
        switchWrapper.setSmartMoneyAccountModel(_workFlowWrapper.getSmartMoneyAccountModel());
        switchWrapper.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.AGENT_IBFT);
        /*
         *** 	SENDING TRANSFER OUT TO CORE BANKING.
         */
        logger.info("Sending to Core > SENDING Agent IBFT TO PHOENIX.");
        logger.info("JS Wallet > FROM ACCOUNT : "+switchWrapper.getFromAccountNo());
        logger.info("Bank Core > TO ACCOUNT : "+switchWrapper.getToAccountNo());
        logger.info("JS Wallet > AMOUNT : "+switchWrapper.getTransactionAmount());
        //Core FT code needs to be incorporated.
        BaseWrapper _baseWrapper = new BaseWrapperImpl();
        _baseWrapper.setBasePersistableModel(new BankModel(BankConstantsInterface.ASKARI_BANK_ID));
        AbstractFinancialInstitution coreFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(_baseWrapper);
        switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField11(switchWrapper.getToAccountNo());
        SwitchWrapper switchWrapper2 = new SwitchWrapperImpl();
        switchWrapper2.setWorkFlowWrapper(_workFlowWrapper) ;
        switchWrapper2.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel()) ;
        switchWrapper2.setInclusiveChargesApplied(switchWrapper.getInclusiveChargesApplied());// to be used in CreditAdvice
        logger.info("[BBToCoreAccountTransaction.doSale] Going to make FT at T24 from Agent A/C to Recipient A/C." +
                " LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
                " Trx ID:" + _workFlowWrapper.getTransactionCodeModel().getCode());
        switchWrapper2 = coreFinancialInstitution.creditAccountAdvice(switchWrapper) ;
        //
        //switchWrapper = phoenixFinancialInstitution.debitAccount(switchWrapper);
        String bankResponseCode = switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode();
        _workFlowWrapper.setMiddlewareSwitchWrapper(switchWrapper); // for day end O.F. settlement of Core FT
        _workFlowWrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
        _workFlowWrapper.getTransactionDetailMasterModel().setFundTransferRrn(switchWrapper.getMiddlewareIntegrationMessageVO().getRetrievalReferenceNumber());
        _workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
        _workFlowWrapper.putObject("CORE_ACCOUNT_NO", coreAccountNumber);
    }


    /**
     * @param _workFlowWrapper
     * @throws Exception
     */
    void computeCommission(WorkFlowWrapper _workFlowWrapper) throws Exception {
        this.calculateCommission(_workFlowWrapper);
        Double franchise1CommissionAmount = 0.0D;
        Double agent1CommissionAmount = 0.0D;
        CommissionWrapper commissionWrapper = _workFlowWrapper.getCommissionWrapper();
        CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        List<CommissionRateModel> commissionRateModelArray = (ArrayList<CommissionRateModel>) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);
        Double _commissionRate = 0.0D;
        if(commissionRateModelArray != null && !commissionRateModelArray.isEmpty()) {
            _commissionRate = commissionRateModelArray.get(0).getRate();
        } else {
            logger.error("No Commission Amount Defined or Check Transaction Amount vs Commission Rate's range");
        }
        //if agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction
        if(commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID) != null) {
            agent1CommissionAmount = commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID);
        }
        if(commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.FRANCHISE1_STAKE_HOLDER_ID) != null) {
            franchise1CommissionAmount = commissionAmountsHolder.getStakeholderCommissionsMap().get(CommissionConstantsInterface.FRANCHISE1_STAKE_HOLDER_ID);
        }
        if(_workFlowWrapper.getFromRetailerContactModel() != null && _workFlowWrapper.getFromRetailerContactModel().getHead()) {
            agent1CommissionAmount += franchise1CommissionAmount;
        }
        if(_commissionRate != null && _commissionRate > 0) {
            commissionAmountsHolder.setTotalCommissionAmount(_commissionRate);
        }
        commissionAmountsHolder.getStakeholderCommissionsMap().put(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID, agent1CommissionAmount);
        _workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);
    }
    @Override
    protected WorkFlowWrapper logTransaction(WorkFlowWrapper workFlowWrapper) throws Exception {
        return workFlowWrapper;
    }

    private StakeholderBankInfoModel getStakeholderBankInfoModel(Long stakeholderBankInfoId) throws FrameworkCheckedException {
        logger.info("Fetch Core Settlement Account in Branchless");
        StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
        stakeholderBankInfoModel.setStakeholderBankInfoId(stakeholderBankInfoId);
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
        this.stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
        stakeholderBankInfoModel = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();
        return stakeholderBankInfoModel;
    }

    @Override
    public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper) throws Exception {
        SwitchWrapper switchWrapper = (SwitchWrapper) workFlowWrapper.getObject(this.OLA_TRANSFER_IN_SWITCH);
        SwitchWrapper switchWrapperRecon = (SwitchWrapper) workFlowWrapper.getObject(this.OLA_TRANSFER_IN_RECON_SWITCH);
        SwitchWrapper switchWrapperCore = (SwitchWrapper) workFlowWrapper.getObject(this.CORE_TRANSFER_IN_SWITCH);
        SmartMoneyAccountModel coreSmartMoneyAccountModel = workFlowWrapper.getSmartMoneyAccountModel();
        SmartMoneyAccountModel olaSmartMoneyAccountModel = workFlowWrapper.getOlaSmartMoneyAccountModel();
        workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
        if(switchWrapper != null) {
            logger.info("****************** ROLL BACK FOR AGENT IBFT ******************");
            saveTransaction(workFlowWrapper);
            switchWrapper.setSmartMoneyAccountModel(olaSmartMoneyAccountModel);
            rollback(switchWrapper, workFlowWrapper);
        }
        if(switchWrapperRecon != null) {
            logger.info("****************** ROLL BACK FOR Agent IBFT ******************");
            saveTransaction(workFlowWrapper);
            switchWrapperRecon.setSmartMoneyAccountModel(olaSmartMoneyAccountModel);
            rollback(switchWrapperRecon, workFlowWrapper);
        }
        if(switchWrapperCore != null) {
            logger.info("****************** ROLL BACK FOR Agent IBFT ******************");
            saveTransaction(workFlowWrapper);
            switchWrapperCore.setSmartMoneyAccountModel(coreSmartMoneyAccountModel);
            rollback(switchWrapperCore, workFlowWrapper);
        }
        return workFlowWrapper;
    }

    private void rollback(SwitchWrapper switchWrapper, WorkFlowWrapper workFlowWrapper) throws Exception {
        logger.info("AgentWalletToCoreTransaction.Rollback()");
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(switchWrapper.getSmartMoneyAccountModel());
        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
        logger.info("Financial Institution Class = "+abstractFinancialInstitution.getClass().getName());
        String olaFromAccount = switchWrapper.getToAccountNo();
        String olaToAccount = switchWrapper.getFromAccountNo();
        if(!StringUtil.isNullOrEmpty(olaFromAccount) && !StringUtil.isNullOrEmpty(olaToAccount)) {
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
            switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
            switchWrapper.setBasePersistableModel(workFlowWrapper.getOlaSmartMoneyAccountModel());
            switchWrapper.setBankId(switchWrapper.getSmartMoneyAccountModel().getBankId());
            switchWrapper.setTransactionAmount(switchWrapper.getTransactionAmount());
            switchWrapper.setWorkFlowWrapper(workFlowWrapper);
            switchWrapper.setFromAccountNo(olaFromAccount);
            switchWrapper.setToAccountNo(olaToAccount);
            OLAVO olaVO = switchWrapper.getOlavo();
            if(olaVO == null) {
                olaVO = new OLAVO();
                olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                switchWrapper.setOlavo(olaVO);
            }
            switchWrapper.getOlavo().setPayingAccNo(olaFromAccount);
            switchWrapper.getOlavo().setReceivingAccNo(olaToAccount);
            switchWrapper.getOlavo().setBalance(switchWrapper.getTransactionAmount());
            logger.info("AgentWalletToCoreTransaction.Rollback() From Account : "+olaFromAccount);
            logger.info("AgentWalletToCoreTransaction.Rollback() To Account   : "+olaToAccount);
            logger.info("AgentWalletToCoreTransaction.Rollback() Amount       : "+switchWrapper.getTransactionAmount());
            abstractFinancialInstitution.rollback(switchWrapper);
        }
    }


    private void saveTransaction(WorkFlowWrapper _workFlowWrapper) throws FrameworkCheckedException {
        txManager.transactionRequiresNewTransaction(_workFlowWrapper);
    }

    /*
     * 	DI/IOC Methods
     */

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }
    public void setGenericDAO(GenericDao genericDAO) {
        this.genericDAO = genericDAO;
    }
    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)	{
        this.financialIntegrationManager = financialIntegrationManager;
    }
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    public void setProductDispenseController(ProductDispenseController productDispenseController) {
        this.productDispenseController = productDispenseController;
    }
    public void setPhoenixFinancialInstitution(AbstractFinancialInstitution phoenixFinancialInstitution) {
        this.phoenixFinancialInstitution = phoenixFinancialInstitution;
    }
    public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
        this.stakeholderBankInfoManager = stakeholderBankInfoManager;
    }
    public void setRetailerContactManager(RetailerContactManager retailerContactManager){
        this.retailerContactManager = retailerContactManager;
    }
    public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
    }


    /**
     * Populates the transaction object with all the necessary data to save it in the db.
     * @param wrapper WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper _workFlowWrapper) throws Exception {
        _workFlowWrapper = super.doPreStart(_workFlowWrapper);
        if(_workFlowWrapper.getHandlerModel() != null){
            // Populate the Handler OLA Smart Money Account from DB
            SmartMoneyAccountModel sma = smartMoneyAccountManager.getSMAccountByHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
            _workFlowWrapper.setHandlerSMAModel(sma);
            // Set Handler User Device Account Model
            UserDeviceAccountsModel handlerUserDeviceAccountsModel = new UserDeviceAccountsModel();
            handlerUserDeviceAccountsModel.setAppUserId(_workFlowWrapper.getHandlerAppUserModel().getAppUserId());
            handlerUserDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(handlerUserDeviceAccountsModel);
            baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
            _workFlowWrapper.setHandlerUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
        }
        return _workFlowWrapper;
    }

    public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
        this.smartMoneyAccountManager = smartMoneyAccountManager;
    }
}
