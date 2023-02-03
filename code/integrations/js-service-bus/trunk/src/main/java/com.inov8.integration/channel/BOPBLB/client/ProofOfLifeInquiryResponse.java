
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
 *         &lt;element name="ProofOfLifeInquiryResult" type="{http://tempuri.org/}Proofoflifeinquiryresponse" minOccurs="0"/&gt;
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
    "proofOfLifeInquiryResult"
})
@XmlRootElement(name = "ProofOfLifeInquiryResponse")
public class ProofOfLifeInquiryResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ProofOfLifeInquiryResult")
    protected Proofoflifeinquiryresponse2 proofOfLifeInquiryResult;

    /**
     * Gets the value of the proofOfLifeInquiryResult property.
     * 
     * @return
     *     possible object is
     *     {@link Proofoflifeinquiryresponse2 }
     *     
     */
    public Proofoflifeinquiryresponse2 getProofOfLifeInquiryResult() {
        return proofOfLifeInquiryResult;
    }

    /**
     * Sets the value of the proofOfLifeInquiryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Proofoflifeinquiryresponse2 }
     *     
     */
    public void setProofOfLifeInquiryResult(Proofoflifeinquiryresponse2 value) {
        this.proofOfLifeInquiryResult = value;
    }

}