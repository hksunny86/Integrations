package com.inov8.integration.webservice.l2Account;


import java.io.Serializable;
import java.util.List;

public class DataList implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;

    private List<String> sourceOfIncome;
    private List<String> purposeOfAccount;
    private List<String> monthlyDebitLimit;
    private List<String> nationality;

    public List<String> getSourceOfIncome() {
        return sourceOfIncome;
    }

    public void setSourceOfIncome(List<String> sourceOfIncome) {
        this.sourceOfIncome = sourceOfIncome;
    }

    public List<String> getPurposeOfAccount() {
        return purposeOfAccount;
    }

    public void setPurposeOfAccount(List<String> purposeOfAccount) {
        this.purposeOfAccount = purposeOfAccount;
    }

    public List<String> getMonthlyDebitLimit() {
        return monthlyDebitLimit;
    }

    public void setMonthlyDebitLimit(List<String> monthlyDebitLimit) {
        this.monthlyDebitLimit = monthlyDebitLimit;
    }

    public List<String> getNationality() {
        return nationality;
    }

    public void setNationality(List<String> nationality) {
        this.nationality = nationality;
    }
}
