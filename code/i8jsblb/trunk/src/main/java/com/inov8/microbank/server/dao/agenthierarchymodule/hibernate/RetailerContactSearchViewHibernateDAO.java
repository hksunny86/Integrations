package com.inov8.microbank.server.dao.agenthierarchymodule.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.complaint.ComplaintHistoryVO;
import com.inov8.microbank.common.model.retailermodule.RetailerContactSearchViewModel;
import com.inov8.microbank.common.vo.RetailerContactDetailVO;
import com.inov8.microbank.server.dao.agenthierarchymodule.RetailerContactSearchViewDAO;

public class RetailerContactSearchViewHibernateDAO extends 
BaseHibernateDAO<RetailerContactSearchViewModel, Long,RetailerContactSearchViewDAO>
implements RetailerContactSearchViewDAO{
	
	private JdbcTemplate jdbcTemplate;
	
	public Boolean deleteAgent(Long retailerContactId){
		boolean retVal=false;
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("delete RetailerContactModel where retailerContactId =");
		stringBuilder.append(retailerContactId);
	    int rows=this.getHibernateTemplate().bulkUpdate(stringBuilder.toString());
	    if(rows >0){
	    	retVal=true;
	    }
	    return retVal;
	}

	@Override
	public List<RetailerContactDetailVO> findRetailerContactModelList() {
		
		List<RetailerContactDetailVO> retailerContactDetailVOList = new ArrayList<RetailerContactDetailVO>(0);
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT LATITUDE, LONGITUDE, CONTACT_NO, FIRST_NAME, LAST_NAME, FULL_ADDRESS ");
		query.append("FROM RETAILER_CONTACT_SEARCH_VIEW ");
		query.append("WHERE LATITUDE IS NOT NULL ");
		query.append("AND ");
		query.append("LONGITUDE IS NOT NULL ");

	    SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(query.toString());
		
		while (sqlRowSet.next()) {

			String LATITUDE = sqlRowSet.getString("LATITUDE");
			String LONGITUDE = sqlRowSet.getString("LONGITUDE");
			String CONTACT_NO = sqlRowSet.getString("CONTACT_NO");
			String FIRST_NAME = sqlRowSet.getString("FIRST_NAME");
			String LAST_NAME = sqlRowSet.getString("LAST_NAME");
			String ADDRESS = sqlRowSet.getString("FULL_ADDRESS");
					
			
			RetailerContactDetailVO retailerContactDetailVO = new RetailerContactDetailVO();
			retailerContactDetailVO.setLatitude(LATITUDE==null ? 0.0 : Double.valueOf(LATITUDE));
			retailerContactDetailVO.setLongitude(LONGITUDE==null ? 0.0 : Double.valueOf(LONGITUDE));
			retailerContactDetailVO.setContactNo(CONTACT_NO);
			retailerContactDetailVO.setFirstName(FIRST_NAME);
			retailerContactDetailVO.setLastName(LAST_NAME);
			retailerContactDetailVO.setAddress(ADDRESS);
			
			
			retailerContactDetailVOList.add(retailerContactDetailVO);
	    }
		
		return retailerContactDetailVOList;
	}

	public void setDataSource(DataSource dataSource)
    {
    	jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
