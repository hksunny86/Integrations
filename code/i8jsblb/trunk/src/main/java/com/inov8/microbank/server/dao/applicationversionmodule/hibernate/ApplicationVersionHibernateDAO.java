package com.inov8.microbank.server.dao.applicationversionmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AppVersionModel;
import com.inov8.microbank.server.dao.applicationversionmodule.ApplicationVersionDAO;

public class ApplicationVersionHibernateDAO
extends BaseHibernateDAO<AppVersionModel, Long, ApplicationVersionDAO>
implements ApplicationVersionDAO
{

}
