package com.inov8.microbank.server.dao.commissionstakeholdermodule;

import java.util.LinkedHashMap;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.commissionmodule.CommShAcctsListViewModel;

public interface CommissionStakeholderAccountsListViewDAO
extends BaseDAO<CommShAcctsListViewModel, Long>
{

	public CustomList<CommShAcctsListViewModel> searchCommissionStakeholderAccountsByCriteria(
			CommShAcctsListViewModel commShAcctsListViewModel,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap) throws FrameworkCheckedException;
}
