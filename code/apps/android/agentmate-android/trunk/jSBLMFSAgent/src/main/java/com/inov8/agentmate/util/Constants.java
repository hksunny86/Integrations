package com.inov8.agentmate.util;

import com.inov8.jsblmfs.BuildConstants;

import java.util.ArrayList;

public class Constants {

    public static final String PROTOCOL = BuildConstants.PROTOCOL;

    public static final String BASE_URL = BuildConstants.BASE_URL;

    public static final String APPURL = PROTOCOL + BASE_URL + "/allpay.me";

    public static final String ATTR_PERSON_DATA = "PERSON_DATA";

    public static final String APP_DOWNLOAD_URL = BuildConstants.APP_DOWNLOAD_URL;

    public static final String NADRA_VERIFICATION_URL = BuildConstants.NADRA_VERIFICATION_URL;

    public static final String NADRA_OTC_URL = BuildConstants.NADRA_OTC_URL;

    public static final String PAYSYS_VERIFY_URL = BuildConstants.PAYSYS_VERIFY_URL;

    public static final String PAYSYS_OTC_URL = BuildConstants.PAYSYS_OTC_URL;

    public static String SECRET_KEY = BuildConstants.SECRET_KEY;

    public static String SECURITY_KEY = BuildConstants.SECURITY_KEY;

    public static final String HANDSHAKE_KEY = BuildConstants.HANDSHAKE_KEY;

    public static final String APPLICATION_VERSION = "2.0.10.28";

    public static final int CONNECTION_TIMEOUT = 180; // seconds
    public static final int OTP_RESEND_TIMER = 60000; // miliseconds
    public static final String URL_TERMS_OF_USE = PROTOCOL + BASE_URL + "/terms-and-conditions.xhtml";
    public static final String URL_TERMS_OF_DEBIT_CARD_ISSUENCE = PROTOCOL + BASE_URL + "/debit-card-terms-and-conditions.jsp";
    public static final String URL_TERMS_OF_BISP = PROTOCOL + BASE_URL + "/bisptermsandconditions.jsp";

    public static final String SIGNATURE = BuildConstants.SIGNATURE;

    public static final String FINGER_TEMPLATE_TYPE_ISO = "ISO_19794_2";

    public static final String APPID = "1";
    public static final String DTID = "5";
    public static final String CURRENCY = " PKR";

    public static final String TRUE = "1";
    public static final String BANK_ACC_TRUE = "01";

    public static final String USER_TYPE = "3";
    public static int LOGIN_RESPONSE_TYPE = 0;
    public static int OTP_RESPONSE_TYPE = 2;
    public static int otpCount = 0;

    public static final int PASSWORD_LENGTH = 4;
    public static final int MAX_LENGTH_PIN = 4;
    public static final int MAX_LENGTH_THIRD_PARTY_OTP = 6;
    public static final int MAX_LENGTH_BOP_OTP = 4;
    public static final int MAX_LENGTH_OTP = 4;
    public static final int MAX_LENGTH_VERIFICATION_OTP = 5;
    public static final int MAX_LENGTH_TRX_CODE = 5;
    public static final int USERNAME_LENGTH = 7;
    public static final int MAX_AMOUNT_LENGTH = 7;
    public static final int MAX_LENGTH_MOBILE = 11;
    public static final int MAX_LENGTH_TRANSACTION_ID = 12;
    public static final int MAX_LENGTH_CNIC = 13;
    public static final int MAX_LENGTH_ACCOUNT_NO = 14;
    public static final int MAX_LENGTH_ACCOUNT_NO_IBFT = 16;
    public static final int MAX_LENGTH_CONSUMER_NUMBER = 50;
    public static final int MAX_LENGTH_CARD = 16;
    public static final long MAX_AMOUNT = 9999999;

    public static final String ACTION_VERIFY = "0";
    public static final String ACTION_RESEND = "1";
    public static final String ACTION_REGENERATE = "2";

    public static final String KEY_CMDRCVD = "cmdRcvd";
    public static final String KEY_CAT_VER = "CVNO";

    public static final String KEY_BANK_ACC_LIST = "BACL";
    public static final String KEY_LIST_USR_BANK = "bank";
    public static final String KEY_LIST_MSGS = "msgs";
    public static final String KEY_LIST_ERRORS = "errs";
    public static final String KEY_LIST_ALERT = "Alert Notification";
    public static final String WAIT = "Please Wait";
    public static final String PROCESSING = "Processing...";
    public static final String KEY_LIST_TRANS = "trans";
    public static final String KEY_LIST_CATALOG = "catalog";
    public static final String KEY_LIST_CITIES = "cities";
    public static final String KEY_LIST_PAYMENT_REASONS = "paymentreasons";
    public static final String KEY_LIST_MEMBERS_BANKS = "memberbanks";

    // Tags
    public static final String TAG_MSG = "msg";
    public static final String TAG_PARAMS = "params";
    public static final String TAG_PARAM = "param";
    public static final String TAG_CAT = "cat";
    public static final String TAG_SERV = "srv";
    public static final String TAG_BANKS = "banks";
    public static final String TAG_BANK = "bank";
    public static final String TAG_ACC = "acc";
    public static final String TAG_MESGS = "mesgs";
    public static final String TAG_MESG = "mesg";
    public static final String TAG_ERRORS = "errors";
    public static final String TAG_ERROR = "error";
    public static final String TAG_CITIES = "cities";
    public static final String TAG_CITY = "city";
    public static final String TAG_PAYMENT_REASONS = "paymentreasons";
    public static final String TAG_PAYMENT_REASON = "paymentreason";
    public static final String TAG_MEMBERS_BANKS = "memberbanks";
    public static final String TAG_SEGMENTS = "segments";
    public static final String TAG_SEGMENT = "segment";
    public static final String TAG_CARDTYPES = "cardtypes";
    public static final String TAG_CARDTYPE = "cardtype";
    public static final String TAG_TRANS = "trans";
    public static final String TAG_TRN = "trn";
    public static final String TAG_BANK_ACCS = "accs";
    public static final String TAG_BANK_ACC = "acc";
    public static final String TAG_Categories = "categories";
    public static final String TAG_Cagtegory = "category";
    public static final String TAG_BANK_Prods = "prds";
    public static final String TAG_BANK_prd = "prd";
    public static final String TAG_BANK_supp = "supp";

