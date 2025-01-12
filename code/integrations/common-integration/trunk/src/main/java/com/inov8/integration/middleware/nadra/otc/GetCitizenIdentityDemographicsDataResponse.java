
package com.inov8.integration.middleware.nadra.otc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
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
 *         &lt;element name="GetCitizenIdentityDemographicsDataResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "getCitizenIdentityDemographicsDataResult"
})
@XmlRootElement(name = "GetCitizenIdentityDemographicsDataResponse")
public class GetCitizenIdentityDemographicsDataResponse {

    @XmlElementRef(name = "GetCitizenIdentityDemographicsDataResult", namespace = "http://NADRA.Biometric.Verification", type = JAXBElement.class, required = false)
    protected JAXBElement<String> getCitizenIdentityDemographicsDataResult;

    /**
     * Gets the value of the getCitizenIdentityDemographicsDataResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGetCitizenIdentityDemographicsDataResult() {
        return getCitizenIdentityDemographicsDataResult;
    }

    /**
     * Sets the value of the getCitizenIdentityDemographicsDataResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGetCitizenIdentityDemographicsDataResult(JAXBElement<String> value) {
        this.getCitizenIdentityDemographicsDataResult = value;
    }

}
