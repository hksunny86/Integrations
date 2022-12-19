package com.inov8.microbank.account.controller;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.account.vo.BlacklistMarkingBulkUploadVo;
import com.inov8.microbank.webapp.action.bankmodule.BankSearchController;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Malik on 8/24/2016.
 */
public class BlacklistMarkingBulkUploadPreviewController extends BaseSearchController
{

    public BlacklistMarkingBulkUploadPreviewController()
    {
        setFilterSearchCommandClass(BlacklistMarkingBulkUploadVo.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest req, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
        List<BlacklistMarkingBulkUploadVo> blacklistMarkingBulkUploadVoList = (List<BlacklistMarkingBulkUploadVo>) req.getSession().getAttribute("blacklistMarkingBulkUploadVoList");
        if(CollectionUtils.isEmpty(blacklistMarkingBulkUploadVoList))
        {
            pagingHelperModel.setTotalRecordsCount(0);
        }
        else
        {
            pagingHelperModel.setTotalRecordsCount(blacklistMarkingBulkUploadVoList.size());
        }

        return new ModelAndView( getSearchView() );
    }
}
