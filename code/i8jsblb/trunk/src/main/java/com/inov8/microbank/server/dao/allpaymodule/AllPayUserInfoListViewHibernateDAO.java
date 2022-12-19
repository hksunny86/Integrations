package com.inov8.microbank.server.dao.allpaymodule;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AllpayUserInfoListViewModel;

public class AllPayUserInfoListViewHibernateDAO  extends
BaseHibernateDAO <AllpayUserInfoListViewModel , Long,AllPayUserInfoListViewDAO>
implements AllPayUserInfoListViewDAO{

}
