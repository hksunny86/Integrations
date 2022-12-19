package com.inov8.microbank.common.model;

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

/**
 * The SegmentModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="SegmentModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "SEGMENT_seq",sequenceName = "SEGMENT_seq",allocationSize=1)
@Table(name = "SEGMENT")
public class SegmentModel extends BasePersistableModel implements Serializable {
  


   private Collection<CommissionRateModel> segmentIdCommissionRateModelList = new ArrayList<CommissionRateModel>();
   private Collection<CustomerModel> segmentIdCustomerModelList = new ArrayList<CustomerModel>();

   private Long segmentId;
   private String name;
   private String description;
   private Boolean isActive;
   private Integer versionNo;
   private AppUserModel createdByAppUserModel;
   private Date createdOn;
   private AppUserModel updatedByAppUserModel;
   private Date updatedOn;

   /**
    * Default constructor.
    */
   public SegmentModel() {
   }   

   public SegmentModel(Long segmentId) {
		super();
		this.segmentId = segmentId;
   }


	/**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getSegmentId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setSegmentId(primaryKey);
    }

   /**
    * Returns the value of the <code>segmentId</code> property.
    *
    */
      @Column(name = "SEGMENT_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEGMENT_seq")
   public Long getSegmentId() {
      return segmentId;
   }

   /**
    * Sets the value of the <code>segmentId</code> property.
    *
    * @param segmentId the value for the <code>segmentId</code> property
    *    
		    */

   public void setSegmentId(Long segmentId) {
      this.segmentId = segmentId;
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
   @Column(name = "DESCRIPTION" , nullable = true , length=250 )
  public String getDescription() {
     return description;
  }

  public void setDescription(String des) {
     this.description = des;
  }
  @Column(name = "IS_ACTIVE" , nullable = false)
  public Boolean getIsActive() {
     return isActive;
  }

  public void setIsActive(Boolean isActive) {
     this.isActive = isActive ;
  }
  
  
  @Column(name = "CREATED_ON" , nullable = false)
  public Date getCreatedOn() {
     return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
     this.createdOn =createdOn;
  }
  
  @Column(name = "UPDATED_ON" , nullable = false)
  public Date getUpdatedOn() {
     return updatedOn;
  }

  public void setUpdatedOn(Date updatedOn) {
     this.updatedOn =updatedOn;
  }
  
  @Version
  @Column(name = "VERSION_NO" , nullable = false)
  public Integer getVersionNo() {
     return versionNo;
  }

  public void setVersionNo(Integer versionNo) {
     this.versionNo=versionNo;
  }
  

   /**
    * Add the related CommissionRateModel to this one-to-many relation.
    *
    * @param commissionRateModel object to be added.
    */
    
   public void addSegmentIdCommissionRateModel(CommissionRateModel commissionRateModel) {
      commissionRateModel.setRelationSegmentIdSegmentModel(this);
      segmentIdCommissionRateModelList.add(commissionRateModel);
   }
   
   /**
    * Remove the related CommissionRateModel to this one-to-many relation.
    *
    * @param commissionRateModel object to be removed.
    */
   
   public void removeSegmentIdCommissionRateModel(CommissionRateModel commissionRateModel) {      
      commissionRateModel.setRelationSegmentIdSegmentModel(null);
      segmentIdCommissionRateModelList.remove(commissionRateModel);      
   }

   /**
    * Get a list of related CommissionRateModel objects of the SegmentModel object.
    * These objects are in a bidirectional one-to-many relation by the SegmentId member.
    *
    * @return Collection of CommissionRateModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSegmentIdSegmentModel")
   @JoinColumn(name = "SEGMENT_ID")
   public Collection<CommissionRateModel> getSegmentIdCommissionRateModelList() throws Exception {
   		return segmentIdCommissionRateModelList;
   }


   /**
    * Set a list of CommissionRateModel related objects to the SegmentModel object.
    * These objects are in a bidirectional one-to-many relation by the SegmentId member.
    *
    * @param commissionRateModelList the list of related objects.
    */
    public void setSegmentIdCommissionRateModelList(Collection<CommissionRateModel> commissionRateModelList) throws Exception {
		this.segmentIdCommissionRateModelList = commissionRateModelList;
   }


   /**
    * Add the related CustomerModel to this one-to-many relation.
    *
    * @param customerModel object to be added.
    */
    
   public void addSegmentIdCustomerModel(CustomerModel customerModel) {
      customerModel.setRelationSegmentIdSegmentModel(this);
      segmentIdCustomerModelList.add(customerModel);
   }
   
   /**
    * Remove the related CustomerModel to this one-to-many relation.
    *
    * @param customerModel object to be removed.
    */
   
   public void removeSegmentIdCustomerModel(CustomerModel customerModel) {      
      customerModel.setRelationSegmentIdSegmentModel(null);
      segmentIdCustomerModelList.remove(customerModel);      
   }

   /**
    * Get a list of related CustomerModel objects of the SegmentModel object.
    * These objects are in a bidirectional one-to-many relation by the SegmentId member.
    *
    * @return Collection of CustomerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationSegmentIdSegmentModel")
   @JoinColumn(name = "SEGMENT_ID")
   public Collection<CustomerModel> getSegmentIdCustomerModelList() throws Exception {
   		return segmentIdCustomerModelList;
   }


   /**
    * Set a list of CustomerModel related objects to the SegmentModel object.
    * These objects are in a bidirectional one-to-many relation by the SegmentId member.
    *
    * @param customerModelList the list of related objects.
    */
    public void setSegmentIdCustomerModelList(Collection<CustomerModel> customerModelList) throws Exception {
		this.segmentIdCustomerModelList = customerModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getSegmentId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&segmentId=" + getSegmentId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "segmentId";
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
    
/////Account CreatedBy///////
	
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
	
/////Account UpdatedBy///////
	
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
          
}
