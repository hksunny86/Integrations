package com.inov8.integration.channel.AppInSnap.enums;

import com.inov8.integration.channel.vrg.echallan.enums.VRG_EChallan_ResponseCodeEnum;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum AppInSnapEnum {
    PROCESSED("200"),
    NOT_PROCESSED("417");

    /**
     * A Map to hold all Enum values used for reverse lookup.
     */
    private static final Map<String, AppInSnapEnum> lookup = new HashMap<String, AppInSnapEnum>();

    /**
     * The static block to populate the Map uses a specialized implementation of Set, java.util.EnumSet,
     * that "probably" (according to the jav
     *
     * adocs) has better performance than java.util.HashSet.
     * Java 5.0 also provides java.util.EnumMap, a specialized implementation of
     * Map for enumerations that is more compact than java.util.HashMap.
     */
    static {
        for (AppInSnapEnum responseCodeEnum : EnumSet.allOf(AppInSnapEnum.class))
            lookup.put(responseCodeEnum.getValue(), responseCodeEnum);
    }

    private AppInSnapEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    /**
     * The static get(int) method here provides the reverse lookup by simply
     * getting the value from the Map,
     *
     * @param code
     * @return RDVMB_ResponseCodeEnum
     */
    public static AppInSnapEnum lookup(String code) {
        return lookup.get(code);
    }
}
