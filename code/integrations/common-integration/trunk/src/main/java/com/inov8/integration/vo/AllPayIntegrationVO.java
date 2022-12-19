package com.inov8.integration.vo;

import com.inov8.integration.middleware.allpay.pdu.response.Product;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import java.util.Date;
import java.util.List;

public class AllPayIntegrationVO implements IntegrationMessageVO
{
    private static final long serialVersionUID = -6738986346088622347L;
    private String username;
    private String password;
    private Date requestDate;
    private String microbankTransactionCode;
    private String channelId;
    private String agentTransactionId;
    private String productId;
    private String supplierId;
    private String serialNo;
    private String pin;
    private String charges;
    private Date expiryDate;
    private String denominationCode;
    private String value;
    private String quantity;
    private List<Product> products;
    private String responseCode;
    private String responseDescription;
    private String messageAsEdi;
    private String CNIC;
    private String MSISDN;
    private String orderId;
    private String orderDescription;
    private String orderStatus;
    private String dueDate;
    private String amountWithinDueDate;
    private String amountAfterDueDate;
    private String billingMonth;
    private String datePaid;
    private String amountPaid;
    private String transactionAuthID;
    private String transactionInitialized;


    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Date getRequestDate()
    {
        return this.requestDate;
    }

    public void setRequestDate(Date requestDate)
    {
        this.requestDate = requestDate;
    }

    public String getMicrobankTransactionCode()
    {
        return this.microbankTransactionCode;
    }

    public void setMicrobankTransactionCode(String microbankTransactionCode)
    {
        this.microbankTransactionCode = microbankTransactionCode;
    }

    public String getChannelId()
    {
        return this.channelId;
    }

    public void setChannelId(String channelId)
    {
        this.channelId = channelId;
    }

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public String getSupplierId()
    {
        return this.supplierId;
    }

    public void setSupplierId(String supplierId)
    {
        this.supplierId = supplierId;
    }

    public String getDenominationCode()
    {
        return this.denominationCode;
    }

    public void setDenominationCode(String denominationCode)
    {
        this.denominationCode = denominationCode;
    }

    public String getValue()
    {
        return this.value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getQuantity()
    {
        return this.quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }

    public List<Product> getProducts()
    {
        return this.products;
    }

    public void setProducts(List<Product> products)
    {
        this.products = products;
    }

    public String getResponseCode()
    {
        return this.responseCode;
    }

    public void setResponseCode(String responseCode)
    {
        this.responseCode = responseCode;
    }

    public String getResponseDescription()
    {
        return this.responseDescription;
    }

    public void setResponseDescription(String responseDescription)
    {
        this.responseDescription = responseDescription;
    }

    public String getPaymentGatewayCode()
    {
        return null;
    }

    public void setPaymentGatewayCode(String code) {}

    public String getSystemTraceAuditNumber()
    {
        return null;
    }

    public String getTransmissionDateAndTime()
    {
        return null;
    }

    public String getMessageAsEdi()
    {
        return this.messageAsEdi;
    }

    public void setMessageAsEdi(String messageAsEdi)
    {
        this.messageAsEdi = messageAsEdi;
    }

    public String getRetrievalReferenceNumber()
    {
        return null;
    }

    public List<CustomerAccount> getCustomerAccounts()
    {
        return null;
    }

    public String getSecureVerificationData()
    {
        return null;
    }

    public void setSecureVerificationData(String secureVerificationData) {}

    public String getIvrChannelStatus()
    {
        return null;
    }

    public String getMobileChannelStatus()
    {
        return null;
    }

    public long getTimeOutInterval()
    {
        return 0L;
    }

    public void setTimeOutInterval(long timeOut) {}

    public String getAgentTransactionId()
    {
        return this.agentTransactionId;
    }

    public void setAgentTransactionId(String agentTransactionId)
    {
        this.agentTransactionId = agentTransactionId;
    }

    public String getCNIC()
    {
        return this.CNIC;
    }

    public void setCNIC(String CNIC)
    {
        this.CNIC = CNIC;
    }

    public String getMSISDN()
    {
        return this.MSISDN;
    }

    public void setMSISDN(String MSISDN)
    {
        this.MSISDN = MSISDN;
    }

    public String getSerialNo()
    {
        return this.serialNo;
    }

    public void setSerialNo(String serialNo)
    {
        this.serialNo = serialNo;
    }

    public String getPin()
    {
        return this.pin;
    }

    public void setPin(String pin)
    {
        this.pin = pin;
    }

    public String getCharges()
    {
        return this.charges;
    }

    public void setCharges(String charges)
    {
        this.charges = charges;
    }

    public Date getExpiryDate()
    {
        return this.expiryDate;
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAmountWithinDueDate() {
        return amountWithinDueDate;
    }

    public void setAmountWithinDueDate(String amountWithinDueDate) {
        this.amountWithinDueDate = amountWithinDueDate;
    }

    public String getAmountAfterDueDate() {
        return amountAfterDueDate;
    }

    public void setAmountAfterDueDate(String amountAfterDueDate) {
        this.amountAfterDueDate = amountAfterDueDate;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public String getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(String datePaid) {
        this.datePaid = datePaid;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getTransactionAuthID() {
        return transactionAuthID;
    }

    public void setTransactionAuthID(String transactionAuthID) {
        this.transactionAuthID = transactionAuthID;
    }

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the transactionInitialized
	 */
	public String getTransactionInitialized() {
		return transactionInitialized;
	}

	/**
	 * @param transactionInitialized the transactionInitialized to set
	 */
	public void setTransactionInitialized(String transactionInitialized) {
		this.transactionInitialized = transactionInitialized;
	}
}
