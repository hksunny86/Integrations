package com.inov8.agentmate.model;

import java.io.Serializable;

public class KhidmatCardRegistrationModel implements Serializable {

    private String cnic;
    private String pitbApproved;
    private String cnicExp;
    private String cmob;
    private String nadraSessionId;
    private String thirdPartyCustSegmentId;
    private String tehsilId;
    private String districtId;
    private String cname;
    private String cdob;
    private String isBvsReq;

    public KhidmatCardRegistrationModel() {
    }

    public KhidmatCardRegistrationModel(String cnic, String pitbApproved, String cnicExp, String cmob, String nadraSessionId, String thirdPartyCustSegmentId, String tehsilId, String districtId, String cname, String cdob, String isBvsReq) {
        this.cnic = cnic;
        this.pitbApproved = pitbApproved;
        this.cnicExp = cnicExp;
        this.cmob = cmob;
        this.nadraSessionId = nadraSessionId;
        this.thirdPartyCustSegmentId = thirdPartyCustSegmentId;
        this.tehsilId = tehsilId;
        this.districtId = districtId;
        this.cname = cname;
        this.cdob = cdob;
        this.isBvsReq = isBvsReq;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getPitbApproved() {
        return pitbApproved;
    }

    public void setPitbApproved(String pitbApproved) {
        this.pitbApproved = pitbApproved;
    }

    public String getCnicExp() {
        return cnicExp;
    }

    public void setCnicExp(String cnicExp) {
        this.cnicExp = cnicExp;
    }

    public String getCmob() {
        return cmob;
    }

    public void setCmob(String cmob) {
        this.cmob = cmob;
    }

    public String getNadraSessionId() {
        return nadraSessionId;
    }

    public void setNadraSessionId(String nadraSessionId) {
        this.nadraSessionId = nadraSessionId;
    }

    public String getThirdPartyCustSegmentId() {
        return thirdPartyCustSegmentId;
    }

    public void setThirdPartyCustSegmentId(String thirdPartyCustSegmentId) {
        this.thirdPartyCustSegmentId = thirdPartyCustSegmentId;
    }

    public String getTehsilId() {
        return tehsilId;
    }

    public void setTehsilId(String tehsilId) {
        this.tehsilId = tehsilId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCdob() {
        return cdob;
    }

    public void setCdob(String cdob) {
        this.cdob = cdob;
    }

    public String getIsBvsReq() {
        return isBvsReq;
    }

    public void setIsBvsReq(String isBvsReq) {
        this.isBvsReq = isBvsReq;
    }
}
