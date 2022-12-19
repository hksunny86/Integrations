package com.inov8.microbank.server.dao.commissionmodule.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.vo.product.CommissionShSharesVO;
import com.inov8.microbank.common.vo.rowmappers.CommissionShSharesRowMapper;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CommissionShSharesModel;
import com.inov8.microbank.common.model.CommissionShSharesModel;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.server.dao.commissionmodule.CommissionShSharesDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CommissionShSharesHibernateDAO
    extends BaseHibernateDAO<CommissionShSharesModel, Long, CommissionShSharesDAO>
    implements CommissionShSharesDAO
{

	private JdbcTemplate jdbcTemplate;
	
	public boolean removeCommissionShSharesByStakeholderIds(List<Long> removeSharesList) throws FrameworkCheckedException{
		boolean deleted = false;
		
		try{
			String hql = "DELETE FROM CommissionShSharesModel model WHERE model.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId IN (" + StringUtil.getCommaSeparatedStringFromLongList(removeSharesList) + ")" ; 
			
			int updated = this.getHibernateTemplate().bulkUpdate(hql);
			deleted = updated > 0;
			
		}catch(Exception ex){
			//ex.printStackTrace();
		}
		return deleted;
	}
	
	public boolean removeCommissionShSharesByShShareIds(List<Long> removeSharesList) throws FrameworkCheckedException{
		boolean deleted = false;
		
		try{
			String hql = "DELETE FROM CommissionShSharesModel model WHERE model.commissionShSharesId IN (" + StringUtil.getCommaSeparatedStringFromLongList(removeSharesList) + ")" ; 
			
			int updated = this.getHibernateTemplate().bulkUpdate(hql);
			deleted = updated > 0;
			
		}catch(Exception ex){
			//ex.printStackTrace();
		}
		return deleted;
	}

	public List<CommissionShSharesModel>  loadCommissionShSharesList(CommissionShSharesModel vo) throws FrameworkCheckedException {
		logger.info("Starting CommissionShSharesHibernateDAO.loadCommissionShSharesList(VO) for Product :: " + vo.getProductId() + " at Time :: " + new Date());
		Long isDeleted = 0L;
		if(vo.getIsDeleted())
			isDeleted = 1L;
		String sql = "SELECT * FROM COMMISSION_SH_SHARES WHERE IS_DELETED = " + isDeleted;
		if(vo.getProductId() != null)
            sql += " AND (PRODUCT_ID IS NULL OR PRODUCT_ID = " + vo.getProductId() + " )";

		if(vo.getDeviceTypeId() != null)
            sql += " AND (DEVICE_TYPE_ID IS NULL OR DEVICE_TYPE_ID = " + vo.getDeviceTypeId() + " )";

		if(vo.getSegmentId() != null)
            sql += " AND (SEGMENT_ID IS NULL OR SEGMENT_ID = " + vo.getSegmentId() + " )";

		if(vo.getDistributorId() != null)
            sql += " AND (DISTRIBUTOR_ID IS NULL OR DISTRIBUTOR_ID = " + vo.getDistributorId() + " )";

		if(vo.getMnoId() != null && vo.getMnoId().equals(50028L))
			sql += " AND SERVICE_OP_ID = " + vo.getMnoId();
		else
			sql += " AND (SERVICE_OP_ID IS NULL OR SERVICE_OP_ID = " + vo.getMnoId() + " )";

		logger.info("Query to find Share Rule :: " + sql);
        List<CommissionShSharesModel> list = (List<CommissionShSharesModel>) jdbcTemplate.query(sql, new CommissionShSharesModel());
        int size = 0;
        if(!list.isEmpty())
            size = list.size();
        logger.info("Total " + size + " Records Fetched in CommissionShSharesHibernateDAO.loadCommissionShSharesList(VO) for Product :: " + vo.getProductId() + " at Time :: " + new Date());
        return list;
	}

	public void setDataSource(DataSource dataSource)
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
}
