
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
 *         &lt;element name="CardID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TrnID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Qnty" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
        "cardID",
        "trnID",
        "qnty"
})
@XmlRootElement(name = "RequestCard")
public class RequestCard {

    @XmlElement(name = "UserID")
    protected String userID;
    @XmlElement(name = "PWD")
    protected String pwd;
    @XmlElement(name = "CardID")
    protected int cardID;
    @XmlElement(name = "TrnID")
    protected String trnID;
    @XmlElement(name = "Qnty")
    protected int qnty;

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
     * Gets the value of the cardID property.
     */
    public int getCardID() {
        return cardID;
    }

    /**
     * Sets the value of the cardID property.
     */
    public void setCardID(int value) {
        this.cardID = value;
    }

    /**
     * Gets the value of the trnID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTrnID() {
        return trnID;
    }

    /**
     * Sets the value of the trnID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTrnID(String value) {
        this.trnID = value;
    }

    /**
     * Gets the value of the qnty property.
     */
    public int getQnty() {
        return qnty;
    }

    /**
     * Sets the value of the qnty property.
     */
    public void setQnty(int value) {
        this.qnty = value;
    }

}