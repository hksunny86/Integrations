package com.inov8.microbank.server.dao.mfssubscriptionremindermodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.mfssubscriptionremindermodule.DeviceSubReminderViewModel;

public interface DeviceSubscriptionReminderDAO extends BaseDAO<DeviceSubReminderViewModel, Long> 

{
	CustomList<DeviceSubReminderViewModel>  loadNoOfDays(SearchBaseWrapper searchBaseWrapper) ;
	 void lockUnpaidAccounts();
}
