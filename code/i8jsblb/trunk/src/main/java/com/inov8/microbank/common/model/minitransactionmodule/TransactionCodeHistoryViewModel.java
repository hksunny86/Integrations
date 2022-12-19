package com.inov8.microbank.common.model.minitransactionmodule;
import java.io.Serializable;
import java.sql.Timestamp;
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

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

/**
 * The TrnsactionCodeHtrModel entity bean.
 *
 * @author  Hassan javaid  Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 20014/05/14
 *
 * @comments: sequence generator name changed
 * @spring.bean name="TrnsactionCodeHtrModel" 
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "TRANSACTION_CODE_HISTORY_SEQ",sequenceName = "TRANSACTION_CODE_HISTORY_SEQ")
@Table(name = "TRANSACTION_CODE_HISTORY_VIEW")
public class TransactionCodeHistoryViewModel extends BasePersistableModel implements Serializable {
	

		// Fields
		private static final long serialVersionUID = 6339939149579119361L;
		private Long transactionCodeHtrId;
		private String code;
		private Long usecaseId;
		private String usecaseName;
		private String comments;
		private AppUserModel createdByAppUserModel;
		private String username;
		

		private Date createdOn;

		// Constructors

		/** default constructor */
		public TransactionCodeHistoryViewModel() {
		}



		// Property accessors
		
		
		@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRANSACTION_CODE_HISTORY_SEQ")
		@Column(name = "TRANSACTION_CODE_HISTORY_ID", nullable = false)
		public Long getTransactionCodeHtrId() {
			return this.transactionCodeHtrId;
		}

		public void setTransactionCodeHtrId(Long transactionCodeHtrId) {
			this.transactionCodeHtrId = transactionCodeHtrId;
		}

		@Column(name = "CODE")
		public String getCode() {
			return this.code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		@Column(name = "USECASE_ID")
		public Long getUsecaseId() {
			return this.usecaseId;
		}

		public void setUsecaseId(Long resetTxCodeUsecaseId) {
			this.usecaseId = resetTxCodeUsecaseId;
		}
		@Column(name = "USECASE_NAME")
		public String getUsecaseName() {
			return usecaseName;
		}

		public void setUsecaseName(String usecaseName) {
			this.usecaseName = usecaseName;
		}

		@Column(name = "COMMENTS")
		public String getComments() {
			return this.comments;
		}

		public void setComments(String comments) {
			this.comments = comments;
		}

		@Column(name = "CREATED_ON")
		public Date getCreatedOn() {
			return this.createdOn;
		}

		public void setCreatedOn(Date createdOn) {
			this.createdOn = createdOn;
		}
		@Column(name = "USERNAME")
		public String getUsername() {
			return this.username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
		
		 @javax.persistence.Transient
		public Long getPrimaryKey() {
			return getTransactionCodeHtrId();
		}



		 @javax.persistence.Transient
		public String getPrimaryKeyFieldName() {
			String primaryKeyFieldName = "transactionCodeHtrId";
			return primaryKeyFieldName;	
		}



		 @javax.persistence.Transient
		public String getPrimaryKeyParameter() {
			String parameters = "";
		      parameters += "&transactionCodeHtrId=" + getTransactionCodeHtrId();
		      return parameters;
		}



		 @javax.persistence.Transient
		public void setPrimaryKey(Long primaryKey) {
			setTransactionCodeHtrId(primaryKey);		
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
	     	
		 	associationModel.setClassName("AppUserModel");
		 	associationModel.setPropertyName("relationCreatedByAppUserModel");   		
			associationModel.setValue(getRelationCreatedByAppUserModel());
			
			associationModelList.add(associationModel);
			return associationModelList;
	    }  
	    
	/////Account CreatedBy///////
		
		 @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
		   @JoinColumn(name = "CREATED_BY")    
		   public AppUserModel getRelationCreatedByAppUserModel(){
		      return createdByAppUserModel;
		   }
		   
		   @javax.persistence.Transient
		   public AppUserModel getCreatedByAppUserModel(){
		      return getRelationCreatedByAppUserModel();
		   }

		   
		   @javax.persistence.Transient
		   public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
		      this.createdByAppUserModel = appUserModel;
		   }
		   
		   
		   @javax.persistence.Transient
		   public void setCreatedByAppUserModel(AppUserModel appUserModel) {
		      if(null != appUserModel)
		      {
		      	setRelationCreatedByAppUserModel((AppUserModel)appUserModel.clone());
		      }      
		   }
		   @javax.persistence.Transient
		   public Long getCreatedById() {
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
		   public void setCreatedById(Long appUserId) {
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

	}	
	
	

	

	
	
	
	
	  


	  