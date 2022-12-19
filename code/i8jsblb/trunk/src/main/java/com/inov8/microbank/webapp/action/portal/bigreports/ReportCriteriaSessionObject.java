package com.inov8.microbank.webapp.action.portal.bigreports;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public class ReportCriteriaSessionObject {

	public static final String REPORT_CRITERIA_SESSION_OBJ_TRX_DET_MSTR = "REPORT_CRITERIA_SESSION_OBJ_TRX_DET_MSTR";
	public static final String REPORT_CRITERIA_SESSION_OBJ_BB_CUS_ACC = "REPORT_CRITERIA_SESSION_OBJ_BB_CUS_ACC";
	
	private SearchBaseWrapper searchBaseWrapper;
	private String fileName;
	
	public ReportCriteriaSessionObject() {}
	
	public ReportCriteriaSessionObject(SearchBaseWrapper searchBaseWrapper) {
		
		this.searchBaseWrapper = searchBaseWrapper;
	}	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public SearchBaseWrapper getSearchBaseWrapper() {
		return searchBaseWrapper;
	}

	public void setSearchBaseWrapper(SearchBaseWrapper searchBaseWrapper) {
		this.searchBaseWrapper = searchBaseWrapper;
	}	
}