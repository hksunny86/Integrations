package com.inov8.microbank.common.model.switchmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The SwitchFinderListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="SwitchFinderListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "SWITCH_FINDER_LIST_VIEW")
public class SwitchFinderListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 130822340387936229L;
private Long switchFinderId;
   private String bankName;
   private String switchName;
   private String paymentModeName;

   /**
    * Default constructor.
    */
   public SwitchFinderListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getSwitchFinderId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setSwitchFinderId(primaryKey);
    }

   /**
    * Returns the value of the <code>switchFinderId</code> property.
    *
    */
      @Column(name = "SWITCH_FINDER_ID" , nullable = false )
   @Id 
   public Long getSwitchFinderId() {
      return switchFinderId;
   }

   /**
    * Sets the value of the <code>switchFinderId</code> property.
    *
    * @param switchFinderId the value for the <code>switchFinderId</code> property
    *    
		    */

   public void setSwitchFinderId(Long switchFinderId) {
      this.switchFinderId = switchFinderId;
   }

   /**
    * Returns the value of the <code>bankName</code> property.
    *
    */
      @Column(name = "BANK_NAME" , nullable = false , length=50 )
   public String getBankName() {
      return bankName;
   }

   /**
    * Sets the value of the <code>bankName</code> property.
    *
    * @param bankName the value for the <code>bankName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setBankName(String bankName) {
      this.bankName = bankName;
   }

   /**
    * Returns the value of the <code>switchName</code> property.
    *
    */
      @Column(name = "SWITCH_NAME" , nullable = false , length=50 )
   public String getSwitchName() {
      return switchName;
   }

   /**
    * Sets the value of the <code>switchName</code> property.
    *
    * @param switchName the value for the <code>switchName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setSwitchName(String switchName) {
      this.switchName = switchName;
   }

   /**
    * Returns the value of the <code>paymentModeName</code> property.
    *
    */
      @Column(name = "PAYMENT_MODE_NAME" , nullable = false , length=50 )
   public String getPaymentModeName() {
      return paymentModeName;
   }

   /**
    * Sets the value of the <code>paymentModeName</code> property.
    *
    * @param paymentModeName the value for the <code>paymentModeName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setPaymentModeName(String paymentModeName) {
      this.paymentModeName = paymentModeName;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getSwitchFinderId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&switchFinderId=" + getSwitchFinderId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "switchFinderId";
			return primaryKeyFieldName;				
    }       
}
