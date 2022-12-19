/**
 * 
 */
package com.inov8.microbank.disbursement.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.inov8.microbank.common.model.BulkDisbursementsModel;

/**
 * @author NaseerUl
 *
 */
@XmlRootElement(name="bulkDisbursementsModel")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class BulkDisbursementsXmlVo
{
	@XmlElement(name="id")
	private Long bulkDisbursementsId;
	@XmlElement(name="mobNo")
	private String mobileNo;
	private String cnic;
	private Long createdBy;

	public BulkDisbursementsXmlVo()
	{
	}
	


	public BulkDisbursementsXmlVo(Long bulkDisbursementsId, String mobileNo, String cnic, Long createdBy) {

		this.bulkDisbursementsId = bulkDisbursementsId;
		this.mobileNo = mobileNo;
		this.cnic = cnic;
		this.createdBy = createdBy;
	}	

	public BulkDisbursementsModel toBulkDisbursementsModel()
	{
		BulkDisbursementsModel model = new BulkDisbursementsModel();
		model.setBulkDisbursementsId(bulkDisbursementsId);
		model.setCreatedBy(createdBy);
		return model;
	}

	public Long getBulkDisbursementsId()
	{
		return bulkDisbursementsId;
	}

	public void setBulkDisbursementsId(Long bulkDisbursementsId)
	{
		this.bulkDisbursementsId = bulkDisbursementsId;
	}

	public String getMobileNo()
	{
		return mobileNo;
	}

	public void setMobileNo(String mobileNo)
	{
		this.mobileNo = mobileNo;
	}

	public String getCnic()
	{
		return cnic;
	}

	public void setCnic(String cnic)
	{
		this.cnic = cnic;
	}

	public Long getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(Long createdBy)
	{
		this.createdBy = createdBy;
	}

}
