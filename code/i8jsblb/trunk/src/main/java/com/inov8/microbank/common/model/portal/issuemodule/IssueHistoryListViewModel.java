package com.inov8.microbank.common.model.portal.issuemodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The IssueHistoryListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="IssueHistoryListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "ISSUE_HISTORY_LIST_VIEW")
public class IssueHistoryListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 3469015912458665560L;
private Long issueId;
   private Date issueDate;
   private String issueCode;
   private String custTransCode;
   private String transactionCode;
   private String comments;
   private Date issueHistoryDate;
   private Long issueHistoryId;
   private String statusName;
   private Long toIssueTypeStatusId;

   /**
    * Default constructor.
    */
   public IssueHistoryListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getIssueHistoryId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setIssueHistoryId(primaryKey);
    }

   /**
    * Returns the value of the <code>issueId</code> property.
    *
    */
      @Column(name = "ISSUE_ID" , nullable = false )
   public Long getIssueId() {
      return issueId;
   }

   /**
    * Sets the value of the <code>issueId</code> property.
    *
    * @param issueId the value for the <code>issueId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setIssueId(Long issueId) {
      this.issueId = issueId;
   }

   /**
    * Returns the value of the <code>issueDate</code> property.
    *
    */
      @Column(name = "ISSUE_DATE" , nullable = false )
   public Date getIssueDate() {
      return issueDate;
   }

   /**
    * Sets the value of the <code>issueDate</code> property.
    *
    * @param issueDate the value for the <code>issueDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setIssueDate(Date issueDate) {
      this.issueDate = issueDate;
   }

   /**
    * Returns the value of the <code>issueCode</code> property.
    *
    */
      @Column(name = "ISSUE_CODE" , nullable = false , length=50 )
   public String getIssueCode() {
      return issueCode;
   }

   /**
    * Sets the value of the <code>issueCode</code> property.
    *
    * @param issueCode the value for the <code>issueCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setIssueCode(String issueCode) {
      this.issueCode = issueCode;
   }

   /**
    * Returns the value of the <code>custTransCode</code> property.
    *
    */
      @Column(name = "CUST_TRANS_CODE"  , length=50 )
   public String getCustTransCode() {
      return custTransCode;
   }

   /**
    * Sets the value of the <code>custTransCode</code> property.
    *
    * @param custTransCode the value for the <code>custTransCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCustTransCode(String custTransCode) {
      this.custTransCode = custTransCode;
   }

   /**
    * Returns the value of the <code>transactionCode</code> property.
    *
    */
      @Column(name = "TRANSACTION_CODE"  , length=50 )
   public String getTransactionCode() {
      return transactionCode;
   }

   /**
    * Sets the value of the <code>transactionCode</code> property.
    *
    * @param transactionCode the value for the <code>transactionCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setTransactionCode(String transactionCode) {
      this.transactionCode = transactionCode;
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
    * Returns the value of the <code>issueHistoryDate</code> property.
    *
    */
      @Column(name = "ISSUE_HISTORY_DATE" , nullable = false )
   public Date getIssueHistoryDate() {
      return issueHistoryDate;
   }

   /**
    * Sets the value of the <code>issueHistoryDate</code> property.
    *
    * @param issueHistoryDate the value for the <code>issueHistoryDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setIssueHistoryDate(Date issueHistoryDate) {
      this.issueHistoryDate = issueHistoryDate;
   }

   /**
    * Returns the value of the <code>issueHistoryId</code> property.
    *
    */
      @Column(name = "ISSUE_HISTORY_ID" , nullable = false )
   @Id 
   public Long getIssueHistoryId() {
      return issueHistoryId;
   }

   /**
    * Sets the value of the <code>issueHistoryId</code> property.
    *
    * @param issueHistoryId the value for the <code>issueHistoryId</code> property
    *    
		    */

   public void setIssueHistoryId(Long issueHistoryId) {
      this.issueHistoryId = issueHistoryId;
   }

   /**
    * Returns the value of the <code>statusName</code> property.
    *
    */
      @Column(name = "STATUS_NAME" , nullable = false , length=50 )
   public String getStatusName() {
      return statusName;
   }

   /**
    * Sets the value of the <code>statusName</code> property.
    *
    * @param statusName the value for the <code>statusName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setStatusName(String statusName) {
      this.statusName = statusName;
   }

   /**
    * Returns the value of the <code>toIssueTypeStatusId</code> property.
    *
    */
      @Column(name = "TO_ISSUE_TYPE_STATUS_ID"  )
   public Long getToIssueTypeStatusId() {
      return toIssueTypeStatusId;
   }

   /**
    * Sets the value of the <code>toIssueTypeStatusId</code> property.
    *
    * @param toIssueTypeStatusId the value for the <code>toIssueTypeStatusId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setToIssueTypeStatusId(Long toIssueTypeStatusId) {
      this.toIssueTypeStatusId = toIssueTypeStatusId;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getIssueHistoryId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&issueHistoryId=" + getIssueHistoryId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "issueHistoryId";
			return primaryKeyFieldName;				
    }       
}
