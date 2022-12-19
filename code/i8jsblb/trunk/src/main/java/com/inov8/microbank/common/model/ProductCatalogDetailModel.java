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
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.productmodule.CategoryModel;

/**
 * The ProductCatalogDetailModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ProductCatalogDetailModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PRODUCT_CATALOG_DETAIL_seq",sequenceName = "PRODUCT_CATALOG_DETAIL_seq", allocationSize=1)
@Table(name = "PRODUCT_CATALOG_DETAIL")
public class ProductCatalogDetailModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = 1540787823607579643L;
private ProductCatalogModel productCatalogIdProductCatalogModel;
   private ProductModel productIdProductModel;
   private CategoryModel categoryModel;

   private Long productCatalogDetailId;
   private Integer versionNo;
   private Integer sequenceNo;
   private Boolean showSupplierInMenu;
   /**
    * Default constructor.
    */
   public ProductCatalogDetailModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getProductCatalogDetailId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setProductCatalogDetailId(primaryKey);
    }

   /**
    * Returns the value of the <code>productCatalogDetailId</code> property.
    *
    */
      @Column(name = "PRODUCT_CATALOG_DETAIL_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUCT_CATALOG_DETAIL_seq")
   public Long getProductCatalogDetailId() {
      return productCatalogDetailId;
   }

   /**
    * Sets the value of the <code>productCatalogDetailId</code> property.
    *
    * @param productCatalogDetailId the value for the <code>productCatalogDetailId</code> property
    *    
		    */

   public void setProductCatalogDetailId(Long productCatalogDetailId) {
      this.productCatalogDetailId = productCatalogDetailId;
   }
   
   /**
    * Returns the value of the <code>sequenceNo</code> property.
    *
    */
      @Column(name = "SEQUENCE_NO"  )
   public Integer getSequenceNo() {
      return sequenceNo;
   }

   /**
    * Sets the value of the <code>sequenceNo</code> property.
    *
    * @param sequenceNo the value for the <code>sequenceNo</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setSequenceNo(Integer sequenceNo) {
      this.sequenceNo = sequenceNo;
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
    * Returns the value of the <code>productCatalogIdProductCatalogModel</code> relation property.
    *
    * @return the value of the <code>productCatalogIdProductCatalogModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "PRODUCT_CATALOG_ID")    
   public ProductCatalogModel getRelationProductCatalogIdProductCatalogModel(){
      return productCatalogIdProductCatalogModel;
   }
    
   /**
    * Returns the value of the <code>productCatalogIdProductCatalogModel</code> relation property.
    *
    * @return the value of the <code>productCatalogIdProductCatalogModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ProductCatalogModel getProductCatalogIdProductCatalogModel(){
      return getRelationProductCatalogIdProductCatalogModel();
   }

   /**
    * Sets the value of the <code>productCatalogIdProductCatalogModel</code> relation property.
    *
    * @param productCatalogModel a value for <code>productCatalogIdProductCatalogModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationProductCatalogIdProductCatalogModel(ProductCatalogModel productCatalogModel) {
      this.productCatalogIdProductCatalogModel = productCatalogModel;
   }
   
   /**
    * Sets the value of the <code>productCatalogIdProductCatalogModel</code> relation property.
    *
    * @param productCatalogModel a value for <code>productCatalogIdProductCatalogModel</code>.
    */
   @javax.persistence.Transient
   public void setProductCatalogIdProductCatalogModel(ProductCatalogModel productCatalogModel) {
      if(null != productCatalogModel)
      {
      	setRelationProductCatalogIdProductCatalogModel((ProductCatalogModel)productCatalogModel.clone());
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
    * Returns the value of the <code>productCatalogId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getProductCatalogId() {
      if (productCatalogIdProductCatalogModel != null) {
         return productCatalogIdProductCatalogModel.getProductCatalogId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>productCatalogId</code> property.
    *
    * @param productCatalogId the value for the <code>productCatalogId</code> property
					    * @spring.validator type="required"
							    */
   
   @javax.persistence.Transient
   public void setProductCatalogId(Long productCatalogId) {
      if(productCatalogId == null)
      {      
      	productCatalogIdProductCatalogModel = null;
      }
      else
      {
        productCatalogIdProductCatalogModel = new ProductCatalogModel();
      	productCatalogIdProductCatalogModel.setProductCatalogId(productCatalogId);
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
        checkBox += "_"+ getProductCatalogDetailId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&productCatalogDetailId=" + getProductCatalogDetailId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "productCatalogDetailId";
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
    	
    	associationModel.setClassName("ProductCatalogModel");
    	associationModel.setPropertyName("relationProductCatalogIdProductCatalogModel");   		
   		associationModel.setValue(getRelationProductCatalogIdProductCatalogModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ProductModel");
    	associationModel.setPropertyName("relationProductIdProductModel");   		
   		associationModel.setValue(getRelationProductIdProductModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }
    
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")    
    public CategoryModel getRelationCategoryModel(){
       return categoryModel;
    }
     
    @javax.persistence.Transient
    public CategoryModel getCategoryModel(){
       return getRelationCategoryModel();
    }

    
    @javax.persistence.Transient
    public void setRelationCategoryModel(CategoryModel categoryModel) {
       this.categoryModel = categoryModel;
    }
    
    @javax.persistence.Transient
    public void setCategoryModel(CategoryModel ccategoryModel) {
       if(null != categoryModel)
       {
       	setRelationCategoryModel((CategoryModel)categoryModel.clone());
       }      
    }
    
    
    @javax.persistence.Transient
    public Long getCategoryId() {
       if (categoryModel != null) {
          return categoryModel.getCategoryId();
       } else {
          return null;
       }
    }

    @javax.persistence.Transient
    public void setCategoryId(Long categoryId) {
       if(categoryId == null)
       {      
     	  categoryModel = null;
       }
       else
       {
     	  categoryModel = new CategoryModel();
     	  categoryModel.setCategoryId(categoryId);
       }      
    }
    
    
    @Column(name = "SHOW_SUPPLIER_IN_MENU" , nullable = false )
    public Boolean getShowSupplierInMenu() 
    {
    	return showSupplierInMenu;
    }

    public void setShowSupplierInMenu(Boolean showSupplierInMenu) 
    {
    	this.showSupplierInMenu = showSupplierInMenu;
    }          
}
