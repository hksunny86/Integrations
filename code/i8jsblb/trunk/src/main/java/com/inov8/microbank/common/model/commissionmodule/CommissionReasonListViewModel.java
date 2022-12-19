package com.inov8.microbank.common.model.commissionmodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CommissionReasonListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CommissionReasonListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "COMMISSION_REASON_LIST_VIEW")
public class CommissionReasonListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = -2379661486911990483L;
private Long commissionReasonId;
   private String name;
   private String reasonDesc;
   private String comments;

   /**
    * Default constructor.
    */
   public CommissionReasonListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCommissionReasonId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCommissionReasonId(primaryKey);
    }

   /**
    * Returns the value of the <code>commissionReasonId</code> property.
    *
    */
      @Column(name = "COMMISSION_REASON_ID" , nullable = false )
   @Id 
   public Long getCommissionReasonId() {
      return commissionReasonId;
   }

   /**
    * Sets the value of the <code>commissionReasonId</code> property.
    *
    * @param commissionReasonId the value for the <code>commissionReasonId</code> property
    *    
		    */

   public void setCommissionReasonId(Long commissionReasonId) {
      this.commissionReasonId = commissionReasonId;
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
    * Returns the value of the <code>reasonDesc</code> property.
    *
    */
      @Column(name = "REASON_DESC"  , length=250 )
   public String getReasonDesc() {
      return reasonDesc;
   }

   /**
    * Sets the value of the <code>reasonDesc</code> property.
    *
    * @param reasonDesc the value for the <code>reasonDesc</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setReasonDesc(String reasonDesc) {
      this.reasonDesc = reasonDesc;
   }

   /**
    * Returns the value of the <code>comments</code> property.
    *
    */
      @Column(name = "COMMENTS"  , length=250 )
   public String getComments() {
      return comments;
   }

   /**
    * Sets the value of the <code>comments</code> property.
    *
    * @param comments the value for the <code>comments</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments(String comments) {
      this.comments = comments;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCommissionReasonId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&commissionReasonId=" + getCommissionReasonId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "commissionReasonId";
			return primaryKeyFieldName;				
    }       
}
