package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import org.springframework.jdbc.core.RowMapper;

/**
 * The CommissionShSharesModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CommissionShSharesModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PRODUCT_SH_SHARES_DEFAULT_SEQ",sequenceName = "PRODUCT_SH_SHARES_DEFAULT_SEQ", allocationSize=1)
@Table(name = "PRODUCT_SH_SHARES_DEFAULT")
public class CommissionShSharesDefaultModel extends BasePersistableModel implements Serializable,RowMapper {
  

   private ProductModel productIdProductModel;
   private CommissionStakeholderModel commissionStakeholderIdCommissionStakeholderModel;

   private Long commissionShSharesDefaultId;
   private Double commissionShare;
   private Date updatedOn;
   private Date createdOn;
   private Integer versionNo;
  
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;
  
   private Boolean isWhtApplicable;
   private Boolean isFedApplicable;
   
   /**
    * Default constructor.
    */
   public CommissionShSharesDefaultModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCommissionShSharesDefaultId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCommissionShSharesDefaultId(primaryKey);
    }

   /**
    * Returns the value of the <code>commissionShSharesId</code> property.
    *
    */
      @Column(name = "PRODUCT_SH_SHARES_DEF_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUCT_SH_SHARES_DEFAULT_SEQ")
   public Long getCommissionShSharesDefaultId() {
      return commissionShSharesDefaultId;
   }

   /**
    * Sets the value of the <code>commissionShSharesId</code> property.
    *
    * @param commissionShSharesId the value for the <code>commissionShSharesId</code> property
    *    
		    */

   public void setCommissionShSharesDefaultId(Long commissionShSharesDefaultId) {
      this.commissionShSharesDefaultId = commissionShSharesDefaultId;
   }

   /**
    * Returns the value of the <code>share</code> property.
    *
    */
      @Column(name = "COMMISSION_SHARE" , nullable = true )
   public Double getCommissionShare() {
      return commissionShare;
   }

   /**
    * Sets the value of the <code>commissionShare</code> property.
    *
    * @param share the value for the <code>commissionShare</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setCommissionShare(Double commissionShare) {
      this.commissionShare = commissionShare;
   }

   /**
    * Returns the value of the <code>productIdProductModel</code> relation property.
    *
    * @return the value of the <code>productIdProductModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PRODUCT_ID")    
   public ProductModel getRelationProductIdProductModel(){
      return productIdProductModel;
   }
    
   /**
    * Returns the value of the <code>productIdProductModel</code> relation property.
    *
    * @return the value of the <code>productIdProductModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ProductModel getProductIdProductModel(){
      return getRelationProductIdProductModel();
   }

   /**
    * Sets the value of the <code>productIdProductModel</code> relation property.
    *
    * @param productModel a value for <code>productIdProductModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationProductIdProductModel(ProductModel productModel) {
      this.productIdProductModel = productModel;
   }
   
   /**
    * Sets the value of the <code>productIdProductModel</code> relation property.
    *
    * @param productModel a value for <code>productIdProductModel</code>.
    */
   @javax.persistence.Transient
   public void setProductIdProductModel(ProductModel productModel) {
      if(null != productModel)
      {
      	setRelationProductIdProductModel((ProductModel)productModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    * @return the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "STAKEHOLDER_ID")    
   public CommissionStakeholderModel getRelationCommissionStakeholderIdCommissionStakeholderModel(){
      return commissionStakeholderIdCommissionStakeholderModel;
   }
    
   /**
    * Returns the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    * @return the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CommissionStakeholderModel getCommissionStakeholderIdCommissionStakeholderModel(){
      return getRelationCommissionStakeholderIdCommissionStakeholderModel();
   }

   /**
    * Sets the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    * @param commissionStakeholderModel a value for <code>commissionStakeholderIdCommissionStakeholderModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCommissionStakeholderIdCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {
      this.commissionStakeholderIdCommissionStakeholderModel = commissionStakeholderModel;
   }
   
   /**
    * Sets the value of the <code>commissionStakeholderIdCommissionStakeholderModel</code> relation property.
    *
    * @param commissionStakeholderModel a value for <code>commissionStakeholderIdCommissionStakeholderModel</code>.
    */
   @javax.persistence.Transient
   public void setCommissionStakeholderIdCommissionStakeholderModel(CommissionStakeholderModel commissionStakeholderModel) {
      if(null != commissionStakeholderModel)
      {
      	setRelationCommissionStakeholderIdCommissionStakeholderModel((CommissionStakeholderModel)commissionStakeholderModel.clone());
      }      
   }
   



   /**
    * Returns the value of the <code>productId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getProductId() {
      if (productIdProductModel != null) {
         return productIdProductModel.getProductId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>productId</code> property.
    *
    * @param productId the value for the <code>productId</code> property
					    * @spring.validator type="required"
							    */
   
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

   /**
    * Returns the value of the <code>commissionStakeholderId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCommissionStakeholderId() {
      if (commissionStakeholderIdCommissionStakeholderModel != null) {
         return commissionStakeholderIdCommissionStakeholderModel.getCommissionStakeholderId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>commissionStakeholderId</code> property.
    *
    * @param commissionStakeholderId the value for the <code>commissionStakeholderId</code> property
							    * @spring.validator type="required"
					    */
   
   @javax.persistence.Transient
   public void setCommissionStakeholderId(Long commissionStakeholderId) {
      if(commissionStakeholderId == null)
      {      
      	commissionStakeholderIdCommissionStakeholderModel = null;
      }
      else
      {
        commissionStakeholderIdCommissionStakeholderModel = new CommissionStakeholderModel();
      	commissionStakeholderIdCommissionStakeholderModel.setCommissionStakeholderId(commissionStakeholderId);
      }      
   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCommissionShSharesDefaultId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&commissionShSharesDefaultId=" + getCommissionShSharesDefaultId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "commissionShSharesDefaultId";
			return primaryKeyFieldName;				
    }
    

    /**
     * Returns the value of the <code>updatedOn</code> property.
     *
     */
       @Column(name = "UPDATED_ON" , nullable = false )
    public Date getUpdatedOn() {
       return updatedOn;
    }

    /**
     * Sets the value of the <code>updatedOn</code> property.
     *
     * @param updatedOn the value for the <code>updatedOn</code> property
     *    
 		    */

    public void setUpdatedOn(Date updatedOn) {
       this.updatedOn = updatedOn;
    }

    /**
     * Returns the value of the <code>createdOn</code> property.
     *
     */
       @Column(name = "CREATED_ON" , nullable = false )
    public Date getCreatedOn() {
       return createdOn;
    }

    /**
     * Sets the value of the <code>createdOn</code> property.
     *
     * @param createdOn the value for the <code>createdOn</code> property
     *    
 		    */

    public void setCreatedOn(Date createdOn) {
       this.createdOn = createdOn;
    }

    /**
     * Returns the value of the <code>versionNo</code> property.
     *
     */
       @Version 
 	    @Column(name = "VERSION_NO" , nullable = false )
    public Integer getVersionNo() {
       return versionNo;
    }

    /**
     * Sets the value of the <code>versionNo</code> property.
     *
     * @param versionNo the value for the <code>versionNo</code> property
     *    
 		    */

    public void setVersionNo(Integer versionNo) {
       this.versionNo = versionNo;
    }
    
    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")    
    public AppUserModel getRelationUpdatedByAppUserModel(){
       return updatedByAppUserModel;
    }
     
    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel(){
       return getRelationUpdatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
       this.updatedByAppUserModel = appUserModel;
    }
    
    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
       if(null != appUserModel)
       {
       	setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
       }      
    }
    

    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY")    
    public AppUserModel getRelationCreatedByAppUserModel(){
       return createdByAppUserModel;
    }
     
    /**
     * Returns the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @return the value of the <code>createdByAppUserModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public AppUserModel getCreatedByAppUserModel(){
       return getRelationCreatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
       this.createdByAppUserModel = appUserModel;
    }
    
    /**
     * Sets the value of the <code>createdByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>createdByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setCreatedByAppUserModel(AppUserModel appUserModel) {
       if(null != appUserModel)
       {
       	setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
       }      
    }
    
    /**
     * Returns the value of the <code>appUserId</code> property.
     *
     */
    @javax.persistence.Transient
    public Long getUpdatedBy() {
       if (updatedByAppUserModel != null) {
          return updatedByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

    /**
     * Sets the value of the <code>appUserId</code> property.
     *
     * @param appUserId the value for the <code>appUserId</code> property
 																																																																																			    */
    
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

    /**
     * Returns the value of the <code>appUserId</code> property.
     *
     */
    @javax.persistence.Transient
    public Long getCreatedBy() {
       if (createdByAppUserModel != null) {
          return createdByAppUserModel.getAppUserId();
       } else {
          return null;
       }
    }

    /**
     * Sets the value of the <code>appUserId</code> property.
     *
     * @param appUserId the value for the <code>appUserId</code> property
 																																																																																			    */
    
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

	   /**
	    * Returns the value of the <code>isWhtApplicable</code> property.
	    *
	    */
	      @Column(name = "IS_WHT_APPLICABLE" , nullable = false )
	   public Boolean getIsWhtApplicable() {
	      return isWhtApplicable;
	   }

	   /**
	    * Sets the value of the <code>isWhtApplicable</code> property.
	    *
	    * @param accountEnabled the value for the <code>isWhtApplicable</code> property
	    *    
			    */

	   public void setIsWhtApplicable(Boolean isWhtApplicable) {
	      this.isWhtApplicable = isWhtApplicable;
	   }


	   
	 /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
    	
    	      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductModel");
    	associationModel.setPropertyName("relationProductIdProductModel");   		
   		associationModel.setValue(getRelationProductIdProductModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CommissionStakeholderModel");
    	associationModel.setPropertyName("relationCommissionStakeholderIdCommissionStakeholderModel");   		
   		associationModel.setValue(getRelationCommissionStakeholderIdCommissionStakeholderModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();

   		associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
    	associationModel.setValue(getRelationUpdatedByAppUserModel());

    	associationModelList.add(associationModel);

    	associationModel = new AssociationModel();

    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
    	associationModel.setValue(getRelationCreatedByAppUserModel());
    	associationModelList.add(associationModel);
    	
    	associationModel = new AssociationModel();

    	return associationModelList;
    }

    @Column(name = "IS_FED_APPLICABLE" , nullable = false )
	public Boolean getIsFedApplicable() {
		return isFedApplicable;
	}

	public void setIsFedApplicable(Boolean isFedApplicable) {
		this.isFedApplicable = isFedApplicable;
	}

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        CommissionShSharesDefaultModel vo = new CommissionShSharesDefaultModel();
        vo.setProductId(resultSet.getLong("PRODUCT_ID"));
        vo.setCommissionStakeholderId(resultSet.getLong("STAKEHOLDER_ID"));
        vo.setCommissionShSharesDefaultId(resultSet.getLong("PRODUCT_SH_SHARES_DEF_ID"));
        vo.setCreatedBy(resultSet.getLong("CREATED_BY"));
        vo.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        vo.setCommissionShare(resultSet.getDouble("COMMISSION_SHARE"));
        vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        vo.setCreatedOn(resultSet.getDate("CREATED_ON"));
        vo.setIsWhtApplicable(resultSet.getBoolean("IS_WHT_APPLICABLE"));
        vo.setIsFedApplicable(resultSet.getBoolean("IS_FED_APPLICABLE"));
        return vo;
    }
}
