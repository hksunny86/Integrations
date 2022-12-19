package com.inov8.microbank.tax.dao.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.tax.TargetedWhtDetailReportModel;
import com.inov8.microbank.common.model.tax.WHTSummaryViewModel;
import com.inov8.microbank.tax.dao.TargetedWhtDetailViewDAO;

public class TargetedWhtDetailReportHibernateDAO extends BaseHibernateDAO<TargetedWhtDetailReportModel, Long, TargetedWhtDetailViewDAO> implements TargetedWhtDetailViewDAO{

	@Override
	public List<TargetedWhtDetailReportModel> loadTargetedWHTDetail(
			TargetedWhtDetailReportModel model,
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap,
			DateRangeHolderModel dateRangeHolderModel)
			throws DataAccessException {

		Connection conn = this.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();
		
	java.sql.CallableStatement statement = null;
	
	try {
		statement = conn.prepareCall("{call WITHHOLDING_TAX_REPORT_PKG.SET_VALUES(?,?)}");
		
		if(dateRangeHolderModel.getFromDate()!=null){
			statement.setTimestamp(1, new java.sql.Timestamp(dateRangeHolderModel.getFromDate().getTime()));
		}else{
			statement.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
		}
		
		if(dateRangeHolderModel.getToDate()!=null){
			statement.setTimestamp(2, new java.sql.Timestamp(dateRangeHolderModel.getToDate().getTime()));
		}else{
			statement.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
		}
		
		statement.execute();
		
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	List<TargetedWhtDetailReportModel> targetedWhtDetailReportModelsList = null;
	
	CustomList<TargetedWhtDetailReportModel> customList = findByExample(model, null);
		
	if(null != customList && customList.getResultsetList() != null && customList.getResultsetList().size() > 0){
		targetedWhtDetailReportModelsList = customList.getResultsetList();
		pagingHelperModel.setTotalRecordsCount(targetedWhtDetailReportModelsList.size());
	}else{
		pagingHelperModel.setTotalRecordsCount(0);
	}
		
		return targetedWhtDetailReportModelsList;
	}

}
