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
 * The LanguageModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="LanguageModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "LANGUAGE_seq",sequenceName = "LANGUAGE_seq", allocationSize=1)
@Table(name = "LANGUAGE")
public class LanguageModel extends BasePersistableModel implements Serializable {
  


   private Collection<CustomerModel> languageIdCustomerModelList = new ArrayList<CustomerModel>();

   private Long languageId;
   private String name;
   private String initials;

   /**
    * Default constructor.
    */
   public LanguageModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getLanguageId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setLanguageId(primaryKey);
    }

   /**
    * Returns the value of the <code>languageId</code> property.
    *
    */
      @Column(name = "LANGUAGE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LANGUAGE_seq")
   public Long getLanguageId() {
      return languageId;
   }

   /**
    * Sets the value of the <code>languageId</code> property.
    *
    * @param languageId the value for the <code>languageId</code> property
    *    
		    */

   public void setLanguageId(Long languageId) {
      this.languageId = languageId;
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
    * Add the related CustomerModel to this one-to-many relation.
    *
    * @param customerModel object to be added.
    */
    
   public void addLanguageIdCustomerModel(CustomerModel customerModel) {
      customerModel.setRelationLanguageIdLanguageModel(this);
      languageIdCustomerModelList.add(customerModel);
   }
   
   /**
    * Remove the related CustomerModel to this one-to-many relation.
    *
    * @param customerModel object to be removed.
    */
   
   public void removeLanguageIdCustomerModel(CustomerModel customerModel) {      
      customerModel.setRelationLanguageIdLanguageModel(null);
      languageIdCustomerModelList.remove(customerModel);      
   }

   /**
    * Get a list of related CustomerModel objects of the LanguageModel object.
    * These objects are in a bidirectional one-to-many relation by the LanguageId member.
    *
    * @return Collection of CustomerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationLanguageIdLanguageModel")
   @JoinColumn(name = "LANGUAGE_ID")
   public Collection<CustomerModel> getLanguageIdCustomerModelList() throws Exception {
   		return languageIdCustomerModelList;
   }


   /**
    * Set a list of CustomerModel related objects to the LanguageModel object.
    * These objects are in a bidirectional one-to-many relation by the LanguageId member.
    *
    * @param customerModelList the list of related objects.
    */
    public void setLanguageIdCustomerModelList(Collection<CustomerModel> customerModelList) throws Exception {
		this.languageIdCustomerModelList = customerModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getLanguageId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&languageId=" + getLanguageId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "languageId";
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
