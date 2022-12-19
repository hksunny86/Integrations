
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
 *         &lt;element name="GetLoginHistoryResult" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}LoginHistoryOutputParams" minOccurs="0"/&gt;
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
    "getLoginHistoryResult"
})
@XmlRootElement(name = "GetLoginHistoryResponse", namespace = "http://tempuri.org/")
public class GetLoginHistoryResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "GetLoginHistoryResult", namespace = "http://tempuri.org/", nillable = true)
    protected LoginHistoryOutputParams getLoginHistoryResult;

    /**
     * Gets the value of the getLoginHistoryResult property.
     * 
     * @return
     *     possible object is
     *     {@link LoginHistoryOutputParams }
     *     
     */
    public LoginHistoryOutputParams getGetLoginHistoryResult() {
        return getLoginHistoryResult;
    }

    /**
     * Sets the value of the getLoginHistoryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoginHistoryOutputParams }
     *     
     */
    public void setGetLoginHistoryResult(LoginHistoryOutputParams value) {
        this.getLoginHistoryResult = value;
    }

}
