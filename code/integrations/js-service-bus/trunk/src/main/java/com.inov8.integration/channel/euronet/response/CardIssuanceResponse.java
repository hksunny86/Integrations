package com.inov8.integration.channel.euronet.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CardIssuanceResponse extends Response {

    private static Logger logger = LoggerFactory.getLogger(CardIssuanceResponse.class.getSimpleName());

    private String outPartID;
    private String outCard;
    private String outResponseCode;
    private String messageNumber;
    private String messageText;
    private String outEmbCard;
    private String outToken;
    private String outBIN;
    private String outExpDt;
    private String outIssDt;
    private String outEmbName;

    public String getOutPartID() {
        return outPartID;
    }

    public void setOutPartID(String outPartID) {
        this.outPartID = outPartID;
    }

    public String getOutCard() {
        return outCard;
    }

    public void setOutCard(String outCard) {
        this.outCard = outCard;
    }

    public String getOutResponseCode() {
        return outResponseCode;
    }

    public void setOutResponseCode(String outResponseCode) {
        this.outResponseCode = outResponseCode;
    }

    public String getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getOutEmbCard() {
        return outEmbCard;
    }

    public void setOutEmbCard(String outEmbCard) {
        this.outEmbCard = outEmbCard;
    }

    public String getOutToken() {
        return outToken;
    }

    public void setOutToken(String outToken) {
        this.outToken = outToken;
    }

    public String getOutBIN() {
        return outBIN;
    }

    public void setOutBIN(String outBIN) {
        this.outBIN = outBIN;
    }

    public String getOutExpDt() {
        return outExpDt;
    }

    public void setOutExpDt(String outExpDt) {
        this.outExpDt = outExpDt;
    }

    public String getOutIssDt() {
        return outIssDt;
    }

    public void setOutIssDt(String outIssDt) {
        this.outIssDt = outIssDt;
    }

    public String getOutEmbName() {
        return outEmbName;
    }

    public void setOutEmbName(String outEmbName) {
        this.outEmbName = outEmbName;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        if (this.getOutResponseCode() != null) {
            if (this.getOutResponseCode().equalsIgnoreCase("00")) {
                i8SBSwitchControllerResponseVO.setResponseCode("00");
            } else {
                i8SBSwitchControllerResponseVO.setResponseCode(this.getOutResponseCode());
            }
            i8SBSwitchControllerResponseVO.setOutPartID(this.getOutPartID());
            i8SBSwitchControllerResponseVO.setOutCard(this.getOutCard());
            i8SBSwitchControllerResponseVO.setMessageNumber(this.getMessageNumber());
            i8SBSwitchControllerResponseVO.setMessageText(this.getMessageText());
            i8SBSwitchControllerResponseVO.setOutEmbCard(this.getOutEmbCard());
            i8SBSwitchControllerResponseVO.setOutToken(this.getOutToken());
            i8SBSwitchControllerResponseVO.setOutBIN(this.getOutBIN());
            i8SBSwitchControllerResponseVO.setOutExpDt(this.getOutExpDt());
            i8SBSwitchControllerResponseVO.setOutIssDt(this.getOutIssDt());
            i8SBSwitchControllerResponseVO.setOutEmbName(this.getOutEmbName());
        }

        return i8SBSwitchControllerResponseVO;
    }
}