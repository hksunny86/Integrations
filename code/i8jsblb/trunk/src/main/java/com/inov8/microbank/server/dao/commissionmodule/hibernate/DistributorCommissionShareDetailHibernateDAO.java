package com.inov8.microbank.server.dao.commissionmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommissionShareDetailModel;
import com.inov8.microbank.server.dao.commissionmodule.DistributorCommissionShareDetailDAO;

public class DistributorCommissionShareDetailHibernateDAO
		extends
		BaseHibernateDAO<DistributorCommissionShareDetailModel, Long, DistributorCommissionShareDetailDAO>
		implements DistributorCommissionShareDetailDAO {

	@Override
	public void deleteDistributorCommShareDtlModel(Long deleteDistributorCommShareId) {

		String hql	=	"delete from DistributorCommissionShareDetailModel where distributorCommissionShareIdModel.distributorCommissionShareId=?";
		getHibernateTemplate().bulkUpdate(hql, deleteDistributorCommShareId);
	}
}
