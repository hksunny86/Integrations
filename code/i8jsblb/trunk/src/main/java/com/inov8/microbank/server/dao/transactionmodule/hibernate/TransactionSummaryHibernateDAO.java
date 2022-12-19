package com.inov8.microbank.server.dao.transactionmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.TransactionSummaryModel;
import com.inov8.microbank.server.dao.transactionmodule.TransactionSummaryDAO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TransactionSummaryHibernateDAO
    extends BaseHibernateDAO<TransactionSummaryModel, Long, TransactionSummaryDAO>
    implements TransactionSummaryDAO
{
  public TransactionSummaryHibernateDAO()
  {
  }
}
