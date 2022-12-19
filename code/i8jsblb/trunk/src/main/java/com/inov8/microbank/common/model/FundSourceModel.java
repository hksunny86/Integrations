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
 * The FundSourceModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="FundSourceModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "FUND_SOURCE_seq",sequenceName = "FUND_SOURCE_seq", allocationSize=1)
@Table(name = "FUND_SOURCE")
public class FundSourceModel extends BasePersistableModel implements Serializable {
  


   private Collection<CustomerModel> fundSourceIdCustomerModelList = new ArrayList<CustomerModel>();

   private Long fundSourceId;
   private String name;

   /**
    * Default constructor.
    */
   public FundSourceModel() {
   }   

    public FundSourceModel(Long fundSourceId)
    {
		this.fundSourceId = fundSourceId;
	}

	/**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getFundSourceId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setFundSourceId(primaryKey);
    }

   /**
    * Returns the value of the <code>fundSourceId</code> property.
    *
    */
      @Column(name = "FUND_SOURCE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FUND_SOURCE_seq")
   public Long getFundSourceId() {
      return fundSourceId;
   }

   /**
    * Sets the value of the <code>fundSourceId</code> property.
    *
    * @param fundSourceId the value for the <code>fundSourceId</code> property
    *    
		    */

   public void setFundSourceId(Long fundSourceId) {
      this.fundSourceId = fundSourceId;
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
    * Add the related CustomerModel to this one-to-many relation.
    *
    * @param customerModel object to be added.
    */
    
   public void addFundSourceIdCustomerModel(CustomerModel customerModel) {
      customerModel.setRelationFundSourceIdFundSourceModel(this);
      fundSourceIdCustomerModelList.add(customerModel);
   }
   
   /**
    * Remove the related CustomerModel to this one-to-many relation.
    *
    * @param customerModel object to be removed.
    */
   
   public void removeFundSourceIdCustomerModel(CustomerModel customerModel) {      
      customerModel.setRelationFundSourceIdFundSourceModel(null);
      fundSourceIdCustomerModelList.remove(customerModel);      
   }

   /**
    * Get a list of related CustomerModel objects of the FundSourceModel object.
    * These objects are in a bidirectional one-to-many relation by the FundSourceId member.
    *
    * @return Collection of CustomerModel objects.
    *
    */
   
   @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationFundSourceIdFundSourceModel")
   @JoinColumn(name = "FUND_SOURCE_ID")
   public Collection<CustomerModel> getFundSourceIdCustomerModelList() throws Exception {
   		return fundSourceIdCustomerModelList;
   }


   /**
    * Set a list of CustomerModel related objects to the FundSourceModel object.
    * These objects are in a bidirectional one-to-many relation by the FundSourceId member.
    *
    * @param customerModelList the list of related objects.
    */
    public void setFundSourceIdCustomerModelList(Collection<CustomerModel> customerModelList) throws Exception {
		this.fundSourceIdCustomerModelList = customerModelList;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getFundSourceId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&fundSourceId=" + getFundSourceId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "fundSourceId";
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
