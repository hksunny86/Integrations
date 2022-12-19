package com.inov8.microbank.server.dao.servicemodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.productmodule.ServiceListViewModel;
import com.inov8.microbank.server.dao.servicemodule.ServiceListViewDAO;

public class ServiceListViewHibernateDAO 
extends BaseHibernateDAO<ServiceListViewModel, Long, ServiceListViewDAO>
implements ServiceListViewDAO
{

}
