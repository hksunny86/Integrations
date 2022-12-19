package com.inov8.microbank.server.dao.complaintsmodule.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ComplaintModel;
import com.inov8.microbank.common.model.ComplaintReportModel;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintDAO;

public class ComplaintHibernateDAO extends BaseHibernateDAO<ComplaintModel, Long, ComplaintDAO> implements ComplaintDAO {
	
	private final static Log logger = LogFactory.getLog(ComplaintDAO.class);
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	
	public void updateComplaintStatus(Map<Long, String> updateStatusMap, Map<Long, String> updateEscStatusMap, List<ComplaintReportModel> complaintReportModelList) {
	
		List<String> queryList = new ArrayList<String>(0);
		
		if(!updateStatusMap.isEmpty()) {
			
			Set<Long> complaintIdCollection = updateStatusMap.keySet();
			
			for(Long complaintId : complaintIdCollection) {
				
				queryList.add("UPDATE COMPLAINT C SET C.STATUS = '"+ updateStatusMap.get(complaintId) +"' WHERE C.COMPLAINT_ID = "+complaintId.longValue());
			}
		}
		
		if(!updateEscStatusMap.isEmpty()) {
			
			Set<Long> complaintIdCollection = updateEscStatusMap.keySet();
			
			for(Long complaintId : complaintIdCollection) {
				
				queryList.add("UPDATE COMPLAINT C SET C.ESCALATION_STATUS = '"+ updateEscStatusMap.get(complaintId) +"' WHERE C.COMPLAINT_ID = "+complaintId.longValue());
			}
		}
		
		if(!complaintReportModelList.isEmpty()) {
			
			for(ComplaintReportModel complaintReportModel : complaintReportModelList) {
				
				SimpleDateFormat format = new SimpleDateFormat("yyy-MMM-dd HH:mm:ss");
				String levelTATEndTime = format.format(complaintReportModel.getLevelTATEndTime());
				
				queryList.add("UPDATE COMPLAINT_REPORT CR SET CR.ESCALATION_STATUS = '"+ complaintReportModel.getEscalationStatus() +"', "+
						"CR.STATUS = '"+ complaintReportModel.getStatus() +"', "+						
						"CR.LEVEL"+complaintReportModel.getDisplayOrder()+"_ASSIGNEE_ID = "+ complaintReportModel.getLevelAssigneeId() +", "+
						"CR.LEVEL"+complaintReportModel.getDisplayOrder()+"_ASSIGNEE_NAME = '"+ complaintReportModel.getLevelAssigneeName() +"', "+
						"CR.LEVEL"+complaintReportModel.getDisplayOrder()+"_ESC_ON =  to_date('"+levelTATEndTime+"', 'YYYY-MON-DD HH24:MI:SS') , "+
						"CURRENT_ASSIGNEE_ID = "+complaintReportModel.getCurrentAssigneeId() +", "+
						"CURRENT_ASSIGNEE_NAME = '"+ complaintReportModel.getCurrentAssigneeName()+"' "+
						"WHERE CR.COMPLAINT_ID = "+complaintReportModel.getComplaintId());
			}
		}
		
		String sql[] = queryList.toArray(new String[] {});
		
		if(!queryList.isEmpty()) {
			
			jdbcTemplate.batchUpdate(sql);
		}
	}
	
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}