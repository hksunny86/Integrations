package com.inov8.microbank.server.dao.productmodule.hibernate;

/**
 * <p>Company: Inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ProductIntgModuleInfoModel;
import com.inov8.microbank.server.dao.productmodule.ProductIntegrationModuleInfoDAO;


public class ProductIntegrationModuleInfoHibernateDAO
    extends BaseHibernateDAO<ProductIntgModuleInfoModel, Long, ProductIntegrationModuleInfoDAO>
    implements ProductIntegrationModuleInfoDAO
{

}
