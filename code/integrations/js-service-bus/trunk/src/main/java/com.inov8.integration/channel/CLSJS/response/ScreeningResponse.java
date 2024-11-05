package com.inov8.integration.channel.CLSJS.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.util.List;

//import javax.xml.bind.annotation.XmlElement;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ProcessingCode",
        "TransmissionDatetime",
        "SystemsTraceAuditNumber",
        "ResponseCode",
        "ResponseDetails",
        "CaseStatus",
        "ImportStatus",
        "IsHit",
        "ScreeningStatus",
        "TotalCWL",
        "TotalGWL",
        "TotalPEPEDD",
        "TotalPrivate",
//        "message",
//        "code",
//        "reqId",
//        "data"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScreeningResponse extends Response {

    @JsonProperty("ProcessingCode")
    private String processingCode;
    @JsonProperty("TransmissionDatetime")
    private String transmissionDatetime;
    @JsonProperty("SystemsTraceAuditNumber")
    private String systemsTraceAuditNumber;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDetails")
    private List<String> responseDetails;
    @JsonProperty("CaseStatus")
    private String caseStatus;
    @JsonProperty("ImportStatus")
    private String importStatus;
    @JsonProperty("IsHit")
    private String isHit;
    @JsonProperty("ScreeningStatus")
    private String screeningStatus;
    @JsonProperty("TotalCWL")
    private String totalCWL;
    @JsonProperty("TotalGWL")
    private String totalGWL;
    @JsonProperty("TotalPEPEDD")
    private String totalPEPEDD;
    @JsonProperty("TotalPrivate")
    private String totalPrivate;

//    private String message;
//    private String code;
//    private String reqId;
//    private ClsScreeningData data;

//    private String requestID;
//    private String caseId;
//    private String caseStatus;
//    private String importStatus;
//    private String isHit;
//    private String screeningStatus;
//    private String totalCWL;
//    private String totalGWL;
//    private String totalPEPEDD;
//    private String totalPrivate;
//
//    public String getRequestID() {
//        return requestID;
//    }
//
//    public void setRequestID(String requestID) {
//        this.requestID = requestID;
//    }
//
//    public String getCaseId() {
//        return caseId;
//    }
//
//    public void setCaseId(String caseId) {
//        this.caseId = caseId;
//    }
//
//    public String getCaseStatus() {
//        return caseStatus;
//    }
//
//    public void setCaseStatus(String caseStatus) {
//        this.caseStatus = caseStatus;
//    }
//
//    public String getImportStatus() {
//        return importStatus;
//    }
//
//    public void setImportStatus(String importStatus) {
//        this.importStatus = importStatus;
//    }
//
//    public String getIsHit() {
//        return isHit;
//    }
//
//    public void setIsHit(String isHit) {
//        this.isHit = isHit;
//    }
//
//    public String getScreeningStatus() {
//        return screeningStatus;
//    }
//
//    public void setScreeningStatus(String screeningStatus) {
//        this.screeningStatus = screeningStatus;
//    }
//
//    public String getTotalCWL() {
//        return totalCWL;
//    }
//
//    public void setTotalCWL(String totalCWL) {
//        this.totalCWL = totalCWL;
//    }
//
//    public String getTotalGWL() {
//        return totalGWL;
//    }
//
//    public void setTotalGWL(String totalGWL) {
//        this.totalGWL = totalGWL;
//    }
//
//    public String getTotalPEPEDD() {
//        return totalPEPEDD;
//    }
//
//    public void setTotalPEPEDD(String totalPEPEDD) {
//        this.totalPEPEDD = totalPEPEDD;
//    }
//
//    public String getTotalPrivate() {
//        return totalPrivate;
//    }
//
//    public void setTotalPrivate(String totalPrivate) {
//        this.totalPrivate = totalPrivate;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getReqId() {
//        return reqId;
//    }
//
//    public void setReqId(String reqId) {
//        this.reqId = reqId;
//    }
//
//    public ClsScreeningData getData() {
//        return data;
//    }
//
//    public void setData(ClsScreeningData data) {
//        this.data = data;
//    }


    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getTransmissionDatetime() {
        return transmissionDatetime;
    }

    public void setTransmissionDatetime(String transmissionDatetime) {
        this.transmissionDatetime = transmissionDatetime;
    }

    public String getSystemsTraceAuditNumber() {
        return systemsTraceAuditNumber;
    }

    public void setSystemsTraceAuditNumber(String systemsTraceAuditNumber) {
        this.systemsTraceAuditNumber = systemsTraceAuditNumber;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public List<String> getResponseDetails() {
        return responseDetails;
    }

    public void setResponseDetails(List<String> responseDetails) {
        this.responseDetails = responseDetails;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
    }

    public String getIsHit() {
        return isHit;
    }

    public void setIsHit(String isHit) {
        this.isHit = isHit;
    }

    public String getScreeningStatus() {
        return screeningStatus;
    }

    public void setScreeningStatus(String screeningStatus) {
        this.screeningStatus = screeningStatus;
    }

    public String getTotalCWL() {
        return totalCWL;
    }

    public void setTotalCWL(String totalCWL) {
        this.totalCWL = totalCWL;
    }

    public String getTotalGWL() {
        return totalGWL;
    }

    public void setTotalGWL(String totalGWL) {
        this.totalGWL = totalGWL;
    }

    public String getTotalPEPEDD() {
        return totalPEPEDD;
    }

    public void setTotalPEPEDD(String totalPEPEDD) {
        this.totalPEPEDD = totalPEPEDD;
    }

    public String getTotalPrivate() {
        return totalPrivate;
    }

    public void setTotalPrivate(String totalPrivate) {
        this.totalPrivate = totalPrivate;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (this.getResponseCode() != null && this.getResponseCode().equals("00")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
        }
        i8SBSwitchControllerResponseVO.setResponseCode(this.getResponseCode());
        if (this.getResponseDetails() != null && this.getResponseDetails().size() > 0) {
            i8SBSwitchControllerResponseVO.setDescription(this.getResponseDetails().get(0));
        }
//        i8SBSwitchControllerResponseVO.setRequestId(this.getReqId());
//        i8SBSwitchControllerResponseVO.setCaseId(this.getData().getCaseId());
        i8SBSwitchControllerResponseVO.setCaseStatus(this.getCaseStatus());
        i8SBSwitchControllerResponseVO.setImportStatus(this.getImportStatus());
        i8SBSwitchControllerResponseVO.setIsHit(this.getIsHit());
        i8SBSwitchControllerResponseVO.setScreeningStatus(this.getScreeningStatus());
        i8SBSwitchControllerResponseVO.setTotalCWL(this.getTotalCWL());
        i8SBSwitchControllerResponseVO.setTotalGWL(this.getTotalGWL());
        i8SBSwitchControllerResponseVO.setTotalPEPEDD(this.getTotalPEPEDD());
        i8SBSwitchControllerResponseVO.setTotalPrivate(this.getTotalPrivate());
//        i8SBSwitchControllerResponseVO.setIsHit(this.getData().getIsHit());
//        i8SBSwitchControllerResponseVO.setTotalCWL(this.getData().getTotalCWL());


        return i8SBSwitchControllerResponseVO;
    }
}
