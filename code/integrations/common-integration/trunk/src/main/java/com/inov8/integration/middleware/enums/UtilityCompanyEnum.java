/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.middleware.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * UtilityCompanyEnum which hold each utility company's unique id, that will be
 * used while performing bill inquiry on external system.
 * 
 */
public enum UtilityCompanyEnum {
	// @formatter:off
	GEPCO("GEPCO001",null, 14, 14),
	HESCO("HESCO001","55", 14, 14),
	KESC("KESC0001", "63",13, 13),
	LESCO("LESCO001", "10",14, 14),
	PTCL_LANDLINE("PTCL0010", "35",10, 10),
	PTCL_EVO_PREPAID("PTCL0011","12", 9, 11),
	PTCL_EVO_POSTPAID("PTCL0012", "13",9, 11),
	PTCL_DEFAULTER("PTCL0013", "14",10, 10),
	PTCL_VFONE("PTCL0015","15", 10, 10),
	SNGPL("SNGPL001","89", 11, 11),
	SSGC("SSGC0001", "69",10, 10),
	TELENOR_PREPAID("TELNOR01","20", 1, 11),
	TELENOR_POSTPAID("TELNOR02","21", 1, 11),
	UFONE_PREPAID("UFONE001", "17",1, 11),
	UFONE_POSTPAID("UFONE002","18",1, 11),
	UFONE_RETAILER("UFONE003", null,1, 11),
	WARID_PREPAID("WARID001","22", 1, 11),
	WARID_POSTPAID("WARID002","23", 1, 11),
	WATEEN("WATEEN01", "28",6, 6),
	WITRIBE("WTRIBE01", "29",1, 20),
	ZONG_PREPAID("ZONG0001", "24",1, 11),
	ZONG_POSTPAID("ZONG0002","30", 1, 11),
	MOBILINK_PREPAID("MBLINK01","26", 1, 11),
	MOBILINK_POSTPAID("MBLINK02","27", 1, 11),
	QUBEE_CONSUMER ("QUBEE001","31", 1, 17),
	QUBEE_DISTRIBUTOR ("QUBEE002","32", 1, 17);
	// @formatter:on

	/**
	 * A Map to hold all Enum values used for reverse lookup.
	 */
	private static final Map<String, UtilityCompanyEnum> lookup = new HashMap<String, UtilityCompanyEnum>();

	/**
	 * The static block to populate the Map uses a specialized implementation of
	 * Set, java.util.EnumSet, that "probably" (according to the javadocs) has
	 * better performance than java.util.HashSet. Java 5.0 also provides
	 * java.util.EnumMap, a specialized implementation of Map for enumerations
	 * that is more compact than java.util.HashMap.
	 */
	static {
		for (UtilityCompanyEnum utilityCompanyEnum : EnumSet.allOf(UtilityCompanyEnum.class))
			lookup.put(utilityCompanyEnum.getId(), utilityCompanyEnum);
	}

	private UtilityCompanyEnum(String value) {
		this.value = value;
	}

	private UtilityCompanyEnum(String code, String id, int minSize, int maxSize) {
		this.value = code;
		this.id = id;
		this.minSize = minSize;
		this.maxSize = maxSize;
	}

	private String id;
	private String value;
	private int maxSize;
	private int minSize;

	public String getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public static UtilityCompanyEnum lookup(String code) {
		return lookup.get(code);
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public int getMinSize() {
		return minSize;
	}

	public void setMinSize(int minSize) {
		this.minSize = minSize;
	}
}
