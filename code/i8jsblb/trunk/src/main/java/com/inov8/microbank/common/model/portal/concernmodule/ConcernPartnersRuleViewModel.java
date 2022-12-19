package com.inov8.microbank.common.model.portal.concernmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The ConcernPartnersRuleViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="ConcernPartnersRuleViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "CONCERN_PARTNERS_RULE_VIEW")
public class ConcernPartnersRuleViewModel extends BasePersistableModel {
  



   private Long concernPartnerAsociationId;
   private Long partnerId;
   private String partnerName;
   private Long associatedPartnerId;
   private String associatedPartnerName;
   private Boolean associated;
   private Boolean partnerActive;
   private Long bankId;
   private Long concernPartnerId;
   private Long mnoId;
   private Long operatorId;
   private Long supplierId;
   private Long concernPartnerTypeId;
   private String concernPartnerTypeName;

   /**
    * Default constructor.
    */
   public ConcernPartnersRuleViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getConcernPartnerAsociationId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setConcernPartnerAsociationId(primaryKey);
    }

   /**
    * Returns the value of the <code>concernPartnerAsociationId</code> property.
    *
    */
      @Column(name = "CONCERN_PARTNER_ASOCIATION_ID" , nullable = false )
   @Id 
   public Long getConcernPartnerAsociationId() {
      return concernPartnerAsociationId;
   }

   /**
    * Sets the value of the <code>concernPartnerAsociationId</code> property.
    *
    * @param concernPartnerAsociationId the value for the <code>concernPartnerAsociationId</code> property
    *    
		    */

   public void setConcernPartnerAsociationId(Long concernPartnerAsociationId) {
      this.concernPartnerAsociationId = concernPartnerAsociationId;
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
    * Returns the value of the <code>partnerName</code> property.
    *
    */
      @Column(name = "PARTNER_NAME" , nullable = false , length=50 )
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
    * Returns the value of the <code>associatedPartnerId</code> property.
    *
    */
      @Column(name = "ASSOCIATED_PARTNER_ID" , nullable = false )
   public Long getAssociatedPartnerId() {
      return associatedPartnerId;
   }

   /**
    * Sets the value of the <code>associatedPartnerId</code> property.
    *
    * @param associatedPartnerId the value for the <code>associatedPartnerId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setAssociatedPartnerId(Long associatedPartnerId) {
      this.associatedPartnerId = associatedPartnerId;
   }

   /**
    * Returns the value of the <code>associatedPartnerName</code> property.
    *
    */
      @Column(name = "ASSOCIATED_PARTNER_NAME"  , length=50 )
   public String getAssociatedPartnerName() {
      return associatedPartnerName;
   }

   /**
    * Sets the value of the <code>associatedPartnerName</code> property.
    *
    * @param associatedPartnerName the value for the <code>associatedPartnerName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setAssociatedPartnerName(String associatedPartnerName) {
      this.associatedPartnerName = associatedPartnerName;
   }

   /**
    * Returns the value of the <code>isAssociated</code> property.
    *
    */
      @Column(name = "IS_ASSOCIATED" , nullable = false )
   public Boolean getAssociated() {
      return associated;
   }

   /**
    * Sets the value of the <code>isAssociated</code> property.
    *
    * @param isAssociated the value for the <code>isAssociated</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setAssociated(Boolean associated) {
      this.associated = associated;
   }

   /**
    * Returns the value of the <code>partnerActive</code> property.
    *
    */
      @Column(name = "IS_PARTNER_ACTIVE" , nullable = false )
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
    * Returns the value of the <code>bankId</code> property.
    *
    */
      @Column(name = "BANK_ID"  )
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
    * Returns the value of the <code>concernPartnerId</code> property.
    *
    */
      @Column(name = "CONCERN_PARTNER_ID" , nullable = false )
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
    * Returns the value of the <code>mnoId</code> property.
    *
    */
      @Column(name = "SERVICE_OP_ID"  )
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
    * Returns the value of the <code>operatorId</code> property.
    *
    */
      @Column(name = "OPERATOR_ID"  )
   public Long getOperatorId() {
      return operatorId;
   }

   /**
    * Sets the value of the <code>operatorId</code> property.
    *
    * @param operatorId the value for the <code>operatorId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setOperatorId(Long operatorId) {
      this.operatorId = operatorId;
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
    * Returns the value of the <code>concernPartnerTypeId</code> property.
    *
    */
      @Column(name = "CONCERN_PARTNER_TYPE_ID" , nullable = false )
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
      @Column(name = "CONCERN_PARTNER_TYPE_NAME" , nullable = false , length=50 )
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
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getConcernPartnerAsociationId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&concernPartnerAsociationId=" + getConcernPartnerAsociationId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "concernPartnerAsociationId";
			return primaryKeyFieldName;				
    }       
}
