package com.inov8.microbank.common.util;

/**
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public interface SwitchConstants
{

  public static final String DEBIT_INVOICE = "Debit" ;
  public static final String CREDIT_INVOICE = "Credit" ;
  
  public static final Long CORE_BANKING_SWITCH = 6L;
  public static final Long OLA_SWITCH = 8L ;

  
  public final static String BALANCE_INQUIRY_TRANS = "31" ;
  public final static String PURCHASE_TRANS = "00" ;
  public final static String BILL_PAYMENT_TRANS = "80"  ;
  
  public final static String SUCCESS_CODE = "00"  ;
  public final static String INSUFFICIENT_FUNDS = "04"  ;
  public final static String LIMIT_EXCEEDED = "06"  ;
  public final static String ACCOUNT_INACTIVE = "07"  ;
  public final static String INVALID_ACCOUNT = "26"  ;
  public final static String TIMED_OUT = "02";
  public final static String UNKNOWN_ERROR = "03";
  public final static String DATABASE_ERROR = "09";
  public final static String INVALID_TRANSACTION_RECORD = "11";
  public final static String CARD_EXPIRED = "16";
  public final static String HOST_REJECT = "19";
  public final static String INVALID_HOST_MODE = "30";
  public final static String HSM_TIMED_OUT = "33";
  public final static String PIN_CHANGE_REJECT = "42";
  public final static String TRANSACTION_REJECTED = "44";
  public final static String BILL_PAST_DUE_DATE = "63";
  public final static String BILL_MARKED_VOID = "66";
  public final static String INVALID_PIN = "15";
  public final static String PIN_MISMATCHED = "43";
  public final static String INVALID_CARD_RECORD = "08";
  public final static String WARM_CARD = "40";
  public final static String HOT_CARD = "41";
  public final static String EXCEEDED_CYCLE_TRANSACTION_LIMIT = "50";
  public final static String BILL_NOT_FOUND = "60";
  public final static String COMPANY_NOT_FOUND = "10";
  public final static String HOST_COMMS_DOWN = "20";
  public final static String HOST_NOT_PROCESSING = "21";
  public final static String INVALID_ACCOUNT_STATUS = "23";
  public final static String PERMISSION_DENIED = "25";
  public final static String INTERNAL_ERROR = "27";
  public final static String MESSAGE_FORMAT_ERROR = "28";
  public final static String SUCCESS = "S";
  public final static String FAILURE = "F";



  
  public static final String KEY_SWITCHWRAPPER = "switchWrrapper" ;
  public final static String KEY_SWITCH_UTILITY_MAPPING_BEAN = "switchUtilityMappingFacade";


}
