package com.inov8.microbank.server.dao.portal.transactiondetail.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.CashTransactionViewModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.SenderRedeemViewModel;
import com.inov8.microbank.server.dao.portal.transactiondetail.CashTransactionViewDAO;
import com.inov8.microbank.server.dao.portal.transactiondetail.SenderRedeemViewDAO;

public class CashTransactionViewHibernateDAOImpl extends BaseHibernateDAO<CashTransactionViewModel, Long, CashTransactionViewDAO> 
	implements CashTransactionViewDAO{

	@SuppressWarnings("unchecked")
	public List<SupplierProcessingStatusModel> loadSupplierProcessingStatuses( Long[] ids ) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass( SupplierProcessingStatusModel.class );
		criteria.add( Restrictions.in( "supProcessingStatusId", ids ) );
		return getHibernateTemplate().findByCriteria( criteria );
	}

}
