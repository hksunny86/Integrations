package com.inov8.microbank.server.dao.handlermodule.hibernate;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.HandlerModel;
import com.inov8.microbank.common.model.HandlerModel;
import com.inov8.microbank.server.dao.handlermodule.HandlerDAO;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class HandlerHibernateDAO
    extends
    BaseHibernateDAO<HandlerModel, Long, HandlerDAO>
    implements
    HandlerDAO

{

	/*public HandlerModel loadHandlerByMobile(String MobileNo){
		List<HandlerModel> handlersList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( HandlerModel.class );
        detachedCriteria.add(Restrictions.eq("relationDistributorIdDistributorModel.distributorId", distributorId)).addOrder(Order.asc("name"));
        handlersList = getHibernateTemplate().findByCriteria( detachedCriteria );
	}*/
	

	public List<Long> getRetalerDataMap(Long retailerContactId) {
	
		List<Long> list = null;
		
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" SELECT a.appUserId, r.relationDistributorIdDistributorModel.distributorId, r.regionModel.regionId ");
		queryBuilder.append(" FROM AppUserModel a, RetailerContactModel rc, RetailerModel r WHERE ");
		queryBuilder.append(" rc.retailerContactId = a.relationRetailerContactIdRetailerContactModel.retailerContactId and ");
		queryBuilder.append(" rc.relationRetailerIdRetailerModel.retailerId = r.retailerId and ");
		queryBuilder.append(" a.relationRetailerContactIdRetailerContactModel.retailerContactId  =:retailerContactId ");
		String[] paramNames = { "retailerContactId"};
		Object[] values = { retailerContactId};
		
		try {
			
			list = getHibernateTemplate().findByNamedParam(queryBuilder.toString(), paramNames, values);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
