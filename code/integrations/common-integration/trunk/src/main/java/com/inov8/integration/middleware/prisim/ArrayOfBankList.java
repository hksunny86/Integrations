
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfBankList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfBankList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="BankList" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}BankList" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfBankList", propOrder = {
    "bankList"
})
public class ArrayOfBankList
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "BankList", nillable = true)
    protected List<BankList> bankList;

    /**
     * Gets the value of the bankList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bankList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBankList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BankList }
     * 
     * 
     */
    public List<BankList> getBankList() {
        if (bankList == null) {
            bankList = new ArrayList<BankList>();
        }
        return this.bankList;
    }

}
