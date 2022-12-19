
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
 *         &lt;element name="GetBillingCompaniesResult" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}BillingCompaniesOutputParams" minOccurs="0"/&gt;
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
    "getBillingCompaniesResult"
})
@XmlRootElement(name = "GetBillingCompaniesResponse", namespace = "http://tempuri.org/")
public class GetBillingCompaniesResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "GetBillingCompaniesResult", namespace = "http://tempuri.org/", nillable = true)
    protected BillingCompaniesOutputParams getBillingCompaniesResult;

    /**
     * Gets the value of the getBillingCompaniesResult property.
     * 
     * @return
     *     possible object is
     *     {@link BillingCompaniesOutputParams }
     *     
     */
    public BillingCompaniesOutputParams getGetBillingCompaniesResult() {
        return getBillingCompaniesResult;
    }

    /**
     * Sets the value of the getBillingCompaniesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingCompaniesOutputParams }
     *     
     */
    public void setGetBillingCompaniesResult(BillingCompaniesOutputParams value) {
        this.getBillingCompaniesResult = value;
    }

}
