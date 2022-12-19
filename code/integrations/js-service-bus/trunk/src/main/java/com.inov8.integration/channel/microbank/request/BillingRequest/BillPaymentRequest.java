package com.inov8.integration.channel.microbank.request.BillingRequest;

import com.inov8.integration.channel.microbank.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

public class BillPaymentRequest extends Request {

    private String CompanyName;
    private String ConsumerNo;
    private String CurrencyCode;
    private String BillAmount;
    private String Reserved1;
    private String Reserved2;
    private String Reserved3;
    private String ProductId;

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getConsumerNo() {
        return ConsumerNo;
    }

    public void setConsumerNo(String consumerNo) {
        ConsumerNo = consumerNo;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(String billAmount) {
        BillAmount = billAmount;
    }

    public String getReserved1() {
        return Reserved1;
    }

    public void setReserved1(String reserved1) {
        Reserved1 = reserved1;
    }

    public String getReserved2() {
        return Reserved2;
    }

    public void setReserved2(String reserved2) {
        Reserved2 = reserved2;
    }

    public String getReserved3() {
        return Reserved3;
    }

    public void setReserved3(String reserved3) {
        Reserved3 = reserved3;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        super.BillingPopulateRequest(i8SBSwitchControllerRequestVO);

        if (hashMap.get("CompanyName") != null && StringUtils.isNotEmpty(hashMap.get("CompanyName").toString())) {
            this.setCompanyName(hashMap.get("CompanyName").toString());
        }
        if (hashMap.get("ConsumerNumber") != null && StringUtils.isNotEmpty(hashMap.get("ConsumerNumber").toString())) {
            this.setConsumerNo(hashMap.get("ConsumerNumber").toString());
        }
        if (hashMap.get("CurrencyCode") != null && StringUtils.isNotEmpty(hashMap.get("CurrencyCode").toString())) {
            this.setCurrencyCode(hashMap.get("CurrencyCode").toString());
        }
        if (hashMap.get("BillAmount") != null && StringUtils.isNotEmpty(hashMap.get("BillAmount").toString())) {
            this.setBillAmount(hashMap.get("BillAmount").toString());
        }
        if (hashMap.get("Reserved1") != null && StringUtils.isNotEmpty(hashMap.get("Reserved1").toString())) {
            this.setReserved1(hashMap.get("Reserved1").toString());
        }
        if (hashMap.get("Reserved2") != null && StringUtils.isNotEmpty(hashMap.get("Reserved2").toString())) {
            this.setReserved2(hashMap.get("Reserved2").toString());
        }
        if (hashMap.get("Reserved3") != null && StringUtils.isNotEmpty(hashMap.get("Reserved3").toString())) {
            this.setReserved3(hashMap.get("Reserved3").toString());
        }
        if (hashMap.get("ChannelId") != null && StringUtils.isNotEmpty(hashMap.get("ChannelId").toString())) {
            this.setChannelId(hashMap.get("ChannelId").toString());
        }
        if (hashMap.get("ProductId") != null && StringUtils.isNotEmpty(hashMap.get("ProductId").toString())) {
            this.setProductId(hashMap.get("ProductId").toString());
        }
    }

    @Override
    public boolean validateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {
        if(!super.BillingValidateRequest(i8SBSwitchControllerRequestVO)){
            return false;
        }
        if (StringUtils.isEmpty(this.getCompanyName())) {
            throw new I8SBValidationException("[VALIDATION FAILED] Company Name is a mandatory parameter: ");
        }
        if (StringUtils.isEmpty(this.getConsumerNo())) {
            throw new I8SBValidationException("[VALIDATION FAILED] Consumer No is a mandatory parameter: ");
        }
        if (StringUtils.isEmpty(this.getCurrencyCode())) {
            throw new I8SBValidationException("[VALIDATION FAILED] Currency Code is a mandatory parameter: ");
        }
        if (StringUtils.isEmpty(this.getBillAmount())) {
            throw new I8SBValidationException("[VALIDATION FAILED] Bill Amount is a mandatory parameter: ");
        }
        if (StringUtils.isEmpty(this.getProductId())) {
            throw new I8SBValidationException("[VALIDATION FAILED] Product ID  is a mandatory parameter: ");
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
                        this.getConsumerNo() +
                        this.getCurrencyCode() +
                        this.getBillAmount() +
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
