package com.inov8.microbank.server.dao.portal.reports.agent;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.agentreportsmodule.AgentTransactionDetailViewModel;
import com.inov8.microbank.common.model.portal.agentreportsmodule.AgentTransactionSummaryViewModel;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Mar 29, 2013 3:56:35 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public interface AgentTransactionDetailViewDao extends BaseDAO<AgentTransactionDetailViewModel, Long>
{
	 List<AgentTransactionDetailViewModel> loadAgentTransactionSummary(AgentTransactionDetailViewModel exampleInstance, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel, String... excludeProperty) throws DataAccessException;
	 
	 List<AgentTransactionDetailViewModel> loadAgentTransactionDetail(AgentTransactionDetailViewModel model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel, String... excludeProperty) throws DataAccessException;
}
