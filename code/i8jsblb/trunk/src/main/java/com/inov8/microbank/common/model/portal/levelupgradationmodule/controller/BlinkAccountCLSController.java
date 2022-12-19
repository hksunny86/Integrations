package com.inov8.microbank.common.model.portal.levelupgradationmodule.controller;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.common.model.ClsPendingBlinkCustomerModel;
import com.inov8.microbank.debitcard.model.DebitCardRequestsViewModel;
import com.inov8.microbank.debitcard.service.DebitCardManager;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.ClsPendingBlinkCustomerManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BlinkAccountCLSController extends BaseFormSearchController {

    private ClsPendingBlinkCustomerManager clsPendingBlinkCustomerManager;
    private ReferenceDataManager referenceDataManager;

    public BlinkAccountCLSController() {
        setCommandName("clsPendingBlinkCustomerModel");
        setCommandClass(ClsPendingBlinkCustomerModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        ClsPendingBlinkCustomerModel clsPendingBlinkCustomerModel = (ClsPendingBlinkCustomerModel) o;
        searchBaseWrapper.setBasePersistableModel(clsPendingBlinkCustomerModel);
        searchBaseWrapper.setSortingOrderMap( linkedHashMap );
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", clsPendingBlinkCustomerModel.getCreatedOnStartDate(),
                clsPendingBlinkCustomerModel.getCreatedOnEndDate());
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

        List<ClsPendingBlinkCustomerModel> list = clsPendingBlinkCustomerManager.searchBlinkAccountCLSData(searchBaseWrapper);
        return new ModelAndView( getSuccessView(), "clsPendingBlinkCustomerList",list);
    }

    public void setClsPendingBlinkCustomerManager(ClsPendingBlinkCustomerManager clsPendingBlinkCustomerManager) {
        this.clsPendingBlinkCustomerManager = clsPendingBlinkCustomerManager;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }
}
