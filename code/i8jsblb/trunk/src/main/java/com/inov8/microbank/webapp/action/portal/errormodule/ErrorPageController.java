package com.inov8.microbank.webapp.action.portal.errormodule;



import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.home.HomeSearchModel;

/**
 * <p>Title: Microbank Demo</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 */

public class ErrorPageController extends BaseFormSearchController
{
	
	public ErrorPageController()
	  {
		  
		  setCommandName("homeSearchModel");
		  setCommandClass(HomeSearchModel.class);
	  }
	
	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request,
			HttpServletResponse response, Object command,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {
		
		return null;
	}

}