package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ProductIntgVoModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ProductIntgVoModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PRODUCT_INTG_VO_seq",sequenceName = "PRODUCT_INTG_VO_seq", allocationSize=1)
@Table(name = "PRODUCT_INTG_VO")
public class ProductIntgVoModel extends BasePersistableModel implements Serializable{
  


   /**
	 * 
	 */
	private static final long serialVersionUID = 1356667345098137203L;

private Collection<ProductModel> productIntgVoIdProductModelList = new ArrayList<ProductModel>();

   private Long productIntgVoId;
   private String name;
   private String className;
   private String description;
   private Boolean active;

   /**
    * Default constructor.
    */
   public ProductIntgVoModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getProductIntgVoId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setProductIntgVoId(primaryKey);
    }

   /**
    * Returns the value of the <code>productIntgVoId</code> property.
    *
    */
      @Column(name = "PRODUCT_INTG_VO_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUCT_INTG_VO_seq")
   public Long getProductIntgVoId() {
      return productIntgVoId;
   }

   /**
    * Sets the value of the <code>productIntgVoId</code> property.
    *
    * @param productIntgVoId the value for the <code>productIntgVoId</code> property
    *    
		    */

   public void setProductIntgVoId(Long productIntgVoId) {
      this.productIntgVoId = productIntgVoId;
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

   /**
    * Returns the value of the <code>className</code> property.
    *
    */
      @Column(name = "CLASS_NAME" , nullable = false , length=250 )
   public String getClassName() {
      return className;
   }

   /**
    * Sets the value of the <code>className</code> property.
    *
    * @param className the value for the <code>className</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setClassName(String className) {
      this.className = className;
   }

   /**
    * Returns the value of the <code>description</code> property.
    *
    */
      @Column(name = "DESCRIPTION"  , length=250 )
   public String getDescription() {
      return description;
   }

   /**
    * Sets the value of the <code>description</code> property.
    *
    * @param description the value for the <code>description</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * Returns the value of the <code>active</code> property.
    *
    */
      @Column(name = "IS_ACTIVE" , nullable = false )
   public Boolean getActive() {
      return active;
   }

   /**
    * Sets the value of the <code>active</code> property.
    *
    * @param active the value for the <code>active</code> property
    *    
		    */

   public void setActive(Boolean active) {
      this.active = active;
   }


   /**
    * Add the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be added.
    */
    
   public void addProductIntgVoIdProductModel(ProductModel productModel) {
      productModel.setRelationProductIntgVoIdProductIntgVoModel(this);
      productIntgVoIdProductModelList.add(productModel);
   }
   
   /**
    * Remove the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be removed.
    */
   
   public void removeProductIntgVoIdProductModel(ProductModel productModel) {      
      productModel.setRelationProductIntgVoIdProductIntgVoModel(null);
      productIntgVoIdProductModelList.remove(productModel);      
   }

   /**
    * Get a list of related ProductModel objects of the ProductIntgVoModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductIntgVoId member.
    *
    * @return Collection of ProductModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIntgVoIdProductIntgVoModel")
   @JoinColumn(name = "PRODUCT_INTG_VO_ID")
   public Collection<ProductModel> getProductIntgVoIdProductModelList() throws Exception {
   		return productIntgVoIdProductModelList;
   }


   /**
    * Set a list of ProductModel related objects to the ProductIntgVoModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductIntgVoId member.
    *
    * @param productModelList the list of related objects.
    */
    public void setProductIntgVoIdProductModelList(Collection<ProductModel> productModelList) throws Exception {
		this.productIntgVoIdProductModelList = productModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getProductIntgVoId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&productIntgVoId=" + getProductIntgVoId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "productIntgVoId";
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
    	
    	    	
    	return associationModelList;
    }    
          
}
