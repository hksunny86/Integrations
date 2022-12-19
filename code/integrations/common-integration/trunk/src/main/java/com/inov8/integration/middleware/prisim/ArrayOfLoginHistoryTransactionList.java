
package com.inov8.integration.middleware.prisim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfLoginHistoryTransactionList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfLoginHistoryTransactionList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LoginHistoryTransactionList" type="{http://schemas.datacontract.org/2004/07/SBLServiceModels}LoginHistoryTransactionList" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfLoginHistoryTransactionList", propOrder = {
    "loginHistoryTransactionList"
})
public class ArrayOfLoginHistoryTransactionList
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "LoginHistoryTransactionList", nillable = true)
    protected List<LoginHistoryTransactionList> loginHistoryTransactionList;

    /**
     * Gets the value of the loginHistoryTransactionList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the loginHistoryTransactionList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLoginHistoryTransactionList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LoginHistoryTransactionList }
     * 
     * 
     */
    public List<LoginHistoryTransactionList> getLoginHistoryTransactionList() {
        if (loginHistoryTransactionList == null) {
            loginHistoryTransactionList = new ArrayList<LoginHistoryTransactionList>();
        }
        return this.loginHistoryTransactionList;
    }

}
