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
 * The CommissionShAcctsTypeModel entity bean.
 *
 * @author  Fahad Tariq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CommissionShAcctsTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COMMISSION_SH_ACCTS_TYPE_seq",sequenceName = "COMMISSION_SH_ACCTS_TYPE_seq", allocationSize=1)
@Table(name = "COMMISSION_SH_ACCTS_TYPE")
public class CommissionShAcctsTypeModel extends BasePersistableModel implements Serializable {
  


   private Collection<CommissionShAcctsModel> cmshaccttypeIdCommissionShAcctsModelList = new ArrayList<CommissionShAcctsModel>();

   private Long cmshacctstypeId;
   private String name;
   private Boolean active;

   /**
    * Default constructor.
    */
   public CommissionShAcctsTypeModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCmshacctstypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCmshacctstypeId(primaryKey);
    }

   /**
    * Returns the value of the <code>cmshacctstypeId</code> property.
    *
    */
      @Column(name = "CMSHACCTSTYPE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMMISSION_SH_ACCTS_TYPE_seq")
   public Long getCmshacctstypeId() {
      return cmshacctstypeId;
   }

   /**
    * Sets the value of the <code>cmshacctstypeId</code> property.
    *
    * @param cmshacctstypeId the value for the <code>cmshacctstypeId</code> property
    *    
		    */

   public void setCmshacctstypeId(Long cmshacctstypeId) {
      this.cmshacctstypeId = cmshacctstypeId;
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
    * Add the related CommissionShAcctsModel to this one-to-many relation.
    *
    * @param commissionShAcctsModel object to be added.
    */
    
   public void addCmshaccttypeIdCommissionShAcctsModel(CommissionShAcctsModel commissionShAcctsModel) {
      commissionShAcctsModel.setRelationCmshaccttypeIdCommissionShAcctsTypeModel(this);
      cmshaccttypeIdCommissionShAcctsModelList.add(commissionShAcctsModel);
   }
   
   /**
    * Remove the related CommissionShAcctsModel to this one-to-many relation.
    *
    * @param commissionShAcctsModel object to be removed.
    */
   
   public void removeCmshaccttypeIdCommissionShAcctsModel(CommissionShAcctsModel commissionShAcctsModel) {      
      commissionShAcctsModel.setRelationCmshaccttypeIdCommissionShAcctsTypeModel(null);
      cmshaccttypeIdCommissionShAcctsModelList.remove(commissionShAcctsModel);      
   }

   /**
    * Get a list of related CommissionShAcctsModel objects of the CommissionShAcctsTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the CmshaccttypeId member.
    *
    * @return Collection of CommissionShAcctsModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCmshaccttypeIdCommissionShAcctsTypeModel")
   @JoinColumn(name = "CMSHACCTTYPE_ID")
   public Collection<CommissionShAcctsModel> getCmshaccttypeIdCommissionShAcctsModelList() throws Exception {
   		return cmshaccttypeIdCommissionShAcctsModelList;
   }


   /**
    * Set a list of CommissionShAcctsModel related objects to the CommissionShAcctsTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the CmshaccttypeId member.
    *
    * @param commissionShAcctsModelList the list of related objects.
    */
    public void setCmshaccttypeIdCommissionShAcctsModelList(Collection<CommissionShAcctsModel> commissionShAcctsModelList) throws Exception {
		this.cmshaccttypeIdCommissionShAcctsModelList = commissionShAcctsModelList;
   }


   @Column(name = "IS_ACTIVE" , nullable = false )
   public Boolean getActive() {
       return active;
   }

   /**
    * Sets the value of the <code>active</code> property.
    *
    * @param active the value for the <code>active</code> property
    *
    */

   public void setActive(Boolean active) {
       this.active = active;
   }



    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCmshacctstypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&cmshacctstypeId=" + getCmshacctstypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "cmshacctstypeId";
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
