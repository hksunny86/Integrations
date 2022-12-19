package com.inov8.microbank.common.model.portal.authorizationreferencedata;

public class UsecaseLevelRefDataModel {

	private Long usecaseLevelId;
	private Long levelNo;
	private Long usecaseId;
	private Boolean intimateOnly;
	
	public UsecaseLevelRefDataModel() {
		// TODO Auto-generated constructor stub
	}

	public Long getUsecaseLevelId() {
		return usecaseLevelId;
	}

	public void setUsecaseLevelId(Long usecaseLevelId) {
		this.usecaseLevelId = usecaseLevelId;
	}

	public Long getLevelNo() {
		return levelNo;
	}

	public void setLevelNo(Long levelNo) {
		this.levelNo = levelNo;
	}

	public Long getUsecaseId() {
		return usecaseId;
	}

	public void setUsecaseId(Long usecaseId) {
		this.usecaseId = usecaseId;
	}

	public Boolean getIntimateOnly() {
		return intimateOnly;
	}

	public void setIntimateOnly(Boolean intimateOnly) {
		this.intimateOnly = intimateOnly;
	}

}
