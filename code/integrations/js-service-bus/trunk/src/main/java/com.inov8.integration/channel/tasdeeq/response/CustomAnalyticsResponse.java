package com.inov8.integration.channel.tasdeeq.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "statusCode",
        "messageCode",
        "message",
        "data"
})
public class CustomAnalyticsResponse extends Response implements Serializable {

    @JsonProperty("statusCode")
    private String statusCode;
    @JsonProperty("messageCode")
    private String messageCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private CustomAnalyticsData data;
    private String responseCode;
    private String responseDescription;

    @JsonProperty("statusCode")
    public String getStatusCode() {
        return statusCode;
    }

    @JsonProperty("statusCode")
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("messageCode")
    public String getMessageCode() {
        return messageCode;
    }

    @JsonProperty("messageCode")
    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("data")
    public CustomAnalyticsData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(CustomAnalyticsData data) {
        this.data = data;
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

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (this.getStatusCode().equals("111")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
            this.setResponseCode(i8SBSwitchControllerResponseVO.getResponseCode());
        } else {
            i8SBSwitchControllerResponseVO.setResponseCode(this.getMessageCode());
        }
        i8SBSwitchControllerResponseVO.setStatusCode(this.getStatusCode());
        i8SBSwitchControllerResponseVO.setMessageCode(this.getMessageCode());
        i8SBSwitchControllerResponseVO.setMessage(this.getMessage());
        i8SBSwitchControllerResponseVO.setReportDate(this.data.getReportDate());
        i8SBSwitchControllerResponseVO.setReportTime(this.data.getReportTime());
        i8SBSwitchControllerResponseVO.setRefNo(this.data.getRefNo());
        i8SBSwitchControllerResponseVO.setName(this.data.getName());
        i8SBSwitchControllerResponseVO.setCNIC(this.data.getCnic());
        i8SBSwitchControllerResponseVO.setCity(this.data.getCity());
        i8SBSwitchControllerResponseVO.setDob(this.data.getDob());
        i8SBSwitchControllerResponseVO.setNoOfActiveAccounts(String.valueOf(this.data.getNoOfActiveAccounts()));
        i8SBSwitchControllerResponseVO.setTotalOutstandingBalance(this.data.getTotalOutstandingBalance());
        i8SBSwitchControllerResponseVO.setPlus3024m(this.data.getPlus3024m());
        i8SBSwitchControllerResponseVO.setPlus6024m(this.data.getPlus6024m());
        i8SBSwitchControllerResponseVO.setPlus9024m(this.data.getPlus9024m());
        i8SBSwitchControllerResponseVO.setPlus12024m(this.data.getPlus12024m());
        i8SBSwitchControllerResponseVO.setPlus15024m(this.data.getPlus15024m());
        i8SBSwitchControllerResponseVO.setPlus18024m(this.data.getPlus18024m());
        i8SBSwitchControllerResponseVO.setWriteOff(this.data.getWriteOff());
        i8SBSwitchControllerResponseVO.setDisclaimerText(this.data.getDisclaimerText());
        i8SBSwitchControllerResponseVO.setRemarks(this.data.getRemarks());

        return i8SBSwitchControllerResponseVO;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "reportDate",
        "reportTime",
        "refNo",
        "NAME",
        "CNIC",
        "DOB",
        "CITY",
        "noOfActiveAccounts",
        "totalOutstandingBalance",
        "PLUS_30_24M",
        "PLUS_60_24M",
        "PLUS_90_24M",
        "PLUS_120_24M",
        "PLUS_150_24M",
        "PLUS_180_24M",
        "WRITE_OFF",
        "disclaimerText",
        "remarks"
})
class CustomAnalyticsData {

    @JsonProperty("reportDate")
    private String reportDate;
    @JsonProperty("reportTime")
    private String reportTime;
    @JsonProperty("refNo")
    private String refNo;
    @JsonProperty("NAME")
    private String name;
    @JsonProperty("CNIC")
    private String cnic;
    @JsonProperty("DOB")
    private String dob;
    @JsonProperty("CITY")
    private String city;
    @JsonProperty("noOfActiveAccounts")
    private Integer noOfActiveAccounts;
    @JsonProperty("totalOutstandingBalance")
    private String totalOutstandingBalance;
    @JsonProperty("PLUS_30_24M")
    private String plus3024m;
    @JsonProperty("PLUS_60_24M")
    private String plus6024m;
    @JsonProperty("PLUS_90_24M")
    private String plus9024m;
    @JsonProperty("PLUS_120_24M")
    private String plus12024m;
    @JsonProperty("PLUS_150_24M")
    private String plus15024m;
    @JsonProperty("PLUS_180_24M")
    private String plus18024m;
    @JsonProperty("WRITE_OFF")
    private String writeOff;
    @JsonProperty("disclaimerText")
    private String disclaimerText;
    @JsonProperty("remarks")
    private String remarks;

