package com.inov8.microbank.common.util;

public interface WorkFlowErrorCodeConstants
{
    public final static String I8SB_SUCCESS_CODE = "I8SB-200" ;
    public final static String I8SB_PREFIX = "I8SB-" ;
    public final static String DISTRIBUTOR_CONTACT_NOT_ACTIVE = "6000" ;
	public final static String DISTRIBUTOR_CONTACT_NULL = "6001" ;
	public final static String DISTRIBUTOR_CONTACT_NOT_NATIONAL_MANAGER = "6002" ;
	public final static String RETAILER_CONTACT_NOT_ACTIVE = "6003" ;
	public final static String RETAILER_CONTACT_NULL = "6004" ;
	public final static String FROMRETAILER_CONTACT_NOT_HEAD = "6005" ;
	public final static String TRANSACTION_CODE_ID_NOT_SUPPLIED = "6006" ;
	public final static String TRANSACTION_MODEL_NULL = "6007" ;
	public final static String TRANSACTION_AMOUNT_NOT_SUPPLIED = "6008";
    public final static String CUSTOMER_MOBILENO_NOT_SUPPLIED = "6009" ;
    public final static String AGENT_MOBILENO_NOT_SUPPLIED = "6087" ;

    public final static String CUSTOMER_MODEL_NULL = "6010" ;
    public final static String AGENT_MODEL_NULL = "6088" ;
	public final static String TO_FROM_DISTRIBUTOR_SAME_LEVEL = "6011";
	public final static String OPERATOR_MODEL_NULL = "6012";
	public final static String OPERATOR_CONTACT_NOT_ACTIVE = "6013";
	public final static String PRODUCT_NOT_ACTIVE = "6014";
	public final static String PRODUCT_NULL = "6015";
	public final static String SUPPLIER_NOT_ACTIVE = "6016" ;
	public final static String SUPPLIER_NULL = "6017" ;
	public final static String FROMDISTRIBUTOR_NOT_ACTIVE = "6018" ;
	public final static String FROMDISTRIBUTOR_NULL = "6019" ;
	public final static String FROMRETAILER_NULL = "6020" ;
	public final static String FROMRETAILER_NOT_ACTIVE = "6021" ;
	public final static String TODISTRIBUTOR_NOT_ACTIVE = "6022" ;
	public final static String TODISTRIBUTOR_NULL = "6023" ;
	public final static String TORETAILER_NULL = "6024" ;
	public final static String TORETAILER_NOT_ACTIVE = "6025" ;
	public final static String NO_CUSTOMER_AGAINST_MOBILENO = "6026" ;
	public final static String RETAILER_NOT_HEAD = "6027" ;
	public final static String INSUFFICIENT_AMOUNT_TO_TRANSFER = "6028" ;
    public final static String SAME_TO_FROM_RETAILERS = "6029" ;
    public final static String NO_DISTRIBUTOR_AGAINST_MOBILENO = "6030" ;
    public final static String NO_RETAILER_AGAINST_MOBILENO = "6031" ;
    //public final static String DIST_CANT_TRANSFR_CREDIT = "6032" ;
    public final static String PRODUCT_STOCK_OUT = "6032" ;
    public final static String PIN_NOT_VERIFIED = "6033" ;
    public final static String NO_INSTRUCTION_FOR_PRODUCT = "6034" ;
    public final static String NO_DEVICE_ACCOUNT_FOR_USER = "6035" ;
    public final static String DIST_RETAIL_DIFF_DISTRIBUTOR = "6036" ;
    public final static String RETAIL_RETAIL_DIFF_DISTRIBUTOR = "6037" ;
    public final static String NO_NM_FOR_DISTRIBUTOR = "6038" ;
    public final static String RETAIL_RETAIL_DIFF_RETAILER = "6039" ;
    public final static String NOT_DISCRETE_PRODUCT = "6040" ;
    public final static String NOT_VARIABLE_PRODUCT = "6041" ;
    public final static String PROD_NOT_IN_CATALOG = "6042" ;
    public final static String INSUFFICIENT_BALANCE = "6043" ;
    public final static String INACTIVE_BANK_INFO = "6044" ;
    public final static String SERVICE_INACTIVE = "6045" ;
    public final static String BANKINFO_NULL = "6046" ;
    public final static String INSUFFICIENT_RETAILER_BALANCE = "6047" ;

    public final static String TORET_ACCOUNT_DISABLED = "6048" ;
    public final static String TORET_ACCOUNT_EXPIRED = "6049" ;
    public final static String TORET_ACCOUNT_LOCKED = "6050" ;
    public final static String TORET_ACCOUNT_CREDENTIALS_EXP = "6051" ;

