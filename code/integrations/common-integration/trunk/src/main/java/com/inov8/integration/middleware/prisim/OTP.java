
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OTP complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OTP"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EmailPin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MobilePin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OTP", propOrder = {
    "emailPin",
    "mobilePin"
})
public class OTP
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "EmailPin", nillable = true)
    protected String emailPin;
    @XmlElement(name = "MobilePin", nillable = true)
    protected String mobilePin;

    /**
     * Gets the value of the emailPin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailPin() {
        return emailPin;
    }

    /**
     * Sets the value of the emailPin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailPin(String value) {
        this.emailPin = value;
    }

    /**
     * Gets the value of the mobilePin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobilePin() {
        return mobilePin;
    }

    /**
     * Sets the value of the mobilePin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobilePin(String value) {
        this.mobilePin = value;
    }

}
