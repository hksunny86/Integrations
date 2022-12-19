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
 * The CountryModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CountryModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COUNTRY_seq",sequenceName = "COUNTRY_seq", allocationSize=1)
@Table(name = "COUNTRY")
public class CountryModel extends BasePersistableModel implements Serializable {
  


   private Collection<AddressModel> countryIdAddressModelList = new ArrayList<AddressModel>();

   private Long countryId;
   private String initials;
   private String name;
   private String countryCode;

   /**
    * Default constructor.
    */
   public CountryModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCountryId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCountryId(primaryKey);
    }

   /**
    * Returns the value of the <code>countryId</code> property.
    *
    */
      @Column(name = "COUNTRY_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COUNTRY_seq")
   public Long getCountryId() {
      return countryId;
   }

   /**
    * Sets the value of the <code>countryId</code> property.
    *
    * @param countryId the value for the <code>countryId</code> property
    *    
		    */

   public void setCountryId(Long countryId) {
      this.countryId = countryId;
   }

   /**
    * Returns the value of the <code>initials</code> property.
    *
    */
      @Column(name = "INITIALS" , nullable = false , length=5 )
   public String getInitials() {
      return initials;
   }

   /**
    * Sets the value of the <code>initials</code> property.
    *
    * @param initials the value for the <code>initials</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="5"
    */

   public void setInitials(String initials) {
      this.initials = initials;
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
    
   public void addCountryIdAddressModel(AddressModel addressModel) {
      addressModel.setRelationCountryIdCountryModel(this);
      countryIdAddressModelList.add(addressModel);
   }
   
   /**
    * Remove the related AddressModel to this one-to-many relation.
    *
    * @param addressModel object to be removed.
    */
   
   public void removeCountryIdAddressModel(AddressModel addressModel) {      
      addressModel.setRelationCountryIdCountryModel(null);
      countryIdAddressModelList.remove(addressModel);      
   }

   /**
    * Get a list of related AddressModel objects of the CountryModel object.
    * These objects are in a bidirectional one-to-many relation by the CountryId member.
    *
    * @return Collection of AddressModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCountryIdCountryModel")
   @JoinColumn(name = "COUNTRY_ID")
   public Collection<AddressModel> getCountryIdAddressModelList() throws Exception {
   		return countryIdAddressModelList;
   }


   /**
    * Set a list of AddressModel related objects to the CountryModel object.
    * These objects are in a bidirectional one-to-many relation by the CountryId member.
    *
    * @param addressModelList the list of related objects.
    */
    public void setCountryIdAddressModelList(Collection<AddressModel> addressModelList) throws Exception {
		this.countryIdAddressModelList = addressModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCountryId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&countryId=" + getCountryId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "countryId";
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

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    @Column(name = "COUNTRY_CODE")
    public String getCountryCode() {
        return countryCode;
    }
}
