package com.inov8.microbank.server.dao.complaintsmodule.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ComplaintHistoryModel;
import com.inov8.microbank.common.model.portal.complaint.ComplaintHistoryVO;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintHistoryDAO;
import com.inov8.microbank.server.service.complaintmodule.ComplaintStatusEnum;

public class ComplaintHistoryHibernateDAO extends BaseHibernateDAO<ComplaintHistoryModel, Long, ComplaintHistoryDAO> implements ComplaintHistoryDAO {
	
	private final static Log logger = LogFactory.getLog(ComplaintHistoryHibernateDAO.class);
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	
	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.dao.complaintsmodule.ComplaintHistoryDAO#getComplaintHistoryModelList(com.inov8.microbank.common.model.ComplaintHistoryModel)
	 */
	public List<ComplaintHistoryModel> getComplaintHistoryModelList(ComplaintHistoryModel complaintHistoryModel) {
		
		Session session = super.getSession();
		
		Criteria criteria = session.createCriteria(ComplaintHistoryModel.class);

		Criterion criterionTatEndTime = Restrictions.le("tatEndTime", complaintHistoryModel.getTatEndTime());
		Criterion criterionStatus = Restrictions.eq("status", complaintHistoryModel.getStatus());
		criteria.addOrder(Order.asc("displayOrder"));
		
		LogicalExpression logicalExpression = Restrictions.and(criterionTatEndTime, criterionStatus);
		
		criteria.add(logicalExpression);
		
		List<ComplaintHistoryModel> list = criteria.list();

		SessionFactoryUtils.releaseSession(session, getSessionFactory());
		
		logger.info("Found "+list.size()+" Complaint History to Escalate" );
		
		return list;		
	}
	
	
	public List<ComplaintHistoryVO> getComplaintHistoryVOList(Long complaintId) {
		
		List<ComplaintHistoryVO> complaintHistoryVOList = new ArrayList<ComplaintHistoryVO>(0);
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT C.COMPLAINT_DESCRIPTION AS TITLE, CH.STATUS AS STATUS, CH.ASSIGNED_ON AS ASSIGNED_ON, CH.TAT_END_TIME AS TAT_END_TIME, CH.APP_USER_ID, AU.FIRST_NAME ||' '|| AU.LAST_NAME AS ASSIGNEE_NAME, CH.REMARKS AS REMARKS ");
		query.append("FROM COMPLAINT_HISTORY CH, COMPLAINT C, COMPLAINT_SUBCATEGORY SC, APP_USER AU ");
		query.append("WHERE ");
		query.append("SC.COMPLAINT_CATEGORY_ID = C.COMPLAINT_CATEGORY_ID ");
		query.append("AND ");
		query.append("SC.COMPLAINT_SUBCATEGORY_ID = C.COMPLAINT_SUBCATEGORY_ID ");
		query.append("AND ");
		query.append("CH.COMPLAINT_ID = C.COMPLAINT_ID ");
		query.append("AND ");
		query.append("AU.APP_USER_ID = CH.APP_USER_ID ");
		query.append("AND ");
		query.append("CH.COMPLAINT_ID = ? ");
		query.append("ORDER BY CH.DISPLAY_ORDER ASC ");

	    SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(query.toString(), complaintId);
		
		while (sqlRowSet.next()) {

			String ASSIGNEE_NAME = sqlRowSet.getString("ASSIGNEE_NAME");
			String TITLE = sqlRowSet.getString("TITLE");
			String STATUS = sqlRowSet.getString("STATUS");
			Date TAT_END_TIME = sqlRowSet.getTimestamp("TAT_END_TIME");
			Long APP_USER_ID = sqlRowSet.getLong("APP_USER_ID");
			Date ASSIGNED_ON = sqlRowSet.getTimestamp("ASSIGNED_ON");
			String REMARKS = sqlRowSet.getString("REMARKS");
			
			ComplaintHistoryVO complaintHistoryVO = new ComplaintHistoryVO();
			complaintHistoryVO.setTitle(TITLE);
			complaintHistoryVO.setStatus(STATUS);
			complaintHistoryVO.setAssignedName(ASSIGNEE_NAME);
			complaintHistoryVO.setTatEndTime(TAT_END_TIME);
			complaintHistoryVO.setAssigneeAppUserId(APP_USER_ID);
			complaintHistoryVO.setAssignedOn(ASSIGNED_ON);
			complaintHistoryVO.setRemarks(REMARKS);
			
			complaintHistoryVOList.add(complaintHistoryVO);
	    }
		
		return complaintHistoryVOList;
	}
	
	public void updateComplaintHistoryStatus(Long complaintId, String complaintStatus, String remarks) {
		String query = "UPDATE COMPLAINT_HISTORY CH SET CH.STATUS = ? , CH.REMARKS = ? , UPDATED_BY=? , UPDATED_ON=? WHERE CH.COMPLAINT_ID = ? and CH.STATUS in ('"+ComplaintStatusEnum.ASSIGNED.getValue()+"','"+ComplaintStatusEnum.OVERDUE.getValue()+"')";
		Object[] args = {complaintStatus, 
							remarks,
							UserUtils.getCurrentUser().getAppUserId(), 
							new Date(),
							complaintId
							};
		jdbcTemplate.update(query, args);
	}
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}