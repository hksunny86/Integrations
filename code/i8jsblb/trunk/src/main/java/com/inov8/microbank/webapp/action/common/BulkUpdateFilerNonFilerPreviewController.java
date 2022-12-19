package com.inov8.microbank.webapp.action.common;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.vo.BulkFilerNonFilerVO;

public class BulkUpdateFilerNonFilerPreviewController extends BaseSearchController {

	public BulkUpdateFilerNonFilerPreviewController(){
		setFilterSearchCommandClass(BulkFilerNonFilerVO.class);
	}
	
	@Override
	protected ModelAndView onSearch(PagingHelperModel arg0, Object arg1,
			HttpServletRequest arg2, LinkedHashMap<String, SortingOrder> arg3)
			throws Exception {
		
	Long totalValidRecords =(long) 0;
	Long totalInValidRecords =(long) 0;
	
		List<BulkFilerNonFilerVO> bulkFilerNonFilerVOList = (List<BulkFilerNonFilerVO>) arg2.getSession().getAttribute("bulkFilerNonFilerVOList");
	
    	if(bulkFilerNonFilerVOList.size()>0){
    		for(BulkFilerNonFilerVO bulkFilerNonFilerVO : bulkFilerNonFilerVOList){
        		if(bulkFilerNonFilerVO.getIsValid() == true){
        			totalValidRecords++;
        			}
        	}
    		totalInValidRecords = bulkFilerNonFilerVOList.size() - totalValidRecords;
    	}
		
		arg2.setAttribute("totalValidRecords", totalValidRecords);
		arg2.setAttribute("totalInValidRecords", totalInValidRecords);
		
		if(CollectionUtils.isEmpty(bulkFilerNonFilerVOList))
    	{
			arg0.setTotalRecordsCount(0);
		}
    	else
    	{
    		arg0.setTotalRecordsCount(bulkFilerNonFilerVOList.size());
    	}

        return new ModelAndView(getSearchView(),"bulkFilerNonFilerVOList",bulkFilerNonFilerVOList);
}

}
