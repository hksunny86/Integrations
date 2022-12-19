package com.inov8.integration.middleware.pdu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by inov8 on 10/25/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BeneficiaryRegistration implements Serializable {

    private static final long serialVersionUID = 1054088476952196151L;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Mobile_No")
    private String mobileNo;

    @JsonProperty("Form_No")
    private String formNo;

    @JsonProperty("Sim_ID")
    private String simId;

    @JsonProperty("District_Id")
    private String districtId;

    @JsonProperty("District_Name")
    private String districtName;

    @JsonProperty("Tehsil_Id")
    private String tehsilId;

    @JsonProperty("Tehsil_Name")
    private String tehsilName;

    @JsonProperty("CNIC_No")
    private String cnicNo;

    @JsonProperty("Account_No")
    private String accountNo;

    @JsonProperty("Card_No")
    private String cardNo;

    @JsonProperty("Registration_Date_Time")
    private String registerationDateTime;

    @JsonProperty("Center_ID")
    private String centerId;

    @JsonProperty("Center_Name")
    private String centerName;

    @JsonProperty("Agent_ID")
    private String agentId;

    @JsonProperty("Agent_Name")
    private String agentName;

    @JsonProperty("Agent_Contact_No")
    private String agentContactNo;

    @JsonProperty("Current_Balance")
    private String currentBalance;

    @JsonProperty("Segment_Type")
    private String segmentType;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }

    public String getSimId() {
        return simId;
    }

    public void setSimId(String simId) {
        this.simId = simId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getTehsilId() {
        return tehsilId;
    }

    public void setTehsilId(String tehsilId) {
        this.tehsilId = tehsilId;
    }

    public String getTehsilName() {
        return tehsilName;
    }

    public void setTehsilName(String tehsilName) {
        this.tehsilName = tehsilName;
    }

    public String getCnicNo() {
        return cnicNo;
    }

    public void setCnicNo(String cnicNo) {
        this.cnicNo = cnicNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getRegisterationDateTime() {
        return registerationDateTime;
    }

    public void setRegisterationDateTime(String registerationDateTime) {
        this.registerationDateTime = registerationDateTime;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentContactNo() {
        return agentContactNo;
    }

    public void setAgentContactNo(String agentContactNo) {
        this.agentContactNo = agentContactNo;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(String segmentType) {
        this.segmentType = segmentType;
    }
}