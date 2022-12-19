package com.inov8.jsblconsumer.util;

import java.util.ArrayList;

public class Constants {

    public static final String PROTOCOL = BuildConstants.PROTOCOL;
    public static final String BASE_URL = BuildConstants.BASE_URL;
    public static boolean withSecurityFixes = true;

    public static final String APPURL = PROTOCOL + BASE_URL + "/allpay.me";
    public static final String APP_DOWNLOAD_URL = "https://play.google.com/store/apps/details?id=com.inov8.jsblconsumer&hl=en";

    public static final String CUSTOM_PROXY_VERIFY = BuildConstants.CUSTOM_PROXY_VERIFY;
    //  USER_TYPE: 2 = Customer, 3 = Retailer/Agent
    public static final int AD_TYPE_SLIDER = 1;
    public static final int AD_TYPE_VIDEO = 2;
    public static final String AD_URL = PROTOCOL + BASE_URL + "/images/ads/";
    public static final String PROD_IMG_URL = PROTOCOL + BASE_URL + "/images/utilities/";
    public static final String API_KEY_BOOKME = "1212789ed434433231a53639e5gg434";
    public static final String BOOKME_BASE_URL = "https://bookme.pk/widgets/";
    public static final String BOOKME_PUBLIC_KEY = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAodEwuoIDkVPvQ6SOEAuS35Ris3evqFOD2YaGm4PHO2FQsCV55FOn4kynjAqoAmH9yMDreDiOPcLdloThiw75aoUKex3BP8P65VhljIGoiNyO10phV0vjEREz+wVwa3irXMSz68RQc7ReQmfA99O5HIxre9lel0peh/39O5d/kkStIoGYQ9vwfCbxgSWXFGcjiqizOYss76uazjATq2pcOsosrQKliBAkax9OiMzZe1D2KweBVn86VUTtFC1K3goafJpGufROGNx9umfh3BiknC9MZpWupyS4bTK1EWKCrzmz5h2glw6Dz1FA5K7kLHrQTtq6SBTVc9wjcvaBEdyYtr4TadcIdoPxVsA77zF3HhzJA7Jd1B4Zyly2Kco4iNleYoQuDQk3re6iJ425iqHGLfQU8/BF5qTLLeUjOhtwXlMj+Er2cbE+EU4NvInX60O9DjZYLkba7y4oWMedXYAVmE0z3frMChM4xazG3On2zI7L9hCROjtdlvtyJmzu4tDDI78mmsjAt38ujrUing6jUjSXksNA2/BB1xCM/gS3Ho4SfJNMJrrAN240Ew5pILG2oQPxly2d7YxXq+xo5X1fs2qU7wr3+7zTs96CJE0Ev+rnvzVUnleODucM7F5SZ8cb4kUrt3ocWMfMnHPTu/eKw2SbCxuf4a1nN3+cPvXMLvsCAwEAAQ==";

    public static final String USER_TYPE = "2";


    public static final String APP_ID = "2";

    public static String SECURITY_KEY = BuildConstants.HANDSHAKE_KEY;

