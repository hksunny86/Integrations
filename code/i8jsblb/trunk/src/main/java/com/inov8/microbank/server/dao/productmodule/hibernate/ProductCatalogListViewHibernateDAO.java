package com.inov8.microbank.server.dao.productmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.productmodule.ProductCatalogListViewModel;
import com.inov8.microbank.server.dao.productmodule.ProductCatalogListViewDAO;

/**
 * <p>Title: The microbank Project</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Abdul Qadeer
 * @version 1.0
 */
public class ProductCatalogListViewHibernateDAO
    extends BaseHibernateDAO<ProductCatalogListViewModel, Long,
                             ProductCatalogListViewDAO>
    implements ProductCatalogListViewDAO
{
  public ProductCatalogListViewHibernateDAO()
  {
  }

}
