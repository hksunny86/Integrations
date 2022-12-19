package com.inov8.microbank.common.model.commissionmodule;

public class ExtendedCommissionShSharesViewModel extends CommissionShSharesViewModel{

	private static final long serialVersionUID = -1184164552795405581L;
	
	private boolean isWhtApplicable;
	private boolean isFedApplicable;

	
	@javax.persistence.Transient
	public boolean getIsWhtApplicable() {
		return isWhtApplicable;
	}

	public void setIsWhtApplicable(boolean isWhtApplicable) {
		this.isWhtApplicable = isWhtApplicable;
	}

	@javax.persistence.Transient
	public boolean getIsFedApplicable() {
		return isFedApplicable;
	}

	public void setIsFedApplicable(boolean isFedApplicable) {
		this.isFedApplicable = isFedApplicable;
	}

}
