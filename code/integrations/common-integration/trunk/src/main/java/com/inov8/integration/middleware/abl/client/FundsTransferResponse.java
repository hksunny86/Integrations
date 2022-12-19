
package com.inov8.integration.middleware.abl.client;

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
 *         &lt;element name="FundsTransferResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "fundsTransferResult"
})
@XmlRootElement(name = "FundsTransferResponse")
public class FundsTransferResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "FundsTransferResult", nillable = true)
    protected String fundsTransferResult;

    /**
     * Gets the value of the fundsTransferResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundsTransferResult() {
        return fundsTransferResult;
    }

    /**
     * Sets the value of the fundsTransferResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundsTransferResult(String value) {
        this.fundsTransferResult = value;
    }

}
