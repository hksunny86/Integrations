package com.inov8.microbank.server.dao.transactiondetailinfomodule.hibernate;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.ExtendedTransactionDetailPortalListModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.model.transactiondetailinfomodule.AllpayTransInfoListViewModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.server.dao.transactiondetailinfomodule.AllPayTransactionInfoListViewDAO;

	public class AllPayTransactionInfoListViewHibernateDAO extends BaseHibernateDAO<AllpayTransInfoListViewModel, Long, AllPayTransactionInfoListViewDAO> implements AllPayTransactionInfoListViewDAO {
	
		Logger logger = Logger.getLogger(AllPayTransactionInfoListViewHibernateDAO.class);
		
		
		/**
		 * @param recipientID
		 * @param mfsId
		 * @return
		 */
		public CustomList<AllpayTransInfoListViewModel> searchAllpayTransInfoListViewModel(SearchBaseWrapper searchBaseWrapper, String mfsId) throws FrameworkCheckedException {
			
			logger.info("searchAllAllPayTransaction");
			AllpayTransInfoListViewModel model = (AllpayTransInfoListViewModel) searchBaseWrapper.getBasePersistableModel();
			Criterion agent1IdCriterion = Restrictions.eq("agent1Id", mfsId);
			Criterion agent2IdCriterion = Restrictions.eq("agent2Id", mfsId);
			
			LogicalExpression expressionCriterion = Restrictions.or(agent1IdCriterion, agent2IdCriterion);
			
			CustomList<AllpayTransInfoListViewModel> customList = findByCriteria( expressionCriterion, model, searchBaseWrapper.getPagingHelperModel(), searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel() );

			logger.info("searchAllAllPayTransaction record found "+ customList.getResultsetList().size());
			
			return customList;
		}

		/* (non-Javadoc)
		 * @see com.inov8.microbank.server.dao.transactiondetailinfomodule.AllPayTransactionInfoListViewDAO#getMiniStatementTransactionList(com.inov8.microbank.common.model.transactiondetailinfomodule.AllpayTransInfoListViewModel, java.lang.Integer)
		 */
		public List<TransactionDetailPortalListModel> getMiniStatementTransactionList(ExtendedTransactionDetailPortalListModel model, Integer fetchSize) throws FrameworkCheckedException {
			
			String STATUS_INPROCESS = "In-process";
			String STATUS_COMPLETE = "Complete";
			String STATUS_REVERSED = "Reversed";
			String STATUS_UNCLAIMED = "Unclaimed";
			
			Criterion statusNameCriterion = Restrictions.in("processingStatusName", new String[]{STATUS_INPROCESS, STATUS_COMPLETE, STATUS_REVERSED, STATUS_UNCLAIMED});
			Criterion criterionOne = null;
			Criterion criterionTwo = null;
			
			if(model.getUserType().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
				
				criterionOne = Restrictions.eq("mfsId", model.getMfsId());;
				criterionTwo = Restrictions.eq("recipientMfsId", model.getRecipientMfsId());
				
			} else if(model.getUserType().longValue() == UserTypeConstantsInterface.RETAILER.longValue()) {
				
				criterionOne = Restrictions.eq("agent1Id", model.getAgent1Id());;
				criterionTwo = Restrictions.eq("agent2Id", model.getAgent2Id());
			}
				
			LogicalExpression orCriterion = Restrictions.or(criterionOne, criterionTwo);
	
			LogicalExpression andCriterion = Restrictions.and(orCriterion, statusNameCriterion);
			
			Session session = getSession();
            Criteria criteria = session.createCriteria(TransactionDetailPortalListModel.class);
			
			if(fetchSize != null) {
				criteria.setMaxResults(fetchSize);
			}
			
			criteria.addOrder(Order.desc("createdOn"));
			
			criteria.add(andCriterion);
			
			CopyOnWriteArrayList<TransactionDetailPortalListModel> resultList = new CopyOnWriteArrayList(criteria.list());
			
			if(resultList != null) {
				
				for(TransactionDetailPortalListModel listModel : resultList) {
					
					long productId = listModel.getProductId().longValue();
					
					String processingStatusName =  listModel.getProcessingStatusName();

					if((productId != 50010L && productId != 50011L) && 
							STATUS_INPROCESS.equalsIgnoreCase(processingStatusName.trim())) { // A2C, C2C
						
						resultList.remove(listModel);
					}					
				}
			}
			
			if(resultList.size() > 5) {	
				List<TransactionDetailPortalListModel> subList = resultList.subList(0, 5);
				resultList = new CopyOnWriteArrayList(subList);;
			}

			SessionFactoryUtils.releaseSession(session, getSessionFactory());						
			return resultList;
		}

	}
