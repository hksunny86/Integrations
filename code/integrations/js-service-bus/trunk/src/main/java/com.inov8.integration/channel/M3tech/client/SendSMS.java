
package com.inov8.integration.channel.M3tech.client;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MobileNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MsgId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SMS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MsgHeader" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SMSType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HandsetPort" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SMSChannel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Telco" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "userId",
    "password",
    "mobileNo",
    "msgId",
    "sms",
    "msgHeader",
    "smsType",
    "handsetPort",
    "smsChannel",
    "telco"
})
@XmlRootElement(name = "SendSMS")
public class SendSMS
    implements Serializable
{

    private final static long serialVersionUID = 1L;
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

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the mobileNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the value of the mobileNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileNo(String value) {
        this.mobileNo = value;
    }

    /**
     * Gets the value of the msgId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * Sets the value of the msgId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgId(String value) {
        this.msgId = value;
    }

    /**
     * Gets the value of the sms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMS() {
        return sms;
    }

    /**
     * Sets the value of the sms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMS(String value) {
        this.sms = value;
    }

    /**
     * Gets the value of the msgHeader property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgHeader() {
        return msgHeader;
    }

    /**
     * Sets the value of the msgHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgHeader(String value) {
        this.msgHeader = value;
    }

    /**
     * Gets the value of the smsType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMSType() {
        return smsType;
    }

    /**
     * Sets the value of the smsType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMSType(String value) {
        this.smsType = value;
    }

    /**
     * Gets the value of the handsetPort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHandsetPort() {
        return handsetPort;
    }

    /**
     * Sets the value of the handsetPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHandsetPort(String value) {
        this.handsetPort = value;
    }

    /**
     * Gets the value of the smsChannel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMSChannel() {
        return smsChannel;
    }

    /**
     * Sets the value of the smsChannel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMSChannel(String value) {
        this.smsChannel = value;
    }

    /**
     * Gets the value of the telco property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelco() {
        return telco;
    }

    /**
     * Sets the value of the telco property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelco(String value) {
        this.telco = value;
    }

}
