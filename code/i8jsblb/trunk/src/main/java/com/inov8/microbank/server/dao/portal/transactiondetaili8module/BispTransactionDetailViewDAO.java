package com.inov8.microbank.server.dao.portal.transactiondetaili8module;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.BispTransactionDetailViewModel;

public interface BispTransactionDetailViewDAO extends BaseDAO<BispTransactionDetailViewModel, Long> {

    public SearchBaseWrapper searchBispTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException ;

}
