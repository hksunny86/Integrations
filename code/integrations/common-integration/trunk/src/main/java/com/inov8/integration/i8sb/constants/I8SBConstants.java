package com.inov8.integration.i8sb.constants;

/**
 * Created by inov8 on 8/28/2017.
 */
public class I8SBConstants {

    public static String PIPE_DELIMITER = "|";
    public static String DOUBLE_AMPERSAND_DELIMITER = "&&";
    public static String CAP_DELIMITER = "^";
    public static String EOL_DELIMITER = "\n";
    // I8SB Gateways
    public static String I8SB_Gateway_Invoker = "Invoker";
    public static String I8SB_Gateway_REST = "REST";
    public static String I8SB_Gateway_SOAP = "SOAP";
    public static String I8SB_Gateway_TCPIP = "TCPIP";
    public static String I8SB_Gateway_RPC = "RPC";
    public static String I8SB_Gateway_XMLRPC = "XMLRPC";
    public static String I8SB_Gateway_XMLRPC_Billing = "XMLRPC_Billing";

    // I8SB Client ID's
    public static String I8SB_Client_ID_JSBL = "JSBL";
    public static String I8SB_Client_ID_MBL = "MBL";
    public static String I8SB_Client_ID_AKBL = "AKBL";
    public static String I8SB_Client_ID_BOP = "BOP";
    public static String I8SB_Client_ID_SBPL = "SBPL";
    public static String I8SB_Client_ID_MasterCard = "MasterCard";
    public static String I8SB_Client_ID_KMBL = "KMBL";
    public static String I8SB_Client_ID_I8SB = "I8SB";
    public static String I8SB_Client_ID_JSBOOKME = "JSBOOKME";



    // I8SB Client Terminal ID's
    public static String I8SB_Client_Terminal_ID_MB = "MB";
    public static String I8SB_Client_Terminal_ID_BLB = "BLB";
    public static String I8SB_Client_Terminal_ID_Portal = "Portal";
    public static String I8SB_Client_Terminal_ID_OTHER = "OTHER";
    public static String I8SB_Client_Terminal_ID_JSBOOKME = "JSBOOKME";

    // I8SB Channel ID's
    public static String I8SB_Channel_ID_RDV_MB = "RDV_MB";
    public static String I8SB_Channel_ID_RDV_WS = "RDV_WS";
    public static String I8SB_Channel_ID_FONEPAY = "FONEPAY";
    public static String I8SB_Channel_ID_VRG_ECHALLAN = "VRG_ECHALLAN";
    public static String I8SB_Channel_ID_TUTUKA = "TUTUKA";
    public static String I8SB_Channel_ID_MICROBANK = "MICROBANK";
    public static String I8SB_Channel_ID_DIGIBANK = "DIGIBANK";
    public static String I8SB_Channel_ID_M3TECH = "M3TECH";
    public static String I8SB_Channel_ID_NADRA = "NADRA";
    public static String I8SB_Channel_ID_NADRA_PAYSIS = "NADRA_PAYSIS";
    public static String I8SB_Channel_ID_NADRA_TPS = "NADRA_TPS";
    public static String I8SB_Channel_ID_APIGEE = "APIGEE";
    public static String I8SB_Channel_ID_KMBL_WS = "KMBL_WS";
    public static String I8SB_Channel_ID_APPINSNAP = "APPINSNAP";
    public static String I8SB_Channel_ID_GCM = "GCM";
    public static String I8SB_Channel_ID_FCM = "FCM";
    public static String I8SB_Channel_ID_JSDEBITCARD = "JSDEBITCARD";
    public static String I8SB_Channel_ID_AKBLPMD = "AKBLPMD";
    public static String I8SB_Channel_ID_BOPMB = "BOPMB";
    public static String I8SB_Channel_ID_PMDC = "PMDC";
    public static String I8SB_Channel_ID_AMMA = "AMMA";
    public static String I8SB_Channel_ID_BOPBLB = "BOPBLB";
    public static String I8SB_Channel_ID_EOCEAN = "EOCEAN";
    public static String I8SB_Channel_ID_HealthAndNutrition = "HealthAndNutrition";
    public static String I8SB_Channel_ID_JSBOOKME = "JSBOOKME";
    public static String I8SB_Channel_ID_JSDEBITCARDAPI = "JSDEBITCARDAPI";
    public static String I8SB_Channel_ID_CLSJS = "CLSJS";
    public static String I8SB_Channel_ID_ZINDIGI = "ZINDIGI";
    public static String I8SB_Channel_ID_TELENOR = "TELENOR";
    public static String I8SB_Channel_ID_VISION = "VISION";
    public static String I8SB_Channel_ID_PUSHNOTIFICATION = "PUSHNOTIFICATION";
    public static String I8SB_Channel_ID_OFFLINE_BILLER = "OFFLINEBILLER";
    public static String I8SB_Channel_ID_REFFERAL_CUSTOMER = "REFFERALCUSTOMER";
    public static String I8SB_Channel_ID_CUSTOMER_DEVICE_VERIFICATION = "CUSTOMERDEVICEVERIFICATION";
    public static String I8SB_Channel_ID_MERCHANTCAMPING = "MERCHANTCAMPING";
    public static String I8SB_Channel_ID_T24API = "T24API";
    public static String I8SB_Channel_ID_LOANINTIMATION = "LOANINTIMATION";
    public static String I8SB_Channel_ID_OPTASIA = "OPTASIA";
    public static String I8SB_Channel_ID_TASDEEQ = "TASDEEQ";
    public static String I8SB_Channel_ID_EOCEAN_NEW = "EOCEANNEWAPI";
    public static String I8SB_Channel_ID_ZINDIGI_CUSTOMER_ACTIVITY = "ZINDIGICUSTOMERACTIVITY";
    public static String I8SB_Channel_ID_ZINDIGI_CUSTOMER = "ZINDIGICUSTOMER";
    public static String I8SB_Channel_ID_BRANDVERSE = "BRANDVERSE";
    public static String I8SB_Channel_ID_PDM = "PDM";
    public static String I8SB_Channel_ID_RAAST = "RAAST";


