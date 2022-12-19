package com.inov8.microbank.server.service.productmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

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
public interface ProductCatalogManager
{
  public BaseWrapper createCatalog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper updateCatalog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper searchCatalog(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper loadCatalog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper loadCatalog(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper loadCatalogProducts(BaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper loadAllCatalogs() throws
      FrameworkCheckedException;

  public BaseWrapper deactivateCatalog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper includeProductInCatalogAll(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper excludeProductInCatalogAll(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
  public BaseWrapper updateCatalogActivateDeActivate(BaseWrapper baseWrapper) throws
  FrameworkCheckedException;
  
  public void updateDefaultCatalogsVersion(BaseWrapper baseWrapper) throws FrameworkCheckedException;
  
  boolean isAssociatedWithAgentOrHandler(Long productCatalogId) throws FrameworkCheckedException;

  boolean isProductExistInCatalog(Long productCatalogId, long productId);
}
