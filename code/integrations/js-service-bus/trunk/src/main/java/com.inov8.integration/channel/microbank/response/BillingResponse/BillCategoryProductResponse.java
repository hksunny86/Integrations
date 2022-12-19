package com.inov8.integration.channel.microbank.response.BillingResponse;

import com.inov8.integration.channel.microbank.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class BillCategoryProductResponse extends Response {

    private List<HashMap<String,String>> CategoryProducts;

    public List<HashMap<String,String>> getCategoryProducts() {
        return CategoryProducts;
    }

    public void setCategoryProducts(List<HashMap<String,String>> categoryProducts) {
        CategoryProducts = categoryProducts;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {

        LinkedHashMap<String, Object> result = super.DobilloPopulate();
        result.put("CategoryProducts",this.getCategoryProducts());
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setCollectionOfList(result);
        return i8SBSwitchControllerResponseVO;
    }
}
