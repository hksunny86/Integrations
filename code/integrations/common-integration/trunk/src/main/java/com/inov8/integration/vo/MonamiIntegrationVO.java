package com.inov8.integration.vo;

import com.inov8.integration.middleware.novatti.*;
import com.inov8.microbank.server.service.switchmodule.IntegrationMessageVO;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonamiIntegrationVO implements Serializable, IntegrationMessageVO
{
    private static final long serialVersionUID = 7881509016351832248L;
    private Map<String, String> extras = new HashMap<String, String>();
    private String productCode;
    private String agentTransactionId;
    private DateTime transactionTimeStamp;
    private String terminalID;
    private Integer quantity;
    private Double value;
    private String msidn;
    private String apiKey;
    private ProductItems productItems;
    private Transactions transactions;
    private List<VoucherKey> voucherKeys = new ArrayList<VoucherKey>();
    private VoucherDetails vouchers;
    private Wallets wallets;
    private String originalTransactionId;
    private String voucherSerial;
    private String productEan;
    private String reasonCode;
    private String comments;
    private String refundUserID;
    private String responseCode;
    private String responseDescription;
    private String originalAgentTransactionId;
    private DateTime originalTransactionTimeStamp;
    private Integer requestedQuantity;
    private Integer reversedCount;

    public Map<String, String> getExtras()
    {
        return this.extras;
    }

    public void setExtras(Map<String, String> extras)
    {
        this.extras = extras;
    }

    public String getProductCode()
    {
        return this.productCode;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    public Integer getQuantity()
    {
        return this.quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public Double getValue()
    {
        return this.value;
    }

    public void setValue(Double value)
    {
        this.value = value;
    }

    public String getMsidn()
    {
        return this.msidn;
    }

    public void setMsidn(String msidn)
    {
        this.msidn = msidn;
    }

    public String getApiKey()
    {
        return this.apiKey;
    }

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
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

    public ProductItems getProductItems()
    {
        return this.productItems;
    }

    public void setProductItems(ProductItems productItems)
    {
        this.productItems = productItems;
    }

    public Transactions getTransactions()
    {
        return this.transactions;
    }

    public void setTransactions(Transactions transactions)
    {
        this.transactions = transactions;
    }

    public String getTerminalID()
    {
        return this.terminalID;
    }

    public void setTerminalID(String terminalID)
    {
        this.terminalID = terminalID;
    }

    public List<VoucherKey> getVoucherKeys()
    {
        return this.voucherKeys;
    }

    public void setVoucherKeys(List<VoucherKey> voucherKeys)
    {
        this.voucherKeys = voucherKeys;
    }

    public String getOriginalTransactionId()
    {
        return this.originalTransactionId;
    }

    public void setOriginalTransactionId(String originalTransactionId)
    {
        this.originalTransactionId = originalTransactionId;
    }

    public String getVoucherSerial()
    {
        return this.voucherSerial;
    }

    public void setVoucherSerial(String voucherSerial)
    {
        this.voucherSerial = voucherSerial;
    }

    public String getProductEan()
    {
        return this.productEan;
    }

    public void setProductEan(String productEan)
    {
        this.productEan = productEan;
    }

    public String getReasonCode()
    {
        return this.reasonCode;
    }

    public void setReasonCode(String reasonCode)
    {
        this.reasonCode = reasonCode;
    }

    public String getComments()
    {
        return this.comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    public String getRefundUserID()
    {
        return this.refundUserID;
    }

    public void setRefundUserID(String refundUserID)
    {
        this.refundUserID = refundUserID;
    }

    public VoucherDetails getVouchers()
    {
        return this.vouchers;
    }

    public void setVouchers(VoucherDetails vouchers)
    {
        this.vouchers = vouchers;
    }

    public Wallets getWallets()
    {
        return this.wallets;
    }

    public void setWallets(Wallets wallets)
    {
        this.wallets = wallets;
    }

    public String getOriginalAgentTransactionId()
    {
        return this.originalAgentTransactionId;
    }

    public void setOriginalAgentTransactionId(String originalAgentTransactionId)
    {
        this.originalAgentTransactionId = originalAgentTransactionId;
    }

    public DateTime getOriginalTransactionTimeStamp()
    {
        return this.originalTransactionTimeStamp;
    }

    public void setOriginalTransactionTimeStamp(DateTime originalTransactionTimeStamp)
    {
        this.originalTransactionTimeStamp = originalTransactionTimeStamp;
    }

    public Integer getRequestedQuantity()
    {
        return this.requestedQuantity;
    }

    public void setRequestedQuantity(Integer requestedQuantity)
    {
        this.requestedQuantity = requestedQuantity;
    }

    public Integer getReversedCount()
    {
        return this.reversedCount;
    }

    public void setReversedCount(Integer reversedCount)
    {
        this.reversedCount = reversedCount;
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
        return null;
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

    public void setMicrobankTransactionCode(String transactionCode) {}

    public String getAgentTransactionId()
    {
        return this.agentTransactionId;
    }

    public void setAgentTransactionId(String agentTransactionId)
    {
        this.agentTransactionId = agentTransactionId;
    }

    public DateTime getTransactionTimeStamp()
    {
        return this.transactionTimeStamp;
    }

    public void setTransactionTimeStamp(DateTime transactionTimeStamp)
    {
        this.transactionTimeStamp = transactionTimeStamp;
    }

    @Override
    public String getMicrobankTransactionCode() {
        return null;
    }
}
