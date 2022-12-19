package com.inov8.microbank.common.vo.dayendsettlement;

import java.util.Date;

public class DayEndSettlementVo implements Comparable<DayEndSettlementVo>
{
	private Date createdOn;

	private String fileName;

	private String fileType;

	public DayEndSettlementVo()
	{
	}

	public DayEndSettlementVo(Date createdOn, String fileName, String fileType)
	{
		super();
		this.createdOn = createdOn;
		this.fileName = fileName;
		this.fileType = fileType;
	}

	@Override
	public int compareTo(DayEndSettlementVo that)
	{
		return that.getCreatedOn().compareTo(this.createdOn);
	}

	public Date getCreatedOn()
	{
		return createdOn;
	}

	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}
