package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "BUSINESS_TYPE")
public class BusinessTypeModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = -958374939457035320L;
	private Long businessTypeId;
	private String name;
	private String category;

   /**
    * Default constructor.
    */
   public BusinessTypeModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getBusinessTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setBusinessTypeId(primaryKey);
    }

   @Id
   @Column(name = "BUISNESS_TYPE_ID" , nullable = false)
	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

   /**
    * Returns the value of the <code>name</code> property.
    *
    */
     @Column(name = "NAME" , nullable = false )
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getBusinessTypeId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&businessTypeId=" + getBusinessTypeId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "businessTypeId";
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

    @Column(name = "CATEGORY")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
