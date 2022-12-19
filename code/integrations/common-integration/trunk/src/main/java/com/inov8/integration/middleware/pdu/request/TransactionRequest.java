package com.inov8.integration.middleware.pdu.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inov8.integration.middleware.pdu.Transaction;

import java.io.Serializable;
import java.util.List;

/**
 * Created by inov8 on 10/25/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TransactionRequest implements Serializable {
    private static final long serialVersionUID = -4749728125457084086L;

    @JsonProperty("Security_Key")
    private String secretKey;

    @JsonProperty("Transaction")
    private List<Transaction> transactionList;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
