
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Registration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Registration"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CNIC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="EmailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FirstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Identifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LoginId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MobileNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SecretAnswer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SecretQuestion" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}SecretQuestion" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Registration", propOrder = {
    "cnic",
    "cardNumber",
    "emailAddress",
    "firstName",
    "identifier",
    "lastName",
    "loginId",
    "mobileNumber",
    "secretAnswer",
    "secretQuestion"
})
public class Registration2
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CNIC", nillable = true)
    protected String cnic;
    @XmlElement(name = "CardNumber", nillable = true)
    protected String cardNumber;
    @XmlElement(name = "EmailAddress", nillable = true)
    protected String emailAddress;
    @XmlElement(name = "FirstName", nillable = true)
    protected String firstName;
    @XmlElement(name = "Identifier", nillable = true)
    protected String identifier;
    @XmlElement(name = "LastName", nillable = true)
    protected String lastName;
    @XmlElement(name = "LoginId", nillable = true)
    protected String loginId;
    @XmlElement(name = "MobileNumber", nillable = true)
    protected String mobileNumber;
    @XmlElement(name = "SecretAnswer", nillable = true)
    protected String secretAnswer;
    @XmlElement(name = "SecretQuestion", nillable = true)
    protected SecretQuestion secretQuestion;

    /**
     * Gets the value of the cnic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNIC() {
        return cnic;
    }

    /**
     * Sets the value of the cnic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNIC(String value) {
        this.cnic = value;
    }

    /**
     * Gets the value of the cardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets the value of the cardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardNumber(String value) {
        this.cardNumber = value;
    }

    /**
     * Gets the value of the emailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the value of the emailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddress(String value) {
        this.emailAddress = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the loginId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * Sets the value of the loginId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginId(String value) {
        this.loginId = value;
    }

    /**
     * Gets the value of the mobileNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets the value of the mobileNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileNumber(String value) {
        this.mobileNumber = value;
    }

    /**
     * Gets the value of the secretAnswer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretAnswer() {
        return secretAnswer;
    }

    /**
     * Sets the value of the secretAnswer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretAnswer(String value) {
        this.secretAnswer = value;
    }

    /**
     * Gets the value of the secretQuestion property.
     * 
     * @return
     *     possible object is
     *     {@link SecretQuestion }
     *     
     */
    public SecretQuestion getSecretQuestion() {
        return secretQuestion;
    }

    /**
     * Sets the value of the secretQuestion property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretQuestion }
     *     
     */
    public void setSecretQuestion(SecretQuestion value) {
        this.secretQuestion = value;
    }

}
