//
//  Constants.swift
//  Timepey
//
//  Created by Adnan Ahmed on 03/06/2016.
//  Copyright © 2016 Inov8. All rights reserved.
//

import UIKit
import Foundation

struct Constants {
    
    struct ServerConfig {
        // TODO: Todo incase of Production Build
        //        let o = SSCheck(withSalt: [AppDelegate.self, NSObject.self, NSString.self])
        //        let value = o.reveal(key: Constants.ServerConfig.BASE_URL)
        //        let url = URL(string:value)
        
        // MARK: Check For DARK MODE
        // QA com.inov8.jsblbb
        // Live com.inov8.jsblconsumer
        
        //AKBL QA Server Device 11Pro
                static let SERVER_URL = "http://jsblb.inov8.com.pk/i8Microbank/allpay.me"
                static let BASE_URL = "http://jsblb.inov8.com.pk/i8Microbank"
        
        //Stagging
        //        static let SERVER_URL = "http://jsstaging.inov8.com.pk/i8Microbank/allpay.me"
        //        static let BASE_URL = "http://jsstaging.inov8.com.pk/i8Microbank"
        
        //UAT
//        static let SERVER_URL = "https://blbuat.jsbl.com/i8Microbank/allpay.me"
//        static let BASE_URL = "https://blbuat.jsbl.com/i8Microbank"
        
        //PROD
//        static let SERVER_URL = "https://blb.jsbl.com/i8Microbank/allpay.me"
//        static let BASE_URL = "https://blb.jsbl.com/i8Microbank"
//
        
        //Developer PC
//                static let SERVER_URL = "http://192.168.51.82:8080/i8Microbank/allpay.me"
//                static let BASE_URL = "http://192.168.51.82:8080/i8Microbank"
        
//                static let SERVER_URL = "http://192.168.50.184:8080/i8Microbank/allpay.me"
//                static let BASE_URL = "http://192.168.50.184:8080/i8Microbank"
        
        static let TERMS_AND_CONDITION_URL = "\(Constants.ServerConfig.BASE_URL)/terms-and-conditions.jsp"
    }
    
    struct AppConfig {
        
        static let IS_MOCK = 0 //0 = OFF & 1 = ON
        static let APP_VERSION = "2.0.9.17"
        
        static let APP_SESSION = 180
        static let HTTP_REQUEST_TIMEOUT: Double = 180
        
        static let ENCP_KEY : [UInt8]  = [119, 72, 66, 33, 1, 9, 93, 86, 87, 77, 93, 118, 54, 122, 90, 12, 7, 85, 16, 113, 69, 71, 32, 92, 8, 93, 82, 87, 68, 80, 43, 99]
        
        static let M_KEY : [UInt8] = [39, 64, 70, 125, 7, 94, 85, 4, 3, 71, 3, 121, 103, 125, 85, 14, 93, 82, 77, 35, 67, 70, 124, 4, 10, 7, 6, 86, 70, 84, 120, 102]
        static let DTID_KEY = "5"
        static let ENCT_KEY = "1"
        static let ACCTYPE = "1"
        static let USER_TYPE = "2"
        static let APP_ID = "2"
        static let OPERATOR_SYSTEM = "iOS"
        static let VENDOR = "Apple"
        static let CURRENCY = "PKR"
    }
    
    struct Validation {
        
        struct Login {
            static let PASSWORD_MIN = 4
            static let PASSWORD_MAX = 4
        }
        
        struct TextField {
            static let AMOUNT_MIN = 1
            static let AMOUNT_MAX = 9999999
            static let MIN_CONSUMER_LEN = 1
            static let MAX_CONSUMER_LEN = 30
        }
        
        static let  kNUMERIC = "1234567890"
        static let  kMAX_PHONE_CHARACTERS = 11
        static let OTP_LENGTH = 4
        static let REG_OTP_LENGTH = 5
        static let PASSWORD = 4
        static let ACCEPTABLE_CHARACTERS = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        static let ALPHABETS_CHARACTERS = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    }
    
    struct BOOKME {
        
        static let APIKEY = "1212789ed434433231a53639e5gg434"
    
