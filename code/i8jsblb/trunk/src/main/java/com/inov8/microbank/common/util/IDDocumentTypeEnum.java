


package com.inov8.microbank.common.util;

/**
 * @author NaseerUl
 *
 */
public enum IDDocumentTypeEnum
{
	CNIC("CNIC",1),
	NICOP("NICOP",2),
	PASSPORT("Passport",3),
	NARA("NARA",4),
	POC("POC",5),
	SNIC("SNIC",6);

	private IDDocumentTypeEnum(String name, int value)
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


