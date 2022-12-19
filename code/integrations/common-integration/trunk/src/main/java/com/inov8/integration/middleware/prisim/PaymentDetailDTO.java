
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PaymentDetailDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentDetailDTO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NoOfOccurrence" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PurposeDetails" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PurposeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RecurEndType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RecurPattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "PaymentDetailDTO", propOrder = {
    "amount",
    "endDate",
    "noOfOccurrence",
    "purposeDetails",
    "purposeId",
    "recurEndType",
    "recurPattern",
    "startDate"
})
public class PaymentDetailDTO
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Amount", nillable = true)
    protected String amount;
    @XmlElement(name = "EndDate", nillable = true)
    protected String endDate;
    @XmlElement(name = "NoOfOccurrence", nillable = true)
    protected String noOfOccurrence;
    @XmlElement(name = "PurposeDetails", nillable = true)
    protected String purposeDetails;
    @XmlElement(name = "PurposeId", nillable = true)
    protected String purposeId;
    @XmlElement(name = "RecurEndType", nillable = true)
    protected String recurEndType;
    @XmlElement(name = "RecurPattern", nillable = true)
    protected String recurPattern;
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
     * Gets the value of the purposeDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurposeDetails() {
        return purposeDetails;
    }

    /**
     * Sets the value of the purposeDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurposeDetails(String value) {
        this.purposeDetails = value;
    }

    /**
     * Gets the value of the purposeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPurposeId() {
        return purposeId;
    }

    /**
     * Sets the value of the purposeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPurposeId(String value) {
        this.purposeId = value;
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
     * Gets the value of the recurPattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecurPattern() {
        return recurPattern;
    }

    /**
     * Sets the value of the recurPattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecurPattern(String value) {
        this.recurPattern = value;
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
