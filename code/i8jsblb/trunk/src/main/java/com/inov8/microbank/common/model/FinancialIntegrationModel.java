package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The FinancialIntegrationModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="FinancialIntegrationModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "FINANCIAL_INTEGRATION_seq",sequenceName = "FINANCIAL_INTEGRATION_seq", allocationSize=1)
@Table(name = "FINANCIAL_INTEGRATION")
public class FinancialIntegrationModel extends BasePersistableModel implements Serializable {
  


   private Collection<BankModel> financialIntegrationIdBankModelList = new ArrayList<BankModel>();

   private Long financialIntegrationId;
   private String name;
   private String className;
   private Date createdOn;
   private Date updatedOn;
   private Long createdBy;
   private Long updatedBy;
   private String description;
   private String comments;
   private Boolean isUsingVerifly;
   
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public FinancialIntegrationModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getFinancialIntegrationId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setFinancialIntegrationId(primaryKey);
    }

   /**
    * Returns the value of the <code>financialIntegrationId</code> property.
    *
    */
      @Column(name = "FINANCIAL_INTEGRATION_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FINANCIAL_INTEGRATION_seq")
   public Long getFinancialIntegrationId() {
      return financialIntegrationId;
   }

   /**
    * Sets the value of the <code>financialIntegrationId</code> property.
    *
    * @param financialIntegrationId the value for the <code>financialIntegrationId</code> property
    *    
		    */

   public void setFinancialIntegrationId(Long financialIntegrationId) {
      this.financialIntegrationId = financialIntegrationId;
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
    * Returns the value of the <code>className</code> property.
    *
    */
      @Column(name = "CLASS_NAME" , nullable = false , length=250 )
   public String getClassName() {
      return className;
   }

   /**
    * Sets the value of the <code>className</code> property.
    *
    * @param className the value for the <code>className</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setClassName(String className) {
      this.className = className;
   }

   /**
    * Returns the value of the <code>createdOn</code> property.
    *
    */
      @Column(name = "CREATED_ON" , nullable = false )
   public Date getCreatedOn() {
      return createdOn;
   }

   /**
    * Sets the value of the <code>createdOn</code> property.
    *
    * @param createdOn the value for the <code>createdOn</code> property
    *    
		    */

   public void setCreatedOn(Date createdOn) {
      this.createdOn = createdOn;
   }

   /**
    * Returns the value of the <code>updatedOn</code> property.
    *
    */
      @Column(name = "UPDATED_ON" , nullable = false )
   public Date getUpdatedOn() {
      return updatedOn;
   }

   /**
    * Sets the value of the <code>updatedOn</code> property.
    *
    * @param updatedOn the value for the <code>updatedOn</code> property
    *    
		    */

   public void setUpdatedOn(Date updatedOn) {
      this.updatedOn = updatedOn;
   }

   /**
    * Returns the value of the <code>createdBy</code> property.
    *
    */
      @Column(name = "CREATED_BY" , nullable = false )
   public Long getCreatedBy() {
      return createdBy;
   }

   /**
    * Sets the value of the <code>createdBy</code> property.
    *
    * @param createdBy the value for the <code>createdBy</code> property
    *    
		    */

   public void setCreatedBy(Long createdBy) {
      this.createdBy = createdBy;
   }

   /**
    * Returns the value of the <code>updatedBy</code> property.
    *
    */
      @Column(name = "UPDATED_BY" , nullable = false )
   public Long getUpdatedBy() {
      return updatedBy;
   }

   /**
    * Sets the value of the <code>updatedBy</code> property.
    *
    * @param updatedBy the value for the <code>updatedBy</code> property
    *    
		    */

   public void setUpdatedBy(Long updatedBy) {
      this.updatedBy = updatedBy;
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
    * Returns the value of the <code>comments</code> property.
    *
    */
      @Column(name = "COMMENTS"  , length=250 )
   public String getComments() {
      return comments;
   }

   /**
 * @return the isUsingVerifly
 */
@Column(name = "IS_USING_VERIFLY"  , nullable = false)
public Boolean getIsUsingVerifly()
{
	return isUsingVerifly;
}

/**
 * @param isUsingVerifly the isUsingVerifly to set
 */
public void setIsUsingVerifly(Boolean isUsingVerifly)
{
	this.isUsingVerifly = isUsingVerifly;
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
    * Returns the value of the <code>versionNo</code> property.
    *
    */
      @Version 
	    @Column(name = "VERSION_NO"  )
   public Integer getVersionNo() {
      return versionNo;
   }

   /**
    * Sets the value of the <code>versionNo</code> property.
    *
    * @param versionNo the value for the <code>versionNo</code> property
    *    
		    */

   public void setVersionNo(Integer versionNo) {
      this.versionNo = versionNo;
   }


   /**
    * Add the related BankModel to this one-to-many relation.
    *
    * @param bankModel object to be added.
    */
    
   public void addFinancialIntegrationIdBankModel(BankModel bankModel) {
      bankModel.setRelationFinancialIntegrationIdFinancialIntegrationModel(this);
      financialIntegrationIdBankModelList.add(bankModel);
   }
   
   /**
    * Remove the related BankModel to this one-to-many relation.
    *
    * @param bankModel object to be removed.
    */
   
   public void removeFinancialIntegrationIdBankModel(BankModel bankModel) {      
      bankModel.setRelationFinancialIntegrationIdFinancialIntegrationModel(null);
      financialIntegrationIdBankModelList.remove(bankModel);      
   }

   /**
    * Get a list of related BankModel objects of the FinancialIntegrationModel object.
    * These objects are in a bidirectional one-to-many relation by the FinancialIntegrationId member.
    *
    * @return Collection of BankModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationFinancialIntegrationIdFinancialIntegrationModel")
   @JoinColumn(name = "FINANCIAL_INTEGRATION_ID")
   public Collection<BankModel> getFinancialIntegrationIdBankModelList() throws Exception {
   		return financialIntegrationIdBankModelList;
   }


   /**
    * Set a list of BankModel related objects to the FinancialIntegrationModel object.
    * These objects are in a bidirectional one-to-many relation by the FinancialIntegrationId member.
    *
    * @param bankModelList the list of related objects.
    */
    public void setFinancialIntegrationIdBankModelList(Collection<BankModel> bankModelList) throws Exception {
		this.financialIntegrationIdBankModelList = bankModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getFinancialIntegrationId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&financialIntegrationId=" + getFinancialIntegrationId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "financialIntegrationId";
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
