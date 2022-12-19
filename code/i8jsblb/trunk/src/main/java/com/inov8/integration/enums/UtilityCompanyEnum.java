/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * UtilityCompanyEnum which hold each utility company's unique id, that will be used while
 * performing bill inquiry on external system.
 *
 */
public enum UtilityCompanyEnum {
	PTCL("PTCL0001"),
	KESC("KESC0001"),
	LESCO("LESCO001"),
	SSGC("SSGC0001"),
	SNGPL("SNGPL001"),
	HESCO("HESCO001"), 
	GEPCO("GEPCO001"),
	IESCO("IESCO001");
	/**
	 * A Map to hold all Enum values used for reverse lookup. 
	 */
	private static final Map<String,UtilityCompanyEnum> lookup = new HashMap<String,UtilityCompanyEnum>();
	
	/**
	 * The static block to populate the Map uses a specialized implementation of Set, java.util.EnumSet, 
	 * that "probably" (according to the javadocs) has better performance than java.util.HashSet. 
	 * Java 5.0 also provides java.util.EnumMap, a specialized implementation of 
	 * Map for enumerations that is more compact than java.util.HashMap.
	 */
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
}
