package com.inov8.microbank.common.util;

public class CommandFieldConstants
{
//*****************************************************//
//**********Commented By Rizwan-ur-Rehman**************//
//*****************************************************//
	
//	 OUTBOUND KEYS
    public static final String KEY_REQ_ID = "reqId";
    public static final String KEY_CURRENT_REQ_TIME = "reqTime";
    public static final String KEY_APP_ID = "APPID";
    public static final String KEY_APP_VER = "APPV";
    public static final String KEY_APP_NAME = "APPN";
    public static final String KEY_ACC_ID = "ACID";
    public static final String KEY_CONF_PIN = "CPIN";
    public static final String KEY_FAV_NO_LIST = "FVNL";
    public static final String KEY_END_TX_NO = "ETNO";
    public static final String KEY_NEW_PIN = "NPIN";
    public static final String KEY_PIN = "PIN";
    public static final String KEY_MAN_OT_PIN = "MANUAL_OTPIN";
    public static final String KEY_MOB_NO = "MOBN";
    public static final String KEY_CW_CUSTOMER_MOBILE = "CWCUSTMOB";
    public static final String KEY_RP_NAME = "RPNAME";
    public static final String KEY_CSCD = "CSCD";
    public static final String KEY_CD_CUSTOMER_MOBILE = "CDCUSTMOB";
    public static final String KEY_ONE_TIME_PIN = "OTPIN";
    public static final String KEY_ENCRYPTION_TYPE = "ENCT";
    public static final String KEY_IS_ROOTED = "ISROOTED";
    
    public static final String KEY_TXAM = "TXAM";
    public static final String KEY_TXAMF = "TXAMF";
    public static final String KEY_MOB_NO_FAV = "MOBN_FAV";
    public static final String KEY_CREDIT_CARD_NO = "CCNO";
    public static final String KEY_DEBIT_CARD_NO = "DCNO";
    public static final String KEY_DEBIT_CARD_BLOCK_TYPE = "DBTYPE";
    public static final String KEY_CREDIT_CARD_NO_FAV = "CCNO_FAV";
    public static final String KEY_CREDIT_CARD_OUTSTANDING_BAL = "CCOB";
    public static final String KEY_MOB_NO_TYPE = "MOBNTP";
    public static final String KEY_PROD_ID = "PID";
    public static final String KEY_RETAILER_CONTACT_ID = "RCID";
    public static final String KEY_ST_TX_NO = "STNO";
    public static final String KEY_TX_CODE = "TXCD";
    public static final String KEY_TX_CODE_MODEL = "TXCDMDL";
    public static final String KEY_TX_AMOUNT = "TXAM";
    public static final String KEY_FORMATTED_TX_AMOUNT = "TXAMF";
    public static final String KEY_TOTAL_AMOUNT = "TAMT";
    public static final String KEY_DISCOUNT_AMOUNT = "DAMT";
    public static final String KEY_FORMATED_DISCOUNT_AMOUNT = "DAMTF";
    public static final String KEY_FORMATED_TOTAL_AMOUNT = "TAMTF";
    public static final String KEY_FORMATED_INCLUSIVE_CHARGES = "incrhge";
    public static final String KEY_FORMATED_FIX_INCLUSIVE_CHARGES = "fixIncrhge";
    public static final String KEY_CONS_REF_NO = "CRNO";
    public static final String KEY_KENNEL_SMS_DATA = "SMS";
     
    public static final String KEY_CNAME      = "CNAME";       //Customer Name
    public static final String KEY_SEGMENT_ID     = "SEGMENT_ID";       //Customer Name
    public static final String KEY_CDOB      = "CDOB";       //Customer DOB
    public static final String KEY_DEPOSIT_AMT = "DEPOSIT_AMT";       
    public static final String KEY_CUST_ACC_TYPE = "CUST_ACC_TYPE";       
    public static final String KEY_CREG_COMMENT = "CREG_COMMENT";       
    public static final String KEY_CMSISDN    = "CMSISDN";     //Customer MSISDN
    public static final String KEY_CREG_STATE = "CREG_STATE";  //Customer Registration State
    public static final String KEY_CREG_STATE_ID = "CREG_STATE_ID";  //Customer Registration State ID
    public static final String KEY_CUST_DISCREPANT_STATUS = "CUST_DISCREPANT_STATUS";
    public static final String KEY_CASE_STATUS = "CASE_STATUS";

    
    
