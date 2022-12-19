package com.inov8.microbank.server.dao.disbursementmodule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.util.AccountCreationStatusEnum;
import com.inov8.microbank.common.util.LabelValueBean;

public class BulkDisbursementHibernateDAO extends BaseHibernateDAO<BulkDisbursementsModel, Long, BulkDisbursementDAO> implements BulkDisbursementDAO {

	@SuppressWarnings("all")
	public List<BulkDisbursementsModel> findDueDisbursement(Long productId, Integer[] types, Date end, Boolean posted, Boolean settled) {
		List<BulkDisbursementsModel> disbursements = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT model ");
		queryString.append(" FROM BulkDisbursementsModel model ");
		queryString.append(" inner join fetch model.relationCreatedByAppUserModel as createdByAppUserModel ");
		queryString.append(" inner join fetch model.relationAppUserIdAppUserModel as appUserModel ");
		queryString.append(" WHERE ");
		queryString.append(" model.relationProductIdProductModel.productId = :productId ");
		queryString.append(" AND ");
		queryString.append(" model.type in (:types) ");
		queryString.append(" AND ");
		queryString.append(" model.paymentDate <= :endDate ");
		queryString.append(" AND ");
		queryString.append(" model.posted = :posted ");
		queryString.append(" AND ");
		queryString.append(" model.settled = :settled ");
		queryString.append(" AND model.deleted = :deleted ");
		queryString.append(" AND model.accountCreationStatus = :accountCreationStatus ");

		String[] paramNames = { "productId", "types", "endDate", "posted", "settled", "deleted", "accountCreationStatus" };
		Object[] values = { productId, types, end, posted, settled, false, AccountCreationStatusEnum.SUCCESSFUL.toString() };
		try {
			disbursements = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return disbursements;
	}

	@SuppressWarnings("all")
	public List<BulkDisbursementsModel> findBulkDisbursement(Long productId, Integer type, Date end, Boolean posted, Boolean settled)
	{
		List<BulkDisbursementsModel> disbursements = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT model ");
		queryString.append(" FROM BulkDisbursementsModel model ");
		queryString.append(" inner join fetch model.relationCreatedByAppUserModel as createdByAppUserModel ");
		queryString.append(" inner join fetch model.relationUpdatedByAppUserModel as updatedByappUserModel ");
		queryString.append(" inner join fetch model.relationAppUserIdAppUserModel as appUserModel ");
		queryString.append(" inner join fetch model.relationProductIdProductModel as productModel ");
		queryString.append(" WHERE ");
		queryString.append(" model.relationProductIdProductModel.productId = :productId ");
		queryString.append(" AND ");
		queryString.append(" model.type = :type ");
		queryString.append(" AND ");
		queryString.append(" model.paymentDate <= :endDate ");
		queryString.append(" AND ");
		queryString.append(" model.posted = :posted ");
		queryString.append(" AND ");
		queryString.append(" model.settled = :settled ");
		queryString.append(" AND model.deleted = :deleted ");
		queryString.append(" AND model.accountCreationStatus = :accountCreationStatus ");

		String[] paramNames = { "productId", "type", "endDate", "posted", "settled", "deleted", "accountCreationStatus" };
		Object[] values = { productId, type, end, posted, settled, false, AccountCreationStatusEnum.SUCCESSFUL.toString() };
		try {
			disbursements = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return disbursements;
	}

	@SuppressWarnings("all")
	public Double findTotalDueDisbursement(Long productId, Integer type, Date end, Boolean posted, Boolean settled) {
		Double total = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append(" SELECT SUM(model.amount) ");
		queryString.append(" FROM BulkDisbursementsModel model ");
		queryString.append(" WHERE ");
		queryString.append(" model.relationProductIdProductModel.productId = :productId ");
		queryString.append(" AND model.type = :type ");
		queryString.append(" AND ");
		queryString.append(" model.paymentDate <= :endDate ");
		queryString.append(" AND ");
		queryString.append(" model.posted = :posted ");
		queryString.append(" AND ");
		queryString.append(" model.settled = :settled ");
		queryString.append(" AND model.deleted = :deleted ");
		queryString.append(" AND model.accountCreationStatus = :accountCreationStatus ");

		String[] paramNames = { "productId", "type", "endDate", "posted", "settled", "deleted", "accountCreationStatus" };
		Object[] values = { productId, type, end, posted, settled, false, AccountCreationStatusEnum.SUCCESSFUL.toString() };
		try {
			List<Double> list = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
			total = list.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return total;
	}

	@Override
	public void update(BulkDisbursementsModel model)
	{
		String updateHql ="update BulkDisbursementsModel model set model.relationAppUserIdAppUserModel.appUserId=?, model.accountCreationStatus=?, model.failureReason=?, model.updatedOn=? where model.bulkDisbursementsId=?";
		Object[] values = {model.getAppUserId(),model.getAccountCreationStatus(),model.getFailureReason(),model.getUpdatedOn(),model.getBulkDisbursementsId()};
		getHibernateTemplate().bulkUpdate(updateHql, values);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LabelValueBean> loadBankUsersList() throws FrameworkCheckedException {
//		List of users who uploaded or updated bulk disbursement file
	    List<LabelValueBean> bankUsersList = new ArrayList<LabelValueBean>();

		StringBuilder hql = new StringBuilder();
		hql.append("select new Map(appUser.appUserId as id, concat(appUser.firstName, ' ', appUser.lastName) as fullname) ");
		hql.append("from AppUserModel appUser where appUser.appUserId IN (");
		hql.append("select DISTINCT bulkDisbursement.relationUpdatedByAppUserModel from BulkDisbursementsModel bulkDisbursement) ");
		hql.append("or appUser.appUserId in (select DISTINCT bulkDisbursement.relationCreatedByAppUserModel from BulkDisbursementsModel bulkDisbursement) ");
		hql.append("order by appUser.firstName asc");
				
		List<Map<String, Object>> list = this.getHibernateTemplate().find(hql.toString());
		for (Map<String, Object> map : list) {
			bankUsersList.add(new LabelValueBean(map.get("fullname").toString(), map.get("id").toString()));
		}
		return bankUsersList;
	}
	
	
	
	/**
	 * @param bulkDisbursementsModelList
	 */
	public void saveOrUpdateList(List<BulkDisbursementsModel> bulkDisbursementsModelList) {
	
		logger.info("\n\n ************************* Session Started for List Size : "+bulkDisbursementsModelList.size());

		
		SessionFactory sessionFactory = this.getHibernateTemplate().getSessionFactory();

		Session session = sessionFactory.openSession();
		
		try {
				
			int i = 0;
		
			for(BulkDisbursementsModel bulkDisbursementsModel : bulkDisbursementsModelList) {
				
				session.update(bulkDisbursementsModel);
		        session.flush();
		        session.clear();
			}

		} catch(Exception e) {
			
			logger.error(e);
			
		} finally {
			session.close();
			SessionFactoryUtils.releaseSession(session, sessionFactory);
		}
	}
}