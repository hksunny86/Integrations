package com.inov8.microbank.common.model.portal.issuemodule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The TransIssueHistoryListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="TransIssueHistoryListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "TRANS_ISSUE_HISTORY_LIST_VIEW")
public class TransIssueHistoryListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 5447825323789010944L;
   private Double transactionId;
   private String mfsId;
   private String transactionCode;
   private Date transDate;
   private Date transUpdateOn;
   private Long issueId;
   private String issueCode;
   private Date issueUpdateDate;
   private Date issueCreatedDate;
   private String comments;
   private Long issueHistoryId;
   private Date issueHistoryDate;
   private String statusName;
   private Long issueTypeStatusId;

   /**
    * Default constructor.
    */
   public TransIssueHistoryListViewModel() {
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
    * Returns the value of the <code>transactionId</code> property.
    *
    */
      @Column(name = "TRANSACTION_ID"  )
   public Double getTransactionId() {
      return transactionId;
   }

   /**
    * Sets the value of the <code>transactionId</code> property.
    *
    * @param transactionId the value for the <code>transactionId</code> property
    *    
		    * @spring.validator type="double"
    * @spring.validator type="doubleRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="99999999999.9999"
    */

   public void setTransactionId(Double transactionId) {
      this.transactionId = transactionId;
   }

   /**
    * Returns the value of the <code>mfsId</code> property.
    *
    */
      @Column(name = "MFS_ID"  , length=50 )
   public String getMfsId() {
      return mfsId;
   }

   /**
    * Sets the value of the <code>mfsId</code> property.
    *
    * @param mfsId the value for the <code>mfsId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMfsId(String mfsId) {
      this.mfsId = mfsId;
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
    * Returns the value of the <code>transDate</code> property.
    *
    */
      @Column(name = "TRANS_DATE"  )
   public Date getTransDate() {
      return transDate;
   }

   /**
    * Sets the value of the <code>transDate</code> property.
    *
    * @param transDate the value for the <code>transDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setTransDate(Date transDate) {
      this.transDate = transDate;
   }

   /**
    * Returns the value of the <code>transUpdateOn</code> property.
    *
    */
      @Column(name = "TRANS_UPDATE_ON"  )
   public Date getTransUpdateOn() {
      return transUpdateOn;
   }

   /**
    * Sets the value of the <code>transUpdateOn</code> property.
    *
    * @param transUpdateOn the value for the <code>transUpdateOn</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setTransUpdateOn(Date transUpdateOn) {
      this.transUpdateOn = transUpdateOn;
   }

   /**
    * Returns the value of the <code>issueId</code> property.
    *
    */
      @Column(name = "ISSUE_ID"  )
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
    * Returns the value of the <code>issueCode</code> property.
    *
    */
      @Column(name = "ISSUE_CODE"  , length=50 )
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
    * Returns the value of the <code>issueUpdateDate</code> property.
    *
    */
      @Column(name = "ISSUE_UPDATE_DATE"  )
   public Date getIssueUpdateDate() {
      return issueUpdateDate;
   }

   /**
    * Sets the value of the <code>issueUpdateDate</code> property.
    *
    * @param issueUpdateDate the value for the <code>issueUpdateDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setIssueUpdateDate(Date issueUpdateDate) {
      this.issueUpdateDate = issueUpdateDate;
   }

   /**
    * Returns the value of the <code>issueCreatedDate</code> property.
    *
    */
      @Column(name = "ISSUE_CREATED_DATE"  )
   public Date getIssueCreatedDate() {
      return issueCreatedDate;
   }

   /**
    * Sets the value of the <code>issueCreatedDate</code> property.
    *
    * @param issueCreatedDate the value for the <code>issueCreatedDate</code> property
    *    
		    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setIssueCreatedDate(Date issueCreatedDate) {
      this.issueCreatedDate = issueCreatedDate;
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
    * Returns the value of the <code>issueHistoryId</code> property.
    *
    */
      @Column(name = "ISSUE_HISTORY_ID"  )
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
    * Returns the value of the <code>issueHistoryDate</code> property.
    *
    */
      @Column(name = "ISSUE_HISTORY_DATE"  )
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
    * Returns the value of the <code>statusName</code> property.
    *
    */
      @Column(name = "STATUS_NAME"  , length=50 )
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
    * Returns the value of the <code>issueTypeStatusId</code> property.
    *
    */
      @Column(name = "ISSUE_TYPE_STATUS_ID"  )
   public Long getIssueTypeStatusId() {
      return issueTypeStatusId;
   }

   /**
    * Sets the value of the <code>issueTypeStatusId</code> property.
    *
    * @param issueTypeStatusId the value for the <code>issueTypeStatusId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setIssueTypeStatusId(Long issueTypeStatusId) {
      this.issueTypeStatusId = issueTypeStatusId;
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
