package com.inov8.microbank.server.dao.bankmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.server.dao.bankmodule.BankDAO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */


public class BankHibernateDAO
    extends BaseHibernateDAO<BankModel, Long, BankDAO>
    implements BankDAO
{
}