    public static final String KEY_TERMS_PHOTO = "TERMS_PHOTO_FLAG";
    public static final String KEY_SIGNATURE_PHOTO = "SIGNATURE_PHOTO_FLAG";
    public static final String KEY_CUSTOMER_PHOTO = "CUSTOMER_PHOTO_FLAG";
    public static final String KEY_CNIC_FRONT_PHOTO = "CNIC_FRONT_PHOTO_FLAG";
    public static final String KEY_CNIC_BACK_PHOTO = "CNIC_BACK_PHOTO_FLAG";
    public static final String KEY_L1_FORM_PHOTO = "L1_FORM_PHOTO_FLAG";
    
    
    public static final String KEY_BILL_DATE = "BDATE";
    public static final String KEY_BILL_DUE_DATE = "BDDATE";
    public static final String KEY_BILL_DATE_OVERDUE = "BDATEOD";
    public static final String KEY_FORMATED_BILL_DATE = "BDATEF";
    public static final String KEY_LATE_BILL_AMT = "LBAMT";
    public static final String KEY_FORMATED_LATE_BILL_AMT = "LBAMTF";    
    public static final String KEY_LATE_BILL_DATE = "LBDATE";
    public static final String KEY_FORMATED_LATE_BILL_DATE = "LBDATEF";
    public static final String KEY_BILL_PAID = "BPAID";
    public static final String KEY_ACTUAL_BILL_AMT = "ACBAMT";
    public static final String KEY_FORMATED_ACTUAL_BILL_AMT = "ACBAMTF";  
    public static final String KEY_STATEMENTS = "STMTS";
    public static final String KEY_FORMATED_CREDIT_LIMIT = "CLMTF";
    public static final String KEY_FORMATED_MINIMUM_AMOUNT_DUE = "MAMTF";
    public static final String KEY_OPERATOR_MODEL = "OperatorModel";
    public static final String KEY_FAV_NUM_ID = "fnid";
    public static final String KEY_FAV_NUM = "num";
    public static final String KEY_TRANSACTION_TYPE = "TTYPE";
    public static final String KEY_FAV_NUM_TAG = "tag";
    
    
    public static final String KEY_U_ID = "UID";
    public static final String KEY_CAT_VER = "CVNO";
    public static final String KEY_CUST_CODE = "CSCD";
    public static final String KEY_CUST_CODE_FAV = "CSCD_FAV";
    public static final String KEY_APP_USER = "APPU";
    public static final String KEY_USER_DEVICE_ACCOUNTS_MODEL = "UDAM";
    public static final String KEY_TO_RET_ID = "TRID";
    public static final String KEY_TO_DIST_ID = "TDID";
    public static final String KEY_FROM_RET_ID = "FRID";
    public static final String KEY_FROM_DIST_ID = "FDID";
    public static final String KEY_DEVICE_TYPE_ID = "DTID";
    public static final String KEY_CVV = "CVV";
    public static final String KEY_TPIN = "TPIN";
    public static final String KEY_USER_TYPE = "USTY";
    public static final String KEY_AGENT_TYPE = "ATYPE";
    public static final String KEY_FIRST_NAME = "FNAME";
    public static final String KEY_LAST_NAME = "LNAME";
    public static final String KEY_CNIC = "CNIC";
    public static final String KEY_CNIC_EXPIRY = "CNIC_EXP";
    public static final String KEY_CNIC_ISSUE_DATE = "CNIC_ISSUE_DATE";
    public static final String KEY_EMAIL_ADDRESS = "EMAIL_ADDRESS";
    public static final String KEY_DELIVERY_CHNL_STATUS = "DCS";
    public static final String KEY_TILL_BALANCE_AMOUNT = "TBAM";
    public static final String KEY_BVS_ENABLED = "BVSE";
    public static final String KEY_AGENT_AREA_NAME = "AGENT_AREA_NAME";
    public static final String KEY_IS_CNIC_EXPIRY_REQUIRED = "IS_CNIC_EXPIRY_REQUIRED";
    
    
	public static final String KEY_CUSTOMER_CNIC = "CUSTOMERCNIC";
	public static final String KEY_CAMT = "CAMT";
	public static final String KEY_TPAM = "TPAM";
	public static final String KEY_TAMT = "TAMT";
	public static final String KEY_CAMTF = "CAMTF";
	public static final String KEY_TPAMF = "TPAMF";
	public static final String KEY_CTYPE = "CTYPE";
	public static final String KEY_TAMTF = "TAMTF";
	public static final String KEY_AGENT_MOB_NO = "AMOB";
	public static final String KEY_HANDLER_MOB_NO = "HMOB";
    public static final String KEY_ORDER_ID = "ORDERID";
    public static final String KEY_SERVICE_TYPE="STYPE";
    public static final String KEY_SERVICE_PROVIDER_NAME="SPNAME";
    public static final String KEY_BASE_FARE="BFARE";
    public static final String KEY_TOTAL_APPROX_AMOUNT="TAPAMT";
    public static final String KEY_TAX="TAX";
    public static final String KEY_FEE="FEE";
    public static final String KEY_CARD_FEE_TYPE_ID="CARD_FEE_TYPE_ID";
    public static final String KEY_BOOKME_NAME="BNAME";
    public static final String KEY_BOOKME_CNIC="BCNIC";
    public static final String KEY_BOOKME_EMAIL="BEMAIL";
    public static final String KEY_BOOKME_MOBILE_NO="BMOB";



    // INBOUND KEYS
    public static final String KEY_APP_USAGE_LVL = "APUL";
    public static final String KEY_BAL = "BAL";
    public static final String KEY_FORMATED_BAL = "BALF";
    public static final String KEY_CAT = "CAT";
    public static final String KEY_TX_PROCESS_AMNT = "TPAM";
    public static final String KEY_A1_COMM_AMOUNT = "A1CAMT";
    public static final String KEY_A1_BAL = "A1BAL";
    public static final String KEY_FORMATED_TX_PROCESS_AMNT = "TPAMF";
    public static final String KEY_MSG = "MSG";    
    public static final String KEY_ACC_NICK = "ACNK";
    public static final String KEY_SUCCUSS_MSG = "SMSG";
    public static final String KEY_TICKER_STR = "TSTR";
    public static final String KEY_PGO_KEY = "PGPK";    
    public static final String KEY_TX_DETAlIL = "TXDT";
    public static final String KEY_IS_DEFAULT = "ISDF";
    public static final String KEY_IS_PIN_CHNG_REQ = "IPCR";
    public static final String KEY_IS_MPIN_CHNG_REQ = "IMPCR";
    public static final String KEY_IS_SET_MPIN_LATER = "IS_SET_MPIN_LATER";
    public static final String KEY_IS_PASSWORD_CHNG_REQ = "IPSCR";
    public static final String KEY_STMT = "STMT";
    public static final String KEY_IS_USSD = "ISUSSD";
    
    public static final String KEY_ENCRYPTED_PIN = "EPIN";
    public static final String KEY_RESPONSE = "RESP";
    public static final String KEY_AGENT_ACC_INFO_REQ = "KEY_AGENT_ACC_INFO_REQ";
    
    
    
    public static final String KEY_COMM_AMOUNT = "CAMT";
    public static final String KEY_FORMATED_COMM_AMOUNT = "CAMTF";
    
    public static final String KEY_BILL_AMOUNT = "BAMT";
    public static final String KEY_FORMATED_BILL_AMOUNT = "BAMTF";
    
    public static final String KEY_RECEPIENT_NAME = "RPNAME";

    public static final String KEY_CURR_COMMAND = "CMD";
    
    public static final String CW_BILL_PAYMENT_TRANS = "65";
    
    //=================My Commission Constants===============================
    public static final String KEY_COMM_START_DATE = "DTODAY";
    public static final String KEY_COMM_END_DATE = "DYESTERDAY";
    //=================End My Commission Constants===========================
    
    
    
    
    //**************************HRA Constants**************************
    public static final String KEY_TRANS_PURPOSE = "TRX_PUR";
    public static final String KEY_OCCUPATION = "OCCUPATION";
    public static final String KEY_NEXT_OF_KIN_MOB="NOKMOB";
    public static final String CUSTOMER_REMITTENCE_KEY = "customerRemittenceKey";
    //*****************************************************************
    
    // A/C To Cash Constants
    public static final String KEY_WALKIN_RECEIVER_CNIC = "WALKIN_REC_CNIC";
    public static final String KEY_WALKIN_RECEIVER_MSISDN = "WALKIN_REC_MSISDN";
    public static final String KEY_SENDER_MOBILE = "SENDER_MOBILE";
    public static final String WALK_IN_CUSTOMER_CNIC_SUFFIX = "-W"; 
    public static final String WALK_IN_LIMIT_APPLIED = "WALK_IN_LIMIT_APPLIED"; 
    public static final String KEY_TX_ID = "TRXID";
    public static final String KEY_TRANSACTION_ID = "TXID";

