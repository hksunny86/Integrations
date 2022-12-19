
package com.inov8.integration.middleware.nadra.biometric;

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
 *         &lt;element name="GetCitizenDataResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "getCitizenDataResult"
})
@XmlRootElement(name = "GetCitizenDataResponse")
public class GetCitizenDataResponse {

    @XmlElementRef(name = "GetCitizenDataResult", namespace = "http://NADRA.Biometric.Verification", type = JAXBElement.class, required = false)
    protected JAXBElement<String> getCitizenDataResult;

    /**
     * Gets the value of the getCitizenDataResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGetCitizenDataResult() {
        return getCitizenDataResult;
    }

    /**
     * Sets the value of the getCitizenDataResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGetCitizenDataResult(JAXBElement<String> value) {
        this.getCitizenDataResult = value;
    }

}
