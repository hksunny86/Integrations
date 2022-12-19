package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * @author Rizwan Munir
 */
@XmlRootElement(name="P2PDetailModel")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "P2P_DETAIL_seq", sequenceName = "P2P_DETAIL_seq", allocationSize = 1)
@Table(name = "P2P_DETAIL")
public class P2PDetailModel extends BasePersistableModel implements
		Serializable {

	private static final long	serialVersionUID	= -8852829461307039257L;
	private Long				p2pDetailId;
	private String				transactionCode;
	private Double				transactionAmount;
	private String				senderCNIC;
	private String				senderMobile;
	private String				recipientCNIC;
	private String				recipientMobile;
	private String				comments;

	private boolean				senderCNICChanged;
	private boolean				senderMobileChanged;
	private boolean				recipientCNICChanged;
	private boolean				recipientMobileChanged;

	private Date				createdOn;
	private Date				updatedOn;
	private AppUserModel		createdByAppUserModel;
	private AppUserModel		updatedByAppUserModel;
	private Integer				versionNo;

	private String				processingStatusName;
	private Long				supProcessingStatusId;

	private String				agent1Id;
	private String				agent1MobileNo;
	private String				agent1Name;
	
	private String				updatedByName;

	public P2PDetailModel() {
	}

	/**
	 * Return the primary key.
	 * @return Long with the primary key.
	 */
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getP2pDetailId();
	}

	/**
	 * Set the primary key.
	 * @param primaryKey the primary key
	 */
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setP2pDetailId(primaryKey);
	}

	@Column(name = "P2P_DETAIL_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "P2P_DETAIL_seq")
	public Long getP2pDetailId() {
		return p2pDetailId;
	}

	public void setP2pDetailId(Long p2pDetailId) {
		if (p2pDetailId != null) {
			this.p2pDetailId = p2pDetailId;
		}
	}

	@Column(name = "TRANSACTION_CODE")
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		if (transactionCode != null) {
			this.transactionCode = transactionCode;
		}
	}

	@Column(name = "SENDER_CNIC")
	public String getSenderCNIC() {
		return senderCNIC;
	}

	public void setSenderCNIC(String senderCNIC) {
		if (senderCNIC != null) {
			this.senderCNIC = senderCNIC;
		}
	}

	@Column(name = "SENDER_MOBILE")
	public String getSenderMobile() {
		return senderMobile;
	}

	public void setSenderMobile(String senderMobile) {
		if (senderMobile != null) {
			this.senderMobile = senderMobile;
		}
	}

	@Column(name = "RECIPIENT_CNIC")
	public String getRecipientCNIC() {
		return recipientCNIC;
	}

	public void setRecipientCNIC(String recipientCNIC) {
		if (recipientCNIC != null) {
			this.recipientCNIC = recipientCNIC;
		}
	}

	@Column(name = "RECIPIENT_MOBILE")
	public String getRecipientMobile() {
		return recipientMobile;
	}

	public void setRecipientMobile(String recipientMobile) {
		if (recipientMobile != null) {
			this.recipientMobile = recipientMobile;
		}
	}

	@Column(name = "TRANSACTION_AMOUNT")
	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		if (transactionAmount != null) {
			this.transactionAmount = transactionAmount;
		}
	}

	@javax.persistence.Transient
	public boolean isSenderCNICChanged() {
		return senderCNICChanged;
	}

	public void setSenderCNICChanged(boolean senderCNICChanged) {
		this.senderCNICChanged = senderCNICChanged;
	}

	@javax.persistence.Transient
	public boolean isSenderMobileChanged() {
		return senderMobileChanged;
	}

	public void setSenderMobileChanged(boolean senderMobileChanged) {
		this.senderMobileChanged = senderMobileChanged;
	}

	@javax.persistence.Transient
	public boolean isRecipientCNICChanged() {
		return recipientCNICChanged;
	}

	public void setRecipientCNICChanged(boolean recipientCNICChanged) {
		this.recipientCNICChanged = recipientCNICChanged;
	}

	@javax.persistence.Transient
	public boolean isRecipientMobileChanged() {
		return recipientMobileChanged;
	}

	public void setRecipientMobileChanged(boolean recipientMobileChanged) {
		this.recipientMobileChanged = recipientMobileChanged;
	}

	@Column(name = "COMMENTS")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		if (comments != null) {
			this.comments = comments;
		}
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getRelationCreatedByAppUserModel() {
		return createdByAppUserModel;
	}

	@javax.persistence.Transient
	public AppUserModel getCreatedByAppUserModel() {
		return getRelationCreatedByAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
		this.createdByAppUserModel = appUserModel;
	}

	@javax.persistence.Transient
	public void setCreatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			setRelationCreatedByAppUserModel((AppUserModel) appUserModel
					.clone());
		}
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getRelationUpdatedByAppUserModel() {
		return updatedByAppUserModel;
	}

	@javax.persistence.Transient
	public AppUserModel getUpdatedByAppUserModel() {
		return getRelationUpdatedByAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
		this.updatedByAppUserModel = appUserModel;
	}

	@javax.persistence.Transient
	public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			setRelationUpdatedByAppUserModel((AppUserModel) appUserModel
					.clone());
		}
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Version
	@Column(name = "VERSION_NO", nullable = false)
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	@javax.persistence.Transient
	public String getProcessingStatusName() {
		return processingStatusName;
	}

	public void setProcessingStatusName(String processingStatusName) {
		if (processingStatusName != null) {
			this.processingStatusName = processingStatusName;
		}
	}

	@javax.persistence.Transient
	public Long getSupProcessingStatusId() {
		return supProcessingStatusId;
	}

	public void setSupProcessingStatusId(Long supProcessingStatusId) {
		if (supProcessingStatusId != null) {
			this.supProcessingStatusId = supProcessingStatusId;
		}
	}

	@javax.persistence.Transient
	public String getAgent1Id() {
		return agent1Id;
	}

	public void setAgent1Id(String agent1Id) {
		if (agent1Id != null) {
			this.agent1Id = agent1Id;
		}
	}

	@javax.persistence.Transient
	public String getAgent1MobileNo() {
		return agent1MobileNo;
	}

	public void setAgent1MobileNo(String agent1MobileNo) {
		if (agent1MobileNo != null) {
			this.agent1MobileNo = agent1MobileNo;
		}
	}
	
	@javax.persistence.Transient
	public String getAgent1Name() {
		return agent1Name;
	}

	public void setAgent1Name(String agent1Name) {
		if (agent1Name != null) {
			this.agent1Name = agent1Name;
		}
	}
	
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&p2pDetailId=" + getP2pDetailId();
		return parameters;
	}

	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "p2pDetailId";
		return primaryKeyFieldName;
	}

	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = null;

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationCreatedByAppUserModel");
		associationModel.setValue(getRelationCreatedByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationUpdatedByAppUserModel");
		associationModel.setValue(getRelationUpdatedByAppUserModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}

	@javax.persistence.Transient
	public String getUpdatedByName() {
		String updatedByName = "";
		try{
			if(createdByAppUserModel != null){
				updatedByName = createdByAppUserModel.getFirstName() + " " + createdByAppUserModel.getLastName();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return updatedByName;
	}

}