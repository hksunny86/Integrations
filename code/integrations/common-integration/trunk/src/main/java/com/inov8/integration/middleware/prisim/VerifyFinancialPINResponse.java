
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
 *         &lt;element name="VerifyFinancialPINResult" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}VerifyFinancialPinOutputParams" minOccurs="0"/&gt;
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
    "verifyFinancialPINResult"
})
@XmlRootElement(name = "VerifyFinancialPINResponse", namespace = "http://tempuri.org/")
public class VerifyFinancialPINResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "VerifyFinancialPINResult", namespace = "http://tempuri.org/", nillable = true)
    protected VerifyFinancialPinOutputParams verifyFinancialPINResult;

    /**
     * Gets the value of the verifyFinancialPINResult property.
     * 
     * @return
     *     possible object is
     *     {@link VerifyFinancialPinOutputParams }
     *     
     */
    public VerifyFinancialPinOutputParams getVerifyFinancialPINResult() {
        return verifyFinancialPINResult;
    }

    /**
     * Sets the value of the verifyFinancialPINResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link VerifyFinancialPinOutputParams }
     *     
     */
    public void setVerifyFinancialPINResult(VerifyFinancialPinOutputParams value) {
        this.verifyFinancialPINResult = value;
    }

}
