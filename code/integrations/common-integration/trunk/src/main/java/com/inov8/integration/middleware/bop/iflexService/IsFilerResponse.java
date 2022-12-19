
package com.inov8.integration.middleware.bop.iflexService;

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
 *         &lt;element name="IsFilerResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
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
    "isFilerResult"
})
@XmlRootElement(name = "IsFilerResponse", namespace = "http://tempuri.org/")
public class IsFilerResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "IsFilerResult", namespace = "http://tempuri.org/")
    protected Boolean isFilerResult;

    /**
     * Gets the value of the isFilerResult property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFilerResult() {
        return isFilerResult;
    }

    /**
     * Sets the value of the isFilerResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFilerResult(Boolean value) {
        this.isFilerResult = value;
    }

}
