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
 * The ResponseCodeModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ResponseCodeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "RESPONSE_CODE_seq",sequenceName = "RESPONSE_CODE_seq", allocationSize=1)
@Table(name = "RESPONSE_CODE")
public class ResponseCodeModel extends BasePersistableModel implements Serializable {
  


   private Collection<LedgerModel> responseCodeIdLedgerModelList = new ArrayList<LedgerModel>();

   private Long responseCodeId;
   private String code;
   private String description;

   /**
    * Default constructor.
    */
   public ResponseCodeModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getResponseCodeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setResponseCodeId(primaryKey);
    }

   /**
    * Returns the value of the <code>responseCodeId</code> property.
    *
    */
      @Column(name = "RESPONSE_CODE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RESPONSE_CODE_seq")
   public Long getResponseCodeId() {
      return responseCodeId;
   }

   /**
    * Sets the value of the <code>responseCodeId</code> property.
    *
    * @param responseCodeId the value for the <code>responseCodeId</code> property
    *    
		    */

   public void setResponseCodeId(Long responseCodeId) {
      this.responseCodeId = responseCodeId;
   }

   /**
    * Returns the value of the <code>code</code> property.
    *
    */
      @Column(name = "CODE" , nullable = false , length=10 )
   public String getCode() {
      return code;
   }

   /**
    * Sets the value of the <code>code</code> property.
    *
    * @param code the value for the <code>code</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="10"
    */

   public void setCode(String code) {
      this.code = code;
   }

   /**
    * Returns the value of the <code>description</code> property.
    *
    */
      @Column(name = "DESCRIPTION"  , length=50 )
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
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setDescription(String description) {
      this.description = description;
   }


   /**
    * Add the related LedgerModel to this one-to-many relation.
    *
    * @param ledgerModel object to be added.
    */
    
   public void addResponseCodeIdLedgerModel(LedgerModel ledgerModel) {
      ledgerModel.setRelationResponseCodeIdResponseCodeModel(this);
      responseCodeIdLedgerModelList.add(ledgerModel);
   }
   
   /**
    * Remove the related LedgerModel to this one-to-many relation.
    *
    * @param ledgerModel object to be removed.
    */
   
   public void removeResponseCodeIdLedgerModel(LedgerModel ledgerModel) {      
      ledgerModel.setRelationResponseCodeIdResponseCodeModel(null);
      responseCodeIdLedgerModelList.remove(ledgerModel);      
   }

   /**
    * Get a list of related LedgerModel objects of the ResponseCodeModel object.
    * These objects are in a bidirectional one-to-many relation by the ResponseCodeId member.
    *
    * @return Collection of LedgerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationResponseCodeIdResponseCodeModel")
   @JoinColumn(name = "RESPONSE_CODE_ID")
   public Collection<LedgerModel> getResponseCodeIdLedgerModelList() throws Exception {
   		return responseCodeIdLedgerModelList;
   }


   /**
    * Set a list of LedgerModel related objects to the ResponseCodeModel object.
    * These objects are in a bidirectional one-to-many relation by the ResponseCodeId member.
    *
    * @param ledgerModelList the list of related objects.
    */
    public void setResponseCodeIdLedgerModelList(Collection<LedgerModel> ledgerModelList) throws Exception {
		this.responseCodeIdLedgerModelList = ledgerModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getResponseCodeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&responseCodeId=" + getResponseCodeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "responseCodeId";
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
