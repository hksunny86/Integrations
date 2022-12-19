package com.inov8.microbank.account.controller;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.account.model.BlacklistedCnicsViewModel;
import com.inov8.microbank.account.service.AccountControlManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class BlacklistedCNICController extends BaseFormSearchController {
    private AccountControlManager accountControlManager;

    public BlacklistedCNICController() {
        setCommandName("blacklistedCnicsViewModel");
        setCommandClass(BlacklistedCnicsViewModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, Object> referenceDataMap = new HashMap<>(0);

        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse httpServletResponse, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        BlacklistedCnicsViewModel blacklistedCNICViewModel = (BlacklistedCnicsViewModel) model;
        searchBaseWrapper.setBasePersistableModel(blacklistedCNICViewModel);

        DateRangeHolderModel dateRangeHolderModel1 = new DateRangeHolderModel("createdOn", blacklistedCNICViewModel.getCreatedOnStart(), blacklistedCNICViewModel.getCreatedOnEnd());
        DateRangeHolderModel dateRangeHolderModel2 = new DateRangeHolderModel("updatedOn", blacklistedCNICViewModel.getUpdatedOnStart(), blacklistedCNICViewModel.getUpdatedOnEnd());

        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>(2);
        dateRangeHolderModels.add(dateRangeHolderModel1);
        dateRangeHolderModels.add(dateRangeHolderModel2);
        searchBaseWrapper.setDateRangeHolderModelList(dateRangeHolderModels);

        if (sortingOrderMap.isEmpty()) {
            sortingOrderMap.put("cnicNo", SortingOrder.ASC);
        }

        searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

        searchBaseWrapper = this.accountControlManager.searchBlacklistedCNICViewModel(searchBaseWrapper);
        List<BlacklistedCnicsViewModel> resultList = searchBaseWrapper.getCustomList().getResultsetList();

        return new ModelAndView(getFormView(), "blacklistedCNICViewModelList", resultList);
    }

    public void setAccountControlManager(AccountControlManager accountControlManager) {
        this.accountControlManager = accountControlManager;
    }
}
