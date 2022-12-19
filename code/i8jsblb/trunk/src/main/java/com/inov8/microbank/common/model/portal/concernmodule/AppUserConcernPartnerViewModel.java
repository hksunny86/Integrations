package com.inov8.microbank.common.model.portal.concernmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The AppUserConcernPartnerViewModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="AppUserConcernPartnerViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "APP_USER_CONCERN_PARTNER_VIEW")
public class AppUserConcernPartnerViewModel extends BasePersistableModel {
  



   private Long concernPartnerId;
   private Long appUserId;

   /**
    * Default constructor.
    */
   public AppUserConcernPartnerViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAppUserId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setAppUserId(primaryKey);
    }

   /**
    * Returns the value of the <code>concernPartnerId</code> property.
    *
    */
      @Column(name = "CONCERN_PARTNER_ID"  )
   public Long getConcernPartnerId() {
      return concernPartnerId;
   }

   /**
    * Sets the value of the <code>concernPartnerId</code> property.
    *
    * @param concernPartnerId the value for the <code>concernPartnerId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setConcernPartnerId(Long concernPartnerId) {
      this.concernPartnerId = concernPartnerId;
   }

   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
      @Column(name = "APP_USER_ID"  )
   @Id 
   public Long getAppUserId() {
      return appUserId;
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
    *    
		    */

   public void setAppUserId(Long appUserId) {
      this.appUserId = appUserId;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getAppUserId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&appUserId=" + getAppUserId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "appUserId";
			return primaryKeyFieldName;				
    }       
}
