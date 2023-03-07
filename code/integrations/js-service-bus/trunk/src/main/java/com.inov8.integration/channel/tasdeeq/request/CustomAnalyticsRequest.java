package com.inov8.integration.channel.tasdeeq.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "reportDataObj"
})
public class CustomAnalyticsRequest extends Request {

    @JsonProperty("reportDataObj")
    private ReportDataObj reportDataObj=new ReportDataObj();

    @JsonProperty("reportDataObj")
    public ReportDataObj getReportDataObj() {
        return reportDataObj;
    }

    @JsonProperty("reportDataObj")
    public void setReportDataObj(ReportDataObj reportDataObj) {
        this.reportDataObj = reportDataObj;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.getReportDataObj().setCnic(i8SBSwitchControllerRequestVO.getCNIC());
        this.getReportDataObj().setFullName(i8SBSwitchControllerRequestVO.getFullName());
        this.getReportDataObj().setDateOfBirth(i8SBSwitchControllerRequestVO.getDob());
        this.getReportDataObj().setCity(i8SBSwitchControllerRequestVO.getCity());
        this.getReportDataObj().setLoanAmount(i8SBSwitchControllerRequestVO.getAmount());
        this.getReportDataObj().setGender(i8SBSwitchControllerRequestVO.getGenderCode());
        this.getReportDataObj().setCurrentAddress(i8SBSwitchControllerRequestVO.getAddress());
        this.getReportDataObj().setFatherHusbandName(i8SBSwitchControllerRequestVO.getFatherName());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
//        if (StringUtils.isEmpty(this.getReportDataObj().getCnic())) {
//            throw new I8SBValidationException("[Failed] CNIC:" + this.getReportDataObj().getCnic());
//        }
//        if (StringUtils.isEmpty(this.getReportDataObj().getLoanAmount())) {
//            throw new I8SBValidationException("[Failed] Loan Amount:" + this.getReportDataObj().getLoanAmount());
//        }
        return true;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "CNIC",
        "fullName",
        "dateOfBirth",
        "city",
        "loanAmount",
        "gender",
        "currentAddress",
        "fatherHusbandName"
})
class ReportDataObj {

    @JsonProperty("CNIC")
    private String cnic;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("dateOfBirth")
    private String dateOfBirth;
    @JsonProperty("city")
    private String city;
    @JsonProperty("loanAmount")
    private String loanAmount;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("currentAddress")
    private String currentAddress;
    @JsonProperty("fatherHusbandName")
    private String fatherHusbandName;

    @JsonProperty("CNIC")
    public String getCnic() {
        return cnic;
    }

    @JsonProperty("CNIC")
    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    @JsonProperty("fullName")
    public String getFullName() {
        return fullName;
    }

    @JsonProperty("fullName")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @JsonProperty("dateOfBirth")
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @JsonProperty("dateOfBirth")
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("loanAmount")
    public String getLoanAmount() {
        return loanAmount;
    }

    @JsonProperty("loanAmount")
    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("currentAddress")
    public String getCurrentAddress() {
        return currentAddress;
    }

    @JsonProperty("currentAddress")
    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    @JsonProperty("fatherHusbandName")
    public String getFatherHusbandName() {
        return fatherHusbandName;
    }

    @JsonProperty("fatherHusbandName")
    public void setFatherHusbandName(String fatherHusbandName) {
        this.fatherHusbandName = fatherHusbandName;
    }

}
