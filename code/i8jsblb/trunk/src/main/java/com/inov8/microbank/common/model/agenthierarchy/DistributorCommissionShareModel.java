package com.inov8.microbank.common.model.agenthierarchy;

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
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.ProductModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DISTRIBUTOR_COMM_SHARE_seq", sequenceName = "DISTRIBUTOR_COMM_SHARE_seq", allocationSize = 1)
@Table(name = "DISTRIBUTOR_COMM_SHARE")
public class DistributorCommissionShareModel extends BasePersistableModel implements Serializable {

	private Long distributorCommissionShareId;
	private ProductModel productIdModel;
	private DistributorModel distributorIdModel;
	private RegionModel regionIdModel;
	private DistributorLevelModel currentDistributorLevelIdModel;
	private Double commissionShare;
	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;
	private Date createdOn;
	private Date updatedOn;
	private Integer versionNo;

	public DistributorCommissionShareModel() {
		// TODO Auto-generated constructor stub
	}

	@Column(name = "DISTRIBUTOR_COMM_SHARE_ID", nullable = false)
	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DISTRIBUTOR_COMM_SHARE_seq")
	public Long getDistributorCommissionShareId() {
		return distributorCommissionShareId;
	}

	public void setDistributorCommissionShareId(
			Long distributorCommissionShareId) {
		this.distributorCommissionShareId = distributorCommissionShareId;
	}

   @javax.persistence.Transient
   public Long getPrimaryKey() {
        return getDistributorCommissionShareId();
    }

   @javax.persistence.Transient
   public void setPrimaryKey(Long primaryKey) {
       setDistributorCommissionShareId(primaryKey);
    }
   
   /**
    * Used by the display tag library for rendering a checkbox in the list.
    * @return String with a HTML checkbox.
    */
   @Transient
   public String getCheckbox() {
       String checkBox = "<input type=\"checkbox\" name=\"checkbox";
       checkBox += "_"+ getDistributorCommissionShareId();
       checkBox += "\"/>";
       return checkBox;
   }

  @javax.persistence.Transient
  public String getPrimaryKeyParameter() {
     String parameters = "";
     parameters += "&distributorCommissionShareId=" + getDistributorCommissionShareId();
     return parameters;
  }
  
   @javax.persistence.Transient
   public String getPrimaryKeyFieldName()
   { 
			String primaryKeyFieldName = "distributorCommissionShareId";
			return primaryKeyFieldName;				
   }
   
   ////////////////

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	public ProductModel getProductIdModel() {
		return productIdModel;
	}

	public void setProductIdModel(ProductModel productIdModel) {
		this.productIdModel = productIdModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "DISTRIBUTOR_ID")
	public DistributorModel getDistributorIdModel() {
		return distributorIdModel;
	}

	public void setDistributorIdModel(DistributorModel distributorIdModel) {
		this.distributorIdModel = distributorIdModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENT_DISTRIBUTOR_LEVEL_ID")
	public DistributorLevelModel getCurrentDistributorLevelIdModel() {
		return currentDistributorLevelIdModel;
	}

	public void setCurrentDistributorLevelIdModel(
			DistributorLevelModel currentDistributorLevelIdModel) {
		this.currentDistributorLevelIdModel = currentDistributorLevelIdModel;
	}

	@Column(name = "COMMISSION_SHARE", nullable = false)
	public Double getCommissionShare() {
		return commissionShare;
	}

	public void setCommissionShare(Double commissionShare) {
		this.commissionShare = commissionShare;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getUpdatedByAppUserModel() {
		return updatedByAppUserModel;
	}

	public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel) {
		this.updatedByAppUserModel = updatedByAppUserModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getCreatedByAppUserModel() {
		return createdByAppUserModel;
	}

	public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel) {
		this.createdByAppUserModel = createdByAppUserModel;
	}

	@Column(name = "CREATED_ON", nullable = false)
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "UPDATED_ON", nullable = false)
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Version
	@Column(name = "VERSION_NO", nullable = false)
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}
	
	@Transient
	public Long getDistributorId() {
		if (distributorIdModel == null) {
			return null;
		} else {
			return distributorIdModel.getDistributorId();
		}
	}
	
	@Transient
	public void setDistributorId(Long distributorId) {
		if (distributorIdModel != null) {
			distributorIdModel.setDistributorId(distributorId);
		} else {
			distributorIdModel = new DistributorModel();
			distributorIdModel.setDistributorId(distributorId);
		}
	}

	@Transient
	public Long getProductId() {
		if (productIdModel == null) {
			return null;
		} else {
			return productIdModel.getProductId();
		}
	}
	
	@Transient
	public void setProductId(Long productId) {
		if(productId == null)
	    {      
			productIdModel = null;
	    }
	    else
	    {
	    	productIdModel = new ProductModel(productId);
	    }
	}

	@Transient
	public Long getCurrentDistributorLevelId() {
		if (currentDistributorLevelIdModel == null) {
			return null;
		} else {
			return currentDistributorLevelIdModel.getDistributorLevelId();
		}
	}
	
	@Transient
	public void setCurrentDistributorLevelId(Long currentDistributorLevelId)
	{
		if (currentDistributorLevelId == null) {
			currentDistributorLevelIdModel = null;
		} else {
			currentDistributorLevelIdModel = new DistributorLevelModel();
			currentDistributorLevelIdModel.setDistributorLevelId(currentDistributorLevelId);
		}
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "REGION_ID")
    public RegionModel getRegionIdModel() {
		return regionIdModel;
	}

	public void setRegionIdModel(RegionModel regionModelIdModel) {
		this.regionIdModel = regionModelIdModel;
	}
	
	@Transient
    public Long getRegionId() {
		if (regionIdModel == null) {
			return null;
		} else {
			return regionIdModel.getRegionId();
		}
	}

	public void setRegionId(Long regionId) {
		if (regionIdModel != null) {
			regionIdModel.setRegionId(regionId);
		} else {
			regionIdModel = new RegionModel();
			regionIdModel.setRegionId(regionId);
		}
	}
	
		
	///////////////////////////

	@javax.persistence.Transient
    @Override
    public List<AssociationModel> getAssociationModelList()
    {
    	List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();    	
    	AssociationModel associationModel = null;    	
    	
    	associationModel = new AssociationModel();
    	associationModel.setClassName("ProductModel");
    	associationModel.setPropertyName("productIdModel");   		
   		associationModel.setValue(getProductIdModel());
   		associationModelList.add(associationModel);
   		

    	associationModel = new AssociationModel();
    	associationModel.setClassName("DistributorModel");
    	associationModel.setPropertyName("distributorIdModel");   		
   		associationModel.setValue(getDistributorIdModel());
   		associationModelList.add(associationModel);

    	associationModel = new AssociationModel();
    	associationModel.setClassName("DistributorLevelModel");
    	associationModel.setPropertyName("currentDistributorLevelIdModel");   		
   		associationModel.setValue(getCurrentDistributorLevelIdModel());
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	associationModel.setClassName("RegionModel");
    	associationModel.setPropertyName("regionIdModel");   		
   		associationModel.setValue(getRegionIdModel());
   		associationModelList.add(associationModel);

    	return associationModelList;
    }
	
	
}