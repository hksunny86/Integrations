package com.inov8.integration.channel.eocean.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.inov8.integration.enums.I8SBResponseCodeEnum;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement(name = "Messages")
@XmlAccessorType(XmlAccessType.NONE)
public class Messages extends Response implements Serializable {

    @XmlElement(name = "Message")
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();

        if (this.getMessage().getStatuscode().equals("200")){

            i8SBSwitchControllerResponseVO.setMessageID(this.getMessage().getMessageID());
            i8SBSwitchControllerResponseVO.setDateTime(this.getMessage().getDate());
            i8SBSwitchControllerResponseVO.setMessage(this.getMessage().getText());
            i8SBSwitchControllerResponseVO.setMobilePhone(this.getMessage().getReceiver());
            i8SBSwitchControllerResponseVO.setShortCode(this.getMessage().getShortCode());
            i8SBSwitchControllerResponseVO.setStatus(this.getMessage().getStatus());
            i8SBSwitchControllerResponseVO.setStatusCode(this.getMessage().getStatuscode());
            i8SBSwitchControllerResponseVO.setResponseCode(I8SBResponseCodeEnum.PROCESSED.getValue());
        }

        return i8SBSwitchControllerResponseVO;
    }
}
@XmlRootElement(name = "Message")
@XmlAccessorType(XmlAccessType.NONE)
class Message {

    @XmlElement(name = "MessageID")
    private String messageID;
    @XmlElement(name = "Date")
    private String date;
    @XmlElement(name = "Text")
    private String text;
    @XmlElement(name = "receiver")
    private String receiver;
    @XmlElement(name = "ShortCode")
    private String shortCode;
    @XmlElement(name = "status")
    private String status;
    @XmlElement(name = "statuscode")
    private String statuscode;


    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }
}
