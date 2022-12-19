package com.inov8.integration.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by inov8 on 10/17/2017.
 */
public enum I8SBResponseCodeEnum {
    PROCESSED("I8SB-200"),
    PROCESSED_00("00"),
    PROCESSED_000("000"),
    PROCESSED_0000("0000"),
//    PROCESSED_00000("200"),

    PROCESSED_00000("2000"), // 2000 only use for BOP BLB

    NOT_PROCESSED("I8SB-204"),

    BAD_REQUEST("I8SB-400"),
    SERVICE_NOT_FOUND("I8SB-404"),
    UNAUTHORIZED("I8SB-401"),
    INTERNAL_ERROR("I8SB-500"),
    FORBIDDEN("I8SB-403"),
    TIME_OUT("I8SB-504");

    /**
     * A Map to hold all Enum values used for reverse lookup.
     */
    private static final Map<String,I8SBResponseCodeEnum> lookup = new HashMap<String,I8SBResponseCodeEnum>();

    /**
     * The static block to populate the Map uses a specialized implementation of Set, java.util.EnumSet,
     * that "probably" (according to the javadocs) has better performance than java.util.HashSet.
     * Java 5.0 also provides java.util.EnumMap, a specialized implementation of
     * Map for enumerations that is more compact than java.util.HashMap.
     */
    static {
        for(I8SBResponseCodeEnum responseCodeEnum : EnumSet.allOf(I8SBResponseCodeEnum.class))
            lookup.put(responseCodeEnum.getValue(), responseCodeEnum);
    }

    private I8SBResponseCodeEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    /**
     * The static get(int) method here provides the reverse lookup by simply
     * getting the value from the Map,
     * @param code
     * @return RDVMB_ResponseCodeEnum
     */
    public static I8SBResponseCodeEnum lookup(String code) {
        return lookup.get(code);
    }
}

