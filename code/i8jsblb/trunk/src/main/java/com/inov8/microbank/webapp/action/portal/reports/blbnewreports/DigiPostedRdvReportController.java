package com.inov8.microbank.webapp.action.portal.reports.blbnewreports;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.blbnewreports.DigiDebitCardAnnualReportViewModel;
import com.inov8.microbank.common.model.portal.blbnewreports.DigiPostedRdvReportViewModel;
import com.inov8.microbank.server.dao.blbnewreports.DigiDebitCardAnnualDAO;
import com.inov8.microbank.server.dao.blbnewreports.DigiPostedRdvDAO;
import org.hibernate.criterion.MatchMode;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DigiPostedRdvReportController extends BaseFormSearchController {

    private ReferenceDataManager referenceDataManager;
  private DigiPostedRdvDAO digiPostedRdvDAO;

    public DigiPostedRdvReportController()
    {
        super.setCommandName("DigiPostedRdvReportController");
        super.setCommandClass(DigiPostedRdvReportViewModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
      return null;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                                    PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

        DigiPostedRdvReportViewModel digiDebitCardAnnualReportViewModel = (DigiPostedRdvReportViewModel) o;

//        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
//                "cardRequestDate", digiDebitCardAnnualReportViewModel.getStartDate(),
//                digiDebitCardAnnualReportViewModel.getEndDate());
//        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>();
//        dateRangeHolderModels.add(dateRangeHolderModel);
//        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
//        digiDebitCardAnnualReportViewModel.setCardRequestDate(digiDebitCardAnnualReportViewModel.getCardRequestDate());
//        searchBaseWrapper.setBasePersistableModel(digiDebitCardAnnualReportViewModel);
        //sorting order
//        if(linkedHashMap.isEmpty())
//        {
//            linkedHashMap.put("cardRequestDate", SortingOrder.DESC);
//        }
//        searchBaseWrapper.setSortingOrderMap(linkedHashMap);

        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        exampleConfigHolderModel.setIgnoreCase(Boolean.FALSE);
        exampleConfigHolderModel.setExcludeZeroes(Boolean.TRUE);
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        CustomList<DigiPostedRdvReportViewModel> list =
//                vcFileDAO.findAll(searchBaseWrapper.getPagingHelperModel());
                this.digiPostedRdvDAO.findByExample(digiDebitCardAnnualReportViewModel,
                        searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(),
                        exampleConfigHolderModel);
        return new ModelAndView(getSuccessView(), "reqList", list.getResultsetList());

    }


    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setDigiPostedRdvDAO(DigiPostedRdvDAO digiPostedRdvDAO) {
        this.digiPostedRdvDAO = digiPostedRdvDAO;
    }
}
