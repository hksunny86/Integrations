package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "BULK_FRANCHISE_seq",sequenceName = "BULK_FRANCHISE_seq", allocationSize=1)
@Table(name = "BULK_FRANCHISE")
public class BulkFranchiseModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long bulkFranchiseId;
	private String serialNo;
	private Long distributorId;
	private String distributorName;
	private Long regionModelId;
	private String regionName;
	private String name;
	private String contactName;
	private String state;
	private String city;
	private String address1;
	private String address2;
	private String phoneNo;
	private String fax;
	private String zip;
	private String email;
	private String description;
	private String comments;
	private Boolean active;
	private String groupEmail;
	private String groupDesc;
	private Boolean groupActive;
	private Boolean permissionCreate;
	private Boolean permissionRead;
	private Boolean permissionUpdate;
	private Boolean permissionDelete;
	private Long createdBy;
	private String createdByName;
	private Date createdOn;
	private Long updatedBy;
	private Date updatedOn;
	private String result;
	private String failureReason;
	private Long versionNo;

	private Date startDate;
    private Date endDate;

	public BulkFranchiseModel() {
	}   

    /**
     * Return the primary key.
     *
     * @return Long with the primary key.
     */
   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getBulkFranchiseId();
    }

    /**
     * Set the primary key.
     *
     * @param primaryKey the primary key
     */
   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setBulkFranchiseId(primaryKey);
    }

   @Column(name = "BULK_FRANCHISE_ID" , nullable = false )
   @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BULK_FRANCHISE_seq")
   public Long getBulkFranchiseId() {
      return bulkFranchiseId;
   }

   public void setBulkFranchiseId(Long bulkFranchiseId) {
      this.bulkFranchiseId = bulkFranchiseId;
   }

    @Transient
    public String getCheckbox() {
        String checkBox = "<input type=\"checkbox\" name=\"checkbox";
        checkBox += "_"+ getBulkFranchiseId();
        checkBox += "\"/>";
        return checkBox;
    }

   @javax.persistence.Transient
   public String getPrimaryKeyParameter() {
      String parameters = "";
      parameters += "&bulkFranchiseId=" + getBulkFranchiseId();
      return parameters;
   }

   @javax.persistence.Transient
    public String getPrimaryKeyFieldName()
    { 
			String primaryKeyFieldName = "bulkFranchiseId";
			return primaryKeyFieldName;				
    }

   	@Column(name = "SERIAL_NO")
	public String getSerialNo() {
		return serialNo;
	}
	
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
   	@Column(name = "FRANCHISE_NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
   	@Column(name = "CONTACT_NAME")
	public String getContactName() {
		return contactName;
	}
	
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
   	@Column(name = "PROVINCE")
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
   	@Column(name = "CITY")
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
   	@Column(name = "CURRENT_ADDRESS")
	public String getAddress1() {
		return address1;
	}
	
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
   	@Column(name = "PERMANENT_ADDRESS")
	public String getAddress2() {
		return address2;
	}
	
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
   	@Column(name = "CONTACT_NO")
	public String getPhoneNo() {
		return phoneNo;
	}
	
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
   	@Column(name = "FAX")
	public String getFax() {
		return fax;
	}
	
	public void setFax(String fax) {
		this.fax = fax;
	}
	
   	@Column(name = "ZIP_CODE")
	public String getZip() {
		return zip;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}
	
   	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
   	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
   	@Column(name = "COMMENTS")
	public String getComments() {
		return comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}
	
   	@Column(name = "IS_ACTIVE")
	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
   	@Column(name = "GROUP_EMAIL")
	public String getGroupEmail() {
		return groupEmail;
	}
	
	public void setGroupEmail(String groupEmail) {
		this.groupEmail = groupEmail;
	}
	
   	@Column(name = "GROUP_DESCRIPTION")
	public String getGroupDesc() {
		return groupDesc;
	}
	
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}
	
   	@Column(name = "GROUP_ACTIVE")
	public Boolean getGroupActive() {
		return groupActive;
	}
	
	public void setGroupActive(Boolean groupActive) {
		this.groupActive = groupActive;
	}
	
   	@Column(name = "CREATE_PERMISSION")
	public Boolean getPermissionCreate() {
		return permissionCreate;
	}
	
	public void setPermissionCreate(Boolean permissionCreate) {
		this.permissionCreate = permissionCreate;
	}
	
   	@Column(name = "READ_PERMISSION")
	public Boolean getPermissionRead() {
		return permissionRead;
	}
	
	public void setPermissionRead(Boolean permissionRead) {
		this.permissionRead = permissionRead;
	}
	
   	@Column(name = "UPDATE_PERMISSION")
	public Boolean getPermissionUpdate() {
		return permissionUpdate;
	}
	
	public void setPermissionUpdate(Boolean permissionUpdate) {
		this.permissionUpdate = permissionUpdate;
	}
	
   	@Column(name = "DELETE_PERMISSION")
	public Boolean getPermissionDelete() {
		return permissionDelete;
	}
	
	public void setPermissionDelete(Boolean permissionDelete) {
		this.permissionDelete = permissionDelete;
	}
	
   	@Column(name = "CREATED_BY")
	public Long getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	
   	@Column(name = "CREATED_BY_NAME")
	public String getCreatedByName() {
		return createdByName;
	}
	
	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}
	
   	@Column(name = "CREATED_ON")
	public Date getCreatedOn() {
		return createdOn;
	}
	
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
   	@Column(name = "RESULT")
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
   	@Column(name = "FAILURE_REASON")
	public String getFailureReason() {
		return failureReason;
	}
	
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	
	@Version
   	@Column(name = "version_no")
	public Long getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Long versionNo) {
		this.versionNo = versionNo;
	}

   	@Column(name = "DISTRIBUTOR_ID")
	public Long getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Long distributorId) {
		this.distributorId = distributorId;
	}

   	@Column(name = "DISTRIBUTOR_NAME")
	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

   	@Column(name = "REGION_ID")
	public Long getRegionModelId() {
		return regionModelId;
	}

	public void setRegionModelId(Long regionModelId) {
		this.regionModelId = regionModelId;
	}

   	@Column(name = "REGION_NAME")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

   	@Column(name = "UPDATED_BY")
	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

   	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

    @Transient
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( Date startDate )
    {
        this.startDate = startDate;
    }

    @Transient
    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate( Date endDate )
    {
        this.endDate = endDate;
    }

    @Transient
    public String getActiveAsString()
    {
        return active? "Yes" : "No";
    }
}
