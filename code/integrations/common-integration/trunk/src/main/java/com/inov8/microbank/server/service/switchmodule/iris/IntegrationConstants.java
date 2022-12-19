/**
 * 
 */
package  com.inov8.microbank.server.service.switchmodule.iris;


/**
 * Project Name: 			Financial-Integration	
 * @author Imran Sarwar
 * Creation Date: 			Nov 13, 2007
 * Creation Time: 			3:46:02 PM
 * Description:				
 */
public class IntegrationConstants
{
	
	public static final String KEY_INTEGRATION_MESSAGE_VO = "KEY_INTEGRATION_MESSAGE_VO";
	public static final String KEY_TRANSACION_DETAIL = "KEY_TRANSACION_DETAIL";
	
	//Transaction State Constants
	
	public static final Long STARTED = 1L;
	public static final Long REQUEST_SENT = 2L;
	public static final Long COMPLETED = 3L;
	public static final Long REVERSAL_IN_PROGRESS = 4L;
	public static final Long RESPONSE_RECEIVED = 5L;
	public static final Long TIMED_OUT = 6L;
	public static final Long REJECTED = 7L;
	public static final Long RETRY_LIMIT_EXCEED = 8L;
	
	
	//Transaction Type Constants
	
	public final static Long BILL_PAYMENT = 1L ;
	public final static Long BALANCE_INQUIRY = 2L ;
	public final static Long CUSTOMER_ACCOUNT_RELATIONSHIP = 3L ;
	public final static Long PURCHASE = 4L ;
	public final static Long BALANCE_INQUIRY_FOR_CREDIT_CARD = 5L ;
	public final static Long BILL_PAYMENT_FOR_CREDIT_CARD = 6L ;
	public final static Long CREDIT_CARD_BILL_PAYMENT = 7L ;
	public final static Long ACTIVATE_ACCOUNT = 8L ;
	public final static Long VERIFY_ACCOUNT = 9L ;
	public final static Long MINI_STATEMENT = 10L ;
	public final static Long MINI_STATEMENT_FOR_CREDIT_CARD = 11L ;
	public final static Long GENERATE_MPIN = 12L ;
	public final static Long CHANGE_MPIN = 13L ;
	
	//Failure Reason Constants
	
	public static final Long INVALID_STAN_OR_RRN = 1L;
	public static final Long INVALID_DATE = 2L;
	public static final Long INVALID_EDI = 3L;
	public static final Long INVALID_RESPONSE_MESSAGE_TYPE = 4L;
	public static final Long INVALID_RESPONSE_CODE = 5L;
	public static final Long INVALID_RESPONSE_TRANSACTION_CODE = 6L;
	public static final Long INVALID_TIME = 7L;
	public static final Long INVALID_CNIC = 8L;
	
	//Response Message fields lengths 
	
