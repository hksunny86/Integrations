package com.inov8.microbank.server.dao.portal.partnergroupmodule.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.UserPermissionModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerPermissionViewModel;
import com.inov8.microbank.common.util.PermissionGroupConstants;
import com.inov8.microbank.server.dao.portal.partnergroupmodule.PartnerPermissionViewDAO;

public class PartnerPermissionViewHibernateDAO extends BaseHibernateDAO<PartnerPermissionViewModel, Long, PartnerPermissionViewDAO>
implements PartnerPermissionViewDAO{

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public List<UserPermissionModel> getFranchisePermissions() throws FrameworkCheckedException {
		
		List<UserPermissionModel> userPermissionList = new ArrayList<UserPermissionModel>(0);
		String query = "select * from user_permission where user_permission_id in (select user_permission_id from permission_group_detail where permission_group_id=?)";
	    try{
			SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(query, new Long[]{PermissionGroupConstants.RETAILOR});
			while (sqlRowSet.next()) {
				UserPermissionModel model = new UserPermissionModel();
				model.setUserPermissionId(sqlRowSet.getLong("USER_PERMISSION_ID"));
				model.setName(sqlRowSet.getString("NAME"));
				model.setDescription(sqlRowSet.getString("DESCRIPTION"));
				model.setComments(sqlRowSet.getString("COMMENTS"));
				model.setShortName(sqlRowSet.getString("SHORT_NAME"));
	
				model.setReadAvailable(sqlRowSet.getBoolean("IS_READ_AVAILABLE"));
				model.setUpdateAvailable(sqlRowSet.getBoolean("IS_UPDATE_AVAILABLE"));
				model.setDeleteAvailable(sqlRowSet.getBoolean("IS_DELETE_AVAILABLE"));
				model.setCreateAvailable(sqlRowSet.getBoolean("IS_CREATE_AVAILABLE"));
				model.setIsDefault(sqlRowSet.getBoolean("IS_DEFAULT"));
				
				userPermissionList.add(model);
		    }
		}catch(Exception ex){
			throw new FrameworkCheckedException(ex.getMessage(),ex);
		}
		
		return userPermissionList;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
