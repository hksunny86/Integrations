
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for VoucherRedemptionDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VoucherRedemptionDetail"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pin" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="serialNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="productCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="productDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="productTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="productSubTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="currencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="countryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="countryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="supplierCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="supplierName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="faceValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="buyValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="buyCurrencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sellValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="sellCurrencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="totalTax" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="reference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VoucherRedemptionDetail", propOrder = {
    "pin",
    "serialNo",
    "productCode",
    "productDescription",
    "productTypeCode",
    "productSubTypeCode",
    "currencyCode",
    "countryCode",
    "countryName",
    "supplierCode",
    "supplierName",
    "faceValue",
    "buyValue",
    "buyCurrencyCode",
    "sellValue",
    "sellCurrencyCode",
    "totalTax",
    "reference",
    "timestamp"
})
public class VoucherRedemptionDetail implements Serializable {

    @XmlElement(required = true)
    protected String pin;
    protected String serialNo;
    protected String productCode;
    protected String productDescription;
    protected String productTypeCode;
    protected String productSubTypeCode;
    protected String currencyCode;
    protected String countryCode;
    protected String countryName;
    protected String supplierCode;
    protected String supplierName;
    protected String faceValue;
    protected Double buyValue;
    protected String buyCurrencyCode;
    protected Double sellValue;
    protected String sellCurrencyCode;
    protected Double totalTax;
    protected String reference;
    protected String timestamp;

    /**
     * Gets the value of the pin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPin() {
        return pin;
    }

    /**
     * Sets the value of the pin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPin(String value) {
        this.pin = value;
    }

    /**
     * Gets the value of the serialNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerialNo() {
        return serialNo;
    }

    /**
     * Sets the value of the serialNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerialNo(String value) {
        this.serialNo = value;
    }

    /**
     * Gets the value of the productCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Sets the value of the productCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductCode(String value) {
        this.productCode = value;
    }

    /**
     * Gets the value of the productDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * Sets the value of the productDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductDescription(String value) {
        this.productDescription = value;
    }

    /**
     * Gets the value of the productTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductTypeCode() {
        return productTypeCode;
    }

    /**
     * Sets the value of the productTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductTypeCode(String value) {
        this.productTypeCode = value;
    }

    /**
     * Gets the value of the productSubTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductSubTypeCode() {
        return productSubTypeCode;
    }

    /**
     * Sets the value of the productSubTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductSubTypeCode(String value) {
        this.productSubTypeCode = value;
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
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    /**
     * Gets the value of the countryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the value of the countryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryName(String value) {
        this.countryName = value;
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
     * Gets the value of the faceValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaceValue() {
        return faceValue;
    }

    /**
     * Sets the value of the faceValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaceValue(String value) {
        this.faceValue = value;
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
     * Gets the value of the sellCurrencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellCurrencyCode() {
        return sellCurrencyCode;
    }

    /**
     * Sets the value of the sellCurrencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellCurrencyCode(String value) {
        this.sellCurrencyCode = value;
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
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestamp(String value) {
        this.timestamp = value;
    }

}
