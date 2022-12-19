
package com.inov8.integration.middleware.bop.nadra;

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
 *         &lt;element name="Credentials" type="{http://schemas.datacontract.org/2004/07/ExternalChannels.Verification}UserCredentials" minOccurs="0"/&gt;
 *         &lt;element name="NadraDTO" type="{http://schemas.datacontract.org/2004/07/ExternalChannels.Verification.IModel}NADRA_DTO" minOccurs="0"/&gt;
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
    "credentials",
    "nadraDTO"
})
@XmlRootElement(name = "VerifyCitizenWithSegment")
public class VerifyCitizenWithSegment
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Credentials", nillable = true)
    protected UserCredentials credentials;
    @XmlElement(name = "NadraDTO", nillable = true)
    protected NADRADTO nadraDTO;

    /**
     * Gets the value of the credentials property.
     * 
     * @return
     *     possible object is
     *     {@link UserCredentials }
     *     
     */
    public UserCredentials getCredentials() {
        return credentials;
    }

    /**
     * Sets the value of the credentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserCredentials }
     *     
     */
    public void setCredentials(UserCredentials value) {
        this.credentials = value;
    }

    /**
     * Gets the value of the nadraDTO property.
     * 
     * @return
     *     possible object is
     *     {@link NADRADTO }
     *     
     */
    public NADRADTO getNadraDTO() {
        return nadraDTO;
    }

    /**
     * Sets the value of the nadraDTO property.
     * 
     * @param value
     *     allowed object is
     *     {@link NADRADTO }
     *     
     */
    public void setNadraDTO(NADRADTO value) {
        this.nadraDTO = value;
    }

}