    public static final String KEY_PAYMENT_TYPE = "PMTTYPE";
    public static final String KEY_BULK_DISBURSMENTS_ID = "BULK_DISBURSMENTS_ID";

    //cash to cash
    public static final String KEY_WALKIN_SENDER_CNIC = "WALKIN_SENDER_CNIC";
    public static final String KEY_WALKIN_SENDER_MSISDN = "WALKIN_SENDER_MSISDN";
    public static final String KEY_DED_AMT = "DED_AMT"; // COMMISSION
    
    //cash withdrawal
    public static final String KEY_MSISDN = "MSISDN";
    public static final String KEY_TX_DATE = "TX_DATE";
    public static final String KEY_TX_TIME = "TX_TIME";
    public static final String KEY_PIN_RETRY_COUNT = "PIN_RETRY_COUNT";
    public static final String KEY_AW_DTID = "AW_DTID";
    
//Adamjee Constants
    
    public static final String KEY_NAME = "NAME";
    public static final String KEY_NOMINEE_NAME = "NOMNAME";
    public static final String KEY_PHONE_NO = "PHONE";
    public static final String KEY_RELATION_ID = "RELID";
    
    
//Airblue Field Constants
    
    public static final String KEY_PNR = "PNR";
    public static final String KEY_PESSENGER_NAME = "PNAME";
    public static final String KEY_FLIGHT_NO_ONE = "FONE";
    public static final String KEY_FLIGHT_NO_TWO = "FTWO";
    public static final String KEY_DEPARTURE_CITY = "DCITY"; 
    public static final String KEY_ARRIVAL_CITY = "ACITY";
    public static final String KEY_DEPARTURE_TIME = "DTIME";
    public static final String KEY_ARRIVAL_TIME = "ATIME";
    public static final String KEY_FORMATTED_DEPARTURE_TIME = "DTIMEF";
    public static final String KEY_FORMATTED_ARRIVAL_TIME = "ATIMEF";
    public static final String KEY_STATUS = "STATUS";
    public static final String KEY_AMOUNT = "AMT";
    public static final String KEY_FORMATTED_AMOUNT = "AMTF";
    public static final String KEY_TRAVEL_DATE = "TDATE";
    public static final String KEY_FORMATTED_TRAVEL_DATE = "TDATEF";
    public static final String KEY_RETURN_DEPARTURE_CITY = "RDCITY"; 
    public static final String KEY_RETURN_ARRIVAL_CITY = "RACITY";
    public static final String KEY_RETURN_DEPARTURE_TIME = "RDTIME";
    public static final String KEY_RETURN_ARRIVAL_TIME = "RATIME";
    public static final String KEY_RETURN_TRAVEL_DATE = "RTDATE";
    public static final String KEY_FORMATTED_RETURN_DEPARTURE_TIME = "RDTIMEF";
    public static final String KEY_FORMATTED_RETURN_ARRIVAL_TIME = "RATIMEF";
    public static final String KEY_FORMATTED_RETURN_TRAVEL_DATE = "RTDATEF";

    public static final String KEY_BAFL_WALLET = "IS_BAFL_WALLET_EXISTS";
    public static final String KEY_BAFL_WALLET_ACCOUNT_ID = "BAFL_WALLET_ACCOUNT_ID";
    public static final String KEY_BAFL_WALLET_BALANCE = "BAFL_WALLET_BALANCE";
    public static final String KEY_MOBILE_IMEI_NUMBER = "IMEI_NUMBER";
    public static final String KEY_BAFL_WALLET_TRANSFER = "IS_WALLET_TRANSFER";
    
//Field Length Constant
    public static final int KEY_MOBILE_LENGTH= 11;
    public static final int KEY_MFS_USER_ID_LENGTH= 8;
    public static final int ALLPAY_USERID_LENGTH= 7;
    public static final int KEY_MFS_PIN_LENGTH = 4;
    public static final int KEY_VERIFLY_PIN_LENGTH= 4;
    public static final int KEY_FAVORITE_NUMBER_LENGTH= 50;
    public static final int KEY_FAVORITE_NAME_LENGTH= 50;
    public static final int KEY_FAVORITE_NUMBER_TYPE_LENGTH= 50;
    public static final int KEY_MFS_PASSWORD_LENGTH_MIN = 8;
    public static final int KEY_MFS_PASSWORD_LENGTH_MAX = 50;
    
//SMS Command
    public static final String KEY_ACTION_LOG_HANDLER= "ALH";
    public static final String KEY_SMS_MESSAGE= "SMSMSG";
    public static final String KENNEL_DEVICE_TYPE = "4";
    public static final String KENNEL_DEFAULT_FIRST_NAME = "user";
    public static final String KENNEL_DEFAULT_LAST_NAME = "name";
    public static final String KENNEL_DEFAULT_ADDRESS = "address";
    public static final String KENNEL_VO = "KennelVO";
    public static final Long KENNEL_POSTPAID_VALUE = 1L;
    public static final Long KENNEL_PREPAID_VALUE = 2L;
    public static final String KENNEL_POSTPAID = "POSTPAID";
    public static final String KENNEL_PREPAID = "PREPAID";
    public static final String KEY_SMS_MESSAGES = "sms_msgs";

    public static final String KEY_NOVA_ALERT_SMS_MESSAGES = "nova_alert";
    
//CustomerAccountRelationshipInquiry Command
        
    public static final String KEY_ACCOUNT_TYPE= "ATYPE";
    public static final String KEY_ACCOUNT_CURRENCY= "ACURR";
    public static final String KEY_ACCOUNT_STATUS = "ASTAT";
    public static final String KEY_ACCOUNT_NUMBER = "ACCNO";
    public static final String KEY_ACCOUNT_NUMBER_BB = "BBACCNO";
    public static final String KEY_ACCOUNT_NUMBER_AGNETMATE = "COREACID";
    public static final String KEY_ACCOUNT_TITLE_AGNETMATE = "COREACTL";
    public static final String KEY_ACCOUNT_TITLE_SENDER = "SENDERACTTITLE";
    public static final String KEY_ACCOUNT_NUMBER_BB_AGNETMATE = "BBACID";

    public static final String KEY_BANK_ID = "BAID";
    
    public static final String DEFAULT_STARTING_TRANS_NO = "0"; 
    public static final String DEFAULT_ENDING_TRANS_NO = "15";
    
    public static final String KEY_DEL_FAV_NUMBER = "delFavoriteNumId";

