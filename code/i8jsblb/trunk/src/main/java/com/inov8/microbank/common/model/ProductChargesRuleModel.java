package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;

/**
 * ProductChargesRule entity. @author MyEclipse Persistence Tools
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "PRODUCT_CHARGES_RULE")
@SequenceGenerator(name = "PRODUCT_CHARGES_RULE_seq",sequenceName = "PRODUCT_CHARGES_RULE_seq", allocationSize=2)
public class ProductChargesRuleModel extends BasePersistableModel implements Serializable
{
	private static final long serialVersionUID = -3930417941784120449L;

	private Long productChargesRuleId;
	private ProductModel productModel;
	private DeviceTypeModel deviceTypeModel;
	private SegmentModel segmentModel;
	private DistributorModel distributorModel;
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private MnoModel  mnoModelIdMnoModel;
	private Double rangeStarts;
	private Double rangeEnds;
	private Double exclusiveFixAmount;
	private Double exclusivePercentAmount;
	private Double inclusiveFixAmount;
	private Double inclusivePercentAmount;
	private Date createdOn;
	private Date updatedOn;
	private Long versionNo;
	private Boolean isDeleted;

	//--Service Operator Model
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE_OP_ID")
	public MnoModel getRelationMnoModel()
	{
		return mnoModelIdMnoModel;
	}

	@Transient
	public void setRelationMnoModel(MnoModel mnoModelIdMnoModel){
		this.mnoModelIdMnoModel = mnoModelIdMnoModel;
	}

	@Transient
	public MnoModel getMnoModel()
	{
		return getRelationMnoModel();
	}

	@Transient
	public void setMnoModel(MnoModel mnoModelIdMnoModel)
	{
		this.mnoModelIdMnoModel = mnoModelIdMnoModel;
	}

	@Transient
	public Long getMnoId()
	{
		if(mnoModelIdMnoModel != null)
			return mnoModelIdMnoModel.getMnoId();
		else
			return null;
	}

	@Transient
	public void setMnoId(Long mnoId)
	{
		if(mnoId == null)
			mnoModelIdMnoModel = null;
		else
		{
			mnoModelIdMnoModel = new MnoModel();
			mnoModelIdMnoModel.setMnoId(mnoId);
		}
	}

	// Constructors

	/** default constructor */
	public ProductChargesRuleModel()
	{
	}

	public ProductChargesRuleModel(Long productId)
	{
		setProductId(productId);
	}

	@Transient
	@Override
	public Long getPrimaryKey()
	{
		return getProductChargesRuleId();
	}

	@Transient
	@Override
	public String getPrimaryKeyFieldName()
	{
		return "productChargesRuleId";
	}

	@Transient
	@Override
	public String getPrimaryKeyParameter()
	{
		return "&productChargesRuleId="+getProductChargesRuleId();
	}

	@Override
	public void setPrimaryKey(Long primaryKey)
	{
		setProductChargesRuleId(primaryKey);
	}

	@Transient
	@Override
	public List<AssociationModel> getAssociationModelList()
	{
		List<AssociationModel> associationModelList = new ArrayList<>();

		AssociationModel associationModel = new AssociationModel();
		associationModel.setClassName("ProductModel");
		associationModel.setPropertyName("productModel");
		associationModel.setValue(getProductModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("DeviceTypeModel");
		associationModel.setPropertyName("deviceTypeModel");
		associationModel.setValue(getDeviceTypeModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("SegmentModel");
		associationModel.setPropertyName("segmentModel");
		associationModel.setValue(getSegmentModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("DistributorModel");
		associationModel.setPropertyName("distributorModel");
		associationModel.setValue(getDistributorModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("createdByAppUserModel");
		associationModel.setValue(getCreatedByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("updatedByAppUserModel");
		associationModel.setValue(getUpdatedByAppUserModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUCT_CHARGES_RULE_seq")
	@Column(name = "PRODUCT_CHARGES_RULE_ID")
	public Long getProductChargesRuleId()
	{
		return this.productChargesRuleId;
	}

	public void setProductChargesRuleId(Long productChargesRuleId)
	{
		this.productChargesRuleId = productChargesRuleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	public ProductModel getProductModel()
	{
		return this.productModel;
	}

	public void setProductModel(ProductModel productModel)
	{
		this.productModel = productModel;
	}

	@Transient
	public Long getProductId()
	{
		return productModel == null ? null : productModel.getProductId();		
	}

	public void setProductId(Long productId)
	{
		if(productId == null)
	    {      
			productModel = null;
	    }
	    else
	    {
	    	productModel = new ProductModel(productId);
	    }
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEVICE_TYPE_ID")
	public DeviceTypeModel getDeviceTypeModel()
	{
		return this.deviceTypeModel;
	}

	public void setDeviceTypeModel(DeviceTypeModel deviceTypeModel)
	{
		this.deviceTypeModel = deviceTypeModel;
	}

	@Transient
	public Long getDeviceTypeId()
	{
		return deviceTypeModel == null ? null : deviceTypeModel.getDeviceTypeId();		
	}

	public void setDeviceTypeId(Long deviceTypeId)
	{
		if(deviceTypeId == null)
	    {      
			deviceTypeModel = null;
	    }
	    else
	    {
	    	deviceTypeModel = new DeviceTypeModel(deviceTypeId);
	    }
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEGMENT_ID")
	public SegmentModel getSegmentModel()
	{
		return this.segmentModel;
	}

	public void setSegmentModel(SegmentModel segmentModel)
	{
		this.segmentModel = segmentModel;
	}

	@Transient
	public Long getSegmentId()
	{
		return segmentModel == null ? null : segmentModel.getSegmentId();		
	}

	public void setSegmentId(Long segmentId)
	{
		if(segmentId == null)
	    {      
			segmentModel = null;
	    }
	    else
	    {
	    	segmentModel = new SegmentModel(segmentId);
	    }
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DISTRIBUTOR_ID")
	public DistributorModel getDistributorModel()
	{
		return this.distributorModel;
	}

	public void setDistributorModel(DistributorModel distributorModel)
	{
		this.distributorModel = distributorModel;
	}

	@Transient
	public Long getDistributorId()
	{
		return distributorModel == null ? null : distributorModel.getDistributorId();
	}

	public void setDistributorId(Long distributorId)
	{
		if(distributorId == null)
	    {      
			distributorModel = null;
	    }
	    else
	    {
	    	distributorModel = new DistributorModel(distributorId);
	    }
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getCreatedByAppUserModel()
	{
		return this.createdByAppUserModel;
	}

	public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel)
	{
		this.createdByAppUserModel = createdByAppUserModel;
	}

	@Transient
	public Long getCreatedBy()
	{
		return createdByAppUserModel == null ? null : createdByAppUserModel.getAppUserId();	
	}

	public void setCreatedBy(Long appUserId)
	{
		if(appUserId == null)
	    {      
			createdByAppUserModel = null;
	    }
	    else
	    {
	    	createdByAppUserModel = new AppUserModel(appUserId);
	    }
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getUpdatedByAppUserModel()
	{
		return this.updatedByAppUserModel;
	}

	public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel)
	{
		this.updatedByAppUserModel = updatedByAppUserModel;
	}

	@Transient
	public Long getUpdatedBy()
	{
		return updatedByAppUserModel == null ? null : updatedByAppUserModel.getAppUserId();	
	}

	public void setUpdatedBy(Long appUserId)
	{
		if(appUserId == null)
	    {      
			updatedByAppUserModel = null;
	    }
	    else
	    {
	    	updatedByAppUserModel = new AppUserModel(appUserId);
	    }
	}

	@Column(name = "RANGE_STARTS")
	public Double getRangeStarts()
	{
		return this.rangeStarts;
	}

	public void setRangeStarts(Double rangeStarts)
	{
		this.rangeStarts = rangeStarts;
	}

	@Column(name = "RANGE_ENDS")
	public Double getRangeEnds()
	{
		return this.rangeEnds;
	}

	public void setRangeEnds(Double rangeEnds)
	{
		this.rangeEnds = rangeEnds;
	}

	@Column(name = "EXCLUSIVE_FIX_AMOUNT")
	public Double getExclusiveFixAmount()
	{
		return this.exclusiveFixAmount;
	}

	public void setExclusiveFixAmount(Double exclusiveFixAmount)
	{
		this.exclusiveFixAmount = exclusiveFixAmount;
	}

	@Column(name = "EXCLUSIVE_PERCENT_AMOUNT")
	public Double getExclusivePercentAmount()
	{
		return this.exclusivePercentAmount;
	}

	public void setExclusivePercentAmount(Double exclusivePercentAmount)
	{
		this.exclusivePercentAmount = exclusivePercentAmount;
	}

	@Column(name = "INCLUSIVE_FIX_AMOUNT")
	public Double getInclusiveFixAmount()
	{
		return this.inclusiveFixAmount;
	}

	public void setInclusiveFixAmount(Double inclusiveFixAmount)
	{
		this.inclusiveFixAmount = inclusiveFixAmount;
	}

	@Column(name = "INCLUSIVE_PERCENT_AMOUNT")
	public Double getInclusivePercentAmount()
	{
		return this.inclusivePercentAmount;
	}

	public void setInclusivePercentAmount(Double inclusivePercentAmount)
	{
		this.inclusivePercentAmount = inclusivePercentAmount;
	}

	@Column(name = "CREATED_ON")
	public Date getCreatedOn()
	{
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}

	@Column(name = "UPDATED_ON")
	public Date getUpdatedOn()
	{
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn)
	{
		this.updatedOn = updatedOn;
	}

	@Version
	@Column(name = "VERSION_NO")
	public Long getVersionNo()
	{
		return this.versionNo;
	}

	public void setVersionNo(Long versionNo)
	{
		this.versionNo = versionNo;
	}
	
	@Column(name="IS_DELETED")
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}