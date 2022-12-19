package com.inov8.microbank.server.dao.portal.transactiondetail;

import java.util.LinkedHashMap;
import java.util.List;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionAllViewModel;

/**
 * @author Naseer
 */
public interface TransactionAllViewDao extends BaseDAO<TransactionAllViewModel, Long>
{
    List<?> findWalkInCustomerTransactions(String cnic,TransactionAllViewModel transactionAllViewModel, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap,DateRangeHolderModel dateRangeHolderModel);
}
