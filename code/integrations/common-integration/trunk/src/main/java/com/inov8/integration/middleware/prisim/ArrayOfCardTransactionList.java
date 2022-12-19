
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfCardTransactionList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfCardTransactionList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CardTransactionList" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}CardTransactionList" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfCardTransactionList", propOrder = {
    "cardTransactionList"
})
public class ArrayOfCardTransactionList
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CardTransactionList", nillable = true)
    protected List<CardTransactionList> cardTransactionList;

    /**
     * Gets the value of the cardTransactionList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cardTransactionList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCardTransactionList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CardTransactionList }
     * 
     * 
     */
    public List<CardTransactionList> getCardTransactionList() {
        if (cardTransactionList == null) {
            cardTransactionList = new ArrayList<CardTransactionList>();
        }
        return this.cardTransactionList;
    }

}
