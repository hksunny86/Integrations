package com.inov8.microbank.server.service.productmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ProductIntgModuleInfoModel;
import com.inov8.microbank.server.dao.productmodule.ProductIntegrationModuleInfoDAO;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class ProductIntgModuleInfoManagerImpl
    implements ProductIntgModuleInfoManager
{
  private ProductIntegrationModuleInfoDAO productIntegrationModuleInfoDAO;

  /**
   * loadProductIntgModuleInfo
   *
   * @param baseWrapper BaseWrapper
   * @return BaseWrapper
   * @throws FrameworkCheckedException
   */
  public BaseWrapper loadProductIntgModuleInfo(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    ProductIntgModuleInfoModel productIntgModuleInfoModel = (ProductIntgModuleInfoModel)this.
        productIntegrationModuleInfoDAO.findByPrimaryKey( (baseWrapper.getBasePersistableModel()).
        getPrimaryKey());
    baseWrapper.setBasePersistableModel(productIntgModuleInfoModel);

    return baseWrapper;
  }

  /**
   * loadProductIntgModuleInfo
   *
   * @param searchBaseWrapper SearchBaseWrapper
   * @return SearchBaseWrapper
   * @throws FrameworkCheckedException
   */
  public SearchBaseWrapper loadProductIntgModuleInfo(SearchBaseWrapper
      searchBaseWrapper) throws FrameworkCheckedException
  {
    ProductIntgModuleInfoModel productIntgModuleInfoModel = (ProductIntgModuleInfoModel)this.productIntegrationModuleInfoDAO.findByPrimaryKey( (
        searchBaseWrapper.getBasePersistableModel()).getPrimaryKey());
    searchBaseWrapper.setBasePersistableModel(productIntgModuleInfoModel);

    return searchBaseWrapper;
  }

  public void setProductIntegrationModuleInfoDAO(
      ProductIntegrationModuleInfoDAO productIntegrationModuleInfoDAO)
  {
    this.productIntegrationModuleInfoDAO = productIntegrationModuleInfoDAO;
  }
}

