package com.inov8.integration.channel.offlineBiller.resquest;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

public class BillPaymentRequest extends Request {

    private String referenceNo;
    private String productCode;
    private String microbankTransactionCode;

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        this.setProductCode(i8SBSwitchControllerRequestVO.getProductCode());
        this.setReferenceNo(i8SBSwitchControllerRequestVO.getConsumerNumber());
        this.setMicrobankTransactionCode(i8SBSwitchControllerRequestVO.getTransactionCode());
    }

    @Override
    public boolean validateRequest() throws I8SBValidationException {
        return true;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getMicrobankTransactionCode() {
        return microbankTransactionCode;
    }

    public void setMicrobankTransactionCode(String microbankTransactionCode) {
        this.microbankTransactionCode = microbankTransactionCode;
    }
}