    public static String HANDSHAKE_KEY = "";
    public static final String SIGNATURE = BuildConstants.SIGNATURE;
    public static final String APPLICATION_VERSION = "2.0.9.18";
    public static final String DEFAULT_FAQ_VERSION = "-1";
    public static final String ATTR_PERSON_DATA = "PERSON_DATA";
    public static final int CONNECTION_TIMEOUT = 180; // seconds
    public static final int OTP_RESEND_TIMER = 60000; // miliseconds
    public static final int SESSION_TIMEEOUT = 180;
    public static final String CURRENCY = "PKR ";
    public static final String TRUE = "1";
    public static final String BANK_ACC_TRUE = "01";
    public static final String LOGIN_PIN = "0";
    public static final String MPIN = "1";
    public static final String SET_MPIN = "233";
    public static final String TAG_PAYMENT_REASONS = "paymentreasons";
    public static final String TAG_PAYMENT_REASON = "paymentreason";
    //    public static final String SET_NEW_MPIN = "233";
    public static final String IGNORE_AND_GOTO_LOGIN = "3";
    public static final String IGNORE_AND_GOTO_MENU = "4";
    public static final int CATEGORY_IS_PRODUCT = 1;
    public static final int PIN_LENGTH = 4;
    public static final int LOGIN_PIN_LENGTH = 4;
    public static final int ACCOUNT_OPENING_OTP_LENGTH = 5;
    public static final int MAX_AMOUNT_LENGTH = 7;
    public static final long MAX_AMOUNT = 9999999;
    public static final String URL_TERMS_OF_USE = PROTOCOL + BASE_URL + "/terms-and-conditions.jsp";
    public static final String URL_TERMS_OF_DEBIT_CARD_ISSUENCE = PROTOCOL + BASE_URL + "/debit-card-terms-and-conditions.jsp";
    public static final String URL_USER_GUIDE = PROTOCOL + BASE_URL + "/user-guide.jsp";
    public static final String KEY_CMDRCVD = "cmdRcvd";
    public static final String KEY_CAT_VER = "CVNO";
    public static final String KEY_BANK_ACC_LIST = "BACL";
    public static final String KEY_LIST_USR_MBANK = "mbank";
    public static final String KEY_LIST_USR_TPURPS = "tpurps";
    public static final String KEY_LIST_USR_BANK = "bank";
    public static final String KEY_LIST_MSGS = "msgs";
    public static final String KEY_LIST_ERRORS = "errs";
    public static final String KEY_LIST_PAYMENT_REASONS = "paymentreasons";
    public static final String KEY_LIST_TRANS = "trans";
    public static final String KEY_LIST_CATALOG = "catalog";
    public static final String KEY_LIST_CARD_TYPES = "cardtypes";
    public static final String KEY_LIST_CARD_CATEGORIES = "cardcategories";
    public static final String KEY_LIST_CARD_RANKS = "cardranks";
    public static final String KEY_LIST_CARD_APPLICANT_TYPES = "cardapplicants";
    public static final String KEY_LIST_CARD_STATES = "cardstates";
    public static final String KEY_LIST_FAQS = "faqs";
    public static final String KEY_LIST_LOCATIONS = "locations";
    public static final String ATTR_CUSTOMER_PHOTO_FLAG = "CUSTOMER_PHOTO_FLAG";
    public static final String ATTR_CNIC_FRONT_PHOTO_FLAG = "CNIC_FRONT_PHOTO_FLAG";
    public final static int REQUEST_GET_DEVICE_CAMERA_FOR_CUSTOMER = 4;
    public final static int REQUEST_GET_DEVICE_CAMERA_FOR_CNIC_FRONT = 5;
    public static final int RSP_ERROR = -1;
    public static final int CMD_CHANGE_PIN = 1;
    public static final int ACCOUNT_OPENING_REQUEST = 1144;
    public static final int VERIFY_ACCOUNT_REQUEST = 1142;
    //    public static final int CMD_SET_MPIN = 250;

