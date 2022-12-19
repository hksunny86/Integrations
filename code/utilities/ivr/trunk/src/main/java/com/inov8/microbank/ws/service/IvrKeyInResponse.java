
package com.inov8.microbank.ws.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ivrKeyInResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ivrKeyInResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="customerMobileNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cardNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="txAmount" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="txCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="txCodeId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="terminalMobileNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="terminalId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="merchantId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="batchNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="invoiceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="apprCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ivrKeyInResponse", propOrder = {
    "errorCode",
    "customerMobileNo",
    "cardNo",
    "txAmount",
    "txCode",
    "txCodeId",
    "terminalMobileNo",
    "terminalId",
    "merchantId",
    "batchNo",
    "invoiceNo",
    "apprCode",
    "errorMsg"
})
public class IvrKeyInResponse {

    protected String errorCode;
    protected String customerMobileNo;
    protected String cardNo;
    protected Double txAmount;
    protected String txCode;
    protected Long txCodeId;
    protected String terminalMobileNo;
    protected Long terminalId;
    protected Long merchantId;
    protected String batchNo;
    protected String invoiceNo;
    protected String apprCode;
	private String errorMsg;

    /**
     * Gets the value of the errorCode property.
     * 
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     * 
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    /**
     * Gets the value of the customerMobileNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    /**
     * Sets the value of the customerMobileNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerMobileNo(String value) {
        this.customerMobileNo = value;
    }

    /**
     * Gets the value of the cardNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * Sets the value of the cardNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardNo(String value) {
        this.cardNo = value;
    }

    /**
     * Gets the value of the txAmount property.
     * 
     */
    public Double getTxAmount() {
        return txAmount;
    }

    /**
     * Sets the value of the txAmount property.
     * 
     */
    public void setTxAmount(Double value) {
        this.txAmount = value;
    }

    /**
     * Gets the value of the txCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxCode() {
        return txCode;
    }

    /**
     * Sets the value of the txCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxCode(String value) {
        this.txCode = value;
    }

    /**
     * Gets the value of the txCodeId property.
     * 
     */
    public Long getTxCodeId() {
        return txCodeId;
    }

    /**
     * Sets the value of the txCodeId property.
     * 
     */
    public void setTxCodeId(Long value) {
        this.txCodeId = value;
    }

    /**
     * Gets the value of the terminalMobileNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminalMobileNo() {
        return terminalMobileNo;
    }

    /**
     * Sets the value of the terminalMobileNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminalMobileNo(String value) {
        this.terminalMobileNo = value;
    }

    /**
     * Gets the value of the terminalId property.
     * 
     */
    public Long getTerminalId() {
        return terminalId;
    }

    /**
     * Sets the value of the terminalId property.
     * 
     */
    public void setTerminalId(Long value) {
        this.terminalId = value;
    }

    /**
     * Gets the value of the merchantId property.
     * 
     */
    public Long getMerchantId() {
        return merchantId;
    }

    /**
     * Sets the value of the merchantId property.
     * 
     */
    public void setMerchantId(Long value) {
        this.merchantId = value;
    }

    /**
     * Gets the value of the batchNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * Sets the value of the batchNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatchNo(String value) {
        this.batchNo = value;
    }

    /**
     * Gets the value of the invoiceNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * Sets the value of the invoiceNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceNo(String value) {
        this.invoiceNo = value;
    }

    /**
     * Gets the value of the apprCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApprCode() {
        return apprCode;
    }

    /**
     * Sets the value of the apprCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApprCode(String value) {
        this.apprCode = value;
    }

	public String getErrorMsg()
	{
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg)
	{
		this.errorMsg = errorMsg;
	}

	@Override
	public String toString()
	{
		return "IvrKeyInResponse [errorCode=" + errorCode
				+ ", customerMobileNo=" + customerMobileNo + ", cardNo="
				+ cardNo + ", txAmount=" + txAmount + ", txCode=" + txCode
				+ ", txCodeId=" + txCodeId + ", terminalMobileNo="
				+ terminalMobileNo + ", terminalId=" + terminalId
				+ ", merchantId=" + merchantId + ", batchNo=" + batchNo
				+ ", invoiceNo=" + invoiceNo + ", apprCode=" + apprCode
				+ ", errorMsg=" + errorMsg + "]";
	}

}
