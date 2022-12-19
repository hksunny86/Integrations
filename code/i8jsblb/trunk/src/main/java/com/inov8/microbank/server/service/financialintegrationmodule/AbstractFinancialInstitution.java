package com.inov8.microbank.server.service.financialintegrationmodule;

import java.io.Serializable;
import java.util.Map;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.ImplementationNotSupportedException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;

public abstract class AbstractFinancialInstitution implements FinancialInstitution, Serializable {

    boolean isVeriflyRequired;
    SwitchController switchController;
    GenericDao genericDao;


    public AbstractFinancialInstitution() {

    }


    public AbstractFinancialInstitution(SwitchController switchController, GenericDao genericDao) {
        super();
        this.switchController = switchController;
        this.genericDao = genericDao;
    }

    public SwitchWrapper verifyWalkinCustomerThroughputLimits(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper billInquiry(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper billPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper customerBillPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper debitPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper feePayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper advanceLoanPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper creditPayment(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }
    public SwitchWrapper pushBillPayment(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {

        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper billPaymentViaNADRA(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper activatePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper settleCashDepositCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper settleCashWithdrawalCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper settleP2PCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper settleAgentBillPaymentCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper changeAccountNick(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper changePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper deactivatePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper deleteAccount(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper generateOneTimePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper generatePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper markAsDeleted(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper modifyAccountInfo(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper modifyAccountInfoForBBAgents(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper resetPin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper verifyOneTimePin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper customerAccountRelationshipInquiry(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper miniStatement(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper debit(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        throw new FrameworkCheckedException("DEBIT not supported....");
    }

    public SwitchWrapper debitWithoutPin(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new FrameworkCheckedException("DEBIT not supported....");
    }


    public boolean activateDeliveryChannel(String accountType, String NIC) throws Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public boolean deActivateDeliveryChannel(String accountType, String NIC) throws Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper verifyPin(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public VeriflyBaseWrapper verifyCredentials(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException, Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public String getAccountTitle(String accountNo, String accountType, String currency, String NIC) throws Exception {
        return null;
    }


    public boolean isIVRChannelActive(String accountNo, String accountType, String currency, String NIC) throws Exception {
        return false;
    }

    public boolean isMobileChannelActive(String accountType, String NIC) throws Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper checkBalanceForAccountVerification(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper checkBalanceWithoutPin(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }


    public boolean isVeriflyLite() throws FrameworkCheckedException, Exception {
        return false;
    }

    public SwitchWrapper createAccount(SwitchWrapper wrapper) throws
            WorkFlowException, FrameworkCheckedException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper debitCreditAccount(SwitchWrapper wrapper) throws
            WorkFlowException, FrameworkCheckedException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper transferFunds(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper getAllOlaAccounts(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper updateAccountInfo(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper getAccountInfo(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public Map<Long, String> getStatusCodes() throws Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper creditTransfer(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper debitAccount(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {

        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper settleCommission(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper settleInov8Commission(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper getAllAccountsWithStats(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
        throw new FrameworkCheckedException("Transaction Not Supported...");
    }

    public SwitchWrapper getAllAccountsStatsWithRange(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
        throw new FrameworkCheckedException("Transaction Not Supported...");
    }

    public SwitchWrapper verifyPIN(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new FrameworkCheckedException("Transaction Not Supported...");
    }

    public SwitchWrapper getLedger(SwitchWrapper wrapper) throws Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper titleFetch(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper accountToCashTransaction(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper donationTransaction(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper collectionPaymentTransaction(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper donationTransactionDr(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper settleAccountToCashCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper settleCustomerRetailPaymentCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper saveWalkinCustomerLedgerEntry(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper saveWalkInLedgerEntry(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper getPhoenixTransactions(SwitchWrapper switchWrapper) throws Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper cashToCashTransaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper settleCashToCashCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper rollbackWalkinCustomer(SwitchWrapper switchWrapper) throws FrameworkCheckedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }


    public SwitchWrapper updateLedger(SwitchWrapper switchWrapper) throws WorkFlowException, FrameworkCheckedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper reverseFundTransfer(SwitchWrapper switchWrapper) throws Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper settleDonationTransactionCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper closeOLAAccount(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper bulkPaymentTransaction(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper settleBulkPaymentCommission(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper saveBulkPaymentWalkInLedgerEntry(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {

        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public AccountInfoModel getAccountInfoModelBySmartMoneyAccount(SmartMoneyAccountModel smartMoneyAccountModel, Long customerId, Long trxCodeId) throws Exception {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper changeAccountDetails(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
        throw new FrameworkCheckedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper creditCardBillPayment(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
        throw new FrameworkCheckedException("This facility is not available. Please contact your bank or call Askari Bank helpline for further information.");
    }

    public SwitchWrapper creditAccountAdvice(SwitchWrapper wrapper) throws
            WorkFlowException, FrameworkCheckedException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper sendReversalAdvice(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper sendCreditAdvice(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper settleCustomerPendingTrx(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper makeBulkDisbursmentToCustomer(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper creditBulkDisbursmentOLA(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper getAccountTitle(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper settleAccountOpeningCommission(SwitchWrapper wrapper) throws WorkFlowException, FrameworkCheckedException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public OLAVO checkDebitCreditLimitOLAVO(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper reverseP2PTransaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException, WorkFlowException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper receiveCashTransaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException, WorkFlowException, Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper bulkDisbursementFromL2SumFT(SwitchWrapper switchWrapper) throws Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper bulkDisbursementSumFT(SwitchWrapper switchWrapper) throws Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper customerWHTDeduction(SwitchWrapper switchWrapper, Long poolAccountId, Long corePoolAccountId) throws Exception {
        throw new FrameworkCheckedException("Transaction not supported....");
    }

    public SwitchWrapper sendM3SMS(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception, ImplementationNotSupportedException {
        throw new ImplementationNotSupportedException("This facility is not available. Please contact your bank or call Bank helpline for further information.");
    }
}
