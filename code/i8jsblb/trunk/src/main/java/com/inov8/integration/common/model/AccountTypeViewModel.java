package com.inov8.integration.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

/**
 * The CustomerAccountTypeModel entity bean.
 *
 * @author  Usman Ashraf  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CustomerAccountTypeModel"
 */
@Entity
@Table(name = "ACCOUNT_TYPE_VIEW")
public class AccountTypeViewModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = -5772166268777856331L;

   private Long accountTypeId;
   private String name;
   private Boolean isActive;
   private Boolean isCustomerAccountType;
   private String accountTypeCategory;
   private String parentAccountTypeId;
   private String parentAccountTypeName;
   
   /**
    * Default constructor.
    */
   public AccountTypeViewModel() {
   }    

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getAccountTypeId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
	   setAccountTypeId(primaryKey);
    }

   @Id
   @Column(name = "ACCOUNT_TYPE_ID")
	public Long getAccountTypeId() {
		return accountTypeId;
	}
	
	public void setAccountTypeId(Long accountTypeId) {
		this.accountTypeId = accountTypeId;
	}
   

   /**
    * Returns the value of the <code>name</code> property.
    *
    */
      @Column(name = "NAME")
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @Column(name = "IS_ACTIVE")
	public Boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	@Column(name = "ACCOUNT_TYPE_CATEGORY")
	public String getAccountTypeCategory() {
		return accountTypeCategory;
	}
	
	public void setAccountTypeCategory(String accountTypeCategory) {
		this.accountTypeCategory = accountTypeCategory;
	}
	
	@Column(name = "PARENT_ACCOUNT_TYPE_ID")
	public String getParentAccountTypeId() {
		return parentAccountTypeId;
	}
	
	public void setParentAccountTypeId(String parentAccountTypeId) {
		this.parentAccountTypeId = parentAccountTypeId;
	}
	
	@Column(name = "PARENT_ACCOUNT_TYPE_NAME")
	public String getParentAccountTypeName() {
		return parentAccountTypeName;
	}
	
	public void setParentAccountTypeName(String parentAccountTypeName) {
		this.parentAccountTypeName = parentAccountTypeName;
	}

	   /**
	    * Helper method for Struts with displaytag
	    */
	   @javax.persistence.Transient
	   public String getPrimaryKeyParameter() {
	      String parameters = "";
	      parameters += "&accountTypeId=" + getAccountTypeId();
	      return parameters;
	   }
		/**
	     * Helper method for default Sorting on Primary Keys
	     */
	    @javax.persistence.Transient
	    public String getPrimaryKeyFieldName()
	    { 
				String primaryKeyFieldName = "accountTypeId";
				return primaryKeyFieldName;				
	    }

	    @Column(name = "IS_CUSTOMER_ACCOUNT_TYPE")
		public Boolean getIsCustomerAccountType() {
			return isCustomerAccountType;
		}

		public void setIsCustomerAccountType(Boolean isCustomerAccountType) {
			this.isCustomerAccountType = isCustomerAccountType;
		}

}
