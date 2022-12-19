package com.inov8.integration.middleware.constants;

import com.inov8.integration.webservice.vo.Transaction;

public enum MiniStatementResponseEnum implements EnumHelper {

    LIST("transactions", "//trans/trn", true, MiniStatementEnum.class, Transaction.class);

    private String name;
    private String path;
    private boolean list;
    private Class listEnum;
    private Class listClass;

    MiniStatementResponseEnum(String name, String path) {
        this.name = name;
        this.path = path;
    }

    MiniStatementResponseEnum(String name, String path, boolean isList, Class helper, Class listClass) {
        this.name = name;
        this.path = path;
        this.list = isList;
        this.listEnum = helper;
        this.listClass = listClass;
    }

    @Override
    public EnumHelper[] result() {
        return values();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public boolean isList() {
        return this.list;
    }

    @Override
    public Class getListEnum() {
        return this.listEnum;
    }

    @Override
    public Class getListClass() {
        return this.listClass;
    }
}
