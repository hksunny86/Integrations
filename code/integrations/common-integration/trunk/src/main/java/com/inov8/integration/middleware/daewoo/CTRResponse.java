
package com.inov8.integration.middleware.daewoo;

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
 *         &lt;element name="CTRResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "ctrResult"
})
@XmlRootElement(name = "CTRResponse")
public class CTRResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CTRResult")
    protected String ctrResult;

    /**
     * Gets the value of the ctrResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCTRResult() {
        return ctrResult;
    }

    /**
     * Sets the value of the ctrResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCTRResult(String value) {
        this.ctrResult = value;
    }

}