    public static final int CMD_SET_MPIN = 233;
    public static final int CMD_SCHEDULE_PAYMENT = 2633;
    public static final int CMD_CHANGE_MPIN = 5;
    public static final int CMD_REGENERATE_OTP = 271;
    public static final int CMD_CHECK_BALANCE = 6;
    public static final int CMD_RETAIL_PAYMENT_VERIFICATION = 208;
    public static final int CMD_RETAIL_PAYMENT_MPASS = 209;
    public static final int CMD_MY_LIMITS = 187;
    public static final int CMD_GENERATE_MPIN = 216;
    public static final int CMD_FORGOT_PASSWORD_FIRST = 217;
    public static final int CMD_FORGOT_PASSWORD_SECOND = 218;
    public static final int CMD_RESET_LOGIN_PIN = 275;
    public static final int CMD_FORGOT_MPIN = 277;
    public static final int CMD_TRANS_PURPOSE_CODE = 247;
    public static final int CMD_CUSTOMER_REGISTRATION = 185;
    public static final int CMD_CUSTOMER_TERMS_CONDITIONS = 251;
    public static final int CMD_LOGIN = 33;
    public static final int CMD_FAQS = 188;
    public static final int CMD_CUSTOMER_REGISTER_OTP = 182;
    public static final int CMD_OPEN_ACCOUNT = 186;
    public static final int CMD_MINI_LOAD_INFO = 144;
    public static final int CMD_MINI_LOAD = 145;
    public static final int CMD_BILL_INQUIRY = 204;
    public static final int CMD_BILL_PAYMENT = 205;
    public static final int CMD_MINI_STATMENT = 66;
    public static final int CMD_FUNDS_TRANSFER_BLB2CNIC = 201;
    public static final int CMD_FUNDS_TRANSFER_BLB2CNIC_INFO = 200;
    public static final int CMD_VERIFY_PIN = 74;
    public static final int CMD_TRANSFER_IN_INFO = 103;
    public static final int CMD_HRA_TO_WALLET_INFO = 238;
    public static final int CMD_HRA_TO_WALLET = 240;
    public static final int CMD_TRANSFER_IN = 104;
    public static final int CMD_TRANSFER_OUT_INFO = 105;
    public static final int CMD_TRANSFER_OUT = 106;
    public static final int CMD_FUNDS_TRANSFER_2CORE_INFO = 202;
    public static final int CMD_FUNDS_TRANSFER_BLB2CORE = 203;
    public static final int CMD_RETAIL_PAYMENT_INFO = 81;
    public static final int CMD_RETAIL_PAYMENT = 82;
    public static final int CMD_SIGN_OUT = 128;
    public static final int CMD_DEBIT_CARD_ISSUANCE_INFO = 151;
    public static final int CMD_DEBIT_CARD_ISSUANCE = 152;
    public static final int CMD_DEBIT_CARD_ACTIVATION_INFO = 153;
    public static final int CMD_DEBIT_CARD_ACTIVATION = 154;
    public static final int CMD_DEBIT_CARD_BLOCK_INFO = 155;
    public static final int CMD_DEBIT_CARD_BLOCK = 156;
    public static final int CMD_ATM_PIN_GENERATE_CHANGE_INFO = 157;
    public static final int CMD_ATM_PIN_GENERATE_CHANGE = 158;
    public static final int CMD_FUNDS_TRANSFER_BLB2BLB = 199;
    public static final int CMD_ADVANCE_LOAN = 267;
    public static final int CMD_FUNDS_TRANSFER_BLB2BLB_INFO = 198;
    public static final int CMD_DEVICE_UPDATE_OTP_VERIFICATION = 178;
    public static final int CMD_LOGIN_OTP_VERIFICATION = 220;
    public static final int CMD_CASH_WITHDRAWAL = 195;
    public static final int CMD_LOCATOR = 207;
    public static final int CMD_COLLECTION_INFO = 210;
    public static final int CMD_COLLECTION_PAYMENT = 211;
    public static final int CMD_INFO_L1_TO_HRA = 224;
    public static final int CMD_L1_TO_HRA = 226;
    public static final int CMD_DEBIT_CARD = 231;
    public static final int CMD_DEBIT_CARD_CONFIRMATION = 232;
    public static final int CMD_GET_BANKS = 258;
    public static final int CMD_ADVANCE_LOAN_INFO = 266;
    public static final int CMD_BOOKME_INQUIRY = 272;
    public static final int CMD_BOOKME_PAYMENT = 273;
    public static final int CMD_DEBIT_CARD_INQUIRY = 279;
    public static final int CMD_DEBIT_CARD_ACTIVATION_CHANGE_PIN = 280;
    public static final int CMD_DEBIT_CARD_BLOCK_TYPE = 281;
    public static final int CMD_SET_MPIN_LATER = 287;