    //Remaining Limits
    public static final String KEY_DAILY_DEBIT_LIMIT = "DDEBIT";
    public static final String KEY_DAILY_CREDIT_LIMIT = "DCREDIT";
    public static final String KEY_MONTHLY_DEBIT_LIMIT = "MDEBIT";
    public static final String KEY_MONTHLY_CREDIT_LIMIT = "MCREDIT";
    public static final String KEY_YEARLY_DEBIT_LIMIT = "YDEBIT";
    public static final String KEY_YEARLY_CREDIT_LIMIT = "YCREDIT";
    public static final String KEY_WITHDRAWAL_AMOUNT = "AMOUNT";
    public static final String KEY_AGENT_ID = "AGENT_ID";
    public static final String KEY_BISP_FUND_TRANSFER = "IS_FUND_TRANSFER";

    //Demographis
    public static final String KEY_UDID = "UDID";
    public static final String KEY_OS = "OS";
    public static final String KEY_OS_VERSION = "OSVERSION";
    public static final String KEY_MODEL = "MODEL";
    public static final String KEY_VENDOR = "VENDOR";
    public static final String KEY_NETWORK= "NETWORK";
    public static final String KEY_DEVICE_CLOUD_ID = "DCID";
    
    public static final String KEY_ACTION = "ACTION";
    public static final Long KEY_OT_PIN_RETRY_LIMIT = 3L;


    public static final String KEY_FAQ_VERSION_NO = "FVNO";
	
	
	
//Default Catalog Version
    
    public static final String KEY_DEFAULT_CATALOG_VERSION_NAME = "ALL"; 
    public static final String KEY_DEFAULT_ALLPAY_CATALOG_VERSION_NAME = "ALLPAY";
 
    
    //Agent Locator
    public static final String KEY_RADIUS = "RADIUS";
    public static final String KEY_LOCATION_TYPE = "TYPE";
    public static final String KEY_LATITUDE = "LATITUDE";
    public static final String KEY_LONGITUDE = "LONGITUDE";
    public static final String KEY_TOTAL_COUNT = "TOTAL_COUNT";
    public static final String KEY_PAGE_NO = "PAGE_NO";
    public static final String KEY_PAGE_SIZE = "PAGE_SIZE";
    public static final String KEY_MAC_ADDRESS = "MAC_ADDRESS";
    public static final String KEY_IP_ADDRESS = "IP_ADDRESS";
    public static final String KEY_MACHINE_NAME = "MACHINE_NAME";

    /**
     * Response xml string to be sent to the client 
     */
    public static final String KEY_RESP_XML_MSG = "MSG";
    
    /**
     * The client request will be sent as a post parameter with this name 
     */
    public static final String REQ_PARAM_MSG = "message";

    public static final String KEY_REC_MSISDN = "REC_MSISDN";

    // Excise and Taxation Constants

    public static final String KEY_VEHICLE_REG_NO  = "VEH_REG_NO";
    public static final String KEY_VEHICLE_CHASSIS_NO = "VEH_CHASSIS_NO";
    public static final String KEY_VEHICLE_ASSESMENT_NO =  "VEH_ASS_NO";
    public static final String KEY_VEH_REG_DATE = "VEH_REG_DATE";
    public static final String KEY_MAKER_MAKE = "MAKER_MAKE";
    public static final String KEY_VEHICLE_CATEGORY = "VEH_CAT";
    public static final String KEY_VEHICLE_BODY_TYPE = "VEH_BODY_TYPE";
    public static final String KEY_VEHICLE_ENG_CAPACITY = "VEH_ENG_CAPACITY";
    public static final String KEY_VEHICLE_SEATS  = "VEH_SEATS";
    public static final String KEY_VEHICLE_CYLINDERS = "VEH_CYLINDERS";
    public static final String KEY_VEHICLE_OWNER_NAME = "VEH_OWNER_NAME";
    public static final String KEY_OWNER_CNIC = "VEH_OWNER_CNIC";
    public static final String KEY_FILEER_STATUS =  "VEH_FILEER_STATUS";
    public static final String KEY_TAX_PAID_FROM  = "TAX_PAID_FROM";
    public static final String KEY_TAX_PAID_UPTO = "TAX_PAID_UPTO";
    public static final String KEY_VEHICLE_TAX_PAID_LIFETIME = "VEH_TAX_PAID_LIFETIME";
    public static final String KEY_VEHICLE_STATUS = "VEH_STATUS";
    public static final String KEY_FITNESS_DATE = "VEH_FITNESS_DATE";
    public static final String KEY_TAX_ID = "VEH_TAX_ID";
    public static final String KEY_TAX_NAME = "VEH_TAX_NAME";
    public static final String KEY_ACCOUNT_HEAD = "VEH_ACCOUNT_HEAD";
    public static final String KEY_TAX_AMOUNT = "VEH_TAX_AMOUNT";
    public static final String KEY_ASSESMENT_DATE = "VEH_ASSESMENT_DATE";
    public static final String KEY_TRANS_PURPOSE_CODE = "TRANS_PURPOSE_CODE";

    public static final String KEY_THIRD_PARTY_CUST_SEGMENT_CODE = "THIRD_PARTY_CUST_SEGMENT_CODE";
    public static final String KEY_THIRD_PARTY_RRN = "THIRD_PARTY_RRN";

    public static final String KEY_TO_BANK_IMD = "TO_BANK_IMD";
    public static final String KEY_CC_TO_BANK_IMD = "BAIMD";
    public static final String KEY_CC_FROM_BANK_IMD = "FROM_BANK_ID";
    public static final String KEY_BENE_BANK_NAME = "BENE_BANK_NAME";
    public static final String KEY_BENE_BRANCH_NAME = "BENE_BRANCH_NAME";
    public static final String KEY_BENE_IBAN = "BENE_IBAN";
    public static final String KEY_BENE_TRX_TYPE = "CR_DR";

    //added by ahsan for bookme


    //========================================================
    //	Commands constants
    //========================================================

    /**
     * Represents a Change Pin Request Command
     */
    public static final String CMD_GNI_CHG_PIN = "1";

    /**
     * Check Application Version Command.
     * Note: This is not in use currently
     */
    public static final String CMD_CHK_APP_VER = "2";

    /**
     * Represents a Discrete Product Transaction Command
     */
    //public static final String CMD_TRANS_FIXED_PROD = "4";
    
    /**
     * Represents a Get Banck Account info Command
     */
    public static final String CMD_GT_ACC_INFO = "3";
    public static final String CMD_ALLPAY_BILL_INFO= "36";
    public static final String CMD_ALLPAY_BILL_PAYMENT= "37";
    public static final String CMD_ALLPAY_SALES_SUMMARY = "35";
    
    public static final String CMD_CHECK_ALLPAY_BALANCE = "38";
    
