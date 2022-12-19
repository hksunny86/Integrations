
package com.inov8.integration.middleware.bop.avenza;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Header" type="{http://BOP_MobileApp.org}Header"/&gt;
 *         &lt;element name="ResponseCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="FromDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ToDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Limit" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Desc" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "responseCode",
    "accountNumber",
    "fromDate",
    "toDate",
    "limit",
    "desc"
})
@XmlRootElement(name = "EmailAccountStatementRequest")
public class EmailAccountStatementRequest {

    @XmlElement(name = "Header", required = true)
    protected Header header;
    @XmlElement(name = "ResponseCode", required = true)
    protected String responseCode;
    @XmlElement(name = "AccountNumber", required = true)
    protected String accountNumber;
    @XmlElement(name = "FromDate", required = true)
    protected String fromDate;
    @XmlElement(name = "ToDate", required = true)
    protected String toDate;
    @XmlElement(name = "Limit", required = true)
    protected String limit;
    @XmlElement(name = "Desc", required = true)
    protected String desc;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link Header }
     *     
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link Header }
     *     
     */
    public void setHeader(Header value) {
        this.header = value;
    }

    /**
     * Gets the value of the responseCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the value of the responseCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseCode(String value) {
        this.responseCode = value;
    }

    /**
     * Gets the value of the accountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the fromDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Sets the value of the fromDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromDate(String value) {
        this.fromDate = value;
    }

    /**
     * Gets the value of the toDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * Sets the value of the toDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToDate(String value) {
        this.toDate = value;
    }

    /**
     * Gets the value of the limit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLimit() {
        return limit;
    }

    /**
     * Sets the value of the limit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLimit(String value) {
        this.limit = value;
    }

    /**
     * Gets the value of the desc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets the value of the desc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesc(String value) {
        this.desc = value;
    }

}
