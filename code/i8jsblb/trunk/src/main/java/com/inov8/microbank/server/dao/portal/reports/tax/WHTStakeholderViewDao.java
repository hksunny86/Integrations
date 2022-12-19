package com.inov8.microbank.server.dao.portal.reports.tax;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.reportmodule.WHTStakeholderViewModel;

/**
 * @author Kashif
 */
public interface WHTStakeholderViewDao extends BaseDAO<WHTStakeholderViewModel, Long> {
	
	public CustomList<WHTStakeholderViewModel> searchWHTStakeholderView(WHTStakeholderViewModel viewModel, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap, DateRangeHolderModel dateRangeHolderModel, String... excludeProperty) throws DataAccessException, SQLException;
	
}