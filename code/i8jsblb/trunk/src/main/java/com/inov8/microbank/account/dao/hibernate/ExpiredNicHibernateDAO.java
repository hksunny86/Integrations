package com.inov8.microbank.account.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.account.dao.ExpiredNicDAO;
import com.inov8.microbank.account.model.ExpiredNicViewModel;

public class ExpiredNicHibernateDAO extends BaseHibernateDAO<ExpiredNicViewModel,Long,ExpiredNicDAO>
    implements ExpiredNicDAO{
}