    /*added by mudassir for cashDepositCommand flow separation from AllPayBillPaymentCommand*/
    public static final String CMD_ACCOUNT_TO_CASH = "68";
    public static final String CMD_ACCOUNT_TO_CASH_INFO = "69";
    public static final String CMD_CASH_TO_CASH_INFO = "70";
    public static final String CMD_ACCOUNT_TO_CASH_LEG_2 = "71";
    public static final String CMD_CASH_TO_CASH_LEG2 = "72";
    //*****************************************************
    public static final String CMD_VERIFY_PIN = "74";
    //*****************************************************
    public static final String CMD_BULK_PAYMENT_LEG2 = "87";

    public static final String CMD_SAVE_TILL_BALANCE = "76";
    public static final String CASH_DEPOSIT_INFO_COMMAND = "78";
    public static final String CMD_MY_COMMISSION = "79";
    public static final String CMD_CUSTOMER_RET_PAYMENT_INFO = "81";
    public static final String CMD_CUSTOMER_RET_PAYMENT = "82";
    public static final String CMD_AGENT_DONATION_PAYMENT = "83";
    public static final String CMD_BULK_PAYMENT = "86";
    public static final String CMD_CASH_DEPOSIT = "67";
    public static final String CMD_AGENT_CASH_DEPOSIT = "245";
    public static final String CMD_CASH_OUT_INFO = "115";
    public static final String CMD_CASH_OUT = "116";
    public static final String CMD_ACCOUNT_TO_ACCOUNT = "101";
    public static final String CMD_ACCOUNT_TO_ACCOUNT_INFO = "102";
    public static final String CMD_DEBIT_CARD_ISSUANCE_INFO = "231";
    public static final String CMD_DEBIT_CARD_ISSUANCE = "232";
    public static final String CMD_CHALLAN_PAYMENT_INFO = "210";
    public static final String CMD_CHALLAN_PAYMENT= "211";
    public static final String CMD_HRA_REGISTRATION_INQUIRY= "224";
    public static final String CMD_HRA_TO_WALLET_INQUIRY= "238";
    public static final String CMD_HRA_TO_WALLET= "240";
    public static final String CMD_DEBIT_INQUIRY_API= "289";
    public static final String CMD_DEBIT_PAYMENT_API= "290";
    public static final String CMD_CREDIT_INQUIRY_API= "291";
    public static final String CMD_CREDIT_PAYMENT_API= "292";
    public static final String CMD_FETCH_CARD_TYPE= "293";
    public static final String CMD_CORE_TO_WALLET_ADVICE= "297";

    /**
     * Represents a Command
     */
    public static final String CMD_MINISTATEMENT = "30";
    
    /**
     * Represents a Command
     */
    public static final String CMD_MINISTATEMENT_AGENT = "66";

    /**
     * Represents a Generate Bank PIN Command
     */
    public static final String CMD_GENERATE_BANK_PIN = "32";
    
    
    /**
     * Represents a Update Catalog Command
     */
    public static final String CMD_UPD_CAT = "4";

    /**
     * Represents a Verifly Pin Change Command
     */
    public static final String CMD_VF_PIN_CHG = "5";

    /**
     * Represents a Verifly Pin Change Command
     */
    public static final String CMD_MIGRATED_PIN_CHG = "233";

    /**
     * Represents a Check Account Balance Command
     */
    public static final String CMD_CHK_ACC_BAL = "6";

    /**
     * Represents a Check Account Balance Command
     */
    public static final String CMD_GT_TK_STR = "7";
    
    /**
     * Represents a Get favorite numbers Command
     */
    public static final String CMD_GT_FAV_NO = "8";

    /**
     * Represents a Set favorite numbers Command
     */
    public static final String CMD_ST_FAV_NO = "9";

    /**
     * Represents a Set default account Command
     */
    public static final String CMD_DEF_ACC= "10";

    /**
     * Represents a Login Command
     */
    public static final String CMD_MFS_LOGIN = "11";
    
    /**
     * Represents a Login Command
     */
    public static final String CMD_LOGIN = "25";
    
    /**
     * Represents a Fecth Transactrons Command
     */
    public static final String CMD_GT_TRANS = "12";

    
    /**
     * Represents a search Transactron Command
     */
    public static final String CMD_SRCH_TRANS = "13";

    /**
     * Represents a Check Balance Command
     */
    public static final String CMD_CHK_BAL = "14";
    
    /**
     * Represents a Customer discrete product sale Command
     */
    public static final String CMD_CDPS_TRANS = "15";
    
    /**
     * Represents a Customer variable product sale Command
     */
    public static final String CMD_CVPS_TRANS = "16";

    /**
     * Represents a retailer to customer discrete product sale Command
     */
    public static final String CMD_R2CDPS_TRANS = "17";

    
    /**
     * Represents a retailer to customer variable product sale Command
     */
    public static final String CMD_R2CVPS_TRANS = "18";

    /**
     * Represents a bill payment Command
     */
    public static final String CMD_BILL_PAY_TRANS = "19";
    
    /**
     * Represents a distributor to distributor transaction Command
     */
    public static final String CMD_D2D_TRANS = "20";

    /**
     * Represents a retailer to retailer transaction Command
     */
    public static final String CMD_R2R_TRANS = "21";

    /**
     * Represents a distributor to retailer transaction Command
     */
    public static final String CMD_D2R_TRANS = "22";
    
    /**
     * Represents a BILL INFORMATION  Command
     */
    public static final String CMD_GET_BILL_INFO = "23";
    public static final String CMD_TOPUP_TX_INFO = "62";
    
    public static final String AGENT_TO_AGENT_INFO = "77";
    
    public static final String RSO_AGENT_TO_AGENT_INFO = "84";
    public static final String RSO_AGENT_TO_AGENT = "85";
    
    /**
     * Represents a Generate Encyted Pin Command
     */
    public static final String CMD_GEN_ENCRYT_PIN = "24";
    
    /**
     * Represents a SMS Login Creation Command
     */
    public static final String CMD_SMS = "26";
    
    /**
     * Represents a SMS Deactivate Login Command
     */
    public static final String CMD_SMS_DEACTIVATE_MFS_ACC = "27";
    
    /**
     * Represents a SMS Activate Login Command
     */
    public static final String CMD_SMS_ACTIVATE_MFS_ACC = "28";
    
    
    /**
     * Represents a CUSTOMER_ACCOUNT_RELATIONSHIP_INQUIRY Command
     */
    public static final String CMD_CUSTOMER_ACCOUNT_RELATIONSHIP_INQUIRY = "29";
    
    
    /**
     * Represents a Credit Card Payment Command
     */
    public static final String CMD_CREDIT_CARD_PAYMENT = "31";
    
    
    /**
     * Represents a ALLPAY Login Command
     */
    public static final String CMD_ALL_PAY_LOGIN = "33";
    
    /**
     * Represents a Get Latest catalog Command
     */
    public static final String CMD_GET_LATEST_CATALOG = "39";
    
