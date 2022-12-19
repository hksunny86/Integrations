
package com.inov8.integration.channel.BOPBLB.client;

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
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="bankMnemonic" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CNIC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MobileNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RRN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DebitCardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TransactionType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SegmentID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Agent_Location" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Agent_City" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Terminal_Id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "userName",
    "password",
    "bankMnemonic",
    "cnic",
    "mobileNo",
    "rrn",
    "debitCardNumber",
    "transactionType",
    "segmentID",
    "agentLocation",
    "agentCity",
    "terminalId"
})
@XmlRootElement(name = "AccountRegisterationInquiry")
public class AccountRegisterationInquiry
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected String userName;
    protected String password;
    protected String bankMnemonic;
    @XmlElement(name = "CNIC")
    protected String cnic;
    @XmlElement(name = "MobileNo")
    protected String mobileNo;
    @XmlElement(name = "RRN")
    protected String rrn;
    @XmlElement(name = "DebitCardNumber")
    protected String debitCardNumber;
    @XmlElement(name = "TransactionType")
    protected String transactionType;
    @XmlElement(name = "SegmentID")
    protected String segmentID;
    @XmlElement(name = "Agent_Location")
    protected String agentLocation;
    @XmlElement(name = "Agent_City")
    protected String agentCity;
    @XmlElement(name = "Terminal_Id")
    protected String terminalId;

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
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
     * Gets the value of the bankMnemonic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankMnemonic() {
        return bankMnemonic;
    }

    /**
     * Sets the value of the bankMnemonic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankMnemonic(String value) {
        this.bankMnemonic = value;
    }

    /**
     * Gets the value of the cnic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNIC() {
        return cnic;
    }

    /**
     * Sets the value of the cnic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNIC(String value) {
        this.cnic = value;
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
     * Gets the value of the rrn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRRN() {
        return rrn;
    }

    /**
     * Sets the value of the rrn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRRN(String value) {
        this.rrn = value;
    }

    /**
     * Gets the value of the debitCardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebitCardNumber() {
        return debitCardNumber;
    }

    /**
     * Sets the value of the debitCardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebitCardNumber(String value) {
        this.debitCardNumber = value;
    }

    /**
     * Gets the value of the transactionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the value of the transactionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionType(String value) {
        this.transactionType = value;
    }

    /**
     * Gets the value of the segmentID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSegmentID() {
        return segmentID;
    }

    /**
     * Sets the value of the segmentID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSegmentID(String value) {
        this.segmentID = value;
    }

    /**
     * Gets the value of the agentLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentLocation() {
        return agentLocation;
    }

    /**
     * Sets the value of the agentLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentLocation(String value) {
        this.agentLocation = value;
    }

    /**
     * Gets the value of the agentCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentCity() {
        return agentCity;
    }

    /**
     * Sets the value of the agentCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentCity(String value) {
        this.agentCity = value;
    }

    /**
     * Gets the value of the terminalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminalId() {
        return terminalId;
    }

    /**
     * Sets the value of the terminalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminalId(String value) {
        this.terminalId = value;
    }

}
