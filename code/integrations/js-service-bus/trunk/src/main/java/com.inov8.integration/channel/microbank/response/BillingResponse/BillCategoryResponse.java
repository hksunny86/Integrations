package com.inov8.integration.channel.microbank.response.BillingResponse;

import com.inov8.integration.channel.microbank.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.util.LinkedHashMap;

public class BillCategoryResponse extends Response {

    private String BillCategory;

    public String getBillCategory() {
        return BillCategory;
    }

    public void setBillCategory(String billCategory) {
        BillCategory = billCategory;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        LinkedHashMap<String, Object> result = super.DobilloPopulate();
        result.put("BillCategory",this.getBillCategory());
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setCollectionOfList(result);
        return i8SBSwitchControllerResponseVO;
    }
}
