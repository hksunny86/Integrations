
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
 *         &lt;element name="ChallanNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VehicleRegistrationNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CustomerMobileNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ChallanTotalAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ChallanStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BankMnemonic" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Cancel_authId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Cancel_TranDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Cancel_TranTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "challanNumber",
    "vehicleRegistrationNumber",
    "customerMobileNumber",
    "challanTotalAmount",
    "challanStatus",
    "bankMnemonic",
    "cancelAuthId",
    "cancelTranDate",
    "cancelTranTime"
})
@XmlRootElement(name = "CancelChallan")
public class CancelChallan {

    @XmlElement(name = "ChallanNumber")
    protected String challanNumber;
    @XmlElement(name = "VehicleRegistrationNumber")
    protected String vehicleRegistrationNumber;
    @XmlElement(name = "CustomerMobileNumber")
    protected String customerMobileNumber;
    @XmlElement(name = "ChallanTotalAmount")
    protected String challanTotalAmount;
    @XmlElement(name = "ChallanStatus")
    protected String challanStatus;
    @XmlElement(name = "BankMnemonic")
    protected String bankMnemonic;
    @XmlElement(name = "Cancel_authId")
    protected String cancelAuthId;
    @XmlElement(name = "Cancel_TranDate")
    protected String cancelTranDate;
    @XmlElement(name = "Cancel_TranTime")
    protected String cancelTranTime;

    /**
     * Gets the value of the challanNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChallanNumber() {
        return challanNumber;
    }

    /**
     * Sets the value of the challanNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChallanNumber(String value) {
        this.challanNumber = value;
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
     * Gets the value of the challanTotalAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChallanTotalAmount() {
        return challanTotalAmount;
    }

    /**
     * Sets the value of the challanTotalAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChallanTotalAmount(String value) {
        this.challanTotalAmount = value;
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
     * Gets the value of the cancelAuthId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancelAuthId() {
        return cancelAuthId;
    }

    /**
     * Sets the value of the cancelAuthId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancelAuthId(String value) {
        this.cancelAuthId = value;
    }

    /**
     * Gets the value of the cancelTranDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancelTranDate() {
        return cancelTranDate;
    }

    /**
     * Sets the value of the cancelTranDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancelTranDate(String value) {
        this.cancelTranDate = value;
    }

    /**
     * Gets the value of the cancelTranTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancelTranTime() {
        return cancelTranTime;
    }

    /**
     * Sets the value of the cancelTranTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancelTranTime(String value) {
        this.cancelTranTime = value;
    }

}
