package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("AccountStatement")
public class AssociatedAccounts implements Serializable {
private String accountNumber;

    public String getAccountNumber() { return accountNumber; }

    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
}
