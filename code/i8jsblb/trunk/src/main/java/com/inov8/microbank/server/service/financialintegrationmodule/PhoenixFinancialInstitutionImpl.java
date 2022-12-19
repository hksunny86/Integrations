package com.inov8.microbank.server.service.financialintegrationmodule;

/**
 * Project Name: 			Microbank
 *
 * @author Jawwad Farooq
 * Creation Date: 			February 2008
 * Description:
 */

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ImplementationNotSupportedException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AccountTypeModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CurrencyCodeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.ISOAccountTypeConstants;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SwitchConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.accounttypemodule.AccountTypeManager;
import com.inov8.microbank.server.service.currencycodemodule.CurrencyCodeManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchModuleManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.IntegrationConstants;
import com.inov8.microbank.server.service.switchmodule.iris.IntegrationConstants.PhoenixDeliveryChannelStatusCodes;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;


public class PhoenixFinancialInstitutionImpl extends VeriflyFinancialInstitutionImpl {
    private SwitchModuleManager switchModuleManager;
    private AccountTypeManager accountTypeManager;
    private CurrencyCodeManager currencyCodeManager;
    private StakeholderBankInfoManager stakeholderBankInfoManager;

    protected final Log logger = LogFactory.getLog(PhoenixFinancialInstitutionImpl.class);

    public PhoenixFinancialInstitutionImpl() {
        super();
    }

    public PhoenixFinancialInstitutionImpl(SwitchController switchController, GenericDao genericDao) {
        super(switchController, genericDao);
    }

