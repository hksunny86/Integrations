package com.inov8.verifly.common.model;

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
 * The FailureReasonModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="FailureReasonModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "FAILURE_REASON_seq",sequenceName = "VF_FAILURE_REASON_seq",allocationSize=1)
@Table(name = "VF_FAILURE_REASON")
public class VfFailureReasonModel extends BasePersistableModel {



   private transient Collection<LogModel> failureReasonIdLogModelList = new ArrayList<LogModel>();
   private transient Collection<LogDetailModel> failureReasonIdLogDetailModelList = new ArrayList<LogDetailModel>();

   private Long failureReasonId;
   private String name;
   private String description;

   /**
    * Default constructor.
    */
   public VfFailureReasonModel() {
   }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getFailureReasonId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setFailureReasonId(primaryKey);
    }

   /**
    * Returns the value of the <code>failureReasonId</code> property.
    *
    */
      @Column(name = "FAILURE_REASON_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FAILURE_REASON_seq")
   public Long getFailureReasonId() {
      return failureReasonId;
   }

   /**
    * Sets the value of the <code>failureReasonId</code> property.
    *
    * @param failureReasonId the value for the <code>failureReasonId</code> property
    *
		    */

   public void setFailureReasonId(Long failureReasonId) {
      this.failureReasonId = failureReasonId;
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
    * Returns the value of the <code>description</code> property.
    *
    */
      @Column(name = "DESCRIPTION"  , length=255 )
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
    * @spring.validator-var name="maxlength" value="255"
    */

   public void setDescription(String description) {
      this.description = description;
   }


   /**
    * Add the related LogModel to this one-to-many relation.
    *
    * @param logModel object to be added.
    */

   public void addFailureReasonIdLogModel(LogModel logModel) {
      logModel.setRelationFailureReasonIdFailureReasonModel(this);
      failureReasonIdLogModelList.add(logModel);
   }

   /**
    * Remove the related LogModel to this one-to-many relation.
    *
    * @param logModel object to be removed.
    */

   public void removeFailureReasonIdLogModel(LogModel logModel) {
      logModel.setRelationFailureReasonIdFailureReasonModel(null);
      failureReasonIdLogModelList.remove(logModel);
   }

   /**
    * Get a list of related LogModel objects of the FailureReasonModel object.
    * These objects are in a bidirectional one-to-many relation by the FailureReasonId member.
    *
    * @return Collection of LogModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationFailureReasonIdFailureReasonModel")
   @JoinColumn(name = "FAILURE_REASON_ID")
   public Collection<LogModel> getFailureReasonIdLogModelList() throws Exception {
   		return failureReasonIdLogModelList;
   }


   /**
    * Set a list of LogModel related objects to the FailureReasonModel object.
    * These objects are in a bidirectional one-to-many relation by the FailureReasonId member.
    *
    * @param logModelList the list of related objects.
    */
    public void setFailureReasonIdLogModelList(Collection<LogModel> logModelList) throws Exception {
		this.failureReasonIdLogModelList = logModelList;
   }


   /**
    * Add the related LogDetailModel to this one-to-many relation.
    *
    * @param logDetailModel object to be added.
    */

   public void addFailureReasonIdLogDetailModel(LogDetailModel logDetailModel) {
      logDetailModel.setRelationFailureReasonIdFailureReasonModel(this);
      failureReasonIdLogDetailModelList.add(logDetailModel);
   }

   /**
    * Remove the related LogDetailModel to this one-to-many relation.
    *
    * @param logDetailModel object to be removed.
    */

   public void removeFailureReasonIdLogDetailModel(LogDetailModel logDetailModel) {
      logDetailModel.setRelationFailureReasonIdFailureReasonModel(null);
      failureReasonIdLogDetailModelList.remove(logDetailModel);
   }

   /**
    * Get a list of related LogDetailModel objects of the FailureReasonModel object.
    * These objects are in a bidirectional one-to-many relation by the FailureReasonId member.
    *
    * @return Collection of LogDetailModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationFailureReasonIdFailureReasonModel")
   @JoinColumn(name = "FAILURE_REASON_ID")
   public Collection<LogDetailModel> getFailureReasonIdLogDetailModelList() throws Exception {
   		return failureReasonIdLogDetailModelList;
   }


   /**
    * Set a list of LogDetailModel related objects to the FailureReasonModel object.
    * These objects are in a bidirectional one-to-many relation by the FailureReasonId member.
    *
    * @param logDetailModelList the list of related objects.
    */
    public void setFailureReasonIdLogDetailModelList(Collection<LogDetailModel> logDetailModelList) throws Exception {
		this.failureReasonIdLogDetailModelList = logDetailModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getFailureReasonId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&failureReasonId=" + getFailureReasonId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
			String primaryKeyFieldName = "failureReasonId";
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
