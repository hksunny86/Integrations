package com.inov8.microbank.common.model.distributormodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The DistributorLevelListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="DistributorLevelListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "DISTRIBUTOR_LEVEL_LIST_VIEW")
public class DistributorLevelListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = -2632342968852977095L;
private Long distributorLevelId;
   private Long managingLevelId;
   private String managingLevelName;
   private Long ultimateManagingLevelId;
   private String ultimateLevelName;
   private String distributorLevelName;
   private String description;
   private Integer versionNo;
   private Long distributorId;
   private String name;

   /**
    * Default constructor.
    */
   public DistributorLevelListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getDistributorLevelId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setDistributorLevelId(primaryKey);
    }

   /**
    * Returns the value of the <code>distributorLevelId</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_LEVEL_ID" , nullable = false )
   @Id 
   public Long getDistributorLevelId() {
      return distributorLevelId;
   }

   /**
    * Sets the value of the <code>distributorLevelId</code> property.
    *
    * @param distributorLevelId the value for the <code>distributorLevelId</code> property
    *    
		    */

   public void setDistributorLevelId(Long distributorLevelId) {
      this.distributorLevelId = distributorLevelId;
   }

   /**
    * Returns the value of the <code>managingLevelId</code> property.
    *
    */
      @Column(name = "MANAGING_LEVEL_ID"  )
   public Long getManagingLevelId() {
      return managingLevelId;
   }

   /**
    * Sets the value of the <code>managingLevelId</code> property.
    *
    * @param managingLevelId the value for the <code>managingLevelId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setManagingLevelId(Long managingLevelId) {
      this.managingLevelId = managingLevelId;
   }

   /**
    * Returns the value of the <code>managingLevelName</code> property.
    *
    */
      @Column(name = "MANAGING_LEVEL_NAME"  , length=50 )
   public String getManagingLevelName() {
      return managingLevelName;
   }

   /**
    * Sets the value of the <code>managingLevelName</code> property.
    *
    * @param managingLevelName the value for the <code>managingLevelName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setManagingLevelName(String managingLevelName) {
      this.managingLevelName = managingLevelName;
   }

   /**
    * Returns the value of the <code>ultimateManagingLevelId</code> property.
    *
    */
      @Column(name = "ULTIMATE_MANAGING_LEVEL_ID"  )
   public Long getUltimateManagingLevelId() {
      return ultimateManagingLevelId;
   }

   /**
    * Sets the value of the <code>ultimateManagingLevelId</code> property.
    *
    * @param ultimateManagingLevelId the value for the <code>ultimateManagingLevelId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setUltimateManagingLevelId(Long ultimateManagingLevelId) {
      this.ultimateManagingLevelId = ultimateManagingLevelId;
   }

   /**
    * Returns the value of the <code>ultimateLevelName</code> property.
    *
    */
      @Column(name = "ULTIMATE_LEVEL_NAME"  , length=50 )
   public String getUltimateLevelName() {
      return ultimateLevelName;
   }

   /**
    * Sets the value of the <code>ultimateLevelName</code> property.
    *
    * @param ultimateLevelName the value for the <code>ultimateLevelName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setUltimateLevelName(String ultimateLevelName) {
      this.ultimateLevelName = ultimateLevelName;
   }

   /**
    * Returns the value of the <code>distributorLevelName</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_LEVEL_NAME" , nullable = false , length=50 )
   public String getDistributorLevelName() {
      return distributorLevelName;
   }

   /**
    * Sets the value of the <code>distributorLevelName</code> property.
    *
    * @param distributorLevelName the value for the <code>distributorLevelName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setDistributorLevelName(String distributorLevelName) {
      this.distributorLevelName = distributorLevelName;
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
    * Returns the value of the <code>distributorId</code> property.
    *
    */
      @Column(name = "DISTRIBUTOR_ID" , nullable = false )
   public Long getDistributorId() {
      return distributorId;
   }

   /**
    * Sets the value of the <code>distributorId</code> property.
    *
    * @param distributorId the value for the <code>distributorId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setDistributorId(Long distributorId) {
      this.distributorId = distributorId;
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getDistributorLevelId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&distributorLevelId=" + getDistributorLevelId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "distributorLevelId";
			return primaryKeyFieldName;				
    }       
}
