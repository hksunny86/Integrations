package com.inov8.microbank.common.util;

/**
 * @author Rizwan ur Rehman 
 * Creation Time: Dec 15, 2006 3:05:23 PM
 */


public class ThreadLocalActionLog
{
	private static ThreadLocal	actionLogId	= new ThreadLocal();

	private ThreadLocalActionLog()
	{
	}

	public static Long getActionLogId()
	{
		return (Long)actionLogId.get();
	}

	@SuppressWarnings("unchecked")
	public static void setActionLogId(Long actionLogModelId)
	{
		actionLogId.set(actionLogModelId);
	}

	public static void remove()
	{
		actionLogId.remove();
	}
}