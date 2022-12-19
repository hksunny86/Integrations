package com.inov8.microbank.server.dao.complaintsmodule.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ComplaintSubcategoryModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryDAO;

public class ComplaintSubcategoryHibernateDAO extends BaseHibernateDAO<ComplaintSubcategoryModel, Long, ComplaintSubcategoryDAO> implements ComplaintSubcategoryDAO {
	
	private final static Log logger = LogFactory.getLog(ComplaintSubcategoryHibernateDAO.class);
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	
	public Map getComplaintSubcategoryByComplaintId(Long complaintId) {
		
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT CS.LEVEL0_ASSIGNEE, CS.LEVEL1_ASSIGNEE, CS.LEVEL2_ASSIGNEE, CS.LEVEL3_ASSIGNEE, ");
		hql.append("CS.LEVEL0_ASSIGNEE_TAT, CS.LEVEL1_ASSIGNEE_TAT, CS.LEVEL2_ASSIGNEE_TAT, CS.LEVEL3_ASSIGNEE_TAT, C.EXPECTED_TAT AS COMPALINT_EXPECTED_TAT_DATE, ");
		hql.append("C.COMPLAINT_DESCRIPTION AS COMPLAINT_DESC, C.COMPLAINT_CODE AS COMPLAINT_CODE, CS.NAME AS CATEGORY, ");
		hql.append("(SELECT FIRST_NAME ||' '|| LAST_NAME AS NAME FROM APP_USER where APP_USER_ID = CS.LEVEL0_ASSIGNEE) AS LEVEL0_ASSIGNEE_NAME, ");
		hql.append("(SELECT FIRST_NAME ||' '|| LAST_NAME AS NAME FROM APP_USER where APP_USER_ID = CS.LEVEL1_ASSIGNEE) AS LEVEL1_ASSIGNEE_NAME, ");
		hql.append("(SELECT FIRST_NAME ||' '|| LAST_NAME AS NAME FROM APP_USER WHERE APP_USER_ID = CS.LEVEL2_ASSIGNEE) AS LEVEL2_ASSIGNEE_NAME, ");
		hql.append("(SELECT FIRST_NAME ||' '|| LAST_NAME AS NAME FROM APP_USER WHERE APP_USER_ID = CS.LEVEL3_ASSIGNEE) AS LEVEL3_ASSIGNEE_NAME ");
		hql.append("FROM COMPLAINT_SUBCATEGORY CS, COMPLAINT C ");
		hql.append("WHERE CS.COMPLAINT_SUBCATEGORY_ID = C.COMPLAINT_SUBCATEGORY_ID AND ");
		hql.append("C.COMPLAINT_ID = ?");
		
		return jdbcTemplate.queryForMap(hql.toString(), new Long[]{complaintId});
	}	
	
	public List<LabelValueBean> loadAssigneeList() {
	    List<LabelValueBean> assigneeList = new ArrayList<LabelValueBean>();

		StringBuilder hql = new StringBuilder();
		hql.append("select app_user_id as id, first_name || ' ' || last_name as name ");
		hql.append("from app_user where app_user_id IN (");
		hql.append("select DISTINCT level0_assignee from complaint_subcategory ");
		hql.append("union select DISTINCT level1_assignee from complaint_subcategory ");
		hql.append("union select DISTINCT level2_assignee from complaint_subcategory ");
		hql.append("union select DISTINCT level3_assignee from complaint_subcategory) order by name asc");
				
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(hql.toString());
		while (rowSet.next()) {
			assigneeList.add(new LabelValueBean(rowSet.getString("name"),rowSet.getString("id")));
	    }
		return assigneeList;
	}	
	
	@SuppressWarnings("unchecked")
	public List<LabelValueBean> loadl0AssigneeList() {
	    List<LabelValueBean> assigneeList = new ArrayList<LabelValueBean>();

		StringBuilder hql = new StringBuilder();
		hql.append("select new Map(appUser.appUserId as id, concat(appUser.firstName, ' ', appUser.lastName) as fullname) ");
		hql.append("from AppUserModel appUser where appUser.appUserId IN (");
		hql.append("select DISTINCT complaintSubcategory.level0Assignee from ComplaintSubcategoryModel complaintSubcategory) ");
		hql.append("order by appUser.firstName asc");
				
		List<Map<String, Object>> list = this.getHibernateTemplate().find(hql.toString());
		for (Map<String, Object> map : list) {
			assigneeList.add(new LabelValueBean(map.get("fullname").toString(), map.get("id").toString()));
		}
		return assigneeList;
	}
	
