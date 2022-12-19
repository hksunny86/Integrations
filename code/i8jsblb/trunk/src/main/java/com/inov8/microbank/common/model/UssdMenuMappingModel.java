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
 * The UssdMenuMappingModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="UssdMenuMappingModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "USSD_MENU_MAPPING_seq",sequenceName = "USSD_MENU_MAPPING_seq", allocationSize=1)
@Table(name = "USSD_MENU_MAPPING")
public class UssdMenuMappingModel extends BasePersistableModel implements Serializable {
  

   private AppUserTypeModel appUserTypeIdAppUserTypeModel;


   private Long ussdMenuMappingId;
   private Long screenCodeInput;
   private Long screenCodeOutput;
   private String options;

   /**
    * Default constructor.
    */
   public UssdMenuMappingModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getUssdMenuMappingId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setUssdMenuMappingId(primaryKey);
    }

   /**
    * Returns the value of the <code>ussdMenuMappingId</code> property.
    *
    */
      @Column(name = "USSD_MENU_MAPPING_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USSD_MENU_MAPPING_seq")
   public Long getUssdMenuMappingId() {
      return ussdMenuMappingId;
   }

   /**
    * Sets the value of the <code>ussdMenuMappingId</code> property.
    *
    * @param ussdMenuMappingId the value for the <code>ussdMenuMappingId</code> property
    *    
		    */

   public void setUssdMenuMappingId(Long ussdMenuMappingId) {
      this.ussdMenuMappingId = ussdMenuMappingId;
   }

   /**
    * Returns the value of the <code>screenCodeInput</code> property.
    *
    */
      @Column(name = "SCREEN_CODE_INPUT" , nullable = false )
   public Long getScreenCodeInput() {
      return screenCodeInput;
   }

   /**
    * Sets the value of the <code>screenCodeInput</code> property.
    *
    * @param screenCodeInput the value for the <code>screenCodeInput</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setScreenCodeInput(Long screenCodeInput) {
      this.screenCodeInput = screenCodeInput;
   }

   /**
    * Returns the value of the <code>screenCodeOutput</code> property.
    *
    */
      @Column(name = "SCREEN_CODE_OUTPUT" , nullable = false )
   public Long getScreenCodeOutput() {
      return screenCodeOutput;
   }

   /**
    * Sets the value of the <code>screenCodeOutput</code> property.
    *
    * @param screenCodeOutput the value for the <code>screenCodeOutput</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setScreenCodeOutput(Long screenCodeOutput) {
      this.screenCodeOutput = screenCodeOutput;
   }

   /**
    * Returns the value of the <code>options</code> property.
    *
    */
      @Column(name = "OPTIONS" , nullable = false , length=100 )
   public String getOptions() {
      return options;
   }

   /**
    * Sets the value of the <code>options</code> property.
    *
    * @param options the value for the <code>options</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="100"
    */

   public void setOptions(String options) {
      this.options = options;
   }

   /**
    * Returns the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @return the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "APP_USER_TYPE_ID")    
   public AppUserTypeModel getRelationAppUserTypeIdAppUserTypeModel(){
      return appUserTypeIdAppUserTypeModel;
   }
    
   /**
    * Returns the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @return the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AppUserTypeModel getAppUserTypeIdAppUserTypeModel(){
      return getRelationAppUserTypeIdAppUserTypeModel();
   }

   /**
    * Sets the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @param appUserTypeModel a value for <code>appUserTypeIdAppUserTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationAppUserTypeIdAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
      this.appUserTypeIdAppUserTypeModel = appUserTypeModel;
   }
   
   /**
    * Sets the value of the <code>appUserTypeIdAppUserTypeModel</code> relation property.
    *
    * @param appUserTypeModel a value for <code>appUserTypeIdAppUserTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setAppUserTypeIdAppUserTypeModel(AppUserTypeModel appUserTypeModel) {
      if(null != appUserTypeModel)
      {
      	setRelationAppUserTypeIdAppUserTypeModel((AppUserTypeModel)appUserTypeModel.clone());
      }      
   }
   



   /**
    * Returns the value of the <code>appUserTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getAppUserTypeId() {
      if (appUserTypeIdAppUserTypeModel != null) {
         return appUserTypeIdAppUserTypeModel.getAppUserTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserTypeId</code> property.
    *
    * @param appUserTypeId the value for the <code>appUserTypeId</code> property
													    */
   
   @javax.persistence.Transient
   public void setAppUserTypeId(Long appUserTypeId) {
      if(appUserTypeId == null)
      {      
      	appUserTypeIdAppUserTypeModel = null;
      }
      else
      {
        appUserTypeIdAppUserTypeModel = new AppUserTypeModel();
      	appUserTypeIdAppUserTypeModel.setAppUserTypeId(appUserTypeId);
      }      
   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getUssdMenuMappingId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&ussdMenuMappingId=" + getUssdMenuMappingId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "ussdMenuMappingId";
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
    	
    	associationModel.setClassName("AppUserTypeModel");
    	associationModel.setPropertyName("relationAppUserTypeIdAppUserTypeModel");   		
   		associationModel.setValue(getRelationAppUserTypeIdAppUserTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
