package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("CCMiniStmt")
public class CCMiniStmt implements Serializable {
    private final static long serialVersionUID = 1L;

    private String txnDate;
    private String txnDesc;
    private String txnNature;
    private String txnAmount;

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getTxnDesc() {
        return txnDesc;
    }

    public void setTxnDesc(String txnDesc) {
        this.txnDesc = txnDesc;
    }

    public String getTxnNature() {
        return txnNature;
    }

    public void setTxnNature(String txnNature) {
        this.txnNature = txnNature;
    }

    public String getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(String txnAmount) {
        this.txnAmount = txnAmount;
    }
}
