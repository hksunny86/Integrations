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
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The AgentNeworkCommShareModel entity bean.
 *
 * @author  yo
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AgentNeworkCommShareModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "AGENT_NETWORK_COMM_SHARE_SEQ",sequenceName = "AGENT_NETWORK_COMM_SHARE_SEQ", allocationSize=1)
@Table(name = "AGENT_NETWORK_COMM_SHARE")
public class AgentNeworkCommShareModel extends BasePersistableModel implements Serializable {
  

	private Long agentNeworkCommShareId;
	private ProductModel productIdProductModel;
	private DistributorModel distributorIdDistributorModel;
	private DistributorLevelModel distributorLevelModel; 
	private DistributorLevelModel sharingDistributorLevelModel; 

	private Double commissionShare;
	private Date updatedOn;
	private Date createdOn;

	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;
  
	private Integer versionNo;
   
   /**
    * Default constructor.
    */
   public AgentNeworkCommShareModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAgentNeworkCommShareId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAgentNeworkCommShareId(primaryKey);
    }

   /**
    * Returns the value of the <code>AgentNeworkCommShareId</code> property.
    *
    */
      @Column(name = "AGENT_NETWORK_COMM_SHARE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AGENT_NETWORK_COMM_SHARE_SEQ")
   public Long getAgentNeworkCommShareId() {
      return agentNeworkCommShareId;
   }

   /**
    * Sets the value of the <code>AgentNeworkCommShareId</code> property.
    *
    * @param AgentNeworkCommShareId the value for the <code>AgentNeworkCommShareId</code> property
    *    
		    */

   public void setAgentNeworkCommShareId(Long agentNeworkCommShareId) {
      this.agentNeworkCommShareId = agentNeworkCommShareId;
   }

   /**
    * Returns the value of the <code>share</code> property.
    *
    */
      @Column(name = "COMMISSION_SHARE" , nullable = false )
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
 
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "DISTRIBUTOR_LEVEL_ID")    
	public DistributorLevelModel getRelationDistributorLevelModel(){
		return distributorLevelModel;
	}
	
	@javax.persistence.Transient
	public DistributorLevelModel getDistributorLevelModel(){
		return getRelationDistributorLevelModel();
	}

	   
	@javax.persistence.Transient
	public void setRelationDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
		this.distributorLevelModel = distributorLevelModel;
	}
	   
	
	@javax.persistence.Transient
	public void setDistributorLevelModel(DistributorLevelModel distributorLevelModel) {
		if(null != distributorLevelModel)
	    {
			setRelationDistributorLevelModel((DistributorLevelModel)distributorLevelModel.clone());
	    }      
	}
	   
	@javax.persistence.Transient
	public Long getDistributorLevelId() {
		if (distributorLevelModel != null) {
			return distributorLevelModel.getDistributorLevelId();
	    } else {
	    	return null;
	    }
	}																																												    
	   
	@javax.persistence.Transient
	public void setDistributorLevelId(Long distributorLevelId) {
		if(distributorLevelId == null)
	    {      
			distributorLevelModel = null;
	    }
	    else
	    {
	    	distributorLevelModel = new DistributorLevelModel();
	    	distributorLevelModel.setDistributorLevelId(distributorLevelId);
	    }      
	}


	//--------------------------------------------------------
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "AGENT_NETWORK_COMM_SHARE")    
	public DistributorLevelModel getRelationSharingDistributorLevelModel(){
		return sharingDistributorLevelModel;
	}
	
	@javax.persistence.Transient
	public DistributorLevelModel getSharingDistributorLevelModel(){
		return getRelationSharingDistributorLevelModel();
	}

	   
	@javax.persistence.Transient
	public void setRelationSharingDistributorLevelModel(DistributorLevelModel sharingDistributorLevelModel) {
		this.sharingDistributorLevelModel = sharingDistributorLevelModel;
	}
	   
	
	@javax.persistence.Transient
	public void setSharingDistributorLevelModel(DistributorLevelModel sharingDistributorLevelModel) {
		if(null != sharingDistributorLevelModel)
	    {
			setRelationSharingDistributorLevelModel((DistributorLevelModel)sharingDistributorLevelModel.clone());
	    }      
	}
	   
	@javax.persistence.Transient
	public Long getSharingDistributorLevelId() {
		if (sharingDistributorLevelModel != null) {
			return sharingDistributorLevelModel.getDistributorLevelId();
	    } else {
	    	return null;
	    }
	}																																												    
	   
	@javax.persistence.Transient
	public void setSharingDistributorLevelId(Long sharingDistributorLevelId) {
		if(sharingDistributorLevelId == null)
	    {      
			sharingDistributorLevelModel = null;
	    }
	    else
	    {
	    	sharingDistributorLevelModel = new DistributorLevelModel();
	    	sharingDistributorLevelModel.setDistributorLevelId(sharingDistributorLevelId);
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAgentNeworkCommShareId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&AgentNeworkCommShareId=" + getAgentNeworkCommShareId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "AgentNeworkCommShareId";
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
	    * Returns the value of the <code>distributorIdDistributorModel</code> relation property.
	    *
	    * @return the value of the <code>distributorIdDistributorModel</code> relation property.
	    *
	    */
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "DISTRIBUTOR_ID")    
	   public DistributorModel getRelationDistributorIdDistributorModel(){
	      return distributorIdDistributorModel;
	   }
	    
	   /**
	    * Returns the value of the <code>distributorIdDistributorModel</code> relation property.
	    *
	    * @return the value of the <code>distributorIdDistributorModel</code> relation property.
	    *
	    */
	   @javax.persistence.Transient
	   public DistributorModel getDistributorIdDistributorModel(){
	      return getRelationDistributorIdDistributorModel();
	   }

	   /**
	    * Sets the value of the <code>distributorIdDistributorModel</code> relation property.
	    *
	    * @param distributorModel a value for <code>distributorIdDistributorModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setRelationDistributorIdDistributorModel(DistributorModel distributorModel) {
	      this.distributorIdDistributorModel = distributorModel;
	   }
	   
	   
	   @javax.persistence.Transient
	   public String getAgentNetworkName()
	   {
		   if(distributorIdDistributorModel != null)
		   {
			   return distributorIdDistributorModel.getName();
		   }
		   else
		   {
			   return null;
		   }	   
	   }
	   
	   
	   /**
	    * Sets the value of the <code>distributorIdDistributorModel</code> relation property.
	    *
	    * @param distributorModel a value for <code>distributorIdDistributorModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setDistributorIdDistributorModel(DistributorModel distributorModel) {
	      if(null != distributorModel)
	      {
	      	setRelationDistributorIdDistributorModel((DistributorModel)distributorModel.clone());
	      }      
	   }


	   /**
	    * Returns the value of the <code>distributorId</code> property.
	    *
	    */
	   @javax.persistence.Transient
	   public Long getDistributorId() {
	      if (distributorIdDistributorModel != null) {
	         return distributorIdDistributorModel.getDistributorId();
	      } else {
	         return null;
	      }
	   }

	   /**
	    * Sets the value of the <code>distributorId</code> property.
	    *
	    * @param distributorId the value for the <code>distributorId</code> property
										    * @spring.validator type="required"
																																												    */
	   
	   @javax.persistence.Transient
	   public void setDistributorId(Long distributorId) {
	      if(distributorId == null)
	      {      
	      	distributorIdDistributorModel = null;
	      }
	      else
	      {
	        distributorIdDistributorModel = new DistributorModel();
	      	distributorIdDistributorModel.setDistributorId(distributorId);
	      }      
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

   		associationModel.setClassName("DistributorModel");
   		associationModel.setPropertyName("relationDistributorIdDistributorModel");   		
   		associationModel.setValue(getRelationDistributorIdDistributorModel());
		associationModelList.add(associationModel);
		
		associationModel = new AssociationModel();
   		
    	associationModel.setClassName("DistributorLevelModel");
    	associationModel.setPropertyName("distributorLevelModel");   		
   		associationModel.setValue(getRelationDistributorLevelModel());
   		
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
   		
    	associationModel.setClassName("DistributorLevelModel");
    	associationModel.setPropertyName("sharingDistributorLevelModel");   		
   		associationModel.setValue(getRelationSharingDistributorLevelModel());
   		
   		associationModelList.add(associationModel);
    	return associationModelList;
    }    
          
}
