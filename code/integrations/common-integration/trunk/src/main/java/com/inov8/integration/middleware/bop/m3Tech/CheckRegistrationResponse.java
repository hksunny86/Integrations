
package com.inov8.integration.middleware.bop.m3Tech;

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
 *         &lt;element name="CheckRegistrationResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "checkRegistrationResult"
})
@XmlRootElement(name = "CheckRegistrationResponse")
public class CheckRegistrationResponse {

    @XmlElement(name = "CheckRegistrationResult")
    protected String checkRegistrationResult;

    /**
     * Gets the value of the checkRegistrationResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckRegistrationResult() {
        return checkRegistrationResult;
    }

    /**
     * Sets the value of the checkRegistrationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckRegistrationResult(String value) {
        this.checkRegistrationResult = value;
    }

}
