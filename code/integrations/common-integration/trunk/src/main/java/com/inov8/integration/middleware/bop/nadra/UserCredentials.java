
package com.inov8.integration.middleware.bop.nadra;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserCredentials complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserCredentials"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Pasword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="UserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserCredentials", namespace = "http://schemas.datacontract.org/2004/07/ExternalChannels.Verification", propOrder = {
    "pasword",
    "userID"
})
public class UserCredentials
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Pasword", nillable = true)
    protected String pasword;
    @XmlElement(name = "UserID", nillable = true)
    protected String userID;

    /**
     * Gets the value of the pasword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasword() {
        return pasword;
    }

    /**
     * Sets the value of the pasword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasword(String value) {
        this.pasword = value;
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

}
