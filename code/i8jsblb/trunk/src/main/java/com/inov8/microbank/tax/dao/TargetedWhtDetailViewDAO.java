package com.inov8.microbank.tax.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.tax.TargetedWhtDetailReportModel;
import com.inov8.microbank.common.model.tax.WHTSummaryViewModel;

/**
 * 
 * Created by Tayyab on 12/02/2018
 *
 */

public interface TargetedWhtDetailViewDAO extends BaseDAO<TargetedWhtDetailReportModel, Long > {
	List<TargetedWhtDetailReportModel> loadTargetedWHTDetail(TargetedWhtDetailReportModel model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap,
			DateRangeHolderModel dateRangeHolderModel) throws DataAccessException ;

}
