package com.inov8.microbank.common.vo;

import org.springframework.web.multipart.MultipartFile;

import com.inov8.microbank.common.model.AppUserModel;

public class BulkFilerNonFilerVO {
	
	private MultipartFile csvFile;
	private String cnic;
	private String srNo;
	private Boolean isValid;
	private String description;
	private Boolean isFiler;
	private AppUserModel appUserModel;
	private String filer;
	
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
	public Boolean getIsFiler() {
		return isFiler;
	}
	public void setIsFiler(Boolean isFiler) {
		this.isFiler = isFiler;
	}
	public AppUserModel getAppUserModel() {
		return appUserModel;
	}
	public void setAppUserModel(AppUserModel appUserModel) {
		this.appUserModel = appUserModel;
	}
	public String getFiler() {
		return filer;
	}
	public void setFiler(String filer) {
		this.filer = filer;
	}
	

}