    public static String I8SB_Channel_ID_ETPaymentCollection = "ETPaymentCollection";
    public static String RequestType_MPASS_MerchantDetail = "MPASS_MerchantDetail";
    public static String RequestType_MPASS_Payment = "MPASS_Payment";

    public static String RequestType_CustomerValidation = "CustomerValidation"; // PanPinVerification|CNICValidation|AccountBalanceInquiry
    public static String RequestType_GetMyAccounts = "GetMyAccounts"; // CheckingAccountSummary|LoanAccountSummary|TDRAccountSummary
    public static String RequestType_CustomerInformation = "GetCustomerInformation";
    public static String RequestType_ZINDIGI_CUSTOMER_ACTIVITY = "ZINDIGICUSTOMERACTIVITY";

    public static String RequestType_EChallanInquiry = "EChallan_Inquiry";
    public static String RequestType_EChallanPayment = "EChallan_Payment";
    public static String RequestType_AuthenticateUser = "AuthenticateUser";
    public static String RequestType_ValidateUsername = "ValidateUsername";
    public static String RequestType_RegisterUser = "RegisterUser";
    public static String RequestType_Login = "Login";
    public static String RequestType_LDAPRequest = "LDAPRequest";
    public static String RequestType_SendSMS = "SendSMS";
    public static String RequestType_Email = "Email";
    public static String RequestType_PanPinVerification = "PanPinVerification";
    public static String RequestType_CNICValidation = "CNICValidation";
    public static String RequestType_GenerateFPIN = "GenerateFPIN";
    public static String RequestType_AuthenticateCustomer = "AuthenticateCustomer";
    public static String RequestType_ChangePassword = "ChangePassword";
    public static String RequestType_ForgetCredentials = "ForgetCredentials";
    public static String RequestType_ForgetUsername = "ForgetUsername";
    public static String RequestType_CheckingAccountSummary = "CheckingAccountSummary";
    public static String RequestType_LoanAccountSummary = "LoanAccountSummary";
    public static String RequestType_TDRAccountSummary = "TDRAccountSummary";
    public static String RequestType_LoanAccountStatement = "LoanAccountStatement";
    public static String RequestType_TDRAccountStatement = "TDRAccountStatement";
    public static String RequestType_AccountFullStatement = "AccountFullStatement";
    public static String RequestType_AccountBalanceInquiry = "AccountBalanceInquiry";
    public static String RequestType_CoreAccountMiniStatement = "CoreAccountMiniStatement";
    public static String RequestType_DebitCardList = "DebitCardList";
    public static String RequestType_REFFERAL_CUSTOMER = "RefferalCustomer";
    public static String RequestType_GET_CUSTOMER_DEVICE_DETAIL = "GetCustomerDeviceDetail";
    public static String RequestType_UPDATE_CUSTOMER_DEVICE_DETAIL = "UpdateCustomerDeviceDetail";