    public SwitchWrapper checkBalanceForAccountVerification(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {

//		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
//		SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)switchWrapper.getBasePersistableModel();
//		
//		AccountInfoModel accountInfoModel = new AccountInfoModel();
//		accountInfoModel.setCustomerId(appUserModel.getCustomerId());
//		accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
//
//		LogModel logModel = new LogModel();
//		logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
//		logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
//
//		VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
//		
//		veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
//		veriflyBaseWrapper.setLogModel(logModel);
//		
//		veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
//		
//		veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
//		boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();


/*********************************/
//			switchWrapper.setAccountInfoModel(accountInfoModel);
//
//            switchWrapper.setBankId( smartMoneyAccountModel.getBankId() );
//            switchWrapper.setPaymentModeId( smartMoneyAccountModel.getPaymentModeId() );
//            switchWrapper.putObject(CommandFieldConstants.KEY_CNIC,ThreadLocalAppUser.getAppUserModel().getNic());
/******************************************/

        if (switchWrapper.getWorkFlowWrapper() != null) {
            switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
        }

        try {
            switchWrapper = this.switchController.checkBalance(switchWrapper);
        } catch (Exception ex) {
            if (ex instanceof WorkFlowException) {
                if (switchWrapper.getIntegrationMessageVO() != null) {
                    PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) switchWrapper.getIntegrationMessageVO();
                    if (phoenixIntegrationMessageVO.getResponseCode() != null && !phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {
                        throw new Exception(WorkFlowErrorCodeConstants.PHOENIX_CUSTOMER_ACC_NOT_EXIST);
                    } else
                        throw ex;
                } else
                    throw ex;
            } else
                throw ex;
        }


        return switchWrapper;

    }


    @Override
    public SwitchWrapper debitAccount(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {

        AppUserModel appUserModel = switchWrapper.getWorkFlowWrapper().getAppUserModel();

        SmartMoneyAccountModel smartMoneyAccountModel = switchWrapper.getSmartMoneyAccountModel();

        AccountInfoModel accountInfoModel = switchWrapper.getWorkFlowWrapper().getAccountInfoModel();
		/*
		if(accountInfoModel == null) {
			accountInfoModel = new AccountInfoModel() ;
		}
		
		accountInfoModel.setOldPin(switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
				
		if( appUserModel.getCustomerId() != null ) {
			accountInfoModel.setCustomerId(appUserModel.getCustomerId());
		} else {
			accountInfoModel.setCustomerId(appUserModel.getAppUserId());
		}
		
		accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
		*/
        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
        logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);

        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

        veriflyBaseWrapper = this.verifyCredentials(veriflyBaseWrapper);

        if (veriflyBaseWrapper.isErrorStatus()) {

            accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();

            switchWrapper.setAccountInfoModel(accountInfoModel);
            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);//TODO:

            if (switchWrapper.getWorkFlowWrapper() != null) {
                switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
                switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
                switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(StringUtil.replaceString(accountInfoModel.getAccountNo(), 5, "*"));
            }

            MiddlewareMessageVO integrationMessageVO = new MiddlewareMessageVO();
            integrationMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            integrationMessageVO.setAccountNo1(switchWrapper.getFromAccountNo());
            integrationMessageVO.setAccountNo2(switchWrapper.getToAccountNo());
            integrationMessageVO.setTransactionAmount(String.valueOf(switchWrapper.getTransactionAmount()));

            switchWrapper.setMiddlewareIntegrationMessageVO(integrationMessageVO);


            switchWrapper = this.switchController.debitAccount(switchWrapper);
            switchWrapper.getWorkFlowWrapper().setTransactionModel(switchWrapper.getTransactionTransactionModel());
            switchWrapper.getWorkFlowWrapper().setFirstFTIntegrationVO(switchWrapper.getIntegrationMessageVO());
            switchWrapper.getWorkFlowWrapper().setFirstFTSwitchWrapper(switchWrapper);
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;
    }


    public SwitchWrapper creditAccountAdvice(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        AppUserModel appUserModel = switchWrapper.getWorkFlowWrapper().getAppUserModel();

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

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

            veriflyBaseWrapper = this.verifyCredentials(veriflyBaseWrapper);
			
			/*if(switchWrapper.getWorkFlowWrapper().isCRetailPayment()){
				veriflyBaseWrapper =  this.verifyCredentials(veriflyBaseWrapper);
			}else{
				veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
			}*/

            errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();
        }

        if (errorMessagesFlag) {

            //---------------------------For Pool Accounts

            //get the pool account
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
            stakeholderBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.AGENT_POOL_ACCOUNT_ID);
            searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
            stakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();


            //------------------------------------------------
            if (false == isThreadLocalAccountInfoLoaded) {
                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
                setThreadLocalAccountInfoModel(switchWrapper, accountInfoModel);//added by mudassir - place AccountinfoModel in threadLocal for reducing verifly calls
            }

            switchWrapper.setAccountInfoModel(accountInfoModel);

            long productId = switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().longValue();
            if (ProductConstantsInterface.BB_TO_CORE_ACCOUNT == productId
                    || ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT == productId
                    || ProductConstantsInterface.CUSTOMER_BB_TO_CORE_ACCOUNT == productId
                    || ProductConstantsInterface.CUSTOMER_BB_TO_IBFT == productId
                    || ProductConstantsInterface.AGENT_BB_TO_IBFT == productId
                    || ProductConstantsInterface.RELIEF_FUND_PRODUCT == productId) {
                switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
                switchWrapper.setToAccountNo(switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().getCustomField11());// "11120000000003");

                double inclusiveChargesApplied = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getInclusiveChargesApplied());
                switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() - inclusiveChargesApplied)));

                logger.info("[PhoenixFinancialInstitutionImpl.creditAccountAdvice] doing CreditAdvice for productId:" + productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            }

            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());

            if (switchWrapper.getWorkFlowWrapper() != null) {
                switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
                switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
                switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(accountInfoModel.getAccountNo());
            }

            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);//TODO:

            MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
            middlewareMessageVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
            middlewareMessageVO.setAccountNo1(switchWrapper.getFromAccountNo());    //must core account
            middlewareMessageVO.setAccountNo2(switchWrapper.getToAccountNo());        // bb account
            middlewareMessageVO.setTransactionAmount(switchWrapper.getTransactionAmount().toString());
            middlewareMessageVO.setTransmissionTime(new Date());
            switchWrapper.setMiddlewareIntegrationMessageVO(middlewareMessageVO);

            switchWrapper = this.switchController.creditAccountAdvice(switchWrapper);
            switchWrapper.getWorkFlowWrapper().setTransactionModel(switchWrapper.getTransactionTransactionModel());
            switchWrapper.getWorkFlowWrapper().setFirstFTIntegrationVO(switchWrapper.getIntegrationMessageVO());
            switchWrapper.getWorkFlowWrapper().setFirstFTSwitchWrapper(switchWrapper);
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;
    }


    public SwitchWrapper debitCreditAccount(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {

        switchWrapper.setPaymentModeId(SwitchConstants.CORE_BANKING_SWITCH);
        this.switchController.debitCreditAccount(switchWrapper);

        logger.info(switchWrapper.getAgentBalance());

        return switchWrapper;
    }


    private SwitchWrapper makeCashToCashTransactions(SwitchWrapper switchWrapper, AccountInfoModel accountInfoModel) throws FrameworkCheckedException, Exception {


        //get cash to cash Sundry Account
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel sundaryStakeholderBankInfoModel = new StakeholderBankInfoModel();
        sundaryStakeholderBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.FUND_TRANSFER_SUNDRY_CORE_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(sundaryStakeholderBankInfoModel);
        sundaryStakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

        if (switchWrapper.isCashToCashLeg2()) {// Leg 2 execution

            switchWrapper.setFromAccountNo(sundaryStakeholderBankInfoModel.getAccountNo());//debit sundry account
            switchWrapper.setToAccountNo(accountInfoModel.getAccountNo());//credit agent account
            switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount())));

            logger.info("[PhoenixFinancialInstitutionImpl.debitCreditAccount] Going to make FT from Sundry A/C to Agent A/C. AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

            switchWrapper = makeC2CFT(switchWrapper, accountInfoModel);

        } else { // Leg 1 :- Make Two FTs.
            //FT#1
            switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());//agent
            switchWrapper.setToAccountNo(sundaryStakeholderBankInfoModel.getAccountNo());//cash to cash sundry
            switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));

            TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel();

            logger.info("[PhoenixFinancialInstitutionImpl.debitCreditAccount] Going to make FT from Agent A/C --> C2C Sundry A/C. logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

            switchWrapper = makeC2CFT(switchWrapper, accountInfoModel);


            //FT # 2 //REMOVED -  THIS FT IS DONE THROUGH SCHEDULER NOW..
			/*StakeholderBankInfoModel commissionReconBankInfoModel = new StakeholderBankInfoModel();
			commissionReconBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_ACCOUNT_ID);
			searchBaseWrapper.setBasePersistableModel(commissionReconBankInfoModel);
			commissionReconBankInfoModel = (StakeholderBankInfoModel)stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
			
			switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModelTemp);

			switchWrapper.setFromAccountNo(sundaryStakeholderBankInfoModel.getAccountNo());///cash to cash sundry   //accountInfoModel.getAccountNo());
			switchWrapper.setToAccountNo(commissionReconBankInfoModel.getAccountNo());
//			trx amount = Total charges - (Agent 1 commission + Agent 2 commission + franchise 2 commission) - bug 992 - subtract franchise 2 comm. from charges also.
			switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - (switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent2CommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFranchise2CommissionAmount()))));
			
			switchWrapper = makeC2CFT(switchWrapper, accountInfoModel);*/


        }

        //set VO for handling rollbacks of FT in transaction.rollback().
        switchWrapper.getWorkFlowWrapper().setFirstFTIntegrationVO(switchWrapper.getIntegrationMessageVO());
        switchWrapper.getWorkFlowWrapper().setFirstFTSwitchWrapper(switchWrapper);

