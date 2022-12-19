package com.inov8.microbank.server.dao.allpaymodule.hibernate;

import java.util.List;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.DistHeadReportListViewModel;
import com.inov8.microbank.server.dao.allpaymodule.DistHeadReportListViewDAO;

public class DistHeadReportListViewHibernateDAO 
extends BaseHibernateDAO<DistHeadReportListViewModel, Long, DistHeadReportListViewDAO>
implements DistHeadReportListViewDAO
{
	
	public List<DistHeadReportListViewModel> getReport( SearchBaseWrapper searchBaseWrapper )
	{
		DistHeadReportListViewModel distHead = (DistHeadReportListViewModel)searchBaseWrapper.getBasePersistableModel() ;
		
		
		String hql = "FROM DistHeadReportListViewModel dhr WHERE 	dhr.distributorContactId = ? " ;
//	        + " sm.relationProductIdProductModel.productId = pm.productId AND sm.purchaseDate = ( select MIN(smm.purchaseDate) from ShipmentModel smm"
//	        + " where smm.active = true and smm.outstandingCredit >= ?"
//	        + " and smm.relationProductIdProductModel.productId = pm.productId and Decode( smm.expiryDate, null, sysdate , to_date(smm.expiryDate)) >= sysdate ) " ;


		return this.getHibernateTemplate().find(hql, new Object[]{distHead.getDistributorContactId()}) ;

	}
	
}
