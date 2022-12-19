package com.inov8.microbank.server.dao.productmodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ProductCatalogDetailModel;

public interface ProductCatalogDetailDAO
    extends BaseDAO<ProductCatalogDetailModel, Long>
{

  public void delete(Long productCatalogId);

}
