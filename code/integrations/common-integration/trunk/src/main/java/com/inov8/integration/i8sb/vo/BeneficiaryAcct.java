package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("BeneficiaryAcct")
public class BeneficiaryAcct implements Serializable {

    private final static long serialVersionUID = 1L;

    private String benAccountNumber;
    private String benAccountTitle;
    private String beneficiaryName;
    private String benBranchName;
    private String benAccountCurrency;
    private String benAccountType;
    private String benBankName;
    private String benLastAmount;
    private String benLastDate;

    public String getBenAccountNumber() {
        return benAccountNumber;
    }

    public void setBenAccountNumber(String benAccountNumber) {
        this.benAccountNumber = benAccountNumber;
    }

    public String getBenAccountTitle() {
        return benAccountTitle;
    }

    public void setBenAccountTitle(String benAccountTitle) {
        this.benAccountTitle = benAccountTitle;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBenBranchName() {
        return benBranchName;
    }

    public void setBenBranchName(String benBranchName) {
        this.benBranchName = benBranchName;
    }

    public String getBenAccountCurrency() {
        return benAccountCurrency;
    }

    public void setBenAccountCurrency(String benAccountCurrency) {
        this.benAccountCurrency = benAccountCurrency;
    }

    public String getBenAccountType() {
        return benAccountType;
    }

    public void setBenAccountType(String benAccountType) {
        this.benAccountType = benAccountType;
    }

    public String getBenBankName() {return benBankName;}

    public void setBenBankName(String benBankName) {this.benBankName = benBankName;}

    public String getBenLastAmount() {return benLastAmount;}

    public void setBenLastAmount(String benLastAmount) {this.benLastAmount = benLastAmount;}

    public String getBenLastDate() {return benLastDate;}

    public void setBenLastDate(String benLastDate) {this.benLastDate = benLastDate;}
}
