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
 * The BillServiceLabelModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="BillServiceLabelModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "BILL_SERVICE_LABEL_seq",sequenceName = "BILL_SERVICE_LABEL_seq", allocationSize=1)
@Table(name = "BILL_SERVICE_LABEL")
public class BillServiceLabelModel extends BasePersistableModel implements Serializable{
  

   private ProductModel productIdProductModel;


   private Long billServiceLabelId;
   private String labelText;

   /**
    * Default constructor.
    */
   public BillServiceLabelModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getBillServiceLabelId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setBillServiceLabelId(primaryKey);
    }

   /**
    * Returns the value of the <code>billServiceLabelId</code> property.
    *
    */
   @Column(name = "BILL_SERVICE_LABEL_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BILL_SERVICE_LABEL_seq")
   public Long getBillServiceLabelId() {
      return billServiceLabelId;
   }

   /**
    * Sets the value of the <code>billServiceLabelId</code> property.
    *
    * @param billServiceLabelId the value for the <code>billServiceLabelId</code> property
    *    
		    */

   public void setBillServiceLabelId(Long billServiceLabelId) {
      this.billServiceLabelId = billServiceLabelId;
   }

   /**
    * Returns the value of the <code>labelText</code> property.
    *
    */
      @Column(name = "LABEL_TEXT" , nullable = false , length=50 )
   public String getLabelText() {
      return labelText;
   }

   /**
    * Sets the value of the <code>labelText</code> property.
    *
    * @param labelText the value for the <code>labelText</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setLabelText(String labelText) {
      this.labelText = labelText;
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
        checkBox += "_"+ getBillServiceLabelId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&billServiceLabelId=" + getBillServiceLabelId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "billServiceLabelId";
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
   		
			    	
    	return associationModelList;
    }    
          
}
