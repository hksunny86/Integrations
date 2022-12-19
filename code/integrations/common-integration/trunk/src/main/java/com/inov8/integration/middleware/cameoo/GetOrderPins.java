package com.inov8.integration.middleware.cameoo;


import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PWD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RefID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "userID",
        "pwd",
        "refID"
})
@XmlRootElement(name = "GetOrderPins")
public class GetOrderPins {

    @XmlElement(name = "UserID")
    protected String userID;
    @XmlElement(name = "PWD")
    protected String pwd;
    @XmlElement(name = "RefID")
    protected String refID;

    /**
     * Gets the value of the userID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUserID(String value) {
        this.userID = value;
    }

    /**
     * Gets the value of the pwd property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPWD() {
        return pwd;
    }

    /**
     * Sets the value of the pwd property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPWD(String value) {
        this.pwd = value;
    }

    /**
     * Gets the value of the refID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRefID() {
        return refID;
    }

    /**
     * Sets the value of the refID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRefID(String value) {
        this.refID = value;
    }

}
