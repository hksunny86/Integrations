
package com.inov8.integration.middleware.bop.IExternalCommunication;

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
 *         &lt;element name="TranData" type="{http://schemas.datacontract.org/2004/07/BOP.TBG.PRI.WSModel.eGovt}TransactionData" minOccurs="0"/&gt;
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
    "tranData"
})
@XmlRootElement(name = "SaveData", namespace = "http://tempuri.org/")
public class SaveData
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "TranData", namespace = "http://tempuri.org/", nillable = true)
    protected TransactionData tranData;

    /**
     * Gets the value of the tranData property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionData }
     *     
     */
    public TransactionData getTranData() {
        return tranData;
    }

    /**
     * Sets the value of the tranData property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionData }
     *     
     */
    public void setTranData(TransactionData value) {
        this.tranData = value;
    }

}
