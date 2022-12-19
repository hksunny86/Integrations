package com.inov8.microbank.server.dao.transactionmodule.hibernate;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstantsInterface;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailDAO;

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
public class TransactionDetailHibernateDAO
    extends BaseHibernateDAO<TransactionDetailModel, Long, TransactionDetailDAO>
    implements TransactionDetailDAO
{
  public TransactionDetailHibernateDAO()
  {
  }
  
  @SuppressWarnings("unchecked")
public TransactionDetailModel loadTransactionDetailModel(String cnic, Long productId, Long supProcessingStatus){
	  TransactionDetailModel txDetailModel = null;
	  logger.info("[TransactionDetailHibernateDAO.loadTransactionDetailModel] c nic:" + cnic + " Product ID:" + productId);
	  
	  String hql = "from TransactionDetailModel txDetail, TransactionModel trx, TransactionCodeModel tcm where txDetail.relationTransactionIdTransactionModel.transactionId = trx.transactionId and trx.relationTransactionCodeIdTransactionCodeModel.transactionCodeId = tcm.transactionCodeId and txDetail.relationProductIdProductModel.productId = :productId and txDetail.customField9 = :cnic and trx.relationSupProcessingStatusIdSupplierProcessingStatusModel.supProcessingStatusId = :statusId order by trx.createdOn";
	  
	  String[] paramNames = { "productId", "cnic", "statusId" };
	  Object[] paramValues = { productId, cnic, supProcessingStatus };
	  
	  try {
			List<Object[]> resultList = getHibernateTemplate().findByNamedParam(hql, paramNames, paramValues);
			if (CollectionUtils.isNotEmpty(resultList)) {
				for (Object[] objects : resultList) {
					txDetailModel = (TransactionDetailModel)objects[0];
					txDetailModel.setTransactionIdTransactionModel((TransactionModel)objects[1]);
					txDetailModel.getTransactionIdTransactionModel().setTransactionCodeIdTransactionCodeModel((TransactionCodeModel)objects[2]);
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return txDetailModel;
	}

  public TransactionDetailModel loadAccOpeningTransactionDetailModel(Long customerId){
	  TransactionDetailModel txDetailModel = null;
	  logger.info("[TransactionDetailHibernateDAO.loadAccOpeningTransactionDetailModel] customerId:" + customerId);
	  
	  String hql = "from TransactionDetailModel txDetail, TransactionModel trx where txDetail.relationTransactionIdTransactionModel.transactionId = trx.transactionId and txDetail.relationProductIdProductModel.productId = :productId and trx.relationCustomerIdCustomerModel.customerId = :customerId and trx.relationSupProcessingStatusIdSupplierProcessingStatusModel.supProcessingStatusId = :statusId";
	  
	  String[] paramNames = { "productId", "customerId", "statusId" };
	  Object[] paramValues = { ProductConstantsInterface.ACCOUNT_OPENING, customerId, SupplierProcessingStatusConstantsInterface.COMPLETED };
	  
	  try {
			List<Object[]> resultList = getHibernateTemplate().findByNamedParam(hql, paramNames, paramValues);
			if (CollectionUtils.isNotEmpty(resultList)) {
				for (Object[] objects : resultList) {
					txDetailModel = (TransactionDetailModel)objects[0];
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return txDetailModel;
	}

	public int findPendingIdpCnicTransactions(String cnic, Long productId, Long supProcessingStatus){
		
		int count = 0;
		  logger.info("[TransactionDetailHibernateDAO.loadTransactionDetailModel] cnic:" + cnic + " Product ID:" + productId);
		  
		  String hql = "from TransactionDetailModel txDetail, TransactionModel trx where txDetail.relationTransactionIdTransactionModel.transactionId = trx.transactionId and txDetail.relationProductIdProductModel.productId = :productId and txDetail.customField9 = :cnic and txDetail.customField15 = :viaCnic and trx.relationSupProcessingStatusIdSupplierProcessingStatusModel.supProcessingStatusId = :statusId";
		  
		  String[] paramNames = { "productId", "cnic", "statusId", "viaCnic" };
		  Object[] paramValues = { productId, cnic, supProcessingStatus, "true" };
		  
		  try {
			  	
				List<Object[]> resultList = getHibernateTemplate().findByNamedParam(hql, paramNames, paramValues);
				if (CollectionUtils.isNotEmpty(resultList)) {
					count = resultList.size();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		  return count;
	  }
}
