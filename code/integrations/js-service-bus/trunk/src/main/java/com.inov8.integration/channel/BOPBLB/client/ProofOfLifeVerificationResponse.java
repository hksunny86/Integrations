
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
 *         &lt;element name="ProofOfLifeVerificationResult" type="{http://tempuri.org/}Proofoflifeverificationresponse" minOccurs="0"/&gt;
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
    "proofOfLifeVerificationResult"
})
@XmlRootElement(name = "ProofOfLifeVerificationResponse")
public class ProofOfLifeVerificationResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ProofOfLifeVerificationResult")
    protected Proofoflifeverificationresponse2 proofOfLifeVerificationResult;

    /**
     * Gets the value of the proofOfLifeVerificationResult property.
     * 
     * @return
     *     possible object is
     *     {@link Proofoflifeverificationresponse2 }
     *     
     */
    public Proofoflifeverificationresponse2 getProofOfLifeVerificationResult() {
        return proofOfLifeVerificationResult;
    }

    /**
     * Sets the value of the proofOfLifeVerificationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Proofoflifeverificationresponse2 }
     *     
     */
    public void setProofOfLifeVerificationResult(Proofoflifeverificationresponse2 value) {
        this.proofOfLifeVerificationResult = value;
    }

}
