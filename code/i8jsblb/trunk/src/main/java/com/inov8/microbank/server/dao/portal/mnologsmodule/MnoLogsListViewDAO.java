package com.inov8.microbank.server.dao.portal.mnologsmodule;

import java.util.LinkedHashMap;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.mnologsmodule.MnologsListViewModel;

public interface MnoLogsListViewDAO extends BaseDAO<MnologsListViewModel, Long> {

	List<UsecaseModel> findUsecases( Long[] usecaseIds );
	
	CustomList<MnologsListViewModel> findByExampleUnSorted(MnologsListViewModel exampleInstance, PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel,
			ExampleConfigHolderModel exampleConfigHolderModel, String... excludeProperty) throws DataAccessException;

	MnologsListViewModel getActionLogXMLByActionLogId(Long actionLogId) throws FrameworkCheckedException;

	List<MnologsListViewModel> getActionLogData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}
