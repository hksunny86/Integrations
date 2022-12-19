package com.inov8.microbank.common.model.customermodule;

import java.beans.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.inov8.framework.common.model.BasePersistableModel;

public class BulkUpdateCustomerScreeningModel extends BasePersistableModel{

	private MultipartFile csvFile;
	private String cnic;
	private String srNo;
	private Boolean isValid;
	private String description;
	private String customerId;
	private Boolean isScreeningPerformed;
	private String screeningPerformed;
	
	public MultipartFile getCsvFile() {
		return csvFile;
	}
	public void setCsvFile(MultipartFile csvFile) {
		this.csvFile = csvFile;
	}
	public String getCnic() {
		return cnic;
	}
	public void setCnic(String cnic) {
		this.cnic = cnic;
	}
	
	public String getSrNo() {
		return srNo;
	}
	public void setSrNo(String srNo) {
		this.srNo = srNo;
	}
	public Boolean getIsValid() {
		return isValid;
	}
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public Boolean getIsScreeningPerformed() {
		return isScreeningPerformed;
	}
	public void setIsScreeningPerformed(Boolean isScreeningPerformed) {
		this.isScreeningPerformed = isScreeningPerformed;
	}
	@Transient
	@Override
	public Long getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transient
	@Override
	public String getPrimaryKeyFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transient
	@Override
	public String getPrimaryKeyParameter() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setPrimaryKey(Long arg0) {
		// TODO Auto-generated method stub
		
	}
	public String getScreeningPerformed() {
		return screeningPerformed;
	}
	public void setScreeningPerformed(String screeningPerformed) {
		this.screeningPerformed = screeningPerformed;
	}
}
