package com.inov8.integration.channel.CLSJS.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.webservice.clsVO.ClsScreeningData;

//import javax.xml.bind.annotation.XmlElement;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "message",
        "code",
        "reqId",
        "data"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScreeningResponse extends Response {


    private String message;
    private String code;
    private String reqId;
    private ClsScreeningData data;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public ClsScreeningData getData() {
        return data;
    }

    public void setData(ClsScreeningData data) {
        this.data = data;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (this.getCode() != null && this.getCode().equals("00")) {
            i8SBSwitchControllerResponseVO.setResponseCode("00");
        }
        i8SBSwitchControllerResponseVO.setResponseCode(this.getCode());
        i8SBSwitchControllerResponseVO.setDescription(this.getMessage());
        i8SBSwitchControllerResponseVO.setRequestId(this.getReqId());
        i8SBSwitchControllerResponseVO.setCaseId(this.getData().getCaseId());
        i8SBSwitchControllerResponseVO.setCaseStatus(this.getData().getCaseStatus());
        i8SBSwitchControllerResponseVO.setTotalGWL(this.getData().getTotalGWL());
        i8SBSwitchControllerResponseVO.setTotalPEPEDD(this.getData().getTotalPEPEDD());
        i8SBSwitchControllerResponseVO.setTotalPrivate(this.getData().getTotalPrivate());
        i8SBSwitchControllerResponseVO.setImportStatus(this.getData().getImportStatus());
//        i8SBSwitchControllerResponseVO.setIsHit(this.getData().getIsHit());
//        i8SBSwitchControllerResponseVO.setScreeningStatus(this.getData().getScreeningStatus());
//        i8SBSwitchControllerResponseVO.setTotalCWL(this.getData().getTotalCWL());


        return i8SBSwitchControllerResponseVO;
    }
}
