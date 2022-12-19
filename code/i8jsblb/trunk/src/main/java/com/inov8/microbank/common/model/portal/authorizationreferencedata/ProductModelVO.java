package com.inov8.microbank.common.model.portal.authorizationreferencedata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.inov8.framework.common.model.BasePersistableModel;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProductModelVO extends BasePersistableModel implements Serializable {

	 private static final long serialVersionUID = 7492690995295841238L;
	
	 private Long productId;
	 private String name;
	 private String description;
	 private Boolean active;
	 private Boolean taxable;
	 private Date updatedOn;
	 private Date createdOn;
	 private Integer versionNo;
	 private String productCode;
	 private Double minLimit;
	 private Double maxLimit;
	 private Boolean inclChargesCheck;
	 private String consumerLabel;
	 private String consumerInputType;
	 private Boolean amtRequired;
	 private Long multiples;
	 private String accountNo;
	 private String accountNick;
	 private Long appUserTypeId;
	 
	 private Double exclusiveFixAmount;
	 private Double exclusivePercentAmount;
	 private Double inclusiveFixAmount;
	 private Double inclusivePercentAmount;
	 
	 private Long supplierId;
	 private Long serviceId;
	 private Long taxRegimeId;
	 private Long wHTConfigId;	
	 private Long commissionStakeholderId;
	 private Long createdByID;
	 private Long updatedByID;  
	 
	 private Long productIntgModuleInfoId;
	 private Long productIntgVoId;
	 private Long instructionId;
	 private Long successMessageId;
	 private Long failureMessageId;
	 private Long helpLine;
	 private Double costPrice;
	 private Double unitPrice;
	 private Double fixedDiscount;
	 private Double percentDiscount;
	 private Long minimumStockLevel;
	 private Boolean batchMode;
	 private Boolean doValidate;
	 private String categoryCode;
	 private Long categoryId;
	 private String supplierName;
	 private Boolean checked;
	 private String serviceName;
	 private Integer uspProductidCheck;
	 
	 private Boolean isWithHolding;
	 private Boolean isFed;
	 private Double withHoldingShare;
	 private Double fedShare;
	 
	 private List<CommissionShSharesDefaultModelVO> productIdCommissionShSharesDefaultModelList = new ArrayList<CommissionShSharesDefaultModelVO>();


	public ProductModelVO() {
	}
	
	
	
	

	@Override
	public Long getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrimaryKeyFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrimaryKeyParameter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrimaryKey(Long arg0) {
		// TODO Auto-generated method stub
		
	}

	public List<CommissionShSharesDefaultModelVO> getProductIdCommissionShSharesDefaultModelList() {
		return productIdCommissionShSharesDefaultModelList;
	}

	public void setProductIdCommissionShSharesDefaultModelList(
			List<CommissionShSharesDefaultModelVO> productIdCommissionShSharesDefaultModelList) {
		this.productIdCommissionShSharesDefaultModelList = productIdCommissionShSharesDefaultModelList;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getTaxable() {
		return taxable;
	}

	public void setTaxable(Boolean taxable) {
		this.taxable = taxable;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Double getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(Double minLimit) {
		this.minLimit = minLimit;
	}

	public Double getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(Double maxLimit) {
		this.maxLimit = maxLimit;
	}

	public Boolean getInclChargesCheck() {
		return inclChargesCheck;
	}

	public void setInclChargesCheck(Boolean inclChargesCheck) {
		this.inclChargesCheck = inclChargesCheck;
	}

	public String getConsumerLabel() {
		return consumerLabel;
	}

	public void setConsumerLabel(String consumerLabel) {
		this.consumerLabel = consumerLabel;
	}

	public String getConsumerInputType() {
		return consumerInputType;
	}

	public void setConsumerInputType(String consumerInputType) {
		this.consumerInputType = consumerInputType;
	}

	public Boolean getAmtRequired() {
		return amtRequired;
	}

	public void setAmtRequired(Boolean amtRequired) {
		this.amtRequired = amtRequired;
	}

	public Long getMultiples() {
		return multiples;
	}

	public void setMultiples(Long multiples) {
		this.multiples = multiples;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountNick() {
		return accountNick;
	}

	public void setAccountNick(String accountNick) {
		this.accountNick = accountNick;
	}

	public Double getExclusiveFixAmount() {
		return exclusiveFixAmount;
	}

	public void setExclusiveFixAmount(Double exclusiveFixAmount) {
		this.exclusiveFixAmount = exclusiveFixAmount;
	}

	public Double getExclusivePercentAmount() {
		return exclusivePercentAmount;
	}

	public void setExclusivePercentAmount(Double exclusivePercentAmount) {
		this.exclusivePercentAmount = exclusivePercentAmount;
	}

	public Double getInclusiveFixAmount() {
		return inclusiveFixAmount;
	}

	public void setInclusiveFixAmount(Double inclusiveFixAmount) {
		this.inclusiveFixAmount = inclusiveFixAmount;
	}

	public Double getInclusivePercentAmount() {
		return inclusivePercentAmount;
	}

	public void setInclusivePercentAmount(Double inclusivePercentAmount) {
		this.inclusivePercentAmount = inclusivePercentAmount;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Long getTaxRegimeId() {
		return taxRegimeId;
	}

	public void setTaxRegimeId(Long taxRegimeId) {
		this.taxRegimeId = taxRegimeId;
	}

	public Long getwHTConfigId() {
		return wHTConfigId;
	}

	public void setwHTConfigId(Long wHTConfigId) {
		this.wHTConfigId = wHTConfigId;
	}

	public Long getCommissionStakeholderId() {
		return commissionStakeholderId;
	}

	public void setCommissionStakeholderId(Long commissionStakeholderId) {
		this.commissionStakeholderId = commissionStakeholderId;
	}

	public Long getCreatedByID() {
		return createdByID;
	}

	public void setCreatedByID(Long createdByID) {
		this.createdByID = createdByID;
	}

	public Long getUpdatedByID() {
		return updatedByID;
	}

	public void setUpdatedByID(Long updatedByID) {
		this.updatedByID = updatedByID;
	}





	public Long getProductIntgModuleInfoId() {
		return productIntgModuleInfoId;
	}





	public void setProductIntgModuleInfoId(Long productIntgModuleInfoId) {
		this.productIntgModuleInfoId = productIntgModuleInfoId;
	}





	public Long getProductIntgVoId() {
		return productIntgVoId;
	}





	public void setProductIntgVoId(Long productIntgVoId) {
		this.productIntgVoId = productIntgVoId;
	}





	public Long getInstructionId() {
		return instructionId;
	}





	public void setInstructionId(Long instructionId) {
		this.instructionId = instructionId;
	}





	public Long getSuccessMessageId() {
		return successMessageId;
	}





	public void setSuccessMessageId(Long successMessageId) {
		this.successMessageId = successMessageId;
	}





	public Long getFailureMessageId() {
		return failureMessageId;
	}





	public void setFailureMessageId(Long failureMessageId) {
		this.failureMessageId = failureMessageId;
	}





	public Long getHelpLine() {
		return helpLine;
	}





	public void setHelpLine(Long helpLine) {
		this.helpLine = helpLine;
	}





	public Double getCostPrice() {
		return costPrice;
	}





	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}





	public Double getUnitPrice() {
		return unitPrice;
	}





	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}





	public Double getFixedDiscount() {
		return fixedDiscount;
	}





	public void setFixedDiscount(Double fixedDiscount) {
		this.fixedDiscount = fixedDiscount;
	}





	public Double getPercentDiscount() {
		return percentDiscount;
	}





	public void setPercentDiscount(Double percentDiscount) {
		this.percentDiscount = percentDiscount;
	}





	public Long getMinimumStockLevel() {
		return minimumStockLevel;
	}





	public void setMinimumStockLevel(Long minimumStockLevel) {
		this.minimumStockLevel = minimumStockLevel;
	}





	public Boolean getBatchMode() {
		return batchMode;
	}





	public void setBatchMode(Boolean batchMode) {
		this.batchMode = batchMode;
	}





	public Boolean getDoValidate() {
		return doValidate;
	}





	public void setDoValidate(Boolean doValidate) {
		this.doValidate = doValidate;
	}





	public Integer getUspProductidCheck() {
		return uspProductidCheck;
	}





	public void setUspProductidCheck(Integer uspProductidCheck) {
		this.uspProductidCheck = uspProductidCheck;
	}





	public String getCategoryCode() {
		return categoryCode;
	}





	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}





	public Long getCategoryId() {
		return categoryId;
	}





	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}


	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	
	
	
	

	public Boolean getIsWithHolding() {
		return isWithHolding;
	}





	public void setIsWithHolding(Boolean isWithHolding) {
		this.isWithHolding = isWithHolding;
	}





	public Boolean getIsFed() {
		return isFed;
	}





	public void setIsFed(Boolean isFed) {
		this.isFed = isFed;
	}





	public Double getWithHoldingShare() {
		return withHoldingShare;
	}





	public void setWithHoldingShare(Double withHoldingShare) {
		this.withHoldingShare = withHoldingShare;
	}





	public Double getFedShare() {
		return fedShare;
	}





	public void setFedShare(Double fedShare) {
		this.fedShare = fedShare;
	}





	public Boolean getChecked() {return checked;}

	public void setChecked(Boolean checked) {this.checked = checked;}

	public Long getAppUserTypeId() {return appUserTypeId;}

	public void setAppUserTypeId(Long appUserTypeId) {this.appUserTypeId = appUserTypeId;}
}
