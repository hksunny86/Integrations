package com.inov8.integration.channel.offlineBiller.resquest;

import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;

public class BillInquiryRequest extends Request {

    private String referenceNo;
    private String productCode;

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {

        this.setReferenceNo(i8SBSwitchControllerRequestVO.getRefrenceNumber());
        this.setProductCode(i8SBSwitchControllerRequestVO.getCompanyCode());

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
}
