package com.inov8.microbank.common.model.goldennosmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The GoldenNosListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="GoldenNosListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "GOLDEN_NOS_LIST_VIEW")
public class GoldenNosListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 1595224771023748791L;
private Long deviceTypeId;
   private String goldenNumber;
   private Long goldenNosId;
   private String name;

   /**
    * Default constructor.
    */
   public GoldenNosListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getGoldenNosId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setGoldenNosId(primaryKey);
    }

   /**
    * Returns the value of the <code>deviceTypeId</code> property.
    *
    */
      @Column(name = "DEVICE_TYPE_ID" , nullable = false )
   public Long getDeviceTypeId() {
      return deviceTypeId;
   }

   /**
    * Sets the value of the <code>deviceTypeId</code> property.
    *
    * @param deviceTypeId the value for the <code>deviceTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setDeviceTypeId(Long deviceTypeId) {
      this.deviceTypeId = deviceTypeId;
   }

   /**
    * Returns the value of the <code>goldenNumber</code> property.
    *
    */
      @Column(name = "GOLDEN_NUMBER" , nullable = false )
   public String getGoldenNumber() {
      return goldenNumber;
   }

   /**
    * Sets the value of the <code>goldenNumber</code> property.
    *
    * @param goldenNumber the value for the <code>goldenNumber</code> property
    *    
	* @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"			
    */

   public void setGoldenNumber(String goldenNumber) {
      this.goldenNumber = goldenNumber;
   }

   /**
    * Returns the value of the <code>goldenNosId</code> property.
    *
    */
      @Column(name = "GOLDEN_NOS_ID" , nullable = false )
   @Id 
   public Long getGoldenNosId() {
      return goldenNosId;
   }

   /**
    * Sets the value of the <code>goldenNosId</code> property.
    *
    * @param goldenNosId the value for the <code>goldenNosId</code> property
    *    
		    */

   public void setGoldenNosId(Long goldenNosId) {
      this.goldenNosId = goldenNosId;
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
        checkBox += "_"+ getGoldenNosId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&goldenNosId=" + getGoldenNosId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "goldenNosId";
			return primaryKeyFieldName;				
    }       
}