    public final static String TODIST_ACCOUNT_DISABLED = "6052" ;
    public final static String TODIST_ACCOUNT_EXPIRED = "6053" ;
    public final static String TODIST_ACCOUNT_LOCKED = "6054" ;
    public final static String TODIST_ACCOUNT_CREDENTIALS_EXP = "6055" ;

    public final static String TORET_RETAILER_INACTIVE = "6056" ;
    public final static String TODIST_DISTRIBUTOR_INACTIVE = "6057" ;
    public final static String CUST_SMARTMONEY_INACTIVE = "6058" ;
    public final static String AGENT_SMARTMONEY_INACTIVE = "6089" ;
    public final static String CUST_SMARTMONEY_PIN_CHG_REQ = "6059" ;
    public final static String CUST_SMARTMONEY_NULL = "6060" ;
    public final static String AGENT_SMARTMONEY_NULL = "6090" ;
    public final static String PAYMENT_MODE_INACTIVE = "6061" ;
    public final static String CONSUMER_NO_NULL = "6062" ;
    public final static String PRODUCT_VO_NULL = "6063" ;

    public final static String BILL_AMOUNT_NULL = "6064" ;
    public final static String TX_PROCESS_AMOUNT_NULL = "6065" ;
    public final static String TOTAL_AMOUNT_NULL = "6066" ;
    public final static String TOTAL_COMM_NULL = "6067" ;

    //FIXME BILL_AMOUNT & related constants need to be replaced with Transaction amount
    public final static String BILL_AMOUNT_NOT_MATCHED = "6068" ;
    public final static String TRANSACTION_AMOUNT_NOT_MATCHED = "6068";
    public final static String TX_PROCESS_AMOUNT_NOT_MATCHED = "6069" ;
    public final static String TOTAL_AMOUNT_NOT_MATCHED = "6070" ;
    public final static String TOTAL_COMM_NOT_MATCHED = "6071" ;
    public final static String CHK_BAL_SERVICE_NOT_AVAILABLE = "6072" ;
    public final static String ROLLBACK_SERVICE_NOT_AVAILABLE = "6073" ;
    public final static String CREATE_ACCOUNT_SERVICE_NOT_AVAILABLE = "6079" ;
    public final static String DEBIT_CREDIT_ACCOUNT_SERVICE_NOT_AVAILABLE = "6080" ;
    public final static String CONNECTION_FAILED_UBL_SWITCH = "6074" ;
    public final static String GENERAL_ERROR = "6075" ;
    public final static String AGENT_TO_AGENT_TRNSFR_RANGE_ERROR = "6086" ;
    public final static String SUPPLIER_MERCH_NO_NULL = "6075" ;
    public final static String OPERATOR_BANK_INFO_NULL = "6075" ;
    public final static String SWITCH_INACTIVE = "6076" ;
    public final static String SERVICE_UNAVAILABLE = "6076" ;
    public final static String INVALID_CUSTOMER_ACCOUNT = "6078" ;
    public final static String INVALID_SERVICE_TYPE = "6080" ;
    
    public final static String DIST_CONTACT_INACTIVE = "6081" ;
    public final static String RET_CONTACT_INACTIVE = "6082" ;
    public final static String DIST_INACTIVE = "6083" ;
    public final static String RET_INACTIVE = "6084" ;
    public final static String INACTIVE_OR_NO_SMA = "6085" ;
    
    
    
    public final static String VERIFLY_INACTIVE = "Service unavailable due to technical difficulties, please try again or contact service provider." ;
    public final static String GENERAL_ERROR_MSG = "Service unavailable due to technical difficulties, please try again or contact service provider." ;
    public final static String CONSUMER_NO_INVALID = "Incorect Consumer Number. Please retry." ;
    public final static String INTEGRATION_ERROR = "An error has occured, please try again or contact your service provider." ;
    public final static String BILL_PAYMENT_AFT_DUE_DATE = "Sorry, but MWallet cannot accept payments for over due bills." ;
    public final static String BILL_ALREADY_PAID_MSG = "The Utility Bill has already been paid." ;
    public final static String REFERENCE_NUMBER_BLOCKED = "This Utility Bill is blocked. Please contact Utility Bill Company." ;
    public final static String DISCOUNT_AMOUNT_EXCEEDS_PRICE = "7028" ;
    public final static String HRA_ERROR_MSG = "Transfer of funds to HRA Account is not allowed.";

    // ******************************************************************************************************
   
    public final static String PAYMENT_PROCESSING_FAILED = "7010" ;    
	// Change by Sheraz on May 16th, 2008

    //*******************************************************************************************************

