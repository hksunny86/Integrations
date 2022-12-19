
package com.inov8.integration.channel.JSBLB.ETPaymentCollection.client;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AssesmentDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AssesmentDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="VctAssesmentNO" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="AssesmentDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="RegistrationNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RegistrationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="ChassisNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MarkerMake" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Category" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BodyType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="EngCapacity" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Seats" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Cylinders" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="OwnerCnic" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OwnerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FilerStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TaxPaidFrom" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="TaxPaidUpTo" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="VehTaxPaidLifeTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VehicalStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FitnessDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="TotalAmount" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="StatusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssesmentDetails", propOrder = {
    "vctAssesmentNO",
    "assesmentDate",
    "registrationNo",
    "registrationDate",
    "chassisNo",
    "markerMake",
    "category",
    "bodyType",
    "engCapacity",
    "seats",
    "cylinders",
    "ownerCnic",
    "ownerName",
    "filerStatus",
    "taxPaidFrom",
    "taxPaidUpTo",
    "vehTaxPaidLifeTime",
    "vehicalStatus",
    "fitnessDate",
    "totalAmount",
    "statusCode"
})
public class AssesmentDetails {

    @XmlElement(name = "VctAssesmentNO")
    protected int vctAssesmentNO;
    @XmlElement(name = "AssesmentDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar assesmentDate;
    @XmlElement(name = "RegistrationNo")
    protected String registrationNo;
    @XmlElement(name = "RegistrationDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar registrationDate;
    @XmlElement(name = "ChassisNo")
    protected String chassisNo;
    @XmlElement(name = "MarkerMake")
    protected String markerMake;
    @XmlElement(name = "Category")
    protected String category;
    @XmlElement(name = "BodyType")
    protected String bodyType;
    @XmlElement(name = "EngCapacity")
    protected int engCapacity;
    @XmlElement(name = "Seats")
    protected int seats;
    @XmlElement(name = "Cylinders")
    protected int cylinders;
    @XmlElement(name = "OwnerCnic")
    protected String ownerCnic;
    @XmlElement(name = "OwnerName")
    protected String ownerName;
    @XmlElement(name = "FilerStatus")
    protected String filerStatus;
    @XmlElement(name = "TaxPaidFrom", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar taxPaidFrom;
    @XmlElement(name = "TaxPaidUpTo", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar taxPaidUpTo;
    @XmlElement(name = "VehTaxPaidLifeTime")
    protected String vehTaxPaidLifeTime;
    @XmlElement(name = "VehicalStatus")
    protected String vehicalStatus;
    @XmlElement(name = "FitnessDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fitnessDate;
    @XmlElement(name = "TotalAmount", required = true)
    protected BigDecimal totalAmount;
    @XmlElement(name = "StatusCode")
    protected String statusCode;

    /**
     * Gets the value of the vctAssesmentNO property.
     * 
     */
    public int getVctAssesmentNO() {
        return vctAssesmentNO;
    }

    /**
     * Sets the value of the vctAssesmentNO property.
     * 
     */
    public void setVctAssesmentNO(int value) {
        this.vctAssesmentNO = value;
    }

    /**
     * Gets the value of the assesmentDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAssesmentDate() {
        return assesmentDate;
    }

    /**
     * Sets the value of the assesmentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAssesmentDate(XMLGregorianCalendar value) {
        this.assesmentDate = value;
    }

    /**
     * Gets the value of the registrationNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistrationNo() {
        return registrationNo;
    }

    /**
     * Sets the value of the registrationNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistrationNo(String value) {
        this.registrationNo = value;
    }

    /**
     * Gets the value of the registrationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Sets the value of the registrationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRegistrationDate(XMLGregorianCalendar value) {
        this.registrationDate = value;
    }

    /**
     * Gets the value of the chassisNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChassisNo() {
        return chassisNo;
    }

    /**
     * Sets the value of the chassisNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChassisNo(String value) {
        this.chassisNo = value;
    }

    /**
     * Gets the value of the markerMake property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarkerMake() {
        return markerMake;
    }

    /**
     * Sets the value of the markerMake property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarkerMake(String value) {
        this.markerMake = value;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategory(String value) {
        this.category = value;
    }

    /**
     * Gets the value of the bodyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBodyType() {
        return bodyType;
    }

    /**
     * Sets the value of the bodyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBodyType(String value) {
        this.bodyType = value;
    }

    /**
     * Gets the value of the engCapacity property.
     * 
     */
    public int getEngCapacity() {
        return engCapacity;
    }

    /**
     * Sets the value of the engCapacity property.
     * 
     */
    public void setEngCapacity(int value) {
        this.engCapacity = value;
    }

    /**
     * Gets the value of the seats property.
     * 
     */
    public int getSeats() {
        return seats;
    }

    /**
     * Sets the value of the seats property.
     * 
     */
    public void setSeats(int value) {
        this.seats = value;
    }

    /**
     * Gets the value of the cylinders property.
     * 
     */
    public int getCylinders() {
        return cylinders;
    }

    /**
     * Sets the value of the cylinders property.
     * 
     */
    public void setCylinders(int value) {
        this.cylinders = value;
    }

    /**
     * Gets the value of the ownerCnic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerCnic() {
        return ownerCnic;
    }

    /**
     * Sets the value of the ownerCnic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerCnic(String value) {
        this.ownerCnic = value;
    }

    /**
     * Gets the value of the ownerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Sets the value of the ownerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerName(String value) {
        this.ownerName = value;
    }

    /**
     * Gets the value of the filerStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilerStatus() {
        return filerStatus;
    }

    /**
     * Sets the value of the filerStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilerStatus(String value) {
        this.filerStatus = value;
    }

    /**
     * Gets the value of the taxPaidFrom property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTaxPaidFrom() {
        return taxPaidFrom;
    }

    /**
     * Sets the value of the taxPaidFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTaxPaidFrom(XMLGregorianCalendar value) {
        this.taxPaidFrom = value;
    }

    /**
     * Gets the value of the taxPaidUpTo property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTaxPaidUpTo() {
        return taxPaidUpTo;
    }

    /**
     * Sets the value of the taxPaidUpTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTaxPaidUpTo(XMLGregorianCalendar value) {
        this.taxPaidUpTo = value;
    }

    /**
     * Gets the value of the vehTaxPaidLifeTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVehTaxPaidLifeTime() {
        return vehTaxPaidLifeTime;
    }

    /**
     * Sets the value of the vehTaxPaidLifeTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVehTaxPaidLifeTime(String value) {
        this.vehTaxPaidLifeTime = value;
    }

    /**
     * Gets the value of the vehicalStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVehicalStatus() {
        return vehicalStatus;
    }

    /**
     * Sets the value of the vehicalStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVehicalStatus(String value) {
        this.vehicalStatus = value;
    }

    /**
     * Gets the value of the fitnessDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFitnessDate() {
        return fitnessDate;
    }

    /**
     * Sets the value of the fitnessDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFitnessDate(XMLGregorianCalendar value) {
        this.fitnessDate = value;
    }

    /**
     * Gets the value of the totalAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the value of the totalAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalAmount(BigDecimal value) {
        this.totalAmount = value;
    }

    /**
     * Gets the value of the statusCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusCode(String value) {
        this.statusCode = value;
    }

}