    // Attributes
    public static final String ATTR_MSG_ID = "id";
    public static final String ATTR_REQ_TIME = "reqTime";
    public static final String ATTR_SERV_ID = "id";
    public static final String ATTR_SERV_GF_ID = "gfid";
    public static final String ATTR_BANK_ID = "id";
    public static final String ATTR_BANK_NICK = "bname";
    public static final String ATTR_PARAM_NAME = "name";
    public static final String ATTR_PARAM_IMD = "IMD";
    public static final String ATTR_PARAM_CODE = "code";
    public static final String ATTR_PARAM_SEG_ID = "segmentId";
    public static final String ATTR_SERV_LABEL = "label";
    public static final String ATTR_SUPP_NAME = "supName";
    public static final String ATTR_CONSUMER_MIN_LENGTH = "consumerMinLength";
    public static final String ATTR_CONSUMER_MAX_LENGTH = "consumerMaxLength";
    public static final String ATTR_DTID = "DTID";
    public static final String ATTR_MOBILE_NETWORK = "MOBILE_NETWORK";

    public static final String ATTR_CAT_VER = "version";
    public static final String ATTR_ACC_ID = "id";
    public static final String ATTR_ACC_NO = "ACCNO";
    public static final String ATTR_ACC_NICK = "nick";
    public static final String ATTR_ACC_IS_DEF = "isdef";
    public static final String ATTR_ACC_PN_CH_REQ = "pinChReq";
    public static final String ATTR_CVV_PIN = "cvv";
    public static final String ATTR_TPIN = "tpin";
    public static final String ATTR_MPIN = "mpin";

    public static final String ATTR_ICON = "icon";
    public static final String ATTR_ISBANK = "isBank";
    public static final String ATTR_IS_BANK_PIN_REQUIRED = "isBPinReq";
    public static final String ATTR_PIN_LEVEL = "pinLevel";
    public static final String ATTR_BANK_PGP_KEY = "key";
    public static final String ATTR_BANK_KEY_EXPO = "mod";
    public static final String ATTR_LEVEL = "level";
    public static final String ATTR_CODE = "code";
    public static final String ATTR_TRN_MOB_NO = "mNo";
    public static final String ATTR_TRN_DATE = "DATE";
    public static final String ATTR_TRN_DATE_FORMAT = "DATEF";
    public static final String ATTR_TRN_TYPE = "type";
    public static final String ATTR_TRN_MUTIPLE = "multiple";
    public static final String ATTR_PP_ALLOWED = "ppallowed";
    public static final String ATTR_TRN_PROD = "PROD";
    public static final String ATTR_STATUS = "STATUS";
    public static final String ATTR_TRN_TIME_FORMAT = "TIMEF";
    public static final String ATTR_TRN_AMT = "amt";
    public static final String ATTR_TRN_AMT_FORMAT = "amtf";
    public static final String ATTR_TRN_AUTH_CODE = "bcode";
    public static final String ATTR_TRN_SUPP = "supp";
    public static final String ATTR_TRN_PAY_MODE = "pmode";
    public static final String ATTR_TRN_BILL_SUPP_HELPLINE = "helpLine";
    public static final String ATTR_BANK_ACC_NUMBER = "accNo";
    public static final String ATTR_BANK_ACC_TYPE = "accType";
    public static final String ATTR_BANK_ACC_CURRENCY = "accCur";
    public static final String ATTR_BANK_ACC_STATUS = "accSts";
    public static final String ATTR_TPAM_FORMAT = "TPAMF";
    public static final String ATTR_TAMT_FORMAT = "TAMTF";
    public static final String ATTR_CUS_CODE = "CSCD";
    public static final String ATTR_BAMT = "BAMT";
    public static final String ATTR_BAMTF = "BAMTF";
    public static final String ATTR_A1CAMT = "A1CAMT";
    public static final String ATTR_A1BAL = "A1BAL";
    public static final String ATTR_SENDER_MSISDN = "senderMSISDN";
    public static final String ATTR_RECEIVER_MSISDN = "receiverMSISDN";
    public static final String ATTR_SENDER_CNIC = "SENDER_CNIC";
    public static final String ATTR_RECEIVER_CNIC = "receiverCNIC";
    public static final String ATTR_AGENTBALANCE = "agentBalance";
    public static final String ATTR_CMOB = "CMOB";
    public static final String ATTR_MSG = "MSG";
    public static final String ATTR_MSG_SMALL = "msg";
    public static final String ATTR_THIRD_PARTY_CUST_SEGMENT_ID = "ATTR_THIRD_PARTY_CUST_SEGMENT_ID";
    public static final String ATTR_TEHSIL_ID = "ATTR_TEHSIL_ID";
    public static final String ATTR_DISTRICT_ID = "ATTR_DISTRICT_ID";
    public static final String ATTR_PITB_APPROVED = "ATTR_PITB_APPROVED";
    public static final String ATTR_TXID = "TXID";
    public static final String ATTR_SMSMSG = "SMSMSG";
    public static final String ATTR_IS_CARD = "IS_CARD";
    public static final String ATTR_IS_OTP_REQUIRED = "IS_OTP_REQ";

    public static final String ATTR_3RD_PARTY_SESSION_ID = "THIRD_PARTY_SESSION_ID";
    public static final String ATTR_NADRA_SESSION_ID = "NADRA_SESSION_ID";
    public static final String ATTR_RAMOB = "RAMOB";
    public static final String ATTR_RANAME = "RANAME";
    public static final String ATTR_RACNIC = "RACNIC";
    public static final String ATTR_CAMT = "CAMT";
    public static final String ATTR_CAMTF = "CAMTF";
    public static final String ATTR_TPAM = "TPAM";
    public static final String ATTR_TPAMF = "TPAMF";
    public static final String ATTR_TAMT = "TAMT";
    public static final String ATTR_TAMTF = "TAMTF";
    public static final String ATTR_PID = "PID";
    public static final String ATTR_BENE_BANK_NAME = "BENE_BANK_NAME";
    public static final String ATTR_BENE_BRANCH_NAME = "BENE_BRANCH_NAME";
    public static final String ATTR_BENE_IBAN = "BENE_IBAN";
    public static final String ATTR_CR_DR = "CR_DR";