    /**
     * Represents a Get Accounts by Bank Command
     */
    public static final String CMD_GET_ACCOUNTS_BY_BANK = "40";
    
    
    /**
     * Represents a Edit favorite number  Command
     */
    public static final String CMD_EDIT_FAV_NUM = "41";
    
    /**
     * Represents a Delete favorite number  Command
     */
    public static final String CMD_DELETE_FAV_NUM = "43";
    
    
    /**
     * Represents a VERIFLY_PIN_CHANGE Command
     */
    public static final String CMD_VERIFLY_PIN_CHANGE = "5";
    public static final String CMD_LOGIN_PIN_CHANGE = "1";

    public static final String CMD_ADD_FAV_NOS = "42";
    
    /**
     * Represents a distributor to retailer transaction Command
     */
    public static final String CMD_GET_SUPPLIER_INFO = "23";
    
    
    /**
     * ALLPAY Constants
     */
    
    
    public static final String KEY_ALLPAY_ID = "APID";
    
    
    //=============================================== MINI ============================================    
    public static final String CMD_MINI_CMD_MNG = "44";
    public static final String CMD_MINI_LOGIN = "45";  
    public static final String CMD_MINI_PAY_SERVICE = "53";
    public static final String CMD_MINI_CHECK_BALANCE = "56";
    public static final String CMD_MINI_TOPUP = "57";
    public static final String CMD_MINI_P2P = "58";
    public static final String CMD_MINI_CI = "60";

    
    public static final String CMD_MINI_CASHOUT = "59";
    
    public static final String CMD_MINI_RETAIL_PAYMENT = "61";
    public static final String CMD_BULK_BILL_PAYMENT = "95"; 
    
    public static final String KEY_PARAM = "Param";
    public static final String KEY_DO_PAY_BILL = "doPayBill";
    public static final String KEY_MINI_TX_MODEL = "miniTransactionModel";
    public static final String PARAM_COUNTER_FIRST_VAL = "1";
    public static final String KEY_PARAM_HASHMAP = "PRHM";
    public static final String KEY_PARAM_SMS_TEXT = "PRSMSTXT";
    public static final String KEY_PARAM_PIN = "PRPIN";
    
    
    public static final String DEFAULT_PAY_ACC_SYMBOL = "DP";
    public static final String DEFAULT_REC_ACC_SYMBOL = "DR";
    
    //===============================================  ============================================

    public static final String KEY_FINANCIAL_INSTITUTION = "financialInstitution";
    public static final String KEY_COMMISSION_OPTION = "commissionOption";
    
    public static final String KEY_TX_DTL_MASTER_MODEL = "TXDTLMM";

    public static final String CMD_TRANSFER_IN_INFO = "103"; 
    public static final String CMD_TRANSFER_IN = "104"; 
    
    public static final String CMD_TRANSFER_OUT_INFO = "105"; 
    public static final String CMD_TRANSFER_OUT = "106"; 
    
    public static final String CMD_CREDIT_CARD_INFO = "107"; 

    public static final String CMD_AGENT_CREDIT_CARD_INFO = "108"; 
    public static final String CMD_AGENT_CREDIT_CARD_PAYMENT = "109"; 

    public static final String CMD_CORE_BANK_MINI_STATEMENT = "110"; 
    
    //JS Constants
    public static final String KEY_RECIPIENT_AGENT_MOBILE = "RAMOB";
    public static final String KEY_RECIPIENT_WALKIN_MOBILE = "RWMOB";
    public static final String KEY_RECIPIENT_AGENT_CNIC = "RACNIC";
    public static final String KEY_RECIPIENT_AGENT_NAME = "RANAME";
    public static final String KEY_AGENT_MOBILE = "AMOB";
    public static final String KEY_CUSTOMER_MOBILE = "CMOB";
    public static final String KEY_RECEIVING_CUSTOMER_MOBILE = "RCMOB";
    public static final String KEY_WALKIN_SENDER_MOBILE = "SWMOB";
    public static final String KEY_S_W_CNIC = "SWCNIC";
    public static final String KEY_R_W_CNIC = "RWCNIC";
    public static final String KEY_CORE_ACC_NO = "COREACID";
    public static final String KEY_CORE_ACC_TITLE = "COREACTITLE";
    public static final String KEY_IS_IVR_RESPONSE = "IVRRESPONSE";
    public static final String KEY_ACC_TITLE = "ACTITLE";
    public static final String KEY_CUSTOMER_NAME = "CNAME";

    public static final String KEY_ID = "ID";
    public static final String KEY_DATE = "DATE";
    public static final String KEY_DATEF = "DATEF";
    public static final String KEY_TIMEF = "TIMEF";
    public static final String KEY_PRODUCT = "PROD";
    public static final String KEY_PRODUCT_NAME = "PNAME";
    public static final String KEY_PROFIT_KEY = "PKEY";
    public static final String KEY_RESERVED_4 = "RESERVED4";
    public static final String KEY_RESERVED_3 = "RESERVED3";
    public static final String KEY_RESERVED_5 = "RESERVED5";
    public static final String KEY_RESERVED_1 = "RESERVED1";
    public static final String KEY_RESERVED_2 = "RESERVED2";
    public static final String KEY_RESERVED_6 = "RESERVED6";
    public static final String KEY_RESERVED_7 = "RESERVED7";
    public static final String KEY_RESERVED_8 = "RESERVED8";
    public static final String KEY_RESERVED_9 = "RESERVED9";
    public static final String KEY_RESERVED_10 = "RESERVED10";

    public static final String KEY_EXPIRY = "EXP";
    public static final String KEY_CURR_COMMAND_ID = "CMDID";
    public static final String CMD_BB_TO_CORE_AC_INFO = "111";
    public static final String CMD_BB_TO_CORE_AC = "112";
    public static final String CMD_CNIC_TO_CORE_AC = "114";

    public static final String CMD_CNIC_TO_BB_ACC_PAYMENT = "117"; 
    public static final String CMD_AGENT_RETAIL_PAYMENT_INFO = "118"; 
    public static final String CMD_AGENT_RETAIL_PAYMENT = "119";
    public static final String CMD_OPEN_CUSTOMER_L0_ACCOUNT_INQUIRY = "120";
    public static final String CMD_OPEN_CUSTOMER_L0_ACCOUNT = "121";
    public static final String CMD_INITIAL_DEPOSIT = "124";
    public static final String CMD_FONEPAY_INQUIRY = "183";
    public static final String CMD_FONEPAY_PAYMENT = "184";
    public static final String CMD_FONEPAY_SETTLEMENT = "206";
    public static final String CMD_CUSTOMER_BILL_PAYMENTS_INQUIRY = "204";
    public static final String CMD_CUSTOMER_BILL_PAYMENTS = "205";

