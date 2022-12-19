/**
 * 
 */
package com.inov8.microbank.server.dao.dailyjob;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.SettlementTransactionModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;

public class SettlementTransactionHibernateDAO extends BaseHibernateDAO <BasePersistableModel, Long, SettlementTransactionDAO> implements SettlementTransactionDAO {
	

	/**
	 * @param productId
	 * @param isSettled
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SettlementTransactionModel> getSettlementTransactionModelList(SettlementTransactionModel settlementTransactionModel) { 	

		Date startTime = null;
		Date endTime = null;
		
		try {
			
			startTime = settlementTransactionModel.getCreatedOn();
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			startTime = format.parse(format.format(startTime));

			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(startTime);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			endTime = calendar.getTime();
			
		} catch (ParseException e) {
			e.printStackTrace();
		}	

		Criterion statusEqual = Restrictions.eq("status", 0L);
		Criterion greateThanEqual = Restrictions.ge("createdOn", startTime);
		Criterion lessThanEqual = Restrictions.le("createdOn", endTime);
		Criterion bankInfoID  = null;
		  
		if(settlementTransactionModel.getFromBankInfoID() != null) {

			bankInfoID  = Restrictions.eq("fromBankInfoID", settlementTransactionModel.getFromBankInfoID());
			
		} else if (settlementTransactionModel.getToBankInfoID() != null) {
			
			bankInfoID  = Restrictions.eq("toBankInfoID", settlementTransactionModel.getToBankInfoID());
		}	

		LogicalExpression logicalExpression1 = Restrictions.and(greateThanEqual, lessThanEqual);
		LogicalExpression logicalExpression2 = Restrictions.and(bankInfoID, statusEqual);

		LogicalExpression criterion = Restrictions.and(logicalExpression1, logicalExpression2);
		
		Session session = super.getSession();
	
		Criteria criteria = session.createCriteria(SettlementTransactionModel.class);
		
		criteria.add(criterion);
		
		List<SettlementTransactionModel> list = criteria.list();

		SessionFactoryUtils.releaseSession(session, getSessionFactory());		
		
		return list;
	}

	/**
	 * @param productId
	 * @param isSettled
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SettlementTransactionModel> getPendingSettlementTransactionList(StakeholderBankInfoModel stakeholderBankInfoModel, Date _startTime, Boolean isCredit) { 	

		Date startTime = null;
		
		try {
			
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			startTime = format.parse(format.format(_startTime));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		
		String stakeholderBankInfoIdProperty = "fromBankInfoID";
		
		if(isCredit) {
			stakeholderBankInfoIdProperty = "toBankInfoID";
		}

		Criterion stakeholderBankInfoIdEqual = Restrictions.eq(stakeholderBankInfoIdProperty, stakeholderBankInfoModel.getStakeholderBankInfoId());
		Criterion lessThan = Restrictions.lt("createdOn", startTime);
		Criterion statusEqual = Restrictions.eq("status", 0L);

		LogicalExpression logicalExpression = Restrictions.and(Restrictions.and(statusEqual, lessThan), stakeholderBankInfoIdEqual);
		
		Session session = super.getSession();
	
		Criteria criteria = session.createCriteria(SettlementTransactionModel.class);
		criteria.addOrder(Order.desc("createdOn"));
		
		criteria.add(logicalExpression);
		
		List<SettlementTransactionModel> list = criteria.list();

		SessionFactoryUtils.releaseSession(session, getSessionFactory());		
		
		return list;
	}
	

	/**
	 * @param transactionIDs
	 * @param settlementSchedulerID
	 * @param status
	 * @return	 */
	@SuppressWarnings("unchecked")
	public Boolean updateSettlementTransaction(Object[] settlementTransactionID, Long settlementSchedulerID, Long status) { 
		
		int affectedRows = 0;
		
		try {
			
			StringBuilder sqlQuery = new StringBuilder();
			
			sqlQuery.append(" update com.inov8.microbank.common.model.SettlementTransactionModel ST set ST.settlementSchedulerID = :settlementSchedulerID , ST.status = :status where ST.settlementTransactionID in (:settlementTransactionID)");

			Session hibernateSession = getHibernateTemplate().getSessionFactory().openSession();
			Query query = hibernateSession.createQuery(sqlQuery.toString());
			query.setParameter("settlementSchedulerID", settlementSchedulerID);
			query.setParameter("status", status);
			query.setParameterList("settlementTransactionID", settlementTransactionID);
			
			affectedRows = query.executeUpdate();
			
//			logger.info("affectedRows "+ affectedRows);
			
			SessionFactoryUtils.releaseSession(hibernateSession, getSessionFactory());

//			super.releaseSession(hibernateSession);
			
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return affectedRows > 0 ? Boolean.TRUE : Boolean.FALSE;
	}
}
