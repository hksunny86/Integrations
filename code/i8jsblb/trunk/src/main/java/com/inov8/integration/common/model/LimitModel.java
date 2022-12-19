package com.inov8.integration.common.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import org.springframework.jdbc.core.RowMapper;

/**
 * The LimitModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="LimitModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "LIMIT_seq",sequenceName = "LIMIT_seq", allocationSize=1)
@Table(name = "LIMIT")
public class LimitModel extends BasePersistableModel implements Serializable ,RowMapper {
  
   private static final long serialVersionUID = -8069646075500499390L;

   private OlaTransactionTypeModel transactionTypeIdTransactionTypeModel;
   private LimitTypeModel limitTypeIdLimitTypeModel;
   private OlaCustomerAccountTypeModel customerAccountTypeIdCustomerAccountTypeModel;


   private Long limitId;
   private Double minimum;
   private Double maximum;
   private Boolean isApplicable;
   private Date updatedOn;
   private Date createdOn;
   private Integer versionNo;
  
   private AppUserModel updatedByAppUserModel;
   private AppUserModel createdByAppUserModel;
  
   /**
    * Default constructor.
    */
   public LimitModel() {
   }   

   
    public LimitModel( Double minimum, Double maximum )
    {
        super();
        this.minimum = minimum;
        this.maximum = maximum;
    }


    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getLimitId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setLimitId(primaryKey);
    }

   /**
    * Returns the value of the <code>limitId</code> property.
    *
    */
      @Column(name = "LIMIT_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LIMIT_seq")
   public Long getLimitId() {
      return limitId;
   }

   /**
    * Sets the value of the <code>limitId</code> property.
    *
    * @param limitId the value for the <code>limitId</code> property
    *    
		    */

   public void setLimitId(Long limitId) {
      this.limitId = limitId;
   }

   /**
    * Returns the value of the <code>minimum</code> property.
    *
    */
      @Column(name = "MINIMUM" , nullable = false )
   public Double getMinimum() {
      return minimum;
   }

   /**
    * Sets the value of the <code>minimum</code> property.
    *
    * @param minimum the value for the <code>minimum</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setMinimum(Double minimum) {
      this.minimum = minimum;
   }

   /**
    * Returns the value of the <code>maximum</code> property.
    *
    */
      @Column(name = "MAXIMUM" , nullable = true )
   public Double getMaximum() {
      return maximum;
   }

   /**
    * Sets the value of the <code>maximum</code> property.
    *
    * @param maximum the value for the <code>maximum</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setMaximum(Double maximum) {
      this.maximum = maximum;
   }

   /**
    * Returns the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    * @return the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
   @JoinColumn(name = "TRANSACTION_TYPE_ID")
   @Fetch(value=FetchMode.JOIN)
   public OlaTransactionTypeModel getRelationTransactionTypeIdTransactionTypeModel(){
      return transactionTypeIdTransactionTypeModel;
   }
    
   /**
    * Returns the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    * @return the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public OlaTransactionTypeModel getTransactionTypeIdTransactionTypeModel(){
      return getRelationTransactionTypeIdTransactionTypeModel();
   }

   /**
    * Sets the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    * @param transactionTypeModel a value for <code>transactionTypeIdTransactionTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationTransactionTypeIdTransactionTypeModel(OlaTransactionTypeModel transactionTypeModel) {
      this.transactionTypeIdTransactionTypeModel = transactionTypeModel;
   }
   
   /**
    * Sets the value of the <code>transactionTypeIdTransactionTypeModel</code> relation property.
    *
    * @param transactionTypeModel a value for <code>transactionTypeIdTransactionTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setTransactionTypeIdTransactionTypeModel(OlaTransactionTypeModel transactionTypeModel) {
      if(null != transactionTypeModel)
      {
      	setRelationTransactionTypeIdTransactionTypeModel((OlaTransactionTypeModel)transactionTypeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>limitTypeIdLimitTypeModel</code> relation property.
    *
    * @return the value of the <code>limitTypeIdLimitTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
   @JoinColumn(name = "LIMIT_TYPE_ID")
   @Fetch(value=FetchMode.JOIN)
   public LimitTypeModel getRelationLimitTypeIdLimitTypeModel(){
      return limitTypeIdLimitTypeModel;
   }
    
   /**
    * Returns the value of the <code>limitTypeIdLimitTypeModel</code> relation property.
    *
    * @return the value of the <code>limitTypeIdLimitTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public LimitTypeModel getLimitTypeIdLimitTypeModel(){
      return getRelationLimitTypeIdLimitTypeModel();
   }

   /**
    * Sets the value of the <code>limitTypeIdLimitTypeModel</code> relation property.
    *
    * @param limitTypeModel a value for <code>limitTypeIdLimitTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationLimitTypeIdLimitTypeModel(LimitTypeModel limitTypeModel) {
      this.limitTypeIdLimitTypeModel = limitTypeModel;
   }
   
   /**
    * Sets the value of the <code>limitTypeIdLimitTypeModel</code> relation property.
    *
    * @param limitTypeModel a value for <code>limitTypeIdLimitTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setLimitTypeIdLimitTypeModel(LimitTypeModel limitTypeModel) {
      if(null != limitTypeModel)
      {
      	setRelationLimitTypeIdLimitTypeModel((LimitTypeModel)limitTypeModel.clone());
      }      
   }
   

   /**
    * Returns the value of the <code>customerAccountTypeIdCustomerAccountTypeModel</code> relation property.
    *
    * @return the value of the <code>customerAccountTypeIdCustomerAccountTypeModel</code> relation property.
    *
    */
   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
   @JoinColumn(name = "CUSTOMER_ACCOUNT_TYPE_ID")    
   public OlaCustomerAccountTypeModel getRelationCustomerAccountTypeIdCustomerAccountTypeModel(){
      return customerAccountTypeIdCustomerAccountTypeModel;
   }
    
   /**
    * Returns the value of the <code>customerAccountTypeIdCustomerAccountTypeModel</code> relation property.
    *
    * @return the value of the <code>customerAccountTypeIdCustomerAccountTypeModel</code> relation property.
    *
    */
   @javax.persistence.Transient
   public OlaCustomerAccountTypeModel getCustomerAccountTypeIdCustomerAccountTypeModel(){
      return getRelationCustomerAccountTypeIdCustomerAccountTypeModel();
   }

   /**
    * Sets the value of the <code>customerAccountTypeIdCustomerAccountTypeModel</code> relation property.
    *
    * @param customerAccountTypeModel a value for <code>customerAccountTypeIdCustomerAccountTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setRelationCustomerAccountTypeIdCustomerAccountTypeModel(OlaCustomerAccountTypeModel customerAccountTypeModel) {
      this.customerAccountTypeIdCustomerAccountTypeModel = customerAccountTypeModel;
   }
   
   /**
    * Sets the value of the <code>customerAccountTypeIdCustomerAccountTypeModel</code> relation property.
    *
    * @param customerAccountTypeModel a value for <code>customerAccountTypeIdCustomerAccountTypeModel</code>.
    */
   @javax.persistence.Transient
   public void setCustomerAccountTypeIdCustomerAccountTypeModel(OlaCustomerAccountTypeModel customerAccountTypeModel) {
      if(null != customerAccountTypeModel)
      {
      	setRelationCustomerAccountTypeIdCustomerAccountTypeModel((OlaCustomerAccountTypeModel)customerAccountTypeModel.clone());
      }      
   }
   



   /**
    * Returns the value of the <code>transactionTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getTransactionTypeId() {
      if (transactionTypeIdTransactionTypeModel != null) {
         return transactionTypeIdTransactionTypeModel.getTransactionTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>transactionTypeId</code> property.
    *
    * @param transactionTypeId the value for the <code>transactionTypeId</code> property
													    * @spring.validator type="required"
			    */
   
   @javax.persistence.Transient
   public void setTransactionTypeId(Long transactionTypeId) {
      if(transactionTypeId == null)
      {      
      	transactionTypeIdTransactionTypeModel = null;
      }
      else
      {
        transactionTypeIdTransactionTypeModel = new OlaTransactionTypeModel();
      	transactionTypeIdTransactionTypeModel.setTransactionTypeId(transactionTypeId);
      }      
   }

   /**
    * Returns the value of the <code>limitTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getLimitTypeId() {
      if (limitTypeIdLimitTypeModel != null) {
         return limitTypeIdLimitTypeModel.getLimitTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>limitTypeId</code> property.
    *
    * @param limitTypeId the value for the <code>limitTypeId</code> property
					    * @spring.validator type="required"
											    */
   
   @javax.persistence.Transient
   public void setLimitTypeId(Long limitTypeId) {
      if(limitTypeId == null)
      {      
      	limitTypeIdLimitTypeModel = null;
      }
      else
      {
        limitTypeIdLimitTypeModel = new LimitTypeModel();
      	limitTypeIdLimitTypeModel.setLimitTypeId(limitTypeId);
      }      
   }

   /**
    * Returns the value of the <code>customerAccountTypeId</code> property.
    *
    */
   @javax.persistence.Transient
   public Long getCustomerAccountTypeId() {
      if (customerAccountTypeIdCustomerAccountTypeModel != null) {
         return customerAccountTypeIdCustomerAccountTypeModel.getCustomerAccountTypeId();
      } else {
         return null;
      }
   }

   /**
    * Sets the value of the <code>customerAccountTypeId</code> property.
    *
    * @param customerAccountTypeId the value for the <code>customerAccountTypeId</code> property
															    */
   
   @javax.persistence.Transient
   public void setCustomerAccountTypeId(Long customerAccountTypeId) {
      if(customerAccountTypeId == null)
      {      
      	customerAccountTypeIdCustomerAccountTypeModel = null;
      }
      else
      {
        customerAccountTypeIdCustomerAccountTypeModel = new OlaCustomerAccountTypeModel();
      	customerAccountTypeIdCustomerAccountTypeModel.setCustomerAccountTypeId(customerAccountTypeId);
      }      
   }


    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getLimitId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&limitId=" + getLimitId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "limitId";
			return primaryKeyFieldName;				
    }


    /**
     * Returns the value of the <code>isApplicable</code> property.
     *
     */
       @Column(name = "IS_APPLICABLE" , nullable = false )
    public Boolean getIsApplicable() {
       return isApplicable;
    }

    /**
     * Sets the value of the <code>active</code> property.
     *
     * @param active the value for the <code>active</code> property
     *    
 		    */

    public void setIsApplicable(Boolean isApplicable) {
       this.isApplicable = isApplicable;
    }

    /**
     * Returns the value of the <code>updatedOn</code> property.
     *
     */
       @Column(name = "UPDATED_ON" , nullable = false )
    public Date getUpdatedOn() {
       return updatedOn;
    }

    /**
     * Sets the value of the <code>updatedOn</code> property.
     *
     * @param updatedOn the value for the <code>updatedOn</code> property
     *    
 		    */

    public void setUpdatedOn(Date updatedOn) {
       this.updatedOn = updatedOn;
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
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     *
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATED_BY")    
    public AppUserModel getRelationUpdatedByAppUserModel(){
       return updatedByAppUserModel;
    }
     
    /**
     * Returns the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @return the value of the <code>updatedByAppUserModel</code> relation property.
     *
     */
    @javax.persistence.Transient
    public AppUserModel getUpdatedByAppUserModel(){
       return getRelationUpdatedByAppUserModel();
    }

    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
       this.updatedByAppUserModel = appUserModel;
    }
    
    /**
     * Sets the value of the <code>updatedByAppUserModel</code> relation property.
     *
     * @param appUserModel a value for <code>updatedByAppUserModel</code>.
     */
    @javax.persistence.Transient
    public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
       if(null != appUserModel)
       {
       	setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
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
     * Returns the value of the <code>appUserId</code> property.
     *
     */
    @javax.persistence.Transient
    public Long getUpdatedBy() {
       if (updatedByAppUserModel != null) {
          return updatedByAppUserModel.getAppUserId();
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
    public void setUpdatedBy(Long appUserId) {
       if(appUserId == null)
       {      
       	updatedByAppUserModel = null;
       }
       else
       {
         updatedByAppUserModel = new AppUserModel();
       	updatedByAppUserModel.setAppUserId(appUserId);
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
     * Helper method for Complex Example Queries
     */
    @javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
    	
    	      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("TransactionTypeModel");
    	associationModel.setPropertyName("relationTransactionTypeIdTransactionTypeModel");   		
   		associationModel.setValue(getRelationTransactionTypeIdTransactionTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("LimitTypeModel");
    	associationModel.setPropertyName("relationLimitTypeIdLimitTypeModel");   		
   		associationModel.setValue(getRelationLimitTypeIdLimitTypeModel());
   		
   		associationModelList.add(associationModel);
   		
			      associationModel = new AssociationModel();
    	
    	associationModel.setClassName("CustomerAccountTypeModel");
    	associationModel.setPropertyName("relationCustomerAccountTypeIdCustomerAccountTypeModel");   		
   		associationModel.setValue(getRelationCustomerAccountTypeIdCustomerAccountTypeModel());
   		
   		associationModelList.add(associationModel);

   		associationModel = new AssociationModel();

   		associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationUpdatedByAppUserModel");   		
    	associationModel.setValue(getRelationUpdatedByAppUserModel());

    	associationModelList.add(associationModel);

    	associationModel = new AssociationModel();

    	associationModel.setClassName("AppUserModel");
    	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
    	associationModel.setValue(getRelationCreatedByAppUserModel());
	    	
    	return associationModelList;
    }

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        LimitModel vo=new LimitModel();
        vo.setTransactionTypeId(resultSet.getLong("TRANSACTION_TYPE_ID"));
        vo.setLimitTypeId(resultSet.getLong("LIMIT_TYPE_ID"));
        vo.setCustomerAccountTypeId(resultSet.getLong("CUSTOMER_ACCOUNT_TYPE_ID"));
        vo.setLimitId(resultSet.getLong("LIMIT_ID"));
        vo.setMinimum(resultSet.getDouble("MINIMUM"));
        vo.setMaximum(resultSet.getDouble("MAXIMUM"));
        vo.setIsApplicable(resultSet.getBoolean("IS_APPLICABLE"));
        vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
        vo.setCreatedOn(resultSet.getTimestamp("CREATED_ON"));
        vo.setVersionNo(resultSet.getInt("VERSION_NO"));
        vo.setCreatedBy(resultSet.getLong("CREATED_BY"));
        vo.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
        return vo;
    }
}
