
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
 *         &lt;element name="orderRef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="batchRequestAgentTransId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "orderRef",
    "batchRequestAgentTransId"
})
@XmlRootElement(name = "SoapPinBatchStatusRequest")
public class SoapPinBatchStatusRequest
    extends SoapRequestBaseWithSession
{

    protected String orderRef;
    protected String batchRequestAgentTransId;

    /**
     * Gets the value of the orderRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderRef() {
        return orderRef;
    }

    /**
     * Sets the value of the orderRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderRef(String value) {
        this.orderRef = value;
    }

    /**
     * Gets the value of the batchRequestAgentTransId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatchRequestAgentTransId() {
        return batchRequestAgentTransId;
    }

    /**
     * Sets the value of the batchRequestAgentTransId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatchRequestAgentTransId(String value) {
        this.batchRequestAgentTransId = value;
    }

}
