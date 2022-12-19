package com.inov8.ola.util;


public interface 	ReasonConstants
{
	public static final long SETTLEMENT = -1L;
	public static final long PRODUCT_SALE = 1L;
	public static final long DISTRIBUTOR_CREDIT_RECHARGE = 2L;
	public static final long NATIONAL_DISTRIBUTOR_CREDIT_RECHARGE = 3L;
	public static final long RETAILER_CREDIT_RECHARGE = 4L;
	public static final long REVERSAL = 5L;
	public static final long DIST_DIST_CREDIT_TRSFR = 6L;
	public static final long DIST_RET_CREDIT_TRSFR = 7L;
	public static final long RET_RET_CREDIT_TRSFR = 8L;
	
	public static final long INOV8_COMMISSION = 10L;
	
	/*added by mudassir: new entries for bill payment and salary disbursement*/
	public static final long SALARY_DISBURSEMENT = 11L;
	public static final long BILL_PAYMENT = 12L;
	public static final long CUSTOMER_ACCOUNT_TO_CASH = 13L;
	public static final long CUSTOMER_CASH_TO_CASH = 14L;
	public static final long ROLLBACK_WALKIN_CUSTOMER = 15L;
//	public static final long CASH_DEPOSIT = 16L;
	
	public static final long RETAIL_PAYMENT = 16L;
	public static final long DONATION_PAYMENT = 17L;
	public static final long REVERSE_BILL_PAYMENT = 18L;
	public static final long BULK_PAYMENT = 19L;
	
	
	public static final long CASH_WITHDRAWAL = 30L;
	public static final long TRANSFER_IN = 20L;
	public static final long TRANSFER_OUT = 21L;
	public static final long CUSTOMER_RETAIL_PAYMENT = 22L;
	public static final long CUSTOMER_ZONG_TOP_UP = 23L;
	public static final long CASH_DEPOSIT = 24L;
	public static final long ACCOUNT_TO_ACCOUNT = 25L;
	public static final long ROLLBACK_LEDGER = 26L;
	
	
	public static final long BB_TO_CORE_AC = 31L;
	public static final long CNIC_TO_CORE_AC = 32L;
	public static final long CNIC_TO_BB_AC = 33L;
	public static final long AGENT_RETAIL_PAYMENT = 34L;
	public static final long CUSTOMER_PENDING_TRX_SETTLEMENT = 35L;
	public static final long INITIAL_DEPOSIT = 36L;
	public static final long MANUAL_ADJUSTMENT = 37L;
	public static final long IBFT = 38L;
	public static final long MANNUAL_REVERSAL = 39L;

	public static final long TELLER_CASH_IN = 40L;
	public static final long TELLER_CASH_OUT = 41L;
	public static final long RECEIVE_CASH = 43L;
	Long FUND_CUSTOMER_BB_CORE_AC=106L;

}
