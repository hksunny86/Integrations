package com.inov8.microbank.server.dao.safrepo.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.SafRepoCoreModel;
import com.inov8.microbank.server.dao.safrepo.SafRepoCoreDao;

public class SafRepoCoreHibernateDao extends BaseHibernateDAO<SafRepoCoreModel,Long,SafRepoCoreDao> implements SafRepoCoreDao {

	@Override
	public void updateStatus(SafRepoCoreModel safRepoCoreModel) {
		String updateHql ="update SafRepoCoreModel set updatedOn=?, status=? where transactionCode=?";
		Object[] values = {safRepoCoreModel.getUpdatedOn(), safRepoCoreModel.getStatus(), safRepoCoreModel.getTransactionCode()};
		getHibernateTemplate().bulkUpdate(updateHql, values);
	}

}
