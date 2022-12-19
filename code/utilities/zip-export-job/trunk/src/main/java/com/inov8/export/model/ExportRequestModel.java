package com.inov8.export.model;

import java.util.Date;

public class ExportRequestModel {
	
	
	Long exportRequestId;
	Long reportId;
	String reportView;
	String exportQuery;
	String totalsQuery;
	String packageCall;
	Date fromDate;
	Date toDate;
	String dateType;
	Long statusId;
	String username;
	String email;
	Date createdon;
	Date updatedOn;
	Long createdBy;
	Long updatedBy;
	Long versionNo;
	Long accountId;
	
	public Long getExportRequestId() {
		return exportRequestId;
	}
	public void setExportRequestId(Long exportRequestId) {
		this.exportRequestId = exportRequestId;
	}
	public Long getReportId() {
		return reportId;
	}
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	public String getReportView() {
		return reportView;
	}
	public void setReportView(String reportView) {
		this.reportView = reportView;
	}
	public String getExportQuery() {
		return exportQuery;
	}
	public void setExportQuery(String exportQuery) {
		this.exportQuery = exportQuery;
	}
	public String getTotalsQuery() {
		return totalsQuery;
	}
	public void setTotalsQuery(String totalsQuery) {
		this.totalsQuery = totalsQuery;
	}
	public String getPackageCall() {
		return packageCall;
	}
	public void setPackageCall(String packageCall) {
		this.packageCall = packageCall;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCreatedon() {
		return createdon;
	}
	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Long getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(Long versionNo) {
		this.versionNo = versionNo;
	}
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
}
