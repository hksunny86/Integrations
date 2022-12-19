package com.inov8.integration.channel.microbank.request.BillingRequest;

import com.inov8.integration.channel.microbank.request.Request;
import com.inov8.integration.exception.I8SBValidationException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import org.apache.commons.lang.StringUtils;

public class BillCategoryProductRequest extends Request {

    private String CategoryId;

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    @Override
    public void populateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) {
        super.BillingPopulateRequest(i8SBSwitchControllerRequestVO);
        this.setCategoryId(i8SBSwitchControllerRequestVO.getCollectionOfList().get("CategoryId").toString());

    }

    @Override
    public boolean validateRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {
        if(!super.BillingValidateRequest(i8SBSwitchControllerRequestVO)){
            return false;
        }
        if (StringUtils.isEmpty(this.getCategoryId())) {
            throw new I8SBValidationException("[FAILED] Category ID validation: " + this.getCategoryId());
        }
        return true;
    }

    @Override
    public void buildRequest(I8SBSwitchControllerRequestVO i8SBSwitchControllerRequestVO) throws I8SBValidationException {

    }
}
