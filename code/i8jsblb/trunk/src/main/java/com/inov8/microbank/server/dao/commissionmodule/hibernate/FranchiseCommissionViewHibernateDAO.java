package com.inov8.microbank.server.dao.commissionmodule.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.FranchiseCommissionViewModel;
import com.inov8.microbank.server.dao.commissionmodule.FranchiseCommissionViewDAO;
/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Asad Hayat
 * @version 1.0
 */



public class FranchiseCommissionViewHibernateDAO 
	extends BaseHibernateDAO<FranchiseCommissionViewModel, Long, FranchiseCommissionViewDAO> implements FranchiseCommissionViewDAO {
	

	@SuppressWarnings("unchecked")
	public List<Object> findTotalUnpostedCommission(Long productId, Long commissionStakeholderId) {
		List<Object> resultList = new ArrayList<Object>();
		Double totalUnpostedCommission = 0.0;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT ");
		queryString.append(" SUM(commissionAmount) ");
		queryString.append(" FROM FranchiseCommissionViewModel model ");
		queryString.append(" WHERE ");
		queryString.append(" model.supProcessingStatusId = 1 ");
		queryString.append(" AND ");
		
//		if(productId.longValue() == 50010L){
			queryString.append(" model.supProcessingStatusId = 1 ");
			queryString.append(" AND ");
			queryString.append(" model.posted IS NULL ");
			queryString.append(" AND ");
			queryString.append(" model.settled IS NULL ");
			queryString.append(" AND ");
//		}

		queryString.append(" model.productId = " + productId);
		queryString.append(" AND ");
		queryString.append(" model.commissionStakeholderId = " + commissionStakeholderId);
		
		StringBuilder queryStringIds = new StringBuilder();
		queryStringIds.append(" SELECT model.commissionTransactionId ");
		queryStringIds.append(" FROM FranchiseCommissionViewModel model ");
		queryStringIds.append(" WHERE ");
		queryStringIds.append(" model.supProcessingStatusId = 1 ");
		queryStringIds.append(" AND ");
		
//		if(productId.longValue() == 50010L){
			queryStringIds.append(" model.posted IS NULL ");
			queryStringIds.append(" AND ");
			queryStringIds.append(" model.settled IS NULL ");
			queryStringIds.append(" AND ");
//		}
		
		queryStringIds.append(" model.productId = " + productId);
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.commissionStakeholderId = " + commissionStakeholderId);
		
		try {
			List<Double> result = getHibernateTemplate().find(queryString.toString());
			List<Long> commissionIds =  getHibernateTemplate().find(queryStringIds.toString());
			
			if( ! CollectionUtils.isEmpty(result) && null != result.get(0)){
				totalUnpostedCommission = result.get(0);
				resultList.add(totalUnpostedCommission);
			}
			
			if( ! CollectionUtils.isEmpty(commissionIds)){
				resultList.add(commissionIds);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	
	public Boolean updateCommissionTransactionPostedSettled(Long commissionTransactionModelId, Long updatedBy) {

		StringBuilder queryString = new StringBuilder();
		queryString.append(" UPDATE CommissionTransactionModel model ");
		queryString.append(" SET model.posted = ?, model.settled = ?, model.updatedOn = ? ");
		queryString.append(" WHERE ");
		queryString.append(" model.commissionTransactionId = ? ");
		Object[] values = { true, false, new Date(), commissionTransactionModelId };
		try {
			int affectedRows = getHibernateTemplate().bulkUpdate(queryString.toString(), values);
			return affectedRows > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/*public List<Object[]> findUnSettledCommissionStakeholderIds() {
		List<Object[]> resultList = new ArrayList<Object[]>();
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT model.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId, ");
		queryString.append(" SUM(commissionAmount) ");
		queryString.append(" FROM CommissionTransactionModel model ");
		queryString.append(" WHERE ");
		// queryString.append(" model.createdOn > :startDate ");
		// queryString.append(" AND ");
		// queryString.append(" model.createdOn < :endDate ");
		// queryString.append(" AND ");
		queryString.append(" model.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId IN (:stakeHolderId) AND ");
		queryString.append(" model.settled = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.posted = 1 ");
		queryString.append(" GROUP BY model.relationCommissionStakeholderIdCommissionStakeholderModel ");

//		String[] paramNames = { "startDate", "endDate" };
//
//		Object[] values = { startDate, endDate };

		Long stakeholderIds[] = {50036L, 50038L};
		String[] paramNames = { "stakeHolderId" };
		Object[] values = { stakeholderIds };

		try {
			resultList = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
			
//			for (Object[] objects : result) {
//				List<Long> transactions = findCommissionTransactions(Long.parseLong(objects[0].toString()));
//				resultList.add(transactions);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}*/
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findCommissionStakeholders() {
		List<Object[]> resultList = new ArrayList<Object[]>();
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT smartMoneyAccountId, ");
		queryString.append(" SUM(commissionAmount) ");
		queryString.append(" FROM FranchiseCommissionViewModel model ");
		queryString.append(" WHERE ");
		queryString.append(" model.smartMoneyAccountId IS NOT NULL ");
		queryString.append(" AND ");
		queryString.append(" model.settled = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.posted = 1 ");
		queryString.append(" GROUP BY model.smartMoneyAccountId ");


		try {
			
			resultList = getHibernateTemplate().find(queryString.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> findUnsettledCommissionTransactionIds() {
		List<Long> resultList = new ArrayList<Long>();
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT commissionTransactionId ");
		queryString.append(" FROM FranchiseCommissionViewModel model ");
		queryString.append(" WHERE ");
		queryString.append(" model.smartMoneyAccountId IS NOT NULL ");
		queryString.append(" AND ");
		queryString.append(" model.settled = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.posted = 1 ");


		try {
			
			resultList = getHibernateTemplate().find(queryString.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
}
