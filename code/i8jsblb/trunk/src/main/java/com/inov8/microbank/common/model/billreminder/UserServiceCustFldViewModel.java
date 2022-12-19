package com.inov8.microbank.common.model.billreminder;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The UserServiceCustFldViewModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="UserServiceCustFldViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "USER_SERVICE_CUST_FLD_VIEW")
public class UserServiceCustFldViewModel extends BasePersistableModel implements Serializable {
  



   private Long userServiceId;
   private String userServiceName;
   private Long appUserId;
   private Long productId;
   private String mobileNo;
   private Boolean active;
   private Long customFieldId1;
   private String customFieldValue1;
   private Long fieldLength1;
   private String keyName1;
   private String name1;
   private String description1;
   private String comments1;
   private Long customFieldId2;
   private String customFieldValue2;
   private Long fieldLength2;
   private String keyName2;
   private String name2;
   private String description2;
   private String comments2;
   private Long customFieldId3;
   private String customFieldValue3;
   private Long fieldLength3;
   private String keyName3;
   private String name3;
   private String description3;
   private String comments3;
   private Long customFieldId4;
   private String customFieldValue4;
   private Long fieldLength4;
   private String keyName4;
   private String name4;
   private String description4;
   private String comments4;
   private Long customFieldId5;
   private String customFieldValue5;
   private Long fieldLength5;
   private String keyName5;
   private String name5;
   private String description5;
   private String comments5;
   private Long customFieldId6;
   private String customFieldValue6;
   private Long fieldLength6;
   private String keyName6;
   private String name6;
   private String description6;
   private String comments6;
   private Long customFieldId7;
   private String customFieldValue7;
   private Long fieldLength7;
   private String keyName7;
   private String name7;
   private String description7;
   private String comments7;
   private Long customFieldId8;
   private String customFieldValue8;
   private Long fieldLength8;
   private String keyName8;
   private String name8;
   private String description8;
   private String comments8;
   private Long customFieldId9;
   private String customFieldValue9;
   private Long fieldLength9;
   private String keyName9;
   private String name9;
   private String description9;
   private String comments9;
   private Long customFieldId10;
   private String customFieldValue10;
   private Long fieldLength10;
   private String keyName10;
   private String name10;
   private String description10;
   private String comments10;

