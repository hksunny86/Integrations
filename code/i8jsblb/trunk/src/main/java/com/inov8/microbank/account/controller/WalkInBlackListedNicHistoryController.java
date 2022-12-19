package com.inov8.microbank.account.controller;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.account.model.BlackListedNicHistoryViewModel;
import com.inov8.microbank.account.model.WalkInBlackListedNicViewModel;
import com.inov8.microbank.account.service.AccountControlManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WalkInBlackListedNicHistoryController extends BaseFormSearchController {

    private AccountControlManager accountControlManager;
    private ReferenceDataManager referenceDataManager;

    public WalkInBlackListedNicHistoryController()
    {
        setCommandClass(WalkInBlackListedNicViewModel.class);
        setCommandName("walkInBlackListedNicViewModel");
    }
    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        WalkInBlackListedNicViewModel model = (WalkInBlackListedNicViewModel) o;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(model);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "actionDate", model.getActionDate(), model.getActionEndDate());
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );
        searchBaseWrapper = accountControlManager.loadWalkInBlackListedNics(searchBaseWrapper);
        List<WalkInBlackListedNicViewModel> list = null;
        if(searchBaseWrapper.getCustomList() != null && !searchBaseWrapper.getCustomList().getResultsetList().isEmpty())
        {
            list = searchBaseWrapper.getCustomList().getResultsetList();
            searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(list.size());
        }
        else
        {
            searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(0);
        }
        return new ModelAndView("p_walkInBlackListedNicReport","walkInBlackListedNicList",list);
    }

    public void setAccountControlManager(AccountControlManager accountControlManager) {
        this.accountControlManager = accountControlManager;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }
}
