package com.inov8.microbank.server.dao.postedtransactionreportmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.TransactionLogModel;

public interface TransactionLogDAO extends BaseDAO<TransactionLogModel, Long>{
	public CustomList<TransactionLogModel> searchPostedTransactions(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}
