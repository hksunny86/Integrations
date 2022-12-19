
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountStatementFilter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountStatementFilter"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AdvanceSearch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DateRange" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FromAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NumberofTransaction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Particular" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="StatementPeriod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ToAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountStatementFilter", propOrder = {
    "advanceSearch",
    "dateRange",
    "fromAmount",
    "numberofTransaction",
    "particular",
    "statementPeriod",
    "toAmount"
})
public class AccountStatementFilter
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "AdvanceSearch", nillable = true)
    protected String advanceSearch;
    @XmlElement(name = "DateRange", nillable = true)
    protected String dateRange;
    @XmlElement(name = "FromAmount", nillable = true)
    protected String fromAmount;
    @XmlElement(name = "NumberofTransaction", nillable = true)
    protected String numberofTransaction;
    @XmlElement(name = "Particular", nillable = true)
    protected String particular;
    @XmlElement(name = "StatementPeriod", nillable = true)
    protected String statementPeriod;
    @XmlElement(name = "ToAmount", nillable = true)
    protected String toAmount;

    /**
     * Gets the value of the advanceSearch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdvanceSearch() {
        return advanceSearch;
    }

    /**
     * Sets the value of the advanceSearch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdvanceSearch(String value) {
        this.advanceSearch = value;
    }

    /**
     * Gets the value of the dateRange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateRange() {
        return dateRange;
    }

    /**
     * Sets the value of the dateRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateRange(String value) {
        this.dateRange = value;
    }

    /**
     * Gets the value of the fromAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromAmount() {
        return fromAmount;
    }

    /**
     * Sets the value of the fromAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromAmount(String value) {
        this.fromAmount = value;
    }

    /**
     * Gets the value of the numberofTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberofTransaction() {
        return numberofTransaction;
    }

    /**
     * Sets the value of the numberofTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberofTransaction(String value) {
        this.numberofTransaction = value;
    }

    /**
     * Gets the value of the particular property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParticular() {
        return particular;
    }

    /**
     * Sets the value of the particular property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParticular(String value) {
        this.particular = value;
    }

    /**
     * Gets the value of the statementPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatementPeriod() {
        return statementPeriod;
    }

    /**
     * Sets the value of the statementPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatementPeriod(String value) {
        this.statementPeriod = value;
    }

    /**
     * Gets the value of the toAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToAmount() {
        return toAmount;
    }

    /**
     * Sets the value of the toAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToAmount(String value) {
        this.toAmount = value;
    }

}
