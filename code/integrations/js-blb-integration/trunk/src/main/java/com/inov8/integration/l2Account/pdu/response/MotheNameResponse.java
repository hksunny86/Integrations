package com.inov8.integration.l2Account.pdu.response;

import com.inov8.integration.vo.MotherName;

import java.util.List;

public class MotheNameResponse {
    private String responseCode;
    private String responseDescription;
    private String responseDateTime;
    private List<MotherName> motherNameList;

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }
    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public List<MotherName> getMotherNameList() {
        return motherNameList;
    }

    public void setMotherNameList(List<MotherName> motherNameList) {
        this.motherNameList = motherNameList;
    }
}
