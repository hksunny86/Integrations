
package com.inov8.integration.middleware.jsnadra;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VerifyFingerPrintsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VerifyFingerPrintsResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="VerifyFingerPrintsResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerifyFingerPrintsResponse", propOrder = {
    "verifyFingerPrintsResult"
})
public class VerifyFingerPrintsResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "VerifyFingerPrintsResult", required = true)
    protected String verifyFingerPrintsResult;

    /**
     * Gets the value of the verifyFingerPrintsResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerifyFingerPrintsResult() {
        return verifyFingerPrintsResult;
    }

    /**
     * Sets the value of the verifyFingerPrintsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerifyFingerPrintsResult(String value) {
        this.verifyFingerPrintsResult = value;
    }

}