    public final static String SERVICE_DOWN = "7001" ;
    public final static String SERVICE_DOWN_MSG = "Your request cannot be processed at the moment. Please try again later." ;
    
//  =============================== IRIS SPECIFIC ERROR CODES =============================================
    
    public final static String IRIS_SERVICE_DOWN = "8000" ;
    public final static String IRIS_INVALID_ACCOUNT = "8001" ;
    public final static String IRIS_ACCOUNT_INFO_MISSING = "8002" ;
    public final static String IRIS_UTILITY_COMPANY_CODE = "8003" ;

    public final static String RDV_SERVICE_DOWN = "Your session has expired. Please try again.";


//  *******************************************************************************************************
    
    
//  =============================== PHOENIX SPECIFIC ERROR CODES =============================================    
    public final static String PHOENIX_SERVICE_DOWN = "8050" ;
    public final static String PHOENIX_INVALID_ACCOUNT = "8051" ;
    public final static String PHOENIX_ACCOUNT_INFO_MISSING = "8052" ;
    public final static String PHOENIX_UTILITY_COMPANY_CODE = "8053" ;
    public final static String PHOENIX_CUSTOMER_PROFILE_NOT_FOUND = "8054" ;
    public final static String PHOENIX_PIN_ALREADY_GENERATED_MSG = "PIN Generation failed. Invalid status. Please contact service provider." ;
    public final static String PHOENIX_CUSTOMER_PROFILE_NOT_FOUND_MSG = "PIN Change request failed. Invalid Status. Please contact service provider." ;
    public final static String PHOENIX_INVALID_PAYMENT_MODE_CREDIT_CARD = "In-correct payment mode selected. Cannot use credit cards for this transaction." ;
    public final static String PHOENIX_INVALID_PAYMENT_MODE_DEBIT_CARD = "In-correct payment mode selected. Cannot use debit cards for this transaction." ;
    
    public final static String PHOENIX_SERVICE_DOWN_MSG = "Your request cannot be processed at the moment. Please try again later." ;
    public final static String PHOENIX_INVALID_CUSTOMER_PROFILE = "Customer Account could not be verified." ;
    public final static String PHOENIX_ACT_CHANNEL_REQ_FAILED = "Request could not be processed. Channel activation failed. Please try again.";
    public final static String PHOENIX_DEACT_CHANNEL_REQ_FAILED = "Request could not be processed. Channel deactivation failed. Please try again.";
    public final static String PHOENIX_INVALID_IVR_RESP = "In-correct IVR channel status.";
    public final static String PHOENIX_CUSTOMER_PROF_NOT_EXIST = "Customer profile does not exist." ;
    public final static String PHOENIX_CUSTOMER_ACC_NOT_EXIST = "Account has not been verified" ;
    public final static String PHOENIX_ACCOUNT_INACTIVE = "Your transaction can not be processed because your Bank account or Credit Card is in active. Please call your bank to resolve this issue." ;
    public final static String PHOENIX_INCORRECT_PIN = "The PIN you entered is incorrect. Please try again." ;
    
    public final static String PHOENIX_PIN_RETRIES_EXHAUSTED = "Your account has been locked due to maximum number of PIN retries. Please call your bank to resolve this issue." ;
    public final static String PHOENIX_INVALID_ACCOUNT_STATUS = "Your account status is invalid. Please call your bank to resolve this issue." ;
    public final static String PHOENIX_ACCOUNT_LOCKED = "Your account status is Locked. Please call your bank to resolve this issue." ;
    public final static String PHOENIX_PIN_EXPIRED = "Your Bank PIN has expired. Please call your bank to resolve this issue." ;
     
    public final static String PHOENIX_CARD_STATUS_ERROR = "Your transaction can not be processed because your card status is not active. Please call your bank to resolve this issue." ;
    public final static String PHOENIX_WARM_CARD = "Your account has been locked due to maximum number of PIN retries. Please call your bank to resolve this issue." ;
    public final static String PHOENIX_HOT_CARD = "Your transaction can not be processed because your card status is not active. Please call your bank to resolve this issue." ;
    public final static String PHOENIX_BAD_CARD_STATUS = "Your transaction can not be processed because your card status is not active. Please call your bank to resolve this issue." ;
    
    public final static String RETAILER_ID = "retailerId" ;
    public final static String DISTRIBUTOR_ID = "distributorId" ;
    public final static String ALLPAY_COMM_TRANS_ID = "allPayCommTransId" ;
    
//  *******************************************************************************************************
    
    public final static String FUNCTIONALITY_NOT_AVAILABLE_MSG = "This facility is not available" ;
    
