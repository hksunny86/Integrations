
package com.inov8.integration.middleware.bop.avenza;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MiniStatementBlk complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MiniStatementBlk"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Reference" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Debit" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Credit" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MiniStatementBlk", propOrder = {
    "date",
    "reference",
    "debit",
    "credit"
})
public class MiniStatementBlk {

    @XmlElement(name = "Date", required = true)
    protected String date;
    @XmlElement(name = "Reference", required = true)
    protected String reference;
    @XmlElement(name = "Debit", required = true)
    protected String debit;
    @XmlElement(name = "Credit", required = true)
    protected String credit;

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDate(String value) {
        this.date = value;
    }

    /**
     * Gets the value of the reference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReference(String value) {
        this.reference = value;
    }

    /**
     * Gets the value of the debit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebit() {
        return debit;
    }

    /**
     * Sets the value of the debit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebit(String value) {
        this.debit = value;
    }

    /**
     * Gets the value of the credit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCredit() {
        return credit;
    }

    /**
     * Sets the value of the credit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCredit(String value) {
        this.credit = value;
    }

}