	public static final int RESPONSE_MESSAGE_START_INDEX = 2;
	public static final int PROTOCOL_LENGTH=8;
	public static final int VERSION_LENGTH=8;
	public static final int TRANSMISSION_DATE_AND_TIME_LENGTH=14;
	public static final int RESERVED_HEADER_FIELD_LENGTH=10;
	public static final int MESSAGE_TYPE_LENGTH=4;
	public static final int CHANNEL_USER_IDENTIFICATION_LENGTH=30;
	public static final int TRANSACTION_CODE_LENGTH=4;
	public static final int TRANSACTION_LOCAL_DATE_LENGTH=8;
	public static final int TRANSACTION_LOCAL_TIME_LENGTH=6;
	public static final int SOURCE_CHANNEL_TYPE_LENGTH=4;
	public static final int DESTINATION_CHANNEL_TYPE_LENGTH=4;
	public static final int DELIVERY_CHANNEL_ID_LENGTH=8;
	public static final int DELIVERY_CHANNEL_NAME_AND_LOCATION_LENGTH=40;
	public static final int SYSTEM_TRACE_AUDIT_NUMBER_LENGTH=6;
	public static final int RETRIEVAL_REFERENCE_NUMBER_LENGTH=12;
	public static final int SECURE_DATA_TYPE_LENGTH=6;
	public static final int SECURE_VERIFICATION_DATA_LENGTH=50;
	public static final int AUTHORIZATION_RESPONSE_ID_LENGTH=6;
	public static final int RESPONSE_CODE_LENGTH=4;
	public static final int RESERVED_FIELD_LENGTH=100;
	
	
	public static final int CUSTOMER_ID_LENGTH=20;
	public static final int NUMBER_OF_ACCOUNTS_LENGTH=2;
	public static final int ACCOUNT_NUMBER_LENGTH=20;
	public static final int ACCOUNT_TYPE_LENGTH=2;
	public static final int ACCOUNT_CURRENCY_LENGTH=3;
	public static final int ACCOUNT_STATUS_LENGTH=2;
	
	
	public static final int UTILITY_COMPANY__ID_LANGTH=8;
	public static final int CONSUMER_LENGTH=24;
	public static final int AMOUNT_PAID=12;
	
	
	public static final int TRANSACTION_FEE_LENGTH=8;
    public static final int TRANSACTION_DESCRIPTION_LENGTH=40;

	
	public static final int AVAILABLE_BALANCE_LENGTH=12;
	public static final int ACTUAL_BALANCE_LENGTH=12;
	public static final int WITHDRAWL_LIMIT_LANGTH=12;
	public static final int AVAILABLE_WITHDRAWL_AMOUNT_LANGTH=12;
	public static final int DRAFT_LIMIT_LENGTH=12;
	public static final int LIMIT_EXPIRT_DATE_LENGTH=8;
	public static final int CURRENCY_NAME=30;
	public static final int CURRENCY_MNEMONIC=3;
	public static final int CURRENCY_DECIMAL_POINTS=1;
	
	public enum PhoenixConstants
	{
		MESSAGE_PROTOCOL("PHXGDCI"),
		VERSION("20"),
		DATE_AND_TIME_FORMAT("yyyyMMddHHmmss"),
		DATE_FORMAT("yyyyMMdd"),
		TIME_FORMAT("HHmmss"),
		PAYMENT_GATEWAY_CODE("1002"),
		
		DELIVERY_CHANNEL_ID("00000007"),
		// TODO Add delivery channel name
		DELIVERY_CHANNEL_NAME_AND_LOC("Innov8 Iserv"),
		
		//ADDED BY HAROON. FEB 25, 2008
		MESSAGE_HEADER_KEY("PHOENIX_MESSAGE_HEADER"),
		TRANSACTION_CODE_INDEX("82"),
		TRANSACTION_CODE_LENGTH("3"),
		MESSAGE_TYPE_INDEX("12"),
		MESSAGE_TYPE_LENGTH("4"),
		RESPONSE_CODE_INDEX("237"),
		RESPONSE_CODE_LENGTH("2");
		//end ADDED BY HAROON. FEB 25, 2008	
		
		private final String code;
		
		private PhoenixConstants(String code)
		{
			this.code = code;
		}
		
		public String getCode()
		{
			return this.code;
		}
	}
	
	public enum PhoenixMessageFieldsLength
	{
		START_INDEX(2),
		HEADER_LENGTH(239),
		MESSAGE_PROTOCOL_LENGTH(7),
		VERSION_LENGTH(2),
		FIELD_IN_ERROR_LENGTH(3),   
		MESSAGE_TYPE_LENGTH(4),
		TRANSMISSION_DATE_AND_TIME_LENGTH(14),
		DELIVERY_CHANNEL_TYPE_LENGTH(2),
		DELIVERY_CHANNEL_ID_LENGTH(20),               
		CUSTOMER_IDENTIFICATION_LENGTH(30),                              
		TRANSACTION_CODE_LENGTH(3),
		TRANSACTION_DATE_LENGTH(8),       
		TRANSACTION_TIME_LENGTH(6),      
		RETRIEVEL_REFERENCE_NUMBER_LENGTH(12),
		CUSTOMER_PIN_DATA_LENGTH(20),
		CHANNEL_SPECIFIC_DATA_FIELD_LENGTH(80),                                                                                
		CHANNEL_PRIVATE_DATA_LENGTH(20),                    
		AUTHORIZATION_RESPONSE_Id_LENGTH(6),      
		RESPONSE_CODE_LENGTH(2),
		
