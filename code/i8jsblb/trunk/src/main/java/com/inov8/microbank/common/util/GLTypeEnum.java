package com.inov8.microbank.common.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AtieqRe on 9/11/2016.
 */
public enum GLTypeEnum {

    ASSETS      (1l, "11", "Assets"),
    EXPENSE     (2l, "22", "Expense"),
    INCOME      (3l, "33", "Income"),
    LIABILITY   (4l, "44", "Liability");

    public static String getCode(long id) {
        return lookup.get(id);
    }

    private static final Map<Long, String> lookup = new HashMap<>(4);

    static {
        for (GLTypeEnum glTypeEnum : EnumSet.allOf(GLTypeEnum.class))
            lookup.put(glTypeEnum.getId(), glTypeEnum.getCode());
    }

    long id;
    String code;
    String name;

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private GLTypeEnum(long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}