    public static String RequestType_CLSJS_ImportScreening = "CLSJSImportScreening";
    public static String RequestType_DebitCardDetails = "DebitCardDetails";
    public static String RequestType_DebitCard = "DebitCard";
    public static String RequestType_PrepaidCardList = "PrepaidCardList";
    public static String RequestType_PrepaidCardDetails = "PrepaidCardDetails";
    public static String RequestType_PrepaidCardStatusChange = "PrepaidCardStatusChange";
    public static String RequestType_PrepaidCardTitleFetch = "PrepaidCardTitleFetch";
    public static String RequestType_CreditCardList = "CreditCardList";
    public static String RequestType_CreditCardMiniStatement = "CreditCardMiniStatement";
    public static String RequestType_CreditCardBillInquiry = "CreditCardBillInquiry";
    public static String RequestType_CreditCardPayment = "CreditCardPayment";
    public static String RequestType_CreditCardTitleFetch = "CreditCardTitleFetch";
    public static String RequestType_FundsTransfer = "FundsTransfer";
    public static String RequestType_FundsTransferReversal = "FundsTransferReversal";
    public static String RequestType_InterBankFundTransfer = "InterBankFundTransfer";
    public static String RequestType_TitleFetch = "TitleFetch";
    public static String RequestType_IBFTTitleFetch = "IBFTTitleFetch";
    public static String RequestType_DonationTransfer = "DonationTransfer";
    public static String RequestType_QRMerchantTransfer = "QRMerchantTransfer";
    public static String RequestType_QRTransferReversal = "QRTransferReversal";
    public static String RequestType_FeeTransfer = "FeeTransfer";
    public static String RequestType_BeneficiaryAccountList = "BeneficiaryAccountList";
    public static String RequestType_IBFTBeneficiaryAccountList = "IBFTBeneficiaryAccountList";
    public static String RequestType_BeneficiaryMaintenance = "BeneficiaryMaintenance";
    public static String RequestType_IBFTBeneficiaryMaintenance = "IBFTBeneficiaryMaintenance";
    public static String RequestType_PrepaidCardBeneficiaryAccountList = "PrepaidCardBeneficiaryAccountList";
    public static String RequestType_PrepaidCardBeneficiaryMaintenance = "PrepaidCardBeneficiaryMaintenance";
    public static String RequestType_UBPSCompaniesList = "UBPSCompaniesList";
    public static String RequestType_UBPSCustomerRegistration = "UBPSCustomerRegistration";
    public static String RequestType_UBPSCustomerUnRegistration = "UBPSCustomerUnRegistration";
    public static String RequestType_UBPSCustomerEdRegistration = "UBPSCustomerEdRegistration";
    public static String RequestType_GetRegisteredConsumers = "GetRegisteredConsumers";
    public static String RequestType_BillInquiry = "UBPSBillInquiry";
    public static String RequestType_BillPayment = "UBPSBillPayment";
    public static String RequestType_PrepaidCardRecharge = "PrepaidCardRecharge";
    public static String RequestType_InstrumentStatus = "InstrumentStatus";
    public static String RequestType_StopChequePayment = "StopChequePayment";
    public static String RequestType_SIFundsTransfer = "SIFundsTransfer";
    public static String RequestType_SIBillPayment = "SIBillPayment";
    public static String RequestType_SIIBFT = "SIIBFT";
    public static String RequestType_SIMiniStatement = "SIMiniStatement";
    public static String RequestType_SIList = "SIList";
    public static String RequestType_LOANINTIMATION = "LOANINTIMATION";