    public static final String CMD_OPEN_HRA_ACCOUNT_INQUIRY = "224";
    public static final String CMD_OPEN_HRA_ACCOUNT_PAYMENT = "226";

    //Agent IBFT
    public static final String WALLET_TO_CORE_INQUIRY = "256";
    public static final String WALLET_TO_CORE_PAYMENT = "257";

    //JS Bank
	public static final String CMD_CHANGE_PIN_IVR = "123";

    public static final String CMD_AGNETMATE_AGENT_MOBILE_NUMBER = "AMOB"; 
    public static final String CMD_AGNETMATE_CUSTOMER_MOBILE_NUMBER = "CMOB"; 
    public static final String CMD_AGNETMATE_CONSUMER_NUMBER = "CONSUMER"; 
    public static final String CMD_AGNETMATE_DUEDATER = "DUEDATE";  
    public static final String CMD_AGNETMATE_DUEDATEF = "DUEDATEF";
    
    public static final String KEY_CUSTOMER_MODEL = "customerModel";
    public static final String KEY_CUSTOMER_PICTURES_COLLECTION = "customerPictures";
    public static final String KEY_APP_USER_MODEL = "appUserModel";
    public static final String KEY_REGISTRATION_STATE_ID = "registrationId";
	public static final String KEY_USER_DEVICE_ACCOUNT_MODEL = "userDeviceAccountModel";
	public static final String KEY_SMART_MONEY_ACCOUNT_MODEL = "smartMoneyAccountModel";
	public static final String KEY_ONLINE_ACCOUNT_MODEL = "ola";
	public static final String KEY_ACCOUNT_INFO_MODEL = "accountInfoModel";
	public static final String KEY_CUST_ERROR_PREFIX = "CUST_ERROR_PREFIX";
	public static final String KEY_HANDLER_MODEL = "handlerModel";
	public static final String KEY_RETAILER_CONTACT_MODEL = "retailerContactModel";
    public static final String KEY_VARISYS_DATA_MODEL = "verisysDataModel";
    public static final String KEY_RETAILER_CONTACT_VO = "retailerContactVO";
	
    public static final String KEY_IVR_RESPONSE = "IVRRESPONSE";
    public static final String KEY_ACCOUNT_NUMBER_SENDER = "ACCNO1";
    public static final String KEY_ACCOUNT_NUMBER_RECIPIENT = "ACCNO2";

    public static final String KEY_IS_CONCURRENT_TRANSACTION = "ICT";

    // IBFT related constants
    public static final String KEY_RRN = "RRN";
    public static final String KEY_STAN = "STAN";
    public static final String KEY_PRODUCT_ID = "productId";

    public static final String TITLE_FETCH_COMMAND = "126";    
    public static final String CREDIT_ADVICE_COMMAND = "127";
    
    
    public static final String CMD_BLB_TO_BLB_INFO = "130";
    public static final String CMD_BLB_TO_CNIC_INFO = "131";
    public static final String CMD_CNIC_TO_BLB_INFO = "132";
    public static final String CMD_CNIC_TO_CNIC_INFO = "133";
    
    public static final String CREATE_PIN_IVR = "125";
    public static final String REGENERATE_PIN_IVR = "126";
    
    public static final String CMD_CUSTOMER_CREATE_PIN = "134";
    public static final String CMD_AGENT_CREATE_PIN = "135";
    
    public static final String CMD_SENDER_REEDEM_INFO = "136";
    public static final String CMD_SENDER_REEDEM = "137";
    
    public static final String RECEIVE_CASH_COMMAND_COMMAND = "143";
    
    public static final String TELLER_CASH_IN_INFO_COMMAND = "145";    
    public static final String TELLER_CASH_IN_COMMAND = "147";  
    public static final String TELLER_CASH_OUT_COMMAND = "149";
    public static final String CMD_HANDLER_CREATE_PIN = "151";
    
    public static final String CMD_DEVICE_VERIFICATION = "178";
    public static final String CMD_BIOMETRIC_VERIFICATION = "180";
    public static final String CMD_CUSTOMER_NADRA_VERIFICATION = "181";
    public static final String CMD_OTP_VERIFICATION = "182";

    public static final String CMD_CUSTOMER_CASH_WITHDRAWAL_REQUEST = "195";
    public static final String CMD_CUSTOMER_CASH_WITHDRAWAL_LEG2_INFO = "196";
    public static final String CMD_CUSTOMER_CASH_WITHDRAWAL_LEG2 = "197";
    public static final String CMD_WEB_SERVICE_INFO_COMMAND = "214";
    public static final String CMD_WEB_SERVICE_CASH_IN_COMMAND = "215";
    public static final String CMD_PIN_CHANGE_INFO = "217";
    public static final String CMD_PIN_CHANGE = "218";

    public static final String CMD_COLLECTION_PAYMENT_INFO_COMMAND = "212";
    public static final String CMD_COLLECTION_PAYMENT_COMMAND = "213";

    public static final String CMD_CUSTOMER_COLLECTION_PAYMENT_INFO = "210";
    public static final String CMD_CUSTOMER_COLLECTION_PAYMENT_COMMAND = "211";
    public static final String CMD_CORE_FT = "175";

    public static final String IS_REG = "ISREG";
    
    public static final String KEY_BILL_INFO = "BILL_INFO";
    
    //BVS related constants
    public static final String KEY_NADRA_SESSION_ID = "NADRA_SESSION_ID";
    public static final String KEY_THIRD_PARTY_TRANSACTION_ID = "THIRD_PARTY_TRANSACTION_ID";
    public static final String KEY_SESSION_ID = "THIRD_PARTY_SESSION_ID";
    public static final String KEY_IS_BVS_REQUIRED = "IS_BVS_REQ";
    public static final String KEY_IS_OTP_REQUIRED = "IS_OTP_REQ";
    public static final String KEY_OTP = "OTP";
    public static final String KEY_IMPRESSION_IMAGE_NAME = "IMPRESSION";
    public static final String KEY_FINGER_INDEX = "FINGER_INDEX";
    public static final String KEY_LAST_FINGER_INDEX = "LAST_FINGER_INDEX";
    public static final String KEY_BVS_CNIC = "BVS_CNIC";
    public static final String KEY_BVS_MSISDN = "BVS_MSISDN";
    public static final String KEY_INITIAL_DEPOSIT_REQUIRED = "IDR";
    public static final String KEY_DOB      = "DOB";
    public static final String KEY_RETAKE_IMAGES  = "RTIMAGES";
    public static final String KEY_MOBILE_UPDATE_ALLOWED  = "MUAOR";
    public static final String KEY_APPROVE_ACCOUNT_ON_UPDATE  = "ACOU";
    public static final String KEY_THUMB_IMPRESSION = "THUMB_IMPRESSION";
    public static final String KEY_SENDER_ACCOUNT_TITLE = "SENACCTITLE";
    public static final String KEY_SERVICE_ID = "SERVICE_ID";

