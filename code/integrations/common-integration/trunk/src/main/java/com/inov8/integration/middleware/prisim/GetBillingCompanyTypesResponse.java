
package com.inov8.integration.middleware.prisim;

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
 *         &lt;element name="GetBillingCompanyTypesResult" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}BillingCompanyTypeOutputParams" minOccurs="0"/&gt;
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
    "getBillingCompanyTypesResult"
})
@XmlRootElement(name = "GetBillingCompanyTypesResponse", namespace = "http://tempuri.org/")
public class GetBillingCompanyTypesResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "GetBillingCompanyTypesResult", namespace = "http://tempuri.org/", nillable = true)
    protected BillingCompanyTypeOutputParams getBillingCompanyTypesResult;

    /**
     * Gets the value of the getBillingCompanyTypesResult property.
     * 
     * @return
     *     possible object is
     *     {@link BillingCompanyTypeOutputParams }
     *     
     */
    public BillingCompanyTypeOutputParams getGetBillingCompanyTypesResult() {
        return getBillingCompanyTypesResult;
    }

    /**
     * Sets the value of the getBillingCompanyTypesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingCompanyTypeOutputParams }
     *     
     */
    public void setGetBillingCompanyTypesResult(BillingCompanyTypeOutputParams value) {
        this.getBillingCompanyTypesResult = value;
    }

}
