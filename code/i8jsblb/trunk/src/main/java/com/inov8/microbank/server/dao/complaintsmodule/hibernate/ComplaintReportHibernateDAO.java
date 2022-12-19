package com.inov8.microbank.server.dao.complaintsmodule.hibernate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ComplaintReportModel;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintReportDAO;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.service.complaintmodule.ComplaintStatusEnum;

public class ComplaintReportHibernateDAO extends BaseHibernateDAO<ComplaintReportModel, Long, ComplaintReportDAO> implements ComplaintReportDAO {
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	private List<Long> getComplaintIdsByReferenceNo(String refNo) throws FrameworkCheckedException{
		List<Long> complaintIds = new ArrayList<Long>(0);
		try{
			String sql = "select complaint_id from complaint_parameter_value where complaint_parameter_id = 6 and value = ?";
			complaintIds = jdbcTemplate.queryForList( sql, new String[]{refNo},Long.class);
		}catch(Exception ex){
			throw new FrameworkCheckedException(ex.getMessage(),ex);
		}
		
		return complaintIds;
	}	

	public List<ComplaintReportModel> searchComplaintByConsumerNo(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		List<ComplaintReportModel> complaintList = new ArrayList<ComplaintReportModel>(0);
		
		ComplaintReportModel model = (ComplaintReportModel)wrapper.getBasePersistableModel();
		String refNo = model.getConsumerNo();
		
		List<Long> complaintIds = getComplaintIdsByReferenceNo(refNo);
		Criterion criterion = null;
		if(complaintIds != null && complaintIds.size() > 0){
			criterion = Restrictions.in("complaintId", complaintIds);
		}else{
			criterion = Restrictions.eq("transactionId", refNo);
		}
		
		CustomList<ComplaintReportModel> customList = super.findByCriteria(criterion, new ComplaintReportModel(), wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap());
		if(customList!=null && customList.getResultsetList().size() > 0){
			complaintList = customList.getResultsetList();
		}

		return complaintList;
	}

	public List<ComplaintReportModel> searchUserComplaintHistory(Long appUserId) throws FrameworkCheckedException {
		List<ComplaintReportModel> complaintList = new ArrayList<ComplaintReportModel>(0);
		
		Criterion criterion1 = Restrictions.eq("initAppUserId", appUserId);
		Criterion criterion2 = Restrictions.in("status", new String[]{ComplaintsModuleConstants.STATUS_ASSIGNED,ComplaintStatusEnum.OVERDUE.getValue()});
		Criterion finalCriterion = Restrictions.and(criterion1, criterion2);
		
		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
		sortingOrderMap.put("createdOn", SortingOrder.DESC);
		CustomList<ComplaintReportModel> customList = super.findByCriteria(finalCriterion, new ComplaintReportModel(), null, sortingOrderMap);
		if(customList!=null && customList.getResultsetList().size() > 0){
			complaintList = customList.getResultsetList();
		}
		return complaintList;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}