        static let BUSES_URL = "https://bookme.pk/widgets/buses?data="
        static let AIRLINES_URL = "https://bookme.pk/widgets/flights?data="
        static let CINEMAS_URL = "https://bookme.pk/widgets/movies?data="
        static let EVENTS_URL = "https://bookme.pk/widgets/events?data="
        static let HOTELS_URL = "https://bookme.pk/widgets/hotels?data="
        
        static let RSA_PUBLIC_KEY = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAodEwuoIDkVPvQ6SOEAuS35Ris3evqFOD2YaGm4PHO2FQsCV55FOn4kynjAqoAmH9yMDreDiOPcLdloThiw75aoUKex3BP8P65VhljIGoiNyO10phV0vjEREz+wVwa3irXMSz68RQc7ReQmfA99O5HIxre9lel0peh/39O5d/kkStIoGYQ9vwfCbxgSWXFGcjiqizOYss76uazjATq2pcOsosrQKliBAkax9OiMzZe1D2KweBVn86VUTtFC1K3goafJpGufROGNx9umfh3BiknC9MZpWupyS4bTK1EWKCrzmz5h2glw6Dz1FA5K7kLHrQTtq6SBTVc9wjcvaBEdyYtr4TadcIdoPxVsA77zF3HhzJA7Jd1B4Zyly2Kco4iNleYoQuDQk3re6iJ425iqHGLfQU8/BF5qTLLeUjOhtwXlMj+Er2cbE+EU4NvInX60O9DjZYLkba7y4oWMedXYAVmE0z3frMChM4xazG3On2zI7L9hCROjtdlvtyJmzu4tDDI78mmsjAt38ujrUing6jUjSXksNA2/BB1xCM/gS3Ho4SfJNMJrrAN240Ew5pILG2oQPxly2d7YxXq+xo5X1fs2qU7wr3+7zTs96CJE0Ev+rnvzVUnleODucM7F5SZ8cb4kUrt3ocWMfMnHPTu/eKw2SbCxuf4a1nN3+cPvXMLvsCAwEAAQ=="
        
    }
    struct APP_VERSION_COMPATIBILITY {
        static let NORMAL = "0"
        static let CRITICAL = "1"
        static let OBSELETE = "2"
        static let BLOCK = "3"
    }
    
    struct ErrorCode {
        static let SESSION_EXPIRED = "9007"
        static let INVALID_OTP = "9023"
        static let PIN_RETRY_EXHAUSTED = "9010"
        static let APP_VERSION_COMPATIBILITY = "9009"
    }
    
    
    struct UI{
        struct Button {
            static let CORNER_RADIUS = CGFloat(5)
        }
        
        struct TextField {
            static let CORNER_RADIUS = CGFloat(2)
            static let BORDER_COLOR = UIColor(red:0.67, green:0.67, blue:0.67, alpha:1.0).cgColor
            static let BORDER_WIDTH = CGFloat(0.7)
            static let ACCEPTABLE_NUMERIC = "0123456789"
            static let ACCEPTABLE_ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            static let ACCEPTABLE_ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            static let ACCEPTABLE_ALPHABETS_CARD_NAME = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "
            static let ACCEPTABLE_ALPHANUMERIC_ADDRESS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 "
            static let ACCEPTABLE_ALL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_%+-.][@"
            static let ACCEPTABLE_ALLSP = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@!#$%^&*()-_./?<>,.|"
        }
        
    }
    
    struct QRCode_ID{
        
