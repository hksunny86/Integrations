package com.inov8.integration.channel.microbank.response.BillingResponse;

import com.inov8.integration.channel.microbank.response.Response;
import com.inov8.integration.exception.I8SBRunTimeException;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FailedBillSummaryResponse extends Response {

    List<HashMap<String,String>> PaidBillSummaryList;

    public List<HashMap<String, String>> getPaidBillSummaryList() {
        return PaidBillSummaryList;
    }

    public void setPaidBillSummaryList(List<HashMap<String, String>> paidBillSummaryList) {
        PaidBillSummaryList = paidBillSummaryList;
    }

    @Override
    public I8SBSwitchControllerResponseVO populateI8SBSwitchControllerResponseVO() throws I8SBRunTimeException {
        LinkedHashMap<String, Object> result = super.DobilloPopulate();
        result.put("PaidBillSummaryList",this.getPaidBillSummaryList());
        I8SBSwitchControllerResponseVO i8SBSwitchControllerResponseVO = new I8SBSwitchControllerResponseVO();
        i8SBSwitchControllerResponseVO.setCollectionOfList(result);
        return i8SBSwitchControllerResponseVO;
    }
}
