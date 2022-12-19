package com.inov8.integration.middleware.cameoo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CardData complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="CardData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PIN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardData", propOrder = {
        "sn",
        "pin",
        "errorCode"
})
public class CardData {

    @XmlElement(name = "SN")
    protected String sn;
    @XmlElement(name = "PIN")
    protected String pin;
    protected String errorCode;

    /**
     * Gets the value of the sn property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSN() {
        return sn;
    }

    /**
     * Sets the value of the sn property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSN(String value) {
        this.sn = value;
    }

    /**
     * Gets the value of the pin property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPIN() {
        return pin;
    }

    /**
     * Sets the value of the pin property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPIN(String value) {
        this.pin = value;
    }

    /**
     * Gets the value of the errorCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

}
