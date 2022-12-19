package com.inov8.microbank.common.util;

public enum ErrorLevel 
{
	LOW(1), MEDIUM(2), HIGH(3),CRITICAL(4);
	
	private int level;
	
	private ErrorLevel(int level)
	{
		this.level = level;
	}
		
	public int level()
	{
		return this.level;
	}

}
