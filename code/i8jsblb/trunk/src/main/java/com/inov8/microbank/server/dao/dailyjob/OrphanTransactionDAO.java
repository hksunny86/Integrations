package com.inov8.microbank.server.dao.dailyjob;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ServiceConstantsInterface;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.server.dao.disbursementmodule.BulkDisbursementDAO;

@SuppressWarnings("all")
public class OrphanTransactionDAO extends BaseHibernateDAO<BulkDisbursementsModel, Long, BulkDisbursementDAO> {
	
	public List<TransactionModel> findOrphanTransactions(Long productId, Date endDate, Long status) {
		List<TransactionModel> orphanTransactions = null;
		
		boolean isAccToCash = false;
		if(ProductConstantsInterface.ACCOUNT_TO_CASH.longValue() == productId){
			isAccToCash = true;
		}
		
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT model ");
		queryString.append(" FROM TransactionModel model");
		queryString.append(" INNER JOIN FETCH model.transactionIdTransactionDetailModelList as txdetails");
		queryString.append(" INNER JOIN FETCH model.relationTransactionCodeIdTransactionCodeModel as codeModel");
		queryString.append(" WHERE ");
		queryString.append(" model.createdOn <= :endDate");
		queryString.append(" AND (txdetails.relationProductIdProductModel.productId = :productId ");
		if(isAccToCash){
			queryString.append(" OR txdetails.relationProductIdProductModel.productId = :productId2) ");
		}else{
			queryString.append(")");
		}
		queryString.append(" AND model.relationSupProcessingStatusIdSupplierProcessingStatusModel.supProcessingStatusId = :status");
		queryString.append(" ORDER BY model.createdOn");
		try {
			if(isAccToCash){
				String[] paramNames = { "endDate", "productId","productId2", "status" };
				Object[] values = { endDate, productId,ProductConstantsInterface.ACT_TO_CASH_CI, status };
				orphanTransactions = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
				
			}else{
				String[] paramNames = { "endDate", "productId", "status" };
				Object[] values = { endDate, productId, status };
				
				orphanTransactions = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return orphanTransactions;
	}

	public List<TransactionModel> findOrphanTransactionsForBulkPayment(Date endDate, Long status) {

		List<TransactionModel> orphanTransactions = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT model ");
		queryString.append(" FROM TransactionModel model , TransactionDetailMasterModel tdm");
		queryString.append(" WHERE ");
		queryString.append(" model.transactionId = tdm.transactionId");
		queryString.append(" AND tdm.serviceId = :serviceId");
		queryString.append(" AND tdm.createdOn <= :endDate");
		queryString.append(" AND tdm.supProcessingStatusId = :status");
		queryString.append(" ORDER BY model.createdOn");
		
		String[] paramNames = { "serviceId" , "endDate", "status" };
		Object[] values = { ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER, endDate, status };

		try {
			orphanTransactions = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orphanTransactions;
	}

	public Double findAgent2Commission(Long txDetailId, Long agentId) {
		Double agentCommision = 0D;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT model.commissionAmount ");
		queryString.append(" FROM CommissionTransactionModel model");
		queryString.append(" WHERE ");
		queryString.append(" model.relationStakeholderBankIdStakeholderBankInfoModel.stakeholderBankInfoId = :agentId");
		queryString.append(" AND ");
		queryString.append(" model.relationTransactionDetailIdTransactionDetailModel.transactionDetailId = :txDetailId");

		String[] paramNames = { "txDetailId", "agentId" };
		Object[] values = { txDetailId, agentId };

		try {
			List<Double> list = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
			if (list != null && list.size() > 0)
				agentCommision = list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agentCommision;
	}

	public boolean reverseTransaction(Long transactionId, Long status, Long updatedBy, Date updatedOn) {
		StringBuilder queryString = new StringBuilder();
		queryString.append(" UPDATE TransactionModel model ");
		queryString.append(" SET model.relationSupProcessingStatusIdSupplierProcessingStatusModel.supProcessingStatusId = ?,");
		queryString.append(" model.relationUpdatedByAppUserModel.appUserId = ?,");
		queryString.append(" model.updatedOn = ?");
		queryString.append(" WHERE ");
		queryString.append(" model.transactionId = ? ");

		Object[] values = { status, updatedBy, updatedOn, transactionId };

		int count = getHibernateTemplate().bulkUpdate(queryString.toString(), values);

		return count > 0 ? true : false;
	}

	public boolean expireMiniTransaction(Long transactionCodeId) {
		StringBuilder queryString = new StringBuilder();
		queryString.append(" UPDATE MiniTransactionModel model ");
		queryString.append(" SET model.relationMiniTransactionStateIdMiniTransactionStateModel.miniTransactionStateId = ?");
		queryString.append(" WHERE ");
		queryString.append(" model.relationTransactionCodeIdTransactionCodeModel.transactionCodeId = ? ");

		Object[] values = { MiniTransactionStateConstant.EXPIRED, transactionCodeId };

		int count = 0;
		try {
			count = getHibernateTemplate().bulkUpdate(queryString.toString(), values);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		return count > 0 ? true : false;
	}

}
