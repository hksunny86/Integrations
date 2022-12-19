package com.inov8.microbank.common.wrapper.inventorymodule;

import java.util.ArrayList;

import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.ProductUnitModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Asad Hayat
 * @version 1.0
 */
public class ShipmentBaseWrapperImpl
    extends BaseWrapperImpl

    implements ShipmentBaseWrapper
{
  private ArrayList<ProductUnitModel> productUnitModelList;
  public ShipmentBaseWrapperImpl()
  {

  }

  public ArrayList getProductUnitModelList()
  {
    return productUnitModelList;
  }

  public void setProductUnitModelList(ArrayList productUnitModelList)
  {
    this.productUnitModelList = productUnitModelList;
  }
}
