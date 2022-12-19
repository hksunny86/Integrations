
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for Transaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Transaction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="reference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="mnoReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="resultCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="resultDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="msisdn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="transTypeId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="transTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="productCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="productDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="productTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="productSubTypeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="systemTimestamp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="countryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="countryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="supplierName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="supplierCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="topupValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="currencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="buyValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="sellValue" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="buyCurrencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tax" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="agentTransactionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="topupValueEstimated" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="reason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="parentTransactionId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="agentId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="agentParentId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="agentMasterDistributorId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="voucherSerial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="terminalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="margin" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="marginTax" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="adminFee" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="adminFeeTax" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="processFee" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="processFeeTax" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="adminRebate" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="adminRebateTax" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="isReversed" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isRefunded" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="openingWalletBalance" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="closingWalletBalance" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="linkedTransactions" type="{http://soap.api.novatti.com/types}Transactions" minOccurs="0"/&gt;
 *         &lt;element name="loyaltyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="transDatas" type="{http://soap.api.novatti.com/types}TransDatas" minOccurs="0"/&gt;
 *         &lt;element name="inResultCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="inResultDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Transaction", propOrder = {
    "id",
    "reference",
    "mnoReference",
    "resultCode",
    "resultDescription",
    "msisdn",
    "transTypeId",
    "transTypeCode",
    "productCode",
    "productDescription",
    "productTypeCode",
    "productSubTypeCode",
    "timestamp",
    "systemTimestamp",
    "countryCode",
    "countryName",
    "supplierName",
    "supplierCode",
    "topupValue",
    "value",
    "currencyCode",
    "buyValue",
    "sellValue",
    "buyCurrencyCode",
    "tax",
    "agentTransactionId",
    "topupValueEstimated",
    "reason",
    "description",
    "parentTransactionId",
    "agentId",
    "agentParentId",
    "agentMasterDistributorId",
    "user",
    "voucherSerial",
    "terminalCode",
    "margin",
    "marginTax",
    "adminFee",
    "adminFeeTax",
    "processFee",
    "processFeeTax",
    "adminRebate",
    "adminRebateTax",
    "isReversed",
    "isRefunded",
    "openingWalletBalance",
    "closingWalletBalance",
    "linkedTransactions",
    "loyaltyId",
    "transDatas",
    "inResultCode",
    "inResultDescription"
})
public class Transaction implements Serializable {

    protected int id;
    protected String reference;
    protected String mnoReference;
    protected String resultCode;
    protected String resultDescription;
    protected String msisdn;
    protected Integer transTypeId;
    protected String transTypeCode;
    protected String productCode;
    protected String productDescription;
    protected String productTypeCode;
    protected String productSubTypeCode;
    protected String timestamp;
    protected String systemTimestamp;
    protected String countryCode;
    protected String countryName;
    protected String supplierName;
    protected String supplierCode;
    protected Double topupValue;
    protected Double value;
    protected String currencyCode;
    protected Double buyValue;
    protected Double sellValue;
    protected String buyCurrencyCode;
    protected Double tax;
    protected String agentTransactionId;
    protected Boolean topupValueEstimated;
    protected String reason;
    protected String description;
    protected Integer parentTransactionId;
    protected Integer agentId;
    protected Integer agentParentId;
    protected Integer agentMasterDistributorId;
    protected String user;
    protected String voucherSerial;
    protected String terminalCode;
    protected Double margin;
    protected Double marginTax;
    protected Double adminFee;
    protected Double adminFeeTax;
    protected Double processFee;
    protected Double processFeeTax;
    protected Double adminRebate;
    protected Double adminRebateTax;
    protected Boolean isReversed;
    protected Boolean isRefunded;
    protected Double openingWalletBalance;
    protected Double closingWalletBalance;
    protected Transactions linkedTransactions;
    protected String loyaltyId;
    protected TransDatas transDatas;
    protected String inResultCode;
    protected String inResultDescription;

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
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
     * Gets the value of the mnoReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMnoReference() {
        return mnoReference;
    }

