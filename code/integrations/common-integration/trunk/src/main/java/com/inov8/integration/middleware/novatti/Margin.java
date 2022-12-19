
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for Margin complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Margin"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="marginValue1" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="marginType1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="marginValue2" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="marginType2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="valueStart" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="valueEnd" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="adminFee" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="adminFeeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="processFee" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="processFeeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rebate" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="rebateType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Margin", propOrder = {
    "marginValue1",
    "marginType1",
    "marginValue2",
    "marginType2",
    "valueStart",
    "valueEnd",
    "adminFee",
    "adminFeeType",
    "processFee",
    "processFeeType",
    "rebate",
    "rebateType"
})
public class Margin implements Serializable {

    protected Double marginValue1;
    protected String marginType1;
    protected Double marginValue2;
    protected String marginType2;
    protected Double valueStart;
    protected Double valueEnd;
    protected Double adminFee;
    protected String adminFeeType;
    protected Double processFee;
    protected String processFeeType;
    protected Double rebate;
    protected String rebateType;

    /**
     * Gets the value of the marginValue1 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMarginValue1() {
        return marginValue1;
    }

    /**
     * Sets the value of the marginValue1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMarginValue1(Double value) {
        this.marginValue1 = value;
    }

    /**
     * Gets the value of the marginType1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarginType1() {
        return marginType1;
    }

    /**
     * Sets the value of the marginType1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarginType1(String value) {
        this.marginType1 = value;
    }

    /**
     * Gets the value of the marginValue2 property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMarginValue2() {
        return marginValue2;
    }

    /**
     * Sets the value of the marginValue2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMarginValue2(Double value) {
        this.marginValue2 = value;
    }

    /**
     * Gets the value of the marginType2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarginType2() {
        return marginType2;
    }

    /**
     * Sets the value of the marginType2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarginType2(String value) {
        this.marginType2 = value;
    }

    /**
     * Gets the value of the valueStart property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValueStart() {
        return valueStart;
    }

    /**
     * Sets the value of the valueStart property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValueStart(Double value) {
        this.valueStart = value;
    }

    /**
     * Gets the value of the valueEnd property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValueEnd() {
        return valueEnd;
    }

    /**
     * Sets the value of the valueEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValueEnd(Double value) {
        this.valueEnd = value;
    }

    /**
     * Gets the value of the adminFee property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAdminFee() {
        return adminFee;
    }

    /**
     * Sets the value of the adminFee property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAdminFee(Double value) {
        this.adminFee = value;
    }

    /**
     * Gets the value of the adminFeeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdminFeeType() {
        return adminFeeType;
    }

    /**
     * Sets the value of the adminFeeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdminFeeType(String value) {
        this.adminFeeType = value;
    }

    /**
     * Gets the value of the processFee property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getProcessFee() {
        return processFee;
    }

    /**
     * Sets the value of the processFee property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setProcessFee(Double value) {
        this.processFee = value;
    }

    /**
     * Gets the value of the processFeeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessFeeType() {
        return processFeeType;
    }

    /**
     * Sets the value of the processFeeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessFeeType(String value) {
        this.processFeeType = value;
    }

    /**
     * Gets the value of the rebate property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRebate() {
        return rebate;
    }

    /**
     * Sets the value of the rebate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRebate(Double value) {
        this.rebate = value;
    }

    /**
     * Gets the value of the rebateType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRebateType() {
        return rebateType;
    }

    /**
     * Sets the value of the rebateType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRebateType(String value) {
        this.rebateType = value;
    }

}
