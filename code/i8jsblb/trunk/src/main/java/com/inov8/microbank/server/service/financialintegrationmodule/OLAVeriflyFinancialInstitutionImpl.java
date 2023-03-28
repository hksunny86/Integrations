package com.inov8.microbank.server.service.financialintegrationmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ImplementationNotSupportedException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.integration.vo.P2PVO;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.ola.integration.vo.OLAInfo;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.CommissionTypeConstants;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.EncryptionUtil;
import com.inov8.ola.util.TransactionTypeConstants;
import com.inov8.verifly.common.constants.FailureReasonConstants;
import com.inov8.verifly.common.exceptions.InvalidDataException;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeUtils;

import java.util.*;

public class OLAVeriflyFinancialInstitutionImpl extends VeriflyFinancialInstitutionImpl {
    protected final Log logger = LogFactory.getLog(OLAVeriflyFinancialInstitutionImpl.class);
    private StakeholderBankInfoManager stakeholderBankInfoManager;
    private GenericDao genericDao;
    private SmartMoneyAccountManager smartMoneyAccountManager;
    private AppUserManager appUserManager;
    private VeriflyManager veriflyManager;
    private CommissionManager commissionManager;

    public OLAVeriflyFinancialInstitutionImpl() {
        super();
    }


    public OLAVeriflyFinancialInstitutionImpl(SwitchController switchController, GenericDao genericDao) {
        super(switchController, genericDao);
    }

    @Override
    public boolean isVeriflyLite() throws FrameworkCheckedException, Exception {
        return false;
    }

    @Override
    public SwitchWrapper rollback(SwitchWrapper switchWrapper) throws FrameworkCheckedException {

        if (switchWrapper != null && switchWrapper.getOlavo() != null) {
            switchWrapper = this.switchController.rollback(switchWrapper);
        }

        return switchWrapper;
    }

    public SwitchWrapper getLedger(SwitchWrapper wrapper) throws Exception {
        if (wrapper != null && wrapper.getLedgerVO() != null) {
            wrapper = this.switchController.getLedger(wrapper);
        }

        return wrapper;
    }

    public SwitchWrapper createAccount(SwitchWrapper switchWrapper) throws FrameworkCheckedException, FrameworkCheckedException, Exception {

        if (switchWrapper.getWorkFlowWrapper() != null) {
            if (switchWrapper.getWorkFlowWrapper().getMPin() != null) {
//				System.out.println("TPin  : "+switchWrapper.getWorkFlowWrapper().getMPin());
//				System.out.println("VeriflyFinancialInstitutionImpl.checkBalance() TPin : "+switchWrapper.getWorkFlowWrapper().getMPin());
            }

        }


//		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();


//		String mPin = (String) switchWrapper.getObject(CommandFieldConstants.KEY_MPIN);

//		AccountInfoModel accountInfoModel = new AccountInfoModel();
//		accountInfoModel.setCustomerId(appUserModel.getCustomerId());
//		accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
//		accountInfoModel.setOldPin(pin);
//
//		LogModel logModel = new LogModel();
//		logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
//		logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
//		logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

//		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
//
//		veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
//		veriflyBaseWrapper.setLogModel(logModel);
//
//		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
//
//		veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
//		boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

//		if(errorMessagesFlag)
//		{

        //switchWrapper.setBankId( smartMoneyAccountModel.getBankId() );
        if (switchWrapper.getPaymentModeId() == null)
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        //WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        if (switchWrapper.getWorkFlowWrapper() != null) {
            switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
        }


//            workFlowWrapper.setTransactionModel(new TransactionModel());
//            workFlowWrapper.setMPin(mPin);
//            switchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

        switchWrapper = this.switchController.createAccount(switchWrapper);
//		}
//		else
//		{
//			String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
//			throw new CommandException(veriflyErrorMessage,ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
//		}
        return switchWrapper;

    }

    public SwitchWrapper debitCreditAccount(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {

        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        AccountInfoModel threadLocalAccountInfoModel = getThreadLocalAccountInfoModel(switchWrapper);
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        boolean errorMessagesFlag = false;
        boolean isThreadLocalAccountInfoLoaded = false;
        boolean skipAccInfoLoading = switchWrapper.getSkipAccountInfoLoading();

        // In case of multiple FTs in single trx, we dont want accountInfoModel in each FT
        if (!skipAccInfoLoading) {
            if (threadLocalAccountInfoModel != null && threadLocalAccountInfoModel.getAccountNo() != null) {
                accountInfoModel = threadLocalAccountInfoModel;
                errorMessagesFlag = true;
                isThreadLocalAccountInfoLoaded = true;

            } else {

                if (switchWrapper.getWorkFlowWrapper().getAccountInfoModel() != null) {
                    accountInfoModel.setOldPin(switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
                }

                if (appUserModel.getCustomerId() != null)
                    accountInfoModel.setCustomerId(appUserModel.getCustomerId());
                else
                    accountInfoModel.setCustomerId(appUserModel.getAppUserId());

                accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());

                LogModel logModel = new LogModel();
                logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
                logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

                veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

                veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
                veriflyBaseWrapper.setLogModel(logModel);

                veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

                veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
                errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();
            }
        } else {
            errorMessagesFlag = true;
            isThreadLocalAccountInfoLoaded = true;
        }

        if (errorMessagesFlag) {

            //---------------------------For Pool Accounts

            //get the pool account
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
//				StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
//				stakeholderBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
//				searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
//				stakeholderBankInfoModel = (StakeholderBankInfoModel)stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

            OLAVO olaVO = new OLAVO();

            //------------------------------------------------
            if (false == isThreadLocalAccountInfoLoaded) {
                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
                setThreadLocalAccountInfoModel(switchWrapper, accountInfoModel);//added by mudassir - place AccountinfoModel in threadLocal for reducing verifly calls
            }

            switchWrapper.setAccountInfoModel(accountInfoModel);

            long productId = switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().longValue();

            if (productId == TransactionProductEnum.TRANSACTION_IN_PRODUCT.getProductId() || productId == TransactionProductEnum.TRANSACTION_OUT_PRODUCT.getProductId()) {

                logger.info("OLAVeriflyFinancialInstitutionImpl > TRANSACTION_IN/OUT_PRODUCT " + productId);

                SmartMoneyAccountModel olaSmartMoneyAccount = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
                accountInfoModel = switchWrapper.getWorkFlowWrapper().getAccountInfoModel();
                accountInfoModel.setAccountNick(olaSmartMoneyAccount.getName());

                LogModel logModel = new LogModel();
                logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
                logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
                logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

                veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
                veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
                veriflyBaseWrapper.setLogModel(logModel);
                veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
                veriflyBaseWrapper = this.verifyCredentials(veriflyBaseWrapper);

                if (veriflyBaseWrapper.isErrorStatus()) {

                    accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();

                    OLAVO olaVo = new OLAVO();
                    olaVo.setBalance(switchWrapper.getTransactionAmount());
                    olaVo.setPayingAccNo(switchWrapper.getFromAccountNo());
                    olaVo.setReceivingAccNo(switchWrapper.getToAccountNo());
                    olaVo.setReasonId(ReasonConstants.TRANSFER_IN);
                    olaVo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

                    switchWrapper.setOlavo(olaVo);
                }
            } else if (productId == TransactionProductEnum.AGENT_IBFT_PRODUCT.getProductId()) {
                logger.info("OLAVeriflyFinancialInstitutionImpl > AGENT_IBFT_PRODUCT " + productId);
                accountInfoModel = switchWrapper.getWorkFlowWrapper().getAccountInfoModel();
                Long customerId = ThreadLocalAppUser.getAppUserModel().getAppUserId();
                accountInfoModel = veriflyManager.getAccountInfoModel(customerId, smartMoneyAccountModel.getName());
                if (accountInfoModel == null) {
                    logger.error("Could not load AccountInfo in OLAVeriflyFinancialInstitution.transferFunds()");
                    throw new InvalidDataException(String.valueOf(FailureReasonConstants.RECORD_IS_INACTIVE));
                }
                switchWrapper.setAccountInfoModel(accountInfoModel);
                if (accountInfoModel != null && accountInfoModel.getAccountInfoId() != null) {
                    OLAVO olaVo = new OLAVO();
                    olaVo.setBalance(switchWrapper.getTransactionAmount());
                    olaVo.setPayingAccNo(switchWrapper.getFromAccountNo());
                    olaVo.setReceivingAccNo(switchWrapper.getToAccountNo());
                    olaVo.setReasonId(ReasonConstants.AGENT_IBFT);
                    olaVo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                    switchWrapper.setOlavo(olaVo);
                }
            }

            if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(50018L)) {

                olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaVO.setReasonId(ReasonConstants.CUSTOMER_RETAIL_PAYMENT);

                StakeholderBankInfoModel retailPaymentSundryBankInfoModel = new StakeholderBankInfoModel();
                retailPaymentSundryBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.RETAIL_PAYMENT_SUNDRY_ACCOUNT_ID);
                searchBaseWrapper.setBasePersistableModel(retailPaymentSundryBankInfoModel);
                retailPaymentSundryBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();


                if (switchWrapper.getFtOrder() == 1) {
                    // FT: Customer to Sundry
                    switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
                    switchWrapper.setToAccountNo(retailPaymentSundryBankInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                    olaVO.setReceivingAccNo(retailPaymentSundryBankInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                } else if (switchWrapper.getFtOrder() == 2) {
                    // FT: Sundry to Agent
                    switchWrapper.setFromAccountNo(retailPaymentSundryBankInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(retailPaymentSundryBankInfoModel.getAccountNo());

                    SmartMoneyAccountModel agentSMA = switchWrapper.getWorkFlowWrapper().getRecipientSmartMoneyAccountModel();
                    AccountInfoModel agentAccountInfo = getAccountInfoModelBySmartMoneyAccount(agentSMA, switchWrapper.getWorkFlowWrapper().getReceiverAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
                    switchWrapper.setToAccountNo(agentAccountInfo.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount())));
                    olaVO.setReceivingAccNo(agentAccountInfo.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount())));
                } else if (switchWrapper.getFtOrder() == 3) {
                    // FT: Sundry to Commission Recon
                    switchWrapper.setFromAccountNo(retailPaymentSundryBankInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(retailPaymentSundryBankInfoModel.getAccountNo());

                    StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
                    commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
                    searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
                    commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
                    switchWrapper.setToAccountNo(commissionReconPoolBankInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));
                    olaVO.setReceivingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));

                } else if (switchWrapper.getFtOrder() == 4) {
                    // Franchise commission
                    switchWrapper.setFromAccountNo(retailPaymentSundryBankInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(retailPaymentSundryBankInfoModel.getAccountNo());

                    AccountInfoModel franchiseAccountInfoModel = this.loadFranchiseAccountInfo(switchWrapper.getWorkFlowWrapper().getReceiverAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

                    switchWrapper.setToAccountNo(franchiseAccountInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));
                    olaVO.setReceivingAccNo(franchiseAccountInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));

                }
                switchWrapper.setOlavo(olaVO);

            }

            if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50013L) {
                switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());

                olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
                olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaVO.setCustomerAccountTypeId(CustomerAccountTypeConstants.RETAILER);
                olaVO.setReasonId(ReasonConstants.RET_RET_CREDIT_TRSFR);

                SmartMoneyAccountModel receiverSMA = switchWrapper.getWorkFlowWrapper().getReceivingSmartMoneyAccountModel();
                AccountInfoModel receiverAccountInfo = getAccountInfoModelBySmartMoneyAccount(receiverSMA, switchWrapper.getWorkFlowWrapper().getToRetailerContactAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
                switchWrapper.setToAccountNo(receiverAccountInfo.getAccountNo());
                switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount())));
                olaVO.setReceivingAccNo(receiverAccountInfo.getAccountNo());
                olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount())));
                switchWrapper.setOlavo(olaVO);
                logger.info("[OLAVeriflyFinancialInstitution.debitCreditAccount] Going to make FT from Agent1 to Agent2 Account. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " FT Amount: " + switchWrapper.getTransactionAmount());
            } else if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50006L) {
                olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaVO.setReasonId(ReasonConstants.CASH_WITHDRAWAL);

                StakeholderBankInfoModel cashWithdrawalSundryBankInfoModel = new StakeholderBankInfoModel();
                cashWithdrawalSundryBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.CASH_WITHDRAWAL_SUNDRY_ACCOUNT_ID);
                searchBaseWrapper.setBasePersistableModel(cashWithdrawalSundryBankInfoModel);
                cashWithdrawalSundryBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();


                if (switchWrapper.getFtOrder() == 1) {
                    // FT: Customer to Sundry
                    SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();
                    AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

                    accountInfoModel = customerAccountInfo; // in case of Cash withdrawal accountInfo loading is skipped, so assigning

                    switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());
                    olaVO.setPayingAccNo(customerAccountInfo.getAccountNo());
                    switchWrapper.setToAccountNo(cashWithdrawalSundryBankInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                    olaVO.setReceivingAccNo(cashWithdrawalSundryBankInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                } else if (switchWrapper.getFtOrder() == 2) {
                    // FT: Sundry to Agent
                    switchWrapper.setFromAccountNo(cashWithdrawalSundryBankInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(cashWithdrawalSundryBankInfoModel.getAccountNo());

                    SmartMoneyAccountModel agentSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
                    AccountInfoModel agentAccountInfo = getAccountInfoModelBySmartMoneyAccount(agentSMA, appUserModel.getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
                    switchWrapper.setToAccountNo(agentAccountInfo.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
                    olaVO.setReceivingAccNo(agentAccountInfo.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
                } else if (switchWrapper.getFtOrder() == 3) {
                    // FT: Sundry to Commission Recon
                    switchWrapper.setFromAccountNo(cashWithdrawalSundryBankInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(cashWithdrawalSundryBankInfoModel.getAccountNo());

                    StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
                    commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
                    searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
                    commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
                    switchWrapper.setToAccountNo(commissionReconPoolBankInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise1CommissionAmount())));
                    olaVO.setReceivingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise1CommissionAmount())));

                } else if (switchWrapper.getFtOrder() == 4) {
                    // Franchise commission
                    switchWrapper.setFromAccountNo(cashWithdrawalSundryBankInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(cashWithdrawalSundryBankInfoModel.getAccountNo());

                    AccountInfoModel franchiseAccountInfoModel = this.loadFranchiseAccountInfo(appUserModel.getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

                    switchWrapper.setToAccountNo(franchiseAccountInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise1CommissionAmount())));
                    olaVO.setReceivingAccNo(franchiseAccountInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise1CommissionAmount())));

                }
                switchWrapper.setOlavo(olaVO);

            } else if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50002L) {
                olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaVO.setCustomerAccountTypeId(CustomerAccountTypeConstants.RETAILER);
                olaVO.setReasonId(ReasonConstants.CASH_DEPOSIT);

                if (switchWrapper.getFtOrder() == 1) {
                    SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
                    AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

                    switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(accountInfoModel.getAccountNo());

                    switchWrapper.setToAccountNo(customerAccountInfo.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount())));
                    olaVO.setReceivingAccNo(customerAccountInfo.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount())));
                } else if (switchWrapper.getFtOrder() == 2) {
                    StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
                    commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
                    searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
                    commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

                    switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(accountInfoModel.getAccountNo());

                    switchWrapper.setToAccountNo(commissionReconPoolBankInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
                    olaVO.setReceivingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
                } else if (switchWrapper.getFtOrder() == 3) {
                    // Franchise commission
                    StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
                    commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
                    searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
                    commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

                    switchWrapper.setFromAccountNo(commissionReconPoolBankInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(commissionReconPoolBankInfoModel.getAccountNo());

                    AccountInfoModel franchiseAccountInfoModel = this.loadFranchiseAccountInfo(appUserModel.getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

                    switchWrapper.setToAccountNo(franchiseAccountInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise1CommissionAmount())));
                    olaVO.setReceivingAccNo(franchiseAccountInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise1CommissionAmount())));
                }


                switchWrapper.setOlavo(olaVO);
                logger.info("[OLAVeriflyFinancialInstitution.debitCreditAccount] Going to make FT from Agent Account to Customer Account. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " FT Amount: " + switchWrapper.getTransactionAmount());

            } else if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50000L) {
                switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());

                olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
                olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaVO.setReasonId(ReasonConstants.ACCOUNT_TO_ACCOUNT);

                if (switchWrapper.getFtOrder() == 1) {
                    SmartMoneyAccountModel receiverCustomerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
                    AccountInfoModel receiverCustomerAccountInfo = getAccountInfoModelBySmartMoneyAccount(receiverCustomerSMA, receiverCustomerSMA.getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
                    switchWrapper.setToAccountNo(receiverCustomerAccountInfo.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount())));
                    olaVO.setReceivingAccNo(receiverCustomerAccountInfo.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount())));

                } else {
                    StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
                    commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
                    searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
                    commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
                    switchWrapper.setToAccountNo(commissionReconPoolBankInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmount())));
                    olaVO.setReceivingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount())));

                }


                switchWrapper.setOlavo(olaVO);
                logger.info("[OLAVeriflyFinancialInstitution.debitCreditAccount] Going to make FT from Customer1 Account to Customer2 Account. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " FT Amount: " + switchWrapper.getTransactionAmount());

            } else if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(50010L)) {
                //Account to Cash
                olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaVO.setReasonId(ReasonConstants.CUSTOMER_ACCOUNT_TO_CASH);

                StakeholderBankInfoModel accToCashSundryBankInfoModel = new StakeholderBankInfoModel();
                accToCashSundryBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID);
                searchBaseWrapper.setBasePersistableModel(accToCashSundryBankInfoModel);
                accToCashSundryBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

                if (switchWrapper.isAccountToCashLeg2()) {
                    //Acc To Cash - Leg 2
                    if (switchWrapper.getFtOrder() == 1) {
                        // FT 1: Sundry to Agent2
                        switchWrapper.setFromAccountNo(accToCashSundryBankInfoModel.getAccountNo());
                        olaVO.setPayingAccNo(accToCashSundryBankInfoModel.getAccountNo());
                        switchWrapper.setToAccountNo(accountInfoModel.getAccountNo());
                        switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount())));
                        olaVO.setReceivingAccNo(accountInfoModel.getAccountNo());
                        olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount())));
                    } else if (switchWrapper.getFtOrder() == 2) {
                        // FT 2: Sundry to Franchise2
                        switchWrapper.setFromAccountNo(accToCashSundryBankInfoModel.getAccountNo());
                        olaVO.setPayingAccNo(accToCashSundryBankInfoModel.getAccountNo());

                        AccountInfoModel franchiseAccountInfoModel = this.loadFranchiseAccountInfo(appUserModel.getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

                        switchWrapper.setToAccountNo(franchiseAccountInfoModel.getAccountNo());
                        switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));
                        olaVO.setReceivingAccNo(franchiseAccountInfoModel.getAccountNo());
                        olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));
                    }
                } else {
                    //Acc To Cash - Leg 1
                    if (switchWrapper.getFtOrder() == 1) {
                        // FT 1: Customer to Sundry
                        SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();
                        AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

                        switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());
                        olaVO.setPayingAccNo(customerAccountInfo.getAccountNo());
                        switchWrapper.setToAccountNo(accToCashSundryBankInfoModel.getAccountNo());
                        switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                        olaVO.setReceivingAccNo(accToCashSundryBankInfoModel.getAccountNo());
                        olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                    } else if (switchWrapper.getFtOrder() == 2) {
                        // FT 2: Sundry to Recon
                        switchWrapper.setFromAccountNo(accToCashSundryBankInfoModel.getAccountNo());
                        olaVO.setPayingAccNo(accToCashSundryBankInfoModel.getAccountNo());

                        StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
                        commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
                        searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
                        commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

                        switchWrapper.setToAccountNo(commissionReconPoolBankInfoModel.getAccountNo());
                        switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));

                        olaVO.setReceivingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
                        olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));
                    }
                }
                switchWrapper.setOlavo(olaVO);

            } else if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(50011L)) {
                //Cash To Cash Transaction
                olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaVO.setReasonId(ReasonConstants.CUSTOMER_CASH_TO_CASH);

                StakeholderBankInfoModel cashToCashSundryBankInfoModel = new StakeholderBankInfoModel();
                cashToCashSundryBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID);
                searchBaseWrapper.setBasePersistableModel(cashToCashSundryBankInfoModel);
                cashToCashSundryBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

                if (switchWrapper.isCashToCashLeg2()) {
                    //Cash To Cash - Leg 2
                    if (switchWrapper.getFtOrder() == 1) {
                        // FT 1: Sundry to Agent2
                        switchWrapper.setFromAccountNo(cashToCashSundryBankInfoModel.getAccountNo());
                        olaVO.setPayingAccNo(cashToCashSundryBankInfoModel.getAccountNo());
                        switchWrapper.setToAccountNo(accountInfoModel.getAccountNo());
                        switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount())));
                        olaVO.setReceivingAccNo(accountInfoModel.getAccountNo());
                        olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount())));
                    } else if (switchWrapper.getFtOrder() == 2) {
                        // FT 3: Sundry to Franchise1
                        switchWrapper.setFromAccountNo(cashToCashSundryBankInfoModel.getAccountNo());
                        olaVO.setPayingAccNo(cashToCashSundryBankInfoModel.getAccountNo());

                        AccountInfoModel franchiseAccountInfoModel = this.loadFranchiseAccountInfo(appUserModel.getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

                        switchWrapper.setToAccountNo(franchiseAccountInfoModel.getAccountNo());
                        switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));
                        olaVO.setReceivingAccNo(franchiseAccountInfoModel.getAccountNo());
                        olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));
                    }
                } else {
                    //Cash To Cash - Leg 1
                    if (switchWrapper.getFtOrder() == 1) {
                        // FT 1: Agent to Sundry
                        switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
                        olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
                        switchWrapper.setToAccountNo(cashToCashSundryBankInfoModel.getAccountNo());
                        switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
                        olaVO.setReceivingAccNo(cashToCashSundryBankInfoModel.getAccountNo());
                        olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
                    } else if (switchWrapper.getFtOrder() == 2) {
                        // FT 2: Sundry to Recon
                        switchWrapper.setFromAccountNo(cashToCashSundryBankInfoModel.getAccountNo());
                        olaVO.setPayingAccNo(cashToCashSundryBankInfoModel.getAccountNo());

                        StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
                        commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
                        searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
                        commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

                        switchWrapper.setToAccountNo(commissionReconPoolBankInfoModel.getAccountNo());
                        switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - (switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise1CommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount()))));

                        olaVO.setReceivingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
                        olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - (switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise1CommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount()))));
                    } else if (switchWrapper.getFtOrder() == 3) {
                        // FT 3: Sundry to Franchise1
                        switchWrapper.setFromAccountNo(cashToCashSundryBankInfoModel.getAccountNo());
                        olaVO.setPayingAccNo(cashToCashSundryBankInfoModel.getAccountNo());

                        AccountInfoModel franchiseAccountInfoModel = this.loadFranchiseAccountInfo(appUserModel.getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

                        switchWrapper.setToAccountNo(franchiseAccountInfoModel.getAccountNo());
                        switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise1CommissionAmount())));
                        olaVO.setReceivingAccNo(franchiseAccountInfoModel.getAccountNo());
                        olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise1CommissionAmount())));

                    }
                }
                switchWrapper.setOlavo(olaVO);


            } else if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(50018L)) {

                olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaVO.setReasonId(ReasonConstants.CUSTOMER_RETAIL_PAYMENT);

                StakeholderBankInfoModel retailPaymentSundryBankInfoModel = new StakeholderBankInfoModel();
                retailPaymentSundryBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.RETAIL_PAYMENT_SUNDRY_ACCOUNT_ID);
                searchBaseWrapper.setBasePersistableModel(retailPaymentSundryBankInfoModel);
                retailPaymentSundryBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();


                if (switchWrapper.getFtOrder() == 1) {
                    // FT: Customer to Sundry
                    switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
                    switchWrapper.setToAccountNo(retailPaymentSundryBankInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                    olaVO.setReceivingAccNo(retailPaymentSundryBankInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                } else if (switchWrapper.getFtOrder() == 2) {
                    // FT: Sundry to Agent
                    switchWrapper.setFromAccountNo(retailPaymentSundryBankInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(retailPaymentSundryBankInfoModel.getAccountNo());

                    SmartMoneyAccountModel agentSMA = switchWrapper.getWorkFlowWrapper().getRecipientSmartMoneyAccountModel();
                    AccountInfoModel agentAccountInfo = getAccountInfoModelBySmartMoneyAccount(agentSMA, switchWrapper.getWorkFlowWrapper().getReceiverAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
                    switchWrapper.setToAccountNo(agentAccountInfo.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount())));
                    olaVO.setReceivingAccNo(agentAccountInfo.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount())));
                } else if (switchWrapper.getFtOrder() == 3) {
                    // FT: Sundry to Commission Recon
                    switchWrapper.setFromAccountNo(retailPaymentSundryBankInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(retailPaymentSundryBankInfoModel.getAccountNo());

                    StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
                    commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
                    searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
                    commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
                    switchWrapper.setToAccountNo(commissionReconPoolBankInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));
                    olaVO.setReceivingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));

                } else if (switchWrapper.getFtOrder() == 4) {
                    // Franchise commission
                    switchWrapper.setFromAccountNo(retailPaymentSundryBankInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(retailPaymentSundryBankInfoModel.getAccountNo());

                    AccountInfoModel franchiseAccountInfoModel = this.loadFranchiseAccountInfo(switchWrapper.getWorkFlowWrapper().getReceiverAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

                    switchWrapper.setToAccountNo(franchiseAccountInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));
                    olaVO.setReceivingAccNo(franchiseAccountInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));

                }
                switchWrapper.setOlavo(olaVO);

            } else if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(2510727L)) {

                olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaVO.setReasonId(ReasonConstants.CUSTOMER_ZONG_TOP_UP);

                StakeholderBankInfoModel topupMirrorBankInfoModel = new StakeholderBankInfoModel();
                topupMirrorBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.TOPUP_POOL_MIRROR_ACCOUNT_ID);
                searchBaseWrapper.setBasePersistableModel(topupMirrorBankInfoModel);
                topupMirrorBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();


                if (switchWrapper.getFtOrder() == 1) {
                    // FT: Customer to Top Up Pool Account
                    switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
                    switchWrapper.setToAccountNo(topupMirrorBankInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                    olaVO.setReceivingAccNo(topupMirrorBankInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                }

                switchWrapper.setOlavo(olaVO);

            } else if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(ProductConstantsInterface.CUSTOMER_CREDIT_CARD)) {

                olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaVO.setReasonId(ReasonConstants.CREDIT_CARD_PAYMENT);

                StakeholderBankInfoModel creditCardBankInfoModel = new StakeholderBankInfoModel();
                creditCardBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.CREDIT_CARD_POOL_ACCOUNT_ID);
                searchBaseWrapper.setBasePersistableModel(creditCardBankInfoModel);
                creditCardBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();


                if (switchWrapper.getFtOrder() == 1) {
                    // FT: Customer to Credit Card Pool Account
                    switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
                    switchWrapper.setToAccountNo(creditCardBankInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                    olaVO.setReceivingAccNo(creditCardBankInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                }

                switchWrapper.setOlavo(olaVO);

            } else if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(ProductConstantsInterface.AGENT_CREDIT_CARD)) {

                olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaVO.setReasonId(ReasonConstants.CREDIT_CARD_PAYMENT);

                StakeholderBankInfoModel creditCardBankInfoModel = new StakeholderBankInfoModel();
                creditCardBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.CREDIT_CARD_POOL_ACCOUNT_ID);
                searchBaseWrapper.setBasePersistableModel(creditCardBankInfoModel);
                creditCardBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();


                if (switchWrapper.getFtOrder() == 1) {
                    // FT: Agent to Credit Card Pool Account
                    switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
                    olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
                    switchWrapper.setToAccountNo(creditCardBankInfoModel.getAccountNo());
                    switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                    olaVO.setReceivingAccNo(creditCardBankInfoModel.getAccountNo());
                    olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
                }

                switchWrapper.setOlavo(olaVO);

            }

            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());

            if (switchWrapper.getWorkFlowWrapper() != null) {
                switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
//	            	switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
                if (!StringUtil.isNullOrEmpty(accountInfoModel.getAccountNo())) {
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(StringUtil.replaceString(accountInfoModel.getAccountNo(), 5, "*"));
                }
            }

            switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;

    }

    //********************************************************************
    public SwitchWrapper customerExciseTaxPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        Long sTime = DateTimeUtils.currentTimeMillis();
        Long eTime;
        logger.info("The Start of [OLAVeriflyFinancialInstitutionImpl.customerExciseTaxPayment()] at the time :: " + String.valueOf(sTime));
        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();
        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();
        CommissionAmountsHolder commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();
        Boolean isInclusiveChargesIncluded = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : isInclusiveChargesIncluded;
        Long olaAccount = null;
        Long coreAccount = null;
        Long reasonId = null;
        Long productId = switchWrapper.getWorkFlowWrapper().getProductModel().getProductId();
        if (productId.equals(ProductConstantsInterface.AGENT_EXCISE_AND_TAXATION)) {
            reasonId = ReasonConstants.AGENT_EXCISE_AND_TAXATION_PAYMENT;
            olaAccount = PoolAccountConstantsInterface.AGENT_EXCISE_AND_TAXATION_PAYMENT_POOL_ACCOUNT;
            coreAccount = PoolAccountConstantsInterface.AGENT_EXCISE_AND_TAXATION_PAYMENT_ACCOUNT_GL;
        }
        SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
        AccountInfoModel customerAccountInfo = veriflyManager.getAccountInfoModel(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), customerSMA.getPaymentModeId());
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
        Double totalExclCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount());
        // Debt Customer
        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setIsAgent(false);
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        if (customerSMA.getPaymentModeId() == 7l)
            olaInfo.setCustomerAccountTypeId(4l);
        else
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        olaInfo.setReasonId(reasonId);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
        olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + totalExclCharges);
        switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());
        debitList.add(olaInfo);
        if (isInclusiveChargesIncluded != null && isInclusiveChargesIncluded && totalInclusiveCharges > 0.0) {
            //Dr Inclusive Charges Sundry A/C
            olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    reasonId,
                    productId,
                    totalInclusiveCharges
            );
            debitList.add(olaInfo);
            totalInclusiveCharges = 0.0;
        }
        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);
        Double collectionPoolCreditAmount = commissionAmountsHolder.getTransactionAmount();
        if (!isInclusiveChargesIncluded && totalInclusiveCharges != null) {
            collectionPoolCreditAmount -= totalInclusiveCharges;
        }
        collectionPoolCreditAmount = (collectionPoolCreditAmount == null ? 0.0D : collectionPoolCreditAmount);
        //Cr Collection Pool
        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                reasonId,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                olaAccount,
                coreAccount,
                collectionPoolCreditAmount
        );
        creditList.add(olaInfo);
        String trxCode = switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode();
        Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
        //Cr Agent A/C
        AccountInfoModel accountInfoModel = veriflyManager.getAccountInfoModel(ThreadLocalAppUser.getAppUserModel().getAppUserId(),
                switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getPaymentModeId());
        olaInfo = new OLAInfo();
        olaInfo.setIsAgent(true);
        if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
            olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
            olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
        }
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
        olaInfo.setReasonId(reasonId);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());
        olaInfo.setBalance(agent1CommAmount);

        switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
        creditList.add(olaInfo);
        Double amount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges;
        //
        olaVO.setFromSegmentId(switchWrapper.getWorkFlowWrapper().getFromSegmentId());
        olaVO.setToSegmentId(switchWrapper.getWorkFlowWrapper().getToSegmentId());
        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
        switchWrapper.setOlavo(olaVO);
        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
        switchWrapper.setBankId(customerSMA.getBankId());
        switchWrapper.setPaymentModeId(customerSMA.getPaymentModeId());
        switchWrapper.setTransactionAmount(collectionPoolCreditAmount);
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
        switchWrapper.getWorkFlowWrapper().setTransactionModel(switchWrapper.getTransactionTransactionModel());
        switchWrapper.setSkipPostedTrxEntry(true);
        eTime = DateTimeUtils.currentTimeMillis();
        logger.info("Going to call prepareCommissionCreditList() from  [OLAVeriflyFinancialInstitutionImpl.customerExciseTaxPayment()] at the time :: " + String.valueOf(eTime));
        this.prepareCommissionCreditList(switchWrapper, creditList, reasonId);
        logger.info("Received result from prepareCommissionCreditList() in  [OLAVeriflyFinancialInstitutionImpl.customerExciseTaxPayment()] at the time :: " + String.valueOf(DateTimeUtils.currentTimeMillis()));
        logger.info("Time taken by prepareCommissionCreditList() in  [OLAVeriflyFinancialInstitutionImpl.customerExciseTaxPayment()] at the time :: " + String.valueOf(DateTimeUtils.currentTimeMillis() - eTime));
        olaVO.setCreditAccountList(creditList);
        olaVO.setDebitAccountList(debitList);
        if (switchWrapper.getWorkFlowWrapper() != null &&
                switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null
                && StringUtil.isNullOrEmpty(switchWrapper.getOlavo().getMicrobankTransactionCode())) {

            switchWrapper.getOlavo().setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        }
        eTime = DateTimeUtils.currentTimeMillis();
        logger.info("Going to call switchController.debitCreditAccount() from  [OLAVeriflyFinancialInstitutionImpl.customerExciseTaxPayment()] at the time :: " + String.valueOf(eTime));
        this.switchController.debitCreditAccount(switchWrapper);
        logger.info("Received result from switchController.debitCreditAccount() in  [OLAVeriflyFinancialInstitutionImpl.customerExciseTaxPayment()] at the time :: " + String.valueOf(DateTimeUtils.currentTimeMillis()));
        logger.info("Time taken by switchController.debitCreditAccount() in  [OLAVeriflyFinancialInstitutionImpl.customerExciseTaxPayment()] at the time :: " + String.valueOf(DateTimeUtils.currentTimeMillis() - eTime));
        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setRecipientAccountNo(customerAccountInfo.getAccountNo());
        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setSenderAgentAccountNo(accountInfoModel.getAccountNo());
        switchWrapper.getWorkFlowWrapper().setFromRetailerContactModel(switchWrapper.getWorkFlowWrapper().getRetailerContactModel());
        eTime = DateTimeUtils.currentTimeMillis();
        logger.info("The End of [OLAVeriflyFinancialInstitutionImpl.customerExciseTaxPayment()] at the time :: " + String.valueOf(eTime));
        logger.info("Total time taken to execute [OLAVeriflyFinancialInstitutionImpl.customerExciseTaxPayment()] :: " + String.valueOf(eTime - sTime));
        return switchWrapper;
    }

    public SwitchWrapper agentCollectionPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {


        Long sTime = DateTimeUtils.currentTimeMillis();
        Long eTime;
        logger.info("The Start of [OLAVeriflyFinancialInstitutionImpl.agentCollectionPayment()] at the time :: " + String.valueOf(sTime));
        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();

        Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));

        Long retailerAccountTypeId = switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId();

        Long customerAccountTypeId = 0L;
        if (switchWrapper.getWorkFlowWrapper().getCustomerModel() != null) {
            customerAccountTypeId = switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId();
        }

        SmartMoneyAccountModel customerSMAModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        AccountInfoModel customerAccountInfoModel = new AccountInfoModel();
        if (switchWrapper.getWorkFlowWrapper().getCustomerModel() != null) {
            customerAccountInfoModel = veriflyManager.getAccountInfoModel(workFlowWrapper.getCustomerModel().getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        }
        AccountInfoModel retailerAccountInfoModel = veriflyManager.getAccountInfoModel(workFlowWrapper.getAppUserModel().getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

        switchWrapper.setAccountInfoModel(retailerAccountInfoModel);

        CommissionAmountsHolder commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();

        Boolean isInclusiveChargesIncluded = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : isInclusiveChargesIncluded;
        Long olaAccount = null;
        Long coreAccount = null;
        Long reasonId = null;
        Long productId = switchWrapper.getWorkFlowWrapper().getProductModel().getProductId();
        if (productId.equals(ProductConstantsInterface.AGENT_ET_COLLECTION)) {
            reasonId = ReasonConstants.AGENT_ET_COLLECTION;
            olaAccount = PoolAccountConstantsInterface.AGENT_ET_COLLECTION_POOL_ACCOUNT;
            coreAccount = PoolAccountConstantsInterface.AGENT_ET_COLLECTION_ACCOUNT_GL;
        } else if ((productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION)) || (productId.equals(ProductConstantsInterface.AGENT_KP_CHALLAN_COLLECTION_BY_ACCOUNT))) {
            reasonId = ReasonConstants.AGENT_KP_CHALLAN_COLLECTION;
            olaAccount = PoolAccountConstantsInterface.AGENT_KP_COLLECTION_POOL_ACCOUNT;
            coreAccount = PoolAccountConstantsInterface.AGENT_KP_COLLECTION_ACCOUNT_GL;
        } else if (productId.equals(ProductConstantsInterface.AGENT_LICENSE_FEE_COLLECTION)) {
            reasonId = ReasonConstants.AGENT_LICENSE_FEE_COLLECTION;
            olaAccount = PoolAccountConstantsInterface.AGENT_LICENSE_FEE_COLLECTION_POOL_ACCOUNT;
            coreAccount = PoolAccountConstantsInterface.AGENT_LICENSE_FEE_COLLECTION_ACCOUNT_GL;
        } else if (productId.equals(ProductConstantsInterface.AGENT_BALUCHISTAN_ET_COLLECTION)) {
            reasonId = ReasonConstants.AGENT_BALUCHSITAN_ET_COLLECTION;
            olaAccount = PoolAccountConstantsInterface.AGENT_BALUCHISTAN_ET_COLLECTION_POOL_ACCOUNT;
            coreAccount = PoolAccountConstantsInterface.AGENT_BALCUHSITAN_ET_COLLECTION_ACCOUNT_GL;
        } else if (productId.equals(ProductConstantsInterface.AGENT_E_LEARNING_MANAGEMENT_SYSTEM)) {
            reasonId = ReasonConstants.AGENT_E_LEARNING_MANAGEMENT_SYSTEM;
            olaAccount = PoolAccountConstantsInterface.AGENT_E_LEARNING_MANAGEMENT_SYSTEM_POOL_ACCOUNT;
            coreAccount = PoolAccountConstantsInterface.AGENT_E_LEARNING_MANAGEMENT_SYSTEM_ACCOUNT_GL;
        } else if (productId.equals(ProductConstantsInterface.SARHAD_UNIVERSITY_COLLECTION)) {
            reasonId = ReasonConstants.COLLECTION_PAYMENT;
            olaAccount = PoolAccountConstantsInterface.SARHAD_UNIVERSITY_COLLECTION_POOL_ACCOUNT;
            coreAccount = PoolAccountConstantsInterface.SARHAD_UNIVERSITY_COLLECTION_ACCOUNT_GL;
        }
        else if ((productId.equals(ProductConstantsInterface.AGENT_VALLENCIA_COLLECTION))) {
            reasonId = ReasonConstants.AGENT_VALENCIA_COLLECTION;
            olaAccount = PoolAccountConstantsInterface.COLLECTION_POOL_ACCOUNT_OLA;
            coreAccount = PoolAccountConstantsInterface.COLLECTION_POOL_ACCOUNT_CORE;
        }
        else {


            reasonId = ReasonConstants.COLLECTION_PAYMENT;
            olaAccount = PoolAccountConstantsInterface.COLLECTION_POOL_ACCOUNT_OLA;
            coreAccount = PoolAccountConstantsInterface.COLLECTION_POOL_ACCOUNT_CORE;
        }

        //Customer Debit in case of KP Challan via account
        if (switchWrapper.getWorkFlowWrapper().getCustomerAppUserModel() != null) {
            OLAInfo customerOLAInfo = new OLAInfo();
            customerOLAInfo.setReasonId(reasonId);
            customerOLAInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
            customerOLAInfo.setCustomerAccountTypeId(customerAccountTypeId);
            customerOLAInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            customerOLAInfo.setPayingAccNo(customerAccountInfoModel.getAccountNo());
            customerOLAInfo.setIsAgent(Boolean.FALSE);
            customerOLAInfo.setBalance(commissionAmountsHolder.getTotalAmount());
            customerOLAInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);

            debitList.add(customerOLAInfo);

        } else {
            //Dr Agent
            OLAInfo agentOLAInfo = new OLAInfo();
            agentOLAInfo.setReasonId(reasonId);
            agentOLAInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
            agentOLAInfo.setCustomerAccountTypeId(retailerAccountTypeId);

            agentOLAInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            agentOLAInfo.setPayingAccNo(retailerAccountInfoModel.getAccountNo());
            agentOLAInfo.setIsAgent(Boolean.TRUE);
            agentOLAInfo.setBalance(commissionAmountsHolder.getTotalAmount() - agent1CommAmount);
            agentOLAInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);

            debitList.add(agentOLAInfo);
        }

        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(commissionAmountsHolder.getInclusiveFixAmount())
                + CommonUtils.getDoubleOrDefaultValue(commissionAmountsHolder.getInclusivePercentAmount());

        //Dr 3rd Party Inclusive Account
        if (isInclusiveChargesIncluded && totalInclusiveCharges > 0) {

            OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    reasonId,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            if (olaInfo.getCoreStakeholderBankInfoId() != null && olaInfo.getCoreStakeholderBankInfoId() > 0L && !StringUtil.isNullOrEmpty(olaInfo.getPayingAccNo())) {
                debitList.add(olaInfo);
            }
        }

        Double collectionPoolCreditAmount = commissionAmountsHolder.getTransactionAmount();

        if (!isInclusiveChargesIncluded && totalInclusiveCharges != null) {
            collectionPoolCreditAmount -= totalInclusiveCharges;
        }

        collectionPoolCreditAmount = (collectionPoolCreditAmount == null ? 0.0D : collectionPoolCreditAmount);

        //Cr Collection Pool
        OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                reasonId,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                olaAccount,
                coreAccount,
                collectionPoolCreditAmount
        );

        creditList.add(olaInfo);

        if (switchWrapper.getWorkFlowWrapper().getCustomerAppUserModel() != null) {
            olaInfo = new OLAInfo();
            olaInfo.setReasonId(reasonId);
            olaInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
            olaInfo.setCustomerAccountTypeId(retailerAccountTypeId);

            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setPayingAccNo(retailerAccountInfoModel.getAccountNo());
            olaInfo.setIsAgent(Boolean.TRUE);
            olaInfo.setAgentBalanceAfterTrxRequired(Boolean.TRUE);
            olaInfo.setBalance(agent1CommAmount);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            creditList.add(olaInfo);
        }

        olaVO.setFromSegmentId(switchWrapper.getWorkFlowWrapper().getFromSegmentId());
        olaVO.setToSegmentId(switchWrapper.getWorkFlowWrapper().getToSegmentId());
        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());

        switchWrapper.setOlavo(olaVO);
        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
        switchWrapper.setBankId(customerSMAModel.getBankId());
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
        switchWrapper.setTransactionAmount(collectionPoolCreditAmount);
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());

        switchWrapper.getWorkFlowWrapper().setTransactionModel(switchWrapper.getTransactionTransactionModel());

        switchWrapper.setSkipPostedTrxEntry(true);

        eTime = DateTimeUtils.currentTimeMillis();
        logger.info("Going to call prepareCommissionCreditList() from  [OLAVeriflyFinancialInstitutionImpl.agentCollectionPayment()] at the time :: " + String.valueOf(eTime));
        this.prepareCommissionCreditList(switchWrapper, creditList, reasonId);
        logger.info("Received result from prepareCommissionCreditList() in  [OLAVeriflyFinancialInstitutionImpl.agentCollectionPayment()] at the time :: " + String.valueOf(DateTimeUtils.currentTimeMillis()));
        logger.info("Time taken by prepareCommissionCreditList() in  [OLAVeriflyFinancialInstitutionImpl.agentCollectionPayment()] at the time :: " + String.valueOf(DateTimeUtils.currentTimeMillis() - eTime));


        olaVO.setCreditAccountList(creditList);
        olaVO.setDebitAccountList(debitList);

        if (switchWrapper.getWorkFlowWrapper() != null &&
                switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null
                && StringUtil.isNullOrEmpty(switchWrapper.getOlavo().getMicrobankTransactionCode())) {

            switchWrapper.getOlavo().setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        }

        eTime = DateTimeUtils.currentTimeMillis();
        logger.info("Going to call switchController.debitCreditAccount() from  [OLAVeriflyFinancialInstitutionImpl.agentCollectionPayment()] at the time :: " + String.valueOf(eTime));
        this.switchController.debitCreditAccount(switchWrapper);
        logger.info("Received result from switchController.debitCreditAccount() in  [OLAVeriflyFinancialInstitutionImpl.agentCollectionPayment()] at the time :: " + String.valueOf(DateTimeUtils.currentTimeMillis()));
        logger.info("Time taken by switchController.debitCreditAccount() in  [OLAVeriflyFinancialInstitutionImpl.agentCollectionPayment()] at the time :: " + String.valueOf(DateTimeUtils.currentTimeMillis() - eTime));


        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setSenderAgentAccountNo(retailerAccountInfoModel.getAccountNo());

        eTime = DateTimeUtils.currentTimeMillis();
        logger.info("The End of [OLAVeriflyFinancialInstitutionImpl.agentCollectionPayment()] at the time :: " + String.valueOf(eTime));
        logger.info("Total time taken to execute [OLAVeriflyFinancialInstitutionImpl.agentCollectionPayment()] :: " + String.valueOf(eTime - sTime));

        return switchWrapper;


    }


    //********************************************************************


    private AccountModel loadAccountModel(String accountNumber) throws FrameworkCheckedException {

        AccountModel _accountModel = null;

        try {

            String _accountNumber = EncryptionUtil.encryptAccountNo(accountNumber);

            List<AccountModel> accountModelList =
                    this.genericDao.findByHQL("from com.inov8.integration.common.model.AccountModel accountModel where accountModel.accountNumber = '" + _accountNumber + "'");

            if (accountModelList != null && accountModelList.size() > 0) {
                _accountModel = accountModelList.get(0);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return _accountModel;
    }


    /* (non-Javadoc)
     * @see com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution#checkDebitCreditLimitOLAVO(com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper)
     */
    public OLAVO checkDebitCreditLimitOLAVO(WorkFlowWrapper _workFlowWrapper) throws FrameworkCheckedException, Exception {
        WorkFlowWrapper workFlowWrapper = _workFlowWrapper;
        Long productId = workFlowWrapper.getProductModel().getProductId();
        Boolean isAgent = workFlowWrapper.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue() ? Boolean.TRUE : Boolean.FALSE;
        double totalExclCharges = 0.0D;
        double transactionAmount = workFlowWrapper.getTransactionModel().getTransactionAmount();
        double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getInclusivePercentAmount());
        double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
        double balance = 0.0;
        Long segmentId = null;
        CustomerModel customerModel = null;
        AccountInfoModel accountInfoModel = null;
        RetailerContactModel retailerContactModel = null;
        OLAVO olaVO = new OLAVO();
        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        if (isAgent) {
            segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
            accountInfoModel = veriflyManager.getAccountInfoModel(workFlowWrapper.getAppUserModel().getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            if (workFlowWrapper.getRetailerContactModel() != null)
                retailerContactModel = workFlowWrapper.getRetailerContactModel();
            else {
                retailerContactModel = new RetailerContactModel();
                retailerContactModel.setRetailerContactId(workFlowWrapper.getAppUserModel().getRetailerContactId());
                retailerContactModel = (RetailerContactModel) this.genericDao.getEntityByPrimaryKey(retailerContactModel);
            }
            olaVO.setCustomerAccountTypeId(retailerContactModel.getOlaCustomerAccountTypeModelId());
            if (workFlowWrapper.getHandlerAppUserModel() != null) {
                if (workFlowWrapper.getHandlerAppUserModel().getHandlerIdHandlerModel() != null) {
                    olaVO.setHandlerAccountTypeId(workFlowWrapper.getHandlerAppUserModel().getHandlerIdHandlerModel().getAccountTypeId());
                }
                olaVO.setHandlerId(workFlowWrapper.getHandlerAppUserModel().getHandlerId());
            }

        } else if (!isAgent) {
            customerModel = workFlowWrapper.getCustomerModel();
            if (customerModel == null && workFlowWrapper.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
                customerModel = new CustomerModel();
                customerModel.setCustomerId(workFlowWrapper.getAppUserModel().getCustomerId());
                customerModel = this.genericDao.getEntityByPrimaryKey(customerModel);
            }
            segmentId = customerModel.getSegmentId();
            Long paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
            if (_workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.HRA_CASH_WITHDRAWAL)) {
                olaVO.setCustomerAccountTypeId(CustomerAccountTypeConstants.HRA);
                paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
            } else
                olaVO.setCustomerAccountTypeId(customerModel.getCustomerAccountTypeId());
            accountInfoModel = veriflyManager.getAccountInfoModel(workFlowWrapper.getAppUserModel().getCustomerId(), paymentModeId);
        }
        if (workFlowWrapper.getTransactionCodeModel() != null) {
            olaInfo.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
        } else {
            olaInfo.setMicrobankTransactionCode("111111111111");
        }

        AccountModel accountModel = this.loadAccountModel(accountInfoModel.getAccountNo());

        olaVO.setFromSegmentId(segmentId);
        olaVO.setAccountId(accountModel.getAccountId());
        olaVO.setProductId(workFlowWrapper.getProductModel().getProductId());
        olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());
        olaInfo.setReceivingAccNo(accountInfoModel.getAccountNo());


        if (UtilityCompanyEnum.contains(String.valueOf(productId)) || NadraCompanyEnum.contains(String.valueOf(productId)) || InternetCompanyEnum.contains(String.valueOf(productId))) {

            Boolean isCustomerTransaction = (Boolean) workFlowWrapper.getObject("misc");

            olaInfo.setReasonId(ReasonConstants.BILL_PAYMENT);
            olaVO.setReasonId(ReasonConstants.BILL_PAYMENT);

            totalExclCharges = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getExclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getExclusivePercentAmount());

            if (!isAgent) {
                balance = transactionAmount + totalExclCharges;
                olaVO.setTransactionTypeId(TransactionTypeConstants.DEBIT.toString());
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
                debitList.add(olaInfo);

            } else if (isAgent) {
                if (!isCustomerTransaction) {

                    balance = (transactionAmount + totalExclCharges) - agent1CommAmount;
                    olaVO.setTransactionTypeId(TransactionTypeConstants.DEBIT.toString());
                    olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
                    debitList.add(olaInfo);

                } else {

                    balance = agent1CommAmount;
                    olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString());
                    olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
                    creditList.add(olaInfo);
                }
            }

            switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());


        } else if (productId.longValue() == TransactionProductEnum.CASH_WITHDRAWL_PRODUCT.getProductId() || productId.longValue() == TransactionProductEnum.HRA_CASH_WITHDRAWL_PRODUCT.getProductId()) {

            olaInfo.setReasonId(ReasonConstants.CASH_WITHDRAWAL);
            olaVO.setReasonId(ReasonConstants.CASH_WITHDRAWAL);

            totalExclCharges = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount());

            if (!isAgent) {
                balance = transactionAmount + totalExclCharges;
                olaVO.setTransactionTypeId(TransactionTypeConstants.DEBIT.toString());
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
                olaVO.setFromSegmentId(segmentId);
                debitList.add(olaInfo);

            } else if (isAgent) {
                balance = transactionAmount + agent1CommAmount - totalInclusiveCharges;
                olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString());
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
                olaVO.setToSegmentId(segmentId);
                creditList.add(olaInfo);
            }

            switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());


        } else if (workFlowWrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_CASH_OUT_TX)) {
            olaInfo.setReasonId(ReasonConstants.CASH_WITHDRAWAL);
            olaVO.setReasonId(ReasonConstants.CASH_WITHDRAWAL);
            totalExclCharges = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount());
            balance = transactionAmount + agent1CommAmount - totalInclusiveCharges;
            olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString());
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaVO.setToSegmentId(segmentId);
            creditList.add(olaInfo);
            switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
        } else if (workFlowWrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CASH_OUT_TX)) {
            olaInfo.setReasonId(ReasonConstants.BOP_CASH_OUT);
            olaVO.setReasonId(ReasonConstants.BOP_CASH_OUT);
            balance = transactionAmount + agent1CommAmount - totalInclusiveCharges;
            olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString());
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaVO.setToSegmentId(segmentId);
            creditList.add(olaInfo);
            switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
        } else if (workFlowWrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING_TX)) {
            olaInfo.setReasonId(ReasonConstants.THIRD_PARTY_ACCOUNT_OPENING);
            olaVO.setReasonId(ReasonConstants.THIRD_PARTY_ACCOUNT_OPENING);
            balance = transactionAmount + agent1CommAmount - totalInclusiveCharges;
            olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString());
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaVO.setToSegmentId(segmentId);
            creditList.add(olaInfo);
            switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
        } else if (workFlowWrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CARD_ISSUANCE_REISSUANCE_TX)) {
            olaInfo.setReasonId(ReasonConstants.BOP_CARD_ISSUANCE_REISSUANCE);
            olaVO.setReasonId(ReasonConstants.BOP_CARD_ISSUANCE_REISSUANCE);
            balance = transactionAmount + agent1CommAmount - totalInclusiveCharges;
            olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString());
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaVO.setToSegmentId(segmentId);
            creditList.add(olaInfo);
            switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
        } else if (workFlowWrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.PROOF_OF_LIFE_TX)) {
            olaInfo.setReasonId(ReasonConstants.PROOF_OF_LIFE);
            olaVO.setReasonId(ReasonConstants.PROOF_OF_LIFE);
            balance = transactionAmount + agent1CommAmount - totalInclusiveCharges;
            olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString());
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaVO.setToSegmentId(segmentId);
            creditList.add(olaInfo);
            switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
        } else if (workFlowWrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.ADVANCE_SALARY_LOAN_TX)) {
            olaInfo.setReasonId(ReasonConstants.ADVANCE_SALARY_LOAN);
            olaVO.setReasonId(ReasonConstants.ADVANCE_SALARY_LOAN);
            totalExclCharges = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount());
            balance = transactionAmount + agent1CommAmount - totalInclusiveCharges;
            olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString());
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaVO.setToSegmentId(segmentId);
            creditList.add(olaInfo);
            switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
        } else if (productId.longValue() == ProductConstantsInterface.BB_TO_CORE_ACCOUNT.longValue()) {

            olaInfo.setReasonId(ReasonConstants.BB_TO_CORE_AC);
            olaVO.setReasonId(ReasonConstants.BB_TO_CORE_AC);

            if (!isAgent) {
                balance = workFlowWrapper.getCommissionAmountsHolder().getTotalAmount();
                olaVO.setTransactionTypeId(TransactionTypeConstants.DEBIT.toString());
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
                debitList.add(olaInfo);

            } else if (isAgent) {
                balance = agent1CommAmount;
                olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString());
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
                creditList.add(olaInfo);
            }

            olaInfo.setReasonId(ReasonConstants.BB_TO_CORE_AC);
            olaVO.setReasonId(ReasonConstants.BB_TO_CORE_AC);
            olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());


        } else if (productId.longValue() == TransactionProductEnum.P2P_TRANSFER_PRODUCT.getProductId()) {

            olaInfo.setReasonId(ReasonConstants.ACCOUNT_TO_ACCOUNT);
            olaVO.setReasonId(ReasonConstants.ACCOUNT_TO_ACCOUNT);

            Boolean isRecipient = (Boolean) workFlowWrapper.getObject("misc");

            if (!isAgent && !isRecipient) {
                balance = workFlowWrapper.getTransactionModel().getTotalAmount();
                olaVO.setTransactionTypeId(TransactionTypeConstants.DEBIT.toString());
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
                debitList.add(olaInfo);

            } else if (!isAgent && isRecipient) {
                balance = transactionAmount - totalInclusiveCharges;
                olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString());
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
                creditList.add(olaInfo);

            } else if (isAgent) {
                balance = agent1CommAmount;
                olaInfo.setReasonId(ReasonConstants.ACCOUNT_TO_ACCOUNT);
                olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString());
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
                creditList.add(olaInfo);
            }


        } else if (productId == TransactionProductEnum.AGENT_RETAIL_PAYMENT_PRODUCT.getProductId()) {

            olaInfo.setReasonId(ReasonConstants.AGENT_RETAIL_PAYMENT);
            olaVO.setReasonId(ReasonConstants.AGENT_RETAIL_PAYMENT);

            totalExclCharges = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getExclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getExclusivePercentAmount());

            if (!isAgent) {
                balance = workFlowWrapper.getTransactionModel().getTotalAmount();
                olaVO.setTransactionTypeId(TransactionTypeConstants.DEBIT.toString());
                debitList.add(olaInfo);

            } else if (isAgent) {
                balance = (transactionAmount - totalInclusiveCharges);
                olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString());
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
                creditList.add(olaInfo);
            }


        } else if (productId.longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH.longValue()) {

            olaInfo.setReasonId(ReasonConstants.CUSTOMER_ACCOUNT_TO_CASH);
            olaVO.setReasonId(ReasonConstants.CUSTOMER_ACCOUNT_TO_CASH);

            totalExclCharges = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getExclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getExclusivePercentAmount());

            if (!isAgent) {
                balance = workFlowWrapper.getTransactionModel().getTotalAmount();
                olaVO.setTransactionTypeId(TransactionTypeConstants.DEBIT.toString());
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
                debitList.add(olaInfo);

            } else if (isAgent) {
                balance = (agent1CommAmount);
                olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString());
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
                creditList.add(olaInfo);
            }
        }


        olaInfo.setIsAgent(isAgent);
        olaInfo.setBalance(balance);
        olaVO.setBalance(balance);
        olaVO.setTransactionDateTime(new Date());
        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);
        switchWrapper.setWorkFlowWrapper(workFlowWrapper);
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        switchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);

        logger.info("\nVerify Limits ...\nProduct "
                + (StringUtil.isNullOrEmpty(workFlowWrapper.getProductModel().getName()) ? workFlowWrapper.getProductModel().getName() : workFlowWrapper.getProductModel().getProductId())
                + "\nBalance " + switchWrapper.getOlavo().getBalance() + "\nType     " + workFlowWrapper.getAppUserModel().getAppUserTypeId());

        switchController.verifyLimits(switchWrapper);

        return olaVO;
    }

    /**
     * Handles multiple debit and multiple credits in OLA
     *
     * @param switchWrapper
     * @return
     * @throws FrameworkCheckedException
     * @throws Exception
     */
    public SwitchWrapper transferFunds(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {
        logger.info("start of OLAVeriflyFinancialInstitutionImpl.transferFunds() :: " + new Date());
        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());

        switchWrapper.putObject(CommandFieldConstants.KEY_PIN, switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
        AccountInfoModel accountInfoModel = null;
        long productId = switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().longValue();

        if (productId != ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue()
                && productId != ProductConstantsInterface.TELLER_ACCOUNT_HOLDER_CASH_IN.longValue()
                && productId != ProductConstantsInterface.TELLER_CASH_OUT.longValue()
                && productId != ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_ON_US
                && productId != ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US
                && productId != ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL
                && productId != ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US
                && productId != ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL) {

            // user verifly pin is being validated through VerifyPinCommand before calling any call which needs pin validation ...  not required to check PIN again.
            //switchWrapper = this.verifyCredentialsWithoutPin(switchWrapper);
            //Change for slowness issue
            Long customerId = null;
            if (ThreadLocalAppUser.getAppUserModel().getCustomerId() != null)
                customerId = ThreadLocalAppUser.getAppUserModel().getCustomerId();
            else
                customerId = ThreadLocalAppUser.getAppUserModel().getAppUserId();

            accountInfoModel = veriflyManager.getAccountInfoModel(customerId, smartMoneyAccountModel.getName());
            if (accountInfoModel == null) {
                logger.error("Could not load AccountInfo in OLAVeriflyFinancialInstitution.transferFunds()");
                throw new InvalidDataException(String.valueOf(FailureReasonConstants.RECORD_IS_INACTIVE));
            }
            switchWrapper.setAccountInfoModel(accountInfoModel);
        }
        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();

        if (productId == TransactionProductEnum.INITIAL_DEPOSIT_PRODUCT.getProductId()) {
            prepareSwitchWrapperForInitialDeposit(switchWrapper, debitList, creditList);
        } else if (productId == TransactionProductEnum.CASH_DEPOSIT_PRODUCT.getProductId()) {
            prepareSwitchWrapperForCashDeposit(switchWrapper, debitList, creditList);
        } else if (productId == TransactionProductEnum.AGENT_CASH_DEPOSIT_PRODUCT.getProductId()) {
            prepareSwitchWrapperForAgentCashDeposit(switchWrapper, debitList, creditList);
        } else if (productId == TransactionProductEnum.CASH_WITHDRAWL_CUSTOMER_PRODUCT.getProductId()) {
            prepareSwitchWrapperForCustomerCashWithdrawal(switchWrapper, debitList, creditList);
        } else if (productId == ProductConstantsInterface.CUSTOMER_BB_TO_CORE_ACCOUNT.longValue()) {
            prepareSwitchWrapperForCustomerBBToCore(switchWrapper, debitList, creditList);
        } else if (productId == ProductConstantsInterface.CUSTOMER_BB_TO_IBFT.longValue()) {
            prepareSwitchWrapperForCustomerBBToCore(switchWrapper, debitList, creditList);
        }else if (productId==ProductConstantsInterface.RELIEF_FUND_PRODUCT.longValue()){
            prepareSwitchWrapperForfundCustomerBBToCore(switchWrapper,debitList,creditList);

        }

        else if (productId == TransactionProductEnum.RETAIL_PAYMENT_PRODUCT.getProductId()) {
            prepareSwitchWrapperForCustomerRetailPayment(switchWrapper, debitList, creditList);
        } else if (productId == TransactionProductEnum.CASH_TO_CASH_PRODUCT.getProductId()) {
            prepareSwitchWrapperForCashToCash(switchWrapper, debitList, creditList);
        } else if (productId == TransactionProductEnum.CUST_INIT_ACCOUNT_TO_ACCOUNT_PRODUCT.getProductId()) {
            prepareSwitchWrapperForCustomerAccToAcc(switchWrapper, debitList, creditList);
        } else if (productId == TransactionProductEnum.P2P_TRANSFER_PRODUCT.getProductId()) {
            //Dr Sender Customer A/C
            SmartMoneyAccountModel senderCustomerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
            AccountInfoModel senderCustomerAccountInfo = getAccountInfoModelBySmartMoneyAccount(senderCustomerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            OLAInfo olaInfo = new OLAInfo();
            olaInfo.setIsAgent(false);
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            olaInfo.setReasonId(ReasonConstants.ACCOUNT_TO_ACCOUNT);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            olaInfo.setPayingAccNo(senderCustomerAccountInfo.getAccountNo());
            olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount());

            switchWrapper.setFromAccountNo(senderCustomerAccountInfo.getAccountNo());

            debitList.add(olaInfo);


            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.ACCOUNT_TO_ACCOUNT,
                        productId,
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;
            }

            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);


            //Cr Recipient Customer A/C
            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getRecipientSmartMoneyAccountModel();
            AccountInfoModel recipientCustomerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getRecipientCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            olaInfo = new OLAInfo();
            olaInfo.setIsAgent(false);
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRecipientCustomerModel().getCustomerAccountTypeId());
            olaInfo.setReasonId(ReasonConstants.ACCOUNT_TO_ACCOUNT);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setPayingAccNo(recipientCustomerAccountInfo.getAccountNo());

            olaInfo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges)));

            switchWrapper.setToAccountNo(recipientCustomerAccountInfo.getAccountNo());
            creditList.add(olaInfo);


            //Cr Agent Account
            olaInfo = new OLAInfo();
            olaInfo.setIsAgent(true);
            if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
            }
            olaInfo.setBalanceAfterTrxRequired(false);
            olaInfo.setAgentBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            olaInfo.setReasonId(ReasonConstants.ACCOUNT_TO_ACCOUNT);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());

            Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));

            olaInfo.setBalance(agent1CommAmount);

            creditList.add(olaInfo);


            // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.ACCOUNT_TO_ACCOUNT);

        } else if (productId == ProductConstantsInterface.BB_TO_CORE_ACCOUNT) {

            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
            AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            //Dr Customer A/C
            OLAInfo olaInfo = new OLAInfo();
            olaInfo.setIsAgent(false);
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            olaInfo.setReasonId(ReasonConstants.BB_TO_CORE_AC);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());

//			Double totalExclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getExclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getExclusivePercentAmount());

            olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount());


            switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());

            debitList.add(olaInfo);

            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.CNIC_TO_CORE_AC,
                        productId,
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;
            }

            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

            //Cr T24 Settlement A/C
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.BB_TO_CORE_AC,
                    TransactionTypeConstantsInterface.OLA_CREDIT,
                    PoolAccountConstantsInterface.T24_SETTLEMENT_ACCOUNT_ID,
                    null,
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges
            );

            creditList.add(olaInfo);


            //Cr Agent Account
            olaInfo = new OLAInfo();
            olaInfo.setIsAgent(true);
            if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
            }
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            olaInfo.setReasonId(ReasonConstants.BB_TO_CORE_AC);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());

            Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));

            olaInfo.setBalance(agent1CommAmount);

            switchWrapper.setToAccountNo(accountInfoModel.getAccountNo());
            creditList.add(olaInfo);

            // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.BB_TO_CORE_AC);

        } else if (productId == ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT) {

            //Dr Agent A/C
            OLAInfo olaInfo = new OLAInfo();
            olaInfo.setIsAgent(true);
            if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
            }
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            olaInfo.setReasonId(ReasonConstants.CNIC_TO_CORE_AC);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());

//			Double totalExclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getExclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getExclusivePercentAmount());

            Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
            olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() - agent1CommAmount);

            SmartMoneyAccountModel agentSMA = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();
            AccountInfoModel agentAccountInfo = getAccountInfoModelBySmartMoneyAccount(agentSMA, switchWrapper.getWorkFlowWrapper().getAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            switchWrapper.setFromAccountNo(agentAccountInfo.getAccountNo());

            debitList.add(olaInfo);


            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.CNIC_TO_CORE_AC,
                        productId,
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;
            }

            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

            //Cr T24 Settlement A/C
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.CNIC_TO_CORE_AC,
                    TransactionTypeConstantsInterface.OLA_CREDIT,
                    PoolAccountConstantsInterface.T24_SETTLEMENT_ACCOUNT_ID,
                    null,
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges
            );

            creditList.add(olaInfo);

            // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.CNIC_TO_CORE_AC);

        } else if (productId == TransactionProductEnum.CNIC_TO_BB_ACCOUNT_PRODUCT.getProductId()) {

            //Dr Agent A/C
            OLAInfo olaInfo = new OLAInfo();
            olaInfo.setIsAgent(true);
            if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
            }
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            olaInfo.setReasonId(ReasonConstants.CNIC_TO_BB_AC);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());

//			Double totalExclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getExclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getExclusivePercentAmount());

            Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
            olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() - agent1CommAmount);

            switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());

            debitList.add(olaInfo);


            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.CNIC_TO_BB_AC,
                        productId,
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;

            }

            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

            //Cr Customer A/C
            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
            AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            OLAInfo customerOlaInfo = new OLAInfo();
            customerOlaInfo.setIsAgent(false);
            customerOlaInfo.setBalanceAfterTrxRequired(true);
            customerOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            customerOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            customerOlaInfo.setReasonId(ReasonConstants.CNIC_TO_BB_AC);
            customerOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            customerOlaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());

            customerOlaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges);

            switchWrapper.setToAccountNo(customerAccountInfo.getAccountNo());
            creditList.add(customerOlaInfo);

            // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.CNIC_TO_BB_AC);

        } else if (productId == TransactionProductEnum.DEBIT_CARD_CASH_WITHDRAWL_ON_US_PRODUCT.getProductId()
                ||productId == TransactionProductEnum.DEBIT_CARD_LESS_CASH_WITHDRAWAL.getProductId()
                || productId == TransactionProductEnum.DEBIT_CARD_CASH_WITHDRAWL_OFF_US_PRODUCT.getProductId()
                || productId == TransactionProductEnum.POS_DEBIT_CARD_CASH_WITHDRAWL_PRODUCT.getProductId()
                || productId == TransactionProductEnum.DEBIT_CARD_ANNUAL_FEE_PRODUCT.getProductId()
                || productId == TransactionProductEnum.DEBIT_CARD_ISSUANCE_PRODUCT.getProductId()
                || productId == TransactionProductEnum.DEBIT_CARD_RE_ISSUANCE_PRODUCT.getProductId()
                || productId == TransactionProductEnum.CUSTOMER_DEBIT_CARD_ISSUANCE_PRODUCT.getProductId()
                || productId == TransactionProductEnum.ATM_BALANCE_INQUIRY_OFF_US.getProductId()
                || productId == TransactionProductEnum.INTERNATIONAL_POS_DEBIT_CARD_CASH_WITHDRAWL_PRODUCT.getProductId()
                || productId == TransactionProductEnum.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWL_OFF_US_PRODUCT.getProductId()
                || productId == TransactionProductEnum.INTERNATIONAL_BALANCE_INQUIRY_OFF_US.getProductId()
        ) {

            Long reasonId = null;
            Long coreStakeHolderBankInfoId = null;
            Long olaStakeHolderBankInfoId = null;
            if (productId == TransactionProductEnum.DEBIT_CARD_CASH_WITHDRAWL_OFF_US_PRODUCT.getProductId()) {
                reasonId = ReasonConstants.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US;
                olaStakeHolderBankInfoId = PoolAccountConstantsInterface.DEBIT_CARD_CW_OFF_US_POOL_ACCOUNT;
                coreStakeHolderBankInfoId = PoolAccountConstantsInterface.DEBIT_CARD_CW_OFF_US_POOL_ACCOUNT_GL;
            } else if (productId == TransactionProductEnum.POS_DEBIT_CARD_CASH_WITHDRAWL_PRODUCT.getProductId()) {
                reasonId = ReasonConstants.POS_DEBIT_CARD_CASH_WITHDRAWAL;
                olaStakeHolderBankInfoId = PoolAccountConstantsInterface.POS_DEBIT_CARD_CW_POOL_ACCOUNT;
                coreStakeHolderBankInfoId = PoolAccountConstantsInterface.POS_DEBIT_CARD_CW_POOL_ACCOUNT_GL;
            } else if (productId == TransactionProductEnum.DEBIT_CARD_ANNUAL_FEE_PRODUCT.getProductId()) {
                reasonId = ReasonConstants.DEBIT_CARD_ANNUAL_FEE;
                olaStakeHolderBankInfoId = PoolAccountConstantsInterface.DEBIT_CARD_ANNUAL_FEE_POOL_ACCOUNT;
                coreStakeHolderBankInfoId = PoolAccountConstantsInterface.DEBIT_CARD_ANNUAL_FEE_GL;
            } else if (productId == TransactionProductEnum.DEBIT_CARD_ISSUANCE_PRODUCT.getProductId()
                    || productId == TransactionProductEnum.CUSTOMER_DEBIT_CARD_ISSUANCE_PRODUCT.getProductId()) {
                reasonId = ReasonConstants.DEBIT_CARD_ISSUANCE_FEE;
                olaStakeHolderBankInfoId = PoolAccountConstantsInterface.DEBIT_CARD_ISSUANCE_FEE_POOL_ACCOUNT;
                coreStakeHolderBankInfoId = PoolAccountConstantsInterface.DEBIT_CARD_ISSUANCE_FEE_GL;
            } else if (productId == TransactionProductEnum.DEBIT_CARD_RE_ISSUANCE_PRODUCT.getProductId()) {
                reasonId = ReasonConstants.DEBIT_CARD_RE_ISSUANCE_FEE;
                olaStakeHolderBankInfoId = PoolAccountConstantsInterface.DEBIT_CARD_RE_ISSUANCE_FEE_POOL_ACCOUNT;
                coreStakeHolderBankInfoId = PoolAccountConstantsInterface.DEBIT_CARD_RE_ISSUANCE_FEE_GL;
            } else if (productId == TransactionProductEnum.ATM_BALANCE_INQUIRY_OFF_US.getProductId() || productId == TransactionProductEnum.INTERNATIONAL_BALANCE_INQUIRY_OFF_US.getProductId()) {
                reasonId = ReasonConstants.DEBIT_CARD_RE_ISSUANCE_FEE;
                olaStakeHolderBankInfoId = PoolAccountConstantsInterface.OF_SETTLEMENT_IFT_POOL_ACCOUNT;
                coreStakeHolderBankInfoId = PoolAccountConstantsInterface.T24_SETTLEMENT_ACCOUNT_ID;
            } else if (productId == TransactionProductEnum.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWL_OFF_US_PRODUCT.getProductId()) {
                reasonId = ReasonConstants.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US;
                olaStakeHolderBankInfoId = PoolAccountConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US_BLB;
                coreStakeHolderBankInfoId = PoolAccountConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US_GL;
            } else if (productId == TransactionProductEnum.INTERNATIONAL_POS_DEBIT_CARD_CASH_WITHDRAWL_PRODUCT.getProductId()) {
                reasonId = ReasonConstants.POS_DEBIT_CARD_CASH_WITHDRAWAL;
                olaStakeHolderBankInfoId = PoolAccountConstantsInterface.POS_DEBIT_CARD_CW_POOL_ACCOUNT;
                coreStakeHolderBankInfoId = PoolAccountConstantsInterface.POS_DEBIT_CARD_CW_POOL_ACCOUNT_GL;
            } else {
                reasonId = ReasonConstants.DEBIT_CARD_CASH_WITHDRAWAL_ON_US;
                olaStakeHolderBankInfoId = PoolAccountConstantsInterface.DEBIT_CARD_CW_ON_US_POOL_ACCOUNT;
                coreStakeHolderBankInfoId = PoolAccountConstantsInterface.DEBIT_CARD_CW_ON_US_POOL_ACCOUNT_GL;
            }
            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
            //AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
            AccountInfoModel customerAccountInfo = veriflyManager.getAccountInfoModel(customerSMA.getCustomerId(), customerSMA.getPaymentModeId());
            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
            Double totalExclCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount());
            Double fed = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().
                    getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID));

            switchWrapper.setAccountInfoModel(customerAccountInfo);
            // Debt Customer
            OLAInfo olaInfo = new OLAInfo();
            olaInfo.setIsAgent(false);
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            if (customerSMA.getPaymentModeId().equals(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT))
                olaInfo.setCustomerAccountTypeId(CustomerAccountTypeConstants.HRA);
            else
                olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            olaInfo.setReasonId(reasonId);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());

            if (totalInclusiveCharges == 0.0 && (productId == ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE || productId == ProductConstantsInterface.DEBIT_CARD_ISSUANCE ||
                    productId == ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE || productId == ProductConstantsInterface.DEBIT_CARD_ANNUAL_FEE) || productId == ProductConstantsInterface.ATM_BALANCE_INQUIRY_OFF_US || productId == ProductConstantsInterface.INTERNATIONAL_BALANCE_INQUIRY_OFF_US) {
                olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + fed);
            } else if (totalInclusiveCharges != 0.0) {
                olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + totalExclCharges);
            } else {
                olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + totalExclCharges);
            }

            if(productId == ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US ||
                    productId == ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL) {
                if (switchWrapper.getWorkFlowWrapper().getFilerRate() != null && switchWrapper.getWorkFlowWrapper().getFilerRate() > 0.0) {
                    olaInfo.setBalance(olaInfo.getBalance() + switchWrapper.getWorkFlowWrapper().getFilerRate());
                }
                if (switchWrapper.getWorkFlowWrapper().getNonFilerRate() != null && switchWrapper.getWorkFlowWrapper().getNonFilerRate() > 0.0) {
                    olaInfo.setBalance(olaInfo.getBalance() + switchWrapper.getWorkFlowWrapper().getNonFilerRate());
                }
            }
            switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());
            debitList.add(olaInfo);


            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        reasonId,
                        productId,
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;

            }
            switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setRecipientAccountNo(customerAccountInfo.getAccountNo());
            Long appUserTypeId = ThreadLocalAppUser.getAppUserModel().getAppUserTypeId();
            if (appUserTypeId != null && appUserTypeId.equals(UserTypeConstantsInterface.RETAILER)) {
                Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().
                        getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
                AccountInfoModel agentAccountInfoModel = veriflyManager.getAccountInfoModel(ThreadLocalAppUser.getAppUserModel().getAppUserId(),
                        switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getPaymentModeId());
                olaInfo = new OLAInfo();
                olaInfo.setIsAgent(true);
                if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                    olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                    olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
                }
                olaInfo.setBalanceAfterTrxRequired(true);
                olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
                olaInfo.setReasonId(reasonId);
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
                olaInfo.setPayingAccNo(agentAccountInfoModel.getAccountNo());
                olaInfo.setBalance(agent1CommAmount);
                switchWrapper.setToAccountNo(agentAccountInfoModel.getAccountNo());
                switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setSenderAgentAccountNo(agentAccountInfoModel.getAccountNo());
                switchWrapper.getWorkFlowWrapper().setFromRetailerContactModel(switchWrapper.getWorkFlowWrapper().getRetailerContactModel());
                creditList.add(olaInfo);
            }
            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

            if (productId == ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE || productId == ProductConstantsInterface.DEBIT_CARD_ISSUANCE ||
                    productId == ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE || productId == ProductConstantsInterface.DEBIT_CARD_ANNUAL_FEE || productId == ProductConstantsInterface.ATM_BALANCE_INQUIRY_OFF_US || productId == ProductConstantsInterface.INTERNATIONAL_BALANCE_INQUIRY_OFF_US) {
                prepareCommissionCreditList(switchWrapper, creditList, reasonId);
            } else {
                String trxCode = switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode();
                Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));

                Double amount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + agent1CommAmount - totalInclusiveCharges;

                olaInfo = getStakeholderOLAInfo(trxCode, reasonId, TransactionTypeConstantsInterface.OLA_CREDIT,
                        olaStakeHolderBankInfoId, coreStakeHolderBankInfoId,
                        amount);

                creditList.add(olaInfo);

                if(productId == ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US ||
                        productId == ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL) {
                    if (switchWrapper.getWorkFlowWrapper().getFilerRate() != null && switchWrapper.getWorkFlowWrapper().getFilerRate() > 0.0) {
                        olaInfo = getStakeholderOLAInfo(trxCode, reasonId, TransactionTypeConstantsInterface.OLA_CREDIT,
                                PoolAccountConstantsInterface.ADVANCE_TAX_FILER_BLB, PoolAccountConstantsInterface.ADVANCE_TAX_FILER_OF,
                                (switchWrapper.getWorkFlowWrapper().getFilerRate()));
                        creditList.add(olaInfo);
                    }
                    if (switchWrapper.getWorkFlowWrapper().getNonFilerRate() != null && switchWrapper.getWorkFlowWrapper().getNonFilerRate() > 0.0) {
                        olaInfo = getStakeholderOLAInfo(trxCode, reasonId, TransactionTypeConstantsInterface.OLA_CREDIT,
                                PoolAccountConstantsInterface.ADVANCE_TAX_NON_FILER_BLB, PoolAccountConstantsInterface.ADVANCE_TAX_NON_FILER_OF,
                                (switchWrapper.getWorkFlowWrapper().getNonFilerRate()));
                        creditList.add(olaInfo);
                    }
                }
                prepareCommissionCreditList(switchWrapper, creditList, reasonId);
            }
            // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
//            prepareCommissionCreditList(switchWrapper, creditList, reasonId);
        } else if (productId == TransactionProductEnum.CASH_WITHDRAWL_PRODUCT.getProductId() || productId == TransactionProductEnum.HRA_CASH_WITHDRAWL_PRODUCT.getProductId()) {
            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
            AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
            Double totalExclCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount());

            // Debt Customer
            OLAInfo olaInfo = new OLAInfo();
            olaInfo.setIsAgent(false);
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            if (customerSMA.getPaymentModeId() == 7l)
                olaInfo.setCustomerAccountTypeId(4l);
            else
                olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            olaInfo.setReasonId(ReasonConstants.CASH_WITHDRAWAL);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());

            olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + totalExclCharges);


            switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());

            debitList.add(olaInfo);


            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.CASH_WITHDRAWAL,
                        productId,
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;

            }

            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

            //Credit Agent Account
            olaInfo = new OLAInfo();
            olaInfo.setIsAgent(true);
            if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
            }
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            olaInfo.setReasonId(ReasonConstants.CASH_WITHDRAWAL);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());

            Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));

            olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + agent1CommAmount - totalInclusiveCharges);
            switchWrapper.setToAccountNo(accountInfoModel.getAccountNo());

            creditList.add(olaInfo);

            // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.CASH_WITHDRAWAL);

        } else if (switchWrapper.getWorkFlowWrapper().getTransactionTypeModel().getTransactionTypeId().equals
                (TransactionTypeConstantsInterface.THIRD_PARTY_CASH_OUT_TX)) {
            CommissionAmountsHolder commissionAmountsHolder = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder();
            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
            Double totalExclCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount());
            OLAInfo olaInfo = new OLAInfo();
            Double amount = 0.0D;
            //Credit Agent Account
            OLAInfo agentOlaInfo = new OLAInfo();
            agentOlaInfo.setIsAgent(true);
            if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                agentOlaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                agentOlaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
            }
            agentOlaInfo.setBalanceAfterTrxRequired(true);
            agentOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            agentOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            agentOlaInfo.setReasonId(ReasonConstants.THIRDPARTY_CASH_OUT_PAYMENT);
            agentOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            agentOlaInfo.setPayingAccNo(accountInfoModel.getAccountNo());
            Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
            if (inclChargesCheck)
                amount = commissionAmountsHolder.getTransactionAmount() + agent1CommAmount;
            else
                amount = commissionAmountsHolder.getTransactionAmount() + agent1CommAmount - totalInclusiveCharges;
            agentOlaInfo.setBalance(amount);
            switchWrapper.setToAccountNo(accountInfoModel.getAccountNo());
            if (productId == ProductConstantsInterface.EOBI_CASH_OUT.longValue() || productId == ProductConstantsInterface.BISP_CASH_OUT.longValue()) {
                if (productId == ProductConstantsInterface.EOBI_CASH_OUT.longValue()) {
                    olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                            ReasonConstants.THIRDPARTY_CASH_OUT_PAYMENT,
                            // These constant will be changed to a new Third party account
                            TransactionTypeConstantsInterface.OLA_DEBIT,
                            PoolAccountConstantsInterface.EOBI_CASH_OUT_ACCOUNT_OLA,
                            PoolAccountConstantsInterface.EOBI_CASH_OUT_SETTLEMENT_ACCOUNT_CORE,
                            commissionAmountsHolder.getTotalAmount() + totalExclCharges - totalInclusiveCharges
                    );

                } else if (productId == ProductConstantsInterface.BISP_CASH_OUT.longValue()) {
                    // Debt Customer
                    amount = commissionAmountsHolder.getTotalAmount();
                    olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                            ReasonConstants.THIRDPARTY_CASH_OUT_PAYMENT,
                            // These constant will be changed to a new Third party account
                            TransactionTypeConstantsInterface.OLA_DEBIT,
                            PoolAccountConstantsInterface.THIRDPARTY_CASH_OUT_ACCOUNT_OLA,
                            PoolAccountConstantsInterface.THIRDPARTY_CASH_OUT_SETTLEMENT_ACCOUNT_CORE,
                            amount
                    );

                }
                switchWrapper.setFromAccountNo(olaInfo.getPayingAccNo());
                debitList.add(olaInfo);
            } else {
                if (commissionAmountsHolder.getStakeholderCommissionsMap().get(50028L) != null)
                    agentOlaInfo.setBalance(commissionAmountsHolder.getStakeholderCommissionsMap().get(50028L));
                else
                    agentOlaInfo.setBalance(0.0D);
            }
            if (agentOlaInfo.getBalance() > 0)
                creditList.add(agentOlaInfo);
            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.THIRDPARTY_CASH_OUT_PAYMENT,
                        productId,
                        totalInclusiveCharges
                );
                debitList.add(olaInfo);
                totalInclusiveCharges = 0.0;
            }
            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);
// populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.THIRDPARTY_CASH_OUT_PAYMENT);

        } else if (switchWrapper.getWorkFlowWrapper().getTransactionTypeModel().getTransactionTypeId().equals
                (TransactionTypeConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING_TX)) {
            CommissionAmountsHolder commissionAmountsHolder = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder();
            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
            Double totalExclCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount());
            OLAInfo olaInfo = new OLAInfo();
            Double amount = 0.0D;
            //Credit Agent Account
            OLAInfo agentOlaInfo = new OLAInfo();
            agentOlaInfo.setIsAgent(true);
            if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                agentOlaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                agentOlaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
            }
            agentOlaInfo.setBalanceAfterTrxRequired(true);
            agentOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            agentOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            agentOlaInfo.setReasonId(ReasonConstants.THIRD_PARTY_ACCOUNT_OPENING);
            agentOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            agentOlaInfo.setPayingAccNo(accountInfoModel.getAccountNo());
            Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
            if (inclChargesCheck)
                amount = commissionAmountsHolder.getTransactionAmount() + agent1CommAmount;
            else
                amount = commissionAmountsHolder.getTransactionAmount() + agent1CommAmount - totalInclusiveCharges;
            agentOlaInfo.setBalance(amount);
            switchWrapper.setToAccountNo(accountInfoModel.getAccountNo());
//            if(productId == ProductConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING.longValue()){
//                // Debt Customer
//                    amount = commissionAmountsHolder.getTotalAmount();
//                    olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
//                            ReasonConstants.THIRD_PARTY_ACCOUNT_OPENING,
//                            // These constant will be changed to a new Third party account
//                            TransactionTypeConstantsInterface.OLA_DEBIT,
//                            PoolAccountConstantsInterface.THIRD_PARTY_ACC_OPENING_BLB,
//                            PoolAccountConstantsInterface.THIRD_PARTY_ACC_OPENING_GL,
//                            amount
//                    );
//
//
//                switchWrapper.setFromAccountNo(olaInfo.getPayingAccNo());
//                debitList.add(olaInfo);
//            }
//            else{
            if (commissionAmountsHolder.getStakeholderCommissionsMap().get(50028L) != null)
                agentOlaInfo.setBalance(commissionAmountsHolder.getStakeholderCommissionsMap().get(50028L));
            else
                agentOlaInfo.setBalance(0.0D);
//            }
            if (agentOlaInfo.getBalance() > 0)
                creditList.add(agentOlaInfo);
            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.THIRD_PARTY_ACCOUNT_OPENING,
                        productId,
                        totalInclusiveCharges
                );
                debitList.add(olaInfo);
                totalInclusiveCharges = 0.0;
            }
            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);
// populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.THIRD_PARTY_ACCOUNT_OPENING);

        } else if (switchWrapper.getWorkFlowWrapper().getTransactionTypeModel().getTransactionTypeId().equals
                (TransactionTypeConstantsInterface.BOP_CARD_ISSUANCE_REISSUANCE_TX)) {
            CommissionAmountsHolder commissionAmountsHolder = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder();
            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
            Double totalExclCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount());
            OLAInfo olaInfo = new OLAInfo();
            Double amount = 0.0D;
            //Credit Agent Account
            OLAInfo agentOlaInfo = new OLAInfo();
            agentOlaInfo.setIsAgent(true);
            if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                agentOlaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                agentOlaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
            }
            agentOlaInfo.setBalanceAfterTrxRequired(true);
            agentOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            agentOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            agentOlaInfo.setReasonId(ReasonConstants.BOP_CARD_ISSUANCE_REISSUANCE);
            agentOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            agentOlaInfo.setPayingAccNo(accountInfoModel.getAccountNo());
            Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
            if (inclChargesCheck)
                amount = commissionAmountsHolder.getTransactionAmount() + agent1CommAmount;
            else
                amount = commissionAmountsHolder.getTransactionAmount() + agent1CommAmount - totalInclusiveCharges;
            agentOlaInfo.setBalance(amount);
            switchWrapper.setToAccountNo(accountInfoModel.getAccountNo());
//            if(productId == ProductConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING.longValue()){
//                // Debt Customer
//                    amount = commissionAmountsHolder.getTotalAmount();
//                    olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
//                            ReasonConstants.THIRD_PARTY_ACCOUNT_OPENING,
//                            // These constant will be changed to a new Third party account
//                            TransactionTypeConstantsInterface.OLA_DEBIT,
//                            PoolAccountConstantsInterface.THIRD_PARTY_ACC_OPENING_BLB,
//                            PoolAccountConstantsInterface.THIRD_PARTY_ACC_OPENING_GL,
//                            amount
//                    );
//
//
//                switchWrapper.setFromAccountNo(olaInfo.getPayingAccNo());
//                debitList.add(olaInfo);
//            }
//            else{
            if (commissionAmountsHolder.getStakeholderCommissionsMap().get(50028L) != null)
                agentOlaInfo.setBalance(commissionAmountsHolder.getStakeholderCommissionsMap().get(50028L));
            else
                agentOlaInfo.setBalance(0.0D);
//            }
            if (agentOlaInfo.getBalance() > 0)
                creditList.add(agentOlaInfo);
            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.BOP_CARD_ISSUANCE_REISSUANCE,
                        productId,
                        totalInclusiveCharges
                );
                debitList.add(olaInfo);
                totalInclusiveCharges = 0.0;
            }
            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);
// populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.BOP_CARD_ISSUANCE_REISSUANCE);

        } else if (switchWrapper.getWorkFlowWrapper().getTransactionTypeModel().getTransactionTypeId().equals
                (TransactionTypeConstantsInterface.PROOF_OF_LIFE_TX)) {
            CommissionAmountsHolder commissionAmountsHolder = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder();
            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
            Double totalExclCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount());
            OLAInfo olaInfo = new OLAInfo();
            Double amount = 0.0D;
            //Credit Agent Account
            OLAInfo agentOlaInfo = new OLAInfo();
            agentOlaInfo.setIsAgent(true);
            if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                agentOlaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                agentOlaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
            }
            agentOlaInfo.setBalanceAfterTrxRequired(true);
            agentOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            agentOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            agentOlaInfo.setReasonId(ReasonConstants.PROOF_OF_LIFE);
            agentOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            agentOlaInfo.setPayingAccNo(accountInfoModel.getAccountNo());
            Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
            if (inclChargesCheck)
                amount = commissionAmountsHolder.getTransactionAmount() + agent1CommAmount;
            else
                amount = commissionAmountsHolder.getTransactionAmount() + agent1CommAmount - totalInclusiveCharges;
            agentOlaInfo.setBalance(amount);
            switchWrapper.setToAccountNo(accountInfoModel.getAccountNo());
//            if(productId == ProductConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING.longValue()){
//                // Debt Customer
//                    amount = commissionAmountsHolder.getTotalAmount();
//                    olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
//                            ReasonConstants.THIRD_PARTY_ACCOUNT_OPENING,
//                            // These constant will be changed to a new Third party account
//                            TransactionTypeConstantsInterface.OLA_DEBIT,
//                            PoolAccountConstantsInterface.THIRD_PARTY_ACC_OPENING_BLB,
//                            PoolAccountConstantsInterface.THIRD_PARTY_ACC_OPENING_GL,
//                            amount
//                    );
//
//
//                switchWrapper.setFromAccountNo(olaInfo.getPayingAccNo());
//                debitList.add(olaInfo);
//            }
//            else{
            if (commissionAmountsHolder.getStakeholderCommissionsMap().get(50028L) != null)
                agentOlaInfo.setBalance(commissionAmountsHolder.getStakeholderCommissionsMap().get(50028L));
            else
                agentOlaInfo.setBalance(0.0D);
//            }
            if (agentOlaInfo.getBalance() > 0)
                creditList.add(agentOlaInfo);
            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.PROOF_OF_LIFE,
                        productId,
                        totalInclusiveCharges
                );
                debitList.add(olaInfo);
                totalInclusiveCharges = 0.0;
            }
            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);
// populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.PROOF_OF_LIFE);

        } else if (switchWrapper.getWorkFlowWrapper().getTransactionTypeModel().getTransactionTypeId().equals
                (TransactionTypeConstantsInterface.ADVANCE_SALARY_LOAN_TX)) {
            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
            AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            CommissionAmountsHolder commissionAmountsHolder = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder();
            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
            Double totalExclCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount());
//            OLAInfo olaInfo = new OLAInfo();
//            Double amount = 0.0D;
            OLAInfo olaInfo = new OLAInfo();
            olaInfo.setIsAgent(false);
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            olaInfo.setReasonId(ReasonConstants.ADVANCE_SALARY_LOAN);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());

            olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());

            switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());

            creditList.add(olaInfo);

            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.ADVANCE_SALARY_LOAN,
                        productId,
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;
            }

            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

            //Cr T24 Settlement A/C
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.ADVANCE_SALARY_LOAN,
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    PoolAccountConstantsInterface.ADVANCE_SALARY_LOAN_BLB,
                    PoolAccountConstantsInterface.ADVANCE_SALARY_LOAN_GL,
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges + totalExclCharges
            );
            debitList.add(olaInfo);

            switchWrapper.setToAccountNo(olaInfo.getPayingAccNo());

            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.ADVANCE_SALARY_LOAN);
            //Credit Agent Account
//            OLAInfo agentOlaInfo = new OLAInfo();
//            agentOlaInfo.setIsAgent(true);
//            if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
//                agentOlaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
//                agentOlaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
//            }
//            agentOlaInfo.setBalanceAfterTrxRequired(true);
//            agentOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
//            agentOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
//            agentOlaInfo.setReasonId(ReasonConstants.ADVANCE_SALARY_LOAN);
//            agentOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
//            agentOlaInfo.setPayingAccNo(accountInfoModel.getAccountNo());
//            Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
//            if (inclChargesCheck)
//                amount = commissionAmountsHolder.getTransactionAmount() + agent1CommAmount;
//            else
//                amount = commissionAmountsHolder.getTransactionAmount() + agent1CommAmount - totalInclusiveCharges;
//            agentOlaInfo.setBalance(amount);
//            switchWrapper.setToAccountNo(accountInfoModel.getAccountNo());
//            if(productId == ProductConstantsInterface.ADVANCE_SALARY_LOAN.longValue()){
//                // Debt Customer
//                    amount = commissionAmountsHolder.getTotalAmount();
//                    olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
//                            ReasonConstants.ADVANCE_SALARY_LOAN,
//                            // These constant will be changed to a new Third party account
//                            TransactionTypeConstantsInterface.OLA_DEBIT,
//                            PoolAccountConstantsInterface.ADVANCE_LOAN_SALARY_BLB,
//                            PoolAccountConstantsInterface.ADVANCE_LOAN_SALARY_GL,
//                            amount
//                    );
//
//
//                switchWrapper.setFromAccountNo(olaInfo.getPayingAccNo());
//                debitList.add(olaInfo);
//            }
//            else {
//                if (commissionAmountsHolder.getStakeholderCommissionsMap().get(50028L) != null)
//                    agentOlaInfo.setBalance(commissionAmountsHolder.getStakeholderCommissionsMap().get(50028L));
//                else
//                    agentOlaInfo.setBalance(0.0D);
//            }
//            if(agentOlaInfo.getBalance() > 0)
//                creditList.add(agentOlaInfo);
//            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
//                //Dr Inclusive Charges Sundry A/C
//                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
//                        ReasonConstants.ADVANCE_SALARY_LOAN,
//                        productId,
//                        totalInclusiveCharges
//                );
//                debitList.add(olaInfo);
//                totalInclusiveCharges = 0.0;
//            }
//            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);
//// populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
//            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.ADVANCE_SALARY_LOAN);
        } else if (switchWrapper.getWorkFlowWrapper().getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CASH_OUT_TX)) {
            CommissionAmountsHolder commissionAmountsHolder = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder();
            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
            Double totalExclCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount());
            OLAInfo olaInfo = new OLAInfo();
            Double amount = 0.0D;
            Long olaStakeholderBankInfoId = null;
            Long coreStakeholderBankInfoId = null;
            productId = switchWrapper.getWorkFlowWrapper().getProductModel().getProductId();
            if (productId == ProductConstantsInterface.BOP_CASH_OUT.longValue()) {
                olaStakeholderBankInfoId = PoolAccountConstantsInterface.BOP_CASH_OUT_POOL_ACCOUNT;
                coreStakeholderBankInfoId = PoolAccountConstantsInterface.BOP_CASH_OUT_GL;
            } else if (productId == ProductConstantsInterface.BOP_CASH_OUT_COVID_19.longValue()) {
                olaStakeholderBankInfoId = PoolAccountConstantsInterface.BOP_CASH_OUT_COVID_19_POOL_ACCOUNT;
                coreStakeholderBankInfoId = PoolAccountConstantsInterface.BOP_CASH_OUT_COVID_19_GL;
            }
            //Credit Agent Account
            OLAInfo agentOlaInfo = new OLAInfo();
            agentOlaInfo.setIsAgent(true);
            if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                agentOlaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                agentOlaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
            }
            agentOlaInfo.setBalanceAfterTrxRequired(true);
            agentOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            agentOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            agentOlaInfo.setReasonId(ReasonConstants.BOP_CASH_OUT);
            agentOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            agentOlaInfo.setPayingAccNo(accountInfoModel.getAccountNo());
            Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
            if (inclChargesCheck)
                amount = commissionAmountsHolder.getTransactionAmount() + agent1CommAmount;
            else
                amount = commissionAmountsHolder.getTransactionAmount() + agent1CommAmount - totalInclusiveCharges;
            agentOlaInfo.setBalance(amount);
            switchWrapper.setToAccountNo(accountInfoModel.getAccountNo());
            // Debt Customer
            amount = commissionAmountsHolder.getTotalAmount();
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.BOP_CASH_OUT,
                    // These constant will be changed to a new Third party account
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    olaStakeholderBankInfoId,
                    coreStakeholderBankInfoId, amount);
            switchWrapper.setFromAccountNo(olaInfo.getPayingAccNo());
            debitList.add(olaInfo);
            if (agentOlaInfo.getBalance() > 0)
                creditList.add(agentOlaInfo);
            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.BOP_CASH_OUT,
                        productId,
                        totalInclusiveCharges
                );
                debitList.add(olaInfo);
                totalInclusiveCharges = 0.0;
            }
            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);
// populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.THIRDPARTY_CASH_OUT_PAYMENT);

        } else if (productId == TransactionProductEnum.AGENT_RETAIL_PAYMENT_PRODUCT.getProductId()) {

            //Dr Customer A/C
            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
            AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            OLAInfo olaInfo = new OLAInfo();
            olaInfo.setIsAgent(false);
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            olaInfo.setReasonId(ReasonConstants.AGENT_RETAIL_PAYMENT);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
            olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount());

            switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());

            debitList.add(olaInfo);


            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.AGENT_RETAIL_PAYMENT,
                        productId,
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;

            }
            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);


            //Cr Agent Account
            olaInfo = new OLAInfo();
            olaInfo.setIsAgent(true);
            if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
            }
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            olaInfo.setReasonId(ReasonConstants.AGENT_RETAIL_PAYMENT);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());

            olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges);
            switchWrapper.setToAccountNo(accountInfoModel.getAccountNo());

            creditList.add(olaInfo);


            // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.AGENT_RETAIL_PAYMENT);

        } else if (productId == ProductConstantsInterface.ACCOUNT_TO_CASH) {

            prepareSwitchWrapperForAccountToCash(switchWrapper, debitList, creditList);

        } else if (productId == ProductConstantsInterface.ACT_TO_CASH_CI) {

            prepareSwitchWrapperForCustomerAccountToCash(switchWrapper, debitList, creditList);

        } else if (TransactionProductEnum.TRANSACTION_OUT_PRODUCT.getProductId() == productId) {

            debitList.clear();
            creditList.clear();

//			@FT
            Boolean _inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();

            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (_inclChargesCheck != null && _inclChargesCheck && totalInclusiveCharges > 0.0) {

                OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.TRANSFER_OUT,
                        productId,
                        totalInclusiveCharges
                );

                switchWrapper.getOlavo().getDebitAccountList().add(olaInfo);
            }

            debitList.addAll(switchWrapper.getOlavo().getDebitAccountList());
            creditList.addAll(switchWrapper.getOlavo().getCreditAccountList());

            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.TRANSFER_OUT);

        } else if (TransactionProductEnum.TRANSACTION_IN_PRODUCT.getProductId() == productId) {

            debitList.clear();
            creditList.clear();

//			@FT
            Boolean _inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();

            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (_inclChargesCheck != null && _inclChargesCheck && totalInclusiveCharges > 0.0) {

                OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.TRANSFER_IN,
                        productId,
                        totalInclusiveCharges
                );

                switchWrapper.getOlavo().getDebitAccountList().add(olaInfo);
            }

            debitList.addAll(switchWrapper.getOlavo().getDebitAccountList());
            creditList.addAll(switchWrapper.getOlavo().getCreditAccountList());

            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.TRANSFER_IN);

        } else if (TransactionProductEnum.AGENT_IBFT_PRODUCT.getProductId() == productId) {
            debitList.clear();
            creditList.clear();
//			@FT
            Boolean _inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();

            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (_inclChargesCheck != null && _inclChargesCheck && totalInclusiveCharges > 0.0) {

                OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.AGENT_IBFT,
                        productId,
                        totalInclusiveCharges
                );

                switchWrapper.getOlavo().getDebitAccountList().add(olaInfo);
            }

            debitList.addAll(switchWrapper.getOlavo().getDebitAccountList());
            creditList.addAll(switchWrapper.getOlavo().getCreditAccountList());

            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.AGENT_IBFT);

        } else if (productId == TransactionProductEnum.AGENT_TO_AGENT_PRODUCT.getProductId()) {

            //Dr Agent A/C
            OLAInfo olaInfo = new OLAInfo();
            olaInfo.setIsAgent(true);
            if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
            }
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getFromRetailerContactModel().getOlaCustomerAccountTypeModelId());
            olaInfo.setReasonId(ReasonConstants.RET_RET_CREDIT_TRSFR);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());

            Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
            olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() - agent1CommAmount);

            switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());

            debitList.add(olaInfo);

            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            Boolean agentTransferThirdPartyCheck = switchWrapper.getWorkFlowWrapper().getAgentTransferRuleModel().getThirdPartyCheck();

            if (agentTransferThirdPartyCheck != null && agentTransferThirdPartyCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.RET_RET_CREDIT_TRSFR,
                        productId,
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;

            }

            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

            //Cr Agent Account
            olaInfo = new OLAInfo();
            olaInfo.setIsAgent(true);
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getToRetailerContactModel().getOlaCustomerAccountTypeModelId());
            olaInfo.setReasonId(ReasonConstants.RET_RET_CREDIT_TRSFR);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);

            SmartMoneyAccountModel receiverSMA = switchWrapper.getWorkFlowWrapper().getReceivingSmartMoneyAccountModel();
            AccountInfoModel receiverAccountInfo = getAccountInfoModelBySmartMoneyAccount(receiverSMA, switchWrapper.getWorkFlowWrapper().getToRetailerContactAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            olaInfo.setPayingAccNo(receiverAccountInfo.getAccountNo());

            olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges);

            switchWrapper.setToAccountNo(receiverAccountInfo.getAccountNo());

            creditList.add(olaInfo);

            // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.RET_RET_CREDIT_TRSFR);
        } else if (productId == TransactionProductEnum.IBFT_PRODUCT.getProductId()) {

//			Double totalInclusiveCharges = 0.0;
            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.IBFT,
                        switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;

            }

            //Dr IBFT Pool A/C
            OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.IBFT,
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    PoolAccountConstantsInterface.IBFT_POOL_OLA_ACCOUNT_ID,
                    PoolAccountConstantsInterface.IBFT_POOL_OF_ACCOUNT_ID,
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount()
            );


            debitList.add(olaInfo);


            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();

            AccountInfoModel customerAccountInfo = null;
            if (ThreadLocalAppUser.getAppUserModel().getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
                customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
            } else {
                customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
            }

            OLAInfo customerOlaInfo = new OLAInfo();
            if (ThreadLocalAppUser.getAppUserModel().getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
                customerOlaInfo.setIsAgent(false);
                customerOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            } else {
                customerOlaInfo.setIsAgent(true);
                customerOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getRelationOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
            }

            customerOlaInfo.setBalanceAfterTrxRequired(true);
            customerOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            customerOlaInfo.setReasonId(ReasonConstants.IBFT);
            customerOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            customerOlaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
            customerOlaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges);

            switchWrapper.setToAccountNo(customerAccountInfo.getAccountNo());
            creditList.add(customerOlaInfo);

            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.IBFT);
            olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
            olaVO.setReceivingAccNo(customerAccountInfo.getAccountNo());// to be used in posted trx

        }
        //Core To Wallet Transaction


        else if (productId == ProductConstantsInterface.CORE_TO_WALLET.longValue()) {

//			Double totalInclusiveCharges = 0.0;
            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.IBFT,
                        switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;

            }

            //Dr Core To Wallet Pool A/C
            OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.IBFT,
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    PoolAccountConstantsInterface.T24_SETTLEMENT_ACCOUNT_ID,
                    PoolAccountConstantsInterface.OF_SETTLEMENT_IFT_POOL_ACCOUNT,
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount()
            );


            debitList.add(olaInfo);


            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();

            AccountInfoModel customerAccountInfo = null;
            if (ThreadLocalAppUser.getAppUserModel().getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
                customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
            } else {
                customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
            }

            OLAInfo customerOlaInfo = new OLAInfo();
            if (ThreadLocalAppUser.getAppUserModel().getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
                customerOlaInfo.setIsAgent(false);
                customerOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            } else {
                customerOlaInfo.setIsAgent(true);
                customerOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getRelationOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
            }

            customerOlaInfo.setBalanceAfterTrxRequired(true);
            customerOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            customerOlaInfo.setReasonId(ReasonConstants.IBFT);
            customerOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            customerOlaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
            customerOlaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges);

            switchWrapper.setToAccountNo(customerAccountInfo.getAccountNo());
            creditList.add(customerOlaInfo);

            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.IBFT);
            olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
            olaVO.setReceivingAccNo(customerAccountInfo.getAccountNo());// to be used in posted trx

        } else if (ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue() == productId || ProductConstantsInterface.TELLER_ACCOUNT_HOLDER_CASH_IN.longValue() == productId) {

            debitList.clear();
            creditList.clear();

            String code = switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode();
            Boolean _inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (_inclChargesCheck != null && _inclChargesCheck && totalInclusiveCharges > 0.0) {

                OLAInfo olaInfo = getOLAInfoForInclCharges(code, ReasonConstants.TELLER_CASH_IN, productId, totalInclusiveCharges);

                switchWrapper.getOlavo().getDebitAccountList().add(olaInfo);
            }

            debitList.addAll(switchWrapper.getOlavo().getDebitAccountList());
            creditList.addAll(switchWrapper.getOlavo().getCreditAccountList());

            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.TELLER_CASH_IN);

        } else if (ProductConstantsInterface.TELLER_CASH_OUT.longValue() == productId) {

            debitList.clear();
            creditList.clear();

            String code = switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode();
            Boolean _inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (_inclChargesCheck != null && _inclChargesCheck && totalInclusiveCharges > 0.0) {

                OLAInfo olaInfo = getOLAInfoForInclCharges(code, ReasonConstants.TELLER_CASH_IN, productId, totalInclusiveCharges);

                switchWrapper.getOlavo().getDebitAccountList().add(olaInfo);
            }

            debitList.addAll(switchWrapper.getOlavo().getDebitAccountList());
            creditList.addAll(switchWrapper.getOlavo().getCreditAccountList());

            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.TELLER_CASH_OUT);
        } else if ((ProductConstantsInterface.WEB_SERVICE_PAYMENT.longValue() == productId) || (ProductConstantsInterface.VIRTUAL_CARD_PAYMENT.longValue() == productId)) {


            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.WEB_SERVICE_PAYMENT,
                        switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;

            }

            //Debit Customer
            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();

            AccountInfoModel customerAccountInfo = null;

            customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            OLAInfo customerOlaInfo = new OLAInfo();

            customerOlaInfo.setIsAgent(false);
            customerOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());


            customerOlaInfo.setBalanceAfterTrxRequired(false);
            customerOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            customerOlaInfo.setReasonId(ReasonConstants.WEB_SERVICE_PAYMENT);
            customerOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            customerOlaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
            customerOlaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount());
            debitList.add(customerOlaInfo);


            //Cr Fonepay settlement A/C
            OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.WEB_SERVICE_PAYMENT,
                    // These constant will be changed to a new Fonepay account
                    TransactionTypeConstantsInterface.OLA_CREDIT,
                    PoolAccountConstantsInterface.FONEPAY_SETTLEMENT_ACCOUNT_OLA,
                    PoolAccountConstantsInterface.FONEPAY_SETTLEMENT_ACCOUNT_CORE,
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges
            );


            creditList.add(olaInfo);


            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

            switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());
            switchWrapper.setToAccountNo(olaInfo.getPayingAccNo());


            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.WEB_SERVICE_PAYMENT);
            olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
            olaVO.setReceivingAccNo(olaInfo.getPayingAccNo());// to be used in posted trx

        } else if (ProductConstantsInterface.WEB_SERVICE_CASH_IN.longValue() == productId) {


            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.WEB_SERVICE_PAYMENT,
                        switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;

            }

            //Debit Customer
            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();

            AccountInfoModel customerAccountInfo = null;


            customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            OLAInfo customerOlaInfo = new OLAInfo();

            customerOlaInfo.setIsAgent(false);
            if (customerSMA.getPaymentModeId() == 7l)
                customerOlaInfo.setCustomerAccountTypeId(4l);
            else
                customerOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());


            customerOlaInfo.setBalanceAfterTrxRequired(true);
            customerOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            customerOlaInfo.setReasonId(ReasonConstants.WEB_SERVICE_PAYMENT);
            customerOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            customerOlaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
            customerOlaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges);
            creditList.add(customerOlaInfo);


            //Cr Fonepay settlement A/C
            OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.WEB_SERVICE_PAYMENT,
                    // These constant will be changed to a new Fonepay account
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    PoolAccountConstantsInterface.APIGEE_CASHIN_SETTLEMENT_ACCOUNT_OLA,
                    PoolAccountConstantsInterface.APIGEE_CASHIN_SETTLEMENT_ACCOUNT_CORE,
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount()
            );


            debitList.add(olaInfo);


            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

            switchWrapper.setFromAccountNo(olaInfo.getPayingAccNo());
            switchWrapper.setToAccountNo(customerAccountInfo.getAccountNo());


            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.WEB_SERVICE_PAYMENT);
            olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
            olaVO.setReceivingAccNo(customerAccountInfo.getAccountNo());// to be used in posted trx


        } else if ((ProductConstantsInterface.APIGEE_PAYMENT.longValue() == productId)) {


            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.WEB_SERVICE_PAYMENT,
                        switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;

            }

            //Debit Customer
            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();

            AccountInfoModel customerAccountInfo = null;

            customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            OLAInfo customerOlaInfo = new OLAInfo();

            customerOlaInfo.setIsAgent(false);
            customerOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());


            customerOlaInfo.setBalanceAfterTrxRequired(false);
            customerOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            customerOlaInfo.setReasonId(ReasonConstants.WEB_SERVICE_PAYMENT);
            customerOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            customerOlaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
            customerOlaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount());
            debitList.add(customerOlaInfo);


            //Cr Fonepay settlement A/C
            OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.WEB_SERVICE_PAYMENT,
                    // These constant will be changed to a new Fonepay account
                    TransactionTypeConstantsInterface.OLA_CREDIT,
                    PoolAccountConstantsInterface.APIGEE_PAYMENT_SETTLEMENT_ACCOUNT_OLA,
                    PoolAccountConstantsInterface.APIGEE_PAYMENT_SETTLEMENT_ACCOUNT_CORE,
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges
            );


            creditList.add(olaInfo);


            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

            switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());
            switchWrapper.setToAccountNo(olaInfo.getPayingAccNo());


            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.WEB_SERVICE_PAYMENT);
            olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
            olaVO.setReceivingAccNo(olaInfo.getPayingAccNo());// to be used in posted trx

        } else if (ProductConstantsInterface.FONEPAY_AGENT_PAYMENT.longValue() == productId) {
            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
            CommissionAmountsHolder commissionAmountsHolder = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder();

            //Dr Fonepay settlement A/C
            OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.WEB_SERVICE_PAYMENT,
                    // These constant will be changed to a new Fonepay account
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    PoolAccountConstantsInterface.FONEPAY_SETTLEMENT_ACCOUNT_OLA,
                    PoolAccountConstantsInterface.FONEPAY_SETTLEMENT_ACCOUNT_CORE,
                    commissionAmountsHolder.getTotalAmount()
            );

            debitList.add(olaInfo);
            switchWrapper.setFromAccountNo(olaInfo.getPayingAccNo());

            //Dr Inclusive Third Party A/C
            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                OLAInfo olaInfo1 = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.WEB_SERVICE_PAYMENT,
                        switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                        totalInclusiveCharges
                );

                debitList.add(olaInfo1);

                totalInclusiveCharges = 0.0;
            }

            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

            //Credit Agent
            SmartMoneyAccountModel agenrSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();

            AccountInfoModel agentAccountInfo = getAccountInfoModelBySmartMoneyAccount(agenrSMA, switchWrapper.getWorkFlowWrapper().getAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            OLAInfo agentOlaInfo = new OLAInfo();

            agentOlaInfo.setIsAgent(true);
            agentOlaInfo.setBalanceAfterTrxRequired(false);
            agentOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            agentOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            agentOlaInfo.setReasonId(ReasonConstants.WEB_SERVICE_PAYMENT);
            agentOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            agentOlaInfo.setPayingAccNo(agentAccountInfo.getAccountNo());
            agentOlaInfo.setBalance(commissionAmountsHolder.getTransactionAmount() - totalInclusiveCharges);
            creditList.add(agentOlaInfo);

            switchWrapper.setToAccountNo(agentAccountInfo.getAccountNo());


            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.WEB_SERVICE_PAYMENT);
//			olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
//			olaVO.setReceivingAccNo(olaInfo.getPayingAccNo());// to be used in posted trx
        } else if (ProductConstantsInterface.POS_DEBIT_CARD_REFUND.longValue() == productId) {


            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.POS_DEBIT_CARD_REFUND,
                        switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;

            }


            //Cr Customer A/C
            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
            AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            OLAInfo customerOlaInfo = new OLAInfo();
            customerOlaInfo.setIsAgent(false);
            customerOlaInfo.setBalanceAfterTrxRequired(true);
            customerOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            customerOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            customerOlaInfo.setReasonId(ReasonConstants.POS_DEBIT_CARD_REFUND);
            customerOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            customerOlaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());

            customerOlaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges);

            switchWrapper.setToAccountNo(customerAccountInfo.getAccountNo());
            creditList.add(customerOlaInfo);

            //Dr Fonepay settlement A/C
            OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.POS_DEBIT_CARD_REFUND,
                    // These constant will be changed to a new Fonepay account
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    PoolAccountConstantsInterface.DEBIT_CARD_POS_REFUND_POOL_ACCOUNT,
                    PoolAccountConstantsInterface.DEBIT_CARD_POS_REFUND_GL,
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount()
            );

            debitList.add(olaInfo);
            switchWrapper.setFromAccountNo(olaInfo.getPayingAccNo());

            switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);
            switchWrapper.setToAccountNo(customerAccountInfo.getAccountNo());


            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.POS_DEBIT_CARD_REFUND);
            olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
            olaVO.setReceivingAccNo(customerAccountInfo.getAccountNo());// to be used in posted trx


        } else if (ProductConstantsInterface.HRA_TO_WALLET_TRANSACTION.equals(productId)) {
            Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.HRA_TO_WALLET_TRANSACTION,
                        switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;

            }


            //Cr Customer BLB Account
            SmartMoneyAccountModel customerSMABLB = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();
            AccountInfoModel customerBLBAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMABLB, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            OLAInfo customerBLBOlaInfo = new OLAInfo();
            customerBLBOlaInfo.setIsAgent(false);
            customerBLBOlaInfo.setBalanceAfterTrxRequired(true);
            customerBLBOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            customerBLBOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            customerBLBOlaInfo.setReasonId(ReasonConstants.HRA_TO_WALLET_TRANSACTION);
            customerBLBOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            customerBLBOlaInfo.setPayingAccNo(customerBLBAccountInfo.getAccountNo());

            customerBLBOlaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges);

            switchWrapper.setToAccountNo(customerBLBAccountInfo.getAccountNo());
            creditList.add(customerBLBOlaInfo);


            //Debit Customer HRA Account
            SmartMoneyAccountModel customerHRASMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
            AccountInfoModel customerHRAAccountInfo = null;
            customerHRAAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerHRASMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

            OLAInfo customerHRAOlaInfo = new OLAInfo();
            customerHRAOlaInfo.setIsAgent(false);
//            customerHRAOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            customerHRAOlaInfo.setCustomerAccountTypeId(4l);


            customerHRAOlaInfo.setBalanceAfterTrxRequired(true);
            customerHRAOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            customerHRAOlaInfo.setReasonId(ReasonConstants.HRA_TO_WALLET_TRANSACTION);
            customerHRAOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            customerHRAOlaInfo.setPayingAccNo(customerHRAAccountInfo.getAccountNo());
            customerHRAOlaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount());

            debitList.add(customerHRAOlaInfo);

            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.HRA_TO_WALLET_TRANSACTION);

            olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
            olaVO.setReceivingAccNo(customerBLBAccountInfo.getAccountNo());// to be used in posted trx
            olaVO.setPayingAccNo(customerHRAAccountInfo.getAccountNo());

        }


        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);


        olaVO.setFromSegmentId(switchWrapper.getWorkFlowWrapper().getFromSegmentId());
        olaVO.setToSegmentId(switchWrapper.getWorkFlowWrapper().getToSegmentId());
        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());


        if (productId == TransactionProductEnum.ACCOUNT_TO_CASH_PRODUCT.getProductId() ||
                productId == TransactionProductEnum.CASH_TO_CASH_PRODUCT.getProductId()) {

            olaVO.setCategory(TransactionConstantsInterface.FIRST_LEG_CATEGORY_ID); // category = Leg 1
        }

        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());

        if (accountInfoModel != null && !StringUtil.isNullOrEmpty(accountInfoModel.getAccountNo())) {

            String accountNo = accountInfoModel.getAccountNo();

            if (switchWrapper.getWorkFlowWrapper().isLeg2Transaction() && productId != TransactionProductEnum.CASH_WITHDRAWL_CUSTOMER_PRODUCT.getProductId()) {
                switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setRecipientAgentAccountNo(accountNo);
            } else {
                if (productId != TransactionProductEnum.IBFT_PRODUCT.getProductId() && !switchWrapper.getWorkFlowWrapper().getIsCustomerInitiatedTransaction()) {
                    switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setSenderAgentAccountNo(accountNo);
                }
            }

            if (productId == TransactionProductEnum.CASH_TO_CASH_PRODUCT.getProductId() ||
                    productId == ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT.longValue() ||
                    productId == ProductConstantsInterface.CNIC_TO_BB_ACCOUNT.longValue()) {

                accountNo = null;
            }

            switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(accountNo);
        }

        if (switchWrapper.getWorkFlowWrapper() != null &&
                switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null
                && StringUtil.isNullOrEmpty(switchWrapper.getOlavo().getMicrobankTransactionCode())) {

            switchWrapper.getOlavo().setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        }

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        // setting OlaSwitchWrapper to be used in TransactionProcessor for Day End settlement process
        switchWrapper.getWorkFlowWrapper().setOLASwitchWrapper(switchWrapper);

        return switchWrapper;

    }

    /*
     * populates Credit Accounts List with commission stakeholders FED, WH, Bank, Zong, I8
     */
    private void prepareCommissionCreditList(SwitchWrapper switchWrapper, List<OLAInfo> creditList, long reasonId) throws FrameworkCheckedException, Exception {
        OLAInfo olaInfo = new OLAInfo();

        for (Map.Entry<Long, Double> entry : switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().entrySet()) {
            Long commissionStakeholderId = entry.getKey();
            Double commissionValue = entry.getValue();

            if (commissionStakeholderId.longValue() == CommissionConstantsInterface.FRANCHISE1_STAKE_HOLDER_ID.longValue() ||
                    commissionStakeholderId.longValue() == CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID.longValue() ||
                    commissionStakeholderId.longValue() == CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID.longValue() ||
                    commissionStakeholderId.longValue() == CommissionConstantsInterface.ACC_OPENING_AGENT_STAKE_HOLDER_ID.longValue()) {

                continue;
            }
            if (commissionValue > 0.0 && reasonId == 45 && commissionStakeholderId.longValue() == CommissionConstantsInterface.BANK_STAKE_HOLDER_ID.longValue()) {
                if (switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID) != null) {
                    olaInfo = getOLAInfoByCommStakeholderId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                            reasonId,
                            TransactionTypeConstantsInterface.OLA_CREDIT,
                            commissionStakeholderId,
                            commissionValue + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID)
                    );
                } else {
                    olaInfo = getOLAInfoByCommStakeholderId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                            reasonId,
                            TransactionTypeConstantsInterface.OLA_CREDIT,
                            commissionStakeholderId,
                            commissionValue);
                }
                creditList.add(olaInfo);
            } else if (commissionValue > 0.0) {
                olaInfo = getOLAInfoByCommStakeholderId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        reasonId,
                        TransactionTypeConstantsInterface.OLA_CREDIT,
                        commissionStakeholderId,
                        commissionValue
                );
                creditList.add(olaInfo);
            }

        }

/*
		//FED
        Double fedCommission = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID));
        if(fedCommission > 0.0){
	        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
	        		reasonId,
	        		TransactionTypeConstantsInterface.OLA_CREDIT,
	        		PoolAccountConstantsInterface.FED_OLA_ACCOUNT_ID,
	        		PoolAccountConstantsInterface.JSBL_OF_FED_POOL_ACCOUNT_ID,
	        		fedCommission
	        		);
	        creditList.add(olaInfo);
        }

        //W.H
        Double whCommission = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID));
        if(whCommission > 0.0){
	        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
	        		reasonId,
	        		TransactionTypeConstantsInterface.OLA_CREDIT,
	        		PoolAccountConstantsInterface.WHT_OLA_ACCOUNT_ID,
	        		PoolAccountConstantsInterface.JSBL_OF_WHT_POOL_ACCOUNT_ID,
	        		whCommission
	        		);
	        creditList.add(olaInfo);
        }

        //Bank
        Double bankCommission = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.BANK_STAKE_HOLDER_ID));
        if(bankCommission > 0.0){
	        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
	        		reasonId,
	        		TransactionTypeConstantsInterface.OLA_CREDIT,
	        		PoolAccountConstantsInterface.BANK_OLA_ACCOUNT_ID,
	        		PoolAccountConstantsInterface.BANK_INCOME_CORE_ACCOUNT_ID,
	        		bankCommission
	        		);
	        creditList.add(olaInfo);
        }

        //SALES TEAM
        Double salesTeamCommission = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.SALES_TEAM_STAKE_HOLDER_ID));
        if(salesTeamCommission > 0.0){
        	olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
	        		reasonId,
	        		TransactionTypeConstantsInterface.OLA_CREDIT,
	        		PoolAccountConstantsInterface.SALES_TEAM_OLA_ACCOUNT_ID,
	        		PoolAccountConstantsInterface.SALES_TEAM_CORE_ACCOUNT_ID,
	        		salesTeamCommission
	        		);
        	logger.info("[OLAVeriflyFinancialInstitutionImpl.prepareCommissionCreditList] Crediting Sales Team Commission: " + salesTeamCommission + " Account No: " + olaInfo.getPayingAccNo());
	        creditList.add(olaInfo);
        }
*/

        //Account Opening Agent
        Double accountOpeningAgentCommission = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.ACC_OPENING_AGENT_STAKE_HOLDER_ID));
        if (accountOpeningAgentCommission > 0.0) {
            AccountInfoModel agentAccountInfo = null;
            AppUserModel createdByAppUserModel = null;
            AppUserModel agentAppUserModel = new AppUserModel();
            boolean agent1InfoLoaded = false;
            Long agentAccTypeId = null;
            String agentAccountNo = null;

            try {
                if (switchWrapper.getWorkFlowWrapper().getCustomerAppUserModel() != null) {
                    createdByAppUserModel = switchWrapper.getWorkFlowWrapper().getCustomerAppUserModel().getRelationCreatedByAppUserModel();
                    if (createdByAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
                        SmartMoneyAccountModel agentSMA = new SmartMoneyAccountModel();
                        agentSMA.setRetailerContactId(createdByAppUserModel.getRetailerContactId());
                        BaseWrapper baseWrapper = new BaseWrapperImpl();
                        baseWrapper.setBasePersistableModel(agentSMA);
                        baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
                        agentSMA = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
                        if (agentSMA != null) {
                            agentAccountInfo = getAccountInfoModelBySmartMoneyAccount(agentSMA, createdByAppUserModel.getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
                            if (agentAccountInfo == null) {
                                logger.error("[Account-Opening-Agent-Commission Settlement] unable to load AccountInfoModel for A/C Opening Agent. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                            } else {
                                agentAppUserModel = new AppUserModel();
                                agentAppUserModel.setRetailerContactId(createdByAppUserModel.getRetailerContactId());
                                agentAppUserModel = appUserManager.getAppUserModel(agentAppUserModel);
                                if (agentAppUserModel == null) {

                                    logger.error("[Account-Opening-Agent-Commission Settlement] Failed to settle retention commission. Reason: Unable to load agentAppUserModel. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

                                } else if ((null != agentAppUserModel.getAccountClosedSettled() && agentAppUserModel.getAccountClosedSettled())
                                        || (null != agentAppUserModel.getAccountClosedUnsettled() && agentAppUserModel.getAccountClosedUnsettled())) {

                                    logger.error("[Account-Opening-Agent-Commission Settlement] Failed to settle retention commission. Reason: ACCOUNT CLOSED !!! for agentAppUserId:" + agentAppUserModel.getAppUserId() + ". Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

                                } else {
                                    agentAccTypeId = agentAppUserModel.getRetailerContactIdRetailerContactModel().getOlaCustomerAccountTypeModelId();
                                    agentAccountNo = agentAccountInfo.getAccountNo();
                                    agent1InfoLoaded = true;

                                    // Setting agent appUserId in workFlowWrapper to be used in CommissionTransaction.retentionAppUserId
                                    switchWrapper.getWorkFlowWrapper().setAccOpeningAppUserId(createdByAppUserModel.getAppUserId());
                                }
                            }
                        } else {
                            logger.error("[Account-Opening-Agent-Commission Settlement] unable to load A/C Opening Agent's SmartMoneyAccountModel. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                        }
                    } else {
                        logger.error("[Account-Opening-Agent-Commission Settlement] workFlowWrapper.getCustomerAppUserModel().getCreatedBy() is not Agent. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                    }
                } else {
                    logger.error("[Account-Opening-Agent-Commission Settlement] workFlowWrapper.getCustomerAppUserModel() is null. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                }
            } catch (Exception ex) {
                logger.error("[Account-Opening-Agent-Commission Settlement] Exception occurred while settlement of Account-Opening-Agent-Commission. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                logger.error(ex.getStackTrace());
            }

            OLAInfo accOpeningOlaInfo = new OLAInfo();
            accOpeningOlaInfo.setBalance(accountOpeningAgentCommission);
            accOpeningOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            accOpeningOlaInfo.setReasonId(reasonId);
            accOpeningOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            accOpeningOlaInfo.setBalanceAfterTrxRequired(Boolean.FALSE);
            accOpeningOlaInfo.setCommissionType(CommissionTypeConstants.RETENTION_POLICY_COMMISSION);

            if (agent1InfoLoaded) {
                accOpeningOlaInfo.setIsAgent(true);
                accOpeningOlaInfo.setPayingAccNo(agentAccountNo);
                accOpeningOlaInfo.setCustomerAccountTypeId(agentAccTypeId);
            } else {
                logger.error("[Account-Opening-Agent-Commission Settlement] Sending A/C Opening Comm to Default Agent Commission A/C. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                accOpeningOlaInfo = processUnsettledAgentCommission(switchWrapper,
                        reasonId,
                        accountOpeningAgentCommission,
                        agentAppUserModel,
                        switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                        switchWrapper.getWorkFlowWrapper().getProductModel().getName(),
                        CommissionTypeConstants.UNSETTLED_COMM_ACC_OPENING);

            }

            creditList.add(accOpeningOlaInfo);

        }


        //Franchise
        /*Double franchiseCommission = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.FRANCHISE1_STAKE_HOLDER_ID));
        if(franchiseCommission > 0.0){
        	SmartMoneyAccountModel agentSMA = (SmartMoneyAccountModel)switchWrapper.getBasePersistableModel();
        	Long agentRetaileId = agentSMA.getRetailerContactIdRetailerContactModel().getRetailerId();
        	logger.info("[OLAVeriflyFinancialInstitutionImpl.prepareCommissionCreditList] Crediting Franchise Commission: " + franchiseCommission + " Agent Retailer Contact ID: " + agentRetaileId);
        	BaseWrapper baseWrapper = new BaseWrapperImpl();
        	baseWrapper.putObject(WorkFlowErrorCodeConstants.RETAILER_ID, agentRetaileId);

        	baseWrapper = smartMoneyAccountManager.loadOLASMAForRetOrDistHead(baseWrapper);
        	SmartMoneyAccountModel franchiseSMA = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();

        	AppUserModel franchiseAppUserModel = new AppUserModel();
        	franchiseAppUserModel.setRetailerContactId(franchiseSMA.getRetailerContactId());
        	franchiseAppUserModel = appUserManager.getAppUserModel(franchiseAppUserModel);

			AccountInfoModel franchiseAccountInfo = getAccountInfoModelBySmartMoneyAccount(franchiseSMA, franchiseAppUserModel.getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

			OLAInfo franchiseOlaInfo = new OLAInfo();
			franchiseOlaInfo.setIsAgent(true);
			franchiseOlaInfo.setBalance(franchiseCommission);
	        franchiseOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
	        franchiseOlaInfo.setCustomerAccountTypeId(franchiseAppUserModel.getRetailerContactIdRetailerContactModel().getOlaCustomerAccountTypeModelId());
	        franchiseOlaInfo.setReasonId(reasonId);
	        franchiseOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
	        franchiseOlaInfo.setPayingAccNo(franchiseAccountInfo.getAccountNo());
	        franchiseOlaInfo.setBalanceAfterTrxRequired(Boolean.FALSE);

	        creditList.add(franchiseOlaInfo);
        }*/

        prepareHierarchyStakeholderCommissionsCreditList(switchWrapper, creditList, reasonId);
    }


    /**
     * prepares OLAInfo for commission credit e.g. FED , Withholding Tax, Bank, MNO, i8
     * Note: coreStakeholderBankInfoId is used to post the commission at day end by FT settlement scheduler
     *
     * @param trxCode
     * @param reasonId
     * @param trxType
     * @param olaStakeholderBankInfoId
     * @param coreStakeholderBankInfoId
     * @param amount
     * @return OLAInfo
     */
    private OLAInfo getStakeholderOLAInfo(String trxCode, Long reasonId, String trxType, Long olaStakeholderBankInfoId, Long coreStakeholderBankInfoId, Double amount) throws FrameworkCheckedException {
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
     * prepares OLAInfo for commission credit e.g. FED , Withholding Tax, Bank, MNO, i8
     * Note: coreStakeholderBankInfoId is used to post the commission at day end by FT settlement scheduler
     *
     * @param trxCode
     * @param reasonId
     * @param trxType
     * @param olaStakeholderBankInfoId
     * @param coreStakeholderBankInfoId
     * @param amount
     * @return OLAInfo
     */
    private OLAInfo getOLAInfoByCommStakeholderId(String trxCode, Long reasonId, String trxType, Long commissionStakeholderId, Double amount) throws FrameworkCheckedException {

        List<StakeholderBankInfoModel> list = stakeholderBankInfoManager.getStakeholderBankInfoByCommShId(commissionStakeholderId);
        if (list == null) {
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.error("!!!!!! getStakeholderBankInfoByCommShId - no record found for commissionStakeholderId:" + commissionStakeholderId + " !!!!!!");
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);
        }

        if (list.size() != 2) {
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.error("!!!!!! getStakeholderBankInfoByCommShId - invalid data (rows != 2) for commissionStakeholderId:" + commissionStakeholderId + " !!!!!!");
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);
        }

        if (((StakeholderBankInfoModel) list.get(0)).getBankId().longValue() == ((StakeholderBankInfoModel) list.get(1)).getBankId().longValue()) {
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.error("!!!!!! getStakeholderBankInfoByCommShId - invalid data (same bank_id) for commissionStakeholderId:" + commissionStakeholderId + " !!!!!!");
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);
        }

        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setBalanceAfterTrxRequired(Boolean.FALSE);
        olaInfo.setMicrobankTransactionCode(trxCode);
        olaInfo.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
        olaInfo.setReasonId(reasonId);
        olaInfo.setTransactionTypeId(trxType);
        olaInfo.setBalance(Double.parseDouble(String.valueOf(amount)));

        for (StakeholderBankInfoModel shBankInfoModel : list) {
            if (shBankInfoModel.getBankId().longValue() == BankConstantsInterface.ASKARI_BANK_ID.longValue()) {
                olaInfo.setCoreStakeholderBankInfoId(shBankInfoModel.getStakeholderBankInfoId());
            } else {
                olaInfo.setPayingAccNo(shBankInfoModel.getAccountNo());
            }
        }

        return olaInfo;
    }

    public AccountInfoModel getAccountInfoModelBySmartMoneyAccount(SmartMoneyAccountModel smartMoneyAccountModel, Long customerId, Long trxCodeId) throws Exception {
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setCustomerId(customerId);
        accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
        accountInfoModel.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
        LogModel logModel = new LogModel();
        if (UserUtils.getCurrentUser() != null) {
            logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
            logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
        } else if (ThreadLocalAppUser.getAppUserModel() != null) {
            logModel.setCreatdByUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
            logModel.setCreatedBy(ThreadLocalAppUser.getAppUserModel().getUsername());
        }
        logModel.setTransactionCodeId(trxCodeId);
        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);
        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        veriflyBaseWrapper = this.verifyCredentials(veriflyBaseWrapper);
        boolean notError = veriflyBaseWrapper.isErrorStatus();
        if (notError) {
            accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
        }

        return accountInfoModel;
    }

    private void prepareHierarchyStakeholderCommissionsCreditList(SwitchWrapper switchWrapper, List<OLAInfo> creditList, long reasonId) throws FrameworkCheckedException, Exception {

        OLAInfo olaInfo = null;

        try {
            for (Map.Entry<Long, Double> entry : switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getHierarchyStakeholderCommissionMapLeg1().entrySet()) {

                Long hierarchyAppUserId = (Long) entry.getKey();
                Double commissionshare = entry.getValue();

                olaInfo = prepareOlaInfoForHierarchy(hierarchyAppUserId, commissionshare, switchWrapper, reasonId, Boolean.TRUE);
                creditList.add(olaInfo);
            }

            for (Map.Entry<Long, Double> entry : switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getHierarchyStakeholderCommissionMap().entrySet()) {

                Long hierarchyAppUserId = (Long) entry.getKey();
                Double commissionshare = entry.getValue();

                olaInfo = prepareOlaInfoForHierarchy(hierarchyAppUserId, commissionshare, switchWrapper, reasonId, Boolean.FALSE);
                creditList.add(olaInfo);
            }
        } catch (Exception e) {
            logger.error("[prepareHierarchyStakeholderCommissionsCreditList] Exception occured while crediting hierarhy commission. Exeption msg: " + e.getMessage());
            throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }


		/*Double franchiseCommission = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.FRANCHISE1_STAKE_HOLDER_ID));
        if(franchiseCommission > 0.0){
        	SmartMoneyAccountModel agentSMA = (SmartMoneyAccountModel)switchWrapper.getBasePersistableModel();
        	Long agentRetaileId = agentSMA.getRetailerContactIdRetailerContactModel().getRetailerId();
        	logger.info("[OLAVeriflyFinancialInstitutionImpl.prepareCommissionCreditList] Crediting Franchise Commission: " + franchiseCommission + " Agent Retailer Contact ID: " + agentRetaileId);
        	BaseWrapper baseWrapper = new BaseWrapperImpl();
        	baseWrapper.putObject(WorkFlowErrorCodeConstants.RETAILER_ID, agentRetaileId);

        	baseWrapper = smartMoneyAccountManager.loadOLASMAForRetOrDistHead(baseWrapper);
        	SmartMoneyAccountModel franchiseSMA = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();

        	AppUserModel franchiseAppUserModel = new AppUserModel();
        	franchiseAppUserModel.setRetailerContactId(franchiseSMA.getRetailerContactId());
        	franchiseAppUserModel = appUserManager.getAppUserModel(franchiseAppUserModel);

			AccountInfoModel franchiseAccountInfo = getAccountInfoModelBySmartMoneyAccount(franchiseSMA, franchiseAppUserModel.getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

			OLAInfo franchiseOlaInfo = new OLAInfo();
			franchiseOlaInfo.setBalanceAfterTrxRequired(false);
			franchiseOlaInfo.setBalance(franchiseCommission);
	        franchiseOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
	        franchiseOlaInfo.setCustomerAccountTypeId(franchiseAppUserModel.getRetailerContactIdRetailerContactModel().getOlaCustomerAccountTypeModelId());
	        franchiseOlaInfo.setReasonId(reasonId);
	        franchiseOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
	        franchiseOlaInfo.setPayingAccNo(franchiseAccountInfo.getAccountNo());

	        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
	        		reasonId,
	        		TransactionTypeConstantsInterface.OLA_CREDIT,
	        		PoolAccountConstantsInterface.SALES_TEAM_OLA_ACCOUNT_ID,
	        		franchiseCommission
	        		);
	        creditList.add(franchiseOlaInfo);
        }*/
    }

    private OLAInfo prepareOlaInfoForHierarchy(Long hierarchyAppUserId, Double commissionshare, SwitchWrapper switchWrapper, Long reasonId,
                                               boolean closedBlacklistedHandling) throws Exception {

        String agentAccountNo = null;
        Long agentAccTypeId = null;
        boolean hierarchyInfoLoaded = false;
        try {
            AppUserModel hierarchyAppUserModel = new AppUserModel();
            hierarchyAppUserModel.setPrimaryKey(hierarchyAppUserId);
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(hierarchyAppUserModel);
            baseWrapper = appUserManager.loadAppUser(baseWrapper);
            hierarchyAppUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
            if (hierarchyAppUserModel == null) {
                logger.error("[prepareOlaInfoForHierarchy] Failed to settle hierarchy commission. Reason: Unable to load agentAppUserModel for hierarchyAppUserId:" + hierarchyAppUserId + ". Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                throw new CommandException("Unable to load hierarchy agent info", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
            if ((null != hierarchyAppUserModel.getAccountClosedSettled() && hierarchyAppUserModel.getAccountClosedSettled())
                    || (null != hierarchyAppUserModel.getAccountClosedUnsettled() && hierarchyAppUserModel.getAccountClosedUnsettled())) {

                logger.error("[prepareOlaInfoForHierarchy] Failed to settle hierarchy commission. Reason: ACCOUNT CLOSED !!! for hierarchyAppUserId:" + hierarchyAppUserId + ". Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                throw new CommandException("Unable to load hierarchy agent info", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
            SmartMoneyAccountModel hierarchySMA = new SmartMoneyAccountModel();
            hierarchySMA.setRetailerContactId(hierarchyAppUserModel.getRetailerContactId());
            baseWrapper.setBasePersistableModel(hierarchySMA);
            baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
            hierarchySMA = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();

            if (hierarchySMA == null) {
                logger.error("[prepareOlaInfoForHierarchy] Unable to load hierarchy SmartMoneyAccountModel [hierarchyAppUserId:" + hierarchyAppUserId + " , hierarchyRetailerContactId:" + hierarchyAppUserModel.getRetailerContactId() + " ]. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                throw new CommandException("Unable to load hierarchy agent info", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }

            AccountInfoModel acountInfo = getAccountInfoModelBySmartMoneyAccount(hierarchySMA, hierarchyAppUserModel.getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
            if (acountInfo == null) {
                logger.error("[prepareOlaInfoForHierarchy] Unable to load hierarchy agent acountInfo [hierarchyAppUserId:" + hierarchyAppUserId + " , hierarchyRetailerContactId:" + hierarchyAppUserModel.getRetailerContactId() + " ]. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                throw new CommandException("Unable to load hierarchy agent info", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            } else {
                agentAccTypeId = hierarchyAppUserModel.getRetailerContactIdRetailerContactModel().getOlaCustomerAccountTypeModelId();
                agentAccountNo = acountInfo.getAccountNo();
                hierarchyInfoLoaded = true;
            }
        } catch (Exception ex) {
            logger.error("[prepareOlaInfoForHierarchy] Exception occurred while agent hierarchy Commission settlement. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(), ex);
        }

        OLAInfo olaInfo = new OLAInfo();
        if (hierarchyInfoLoaded) {
            olaInfo.setIsAgent(true);
            olaInfo.setBalanceAfterTrxRequired(false);
            olaInfo.setBalance(commissionshare);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(agentAccTypeId);
            olaInfo.setReasonId(reasonId);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setPayingAccNo(agentAccountNo);
            olaInfo.setCommissionType(CommissionTypeConstants.HIERARCHY_COMMISSION);
            olaInfo.setProcessThroughQueue(Boolean.TRUE);
        } else {

            if (closedBlacklistedHandling) {
                logger.info("[prepareOlaInfoForHierarchy] Sending hierarchy Comm to Default Agent Commission A/C [hierarchyAppUserId:" + hierarchyAppUserId + " ]. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                AppUserModel hAppUserModel = new AppUserModel();
                hAppUserModel.setAppUserId(hierarchyAppUserId);
                olaInfo = processUnsettledAgentCommission(switchWrapper,
                        reasonId,
                        commissionshare,
                        hAppUserModel,
                        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getProductId(),
                        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getProductName(),
                        CommissionTypeConstants.UNSETTLED_COMM_HIERARCHY1);

            } else {
                logger.error("Unable to load hierarchy agent info. Throwing generic Exception");
                throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);
            }

        }
        return olaInfo;

    }

    public SwitchWrapper transaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {


        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        AccountInfoModel threadLocalAccountInfoModel = getThreadLocalAccountInfoModel(switchWrapper);
        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();

        boolean isVerifyOneTimePin = false;
        if (null != switchWrapper.getWorkFlowWrapper().getDeviceTypeModel() && switchWrapper.getWorkFlowWrapper().getDeviceTypeModel().getDeviceTypeId() == DeviceTypeConstantsInterface.USSD && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50006L) {
            isVerifyOneTimePin = true;
        }

        AccountInfoModel accountInfoModel = switchWrapper.getWorkFlowWrapper().getAccountInfoModel();
        String pin = accountInfoModel.getOldPin();

        boolean errorMessagesFlag = false;
        boolean isThreadLocalAccountInfoLoaded = false;
        if (threadLocalAccountInfoModel != null && threadLocalAccountInfoModel.getAccountNo() != null && false == isVerifyOneTimePin) {
            accountInfoModel = threadLocalAccountInfoModel;
            errorMessagesFlag = true;
            isThreadLocalAccountInfoLoaded = true;

        } else {

            //Added by Maqsood Shahzad for moderate Verifly
            switchWrapper.putObject(CommandFieldConstants.KEY_PIN, pin);

            if (ThreadLocalAppUser.getAppUserModel().getCustomerId() != null)
                accountInfoModel.setCustomerId(appUserModel.getCustomerId());
            else if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50006L)
                accountInfoModel.setCustomerId(switchWrapper.getWorkFlowWrapper().getCustomerAppUserModel().getCustomerId());
            else
                accountInfoModel.setCustomerId(appUserModel.getAppUserId());

            accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
            accountInfoModel.setOldPin(pin);

            LogModel logModel = new LogModel();
            logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
            logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
            logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

            veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
            veriflyBaseWrapper.setLogModel(logModel);

            veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            if (isVerifyOneTimePin) {
                accountInfoModel.setOtPin(switchWrapper.getWorkFlowWrapper().getOneTimePin());
                accountInfoModel.setPin("");
                veriflyBaseWrapper = this.verifyOneTimePin(veriflyBaseWrapper);

            } else {

                veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
            }

            errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

        }

        if (errorMessagesFlag) {
            if (false == isThreadLocalAccountInfoLoaded) {
                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
                setThreadLocalAccountInfoModel(switchWrapper, accountInfoModel);//added by mudassir - place AccountinfoModel in threadLocal for reducing verifly calls
            }

            switchWrapper.setAccountInfoModel(accountInfoModel);
            switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(StringUtil.replaceString(accountInfoModel.getAccountNo(), 5, "*"));
            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            //WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
//            if(switchWrapper.getWorkFlowWrapper() != null)
//            {
////            	switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
//            }
            OLAVO olaVO = new OLAVO();
            if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50002L) {
                olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
            } else if (UtilityCompanyEnum.contains(String.valueOf(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId()))) {
                olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount())));
            } else {
                olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));


            }
            olaVO.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
            if (null != switchWrapper.getWorkFlowWrapper().getCustomerModel()) {
                olaVO.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            } else {
                olaVO.setCustomerAccountTypeId(1L);
            }
            olaVO.setTransactionDateTime(new Date());
            olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            if (UtilityCompanyEnum.contains(String.valueOf(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId()))
                    || InternetCompanyEnum.contains(String.valueOf(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId()))) {
                olaVO.setReasonId(ReasonConstants.BILL_PAYMENT);

            } else if (null != switchWrapper.getWorkFlowWrapper().getProductModel() && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(50010L)) {
                //for accont to cash transaction
                olaVO.setReasonId(ReasonConstants.CUSTOMER_ACCOUNT_TO_CASH);

            } else if (null != switchWrapper.getWorkFlowWrapper().getProductModel() && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(50011L)) {
                //for cash to cash transaction
                olaVO.setReasonId(ReasonConstants.CUSTOMER_CASH_TO_CASH);
//            	switchWrapper.setPaymentModeId(  );
            } else if (null != switchWrapper.getWorkFlowWrapper().getProductModel() && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(50018L)) {
                //For retail payment, bypass limits
                olaVO.setReasonId(ReasonConstants.RETAIL_PAYMENT);
//            	switchWrapper.setPaymentModeId(  );
            } else if (null != switchWrapper.getWorkFlowWrapper().getProductModel() && switchWrapper.getWorkFlowWrapper().getProductModel().getServiceId().longValue() ==
                    ServiceTypeConstantsInterface.SERVICE_TYPE_DONATION_CUSTOMER_PAYMENT.longValue()) {
                //Apothecare transaction.. reason id bill payment is set to bypass limits
                olaVO.setReasonId(ReasonConstants.DONATION_PAYMENT);
            } else {
                olaVO.setReasonId(OLATransactionReasonsInterface.PRODUCT_SALE);
            }
            switchWrapper.setOlavo(olaVO);
            switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());

            logger.info("OLAVeriflyFinancialInstitution.transaction] Debit OLA Account. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
                    " Trx ID:" + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() +
                    " Reason ID:" + olaVO.getReasonId() +
                    " ProductID:" + (switchWrapper.getWorkFlowWrapper().getProductModel() == null ? " NULL" : switchWrapper.getWorkFlowWrapper().getProductModel().getProductId()));

            switchWrapper = this.switchController.transaction(switchWrapper, null);
//            System.out.println("OLAVERiflyFinancialInstitution.transaction ended");
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;


    }


    public SwitchWrapper verifyPIN(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getSmartMoneyAccountModel();

        AccountInfoModel accountInfoModel = new AccountInfoModel();
        String pin = (String) switchWrapper.getObject(CommandFieldConstants.KEY_PIN);
        boolean isHandler = switchWrapper.getWorkFlowWrapper() != null && switchWrapper.getWorkFlowWrapper().getHandlerAppUserModel() != null && switchWrapper.getWorkFlowWrapper().getHandlerAppUserModel().getHandlerId() != null;

        accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
        accountInfoModel.setOldPin(pin);

        if (isHandler) {
            accountInfoModel.setCustomerId(switchWrapper.getWorkFlowWrapper().getHandlerAppUserModel().getAppUserId());
            accountInfoModel.setAccountNick(switchWrapper.getWorkFlowWrapper().getHandlerSMAModel().getName());
        } else if (ThreadLocalAppUser.getAppUserModel().getCustomerId() != null)
            accountInfoModel.setCustomerId(appUserModel.getCustomerId());
        else
            accountInfoModel.setCustomerId(appUserModel.getAppUserId());

        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
//		logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

        veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
        boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

        logger.info("[OLAVeriflyFinancialInstitution.verifyPIN] Going to verify PIN." +
                " Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
                " Customer SmartMoneyAcctID:" + smartMoneyAccountModel.getSmartMoneyAccountId() +
                " Customer Account Holder Nick:" + accountInfoModel.getAccountNick());

        if (errorMessagesFlag) {
            if (isHandler) {
                Boolean isAgentAccountRequired = (Boolean) switchWrapper.getObject(CommandFieldConstants.KEY_AGENT_ACC_INFO_REQ);
                if (isAgentAccountRequired == null || isAgentAccountRequired) {

                    AccountInfoModel aim = new AccountInfoModel();

                    aim.setAccountNick(smartMoneyAccountModel.getName());
                    aim.setCustomerId(appUserModel.getAppUserId());

                    veriflyBaseWrapper.setAccountInfoModel(aim);

                    this.verifyCredentials(veriflyBaseWrapper);
                }
            }
            switchWrapper.setAccountInfoModel(veriflyBaseWrapper.getAccountInfoModel());
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }

        return switchWrapper;
    }


    public SwitchWrapper verifyCredentialsWithoutPin(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getSmartMoneyAccountModel();

        AccountInfoModel accountInfoModel = new AccountInfoModel();
        String pin = (String) switchWrapper.getObject(CommandFieldConstants.KEY_PIN);

        if (ThreadLocalAppUser.getAppUserModel().getCustomerId() != null)
            accountInfoModel.setCustomerId(appUserModel.getCustomerId());
        else
            accountInfoModel.setCustomerId(appUserModel.getAppUserId());

        accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
        accountInfoModel.setOldPin(pin);

        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
//		logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);


        logger.info("[OLAVeriflyFinancialInstitution.verifyCredentialsWithoutPin] Going to load Account Info details." +
                " Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
                " Customer SmartMoneyAcctID:" + smartMoneyAccountModel.getSmartMoneyAccountId() +
                " Customer Account Holder Nick:" + accountInfoModel.getAccountNick());

        veriflyBaseWrapper = this.verifyCredentials(veriflyBaseWrapper);
        boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

        if (errorMessagesFlag) {
            switchWrapper.setAccountInfoModel(veriflyBaseWrapper.getAccountInfoModel());
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }

        return switchWrapper;
    }

    public SwitchWrapper checkBalanceWithoutPin(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {

	/*	if(switchWrapper.getWorkFlowWrapper() != null)
		{
			if(switchWrapper.getWorkFlowWrapper().getMPin() != null)
			{
//				System.out.println("TPin  : "+switchWrapper.getWorkFlowWrapper().getMPin());
//				System.out.println("VeriflyFinancialInstitutionImpl.checkBalance() TPin : "+switchWrapper.getWorkFlowWrapper().getMPin());
			}

		}
		*/

        AppUserModel appUserModel = null;

        if (switchWrapper.getWorkFlowWrapper().getProductModel() != null && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().longValue() == 50000L) { /*product is p2p*/

            if (switchWrapper.getWorkFlowWrapper().isP2PRecepient()) {
                appUserModel = new AppUserModel();
                P2PVO p2PVO = (P2PVO) switchWrapper.getWorkFlowWrapper().getProductVO();
                appUserModel.setCustomerId(p2PVO.getCustomerId());//get recipient ID
            } else {
                appUserModel = ThreadLocalAppUser.getAppUserModel();
            }

        } else if (switchWrapper.getWorkFlowWrapper().getProductModel() != null && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().longValue() == ProductConstantsInterface.TELLER_CASH_OUT.longValue()) {
            appUserModel = switchWrapper.getWorkFlowWrapper().getReceiverAppUserModel();
        } else if (switchWrapper.getWorkFlowWrapper().getProductModel() != null && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().longValue() == 50002L) { /*product is Cash Deposit*/
            appUserModel = switchWrapper.getWorkFlowWrapper().getCustomerAppUserModel();
        } else {

            appUserModel = ThreadLocalAppUser.getAppUserModel();
        }

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        String pin = (String) switchWrapper.getObject(CommandFieldConstants.KEY_PIN);
//		String mPin = (String) switchWrapper.getObject(CommandFieldConstants.KEY_MPIN);

        AccountInfoModel threadLocalAccountInfoModel = getThreadLocalAccountInfoModel(switchWrapper);
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        boolean errorMessagesFlag = false;
        boolean isThreadLocalAccountInfoLoaded = false;

        if (threadLocalAccountInfoModel != null && threadLocalAccountInfoModel.getAccountNo() != null) {
            accountInfoModel = threadLocalAccountInfoModel;
            errorMessagesFlag = true;
            isThreadLocalAccountInfoLoaded = true;

        } else {

            if (appUserModel.getCustomerId() != null) {
                accountInfoModel.setCustomerId(appUserModel.getCustomerId());
            } else {
                accountInfoModel.setCustomerId(appUserModel.getAppUserId());
            }

            accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
            accountInfoModel.setOldPin(pin);

            LogModel logModel = new LogModel();
            logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
            logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

            if (switchWrapper.getWorkFlowWrapper().getTransactionModel() != null) {
                logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());
            }

            veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

            veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
            veriflyBaseWrapper.setLogModel(logModel);

            veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            veriflyBaseWrapper = this.verifyCredentials(veriflyBaseWrapper);
            errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();
        }

        if (errorMessagesFlag) {
            if (false == isThreadLocalAccountInfoLoaded) {
                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
                setThreadLocalAccountInfoModel(switchWrapper, accountInfoModel);//added by mudassir - place AccountinfoModel in threadLocal for reducing verifly calls
            }

            switchWrapper.setAccountInfoModel(accountInfoModel);

            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            //WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
            if (switchWrapper.getWorkFlowWrapper().getTransactionModel() == null) {
                switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
            }

            logger.info("[OLAVeriflyFinancialInstitution.checkBalaneWithoutPin] Check Customer Balance in  OLA Account." +
                    " Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
                    " Transaction ID: " + (switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() == null ? " NULL" : switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode()) +
                    " Customer SmartMoneyAcctID:" + smartMoneyAccountModel.getSmartMoneyAccountId() +
                    " Customer Account Holder Nick:" + accountInfoModel.getAccountNick());

            switchWrapper = this.switchController.checkBalance(switchWrapper);
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;
    }


    @Override
    public SwitchWrapper settleCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getOlaCommissionSMA();

        AccountInfoModel threadLocalAccountInfoModel = ThreadLocalAccountInfo.getCustomerAccountInfoModel();
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        boolean errorMessagesFlag = false;

        if (threadLocalAccountInfoModel != null && threadLocalAccountInfoModel.getAccountNo() != null) {
            accountInfoModel = threadLocalAccountInfoModel;
            errorMessagesFlag = true;

        } else {

            accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());

            // Load the AppUserId of the retailer or distributor
            accountInfoModel.setCustomerId(switchWrapper.getCommissionAppUserModel().getAppUserId());


            LogModel logModel = new LogModel();
            logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
            logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
            logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

            veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

            veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
            veriflyBaseWrapper.setLogModel(logModel);

            veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
            errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();
        }

        if (errorMessagesFlag) {
            accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
            ThreadLocalAccountInfo.setCustomerAccountInfoModel(accountInfoModel);//added by mudassir - place AccountinfoModel in threadLocal for reducing verifly calls

            switchWrapper.setAccountInfoModel(accountInfoModel);

            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());

            OLAVO olaVO = new OLAVO();
            olaVO.setBalance(switchWrapper.getAmountDue());
            olaVO.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
            olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaVO.setReasonId(OLATransactionReasonsInterface.COMMISSION_TRANSFER);
            switchWrapper.setOlavo(olaVO);
            switchWrapper = this.switchController.settleCommission(switchWrapper);
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }

        return switchWrapper;
    }


    @Override
    public SwitchWrapper settleInov8Commission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getOlaCommissionSMA();

        switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());

        OLAVO olaVO = new OLAVO();
        olaVO.setBalance(switchWrapper.getAmountDue());
        olaVO.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaVO.setReasonId(OLATransactionReasonsInterface.INOV8_COMMISSION_TRANSFER);
        switchWrapper.setOlavo(olaVO);
        switchWrapper = this.switchController.settleInov8Commission(switchWrapper);

        return switchWrapper;
    }


    public SwitchWrapper debit(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();

        AccountInfoModel threadLocalAccountInfoModel = ThreadLocalAccountInfo.getCustomerAccountInfoModel();
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        boolean errorMessagesFlag = false;

        if (threadLocalAccountInfoModel != null && threadLocalAccountInfoModel.getAccountNo() != null) {
            accountInfoModel = threadLocalAccountInfoModel;
            errorMessagesFlag = true;

        } else {

            accountInfoModel.setOldPin(switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());

            if (ThreadLocalAppUser.getAppUserModel().getCustomerId() != null)
                accountInfoModel.setCustomerId(appUserModel.getCustomerId());
            else
                accountInfoModel.setCustomerId(appUserModel.getAppUserId());

            accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());

            LogModel logModel = new LogModel();
            logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
            logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
            logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

            veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

            veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
            veriflyBaseWrapper.setLogModel(logModel);

            veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
            errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

        }

        if (errorMessagesFlag) {
            accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
            ThreadLocalAccountInfo.setCustomerAccountInfoModel(accountInfoModel);//added by mudassir - place AccountinfoModel in threadLocal for reducing verifly calls

            switchWrapper.setAccountInfoModel(accountInfoModel);

            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            OLAVO olaVO = new OLAVO();

            if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50000)
                olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount())));
            else if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50006L)
                olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
            else
                olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));

            olaVO.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
            olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

            WorkFlowWrapper workflowWrapper = switchWrapper.getWorkFlowWrapper();

            // Case when distributor is recharging its account
            if (appUserModel.getDistributorContactId() != null) {
                if (workflowWrapper.getFromDistributorContactModel() != null && workflowWrapper.getFromDistributorContactModel().getDistributorIdDistributorModel() != null
                        && workflowWrapper.getFromDistributorContactModel().getDistributorIdDistributorModel().getNational()) {
                    olaVO.setReasonId(OLATransactionReasonsInterface.NATIONAL_DISTRIBUTOR_CREDIT_RECHARGE);
                } else
                    olaVO.setReasonId(OLATransactionReasonsInterface.DISTRIBUTOR_CREDIT_RECHARGE);
            } else if (appUserModel.getRetailerContactId() != null) {
                olaVO.setReasonId(OLATransactionReasonsInterface.RETAILER_CREDIT_RECHARGE);
            } else
                olaVO.setReasonId(OLATransactionReasonsInterface.RETAILER_CREDIT_RECHARGE);

            switchWrapper.setOlavo(olaVO);
//            switchWrapper.getWorkFlowWrapper().setOLASwitchWrapper(switchWrapper); // setting the switchWrapper for rollback
            switchWrapper = this.switchController.debit(switchWrapper, null);
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;
    }

//-----------Verifly method without PIN needs to be called----------------------------------------------

    public SwitchWrapper debitWithoutPin(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        AppUserModel appUserModel = null;

        if (switchWrapper.getWorkFlowWrapper().getProductModel() != null && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().longValue() == 50000L) { /*product is p2p*/

            appUserModel = new AppUserModel();
            P2PVO p2PVO = (P2PVO) switchWrapper.getWorkFlowWrapper().getProductVO();
            appUserModel.setCustomerId(p2PVO.getCustomerId());//get recipient ID

        } else {

            appUserModel = ThreadLocalAppUser.getAppUserModel();

        }

        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
        AccountInfoModel threadLocalAccountInfoModel = getThreadLocalAccountInfoModel(switchWrapper);
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        boolean errorMessagesFlag = false;
        boolean isThreadLocalAccountInfoLoaded = false;

        if (threadLocalAccountInfoModel != null && threadLocalAccountInfoModel.getAccountNo() != null) {
            accountInfoModel = threadLocalAccountInfoModel;
            errorMessagesFlag = true;
            isThreadLocalAccountInfoLoaded = true;

        } else {

            accountInfoModel.setOldPin(switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());

            if (ThreadLocalAppUser.getAppUserModel().getCustomerId() != null)
                accountInfoModel.setCustomerId(appUserModel.getCustomerId());
            else
                accountInfoModel.setCustomerId(appUserModel.getAppUserId());

            accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());

            LogModel logModel = new LogModel();
            logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
            logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
            logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

            veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

            veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
            veriflyBaseWrapper.setLogModel(logModel);

            veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            veriflyBaseWrapper = this.verifyCredentials(veriflyBaseWrapper);
            errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

        }

        if (errorMessagesFlag) {
            if (false == isThreadLocalAccountInfoLoaded) {
                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
                setThreadLocalAccountInfoModel(switchWrapper, accountInfoModel);//added by mudassir - place AccountinfoModel in threadLocal for reducing verifly calls
            }

            switchWrapper.setAccountInfoModel(accountInfoModel);
            switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            OLAVO olaVO = new OLAVO();

            olaVO.setCustomerAccountTypeId(smartMoneyAccountModel.getCustomerIdCustomerModel().getCustomerAccountTypeId());

            if (switchWrapper.getOlavo() != null) {
                olaVO.setIsBillPayment(switchWrapper.getOlavo().getIsBillPayment());
            }

            if (null != switchWrapper.getWorkFlowWrapper().getProductModel() && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50000L) {
                olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
                switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField2(StringUtil.replaceString(accountInfoModel.getAccountNo(), 5, "*"));
            } else if (null != switchWrapper.getWorkFlowWrapper().getProductModel() && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50002L)
                olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
            else
                olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount());

            olaVO.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
            olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

            WorkFlowWrapper workflowWrapper = switchWrapper.getWorkFlowWrapper();


            // Case when distributor is recharging its account
            if (appUserModel.getDistributorContactId() != null) {
                if (null != switchWrapper.getOlavo() && switchWrapper.getOlavo().getReasonId() != null) {
                    olaVO.setReasonId(switchWrapper.getOlavo().getReasonId());
                } else if (workflowWrapper.getFromDistributorContactModel() != null && workflowWrapper.getFromDistributorContactModel().getDistributorIdDistributorModel() != null
                        && workflowWrapper.getFromDistributorContactModel().getDistributorIdDistributorModel().getNational()) {
                    olaVO.setReasonId(OLATransactionReasonsInterface.NATIONAL_DISTRIBUTOR_CREDIT_RECHARGE);
                } else
                    olaVO.setReasonId(OLATransactionReasonsInterface.DISTRIBUTOR_CREDIT_RECHARGE);
            } else if (appUserModel.getRetailerContactId() != null) {
                if (null != switchWrapper.getOlavo() && switchWrapper.getOlavo().getReasonId() != null) {
                    olaVO.setReasonId(switchWrapper.getOlavo().getReasonId());
                }
                olaVO.setReasonId(OLATransactionReasonsInterface.RETAILER_CREDIT_RECHARGE);
            } else {
                if (null != switchWrapper.getOlavo() && switchWrapper.getOlavo().getReasonId() != null) {
                    olaVO.setReasonId(switchWrapper.getOlavo().getReasonId());
                } else {
                    olaVO.setReasonId(OLATransactionReasonsInterface.RETAILER_CREDIT_RECHARGE);
                }
            }
            switchWrapper.setOlavo(olaVO);

            logger.info("[OLAVeriflyFinancialInstitution.debitWithoutPin] Crediting OLA Account." +
                    " Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
                    " Transaction ID: " + olaVO.getMicrobankTransactionCode() +
                    " Beneficiary SmartMoneyAcctID:" + smartMoneyAccountModel.getSmartMoneyAccountId() +
                    " Beneficiary Account Holder Nick:" + accountInfoModel.getAccountNick());

            switchWrapper = this.switchController.debit(switchWrapper, null);
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;
    }


//------------------------------------------------------------------------------------------------------


    SwitchWrapper credit(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        AccountInfoModel threadLocalAccountInfoModel = ThreadLocalAccountInfo.getCustomerAccountInfoModel();
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();
        boolean errorMessagesFlag = false;

        if (threadLocalAccountInfoModel != null && threadLocalAccountInfoModel.getAccountNo() != null) {
            accountInfoModel = threadLocalAccountInfoModel;
            errorMessagesFlag = true;

        } else {

            String pin = (String) switchWrapper.getObject(CommandFieldConstants.KEY_PIN);

            accountInfoModel.setCustomerId(appUserModel.getCustomerId());
            accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
            accountInfoModel.setOldPin(pin);

            LogModel logModel = new LogModel();
            logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
            logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
            logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

            veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
            veriflyBaseWrapper.setLogModel(logModel);

            veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
            errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

        }

        if (errorMessagesFlag) {
            accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
            ThreadLocalAccountInfo.setCustomerAccountInfoModel(accountInfoModel);//added by mudassir - place AccountinfoModel in threadLocal for reducing verifly calls

            switchWrapper.setAccountInfoModel(accountInfoModel);

            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            if (switchWrapper.getWorkFlowWrapper() != null) {
                switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
            }
            switchWrapper.getOlavo().setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;


    }


    public SwitchWrapper closeOLAAccount(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {

        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();
        boolean errorMessagesFlag = false;


        accountInfoModel.setCustomerId(appUserModel.getCustomerId());
        accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());

        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

        veriflyBaseWrapper = this.verifyCredentials(veriflyBaseWrapper);
        errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();


        if (errorMessagesFlag) {
            accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();

            switchWrapper.setAccountInfoModel(accountInfoModel);

            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            if (switchWrapper.getWorkFlowWrapper() != null) {
                switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
            }

            OLAVO olaVO = new OLAVO();
            olaVO.setBalance(switchWrapper.getBalance());
            olaVO.setReasonId(-1L);
            olaVO.setCustomerAccountTypeId(3L);
            olaVO.setMicrobankTransactionCode("-1");
            olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
            switchWrapper.setOlavo(olaVO);
            switchWrapper.getOlavo().setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            switchWrapper = this.switchController.transaction(switchWrapper, null);
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;


    }

    public SwitchWrapper getAllOlaAccounts(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        switchWrapper = this.switchController.getAllOlaAccounts(switchWrapper);
        return switchWrapper;
    }

    public SwitchWrapper getAllAccountsWithStats(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        switchWrapper = this.switchController.getAllAccountsWithStats(switchWrapper);

        return switchWrapper;
    }

    public SwitchWrapper getAllAccountsStatsWithRange(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        switchWrapper = this.switchController.getAllAccountsStatsWithRange(switchWrapper);

        return switchWrapper;
    }

    public SwitchWrapper getAccountInfo(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        switchWrapper = this.switchController.getAccountInfo(switchWrapper);
        return switchWrapper;
    }

    public SwitchWrapper updateAccountInfo(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
        wrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        wrapper = this.switchController.updateAccountInfo(wrapper);
        return wrapper;
    }

    public SwitchWrapper changeAccountDetails(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
        wrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        wrapper = this.switchController.changeAccountDetails(wrapper);
        return wrapper;
    }

    public SwitchWrapper creditTransfer(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();
        SmartMoneyAccountModel receivingSmartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getReceivingSmartMoneyAccountModel();
        AccountInfoModel accountInfoModel = new AccountInfoModel();
        AccountInfoModel receivingAccountInfoModel = new AccountInfoModel();

        accountInfoModel.setCustomerId(appUserModel.getAppUserId());
        accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());

//		accountInfoModel.setCustomerId(switchWrapper.getWorkFlowWrapper().getToDistributorContactAppUserModel().getAppUserId());
//		accountInfoModel.setAccountNick(receivingSmartMoneyAccountModel.getName());

        receivingAccountInfoModel.setCustomerId(switchWrapper.getWorkFlowWrapper().getDistOrRetAppUserModel().getAppUserId());
        receivingAccountInfoModel.setAccountNick(receivingSmartMoneyAccountModel.getName());

        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
        logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

        LogModel receivingLogModel = new LogModel();
        receivingLogModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        receivingLogModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
        receivingLogModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        VeriflyBaseWrapper receivingVeriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

        veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
        receivingVeriflyBaseWrapper.setAccountInfoModel(receivingAccountInfoModel);
        receivingVeriflyBaseWrapper.setLogModel(receivingLogModel);
        receivingVeriflyBaseWrapper.setBasePersistableModel(receivingSmartMoneyAccountModel);
        receivingVeriflyBaseWrapper = this.verifyPin(receivingVeriflyBaseWrapper);
        boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();
        boolean receivingErrorMessagesFlag = receivingVeriflyBaseWrapper.isErrorStatus();

        if (errorMessagesFlag && receivingErrorMessagesFlag) {
            accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
            receivingAccountInfoModel = receivingVeriflyBaseWrapper.getAccountInfoModel();

            switchWrapper.setAccountInfoModel(accountInfoModel);
            switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(StringUtil.replaceString(accountInfoModel.getAccountNo(), 5, "*"));
            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            //WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
//            if(switchWrapper.getWorkFlowWrapper() != null)
//            {
////            	switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
//            }
            OLAVO olaVO = new OLAVO();
            olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount());
            olaVO.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
            olaVO.setReceivingAccNo(receivingAccountInfoModel.getAccountNo());
            olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            if (switchWrapper.getWorkFlowWrapper().getTransactionTypeModel().getTransactionTypeId() == TransactionTypeConstantsInterface.DIST_TO_DIST_TX) {
                olaVO.setReasonId(OLATransactionReasonsInterface.DIST_DIST_CREDIT_TRSFR);
            } else if (switchWrapper.getWorkFlowWrapper().getTransactionTypeModel().getTransactionTypeId() == TransactionTypeConstantsInterface.DIST_TO_RET_TX) {
                olaVO.setReasonId(OLATransactionReasonsInterface.DIST_RET_CREDIT_TRSFR);
            } else if (switchWrapper.getWorkFlowWrapper().getTransactionTypeModel().getTransactionTypeId() == TransactionTypeConstantsInterface.RET_TO_RET_TX) {
                olaVO.setReasonId(OLATransactionReasonsInterface.RET_RET_CREDIT_TRSFR);
            }
            switchWrapper.setOlavo(olaVO);
            switchWrapper = this.switchController.creditTransfer(switchWrapper);
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;

    }

    @Override
    public SwitchWrapper settleP2PCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        //get the pool account
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
        commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
        commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

//		StakeholderBankInfoModel commissionReconBankInfoModel = new StakeholderBankInfoModel();
//		commissionReconBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_ACCOUNT_ID);
//		searchBaseWrapper.setBasePersistableModel(commissionReconBankInfoModel);
//		commissionReconBankInfoModel = (StakeholderBankInfoModel)stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
//
////		AccountInfoModel accountInfoModel = new AccountInfoModel();
////		accountInfoModel.setOldPin(switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
//
//		switchWrapper.setFromAccountNo(customerPoolBankInfoModel.getAccountNo());//TODO: get the pool account
//		switchWrapper.setToAccountNo(commissionReconBankInfoModel.getAccountNo());
//		switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmount())));
//		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//		switchWrapper.setToCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
//		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		/*
		switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
		*/
        OLAVO olavo = new OLAVO();
        olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount())));
        olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olavo.setPayingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
        olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olavo.setCustomerAccountTypeId(3L);

        olavo.setReasonId(-1L);

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(50110L);
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olavo);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());
//		newSwitchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
//		newSwitchWrapper.setBankId(50110L);
//		newSwitchWrapper.setPaymentModeId(6L);
//		newSwitchWrapper.setFromAccountNo(customerPoolBankInfoModel.getAccountNo());
//		newSwitchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//		newSwitchWrapper.setFromCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
//		newSwitchWrapper.setToAccountNo(commissionReconBankInfoModel.getAccountNo());
//		newSwitchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//		newSwitchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount())));
//		newSwitchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        logger.info("[OLAVeriflyFinancialInstitution.settleP2PCommission] Credit Recon Mirror Account. LoggedIn AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + olavo.getMicrobankTransactionCode());
        newSwitchWrapper = this.switchController.debit(newSwitchWrapper, null);

        return switchWrapper;

    }


    @Override
    public SwitchWrapper settleCashWithdrawalCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        //get the pool account
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
        commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
        commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

//		StakeholderBankInfoModel commissionReconBankInfoModel = new StakeholderBankInfoModel();
//		commissionReconBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_ACCOUNT_ID);
//		searchBaseWrapper.setBasePersistableModel(commissionReconBankInfoModel);
//		commissionReconBankInfoModel = (StakeholderBankInfoModel)stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
//
//		AccountInfoModel accountInfoModel = new AccountInfoModel();
//		accountInfoModel.setOldPin(switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());

//		switchWrapper.setFromAccountNo(commissionReconPoolBankInfoModel.getAccountNo());//TODO: get the pool account
//		switchWrapper.setToAccountNo(switchWrapper.getAccountInfoModel().getAccountNo());
//		switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble((switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFedCommissionAmount()) -  switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
//		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//		switchWrapper.setToCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
//		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//		/*
//		switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
        OLAVO olavo = new OLAVO();
        olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
        olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olavo.setPayingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
        olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olavo.setCustomerAccountTypeId(3L);

        olavo.setReasonId(-1L);

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olavo);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());
//		newSwitchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
//		newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
//		newSwitchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
//		newSwitchWrapper.setFromAccountNo(customerPoolBankInfoModel.getAccountNo());
//		newSwitchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//		newSwitchWrapper.setFromCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
//		newSwitchWrapper.setToAccountNo(commissionReconBankInfoModel.getAccountNo());
//		newSwitchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//		newSwitchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() -  switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
//		newSwitchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        newSwitchWrapper = this.switchController.debit(newSwitchWrapper, null);

        return switchWrapper;

    }

    @Override
    public SwitchWrapper settleCashDepositCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        //get the pool account

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
        commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
        commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

//		StakeholderBankInfoModel commissionReconBankInfoModel = new StakeholderBankInfoModel();
//		commissionReconBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_ACCOUNT_ID);
//		searchBaseWrapper.setBasePersistableModel(commissionReconBankInfoModel);
//		commissionReconBankInfoModel = (StakeholderBankInfoModel)stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
//
//		AccountInfoModel accountInfoModel = new AccountInfoModel();
//		accountInfoModel.setOldPin(switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());

//		switchWrapper.setFromAccountNo(customerPoolBankInfoModel.getAccountNo());//TODO: get the pool account
//		switchWrapper.setToAccountNo(switchWrapper.getAccountInfoModel().getAccountNo());
//		switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmount() -  switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
//		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//		switchWrapper.setToCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
//		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		/*
		switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
		*/
        OLAVO olavo = new OLAVO();
        olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
        olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olavo.setPayingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
        olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olavo.setCustomerAccountTypeId(3L);

        olavo.setReasonId(-1L);

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olavo);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());
//		newSwitchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
//		newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
//		newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
//		newSwitchWrapper.setFromAccountNo(commissionReconBankInfoModel.getAccountNo());
//		newSwitchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//		newSwitchWrapper.setFromCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
//		newSwitchWrapper.setToAccountNo(commissionReconBankInfoModel.getAccountNo());
//		newSwitchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//		newSwitchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() -  switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
//		newSwitchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());

        logger.info("[OLAVeriflyFinancialInstitution.settleCashDepositCommission] Credit Recon Mirror Account. LoggedIn AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + olavo.getMicrobankTransactionCode());
        newSwitchWrapper = this.switchController.debit(newSwitchWrapper, null);

        return switchWrapper;
    }


    public void setStakeholderBankInfoManager(
            StakeholderBankInfoManager stakeholderBankInfoManager) {
        this.stakeholderBankInfoManager = stakeholderBankInfoManager;
    }

    /*sundary account transaction*/
    public SwitchWrapper accountToCashTransaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
//		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel accountToCashSundaryBankInfoModel = new StakeholderBankInfoModel();
        accountToCashSundaryBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(accountToCashSundaryBankInfoModel);
        accountToCashSundaryBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

        OLAVO olavo = switchWrapper.getOlavo();
//		olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount())));
        olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));
//		olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olavo.setPayingAccNo(accountToCashSundaryBankInfoModel.getAccountNo());
        olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olavo.setCustomerAccountTypeId(3L);//add & use account type SETTLEMENT from constants

        olavo.setReasonId(-1L);//Special Reason is used for sundry account transaction. need to add constant for this

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olavo);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

        StringBuilder logString = new StringBuilder("[OLAVeriflyFinancialInstitutionImpl.accountToCashTransaction] Going to Debit/Credit A2C Sundry A/C. LoggedIn user SmartMoneyAccountId: ")
                .append(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getSmartMoneyAccountId())
                .append(" LoggedIn User AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId())
                .append(" Trx ID:" + olavo.getMicrobankTransactionCode());

        logger.info(logString.toString());

        newSwitchWrapper = this.switchController.debit(newSwitchWrapper, null);

        return newSwitchWrapper;

    }


    public SwitchWrapper collectionPaymentTransaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        Integer FT_ORDER = switchWrapper.getFtOrder();

        String commissionReconAccount = (String) switchWrapper.getObject(String.valueOf(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID));
        String franchiseAccount = null;
        String userAccount = switchWrapper.getAccountInfoModel().getAccountNo();
        String supplierAccount = switchWrapper.getWorkFlowWrapper().getSupplierBankInfoModel().getAccountNo();

        String fromAccountNumber = null;
        String toAccountNumber = null;
        Double amount = null;

        Boolean isAgent = (ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER) ? Boolean.TRUE : Boolean.FALSE;

        switch (FT_ORDER) {

            case 1: {

                fromAccountNumber = userAccount;                                                            // AGENT/CUSTOMER ACCOUNT
                toAccountNumber = supplierAccount;                                                            // SUPPLIER BB ACCOUNT

                if (isAgent) {
                    amount = (switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() -
                            (switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())) -
                            switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise1CommissionAmount();
                } else {
                    amount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount();
                }

                break;
            }

            case 2: {

                fromAccountNumber = supplierAccount;                                                            // SUPPLIER BB ACCOUNT
                toAccountNumber = commissionReconAccount;                                                    // COMMISSION RECON ACCOUNT
                switchWrapper.getOlavo().setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);

                if (isAgent) {

                    amount = (switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFedCommissionAmount())
                            - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount();
                } else {
                    amount = switchWrapper.getWorkFlowWrapper().getTotalCommissionAmount();
                }

                break;
            }

            case 3: {

                FRANCHISE_ACCOUNT:
                {
                    TransactionModel transactionModel = switchWrapper.getWorkFlowWrapper().getTransactionModel();
                    AccountInfoModel franchiseAccountInfoModel = loadFranchiseAccountInfo(switchWrapper.getWorkFlowWrapper().getAppUserModel().getAppUserId(), transactionModel.getTransactionCodeId());
                    franchiseAccount = franchiseAccountInfoModel.getAccountNo();
                }

                fromAccountNumber = commissionReconAccount;                                                // COMMISSION RECON ACCOUNT
                toAccountNumber = franchiseAccount;                                                            // FRANCHISE ACCOUNT

                if (isAgent) {
                    amount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount();
                } else {
                    amount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount();
                }

                break;
            }

            case 4: {

                fromAccountNumber = userAccount;                                                            // AGENT ACCOUNT
                toAccountNumber = supplierAccount;                                                            // SUPPLIER BB ACCOUNT

                if (isAgent) {
                    amount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount();
                } else {
                    amount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount();
                }

                break;
            }
        }

        switchWrapper.getOlavo().setPayingAccNo(fromAccountNumber);
        switchWrapper.getOlavo().setReceivingAccNo(toAccountNumber);
        switchWrapper.getOlavo().setBalance(Double.parseDouble(Formatter.formatDouble(amount)));

        switchWrapper.setFromAccountNo(fromAccountNumber);
        switchWrapper.setToAccountNo(toAccountNumber);
        switchWrapper.setTransactionAmount(amount);

        if (FT_ORDER > 0) {

            logger.info("Collection Payment Transaction");
            logger.info("FT Number    : " + FT_ORDER);
            logger.info("From Account : " + fromAccountNumber);
            logger.info("To Account : " + toAccountNumber);
            logger.info("Amount : " + amount);

            switchWrapper = debitCreditAccount(switchWrapper);
        }
        return switchWrapper;
    }


    /*sundary account transaction*/
    public SwitchWrapper donationTransaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();

//		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
//		StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
//		stakeholderBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.SKMCH_DONATION_SUNDRY_ACCOUNT_ID);
//		searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
//		stakeholderBankInfoModel = (StakeholderBankInfoModel)stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

//		String payingAccNo = stakeholderBankInfoModel.getAccountNo();
        String payingAccNo = switchWrapper.getWorkFlowWrapper().getSupplierBankInfoModel().getAccountNo();

        OLAVO olaVO = switchWrapper.getOlavo();
        olaVO.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount())));
        olaVO.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olaVO.setPayingAccNo(payingAccNo);
        olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaVO.setCustomerAccountTypeId(3L);
        olaVO.setReasonId(-1L);//Special Reason is used for sundry account transaction. need to add constant for this

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olaVO);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

        StringBuilder logString = new StringBuilder("[OLAVeriflyFinancialInstitutionImpl.donationTransaction] Going to Credit Donation Sundry A/C. LoggedIn user SmartMoneyAccountId: ")
                .append(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getSmartMoneyAccountId())
                .append(" LoggedIn User AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId())
                .append(" Trx ID:" + olaVO.getMicrobankTransactionCode());

        logger.info(logString.toString());

        newSwitchWrapper = this.switchController.debit(newSwitchWrapper, null);

        return newSwitchWrapper;
    }


    /* (non-Javadoc)
     * @see com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution#donationTransactionDr(com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper)
     */
    public SwitchWrapper donationTransactionDr(SwitchWrapper switchWrapper) throws FrameworkCheckedException {

        SupplierBankInfoModel supplierBankInfoModel = switchWrapper.getWorkFlowWrapper().getSupplierBankInfoModel();

        TransactionModel transactionModel = switchWrapper.getWorkFlowWrapper().getTransactionModel();

        OLAVO olaVO = new OLAVO();
        olaVO.setBalance(transactionModel.getTotalAmount() - transactionModel.getTotalCommissionAmount());
        olaVO.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaVO.setPayingAccNo(supplierBankInfoModel.getAccountNo());
        olaVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaVO.setCustomerAccountTypeId(3L);
        olaVO.setReasonId(-1L);

        SwitchWrapper _switchWrapper = new SwitchWrapperImpl();
        _switchWrapper.setBankId(supplierBankInfoModel.getBankId());
        _switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        _switchWrapper.setOlavo(olaVO);
        _switchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

        logger.info("[OlaVeriflyFinancialInstitution.donationTransactionDr]");
        logger.info("Microbank Transaction Code : " + olaVO.getMicrobankTransactionCode());

        _switchWrapper = this.switchController.debit(_switchWrapper, null);

        return _switchWrapper;
    }


    /*sundary account transaction*/
    public SwitchWrapper cashToCashTransaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
//		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel accountToCashSundaryBankInfoModel = new StakeholderBankInfoModel();
        accountToCashSundaryBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(accountToCashSundaryBankInfoModel);
        accountToCashSundaryBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

        OLAVO olavo = switchWrapper.getOlavo();

        if (switchWrapper.isCashToCashLeg2()) {

            olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount())));

        } else {//Also put franchise 2 commission in cash to cash sundry account

            olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));

        }
//		olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olavo.setPayingAccNo(accountToCashSundaryBankInfoModel.getAccountNo());
        olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olavo.setCustomerAccountTypeId(3L);//add & use account type SETTLEMENT from constants

        olavo.setReasonId(-1L);//Special Reason is used for sundry account transaction. need to add constant for this

//		olavo.setRequiresNewTrx(Boolean.TRUE);//C2C Sundry account is debited/credit in NEW Transaction as per maqsood's suggestion.

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olavo);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

        StringBuilder logString = new StringBuilder("[OLAVeriflyFinancialInstitutionImpl.CashToCashTransaction] Going to Debit/Credit C2C Sundry A/C. LoggedIn user SmartMoneyAccountId: ")
                .append(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getSmartMoneyAccountId())
                .append(" LoggedIn User AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId())
                .append(" Trx ID:" + olavo.getMicrobankTransactionCode());

        logger.info(logString.toString());

        newSwitchWrapper = this.switchController.debit(newSwitchWrapper, null);

        return newSwitchWrapper;

    }

    @Override
    public SwitchWrapper bulkPaymentTransaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());

        OLAInfo olaInfo = new OLAInfo();

        StakeholderBankInfoModel productStakeholderBankInfoModel = (StakeholderBankInfoModel) switchWrapper.getWorkFlowWrapper().getObject("PRODUCT_ACCOUNT");
        switchWrapper.setFromAccountNo(productStakeholderBankInfoModel.getAccountNo());

        if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(2510816L)) {
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.BULK_PAYMENT,
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    PoolAccountConstantsInterface.BULK_DISBURSEMENT_POOL_ACCOUNT_OLA,
                    PoolAccountConstantsInterface.OF_SETTLEMENT_BULK_DISBURSEMENT_POOL_ACCOUNT,
                    switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalAmount()
            );
        } else {
            //Dr Product Based Sundry Account
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.BULK_PAYMENT,
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    productStakeholderBankInfoModel.getStakeholderBankInfoId(),
                    productStakeholderBankInfoModel.getOfSettlementStakeholderBankInfoModelId(),
                    switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalAmount()
            );
        }
        debitList.add(olaInfo);

        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Debit Incl 3rd Party account at Leg II
            totalInclusiveCharges = 0.0;
        }

        // ThirdPartyCheck to be used at leg II
        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setThirdPartyCheck(inclChargesCheck);
        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        //Cr Funds Transfer Sundry A/C
        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.BULK_PAYMENT,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID,
                PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID,
                switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalAmount()
        );

        creditList.add(olaInfo);

        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);

        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
        olaVO.setCategory(TransactionConstantsInterface.FIRST_LEG_CATEGORY_ID); // category = Leg 1

        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        return switchWrapper;
    }


    public SwitchWrapper bulkDisbursmentPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException {

        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

        OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.BULK_PAYMENT,
                TransactionTypeConstantsInterface.OLA_DEBIT,
                PoolAccountConstantsInterface.BULK_DISBURSEMENT_POOL_ACCOUNT_OLA,
                PoolAccountConstantsInterface.OF_SETTLEMENT_BULK_DISBURSEMENT_POOL_ACCOUNT,
                (Double) switchWrapper.getObject(CommandFieldConstants.KEY_TX_AMOUNT)
        );

        debitList.add(olaInfo);

        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.BULK_PAYMENT,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_OLA,
                PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_CORE,
                (Double) switchWrapper.getObject(CommandFieldConstants.KEY_TX_AMOUNT)
        );

        creditList.add(olaInfo);

        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);

        if (switchWrapper.getWorkFlowWrapper().getProductModel() != null) {

            olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
        }

        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        return switchWrapper;
    }

    public SwitchWrapper bulkDisbursementSumFT(SwitchWrapper switchWrapper) throws Exception {

        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();
        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();
        olaVO.setCreditAccountList(creditList);
        olaVO.setDebitAccountList(debitList);
        switchWrapper.setOlavo(olaVO);

        olaVO.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
        olaVO.setTransactionDateTime(new Date());
        olaVO.setProductId(workFlowWrapper.getProductModel().getProductId());

        switchWrapper.getOlavo().setReasonId(ReasonConstants.BULK_PAYMENT);

        CommissionAmountsHolder amountsHolder = workFlowWrapper.getCommissionAmountsHolder();

        Double totalAmount = CommonUtils.formatAmountTwoDecimal(amountsHolder.getTransactionAmount() + amountsHolder.getTransactionProcessingAmount());

        OLAInfo olaInfo = new OLAInfo();
        long productId = Long.parseLong(MessageUtil.getMessage("SalaryDisbursementProductId"));
        if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(productId)) {
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.BULK_PAYMENT,
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    PoolAccountConstantsInterface.BULK_DISBURSEMENT_SUNDRY_BLB,
                    PoolAccountConstantsInterface.OF_SETTLEMENT_BULK_DISBURSEMENT_POOL_ACCOUNT,
                    totalAmount
            );
        }
        else {
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.BULK_PAYMENT,
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    PoolAccountConstantsInterface.T24_SETTLEMENT_ACCOUNT_ID,
                    PoolAccountConstantsInterface.OF_SETTLEMENT_IFT_POOL_ACCOUNT,
                    totalAmount
            );
        }

        debitList.add(olaInfo);

        StakeholderBankInfoModel productBLBStakeholderBankInfoModel = (StakeholderBankInfoModel) switchWrapper.getObject("TARGET_SBI");
        ;

        // Cr Product based Bulk Sundry tx amount + exclusive charges
        olaInfo = new OLAInfo();
        olaInfo.setIsAgent(false);
        olaInfo.setBalanceAfterTrxRequired(Boolean.FALSE);
        olaInfo.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);

        if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(productId)) {
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.BULK_PAYMENT,
                    TransactionTypeConstantsInterface.OLA_CREDIT,
                    PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_OLA,
                    PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_CORE,
                    totalAmount
            );
        } else {
            olaInfo.setReasonId(ReasonConstants.BULK_PAYMENT);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setPayingAccNo(productBLBStakeholderBankInfoModel.getAccountNo());
            olaInfo.setCoreStakeholderBankInfoId(productBLBStakeholderBankInfoModel.getOfSettlementStakeholderBankInfoModelId());
            olaInfo.setBalance(totalAmount);
        }

        creditList.add(olaInfo);

        olaVO.setFromSegmentId(workFlowWrapper.getFromSegmentId());
        olaVO.setToSegmentId(workFlowWrapper.getToSegmentId());
        olaVO.setProductId(workFlowWrapper.getProductModel().getProductId());
        switchWrapper.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        // setting OlaSwitchWrapper to be used in TransactionProcessor for Day End settlement process
        workFlowWrapper.setOLASwitchWrapper(switchWrapper);

        return switchWrapper;

    }

    public SwitchWrapper bulkDisbursementFromL2SumFT(SwitchWrapper switchWrapper) throws Exception {

        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();
        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();
        olaVO.setCreditAccountList(creditList);
        olaVO.setDebitAccountList(debitList);
        switchWrapper.setOlavo(olaVO);

        olaVO.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
        olaVO.setTransactionDateTime(new Date());
        olaVO.setProductId(workFlowWrapper.getProductModel().getProductId());

        Long accountTypeId = 0L;
        if (workFlowWrapper.getAccountInfoModel().getAccountTypeId() != CustomerAccountTypeConstants.SETTLEMENT) {
            accountTypeId = workFlowWrapper.getCustomerModel().getCustomerAccountTypeId();
        } else {
            accountTypeId = workFlowWrapper.getAccountInfoModel().getAccountTypeId();
        }
        switchWrapper.getOlavo().setReasonId(ReasonConstants.BULK_PAYMENT);


        CommissionAmountsHolder amountsHolder = workFlowWrapper.getCommissionAmountsHolder();

        Double totalAmount = CommonUtils.formatAmountTwoDecimal(amountsHolder.getTransactionAmount() + amountsHolder.getTransactionProcessingAmount());

        // Dr (L2 Corporate A/c) debit entry tx amount + exclusive charges
        OLAInfo olaInfo = new OLAInfo();
        long productId = Long.parseLong(MessageUtil.getMessage("SalaryDisbursementProductId"));
        if(workFlowWrapper.getProductModel().getProductId().equals(productId)){
            olaInfo.setIsAgent(false);
            olaInfo = getStakeholderOLAInfo(workFlowWrapper.getTransactionCodeModel().getCode(),
                    ReasonConstants.BULK_PAYMENT,
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    PoolAccountConstantsInterface.BULK_DISBURSEMENT_SUNDRY_BLB,
                    PoolAccountConstantsInterface.OF_SETTLEMENT_BULK_DISBURSEMENT_POOL_ACCOUNT,
                    totalAmount
            );
        }
        else {
            olaInfo.setIsAgent(false);
            olaInfo.setBalanceAfterTrxRequired(false);
            olaInfo.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(accountTypeId);
            olaInfo.setReasonId(ReasonConstants.BULK_PAYMENT);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            olaInfo.setBalance(totalAmount);
            olaInfo.setPayingAccNo(switchWrapper.getFromAccountNo());
        }
        debitList.add(olaInfo);

        StakeholderBankInfoModel productBLBStakeholderBankInfoModel = switchWrapper.getStakeholderBankInfoModel();

        // Cr Product based Bulk Sundry tx amount + exclusive charges
        olaInfo = new OLAInfo();
//        productId = Long.parseLong(MessageUtil.getMessage("SalaryDisbursementProductId"));
        if(workFlowWrapper.getProductModel().getProductId().equals(productId)){
            olaInfo.setIsAgent(false);
            olaInfo = getStakeholderOLAInfo(workFlowWrapper.getTransactionCodeModel().getCode(),
                    ReasonConstants.BULK_PAYMENT,
                    TransactionTypeConstantsInterface.OLA_CREDIT,
                    PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_OLA,
                    PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_CORE,
                    totalAmount
            );
        }
        else {
            olaInfo.setIsAgent(false);
            olaInfo.setBalanceAfterTrxRequired(Boolean.FALSE);
            olaInfo.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
            olaInfo.setReasonId(ReasonConstants.BULK_PAYMENT);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setPayingAccNo(productBLBStakeholderBankInfoModel.getAccountNo());
            olaInfo.setCoreStakeholderBankInfoId(productBLBStakeholderBankInfoModel.getOfSettlementStakeholderBankInfoModelId());
            olaInfo.setBalance(totalAmount);
        }

        creditList.add(olaInfo);


        olaVO.setFromSegmentId(workFlowWrapper.getFromSegmentId());
        olaVO.setToSegmentId(workFlowWrapper.getToSegmentId());
        olaVO.setProductId(workFlowWrapper.getProductModel().getProductId());
        switchWrapper.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        // setting OlaSwitchWrapper to be used in TransactionProcessor for Day End settlement process
        workFlowWrapper.setOLASwitchWrapper(switchWrapper);

        return switchWrapper;
    }

    public SwitchWrapper makeBulkDisbursmentToCustomer(SwitchWrapper switchWrapper) throws Exception {
        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();

        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();


        OLAInfo olaInfo = new OLAInfo();

        StakeholderBankInfoModel productStakeholderBankInfoModel = (StakeholderBankInfoModel) workFlowWrapper.getObject("PRODUCT_ACCOUNT");
        String toAccountNumber = workFlowWrapper.getAccountInfoModel().getAccountNo();

        switchWrapper.setFromAccountNo(productStakeholderBankInfoModel.getAccountNo());
        switchWrapper.setToAccountNo(toAccountNumber);

        long productId = Long.parseLong(MessageUtil.getMessage("SalaryDisbursementProductId"));
        if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(productId)) {
            olaInfo = getStakeholderOLAInfo(workFlowWrapper.getTransactionCodeModel().getCode(),
                    ReasonConstants.BULK_PAYMENT,
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_OLA,
                    PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_CORE,
                    workFlowWrapper.getCommissionAmountsHolder().getTotalAmount()
            );
        }
        else {
            //Dr Product Based Sundry Account
            olaInfo = getStakeholderOLAInfo(workFlowWrapper.getTransactionCodeModel().getCode(),
                    ReasonConstants.BULK_PAYMENT,
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    productStakeholderBankInfoModel.getStakeholderBankInfoId(),
                    productStakeholderBankInfoModel.getOfSettlementStakeholderBankInfoModelId(),
                    workFlowWrapper.getCommissionAmountsHolder().getTotalAmount()
            );
        }
        debitList.add(olaInfo);

        Boolean inclChargesCheck = workFlowWrapper.getProductModel().getInclChargesCheck();

        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getInclusivePercentAmount());

        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Dr Inclusive Charges Sundry A/C
            olaInfo = getOLAInfoForInclCharges(workFlowWrapper.getTransactionCodeModel().getCode(),
                    ReasonConstants.ACCOUNT_TO_ACCOUNT,
                    workFlowWrapper.getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            debitList.add(olaInfo);

            totalInclusiveCharges = 0.0;
        }

        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        Long accountTypeId = null;
        if (workFlowWrapper.getCustomerModel() != null) {
            accountTypeId = workFlowWrapper.getCustomerModel().getCustomerAccountTypeId();
        } else {
            accountTypeId = workFlowWrapper.getRetailerContactModel().getOlaCustomerAccountTypeModelId();
        }

        Double blbCreditAmount = Double.valueOf(Formatter.formatDouble(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount() - totalInclusiveCharges));

        //Cr Customer/Agent A/C
        olaInfo = new OLAInfo();
        olaInfo.setIsAgent(false);
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(accountTypeId);
        olaInfo.setReasonId(ReasonConstants.BULK_PAYMENT);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olaInfo.setPayingAccNo(toAccountNumber);
        if (workFlowWrapper.getLoanAmount() != null) {
            if (workFlowWrapper.getLoanAmount() > 0.0) {
                olaInfo.setBalance(blbCreditAmount - workFlowWrapper.getLoanAmount());
            }
        } else {
            olaInfo.setBalance(blbCreditAmount);
        }
        creditList.add(olaInfo);

//        if (workFlowWrapper.getLoanAmount() != null) {
//            if (workFlowWrapper.getLoanAmount() > 0.0) {
//                olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
//                        ReasonConstants.DEBIT_PAYMENT_API,
//                        TransactionTypeConstantsInterface.OLA_CREDIT,
//                        PoolAccountConstantsInterface.ADVANCE_SALARY_LOAN_BLB,
//                        PoolAccountConstantsInterface.ADVANCE_SALARY_LOAN_GL,
//                        workFlowWrapper.getLoanAmount()
//                );
//
//                creditList.add(olaInfo);
//            }
//        }
        // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
        prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.BULK_PAYMENT);

        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
        this.switchController.debitCreditAccount(switchWrapper);

        // setting OlaSwitchWrapper to be used for Day End settlement process
        switchWrapper.getWorkFlowWrapper().setOLASwitchWrapper(switchWrapper);

        return switchWrapper;
    }

    public SwitchWrapper creditBulkDisbursmentOLA(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

        OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.SALARY_DISBURSEMENT,
                TransactionTypeConstantsInterface.OLA_DEBIT,
                PoolAccountConstantsInterface.BULK_DISBURSEMENT_POOL_ACCOUNT_OLA,
                PoolAccountConstantsInterface.OF_SETTLEMENT_BULK_DISBURSEMENT_POOL_ACCOUNT,
                switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount()
        );

        debitList.add(olaInfo);

        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.SALARY_DISBURSEMENT,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_OLA,
                PoolAccountConstantsInterface.BULK_DISBURSEMENT_SETTLEMENT_ACCOUNT_CORE,
                switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount()
        );

        creditList.add(olaInfo);

        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);

        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        return switchWrapper;
    }



	/*public SwitchWrapper bulkPaymentTransaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException,Exception,ImplementationNotSupportedException{

		  SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();

		  SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		  StakeholderBankInfoModel bulkPaymentankInfoModel = new StakeholderBankInfoModel();
		  bulkPaymentankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.BULK_PAYMENT_SUNDRY_OLA_ACCOUNT_ID);
		  searchBaseWrapper.setBasePersistableModel(bulkPaymentankInfoModel);
		  bulkPaymentankInfoModel = (StakeholderBankInfoModel)stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

		  if (switchWrapper.getWorkFlowWrapper().getTransactionModel() != null && switchWrapper.getWorkFlowWrapper().getTransactionModel().getBankAccountNo() == null) {
			  searchBaseWrapper = new SearchBaseWrapperImpl();
			  StakeholderBankInfoModel bulkPaymentankPoolInfoModel = new StakeholderBankInfoModel();
			  bulkPaymentankPoolInfoModel.setPrimaryKey(PoolAccountConstantsInterface.BULK_PAYMENT_IDP_POOL_ACCOUNT_ID);
			  searchBaseWrapper.setBasePersistableModel(bulkPaymentankPoolInfoModel);
			  bulkPaymentankPoolInfoModel = (StakeholderBankInfoModel)stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
			  switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(bulkPaymentankPoolInfoModel.getAccountNo());
		  }


		  OLAVO olavo = switchWrapper.getOlavo();

		  if (olavo.getTransactionTypeId().equals(TransactionTypeConstantsInterface.OLA_CREDIT)) {//Leg 1
			  olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() -
					  switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmount() +
					  switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() +
					  switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));

		  }else{//Leg 2
			  olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() -
					  switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmount() +
					  switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() +
					  switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));

		  }

		  olavo.setPayingAccNo(bulkPaymentankInfoModel.getAccountNo());
		  olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
		  olavo.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
		  olavo.setReasonId(-1L);//Special Reason

		  olavo.setRequiresNewTrx(Boolean.TRUE);
		  olavo.setRequiresNoCrypto(Boolean.TRUE);
		  SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
		  newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
		  newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		  newSwitchWrapper.setOlavo(olavo);
		  newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

		  StringBuilder logString = new StringBuilder("[OLAVeriflyFinancialInstitutionImpl.bulkPaymentTransaction] Going to Credit Bulk Payment Sundry A/C...")
		  .append(" LoggedIn User AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId())
		  .append(" Trx ID:" + olavo.getMicrobankTransactionCode());

		  logger.info(logString.toString());

		  newSwitchWrapper = this.switchController.debit(newSwitchWrapper, null);

		  return newSwitchWrapper;

		 }*/

    /*make walk in customer ledger entry*/
    public SwitchWrapper saveWalkinCustomerLedgerEntry(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        Long productId = switchWrapper.getWorkFlowWrapper().getProductModel().getProductId();
        Long reasonId = ReasonConstants.CUSTOMER_ACCOUNT_TO_CASH;

        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getRecipientWalkinSmartMoneyAccountModel();
        WalkinCustomerModel walkinCustomerModel = switchWrapper.getWorkFlowWrapper().getRecipientWalkinCustomerModel();

        String[] recipientSenderCNICList = {walkinCustomerModel.getCnic(), null};
        String[] crDrTranList = {TransactionTypeConstantsInterface.OLA_CREDIT, TransactionTypeConstantsInterface.OLA_DEBIT};

        StringBuilder logString = new StringBuilder("[OLAVeriflyFinancialInstitutionImpl.saveWalkinCustomerLedgerEntry] ");
        if (50011L == productId.longValue()) {    // In case of C2C

            reasonId = ReasonConstants.CUSTOMER_CASH_TO_CASH;
            walkinCustomerModel = switchWrapper.getWorkFlowWrapper().getSenderWalkinCustomerModel();
            recipientSenderCNICList[1] = walkinCustomerModel.getCnic();
            logString.append("Cash to Cash - ");

        } else {

            logString.append("Account to Cash - ");
        }

        for (Integer i = 0; i < recipientSenderCNICList.length; i++) {

            String CNIC = recipientSenderCNICList[i];

            if (CNIC == null) {
                // No Sender Walkin Customer Available in current iteration
                continue;
            }

            OLAVO olavo = new OLAVO();
            olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount())));
            olavo.setTransactionTypeId(crDrTranList[i]);
            olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olavo.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);//add & use account type SETTLEMENT from constants
            olavo.setCnic(CommonUtils.maskWalkinCustomerCNIC(CNIC));
            olavo.setReasonId(reasonId);//need to add constant for this

            newSwitchWrapper = new SwitchWrapperImpl();
            newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            newSwitchWrapper.setOlavo(olavo);
            newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

            StringBuffer logString2 = new StringBuffer();
            logger.info(logString2.append(logString).append("Saving Walkin Customer Ledger Entry for Cnic ").append(CNIC).append(" Sender SmartMoneyAccountId: ").append(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getSmartMoneyAccountId()));

            newSwitchWrapper = this.switchController.saveWalkinCustomerLedgerEntry(newSwitchWrapper);

            if (i == 0) {
                switchWrapper.getWorkFlowWrapper().setOlaSwitchWrapper_4(newSwitchWrapper);
                newSwitchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField12(newSwitchWrapper.getOlavo().getAuthCode());

				/*//save auth codes of walkin customer ledgers in Transaction
				if(50011L == productId.longValue()) {	// In case of C2C

					newSwitchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField11(newSwitchWrapper.getOlavo().getAuthCode());

				}else{//Account tO CASH

					newSwitchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField12(newSwitchWrapper.getOlavo().getAuthCode());

				}*/

            } else {
                switchWrapper.getWorkFlowWrapper().setOlaSwitchWrapper_3(newSwitchWrapper);
                newSwitchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField11(newSwitchWrapper.getOlavo().getAuthCode());
            }

        }

        return newSwitchWrapper;

    }

    public SwitchWrapper saveBulkPaymentWalkInLedgerEntry(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();

        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getRecipientWalkinSmartMoneyAccountModel();
        WalkinCustomerModel walkinCustomerModel = switchWrapper.getWorkFlowWrapper().getRecipientWalkinCustomerModel();

        StringBuilder logString = new StringBuilder("[OLAVeriflyFinancialInstitutionImpl.saveBulkPaymentWalkInLedgerEntry] Bulk Payment - ");
        String CNIC = walkinCustomerModel.getCnic();

        OLAVO olavo = new OLAVO();
        olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount())));
        olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olavo.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
        olavo.setCnic(CommonUtils.maskWalkinCustomerCNIC(CNIC));
        olavo.setReasonId(ReasonConstants.BULK_PAYMENT);
        if (!switchWrapper.getWorkFlowWrapper().isWalkinLimitApplicable()) {
            olavo.setExcludeLimit(true);
        }

        newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olavo);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

        StringBuffer logString2 = new StringBuffer();
        logger.info(logString2.append(logString).append("Saving Walkin Customer Ledger Entry for Cnic ").append(CNIC).append(" Sender SmartMoneyAccountId: ").append(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getSmartMoneyAccountId()));

        newSwitchWrapper = this.switchController.saveWalkinCustomerLedgerEntry(newSwitchWrapper);

        return newSwitchWrapper;

    }

    /**
     * Used in CNIC_TO_BB and CNIC_TO_CORE transactions
     *
     * @param switchWrapper
     * @return
     * @throws FrameworkCheckedException
     * @throws Exception
     * @throws ImplementationNotSupportedException
     */
    @Override
    public SwitchWrapper saveWalkInLedgerEntry(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        Long productId = switchWrapper.getWorkFlowWrapper().getProductModel().getProductId();

        String CNIC = switchWrapper.getWorkFlowWrapper().getWalkInCustomerCNIC();

        OLAVO olavo = new OLAVO();
        olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount())));
        olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olavo.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
        olavo.setCnic(CommonUtils.maskWalkinCustomerCNIC(CNIC));

        if (productId == ProductConstantsInterface.CNIC_TO_BB_ACCOUNT.longValue()) {
            olavo.setReasonId(ReasonConstants.CNIC_TO_BB_AC);
        } else if (productId == ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT.longValue()) {
            olavo.setReasonId(ReasonConstants.CNIC_TO_CORE_AC);
        }

        newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olavo);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

        logger.info("Saving Walkin Customer Ledger Entry for Cnic " + CNIC + ", trxId: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

        newSwitchWrapper = this.switchController.saveWalkinCustomerLedgerEntry(newSwitchWrapper);

        return newSwitchWrapper;

    }

    @Override
    public SwitchWrapper settleAccountToCashCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
//		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)switchWrapper.getBasePersistableModel();
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();

        //get the pool account

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
        commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
        commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

        OLAVO olavo = new OLAVO();
        olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount())));
        olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olavo.setPayingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
        olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olavo.setCustomerAccountTypeId(3L);

        olavo.setReasonId(-1L);//Special Reason

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olavo);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

        logger.info("[OLAVeriflyFinancialInstitutionImpl.settleAccountToCashCommission] Crediting Commission Recon Mirror A/C. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

        newSwitchWrapper = this.switchController.debit(newSwitchWrapper, null);

        return newSwitchWrapper;
    }

    @Override
    public SwitchWrapper settleBulkPaymentCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
        commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
        commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

        OLAVO olavo = new OLAVO();
        olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount())));
        olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olavo.setPayingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
        olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olavo.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);

        olavo.setRequiresNewTrx(Boolean.TRUE);

        olavo.setReasonId(-1L);//Special Reason

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olavo);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

        logger.info("[OLAVeriflyFinancialInstitutionImpl.settleBulkPaymentCommission] Crediting Commission Recon Mirror A/C. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

        newSwitchWrapper = this.switchController.debit(newSwitchWrapper, null);

        return newSwitchWrapper;
    }

    @Override
    public SwitchWrapper settleCashToCashCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
//		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)switchWrapper.getBasePersistableModel();
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();

        //get the pool account

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
        commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
        commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

        OLAVO olavo = new OLAVO();
        olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - (switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount()))));
        olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olavo.setPayingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
        olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olavo.setCustomerAccountTypeId(3L);

        olavo.setReasonId(-1L);//Special Reason

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olavo);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

        logger.info("[OLAVeriflyFinancialInstitutionImpl.settleCashToCashCommission] Crediting Commission Recon Mirror A/C. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " Agent AppUserID: " + switchWrapper.getWorkFlowWrapper().getAppUserModel().getAppUserId());

        newSwitchWrapper = this.switchController.debit(newSwitchWrapper, null);

        return newSwitchWrapper;
    }


    @Override
    public SwitchWrapper settleCustomerRetailPaymentCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();

        //get the pool account
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
        commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
        commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

        OLAVO olavo = new OLAVO();

//		balance = total charges - (Agent2 Commission + Franchise2 Commission)
        olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - (switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount()))));
        olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olavo.setPayingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
        olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olavo.setCustomerAccountTypeId(3L);

        olavo.setReasonId(-1L);//Special Reason

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olavo);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

        logger.info("[OLAVeriflyFinancialInstitutionImpl.settleAccountToCashCommission] Crediting Commission Recon Mirror A/C. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

        newSwitchWrapper = this.switchController.debit(newSwitchWrapper, null);

        return newSwitchWrapper;
    }

    @Override
    public SwitchWrapper settleDonationTransactionCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {


        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();

        //Get The Pool Account
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel commissionReconPoolBankInfoModel = new StakeholderBankInfoModel();
        commissionReconPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_MIRROR_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(commissionReconPoolBankInfoModel);
        commissionReconPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

        OLAVO olavo = new OLAVO();
        olavo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTotalCommissionAmount())));
        olavo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olavo.setPayingAccNo(commissionReconPoolBankInfoModel.getAccountNo());
        olavo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olavo.setCustomerAccountTypeId(3L);
        olavo.setReasonId(-1L);//Special Reason

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olavo);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

        logger.info("[OLAVeriflyFinancialInstitutionImpl.settleDonationTransactionCommission] Crediting Commission Recon Mirror A/C. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

        newSwitchWrapper = this.switchController.debit(newSwitchWrapper, null);

        return newSwitchWrapper;
    }

    /*check walkiin customer throughput limits*/
    public SwitchWrapper verifyWalkinCustomerThroughputLimits(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();

        OLAVO olavo = switchWrapper.getOlavo();
        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(BankConstantsInterface.OLA_BANK_ID);
        newSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        newSwitchWrapper.setOlavo(olavo);
        newSwitchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());
        newSwitchWrapper.putObject("EXCLUDE_INPROCESS_TX", switchWrapper.getObject("EXCLUDE_INPROCESS_TX"));

        newSwitchWrapper = this.switchController.verifyWalkinCustomerThroughputLimits(newSwitchWrapper);

        return newSwitchWrapper;

    }

    @Override
    public SwitchWrapper rollbackWalkinCustomer(SwitchWrapper switchWrapper) throws FrameworkCheckedException {

        if (switchWrapper != null && switchWrapper.getOlavo() != null) {
            switchWrapper = this.switchController.rollbackWalkinCustomer(switchWrapper);
        }

        return switchWrapper;
    }


    @Override
    public SwitchWrapper updateLedger(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

        if (switchWrapper != null && switchWrapper.getOlavo() != null) {
            switchWrapper = this.switchController.updateLedger(switchWrapper);
        }

        return switchWrapper;
    }

    private AccountInfoModel getThreadLocalAccountInfoModel(SwitchWrapper switchWrapper) {
        AccountInfoModel accountInfoModel = null;
		/*StringBuffer ids = new StringBuffer()
		.append(" [ TransactionID: " + (switchWrapper.getWorkFlowWrapper().getTransactionModel() == null ? " is Null " : switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionId())+ " ")
		.append(" TransactionCode: " + (switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() == null ? " is Null " : switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode())+ " ")
		.append(" ProductID: " + (switchWrapper.getWorkFlowWrapper().getProductModel() == null ? " is Null " : switchWrapper.getWorkFlowWrapper().getProductModel().getProductId()) + " ")
		.append(" AppUserID: " + (ThreadLocalAppUser.getAppUserModel().getAppUserId()) + " ] ");
		StringBuffer debugLogStr = new StringBuffer()
			.append("[OLAVeriflyFinancialInstitution] Looking up for ThreadLocalAccountInfoModel. ")
			.append(ids.toString());

		System.out.println( debugLogStr.toString() );

		if(switchWrapper.getWorkFlowWrapper().isAccountToCashLeg1()){

			accountInfoModel = ThreadLocalAccountInfo.getLoggedInCustomerAccountInfo();

		}else if(switchWrapper.getWorkFlowWrapper().isCashToCashLeg1()){

			accountInfoModel = ThreadLocalAccountInfo.getLoggedInAgentAccountInfo();

		}else if(switchWrapper.getWorkFlowWrapper().isCashDeposit()){

			accountInfoModel = ThreadLocalAccountInfo.getCustomerAccountInfoModel();

		}else if(switchWrapper.getWorkFlowWrapper().getProductModel() != null && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50006L){

			accountInfoModel = ThreadLocalAccountInfo.getCustomerAccountInfoModel();

		}else if(switchWrapper.getWorkFlowWrapper().getProductModel() != null && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50000L){

			if(switchWrapper.getWorkFlowWrapper().isP2PRecepient()){//Recepient BB Customer in Account To Account (P2P) transaction

				accountInfoModel = ThreadLocalAccountInfo.getCustomerAccountInfoModel();

			}else{//Sender BB Customer in Account To Account (P2P) transaction

				accountInfoModel = ThreadLocalAccountInfo.getLoggedInCustomerAccountInfo();

			}

		}else if(switchWrapper.getWorkFlowWrapper().getProductModel() != null && UtilityCompanyEnum.contains(String.valueOf(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId()))){

			if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

				accountInfoModel = ThreadLocalAccountInfo.getLoggedInCustomerAccountInfo();

			}else{//Agent bill payment

				accountInfoModel = ThreadLocalAccountInfo.getLoggedInAgentAccountInfo();

			}

		}

		//Logging AccountInfoModel
		if (accountInfoModel == null) {

			System.out.println("[OLAVeriflyFinancialInstitution] ThreadLocalAccountInfoModel Not found. Going to load from Verifly Module. " + ids.toString() + " ");
		}else{
			StringBuffer accountInfoModelIds = new StringBuffer()
					.append(" [ AccountInfoModel.customerId" + accountInfoModel.getCustomerId() + " ")
					.append(" Account No: " + accountInfoModel.getAccountNo() + " ")
					.append(" Nick: " + accountInfoModel.getAccountNick() + " ] ");

			System.out.println("[OLAVeriflyFinancialInstitution] Found ThreadLocalAccountInfoModel " + accountInfoModelIds.toString() + " " + ids.toString() + " ");
		}*/

        return accountInfoModel;

    }

    private void setThreadLocalAccountInfoModel(SwitchWrapper switchWrapper, AccountInfoModel accountInfoModel) {
		/*StringBuffer accountInfoModelIds = new StringBuffer()
		.append(" [ AccountInfoModel.customerId" + accountInfoModel.getCustomerId() + " ")
		.append(" Account No: " + accountInfoModel.getAccountNo() + " ")
		.append(" Nick: " + accountInfoModel.getAccountNick() + " ] ");

		StringBuffer userAndTrxids = new StringBuffer()
		.append(" [ TransactionID: " + (switchWrapper.getWorkFlowWrapper().getTransactionModel() == null ? " is Null " : switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionId())+ " ")
		.append(" TransactionCode: " + (switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() == null ? " is Null " : switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode())+ " ")
		.append(" ProductID: " + (switchWrapper.getWorkFlowWrapper().getProductModel() == null ? " is Null " : switchWrapper.getWorkFlowWrapper().getProductModel().getProductId()) + " ")
		.append(" AppUserID: " + (ThreadLocalAppUser.getAppUserModel().getAppUserId()) + " ] ");

		System.out.println("[OLAVeriflyFinancialInstitution] Now Putting AccountInfoModel in ThreadLocal. " + accountInfoModelIds.toString() + " " + userAndTrxids.toString());

		if(switchWrapper.getWorkFlowWrapper().isAccountToCashLeg1()){

			ThreadLocalAccountInfo.setLoggedInCustomerAccountInfo(accountInfoModel);

		}else if(switchWrapper.getWorkFlowWrapper().isCashToCashLeg1()){

			ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);

		} else if(switchWrapper.getWorkFlowWrapper().isCashDeposit()){

			ThreadLocalAccountInfo.setCustomerAccountInfoModel(accountInfoModel);

		} else if(switchWrapper.getWorkFlowWrapper().getProductModel() != null && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50006L){

			ThreadLocalAccountInfo.setCustomerAccountInfoModel(accountInfoModel);

		} else if(switchWrapper.getWorkFlowWrapper().getProductModel() != null && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50000L){

			if(switchWrapper.getWorkFlowWrapper().isP2PRecepient()){//Recepient BB Customer in Account To Account (P2P) transaction

				ThreadLocalAccountInfo.setCustomerAccountInfoModel(accountInfoModel);

			}else{//Sender BB Customer in Account To Account (P2P) transaction

				ThreadLocalAccountInfo.setLoggedInCustomerAccountInfo(accountInfoModel);

			}

		}else if(switchWrapper.getWorkFlowWrapper().getProductModel() != null && UtilityCompanyEnum.contains(String.valueOf(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId()))){

			if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {

				ThreadLocalAccountInfo.setLoggedInCustomerAccountInfo(accountInfoModel);

			}else{//Agent bill payment

				ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);

			}

		}*/

    }


    //********************************************************************

    //For Consumer App Bill Payments
    public SwitchWrapper customerBillPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();

        Long customerAccountTypeId = switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId();
        SmartMoneyAccountModel customerSMAModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();
        AccountInfoModel customerAccountInfoModel = getAccountInfoModelBySmartMoneyAccount(customerSMAModel, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        switchWrapper.setAccountInfoModel(customerAccountInfoModel);

        CommissionAmountsHolder commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();

        Boolean isInclusiveChargesIncluded = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : isInclusiveChargesIncluded;
        String isAirTimeTopUp = (String) switchWrapper.getWorkFlowWrapper().getObject("IS_AIR_TIME_TOP_UP");
        String payingAccountNumber = null;
        //Dr Customer
        OLAInfo customerOLAInfo = new OLAInfo();
        customerOLAInfo.setReasonId(ReasonConstants.BILL_PAYMENT);
        customerOLAInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
        customerOLAInfo.setCustomerAccountTypeId(customerAccountTypeId);
        customerOLAInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        if (isAirTimeTopUp != null && isAirTimeTopUp.equals("1"))
            payingAccountNumber = "10121934709";//Air Time Top Up Settlement Account
        else
            payingAccountNumber = customerAccountInfoModel.getAccountNo();
        customerOLAInfo.setPayingAccNo(payingAccountNumber);
        customerOLAInfo.setIsAgent(Boolean.FALSE);
        customerOLAInfo.setBalance(commissionAmountsHolder.getTotalAmount());
        customerOLAInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);

        debitList.add(customerOLAInfo);

        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(commissionAmountsHolder.getInclusiveFixAmount())
                + CommonUtils.getDoubleOrDefaultValue(commissionAmountsHolder.getInclusivePercentAmount());

        //Dr 3rd Party Inclusive Account
        if (isInclusiveChargesIncluded) {

            OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.BILL_PAYMENT,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            if (olaInfo.getCoreStakeholderBankInfoId() != null && olaInfo.getCoreStakeholderBankInfoId() > 0L && !StringUtil.isNullOrEmpty(olaInfo.getPayingAccNo())) {

                debitList.add(olaInfo);
            }
        }

        Double utilityBillPoolCreditAmount = commissionAmountsHolder.getTransactionAmount();

        if (!isInclusiveChargesIncluded && totalInclusiveCharges != null) {
            utilityBillPoolCreditAmount -= totalInclusiveCharges;
        }

        utilityBillPoolCreditAmount = (utilityBillPoolCreditAmount == null ? 0.0D : utilityBillPoolCreditAmount);
        Long utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_BILL_POOL_ACCOUNT_ID;

        if (workFlowWrapper.getProductModel().getProductId().equals(10245384L))
            utilityBillPoolAccountId = PoolAccountConstantsInterface.OFFLINE_BILLER_BLB_POOL_ACCOUNT_ID;
        if (workFlowWrapper.getProductModel().getProductId().equals(10245397L))
            utilityBillPoolAccountId = PoolAccountConstantsInterface.OFFLINE_BILLER_ASKARI_BLB_POOL_ACCOUNT_ID;

        if (OneBillProductEnum.contains(workFlowWrapper.getProductModel().getProductId().toString()))
            utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_ONE_BILL_BLB_POOL_ACCOUNT_ID;
        if (BookMeProductEnum.contains((workFlowWrapper.getProductModel().getProductId().toString())))
            utilityBillPoolAccountId = PoolAccountConstantsInterface.BOOK_ME_BLB;
        //Cr Utility Bill Pool
        OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.BILL_PAYMENT,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                utilityBillPoolAccountId,
                null,
                utilityBillPoolCreditAmount
        );

        creditList.add(olaInfo);


        olaVO.setFromSegmentId(switchWrapper.getWorkFlowWrapper().getFromSegmentId());
        olaVO.setToSegmentId(switchWrapper.getWorkFlowWrapper().getToSegmentId());
        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());

        switchWrapper.setOlavo(olaVO);
        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
        switchWrapper.setBankId(customerSMAModel.getBankId());
        switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
        switchWrapper.setTransactionAmount(utilityBillPoolCreditAmount);
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());

        switchWrapper.getWorkFlowWrapper().setTransactionModel(switchWrapper.getTransactionTransactionModel());

        this.prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.BILL_PAYMENT);

        olaVO.setCreditAccountList(creditList);
        olaVO.setDebitAccountList(debitList);

        if (switchWrapper.getWorkFlowWrapper() != null &&
                switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null
                && StringUtil.isNullOrEmpty(switchWrapper.getOlavo().getMicrobankTransactionCode())) {

            switchWrapper.getOlavo().setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        }

        this.switchController.debitCreditAccount(switchWrapper);

        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setSenderAgentAccountNo("");

        return switchWrapper;
    }


    public SwitchWrapper debitPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();

        Long customerAccountTypeId = switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId();
        SmartMoneyAccountModel customerSMAModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();
        AccountInfoModel customerAccountInfoModel = getAccountInfoModelBySmartMoneyAccount(customerSMAModel, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        switchWrapper.setAccountInfoModel(customerAccountInfoModel);

        CommissionAmountsHolder commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();

        Boolean isInclusiveChargesIncluded = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : isInclusiveChargesIncluded;

        String payingAccountNumber = null;
        //Dr Customer
        OLAInfo customerOLAInfo = new OLAInfo();
        customerOLAInfo.setReasonId(ReasonConstants.DEBIT_PAYMENT_API);
        customerOLAInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
        customerOLAInfo.setCustomerAccountTypeId(customerAccountTypeId);
        customerOLAInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        payingAccountNumber = customerAccountInfoModel.getAccountNo();
        customerOLAInfo.setPayingAccNo(payingAccountNumber);
        customerOLAInfo.setIsAgent(Boolean.FALSE);
        customerOLAInfo.setBalance(commissionAmountsHolder.getTotalAmount());
        customerOLAInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);

        debitList.add(customerOLAInfo);

        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(commissionAmountsHolder.getInclusiveFixAmount())
                + CommonUtils.getDoubleOrDefaultValue(commissionAmountsHolder.getInclusivePercentAmount());

        //Dr 3rd Party Inclusive Account
        if (isInclusiveChargesIncluded) {

            OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.DEBIT_PAYMENT_API,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            if (olaInfo.getCoreStakeholderBankInfoId() != null && olaInfo.getCoreStakeholderBankInfoId() > 0L && !StringUtil.isNullOrEmpty(olaInfo.getPayingAccNo())) {

                debitList.add(olaInfo);
            }
        }

        Double utilityBillPoolCreditAmount = commissionAmountsHolder.getTransactionAmount();

        if (!isInclusiveChargesIncluded && totalInclusiveCharges != null) {
            utilityBillPoolCreditAmount -= totalInclusiveCharges;
        }
        Long utilityBillPoolAccountId = null;

        String names = MessageUtil.getMessage("product.ids");
        List<String> items = Arrays.asList(names.split("\\s*,\\s*"));
        String cardProductsNames = MessageUtil.getMessage("cardPayment.product.ids");
        List<String> cardProductItems = Arrays.asList(cardProductsNames.split("\\s*,\\s*"));
        StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
        if (items.contains(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().toString())) {
            utilityBillPoolAccountId = PoolAccountConstantsInterface.T24_SETTLEMENT_ACCOUNT_ID;
            if (stakeholderBankInfoModel != null) {
                workFlowWrapper.setRecipientAccountNo(stakeholderBankInfoModel.getAccountNo());
            }
        } else if (cardProductItems.contains(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().toString())) {
            utilityBillPoolAccountId = PoolAccountConstantsInterface.CARD_PAYMENT_OLA_ACCOUNT_ID;
            if (stakeholderBankInfoModel != null) {
                workFlowWrapper.setRecipientAccountNo(stakeholderBankInfoModel.getAccountNo());
            }
        } else {
            utilityBillPoolCreditAmount = (utilityBillPoolCreditAmount == null ? 0.0D : utilityBillPoolCreditAmount);
            List<StakeholderBankInfoModel> stakeholderBankInfoModelList = this.stakeholderBankInfoManager.getStakeholderBankInfoForProductandAccountType(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(), "BLB");
            if (stakeholderBankInfoModelList != null) {
                utilityBillPoolAccountId = Long.valueOf(stakeholderBankInfoModelList.get(0).getStakeholderBankInfoId());
                workFlowWrapper.setRecipientAccountNo(((StakeholderBankInfoModel) stakeholderBankInfoModelList.get(0)).getAccountNo());

            }
        }
//        Long utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_BILL_POOL_ACCOUNT_ID;
//        if (OneBillProductEnum.contains(workFlowWrapper.getProductModel().getProductId().toString()))
//            utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_ONE_BILL_BLB_POOL_ACCOUNT_ID;
//        if (BookMeProductEnum.contains((workFlowWrapper.getProductModel().getProductId().toString())))
//            utilityBillPoolAccountId = PoolAccountConstantsInterface.BOOK_ME_BLB;
        //Cr Utility Bill Pool
        OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.DEBIT_PAYMENT_API,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                utilityBillPoolAccountId,
                null,
                utilityBillPoolCreditAmount
        );

        creditList.add(olaInfo);


        olaVO.setFromSegmentId(switchWrapper.getWorkFlowWrapper().getFromSegmentId());
        olaVO.setToSegmentId(switchWrapper.getWorkFlowWrapper().getToSegmentId());
        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());

        switchWrapper.setOlavo(olaVO);
        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
        switchWrapper.setBankId(customerSMAModel.getBankId());
        switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
        switchWrapper.setTransactionAmount(utilityBillPoolCreditAmount);
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
        switchWrapper.setFromAccountNo(payingAccountNumber);
        switchWrapper.getWorkFlowWrapper().setTransactionModel(switchWrapper.getTransactionTransactionModel());

        this.prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.DEBIT_PAYMENT_API);

        olaVO.setCreditAccountList(creditList);
        olaVO.setDebitAccountList(debitList);

        if (switchWrapper.getWorkFlowWrapper() != null &&
                switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null
                && StringUtil.isNullOrEmpty(switchWrapper.getOlavo().getMicrobankTransactionCode())) {

            switchWrapper.getOlavo().setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        }

        this.switchController.debitCreditAccount(switchWrapper);

        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setSenderAgentAccountNo("");

        return switchWrapper;
    }


    public SwitchWrapper feePayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();

        Long customerAccountTypeId = switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId();
        SmartMoneyAccountModel customerSMAModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();
        AccountInfoModel customerAccountInfoModel = getAccountInfoModelBySmartMoneyAccount(customerSMAModel, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        switchWrapper.setAccountInfoModel(customerAccountInfoModel);

        CommissionAmountsHolder commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();

        Boolean isInclusiveChargesIncluded = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : isInclusiveChargesIncluded;
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
        Double totalExclCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount());
        Double fed = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().
                getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID));
        String payingAccountNumber = null;

        //Dr Customer
        OLAInfo customerOLAInfo = new OLAInfo();
        if (totalInclusiveCharges == 0.0) {
            customerOLAInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + fed);
        } else if (totalInclusiveCharges != 0.0) {
            customerOLAInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + totalExclCharges);
        } else {
            customerOLAInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + totalExclCharges);
        }
        customerOLAInfo.setReasonId(ReasonConstants.FEE_PAYMENT);
        customerOLAInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
        customerOLAInfo.setCustomerAccountTypeId(customerAccountTypeId);
        customerOLAInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        payingAccountNumber = customerAccountInfoModel.getAccountNo();
        customerOLAInfo.setPayingAccNo(payingAccountNumber);
        customerOLAInfo.setIsAgent(Boolean.FALSE);

        customerOLAInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);

        debitList.add(customerOLAInfo);


        //Dr 3rd Party Inclusive Account
        if (isInclusiveChargesIncluded) {

            OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.FEE_PAYMENT,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            if (olaInfo.getCoreStakeholderBankInfoId() != null && olaInfo.getCoreStakeholderBankInfoId() > 0L && !StringUtil.isNullOrEmpty(olaInfo.getPayingAccNo())) {

                debitList.add(olaInfo);
            }
        }

        Double utilityBillPoolCreditAmount = commissionAmountsHolder.getTransactionAmount();

        if (!isInclusiveChargesIncluded && totalInclusiveCharges != null) {
            utilityBillPoolCreditAmount -= totalInclusiveCharges;
        }
        Long utilityBillPoolAccountId = null;


//            utilityBillPoolCreditAmount = (utilityBillPoolCreditAmount == null ? 0.0D : utilityBillPoolCreditAmount);
//            List<StakeholderBankInfoModel> stakeholderBankInfoModelList = this.stakeholderBankInfoManager.getStakeholderBankInfoForProductandAccountType(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(), "BLB");
//            if (stakeholderBankInfoModelList != null) {
//                utilityBillPoolAccountId = Long.valueOf(stakeholderBankInfoModelList.get(0).getStakeholderBankInfoId());
//            }
//        OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
//                ReasonConstants.FEE_PAYMENT,
//                TransactionTypeConstantsInterface.OLA_CREDIT,
//                utilityBillPoolAccountId,
//                null,
//                utilityBillPoolCreditAmount
//        );
//
//        creditList.add(olaInfo);


        olaVO.setFromSegmentId(switchWrapper.getWorkFlowWrapper().getFromSegmentId());
        olaVO.setToSegmentId(switchWrapper.getWorkFlowWrapper().getToSegmentId());
        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());

        switchWrapper.setOlavo(olaVO);
        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
        switchWrapper.setBankId(customerSMAModel.getBankId());
        switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
        switchWrapper.setTransactionAmount(utilityBillPoolCreditAmount);
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());

        switchWrapper.getWorkFlowWrapper().setTransactionModel(switchWrapper.getTransactionTransactionModel());
        this.prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.FEE_PAYMENT);

        olaVO.setCreditAccountList(creditList);
        olaVO.setDebitAccountList(debitList);

        if (switchWrapper.getWorkFlowWrapper() != null &&
                switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null
                && StringUtil.isNullOrEmpty(switchWrapper.getOlavo().getMicrobankTransactionCode())) {

            switchWrapper.getOlavo().setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        }

        this.switchController.debitCreditAccount(switchWrapper);

        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setSenderAgentAccountNo("");

        return switchWrapper;
    }


    public SwitchWrapper advanceLoanPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();

        Long customerAccountTypeId = switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId();
        SmartMoneyAccountModel customerSMAModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();
        AccountInfoModel customerAccountInfoModel = getAccountInfoModelBySmartMoneyAccount(customerSMAModel, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        switchWrapper.setAccountInfoModel(customerAccountInfoModel);

        CommissionAmountsHolder commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();

        Boolean isInclusiveChargesIncluded = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : isInclusiveChargesIncluded;

        String payingAccountNumber = null;
        //Dr Customer
        OLAInfo customerOLAInfo = new OLAInfo();
        customerOLAInfo.setReasonId(ReasonConstants.LOAN_PAYMENT);
        customerOLAInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
        customerOLAInfo.setCustomerAccountTypeId(customerAccountTypeId);
        customerOLAInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        payingAccountNumber = customerAccountInfoModel.getAccountNo();
        customerOLAInfo.setPayingAccNo(payingAccountNumber);
        customerOLAInfo.setIsAgent(Boolean.FALSE);
        customerOLAInfo.setBalance(commissionAmountsHolder.getTotalAmount());
        customerOLAInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);

        debitList.add(customerOLAInfo);

        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(commissionAmountsHolder.getInclusiveFixAmount())
                + CommonUtils.getDoubleOrDefaultValue(commissionAmountsHolder.getInclusivePercentAmount());

        //Dr 3rd Party Inclusive Account
        if (isInclusiveChargesIncluded) {

            OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.LOAN_PAYMENT,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            if (olaInfo.getCoreStakeholderBankInfoId() != null && olaInfo.getCoreStakeholderBankInfoId() > 0L && !StringUtil.isNullOrEmpty(olaInfo.getPayingAccNo())) {

                debitList.add(olaInfo);
            }
        }

        Double utilityBillPoolCreditAmount = commissionAmountsHolder.getTransactionAmount();

        if (!isInclusiveChargesIncluded && totalInclusiveCharges != null) {
            utilityBillPoolCreditAmount -= totalInclusiveCharges;
        }
        Long utilityBillPoolAccountId = null;


        utilityBillPoolCreditAmount = (utilityBillPoolCreditAmount == null ? 0.0D : utilityBillPoolCreditAmount);
        List<StakeholderBankInfoModel> stakeholderBankInfoModelList = this.stakeholderBankInfoManager.getStakeholderBankInfoForProductandAccountType(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(), "BLB");
        if (stakeholderBankInfoModelList != null) {
            utilityBillPoolAccountId = Long.valueOf(stakeholderBankInfoModelList.get(0).getStakeholderBankInfoId());
        }


        Long utilityBillPoolAccountId2 = null;
        List<StakeholderBankInfoModel> stakeholderBankInfoModelList2 = this.stakeholderBankInfoManager.getStakeholderBankInfoForProductandAccountType(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(), "OF_SET");

        if (stakeholderBankInfoModelList2 != null) {
            utilityBillPoolAccountId2 = Long.valueOf(stakeholderBankInfoModelList2.get(0).getStakeholderBankInfoId());
        }

//        Long utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_BILL_POOL_ACCOUNT_ID;
//        if (OneBillProductEnum.contains(workFlowWrapper.getProductModel().getProductId().toString()))
//            utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_ONE_BILL_BLB_POOL_ACCOUNT_ID;
//        if (BookMeProductEnum.contains((workFlowWrapper.getProductModel().getProductId().toString())))
//            utilityBillPoolAccountId = PoolAccountConstantsInterface.BOOK_ME_BLB;
        //Cr Utility Bill Pool
        OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.LOAN_PAYMENT,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                utilityBillPoolAccountId,
                utilityBillPoolAccountId2,
                utilityBillPoolCreditAmount
        );

        creditList.add(olaInfo);


        olaVO.setFromSegmentId(switchWrapper.getWorkFlowWrapper().getFromSegmentId());
        olaVO.setToSegmentId(switchWrapper.getWorkFlowWrapper().getToSegmentId());
        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());

        switchWrapper.setOlavo(olaVO);
        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
        switchWrapper.setBankId(customerSMAModel.getBankId());
        switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
        switchWrapper.setTransactionAmount(utilityBillPoolCreditAmount);
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());

        switchWrapper.getWorkFlowWrapper().setTransactionModel(switchWrapper.getTransactionTransactionModel());

        this.prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.LOAN_PAYMENT);

        olaVO.setCreditAccountList(creditList);
        olaVO.setDebitAccountList(debitList);

        if (switchWrapper.getWorkFlowWrapper() != null &&
                switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null
                && StringUtil.isNullOrEmpty(switchWrapper.getOlavo().getMicrobankTransactionCode())) {

            switchWrapper.getOlavo().setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        }

        this.switchController.debitCreditAccount(switchWrapper);

        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setSenderAgentAccountNo("");
        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setBankAccountNo(payingAccountNumber);
        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setRecipientAccountNo(stakeholderBankInfoModelList.get(0).getAccountNo());

        return switchWrapper;
    }


    public SwitchWrapper creditPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {
        logger.info("start of OLAVeriflyFinancialInstitutionImpl.creditPayment() :: " + new Date());
        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();


        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();

        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Dr Inclusive Charges Sundry A/C
            OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.WEB_SERVICE_PAYMENT,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            debitList.add(olaInfo);

            totalInclusiveCharges = 0.0;

        }

        //Debit Customer
        SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();

        AccountInfoModel customerAccountInfo = null;


        customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        OLAInfo customerOlaInfo = new OLAInfo();

        customerOlaInfo.setIsAgent(false);
//        if (customerSMA.getPaymentModeId() == 7l)
//            customerOlaInfo.setCustomerAccountTypeId(4l);
//        else
        customerOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());


        customerOlaInfo.setBalanceAfterTrxRequired(true);
        customerOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        customerOlaInfo.setReasonId(ReasonConstants.WEB_SERVICE_PAYMENT);
        customerOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        customerOlaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
        customerOlaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges);
        creditList.add(customerOlaInfo);

        Long utilityBillPoolAccountId = null;
        List<StakeholderBankInfoModel> stakeholderBankInfoModelList = this.stakeholderBankInfoManager.getStakeholderBankInfoForProductandAccountType(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(), "BLB");


        if (stakeholderBankInfoModelList != null) {
            utilityBillPoolAccountId = Long.valueOf(stakeholderBankInfoModelList.get(0).getStakeholderBankInfoId());
        }

        Long utilityBillPoolAccountId2 = null;
        List<StakeholderBankInfoModel> stakeholderBankInfoModelList2 = this.stakeholderBankInfoManager.getStakeholderBankInfoForProductandAccountType(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(), "OF_SET");

        if (stakeholderBankInfoModelList2 != null) {
            utilityBillPoolAccountId2 = Long.valueOf(stakeholderBankInfoModelList2.get(0).getStakeholderBankInfoId());
        }
        //Cr Fonepay settlement A/C
        OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.WEB_SERVICE_PAYMENT,
                // These constant will be changed to a new Fonepay account
                TransactionTypeConstantsInterface.OLA_DEBIT,
                utilityBillPoolAccountId,
                utilityBillPoolAccountId2,
                switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount()
        );


        debitList.add(olaInfo);


        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        switchWrapper.setFromAccountNo(olaInfo.getPayingAccNo());
        switchWrapper.setToAccountNo(customerAccountInfo.getAccountNo());


        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
        switchWrapper.setBankId(customerSMA.getBankId());
        switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
        switchWrapper.setTransactionAmount(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount());
        prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.WEB_SERVICE_PAYMENT);
        olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
        olaVO.setReceivingAccNo(customerAccountInfo.getAccountNo());// to be used in posted trx
        olaVO.setFromSegmentId(switchWrapper.getWorkFlowWrapper().getFromSegmentId());
        olaVO.setToSegmentId(switchWrapper.getWorkFlowWrapper().getToSegmentId());
        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);

        if (switchWrapper.getWorkFlowWrapper() != null &&
                switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null
                && StringUtil.isNullOrEmpty(switchWrapper.getOlavo().getMicrobankTransactionCode())) {

            switchWrapper.getOlavo().setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        }

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        // setting OlaSwitchWrapper to be used in TransactionProcessor for Day End settlement process
        switchWrapper.getWorkFlowWrapper().setOLASwitchWrapper(switchWrapper);
        return switchWrapper;
    }


    public SwitchWrapper customercollectionPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();

        Long customerAccountTypeId = switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId();
        SmartMoneyAccountModel customerSMAModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();
        AccountInfoModel customerAccountInfoModel = getAccountInfoModelBySmartMoneyAccount(customerSMAModel, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        switchWrapper.setAccountInfoModel(customerAccountInfoModel);

        CommissionAmountsHolder commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();

        Boolean isInclusiveChargesIncluded = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : isInclusiveChargesIncluded;
        Long olaAccount = null;
        Long coreAccount = null;
        Long reasonId = null;
        Long productId = switchWrapper.getWorkFlowWrapper().getProductModel().getProductId();
        if (productId.equals(ProductConstantsInterface.CUSTOMER_ET_COLLECTION)) {
            reasonId = ReasonConstants.ET_COLLECTION;
            olaAccount = PoolAccountConstantsInterface.CUSTOMER_ET_COLLECTION_POOL_ACCOUNT;
            coreAccount = PoolAccountConstantsInterface.CUSTOMER_ET_COLLECTION_ACCOUNT_GL;
        } else if (productId.equals(ProductConstantsInterface.CUSTOMER_KP_CHALLAN_COLLECTION)) {
            reasonId = ReasonConstants.KP_CHALLAN_COLLECTION;
            olaAccount = PoolAccountConstantsInterface.CUSTOMER_KP_COLLECTION_POOL_ACCOUNT;
            coreAccount = PoolAccountConstantsInterface.CUSTOMER_KP_COLLECTION_ACCOUNT_GL;
        } else if (productId.equals(ProductConstantsInterface.LICENSE_FEE_COLLECTION)) {
            reasonId = ReasonConstants.LICENSE_FEE_COLLECTION;
            olaAccount = PoolAccountConstantsInterface.LICENSE_FEE_COLLECTION_POOL_ACCOUNT;
            coreAccount = PoolAccountConstantsInterface.LICENSE_FEE_COLLECTION_ACCOUNT_GL;
        } else if (productId.equals(ProductConstantsInterface.CUST_SARHAD_UNIVERSITY_COLLECTION)) {
            reasonId = ReasonConstants.COLLECTION_PAYMENT;
            olaAccount = PoolAccountConstantsInterface.SARHAD_UNIVERSITY_COLLECTION_POOL_ACCOUNT;
            coreAccount = PoolAccountConstantsInterface.SARHAD_UNIVERSITY_COLLECTION_ACCOUNT_GL;
        } else if (productId.equals(ProductConstantsInterface.CUSTOMER_VALLENCIA_COLLECTION)) {
            reasonId = ReasonConstants.VALENCIA_COLLECTION_PAYMENT;
            olaAccount = PoolAccountConstantsInterface.COLLECTION_POOL_ACCOUNT_OLA;
            coreAccount = PoolAccountConstantsInterface.COLLECTION_POOL_ACCOUNT_CORE;
        }
        else {
            reasonId = ReasonConstants.COLLECTION_PAYMENT;
            olaAccount = PoolAccountConstantsInterface.COLLECTION_POOL_ACCOUNT_OLA;
            coreAccount = PoolAccountConstantsInterface.COLLECTION_POOL_ACCOUNT_CORE;
        }


        //Dr Customer
        OLAInfo customerOLAInfo = new OLAInfo();
        customerOLAInfo.setReasonId(reasonId);
        customerOLAInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
        customerOLAInfo.setCustomerAccountTypeId(customerAccountTypeId);
        customerOLAInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        customerOLAInfo.setPayingAccNo(customerAccountInfoModel.getAccountNo());
        customerOLAInfo.setIsAgent(Boolean.FALSE);
        customerOLAInfo.setBalance(commissionAmountsHolder.getTotalAmount());
        customerOLAInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);

        debitList.add(customerOLAInfo);

        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(commissionAmountsHolder.getInclusiveFixAmount())
                + CommonUtils.getDoubleOrDefaultValue(commissionAmountsHolder.getInclusivePercentAmount());

        //Dr 3rd Party Inclusive Account
        if (isInclusiveChargesIncluded && totalInclusiveCharges > 0) {

            OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    reasonId,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            if (olaInfo.getCoreStakeholderBankInfoId() != null && olaInfo.getCoreStakeholderBankInfoId() > 0L && !StringUtil.isNullOrEmpty(olaInfo.getPayingAccNo())) {
                debitList.add(olaInfo);
            }
        }

        Double collectionPoolCreditAmount = commissionAmountsHolder.getTransactionAmount();

        if (!isInclusiveChargesIncluded && totalInclusiveCharges != null) {
            collectionPoolCreditAmount -= totalInclusiveCharges;
        }

        collectionPoolCreditAmount = (collectionPoolCreditAmount == null ? 0.0D : collectionPoolCreditAmount);

        //Cr Collection Pool
        OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                reasonId,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                olaAccount,
                coreAccount,
                collectionPoolCreditAmount
        );

        creditList.add(olaInfo);


        olaVO.setFromSegmentId(switchWrapper.getWorkFlowWrapper().getFromSegmentId());
        olaVO.setToSegmentId(switchWrapper.getWorkFlowWrapper().getToSegmentId());
        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());

        switchWrapper.setOlavo(olaVO);
        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
        switchWrapper.setBankId(customerSMAModel.getBankId());
//        switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
        switchWrapper.setTransactionAmount(collectionPoolCreditAmount);
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());

        switchWrapper.getWorkFlowWrapper().setTransactionModel(switchWrapper.getTransactionTransactionModel());

        this.prepareCommissionCreditList(switchWrapper, creditList, reasonId);

        olaVO.setCreditAccountList(creditList);
        olaVO.setDebitAccountList(debitList);

        if (switchWrapper.getWorkFlowWrapper() != null &&
                switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null
                && StringUtil.isNullOrEmpty(switchWrapper.getOlavo().getMicrobankTransactionCode())) {

            switchWrapper.getOlavo().setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        }

        this.switchController.debitCreditAccount(switchWrapper);

        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setSenderAgentAccountNo("");

        return switchWrapper;
    }


    //********************************************************************
    /* (non-Javadoc)
     * @see com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution#billPayment(com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper)
     */
    public SwitchWrapper billPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();

        switchWrapper.setAccountInfoModel(workFlowWrapper.getAccountInfoModel());

        Long customerAccountTypeId = null;
        Long retailerAccountTypeId = null;

        if (switchWrapper.getWorkFlowWrapper().getCustomerModel() != null) {
            customerAccountTypeId = switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId();
        }

        if (switchWrapper.getWorkFlowWrapper().getRetailerContactModel() != null) {
            retailerAccountTypeId = switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId();
        }

        Boolean isPayByCustomerAccount = ((Integer) workFlowWrapper.getCustomField()).intValue() == 0;
        logger.info("Is Pay By Customer Account " + isPayByCustomerAccount);
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();
        Long utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_BILL_POOL_ACCOUNT_ID;
        if (OneBillProductEnum.contains(workFlowWrapper.getProductModel().getProductId().toString()))
            utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_ONE_BILL_BLB_POOL_ACCOUNT_ID;

        StakeholderBankInfoModel utilityBillPoolAccount =
                (StakeholderBankInfoModel) workFlowWrapper.getDataMap().get(utilityBillPoolAccountId.toString());

        AccountInfoModel customerAccountInfoModel = (AccountInfoModel) workFlowWrapper.getDataMap().get("CUSTOMER_RetailerAccountInfoModel");

        CommissionWrapper commissionWrapper = workFlowWrapper.getCommissionWrapper();
        CommissionAmountsHolder commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();
        Map<Long, Double> stakeholderCommissionsMap = commissionAmountsHolder.getStakeholderCommissionsMap();

        Double agentCommissionAmount = 0.0D;
        Double billAmount = 0.0D;
        Double transactionalAmount = 0.0D;
        Double transactionalCharges = 0.0D;
        Boolean isInclusiveChargesIncluded = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : isInclusiveChargesIncluded;

        if (stakeholderCommissionsMap.get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID) != null) {
            agentCommissionAmount = stakeholderCommissionsMap.get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID);
        }
        if (commissionAmountsHolder.getBillingOrganizationAmount() != null) {
            billAmount = commissionAmountsHolder.getBillingOrganizationAmount();
        }
        if (commissionAmountsHolder.getTotalAmount() != null) {
            transactionalAmount = commissionAmountsHolder.getTransactionAmount();
        }
        if (commissionAmountsHolder.getTotalCommissionAmount() != null) {
            transactionalCharges = commissionAmountsHolder.getTotalCommissionAmount();
        }

        String agentAccountNumber = switchWrapper.getAccountInfoModel().getAccountNo();

        String customerAccountNumber = null;

        if (customerAccountInfoModel != null) {
            customerAccountNumber = customerAccountInfoModel.getAccountNo();
        }

//		@FT
        OLAInfo agentOLAInfo = new OLAInfo();
        agentOLAInfo.setReasonId(ReasonConstants.BILL_PAYMENT);
        agentOLAInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
        agentOLAInfo.setCustomerAccountTypeId(retailerAccountTypeId);
        agentOLAInfo.setPayingAccNo(agentAccountNumber);
        agentOLAInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        agentOLAInfo.setAgentBalanceAfterTrxRequired(Boolean.TRUE);
        agentOLAInfo.setIsAgent(Boolean.TRUE);
        if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
            agentOLAInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
            agentOLAInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
        }

        if (isPayByCustomerAccount) {

//			@FT
            OLAInfo customerOLAInfo = new OLAInfo();
            customerOLAInfo.setReasonId(ReasonConstants.BILL_PAYMENT);
            customerOLAInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
            customerOLAInfo.setCustomerAccountTypeId(customerAccountTypeId);
            customerOLAInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            customerOLAInfo.setPayingAccNo(customerAccountNumber);
            customerOLAInfo.setIsAgent(Boolean.FALSE);
            customerOLAInfo.setBalance(
                    transactionalAmount +
                            commissionAmountsHolder.getExclusiveFixAmount() +
                            commissionAmountsHolder.getExclusivePercentAmount());

            customerOLAInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);

            debitList.add(customerOLAInfo);

            agentOLAInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            agentOLAInfo.setBalance(agentCommissionAmount);

            creditList.add(agentOLAInfo);

        } else if (!isPayByCustomerAccount) {// && (transactionalAmount - agentCommissionAmount) > 0) {

            agentOLAInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
            agentOLAInfo.setBalance((transactionalAmount +
                    commissionAmountsHolder.getExclusiveFixAmount() +
                    commissionAmountsHolder.getExclusivePercentAmount()) -
                    agentCommissionAmount);

            debitList.add(agentOLAInfo);
        }


//		@FT
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
        ;

        if (isInclusiveChargesIncluded) {

            OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.BILL_PAYMENT,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            if (olaInfo.getCoreStakeholderBankInfoId() != null && olaInfo.getCoreStakeholderBankInfoId() > 0L && !StringUtil.isNullOrEmpty(olaInfo.getPayingAccNo())) {

                debitList.add(olaInfo);
            }
        }

        Double utilityBillPoolCreditAmount = transactionalAmount;

        if (!isInclusiveChargesIncluded && totalInclusiveCharges != null) {

            utilityBillPoolCreditAmount -= totalInclusiveCharges;
        }

        utilityBillPoolCreditAmount = (utilityBillPoolCreditAmount == null ? 0.0D : utilityBillPoolCreditAmount);


//		@FT
        OLAInfo utilityBillPoolAccountOLAInfo = new OLAInfo();
        utilityBillPoolAccountOLAInfo.setReasonId(ReasonConstants.BILL_PAYMENT);
        utilityBillPoolAccountOLAInfo.setBalanceAfterTrxRequired(Boolean.FALSE);
        utilityBillPoolAccountOLAInfo.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
        utilityBillPoolAccountOLAInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        utilityBillPoolAccountOLAInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        utilityBillPoolAccountOLAInfo.setPayingAccNo(utilityBillPoolAccount.getAccountNo());
        utilityBillPoolAccountOLAInfo.setBalance(utilityBillPoolCreditAmount);

        if (utilityBillPoolAccountOLAInfo.getBalance() != null) {
            creditList.add(utilityBillPoolAccountOLAInfo);
            billAmount = utilityBillPoolAccountOLAInfo.getBalance();
        }

        olaVO.setFromSegmentId(switchWrapper.getWorkFlowWrapper().getFromSegmentId());
        olaVO.setToSegmentId(switchWrapper.getWorkFlowWrapper().getToSegmentId());
        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());

        switchWrapper.setOlavo(olaVO);
        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
        switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT.longValue());
        switchWrapper.setTransactionAmount(billAmount);
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());

        if (switchWrapper.getWorkFlowWrapper() != null) {
            switchWrapper.getWorkFlowWrapper().setTransactionModel(switchWrapper.getTransactionTransactionModel());
        }

        switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        switchWrapper.setWorkFlowWrapper(workFlowWrapper);

        logger.info(commissionAmountsHolder.toString());

//		@FT
        this.prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.BILL_PAYMENT);

        olaVO.setCreditAccountList(creditList);
        olaVO.setDebitAccountList(debitList);

        if (switchWrapper.getWorkFlowWrapper() != null &&
                switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() != null
                && StringUtil.isNullOrEmpty(switchWrapper.getOlavo().getMicrobankTransactionCode())) {

            switchWrapper.getOlavo().setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        }

        this.switchController.debitCreditAccount(switchWrapper);

        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setSenderAgentAccountNo(agentAccountNumber);

        logger.debug("From Balance After Transaction  : " + switchWrapper.getOlavo().getFromBalanceAfterTransaction());

        return switchWrapper;
    }


    private AccountInfoModel loadFranchiseAccountInfo(Long agentAppUserId, Long trxCodeId) throws Exception {
        AccountInfoModel frnAccountInfoModel = null;
        FranchiseAccountDetailViewModel franchiseAccountDetailViewModel = new FranchiseAccountDetailViewModel();
        franchiseAccountDetailViewModel.setAgentAppUserId(agentAppUserId);

        List<FranchiseAccountDetailViewModel> list = this.genericDao.findEntityByExample(franchiseAccountDetailViewModel, null);
        if (list != null && list.size() > 0) {
            franchiseAccountDetailViewModel = list.get(0);

            SmartMoneyAccountModel franchiseSMA = new SmartMoneyAccountModel();
            franchiseSMA.setRetailerContactId(franchiseAccountDetailViewModel.getHeadRetailerContactId());

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(franchiseSMA);
            baseWrapper = this.smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
            franchiseSMA = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();

            frnAccountInfoModel = getAccountInfoModelBySmartMoneyAccount(franchiseSMA, franchiseAccountDetailViewModel.getHeadAppUserId(), trxCodeId);
        }

        return frnAccountInfoModel;
    }

    public void setSmartMoneyAccountManager(
            SmartMoneyAccountManager smartMoneyAccountManager) {

        this.smartMoneyAccountManager = smartMoneyAccountManager;
    }

    public void setGenericDao(GenericDao genericDao) {
        this.genericDao = genericDao;
    }

    public SwitchWrapper settleCustomerPendingTrx(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {
        return this.performLeg2(switchWrapper,
                ReasonConstants.CUSTOMER_PENDING_TRX_SETTLEMENT,
                TransactionConstantsInterface.RECIEVED_IN_ACCOUNT_CATEGORY_ID,
                Boolean.TRUE);
/*		OLAVO olaVO = new OLAVO();
		List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

		long productId = switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().longValue();
		CommissionAmountsHolder amountsHolder = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder();

		//Dr Sundry	A/C
		OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
        		ReasonConstants.CUSTOMER_PENDING_TRX_SETTLEMENT,
        		TransactionTypeConstantsInterface.OLA_DEBIT,
        		PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID,
        		PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID,
				amountsHolder.getTransactionAmount()
					+ amountsHolder.getAgent2CommAmountBeforeHierarchy()
					+ amountsHolder.getAgent2WHTAmount()
					- amountsHolder.getTotalInclusiveAmount()
        		);
        debitList.add(olaInfo);

		SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
		AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
		AccountInfoModel agentAccountInfo = getAccountInfoModelBySmartMoneyAccount(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel(), switchWrapper.getWorkFlowWrapper().getAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
		if(agentAccountInfo == null){
			logger.error("[settleCustomerPendingTrx] Unable to load Agent Account Info...Agent AppUserId:"+switchWrapper.getWorkFlowWrapper().getAppUserModel().getAppUserId());
			throw new WorkFlowException("Unable to load Agent Account Info");
		}

		//Cr Customer A/C
        olaInfo = new OLAInfo();
        olaInfo.setIsAgent(false);
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        olaInfo.setReasonId(ReasonConstants.CUSTOMER_PENDING_TRX_SETTLEMENT);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
		olaInfo.setBalance(amountsHolder.getTransactionAmount() - amountsHolder.getTotalInclusiveAmount());

        creditList.add(olaInfo);


		// Cr Agent 2 account - By Agent 2 Commission
		if(amountsHolder.getAgent2CommissionAmount() != null && amountsHolder.getAgent2CommissionAmount() > 0){
	        olaInfo = new OLAInfo();
	        olaInfo.setIsAgent(true);
	        if((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)){
		        olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
		        olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
	        }
	        olaInfo.setBalanceAfterTrxRequired(false);
	        olaInfo.setAgentBalanceAfterTrxRequired(true);
	        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
			olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
			olaInfo.setReasonId(ReasonConstants.CUSTOMER_PENDING_TRX_SETTLEMENT);
	        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
	        olaInfo.setPayingAccNo(agentAccountInfo.getAccountNo());

			olaInfo.setBalance(amountsHolder.getAgent2CommissionAmount());

	        creditList.add(olaInfo);
		}


        // Cr WHT A/C - By Agent2 WHT
		if(amountsHolder.getAgent2WHTAmount() != null && amountsHolder.getAgent2WHTAmount() > 0){
			olaInfo = getOLAInfoByCommStakeholderId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
					ReasonConstants.CUSTOMER_PENDING_TRX_SETTLEMENT,
					TransactionTypeConstantsInterface.OLA_CREDIT,
					CommissionConstantsInterface.WHT_STAKE_HOLDER_ID,
					amountsHolder.getAgent2WHTAmount());
			creditList.add(olaInfo);
		}

		//process agent 2 hierarchy commission
    	prepareHierarchyStakeholderCommissionsCreditList(switchWrapper, creditList, ReasonConstants.CUSTOMER_PENDING_TRX_SETTLEMENT);


        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);
        olaVO.setProductId(productId);
		olaVO.setCategory(TransactionConstantsInterface.RECIEVED_IN_ACCOUNT_CATEGORY_ID); // 5 -> Received in Account
        switchWrapper.setOlavo(olaVO);

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        switchWrapper.setToAccountNo(customerAccountInfo.getAccountNo());
        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setRecipientAgentAccountNo(agentAccountInfo.getAccountNo());

        return switchWrapper;
	*/

    }

    private void prepareSwitchWrapperForCustomerRetailPayment(SwitchWrapper switchWrapper, List<OLAInfo> debitList, List<OLAInfo> creditList) throws Exception {

        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();

        SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
        SmartMoneyAccountModel agentSMA = switchWrapper.getWorkFlowWrapper().getReceivingSmartMoneyAccountModel();
        AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
        AccountInfoModel agentAccountInfo = getAccountInfoModelBySmartMoneyAccount(agentSMA, switchWrapper.getWorkFlowWrapper().getRetailerAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        //Dr Customer Account
        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setIsAgent(false);
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        olaInfo.setReasonId(ReasonConstants.CUSTOMER_RETAIL_PAYMENT);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
        olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount());

        switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());

        debitList.add(olaInfo);


        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Dr Inclusive Charges Sundry A/C
            olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.CUSTOMER_RETAIL_PAYMENT,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            debitList.add(olaInfo);

            totalInclusiveCharges = 0.0;

        }
        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);


        //Cr Agent Account
        olaInfo = new OLAInfo();
        olaInfo.setIsAgent(true);
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
        olaInfo.setReasonId(ReasonConstants.CUSTOMER_RETAIL_PAYMENT);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olaInfo.setPayingAccNo(agentAccountInfo.getAccountNo());

        olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges);
        switchWrapper.setToAccountNo(agentAccountInfo.getAccountNo());

        creditList.add(olaInfo);


        // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
        prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.CUSTOMER_RETAIL_PAYMENT);


    }


    private void prepareSwitchWrapperForCustomerBBToCore(SwitchWrapper switchWrapper, List<OLAInfo> debitList, List<OLAInfo> creditList) throws Exception {

        SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
        AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();

        //Dr Customer A/C
        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setIsAgent(false);
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        olaInfo.setReasonId(ReasonConstants.CUSTOMER_BB_TO_CORE_AC);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
        olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount());

        switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());

        debitList.add(olaInfo);

        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Dr Inclusive Charges Sundry A/C
            olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.CUSTOMER_BB_TO_CORE_AC,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            debitList.add(olaInfo);

            totalInclusiveCharges = 0.0;
        }

        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        //Cr T24 Settlement A/C
        if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(ProductConstantsInterface.CUSTOMER_BB_TO_CORE_ACCOUNT)) {
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.CUSTOMER_BB_TO_CORE_AC,
                    TransactionTypeConstantsInterface.OLA_CREDIT,
                    PoolAccountConstantsInterface.T24_SETTLEMENT_ACCOUNT_ID,
                    PoolAccountConstantsInterface.OF_SETTLEMENT_IFT_POOL_ACCOUNT,
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges
            );
        } else {
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.CUSTOMER_BB_TO_CORE_AC,
                    TransactionTypeConstantsInterface.OLA_CREDIT,
                    PoolAccountConstantsInterface.AGENT_IBFT_POOL_ACCOUNT,
                    PoolAccountConstantsInterface.AGENT_IBFT_GL,
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges
            );
        }
        creditList.add(olaInfo);

        switchWrapper.setToAccountNo(olaInfo.getPayingAccNo());

        // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
        prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.CUSTOMER_BB_TO_CORE_AC);
    }


    private void prepareSwitchWrapperForfundCustomerBBToCore(SwitchWrapper switchWrapper, List<OLAInfo> debitList, List<OLAInfo> creditList) throws Exception {

        SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
        AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();

        //Dr Customer A/C
        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setIsAgent(false);
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        olaInfo.setReasonId(ReasonConstants.FUND_CUSTOMER_BB_CORE_AC);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
        olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount());

        switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());

        debitList.add(olaInfo);

        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Dr Inclusive Charges Sundry A/C
            olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.FUND_CUSTOMER_BB_CORE_AC,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            debitList.add(olaInfo);

            totalInclusiveCharges = 0.0;
        }

        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        //Cr T24 Settlement A/C
        if (switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().equals(ProductConstantsInterface.RELIEF_FUND_PRODUCT)) {
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.FUND_CUSTOMER_BB_CORE_AC,
                    TransactionTypeConstantsInterface.OLA_CREDIT,
                    PoolAccountConstantsInterface.T24_SETTLEMENT_ACCOUNT_ID,
                    PoolAccountConstantsInterface.OF_SETTLEMENT_IFT_POOL_ACCOUNT,
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges
            );
        } else {
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.FUND_CUSTOMER_BB_CORE_AC,
                    TransactionTypeConstantsInterface.OLA_CREDIT,
                    PoolAccountConstantsInterface.AGENT_IBFT_POOL_ACCOUNT,
                    PoolAccountConstantsInterface.AGENT_IBFT_GL,
                    switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges
            );
        }
        creditList.add(olaInfo);

        switchWrapper.setToAccountNo(olaInfo.getPayingAccNo());

        // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
        prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.FUND_CUSTOMER_BB_CORE_AC);
    }

    private void prepareSwitchWrapperForCustomerCashWithdrawal(SwitchWrapper switchWrapper, List<OLAInfo> debitList, List<OLAInfo> creditList) throws Exception {
        SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
        AccountInfoModel accountInfoModel = switchWrapper.getAccountInfoModel();
        AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();

        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
        Double totalExclCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount());

        // Debt Customer
        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setIsAgent(false);
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        olaInfo.setReasonId(ReasonConstants.CUSTOMER_CASH_WITHDRAWAL);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());

        olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + totalExclCharges);


        switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());

        debitList.add(olaInfo);


        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Dr Inclusive Charges Sundry A/C
            olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.CUSTOMER_CASH_WITHDRAWAL,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            debitList.add(olaInfo);

            totalInclusiveCharges = 0.0;

        }

        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        //Credit Agent Account
        olaInfo = new OLAInfo();
        olaInfo.setIsAgent(true);
        if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
            olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
            olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
        }
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setAgentBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
        olaInfo.setReasonId(ReasonConstants.CUSTOMER_CASH_WITHDRAWAL);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());

        Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));

        olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + agent1CommAmount - totalInclusiveCharges);
        switchWrapper.setToAccountNo(accountInfoModel.getAccountNo());

        creditList.add(olaInfo);

        // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
        prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.CUSTOMER_CASH_WITHDRAWAL);
    }

    private void prepareSwitchWrapperForCashDeposit(SwitchWrapper switchWrapper, List<OLAInfo> debitList, List<OLAInfo> creditList) throws Exception {
        AccountInfoModel accountInfoModel = switchWrapper.getAccountInfoModel();
        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();

        //Dr Agent A/C
        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setIsAgent(true);
        if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
            olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
            olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
        }
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
        olaInfo.setReasonId(ReasonConstants.CASH_DEPOSIT);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());

        Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
        olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() - agent1CommAmount);

        switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());

        debitList.add(olaInfo);

        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Dr Inclusive Charges Sundry A/C
            olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.CASH_DEPOSIT,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            debitList.add(olaInfo);

            totalInclusiveCharges = 0.0;

        }

        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        //Cr Customer A/C
        SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
        AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        OLAInfo customerOlaInfo = new OLAInfo();
        customerOlaInfo.setIsAgent(false);
        customerOlaInfo.setBalanceAfterTrxRequired(true);
        customerOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        customerOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        customerOlaInfo.setReasonId(ReasonConstants.CASH_DEPOSIT);
        customerOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        customerOlaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());

        customerOlaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges);

        switchWrapper.setToAccountNo(customerAccountInfo.getAccountNo());
        creditList.add(customerOlaInfo);

        // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
        prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.CASH_DEPOSIT);
    }

    /**
     * Performs Initial Deposit trx for Customer Account Opened at agent location.
     * NOTE: All Commissions (including agent comm) will be settled at leg II (Approved by Bank user)
     * Commission Amount is temporarily parked at FT Sundry Account
     *
     * @param switchWrapper
     * @param debitList
     * @param creditList
     * @throws Exception
     */


    private void prepareSwitchWrapperForAgentCashDeposit(SwitchWrapper switchWrapper, List<OLAInfo> debitList, List<OLAInfo> creditList) throws Exception {
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        OLAVO olaVO = new OLAVO();
        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Dr Inclusive Charges Sundry A/C
            OLAInfo olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.AGENT_CASH_DEPOSIT,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );
            debitList.add(olaInfo);
            totalInclusiveCharges = 0.0;
        }
        SmartMoneyAccountModel agentSMA = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();

        AccountInfoModel agentAccountInfo = null;
//        RetailerModel retailer = new RetailerModel();
//        retailer.setRetailerId(switchWrapper.getWorkFlowWrapper().getRetailerModel().getRetailerId());

        agentAccountInfo = getAccountInfoModelBySmartMoneyAccount(agentSMA, switchWrapper.getWorkFlowWrapper().getRetailerAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        OLAInfo agentOlaInfo = new OLAInfo();

        agentOlaInfo.setIsAgent(false);
        if (agentSMA.getPaymentModeId() == 7l)
            agentOlaInfo.setCustomerAccountTypeId(4l);
        else
            agentOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getFromRetailerContactModel().getOlaCustomerAccountTypeModelId());


        agentOlaInfo.setBalanceAfterTrxRequired(true);
        agentOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
//        agentOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerModel().getRetailerTypeId());
//        agentOlaInfo.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
        agentOlaInfo.setReasonId(ReasonConstants.AGENT_CASH_DEPOSIT);
        agentOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        agentOlaInfo.setPayingAccNo(agentAccountInfo.getAccountNo());
        agentOlaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() - totalInclusiveCharges);

        creditList.add(agentOlaInfo);


        //Cr Fonepay settlement A/C
        OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.AGENT_CASH_DEPOSIT,
                // These constant will be changed to a new Fonepay account
                TransactionTypeConstantsInterface.OLA_DEBIT,
                PoolAccountConstantsInterface.AGENT_CASH_DEPOSIT_POOL_ACCOUNT,
                PoolAccountConstantsInterface.AGENT_CASH_DEPOSIT_ACCOUNT_GL,
                switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount()
        );


        olaInfo.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
        debitList.add(olaInfo);


        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        switchWrapper.setFromAccountNo(olaInfo.getPayingAccNo());
        switchWrapper.setToAccountNo(agentAccountInfo.getAccountNo());


        prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.AGENT_CASH_DEPOSIT);
        olaVO.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
        olaVO.setReceivingAccNo(agentAccountInfo.getAccountNo());// to be used in posted trx
    }

    private void prepareSwitchWrapperForInitialDeposit(SwitchWrapper switchWrapper, List<OLAInfo> debitList, List<OLAInfo> creditList) throws Exception {

        WorkFlowWrapper wrapper = switchWrapper.getWorkFlowWrapper();
        //boolean isBvsAccount =  (Boolean) wrapper.getObject("isBvsAccount");


        Boolean isBvsAccountObj = (Boolean) wrapper.getObject("isBvsAccount");
        boolean isBvsAccount = (isBvsAccountObj == null) ? false : isBvsAccountObj;

        AccountInfoModel accountInfoModel = switchWrapper.getAccountInfoModel();
        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
        Double totalExclCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTransactionProcessingAmount());
        Double agent1CommAmount = 0.0D;
        if (isBvsAccount) {
            agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
        }

        //Dr Agent A/C
        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setIsAgent(true);
        if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
            olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
            olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
        }
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
        olaInfo.setReasonId(ReasonConstants.INITIAL_DEPOSIT);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());

        olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() - agent1CommAmount);

        switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());

        debitList.add(olaInfo);


        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            if (isBvsAccount) {
                //Debit Incl 3rd Party account Moved to Customer approval part
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.INITIAL_DEPOSIT,
                        switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);
            }
            totalInclusiveCharges = 0.0;
        }

        // ThirdPartyCheck to be used at customer activation for debit 3rd party account
        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setThirdPartyCheck(inclChargesCheck);

        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        //Cr Customer A/C
        SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
        AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        OLAInfo customerOlaInfo = new OLAInfo();
        customerOlaInfo.setIsAgent(false);
        customerOlaInfo.setBalanceAfterTrxRequired(true);
        customerOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        customerOlaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        customerOlaInfo.setReasonId(ReasonConstants.INITIAL_DEPOSIT);
        customerOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        customerOlaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());

        customerOlaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges);

        switchWrapper.setToAccountNo(customerAccountInfo.getAccountNo());
        creditList.add(customerOlaInfo);


        if (!isBvsAccount) {
            if (totalExclCharges + totalInclusiveCharges > 0) {
                //Cr Funds Transfer Sundry A/C
                olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.INITIAL_DEPOSIT,
                        TransactionTypeConstantsInterface.OLA_CREDIT,
                        PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID,
                        PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID,
                        totalExclCharges + totalInclusiveCharges
                );

                creditList.add(olaInfo);
            }
        } else {
            //enable these in case of L1
            //commission will be settled on approval of customer account creation
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.INITIAL_DEPOSIT);
        }

    }

    private void prepareSwitchWrapperForCustomerAccToAcc(SwitchWrapper switchWrapper, List<OLAInfo> debitList, List<OLAInfo> creditList) throws Exception {
        //Dr Sender Customer A/C
        SmartMoneyAccountModel senderCustomerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
        AccountInfoModel senderCustomerAccountInfo = getAccountInfoModelBySmartMoneyAccount(senderCustomerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
        Double totalAmount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount();

        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setIsAgent(false);
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        olaInfo.setReasonId(ReasonConstants.ACCOUNT_TO_ACCOUNT_CI);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaInfo.setPayingAccNo(senderCustomerAccountInfo.getAccountNo());
        olaInfo.setBalance(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount());

        switchWrapper.setFromAccountNo(senderCustomerAccountInfo.getAccountNo());

        debitList.add(olaInfo);


        long productId = switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().longValue();
        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());

        if (!switchWrapper.getWorkFlowWrapper().getDeviceTypeModel().getDeviceTypeId().equals(DeviceTypeConstantsInterface.WEB_SERVICE) &&
                switchWrapper.getWorkFlowWrapper().getReceiverAppUserModel() == null) {
            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.ACCOUNT_TO_ACCOUNT_CI,
                        productId,
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;
            }
        } else if (!switchWrapper.getWorkFlowWrapper().getDeviceTypeModel().getDeviceTypeId().equals(DeviceTypeConstantsInterface.WEB_SERVICE)) {
            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.ACCOUNT_TO_ACCOUNT_CI,
                        productId,
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;
            }
        } else if (switchWrapper.getWorkFlowWrapper().getDeviceTypeModel().getDeviceTypeId().equals(DeviceTypeConstantsInterface.WEB_SERVICE) &&
                switchWrapper.getWorkFlowWrapper().getReceiverAppUserModel() != null) {
            if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
                //Dr Inclusive Charges Sundry A/C
                olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.ACCOUNT_TO_ACCOUNT_CI,
                        productId,
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);

                totalInclusiveCharges = 0.0;
            }
        }
        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);


        if (switchWrapper.getWorkFlowWrapper().getDeviceTypeModel().getDeviceTypeId().equals(DeviceTypeConstantsInterface.WEB_SERVICE)) {
            if (switchWrapper.getWorkFlowWrapper().getReceiverAppUserModel() == null) {
                //Cr T24 Sundry	 A/C
                olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                        ReasonConstants.ACCOUNT_TO_ACCOUNT_CI,
                        TransactionTypeConstantsInterface.OLA_CREDIT,
                        PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID,
                        PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID,
                        totalAmount
                );

                // To be used in transactionDetail.customField2
                switchWrapper.setToAccountNo(olaInfo.getPayingAccNo());

                creditList.add(olaInfo);
            } else {
//                 olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
//                        ReasonConstants.ACCOUNT_TO_ACCOUNT_CI,
//                        TransactionTypeConstantsInterface.OLA_CREDIT,
//                        PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID,
//                        PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID,
//                        switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount()
//                );
//
//                // To be used in transactionDetail.customField2
//                switchWrapper.setToAccountNo(olaInfo.getPayingAccNo());
//
//                creditList.add(olaInfo);
                SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getRecipientSmartMoneyAccountModel();
                AccountInfoModel recipientCustomerAccountInfo = new AccountInfoModel();
                recipientCustomerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getRecipientCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
                olaInfo = new OLAInfo();
                olaInfo.setIsAgent(false);
                olaInfo.setBalanceAfterTrxRequired(true);
                olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRecipientCustomerModel().getCustomerAccountTypeId());
                olaInfo.setPayingAccNo(recipientCustomerAccountInfo.getAccountNo());
                switchWrapper.setToAccountNo(recipientCustomerAccountInfo.getAccountNo());
                olaInfo.setReasonId(ReasonConstants.ACCOUNT_TO_ACCOUNT_CI);
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);

                olaInfo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges)));

                creditList.add(olaInfo);
            }

        } else {
            //Cr Recipient Customer A/C
            SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getRecipientSmartMoneyAccountModel();
            AccountInfoModel recipientCustomerAccountInfo = new AccountInfoModel();
            recipientCustomerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getRecipientCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
            olaInfo = new OLAInfo();
            olaInfo.setIsAgent(false);
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRecipientCustomerModel().getCustomerAccountTypeId());
            olaInfo.setPayingAccNo(recipientCustomerAccountInfo.getAccountNo());
            switchWrapper.setToAccountNo(recipientCustomerAccountInfo.getAccountNo());
            olaInfo.setReasonId(ReasonConstants.ACCOUNT_TO_ACCOUNT_CI);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);

            olaInfo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - totalInclusiveCharges)));

            creditList.add(olaInfo);

            // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
        }
        if (!switchWrapper.getWorkFlowWrapper().getDeviceTypeModel().getDeviceTypeId().equals(DeviceTypeConstantsInterface.WEB_SERVICE) &&
                switchWrapper.getWorkFlowWrapper().getReceiverAppUserModel() == null) {
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.ACCOUNT_TO_ACCOUNT_CI);
        } else if (!switchWrapper.getWorkFlowWrapper().getDeviceTypeModel().getDeviceTypeId().equals(DeviceTypeConstantsInterface.WEB_SERVICE)) {
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.ACCOUNT_TO_ACCOUNT_CI);
        } else if (switchWrapper.getWorkFlowWrapper().getDeviceTypeModel().getDeviceTypeId().equals(DeviceTypeConstantsInterface.WEB_SERVICE) &&
                switchWrapper.getWorkFlowWrapper().getReceiverAppUserModel() != null) {
            prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.ACCOUNT_TO_ACCOUNT_CI);
        }
    }

    private void prepareSwitchWrapperForAccountToCash(SwitchWrapper switchWrapper, List<OLAInfo> debitList, List<OLAInfo> creditList) throws Exception {

        AccountInfoModel accountInfoModel = switchWrapper.getAccountInfoModel();
        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
        Double totalAmount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount();

        //Dr Customer A/C
        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setIsAgent(false);
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        olaInfo.setReasonId(ReasonConstants.CUSTOMER_ACCOUNT_TO_CASH);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaInfo.setBalance(totalAmount);

        SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
        AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
        switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());

        debitList.add(olaInfo);

        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Debit Incl 3rd Party account at Leg II
            totalInclusiveCharges = 0.0;
        }

        // ThirdPartyCheck to be used at leg II
        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setThirdPartyCheck(inclChargesCheck);
        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        //Cr T24 Sundry	 A/C
        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.CUSTOMER_ACCOUNT_TO_CASH,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID,
                PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID,
                totalAmount
        );

        // To be used in transactionDetail.customField2
        switchWrapper.setToAccountNo(olaInfo.getPayingAccNo());

        creditList.add(olaInfo);



        /*Cr Agent Account
        olaInfo = new OLAInfo();
		olaInfo.setIsAgent(true);
        if((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)){
	        olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
	        olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
        }
		olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
        olaInfo.setReasonId(ReasonConstants.CUSTOMER_ACCOUNT_TO_CASH);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());

		Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));

        olaInfo.setBalance(agent1CommAmount);

        creditList.add(olaInfo);

        // following check is to minus agent2WHT at cash to cash leg1 (to settle it in leg 2)
        // BE-WARE that this is assumed to be done after saving commission_transaction in transaction flow
        Double whtCommission = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
        if(whtCommission != null && whtCommission > 0 && agent2WHT > 0){
        	switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().put(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID, whtCommission - agent2WHT);
        }

        // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
        prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.CUSTOMER_ACCOUNT_TO_CASH);
        */
    }

    private void prepareSwitchWrapperForCustomerAccountToCash(SwitchWrapper switchWrapper, List<OLAInfo> debitList, List<OLAInfo> creditList) throws Exception {

        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
        Double totalAmount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount();

        //Dr Customer A/C
        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setIsAgent(false);
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        olaInfo.setReasonId(ReasonConstants.ACCOUNT_TO_CASH_CI);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaInfo.setBalance(totalAmount);

        SmartMoneyAccountModel customerSMA = switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel();
        AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
        switchWrapper.setFromAccountNo(customerAccountInfo.getAccountNo());

        debitList.add(olaInfo);

        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Debit Incl 3rd Party account at Leg II
            totalInclusiveCharges = 0.0;
        }

        // ThirdPartyCheck to be used at leg II
        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setThirdPartyCheck(inclChargesCheck);
        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        //Cr T24 Sundry	 A/C
        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.ACCOUNT_TO_CASH_CI,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID,
                PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID,
                totalAmount
        );

        // To be used in transactionDetail.customField2
        switchWrapper.setToAccountNo(olaInfo.getPayingAccNo());

        creditList.add(olaInfo);



        /*Cr Agent Account
        olaInfo = new OLAInfo();
		olaInfo.setIsAgent(true);
        if((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)){
	        olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
	        olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
        }
		olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
        olaInfo.setReasonId(ReasonConstants.CUSTOMER_ACCOUNT_TO_CASH);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());

		Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));

        olaInfo.setBalance(agent1CommAmount);

        creditList.add(olaInfo);

        // following check is to minus agent2WHT at cash to cash leg1 (to settle it in leg 2)
        // BE-WARE that this is assumed to be done after saving commission_transaction in transaction flow
        Double whtCommission = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID);
        if(whtCommission != null && whtCommission > 0 && agent2WHT > 0){
        	switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().put(CommissionConstantsInterface.WHT_STAKE_HOLDER_ID, whtCommission - agent2WHT);
        }

        // populate creditList with commission stakeholders FED, WH, Bank, Zong, I8
        prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.CUSTOMER_ACCOUNT_TO_CASH);
        */
    }

    public SwitchWrapper settleAccountOpeningCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, WorkFlowException, Exception {
        OLAVO olaVO = new OLAVO();

        List<OLAInfo> debitList = new ArrayList<OLAInfo>();

        List<OLAInfo> creditList = new ArrayList<OLAInfo>();


        OLAInfo olaInfo = new OLAInfo();

        //Cr Agent account for commission
        Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
//		Double agent1CommAmountNotSettled = agent1CommAmount; // this is used to skip agent commission settlement
        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalInclusiveAmount());

        if (agent1CommAmount > 0) {
            AccountInfoModel agentAccountInfo = null;
            boolean agent1InfoLoaded = false;
            Long agentAccTypeId = null;
            String agentAccountNo = null;

            AppUserModel agentAppUserModel = null;
            SmartMoneyAccountModel agentSMA = new SmartMoneyAccountModel();
            agentSMA.setRetailerContactId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getFromRetContactId());
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(agentSMA);
            baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
            agentSMA = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
            if (agentSMA != null) {
                agentAccountInfo = getAccountInfoModelBySmartMoneyAccount(agentSMA, switchWrapper.getWorkFlowWrapper().getTransactionModel().getCreatedBy(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
                if (agentAccountInfo == null) {
                    logger.error("[Settle Account Opening Commission] Failed to settle agent commission. Reason: Unable to load AccountInfoModel for Agent. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                } else {
                    agentAppUserModel = new AppUserModel();
                    agentAppUserModel.setRetailerContactId(agentSMA.getRetailerContactId());
                    agentAppUserModel = appUserManager.getAppUserModel(agentAppUserModel);
                    if (agentAppUserModel == null) {

                        logger.error("[Settle Account Opening Commission] Failed to settle agent1 commission. Reason: Unable to load agentAppUserModel for Agent1. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

                    } else if ((null != agentAppUserModel.getAccountClosedSettled() && agentAppUserModel.getAccountClosedSettled())
                            || (null != agentAppUserModel.getAccountClosedUnsettled() && agentAppUserModel.getAccountClosedUnsettled())) {

                        logger.error("[Settle Account Opening Commission] Failed to settle agent1 commission. Reason: ACCOUNT CLOSED !!! for agentAppUserId:" + agentAppUserModel.getAppUserId() + ". Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

                    } else {
                        agentAccTypeId = agentAppUserModel.getRetailerContactIdRetailerContactModel().getOlaCustomerAccountTypeModelId();
                        agentAccountNo = agentAccountInfo.getAccountNo();
                        agent1InfoLoaded = true;
                    }

                }
            } else {
                logger.error("[Settle Account Opening Commission]Failed to settle agent commission. Unable to load Agent's SmartMoneyAccountModel. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            }

            OLAInfo accOpeningOlaInfo = new OLAInfo();
            accOpeningOlaInfo.setBalance(agent1CommAmount);
            accOpeningOlaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            accOpeningOlaInfo.setReasonId(ReasonConstants.INITIAL_DEPOSIT);
            accOpeningOlaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            accOpeningOlaInfo.setBalanceAfterTrxRequired(Boolean.FALSE);

            if (agent1InfoLoaded) {
                accOpeningOlaInfo.setIsAgent(true);
                accOpeningOlaInfo.setPayingAccNo(agentAccountNo);
                accOpeningOlaInfo.setCustomerAccountTypeId(agentAccTypeId);
            } else {
                logger.error("[Settle Account Opening Commission] Sending Agent1 Comm to Default Agent Commission A/C. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                accOpeningOlaInfo = processUnsettledAgentCommission(switchWrapper,
                        ReasonConstants.INITIAL_DEPOSIT,
                        agent1CommAmount,
                        agentAppUserModel,
                        switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                        switchWrapper.getWorkFlowWrapper().getProductModel().getName(),
                        CommissionTypeConstants.UNSETTLED_ACC_OPENING_COMM_AGENT1);
            }

            creditList.add(accOpeningOlaInfo);

        }

        Double inclAmountFrom3rdParty = 0.0D;
        //Dr Inclusive Charges Sundry A/C
        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.INITIAL_DEPOSIT,
                    switchWrapper.getWorkFlowWrapper().getProductModel().getProductId(),
                    totalInclusiveCharges
            );

            debitList.add(olaInfo);

            inclAmountFrom3rdParty = totalInclusiveCharges;
        }

        Double sundryDebitAmount = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmount() - inclAmountFrom3rdParty;
        if (sundryDebitAmount > 0) {
            //Dr Funds Transfer Sundry A/C
            olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.INITIAL_DEPOSIT,
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID,
                    PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID,
                    sundryDebitAmount
            );

            debitList.add(olaInfo);
        }

        prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.INITIAL_DEPOSIT);

        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);

        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());

        olaVO.setCategory(TransactionConstantsInterface.SECOND_LEG_CATEGORY_ID); // category = Leg 2

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        return switchWrapper;

    }


    /**
     * prepares OLAInfo for Inclusive Charges
     *
     * @param trxCode
     * @param reasonId
     * @param productId
     * @param amount
     * @return OLAInfo
     */
    private OLAInfo getOLAInfoForInclCharges(String trxCode, Long reasonId, Long productId, Double amount) throws FrameworkCheckedException {

        List<StakeholderBankInfoModel> list = stakeholderBankInfoManager.getStakeholderBankInfoForInclCharges(productId);
        if (list == null) {
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.error("!!!!!! getOLAInfoForInclCharges - no record found for productId:" + productId + " !!!!!!");
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);
        }

        if (list.size() != 2) {
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.error("!!!!!! getOLAInfoForInclCharges - invalid data (rows != 2) for productId:" + productId + " !!!!!!");
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);
        }

        if (((StakeholderBankInfoModel) list.get(0)).getBankId().longValue() == ((StakeholderBankInfoModel) list.get(1)).getBankId().longValue()) {
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.error("!!!!!! getOLAInfoForInclCharges - invalid data (same bank_id) for productId:" + productId + " !!!!!!");
            logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);
        }

        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setBalanceAfterTrxRequired(Boolean.FALSE);
        olaInfo.setMicrobankTransactionCode(trxCode);
        olaInfo.setCustomerAccountTypeId(CustomerAccountTypeConstants.SETTLEMENT);
        olaInfo.setReasonId(reasonId);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaInfo.setBalance(Double.parseDouble(Formatter.formatDouble(amount)));

        for (StakeholderBankInfoModel shBankInfoModel : list) {
            if (shBankInfoModel.getBankId().longValue() == BankConstantsInterface.ASKARI_BANK_ID.longValue()) {
                olaInfo.setCoreStakeholderBankInfoId(shBankInfoModel.getStakeholderBankInfoId());
            } else {
                olaInfo.setPayingAccNo(shBankInfoModel.getAccountNo());
            }
        }

        return olaInfo;
    }

    public SwitchWrapper getAccountTitle(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();
        AccountInfoModel accountInfoModel = new AccountInfoModel();

        if (appUserModel.getAppUserTypeId() == UserTypeConstantsInterface.RETAILER.longValue()) {
            accountInfoModel.setCustomerId(appUserModel.getAppUserId());
        } else {
            accountInfoModel.setCustomerId(appUserModel.getCustomerIdCustomerModel().getCustomerId());
        }
        accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_RRN, switchWrapper.getObject(CommandFieldConstants.KEY_RRN));
        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_STAN, switchWrapper.getObject(CommandFieldConstants.KEY_STAN));
        veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_PRODUCT_ID, switchWrapper.getObject(CommandFieldConstants.KEY_PRODUCT_ID));

        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        try {
            //RRN and STAN will be saved in posted trx via verifyCredentials()
            veriflyBaseWrapper = this.verifyCredentials(veriflyBaseWrapper);
            if (veriflyBaseWrapper.isErrorStatus()) {

                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();

                OLAVO olaVO = new OLAVO();
                olaVO.setBalance(switchWrapper.getTransactionAmount());
                olaVO.setPayingAccNo(accountInfoModel.getAccountNo());
                olaVO.setReasonId(com.inov8.ola.util.ReasonConstants.SETTLEMENT);
                switchWrapper.setOlavo(olaVO);
                switchWrapper = this.switchController.getAccountTitle(switchWrapper);

            }
        } catch (Exception e) {
            throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }

        return switchWrapper;

    }

    public SwitchWrapper reverseP2PTransaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException, WorkFlowException, Exception {
        OLAVO olaVO = new OLAVO();

        switchWrapper.setSmartMoneyAccountModel(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel());
        switchWrapper.setBankId(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getSmartMoneyAccountId());
        switchWrapper.setPaymentModeId(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getPaymentModeId());

        List<OLAInfo> debitList = new ArrayList<OLAInfo>();

        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        CommissionAmountsHolder amountsHolder = switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder();

        OLAInfo olaInfo = new OLAInfo();

        // Dr Unclaimed BLB A/C
        // By Tx amount + Agent2 commission + Agent2 WHT - Total Inclusive Charges
        olaInfo = getStakeholderOLAInfo(
                switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.SENDER_REDEEM,
                TransactionTypeConstantsInterface.OLA_DEBIT,
                PoolAccountConstantsInterface.UNCLAIMED_C2C_SUNDARY_OLA_ACCOUNT,
                PoolAccountConstantsInterface.UNCLAIMED_C2C_SUNDARY_ACCOUNT,
                amountsHolder.getTransactionAmount()
                        + amountsHolder.getAgent2CommAmountBeforeHierarchy()
                        + amountsHolder.getAgent2WHTAmount()
                        - amountsHolder.getTotalInclusiveAmount());

        debitList.add(olaInfo);

        // Cr Agent account for commission
        // By Tx amount + Agent2 commission � Total Inclusive Charges
        AccountInfoModel agentAccountInfo = getAccountInfoModelBySmartMoneyAccount(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel(), switchWrapper.getWorkFlowWrapper().getAppUserModel().getAppUserId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());
        if (agentAccountInfo == null) {
            logger.error("[reverseP2PTransaction] Unable to load Agent Account Info...Agent AppUserId:" + switchWrapper.getWorkFlowWrapper().getAppUserModel().getAppUserId());
            throw new WorkFlowException("Unable to load Agent Account Info");
        }

        olaInfo = new OLAInfo();
        olaInfo.setIsAgent(true);
        if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
            olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
            olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
        }
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setAgentBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
        olaInfo.setReasonId(ReasonConstants.SENDER_REDEEM);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olaInfo.setPayingAccNo(agentAccountInfo.getAccountNo());

        Double agent2CreditAmount = amountsHolder.getTransactionAmount() + amountsHolder.getAgent2CommissionAmount() - amountsHolder.getTotalInclusiveAmount();

        olaInfo.setBalance(agent2CreditAmount);

        creditList.add(olaInfo);


        // Cr WHT A/C
        // By Agent2 WHT
        if (amountsHolder.getAgent2WHTAmount() != null && amountsHolder.getAgent2WHTAmount() > 0) {
            olaInfo = getOLAInfoByCommStakeholderId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.SENDER_REDEEM,
                    TransactionTypeConstantsInterface.OLA_CREDIT,
                    CommissionConstantsInterface.WHT_STAKE_HOLDER_ID,
                    amountsHolder.getAgent2WHTAmount());
            creditList.add(olaInfo);
        }

        //process hierarchy commission
        prepareHierarchyStakeholderCommissionsCreditList(switchWrapper, creditList, ReasonConstants.SENDER_REDEEM);

        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);

        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
        switchWrapper.setToAccountNo(agentAccountInfo.getAccountNo());
        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setRecipientAgentAccountNo(agentAccountInfo.getAccountNo());

        olaVO.setCategory(TransactionConstantsInterface.SECOND_LEG_CATEGORY_ID); // category = Leg 2

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        return switchWrapper;

    }

    public SwitchWrapper reverseTransaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException, WorkFlowException, Exception {
        OLAVO olaVO = new OLAVO();

        switchWrapper.setBankId(switchWrapper.getBankId());
        switchWrapper.setPaymentModeId(switchWrapper.getPaymentModeId());

        Boolean isExpired = (Boolean) switchWrapper.getObject("isExpired");

        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        Long olaAccount = PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID;
        Long coreAccount = PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID;

        if (isExpired != null && isExpired) {
            olaAccount = PoolAccountConstantsInterface.UNCLAIMED_C2C_SUNDARY_OLA_ACCOUNT;
            coreAccount = PoolAccountConstantsInterface.UNCLAIMED_C2C_SUNDARY_ACCOUNT;
        }

        Boolean isThirdPartyIncluded = switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getThirdPartyCheck();
        isThirdPartyIncluded = isThirdPartyIncluded == null ? Boolean.FALSE : isThirdPartyIncluded;
        Double inclusiveChargesApplied = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getInclusiveCharges());

        double totalUnsettledCommission = CommonUtils.getDoubleOrDefaultValue(
                switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmountUnsettled());

        Double totalAmount = Double.valueOf(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getTransactionAmount()
                + totalUnsettledCommission
                - inclusiveChargesApplied));

        logger.info("CSR reverseTransaction Details - Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode()
                + "\nProduct: " + switchWrapper.getWorkFlowWrapper().getProductModel().getProductId()
                + "\nsundryDebitAmount: " + totalAmount
                + "\ntrxAmount: " + switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getTransactionAmount()
                + "\ninclChargesApplied: " + inclusiveChargesApplied
                + "\ntotalUnsettledComm: " + totalUnsettledCommission
        );


        OLAInfo debitOlaInfo = getStakeholderOLAInfo(
                switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.SENDER_REDEEM,
                TransactionTypeConstantsInterface.OLA_DEBIT,
                olaAccount,
                coreAccount,
                totalAmount);

        debitList.add(debitOlaInfo);

        OLAInfo creditOlaInfo = getStakeholderOLAInfo(
                switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.SENDER_REDEEM,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                PoolAccountConstantsInterface.REDEMPTION_GL_ACCOUNT_OLA,
                PoolAccountConstantsInterface.REDEMPTION_GL_ACCOUNT_CORE,
                totalAmount);

        creditList.add(creditOlaInfo);

        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);

        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
        olaVO.setCategory(TransactionConstantsInterface.REDEMPTION_REQUEST_CATEGORY_ID); // category = Redemption Request

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        return switchWrapper;

    }

    public SwitchWrapper performLeg2(SwitchWrapper switchWrapper, Long reasonId, Long categoryId, boolean settleCommission) throws FrameworkCheckedException, WorkFlowException, Exception {

        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();
        OLAInfo olaInfo = new OLAInfo();
        WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();
        TransactionDetailMasterModel trxDetailMasterModel = workFlowWrapper.getTransactionDetailMasterModel();
        double productId = workFlowWrapper.getProductModel().getProductId();

        switchWrapper.setSmartMoneyAccountModel(workFlowWrapper.getSmartMoneyAccountModel());
        switchWrapper.setBankId(workFlowWrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
        switchWrapper.setPaymentModeId(workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId());

        boolean inclChargesCheck = false;
        if (trxDetailMasterModel.getThirdPartyCheck() != null && trxDetailMasterModel.getThirdPartyCheck()) {
            inclChargesCheck = true;
        }

        //Cr Agent1 account for commission
        Double agent1CommAmount = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT1_STAKE_HOLDER_ID));
        Double agent2CommAmount = CommonUtils.getDoubleOrDefaultValue(workFlowWrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID));
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(trxDetailMasterModel.getInclusiveCharges());
        String agent2AccountNumber = null;

        if (settleCommission && agent1CommAmount > 0) {
            String agentAccountNo = null;
            Long agentAccTypeId = null;
            boolean agent1InfoLoaded = false;
            AppUserModel agentAppUserModel = null;

            try {
                SmartMoneyAccountModel agentSMA = new SmartMoneyAccountModel();
                agentSMA.setRetailerContactId(workFlowWrapper.getTransactionModel().getFromRetContactId());
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                baseWrapper.setBasePersistableModel(agentSMA);
                baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
                agentSMA = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();
                if (agentSMA != null) {
                    AccountInfoModel agentAccountInfo = getAccountInfoModelBySmartMoneyAccount(agentSMA, workFlowWrapper.getTransactionModel().getCreatedBy(), workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());
                    if (agentAccountInfo == null) {
                        logger.error("[performLeg2] Failed to settle agent1 commission. Reason: Unable to load AccountInfoModel for Agent1. Transaction ID: " + workFlowWrapper.getTransactionCodeModel().getCode());
                    } else {
                        agentAppUserModel = new AppUserModel();
                        agentAppUserModel.setRetailerContactId(agentSMA.getRetailerContactId());
                        agentAppUserModel = appUserManager.getAppUserModel(agentAppUserModel);
                        if (agentAppUserModel == null) {

                            logger.error("[performLeg2] Failed to settle agent1 commission. Reason: Unable to load agentAppUserModel for Agent1. Transaction ID: " + workFlowWrapper.getTransactionCodeModel().getCode());

                        } else if ((null != agentAppUserModel.getAccountClosedSettled() && agentAppUserModel.getAccountClosedSettled())
                                || (null != agentAppUserModel.getAccountClosedUnsettled() && agentAppUserModel.getAccountClosedUnsettled())) {

                            logger.error("[performLeg2] Failed to settle agent1 commission. Reason: ACCOUNT CLOSED !!! for agentAppUserId:" + agentAppUserModel.getAppUserId() + ". Transaction ID: " + workFlowWrapper.getTransactionCodeModel().getCode());

                        } else {
                            agentAccTypeId = agentAppUserModel.getRetailerContactIdRetailerContactModel().getOlaCustomerAccountTypeModelId();
                            agentAccountNo = agentAccountInfo.getAccountNo();
                            agent1InfoLoaded = true;
                        }
                    }
                } else {
                    logger.error("[performLeg2] Failed to settle agent1 commission. Unable to load Agent1's SmartMoneyAccountModel. Transaction ID: " + workFlowWrapper.getTransactionCodeModel().getCode());
                }
            } catch (Exception ex) {
                logger.error("[performLeg2] Exception occurred while settlement of agent1 Commission. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(), ex);
            }

            olaInfo = new OLAInfo();
            olaInfo.setIsAgent(true);
            olaInfo.setBalanceAfterTrxRequired(false);
            olaInfo.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
            olaInfo.setReasonId(reasonId);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setBalance(agent1CommAmount);

            if (agent1InfoLoaded) {
                olaInfo.setPayingAccNo(agentAccountNo);
                olaInfo.setCustomerAccountTypeId(agentAccTypeId);
            } else {
                logger.error("[performLeg2] Sending Agent1 Comm to Default Agent Commission A/C. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaInfo = processUnsettledAgentCommission(switchWrapper,
                        reasonId,
                        agent1CommAmount,
                        agentAppUserModel,
                        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getProductId(),
                        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getProductName(),
                        CommissionTypeConstants.UNSETTLED_COMM_AGENT1);

            }
            creditList.add(olaInfo);
        }

        double totalUnsettledCommission = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmountUnsettled());
        double totalCommAmount = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getInclusiveCharges()) +
                CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getExclusiveCharges());

        boolean inclusiveAccAlreadyDr = true;
        if (totalCommAmount - totalUnsettledCommission < 0.01) { // to handle schenaro of 0.899999999 or 0.0000000001 etc
            inclusiveAccAlreadyDr = false;
        }

        //Dr Inclusive Charges Sundry A/C
        Double inclChargesApplied = totalInclusiveCharges;
        if (settleCommission && inclChargesCheck && totalInclusiveCharges > 0.0) {
            if (!inclusiveAccAlreadyDr) {
                olaInfo = getOLAInfoForInclCharges(workFlowWrapper.getTransactionCodeModel().getCode(),
                        reasonId,
                        workFlowWrapper.getProductModel().getProductId(),
                        totalInclusiveCharges
                );

                debitList.add(olaInfo);
                logger.info("Applying incl 3rd Party Debit (Debit Not performed at leg1). totalInclCharges:" + totalInclusiveCharges + ", totalCommAmount:" + totalCommAmount + ",totalUnsettledCommission:" + totalUnsettledCommission);
            } else {
                logger.info("Skipping incl 3rd Party Debit (Debit already performed at leg1). totalInclCharges:" + totalInclusiveCharges + ", totalCommAmount:" + totalCommAmount + ",totalUnsettledCommission:" + totalUnsettledCommission);
            }
            inclChargesApplied = 0D;
        }
        switchWrapper.setInclusiveChargesApplied(inclChargesApplied);

        Long olaAccountID = PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID;
        Long ofSettlementAccountID = PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID;

        if (reasonId == ReasonConstants.SENDER_REDEEM) {
            olaAccountID = PoolAccountConstantsInterface.REDEMPTION_GL_ACCOUNT_OLA;
            ofSettlementAccountID = PoolAccountConstantsInterface.REDEMPTION_GL_ACCOUNT_CORE;
        }

        //Dr Funds Transfer Sundry A/C
//		Double sundryDebitAmount = trxDetailMasterModel.getTotalAmount();
        Double sundryDebitAmount = Double.valueOf(Formatter.formatDouble(trxDetailMasterModel.getTransactionAmount() - totalInclusiveCharges + totalUnsettledCommission));
        logger.info("performLeg2 Details - Transaction ID: " + workFlowWrapper.getTransactionCodeModel().getCode()
                + "\nProduct: " + switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getProductName()
                + "\nsundryDebitAmount: " + sundryDebitAmount
                + "\ntrxAmount: " + trxDetailMasterModel.getTransactionAmount()
                + "\ninclChargesApplied: " + inclChargesApplied
                + "\ninclChargesTotal: " + totalInclusiveCharges
                + "\ntotalUnsettledComm: " + totalUnsettledCommission
        );

        if (sundryDebitAmount > 0) {
            olaInfo = getStakeholderOLAInfo(workFlowWrapper.getTransactionCodeModel().getCode(),
                    reasonId,
                    TransactionTypeConstantsInterface.OLA_DEBIT,
                    olaAccountID,
                    ofSettlementAccountID,
                    sundryDebitAmount
            );

            debitList.add(olaInfo);
        }


        if (reasonId == ReasonConstants.CUSTOMER_PENDING_TRX_SETTLEMENT) {

            SmartMoneyAccountModel customerSMA = workFlowWrapper.getOlaSmartMoneyAccountModel();
            AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, workFlowWrapper.getCustomerModel().getCustomerId(), workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());
            AccountInfoModel agentAccountInfo = getAccountInfoModelBySmartMoneyAccount(workFlowWrapper.getSmartMoneyAccountModel(), workFlowWrapper.getAppUserModel().getAppUserId(), workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());
            if (agentAccountInfo == null) {
                logger.error("[performLeg2] Unable to load Agent Account Info...Agent AppUserId:" + workFlowWrapper.getAppUserModel().getAppUserId());
                throw new WorkFlowException("Unable to load Agent Account Info");
            }

            agent2AccountNumber = agentAccountInfo.getAccountNo();

            //Cr Customer A/C
            olaInfo = new OLAInfo();
            olaInfo.setIsAgent(false);
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            olaInfo.setReasonId(reasonId);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
            olaInfo.setBalance(workFlowWrapper.getTransactionModel().getTransactionAmount() - inclChargesApplied);
            creditList.add(olaInfo);

            // Cr Agent 2 - By Agent 2 Commission
            if (agent2CommAmount != null && agent2CommAmount > 0) {
                olaInfo = new OLAInfo();
                olaInfo.setIsAgent(true);
                if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
                    olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
                    olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
                }
                olaInfo.setBalanceAfterTrxRequired(false);
                olaInfo.setAgentBalanceAfterTrxRequired(true);
                olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
                olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
                olaInfo.setReasonId(reasonId);
                olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
                olaInfo.setPayingAccNo(agentAccountInfo.getAccountNo());
                olaInfo.setBalance(agent2CommAmount);
                creditList.add(olaInfo);
            }

        } else if (reasonId == ReasonConstants.RECEIVE_CASH) {

            //Cr Agent2
            olaInfo = new OLAInfo();
            olaInfo.setIsAgent(Boolean.TRUE);
            if ((workFlowWrapper.getHandlerModel() != null)) {
                olaInfo.setHandlerId(workFlowWrapper.getHandlerModel().getHandlerId());
                olaInfo.setHandlerAccountTypeId(workFlowWrapper.getHandlerModel().getAccountTypeId());
            }
            olaInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
            olaInfo.setAgentBalanceAfterTrxRequired(Boolean.TRUE);
            olaInfo.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(workFlowWrapper.getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            olaInfo.setReasonId(reasonId);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setPayingAccNo(switchWrapper.getAccountInfoModel().getAccountNo());
            Double agent2CreditAmount = (workFlowWrapper.getTransactionModel().getTransactionAmount() - inclChargesApplied) + agent2CommAmount;
            olaInfo.setBalance(agent2CreditAmount);
            creditList.add(olaInfo);

            agent2AccountNumber = switchWrapper.getAccountInfoModel().getAccountNo();

        } else if (reasonId == ReasonConstants.SENDER_REDEEM && productId == ProductConstantsInterface.CASH_TRANSFER) {
            Double agent2CreditAmount = 0D;

            AccountInfoModel agent2AccountInfo = getAccountInfoModelBySmartMoneyAccount(workFlowWrapper.getSmartMoneyAccountModel(), workFlowWrapper.getAppUserModel().getAppUserId(), workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());
            if (agent2AccountInfo == null) {
                logger.error("[performLeg2] Unable to load Agent Account Info. Agent AppUserId:" + workFlowWrapper.getAppUserModel().getAppUserId());
                throw new WorkFlowException("Unable to load Agent Account Info");
            }

            agent2AccountNumber = agent2AccountInfo.getAccountNo();
            switchWrapper.setToAccountNo(agent2AccountInfo.getAccountNo());

            if (settleCommission) { // Partial Sender Redemption Flow
                agent2CreditAmount = (workFlowWrapper.getTransactionModel().getTransactionAmount() - inclChargesApplied) + agent2CommAmount;
            } else { // Full Sender Redemption Flow
                agent2CreditAmount = sundryDebitAmount;
            }

            //Cr Agent2
            olaInfo = new OLAInfo();
            olaInfo.setIsAgent(Boolean.TRUE);
            if ((workFlowWrapper.getHandlerModel() != null)) {
                olaInfo.setHandlerId(workFlowWrapper.getHandlerModel().getHandlerId());
                olaInfo.setHandlerAccountTypeId(workFlowWrapper.getHandlerModel().getAccountTypeId());
            }
            olaInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
            olaInfo.setAgentBalanceAfterTrxRequired(Boolean.TRUE);
            olaInfo.setMicrobankTransactionCode(workFlowWrapper.getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(workFlowWrapper.getRetailerContactModel().getOlaCustomerAccountTypeModelId());
            olaInfo.setReasonId(reasonId);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setPayingAccNo(agent2AccountInfo.getAccountNo());
            olaInfo.setBalance(agent2CreditAmount);
            creditList.add(olaInfo);

        } else if (reasonId == ReasonConstants.SENDER_REDEEM && (productId == ProductConstantsInterface.ACCOUNT_TO_CASH || productId == ProductConstantsInterface.ACT_TO_CASH_CI)) {
//			double agent2WhtAmount = 0D;
            Double amountToReverse = 0D;
//			TaxValueBean agent2TaxBean = workFlowWrapper.getCommissionAmountsHolder().getStakeholderTaxMap().get(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID);
//			if(agent2TaxBean != null && agent2TaxBean.getWhtAmount() != null && agent2TaxBean.getWhtAmount() > 0){
//				agent2WhtAmount = agent2TaxBean.getWhtAmount().doubleValue();
//			}

            if (settleCommission) { // Partial Sender Redemption Flow
//                amountToReverse = workFlowWrapper.getTransactionModel().getTransactionAmount() - inclChargesApplied + agent2CommAmount;
                amountToReverse = workFlowWrapper.getTransactionModel().getTransactionAmount() - inclChargesApplied;
            } else { // Full Sender Redemption Flow
                amountToReverse = sundryDebitAmount;
            }

            //Following is be used in Customer SMS
            switchWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, amountToReverse);

            SmartMoneyAccountModel customerSMA = workFlowWrapper.getOlaSmartMoneyAccountModel();
            AccountInfoModel customerAccountInfo = getAccountInfoModelBySmartMoneyAccount(customerSMA, workFlowWrapper.getCustomerAppUserModel().getCustomerId(), workFlowWrapper.getTransactionCodeModel().getTransactionCodeId());
            if (customerAccountInfo == null) {
                logger.error("[performLeg2] Unable to load Customer Account Info... AppUserId:" + workFlowWrapper.getCustomerAppUserModel().getAppUserId());
                throw new WorkFlowException("Unable to load Customer Account Info");
            }

            //Cr Customer A/C
            olaInfo = new OLAInfo();
            olaInfo.setIsAgent(false);
            olaInfo.setBalanceAfterTrxRequired(true);
            olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
            olaInfo.setReasonId(reasonId);
            olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
            olaInfo.setPayingAccNo(customerAccountInfo.getAccountNo());
            olaInfo.setBalance(amountToReverse);
            creditList.add(olaInfo);

            if (switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.BANK_STAKE_HOLDER_ID) == null) {
                if (switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID) != null) {
                    olaInfo = getOLAInfoByCommStakeholderId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                            reasonId,
                            TransactionTypeConstantsInterface.OLA_CREDIT,
                            CommissionConstantsInterface.BANK_STAKE_HOLDER_ID,
                            switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID)
                    );
                    creditList.add(olaInfo);
                }
            }


        }

        if (settleCommission) {
            prepareCommissionCreditList(switchWrapper, creditList, reasonId);
        }

        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);

        olaVO.setProductId(workFlowWrapper.getProductModel().getProductId());

        olaVO.setCategory(categoryId);

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        if (!StringUtil.isNullOrEmpty(agent2AccountNumber)) {
            workFlowWrapper.getTransactionDetailMasterModel().setRecipientAgentAccountNo(agent2AccountNumber);
        }
        return switchWrapper;

    }

    private OLAInfo processUnsettledAgentCommission(SwitchWrapper switchWrapper,
                                                    Long reasonId,
                                                    Double commissionAmount,
                                                    AppUserModel agentAppUserModel,
                                                    Long productId,
                                                    String productName,
                                                    String commissionType)
            throws FrameworkCheckedException {

        OLAInfo olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                reasonId,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                PoolAccountConstantsInterface.AGENT_DEFAULT_COMMISSION_ACCOUNT_OLA,
                PoolAccountConstantsInterface.AGENT_DEFAULT_COMMISSION_ACCOUNT_CORE,
                commissionAmount
        );

        olaInfo.setCategory(TransactionConstantsInterface.DEFAULT_AGNET_ACC_CATEGORY_ID);

        UnsettledAgentCommModel unsettledModel = new UnsettledAgentCommModel(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                switchWrapper.getWorkFlowWrapper().getTransactionModel().getCreatedOn(),
                productId,
                productName,
                commissionType,
                commissionAmount
        );

        Long agentAppUserId = (agentAppUserModel == null) ? null : agentAppUserModel.getAppUserId();
        logger.error("[processUnsettledAgentCommission] Going to save UnsettledCommission for agentAppUserId=" + agentAppUserId + ". Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        commissionManager.saveUnsettledCommission(unsettledModel, agentAppUserId);

        return olaInfo;
    }


    public SwitchWrapper customerWHTDeduction(SwitchWrapper switchWrapper, Long poolAccountId, Long corePoolAccountId) throws Exception {

        Long reasonId = (Long) switchWrapper.getObject("REASON_ID");

        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        switchWrapper.setOlavo(olaVO);
        switchWrapper.getOlavo().setCategory(1L);
        switchWrapper.getOlavo().setReasonId(reasonId);

        WorkFlowWrapper wrapper = switchWrapper.getWorkFlowWrapper();
        String transactionCode = wrapper.getTransactionCodeModel().getCode();
        Long userId = null;
        Long accountTypeId = null;
        if (wrapper.getCustomerModel() != null) {
            userId = wrapper.getCustomerModel().getCustomerId();
            accountTypeId = wrapper.getCustomerModel().getCustomerAccountTypeId();
        } else if (wrapper.getRetailerContactModel() != null) {
            userId = wrapper.getAppUserModel().getAppUserId();
            accountTypeId = wrapper.getRetailerContactModel().getOlaCustomerAccountTypeModelId();
        }

        SmartMoneyAccountModel senderCustomerSMA = wrapper.getOlaSmartMoneyAccountModel();
        AccountInfoModel accountInfo = getAccountInfoModelBySmartMoneyAccount(senderCustomerSMA, userId,
                wrapper.getTransactionCodeModel().getTransactionCodeId());

        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setIsAgent(Boolean.FALSE);
        olaInfo.setBalanceAfterTrxRequired(Boolean.TRUE);
        olaInfo.setMicrobankTransactionCode(transactionCode);
        olaInfo.setCustomerAccountTypeId(accountTypeId);
        olaInfo.setReasonId(reasonId);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaInfo.setPayingAccNo(accountInfo.getAccountNo());
        olaInfo.setBalance(wrapper.getTransactionModel().getTotalAmount());
        //	olaInfo.setNarration(MessageUtil.getMessage("tx.narration.totalAmount.debit"));
        switchWrapper.setFromAccountNo(accountInfo.getAccountNo());

        debitList.add(olaInfo);

        olaInfo = getStakeholderOLAInfo(transactionCode, reasonId,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                poolAccountId, corePoolAccountId, wrapper.getTransactionModel().getTotalAmount());

        switchWrapper.setToAccountNo(olaInfo.getPayingAccNo());
        wrapper.getTransactionDetailMasterModel().setSundryAmount(olaInfo.getBalance());
        creditList.add(olaInfo);

        olaVO.setFromSegmentId(switchWrapper.getWorkFlowWrapper().getFromSegmentId());
        olaVO.setToSegmentId(switchWrapper.getWorkFlowWrapper().getToSegmentId());
        olaVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
        switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());

        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);
        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        // setting OlaSwitchWrapper to be used in TransactionProcessor for Day End settlement process
        switchWrapper.getWorkFlowWrapper().setOLASwitchWrapper(switchWrapper);

        return switchWrapper;
    }

    private void prepareSwitchWrapperForCashToCash(SwitchWrapper switchWrapper, List<OLAInfo> debitList, List<OLAInfo> creditList) throws Exception {
        AccountInfoModel accountInfoModel = switchWrapper.getAccountInfoModel();
        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getInclusivePercentAmount());
        Double totalAmount = switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount();

        //Dr Agent A/C
        OLAInfo olaInfo = new OLAInfo();
        olaInfo.setIsAgent(true);
        if ((switchWrapper.getWorkFlowWrapper().getHandlerModel() != null)) {
            olaInfo.setHandlerId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getHandlerId());
            olaInfo.setHandlerAccountTypeId(switchWrapper.getWorkFlowWrapper().getHandlerModel().getAccountTypeId());
        }
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getRetailerContactModel().getOlaCustomerAccountTypeModelId());
        olaInfo.setReasonId(ReasonConstants.CUSTOMER_CASH_TO_CASH);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
        olaInfo.setPayingAccNo(accountInfoModel.getAccountNo());

        olaInfo.setBalance(totalAmount);

        switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());

        debitList.add(olaInfo);

        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Debit Incl 3rd Party account at Leg II
            totalInclusiveCharges = 0.0;
        }

        // ThirdPartyCheck to be used at leg II
        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setThirdPartyCheck(inclChargesCheck);
        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.CUSTOMER_CASH_TO_CASH,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID,
                PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID,
                totalAmount
        );

        switchWrapper.setToAccountNo(olaInfo.getPayingAccNo());
        switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setSundryAmount(olaInfo.getBalance());
        creditList.add(olaInfo);

    }

    public SwitchWrapper walletToWalletTransactionTransferFunds(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {
        logger.info("start of OLAVeriflyFinancialInstitutionImpl.walletToWalletTransactionTransferFunds() :: " + new Date());
        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        long productId = ProductConstantsInterface.ACT_TO_ACT_CI;
        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getInclusiveCharges());

        OLAInfo olaInfo = new OLAInfo();

        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Dr Inclusive Charges Sundry A/C
            olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.ACCOUNT_TO_ACCOUNT_CI,
                    productId,
                    totalInclusiveCharges
            );

            debitList.add(olaInfo);

            totalInclusiveCharges = 0.0;
        }

        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        Long olaAccountID = PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID;
        Long ofSettlementAccountID = PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID;
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);

        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.ACCOUNT_TO_ACCOUNT_CI,
                TransactionTypeConstantsInterface.OLA_DEBIT,
                olaAccountID,
                ofSettlementAccountID,
                switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount()
        );
        debitList.add(olaInfo);

        SmartMoneyAccountModel receiverCustomerSMA = switchWrapper.getWorkFlowWrapper().getReceivingSmartMoneyAccountModel();
        AccountInfoModel receiverCustomerAccountInfo = getAccountInfoModelBySmartMoneyAccount(receiverCustomerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        olaInfo = new OLAInfo();
        olaInfo.setIsAgent(false);
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        olaInfo.setReasonId(ReasonConstants.ACCOUNT_TO_ACCOUNT_CI);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olaInfo.setPayingAccNo(receiverCustomerAccountInfo.getAccountNo());
        olaInfo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getTransactionAmount())) - totalInclusiveCharges);

        switchWrapper.setFromAccountNo(receiverCustomerAccountInfo.getAccountNo());

        creditList.add(olaInfo);

        prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.ACCOUNT_TO_ACCOUNT_CI);
//        if (settleCommission) {
//            prepareCommissionCreditList(switchWrapper, creditList, reasonId);
//        }

        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        return switchWrapper;
    }

    public SwitchWrapper VCFundsTransfer(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {
        logger.info("start of OLAVeriflyFinancialInstitutionImpl.VCFundsTransfer() :: " + new Date());
        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        OLAInfo olaInfo = new OLAInfo();

        Long debitCollectionBLB = PoolAccountConstantsInterface.VIRTUAL_PREPAID_CARD_INTERNAL_BLB;
        Long debitCollectionGL = PoolAccountConstantsInterface.VIRTUAL_PREPAID_CARD_INTERNAL_GL;
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);

        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.VC_TRANSFER,
                TransactionTypeConstantsInterface.OLA_DEBIT,
                debitCollectionBLB,//blb
                debitCollectionGL,//gl
                switchWrapper.getWorkFlowWrapper().getTransactionAmount()
        );
        debitList.add(olaInfo);


        olaInfo = new OLAInfo();

        Long creditInternalBLB = PoolAccountConstantsInterface.VIRTUAL_PREPAID_CARD_COLLECTION_BLB;
        Long creditInternalGL = PoolAccountConstantsInterface.VIRTUAL_PREPAID_CARD_COLLECTION_GL;
        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.VC_TRANSFER,
                TransactionTypeConstantsInterface.OLA_CREDIT,
                creditInternalBLB,//blb
                creditInternalGL,//gl
                switchWrapper.getWorkFlowWrapper().getTransactionAmount()
        );
        creditList.add(olaInfo);

        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        return switchWrapper;
    }

    public SwitchWrapper reverseWalletToWalletTransactionTransferFunds(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {
        logger.info("start of OLAVeriflyFinancialInstitutionImpl.reverseWalletToWalletTransactionTransferFunds() :: " + new Date());
        OLAVO olaVO = new OLAVO();
        List<OLAInfo> debitList = new ArrayList<OLAInfo>();
        List<OLAInfo> creditList = new ArrayList<OLAInfo>();

        long productId = ProductConstantsInterface.ACT_TO_ACT_CI;
        Boolean inclChargesCheck = switchWrapper.getWorkFlowWrapper().getProductModel().getInclChargesCheck();
        Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getInclusiveCharges());

        OLAInfo olaInfo = new OLAInfo();

        if (inclChargesCheck != null && inclChargesCheck && totalInclusiveCharges > 0.0) {
            //Dr Inclusive Charges Sundry A/C
            olaInfo = getOLAInfoForInclCharges(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                    ReasonConstants.ACCOUNT_TO_ACCOUNT_CI,
                    productId,
                    totalInclusiveCharges
            );

            debitList.add(olaInfo);

            totalInclusiveCharges = 0.0;
        }

        switchWrapper.setInclusiveChargesApplied(totalInclusiveCharges);

        Long olaAccountID = PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_OLA_ACCOUNT_ID;
        Long ofSettlementAccountID = PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID;

        olaInfo = getStakeholderOLAInfo(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode(),
                ReasonConstants.ACCOUNT_TO_ACCOUNT_CI,
                TransactionTypeConstantsInterface.OLA_DEBIT,
                olaAccountID,
                ofSettlementAccountID,
                switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() + totalInclusiveCharges
        );
        debitList.add(olaInfo);

        SmartMoneyAccountModel senderCustomerSMA = switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel();
        AccountInfoModel senderCustomerAccountInfo = getAccountInfoModelBySmartMoneyAccount(senderCustomerSMA, switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerId(), switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

        olaInfo = new OLAInfo();
        olaInfo.setIsAgent(false);
        olaInfo.setBalanceAfterTrxRequired(true);
        olaInfo.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        olaInfo.setCustomerAccountTypeId(switchWrapper.getWorkFlowWrapper().getCustomerModel().getCustomerAccountTypeId());
        olaInfo.setReasonId(ReasonConstants.ACCOUNT_TO_ACCOUNT_CI);
        olaInfo.setTransactionTypeId(TransactionTypeConstantsInterface.OLA_CREDIT);
        olaInfo.setPayingAccNo(senderCustomerAccountInfo.getAccountNo());
        olaInfo.setBalance(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().getTransactionAmount() - totalInclusiveCharges)));

        switchWrapper.setFromAccountNo(senderCustomerAccountInfo.getAccountNo());

        creditList.add(olaInfo);

        prepareCommissionCreditList(switchWrapper, creditList, ReasonConstants.ACCOUNT_TO_ACCOUNT_CI);
//        if (settleCommission) {
//            prepareCommissionCreditList(switchWrapper, creditList, reasonId);
//        }

        olaVO.setDebitAccountList(debitList);
        olaVO.setCreditAccountList(creditList);

        switchWrapper.setOlavo(olaVO);

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        return switchWrapper;
    }

    public AppUserManager getAppUserManager() {
        return appUserManager;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setVeriflyManager(VeriflyManager veriflyManager) {
        this.veriflyManager = veriflyManager;
    }

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }

}