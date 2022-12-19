package com.inov8.microbank.server.service.commissionmodule;

import java.util.HashMap;
import java.util.Map;

import com.inov8.microbank.common.util.TaxValueBean;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class CommissionAmountsHolder
{
  Long productId;
  Double totalAmount;
  Double totalCommissionAmount;
  Double totalCommissionAmountUnsettled;
  Double transactionAmount;
  Double billingOrganizationAmount;
  Double transactionProcessingAmount;
  Double supplierCharges;
  Double serviceCharges;
  Double i8SupplierCharges;
  Double i8ServiceCharges; 
  Double exclusiveFixAmount;
  Double exclusivePercentAmount;
  Double inclusiveFixAmount;
  Double inclusivePercentAmount;
  Double totalInclusiveAmount;
  
  // For all stake Holders
  Double askariCommissionAmount;
  Double zongCommissionAmount;
  Double i8CommissionAmount;
  Double fedCommissionAmount;
  Double whtCommissionAmount;
  Double agent1CommissionAmount = Double.valueOf(0.0D);
  Double agent2CommissionAmount = Double.valueOf(0.0D);
  Double agent2CommAmountBeforeHierarchy = Double.valueOf(0.0D);
  Double agent2WHTAmount = Double.valueOf(0.0D);
  Double franchise1CommissionAmount = Double.valueOf(0.0D);
  Double franchise2CommissionAmount = Double.valueOf(0.0D);
  private boolean isInclusiveCharges;
  
  Map<Long, Double> stakeholderCommissionsMap = new HashMap<Long, Double>();
  Map<Long, Double> hierarchyStakeholderCommissionMap = new HashMap<Long, Double>();
  Map<Long, Double> hierarchyStakeholderCommissionMapLeg1 = new HashMap<Long, Double>();
  Map<Long, TaxValueBean> stakeholderTaxMap = new HashMap<Long, TaxValueBean>();
  Map<Long, TaxValueBean> hierarchyStakeholderTaxMap = new HashMap<Long, TaxValueBean>();
  
  private Map<Long, Boolean> stakeholderFilerMap = new HashMap<Long, Boolean>();
  
  public CommissionAmountsHolder(Boolean isDefaultValZeroRequired) {
		if(isDefaultValZeroRequired){
			this.setTransactionAmount(new Double(0.0));
		    this.setTotalAmount(new Double(0.0));
		    this.setBillingOrganizationAmount(new Double(0.0));
		    this.setTotalCommissionAmount(new Double(0.0));
		    this.setTotalCommissionAmountUnsettled(new Double(0.0));
		    this.setTransactionProcessingAmount(new Double(0.0));
		    this.setServiceCharges(new Double(0.0));
		    this.setSupplierCharges(new Double(0.0));
		    this.setAgent1CommissionAmount(new Double(0.0));
		    this.setAgent2CommissionAmount(new Double(0.0));
		    this.setAskariCommissionAmount(new Double(0.0));
		    this.setFedCommissionAmount(new Double(0.0));
		    this.setI8CommissionAmount(new Double(0.0));
		    this.setWhtCommissionAmount(new Double(0.0));
		    this.setZongCommissionAmount(new Double(0.0));
		    this.setFranchise1CommissionAmount(new Double(0.0));
		    this.setFranchise2CommissionAmount(new Double(0.0));
		}
	}
  
  public Double getBillingOrganizationAmount()
  {
    return billingOrganizationAmount;
  }

  public Double getTotalAmount()
  {
    return totalAmount;
  }

  public Double getTotalCommissionAmount()
  {
    return totalCommissionAmount;
  }

  public Double getTransactionAmount()
  {
    return transactionAmount;
  }

  public void setBillingOrganizationAmount(Double billingOrganizationAmount)
  {
    this.billingOrganizationAmount = billingOrganizationAmount;
  }

  public void setTotalAmount(Double totalAmount)
  {
    this.totalAmount = totalAmount;
  }

  public void setTotalCommissionAmount(Double totalCommissionAmount)
  {
    this.totalCommissionAmount = totalCommissionAmount;
  }

  public void setTransactionAmount(Double transactionAmount)
  {
    this.transactionAmount = transactionAmount;
  }

public Double getTransactionProcessingAmount()
{
	return transactionProcessingAmount;
}

public void setTransactionProcessingAmount(Double transactionProcessingAmount)
{
	this.transactionProcessingAmount = transactionProcessingAmount;
}


public Double getServiceCharges()
{
	return serviceCharges;
}


public void setServiceCharges(Double serviceCharges)
{
	this.serviceCharges = serviceCharges;
	this.i8ServiceCharges = this.serviceCharges ;
}


public Double getSupplierCharges()
{
	return supplierCharges;
}


public void setSupplierCharges(Double supplierCharges)
{
	this.supplierCharges = supplierCharges;
	this.i8SupplierCharges = this.supplierCharges ;
}

public Double getI8SupplierCharges()
{
	return i8SupplierCharges;
}

public void setI8SupplierCharges(Double supplierCharges)
{
	i8SupplierCharges = supplierCharges;
}

public Double getI8ServiceCharges()
{
	return i8ServiceCharges;
}

public void setI8ServiceCharges(Double serviceCharges)
{
	i8ServiceCharges = serviceCharges;
}

public Double getAskariCommissionAmount() {
	return askariCommissionAmount;
}

public void setAskariCommissionAmount(Double askariCommissionAmount) {
	this.askariCommissionAmount = askariCommissionAmount;
}

public Double getZongCommissionAmount() {
	return zongCommissionAmount;
}

public void setZongCommissionAmount(Double zongCommissionAmount) {
	this.zongCommissionAmount = zongCommissionAmount;
}

public Double getI8CommissionAmount() {
	return i8CommissionAmount;
}

public void setI8CommissionAmount(Double i8CommissionAmount) {
	this.i8CommissionAmount = i8CommissionAmount;
}

public Double getFedCommissionAmount() {
	return fedCommissionAmount;
}

public void setFedCommissionAmount(Double fedCommissionAmount) {
	this.fedCommissionAmount = fedCommissionAmount;
}

public Double getWhtCommissionAmount() {
	return whtCommissionAmount;
}

public void setWhtCommissionAmount(Double whtCommissionAmount) {
	this.whtCommissionAmount = whtCommissionAmount;
}

public Double getAgent1CommissionAmount() {
	return agent1CommissionAmount;
}

public void setAgent1CommissionAmount(Double agent1CommissionAmount) {
	this.agent1CommissionAmount = agent1CommissionAmount;
}

public Double getAgent2CommissionAmount() {
	return agent2CommissionAmount;
}

public void setAgent2CommissionAmount(Double agent2CommissionAmount) {
	this.agent2CommissionAmount = agent2CommissionAmount;
}

public Double getFranchise1CommissionAmount() {
	return franchise1CommissionAmount;
}

public void setFranchise1CommissionAmount(Double franchise1CommissionAmount) {
	this.franchise1CommissionAmount = franchise1CommissionAmount;
}

public Double getFranchise2CommissionAmount() {
	return franchise2CommissionAmount;
}

public void setFranchise2CommissionAmount(Double franchise2CommissionAmount) {
	this.franchise2CommissionAmount = franchise2CommissionAmount;
}

public boolean getIsInclusiveCharges() {
	return isInclusiveCharges;
}

public void setIsInclusiveCharges(boolean isInclusiveCharges) {
	this.isInclusiveCharges = isInclusiveCharges;
}

public CommissionAmountsHolder() {
	super();
}

public Map<Long, Double> getStakeholderCommissionsMap() {
	return stakeholderCommissionsMap;
}

public void setStakeholderCommissionsMap(
		Map<Long, Double> stakeholderCommissionsMap) {
	this.stakeholderCommissionsMap = stakeholderCommissionsMap;
}

public Double getExclusiveFixAmount() {
	return exclusiveFixAmount;
}

public Double getExclusivePercentAmount() {
	return exclusivePercentAmount;
}

public Double getInclusiveFixAmount() {
	return inclusiveFixAmount;
}

public Double getInclusivePercentAmount() {
	return inclusivePercentAmount;
}

public void setExclusiveFixAmount(Double exclusiveFixAmount) {
	this.exclusiveFixAmount = exclusiveFixAmount;
}

public void setExclusivePercentAmount(Double exclusivePercentAmount) {
	this.exclusivePercentAmount = exclusivePercentAmount;
}

public void setInclusiveFixAmount(Double inclusiveFixAmount) {
	this.inclusiveFixAmount = inclusiveFixAmount;
}

public void setInclusivePercentAmount(Double inclusivePercentAmount) {
	this.inclusivePercentAmount = inclusivePercentAmount;
}

public Map<Long, Double> getHierarchyStakeholderCommissionMap() {
	return hierarchyStakeholderCommissionMap;
}

public void setHierarchyStakeholderCommissionMap(
		Map<Long, Double> hierarchyStakeholderCommissionMap) {
	this.hierarchyStakeholderCommissionMap = hierarchyStakeholderCommissionMap;
}
public Map<Long, Double> getHierarchyStakeholderCommissionMapLeg1() {
	return hierarchyStakeholderCommissionMapLeg1;
}

public void setHierarchyStakeholderCommissionMapLeg1(
		Map<Long, Double> hierarchyStakeholderCommissionMapLeg1) {
	this.hierarchyStakeholderCommissionMapLeg1 = hierarchyStakeholderCommissionMapLeg1;
}


	@Override
	public String toString() {
		
		return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this);
	}

	public Map<Long, TaxValueBean> getStakeholderTaxMap() {
		return stakeholderTaxMap;
	}

	public void setStakeholderTaxMap(Map<Long, TaxValueBean> stakeholderTaxMap) {
		this.stakeholderTaxMap = stakeholderTaxMap;
	}

	public Map<Long, TaxValueBean> getHierarchyStakeholderTaxMap() {
		return hierarchyStakeholderTaxMap;
	}

	public void setHierarchyStakeholderTaxMap(
			Map<Long, TaxValueBean> hierarchyStakeholderTaxMap) {
		this.hierarchyStakeholderTaxMap = hierarchyStakeholderTaxMap;
	}

	public Double getTotalInclusiveAmount() {
		return totalInclusiveAmount;
	}

	public void setTotalInclusiveAmount(Double totalInclusiveAmount) {
		this.totalInclusiveAmount = totalInclusiveAmount;
	}

	public Double getAgent2WHTAmount() {
		return agent2WHTAmount;
	}

	public void setAgent2WHTAmount(Double agent2whtAmount) {
		agent2WHTAmount = agent2whtAmount;
	}

	public Double getAgent2CommAmountBeforeHierarchy() {
		return agent2CommAmountBeforeHierarchy;
	}

	public void setAgent2CommAmountBeforeHierarchy(
			Double agent2CommAmountBeforeHierarchy) {
		this.agent2CommAmountBeforeHierarchy = agent2CommAmountBeforeHierarchy;
	}

	public Double getTotalCommissionAmountUnsettled() {
		return totalCommissionAmountUnsettled;
	}

	public void setTotalCommissionAmountUnsettled(
			Double totalCommissionAmountUnsettled) {
		this.totalCommissionAmountUnsettled = totalCommissionAmountUnsettled;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public Map<Long, Boolean> getStakeholderFilerMap() {
		return stakeholderFilerMap;
	}

}
