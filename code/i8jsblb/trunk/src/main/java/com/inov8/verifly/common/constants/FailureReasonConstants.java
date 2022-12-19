package com.inov8.verifly.common.constants;

public interface FailureReasonConstants
{
  //public static long PIN_NOT_MATCHED            = 1; //USE MESSAGE ID 18 INSTEAD OF THIS AS QA DOCUMENTS SHOW THAT CASE 1 AND 18 HAVE SAME MESSAGE SEE BUG # 923
  public static long NO_ACTIVE_RECORD           = 2;
  public static long PINS_ARE_SAME              = 3;
  public static long PIN_LENGTH_IS_SHORTER      = 4;
  public static long PIN_LENGTH_IS_GREATER      = 5;
  public static long RECORD_ALREADY_EXISTS      = 6;
  public static long CAN_NOT_DEACTIVATE_RECORD  = 7;
  public static long RECORD_ALREADY_DEACTIVATED = 8;
  public static long RECORD_ALREADY_ACTIVATED   = 9;
  public static long ERROR_GENERATING_RANDOM_NUMBER = 10;
  public static long ERROR_APPLYING_HASHING_ALGORITHM = 11;
  public static long ERROR_SAVING_OR_UPDATING_RECORD = 12;
  public static long RECORD_NOT_FOUND = 13;
  public static long INCORRECT_CARD_EXPIRED_DATE = 14;
  public static long RECORD_IS_INACTIVE = 15;
  public static long CARD_NO_ALREADY_EXIST = 16;
  public static long ACCOUNT_NO_ALREADY_EXIST = 17;
  public static long INVALID_BANK_PIN_PLEASE_ENTER_VALID_BANK_PIN = 18;
  public static long INCORRECT_DATA_FOR_CARD_NO = 19;
  public static long INCORRECT_DATA_FOR_ACCOUNT_NO = 20;
  public static long CANNOT_MODIFY_PAYMENT_MODE = 21;
  public static long CANNOT_MODIFY_CARD_TYPE = 22;
  public static long INCORRECT_PAYMENT_MODE = 23;
  public static long INCORRECT_CARD_TYPE = 24;
  public static long INSUFFICIENT_DATA = 25;
  public static long INCORRECT_FIELD_LENGTH = 26;
  public static long INCORRECT_CUSTOMER_MOBILE_NO = 27;
  public static long NEW_AND_CONFIRM_NEW_PINS_ARE_NOT_MATCHED = 28;
  public static long ONETIMEPIN_AND_PIN_ARE_NOT_MATCHED = 29 ;
  public static long OT_PIN_DOES_NOT_EXISTS = 30 ;
  public static long OT_PIN_EXPIRED = 31 ;  
  public static long OT_RETRY_COUNT_EXCEEDED = 33 ;
  public static long RECORD_MARK_AS_DELETED = 32 ; 
  public static long FACILITY_NOT_AVAILABLE = 34 ;
}
