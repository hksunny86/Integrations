package com.inov8.microbank.server.dao.transactionmodule.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.TransactionProductEnum;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDAO;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class TransactionHibernateDAO extends BaseHibernateDAO<TransactionModel, Long, TransactionDAO> implements TransactionDAO {
	
	@SuppressWarnings("unchecked")
	public List<Object> getTransactionsByCriteria(Long productId, Long status, Date date){ 
		
		List<Object> resultList = new ArrayList<Object>();
		
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT ");
		queryString.append(" SUM(model.transactionAmount) ");
		queryString.append(" FROM TransactionModel model, TransactionDetailModel detail ");
		queryString.append(" WHERE ");
		queryString.append(" detail.relationTransactionIdTransactionModel.transactionId = model.transactionId ");
		queryString.append(" AND ");
		queryString.append(" detail.relationProductIdProductModel.productId = ? ");
		//queryString.append(" AND ");
		//queryString.append(" trunc(model.createdOn) = trunc(?) ");
		queryString.append(" AND ");
		queryString.append(" model.relationSupProcessingStatusIdSupplierProcessingStatusModel.supProcessingStatusId =  ? ");

		StringBuilder queryString2 = new StringBuilder();
		queryString2.append(" SELECT ");
		queryString2.append(" model ");
		queryString2.append(" FROM TransactionModel model, TransactionDetailModel detail ");
		queryString2.append(" WHERE ");
		queryString2.append(" detail.relationTransactionIdTransactionModel.transactionId = model.transactionId ");
		queryString2.append(" AND ");
		queryString2.append(" detail.relationProductIdProductModel.productId = ? ");
		//queryString2.append(" AND ");
		//queryString2.append(" trunc(model.createdOn) = trunc(?) ");
		queryString2.append(" AND ");
		queryString2.append(" model.relationSupProcessingStatusIdSupplierProcessingStatusModel.supProcessingStatusId =  ? ");

//		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
//		String dateString = format.format(date);
//		try {
//			date = format.parse(dateString);
//		} catch (ParseException e1) {
//			e1.printStackTrace();
//		}
		
		Object[] params = {productId, status};

		try {
			
			List<Double> transactionAmountList = getHibernateTemplate().find(queryString.toString(), params);
			List<TransactionModel> transactionsList = getHibernateTemplate().find(queryString2.toString(), params);
			
			resultList.add(0, transactionAmountList.get(0));
			resultList.add(1, transactionsList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object> getTransactionsByDate(Long productId, Long status, Date date){ 
		
		List<Object> resultList = new ArrayList<Object>();
		
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT ");
		queryString.append(" trxModel, codeModel ");
		queryString.append(" FROM TransactionModel trxModel, TransactionCodeModel codeModel, TransactionDetailModel detail ");
		queryString.append(" WHERE ");
		queryString.append(" detail.relationProductIdProductModel.productId = ? ");
		queryString.append(" AND ");
		queryString.append(" trxModel.transactionId = detail.relationTransactionIdTransactionModel.transactionId ");
		queryString.append(" AND ");
		queryString.append(" codeModel.transactionCodeId = trxModel.relationTransactionCodeIdTransactionCodeModel.transactionCodeId ");
		queryString.append(" AND ");
		queryString.append(" trunc(trxModel.createdOn) = trunc(?) "); 
		queryString.append(" AND ");
		queryString.append(" trxModel.relationSupProcessingStatusIdSupplierProcessingStatusModel.supProcessingStatusId =  ? ");

		Object[] params = {productId, date, status};

		try {
			
			resultList = getHibernateTemplate().find(queryString.toString(), params);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	
	public List<Object[]> getDonationTransactionList(Long transactionTypeId, Long supProcessingStatusId, Long serviceId) throws FrameworkCheckedException {
		
		StringBuilder queryString = new StringBuilder();
		
		queryString.append("SELECT transactionModel, productModel ");
		queryString.append("from TransactionModel transactionModel, ");
		queryString.append("ProductModel productModel, TransactionDetailModel transactionDetailModel ");
		queryString.append("where ");
		queryString.append("transactionModel.relationTransactionTypeIdTransactionTypeModel.transactionTypeId = ? ");
		queryString.append("and ");
		queryString.append("productModel.productId = transactionDetailModel.relationProductIdProductModel.productId ");
		queryString.append("and ");
		queryString.append("transactionModel.relationSupProcessingStatusIdSupplierProcessingStatusModel.supProcessingStatusId = ? ");
		queryString.append("and ");
		queryString.append("productModel.relationServiceIdServiceModel.serviceId = ?");		
		queryString.append("and ");
		queryString.append("transactionModel.transactionId = transactionDetailModel.relationTransactionIdTransactionModel.transactionId ");
		queryString.append("order by productModel.relationSupplierIdSupplierModel.supplierId asc ");
		
		logger.info(queryString.toString());

		Object[] params = {transactionTypeId, supProcessingStatusId, serviceId};

		List<Object[]> resultList = new ArrayList<Object[]>();
		
		try {
			
			resultList = getHibernateTemplate().find(queryString.toString(), params);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return resultList;
	}
	
	
	/**
	 * @param transactionProcessingStatus
	 * @param transactionId
	 * @return
	 */
	public Integer updateTransactionProcessingStatus(Long transactionProcessingStatus, List<Long> transactionId) {
	
		boolean failureFlag = false;
		
		if(SupplierProcessingStatusConstants.FAILED.longValue() == transactionProcessingStatus 
				|| SupplierProcessingStatusConstants.IVR_VALIDATION_ABORTED.longValue() == transactionProcessingStatus){
			
			failureFlag = true;
		}
		
		StringBuilder query = new StringBuilder();
		query.append("update TransactionModel set relationSupProcessingStatusIdSupplierProcessingStatusModel.supProcessingStatusId = :transactionProcessingStatus ");
		query.append("where transactionId in (:transactionId) ");
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query sessionQuery = session.createQuery(query.toString());
		sessionQuery.setParameter("transactionProcessingStatus", transactionProcessingStatus);
		sessionQuery.setParameterList("transactionId", transactionId);
		
		int updateCount = sessionQuery.executeUpdate();
		
		
		StringBuilder queryTDM = new StringBuilder();
		queryTDM.append("update TransactionDetailMasterModel set supProcessingStatusId = :transactionProcessingStatus, "
				+ " processingStatusName = :transactionProcessingStatusName ");
		
		if(failureFlag){
			queryTDM.append(", failureReasonId = :failureReasonID, failureReason = :failureReason ");
		}
		
		queryTDM.append("where transactionId in (:transactionId) ");

		String transactionProcessingStatusName = SupplierProcessingStatusConstants.processingStatusNamesMap.get(transactionProcessingStatus);
		Query sessionQueryTDM = session.createQuery(queryTDM.toString());
		sessionQueryTDM.setParameter("transactionProcessingStatus", transactionProcessingStatus);
		sessionQueryTDM.setParameter("transactionProcessingStatusName", transactionProcessingStatusName);
		if(failureFlag){
			sessionQueryTDM.setParameter("failureReasonID", Long.valueOf(WorkFlowErrorCodeConstants.IVR_AUTHORIZATION_FAILED));
			sessionQueryTDM.setParameter("failureReason", MessageUtil.getMessage(WorkFlowErrorCodeConstants.IVR_AUTHORIZATION_FAILED.toString()));
		}
		
		sessionQueryTDM.setParameterList("transactionId", transactionId);
		sessionQueryTDM.executeUpdate();
		
		SessionFactoryUtils.releaseSession(session, getSessionFactory());

		return updateCount;
	}

	/**
	 * @param productId
	 * @param isSettled
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getTransactionsByCriteria(Long distributorId, Long productId, Boolean isSettled, Boolean isPosted) { 
		
		Object[] params = null;
		String commissionTransactionQuery = null;
		
		if(TransactionProductEnum.CASH_DEPOSIT_PRODUCT.getProductId() == productId.longValue() ||
				TransactionProductEnum.CASH_WITHDRAWL_PRODUCT.getProductId() == productId.longValue()) {

			commissionTransactionQuery = " (select sum(CT.commissionAmount) as sum_commission from CommissionTransactionModel CT where CT.settled = ? and  CT.posted = ? and TD.transactionDetailId = CT.relationTransactionDetailIdTransactionDetailModel.transactionDetailId) as sum_commission ";
			
			Object[] queryParams = {isSettled, isPosted, distributorId, productId};
			params = queryParams;
		}	
		
		if(TransactionProductEnum.ACCOUNT_TO_CASH_PRODUCT.getProductId() == productId.longValue()) {

			commissionTransactionQuery = " (select sum(CT.commissionAmount) as sum_commission from CommissionTransactionModel CT where CT.settled is null and  CT.posted is null and TD.transactionDetailId = CT.relationTransactionDetailIdTransactionDetailModel.transactionDetailId) as sum_commission ";

			Object[] queryParams = {distributorId, productId};
			params = queryParams;
		}	
		
		
		StringBuilder queryString = new StringBuilder();
		queryString.append(" select T, ");
		queryString.append(commissionTransactionQuery);		
		queryString.append(" , TD.transactionDetailId ");
		queryString.append(" from TransactionModel T, TransactionDetailModel TD ");
		queryString.append(" where ");
		queryString.append(" T.transactionId = TD.relationTransactionIdTransactionModel.transactionId ");
		queryString.append(" and ");
		queryString.append(" T.relationDistributorIdDistributorModel.distributorId = ? ");
		queryString.append(" and ");		
		queryString.append(" TD.relationProductIdProductModel.productId = ? ");
//		queryString.append(" and TD.transactionDetailId in (68195, 68199, 68175, 68269, 68133) ");
		queryString.append(" order by ");
		queryString.append(" T.relationDistributorIdDistributorModel.distributorId asc");
		
		
		List list = getHibernateTemplate().find(queryString.toString(), params);
		
		return list;
	}

	@SuppressWarnings( "unchecked" )
    @Override
	public TransactionModel loadTxAndTxDetailModel( Long transactionId ) throws FrameworkCheckedException
	{
	    TransactionModel transactionModel = null;
	    String hql = "SELECT transactionModel FROM TransactionModel transactionModel INNER JOIN FETCH transactionModel.transactionIdTransactionDetailModelList WHERE transactionModel.transactionId=?";
	    List<TransactionModel> transactionModelList = getHibernateTemplate().find( hql, transactionId );
	    if( transactionModelList != null && !transactionModelList.isEmpty() )
	    {
	        transactionModel = transactionModelList.get( 0 );
	    }
	    return transactionModel;
	}

	@Override
	@SuppressWarnings("unchecked")
	public MiniTransactionModel loadMiniTransactionByTransactionCode(String transactionCode) {
		MiniTransactionModel miniTransactionModel = null;

		String hql = "from MiniTransactionModel mtrx, TransactionCodeModel tcm "
				+ "where mtrx.relationTransactionCodeIdTransactionCodeModel.transactionCodeId = tcm.transactionCodeId "
				+ "and tcm.code = :transactionCode";

		String[] paramNames = {"transactionCode"};
		Object[] paramValues = {transactionCode};

		try {
			List<Object[]> resultList = getHibernateTemplate().findByNamedParam(hql, paramNames, paramValues);
			for (Object[] objects : resultList) {
				miniTransactionModel = (MiniTransactionModel) objects[0];

				if(miniTransactionModel != null) {
					miniTransactionModel.setTransactionCodeIdTransactionCodeModel((TransactionCodeModel)objects[1]);
					break;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

		return miniTransactionModel;
	}

}
