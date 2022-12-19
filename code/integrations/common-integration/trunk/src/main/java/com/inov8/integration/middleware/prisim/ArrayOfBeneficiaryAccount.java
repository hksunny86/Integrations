
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfBeneficiaryAccount complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfBeneficiaryAccount"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="BeneficiaryAccount" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}BeneficiaryAccount" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfBeneficiaryAccount", propOrder = {
    "beneficiaryAccount"
})
public class ArrayOfBeneficiaryAccount
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "BeneficiaryAccount", nillable = true)
    protected List<BeneficiaryAccount> beneficiaryAccount;

    /**
     * Gets the value of the beneficiaryAccount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the beneficiaryAccount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBeneficiaryAccount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BeneficiaryAccount }
     * 
     * 
     */
    public List<BeneficiaryAccount> getBeneficiaryAccount() {
        if (beneficiaryAccount == null) {
            beneficiaryAccount = new ArrayList<BeneficiaryAccount>();
        }
        return this.beneficiaryAccount;
    }

}