        static let QR_PAN_TAG_OLD                  =   "00"
        static let QR_MERCHANT_MOBILE_NO_OLD       =  "01"
        static let QR_MERCHANT_NAME_TAG_OLD        =   "0A"
        static let QR_MERCHANT_CATEGORY_TAG_OLD    =   "0B"
        static let QR_MERCHANT_CITY_TAG_OLD        =   "0C"
        static let QR_MERCHANT_COUNTRY_CODE_TAG_OLD  = "0D"
        static let QR_MERCHANT_CURRENCY_CODE_TAG_OLD = "0E"
        static let QR_MERCHANT_ID_TAG_OLD            = "A5"
        static let QR_AMOUNT_TAG_OLD                 = "A1"
        static let QR_CRC_VALUE_OLD                  = "CRC_VALUE"
        static let QR_PAN_TAG                        = "04"
        static let QR_MERCHANT_NAME_TAG              = "59"
        static let QR_MERCHANT_CATEGORY_TAG          = "52"
        static let QR_MERCHANT_CITY_TAG              = "60"
        static let QR_MERCHANT_COUNTRY_CODE_TAG      = "58"
        static let QR_MERCHANT_CURRENCY_CODE_TAG     = "53"
        static let QR_ADDITIONAL_DATA_TAG            = "62"
        static let QR_BILL_NO_TAG                    = "01"
        static let QR_REFERENCE_ID_TAG               = "05"
        static let QR_MERCHANT_ID_TAG                = "03"
        static let QR_PRODUCT_ID_TAG                 = "08"
        static let QR_PAYLOAD_FORMAT_TAG             = "00"
        static let QR_POI_TAG                        = "01"
        static let QR_AMOUNT_TAG                     = "54"
        static let QR_CRC_VALUE                      = "63"
        static let CP_NAME_KEY     =  "CPNAME"
        static let CP_ICON_KEY      = "CPICON"
        
    }
    
    static let ROOT_PARAM_KEY = "params"
    static let CHILD_PARAM_KEY = "param"
    
    struct CID {
        //MAIN CATEGORIES CIDs
        static let PAY_BILL = "4"
        static let MONEY_TRANSFER = "3"
        static let PAYMENTS = "34"
        static let ZONG_SERVICES = "25"
        static let MY_ACCOUNT = "7"
        static let MOBILELOAD = "103"
        static let LOANPAYMENTS = "281"
    }
    
    //Catelog related constants
    static let CATEGORIES_KEY = "categories"
    static let CATEGORY_KEY = "category"
    //Category attributes
    struct CatelogXML {
        static let CAT_ID = "cid"
        static let CAT_FLOW_ID = "fid"
        static let CAT_ICON = "icon"
        static let CAT_ISACTIVE_KEY = "isActive"
        static let CAT_ISPRODUCT_KEY = "isProduct"
        static let CAT_MSG = "msg"
        static let CAT_NAME = "name"
        static let CAT_SEQ_KEY = "seq"
        static let CAR_IMAGE_URL = "url"
    }
    
    struct ProductsXML {
        static let ID = "id"
        static let FID = "fid"
        static let LABEL = "label"
        static let TYPE = "type"
        static let AMT_REQUIRED = "amtrequired"
        static let DOVALIDATE = "dovalidate"
        static let MINAMT = "minamt"
        static let MINAMTF = "minamtf"
        static let MAXAMT = "maxamt"
        static let MAXAMTF = "maxamtf"
        static let MULTIPLE = "multiple"
        static let IMAGE_URL = "url"
    }
    
