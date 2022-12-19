/**
 * 
 */
package com.inov8.microbank.common.util;

/**
 * @author NaseerUl
 *
 */
public enum AccountTypeCategoryEnum
{
	AGENT("Agent",0),
	CUSTOMER("Customer",1);

	private AccountTypeCategoryEnum(String name, int index)
	{
		this.name = name;
		this.index = index;
	}

	private String name;
	private int index;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

}
