package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;

import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by ABC on 1/10/2017.
 */
public class BulkManualAdjustmentReportController extends BaseFormSearchController {

    private ReferenceDataManager referenceDataManager;
    private ManualAdjustmentManager manualAdjustmentManager;


    public BulkManualAdjustmentReportController() {
        setCommandName("bulkManualAdjustmentModel");
        setCommandClass(BulkManualAdjustmentModel.class);
    }


    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setManualAdjustmentManager(ManualAdjustmentManager manualAdjustmentManager) {
        this.manualAdjustmentManager = manualAdjustmentManager;
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, Object> referenceDataMap = new HashMap<String, Object>(1);
        List<LabelValueBean> adjustmentTypeList = new ArrayList<LabelValueBean>();
        LabelValueBean adjustmentType = new LabelValueBean("BB to BB", "1");
        adjustmentTypeList.add(adjustmentType);
        adjustmentType = new LabelValueBean("BB to Core", "2");
        adjustmentTypeList.add(adjustmentType);
        adjustmentType = new LabelValueBean("Core to BB", "3");
        adjustmentTypeList.add(adjustmentType);
        referenceDataMap.put("adjustmentTypeList", adjustmentTypeList);
        return referenceDataMap;

    }

    @Override
    protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse httpServletResponse, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

        BulkManualAdjustmentModel bulkManualAdjustmentModel = (BulkManualAdjustmentModel) model;
        bulkManualAdjustmentModel.setIsApproved(true); // Only Approved records will be shown in report, that's why 'true' is set

        DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
                "createdOn", bulkManualAdjustmentModel.getStartDate(), bulkManualAdjustmentModel.getEndDate());

        searchBaseWrapper.setBasePersistableModel( (BulkManualAdjustmentModel) bulkManualAdjustmentModel );
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );


        if( sortingOrderMap.isEmpty() ){
            sortingOrderMap.put( "createdOn", SortingOrder.DESC );
        }


        searchBaseWrapper.setSortingOrderMap( sortingOrderMap );
        searchBaseWrapper = this.manualAdjustmentManager.loadBulkManualAdjustments( searchBaseWrapper );
        List<BulkManualAdjustmentModel> list = searchBaseWrapper.getCustomList().getResultsetList();

        String successView = StringUtil.trimExtension( req.getServletPath() );
        return new ModelAndView( successView, "bulkManualAdjustmentModelList", list );

    }
}
