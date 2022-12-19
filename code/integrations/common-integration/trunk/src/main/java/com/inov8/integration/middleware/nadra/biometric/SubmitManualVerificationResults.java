
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
 *         &lt;element name="franchizeID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="xml_request_data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "franchizeID",
    "xmlRequestData"
})
@XmlRootElement(name = "SubmitManualVerificationResults")
public class SubmitManualVerificationResults {

    @XmlElementRef(name = "franchizeID", namespace = "http://NADRA.Biometric.Verification", type = JAXBElement.class, required = false)
    protected JAXBElement<String> franchizeID;
    @XmlElementRef(name = "xml_request_data", namespace = "http://NADRA.Biometric.Verification", type = JAXBElement.class, required = false)
    protected JAXBElement<String> xmlRequestData;

    /**
     * Gets the value of the franchizeID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFranchizeID() {
        return franchizeID;
    }

    /**
     * Sets the value of the franchizeID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFranchizeID(JAXBElement<String> value) {
        this.franchizeID = value;
    }

    /**
     * Gets the value of the xmlRequestData property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getXmlRequestData() {
        return xmlRequestData;
    }

    /**
     * Sets the value of the xmlRequestData property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setXmlRequestData(JAXBElement<String> value) {
        this.xmlRequestData = value;
    }

}
