package com.inov8.microbank.common.model.home;

import java.io.Serializable;

public class HomeSearchModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8244948327760283277L;

	private boolean searchCriteria;
	private String customerSearch;
	private String agentSearch;
	private String handlerSearch;
	
	public HomeSearchModel() {
		
	}

	public boolean getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(boolean searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public String getCustomerSearch() {
		return customerSearch;
	}

	public void setCustomerSearch(String customerSearch) {
		this.customerSearch = customerSearch;
	}

	public String getAgentSearch() {
		return agentSearch;
	}

	public void setAgentSearch(String agentSearch) {
		this.agentSearch = agentSearch;
	}

	public String getHandlerSearch() {
		return handlerSearch;
	}

	public void setHandlerSearch(String handlerSearch) {
		this.handlerSearch = handlerSearch;
	}
	
}
