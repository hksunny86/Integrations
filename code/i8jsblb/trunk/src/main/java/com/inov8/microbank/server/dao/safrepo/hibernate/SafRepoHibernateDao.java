package com.inov8.microbank.server.dao.safrepo.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.SafRepoModel;
import com.inov8.microbank.server.dao.safrepo.SafRepoDao;

public class SafRepoHibernateDao extends BaseHibernateDAO<SafRepoModel,Long,SafRepoDao> implements SafRepoDao {

	@Override
	public void updateSafRepoStatus(SafRepoModel model) {
				
		String updateHql ="update SafRepoModel model set model.updatedOn=?, model.transactionStatus=? where model.ledgerId=?";
		Object[] values = {model.getUpdatedOn(),model.getTransactionStatus(),model.getLedgerId()};
		getHibernateTemplate().bulkUpdate(updateHql, values);
	}

	@Override
	public void updateSafRepoByQueue(SafRepoModel model) {
				
		String updateHql ="update SafRepoModel model set model.updatedOn=?, model.isComplete=? where model.ledgerId=?";
		Object[] values = {model.getUpdatedOn(),model.getIsComplete(),model.getLedgerId()};
		getHibernateTemplate().bulkUpdate(updateHql, values);
	}

}
