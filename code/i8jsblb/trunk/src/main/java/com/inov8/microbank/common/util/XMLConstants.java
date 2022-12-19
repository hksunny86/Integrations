package com.inov8.microbank.common.util;

public class XMLConstants 
{
	public static final String TAG_MSG = "msg";
    public static final String TAG_REQ = "req";
    public static final String TAG_PARAMS = "params";
    public static final String TRANS_PURPOSE_REASON = "paymentreasons";
    public static final String MEMBER_BANKS = "memberbanks";
    public static final String THIRD_PARTY_SEGMENTS = "segments";
    public static final String CARD_TYPE = "cardTypes";

    public static final String TAG_PARAM = "param";
    public static final String TAG_CAT = "cat";
    public static final String TAG_PRODS = "prds";
    public static final String ATTR_CAT_ICON = "icon";
    public static final String TAG_PROD = "prd";
    public static final String ATTR_MULTIPLE = "multiple";
    public static final String ATTR_NAME = "name";
    public static final String TAG_SUPP = "supp";
    public static final String ATTR_CAT_ID = "cid";
    public static final String ATTR_PARENT_CAT_ID = "pcid";
    public static final String ATTR_URL="url";
    public static final String TAG_SERVS = "srvs";
    public static final String TAG_SERV = "srv";
    public static final String TAG_ACCS = "accs";
    public static final String ATTR_FLOW_ID = "fid";
    public static final String ATTR_CONSUMER_LABEL = "label";
    public static final String ATTR_MIN_AMOUNT = "minamt";
    public static final String ATTR_MIN_AMOUNT_FORMATTED = "minamtf";
    public static final String ATTR_MAX_AMOUNT = "maxamt";
    public static final String ATTR_MAX_AMOUNT_FORMATTED = "maxamtf";
    public static final String ATTR_AMOUNT_REQUIRED = "amtrequired";
    public static final String ATTR_DO_VALIDATE = "dovalidate";
    public static final String ATTR_CNSMR_INPUT_TYPE = "type";
    public static final String TAG_ACC = "acc";
    public static final String TAG_MESGS = "mesgs";
    public static final String TAG_MESG = "mesg";
    public static final String TAG_ERRORS = "errors";
    public static final String TAG_ERROR = "error";
    public static final String TAG_TRANS = "trans";
    public static final String TAG_TRN = "trn";
    public static final String TAG_FAVS = "favs";
    public static final String TAG_FAV_NUM = "fav";
    public static final String TAG_STMTS = "STMTS";
    public static final String ATTR_ID = "id";
    public static final String TAG_BANK_ACCOUNTS = "baccs";
    public static final String TAG_BANK_ACCOUNT = "bacc";
    public static final String TAG_CATEGORIES = "categories";
    public static final String TAG_CATEGORY = "category";
 

