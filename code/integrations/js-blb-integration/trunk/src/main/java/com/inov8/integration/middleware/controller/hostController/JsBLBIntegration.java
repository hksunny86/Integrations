package com.inov8.integration.middleware.controller.hostController;

import com.inov8.integration.middleware.pdu.request.*;
import com.inov8.integration.middleware.pdu.response.*;
import com.inov8.integration.middleware.pdu.response.L2AccountOpeningResponse;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "JsBLBIntegration", targetNamespace = "http://tempuri.org/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface JsBLBIntegration {
    public VerifyAccountResponse verifyAccount(@WebParam(name = "verifyAccountRequest") VerifyAccountRequest request);

    public AccountOpeningResponse accountOpening(@WebParam(name = "accountOpeningRequest") AccountOpeningRequest request);

    public ConventionalAccountOpeningResponse conventionalAccountOpening(@WebParam(name = "conventionalAccountOpening") ConventionalAccountOpening request);

    public PaymentInquiryResponse paymentInquiry(@WebParam(name = "accountInquiryRequest") PaymentInquiryRequest request);

    public PaymentResponse paymentRequest(@WebParam(name = "paymentRequest") PaymentRequest request);

    public PaymentReversalResponse paymentReversal(@WebParam(name = "paymentReversalRequest") PaymentReversalRequest request);

    public OtpVerificationResponse otpVerification(@WebParam(name = "otpVerificationRequest") OtpVerificationRequest request);

    public CardTaggingResponse cardTagging(@WebParam(name = "cardTaggingRequest") CardTagging request);

    public AccountLinkDeLinkResponse accountLinkDeLink(@WebParam(name = "accountLinkDeLink") AccountLinkDeLink request);

    public SetCardStatusResponse setCardStatus(@WebParam(name = "setCardStatus") SetCardStatus request);

    public GenerateOtpResponse generateOTP(@WebParam(name = "generateOtpRequest") GenerateOtpRequest request);

    public BalanceInquiryResponse balanceInquiry(@WebParam(name = "balanceInquiryRequest") BalanceInquiryRequest request);

    public MiniStatementResponse miniStatement(@WebParam(name = "miniStatementRequest") MiniStatementRequest request);

    public BillPaymentInquiryResponse billPaymentInquiry(@WebParam(name = "billPaymentInquiryRequest") BillPaymentInquiryRequest request);

    public BillPaymentResponse billPayment(@WebParam(name = "billPaymentRequest") BillPaymentRequest request);

