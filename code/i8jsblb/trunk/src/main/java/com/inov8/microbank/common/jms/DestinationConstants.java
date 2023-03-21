package com.inov8.microbank.common.jms;

public interface DestinationConstants
{
	public static final String OUTGOING_SMS_DESTINATION = "outgoingSmsQueue";
	public static final String PHOENIX_BILL_PAYMENT_DESTINATION = "phoenixBillPaymentQueue";
	public static final String EMAIL_DESTINATION = "emailServiceQueue";
	public static final String DELAYED_SMS_DESTINATION = "delayedSmsQueue";
	public static final String ALERT_NOVA_SMS_DESTINATION = "alertNovaSmsQueue";

	public static final String OLA_DEBIT_DESTINATION = "olaDebitQueue";
	public static final String OLA_COMMISSION_DESTINATION = "olaCommissionQueue";
	public static final String BULK_CUSTOMER_DESTINATION = "bulkCustomerQueue";
	public static final String AGENT_DESTINATION = "bulkAgentQueue";
	public static final String FRANCHISE_DESTINATION = "franchiseQueue";
	public static final String BULK_WALK_IN_ACCOUNTS_DESTINATION = "bulkWalkInAccountsQueue";
	public static final String CREDIT_ACCOUNT_DESTINATION = "creditAccountQueue";
	public static final String IVR_AUTHENTICATION_DESTINATION = "ivrAuthenticationQueue";
	public static final String IBFT_DESTINATION = "ibftIncomingQueue";
	public static final String WALLET_TO_CORE_DESTINATION = "walletToCoreIncomingQueue";
	public static final String CREDIT_PAYMENT_DESTINATION = "creditPaymentQueue";
	public static final String DEDIT_PAYMENT_DESTINATION = "debitPaymentQueue";

	public static final String CORE_ADVICE_DESTINATION = "coreAdviceQueue";
	public static final String THIRD_PARTY_CASH_OUT_ADVICE_DESTINATION = "thirdPartyCashOutQueue";
	String BULK_MANUAL_ADJUSTMENT_DESTINATION = "bulkManualAdjustmentQueue";

	String REVERSAL_ADVICE_QUEUE = "reversalReqAdviceQueue";
	String BULK_REVERSAL_DESTINATION ="bulkReversalQueue" ;
}
