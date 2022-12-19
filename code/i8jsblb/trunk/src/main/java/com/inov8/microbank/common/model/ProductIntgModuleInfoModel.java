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
 * The ProductIntgModuleInfoModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ProductIntgModuleInfoModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PRODUCT_INTG_MODULE_INFO_seq",sequenceName = "PRODUCT_INTG_MODULE_INFO_seq", allocationSize=1)
@Table(name = "PRODUCT_INTG_MODULE_INFO")
public class ProductIntgModuleInfoModel extends BasePersistableModel implements Serializable{
  


   /**
	 * 
	 */
	private static final long serialVersionUID = 4162238944083033292L;

private Collection<ProductModel> productIntgModuleInfoIdProductModelList = new ArrayList<ProductModel>();

   private Long productIntgModuleInfoId;
   private String className;
   private String queueName;
   private Boolean jmsRequired;
   private String description;
   private Boolean active;
   private String userId;
   private String password;
   private String url;
   private String name;

   /**
    * Default constructor.
    */
   public ProductIntgModuleInfoModel() {
   }   

   public ProductIntgModuleInfoModel( Long productIntgModuleInfoId )
   {
       this.productIntgModuleInfoId = productIntgModuleInfoId;
   }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getProductIntgModuleInfoId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setProductIntgModuleInfoId(primaryKey);
    }

   /**
    * Returns the value of the <code>productIntgModuleInfoId</code> property.
    *
    */
      @Column(name = "PRODUCT_INTG_MODULE_INFO_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUCT_INTG_MODULE_INFO_seq")
   public Long getProductIntgModuleInfoId() {
      return productIntgModuleInfoId;
   }

   /**
    * Sets the value of the <code>productIntgModuleInfoId</code> property.
    *
    * @param productIntgModuleInfoId the value for the <code>productIntgModuleInfoId</code> property
    *    
		    */

   public void setProductIntgModuleInfoId(Long productIntgModuleInfoId) {
      this.productIntgModuleInfoId = productIntgModuleInfoId;
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
    * Returns the value of the <code>queueName</code> property.
    *
    */
      @Column(name = "QUEUE_NAME"  , length=250 )
   public String getQueueName() {
      return queueName;
   }

   /**
    * Sets the value of the <code>queueName</code> property.
    *
    * @param queueName the value for the <code>queueName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setQueueName(String queueName) {
      this.queueName = queueName;
   }

   /**
    * Returns the value of the <code>jmsRequired</code> property.
    *
    */
      @Column(name = "IS_JMS_REQUIRED" , nullable = false )
   public Boolean getJmsRequired() {
      return jmsRequired;
   }

   /**
    * Sets the value of the <code>jmsRequired</code> property.
    *
    * @param jmsRequired the value for the <code>jmsRequired</code> property
    *    
		    */

   public void setJmsRequired(Boolean jmsRequired) {
      this.jmsRequired = jmsRequired;
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
    * Returns the value of the <code>userId</code> property.
    *
    */
      @Column(name = "USER_ID"  , length=50 )
   public String getUserId() {
      return userId;
   }

   /**
    * Sets the value of the <code>userId</code> property.
    *
    * @param userId the value for the <code>userId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setUserId(String userId) {
      this.userId = userId;
   }

   /**
    * Returns the value of the <code>password</code> property.
    *
    */
      @Column(name = "PASSWORD"  , length=50 )
   public String getPassword() {
      return password;
   }

   /**
    * Sets the value of the <code>password</code> property.
    *
    * @param password the value for the <code>password</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setPassword(String password) {
      this.password = password;
   }

   /**
    * Returns the value of the <code>url</code> property.
    *
    */
      @Column(name = "URL"  , length=250 )
   public String getUrl() {
      return url;
   }

   /**
    * Sets the value of the <code>url</code> property.
    *
    * @param url the value for the <code>url</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setUrl(String url) {
      this.url = url;
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
    * Add the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be added.
    */
    
   public void addProductIntgModuleInfoIdProductModel(ProductModel productModel) {
      productModel.setRelationProductIntgModuleInfoIdProductIntgModuleInfoModel(this);
      productIntgModuleInfoIdProductModelList.add(productModel);
   }
   
   /**
    * Remove the related ProductModel to this one-to-many relation.
    *
    * @param productModel object to be removed.
    */
   
   public void removeProductIntgModuleInfoIdProductModel(ProductModel productModel) {      
      productModel.setRelationProductIntgModuleInfoIdProductIntgModuleInfoModel(null);
      productIntgModuleInfoIdProductModelList.remove(productModel);      
   }

   /**
    * Get a list of related ProductModel objects of the ProductIntgModuleInfoModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductIntgModuleInfoId member.
    *
    * @return Collection of ProductModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationProductIntgModuleInfoIdProductIntgModuleInfoModel")
   @JoinColumn(name = "PRODUCT_INTG_MODULE_INFO_ID")
   public Collection<ProductModel> getProductIntgModuleInfoIdProductModelList() throws Exception {
   		return productIntgModuleInfoIdProductModelList;
   }


   /**
    * Set a list of ProductModel related objects to the ProductIntgModuleInfoModel object.
    * These objects are in a bidirectional one-to-many relation by the ProductIntgModuleInfoId member.
    *
    * @param productModelList the list of related objects.
    */
    public void setProductIntgModuleInfoIdProductModelList(Collection<ProductModel> productModelList) throws Exception {
		this.productIntgModuleInfoIdProductModelList = productModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getProductIntgModuleInfoId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&productIntgModuleInfoId=" + getProductIntgModuleInfoId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "productIntgModuleInfoId";
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
