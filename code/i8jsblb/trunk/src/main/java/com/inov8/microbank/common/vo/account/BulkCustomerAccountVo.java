package com.inov8.microbank.common.vo.account;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.springframework.web.multipart.MultipartFile;

import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.Level2AccountModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.ApplicantTypeConstants;

@XmlRootElement(name="bulkCustomerAccountVo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class BulkCustomerAccountVo
{
	private Long customerAccountTypeId;
	
	private Long segmentId;

	@XmlTransient
	private MultipartFile csvFile;

	private String name;

	private String mobileNo;

	private String cnic;

	private String initialAppFormNo;
	@XmlTransient
	private Boolean isLevel2;

	@XmlTransient
	private String cnicExpiryDateStr;

	private Date cnicExpiryDate;

	private Long createdBy;

	@XmlTransient
	private Boolean validRecord;
	@XmlTransient
	private int invalidRecordsCount;

	public BulkCustomerAccountVo()
	{

	}

	public MfsAccountModel toMfsAccountModel()
	{
		MfsAccountModel mfsAccountModel = new MfsAccountModel();
		mfsAccountModel.setCustomerAccountTypeId(customerAccountTypeId);
		mfsAccountModel.setSegmentId(segmentId);
		mfsAccountModel.setName(name);

		String[] nameArray = mfsAccountModel.getName().split(" ");
		mfsAccountModel.setFirstName(nameArray[0]);
		if(nameArray.length > 1)
		{
			mfsAccountModel.setLastName(mfsAccountModel.getName().substring(mfsAccountModel.getFirstName().length()+1));
		}
		else
		{
			mfsAccountModel.setLastName(nameArray[0]);
		}

		mfsAccountModel.setMobileNo(mobileNo);
		mfsAccountModel.setNic(cnic);
		mfsAccountModel.setNicExpiryDate(cnicExpiryDate);
		
		return mfsAccountModel;
	}

	public Level2AccountModel toLevel2AccountModel()
	{
		Level2AccountModel mfsAccountModel = new Level2AccountModel();
		mfsAccountModel.setCustomerAccountTypeId(customerAccountTypeId);
		mfsAccountModel.setSegmentId(segmentId);
		mfsAccountModel.setMobileNo(mobileNo);

		ApplicantDetailModel applicant1DetailModel = new ApplicantDetailModel();
		applicant1DetailModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
		applicant1DetailModel.setName(name);
		//applicant1DetailModel.setBuisnessName(name);
	//	applicant1DetailModel.setNic(cnic);
		applicant1DetailModel.setContactNo(mobileNo);
	//	applicant1DetailModel.setNicExpiryDate(cnicExpiryDate);
		mfsAccountModel.setApplicant1DetailModel(applicant1DetailModel);
		mfsAccountModel.setInitialAppFormNo(initialAppFormNo);

		return mfsAccountModel;
	}

	public Long getCustomerAccountTypeId()
	{
		return customerAccountTypeId;
	}
	
	public void setCustomerAccountTypeId(Long customerAccountTypeId)
	{
		this.customerAccountTypeId = customerAccountTypeId;
	}
	
	public Long getSegmentId()
	{
		return segmentId;
	}
	
	public void setSegmentId(Long segmentId)
	{
		this.segmentId = segmentId;
	}

	public MultipartFile getCsvFile()
	{
		return csvFile;
	}

	public void setCsvFile(MultipartFile csvFile)
	{
		this.csvFile = csvFile;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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

	public String getCnicExpiryDateStr()
	{
		return cnicExpiryDateStr;
	}

	public void setCnicExpiryDateStr(String cnicExpiryDateStr)
	{
		this.cnicExpiryDateStr = cnicExpiryDateStr;
	}

	public void setCnicExpiryDate(Date cnicExpiryDate)
	{
		this.cnicExpiryDate = cnicExpiryDate;
	}

	public Date getCnicExpiryDate()
	{
		return cnicExpiryDate;
	}

	public void setNicExpiryDate(Date nicExpiryDate)
	{
		this.cnicExpiryDate = nicExpiryDate;
	}

	public Long getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(Long createdBy)
	{
		this.createdBy = createdBy;
	}

	public Boolean getValidRecord()
	{
		return validRecord;
	}

	public void setValidRecord(Boolean validRecord)
	{
		this.validRecord = validRecord;
	}

	public int getInvalidRecordsCount()
	{
		return invalidRecordsCount;
	}

	public void setInvalidRecordsCount(int invalidRecordsCount)
	{
		this.invalidRecordsCount = invalidRecordsCount;
	}

	public String getInitialAppFormNo() {
		return initialAppFormNo;
	}

	public void setInitialAppFormNo(String initialAppFormNo) {
		this.initialAppFormNo = initialAppFormNo;
	}

	public Boolean getIsLevel2() {
		return isLevel2;
	}

	public void setIsLevel2(Boolean isLevel2) {
		this.isLevel2 = isLevel2;
	}

}
