package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.vo.account.BulkCustomerAccountVo;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 15, 2014 7:00:00 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class BulkCustomersPreviewController extends BaseSearchController
{

    public BulkCustomersPreviewController()
    {
	    setFilterSearchCommandClass(BulkCustomerAccountVo.class);
    }

    @SuppressWarnings("unchecked")
	@Override
    protected ModelAndView onSearch( PagingHelperModel pagingHelperModel, Object model, HttpServletRequest req, LinkedHashMap<String, SortingOrder> sortingOrderMap ) throws Exception
    {
    	List<BulkCustomerAccountVo> bulkCustomerAccountVoList = (List<BulkCustomerAccountVo>) req.getSession().getAttribute("bulkCustomerAccountVoList");
    	if(CollectionUtils.isEmpty(bulkCustomerAccountVoList))
    	{
    		pagingHelperModel.setTotalRecordsCount(0);
		}
    	else
    	{
    		pagingHelperModel.setTotalRecordsCount(bulkCustomerAccountVoList.size());
    	}

        return new ModelAndView( getSearchView() );
    }

}
