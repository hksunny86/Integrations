
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
 *         &lt;element name="GetExistingVerisysResult" type="{http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel}CitizenVerificationDTO" minOccurs="0"/&gt;
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
    "getExistingVerisysResult"
})
@XmlRootElement(name = "GetExistingVerisysResponse")
public class GetExistingVerisysResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "GetExistingVerisysResult", nillable = true)
    protected CitizenVerificationDTO getExistingVerisysResult;

    /**
     * Gets the value of the getExistingVerisysResult property.
     * 
     * @return
     *     possible object is
     *     {@link CitizenVerificationDTO }
     *     
     */
    public CitizenVerificationDTO getGetExistingVerisysResult() {
        return getExistingVerisysResult;
    }

    /**
     * Sets the value of the getExistingVerisysResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CitizenVerificationDTO }
     *     
     */
    public void setGetExistingVerisysResult(CitizenVerificationDTO value) {
        this.getExistingVerisysResult = value;
    }

}
