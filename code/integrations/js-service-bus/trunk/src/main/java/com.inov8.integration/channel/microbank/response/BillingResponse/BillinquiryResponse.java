package com.inov8.integration.channel.microbank.response.BillingResponse;

import com.inov8.integration.channel.microbank.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.util.LinkedHashMap;

public class BillinquiryResponse extends Response {

    private String CompanyName;
    private String ProductId;
    private String ConsumerName;
    private String ConsumerNumber;
    private String CurrencyCode;
    private String CurrencyExchangeRate;
    private String BillAmount;
    private String TransactionfeeCharges;
    private String TotalAmount;
    private String BillStatus;
    private String DueDate;
    private String ChargesAfterBillDueDate;
    private String LateBillAmount;

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getConsumerName() {
        return ConsumerName;
    }

    public void setConsumerName(String consumerName) {
        ConsumerName = consumerName;
    }

    public String getConsumerNumber() {
        return ConsumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        ConsumerNumber = consumerNumber;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getCurrencyExchangeRate() {
        return CurrencyExchangeRate;
    }

    public void setCurrencyExchangeRate(String currencyExchangeRate) {
        CurrencyExchangeRate = currencyExchangeRate;
    }

    public String getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(String billAmount) {
        BillAmount = billAmount;
    }

    public String getTransactionfeeCharges() {
        return TransactionfeeCharges;
    }

    public void setTransactionfeeCharges(String transactionfeeCharges) {
        TransactionfeeCharges = transactionfeeCharges;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getBillStatus() {
        return BillStatus;
    }

    public void setBillStatus(String billStatus) {
        BillStatus = billStatus;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getChargesAfterBillDueDate() {
        return ChargesAfterBillDueDate;
    }

    public void setChargesAfterBillDueDate(String chargesAfterBillDueDate) {
        ChargesAfterBillDueDate = chargesAfterBillDueDate;
    }

    public String getLateBillAmount() {
        return LateBillAmount;
    }

    public void setLateBillAmount(String lateBillAmount) {
        LateBillAmount = lateBillAmount;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        LinkedHashMap<String, Object> result = super.DobilloPopulate();
        result.put("CompanyName",this.getCompanyName());
        result.put("ProductId",this.getProductId());
        result.put("ConsumerName",this.getConsumerName());
        result.put("ConsumerNumber",this.getConsumerNumber());
        result.put("BillAmount",this.getBillAmount());
        result.put("BillStatus",this.getBillStatus());
        result.put("ChargesAfterBillDueDate",this.getChargesAfterBillDueDate());
        result.put("CurrencyCode",this.getCurrencyCode());
        result.put("CurrencyExchangeRate",this.getCurrencyExchangeRate());
        result.put("DueDate",this.getDueDate());
        result.put("LateBillAmount",this.getLateBillAmount());
        result.put("TotalAmount",this.getTotalAmount());
        result.put("TransactionfeeCharges",this.getTransactionfeeCharges());
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setCollectionOfList(result);
        return i8SBSwitchControllerResponseVO;
    }
}