    public static final String KEY_FINGER_TEMPLATE = "FINGER_TEMPLATE";
    public static final String KEY_TEMPLATE_TYPE = "TEMPLATE_TYPE";
    public static final String KEY_AREA_NAME = "AREA_NAME";
    public static final String KEY_REMITTANCE_TYPE = "REMITTANCE_TYPE";

    public static final String IS_SENDER_BVS_REQUIRED = "IS_SENDER_BVS_REQUIRED";
    public static final String IS_RECEIVER_BVS_REQUIRED = "IS_RECEIVER_BVS_REQUIRED";
    public static final String KEY_BVS_FAIL = "BVS_FAIL";
    public static final String KEY_BIRTH_PLACE = "BIRTH_PLACE";
    public static final String KEY_MOTHER_MAIDEN = "MOTHER_MAIDEN";
    public static final String KEY_PRESENT_ADDR = "PRESENT_ADDR";
    public static final String KEY_PERMANENT_ADDR = "PERMANENT_ADDR";
    public static final String KEY_CNIC_STATUS = "CNIC_STATUS";

    public static final String KEY_ADTYPE = "ADTYPE";
    public static final String KEY_VIDEO_LINKS = "VIDEOLINK";

    public static final String KEY_ISWEBSERVICE = "ISWEBSERVICE";
    public static final String KEY_CHANNEL_ID = "CHANNELID";

    public static final String LOAD_SMA_MODEL="loadSmartMoneyAccountModel";
    public static final String VALIDATE_SMART_MONEY_ACCOUNT="validateSmartMoneyAccount";
    public static final String VALIDATE_RETAILER = "validateRetailer";
    public static final String VALIDATE_USER_DEVICE_ACCOUNT = "validateUserDeviceAccount";
    public static final String DEFAULT_LABEL = "Your";
    public static final String KEY_INCC = "INCC";
    public static final String KEY_INCC_EXCC = "INCC_EXCC";
    public static final String BANK_NAME_ATTRIBUTE = "name";

    //QR Payment related params
    public static final String KEY_MERCHANT_ID = "MRID";
    public static final String KEY_MERCHANT_NAME = "MNAME";


    public static final String KEY_TERMINAL_ID = "TERMINAL_ID";

    public static final String KEY_PAYMENT_MODE = "PAYMENT_MODE";
    public static final String KEY_QR_STRING = "QR_STRING";
    public static final String KEY_SENDER_CITY = "SENDER_CITY";
    public static final String KEY_RECEIVER_CITY = "RECEIVER_CITY";

    public static final String KEY_NADRA_MINUTIAE_COUNT = "MINUTIAE_COUNT";
    public static final String KEY_NADRA_NIFQ = "NADRA_NIFQ";
    public static final String KEY_ERROR_COUNTER = "ERROR_COUNTER";

    //Debit Card Constants
    public static final String KEY_DEBIT_CARD_MAILING_ADDRESS_MODEL = "DEBIT_CARD_MAILING_ADDRESS_MODEL";
    public static final String KEY_DEBIT_CARD_ISSUED = "DEBIT_CARD_MAILING_ADDRESS_MODEL";

    public static final String KEY_THIRD_PARTY_TRX_INTERNAL_ERROR = "IS_INTERNAL_ERROR";

    public static final String CMD_DEBIT_CARD_CW = "234";
    public static final String KEY_CMD_POS_REFUND = "236";
    public static final String KEY_CMD_ADVANCE_LOAN_PAYMENT = "298";

    public static final String KEY_BB_ACC_ID = "BBACID";
    public static final String KEY_ACCOUNT_TYPES = "ACCTYPE";
    public static final String ATTR_ACCOUNT_TYPE_CODE = "ACTYPECODE";
    public static final String KEY_SENDER_BANK_ID = "SENBAID";
    public static final String KEY_CARD_NO = "CARDNO";
    public static final String KEY_CARD_EXPIRY = "CARDEXPIRY";
    public static final String KEY_RECIEVER_ACCOUNT_CORE = "RECACCOUNTNO";
    public static final String KEY_RECIEVER_ACCOUNT_TITLE = "RECACCTITLE";
    public static final String KEY_RECIEVER_BANK_IMD = "RECBBIMD";
    public static final String KEY_BENEFICIARY_BANK_NAME = "BBNAME";
    public static final String KEY_BENE_NICK_NAME = "BENENICKNAME";

    public static final String KEY_VEHICLE_CHESIS_NO = "VEHICLE_CHESIS_NO";
    public static final String KEY_ASSESSMENT_NO = "ASSESSMENT_NO";
    public static final String KEY_ASSESSMENT_AMOUNT = "ASSESSMENT_AMOUNT";

    public static final String KEY_HRA_LINKED_REQUEST = "HRA_LINKED_REQUEST";

    public static final String ATTR_PARAM_PITB_APPROVED = "PITB_APPROVED";
    public static final String KEY_TEHSIL = "TEHSIL_ID";
    public static final String KEY_DISTRICT = "DISTRICT_ID";
    public static final String KEY_SENDER_IBAN = "SENDER_IBAN";
    public static final String KEY_SENDER_BANK_NAME = "SENDER_BANK_NAME";
    public static final String KEY_SENDER_BRANCH_NAME = "SENDER_BRANCH_NAME";

    // USSD COmmands

    public static final String KEY_CMD_WALLET2WALLETINFO = "198";
    public static final String KEY_CMD_WALLET2WALLETCMD = "199";
    public static final String KEY_CMD_WALLET2CNICINFO = "200";
    public static final String KEY_CMD_WALLET2CNICCMD = "201";

    public static final String KEY_CMD_BB2COREINFO = "202";
    public static final String KEY_CMD_BBTOCORECMD = "203";

    public static final String KEY_CMD_HRA_CASH_WITHDRAWAL = "227";
    public static final String KEY_CMD_ACC_LINK = "260";
    public static final String KEY_CMD_ACC_DELINK = "262";
    public static final String KEY_CMD_THIRD_PARTY_BALANCE_INQUIRY = "228";
    public static final String CMD_UPGRADE_L2_ACCOUNT = "304";

    public static final String CMD_UPGRADE_MERCHANT_ACCOUNT = "314";

    public static final String CMD_BLINK_BVS_VERIFICATION_INQUIRY = "307";
    public static final String CMD_FEE_PAYMENT_INQUIRY = "309";
    public static final String CMD_FEE_PAYMENT = "310";
    public static final String CMD_VC_TRANSFER = "311";

}