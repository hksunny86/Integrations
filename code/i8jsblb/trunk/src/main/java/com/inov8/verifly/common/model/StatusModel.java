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
 * The StatusModel entity bean.
 *
 * @author  Shoaib Akhtar  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="StatusModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "STATUS_seq",sequenceName = "STATUS_seq",allocationSize=1)
@Table(name = "STATUS")
public class StatusModel extends BasePersistableModel {



   private transient Collection<LogModel> statusIdLogModelList = new ArrayList<LogModel>();
   private transient Collection<LogDetailModel> statusIdLogDetailModelList = new ArrayList<LogDetailModel>();

   private Long statusId;
   private String name;
   private String description;

   /**
    * Default constructor.
    */
   public StatusModel() {
   }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getStatusId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setStatusId(primaryKey);
    }

   /**
    * Returns the value of the <code>statusId</code> property.
    *
    */
      @Column(name = "STATUS_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="STATUS_seq")
   public Long getStatusId() {
      return statusId;
   }

   /**
    * Sets the value of the <code>statusId</code> property.
    *
    * @param statusId the value for the <code>statusId</code> property
    *
		    */

   public void setStatusId(Long statusId) {
      this.statusId = statusId;
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

   public void addStatusIdLogModel(LogModel logModel) {
      logModel.setRelationStatusIdStatusModel(this);
      statusIdLogModelList.add(logModel);
   }

   /**
    * Remove the related LogModel to this one-to-many relation.
    *
    * @param logModel object to be removed.
    */

   public void removeStatusIdLogModel(LogModel logModel) {
      logModel.setRelationStatusIdStatusModel(null);
      statusIdLogModelList.remove(logModel);
   }

   /**
    * Get a list of related LogModel objects of the StatusModel object.
    * These objects are in a bidirectional one-to-many relation by the StatusId member.
    *
    * @return Collection of LogModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationStatusIdStatusModel")
   @JoinColumn(name = "STATUS_ID")
   public Collection<LogModel> getStatusIdLogModelList() throws Exception {
   		return statusIdLogModelList;
   }


   /**
    * Set a list of LogModel related objects to the StatusModel object.
    * These objects are in a bidirectional one-to-many relation by the StatusId member.
    *
    * @param logModelList the list of related objects.
    */
    public void setStatusIdLogModelList(Collection<LogModel> logModelList) throws Exception {
		this.statusIdLogModelList = logModelList;
   }


   /**
    * Add the related LogDetailModel to this one-to-many relation.
    *
    * @param logDetailModel object to be added.
    */

   public void addStatusIdLogDetailModel(LogDetailModel logDetailModel) {
      logDetailModel.setRelationStatusIdStatusModel(this);
      statusIdLogDetailModelList.add(logDetailModel);
   }

   /**
    * Remove the related LogDetailModel to this one-to-many relation.
    *
    * @param logDetailModel object to be removed.
    */

   public void removeStatusIdLogDetailModel(LogDetailModel logDetailModel) {
      logDetailModel.setRelationStatusIdStatusModel(null);
      statusIdLogDetailModelList.remove(logDetailModel);
   }

   /**
    * Get a list of related LogDetailModel objects of the StatusModel object.
    * These objects are in a bidirectional one-to-many relation by the StatusId member.
    *
    * @return Collection of LogDetailModel objects.
    *
    */

   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationStatusIdStatusModel")
   @JoinColumn(name = "STATUS_ID")
   public Collection<LogDetailModel> getStatusIdLogDetailModelList() throws Exception {
   		return statusIdLogDetailModelList;
   }


   /**
    * Set a list of LogDetailModel related objects to the StatusModel object.
    * These objects are in a bidirectional one-to-many relation by the StatusId member.
    *
    * @param logDetailModelList the list of related objects.
    */
    public void setStatusIdLogDetailModelList(Collection<LogDetailModel> logDetailModelList) throws Exception {
		this.statusIdLogDetailModelList = logDetailModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getStatusId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&statusId=" + getStatusId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    {
			String primaryKeyFieldName = "statusId";
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
