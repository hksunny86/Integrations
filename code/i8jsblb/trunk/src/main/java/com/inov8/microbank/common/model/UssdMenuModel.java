package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The UssdMenuModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="UssdMenuModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "USSD_MENU_seq",sequenceName = "USSD_MENU_seq", allocationSize=1)
@Table(name = "USSD_MENU")
public class UssdMenuModel extends BasePersistableModel implements Serializable {
  

   private CustomInputTypeModel customInputTypeIdCustomInputTypeModel;


   private Long ussdMenuId;
   private Long screenCode;
   private String menuString;
   private String commandCode;
   private String inputRequired;
   private String executionRequired;
   private String validInputs;

   /**
    * Default constructor.
    */
   public UssdMenuModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getUssdMenuId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setUssdMenuId(primaryKey);
    }

   /**
    * Returns the value of the <code>ussdMenuId</code> property.
    *
    */
      @Column(name = "USSD_MENU_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USSD_MENU_seq")
   public Long getUssdMenuId() {
      return ussdMenuId;
   }

   /**
    * Sets the value of the <code>ussdMenuId</code> property.
    *
    * @param ussdMenuId the value for the <code>ussdMenuId</code> property
    *    
		    */

   public void setUssdMenuId(Long ussdMenuId) {
      this.ussdMenuId = ussdMenuId;
   }

   /**
    * Returns the value of the <code>screenCode</code> property.
    *
    */
      @Column(name = "SCREEN_CODE" , nullable = false )
   public Long getScreenCode() {
      return screenCode;
   }

   /**
    * Sets the value of the <code>screenCode</code> property.
    *
    * @param screenCode the value for the <code>screenCode</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setScreenCode(Long screenCode) {
      this.screenCode = screenCode;
   }

   /**
    * Returns the value of the <code>menuString</code> property.
    *
    */
      @Column(name = "MENU_STRING" , nullable = false , length=182 )
   public String getMenuString() {
      return menuString;
   }

   /**
    * Sets the value of the <code>menuString</code> property.
    *
    * @param menuString the value for the <code>menuString</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="182"
    */

   public void setMenuString(String menuString) {
      this.menuString = menuString;
   }

   /**
    * Returns the value of the <code>commandCode</code> property.
    *
    */
      @Column(name = "COMMAND_CODE"  , length=16 )
   public String getCommandCode() {
      return commandCode;
   }

   /**
    * Sets the value of the <code>commandCode</code> property.
    *
    * @param commandCode the value for the <code>commandCode</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="16"
    */

   public void setCommandCode(String commandCode) {
      this.commandCode = commandCode;
   }

   /**
    * Returns the value of the <code>inputRequired</code> property.
    *
    */
      @Column(name = "INPUT_REQUIRED" , nullable = false , length=1 )
   public String getInputRequired() {
      return inputRequired;
   }

   /**
    * Sets the value of the <code>inputRequired</code> property.
    *
    * @param inputRequired the value for the <code>inputRequired</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="1"
    */

   public void setInputRequired(String inputRequired) {
      this.inputRequired = inputRequired;
   }

   /**
    * Returns the value of the <code>executionRequired</code> property.
    *
    */
      @Column(name = "EXECUTION_REQUIRED"  , length=1 )
   public String getExecutionRequired() {
      return executionRequired;
   }

   /**
    * Sets the value of the <code>executionRequired</code> property.
    *
    * @param executionRequired the value for the <code>executionRequired</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="1"
    */

   public void setExecutionRequired(String executionRequired) {
      this.executionRequired = executionRequired;
   }

   /**
    * Returns the value of the <code>validInputs</code> property.
    *
    */
      @Column(name = "VALID_INPUTS"  , length=32 )
   public String getValidInputs() {
      return validInputs;
   }

   /**
    * Sets the value of the <code>validInputs</code> property.
    *
    * @param validInputs the value for the <code>validInputs</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="32"
    */

   public void setValidInputs(String validInputs) {
      this.validInputs = validInputs;
   }

   /**
    * Returns the value of the <code>customInputTypeIdCustomInputTypeModel</code> relation property.
    *
    * @return the value of the <code>customInputTypeIdCustomInputTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CUSTOM_INPUT_TYPE_ID")    
   public CustomInputTypeModel getRelationCustomInputTypeIdCustomInputTypeModel(){
      return customInputTypeIdCustomInputTypeModel;
   }
    
   /**
    * Returns the value of the <code>customInputTypeIdCustomInputTypeModel</code> relation property.
    *
    * @return the value of the <code>customInputTypeIdCustomInputTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CustomInputTypeModel getCustomInputTypeIdCustomInputTypeModel(){
      return getRelationCustomInputTypeIdCustomInputTypeModel();
   }

   /**
    * Sets the value of the <code>customInputTypeIdCustomInputTypeModel</code> relation property.
    *
    * @param customInputTypeModel a value for <code>customInputTypeIdCustomInputTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCustomInputTypeIdCustomInputTypeModel(CustomInputTypeModel customInputTypeModel) {
      this.customInputTypeIdCustomInputTypeModel = customInputTypeModel;
   }
   
   /**
    * Sets the value of the <code>customInputTypeIdCustomInputTypeModel</code> relation property.
    *
    * @param customInputTypeModel a value for <code>customInputTypeIdCustomInputTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setCustomInputTypeIdCustomInputTypeModel(CustomInputTypeModel customInputTypeModel) {
      if(null != customInputTypeModel)
      {
      	setRelationCustomInputTypeIdCustomInputTypeModel((CustomInputTypeModel)customInputTypeModel.clone());
      }      
   }
   



   /**
    * Returns the value of the <code>customInputTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCustomInputTypeId() {
      if (customInputTypeIdCustomInputTypeModel != null) {
         return customInputTypeIdCustomInputTypeModel.getCustomInputTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>customInputTypeId</code> property.
    *
    * @param customInputTypeId the value for the <code>customInputTypeId</code> property
																			    */
   
   @javax.persistence.Transient
   public void setCustomInputTypeId(Long customInputTypeId) {
      if(customInputTypeId == null)
      {      
      	customInputTypeIdCustomInputTypeModel = null;
      }
      else
      {
        customInputTypeIdCustomInputTypeModel = new CustomInputTypeModel();
      	customInputTypeIdCustomInputTypeModel.setCustomInputTypeId(customInputTypeId);
      }      
   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getUssdMenuId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&ussdMenuId=" + getUssdMenuId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "ussdMenuId";
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
    	
    	      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CustomInputTypeModel");
    	associationModel.setPropertyName("relationCustomInputTypeIdCustomInputTypeModel");   		
   		associationModel.setValue(getRelationCustomInputTypeIdCustomInputTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
