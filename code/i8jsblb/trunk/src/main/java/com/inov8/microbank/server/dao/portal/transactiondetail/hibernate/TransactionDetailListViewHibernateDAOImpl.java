package com.inov8.microbank.server.dao.portal.transactiondetail.hibernate;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionDetailListViewModel;
import com.inov8.microbank.server.dao.portal.transactiondetail.TransactionDetailListViewDAO;

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

public class TransactionDetailListViewHibernateDAOImpl
		extends
		BaseHibernateDAO<TransactionDetailListViewModel, Long,TransactionDetailListViewDAO> implements TransactionDetailListViewDAO {

	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.dao.portal.transactiondetail.TransactionDetailListViewDAO#getTransactionDetail(com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionDetailListViewModel)
	 */
	public SearchBaseWrapper getTransactionDetail(SearchBaseWrapper wrapper, String mfsId) throws FrameworkCheckedException {
		TransactionDetailListViewModel viewModel = (TransactionDetailListViewModel) wrapper.getBasePersistableModel();
		Criterion criterionOne = Restrictions.eq("mfsId", mfsId);
		Criterion criterionTwo = Restrictions.eq("recipientId", mfsId);

		LogicalExpression expressionCriterion = Restrictions.or(criterionOne, criterionTwo);

		CustomList<TransactionDetailListViewModel> customList = super.findByCriteria(expressionCriterion, viewModel, wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap(),wrapper.getDateRangeHolderModel());
		wrapper.setCustomList( customList );

		logger.info("getTransactionDetail mfsId = "+ viewModel.getMfsId() +
				" recipientId = "+viewModel.getRecipientId() +
				" record found "+ customList.getResultsetList().size());

		return wrapper;
	}
}
