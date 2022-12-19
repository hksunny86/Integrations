
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
 *         &lt;element name="GetChallanStatusResult" type="{http://TaxManagementSystem/}ChallanDetail" minOccurs="0"/&gt;
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
    "getChallanStatusResult"
})
@XmlRootElement(name = "GetChallanStatusResponse")
public class GetChallanStatusResponse {

    @XmlElement(name = "GetChallanStatusResult")
    protected ChallanDetail getChallanStatusResult;

    /**
     * Gets the value of the getChallanStatusResult property.
     * 
     * @return
     *     possible object is
     *     {@link ChallanDetail }
     *     
     */
    public ChallanDetail getGetChallanStatusResult() {
        return getChallanStatusResult;
    }

    /**
     * Sets the value of the getChallanStatusResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChallanDetail }
     *     
     */
    public void setGetChallanStatusResult(ChallanDetail value) {
        this.getChallanStatusResult = value;
    }

}
