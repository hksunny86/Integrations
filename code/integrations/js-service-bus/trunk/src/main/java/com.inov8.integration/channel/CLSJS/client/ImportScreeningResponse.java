
package com.inov8.integration.channel.CLSJS.client;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ImportScreeningResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ImportScreeningResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}RequestID"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}CaseId"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}CaseStatus"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}ImportStatus"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}IsHit"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}ScreeningStatus"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}TotalCWL"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}TotalGWL"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}TotalPEPEDD"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}TotalPrivate"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImportScreeningResponse", propOrder = {
    "requestID",
    "caseId",
    "caseStatus",
    "importStatus",
    "isHit",
    "screeningStatus",
    "totalCWL",
    "totalGWL",
    "totalPEPEDD",
    "totalPrivate"
})
public class ImportScreeningResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "RequestID", required = true)
    protected String requestID;
    @XmlElement(name = "CaseId", required = true)
    protected String caseId;
    @XmlElement(name = "CaseStatus", required = true)
    protected String caseStatus;
    @XmlElement(name = "ImportStatus", required = true)
    protected String importStatus;
    @XmlElement(name = "IsHit", required = true)
    protected String isHit;
    @XmlElement(name = "ScreeningStatus", required = true)
    protected String screeningStatus;
    @XmlElement(name = "TotalCWL", required = true)
    protected String totalCWL;
    @XmlElement(name = "TotalGWL", required = true)
    protected String totalGWL;
    @XmlElement(name = "TotalPEPEDD", required = true)
    protected String totalPEPEDD;
    @XmlElement(name = "TotalPrivate", required = true)
    protected String totalPrivate;

    /**
     * Gets the value of the requestID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestID(String value) {
        this.requestID = value;
    }

    /**
     * Gets the value of the caseId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * Sets the value of the caseId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaseId(String value) {
        this.caseId = value;
    }

    /**
     * Gets the value of the caseStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaseStatus() {
        return caseStatus;
    }

    /**
     * Sets the value of the caseStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaseStatus(String value) {
        this.caseStatus = value;
    }

    /**
     * Gets the value of the importStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImportStatus() {
        return importStatus;
    }

    /**
     * Sets the value of the importStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImportStatus(String value) {
        this.importStatus = value;
    }

    /**
     * Gets the value of the isHit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsHit() {
        return isHit;
    }

    /**
     * Sets the value of the isHit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsHit(String value) {
        this.isHit = value;
    }

    /**
     * Gets the value of the screeningStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScreeningStatus() {
        return screeningStatus;
    }

    /**
     * Sets the value of the screeningStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScreeningStatus(String value) {
        this.screeningStatus = value;
    }

    /**
     * Gets the value of the totalCWL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalCWL() {
        return totalCWL;
    }

    /**
     * Sets the value of the totalCWL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalCWL(String value) {
        this.totalCWL = value;
    }

    /**
     * Gets the value of the totalGWL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalGWL() {
        return totalGWL;
    }

    /**
     * Sets the value of the totalGWL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalGWL(String value) {
        this.totalGWL = value;
    }

    /**
     * Gets the value of the totalPEPEDD property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalPEPEDD() {
        return totalPEPEDD;
    }

    /**
     * Sets the value of the totalPEPEDD property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalPEPEDD(String value) {
        this.totalPEPEDD = value;
    }

    /**
     * Gets the value of the totalPrivate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalPrivate() {
        return totalPrivate;
    }

    /**
     * Sets the value of the totalPrivate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalPrivate(String value) {
        this.totalPrivate = value;
    }

}
