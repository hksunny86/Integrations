package com.inov8.microbank.webapp.action.portal.reports.blbreports;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.portal.blbreports.BdeKpiReportViewModel;
import com.inov8.microbank.common.model.portal.blbreports.ExtendedBdeKpiReportModel;
import com.inov8.microbank.common.model.portal.salesmodule.ExtendedSalesTeamComissionViewModel;
import com.inov8.microbank.common.model.portal.salesmodule.SalesTeamComissionViewModel;
import com.inov8.microbank.common.util.GenericComparator;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.service.blbreports.BLBReportsManager;
import com.inov8.ola.server.facade.AccountFacade;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BdeKpiReportController extends BaseFormSearchController {

    private ReferenceDataManager referenceDataManager;
    private AccountFacade accountFacade;
    private BLBReportsManager blbReportsManager;

    public BdeKpiReportController()
    {
        super.setCommandName("bdeKpiReportViewModel");
        super.setCommandClass(BdeKpiReportViewModel.class);
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
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                                    PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {

        BdeKpiReportViewModel bdeKpiReportViewModel = (BdeKpiReportViewModel) o;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(bdeKpiReportViewModel);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        //sorting order
        if(linkedHashMap.isEmpty())
        {
            linkedHashMap.put("accountOpeningDate", SortingOrder.DESC);
        }
        searchBaseWrapper.setSortingOrderMap(linkedHashMap);

        List<BdeKpiReportViewModel> list = blbReportsManager.loadBdeKpiReportViewModel(bdeKpiReportViewModel);

        ModelAndView modelAndView = new ModelAndView( getSuccessView(), "bdeKpiReportViewModelList", list );
        pagingHelperModel.setTotalRecordsCount(list.size());
        return modelAndView;
    }

    public void setAccountFacade(AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setBlbReportsManager(BLBReportsManager blbReportsManager) {
        this.blbReportsManager = blbReportsManager;
    }
}
