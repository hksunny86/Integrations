
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapResponseBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="topupValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="supplierName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="supplierCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="currencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="buyValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="sellValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="buyCurrencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="wholesaleBuyValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="wholesaleBuyCurrencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="totalTax" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="quoteIsEstimate" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="valueAddProducts" type="{http://soap.api.novatti.com/types}Products" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "topupValue",
    "supplierName",
    "supplierCode",
    "currencyCode",
    "buyValue",
    "sellValue",
    "buyCurrencyCode",
    "wholesaleBuyValue",
    "wholesaleBuyCurrencyCode",
    "totalTax",
    "quoteIsEstimate",
    "valueAddProducts"
})
@XmlRootElement(name = "SoapSubscriberTopupQuoteResponse")
public class SoapSubscriberTopupQuoteResponse
    extends SoapResponseBase
{

    protected Double topupValue;
    protected String supplierName;
    protected String supplierCode;
    protected String currencyCode;
    protected Double buyValue;
    protected Double sellValue;
    protected String buyCurrencyCode;
    protected Double wholesaleBuyValue;
    protected String wholesaleBuyCurrencyCode;
    protected Double totalTax;
    protected Boolean quoteIsEstimate;
    protected Products valueAddProducts;

    /**
     * Gets the value of the topupValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTopupValue() {
        return topupValue;
    }

    /**
     * Sets the value of the topupValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTopupValue(Double value) {
        this.topupValue = value;
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
     * Gets the value of the supplierCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupplierCode() {
        return supplierCode;
    }

    /**
     * Sets the value of the supplierCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupplierCode(String value) {
        this.supplierCode = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the buyValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getBuyValue() {
        return buyValue;
    }

    /**
     * Sets the value of the buyValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setBuyValue(Double value) {
        this.buyValue = value;
    }

    /**
     * Gets the value of the sellValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSellValue() {
        return sellValue;
    }

    /**
     * Sets the value of the sellValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSellValue(Double value) {
        this.sellValue = value;
    }

    /**
     * Gets the value of the buyCurrencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuyCurrencyCode() {
        return buyCurrencyCode;
    }

    /**
     * Sets the value of the buyCurrencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuyCurrencyCode(String value) {
        this.buyCurrencyCode = value;
    }

    /**
     * Gets the value of the wholesaleBuyValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getWholesaleBuyValue() {
        return wholesaleBuyValue;
    }

    /**
     * Sets the value of the wholesaleBuyValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setWholesaleBuyValue(Double value) {
        this.wholesaleBuyValue = value;
    }

    /**
     * Gets the value of the wholesaleBuyCurrencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWholesaleBuyCurrencyCode() {
        return wholesaleBuyCurrencyCode;
    }

    /**
     * Sets the value of the wholesaleBuyCurrencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWholesaleBuyCurrencyCode(String value) {
        this.wholesaleBuyCurrencyCode = value;
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
     * Gets the value of the quoteIsEstimate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isQuoteIsEstimate() {
        return quoteIsEstimate;
    }

    /**
     * Sets the value of the quoteIsEstimate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setQuoteIsEstimate(Boolean value) {
        this.quoteIsEstimate = value;
    }

    /**
     * Gets the value of the valueAddProducts property.
     * 
     * @return
     *     possible object is
     *     {@link Products }
     *     
     */
    public Products getValueAddProducts() {
        return valueAddProducts;
    }

    /**
     * Sets the value of the valueAddProducts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Products }
     *     
     */
    public void setValueAddProducts(Products value) {
        this.valueAddProducts = value;
    }

}
