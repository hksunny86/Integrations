package com.inov8.microbank.server.dao.commissionmodule.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.util.CommissionStackHolderEnum;
import com.inov8.microbank.common.util.DonationCompanyEnum;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.TransactionProductEnum;
import com.inov8.microbank.server.dao.commissionmodule.CommissionTransactionDAO;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: inov8 Limited
 * </p>
 * 
 * @author Jawwad Farooq
 * @version 1.0
 */
@SuppressWarnings("all")
public class CommissionTransactionHibernateDAO extends BaseHibernateDAO<CommissionTransactionModel, Long, CommissionTransactionDAO> implements
		CommissionTransactionDAO {

	/*All commissions except cash to cash trx.*/
	public List<Object> findTotalUnpostedCommission() {
		List<Object> resultList = new ArrayList<Object>();
		Double totalUnpostedCommission = 0.0;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT ");
		queryString.append(" SUM(commissionAmount) ");
		queryString.append(" FROM CommissionTransactionModel model ");
		queryString.append(" WHERE ");
		queryString.append(" model.settled = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.posted = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.relationProductIdProductModel.productId <> 50011 and model.relationProductIdProductModel.productId <> 2510801 ");
		queryString.append(" AND ");
		queryString.append(" model.relationProductIdProductModel.relationServiceIdServiceModel.serviceId NOT IN (7, 9) ");
		
		StringBuilder queryStringIds = new StringBuilder();
		queryStringIds.append(" SELECT model.commissionTransactionId ");
		queryStringIds.append(" FROM CommissionTransactionModel model ");
		queryStringIds.append(" WHERE ");
		queryStringIds.append(" model.settled = 0 ");
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.posted = 0 ");
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.relationProductIdProductModel.productId <> 50011 and model.relationProductIdProductModel.productId <> 2510801 ");
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.relationProductIdProductModel.relationServiceIdServiceModel.serviceId NOT IN (7, 9) ");
		
		try {
			List<Double> result = getHibernateTemplate().find(queryString.toString());
			List<Long> commissionIds =  getHibernateTemplate().find(queryStringIds.toString());
			totalUnpostedCommission = result.get(0);
			resultList.add(totalUnpostedCommission);
			resultList.add(commissionIds);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/*Commission transactions of Cash to Cash only.*/
	public List<Object> findCashToCashTotalUnpostedCommission() {
		List<Object> resultList = new ArrayList<Object>();
		Double totalUnpostedCommission = 0.0;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT ");
		queryString.append(" SUM(commissionAmount) ");
		queryString.append(" FROM CommissionTransactionModel model ");
		queryString.append(" WHERE ");
		queryString.append(" model.settled = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.posted = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.relationProductIdProductModel.productId = 50011 ");
		
		StringBuilder queryStringIds = new StringBuilder();
		queryStringIds.append(" SELECT model.commissionTransactionId ");
		queryStringIds.append(" FROM CommissionTransactionModel model ");
		queryStringIds.append(" WHERE ");
		queryStringIds.append(" model.settled = 0 ");
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.posted = 0 ");
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.relationProductIdProductModel.productId = 50011 ");
		
		try {
			List<Double> result = getHibernateTemplate().find(queryString.toString());
			List<Long> commissionIds =  getHibernateTemplate().find(queryStringIds.toString());
			totalUnpostedCommission = result.get(0);
			resultList.add(totalUnpostedCommission);
			resultList.add(commissionIds);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/*Commission transactions of Apothecare Donation Payments.*/
	public List<Object> findDonationPaymentTotalUnpostedCommission() {
		List<Object> resultList = new ArrayList<Object>();
		Double totalUnpostedCommission = 0.0;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT ");
		queryString.append(" SUM(commissionAmount) ");
		queryString.append(" FROM CommissionTransactionModel model ");
		queryString.append(" WHERE ");
		queryString.append(" model.settled = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.posted = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.relationProductIdProductModel.productId IN (" + ProductConstantsInterface.APOTHECARE + ", " + ProductConstantsInterface.APOTHECARE_PAYMENT + ") ");
		
		StringBuilder queryStringIds = new StringBuilder();
		queryStringIds.append(" SELECT model.commissionTransactionId ");
		queryStringIds.append(" FROM CommissionTransactionModel model ");
		queryStringIds.append(" WHERE ");
		queryStringIds.append(" model.settled = 0 ");
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.posted = 0 ");
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.relationProductIdProductModel.productId IN (" + ProductConstantsInterface.APOTHECARE + ", " + ProductConstantsInterface.APOTHECARE_PAYMENT + ") ");
		
		try {
			List<Double> result = getHibernateTemplate().find(queryString.toString());
			List<Long> commissionIds =  getHibernateTemplate().find(queryStringIds.toString());
			totalUnpostedCommission = result.get(0);
			resultList.add(totalUnpostedCommission);
			resultList.add(commissionIds);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}


	/*Commission transactions of Dawat e Islami.*/
	public List<Object> findDawatEIslamiDonationPaymentTotalUnpostedCommission() {
		List<Object> resultList = new ArrayList<Object>();
		Double totalUnpostedCommission = 0.0;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT ");
		queryString.append(" SUM(commissionAmount) ");
		queryString.append(" FROM CommissionTransactionModel model ");
		queryString.append(" WHERE ");
		queryString.append(" model.settled = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.posted = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.relationProductIdProductModel.productId IN (" + ProductConstantsInterface.DAWAT_E_ISLAMI_SADQA + ", " + ProductConstantsInterface.DAWAT_E_ISLAMI_SADQA_PAYMENT + "," + ProductConstantsInterface.DAWAT_E_ISLAMI_ZAKAT + "," + ProductConstantsInterface.DAWAT_E_ISLAMI_ZAKAT_PAYMENT + ") ");
		
		StringBuilder queryStringIds = new StringBuilder();
		queryStringIds.append(" SELECT model.commissionTransactionId ");
		queryStringIds.append(" FROM CommissionTransactionModel model ");
		queryStringIds.append(" WHERE ");
		queryStringIds.append(" model.settled = 0 ");
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.posted = 0 ");
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.relationProductIdProductModel.productId IN (" + ProductConstantsInterface.DAWAT_E_ISLAMI_SADQA + ", " + ProductConstantsInterface.DAWAT_E_ISLAMI_SADQA_PAYMENT + "," + ProductConstantsInterface.DAWAT_E_ISLAMI_ZAKAT + "," + ProductConstantsInterface.DAWAT_E_ISLAMI_ZAKAT_PAYMENT + ") ");
		
		try {
			List<Double> result = getHibernateTemplate().find(queryString.toString());
			List<Long> commissionIds =  getHibernateTemplate().find(queryStringIds.toString());
			totalUnpostedCommission = result.get(0);
			resultList.add(totalUnpostedCommission);
			resultList.add(commissionIds);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/*Commission transactions of Dawat e Islami.*/
	public List<Object> findZongMLPaymentTotalUnpostedCommission() {
		List<Object> resultList = new ArrayList<Object>();
		Double totalUnpostedCommission = 0.0;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT ");
		queryString.append(" SUM(commissionAmount) ");
		queryString.append(" FROM CommissionTransactionModel model ");
		queryString.append(" WHERE ");
		queryString.append(" model.settled = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.posted = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.relationProductIdProductModel.productId IN (" + ProductConstantsInterface.ML_TRANSFER_TO_RETAILER + ", " + ProductConstantsInterface.ML_TRANSFER_TO_CUSTOMER + ") ");
		
		StringBuilder queryStringIds = new StringBuilder();
		queryStringIds.append(" SELECT model.commissionTransactionId ");
		queryStringIds.append(" FROM CommissionTransactionModel model ");
		queryStringIds.append(" WHERE ");
		queryStringIds.append(" model.settled = 0 ");
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.posted = 0 ");
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.relationProductIdProductModel.productId IN (" + ProductConstantsInterface.ML_TRANSFER_TO_RETAILER + ", " + ProductConstantsInterface.ML_TRANSFER_TO_CUSTOMER + ") ");
		
		try {
			List<Double> result = getHibernateTemplate().find(queryString.toString());
			List<Long> commissionIds =  getHibernateTemplate().find(queryStringIds.toString());
			totalUnpostedCommission = result.get(0);
			resultList.add(totalUnpostedCommission);
			resultList.add(commissionIds);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/*Commission transactions of Dawat e Islami.*/
	public List<Object> findBulkLPaymentTotalUnpostedCommission() {
		List<Object> resultList = new ArrayList<Object>();
		Double totalUnpostedCommission = 0.0;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT ");
		queryString.append(" SUM(commissionAmount) ");
		queryString.append(" FROM CommissionTransactionModel model ");
		queryString.append(" WHERE ");
		queryString.append(" model.settled = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.posted = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.relationProductIdProductModel.productId = " + ProductConstantsInterface.BULK_PAYMENT);
		
		StringBuilder queryStringIds = new StringBuilder();
		queryStringIds.append(" SELECT model.commissionTransactionId ");
		queryStringIds.append(" FROM CommissionTransactionModel model ");
		queryStringIds.append(" WHERE ");
		queryStringIds.append(" model.settled = 0 ");
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.posted = 0 ");
		queryStringIds.append(" AND ");
		queryStringIds.append(" model.relationProductIdProductModel.productId = " + ProductConstantsInterface.BULK_PAYMENT);
		
		try {
			List<Double> result = getHibernateTemplate().find(queryString.toString());
			List<Long> commissionIds =  getHibernateTemplate().find(queryStringIds.toString());
			totalUnpostedCommission = result.get(0);
			resultList.add(totalUnpostedCommission);
			resultList.add(commissionIds);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	public List<Object[]> findCommissionStakeholders() {
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
		queryString.append(" model.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId NOT IN (:stakeHolderId) AND ");
		queryString.append(" model.relationStakeholderBankIdStakeholderBankInfoModel.relationCmshaccttypeIdCommissionShAcctsTypeModel.cmshacctstypeId = 3 AND ");
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
	}

	@SuppressWarnings("unchecked")
	public List<Long> findCommissionTransactions(Long commissionStakeholderId) {

		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT model.commissionTransactionId ");
		queryString.append(" FROM CommissionTransactionModel model ");
		queryString.append(" WHERE ");
//		queryString.append(" model.createdOn > :startDate ");
//		queryString.append(" AND ");
//		queryString.append(" model.createdOn < :endDate ");
//		queryString.append(" AND ");
		queryString.append(" model.settled = 0 ");
		queryString.append(" AND ");
		queryString.append(" model.posted = 1 ");
		queryString.append(" AND ");
		queryString.append(" model.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId = :commissionStakeholderId ");

		String[] paramNames = {  "commissionStakeholderId" };

		Object[] values = {  commissionStakeholderId };

		try {
			List<Long> result = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<StakeholderBankInfoModel> findCommissionStakeholderBankAccount(Long stakeHolderId) {

		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT model ");
		queryString.append(" FROM StakeholderBankInfoModel model ");
		queryString.append(" WHERE ");
		queryString.append(" model.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId = :stakeHolderId ");
		queryString.append(" AND ");
		queryString.append(" model.relationCmshaccttypeIdCommissionShAcctsTypeModel.cmshacctstypeId = 3 ");

		String[] paramNames = { "stakeHolderId" };

		Object[] values = { stakeHolderId };

		try {
			List<StakeholderBankInfoModel> result = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<StakeholderBankInfoModel> findCommissionPoolAccount() {

		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT model ");
		queryString.append(" FROM StakeholderBankInfoModel model ");
		queryString.append(" WHERE ");
		queryString.append(" model.relationCmshaccttypeIdCommissionShAcctsTypeModel.cmshacctstypeId = 4 ");

		try {
			List<StakeholderBankInfoModel> result = getHibernateTemplate().find(queryString.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Boolean updateCommissionTransaction(Long commissionTransactionModelId, Long updatedBy) {

		StringBuilder queryString = new StringBuilder();
		queryString.append(" UPDATE CommissionTransactionModel model ");
		queryString.append(" SET model.settled = ?, model.updatedOn = ? ");
		queryString.append(" WHERE ");
		queryString.append(" model.commissionTransactionId = ? ");
		Object[] values = { true, new Date(), commissionTransactionModelId };
		try {
			int affectedRows = getHibernateTemplate().bulkUpdate(queryString.toString(), values);
			return affectedRows > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public Boolean updateCommissionTransaction(String sqlQuery, Boolean isSettled, Boolean isPosted, Object[] params) {

		logger.info(sqlQuery);
		logger.info("isSettled"+ isSettled);
		logger.info("isPosted"+ isPosted);
		logger.info("trd id params"+ params);
		
		Session hibernateSession = getHibernateTemplate().getSessionFactory().openSession();
		Query query = hibernateSession.createQuery(sqlQuery);
		query.setParameter("isSettled", isSettled);
		query.setParameter("isPosted", isPosted);
		query.setParameterList("param", params);
		
		int affectedRows = query.executeUpdate();
		
		super.releaseSession(hibernateSession);
		
		return affectedRows > 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	@SuppressWarnings("unchecked")
	public Boolean updateCommissionTransaction(Boolean isSettled, Boolean isPosted, Object[] transactionIdList, TransactionProductEnum productEnum, Integer legNumber) {
		
		Boolean executed = Boolean.FALSE;
		
		String sqlQuery = null;
		sqlQuery = "update CommissionTransactionModel CT set CT.settled = :isSettled , CT.posted = :isPosted ";
		sqlQuery += "where CT.relationTransactionDetailIdTransactionDetailModel.transactionDetailId in ";
		sqlQuery += "(select TD.transactionDetailId from TransactionDetailModel TD where TD.relationTransactionIdTransactionModel.transactionId in (:param)) ";

		if(TransactionProductEnum.CASH_DEPOSIT_PRODUCT.getProductId() == productEnum.getProductId() ||
				TransactionProductEnum.RETAIL_PAYMENT_PRODUCT.getProductId() == productEnum.getProductId() ||
				(TransactionProductEnum.CASH_WITHDRAWL_PRODUCT.getProductId() == productEnum.getProductId() && legNumber.intValue() == 2)) {
			
			String query = sqlQuery + "and CT.settled is null and CT.posted is null "
					+ " and CT.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId in ("
					+ CommissionStackHolderEnum.INOV8_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.ASKARI_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.ZONG_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.FED_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.WHT_STACKHOLDER.getCommissionStackHolderId() +") ";
			
			executed = updateCommissionTransaction(query, isSettled, isPosted, transactionIdList);
		}
		
		if(TransactionProductEnum.ACCOUNT_TO_CASH_PRODUCT.getProductId() == productEnum.getProductId() && legNumber.intValue() == 1) {
			
			String query = sqlQuery + "and CT.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId in ("
					+ CommissionStackHolderEnum.INOV8_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.ASKARI_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.ZONG_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.FED_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.WHT_STACKHOLDER.getCommissionStackHolderId() +") ";
			
			executed = updateCommissionTransaction(query, isSettled, isPosted, transactionIdList);
			
			if(executed) {
				
				query = sqlQuery + "and CT.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId in ("
						+ CommissionStackHolderEnum.AGENT1_STACKHOLDER.getCommissionStackHolderId() +", "
						+ CommissionStackHolderEnum.FRANCHISE1_STACKHOLDER.getCommissionStackHolderId() +") ";
				
				Object[] parameters = {null, null};
				
				executed = updateCommissionTransaction(query, isSettled, isPosted, parameters);
			}
		}
		
		if(TransactionProductEnum.CASH_TO_CASH_PRODUCT.getProductId() == productEnum.getProductId() && legNumber.intValue() == 1) {
			
			String query = sqlQuery + "and CT.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId in ("
					+ CommissionStackHolderEnum.INOV8_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.ASKARI_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.ZONG_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.FED_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.WHT_STACKHOLDER.getCommissionStackHolderId() +") ";
			
			executed = updateCommissionTransaction(query, isSettled, isPosted, transactionIdList);
			
			if(executed) {
				
				query = sqlQuery + "and CT.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId in ("
						+ CommissionStackHolderEnum.AGENT1_STACKHOLDER.getCommissionStackHolderId() +", "
						+ CommissionStackHolderEnum.FRANCHISE1_STACKHOLDER.getCommissionStackHolderId() +") ";
				
				Object[] parameters = {Boolean.TRUE, Boolean.FALSE};
				
				executed = updateCommissionTransaction(query, Boolean.TRUE, Boolean.FALSE, transactionIdList);
			}
			
			if(executed) {
				
				query = sqlQuery + "and CT.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId in ("
						+ CommissionStackHolderEnum.AGENT2_STACKHOLDER.getCommissionStackHolderId() +", "
						+ CommissionStackHolderEnum.FRANCHISE2_STACKHOLDER.getCommissionStackHolderId() +") ";
								
				executed = updateCommissionTransaction(query, null, null, transactionIdList);
			}
		}
		
		if(TransactionProductEnum.CASH_TO_CASH_PRODUCT.getProductId() == productEnum.getProductId() && legNumber.intValue() == 2) {
			
			String query = sqlQuery + "and CT.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId in ("
					+ CommissionStackHolderEnum.INOV8_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.ASKARI_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.ZONG_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.FED_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.WHT_STACKHOLDER.getCommissionStackHolderId() +") ";
			
			executed = updateCommissionTransaction(query, isSettled, isPosted, transactionIdList);
			
			if(executed) {
				
				query = sqlQuery + "and CT.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId in ("
						+ CommissionStackHolderEnum.AGENT1_STACKHOLDER.getCommissionStackHolderId() +", "
						+ CommissionStackHolderEnum.FRANCHISE1_STACKHOLDER.getCommissionStackHolderId() 
						+ CommissionStackHolderEnum.AGENT2_STACKHOLDER.getCommissionStackHolderId() +", "
						+ CommissionStackHolderEnum.FRANCHISE2_STACKHOLDER.getCommissionStackHolderId() +") ";
								
				executed = updateCommissionTransaction(query, Boolean.TRUE, Boolean.FALSE, transactionIdList);
			}
		}
		
		if(TransactionProductEnum.CASH_TO_CASH_PRODUCT.getProductId() == Long.parseLong(DonationCompanyEnum.APOTHECARE.getValue()) ||
				TransactionProductEnum.CASH_TO_CASH_PRODUCT.getProductId() == Long.parseLong(DonationCompanyEnum.DAWAT_E_ISLAMI_ZAKAT.getValue()) ||
					TransactionProductEnum.CASH_TO_CASH_PRODUCT.getProductId() == Long.parseLong(DonationCompanyEnum.DAWAT_E_ISLAMI_SADQA.getValue())) {
			
			String query = sqlQuery + "and CT.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId in ("
					+ CommissionStackHolderEnum.INOV8_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.ASKARI_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.ZONG_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.FED_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.WHT_STACKHOLDER.getCommissionStackHolderId() +") ";
			
			executed = updateCommissionTransaction(query, isSettled, isPosted, transactionIdList);
			
			if(executed) {
				
				query = sqlQuery + "and CT.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId in ("
						+ CommissionStackHolderEnum.AGENT1_STACKHOLDER.getCommissionStackHolderId() +", "
						+ CommissionStackHolderEnum.FRANCHISE1_STACKHOLDER.getCommissionStackHolderId() +") ";
				
				Object[] parameters = {Boolean.TRUE, Boolean.FALSE};
				
				executed = updateCommissionTransaction(query, Boolean.TRUE, Boolean.FALSE, transactionIdList);
			}
		}
		
		if(TransactionProductEnum.CASH_TO_CASH_PRODUCT.getProductId() == Long.parseLong(DonationCompanyEnum.APOTHECARE_PAYMENT.getValue()) ||
				TransactionProductEnum.CASH_TO_CASH_PRODUCT.getProductId() == Long.parseLong(DonationCompanyEnum.DAWAT_E_ISLAMI_ZAKAT_PAYMENT.getValue()) ||
					TransactionProductEnum.CASH_TO_CASH_PRODUCT.getProductId() == Long.parseLong(DonationCompanyEnum.DAWAT_E_ISLAMI_SADQA_PAYMENT.getValue())) {
			
			String query = sqlQuery + "and CT.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId in ("
					+ CommissionStackHolderEnum.INOV8_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.ASKARI_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.ZONG_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.FED_STACKHOLDER.getCommissionStackHolderId() +", "
					+ CommissionStackHolderEnum.WHT_STACKHOLDER.getCommissionStackHolderId() +") ";
			
			executed = updateCommissionTransaction(query, isSettled, isPosted, transactionIdList);
		}
		
		
		if(ProductConstantsInterface.ZONG_TOPUP.longValue() == productEnum.getProductId()) {

			logger.info("No Entry For ZONG_TOPUP");
		}
		
		return executed;
	}

	public Boolean updateCommissionTransactionPosted(Long commissionTransactionModelId, Long updatedBy) {

		StringBuilder queryString = new StringBuilder();
		queryString.append(" UPDATE CommissionTransactionModel model ");
		queryString.append(" SET model.posted = ?, model.updatedOn = ? ");
		queryString.append(" WHERE ");
		queryString.append(" model.commissionTransactionId = ? ");
		Object[] values = { true, new Date(), commissionTransactionModelId };
		try {
			int affectedRows = getHibernateTemplate().bulkUpdate(queryString.toString(), values);
			return affectedRows > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public Boolean removeCommissionTransactionModel(CommissionTransactionModel commissionTxModel) {
		
		try{
			
			getHibernateTemplate().delete(commissionTxModel);
			return true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}

		return false;
		
	}
	
	public void updateCommissionTransactionSettled(Long transactionDetailId) {

		StringBuilder queryString = new StringBuilder();
		queryString.append(" UPDATE CommissionTransactionModel model ");
		queryString.append(" SET model.settled = ?, model.updatedOn = ? ");
		queryString.append(" WHERE ");
		queryString.append(" model.relationTransactionDetailIdTransactionDetailModel.transactionDetailId = ?");
		queryString.append(" and model.settled = ?");
		Object[] values = { true, new Date(), transactionDetailId, false };
		
		getHibernateTemplate().bulkUpdate(queryString.toString(), values);
		
	}


}
