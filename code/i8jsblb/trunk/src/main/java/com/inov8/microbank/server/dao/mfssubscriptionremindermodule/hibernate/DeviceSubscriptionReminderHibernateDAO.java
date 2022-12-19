package com.inov8.microbank.server.dao.mfssubscriptionremindermodule.hibernate;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.mfssubscriptionremindermodule.DeviceSubReminderViewModel;
import com.inov8.microbank.server.dao.mfssubscriptionremindermodule.DeviceSubscriptionReminderDAO;


public class DeviceSubscriptionReminderHibernateDAO 
extends BaseHibernateDAO<DeviceSubReminderViewModel, Long, DeviceSubscriptionReminderDAO>
implements DeviceSubscriptionReminderDAO

{

	public void lockUnpaidAccounts( ){
		
		String lockQuery = 
			
			" update UserDeviceAccountsModel set accountLocked = 1 "+
			" where accountLocked = 0 "+
			" and (to_date(to_char(expiryDate,'DD-MON-YYYY'))) <= (to_date(to_char(sysdate,'DD-MON-YYYY'))) ";
		
		try {
			this.getHibernateTemplate().bulkUpdate(lockQuery);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings( "unchecked" )
    public CustomList<DeviceSubReminderViewModel> loadNoOfDays( SearchBaseWrapper searchBaseWrapper )
	  {
		
			Double days = (Double)searchBaseWrapper.getObject("days");
			CustomList<DeviceSubReminderViewModel> list = new CustomList<>(); 
				
			Session session = this.getSession();
            list.setResultsetList(session.createSQLQuery("select * from DEVICE_SUB_REMINDER_VIEW "
					+" where "
					+" expiry_Date between "
					+" (to_date(to_char(sysdate,'DD-MON-YYYY'))) and (to_date(to_char(sysdate,'DD-MON-YYYY')) + " + days + " ) "		
			)
			.addEntity( DeviceSubReminderViewModel.class )
			.list()) ;
			SessionFactoryUtils.releaseSession(session, getSessionFactory());
	    return list;

	  }

}
