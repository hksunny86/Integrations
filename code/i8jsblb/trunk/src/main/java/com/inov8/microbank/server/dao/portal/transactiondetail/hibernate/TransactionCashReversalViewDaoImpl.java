package com.inov8.microbank.server.dao.portal.transactiondetail.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.TransactionCashReversalViewModel;
import com.inov8.microbank.server.dao.portal.transactiondetail.TransactionCashReversalViewDao;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jul 27, 2012 1:51:41 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class TransactionCashReversalViewDaoImpl extends BaseHibernateDAO<TransactionCashReversalViewModel, Long, TransactionCashReversalViewDao> implements TransactionCashReversalViewDao
{

	@SuppressWarnings("unchecked")
	public List<SupplierProcessingStatusModel> loadSupplierProcessingStatuses( Long[] ids ) throws DataAccessException
	{
		DetachedCriteria criteria = DetachedCriteria.forClass( SupplierProcessingStatusModel.class );
		criteria.add( Restrictions.in( "supProcessingStatusId", ids ) );
		return getHibernateTemplate().findByCriteria( criteria );
	}

}
