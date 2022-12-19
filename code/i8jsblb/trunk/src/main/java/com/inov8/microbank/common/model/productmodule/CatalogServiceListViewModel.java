package com.inov8.microbank.common.model.productmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CatalogServiceListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CatalogServiceListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "CATALOG_SERVICE_LIST_VIEW")
public class CatalogServiceListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = -9183786833709956409L;
private Long serviceTypeId;
   private Long serviceId;
   private String serviceName;

   /**
    * Default constructor.
    */
   public CatalogServiceListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getServiceId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setServiceId(primaryKey);
    }

   /**
    * Returns the value of the <code>serviceTypeId</code> property.
    *
    */
      @Column(name = "SERVICE_TYPE_ID" , nullable = false )
   public Long getServiceTypeId() {
      return serviceTypeId;
   }

   /**
    * Sets the value of the <code>serviceTypeId</code> property.
    *
    * @param serviceTypeId the value for the <code>serviceTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setServiceTypeId(Long serviceTypeId) {
      this.serviceTypeId = serviceTypeId;
   }

   /**
    * Returns the value of the <code>serviceId</code> property.
    *
    */
      @Column(name = "SERVICE_ID" , nullable = false )
   @Id 
   public Long getServiceId() {
      return serviceId;
   }

   /**
    * Sets the value of the <code>serviceId</code> property.
    *
    * @param serviceId the value for the <code>serviceId</code> property
    *    
		    */

   public void setServiceId(Long serviceId) {
      this.serviceId = serviceId;
   }

   /**
    * Returns the value of the <code>serviceName</code> property.
    *
    */
      @Column(name = "SERVICE_NAME" , nullable = false , length=50 )
   public String getServiceName() {
      return serviceName;
   }

   /**
    * Sets the value of the <code>serviceName</code> property.
    *
    * @param serviceName the value for the <code>serviceName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setServiceName(String serviceName) {
      this.serviceName = serviceName;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getServiceId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&serviceId=" + getServiceId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "serviceId";
			return primaryKeyFieldName;				
    }       
}
