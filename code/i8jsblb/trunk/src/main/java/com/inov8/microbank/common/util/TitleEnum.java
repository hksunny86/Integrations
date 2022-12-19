/**
 * 
 */
package com.inov8.microbank.common.util;

/**
 * @author NaseerUl
 *
 */
public enum TitleEnum
{
	MR("Mr.",1),
	MRS("Mrs.",2),
	MS("Ms.",3),
	DR("Dr.",4),
	PROF("Prof.",5),
	MISS("Miss.",6);

	private TitleEnum(String name, int value)
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
