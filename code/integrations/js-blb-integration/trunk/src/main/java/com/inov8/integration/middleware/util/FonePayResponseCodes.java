package com.inov8.integration.middleware.util;

public interface FonePayResponseCodes {

    String SUCCESS_RESPONSE_CODE = "00";
    String INPUT_ERROR = "09"; // Invalid input
    String GENERAL_ERROR = "10"; // An error has occurred, please try again or contact your service provider
    String LOGIN_FAILURE = "11"; // An error has occurred, please try again or contact your service provider
    String SMA_NOT_LOADED = "12"; // An error has occurred, please try again or contact your service provider
    String SMA_NOT_ACTIVE = "13"; //Account is not Active
    String CUSTOMER_NOT_FOUND = "14"; // No Customer found against given CNIC/Mobile Number
    String CUSTOMER_ACCOUNT_CLOSED = "15"; //Customer account has been Closed
    String CUSTOMER_ACCOUNT_EXPIRED = "16"; //Customer account has expired
    String CUSTOMER_ACCOUNT_BLOCKED = "17"; //Customer account has been blocked
    String CUSTOMER_ACCOUNT_CREDENTIALS_EXP = "18"; //Customer credentials have been expired
    String CUSTOMER_ACCOUNT_DEACTIVATED = "19"; //Customer account has been deactivated
    String CUSTOMER_ACCOUNT_CREDENTIALS_MISSING = "20"; //No Credentials Exist Against Customer
    String CUSTOMER_ACCOUNT_CREDENTIALS_INVALID = "21"; //Invalid Credentials against Customer
    String CUSTOMER_CNIC_BLACKLISTED = "22"; //Customer CNIC is blacklisted. Please contact your service provider
    String CUSTOMER_MOBILE_ALREADY_EXIST = "23"; //Mobile No is already in use
    String CUSTOMER_CNIC_ALREADY_EXIST = "24"; //CNIC is already in use
    String WEB_SERVICE_NOT_ENABLED = "25"; //Service disabled
    String ACCOUNT_OPENING_VALIDATION_FAILED = "26"; //Invalid input provided
    String ACCOUNT_OPENING_FAILED = "27"; //An error has occurred, please try again or contact your service provider
    String FONEPAY_NOT_ENABLED = "28"; //FonePay service not enabled

    String ACCOUNT_OPENING_AGE_LIMIT_FAILED="156";
    String ACCOUNT_OPENING_CNIC_EXPIRY_FAILED="157";

    String CARD_ALREADY_TAG="51"; //Virtual Card Already Tagged
    String CARD_IS_BLOCKED = "52";//Virtual Card Is Blocked
    String CARD_IS_EXPIRED = "53";//Virtual Card Is Expired

    String CARD_EXPIRY_INCORRECT = "54";//Expiry Date Is Incorrect.
    String CARD_NOT_FOUND = "55";//Virtual Card Is Avalible
    String TRANSACTION_CODE_NOT_AVALIBLE="56"; //Virtual Card Already Tagged
    String TRANSACTION_CODE_INCORRECT="57"; //Virtual Card Already Tagged

    String DEVICE_OTP_INVALID="58";
    String DEVICE_OTP_EXPIRED="59";
    String SUCCESS_RESPONSE_DESCRIPTION = "Successful";
    String LOAN_DATA_NOT_FOUND = "Loan Data Not Found";



    String TRXN_AMOUNT_GREATER_THAN_LIMIT = "61"; //The amount provided is greater than the maximum Product Limit.
    String TRXN_AMOUNT_LESSER_THAN_LIMIT = "60"; //The amount provided is lesser than the maximum Product Limit.

    // Fonepay PIN response codes
    public static final Long INVALID_PIN = 63L;
    String RETRY_LIMIT_EXHAUSTED="62";
    public static final Long PIN_CHANGE_REQUIRED=64L;
    String COMMAND_GENERAL_EXCEPTION="65";
    String ACCOUNT_INVALID_STATE="66";
    String SAME_PIN = "112"; //Old Pin and New Pin Cannot be Same.
    String PIN_MISMATCHED = "113"; //New pin and Confirm Pin are not same.
    String BILL_ALREADY_PAID="131";
    String REFERENCE_NUMBER_BLOCKED="132";
    String INVALID_AMOUNT="133";
    String RDV_DOWN="134";
    String CONSUMER_NUMBER_INVALID="135";

    String CUSTOMER_MOBILE_ALREADY_EXIST_AS_CUSTOMER = "67"; //Mobile No is already in use as a customer
    String CUSTOMER_MOBILE_ALREADY_EXIST_AS_RETAILER = "68"; //Mobile No is already in use as a retailer
    String CUSTOMER_MOBILE_ALREADY_EXIST_AS_HANDLER = "69"; //Mobile No is already in use as a handler

