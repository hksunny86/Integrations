package com.inov8.microbank.common.util;

/**
 * @author NaseerUl
 *
 */
public enum ACOwnershipTypeEnum
{
	PARTNER("Partner",1),
	DIRECTOR("Director",2),
	GUARDIAN("Guardian",3),
	SHAREHOLDER("Shareholder",4),
	SIGNATORY("Signatory",5),
	MEMBER("Member",6);

	private ACOwnershipTypeEnum(String name, int value)
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