    public static String RequestType_CardLessWithdrawalRequest = "CardLessWithdrawalRequest";
    public static String RequestType_mVisaPushPayment = "mVisaPushPayment";
    public static String RequestType_CardActivationDeactivation = "CardActivationDeactivation";
    public static String RequestType_CardShoppingStatement = "CardShoppingStatement";
    public static String RequestType_GetBankList = "GetBankList";
    public static String RequestType_GetBranchList = "GetBranchList";
    public static String RequestType_GenerateOTP = "GenerateOTP";
    public static String RequestType_AuthenticateOTP = "AuthenticateOTP";
    public static String RequestType_GetVoucherCode = "GetVoucherCode";
    public static String RequestType_AddEForm = "AddEForm";
    public static String RequestType_DoActivity = "DoActivity";
    public static String RequestType_LoginTrasaction = "LoginTransaction";
    public static String RequestType_UpdateBioStatus = "UpdateBioStatus";
    public static String RequestType_ActivateCard = "ActivateCard";
    public static String RequestType_GetActiveLinkedCards = "GetActiveLinkedCards";
    public static String RequestType_Status = "Status";
    public static String RequestType_ChangePin = "ChangePin";
    public static String RequestType_CreateLinkedCard = "CreateLinkedCard";
    public static String RequestType_LinkCard = "LinkCard";
    public static String RequestType_OrderCard = "OrderCard";
    public static String RequestType_ResetPin = "ResetPin";
    public static String RequestType_Set3dSecureCode = "Set3dSecureCode";
    public static String RequestType_StopCard = "StopCard";
    public static String RequestType_TransferLink = "TransferLink";
    public static String RequestType_UnstopCard = "UnstopCard";
    public static String RequestType_UpdateBarer = "UpdateBarer";
    public static String RequestType_QRPayment="QRPayment";
    public static String RequestType_TransactionStatus="TransactionStatus";
    public static String RequestType_MblFundTransfer="MblFundTransfer";
    public static String RequestType_MblFundTransferReversal="MblFundTransferReversal";
    public static String RequestType_ThirdPartyFundTransfer="ThirdPartyFundTransfer";
    public static String RequestType_BookMeIPN="BookMeIPN";
    public static String RequestType_ATM_PIN_GENERATION="AtmPinGeneration";
    public static String RequestType_GET_CARD_DETAILS="GetCardDetails";
    public static String RequestType_Card_Status_Change="CardStatusChange";
    public static String RequestType_JSDEBITCARD_Import_Customer="ImportCustomer";
    public static String RequestType_JSDEBITCARD_Import_Account="ImportAccount";
    public static String RequestType_JSDEBITCARD_Import_Card="ImportCard";
    public static String RequestType_JSDEBITCARD_CardReissuance="CardReissuance";
    public static String RequestType_JSDEBITCARD_UpdateCardStatus="UpdateCardStatus";
    public static String RequestType_CustomerNameUpdate="CustomerNameUpdate";

    public static String RequestType_ZINDIGI_CUSTOMER_SYNC="ZindigiCustomerSync";
    public static String RequestType_ZINDIGI_P2M_STATUS_UPDATE="P2MStatusUpdate";
    public static String RequestType_L2_ACCOUNT_UPGRADE_VALIDATION="L2AccountUpgradeValidation";

    public static String RequestType_EOCEAN="EOCEAN";
    public static String RequestType_TELENOR_RECHARGE="Recharge";
    public static String RequestType_TELENOR_SERVICE_DETAIL="ServiceDetail";
    public static String RequestType_VISION_DEBIT_CARD="VisionDebitCard";

    public static String RequestType_AdministrativeMessage = "AdministrativeMessage";
    public static String RequestType_Balance = "Balance";
    public static String RequestType_Deduct = "Deduct";
    public static String RequestType_DeductAdjustment = "DeductAdjustment";
    public static String RequestType_DeductReversal = "DeductReversal";
    public static String RequestType_LoadAdjustment = "LoadAdjustment";
    public static String RequestType_LoadReversal = "LoadReversal";
    public static String RequestType_ValidatePIN = "ValidatePIN";
    public static String RequestType_Stop = "Stop";
    public static String RequestType_BlockUser = "BlockUser";
    public static String RequestType_AccountStatment = "AccountStatement";
    public static String RequestType_MinorAccountSync = "MinorAccountSync";

    // APIGEE calls
    public static String RequestType_PayMTCN = "PayMTCN";
    public static String RequestType_PayMtcnAccessToken = "PayMtcnAccessToken";

