package com.inov8.microbank.webapp.action.portal.ibft;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.IBFTRetryAdviceModel;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.facade.portal.transactionreversal.TransactionReversalFacade;

public class IBFTRetryAdviceHistoryController extends BaseSearchController {

	@Autowired
	private TransactionReversalFacade transactionReversalFacade;

	public IBFTRetryAdviceHistoryController() {
		setFilterSearchCommandClass(IBFTRetryAdviceModel.class);
	}
	
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
			Object model, HttpServletRequest request,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {

		String stan = request.getParameter("stan");
		String reqDateParam = request.getParameter("reqdate");
		
		List<IBFTRetryAdviceModel> resultList = new ArrayList<IBFTRetryAdviceModel>();
		if (validateInput(stan,reqDateParam)){
			
			Date requestDate = this.parseDate(reqDateParam);
			resultList = transactionReversalFacade.getIbftRetryAdviceList(stan, requestDate, null);
		}
		
		if(resultList != null){
			pagingHelperModel.setTotalRecordsCount(resultList.size());
		}else{
			pagingHelperModel.setTotalRecordsCount(0);
		}
		
		return new ModelAndView( getSearchView(), "ibftHistoryList", resultList);
	}
	
	private boolean validateInput(String stan, String reqDate){
		boolean result = true;
		
		if(StringUtil.isNullOrEmpty(stan) || StringUtil.isNullOrEmpty(reqDate) || null == parseDate(reqDate)){
			result = false;	
		}
			
		return result;
	}
	
	private Date parseDate(String reqDate){
		try {
			
			if(reqDate.length() == 19){ // e.g. 2015-02-17 12:30:12
				return PortalDateUtils.parseStringAsDate(reqDate, "yyyy-MM-dd HH:mm:ss");
			}
		} catch (ParseException e) {
			logger.error("[IBFTRetryAdviceHistoryController] Invalid Date provided",e);
		}
		return null;
	}
	
	public void setTransactionReversalFacade(TransactionReversalFacade transactionReversalFacade) {
		this.transactionReversalFacade = transactionReversalFacade;
	}
}