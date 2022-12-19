
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
 *         &lt;element name="GetAccountMappingResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "getAccountMappingResult"
})
@XmlRootElement(name = "GetAccountMappingResponse", namespace = "http://tempuri.org/")
public class GetAccountMappingResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "GetAccountMappingResult", namespace = "http://tempuri.org/", nillable = true)
    protected String getAccountMappingResult;

    /**
     * Gets the value of the getAccountMappingResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetAccountMappingResult() {
        return getAccountMappingResult;
    }

    /**
     * Sets the value of the getAccountMappingResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetAccountMappingResult(String value) {
        this.getAccountMappingResult = value;
    }

}
