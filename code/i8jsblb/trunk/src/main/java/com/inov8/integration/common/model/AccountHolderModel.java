package com.inov8.integration.common.model;

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
 * The AccountHolderModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2008/11/04 19:29:08 $
 *
 *
 * @spring.bean name="AccountHolderModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "ACCOUNT_HOLDER_seq",sequenceName = "ACCOUNT_HOLDER_seq", allocationSize=1)
@Table(name = "ACCOUNT_HOLDER")
public class AccountHolderModel extends BasePersistableModel implements Serializable {
  


   private Collection<AccountModel> accountHolderIdAccountModelList = new ArrayList<AccountModel>();

   private Long accountHolderId;
   private String firstName;
   private String lastName;
   private String middleName;
   private String address;
   private String cnic;
   private String fatherName;
   private Date createdOn;
   private Date updatedOn;
   private Boolean active;
   private Boolean deleted;
   private String landlineNumber;
   private String mobileNumber;
   private String dob;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public AccountHolderModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAccountHolderId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAccountHolderId(primaryKey);
    }
   
   /**
    * Returns the value of the <code>versionNo</code> property.
    *
    */
      @Version 
	    @Column(name = "VERSION_NO" , nullable = false )
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
    * Returns the value of the <code>accountHolderId</code> property.
    *
    */
      @Column(name = "ACCOUNT_HOLDER_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACCOUNT_HOLDER_seq")
   public Long getAccountHolderId() {
      return accountHolderId;
   }

   /**
    * Sets the value of the <code>accountHolderId</code> property.
    *
    * @param accountHolderId the value for the <code>accountHolderId</code> property
    *    
		    */

   public void setAccountHolderId(Long accountHolderId) {
      this.accountHolderId = accountHolderId;
   }

   /**
    * Returns the value of the <code>firstName</code> property.
    *
    */
      @Column(name = "FIRST_NAME" , nullable = false , length=100 )
   public String getFirstName() {
      return firstName;
   }

   /**
    * Sets the value of the <code>firstName</code> property.
    *
    * @param firstName the value for the <code>firstName</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   /**
    * Returns the value of the <code>lastName</code> property.
    *
    */
      @Column(name = "LAST_NAME" , nullable = false , length=50 )
   public String getLastName() {
      return lastName;
   }

   /**
    * Sets the value of the <code>lastName</code> property.
    *
    * @param lastName the value for the <code>lastName</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   /**
    * Returns the value of the <code>middleName</code> property.
    *
    */
      @Column(name = "MIDDLE_NAME" , nullable = false , length=50 )
   public String getMiddleName() {
      return middleName;
   }

   /**
    * Sets the value of the <code>middleName</code> property.
    *
    * @param middleName the value for the <code>middleName</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setMiddleName(String middleName) {
      this.middleName = middleName;
   }

   /**
    * Returns the value of the <code>address</code> property.
    *
    */
      @Column(name = "ADDRESS" , nullable = false , length=250 )
   public String getAddress() {
      return address;
   }

   /**
    * Sets the value of the <code>address</code> property.
    *
    * @param address the value for the <code>address</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setAddress(String address) {
      this.address = address;
   }

   /**
    * Returns the value of the <code>cnic</code> property.
    *
    */
      @Column(name = "CNIC" , nullable = false , length=255 )
   public String getCnic() {
      return cnic;
   }

   /**
    * Sets the value of the <code>cnic</code> property.
    *
    * @param cnic the value for the <code>cnic</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="15"
    */

   public void setCnic(String cnic) {
      this.cnic = cnic;
   }

   /**
    * Returns the value of the <code>fatherName</code> property.
    *
    */
      @Column(name = "FATHER_NAME" , nullable = false , length=100 )
   public String getFatherName() {
      return fatherName;
   }

   /**
    * Sets the value of the <code>fatherName</code> property.
    *
    * @param fatherName the value for the <code>fatherName</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="100"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setFatherName(String fatherName) {
      this.fatherName = fatherName;
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
    * Returns the value of the <code>active</code> property.
    *
    */
      @Column(name = "IS_ACTIVE" , nullable = true )
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
    * Returns the value of the <code>deleted</code> property.
    *
    */
      @Column(name = "IS_DELETED" , nullable = true )
   public Boolean getDeleted() {
      return deleted;
   }

   /**
    * Sets the value of the <code>deleted</code> property.
    *
    * @param deleted the value for the <code>deleted</code> property
    *    
		    */

   public void setDeleted(Boolean deleted) {
      this.deleted = deleted;
   }

   /**
    * Returns the value of the <code>landlineNumber</code> property.
    *
    */
      @Column(name = "LANDLINE_NUMBER" , nullable = false , length=50 )
   public String getLandlineNumber() {
      return landlineNumber;
   }

   /**
    * Sets the value of the <code>landlineNumber</code> property.
    *
    * @param landlineNumber the value for the <code>landlineNumber</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setLandlineNumber(String landlineNumber) {
      this.landlineNumber = landlineNumber;
   }

   /**
    * Returns the value of the <code>mobileNumber</code> property.
    *
    */
      @Column(name = "MOBILE_NUMBER" , nullable = false , length=50 )
   public String getMobileNumber() {
      return mobileNumber;
   }

   /**
    * Sets the value of the <code>mobileNumber</code> property.
    *
    * @param mobileNumber the value for the <code>mobileNumber</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMobileNumber(String mobileNumber) {
      this.mobileNumber = mobileNumber;
   }

   /**
    * Returns the value of the <code>dob</code> property.
    *
    */
      @Column(name = "DOB" , nullable = false, length=255 )
   public String getDob() {
      return dob;
   }

   /**
    * Sets the value of the <code>dob</code> property.
    *
    * @param dob the value for the <code>dob</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setDob(String dob) {
      this.dob = dob;
   }


   /**
    * Add the related AccountModel to this one-to-many relation.
    *
    * @param accountModel object to be added.
    */
    
   public void addAccountHolderIdAccountModel(AccountModel accountModel) {
      accountModel.setRelationAccountHolderIdAccountHolderModel(this);
      accountHolderIdAccountModelList.add(accountModel);
   }
   
   /**
    * Remove the related AccountModel to this one-to-many relation.
    *
    * @param accountModel object to be removed.
    */
   
   public void removeAccountHolderIdAccountModel(AccountModel accountModel) {      
      accountModel.setRelationAccountHolderIdAccountHolderModel(null);
      accountHolderIdAccountModelList.remove(accountModel);      
   }

   /**
    * Get a list of related AccountModel objects of the AccountHolderModel object.
    * These objects are in a bidirectional one-to-many relation by the AccountHolderId member.
    *
    * @return Collection of AccountModel objects.
    *
    */
   
//   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationAccountHolderIdAccountHolderModel")
   @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, 
		   fetch = FetchType.LAZY,mappedBy = "relationAccountHolderIdAccountHolderModel")
   @JoinColumn(name = "ACCOUNT_HOLDER_ID")
   @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
                                      org.hibernate.annotations.CascadeType.PERSIST,
                                      org.hibernate.annotations.CascadeType.MERGE,
                                      org.hibernate.annotations.CascadeType.REFRESH})
   public Collection<AccountModel> getAccountHolderIdAccountModelList() throws Exception {
   		return accountHolderIdAccountModelList;
   }


   /**
    * Set a list of AccountModel related objects to the AccountHolderModel object.
    * These objects are in a bidirectional one-to-many relation by the AccountHolderId member.
    *
    * @param accountModelList the list of related objects.
    */
    public void setAccountHolderIdAccountModelList(Collection<AccountModel> accountModelList) throws Exception {
		this.accountHolderIdAccountModelList = accountModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAccountHolderId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&accountHolderId=" + getAccountHolderId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "accountHolderId";
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