    public static final String VEH_REG_NO = "VEH_REG_NO";
    public static final String VEH_CHASSIS_NO = "VEH_CHASSIS_NO";
    public static final String VEH_ASS_NO = "VEH_ASS_NO";
    public static final String VEH_REG_DATE = "VEH_REG_DATE";
    public static final String MAKER_MAKE = "MAKER_MAKE";
    public static final String VEH_CAT = "VEH_CAT";
    public static final String VEH_BODY_TYPE = "VEH_BODY_TYPE";
    public static final String VEH_ENG_CAPACITY = "VEH_ENG_CAPACITY";
    public static final String VEH_SEATS = "VEH_SEATS";
    public static final String VEH_CYLINDERS = "VEH_CYLINDERS";
    public static final String VEH_OWNER_NAME = "VEH_OWNER_NAME";
    public static final String VEH_OWNER_CNIC = "VEH_OWNER_CNIC";
    public static final String VEH_FILEER_STATUS = "VEH_FILEER_STATUS";
    public static final String TAX_PAID_FROM = "TAX_PAID_FROM";
    public static final String TAX_PAID_UPTO = "TAX_PAID_UPTO";
    public static final String VEH_TAX_PAID_LIFETIME = "VEH_TAX_PAID_LIFETIME";
    public static final String VEH_STATUS = "VEH_STATUS";
    public static final String VEH_FITNESS_DATE = "VEH_FITNESS_DATE";
    public static final String VEH_TAX_ID = "VEH_TAX_ID";
    public static final String VEH_TAX_NAME = "VEH_TAX_NAME";
    public static final String VEH_ACCOUNT_HEAD = "VEH_ACCOUNT_HEAD";
    public static final String VEH_TAX_AMOUNT = "VEH_TAX_AMOUNT";
    public static final String VEH_ASSESMENT_DATE = "VEH_ASSESMENT_DATE";
    public static final String VEH_ASSESMENT_NO = "VEH_ASS_NO";

    public static final String ATTR_TXAM = "TXAM";
    public static final String ATTR_TXAMF = "TXAMF";
    public static final String ATTR_DCNO = "DCNO";
    public static final String ATTR_THIRD_PARTY_CUST_SEGMENT_CODE = "THIRD_PARTY_CUST_SEGMENT_CODE";
    public static final String ATTR_BALF = "BALF";
    public static final String ATTR_CORE_AC_NO = "COREACNUMBER";
    public static final String ATTR_BAL = "BAL";
    public static final String ATTR_TRXID = "TRXID";
    public static final String ATTR_BBACID = "BBACID";
    public static final String ATTR_COREACID = "COREACID";
    public static final String ATTR_PROD = "PROD";
    public static final String ATTR_COREACTL = "COREACTL";
    public static final String ATTR_COREACTITLE = "COREACTITLE";
    public static final String ATTR_AMOB = "AMOB";
    public static final String ATTR_RCMOB = "RCMOB";
    public static final String ATTR_SWCNIC = "SWCNIC";
    public static final String ATTR_SWMOB = "SWMOB";
    public static final String ATTR_RWMOB = "RWMOB";
    public static final String ATTR_RWCNIC = "RWCNIC";
    public static final String ATTR_DESCRIPTION = "DESCRIPTION";
    public static final String ATTR_PNAME = "PNAME";
    public static final String ATTR_THIRD_PARTY_TRANSACTION_ID = "THIRD_PARTY_TRANSACTION_ID";
    public static final String ATTR_CONSUMER = "CONSUMER";
    public static final String ATTR_ACTITLE = "ACTITLE";
    public static final String ATTR_ATYPE = "ATYPE";

    public static final String ATTR_DUEDATE = "DUEDATE";
    public static final String ATTR_DUEDATEF = "DUEDATEF";
    public static final String ATTR_LBAMT = "LBAMT";
    public static final String ATTR_LBAMTF = "LBAMTF";
    public static final String ATTR_ISOVERDUE = "ISOVERDUE";
    public static final String ATTR_BPAID = "BPAID";
    public static final String ATTR_ID = "ID";
    public static final String ATTR_DATEF = "DATEF";
    public static final String ATTR_TIMEF = "TIMEF";
    public static final String ATTR_ISREG = "ISREG";

    public static final String ATTR_CID = "cid";
    public static final String ATTR_FID = "fid";
    public static final String ATTR_ISPRODUCT = "isProduct";
    public static final String ATTR_MINAMT = "minamt";
    public static final String ATTR_MIN_LENGTH = "MIN_LENGTH";
    public static final String ATTR_MAX_LENGTH = "MAX_LENGTH";
    public static final String ATTR_MINAMTF = "minamtf";
    public static final String ATTR_MAXAMT = "maxamt";
    public static final String ATTR_MAXAMTF = "maxamtf";
    public static final String ATTR_AMT_REQUIRED = "amtrequired";
    public static final String ATTR_DO_VALIDATE = "dovalidate";

    // Attribute cont
    public static final String ATTR_CNIC = "CNIC";
    public static final String ATTR_FEE = "FEE";
    public static final String ATTR_SEGMENT_ID = "SEGMENT_ID";
    public static final String ATTR_CMSISDN = "CMSISDN";
    public static final String ATTR_CREG_STATE = "CREG_STATE";
    public static final String ATTR_CREG_STATE_ID = "CREG_STATE_ID";
    public static final String ATTR_CNAME = "CNAME";
    public static final String ATTR_NAME = "NAME";
    public static final String ATTR_CUST_ACC_TYPE = "CUST_ACC_TYPE";
    public static final String ATTR_DEPOSIT_AMT = "DEPOSIT_AMT";
    public static final String ATTR_IS_CNIC_SEEN = "IS_CNIC_SEEN";
    public static final String ATTR_IS_BVS_REQUIRED = "IS_BVS_REQ";
    public static final String ATTR_CNIC_EXP = "CNIC_EXP";
    public static final String ATTR_CREG_COMMENT = "CREG_COMMENT";
    public static final String ATTR_CDOB = "CDOB";
    public static final String ATTR_CNIC_EXP_FORMATED = "CNIC_EXP_formated";
    public static final String ATTR_CDOB_FORMATED = "CDOB_FORMATED";