    public static final int PAYMENT_TYPE_ACCOUNT = 0;
    public static final int PAYMENT_TYPE_CASH = 1;
    public static final byte FLOW_ID_FT_BLB_TO_BLB = 3;
    public static final byte FLOW_ID_FT_BLB_TO_CORE_AC = 4;
    public static final byte FLOW_ID_FT_BLB_TO_CNIC = 15;
    public static final byte FLOW_ID_FT_BLB_TO_IBFT = 44;
    public static final byte FLOW_ID_BILL_PAYMENT_GAS = 5;
    public static final byte FLOW_ID_BILL_PAYMENT_WATER_ELECTRICITY = 6;
    public static final byte FLOW_ID_BILL_PAYMENT_MOBILE_POSTPAID = 7;
    public static final byte FLOW_ID_BILL_PAYMENT_MOBILE_PREPAID = 6;
    public static final byte FLOW_ID_HRA_TO_WALLET = 34;
    public static final byte FLOW_ID_MINI_LOAD = 22;
    public static final byte FLOW_ID_BILL_PAYMENT_TELEPHONE = 9;
    public static final byte FLOW_ID_RETAIL_PAYMENT = 10;
    public static final byte FLOW_ID_ADVANCE_LOAN = 49;
    public static final byte FLOW_ID_TRANSFER_IN = 11;
    public static final byte FLOW_ID_TRANSFER_OUT = 12;
    public static final byte FLOW_ID_DEBIT_CARD_ISSUANCE = 27;
    public static final byte FLOW_ID_ATM_PIN_CHANGE = 107;
    public static final byte FLOW_ID_ATM_PIN_GENERATE = 108;
    public static final byte FLOW_ID_DEBIT_CARD_BLOCK = 109;
    public static final byte FLOW_ID_DEBIT_CARD_ACTIVATION = 110;
    public static final byte FLOW_ID_SI_SCHEDULE =40;
    public static final byte FLOW_ID_E_TICKETING = 21;
    public static final byte FLOW_ID_BUS_TICKETING = 55;
    public static final byte FLOW_ID_BOOKME_AIR = 56;
    public static final byte FLOW_ID_BOOKME_CINEMA = 57;
    public static final byte FLOW_ID_BOOKME_HOTEL = 58;
    public static final byte FLOW_ID_BOOKME_EVENT = 59;
    public static final byte FLOW_ID_DEBIT_CARD_ACTIVATION_AND_PIN = 60;
    public static final byte FLOW_ID_DEBIT_CARD_PIN_CHANGE = 61;
    public static final byte FLOW_ID_DEBIT_CARD_TEM_PER_BLOCK = 62;

    // Attribute cont
    public static final String ATTR_CNIC = "CNIC";
    public static final String ATTR_CMSISDN = "CMSISDN";
    public static final String ATTR_CREG_STATE = "CREG_STATE";
    public static final String ATTR_CREG_STATE_ID = "CREG_STATE_ID";
    public static final String ATTR_CNAME = "CNAME";
    public static final String ATTR_PARAM_CODE = "code";
    public static final String ATTR_PARAM_NAME = "name";
    public static final String ATTR_BANK_ID = "id";
    public static final String ATTR_BANK_NICK = "bname";
    public static final String ATTR_MIN_LENGTH = "MIN_LENGTH";
    public static final String ATTR_MAX_LENGTH = "MAX_LENGTH";
    public static final String ATTR_PARAM_IMD = "IMD";
    public static final String ATTR_NAME = "NAME";
    public static final String ATTR_CUST_ACC_TYPE = "CUST_ACC_TYPE";
    public static final String ATTR_DEPOSIT_AMT = "DEPOSIT_AMT";
    public static final String ATTR_IS_CNIC_SEEN = "IS_CNIC_SEEN";
    public static final String ATTR_CNIC_EXP = "CNIC_EXP";
    public static final String ATTR_CREG_COMMENT = "CREG_COMMENT";
    public static final String ATTR_CDOB = "CDOB";
    public static final String ATTR_CNIC_EXP_FORMATED = "CNIC_EXP_formated";
    public static final String ATTR_CDOB_FORMATED = "CDOB_FORMATED";
    public static final String ATTR_DTID = "DTID";
    public static final String ATTR_MOBILE_NETWORK = "MOBILE_NETWORK";

