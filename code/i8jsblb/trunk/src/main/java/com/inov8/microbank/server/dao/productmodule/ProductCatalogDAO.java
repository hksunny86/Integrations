package com.inov8.microbank.server.dao.productmodule;

import org.springframework.dao.DataAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ProductCatalogModel;

import java.util.List;

public interface ProductCatalogDAO
    extends BaseDAO<ProductCatalogModel, Long>
{
  ProductCatalogModel loadProductCatalogModelWithDetailsById(Long
      productCatalogId) throws DataAccessException;

  List<ProductCatalogModel> fetchProductCatalog(ProductCatalogModel productCatalogModel) throws DataAccessException;

  public ProductCatalogModel loadCatalog(Long productCatalogId) throws
      FrameworkCheckedException;

  public ProductCatalogModel loadCatalogAll() throws FrameworkCheckedException;
}
