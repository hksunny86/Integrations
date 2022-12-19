
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
 *         &lt;element name="VerifyCitizenLocallyResult" type="{http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel}CitizenVerificationLocalDTO" minOccurs="0"/&gt;
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
    "verifyCitizenLocallyResult"
})
@XmlRootElement(name = "VerifyCitizenLocallyResponse")
public class VerifyCitizenLocallyResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "VerifyCitizenLocallyResult", nillable = true)
    protected CitizenVerificationLocalDTO verifyCitizenLocallyResult;

    /**
     * Gets the value of the verifyCitizenLocallyResult property.
     * 
     * @return
     *     possible object is
     *     {@link CitizenVerificationLocalDTO }
     *     
     */
    public CitizenVerificationLocalDTO getVerifyCitizenLocallyResult() {
        return verifyCitizenLocallyResult;
    }

    /**
     * Sets the value of the verifyCitizenLocallyResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CitizenVerificationLocalDTO }
     *     
     */
    public void setVerifyCitizenLocallyResult(CitizenVerificationLocalDTO value) {
        this.verifyCitizenLocallyResult = value;
    }

}
