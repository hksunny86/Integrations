package com.inov8.microbank.common.model.portal.concernmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ConcernOpRaisedToListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ConcernOpRaisedToListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "CONCERN_OP_RAISED_TO_LIST_VIEW")
public class ConcernOpRaisedToListViewModel extends BasePersistableModel {
  



   private Long pk;
   private Long concernPartnerId;
   private String partnerName;
   private Long concernPartnerTypeId;
   private String concernPartnerTypeName;
   private String concernCode;
   private Boolean partnerActive;

   /**
    * Default constructor.
    */
   public ConcernOpRaisedToListViewModel() {
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
    * Returns the value of the <code>partnerName</code> property.
    *
    */
      @Column(name = "PARTNER_NAME"  , length=50 )
   public String getPartnerName() {
      return partnerName;
   }

   /**
    * Sets the value of the <code>partnerName</code> property.
    *
    * @param partnerName the value for the <code>partnerName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setPartnerName(String partnerName) {
      this.partnerName = partnerName;
   }

   /**
    * Returns the value of the <code>concernPartnerTypeId</code> property.
    *
    */
      @Column(name = "CONCERN_PARTNER_TYPE_ID"  )
   public Long getConcernPartnerTypeId() {
      return concernPartnerTypeId;
   }

   /**
    * Sets the value of the <code>concernPartnerTypeId</code> property.
    *
    * @param concernPartnerTypeId the value for the <code>concernPartnerTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setConcernPartnerTypeId(Long concernPartnerTypeId) {
      this.concernPartnerTypeId = concernPartnerTypeId;
   }

   /**
    * Returns the value of the <code>concernPartnerTypeName</code> property.
    *
    */
      @Column(name = "CONCERN_PARTNER_TYPE_NAME"  , length=50 )
   public String getConcernPartnerTypeName() {
      return concernPartnerTypeName;
   }

   /**
    * Sets the value of the <code>concernPartnerTypeName</code> property.
    *
    * @param concernPartnerTypeName the value for the <code>concernPartnerTypeName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setConcernPartnerTypeName(String concernPartnerTypeName) {
      this.concernPartnerTypeName = concernPartnerTypeName;
   }

   /**
    * Returns the value of the <code>concernCode</code> property.
    *
    */
      @Column(name = "CONCERN_CODE"  , length=50 )
   public String getConcernCode() {
      return concernCode;
   }

   /**
    * Sets the value of the <code>concernCode</code> property.
    *
    * @param concernCode the value for the <code>concernCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setConcernCode(String concernCode) {
      this.concernCode = concernCode;
   }

   /**
    * Returns the value of the <code>partnerActive</code> property.
    *
    */
      @Column(name = "IS_PARTNER_ACTIVE"  )
   public Boolean getPartnerActive() {
      return partnerActive;
   }

   /**
    * Sets the value of the <code>partnerActive</code> property.
    *
    * @param partnerActive the value for the <code>partnerActive</code> property
    *    
		    */

   public void setPartnerActive(Boolean partnerActive) {
      this.partnerActive = partnerActive;
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
