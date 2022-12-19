package com.inov8.microbank.account.model;

import java.io.Serializable;
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

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "BLCKLST_MARKUNMARK_HIST_SEQ",sequenceName = "BLCKLST_MARKUNMARK_HIST_SEQ")
@Table(name = "BLACKLIST_MARK_UNMARK_HISTORY")
public class BlacklistMarkUnmarkHistoryModel extends BasePersistableModel implements Serializable {

	
	 private AppUserModel updatedByAppUserModel;
	 private Long blacklistMarkUnmarkHistId;
	 private String cnicNo;
	 private Date updatedOn;
	 private String action;
	 private String comments;
	 private Date startDate;
	 private Date endDate;
	
	 
	
	 @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	 @JoinColumn(name = "UPDATED_BY")
	 public AppUserModel getUpdatedByAppUserModel()
	 {
	     return updatedByAppUserModel;
	 }

	 public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel)
	 {
	    this.updatedByAppUserModel = updatedByAppUserModel;
	 }

	 	 

	@Column(name = "BLKLST_MARK_UNMARK_HIST_ID" , nullable = false )
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="BLCKLST_MARKUNMARK_HIST_SEQ")
	public Long getBlacklistMarkUnmarkHistId() {
		return blacklistMarkUnmarkHistId;
	}

	public void setBlacklistMarkUnmarkHistId(Long blacklistMarkUnmarkHistId) {
		this.blacklistMarkUnmarkHistId = blacklistMarkUnmarkHistId;
	}
	
		
	@Column(name = "CNIC_NO" , nullable = false )
	public String getCnicNo() {
		return cnicNo;
	}

	public void setCnicNo(String cnicNo) {
		this.cnicNo = cnicNo;
	}
	
	@Column(name = "UPDATED_ON" , nullable = false )
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name="COMMENTS")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}  
	 
	 
	@Column(name = "ACTION" , nullable = false )
	 public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Transient
	    public String getUpdatedBy() {
	        if (updatedByAppUserModel != null) {
	            return updatedByAppUserModel.getUsername();
	        } else {
	            return null;
	        }
	    }

	    /**
	     * Sets the value of the <code>appUserId</code> property.
	     *
	     * @param appUserId the value for the <code>appUserId</code> property
	     */

	    @Transient
	    public void setUpdatedBy(String username) {
	        if(username == null)
	        {
	            updatedByAppUserModel = null;
	        }
	        else
	        {
	            updatedByAppUserModel = new AppUserModel();
	            updatedByAppUserModel.setUsername(username);
	        }
	    }
	    
		@javax.persistence.Transient
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

		@javax.persistence.Transient
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
	    
	    @javax.persistence.Transient
		public Long getPrimaryKey() {			
			return getBlacklistMarkUnmarkHistId();
		}
	    
	    @javax.persistence.Transient
		public void setPrimaryKey(Long primaryKey) {
	    	 this.setBlacklistMarkUnmarkHistId(primaryKey);
			
		}

	    @javax.persistence.Transient
		public String getPrimaryKeyFieldName() {
			 String primaryKeyFieldName = "blacklistMarkUnmarkHistId";
		        return primaryKeyFieldName;
		}

	    @javax.persistence.Transient
		public String getPrimaryKeyParameter() {
			String parameters = "";
	        parameters += "&blacklistMarkUnmarkHistId=" + getBlacklistMarkUnmarkHistId();
	        return parameters;
		}

		 @Transient
		 public List<AssociationModel> getAssociationModelList()
		 {
		        List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		        AssociationModel associationModel = new AssociationModel();

		        associationModel.setClassName("AppUserModel");
		        associationModel.setPropertyName("updatedByAppUserModel");
		        associationModel.setValue(getUpdatedByAppUserModel());

		        associationModelList.add(associationModel);

		        return associationModelList;
		}		
		
		
	 

}
