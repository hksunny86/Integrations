package com.inov8.integration.common.model;

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
 * The StatusModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="StatusModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "OLA_STATUS_seq",sequenceName = "OLA_STATUS_seq", allocationSize=1)
@Table(name = "OLA_STATUS")
public class OlaStatusModel extends BasePersistableModel implements Serializable {
  


   private Collection<AccountModel> statusIdAccountModelList = new ArrayList<AccountModel>();

   private Long statusId;
   private String status;
   private String description;

   /**
    * Default constructor.
    */
   public OlaStatusModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getStatusId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setStatusId(primaryKey);
    }

   /**
    * Returns the value of the <code>statusId</code> property.
    *
    */
      @Column(name = "STATUS_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OLA_STATUS_seq")
   public Long getStatusId() {
      return statusId;
   }

   /**
    * Sets the value of the <code>statusId</code> property.
    *
    * @param statusId the value for the <code>statusId</code> property
    *    
		    */

   public void setStatusId(Long statusId) {
      this.statusId = statusId;
   }

   /**
    * Returns the value of the <code>status</code> property.
    *
    */
      @Column(name = "STATUS" , nullable = false , length=50 )
   public String getStatus() {
      return status;
   }

   /**
    * Sets the value of the <code>status</code> property.
    *
    * @param status the value for the <code>status</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setStatus(String status) {
      this.status = status;
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
    * Add the related AccountModel to this one-to-many relation.
    *
    * @param accountModel object to be added.
    */
    
   public void addStatusIdAccountModel(AccountModel accountModel) {
      accountModel.setRelationStatusIdStatusModel(this);
      statusIdAccountModelList.add(accountModel);
   }
   
   /**
    * Remove the related AccountModel to this one-to-many relation.
    *
    * @param accountModel object to be removed.
    */
   
   public void removeStatusIdAccountModel(AccountModel accountModel) {      
      accountModel.setRelationStatusIdStatusModel(null);
      statusIdAccountModelList.remove(accountModel);      
   }

   /**
    * Get a list of related AccountModel objects of the StatusModel object.
    * These objects are in a bidirectional one-to-many relation by the StatusId member.
    *
    * @return Collection of AccountModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationStatusIdStatusModel")
   @JoinColumn(name = "STATUS_ID")
   public Collection<AccountModel> getStatusIdAccountModelList() throws Exception {
   		return statusIdAccountModelList;
   }


   /**
    * Set a list of AccountModel related objects to the StatusModel object.
    * These objects are in a bidirectional one-to-many relation by the StatusId member.
    *
    * @param accountModelList the list of related objects.
    */
    public void setStatusIdAccountModelList(Collection<AccountModel> accountModelList) throws Exception {
		this.statusIdAccountModelList = accountModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getStatusId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&statusId=" + getStatusId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "statusId";
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
