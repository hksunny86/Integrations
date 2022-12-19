package com.inov8.microbank.server.service.postedtransactionmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.postedrransactionreportmodule.IntgTransactionTypeModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionNadraViewModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionViewModel;

public interface PostedTransactionReportManager 
{
	public BaseWrapper createOrUpdatePostedTransactionRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	CustomList<PostedTransactionViewModel> searchPostedTransactionView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;

	List<PostedTransactionNadraViewModel> searchPostedTransactionNadraView( SearchBaseWrapper searchBaseWrapper ) throws FrameworkCheckedException;

	List<IntgTransactionTypeModel> fetchIntgTransactionTypes( java.util.List<IntgTransactionTypeModel> intgTransactionTypeModel, String propertyToSortBy, SortingOrder sortingOrder, Long[] intgTransactionTypeIds ) throws FrameworkCheckedException;

	CustomList<PostedTransactionViewModel> searchPostedTransactionView(PostedTransactionViewModel postedTransactionViewModel)throws FrameworkCheckedException;
}
