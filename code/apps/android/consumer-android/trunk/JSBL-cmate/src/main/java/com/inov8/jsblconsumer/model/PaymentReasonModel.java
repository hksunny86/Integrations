package com.inov8.jsblconsumer.model;

public class PaymentReasonModel {

    private String code;
    private String name;

    public PaymentReasonModel(){

    }

    public PaymentReasonModel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
