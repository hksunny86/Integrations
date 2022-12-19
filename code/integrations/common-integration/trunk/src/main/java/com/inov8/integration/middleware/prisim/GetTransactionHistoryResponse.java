
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
 *         &lt;element name="GetTransactionHistoryResult" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}TransactionHistoryOutputParams" minOccurs="0"/&gt;
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
    "getTransactionHistoryResult"
})
@XmlRootElement(name = "GetTransactionHistoryResponse", namespace = "http://tempuri.org/")
public class GetTransactionHistoryResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "GetTransactionHistoryResult", namespace = "http://tempuri.org/", nillable = true)
    protected TransactionHistoryOutputParams getTransactionHistoryResult;

    /**
     * Gets the value of the getTransactionHistoryResult property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionHistoryOutputParams }
     *     
     */
    public TransactionHistoryOutputParams getGetTransactionHistoryResult() {
        return getTransactionHistoryResult;
    }

    /**
     * Sets the value of the getTransactionHistoryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionHistoryOutputParams }
     *     
     */
    public void setGetTransactionHistoryResult(TransactionHistoryOutputParams value) {
        this.getTransactionHistoryResult = value;
    }

}
