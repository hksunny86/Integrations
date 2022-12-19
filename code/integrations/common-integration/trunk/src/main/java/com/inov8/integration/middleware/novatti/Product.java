
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for Product complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Product"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ean" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="typeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="subTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="supplierName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="supplierCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="countryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="countryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="currencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="denominations" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="minValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="maxValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="increment" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="sellMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="commisionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="buyValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="buyMinValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="buyMaxValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="buyIncrement" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="buyDenominations" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="buyCurrencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sellValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="sellMinValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="sellMaxValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="sellIncrement" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="sellDenominations" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sellCurrencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="totalTax" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="documents" type="{http://soap.api.novatti.com/types}Documents" minOccurs="0"/&gt;
 *         &lt;element name="extraProperties" type="{http://soap.api.novatti.com/types}Map" minOccurs="0"/&gt;
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="margins" type="{http://soap.api.novatti.com/types}Margins" minOccurs="0"/&gt;
 *         &lt;element name="wholesaleBuyMinValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="wholesaleBuyMaxValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="wholesaleBuyIncrement" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="wholesaleBuyDenominations" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="wholesaleBuyCurrencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="transTypeId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="transTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="transTypeDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Product", propOrder = {
    "code",
    "ean",
    "description",
    "typeCode",
    "subTypeCode",
    "supplierName",
    "supplierCode",
    "countryName",
    "countryCode",
    "currencyCode",
    "denominations",
    "minValue",
    "maxValue",
    "increment",
    "sellMode",
    "commisionId",
    "buyValue",
    "buyMinValue",
    "buyMaxValue",
    "buyIncrement",
    "buyDenominations",
    "buyCurrencyCode",
    "sellValue",
    "sellMinValue",
    "sellMaxValue",
    "sellIncrement",
    "sellDenominations",
    "sellCurrencyCode",
    "totalTax",
    "documents",
    "extraProperties",
    "value",
    "margins",
    "wholesaleBuyMinValue",
    "wholesaleBuyMaxValue",
    "wholesaleBuyIncrement",
    "wholesaleBuyDenominations",
    "wholesaleBuyCurrencyCode",
    "transTypeId",
    "transTypeCode",
    "transTypeDescription"
})
public class Product implements Serializable {

    protected String code;
    protected String ean;
    protected String description;
    protected String typeCode;
    protected String subTypeCode;
    protected String supplierName;
    protected String supplierCode;
    protected String countryName;
    protected String countryCode;
    protected String currencyCode;
    protected String denominations;
    protected Double minValue;
    protected Double maxValue;
    protected Double increment;
    protected String sellMode;
    protected String commisionId;
    protected Double buyValue;
    protected Double buyMinValue;
    protected Double buyMaxValue;
    protected Double buyIncrement;
    protected String buyDenominations;
    protected String buyCurrencyCode;
    protected Double sellValue;
    protected Double sellMinValue;
    protected Double sellMaxValue;
    protected Double sellIncrement;
    protected String sellDenominations;
    protected String sellCurrencyCode;
    protected Double totalTax;
    protected Documents documents;
    protected Map extraProperties;
    protected Double value;
    protected Margins margins;
    protected Double wholesaleBuyMinValue;
    protected Double wholesaleBuyMaxValue;
    protected Double wholesaleBuyIncrement;
    protected String wholesaleBuyDenominations;
    protected String wholesaleBuyCurrencyCode;
    protected Integer transTypeId;
    protected String transTypeCode;
    protected String transTypeDescription;

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the ean property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEan() {
        return ean;
    }

