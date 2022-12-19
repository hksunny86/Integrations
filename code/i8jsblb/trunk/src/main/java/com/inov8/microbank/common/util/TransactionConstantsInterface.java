package com.inov8.microbank.common.util;

/**
 *
 * <p>Title: </p>
 *
 * <p>Contains constants to be used in the transaction.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */
public interface TransactionConstantsInterface
{

  public static String OPERATOR_TO_DISTRIBUTOR = "operatortodistributor";
  public static String DISTRIBUTOR_TO_DISTRIBUTOR = "distributortodistributor";
  public static String DISTRIBUTOR_TO_RETAILER = "distributortoretailer";
  public static String RETAILER_TO_RETAILER = "retailertoretailer";
  public static String RETAILER_TO_CUSTOMER = "retailertocustomer";
  public static String CUSTOMER_TO_I8 = "customertoi8";
  public static String SERVICE_TYPE_VARIABLE = "variable";
  public static String SERVICE_TYPE_DISCRETE = "discrete";
  
  public final static String GENERIC_ERROR_MESSAGE = "genericErrorMessage";
  public final static String REQUEST_PROCESSING_FAILED = "requestProcessingError";
  public final static String SUCCESS = "S";
  public final static String FAILURE = "F";

  public final static Long FIRST_LEG_CATEGORY_ID = 1L;
  public final static Long UNCLAIMED_CATEGORY_ID = 2L;
  public final static Long SECOND_LEG_CATEGORY_ID = 3L;
  public final static Long RECIEVE_CASH_CATEGORY_ID =4L;
  public final static Long RECIEVED_IN_ACCOUNT_CATEGORY_ID =5L;
  public final static Long REDEMPTION_REQUEST_CATEGORY_ID =6L;
  public final static Long SENDER_REDEEM_CATEGORY_ID =7L;
  public final static Long DEFAULT_AGNET_ACC_CATEGORY_ID =8L;
  public final static Long REVERSAL_CATEGORY_ID =9L;
  public final static Long MANUAL_ADJUSTMENT_CATEGORY_ID =10L;
  public final static Long AUTO_REVERSAL_CATEGORY_ID =11L;

}
