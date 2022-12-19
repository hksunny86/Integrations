package com.inov8.microbank.server.dao.complaintsmodule;

public final class ComplaintsModuleConstants {

	public static final Long UTILITY_SP_TYPE = new Long(1);
	public static final Long UTILITY_SP_NAME = new Long(2);
	public static final Long UTILITY_TRX_ID = new Long(3);
	public static final Long UTILITY_PAYMENT_DATE = new Long(4);
	public static final Long UTILITY_AMOUNT = new Long(5);
	public static final Long UTILITY_CONSUMER_NO = new Long(6);

	public static final Long FT_TRANSACTION_ID = new Long(7);
	public static final Long FT_TRX_DATE = new Long(8);
	public static final Long FT_AMOUNT = new Long(9);
	public static final Long FT_SENDER_MSISDN = new Long(10);
	public static final Long FT_SENDER_CNIC = new Long(11);
	public static final Long FT_SENDER_ACC_NO = new Long(12);
	public static final Long FT_RECIPIENT_MSISDN = new Long(13);
	public static final Long FT_RECIPIENT_CNIC = new Long(14);
	public static final Long FT_RECIPIENT_ACC_NO = new Long(15);

	public static final Long AGENT_ID = new Long(16);
	public static final Long AGENT_LOCATION = new Long(17);
	public static final Long AGENT_SHOP_NAME = new Long(18);
	public static final Long AGENT_SHOP_ADDRESS = new Long(19);
	public static final Long AGENT_TRANSACTION_ID = new Long(20);
	public static final Long AGENT_AMOUNT = new Long(21);
	public static final Long SENDER_AGENT_MSISDN = new Long(34);
	public static final Long RECEIVER_AGENT_MSISDN = new Long(35);
	public static final Long AGENT_TRANSACTION_DATE = new Long(36);

	public static final Long TOPUP_TRX_ID = new Long(22);
	public static final Long TOPUP_MOBILE_NO = new Long(23);
	public static final Long TOPUP_DATE = new Long(24);
	public static final Long TOPUP_AMOUNT = new Long(25);

	public static final Long CB_TRANSACTION_ID = new Long(26);
	public static final Long CB_TRX_DATE = new Long(27);
	public static final Long CB_AMOUNT = new Long(28);
	public static final Long CB_SENDER_MSISDN = new Long(29);
	public static final Long CB_SENDER_CNIC = new Long(30);
	public static final Long CB_RECIPIENT_MSISDN = new Long(31);
	public static final Long CB_RECIPIENT_CNIC = new Long(32);

	public static final Long BB_ACCOUNT_DATE = new Long(33);
	public static final Long BB_CUSTOMER_MSISDN = new Long(37);
	public static final Long BB_TRANSACTION_ID = new Long(38);
	public static final Long BB_AMOUNT_TRANSFERRED = new Long(39);
	

	
	public static final String STATUS_ASSIGNED = "Assigned";
	public static final String STATUS_RESOLVED = "Resolved";
	public static final String STATUS_DECLINED = "Declined";

	public static final String ESC_STATUS_DEFAULT = "Default";
	public static final String ESC_STATUS_LEVEL_1 = "Level 1";
	public static final String ESC_STATUS_LEVEL_2 = "Level 2";
	public static final String ESC_STATUS_LEVEL_3 = "Level 3";
	public static final String ESC_STATUS_OVERDUE = "Overdue";

	public static final Long CATEGORY_UTILITY = new Long(1);
	public static final Long CATEGORY_FUND_TRANSFER = new Long(3);
	public static final Long CATEGORY_AGENT_COMPLAINT = new Long(5);
	public static final Long CATEGORY_REGENERATE_PIN = new Long(7);
	public static final Long CATEGORY_ACTIVATE = new Long(9);
	public static final Long CATEGORY_DEACTIVATE = new Long(12);
	public static final Long CATEGORY_RESEND_SMS = new Long(15);
	public static final Long CATEGORY_BB_ACCOUNT = new Long(17);
	public static final Long CATEGORY_TOP_UP = new Long(19);
	public static final Long CATEGORY_NETWORK = new Long(21);
	public static final Long CATEGORY_CHARGEBACK = new Long(23);
	public static final Long CATEGORY_OTHERS = new Long(25);
	public static final Long CATEGORY_BLOCK = new Long(27);
	public static final Long CATEGORY_UNBLOCK = new Long(29);

}
