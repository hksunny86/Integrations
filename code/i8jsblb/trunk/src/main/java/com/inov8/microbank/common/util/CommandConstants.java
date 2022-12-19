package com.inov8.microbank.common.util;

public class CommandConstants
{
	public static final int DEFAULT_YEARS_TO_ADD = 100;
	public static final int PREPAID_YEARS_TO_ADD = 1;
	public static final int ADD_ONE_YEARS_IN_CURRENT_DATE = 1;
	public static final int ADD_TWO_YEARS_IN_CURRENT_DATE = 2;
	public static final String GREATER = "GREATER";
	public static final String EQUAL = "EQUAL";
	public static final String LESS = "LESS";
	
	public static final Long ALLPAY_TICKER_STRING_ID = 2L;

	public static final String AGENT_MATE = "1";
	public static final String CONSUMER_APP = "2";
	public static final String SCO_AGENT_MATE = "3";

	public static final String i8_CREDIT_RECHARGE = "i8 Credit Recharge";
	public static final String i8_CREDIT_TRANSFER = "i8 Credit Transfer";
	public static final String AGENT_TO_AGENT_TRANSFER_NAME = "Agent to Agent Transfer";
	
	//Velocity restriction related constants
	public static final String VELOCITY_PRODUCT_ID = "productId";
	public static final String VELOCITY_DEVICE_TYPE_ID = "deviceTypeId";
	public static final String VELOCITY_SEGMENT_ID = "segmentId";
	public static final String VELOCITY_DISTRIBUTOR_ID = "distributorId";
	public static final String VELOCITY_AGENT_TYPE = "agentType";
	public static final String VELOCITY_TRANSACTION_AMOUNT = "trxAmount";
	public static final String VELOCITY_ACCOUNT_TYPE = "accountType";
	public static final String VELOCITY_CUSTOMER_ID = "customerId";
	public static final String VELOCITY_RETAILER_ID = "retailerId";

	
	//USSD Commands
	
	public static final String CASH_OUT_COMMAND_CODE = "CO";
	public static final String CASH_IN_COMMAND_CODE = "CI";
	public static final String PAY_CASH_COMMAND_CODE = "PC";
	public static final String P2P_COMMAND_CODE = "P2P";
	public static final String BILL_PAY_COMMAND_CODE = "BP";
	public static final String BILL_PAY_INFO_COMMAND_CODE = "BI";
	public static final String BILL_RETRIEVAL_COMMAND_CODE = "BR";
	
	public static final String BALANCE_CHECK_AGENT = "BCA";
	public static final String UTILITY_BILL_PAYMENT_AGENT = "UBPA";
	public static final String CHANGE_PIN_AGENT = "CPA";
	public static final String CHANGE_LOGIN_PIN_AGENT = "LCPA";
	public static final String CUSTOMER_AC_TO_CASH = "CAC";
	public static final String CUSTOMER_RETAIL_PAYMENT = "CRP";
	public static final String CUSTOMER_BALANCE_TOP_UP = "CBTU";
	public static final String AGENT_CASH_TRANSFER = "ACT";
	public static final String AGENT_TO_AGENT_TRANSFER = "ATAT";
	public static final String RSO_AGENT_TO_AGENT_TRANSFER = "RATAT";
	public static final String BB_MINI_STATEMENT="MINISTMT";
	public static final String BB_CASH_OUT_INFO="COUTINFO";
	public static final String BB_CASH_OUT="COUT";
	public static final String BB_CD_INFO="CDINFO";
	public static final String BB_CD="CD";
	public static final String CHALLAN_INFO = "CHALLANINFO";
	public static final String CHALLAN_DEPOSIT = "CHALLANDP";
	public static final String TRANSFER_IN_INFO = "TININFO";
	public static final String TRANSFER_IN_CMD = "TIN";
	public static final String TRANSFER_OUT_INFO = "TOUTINFO";
	public static final String TRANSFER_OUT_CMD = "TOUT";
    public static final String AG_TRAN_INFO  = "AGTRANINFO";
    public static final String AG_TRAN_CMD = "AGTRAN";
	public static final String WALLET_TO_CNIC_INFO = "WALLET2CNICINFO";
	public static final String WALLET_TO_CNIC_CMD = "WALLET2CNIC";
	public static final String WALLET2_WALLET_INFO = "W2WINFO";
	public static final String WALLET2_WALLET_CMD = "W2W";
    public static final String BB2_CROE_INFO = "BB2COREINFO";
    public static final String BB2_CORE_CMD = "BB2CORE";
	public static final String CUSTOMER_CHALLAN_INFO = "CCHALLANINFO";
	public static final String CUSTOMER_CHALLAN_DEPOSIT = "CCHALLANDP";

	public static final String CUSTOMER_BILL_PAYMENT_INFO = "CBI";
	public static final String CUSTOMER_BILL_PAYMENT_CMD = "CBP";
	
	public static final String BULK_PAYMENT_LEG_TWO="BPLT";
	public final static String GENERAL_ERROR_MSG = "Service unavailable due to technical difficulties, please try again or contact service provider." ;
}
