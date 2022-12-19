package com.inov8.microbank.common.model.productmodule;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.inov8.framework.common.model.BasePersistableModel;


/**
 * MenuLevelModel entity. 
 * @author Rashid Mahmood
 * @since  December 2013
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "MENU_LEVEL_SEQ",sequenceName = "MENU_LEVEL_SEQ", allocationSize=1)
@Table(name="MENU_LEVEL")

public class MenuLevelModel  extends BasePersistableModel implements Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long menuLevelId;
	private MenuLevelModel parentMenuLevelModel;
     private String name;
     private String description;
     private Long createdBy;
     private Long updatedBy;
     private Date createdOn;
     private Date updatedOn;
     private Short version;


   
    // Property accessors
    @Id 
    @Column(name="MENU_LEVEL_ID", unique=true, nullable=false, precision=10, scale=0)

    public Long getMenuLevelId() {
        return this.menuLevelId;
    }
    
    public void setMenuLevelId(Long menuLevelId) {
        this.menuLevelId = menuLevelId;
    }
    
    @Column(name="NAME", length=100)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="DESCRIPTION", length=500)

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="CREATED_BY", precision=10, scale=0)

    public Long getCreatedBy() {
        return this.createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    
    @Column(name="UPDATED_BY", precision=10, scale=0)
    public Long getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="CREATED_ON", length=7)

    public Date getCreatedOn() {
        return this.createdOn;
    }
    
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="UPDATED_ON", length=7)

    public Date getUpdatedOn() {
        return this.updatedOn;
    }
    
    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    
    @Column(name="VERSION", precision=4, scale=0)
    @Version
    public Short getVersion() {
        return this.version;
    }
    
    public void setVersion(Short version) {
        this.version = version;
    }

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_MENU_LEVEL_ID")
    public MenuLevelModel getParentMenuLevelModel() {
		return parentMenuLevelModel;
	}

	public void setParentMenuLevelModel(MenuLevelModel parentMenuLevelModel) {
		this.parentMenuLevelModel = parentMenuLevelModel;
	}

	
	@javax.persistence.Transient
	public Long getParentMenuLevelId() 
	{
		if (parentMenuLevelModel != null) 
		{
			return parentMenuLevelModel.getMenuLevelId();
		} 
		else 
		{
			return null;
		}
	}
    
	@javax.persistence.Transient
	public String getParentMenuLevelName() 
	{
		if (parentMenuLevelModel != null) 
		{
			return parentMenuLevelModel.getName();
		} 
		else 
		{
			return null;
		}
	}
   
	@javax.persistence.Transient
	public void setParentMenuLevelId(Long menuLevelId) 
	{
		if(menuLevelId == null)
		{      
			this.parentMenuLevelModel = null;
		}
		else
		{
			parentMenuLevelModel = new MenuLevelModel();
			parentMenuLevelModel.setMenuLevelId(menuLevelId);
		}      
	}
	
	
	@javax.persistence.Transient
	public Long getPrimaryKey() {
    	return getMenuLevelId();
	}

    @javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "menuLevelId";
		return primaryKeyFieldName;
	}

    @javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
	      parameters += "&menuLevelId=" + getMenuLevelId();
	      return parameters;
	}

    @javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) 
	{
    	setMenuLevelId(primaryKey);
	}
    
}