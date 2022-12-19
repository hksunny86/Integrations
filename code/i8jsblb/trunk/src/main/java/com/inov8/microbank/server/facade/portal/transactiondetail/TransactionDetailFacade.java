package com.inov8.microbank.server.facade.portal.transactiondetail;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionAllViewModel;
import com.inov8.microbank.server.service.portal.transactiondetail.TransactionDetailManager;

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


	public interface TransactionDetailFacade extends TransactionDetailManager
	{
		CustomList<TransactionAllViewModel> searchTransactionAllView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;

		SearchBaseWrapper searchTransactionCodeHtrViewModel(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	}
