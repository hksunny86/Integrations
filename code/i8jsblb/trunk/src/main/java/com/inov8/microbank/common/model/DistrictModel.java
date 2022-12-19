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
 * The DistrictModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="DistrictModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DISTRICT_seq",sequenceName = "DISTRICT_seq", allocationSize=1)
@Table(name = "DISTRICT")
public class DistrictModel extends BasePersistableModel implements Serializable {
  


   private Collection<AddressModel> districtIdAddressModelList = new ArrayList<AddressModel>();

   private Long districtId;
   private String name;

   /**
    * Default constructor.
    */
   public DistrictModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getDistrictId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setDistrictId(primaryKey);
    }

   /**
    * Returns the value of the <code>districtId</code> property.
    *
    */
      @Column(name = "DISTRICT_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DISTRICT_seq")
   public Long getDistrictId() {
      return districtId;
   }

   /**
    * Sets the value of the <code>districtId</code> property.
    *
    * @param districtId the value for the <code>districtId</code> property
    *    
		    */

   public void setDistrictId(Long districtId) {
      this.districtId = districtId;
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
    
   public void addDistrictIdAddressModel(AddressModel addressModel) {
      addressModel.setRelationDistrictIdDistrictModel(this);
      districtIdAddressModelList.add(addressModel);
   }
   
   /**
    * Remove the related AddressModel to this one-to-many relation.
    *
    * @param addressModel object to be removed.
    */
   
   public void removeDistrictIdAddressModel(AddressModel addressModel) {      
      addressModel.setRelationDistrictIdDistrictModel(null);
      districtIdAddressModelList.remove(addressModel);      
   }

   /**
    * Get a list of related AddressModel objects of the DistrictModel object.
    * These objects are in a bidirectional one-to-many relation by the DistrictId member.
    *
    * @return Collection of AddressModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationDistrictIdDistrictModel")
   @JoinColumn(name = "DISTRICT_ID")
   public Collection<AddressModel> getDistrictIdAddressModelList() throws Exception {
   		return districtIdAddressModelList;
   }


   /**
    * Set a list of AddressModel related objects to the DistrictModel object.
    * These objects are in a bidirectional one-to-many relation by the DistrictId member.
    *
    * @param addressModelList the list of related objects.
    */
    public void setDistrictIdAddressModelList(Collection<AddressModel> addressModelList) throws Exception {
		this.districtIdAddressModelList = addressModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getDistrictId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&districtId=" + getDistrictId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "districtId";
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
