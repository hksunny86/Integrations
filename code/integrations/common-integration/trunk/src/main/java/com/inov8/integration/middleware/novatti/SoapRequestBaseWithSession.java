
package com.inov8.integration.middleware.novatti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SoapRequestBaseWithSession complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SoapRequestBaseWithSession"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://soap.api.novatti.com/types}SoapRequestBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header" type="{http://soap.api.novatti.com/types}RequestHeaderWithSession" minOccurs="0"/&gt;
 *         &lt;element name="keyValues" type="{http://soap.api.novatti.com/types}Map" minOccurs="0"/&gt;
 *         &lt;element name="dataHash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoapRequestBaseWithSession", propOrder = {
    "header",
    "keyValues",
    "dataHash"
})
@XmlSeeAlso({
    SoapTransactionInfoRequest.class,
    SoapSubscriberTopupReversalRequest.class,
    SoapSubscriberTopupRequest.class,
    SoapSubscriberTopupQuoteRequest.class,
    SoapSubscriberQueryRequest.class,
    SoapSubscriberHistoryRequest.class,
    SoapSessionValidateRequest.class,
    SoapSelectionListInfoRequest.class,
    SoapRoleInfoRequest.class,
    SoapResolveMsisdnRequest.class,
    SoapPurchaseReversalRequest.class,
    SoapPurchaseReprintRequest.class,
    SoapPurchaseRefundRequest.class,
    SoapProductPurchaseRequest.class,
    SoapPinBatchStatusRequest.class,
    SoapPinBatchRequest.class,
    SoapPinBatchReceiveRequest.class,
    SoapProductRedeemRequest.class,
    SoapLanguageInfoRequest.class,
    SoapDocumentsInfoRequest.class,
    SoapCurrencyInfoRequest.class,
    SoapCountryInfoRequest.class,
    SoapAgentWalletTransferRequest.class,
    SoapAgentUpdateRequest.class,
    SoapAgentTemplateRequest.class,
    SoapAgentStructureRequest.class,
    SoapAgentSettlementRequest.class,
    SoapAgentSearchRequest.class,
    SoapAgentProfileRequest.class,
    SoapAgentProductsRequest.class,
    SoapAgentInfoRequest.class,
    SoapAgentCreateRequest.class,
    SoapAgentChangeAuthkeyRequest.class,
    SoapAgentAddressesRequest.class
})
public class SoapRequestBaseWithSession
    extends SoapRequestBase
{

    protected RequestHeaderWithSession header;
    protected Map keyValues;
    protected String dataHash;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link RequestHeaderWithSession }
     *     
     */
    public RequestHeaderWithSession getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestHeaderWithSession }
     *     
     */
    public void setHeader(RequestHeaderWithSession value) {
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

}
