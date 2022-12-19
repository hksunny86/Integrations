package com.inov8.integration.channel.JSBLB.ETPaymentCollection.enums;



import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Inov8 on 10/23/2019.
 */
public enum ETPaymentCollectionEnum{
    PROCESSED("00000");



    private static final Map<String, ETPaymentCollectionEnum> lookup = new HashMap<String, ETPaymentCollectionEnum>();

    /**
     * The static block to populate the Map uses a specialized implementation of Set, java.util.EnumSet,
     * that "probably" (according to the javadocs) has better performance than java.util.HashSet.
     * Java 5.0 also provides java.util.EnumMap, a specialized implementation of
     * Map for enumerations that is more compact than java.util.HashMap.
     */
    static {
        for (ETPaymentCollectionEnum responseCodeEnum : EnumSet.allOf(ETPaymentCollectionEnum.class))
            lookup.put(responseCodeEnum.getValue(), responseCodeEnum);
    }

    private ETPaymentCollectionEnum(String value) {
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
    public static ETPaymentCollectionEnum lookup(String code) {
        return lookup.get(code);
    }
}
