package com.inov8.microbank.server.dao.postedtransactionreportmodule;

import java.util.LinkedHashMap;

import org.hibernate.criterion.Criterion;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionViewModel;

public interface PostedTransactionViewDao extends BaseDAO<PostedTransactionViewModel, Long>
{
	public CustomList<PostedTransactionViewModel> findBillPaymentTransactions(Criterion criterion,PostedTransactionViewModel exampleInstance, PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel,
			ExampleConfigHolderModel exampleConfigHolderModel, String... excludeProperty) throws DataAccessException;
}
