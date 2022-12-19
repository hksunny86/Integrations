
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BPScheduleTransactionList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BPScheduleTransactionList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BatchId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BillingCompany" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ConsumerNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Frequency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FromAccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NoOfOccurrence" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RecurEndType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="StartDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BPScheduleTransactionList", propOrder = {
    "amount",
    "batchId",
    "billingCompany",
    "consumerNumber",
    "endDate",
    "frequency",
    "fromAccountNumber",
    "noOfOccurrence",
    "recurEndType",
    "startDate"
})
public class BPScheduleTransactionList
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Amount", nillable = true)
    protected String amount;
    @XmlElement(name = "BatchId", nillable = true)
    protected String batchId;
    @XmlElement(name = "BillingCompany", nillable = true)
    protected String billingCompany;
    @XmlElement(name = "ConsumerNumber", nillable = true)
    protected String consumerNumber;
    @XmlElement(name = "EndDate", nillable = true)
    protected String endDate;
    @XmlElement(name = "Frequency", nillable = true)
    protected String frequency;
    @XmlElement(name = "FromAccountNumber", nillable = true)
    protected String fromAccountNumber;
    @XmlElement(name = "NoOfOccurrence", nillable = true)
    protected String noOfOccurrence;
    @XmlElement(name = "RecurEndType", nillable = true)
    protected String recurEndType;
    @XmlElement(name = "StartDate", nillable = true)
    protected String startDate;

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmount(String value) {
        this.amount = value;
    }

    /**
     * Gets the value of the batchId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatchId() {
        return batchId;
    }

    /**
     * Sets the value of the batchId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatchId(String value) {
        this.batchId = value;
    }

    /**
     * Gets the value of the billingCompany property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingCompany() {
        return billingCompany;
    }

    /**
     * Sets the value of the billingCompany property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingCompany(String value) {
        this.billingCompany = value;
    }

    /**
     * Gets the value of the consumerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsumerNumber() {
        return consumerNumber;
    }

    /**
     * Sets the value of the consumerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsumerNumber(String value) {
        this.consumerNumber = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndDate(String value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the frequency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Sets the value of the frequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrequency(String value) {
        this.frequency = value;
    }

    /**
     * Gets the value of the fromAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    /**
     * Sets the value of the fromAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromAccountNumber(String value) {
        this.fromAccountNumber = value;
    }

    /**
     * Gets the value of the noOfOccurrence property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoOfOccurrence() {
        return noOfOccurrence;
    }

    /**
     * Sets the value of the noOfOccurrence property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoOfOccurrence(String value) {
        this.noOfOccurrence = value;
    }

    /**
     * Gets the value of the recurEndType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecurEndType() {
        return recurEndType;
    }

    /**
     * Sets the value of the recurEndType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecurEndType(String value) {
        this.recurEndType = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartDate(String value) {
        this.startDate = value;
    }

}
