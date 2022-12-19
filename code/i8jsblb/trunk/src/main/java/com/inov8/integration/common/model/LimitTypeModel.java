package com.inov8.integration.common.model;

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
 * The LimitTypeModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="LimitTypeModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "LIMIT_TYPE_seq",sequenceName = "LIMIT_TYPE_seq", allocationSize=1)
@Table(name = "LIMIT_TYPE")
public class LimitTypeModel extends BasePersistableModel implements Serializable {
  
    private static final long serialVersionUID = -2053701223349868426L;

    private Collection<LimitModel> limitTypeIdLimitModelList = new ArrayList<LimitModel>();

   private Long limitTypeId;
   private String name;

   /**
    * Default constructor.
    */
   public LimitTypeModel() {
   }   

    public LimitTypeModel( Long limitTypeId )
    {
        super();
        this.limitTypeId = limitTypeId;
    }

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getLimitTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setLimitTypeId(primaryKey);
    }

   /**
    * Returns the value of the <code>limitTypeId</code> property.
    *
    */
      @Column(name = "LIMIT_TYPE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LIMIT_TYPE_seq")
   public Long getLimitTypeId() {
      return limitTypeId;
   }

   /**
    * Sets the value of the <code>limitTypeId</code> property.
    *
    * @param limitTypeId the value for the <code>limitTypeId</code> property
    *    
		    */

   public void setLimitTypeId(Long limitTypeId) {
      this.limitTypeId = limitTypeId;
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
    * Add the related LimitModel to this one-to-many relation.
    *
    * @param limitModel object to be added.
    */
    
   public void addLimitTypeIdLimitModel(LimitModel limitModel) {
      limitModel.setRelationLimitTypeIdLimitTypeModel(this);
      limitTypeIdLimitModelList.add(limitModel);
   }
   
   /**
    * Remove the related LimitModel to this one-to-many relation.
    *
    * @param limitModel object to be removed.
    */
   
   public void removeLimitTypeIdLimitModel(LimitModel limitModel) {      
      limitModel.setRelationLimitTypeIdLimitTypeModel(null);
      limitTypeIdLimitModelList.remove(limitModel);      
   }

   /**
    * Get a list of related LimitModel objects of the LimitTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the LimitTypeId member.
    *
    * @return Collection of LimitModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationLimitTypeIdLimitTypeModel")
   @JoinColumn(name = "LIMIT_TYPE_ID")
   public Collection<LimitModel> getLimitTypeIdLimitModelList() throws Exception {
   		return limitTypeIdLimitModelList;
   }


   /**
    * Set a list of LimitModel related objects to the LimitTypeModel object.
    * These objects are in a bidirectional one-to-many relation by the LimitTypeId member.
    *
    * @param limitModelList the list of related objects.
    */
    public void setLimitTypeIdLimitModelList(Collection<LimitModel> limitModelList) throws Exception {
		this.limitTypeIdLimitModelList = limitModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getLimitTypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&limitTypeId=" + getLimitTypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "limitTypeId";
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
