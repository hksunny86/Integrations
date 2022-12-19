package com.inov8.microbank.server.dao.operatormodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.OperatorUserModel;
import com.inov8.microbank.server.dao.operatormodule.OperatorUserDAO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class OperatorUserHibernateDAO
    extends BaseHibernateDAO<OperatorUserModel, Long
                             , OperatorUserDAO>
    implements OperatorUserDAO
{
  public OperatorUserHibernateDAO()
  {
  }
}