//  public CashInInquiryResponse cashInInquiry(@WebParam(name = "cashInInquiryRequest") CashInInquiryRequest request);

    public CashInResponse cashIn(@WebParam(name = "cashInRequest") CashInRequest request);

    public TitleFetchResponse titleFetch(@WebParam(name = "titleFetchRequest") TitleFetchRequest request);

    public CashInAgentResponse cashInAgent(@WebParam(name = "CashInAgentRequest") CashInAgentRequest request);

    public CashOutInquiryResponse cashOutInquiry(@WebParam(name = "CashOutInquiryRequest") CashOutInquiryRequest request);

    public CashOutResponse cashOut(@WebParam(name = "CashOutRequest") CashOutRequest request);

    public MpinRegistrationResponse mpinRegistration(@WebParam(name = "mpinRegistrationRequest") MpinRegistrationRequest request);

    public MpinChangeResponse mpinChange(@WebParam(name = "mpinChangeRequest") MpinChangeRequest request);

    public UpgradeAccountResponse upgradeAccount(@WebParam(name = "UpgradeAccountRequest") UpgradeAccountRequest request);

    public WalletToWalletPaymentInquiryResponse walletToWalletPaymentInquery(@WebParam(name = "WalletToWalletPaymentInquery") WalletToWalletPaymentInquiryRequest request);

    public WalletToWalletPaymentResponse walletToWalletPayment(@WebParam(name = "WalletToWalletPaymentRequest") WalletToWalletPaymentRequest request);

    public UpgradeAccountInquiryResponse upgradeAccountInquiry(@WebParam(name = "UpgradeAccountInquiryRequest") UpgradeAccountInquiryRequest request);

    public AccountStatusChangeResponse accountStatusChange(@WebParam(name = "AccountStatusChangeRequest") AccountStatusChangeRequest request);

    public IbftTitleFetchResponse ibftTitleFetchResponse(@WebParam(name = "IbftTitleFetchRequest") IbftTitleFetchRequest request);

    public IbftAdviceResponse ibftAdviceResponse(@WebParam(name = "IbftAdviceRequest") IbftAdviceRequest ibftAdviceRequest);

    public ChallanPaymentResponse challanPayment(@WebParam(name = "ChallanPaymentRequest") ChallanPaymentRequest challanPaymentRequest);

    public ChallanPaymentInquiryResponse challanPaymentInquiry(@WebParam(name = "ChallanPaymentInquiryRequest") ChallanPaymentInquiryRequest challanPaymentInquiryRequest);

    public DebitCardIssuanceInquiryResponse debitCardIssuanceInquiry(@WebParam(name = "DebitCardIssuanceInquiryRequest") DebitCardIssuanceInquiryRequest debitCardIssuanceInquiryRequest);

    public DebitCardIssuanceResponse debitCardIssuance(@WebParam(name = "DebitCardIssuanceRequest") DebitCardIssuanceRequest debitCardIssuanceRequest);

    public WalletToCnicResponse walleToCnic(@WebParam(name = "WalletToCnicRequest") WalletToCnicRequest walletToCnicRequest);

    public WalletToCnicInquiryResponse walletToCnicInquiry(@WebParam(name = "WalletToCnicInquiryRequest") WalletToCnicInquiryRequest walletToCnicInquiryRequest);

    public HRARegistrationInquiryResponse hraRegistrationInquiry(@WebParam(name = "HRARegistrationInquiryRequest") HRARegistrationInquiryRequest hraRegistrationInquiryRequest);

    public HRARegistrationResponse hraRegistration(@WebParam(name = "HRARegistrationRequest") HRARegistrationRequest hraRegistrationRequest);

    public WalletToCoreInquiryResponse walletToCoreInquiry(@WebParam(name = "WalletToCoreInquiryRequest") WalletToCoreInquiryRequest walletToCoreInquiryRequest);

    public WalletToCoreResponse walletToCore(@WebParam(name = "WalletToCoreRequest") WalletToCoreRequest walletToCoreRequest);

    public WalletToCoreInquiryResponse FundwalletToCoreInquiry(@WebParam(name = "FundWalletToCoreInquiryRequest") WalletToCoreInquiryRequest walletToCoreInquiryRequest);

    public WalletToCoreResponse FundwalletToCore(@WebParam(name = "FundWalletToCoreRequest") WalletToCoreRequest walletToCoreRequest);

    public HRAToWalletInquiryResponse hraToWalletInquiry(@WebParam(name = "HRAToWalletInquiryRequest") HRAToWalletInquiryRequest hraToWalletInquiryRequest);

    public HRAToWalletResponse hraToWallet(@WebParam(name = "HRAToWalletRequest") HRAToWalletRequest hraToWalletRequest);

    public DebitInquiryResponse debitInquiry(@WebParam(name = "DebitInquiryRequest") DebitInquiryRequest debitInquiryRequest);

    public DebitResponse debit(@WebParam(name = "DebitRequest") DebitRequest debitRequest);

    public AgentBillPaymentInquiryResponse agentBillPaymentInquiry(@WebParam(name = "AgentBillPaymentInquiryRequest") AgentBillPaymentInquiryRequest agentBillPaymentInquiryRequest);

    public AgentBillPaymentResponse agentBillPayment(@WebParam(name = "AgentBillPaymentRequest") AgentBillPaymentRequest agentBillPaymentRequest);

    public CreditInquiryResponse creditInquiry(@WebParam(name = "CreditInquiryRequest") CreditInquiryRequest creditInquiryRequest);

    public CreditResponse credit(@WebParam(name = "CreditRequest") CreditRequest creditRequest);

    public HRACashWithdrawalInquiryResponse hraCashWithdrawalInquiry (@WebParam(name = "HRACashWithdrawalInquiryRequest")HRACashWithdrawalInquiryRequest hraCashWithdrawalInquiryRequest);

    public HRACashWithdrawalResponse hraCashWithdrawal(@WebParam(name = "HRACashWithdrawalRequest") HRACashWithdrawalRequest hraCashWithdrawalRequest);

    public LoginAuthenticationResponse loginAuthentication(@WebParam(name = "LoginAuthenticationRequest") LoginAuthenticationRequest loginAuthenticationRequest);

    public LoginPinResponse loginPin(@WebParam (name = "LoginPinRequest") LoginPinRequest loginPinRequest);

    public LoginPinChangeResponse loginPinChange(@WebParam (name = "LoginPinChangeRequest") LoginPinChangeRequest loginPinChangeRequest);

    public ResetPinResponse resetPin(@WebParam (name = "ResetPinRequest") ResetPinRequest resetPinRequest);

    public AdvanceLoanSalaryResponse advanceLoanSalary (@WebParam(name = "AdvanceLoanSalary") AdvanceLoanSalaryRequest request);

    public SmsGenerationResponse smsGeneration(@WebParam(name = "SmsGenerationRequest") SmsGenerationRequest request);

    public AgentAccountLoginResponse agentAccountLogin(@WebParam (name = "AgentAccountLoginRequest")AgentAccountLoginRequest agentAccountLoginRequest);

    public AgentLoginPinGenerationResponse agentLoginPinGeneration(@WebParam (name = "AgentLoginPinGenerationRequest") AgentLoginPinGenerationRequest agentLoginPinGenerationRequest);

    public AgentLoginPinResetResponse agentLoginPinReset(@WebParam (name = "AgentLoginPinReset")AgentLoginPinResetRequest agentLoginPinResetRequest);

    public AgentMpinGenerationResponse agentMpinGeneration(@WebParam(name = "AgentMpinGenerationRequest") AgentMpinGenerationRequest agentMpinGenerationRequest);

    public AgentMpinResetResponse agentMpinReset(@WebParam(name = "AgentMPinResetRequest") AgentMpinResetRequest agentMpinResetRequest);

    public AgentBalanceInquiryResponse agentBalanceInquiry(@WebParam(name = "AgentBalanceInquiryRequest") AgentBalanceInquiryRequest agentBalanceInquiryRequest);

