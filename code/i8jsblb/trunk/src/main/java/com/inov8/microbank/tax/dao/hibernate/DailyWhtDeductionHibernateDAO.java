package com.inov8.microbank.tax.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.tax.common.TaxConstantsInterface;
import com.inov8.microbank.tax.dao.DailyWhtDeductionDAO;
import com.inov8.microbank.tax.model.DailyWhtDeductionModel;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DailyWhtDeductionHibernateDAO
extends BaseHibernateDAO<DailyWhtDeductionModel, Long, DailyWhtDeductionDAO> implements DailyWhtDeductionDAO {
	public DailyWhtDeductionHibernateDAO()	{	}

	public List<DailyWhtDeductionModel> loadUnsettledWhtDeductionList(Date toDate) throws Exception{
		List<DailyWhtDeductionModel> unsettledWhtList = new ArrayList<>();

		Criteria criteria = getSession().createCriteria(DailyWhtDeductionModel.class);
		criteria.add(Restrictions.ne("createdOn", toDate));
		criteria.add(Restrictions.ne("status", TaxConstantsInterface.DAILY_WHT_DED_SUCCESS));
		List<DailyWhtDeductionModel> objectList = (List<DailyWhtDeductionModel>) criteria.list();

		return objectList;
	}
}
