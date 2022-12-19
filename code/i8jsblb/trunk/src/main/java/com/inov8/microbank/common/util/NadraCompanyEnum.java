package com.inov8.microbank.common.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum NadraCompanyEnum {

	BWASA("2510776"),
	CDGK("2510784"),
	FESCO("2510758"),
	FWASA("2510778"),
	GEPCO("2510700"),
	GWASA("2510780"),
	KWSB("2510768"),
	LWASA("2510770"),
	MEPCO("2510756"),
	MWASA("2510774"),
	PESCO("2510760"),
	QESCO("2510762"),
	RWASA("2510772"),
	SCO("2510782"),
	SEPCO("2510766"),
	TESCO("2510764");
	
//  Product IDs conflicted with Utility/Internet Company Enum
//	BWASA_BILL("2510777"),
//	CDGK_BILL("2510785"),
//	FESCO_BILL("2510759"),
//	FWASA_BILL("2510779"),
//	GEPCO_BILL("2510718"),
//	GWASA_BILL("2510781"),
//	KWSB_BILL("2510769"),
//	LWASA_BILL("2510771"),
//	MWASA_BILL("2510775"),
//	PESCO_BILL("2510761"),
//	QESCO_BILL("-1"), //***2510801 commented on behalf of kashif, conflict with Bulk Payment***
//	RWASA_BILL("2510773"),
//	SCO_BILL("2510783"),
//	SEPCO_BILL("2510767"),
//	TESCO_BILL("2510765");

	private String value;

	private static final Map<String,NadraCompanyEnum> lookup = new HashMap<String,NadraCompanyEnum>();
	public static final List<NadraCompanyEnum> enumList = new ArrayList<NadraCompanyEnum>(0);
	
	static {
	
		for(NadraCompanyEnum nadraCompanyEnum : EnumSet.allOf(NadraCompanyEnum.class)) {
	         lookup.put(nadraCompanyEnum.getValue(), nadraCompanyEnum);
		}
		
		enumList.addAll(lookup.values());
	}

	private NadraCompanyEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static NadraCompanyEnum lookup(String code) {
		return lookup.get(code);
	}
	
	public static Boolean contains(String code) {
	
		Boolean retVal = Boolean.FALSE;
		
		for (NadraCompanyEnum nadraCompanyEnum : NadraCompanyEnum.values()) {
		
			if(nadraCompanyEnum.value.equals(code)){
				
				retVal=Boolean.TRUE;
				
				break;
			}
		}
		
		return retVal;
	}	
	
	
	public static long getProductUserTypeId(String code) {
		
		long productUserTypeId = 0L;
		
		NadraCompanyEnum nadraCompanyEnum = lookup(code);
		
		if(nadraCompanyEnum!= null && nadraCompanyEnum.name().contains("BILL")) {
			
			productUserTypeId = UserTypeConstantsInterface.CUSTOMER;
			
		} else if(nadraCompanyEnum!= null) {

			productUserTypeId = UserTypeConstantsInterface.RETAILER;
		}
		
		return productUserTypeId;
	}
}