    // Attributes
    public static final String ATTR_TRACK_ID = "trackid";
    public static final String ATTR_MSG_ID = "id";
	public static final String	ATTR_REQ_TIME = "reqTime";
    public static final String ATTR_REQ_ID = "id";
    public static final String ATTR_PROD_ID = "id";
    public static final String ATTR_PROD_TYPE = "type";
    public static final String ATTR_PROD_AMOUNT = "amt";
    public static final String ATTR_SERV_ID = "id";
    public static final String ATTR_GENI_DEV_FLOW_ID = "dfid";
    public static final String ATTR_GENI_BILL_SERVICE_LABEL = "label";
    public static final String ATTR_ACC_ID = "id";
    public static final String ATTR_ALLPAY_ID = "apId";
    public static final String ATTR_PARAM_NAME = "name";
    public static final String ATTR_PARAM_BANK_NAME = "name";
    public static final String ATTR_SUPP_NAME = "name";
    public static final String ATTR_CAT_VER = "version";
    public static final String ATTR_ACC_NICK = "nick";
    public static final String ATTR_ACC_IS_DEF = "isDef";
    public static final String ATTR_ACC_PN_CH_REQ = "pinChReq";
    public static final String ATTR_IS_PRODUCT = "isProduct";
    public static final String ATTR_ACC_MOD = "mod";
    public static final String ATTR_ACC_PGP_KEY = "key";
    public static final String ATTR_ACC_P2P_KEY = "p2p";
    public static final String ATTR_LEVEL = "level";
    public static final String ATTR_CODE = "code";
    public static final String ATTR_TRN_MOB_NO = "mNo";
    public static final String ATTR_TRN_PRODUCT = "prod";
    public static final String ATTR_TRN_SUPPLIER = "supp";
    public static final String ATTR_TRN_DATE = "date";
    public static final String ATTR_TRN_DATEF = "datef";
    public static final String ATTR_TRN_TIMEF = "timef";
    public static final String ATTR_FORMATED_AMOUNT = "amtf";
    public static final String ATTR_AMOUNT = "amt";
    public static final String ATTR_FORMATTED_DISCOUNT_AMOUNT = "damtf";
    public static final String ATTR_DISCOUNT_AMOUNT = "damt";
    public static final String ATTR_TRN_TYPE = "type";
    public static final String ATTR_TRN_HELPLINE = "helpLine";
    public static final String ATTR_FAV_NUM = "num";
    public static final String ATTR_FAV_NUM_TYPE = "type";
    public static final String ATTR_FAV_NUM_NAME = "name";
    public static final String ATTR_PAYMENT_MODE = "pmode";
    public static final String ATTR_BANK_RESPONSE_CODE = "bcode";
    public static final String ATTR_ACC_CVV = "cvv";
    public static final String ATTR_ACC_TPIN = "tpin";
    public static final String ATTR_ACC_MPIN = "mpin";
    public static final String ATTR_PIN = "pin";
    public static final String ATTR_NEW_PIN = "npin";
    public static final String ATTR_CONFIRM_PIN = "cpin";
    public static final String ATTR_TRACKING_ID = "trckid";
    
    public static final String ATTR_ACC_NO = "accNo";
    public static final String ATTR_ACC_TYPE = "accType";
    public static final String ATTR_REG_STATE = "registrationState";
    public static final String ATTR_ACC_TITLE = "accountTitle";
    public static final String ATTR_ACC_CURRENCY = "accCur";
    public static final String ATTR_ACC_STATUS = "accSts";
    public static final String ATTR_IS_BANK = "isBank";
    public static final String ATTR_IS_BANK_PIN_REQ = "isBPinReq";
    
    // added during Integration 
    public static final String TAG_BANK = "bank";
    public static final String ATTR_PIN_LEVEL = "pinLevel";
    public static final String TAG_BANKS = "banks";
    public static final String ATTR_BANK_ID = "id";
    

    public static final String TRANSACTION_ID = "trid";
    public static final String ATTR_TRN_PRODUCT_ID = "prodid";
    public static final String ATTR_COMMISSION = "cmsn";
    public static final String EXCLUSIVE_CHARGES = "excrhge";
    public static final String INCLUSIVE_CHARGES = "incrhge";   
    

    // About Tag
    public static final String TAG_SYMBOL_OPEN = "<";
    public static final String TAG_SYMBOL_CLOSE = ">";
    public static final String TAG_SYMBOL_SLASH_CLOSE = "/>";
    public static final String TAG_SYMBOL_SLASH = "/";
    public static final String TAG_SYMBOL_SPACE = " ";
    public static final String TAG_SYMBOL_QUOTE = "\"";
    public static final String TAG_SYMBOL_EQUAL = "=";
    public static final String TAG_SYMBOL_OPEN_SLASH = "</";
    
    //Attribute Values
    public static final String ATTR_LEVEL_ONE = "1";
    public static final String ATTR_LEVEL_TWO = "2";
    public static final String ATTR_LEVEL_THREE = "3";

