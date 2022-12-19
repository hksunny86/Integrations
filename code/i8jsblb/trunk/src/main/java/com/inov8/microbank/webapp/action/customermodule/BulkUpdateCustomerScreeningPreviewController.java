package com.inov8.microbank.webapp.action.customermodule;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.customermodule.BulkUpdateCustomerScreeningModel;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;

public class BulkUpdateCustomerScreeningPreviewController extends BaseSearchController{

	public BulkUpdateCustomerScreeningPreviewController(){
		setFilterSearchCommandClass(BulkUpdateCustomerScreeningModel.class);
	}
	
	@Override
	protected ModelAndView onSearch(PagingHelperModel arg0, Object arg1,
			HttpServletRequest arg2, LinkedHashMap<String, SortingOrder> arg3)
			throws Exception {
			
		Long totalValidRecords =(long) 0;
		Long totalInValidRecords =(long) 0;
		
			List<BulkUpdateCustomerScreeningModel> BulkUpdateCustomerScreeningModels = (List<BulkUpdateCustomerScreeningModel>) arg2.getSession().getAttribute("bulkUpdateCustomerScreeningModelsLsit");
		
	    	if(BulkUpdateCustomerScreeningModels.size()>0){
	    		for(BulkUpdateCustomerScreeningModel bulkUpdateCustomerScreeningModel : BulkUpdateCustomerScreeningModels){
	        		if(bulkUpdateCustomerScreeningModel.getIsValid() == true){
	        			totalValidRecords++;
	        			}
	        	}
	    		totalInValidRecords = BulkUpdateCustomerScreeningModels.size() - totalValidRecords;
	    	}
			
			arg2.setAttribute("totalValidRecords", totalValidRecords);
			arg2.setAttribute("totalInValidRecords", totalInValidRecords);
			
			if(CollectionUtils.isEmpty(BulkUpdateCustomerScreeningModels))
	    	{
				arg0.setTotalRecordsCount(0);
			}
	    	else
	    	{
	    		arg0.setTotalRecordsCount(BulkUpdateCustomerScreeningModels.size());
	    	}

	        return new ModelAndView(getSearchView(),"bulkUpdateCustomerScreeningModelsList",BulkUpdateCustomerScreeningModels);
	}

}
