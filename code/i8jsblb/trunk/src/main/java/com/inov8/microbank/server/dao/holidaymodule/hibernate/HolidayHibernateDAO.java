package com.inov8.microbank.server.dao.holidaymodule.hibernate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.HolidayModel;
import com.inov8.microbank.server.dao.holidaymodule.HolidayDAO;

public class HolidayHibernateDAO extends BaseHibernateDAO<HolidayModel, Long, HolidayDAO> implements HolidayDAO {
	
	private final static Log logger = LogFactory.getLog(HolidayHibernateDAO.class);
	
	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.dao.holidaymodule.HolidayDAO#getHolidayModelByDate(java.util.Date)
	 */
	public List<HolidayModel> getHolidayModelByDate(Date holidayDate) {
		
		List<HolidayModel> list = new ArrayList<HolidayModel>(0);
		
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date _holidayDate;
		
		try {
			
			_holidayDate = format.parse(format.format(holidayDate));
			  Criterion criterion = Restrictions.eq("holidayDate", _holidayDate);
			  Session session = super.getSession();
		
			  Criteria criteria = session.createCriteria(HolidayModel.class);
			  criteria.add(criterion);

			  list = criteria.list();

			  SessionFactoryUtils.releaseSession(session, getSessionFactory());
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		
		  
		  return list;
	}
	
	/**
	 * @param firstDate
	 * @param endDate
	 * @return
	 */
	public List<HolidayModel> getHolidayModelByRange(Calendar firstDate, Calendar endDate) {
		
		java.sql.Timestamp startTime = new java.sql.Timestamp(firstDate.getTime().getTime());
		java.sql.Timestamp endTime = new java.sql.Timestamp(endDate.getTime().getTime());
		
		Criterion greateThanEqual = Restrictions.ge("holidayDate", startTime);
		Criterion lessThanEqual = Restrictions.le("holidayDate", endTime);
		  
		LogicalExpression logicalExpression = Restrictions.and(greateThanEqual, lessThanEqual);
					  
		Session session = super.getSession();
	
		Criteria criteria = session.createCriteria(HolidayModel.class);
		criteria.add(logicalExpression);

		logger.info(criteria.toString());
		
		List<HolidayModel> list = criteria.list();

		SessionFactoryUtils.releaseSession(session, getSessionFactory());
		  
		return list;
	}
	
}