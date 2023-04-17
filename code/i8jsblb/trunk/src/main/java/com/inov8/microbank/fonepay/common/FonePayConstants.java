package com.inov8.microbank.fonepay.common;

public interface FonePayConstants {

    public static final String CUSTOMER_STATUS_ACTIVE = "1";
    public static final String CUSTOMER_STATUS_INVALID = "0";


    public static final String ACCOUNT_INQUIRY_NEW = "01";
    public static final String ACCOUNT_INQUIRY_EXISTING = "02";

    public static final String ACCOUNT_LINK = "01";
    public static final String ACCOUNT_DELINK = "02";
    public static final String CARD_ACTIVATE = "01";
    public static final String CARD_DEACTIVATE = "02";
    public static final String CARD_DELETE = "03";

    // RequestTypes for FonePayIntegrationLog
    public static final String REQ_VERIFY_NEW_ACCOUNT = "Verify New Customer";
    public static final String REQ_VERIFY_EXISTING_ACCOUNT = "Verify Existing Customer";
    public static final String REQ_ACCOUNT_OPENING = "Account Opening";
    public static final String REQ_ACCOUNT_OPENING_CONVENTIONAL = "Conventional Account Opening";
    public static final String REQ_ACCOUNT_OPENING_L2 = "Level2 Account Opening";
    public static final String REQ_ACCOUNT_LINK = "Account Link";
    public static final String REQ_ACCOUNT_DELINK = "Account DeLink";
    public static final String REQ_REVERSAL = "Payment Reversal";
    public static final String REQ_VERIFY_OTP = "Verify OTP";
    public static final String REQ_CARD_TAGGING = "Virtual Card Tagging";
    public static final String REQ_PAYMENT_INQUIRY = "Payment Inquiry";
    public static final String REQ_PAYMENT_FONEPAY = "FonePay Payment";
    public static final String REQ_PAYMENT_CARD = "Virtual Card Payment";
    public static final String REQ_CARD_ACTIVATE = "Virtual Card Activation";
    public static final String REQ_CARD_DEACTIVATE = "Virtual Card De-Activation";
    public static final String REQ_CARD_DELETE = "Virtual Card Delete";

    public static final String REQ_BALANCE_INQUIRY_INFO = "Balance Inquiry Info";
    public static final String REQ_BALANCE_INQUIRY = "Balance Inquiry";
    public static final String REQ_MINI_STATMENT_INFO = "Mini Statment Info";
    public static final String REQ_MINI_STATMENT = "Mini Statment";
    public static final String REQ_DEBIT_CARD_ISSUANCE_INQUIRY = "Debit Card Issuance Inquiry";
    public static final String REQ_DEBIT_CARD_ISSUANCE = "Debit Card Issuance";
    public static final String REQ_CHALLAN_PAYMENT_INQUIRY = "Challan Payment Inquiry";
    public static final String REQ_CHALLAN_PAYMENT = "Challan Payment";
    public static final String REQ_BILL_PAYMENT_INQUIRY = "Bill Payment Inquiry";
    public static final String REQ_BILL_PAYMENT = "Bill Payment";
    public static final String REQ_CASH_IN_INFO = "CashIn Info";
    public static final String REQ_CASH_IN = "CashIn";
    public static final String REQ_AGENT_CASH_IN = "AgentCashIn";
    public static final String REQ_CASH_OUT_INFO = "Cashout Info";
    public static final String REQ_CASH_OUT = "Cashout";
    public static final String REQ_WALLET_TO_WALLET_INFO = "Wallet to Wallet Info";
    public static final String REQ_ACC_LINK_DELINK = "Account Link Delink";
    public static final String REQ_HRA_REGISTRATION_INQUIRY = "Hra Registration Inquiry";
    public static final String REQ_HRA_REGISTRATION = "Hra Registration";
    public static final String REQ_HRA_TO_WALLET_INQUIRY = "Hra To Wallet Inquiry";
    public static final String REQ_HRA_TO_WALLET= "Hra To Wallet";
    public static final String REQ_DEBIT_INQUIRY= "Debit Inquiry";
    public static final String REQ_DEBIT_PAYMENT= "Debit Payment";
    public static final String REQ_CREDIT_INQUIRY= "Credit Inquiry";
    public static final String REQ_CREDIT_PAYMENT= "Credit Payment";
    public static final String REQ_HRA_CASHWITHDRAWAL_PAYMENT_INQUIRY= "Hra Cashwithdrawal Payment Inquiry";
    public static final String REQ_HRA_CASHWITHDRAWAL_PAYMENT= "Hra Cashwithdrawal Payment";
    public static final String REQ_LOGIN_AUTHENTICATION= "Login Authentication";
    public static final String REQ_lOGIN_PIN= "Login Pin";
    public static final String REQ_LOGIN_PIN_CHANGE= "Login Pin Change";
    public static final String REQ_RESET_LOGIN_PIN= "Login Pin Reset";
    public static final String REQ_ADVANCE_SALARY_LOAN= "Advance Salary Loan";
    public static final String REQ_MPIN_Verification= "MPIN Verificaton";
    public static final String REQ_L2_UPGRADE = "UpgradeL2Account";
    public static final String REQ_MERCHANT_ACCOUNT_UPGRADE = "MerchantAccountUpgrade";


