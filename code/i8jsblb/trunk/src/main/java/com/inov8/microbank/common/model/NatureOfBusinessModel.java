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
 * The NatureOfBusinessModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="NatureOfBusinessModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "NATURE_OF_BUSINESS_seq",sequenceName = "NATURE_OF_BUSINESS_seq", allocationSize=1)
@Table(name = "NATURE_OF_BUSINESS")
public class NatureOfBusinessModel extends BasePersistableModel implements Serializable {
  


   private Collection<RetailerContactModel> natureOfBusinessIdRetailerContactModelList = new ArrayList<RetailerContactModel>();

   private Long natureOfBusinessId;
   private String name;
   private String merchantGroup;

   /**
    * Default constructor.
    */
   public NatureOfBusinessModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Double with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getNatureOfBusinessId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setNatureOfBusinessId(primaryKey);
    }

   /**
    * Returns the value of the <code>natureOfBusinessId</code> property.
    *
    */
      @Column(name = "NATURE_OF_BUSINESS_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NATURE_OF_BUSINESS_seq")
   public Long getNatureOfBusinessId() {
      return natureOfBusinessId;
   }

   /**
    * Sets the value of the <code>natureOfBusinessId</code> property.
    *
    * @param natureOfBusinessId the value for the <code>natureOfBusinessId</code> property
    *    
		    */

   public void setNatureOfBusinessId(Long natureOfBusinessId) {
      this.natureOfBusinessId = natureOfBusinessId;
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
    * Add the related RetailerContactModel to this one-to-many relation.
    *
    * @param retailerContactModel object to be added.
    */
    
   public void addNatureOfBusinessIdRetailerContactModel(RetailerContactModel retailerContactModel) {
      retailerContactModel.setRelationNatureOfBusinessIdNatureOfBusinessModel(this);
      natureOfBusinessIdRetailerContactModelList.add(retailerContactModel);
   }
   
   /**
    * Remove the related RetailerContactModel to this one-to-many relation.
    *
    * @param retailerContactModel object to be removed.
    */
   
   public void removeNatureOfBusinessIdRetailerContactModel(RetailerContactModel retailerContactModel) {      
      retailerContactModel.setRelationNatureOfBusinessIdNatureOfBusinessModel(null);
      natureOfBusinessIdRetailerContactModelList.remove(retailerContactModel);      
   }

   /**
    * Get a list of related RetailerContactModel objects of the NatureOfBusinessModel object.
    * These objects are in a bidirectional one-to-many relation by the NatureOfBusinessId member.
    *
    * @return Collection of RetailerContactModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationNatureOfBusinessIdNatureOfBusinessModel")
   @JoinColumn(name = "NATURE_OF_BUSINESS_ID")
   public Collection<RetailerContactModel> getNatureOfBusinessIdRetailerContactModelList() throws Exception {
   		return natureOfBusinessIdRetailerContactModelList;
   }


   /**
    * Set a list of RetailerContactModel related objects to the NatureOfBusinessModel object.
    * These objects are in a bidirectional one-to-many relation by the NatureOfBusinessId member.
    *
    * @param retailerContactModelList the list of related objects.
    */
    public void setNatureOfBusinessIdRetailerContactModelList(Collection<RetailerContactModel> retailerContactModelList) throws Exception {
		this.natureOfBusinessIdRetailerContactModelList = retailerContactModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getNatureOfBusinessId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&natureOfBusinessId=" + getNatureOfBusinessId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "natureOfBusinessId";
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

    
    @Column(name = "MERCHANT_GROUP")
	public String getMerchantGroup() {
		return merchantGroup;
	}

	public void setMerchantGroup(String merchantGroup) {
		this.merchantGroup = merchantGroup;
	}    
          
}
