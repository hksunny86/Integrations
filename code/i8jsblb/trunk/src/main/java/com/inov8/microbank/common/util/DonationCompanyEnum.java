package com.inov8.microbank.common.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum DonationCompanyEnum {
	
	SKMCH("2510787"),
	APOTHECARE("2510791"),
	APOTHECARE_PAYMENT("2510793"),
	
	DAWAT_E_ISLAMI_ZAKAT("2510795"),
	DAWAT_E_ISLAMI_SADQA("2510797"),
	
	DAWAT_E_ISLAMI_ZAKAT_PAYMENT("2510794"),
	DAWAT_E_ISLAMI_SADQA_PAYMENT("2510796"),
	ML_RETAILER("2510798"),
	ML_CUSTOMER("2510800"),
	;
	
	private static final Map<String, DonationCompanyEnum> lookup = new HashMap<String, DonationCompanyEnum>();
	private String value;
	
	static {
	
		for(DonationCompanyEnum donationCompanyEnum : EnumSet.allOf(DonationCompanyEnum.class)) {
	         lookup.put(donationCompanyEnum.getValue(), donationCompanyEnum);
		}
	}

	private DonationCompanyEnum(String value) {
		this.value = value;
	}


	public String getValue() {
	
		return value;
	}
	
	public static DonationCompanyEnum lookup(String code) {
	
		return lookup.get(code);
	}
	
	public static Boolean contains(String code) {
		
		Boolean retVal = Boolean.FALSE;
		
		for (DonationCompanyEnum donationCompanyEnum : DonationCompanyEnum.values()) {
		
			if(donationCompanyEnum.value.equals(code)) {
				retVal = Boolean.TRUE;
				break;
			}
		}
		
		return retVal;
	}	
}