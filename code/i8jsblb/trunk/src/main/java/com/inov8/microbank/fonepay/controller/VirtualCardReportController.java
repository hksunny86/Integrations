package com.inov8.microbank.fonepay.controller;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.fonepay.model.VirtualCardReportModel;
import com.inov8.microbank.fonepay.service.FonePayManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Attique on 7/25/2017.
 */
public class VirtualCardReportController extends BaseFormSearchController {

    private FonePayManager fonePayManager;

    public VirtualCardReportController()
    {
        setCommandName( "virtualCardReportModel" );
        setCommandClass( VirtualCardReportModel.class );
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
        VirtualCardReportModel virtualCardReportModel = (VirtualCardReportModel) model;
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", virtualCardReportModel.getStartDate(), virtualCardReportModel.getEndDate() );
        wrapper.setDateRangeHolderModel( dateRangeHolderModel );

        wrapper.setBasePersistableModel( (VirtualCardReportModel) virtualCardReportModel );
        wrapper.setPagingHelperModel( pagingHelperModel );
        CustomList<VirtualCardReportModel> list = fonePayManager.searchCards( wrapper );
        List<VirtualCardReportModel> virtualCardReportModelList=list.getResultsetList();
        return new ModelAndView( getFormView(), "virtualCardReportModelList", virtualCardReportModelList );
    }

    public void setFonePayManager(FonePayManager fonePayManager) {
        this.fonePayManager = fonePayManager;
    }
}
