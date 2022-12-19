package com.inov8.microbank.server.dao.inventorymodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ShipmentModel;

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

public interface ShipmentDAO
    extends BaseDAO<ShipmentModel, Long>
{
  void updateShipment(Long id, Long quantity, Double outstandingCredit);
  List getVariableProdShipment( Long productId, Double txAmount ) ;
}
