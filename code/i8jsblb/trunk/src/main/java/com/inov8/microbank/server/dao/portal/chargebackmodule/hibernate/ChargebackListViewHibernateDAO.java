package com.inov8.microbank.server.dao.portal.chargebackmodule.hibernate;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.chargebackmodule.ChargebackListViewModel;
import com.inov8.microbank.server.dao.portal.chargebackmodule.ChargebackListViewDAO;

public class ChargebackListViewHibernateDAO extends BaseHibernateDAO<ChargebackListViewModel, Long, ChargebackListViewDAO> implements ChargebackListViewDAO {
	
	public CustomList<ChargebackListViewModel> getChargeBackTransactions(SearchBaseWrapper wrapper) throws FrameworkCheckedException {
		ChargebackListViewModel chargebackListViewModel = (ChargebackListViewModel)wrapper.getBasePersistableModel();
		Criterion mfsId = Restrictions.eq("mfsId", chargebackListViewModel.getMfsId());
		Criterion recipientMfsID = Restrictions.eq("recipientMfsId", chargebackListViewModel.getMfsId());
		LogicalExpression expressionCriterion = Restrictions.or(mfsId, recipientMfsID);
		CustomList<ChargebackListViewModel> customList = this.findByCriteria(expressionCriterion, new ChargebackListViewModel(), wrapper.getPagingHelperModel(), wrapper.getSortingOrderMap() );
		return customList;
	}		

}