    struct CommandId {
        static let LOGIN = "33"
        static let OTLIP = "207"
        static let SIGNOUT = "128"
        static let BAL_INQUIRY = "6"
        static let MBANKS_LIST = "258"
        static let PAYMENT_PURPOSE = "247"
        static let CHANGE_MPIN = "5"
        static let CHANGE_LOGINPIN = "1"
        static let SET_MPIN = "233"
        static let MINI_STATEMENT = "66"
        static let CASH_WITHDRAWAL_INFO = "195"
        static let CASH_WITHDRAWAL_CHECKOUT = "186"
        static let MT_ACCTOCASH_INFO = "200"
        static let MT_ACCTOCASH_CHECKOUT = "201"
        static let MT_ACCTOACC_INFO = "198"
        static let MT_ACCTOACC_CHECKOUT = "199"
        static let MT_BBTOCORE_INFO = "202"
        static let MT_HRATOWALLET_INFO = "238"
        static let MT_HRATOWALLET_CHECKOUT = "240"
        static let MT_BBTOCORE_CHECKOUT = "203"
        static let RETAILPAYMENT_INFO = "208"
        static let RETAILPAYMENT_CHECKOUT = "209"
        static let BILLPAYMENT_INFO = "204"
        static let BILLPAYMENT_CHECKOUT = "205"
        static let VERIFY_PIN_FONEPAY = "182"
        static let VERIFY_PIN = "74"
        static let VERIFY_OTP = "178"
        static let BALANCE_TOPUP_INFO = "144"
        static let BALANCE_TOPUP_CHECKOUT = "145"
        static let CASH_WITHDRAWAL_LEG1_INFO = "195"
        static let SELF_REGISTRATION_VERIFICATION = "185"
        static let SELF_REGISTRATION_FINAL = "186"
        static let FAQ = "188"
        static let MY_LIMITS = "187"
        static let TRANSFER_IN_INFO = "103"
        static let TRANSFER_IN_CHECKOUT = "104"
        static let TRANSFER_OUT_INFO = "105"
        static let TRANSFER_OUT_CHECKOUT = "106"
        static let LOCATOR = "207"
        static let DEBIT_CARD_ISSUANCE_INFO = "231"
        static let DEBIT_CARD_ISSUANCE = "232"
        static let CARD_ACTIVATION_INFO = "151"
        static let CARD_ACTIVATION_CHECKOUT = "152"
        static let CARD_BLOCK_INFO = "155"
        static let CARD_BLOCK_CHECKOUT = "156"
        static let CARD_ATM_PIN_GEN_CHANAGE_INFO = "157"
        static let CARD_ATM_PIN_GEN_CHANAGE_CHECKOUT = "158"
        static let COLLECTION_PAYMENT_INFO = "210"
        static let COLLECTION_PAYMENT_CHECKOUT = "211"
        static let REGENRATE_MPIN = "216"
        static let FORGOT_PASSWORD_INFO = "217"
        static let FORGOT_PASSWORD_FINAL = "218"
        static let FORGOT_LOGIN_PIN = "275"
        static let FORGOT_MPIN = "277"
        static let HRA_ACCOUNT_OPENINGINFO = "224"
        static let HRA_ACCOUNT_OPENING = "226"
        static let LOAN_PAYMENTS_INFO = "266"
        static let LOAN_PAYMENTS_FINAL = "267"
        static let REGENERATE_OTP = "271"
        static let BOOKME_INFO = "272"
        static let BOOKME_FINAL = "273"
        static let SETMPINLATER = "287"
        
    }
    
    struct FID {
        static let ACCTOACC_FID = "3"
        static let ACCTOCASH_FID = "15"
        static let CHECKIBAN = "1001"
        static let BBTOCORE_FID = "4"
        static let BBToIBFT_FID = "44"
        static let ADVANCE_LOAN_SALARY = "49"
        static let RETAILPAYMENT_FID = "10"
        static let TRANSFER_HRA_TO_WALLET = "34"
        //My Account
        static let CHANGELOGINPIN = "500"
        static let CHANGEMPIN = "510"
        static let BALANCE_INQUERY_FID = "501"
        static let MINISTATEMENT_FID = "502"
        static let TRANSFER_IN_FID = "11"
        static let TRANSFER_OUT_FID = "12"
        
        static let MY_LIMITS_FID = "1000"
        static let HRA_ACCCOUNT_OPENING = "113"
        static let HRA_BALANCE_INQUIRY_FID = "101"
        static let HRA_MINI_STATEMENT_FID = "103"
        
        static let CASH_WITHDRAWAL = "505"
        //Bill Payment
        static let BILL_PAYMENT = "6"
        //static let MOBILE_PREPAID = "8"
        //static let GAS = "5"
        //static let TELEPHONE = "9"
        //static let ELECTRICTY = "6"
        //static let MOBILE_POSTPAID = "7"
        //static let WATER = "6"
        //static let CITY_GOVT = "6"
        //static let INTERNET_SERVICES = "9"
        static let ZONG_MINILOAD_FID = "30"
        static let LOAN_PAYMENTS_FID = "49"
        static let CASH_WITHDRAWAL_LEG1_FID = "2"
        static let DEBIT_CARD_ISSUANCE_INFO_UAT = "27"
        static let DEBIT_CARD_ISSUANCE_INFO_QA = "29"
        static let CHANGE_ATM_PIN = "506"
        static let GENRATE_ATM_PIN = "507"
        static let ATM_CARD_BLOCK = "508"
        static let ATM_CARD_ACTIVATION = "509"
        static let COLLECTION_PAYMENT = "25"
        static let CHALLAN_NUMBER = "21"
        static let BOOKME_BUSES = "55"
        static let BOOKME_EVENTS = "59"
        static let BOOKME_AIR = "56"
        static let BOOKME_CINEMA = "57"
        static let BOOKME_HOTEL = "58"
        static let REGENRATE_MPIN = "515"
        
