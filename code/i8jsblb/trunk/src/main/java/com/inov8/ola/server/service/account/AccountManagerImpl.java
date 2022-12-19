package com.inov8.ola.server.service.account;

/**
 * @author Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008
 * Description:
 */


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.integration.common.model.*;
import com.inov8.integration.enums.ResponseCodeEnum;
import com.inov8.integration.middleware.novatti.Wallet;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.dao.portal.ola.AccountTypeViewDao;
import com.inov8.microbank.server.dao.portal.ola.OlaCustomerAccountTypeDao;
import com.inov8.microbank.server.dao.safrepo.SafRepoDao;
import com.inov8.microbank.server.dao.stakeholdermodule.StakeholderBankInfoDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.customermodule.CustomerManager;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.ola.integration.vo.OLAInfo;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.dao.account.AccountDAO;
import com.inov8.ola.server.dao.ledger.LedgerDAO;
import com.inov8.ola.server.dao.limit.LimitRuleDAO;
import com.inov8.ola.server.facade.LedgerFacade;
import com.inov8.ola.server.service.limit.LimitManager;
import com.inov8.ola.util.*;
import com.inov8.ola.util.EncryptionUtil;
import com.inov8.ola.util.ReasonConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTimeUtils;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;


public class AccountManagerImpl implements AccountManager {

    private AccountDAO accountDAO;
    private LedgerDAO ledgerDAO;
    private GenericDao genericDAO;
    private LimitManager limitManager;
    private OlaCustomerAccountTypeDao olaCustomerAccountTypeDao;
    private AccountTypeViewDao accountTypeViewDao;
    private ActionLogManager actionLogManager;
    private LimitRuleDAO limitRuleDAO;
    private CreditAccountQueueSender creditAccountQueueSender;
    private StakeholderBankInfoDAO stakeholderBankInfoDAO;
    private CustomerManager customerManager;
    private RetailerContactManager retailerContactManager;
    private HandlerManager handlerManager;
    private SafRepoDao safRepoDao;
    private LedgerFacade ledgerFacade;


    protected static Log logger = LogFactory.getLog(AccountManagerImpl.class);

    public OLAVO makeTxRequiresNewTransaction(OLAVO olaVO) throws Exception {

        if (olaVO.getRequiresNoCrypto()) {//for special accounts where encryption is NOT used in balance and account no. columns

            return makeTxWithNoCrypto(olaVO);

        } else {

            return makeTx(olaVO);
        }
    }

    private BaseWrapper loadAccountModelByAccNumber(String plainText) throws Exception {

        AccountModel accountModel = new AccountModel();
        accountModel.setAccountNumber(EncryptionUtil.encryptAccountNo(plainText));

        BaseWrapper _baseWrapper = new BaseWrapperImpl();
        _baseWrapper.setBasePersistableModel(accountModel);
        BaseWrapper baseWrapper = this.accountDAO.loadAccountAndLock(_baseWrapper); // Lock the account for update

        return baseWrapper;
    }

    private boolean checkAlreadySuccessful(Long ledgerId) {
        boolean result = false;
        LedgerModel ledgerModel = ledgerDAO.findByPrimaryKey(ledgerId);
        if (ledgerModel != null &&
                ledgerModel.getIsProcessedByQueue() == Boolean.TRUE &&
                ledgerModel.getQueueStatus() == OLAConstantsInterface.QUEUE_STATUS_COMPLETE.longValue()) {

            result = true;
        }
        return result;
    }

    private void updateSafRepoByQueue(long ledgerId) throws FrameworkCheckedException {
        logger.info("Going to update safeRepo Is_Complete = 1 for LedgerId:" + ledgerId);
        SafRepoModel safRepoModel = new SafRepoModel();
        safRepoModel.setLedgerId(ledgerId);
        safRepoModel.setIsComplete(true);
        safRepoModel.setUpdatedOn(new Date());
        safRepoDao.updateSafRepoByQueue(safRepoModel);
    }

    public OLAVO makeTx(OLAVO olaVO) throws Exception {
        if (olaVO.getCustomerAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT || olaVO.getIsViaQueue()) {
            if (checkAlreadySuccessful(olaVO.getLedgerId())) {
                logger.error("*********************************************************************************");
                logger.error("Ignoring duplicate credit request against trxID:" + olaVO.getMicrobankTransactionCode() + " --- ledger_id:" + olaVO.getLedgerId() + " because It is already successful. Further investigation required.");
                logger.error("*********************************************************************************");

                if (olaVO.getAccountCreditQueueLogId() != null) { // in case when we retry the credit entry from table ACCOUNT_QUEUE_CR
                    olaVO.setStatusName(AccountCreditQueueLogModel.SUCCESSFUL);
                    logger.error("Going to update status to successful for ACCOUNT_QUEUE_CR against trxID:" + olaVO.getMicrobankTransactionCode() + " --- ledger_id:" + olaVO.getLedgerId() + " because It is already successful.");
                    createSaveAccountCreditQueueLogModel(olaVO);
                }
                updateSafRepoByQueue(olaVO.getLedgerId());

                olaVO.setResponseCode("00");
                return olaVO;

            }
        }


        if (olaVO.getTransactionTypeId().equalsIgnoreCase(String.valueOf(TransactionTypeConstants.DEBIT.longValue()))) {

            BaseWrapper baseWrapper = loadAccountModelByAccNumber(olaVO.getPayingAccNo());

            AccountModel payingAccNoAccountModel = (AccountModel) baseWrapper.getBasePersistableModel();

            if (payingAccNoAccountModel == null) {

                olaVO.setResponseCode("03");
                return olaVO;

            } else if (payingAccNoAccountModel.getStatusId().longValue() == StatusConstants.INACTIVE.longValue() && payingAccNoAccountModel.getCustomerAccountTypeId() != CustomerAccountTypeConstants.SETTLEMENT) {

                baseWrapper = this.updateAccount(baseWrapper); // update with the same data..dont want to have it locked forever
                olaVO.setResponseCode("02");
                return olaVO;
            }

            olaVO.setCustomerAccountTypeId(payingAccNoAccountModel.getCustomerAccountTypeId());

//			Double balance = Double.parseDouble(EncryptionUtil.decryptPin(payingAccNoAccountModel.getBalance())) ;
            Double balance = Double.parseDouble(payingAccNoAccountModel.getBalance());

            balance = Double.valueOf(Formatter.formatDouble(balance)); // formatting to up to 2 decimal places

            olaVO.setTransactionAmount(olaVO.getBalance());

            olaVO.setAccountId(payingAccNoAccountModel.getAccountId());//From A/C ID


            if ((balance - olaVO.getBalance() < 0) && olaVO.getCustomerAccountTypeId().longValue() != CustomerAccountTypeConstants.SETTLEMENT) {
                baseWrapper = this.updateAccount(baseWrapper); // update with the same data..dont want to have it locked forever
                olaVO.setResponseCode("01");
                return olaVO;
            }

            try {
                /*added by mudassir:
                 * 1.	salary disbursement also does not check the limits
                 * 2.	Recon Settlement Account also bypasses the limits*/
                if (// ! olaVO.getReasonId().equals(ReasonConstants.BILL_PAYMENT) &&
                        !olaVO.getReasonId().equals(ReasonConstants.SALARY_DISBURSEMENT)
                                && !olaVO.getReasonId().equals(ReasonConstants.REVERSAL)
                                && !olaVO.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.SETTLEMENT)
                                && !olaVO.getReasonId().equals(ReasonConstants.SETTLEMENT)
                                && !olaVO.getReasonId().equals(ReasonConstants.ROLLBACK_WALKIN_CUSTOMER)
//						&& ! olaVO.getReasonId().equals(ReasonConstants.RETAIL_PAYMENT)
//						&& ! olaVO.getReasonId().equals(ReasonConstants.DONATION_PAYMENT)
                                && !olaVO.getReasonId().equals(ReasonConstants.REVERSE_BILL_PAYMENT)
                && !olaVO.getReasonId().equals(ReasonConstants.FUND_CUSTOMER_BB_CORE_AC)) {
                    OLAVO debitLimitsVO = verifyDebitLimits(olaVO);
                    if (debitLimitsVO.getResponseCode().equals("00")) {
                        Double finalBalance = Double.valueOf(Formatter.formatDouble(balance - olaVO.getBalance())); // formatting to up to 2 decimal places


//						payingAccNoAccountModel.setBalance( EncryptionUtil.encryptPin(String.valueOf(finalBalance)) ) ;
                        payingAccNoAccountModel.setBalance(String.valueOf(finalBalance));
                        baseWrapper.setBasePersistableModel(payingAccNoAccountModel);
                        payingAccNoAccountModel.setUpdatedOn(new Date());
                        baseWrapper = this.updateAccount(baseWrapper);

                        logger.info("Balance verification :: " + balance + " " + "New Balance ::" + finalBalance);
                        if (balance.equals(finalBalance)) {
                            olaVO.setResponseCode("74");
                            throw new FrameworkCheckedException(olaVO.getResponseCode());
                        }
                        logger.info("Balance Updated Successfully :: " + balance + "New Balance" + finalBalance);

                        olaVO.setFromBalanceAfterTransaction(finalBalance);
                        olaVO.setResponseCode("00");

                    } else {
                        olaVO.setResponseCode(debitLimitsVO.getResponseCode());
                    }
                } else {
//					payingAccNoAccountModel.setBalance( EncryptionUtil.encryptPin(String.valueOf((balance - olaVO.getBalance()))) ) ;
                    payingAccNoAccountModel.setBalance(String.valueOf((balance - olaVO.getBalance())));
                    baseWrapper.setBasePersistableModel(payingAccNoAccountModel);
                    payingAccNoAccountModel.setUpdatedOn(new Date());
                    baseWrapper = this.updateAccount(baseWrapper);

                    olaVO.setFromBalanceAfterTransaction(balance - olaVO.getBalance());
                    olaVO.setResponseCode("00");

                }

                logger.info("[Debit Log] TrxId:" + olaVO.getMicrobankTransactionCode() +
                        " From_AccId:" + payingAccNoAccountModel.getAccountId() +
                        "\nBalance_Equation:" + balance + " - " + olaVO.getBalance() + " = " + olaVO.getFromBalanceAfterTransaction()
                        + "\n At Time :: " + new Date());

                if (olaVO.getCustomerAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT) {
                    updateLedgerModelAfterQueue(olaVO);
                    updateSafRepoByQueue(olaVO.getLedgerId());
                }


                if (olaVO.getAccountCreditQueueLogId() != null) { // in case when we retry the debit entry from table ACCOUNT_QUEUE_CR
                    olaVO.setStatusName(AccountCreditQueueLogModel.SUCCESSFUL);
                    createSaveAccountCreditQueueLogModel(olaVO);
                }

            } catch (Exception e) {
                logger.error("[AccountManagerImpl.makeTx] Exception in Debit: " + e.getMessage() + " microbankTxcode:" + olaVO.getMicrobankTransactionCode());
//				e.printStackTrace();
                olaVO.setResponseCode("06");
            }
        } else if (olaVO.getTransactionTypeId().equalsIgnoreCase(String.valueOf(TransactionTypeConstants.CREDIT.longValue()))) {

            try {

                BaseWrapper baseWrapper = this.accountDAO.loadAccountAndLock(olaVO.getReceivingAccountId());

                logger.info("loadAccountAndLock CR ACID : " + olaVO.getReceivingAccountId() + " at Time :: " + new Date());
                AccountModel receivingAccountModel = (AccountModel) baseWrapper.getBasePersistableModel();

//				Double existingAccountBalance = Double.parseDouble(EncryptionUtil.decryptPin(receivingAccountModel.getBalance())) ;
                Double existingAccountBalance = Double.parseDouble(receivingAccountModel.getBalance());

                Double balance = existingAccountBalance + olaVO.getBalance();

                balance = Double.valueOf(Formatter.formatDouble(balance)); // formatting to up to 2 decimal places

                olaVO.setCustomerAccountTypeId(receivingAccountModel.getCustomerAccountTypeId());

                LedgerModel ledgerModel = ledgerDAO.findByPrimaryKey(olaVO.getLedgerId());
                ledgerModel.setToBalanceAfterTransaction(balance);
                ledgerModel.setTransactionTime(new Date());

                if (olaVO.getCustomerAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT || olaVO.getIsViaQueue()) {
                    ledgerModel.setIsSettledQueue(true);
                    ledgerModel.setIsProcessedByQueue(true);
                    ledgerModel.setQueueStatus(OLAConstantsInterface.QUEUE_STATUS_COMPLETE);
                }

                ledgerDAO.saveOrUpdate(ledgerModel);

//				receivingAccountModel.setBalance(EncryptionUtil.encryptPin(String.valueOf(balance))) ;
                receivingAccountModel.setBalance(String.valueOf(balance));
                receivingAccountModel.setUpdatedOn(new Date());
                baseWrapper.setBasePersistableModel(receivingAccountModel);

                baseWrapper = this.updateAccount(baseWrapper);

                logger.info("[Credit Log] TrxId:" + olaVO.getMicrobankTransactionCode() +
                        " To_AccId:" + olaVO.getReceivingAccountId() +
                        "\nBalance_Equation:" + existingAccountBalance + " + " + olaVO.getBalance() + " = " + balance
                        + "\n At Time :: " + new Date());

                olaVO.setResponseCode("00");

                if (olaVO.getCustomerAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT || olaVO.getIsViaQueue()) {
                    updateSafRepoByQueue(olaVO.getLedgerId());
                }

                if (olaVO.getAccountCreditQueueLogId() != null) { // in case when we retry the credit entry from table ACCOUNT_QUEUE_CR
                    olaVO.setStatusName(AccountCreditQueueLogModel.SUCCESSFUL);
                    createSaveAccountCreditQueueLogModel(olaVO);
                }

            } catch (Exception e) {
                logger.error("[AccountManagerImpl.makeTx] Exception in Credit: microbankTxcode:" + olaVO.getMicrobankTransactionCode());
                logger.error(e);
                olaVO.setAddress(e.getLocalizedMessage());
                olaVO.setResponseCode("06");
            }
        }

