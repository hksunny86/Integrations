package com.inov8.microbank.server.dao.portal.allpaymodule;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AllpayDeReLinkListViewModel;

public class AllpayDelinkRelinkPaymentModeHibernateDAO extends
		BaseHibernateDAO<AllpayDeReLinkListViewModel, Long, AllpayDelinkRelinkPaymentModeViewDAO> implements AllpayDelinkRelinkPaymentModeViewDAO
{

	@Override
	public boolean isCoreAccountLinkedToOtherAgent(String accountNumber, Long retailerContactId) throws FrameworkCheckedException
	{
		List<AllpayDeReLinkListViewModel> list = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AllpayDeReLinkListViewModel.class);
		detachedCriteria.add(Restrictions.eq("accountNo", accountNumber));
		detachedCriteria.add(Restrictions.ne("retailerContactId", retailerContactId));

		list = getHibernateTemplate().findByCriteria(detachedCriteria);

		if (list != null && list.size() > 0)
		{
			return true;
		}
		return false;
	}
}
