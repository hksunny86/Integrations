package com.inov8.microbank.common.model;

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
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;

/**
 * The ProductModel entity bean.
 * 
 * @author Muhammad Atif Hussain Inov8 Limited
 * @version $Revision: 1.0 $, $Date: 2014/08/20
 * 
 * 
 * @spring.bean name="ProductLimitRuleModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PRODUCT_LIMIT_RULE_seq", sequenceName = "PRODUCT_LIMIT_RULE_seq", allocationSize = 1)
@Table(name = "PRODUCT_LIMIT_RULE")
public class ProductLimitRuleModel extends BasePersistableModel implements
		Serializable {

	private ProductModel productIdModel;
	private DeviceTypeModel deviceTypeIdModel;
	private SegmentModel segmentIdModel;
	private DistributorModel distributorModel;
	private OlaCustomerAccountTypeModel accountTypeModel;
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private MnoModel  mnoModelIdMnoModel;

	private Long productLimitRuleId;
	private Double minLimit;
	private Double maxLimit;
	private Date updatedOn;
	private Date createdOn;
	private Integer versionNo;
	private Boolean active;

	public ProductLimitRuleModel() {

	}

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
    	associationModel.setClassName("ProductModel");
    	associationModel.setPropertyName("productIdModel");   		
   		associationModel.setValue(getProductIdModel());
   		
   		associationModelList.add(associationModel);
    	
    	associationModel = new AssociationModel();
    	associationModel.setClassName("SegmentModel");
    	associationModel.setPropertyName("segmentIdModel");   		
   		associationModel.setValue(getSegmentIdModel());

   		associationModelList.add(associationModel);
   		
    	associationModel = new AssociationModel();
    	associationModel.setClassName("DistributorModel");
    	associationModel.setPropertyName("distributorModel");   		
   		associationModel.setValue(getDistributorModel());
   		associationModelList.add(associationModel);
   		
   		associationModel = new AssociationModel();
    	associationModel.setClassName("OlaCustomerAccountTypeModel");
    	associationModel.setPropertyName("accountTypeModel");   		
   		associationModel.setValue(getAccountTypeModel());
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


	@Column(name = "PRODUCT_LIMIT_RULE_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_LIMIT_RULE_seq")
	public Long getProductLimitRuleId() {
		return productLimitRuleId;
	}

	public void setProductLimitRuleId(Long productLimitRuleId) {
		this.productLimitRuleId = productLimitRuleId;
	}

	@Transient
	@Override
	public Long getPrimaryKey() {
		return getProductLimitRuleId();
	}

	@Transient
	@Override
	public String getPrimaryKeyFieldName() {
		return "productLimitRuleId";
	}

	@Transient
	@Override
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&productLimitRuleId=" + getProductLimitRuleId();
		return parameters;
	}

	@Override
	public void setPrimaryKey(Long arg0) {
		setProductLimitRuleId(arg0);

	}

	@Column(name = "UPDATED_ON", nullable = false)
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Column(name = "CREATED_ON", nullable = false)
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Version
	@Column(name = "VERSION_NO", nullable = false)
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	@Column(name = "IS_ACTIVE", nullable = false)
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "MINIMUM_LIMIT")
	public Double getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(Double minLimit) {
		this.minLimit = minLimit;
	}

	@Column(name = "MAXIMUM_LIMIT")
	public Double getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(Double maxLimit) {
		this.maxLimit = maxLimit;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "DEVICE_TYPE_ID")
	public DeviceTypeModel getDeviceTypeIdModel() {
		return deviceTypeIdModel;
	}

	@javax.persistence.Transient
	public void setDeviceTypeIdModel(DeviceTypeModel deviceTypeModel) {
		this.deviceTypeIdModel = deviceTypeModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	public ProductModel getProductIdModel() {
		return productIdModel;
	}

	public void setProductIdModel(ProductModel productIdModel) {
		this.productIdModel = productIdModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEGMENT_ID")
	public SegmentModel getSegmentIdModel() {
		return segmentIdModel;
	}

	public void setSegmentIdModel(SegmentModel segmentIdModel) {
		this.segmentIdModel = segmentIdModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "DISTRIBUTOR_ID")
	public DistributorModel getDistributorModel() {
		return distributorModel;
	}

	public void setDistributorModel(DistributorModel distributorModel) {
		this.distributorModel = distributorModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getCreatedByAppUserModel() {
		return createdByAppUserModel;
	}

	public void setCreatedByAppUserModel(AppUserModel createdByAppUserModel) {
		this.createdByAppUserModel = createdByAppUserModel;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getUpdatedByAppUserModel() {
		return updatedByAppUserModel;
	}

	public void setUpdatedByAppUserModel(AppUserModel updatedByAppUserModel) {
		this.updatedByAppUserModel = updatedByAppUserModel;
	}

	@javax.persistence.Transient
	public Long getProductId() {
		if (productIdModel != null) {
			return productIdModel.getProductId();
		} else {
			return null;
		}
	}

	public void setProductId(Long productId) {
		if (productId == null) {
			productIdModel = null;
		} else {
			productIdModel = new ProductModel();
			productIdModel.setProductId(productId);
		}
	}

	//
	@javax.persistence.Transient
	public Long getSegmentId() {
		if (segmentIdModel != null) {
			return segmentIdModel.getSegmentId();
		} else {
			return null;
		}
	}

	public void setSegmentId(Long segmentId) {
		if (segmentId == null) {
			segmentIdModel = null;
		} else {
			segmentIdModel = new SegmentModel();
			segmentIdModel.setSegmentId(segmentId);
		}
	}

	@javax.persistence.Transient
	public Long getDistributorId() {
		if (distributorModel != null) {
			return distributorModel.getDistributorId();
		} else {
			return null;
		}
	}

	public void setDistributorId(Long distributorId) {
		if (distributorId == null) {
			distributorModel = null;
		} else {
			distributorModel = new DistributorModel();
			distributorModel.setDistributorId(distributorId);
		}
	}

	@javax.persistence.Transient
	public Long getDeviceTypeId() {
		if (deviceTypeIdModel != null) {
			return deviceTypeIdModel.getDeviceTypeId();
		} else {
			return null;
		}
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		if (deviceTypeId == null) {
			deviceTypeIdModel = null;
		} else {
			deviceTypeIdModel = new DeviceTypeModel();
			deviceTypeIdModel.setDeviceTypeId(deviceTypeId);
		}
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "HANDLER_ACCOUNT_TYPE_ID")
	public OlaCustomerAccountTypeModel getAccountTypeModel() {
		return accountTypeModel;
	}

	public void setAccountTypeModel(OlaCustomerAccountTypeModel accountTypeModel) {
		this.accountTypeModel = accountTypeModel;
	}

	
	
	@javax.persistence.Transient
	public Long getAccountTypeId() {
		if (accountTypeModel != null) {
			return accountTypeModel.getCustomerAccountTypeId();
		} else {
			return null;
		}
	}

	public void setAccountTypeId(Long accountTypeId) {
		if (accountTypeId == null) {
			accountTypeModel = null;
		} else {
			accountTypeModel = new OlaCustomerAccountTypeModel();
			accountTypeModel.setCustomerAccountTypeId(accountTypeId);
		}
	}
	
	
	@Override
	public boolean equals(Object obj) {
		ProductLimitRuleModel item = (ProductLimitRuleModel) obj;
		boolean flag=false;
		
		if(!item.getActive() || !this.getActive())
			return flag;
		
		if(this.getDeviceTypeId().longValue()==item.getDeviceTypeId().longValue())
		{
			if(this.getDistributorId().longValue()==item.getDistributorId().longValue())
			{
				if(this.getSegmentId().longValue()==item.getSegmentId().longValue())
				{
					if(this.getAccountTypeId()==null && item.getAccountTypeId()==null)
					{
						flag=true;
					}
					else if(this.getAccountTypeId()==null || item.getAccountTypeId()==null)
					{
						flag=false;
					}
					else if(this.getAccountTypeId().longValue()==item.getAccountTypeId().longValue())
					{
						flag=true;
					}
				}
			}
		}
		
		return flag;
	}

}