package com.inov8.microbank.disbursement.dao.hibernate;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.util.AccountCreationStatusEnum;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.ServiceConstantsInterface;
import com.inov8.microbank.disbursement.dao.BulkDisbursementDAO;
import com.inov8.microbank.disbursement.dao.DisbursementDAO;
import com.inov8.microbank.disbursement.util.DisbursementStatusConstants;
import com.inov8.microbank.disbursement.vo.BulkDisbursementsXmlVo;
import com.inov8.microbank.disbursement.vo.DisbursementVO;

public class BulkDisbursementHibernateDAO extends BaseHibernateDAO<BulkDisbursementsModel, Long, BulkDisbursementDAO> implements BulkDisbursementDAO {

	public List<DisbursementVO> findDueDisbursement(Long serviceId, Date end, Boolean isCoreSumAccountNumber, Boolean posted, Boolean settled) {

		StringBuilder queryString = new StringBuilder();
		queryString.append(" select bd.bulkDisbursementsId as disbursementId, bd.name as name, bd.mobileNo as mobileNo, bd.cnic as cnic, bd.amount as amount, bd.charges as charges,");
		queryString.append(" bd.fed as fed, bd.paymentDate as paymentDate, bd.settled as settled, bd.posted as posted, bd.batchNumber as batchNumber,");
		queryString.append(" bd.limitApplicable as limitApplicable, bd.payCashViaCnic as payCashViaCnic, bd.description as description,");
		queryString.append(" fi.sourceAccountNumber as sourceACNo, fi.fileInfoId as disbursementsFileInfoId, fi.isCoreSumAccountNumber as isCoreSumAccountNumber,");
		queryString.append(" aut.appUserTypeId as appUserTypeId, ");
		queryString.append(" p.productId as productId, p.name as productName, s.serviceId as serviceId, s.name as serviceName");

		queryString.append(" from BulkDisbursementsModel bd, BulkDisbursementsFileInfoModel fi, ProductModel p, ServiceModel s, AppUserTypeModel aut ");

		queryString.append(" where bd.relationFileInfoIdBulkDisbursementsFileInfoModel.fileInfoId = fi.fileInfoId");
		queryString.append(" and bd.relationProductIdProductModel = p.productId");
		queryString.append(" and bd.serviceId = s.serviceId" );
		queryString.append(" and s.serviceId = :serviceId" );
		queryString.append(" and bd.paymentDate <= :endDate ");
		queryString.append(" and bd.deleted = :deleted ");

		if(ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue() == serviceId) {
			queryString.append(" and bd.accountCreationStatus = :accountCreationStatus ");
		}

		queryString.append(" and bd.validRecord = :validRecord ");
		queryString.append(" and bd.posted = :posted ");
		queryString.append(" and bd.settled = :settled ");
		queryString.append(" and fi.status in( :status )");
		queryString.append(" and fi.isApproved in( :isApproved )");
		queryString.append(" and fi.isCoreSumAccountNumber = :isCoreSumAccountNumber ");
		queryString.append(" and fi.appUserTypeModel.appUserTypeId = aut.appUserTypeId");

		try {

			Query query = getSession().createQuery(queryString.toString());
			query.setParameter("serviceId", serviceId);
			query.setParameter("endDate", end);
			query.setParameter("deleted", false);

			if(ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue() == serviceId) {
				query.setParameter("accountCreationStatus", AccountCreationStatusEnum.SUCCESSFUL.toString());
			}

			query.setParameter("validRecord", true);
			query.setParameter("posted", posted);
			query.setParameter("settled", settled);
			query.setParameter("isCoreSumAccountNumber", isCoreSumAccountNumber);
			query.setParameter("status", DisbursementStatusConstants.STATUS_READY_TO_DISBURSE);
			query.setParameter("isApproved", "1");

			query.setResultTransformer(Transformers.aliasToBean(DisbursementVO.class));

			return query.list();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<DisbursementVO> findDueDisbursementForT24(Long serviceId, Date end, Boolean isCoreSumAccountNumber, Boolean posted, Boolean settled) {
		StringBuilder queryString = new StringBuilder();
		queryString.append(" select bd.bulkDisbursementsId as disbursementId, bd.name as name, bd.mobileNo as mobileNo, bd.cnic as cnic, bd.amount as amount, bd.charges as charges,");
		queryString.append(" bd.fed as fed, bd.paymentDate as paymentDate, bd.settled as settled, bd.posted as posted, bd.batchNumber as batchNumber,");
		queryString.append(" bd.limitApplicable as limitApplicable, bd.payCashViaCnic as payCashViaCnic, bd.description as description,");
		queryString.append(" fi.sourceAccountNumber as sourceACNo, fi.fileInfoId as disbursementsFileInfoId, fi.isCoreSumAccountNumber as isCoreSumAccountNumber,");
		queryString.append(" aut.appUserTypeId as appUserTypeId, ");
		queryString.append(" p.productId as productId, p.name as productName, s.serviceId as serviceId, s.name as serviceName");

		queryString.append(" from BulkDisbursementsModel bd, BulkDisbursementsFileInfoModel fi, ProductModel p, ServiceModel s, AppUserTypeModel aut ");

		queryString.append(" where bd.relationFileInfoIdBulkDisbursementsFileInfoModel.fileInfoId = fi.fileInfoId");
		queryString.append(" and bd.relationProductIdProductModel = p.productId");
		queryString.append(" and bd.serviceId = s.serviceId" );
		queryString.append(" and s.serviceId = :serviceId" );
		queryString.append(" and bd.paymentDate <= :endDate ");
		queryString.append(" and bd.deleted = :deleted ");

		if(ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue() == serviceId) {
			queryString.append(" and bd.accountCreationStatus = :accountCreationStatus ");
		}

//		queryString.append(" and bd.validRecord = :validRecord ");
		queryString.append(" and bd.posted = :posted ");
		queryString.append(" and bd.settled = :settled ");
		queryString.append(" and fi.status in( :status )");
		queryString.append(" and fi.isApproved in( :isApproved )");
		queryString.append(" and fi.isCoreSumAccountNumber = :isCoreSumAccountNumber ");
		queryString.append(" and fi.appUserTypeModel.appUserTypeId = aut.appUserTypeId");

		try {

			Query query = getSession().createQuery(queryString.toString());
			query.setParameter("serviceId", serviceId);
			query.setParameter("endDate", end);
			query.setParameter("deleted", false);

			if(ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue() == serviceId) {
				query.setParameter("accountCreationStatus", AccountCreationStatusEnum.SUCCESSFUL.toString());
			}

//			query.setParameter("validRecord", true);
			query.setParameter("posted", posted);
			query.setParameter("settled", settled);
			query.setParameter("isCoreSumAccountNumber", isCoreSumAccountNumber);
			query.setParameter("status", DisbursementStatusConstants.STATUS_READY_TO_DISBURSE);
			query.setParameter("isApproved", "1");

			query.setResultTransformer(Transformers.aliasToBean(DisbursementVO.class));

			return query.list();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
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
		queryString.append(" AND model.validRecord = :validRecord ");
//		queryString.append(" AND model.relationBulkDisbursementsFileInfoIdBulkDisbursementsFileInfoModel.toProcess = :toProcess ");

		String[] paramNames = { "productId", "type", "endDate", "posted", "settled", "deleted", "accountCreationStatus", "validRecord"};//, "toProcess" };
		Object[] values = { productId, type, end, posted, settled, false, AccountCreationStatusEnum.SUCCESSFUL.toString(), Boolean.TRUE};//, Boolean.TRUE};
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
		queryString.append(" AND model.validRecord = :validRecord ");

		String[] paramNames = { "productId", "type", "endDate", "posted", "settled", "deleted", "accountCreationStatus", "validRecord" };
		Object[] values = { productId, type, end, posted, settled, false, AccountCreationStatusEnum.SUCCESSFUL.toString(), Boolean.TRUE };
		try {
			List<Double> list = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);
			total = list.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return total;
	}

	@Override
	public void update(Long disbursementId, String txCode) {
		StringBuilder str = new StringBuilder();
		str.append("update BulkDisbursementsModel model set model.transactionCode =:txCode, settled =:settled, settledOn =:settledOn, updatedOn =:updatedOn ");
		str.append("where bulkDisbursementsId =:disbursementId");

		Query query = getSession().createQuery(str.toString());
		query.setParameter("txCode", txCode);
		query.setParameter("settled", true);
		query.setParameter("settledOn", new Date(System.currentTimeMillis()));
		query.setParameter("updatedOn", new Date(System.currentTimeMillis()));
		query.setParameter("disbursementId", disbursementId);

		query.executeUpdate();
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

		StatelessSession session = sessionFactory.openStatelessSession();

		try {
				
			int i = 0;
		
			for(BulkDisbursementsModel bulkDisbursementsModel : bulkDisbursementsModelList) {
				session.update(bulkDisbursementsModel);
			}

		} catch(Exception e) {
			e.printStackTrace();
			logger.error(e);
			
		} finally {
			session.close();
		}
	}


	public void createOrUpdateBulkDisbursements(CopyOnWriteArrayList<String[]> recordList) throws FrameworkCheckedException {

		SessionFactory sessionFactory = this.getHibernateTemplate().getSessionFactory();

		Session session = sessionFactory.openSession();
		Connection connection = session.connection();


		try {

			connection.setAutoCommit(Boolean.FALSE);

			DisbursementDAO bulkDisbursementBatchInsert = new DisbursementDAO(connection);
			bulkDisbursementBatchInsert.processBulkDibursementFile(recordList, connection);

			connection.commit();

		} catch (Exception e) {
			throw new FrameworkCheckedException(e.getLocalizedMessage(), e);
		} finally {
			session.close();

		}
	}
	
	
	@Override
	public List<BulkDisbursementsXmlVo> getBulkDisbursementsXmlVoByBatchNo(String batchNo) {

		List<BulkDisbursementsXmlVo> voList = new ArrayList<BulkDisbursementsXmlVo>(0);

		String query = "SELECT BULK_DISBURSEMENTS_ID, MOBILE_NO, CNIC, CREATED_BY FROM BULK_DISBURSEMENTS WHERE BATCH_NUMBER = ?";

		SessionFactory sessionFactory = this.getHibernateTemplate().getSessionFactory();

		Session session = sessionFactory.openSession();
		Connection connection = session.connection();

		try {

			java.sql.PreparedStatement prepareStatement = connection.prepareStatement(query);

			prepareStatement.setString(1, batchNo);
			java.sql.ResultSet resultSet = prepareStatement.executeQuery();

			while(resultSet.next()) {

				voList.add(new BulkDisbursementsXmlVo(resultSet.getLong("BULK_DISBURSEMENTS_ID"), resultSet.getString("MOBILE_NO"), resultSet.getString("CNIC"), resultSet.getLong("CREATED_BY")));
			}

		} catch(Exception e) {

			e.printStackTrace();
		} finally {

			try {

				connection.close();
			} catch(Exception e) {

				e.printStackTrace();
			}
		}

		return voList;
	}


	@SuppressWarnings("unchecked")
	public Boolean updatePostedRecords(String batchNumber) {

		int affectedRows = 0;

		try {

			StringBuilder sqlQuery = new StringBuilder();

			sqlQuery.append(" update BulkDisbursementsModel model set model.posted = :posted, model.postedOn = :postedOn where model.batchNumber =:batchNumber" +
					" and validRecord =:valid and deleted =:deleted");

			Session hibernateSession = getHibernateTemplate().getSessionFactory().openSession();
			Query query = hibernateSession.createQuery(sqlQuery.toString());
			query.setParameter("posted", Boolean.TRUE);
			query.setParameter("postedOn", new Date());
			query.setParameter("batchNumber", batchNumber);
			query.setParameter("valid", true);
			query.setParameter("deleted", false);

			affectedRows = query.executeUpdate();

//			logger.info("affectedRows "+ affectedRows);

			SessionFactoryUtils.releaseSession(hibernateSession, getSessionFactory());

			super.releaseSession(hibernateSession);

		} catch (HibernateException e) {

			e.printStackTrace();
		}

		return affectedRows > 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public Boolean updatePostedRecordsForT24(String batchNumber) {
		int affectedRows = 0;

		try {

			StringBuilder sqlQuery = new StringBuilder();

			sqlQuery.append(" update BulkDisbursementsModel model set model.posted = :posted, model.postedOn = :postedOn where model.batchNumber =:batchNumber" +
					" and deleted =:deleted");

			Session hibernateSession = getHibernateTemplate().getSessionFactory().openSession();
			Query query = hibernateSession.createQuery(sqlQuery.toString());
			query.setParameter("posted", Boolean.TRUE);
			query.setParameter("postedOn", new Date());
			query.setParameter("batchNumber", batchNumber);
//			query.setParameter("valid", true);
			query.setParameter("deleted", false);

			affectedRows = query.executeUpdate();

//			logger.info("affectedRows "+ affectedRows);

			SessionFactoryUtils.releaseSession(hibernateSession, getSessionFactory());

			super.releaseSession(hibernateSession);

		} catch (HibernateException e) {

			e.printStackTrace();
		}

		return affectedRows > 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public List<Object[]> loadDisbursementsForSMS(Long disbursementFileInfoId) {

		List<Object[]> disbursements = null;

		StringBuilder queryString = new StringBuilder();

		queryString.append(" select model.mobileNo, model.amount, model.paymentDate, model.batchNumber  ");
		queryString.append(" from BulkDisbursementsModel model ");
		queryString.append(" where ");
		queryString.append(" model.relationFileInfoIdBulkDisbursementsFileInfoModel.fileInfoId = :disbursementFileInfoId ");
		queryString.append(" and model.relationFileInfoIdBulkDisbursementsFileInfoModel.status = :status ");
		queryString.append(" and model.serviceId =:serviceId");
		queryString.append(" and model.deleted = :deleted ");
		queryString.append(" and model.validRecord = :validRecord ");

		String[] paramNames = { "disbursementFileInfoId", "status", "serviceId", "deleted", "validRecord"};
		Object[] values = { disbursementFileInfoId, DisbursementStatusConstants.STATUS_READY_TO_DISBURSE, ServiceConstantsInterface.BULK_DISB_ACC_HOLDER, false, true};

		try {

			disbursements = getHibernateTemplate().findByNamedParam(queryString.toString(), paramNames, values);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return disbursements;
	}
}