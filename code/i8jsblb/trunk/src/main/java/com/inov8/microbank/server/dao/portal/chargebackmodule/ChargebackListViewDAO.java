package com.inov8.microbank.server.dao.portal.chargebackmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.chargebackmodule.ChargebackListViewModel;


public interface ChargebackListViewDAO extends BaseDAO<ChargebackListViewModel, Long> 
{
	public CustomList<ChargebackListViewModel> getChargeBackTransactions(SearchBaseWrapper wrapper) throws FrameworkCheckedException;
}
