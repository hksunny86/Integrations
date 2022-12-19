package com.inov8.integration.channel.microbank.response.BillingResponse;

import com.inov8.integration.channel.microbank.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.util.LinkedHashMap;

public class BillPaymentResponse extends Response {

    private String CompanyName;
    private String ConsumerNumber;
    private String BillStatus;
    private String BillPaymentAmount;
    private String CurrencyCode;
    private String CurrencyExchangeRate;

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getConsumerNumber() {
        return ConsumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        ConsumerNumber = consumerNumber;
    }

    public String getBillStatus() {
        return BillStatus;
    }

    public void setBillStatus(String billStatus) {
        BillStatus = billStatus;
    }

    public String getBillPaymentAmount() {
        return BillPaymentAmount;
    }

    public void setBillPaymentAmount(String billPaymentAmount) {
        BillPaymentAmount = billPaymentAmount;
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

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        LinkedHashMap<String, Object> result = super.DobilloPopulate();
        result.put("CompanyName",this.getCompanyName());
        result.put("ConsumerNumber",this.getConsumerNumber());
        result.put("BillStatus",this.getBillStatus());
        result.put("CurrencyCode",this.getCurrencyCode());
        result.put("CurrencyExchangeRate",this.getCurrencyExchangeRate());
        result.put("BillPaymentAmount",this.getBillPaymentAmount());
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setCollectionOfList(result);
        return i8SBSwitchControllerResponseVO;
    }
}
