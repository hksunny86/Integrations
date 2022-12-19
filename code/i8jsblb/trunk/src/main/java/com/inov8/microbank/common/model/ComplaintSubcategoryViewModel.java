package com.inov8.microbank.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;


/**
 * HolidayModel entity.
 * @author Muhammad Omar Butt
 */
@Entity
@org.hibernate.annotations.Entity(mutable = false)
@Table(name = "COMPLAINT_SUBCATEGORY_VIEW")
public class ComplaintSubcategoryViewModel extends BasePersistableModel {

	private static final long serialVersionUID = 5442138156921908568L;
	private Long complaintSubcategoryId;
	private	String complaintSubcategoryName;
	private String complaintCategoryId;
    private String complaintCategoryName;
    private String description;
    private Short totalTat;
    private String level0AssigneeId;
    private String level0AssigneeName;
    private Short level0AssigneeTat;
    private String level1AssigneeId;
    private String level1AssigneeName;
    private Short level1AssigneeTat;
    private String level2AssigneeId;
    private String level2AssigneeName;
    private Short level2AssigneeTat;
    private String level3AssigneeId;
    private String level3AssigneeName;
    private Short level3AssigneeTat;
    private Boolean isActive;

    public ComplaintSubcategoryViewModel() {
    	
    }

    @javax.persistence.Transient
    public Long getPrimaryKey() {
    	return getComplaintSubcategoryId();
    }
    
    @javax.persistence.Transient
    public void setPrimaryKey(Long primaryKey) {
	   setComplaintSubcategoryId(primaryKey);
    }
    
    @Id
    @Column(name = "COMPLAINT_SUBCATEGORY_ID")
    public Long getComplaintSubcategoryId() {
        return this.complaintSubcategoryId;
    }
    
    public void setComplaintSubcategoryId(Long complaintSubcategoryId) {
        this.complaintSubcategoryId = complaintSubcategoryId;
    }
    
    @Column(name = "COMPLAINT_SUBCATEGORY_NAME")
	public String getComplaintSubcategoryName() {
		return complaintSubcategoryName;
	}

	public void setComplaintSubcategoryName(String complaintSubcategoryName) {
		this.complaintSubcategoryName = complaintSubcategoryName;
	}
    
	@Column(name = "COMPLAINT_CATEGORY_NAME")
	public String getComplaintCategoryName() {
		return complaintCategoryName;
	}

	public void setComplaintCategoryName(String complaintCategoryName) {
		this.complaintCategoryName = complaintCategoryName;
	}
	
	@Column(name = "COMPLAINT_CATEGORY_ID")
	public String getComplaintCategoryId() {
		return complaintCategoryId;
	}

	public void setComplaintCategoryId(String complaintCategoryId) {
		this.complaintCategoryId = complaintCategoryId;
	}
	
    @Column(name="DESCRIPTION")
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="TOTAL_TAT")
    public Short getTotalTat() {
        return this.totalTat;
    }
    
    public void setTotalTat(Short totalTat) {
        this.totalTat = totalTat;
    }
    
	@Column(name="LEVEL0_ASSIGNEE_ID")
	public String getLevel0AssigneeId() {
		return level0AssigneeId;
	}

	public void setLevel0AssigneeId(String level0AssigneeId) {
		this.level0AssigneeId = level0AssigneeId;
	}
    
    @Column(name="LEVEL0_ASSIGNEE_NAME")
	public String getLevel0AssigneeName() {
		return level0AssigneeName;
	}

	public void setLevel0AssigneeName(String level0AssigneeName) {
		this.level0AssigneeName = level0AssigneeName;
	}
    
    @Column(name="LEVEL0_ASSIGNEE_TAT")
    public Short getLevel0AssigneeTat() {
        return this.level0AssigneeTat;
    }
    
    public void setLevel0AssigneeTat(Short level0AssigneeTat) {
        this.level0AssigneeTat = level0AssigneeTat;
    }
    
	@Column(name="LEVEL1_ASSIGNEE_ID")
	public String getLevel1AssigneeId() {
		return level1AssigneeId;
	}

	public void setLevel1AssigneeId(String level1AssigneeId) {
		this.level1AssigneeId = level1AssigneeId;
	}
    
    @Column(name="LEVEL1_ASSIGNEE_NAME")
	public String getLevel1AssigneeName() {
		return level1AssigneeName;
	}

	public void setLevel1AssigneeName(String level1AssigneeName) {
		this.level1AssigneeName = level1AssigneeName;
	}
    
    @Column(name="LEVEL1_ASSIGNEE_TAT")
    public Short getLevel1AssigneeTat() {
        return this.level1AssigneeTat;
    }
    
    public void setLevel1AssigneeTat(Short level1AssigneeTat) {
        this.level1AssigneeTat = level1AssigneeTat;
    }
    
	@Column(name="LEVEL2_ASSIGNEE_ID")
	public String getLevel2AssigneeId() {
		return level2AssigneeId;
	}

	public void setLevel2AssigneeId(String level2AssigneeId) {
		this.level2AssigneeId = level2AssigneeId;
	}
    
    @Column(name="LEVEL2_ASSIGNEE_NAME")
	public String getLevel2AssigneeName() {
		return level2AssigneeName;
	}

	public void setLevel2AssigneeName(String level2AssigneeName) {
		this.level2AssigneeName = level2AssigneeName;
	}
    
    @Column(name="LEVEL2_ASSIGNEE_TAT")
    public Short getLevel2AssigneeTat() {
        return this.level2AssigneeTat;
    }
    
    public void setLevel2AssigneeTat(Short level2AssigneeTat) {
        this.level2AssigneeTat = level2AssigneeTat;
    }
    
	@Column(name="LEVEL3_ASSIGNEE_ID")
	public String getLevel3AssigneeId() {
		return level3AssigneeId;
	}

	public void setLevel3AssigneeId(String level3AssigneeId) {
		this.level3AssigneeId = level3AssigneeId;
	}
    
    @Column(name="LEVEL3_ASSIGNEE_NAME")
	public String getLevel3AssigneeName() {
		return level3AssigneeName;
	}

	public void setLevel3AssigneeName(String level3AssigneeName) {
		this.level3AssigneeName = level3AssigneeName;
	}
    
    @Column(name="LEVEL3_ASSIGNEE_TAT")
    public Short getLevel3AssigneeTat() {
        return this.level3AssigneeTat;
    }
    
    public void setLevel3AssigneeTat(Short level3AssigneeTat) {
        this.level3AssigneeTat = level3AssigneeTat;
    }
    
    @Column(name="IS_ACTIVE")
    public Boolean getIsActive() {
        return this.isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Helper method for Struts with displaytag
     */
    @javax.persistence.Transient
    public String getPrimaryKeyParameter() {
       String parameters = "";
       parameters += "&complaintSubcategoryId=" + getComplaintSubcategoryId();
       return parameters;
    }
    
 	/**
      * Helper method for default Sorting on Primary Keys
      */
    @javax.persistence.Transient
    public String getPrimaryKeyFieldName(){ 
    	String primaryKeyFieldName = "complaintSubcategoryId";
    	return primaryKeyFieldName;
    }
	
}