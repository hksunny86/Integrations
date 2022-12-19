package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ProductDeviceFlowModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ProductDeviceFlowModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PRODUCT_DEVICE_FLOW_seq",sequenceName = "PRODUCT_DEVICE_FLOW_seq", allocationSize=1)
@Table(name = "PRODUCT_DEVICE_FLOW")
public class ProductDeviceFlowModel extends BasePersistableModel implements Serializable{
  

   
   private static final long serialVersionUID = 9197819859126417478L;

   private ProductModel productIdProductModel;
   private DeviceFlowModel deviceFlowIdDeviceFlowModel;
   private DeviceTypeModel deviceTypeIdDeviceTypeModel;


   private Long productDeviceFlowId;

   /**
    * Default constructor.
    */
   public ProductDeviceFlowModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getProductDeviceFlowId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setProductDeviceFlowId(primaryKey);
    }

   /**
    * Returns the value of the <code>productDeviceFlowId</code> property.
    *
    */
      @Column(name = "PRODUCT_DEVICE_FLOW_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUCT_DEVICE_FLOW_seq")
   public Long getProductDeviceFlowId() {
      return productDeviceFlowId;
   }

   /**
    * Sets the value of the <code>productDeviceFlowId</code> property.
    *
    * @param productDeviceFlowId the value for the <code>productDeviceFlowId</code> property
    *    
		    */

   public void setProductDeviceFlowId(Long productDeviceFlowId) {
      this.productDeviceFlowId = productDeviceFlowId;
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
    * Returns the value of the <code>deviceFlowIdDeviceFlowModel</code> relation property.
    *
    * @return the value of the <code>deviceFlowIdDeviceFlowModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "DEVICE_FLOW_ID")    
   public DeviceFlowModel getRelationDeviceFlowIdDeviceFlowModel(){
      return deviceFlowIdDeviceFlowModel;
   }
    
   /**
    * Returns the value of the <code>deviceFlowIdDeviceFlowModel</code> relation property.
    *
    * @return the value of the <code>deviceFlowIdDeviceFlowModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DeviceFlowModel getDeviceFlowIdDeviceFlowModel(){
      return getRelationDeviceFlowIdDeviceFlowModel();
   }

   /**
    * Sets the value of the <code>deviceFlowIdDeviceFlowModel</code> relation property.
    *
    * @param deviceFlowModel a value for <code>deviceFlowIdDeviceFlowModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationDeviceFlowIdDeviceFlowModel(DeviceFlowModel deviceFlowModel) {
      this.deviceFlowIdDeviceFlowModel = deviceFlowModel;
   }
   
   /**
    * Sets the value of the <code>deviceFlowIdDeviceFlowModel</code> relation property.
    *
    * @param deviceFlowModel a value for <code>deviceFlowIdDeviceFlowModel</code>.
    */
   @javax.persistence.Transient
   public void setDeviceFlowIdDeviceFlowModel(DeviceFlowModel deviceFlowModel) {
      if(null != deviceFlowModel)
      {
      	setRelationDeviceFlowIdDeviceFlowModel((DeviceFlowModel)deviceFlowModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    * @return the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "DEVICE_TYPE_ID")    
   public DeviceTypeModel getRelationDeviceTypeIdDeviceTypeModel(){
      return deviceTypeIdDeviceTypeModel;
   }
    
   /**
    * Returns the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    * @return the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public DeviceTypeModel getDeviceTypeIdDeviceTypeModel(){
      return getRelationDeviceTypeIdDeviceTypeModel();
   }

   /**
    * Sets the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    * @param deviceTypeModel a value for <code>deviceTypeIdDeviceTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationDeviceTypeIdDeviceTypeModel(DeviceTypeModel deviceTypeModel) {
      this.deviceTypeIdDeviceTypeModel = deviceTypeModel;
   }
   
   /**
    * Sets the value of the <code>deviceTypeIdDeviceTypeModel</code> relation property.
    *
    * @param deviceTypeModel a value for <code>deviceTypeIdDeviceTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setDeviceTypeIdDeviceTypeModel(DeviceTypeModel deviceTypeModel) {
      if(null != deviceTypeModel)
      {
      	setRelationDeviceTypeIdDeviceTypeModel((DeviceTypeModel)deviceTypeModel.clone());
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
    * Returns the value of the <code>deviceFlowId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getDeviceFlowId() {
      if (deviceFlowIdDeviceFlowModel != null) {
         return deviceFlowIdDeviceFlowModel.getDeviceFlowId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>deviceFlowId</code> property.
    *
    * @param deviceFlowId the value for the <code>deviceFlowId</code> property
					    * @spring.validator type="required"
							    */
   
   @javax.persistence.Transient
   public void setDeviceFlowId(Long deviceFlowId) {
      if(deviceFlowId == null)
      {      
      	deviceFlowIdDeviceFlowModel = null;
      }
      else
      {
        deviceFlowIdDeviceFlowModel = new DeviceFlowModel();
      	deviceFlowIdDeviceFlowModel.setDeviceFlowId(deviceFlowId);
      }      
   }

   /**
    * Returns the value of the <code>deviceTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getDeviceTypeId() {
      if (deviceTypeIdDeviceTypeModel != null) {
         return deviceTypeIdDeviceTypeModel.getDeviceTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>deviceTypeId</code> property.
    *
    * @param deviceTypeId the value for the <code>deviceTypeId</code> property
							    * @spring.validator type="required"
					    */
   
   @javax.persistence.Transient
   public void setDeviceTypeId(Long deviceTypeId) {
      if(deviceTypeId == null)
      {      
      	deviceTypeIdDeviceTypeModel = null;
      }
      else
      {
        deviceTypeIdDeviceTypeModel = new DeviceTypeModel();
      	deviceTypeIdDeviceTypeModel.setDeviceTypeId(deviceTypeId);
      }      
   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getProductDeviceFlowId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&productDeviceFlowId=" + getProductDeviceFlowId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "productDeviceFlowId";
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
    	
    	associationModel.setClassName("ProductModel");
    	associationModel.setPropertyName("relationProductIdProductModel");   		
   		associationModel.setValue(getRelationProductIdProductModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DeviceFlowModel");
    	associationModel.setPropertyName("relationDeviceFlowIdDeviceFlowModel");   		
   		associationModel.setValue(getRelationDeviceFlowIdDeviceFlowModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("DeviceTypeModel");
    	associationModel.setPropertyName("relationDeviceTypeIdDeviceTypeModel");   		
   		associationModel.setValue(getRelationDeviceTypeIdDeviceTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
