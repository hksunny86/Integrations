
package com.inov8.integration.middleware.abl.client;

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
 *         &lt;element name="CustomerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="UtilityComapanyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ConsumerNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="amountpaid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FromAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FromAccountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RRN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "customerID",
    "utilityComapanyId",
    "consumerNo",
    "amountpaid",
    "fromAccount",
    "fromAccountType",
    "rrn"
})
@XmlRootElement(name = "BillPayment")
public class BillPayment
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CustomerID", nillable = true)
    protected String customerID;
    @XmlElement(name = "UtilityComapanyId", nillable = true)
    protected String utilityComapanyId;
    @XmlElement(name = "ConsumerNo", nillable = true)
    protected String consumerNo;
    @XmlElement(nillable = true)
    protected String amountpaid;
    @XmlElement(name = "FromAccount", nillable = true)
    protected String fromAccount;
    @XmlElement(name = "FromAccountType", nillable = true)
    protected String fromAccountType;
    @XmlElement(name = "RRN", nillable = true)
    protected String rrn;

    /**
     * Gets the value of the customerID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerID() {
        return customerID;
    }

    /**
     * Sets the value of the customerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerID(String value) {
        this.customerID = value;
    }

    /**
     * Gets the value of the utilityComapanyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUtilityComapanyId() {
        return utilityComapanyId;
    }

    /**
     * Sets the value of the utilityComapanyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUtilityComapanyId(String value) {
        this.utilityComapanyId = value;
    }

    /**
     * Gets the value of the consumerNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumerNo() {
        return consumerNo;
    }

    /**
     * Sets the value of the consumerNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumerNo(String value) {
        this.consumerNo = value;
    }

    /**
     * Gets the value of the amountpaid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmountpaid() {
        return amountpaid;
    }

    /**
     * Sets the value of the amountpaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmountpaid(String value) {
        this.amountpaid = value;
    }

    /**
     * Gets the value of the fromAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromAccount() {
        return fromAccount;
    }

    /**
     * Sets the value of the fromAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromAccount(String value) {
        this.fromAccount = value;
    }

    /**
     * Gets the value of the fromAccountType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromAccountType() {
        return fromAccountType;
    }

    /**
     * Sets the value of the fromAccountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromAccountType(String value) {
        this.fromAccountType = value;
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

}
