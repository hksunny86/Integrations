package com.inov8.integration.channel.fonepay.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by inov8 on 11/8/2017.
 */
public enum FonePay_ResponseCodeEnum {
    PROCESSED("0000"),
    NOT_PROCESSED("0001");

    /**
     * A Map to hold all Enum values used for reverse lookup.
     */
    private static final Map<String,FonePay_ResponseCodeEnum> lookup = new HashMap<String,FonePay_ResponseCodeEnum>();

    /**
     * The static block to populate the Map uses a specialized implementation of Set, java.util.EnumSet,
     * that "probably" (according to the javadocs) has better performance than java.util.HashSet.
     * Java 5.0 also provides java.util.EnumMap, a specialized implementation of
     * Map for enumerations that is more compact than java.util.HashMap.
     */
    static {
        for(FonePay_ResponseCodeEnum responseCodeEnum : EnumSet.allOf(FonePay_ResponseCodeEnum.class))
            lookup.put(responseCodeEnum.getValue(), responseCodeEnum);
    }

    private FonePay_ResponseCodeEnum(String value) {
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
    public static FonePay_ResponseCodeEnum lookup(String code) {
        return lookup.get(code);
    }
}
