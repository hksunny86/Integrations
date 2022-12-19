
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
 *         &lt;element name="agentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="supplierCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="resultsPerPage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pageNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "agentCode",
    "supplierCode",
    "dateFrom",
    "dateTo",
    "resultsPerPage",
    "pageNumber"
})
@XmlRootElement(name = "SoapAgentSettlementRequest")
public class SoapAgentSettlementRequest
    extends SoapRequestBaseWithSession
{

    protected String agentCode;
    protected String supplierCode;
    protected String dateFrom;
    protected String dateTo;
    protected String resultsPerPage;
    protected String pageNumber;

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
     * Gets the value of the resultsPerPage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultsPerPage() {
        return resultsPerPage;
    }

    /**
     * Sets the value of the resultsPerPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultsPerPage(String value) {
        this.resultsPerPage = value;
    }

    /**
     * Gets the value of the pageNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPageNumber() {
        return pageNumber;
    }

    /**
     * Sets the value of the pageNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPageNumber(String value) {
        this.pageNumber = value;
    }

}
