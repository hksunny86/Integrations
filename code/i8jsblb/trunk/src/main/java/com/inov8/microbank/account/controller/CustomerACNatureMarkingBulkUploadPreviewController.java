package com.inov8.microbank.account.controller;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.account.vo.BlacklistMarkingBulkUploadVo;
import com.inov8.microbank.account.vo.CustomerACNatureMarkingUploadVo;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

public class CustomerACNatureMarkingBulkUploadPreviewController extends BaseSearchController {

    public CustomerACNatureMarkingBulkUploadPreviewController() {
        setFilterSearchCommandClass(CustomerACNatureMarkingUploadVo.class);
    }

    @Override
    protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object o, HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        List<CustomerACNatureMarkingUploadVo> customerACNatureMarkingBulkUploadVoList = (List<CustomerACNatureMarkingUploadVo>) httpServletRequest.getSession().getAttribute("customerACNatureMarkingBulkUploadVoList");
        if (CollectionUtils.isEmpty(customerACNatureMarkingBulkUploadVoList)) {
            pagingHelperModel.setTotalRecordsCount(0);
        } else {
            pagingHelperModel.setTotalRecordsCount(customerACNatureMarkingBulkUploadVoList.size());
        }

        return new ModelAndView(getSearchView());
    }
}
