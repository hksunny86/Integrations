package com.inov8.integration.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

/**
 * The CustomerAccountTypeModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CustomerAccountTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "OLA_CUSTOMER_ACCOUNT_TYPE_seq",sequenceName = "OLA_CUSTOMER_ACCOUNT_TYPE_seq", allocationSize=1)
@Table(name = "OLA_CUSTOMER_ACCOUNT_TYPE")
public class OlaCustomerAccountTypeModel extends BasePersistableModel implements Serializable {
  


   private Collection<LimitModel> customerAccountTypeIdLimitModelList = new ArrayList<LimitModel>();

   private Long customerAccountTypeId;
   private String name;
   private Boolean active;
   private Date updatedOn;
   private Date createdOn;
   private Integer versionNo;
   
   //added by Turab, isCustomerAccountType
   private Boolean isCustomerAccountType;
   private String  customerAccountTypeCategoryName;
 
   private OlaCustomerAccountTypeModel parentOlaCustomerAccountTypeModel;

   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;
   
   private Boolean dormantMarkingEnabled;
   private Double dormantTimePeriod;
   
   /**
    * Default constructor.
    */
   public OlaCustomerAccountTypeModel() {
   }  
   public OlaCustomerAccountTypeModel(Long customerAccountTypeId) {
	   this.customerAccountTypeId = customerAccountTypeId;
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCustomerAccountTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCustomerAccountTypeId(primaryKey);
    }

   /**
    * Returns the value of the <code>customerAccountTypeId</code> property.
    *
    */
      @Column(name = "CUSTOMER_ACCOUNT_TYPE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OLA_CUSTOMER_ACCOUNT_TYPE_seq")
   public Long getCustomerAccountTypeId() {
      return customerAccountTypeId;
   }

   /**
    * Sets the value of the <code>customerAccountTypeId</code> property.
    *
    * @param customerAccountTypeId the value for the <code>customerAccountTypeId</code> property
    *    
		    */

   public void setCustomerAccountTypeId(Long customerAccountTypeId) {
      this.customerAccountTypeId = customerAccountTypeId;
   }

   /**
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME" , nullable = false , length=50 )
   public String getName() {
      return name;
   }

   /**
    * Sets the value of the <code>name</code> property.
    *
    * @param name the value for the <code>name</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setName(String name) {
      this.name = name;
   }

    @Column( name = "IS_ACTIVE")
    public Boolean getActive()
    {
        return active;
    }

    public void setActive( Boolean active )
    {
        this.active = active;
    }

/**
    * Add the related LimitModel to this one-to-many relation.
    *
    * @param limitModel object to be added.
    */
    
   public void addCustomerAccountTypeIdLimitModel(LimitModel limitModel) {
      limitModel.setRelationCustomerAccountTypeIdCustomerAccountTypeModel(this);
      customerAccountTypeIdLimitModelList.add(limitModel);
   }
   
   /**
    * Remove the related LimitModel to this one-to-many relation.
    *
    * @param limitModel object to be removed.
    */
   
   public void removeCustomerAccountTypeIdLimitModel(LimitModel limitModel) {      
      limitModel.setRelationCustomerAccountTypeIdCustomerAccountTypeModel(null);
      customerAccountTypeIdLimitModelList.remove(limitModel);      
   }

   /**
    * Get a list of related LimitModel objects of the CustomerAccountTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the CustomerAccountTypeId member.
    *
    * @return Collection of LimitModel objects.
    *
    */
   
   @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, 
		   fetch = FetchType.LAZY, mappedBy = "relationCustomerAccountTypeIdCustomerAccountTypeModel")
   @JoinColumn(name = "CUSTOMER_ACCOUNT_TYPE_ID")
   @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
       org.hibernate.annotations.CascadeType.PERSIST,
       org.hibernate.annotations.CascadeType.MERGE,
       org.hibernate.annotations.CascadeType.REFRESH})
   public Collection<LimitModel> getCustomerAccountTypeIdLimitModelList() {
   		return customerAccountTypeIdLimitModelList;
   }


   /**
    * Set a list of LimitModel related objects to the CustomerAccountTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the CustomerAccountTypeId member.
    *
    * @param limitModelList the list of related objects.
    */
    public void setCustomerAccountTypeIdLimitModelList(Collection<LimitModel> limitModelList) {
		this.customerAccountTypeIdLimitModelList = limitModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCustomerAccountTypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&customerAccountTypeId=" + getCustomerAccountTypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "customerAccountTypeId";
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

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "PARENT_ACCOUNT_TYPE_ID")
    public OlaCustomerAccountTypeModel getParentOlaCustomerAccountTypeModel()
    {
		return parentOlaCustomerAccountTypeModel;
	}

    public void setParentOlaCustomerAccountTypeModel(OlaCustomerAccountTypeModel parentOlaCustomerAccountTypeModel)
    {
		this.parentOlaCustomerAccountTypeModel = parentOlaCustomerAccountTypeModel;
	}

    @Transient
    public Long getParentAccountTypeId()
	{
		return parentOlaCustomerAccountTypeModel == null ? null : parentOlaCustomerAccountTypeModel.getCustomerAccountTypeId();
	}

	public void setParentAccountTypeId(Long parentAccountTypeId)
	{
		if(parentAccountTypeId == null)
		{
			parentOlaCustomerAccountTypeModel = null;
		}
		else
		{
			parentOlaCustomerAccountTypeModel = new OlaCustomerAccountTypeModel(parentAccountTypeId);
		}
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
    
    @Column( name = "IS_CUSTOMER_ACCOUNT_TYPE")
	public Boolean getIsCustomerAccountType() {
		return isCustomerAccountType;
	}
	public void setIsCustomerAccountType(Boolean isCustomerAccountType) {
		this.isCustomerAccountType = isCustomerAccountType;
	}
	
	 @Column(name = "IS_DORMANT_ENABLED")
	    public Boolean getDormantMarkingEnabled() {
	        return dormantMarkingEnabled;
	    }

	    public void setDormantMarkingEnabled(Boolean dormantMarkingEnabled) {
	        this.dormantMarkingEnabled = dormantMarkingEnabled;
	    }

	    @Column(name = "DORMANT_TIME_PERIOD")
	    public Double getDormantTimePeriod() {
	        return dormantTimePeriod;
	    }

	    public void setDormantTimePeriod(Double dormantTimePeriod) {
	        this.dormantTimePeriod = dormantTimePeriod;
	    }
	
	@javax.persistence.Transient
	public String getCustomerAccountTypeCategoryName() {
		if ( this.getIsCustomerAccountType() != null ){
			if ( this.getIsCustomerAccountType() == false ){
				customerAccountTypeCategoryName = "Agent";
			}else{
				customerAccountTypeCategoryName = "Customer";
			}
		}/*else{
			customerAccountTypeCategoryName = "Undefined";
		}*/
		return customerAccountTypeCategoryName;
	}

	public void setCustomerAccountTypeCategoryName(
			String customerAccountTypeCategoryName) {
		this.customerAccountTypeCategoryName = customerAccountTypeCategoryName;
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
    	associationModel.setClassName("OlaCustomerAccountTypeModel");
    	associationModel.setPropertyName("parentOlaCustomerAccountTypeModel");   		
    	associationModel.setValue(getParentOlaCustomerAccountTypeModel());

    	associationModelList.add(associationModel);
    	
    	return associationModelList;
    }
    
}
