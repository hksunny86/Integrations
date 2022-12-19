package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkAutoReversalModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualReversalManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BulkReversalFormController extends BaseFormSearchController {

    private ManualReversalManager manualReversalManager;

    public BulkReversalFormController() {
        setCommandName("bulkAutoReversalModel");
        setCommandClass(BulkAutoReversalModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, Object> referenceDataMap = new HashMap<String, Object>(1);
        return referenceDataMap;
    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                                    PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

        BulkAutoReversalModel bulkAutoReversalModel = (BulkAutoReversalModel) o;
        bulkAutoReversalModel.setApproved(true);

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "createdOn", bulkAutoReversalModel.getStartDate(), bulkAutoReversalModel.getEndDate());

        searchBaseWrapper.setBasePersistableModel( (BulkAutoReversalModel) bulkAutoReversalModel );
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

        if( sortingOrderMap.isEmpty() ){
            sortingOrderMap.put( "createdOn", SortingOrder.DESC );
        }

        searchBaseWrapper.setSortingOrderMap( sortingOrderMap );
        searchBaseWrapper = this.manualReversalManager.loadBulkAutoReversals( searchBaseWrapper );
        List<BulkAutoReversalModel> list = searchBaseWrapper.getCustomList().getResultsetList();

        String successView = StringUtil.trimExtension( httpServletRequest.getServletPath() );
        return new ModelAndView( successView, "bulkAutoReversalModelList", list );
    }

    public void setManualReversalManager(ManualReversalManager manualReversalManager) {
        this.manualReversalManager = manualReversalManager;
    }
}
