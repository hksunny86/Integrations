package com.inov8.microbank.account.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.account.dao.WalkInBlackListedNicDAO;
import com.inov8.microbank.account.model.WalkInBlackListedNicViewModel;

public class WalkInBlackListedNicHibernateDAO extends BaseHibernateDAO<WalkInBlackListedNicViewModel,Long,WalkInBlackListedNicDAO>
    implements WalkInBlackListedNicDAO{
}
