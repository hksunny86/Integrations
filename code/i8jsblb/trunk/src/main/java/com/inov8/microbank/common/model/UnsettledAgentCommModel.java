package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;


@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@javax.persistence.SequenceGenerator(name = "UNSETTLED_AGENT_COMMISSION_SEQ", sequenceName = "UNSETTLED_AGENT_COMMISSION_SEQ", allocationSize=1)
@Table(name = "UNSETTLED_AGENT_COMMISSION")
public class UnsettledAgentCommModel extends BasePersistableModel implements Serializable {
	
	private static final long serialVersionUID = -1L;
	
	private Long unsettledAgentCommId;
	private String transactionCode;
	private Date transactionDate;
	private Long productId;
	private String productName;
	private String agentId;
	private String commissionType;
	private Double commissionAmount;
    private Date createdOn;

    private Date startDate;
    private Date endDate;
    private Long supplierId;
    
	public UnsettledAgentCommModel(){
	}
	
	public UnsettledAgentCommModel(
				String transactionCode,
				Date transactionDate,
				Long productId,
				String productName,
				String commissionType,
				Double commissionAmount) {
		
		this.transactionCode = transactionCode;
		this.transactionDate = transactionDate;
		this.productId = productId;
		this.productName = productName;
		this.commissionType = commissionType;
		this.commissionAmount = commissionAmount;
	}
	
	@Transient
	public Long getPrimaryKey() {
		return getUnsettledAgentCommId();
	}

	@Transient
	public void setPrimaryKey(Long primaryKey) {
		setUnsettledAgentCommId(primaryKey);
	}

	@Column(name = "UNSETTLED_AGENT_COMMISSION_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UNSETTLED_AGENT_COMMISSION_SEQ")
	public Long getUnsettledAgentCommId() {
		return unsettledAgentCommId;
	}

	public void setUnsettledAgentCommId(Long unsettledAgentCommId) {
		this.unsettledAgentCommId = unsettledAgentCommId;
	}
	
    @Column(name="TRANSACTION_CODE")
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

    @Column(name="TRANSACTION_DATE")
	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

    @Column(name="PRODUCT_ID")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

    @Column(name="PRODUCT_NAME")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

    @Column(name="AGENT_ID")
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

    @Column(name="COMMISSION_TYPE")
	public String getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
	}

    @Column(name="COMMISSION_AMOUNT")
	public Double getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(Double commissionAmount) {
		this.commissionAmount = commissionAmount;
	}
	
    @Column(name="CREATED_ON")
    public Date getCreatedOn() {
        return this.createdOn;
    }
    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    
	/**
	 * Helper method for Struts with displaytag
	 */
	@Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&unsettledAgentCommId=" + getUnsettledAgentCommId();
		return parameters;
	}

	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "unsettledAgentCommId";
		return primaryKeyFieldName;
	}

	/**
	 * Helper method for Complex Example Queries
	 */
	@Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		return associationModelList;
	}

	@Transient
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Transient
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Transient
	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

}
