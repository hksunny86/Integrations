package com.inov8.microbank.server.webserviceclient.ivr;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ivrRequestDTO complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ivrRequestDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="agentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="agentMobileNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="charges" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="customerMobileNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isCredentialsExpired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isPinAuthenticated" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="pin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="retryCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transactionResponse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientCnic" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipientMobile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="coreAccountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ivrRequestDTO", propOrder = { "agentId", "agentMobileNo", "handlerMobileNo",
		"amount", "charges", "customerMobileNo", "isCredentialsExpired",
		"isPinAuthenticated", "pin", "productId", "retryCount", "transactionId", "transactionResponse" , "recipientCnic", "recipientMobile", "coreAccountNo", "consumerNo" })
public class IvrRequestDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 368555191016039844L;
	protected String agentId;
	protected String agentMobileNo;
	protected String handlerMobileNo;
	protected Double amount;
	protected Double charges;
	protected String customerMobileNo;
	protected Boolean isCredentialsExpired;
	protected Boolean isPinAuthenticated;
	protected String pin;
	protected Long productId;
	protected Integer retryCount;
	protected String transactionId;
	protected String transactionResponse;
	protected String recipientCnic;
	protected String recipientMobile;
	protected String coreAccountNo; 
	protected String consumerNo;
	

	/**
	 * Gets the value of the agentId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAgentId() {
		return agentId;
	}

	/**
	 * Sets the value of the agentId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAgentId(String value) {
		this.agentId = value;
	}

	/**
	 * Gets the value of the agentMobileNo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAgentMobileNo() {
		return agentMobileNo;
	}

	/**
	 * Sets the value of the agentMobileNo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAgentMobileNo(String value) {
		this.agentMobileNo = value;
	}

	public String getHandlerMobileNo() {
		return handlerMobileNo;
	}

	public void setHandlerMobileNo(String value) {
		this.handlerMobileNo = value;
	}

	/**
	 * Gets the value of the amount property.
	 * 
	 * @return possible object is {@link Double }
	 * 
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * Sets the value of the amount property.
	 * 
	 * @param value
	 *            allowed object is {@link Double }
	 * 
	 */
	public void setAmount(Double value) {
		this.amount = value;
	}

	/**
	 * Gets the value of the charges property.
	 * 
	 * @return possible object is {@link Double }
	 * 
	 */
	public Double getCharges() {
		return charges;
	}

	/**
	 * Sets the value of the charges property.
	 * 
	 * @param value
	 *            allowed object is {@link Double }
	 * 
	 */
	public void setCharges(Double value) {
		this.charges = value;
	}

	/**
	 * Gets the value of the customerMobileNo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCustomerMobileNo() {
		return customerMobileNo;
	}

	/**
	 * Sets the value of the customerMobileNo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCustomerMobileNo(String value) {
		this.customerMobileNo = value;
	}

	/**
	 * Gets the value of the isCredentialsExpired property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isIsCredentialsExpired() {
		return isCredentialsExpired;
	}

	/**
	 * Sets the value of the isCredentialsExpired property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setIsCredentialsExpired(Boolean value) {
		this.isCredentialsExpired = value;
	}

	/**
	 * Gets the value of the isPinAuthenticated property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isIsPinAuthenticated() {
		return isPinAuthenticated;
	}

	/**
	 * Sets the value of the isPinAuthenticated property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setIsPinAuthenticated(Boolean value) {
		this.isPinAuthenticated = value;
	}

	/**
	 * Gets the value of the pin property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * Sets the value of the pin property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPin(String value) {
		this.pin = value;
	}

	/**
	 * Gets the value of the productId property.
	 * 
	 * @return possible object is {@link Long }
	 * 
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * Sets the value of the productId property.
	 * 
	 * @param value
	 *            allowed object is {@link Long }
	 * 
	 */
	public void setProductId(Long value) {
		this.productId = value;
	}

	/**
	 * Gets the value of the retryCount property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getRetryCount() {
		return retryCount;
	}

	/**
	 * Sets the value of the retryCount property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setRetryCount(Integer value) {
		this.retryCount = value;
	}

	/**
	 * Gets the value of the transactionId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * Sets the value of the transactionId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTransactionId(String value) {
		this.transactionId = value;
	}

	/**
	 * Gets the value of the transactionResponse property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTransactionResponse() {
		return transactionResponse;
	}

	/**
	 * Sets the value of the transactionResponse property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTransactionResponse(String transactionResponse) {
		this.transactionResponse = transactionResponse;
	}

	public String getRecipientCnic() {
		return recipientCnic;
	}

	public String getRecipientMobile() {
		return recipientMobile;
	}

	public String getCoreAccountNo() {
		return coreAccountNo;
	}

	public void setRecipientCnic(String recipientCnic) {
		this.recipientCnic = recipientCnic;
	}

	public void setRecipientMobile(String recipientMobile) {
		this.recipientMobile = recipientMobile;
	}

	public void setCoreAccountNo(String coreAccountNo) {
		this.coreAccountNo = coreAccountNo;
	}

	public String getConsumerNo() {
		return consumerNo;
	}

	public void setConsumerNo(String consumerNo) {
		this.consumerNo = consumerNo;
	}
}
