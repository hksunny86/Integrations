package com.inov8.microbank.server.dao.complaintsmodule.hibernate;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ComplaintParamValueModel;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintParamValueDAO;

public class ComplaintParamHibernateDAO extends BaseHibernateDAO<ComplaintParamValueModel, Long, ComplaintParamValueDAO> implements ComplaintParamValueDAO {

	
	private final static Log logger = LogFactory.getLog(ComplaintParamHibernateDAO.class);
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	
	public Map<String, String> getComplaintParamValueMap(Long complaintId) {
		
		Map<String, String> complaintParamValueMap = new HashMap<String, String>(0); 
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT CP.PARAMETER_NAME, CPV.VALUE ");
		query.append("FROM COMPLAINT_PARAMETER CP, COMPLAINT_PARAMETER_VALUE CPV, COMPLAINT C WHERE ");
		query.append("CP.COMPLAINT_CATEGORY_ID = C.COMPLAINT_CATEGORY_ID AND ");
		query.append("CP.COMPLAINT_PARAMETER_ID = CPV.COMPLAINT_PARAMETER_ID AND ");
		query.append("CPV.COMPLAINT_ID = C.COMPLAINT_ID AND ");
		query.append("C.COMPLAINT_ID = ?");
		
	    SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(query.toString(), new Long[]{complaintId});

	    int rowCount = 0;
	    
	    while (sqlRowSet.next()) {

	    	String PARAMETER_NAME = sqlRowSet.getString("PARAMETER_NAME");
	    	String VALUE = sqlRowSet.getString("VALUE");
	    	
	    	complaintParamValueMap.put(PARAMETER_NAME, VALUE);
	    	
	    	rowCount++;
	    }		
		
		return complaintParamValueMap;
	}
	
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}