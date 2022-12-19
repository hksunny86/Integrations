package com.inov8.microbank.server.service.switchmodule.iris.model;

import java.io.Serializable;

public class CompanyVO implements Serializable {
	
	private String companyShortName;
	private String companyName;
	
	public String getCompanyShortName() {
		return companyShortName;
	}
	public void setCompanyShortName(String companyShortName) {
		this.companyShortName = companyShortName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
