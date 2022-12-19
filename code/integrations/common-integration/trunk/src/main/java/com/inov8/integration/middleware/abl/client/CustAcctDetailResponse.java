
package com.inov8.integration.middleware.abl.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="CNIC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CUSTNAME" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="EMAIL" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="MOBNO" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="IBAN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ACCTCURRENY" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CURRENYMNEMONIC" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="AVAILABLEBALANCE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CURRENTBALANCE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ACCTTYPE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="BRANCHCODE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="BRANCHNAME" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ACCOUTNAME" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="BANKIMD" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ACCNO"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Acclist" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
    "cnic",
    "custname",
    "email",
    "mobno",
    "iban",
    "acctcurreny",
    "currenymnemonic",
    "availablebalance",
    "currentbalance",
    "accttype",
    "branchcode",
    "branchname",
    "accoutname",
    "bankimd",
    "accno"
})
@XmlRootElement(name = "CustAcctDetailResponse")
public class CustAcctDetailResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CNIC", nillable = true)
    protected String cnic;
    @XmlElement(name = "CUSTNAME", required = true)
    protected String custname;
    @XmlElement(name = "EMAIL", required = true)
    protected String email;
    @XmlElement(name = "MOBNO", required = true)
    protected String mobno;
    @XmlElement(name = "IBAN", required = true)
    protected String iban;
    @XmlElement(name = "ACCTCURRENY", required = true)
    protected String acctcurreny;
    @XmlElement(name = "CURRENYMNEMONIC", required = true)
    protected String currenymnemonic;
    @XmlElement(name = "AVAILABLEBALANCE", required = true)
    protected String availablebalance;
    @XmlElement(name = "CURRENTBALANCE", required = true)
    protected String currentbalance;
    @XmlElement(name = "ACCTTYPE", required = true)
    protected String accttype;
    @XmlElement(name = "BRANCHCODE", required = true)
    protected String branchcode;
    @XmlElement(name = "BRANCHNAME", required = true)
    protected String branchname;
    @XmlElement(name = "ACCOUTNAME", required = true)
    protected String accoutname;
    @XmlElement(name = "BANKIMD", required = true)
    protected String bankimd;
    @XmlElement(name = "ACCNO", required = true)
    protected CustAcctDetailResponse.ACCNO accno;

    /**
     * Gets the value of the cnic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNIC() {
        return cnic;
    }

    /**
     * Sets the value of the cnic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNIC(String value) {
        this.cnic = value;
    }

    /**
     * Gets the value of the custname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUSTNAME() {
        return custname;
    }

    /**
     * Sets the value of the custname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUSTNAME(String value) {
        this.custname = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEMAIL() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEMAIL(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the mobno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMOBNO() {
        return mobno;
    }

    /**
     * Sets the value of the mobno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMOBNO(String value) {
        this.mobno = value;
    }

    /**
     * Gets the value of the iban property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIBAN() {
        return iban;
    }

    /**
     * Sets the value of the iban property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIBAN(String value) {
        this.iban = value;
    }

    /**
     * Gets the value of the acctcurreny property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACCTCURRENY() {
        return acctcurreny;
    }

    /**
     * Sets the value of the acctcurreny property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACCTCURRENY(String value) {
        this.acctcurreny = value;
    }

    /**
     * Gets the value of the currenymnemonic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCURRENYMNEMONIC() {
        return currenymnemonic;
    }

    /**
     * Sets the value of the currenymnemonic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCURRENYMNEMONIC(String value) {
        this.currenymnemonic = value;
    }

    /**
     * Gets the value of the availablebalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAVAILABLEBALANCE() {
        return availablebalance;
    }

    /**
     * Sets the value of the availablebalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAVAILABLEBALANCE(String value) {
        this.availablebalance = value;
    }

    /**
     * Gets the value of the currentbalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCURRENTBALANCE() {
        return currentbalance;
    }

    /**
     * Sets the value of the currentbalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCURRENTBALANCE(String value) {
        this.currentbalance = value;
    }

    /**
     * Gets the value of the accttype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACCTTYPE() {
        return accttype;
    }

    /**
     * Sets the value of the accttype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACCTTYPE(String value) {
        this.accttype = value;
    }

    /**
     * Gets the value of the branchcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBRANCHCODE() {
        return branchcode;
    }

    /**
     * Sets the value of the branchcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBRANCHCODE(String value) {
        this.branchcode = value;
    }

    /**
     * Gets the value of the branchname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBRANCHNAME() {
        return branchname;
    }

    /**
     * Sets the value of the branchname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBRANCHNAME(String value) {
        this.branchname = value;
    }

    /**
     * Gets the value of the accoutname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACCOUTNAME() {
        return accoutname;
    }

    /**
     * Sets the value of the accoutname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACCOUTNAME(String value) {
        this.accoutname = value;
    }

    /**
     * Gets the value of the bankimd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBANKIMD() {
        return bankimd;
    }

    /**
     * Sets the value of the bankimd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBANKIMD(String value) {
        this.bankimd = value;
    }

    /**
     * Gets the value of the accno property.
     * 
     * @return
     *     possible object is
     *     {@link CustAcctDetailResponse.ACCNO }
     *     
     */
    public CustAcctDetailResponse.ACCNO getACCNO() {
        return accno;
    }

    /**
     * Sets the value of the accno property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustAcctDetailResponse.ACCNO }
     *     
     */
    public void setACCNO(CustAcctDetailResponse.ACCNO value) {
        this.accno = value;
    }


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
     *         &lt;element name="Acclist" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
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
        "acclist"
    })
    public static class ACCNO
        implements Serializable
    {

        private final static long serialVersionUID = 1L;
        @XmlElement(name = "Acclist")
        protected List<String> acclist;

        /**
         * Gets the value of the acclist property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the acclist property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAcclist().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getAcclist() {
            if (acclist == null) {
                acclist = new ArrayList<String>();
            }
            return this.acclist;
        }

    }

}
