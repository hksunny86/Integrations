
package com.inov8.integration.middleware.bop.nadra;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OTCVerificationDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OTCVerificationDTO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AvailableFinderIndex" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfint" minOccurs="0"/&gt;
 *         &lt;element name="CitizenNumber" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="ContactNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ResponseCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ResponseMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SecondaryCitizenName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SecondaryCitizenNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SessionID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="UserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VerificationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VerificationType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OTCVerificationDTO", namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", propOrder = {
    "availableFinderIndex",
    "citizenNumber",
    "contactNumber",
    "id",
    "name",
    "responseCode",
    "responseMessage",
    "secondaryCitizenName",
    "secondaryCitizenNo",
    "sessionID",
    "userID",
    "verificationDate",
    "verificationType"
})
public class OTCVerificationDTO
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "AvailableFinderIndex", nillable = true)
    protected ArrayOfint availableFinderIndex;
    @XmlElement(name = "CitizenNumber")
    protected Long citizenNumber;
    @XmlElement(name = "ContactNumber", nillable = true)
    protected String contactNumber;
    @XmlElement(name = "ID")
    protected Long id;
    @XmlElement(name = "Name", nillable = true)
    protected String name;
    @XmlElement(name = "ResponseCode", nillable = true)
    protected String responseCode;
    @XmlElement(name = "ResponseMessage", nillable = true)
    protected String responseMessage;
    @XmlElement(name = "SecondaryCitizenName", nillable = true)
    protected String secondaryCitizenName;
    @XmlElement(name = "SecondaryCitizenNo", nillable = true)
    protected String secondaryCitizenNo;
    @XmlElement(name = "SessionID")
    protected Long sessionID;
    @XmlElement(name = "UserID", nillable = true)
    protected String userID;
    @XmlElement(name = "VerificationDate", nillable = true)
    protected String verificationDate;
    @XmlElement(name = "VerificationType", nillable = true)
    protected String verificationType;

    /**
     * Gets the value of the availableFinderIndex property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfint }
     *     
     */
    public ArrayOfint getAvailableFinderIndex() {
        return availableFinderIndex;
    }

    /**
     * Sets the value of the availableFinderIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfint }
     *     
     */
    public void setAvailableFinderIndex(ArrayOfint value) {
        this.availableFinderIndex = value;
    }

    /**
     * Gets the value of the citizenNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCitizenNumber() {
        return citizenNumber;
    }

    /**
     * Sets the value of the citizenNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCitizenNumber(Long value) {
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
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setID(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
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
     * Gets the value of the responseMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * Sets the value of the responseMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseMessage(String value) {
        this.responseMessage = value;
    }

    /**
     * Gets the value of the secondaryCitizenName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecondaryCitizenName() {
        return secondaryCitizenName;
    }

    /**
     * Sets the value of the secondaryCitizenName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecondaryCitizenName(String value) {
        this.secondaryCitizenName = value;
    }

    /**
     * Gets the value of the secondaryCitizenNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecondaryCitizenNo() {
        return secondaryCitizenNo;
    }

    /**
     * Sets the value of the secondaryCitizenNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecondaryCitizenNo(String value) {
        this.secondaryCitizenNo = value;
    }

    /**
     * Gets the value of the sessionID property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSessionID() {
        return sessionID;
    }

    /**
     * Sets the value of the sessionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSessionID(Long value) {
        this.sessionID = value;
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
     * Gets the value of the verificationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerificationDate() {
        return verificationDate;
    }

    /**
     * Sets the value of the verificationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerificationDate(String value) {
        this.verificationDate = value;
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

}
