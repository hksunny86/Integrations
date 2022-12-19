package com.inov8.microbank.server.dao.customermodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.RegistrationStateModel;
import com.inov8.microbank.server.dao.customermodule.RegistrationStateDAO;

/**
 * @author Soofiafa
 * 
 */

public class RegistrationStateHibernateDAO extends
		BaseHibernateDAO<RegistrationStateModel, Long, RegistrationStateDAO>
		implements RegistrationStateDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inov8.microbank.server.dao.customermodule.RegistrationStateDAO#
	 * getRegistrationStateById(java.lang.Long)
	 */
	public RegistrationStateModel getRegistrationStateById(Long registrationStateId) {

		RegistrationStateModel regStateModel = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(RegistrationStateModel.class);
		criteria.add( Restrictions.eq("registrationStateId", registrationStateId) );
		List<RegistrationStateModel> list = getHibernateTemplate().findByCriteria(criteria);

		if(list != null && !list.isEmpty()) {

			regStateModel = list.get(0);
		}

		return regStateModel;
	}
	
	public CustomList<RegistrationStateModel> getRegistrationStateByIds(Long[] registrationStateIds) {
		
		CustomList<RegistrationStateModel> regStateList = new CustomList<RegistrationStateModel>();
		RegistrationStateModel regStateModel = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(RegistrationStateModel.class);
		criteria.add( Restrictions.in("registrationStateId", registrationStateIds) );
		List<RegistrationStateModel> list = getHibernateTemplate().findByCriteria(criteria);

		if(list != null && !list.isEmpty()) {

			regStateList.setResultsetList(list);
		}

		return regStateList;
	}
}
