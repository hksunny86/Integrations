package com.inov8.integration.webservice.controller;

import com.inov8.integration.webservice.vo.WebServiceVO;

import javax.xml.ws.WebServiceClient;
import java.lang.ref.WeakReference;

public interface WebServiceSwitchController {

    WebServiceVO verifyAccount(WebServiceVO webServiceVO);
    WebServiceVO accountOpening(WebServiceVO webServiceVO);
    WebServiceVO conventionalAccountOpening(WebServiceVO webServiceVO);
    WebServiceVO paymentInquiry(WebServiceVO webServiceVO);
    WebServiceVO paymentRequest(WebServiceVO webServiceVO);
    WebServiceVO paymentReversal(WebServiceVO webServiceVO);
    WebServiceVO otpVerification(WebServiceVO webServiceVO);
    WebServiceVO cardTagging(WebServiceVO webServiceVO);
    WebServiceVO accountLinkDelink(WebServiceVO webServiceVO);
    WebServiceVO setCardStatus(WebServiceVO webServiceVO);
    WebServiceVO generateOTP(WebServiceVO webServiceVO);
    WebServiceVO balanceInquiry(WebServiceVO webServiceVO);
    WebServiceVO miniStatement(WebServiceVO webServiceVO);
    WebServiceVO billPaymentInquiry(WebServiceVO webServiceVO);
    WebServiceVO billPayment(WebServiceVO webServiceVO);
    WebServiceVO cashInInquiry(WebServiceVO webServiceVO);
    WebServiceVO cashIn(WebServiceVO webServiceVO);
    WebServiceVO TitleFetch(WebServiceVO webServiceVO);
    WebServiceVO cashInAgent(WebServiceVO webServiceVO);
    WebServiceVO cashOutInquiry(WebServiceVO webServiceVO);
    WebServiceVO cashOut(WebServiceVO webServiceVO);
    WebServiceVO mpinRegistration(WebServiceVO webServiceVO);
    WebServiceVO mpinChange(WebServiceVO webServiceVO);
    WebServiceVO upgradeAccount(WebServiceVO webServiceVO);
    WebServiceVO walletToWalletPaymentInquiry(WebServiceVO webServiceVO);
    WebServiceVO walletToWalletPayment(WebServiceVO webServiceVO);
    WebServiceVO upgradeAccountInquiry(WebServiceVO webServiceVO);
    WebServiceVO accountStatusChange(WebServiceVO webServiceVO);
    WebServiceVO ibftTitleFetch(WebServiceVO webServiceVO);
    WebServiceVO ibftAdvice(WebServiceVO webServiceVO);
    WebServiceVO challanPayment(WebServiceVO webServiceVO);
    WebServiceVO challanPaymentInquiry(WebServiceVO webServiceVO);
    WebServiceVO walletToCnic(WebServiceVO webServiceVO);
    WebServiceVO walletToCnicInquiry(WebServiceVO webServiceVO);
    WebServiceVO hraToWallet(WebServiceVO webServiceVO);
    WebServiceVO hraToWalletInquiry(WebServiceVO webServiceVO);
    WebServiceVO walletToCore(WebServiceVO webServiceVO);
    WebServiceVO walletToCoreInquiry(WebServiceVO webServiceVO);
    WebServiceVO debitCardIssuance(WebServiceVO webServiceVO);
    WebServiceVO debitCardIssuanceInquiry(WebServiceVO webServiceVO);
    WebServiceVO hraRegistration(WebServiceVO webServiceVO);
    WebServiceVO hraRegistrationInquiry(WebServiceVO webServiceVO);
    WebServiceVO debitInquiry(WebServiceVO webServiceVO);
    WebServiceVO debit(WebServiceVO webServiceVO);
    WebServiceVO agentBillPaymentInquiry(WebServiceVO webServiceVO);
    WebServiceVO agentBillPayment(WebServiceVO webServiceVO);
    WebServiceVO creditInquiry(WebServiceVO webServiceVO);
    WebServiceVO credit(WebServiceVO webServiceVO);
    WebServiceVO hraCashWithDrawlInquiry(WebServiceVO webServiceVO);
    WebServiceVO hraCashWithDrawl(WebServiceVO webServiceVO);
    WebServiceVO accountAuthentication(WebServiceVO webServiceVO);
    WebServiceVO loginPin(WebServiceVO webServiceVO);
    WebServiceVO loginPinChange(WebServiceVO webServiceVO);
    WebServiceVO resetPin(WebServiceVO webServiceVO);

    WebServiceVO advanceLoanSalary(WebServiceVO webServiceVO);
    WebServiceVO smsGeneration(WebServiceVO webServiceVO);

    WebServiceVO agentAccountLogin(WebServiceVO webServiceVO);
    WebServiceVO agentLoginPinGeneration(WebServiceVO webServiceVO);
    WebServiceVO agentLoginPinReset(WebServiceVO webServiceVO);

    WebServiceVO agentMpinGeneration(WebServiceVO webServiceVO);
    WebServiceVO agentMpinReset(WebServiceVO webServiceVO);
//    WebServiceVO agentMpinVerification(WebServiceVO webServiceVO);

