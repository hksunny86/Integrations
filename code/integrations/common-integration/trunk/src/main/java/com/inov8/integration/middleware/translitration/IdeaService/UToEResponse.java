
package com.inov8.integration.middleware.translitration.IdeaService;

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
 *         &lt;element name="U_To_EResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "uToEResult"
})
@XmlRootElement(name = "U_To_EResponse")
public class UToEResponse {

    @XmlElement(name = "U_To_EResult")
    protected String uToEResult;

    /**
     * Gets the value of the uToEResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUToEResult() {
        return uToEResult;
    }

    /**
     * Sets the value of the uToEResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUToEResult(String value) {
        this.uToEResult = value;
    }

}
