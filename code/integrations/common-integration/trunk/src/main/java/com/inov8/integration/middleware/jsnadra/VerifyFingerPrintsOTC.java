
package com.inov8.integration.middleware.jsnadra;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VerifyFingerPrintsOTC complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VerifyFingerPrintsOTC"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="franchizeID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="xml_request_data" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerifyFingerPrintsOTC", propOrder = {
    "franchizeID",
    "xmlRequestData"
})
public class VerifyFingerPrintsOTC
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String franchizeID;
    @XmlElement(name = "xml_request_data", required = true)
    protected String xmlRequestData;

    /**
     * Gets the value of the franchizeID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFranchizeID() {
        return franchizeID;
    }

    /**
     * Sets the value of the franchizeID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFranchizeID(String value) {
        this.franchizeID = value;
    }

    /**
     * Gets the value of the xmlRequestData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmlRequestData() {
        return xmlRequestData;
    }

    /**
     * Sets the value of the xmlRequestData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmlRequestData(String value) {
        this.xmlRequestData = value;
    }

}
