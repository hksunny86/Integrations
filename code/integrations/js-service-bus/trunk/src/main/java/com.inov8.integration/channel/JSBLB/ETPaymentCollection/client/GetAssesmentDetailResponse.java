
package com.inov8.integration.channel.JSBLB.ETPaymentCollection.client;

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
 *         &lt;element name="GetAssesmentDetailResult" type="{http://TaxManagementSystem/}AssesmentDetails" minOccurs="0"/&gt;
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
    "getAssesmentDetailResult"
})
@XmlRootElement(name = "GetAssesmentDetailResponse")
public class GetAssesmentDetailResponse {

    @XmlElement(name = "GetAssesmentDetailResult")
    protected AssesmentDetails getAssesmentDetailResult;

    /**
     * Gets the value of the getAssesmentDetailResult property.
     * 
     * @return
     *     possible object is
     *     {@link AssesmentDetails }
     *     
     */
    public AssesmentDetails getGetAssesmentDetailResult() {
        return getAssesmentDetailResult;
    }

    /**
     * Sets the value of the getAssesmentDetailResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssesmentDetails }
     *     
     */
    public void setGetAssesmentDetailResult(AssesmentDetails value) {
        this.getAssesmentDetailResult = value;
    }

}
