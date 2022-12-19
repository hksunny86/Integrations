package com.inov8.microbank.common.model.commissionmodule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.model.UsecaseModel;

/**
 * The CommShAcctsListViewModel entity bean.
 *
 * @author  Fahad Tariq  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="CommShAcctsListViewModel"
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "COMM_SH_ACCTS_LIST_VIEW")
public class CommShAcctsListViewModel extends BasePersistableModel implements Serializable {
  
   private static final long serialVersionUID = -8287292572881806500L;
   
   private Long stakeholderBankInfoId;
   private String accountNo;
   private String accountTitle;
   private Boolean active;
   private String acctTypeName;
   private Long acctTypeId;
   private String shName;
   private Long shId;
   private Long bankId;
   private String accountType;
   private StakeholderBankInfoModel ofSettlementStakeholderBankInfoModel;
   private StakeholderBankInfoModel t24StakeholderBankInfoModel;

	/**
	 * Added by atif hussain
	 */
	private Long accountTypeId;
	private String accountTypeName;
	private Long productId;
	private String productName;
   /**
    * Default constructor.
    */
   public CommShAcctsListViewModel() {
   }   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getStakeholderBankInfoId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setStakeholderBankInfoId(primaryKey);
    }

   /**
    * Returns the value of the <code>stakeholderBankInfoId</code> property.
    *
    */
      @Column(name = "STAKEHOLDER_BANK_INFO_ID" , nullable = false )
   @Id
   public Long getStakeholderBankInfoId() {
      return stakeholderBankInfoId;
   }

   /**
    * Sets the value of the <code>stakeholderBankInfoId</code> property.
    *
    * @param stakeholderBankInfoId the value for the <code>stakeholderBankInfoId</code> property
    *    
		    */

   public void setStakeholderBankInfoId(Long stakeholderBankInfoId) {
      this.stakeholderBankInfoId = stakeholderBankInfoId;
   }

   /**
    * Returns the value of the <code>accountNo</code> property.
    *
    */
      @Column(name = "ACCOUNT_NO" , nullable = false , length=50 )
   public String getAccountNo() {
      return accountNo;
   }

   /**
    * Sets the value of the <code>accountNo</code> property.
    *
    * @param accountNo the value for the <code>accountNo</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAccountNo(String accountNo) {
      this.accountNo = accountNo;
   }

   /**
    * Returns the value of the <code>accountTitle</code> property.
    *
    */
      @Column(name = "ACCOUNT_TITLE" , nullable = false , length=50 )
   public String getAccountTitle() {
      return accountTitle;
   }

   /**
    * Sets the value of the <code>accountTitle</code> property.
    *
    * @param accountTitle the value for the <code>accountTitle</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    */

   public void setAccountTitle(String accountTitle) {
      this.accountTitle = accountTitle;
   }

   /**
    * Returns the value of the <code>active</code> property.
    *
    */
      @Column(name = "IS_ACTIVE" , nullable = false )
   public Boolean getActive() {
      return active;
   }

   /**
    * Sets the value of the <code>active</code> property.
    *
    * @param active the value for the <code>active</code> property
    *    
		    */

   public void setActive(Boolean active) {
      this.active = active;
   }

   /**
    * Returns the value of the <code>acctTypeName</code> property.
    *
    */
      @Column(name = "ACCT_TYPE_NAME" , nullable = false , length=50 )
   public String getAcctTypeName() {
      return acctTypeName;
   }

   /**
    * Sets the value of the <code>acctTypeName</code> property.
    *
    * @param acctTypeName the value for the <code>acctTypeName</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setAcctTypeName(String acctTypeName) {
      this.acctTypeName = acctTypeName;
   }

   /**
    * Returns the value of the <code>acctTypeId</code> property.
    *
    */
      @Column(name = "ACCT_TYPE_ID" , nullable = false )
   public Long getAcctTypeId() {
      return acctTypeId;
   }

   /**
    * Sets the value of the <code>acctTypeId</code> property.
    *
    * @param acctTypeId the value for the <code>acctTypeId</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setAcctTypeId(Long acctTypeId) {
      this.acctTypeId = acctTypeId;
   }

   /**
    * Returns the value of the <code>shName</code> property.
    *
    */
      @Column(name = "SH_NAME"  , length=50 )
   public String getShName() {
      return shName;
   }

   /**
    * Sets the value of the <code>shName</code> property.
    *
    * @param shName the value for the <code>shName</code> property
    *    
		    * @spring.validator type="maxlength"     
    * @spring.validator-args arg1value="${var:maxlength}"
    * @spring.validator-var name="maxlength" value="50"
    * @spring.validator type="mask"
    * @spring.validator-args arg1value="${mask}"
    * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
    */

   public void setShName(String shName) {
      this.shName = shName;
   }

   /**
    * Returns the value of the <code>shId</code> property.
    *
    */
      @Column(name = "SH_ID" , nullable = false )
   public Long getShId() {
      return shId;
   }

   /**
    * Sets the value of the <code>shId</code> property.
    *
    * @param shId the value for the <code>shId</code> property
    *    
		    * @spring.validator type="required"
    * @spring.validator type="long"
    * @spring.validator type="longRange"		
    * @spring.validator-args arg1value="${var:min}"
    * @spring.validator-var name="min" value="0"
    * @spring.validator-args arg2value="${var:max}"
    * @spring.validator-var name="max" value="9999999999"			
    */

   public void setShId(Long shId) {
      this.shId = shId;
   }




    /**
     * Used by the display tag library for rendering a checkbox in the list.
     * @return String with a HTML checkbox.
     */
    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getStakeholderBankInfoId();
        checkBox += "\"/>";
        return checkBox;
    }

   /**
    * Helper method for Struts with displaytag
    */
   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&stakeholderBankInfoId=" + getStakeholderBankInfoId();
      return parameters;
   }
	/**
     * Helper method for default Sorting on Primary Keys
     */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "stakeholderBankInfoId";
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
      	
      	associationModel.setClassName("StakeholderBankInfoModel");
      	associationModel.setPropertyName("relationOfSettlementStakeholderBankInfoModel");   		
    	associationModel.setValue(getRelationOfSettlementStakeholderBankInfoModel());
     		
     	associationModelList.add(associationModel);
     	
     	associationModel = new AssociationModel();
        	
    	associationModel.setClassName("StakeholderBankInfoModel");
    	associationModel.setPropertyName("relationT24StakeholderBankInfoModel");   		
  		associationModel.setValue(getRelationT24StakeholderBankInfoModel());
       		
       	associationModelList.add(associationModel);
    	   	    	
    	return associationModelList;
    }
    @Column(name = "BANK_ID")
	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	@Transient
	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	  /**
	 * @return the ofSettlementStakeholderBankInfoModel
	 */
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "OF_SETT_ACC_ID")    
	public StakeholderBankInfoModel getRelationOfSettlementStakeholderBankInfoModel() {
		return ofSettlementStakeholderBankInfoModel;
	}
	   
	   @javax.persistence.Transient
	 public StakeholderBankInfoModel getOfSettlementStakeholderBankInfoModel() {
		return getRelationOfSettlementStakeholderBankInfoModel();
	}   
	@javax.persistence.Transient
	   public void setRelationOfSettlementStakeholderBankInfoModel(StakeholderBankInfoModel stakeholderBankInfoModel) {
		ofSettlementStakeholderBankInfoModel= stakeholderBankInfoModel;
	}   
	
	@javax.persistence.Transient
	public void setOfSettlementStakeholderBankInfoModel(StakeholderBankInfoModel ofSettlementStakeholderBankInfoModel) {
	   if(null != ofSettlementStakeholderBankInfoModel)
	   {
		   setRelationOfSettlementStakeholderBankInfoModel((StakeholderBankInfoModel)ofSettlementStakeholderBankInfoModel.clone());
	   }      
	}

	@javax.persistence.Transient
	public Long getOfSettlementStakeholderBankInfoModelId() {
	   if (ofSettlementStakeholderBankInfoModel != null) {
	      return ofSettlementStakeholderBankInfoModel.getStakeholderBankInfoId();
	   } else {
	      return null;
	   }
	}
																				   
	@javax.persistence.Transient
	public void setOfSettlementStakeholderBankInfoModelId(Long id) {
	   if(id == null)
	   {      
		   ofSettlementStakeholderBankInfoModel = null;
	   }
	   else
	   {
		   ofSettlementStakeholderBankInfoModel = new StakeholderBankInfoModel();
		   ofSettlementStakeholderBankInfoModel.setStakeholderBankInfoId(id);
	   }      
	}
	@javax.persistence.Transient
	public String getOfSettlementStakeholderBankInfoModelAccountTitle() {
	   if (ofSettlementStakeholderBankInfoModel != null) {
	      return ofSettlementStakeholderBankInfoModel.getName();
	   } else {
	      return null;
	   }
	}
																				   
	@javax.persistence.Transient
	public void setOfSettlementStakeholderBankInfoModelAccountTitle(String accName) {
	   if(accName == null)
	   {      
		   ofSettlementStakeholderBankInfoModel = null;
	   }
	   else
	   {
		   ofSettlementStakeholderBankInfoModel = new StakeholderBankInfoModel();
		   ofSettlementStakeholderBankInfoModel.setName(accName);
	   }      
	}
	@javax.persistence.Transient
	public String getOfSettlementStakeholderBankInfoModelAccountNo() {
	   if (ofSettlementStakeholderBankInfoModel != null) {
	      return ofSettlementStakeholderBankInfoModel.getAccountNo();
	   } else {
	      return null;
	   }
	}
																				   
	@javax.persistence.Transient
	public void setOfSettlementStakeholderBankInfoModelAccountNo(String accNo) {
	   if(accNo == null)
	   {      
		   ofSettlementStakeholderBankInfoModel = null;
	   }
	   else
	   {
		   ofSettlementStakeholderBankInfoModel = new StakeholderBankInfoModel();
		   ofSettlementStakeholderBankInfoModel.setAccountNo(accNo);
	   }      
	}


	/**
	 * @return the t24StakeholderBankInfoModel
	 */
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "T24_ACC_ID")
	public StakeholderBankInfoModel getRelationT24StakeholderBankInfoModel(){
	      return t24StakeholderBankInfoModel ;
	   }
	
	@javax.persistence.Transient
	public StakeholderBankInfoModel getT24StakeholderBankInfoModel() {
		return getRelationT24StakeholderBankInfoModel();
	}

	/**
	 * @param t24StakeholderBankInfoModel the t24StakeholderBankInfoModel to set
	 */
	@javax.persistence.Transient
	public void setT24StakeholderBankInfoModel(StakeholderBankInfoModel t24StakeholderBankInfoModel) {
		 if(null != t24StakeholderBankInfoModel)
		   {
			 setRelationT24StakeholderBankInfoModel((StakeholderBankInfoModel)t24StakeholderBankInfoModel.clone());
		   } 
	}
	 @javax.persistence.Transient
	   public void setRelationT24StakeholderBankInfoModel(StakeholderBankInfoModel t24StakeholderBankInfoModel) {
	      this.t24StakeholderBankInfoModel = t24StakeholderBankInfoModel;
	   }
	   
	
	@javax.persistence.Transient
	public Long getT24StakeholderBankInfoModelId() {
	   if (t24StakeholderBankInfoModel != null) {
	      return t24StakeholderBankInfoModel.getStakeholderBankInfoId();
	   } else {
	      return null;
	   }
	}
																				   
	@javax.persistence.Transient
	public void setT24StakeholderBankInfoModelId(Long id) {
	   if(id == null)
	   {      
		   t24StakeholderBankInfoModel = null;
	   }
	   else
	   {
		   t24StakeholderBankInfoModel = new StakeholderBankInfoModel();
		   t24StakeholderBankInfoModel.setStakeholderBankInfoId(id);
	   }      
	}
	@javax.persistence.Transient
	public String getT24StakeholderBankInfoModelAccountTitle() {
	   if (t24StakeholderBankInfoModel != null) {
	      return t24StakeholderBankInfoModel.getName();
	   } else {
	      return null;
	   }
	}
																				   
	@javax.persistence.Transient
	public void setT24StakeholderBankInfoModelAccountTitle(String  title) {
	   if(title == null)
	   {      
		   t24StakeholderBankInfoModel = null;
	   }
	   else
	   {
		   t24StakeholderBankInfoModel = new StakeholderBankInfoModel();
		   t24StakeholderBankInfoModel.setName(title);;
	   }      
	}
	@javax.persistence.Transient
	public String getT24StakeholderBankInfoModelAccountNo() {
	   if (t24StakeholderBankInfoModel != null) {
	      return t24StakeholderBankInfoModel.getAccountNo();
	   } else {
	      return null;
	   }
	}
																				   
	@javax.persistence.Transient
	public void setT24StakeholderBankInfoModelAccountNo(String  accNo) {
	   if(accNo == null)
	   {      
		   t24StakeholderBankInfoModel = null;
	   }
	   else
	   {
		   t24StakeholderBankInfoModel = new StakeholderBankInfoModel();
		   t24StakeholderBankInfoModel.setAccountNo(accNo);
	   }      
	}
	


	@Column( name = "CUSTOMER_ACCOUNT_TYPE_ID" )
	public Long getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(Long accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	@Column( name = "CUSTOMER_ACCOUNT_TYPE_NAME" )
	public String getAccountTypeName() {
		return accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}

	@Column( name = "PRODUCT_ID" )
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	@Column( name = "PRODUCT_NAME" )
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}
