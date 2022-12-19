package com.inov8.microbank.server.service.mfssubscriptionremindermodule;

/*
 * Author: ahsan.saleem@invo8.com.pk
 * ******
 * Date: 12-06-2007
 * */


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface DeviceSubscriptionReminderManager {
	
	  public SearchBaseWrapper getNoOfDays(SearchBaseWrapper searchBaseWrapper) throws
      	FrameworkCheckedException;

	  public void lockUnpaidAccounts() throws
      	FrameworkCheckedException;

}
