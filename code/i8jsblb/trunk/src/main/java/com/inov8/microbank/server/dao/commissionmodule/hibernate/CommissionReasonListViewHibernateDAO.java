package com.inov8.microbank.server.dao.commissionmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.commissionmodule.CommissionReasonListViewModel;
import com.inov8.microbank.server.dao.commissionmodule.CommissionReasonListViewDAO;

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

public class CommissionReasonListViewHibernateDAO
    extends BaseHibernateDAO<CommissionReasonListViewModel, Long,
                             CommissionReasonListViewDAO>
    implements CommissionReasonListViewDAO
{
}
