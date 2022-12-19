package com.inov8.microbank.tax.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.tax.common.TaxConstantsInterface;
import com.inov8.microbank.tax.dao.DailyWhtDeductionDAO;
import com.inov8.microbank.tax.dao.WHTDeductionSchedularStatusDAO;
import com.inov8.microbank.tax.model.DailyWhtDeductionModel;
import com.inov8.microbank.tax.model.WHTDeductionSchedularStatusModel;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2017</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class WHTDeductionSchedularStatusHibernateDAO
extends BaseHibernateDAO<WHTDeductionSchedularStatusModel, Long, WHTDeductionSchedularStatusDAO> implements WHTDeductionSchedularStatusDAO {
	public WHTDeductionSchedularStatusHibernateDAO()	{	}

	@Override
	public List<WHTDeductionSchedularStatusModel> findWHTDeductionMissedEntries()
			throws Exception {
		List<WHTDeductionSchedularStatusModel> appUserList = null;
			
		Session session = this.getSession();
		Criteria crit = session.createCriteria(WHTDeductionSchedularStatusModel.class);
		crit.addOrder(Order.desc("start_date"));
		crit.setMaxResults(5);
		
		List<WHTDeductionSchedularStatusModel> results = crit.list();
		
			return results;
	}

	@Override
	public void updateWhtSchedulerStatus() throws Exception {
		// TODO Auto-generated method stub
		
		logger.info("Start of WHTDeductionSchedularStatusHibernateDAO.updateWhtSchedulerStatus()");
		
		String hql = "update WHTDeductionSchedularStatusModel set completion_status=1,updated_on=? where completion_status = 0";
	    this.getHibernateTemplate().bulkUpdate(hql,new Date()); 
	    
	    logger.info("End of WHTDeductionSchedularStatusHibernateDAO.updateWhtSchedulerStatus()");
		
	}

}
