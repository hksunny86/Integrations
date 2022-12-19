
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
 *         &lt;element name="DeleteBeneficiaryAccountResult" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}DeleteBeneficiaryAccountOutputParams" minOccurs="0"/&gt;
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
    "deleteBeneficiaryAccountResult"
})
@XmlRootElement(name = "DeleteBeneficiaryAccountResponse", namespace = "http://tempuri.org/")
public class DeleteBeneficiaryAccountResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "DeleteBeneficiaryAccountResult", namespace = "http://tempuri.org/", nillable = true)
    protected DeleteBeneficiaryAccountOutputParams deleteBeneficiaryAccountResult;

    /**
     * Gets the value of the deleteBeneficiaryAccountResult property.
     * 
     * @return
     *     possible object is
     *     {@link DeleteBeneficiaryAccountOutputParams }
     *     
     */
    public DeleteBeneficiaryAccountOutputParams getDeleteBeneficiaryAccountResult() {
        return deleteBeneficiaryAccountResult;
    }

    /**
     * Sets the value of the deleteBeneficiaryAccountResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteBeneficiaryAccountOutputParams }
     *     
     */
    public void setDeleteBeneficiaryAccountResult(DeleteBeneficiaryAccountOutputParams value) {
        this.deleteBeneficiaryAccountResult = value;
    }

}
