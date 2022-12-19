
package com.inov8.integration.middleware.bop.nadra;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NADRA_DTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NADRA_DTO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AccountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AreaName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Branch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CitizenNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ConectNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FinderIndex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FingerTemplate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SegmentID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SessionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TemplateFormat" type="{http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel}Enums.Template_Format" minOccurs="0"/&gt;
 *         &lt;element name="TransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="UserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="contextData" type="{http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel}NADRA_DTO.ContextData" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NADRA_DTO", namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", propOrder = {
    "accountType",
    "areaName",
    "branch",
    "citizenNumber",
    "conectNumber",
    "finderIndex",
    "fingerTemplate",
    "segmentID",
    "sessionID",
    "templateFormat",
    "transactionID",
    "userID",
    "contextData"
})
public class NADRADTO
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "AccountType", nillable = true)
    protected String accountType;
    @XmlElement(name = "AreaName", nillable = true)
    protected String areaName;
    @XmlElement(name = "Branch", nillable = true)
    protected String branch;
    @XmlElement(name = "CitizenNumber", nillable = true)
    protected String citizenNumber;
    @XmlElement(name = "ConectNumber", nillable = true)
    protected String conectNumber;
    @XmlElement(name = "FinderIndex", nillable = true)
    protected String finderIndex;
    @XmlElement(name = "FingerTemplate", nillable = true)
    protected String fingerTemplate;
    @XmlElement(name = "SegmentID", nillable = true)
    protected String segmentID;
    @XmlElement(name = "SessionID", nillable = true)
    protected String sessionID;
    @XmlElement(name = "TemplateFormat")
    @XmlSchemaType(name = "string")
    protected EnumsTemplateFormat templateFormat;
    @XmlElement(name = "TransactionID", nillable = true)
    protected String transactionID;
    @XmlElement(name = "UserID", nillable = true)
    protected String userID;
    @XmlElement(nillable = true)
    protected NADRADTOContextData contextData;

    /**
     * Gets the value of the accountType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the value of the accountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountType(String value) {
        this.accountType = value;
    }

    /**
     * Gets the value of the areaName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * Sets the value of the areaName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAreaName(String value) {
        this.areaName = value;
    }

    /**
     * Gets the value of the branch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBranch() {
        return branch;
    }

    /**
     * Sets the value of the branch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBranch(String value) {
        this.branch = value;
    }

    /**
     * Gets the value of the citizenNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCitizenNumber() {
        return citizenNumber;
    }

    /**
     * Sets the value of the citizenNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCitizenNumber(String value) {
        this.citizenNumber = value;
    }

    /**
     * Gets the value of the conectNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConectNumber() {
        return conectNumber;
    }

    /**
     * Sets the value of the conectNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConectNumber(String value) {
        this.conectNumber = value;
    }

    /**
     * Gets the value of the finderIndex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinderIndex() {
        return finderIndex;
    }

    /**
     * Sets the value of the finderIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinderIndex(String value) {
        this.finderIndex = value;
    }

    /**
     * Gets the value of the fingerTemplate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFingerTemplate() {
        return fingerTemplate;
    }

    /**
     * Sets the value of the fingerTemplate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFingerTemplate(String value) {
        this.fingerTemplate = value;
    }

    /**
     * Gets the value of the segmentID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSegmentID() {
        return segmentID;
    }

    /**
     * Sets the value of the segmentID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSegmentID(String value) {
        this.segmentID = value;
    }

    /**
     * Gets the value of the sessionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * Sets the value of the sessionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionID(String value) {
        this.sessionID = value;
    }

    /**
     * Gets the value of the templateFormat property.
     * 
     * @return
     *     possible object is
     *     {@link EnumsTemplateFormat }
     *     
     */
    public EnumsTemplateFormat getTemplateFormat() {
        return templateFormat;
    }

    /**
     * Sets the value of the templateFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumsTemplateFormat }
     *     
     */
    public void setTemplateFormat(EnumsTemplateFormat value) {
        this.templateFormat = value;
    }

    /**
     * Gets the value of the transactionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionID() {
        return transactionID;
    }

    /**
     * Sets the value of the transactionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionID(String value) {
        this.transactionID = value;
    }

    /**
     * Gets the value of the userID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserID(String value) {
        this.userID = value;
    }

    /**
     * Gets the value of the contextData property.
     * 
     * @return
     *     possible object is
     *     {@link NADRADTOContextData }
     *     
     */
    public NADRADTOContextData getContextData() {
        return contextData;
    }

    /**
     * Sets the value of the contextData property.
     * 
     * @param value
     *     allowed object is
     *     {@link NADRADTOContextData }
     *     
     */
    public void setContextData(NADRADTOContextData value) {
        this.contextData = value;
    }

}
