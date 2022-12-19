package com.inov8.microbank.server.dao.smartmoneymodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.smartmoneymodule.SmartMoneyAccountListViewModel;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountListViewDAO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Asad Hayat
 * @version 1.0
 */


public class SmartMoneyAccountListViewHibernateDAO
    extends BaseHibernateDAO<SmartMoneyAccountListViewModel, Long,
                             SmartMoneyAccountListViewDAO>
    implements SmartMoneyAccountListViewDAO
{
}