    public static final String ATTR_CUSTOMER_PHOTO_FLAG = "CUSTOMER_PHOTO_FLAG";
    public static final String ATTR_TERMS_PHOTO_FLAG = "TERMS_PHOTO_FLAG";
    public static final String ATTR_SIGNATURE_PHOTO_FLAG = "SIGNATURE_PHOTO_FLAG";
    public static final String ATTR_CNIC_FRONT_PHOTO_FLAG = "CNIC_FRONT_PHOTO_FLAG";
    public static final String ATTR_CNIC_BACK_PHOTO_FLAG = "CNIC_BACK_PHOTO_FLAG";
    public static final String ATTR_L1_FORM_PHOTO_FLAG = "L1_FORM_PHOTO_FLAG";

    public static final String ATTR_SIGNATURE_SKIPPED = "SIGNATURE_SKIPPED";
    public static final String ATTR_NIC_BACK_SKIPPED = "NIC_BACK_SKIPPED";

    // Commands
    public static final int RSP_ERROR = -1;
    public static final int CMD_CHANGE_PIN = 1;
    public static final int CMD_CHANGE_MPIN = 5;
    public static final int CMD_SET_MPIN = 233;
    public static final String SET_MPIN = "233";
    public static final int CMD_CHECK_BALANCE = 6;
    public static final int CMD_AGENT_TO_AGENT_TRANSFER = 21;
    public static final int CMD_LOGIN = 33;
    public static final int CMD_BILL_INQUIRY = 36;
    public static final int CMD_BILL_PAYMENT = 37;
    public static final int CMD_MINI_STATMENT = 66;
    public static final int CMD_CASH_IN = 67;
    public static final int CMD_BANKS = 258;
    public static final int CMD_FUNDS_TRANSFER_BLB2CNIC = 68;
    public static final int CMD_FUNDS_TRANSFER_CNIC2CNIC = 70;
    public static final int CMD_VERIFY_PIN = 74;
    public static final int CMD_BVS_FAIL = 302;
    public static final int CMD_AGENT_TO_AGENT_TRANSFER_INFO = 77;
    public static final int CMD_CASH_IN_INFO = 78;
    public static final int CMD_FUNDS_TRANSFER_BLB2BLB = 101;
    public static final int CMD_TRANSFER_IN_INFO = 103;
    public static final int CMD_IBFT_AGENT = 256 ;
    public static final int CMD_IBFT_AGENT_CONFORMATION = 257 ;
    public static final int CMD_TRANSFER_IN = 104;
    public static final int CMD_TRANSFER_OUT_INFO = 105;
    public static final int CMD_TRANSFER_OUT = 106;
    public static final int CMD_FUNDS_TRANSFER_2CORE_INFO = 111;
    public static final int CMD_FUNDS_TRANSFER_BLB2CORE = 112;
    public static final int CMD_FUNDS_TRANSFER_CNIC2CORE = 114;
    public static final int CMD_CASH_OUT_INFO = 115;
    public static final int CMD_CASH_OUT = 116;
    public static final int CMD_FUNDS_TRANSFER_CNIC2BLB = 117;
    public static final int CMD_RETAIL_PAYMENT_INFO = 118;
    public static final int CMD_RETAIL_PAYMENT = 119;
    public static final int CMD_OPEN_ACCOUNT_VERIFY_CUSTOMER_REGISTRATION = 120;
    public static final int CMD_KHIDMAT_CARD_REGISTRATION = 254;
    public static final int CMD_KHIDMAT_CARD_CONFIRMATION = 255;
    public static final int CMD_CHECK_BVS = 249;
    public static final int CMD_OPEN_ACCOUNT = 121;
    public static final int CMD_VERIFY_NADRA_FINGERPRINT = 125;
    public static final int CMD_SIGN_OUT = 128;
    public static final int CMD_OPEN_ACCOUNT_BVS = 129;  // actually 121
    public static final int CMD_FUNDS_TRANSFER_BLB2BLB_INFO = 130;
    public static final int CMD_FUNDS_TRANSFER_BLB2CNIC_INFO = 131;
    public static final int CMD_FUNDS_TRANSFER_CNIC2BLB_INFO = 132;
    public static final int CMD_FUNDS_TRANSFER_CNIC2CNIC_INFO = 133;
    public static final int CMD_RECEIVE_MONEY_SENDER_REDEEM_INFO = 136;
    public static final int CMD_RECEIVE_MONEY_SENDER_REDEEM_PAYMENT = 137;
    public static final int CMD_RECEIVE_MONEY_PENDING_TRX_PAYMENT = 138;
    public static final int CMD_RECEIVE_MONEY_RECEIVE_CASH_INFO = 139;
    public static final int CMD_RECEIVE_MONEY_RECEIVE_CASH = 143;
    public static final int CMD_OTP_VERIFICATION = 178;
    public static final int CMD_OPEN_ACCOUNT_NADRA_VERIFICATION = 181;
    public static final int CMD_OPEN_ACCOUNT_OTP_VERIFICATION = 182;
    public static final int CMD_CASH_OUT_BY_TRX_ID_INFO = 196;
    public static final int CMD_CASH_OUT_BY_TRX_ID = 197;
    public static final int CMD_COLLECTION_PAYMENT_INFO = 212;
    public static final int CMD_EXCISE_AND_TAXATION_INFO = 241;
    public static final int CMD_TRANS_PURPOSE_CODE = 247;
    public static final int CMD_EXCISE_AND_TAXATION_PAYMENT = 242;
    public static final int CMD_COLLECTION_PAYMENT_TRX = 213;
    public static final int CMD_INFO_L1_TO_HRA = 224;
    public static final int CMD_L1_TO_HRA = 226; // 121
    public static final int CMD_HRA_CASH_WITHDRAWAL = 227;
    public static final int CMD_3RD_PARTY_CASH_OUT_INFO = 228;
    public static final int CMD_BOP_CASH_OUT_INFO = 251;
    public static final int CMD_BOP_ISSUANCE_REISSUANCE_INFO = 263;
    public static final int CMD_BOP_ISSUANCE_REISSUANCE = 264;
    public static final int CMD_BOP_CASH_OUT = 252;
    public static final int CMD_3RD_PARTY_CASH_OUT = 229;
    public static final int CMD_3RD_PARTY_AGENT_BVS = 250;
    public static final int CMD_FETCH_SEGMENTS = 253;
    public static final int CMD_DEBIT_CARD_FETCH_SEGMENTS = 293;
    public static final int CMD_OPEN_ACCOUNT_FETCH_SEGMENTS = 295;
    public static final int CMD_PROOF_OF_LIFE_INFO = 269;
    public static final int CMD_PROOF_OF_LIFE = 270;

