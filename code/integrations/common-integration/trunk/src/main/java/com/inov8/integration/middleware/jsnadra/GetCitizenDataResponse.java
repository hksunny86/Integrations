
package com.inov8.integration.middleware.jsnadra;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetCitizenDataResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCitizenDataResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetCitizenDataResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCitizenDataResponse", propOrder = {
    "getCitizenDataResult"
})
public class GetCitizenDataResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "GetCitizenDataResult", required = true)
    protected String getCitizenDataResult;

    /**
     * Gets the value of the getCitizenDataResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetCitizenDataResult() {
        return getCitizenDataResult;
    }

    /**
     * Sets the value of the getCitizenDataResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetCitizenDataResult(String value) {
        this.getCitizenDataResult = value;
    }

}
