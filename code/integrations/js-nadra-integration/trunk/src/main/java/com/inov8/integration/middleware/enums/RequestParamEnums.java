package com.inov8.integration.middleware.enums;

public enum RequestParamEnums {

    SUBSCRIPTION_KEY("Ocp-Apim-Subscription-Key"),
    VERIFY_OPERATION("/verify"),
    OTC_OPERATION("/otc");

    private String value;

    private RequestParamEnums(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
