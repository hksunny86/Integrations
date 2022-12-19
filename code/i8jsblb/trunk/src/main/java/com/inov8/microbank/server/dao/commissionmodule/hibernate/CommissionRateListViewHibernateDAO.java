package com.inov8.microbank.server.dao.commissionmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.commissionmodule.CommissionRateListViewModel;
import com.inov8.microbank.server.dao.commissionmodule.CommissionRateListViewDAO;

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

public class CommissionRateListViewHibernateDAO
    extends BaseHibernateDAO<CommissionRateListViewModel, Long,
                             CommissionRateListViewDAO>
    implements CommissionRateListViewDAO
{
}
