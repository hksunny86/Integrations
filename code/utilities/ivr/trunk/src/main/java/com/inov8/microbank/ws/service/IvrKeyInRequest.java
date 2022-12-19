
package com.inov8.microbank.ws.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ivrKeyInRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ivrKeyInRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="commandId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="encryptionType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deviceTypeId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="callId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="terminalMobileNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="txAmount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="customerData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="otpPin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="txId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="txCode" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="customerMobileNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ivrKeyInRequest", propOrder = {
    "commandId",
    "encryptionType",
    "deviceTypeId",
    "callId",
    "terminalMobileNo",
    "txAmount",
    "customerData",
    "otpPin",
    "txId",
    "txCode",
    "customerMobileNo"
})
public class IvrKeyInRequest {

    protected String commandId;
    protected String encryptionType;
    protected Long deviceTypeId;
    protected String callId;
    protected String terminalMobileNo;
    protected Integer txAmount;
    protected String customerData;
    protected String otpPin;
    protected String txId;
    protected Long txCode;
    protected String customerMobileNo;

    /**
     * Gets the value of the commandId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommandId() {
        return commandId;
    }

    /**
     * Sets the value of the commandId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommandId(String value) {
        this.commandId = value;
    }

    /**
     * Gets the value of the encryptionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncryptionType() {
        return encryptionType;
    }

    /**
     * Sets the value of the encryptionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncryptionType(String value) {
        this.encryptionType = value;
    }

    /**
     * Gets the value of the deviceTypeId property.
     * 
     */
    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    /**
     * Sets the value of the deviceTypeId property.
     * 
     */
    public void setDeviceTypeId(Long value) {
        this.deviceTypeId = value;
    }

    /**
     * Gets the value of the callId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallId() {
        return callId;
    }

    /**
     * Sets the value of the callId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallId(String value) {
        this.callId = value;
    }

    /**
     * Gets the value of the terminalMobileNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminalMobileNo() {
        return terminalMobileNo;
    }

    /**
     * Sets the value of the terminalMobileNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminalMobileNo(String value) {
        this.terminalMobileNo = value;
    }

    /**
     * Gets the value of the txAmount property.
     * 
     */
    public Integer getTxAmount() {
        return txAmount;
    }

    /**
     * Sets the value of the txAmount property.
     * 
     */
    public void setTxAmount(Integer value) {
        this.txAmount = value;
    }

    /**
     * Gets the value of the customerData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerData() {
        return customerData;
    }

    /**
     * Sets the value of the customerData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerData(String value) {
        this.customerData = value;
    }

    /**
     * Gets the value of the otpPin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtpPin() {
        return otpPin;
    }

    /**
     * Sets the value of the otpPin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtpPin(String value) {
        this.otpPin = value;
    }

    /**
     * Gets the value of the txId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxId() {
        return txId;
    }

    /**
     * Sets the value of the txId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxId(String value) {
        this.txId = value;
    }

    /**
     * Gets the value of the txCode property.
     * 
     */
    public Long getTxCode() {
        return txCode;
    }

    /**
     * Sets the value of the txCode property.
     * 
     */
    public void setTxCode(Long value) {
        this.txCode = value;
    }

    /**
     * Gets the value of the customerMobileNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    /**
     * Sets the value of the customerMobileNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerMobileNo(String value) {
        this.customerMobileNo = value;
    }

	@Override
	public String toString()
	{
		return "IvrKeyInRequest [commandId=" + commandId + ", encryptionType="
				+ encryptionType + ", deviceTypeId=" + deviceTypeId
				+ ", callId=" + callId + ", terminalMobileNo="
				+ terminalMobileNo + ", txAmount=" + txAmount
				+ ", customerData=" + customerData + ", otpPin=" + otpPin
				+ ", txId=" + txId + ", txCode=" + txCode
				+ ", customerMobileNo=" + customerMobileNo + "]";
	}

}
