package com.inov8.microbank.server.service.productmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.ProductIntgVoModel;
import com.inov8.microbank.server.dao.productmodule.ProductIntgVODAO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class ProductIntgVOManagerImpl
    implements ProductIntgVOManager
{
  private ProductIntgVODAO productIntgVODAO;
  /**
   * loadProductIntgVO
   *
   * @param baseWrapper BaseWrapper
   * @return BaseWrapper
   * @throws FrameworkCheckedException
   * @todo Implement this com.inov8.microbank.server.service.productmodule.ProductIntgVOManager method
   */
  public BaseWrapper loadProductIntgVO(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    ProductIntgVoModel productIntgVoModel = (ProductIntgVoModel)this.
        productIntgVODAO.findByPrimaryKey( (baseWrapper.getBasePersistableModel()).getPrimaryKey());
    baseWrapper.setBasePersistableModel(productIntgVoModel);

    return baseWrapper;

  }

  public void setProductIntgVODAO(ProductIntgVODAO productIntgVODAO)
  {

    this.productIntgVODAO = productIntgVODAO;
  }
}
