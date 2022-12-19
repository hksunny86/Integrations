package com.inov8.microbank.server.dao.inventorymodule.hibernate;

import java.util.List;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ShipmentModel;
import com.inov8.microbank.server.dao.inventorymodule.ShipmentDAO;

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

public class ShipmentHibernateDAO
    extends BaseHibernateDAO<ShipmentModel, Long, ShipmentDAO>
    implements ShipmentDAO
{
  private String hqlUpdate = "update ShipmentModel s set s.quantity = ? , s.outstandingCredit=? where s.shipmentId = ?";
  public void updateShipment(Long id, Long quantity, Double outstandingCredit)
  {
    this.getHibernateTemplate().bulkUpdate(hqlUpdate, new Object[]
                                           {quantity, outstandingCredit, id});
  }


  public List getVariableProdShipment( Long productId, Double txAmount )
  {
    String hql = "SELECT sm.shipmentId FROM ShipmentModel sm, ProductModel pm WHERE 	pm.productId = ? and"
        + " sm.relationProductIdProductModel.productId = pm.productId AND sm.purchaseDate = ( select MIN(smm.purchaseDate) from ShipmentModel smm"
        + " where smm.active = true and smm.outstandingCredit >= ?"
        + " and smm.relationProductIdProductModel.productId = pm.productId and Decode( smm.expiryDate, null, sysdate , to_date(smm.expiryDate)) >= sysdate ) " ;

    return this.getHibernateTemplate().find(hql, new Object[]{productId, txAmount}) ;

  }




}
