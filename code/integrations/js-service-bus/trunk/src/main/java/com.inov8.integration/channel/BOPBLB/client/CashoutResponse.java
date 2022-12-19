
package com.inov8.integration.channel.BOPBLB.client;

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
 *         &lt;element name="CashOutResult" type="{http://tempuri.org/}CashoutResponse" minOccurs="0"/&gt;
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
    "cashOutResult"
})
@XmlRootElement(name = "CashOutResponse")
public class CashoutResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CashOutResult")
    protected CashOutResponse2 cashOutResult;

    /**
     * Gets the value of the cashOutResult property.
     * 
     * @return
     *     possible object is
     *     {@link CashOutResponse2 }
     *     
     */
    public CashOutResponse2 getCashOutResult() {
        return cashOutResult;
    }

    /**
     * Sets the value of the cashOutResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CashOutResponse2 }
     *     
     */
    public void setCashOutResult(CashOutResponse2 value) {
        this.cashOutResult = value;
    }

}
