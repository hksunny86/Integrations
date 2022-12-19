package com.inov8.microbank.common.model.portal.authorizationreferencedata;

import org.codehaus.jackson.map.annotate.JsonSerialize;



@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CommissionShSharesDefaultModelVO {
	 
	private Long commissionShSharesDefaultId;
	private Long productIdPlain;
	private Long commissionStakeholderIdPlain;
	private Double commissionShare;
	private Boolean isWhtApplicable;
	
	public CommissionShSharesDefaultModelVO() {
		
	}
	
	public Long getProductIdPlain() {
		return productIdPlain;
	}
	public void setProductIdPlain(Long productIdPlain) {
		this.productIdPlain = productIdPlain;
	}
	public Long getCommissionStakeholderIdPlain() {
		return commissionStakeholderIdPlain;
	}
	public void setCommissionStakeholderIdPlain(Long commissionStakeholderIdPlain) {
		this.commissionStakeholderIdPlain = commissionStakeholderIdPlain;
	}
	public Double getCommissionShare() {
		return commissionShare;
	}
	public void setCommissionShare(Double commissionShare) {
		this.commissionShare = commissionShare;
	}

	public Boolean getIsWhtApplicable() {
		return isWhtApplicable;
	}

	public void setIsWhtApplicable(Boolean isWhtApplicable) {
		this.isWhtApplicable = isWhtApplicable;
	}

	public Long getCommissionShSharesDefaultId() {
		return commissionShSharesDefaultId;
	}

	public void setCommissionShSharesDefaultId(Long commissionShSharesDefaultId) {
		this.commissionShSharesDefaultId = commissionShSharesDefaultId;
	}

}