    public static final String TAG_MEMBERS_BANKS = "memberbanks";
    public static final String TAG_BANK = "bank";
    public static final String KEY_LIST_MEMBERS_BANKS = "memberbanks";
    /**
     * App Internal Flow IDs
     */
    public static final byte FLOW_ID_BALANCE_INQUIRY = 101;
    public static final byte FLOW_ID_CHANGE_PIN = 102;
    public static final byte FLOW_ID_MINI_STATEMENT = 103;
    public static final byte FLOW_ID_CASH_WITHDRAWAL = 2;
    public static final byte FLOW_ID_MY_LIMITS = 112;
    public static final byte FLOW_ID_GENERATE_MPIN = 111;
    public static final byte FLOW_ID_HRA_ACCOUNT = 113;
    public static final byte FLOW_ID_L0_TO_L1_ACCOUNT = 114;
    public static final byte FLOW_ID_FORGOT_MPIN = 115;
    public static final byte FLOW_ID_CHECK_IBAN = 116;
    public static final byte FLOW_ID_DEBIT_CARD = 27;
    public static final int DEBIT_CARD_CATEGORY_PERSONALIZED = 1;
    public static final int DEBIT_CARD_CATEGORY_NONPERSONALIZED = 2;
    public static final String DEBIT_CARD_CATEGORY_NAME_PERSONALIZED = "Personalized";
    public static final String DEBIT_CARD_CATEGORY_NAME_NONPERSONALIZED = "Non personalized";
    public static final int DEBIT_CARD_ACTIVATION_CUSTOMER = 1;
    public static final int DEBIT_CARD_ACTIVATION_OWN = 0;
    public static final String CATEGORY_ID_MONEY_TRANSFER = "3";
    public static final String CATEGORY_ID_PAY_BILL = "4";
    public static final String CATEGORY_ID_MY_ACCOUNT = "33";
    public static final String CATEGORY_ID_PAYMENTS = "34";
    public static final String CATEGORY_ID_CARD_SERVICES = "44";
    public static final String CATEGORY_ID_ZONG_SERVICES = "25";
    public static final String CATEGORY_ID_COLLECTION = "55";
    /**
     * App Internal Product IDs
     */

    public static final String PRODUCT_ID_BLB_BALANCE_INQUIRY = "10025";
    public static final String PRODUCT_ID_HRA_BALANCE_INQUIRY = "10026";
    public static final String PRODUCT_ID_CHANGE_LOGIN_PASSWORD = "10027";
    public static final String PRODUCT_ID_CHANGE_MPIN = "10028";
    public static final String PRODUCT_ID_BLB_MINI_STATEMENT = "10029";
    public static final String PRODUCT_ID_HRA_MINI_STATEMENT = "10036";
    public static final String PRODUCT_ID_HRA_ACCOUNT = "10037";
    public static final String PRODUCT_ID_L0_TO_L1_ACCOUNT = "10038";
    public static final String PRODUCT_ID_FORGOT_MPIN = "10039";
    public static final String PRODUCT_ID_CHECK_IBAN = "10040";


    public static final String PRODUCT_ID_CASH_WITHDRAWAL = "10030";
    public static final String PRODUCT_ID_MY_LIMITS = "10031";
    public static final String PRODUCT_ID_ATM_PIN_CHANGE = "10032";
    public static final String PRODUCT_ID_ATM_PIN_GENERATE = "10033";
    public static final String PRODUCT_ID_DEBIT_CARD_BLOCK = "10034";
    public static final String PRODUCT_ID_DEBIT_CARD_ACTIVATION = "10035";
    /**
     * App Internal Category IDs
     */


    public static final String REGISTRATION_STATE_VERIFIED = "3";
    public static final String ENCRYPTION_TYPE = "1";
    public static final String DEVICE_TYPE_ID = "5";
    public static final String DEVICE_NOT_ROOTED = "0";
    public static String SECRET_KEY = "";
    public static int isLogin = 0;
    public static int isOTP = 0;
    public static int otpCount = 1;

    public static class AgentAccountType {
        public static final int CONSUMER = 2;
        public static final int NORMAL = 3;
        public static final int HANDLER = 12;
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

    public class IntentKeys {

        public static final String HRA_MODEL = "hraModel";
        public static final String HRA = "hra";
        public static final String FORGOT_MPIN = "FORGOT_MPIN";
        public static final String PURPOSE_CODE = "PURPOSE_CODE";
        public static final String OPEN_FRONT_CAMERA = "isFrontCamera";

