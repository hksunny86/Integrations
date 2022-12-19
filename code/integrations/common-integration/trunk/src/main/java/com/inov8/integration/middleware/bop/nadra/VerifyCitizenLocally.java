
package com.inov8.integration.middleware.bop.nadra;

import java.io.Serializable;
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
 *         &lt;element name="Credentials" type="{http://schemas.datacontract.org/2004/07/ExternalChannels.Verification}UserCredentials" minOccurs="0"/&gt;
 *         &lt;element name="CitizenNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "credentials",
    "citizenNumber"
})
@XmlRootElement(name = "VerifyCitizenLocally")
public class VerifyCitizenLocally
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Credentials", nillable = true)
    protected UserCredentials credentials;
    @XmlElement(name = "CitizenNumber", nillable = true)
    protected String citizenNumber;

    /**
     * Gets the value of the credentials property.
     * 
     * @return
     *     possible object is
     *     {@link UserCredentials }
     *     
     */
    public UserCredentials getCredentials() {
        return credentials;
    }

    /**
     * Sets the value of the credentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserCredentials }
     *     
     */
    public void setCredentials(UserCredentials value) {
        this.credentials = value;
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

}
