
package com.inov8.integration.middleware.bop.nadra;

import java.io.Serializable;
import javax.xml.bind.annotation.*;


/**
 * <p>Java class for CitizenVerificationDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CitizenVerificationDTO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AvailableFinderIndex" type="{http://schemas.microsoft.com/2003/10/Serialization/Arrays}ArrayOfint" minOccurs="0"/&gt;
 *         &lt;element name="BirthPlace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Branch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CNICExpiryDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CitizenNumber" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="ContactNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DateOfBirth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="District" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Gender" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="MotherName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NameInAlphabet" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PresentAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ResponseCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ResponseMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SessionID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="Teshsil" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CitizenVerificationDTO", namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel", propOrder = {
    "availableFinderIndex",
    "birthPlace",
    "branch",
    "cnicExpiryDate",
    "citizenNumber",
    "contactNumber",
    "dateOfBirth",
    "district",
    "gender",
    "id",
    "motherName",
    "name",
    "nameInAlphabet",
    "presentAddress",
    "responseCode",
    "responseMessage",
    "sessionID",
    "teshsil",
    "userID",
    "verificationDate",
    "verificationType"
})
public class CitizenVerificationDTO
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "AvailableFinderIndex", nillable = true)
    protected ArrayOfint availableFinderIndex;
    @XmlElement(name = "BirthPlace", nillable = true)
    protected String birthPlace;
    @XmlElement(name = "Branch", nillable = true)
    protected String branch;
    @XmlElement(name = "CNICExpiryDate", nillable = true)
    protected String cnicExpiryDate;
    @XmlElement(name = "CitizenNumber")
    protected Long citizenNumber;
    @XmlElement(name = "ContactNumber", nillable = true)
    protected String contactNumber;
    @XmlElement(name = "DateOfBirth", nillable = true)
    protected String dateOfBirth;
    @XmlElement(name = "District", nillable = true)
    protected String district;
    @XmlElement(name = "Gender", nillable = true)
    protected String gender;
    @XmlElement(name = "ID")
    protected Long id;
    @XmlElement(name = "MotherName", nillable = true)
    protected String motherName;
    @XmlElement(name = "Name", nillable = true)
    protected String name;
    @XmlElement(name = "NameInAlphabet", nillable = true)
    protected String nameInAlphabet;
    @XmlElement(name = "PresentAddress", nillable = true)
    protected String presentAddress;
    @XmlElement(name = "ResponseCode", nillable = true)
    protected String responseCode;
    @XmlElement(name = "ResponseMessage", nillable = true)
    protected String responseMessage;
    @XmlElement(name = "SessionID")
    protected Long sessionID;
    @XmlElement(name = "Teshsil", nillable = true)
    protected String teshsil;
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
     * Gets the value of the birthPlace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthPlace() {
        return birthPlace;
    }

    /**
     * Sets the value of the birthPlace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthPlace(String value) {
        this.birthPlace = value;
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
     * Gets the value of the cnicExpiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNICExpiryDate() {
        return cnicExpiryDate;
    }

    /**
     * Sets the value of the cnicExpiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNICExpiryDate(String value) {
        this.cnicExpiryDate = value;
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
     * Gets the value of the dateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateOfBirth(String value) {
        this.dateOfBirth = value;
    }

    /**
     * Gets the value of the district property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistrict() {
        return district;
    }

    /**
     * Sets the value of the district property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistrict(String value) {
        this.district = value;
    }

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGender(String value) {
        this.gender = value;
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
     * Gets the value of the motherName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotherName() {
        return motherName;
    }

    /**
     * Sets the value of the motherName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotherName(String value) {
        this.motherName = value;
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
     * Gets the value of the nameInAlphabet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameInAlphabet() {
        return nameInAlphabet;
    }

    /**
     * Sets the value of the nameInAlphabet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameInAlphabet(String value) {
        this.nameInAlphabet = value;
    }

    /**
     * Gets the value of the presentAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPresentAddress() {
        return presentAddress;
    }

    /**
     * Sets the value of the presentAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPresentAddress(String value) {
        this.presentAddress = value;
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
     * Gets the value of the teshsil property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTeshsil() {
        return teshsil;
    }

    /**
     * Sets the value of the teshsil property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTeshsil(String value) {
        this.teshsil = value;
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
