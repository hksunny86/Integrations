package com.inov8.microbank.common.util;


public interface ReasonConstants
{
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
	public static final long RETAIL_PAYMENT = 16L;
	public static final long DONATION_PAYMENT = 17L;
	public static final long REVERSE_BILL_PAYMENT = 18L;
	
//	added for Account To Cash 
	public static final long CUSTOMER_ACCOUNT_TO_CASH = 13L;
	public static final long CUSTOMER_CASH_TO_CASH = 14L;
	public static final long BULK_PAYMENT = 19L;
	
	public static final long TRANSFER_IN = 20L;
	public static final long TRANSFER_OUT = 21L;
	public static final long CUSTOMER_RETAIL_PAYMENT = 22L;
	public static final long CUSTOMER_ZONG_TOP_UP = 23L;
	public static final long CASH_DEPOSIT = 24L;
	public static final long ACCOUNT_TO_ACCOUNT = 25L;
	public static final long CREDIT_CARD_PAYMENT = 27L;
	public static final long CASH_WITHDRAWAL = 30L;
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
	public static final long RECEIVE_CASH = 44L;
	public static final long SENDER_REDEEM = 45L;
	
	Long WHT_DEDUCTION_ON_WITHDRAWAL = 46L;
	Long WHT_DEDUCTION_ON_TRANSFER = 47L;

	Long CUSTOMER_CASH_WITHDRAWAL = 51L;
	Long CUSTOMER_BB_TO_CORE_AC = 53L;
	Long FUND_CUSTOMER_BB_CORE_AC=106L;
	Long VALENCIA_COLLECTION_PAYMENT=107L;

	public static final long WEB_SERVICE_PAYMENT = 48L;
	public static final long AGENT_CASH_DEPOSIT = 80L;
	public static final long VIRTUAL_CARD_PAYMENT = 49L;
	public static final long ACCOUNT_TO_ACCOUNT_CI = 50L;
	public static final long ACCOUNT_TO_CASH_CI = 52L;
    public static final long COLLECTION_PAYMENT = 54L;
	public static final long THIRDPARTY_CASH_OUT_PAYMENT = 56L;

	long DEBIT_CARD_CASH_WITHDRAWAL_ON_US = 58L;
	long DEBIT_CARD_CASH_WITHDRAWAL_OFF_US = 60L;
	long POS_DEBIT_CARD_CASH_WITHDRAWAL = 62L;
	long POS_DEBIT_CARD_REFUND = 63L;
	long HRA_TO_WALLET_TRANSACTION = 68L;

	long ET_COLLECTION = 70L;
	long KP_CHALLAN_COLLECTION = 71L;
	long LICENSE_FEE_COLLECTION = 72L;

	long AGENT_ET_COLLECTION = 73L;
	long AGENT_KP_CHALLAN_COLLECTION = 74L;
	long AGENT_VALENCIA_COLLECTION = 108L;

	long AGENT_LICENSE_FEE_COLLECTION = 75L;
	long AGENT_BALUCHSITAN_ET_COLLECTION = 97L;
	long AGENT_E_LEARNING_MANAGEMENT_SYSTEM = 99L;

	public static final long DEBIT_CARD_ISSUANCE_FEE = 65L;
	public static final long DEBIT_CARD_RE_ISSUANCE_FEE = 66L;
	public static final long DEBIT_CARD_ANNUAL_FEE = 65L;

	long AGENT_EXCISE_AND_TAXATION_PAYMENT = 78L;

	long AIR_TIME_TOP_UP = 85L;

	long BOP_CASH_OUT = 86L;

	long THIRD_PARTY_ACCOUNT_OPENING = 87L;
	long PROOF_OF_LIFE = 95L;

	long AGENT_IBFT = 89L;
	long BOP_CARD_ISSUANCE_REISSUANCE = 91L;
	long ADVANCE_SALARY_LOAN = 93L;
	long DEBIT_PAYMENT_API=98L;
	long LOAN_PAYMENT=100l;
	long FEE_PAYMENT=102l;
	long VC_TRANSFER=105L;
}
