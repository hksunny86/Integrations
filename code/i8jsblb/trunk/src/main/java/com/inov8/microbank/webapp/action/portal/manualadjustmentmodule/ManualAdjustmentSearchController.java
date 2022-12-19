package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ManualAdjustmentModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class ManualAdjustmentSearchController extends BaseFormSearchController{
    
	private ManualAdjustmentManager manualAdjustmentManager;
    private ReferenceDataManager referenceDataManager;
    
	public ManualAdjustmentSearchController() {
		setCommandName("manualAdjustmentModel");
		setCommandClass(ManualAdjustmentModel.class);
	}

	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest req) throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(1);
		List<LabelValueBean> adjustmentTypeList = new ArrayList<LabelValueBean>();
	    LabelValueBean adjustmentType = new LabelValueBean("BB to BB", "1");
	    adjustmentTypeList.add(adjustmentType);
	    adjustmentType = new LabelValueBean("BB to Core", "2");
	    adjustmentTypeList.add(adjustmentType);
	    adjustmentType = new LabelValueBean("Core to BB", "3");
	    adjustmentTypeList.add(adjustmentType);
	    referenceDataMap.put("adjustmentTypeList", adjustmentTypeList);

	    //********************************************************************************
		// Adding Adjustment Category List
		List<LabelValueBean> adjustmentCategoryList = new ArrayList<LabelValueBean>();
		LabelValueBean adjustmentCategory = new LabelValueBean("Single", "1");
		adjustmentCategoryList.add(adjustmentCategory);
		adjustmentCategory = new LabelValueBean("Bulk", "2");
		adjustmentCategoryList.add(adjustmentCategory);
		referenceDataMap.put("adjustmentCategoryList", adjustmentCategoryList);

	    return referenceDataMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse res, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

		ManualAdjustmentModel manualAdjustmentModel = (ManualAdjustmentModel) model;

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", manualAdjustmentModel.getStartDate(), manualAdjustmentModel.getEndDate());

		searchBaseWrapper.setBasePersistableModel( (ManualAdjustmentModel) manualAdjustmentModel );
        searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

        if( sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put( "createdOn", SortingOrder.DESC );
		}

        searchBaseWrapper.setSortingOrderMap( sortingOrderMap ); 
        searchBaseWrapper = this.manualAdjustmentManager.loadManualAdjustments( searchBaseWrapper );
		List<ManualAdjustmentModel> list = searchBaseWrapper.getCustomList().getResultsetList();
		
		String successView = StringUtil.trimExtension( req.getServletPath() );
        return new ModelAndView( successView, "manualAdjustmentModelList", list );
	}

	public ManualAdjustmentManager getManualAdjustmentManager() {
		return manualAdjustmentManager;
	}

	public void setManualAdjustmentManager(ManualAdjustmentManager manualAdjustmentManager) {
		this.manualAdjustmentManager = manualAdjustmentManager;
	}

	public ReferenceDataManager getReferenceDataManager() {
		return referenceDataManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
}