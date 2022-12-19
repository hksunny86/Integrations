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
 * The ReasonModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2008/11/04 19:29:08 $
 *
 *
 * @spring.bean name="ReasonModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "REASON_seq",sequenceName = "REASON_seq", allocationSize=1)
@Table(name = "REASON")
public class ReasonModel extends BasePersistableModel implements Serializable {
  


   private Collection<LedgerModel> reasonIdLedgerModelList = new ArrayList<LedgerModel>();

   private Long reasonId;
   private String name;
   private String description;

   /**
    * Default constructor.
    */
   public ReasonModel() {
   }   
   
   public ReasonModel(Long pk) {
       this.reasonId = pk;
   }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getReasonId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setReasonId(primaryKey);
    }

   /**
    * Returns the value of the <code>reasonId</code> property.
    *
    */
      @Column(name = "REASON_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="REASON_seq")
   public Long getReasonId() {
      return reasonId;
   }

   /**
    * Sets the value of the <code>reasonId</code> property.
    *
    * @param reasonId the value for the <code>reasonId</code> property
    *    
		    */

   public void setReasonId(Long reasonId) {
      this.reasonId = reasonId;
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
    * Add the related LedgerModel to this one-to-many relation.
    *
    * @param ledgerModel object to be added.
    */
    
   public void addReasonIdLedgerModel(LedgerModel ledgerModel) {
      ledgerModel.setRelationReasonIdReasonModel(this);
      reasonIdLedgerModelList.add(ledgerModel);
   }
   
   /**
    * Remove the related LedgerModel to this one-to-many relation.
    *
    * @param ledgerModel object to be removed.
    */
   
   public void removeReasonIdLedgerModel(LedgerModel ledgerModel) {      
      ledgerModel.setRelationReasonIdReasonModel(null);
      reasonIdLedgerModelList.remove(ledgerModel);      
   }

   /**
    * Get a list of related LedgerModel objects of the ReasonModel object.
    * These objects are in a bidirectional one-to-many relation by the ReasonId member.
    *
    * @return Collection of LedgerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationReasonIdReasonModel")
   @JoinColumn(name = "REASON_ID")
   public Collection<LedgerModel> getReasonIdLedgerModelList() throws Exception {
   		return reasonIdLedgerModelList;
   }


   /**
    * Set a list of LedgerModel related objects to the ReasonModel object.
    * These objects are in a bidirectional one-to-many relation by the ReasonId member.
    *
    * @param ledgerModelList the list of related objects.
    */
    public void setReasonIdLedgerModelList(Collection<LedgerModel> ledgerModelList) throws Exception {
		this.reasonIdLedgerModelList = ledgerModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getReasonId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&reasonId=" + getReasonId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "reasonId";
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
