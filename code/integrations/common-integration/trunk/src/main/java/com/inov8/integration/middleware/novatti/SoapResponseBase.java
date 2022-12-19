
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for SoapResponseBase complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SoapResponseBase"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header" type="{http://soap.api.novatti.com/types}ResponseHeader" minOccurs="0"/&gt;
 *         &lt;element name="keyValues" type="{http://soap.api.novatti.com/types}Map" minOccurs="0"/&gt;
 *         &lt;element name="validationInfo" type="{http://soap.api.novatti.com/types}ValidationGroups" minOccurs="0"/&gt;
 *         &lt;element name="dataHash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="notifyMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoapResponseBase", propOrder = {
    "header",
    "keyValues",
    "validationInfo",
    "dataHash",
    "notifyMessage"
})
@XmlSeeAlso({
    SoapTransactionInfoResponse.class,
    SoapSystemStatusResponse.class,
    SoapSubscriberTopupReversalResponse.class,
    SoapSubscriberTopupResponse.class,
    SoapSubscriberTopupQuoteResponse.class,
    SoapSubscriberQueryResponse.class,
    SoapSubscriberHistoryResponse.class,
    SoapSessionValidateResponse.class,
    SoapSelectionListInfoResponse.class,
    SoapRoleInfoResponse.class,
    SoapResolveMsisdnResponse.class,
    SoapPurchaseReversalResponse.class,
    SoapPurchaseReprintResponse.class,
    SoapPurchaseRefundResponse.class,
    SoapProductPurchaseResponse.class,
    SoapPinBatchStatusResponse.class,
    SoapPinBatchResponse.class,
    SoapPinBatchReceiveResponse.class,
    SoapProductRedeemResponse.class,
    SoapLanguageInfoResponse.class,
    SoapDocumentsInfoResponse.class,
    SoapCurrencyInfoResponse.class,
    SoapCountryInfoResponse.class,
    SoapBulletinInfoResponse.class,
    SoapAgentWalletTransferResponse.class,
    SoapAgentUpdateResponse.class,
    SoapAgentTemplateResponse.class,
    SoapAgentStructureResponse.class,
    SoapAgentSettlementResponse.class,
    SoapAgentSearchResponse.class,
    SoapAgentProfileResponse.class,
    SoapAgentProductsResponse.class,
    SoapAgentLoginResponse.class,
    SoapAgentInfoResponse.class,
    SoapAgentCreateResponse.class,
    SoapAgentChangePasswordResponse.class,
    SoapAgentChangeAuthkeyResponse.class,
    SoapAgentAddressesResponse.class
})
public class SoapResponseBase implements Serializable {

    protected ResponseHeader header;
    protected Map keyValues;
    protected ValidationGroups validationInfo;
    protected String dataHash;
    protected String notifyMessage;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseHeader }
     *     
     */
    public ResponseHeader getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseHeader }
     *     
     */
    public void setHeader(ResponseHeader value) {
        this.header = value;
    }

    /**
     * Gets the value of the keyValues property.
     * 
     * @return
     *     possible object is
     *     {@link Map }
     *     
     */
    public Map getKeyValues() {
        return keyValues;
    }

    /**
     * Sets the value of the keyValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link Map }
     *     
     */
    public void setKeyValues(Map value) {
        this.keyValues = value;
    }

    /**
     * Gets the value of the validationInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ValidationGroups }
     *     
     */
    public ValidationGroups getValidationInfo() {
        return validationInfo;
    }

    /**
     * Sets the value of the validationInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValidationGroups }
     *     
     */
    public void setValidationInfo(ValidationGroups value) {
        this.validationInfo = value;
    }

    /**
     * Gets the value of the dataHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataHash() {
        return dataHash;
    }

    /**
     * Sets the value of the dataHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataHash(String value) {
        this.dataHash = value;
    }

    /**
     * Gets the value of the notifyMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotifyMessage() {
        return notifyMessage;
    }

    /**
     * Sets the value of the notifyMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotifyMessage(String value) {
        this.notifyMessage = value;
    }

}
