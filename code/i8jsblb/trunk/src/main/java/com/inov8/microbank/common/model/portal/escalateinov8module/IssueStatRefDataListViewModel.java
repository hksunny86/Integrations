package com.inov8.microbank.common.model.portal.escalateinov8module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The IssueStatRefDataListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="IssueStatRefDataListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "ISSUE_STAT_REF_DATA_LIST_VIEW")
public class IssueStatRefDataListViewModel extends BasePersistableModel {
  



   /**
	 * 
	 */
	private static final long serialVersionUID = 8496427880618778771L;
private Long issueTypeStatusId;
   private String issueStatusName;
   private String description;
   private String issueTypeName;
   private Long issueTypeId;

   /**
    * Default constructor.
    */
   public IssueStatRefDataListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getIssueTypeStatusId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setIssueTypeStatusId(primaryKey);
    }

   /**
    * Returns the value of the <code>issueTypeStatusId</code> property.
    *
    */
      @Column(name = "ISSUE_TYPE_STATUS_ID" , nullable = false )
   @Id 
   public Long getIssueTypeStatusId() {
      return issueTypeStatusId;
   }

   /**
    * Sets the value of the <code>issueTypeStatusId</code> property.
    *
    * @param issueTypeStatusId the value for the <code>issueTypeStatusId</code> property
    *    
		    */

   public void setIssueTypeStatusId(Long issueTypeStatusId) {
      this.issueTypeStatusId = issueTypeStatusId;
   }

   /**
    * Returns the value of the <code>issueStatusName</code> property.
    *
    */
      @Column(name = "ISSUE_STATUS_NAME" , nullable = false , length=50 )
   public String getIssueStatusName() {
      return issueStatusName;
   }

   /**
    * Sets the value of the <code>issueStatusName</code> property.
    *
    * @param issueStatusName the value for the <code>issueStatusName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setIssueStatusName(String issueStatusName) {
      this.issueStatusName = issueStatusName;
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
    * Returns the value of the <code>issueTypeName</code> property.
    *
    */
      @Column(name = "ISSUE_TYPE_NAME" , nullable = false , length=50 )
   public String getIssueTypeName() {
      return issueTypeName;
   }

   /**
    * Sets the value of the <code>issueTypeName</code> property.
    *
    * @param issueTypeName the value for the <code>issueTypeName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setIssueTypeName(String issueTypeName) {
      this.issueTypeName = issueTypeName;
   }

   /**
    * Returns the value of the <code>issueTypeId</code> property.
    *
    */
      @Column(name = "ISSUE_TYPE_ID" , nullable = false )
   public Long getIssueTypeId() {
      return issueTypeId;
   }

   /**
    * Sets the value of the <code>issueTypeId</code> property.
    *
    * @param issueTypeId the value for the <code>issueTypeId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setIssueTypeId(Long issueTypeId) {
      this.issueTypeId = issueTypeId;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getIssueTypeStatusId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&issueTypeStatusId=" + getIssueTypeStatusId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "issueTypeStatusId";
			return primaryKeyFieldName;				
    }       
}