    // KMBL
    public static String RequestType_CashDeposit = "CashDeposit";
    public static String RequestType_CashWithdrawal = "CashWithdrawal";
    public static String RequestType_CashWithdrawalReversal = "CashWithdrawalReversal";
    public static String RequestType_TransactionInquiry = "TransactionInquiry";
    public static String RequestType_TransactionReversal = "TransactionReversal";
    public static String RequestType_AccessToken = "AccessToken";
    public static String RequestType_IrisAccessToken = "IrisAccessToken";
    public static String RequestType_UpdateKyc = "UpdateKyc";
    public static String RequestType_EOBI_AccessToken = "EOBIAccessToken";
    public static String RequestType_EOBI_TitleFetch = "EOBITitleFetch";
    public static String RequestType_EOBI_CashWithdrawal = "EOBICashWithdrawal";
    //AppInSnapApi call
    public static String RequestType_AppInSnap_CustomerDataSet = "AppInSnapCustomerDataSet";

    public static String RequestType_IVR_Passcode = "IVRPasscode";


    //Gcm Calls
    public static String RequestType_Notification_Message = "NotificationMsg";
    //Fcm
    public static String RequestType_Notification = "NotificationMessage";
    public static String RequestType_UserVerification = "UserVerification";

    // JSDEBITCARD Request Types:

    public static String RequestType_JSDEBITCARD_EXPORT = "JSDebitCardExport";
    public static String RequestType_JSDEBITCARD_IMPORT = "JSDebitCardImport";

    public static String RequestType_JSBLB_SEO_USSD = "JSBLBSEOUSSD";

    //JS Excise and Taxation Request Type

    public static String RequestType_GetAssesment_Detail = "GetAssesmentDetail";
    public static String RequestType_Generate_Challan = "GenerateChallan";
    public static String RequestType_Challan_Status = "ChallanStatus";

    public static String RequestTyp_App_Deactivation = "AppDeactivation";

    public static String RequestType_GetManualTransactionWindow = "GetManualTransactionWindow";
    public static String RequestType_PostManaualTransactionWindow = "PostManualTransactionWindow";
    public static String RequestType_DeleteManualTransactionWindow = "DeleteManualTrnsactionWindow";
    public static String RequestType_ZAKAAT_FT_SI = "ZakaatFTSI";
    public static String RequestType_GET_DETAIL_SI = "GetDetailSI";
    public static String RequestType_DELETE_SI = "DeleteSI";
    public static String RequestType_SI_EXECUTION = "SIExecution";
    public static String RequestType_Verify_User = "verifyUser";
    public static String RequestType_AMMA_Update_Account = "AMMAUpdateAccount";
    public static String RequestType_CardList="CardList";

    public static String RequestType_AccountRegistration = "AccountRegistration";
    public static String RequestType_SetMPin = "Mpin";
    public static String RequestType_ChangeMPin = "ChangeMPin";
    public static String RequestType_Ministatement = "miniStatement";
    public static String RequestType_BOP_BalanceInquiry = "balanceInquiry";
    public static String RequestType_BOP_MPinReset = "mPINReset";
    public static String RequestType_BOP_AtmPinReset = "atmPinReset";
    public static String RequestType_BOP_ChannelActivation = "channelActiation";
    public static String RequestType_BOP_CardStatusUpdate = "cardStatusUpdate";
    public static String RequestType_BOP_CashWithdrawalInquiry = "CashWithdrawalInquiry";
    public static String RequestType_BOP_CashWithdrawal = "CashWithdrawal";
    public static String RequestType_AgentVerification = "AgentVerification";

    public static String RequestType_BOP_CashOutInquiry="CashOutInquiry";
    public static String RequestType_BOP_CashOut="CashOut";
    public static String RequestType_BOP_AccountRegistrationInquiry="AccountRegistrationInquiry";
    public static String RequestType_BOP_AccountRegistration="AccountRegistrationBop";
    public static String RequestType_BOP_CashWithdrawalReversal="CashWithdrawalReversal";
    public static String RequestType_BOP_CardIssuanceReIssuanceInquiry="CardIssuanceReIssuanceInquiry";
    public static String RequestType_BOP_CardIssuanceReIssuance="CardIssuanceReIssuance";
    public static String RequestType_BOP_ProofOFLifeVerficationInquiry="ProofOFLifeVerficationInquiry";
    public static String RequestType_BOP_ProofOfLifeVerification="ProofOfLifeVerification";
    public static String RequestType_LoanManagement="loanManagement";


