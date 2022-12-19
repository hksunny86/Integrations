package com.inov8.microbank.webapp.action.portal.clsscreenigmodule;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoVeiwModel;
import com.inov8.microbank.common.model.portal.bookmemodule.BookMeTransactionDetailViewModel;
import com.inov8.microbank.server.service.pendingaccountopeningmodule.dao.PendingAccountOpeningSafRepoSearchDAO;
import com.inov8.microbank.server.service.pendingaccountopeningmodule.dao.hibernate.PendingAccountOpeningSafRepoSearchHibernateDAO;
import org.hibernate.criterion.MatchMode;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClsScreeningSearchController extends BaseFormSearchController {
    private ReferenceDataManager referenceDataManager;
private PendingAccountOpeningSafRepoSearchDAO pendingAccountOpeningSafRepoSearchDAO;



    public ClsScreeningSearchController() {

        super.setCommandName("accountOpeningPendingSafRepoVeiwModel");
        super.setCommandClass(AccountOpeningPendingSafRepoVeiwModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map map = new HashMap();
        return map;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

        AccountOpeningPendingSafRepoVeiwModel accountOpeningPendingSafRepoVeiwModel = (AccountOpeningPendingSafRepoVeiwModel) o;

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "createdOn", accountOpeningPendingSafRepoVeiwModel.getCreatedOn(),
                accountOpeningPendingSafRepoVeiwModel.getUpdatedOn());
        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>();
        dateRangeHolderModels.add(dateRangeHolderModel);
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        accountOpeningPendingSafRepoVeiwModel.setCompleted("0");
        searchBaseWrapper.setBasePersistableModel(accountOpeningPendingSafRepoVeiwModel);

        if(linkedHashMap.isEmpty())
        {
            linkedHashMap.put("createdOn", SortingOrder.DESC);

        }

        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        CustomList<AccountOpeningPendingSafRepoVeiwModel> list = this.pendingAccountOpeningSafRepoSearchDAO.findByExample(accountOpeningPendingSafRepoVeiwModel,
                searchBaseWrapper.getPagingHelperModel(),searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel(),
                exampleConfigHolderModel);
        return new ModelAndView( getSuccessView(), "bookMeList",list.getResultsetList());
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
    {
        this.referenceDataManager = referenceDataManager;
    }



    public void setPendingAccountOpeningSafRepoSearchDAO(PendingAccountOpeningSafRepoSearchDAO pendingAccountOpeningSafRepoSearchDAO) {
        this.pendingAccountOpeningSafRepoSearchDAO = pendingAccountOpeningSafRepoSearchDAO;
    }
}
