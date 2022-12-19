package com.inov8.microbank.common.util;


public class ThreadLocalTBR {
	private static ThreadLocal	tillBalanceRequired	= new ThreadLocal();

	private ThreadLocalTBR()
	{
	}

	public static Long getTBR()
	{
		return (Long) tillBalanceRequired.get();
	}

	@SuppressWarnings("unchecked")
	public static void setTBR(Long tbr)
	{
		tillBalanceRequired.set(tbr);
	}

	public static void remove()
	{
		tillBalanceRequired.remove();
	}
}
