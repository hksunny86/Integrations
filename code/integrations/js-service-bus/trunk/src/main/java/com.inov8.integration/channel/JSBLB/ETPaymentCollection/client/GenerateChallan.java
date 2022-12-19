
package com.inov8.integration.channel.JSBLB.ETPaymentCollection.client;

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
 *         &lt;element name="AssesementNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VehicleRegistrationNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CustomerMobileNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AssesementTotalAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ChallanStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BankMnemonic" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="authId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TranDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TranTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "assesementNumber",
    "vehicleRegistrationNumber",
    "customerMobileNumber",
    "assesementTotalAmount",
    "challanStatus",
    "bankMnemonic",
    "authId",
    "tranDate",
    "tranTime"
})
@XmlRootElement(name = "GenerateChallan")
public class GenerateChallan {

    @XmlElement(name = "AssesementNumber")
    protected String assesementNumber;
    @XmlElement(name = "VehicleRegistrationNumber")
    protected String vehicleRegistrationNumber;
    @XmlElement(name = "CustomerMobileNumber")
    protected String customerMobileNumber;
    @XmlElement(name = "AssesementTotalAmount")
    protected String assesementTotalAmount;
    @XmlElement(name = "ChallanStatus")
    protected String challanStatus;
    @XmlElement(name = "BankMnemonic")
    protected String bankMnemonic;
    protected String authId;
    @XmlElement(name = "TranDate")
    protected String tranDate;
    @XmlElement(name = "TranTime")
    protected String tranTime;

    /**
     * Gets the value of the assesementNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssesementNumber() {
        return assesementNumber;
    }

    /**
     * Sets the value of the assesementNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssesementNumber(String value) {
        this.assesementNumber = value;
    }

    /**
     * Gets the value of the vehicleRegistrationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    /**
     * Sets the value of the vehicleRegistrationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVehicleRegistrationNumber(String value) {
        this.vehicleRegistrationNumber = value;
    }

    /**
     * Gets the value of the customerMobileNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerMobileNumber() {
        return customerMobileNumber;
    }

    /**
     * Sets the value of the customerMobileNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerMobileNumber(String value) {
        this.customerMobileNumber = value;
    }

    /**
     * Gets the value of the assesementTotalAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssesementTotalAmount() {
        return assesementTotalAmount;
    }

    /**
     * Sets the value of the assesementTotalAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssesementTotalAmount(String value) {
        this.assesementTotalAmount = value;
    }

    /**
     * Gets the value of the challanStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChallanStatus() {
        return challanStatus;
    }

    /**
     * Sets the value of the challanStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChallanStatus(String value) {
        this.challanStatus = value;
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
     * Gets the value of the authId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthId() {
        return authId;
    }

    /**
     * Sets the value of the authId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthId(String value) {
        this.authId = value;
    }

    /**
     * Gets the value of the tranDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranDate() {
        return tranDate;
    }

    /**
     * Sets the value of the tranDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranDate(String value) {
        this.tranDate = value;
    }

    /**
     * Gets the value of the tranTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTranTime() {
        return tranTime;
    }

    /**
     * Sets the value of the tranTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTranTime(String value) {
        this.tranTime = value;
    }

}