//		
        return switchWrapper;

    }

    private SwitchWrapper makeC2CFT(SwitchWrapper switchWrapper, AccountInfoModel accountInfoModel) throws FrameworkCheckedException, Exception {

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(StringUtil.replaceString(accountInfoModel.getAccountNo(), 5, "*"));

        switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());

        if (switchWrapper.getWorkFlowWrapper() != null) {
            switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
            switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
            switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(StringUtil.replaceString(accountInfoModel.getAccountNo(), 5, "*"));
        }

        switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
        switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());

        switchWrapper = this.switchController.debitCreditAccount(switchWrapper);

        return switchWrapper;

    }

    public SwitchWrapper pushBillPayment(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

        return this.switchController.pushBillPayment(switchWrapper);
    }

    public SwitchWrapper billPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {

        Long ACTION_LOG_ID = (Long) switchWrapper.getWorkFlowWrapper().getObject("ACTION_LOG_ID");
        Long TRANSACTION_ID = (Long) switchWrapper.getWorkFlowWrapper().getObject("TRANSACTION_ID");

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setTransactionId(TRANSACTION_ID);

        ProductModel productModel = super.genericDao.getEntityByPrimaryKey(switchWrapper.getWorkFlowWrapper().getProductModel());

        transactionModel = super.genericDao.getEntityByPrimaryKey(transactionModel);

        ThreadLocalActionLog.setActionLogId(ACTION_LOG_ID);

        switchWrapper.getWorkFlowWrapper().setProductModel(productModel);
        switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModel);
        switchWrapper.getWorkFlowWrapper().setTransactionCodeModel(transactionModel.getTransactionCodeIdTransactionCodeModel());
        switchWrapper.setAmountPaid(switchWrapper.getMiddlewareIntegrationMessageVO().getTransactionAmount());
        switchWrapper.setConsumerNumber(switchWrapper.getMiddlewareIntegrationMessageVO().getConsumerNo());

        return this.switchController.billPayment(switchWrapper);
    }


    public SwitchWrapper billPaymentViaNADRA(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {

        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

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

        if (errorMessagesFlag) {

            //get the pool account
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
            stakeholderBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
            searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);
            stakeholderBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

            // get utility bill pool account to be sent with advice


            //------------------------------------------------
            if (false == isThreadLocalAccountInfoLoaded) {
                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
                setThreadLocalAccountInfoModel(switchWrapper, accountInfoModel);//added by mudassir - place AccountinfoModel in threadLocal for reducing verifly calls
            }

            switchWrapper.setAccountInfoModel(accountInfoModel);


            if (ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
                switchWrapper.setFromAccountNo(stakeholderBankInfoModel.getAccountNo());//get the pool account
                switchWrapper.setAmountPaid(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount()));

            } else {
                StakeholderBankInfoModel utilityBillStakeholderBankInfo = new StakeholderBankInfoModel();
                if (ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER) {
                    SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
                    utilityBillStakeholderBankInfo.setPrimaryKey(PoolAccountConstantsInterface.UTILITY_BILL_NADRA_POOL_ACCOUNT_ID);
                    sBaseWrapper.setBasePersistableModel(utilityBillStakeholderBankInfo);
                    utilityBillStakeholderBankInfo = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(sBaseWrapper).getBasePersistableModel();
                    switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
                    switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
                    switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
                    switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
                    switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
                    switchWrapper.setTransactionAmount(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
                    switchWrapper.setToAccountNo(utilityBillStakeholderBankInfo.getAccountNo());

                    logger.info("[PhoenixFinancialInstitution.billPaymentViaNADRA] Going to Make FT from AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
                            " A/C to Utility Bill Pool A/C." +
                            " Trx ID:" + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

                    switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
                    WorkFlowWrapper wrapper = switchWrapper.getWorkFlowWrapper();
                    SwitchWrapper ftSwitchWrappper = new SwitchWrapperImpl();
                    ftSwitchWrappper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
                    ftSwitchWrappper.setFromAccountNo(accountInfoModel.getAccountNo());
                    ftSwitchWrappper.setBankId(smartMoneyAccountModel.getBankId());
                    ftSwitchWrappper.setIntegrationMessageVO(switchWrapper.getIntegrationMessageVO());
                    ftSwitchWrappper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
                    ftSwitchWrappper.setTransactionAmount(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount());
                    ftSwitchWrappper.setToAccountNo(utilityBillStakeholderBankInfo.getAccountNo());
                    ftSwitchWrappper.setAccountInfoModel(accountInfoModel);
                    ftSwitchWrappper.setWorkFlowWrapper(wrapper);
                    wrapper.setFirstFTIntegrationVO(switchWrapper.getIntegrationMessageVO());
                    wrapper.setFirstFTSwitchWrapper(ftSwitchWrappper);
                    switchWrapper.setWorkFlowWrapper(wrapper);
//						if(1==1)
//						{
//							throw new Exception("shit");
//						}
                }

                switchWrapper.setFromAccountNo(utilityBillStakeholderBankInfo.getAccountNo()); //Utility Bill Payment Pool Account
                switchWrapper.setToAccountNo(null);
                switchWrapper.setAmountPaid(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount()));
            }

            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
            switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
            if (switchWrapper.getWorkFlowWrapper() != null) {
                switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
            }
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);//TODO:
            switchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());

            logger.info("[PhoenixFinancialInstitution.billPaymentViaNADRA] Going to Pay Bill from Utility Bill NADRA Pool A/C. Logged In AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
                    " Consumer No:" + switchWrapper.getConsumerNumber() +
                    " Trx ID:" + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());

            switchWrapper = this.switchController.billPaymentViaNADRA(switchWrapper);
            WorkFlowWrapper wrapper = switchWrapper.getWorkFlowWrapper();
            wrapper.setBillPaymentIntegrationVO(switchWrapper.getIntegrationMessageVO());
            wrapper.setBillPaymentSwitchWrapper(switchWrapper);


        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;

    }


    @Override
    public SwitchWrapper titleFetch(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

        return this.switchController.titleFetch(switchWrapper);
    }


    public SwitchWrapper accountBalanceInquiry(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

        this.switchController.checkBalance(switchWrapper);

        logger.info(switchWrapper.getAgentBalance());

        return switchWrapper;
    }


    public SwitchWrapper checkBalance(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {

//		if(switchWrapper.getWorkFlowWrapper() != null)
//		{
//			if(switchWrapper.getWorkFlowWrapper().getMPin() != null) 
//			{
//				
//			}			
//		}		

        if (DeviceTypeConstantsInterface.ALL_PAY.longValue() == switchWrapper.getWorkFlowWrapper().getDeviceTypeModel().getDeviceTypeId().longValue()) {

            return accountBalanceInquiry(switchWrapper);
        }

        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

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


            if (appUserModel.getCustomerId() != null)
                accountInfoModel.setCustomerId(appUserModel.getCustomerId());
            else
                accountInfoModel.setCustomerId(appUserModel.getAppUserId());

            accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
            String pin = (String) switchWrapper.getObject(CommandFieldConstants.KEY_PIN);
            accountInfoModel.setOldPin(pin);

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

            if (false == isThreadLocalAccountInfoLoaded) {
                accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
                setThreadLocalAccountInfoModel(switchWrapper, accountInfoModel);//added by mudassir - place AccountinfoModel in threadLocal for reducing verifly calls
            }

//			accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();

            switchWrapper.setAccountInfoModel(accountInfoModel);

            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT); //TODO:comment this after payment mode thing
            switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);

            switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());

            if (switchWrapper.getWorkFlowWrapper() != null) {
                switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
            }

            logger.info("[PhoenixFinancialInstitution.checkBalance] Logged in AppUserID:" + appUserModel.getAppUserId() + " Trx ID:" + (switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() == null ? " is NULL. " : switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode()) + " Agent SmartMoneyAccountId: " + (switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel() == null ? " is NULL" : switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getSmartMoneyAccountId()));
            switchWrapper = this.switchController.checkBalance(switchWrapper);
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;

    }

    public boolean isVeriflyRequired() throws FrameworkCheckedException {
        return true;
    }

    public SwitchWrapper transaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {
        AccountInfoModel accountInfoModel = new AccountInfoModel();

        if (ThreadLocalAppUser.getAppUserModel().getCustomerId() != null)
            accountInfoModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
        else
            accountInfoModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getAppUserId());

        accountInfoModel.setAccountNick(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getName());

        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
        logModel.setTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionCodeId());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel());

        veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
        boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

        if (errorMessagesFlag) {
            accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();

            switchWrapper.setAccountInfoModel(accountInfoModel);
            if (accountInfoModel.getAccountNo() != null)
                switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(StringUtil.replaceString(accountInfoModel.getAccountNo(), 5, "*"));
            switchWrapper.setBankId(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getBankId());
            switchWrapper.setPaymentModeId(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getPaymentModeId());
            switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
            switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());

            switchWrapper = loadSwitch(switchWrapper);

            switchWrapper = this.switchController.transaction(switchWrapper, null);
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }

        return switchWrapper;
    }


    @Override
    public VeriflyBaseWrapper generatePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        SwitchWrapper switchWrapper = (SwitchWrapper) veriflyBaseWrapper.getObject(SwitchConstants.KEY_SWITCHWRAPPER);

        if (veriflyBaseWrapper != null) {
            if (veriflyBaseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID) != null) {
                Long deviceTypeId = (Long) veriflyBaseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID);

                if (deviceTypeId.longValue() == DeviceTypeConstantsInterface.MOBILE.longValue() ||
                        deviceTypeId.longValue() == DeviceTypeConstantsInterface.MFS_WEB.longValue()) {
                    switchWrapper = loadSwitch(switchWrapper);
                    switchWrapper = switchController.generatePIN(switchWrapper);
                    veriflyBaseWrapper.putObject(SwitchConstants.KEY_SWITCHWRAPPER, switchWrapper);
                } else if (deviceTypeId.longValue() == DeviceTypeConstantsInterface.WEB.longValue()) {
                    veriflyBaseWrapper = super.generatePin(veriflyBaseWrapper);
                }
            }
        }

        return veriflyBaseWrapper;
    }

    @Override
    public VeriflyBaseWrapper resetPin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("Method Not Supported....");
    }

    @Override
    public VeriflyBaseWrapper changePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        // TODO Implement the logic on Device_Type_Id ... send request through Portals


        if (veriflyBaseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID) != null) {
            Long deviceTypeId = (Long) veriflyBaseWrapper.getObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID);

            if (deviceTypeId.longValue() == DeviceTypeConstantsInterface.MOBILE.longValue() ||
                    deviceTypeId.longValue() == DeviceTypeConstantsInterface.MFS_WEB.longValue() ||
                    deviceTypeId.longValue() == DeviceTypeConstantsInterface.ALL_PAY.longValue()) {
                SwitchWrapper switchWrapper = (SwitchWrapper) veriflyBaseWrapper.getObject(SwitchConstants.KEY_SWITCHWRAPPER);

                veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
                boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

                if (errorMessagesFlag) {

                    switchWrapper = loadSwitch(switchWrapper);
                    switchWrapper = switchController.changePIN(switchWrapper);
                    veriflyBaseWrapper.putObject(SwitchConstants.KEY_SWITCHWRAPPER, switchWrapper);

                } else {
                    String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
                    throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }


            } else if (deviceTypeId.longValue() == DeviceTypeConstantsInterface.WEB.longValue()) {
                String NIC = (String) veriflyBaseWrapper.getObject(CommandFieldConstants.KEY_CNIC);
                String accountTypeId = (String) veriflyBaseWrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_TYPE);

                boolean response = this.activateDeliveryChannel(accountTypeId, NIC);
                veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_RESPONSE, response);
            }
        }

        return veriflyBaseWrapper;
    }

    private SwitchWrapper loadSwitch(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
        switchWrapper = this.switchModuleManager.getSwitchClassPath(switchWrapper);
        if (switchWrapper.getSwitchSwitchModel() == null && switchWrapper.getSwitchSwitchModel().getPaymentGatewayCode() == null
                && switchWrapper.getSwitchSwitchModel().getPaymentGatewayCode().equals("")) {
            throw new FrameworkCheckedException("Payment Gateway Code is null or Empty..");
        }
        return switchWrapper;
    }

    public void setSwitchModuleManager(SwitchModuleManager switchModuleManager) {
        this.switchModuleManager = switchModuleManager;
    }

    @Override
    public String getAccountTitle(String accountNo, String accountType, String currency, String NIC) throws Exception {

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        CustomerAccount customerAccount = new CustomerAccount();

        switchWrapper.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, NIC);

        accountType = this.getAccountTypeCode(Long.parseLong(accountType));

        if (ISOAccountTypeConstants.CREDIT_CARD.equalsIgnoreCase(accountType)) {
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CREDIT_CARD);
        } else {
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        }

        customerAccount.setNumber(accountNo);
        customerAccount.setType(accountType);
        getCurrencyCode(currency);
        customerAccount.setCurrency(this.getCurrencyCode(currency));

        switchWrapper.setCustomerAccount(customerAccount);

        switchWrapper = loadSwitch(switchWrapper);
        try {
            switchWrapper = this.switchController.fetchTitle(switchWrapper);
        } catch (Exception ex) {
            if (ex instanceof WorkFlowException) {
                if (switchWrapper.getIntegrationMessageVO() != null) {
                    PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) switchWrapper.getIntegrationMessageVO();
                    if (phoenixIntegrationMessageVO.getResponseCode() != null && !phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {
                        throw new Exception(WorkFlowErrorCodeConstants.PHOENIX_INVALID_CUSTOMER_PROFILE);
                    } else
                        throw ex;
                } else
                    throw ex;
            } else
                throw ex;
        }


        return switchWrapper.getCustomerAccount().getTitleOfTheAccount();
    }

    @Override
    public boolean isIVRChannelActive(String accountNo, String accountType, String currency, String NIC) throws Exception {

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();

        switchWrapper.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, NIC);

        accountType = this.getAccountTypeCode(Long.parseLong(accountType));

        if (ISOAccountTypeConstants.CREDIT_CARD.equalsIgnoreCase(accountType)) {
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CREDIT_CARD);
        } else {
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        }

        switchWrapper = loadSwitch(switchWrapper);
        try {
            switchWrapper = this.switchController.customerProfile(switchWrapper);
        } catch (Exception ex) {
            if (ex instanceof WorkFlowException) {
                if (switchWrapper.getIntegrationMessageVO() != null) {
                    PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) switchWrapper
                            .getIntegrationMessageVO();
                    if (phoenixIntegrationMessageVO.getResponseCode() != null
                            && !phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
                            IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {
                        throw new Exception(WorkFlowErrorCodeConstants.PHOENIX_CUSTOMER_PROF_NOT_EXIST);
                    } else
                        throw ex;
                } else
                    throw ex;
            } else
                throw ex;
        }

        IntegrationMessageVO integrationMessageVO = switchWrapper.getIntegrationMessageVO();

        if (integrationMessageVO.getIvrChannelStatus().equalsIgnoreCase(IntegrationConstants.PhoenixDeliveryChannelStatusCodes.OK.getDeliveryChannelStatusCodeValue()))
            return true;
        else {
            throw new Exception(WorkFlowErrorCodeConstants.PHOENIX_INVALID_IVR_RESP);
            //return false;
        }


    }

    public boolean isMobileChannelActive(String accountType, String NIC) throws Exception {

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();


        switchWrapper.setBankId(ThreadLocalAppUser.getAppUserModel().getBankUserIdBankUserModel().getBankId());

        if (ISOAccountTypeConstants.CREDIT_CARD.equalsIgnoreCase(accountType)) {
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CREDIT_CARD);
        } else {
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        }

        switchWrapper = loadSwitch(switchWrapper);
        switchWrapper = this.switchController.customerProfile(switchWrapper);

        IntegrationMessageVO integrationMessageVO = switchWrapper.getIntegrationMessageVO();

        if (integrationMessageVO.getMobileChannelStatus().equalsIgnoreCase(PhoenixDeliveryChannelStatusCodes.OK.getDeliveryChannelStatusCodeValue())) {
            return true;
        }

        return false;

