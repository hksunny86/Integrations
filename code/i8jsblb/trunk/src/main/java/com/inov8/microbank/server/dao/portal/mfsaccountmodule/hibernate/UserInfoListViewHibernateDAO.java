package com.inov8.microbank.server.dao.portal.mfsaccountmodule.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.UserInfoListViewModel;
import com.inov8.microbank.server.dao.portal.mfsaccountmodule.UserInfoListViewDAO;

public class UserInfoListViewHibernateDAO
		extends BaseHibernateDAO<UserInfoListViewModel, Long, UserInfoListViewDAO>
		implements UserInfoListViewDAO
{

	private JdbcTemplate jdbcTemplate;


	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private Boolean checkMobileNumber(PreparedStatement ps, String mobileNumber) {
		ResultSet rs = null;
		try {
			ps.setString(1, mobileNumber);
			rs = ps.executeQuery();
			if(rs != null && rs.next()){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(null != rs){
				try{
					rs.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * validateMobileNos uses JDBC to speed up the operation
	 */
	public List<BulkDisbursementsModel> validateMobileNos(List<BulkDisbursementsModel> bulkList) throws FrameworkCheckedException{
		Connection con = null;
		PreparedStatement ps = null;
		List<BulkDisbursementsModel> bulkDisbursementsModels = new ArrayList<BulkDisbursementsModel>();
		try {
			con = this.jdbcTemplate.getDataSource().getConnection();
			ps = con.prepareStatement("SELECT MOBILE_NO FROM APP_USER WHERE APP_USER_TYPE_ID=2 AND MOBILE_NO=?");

			for (BulkDisbursementsModel bulkModel : bulkList) {

				if(bulkModel.getValidRecord() && !checkMobileNumber(ps,bulkModel.getMobileNo())){
					bulkModel.setValidRecord(false);
				}

				bulkDisbursementsModels.add(bulkModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FrameworkCheckedException("ERROR: Unable to connect to DB for mobile number verification of BulkDisbursments...");
		}finally{
			if(null != ps){
				try{
					ps.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != con){
				try{
					con.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		return bulkDisbursementsModels;
	}

	public String getAreaByAppUserId(Long appUserId) throws FrameworkCheckedException{
		String area = "";
		StringBuilder hql = new StringBuilder();
		try{
			hql.append("select name from area where area_id=(select area_id from retailer_contact where retailer_contact_id=(select retailer_contact_id from app_user where app_user_id = ?))");
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet(hql.toString(), appUserId);
			while (rowSet.next()) {
				area = rowSet.getString("name");
			}
		}catch(Exception ex){
			throw new FrameworkCheckedException(ex.getMessage(),ex);
		}
		return area;
	}

}
