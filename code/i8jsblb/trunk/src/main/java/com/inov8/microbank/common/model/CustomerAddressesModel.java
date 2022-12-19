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
 * The CustomerAddressesModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CustomerAddressesModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CUSTOMER_ADDRESSES_seq",sequenceName = "CUSTOMER_ADDRESSES_seq", allocationSize=1)
@Table(name = "CUSTOMER_ADDRESSES")
public class CustomerAddressesModel extends BasePersistableModel implements Serializable {
  

   private CustomerModel customerIdCustomerModel;
   private AddressTypeModel addressTypeIdAddressTypeModel;
   private AddressModel addressIdAddressModel;
   private Long applicantTypeId;
   
   private Long	applicantDetailId;	//added by Turab


   private Long customerAddressesId;

   /**
    * Default constructor.
    */
   public CustomerAddressesModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCustomerAddressesId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCustomerAddressesId(primaryKey);
    }

   /**
    * Returns the value of the <code>customerAddressesId</code> property.
    *
    */
      @Column(name = "CUSTOMER_ADDRESSES_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUSTOMER_ADDRESSES_seq")
   public Long getCustomerAddressesId() {
      return customerAddressesId;
   }

   /**
    * Sets the value of the <code>customerAddressesId</code> property.
    *
    * @param customerAddressesId the value for the <code>customerAddressesId</code> property
    *    
		    */

   public void setCustomerAddressesId(Long customerAddressesId) {
      this.customerAddressesId = customerAddressesId;
   }

   /**
    * Returns the value of the <code>customerIdCustomerModel</code> relation property.
    *
    * @return the value of the <code>customerIdCustomerModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CUSTOMER_ID")    
   public CustomerModel getRelationCustomerIdCustomerModel(){
      return customerIdCustomerModel;
   }
    
   /**
    * Returns the value of the <code>customerIdCustomerModel</code> relation property.
    *
    * @return the value of the <code>customerIdCustomerModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CustomerModel getCustomerIdCustomerModel(){
      return getRelationCustomerIdCustomerModel();
   }

   /**
    * Sets the value of the <code>customerIdCustomerModel</code> relation property.
    *
    * @param customerModel a value for <code>customerIdCustomerModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCustomerIdCustomerModel(CustomerModel customerModel) {
      this.customerIdCustomerModel = customerModel;
   }
   
   /**
    * Sets the value of the <code>customerIdCustomerModel</code> relation property.
    *
    * @param customerModel a value for <code>customerIdCustomerModel</code>.
    */
   @javax.persistence.Transient
   public void setCustomerIdCustomerModel(CustomerModel customerModel) {
      if(null != customerModel)
      {
      	setRelationCustomerIdCustomerModel((CustomerModel)customerModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>addressTypeIdAddressTypeModel</code> relation property.
    *
    * @return the value of the <code>addressTypeIdAddressTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ADDRESS_TYPE_ID")    
   public AddressTypeModel getRelationAddressTypeIdAddressTypeModel(){
      return addressTypeIdAddressTypeModel;
   }
    
   /**
    * Returns the value of the <code>addressTypeIdAddressTypeModel</code> relation property.
    *
    * @return the value of the <code>addressTypeIdAddressTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AddressTypeModel getAddressTypeIdAddressTypeModel(){
      return getRelationAddressTypeIdAddressTypeModel();
   }

   /**
    * Sets the value of the <code>addressTypeIdAddressTypeModel</code> relation property.
    *
    * @param addressTypeModel a value for <code>addressTypeIdAddressTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAddressTypeIdAddressTypeModel(AddressTypeModel addressTypeModel) {
      this.addressTypeIdAddressTypeModel = addressTypeModel;
   }
   
   /**
    * Sets the value of the <code>addressTypeIdAddressTypeModel</code> relation property.
    *
    * @param addressTypeModel a value for <code>addressTypeIdAddressTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setAddressTypeIdAddressTypeModel(AddressTypeModel addressTypeModel) {
      if(null != addressTypeModel)
      {
      	setRelationAddressTypeIdAddressTypeModel((AddressTypeModel)addressTypeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>addressIdAddressModel</code> relation property.
    *
    * @return the value of the <code>addressIdAddressModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ADDRESS_ID")    
   public AddressModel getRelationAddressIdAddressModel(){
      return addressIdAddressModel;
   }
    
   /**
    * Returns the value of the <code>addressIdAddressModel</code> relation property.
    *
    * @return the value of the <code>addressIdAddressModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AddressModel getAddressIdAddressModel(){
      return getRelationAddressIdAddressModel();
   }

   /**
    * Sets the value of the <code>addressIdAddressModel</code> relation property.
    *
    * @param addressModel a value for <code>addressIdAddressModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAddressIdAddressModel(AddressModel addressModel) {
      this.addressIdAddressModel = addressModel;
   }
   
   /**
    * Sets the value of the <code>addressIdAddressModel</code> relation property.
    *
    * @param addressModel a value for <code>addressIdAddressModel</code>.
    */
   @javax.persistence.Transient
   public void setAddressIdAddressModel(AddressModel addressModel) {
      if(null != addressModel)
      {
      	setRelationAddressIdAddressModel((AddressModel)addressModel.clone());
      }      
   }
   



   /**
    * Returns the value of the <code>customerId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCustomerId() {
      if (customerIdCustomerModel != null) {
         return customerIdCustomerModel.getCustomerId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>customerId</code> property.
    *
    * @param customerId the value for the <code>customerId</code> property
					    * @spring.validator type="required"
							    */
   
   @javax.persistence.Transient
   public void setCustomerId(Long customerId) {
      if(customerId == null)
      {      
      	customerIdCustomerModel = null;
      }
      else
      {
        customerIdCustomerModel = new CustomerModel();
      	customerIdCustomerModel.setCustomerId(customerId);
      }      
   }

   /**
    * Returns the value of the <code>addressTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAddressTypeId() {
      if (addressTypeIdAddressTypeModel != null) {
         return addressTypeIdAddressTypeModel.getAddressTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>addressTypeId</code> property.
    *
    * @param addressTypeId the value for the <code>addressTypeId</code> property
									    * @spring.validator type="required"
			    */
   
   @javax.persistence.Transient
   public void setAddressTypeId(Long addressTypeId) {
      if(addressTypeId == null)
      {      
      	addressTypeIdAddressTypeModel = null;
      }
      else
      {
        addressTypeIdAddressTypeModel = new AddressTypeModel();
      	addressTypeIdAddressTypeModel.setAddressTypeId(addressTypeId);
      }      
   }

   /**
    * Returns the value of the <code>addressId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAddressId() {
      if (addressIdAddressModel != null) {
         return addressIdAddressModel.getAddressId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>addressId</code> property.
    *
    * @param addressId the value for the <code>addressId</code> property
							    * @spring.validator type="required"
					    */
   
   @javax.persistence.Transient
   public void setAddressId(Long addressId) {
      if(addressId == null)
      {      
      	addressIdAddressModel = null;
      }
      else
      {
        addressIdAddressModel = new AddressModel();
      	addressIdAddressModel.setAddressId(addressId);
      }      
   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCustomerAddressesId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&customerAddressesId=" + getCustomerAddressesId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "customerAddressesId";
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
    	
    	associationModel.setClassName("CustomerModel");
    	associationModel.setPropertyName("relationCustomerIdCustomerModel");   		
   		associationModel.setValue(getRelationCustomerIdCustomerModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AddressTypeModel");
    	associationModel.setPropertyName("relationAddressTypeIdAddressTypeModel");   		
   		associationModel.setValue(getRelationAddressTypeIdAddressTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AddressModel");
    	associationModel.setPropertyName("relationAddressIdAddressModel");   		
   		associationModel.setValue(getRelationAddressIdAddressModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }

    @Column(name = "APPLICANT_TYPE_ID")  
	public Long getApplicantTypeId() {
		return applicantTypeId;
	}

	public void setApplicantTypeId(Long applicantTypeId) {
		this.applicantTypeId = applicantTypeId;
	}    
     
	@Column(name = "APPLICANT_DETAIL_ID")  
	public Long getApplicantDetailId() {
		return applicantDetailId;
	}

	public void setApplicantDetailId(Long applicantDetailId) {
		this.applicantDetailId = applicantDetailId;
	}
}
