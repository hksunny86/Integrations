package com.inov8.microbank.common.util;

public interface IntgTransactionTypeConstantsInterface {
	
	public static final Long BILL_PAYMENT_PHOENIX = 1L;
	public static final Long TITLE_FETCH_PHOENIX = 2L;
	public static final Long CREDIT_ACCOUNT_ADVICE_CORE = 3L;
	public static final Long DEBIT_ACCOUNT_ADVICE_CORE = 20L;
	public static final Long BILL_INQUIRY_PHOENIX = 4L;
	public static final Long CHECK_BALANCE_PHOENIX = 5L;
	
	public static final Long ROLL_BACK_OLA = 6L;
	public static final Long LEDGER_OLA = 7L;
	public static final Long DEBIT_OLA = 8L;
	public static final Long CREDIT_OLA = 9L;
	public static final Long CHECK_BALANCE_OLA = 10L;
	
	public static final Long VERIFY_PIN_VERIFLY = 11L;
	public static final Long VERIFY_ONE_TIME_PIN_VERIFLY =12L;
	public static final Long VERIFY_CREDENTIALS_VERIFLY = 13L;
	public static final Long BILL_INQUIRY_NADRA = 14L;
	public static final Long BILL_PAYMENT_NADRA = 15L;
	public static final Long[] CSR_VIEW_TRANS_TYPE_ID = {1L, 4L};

	public static final Long DEBIT_CREDIT_OLA = 16L;
	
	public static final Long CREDIT_CARD_BILL_INQUIRY_PHOENIX = 17L;
	public static final Long CREDIT_CARD_BILL_PAYMENT_PHOENIX = 18L;
	public static final Long TRANSFER_IN_CORE = 19L;
	public static final Long TRANSFER_OUT_CORE = 21L;
	public static final Long TRANSFER_IN_CORE_REVERSAL = 22L;
	public static final Long TELLER_CASH_IN_CORE_REVERSAL = 23L;

	public static final Long DEBIT_CARD = 25L;

	public static final Long AGENT_IBFT = 26L;
	public static final Long VC_TRANSFER = 27L;
}
