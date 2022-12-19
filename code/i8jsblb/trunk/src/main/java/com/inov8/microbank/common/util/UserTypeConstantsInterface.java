package com.inov8.microbank.common.util;

public interface UserTypeConstantsInterface
{
  public static final Long DISTRIBUTOR = 4L;
  public static final Long RETAILER = 3L;
  public static final Long INOV8 = 1L;
  public static final Long CUSTOMER = 2L;
  public static final Long SUPPLIER = 5L;
  public static final Long BANK = 6L;
  public static final Long MNO = 7L;
  public static final Long WALKIN_CUSTOMER = 9L;
  public static final Long SCHEDULER = 11L;
  public static final Long HANDLER = 12L;
  
	/**
	 * This constant value distinguishes between a product supplier from payment service
	 */
	public static final Long PRODUCT_SUPPLIER = 99L;
	
	/*maxim transaction Amount limit for direct agent retailer contact*/
	public static final Double MAX_DIRECT_RETAILER_TX_AMOUNT = 15000D;

}
