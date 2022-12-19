
package com.inov8.integration.middleware.jsnadra;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VerifyFingerPrintsOTCResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VerifyFingerPrintsOTCResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="VerifyFingerPrintsOTCResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerifyFingerPrintsOTCResponse", propOrder = {
    "verifyFingerPrintsOTCResult"
})
public class VerifyFingerPrintsOTCResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "VerifyFingerPrintsOTCResult", required = true)
    protected String verifyFingerPrintsOTCResult;

    /**
     * Gets the value of the verifyFingerPrintsOTCResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerifyFingerPrintsOTCResult() {
        return verifyFingerPrintsOTCResult;
    }

    /**
     * Sets the value of the verifyFingerPrintsOTCResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerifyFingerPrintsOTCResult(String value) {
        this.verifyFingerPrintsOTCResult = value;
    }

}
