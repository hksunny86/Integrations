
package com.inov8.integration.middleware.abl.client;

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
 *         &lt;element name="GetDataUsingDataContractResult" type="{http://schemas.datacontract.org/2004/07/WcfService1}CompositeType" minOccurs="0"/&gt;
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
public class GetDataUsingDataContractResponse {

    @XmlElementRef(name = "GetDataUsingDataContractResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<CompositeType> getDataUsingDataContractResult;

    /**
     * Gets the value of the getDataUsingDataContractResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CompositeType }{@code >}
     *     
     */
    public JAXBElement<CompositeType> getGetDataUsingDataContractResult() {
        return getDataUsingDataContractResult;
    }

    /**
     * Sets the value of the getDataUsingDataContractResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CompositeType }{@code >}
     *     
     */
    public void setGetDataUsingDataContractResult(JAXBElement<CompositeType> value) {
        this.getDataUsingDataContractResult = value;
    }

}
