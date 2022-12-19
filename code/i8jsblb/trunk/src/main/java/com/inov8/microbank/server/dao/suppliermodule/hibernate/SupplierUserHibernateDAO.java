package com.inov8.microbank.server.dao.suppliermodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.SupplierUserModel;
import com.inov8.microbank.server.dao.suppliermodule.SupplierUserDAO;

/**
 * @author Asad Hayat
 * @version 1.0
 */

public class SupplierUserHibernateDAO
    extends BaseHibernateDAO<SupplierUserModel, Long, SupplierUserDAO>
    implements SupplierUserDAO
{

}