    // FONE PAY PAYMENT TRANSACTION TYPES
    public static final String WEB_SERVICE_PAYMENT = "01";
    public static final String VIRTUAL_CARD_PAYMENT = "02";
    public static final String AGENT_SETTLEMENT_PAYMENT = "03";
    public static final String TRANSACTION_TYPE = "Transaction Type";
    public static final String PAYMENT_TYPE = "Payment Type";
    public static final String VIRTUAL_CARD_NO = "Virtual Card No";
    public static final String VIRTUAL_CARD_EXPIRY = "Virtual Card Expiry";

    public static final String KEY_EXTERNAL_TRANSACTION_CODE = "ETCODE";
    public static final String KEY_EXTERNAL_PRODUCT_NAME = "ETPNAME";
    public static final String KEY_FONEPAY_SETTLEMENT_TYPE = "FPSETTLEMENT";

    public static final String L0_CUSTOMER = "01";
    public static final String L1_CUSTOMER = "02";

    public static final Long FONEPAY_AGENT_NETWORK=1001L;

    public static final String FONEPAY_BALANCE_INQUIRY="01";
    public static final String FONEPAY_MINI_STATMENT="02";
    public static final String FONEPAY_WALLET_TO_WALLET="03";
    public static final String FONEPAY_ACC_LINK_DELINK="04";

    public static final String APIGEE_CHANNEL="APIGEE";
    public static final String NOVA_CHANNEL="NOVA";
    public static final String PAYFAST_CHANNEL="PAYFAST";
    public static final String PAYFAST_COMM_CHANNEL="PAYFAST-COMM";
    public static final String PAYFAST_UBPS_CHANNEL="PAYFAST-UBPS";
    public static final String PAYFAST_WTOW_CHANNEL="PAYFAST-WTOW";
    public static final String VENDI_CHANNEL="VENDI";
    public static final String BRANDVERSE_CHANNEL="CHIKOO";
    public static final String ECOFIN_CHANNEL="ECOFIN";
    public static final String TELEMART_CHANNEL="TELEMART";
    public static final String RAHPER_CHANNEL= "RAHPER";
    public static final String GOLOOTLO_CHANNEL= "GOLOOTLO";
    public static final String  MARHAM_CHANNEL= "MARHAM";
    public static final String AIGENIX_CHANNEL= "AIGENIX";
    public static final String VOUCH365_CHANNEL= "VOUCH365";
    public static final String PUBLISHEX_CHANNEL= "PUBLISHEX";
    public static final String TAPMAD_CHANNEL= "TAPMAD";
    public static final String OPTASIA_CHANNEL= "OPTASIA";
    public static final String ECOMM_CHANNEL="ECOMM";
    public static final String KUICK_PAY_CHANNEL="KUICKPAY";
    public static final String QUWAT_CHANNEL="QUWAT";







    public static final String DEBIT_CARD_CARDLESS_CHANNEL="DEBIT_CARD_CARDLESS";
    public static final String DEBIT_CARD_CHANNEL="DEBIT_CARD";
    public static final String WALLET_2_WALLET_INFO="WALLET_TO_WALLET_INFO";
    public static final String WALLET_2_WALLET="WALLET_TO_WALLET";
    public static final String WALLET_2_CNIC_INFO="WALLET_TO_CNIC_INFO";
    public static final String WALLET_2_CNIC="WALLET_TO_CNIC";
    public static final String WALLET_2_CORE_INFO="WALLET_TO_CORE_INFO";
    public static final String WALLET_2_CORE="WALLET_TO_CORE";
    public static final String FUND_WALLET_2_CORE_INFO="FUND_WALLET_TO_CORE_INFO";
    public static final String FUND_WALLET_2_CORE="FUND_WALLET_TO_CORE";
    public static final String FEE_PAYMENT_INFO="FEE_PAYMENT_INFO";
    public static final String FEE_PAYMENT="FEE_PAYMENT";



    public static final String MPIN_RETRY_COUNT = "3";
   // public static final String MPIN_REFRESH_TIME = "60";
    public static final Long MPIN_REFRESH_TIME =new Long(60);

    public static final String REQ_DEBIT_CARD_CASH_WITHDRAWL_ON_US = "Debit Card Cash WithDrawl ON - US";
    public static final String REQ_DEBIT_CARD_LESS_CASH_WITHDRAWL = "Card Less Cash WithDrawl";

    public static final String REQ_DEBIT_CARD_CASH_WITHDRAWL_OFF_US = "Debit Card Cash WithDrawl OFF - US";
    public static final String REQ_INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWL_OFF_US = "International Debit Card Cash WithDrawl OFF - US";

    public static final String REQ_POS_DEBIT_CARD_CASH_WITHDRAWL = "POS Debit Card Cash WithDrawl";
    public static final String REQ_POS_DEBIT_CARD_REFUND = "POS Debit Card Refund";
    

}