        static let CONTACT_US = "540"
        static let FAQ = "541"
        static let TERMSANDCONDITIONS = "542"
        static let FORGOT_MPIN = "516"
        
    }
    
    struct ProductID {
        static let CHANGE_MPIN = "10028"
        static let CHANGE_LOGINPIN = "10027"
        //Bill payment
        static let POSTPAID_UFONE = "2510743"
        static let POSTPAID_ZONG = "2510745"
        static let POSTPAID_TELENOR = "2510749"
        static let POSTPAID_WARID = "2510753"
        static let POSTPAID_MOBILINK = "2510742"
        static let PREPAID_WARID = "2510715"
        static let PREPAID_MOBILINK = "2510719"
        static let PREPAID_TELENOR = "2510738"
        static let PREPAID_ZONG = "2510744"
        static let PREPAID_UFONE = "2510765"
        static let GAS_SSGC = "2510711"
        static let GAS_SNGPL = "2510710"
        static let PTCL_VFONE = "2510783"
        static let PTCL_LANDLINE = "2510708"
        static let WATEEN = "2510740"
        static let WITRIBE = "2510741"
        static let PTCL_EVO_PREPAID = "2510747"
        static let PTCL_EVO_POTPAID = "2510748"
        static let PTCL_DEFAULTER = "2510751"
        static let QUBEE_CONSUMER = "2510789"
        static let QUBEE_DISTRIBUTOR = "2510767"
        static let ELECTRICITY_LESCO = "2510705"
        static let ELECTRICITY_KESC = "2510704"
        static let ELECTRICITY_GEPCO = "2510700"
        static let ELECTRICITY_HESCO = "2510720"
        static let ELECTRICITY_MEPCO = "2510756"
        static let ELECTRICITY_QESCO = "2510762"
        static let ELECTRICITY_SEPCO = "2510766"
        static let ELECTRICITY_FESCO = "2510758"
        static let ELECTRICITY_PESCO = "2510760"
        static let WATER_MWASA = "2510774"
        static let WATER_FWASA = "2510778"
        static let WATER_GWASA = "2510780"
        static let WATER_RWASA = "2510772"
        static let WATER_LWASA = "2510770"
        static let CDGK = "2510784"
    }
    
    struct AppUsageLevel {
        static let NORMAL = "0"
        static let CRITICAL = "1"
        static let BLOCK = "2"
        static let OBSOLETE = "3"
    }
    
    struct Message {
        
        static let APP_DOWNLOAD_URL = "https://apps.apple.com/app/Js-bank-wallet/id1369107115"
        static let APPLICATION_VERSION_LEVEL_CRITICAL = "A new version of App is now available.For enhanced usability and feature please download new version from \(APP_DOWNLOAD_URL)"
        
        static let APPLICATION_VERSION_LEVEL_OBSOLETE = "Please download the new Customer Application "
            + "from " + APP_DOWNLOAD_URL + ". The older version is now obsolete and will not work."
        
        static let ALERT_NOTIFICATION_TITLE = "Alert Notification"
        static let ALERT_NOTIFICATION_TITLE_PGR_SUCCESS = "SET MPIN"
        static let ALERT_NOTIFICATION_TITLE_IPCR_SUCCESS = "Login PIN Changed"
        static let ALERT_NOTIFICATION_TITLE_IMPCR_SUCCESS = "MPIN Changed"
        static let ALERT_NOTIFICATION_TITLE_CARD_BLOCK_SUCCESS = "Card Blocked"
        static let ALERT_NOTIFICATION_TITLE_HRA_SUCCESS = "HRA Account Created Successfully"
        
