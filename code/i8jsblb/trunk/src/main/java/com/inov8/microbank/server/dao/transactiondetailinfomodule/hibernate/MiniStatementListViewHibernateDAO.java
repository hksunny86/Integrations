package com.inov8.microbank.server.dao.transactiondetailinfomodule.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.MiniStatementListViewModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.dao.transactiondetailinfomodule.MiniStatementListViewDAO;

	public class MiniStatementListViewHibernateDAO extends BaseHibernateDAO<MiniStatementListViewModel, Long, MiniStatementListViewDAO> implements MiniStatementListViewDAO {
	
		Logger logger = Logger.getLogger(MiniStatementListViewHibernateDAO.class);
		
		
		public List<MiniStatementListViewModel> getMiniStatementListViewModelList(MiniStatementListViewModel model, Integer fetchSize) throws FrameworkCheckedException {
			
			Criterion criterionOne = null;
			Criterion criterionTwo = null;
			Criterion criterionThree = null;
			Criterion criterionFour = null;

			LogicalExpression andCriterion = null;
			LogicalExpression finalCriterion = null;
						
			if(model.getUserType().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
				
				criterionOne = Restrictions.eq("mfsId", model.getMfsId());
				criterionTwo = Restrictions.eq("recipientMfsId", model.getRecipientMfsId());
				if(model.getRecipientAccountNick() != null)
					criterionThree = Restrictions.eq("recipientAccountNick", model.getRecipientAccountNick());
				
			} else if(model.getUserType().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
				
				criterionOne = Restrictions.eq("agent1Id", model.getAgent1Id());
				criterionTwo = Restrictions.eq("agent2Id", model.getAgent2Id());
				if(model.getSenderAccountNick() != null)
					criterionThree = Restrictions.eq("senderAccountNick", model.getSenderAccountNick());
			}

			if(model.getPaymentModeId() != null)
				criterionFour = Restrictions.eq("paymentModeId", model.getPaymentModeId());

			LogicalExpression orCriterion = Restrictions.or(criterionOne, criterionTwo);

			if(criterionThree != null)
				andCriterion = Restrictions.or(orCriterion,criterionThree);
			if(andCriterion != null)
				finalCriterion = Restrictions.and(andCriterion,criterionFour);
			else
				finalCriterion = Restrictions.and(orCriterion,criterionFour);

			Session session = getSession();
            Criteria criteria = session.createCriteria(MiniStatementListViewModel.class);
			criteria.add(finalCriterion);
			criteria.setMaxResults(fetchSize);
			criteria.addOrder(Order.desc("createdOn"));
						
			List<MiniStatementListViewModel> resultList = criteria.list();
			SessionFactoryUtils.releaseSession(session, getSessionFactory());
			logger.info(resultList.size());
			
			return resultList;
		}

	}
