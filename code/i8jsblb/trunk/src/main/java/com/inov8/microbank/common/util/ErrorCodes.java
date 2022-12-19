package com.inov8.microbank.common.util;

public class ErrorCodes
{
	public static final Long VALIDATION_ERROR = 9000L;
	public static final Long COMMAND_EXECUTION_ERROR = 9001L;

	public static final Long BAD_REQUEST = 9002L;
	public static final Long INVALID_USER = 9003L;
	public static final Long INVALID_USER_ACCOUNT = 9004L;
	public static final Long INVALID_INPUT = 9005L;
	public static final Long UNKNOWN_ERROR = 9006L;
	public static final Long INVALID_LOGIN = 9007L;
	public static final Long FILE_UPLOAD_ERROR = 9011L;
	public static final Long ACCOUNT_EXPIRED_ERROR = 9012L;	 
	public static final Long ACCOUNT_DISABLED_ERROR = 9013L;		 
	public static final Long PIN_CHANGE_ERROR = 9014L;	
	public static final Long CREDENTIALS_EXPIRED = 9015L;	
	public static final Long ACCOUNT_BLOCKED = 9016L;	
	public static final Long ACCOUNT_LOCKED= 9017L;	
	public static final Long FILE_NOT_FOUND = 9018L;
	
	public static final Long OTP_EXPIRED = 9019L;
	public static final Long OTP_INVALID = 9023L;
	public static final Long TERMINATE_EXECUTION_FLOW = 9999L;
	public static final Long MPIN_EXPIRED = 9020L;
	public static final Long UN_AUTHORIZED_ACCESS = 9021L;
	public static final Long APP_VERSION_OBSOLETE = 9022L;
	public static final Long DEVICE_LOCKED = 9025L;
	public static final Long DEVICE_OTP_RESENT = 9026L;
	public static final Long DEVICE_OTP_REGENERATED = 9027L;
	public static final Long DEVICE_OTP_INVALID = 9028L;
	public static final Long DEVICE_OTP_EXPIRED = 9029L;
	public static final Long DEVICE_INVALID_INPUT = 9030L;

	//***********************************************
	public static final Long CATALOG_ERROR = 9031L;
	//***********************************************
	
	
	/**
	 * Send this message to client (Microbank) to notify that the 
	 * current connection creation mechanism is not supported 
	 * hence change the connection scheme for future requests  
	 */
	public static final Long INVALID_MSG = 9008L;

	/**
	 * Send this to client (Microbank) when using Blacklist or Obsolete 
	 * Applicaion Version.
	 */
		
	public static final Long APP_VERSION_BLACKLIST_OBSOLETE = 9009L;
	
	public static final String ERROR_MESSAGE_CODE = "-1";
	
	public static final Long INVALID_PIN = 9010L;
	public static final Long ERROR_INVALID_PIN = 9011L;

	//Added By Sheheryaar
	//CheckBalance Command If HRA not exists
	public static final Long NO_HRA_EXISTS = 9033L;

	public static final Long FINGER_NOT_MATCHED = 121L;
	public static final Long INVALID_FINGER_INDEX = 122L;
	public static final Long NADRA_FINGER_EXAUST_ERROR = 118L;
	public static final Long INVALID_NIFQ_MINUTIAE_VALUE = 135L;
	public static final Long UPGRADE_LEVEL_0_TO_LEVEL_1 = 9041L;
	public static final Long HRA_CW_LEVEL_1_NOT_FOUND = 9042L;
	public static final Long MEMBER_BANK_NOT_FOUND = 9045L;

	public static final Long INSUFFICIENT_BALANCE_FOR_DEBIT_CARD_ISSUANCE = 9044L;
	public static final Long INSUFFICIENT_BALANCE_FOR_FEE_PAYMENT_API = 9047L;
	public static final Long DEBIT_CARD_CLS_PENDING_STATE = 9046L;

	public static final Long THIRD_PARTY_TRANSACTION_TIME_OUT = 9050L;
	public static final Long TRANSACTION_TIME_OUT = 9051L;
	public static final Long TRXN_AMOUNT_GREATER_THAN_LIMIT= 9052L;
	public static final Long INVALID_ACCOUNT_NUMBER= 9053L;
	public static final Long AGENT_BVS_REQUIRED= 1009L;

	//added by Abubakar
	public static final Long DEBIT_BLOCKED = 9095L;
	public static final Long AGELIMIT_ACCOUNT_BLOCK=9096L;
	public static final Long CNIC_EXPIRE_ACCOUNT_BLOCK=9096L;
	public static final Long DEBIT_CARD_REISSUANCE_EXISTS=9097L;
	public static final Long EMAIL_ADDRESS_ALREADY_EXISTS=9098L;
	public static final Long ACCOUNT_OPENING_FAILED=9099L;



}