    /**
     * Sets the value of the mnoReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMnoReference(String value) {
        this.mnoReference = value;
    }

    /**
     * Gets the value of the resultCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * Sets the value of the resultCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultCode(String value) {
        this.resultCode = value;
    }

    /**
     * Gets the value of the resultDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultDescription() {
        return resultDescription;
    }

    /**
     * Sets the value of the resultDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultDescription(String value) {
        this.resultDescription = value;
    }

    /**
     * Gets the value of the msisdn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * Sets the value of the msisdn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsisdn(String value) {
        this.msisdn = value;
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

    /**
     * Gets the value of the systemTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemTimestamp() {
        return systemTimestamp;
    }

    /**
     * Sets the value of the systemTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemTimestamp(String value) {
        this.systemTimestamp = value;
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
     * Gets the value of the tax property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTax() {
        return tax;
    }

    /**
     * Sets the value of the tax property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTax(Double value) {
        this.tax = value;
    }

    /**
     * Gets the value of the agentTransactionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentTransactionId() {
        return agentTransactionId;
    }

    /**
     * Sets the value of the agentTransactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentTransactionId(String value) {
        this.agentTransactionId = value;
    }

    /**
     * Gets the value of the topupValueEstimated property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTopupValueEstimated() {
        return topupValueEstimated;
    }

    /**
     * Sets the value of the topupValueEstimated property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTopupValueEstimated(Boolean value) {
        this.topupValueEstimated = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
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
     * Gets the value of the parentTransactionId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getParentTransactionId() {
        return parentTransactionId;
    }

    /**
     * Sets the value of the parentTransactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setParentTransactionId(Integer value) {
        this.parentTransactionId = value;
    }

    /**
     * Gets the value of the agentId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAgentId() {
        return agentId;
    }

    /**
     * Sets the value of the agentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAgentId(Integer value) {
        this.agentId = value;
    }

    /**
     * Gets the value of the agentParentId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAgentParentId() {
        return agentParentId;
    }

    /**
     * Sets the value of the agentParentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAgentParentId(Integer value) {
        this.agentParentId = value;
    }

    /**
     * Gets the value of the agentMasterDistributorId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAgentMasterDistributorId() {
        return agentMasterDistributorId;
    }

    /**
     * Sets the value of the agentMasterDistributorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAgentMasterDistributorId(Integer value) {
        this.agentMasterDistributorId = value;
    }

    /**
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Gets the value of the voucherSerial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVoucherSerial() {
        return voucherSerial;
    }

    /**
     * Sets the value of the voucherSerial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVoucherSerial(String value) {
        this.voucherSerial = value;
    }

    /**
     * Gets the value of the terminalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminalCode() {
        return terminalCode;
    }

    /**
     * Sets the value of the terminalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminalCode(String value) {
        this.terminalCode = value;
    }

    /**
     * Gets the value of the margin property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMargin() {
        return margin;
    }

    /**
     * Sets the value of the margin property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMargin(Double value) {
        this.margin = value;
    }

    /**
     * Gets the value of the marginTax property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMarginTax() {
        return marginTax;
    }

    /**
     * Sets the value of the marginTax property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMarginTax(Double value) {
        this.marginTax = value;
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
     * Gets the value of the adminFeeTax property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAdminFeeTax() {
        return adminFeeTax;
    }

    /**
     * Sets the value of the adminFeeTax property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAdminFeeTax(Double value) {
        this.adminFeeTax = value;
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
     * Gets the value of the processFeeTax property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getProcessFeeTax() {
        return processFeeTax;
    }

    /**
     * Sets the value of the processFeeTax property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setProcessFeeTax(Double value) {
        this.processFeeTax = value;
    }

    /**
     * Gets the value of the adminRebate property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAdminRebate() {
        return adminRebate;
    }

    /**
     * Sets the value of the adminRebate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAdminRebate(Double value) {
        this.adminRebate = value;
    }

    /**
     * Gets the value of the adminRebateTax property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAdminRebateTax() {
        return adminRebateTax;
    }

    /**
     * Sets the value of the adminRebateTax property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAdminRebateTax(Double value) {
        this.adminRebateTax = value;
    }

    /**
     * Gets the value of the isReversed property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsReversed() {
        return isReversed;
    }

    /**
     * Sets the value of the isReversed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsReversed(Boolean value) {
        this.isReversed = value;
    }

    /**
     * Gets the value of the isRefunded property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRefunded() {
        return isRefunded;
    }

    /**
     * Sets the value of the isRefunded property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRefunded(Boolean value) {
        this.isRefunded = value;
    }

    /**
     * Gets the value of the openingWalletBalance property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getOpeningWalletBalance() {
        return openingWalletBalance;
    }

    /**
     * Sets the value of the openingWalletBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setOpeningWalletBalance(Double value) {
        this.openingWalletBalance = value;
    }

    /**
     * Gets the value of the closingWalletBalance property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getClosingWalletBalance() {
        return closingWalletBalance;
    }

    /**
     * Sets the value of the closingWalletBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setClosingWalletBalance(Double value) {
        this.closingWalletBalance = value;
    }

    /**
     * Gets the value of the linkedTransactions property.
     * 
     * @return
     *     possible object is
     *     {@link Transactions }
     *     
     */
    public Transactions getLinkedTransactions() {
        return linkedTransactions;
    }

    /**
     * Sets the value of the linkedTransactions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Transactions }
     *     
     */
    public void setLinkedTransactions(Transactions value) {
        this.linkedTransactions = value;
    }

    /**
     * Gets the value of the loyaltyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoyaltyId() {
        return loyaltyId;
    }

    /**
     * Sets the value of the loyaltyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoyaltyId(String value) {
        this.loyaltyId = value;
    }

    /**
     * Gets the value of the transDatas property.
     * 
     * @return
     *     possible object is
     *     {@link TransDatas }
     *     
     */
    public TransDatas getTransDatas() {
        return transDatas;
    }

    /**
     * Sets the value of the transDatas property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransDatas }
     *     
     */
    public void setTransDatas(TransDatas value) {
        this.transDatas = value;
    }

    /**
     * Gets the value of the inResultCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInResultCode() {
        return inResultCode;
    }

    /**
     * Sets the value of the inResultCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInResultCode(String value) {
        this.inResultCode = value;
    }

    /**
     * Gets the value of the inResultDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInResultDescription() {
        return inResultDescription;
    }

    /**
     * Sets the value of the inResultDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInResultDescription(String value) {
        this.inResultDescription = value;
    }

}