    // App Version Usage Level
    public static final String TAG_APP_LEVEL_NORMAL = "0";
    public static final String TAG_APP_LEVEL_CRITICAL = "1";
    public static final String TAG_APP_LEVEL_BLOCK = "2";
    public static final String TAG_APP_LEVEL_Obsolete = "3";
            
    //Messages
    public static final String LOGIN_SUCCESS_MSG = "You Are Logged In Successfully";
    public static final String LOGOUT_SUCCESS_MSG = "You are logged out successfully.";
    public static final String CREDIT_RECHARGE_NAME = "i8 Credit Recharge";
    public static final String CREDIT_TRANSFER_NAME = "i8 Credit Transfer";
    public static final String AGENT_TO_AGENT_TRANSFER_NAME = "Agent to Agent Transfer";
    public static final String INOV8_SUPPLIER = "Inov8";
    
    //Tag Values
    public static final String TAG_USER_TYPE_CUSTOMER = "2";
    public static final String TAG_USER_TYPE_RETAILER = "3";
    public static final String TAG_USER_TYPE_DISTRIBUTOR = "4";
    public static final String TAG_USER_TYPE_HANDLER = "12";
    
    
    //JS Attributes
    public static final String ATTR_RAMOB = "RAMOB";
    public static final String ATTR_RACNIC = "RACNIC";
    public static final String ATTR_TRXID = "TRXID";
    public static final String ATTR_CMOB = "CMOB";
    public static final String ATTR_COREACID = "COREACID";
    public static final String ATTR_DATE = "DATE";
    public static final String ATTR_DATEF = "DATEF";
    public static final String ATTR_TIMEF = "TIMEF";
    public static final String ATTR_PROD = "PROD";
    public static final String ATTR_CAMT = "CAMT";
    public static final String ATTR_CAMTF = "CAMTF";
    public static final String ATTR_TPAM = "TPAM";
    public static final String ATTR_TPAMF = "TPAMF";
    public static final String ATTR_TAMT = "TAMT";
    public static final String ATTR_TAMTF = "TAMTF";
    public static final String ATTR_BALF = "BALF";
    public static final String ATTR_TXAM = "TXAM";
    public static final String ATTR_TXAMF = "TXAMF";
    public static final String ATTR_SWCNIC = "SWCNIC";
    public static final String ATTR_RWCNIC = "RWCNIC";
    public static final String ATTR_RCMOB = "RCMOB";
    public static final String ATTR_RCNAME = "RCNAME";
    public static final String ATTR_RWMOB = "RWMOB";
    public static final String ATTR_SWMOB = "SWMOB";
    public static final String ATTR_MNAME = "MNAME";

    public static final String ATTR_CNIC = "CNIC";
    public static final String ATTR_CNIC_EXPIRY = "CNIC_EXP";

    public static final String ATTR_CONSUMER_MIN_LENGTH = "consumerMinLength";
    public static final String ATTR_CONSUMER_MAX_LENGTH = "consumerMaxLength";
    public static final String ATTR_INQUIRY_REQUIRED = "inrequired";
    public static final String ATTR_PARTIAL_PAYMENT_REQUIRED = "ppallowed";
    
  //locator
    public static final String TAG_LOCATIONS = "locations";
    public static final String TAG_LOCATION = "location";


//    public static final String ATTR_ = "";
//    public static final String ATTR_ = "";
    
