package com.inov8.microbank.webapp.action.portal.transactiondetaili8module;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ThirdPartyAccountOpeningModel;
import com.inov8.microbank.common.model.ThirdPartyAccountOpeningViewModel;
import com.inov8.microbank.server.dao.thirdpartcashoutmodule.ThirdPartyAcOpeningDAO;
import com.inov8.microbank.server.dao.thirdpartcashoutmodule.hibernate.ThirdPartyAccountOpeningViewDAO;
import org.hibernate.criterion.MatchMode;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ThirdPartyAccountOPeningReportController extends BaseFormSearchController {

    private ReferenceDataManager referenceDataManager;
    private ThirdPartyAccountOpeningViewDAO thirdPartyAccountOpeningViewDAO;

    public ThirdPartyAccountOPeningReportController(){
        setCommandClass(ThirdPartyAccountOpeningViewModel.class);
        setCommandName("thirdPartyAccountOpeningReport");
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

        ThirdPartyAccountOpeningViewModel thirdPartyAccountOpeningViewModel = (ThirdPartyAccountOpeningViewModel) o;
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "createdOn", thirdPartyAccountOpeningViewModel.getCreatedOn(),
                thirdPartyAccountOpeningViewModel.getCreatedOnToDate());
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        searchBaseWrapper.setBasePersistableModel(thirdPartyAccountOpeningViewModel);
        if(linkedHashMap.isEmpty())
        {
            linkedHashMap.put("createdOn", SortingOrder.DESC);
        }
        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
                ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        CustomList<ThirdPartyAccountOpeningViewModel> list = this.thirdPartyAccountOpeningViewDAO.findByExample(thirdPartyAccountOpeningViewModel,
                searchBaseWrapper.getPagingHelperModel(),searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel(),
                exampleConfigHolderModel);
        return new ModelAndView( getSuccessView(), "thirdPartyList",list.getResultsetList());
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setThirdPartyAccountOpeningViewDAO(ThirdPartyAccountOpeningViewDAO thirdPartyAccountOpeningViewDAO) {
        this.thirdPartyAccountOpeningViewDAO = thirdPartyAccountOpeningViewDAO;
    }
}