        static let RETAIL_PAY_SUCCESS = "Retail Payment Successful"
        static let INVALID_OLD_PIN = "Invalid Old PIN"
        static let BILLPAYMENT_SUCCESS = "Bill Payment Successful"
        static let MINI_LOAD_SUCCESS = "Mini Load Successful"
        static let UPDATE_APP = "Please update your application"
        static let TRANSACTION_SUCCESS = "Transaction Successful"
        static let UNKNOWN_SERVER_ERROR = "We are unable to process your request at the moment. Please try again later."
        
        
        
        static let GENERAL_SERVER_ERROR = "Connection with the server cannot be established at this time. Please try again or contact your service provider."
        
        //static let EXCEPTION_GENERIC = "Your session has expired. Please try again."
        
        static let EXCEPTION_TIME_OUT = "This seems to be taking longer than usual. Please try again later."
        
        static let EXCEPTION_HTTP_UNAVAILABLE = "Service unavailable due to technical difficulties. Please try again or contact service provider."
        
        static let SESSION_TIMEOUT = "Session has expired. Please login again."
        
        static let CONNECTIVITY_ISSUE = "There is no or poor internet connection. Please connect to stable internet connection and try again."
        
        
        
        static let  kMESSAGE_EmptyMendatoryFieldMessage = "Please fill all mandatory fields."
        static let  kMESSAGE_EMPTY_DOB_ERROR =            "Please select Date of Birth"
        static let  kMESSAGE_EMPTY_CNIC_EXPIRY_ERROR = "Please select CNIC Expiry"
        static let  kMESSAGE_CUSTOMER_IMAGE_ERROR = "Please upload customer image"
        static let  kMESSAGE_CNIC_FRONT_IMAGE_ERROR = "Please upload cnic front image"
        static let  kMESSAGE_EXPIRE_FINANCIAL_PIN_MSG =   "Your Financial PIN has been expired with immediate effect. In order to perform any Financial Transaction you will have to generate the New Financial PIN."
        static let  kMESSAGE_InvalidDateRangeError =      "Invalid Date Range"
        static let kMESSAGE_EMPTY_FIELD_ERROR =          "Please fill all required fields"
        static let  kMESSAGE_INVALID_MOBILE_NO =          "Please enter valid mobile number."
        static let  kMESSAGE_cnicErrorMsg =               "You have entered invalid CNIC."
        
    }
    
    struct HTTPStatusCode {
        static let SUCCESS = 200
        static let NOT_FOUND = 404
        static let SERVICE_UNAVAILABLE = 503
        static let GATEWAY_TIMEOUT = 504
    }
    
    struct DEVICEFAMILY {
        
        static let   IS_IPAD = UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiom.pad
        static let IS_IPHONE = UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiom.phone
        
    }
    
    struct ScreenSize
    {
        static let SCREEN_WIDTH = UIScreen.main.bounds.size.width
        static let SCREEN_HEIGHT = UIScreen.main.bounds.size.height
        static let SCREEN_MAX_LENGTH = max(ScreenSize.SCREEN_WIDTH, ScreenSize.SCREEN_HEIGHT)
        static let SCREEN_MIN_LENGTH = min(ScreenSize.SCREEN_WIDTH, ScreenSize.SCREEN_HEIGHT)
    }
    
    
    
    struct DeviceType
    {
        static let IS_IPHONE_4_OR_LESS =  UIDevice.current.userInterfaceIdiom == .phone && ScreenSize.SCREEN_MAX_LENGTH < 568.0
        static let IS_IPHONE_5 = UIDevice.current.userInterfaceIdiom == .phone && ScreenSize.SCREEN_MAX_LENGTH == 568.0
        static let IS_IPHONE_6 = UIDevice.current.userInterfaceIdiom == .phone && ScreenSize.SCREEN_MAX_LENGTH == 667.0
        static let IS_IPHONE_6P = UIDevice.current.userInterfaceIdiom == .phone && ScreenSize.SCREEN_MAX_LENGTH == 736.0
        static let IS_IPHONE_X = UIDevice.current.userInterfaceIdiom == .phone && ScreenSize.SCREEN_MAX_LENGTH >= 812.0
        static let IS_IPHONE_XSMAX = UIDevice.current.userInterfaceIdiom == .phone && ScreenSize.SCREEN_MAX_LENGTH == 896.0
    }
    
}