    public static final String ENCRYPTION_KEY = "<java.security.KeyPair>"+
    "<privateKey class=\"org.bouncycastle.jce.provider.JCERSAPrivateCrtKey\" serialization=\"custom\">"+
      "<org.bouncycastle.jce.provider.JCERSAPrivateKey>"+
        "<big-int>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</big-int>"+
        "<hashtable/>"+
        "<vector/>"+
        "<big-int>71384514143029669723551107026465795665969504932762260872273399830368082365906187467668214984012163977682506096679856595257545444615256538326521829064276727003290795381599896328700256905870663664029900889537120444479337324549948070945596338321920199665688684301471813370336304520400176690147029241627696317725</big-int>"+
      "</org.bouncycastle.jce.provider.JCERSAPrivateKey>"+
      "<org.bouncycastle.jce.provider.JCERSAPrivateCrtKey>"+
        "<default>"+
          "<crtCoefficient>9468751160817268233756547356236396730645841435629025868624379377327400407697667821610193297656794577908397509743039398698846728715921845285922203096011286</crtCoefficient>"+
          "<primeExponentP>751932392925928809988483746867994445246921297589617520951690698047420756934272663807759432585394432207472487665710007861518149884957277164669111569628369</primeExponentP>"+
          "<primeExponentQ>7559389705668927111340368365677981466638949292942806830040308281855946556779878342601248847929090177344446009362152155904337664239688317895289085592509239</primeExponentQ>"+
          "<primeP>11261287302373536659098551032561186462099515786135914869883673052544290253016779608768082708717777628789105215755858497536635052333282695050484361274847903</primeP>"+
          "<primeQ>10401203483874461508175636068556987495100183070092854049261020844995762622906990970986501348842744913031911110738119414698464812858777965818554319717752683</primeQ>"+
          "<publicExponent>65537</publicExponent>"+
        "</default>"+
      "</org.bouncycastle.jce.provider.JCERSAPrivateCrtKey>"+
    "</privateKey>"+
    "<publicKey class=\"org.bouncycastle.jce.provider.JCERSAPublicKey\">"+
      "<modulus>117130940722358865944076735715016871148960803304334901248996815419815052552875336322790410991392433604701394608500231884113911915168625416296669114728862690539451024021812353340986348428958506523689933432584403548435474622224828221548841371083486321081622447517054022904372023020885356296462823306439795173749</modulus>"+
      "<publicExponent>65537</publicExponent>"+
    "</publicKey>"+
  "</java.security.KeyPair>";

    public static final String OTP_ENCRYPTION_KEY = "682ede816988e58fb6d057d9d85605e0";
    
    public static final String MESSAGE_SENT_SUCCESS = "Customer request has been submitted successfully."; 
    public static final String MESSAGE_SENT_SUCCESS2 = "Customer has been created successfully.";
    public static final String AES_ENCRYPTION_KEY = "682ede816988e58fb6d057d9d85605e0";
    public static final String THIRD_PARTY_ENCRYPTION_KEY="65412399991212FF65412399991212FF";
    public static final String AES_HANDSHAKE_KEY    =     "f069b20cb3f7427d819b368afba72165";
    public static final String ENCRYPTION_TYPE_AES = "1";
    public static final String ENCRYPTION_TYPE_RSA = "0";

    public static final String ATTR_TYPE = "TYPE";
    public static final String TAG_ADS = "ads";
    public static final String TAG_AD = "ad";

    public static final String TAG_FAQS = "faqs";
    public static final String TAG_FAQ = "faq";
    public static final String ATTR_QUESTION = "question";
    
    public static final String ATTR_NAME_CAP_ID = "NAME";
    public static final String ATTR_DISTANCE_ID = "DISTANCE";
    public static final String ATTR_CONTACT_ID = "CONTACT";
    public static final String ATTR_ADDRESS_ID = "ADD";
    public static final String ATTR_LATITUDE_ID = "LATITUDE";
    public static final String ATTR_LONGITUDE_ID = "LONGITUDE";
    public static final String ATTR_IS_BVS_REQUIRED = "IS_BVS_REQUIRED";

    public static final String ATTR_NADRA_SESSION = "nadraSessionId";
    public static final String ATTR_PROD_DENOM = "prodDenom";
    public static final String ATTR_DENOM_FLAG = "denomFlag";
    public static final String ATTR_DENOM_STRING = "denomString";



}
