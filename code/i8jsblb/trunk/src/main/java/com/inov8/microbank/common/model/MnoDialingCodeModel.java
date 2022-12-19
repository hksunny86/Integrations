package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
 * The MnoDialingCodeModel entity bean.
 *
 * @author  Maqsood Shahzad  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="MnoDialingCodeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "SERVICE_OP_DIALING_CODE_seq",sequenceName = "SERVICE_OP_DIALING_CODE_seq", allocationSize=1)
@Table(name = "SERVICE_OP_DIALING_CODE")
public class MnoDialingCodeModel extends BasePersistableModel implements Serializable{
  

   /**
	 * 
	 */
	private static final long serialVersionUID = -7314440908555876389L;
	private MnoModel mnoIdMnoModel;
    private AppUserModel createdByAppUserModel;


   private Long mnoDialingCodeId;
   private String code;
   private String description;
   private String comments;
   private Date createdOn;
   private Integer versionNo;

   /**
    * Default constructor.
    */
   public MnoDialingCodeModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getMnoDialingCodeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setMnoDialingCodeId(primaryKey);
    }

   /**
    * Returns the value of the <code>mnoDialingCodeId</code> property.
    *
    */
      @Column(name = "SERVICE_OP_DIALING_CODE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SERVICE_OP_DIALING_CODE_seq")
   public Long getMnoDialingCodeId() {
      return mnoDialingCodeId;
   }

   /**
    * Sets the value of the <code>mnoDialingCodeId</code> property.
    *
    * @param mnoDialingCodeId the value for the <code>mnoDialingCodeId</code> property
    *    
		    */

   public void setMnoDialingCodeId(Long mnoDialingCodeId) {
      this.mnoDialingCodeId = mnoDialingCodeId;
   }

   /**
    * Returns the value of the <code>code</code> property.
    *
    */
      @Column(name = "CODE" , nullable = false , length=50 )
   public String getCode() {
      return code;
   }

   /**
    * Sets the value of the <code>code</code> property.
    *
    * @param code the value for the <code>code</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setCode(String code) {
      this.code = code;
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
    * Returns the value of the <code>createdOn</code> property.
    *
    */
      @Column(name = "CREATED_ON" , nullable = false )
   public Date getCreatedOn() {
      return createdOn;
   }

   /**
    * Sets the value of the <code>createdOn</code> property.
    *
    * @param createdOn the value for the <code>createdOn</code> property
    *    
		    */

   public void setCreatedOn(Date createdOn) {
      this.createdOn = createdOn;
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
    * Returns the value of the <code>mnoIdMnoModel</code> relation property.
    *
    * @return the value of the <code>mnoIdMnoModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "SERVICE_OP_ID")    
   public MnoModel getRelationMnoIdMnoModel(){
      return mnoIdMnoModel;
   }
    
   /**
    * Returns the value of the <code>mnoIdMnoModel</code> relation property.
    *
    * @return the value of the <code>mnoIdMnoModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public MnoModel getMnoIdMnoModel(){
      return getRelationMnoIdMnoModel();
   }

   /**
    * Sets the value of the <code>mnoIdMnoModel</code> relation property.
    *
    * @param mnoModel a value for <code>mnoIdMnoModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationMnoIdMnoModel(MnoModel mnoModel) {
      this.mnoIdMnoModel = mnoModel;
   }
   
   /**
    * Sets the value of the <code>mnoIdMnoModel</code> relation property.
    *
    * @param mnoModel a value for <code>mnoIdMnoModel</code>.
    */
   @javax.persistence.Transient
   public void setMnoIdMnoModel(MnoModel mnoModel) {
      if(null != mnoModel)
      {
      	setRelationMnoIdMnoModel((MnoModel)mnoModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @return the value of the <code>createdByAppUserModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CREATED_BY")    
   public AppUserModel getRelationCreatedByAppUserModel(){
      return createdByAppUserModel;
   }
    
   /**
    * Returns the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @return the value of the <code>createdByAppUserModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public AppUserModel getCreatedByAppUserModel(){
      return getRelationCreatedByAppUserModel();
   }

   /**
    * Sets the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>createdByAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
      this.createdByAppUserModel = appUserModel;
   }
   
   /**
    * Sets the value of the <code>createdByAppUserModel</code> relation property.
    *
    * @param appUserModel a value for <code>createdByAppUserModel</code>.
    */
   @javax.persistence.Transient
   public void setCreatedByAppUserModel(AppUserModel appUserModel) {
      if(null != appUserModel)
      {
      	setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
      }      
   }
   



   /**
    * Returns the value of the <code>mnoId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getMnoId() {
      if (mnoIdMnoModel != null) {
         return mnoIdMnoModel.getMnoId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>mnoId</code> property.
    *
    * @param mnoId the value for the <code>mnoId</code> property
					    * @spring.validator type="required"
															    */
   
   @javax.persistence.Transient
   public void setMnoId(Long mnoId) {
      if(mnoId == null)
      {      
      	mnoIdMnoModel = null;
      }
      else
      {
        mnoIdMnoModel = new MnoModel();
      	mnoIdMnoModel.setMnoId(mnoId);
      }      
   }

   /**
    * Returns the value of the <code>appUserId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCreatedBy() {
      if (createdByAppUserModel != null) {
         return createdByAppUserModel.getAppUserId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>appUserId</code> property.
    *
    * @param appUserId the value for the <code>appUserId</code> property
																	    */
   
   @javax.persistence.Transient
   public void setCreatedBy(Long appUserId) {
      if(appUserId == null)
      {      
      	createdByAppUserModel = null;
      }
      else
      {
        createdByAppUserModel = new AppUserModel();
      	createdByAppUserModel.setAppUserId(appUserId);
      }      
   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getMnoDialingCodeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&mnoDialingCodeId=" + getMnoDialingCodeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "mnoDialingCodeId";
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
    	
    	associationModel.setClassName("MnoModel");
    	associationModel.setPropertyName("relationMnoIdMnoModel");   		
   		associationModel.setValue(getRelationMnoIdMnoModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
   		associationModel.setValue(getRelationCreatedByAppUserModel());
   		
   		associationModelList.add(associationModel);
   		
			    	
    	return associationModelList;
    }    
          
}