    WebServiceVO agentBalanceInquiry(WebServiceVO webServiceVO);

//    WebServiceVO agentToAgentInquiry(WebServiceVO webServiceVO);
//    WebServiceVO agentToAgentPayment(WebServiceVO webServiceVO);
//    WebServiceVO agentAccountOpening(WebServiceVO webServiceVO);
//    WebServiceVO agentUpgradeAccountInquiry(WebServiceVO webServiceVO);
//    WebServiceVO agentUpgradeAccount(WebServiceVO webServiceVO);

//    WebServiceVO agentCashInInquiry(WebServiceVO webServiceVO);
//    WebServiceVO agentCashIn(WebServiceVO webServiceVO);
//    WebServiceVO agentCashOutInquiry(WebServiceVO webServiceVO);
//    WebServiceVO agentCashOut(WebServiceVO webServiceVO);

//
//    WebServiceVO agentWalletToWalletInquiry(WebServiceVO webServiceVO);
//    WebServiceVO agentWalletToWalletPayment(WebServiceVO webServiceVO);
//    WebServiceVO agentWalletToCnicInquiry(WebServiceVO webServiceVO);
//    WebServiceVO agentWalletToCnicPayment(WebServiceVO webServiceVO);
//
    WebServiceVO agentIbftInquiry(WebServiceVO webServiceVO);
    WebServiceVO agentIbftPayment(WebServiceVO webServiceVO);

//    WebServiceVO agentRetailPaymentInquiry(WebServiceVO webServiceVO);
//    WebServiceVO agentRetailPayment(WebServiceVO webServiceVO);
//    WebServiceVO agentWalletToCoreInquiry(WebServiceVO webServiceVO);
//    WebServiceVO agentWalletToCorePayment(WebServiceVO webServiceVO);
//    WebServiceVO agentReceiveMoneyInquiry(WebServiceVO webServiceVO);
//    WebServiceVO agentReceiveMoneyPayment(WebServiceVO webServiceVO);
//    WebServiceVO agentCnicToCnicInquiry(WebServiceVO webServiceVO);
//    WebServiceVO agentCnicToCnicPayment(WebServiceVO webServiceVO);

//    WebServiceVO agentHRARegistrationInquiry(WebServiceVO webServiceVO);
//    WebServiceVO agentHRARegistration(WebServiceVO webServiceVO);
//    WebServiceVO agentCnicToCoreInquiry(WebServiceVO webServiceVO);
//      WebServiceVO agentCnicToCorePayment(WebServiceVO webServiceVO);


    WebServiceVO agentCashDepositInquiry(WebServiceVO webServiceVO);
    WebServiceVO agentCashDepositPayment(WebServiceVO webServiceVO);
    WebServiceVO agentCashWithdrawalInquiry(WebServiceVO webServiceVO);
    WebServiceVO agentCashWithdrawalPayment(WebServiceVO webServiceVO);

    WebServiceVO mpinVerification(WebServiceVO webServiceVO);
    WebServiceVO listSegments(WebServiceVO webServiceVO);
    WebServiceVO listCatalogs(WebServiceVO webServiceVO);
    WebServiceVO l2AccountOpening(WebServiceVO webServiceVO);
    WebServiceVO l2AccountUpgrade(WebServiceVO webServiceVO);
    WebServiceVO accountDetail(WebServiceVO webServiceVO);
    WebServiceVO customerNameUpdate(WebServiceVO webServiceVO);
    WebServiceVO clsStatusUpdate(WebServiceVO webServiceVO);
    WebServiceVO blinkAccountVerificationInquiry(WebServiceVO webServiceVO);
    WebServiceVO blinkAccountVerification(WebServiceVO webServiceVO);
    WebServiceVO fundWalletToCoreInquiry(WebServiceVO webServiceVO);
    WebServiceVO fundWalletToCore(WebServiceVO webServiceVO);
    WebServiceVO debitCardStatusVerification(WebServiceVO webServiceVO);
    WebServiceVO advanceLoanPaymentSettlement(WebServiceVO webServiceVO);
    WebServiceVO feePaymentInquiry(WebServiceVO webServiceVO);
    WebServiceVO feePayment(WebServiceVO webServiceVO);
    WebServiceVO updateMinorAccount(WebServiceVO webServiceVO);
    WebServiceVO verifyLoginAccount(WebServiceVO webServiceVO);
    WebServiceVO minorFatherBvsVerification(WebServiceVO webServiceVO);
    WebServiceVO checqueBookStatus(WebServiceVO webServiceVO);
    WebServiceVO cnicTo256(WebServiceVO webServiceVO);
    WebServiceVO transactionStatus(WebServiceVO webServiceVO);
    WebServiceVO profileStatus(WebServiceVO webServiceVO);
    WebServiceVO lienStatus(WebServiceVO webServiceVO);
    WebServiceVO initiateLoan(WebServiceVO webServiceVO);
    WebServiceVO selectLoanOffer(WebServiceVO webServiceVO);
    WebServiceVO selectLoan(WebServiceVO webServiceVO);
//    WebServiceVO repayLoan(WebServiceVO webServiceVO);
//    WebServiceVO getLoanSummary(WebServiceVO webServiceVO);
    WebServiceVO payLoan(WebServiceVO webServiceVO);
    WebServiceVO outstandingLoanStatus(WebServiceVO webServiceVO);
    WebServiceVO getOutstandingLoan(WebServiceVO webServiceVO);
    WebServiceVO loanPlan(WebServiceVO webServiceVO);
    WebServiceVO loanHistory(WebServiceVO webServiceVO);
    WebServiceVO transactionActive(WebServiceVO webServiceVO);
//    WebServiceVO loanStatus(WebServiceVO webServiceVO);
    WebServiceVO loanCallBack(WebServiceVO webServiceVO);
    WebServiceVO simpleAccountOpening(WebServiceVO webServiceVO);
//    WebServiceVO offerListForCommodity(WebServiceVO webServiceVO);
//    WebServiceVO customerAnalytics(WebServiceVO webServiceVO);

}
