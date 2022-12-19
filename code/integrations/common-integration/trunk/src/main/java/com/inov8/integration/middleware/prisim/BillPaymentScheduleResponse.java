
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
 *         &lt;element name="BillPaymentScheduleResult" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}BillPaymentScheduleOutputParams" minOccurs="0"/&gt;
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
    "billPaymentScheduleResult"
})
@XmlRootElement(name = "BillPaymentScheduleResponse", namespace = "http://tempuri.org/")
public class BillPaymentScheduleResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "BillPaymentScheduleResult", namespace = "http://tempuri.org/", nillable = true)
    protected BillPaymentScheduleOutputParams billPaymentScheduleResult;

    /**
     * Gets the value of the billPaymentScheduleResult property.
     * 
     * @return
     *     possible object is
     *     {@link BillPaymentScheduleOutputParams }
     *     
     */
    public BillPaymentScheduleOutputParams getBillPaymentScheduleResult() {
        return billPaymentScheduleResult;
    }

    /**
     * Sets the value of the billPaymentScheduleResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillPaymentScheduleOutputParams }
     *     
     */
    public void setBillPaymentScheduleResult(BillPaymentScheduleOutputParams value) {
        this.billPaymentScheduleResult = value;
    }

}
