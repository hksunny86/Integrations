package com.inov8.microbank.common.model.retailermodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The RetailerListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="RetailerListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "RETAILER_LIST_VIEW")
public class RetailerListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 4049329716702367718L;
	private Long retailerId;
   private String name;
   private String contactName;
   private String phoneNo;
   private String email;
   private String distributorName;
   private Long distributorId;
   private Boolean active;
   private Long regionId;
   private String regionName;
   

   /**
    * Default constructor.
    */
   public RetailerListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getRetailerId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setRetailerId(primaryKey);
    }

   /**
    * Returns the value of the <code>retailerId</code> property.
    *
    */
      @Column(name = "RETAILER_ID" , nullable = false )
   @Id 
   public Long getRetailerId() {
      return retailerId;
   }

   /**
    * Sets the value of the <code>retailerId</code> property.
    *
    * @param retailerId the value for the <code>retailerId</code> property
    *    
		    */

   public void setRetailerId(Long retailerId) {
      this.retailerId = retailerId;
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
    * Returns the value of the <code>contactName</code> property.
    *
    */
      @Column(name = "CONTACT_NAME" , nullable = false , length=50 )
   public String getContactName() {
      return contactName;
   }

   /**
    * Sets the value of the <code>contactName</code> property.
    *
    * @param contactName the value for the <code>contactName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setContactName(String contactName) {
      this.contactName = contactName;
   }

   /**
    * Returns the value of the <code>phoneNo</code> property.
    *
    */
      @Column(name = "PHONE_NO"  , length=50 )
   public String getPhoneNo() {
      return phoneNo;
   }

   /**
    * Sets the value of the <code>phoneNo</code> property.
    *
    * @param phoneNo the value for the <code>phoneNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setPhoneNo(String phoneNo) {
      this.phoneNo = phoneNo;
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

   @Column(name = "DISTRIBUTOR_NAME", nullable = false, length = 50)
   public String getDistributorName() {
       return this.distributorName;
   }

   public void setDistributorName(String distributorName) {
       this.distributorName = distributorName;
   }

   @Column(name = "DISTRIBUTOR_ID", nullable = false, precision = 10, scale = 0)
   public Long getDistributorId() {
       return this.distributorId;
   }

   public void setDistributorId(Long distributorId) {
       this.distributorId = distributorId;
   }

   /**
    * Returns the value of the <code>active</code> property.
    *
    */
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

   @Column(name = "REGION_ID", precision = 10, scale = 0)
   public Long getRegionId() {
       return this.regionId;
   }

   public void setRegionId(Long regionId) {
       this.regionId = regionId;
   }

   @Column(name = "REGION_NAME", length = 50)
   public String getRegionName() {
       return this.regionName;
   }

   public void setRegionName(String regionName) {
       this.regionName = regionName;
   }

    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getRetailerId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&retailerId=" + getRetailerId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "retailerId";
			return primaryKeyFieldName;				
    }       
}
