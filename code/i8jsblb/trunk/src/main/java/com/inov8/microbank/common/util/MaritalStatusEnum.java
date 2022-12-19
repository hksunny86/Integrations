package com.inov8.microbank.common.util;

/**
 * @author NaseerUl
 *
 */
public enum MaritalStatusEnum
{
	SINGLE("Single",1),
	MARRIED("Married",2),
	WIDOWED("Widowed",3),
	SEPERATED("Seperated",4),
	DIVORCED("Divorced",5);

	private MaritalStatusEnum(String name, int value)
	{
		this.name = name;
		this.value = value;
	}

	private String name;
	private int value;

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
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

}

