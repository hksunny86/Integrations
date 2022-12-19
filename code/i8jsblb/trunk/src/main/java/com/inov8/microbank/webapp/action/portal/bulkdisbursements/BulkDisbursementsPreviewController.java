package com.inov8.microbank.webapp.action.portal.bulkdisbursements;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.BulkDisbursementsModel;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jun 27, 2014 6:00:00 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class BulkDisbursementsPreviewController extends BaseSearchController
{

    public BulkDisbursementsPreviewController()
    {
	    setFilterSearchCommandClass(BulkDisbursementsModel.class);
    }

    @SuppressWarnings("unchecked")
	@Override
    protected ModelAndView onSearch( PagingHelperModel pagingHelperModel, Object model, HttpServletRequest req, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
    	List<BulkDisbursementsModel> bulkDisbursementsModelList = (List<BulkDisbursementsModel>) req.getSession().getAttribute("bulkDisbursementsModelList");
    	if(CollectionUtils.isEmpty(bulkDisbursementsModelList))
    	{
    		pagingHelperModel.setTotalRecordsCount(0);
		}
    	else
    	{
    		pagingHelperModel.setTotalRecordsCount(bulkDisbursementsModelList.size());
    	}

        return new ModelAndView( getSearchView() );
    }

}
