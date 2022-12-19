package com.inov8.integration.channel.microbank.request.BillingRequest;

import com.inov8.integration.channel.microbank.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

public class BillinquiryRequest extends Request {


    private String CompanyName;
    private String ProductId;
    private String ConsumerNumber;
    private String CurrencyCode;


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

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        super.BillingPopulateRequest(i8SBSwitchControllerRequestVO);

        if (hashMap.get("CompanyName") != null && StringUtils.isNotEmpty(hashMap.get("CompanyName").toString())) {
            this.setCompanyName(hashMap.get("CompanyName").toString());
        }
        if (hashMap.get("ProductId") != null && StringUtils.isNotEmpty(hashMap.get("ProductId").toString())) {
            this.setProductId(hashMap.get("ProductId").toString());
        }
        if (hashMap.get("ConsumerNumber") != null && StringUtils.isNotEmpty(hashMap.get("ConsumerNumber").toString())) {
            this.setConsumerNumber(hashMap.get("ConsumerNumber").toString());
        }
        if (hashMap.get("CurrencyCode") != null && StringUtils.isNotEmpty(hashMap.get("CurrencyCode").toString())) {
            this.setCurrencyCode(hashMap.get("CurrencyCode").toString());
        }
        if (hashMap.get("ChannelId") != null && StringUtils.isNotEmpty(hashMap.get("ChannelId").toString())) {
            this.setChannelId(hashMap.get("ChannelId").toString());
        }
    }

    @Override
    public boolean validateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {
        if(!super.BillingValidateRequest(i8SBSwitchControllerRequestVO)){
            return false;
        }
        if (StringUtils.isEmpty(this.getCompanyName())) {
            throw new I8SBValidationException("[VALIDATION FAILED] Company Name  is a mandatory parameter: ");
        }
        if (StringUtils.isEmpty(this.getProductId())) {
            throw new I8SBValidationException("[VALIDATION FAILED] Product ID  is a mandatory parameter: ");
        }
        if (StringUtils.isEmpty(this.getConsumerNumber())) {
            throw new I8SBValidationException("[VALIDATION FAILED] Consumer Number  is a mandatory parameter: ");
        }
        if (StringUtils.isEmpty(this.getCurrencyCode())) {
            throw new I8SBValidationException("[VALIDATION FAILED] Currency Code  is a mandatory parameter: ");
        }
        StringBuffer stringText = new StringBuffer(
                        "i8billing"+
                        this.getClientId() +
                        this.getTerminalId() +
                        this.getChannelId() +
                        this.getRequestDateTime() +
                        this.getRequestType() +
                        this.getUserName() +
                        this.getPassword() +
                        this.getCompanyName() +
                        this.getProductId() +
                        this.getConsumerNumber() +
                        this.getCurrencyCode() +
                        this.getRRN()
        );
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(stringText.toString());
        if (!sha256hex.equalsIgnoreCase(this.getCheckSum()))
        {
            throw new I8SBValidationException("[FAILED] CheckSum validation Failed!");
        }

        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
}
