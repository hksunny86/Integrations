
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
 *         &lt;element name="GenerateChallanResult" type="{http://TaxManagementSystem/}ChallanDetail" minOccurs="0"/&gt;
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
    "generateChallanResult"
})
@XmlRootElement(name = "GenerateChallanResponse")
public class GenerateChallanResponse {

    @XmlElement(name = "GenerateChallanResult")
    protected ChallanDetail generateChallanResult;

    /**
     * Gets the value of the generateChallanResult property.
     * 
     * @return
     *     possible object is
     *     {@link ChallanDetail }
     *     
     */
    public ChallanDetail getGenerateChallanResult() {
        return generateChallanResult;
    }

    /**
     * Sets the value of the generateChallanResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChallanDetail }
     *     
     */
    public void setGenerateChallanResult(ChallanDetail value) {
        this.generateChallanResult = value;
    }

}