    @JsonProperty("reportDate")
    public String getReportDate() {
        return reportDate;
    }

    @JsonProperty("reportDate")
    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    @JsonProperty("reportTime")
    public String getReportTime() {
        return reportTime;
    }

    @JsonProperty("reportTime")
    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    @JsonProperty("refNo")
    public String getRefNo() {
        return refNo;
    }

    @JsonProperty("refNo")
    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    @JsonProperty("NAME")
    public String getName() {
        return name;
    }

    @JsonProperty("NAME")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("CNIC")
    public String getCnic() {
        return cnic;
    }

    @JsonProperty("CNIC")
    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @JsonProperty("DOB")
    public String getDob() {
        return dob;
    }

    @JsonProperty("DOB")
    public void setDob(String dob) {
        this.dob = dob;
    }

    @JsonProperty("CITY")
    public String getCity() {
        return city;
    }

    @JsonProperty("CITY")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("noOfActiveAccounts")
    public Integer getNoOfActiveAccounts() {
        return noOfActiveAccounts;
    }

    @JsonProperty("noOfActiveAccounts")
    public void setNoOfActiveAccounts(Integer noOfActiveAccounts) {
        this.noOfActiveAccounts = noOfActiveAccounts;
    }

    @JsonProperty("totalOutstandingBalance")
    public String getTotalOutstandingBalance() {
        return totalOutstandingBalance;
    }

    @JsonProperty("totalOutstandingBalance")
    public void setTotalOutstandingBalance(String totalOutstandingBalance) {
        this.totalOutstandingBalance = totalOutstandingBalance;
    }

    @JsonProperty("PLUS_30_24M")
    public String getPlus3024m() {
        return plus3024m;
    }

    @JsonProperty("PLUS_30_24M")
    public void setPlus3024m(String plus3024m) {
        this.plus3024m = plus3024m;
    }

    @JsonProperty("PLUS_60_24M")
    public String getPlus6024m() {
        return plus6024m;
    }

    @JsonProperty("PLUS_60_24M")
    public void setPlus6024m(String plus6024m) {
        this.plus6024m = plus6024m;
    }

    @JsonProperty("PLUS_90_24M")
    public String getPlus9024m() {
        return plus9024m;
    }

    @JsonProperty("PLUS_90_24M")
    public void setPlus9024m(String plus9024m) {
        this.plus9024m = plus9024m;
    }

    @JsonProperty("PLUS_120_24M")
    public String getPlus12024m() {
        return plus12024m;
    }

    @JsonProperty("PLUS_120_24M")
    public void setPlus12024m(String plus12024m) {
        this.plus12024m = plus12024m;
    }

    @JsonProperty("PLUS_150_24M")
    public String getPlus15024m() {
        return plus15024m;
    }

    @JsonProperty("PLUS_150_24M")
    public void setPlus15024m(String plus15024m) {
        this.plus15024m = plus15024m;
    }

    @JsonProperty("PLUS_180_24M")
    public String getPlus18024m() {
        return plus18024m;
    }

    @JsonProperty("PLUS_180_24M")
    public void setPlus18024m(String plus18024m) {
        this.plus18024m = plus18024m;
    }

    @JsonProperty("WRITE_OFF")
    public String getWriteOff() {
        return writeOff;
    }

    @JsonProperty("WRITE_OFF")
    public void setWriteOff(String writeOff) {
        this.writeOff = writeOff;
    }

    @JsonProperty("disclaimerText")
    public String getDisclaimerText() {
        return disclaimerText;
    }

    @JsonProperty("disclaimerText")
    public void setDisclaimerText(String disclaimerText) {
        this.disclaimerText = disclaimerText;
    }

    @JsonProperty("remarks")
    public String getRemarks() {
        return remarks;
    }

    @JsonProperty("remarks")
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
