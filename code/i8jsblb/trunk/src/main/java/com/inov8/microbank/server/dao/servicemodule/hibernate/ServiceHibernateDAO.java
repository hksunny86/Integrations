package com.inov8.microbank.server.dao.servicemodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.server.dao.servicemodule.ServiceDAO;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import java.util.List;

public class ServiceHibernateDAO extends BaseHibernateDAO<ServiceModel, Long, ServiceDAO> implements ServiceDAO {

	
	/**
	 * @param serviceModel
	 * @return
	 */
	public List<ServiceModel> loadSeviceModelListByType(ServiceModel serviceModel) {
	
		List<ServiceModel> list = null;
	
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ServiceModel.class);
		detachedCriteria.add(Restrictions.eq("relationServiceTypeIdServiceTypeModel.serviceTypeId", serviceModel.getServiceTypeId()));
	    detachedCriteria.addOrder(Order.asc("name"));
	    
	    list = getHibernateTemplate().findByCriteria(detachedCriteria);	
	    return list;
	}

	@Override
	public List<LabelValueBean> getServiceLabels(Long... pk) {
		StringBuilder  queryStr = new StringBuilder();
		queryStr.append("select a.serviceId as id, a.name as label ");
		queryStr.append("from ServiceModel a ");
		queryStr.append("where a.active = 1 ");

		if(pk.length > 0) {
			queryStr.append("and a.serviceId in (:pk) ");
		}
		queryStr.append("order by a.name asc");

		try {
			Query query = getSession().createQuery(queryStr.toString());

			if(pk.length > 0) {
				query.setParameterList("pk", pk);
			}

			query.setResultTransformer(Transformers.aliasToBean(LabelValueBean.class));
			return query.list();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}