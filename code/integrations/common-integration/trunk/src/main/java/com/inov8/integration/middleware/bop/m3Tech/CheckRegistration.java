
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
 *         &lt;element name="AccessKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CNIC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "accessKey",
    "cnic"
})
@XmlRootElement(name = "CheckRegistration")
public class CheckRegistration {

    @XmlElement(name = "AccessKey")
    protected String accessKey;
    @XmlElement(name = "CNIC")
    protected String cnic;

    /**
     * Gets the value of the accessKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * Sets the value of the accessKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessKey(String value) {
        this.accessKey = value;
    }

    /**
     * Gets the value of the cnic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNIC() {
        return cnic;
    }

    /**
     * Sets the value of the cnic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNIC(String value) {
        this.cnic = value;
    }

}
