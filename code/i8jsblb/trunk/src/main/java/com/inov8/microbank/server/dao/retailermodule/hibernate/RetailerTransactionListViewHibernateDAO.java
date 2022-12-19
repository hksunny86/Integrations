package com.inov8.microbank.server.dao.retailermodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.retailermodule.RetailerTransactionListViewModel;
import com.inov8.microbank.server.dao.retailermodule.RetailerTransactionListViewDAO;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class RetailerTransactionListViewHibernateDAO
    extends
    BaseHibernateDAO<RetailerTransactionListViewModel, Long,
                     RetailerTransactionListViewDAO>
    implements
    RetailerTransactionListViewDAO
{

}
