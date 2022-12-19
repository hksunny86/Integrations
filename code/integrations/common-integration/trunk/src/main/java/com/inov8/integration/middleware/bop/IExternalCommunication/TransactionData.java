
package com.inov8.integration.middleware.bop.IExternalCommunication;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for TransactionData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransactionData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AccountHead" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="BeneficiaryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CNIC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ChallanDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="ChallanNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ChannelID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="DepartmentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DistrictCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FatherName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HeadDetailList" type="{http://schemas.datacontract.org/2004/07/BOP.TBG.PRI.WSModel.eGovt}ArrayOfHeadsDetail" minOccurs="0"/&gt;
 *         &lt;element name="ReportingDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="TransactionAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="TransactionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TransactionType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionData", propOrder = {
    "accountHead",
    "amount",
    "beneficiaryName",
    "cnic",
    "challanDate",
    "challanNumber",
    "channelID",
    "departmentCode",
    "districtCode",
    "fatherName",
    "headDetailList",
    "reportingDate",
    "transactionAmount",
    "transactionID",
    "transactionType"
})
public class TransactionData
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "AccountHead", nillable = true)
    protected String accountHead;
    @XmlElement(name = "Amount")
    protected BigDecimal amount;
    @XmlElement(name = "BeneficiaryName", nillable = true)
    protected String beneficiaryName;
    @XmlElement(name = "CNIC", nillable = true)
    protected String cnic;
    @XmlElement(name = "ChallanDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar challanDate;
    @XmlElement(name = "ChallanNumber", nillable = true)
    protected String challanNumber;
    @XmlElement(name = "ChannelID")
    protected Integer channelID;
    @XmlElement(name = "DepartmentCode", nillable = true)
    protected String departmentCode;
    @XmlElement(name = "DistrictCode", nillable = true)
    protected String districtCode;
    @XmlElement(name = "FatherName", nillable = true)
    protected String fatherName;
    @XmlElement(name = "HeadDetailList", nillable = true)
    protected ArrayOfHeadsDetail headDetailList;
    @XmlElement(name = "ReportingDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar reportingDate;
    @XmlElement(name = "TransactionAmount")
    protected BigDecimal transactionAmount;
    @XmlElement(name = "TransactionID", nillable = true)
    protected String transactionID;
    @XmlElement(name = "TransactionType", nillable = true)
    protected String transactionType;

    /**
     * Gets the value of the accountHead property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountHead() {
        return accountHead;
    }

    /**
     * Sets the value of the accountHead property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountHead(String value) {
        this.accountHead = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Gets the value of the beneficiaryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    /**
     * Sets the value of the beneficiaryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeneficiaryName(String value) {
        this.beneficiaryName = value;
    }

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
     * Gets the value of the challanDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getChallanDate() {
        return challanDate;
    }

    /**
     * Sets the value of the challanDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setChallanDate(XMLGregorianCalendar value) {
        this.challanDate = value;
    }

    /**
     * Gets the value of the challanNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChallanNumber() {
        return challanNumber;
    }

    /**
     * Sets the value of the challanNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChallanNumber(String value) {
        this.challanNumber = value;
    }

    /**
     * Gets the value of the channelID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getChannelID() {
        return channelID;
    }

    /**
     * Sets the value of the channelID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setChannelID(Integer value) {
        this.channelID = value;
    }

    /**
     * Gets the value of the departmentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartmentCode() {
        return departmentCode;
    }

    /**
     * Sets the value of the departmentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartmentCode(String value) {
        this.departmentCode = value;
    }

    /**
     * Gets the value of the districtCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistrictCode() {
        return districtCode;
    }

    /**
     * Sets the value of the districtCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistrictCode(String value) {
        this.districtCode = value;
    }

    /**
     * Gets the value of the fatherName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFatherName() {
        return fatherName;
    }

    /**
     * Sets the value of the fatherName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFatherName(String value) {
        this.fatherName = value;
    }

    /**
     * Gets the value of the headDetailList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfHeadsDetail }
     *     
     */
    public ArrayOfHeadsDetail getHeadDetailList() {
        return headDetailList;
    }

    /**
     * Sets the value of the headDetailList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfHeadsDetail }
     *     
     */
    public void setHeadDetailList(ArrayOfHeadsDetail value) {
        this.headDetailList = value;
    }

    /**
     * Gets the value of the reportingDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReportingDate() {
        return reportingDate;
    }

    /**
     * Sets the value of the reportingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReportingDate(XMLGregorianCalendar value) {
        this.reportingDate = value;
    }

    /**
     * Gets the value of the transactionAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * Sets the value of the transactionAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTransactionAmount(BigDecimal value) {
        this.transactionAmount = value;
    }

    /**
     * Gets the value of the transactionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionID() {
        return transactionID;
    }

    /**
     * Sets the value of the transactionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionID(String value) {
        this.transactionID = value;
    }

    /**
     * Gets the value of the transactionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the value of the transactionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionType(String value) {
        this.transactionType = value;
    }

}