//    public AgentToAgentInquiryResponse agentToAgentInquiry(@WebParam(name = "AgentToAgentInquiryRequest") AgentToAgentInquiryRequest agentToAgentInquiryRequest);
//
//    public AgentToAgentPaymentResponse agentToAgentPayment(@WebParam(name = "AgentToAgentPaymentRequest") AgentToAgentPaymentRequest agentToAgentPaymentRequest);
//
//    public AgentAccountOpeningResponse agentAccountOpening(@WebParam(name = "AgentAccountOpeningRequest") AgentAccountOpeningRequest request);
//
//    public AgentUpgradeAccountInquiryResponse agentUpgradeAccountInquiry(@WebParam (name = "AgentUpgradeAccountInquiryRequest") AgentUpgradeAccountInquiryRequest request);
//
//    public AgentUpgradeAccountResponse agentUpgradeAccount(@WebParam(name = "AgentUpgradeAccountRequest") AgentUpgradeAccountRequest request);
//
//    public AgentCashInInquiryResponse agentCashInInquiry(@WebParam(name = "AgentCashInInquiry") AgentCashInInquiryRequest request);
//
//    public AgentCashInResponse agentCashIn(@WebParam(name = "AgentCashInRequest") AgentCashInRequest request);
//
//    public AgentCashOutInquiryResponse agentCashOutInquiry(@WebParam(name = "AgentCashOutInquiry") AgentCashOutInquiryRequest request);
//
//    public AgentCashOutResponse agentCashOut (@WebParam(name = "AgentCashOut") AgentCashOutRequest request);
//
//    public AgentWalletToWalletInquiryResponse agentWalletToWalletInquiry(@WebParam(name = "AgentWalletToWalletInquiry") AgentWalletToWalletInquiryRequest request);
//
//    public AgentWalletToWalletPaymentResponse agentWalletToWalletPayment(@WebParam(name = "AgentWalletToWalletPayment") AgentWalletToWalletPaymentRequest request);
//
//    public AgentWalletToCnicInquiryResponse agentWalletToCnicInquiry(@WebParam(name = "AgentWalletToCnicInquiryRequest") AgentWalletToCnicInquiryRequest request);
//
//    public AgentWalletToCnicPaymentResponse agentWalletToCnicPayment(@WebParam(name = "AgentWalletToCnicPaymentRequest") AgentWalletToCnicPaymentRequest request);

    public AgentIbftInquiryResponse agentIbftInquiry(@WebParam (name = "AgentIbftInquiryRequest") AgentIbftInquiryRequest request);

    public AgentIbftPaymentResponse agentIbftPayment(@WebParam (name = "AgentIbftPaymentRequest") AgentIbftPaymentRequest request);

