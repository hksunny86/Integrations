package com.inov8.integration.middleware.enums;

public enum ConstantEnums {
    SETTLEMENT_TYPE("03");

    private String value;

    private ConstantEnums(String value){
        this.value = value;}

    public String getValue(){
        return this.value;
    }
}
