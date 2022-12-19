package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;

public class BulkManualAdjustmentPreviewController extends BaseSearchController{
	
	
	 public BulkManualAdjustmentPreviewController()
	    {
		    setFilterSearchCommandClass(BulkManualAdjustmentModel.class);
	    }

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
			Object model, HttpServletRequest request,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {
		Double totalValidRecordTraxnAmount = 0.0;
		Double totalInValidRecordTraxnAmount = 0.0;
		Long totalValidTraxns =(long) 0;
		Long totalInValidTraxns =(long) 0;
		List<BulkManualAdjustmentModel> bulkManualAdjustmentsModelList = (List<BulkManualAdjustmentModel>) request.getSession().getAttribute("bulkManualAdjustmentsModelList");
    	if(bulkManualAdjustmentsModelList.size()>0){
    		for(BulkManualAdjustmentModel bulkManualAdjustmentModel : bulkManualAdjustmentsModelList){
        		if(bulkManualAdjustmentModel.getIsValid() == true){
        			if(bulkManualAdjustmentModel.getAmount()!=null){
        				totalValidRecordTraxnAmount += bulkManualAdjustmentModel.getAmount();
            			totalValidTraxns +=1;
        			}
        			
        		}else{
        			if(bulkManualAdjustmentModel.getAmount() !=null){
        				totalInValidRecordTraxnAmount += bulkManualAdjustmentModel.getAmount();
        			}
        			
        		}
        	}
    			totalInValidTraxns = bulkManualAdjustmentsModelList.size() - totalValidTraxns;
    	}
    	
		request.setAttribute("totalValidRecordTraxnAmount", totalValidRecordTraxnAmount);
		request.setAttribute("totalInValidRecordTraxnAmount", totalInValidRecordTraxnAmount);
		request.setAttribute("totalValidTraxns", totalValidTraxns);
		request.setAttribute("totalInValidTraxns", totalInValidTraxns);
		
		if(CollectionUtils.isEmpty(bulkManualAdjustmentsModelList))
    	{
    		pagingHelperModel.setTotalRecordsCount(0);
		}
    	else
    	{
    		pagingHelperModel.setTotalRecordsCount(bulkManualAdjustmentsModelList.size());
    	}

        return new ModelAndView(getSearchView(),"bulkManualAdjustmentsModelList",bulkManualAdjustmentsModelList);
    }

}
