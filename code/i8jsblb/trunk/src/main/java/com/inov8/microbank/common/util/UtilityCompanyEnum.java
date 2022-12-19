package com.inov8.microbank.common.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum UtilityCompanyEnum {
	PTCL("2510708"),
	SSGC("2510711"),
	SNGPL("2510710"),
	LESCO("2510705"),
	HESCO("2510720"),
	GEPCO("2510700"),
	IESCO("2510600"),
	KESC("2510704"),
	MEPCO("2510756"),
	FESCO("2510758"),
	PESCO("2510760"),
	QESCO("2510762"),
	TESCO("2510764"),
	SEPCO("2510766"),
	KWSB("2510768"),
	LWASA("2510770"),
	RWASA("2510772"),
	MWASA("2510774"),
	BWASA("2510776"),
	FWASA("2510778"),
	GWASA("2510780"),
	SCO("2510782"),
	CDGK("2510784"),
	
	PTCL_BILL("60001"),
	SSGC_BILL("60002"),
	SNGPL_BILL("60003"),
	LESCO_BILL("60004"),
	HESCO_BILL("60005"),
	GEPCO_BILL("60006"),
	IESCO_BILL("60007"),
	KESC_BILL("60008"),
	MEPCO_BILL("60009"),
	FESCO_BILL("60010"),
	PESCO_BILL("60011"),
	QESCO_BILL("60012"),
	TESCO_BILL("60013"),
	SEPCO_BILL("60014"),
	KWSB_BILL("60015"),
	LWASA_BILL("60016"),
	RWASA_BILL("60017"),
	BWASA_BILL("60019"),
	MWASA_BILL("60018"),
	FWASA_BILL("60020"),
	GWASA_BILL("60021"),
	SCO_BILL("60022"),
	CDGK_BILL("60023")
	;
	
	
	private static final Map<String,UtilityCompanyEnum> lookup = new HashMap<String,UtilityCompanyEnum>();
	static {
	    for(UtilityCompanyEnum utilityCompanyEnum : EnumSet.allOf(UtilityCompanyEnum.class))
	         lookup.put(utilityCompanyEnum.getValue(), utilityCompanyEnum);
	}

	private UtilityCompanyEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
	public static UtilityCompanyEnum lookup(String code) {
		return lookup.get(code);
	}
	public static boolean contains(String code){
		boolean retVal=false;
		for (UtilityCompanyEnum utilityCompanyEnum : UtilityCompanyEnum.values()) {
			if(utilityCompanyEnum.value.equals(code)){
				retVal=true;
				break;
			}
		}
		return retVal;
		
	}
	
}
