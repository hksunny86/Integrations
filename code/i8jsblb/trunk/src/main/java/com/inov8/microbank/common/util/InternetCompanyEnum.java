package com.inov8.microbank.common.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum InternetCompanyEnum {
	VFONE("2510739"),
	WATEEN("2510740"),
    WITRIBE("2510741"),
	ZONG_PREPAID("2510744"),
	ZONG_POSTPAID("2510745"),
	EVO_PREPAID("2510747"),
	EVO_POSTPAID("2510748"),
	DEFAULTER("2510752"),
	WARID_PREPAID("2510715"),
	WARID_POSTPAID("2510753"),
	UFONE_PREPAID("2510765"),
	UFONE_POSTPAID("2510743"),
	TELENOR_PREPAID("2510738"),
	TELENOR_POSTPAID("2510749"),
	MOBILINK_PREPAID("2510719"),
	MOBILINK_POSTPAID("2510742"),
	QUBEE_CONSUMER("2510789"),
	QUBEE_DISTRIBUTOR("2510767"),

	MOBILINK_PREPAID_AIRTIME("10245146"),
	WARID_PREPAID_AIRTIME("10245148"),
	UFONE_PREPAID_AIRTIME("10245150"),
	TELENOR_PREPAID_AIRTIME("10245152"),
	ZONG_PREPAID_AIRTIME("10245154"),

	VFONE_BILL("60024"),
	WATEEN_BILL("60025"),
	WITRIBE_BILL("60026"),
	ZONG_PREPAID_BILL("60027"),
	ZONG_POSTPAID_BILL("60028"),
	EVO_PREPAID_BILL("60029"),
	EVO_POSTPAID_BILL("60030"),
	DEFAULTER_BILL("60031"),
	WARID_PREPAID_BILL("60032"),
	WARID_POSTPAID_BILL("60033"),
	UFONE_PREPAID_BILL("60034"),
	UFONE_POSTPAID_BILL("60035"),
	TELENOR_PREPAID_BILL("60036"),
	TELENOR_POSTPAID_BILL("60037"),
	MOBILINK_PREPAID_BILL("60038"),
	MOBILINK_POSTPAID_BILL("60039"),
	QUBEE_CONSUMER_BILL("60040"),
	QUBEE_DISTRIBUTOR_BILL("60041"),
	CREDIT_CARD_BILL("10245109");


	private static final Map<String,InternetCompanyEnum> lookup = new HashMap<String,InternetCompanyEnum>();
	static {
	    for(InternetCompanyEnum internetCompanyEnum : EnumSet.allOf(InternetCompanyEnum.class))
	         lookup.put(internetCompanyEnum.getValue(), internetCompanyEnum);
	}

	private InternetCompanyEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
	public static InternetCompanyEnum lookup(String code) {
		return lookup.get(code);
	}
	public static boolean contains(String code){
		boolean retVal=false;
		for (InternetCompanyEnum internetCompanyEnum : InternetCompanyEnum.values()) {
			if(internetCompanyEnum.value.equals(code)){
				retVal=true;
				break;
			}
		}
		return retVal;
		
	}
	
}
