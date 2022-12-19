package com.inov8.microbank.common.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum BookMeProductEnum {

    Bus_Ticketing("10245173"),
    Cinema("10245177"),
    Air("10245175"),
    Hotel("10245179"),
    Event("10245181");


    private static final Map<String, BookMeProductEnum> lookup = new HashMap<String, BookMeProductEnum>();

    static {
        for (BookMeProductEnum bookMeProductEnum : EnumSet.allOf(BookMeProductEnum.class))
            lookup.put(bookMeProductEnum.getValue(), bookMeProductEnum);
    }

    private BookMeProductEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public static BookMeProductEnum lookup(String code) {
        return lookup.get(code);
    }

    public static boolean contains(String code) {
        boolean retVal = false;
        for (BookMeProductEnum bookMeProductEnum : BookMeProductEnum.values()) {
            if (bookMeProductEnum.value.equals(code)) {
                retVal = true;
                break;
            }
        }
        return retVal;

    }
}
