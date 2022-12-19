package com.inov8.verifly.common.model;

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
 * The AccountTypeModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AccountTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ACCOUNT_TYPE_seq",sequenceName = "VF_ACCOUNT_TYPE_seq")
@Table(name = "VF_ACCOUNT_TYPE")
public class VfAccountTypeModel extends BasePersistableModel implements Serializable {
  


   private Collection<AccountInfoModel> accountTypeIdAccountInfoModelList = new ArrayList<AccountInfoModel>();

   private Long accountTypeId;
   private String name;
   private String accountTypeCode;
   private String description;

   /**
    * Default constructor.
    */
   public VfAccountTypeModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAccountTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAccountTypeId(primaryKey);
    }

   /**
    * Returns the value of the <code>accountTypeId</code> property.
    *
    */
      @Column(name = "ACCOUNT_TYPE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACCOUNT_TYPE_seq")
   public Long getAccountTypeId() {
      return accountTypeId;
   }

   /**
    * Sets the value of the <code>accountTypeId</code> property.
    *
    * @param accountTypeId the value for the <code>accountTypeId</code> property
    *    
		    */

   public void setAccountTypeId(Long accountTypeId) {
      this.accountTypeId = accountTypeId;
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
    * Returns the value of the <code>accountTypeCode</code> property.
    *
    */
      @Column(name = "ACCOUNT_TYPE_CODE" , nullable = false , length=50 )
   public String getAccountTypeCode() {
      return accountTypeCode;
   }

   /**
    * Sets the value of the <code>accountTypeCode</code> property.
    *
    * @param accountTypeCode the value for the <code>accountTypeCode</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAccountTypeCode(String accountTypeCode) {
      this.accountTypeCode = accountTypeCode;
   }

   /**
    * Returns the value of the <code>description</code> property.
    *
    */
      @Column(name = "DESCRIPTION"  , length=255 )
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
    * @spring.validator-var name="maxlength" value="255"
    */

   public void setDescription(String description) {
      this.description = description;
   }


   /**
    * Add the related AccountInfoModel to this one-to-many relation.
    *
    * @param accountInfoModel object to be added.
    */
    
   public void addAccountTypeIdAccountInfoModel(AccountInfoModel accountInfoModel) {
      accountInfoModel.setRelationAccountTypeIdAccountTypeModel(this);
      accountTypeIdAccountInfoModelList.add(accountInfoModel);
   }
   
   /**
    * Remove the related AccountInfoModel to this one-to-many relation.
    *
    * @param accountInfoModel object to be removed.
    */
   
   public void removeAccountTypeIdAccountInfoModel(AccountInfoModel accountInfoModel) {      
      accountInfoModel.setRelationAccountTypeIdAccountTypeModel(null);
      accountTypeIdAccountInfoModelList.remove(accountInfoModel);      
   }

   /**
    * Get a list of related AccountInfoModel objects of the AccountTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AccountTypeId member.
    *
    * @return Collection of AccountInfoModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAccountTypeIdAccountTypeModel")
   @JoinColumn(name = "ACCOUNT_TYPE_ID")
   public Collection<AccountInfoModel> getAccountTypeIdAccountInfoModelList() throws Exception {
   		return accountTypeIdAccountInfoModelList;
   }


   /**
    * Set a list of AccountInfoModel related objects to the AccountTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the AccountTypeId member.
    *
    * @param accountInfoModelList the list of related objects.
    */
    public void setAccountTypeIdAccountInfoModelList(Collection<AccountInfoModel> accountInfoModelList) throws Exception {
		this.accountTypeIdAccountInfoModelList = accountInfoModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAccountTypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&accountTypeId=" + getAccountTypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "accountTypeId";
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
