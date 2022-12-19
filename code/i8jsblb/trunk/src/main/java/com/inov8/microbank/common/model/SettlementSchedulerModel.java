package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="SETTLEMENT_SCHEDULER_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="SETTLEMENT_SCHEDULER_seq") } )
//@javax.persistence.SequenceGenerator(name = "SETTLEMENT_SCHEDULER_seq",sequenceName = "SETTLEMENT_SCHEDULER_seq")
@Table(name = "SETTLEMENT_SCHEDULER")
public class SettlementSchedulerModel extends BasePersistableModel implements Serializable{

   private static final long serialVersionUID = -3183432472310526991L;

   private Long settlementSchedulerId;
   private ProductModel productIdProductModel;
   private Long fromStakeholderBankId;
   private Long toStakeholderBankId;
   private Boolean status;
   private Double totalAmount;
   private String rrn;
   private String responseCode;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;  	
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;	
   
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getSettlementSchedulerId();
   }

   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setSettlementSchedulerId(primaryKey);
   }

   @Column(name = "SETTLEMENT_SCHEDULER_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SETTLEMENT_SCHEDULER_seq")
   public Long getSettlementSchedulerId() {
      return settlementSchedulerId;
   }

   public void setSettlementSchedulerId(Long settlementSchedulerId) {
      this.settlementSchedulerId = settlementSchedulerId;
   }


   @Column(name = "CREATED_ON" , nullable = false )
   public Date getCreatedOn() {
      return createdOn;
   }

   public void setCreatedOn(Date createdOn) {
      this.createdOn = createdOn;
   }

   @Column(name = "UPDATED_ON" , nullable = false )
   public Date getUpdatedOn() {
      return updatedOn;
   }

   public void setUpdatedOn(Date updatedOn) {
      this.updatedOn = updatedOn;
   }

   @Version 
   @Column(name = "VERSION_NO" , nullable = false )
   public Integer getVersionNo() {
      return versionNo;
   }

   public void setVersionNo(Integer versionNo) {
      this.versionNo = versionNo;
   }


   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PRODUCT_ID")    
   public ProductModel getRelationProductIdProductModel(){
      return productIdProductModel;
   }
    

   @javax.persistence.Transient
   public ProductModel getProductIdProductModel(){
      return getRelationProductIdProductModel();
   }


   @javax.persistence.Transient
   public void setRelationProductIdProductModel(ProductModel productModel) {
      this.productIdProductModel = productModel;
   }
   

   @javax.persistence.Transient
   public void setProductIdProductModel(ProductModel productModel) {
      if(null != productModel)
      {
      	setRelationProductIdProductModel((ProductModel)productModel.clone());
      }      
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
   public Long getProductId() {
      if (productIdProductModel != null) {
         return productIdProductModel.getProductId();
      } else {
         return null;
      }
   }

   
   @javax.persistence.Transient
   public void setProductId(Long productId) {
      if(productId == null)
      {      
      	productIdProductModel = null;
      }
      else
      {
        productIdProductModel = new ProductModel();
      	productIdProductModel.setProductId(productId);
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
      if(appUserId == null) {      
      	createdByAppUserModel = null;
      } else {
        createdByAppUserModel = new AppUserModel();
      	createdByAppUserModel.setAppUserId(appUserId);
      }      
   }


   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&settlementSchedulerId=" + settlementSchedulerId;
      return parameters;
   }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName() { 
			String primaryKeyFieldName = "settlementSchedulerId";
			return primaryKeyFieldName;				
    }
    

    @Column(name = "STATUS" , nullable = true )
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}             

    @Column(name = "TOTAL_AMOUNT")
	public Double getTotalAmount() {
		return totalAmount;
	}
		
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

    @Column(name = "RRN")
	public String getRrn() {
		return rrn;
	}
		
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

    @Column(name = "FROM_BANK_INFO_ID")
	public Long getFromStakeholderBankId() {
		return fromStakeholderBankId;
	}
			
	public void setFromStakeholderBankId(Long fromStakeholderBankId) {
		this.fromStakeholderBankId = fromStakeholderBankId;
	}

    @Column(name = "TO_BANK_INFO_ID")
	public Long getToStakeholderBankId() {
		return toStakeholderBankId;
	}
			
	public void setToStakeholderBankId(Long toStakeholderBankId) {
		this.toStakeholderBankId = toStakeholderBankId;
	}

    @Column(name = "RESPONSE_CODE")
	public String getResponseCode() {
		return responseCode;
	}
		
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}	
}