        public static final String DISCREPANT = "discrepant";
        public static final String MESSAGE = "mesage";
        public static final String OPEN_ACCOUNT_IMAGES_LIST = "OPEN_ACCOUNT_IMAGES_LIST";
        public static final String TRXID = "TRXID";
        public static final String CAME_FROM_BVS = "CAME_FROM_BVS";
        public static final String ATTR_TITLE = "title";
        public static final String TITLE = "TITLE";
        public static final String VALID_INDEXES = "validIndexes";
        public static final String FINGERS = "fingers";
        public static final String VALID_FINGER_INDEXES = "validFingerIndexes";

        public static final String IDENTIFIER = "identifier";
        public static final String FINGER = "finger";
        public static final String MOBILE_NO = "MOBILE_NO";
        public static final String MOBILE_ID = "MOBILE_ID";
        public static final String CNIC = "CNIC";

        public static final String LOCATOR_TYPE = "LOCATOR_TYPE";
        public static final String CURRENT_LATITUDE = "CURRENT_LATITUDE";
        public static final String CURRENT_LONGITUDE = "CURRENT_LONGITUDE";
        public static final String TARGET_LOCATION = "TARGET_LOCATION";
        public static final String WEB_URL = "WEB_URL";
        public static final String HEADING = "HEADING";
        public static final String ICON = "ICON";
        public static final String FLOW_ID = "FLOW_ID";
        public static final String SUB_FLOW_ID = "SUB_FLOW_ID";
        public static final String TRANSACTION_INFO_MODEL = "TRANSACTION_INFO_MODEL";
        public static final String TRANSACTION_MODEL = "TRANSACTION_MODEL";
        public static final String TRANSACTION_AMOUNT = "TRANSACTION_AMOUNT";
        public static final String PIN_CHANGE_TYPE = "PIN_CHANGE_TYPE";
        public static final String NEXTPIN_CHANGE_TYPE = "NEXTPIN_CHANGE_TYPE";
        public static final String PAYMENT_TYPE = "PAYMENT_TYPE";
        public static final String CATEGORY_MODEL = "CATEGORY_MODEL";
        public static final String ADVANCE_LOAN_MODEL = "ADVANCE_LOAN_MODEL";
        public static final String PRODUCT_MODEL = "PRODUCT_MODEL";
        public static final String SCHEDULE_MODEL = "SCHEDULE_MODEL";
        public static final String SHOW_REGISTRATION_POPUP = "SHOW_REGISTRATION_POPUP";
        public static final String BOTH_PIN_CHANGE = "BOTH_PIN_CHANGE";
        public static final String MPIN_FROM_LOGIN = "MPIN_FROM_LOGIN";
        public static final String PRODUCT_ID = "PRODUCT_ID";
        public static final String PRODUCT_NAME = "PRODUCT_NAME";
        public static final String BANK_NAME = "BANK_NAME";
        public static final String BANK_IMD = "BANK_IMD";
        public static final String TRANSACTION_PURPOSE_NAME = "TRANSACTION_PURPOSE_NAME";


        public static final String BOOKME_ORDER_REF_ID = "BOOKME_ORDER_REF_ID";
        public static final String BOOKME_SERVICE_PROVIDER = "BOOKME_SERVICE_PROVIDER";
        public static final String BOOKME_DATE = "BOOKME_DATE";
        public static final String BOOKME_TIME = "BOOKME_TIME";
        public static final String BOOKME_TYPE = "BOOKME_TYPE";
        public static final String BOOKME_EVENT_VENUE = "BOOKME_EVENT_VENUE";
        public static final String BOOKME_APR_AMOUNT = "BOOKME_APR_AMOUNT";
        public static final String BOOKME_BASE_FARE = "BOOKME_BASE_FARE";
        public static final String BOOKME_DIS_AMOUNT = "BOOKME_DIS_AMOUNT";
        public static final String BOOKME_TAX = "BOOKME_TAX";
        public static final String BOOKME_FEE = "BOOKME_FEE";
        public static final String BOOKME_HOTEL_NAME = "BOOKME_HOTEL_NAME";
        public static final String BOOKME_CUSTOMER_PHONE = "BOOKME_CUSTOMER_PHONE";
        public static final String BOOKME_CUSTOMER_EMAIL = "BOOKME_CUSTOMER_EMAIL";
        public static final String BOOKME_CUSTOMER_NAME = "BOOKME_CUSTOMER_NAME";
        public static final String BOOKME_CUSTOMER_CNIC = "BOOKME_CUSTOMER_CNIC";
        public static final String BOOKME_TICKETING_TYPE = "BOOKME_TICKETING_TYPE";
        public static final String BOOKME_FLIGHT_TO = "BOOKME_FLIGHT_TO";
        public static final String BOOKME_EVENT_NAME = "BOOKME_EVENT_NAME";

