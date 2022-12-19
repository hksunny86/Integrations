package com.inov8.integration.middleware.cameoo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CardRequest complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="CardRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CardDataList" type="{http://tempuri.org/}ArrayOfCardData" minOccurs="0"/>
 *         &lt;element name="UserID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TotalValue" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="CardID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CardName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Qnty" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="RefID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TrnID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="responseCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transactionIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="responseDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardRequest", propOrder = {
        "cardDataList",
        "userID",
        "totalValue",
        "cardID",
        "cardName",
        "qnty",
        "refID",
        "trnID",
        "responseCode",
        "transactionIdentifier",
        "responseDate",
        "errorCode"
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardRequest {

    @XmlElement(name = "CardDataList")
    protected ArrayOfCardData cardDataList;
    @XmlElement(name = "UserID")
    protected int userID;
    @XmlElement(name = "TotalValue")
    protected double totalValue;
    @XmlElement(name = "CardID")
    protected int cardID;
    @XmlElement(name = "CardName")
    protected String cardName;
    @JsonProperty("qnty")
    @XmlElement(name = "Qnty")
    protected int qnty;
    @JsonProperty("refID")
    @XmlElement(name = "RefID")
    protected String refID;
    @JsonIgnore
    @XmlElement(name = "TrnID")
    protected String trnID;
    protected String responseCode;
    @JsonProperty("trnID")
    @XmlElement(name = "trnID")
    protected String transactionIdentifier;
    protected String responseDate;
    protected String errorCode;

    /**
     * Gets the value of the cardDataList property.
     *
     * @return possible object is
     * {@link ArrayOfCardData }
     */
    public ArrayOfCardData getCardDataList() {
        return cardDataList;
    }

    /**
     * Sets the value of the cardDataList property.
     *
     * @param value allowed object is
     *              {@link ArrayOfCardData }
     */
    public void setCardDataList(ArrayOfCardData value) {
        this.cardDataList = value;
    }

    /**
     * Gets the value of the userID property.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     */
    public void setUserID(int value) {
        this.userID = value;
    }

    /**
     * Gets the value of the totalValue property.
     */
    public double getTotalValue() {
        return totalValue;
    }

    /**
     * Sets the value of the totalValue property.
     */
    public void setTotalValue(double value) {
        this.totalValue = value;
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
     * Gets the value of the responseCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the value of the responseCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResponseCode(String value) {
        this.responseCode = value;
    }


    public String getTransactionIdentifier() {
        return (transactionIdentifier != null) ? this.transactionIdentifier : (this.trnID != null) ? this.trnID : null;
    }

    public void setTransactionIdentifier(String transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }

    /**
     * Gets the value of the responseDate property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getResponseDate() {
        return responseDate;
    }

    /**
     * Sets the value of the responseDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResponseDate(String value) {
        this.responseDate = value;
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