    public static final int CMD_DEBIT_CARD = 231;
    public static final int CMD_DORMANCY = 299;
    public static final int CMD_DORMANCY_CONFIRMATION = 300;
    public static final int CMD_DEBIT_CARD_CONFIRMATION = 232;
    public final static int REQUEST_GET_DEVICE_CAMERA_FOR_CUSTOMER = 4;
    public final static int REQUEST_GET_DEVICE_CAMERA_FOR_CNIC_FRONT = 5;
    public final static int REQUEST_GET_DEVICE_CAMERA_FOR_SIGNATURE = 6;
    public final static int REQUEST_GET_DEVICE_CAMERA_FOR_TERMS = 7;
    public final static int REQUEST_GET_DEVICE_CAMERA_FOR_CNIC_BACK = 8;
    public final static int REQUEST_GET_DEVICE_CAMERA_FOR_L1_FORM = 9;

    public static final int PAYMENT_TYPE_ACCOUNT = 0;
    public static final int PAYMENT_TYPE_CASH = 1;

    public static final int CHANGE_LOGIN_PIN = 0;
    public static final int CHANGE_TRANSACTIONAL_PIN = 1;

    public static final byte FLOW_ID_CASH_IN = 1;
    public static final byte FLOW_ID_BOP_CARD_ISSUANCE_RE_ISSUANCE = 47;
    public static final byte FLOW_ID_CASH_OUT_BY_IVR = 2;
    public static final byte FLOW_ID_FT_BLB_TO_BLB = 3;
    public static final byte FLOW_ID_FT_BLB_TO_CORE_AC = 4;
    public static final byte FLOW_ID_BILL_PAYMENT_GAS = 5;
    public static final byte FLOW_ID_BILL_PAYMENT_WATER_ELECTRICITY = 6;
    public static final byte FLOW_ID_BILL_PAYMENT_MOBILE_POSTPAID = 7;
    public static final byte FLOW_ID_BILL_PAYMENT_MOBILE_PREPAID = 8;
    public static final byte FLOW_ID_BILL_PAYMENT_TELEPHONE = 9;
    public static final byte FLOW_ID_DORMANCY = 63;
    public static final byte FLOW_ID_RETAIL_PAYMENT = 10;
    public static final byte FLOW_ID_TRANSFER_IN = 11;
    public static final byte FLOW_ID_TRANSFER_OUT = 12;
    public static final byte FLOW_ID_IBFT_AGENT = 42;
    public static final byte FLOW_ID_ACCOUNT_OPENING = 13;
    public static final byte FLOW_ID_FT_CNIC_TO_CNIC = 14;
    public static final byte FLOW_ID_FT_BLB_TO_CNIC = 15;
    public static final byte FLOW_ID_FT_CNIC_TO_BLB = 16;
    public static final byte FLOW_ID_FT_CNIC_TO_CORE_AC = 17;
    public static final byte FLOW_ID_PROOF_OF_LIFE = 51;
    public static final byte FLOW_ID_AGENT_TO_AGENT = 18;
    public static final byte FLOW_ID_CASH_OUT_BY_TRX_ID = 19;
    public static final byte FLOW_ID_EXCISE_AND_TAXATION = 36;
    public static final byte FLOW_ID_COLLECTION_PAYMENT = 21;
    public static final byte FLOW_ID_HRA = 23;
    public static final byte FLOW_ID_HRA_CASH_WITH_DRAWAL = 24;
    public static final byte FLOW_ID_L0_L1 = 25;
    public static final byte FLOW_ID_BOP_KHIDMAT_CARD_REGISTRATION = 40;
    public static final byte FLOW_ID_3RD_PARTY_CASH_OUT = 26;
    public static final byte FLOW_ID_BOP_CASH_OUT = 39;
    public static final byte FLOW_ID_BOP_CASH_OUT_BY_CNIC = 43;
    public static final byte FLOW_ID_DEBIT_CARD = 27;

    public static final String CATEGORY_ID_CASH_OUT = "2";
    public static final String CATEGORY_ID_MONEY_TRANSFER = "3";
    public static final String PRODUCT_ID_SEND_MONEY = "50011";
    public static final String PRODUCT_ID_RECEIVE_CASH = "50036";
    public static final String PRODUCT_ID_CASH_WITH_DRAWAL = "10245106";
    public static final String PRODUCT_ID_BOP_CASH_OUT = "10245156";
    public static final String PRODUCT_ID_BOP_CASH_OUT_BY_CNIC = "10245159";
    public static final String CATEGORY_ID_HRA_REGISTRATION = "00";
    public static final String CATEGORY_ID_3RD_PARTY_CASH_OUT = "208";
    public static final String PRODUCT_ID_HRA_REGISTRATION = "01";
    public static final String PRODUCT_ID_EOBI_CASH_OUT = "2510986";
    public static final String PRODUCT_ID_3RD_PARTY_CASH_OUT = "2510812"; //2510982

