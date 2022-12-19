package com.inov8.microbank.server.dao.customermodule.hibernate;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.BlinkCustomerRegistrationStateModel;
import com.inov8.microbank.common.model.RegistrationStateModel;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerRegistrationStateDAO;
import com.inov8.microbank.server.dao.customermodule.RegistrationStateDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Soofiafa
 * 
 */

public class BlinkCustomerRegistrationStateHibernateDAO extends
		BaseHibernateDAO<BlinkCustomerRegistrationStateModel, Long, BlinkCustomerRegistrationStateDAO>
		implements BlinkCustomerRegistrationStateDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inov8.microbank.server.dao.customermodule.RegistrationStateDAO#
	 * getRegistrationStateById(java.lang.Long)
	 */
	public BlinkCustomerRegistrationStateModel getRegistrationStateById(Long registrationStateId) {

		BlinkCustomerRegistrationStateModel regStateModel = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(BlinkCustomerRegistrationStateModel.class);
		criteria.add( Restrictions.eq("registrationStateId", registrationStateId) );
		List<BlinkCustomerRegistrationStateModel> list = getHibernateTemplate().findByCriteria(criteria);

		if(list != null && !list.isEmpty()) {

			regStateModel = list.get(0);
		}

		return regStateModel;
	}
	
	public CustomList<BlinkCustomerRegistrationStateModel> getRegistrationStateByIds(Long[] registrationStateIds) {
		
		CustomList<BlinkCustomerRegistrationStateModel> regStateList = new CustomList<BlinkCustomerRegistrationStateModel>();
		BlinkCustomerRegistrationStateModel regStateModel = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(BlinkCustomerRegistrationStateModel.class);
		criteria.add( Restrictions.in("registrationStateId", registrationStateIds) );
		List<BlinkCustomerRegistrationStateModel> list = getHibernateTemplate().findByCriteria(criteria);

		if(list != null && !list.isEmpty()) {

			regStateList.setResultsetList(list);
		}

		return regStateList;
	}
}
