
package com.inov8.integration.middleware.abl.smsservice;

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
 *         &lt;element name="GetDataUsingDataContractResult" type="{http://schemas.datacontract.org/2004/07/}CompositeType" minOccurs="0"/&gt;
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
    "getDataUsingDataContractResult"
})
@XmlRootElement(name = "GetDataUsingDataContractResponse")
public class GetDataUsingDataContractResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "GetDataUsingDataContractResult", nillable = true)
    protected CompositeType getDataUsingDataContractResult;

    /**
     * Gets the value of the getDataUsingDataContractResult property.
     * 
     * @return
     *     possible object is
     *     {@link CompositeType }
     *     
     */
    public CompositeType getGetDataUsingDataContractResult() {
        return getDataUsingDataContractResult;
    }

    /**
     * Sets the value of the getDataUsingDataContractResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CompositeType }
     *     
     */
    public void setGetDataUsingDataContractResult(CompositeType value) {
        this.getDataUsingDataContractResult = value;
    }

}
