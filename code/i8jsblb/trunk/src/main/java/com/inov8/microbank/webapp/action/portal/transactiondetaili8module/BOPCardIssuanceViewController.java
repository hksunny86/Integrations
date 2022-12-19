package com.inov8.microbank.webapp.action.portal.transactiondetaili8module;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.BOPCardIssuanceViewModel;
import com.inov8.microbank.server.dao.thirdpartcashoutmodule.BOPCardIssuanceViewDAO;
import org.hibernate.criterion.MatchMode;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class BOPCardIssuanceViewController extends BaseFormSearchController {

    private BOPCardIssuanceViewDAO bopCardIssuanceViewDAO;

    public BOPCardIssuanceViewController()
    {
        super.setCommandName("bopcardissuancereport");
        super.setCommandClass(BOPCardIssuanceViewModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map map = new HashMap();
        return map;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                                    PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
        BOPCardIssuanceViewModel bopCardIssuanceViewModel = (BOPCardIssuanceViewModel) o;

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "createdOn", bopCardIssuanceViewModel.getCreatedOn(),
                bopCardIssuanceViewModel.getCreatedOnToDate());
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        searchBaseWrapper.setBasePersistableModel(bopCardIssuanceViewModel);
        if(linkedHashMap.isEmpty())
        {
            linkedHashMap.put("createdOn", SortingOrder.DESC);
        }
        searchBaseWrapper.setSortingOrderMap(linkedHashMap);

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        CustomList<BOPCardIssuanceViewModel> customList = this.bopCardIssuanceViewDAO.findByExample(bopCardIssuanceViewModel,
                searchBaseWrapper.getPagingHelperModel(),searchBaseWrapper.getSortingOrderMap(),
                searchBaseWrapper.getDateRangeHolderModel(),exampleConfigHolderModel);

        return new ModelAndView( getSuccessView(), "bopCardIssuanceList",customList.getResultsetList());
    }

    public void setBopCardIssuanceViewDAO(BOPCardIssuanceViewDAO bopCardIssuanceViewDAO) {
        this.bopCardIssuanceViewDAO = bopCardIssuanceViewDAO;
    }
}
