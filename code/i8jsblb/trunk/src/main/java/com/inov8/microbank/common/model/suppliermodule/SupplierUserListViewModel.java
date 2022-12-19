package com.inov8.microbank.common.model.suppliermodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The SupplierUserListViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="SupplierUserListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "SUPPLIER_USER_LIST_VIEW")
public class SupplierUserListViewModel extends BasePersistableModel implements Serializable {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = -3777610071644298252L;
private Long supplierUserId;
   private Long supplierId;
   private Integer versionNo;
   private Long appUserId;
   private String firstName;
   private String lastName;
   private String mobileNo;
   private String email;
   private String username;
   private String supplierName;
   private Boolean accountEnabled;
   private String partnerGroupId;
   private String partnerGroupName;

   /**
    * Default constructor.
    */
   public SupplierUserListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getSupplierUserId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setSupplierUserId(primaryKey);
    }

   /**
    * Returns the value of the <code>supplierUserId</code> property.
    *
    */
      @Column(name = "SUPPLIER_USER_ID" , nullable = false )
   @Id 
   public Long getSupplierUserId() {
      return supplierUserId;
   }

   /**
    * Sets the value of the <code>supplierUserId</code> property.
    *
    * @param supplierUserId the value for the <code>supplierUserId</code> property
    *    
		    */

   public void setSupplierUserId(Long supplierUserId) {
      this.supplierUserId = supplierUserId;
   }

   /**
    * Returns the value of the <code>supplierId</code> property.
    *
    */
      @Column(name = "SUPPLIER_ID" , nullable = false )
   public Long getSupplierId() {
      return supplierId;
   }

   /**
    * Sets the value of the <code>supplierId</code> property.
    *
    * @param supplierId the value for the <code>supplierId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setSupplierId(Long supplierId) {
      this.supplierId = supplierId;
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
    * Returns the value of the <code>appUserId</code> property.
    *
    */
      @Column(name = "APP_USER_ID" , nullable = false )
   public Long getAppUserId() {
      return appUserId;
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setAppUserId(Long appUserId) {
      this.appUserId = appUserId;
   }

   /**
    * Returns the value of the <code>firstName</code> property.
    *
    */
      @Column(name = "FIRST_NAME" , nullable = false , length=50 )
   public String getFirstName() {
      return firstName;
   }

   /**
    * Sets the value of the <code>firstName</code> property.
    *
    * @param firstName the value for the <code>firstName</code> property
    *    
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
    * Returns the value of the <code>mobileNo</code> property.
    *
    */
      @Column(name = "MOBILE_NO" , nullable = false , length=50 )
   public String getMobileNo() {
      return mobileNo;
   }

   /**
    * Sets the value of the <code>mobileNo</code> property.
    *
    * @param mobileNo the value for the <code>mobileNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMobileNo(String mobileNo) {
      this.mobileNo = mobileNo;
   }

   /**
    * Returns the value of the <code>email</code> property.
    *
    */
      @Column(name = "EMAIL"  , length=50 )
   public String getEmail() {
      return email;
   }

   /**
    * Sets the value of the <code>email</code> property.
    *
    * @param email the value for the <code>email</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setEmail(String email) {
      this.email = email;
   }

   /**
    * Returns the value of the <code>username</code> property.
    *
    */
      @Column(name = "USERNAME" , nullable = false , length=50 )
   public String getUsername() {
      return username;
   }

   /**
    * Sets the value of the <code>username</code> property.
    *
    * @param username the value for the <code>username</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setUsername(String username) {
      this.username = username;
   }

   /**
    * Returns the value of the <code>supplierName</code> property.
    *
    */
      @Column(name = "SUPPLIER_NAME" , nullable = false , length=50 )
   public String getSupplierName() {
      return supplierName;
   }

   /**
    * Sets the value of the <code>supplierName</code> property.
    *
    * @param supplierName the value for the <code>supplierName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setSupplierName(String supplierName) {
      this.supplierName = supplierName;
   }

   /**
    * Returns the value of the <code>accountEnabled</code> property.
    *
    */
      @Column(name = "IS_ACCOUNT_ENABLED" , nullable = false )
   public Boolean getAccountEnabled() {
      return accountEnabled;
   }

   /**
    * Sets the value of the <code>accountEnabled</code> property.
    *
    * @param accountEnabled the value for the <code>accountEnabled</code> property
    *    
		    */

   public void setAccountEnabled(Boolean accountEnabled) {
      this.accountEnabled = accountEnabled;
   }

   /**
    * Returns the value of the <code>partnerGroupId</code> property.
    *
    */
      @Column(name = "PARTNER_GROUP_ID"  )
   public String getPartnerGroupId() {
      return partnerGroupId;
   }

   /**
    * Sets the value of the <code>partnerGroupId</code> property.
    *
    * @param partnerGroupId the value for the <code>partnerGroupId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="${field.Size}"
    */

   public void setPartnerGroupId(String partnerGroupId) {
      this.partnerGroupId = partnerGroupId;
   }

   /**
    * Returns the value of the <code>partnerGroupName</code> property.
    *
    */
      @Column(name = "PARTNER_GROUP_NAME"  )
   public String getPartnerGroupName() {
      return partnerGroupName;
   }

   /**
    * Sets the value of the <code>partnerGroupName</code> property.
    *
    * @param partnerGroupName the value for the <code>partnerGroupName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="${field.Size}"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setPartnerGroupName(String partnerGroupName) {
      this.partnerGroupName = partnerGroupName;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getSupplierUserId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&supplierUserId=" + getSupplierUserId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "supplierUserId";
			return primaryKeyFieldName;				
    }       
}
