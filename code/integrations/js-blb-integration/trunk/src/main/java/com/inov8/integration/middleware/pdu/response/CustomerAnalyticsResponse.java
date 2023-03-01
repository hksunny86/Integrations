package com.inov8.integration.middleware.pdu.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Rrn",
        "ResponseCode",
        "ResponseDescription",
        "ResponseDateTime",
        "StatusCode",
        "MessageCode",
        "Message",
        "ReportDate",
        "ReportTime",
        "Name",
        "Cnic",
        "City",
        "NoOfActiveAccounts",
        "TotalOutstandingBalance",
        "DOB",
        "PLUS_30_24M",
        "PLUS_60_24M",
        "PLUS_90_24M",
        "PLUS_120_24M",
        "PLUS_150_24M",
        "PLUS_180_24M",
        "Write_Off",
        "DisclaimerText",
        "Remarks",
        "HashData",
})
public class CustomerAnalyticsResponse {

    @JsonProperty("Rrn")
    private String rrn;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
    @JsonProperty("ResponseDateTime")
    private String responseDateTime;
    @JsonProperty("StatusCode")
    private String statusCode;
    @JsonProperty("MessageCode")
    private String messageCode;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("ReportDate")
    private String reportDate;
    @JsonProperty("ReportTime")
    private String reportTime;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Cnic")
    private String cnic;
    @JsonProperty("City")
    private String city;
    @JsonProperty("NoOfActiveAccounts")
    private String noOfActiveAccounts;
    @JsonProperty("TotalOutstandingBalance")
    private String totalOutstandingBalance;
    @JsonProperty("DOB")
    private String dob;
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
    @JsonProperty("Write_Off")
    private String writeOff;
    @JsonProperty("DisclaimerText")
    private String disclaimerText;
    @JsonProperty("Remarks")
    private String remarks;
    @JsonProperty("HashData")
    private String hashData;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
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

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNoOfActiveAccounts() {
        return noOfActiveAccounts;
    }

    public void setNoOfActiveAccounts(String noOfActiveAccounts) {
        this.noOfActiveAccounts = noOfActiveAccounts;
    }

    public String getTotalOutstandingBalance() {
        return totalOutstandingBalance;
    }

    public void setTotalOutstandingBalance(String totalOutstandingBalance) {
        this.totalOutstandingBalance = totalOutstandingBalance;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPlus3024m() {
        return plus3024m;
    }

    public void setPlus3024m(String plus3024m) {
        this.plus3024m = plus3024m;
    }

    public String getPlus6024m() {
        return plus6024m;
    }

    public void setPlus6024m(String plus6024m) {
        this.plus6024m = plus6024m;
    }

    public String getPlus9024m() {
        return plus9024m;
    }

    public void setPlus9024m(String plus9024m) {
        this.plus9024m = plus9024m;
    }

    public String getPlus12024m() {
        return plus12024m;
    }

    public void setPlus12024m(String plus12024m) {
        this.plus12024m = plus12024m;
    }

    public String getPlus15024m() {
        return plus15024m;
    }

    public void setPlus15024m(String plus15024m) {
        this.plus15024m = plus15024m;
    }

    public String getPlus18024m() {
        return plus18024m;
    }

    public void setPlus18024m(String plus18024m) {
        this.plus18024m = plus18024m;
    }

    public String getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(String writeOff) {
        this.writeOff = writeOff;
    }

    public String getDisclaimerText() {
        return disclaimerText;
    }

    public void setDisclaimerText(String disclaimerText) {
        this.disclaimerText = disclaimerText;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }
}