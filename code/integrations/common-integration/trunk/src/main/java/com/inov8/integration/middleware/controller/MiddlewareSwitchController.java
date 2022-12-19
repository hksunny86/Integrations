package com.inov8.integration.middleware.controller;

import com.inov8.integration.vo.AccessIntegrationVO;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

public interface MiddlewareSwitchController {
    MiddlewareMessageVO generateMobilePIN(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO deleteScheduleTransaction(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO editScheduleTransaction(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO forgotSecretQuestionAnswer(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO changeSecretAnswer(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO changeEmail(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getTransactionHistory(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getLoginHistory(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO statementRequest(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO chequeBookRequest(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO billInquiry(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO billPayment(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO titleFetch(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO fundTransfer(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO openAccountFTNarration(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO fundTransferAdvice(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO acquirerReversalAdvice(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO accountBalanceInquiry(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO miniStatement(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO customerRelationshipInquiry(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO titleFetchForIBFT(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO interBankFundTransfer(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO validateATMPIN(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO generateATMPIN(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO changeATMPIN(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO cardStatusUpdate(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO cardStatusInquiry(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO validateInternetPassword(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO generateInternetPassword(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO changeInternetPassword(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO cardRelationshipInquiry(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO debitCardInquiry(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO extendedCardInquiry(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO onlineCardStatement(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO customerExtendedProfile(MiddlewareMessageVO messageVO) throws RuntimeException;

    MiddlewareMessageVO changeChannelDelivery(MiddlewareMessageVO messageVO) throws RuntimeException;

    MiddlewareMessageVO customerRegistration(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getCustomerDetail(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getBeneficiaryRelationships(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getBankDetails(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getBeneficiaryAccounts(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO addBeneficiaryAccount(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO modifyBeneficiaryAccount(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO deleteBeneficiaryAccount(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getBillingCompanyTypes(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getBillingCompanies(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getBillPayee(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO addBillPayee(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO deleteBillPayee(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO generateFinancialPIN(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO verifyFinancialPIN(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO expireFinancialPIN(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO generateOTP(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getSecretQuestion(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO updateFirstTimeLoginStatus(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getTransactionPurposes(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO auditLoggging(MiddlewareMessageVO requestVO) throws RuntimeException;


    MiddlewareMessageVO getForgetLoginID(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getFundTransferSchedule(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getBlockInternetBanking(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getBillPaymentSchedule(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getViewScheduleFundsTransfer(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO getViewScheduleBillPayment(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO step1Verification(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO mPinGeneration(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO mPinReset(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO mPinChange(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO channelDeRegistration(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO channelDeActivation(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO channelReActivation(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO passwordPattern(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO sendMessage(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO verifyPassword(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO accountAddition(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO relationshipAddition(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO profileAddition(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO accountRelationshipAddition(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO cnicToPanRelationshipInquiry(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO panToAccountRelationshipInquiry(MiddlewareMessageVO requestVO) throws RuntimeException;

    MiddlewareMessageVO sendSMS(MiddlewareMessageVO integrationVO) throws RuntimeException;

    MiddlewareMessageVO checkRegistration(MiddlewareMessageVO integrationVO) throws RuntimeException;

    MiddlewareMessageVO channelActivation(MiddlewareMessageVO integrationVO) throws RuntimeException;

    MiddlewareMessageVO customerEmailUpdateRequest(MiddlewareMessageVO integrationVO) throws RuntimeException;

    MiddlewareMessageVO customerEmailVerificationRequest(MiddlewareMessageVO integrationVO) throws RuntimeException;

    MiddlewareMessageVO viewAccountStatementRequest(MiddlewareMessageVO integrationVO) throws RuntimeException;

    MiddlewareMessageVO emailAccountStatementRequest(MiddlewareMessageVO integrationVO) throws RuntimeException;

    MiddlewareMessageVO checkBalance(MiddlewareMessageVO integrationVO) throws RuntimeException;

    MiddlewareMessageVO reverseFundTransfer(MiddlewareMessageVO integrationVO) throws RuntimeException;

    MiddlewareMessageVO customerAccountLink(MiddlewareMessageVO integrationVO) throws RuntimeException;

    MiddlewareMessageVO onlineRetailTransaction(MiddlewareMessageVO integrationVO) throws RuntimeException;

    MiddlewareMessageVO onlineRetailTransactionReversal(MiddlewareMessageVO integrationVO) throws RuntimeException;

    public AccessIntegrationVO updateCard(AccessIntegrationVO AccessIntegrationVO) throws RuntimeException;

    public AccessIntegrationVO exportCard(AccessIntegrationVO AccessIntegrationVO) throws RuntimeException;

    MiddlewareMessageVO subscriptionFeeDeduction(MiddlewareMessageVO integrationVO) throws RuntimeException;

    MiddlewareMessageVO getVoucherCode(MiddlewareMessageVO integrationVO) throws RuntimeException;

    MiddlewareMessageVO getFullStatement(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO getBeneficiaryList(MiddlewareMessageVO messageVO) throws RuntimeException;

    MiddlewareMessageVO getIBFTBeneficiaryList(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO interBankTitleFetch(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO getCustomerAccountInfo(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO getBillPaymentConsumerList(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO getDebitCardList(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO debitCardStatusChange(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO checkBill(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO getBillPaymentCompanies(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO userAuthentication(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO getCustomerInfo(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO otpVerification(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO cardAddition(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO deleteAccount(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO updateCustomerMobileNumber(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO ibftTitleFetch(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO ibftAdvice(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

     MiddlewareMessageVO checkBvsStatusOnNadra(MiddlewareMessageVO middlewareMessageVO) throws RuntimeException;

    MiddlewareMessageVO toupUp(MiddlewareMessageVO var1) throws RuntimeException;
}
