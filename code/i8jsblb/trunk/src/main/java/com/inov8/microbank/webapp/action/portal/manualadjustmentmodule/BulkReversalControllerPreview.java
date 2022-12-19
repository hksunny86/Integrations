package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkAutoReversalModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

public class BulkReversalControllerPreview extends BaseSearchController{


	 public BulkReversalControllerPreview()
	    {
		    setFilterSearchCommandClass(BulkAutoReversalModel.class);
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
		List<BulkAutoReversalModel> bulkAutoReversalModelList = (List<BulkAutoReversalModel>) request.getSession().getAttribute("bulkAutoReversalModelList");
    	if(bulkAutoReversalModelList.size()>0){
    		for(BulkAutoReversalModel bulkAutoReversalModel : bulkAutoReversalModelList){
        		if(bulkAutoReversalModel.getIsValid() == true){
//        			if(bulkManualAdjustmentModel.getAmount()!=null){
//        				totalValidRecordTraxnAmount += bulkManualAdjustmentModel.getAmount();
//            			totalValidTraxns +=1;
//        			}
					totalValidTraxns +=1;
        		}else{
//        			if(bulkManualAdjustmentModel.getAmount() !=null){
//        				totalInValidRecordTraxnAmount += bulkManualAdjustmentModel.getAmount();
//        			}

        		}
        	}
    			totalInValidTraxns = bulkAutoReversalModelList.size() - totalValidTraxns;
    	}
    	
		request.setAttribute("totalValidRecordTraxnAmount", totalValidRecordTraxnAmount);
		request.setAttribute("totalInValidRecordTraxnAmount", totalInValidRecordTraxnAmount);
		request.setAttribute("totalValidTraxns", totalValidTraxns);
		request.setAttribute("totalInValidTraxns", totalInValidTraxns);
		
		if(CollectionUtils.isEmpty(bulkAutoReversalModelList))
    	{
    		pagingHelperModel.setTotalRecordsCount(0);
		}
    	else
    	{
    		pagingHelperModel.setTotalRecordsCount(bulkAutoReversalModelList.size());
    	}

        return new ModelAndView(getSearchView(),"bulkAutoReversalModelList",bulkAutoReversalModelList);
    }

}
