package com.inov8.microbank.webapp.action.portal.reports.blbreports;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.portal.blbreports.AgentParentChildReportModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.service.blbreports.BLBReportsManager;
import com.inov8.ola.server.facade.AccountFacade;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AgentParentChildReportController extends BaseFormSearchController {
    private BLBReportsManager blbReportsManager;
    private AccountFacade accountFacade;

    public AgentParentChildReportController()
    {
        super.setCommandName("agentParentChildReportModel");
        super.setCommandClass(AgentParentChildReportModel.class);
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
        AgentParentChildReportModel parentChildReportModel = (AgentParentChildReportModel) o;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(parentChildReportModel);
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "accountOpeningDate",
                parentChildReportModel.getStartDate(), parentChildReportModel.getEndDate() );
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

        searchBaseWrapper = blbReportsManager.searchParentAgentChildReport( searchBaseWrapper );

        List<AgentParentChildReportModel> list = null;
        if(searchBaseWrapper.getCustomList() != null) {
            list = searchBaseWrapper.getCustomList().getResultsetList();
        }

        String successView = StringUtil.trimExtension( httpServletRequest.getServletPath() );
        return new ModelAndView( successView, "agentParentChildReportModelList", list );
    }

    public void setBlbReportsManager(BLBReportsManager blbReportsManager) {
        this.blbReportsManager = blbReportsManager;
    }

    public void setAccountFacade(AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }
}
