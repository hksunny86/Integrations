
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapRequestBaseWithSession"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="transReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "transReference"
})
@XmlRootElement(name = "SoapPurchaseReprintRequest")
public class SoapPurchaseReprintRequest
    extends SoapRequestBaseWithSession
{

    protected String transReference;

    /**
     * Gets the value of the transReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransReference() {
        return transReference;
    }

    /**
     * Sets the value of the transReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransReference(String value) {
        this.transReference = value;
    }

}
