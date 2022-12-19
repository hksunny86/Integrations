package com.inov8.microbank.server.service.mfssubscriptionremindermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.mfssubscriptionremindermodule.DeviceSubReminderViewModel;
import com.inov8.microbank.server.dao.mfssubscriptionremindermodule.DeviceSubscriptionReminderDAO;


public class DeviceSubscriptionReminderManagerImpl implements DeviceSubscriptionReminderManager {

	  private DeviceSubscriptionReminderDAO deviceSubscriptionReminderDAO;
	
	  public SearchBaseWrapper getNoOfDays(SearchBaseWrapper searchBaseWrapper) throws
    	FrameworkCheckedException{
		  
		  CustomList<DeviceSubReminderViewModel> list = deviceSubscriptionReminderDAO.loadNoOfDays(searchBaseWrapper);
		  searchBaseWrapper.setCustomList(list);
		  return searchBaseWrapper;
	  }

	  public void lockUnpaidAccounts() throws
    	FrameworkCheckedException{
		  
		  deviceSubscriptionReminderDAO.lockUnpaidAccounts();
		  
	  }

	
	public void setDeviceSubscriptionReminderDAO(DeviceSubscriptionReminderDAO deviceSubscriptionReminderDAO)
	{
		this.deviceSubscriptionReminderDAO = deviceSubscriptionReminderDAO;
	}
	
}
