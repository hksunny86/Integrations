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
 * The CustomerTypeModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CustomerTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CUSTOMER_TYPE_seq",sequenceName = "CUSTOMER_TYPE_seq", allocationSize=1)
@Table(name = "CUSTOMER_TYPE")
public class CustomerTypeModel extends BasePersistableModel implements Serializable {
  


   private Collection<CustomerModel> customerTypeIdCustomerModelList = new ArrayList<CustomerModel>();

   private Long customerTypeId;
   private String name;

   /**
    * Default constructor.
    */
   public CustomerTypeModel() {
   }   

   public CustomerTypeModel(Long customerTypeId)
   {
	   this.customerTypeId = customerTypeId;
   }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCustomerTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCustomerTypeId(primaryKey);
    }

   /**
    * Returns the value of the <code>customerTypeId</code> property.
    *
    */
      @Column(name = "CUSTOMER_TYPE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUSTOMER_TYPE_seq")
   public Long getCustomerTypeId() {
      return customerTypeId;
   }

   /**
    * Sets the value of the <code>customerTypeId</code> property.
    *
    * @param customerTypeId the value for the <code>customerTypeId</code> property
    *    
		    */

   public void setCustomerTypeId(Long customerTypeId) {
      this.customerTypeId = customerTypeId;
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
    * Add the related CustomerModel to this one-to-many relation.
    *
    * @param customerModel object to be added.
    */
    
   public void addCustomerTypeIdCustomerModel(CustomerModel customerModel) {
      customerModel.setRelationCustomerTypeIdCustomerTypeModel(this);
      customerTypeIdCustomerModelList.add(customerModel);
   }
   
   /**
    * Remove the related CustomerModel to this one-to-many relation.
    *
    * @param customerModel object to be removed.
    */
   
   public void removeCustomerTypeIdCustomerModel(CustomerModel customerModel) {      
      customerModel.setRelationCustomerTypeIdCustomerTypeModel(null);
      customerTypeIdCustomerModelList.remove(customerModel);      
   }

   /**
    * Get a list of related CustomerModel objects of the CustomerTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the CustomerTypeId member.
    *
    * @return Collection of CustomerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCustomerTypeIdCustomerTypeModel")
   @JoinColumn(name = "CUSTOMER_TYPE_ID")
   public Collection<CustomerModel> getCustomerTypeIdCustomerModelList() throws Exception {
   		return customerTypeIdCustomerModelList;
   }


   /**
    * Set a list of CustomerModel related objects to the CustomerTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the CustomerTypeId member.
    *
    * @param customerModelList the list of related objects.
    */
    public void setCustomerTypeIdCustomerModelList(Collection<CustomerModel> customerModelList) throws Exception {
		this.customerTypeIdCustomerModelList = customerModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCustomerTypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&customerTypeId=" + getCustomerTypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "customerTypeId";
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
