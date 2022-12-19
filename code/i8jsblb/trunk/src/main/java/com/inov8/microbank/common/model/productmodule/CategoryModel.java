package com.inov8.microbank.common.model.productmodule;



import java.io.Serializable;	
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.springframework.web.multipart.MultipartFile;

import com.inov8.framework.common.model.BasePersistableModel;



/**
 * CategoryModel entity. 
 * @author : Rashid Mahmood
 * @since  December 2013
 */

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "CATEGORY_SEQ",sequenceName = "CATEGORY_SEQ", allocationSize=1)
@Table(name="CATEGORY")

public class CategoryModel  extends BasePersistableModel implements Serializable {

    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long categoryId;
     private CategoryModel parentCategoryModel;
     private CategoryModel ultimateParentCategoryModel;
     private MenuLevelModel menuLevelModel;
     private String name;
     private byte[] icon;
     private MultipartFile file; 
     private String description;
     private Long createdBy;
     private Long updateBy;
     private Date createdOn;
     private Date updatedOn;
     private Short versionNo;
     private String iconExt;
	 private String iconLocation;
     private boolean removeIcon;
     private Boolean isProduct;


    @Id 
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATEGORY_SEQ")
    @Column(name="CATEGORY_ID", unique=true, nullable=false, precision=10, scale=0)
    public Long getCategoryId() {
        return this.categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @javax.persistence.Transient
	public Long getPrimaryKey() {
		return this.getCategoryId();
	}

    @javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "categoryId";
		return primaryKeyFieldName;
	}

    @javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
	      parameters += "&categoryId=" + getCategoryId();
	      return parameters;
	}

    @javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) 
	{
		setCategoryId(primaryKey);
	}

	@Column(name="NAME", length=500)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
        
    @Column(name="DESCRIPTION", length=100)

    public String getDescription() {
        return this.description;
    }
    
    @Column(name="ICON")
    public byte[] getIcon() {
		return icon;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
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
    
    @Column(name="UPDATE_BY", precision=10, scale=0)

    public Long getUpdateBy() {
        return this.updateBy;
    }
    
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
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
    
    @Version
    @Column(name="VERSION_NO", precision=4, scale=0)
    public Short getVersionNo() {
        return this.versionNo;
    }
    
    public void setVersionNo(Short versionNo) {
        this.versionNo = versionNo;
    }


    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_CATEGORY_ID")
	public CategoryModel getParentCategoryModel() {
		return parentCategoryModel;
	}

	public void setParentCategoryModel(CategoryModel parentCategoryModel) {
		this.parentCategoryModel = parentCategoryModel;
	}

	@javax.persistence.Transient
	public Long getParentCategoryId() 
	{
		if (parentCategoryModel != null) 
		{
			return parentCategoryModel.getCategoryId();
		} 
		else 
		{
			return null;
		}
	}
    
	@javax.persistence.Transient
	public String getParentCategoryName() 
	{
		if (parentCategoryModel != null) 
		{
			return parentCategoryModel.getName();
		} 
		else 
		{
			return null;
		}
	}
   
	@javax.persistence.Transient
	public void setParentCategoryId(Long categoryId) 
	{
		if(categoryId == null)
		{      
			this.parentCategoryModel = null;
		}
		else
		{
			parentCategoryModel = new CategoryModel();
			parentCategoryModel.setCategoryId(categoryId);
		}      
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ULTIMATE_PARENT_CATEGORY_ID")
	public CategoryModel getUltimateParentCategoryModel() {
		return ultimateParentCategoryModel;
	}

	public void setUltimateParentCategoryModel(
			CategoryModel ultimateParentCategoryModel) {
		this.ultimateParentCategoryModel = ultimateParentCategoryModel;
	}

	@javax.persistence.Transient
	public Long getUltimateParentCategoryId() 
	{
		if (ultimateParentCategoryModel != null) 
		{
			return ultimateParentCategoryModel.getCategoryId();
		} 
		else 
		{
			return null;
		}
	}
    
	@javax.persistence.Transient
	public String getUltimateParentCategoryName() 
	{
		if (ultimateParentCategoryModel != null) 
		{
			return ultimateParentCategoryModel.getName();
		} 
		else 
		{
			return null;
		}
	}
   
	@javax.persistence.Transient
	public void setUltimateParentCategoryId(Long categoryId) 
	{
		if(categoryId == null)
		{      
			this.ultimateParentCategoryModel = null;
		}
		else
		{
			ultimateParentCategoryModel = new CategoryModel();
			ultimateParentCategoryModel.setCategoryId(categoryId);
		}      
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_LEVEL_ID")
	public MenuLevelModel getMenuLevelModel() {
		return menuLevelModel;
	}

	public void setMenuLevelModel(MenuLevelModel menuLevelModel) {
		this.menuLevelModel = menuLevelModel;
	}
   
	@javax.persistence.Transient
	public Long getMenuLevelId() 
	{
		if (menuLevelModel != null) 
		{
			return menuLevelModel.getMenuLevelId();
		} 
		else 
		{
			return null;
		}
	}
    
	@javax.persistence.Transient
	public String getMenuLevelName() 
	{
		if (menuLevelModel != null) 
		{
			return menuLevelModel.getName();
		} 
		else 
		{
			return null;
		}
	}
   
	@javax.persistence.Transient
	public void setMenuLevelId(Long menuLevelId) 
	{
		if(menuLevelId == null)
		{      
			this.menuLevelModel = null;
		}
		else
		{
			menuLevelModel = new MenuLevelModel();
			menuLevelModel.setMenuLevelId(menuLevelId);
		}      
	}

	@javax.persistence.Transient
	public MultipartFile getFile() {
		return file;
	}

	@javax.persistence.Transient
	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@Column(name="ICON_EXTENTION", length=10)
	public String getIconExt() {
		return iconExt;
	}

	public void setIconExt(String iconExt) {
		this.iconExt = iconExt;
	}

	@javax.persistence.Transient
	public boolean isRemoveIcon() {
		return removeIcon;
	}

	@javax.persistence.Transient
	public void setRemoveIcon(boolean removeIcon) {
		this.removeIcon = removeIcon;
	}

	@Column(name = "IS_PRODUCT")
	public Boolean getIsProduct() {
		return isProduct;
	}

	public void setIsProduct(Boolean isProduct) {
		this.isProduct = isProduct;
	}

	@Column(name = "ICON_LOCATION")
	public String getIconLocation() {
		return iconLocation;
	}

	public void setIconLocation(String iconLocation) {
		this.iconLocation = iconLocation;
	}

}