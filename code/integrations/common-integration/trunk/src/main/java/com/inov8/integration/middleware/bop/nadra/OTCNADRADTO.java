
package com.inov8.integration.middleware.bop.nadra;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OTC_NADRA_DTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OTC_NADRA_DTO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ACCOUNT_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AreaName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CitizenNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ContactNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ContextData" type="{http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel}ContextData" minOccurs="0"/&gt;
 *         &lt;element name="FingerIndex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FingerTemplate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="REMITTANCE_AMOUNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="REMITTANCE_TYPE" type="{http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel}Enums.REMITTANCE_TYPE" minOccurs="0"/&gt;
 *         &lt;element name="SECONDARY_CITIZEN_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SECONDARY_CONTACT_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SessionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TemplateFormat" type="{http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel}Enums.Template_Format" minOccurs="0"/&gt;
 *         &lt;element name="TransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="UserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VerificationType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VerificationTypeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OTC_NADRA_DTO", namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", propOrder = {
    "accountnumber",
    "areaName",
    "citizenNumber",
    "contactNumber",
    "contextData",
    "fingerIndex",
    "fingerTemplate",
    "remittanceamount",
    "remittancetype",
    "secondarycitizennumber",
    "secondarycontactnumber",
    "sessionID",
    "templateFormat",
    "transactionID",
    "userID",
    "verificationType",
    "verificationTypeName"
})
public class OTCNADRADTO
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ACCOUNT_NUMBER", nillable = true)
    protected String accountnumber;
    @XmlElement(name = "AreaName", nillable = true)
    protected String areaName;
    @XmlElement(name = "CitizenNumber", nillable = true)
    protected String citizenNumber;
    @XmlElement(name = "ContactNumber", nillable = true)
    protected String contactNumber;
    @XmlElement(name = "ContextData", nillable = true)
    protected ContextData contextData;
    @XmlElement(name = "FingerIndex", nillable = true)
    protected String fingerIndex;
    @XmlElement(name = "FingerTemplate", nillable = true)
    protected String fingerTemplate;
    @XmlElement(name = "REMITTANCE_AMOUNT", nillable = true)
    protected String remittanceamount;
    @XmlElement(name = "REMITTANCE_TYPE")
    @XmlSchemaType(name = "string")
    protected EnumsREMITTANCETYPE remittancetype;
    @XmlElement(name = "SECONDARY_CITIZEN_NUMBER", nillable = true)
    protected String secondarycitizennumber;
    @XmlElement(name = "SECONDARY_CONTACT_NUMBER", nillable = true)
    protected String secondarycontactnumber;
    @XmlElement(name = "SessionID", nillable = true)
    protected String sessionID;
    @XmlElement(name = "TemplateFormat")
    @XmlSchemaType(name = "string")
    protected EnumsTemplateFormat templateFormat;
    @XmlElement(name = "TransactionID", nillable = true)
    protected String transactionID;
    @XmlElement(name = "UserID", nillable = true)
    protected String userID;
    @XmlElement(name = "VerificationType", nillable = true)
    protected String verificationType;
    @XmlElement(name = "VerificationTypeName", nillable = true)
    protected String verificationTypeName;

    /**
     * Gets the value of the accountnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACCOUNTNUMBER() {
        return accountnumber;
    }

    /**
     * Sets the value of the accountnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACCOUNTNUMBER(String value) {
        this.accountnumber = value;
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
     * Gets the value of the contactNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * Sets the value of the contactNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactNumber(String value) {
        this.contactNumber = value;
    }

    /**
     * Gets the value of the contextData property.
     * 
     * @return
     *     possible object is
     *     {@link ContextData }
     *     
     */
    public ContextData getContextData() {
        return contextData;
    }

    /**
     * Sets the value of the contextData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContextData }
     *     
     */
    public void setContextData(ContextData value) {
        this.contextData = value;
    }

    /**
     * Gets the value of the fingerIndex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFingerIndex() {
        return fingerIndex;
    }

    /**
     * Sets the value of the fingerIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFingerIndex(String value) {
        this.fingerIndex = value;
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
     * Gets the value of the remittanceamount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREMITTANCEAMOUNT() {
        return remittanceamount;
    }

    /**
     * Sets the value of the remittanceamount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREMITTANCEAMOUNT(String value) {
        this.remittanceamount = value;
    }

    /**
     * Gets the value of the remittancetype property.
     * 
     * @return
     *     possible object is
     *     {@link EnumsREMITTANCETYPE }
     *     
     */
    public EnumsREMITTANCETYPE getREMITTANCETYPE() {
        return remittancetype;
    }

    /**
     * Sets the value of the remittancetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumsREMITTANCETYPE }
     *     
     */
    public void setREMITTANCETYPE(EnumsREMITTANCETYPE value) {
        this.remittancetype = value;
    }

    /**
     * Gets the value of the secondarycitizennumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSECONDARYCITIZENNUMBER() {
        return secondarycitizennumber;
    }

    /**
     * Sets the value of the secondarycitizennumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSECONDARYCITIZENNUMBER(String value) {
        this.secondarycitizennumber = value;
    }

    /**
     * Gets the value of the secondarycontactnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSECONDARYCONTACTNUMBER() {
        return secondarycontactnumber;
    }

    /**
     * Sets the value of the secondarycontactnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSECONDARYCONTACTNUMBER(String value) {
        this.secondarycontactnumber = value;
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
     * Gets the value of the verificationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerificationType() {
        return verificationType;
    }

    /**
     * Sets the value of the verificationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerificationType(String value) {
        this.verificationType = value;
    }

    /**
     * Gets the value of the verificationTypeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerificationTypeName() {
        return verificationTypeName;
    }

    /**
     * Sets the value of the verificationTypeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerificationTypeName(String value) {
        this.verificationTypeName = value;
    }

}
