
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
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapRequestBaseWithSession"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="msisdn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="maxRecords" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="transReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentTransReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="transTypesFilter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pageLastTransId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="includeSubAgents" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="voucherSerial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="isRefunded" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isReversed" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="useSystemTime" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
    "msisdn",
    "dateFrom",
    "dateTo",
    "maxRecords",
    "transReference",
    "agentTransReference",
    "transTypesFilter",
    "pageLastTransId",
    "includeSubAgents",
    "agentCode",
    "voucherSerial",
    "isRefunded",
    "isReversed",
    "useSystemTime"
})
@XmlRootElement(name = "SoapTransactionInfoRequest")
public class SoapTransactionInfoRequest
    extends SoapRequestBaseWithSession
{

    protected String msisdn;
    protected String dateFrom;
    protected String dateTo;
    protected Integer maxRecords;
    protected String transReference;
    protected String agentTransReference;
    protected String transTypesFilter;
    protected String pageLastTransId;
    protected String includeSubAgents;
    protected String agentCode;
    protected String voucherSerial;
    protected Boolean isRefunded;
    protected Boolean isReversed;
    protected Boolean useSystemTime;

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
     * Gets the value of the dateFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateFrom() {
        return dateFrom;
    }

    /**
     * Sets the value of the dateFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateFrom(String value) {
        this.dateFrom = value;
    }

    /**
     * Gets the value of the dateTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateTo() {
        return dateTo;
    }

    /**
     * Sets the value of the dateTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateTo(String value) {
        this.dateTo = value;
    }

    /**
     * Gets the value of the maxRecords property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxRecords() {
        return maxRecords;
    }

    /**
     * Sets the value of the maxRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxRecords(Integer value) {
        this.maxRecords = value;
    }

    /**
     * Gets the value of the transReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransReference() {
        return transReference;
    }

    /**
     * Sets the value of the transReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransReference(String value) {
        this.transReference = value;
    }

    /**
     * Gets the value of the agentTransReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentTransReference() {
        return agentTransReference;
    }

    /**
     * Sets the value of the agentTransReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentTransReference(String value) {
        this.agentTransReference = value;
    }

    /**
     * Gets the value of the transTypesFilter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransTypesFilter() {
        return transTypesFilter;
    }

    /**
     * Sets the value of the transTypesFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransTypesFilter(String value) {
        this.transTypesFilter = value;
    }

    /**
     * Gets the value of the pageLastTransId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPageLastTransId() {
        return pageLastTransId;
    }

    /**
     * Sets the value of the pageLastTransId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPageLastTransId(String value) {
        this.pageLastTransId = value;
    }

    /**
     * Gets the value of the includeSubAgents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncludeSubAgents() {
        return includeSubAgents;
    }

    /**
     * Sets the value of the includeSubAgents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncludeSubAgents(String value) {
        this.includeSubAgents = value;
    }

    /**
     * Gets the value of the agentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentCode() {
        return agentCode;
    }

    /**
     * Sets the value of the agentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentCode(String value) {
        this.agentCode = value;
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
     * Gets the value of the useSystemTime property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUseSystemTime() {
        return useSystemTime;
    }

    /**
     * Sets the value of the useSystemTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUseSystemTime(Boolean value) {
        this.useSystemTime = value;
    }

}
