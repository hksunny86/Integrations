package com.inov8.jsblconsumer.model;

import com.inov8.jsblconsumer.util.XmlConstants;

import java.io.Serializable;

public class AdvanceLoanModel implements Serializable {

    private String pId;
    private String cnic;
    private String cmob;
    private String tAmt;
    private String tAmtF;
    private String cAmt;
    private String cAmtF;
    private String txam;
    private String txamF;
    private String tpam;
    private String tpamF;
    private String thirdPartyRRN;

    public AdvanceLoanModel() {

    }

    public AdvanceLoanModel(String pId, String cnic, String cmob, String tAmt, String tAmtF, String cAmt, String cAmtF, String txam,
                            String txamF, String tpam, String tpamF, String thirdPartyRRN) {
        this.pId = pId;
        this.cnic = cnic;
        this.cmob = cmob;
        this.tAmt = tAmt;
        this.tAmtF = tAmtF;
        this.cAmt = cAmt;
        this.cAmtF = cAmtF;
        this.txam = txam;
        this.txamF = txamF;
        this.tpam = tpam;
        this.tpamF = tpamF;
        this.thirdPartyRRN = thirdPartyRRN;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getCmob() {
        return cmob;
    }

    public void setCmob(String cmob) {
        this.cmob = cmob;
    }

    public String gettAmt() {
        return tAmt;
    }

    public void settAmt(String tAmt) {
        this.tAmt = tAmt;
    }

    public String gettAmtF() {
        return tAmtF;
    }

    public void settAmtF(String tAmtF) {
        this.tAmtF = tAmtF;
    }

    public String getTxam() {
        return txam;
    }

    public void setTxam(String txam) {
        this.txam = txam;
    }

    public String getTxamF() {
        return txamF;
    }

    public void setTxamF(String txamF) {
        this.txamF = txamF;
    }

    public String getTpam() {
        return tpam;
    }

    public void setTpam(String tpam) {
        this.tpam = tpam;
    }

    public String getTpamF() {
        return tpamF;
    }

    public void setTpamF(String tpamF) {
        this.tpamF = tpamF;
    }

    public String getcAmt() {
        return cAmt;
    }

    public void setcAmt(String cAmt) {
        this.cAmt = cAmt;
    }

    public String getcAmtF() {
        return cAmtF;
    }

    public void setcAmtF(String cAmtF) {
        this.cAmtF = cAmtF;
    }

    public String getThirdPartyRRN() {
        return thirdPartyRRN;
    }

    public void setThirdPartyRRN(String thirdPartyRRN) {
        this.thirdPartyRRN = thirdPartyRRN;
    }
}
