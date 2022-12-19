package com.inov8.microbank.server.dao.transactionmodule.hibernate;

import java.util.ArrayList;
import java.util.List;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.TransactionCodeModel;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.server.dao.transactionmodule.MiniTransactionDAO;
import org.springframework.util.CollectionUtils;

/**
 * 
 * @author Jawwad Farooq
 * July 19, 2007
 * 
 */

public class MiniTransactionHibernateDAO
extends BaseHibernateDAO<MiniTransactionModel, Long, MiniTransactionDAO>
implements MiniTransactionDAO
{
	public MiniTransactionModel LoadMiniTransactionModel(String transactionCode)throws FrameworkCheckedException{	
		
		List<MiniTransactionModel> miniTransactionModelList = getHibernateTemplate().find("from MiniTransactionModel mtm where mtm.relationTransactionCodeIdTransactionCodeModel.transactionCodeId =(select transactionCodeId from TransactionCodeModel where code ='"+transactionCode+"')");
		
		if(null!=miniTransactionModelList && !miniTransactionModelList.isEmpty()){
			return miniTransactionModelList.get(0);
		}
		else
			return null;
	}

	@Override
	public BaseWrapper loadAndLockMinitransaction(BaseWrapper baseWrapper) {

		MiniTransactionModel miniTransactionModel = (MiniTransactionModel) baseWrapper.getBasePersistableModel();
		Long PK = miniTransactionModel.getPrimaryKey();
		miniTransactionModel = this.getHibernateTemplate().get(this.getPersistentClass(), PK,LockMode.UPGRADE_NOWAIT);
		baseWrapper.setBasePersistableModel(miniTransactionModel);
		return baseWrapper;
	}

	public List<MiniTransactionModel> LoadMiniTransactionModelByPK(Long miniTransactionId)throws FrameworkCheckedException{	
		
		List<MiniTransactionModel> miniTransactionModelList = getHibernateTemplate().find("from MiniTransactionModel mtm where mtm.relationTransactionCodeIdTransactionCodeModel.transactionCodeId =(select transactionCodeId from TransactionCodeModel where transactionCodeId ='"+miniTransactionId+"')");
		
		return miniTransactionModelList;
	}

	
	public MiniTransactionModel loadAccountAndLock(MiniTransactionModel miniTransactionModel) {
		
		Long PK = miniTransactionModel.getPrimaryKey();
		MiniTransactionModel _miniTransactionModel = (MiniTransactionModel) this.getHibernateTemplate().get(miniTransactionModel.getClass(), PK,LockMode.UPGRADE_NOWAIT);
		return _miniTransactionModel;
	}

	
	public List<MiniTransactionModel> searchMiniTransactionModel(MiniTransactionModel miniTransactionModel) throws FrameworkCheckedException {

		Criterion criterionOne = Restrictions.eq("relationMiniTransactionStateIdMiniTransactionStateModel.miniTransactionStateId", miniTransactionModel.getMiniTransactionStateId());
		Criterion criterionTwo = Restrictions.eq("relationTransactionCodeIdTransactionCodeModel.transactionCodeId", miniTransactionModel.getTransactionCodeId());
			
		LogicalExpression logicalExpression = Restrictions.and(criterionOne, criterionTwo);
			
		Session session = getSession();
        Criteria criteria = session.createCriteria(MiniTransactionModel.class);
		criteria.add(logicalExpression);
					
		List<MiniTransactionModel> resultList = criteria.list();
		SessionFactoryUtils.releaseSession(session, getSessionFactory());
		logger.info(resultList.size());
		
		return resultList;
	}	
	
	@Override
	public boolean updateMiniTransactionStatus(Long transactionCodeId, Long stateId) {
		StringBuilder queryString = new StringBuilder();
		queryString.append(" UPDATE MiniTransactionModel model ");
		queryString.append(" SET model.relationMiniTransactionStateIdMiniTransactionStateModel.miniTransactionStateId = ?");
		queryString.append(" WHERE ");
		queryString.append(" model.relationTransactionCodeIdTransactionCodeModel.transactionCodeId = ? ");

		Object[] values = { stateId, transactionCodeId };

		int count = 0;
		try {
			count = getHibernateTemplate().bulkUpdate(queryString.toString(), values);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		return count > 0 ? true : false;
	}

	@Override
	public void updateMiniTransactionModel(MiniTransactionModel miniTransactionModel) {
		
		getHibernateTemplate().update(miniTransactionModel);
	}

	@Override
	public List<MiniTransactionModel> findOrphanTransactions(Long status, Long... commandId) {
		List<MiniTransactionModel> orphanTransactions = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT model ");
		queryString.append(" FROM MiniTransactionModel model");
		queryString.append(" INNER JOIN FETCH model.relationTransactionCodeIdTransactionCodeModel as codeModel");
		queryString.append(" WHERE ");
		queryString.append(" model.relationCommandIdCommandModel.commandId in (:commandId) ");
		queryString.append(" AND model.relationMiniTransactionStateIdMiniTransactionStateModel.miniTransactionStateId = :status");
		queryString.append(" ORDER BY model.timeDate");
		String[] paramNames = { "commandId", "status" };
		Object[] values = { commandId, status };

		try {
			orphanTransactions = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orphanTransactions;
	}

	@Override
	public boolean updateMiniTransactionModels(MiniTransactionModel miniTransactionModel, Long stateId) {
		StringBuilder queryString = new StringBuilder();
		queryString.append(" UPDATE MiniTransactionModel model ");
		queryString.append(" SET model.relationMiniTransactionStateIdMiniTransactionStateModel.miniTransactionStateId =:stateId ");
		queryString.append(" WHERE ");
		queryString.append(" model.relationMiniTransactionStateIdMiniTransactionStateModel.miniTransactionStateId =:oldStateId ");
		queryString.append(" and model.relationAppUserIdAppUserModel.appUserId =:appUserId ");
		queryString.append(" and model.mobileNo =:mobileNo");
		queryString.append(" and model.relationCommandIdCommandModel.commandId =:commandId");

		Query query = getSession().createQuery(queryString.toString());
		query.setParameter("stateId", stateId);
		query.setParameter("oldStateId", miniTransactionModel.getMiniTransactionStateId());
		query.setParameter("appUserId", miniTransactionModel.getAppUserId());
		query.setParameter("mobileNo",miniTransactionModel.getMobileNo());
		query.setParameter("commandId", miniTransactionModel.getCommandId());
		int count = query.executeUpdate();

		return count > 0 ? true : false;
	}

	public boolean updateMiniTransactionModelsFonepay(MiniTransactionModel miniTransactionModel, Long stateId) {
		StringBuilder queryString = new StringBuilder();
		queryString.append(" UPDATE MiniTransactionModel model ");
		queryString.append(" SET model.relationMiniTransactionStateIdMiniTransactionStateModel.miniTransactionStateId =:stateId ");
		queryString.append(" WHERE ");
		queryString.append(" model.relationMiniTransactionStateIdMiniTransactionStateModel.miniTransactionStateId =:oldStateId ");
		//queryString.append(" and model.relationAppUserIdAppUserModel.appUserId =:appUserId ");
		queryString.append(" and model.mobileNo =:mobileNo");
		queryString.append(" and model.relationCommandIdCommandModel.commandId =:commandId");

		Query query = getSession().createQuery(queryString.toString());
		query.setParameter("stateId", stateId);
		query.setParameter("oldStateId", miniTransactionModel.getMiniTransactionStateId());
		//query.setParameter("appUserId", miniTransactionModel.getAppUserId());
		query.setParameter("mobileNo",miniTransactionModel.getMobileNo());
		query.setParameter("commandId", miniTransactionModel.getCommandId());
		int count = query.executeUpdate();

		return count > 0 ? true : false;
	}

	@Override
	public MiniTransactionModel loadMiniTransactionModel(MiniTransactionModel miniTransactionModel) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("from MiniTransactionModel model ");
		queryString.append(" WHERE ");
		queryString.append(" model.relationMiniTransactionStateIdMiniTransactionStateModel.miniTransactionStateId = ? ");
		queryString.append(" and model.mobileNo = ?");
		queryString.append(" and model.relationCommandIdCommandModel.commandId = ?");

		Object[] values = { miniTransactionModel.getMiniTransactionStateId(),
				miniTransactionModel.getMobileNo(),
				miniTransactionModel.getCommandId() };

		try {
			List<MiniTransactionModel> list = getHibernateTemplate().find(queryString.toString(), values);
			if(!CollectionUtils.isEmpty(list)) {
				return list.get(0);
			}
		}

		catch (DataAccessException e) {
			logger.error(e.getMessage());
		}

		return null;
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
