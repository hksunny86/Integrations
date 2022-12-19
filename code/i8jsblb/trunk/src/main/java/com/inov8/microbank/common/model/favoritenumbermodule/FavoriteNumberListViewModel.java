package com.inov8.microbank.common.model.favoritenumbermodule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The FavoriteNumberListViewModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="FavoriteNumberListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "FAVORITE_NUMBER_LIST_VIEW")
public class FavoriteNumberListViewModel extends BasePersistableModel {
  



   private Long favoriteNumbersId;
   private String name;
   private String favoriteNumber;
   private String favoriteType;
   private Long appUserId;
   private String mobileNo;
   private String userId;
   private Long sequenceNumber;

   /**
    * Default constructor.
    */
   public FavoriteNumberListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getFavoriteNumbersId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setFavoriteNumbersId(primaryKey);
    }

   /**
    * Returns the value of the <code>favoriteNumbersId</code> property.
    *
    */
      @Column(name = "FAVORITE_NUMBERS_ID" , nullable = false )
   @Id 
   public Long getFavoriteNumbersId() {
      return favoriteNumbersId;
   }

   /**
    * Sets the value of the <code>favoriteNumbersId</code> property.
    *
    * @param favoriteNumbersId the value for the <code>favoriteNumbersId</code> property
    *    
		    */

   public void setFavoriteNumbersId(Long favoriteNumbersId) {
      this.favoriteNumbersId = favoriteNumbersId;
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
    * Returns the value of the <code>favoriteNumber</code> property.
    *
    */
      @Column(name = "FAVORITE_NUMBER" , nullable = false , length=50 )
   public String getFavoriteNumber() {
      return favoriteNumber;
   }

   /**
    * Sets the value of the <code>favoriteNumber</code> property.
    *
    * @param favoriteNumber the value for the <code>favoriteNumber</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setFavoriteNumber(String favoriteNumber) {
      this.favoriteNumber = favoriteNumber;
   }

   /**
    * Returns the value of the <code>favoriteType</code> property.
    *
    */
      @Column(name = "FAVORITE_TYPE" , nullable = false , length=50 )
   public String getFavoriteType() {
      return favoriteType;
   }

   /**
    * Sets the value of the <code>favoriteType</code> property.
    *
    * @param favoriteType the value for the <code>favoriteType</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setFavoriteType(String favoriteType) {
      this.favoriteType = favoriteType;
   }

   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
      @Column(name = "APP_USER_ID" , nullable = false )
   public Long getAppUserId() {
      return appUserId;
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setAppUserId(Long appUserId) {
      this.appUserId = appUserId;
   }

   /**
    * Returns the value of the <code>mobileNo</code> property.
    *
    */
      @Column(name = "MOBILE_NO" , nullable = false , length=50 )
   public String getMobileNo() {
      return mobileNo;
   }

   /**
    * Sets the value of the <code>mobileNo</code> property.
    *
    * @param mobileNo the value for the <code>mobileNo</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMobileNo(String mobileNo) {
      this.mobileNo = mobileNo;
   }

   /**
    * Returns the value of the <code>userId</code> property.
    *
    */
      @Column(name = "USER_ID"  , length=50 )
   public String getUserId() {
      return userId;
   }

   /**
    * Sets the value of the <code>userId</code> property.
    *
    * @param userId the value for the <code>userId</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setUserId(String userId) {
      this.userId = userId;
   }

   /**
    * Returns the value of the <code>sequenceNumber</code> property.
    *
    */
      @Column(name = "SEQUENCE_NUMBER" , nullable = false )
   public Long getSequenceNumber() {
      return sequenceNumber;
   }

   /**
    * Sets the value of the <code>sequenceNumber</code> property.
    *
    * @param sequenceNumber the value for the <code>sequenceNumber</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setSequenceNumber(Long sequenceNumber) {
      this.sequenceNumber = sequenceNumber;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getFavoriteNumbersId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&favoriteNumbersId=" + getFavoriteNumbersId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "favoriteNumbersId";
			return primaryKeyFieldName;				
    }       
}
