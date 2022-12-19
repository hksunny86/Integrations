package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * Created by Inov8 Limited on 10/18/2017.
 */
@XStreamAlias("OneLinkBank")
public class OneLinkBank implements Serializable {

    private final static long serialVersionUID = 1L;
    private String bankIMD;
    private String bankNumber;
    private String accountNoMinLength;
    private String accountNoMaxLength;

    public OneLinkBank() {
    }

    public OneLinkBank(String bankIMD, String bankNumber, String accountNoMinLength, String accountNoMaxLength) {
        this.bankIMD = bankIMD;
        this.bankNumber = bankNumber;
        this.accountNoMinLength = accountNoMinLength;
        this.accountNoMaxLength = accountNoMaxLength;
    }

    public String getBankIMD() {
        return bankIMD;
    }

    public void setBankIMD(String bankIMD) {
        this.bankIMD = bankIMD;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getAccountNoMinLength() {
        return accountNoMinLength;
    }

    public void setAccountNoMinLength(String accountNoMinLength) {
        this.accountNoMinLength = accountNoMinLength;
    }

    public String getAccountNoMaxLength() {
        return accountNoMaxLength;
    }

    public void setAccountNoMaxLength(String accountNoMaxLength) {
        this.accountNoMaxLength = accountNoMaxLength;
    }
}