//		return true;
    }

    public boolean isMobileChannelFirstTimeLogin(String accountType, String NIC) throws Exception {

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();

        switchWrapper.setBankId(ThreadLocalAppUser.getAppUserModel().getBankUserIdBankUserModel().getBankId());

        if (ISOAccountTypeConstants.CREDIT_CARD.equalsIgnoreCase(accountType)) {
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CREDIT_CARD);
        } else {
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        }

        switchWrapper = loadSwitch(switchWrapper);
        switchWrapper = this.switchController.customerProfile(switchWrapper);

        IntegrationMessageVO integrationMessageVO = switchWrapper.getIntegrationMessageVO();

        if (integrationMessageVO.getMobileChannelStatus().equalsIgnoreCase(PhoenixDeliveryChannelStatusCodes.FIRST_TIME_LOGIN.getDeliveryChannelStatusCodeValue())) {
            return true;
        }

        return false;

    }

    public boolean activateDeliveryChannel(String accountType, String NIC) throws Exception {
        try {
            SwitchWrapper switchWrapper = new SwitchWrapperImpl();

            switchWrapper.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
            switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, NIC);

            accountType = this.getAccountTypeCode(Long.parseLong(accountType));

            if (ISOAccountTypeConstants.CREDIT_CARD.equalsIgnoreCase(accountType)) {
                switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CREDIT_CARD);
            } else {
                switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            }

            //TODO Check what would be return with IntegrationConstants.PhoenixDeliveryChannelStatusCodes.FIRST_TIME_LOGIN
            switchWrapper.putObject(CommandFieldConstants.KEY_DELIVERY_CHNL_STATUS,
                    IntegrationConstants.PhoenixDeliveryChannelStatusCodes.FIRST_TIME_LOGIN.getDeliveryChannelStatusCodeValue());

            switchWrapper = loadSwitch(switchWrapper);
            try {
                switchWrapper = this.switchController.changeDeliveryChannel(switchWrapper);
            } catch (Exception ex) {
                if (ex instanceof WorkFlowException) {
                    if (switchWrapper.getIntegrationMessageVO() != null) {
                        PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) switchWrapper
                                .getIntegrationMessageVO();
                        if (phoenixIntegrationMessageVO.getResponseCode() != null
                                && !phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
                                IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {
                            throw new Exception(WorkFlowErrorCodeConstants.PHOENIX_ACT_CHANNEL_REQ_FAILED);
                        } else
                            throw ex;
                    } else
                        throw ex;
                } else
                    throw ex;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new WorkFlowException(e.getMessage());
        }
    }

    public boolean deActivateDeliveryChannel(String accountType, String NIC) throws Exception {
        try {
            SwitchWrapper switchWrapper = new SwitchWrapperImpl();

            switchWrapper.setBankId(UserUtils.getCurrentUser().getBankUserIdBankUserModel().getBankId());
            switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, NIC);

            accountType = this.getAccountTypeCode(Long.parseLong(accountType));

            if (ISOAccountTypeConstants.CREDIT_CARD.equalsIgnoreCase(accountType)) {
                switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CREDIT_CARD);
            } else {
                switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            }

            switchWrapper.putObject(CommandFieldConstants.KEY_DELIVERY_CHNL_STATUS,
                    IntegrationConstants.PhoenixDeliveryChannelStatusCodes.WARM.getDeliveryChannelStatusCodeValue());

            switchWrapper = loadSwitch(switchWrapper);
            try {
                switchWrapper = this.switchController.changeDeliveryChannel(switchWrapper);
            } catch (Exception ex) {
                if (ex instanceof WorkFlowException) {
                    if (switchWrapper.getIntegrationMessageVO() != null) {
                        PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = (PhoenixIntegrationMessageVO) switchWrapper
                                .getIntegrationMessageVO();
                        if (phoenixIntegrationMessageVO.getResponseCode() != null
                                && !phoenixIntegrationMessageVO.getResponseCode().equalsIgnoreCase(
                                IntegrationConstants.PhoenixResponseCodes.OK.getResponseCodeValue())) {
                            throw new Exception(WorkFlowErrorCodeConstants.PHOENIX_DEACT_CHANNEL_REQ_FAILED);
                        } else
                            throw ex;
                    } else
                        throw ex;
                } else
                    throw ex;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new WorkFlowException(e.getMessage());
        }
    }

    @Override
    public SwitchWrapper miniStatement(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

//		if(switchWrapper.getWorkFlowWrapper() != null)
//		{
//			if(switchWrapper.getWorkFlowWrapper().getMPin() != null) 
//			{
//			}			
//		}		

        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        AccountInfoModel accountInfoModel = new AccountInfoModel();
        accountInfoModel.setCustomerId(appUserModel.getAppUserId());
        accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
        accountInfoModel.setOldPin((String) switchWrapper.getObject(CommandFieldConstants.KEY_PIN));


        LogModel logModel = new LogModel();
        logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
        logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());

        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();

        veriflyBaseWrapper.setAccountInfoModel(accountInfoModel);
        veriflyBaseWrapper.setLogModel(logModel);

        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

        veriflyBaseWrapper = this.verifyPin(veriflyBaseWrapper);
        boolean errorMessagesFlag = veriflyBaseWrapper.isErrorStatus();

        if (errorMessagesFlag) {

            accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();

            switchWrapper.setAccountInfoModel(accountInfoModel);
            switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT); //TODO:comment this after payment mode thing
            switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);

            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());

            if (switchWrapper.getWorkFlowWrapper() != null) {
                switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
            }

            switchWrapper = this.switchController.miniStatement(switchWrapper);
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;

    }


    public VeriflyBaseWrapper verifyPin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception {
        VeriflyBaseWrapper localVeriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        //localVeriflyBaseWrapper.setBasePersistableModel( veriflyBaseWrapper.getBasePersistableModel() ) ;
        localVeriflyBaseWrapper.setAccountInfoModel(veriflyBaseWrapper.getAccountInfoModel());
        localVeriflyBaseWrapper.setLogModel(veriflyBaseWrapper.getLogModel());

        VeriflyManager veriflyManager = loadVeriflyManager(veriflyBaseWrapper);

        //SwitchWrapper switchWrapper = (SwitchWrapper)veriflyBaseWrapper.getObject(SwitchConstants.KEY_SWITCHWRAPPER);
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) veriflyBaseWrapper.getBasePersistableModel();
        veriflyBaseWrapper.setBasePersistableModel(null);
        veriflyBaseWrapper = veriflyManager.verifyPIN(localVeriflyBaseWrapper);
        veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
        return veriflyBaseWrapper;
    }

    private VeriflyManager loadVeriflyManager(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException {
        VeriflyManager veriflyManager = null;
        if (veriflyBaseWrapper.getBasePersistableModel() instanceof BankModel) {
            veriflyManager = this.veriflyManagerService.getVeriflyMgrByBankId((BankModel) veriflyBaseWrapper.getBasePersistableModel());
        } else if (veriflyBaseWrapper.getBasePersistableModel() instanceof SmartMoneyAccountModel) {
            veriflyManager = this.veriflyManagerService.getVeriflyMgrByAccountId((SmartMoneyAccountModel) veriflyBaseWrapper.getBasePersistableModel());
        } else {
            throw new FrameworkCheckedException("Invalid Identifier...");
        }
        return veriflyManager;
    }

    private String getAccountTypeCode(Long accountTypeId) {
        AccountTypeModel accountTypeModel = new AccountTypeModel();
        BaseWrapper baseWrapper = new BaseWrapperImpl();

        accountTypeModel.setAccountTypeId(accountTypeId);
        baseWrapper.setBasePersistableModel(accountTypeModel);

        try {
            accountTypeModel = (AccountTypeModel) this.accountTypeManager.loadAccountType(baseWrapper).getBasePersistableModel();
            return accountTypeModel.getAccountTypeCode();
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getCurrencyCode(String currencyCodeId) {
        CurrencyCodeModel currencyCodeModel = new CurrencyCodeModel();
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

        currencyCodeModel.setCurrencyCodeId(Long.parseLong(currencyCodeId));
        searchBaseWrapper.setBasePersistableModel(currencyCodeModel);

        try {
            currencyCodeModel = (CurrencyCodeModel) this.currencyCodeManager.loadCurrencyCode(searchBaseWrapper).getBasePersistableModel();
            return currencyCodeModel.getCurrencyCode();
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SwitchWrapper checkBalanceWithoutPin(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {
        AppUserModel appUserModel = null;
        if (switchWrapper.getWorkFlowWrapper() != null && switchWrapper.getWorkFlowWrapper().isCRetailPayment()) {//load receipeint AppUserModel in case of Cust0mer RetailPayment transaction.
            appUserModel = switchWrapper.getWorkFlowWrapper().getReceiverAppUserModel();
        } else {
            appUserModel = ThreadLocalAppUser.getAppUserModel();
        }

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

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

            if (appUserModel.getCustomerId() != null)
                accountInfoModel.setCustomerId(appUserModel.getCustomerId());
            else
                accountInfoModel.setCustomerId(appUserModel.getAppUserId());

            accountInfoModel.setAccountNick(smartMoneyAccountModel.getName());
            String pin = (String) switchWrapper.getObject(CommandFieldConstants.KEY_PIN);
            accountInfoModel.setOldPin(pin);

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

            switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
            switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT); //TODO:comment this after payment mode thing
            switchWrapper.setFromAccountType(com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
            switchWrapper.setCurrencyCode(com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
            switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());

            TransactionModel _transactionModel = null;

            if (switchWrapper.getWorkFlowWrapper() != null) {

                _transactionModel = switchWrapper.getWorkFlowWrapper().getTransactionModel();
                switchWrapper.getWorkFlowWrapper().setTransactionModel(new TransactionModel());
                switchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
            }

            logger.info("[PhoenixFinancialInstitution.checkBalanceWithoutPin] Trx ID:" + (switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() == null ? " is null. " : switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode()) + " user SmartMoneyAccountId: " + switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel().getSmartMoneyAccountId() + " AppUserID: " + appUserModel.getAppUserId());

            switchWrapper = this.switchController.checkBalance(switchWrapper);

            if (_transactionModel != null) {
                switchWrapper.getWorkFlowWrapper().setTransactionModel(_transactionModel);
            }
        } else {
            String veriflyErrorMessage = veriflyBaseWrapper.getErrorMessage();
            throw new CommandException(veriflyErrorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        return switchWrapper;
    }

    @Override
    public SwitchWrapper settleCashDepositCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        //get the pool account
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel customerPoolBankInfoModel = new StakeholderBankInfoModel();
        customerPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(customerPoolBankInfoModel);
        customerPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

        StakeholderBankInfoModel commissionReconBankInfoModel = new StakeholderBankInfoModel();
        commissionReconBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(commissionReconBankInfoModel);
        commissionReconBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

//		AccountInfoModel accountInfoModel = new AccountInfoModel();
//		accountInfoModel.setOldPin(switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());

        switchWrapper.setFromAccountNo(customerPoolBankInfoModel.getAccountNo());//TODO: get the pool account
        switchWrapper.setToAccountNo(switchWrapper.getAccountInfoModel().getAccountNo());
        switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
        switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.setToCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		/*
		switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
		*/

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
        newSwitchWrapper.setFromAccountNo(customerPoolBankInfoModel.getAccountNo());
        newSwitchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        newSwitchWrapper.setFromCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        newSwitchWrapper.setToAccountNo(commissionReconBankInfoModel.getAccountNo());
        newSwitchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        newSwitchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
        newSwitchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        newSwitchWrapper = this.switchController.debitCreditAccount(newSwitchWrapper);

        return switchWrapper;
    }

    @Override
    public SwitchWrapper settleCashWithdrawalCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        //get the pool account
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel customerPoolBankInfoModel = new StakeholderBankInfoModel();
        customerPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(customerPoolBankInfoModel);
        customerPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

        StakeholderBankInfoModel commissionReconBankInfoModel = new StakeholderBankInfoModel();
        commissionReconBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(commissionReconBankInfoModel);
        commissionReconBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

//		AccountInfoModel accountInfoModel = new AccountInfoModel();
//		accountInfoModel.setOldPin(switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());

        switchWrapper.setFromAccountNo(customerPoolBankInfoModel.getAccountNo());//TODO: get the pool account
        switchWrapper.setToAccountNo(switchWrapper.getAccountInfoModel().getAccountNo());
        switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble((switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmount() + switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getFedCommissionAmount()) - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
        switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.setToCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		/*
		switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
		*/

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
        newSwitchWrapper.setFromAccountNo(customerPoolBankInfoModel.getAccountNo());
        newSwitchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        newSwitchWrapper.setFromCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        newSwitchWrapper.setToAccountNo(commissionReconBankInfoModel.getAccountNo());
        newSwitchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        newSwitchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
        newSwitchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        newSwitchWrapper = this.switchController.debitCreditAccount(newSwitchWrapper);

        return switchWrapper;

    }

    @Override
    public SwitchWrapper settleP2PCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        //get the pool account
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        StakeholderBankInfoModel customerPoolBankInfoModel = new StakeholderBankInfoModel();
        customerPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(customerPoolBankInfoModel);
        customerPoolBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

        StakeholderBankInfoModel commissionReconBankInfoModel = new StakeholderBankInfoModel();
        commissionReconBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(commissionReconBankInfoModel);
        commissionReconBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();

//		AccountInfoModel accountInfoModel = new AccountInfoModel();
//		accountInfoModel.setOldPin(switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());

        switchWrapper.setFromAccountNo(customerPoolBankInfoModel.getAccountNo());//TODO: get the pool account
        switchWrapper.setToAccountNo(commissionReconBankInfoModel.getAccountNo());
        switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getTotalCommissionAmount())));
        switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.setToCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		/*
		switchWrapper = this.switchController.debitCreditAccount(switchWrapper);
		*/

        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
        newSwitchWrapper.setBankId(50110L);
        newSwitchWrapper.setPaymentModeId(6L);
        newSwitchWrapper.setFromAccountNo(customerPoolBankInfoModel.getAccountNo());
        newSwitchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        newSwitchWrapper.setFromCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        newSwitchWrapper.setToAccountNo(commissionReconBankInfoModel.getAccountNo());
        newSwitchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        newSwitchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount())));
        newSwitchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
        newSwitchWrapper = this.switchController.debitCreditAccount(newSwitchWrapper);

        return switchWrapper;

    }


    @Override
    public SwitchWrapper settleAgentBillPaymentCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) switchWrapper.getBasePersistableModel();

        //get the pool account
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

        StakeholderBankInfoModel commissionReconBankInfoModel = new StakeholderBankInfoModel();
        commissionReconBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.COMMISSION_RECON_ACCOUNT_ID);
        searchBaseWrapper.setBasePersistableModel(commissionReconBankInfoModel);
        commissionReconBankInfoModel = (StakeholderBankInfoModel) stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();


        SwitchWrapper newSwitchWrapper = new SwitchWrapperImpl();
        newSwitchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        newSwitchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
        newSwitchWrapper.setFromAccountNo(switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getAccountNo());
        newSwitchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        newSwitchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        newSwitchWrapper.setToAccountNo(commissionReconBankInfoModel.getAccountNo());
        newSwitchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        newSwitchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalCommissionAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));

        newSwitchWrapper = this.switchController.debitCreditAccount(newSwitchWrapper);

        return switchWrapper;
    }

    @Override
    public SwitchWrapper sendReversalAdvice(SwitchWrapper switchWrapper) throws FrameworkCheckedException, WorkFlowException {
        return this.switchController.sendReversalAdvice(switchWrapper);
    }

    @Override
    public SwitchWrapper sendCreditAdvice(SwitchWrapper switchWrapper) throws FrameworkCheckedException, WorkFlowException {
        return this.switchController.sendCreditAdvice(switchWrapper);
    }


    @Override
    public boolean isVeriflyLite() throws FrameworkCheckedException, Exception {
        return false;
    }


    public void setAccountTypeManager(AccountTypeManager accountTypeManager) {
        this.accountTypeManager = accountTypeManager;
    }


    public void setCurrencyCodeManager(CurrencyCodeManager currencyCodeManager) {
        this.currencyCodeManager = currencyCodeManager;
    }

    public void setStakeholderBankInfoManager(
            StakeholderBankInfoManager stakeholderBankInfoManager) {
        this.stakeholderBankInfoManager = stakeholderBankInfoManager;
    }

    //@Override
    public SwitchWrapper getPhoenixTransactions(SwitchWrapper switchWrapper) throws Exception {
        return this.switchController.getPhoenixTransactions(switchWrapper);
    }


    private AccountInfoModel getThreadLocalAccountInfoModel(SwitchWrapper switchWrapper) {
        AccountInfoModel accountInfoModel = null;
		/*StringBuffer ids = new StringBuffer()
		.append(" [ TransactionID: " + (switchWrapper.getWorkFlowWrapper().getTransactionModel() == null ? " is Null " : switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionId())+ " ")
		.append(" TransactionCode: " + (switchWrapper.getWorkFlowWrapper().getTransactionCodeModel() == null ? " is Null " : switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode())+ " ")
		.append(" ProductID: " + (switchWrapper.getWorkFlowWrapper().getProductModel() == null ? " is Null " : switchWrapper.getWorkFlowWrapper().getProductModel().getProductId()) + " ")
		.append(" AppUserID: " + (ThreadLocalAppUser.getAppUserModel().getAppUserId()) + " ] ");
		StringBuffer debugLogStr = new StringBuffer()
			.append("[PhoenixFinancialInstitutionImpl] Looking up for ThreadLocalAccountInfoModel. ")
			.append(ids.toString());
		
		System.out.println( debugLogStr.toString() );

		
		if(switchWrapper.getWorkFlowWrapper().isAccountToCashLeg1()){
		
			accountInfoModel = ThreadLocalAccountInfo.getLoggedInCustomerAccountInfo();
		
		}else if(switchWrapper.isAccountToCashLeg2() || switchWrapper.isCashToCashLeg2()){
		
			accountInfoModel = ThreadLocalAccountInfo.getLoggedInAgentAccountInfo();
		
		}else if(switchWrapper.getWorkFlowWrapper().isCashToCashLeg1()){
		
			accountInfoModel = ThreadLocalAccountInfo.getLoggedInAgentAccountInfo();
		
		}else if(switchWrapper.getWorkFlowWrapper().isCashDeposit()){
			
			accountInfoModel = ThreadLocalAccountInfo.getLoggedInAgentAccountInfo();
			
		}else if(switchWrapper.getWorkFlowWrapper().getProductModel() != null && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50006L){
			
			accountInfoModel = ThreadLocalAccountInfo.getLoggedInAgentAccountInfo();
			
		}else if(switchWrapper.getWorkFlowWrapper().getProductModel() != null && UtilityCompanyEnum.contains(String.valueOf(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId()))){
			
			if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
				
				accountInfoModel = ThreadLocalAccountInfo.getLoggedInCustomerAccountInfo();
				
			}else{//Agent bill payment
				
				accountInfoModel = ThreadLocalAccountInfo.getLoggedInAgentAccountInfo();
				
			}
			
		}else if (switchWrapper.getWorkFlowWrapper().getProductModel() != null && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50013L){
			
			accountInfoModel = ThreadLocalAccountInfo.getLoggedInAgentAccountInfo();
			
		}else if(switchWrapper.getWorkFlowWrapper().isCheckBalance()){
		
			accountInfoModel = ThreadLocalAccountInfo.getLoggedInAgentAccountInfo();
		
		}
		
		//Logging AccountInfoModel
		if (accountInfoModel == null) {

			System.out.println("[PhoenixFinancialInstitutionImpl] AccountInfoModel Not found in ThreadLocal. Going to load from Verifly Module. " + ids.toString() + " ");
			
		}else{
			StringBuffer accountInfoModelIds = new StringBuffer()
				.append(" [ AccountInfoModel.customerId" + accountInfoModel.getCustomerId() + " ")
				.append(" Account No: " + accountInfoModel.getAccountNo() + " ")
				.append(" Nick: " + accountInfoModel.getAccountNick() + " ] ");

			System.out.println("[PhoenixFinancialInstitutionImpl] Found ThreadLocalAccountInfoModel " + accountInfoModelIds.toString() + " " + ids.toString() + " ");
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
		
		System.out.println("[PhoenixFinancialInstitutionImpl] Now Putting AccountInfoModel in ThreadLocal. " + accountInfoModelIds.toString() + " " + userAndTrxids.toString());
		
		if(switchWrapper.getWorkFlowWrapper().isAccountToCashLeg1()){
		
			ThreadLocalAccountInfo.setLoggedInCustomerAccountInfo(accountInfoModel);
		
		}else if(switchWrapper.isCashToCashLeg2()){
		
			ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);
			
		}else if(switchWrapper.getWorkFlowWrapper().isCashToCashLeg1()){
		
			ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);
		
		}else if(switchWrapper.getWorkFlowWrapper().isCashDeposit()){
		
			ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);
		
		} else if(switchWrapper.getWorkFlowWrapper().getProductModel() != null && switchWrapper.getWorkFlowWrapper().getProductModel().getProductId() == 50006L){
		
			ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);
		
		}else if(switchWrapper.getWorkFlowWrapper().getProductModel() != null && UtilityCompanyEnum.contains(String.valueOf(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId()))){
			
			if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
				
				ThreadLocalAccountInfo.setLoggedInCustomerAccountInfo(accountInfoModel);
				
			}else{//Agent bill payment
				
				ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);
				
			}
			
		}else if(switchWrapper.getWorkFlowWrapper().isCheckBalance()){
		
			ThreadLocalAccountInfo.setLoggedInAgentAccountInfo(accountInfoModel);
			
		}*/

    }

    public SwitchWrapper reverseFundTransfer(SwitchWrapper switchWrapper) throws Exception {
        return this.switchController.reverseFundTransfer(switchWrapper);
    }

}
