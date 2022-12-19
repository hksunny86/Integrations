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
 * The CustomInputTypeModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CustomInputTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CUSTOM_INPUT_TYPE_seq",sequenceName = "CUSTOM_INPUT_TYPE_seq", allocationSize=1)
@Table(name = "CUSTOM_INPUT_TYPE")
public class CustomInputTypeModel extends BasePersistableModel implements Serializable {
  


   private Collection<UssdMenuModel> customInputTypeIdUssdMenuModelList = new ArrayList<UssdMenuModel>();

   private Long customInputTypeId;
   private String keyword;

   /**
    * Default constructor.
    */
   public CustomInputTypeModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCustomInputTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCustomInputTypeId(primaryKey);
    }

   /**
    * Returns the value of the <code>customInputTypeId</code> property.
    *
    */
      @Column(name = "CUSTOM_INPUT_TYPE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUSTOM_INPUT_TYPE_seq")
   public Long getCustomInputTypeId() {
      return customInputTypeId;
   }

   /**
    * Sets the value of the <code>customInputTypeId</code> property.
    *
    * @param customInputTypeId the value for the <code>customInputTypeId</code> property
    *    
		    */

   public void setCustomInputTypeId(Long customInputTypeId) {
      this.customInputTypeId = customInputTypeId;
   }

   /**
    * Returns the value of the <code>keyword</code> property.
    *
    */
      @Column(name = "KEYWORD" , nullable = false , length=32 )
   public String getKeyword() {
      return keyword;
   }

   /**
    * Sets the value of the <code>keyword</code> property.
    *
    * @param keyword the value for the <code>keyword</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="32"
    */

   public void setKeyword(String keyword) {
      this.keyword = keyword;
   }


   /**
    * Add the related UssdMenuModel to this one-to-many relation.
    *
    * @param ussdMenuModel object to be added.
    */
    
   public void addCustomInputTypeIdUssdMenuModel(UssdMenuModel ussdMenuModel) {
      ussdMenuModel.setRelationCustomInputTypeIdCustomInputTypeModel(this);
      customInputTypeIdUssdMenuModelList.add(ussdMenuModel);
   }
   
   /**
    * Remove the related UssdMenuModel to this one-to-many relation.
    *
    * @param ussdMenuModel object to be removed.
    */
   
   public void removeCustomInputTypeIdUssdMenuModel(UssdMenuModel ussdMenuModel) {      
      ussdMenuModel.setRelationCustomInputTypeIdCustomInputTypeModel(null);
      customInputTypeIdUssdMenuModelList.remove(ussdMenuModel);      
   }

   /**
    * Get a list of related UssdMenuModel objects of the CustomInputTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the CustomInputTypeId member.
    *
    * @return Collection of UssdMenuModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCustomInputTypeIdCustomInputTypeModel")
   @JoinColumn(name = "CUSTOM_INPUT_TYPE_ID")
   public Collection<UssdMenuModel> getCustomInputTypeIdUssdMenuModelList() throws Exception {
   		return customInputTypeIdUssdMenuModelList;
   }


   /**
    * Set a list of UssdMenuModel related objects to the CustomInputTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the CustomInputTypeId member.
    *
    * @param ussdMenuModelList the list of related objects.
    */
    public void setCustomInputTypeIdUssdMenuModelList(Collection<UssdMenuModel> ussdMenuModelList) throws Exception {
		this.customInputTypeIdUssdMenuModelList = ussdMenuModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCustomInputTypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&customInputTypeId=" + getCustomInputTypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "customInputTypeId";
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
