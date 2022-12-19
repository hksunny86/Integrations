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

/**
 * The CountryModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CountryModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "OCCUPATION")
public class OccupationModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = -7530742427041947692L;
	private Long occupationId;
	private String name;

   /**
    * Default constructor.
    */
   public OccupationModel() {
	   
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getOccupationId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setOccupationId(primaryKey);
    }

   @Id
   @Column(name = "OCCUPATION_ID" , nullable = false)
	public Long getOccupationId() {
		return occupationId;
	}

	public void setOccupationId(Long occupationId) {
		this.occupationId = occupationId;
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
        checkBox += "_"+ getOccupationId();
        checkBox += "\"/>";
        return checkBox;
    }


   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&occupationId=" + getOccupationId();
      return parameters;
   }

    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "occupationId";
			return primaryKeyFieldName;				
    }
    

    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
    	return associationModelList;
    }

}
