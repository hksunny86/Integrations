package com.inov8.integration.middleware.cameoo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CardBalance complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="CardBalance">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CardID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CardName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Balance" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardBalance", propOrder = {
        "cardID",
        "cardName",
        "balance"
})
public class CardBalance {

    @XmlElement(name = "CardID")
    protected int cardID;
    @XmlElement(name = "CardName")
    protected String cardName;
    @XmlElement(name = "Balance")
    protected int balance;

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
     * Gets the value of the cardName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * Sets the value of the cardName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCardName(String value) {
        this.cardName = value;
    }

    /**
     * Gets the value of the balance property.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Sets the value of the balance property.
     */
    public void setBalance(int value) {
        this.balance = value;
    }

}
