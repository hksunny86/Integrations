package com.inov8.microbank.server.dao.retailermodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.retailermodule.RetailerListViewModel;
import com.inov8.microbank.server.dao.retailermodule.RetailerListViewDAO;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class RetailerListViewHibernateDAO
    extends BaseHibernateDAO<RetailerListViewModel, Long, RetailerListViewDAO>
    implements RetailerListViewDAO
{

}
