/**
 * 
 */
package com.inov8.microbank.common.util;

import com.inov8.microbank.common.model.AppUserModel;

/**
 * @author imran.sarwar 
 * Creation Time: Oct 4, 2006 3:43:23 PM
 */
public class ThreadLocalAppUser
{

	private static ThreadLocal	appUser	= new ThreadLocal();

	private ThreadLocalAppUser()
	{
	}

	public static AppUserModel getAppUserModel()
	{
		return (AppUserModel) appUser.get();
	}

	@SuppressWarnings("unchecked")
	public static void setAppUserModel(AppUserModel appUserModel)
	{
		appUser.set(appUserModel);
	}

	public static void remove()
	{
		appUser.remove();
	}

}