package com.inov8.microbank.server.dao.transactionmodule;

import java.util.List;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.transactionmodule.SalesSummaryListViewModel;

public interface SalesSummaryListViewDAO 
extends BaseDAO<SalesSummaryListViewModel, Long>
{
	public List<SalesSummaryListViewModel> getSalesSummary( BaseWrapper baseWrapper );
}
