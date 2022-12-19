package com.inov8.microbank.common.model.creditmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CreditInfoListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CreditInfoListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "CREDIT_INFO_LIST_VIEW")
public class CreditInfoListViewModel extends BasePersistableModel {
  



   private Long pk;
   private Long id;
   private Long organization;
   private Double balance;
   private String name;
   private String organizationName;
   private String mobileNo;
   private Boolean active;
   private String type;

   /**
    * Default constructor.
    */
   public CreditInfoListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getPk();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setPk(primaryKey);
    }

   /**
    * Returns the value of the <code>pk</code> property.
    *
    */
      @Column(name = "PK"  )
   @Id 
   public Long getPk() {
      return pk;
   }

   /**
    * Sets the value of the <code>pk</code> property.
    *
    * @param pk the value for the <code>pk</code> property
    *    
		    */

   public void setPk(Long pk) {
      this.pk = pk;
   }

   /**
    * Returns the value of the <code>id</code> property.
    *
    */
      @Column(name = "ID"  )
   public Long getId() {
      return id;
   }

   /**
    * Sets the value of the <code>id</code> property.
    *
    * @param id the value for the <code>id</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setId(Long id) {
      this.id = id;
   }

   /**
    * Returns the value of the <code>organization</code> property.
    *
    */
      @Column(name = "ORGANIZATION"  )
   public Long getOrganization() {
      return organization;
   }

   /**
    * Sets the value of the <code>organization</code> property.
    *
    * @param organization the value for the <code>organization</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setOrganization(Long organization) {
      this.organization = organization;
   }

   /**
    * Returns the value of the <code>balance</code> property.
    *
    */
      @Column(name = "BALANCE"  )
   public Double getBalance() {
      return balance;
   }

   /**
    * Sets the value of the <code>balance</code> property.
    *
    * @param balance the value for the <code>balance</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setBalance(Double balance) {
      this.balance = balance;
   }

   /**
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME"  , length=101 )
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
    * @spring.validator-var name="maxlength" value="101"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setName(String name) {
      this.name = name;
   }

   /**
    * Returns the value of the <code>organizationName</code> property.
    *
    */
      @Column(name = "ORGANIZATION_NAME"  , length=50 )
   public String getOrganizationName() {
      return organizationName;
   }

   /**
    * Sets the value of the <code>organizationName</code> property.
    *
    * @param organizationName the value for the <code>organizationName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setOrganizationName(String organizationName) {
      this.organizationName = organizationName;
   }

   /**
    * Returns the value of the <code>mobileNo</code> property.
    *
    */
      @Column(name = "MOBILE_NO"  , length=50 )
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
    * Returns the value of the <code>active</code> property.
    *
    */
      @Column(name = "IS_ACTIVE"  )
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
    * Returns the value of the <code>type</code> property.
    *
    */
      @Column(name = "TYPE"  , length=11 )
   public String getType() {
      return type;
   }

   /**
    * Sets the value of the <code>type</code> property.
    *
    * @param type the value for the <code>type</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="11"
    */

   public void setType(String type) {
      this.type = type;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getPk();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&pk=" + getPk();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "pk";
			return primaryKeyFieldName;				
    }       
}
