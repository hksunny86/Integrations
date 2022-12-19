
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
 *         &lt;element name="CashWithdrawReversalResult" type="{http://tempuri.org/}ReverselResponse" minOccurs="0"/&gt;
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
    "cashWithdrawReversalResult"
})
@XmlRootElement(name = "CashWithdrawReversalResponse")
public class CashWithdrawReversalResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CashWithdrawReversalResult")
    protected ReverselResponse cashWithdrawReversalResult;

    /**
     * Gets the value of the cashWithdrawReversalResult property.
     * 
     * @return
     *     possible object is
     *     {@link ReverselResponse }
     *     
     */
    public ReverselResponse getCashWithdrawReversalResult() {
        return cashWithdrawReversalResult;
    }

    /**
     * Sets the value of the cashWithdrawReversalResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReverselResponse }
     *     
     */
    public void setCashWithdrawReversalResult(ReverselResponse value) {
        this.cashWithdrawReversalResult = value;
    }

}
