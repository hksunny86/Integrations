package com.inov8.microbank.server.facade;

import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentTypeManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentUploadManager;

public interface InventoryFacade
    extends ShipmentManager, ShipmentTypeManager, ProductUnitManager,
    ShipmentUploadManager
{

}
