package com.inov8.microbank.common.model;

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

/**
 * The CustomerFundSourceModel entity bean.
 * @author Rizwan Munir Inov8 Limited
 * @spring.bean name="CustomerFundSourceModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CUSTOMER_FUND_SOURCE_SEQ", sequenceName = "CUSTOMER_FUND_SOURCE_SEQ", allocationSize = 2)
@Table(name = "CUSTOMER_FUND_SOURCE")
public class CustomerFundSourceModel extends BasePersistableModel implements
		Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3248123103998216370L;
	private Long				customerFundSourceId;
	private FundSourceModel		fundSourceIdFundSourceModel;
	private Long				customerId;

	public CustomerFundSourceModel() {
	}

	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return this.getCustomerFundSourceId();
	}

	@Override
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		this.setCustomerFundSourceId(primaryKey);
	}

	@Column(name = "CUSTOMER_FUND_SOURCE_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_FUND_SOURCE_SEQ")
	public Long getCustomerFundSourceId() {
		return this.customerFundSourceId;
	}

	public void setCustomerFundSourceId(Long customerFundSourceId) {
		if (customerFundSourceId != null) {
			this.customerFundSourceId = customerFundSourceId;
		}
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "FUND_SOURCE_ID")
	public FundSourceModel getRelationFundSourceIdFundSourceModel() {
		return this.fundSourceIdFundSourceModel;
	}

	@javax.persistence.Transient
	public FundSourceModel getFundSourceIdFundSourceModel() {
		return this.getRelationFundSourceIdFundSourceModel();
	}

	@javax.persistence.Transient
	public void setRelationFundSourceIdFundSourceModel(
			FundSourceModel fundSourceModel) {
		this.fundSourceIdFundSourceModel = fundSourceModel;
	}

	@javax.persistence.Transient
	public void setFundSourceIdFundSourceModel(FundSourceModel fundSourceModel) {
		if (null != fundSourceModel) {
			this.setRelationFundSourceIdFundSourceModel((FundSourceModel) fundSourceModel
					.clone());
		}
	}

	@javax.persistence.Transient
	public Long getFundSourceId() {
		if (this.fundSourceIdFundSourceModel != null) {
			return this.fundSourceIdFundSourceModel.getFundSourceId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setFundSourceId(Long fundSourceId) {
		if (fundSourceId == null) {
			this.fundSourceIdFundSourceModel = null;
		} else {
			this.fundSourceIdFundSourceModel = new FundSourceModel();
			this.fundSourceIdFundSourceModel.setFundSourceId(fundSourceId);
		}
	}

	@Column(name = "CUSTOMER_ID", nullable = false)
	public Long getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Long customerId) {
		if (customerId != null) {
			this.customerId = customerId;
		}
	}

	@Transient
	public String getCheckbox() {
		String checkBox = "<input type=\"checkbox\" name=\"checkbox";
		checkBox += "_" + this.getCustomerFundSourceId();
		checkBox += "\"/>";
		return checkBox;
	}

	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&customerFundSourceId=" + this.getCustomerFundSourceId();
		return parameters;
	}

	@Override
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "customerFundSourceId";
		return primaryKeyFieldName;
	}

	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = new AssociationModel();
		associationModel.setClassName("FundSourceModel");
		associationModel.setPropertyName("relationFundSourceIdFundSourceModel");
		associationModel.setValue(this.getRelationFundSourceIdFundSourceModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}

}
