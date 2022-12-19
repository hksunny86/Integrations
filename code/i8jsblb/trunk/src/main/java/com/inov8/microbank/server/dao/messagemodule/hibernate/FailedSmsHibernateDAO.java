package com.inov8.microbank.server.dao.messagemodule.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.FailedSmsModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.server.dao.messagemodule.FailedSmsDAO;
import com.sun.org.apache.xml.internal.security.utils.EncryptionConstants;

public class FailedSmsHibernateDAO extends BaseHibernateDAO<FailedSmsModel, Long, FailedSmsDAO> implements FailedSmsDAO{

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	
	public List<FailedSmsModel> getFailedSmsList(Long chunkSize){
		List<FailedSmsModel> list = new ArrayList<FailedSmsModel>();
		String query = "select failed_sms_id,mobile_number,sms_text,created_on from  "
				+ "(select failed_sms_id,mobile_number,dbcrypt.decryptusingaes(sms_text) as sms_text, created_on from failed_sms where created_on >= SYSDATE - 1 order by created_on asc) "
				+ "where ROWNUM <= ?";
		
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query,new Long[]{chunkSize});
		
		while (rowSet.next()) {
			
			list.add(new FailedSmsModel(rowSet.getLong("failed_sms_id"),
										rowSet.getString("mobile_number"),
										rowSet.getString("sms_text"),
										rowSet.getDate("created_on")));
	    }
		
		return list;
	}

	public void saveFailedSms(FailedSmsModel failedSmsModel){
		
		String query = "insert into failed_sms (failed_sms_id,mobile_number,sms_text,created_on) values "
				+ " (failed_sms_seq.nextval, ? ,dbcrypt.encryptusingaes( ? ), ?)";
		
		jdbcTemplate.update(query, new Object[]{failedSmsModel.getMobileNumber(),failedSmsModel.getSmsText(),failedSmsModel.getCreatedOn()});
		
	}

	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
