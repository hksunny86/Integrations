package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.List;
@XStreamAlias("TDRAccountStatement")
public class TDRAccountStatement implements Serializable {


    private static final long serialVersionUID = 622936830313691046L;

    private String principalAmount1;
    private String principalAmount2;
    private List<TDRAccountTransaction> transactions;

    public String getPrincipalAmount1() {
        return principalAmount1;
    }

    public void setPrincipalAmount1(String principalAmount1) {
        this.principalAmount1 = principalAmount1;
    }

    public String getPrincipalAmount2() {
        return principalAmount2;
    }

    public void setPrincipalAmount2(String principalAmount2) {
        this.principalAmount2 = principalAmount2;
    }

    public List<TDRAccountTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TDRAccountTransaction> transactions) {
        this.transactions = transactions;
    }
}
