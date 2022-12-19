package com.inov8.microbank.common.model.portal.partnergroupmodule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The PartnerPermissionViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="PartnerPermissionViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "PARTNER_PERMISSION_VIEW")
public class PartnerPermissionViewModel extends BasePersistableModel implements Serializable {
    private static final long serialVersionUID = -8069484776076288677L;

   private Long pk;
   private Long partnerId;
   private Integer userPermissionSectionId;
   private String userPermissionSectionName;
   private Integer sequenceNo;
   private Long userPermissionId;
   
   private String name;
   private String shortName;
   private Boolean readAvailable;
   private Boolean updateAvailable;
   private Boolean deleteAvailable;
   private Boolean createAvailable;
   private Boolean isDefault;

   /**
    * Default constructor.
    */
   public PartnerPermissionViewModel() {
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
    * Returns the value of the <code>partnerId</code> property.
    *
    */
      @Column(name = "PARTNER_ID" , nullable = false )
   public Long getPartnerId() {
      return partnerId;
   }

   /**
    * Sets the value of the <code>partnerId</code> property.
    *
    * @param partnerId the value for the <code>partnerId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setPartnerId(Long partnerId) {
      this.partnerId = partnerId;
   }

   /**
    * Returns the value of the <code>userPermissionId</code> property.
    *
    */
      @Column(name = "USER_PERMISSION_ID" , nullable = false )
   public Long getUserPermissionId() {
      return userPermissionId;
   }

   /**
    * Sets the value of the <code>userPermissionId</code> property.
    *
    * @param userPermissionId the value for the <code>userPermissionId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

    public void setUserPermissionId( Long userPermissionId )
    {
        this.userPermissionId = userPermissionId;
    }

    @Column(name="USER_PERMISSION_SECTION_ID")
    public Integer getUserPermissionSectionId()
    {
        return userPermissionSectionId;
    }

    public void setUserPermissionSectionId( Integer userPermissionSectionId )
    {
        this.userPermissionSectionId = userPermissionSectionId;
    }

    @Column(name="USER_PERMISSION_SECTION_NAME")
    public String getUserPermissionSectionName()
    {
        return userPermissionSectionName;
    }

    public void setUserPermissionSectionName( String userPermissionSectionName )
    {
        this.userPermissionSectionName = userPermissionSectionName;
    }

    @Column(name="SEQUENCE_NO")
    public Integer getSequenceNo()
    {
        return sequenceNo;
    }

    public void setSequenceNo( Integer sequenceNo )
    {
        this.sequenceNo = sequenceNo;
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
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "SHORT_NAME" , nullable = false , length=50 )
   public String getShortName() {
      return shortName;
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

   public void setShortName(String shortName) {
      this.shortName = shortName;
   }

   /**
    * Returns the value of the <code>readAvailable</code> property.
    *
    */
      @Column(name = "IS_READ_AVAILABLE" , nullable = false )
   public Boolean getReadAvailable() {
      return readAvailable;
   }

   /**
    * Sets the value of the <code>readAvailable</code> property.
    *
    * @param readAvailable the value for the <code>readAvailable</code> property
    *    
		    */

   public void setReadAvailable(Boolean readAvailable) {
      this.readAvailable = readAvailable;
   }

   /**
    * Returns the value of the <code>updateAvailable</code> property.
    *
    */
      @Column(name = "IS_UPDATE_AVAILABLE" , nullable = false )
   public Boolean getUpdateAvailable() {
      return updateAvailable;
   }

   /**
    * Sets the value of the <code>updateAvailable</code> property.
    *
    * @param updateAvailable the value for the <code>updateAvailable</code> property
    *    
		    */

   public void setUpdateAvailable(Boolean updateAvailable) {
      this.updateAvailable = updateAvailable;
   }

   /**
    * Returns the value of the <code>deleteAvailable</code> property.
    *
    */
      @Column(name = "IS_DELETE_AVAILABLE" , nullable = false )
   public Boolean getDeleteAvailable() {
      return deleteAvailable;
   }

   /**
    * Sets the value of the <code>deleteAvailable</code> property.
    *
    * @param deleteAvailable the value for the <code>deleteAvailable</code> property
    *    
		    */

   public void setDeleteAvailable(Boolean deleteAvailable) {
      this.deleteAvailable = deleteAvailable;
   }

   /**
    * Returns the value of the <code>createAvailable</code> property.
    *
    */
      @Column(name = "IS_CREATE_AVAILABLE" , nullable = false )
   public Boolean getCreateAvailable() {
      return createAvailable;
   }

   /**
    * Sets the value of the <code>createAvailable</code> property.
    *
    * @param createAvailable the value for the <code>createAvailable</code> property
    *    
		    */

   public void setCreateAvailable(Boolean createAvailable) {
      this.createAvailable = createAvailable;
   }

   /**
    * Returns the value of the <code>createAvailable</code> property.
    *
    */
      @Column(name = "IS_DEFAULT" , nullable = false )
   public Boolean getIsDefault() {
      return this.isDefault;
   }

   /**
    * Sets the value of the <code>createAvailable</code> property.
    *
    * @param createAvailable the value for the <code>createAvailable</code> property
    *    
		    */

   public void setIsDefault(Boolean isDefault) {
      this.isDefault = isDefault;
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
