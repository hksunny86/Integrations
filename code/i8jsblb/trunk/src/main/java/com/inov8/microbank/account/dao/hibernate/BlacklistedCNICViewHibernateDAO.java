package com.inov8.microbank.account.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.account.dao.BlacklistMarkingViewDAO;
import com.inov8.microbank.account.dao.BlacklistedCNICViewDAO;
import com.inov8.microbank.account.model.BlacklistMarkingViewModel;
import com.inov8.microbank.account.model.BlacklistedCnicsViewModel;

public class BlacklistedCNICViewHibernateDAO
        extends BaseHibernateDAO<BlacklistedCnicsViewModel, Long, BlacklistedCNICViewDAO>
        implements BlacklistedCNICViewDAO {
}
