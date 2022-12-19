package com.inov8.microbank.common.util;

public interface OLATransactionReasonsInterface
{
	
	public static final long PRODUCT_SALE = 1L;
	public static final long DISTRIBUTOR_CREDIT_RECHARGE = 2L;
	public static final long NATIONAL_DISTRIBUTOR_CREDIT_RECHARGE = 3L;
	public static final long RETAILER_CREDIT_RECHARGE = 4L;
	public static final long REVERSAL = 5L;
	public static final long DIST_DIST_CREDIT_TRSFR = 6L;
	public static final long DIST_RET_CREDIT_TRSFR = 7L;
	public static final long RET_RET_CREDIT_TRSFR = 8L;
	public static final long COMMISSION_TRANSFER = 9L;
	public static final long INOV8_COMMISSION_TRANSFER = 10L;
	public static final long ROLLBACK_WALKIN_CUSTOMER = 15L;
	public static final long MANNUAL_REVERSAL = 16L;

}
