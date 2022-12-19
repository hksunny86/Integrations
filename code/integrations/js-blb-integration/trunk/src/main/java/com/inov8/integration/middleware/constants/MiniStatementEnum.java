package com.inov8.integration.middleware.constants;

public enum MiniStatementEnum implements EnumHelper {

    DATEF("date", "datef"),
    DESCRIPTION("description", "description"),
    AMTF("amount", "amtf");

    private String name;
    private String path;

    MiniStatementEnum(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public EnumHelper[] result() {
        return values();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public Class getListEnum() {
        return null;
    }

    @Override
    public Class getListClass() {
        return null;
    }
}