        public static final String FLAG_TRANSITION = "FLAG_TRANSITION";
        public static final String FLAG_LOGIN = "FLAG_LOGIN";
        public static final String FLAG_OPEN_ACCOUNT = "FLAG_OPEN_ACCOUNT";
        public static final String SESSION_OUT = "SESSION_OUT";

        public static final String TERMS = "TERMS";
        public static final String RCMOB = "RCMOB";
        public static final String AMOB = "AMOB";
        public static final String OTP = "OTP";
        public static final String DEBIT_CARD_ACTIVATION_TYPE = "DEBIT_CARD_ACTIVATION_TYPE";

        public static final String MENU_ITEM_POS = "MENU_ITEM_POS";
        public static final String MENU_ITEM_POS_PARENT = "MENU_ITEM_POS_PARENT";
        public static final String LIST_CATEGORIES = "LIST_CATEGORIES";


        public static final String MERCHANT_ID = "MERCHANT_ID";
        public static final String QR_AMOUNT = "QR_AMOUNT";
        public static final String QR_STRING = "QR_STRING";
        public static final String QR_TIP = "QR_TIP";
        public static final String QR_AMOUNT_F = "QR_AMOUNT_F";
        public static final String MERCHANT_NAME = "MERCHANT_NAME";

        public static final String MERCHANT_NAME_F = "MERCHANT_NAME_F";

        public static final String MERCHANT_ID_N = "MERCHANT_ID_N";
        public static final String QR_AMOUNT_N = "QR_AMOUNT_N";
        public static final String QR_AMOUNT_F_N = "QR_AMOUNT_F_N";
        public static final String QR_FEE = "QR_FEE";
        public static final String MERCHANT_NAME_N = "MERCHANT_NAME_N";

        public static final String MERCHANT_LACTION = "MERCHANT_LACTION";
    }

    public class ErrorCodes {
        public static final String INTERNAL_SSL = "5911";
        public static final String INTERNAL_SSL_HAND_SHAKE = "5912";
        public static final String OTP_EXPIRE = "9019";
        public static final String LOGIN_FAILED_LEVEL2 = "9009";
        public static final String ACCOUNT_BLOCKED = "9004";
        public static final String SESSION_EXPIRED = "9007";
        public static final String WALK_IN_CUSTOMER = "8076";
        public static final String CREDENTIALS_BLOCKED = "9010";
        public static final String INTERNAL = "5000";
        public static final String EXCEPTION_ERROR = "00000";

        public static final String DEVICE_UPDATE_OTP_VERIFICATION = "9025";
        public static final String DEVICE_UPDATE_OTP_RESEND = "9026";
        public static final String DEVICE_UPDATE_OTP_REGENERATION = "9027";
        public static final String DEVICE_UPDATE_OTP_ERROR = "9028";
        public static final String DEVICE_UPDATE_OTP_EXPIRE_ERROR = "9029";

        public static final String LOGIN_OTP_VERIFICATION = "9096";
        public static final String LOGIN_OTP_ERROR = "9097";
        public static final String LOGIN_OTP_EXPIRE_ERROR = "9098";


    }

    public class VersionUsageLevel {
        public static final int NORMAL = 0;
        public static final int CRITICAL = 1;
        public static final int BLOCK = 2;
        public static final int OBSOLETE = 3;
    }

