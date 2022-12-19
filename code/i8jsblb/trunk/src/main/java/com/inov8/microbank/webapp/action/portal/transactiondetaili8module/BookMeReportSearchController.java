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
import com.inov8.microbank.common.model.portal.bookmemodule.BookMeTransactionDetailViewModel;
import com.inov8.microbank.server.dao.portal.bookmemodule.BookMeTransactionDetailDAO;
import org.hibernate.criterion.MatchMode;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class BookMeReportSearchController extends BaseFormSearchController {

    private ReferenceDataManager referenceDataManager;
    private BookMeTransactionDetailDAO bookMeTransactionDetailDAO;

    public BookMeReportSearchController()
    {
        super.setCommandName("bookmeTransactionDetailModel");
        super.setCommandClass(BookMeTransactionDetailViewModel.class);
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

        BookMeTransactionDetailViewModel bookMeTransactionDetailViewModel = (BookMeTransactionDetailViewModel) o;
        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "createdOn", bookMeTransactionDetailViewModel.getCreatedOn(),
                bookMeTransactionDetailViewModel.getCreatedOnToDate());
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        searchBaseWrapper.setBasePersistableModel(bookMeTransactionDetailViewModel);

        if(linkedHashMap.isEmpty())
        {
            linkedHashMap.put("createdOn", SortingOrder.DESC);
        }

        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        CustomList<BookMeTransactionDetailViewModel> list = this.bookMeTransactionDetailDAO.findByExample(bookMeTransactionDetailViewModel,
                searchBaseWrapper.getPagingHelperModel(),searchBaseWrapper.getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel(),
                exampleConfigHolderModel);
        return new ModelAndView( getSuccessView(), "bookMeList",list.getResultsetList());
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
    {
        this.referenceDataManager = referenceDataManager;
    }

    public void setBookMeTransactionDetailDAO(BookMeTransactionDetailDAO bookMeTransactionDetailDAO) {
        this.bookMeTransactionDetailDAO = bookMeTransactionDetailDAO;
    }
}
