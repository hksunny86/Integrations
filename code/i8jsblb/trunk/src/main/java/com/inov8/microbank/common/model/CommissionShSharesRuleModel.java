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

/**
 * @author Abu Turab
 * @date   20-08-2014
 *
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PRODUCT_SH_SHARES_RULE_seq", sequenceName = "PRODUCT_SH_SHARES_RULE_seq", allocationSize = 1)
@Table(name = "PRODUCT_SH_SHARES_RULE")
public class CommissionShSharesRuleModel extends BasePersistableModel implements
		Serializable {

	private ProductModel productIdProductModel;
	private CommissionStakeholderModel commissionStakeholderIdCommissionStakeholderModel;
	private SegmentModel segmentIdSegmentModel;
	private DeviceTypeModel deviceTypeIdDeviceTypeModel;
	private DistributorModel distributorModel;
	private MnoModel  mnoModelIdMnoModel;

	private Long commissionShSharesRuleId;
	private Double commissionShare;
	private Date updatedOn;
	private Date createdOn;
	private Integer versionNo;

	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;

	private Boolean isWhtApplicable;
	private Boolean isFedApplicable;
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

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	public ProductModel getRelationProductIdProductModel() {
		return productIdProductModel;
	}

	/**
	 * Returns the value of the <code>productIdProductModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>productIdProductModel</code> relation
	 *         property.
	 * 
	 */
	@javax.persistence.Transient
	public ProductModel getProductIdProductModel() {
		return getRelationProductIdProductModel();
	}

	/**
	 * Sets the value of the <code>productIdProductModel</code> relation
	 * property.
	 * 
	 * @param productModel
	 *            a value for <code>productIdProductModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationProductIdProductModel(ProductModel productModel) {
		this.productIdProductModel = productModel;
	}

	/**
	 * Sets the value of the <code>productIdProductModel</code> relation
	 * property.
	 * 
	 * @param productModel
	 *            a value for <code>productIdProductModel</code>.
	 */
	@javax.persistence.Transient
	public void setProductIdProductModel(ProductModel productModel) {
		if (null != productModel) {
			setRelationProductIdProductModel((ProductModel) productModel
					.clone());
		}
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "STAKEHOLDER_ID")
	public CommissionStakeholderModel getRelationCommissionStakeholderIdCommissionStakeholderModel() {
		return commissionStakeholderIdCommissionStakeholderModel;
	}

	/**
	 * Returns the value of the
	 * <code>commissionStakeholderIdCommissionStakeholderModel</code> relation
	 * property.
	 * 
	 * @return the value of the
	 *         <code>commissionStakeholderIdCommissionStakeholderModel</code>
	 *         relation property.
	 * 
	 */
	@javax.persistence.Transient
	public CommissionStakeholderModel getCommissionStakeholderIdCommissionStakeholderModel() {
		return getRelationCommissionStakeholderIdCommissionStakeholderModel();
	}

	/**
	 * Sets the value of the
	 * <code>commissionStakeholderIdCommissionStakeholderModel</code> relation
	 * property.
	 * 
	 * @param commissionStakeholderModel
	 *            a value for
	 *            <code>commissionStakeholderIdCommissionStakeholderModel</code>
	 *            .
	 */
	@javax.persistence.Transient
	public void setRelationCommissionStakeholderIdCommissionStakeholderModel(
			CommissionStakeholderModel commissionStakeholderModel) {
		this.commissionStakeholderIdCommissionStakeholderModel = commissionStakeholderModel;
	}

	/**
	 * Sets the value of the
	 * <code>commissionStakeholderIdCommissionStakeholderModel</code> relation
	 * property.
	 * 
	 * @param commissionStakeholderModel
	 *            a value for
	 *            <code>commissionStakeholderIdCommissionStakeholderModel</code>
	 *            .
	 */
	@javax.persistence.Transient
	public void setCommissionStakeholderIdCommissionStakeholderModel(
			CommissionStakeholderModel commissionStakeholderModel) {
		if (null != commissionStakeholderModel) {
			setRelationCommissionStakeholderIdCommissionStakeholderModel((CommissionStakeholderModel) commissionStakeholderModel
					.clone());
		}
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEGMENT_ID")
	public SegmentModel getRelationSegmentIdSegmentModel() {
		return segmentIdSegmentModel;
	}

	/**
	 * Returns the value of the <code>segmentIdSegmentModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>segmentIdSegmentModel</code> relation
	 *         property.
	 * 
	 */
	@javax.persistence.Transient
	public SegmentModel getSegmentIdSegmentModel() {
		return getRelationSegmentIdSegmentModel();
	}

	/**
	 * Sets the value of the <code>segmentIdSegmentModel</code> relation
	 * property.
	 * 
	 * @param segmentModel
	 *            a value for <code>segmentIdSegmentModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationSegmentIdSegmentModel(SegmentModel segmentModel) {
		this.segmentIdSegmentModel = segmentModel;
	}

	/**
	 * Sets the value of the <code>segmentIdSegmentModel</code> relation
	 * property.
	 * 
	 * @param segmentModel
	 *            a value for <code>segmentIdSegmentModel</code>.
	 */
	@javax.persistence.Transient
	public void setSegmentIdSegmentModel(SegmentModel segmentModel) {
		if (null != segmentModel) {
			setRelationSegmentIdSegmentModel((SegmentModel) segmentModel
					.clone());
		}
	}

	/**
	 * Returns the value of the <code>segmentId</code> property.
	 * 
	 */
	@javax.persistence.Transient
	public Long getSegmentId() {
		if (segmentIdSegmentModel != null) {
			return segmentIdSegmentModel.getSegmentId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>segmentId</code> property.
	 * 
	 * @param segmentId
	 *            the value for the <code>segmentId</code> property
	 */

	@javax.persistence.Transient
	public void setSegmentId(Long segmentId) {
		if (segmentId == null) {
			segmentIdSegmentModel = null;
		} else {
			segmentIdSegmentModel = new SegmentModel();
			segmentIdSegmentModel.setSegmentId(segmentId);
		}
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "DEVICE_TYPE_ID")
	public DeviceTypeModel getRelationDeviceTypeIdDeviceTypeModel() {
		return deviceTypeIdDeviceTypeModel;
	}
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "DISTRIBUTOR_ID")
	public DistributorModel getDistributorModel() {
		return distributorModel;
	}
	
	public void setDistributorModel(DistributorModel distributorModel) {
		this.distributorModel = distributorModel;
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

	/**
	 * Returns the value of the <code>deviceTypeIdDeviceTypeModel</code>
	 * relation property.
	 * 
	 * @return the value of the <code>deviceTypeIdDeviceTypeModel</code>
	 *         relation property.
	 * 
	 */
	@javax.persistence.Transient
	public DeviceTypeModel getDeviceTypeIdDeviceTypeModel() {
		return getRelationDeviceTypeIdDeviceTypeModel();
	}

	/**
	 * Sets the value of the <code>deviceTypeIdDeviceTypeModel</code> relation
	 * property.
	 * 
	 * @param deviceTypeModel
	 *            a value for <code>deviceTypeIdDeviceTypeModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationDeviceTypeIdDeviceTypeModel(
			DeviceTypeModel deviceTypeModel) {
		this.deviceTypeIdDeviceTypeModel = deviceTypeModel;
	}

	/**
	 * Sets the value of the <code>deviceTypeIdDeviceTypeModel</code> relation
	 * property.
	 * 
	 * @param deviceTypeModel
	 *            a value for <code>deviceTypeIdDeviceTypeModel</code>.
	 */
	@javax.persistence.Transient
	public void setDeviceTypeIdDeviceTypeModel(DeviceTypeModel deviceTypeModel) {
		if (null != deviceTypeModel) {
			setRelationDeviceTypeIdDeviceTypeModel((DeviceTypeModel) deviceTypeModel
					.clone());
		}
	}

	/**
	 * Returns the value of the <code>deviceTypeId</code> property.
	 * 
	 */
	@javax.persistence.Transient
	public Long getDeviceTypeId() {
		if (deviceTypeIdDeviceTypeModel != null) {
			return deviceTypeIdDeviceTypeModel.getDeviceTypeId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>deviceTypeId</code> property.
	 * 
	 * @param deviceTypeId
	 *            the value for the <code>deviceTypeId</code> property
	 * @spring.validator type="required"
	 */

	@javax.persistence.Transient
	public void setDeviceTypeId(Long deviceTypeId) {
		if (deviceTypeId == null) {
			deviceTypeIdDeviceTypeModel = null;
		} else {
			deviceTypeIdDeviceTypeModel = new DeviceTypeModel();
			deviceTypeIdDeviceTypeModel.setDeviceTypeId(deviceTypeId);
		}
	}

	@javax.persistence.Transient
	public void setCommissionStakeholderId(Long commissionStakeholderId) {
		if (commissionStakeholderId == null) {
			commissionStakeholderIdCommissionStakeholderModel = null;
		} else {
			commissionStakeholderIdCommissionStakeholderModel = new CommissionStakeholderModel();
			commissionStakeholderIdCommissionStakeholderModel
					.setCommissionStakeholderId(commissionStakeholderId);
		}
	}

	@javax.persistence.Transient
	public Long getCommissionStakeholderId() {
		if (commissionStakeholderIdCommissionStakeholderModel != null) {
			return commissionStakeholderIdCommissionStakeholderModel
					.getCommissionStakeholderId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public Long getProductId() {
		if (productIdProductModel != null) {
			return productIdProductModel.getProductId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>productId</code> property.
	 * 
	 * @param productId
	 *            the value for the <code>productId</code> property
	 * @spring.validator type="required"
	 */

	@javax.persistence.Transient
	public void setProductId(Long productId) {
		if (productId == null) {
			productIdProductModel = null;
		} else {
			productIdProductModel = new ProductModel();
			productIdProductModel.setProductId(productId);
		}
	}

	@Transient
	public String getCheckbox() {
		String checkBox = "<input type=\"checkbox\" name=\"checkbox";
		checkBox += "_" + getCommissionShSharesRuleId();
		checkBox += "\"/>";
		return checkBox;
	}

	/**
	 * Helper method for Struts with displaytag
	 */

	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getCommissionShSharesRuleId();
	}

	/**
	 * Set the primary key.
	 * 
	 * @param primaryKey
	 *            the primary key
	 */
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setCommissionShSharesRuleId(primaryKey);
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&commissionShSharesRuleId="
				+ getCommissionShSharesRuleId();
		return parameters;
	}

	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "commissionShSharesRuleId";
		return primaryKeyFieldName;
	}

	/**
	 * Returns the value of the <code>updatedOn</code> property.
	 * 
	 */
	@Column(name = "UPDATED_ON", nullable = false)
	public Date getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * Sets the value of the <code>updatedOn</code> property.
	 * 
	 * @param updatedOn
	 *            the value for the <code>updatedOn</code> property
	 * 
	 */

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * Returns the value of the <code>createdOn</code> property.
	 * 
	 */
	@Column(name = "CREATED_ON", nullable = false)
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * Sets the value of the <code>createdOn</code> property.
	 * 
	 * @param createdOn
	 *            the value for the <code>createdOn</code> property
	 * 
	 */

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * Returns the value of the <code>versionNo</code> property.
	 * 
	 */
	@Version
	@Column(name = "VERSION_NO", nullable = false)
	public Integer getVersionNo() {
		return versionNo;
	}

	/**
	 * Sets the value of the <code>versionNo</code> property.
	 * 
	 * @param versionNo
	 *            the value for the <code>versionNo</code> property
	 * 
	 */

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	/**
	 * Returns the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>updatedByAppUserModel</code> relation
	 *         property.
	 * 
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getRelationUpdatedByAppUserModel() {
		return updatedByAppUserModel;
	}

	/**
	 * Returns the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>updatedByAppUserModel</code> relation
	 *         property.
	 * 
	 */
	@javax.persistence.Transient
	public AppUserModel getUpdatedByAppUserModel() {
		return getRelationUpdatedByAppUserModel();
	}

	/**
	 * Sets the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * 
	 * @param appUserModel
	 *            a value for <code>updatedByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
		this.updatedByAppUserModel = appUserModel;
	}

	/**
	 * Sets the value of the <code>updatedByAppUserModel</code> relation
	 * property.
	 * 
	 * @param appUserModel
	 *            a value for <code>updatedByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			setRelationUpdatedByAppUserModel((AppUserModel) appUserModel
					.clone());
		}
	}

	/**
	 * Returns the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>createdByAppUserModel</code> relation
	 *         property.
	 * 
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	public AppUserModel getRelationCreatedByAppUserModel() {
		return createdByAppUserModel;
	}

	/**
	 * Returns the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>createdByAppUserModel</code> relation
	 *         property.
	 * 
	 */
	@javax.persistence.Transient
	public AppUserModel getCreatedByAppUserModel() {
		return getRelationCreatedByAppUserModel();
	}

	/**
	 * Sets the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * 
	 * @param appUserModel
	 *            a value for <code>createdByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationCreatedByAppUserModel(AppUserModel appUserModel) {
		this.createdByAppUserModel = appUserModel;
	}

	/**
	 * Sets the value of the <code>createdByAppUserModel</code> relation
	 * property.
	 * 
	 * @param appUserModel
	 *            a value for <code>createdByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setCreatedByAppUserModel(AppUserModel appUserModel) {
		if (null != appUserModel) {
			setRelationCreatedByAppUserModel((AppUserModel) appUserModel
					.clone());
		}
	}

	/**
	 * Returns the value of the <code>appUserId</code> property.
	 * 
	 */
	@javax.persistence.Transient
	public Long getUpdatedBy() {
		if (updatedByAppUserModel != null) {
			return updatedByAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>appUserId</code> property.
	 * 
	 * @param appUserId
	 *            the value for the <code>appUserId</code> property
	 */

	@javax.persistence.Transient
	public void setUpdatedBy(Long appUserId) {
		if (appUserId == null) {
			updatedByAppUserModel = null;
		} else {
			updatedByAppUserModel = new AppUserModel();
			updatedByAppUserModel.setAppUserId(appUserId);
		}
	}

	/**
	 * Returns the value of the <code>appUserId</code> property.
	 * 
	 */
	@javax.persistence.Transient
	public Long getCreatedBy() {
		if (createdByAppUserModel != null) {
			return createdByAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>appUserId</code> property.
	 * 
	 * @param appUserId
	 *            the value for the <code>appUserId</code> property
	 */

	@javax.persistence.Transient
	public void setCreatedBy(Long appUserId) {
		if (appUserId == null) {
			createdByAppUserModel = null;
		} else {
			createdByAppUserModel = new AppUserModel();
			createdByAppUserModel.setAppUserId(appUserId);
		}
	}

	@Column(name = "PRODUCT_SH_SHARES_RULE_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_SH_SHARES_RULE_seq")
	public Long getCommissionShSharesRuleId() {
		return commissionShSharesRuleId;
	}

	public void setCommissionShSharesRuleId(Long commissionShSharesRuleId) {
		this.commissionShSharesRuleId = commissionShSharesRuleId;
	}

	@Column(name = "COMMISSION_SHARE", nullable = true)
	public Double getCommissionShare() {
		return commissionShare;
	}

	/**
	 * Sets the value of the <code>commissionShare</code> property.
	 * 
	 * @param share
	 *            the value for the <code>commissionShare</code> property
	 * 
	 * @spring.validator type="required"
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setCommissionShare(Double commissionShare) {
		this.commissionShare = commissionShare;
	}

	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = null;

		associationModel = new AssociationModel();

		associationModel.setClassName("ProductModel");
		associationModel.setPropertyName("relationProductIdProductModel");
		associationModel.setValue(getRelationProductIdProductModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("CommissionStakeholderModel");
		associationModel
				.setPropertyName("relationCommissionStakeholderIdCommissionStakeholderModel");
		associationModel
				.setValue(getRelationCommissionStakeholderIdCommissionStakeholderModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationUpdatedByAppUserModel");
		associationModel.setValue(getRelationUpdatedByAppUserModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationCreatedByAppUserModel");
		associationModel.setValue(getRelationCreatedByAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("SegmentModel");
		associationModel.setPropertyName("relationSegmentIdSegmentModel");
		associationModel.setValue(getRelationSegmentIdSegmentModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("DeviceTypeModel");
		associationModel.setPropertyName("relationDeviceTypeIdDeviceTypeModel");
		associationModel.setValue(getRelationDeviceTypeIdDeviceTypeModel());
		associationModelList.add(associationModel);
		
		associationModel = new AssociationModel();
    	associationModel.setClassName("DistributorModel");
    	associationModel.setPropertyName("distributorModel");   		
   		associationModel.setValue(getDistributorModel());
   		associationModelList.add(associationModel);

		return associationModelList;
	}

	@Column(name="IS_WHT_APPLICABLE")
	public Boolean getIsWhtApplicable() {
		return isWhtApplicable;
	}

	public void setIsWhtApplicable(Boolean isWhtApplicable) {
		this.isWhtApplicable = isWhtApplicable;
	}

	@Column(name="IS_FED_APPLICABLE")
	public Boolean getIsFedApplicable() {
		return isFedApplicable;
	}

	public void setIsFedApplicable(Boolean isFedApplicable) {
		this.isFedApplicable = isFedApplicable;
	}

	@Column(name="IS_DELETED")
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