    public final static String ACTION_LOG_ID_NOT_PROVIDED = "ActionLogId is not available in ThreadLocal." ;

    public final static String INVALID_TPIN = "7022";
    public final static String INVALID_TPIN_MESSAGE = "Invalid TPin";
    public final static String COMPANY_NOT_FOUND = "7026";
    public final static String BILL_PAST_DUE_DATE = "7027";
    public final static String SUCCESS = "S";
    public final static String FAILURE = "F";

    public final static String TRX_IN_PROCESS_MSG = "Transaction is already in process." ;
//  =============================== PHOENIX SPECIFIC ERROR CODES =============================================
    
    public final static String NO_ACCOUNTS_FOUND = "No accounts found." ;
    
    public final static String UNKNOWN_ERROR_MSG = "Your request cannot be processed at the moment. Please try again later." ;
        
    
//  =============================== ZONG SPECIFIC ERROR CODES =============================================    
    
    public final static String ZONG_TOPUP_MOB_NOT_FOUND_ERROR_MSG = "Invalid number for Zong Top Up. Please enter a correct pre-paid number." ;
    public final static String ZONG_BILL_PAYMENT_MOB_NOT_FOUND_ERROR_MSG = "Invalid number for Zong Bill Payment. Please enter a correct post paid number." ;
    
//  =============================== OLA SPECIFIC ERROR CODES =============================================
    
    public final static String INACTIVE_ACCOUNT = "8055" ;
    public final static String DELETED_ACCOUNT = "8056" ;
    public final static String DATA_TYPE_MISMATCH = "8057" ;
    public final static String TRANSACTION_TIMEOUT = "8058" ;
    public final static String INSUFFICIENT_ACC_BALANCE = "8059" ;
    public final static String CNIC_UNIQUE_CONSTRAINT = "8060" ;
    public final static String DAILY_DEBIT_LIMIT_BUSTED = "8062" ;
    public final static String DAILY_DEBIT_LIMIT_MESSAGE = "Per day limit of Sender exceeded.";
    public final static String MONTHLY_DEBIT_LIMIT_MESSAGE = "Per month limit of Sender exceeded.";
    public final static String MONTHLY_DEBIT_LIMIT_BUSTED = "8064" ;
    public final static String YEARLY_DEBIT_LIMIT_BUSTED = "8065" ;
    public final static String DAILY_CREDIT_LIMIT_BUSTED = "8061" ;
    public final static String MONTHLY_CREDIT_LIMIT_BUSTED = "8063" ;
    public final static String DAILY_CREDIT_LIMIT_MESSAGE = "Per day limit of Recipient exceeded.";
    public final static String MONTHLY_CREDIT_LIMIT_MESSAGE = "Per month limit of Recipient exceeded.";
    public final static String YEARLY_CREDIT_LIMIT_BUSTED = "8066" ;
    public final static String BALANCE_LIMIT_BUSTED = "8067" ;
    public final static String MONTHLY_CREDIT_THROUGHPUT_LIMIT_BUSTED = "8068" ;
    public final static String MONTHLY_THROUGHPUT_LIMIT_NOT_DEFINED = "8069" ;
    public final static String MONTHLY_THROUGHPUT_LIMIT_EXCEPTION = "8070" ;
    public final static String MONTHLY_DEBIT_THROUGHPUT_LIMIT_BUSTED = "8071" ;
    public final static String AGENT_ACCOUNT_NOT_FOUND = "8072";

    public final static String POOL_ACCOUNT_MISSING = "8073";
    public final static String DISTRIBUTOR_INFO_MISSING_TRX = "8074";
    public final static String ACC_TYPE_INFO_MISSING = "8075";

    public final static String CUSTOMER_NOT_REGISTERED = "8076";
    public final static String USABLE_ACCOUNT_BALANCE_INSUFFICIENT = "8077";
    public final static String LEG2_ALREADY_PERFORMED = "8078";
    public final static String IVR_AUTHORIZATION_FAILED = "8079";
    public final static String MONTHLY_BVS_LIMIT_NOT_DEFINED = "8080" ;
    public final static String DEBIT_CREDIT_NOT_EQUAL = "8081";
    public final static String DEBIT_BLOCKED = "8091";

    public final static String RECIPIENT_MOBILE_NUMBER = "8090";
    public final static String RECIPIENT_MOBILE_NUMBER_MESSAGE = "Invalid recipient mobile number or branchless banking account. Please enter the correct number.";

    public final static String INVALID_BANK_PIN = "In-valid Bank PIN, please enter valid Bank PIN.";
    public final static String INSUFFICIENT_ACC_BALANCE_MESSAGE = "Transaction cannot be processed due to insufficient balance.";

}

