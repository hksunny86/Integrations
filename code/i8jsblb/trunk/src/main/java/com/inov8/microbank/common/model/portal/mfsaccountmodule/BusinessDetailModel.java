package com.inov8.microbank.common.model.portal.mfsaccountmodule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.RetailerContactModel;


@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "BUSINESS_DETAIL_SEQ",sequenceName = "BUSINESS_DETAIL_SEQ", allocationSize=2)
@Table(name = "BUSINESS_DETAIL")
public class BusinessDetailModel extends BasePersistableModel implements Serializable{

	private static final long serialVersionUID = 5157893121898521991L;
	public Long businessDetailId;
	public Long customerId;
	private RetailerContactModel retailerContactModel;
	public String avgMonthlyIncome;
    public String minMonthlyIncome;
    public String maxMonthlyIncome;
    public String annualMonthlyTurnover;
    public String majorBuyer;
    public String supplierCustomer;
    public String otherACwithJSBL;

    public BusinessDetailModel() {
	}

    @Transient
    @Override
	public List<AssociationModel> getAssociationModelList()
	{
		List<AssociationModel> associationModelList = new ArrayList<>();
		AssociationModel associationModel = new AssociationModel();
		associationModel.setClassName("RetailerContactModel");
		associationModel.setPropertyName("retailerContactModel");
		associationModel.setValue(getRetailerContactModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BUSINESS_DETAIL_SEQ")
	@Column(name = "BUSINESS_DETAIL_ID")
	public Long getBusinessDetailId() {
		return businessDetailId;
	}
	public void setBusinessDetailId(Long businessDetailId) {
		this.businessDetailId = businessDetailId;
	}
    
	@Column(name = "AVG_MONTHLY_INCOME")
	public String getAvgMonthlyIncome() {
		return avgMonthlyIncome;
	}
	public void setAvgMonthlyIncome(String avgMonthlyIncome) {
		this.avgMonthlyIncome = avgMonthlyIncome;
	}
	
	@Column(name = "MIN_MONTHLY_INCOME")
	public String getMinMonthlyIncome() {
		return minMonthlyIncome;
	}
	public void setMinMonthlyIncome(String minMonthlyIncome) {
		this.minMonthlyIncome = minMonthlyIncome;
	}
	
	@Column(name = "MAX_MONTHLY_INCOME")
	public String getMaxMonthlyIncome() {
		return maxMonthlyIncome;
	}
	public void setMaxMonthlyIncome(String maxMonthlyIncome) {
		this.maxMonthlyIncome = maxMonthlyIncome;
	}
	
	@Column(name = "ANNUAL_MONTHLY_TURNOVER")
	public String getAnnualMonthlyTurnover() {
		return annualMonthlyTurnover;
	}
	public void setAnnualMonthlyTurnover(String annualMonthlyTurnover) {
		this.annualMonthlyTurnover = annualMonthlyTurnover;
	}
	
	@Column(name = "MAJOR_BUYER")
	public String getMajorBuyer() {
		return majorBuyer;
	}
	public void setMajorBuyer(String majorBuyer) {
		this.majorBuyer = majorBuyer;
	}
	
	@Column(name = "SUPPLIER_CUSTOMER")
	public String getSupplierCustomer() {
		return supplierCustomer;
	}
	public void setSupplierCustomer(String supplierCustomer) {
		this.supplierCustomer = supplierCustomer;
	}
	
	@Column(name = "OTHER_AC_JSBL")
	public String getOtherACwithJSBL() {
		return otherACwithJSBL;
	}
	public void setOtherACwithJSBL(String otherACwithJSBL) {
		this.otherACwithJSBL = otherACwithJSBL;
	}
	
	@Override
	public void setPrimaryKey(Long primaryKey) {
		setBusinessDetailId(primaryKey);
	}

	@Transient
	@Override
	public Long getPrimaryKey() {
		return getBusinessDetailId();
	}

	@Transient
	@Override
	public String getPrimaryKeyParameter() {
		return "&businessDetailId="+getBusinessDetailId();
	}

	@Transient
	@Override
	public String getPrimaryKeyFieldName() {
		return "businessDetailId";
	}
	
	@Column(name = "CUSTOMER_ID")
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "RETAILER_CONTACT_ID")
	public RetailerContactModel getRetailerContactModel() {
		return retailerContactModel;
	}

	public void setRetailerContactModel(RetailerContactModel retailerContactModel) {
		this.retailerContactModel = retailerContactModel;
	}

	@Transient
	public Long getRetailerContactId(){
		if(retailerContactModel == null)
		{
			return null;
		}
		return retailerContactModel.getRetailerContactId();
	}

	public void setRetailerContactId(Long retailerContactId) {
		if (retailerContactId == null) {
			retailerContactModel = null;
		} else {
			retailerContactModel = new RetailerContactModel();
			retailerContactModel.setRetailerContactId(retailerContactId);
		}
	}
}
