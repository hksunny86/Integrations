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
 * The AllpayCommissionReasonModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2008/12/01 19:29:08 $
 *
 *
 * @spring.bean name="AllpayCommissionReasonModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ALLPAY_COMMISSION_REASON_seq",sequenceName = "ALLPAY_COMMISSION_REASON_seq", allocationSize=1)
@Table(name = "ALLPAY_COMMISSION_REASON")
public class AllpayCommissionReasonModel extends BasePersistableModel implements Serializable {
  


   private Collection<AllpayCommissionRateModel> allpayCommissionReasonIdAllpayCommissionRateModelList = new ArrayList<AllpayCommissionRateModel>();
   private Collection<AllpayCommissionTransactionModel> allpayCommissionReasonIdAllpayCommissionTransactionModelList = new ArrayList<AllpayCommissionTransactionModel>();

   private Long allpayCommissionReasonId;
   private String name;
   private String comments;
   private String description;

   /**
    * Default constructor.
    */
   public AllpayCommissionReasonModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAllpayCommissionReasonId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAllpayCommissionReasonId(primaryKey);
    }

   /**
    * Returns the value of the <code>commissionReasonId</code> property.
    *
    */
      @Column(name = "ALLPAY_COMMISSION_REASON_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ALLPAY_COMMISSION_REASON_seq")
   public Long getAllpayCommissionReasonId() {
      return allpayCommissionReasonId;
   }

   /**
    * Sets the value of the <code>commissionReasonId</code> property.
    *
    * @param commissionReasonId the value for the <code>commissionReasonId</code> property
    *    
		    */

   public void setAllpayCommissionReasonId(Long commissionReasonId) {
      this.allpayCommissionReasonId = commissionReasonId;
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
    * Returns the value of the <code>comments</code> property.
    *
    */
      @Column(name = "COMMENTS"  , length=250 )
   public String getComments() {
      return comments;
   }

   /**
    * Sets the value of the <code>comments</code> property.
    *
    * @param comments the value for the <code>comments</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments(String comments) {
      this.comments = comments;
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
    * Add the related AllpayCommissionRateModel to this one-to-many relation.
    *
    * @param allpayCommissionRateModel object to be added.
    */
    
   public void addAllpayCommissionReasonIdAllpayCommissionRateModel(AllpayCommissionRateModel allpayCommissionRateModel) {
      allpayCommissionRateModel.setRelationAllpayCommissionReasonIdAllpayCommissionReasonModel(this);
      allpayCommissionReasonIdAllpayCommissionRateModelList.add(allpayCommissionRateModel);
   }
   
   /**
    * Remove the related AllpayCommissionRateModel to this one-to-many relation.
    *
    * @param allpayCommissionRateModel object to be removed.
    */
   
   public void removeAllpayCommissionReasonIdAllpayCommissionRateModel(AllpayCommissionRateModel allpayCommissionRateModel) {      
      allpayCommissionRateModel.setRelationAllpayCommissionReasonIdAllpayCommissionReasonModel(null);
      allpayCommissionReasonIdAllpayCommissionRateModelList.remove(allpayCommissionRateModel);      
   }

   /**
    * Get a list of related AllpayCommissionRateModel objects of the AllpayCommissionReasonModel object.
    * These objects are in a bidirectional one-to-many relation by the AllpayCommissionReasonId member.
    *
    * @return Collection of AllpayCommissionRateModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAllpayCommissionReasonIdAllpayCommissionReasonModel")
   @JoinColumn(name = "ALLPAY_COMMISSION_REASON_ID")
   public Collection<AllpayCommissionRateModel> getAllpayCommissionReasonIdAllpayCommissionRateModelList() throws Exception {
   		return allpayCommissionReasonIdAllpayCommissionRateModelList;
   }


   /**
    * Set a list of AllpayCommissionRateModel related objects to the AllpayCommissionReasonModel object.
    * These objects are in a bidirectional one-to-many relation by the AllpayCommissionReasonId member.
    *
    * @param allpayCommissionRateModelList the list of related objects.
    */
    public void setAllpayCommissionReasonIdAllpayCommissionRateModelList(Collection<AllpayCommissionRateModel> allpayCommissionRateModelList) throws Exception {
		this.allpayCommissionReasonIdAllpayCommissionRateModelList = allpayCommissionRateModelList;
   }


   /**
    * Add the related AllpayCommissionTransactionModel to this one-to-many relation.
    *
    * @param allpayCommissionTransactionModel object to be added.
    */
    
   public void addAllpayCommissionReasonIdAllpayCommissionTransactionModel(AllpayCommissionTransactionModel allpayCommissionTransactionModel) {
      allpayCommissionTransactionModel.setRelationAllpayCommissionReasonIdAllpayCommissionReasonModel(this);
      allpayCommissionReasonIdAllpayCommissionTransactionModelList.add(allpayCommissionTransactionModel);
   }
   
   /**
    * Remove the related AllpayCommissionTransactionModel to this one-to-many relation.
    *
    * @param allpayCommissionTransactionModel object to be removed.
    */
   
   public void removeAllpayCommissionReasonIdAllpayCommissionTransactionModel(AllpayCommissionTransactionModel allpayCommissionTransactionModel) {      
      allpayCommissionTransactionModel.setRelationAllpayCommissionReasonIdAllpayCommissionReasonModel(null);
      allpayCommissionReasonIdAllpayCommissionTransactionModelList.remove(allpayCommissionTransactionModel);      
   }

   /**
    * Get a list of related AllpayCommissionTransactionModel objects of the AllpayCommissionReasonModel object.
    * These objects are in a bidirectional one-to-many relation by the AllpayCommissionReasonId member.
    *
    * @return Collection of AllpayCommissionTransactionModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAllpayCommissionReasonIdAllpayCommissionReasonModel")
   @JoinColumn(name = "ALLPAY_COMMISSION_REASON_ID")
   public Collection<AllpayCommissionTransactionModel> getAllpayCommissionReasonIdAllpayCommissionTransactionModelList() throws Exception {
   		return allpayCommissionReasonIdAllpayCommissionTransactionModelList;
   }


   /**
    * Set a list of AllpayCommissionTransactionModel related objects to the AllpayCommissionReasonModel object.
    * These objects are in a bidirectional one-to-many relation by the AllpayCommissionReasonId member.
    *
    * @param allpayCommissionTransactionModelList the list of related objects.
    */
    public void setAllpayCommissionReasonIdAllpayCommissionTransactionModelList(Collection<AllpayCommissionTransactionModel> allpayCommissionTransactionModelList) throws Exception {
		this.allpayCommissionReasonIdAllpayCommissionTransactionModelList = allpayCommissionTransactionModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAllpayCommissionReasonId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&AllpayCommissionReasonId=" + getAllpayCommissionReasonId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "allpayCommissionReasonId";
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
