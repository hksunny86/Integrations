package com.inov8.microbank.server.dao.portal.manualadjustmentmodule;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.bulkdisbursements.BulkManualAdjustmentModel;
import com.inov8.microbank.common.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

//import com.inov8.framework.common.util.StringUtils;

public class BulkManualAdjustmentHibernateDAO extends BaseHibernateDAO<BulkManualAdjustmentModel, Long, BulkManualAdjustmentDAO>
implements BulkManualAdjustmentDAO{

private final static Log logger = LogFactory.getLog(BulkManualAdjustmentHibernateDAO.class);
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Boolean updateBulkManualAdjustment(Long bulkManualAdjustmentId, String transactionCode, String errorMessage, Boolean isProcessed, Boolean isApproved, String coreAccTitle, String fromToType) throws Exception{
		  StringBuilder sql = new StringBuilder("update BulkManualAdjustmentModel model set ");
		  boolean isMuitipleCols = false;
		  if (!StringUtil.isNullOrEmpty(errorMessage)){
		   sql.append(" model.errorDescription = '" + errorMessage + "'");
		   isMuitipleCols = true;
		  }
		  if (isProcessed != null){
		   if (isMuitipleCols){
		    sql.append(",");
		   }
		   sql.append(" model.isProcessed = '" + 1 + "'");
		   isMuitipleCols = true;
		  }

		  if (!StringUtil.isNullOrEmpty(transactionCode)){
			  if (isMuitipleCols){
				  sql.append(",");
			  }
			  sql.append(" model.trxnId = '" + transactionCode + "'");
			  isMuitipleCols = true;
		  }
		  
		  if (isApproved != null){
		   if (isMuitipleCols){
		    sql.append(",");
		   }
		   sql.append(" model.isApproved = '" + 1 + "'");
		  }

		if ((coreAccTitle != null) && fromToType.equals("toAcc")){
			if (isMuitipleCols){
				sql.append(",");
			}
			sql.append(" model.toAccountTitle = '" + coreAccTitle + "'");
		}
		if ((coreAccTitle != null) && fromToType.equals("fromAcc")){
			if (isMuitipleCols){
				sql.append(",");
			}
			sql.append(" model.fromAccountTitle = '" + coreAccTitle + "'");
		}


		  sql.append(" where model.bulkAdjustmentId = ? ");

		  Object[] values = { bulkManualAdjustmentId};

		  try {
		   int affectedRows = getHibernateTemplate().bulkUpdate(sql.toString(), values);
		   return affectedRows > 0 ? true : false;
		  } catch (Exception e) {
		   e.printStackTrace();
		  }

		  return false;
		 }
	
	/*public void updateIsApprovedForBatch(Long batchId,String bulkAdjustmentId []) throws Exception{
		  StringBuilder sql = new StringBuilder("update BulkManualAdjustmentModel model set model.isApproved = 1 where model.batchId = ?");
		  Object[] values = {batchId};
		  int affectedRows = getHibernateTemplate().bulkUpdate(sql.toString(), values);
		  logger.info("updateIsApprovedForBatch batchId:" + batchId + " ... affectedRows:" + affectedRows);
	}*/
	
}