	@SuppressWarnings("unchecked")
	public List<LabelValueBean> loadl1AssigneeList() {
	    List<LabelValueBean> assigneeList = new ArrayList<LabelValueBean>();

		StringBuilder hql = new StringBuilder();
		hql.append("select new Map(appUser.appUserId as id, concat(appUser.firstName, ' ', appUser.lastName) as fullname) ");
		hql.append("from AppUserModel appUser where appUser.appUserId IN (");
		hql.append("select DISTINCT complaintSubcategory.level1Assignee from ComplaintSubcategoryModel complaintSubcategory) ");
		hql.append("order by appUser.firstName asc");
				
		List<Map<String, Object>> list = this.getHibernateTemplate().find(hql.toString());
		for (Map<String, Object> map : list) {
			assigneeList.add(new LabelValueBean(map.get("fullname").toString(), map.get("id").toString()));
		}
		return assigneeList;
	}
	
	@SuppressWarnings("unchecked")
	public List<LabelValueBean> loadl2AssigneeList() {
	    List<LabelValueBean> assigneeList = new ArrayList<LabelValueBean>();

		StringBuilder hql = new StringBuilder();
		hql.append("select new Map(appUser.appUserId as id, concat(appUser.firstName, ' ', appUser.lastName) as fullname) ");
		hql.append("from AppUserModel appUser where appUser.appUserId IN (");
		hql.append("select DISTINCT complaintSubcategory.level2Assignee from ComplaintSubcategoryModel complaintSubcategory) ");
		hql.append("order by appUser.firstName asc");
				
		List<Map<String, Object>> list = this.getHibernateTemplate().find(hql.toString());
		for (Map<String, Object> map : list) {
			assigneeList.add(new LabelValueBean(map.get("fullname").toString(), map.get("id").toString()));
		}
		return assigneeList;
	}
	
	@SuppressWarnings("unchecked")
	public List<LabelValueBean> loadl3AssigneeList() {
	    List<LabelValueBean> assigneeList = new ArrayList<LabelValueBean>();

		StringBuilder hql = new StringBuilder();
		hql.append("select new Map(appUser.appUserId as id, concat(appUser.firstName, ' ', appUser.lastName) as fullname) ");
		hql.append("from AppUserModel appUser where appUser.appUserId IN (");
		hql.append("select DISTINCT complaintSubcategory.level3Assignee from ComplaintSubcategoryModel complaintSubcategory) ");
		hql.append("order by appUser.firstName asc");
				
		List<Map<String, Object>> list = this.getHibernateTemplate().find(hql.toString());
		for (Map<String, Object> map : list) {
			assigneeList.add(new LabelValueBean(map.get("fullname").toString(), map.get("id").toString()));
		}
		return assigneeList;
	}
	
	public String getAssigneeName(Long appUserId) throws FrameworkCheckedException{
	    String name = "";
		StringBuilder hql = new StringBuilder();
		try{
			hql.append("select  first_name || ' ' || last_name as name from app_user where app_user_id = ?");
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet(hql.toString(), new Long[]{appUserId});
			while (rowSet.next()) {
				name = rowSet.getString("name");
		    }
		}catch(Exception ex){
			throw new FrameworkCheckedException(ex.getMessage(),ex);
		}
		
		return name;
	}

	@SuppressWarnings("unchecked")
	public List<LabelValueBean> loadBankUsers() throws FrameworkCheckedException{
	    List<LabelValueBean> bankUserList = new ArrayList<LabelValueBean>();
		StringBuilder hql = new StringBuilder();
		try{
			hql.append("select new Map(appUser.appUserId as id, concat(appUser.firstName, ' ', appUser.lastName) as fullname)  from AppUserModel appUser ");
			hql.append("where appUser.relationAppUserTypeIdAppUserTypeModel.appUserTypeId = 6 order by appUser.firstName asc");
					
			List<Map<String, Object>> list = this.getHibernateTemplate().find(hql.toString());
			for (Map<String, Object> map : list) {
				bankUserList.add(new LabelValueBean(map.get("fullname").toString(), map.get("id").toString()));
			}
		}catch(Exception ex){
			throw new FrameworkCheckedException(ex.getMessage(),ex);
		}
		return bankUserList;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}