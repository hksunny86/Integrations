package com.inov8.microbank.account.controller;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.account.model.ExpiredNicViewModel;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.common.model.AccountStateModel;
import com.inov8.microbank.common.model.CustomerAccountTypeModel;
import com.inov8.microbank.common.model.RegistrationStateModel;
import com.inov8.microbank.common.model.SegmentModel;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExpiredNicSearchController extends BaseFormSearchController {

    private AccountControlManager accountControlManager;
    private ReferenceDataManager referenceDataManager;

    public ExpiredNicSearchController() {
        setCommandClass(ExpiredNicViewModel.class);
        setCommandName("expiredNicViewModel");
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {

        Map<String, Object> referenceDataMap = new HashMap<>(2);

        SegmentModel segmentModel = new SegmentModel();
        List<SegmentModel> segmentModelList = null;
        ReferenceDataWrapper segmentDataWrapper = new ReferenceDataWrapperImpl(segmentModel, "name", SortingOrder.ASC);
        segmentModelList = (List<SegmentModel>) referenceDataManager.getReferenceData(segmentDataWrapper).getReferenceDataList();
        referenceDataMap.put("segmentModelList", segmentModelList);

        OlaCustomerAccountTypeModel olaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
        List<OlaCustomerAccountTypeModel> olaCustomerAccountTypeModelList = null;
        ReferenceDataWrapper olaCustomerAccountTypeDataWrapper = new ReferenceDataWrapperImpl(olaCustomerAccountTypeModel, "name", SortingOrder.ASC);
        olaCustomerAccountTypeModelList = (List<OlaCustomerAccountTypeModel>) referenceDataManager.getReferenceData(olaCustomerAccountTypeDataWrapper).getReferenceDataList();
        referenceDataMap.put("olaCustomerAccountTypeModelList", olaCustomerAccountTypeModelList);


        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        ExpiredNicViewModel model = (ExpiredNicViewModel) o;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(model);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("cNicExpiryDate", model.getcNicExpiryDate(), model.getcNicEpiryDateEnd());
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        searchBaseWrapper = accountControlManager.loadExpiredNics(searchBaseWrapper);
        List<ExpiredNicViewModel> list = null;
        if (searchBaseWrapper.getCustomList() != null && !searchBaseWrapper.getCustomList().getResultsetList().isEmpty()) {
            list = searchBaseWrapper.getCustomList().getResultsetList();
            searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(list.size());
        } else {
            searchBaseWrapper.getPagingHelperModel().setTotalRecordsCount(0);
        }
        return new ModelAndView("p_expiredNicReport", "expiredNicDataList", list);
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setAccountControlManager(AccountControlManager accountControlManager) {
        this.accountControlManager = accountControlManager;
    }
}