    public static String RequestType_GetInternationalTransactionWindow = "GetInternationalTransactionWindow";
    public static String RequestType_PostInternationalTransactionWindow = "PostInternationalTransactionWindow";
    public static String RequestType_DeleteIntenationalTransactionWindow = "DeleteInternationalTrnsactionWindow";
    public static String RequestType_AccountEmailStatement="AccountEmailStatement";
    public static String RequestType_AccountClose="AccountClose";
    public static String RequestType_DCEGetCurrentLimitProfile="GetCurrentLimitProfile";
    public static String RequestType_DCEGetLimitProfileList="GetLimitProfileList";
    public static String RequestType_DCEGetLimitProfileDetails="GetLimitProfileDetails";
    public static String RequestType_DCEGetPermissionProfileList="GetPermissionProfileList";
    public static String RequestType_DCEGetCurrentPermissionProfile="GetCurrentPermissionProfile";
    public static String RequestType_DCEGetPermissionProfileDetail="GetPermissionProfileDetail";
    public static String RequestType_DCESetLimitProfile="SetLimitProfile";
    public static String RequestType_DCESetTemporaryLimit="SetTemporaryLimit";
    public static String RequestType_DCESetTransactionPermission="SetTransactionPermission";
    public static String RequestType_BenificiaryHistory="BenificiaryHistory";

    //billing contants

    public static String RequestType_Billing_BillCategory = "BillCategory";
    public static String RequestType_Billing_BillCategoryProducts = "BillCategoryProducts";
    public static String RequestType_Billing_BillInquiry = "BillInquiry";
    public static String RequestType_Billing_BillPayment = "BillPayment";
    public static String RequestType_Billing_BillStatus = "BillStatus";
    public static String RequestType_Billing_PaidBillSummary = "PaidBillSummary";
    public static String RequestType_Billing_FailedBillSummary = "FailedBillSummary";

    //Health and Nutrition

    public static String RequestType_BenificiaryDetails = "BenificiaryDetails";
    public static String RequestType_BenificiaryVisit = "BenificiaryVisit";
    public static String RequestType_Transaction = "Transaction";
    public static String RequestType_Cls_Pending_Bot_Status_Update = "clsPendingBotStatusUpdtae";
    public static String RequestType_SendPushNotification = "SendPushNotification";
    public static String RequestType_OPTASIA_OfferListForCommodity = "OptasiaOfferListForCommodity";
    public static String RequestType_OPTASIA_LOANOFFER = "OptasiaLoanOffer";
    public static String RequestType_OPTASIA_CALLBACK = "OptasiaCallBack";
    public static String RequestType_OPTASIA_LOANS = "OptasiaLoans";
    public static String RequestType_OPTASIA_PROJECTION = "OptasiaProjection";
    public static String RequestType_OPTASIA_OUTSTANDING = "OptasiaOutstanding";
    public static String RequestType_OPTASIA_TRANSACTIONS = "OptasiaTransactions";
    public static String RequestType_OPTASIA_LOANSUMMARY = "OptasiaLoanSummary";
    public static String RequestType_OPTASIA_PAYLOAN = "OptasiaPayLoan";
    public static String RequestType_OPTASIA_REPAYLOAN = "OptasiaRepayLoan";
    public static String RequestType_OPTASIA_ECIB_DATA = "OptasiaEcibData";
    public static String RequestType_Tasdeeq_AuthenticateUpdated = "TasdeeqAuthenticateUpdated";
    public static String RequestType_Tasdeeq_CustomAnalytics = "TasdeeqCustomAnalytics";
    public static String RequestType_Brandverse_AccessToken = "BrandverseToken";
    public static String RequestType_Brandverse_Notify = "BrandverseNotify";
    public static String RequestType_PDM_Validate = "PDMValidate";
    public static String RequestType_RAAST_CustomerAliasAccountID = "CustomerAliasAccountID";
    public static String RequestType_RAAST_GetDefaultAccountByAlias = "GetDefaultAccountByAlias";
    public static String RequestType_RAAST_GetCustomerInformation = "GetCustomerInformation";
    public static String RequestType_RAAST_GetCustomerAccounts = "GetCustomerAccounts";
    public static String RequestType_RAAST_GetCustomerAliases = "GetCustomerAliases";
    public static String RequestType_RAAST_DeleteAccount = "DeleteAccount";
    public static String RequestType_RAAST_DeleteAlias = "DeleteAlias";
    public static String RequestType_RAAST_DeleteCustomer = "DeleteCustomer";
    public static String RequestType_DEBIT_CARD_DiscrepancyStatus = "DebitCardDiscrepancyStatus";



}
