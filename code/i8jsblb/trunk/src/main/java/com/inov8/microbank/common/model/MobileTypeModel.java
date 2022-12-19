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
 * The MobileTypeModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="MobileTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "MOBILE_TYPE_seq",sequenceName = "MOBILE_TYPE_seq", allocationSize=1)
@Table(name = "MOBILE_TYPE")
public class MobileTypeModel extends BasePersistableModel implements Serializable{
  


   /**
	 * 
	 */
	private static final long serialVersionUID = 2775005394247423482L;

private Collection<AppUserModel> mobileTypeIdAppUserModelList = new ArrayList<AppUserModel>();

   private Long mobileTypeId;
   private String name;
   private String description;

   /**
    * Default constructor.
    */
   public MobileTypeModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getMobileTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setMobileTypeId(primaryKey);
    }

   /**
    * Returns the value of the <code>mobileTypeId</code> property.
    *
    */
      @Column(name = "MOBILE_TYPE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MOBILE_TYPE_seq")
   public Long getMobileTypeId() {
      return mobileTypeId;
   }

   /**
    * Sets the value of the <code>mobileTypeId</code> property.
    *
    * @param mobileTypeId the value for the <code>mobileTypeId</code> property
    *    
		    */

   public void setMobileTypeId(Long mobileTypeId) {
      this.mobileTypeId = mobileTypeId;
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
    * Add the related AppUserModel to this one-to-many relation.
    *
    * @param appUserModel object to be added.
    */
    
   public void addMobileTypeIdAppUserModel(AppUserModel appUserModel) {
      appUserModel.setRelationMobileTypeIdMobileTypeModel(this);
      mobileTypeIdAppUserModelList.add(appUserModel);
   }
   
   /**
    * Remove the related AppUserModel to this one-to-many relation.
    *
    * @param appUserModel object to be removed.
    */
   
   public void removeMobileTypeIdAppUserModel(AppUserModel appUserModel) {      
      appUserModel.setRelationMobileTypeIdMobileTypeModel(null);
      mobileTypeIdAppUserModelList.remove(appUserModel);      
   }

   /**
    * Get a list of related AppUserModel objects of the MobileTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the MobileTypeId member.
    *
    * @return Collection of AppUserModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationMobileTypeIdMobileTypeModel")
   @JoinColumn(name = "MOBILE_TYPE_ID")
   public Collection<AppUserModel> getMobileTypeIdAppUserModelList() throws Exception {
   		return mobileTypeIdAppUserModelList;
   }


   /**
    * Set a list of AppUserModel related objects to the MobileTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the MobileTypeId member.
    *
    * @param appUserModelList the list of related objects.
    */
    public void setMobileTypeIdAppUserModelList(Collection<AppUserModel> appUserModelList) throws Exception {
		this.mobileTypeIdAppUserModelList = appUserModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getMobileTypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&mobileTypeId=" + getMobileTypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "mobileTypeId";
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
