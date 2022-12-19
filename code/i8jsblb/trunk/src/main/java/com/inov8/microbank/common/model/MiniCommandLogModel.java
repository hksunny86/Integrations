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
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The MiniCommandLogModel entity bean.
 *
 * @author  Jawwad Farooq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="MiniCommandLogModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "MINI_COMMAND_LOG_seq",sequenceName = "MINI_COMMAND_LOG_seq", allocationSize=1)
@Table(name = "MINI_COMMAND_LOG")
public class MiniCommandLogModel extends BasePersistableModel implements Serializable {
  

   private CommandModel commandIdCommandModel;
   private ActionLogModel actionLogIdActionLogModel;


   private Long miniCommandLogId;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public MiniCommandLogModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getMiniCommandLogId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setMiniCommandLogId(primaryKey);
    }

   /**
    * Returns the value of the <code>miniCommandLogId</code> property.
    *
    */
      @Column(name = "MINI_COMMAND_LOG_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MINI_COMMAND_LOG_seq")
   public Long getMiniCommandLogId() {
      return miniCommandLogId;
   }

   /**
    * Sets the value of the <code>miniCommandLogId</code> property.
    *
    * @param miniCommandLogId the value for the <code>miniCommandLogId</code> property
    *    
		    */

   public void setMiniCommandLogId(Long miniCommandLogId) {
      this.miniCommandLogId = miniCommandLogId;
   }

   /**
    * Returns the value of the <code>versionNo</code> property.
    *
    */
      @Version 
	    @Column(name = "VERSION_NO" , nullable = false )
   public Integer getVersionNo() {
      return versionNo;
   }

   /**
    * Sets the value of the <code>versionNo</code> property.
    *
    * @param versionNo the value for the <code>versionNo</code> property
    *    
		    */

   public void setVersionNo(Integer versionNo) {
      this.versionNo = versionNo;
   }

   /**
    * Returns the value of the <code>commandIdCommandModel</code> relation property.
    *
    * @return the value of the <code>commandIdCommandModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "COMMAND_ID")    
   public CommandModel getRelationCommandIdCommandModel(){
      return commandIdCommandModel;
   }
    
   /**
    * Returns the value of the <code>commandIdCommandModel</code> relation property.
    *
    * @return the value of the <code>commandIdCommandModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public CommandModel getCommandIdCommandModel(){
      return getRelationCommandIdCommandModel();
   }

   /**
    * Sets the value of the <code>commandIdCommandModel</code> relation property.
    *
    * @param commandModel a value for <code>commandIdCommandModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCommandIdCommandModel(CommandModel commandModel) {
      this.commandIdCommandModel = commandModel;
   }
   
   /**
    * Sets the value of the <code>commandIdCommandModel</code> relation property.
    *
    * @param commandModel a value for <code>commandIdCommandModel</code>.
    */
   @javax.persistence.Transient
   public void setCommandIdCommandModel(CommandModel commandModel) {
      if(null != commandModel)
      {
      	setRelationCommandIdCommandModel((CommandModel)commandModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    * @return the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "ACTION_LOG_ID")    
   public ActionLogModel getRelationActionLogIdActionLogModel(){
      return actionLogIdActionLogModel;
   }
    
   /**
    * Returns the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    * @return the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public ActionLogModel getActionLogIdActionLogModel(){
      return getRelationActionLogIdActionLogModel();
   }

   /**
    * Sets the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    * @param actionLogModel a value for <code>actionLogIdActionLogModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationActionLogIdActionLogModel(ActionLogModel actionLogModel) {
      this.actionLogIdActionLogModel = actionLogModel;
   }
   
   /**
    * Sets the value of the <code>actionLogIdActionLogModel</code> relation property.
    *
    * @param actionLogModel a value for <code>actionLogIdActionLogModel</code>.
    */
   @javax.persistence.Transient
   public void setActionLogIdActionLogModel(ActionLogModel actionLogModel) {
      if(null != actionLogModel)
      {
      	setRelationActionLogIdActionLogModel((ActionLogModel)actionLogModel.clone());
      }      
   }
   



   /**
    * Returns the value of the <code>commandId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCommandId() {
      if (commandIdCommandModel != null) {
         return commandIdCommandModel.getCommandId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>commandId</code> property.
    *
    * @param commandId the value for the <code>commandId</code> property
							    * @spring.validator type="required"
					    */
   
   @javax.persistence.Transient
   public void setCommandId(Long commandId) {
      if(commandId == null)
      {      
      	commandIdCommandModel = null;
      }
      else
      {
        commandIdCommandModel = new CommandModel();
      	commandIdCommandModel.setCommandId(commandId);
      }      
   }

   /**
    * Returns the value of the <code>actionLogId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getActionLogId() {
      if (actionLogIdActionLogModel != null) {
         return actionLogIdActionLogModel.getActionLogId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>actionLogId</code> property.
    *
    * @param actionLogId the value for the <code>actionLogId</code> property
					    * @spring.validator type="required"
							    */
   
   @javax.persistence.Transient
   public void setActionLogId(Long actionLogId) {
      if(actionLogId == null)
      {      
      	actionLogIdActionLogModel = null;
      }
      else
      {
        actionLogIdActionLogModel = new ActionLogModel();
      	actionLogIdActionLogModel.setActionLogId(actionLogId);
      }      
   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getMiniCommandLogId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&miniCommandLogId=" + getMiniCommandLogId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "miniCommandLogId";
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
    	
    	associationModel.setClassName("CommandModel");
    	associationModel.setPropertyName("relationCommandIdCommandModel");   		
   		associationModel.setValue(getRelationCommandIdCommandModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("ActionLogModel");
    	associationModel.setPropertyName("relationActionLogIdActionLogModel");   		
   		associationModel.setValue(getRelationActionLogIdActionLogModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
