package com.inov8.microbank.common.util;

import com.inov8.microbank.common.model.UserDeviceAccountsModel;

public class ThreadLocalUserDeviceAccounts
{
	private static ThreadLocal	userDeviceAccounts	= new ThreadLocal();

	private ThreadLocalUserDeviceAccounts()
	{
	}

	public static UserDeviceAccountsModel getUserDeviceAccountsModel()
	{
		return (UserDeviceAccountsModel) userDeviceAccounts.get();
	}

	@SuppressWarnings("unchecked")
	public static void setUserDeviceAccountsModel(UserDeviceAccountsModel userDeviceAccountsModel)
	{
		userDeviceAccounts.set(userDeviceAccountsModel);
	}

	public static void remove()
	{
		userDeviceAccounts.remove();
	}
}
