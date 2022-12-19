package com.inov8.microbank.common.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MyCommissionEnum {
	
	TODAY("1"),
	
	YESTERDAY("2"),
	THIS_WEEK("3"),
	
	LAST_WEEK("4"),
	THIS_MONTH("5"),
	LAST_MONTH("6")
	;
	
	
	private static final Map<String,MyCommissionEnum> lookup = new HashMap<String,MyCommissionEnum>();
	static {
	    for(MyCommissionEnum myCommissionEnum : EnumSet.allOf(MyCommissionEnum.class))
	         lookup.put(myCommissionEnum.getValue(), myCommissionEnum);
	}

	private MyCommissionEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
	public static MyCommissionEnum lookup(String code) {
		return lookup.get(code);
	}
	public static boolean contains(String code){
		boolean retVal=false;
		for (MyCommissionEnum myCommissionEnum : MyCommissionEnum.values()) {
			if(myCommissionEnum.value.equals(code)){
				retVal=true;
				break;
			}
		}
		return retVal;
		
	}
	
}
