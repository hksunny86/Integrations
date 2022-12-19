package com.inov8.microbank.server.dao.portal.reports.finance;

import java.util.LinkedHashMap;
import java.util.List;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.financereportsmodule.SettlementClosingBalanceViewModel;

/**
 * 
 * @author AtifHu
 * 
 */
public interface SettlementClosingBalanceViewDAO extends
		BaseDAO<SettlementClosingBalanceViewModel, Long> {

	List<Object[]> searchSettlementClosingBalance(SettlementClosingBalanceViewModel settlementClosingBalanceViewModel, 
			DateRangeHolderModel dateRangeHolderModel, LinkedHashMap<String, SortingOrder> sortingOrderMap, PagingHelperModel pagingHelperModel);
}
