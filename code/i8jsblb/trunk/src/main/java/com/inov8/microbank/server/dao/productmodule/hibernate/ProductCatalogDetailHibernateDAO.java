package com.inov8.microbank.server.dao.productmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ProductCatalogDetailModel;
import com.inov8.microbank.server.dao.productmodule.ProductCatalogDetailDAO;

public class ProductCatalogDetailHibernateDAO
    extends BaseHibernateDAO<ProductCatalogDetailModel, Long,
                             ProductCatalogDetailDAO>
    implements ProductCatalogDetailDAO
{

  //private String hqlDelete = "delete ProductCatalogueDetailModel pCd where pCd.relationProductCatalogueIdProductCatalogueModel.productCatalogueId = ?";
  public void delete(Long productCatalogId)
  {

    String hqlDelete = "delete ProductCatalogDetailModel pCd where pCd.relationProductCatalogIdProductCatalogModel.id = " +
        productCatalogId;
    this.getHibernateTemplate().bulkUpdate(hqlDelete);
  }

}
