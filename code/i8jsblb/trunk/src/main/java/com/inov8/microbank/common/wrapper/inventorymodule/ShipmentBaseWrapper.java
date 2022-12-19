package com.inov8.microbank.common.wrapper.inventorymodule;

import java.util.ArrayList;

import com.inov8.framework.common.wrapper.BaseWrapper;

public interface ShipmentBaseWrapper
    extends BaseWrapper
{
  public ArrayList getProductUnitModelList();

  public void setProductUnitModelList(ArrayList productUnitModelList);
}
