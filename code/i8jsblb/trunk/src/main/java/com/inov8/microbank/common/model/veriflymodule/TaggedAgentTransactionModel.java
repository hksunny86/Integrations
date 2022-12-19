package com.inov8.microbank.common.model.veriflymodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "PRODUCT_WISE_TRANX_DET_VIEW")
public class TaggedAgentTransactionModel extends BasePersistableModel  implements Serializable {
	
/**
	 * 
	 */
	private static final long serialVersionUID = -6612722652606727987L;
//	private static final long serialVersionUID = 6005367025347151979L;

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private Long pk;
	private String productOrService;
	private String transactionCount;  
	private String transactionAmount;
	private Long productId;
	private Long handlerId;
	// Transient property
	private String agentId;
	
	@Id
	@Column(name="PK")
	public Long getPk() {
		return pk;
	}

	public void setPk(Long PK) {
		this.pk = PK;
	}
	@Column(name="PRODUCT")
	public String getProductOrService() {
		return productOrService;
	}

	public void setProductOrService(String productOrService) {
		this.productOrService = productOrService;
	}

	@Column(name="TOTAL_TRANSACTION_COUNT")
	public String getTransactionCount() {
		return transactionCount;
	}

	public void setTransactionCount(String transactionCount) {
		this.transactionCount = transactionCount;
	}

	@Column(name="TOTAL_TRANSACTION_AMOUNT")
	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	@Column(name="PRODUCT_ID")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	 @Transient
	public Long getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(Long handlerId) {
		this.handlerId = handlerId;
	}

   @javax.persistence.Transient
	public Long getPrimaryKey() {
		// TODO Auto-generated method stub
		return getPk();
	}

	 @javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "pk";
		return primaryKeyFieldName;	
	}

	 @javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		 String parameters = "";
	      parameters += "&pk=" + getPk();
	      return parameters;
	}

	 @javax.persistence.Transient
	public void setPrimaryKey(Long arg0) {
		// TODO Auto-generated method stub
		setPk(arg0);
	}
	 @Transient
	 public String getAgentId(){
			return agentId;
		}

	 public void setAgentId(String agentId) {
			this.agentId = agentId;
		}

}
