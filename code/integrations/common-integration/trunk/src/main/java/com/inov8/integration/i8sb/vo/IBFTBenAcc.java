package com.inov8.integration.i8sb.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
@XStreamAlias("IBFTBenAcc")
public class IBFTBenAcc implements Serializable {

    private final static long serialVersionUID = 1L;

    private String IBFTBenAcctNumber;
    private String IBFTBenAcctTitle;
    private String IBFTBenName;
    private String IBFTBenBankCode;
    private String IBFTBenBankName;
    private String IBFTBenLastAmount;
    private String IBFTBenLastDate;
    private String IBFTBenBankIMD;
    private String IBFTBenBranchName;

    public String getIBFTBenAcctNumber() {return IBFTBenAcctNumber;}

    public void setIBFTBenAcctNumber(String IBFTBenAcctNumber) {this.IBFTBenAcctNumber = IBFTBenAcctNumber;}

    public String getIBFTBenAcctTitle() {return IBFTBenAcctTitle;}

    public void setIBFTBenAcctTitle(String IBFTBenAcctTitle) {this.IBFTBenAcctTitle = IBFTBenAcctTitle;}

    public String getIBFTBenName() {return IBFTBenName;}

    public void setIBFTBenName(String IBFTBenName) {this.IBFTBenName = IBFTBenName;}

    public String getIBFTBenBankCode() {return IBFTBenBankCode;}

    public void setIBFTBenBankCode(String IBFTBenBankCode) {this.IBFTBenBankCode = IBFTBenBankCode;}

    public String getIBFTBenBankName() {return IBFTBenBankName;}

    public void setIBFTBenBankName(String IBFTBenBankName) {this.IBFTBenBankName = IBFTBenBankName;}

    public String getIBFTBenLastAmount() {return IBFTBenLastAmount;}

    public void setIBFTBenLastAmount(String IBFTBenLastAmount) {this.IBFTBenLastAmount = IBFTBenLastAmount;}

    public String getIBFTBenLastDate() {return IBFTBenLastDate;}

    public void setIBFTBenLastDate(String IBFTBenLastDate) {this.IBFTBenLastDate = IBFTBenLastDate;}

    public String getIBFTBenBankIMD() {return IBFTBenBankIMD;}

    public void setIBFTBenBankIMD(String IBFTBenBankIMD) {this.IBFTBenBankIMD = IBFTBenBankIMD;}

    public String getIBFTBenBranchName() {return IBFTBenBranchName;}

    public void setIBFTBenBranchName(String IBFTBenBranchName) {this.IBFTBenBranchName = IBFTBenBranchName;}
}
