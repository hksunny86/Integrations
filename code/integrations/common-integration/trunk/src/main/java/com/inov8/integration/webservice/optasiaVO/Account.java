package com.inov8.integration.webservice.optasiaVO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "accountType",
        "accountID",
        "accountBalance",
        "accountCurrencyCode"
})
public class Account implements Serializable {

    private static final long serialVersionUID = 5824473488070382311L;


    @JsonProperty("accountType")
    private String accountType;
    @JsonProperty("accountID")
    private String accountID;
    @JsonProperty("accountBalance")
    private String accountBalance;
    @JsonProperty("accountCurrencyCode")
    private String accountCurrencyCode;

    @JsonProperty("accountType")
    public String getAccountType() {
        return accountType;
    }

    @JsonProperty("accountType")
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @JsonProperty("accountID")
    public String getAccountID() {
        return accountID;
    }

    @JsonProperty("accountID")
    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    @JsonProperty("accountBalance")
    public String getAccountBalance() {
        return accountBalance;
    }

    @JsonProperty("accountBalance")
    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    @JsonProperty("accountCurrencyCode")
    public String getAccountCurrencyCode() {
        return accountCurrencyCode;
    }

    @JsonProperty("accountCurrencyCode")
    public void setAccountCurrencyCode(String accountCurrencyCode) {
        this.accountCurrencyCode = accountCurrencyCode;
    }

}
