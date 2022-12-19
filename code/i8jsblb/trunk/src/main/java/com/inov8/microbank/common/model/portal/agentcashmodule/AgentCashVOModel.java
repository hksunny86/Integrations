package com.inov8.microbank.common.model.portal.agentcashmodule;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class AgentCashVOModel implements Serializable{

	private static final long serialVersionUID = 2986518026326524339L;
	
	private MultipartFile csvFile;

    
	public MultipartFile getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(MultipartFile csvFile) {
		this.csvFile = csvFile;
	}
	
	

}
