package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "MANUAL_ADJUSTMENT_SEQ",sequenceName = "MANUAL_ADJUSTMENT_SEQ", allocationSize=2)
@Table(name = "MANUAL_ADJUSTMENT")
public class ManualAdjustmentModel extends BasePersistableModel {

	private static final long serialVersionUID = -287273485218694909L;
	
	private Long manualAdjustmentID;
	private String transactionCodeId;
	private Long adjustmentType; 
	private String fromACNo;
	private String fromACNick;
	private String toACNo;
	private String toACNick;
	private Double amount;
	private String comments;
    private AppUserModel createdByAppUserModel;
    private AppUserModel updatedByAppUserModel;
    private Date createdOn;
    private Date updatedOn;
    private Integer versionNo;
    private Boolean isActive;
	private Date startDate;
	private Date endDate;
	private String authorizerId;
	private Long actionAuthorizationId;
	private Long adjustmentCategory;
	private Long bulkAdjustmentId;
	
	
    @Column(name = "MANUAL_ADJUSTMENT_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MANUAL_ADJUSTMENT_SEQ")
	public Long getManualAdjustmentID() {
		return manualAdjustmentID;
	}

	public void setManualAdjustmentID(Long manualAdjustmentID) {
		this.manualAdjustmentID = manualAdjustmentID;
	}
	
    @javax.persistence.Transient
    public Long getPrimaryKey() {
    	return getManualAdjustmentID();
    }

    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
    	setManualAdjustmentID(primaryKey);
    }
	
    @Column(name = "TRANSACTION_CODE_ID" )
	public String getTransactionCodeId() {
		return transactionCodeId;
	}

	public void setTransactionCodeId(String transactionCodeId) {
		this.transactionCodeId = transactionCodeId;
	}
    
	@Column(name = "ADJUSTMENT_TYPE" )
	public Long getAdjustmentType() {
		return adjustmentType;
	}

	public void setAdjustmentType(Long adjustmentType) {
		this.adjustmentType = adjustmentType;
	}

	@Column(name = "FROM_ACCOUNT_NO" )
	public String getFromACNo() {
		return fromACNo;
	}
	public void setFromACNo(String fromACNo) {
		this.fromACNo = fromACNo;
	}
	
	@Column(name = "FROM_ACCOUNT_NICK" )
	public String getFromACNick() {
		return fromACNick;
	}
	public void setFromACNick(String fromACNick) {
		this.fromACNick = fromACNick;
	}
	
	@Column(name = "TO_ACCOUNT_NO" )
	public String getToACNo() {
		return toACNo;
	}
	public void setToACNo(String toACNo) {
		this.toACNo = toACNo;
	}
	
	@Column(name = "TO_ACCOUNT_NICK" )
	public String getToACNick() {
		return toACNick;
	}
	public void setToACNick(String toACNick) {
		this.toACNick = toACNick;
	}
	
	@Column(name = "AMOUNT" )
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Column(name = "COMMENTS" )
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")    
    public AppUserModel getRelationUpdatedByAppUserModel(){
       return updatedByAppUserModel;
    }
     
    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel(){
       return getRelationUpdatedByAppUserModel();
    }

    @javax.persistence.Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
       this.updatedByAppUserModel = appUserModel;
    }
    
    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
       if(null != appUserModel)
       {
       	setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
       }      
    }
    
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")    
    public AppUserModel getRelationCreatedByAppUserModel(){
       return createdByAppUserModel;
    }
     
    @javax.persistence.Transient
    public AppUserModel getCreatedByAppUserModel(){
       return getRelationCreatedByAppUserModel();
    }

    @javax.persistence.Transient
    public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
       this.createdByAppUserModel = appUserModel;
    }
    
    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
       if(null != appUserModel)
       {
       	setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
       }      
    }

    @javax.persistence.Transient
    public Long getUpdatedBy() {
       if (updatedByAppUserModel != null) {
          return updatedByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setUpdatedBy(Long appUserId) {
       if(appUserId == null)
       {      
       	updatedByAppUserModel = null;
       }
       else
       {
         updatedByAppUserModel = new AppUserModel();
       	 updatedByAppUserModel.setAppUserId(appUserId);
       }      
    }

    @javax.persistence.Transient
    public Long getCreatedBy() {
       if (createdByAppUserModel != null) {
          return createdByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setCreatedBy(Long appUserId) {
       if(appUserId == null)
       {      
       	createdByAppUserModel = null;
       }
       else
       {
         createdByAppUserModel = new AppUserModel();
       	 createdByAppUserModel.setAppUserId(appUserId);
       }      
    }
    
    @Column(name="CREATED_ON")
    public Date getCreatedOn() {
        return this.createdOn;
    }
    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    
    @Column(name="UPDATED_ON")
    public Date getUpdatedOn() {
        return this.updatedOn;
    }
    
    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    @Version
    @Column(name="VERSION_NO")
    public Integer getVersionNo() {
        return this.versionNo;
    }
    
    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }
    
    @Column(name="IS_ACTIVE")
    public Boolean getIsActive() {
        return this.isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    @javax.persistence.Transient
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@javax.persistence.Transient
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
    
    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
       String parameters = "";
       parameters += "&manualAdjustmentID=" + getManualAdjustmentID();
       return parameters;
    }
    
 	/**
      * Helper method for default Sorting on Primary Keys
      */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName(){ 
    	String primaryKeyFieldName = "manualAdjustmentID";
    	return primaryKeyFieldName;
    }


    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	

    	AssociationModel associationModel = new AssociationModel();
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

    @Column(name="AUTHORIZER_ID")
	public String getAuthorizerId() {
		return authorizerId;
	}

	public void setAuthorizerId(String authorizerId) {
		this.authorizerId = authorizerId;
	}

    @Column(name="ACTION_AUTHORIZATION_ID")
	public Long getActionAuthorizationId() {
		return actionAuthorizationId;
	}

	public void setActionAuthorizationId(Long actionAuthorizationId) {
		this.actionAuthorizationId = actionAuthorizationId;
	}

    @Column(name="ADJUSTMENT_CATEGORY")
    public Long getAdjustmentCategory() {
        return adjustmentCategory;
    }

    public void setAdjustmentCategory(Long adjustmentCategory) {
        this.adjustmentCategory = adjustmentCategory;
    }

	@Transient
    public Long getBulkAdjustmentId() {
		return bulkAdjustmentId;
	}

	public void setBulkAdjustmentId(Long bulkAdjustmentId) {
		this.bulkAdjustmentId = bulkAdjustmentId;
	}
}