    /**
     * App Internal Product IDs
     */
    public static final String PRODUCT_ID_BLB_BALANCE_INQUIRY = "10025";
    public static final String PRODUCT_ID_CORE_BALANCE_INQUIRY = "10026";
    public static final String PRODUCT_ID_CHANGE_LOGIN_PIN = "10027";
    public static final String PRODUCT_ID_CHANGE_MPIN = "10028";
    public static final String PRODUCT_ID_BLB_MINI_STATEMENT = "10029";
    public static final String PRODUCT_ACCONT_OPENING = "2510763";
    public static final String PRODUCT_ID_CASH_OUT = "50006";
    public static final String PRODUCT_ID_CASH_OUT_BY_TRX_ID = "50006";

    /**
     * App Internal Flow IDs
     */

    public static final byte FLOW_ID_BALANCE_INQUIRY = 101;
    public static final byte FLOW_ID_CHANGE_PIN = 102;
    public static final byte FLOW_ID_MINI_STATEMENT = 103;
    public static final byte FLOW_ID_RM_SENDER_REDEEM = 104;
    public static final byte FLOW_ID_RM_RECEIVE_CASH = 105;
    public static final byte FLOW_ID_RM_RECEIVE_CASH_NOT_REGISTERED = 106;
    public static final byte FLOW_ID_HRA_REGISTRATION = 19;

    /**
     * App Internal Category IDs
     */
    public static final byte CATEGORY_ID_RECEIVE_MONEY = 101;
    public static final byte CATEGORY_ID_SENDER_REDEEM = 102;
    public static final byte CATEGORY_ID_RECEIVE_CASH = 103;

    public static final byte RESULT_CLOSE_ALL = -1;

    public static final String ENCRYPTION_TYPE = "1";

    public class Messages {
        public static final String INTERNET_CONNECTION_PROBLEM = "There is no or poor internet connection. Please connect to stable internet connection and try again.";
        public static final String EXCEPTION_HTTP_UNAVAILABLE = "Service unavailable due to technical difficulties. Please try again or contact service provider.";
        public static final String EXCEPTION_SERVICE_UNAVAILABLE = "Service unavailable due to technical difficulties. Please try again or contact service provider.";
        public static final String EXCEPTION_MAC_ADDRESS_NOT_FOUND = "MAC address not found. Please connect to your WIFI network and try again.";
        public static final String INVALID_BILL_AMOUNT = "Bill amount is invalid.";
        public static final String EXCEPTION_TIME_OUT = "This seems to be taking longer than usual. Please try again later.";
        public static final String EXCEPTION_INVALID_RESPONSE = "We are unable to process your request at the moment. Please try again later.";
        public static final String EXCEPTION = "Connection with the server can not be established at this time. Please try again or contact your service provider.";
        public static final String EXCEPTION_GENERIC = "Your session has expired. Please try again.";
        public static final String EXCEPTION_SSL_HANDSHAKE = "No trusted certificate found. Connection with the server can not be established at this time.";
        public static final String VERIFY_CUSTOMER_DETAILS = "Please verify details.";

        public static final String INVALID_APP = "Application Security Compromised!! This is not a valid application.";
        public static final String INVALID_INSTALLER = "Application Security Compromised!! Invalid installer.";
        public static final String INVALID_ENVIRONMENT = "Application Security Compromised!! Invalid environment.";
        public static final String INVALID_DEBUGGABLE = "Application Security Compromised!!";

        public static final String BILL_ALREADY_PAID = "Bill Already Paid.";
        public static final String INVLAID_INPUT = "Invalid Input";
        public static final String INVALID_USERNAME = "Length should be of " + Constants.USERNAME_LENGTH + " digits.";
        public static final String INVALID_PASSWORD = "Length should be of " + Constants.PASSWORD_LENGTH + " digits.";
        public static final String INVALID_PIN = "Length should be of 4 digits.";
        public static final String INVALID_THIRD_PARTY_OTP = "Length should be of "+Constants.MAX_LENGTH_THIRD_PARTY_OTP+" digits.";
        public static final String INVALID_BOP_OTP = "Length should be of "+Constants.MAX_LENGTH_BOP_OTP+" digits.";
        public static final String INVALID_VERIFICATION_TRX_CODE = "Length should be of " + Constants.MAX_LENGTH_VERIFICATION_OTP + " digits.";
        public static final String INVALID_TRX_CODE = "Length should be of " + Constants.MAX_LENGTH_OTP + " digits.";
        public static final String INVALID_MOBILE_NUMBER = "Length should be of 11 digits.";
        public static final String INVALID_CNIC = "Length should be of 13 digits.";
        public static final String INVALID_CARD = "Length should be of 16 digits.";
        public static final String INVALID_TRANSACTION_ID = "Length should be of " + Constants.MAX_LENGTH_TRANSACTION_ID + " digits.";
        public static final String INVALID_MOBILE_NUMBER_FORMAT = "Mobile No. must start with 03.";
        public static final String ERROR_MULTIPLE = "You have entered invalid amount.";
        public static final String ERROR_AMOUNT_INVALID = "You have entered invalid amount.";
        public static final String ERROR_INVALID_OTP = "You should entered OTP of 4 numbers.";
        public static final String ERROR_CUSTOMER_AGE = "Customer age cannot be less than 18 year.";
        public static final String ERROR_INVALID_CUSTOMER_DOB = "Please enter valid date of birth.";
        public static final String ERROR_INVALID_CARD_EXPEIRY = "Please enter valid card expiry date.";
        public static final String SESSION_EXPIRED = "Application Session has expired. Please Re-Login.";
        public static final String ALERT_HEADING = "Alert Notification";
        public static final String ALERT_ERROR = "Verification Error";
        public static final String REGISTERATION_REQUIRED = "Would you like to register as a branchless banking customer for ";
        public static final String DEVICE_IS_ROOTED = "Device Rooted/Jail Broken or Malware/Virus Detected!! Data Security Compromised. Any/all actions using this application can loose private data, and therefore result in the possibility of fraudulent activity on your account. Do you wish to continue using this device and application?";
        public static final String ERROR_INVALID_ISCNIC_SEEN = "CNIC is not verified.";
        public static final String CANCEL_TRANSACTION = "Do you want to cancel the transaction?";
        public static final String AMOUNT_MAX_LIMIT = "Amount should be between 10 and 9,999,999.";
        public static final String SESSSION_OUT_ERROR = "Unstable Internet Connection. Press Exit to quit.";

