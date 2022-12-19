package com.inov8.microbank.server.dao.mfsmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.appversionmodule.AppVersionListViewModel;
import com.inov8.microbank.server.dao.mfsmodule.AppVersionListViewDAO;

public class AppVersionListViewHibernateDAO
	extends BaseHibernateDAO<AppVersionListViewModel, Long, AppVersionListViewDAO>
	implements AppVersionListViewDAO
{

}
