package com.inov8.microbank.server.dao.transactiondetailinfomodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.MiniStatementListViewModel;

public interface MiniStatementListViewDAO extends BaseDAO<MiniStatementListViewModel, Long> {

	public List<MiniStatementListViewModel> getMiniStatementListViewModelList(MiniStatementListViewModel model, Integer fetchSize) throws FrameworkCheckedException;
}
