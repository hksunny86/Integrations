package com.inov8.microbank.server.service.inventorymodule;

import com.inov8.microbank.server.dao.inventorymodule.ShipmentTypeDAO;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */

public class ShipmentTypeManagerImpl
    implements ShipmentTypeManager
{

  private ShipmentTypeDAO shipmentTypeDAO;

  public void setShipmentTypeDAO(ShipmentTypeDAO shipmentTypeDAO)
  {
    this.shipmentTypeDAO = shipmentTypeDAO;

  }

}
