package com.inov8.microbank.server.dao.portal.transactiondetail.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.SupplierProcessingStatusModel;
import com.inov8.microbank.common.model.portal.transactiondetailmodule.SenderRedeemViewModel;
import com.inov8.microbank.server.dao.portal.transactiondetail.SenderRedeemViewDAO;

public class SenderRedeemViewHibernateDAOImpl extends BaseHibernateDAO<SenderRedeemViewModel, Long, SenderRedeemViewDAO> 
	implements SenderRedeemViewDAO{

	@SuppressWarnings("unchecked")
	public List<SupplierProcessingStatusModel> loadSupplierProcessingStatuses( Long[] ids ) throws DataAccessException{
		DetachedCriteria criteria = DetachedCriteria.forClass( SupplierProcessingStatusModel.class );
		criteria.add( Restrictions.in( "supProcessingStatusId", ids ) );
		return getHibernateTemplate().findByCriteria( criteria );
	}

}
