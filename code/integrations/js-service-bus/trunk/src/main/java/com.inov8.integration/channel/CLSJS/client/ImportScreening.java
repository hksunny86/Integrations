
package com.inov8.integration.channel.CLSJS.client;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ImportScreening complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ImportScreening"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}RequestID"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}CNIC"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}CustomerName"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}FatherName"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}DateOfBirth"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}Nationality"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}City"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}CustomerNumber"/&gt;
 *         &lt;element ref="{http://tempuri.org/Screen_MS}UserId"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImportScreening", propOrder = {
        "requestID",
        "cnic",
        "customerName",
        "fatherName",
        "dateOfBirth",
        "nationality",
        "city",
        "customerNumber",
        "userId"
})
public class ImportScreening
        implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "RequestID", required = true)
    protected String requestID;
    @XmlElement(name = "CNIC", required = true)
    protected String cnic;
    @XmlElement(name = "CustomerName", required = true)
    protected String customerName;
    @XmlElement(name = "FatherName", required = true)
    protected String fatherName;
    @XmlElement(name = "DateOfBirth", required = true)
    protected String dateOfBirth;
    @XmlElement(name = "Nationality", required = true)
    protected String nationality;
    @XmlElement(name = "City", required = true)
    protected String city;
    @XmlElement(name = "CustomerNumber", required = true)
    protected String customerNumber;
    @XmlElement(name = "UserId", required = true)
    protected String userId;

    /**
     * Gets the value of the requestID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRequestID(String value) {
        this.requestID = value;
    }

    /**
     * Gets the value of the cnic property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCNIC() {
        return cnic;
    }

    /**
     * Sets the value of the cnic property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCNIC(String value) {
        this.cnic = value;
    }

    /**
     * Gets the value of the fatherName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFatherName() {
        return fatherName;
    }

    /**
     * Sets the value of the fatherName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFatherName(String value) {
        this.fatherName = value;
    }

    /**
     * Gets the value of the customerName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the value of the customerName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCustomerName(String value) {
        this.customerName = value;
    }

    /**
     * Gets the value of the dateOfBirth property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDateOfBirth(String value) {
        this.dateOfBirth = value;
    }

    /**
     * Gets the value of the nationality property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Sets the value of the nationality property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNationality(String value) {
        this.nationality = value;
    }

    /**
     * Gets the value of the city property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the customerNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCustomerNumber() {
        return customerNumber;
    }

    /**
     * Sets the value of the customerNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCustomerNumber(String value) {
        this.customerNumber = value;
    }

    /**
     * Gets the value of the userId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUserId(String value) {
        this.userId = value;
    }

}
