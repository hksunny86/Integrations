package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The UserStateModel entity bean.
 *
 * @author  Fahad Tariq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="UserStateModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@org.hibernate.annotations.GenericGenerator(name="USER_STATE_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="USER_STATE_seq") } )
//@javax.persistence.SequenceGenerator(name = "USER_STATE_seq",sequenceName = "USER_STATE_seq")
@Table(name = "USER_STATE")
public class UserStateModel extends BasePersistableModel implements Serializable {
  



   private Long userStateId;
   private String msisdn;
   private java.sql.Blob state;
   private Date creationDate;
   private Date accessDate;

   /**
    * Default constructor.
    */
   public UserStateModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getUserStateId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setUserStateId(primaryKey);
    }

   /**
    * Returns the value of the <code>userStateId</code> property.
    *
    */
      @Column(name = "USER_STATE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_STATE_seq")
   public Long getUserStateId() {
      return userStateId;
   }

   /**
    * Sets the value of the <code>userStateId</code> property.
    *
    * @param userStateId the value for the <code>userStateId</code> property
    *    
		    */

   public void setUserStateId(Long userStateId) {
      this.userStateId = userStateId;
   }

   /**
    * Returns the value of the <code>msisdn</code> property.
    *
    */
      @Column(name = "MSISDN" , nullable = false , length=14 )
   public String getMsisdn() {
      return msisdn;
   }

   /**
    * Sets the value of the <code>msisdn</code> property.
    *
    * @param msisdn the value for the <code>msisdn</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="14"
    */

   public void setMsisdn(String msisdn) {
      this.msisdn = msisdn;
   }

   /**
    * Returns the value of the <code>state</code> property.
    *
    */
      @Column(name = "STATE" , nullable = false )
   public java.sql.Blob getState() {
      return state;
   }

   /**
    * Sets the value of the <code>state</code> property.
    *
    * @param state the value for the <code>state</code> property
    *    
		    * @spring.validator type="required"
    */

   public void setState(java.sql.Blob state) {
      this.state = state;
   }

   /**
    * Returns the value of the <code>creationDate</code> property.
    *
    */
      @Column(name = "CREATION_DATE" , nullable = false )
   public Date getCreationDate() {
      return creationDate;
   }

   /**
    * Sets the value of the <code>creationDate</code> property.
    *
    * @param creationDate the value for the <code>creationDate</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setCreationDate(Date creationDate) {
      this.creationDate = creationDate;
   }

   /**
    * Returns the value of the <code>accessDate</code> property.
    *
    */
      @Column(name = "ACCESS_DATE" , nullable = false )
   public Date getAccessDate() {
      return accessDate;
   }

   /**
    * Sets the value of the <code>accessDate</code> property.
    *
    * @param accessDate the value for the <code>accessDate</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="date"
    * @spring.validator-var name="datePattern" value="${date_format}"
    */

   public void setAccessDate(Date accessDate) {
      this.accessDate = accessDate;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getUserStateId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&userStateId=" + getUserStateId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "userStateId";
			return primaryKeyFieldName;				
    }
    
    /**
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
    	
    	    	
    	return associationModelList;
    }    
          
}