        public static final String CAMERA_NOT_AVAILABLE_ERROR = "Camera is required to perform BVS transaction.";
        public static final String CAMERA_AUTO_FOCUS_ERROR = "Camera with auto focus is required to perform BVS transaction.";
        public static final String OS_ERROR = "Minimum OS version 4.4 is required to perform BVS transaction.";

        public static final String APPLICATION_VERSION_LEVEL_CRITICAL = "A new version of AGENTmate is now available.For enhanced usability and feature please download new "
                + "version from " + APP_DOWNLOAD_URL;
        public static final String APPLICATION_VERSION_LEVEL_OBSOLETE = "Please download the new AGENTmate Application "
                + "from "
                + APP_DOWNLOAD_URL
                + ". The older version is now obsolete and will not work.";
        public static final String APPLICATION_SSL_EXPIRE = "Please download the new AGENTmate Application. The older version is now obsolete and will not work.";
    }

    public class IntentKeys {
        public static final String FLOW_ID = "FLOW_ID";
        public static final String PURPOSE_CODE = "PURPOSE_CODE";
        public static final String TRANSACTION_INFO_MODEL = "TRANSACTION_INFO_MODEL";
        public static final String KHIDMAT_CARD_REGISTRATION_MODEL = "KHIDMAT_CARD_REGISTRATION_MODEL";
        public static final String TRANSACTION_MODEL = "TRANSACTION_MODEL";
        public static final String CITIES_MODEL = "CITIES_MODEL";
        public static final String TITLE = "TITLE";
        public static final String VALID_INDEXES = "validIndexes";
        public static final String FINGERS = "fingers";
        public static final String VALID_FINGER_INDEXES = "validFingerIndexes";
        public static final String TRANSACTION_AMOUNT = "TRANSACTION_AMOUNT";
        public static final String HRA_LINKED_REQUEST = "HRA_LINKED_REQUEST";
        public static final String PIN_CHANGE_TYPE = "PIN_CHANGE_TYPE";
        public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
        public static final String EXCISE_AND_TAXATION_INFO_MODEL = "EXCISE_AND_TAXATION_INFO_MODEL";
        public static final String PAYMENT_TYPE = "PAYMENT_TYPE";
        public static final String ENCRPYED_KEY = "ENCRPYED_KEY";
        public static final String CATEGORY_MODEL = "CATEGORY_MODEL";
        public static final String CATEGORY_LIST = "CATEGORY_LIST";
        public static final String PRODUCT_LIST = "PRODUCT_LIST";
        public static final String PRODUCT_MODEL = "PRODUCT_MODEL";
        public static final String MSG = "MSG";
        public static final String MAC_ADDRESS = "MAC_ADDRESS";
        public static final String PRODUCT_MODEL_ACCOUNT_OPENING = "PRODUCT_MODEL_ACCOUNT_OPENING";
        public static final String SHOW_REGISTRATION_POPUP = "SHOW_REGISTRATION_POPUP";
        public static final String EXIT_APP = "EXIT_APP";
        public static final String BOTH_PIN_CHANGE = "BOTH_PIN_CHANGE";
        public static final String PRODUCT_ID = "PRODUCT_ID";
        public static final String BVS_FLAG = "BVS_FLAG";
        public static final String CASH_OUT_TYPE = "CASH_OUT_TYPE";

        public static final String FLAG_TRANSITION = "FLAG_TRANSITION";
        public static final String FLAG_LOGIN = "FLAG_LOGIN";
        public static final String FLAG_OPEN_ACCOUNT = "FLAG_OPEN_ACCOUNT";
        public static final String SESSION_OUT = "SESSION_OUT";
        public static final String BIOMETRIC_FLOW = "BIOMETRIC_FLOW";

        public static final String ORG_LOC_1 = "ORG_LOC_1";
        public static final String ORG_LOC_2 = "ORG_LOC_2";
        public static final String ORG_LOC_3 = "ORG_LOC_3";
        public static final String ORG_LOC_4 = "ORG_LOC_4";
        public static final String ORG_LOC_5 = "ORG_LOC_5";

        public static final String ORG_REL_1 = "ORG_REL_1";
        public static final String ORG_REL_2 = "ORG_REL_2";
        public static final String ORG_REL_3 = "ORG_REL_3";
        public static final String ORG_REL_4 = "ORG_REL_4";
        public static final String ORG_REL_5 = "ORG_REL_5";

        public static final String PURPOSE_OF_ACCOUNT = "PURPOSE_OF_ACCOUNT";
        public static final String OCCUPATION = "OCCUPATION";
        public static final String NEXT_OF_KIN = "NEXT_OF_KIN";

        public static final String RCMOB = "RCMOB";
        public static final String RCNIC = "RCNIC";
        public static final String TRXID = "TRXID";
        public static final String ACCOUNT_OPEN_MIN_AMOUNT = "ACCOUNT_OPEN_MIN_AMOUNT";
        public static final String ACCOUNT_OPEN_MAX_AMOUNT = "ACCOUNT_OPEN_MAX_AMOUNT";
        public static final String IS_RECEIVE_CASH = "IS_RECEIVE_CASH";
        public static final String IS_EXCISE_AND_TAXATION = "IS_EXCISE_AND_TAXATION";
        public static final String OPEN_ACCOUNT_IMAGES_LIST = "OPEN_ACCOUNT_IMAGES_LIST";
        public static final String CAME_FROM_BVS = "CAME_FROM_BVS";

        public static final String AREA_NAME = "areaName";
        public static final String IDENTIFIER = "identifier";
        public static final String SECONDARY_IDENTIFIER = "secondaryIdentifier";
        public static final String CONTACT_NUMBER = "contactNumber";
        public static final String SECONDARY_CONTACT_NUMBER = "secondaryContactNumber";
        public static final String REMITTANCE_TYPE = "remittanceType";
        public static final String REMITTANCE_AMOUNT = "remittanceAmount";
        public static final String ACCOUNT_NUMBER = "accountNumber";
        public static final String FINGER = "finger";
        public static final String HRA_MODEL = "hraModel";
        public static final String CNIC = "CNIC";
        public static final String DCNO = "DCNO";
        public static final String CMOB = "CMOB";
        public static final String SEGMENT_CODE = "SEGMENT_CODE";
        public static final String MOBILE = "mobile";
        public static final String IS_CASH_WITHDRAW = "isCashWithDraw";
        public static final String L0_TO_L1_UPGRADE = "l0ToL1Upgrade";
    }

