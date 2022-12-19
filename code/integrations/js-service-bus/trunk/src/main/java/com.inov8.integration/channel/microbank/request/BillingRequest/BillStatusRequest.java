package com.inov8.integration.channel.microbank.request.BillingRequest;

import com.inov8.integration.channel.microbank.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

public class BillStatusRequest extends Request {

    private String CompanyName;
    private String ConsumerNumber;
    private String TransactionId;

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

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

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        super.BillingPopulateRequest(i8SBSwitchControllerRequestVO);
        if (hashMap.get("ChannelId") != null && StringUtils.isNotEmpty(hashMap.get("ChannelId").toString())) {
            this.setChannelId(hashMap.get("ChannelId").toString());
        }
        if (hashMap.get("CompanyName") != null && StringUtils.isNotEmpty(hashMap.get("CompanyName").toString())) {
            this.setCompanyName(hashMap.get("CompanyName").toString());
        }
        if (hashMap.get("ConsumerNumber") != null && StringUtils.isNotEmpty(hashMap.get("ConsumerNumber").toString())) {
            this.setConsumerNumber(hashMap.get("ConsumerNumber").toString());
        }
        if (hashMap.get("TransactionIdInov8") != null && StringUtils.isNotEmpty(hashMap.get("TransactionIdInov8").toString())) {
            this.setTransactionId(hashMap.get("TransactionIdInov8").toString());
        }
        this.setCompanyName(i8SBSwitchControllerRequestVO.getCollectionOfList().get("CompanyName").toString());
        this.setConsumerNumber(i8SBSwitchControllerRequestVO.getCollectionOfList().get("ConsumerNumber").toString());

    }

    @Override
    public boolean validateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {
        if(!super.BillingValidateRequest(i8SBSwitchControllerRequestVO)){
            return false;
        }
        if (StringUtils.isEmpty(this.getCompanyName())) {
            throw new I8SBValidationException("[FAILED] Company Name validation: " + this.getCompanyName());
        }
        if (StringUtils.isEmpty(this.getConsumerNumber())) {
            throw new I8SBValidationException("[FAILED] Consumer No validation: " + this.getConsumerNumber());
        }
        if (StringUtils.isEmpty(this.getTransactionId())) {
            throw new I8SBValidationException("[FAILED] Transaction Id validation: " + this.getConsumerNumber());
        }
        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
}
