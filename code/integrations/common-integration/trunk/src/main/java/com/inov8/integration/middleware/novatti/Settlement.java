
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for Settlement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Settlement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="settledCount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="supplierName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="totalValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="totalMargin" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="totalTax" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="settlementFile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="settlementId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Settlement", propOrder = {
    "date",
    "settledCount",
    "supplierName",
    "agentName",
    "totalValue",
    "totalMargin",
    "totalTax",
    "settlementFile",
    "settlementId"
})
public class Settlement implements Serializable {

    protected String date;
    protected String settledCount;
    protected String supplierName;
    protected String agentName;
    protected Double totalValue;
    protected Double totalMargin;
    protected Double totalTax;
    protected String settlementFile;
    protected String settlementId;

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
     * Gets the value of the settledCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettledCount() {
        return settledCount;
    }

    /**
     * Sets the value of the settledCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettledCount(String value) {
        this.settledCount = value;
    }

    /**
     * Gets the value of the supplierName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * Sets the value of the supplierName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupplierName(String value) {
        this.supplierName = value;
    }

    /**
     * Gets the value of the agentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentName() {
        return agentName;
    }

    /**
     * Sets the value of the agentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentName(String value) {
        this.agentName = value;
    }

    /**
     * Gets the value of the totalValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTotalValue() {
        return totalValue;
    }

    /**
     * Sets the value of the totalValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTotalValue(Double value) {
        this.totalValue = value;
    }

    /**
     * Gets the value of the totalMargin property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTotalMargin() {
        return totalMargin;
    }

    /**
     * Sets the value of the totalMargin property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTotalMargin(Double value) {
        this.totalMargin = value;
    }

    /**
     * Gets the value of the totalTax property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTotalTax() {
        return totalTax;
    }

    /**
     * Sets the value of the totalTax property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTotalTax(Double value) {
        this.totalTax = value;
    }

    /**
     * Gets the value of the settlementFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettlementFile() {
        return settlementFile;
    }

    /**
     * Sets the value of the settlementFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettlementFile(String value) {
        this.settlementFile = value;
    }

    /**
     * Gets the value of the settlementId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettlementId() {
        return settlementId;
    }

    /**
     * Sets the value of the settlementId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettlementId(String value) {
        this.settlementId = value;
    }

}
