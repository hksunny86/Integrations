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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The PostalOfficeModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="PostalOfficeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "POSTAL_OFFICE_seq",sequenceName = "POSTAL_OFFICE_seq", allocationSize=1)
@Table(name = "POSTAL_OFFICE")
public class PostalOfficeModel extends BasePersistableModel implements Serializable {
  


   private Collection<AddressModel> postalOfficeIdAddressModelList = new ArrayList<AddressModel>();

   private Long postalOfficeId;
   private String name;
   private CityModel cityModel;

   /**
    * Default constructor.
    */
   public PostalOfficeModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPostalOfficeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setPostalOfficeId(primaryKey);
    }

   /**
    * Returns the value of the <code>postalOfficeId</code> property.
    *
    */
      @Column(name = "POSTAL_OFFICE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="POSTAL_OFFICE_seq")
   public Long getPostalOfficeId() {
      return postalOfficeId;
   }

   /**
    * Sets the value of the <code>postalOfficeId</code> property.
    *
    * @param postalOfficeId the value for the <code>postalOfficeId</code> property
    *    
		    */

   public void setPostalOfficeId(Long postalOfficeId) {
      this.postalOfficeId = postalOfficeId;
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
    * Add the related AddressModel to this one-to-many relation.
    *
    * @param addressModel object to be added.
    */
    
   public void addPostalOfficeIdAddressModel(AddressModel addressModel) {
      addressModel.setRelationPostalOfficeIdPostalOfficeModel(this);
      postalOfficeIdAddressModelList.add(addressModel);
   }
   
   /**
    * Remove the related AddressModel to this one-to-many relation.
    *
    * @param addressModel object to be removed.
    */
   
   public void removePostalOfficeIdAddressModel(AddressModel addressModel) {      
      addressModel.setRelationPostalOfficeIdPostalOfficeModel(null);
      postalOfficeIdAddressModelList.remove(addressModel);      
   }

   /**
    * Get a list of related AddressModel objects of the PostalOfficeModel object.
    * These objects are in a bidirectional one-to-many relation by the PostalOfficeId member.
    *
    * @return Collection of AddressModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationPostalOfficeIdPostalOfficeModel")
   @JoinColumn(name = "POSTAL_OFFICE_ID")
   public Collection<AddressModel> getPostalOfficeIdAddressModelList() throws Exception {
   		return postalOfficeIdAddressModelList;
   }


   /**
    * Set a list of AddressModel related objects to the PostalOfficeModel object.
    * These objects are in a bidirectional one-to-many relation by the PostalOfficeId member.
    *
    * @param addressModelList the list of related objects.
    */
    public void setPostalOfficeIdAddressModelList(Collection<AddressModel> addressModelList) throws Exception {
		this.postalOfficeIdAddressModelList = addressModelList;
   }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "CITY_ID")
    public CityModel getCityModel() {
    	return cityModel;
    }

    public void setCityModel(CityModel cityModel) {
    	this.cityModel = cityModel;
    }

	/**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getPostalOfficeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&postalOfficeId=" + getPostalOfficeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "postalOfficeId";
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

        associationModel.setClassName("CityModel");
        associationModel.setPropertyName("cityModel");
        associationModel.setValue(getCityModel());

        associationModelList.add(associationModel);
    	
    	return associationModelList;
    }    
          
}