    String CUSTOMER_CNIC_ALREADY_EXIST_AS_CUSTOMER = "70"; //CNIC is already in use as a customer
    String CUSTOMER_CNIC_ALREADY_EXIST_AS_RETAILER = "71"; //CNIC is already in use as a retailer
    String CUSTOMER_CNIC_ALREADY_EXIST_AS_HANDLER = "72"; //CNIC is already in use as a handler
    String CNIC_BLACKLISTED = "73"; //CNIC is already in use as a handler
    String ACCOUNT_CLOSED = "74"; //CNIC is already in use as a handler
    String ACCOUNT_EXPIRED = "75"; //CNIC is already in use as a handler
    String ACCOUNT_BLOCKED = "76"; //CNIC is already in use as a handler
    String ACCOUNT_CREDENTIALS_EXP = "77"; //CNIC is already in use as a handler
    String ACCOUNT_DEACTIVATED = "78"; //CNIC is already in use as a handler
    String ACCOUNT_CREDENTIALS_INVALID = "79"; //CNIC is already in use as a handler
    String ACCOUNT_CREDENTIALS_MISSING = "80"; //CNIC is already in use as a handler
    String AGENT_NOT_FOUND = "81"; //CNIC is already in use as a handler

    String INVALID_REQUEST = "82"; //CNIC is already in use as a handler
    String TRXN_AMOUNT_DECIMAL_PLACES = "83";
    String CUST_HAS_PEND_TRXNS = "84";

    String LOW_BALANCE = "04";
    String INVALID_LOGIN_PIN = "63";
    Long STAN_ALREADY_EXISTS = 86L;
    String STAN_ALREADY_EXISTS_DESCRIPTION = "Stan already Exists.";

    String INTERNATIONAL_TRANSACTION_NOT_ALLOWED= "08";
    String INTERNATIONAL_TRANSACTION_NOT_ALLOWED_DESCRIPTION="Transaction Not Allowed For Level 1";


    Long APIGEE_RRN_ALREADY_EXISTS = 88L;
    String APIGEE_RRN_ALREADY_EXISTS_DESCRIPTION = "APIGEE RRN Already Exists.";

    String DAILY_DEBIT_LIMIT_BUSTED = "140" ;
    String MONTHLY_DEBIT_LIMIT_BUSTED = "141" ;

    String DAILY_CREDIT_LIMIT_BUSTED = "145";
    String INSUFFICIENT_ACC_BALANCE = "147";
    String MONTHLY_CREDIT_LIMIT_BUSTED = "146";
    String RECIPIENT_MOBILE_NUMBER="142";
    String INVALID_OLD_PIN="143";
    String MEMBER_BANK_NOT_FOUND= "148";
    String ACC_ALREADY_DELINKED= "149";
    String ACC_ALREADY_LINKED= "150";
    String BAL_NOT_ZERO= "151";
    String MPIN_CHANGE_REQ  = "152";
    String INVALID_CNIC  = "153";

    String ACCOUNT_STATE_HOT = "Account State is Hot";
    String ACCOUNT_STATE_WARM = "Account State is Warm";
    String ACCOUNT_STATE_COLD = "Account State is Cold";
    String ACCOUNT_STATE_DECEASED = "Account State is Deceased";
    String ACCOUNT_STATE_DORMANT = "Account State is Dormant";
    String ACCOUNT_STATE_CLOSED = "Account State is Closed";

    String PIN_IS_NUMERIC="154";
    String PRODUCT_NOT_FOUND="155";
    String BLINK_CUSTOMER_DATA_ALREADY_EXISTS="158";
    String BB_WALLET_NOT_REGISTERED="159";
    String DEBIT_CARD_ALREADY_EXISTS="160";
    String LOAN_DATA="162";
    String CUSTOMER_EMAIL_ADDRESS_ALREADY_EXISTS = "161"; //CNIC is already in use as a customer
    String AMA_CHANNEL = "25"; //This response code is set for account opening via AMA channel
    String DEBIT_BLOCKED="26";

    String FINGER_DOES_NOT_EXIT="111";
    String NADRA_FINGER_EXAUST_ERROR="118";
    String INVALID_INPUT_FINGER_TEMPLETE="120";
    String FINGER_PRINT_NOT_MATCHED="121";
    String INVALID_FINGER_INDEX="122";
    String INVALID_FINGER_TEMPLETE_TYPE="123";
    String TRANSACTION_NOT_FOUND="165";
    String LOAN_ALEADY_AVAILED="191";
    String CREDIT_INQUIRY_RRN_NOT_FOUND="192";
    String CREDIT_INQUIRY_RRN_NOT_FOUND_DESCRIPTION = "Credit Inquiry RRN Not Exists.";


}