    public class FieldLength {
        public static final int MPIN = 4;
    }

    public class ProductIds {
        public static final int BLB2CORE = 50053;
        public static final int BLB2IBFT = 10245160;
        public static final int BULK2CNIC = 2510801;
    }

    public static final int REQUEST_CODE_TERMS_CONDITIONS = 120;
    public static final int REQUEST_CODE_VERIFY_NADRA = 121;
    public static final int REQUEST_CODE_OTC = 123;
    public static final int REQUEST_CODE_FINGER_SCAN = 124;
    public static final int REQUEST_CODE_ACCOUNT_OPEN_OTP = 125;

    public static final int RESULT_CODE_SUCCESS_NADRA = 504;
    public static final int RESULT_CODE_FAILED_NADRA_FINGER_INDEXES = 501;
    public static final int RESULT_CODE_FAILED_NADRA_OTHER_ERROR = 503;
    public static final int RESULT_CODE_FINGERS_EMPTY = 509;

    public class Messages {
        public static final String APPLICATION_SSL_EXPIRE = "Please download the new Application. The older version is now obsolete and will not work.";
        public static final String EXCEPTION_HTTP_UNAVAILABLE = "Service unavailable due to technical difficulties. Please try again or contact service provider.";
        public static final String EXCEPTION_SERVICE_UNAVAILABLE = "Service unavailable due to technical difficulties. Please try again or contact service provider.";
        public static final String EXCEPTION_TIME_OUT = "This seems to be taking longer than usual. Please try again later.";
        public static final String EXCEPTION_INVALID_RESPONSE = "We are unable to process your request at the moment. Please try again later.";
        public static final String EXCEPTION = "Connection with the server can not be established at this time. Please try again or contact your service provider.";
        public static final String EXCEPTION_GENERIC = "Your session has expired. Please try again.";
        public static final String EXCEPTION_SSL_HANDSHAKE = "No trusted certificate found. Please download the new Application.";


        public static final String INTERNET_CONNECTION_PROBLEM = "There is no or poor internet connection. Please connect to stable internet connection and try again.";
        public static final String INVALID_TRANSACTION_ID = "Length should be of 14 digits.";
        public static final String INVALID_MOBILE_NUMBER_FORMAT = "Mobile No. must start with 03.";
        public static final String ERROR_MULTIPLE = "You have entered invalid amount.";
        public static final String ERROR_AMOUNT_INVALID = "You have entered invalid amount.";
        public static final String ERROR_CUSTOMER_AGE = "Customer age cannot be less than 18 year.";
        public static final String ERROR_INVALID_CUSTOMER_DOB = "Please enter valid date of birth.";
        public static final String ERROR_INVALID_CARD_EXPEIRY = "Please enter valid card expiry date.";
        public static final String SESSION_EXPIRED = "Application Session has expired. Please Re-Login.";
        public static final String ALERT_HEADING = "Alert Notification";
        public static final String ALERT_ERROR = "Verification Error";
        public static final String ERROR_INVALID_ISCNIC_SEEN = "CNIC is not verified.";
        public static final String CANCEL_TRANSACTION = "Do you want to cancel the transaction?";
        public static final String AMOUNT_MAX_LIMIT = "Amount should be between 10 and 9,999,999.";
        public static final String SESSSION_OUT_ERROR = "Unstable Internet Connection. Press Exit to quit.";
        public static final String DEBIT_BLOCK_TYPE_ERROR = "Select the block type of the debit card.";


    }

    public class MastercardQR {
        public static final String QR_PAN_TAG = "04";
        public static final String QR_MERCHANT_ID_TAG = "03";
        public static final String QR_AMOUNT_TAG = "54";
        public static final String MERCHANT_NAME = "59";
        public static final String MERCHANT_LACTION = "60";
        public static final String TRANSACTION_TIP = "55";
    }

    public class MastercardOldQR {
        public static final String QR_PAN_TAG = "00";
        public static final String QR_MERCHANT_ID_TAG = "A5";
        public static final String QR_AMOUNT_TAG = "A1";
    }
}