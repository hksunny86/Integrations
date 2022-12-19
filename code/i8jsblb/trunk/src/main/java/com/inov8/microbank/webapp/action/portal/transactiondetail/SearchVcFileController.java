package com.inov8.microbank.webapp.action.portal.transactiondetail;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ClsPendingAccountOpeningModel;
import com.inov8.microbank.common.model.VCFileModel;
import com.inov8.microbank.common.model.VCFileReportModel;
import com.inov8.microbank.hra.paymtnc.model.PayMtncRequestModel;
import com.inov8.microbank.hra.service.HRAManager;
import com.inov8.microbank.server.dao.safrepo.VCFileDAO;
import com.inov8.microbank.server.dao.safrepo.VCFileReportDAO;
import org.hibernate.criterion.MatchMode;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchVcFileController extends BaseFormSearchController {

    private HRAManager hraManager;
    private ReferenceDataManager referenceDataManager;

    private VCFileDAO vcFileDAO;

    public SearchVcFileController() {
        setCommandClass(VCFileModel.class);
        setCommandName("SearchVcFileController");
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

        VCFileModel vcFileModel = (VCFileModel) o;

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "transactionDate", vcFileModel.getStart(),
                vcFileModel.getEnd());
        ArrayList<DateRangeHolderModel> dateRangeHolderModels = new ArrayList<>();
        dateRangeHolderModels.add(dateRangeHolderModel);
        searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
        vcFileModel.setTransactionType(vcFileModel.getTransactionType());
        vcFileModel.setChannel(vcFileModel.getChannel());
        vcFileModel.setTransactionOrigin(vcFileModel.getTransactionOrigin());
        vcFileModel.setAuthorizationNumber(vcFileModel.getAuthorizationNumber());
        vcFileModel.setTransactionDate(vcFileModel.getTransactionDate());
        searchBaseWrapper.setBasePersistableModel(vcFileModel);

        if (linkedHashMap.isEmpty()) {
            linkedHashMap.put("transactionDate", SortingOrder.DESC);

        }

        searchBaseWrapper.setSortingOrderMap(linkedHashMap);
        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        exampleConfigHolderModel.setIgnoreCase(Boolean.FALSE);
        exampleConfigHolderModel.setExcludeZeroes(Boolean.TRUE);
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        CustomList<VCFileModel> list =
//                vcFileDAO.findAll(searchBaseWrapper.getPagingHelperModel());
                this.vcFileDAO.findByExample(vcFileModel,
                        searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(),
                        exampleConfigHolderModel);
        return new ModelAndView(getSuccessView(), "reqList", list.getResultsetList());

    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setHraManager(HRAManager hraManager) {
        this.hraManager = hraManager;
    }

    public void setVcFileDAO(VCFileDAO vcFileDAO) {
        this.vcFileDAO = vcFileDAO;
    }
}
