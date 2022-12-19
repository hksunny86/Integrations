package com.inov8.microbank.server.dao.portal.reports.tax;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.reportmodule.WHTAgentReportViewModel;
import com.inov8.microbank.common.model.tax.WHTSummaryViewModel;

public interface AgentCustWHTSummaryDAO extends BaseDAO<WHTSummaryViewModel, Long> {

	List<WHTSummaryViewModel> loadAgentCustWHTReport(WHTSummaryViewModel exampleInstance, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel) throws DataAccessException;
	
}
