package com.inov8.microbank.tax.dao.hibernate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.ParseException;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.util.DateUtils;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.tax.dao.WHTExemptionDAO;
import com.inov8.microbank.tax.model.DateWiseUserWHTAmountViewModel;
import com.inov8.microbank.tax.model.WHTExemptionModel;

public class WHTExemptionHibernateDAO extends 
    BaseHibernateDAO<WHTExemptionModel, Long, WHTExemptionDAO>
    implements WHTExemptionDAO
{

	public List<WHTExemptionModel> loadWHTExemptionByAppUserId(Long appUserId){

		DetachedCriteria criteria = DetachedCriteria.forClass(WHTExemptionModel.class);
		criteria.add( Restrictions.eq("appUserModel.appUserId",appUserId));
		criteria.add( Restrictions.eq("active",true));
		criteria.addOrder(Order.desc("endDate"));

		List<WHTExemptionModel> list = getHibernateTemplate().findByCriteria(criteria);
		return list;

	}
	
	public WHTExemptionModel loadValidWHTExemption(Long appUserId) {
		 Date today = new Date();
		today = PortalDateUtils.getDateWithoutTime(today);
		
		Criteria criteria = getSession().createCriteria(WHTExemptionModel.class);
	    criteria.add( Restrictions.eq("appUserModel.appUserId",appUserId));
		criteria.add( Restrictions.eq("active",true));
	    criteria.add( Restrictions.le("startDate", today));
	    criteria.add( Restrictions.ge("endDate", today));
		
		List<WHTExemptionModel> list = criteria.list();
		if(CollectionUtils.isNotEmpty(list))
			return list.get(0);
		
		return null;
	}
	
	public WHTExemptionModel loadValidWHTExemptionForScheduler(Long appUserId) {
		 Date today = new Date();
		today = PortalDateUtils.getDateWithoutTime(today);
		Date wht_date = new Date();
		wht_date=PortalDateUtils.getDateWithoutTime(PortalDateUtils.subtractDays(wht_date,1));
		
		Criteria criteria = getSession().createCriteria(WHTExemptionModel.class);
	    criteria.add( Restrictions.eq("appUserModel.appUserId",appUserId));
		criteria.add( Restrictions.eq("active",true));
	    criteria.add( Restrictions.le("startDate", wht_date));
	    criteria.add( Restrictions.ge("endDate", wht_date));
		
		List<WHTExemptionModel> list = criteria.list();
		if(CollectionUtils.isNotEmpty(list))
			return list.get(0);
		
		return null;
	}
	

}