        return olaVO;
    }

    private void updateLedgerModelAfterQueue(OLAVO olaVO) {

        if (olaVO.getToBalanceAfterTransaction() == null) {
            olaVO.setToBalanceAfterTransaction(0d);
        }

        if (olaVO.getFromBalanceAfterTransaction() == null) {
            olaVO.setFromBalanceAfterTransaction(0d);
        }

        LedgerModel ledgerModel = ledgerDAO.findByPrimaryKey(olaVO.getLedgerId());
        ledgerModel.setToBalanceAfterTransaction(olaVO.getToBalanceAfterTransaction());
        ledgerModel.setBalanceAfterTransaction(olaVO.getFromBalanceAfterTransaction());
        ledgerModel.setTransactionTime(new Date());
        ledgerModel.setIsSettledQueue(true);
        ledgerModel.setIsProcessedByQueue(true);
        ledgerModel.setQueueStatus(OLAConstantsInterface.QUEUE_STATUS_COMPLETE);
        ledgerDAO.saveOrUpdate(ledgerModel);

    }


    public OLAVO makeTxWithNoCrypto(OLAVO olaVO) throws Exception {

        AccountModel accountModel = new AccountModel();
        accountModel.setAccountNumber(olaVO.getPayingAccNo());

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(accountModel);

        baseWrapper = this.loadAccount(baseWrapper); // Lock the account for update
        accountModel = (AccountModel) baseWrapper.getBasePersistableModel();

        if (accountModel == null) {
            olaVO.setResponseCode("03");
            return olaVO;

        } else if (accountModel.getStatusId().longValue() == StatusConstants.INACTIVE.longValue()) {
//			baseWrapper = this.updateAccount(baseWrapper) ; // update with the same data..dont want to have it locked forever
            olaVO.setResponseCode("02");
            return olaVO;
        }

        logger.info("[AccountManagerImpl.makeTxWithNoCrypto] Loaded Account ID:" + accountModel.getAccountId() + " microbankTxcode:" + olaVO.getMicrobankTransactionCode());

        Double balance = Double.valueOf(accountModel.getBalance());

        com.inov8.integration.common.model.LedgerModel ledgerModel = new com.inov8.integration.common.model.LedgerModel();

        ledgerModel.setTransactionAmount(olaVO.getBalance());
        ledgerModel.setAccountId(accountModel.getAccountId());

        if (olaVO.getAuthCode() != null && !olaVO.getAuthCode().equals(""))
            ledgerModel.setAuthCode(olaVO.getAuthCode());
        else
            ledgerModel.setAuthCode(AuthCodeGenerator.getAuthCode());

        ledgerModel.setMicrobankTransactionCode(olaVO.getMicrobankTransactionCode());
        ledgerModel.setReasonId(olaVO.getReasonId());
        ledgerModel.setTransactionTime(new Date());
        ledgerModel.setTransactionTypeId(Long.parseLong(olaVO.getTransactionTypeId()));
        olaVO.setAuthCode(ledgerModel.getAuthCode());
        olaVO.setAccountId(accountModel.getAccountId());

        if (olaVO.getTransactionTypeId().equalsIgnoreCase(String.valueOf(TransactionTypeConstants.DEBIT.longValue()))) {
            if ((balance - olaVO.getBalance() < 0) && accountModel.getCustomerAccountTypeId().longValue() != CustomerAccountTypeConstants.SETTLEMENT) {
//				baseWrapper = this.updateAccount(baseWrapper) ; // update with the same data..dont want to have it locked forever
                olaVO.setResponseCode("01");
                ledgerModel.setResponseCodeId(ResponseCodeConstants.INSUFFICIENT_BALANCE);
                return olaVO;
            }

            try {
                //save ledger first
                //olaVO.setBalance(ledgerModel.getBalanceAfterTransaction());
                ledgerModel.setBalanceAfterTransaction(balance - olaVO.getBalance());
                ledgerModel.setResponseCodeId(ResponseCodeConstants.SUCCESS);
                ledgerModel = this.genericDAO.createEntity(ledgerModel);

                //update Account
                this.updateAccountBalanceByAccountId(accountModel.getAccountId(), olaVO.getBalance(), ledgerModel.getLedgerId(), Boolean.FALSE);

                olaVO.setResponseCode("00");


                logger.info("[AccountManagerImpl.makeTxWithNoCrypto] (Debit) microbankTxcode:" + olaVO.getMicrobankTransactionCode() + " Reason ID:" + olaVO.getReasonId() + " AuthCode:" + ledgerModel.getAuthCode());

            } catch (Exception e) {
                logger.error("[AccountManagerImpl.makeTxWithNoCrypto] Exception in Debit: " + e.getMessage() + " microbankTxcode:" + olaVO.getMicrobankTransactionCode());
//				e.printStackTrace();
                olaVO.setResponseCode("06");
                ledgerModel.setResponseCodeId(ResponseCodeConstants.UNKNOWN_RESPONSE);
            }
        } else if (olaVO.getTransactionTypeId().equalsIgnoreCase(String.valueOf(TransactionTypeConstants.CREDIT.longValue()))) {
            try {

                //save ledger first
//				olaVO.setBalance(ledgerModel.getBalanceAfterTransaction());
                ledgerModel.setBalanceAfterTransaction(balance + olaVO.getBalance());
                ledgerModel.setResponseCodeId(ResponseCodeConstants.SUCCESS); // moved by maqsood from 2 lines below
                baseWrapper.setBasePersistableModel(ledgerModel);
                ledgerModel = this.genericDAO.createEntity(ledgerModel);

                //update Account
                this.updateAccountBalanceByAccountId(accountModel.getAccountId(), olaVO.getBalance(), ledgerModel.getLedgerId(), Boolean.TRUE);

                accountModel = null;
                olaVO.setResponseCode("00");

                logger.info("[AccountManagerImpl.makeTxWithNoCrypto] (Credit) microbankTxcode:" + olaVO.getMicrobankTransactionCode() + " Reason ID:" + olaVO.getReasonId() + " AuthCode:" + ledgerModel.getAuthCode());

            } catch (Exception e) {
                logger.error("[AccountManagerImpl.makeTxWithNoCrypto] Exception in Credit: " + e.getMessage() + " microbankTxcode:" + olaVO.getMicrobankTransactionCode());
//				e.printStackTrace();
                olaVO.setResponseCode("06");
                ledgerModel.setResponseCodeId(ResponseCodeConstants.UNKNOWN_RESPONSE);
            }
        }

        return olaVO;
    }


    private void createSaveAccountCreditQueueLogModel(OLAVO olaVO) {

        AccountCreditQueueLogModel accountCreditQueueLogModel = new AccountCreditQueueLogModel();

        if (olaVO.getAccountCreditQueueLogId() != null) {
            accountCreditQueueLogModel.setAccountCreditQueueLogId(olaVO.getAccountCreditQueueLogId());
            accountCreditQueueLogModel = (AccountCreditQueueLogModel) this.genericDAO.getEntityByPrimaryKey(accountCreditQueueLogModel);
            accountCreditQueueLogModel.setStatus(olaVO.getStatusName());

            if (olaVO.getStatusName() != null && olaVO.getStatusName().equals(AccountCreditQueueLogModel.SUCCESSFUL)) {
                accountCreditQueueLogModel.setDescription(""); // remove desc added by DB Scheduler
            } else {
                accountCreditQueueLogModel.setDescription(olaVO.getAddress());
            }

            if (ThreadLocalAppUser.getAppUserModel() != null && ThreadLocalAppUser.getAppUserModel().getAppUserId() != null) {
                accountCreditQueueLogModel.setUpdatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
            } else {
                accountCreditQueueLogModel.setUpdatedBy(2L);
            }
            accountCreditQueueLogModel.setUpdatedOn(new Date());
            this.genericDAO.updateEntity(accountCreditQueueLogModel);
        } else {

            if (Long.valueOf(olaVO.getTransactionTypeId()) == TransactionTypeConstants.CREDIT) {
                accountCreditQueueLogModel.setIsCreditPush(Boolean.TRUE);
            } else {
                accountCreditQueueLogModel.setIsCreditPush(Boolean.FALSE);
            }

            accountCreditQueueLogModel.setAuthCode(olaVO.getAuthCode());
            accountCreditQueueLogModel.setBalance(olaVO.getBalance());
            accountCreditQueueLogModel.setCreatedBy(2L);
            accountCreditQueueLogModel.setCreatedOn(new Date());
            accountCreditQueueLogModel.setCustomerAccountTypeId(olaVO.getCustomerAccountTypeId());
            accountCreditQueueLogModel.setDescription(olaVO.getAddress());
            accountCreditQueueLogModel.setLedgerId(olaVO.getLedgerId());
            accountCreditQueueLogModel.setReasonId(olaVO.getReasonId());
            accountCreditQueueLogModel.setResponseCode(olaVO.getResponseCode());
            accountCreditQueueLogModel.setBalanceAfterTransaction(olaVO.getToBalanceAfterTransaction());
            accountCreditQueueLogModel.setTransactionDateTime(olaVO.getTransactionDateTime());
            accountCreditQueueLogModel.setTransactionTypeId(Long.valueOf(olaVO.getTransactionTypeId()));
            accountCreditQueueLogModel.setToBankAccountNumber(olaVO.getPayingAccNo());
            accountCreditQueueLogModel.setReceivingAccountId(olaVO.getReceivingAccountId());
            accountCreditQueueLogModel.setTransactionCode(olaVO.getMicrobankTransactionCode());
            accountCreditQueueLogModel.setStatus(AccountCreditQueueLogModel.FAILED);

            this.genericDAO.createEntity(accountCreditQueueLogModel);
        }
    }


    private OlaCustomerAccountTypeModel getOlaCustomerAccountTypeModel(AccountModel accountModel) throws FrameworkCheckedException {

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(new OlaCustomerAccountTypeModel(accountModel.getCustomerAccountTypeId()));
        searchOlaCustomerAccountTypes(searchBaseWrapper);

        List<OlaCustomerAccountTypeModel> resultsetList = searchBaseWrapper.getCustomList().getResultsetList();

        OlaCustomerAccountTypeModel customerAccountTypeModel = null;

        if (resultsetList != null && resultsetList.size() == 1) {

            return customerAccountTypeModel = resultsetList.get(0);
        }

        return null;
    }

    /**
     * @param olaVO
     * @return
     * @throws Exception
     */
    private OLAVO checkCreditLimits(OLAVO olaVO, BaseWrapper baseWrapper) throws Exception {

        Long sTime = DateTimeUtils.currentTimeMillis();
        AccountModel receivingAccountModel = (AccountModel) baseWrapper.getBasePersistableModel();
        logger.info("Product ID ( " + olaVO.getProductId() + " ) The Start of [checkCreditLimits()] at the time :: " + new Date());
        if (receivingAccountModel == null) {

            olaVO.setResponseCode("03");
            return olaVO;

        } else if (receivingAccountModel.getStatusId().longValue() == StatusConstants.INACTIVE.longValue() && receivingAccountModel.getCustomerAccountTypeId() != CustomerAccountTypeConstants.SETTLEMENT) {

            olaVO.setResponseCode("02");

            return olaVO;
        }

        OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = getOlaCustomerAccountTypeModel(receivingAccountModel);
        olaVO.setIsCustomerAccountType(olaCustomerAccountTypeModel.getIsCustomerAccountType());
        olaVO.setCustomerAccountTypeId(receivingAccountModel.getCustomerAccountTypeId());

//		Double existingAccountBalance = Double.parseDouble(EncryptionUtil.decryptPin(receivingAccountModel.getBalance())) ;
        Double existingAccountBalance = Double.parseDouble(receivingAccountModel.getBalance());

        olaVO.setTransactionAmount(olaVO.getBalance());

        olaVO.setReceivingAccountId(receivingAccountModel.getAccountId());

        //If Reason id = 3, then decrease the amount from i8 OLA account ... because National distributor is recharging his account

        if (olaVO.getReasonId().longValue() == ReasonConstants.NATIONAL_DISTRIBUTOR_CREDIT_RECHARGE
                || olaVO.getReasonId().longValue() == ReasonConstants.DISTRIBUTOR_CREDIT_RECHARGE
                || olaVO.getReasonId().longValue() == ReasonConstants.RETAILER_CREDIT_RECHARGE) {

            AccountModel i8AccountModel = new AccountModel();
            i8AccountModel.setPrimaryKey(AccountConstants.INOV8_ACCOUNT_NUMBER);
            BaseWrapper i8BaseWrapper = new BaseWrapperImpl();
            i8BaseWrapper.setBasePersistableModel(i8AccountModel);

            i8BaseWrapper = this.loadAccountByPK(i8BaseWrapper);
            i8AccountModel = (AccountModel) i8BaseWrapper.getBasePersistableModel();
//			Double inov8Balance = Double.parseDouble( EncryptionUtil.decryptPin(i8AccountModel.getBalance())) ;
            Double inov8Balance = Double.parseDouble(i8AccountModel.getBalance());
            DecimalFormat dc = new DecimalFormat("##.##");
            inov8Balance = inov8Balance - olaVO.getBalance();
//			i8AccountModel.setBalance( EncryptionUtil.encryptPin( String.valueOf(inov8Balance))) ;
            i8AccountModel.setBalance(String.valueOf(inov8Balance));
            i8AccountModel.setUpdatedOn(new Date());
            i8BaseWrapper.setBasePersistableModel(i8AccountModel);
            baseWrapper = this.updateAccount(baseWrapper);
        }

        Double balance = existingAccountBalance + olaVO.getBalance();

        if (!olaVO.getReasonId().equals(ReasonConstants.SALARY_DISBURSEMENT)
                && !olaVO.getReasonId().equals(ReasonConstants.REVERSAL)
                && !olaVO.getReasonId().equals(ReasonConstants.SETTLEMENT)
                && !olaVO.getReasonId().equals(ReasonConstants.REVERSE_BILL_PAYMENT)
                && !olaVO.getReasonId().equals(ReasonConstants.ROLLBACK_WALKIN_CUSTOMER)
                && !olaVO.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.SETTLEMENT)
        &&!olaVO.getReasonId().equals(ReasonConstants.FUND_CUSTOMER_BB_CORE_AC)) {

            OLAVO creditLimitsVO = verifyCreditLimits(olaVO);
            olaVO.setResponseCode(creditLimitsVO.getResponseCode());

            if (olaVO.getResponseCode().equals("00")) {

                olaVO.setToBalanceAfterTransaction(balance);
            }

        } else {

            olaVO.setToBalanceAfterTransaction(balance);
            olaVO.setResponseCode("00");
        }
        Long eTime = DateTimeUtils.currentTimeMillis();
        logger.info("Product ID ( " + olaVO.getProductId() + " ) Total Time taken to execute [checkCreditLimits()] :: " + String.valueOf(eTime - sTime));
        return olaVO;
    }


    public OLAVO verifyCreditLimits(OLAVO olaVO) throws FrameworkCheckedException {
        OLAVO result = new OLAVO();
        String responseCode = null;

        try {
            if (!olaVO.getReasonId().equals(ReasonConstants.SALARY_DISBURSEMENT) && !olaVO.getReasonId().equals(ReasonConstants.REVERSAL)) {

                if (isLimitApplicable(olaVO, true, false)) {
                    responseCode = verifyDailyLimitForCredit(olaVO.getTransactionDateTime(), olaVO.getBalance(), olaVO.getReceivingAccountId(), olaVO.getCustomerAccountTypeId(), null);
                    if (!responseCode.equals("00")) {
                        result.setResponseCode(responseCode);
                        return result;
                    }

                    responseCode = verifyMonthlyLimitForCredit(olaVO.getTransactionDateTime(), olaVO.getBalance(), olaVO.getReceivingAccountId(), olaVO.getCustomerAccountTypeId(), null);
                    if (!responseCode.equals("00")) {
                        result.setResponseCode(responseCode);
                        return result;
                    }

                    responseCode = verifyYearlyLimitForCredit(olaVO.getTransactionDateTime(), olaVO.getBalance(), olaVO.getReceivingAccountId(), olaVO.getCustomerAccountTypeId(), null);
                    if (!responseCode.equals("00")) {
                        result.setResponseCode(responseCode);
                        return result;
                    }

                    if (olaVO.getIsCustomerAccountType()) {
                        responseCode = verifyMaximumBalanceLimitForCredit(olaVO.getBalance(), olaVO.getPayingAccNo(), olaVO.getCustomerAccountTypeId());
                        if (!responseCode.equals("00")) {
                            result.setResponseCode(responseCode);
                            return result;
                        }
                    }

                } else {
                    olaVO.setExcludeLimit(true);
                }


                // Handler Credit Limit check
                if (olaVO.getHandlerId() != null && olaVO.getHandlerAccountTypeId() != null) {

                    if (isLimitApplicable(olaVO, false, true)) {

                        responseCode = verifyDailyLimitForCredit(olaVO.getTransactionDateTime(), olaVO.getBalance(), olaVO.getReceivingAccountId(), olaVO.getHandlerAccountTypeId(), olaVO.getHandlerId());
                        if (!responseCode.equals("00")) {
                            result.setResponseCode(responseCode);
                            return result;
                        }

                        responseCode = verifyMonthlyLimitForCredit(olaVO.getTransactionDateTime(), olaVO.getBalance(), olaVO.getReceivingAccountId(), olaVO.getHandlerAccountTypeId(), olaVO.getHandlerId());
                        if (!responseCode.equals("00")) {
                            result.setResponseCode(responseCode);
                            return result;
                        }

                        responseCode = verifyYearlyLimitForCredit(olaVO.getTransactionDateTime(), olaVO.getBalance(), olaVO.getReceivingAccountId(), olaVO.getHandlerAccountTypeId(), olaVO.getHandlerId());
                        if (!responseCode.equals("00")) {
                            result.setResponseCode(responseCode);
                            return result;
                        }

                    } else {
                        olaVO.setHandlerExcludeLimit(true);
                    }
                }

                result.setResponseCode("00");

            } else {
                result.setResponseCode("00");
            }

        } catch (Exception ex) {
            logger.error("Error in AccountManagerImpl.verifyCreditLimits() :: " + ex);
        }
        return result;

    }

    public OLAVO verifyDebitLimits(OLAVO olaVO) throws FrameworkCheckedException {
        OLAVO result = new OLAVO();
        String responseCode = null;
        try {
            if (!olaVO.getReasonId().equals(ReasonConstants.SALARY_DISBURSEMENT) && !olaVO.getReasonId().equals(ReasonConstants.REVERSAL)) {

                if (isLimitApplicable(olaVO, false, false)) {

                    responseCode = verifyDailyLimitForDebit(olaVO.getTransactionDateTime(), olaVO.getBalance(), olaVO.getAccountId(), olaVO.getCustomerAccountTypeId(), null);
                    if (!responseCode.equals("00")) {
                        result.setResponseCode(responseCode);
                        return result;
                    }

                    responseCode = verifyMonthlyLimitForDebit(olaVO.getTransactionDateTime(), olaVO.getBalance(), olaVO.getAccountId(), olaVO.getCustomerAccountTypeId(), null);
                    if (!responseCode.equals("00")) {
                        result.setResponseCode(responseCode);
                        return result;
                    }

                    responseCode = verifyYearlyLimitForDebit(olaVO.getTransactionDateTime(), olaVO.getBalance(), olaVO.getAccountId(), olaVO.getCustomerAccountTypeId(), null);
                    if (!responseCode.equals("00")) {
                        result.setResponseCode(responseCode);
                        return result;
                    }

                } else {
                    olaVO.setExcludeLimit(true);
                }

                // Handler Debit Limit check
                if (olaVO.getHandlerId() != null && olaVO.getHandlerAccountTypeId() != null) {
                    if (isLimitApplicable(olaVO, false, true)) {

                        responseCode = verifyDailyLimitForDebit(olaVO.getTransactionDateTime(), olaVO.getBalance(), olaVO.getAccountId(), olaVO.getHandlerAccountTypeId(), olaVO.getHandlerId());
                        if (!responseCode.equals("00")) {
                            result.setResponseCode(responseCode);
                            return result;
                        }

                        responseCode = verifyMonthlyLimitForDebit(olaVO.getTransactionDateTime(), olaVO.getBalance(), olaVO.getAccountId(), olaVO.getHandlerAccountTypeId(), olaVO.getHandlerId());
                        if (!responseCode.equals("00")) {
                            result.setResponseCode(responseCode);
                            return result;
                        }

                        responseCode = verifyYearlyLimitForDebit(olaVO.getTransactionDateTime(), olaVO.getBalance(), olaVO.getAccountId(), olaVO.getHandlerAccountTypeId(), olaVO.getHandlerId());
                        if (!responseCode.equals("00")) {
                            result.setResponseCode(responseCode);
                            return result;
                        }

                    } else {
                        olaVO.setHandlerExcludeLimit(true);
                    }
                }

                result.setResponseCode("00");

            } else {
                result.setResponseCode("00");
            }

        } catch (Exception ex) {

            logger.error("Error in AccountManagerImpl.verifyDebitLimit() :: " + ex);
        }
        return result;

    }

    public OLAVO makeTxFori8Commission(OLAVO olaVO) throws Exception {
        AccountModel accountModel = new AccountModel();
        accountModel.setPrimaryKey(AccountConstants.INOV8_ACCOUNT_NUMBER);

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(accountModel);

        baseWrapper = this.loadAccountByPK(baseWrapper);
        accountModel = (AccountModel) baseWrapper.getBasePersistableModel();

        if (accountModel == null) {
            olaVO.setResponseCode("03");
            return olaVO;

        } else if (accountModel.getStatusId().longValue() == StatusConstants.INACTIVE.longValue()) {
            olaVO.setResponseCode("02");
            return olaVO;
        }

//		Double balance = Double.parseDouble(EncryptionUtil.decryptPin(accountModel.getBalance())) ;
        Double balance = Double.parseDouble(accountModel.getBalance());

        com.inov8.integration.common.model.LedgerModel ledgerModel = new com.inov8.integration.common.model.LedgerModel();

        ledgerModel.setTransactionAmount(olaVO.getBalance());
        ledgerModel.setAccountId(accountModel.getAccountId());

        if (olaVO.getAuthCode() != null && !olaVO.getAuthCode().equals(""))
            ledgerModel.setAuthCode(olaVO.getAuthCode());
        else
            ledgerModel.setAuthCode(AuthCodeGenerator.getAuthCode());

        ledgerModel.setMicrobankTransactionCode(olaVO.getMicrobankTransactionCode());
        ledgerModel.setReasonId(olaVO.getReasonId());
        ledgerModel.setTransactionTime(new Date());
        ledgerModel.setTransactionTypeId(Long.parseLong(olaVO.getTransactionTypeId()));
        olaVO.setAuthCode(ledgerModel.getAuthCode());


        if (olaVO.getTransactionTypeId().equalsIgnoreCase(String.valueOf(TransactionTypeConstants.CREDIT.longValue()))) {
            try {
                balance = balance + olaVO.getBalance();
//				accountModel.setBalance( EncryptionUtil.encryptPin( String.valueOf(balance))) ;
                accountModel.setBalance(String.valueOf(balance));
                accountModel.setUpdatedOn(new Date());
                baseWrapper.setBasePersistableModel(accountModel);
                baseWrapper = this.updateAccount(baseWrapper);

                // Saving in ledger
                com.inov8.integration.common.model.LedgerModel i8LedgerModel = new com.inov8.integration.common.model.LedgerModel();

                ledgerModel.setBalanceAfterTransaction(balance);

                olaVO.setResponseCode("00");
                ledgerModel.setResponseCodeId(ResponseCodeConstants.SUCCESS);
            } catch (Exception e) {
                logger.error("Error in AccountManagerImpl.makeTxFori8Commission() :: " + e);
                olaVO.setResponseCode("06");
                ledgerModel.setResponseCodeId(ResponseCodeConstants.UNKNOWN_RESPONSE);
            }
        }

        accountModel = null;
        baseWrapper.setBasePersistableModel(ledgerModel);
//		baseWrapper = this.saveLedgerEntry(baseWrapper) ;

        ledgerModel = this.genericDAO.createEntity(ledgerModel);

        return olaVO;
    }


    public OLAVO creditTransferRequiresNewTransaction(OLAVO olaVO) throws Exception {

        olaVO.setTransactionTypeId(TransactionTypeConstants.DEBIT.toString());

        olaVO = this.makeTx(olaVO);

        if (!olaVO.getResponseCode().equals("00")) {
            logger.error("DEBIT ACCOUNT : " + olaVO.getPayingAccNo() + " . " + olaVO.getResponseCode());
            throw new FrameworkCheckedException(olaVO.getResponseCode());
        }

        pushCreditQueue(olaVO);

		/*
		String payingAccountNo = olaVO.getPayingAccNo() ;
		olaVO.setPayingAccNo(olaVO.getReceivingAccNo()) ;
		olaVO.setTransactionTypeId(TransactionTypeConstants.CREDIT.toString()) ;
		olaVO = this.makeTx(olaVO);

		olaVO.setReceivingAccNo(olaVO.getPayingAccNo());//   A/C no. used in credit is Receiving A/C No.
		olaVO.setPayingAccNo(payingAccountNo) ;	//   A/C no. used in Debit is Paying A/C No.
		*/

        if (!olaVO.getResponseCode().equals("00")) {
            logger.error("CREDIT ACCOUNT ID: " + olaVO.getReceivingAccountId() + " . " + olaVO.getResponseCode());
            throw new FrameworkCheckedException(olaVO.getResponseCode());
        }

        olaVO = this.makeLedgerEntry(olaVO);

        return olaVO;
    }


    /**
     * @param olaVO
     * @return
     * @throws FrameworkCheckedException
     */
    private OLAVO pushCreditQueue(OLAVO olaVO) throws FrameworkCheckedException {

        if (olaVO.getTransactionTypeId().equalsIgnoreCase(TransactionTypeConstants.CREDIT.toString())) {
            olaVO.setIsCreditPush(Boolean.TRUE);
        } else {
            olaVO.setIsDebitPush(Boolean.TRUE);
        }

        try {
            logger.info("Pushing OLA Debit/Credit to queue for Ledger id: " + olaVO.getLedgerId());
            creditAccountQueueSender.send(olaVO);
        } catch (Exception e) {
            logger.error(e);
            throw new FrameworkCheckedException("Failed to queue Credit Account # " + olaVO.getPayingAccNo());
        }

        return olaVO;
    }


    /**
     * performs debit on OLAVO.debitAccountList and credit on OLAVO.creditAccountList
     * fromBalanceAfterTransaction, toBalanceAfterTransaction are set on the basis of OLAInfo.balanceAfterTrxRequired
     *
     * IMPORTANT: if debitAccountList / creditAccountList contains multiple balanceAfterTrxRequired = true
     * then it will be overridden (wrong output)
     */
    public OLAVO makeTransferFunds(OLAVO olaVo) throws Exception {
        Long sTime = DateTimeUtils.currentTimeMillis();
        Long eTime = DateTimeUtils.currentTimeMillis();
        logger.info("Product ID ( " + olaVo.getProductId() + " ) The Start of [AccountManagerImpl.makeTransferFunds()] at the time :: " + new Date());
        List<OLAInfo> debitAccountList = olaVo.getDebitAccountList();
        List<OLAInfo> creditAccountList = olaVo.getCreditAccountList();
        if (!olaVo.getIsCreditOnly() && !isDebitCreditEqual(debitAccountList, creditAccountList)) {
            logger.error("Debit/Credits not equal for trxId:" + olaVo.getMicrobankTransactionCode());
            olaVo.setResponseCode("51");
            throw new FrameworkCheckedException("51");
        }


        OLAVO _olaVO = new OLAVO();

//		String agentAccountNumber = null;

        if (olaVo.getIsCreditOnly() ||
                (debitAccountList != null && debitAccountList.size() > 0 &&
                        creditAccountList != null && creditAccountList.size() > 0)) {

            for (OLAInfo olaInfo : debitAccountList) {

                _olaVO = olaInfo.getOlaVoObject();
                _olaVO.setTransactionDateTime(olaVo.getTransactionDateTime());
                _olaVO.setProductId(olaVo.getProductId());
                _olaVO.setFromSegmentId(olaVo.getFromSegmentId());
                if (_olaVO.getCategory() == null) {
                    _olaVO.setCategory(olaVo.getCategory());
                }


                //**************************************************** start
                BaseWrapper baseWrapper = new BaseWrapperImpl();

                if (olaInfo.getCustomerAccountTypeId() != null && olaInfo.getCustomerAccountTypeId().longValue() == Long.valueOf(CustomerAccountTypeConstants.SETTLEMENT)) {
                    AccountModel tempAccountModel = new AccountModel();
                    tempAccountModel.setAccountNumber(EncryptionUtil.encryptAccountNo(olaInfo.getPayingAccNo()));
                    baseWrapper.setBasePersistableModel(tempAccountModel);
                    logger.info("Start of AccountManagerImpl.loadAccount(baseWrapper) at Time :: " + new Date() + " for A/C #: " + olaInfo.getPayingAccNo());
                    baseWrapper = loadAccount(baseWrapper); // load without locking
                    logger.info("End of AccountManagerImpl.loadAccount(baseWrapper) at Time :: " + new Date() + " for A/C #: " + olaInfo.getPayingAccNo());
                } else {
                    logger.info("Start of AccountManagerImpl.loadAccountModelByAccNumber(a/c #) at Time :: " + new Date() + " for A/C #: " + olaInfo.getPayingAccNo());
                    baseWrapper = loadAccountModelByAccNumber(olaInfo.getPayingAccNo()); // load and lock
                    logger.info("End of AccountManagerImpl.loadAccountModelByAccNumber(a/c #) at Time :: " + new Date() + " for A/C #: " + olaInfo.getPayingAccNo());
                }

                AccountModel payingAccNoAccountModel = (AccountModel) baseWrapper.getBasePersistableModel();
//				Double accountBalance = Double.parseDouble(EncryptionUtil.decryptPin(payingAccNoAccountModel.getBalance())) ;
                Double accountBalance = Double.parseDouble(payingAccNoAccountModel.getBalance());

                accountBalance = Double.valueOf(Formatter.formatDouble(accountBalance)); // formatting to up to 2 decimal places

                if (_olaVO.getCustomerAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT || (null != olaInfo.getProcessThroughQueue() && olaInfo.getProcessThroughQueue())) {
                    _olaVO.setFromBalanceAfterTransaction(-1d);
                    _olaVO.setIsViaQueue(true);
                } else {
                    _olaVO.setFromBalanceAfterTransaction(Double.valueOf(Formatter.formatDouble(accountBalance - olaInfo.getBalance())));
                    _olaVO.setIsViaQueue(false);
                }

                olaVo.setPayingAccNo(olaInfo.getPayingAccNo());
                _olaVO.setAccountId(payingAccNoAccountModel.getAccountId());

                if (payingAccNoAccountModel.getCustomerAccountTypeId().longValue() != Long.valueOf(CustomerAccountTypeConstants.SETTLEMENT) && null == olaInfo.getProcessThroughQueue()) {
                    _olaVO = this.makeTx(_olaVO);

                    if (_olaVO.getResponseCode() != null && !_olaVO.getResponseCode().equals("00")) {
                        olaVo.setResponseCode(_olaVO.getResponseCode());

                        throw new FrameworkCheckedException(_olaVO.getResponseCode());
                    }

                    if (olaInfo.getBalanceAfterTrxRequired() != null && olaInfo.getBalanceAfterTrxRequired()) {
                        olaVo.setFromBalanceAfterTransaction(_olaVO.getFromBalanceAfterTransaction());
                    }

                    if (olaInfo.getAgentBalanceAfterTrxRequired() != null && olaInfo.getAgentBalanceAfterTrxRequired() &&
                            olaInfo.getIsAgent() != null && olaInfo.getIsAgent()) {

//						agentAccountNumber = olaInfo.getPayingAccNo();
                        olaVo.setAgentBalanceAfterTransaction(_olaVO.getFromBalanceAfterTransaction());
                    }
                }
                _olaVO.setDeviceTypeId(olaVo.getDeviceTypeId());
                this.makeLedgerEntryMultipleCredit(_olaVO);
                if (_olaVO.getResponseCode() != null && (_olaVO.getResponseCode().equals("50") || _olaVO.getResponseCode().equals("06"))) {
                    olaVo.setResponseCode(_olaVO.getResponseCode());
                    throw new FrameworkCheckedException(_olaVO.getResponseCode());
                }

                if (payingAccNoAccountModel.getCustomerAccountTypeId().longValue() == Long.valueOf(CustomerAccountTypeConstants.SETTLEMENT) || (null != olaInfo.getProcessThroughQueue() && olaInfo.getProcessThroughQueue())) {
                    saveSafRepo(olaVo, _olaVO, payingAccNoAccountModel);
                }
                /**************************************************** old code before Debit Queue
                 _olaVO = this.makeTx(_olaVO);

                 if( !_olaVO.getResponseCode().equals("00") ) {

                 olaVo.setResponseCode(_olaVO.getResponseCode());

                 throw new FrameworkCheckedException(_olaVO.getResponseCode() );
                 }

                 if(olaInfo.getBalanceAfterTrxRequired() != null && olaInfo.getBalanceAfterTrxRequired()){
                 olaVo.setFromBalanceAfterTransaction(_olaVO.getFromBalanceAfterTransaction());
                 }

                 if(olaInfo.getAgentBalanceAfterTrxRequired() != null && olaInfo.getAgentBalanceAfterTrxRequired() &&
                 olaInfo.getIsAgent() != null && olaInfo.getIsAgent()) {

                 agentAccountNumber = olaInfo.getPayingAccNo();
                 olaVo.setAgentBalanceAfterTransaction(_olaVO.getFromBalanceAfterTransaction());
                 }

                 this.makeLedgerEntryMultipleCredit(_olaVO); */
            }
            logger.info("Product ID ( " + olaVo.getProductId() + " ) in [AccountManagerImpl.makeTransferFunds()] " +
                    "after debitAccountList  at the time :: " + String.valueOf(eTime - DateTimeUtils.currentTimeMillis()));
            eTime = DateTimeUtils.currentTimeMillis();

            for (OLAInfo olaInfo : creditAccountList) {

                _olaVO = olaInfo.getOlaVoObject();
                _olaVO.setTransactionDateTime(olaVo.getTransactionDateTime());
                _olaVO.setProductId(olaVo.getProductId());
                _olaVO.setToSegmentId(olaVo.getToSegmentId());
                if (_olaVO.getCategory() == null) {
                    _olaVO.setCategory(olaVo.getCategory());
                }

                AccountModel accountModel = new AccountModel();
                accountModel.setAccountNumber(EncryptionUtil.encryptAccountNo(_olaVO.getPayingAccNo()));
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                baseWrapper.setBasePersistableModel(accountModel);
                baseWrapper = loadAccount(baseWrapper);
                accountModel = (AccountModel) baseWrapper.getBasePersistableModel();

                if (accountModel == null) {

                    _olaVO.setResponseCode("03");
                    throw new FrameworkCheckedException(_olaVO.getResponseCode());

                } else if (accountModel.getStatusId().longValue() == StatusConstants.INACTIVE.longValue() && accountModel.getCustomerAccountTypeId() != CustomerAccountTypeConstants.SETTLEMENT) {

                    _olaVO.setResponseCode("02");
                    throw new FrameworkCheckedException(_olaVO.getResponseCode());
                }

                this.checkCreditLimits(_olaVO, baseWrapper);

                if (!ResponseCodeEnum.PROCESSED_OK.getValue().equals(_olaVO.getResponseCode())) {

                    olaVo.setResponseCode(_olaVO.getResponseCode());
                    throw new FrameworkCheckedException(_olaVO.getResponseCode());
                }

                _olaVO.setReceivingAccountId(accountModel.getAccountId());

                if (olaInfo.getAgentBalanceAfterTrxRequired() != null && olaInfo.getAgentBalanceAfterTrxRequired() &&
                        olaInfo.getIsAgent() != null && olaInfo.getIsAgent()) {

//					agentAccountNumber = olaInfo.getPayingAccNo();
//					Double existingAccountBalance = Double.parseDouble(EncryptionUtil.decryptPin(accountModel.getBalance()));
                    Double existingAccountBalance = Double.parseDouble(accountModel.getBalance());
                    olaVo.setAgentBalanceAfterTransaction(existingAccountBalance + olaInfo.getBalance());
                }

                if (olaInfo.getBalanceAfterTrxRequired() != null && olaInfo.getBalanceAfterTrxRequired()) {
                    olaVo.setToBalanceAfterTransaction(_olaVO.getToBalanceAfterTransaction());
                }

                if (_olaVO.getCustomerAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT || (null != olaInfo.getProcessThroughQueue() && olaInfo.getProcessThroughQueue())) {
                    _olaVO.setIsViaQueue(true);
                } else {
                    _olaVO.setIsViaQueue(false);
                }


                if (_olaVO.getBalance() > 0) {    //	to avoid 0 ledger entry.

                    this.makeLedgerEntryMultipleCredit(_olaVO);
                    if (_olaVO.getResponseCode() != null && (_olaVO.getResponseCode().equals("50") || _olaVO.getResponseCode().equals("06"))) {
                        olaVo.setResponseCode(_olaVO.getResponseCode());
                        throw new FrameworkCheckedException(_olaVO.getResponseCode());
                    }

                    if (CustomerAccountTypeConstants.SETTLEMENT == _olaVO.getCustomerAccountTypeId().longValue() || (null != olaInfo.getProcessThroughQueue() && olaInfo.getProcessThroughQueue())) {
//							_olaVO.setMicrobankTransactionCode(olaVo.getMicrobankTransactionCode()); Throwing error in case of Manual BB 2 BB Transfer
//							creditPushQueueList.add(_olaVO);
                        saveSafRepo(olaVo, _olaVO, accountModel);

                    } else {

                        this.makeTx(_olaVO);

                        if (!ResponseCodeEnum.PROCESSED_OK.getValue().equals(_olaVO.getResponseCode())) {

                            olaVo.setResponseCode(_olaVO.getResponseCode());

                            throw new FrameworkCheckedException(_olaVO.getResponseCode());
                        }
                    }
                }
                if (olaVo.getDeviceTypeId().equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString()) && olaVo.getReceiverCnic() == null) {
                    if (!_olaVO.getBalance().equals(olaVo.getCommissionAmount()))
                        saveWalletSafRepo(olaVo, _olaVO, accountModel);
                }

            }
            logger.info("Product ID ( " + olaVo.getProductId() + " ) in [AccountManagerImpl.makeTransferFunds()] " +
                    "after creditAccountList  at the time :: " + String.valueOf(eTime - DateTimeUtils.currentTimeMillis()));

            Double agentBalanceAfterTransaction = olaVo.getAgentBalanceAfterTransaction();
            agentBalanceAfterTransaction = (agentBalanceAfterTransaction == null) ? 0.0D : agentBalanceAfterTransaction;
            olaVo.setAgentBalanceAfterTransaction(agentBalanceAfterTransaction);

//			for(OLAVO creditOLAVO : creditPushQueueList) {
//
//				this.pushCreditQueue(creditOLAVO);
//			}

        } else if (olaVo.getProductId() == null || (olaVo.getProductId() != null && !olaVo.getProductId().equals(ProductConstantsInterface.BISP_CASH_OUT_WALLET)
                && !olaVo.getProductId().equals(ProductConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING) && !olaVo.getProductId().equals(ProductConstantsInterface.PROOF_OF_LIFE)) &&
                !olaVo.getProductId().equals(ProductConstantsInterface.BOP_CARD_ISSUANCE_REISSUANCE)) {
            logger.error("[AccountManagerImpl.makeTransferFunds] Error due to invalid data supplied.");
            olaVo.setResponseCode("06");
        }

        olaVo.setResponseCode(StringUtil.isNullOrEmpty(olaVo.getResponseCode()) ? "00" : olaVo.getResponseCode());
        eTime = DateTimeUtils.currentTimeMillis();
        logger.info("Product ID ( " + olaVo.getProductId() + " ) [AccountManagerImpl.makeTransferFunds()] " +
                "Total time taken to execute :: " + String.valueOf(eTime - sTime));
        return olaVo;
    }


    private OLAVO makeLedgerEntryMultipleCredit(OLAVO olaVO) throws Exception {

        Long fromAccountId = olaVO.getAccountId();
        Long toAccountId = olaVO.getReceivingAccountId();

        LedgerModel ledgerModel = new LedgerModel();

        ledgerModel.setTransactionAmount(olaVO.getBalance());

        Double balanceAfterTransaction = -1D;

        if (olaVO.getTransactionTypeId() != null && olaVO.getTransactionTypeId().equals(TransactionTypeConstantsInterface.OLA_DEBIT)) {
            ledgerModel.setAccountId(fromAccountId);
            ledgerModel.setToAccountId(PoolAccountConstantsInterface.LEDGER_SETTLEMENT_ACCOUNT_ID);

            if (!olaVO.getIsViaQueue()) {
                balanceAfterTransaction = olaVO.getFromBalanceAfterTransaction();
            }

            ledgerModel.setBalanceAfterTransaction(balanceAfterTransaction);
            ledgerModel.setToBalanceAfterTransaction(0d);
        } else {
            ledgerModel.setAccountId(PoolAccountConstantsInterface.LEDGER_SETTLEMENT_ACCOUNT_ID);
            ledgerModel.setToAccountId(toAccountId);

            ledgerModel.setBalanceAfterTransaction(0d);

            if (!olaVO.getIsViaQueue()) {
                balanceAfterTransaction = olaVO.getToBalanceAfterTransaction();
            }

            ledgerModel.setToBalanceAfterTransaction(balanceAfterTransaction);
        }


        ledgerModel.setAuthCode(AuthCodeGenerator.getAuthCode());

//		if( olaVO.getAuthCode() != null && !olaVO.getAuthCode().equals("") )
//			ledgerModel.setAuthCode( olaVO.getAuthCode() );
//		else
//			ledgerModel.setAuthCode( AuthCodeGenerator.getAuthCode() );
        if (olaVO.getExcludeLimit() != null && olaVO.getExcludeLimit()) {
            ledgerModel.setExcludeLimit(true);
        } else {
            ledgerModel.setExcludeLimit(false);
        }

        if (olaVO.getHandlerExcludeLimit() != null && olaVO.getHandlerExcludeLimit()) {
            ledgerModel.setExcludeLimitForHandler(true);
        } else {
            ledgerModel.setExcludeLimitForHandler(false);
        }

        ledgerModel.setMicrobankTransactionCode(olaVO.getMicrobankTransactionCode());
        ledgerModel.setReasonId(olaVO.getReasonId());
        ledgerModel.setTransactionTime(new Date());
        if (olaVO.getReasonId() != null && olaVO.getReasonId().equals(ReasonConstants.REVERSAL)) {
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            LedgerModel ogLedgerModel = new com.inov8.integration.common.model.LedgerModel();
            ogLedgerModel.setMicrobankTransactionCode(olaVO.getMicrobankTransactionCode());
//			ledgerModel.setAccountId(olaVO.getAccountId()) ;
            baseWrapper.setBasePersistableModel(ogLedgerModel);

            List<com.inov8.integration.common.model.LedgerModel> ledgerList = this.ledgerFacade.loadLedgerEntries(baseWrapper);
//			ledgerModel = (com.inov8.integration.common.model.LedgerModel)baseWrapper.getBasePersistableModel();
            for (LedgerModel originalLedger : ledgerList) {
                originalLedger.setIsReversal(true);
            }

            ledgerModel.setIsReversal(true);
        }
        ledgerModel.setTransactionTypeId(null);
        ledgerModel.setResponseCodeId(ResponseCodeConstants.SUCCESS);
        ledgerModel.setHandlerId(olaVO.getHandlerId());
        ledgerModel.setCategory(olaVO.getCategory());
        ledgerModel.setCommissionType(olaVO.getCommissionType());

        if (olaVO.getIsCreditPush()) {
            ledgerModel.setIsSettledQueue(olaVO.getIsCreditPush());
        }
        if (olaVO.getIsViaQueue()) {
            ledgerModel.setIsProcessedByQueue(true);
            ledgerModel.setQueueStatus(OLAConstantsInterface.QUEUE_STATUS_PENDING);
        }
        //set actual ledger ID to be reversed
//		if(olaVO.getReasonId() != null && olaVO.getReasonId().equals(ReasonConstants.REVERSAL)){
//			ledgerModel.setReversalLedgerId(olaVO.getLedgerId());
//			ledgerModel.setIsReversal(true);
//		}
        try {

            logger.info("[AccountManagerImpl.makeLedgerEntryMultipleCredit] microbankTxcode:" + olaVO.getMicrobankTransactionCode() + " blance after tx: " + olaVO.getBalance() + " Reason ID:" + olaVO.getReasonId() + " AuthCode:" + ledgerModel.getAuthCode());

            ledgerDAO.saveOrUpdate(ledgerModel);

            olaVO.setLedgerId(ledgerModel.getLedgerId());// set ledgerId in olaVO (to be used in case of reversal)
            olaVO.setAuthCode(ledgerModel.getAuthCode());// set authCode in olaVO (to be used in case of Orphan reversal)
        } catch (Exception e) {
            logger.error("[AccountManagerImpl.makeLedgerEntryMultipleCredit] Exception in Ledgger Tx: " + e.getMessage() + " microbankTxcode:" + olaVO.getMicrobankTransactionCode() + " Amount: " + olaVO.getBalance());
            olaVO.setResponseCode("06");
            ledgerModel.setResponseCodeId(ResponseCodeConstants.UNKNOWN_RESPONSE);

            //handle duplicate FT error message customization
            if (e != null && e.getCause() != null) {
                String detailedMessage = e.getCause().getMessage();
                if (detailedMessage != null && detailedMessage.contains("ORA-04088") && detailedMessage.contains("LEDGER_LEG2_FT_CHECK_TRIGGER")) {
                    olaVO.setResponseCode("50");
                }
            }
        }

        return olaVO;
    }


    /*
     * THis method is used from Orphan Transaction Reversal Scheduler.
     *
     */
    public OLAVO makeCreditTransfer(OLAVO olaVO) throws Exception {
        return creditTransferRequiresNewTransaction(olaVO);
    }

    public boolean markLedgerEntriesRequiresNewTransaction(Long ledgerId) throws Exception {
        return ledgerDAO.markLedgerReversalEntries(ledgerId);
    }

    public OLAVO makeLedgerEntry(OLAVO olaVO) throws Exception {

        // Setting From Account
        Long fromAccountId = olaVO.getAccountId();

        // Setting TO Account
        Long toAccountId = olaVO.getReceivingAccountId();

        com.inov8.integration.common.model.LedgerModel ledgerModel = new com.inov8.integration.common.model.LedgerModel();

        ledgerModel.setTransactionAmount(olaVO.getBalance());

        // From Account Id
        ledgerModel.setAccountId(fromAccountId);

        // To Account Id
        ledgerModel.setToAccountId(toAccountId);

        if (olaVO.getAuthCode() != null && !olaVO.getAuthCode().equals(""))
            ledgerModel.setAuthCode(olaVO.getAuthCode());
        else
            ledgerModel.setAuthCode(AuthCodeGenerator.getAuthCode());

        ledgerModel.setMicrobankTransactionCode(olaVO.getMicrobankTransactionCode());
        ledgerModel.setReasonId(olaVO.getReasonId());
        ledgerModel.setTransactionTime(new Date());
        ledgerModel.setTransactionTypeId(null);
        ledgerModel.setResponseCodeId(ResponseCodeConstants.SUCCESS);

        // From Balance After Transaction
        ledgerModel.setBalanceAfterTransaction(olaVO.getFromBalanceAfterTransaction());

        // To Balance After Transaction
        ledgerModel.setToBalanceAfterTransaction(olaVO.getToBalanceAfterTransaction());

        //set actual ledger ID to be reversed
        if (olaVO.getReasonId() != null && olaVO.getReasonId().equals(ReasonConstants.REVERSAL)) {
            ledgerModel.setReversalLedgerId(olaVO.getLedgerId());
            ledgerModel.setIsReversal(true);
        }
        try {

            logger.info("[AccountManagerImpl.makeLedgerTransaction] microbankTxcode:" + olaVO.getMicrobankTransactionCode() + " blance after tx: " + olaVO.getBalance() + " Reason ID:" + olaVO.getReasonId() + " AuthCode:" + ledgerModel.getAuthCode());

            ledgerModel = this.genericDAO.createEntity(ledgerModel);
            olaVO.setLedgerId(ledgerModel.getLedgerId());// set ledgerId in olaVO (to be used in case of reversal)
            olaVO.setAuthCode(ledgerModel.getAuthCode());// set authCode in olaVO (to be used in case of Orphan reversal)
        } catch (Exception e) {
            logger.error("[AccountManagerImpl.makeLedgerTransaction] Exception in Ledgger Tx: " + e.getMessage() + " microbankTxcode:" + olaVO.getMicrobankTransactionCode() + " Amount: " + olaVO.getBalance());
            olaVO.setResponseCode("06");
            ledgerModel.setResponseCodeId(ResponseCodeConstants.UNKNOWN_RESPONSE);
        }

        return olaVO;
    }


    /**
     * This method will be used to create Ledger Transaction Roll back entries in Ledger
     */
    public OLAVO makeLedgerRollbackEntriesRequiresNewTransaction(OLAVO olaVo) throws Exception {

        if (logger.isDebugEnabled()) {
            logger.debug(" ******* ---- createLedgerRollbackEntries() ------ ******* called ");
        }

        if (olaVo.getMicrobankTransactionCode() == null || olaVo.getMicrobankTransactionCode().equalsIgnoreCase("")) {
            throw new Exception("Transaction Code missing ..........");
        }

        try {
            LedgerModel ledgerModel = new LedgerModel();
            ledgerModel.setMicrobankTransactionCode(olaVo.getMicrobankTransactionCode());
            List<LedgerModel> ledgerList = genericDAO.findEntityByExample(ledgerModel, null);

            if (ledgerList != null) {

                LedgerModel ledgerSaveModel = null;

                for (LedgerModel ledger : ledgerList) {

                    ledgerSaveModel = new LedgerModel();

                    Long fromAccountId = ledger.getAccountId();

                    // Setting TO Account
                    Long toAccountId = ledger.getToAccountId();

                    ledgerSaveModel.setTransactionAmount(ledger.getTransactionAmount());

                    // From Account Id
                    ledgerSaveModel.setAccountId(fromAccountId);

                    // To Account Id
                    ledgerSaveModel.setToAccountId(toAccountId);

                    ledgerSaveModel.setAuthCode(ledger.getAuthCode());

                    ledgerSaveModel.setMicrobankTransactionCode(ledger.getMicrobankTransactionCode());
                    ledgerSaveModel.setReasonId(ReasonConstants.ROLLBACK_LEDGER);
                    ledgerSaveModel.setTransactionTime(new Date());
                    ledgerSaveModel.setTransactionTypeId(null);
                    ledgerSaveModel.setResponseCodeId(ResponseCodeConstants.SUCCESS);

                    Double trnsactionAmnt = ledger.getTransactionAmount();

                    // From Balance After Transaction
                    ledgerSaveModel.setBalanceAfterTransaction(ledger.getBalanceAfterTransaction() + trnsactionAmnt);

                    // To Balance After Transaction
                    ledgerSaveModel.setToBalanceAfterTransaction(ledger.getToBalanceAfterTransaction() - trnsactionAmnt);

                    this.genericDAO.createEntity(ledgerSaveModel);

                }
            }

        } catch (Exception e) {
            logger.error("[AccountManagerImpl.createLedgerRollbackEntries] Exception in Ledgger Tx: " + e.getMessage() + " microbankTxcode:" + olaVo.getMicrobankTransactionCode());
            olaVo.setResponseCode("06");
        }

        return olaVo;
    }


    /**
     *
     * @param olaVO
     * @return
     * @throws Exception
     */
    @Deprecated
    private OLAVO makeLedgerTransaction(OLAVO olaVO) throws Exception {

        // Setting From Account
        Long fromAccountId = olaVO.getAccountId();

        // Setting TO Account
        Long toAccountId = olaVO.getReceivingAccountId();

        com.inov8.integration.common.model.LedgerModel ledgerModel = new com.inov8.integration.common.model.LedgerModel();

        ledgerModel.setTransactionAmount(olaVO.getBalance());

        // From Account Id
        ledgerModel.setAccountId(fromAccountId);

        // To Account Id
        ledgerModel.setToAccountId(toAccountId);

        if (olaVO.getAuthCode() != null && !olaVO.getAuthCode().equals(""))
            ledgerModel.setAuthCode(olaVO.getAuthCode());
        else
            ledgerModel.setAuthCode(AuthCodeGenerator.getAuthCode());

        ledgerModel.setMicrobankTransactionCode(olaVO.getMicrobankTransactionCode());
        ledgerModel.setReasonId(olaVO.getReasonId());
        ledgerModel.setTransactionTime(new Date());
        ledgerModel.setTransactionTypeId(null);
        ledgerModel.setResponseCodeId(ResponseCodeConstants.SUCCESS);

        // From Balance After Transaction
        ledgerModel.setBalanceAfterTransaction(olaVO.getFromBalanceAfterTransaction());

        // To Balance After Transaction
        ledgerModel.setToBalanceAfterTransaction(olaVO.getToBalanceAfterTransaction());


        try {

            logger.info("[AccountManagerImpl.makeLedgerTransaction] microbankTxcode:" + olaVO.getMicrobankTransactionCode() + " blance after tx: " + olaVO.getBalance() + " Reason ID:" + olaVO.getReasonId() + " AuthCode:" + ledgerModel.getAuthCode());

            ledgerModel = this.genericDAO.createEntity(ledgerModel);

        } catch (Exception e) {
            logger.error("[AccountManagerImpl.makeLedgerTransaction] Exception in Ledgger Tx: " + e.getMessage() + " microbankTxcode:" + olaVO.getMicrobankTransactionCode() + " Amount: " + olaVO.getBalance());
            olaVO.setResponseCode("06");
            ledgerModel.setResponseCodeId(ResponseCodeConstants.UNKNOWN_RESPONSE);
        }

        return olaVO;
    }


    public BaseWrapper updateAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        AccountModel accountModel = (AccountModel) baseWrapper.getBasePersistableModel();
        logger.info("a/c # ( " + accountModel.getAccountNumber() + " ) The Start of [updateAccount()] at the time :: " + new Date());
        accountModel = this.accountDAO.saveOrUpdate(accountModel);
        baseWrapper.setBasePersistableModel(accountModel);
        logger.info("a/c # ( " + accountModel.getAccountNumber() + " ) The End of [updateAccount()] at the time :: " + new Date());
        return baseWrapper;
    }

    @Override
    public AccountModel getAccountModelByCNICAndMobile(String cnic, String mobileNo) {
        return accountDAO.getAccountModelByCNICAndMobile(cnic, mobileNo);
    }

    public BaseWrapper loadAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {
            AccountModel accountModel = (AccountModel) baseWrapper.getBasePersistableModel();
            logger.info("Start of AccountManagerImpl.loadAccount(baseWrapper) at Time :: " + new Date() + " for A/C #: " + accountModel.getAccountNumber());
            ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
            exampleHolder.setMatchMode(MatchMode.EXACT);
            exampleHolder.setEnableLike(Boolean.FALSE);
            exampleHolder.setIgnoreCase(Boolean.FALSE);
            List<AccountModel> accountModelList = this.accountDAO.findByExample(accountModel).getResultsetList();


            if (accountModelList != null && accountModelList.size() > 0) {
                accountModel = accountModelList.get(0);
                baseWrapper.setBasePersistableModel(accountModel);
            } else {
                baseWrapper.setBasePersistableModel(null);
            }
        } catch (Exception ex) {
            logger.error("Error in AccountManagerImpl.loadAccount(baseWrapper) :: " + ex);
        }
        return baseWrapper;
    }

    /**
     * @author AtifHu
     */
    public SearchBaseWrapper searchAccount(SearchBaseWrapper searchBaseWrapper)
            throws Exception {
        CustomList<AccountModel> list = this.accountDAO.findByExample(
                (AccountModel) searchBaseWrapper.getBasePersistableModel(),
                searchBaseWrapper.getPagingHelperModel(),
                searchBaseWrapper.getSortingOrderMap());
        searchBaseWrapper.setCustomList(list);
        return searchBaseWrapper;
    }

    public BaseWrapper loadAccountAndLock(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        logger.debug("Inside LoadAccountAndLOCKKKK****");
        try {
            AccountModel accountModel = (AccountModel) baseWrapper.getBasePersistableModel();
            ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
            exampleHolder.setMatchMode(MatchMode.EXACT);
            exampleHolder.setEnableLike(Boolean.FALSE);
//        logger.info(accountModel.getAccountNumber());
            List<AccountModel> accountModelList = this.accountDAO.findByExample(accountModel, null, null, exampleHolder).getResultsetList();
            if (accountModelList != null && accountModelList.size() > 0) {

                //get the model again from DB to lock it..versioning has been disabled needs aggressive testing

                accountModel = accountModelList.get(0);
                baseWrapper.setBasePersistableModel(accountModel);

                logger.info("[AccountManagerImpl.loadAccountAndLock] Attempting to Lock Account. AccountId: " + accountModel.getAccountId());

                baseWrapper = this.accountDAO.loadAccountAndLock(baseWrapper);

                logger.info("[AccountManagerImpl.loadAccountAndLock] Successfully acquired Lock on Account. AccountId: " + accountModel.getAccountId());


            } else {
                baseWrapper.setBasePersistableModel(null);
            }
        } catch (Exception ex) {
            logger.error("[AccountManagerImpl.loadAccountAndLock] Failed to Lock Account :: " + ex);
            //ex.printStackTrace();
            throw new FrameworkCheckedException(ex.getMessage());
        }
        logger.debug("Ending LoadAccountAndLOCKKKK****");
        return baseWrapper;
    }

    private int updateAccountBalanceByAccountId(Long accountId, Double amount, Long ledgerId, boolean isCredit) throws FrameworkCheckedException {
        int rowsUpdated = 0;
        try {
            logger.info("[AccountManagerImpl.updateAccountBalanceByAccountId] going to update Account for Ledger ID:" + ledgerId + " Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());

            accountDAO.updateAccountBalanceByAccountId(accountId, amount, ledgerId, isCredit);

        } catch (Exception ex) {
            logger.error("[AccountManagerImpl.updateAccountBalanceByAccountId] Failed update Account. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            //ex.printStackTrace();
            throw new FrameworkCheckedException(ex.getMessage());
        }

        return rowsUpdated;
    }

    public BaseWrapper loadAccountByPK(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        AccountModel accountModel = (AccountModel) baseWrapper.getBasePersistableModel();
        logger.info("a/c # ( " + accountModel.getAccountNumber() + " ) The Start of [loadAccountByPK()] at the time :: " + new Date());
        accountModel = this.accountDAO.findByPrimaryKey(accountModel.getPrimaryKey());
        baseWrapper.setBasePersistableModel(accountModel);
        logger.info("a/c # ( " + accountModel.getAccountNumber() + " ) The End of [loadAccountByPK()] at the time :: " + new Date());
        return baseWrapper;
    }

    public SearchBaseWrapper getAllAccounts(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        AccountModel accountModel = (AccountModel) searchBaseWrapper.getBasePersistableModel();
        searchBaseWrapper.setCustomList(new CustomList());
        searchBaseWrapper.getCustomList().setResultsetList(this.accountDAO.getAllAccounts(searchBaseWrapper));

//		searchBaseWrapper.setCustomList( this.accountDAO.findByExample(accountModel,null) ) ;


        return searchBaseWrapper;
    }


    public List<AccountsWithStatsListViewModel> getAllAccountsWithStats(Date date) throws FrameworkCheckedException {
//		AccountModel accountModel = (AccountModel)searchBaseWrapper.getBasePersistableModel() ;
//		searchBaseWrapper.setCustomList(new CustomList());
//		searchBaseWrapper.getCustomList().setResultsetList(this.accountDAO.getAllAccounts(searchBaseWrapper)) ;


        return this.accountDAO.getAccountBalanceStats(date);
    }


    public List<Object> getAccountStatsWithRange(Date startDate, Date endDate) throws Exception {
        List<Object> list = this.accountDAO.getAccountStatsWithRange(startDate, endDate);
//		HashMap<String,AccountsStatsRangeListViewModel> hashMap = new HashMap<String, AccountsStatsRangeListViewModel>() ;
//
//		for( AccountsStatsRangeListViewModel dailyAccount : list )
//		{
//
//			if( hashMap.get( dailyAccount.getAccountNumber()) != null  )
//			{
//
//
//			}
//			else
//				hashMap.put( dailyAccount.getAccountNumber() , dailyAccount ) ;
//
//
//
//		}
//

        return list;
    }


    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }


    public BaseWrapper saveLedgerEntry(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        LedgerModel ledgerModel = (LedgerModel) baseWrapper.getBasePersistableModel();
        ledgerModel = this.ledgerDAO.saveOrUpdate(ledgerModel);

        baseWrapper.setBasePersistableModel(ledgerModel);

        return baseWrapper;
    }

    private String verifyDailyLimitForCredit(Date transactionDateTime, Double amountToAdd, Long accountId, Long customerAccountTypeId, Long handlerId) throws FrameworkCheckedException {
        logger.info("Start of verifyDailyLimitForCredit at Time :: " + new Date());
        String responseCode = "";
        try {
            LimitModel limitModel=new LimitModel();
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.BLINK)) {
                BlinkCustomerLimitModel blinkCustomerLimitModel = this.limitManager.getBlinkCustomerLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.DAILY,customerAccountTypeId,accountId);
                if (blinkCustomerLimitModel != null) {
                    limitModel.setMaximum(Double.valueOf(blinkCustomerLimitModel.getMaximum()));
                    if (blinkCustomerLimitModel.getIsApplicable()==1) {
                        limitModel.setIsApplicable(true);
                    }
                    limitModel.setCustomerAccountTypeId(blinkCustomerLimitModel.getCustomerAccTypeId());
                }
            }else {
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

    private String verifyMonthlyLimitForCredit(Date transactionDateTime, Double amountToAdd, Long accountId, Long customerAccountTypeId, Long handlerId) throws FrameworkCheckedException {
        String responseCode = "";
        try {
            LimitModel limitModel=new LimitModel();
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.BLINK)) {
                BlinkCustomerLimitModel blinkCustomerLimitModel = this.limitManager.getBlinkCustomerLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.MONTHLY,customerAccountTypeId,accountId);
                if (blinkCustomerLimitModel != null) {
                    limitModel.setMaximum(Double.valueOf(blinkCustomerLimitModel.getMaximum()));
                    if (blinkCustomerLimitModel.getIsApplicable()==1) {
                        limitModel.setIsApplicable(true);
                    }
                    limitModel.setCustomerAccountTypeId(blinkCustomerLimitModel.getCustomerAccTypeId());
                }
            }else {

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

    private String verifyYearlyLimitForCredit(Date transactionDateTime, Double amountToAdd, Long accountId, Long customerAccountTypeId, Long handlerId) throws FrameworkCheckedException {
        return "00";
		/*String responseCode = "";
		try{
			LimitModel limitModel = this.limitManager.getLimitByTransactionType(TransactionTypeConstants.CREDIT,LimitTypeConstants.YEARLY,customerAccountTypeId);
			if(limitModel != null){
				if(limitModel.getIsApplicable() && limitModel.getMaximum() != null){
					Calendar startCalendar = Calendar.getInstance();
					startCalendar.setTime(transactionDateTime);
					startCalendar.set(Calendar.DAY_OF_MONTH, 1);
					startCalendar.set(Calendar.MONTH,0);
					PortalDateUtils.resetTime(startCalendar);
					Date startDate = startCalendar.getTime();
					Double consumedBalance = ledgerDAO.getConsumedBalanceByDateRange(accountId,TransactionTypeConstants.CREDIT, startDate, transactionDateTime, handlerId);
					if(consumedBalance != null){
						if(consumedBalance + amountToAdd > limitModel.getMaximum()){
							responseCode = "13";// Your entered amount will exceed the customer's Maximum transaction Credit Limit per Year, please try again.
							logger.error("Your entered amount will exceed the customer's Maximum transaction Credit Limit per Year, please try again.");
						}else{
							responseCode = "00";//Success Message
						}
					}
				}else{
					responseCode = "00"; //Success Message when limit is not applicable
				}
			}else{
				responseCode = "12"; // No Limit is defined for this data (Yearly Limit for credit).
				logger.error("No Limit is defined for this data (Yearly Limit for credit.");
			}
		}catch(Exception ex){
			logger.error("Error in AccountManagerImpl.verifyYearlyLimitForCredit() :: " + ex.getMessage() + " :: Exception " + ex);
			responseCode = "27";
		}
		return responseCode;*/
    }

    private String verifyMaximumBalanceLimitForCredit(Double amountToAdd, String payingAccountNo, Long customerAccountTypeId) throws FrameworkCheckedException {
        String responseCode = "";
        try {
            AccountModel accountModel = new AccountModel();
            accountModel.setAccountNumber(EncryptionUtil.encryptAccountNo(payingAccountNo));

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(accountModel);

            baseWrapper = this.loadAccount(baseWrapper);
            accountModel = (AccountModel) baseWrapper.getBasePersistableModel();

            if (accountModel == null) {
                responseCode = "03";
                return responseCode;

            } else if (accountModel.getStatusId().longValue() == StatusConstants.INACTIVE.longValue()) {
                responseCode = "02";
                return responseCode;
            }

//			Double accountBalance = Double.parseDouble(EncryptionUtil.decryptPin(accountModel.getBalance())) ;
            Double accountBalance = Double.parseDouble(accountModel.getBalance());
            LimitModel limitModel=new LimitModel();
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.BLINK)) {
                BlinkCustomerLimitModel blinkCustomerLimitModel = this.limitManager.getBlinkCustomerLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.MAXIMUM,customerAccountTypeId,accountModel.getAccountId());
                if (blinkCustomerLimitModel != null) {
                    limitModel.setMaximum(Double.valueOf(blinkCustomerLimitModel.getMaximum()));
                    if (blinkCustomerLimitModel.getIsApplicable()==1) {
                        limitModel.setIsApplicable(true);
                    }
                    limitModel.setCustomerAccountTypeId(blinkCustomerLimitModel.getCustomerAccTypeId());
                }
            }else {
                 limitModel = this.limitManager.getLimitByTransactionType(TransactionTypeConstants.CREDIT, LimitTypeConstants.MAXIMUM, customerAccountTypeId);

            }
            if (limitModel != null) {
                if (limitModel.getIsApplicable() && limitModel.getMaximum() != null) {
                    if (accountBalance + amountToAdd > limitModel.getMaximum()) {
                        responseCode = "15";// Your entered amount will exceed the customer's Maximum Account Balance , please try again.
                        logger.error("Your entered amount will exceed the customer's Maximum Account Balance , please try again.");
                    } else {
                        responseCode = "00";//Success Message
                    }
                } else {
                    responseCode = "00"; //Success Message when limit is not applicable
                }
            } else {
                responseCode = "14"; // No Limit is defined for this data (Maximum amount Limit for credit in account).
                logger.error("No Limit is defined for this data (Maximum amount Limit for credit in account.");
            }
        } catch (Exception ex) {
            logger.error("Error in AccountManagerImpl.maximumBalanceLimitForCredit() :: " + ex.getMessage() + " :: Exception " + ex);
            responseCode = "28";
        }
        return responseCode;
    }

    private String verifyDailyLimitForDebit(Date transactionDateTime, Double amountToAdd, Long accountId, Long customerAccountTypeId, Long handlerId) throws FrameworkCheckedException {

        String responseCode = "";
        LimitModel limitModel = new LimitModel();
        try {
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.BLINK)) {
                BlinkCustomerLimitModel blinkCustomerLimitModel = this.limitManager.getBlinkCustomerLimitByTransactionType(TransactionTypeConstants.DEBIT, LimitTypeConstants.DAILY,customerAccountTypeId,accountId);
                if (blinkCustomerLimitModel != null) {

                    limitModel.setMaximum(Double.valueOf(blinkCustomerLimitModel.getMaximum()));
                    if (blinkCustomerLimitModel.getIsApplicable()==1) {
                        limitModel.setIsApplicable(true);
                    }
                    limitModel.setCustomerAccountTypeId(blinkCustomerLimitModel.getCustomerAccTypeId());
                }
            } else {
                   limitModel = this.limitManager.getLimitByTransactionType(TransactionTypeConstants.DEBIT, LimitTypeConstants.DAILY, customerAccountTypeId);
            }
            if (limitModel != null) {
                if (limitModel.getIsApplicable() && limitModel.getMaximum() != null) {
                    Double consumedBalance = ledgerDAO.getDailyConsumedBalance(accountId, TransactionTypeConstants.DEBIT, transactionDateTime, handlerId);
                    if (consumedBalance != null) {
                        if (consumedBalance + amountToAdd > limitModel.getMaximum()) {
                            responseCode = "17"; // Your entered amount will exceed your Maximum Debit transaction Limit per Day, please try again
                            logger.error("Your entered amount will exceed your Maximum Debit transaction Limit per Day, please try again.");
                        } else {
                            responseCode = "00"; //Success Message
                        }
                    }
                } else {
                    responseCode = "00"; //Success Message when limit is not applicable
                }
            } else {
                responseCode = "16"; // No Limit is defined for this data (Daily Limit for Debit).
                logger.error("No Limit is defined for this data (Daily Limit for Debit) : customerAccountTypeId " + customerAccountTypeId);
            }
        } catch (Exception e) {
            logger.error("Error in AccountManagerImpl.verifyDailyLimitForDebit() :: " + e.getMessage() + " :: Exception " + e);
            responseCode = "22";
        }
        return responseCode;
    }

    private String verifyMonthlyLimitForDebit(Date transactionDateTime, Double amountToAdd, Long accountId, Long customerAccountTypeId, Long handlerId) throws FrameworkCheckedException {
        String responseCode = "";
        LimitModel limitModel=new LimitModel();
        try {
            if (customerAccountTypeId.equals(CustomerAccountTypeConstants.BLINK)) {
                BlinkCustomerLimitModel blinkCustomerLimitModel = this.limitManager.getBlinkCustomerLimitByTransactionType(TransactionTypeConstants.DEBIT, LimitTypeConstants.MONTHLY,customerAccountTypeId,accountId);
                if (blinkCustomerLimitModel != null) {
                    limitModel.setMaximum(Double.valueOf(blinkCustomerLimitModel.getMaximum()));
                    if (blinkCustomerLimitModel.getIsApplicable()==1) {
                        limitModel.setIsApplicable(true);
                    }
                    limitModel.setCustomerAccountTypeId(blinkCustomerLimitModel.getCustomerAccTypeId());
                }
            }else {
                 limitModel = this.limitManager.getLimitByTransactionType(TransactionTypeConstants.DEBIT, LimitTypeConstants.MONTHLY, customerAccountTypeId);
            }
            if (limitModel != null) {
                if (limitModel.getIsApplicable() && limitModel.getMaximum() != null) {
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(transactionDateTime);
                    startCalendar.set(Calendar.DAY_OF_MONTH, 1);
                    PortalDateUtils.resetTime(startCalendar);
                    Date startDate = startCalendar.getTime();
                    Double consumedBalance = ledgerDAO.getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.DEBIT, startDate, transactionDateTime, handlerId);
                    if (consumedBalance != null) {
                        if (consumedBalance + amountToAdd > limitModel.getMaximum()) {
                            responseCode = "19";// Your entered amount will exceed your Maximum Debit transaction Limit per Month, please enter amount again
                            logger.error("Your entered amount will exceed your Maximum Debit transaction Limit per Month, please enter amount again.");
                        } else {
                            responseCode = "00";//Success Message
                        }
                    }
                } else {
                    responseCode = "00"; //Success Message when limit is not applicable
                }
            } else {
                responseCode = "18"; // No Limit is defined for this data (Monthly Limit for Debit).
                logger.error("No Limit is defined for this data (Monthly Limit for Debit). customerAccountTypeId " + customerAccountTypeId);
            }
        } catch (Exception ex) {
            logger.error("Error in AccountManagerImpl.verifyMonthlyLimitForDebit() :: " + ex.getMessage() + " :: Exception " + ex);
            responseCode = "23";
        }
        return responseCode;
    }

    private String verifyYearlyLimitForDebit(Date transactionDateTime, Double amountToAdd, Long accountId, Long customerAccountTypeId, Long handlerId) throws FrameworkCheckedException {
        return "00";
        //by passing yearly limit return


		/*String responseCode = "";
		try{
			LimitModel limitModel = this.limitManager.getLimitByTransactionType(TransactionTypeConstants.DEBIT,LimitTypeConstants.YEARLY,customerAccountTypeId);
			if(limitModel != null){
				if(limitModel.getIsApplicable() && limitModel.getMaximum() != null){
					Calendar startCalendar = Calendar.getInstance();
					startCalendar.setTime(transactionDateTime);
					startCalendar.set(Calendar.DAY_OF_MONTH, 1);
					startCalendar.set(Calendar.MONTH,0);
					PortalDateUtils.resetTime(startCalendar);
					Date startDate = startCalendar.getTime();
					Double consumedBalance = ledgerDAO.getConsumedBalanceByDateRange(accountId,TransactionTypeConstants.DEBIT, startDate, transactionDateTime, handlerId);
					if(consumedBalance != null){
						if(consumedBalance + amountToAdd > limitModel.getMaximum()){
							responseCode = "21";// entered amount will exceed your Maximum Debit transaction Limit per Year, please try again
							logger.error("entered amount will exceed your Maximum Debit transaction Limit per Year, please try again.");
						}else{
							responseCode = "00";//Success Message
						}
					}
				}else{
					responseCode = "00"; //Success Message when limit is not applicable
				}
			}else{
				responseCode = "20"; // No Limit is defined for this data (Yearly Limit for Debit).
				logger.error("No Limit is defined for this data (Yearly Limit for Debit). customerAccountTypeId "+customerAccountTypeId);
			}
		}catch(Exception ex){
			logger.error("Error in AccountManagerImpl.verifyYearlyLimitForDebit() :: " + ex.getMessage() + " :: Exception " + ex);
			responseCode = "24";
		}
		return responseCode;*/
    }

    public OLAVO verifyWalkinCustomerThroughputLimits(OLAVO olaVO) throws Exception {

        OLAVO resultOLAVO = new OLAVO();
        Long accountId = null;

        AccountModel accountModel = new AccountModel();
        AccountHolderModel accountHolderModel = new AccountHolderModel();
        Map responseCodeMap = new HashMap(5);
        try {
            accountHolderModel.setCnic(olaVO.getCnic());
            accountModel.setAccountHolderIdAccountHolderModel(accountHolderModel);
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(accountModel);
            baseWrapper = this.loadAccount(baseWrapper);
            accountModel = (AccountModel) baseWrapper.getBasePersistableModel();

            logger.info("[AccountManagerImpl.verifyWalkinCustomerThroughputLimits] CNIC:" + olaVO.getCnic() + " Trx Type ID:" + olaVO.getTransactionTypeId());

            if (accountModel == null) {
                logger.error("[AccountManagerImpl.verifyWalkinCustomerThroughputLimits] AccountModel is null. Walkin Customer CNIC: " + olaVO.getCnic());
                responseCodeMap.put(TransactionTypeConstants.KEY_RESPONSE_CODE, "06"); // General Error
                resultOLAVO.setResponseCodeMap(responseCodeMap);
                return resultOLAVO;
            }

            if (olaVO.getCheckIfLimitApplicable() == null || olaVO.getCheckIfLimitApplicable()) {
                responseCodeMap = verifyMonthlyThroughputLimit(Long.parseLong(olaVO.getTransactionTypeId()),
                        olaVO.getTransactionDateTime(),
                        olaVO.getBalance(),
                        accountModel.getAccountId(),
                        olaVO.getCustomerAccountTypeId(),
                        olaVO.isExcludeInProcessTx());
            } else {
                responseCodeMap.put(TransactionTypeConstants.KEY_RESPONSE_CODE, "00");
                responseCodeMap.put(TransactionTypeConstants.KEY_BVS_VAL, false);
            }
            resultOLAVO.setResponseCodeMap(responseCodeMap);
        } catch (Exception e) {
            logger.error("[AccountManagerImpl.verifyWalkinCustomerThroughputLimits] Exception: " + e.getMessage() + " for CNIC:" + olaVO.getCnic() + " Trx Type ID:" + olaVO.getTransactionTypeId());
        }
        return resultOLAVO;
    }

    private Map<String, Object> verifyMonthlyThroughputLimit(Long transactionTypeId, Date transactionDateTime, Double amountToAdd,
                                                             Long accountId, Long customerAccountTypeId, boolean excludeInProcessTx) throws FrameworkCheckedException {
        Map<String, Object> responseMap = new HashMap<>();
        String responseCode = "";
        boolean isBvsApplied = false;

        try {
            LimitModel nonBvsLimitModel = this.limitManager.getLimitByTransactionType(transactionTypeId, LimitTypeConstants.THROUGHPUT, customerAccountTypeId);
            LimitModel bvsLimitModel = this.limitManager.getLimitByTransactionType(transactionTypeId, LimitTypeConstants.BVS_THROUGHPUT, customerAccountTypeId);

            if (bvsLimitModel == null) {
                responseCode = "33"; // Monthly BVS Limit not defined in the system.
            } else if (nonBvsLimitModel != null) {
                Double nonBvsmaxLimit = CommonUtils.getDoubleOrDefaultValue(nonBvsLimitModel.getMaximum());
                Double bvsmaxLimit = CommonUtils.getDoubleOrDefaultValue(bvsLimitModel.getMaximum());

                if (!bvsLimitModel.getIsApplicable()) {
                    bvsmaxLimit = 0.0;
                }
                if (!nonBvsLimitModel.getIsApplicable()) {
                    nonBvsmaxLimit = 0.0;
                }

                Double totalMaxLimit = nonBvsmaxLimit + bvsmaxLimit;

                if (!bvsLimitModel.getIsApplicable() && !nonBvsLimitModel.getIsApplicable()) {
                    isBvsApplied = false;
                    responseCode = "00";// Both Limits are not applicable
                } else {
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(transactionDateTime);
                    startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                    PortalDateUtils.resetTime(startCalendar);
                    Date startDate = startCalendar.getTime();
                    Double totalConsumedBalance = ledgerDAO.getWalkinCustomerConsumedBalanceByDateRange(accountId, transactionTypeId, startDate,
                            transactionDateTime, excludeInProcessTx);
                    if (totalConsumedBalance + amountToAdd > nonBvsmaxLimit) {
                        //NON-BVS limit exhausted. Now check if amount lies under sum total of both consumed balances
                        //in this case, remaining of both, Non-BVS and BVS limits is used. Therefore considering it a BVS
                        if (totalConsumedBalance + amountToAdd > totalMaxLimit) {
                            if (transactionTypeId.longValue() == TransactionTypeConstants.CREDIT.longValue()) {
                                responseCode = "30";//Your entered amount will exceed your Maximum Credit transaction Limit per Month, please try again
                            } else {
                                responseCode = "32";//Your entered amount will exceed your Maximum Debit transaction Limit per Month, please try again
                            }
                        } else {//BVS Limit is used for this amount
                            isBvsApplied = true;
                            responseCode = "00";//Success Message
                        }
                    } else {//NON-BVS Limit is used for this amount
                        isBvsApplied = false;
                        responseCode = "00";
                    }
                }
            } else {
                responseCode = "29"; // Monthly Cash Payment Limit not defined in the system, please try again
            }

        } catch (Exception ex) {
            logger.error("Error in AccountManagerImpl.verifyMonthlyThroughputLimit() :: " + ex.getMessage() + " :: Exception " + ex);
            responseCode = "31";
        } finally {
            responseMap.put(TransactionTypeConstants.KEY_RESPONSE_CODE, responseCode);
            responseMap.put(TransactionTypeConstants.KEY_BVS_VAL, isBvsApplied);
        }

        return responseMap;
    }

    public OLAVO saveWalkinCustomerLedgerEntry(OLAVO olaVO) throws Exception {

        try {
            String cnic = olaVO.getCnic();

            //		AccountModel accountModel = accountDAO.findByExample(new AccountHolderModel(cnic), null);
//			AccountModel accountModel = accountDAO.getAccountModelByCNIC( EncryptionUtil.encryptPin(olaVO.getCnic()) );
            AccountModel accountModel = accountDAO.getAccountModelByCNIC(olaVO.getCnic());

            if (accountModel != null) {

                LedgerModel ledgerModel = new LedgerModel();
                ledgerModel.setAccountId(accountModel.getAccountId());
                ledgerModel.setAuthCode(AuthCodeGenerator.getAuthCode());
                ledgerModel.setMicrobankTransactionCode(olaVO.getMicrobankTransactionCode());
                ledgerModel.setReasonId(olaVO.getReasonId());
                ledgerModel.setTransactionTime(new Date());
                ledgerModel.setTransactionAmount(olaVO.getBalance());
                ledgerModel.setBalanceAfterTransaction(0d);
                ledgerModel.setToBalanceAfterTransaction(0d);
                ledgerModel.setResponseCodeId(ResponseCodeConstants.SUCCESS);
                if (olaVO.getTransactionTypeId() != null && olaVO.getTransactionTypeId().equals(TransactionTypeConstantsInterface.OLA_DEBIT)) {
                    ledgerModel.setAccountId(accountModel.getAccountId());
                    ledgerModel.setToAccountId(PoolAccountConstantsInterface.WALK_IN_SUNDRY_ACCOUNT_ID);
                } else {
                    ledgerModel.setToAccountId(accountModel.getAccountId());
                    ledgerModel.setAccountId(PoolAccountConstantsInterface.WALK_IN_SUNDRY_ACCOUNT_ID);
                }

                if (olaVO.getExcludeLimit() != null && olaVO.getExcludeLimit()) {
                    ledgerModel.setExcludeLimit(true);
                } else {
                    ledgerModel.setExcludeLimit(false);
                }

//				if(olaVO.isBvs()){
//					ledgerModel.setIsBvs(true);
//				}

                ledgerModel = this.genericDAO.createEntity(ledgerModel);

                olaVO.setResponseCode("00");
                olaVO.setAuthCode(ledgerModel.getAuthCode());

                logger.info("[AccountManagerImpl.makeTx] microbankTxcode:" + olaVO.getMicrobankTransactionCode() + " CNIC:" + olaVO.getCnic() + " Reason ID:" + olaVO.getReasonId() + " AuthCode:" + ledgerModel.getAuthCode() + " Trx Type ID:" + olaVO.getTransactionTypeId());


            } else {
                olaVO.setResponseCode("03");

            }
        } catch (Exception ex) {
            logger.error("[AccountManagerImpl.makeTx] Exception: " + ex.getMessage() + " microbankTxcode:" + olaVO.getMicrobankTransactionCode() + " Reason ID:" + olaVO.getReasonId() + " cnic:" + olaVO.getCnic());
//			ex.printStackTrace();
            olaVO.setResponseCode("03");
        }

        return olaVO;
    }

    public AccountModel getAccountModelByCNIC(String cnic) throws Exception {
        AccountModel accountModel = null;
        if (cnic != null && !cnic.equals("")) {
//			accountModel = accountDAO.getAccountModelByCNIC(EncryptionUtil.encryptPin(cnic) );
            accountModel = accountDAO.getAccountModelByCNIC(cnic);
        }

        return accountModel;

    }

    @Override
    public List<AccountModel> getAccountModelListByCNICs(List<String> cnicList) throws FrameworkCheckedException {
        List<AccountModel> accountModelList = null;
        List<String> encryptedCnisList = new ArrayList<String>();
        for (String cnic : cnicList) {
            if (cnic != null && !"".equals(cnic)) {
//					encryptedCnisList.add(EncryptionUtil.encryptPin(cnic));
                encryptedCnisList.add(cnic);
            }
        }

        accountModelList = accountDAO.getAccountModelListByCNICs(encryptedCnisList);
        return accountModelList;
    }

    @Override
    public List<LabelValueBean> loadAccountIdsAndTitles(Long olaCustomerAccountTypeId) throws FrameworkCheckedException {
        return accountDAO.loadAccountIdsAndTitles(olaCustomerAccountTypeId);
    }

    @Override
    public OlaCustomerAccountTypeModel findAccountTypeById(Long accountTypeId) throws FrameworkCheckedException {
        return olaCustomerAccountTypeDao.findByPrimaryKey(accountTypeId);
    }

    @Override
    public SearchBaseWrapper searchOlaCustomerAccountTypes(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = (OlaCustomerAccountTypeModel) wrapper.getBasePersistableModel();
        CustomList<OlaCustomerAccountTypeModel> customList = null;

        if (olaCustomerAccountTypeModel.getPrimaryKey() != null) {
            OlaCustomerAccountTypeModel model = olaCustomerAccountTypeDao.findByPrimaryKey(olaCustomerAccountTypeModel.getPrimaryKey());
            customList = new CustomList<>(new ArrayList<OlaCustomerAccountTypeModel>());
            customList.getResultsetList().add(model);
            wrapper.setCustomList(customList);
        } else {
            customList = olaCustomerAccountTypeDao.findByExample(olaCustomerAccountTypeModel, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap());
            wrapper.setCustomList(customList);
        }
        return wrapper;
    }

    @Override
    public SearchBaseWrapper searchAccountTypeView(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
        AccountTypeViewModel accountTypeViewModel = (AccountTypeViewModel) wrapper.getBasePersistableModel();
        CustomList<AccountTypeViewModel> customList = null;

        if (accountTypeViewModel.getPrimaryKey() != null) {
            AccountTypeViewModel model = accountTypeViewDao.findByPrimaryKey(accountTypeViewModel.getPrimaryKey());
            customList = new CustomList<>(new ArrayList<AccountTypeViewModel>());
            customList.getResultsetList().add(model);
            wrapper.setCustomList(customList);
        } else {
            customList = accountTypeViewDao.findByExample(accountTypeViewModel, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap());
            wrapper.setCustomList(customList);
        }
        return wrapper;
    }

    @Override
    public List<OlaCustomerAccountTypeModel> searchParentOlaCustomerAccountTypes(Long... customerAccountTypeIdsToExclude) throws FrameworkCheckedException {
        return olaCustomerAccountTypeDao.searchParentOlaCustomerAccountTypes(customerAccountTypeIdsToExclude);
    }

    @Override
    public List<OlaCustomerAccountTypeModel> searchSubtypesAndLimits(Long parentAccountTypeId) throws FrameworkCheckedException {
        return olaCustomerAccountTypeDao.searchSubtypesAndLimits(parentAccountTypeId);
    }

    @Override
    public boolean hasActiveAccountSubtypes(Long parentAccountTypeId) throws FrameworkCheckedException {
        OlaCustomerAccountTypeModel model = new OlaCustomerAccountTypeModel();
        model.setParentAccountTypeId(parentAccountTypeId);
        model.setActive(Boolean.TRUE);
        int count = olaCustomerAccountTypeDao.countByExample(model, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
        return count > 0;
    }

    @Override
    public boolean isAssociatedWithAgentCustomerOrHandler(Long accountTypeId, Long parentAccountTypeId, boolean isCustomerAccountType) {
        boolean isAssociated = false;
        int customerCount = 0;
        int agentCount = 0;
        int handlerCount = 0;
        if (isCustomerAccountType) {
            CustomerModel customerModel = new CustomerModel();
            customerModel.setCustomerAccountTypeId(accountTypeId);
            customerCount = customerManager.countByExample(customerModel, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
        } else {
            if (parentAccountTypeId == null)//Parent/Agent Type case
            {
                RetailerContactModel agentModel = new RetailerContactModel();//turab
                OlaCustomerAccountTypeModel accountTypeModel = new OlaCustomerAccountTypeModel(accountTypeId);
                agentModel.setOlaCustomerAccountTypeModel(accountTypeModel);
                agentCount = retailerContactManager.countByExample(agentModel, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
            } else {
                HandlerModel handlerModel = new HandlerModel();
                handlerModel.setAccountTypeId(accountTypeId);


                handlerCount = handlerManager.countByExample(handlerModel, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
            }
        }

        if (customerCount > 0 || agentCount > 0 || handlerCount > 0) {
            isAssociated = true;
        }
        return isAssociated;
    }

    @Override
    public int countByExample(OlaCustomerAccountTypeModel model, ExampleConfigHolderModel exampleConfigHolderModel) throws FrameworkCheckedException {
        int count = olaCustomerAccountTypeDao.countByExample(model, exampleConfigHolderModel);
        return count;
    }

    @Override
    public BaseWrapper saveOlaCustomerAccountType(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        OlaCustomerAccountTypeModel model = (OlaCustomerAccountTypeModel) baseWrapper.getBasePersistableModel();
        model = olaCustomerAccountTypeDao.saveOrUpdate(model);

        StakeholderBankInfoModel stakeholderBankInfoModel;
        if (baseWrapper.getObject("STAKE_HOLDER_BANK_INFO_MODEL_CB") != null) {
            stakeholderBankInfoModel = (StakeholderBankInfoModel) baseWrapper.getObject("STAKE_HOLDER_BANK_INFO_MODEL_CB");
            stakeholderBankInfoModel.setCustomerAccountTypeIdModel(model);
            stakeholderBankInfoDAO.saveOrUpdate(stakeholderBankInfoModel);
	    	/*if(!stakeholderBankInfoDAO.isAccountTypeUnique(stakeholderBankInfoModel.getAccountNo(), model.getCustomerAccountTypeId()))
	    	{
	    		throw new FrameworkCheckedException("Core-Bank-Account-Duplicate-Linkage");
	    	}*/
        }
        actionLogModel.setCustomField11(model.getName());
        this.actionLogManager.completeActionLog(actionLogModel);
        return baseWrapper;
    }

    private boolean isLimitApplicable(OLAVO olaVO, boolean isCredit, boolean isHandler) {
        logger.info("Product ID ( " + olaVO.getProductId() + " ) The Start of [AccountManagerImpl.isLimitApplicable()] at the time :: " + new Date());
        LimitRuleModel model = new LimitRuleModel();
        model.setProductId(olaVO.getProductId());

        if (isHandler) {
            model.setAccountTypeId(olaVO.getHandlerAccountTypeId());
        } else {
            model.setAccountTypeId(olaVO.getCustomerAccountTypeId());
        }

        if (isCredit) {
            model.setSegmentId(olaVO.getToSegmentId());
            model.setSegmentId(olaVO.getFromSegmentId());
        } else {
        }
        Boolean isValid = limitRuleDAO.isLimitApplicable(model);
        logger.info("Product ID ( " + olaVO.getProductId() + " ) Total Time taken to execute [AccountManagerImpl.isLimitApplicable()] :: " + new Date());
        return isValid;
    }


    @Override
    public SearchBaseWrapper getAllHanlderAccountTypes()
            throws FrameworkCheckedException {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        CustomList<OlaCustomerAccountTypeModel> customList = new CustomList<>(olaCustomerAccountTypeDao.loadHandlerACTypes());
        searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    @Override
    public SearchBaseWrapper getAllActiveCustomerAccountTypes() throws FrameworkCheckedException {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        CustomList<OlaCustomerAccountTypeModel> customList = new CustomList<>(olaCustomerAccountTypeDao.getAllActiveCustomerAccountTypes());
        searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    @Override
    public List<OlaCustomerAccountTypeModel> getAllActiveCustomerAccountTypesGrouped() throws FrameworkCheckedException {
        SearchBaseWrapper searchBaseWrapper = getAllActiveCustomerAccountTypes();

        return sortAccountTypes(searchBaseWrapper.getCustomList().getResultsetList());
    }

    @Override
    public SearchBaseWrapper getParentOlaCustomerAccountTypes(SearchBaseWrapper searchBaseWrapper)
            throws FrameworkCheckedException {

        List<ProductLimitRuleModel> list = (List<ProductLimitRuleModel>) searchBaseWrapper.getObject("PRODUCT-LIMIT-RULE-MODEL-LIST");

        Long[] accountTypeIds = new Long[list.size()];
        int count = 0;

        for (ProductLimitRuleModel model : list) {
            accountTypeIds[count] = model.getAccountTypeId();
            count++;
        }

        List<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModellist = olaCustomerAccountTypeDao.getParentOlaCustomerAccountTypes(accountTypeIds);

        searchBaseWrapper.setCustomList(new CustomList<>(olaCustomerAccountTypeModellist));
        return searchBaseWrapper;
    }

    private List<OlaCustomerAccountTypeModel> sortAccountTypes(List<OlaCustomerAccountTypeModel> list) {

        List<OlaCustomerAccountTypeModel> parentList = new ArrayList<>();
        List<OlaCustomerAccountTypeModel> childList = new ArrayList<>();

        for (OlaCustomerAccountTypeModel model : list) {
            if (null == model.getParentAccountTypeId()) {
                parentList.add(model);
            } else {
                childList.add(model);
            }
        }

        list = new ArrayList<OlaCustomerAccountTypeModel>();
        for (OlaCustomerAccountTypeModel parent : parentList) {
            list.add(parent);

            for (OlaCustomerAccountTypeModel child : childList) {
                Long parentAccountType = child.getParentAccountTypeId();
                if (parentAccountType == null || parentAccountType != parent.getCustomerAccountTypeId().longValue())
                    continue;

                child.setName("--- " + child.getName());
                list.add(child);
            }
        }
        return list;
    }


    @Override
    public SearchBaseWrapper getAllAgentAccountTypes() throws FrameworkCheckedException {
        OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
        olaCustomerAccountTypeModel.setIsCustomerAccountType(Boolean.FALSE);

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

        searchBaseWrapper.setCustomList(olaCustomerAccountTypeDao.findByExample(olaCustomerAccountTypeModel, ""));

        return searchBaseWrapper;
    }

    @Override
    public Map<String, Object> getAllLimits(Date transactionDateTime, Long customerAccountTypeId, Long handlerId, String cnic, Long limitType) throws FrameworkCheckedException {

        Map<String, Object> limitsMap = new HashMap<>();
        Map<String, String> returnedMap = new HashMap<>();

        List<LimitModel> limitModelList = limitManager.getLimitsByCustomerAccountType(customerAccountTypeId);

        Long accountId = accountDAO.getAccountIdByCnic(cnic, customerAccountTypeId);

        for (LimitModel model : limitModelList) {
            returnedMap = this.getRemainingLimit(model, accountId, handlerId, transactionDateTime);
            limitsMap.putAll(returnedMap);
        }


        return limitsMap;
    }

    public Map<String, String> getRemainingLimit(LimitModel limitModel, Long accountId, Long handlerId, Date transactionDateTime) {
        Map<String, String> remainingLimitMap = new HashMap<>();
        Double remainingAmount;
        String limitType = null;
        Double consumedBalance = 0.0;
        Long limitTypeId = limitModel.getLimitTypeId().longValue();

        Calendar calendar = Calendar.getInstance();
        Date monthlyStartDate = this.setDayAndTimeToStartOfMonth(calendar);
        Date yearlyStartDate = this.setMonthDayAndTimeToStartOfYear(calendar);

        try {

            if (limitModel.getTransactionTypeId().longValue() == TransactionTypeConstants.CREDIT) {
                if (limitTypeId.longValue() == LimitTypeConstants.DAILY) {
                    limitType = CommandFieldConstants.KEY_DAILY_CREDIT_LIMIT;
                    consumedBalance = ledgerDAO.getDailyConsumedBalance(accountId, limitModel.getTransactionTypeId(), transactionDateTime, handlerId);
                } else if (limitTypeId.longValue() == LimitTypeConstants.MONTHLY) {
                    limitType = CommandFieldConstants.KEY_MONTHLY_CREDIT_LIMIT;
                    consumedBalance = ledgerDAO.getConsumedBalanceByDateRange(accountId, limitModel.getTransactionTypeId(), monthlyStartDate, new Date(), handlerId);
                } else if (limitTypeId.longValue() == LimitTypeConstants.YEARLY) {
                    limitType = CommandFieldConstants.KEY_YEARLY_CREDIT_LIMIT;
                    //by passing yearly limit check
                    //consumedBalance = ledgerDAO.getConsumedBalanceByDateRange(accountId, limitModel.getTransactionTypeId(),  yearlyStartDate,new Date(), handlerId);
                }
            } else {
                if (limitTypeId.longValue() == LimitTypeConstants.DAILY) {
                    limitType = CommandFieldConstants.KEY_DAILY_DEBIT_LIMIT;
                    consumedBalance = ledgerDAO.getDailyConsumedBalance(accountId, limitModel.getTransactionTypeId(), transactionDateTime, handlerId);

                } else if (limitTypeId.longValue() == LimitTypeConstants.MONTHLY) {
                    limitType = CommandFieldConstants.KEY_MONTHLY_DEBIT_LIMIT;
                    consumedBalance = ledgerDAO.getConsumedBalanceByDateRange(accountId, limitModel.getTransactionTypeId(), monthlyStartDate, new Date(), handlerId);

                } else if (limitTypeId.longValue() == LimitTypeConstants.YEARLY) {
                    limitType = CommandFieldConstants.KEY_YEARLY_DEBIT_LIMIT;
                    //by passing yearly limit check
                    //consumedBalance = ledgerDAO.getConsumedBalanceByDateRange(accountId, limitModel.getTransactionTypeId(),  yearlyStartDate,new Date(), handlerId);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception on loading remaining limits. Message: " + ex.getMessage());
        }

        Double max = CommonUtils.getDoubleOrDefaultValue(limitModel.getMaximum());

        if (limitModel.getIsApplicable()) {
            consumedBalance = CommonUtils.getDoubleOrDefaultValue(consumedBalance);
            remainingAmount = max - consumedBalance;
            remainingLimitMap.put(limitType, Formatter.formatNumbers(remainingAmount));
        } else {
            remainingLimitMap.put(limitType, "N/A");
        }

        return remainingLimitMap;
    }

    private Date setDayAndTimeToStartOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date setMonthDayAndTimeToStartOfYear(Calendar calendar) {
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public void setLedgerDAO(LedgerDAO ledgerDAO) {
        this.ledgerDAO = ledgerDAO;
    }


    public void setGenericDAO(GenericDao genericDAO) {
        this.genericDAO = genericDAO;
    }


    public void setLimitManager(LimitManager limitManager) {
        this.limitManager = limitManager;
    }

    public void setOlaCustomerAccountTypeDao(OlaCustomerAccountTypeDao olaCustomerAccountTypeDao) {
        this.olaCustomerAccountTypeDao = olaCustomerAccountTypeDao;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }

    public void setLimitRuleDAO(LimitRuleDAO limitRuleDAO) {
        this.limitRuleDAO = limitRuleDAO;
    }

    public void setCreditAccountQueueSender(CreditAccountQueueSender creditAccountQueueSender) {
        this.creditAccountQueueSender = creditAccountQueueSender;
    }

    public void setStakeholderBankInfoDAO(
            StakeholderBankInfoDAO stakeholderBankInfoDAO) {
        this.stakeholderBankInfoDAO = stakeholderBankInfoDAO;
    }

    public void setAccountTypeViewDao(AccountTypeViewDao accountTypeViewDao) {
        this.accountTypeViewDao = accountTypeViewDao;
    }

    public void setCustomerManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
        this.retailerContactManager = retailerContactManager;
    }

    public void setHandlerManager(HandlerManager handlerManager) {
        this.handlerManager = handlerManager;
    }

    public void setSafRepoDao(SafRepoDao safRepoDao) {
        this.safRepoDao = safRepoDao;
    }


    private void saveSafRepo(OLAVO olaVo, OLAVO _olaVO, AccountModel payingAccNoAccountModel) throws FrameworkCheckedException {

        try {

            SafRepoModel safRepoModel = new SafRepoModel();
            safRepoModel.setAccountId(payingAccNoAccountModel.getAccountId());
            safRepoModel.setCategory(_olaVO.getCategory());
            safRepoModel.setCustomerAccountTypeId(_olaVO.getCustomerAccountTypeId());
            safRepoModel.setLedgerId(_olaVO.getLedgerId());
            safRepoModel.setProductId(_olaVO.getProductId());
            ReasonModel reasonModel = new ReasonModel();
            reasonModel.setReasonId(_olaVO.getReasonId());
            safRepoModel.setReasonIdReasonModel(reasonModel);
            safRepoModel.setSegmentId(_olaVO.getFromSegmentId());
            safRepoModel.setTransactionAmount(_olaVO.getBalance());
            safRepoModel.setTransactionCode(_olaVO.getMicrobankTransactionCode());
            safRepoModel.setTransactionStatus(PortalConstants.SAF_STATUS_INITIATED);
            safRepoModel.setTransactionTime(_olaVO.getTransactionDateTime());
            OlaTransactionTypeModel transactionTypeModel = new OlaTransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(Long.valueOf(_olaVO.getTransactionTypeId()));
            safRepoModel.setTransactionTypeIdTransactionTypeModel(transactionTypeModel);
            if (ThreadLocalAppUser.getAppUserModel() != null && ThreadLocalAppUser.getAppUserModel().getAppUserId() != null) {
                safRepoModel.setCreatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
            } else {
                safRepoModel.setCreatedBy(2L);
            }
            safRepoModel.setCreatedOn(new Date());
            safRepoModel.setUpdatedOn(new Date());
            logger.info("[AccountManagerImpl.saveSafRepo] Inserting into SafRepo with Ledger Id: " + safRepoModel.getLedgerId() + "\nTransaction Code: " + safRepoModel.getTransactionCode());
            SafRepoModel model = this.genericDAO.createEntity(safRepoModel);
//			SafRepoModel model = safRepoDao.saveOrUpdate(safRepoModel);
            if (model == null) {
                logger.error("Exception occured on saving trx to SafRepoModel : " + _olaVO.getPayingAccNo());
                olaVo.setResponseCode("08");
                throw new FrameworkCheckedException(_olaVO.getResponseCode());
            }
        } catch (Exception e) {
            logger.error("Exception occured on saving trx to SafRepoModel : " + _olaVO.getPayingAccNo() + " Exception Message :: " + e.getMessage() + " Exception :: " + e);
            //e.printStackTrace();
            olaVo.setResponseCode("08");
            throw new FrameworkCheckedException(_olaVO.getResponseCode());
        }
    }

    private void saveWalletSafRepo(OLAVO olaVo, OLAVO _olaVO, AccountModel payingAccNoAccountModel) throws FrameworkCheckedException {

        try {

            WalletSafRepoModel walletSafRepoModel = new WalletSafRepoModel();
            walletSafRepoModel.setAccountId(payingAccNoAccountModel.getAccountId());
            walletSafRepoModel.setCategory(_olaVO.getCategory());
            walletSafRepoModel.setCustomerAccountTypeId(_olaVO.getCustomerAccountTypeId());
            walletSafRepoModel.setLedgerId(_olaVO.getLedgerId());
            walletSafRepoModel.setProductId(_olaVO.getProductId());
            ReasonModel reasonModel = new ReasonModel();
            reasonModel.setReasonId(_olaVO.getReasonId());
            walletSafRepoModel.setReasonIdReasonModel(reasonModel);
            walletSafRepoModel.setSegmentId(_olaVO.getFromSegmentId());
            walletSafRepoModel.setTransactionAmount(_olaVO.getBalance());
            walletSafRepoModel.setCommissionAmount(olaVo.getCommissionAmount());
            walletSafRepoModel.setTransactionProcessingAmount(olaVo.getTransactionProcessingAmount());
            walletSafRepoModel.setTotalAmount(olaVo.getTotalAmount());
            walletSafRepoModel.setTransactionCode(_olaVO.getMicrobankTransactionCode());
            walletSafRepoModel.setTransactionStatus(PortalConstants.SAF_STATUS_INITIATED);
            walletSafRepoModel.setTransactionTime(_olaVO.getTransactionDateTime());
            OlaTransactionTypeModel transactionTypeModel = new OlaTransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(Long.valueOf(_olaVO.getTransactionTypeId()));
            walletSafRepoModel.setTransactionTypeIdTransactionTypeModel(transactionTypeModel);
            walletSafRepoModel.setSenderCnic(olaVo.getCnic());
            walletSafRepoModel.setSenderMobileNumber(olaVo.getMobileNumber());
            walletSafRepoModel.setReceiverMobileNumber(olaVo.getReceiverMobileNumber());
            walletSafRepoModel.setIsComplete(0L);
            if (ThreadLocalAppUser.getAppUserModel() != null && ThreadLocalAppUser.getAppUserModel().getAppUserId() != null) {
                walletSafRepoModel.setCreatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
            } else {
                walletSafRepoModel.setCreatedBy(2L);
            }
            walletSafRepoModel.setCreatedOn(new Date());
            walletSafRepoModel.setUpdatedOn(new Date());
            logger.info("[AccountManagerImpl.saveSafRepo] Inserting into WalletSafRepoModel with Ledger Id: " + walletSafRepoModel.getLedgerId() + "\nTransaction Code: " + walletSafRepoModel.getTransactionCode());
            WalletSafRepoModel model = this.genericDAO.createEntity(walletSafRepoModel);
//			SafRepoModel model = safRepoDao.saveOrUpdate(safRepoModel);
            if (model == null) {
                logger.error("Exception occured on saving trx to WalletSafRepoModel : " + _olaVO.getPayingAccNo());
                olaVo.setResponseCode("08");
                throw new FrameworkCheckedException(_olaVO.getResponseCode());
            }
        } catch (Exception e) {
            logger.error("Exception occured on saving trx to WalletSafRepoModel : " + _olaVO.getPayingAccNo() + " Exception Message :: " + e.getMessage() + " Exception :: " + e);
            //e.printStackTrace();
            olaVo.setResponseCode("08");
            throw new FrameworkCheckedException(_olaVO.getResponseCode());
        }
    }

    @Override
    public void saveFailedOLACreditDebit(OLAVO olaVO) throws FrameworkCheckedException {
        olaVO.setStatusName(AccountCreditQueueLogModel.FAILED);

        this.createSaveAccountCreditQueueLogModel(olaVO);

    }


    @Override
    public void makeOLACreditDebitViaQueue(OLAVO olavo) throws FrameworkCheckedException {
        this.updateSafRepoStatus(olavo);
        this.pushCreditQueue(olavo);
    }

    @Override
    public void makeWalletOLACreditDebitViaQueue(OLAVO olavo) throws FrameworkCheckedException {
        this.pushCreditQueue(olavo);
    }

    @Override
    public Long getAccountIdByCustomerAccountType(String cnic, Long customerAccountTypeId) {
        return accountDAO.getAccountIdByCustomerAccountType(cnic, customerAccountTypeId);
    }

    @Override
    public AccountModel getAccountModelByCnicAndCustomerAccountTypeAndStatusId(String cnic, Long customerAccountTypeId, Long statusId) {
        return this.accountDAO.getAccountModelByCnicAndCustomerAccountTypeAndStatusId(cnic, customerAccountTypeId, statusId);
    }

    private void updateSafRepoStatus(OLAVO olavo) throws FrameworkCheckedException {
        logger.info("Going to update safeRepo Status:'Successful' for LedgerId:" + olavo.getLedgerId());
        SafRepoModel safRepoModel = new SafRepoModel();
        safRepoModel.setLedgerId(olavo.getLedgerId());
        safRepoModel.setTransactionStatus(PortalConstants.SAF_STATUS_SUCCESSFUL);
        safRepoModel.setUpdatedOn(new Date());
        safRepoDao.updateSafRepoStatus(safRepoModel);
    }

    public BaseWrapper loadTaggedAgentAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        try {

            AccountModel accountModel = (AccountModel) baseWrapper.getBasePersistableModel();

            ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
            exampleHolder.setMatchMode(MatchMode.EXACT);
            exampleHolder.setEnableLike(Boolean.FALSE);
            exampleHolder.setIgnoreCase(Boolean.FALSE);
            Criterion criteriaOne = Restrictions.ne("customerAccountTypeModel.customerAccountTypeId", 9L);
            List<AccountModel> accountModelList = this.accountDAO.findByCriteria(criteriaOne, accountModel).getResultsetList();


            if (accountModelList != null && accountModelList.size() > 0) {
                accountModel = accountModelList.get(0);
                baseWrapper.setBasePersistableModel(accountModel);
            } else {
                baseWrapper.setBasePersistableModel(null);
            }
        } catch (Exception ex) {
            logger.error("Error in AccountManagerImpl.loadTaggedAccount() :: " + ex.getMessage() + " Exception :: " + ex);
        }

        return baseWrapper;
    }

    private boolean isDebitCreditEqual(List<OLAInfo> debitList, List<OLAInfo> creditList) throws FrameworkCheckedException {
        boolean result = true;
        double totalDebit = 0.0;
        double totalCredit = 0.0;

        if (debitList != null) {
            for (OLAInfo ola : debitList) {
                totalDebit += ola.getBalance();
            }
        }

        if (creditList != null) {
            for (OLAInfo ola : creditList) {
                totalCredit += ola.getBalance();
            }
        }

        if (totalDebit != totalCredit) {
            double diff = totalDebit - totalCredit;
            diff = CommonUtils.formatAmountOneDecimal(diff);

            if (diff >= 0.01 || diff <= -0.01) {
                logger.error("Sum of debits is not equal to sum of credits. Difference=" + diff);
                result = false;
            }
        }
        return result;
    }

    @Override
    public AccountModel getLastClosedAccount(String cnic, Long customerAccountTypeId) {
        return accountDAO.getLastClosedAccountModel(cnic, customerAccountTypeId);
    }

    public void setLedgerFacade(LedgerFacade ledgerFacade) {
        this.ledgerFacade = ledgerFacade;
    }
}