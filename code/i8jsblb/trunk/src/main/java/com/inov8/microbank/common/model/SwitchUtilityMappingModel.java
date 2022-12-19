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
 * The SwitchUtilityMappingModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="SwitchUtilityMappingModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "SWITCH_UTILITY_MAPPING_seq",sequenceName = "SWITCH_UTILITY_MAPPING_seq", allocationSize=1)
@Table(name = "SWITCH_UTILITY_MAPPING")
public class SwitchUtilityMappingModel extends BasePersistableModel implements Serializable {
  

   /**
	 * 
	 */
	private static final long	serialVersionUID	= 7182720527672709162L;
private SwitchModel switchIdSwitchModel;
   private ProductModel productIdProductModel;


   private Long utilityCompanySwitchId;
   private String utilityCompanyCode;
   private Date createdOn;
   private Date updatedOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public SwitchUtilityMappingModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getUtilityCompanySwitchId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setUtilityCompanySwitchId(primaryKey);
    }

   /**
    * Returns the value of the <code>utilityCompanySwitchId</code> property.
    *
    */
      @Column(name = "UTILITY_COMPANY_SWITCH_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SWITCH_UTILITY_MAPPING_seq")
   public Long getUtilityCompanySwitchId() {
      return utilityCompanySwitchId;
   }

   /**
    * Sets the value of the <code>utilityCompanySwitchId</code> property.
    *
    * @param utilityCompanySwitchId the value for the <code>utilityCompanySwitchId</code> property
    *    
		    */

   public void setUtilityCompanySwitchId(Long utilityCompanySwitchId) {
      this.utilityCompanySwitchId = utilityCompanySwitchId;
   }

   /**
    * Returns the value of the <code>utilityCompanyCode</code> property.
    *
    */
      @Column(name = "UTILITY_COMPANY_CODE" , nullable = false , length=50 )
   public String getUtilityCompanyCode() {
      return utilityCompanyCode;
   }

   /**
    * Sets the value of the <code>utilityCompanyCode</code> property.
    *
    * @param utilityCompanyCode the value for the <code>utilityCompanyCode</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setUtilityCompanyCode(String utilityCompanyCode) {
      this.utilityCompanyCode = utilityCompanyCode;
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
    * Returns the value of the <code>switchIdSwitchModel</code> relation property.
    *
    * @return the value of the <code>switchIdSwitchModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SWITCH_ID")    
   public SwitchModel getRelationSwitchIdSwitchModel(){
      return switchIdSwitchModel;
   }
    
   /**
    * Returns the value of the <code>switchIdSwitchModel</code> relation property.
    *
    * @return the value of the <code>switchIdSwitchModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public SwitchModel getSwitchIdSwitchModel(){
      return getRelationSwitchIdSwitchModel();
   }

   /**
    * Sets the value of the <code>switchIdSwitchModel</code> relation property.
    *
    * @param switchModel a value for <code>switchIdSwitchModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationSwitchIdSwitchModel(SwitchModel switchModel) {
      this.switchIdSwitchModel = switchModel;
   }
   
   /**
    * Sets the value of the <code>switchIdSwitchModel</code> relation property.
    *
    * @param switchModel a value for <code>switchIdSwitchModel</code>.
    */
   @javax.persistence.Transient
   public void setSwitchIdSwitchModel(SwitchModel switchModel) {
      if(null != switchModel)
      {
      	setRelationSwitchIdSwitchModel((SwitchModel)switchModel.clone());
      }      
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
    * Returns the value of the <code>switchId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getSwitchId() {
      if (switchIdSwitchModel != null) {
         return switchIdSwitchModel.getSwitchId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>switchId</code> property.
    *
    * @param switchId the value for the <code>switchId</code> property
					    * @spring.validator type="required"
													    */
   
   @javax.persistence.Transient
   public void setSwitchId(Long switchId) {
      if(switchId == null)
      {      
      	switchIdSwitchModel = null;
      }
      else
      {
        switchIdSwitchModel = new SwitchModel();
      	switchIdSwitchModel.setSwitchId(switchId);
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
        checkBox += "_"+ getUtilityCompanySwitchId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&utilityCompanySwitchId=" + getUtilityCompanySwitchId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "utilityCompanySwitchId";
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
    	
    	associationModel.setClassName("SwitchModel");
    	associationModel.setPropertyName("relationSwitchIdSwitchModel");   		
   		associationModel.setValue(getRelationSwitchIdSwitchModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductModel");
    	associationModel.setPropertyName("relationProductIdProductModel");   		
   		associationModel.setValue(getRelationProductIdProductModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
