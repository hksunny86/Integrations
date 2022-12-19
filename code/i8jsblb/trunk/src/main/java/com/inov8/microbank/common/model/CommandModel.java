package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CommandModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CommandModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COMMAND_seq",sequenceName = "COMMAND_seq", allocationSize=1)
@Table(name = "COMMAND")
public class CommandModel extends BasePersistableModel implements Serializable{
  


   /**
	 * 
	 */
	private static final long serialVersionUID = 526830051989114190L;
private Collection<ActionLogModel> commandIdActionLogModelList = new ArrayList<ActionLogModel>();
   private Collection<DeviceTypeCommandModel> commandIdDeviceTypeCommandModelList = new ArrayList<DeviceTypeCommandModel>();

   private Long commandId;
   private String name;
   private String className;
   private String description;
   private Boolean active;

   /**
    * Default constructor.
    */
   public CommandModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getCommandId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setCommandId(primaryKey);
    }

   /**
    * Returns the value of the <code>commandId</code> property.
    *
    */
      @Column(name = "COMMAND_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMMAND_seq")
   public Long getCommandId() {
      return commandId;
   }

   /**
    * Sets the value of the <code>commandId</code> property.
    *
    * @param commandId the value for the <code>commandId</code> property
    *    
		    */

   public void setCommandId(Long commandId) {
      this.commandId = commandId;
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
		    * @spring.validator type="required"
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
    * Returns the value of the <code>className</code> property.
    *
    */
      @Column(name = "CLASS_NAME" , nullable = false , length=250 )
   public String getClassName() {
      return className;
   }

   /**
    * Sets the value of the <code>className</code> property.
    *
    * @param className the value for the <code>className</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="250"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setClassName(String className) {
      this.className = className;
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
    * Add the related ActionLogModel to this one-to-many relation.
    *
    * @param actionLogModel object to be added.
    */
    
   public void addCommandIdActionLogModel(ActionLogModel actionLogModel) {
      actionLogModel.setRelationCommandIdCommandModel(this);
      commandIdActionLogModelList.add(actionLogModel);
   }
   
   /**
    * Remove the related ActionLogModel to this one-to-many relation.
    *
    * @param actionLogModel object to be removed.
    */
   
   public void removeCommandIdActionLogModel(ActionLogModel actionLogModel) {      
      actionLogModel.setRelationCommandIdCommandModel(null);
      commandIdActionLogModelList.remove(actionLogModel);      
   }

   /**
    * Get a list of related ActionLogModel objects of the CommandModel object.
    * These objects are in a bidirectional one-to-many relation by the CommandId member.
    *
    * @return Collection of ActionLogModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCommandIdCommandModel")
   @JoinColumn(name = "COMMAND_ID")
   public Collection<ActionLogModel> getCommandIdActionLogModelList() throws Exception {
   		return commandIdActionLogModelList;
   }


   /**
    * Set a list of ActionLogModel related objects to the CommandModel object.
    * These objects are in a bidirectional one-to-many relation by the CommandId member.
    *
    * @param actionLogModelList the list of related objects.
    */
    public void setCommandIdActionLogModelList(Collection<ActionLogModel> actionLogModelList) throws Exception {
		this.commandIdActionLogModelList = actionLogModelList;
   }


   /**
    * Add the related DeviceTypeCommandModel to this one-to-many relation.
    *
    * @param deviceTypeCommandModel object to be added.
    */
    
   public void addCommandIdDeviceTypeCommandModel(DeviceTypeCommandModel deviceTypeCommandModel) {
      deviceTypeCommandModel.setRelationCommandIdCommandModel(this);
      commandIdDeviceTypeCommandModelList.add(deviceTypeCommandModel);
   }
   
   /**
    * Remove the related DeviceTypeCommandModel to this one-to-many relation.
    *
    * @param deviceTypeCommandModel object to be removed.
    */
   
   public void removeCommandIdDeviceTypeCommandModel(DeviceTypeCommandModel deviceTypeCommandModel) {      
      deviceTypeCommandModel.setRelationCommandIdCommandModel(null);
      commandIdDeviceTypeCommandModelList.remove(deviceTypeCommandModel);      
   }

   /**
    * Get a list of related DeviceTypeCommandModel objects of the CommandModel object.
    * These objects are in a bidirectional one-to-many relation by the CommandId member.
    *
    * @return Collection of DeviceTypeCommandModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCommandIdCommandModel")
   @JoinColumn(name = "COMMAND_ID")
   public Collection<DeviceTypeCommandModel> getCommandIdDeviceTypeCommandModelList() throws Exception {
   		return commandIdDeviceTypeCommandModelList;
   }


   /**
    * Set a list of DeviceTypeCommandModel related objects to the CommandModel object.
    * These objects are in a bidirectional one-to-many relation by the CommandId member.
    *
    * @param deviceTypeCommandModelList the list of related objects.
    */
    public void setCommandIdDeviceTypeCommandModelList(Collection<DeviceTypeCommandModel> deviceTypeCommandModelList) throws Exception {
		this.commandIdDeviceTypeCommandModelList = deviceTypeCommandModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getCommandId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&commandId=" + getCommandId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "commandId";
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