    /**
     * Sets the value of the ean property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEan(String value) {
        this.ean = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the typeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * Sets the value of the typeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeCode(String value) {
        this.typeCode = value;
    }

    /**
     * Gets the value of the subTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubTypeCode() {
        return subTypeCode;
    }

    /**
     * Sets the value of the subTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubTypeCode(String value) {
        this.subTypeCode = value;
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
     * Gets the value of the denominations property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominations() {
        return denominations;
    }

    /**
     * Sets the value of the denominations property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominations(String value) {
        this.denominations = value;
    }

    /**
     * Gets the value of the minValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMinValue() {
        return minValue;
    }

    /**
     * Sets the value of the minValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMinValue(Double value) {
        this.minValue = value;
    }

    /**
     * Gets the value of the maxValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the value of the maxValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMaxValue(Double value) {
        this.maxValue = value;
    }

    /**
     * Gets the value of the increment property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getIncrement() {
        return increment;
    }

    /**
     * Sets the value of the increment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setIncrement(Double value) {
        this.increment = value;
    }

    /**
     * Gets the value of the sellMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellMode() {
        return sellMode;
    }

    /**
     * Sets the value of the sellMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellMode(String value) {
        this.sellMode = value;
    }

    /**
     * Gets the value of the commisionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommisionId() {
        return commisionId;
    }

    /**
     * Sets the value of the commisionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommisionId(String value) {
        this.commisionId = value;
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
     * Gets the value of the buyMinValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getBuyMinValue() {
        return buyMinValue;
    }

    /**
     * Sets the value of the buyMinValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setBuyMinValue(Double value) {
        this.buyMinValue = value;
    }

    /**
     * Gets the value of the buyMaxValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getBuyMaxValue() {
        return buyMaxValue;
    }

    /**
     * Sets the value of the buyMaxValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setBuyMaxValue(Double value) {
        this.buyMaxValue = value;
    }

    /**
     * Gets the value of the buyIncrement property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getBuyIncrement() {
        return buyIncrement;
    }

    /**
     * Sets the value of the buyIncrement property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setBuyIncrement(Double value) {
        this.buyIncrement = value;
    }

    /**
     * Gets the value of the buyDenominations property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuyDenominations() {
        return buyDenominations;
    }

    /**
     * Sets the value of the buyDenominations property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuyDenominations(String value) {
        this.buyDenominations = value;
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
     * Gets the value of the sellMinValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSellMinValue() {
        return sellMinValue;
    }

    /**
     * Sets the value of the sellMinValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSellMinValue(Double value) {
        this.sellMinValue = value;
    }

    /**
     * Gets the value of the sellMaxValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSellMaxValue() {
        return sellMaxValue;
    }

    /**
     * Sets the value of the sellMaxValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSellMaxValue(Double value) {
        this.sellMaxValue = value;
    }

    /**
     * Gets the value of the sellIncrement property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSellIncrement() {
        return sellIncrement;
    }

    /**
     * Sets the value of the sellIncrement property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSellIncrement(Double value) {
        this.sellIncrement = value;
    }

    /**
     * Gets the value of the sellDenominations property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellDenominations() {
        return sellDenominations;
    }

    /**
     * Sets the value of the sellDenominations property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellDenominations(String value) {
        this.sellDenominations = value;
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
     * Gets the value of the documents property.
     * 
     * @return
     *     possible object is
     *     {@link Documents }
     *     
     */
    public Documents getDocuments() {
        return documents;
    }

    /**
     * Sets the value of the documents property.
     * 
     * @param value
     *     allowed object is
     *     {@link Documents }
     *     
     */
    public void setDocuments(Documents value) {
        this.documents = value;
    }

    /**
     * Gets the value of the extraProperties property.
     * 
     * @return
     *     possible object is
     *     {@link Map }
     *     
     */
    public Map getExtraProperties() {
        return extraProperties;
    }

    /**
     * Sets the value of the extraProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link Map }
     *     
     */
    public void setExtraProperties(Map value) {
        this.extraProperties = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Gets the value of the margins property.
     * 
     * @return
     *     possible object is
     *     {@link Margins }
     *     
     */
    public Margins getMargins() {
        return margins;
    }

    /**
     * Sets the value of the margins property.
     * 
     * @param value
     *     allowed object is
     *     {@link Margins }
     *     
     */
    public void setMargins(Margins value) {
        this.margins = value;
    }

    /**
     * Gets the value of the wholesaleBuyMinValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getWholesaleBuyMinValue() {
        return wholesaleBuyMinValue;
    }

    /**
     * Sets the value of the wholesaleBuyMinValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setWholesaleBuyMinValue(Double value) {
        this.wholesaleBuyMinValue = value;
    }

    /**
     * Gets the value of the wholesaleBuyMaxValue property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getWholesaleBuyMaxValue() {
        return wholesaleBuyMaxValue;
    }

    /**
     * Sets the value of the wholesaleBuyMaxValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setWholesaleBuyMaxValue(Double value) {
        this.wholesaleBuyMaxValue = value;
    }

    /**
     * Gets the value of the wholesaleBuyIncrement property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getWholesaleBuyIncrement() {
        return wholesaleBuyIncrement;
    }

    /**
     * Sets the value of the wholesaleBuyIncrement property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setWholesaleBuyIncrement(Double value) {
        this.wholesaleBuyIncrement = value;
    }

    /**
     * Gets the value of the wholesaleBuyDenominations property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWholesaleBuyDenominations() {
        return wholesaleBuyDenominations;
    }

    /**
     * Sets the value of the wholesaleBuyDenominations property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWholesaleBuyDenominations(String value) {
        this.wholesaleBuyDenominations = value;
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
     * Gets the value of the transTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTransTypeId() {
        return transTypeId;
    }

    /**
     * Sets the value of the transTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTransTypeId(Integer value) {
        this.transTypeId = value;
    }

    /**
     * Gets the value of the transTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransTypeCode() {
        return transTypeCode;
    }

    /**
     * Sets the value of the transTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransTypeCode(String value) {
        this.transTypeCode = value;
    }

    /**
     * Gets the value of the transTypeDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransTypeDescription() {
        return transTypeDescription;
    }

    /**
     * Sets the value of the transTypeDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransTypeDescription(String value) {
        this.transTypeDescription = value;
    }

}
