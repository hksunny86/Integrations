package com.inov8.integration.middleware.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * An enum that will have all Response Codes from AMBIT Server. 
 */
public enum ResponseCodeEnum {
	PROCESSED_OK("000"),
	PENDING("001"),
	TIMED_OUT("002"),
	RESPONSE_MAPPING_ERROR("004"),
	POSTING_RESTRICTED("092"),
	LOW_BALANCE("104"),
	LIMIT_EXCEEDED("106"),
	ACCOUNT_INACTIVE("107"),
	INVALID_CARD_RECORD("108"),
	FIELD_ERROR("109"),
	DUPLICATE_TRANSACTION("110"),
	INVALID_TRANSACTION_CODE("111"),
	INVALID_TRANSACTION_TYPE("112"),
	DATABASE_ERROR("113"),
	INVALID_CURRENCY_CODE("114"),
	INVALID_PIN("115"),
	CARD_EXPIRED("116"),
	INVALID_AMOUNT("117"),
	INVALID_HOST_STATUS("118"),
	HOST_REJECT("119"),
	SOME_ERROR_OCCURRED_IN_WEB_SERVICE("119"),
	HOST_COMMS_DOWN("120"),
	HOST_NOT_PROCESSING("121"),
	HOST_NOT_FOUND("122"),
	INVALID_ACCOUNT_STATUS("123"),
	UNKNOWN_TRANSACTION_SOURCE("124"),
	PERMISSION_DENIED("125"),
	INVALID_ACCOUNT("126"),
	ACCOUNT_NOT_FOUND("126"),
	INTERNAL_ERROR("127"),
	MESSAGE_FORMAT_ERROR("128"),
	INVALID_CARD_STATUS("129"),
	INVALID_HOST_MODE("130"),
	PIN_RETRIES_EXCEEDED("132"),
	HSM_TIMEDOUT("133"),
	NO_CHEQUES_PRESENTED_OR_BLOCKED("134"),
	WARM_CARD("140"),
	HOT_CARD("141"),
	PIN_DEACTIVATED("144"),
	PIN_TEMPORARILY_DEACTIVATED("145"),
	PASSWORD_MUST_BE_6_OR_MORE_CHARACTERS_LONG("146"),
	HOST_BRANCH_PROCESSING_ERROR("151"),
	TRANSACTION_REJECTED("152"),
	REMOTE_BRANCH_DOWN("153"),
	INVALID_TRANSACTION_DATE("154"),
	UNKNOWN_AUTHENTICATION_MODE("155"),
	ERROR_CURRENCY_CONVERSION("156"),
	HOST_IN_STAND_IN_MODE("157"),
	HOST_IN_BALANCE_DOWNLOAD_MODE("158"),
	SAF_TRANSACTION_MODE("159"),
	INVALID_MERCHANT("160"),
	INVALID_PIN_LENGTH("161"),
	CUSTOMER_NOT_FOUND("162"),
	ACCOUNT_LOCKED("163"),
	INVALID_EXPIRAY_DATE("164"),
	INSTRUMENT_NOT_FOUND("171"),
	GENERAL_PROCESSING_ERROR("195"),
	CUSTOMER_ALREADY_VERIFIED("251"),
	CHANNEL_NOT_SUPPORTED("253"),
	NO_EMAIL_ADDRESS_FOUND_FOR_CUSTOMER("254"),
	TRANSACTION_DENIED("255"),
	INVALID_PASSWORD("256"),
	CHEQUE_DOES_NOT_BELONG_TO_SELECTED_ACCOUNT("257"),
	SECURITY_VIOLATION("280"),
	CHEQUE_NOT_FOUND("281"),
	CHEQUE_STOPPED("282"),
	CHEQUE_PRESENTED("282"),
	BILL_ALREADY_PAID("341"),
	DUPLICATE_USER_NAME("410"),
	USER_NAME_CHANGE_REJECT("411"),
	USER_NAME_EXIST_Already_Generated("412"),
	INCORRECT_USER_NAME_OR_PASSWORD("413"),
	PASSWORD_NOT_GENERATED("414"),
	PASSWORD_CHANGE_REJECT("415"),
	INVALID_NIC("418"),
	NO_TRANSACTIONS_FOUND("419"),
	CONSUMER_NUMBER_NOT_FOUND("435"),
	DUE_DATE_PASSED("436"),
	INVALID_MOBILE_NUMBER("438"),
	RELATIONSHIP_NOT_FOUND("482"),
	BILL_NOT_FOUND("483"),
	INVALID_IBAN("500"),
	CONSUMER_NUM_ALREADY_REGISTERED("501"),
	UTILITY_COMPANY_ID_NOT_FOUND("618");
	
	/**
	 * A Map to hold all Enum values used for reverse lookup. 
	 */
	private static final Map<String,ResponseCodeEnum> lookup = new HashMap<String,ResponseCodeEnum>();

	/**
	 * The static block to populate the Map uses a specialized implementation of Set, java.util.EnumSet, 
	 * that "probably" (according to the javadocs) has better performance than java.util.HashSet. 
	 * Java 5.0 also provides java.util.EnumMap, a specialized implementation of 
	 * Map for enumerations that is more compact than java.util.HashMap.
	 */
	static {
	    for(ResponseCodeEnum responseCodeEnum : EnumSet.allOf(ResponseCodeEnum.class))
	         lookup.put(responseCodeEnum.getValue(), responseCodeEnum);
	}

	private ResponseCodeEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
	
	/**
	 * The static get(int) method here provides the reverse lookup by simply 
	 * getting the value from the Map,
	 * @return ResponseCodeEnum
	 */
	public static ResponseCodeEnum lookup(String code) {
		return lookup.get(code);
	}
}
