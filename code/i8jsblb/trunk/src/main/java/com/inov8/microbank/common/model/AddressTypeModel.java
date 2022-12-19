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
 * The AddressTypeModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AddressTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ADDRESS_TYPE_seq",sequenceName = "ADDRESS_TYPE_seq",allocationSize=1)
@Table(name = "ADDRESS_TYPE")
public class AddressTypeModel extends BasePersistableModel implements Serializable {
  


   private Collection<CustomerAddressesModel> addressTypeIdCustomerAddressesModelList = new ArrayList<CustomerAddressesModel>();
   private Collection<RetailerContactAddressesModel> addressTypeIdRetailerContactAddressesModelList = new ArrayList<RetailerContactAddressesModel>();

   private Long addressTypeId;
   private String name;

   /**
    * Default constructor.
    */
   public AddressTypeModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAddressTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAddressTypeId(primaryKey);
    }

   /**
    * Returns the value of the <code>addressTypeId</code> property.
    *
    */
      @Column(name = "ADDRESS_TYPE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ADDRESS_TYPE_seq")
   public Long getAddressTypeId() {
      return addressTypeId;
   }

   /**
    * Sets the value of the <code>addressTypeId</code> property.
    *
    * @param addressTypeId the value for the <code>addressTypeId</code> property
    *    
		    */

   public void setAddressTypeId(Long addressTypeId) {
      this.addressTypeId = addressTypeId;
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
    * Add the related CustomerAddressesModel to this one-to-many relation.
    *
    * @param customerAddressesModel object to be added.
    */
    
   public void addAddressTypeIdCustomerAddressesModel(CustomerAddressesModel customerAddressesModel) {
      customerAddressesModel.setRelationAddressTypeIdAddressTypeModel(this);
      addressTypeIdCustomerAddressesModelList.add(customerAddressesModel);
   }
   
   /**
    * Remove the related CustomerAddressesModel to this one-to-many relation.
    *
    * @param customerAddressesModel object to be removed.
    */
   
   public void removeAddressTypeIdCustomerAddressesModel(CustomerAddressesModel customerAddressesModel) {      
      customerAddressesModel.setRelationAddressTypeIdAddressTypeModel(null);
      addressTypeIdCustomerAddressesModelList.remove(customerAddressesModel);      
   }

   /**
    * Get a list of related CustomerAddressesModel objects of the AddressTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AddressTypeId member.
    *
    * @return Collection of CustomerAddressesModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAddressTypeIdAddressTypeModel")
   @JoinColumn(name = "ADDRESS_TYPE_ID")
   public Collection<CustomerAddressesModel> getAddressTypeIdCustomerAddressesModelList() throws Exception {
   		return addressTypeIdCustomerAddressesModelList;
   }


   /**
    * Set a list of CustomerAddressesModel related objects to the AddressTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AddressTypeId member.
    *
    * @param customerAddressesModelList the list of related objects.
    */
    public void setAddressTypeIdCustomerAddressesModelList(Collection<CustomerAddressesModel> customerAddressesModelList) throws Exception {
		this.addressTypeIdCustomerAddressesModelList = customerAddressesModelList;
   }


   /**
    * Add the related RetailerContactAddressesModel to this one-to-many relation.
    *
    * @param retailerContactAddressesModel object to be added.
    */
    
   public void addAddressTypeIdRetailerContactAddressesModel(RetailerContactAddressesModel retailerContactAddressesModel) {
      retailerContactAddressesModel.setRelationAddressTypeIdAddressTypeModel(this);
      addressTypeIdRetailerContactAddressesModelList.add(retailerContactAddressesModel);
   }
   
   /**
    * Remove the related RetailerContactAddressesModel to this one-to-many relation.
    *
    * @param retailerContactAddressesModel object to be removed.
    */
   
   public void removeAddressTypeIdRetailerContactAddressesModel(RetailerContactAddressesModel retailerContactAddressesModel) {      
      retailerContactAddressesModel.setRelationAddressTypeIdAddressTypeModel(null);
      addressTypeIdRetailerContactAddressesModelList.remove(retailerContactAddressesModel);      
   }

   /**
    * Get a list of related RetailerContactAddressesModel objects of the AddressTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AddressTypeId member.
    *
    * @return Collection of RetailerContactAddressesModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAddressTypeIdAddressTypeModel")
   @JoinColumn(name = "ADDRESS_TYPE_ID")
   public Collection<RetailerContactAddressesModel> getAddressTypeIdRetailerContactAddressesModelList() throws Exception {
   		return addressTypeIdRetailerContactAddressesModelList;
   }


   /**
    * Set a list of RetailerContactAddressesModel related objects to the AddressTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AddressTypeId member.
    *
    * @param retailerContactAddressesModelList the list of related objects.
    */
    public void setAddressTypeIdRetailerContactAddressesModelList(Collection<RetailerContactAddressesModel> retailerContactAddressesModelList) throws Exception {
		this.addressTypeIdRetailerContactAddressesModelList = retailerContactAddressesModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAddressTypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&addressTypeId=" + getAddressTypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "addressTypeId";
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