    public static final int IS_RECEIVE_CASH = 1;
    public static final int IS_NOT_RECEIVE_CASH = 0;

    public class ErrorCodes {
        public static final String LOGIN_FAILED_LEVEL2 = "9009";
        public static final String SESSION_EXPIRED = "9007";
        public static final String WALK_IN_CUSTOMER = "8076";
        // public static final String WALK_IN_CUSTOMER = "9001";
        public static final String CREDENTIALS_BLOCKED = "9010";
        public static final String FILE_NOT_FOUND = "9018";
        public static final String INTERNAL = "5000";
        public static final String INTERNAL_CONNECTION = "5001";
        public static final String INTERNAL_SSL = "5911";
        public static final String INTERNAL_SSL_HAND_SHAKE = "5912";
        public static final String OTP_INVALID = "9023";
        public static final String OTP_VERIFICATION = "9025";
        public static final String OTP_VERIFICATION_ERROR = "9027";
        public static final String OTP_RESEND = "9026";
        public static final String OTP_REGENERATION = "9027";
        public static final String INSUFFICIENT_BALANCE_ERROR = "9044";
        public static final String DEBIT_CARD_PENDING_REQUEST_ERROR = "9046";
        public static final String OTP_ERROR = "9028";
        public static final String OTP_ERROR_FINAL = "9029";
        public static final String PHOENIX = "9999";
        public static final String EXCEPTION_ERROR = "00000";
    }

    public class VersionUsageLevel {
        public static final int NORMAL = 0;
        public static final int CRITICAL = 1;
        public static final int BLOCK = 2;
        public static final int OBSOLETE = 3;
    }

    public class ProductIds {
        public static final int BLB2BLB = 50000;
        public static final int BLB2CNIC = 50010;
        public static final int CNIC2BLB = 50030;   // bvs
        public static final int CNIC2CNIC = 50011;  // bvs
        public static final int BLB2CORE = 50026;
        public static final int CNIC2CORE = 50028;
        public static final int BULK2CNIC = 2510801;
    }

    public static class AccountOpenningImages {
        public static final int CUSTOMER = 0;
        public static final int CNIC_FRONT = 1;
        public static final int SIGNATURE = 2;
        public static final int TERMS = 3;
        public static final int CNIC_BACK = 4;
        public static final int L1_FORM = 5;

        public static final ArrayList<String> imageTags = new ArrayList<String>() {
            private static final long serialVersionUID = 1L;

            {
                add(0, "Customer");
                add(1, "CNIC Front");
                add(2, "Signature");
                add(3, "Terms & Conditions");
                add(4, "CNIC Back");
                add(5, "L1 Form");
            }
        };
    }

    public static final String REGISTRATION_STATE_BULK_REQUEST_RECEIVED = "1";
    public static final String REGISTRATION_STATE_REQUEST_RECEIVED = "2";
    public static final String REGISTRATION_STATE_VERIFIED = "3";
    public static final String REGISTRATION_STATE_DISCREPANT = "4";

    public static class AgentAccountType {
        public static final int NORMAL = 3;
        public static final int HANDLER = 12;
    }

    public static final int REQUEST_CODE_TERMS_CONDITIONS = 120;
    public static final int REQUEST_CODE_VERIFY_NADRA = 121;
    public static final int REQUEST_CODE_OTC = 123;
    public static final int REQUEST_CODE_FINGER_SCAN = 124;
    public static final int REQUEST_CODE_BIOMETRIC_SELECTION = 125;
    public static final int REQUEST_CODE_BVS = 127;
    public static final int REQUEST_CODE_HRA = 128;

    public static final int RESULT_CODE_SUCCESS_NADRA = 504;
    public static final int RESULT_CODE_FAILED_NADRA_FINGER_INDEXES = 501;
    public static final int RESULT_CODE_FAILED_NADRA_OTHER_ERROR = 503;
    public static final int RESULT_CODE_FINGERS_EMPTY = 509;

    public static final int RESULT_CODE_SUCCESS_OTC = 508;
    public static final int RESULT_CODE_FAILED_OTC_FINGER_INDEXES = 506;
    public static final int RESULT_CODE_FAILED_OTC_OTHER_ERROR = 507;

    public class AreaName {
        public static final String PUNJAB = "Punjab";
        public static final String KHYBER_PAKHTUNKHWA = "Khyber Pakhtunkhwa";
        public static final String FATA = "fata";
        public static final String SINDH = "Sindh";
        public static final String BALUCHISTAN = "BRA, Balochistan";
        public static final String ISLAMABAD = "FBR, ICT";
        public static final String GILGIT_BALTISTAN = "gilgit-baltistan";
        public static final String AZAD_KASHMIR = "azad kashmir";
    }

    public class BvsDevices {
        public static final String SUPREMA = "Suprema";
        public static final String SUPREMA_SLIM_2 = "SupremaBioMiniSlim2";
        public static final String P41M2 = "P41M2";
    }

    public static final String DEVICE_NOT_ROOTED = "0";
    public static final String DEVICE_ROOTED = "1";

    public static final String CASH_OUT_BY_IVR = "1";
    public static final String CASH_OUT_BY_TRX_ID = "2";

    public static final String REQUEST_TYPE_PAYSYS = "Paysys";
    public static final String REQUEST_TYPE_NADRA = "Nadra";

    public static final String PAYSYS = "PAYSYS";
    public static final String SUPREMA = "SUPREMA";
    public static final String SUPREMA_SLIM_2 = "SUPREMA_SLIM_2";
    public static final String P41M2 = "P41M2";
    public static final String DEBIT_CARD_TERM_AND_CONDITIONS = "DEBIT_CARD_TERM_AND_CONDITIONS";

}