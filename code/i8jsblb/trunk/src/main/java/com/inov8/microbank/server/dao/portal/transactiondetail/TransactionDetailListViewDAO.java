package com.inov8.microbank.server.dao.portal.transactiondetail;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionDetailListViewModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Asad Hayat
 * @version 1.0
 */
public interface TransactionDetailListViewDAO extends BaseDAO< TransactionDetailListViewModel, Long> {

	public SearchBaseWrapper getTransactionDetail(SearchBaseWrapper searchBaseWrapper, String mfsId) throws FrameworkCheckedException;
}
