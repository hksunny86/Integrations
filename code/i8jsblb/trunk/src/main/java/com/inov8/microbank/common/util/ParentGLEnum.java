package com.inov8.microbank.common.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AtieqRe on 9/11/2016.
 */
public enum ParentGLEnum {

    BLB_DEPOSITS(1l, "BLB Deposits",    GLTypeEnum.LIABILITY, PoolAccountConstantsInterface.PARENT_GL_BLB_DEPOSITS),
    PAYABLE     (2l, "Payable",         GLTypeEnum.LIABILITY, PoolAccountConstantsInterface.PARENT_GL_PAYABLE),
    EXPENSE     (3l, "Expense",         GLTypeEnum.EXPENSE,   PoolAccountConstantsInterface.PARENT_GL_EXPENSE),
    INCOME      (4l, "Income",          GLTypeEnum.INCOME,    PoolAccountConstantsInterface.PARENT_GL_INCOME),
    CONTROL_AC  (5l, "Control A/c",     GLTypeEnum.LIABILITY, PoolAccountConstantsInterface.PARENT_GL_CONTROL_AC),
    SUNDRY      (6l, "Sundry",          GLTypeEnum.LIABILITY, PoolAccountConstantsInterface.PARENT_GL_SUNDRY),
    SETTLEMENT  (7l, "Settlement",      GLTypeEnum.LIABILITY, PoolAccountConstantsInterface.PARENT_GL_SETTLEMENT),
    LIABILITY   (8l, "Liability",       GLTypeEnum.LIABILITY, PoolAccountConstantsInterface.PARENT_GL_LIABILITY),
    RECEIVABLES (9l, "Receivables",     GLTypeEnum.ASSETS,    PoolAccountConstantsInterface.PARENT_GL_RECEIVABLES),
    WHT_PAYABLES(10l, "WHT Payables",   GLTypeEnum.LIABILITY, PoolAccountConstantsInterface.PARENT_GL_WHT_PAYABLES)
    ;

    private long id;
    private String name;
    private GLTypeEnum glTypeEnum;
    private Long parentGLSBIId;

    private static final Map<Long, ParentGLEnum> lookup = new HashMap<>(8);

    static {
        for (ParentGLEnum glTypeEnum : EnumSet.allOf(ParentGLEnum.class))
            lookup.put(glTypeEnum.getId(), glTypeEnum);
    }

    public static ParentGLEnum getParentGLEnum(long id) {
        return lookup.get(id);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getParentGLSBIId() {
        return parentGLSBIId;
    }
    private ParentGLEnum(long id, String name, GLTypeEnum glTypeEnum, Long parentGLSBIId) {
        this.id = id;
        this.name = name;
        this.glTypeEnum = glTypeEnum;
        this.parentGLSBIId = parentGLSBIId;
    }
}

