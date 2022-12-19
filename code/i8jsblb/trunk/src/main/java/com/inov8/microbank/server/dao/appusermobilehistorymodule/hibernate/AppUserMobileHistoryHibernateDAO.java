package com.inov8.microbank.server.dao.appusermobilehistorymodule.hibernate;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AppUserMobileHistoryModel;
import com.inov8.microbank.server.dao.appusermobilehistorymodule.AppUserMobileHistoryDAO;


public class AppUserMobileHistoryHibernateDAO  extends BaseHibernateDAO<AppUserMobileHistoryModel, Long, AppUserMobileHistoryDAO> implements AppUserMobileHistoryDAO  {

	@Override
	public SearchBaseWrapper find(SearchBaseWrapper wrapper) {
		CustomList<AppUserMobileHistoryModel> customList = null;
		Criterion toDateIsNullCriterian = Restrictions.isNull("toDate");
		customList = findByCriteria(toDateIsNullCriterian, (AppUserMobileHistoryModel) wrapper.getBasePersistableModel());
		wrapper.setCustomList(customList);
		return wrapper;
	}
}