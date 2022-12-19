package com.inov8.microbank.common.model.commissionmodule;

public class ExtendedCommShAcctsListViewModel extends CommShAcctsListViewModel{
	
	private Long commStakeHolderId;

	@javax.persistence.Transient
	public Long getCommStakeHolderId() {
		return commStakeHolderId;
	}

	public void setCommStakeHolderId(Long commStakeHolderId) {
		this.commStakeHolderId = commStakeHolderId;
	}
	
	

}
