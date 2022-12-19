
package com.inov8.integration.middleware.abl.mobapp;

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
 *         &lt;element name="GetAccountsResult" type="{http://schemas.datacontract.org/2004/07/ABLServiceModels}GetAccountsOutputParams" minOccurs="0"/&gt;
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
    "getAccountsResult"
})
@XmlRootElement(name = "GetAccountsResponse", namespace = "http://tempuri.org/")
public class GetAccountsResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "GetAccountsResult", namespace = "http://tempuri.org/", nillable = true)
    protected GetAccountsOutputParams getAccountsResult;

    /**
     * Gets the value of the getAccountsResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetAccountsOutputParams }
     *     
     */
    public GetAccountsOutputParams getGetAccountsResult() {
        return getAccountsResult;
    }

    /**
     * Sets the value of the getAccountsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetAccountsOutputParams }
     *     
     */
    public void setGetAccountsResult(GetAccountsOutputParams value) {
        this.getAccountsResult = value;
    }

}
