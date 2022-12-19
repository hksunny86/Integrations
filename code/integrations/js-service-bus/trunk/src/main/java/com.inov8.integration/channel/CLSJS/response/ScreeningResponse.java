package com.inov8.integration.channel.CLSJS.response;

import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.XmlElement;

public class ScreeningResponse extends Response{


    private String requestID;
    private String caseId;
    private String caseStatus;
    private String importStatus;
    private String isHit;
    private String screeningStatus;
    private String totalCWL;
    private String totalGWL;
    private String totalPEPEDD;
    private String totalPrivate;

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
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

        i8SBSwitchControllerResponseVO.setRequestId(this.getRequestID());
        i8SBSwitchControllerResponseVO.setCaseId(this.getCaseId());
        i8SBSwitchControllerResponseVO.setCaseStatus(this.getCaseStatus());
        i8SBSwitchControllerResponseVO.setImportStatus(this.getImportStatus());
        i8SBSwitchControllerResponseVO.setIsHit(this.getIsHit());
        i8SBSwitchControllerResponseVO.setScreeningStatus(this.getScreeningStatus());
        i8SBSwitchControllerResponseVO.setTotalCWL(this.getTotalCWL());
        i8SBSwitchControllerResponseVO.setTotalGWL(this.getTotalGWL());
        i8SBSwitchControllerResponseVO.setTotalPEPEDD(this.getTotalPEPEDD());
        i8SBSwitchControllerResponseVO.setTotalPrivate(this.getTotalPrivate());


        return i8SBSwitchControllerResponseVO;
    }
}
