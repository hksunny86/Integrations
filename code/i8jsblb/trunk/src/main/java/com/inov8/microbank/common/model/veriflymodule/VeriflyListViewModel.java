package com.inov8.microbank.common.model.veriflymodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The VeriflyListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="VeriflyListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "VERIFLY_LIST_VIEW")
public class VeriflyListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 5681239104340446699L;
private Long appUserBankUserId;
   private Long bankUserId;
   private Long bankVeriflyId;
   private Long bankId;
   private String veriflyUrl;
   private Long veriflyId;

   /**
    * Default constructor.
    */
   public VeriflyListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getBankUserId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setBankUserId(primaryKey);
    }

   /**
    * Returns the value of the <code>appUserBankUserId</code> property.
    *
    */
      @Column(name = "APP_USER_BANK_USER_ID"  )
   public Long getAppUserBankUserId() {
      return appUserBankUserId;
   }

   /**
    * Sets the value of the <code>appUserBankUserId</code> property.
    *
    * @param appUserBankUserId the value for the <code>appUserBankUserId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setAppUserBankUserId(Long appUserBankUserId) {
      this.appUserBankUserId = appUserBankUserId;
   }

   /**
    * Returns the value of the <code>bankUserId</code> property.
    *
    */
      @Column(name = "BANK_USER_ID" , nullable = false )
   @Id 
   public Long getBankUserId() {
      return bankUserId;
   }

   /**
    * Sets the value of the <code>bankUserId</code> property.
    *
    * @param bankUserId the value for the <code>bankUserId</code> property
    *    
		    */

   public void setBankUserId(Long bankUserId) {
      this.bankUserId = bankUserId;
   }

   /**
    * Returns the value of the <code>bankVeriflyId</code> property.
    *
    */
      @Column(name = "BANK_VERIFLY_ID" , nullable = false )
   public Long getBankVeriflyId() {
      return bankVeriflyId;
   }

   /**
    * Sets the value of the <code>bankVeriflyId</code> property.
    *
    * @param bankVeriflyId the value for the <code>bankVeriflyId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setBankVeriflyId(Long bankVeriflyId) {
      this.bankVeriflyId = bankVeriflyId;
   }

   /**
    * Returns the value of the <code>bankId</code> property.
    *
    */
      @Column(name = "BANK_ID" , nullable = false )
   public Long getBankId() {
      return bankId;
   }

   /**
    * Sets the value of the <code>bankId</code> property.
    *
    * @param bankId the value for the <code>bankId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setBankId(Long bankId) {
      this.bankId = bankId;
   }

   /**
    * Returns the value of the <code>veriflyUrl</code> property.
    *
    */
      @Column(name = "VERIFLY_URL" , nullable = false , length=250 )
   public String getVeriflyUrl() {
      return veriflyUrl;
   }

   /**
    * Sets the value of the <code>veriflyUrl</code> property.
    *
    * @param veriflyUrl the value for the <code>veriflyUrl</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setVeriflyUrl(String veriflyUrl) {
      this.veriflyUrl = veriflyUrl;
   }

   /**
    * Returns the value of the <code>veriflyId</code> property.
    *
    */
      @Column(name = "VERIFLY_ID" , nullable = false )
   public Long getVeriflyId() {
      return veriflyId;
   }

   /**
    * Sets the value of the <code>veriflyId</code> property.
    *
    * @param veriflyId the value for the <code>veriflyId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setVeriflyId(Long veriflyId) {
      this.veriflyId = veriflyId;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getBankUserId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&bankUserId=" + getBankUserId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "bankUserId";
			return primaryKeyFieldName;				
    }       
}