//    public AgentRetailPaymentInquiryResponse agentRetailPaymentInquiry(@WebParam (name = "AgentRetailPaymentInquiryRequest") AgentRetailPaymentInquiryRequest request);
//
//    public AgentRetailPaymentResponse agentRetailPayment(@WebParam (name = "AgentRetailPaymentRequest") AgentRetailPaymentRequest request);
//
//    public AgentWalletToCoreInquiryResponse agentWalletToCoreInquiry (@WebParam(name = "AgentWalletToCnicInquiryRequest") AgentWalletToCoreInquiryRequest request);
//
//    public AgentWalletToCorePaymentResponse agentWalletToCorePayment (@WebParam(name = "AgentWalletToCnicPaymentRequest") AgentWalletToCorePaymentRequest request);
//
//    public AgentReceiveMoneyInquiryResponse agentReceiveMoneyInquiry (@WebParam(name = "AgentReceiveMoneyInquiryRequest") AgentReceiveMoneyInquiryRequest request);
//
//    public AgentReceiveMoneyPaymentResponse agentReceiveMoneyPayment (@WebParam(name = "AgentReceiveMoneyPaymentRequest") AgentReceiveMoneyPaymentRequest request);
//
//    public AgentCnicToCnicInquiryResponse agentCnicToCnicInquiry(@WebParam (name = "AgentCnicToCnicInquiryRequest") AgentCnicToCnicInquiryRequest request);
//
//    public AgentCnicToCnicPaymentResponse agentCnicToCnicPayment(@WebParam (name = "AgentCnicToCnicPaymentRequest") AgentCnicToCnicPaymentRequest request);

//    public AgentHRARegistrationInquiryResponse agentHRARegistrationInquiry (@WebParam (name = "AgentHRARegistrationInquiryRequest") AgentHRARegistrationInquiryRequest request);
//
//    public AgentHRARegistrationResponse agentHRARegistration (@WebParam (name = "AgentHRARegistrationRequest") AgentHRARegistrationRequest request);
//
//    public AgentCnicToCoreInquiryResponse agentCnicToCoreInquiry (@WebParam (name = "AgentCnicToCoreInquiryRequest") AgentCnicToCoreInquiryRequest request);

//    public AgentCnicToCorePaymentResponse agentCnicToCorePayment(@WebParam(name = "AgentCnicToCorePaymentRequest") AgentCnicToCorePaymentRequest request);

    public AgentCashDepositInquiryResponse agentCashDepositInquiry(@WebParam(name = "AgentCashDepositInquiryRequest") AgentCashDepositInquiryRequest request);

    public AgentCashDepositPaymentResponse agentCashDepositPayment(@WebParam(name = "AgentCashDepositPaymentRequest") AgentCashDepositPaymentRequest request);

    public AgentCashWithdrawalInquiryResponse agentCashWithdrawalInquiry(@WebParam (name = "AgentCashWithdrawalInquiryRequest") AgentCashWithdrawalInquiryRequest request);

    public AgentCashWithdrawalPaymentResponse agentCashWithdrawalPayment(@WebParam (name = "AgentCashWithdrawalPaymentRequest") AgentCashWithdrawalPaymentRequest request);

    public MpinVerificationResponse mpinVerification(@WebParam (name = "MpinVerificationRequest") MpinVerificationRequest request);

    public SegmentListResponse segmentList (@WebParam (name = "SegmentListRequest") SegmentListRequest request);

    public AgentCatalogResponse catalogList (@WebParam (name = "AgentCatalogRequest") AgentCatalogsRequest request);

    public L2AccountOpeningResponse l2AccountOpening(@WebParam (name = "L2AccountOpeningRequest") L2AccountOpeningRequest request);

    public L2AccountUpgradeResponse l2AccountUpgrade(@WebParam (name = "L2AccountUpgradeRequest") L2AccountUpgradeRequest request);

    public AccountDetailResponse accountDetail(@WebParam (name = "AccountDetail") AccountDetails request);

//    public CustomerNameUpdateResponse customerNameUpdate(@WebParam (name = "CustomerNameUpdateRequest") CustomerNameUpdateRequest request);


}
