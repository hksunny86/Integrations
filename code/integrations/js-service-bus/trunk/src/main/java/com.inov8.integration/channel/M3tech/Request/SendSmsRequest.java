package com.inov8.integration.channel.M3tech.Request;

import com.inov8.integration.config.PropertyReader;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SendSmsRequest extends Request {

    @XmlElement(name = "UserId")
    protected String userId;
    @XmlElement(name = "Password")
    protected String password;
    @XmlElement(name = "MobileNo")
    protected String mobileNo;
    @XmlElement(name = "MsgId")
    protected String msgId;
    @XmlElement(name = "SMS")
    protected String sms;
    @XmlElement(name = "MsgHeader")
    protected String msgHeader;
    @XmlElement(name = "SMSType")
    protected String smsType;
    @XmlElement(name = "HandsetPort")
    protected String handsetPort;
    @XmlElement(name = "SMSChannel")
    protected String smsChannel;
    @XmlElement(name = "Telco")
    protected String telco;

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        String message = i8SBSwitchControllerRequestVO.getSmsText();
        String regex = "(?i) Your available balance is: \\w+\\.? \\d+\\.?";
        String replacedMessage = message.replaceAll(regex, " ");

        this.setUserId(PropertyReader.getProperty("m3Tech.username"));
        this.setPassword(PropertyReader.getProperty("m3Tech.password"));
        this.setMobileNo(i8SBSwitchControllerRequestVO.getRecieverMobileNo());
        this.setSms(replacedMessage);
        this.setMsgId(i8SBSwitchControllerRequestVO.getSTAN());
        this.setMsgHeader(PropertyReader.getProperty("m3Tech.message.header"));
        this.setSmsType("");
        this.setHandsetPort("");
        this.setTelco("");
        this.setSmsChannel("");
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(String msgHeader) {
        this.msgHeader = msgHeader;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getHandsetPort() {
        return handsetPort;
    }

    public void setHandsetPort(String handsetPort) {
        this.handsetPort = handsetPort;
    }

    public String getSmsChannel() {
        return smsChannel;
    }

    public void setSmsChannel(String smsChannel) {
        this.smsChannel = smsChannel;
    }

    public String getTelco() {
        return telco;
    }

    public void setTelco(String telco) {
        this.telco = telco;
    }
}
