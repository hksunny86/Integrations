package com.inov8.microbank.webapp.action.portal.reports.blbreports;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.portal.blbreports.DebitCardChargesViewModel;
import com.inov8.microbank.common.model.portal.blbreports.HandlerActivityReportModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.facade.CommonFacade;
import com.inov8.microbank.server.service.blbreports.BLBReportsManager;
import com.inov8.ola.server.facade.AccountFacade;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HandlerActivityReportController extends BaseFormSearchController {
    private BLBReportsManager blbReportsManager;
    private AccountFacade accountFacade;

    public HandlerActivityReportController()
    {
        super.setCommandName("handlerActivityReportModel");
        super.setCommandClass(HandlerActivityReportModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String,Object> refDataMap = new HashMap<String, Object>(2);
        SearchBaseWrapper searchBaseWrapper=accountFacade.getAllAgentAccountTypes();
        if(searchBaseWrapper.getCustomList()!=null) {
            List<OlaCustomerAccountTypeModel> accountTypeModelList	=	searchBaseWrapper.getCustomList().getResultsetList();
            refDataMap.put("accountTypeList", accountTypeModelList);
        }
        return refDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        HandlerActivityReportModel handlerActivityReportModel = (HandlerActivityReportModel) o;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(handlerActivityReportModel);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "transactionSummaryDate",
                handlerActivityReportModel.getStartDate(), handlerActivityReportModel.getEndDate() );
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

        searchBaseWrapper = blbReportsManager.searchHandlerActivityReport( searchBaseWrapper );

        List<HandlerActivityReportModel> list = null;
        if(searchBaseWrapper.getCustomList() != null) {
            list = searchBaseWrapper.getCustomList().getResultsetList();
        }

        String successView = StringUtil.trimExtension( httpServletRequest.getServletPath() );
        return new ModelAndView( successView, "handlerActivityReportModelList", list );
    }

    public void setBlbReportsManager(BLBReportsManager blbReportsManager) {
        this.blbReportsManager = blbReportsManager;
    }

    public void setAccountFacade(AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }
}
