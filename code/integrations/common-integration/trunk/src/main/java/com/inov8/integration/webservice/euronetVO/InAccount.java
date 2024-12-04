package com.inov8.integration.webservice.euronetVO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InAccount implements Serializable {
    private static final long serialVersionUID = 5824473488070382311L;
    private String inAccTyp;
    private String inAccNum;
    private String inAccDesc;
    private String inAccBrch;
    private String inAccCurCd;
    private String inAccRouting;
    private String inAccSts;
    private String inAccWvChrg;
    private String inPriFlag;
    private String unspecified;

    public String getInAccTyp() {
        return inAccTyp;
    }

    public void setInAccTyp(String inAccTyp) {
        this.inAccTyp = inAccTyp;
    }

    public String getInAccNum() {
        return inAccNum;
    }

    public void setInAccNum(String inAccNum) {
        this.inAccNum = inAccNum;
    }

    public String getInAccDesc() {
        return inAccDesc;
    }

    public void setInAccDesc(String inAccDesc) {
        this.inAccDesc = inAccDesc;
    }

    public String getInAccBrch() {
        return inAccBrch;
    }

    public void setInAccBrch(String inAccBrch) {
        this.inAccBrch = inAccBrch;
    }

    public String getInAccCurCd() {
        return inAccCurCd;
    }

    public void setInAccCurCd(String inAccCurCd) {
        this.inAccCurCd = inAccCurCd;
    }

    public String getInAccRouting() {
        return inAccRouting;
    }

    public void setInAccRouting(String inAccRouting) {
        this.inAccRouting = inAccRouting;
    }

    public String getInAccSts() {
        return inAccSts;
    }

    public void setInAccSts(String inAccSts) {
        this.inAccSts = inAccSts;
    }

    public String getInAccWvChrg() {
        return inAccWvChrg;
    }

    public void setInAccWvChrg(String inAccWvChrg) {
        this.inAccWvChrg = inAccWvChrg;
    }

    public String getInPriFlag() {
        return inPriFlag;
    }

    public void setInPriFlag(String inPriFlag) {
        this.inPriFlag = inPriFlag;
    }

    public String getUnspecified() {
        return unspecified;
    }

    public void setUnspecified(String unspecified) {
        this.unspecified = unspecified;
    }
}
