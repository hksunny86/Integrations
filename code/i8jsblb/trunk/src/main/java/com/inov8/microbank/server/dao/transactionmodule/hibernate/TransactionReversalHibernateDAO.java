package com.inov8.microbank.server.dao.transactionmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.TransactionReversalModel;
import com.inov8.microbank.server.dao.transactionmodule.TransactionReversalDAO;

public class TransactionReversalHibernateDAO extends BaseHibernateDAO<TransactionReversalModel,Long,TransactionReversalDAO>
    implements TransactionReversalDAO{
}