   /**
    * Default constructor.
    */
   public UserServiceCustFldViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getUserServiceId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setUserServiceId(primaryKey);
    }

   /**
    * Returns the value of the <code>userServiceId</code> property.
    *
    */
      @Column(name = "USER_SERVICE_ID" , nullable = false )
   @Id 
   public Long getUserServiceId() {
      return userServiceId;
   }

   /**
    * Sets the value of the <code>userServiceId</code> property.
    *
    * @param userServiceId the value for the <code>userServiceId</code> property
    *    
		    */

   public void setUserServiceId(Long userServiceId) {
      this.userServiceId = userServiceId;
   }

   /**
    * Returns the value of the <code>userServiceName</code> property.
    *
    */
      @Column(name = "USER_SERVICE_NAME" , nullable = false , length=50 )
   public String getUserServiceName() {
      return userServiceName;
   }

   /**
    * Sets the value of the <code>userServiceName</code> property.
    *
    * @param userServiceName the value for the <code>userServiceName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setUserServiceName(String userServiceName) {
      this.userServiceName = userServiceName;
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
    * Returns the value of the <code>productId</code> property.
    *
    */
      @Column(name = "PRODUCT_ID" , nullable = false )
   public Long getProductId() {
      return productId;
   }

   /**
    * Sets the value of the <code>productId</code> property.
    *
    * @param productId the value for the <code>productId</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setProductId(Long productId) {
      this.productId = productId;
   }

   /**
    * Returns the value of the <code>active</code> property.
    *
    */
      @Column(name = "IS_ACTIVE" , nullable = false )
   public Boolean getActive() {
      return active;
   }

   /**
    * Sets the value of the <code>active</code> property.
    *
    * @param active the value for the <code>active</code> property
    *    
		    */

   public void setActive(Boolean active) {
      this.active = active;
   }

   /**
    * Returns the value of the <code>customFieldId1</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_ID_1" , nullable = false )
   public Long getCustomFieldId1() {
      return customFieldId1;
   }

   /**
    * Sets the value of the <code>customFieldId1</code> property.
    *
    * @param customFieldId1 the value for the <code>customFieldId1</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCustomFieldId1(Long customFieldId1) {
      this.customFieldId1 = customFieldId1;
   }

   /**
    * Returns the value of the <code>customFieldValue1</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_VALUE_1" , nullable = false , length=250 )
   public String getCustomFieldValue1() {
      return customFieldValue1;
   }

   /**
    * Sets the value of the <code>customFieldValue1</code> property.
    *
    * @param customFieldValue1 the value for the <code>customFieldValue1</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomFieldValue1(String customFieldValue1) {
      this.customFieldValue1 = customFieldValue1;
   }

   /**
    * Returns the value of the <code>fieldLength1</code> property.
    *
    */
      @Column(name = "field_length_1" , nullable = false )
   public Long getFieldLength1() {
      return fieldLength1;
   }

   /**
    * Sets the value of the <code>fieldLength1</code> property.
    *
    * @param fieldLength1 the value for the <code>fieldLength1</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFieldLength1(Long fieldLength1) {
      this.fieldLength1 = fieldLength1;
   }

   /**
    * Returns the value of the <code>keyName1</code> property.
    *
    */
      @Column(name = "key_name_1" , nullable = false , length=50 )
   public String getKeyName1() {
      return keyName1;
   }

   /**
    * Sets the value of the <code>keyName1</code> property.
    *
    * @param keyName1 the value for the <code>keyName1</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setKeyName1(String keyName1) {
      this.keyName1 = keyName1;
   }

   /**
    * Returns the value of the <code>name1</code> property.
    *
    */
      @Column(name = "NAME_1" , nullable = false , length=50 )
   public String getName1() {
      return name1;
   }

   /**
    * Sets the value of the <code>name1</code> property.
    *
    * @param name1 the value for the <code>name1</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setName1(String name1) {
      this.name1 = name1;
   }

   /**
    * Returns the value of the <code>description1</code> property.
    *
    */
      @Column(name = "description_1"  , length=250 )
   public String getDescription1() {
      return description1;
   }

   /**
    * Sets the value of the <code>description1</code> property.
    *
    * @param description1 the value for the <code>description1</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription1(String description1) {
      this.description1 = description1;
   }

   /**
    * Returns the value of the <code>comments1</code> property.
    *
    */
      @Column(name = "comments_1"  , length=250 )
   public String getComments1() {
      return comments1;
   }

   /**
    * Sets the value of the <code>comments1</code> property.
    *
    * @param comments1 the value for the <code>comments1</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments1(String comments1) {
      this.comments1 = comments1;
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
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setMobileNo(String mobileNo) {
      this.mobileNo = mobileNo;
   }

   /**
    * Returns the value of the <code>customFieldId2</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_ID_2"  )
   public Long getCustomFieldId2() {
      return customFieldId2;
   }

   /**
    * Sets the value of the <code>customFieldId2</code> property.
    *
    * @param customFieldId2 the value for the <code>customFieldId2</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCustomFieldId2(Long customFieldId2) {
      this.customFieldId2 = customFieldId2;
   }

   /**
    * Returns the value of the <code>customFieldValue2</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_VALUE_2"  , length=250 )
   public String getCustomFieldValue2() {
      return customFieldValue2;
   }

   /**
    * Sets the value of the <code>customFieldValue2</code> property.
    *
    * @param customFieldValue2 the value for the <code>customFieldValue2</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomFieldValue2(String customFieldValue2) {
      this.customFieldValue2 = customFieldValue2;
   }

   /**
    * Returns the value of the <code>fieldLength2</code> property.
    *
    */
      @Column(name = "field_length_2" , nullable = false )
   public Long getFieldLength2() {
      return fieldLength2;
   }

   /**
    * Sets the value of the <code>fieldLength2</code> property.
    *
    * @param fieldLength2 the value for the <code>fieldLength2</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFieldLength2(Long fieldLength2) {
      this.fieldLength2 = fieldLength2;
   }

   /**
    * Returns the value of the <code>keyName2</code> property.
    *
    */
      @Column(name = "key_name_2" , nullable = false , length=50 )
   public String getKeyName2() {
      return keyName2;
   }

   /**
    * Sets the value of the <code>keyName2</code> property.
    *
    * @param keyName2 the value for the <code>keyName2</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setKeyName2(String keyName2) {
      this.keyName2 = keyName2;
   }

   /**
    * Returns the value of the <code>name2</code> property.
    *
    */
      @Column(name = "NAME_2" , nullable = false , length=50 )
   public String getName2() {
      return name2;
   }

   /**
    * Sets the value of the <code>name2</code> property.
    *
    * @param name2 the value for the <code>name2</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setName2(String name2) {
      this.name2 = name2;
   }

   /**
    * Returns the value of the <code>description2</code> property.
    *
    */
      @Column(name = "description_2"  , length=250 )
   public String getDescription2() {
      return description2;
   }

   /**
    * Sets the value of the <code>description2</code> property.
    *
    * @param description2 the value for the <code>description2</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription2(String description2) {
      this.description2 = description2;
   }

   /**
    * Returns the value of the <code>comments2</code> property.
    *
    */
      @Column(name = "comments_2"  , length=250 )
   public String getComments2() {
      return comments2;
   }

   /**
    * Sets the value of the <code>comments2</code> property.
    *
    * @param comments2 the value for the <code>comments2</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments2(String comments2) {
      this.comments2 = comments2;
   }

   /**
    * Returns the value of the <code>customFieldId3</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_ID_3"  )
   public Long getCustomFieldId3() {
      return customFieldId3;
   }

   /**
    * Sets the value of the <code>customFieldId3</code> property.
    *
    * @param customFieldId3 the value for the <code>customFieldId3</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCustomFieldId3(Long customFieldId3) {
      this.customFieldId3 = customFieldId3;
   }

   /**
    * Returns the value of the <code>customFieldValue3</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_VALUE_3"  , length=250 )
   public String getCustomFieldValue3() {
      return customFieldValue3;
   }

   /**
    * Sets the value of the <code>customFieldValue3</code> property.
    *
    * @param customFieldValue3 the value for the <code>customFieldValue3</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomFieldValue3(String customFieldValue3) {
      this.customFieldValue3 = customFieldValue3;
   }

   /**
    * Returns the value of the <code>fieldLength3</code> property.
    *
    */
      @Column(name = "field_length_3" , nullable = false )
   public Long getFieldLength3() {
      return fieldLength3;
   }

   /**
    * Sets the value of the <code>fieldLength3</code> property.
    *
    * @param fieldLength3 the value for the <code>fieldLength3</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFieldLength3(Long fieldLength3) {
      this.fieldLength3 = fieldLength3;
   }

   /**
    * Returns the value of the <code>keyName3</code> property.
    *
    */
      @Column(name = "key_name_3" , nullable = false , length=50 )
   public String getKeyName3() {
      return keyName3;
   }

   /**
    * Sets the value of the <code>keyName3</code> property.
    *
    * @param keyName3 the value for the <code>keyName3</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setKeyName3(String keyName3) {
      this.keyName3 = keyName3;
   }

   /**
    * Returns the value of the <code>name3</code> property.
    *
    */
      @Column(name = "NAME_3" , nullable = false , length=50 )
   public String getName3() {
      return name3;
   }

   /**
    * Sets the value of the <code>name3</code> property.
    *
    * @param name3 the value for the <code>name3</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setName3(String name3) {
      this.name3 = name3;
   }

   /**
    * Returns the value of the <code>description3</code> property.
    *
    */
      @Column(name = "description_3"  , length=250 )
   public String getDescription3() {
      return description3;
   }

   /**
    * Sets the value of the <code>description3</code> property.
    *
    * @param description3 the value for the <code>description3</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription3(String description3) {
      this.description3 = description3;
   }

   /**
    * Returns the value of the <code>comments3</code> property.
    *
    */
      @Column(name = "comments_3"  , length=250 )
   public String getComments3() {
      return comments3;
   }

   /**
    * Sets the value of the <code>comments3</code> property.
    *
    * @param comments3 the value for the <code>comments3</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments3(String comments3) {
      this.comments3 = comments3;
   }

   /**
    * Returns the value of the <code>customFieldId4</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_ID_4"  )
   public Long getCustomFieldId4() {
      return customFieldId4;
   }

   /**
    * Sets the value of the <code>customFieldId4</code> property.
    *
    * @param customFieldId4 the value for the <code>customFieldId4</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCustomFieldId4(Long customFieldId4) {
      this.customFieldId4 = customFieldId4;
   }

   /**
    * Returns the value of the <code>customFieldValue4</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_VALUE_4"  , length=250 )
   public String getCustomFieldValue4() {
      return customFieldValue4;
   }

   /**
    * Sets the value of the <code>customFieldValue4</code> property.
    *
    * @param customFieldValue4 the value for the <code>customFieldValue4</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomFieldValue4(String customFieldValue4) {
      this.customFieldValue4 = customFieldValue4;
   }

   /**
    * Returns the value of the <code>fieldLength4</code> property.
    *
    */
      @Column(name = "field_length_4" , nullable = false )
   public Long getFieldLength4() {
      return fieldLength4;
   }

   /**
    * Sets the value of the <code>fieldLength4</code> property.
    *
    * @param fieldLength4 the value for the <code>fieldLength4</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFieldLength4(Long fieldLength4) {
      this.fieldLength4 = fieldLength4;
   }

   /**
    * Returns the value of the <code>keyName4</code> property.
    *
    */
      @Column(name = "key_name_4" , nullable = false , length=50 )
   public String getKeyName4() {
      return keyName4;
   }

   /**
    * Sets the value of the <code>keyName4</code> property.
    *
    * @param keyName4 the value for the <code>keyName4</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setKeyName4(String keyName4) {
      this.keyName4 = keyName4;
   }

   /**
    * Returns the value of the <code>name4</code> property.
    *
    */
      @Column(name = "NAME_4" , nullable = false , length=50 )
   public String getName4() {
      return name4;
   }

   /**
    * Sets the value of the <code>name4</code> property.
    *
    * @param name4 the value for the <code>name4</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setName4(String name4) {
      this.name4 = name4;
   }

   /**
    * Returns the value of the <code>description4</code> property.
    *
    */
      @Column(name = "description_4"  , length=250 )
   public String getDescription4() {
      return description4;
   }

   /**
    * Sets the value of the <code>description4</code> property.
    *
    * @param description4 the value for the <code>description4</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription4(String description4) {
      this.description4 = description4;
   }

   /**
    * Returns the value of the <code>comments4</code> property.
    *
    */
      @Column(name = "comments_4"  , length=250 )
   public String getComments4() {
      return comments4;
   }

   /**
    * Sets the value of the <code>comments4</code> property.
    *
    * @param comments4 the value for the <code>comments4</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments4(String comments4) {
      this.comments4 = comments4;
   }

   /**
    * Returns the value of the <code>customFieldId5</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_ID_5"  )
   public Long getCustomFieldId5() {
      return customFieldId5;
   }

   /**
    * Sets the value of the <code>customFieldId5</code> property.
    *
    * @param customFieldId5 the value for the <code>customFieldId5</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCustomFieldId5(Long customFieldId5) {
      this.customFieldId5 = customFieldId5;
   }

   /**
    * Returns the value of the <code>customFieldValue5</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_VALUE_5"  , length=250 )
   public String getCustomFieldValue5() {
      return customFieldValue5;
   }

   /**
    * Sets the value of the <code>customFieldValue5</code> property.
    *
    * @param customFieldValue5 the value for the <code>customFieldValue5</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomFieldValue5(String customFieldValue5) {
      this.customFieldValue5 = customFieldValue5;
   }

   /**
    * Returns the value of the <code>fieldLength5</code> property.
    *
    */
      @Column(name = "field_length_5" , nullable = false )
   public Long getFieldLength5() {
      return fieldLength5;
   }

   /**
    * Sets the value of the <code>fieldLength5</code> property.
    *
    * @param fieldLength5 the value for the <code>fieldLength5</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFieldLength5(Long fieldLength5) {
      this.fieldLength5 = fieldLength5;
   }

   /**
    * Returns the value of the <code>keyName5</code> property.
    *
    */
      @Column(name = "key_name_5" , nullable = false , length=50 )
   public String getKeyName5() {
      return keyName5;
   }

   /**
    * Sets the value of the <code>keyName5</code> property.
    *
    * @param keyName5 the value for the <code>keyName5</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setKeyName5(String keyName5) {
      this.keyName5 = keyName5;
   }

   /**
    * Returns the value of the <code>name5</code> property.
    *
    */
      @Column(name = "NAME_5" , nullable = false , length=50 )
   public String getName5() {
      return name5;
   }

   /**
    * Sets the value of the <code>name5</code> property.
    *
    * @param name5 the value for the <code>name5</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setName5(String name5) {
      this.name5 = name5;
   }

   /**
    * Returns the value of the <code>description5</code> property.
    *
    */
      @Column(name = "description_5"  , length=250 )
   public String getDescription5() {
      return description5;
   }

   /**
    * Sets the value of the <code>description5</code> property.
    *
    * @param description5 the value for the <code>description5</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription5(String description5) {
      this.description5 = description5;
   }

   /**
    * Returns the value of the <code>comments5</code> property.
    *
    */
      @Column(name = "comments_5"  , length=250 )
   public String getComments5() {
      return comments5;
   }

   /**
    * Sets the value of the <code>comments5</code> property.
    *
    * @param comments5 the value for the <code>comments5</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments5(String comments5) {
      this.comments5 = comments5;
   }

   /**
    * Returns the value of the <code>customFieldId6</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_ID_6"  )
   public Long getCustomFieldId6() {
      return customFieldId6;
   }

   /**
    * Sets the value of the <code>customFieldId6</code> property.
    *
    * @param customFieldId6 the value for the <code>customFieldId6</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCustomFieldId6(Long customFieldId6) {
      this.customFieldId6 = customFieldId6;
   }

   /**
    * Returns the value of the <code>customFieldValue6</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_VALUE_6"  , length=250 )
   public String getCustomFieldValue6() {
      return customFieldValue6;
   }

   /**
    * Sets the value of the <code>customFieldValue6</code> property.
    *
    * @param customFieldValue6 the value for the <code>customFieldValue6</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomFieldValue6(String customFieldValue6) {
      this.customFieldValue6 = customFieldValue6;
   }

   /**
    * Returns the value of the <code>fieldLength6</code> property.
    *
    */
      @Column(name = "field_length_6" , nullable = false )
   public Long getFieldLength6() {
      return fieldLength6;
   }

   /**
    * Sets the value of the <code>fieldLength6</code> property.
    *
    * @param fieldLength6 the value for the <code>fieldLength6</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFieldLength6(Long fieldLength6) {
      this.fieldLength6 = fieldLength6;
   }

   /**
    * Returns the value of the <code>keyName6</code> property.
    *
    */
      @Column(name = "key_name_6" , nullable = false , length=50 )
   public String getKeyName6() {
      return keyName6;
   }

   /**
    * Sets the value of the <code>keyName6</code> property.
    *
    * @param keyName6 the value for the <code>keyName6</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setKeyName6(String keyName6) {
      this.keyName6 = keyName6;
   }

   /**
    * Returns the value of the <code>name6</code> property.
    *
    */
      @Column(name = "NAME_6" , nullable = false , length=50 )
   public String getName6() {
      return name6;
   }

   /**
    * Sets the value of the <code>name6</code> property.
    *
    * @param name6 the value for the <code>name6</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setName6(String name6) {
      this.name6 = name6;
   }

   /**
    * Returns the value of the <code>description6</code> property.
    *
    */
      @Column(name = "description_6"  , length=250 )
   public String getDescription6() {
      return description6;
   }

   /**
    * Sets the value of the <code>description6</code> property.
    *
    * @param description6 the value for the <code>description6</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription6(String description6) {
      this.description6 = description6;
   }

   /**
    * Returns the value of the <code>comments6</code> property.
    *
    */
      @Column(name = "comments_6"  , length=250 )
   public String getComments6() {
      return comments6;
   }

   /**
    * Sets the value of the <code>comments6</code> property.
    *
    * @param comments6 the value for the <code>comments6</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments6(String comments6) {
      this.comments6 = comments6;
   }

   /**
    * Returns the value of the <code>customFieldId7</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_ID_7"  )
   public Long getCustomFieldId7() {
      return customFieldId7;
   }

   /**
    * Sets the value of the <code>customFieldId7</code> property.
    *
    * @param customFieldId7 the value for the <code>customFieldId7</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCustomFieldId7(Long customFieldId7) {
      this.customFieldId7 = customFieldId7;
   }

   /**
    * Returns the value of the <code>customFieldValue7</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_VALUE_7"  , length=250 )
   public String getCustomFieldValue7() {
      return customFieldValue7;
   }

   /**
    * Sets the value of the <code>customFieldValue7</code> property.
    *
    * @param customFieldValue7 the value for the <code>customFieldValue7</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomFieldValue7(String customFieldValue7) {
      this.customFieldValue7 = customFieldValue7;
   }

   /**
    * Returns the value of the <code>fieldLength7</code> property.
    *
    */
      @Column(name = "field_length_7" , nullable = false )
   public Long getFieldLength7() {
      return fieldLength7;
   }

   /**
    * Sets the value of the <code>fieldLength7</code> property.
    *
    * @param fieldLength7 the value for the <code>fieldLength7</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFieldLength7(Long fieldLength7) {
      this.fieldLength7 = fieldLength7;
   }

   /**
    * Returns the value of the <code>keyName7</code> property.
    *
    */
      @Column(name = "key_name_7" , nullable = false , length=50 )
   public String getKeyName7() {
      return keyName7;
   }

   /**
    * Sets the value of the <code>keyName7</code> property.
    *
    * @param keyName7 the value for the <code>keyName7</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setKeyName7(String keyName7) {
      this.keyName7 = keyName7;
   }

   /**
    * Returns the value of the <code>name7</code> property.
    *
    */
      @Column(name = "NAME_7" , nullable = false , length=50 )
   public String getName7() {
      return name7;
   }

   /**
    * Sets the value of the <code>name7</code> property.
    *
    * @param name7 the value for the <code>name7</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setName7(String name7) {
      this.name7 = name7;
   }

   /**
    * Returns the value of the <code>description7</code> property.
    *
    */
      @Column(name = "description_7"  , length=250 )
   public String getDescription7() {
      return description7;
   }

   /**
    * Sets the value of the <code>description7</code> property.
    *
    * @param description7 the value for the <code>description7</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription7(String description7) {
      this.description7 = description7;
   }

   /**
    * Returns the value of the <code>comments7</code> property.
    *
    */
      @Column(name = "comments_7"  , length=250 )
   public String getComments7() {
      return comments7;
   }

   /**
    * Sets the value of the <code>comments7</code> property.
    *
    * @param comments7 the value for the <code>comments7</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments7(String comments7) {
      this.comments7 = comments7;
   }

   /**
    * Returns the value of the <code>customFieldId8</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_ID_8"  )
   public Long getCustomFieldId8() {
      return customFieldId8;
   }

   /**
    * Sets the value of the <code>customFieldId8</code> property.
    *
    * @param customFieldId8 the value for the <code>customFieldId8</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCustomFieldId8(Long customFieldId8) {
      this.customFieldId8 = customFieldId8;
   }

   /**
    * Returns the value of the <code>customFieldValue8</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_VALUE_8"  , length=250 )
   public String getCustomFieldValue8() {
      return customFieldValue8;
   }

   /**
    * Sets the value of the <code>customFieldValue8</code> property.
    *
    * @param customFieldValue8 the value for the <code>customFieldValue8</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomFieldValue8(String customFieldValue8) {
      this.customFieldValue8 = customFieldValue8;
   }

   /**
    * Returns the value of the <code>fieldLength8</code> property.
    *
    */
      @Column(name = "field_length_8" , nullable = false )
   public Long getFieldLength8() {
      return fieldLength8;
   }

   /**
    * Sets the value of the <code>fieldLength8</code> property.
    *
    * @param fieldLength8 the value for the <code>fieldLength8</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFieldLength8(Long fieldLength8) {
      this.fieldLength8 = fieldLength8;
   }

   /**
    * Returns the value of the <code>keyName8</code> property.
    *
    */
      @Column(name = "key_name_8" , nullable = false , length=50 )
   public String getKeyName8() {
      return keyName8;
   }

   /**
    * Sets the value of the <code>keyName8</code> property.
    *
    * @param keyName8 the value for the <code>keyName8</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setKeyName8(String keyName8) {
      this.keyName8 = keyName8;
   }

   /**
    * Returns the value of the <code>name8</code> property.
    *
    */
      @Column(name = "NAME_8" , nullable = false , length=50 )
   public String getName8() {
      return name8;
   }

   /**
    * Sets the value of the <code>name8</code> property.
    *
    * @param name8 the value for the <code>name8</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setName8(String name8) {
      this.name8 = name8;
   }

   /**
    * Returns the value of the <code>description8</code> property.
    *
    */
      @Column(name = "description_8"  , length=250 )
   public String getDescription8() {
      return description8;
   }

   /**
    * Sets the value of the <code>description8</code> property.
    *
    * @param description8 the value for the <code>description8</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription8(String description8) {
      this.description8 = description8;
   }

   /**
    * Returns the value of the <code>comments8</code> property.
    *
    */
      @Column(name = "comments_8"  , length=250 )
   public String getComments8() {
      return comments8;
   }

   /**
    * Sets the value of the <code>comments8</code> property.
    *
    * @param comments8 the value for the <code>comments8</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments8(String comments8) {
      this.comments8 = comments8;
   }

   /**
    * Returns the value of the <code>customFieldId9</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_ID_9"  )
   public Long getCustomFieldId9() {
      return customFieldId9;
   }

   /**
    * Sets the value of the <code>customFieldId9</code> property.
    *
    * @param customFieldId9 the value for the <code>customFieldId9</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCustomFieldId9(Long customFieldId9) {
      this.customFieldId9 = customFieldId9;
   }

   /**
    * Returns the value of the <code>customFieldValue9</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_VALUE_9"  , length=250 )
   public String getCustomFieldValue9() {
      return customFieldValue9;
   }

   /**
    * Sets the value of the <code>customFieldValue9</code> property.
    *
    * @param customFieldValue9 the value for the <code>customFieldValue9</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomFieldValue9(String customFieldValue9) {
      this.customFieldValue9 = customFieldValue9;
   }

   /**
    * Returns the value of the <code>fieldLength9</code> property.
    *
    */
      @Column(name = "field_length_9" , nullable = false )
   public Long getFieldLength9() {
      return fieldLength9;
   }

   /**
    * Sets the value of the <code>fieldLength9</code> property.
    *
    * @param fieldLength9 the value for the <code>fieldLength9</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFieldLength9(Long fieldLength9) {
      this.fieldLength9 = fieldLength9;
   }

   /**
    * Returns the value of the <code>keyName9</code> property.
    *
    */
      @Column(name = "key_name_9" , nullable = false , length=50 )
   public String getKeyName9() {
      return keyName9;
   }

   /**
    * Sets the value of the <code>keyName9</code> property.
    *
    * @param keyName9 the value for the <code>keyName9</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setKeyName9(String keyName9) {
      this.keyName9 = keyName9;
   }

   /**
    * Returns the value of the <code>name9</code> property.
    *
    */
      @Column(name = "NAME_9" , nullable = false , length=50 )
   public String getName9() {
      return name9;
   }

   /**
    * Sets the value of the <code>name9</code> property.
    *
    * @param name9 the value for the <code>name9</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setName9(String name9) {
      this.name9 = name9;
   }

   /**
    * Returns the value of the <code>description9</code> property.
    *
    */
      @Column(name = "description_9"  , length=250 )
   public String getDescription9() {
      return description9;
   }

   /**
    * Sets the value of the <code>description9</code> property.
    *
    * @param description9 the value for the <code>description9</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription9(String description9) {
      this.description9 = description9;
   }

   /**
    * Returns the value of the <code>comments9</code> property.
    *
    */
      @Column(name = "comments_9"  , length=250 )
   public String getComments9() {
      return comments9;
   }

   /**
    * Sets the value of the <code>comments9</code> property.
    *
    * @param comments9 the value for the <code>comments9</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments9(String comments9) {
      this.comments9 = comments9;
   }

   /**
    * Returns the value of the <code>customFieldId10</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_ID_10"  )
   public Long getCustomFieldId10() {
      return customFieldId10;
   }

   /**
    * Sets the value of the <code>customFieldId10</code> property.
    *
    * @param customFieldId10 the value for the <code>customFieldId10</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setCustomFieldId10(Long customFieldId10) {
      this.customFieldId10 = customFieldId10;
   }

   /**
    * Returns the value of the <code>customFieldValue10</code> property.
    *
    */
      @Column(name = "CUSTOM_FIELD_VALUE_10"  , length=250 )
   public String getCustomFieldValue10() {
      return customFieldValue10;
   }

   /**
    * Sets the value of the <code>customFieldValue10</code> property.
    *
    * @param customFieldValue10 the value for the <code>customFieldValue10</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setCustomFieldValue10(String customFieldValue10) {
      this.customFieldValue10 = customFieldValue10;
   }

   /**
    * Returns the value of the <code>fieldLength10</code> property.
    *
    */
      @Column(name = "field_length_10" , nullable = false )
   public Long getFieldLength10() {
      return fieldLength10;
   }

   /**
    * Sets the value of the <code>fieldLength10</code> property.
    *
    * @param fieldLength10 the value for the <code>fieldLength10</code> property
    *    
		    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setFieldLength10(Long fieldLength10) {
      this.fieldLength10 = fieldLength10;
   }

   /**
    * Returns the value of the <code>keyName10</code> property.
    *
    */
      @Column(name = "key_name_10" , nullable = false , length=50 )
   public String getKeyName10() {
      return keyName10;
   }

   /**
    * Sets the value of the <code>keyName10</code> property.
    *
    * @param keyName10 the value for the <code>keyName10</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setKeyName10(String keyName10) {
      this.keyName10 = keyName10;
   }

   /**
    * Returns the value of the <code>name10</code> property.
    *
    */
      @Column(name = "NAME_10" , nullable = false , length=50 )
   public String getName10() {
      return name10;
   }

   /**
    * Sets the value of the <code>name10</code> property.
    *
    * @param name10 the value for the <code>name10</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setName10(String name10) {
      this.name10 = name10;
   }

   /**
    * Returns the value of the <code>description10</code> property.
    *
    */
      @Column(name = "description_10"  , length=250 )
   public String getDescription10() {
      return description10;
   }

   /**
    * Sets the value of the <code>description10</code> property.
    *
    * @param description10 the value for the <code>description10</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setDescription10(String description10) {
      this.description10 = description10;
   }

   /**
    * Returns the value of the <code>comments10</code> property.
    *
    */
      @Column(name = "comments_10"  , length=250 )
   public String getComments10() {
      return comments10;
   }

   /**
    * Sets the value of the <code>comments10</code> property.
    *
    * @param comments10 the value for the <code>comments10</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    */

   public void setComments10(String comments10) {
      this.comments10 = comments10;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getUserServiceId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&userServiceId=" + getUserServiceId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "userServiceId";
			return primaryKeyFieldName;				
    }       
}
