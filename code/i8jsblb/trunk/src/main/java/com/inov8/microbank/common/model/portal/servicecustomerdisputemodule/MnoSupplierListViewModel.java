package com.inov8.microbank.common.model.portal.servicecustomerdisputemodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The MnoSupplierListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="MnoSupplierListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "SERVICE_OP_SUPPLIER_LIST_VIEW")
public class MnoSupplierListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 5011088962216149421L;
private Long mnoUserId;
   private Long mnoId;
   private String name;
   private Long supplierId;

   /**
    * Default constructor.
    */
   public MnoSupplierListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getMnoUserId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setMnoUserId(primaryKey);
    }

   /**
    * Returns the value of the <code>mnoUserId</code> property.
    *
    */
      @Column(name = "SERVICE_OP_USER_ID" , nullable = false )
   @Id 
   public Long getMnoUserId() {
      return mnoUserId;
   }

   /**
    * Sets the value of the <code>mnoUserId</code> property.
    *
    * @param mnoUserId the value for the <code>mnoUserId</code> property
    *    
		    */

   public void setMnoUserId(Long mnoUserId) {
      this.mnoUserId = mnoUserId;
   }

   /**
    * Returns the value of the <code>mnoId</code> property.
    *
    */
      @Column(name = "SERVICE_OP_ID" , nullable = false )
   public Long getMnoId() {
      return mnoId;
   }

   /**
    * Sets the value of the <code>mnoId</code> property.
    *
    * @param mnoId the value for the <code>mnoId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setMnoId(Long mnoId) {
      this.mnoId = mnoId;
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
    * Returns the value of the <code>supplierId</code> property.
    *
    */
      @Column(name = "SUPPLIER_ID"  )
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getMnoUserId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&mnoUserId=" + getMnoUserId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "mnoUserId";
			return primaryKeyFieldName;				
    }       
}