		ACCOUNT_NUMBER_LENGTH(20),
		ACCOUNT_TYPE_LENGTH(2),
		ACCOUNT_CURRENCY_LENGTH(3),
		ACCOUNT_DETAIL_LENGTH(25),
		CARD_NUMBER_LENGTH(20),
		TRANSACTION_AMOUNT_LENGTH(13),
		TRANSACTION_CURRENCY_LENGTH(3),
		UTILITY_COMPANY_ID_LENGTH(8),
		CONSUMER_NUMBER_LENGTH(24),
		AMOUNT_PAID_LENGTH(13),
		ACCOUNT_BANK_IMD_LENGTH(11),
		REQ_CHANNEL_STATUS_LENGTH(2),
		PAN_LENGTH(20),
		DC_TYPE_LENGTH(2);
		
		private final int code;
		
		private PhoenixMessageFieldsLength(int code)
		{
			this.code = code;
		}
		
		public int getCode()
		{
			return this.code;
		}
	}
	
	public enum PhoenixMessageTypes
	{
		TRANSACTION_REQUEST("0200"),
		TRANSACTION_RESPONSE("0210"),
		FILE_UPDATE_REQUEST("0302"),
		FILE_UPDATE_RESPONSE("0312"),
		TRANSACTION_REV_REQUEST("0420"),
		TRANSACTION_REV_RESPONSE("0430"),
		NETWORK_REQUEST("0800"),
		NETWORK_RESPONSE("0810");

		private final String code;
		
		private PhoenixMessageTypes(String code)
		{
			this.code = code;
		}
		
		public String getMessageTypeValue()
		{
			return this.code;
		}
	}
	
	public enum PhoenixTransactionCodes
	{
		LOGON("801"),
		LOGOUT("802"),
		ECHO("803"),
		
		ACC_REL_INQUIRY("318"),
		MPIN_GENERATE("301"),
		MPIN_CHANGE("302"),
		ACC_ACTIVATION("322"),
		ACC_VERIFICATION("018"),
		BILL_INQUIRY("010"),
		BILL_PAYMENT("011"),
		CREDIT_CARD_BILL_PAYMENT("106"),
		MINI_STATEMENT("004"),
		CREDIT_CARD_ACC_BALANCE("103"),
		BANK_ACC_BALANCE("003");
		
		private final String code;
		
		private PhoenixTransactionCodes(String code)
		{
			this.code = code;
		}
		
		public String getTransactionCodeValue()
		{
			return this.code;
		}
	}
	
	public enum PhoenixDeliveryChannelStatusCodes
	{
		OK("00"),
		WARM("01"),
		HOT("02"),		
		FIRST_TIME_LOGIN("03");
		
		private final String code;
		
		private PhoenixDeliveryChannelStatusCodes(String code)
		{
			this.code = code;
		}
		
		public String getDeliveryChannelStatusCodeValue()
		{
			return this.code;
		}
	}
	
	public enum PhoenixChannelTypes
	{
		ATM("00"),
		IVR("01"),
		INTERNET_BANKING("02"),
		CALL_CENTER("06"),
		MOBILE_BANKING("08"),
		SMART_DEPOSIT("25");
		
		private final String code;
		
		private PhoenixChannelTypes(String code)
		{
			this.code = code;
		}
		
		public String getChannelType()
		{
			return this.code;
		}	
	}
	
	public enum PhoenixResponseCodes
	{
		OK("00"),
		LIMIT_EXCEEDED("01"),
		ACC_INACTIVE("03"),
		LOW_BALANCE("04"),
		CARD_STATUS_ERROR("12"),
		WARM_CARD("14"),
		HOT_CARD("15"),
		BAD_CARD_STATUS("16"),
		INCORRECT_PIN("24"),
		PIN_RETRY_EXHAUSTED("60"),
		ACC_STATUS_INVALID("67"),
		ACC_LOCKED("92"),
		PIN_EXPIRED("93");
		
		private final String code;
		

		private PhoenixResponseCodes(String code)
		{
			this.code = code;
		}
		
		public String getResponseCodeValue()
		{
			return this.code;
		}
		
	}
	
	public enum MessageProtocolScope
	{
		REQUIRED_IN_REQUEST_ONLY,
		REQUIRED_IN_RESPONSE_ONLY,
		REQUIRED_IN_ALL,
	}
	
}
