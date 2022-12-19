package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PoolAccountConstantsInterface;
import org.springframework.jdbc.core.RowMapper;

/**
 * The CommissionRateModel entity bean.
 * 
 * @author Usman Ashraf Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 * 
 * 
 * @spring.bean name="CommissionRateModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "COMMISSION_RATE_seq", sequenceName = "COMMISSION_RATE_seq", allocationSize = 1)
@Table(name = "COMMISSION_RATE")
public class CommissionRateModel extends BasePersistableModel implements
		Serializable,RowMapper {

	private TransactionTypeModel transactionTypeIdTransactionTypeModel;
	private StakeholderBankInfoModel stakeholderBankInfoIdStakeholderBankInfoModel;
	private SegmentModel segmentIdSegmentModel;
	private ProductModel productIdProductModel;
	private PaymentModeModel paymentModeIdPaymentModeModel;
	private CommissionTypeModel commissionTypeIdCommissionTypeModel;
	private CommissionStakeholderModel commissionStakeholderIdCommissionStakeholderModel;
	private CommissionReasonModel commissionReasonIdCommissionReasonModel;
	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private DeviceTypeModel deviceTypeIdDeviceTypeModel;
    private DistributorModel distributorIdDistributorModel;
	private MnoModel  mnoModelIdMnoModel;

	private Collection<CommissionTransactionModel> commissionRateIdCommissionTransactionModelList = new ArrayList<CommissionTransactionModel>();

	private Long commissionRateId;
	private Double rate;
	private Date fromDate;
	private Date toDate;
	private Boolean active;
	private String description;
	private String comments;
	private Date createdOn;
	private Date updatedOn;
	private Integer versionNo;
	private Double rangeStarts;
	private Double rangeEnds;
	private Double exclusiveFixAmount = 0D;
	private Double exclusivePercentAmount = 0D;
	private Double inclusiveFixAmount = 0D;
	private Double inclusivePercentAmount = 0D;
	private Boolean isDeleted;
	/**
	 * Default constructor.
	 */
	public CommissionRateModel() {
	}
	
	public CommissionRateModel(boolean initializeForHierarchyUser, Long hierarchyStakeholderId) {
		this.setCommissionTypeId(CommissionConstantsInterface.FIXED_COMMISSION);
		this.setCommissionStakeholderId(hierarchyStakeholderId); // (CommissionConstantsInterface.HIERARCHY_STAKE_HOLDER_ID);
	    
		this.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		this.setStakeholderBankInfoId(PoolAccountConstantsInterface.HIERARCHY_INCOME_ACCOUNT_ID);
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
	 * Return the primary key.
	 * 
	 * @return Long with the primary key.
	 */
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getCommissionRateId();
	}

	/**
	 * Set the primary key.
	 * 
	 * @param primaryKey
	 *            the primary key
	 */
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setCommissionRateId(primaryKey);
	}

	/**
	 * Returns the value of the <code>commissionRateId</code> property.
	 * 
	 */
	@Column(name = "COMMISSION_RATE_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMISSION_RATE_seq")
	public Long getCommissionRateId() {
		return commissionRateId;
	}

	/**
	 * Sets the value of the <code>commissionRateId</code> property.
	 * 
	 * @param commissionRateId
	 *            the value for the <code>commissionRateId</code> property
	 * 
	 */

	public void setCommissionRateId(Long commissionRateId) {
		this.commissionRateId = commissionRateId;
	}

	/**
	 * Returns the value of the <code>rate</code> property.
	 * 
	 */
	@Column(name = "RATE", nullable = false)
	public Double getRate() {
		return rate;
	}

	/**
	 * Sets the value of the <code>rate</code> property.
	 * 
	 * @param rate
	 *            the value for the <code>rate</code> property
	 * 
	 * @spring.validator type="required"
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setRate(Double rate) {
		this.rate = rate;
	}

	
	/**
	 * Returns the value of the <code>exclusiveFixAmount</code> property.
	 * 
	 */
	@Column(name = "EXCLUSIVE_FIX_AMOUNT", nullable = false)
	public Double getExclusiveFixAmount() {
		return exclusiveFixAmount;
	}

	/**
	 * Sets the value of the <code>exclusiveFixAmount</code> property.
	 * 
	 * @param rate
	 *            the value for the <code>exclusiveFixAmount</code> property
	 * 
	 * @spring.validator type="required"
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setExclusiveFixAmount(Double exclusiveFixAmount) {
		this.exclusiveFixAmount = exclusiveFixAmount;
	}

	
	/**
	 * Returns the value of the <code>exclusiveFixAmount</code> property.
	 * 
	 */
	@Column(name = "INCLUSIVE_FIX_AMOUNT", nullable = false)
	public Double getInclusiveFixAmount() {
		return inclusiveFixAmount;
	}

	/**
	 * Sets the value of the <code>inclusiveFixAmount</code> property.
	 * 
	 * @param rate
	 *            the value for the <code>inclusiveFixAmount</code> property
	 * 
	 * @spring.validator type="required"
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setInclusiveFixAmount(Double inclusiveFixAmount) {
		this.inclusiveFixAmount = inclusiveFixAmount;
	}

	/**
	 * Returns the value of the <code>exclusivePercentAmount</code> property.
	 * 
	 */
	@Column(name = "EXCLUSIVE_PERCENT_AMOUNT", nullable = false)
	public Double getExclusivePercentAmount() {
		return exclusivePercentAmount;
	}

	/**
	 * Sets the value of the <code>exclusivePercentAmount</code> property.
	 * 
	 * @param rate
	 *            the value for the <code>exclusivePercentAmount</code> property
	 * 
	 * @spring.validator type="required"
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setExclusivePercentAmount(Double exclusivePercentAmount) {
		this.exclusivePercentAmount = exclusivePercentAmount;
	}


	/**
	 * Returns the value of the <code>inclusivePercentAmount</code> property.
	 * 
	 */
	@Column(name = "INCLUSIVE_PERCENT_AMOUNT", nullable = false)
	public Double getInclusivePercentAmount() {
		return inclusivePercentAmount;
	}

	/**
	 * Sets the value of the <code>inclusivePercentAmount</code> property.
	 * 
	 * @param rate
	 *            the value for the <code>inclusivePercentAmount</code> property
	 * 
	 * @spring.validator type="required"
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setInclusivePercentAmount(Double inclusivePercentAmount) {
		this.inclusivePercentAmount = inclusivePercentAmount;
	}

	/**
	 * Returns the value of the <code>fromDate</code> property.
	 * 
	 */
	@Column(name = "FROM_DATE")
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * Sets the value of the <code>fromDate</code> property.
	 * 
	 * @param fromDate
	 *            the value for the <code>fromDate</code> property
	 * 
	 * @spring.validator type="date"
	 * @spring.validator-var name="datePattern" value="${date_format}"
	 */

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * Returns the value of the <code>toDate</code> property.
	 * 
	 */
	@Column(name = "TO_DATE")
	public Date getToDate() {
		return toDate;
	}

	/**
	 * Sets the value of the <code>toDate</code> property.
	 * 
	 * @param toDate
	 *            the value for the <code>toDate</code> property
	 * 
	 * @spring.validator type="date"
	 * @spring.validator-var name="datePattern" value="${date_format}"
	 */

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * Returns the value of the <code>active</code> property.
	 * 
	 */
	@Column(name = "IS_ACTIVE", nullable = false)
	public Boolean getActive() {
		return active;
	}

	/**
	 * Sets the value of the <code>active</code> property.
	 * 
	 * @param active
	 *            the value for the <code>active</code> property
	 * 
	 */

	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * Returns the value of the <code>description</code> property.
	 * 
	 */
	@Column(name = "DESCRIPTION", length = 250)
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the value of the <code>description</code> property.
	 * 
	 * @param description
	 *            the value for the <code>description</code> property
	 * 
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="250"
	 */

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the value of the <code>comments</code> property.
	 * 
	 */
	@Column(name = "COMMENTS", length = 250)
	public String getComments() {
		return comments;
	}

	/**
	 * Sets the value of the <code>comments</code> property.
	 * 
	 * @param comments
	 *            the value for the <code>comments</code> property
	 * 
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="250"
	 */

	public void setComments(String comments) {
		this.comments = comments;
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
	 * Returns the value of the <code>rangeStarts</code> property.
	 * 
	 */
	@Column(name = "RANGE_STARTS")
	public Double getRangeStarts() {
		return rangeStarts;
	}

	/**
	 * Sets the value of the <code>rangeStarts</code> property.
	 * 
	 * @param rangeStarts
	 *            the value for the <code>rangeStarts</code> property
	 * 
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setRangeStarts(Double rangeStarts) {
		this.rangeStarts = rangeStarts;
	}

	/**
	 * Returns the value of the <code>rangeEnds</code> property.
	 * 
	 */
	@Column(name = "RANGE_ENDS")
	public Double getRangeEnds() {
		return rangeEnds;
	}

	/**
	 * Sets the value of the <code>rangeEnds</code> property.
	 * 
	 * @param rangeEnds
	 *            the value for the <code>rangeEnds</code> property
	 * 
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setRangeEnds(Double rangeEnds) {
		this.rangeEnds = rangeEnds;
	}

	/**
	 * Returns the value of the
	 * <code>transactionTypeIdTransactionTypeModel</code> relation property.
	 * 
	 * @return the value of the
	 *         <code>transactionTypeIdTransactionTypeModel</code> relation
	 *         property.
	 * 
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "TRANSACTION_TYPE_ID")
	public TransactionTypeModel getRelationTransactionTypeIdTransactionTypeModel() {
		return transactionTypeIdTransactionTypeModel;
	}

	/**
	 * Returns the value of the
	 * <code>transactionTypeIdTransactionTypeModel</code> relation property.
	 * 
	 * @return the value of the
	 *         <code>transactionTypeIdTransactionTypeModel</code> relation
	 *         property.
	 * 
	 */
	@javax.persistence.Transient
	public TransactionTypeModel getTransactionTypeIdTransactionTypeModel() {
		return getRelationTransactionTypeIdTransactionTypeModel();
	}

	/**
	 * Sets the value of the <code>transactionTypeIdTransactionTypeModel</code>
	 * relation property.
	 * 
	 * @param transactionTypeModel
	 *            a value for <code>transactionTypeIdTransactionTypeModel</code>
	 *            .
	 */
	@javax.persistence.Transient
	public void setRelationTransactionTypeIdTransactionTypeModel(
			TransactionTypeModel transactionTypeModel) {
		this.transactionTypeIdTransactionTypeModel = transactionTypeModel;
	}

	/**
	 * Sets the value of the <code>transactionTypeIdTransactionTypeModel</code>
	 * relation property.
	 * 
	 * @param transactionTypeModel
	 *            a value for <code>transactionTypeIdTransactionTypeModel</code>
	 *            .
	 */
	@javax.persistence.Transient
	public void setTransactionTypeIdTransactionTypeModel(
			TransactionTypeModel transactionTypeModel) {
		if (null != transactionTypeModel) {
			setRelationTransactionTypeIdTransactionTypeModel((TransactionTypeModel) transactionTypeModel
					.clone());
		}
	}

	/**
	 * Returns the value of the
	 * <code>stakeholderBankInfoIdStakeholderBankInfoModel</code> relation
	 * property.
	 * 
	 * @return the value of the
	 *         <code>stakeholderBankInfoIdStakeholderBankInfoModel</code>
	 *         relation property.
	 * 
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "STAKEHOLDER_BANK_INFO_ID")
	public StakeholderBankInfoModel getRelationStakeholderBankInfoIdStakeholderBankInfoModel() {
		return stakeholderBankInfoIdStakeholderBankInfoModel;
	}

	/**
	 * Returns the value of the
	 * <code>stakeholderBankInfoIdStakeholderBankInfoModel</code> relation
	 * property.
	 * 
	 * @return the value of the
	 *         <code>stakeholderBankInfoIdStakeholderBankInfoModel</code>
	 *         relation property.
	 * 
	 */
	@javax.persistence.Transient
	public StakeholderBankInfoModel getStakeholderBankInfoIdStakeholderBankInfoModel() {
		return getRelationStakeholderBankInfoIdStakeholderBankInfoModel();
	}

	/**
	 * Sets the value of the
	 * <code>stakeholderBankInfoIdStakeholderBankInfoModel</code> relation
	 * property.
	 * 
	 * @param stakeholderBankInfoModel
	 *            a value for
	 *            <code>stakeholderBankInfoIdStakeholderBankInfoModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationStakeholderBankInfoIdStakeholderBankInfoModel(
			StakeholderBankInfoModel stakeholderBankInfoModel) {
		this.stakeholderBankInfoIdStakeholderBankInfoModel = stakeholderBankInfoModel;
	}

	/**
	 * Sets the value of the
	 * <code>stakeholderBankInfoIdStakeholderBankInfoModel</code> relation
	 * property.
	 * 
	 * @param stakeholderBankInfoModel
	 *            a value for
	 *            <code>stakeholderBankInfoIdStakeholderBankInfoModel</code>.
	 */
	@javax.persistence.Transient
	public void setStakeholderBankInfoIdStakeholderBankInfoModel(
			StakeholderBankInfoModel stakeholderBankInfoModel) {
		if (null != stakeholderBankInfoModel) {
			setRelationStakeholderBankInfoIdStakeholderBankInfoModel((StakeholderBankInfoModel) stakeholderBankInfoModel
					.clone());
		}
	}

	/**
	 * Returns the value of the <code>segmentIdSegmentModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>segmentIdSegmentModel</code> relation
	 *         property.
	 * 
	 */
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
	 * Returns the value of the <code>productIdProductModel</code> relation
	 * property.
	 * 
	 * @return the value of the <code>productIdProductModel</code> relation
	 *         property.
	 * 
	 */
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

	/**
	 * Returns the value of the <code>paymentModeIdPaymentModeModel</code>
	 * relation property.
	 * 
	 * @return the value of the <code>paymentModeIdPaymentModeModel</code>
	 *         relation property.
	 * 
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PAYMENT_MODE_ID")
	public PaymentModeModel getRelationPaymentModeIdPaymentModeModel() {
		return paymentModeIdPaymentModeModel;
	}

	/**
	 * Returns the value of the <code>paymentModeIdPaymentModeModel</code>
	 * relation property.
	 * 
	 * @return the value of the <code>paymentModeIdPaymentModeModel</code>
	 *         relation property.
	 * 
	 */
	@javax.persistence.Transient
	public PaymentModeModel getPaymentModeIdPaymentModeModel() {
		return getRelationPaymentModeIdPaymentModeModel();
	}

	/**
	 * Sets the value of the <code>paymentModeIdPaymentModeModel</code> relation
	 * property.
	 * 
	 * @param paymentModeModel
	 *            a value for <code>paymentModeIdPaymentModeModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationPaymentModeIdPaymentModeModel(
			PaymentModeModel paymentModeModel) {
		this.paymentModeIdPaymentModeModel = paymentModeModel;
	}

	/**
	 * Sets the value of the <code>paymentModeIdPaymentModeModel</code> relation
	 * property.
	 * 
	 * @param paymentModeModel
	 *            a value for <code>paymentModeIdPaymentModeModel</code>.
	 */
	@javax.persistence.Transient
	public void setPaymentModeIdPaymentModeModel(
			PaymentModeModel paymentModeModel) {
		if (null != paymentModeModel) {
			setRelationPaymentModeIdPaymentModeModel((PaymentModeModel) paymentModeModel
					.clone());
		}
	}

	/**
	 * Returns the value of the <code>DeviceTypeIdDeviceTypeModel</code>
	 * relation property.
	 * 
	 * @return the value of the <code>DeviceTypeIdDeviceTypeModel</code>
	 *         relation property.
	 * 
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "DEVICE_TYPE_ID")
	public DeviceTypeModel getRelationDeviceTypeIdDeviceTypeModel() {
		return deviceTypeIdDeviceTypeModel;
	}

	/**
	 * Returns the value of the <code>DeviceTypeIdDeviceTypeModel</code>
	 * relation property.
	 * 
	 * @return the value of the <code>DeviceTypeIdDeviceTypeModel</code>
	 *         relation property.
	 * 
	 */
	@javax.persistence.Transient
	public DeviceTypeModel getDeviceTypeIdDeviceTypeModel() {
		return getRelationDeviceTypeIdDeviceTypeModel();
	}

	/**
	 * Sets the value of the <code>DeviceTypeIdDeviceTypeModel</code>
	 * relation property.
	 * 
	 * @param DeviceTypeModel
	 *            a value for <code>DeviceTypeIdDeviceTypeModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationDeviceTypeIdDeviceTypeModel(
			DeviceTypeModel DeviceTypeModel) {
		this.deviceTypeIdDeviceTypeModel = DeviceTypeModel;
	}

	/**
	 * Sets the value of the <code>DeviceTypeIdDeviceTypeModel</code>
	 * relation property.
	 * 
	 * @param DeviceTypeModel
	 *            a value for <code>DeviceTypeIdDeviceTypeModel</code>.
	 */
	@javax.persistence.Transient
	public void setDeviceTypeIdDeviceTypeModel(
			DeviceTypeModel DeviceTypeModel) {
		if (null != DeviceTypeModel) {
			setRelationDeviceTypeIdDeviceTypeModel((DeviceTypeModel) DeviceTypeModel
					.clone());
		}
	}

	/**
	 * Returns the value of the <code>commissionTypeIdCommissionTypeModel</code>
	 * relation property.
	 * 
	 * @return the value of the <code>commissionTypeIdCommissionTypeModel</code>
	 *         relation property.
	 * 
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "COMMISSION_TYPE_ID")
	public CommissionTypeModel getRelationCommissionTypeIdCommissionTypeModel() {
		return commissionTypeIdCommissionTypeModel;
	}

	/**
	 * Returns the value of the <code>commissionTypeIdCommissionTypeModel</code>
	 * relation property.
	 * 
	 * @return the value of the <code>commissionTypeIdCommissionTypeModel</code>
	 *         relation property.
	 * 
	 */
	@javax.persistence.Transient
	public CommissionTypeModel getCommissionTypeIdCommissionTypeModel() {
		return getRelationCommissionTypeIdCommissionTypeModel();
	}

	/**
	 * Sets the value of the <code>commissionTypeIdCommissionTypeModel</code>
	 * relation property.
	 * 
	 * @param commissionTypeModel
	 *            a value for <code>commissionTypeIdCommissionTypeModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationCommissionTypeIdCommissionTypeModel(
			CommissionTypeModel commissionTypeModel) {
		this.commissionTypeIdCommissionTypeModel = commissionTypeModel;
	}

	/**
	 * Sets the value of the <code>commissionTypeIdCommissionTypeModel</code>
	 * relation property.
	 * 
	 * @param commissionTypeModel
	 *            a value for <code>commissionTypeIdCommissionTypeModel</code>.
	 */
	@javax.persistence.Transient
	public void setCommissionTypeIdCommissionTypeModel(
			CommissionTypeModel commissionTypeModel) {
		if (null != commissionTypeModel) {
			setRelationCommissionTypeIdCommissionTypeModel((CommissionTypeModel) commissionTypeModel
					.clone());
		}
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
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "COMMISSION_STAKEHOLDER_ID")
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

	/**
	 * Returns the value of the
	 * <code>commissionReasonIdCommissionReasonModel</code> relation property.
	 * 
	 * @return the value of the
	 *         <code>commissionReasonIdCommissionReasonModel</code> relation
	 *         property.
	 * 
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "COMMISSION_REASON_ID")
	public CommissionReasonModel getRelationCommissionReasonIdCommissionReasonModel() {
		return commissionReasonIdCommissionReasonModel;
	}

	/**
	 * Returns the value of the
	 * <code>commissionReasonIdCommissionReasonModel</code> relation property.
	 * 
	 * @return the value of the
	 *         <code>commissionReasonIdCommissionReasonModel</code> relation
	 *         property.
	 * 
	 */
	@javax.persistence.Transient
	public CommissionReasonModel getCommissionReasonIdCommissionReasonModel() {
		return getRelationCommissionReasonIdCommissionReasonModel();
	}

	/**
	 * Sets the value of the
	 * <code>commissionReasonIdCommissionReasonModel</code> relation property.
	 * 
	 * @param commissionReasonModel
	 *            a value for
	 *            <code>commissionReasonIdCommissionReasonModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationCommissionReasonIdCommissionReasonModel(
			CommissionReasonModel commissionReasonModel) {
		this.commissionReasonIdCommissionReasonModel = commissionReasonModel;
	}

	/**
	 * Sets the value of the
	 * <code>commissionReasonIdCommissionReasonModel</code> relation property.
	 * 
	 * @param commissionReasonModel
	 *            a value for
	 *            <code>commissionReasonIdCommissionReasonModel</code>.
	 */
	@javax.persistence.Transient
	public void setCommissionReasonIdCommissionReasonModel(
			CommissionReasonModel commissionReasonModel) {
		if (null != commissionReasonModel) {
			setRelationCommissionReasonIdCommissionReasonModel((CommissionReasonModel) commissionReasonModel
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
	 * Add the related CommissionTransactionModel to this one-to-many relation.
	 * 
	 * @param commissionTransactionModel
	 *            object to be added.
	 */

	public void addCommissionRateIdCommissionTransactionModel(
			CommissionTransactionModel commissionTransactionModel) {
		commissionTransactionModel
				.setRelationCommissionRateIdCommissionRateModel(this);
		commissionRateIdCommissionTransactionModelList
				.add(commissionTransactionModel);
	}

	/**
	 * Remove the related CommissionTransactionModel to this one-to-many
	 * relation.
	 * 
	 * @param commissionTransactionModel
	 *            object to be removed.
	 */

	public void removeCommissionRateIdCommissionTransactionModel(
			CommissionTransactionModel commissionTransactionModel) {
		commissionTransactionModel
				.setRelationCommissionRateIdCommissionRateModel(null);
		commissionRateIdCommissionTransactionModelList
				.remove(commissionTransactionModel);
	}

	/**
	 * Get a list of related CommissionTransactionModel objects of the
	 * CommissionRateModel object. These objects are in a bidirectional
	 * one-to-many relation by the CommissionRateId member.
	 * 
	 * @return Collection of CommissionTransactionModel objects.
	 * 
	 */

	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "relationCommissionRateIdCommissionRateModel")
	@JoinColumn(name = "COMMISSION_RATE_ID")
	public Collection<CommissionTransactionModel> getCommissionRateIdCommissionTransactionModelList()
			throws Exception {
		return commissionRateIdCommissionTransactionModelList;
	}

	/**
	 * Set a list of CommissionTransactionModel related objects to the
	 * CommissionRateModel object. These objects are in a bidirectional
	 * one-to-many relation by the CommissionRateId member.
	 * 
	 * @param commissionTransactionModelList
	 *            the list of related objects.
	 */
	public void setCommissionRateIdCommissionTransactionModelList(
			Collection<CommissionTransactionModel> commissionTransactionModelList)
			throws Exception {
		this.commissionRateIdCommissionTransactionModelList = commissionTransactionModelList;
	}

	/**
	 * Returns the value of the <code>transactionTypeId</code> property.
	 * 
	 */
	@javax.persistence.Transient
	public Long getTransactionTypeId() {
		if (transactionTypeIdTransactionTypeModel != null) {
			return transactionTypeIdTransactionTypeModel.getTransactionTypeId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>transactionTypeId</code> property.
	 * 
	 * @param transactionTypeId
	 *            the value for the <code>transactionTypeId</code> property
	 */

	@javax.persistence.Transient
	public void setTransactionTypeId(Long transactionTypeId) {
		if (transactionTypeId == null) {
			transactionTypeIdTransactionTypeModel = null;
		} else {
			transactionTypeIdTransactionTypeModel = new TransactionTypeModel();
			transactionTypeIdTransactionTypeModel
					.setTransactionTypeId(transactionTypeId);
		}
	}

	/**
	 * Returns the value of the <code>stakeholderBankInfoId</code> property.
	 * 
	 */
	@javax.persistence.Transient
	public Long getStakeholderBankInfoId() {
		if (stakeholderBankInfoIdStakeholderBankInfoModel != null) {
			return stakeholderBankInfoIdStakeholderBankInfoModel
					.getStakeholderBankInfoId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>stakeholderBankInfoId</code> property.
	 * 
	 * @param stakeholderBankInfoId
	 *            the value for the <code>stakeholderBankInfoId</code> property
	 */

	@javax.persistence.Transient
	public void setStakeholderBankInfoId(Long stakeholderBankInfoId) {
		if (stakeholderBankInfoId == null) {
			stakeholderBankInfoIdStakeholderBankInfoModel = null;
		} else {
			stakeholderBankInfoIdStakeholderBankInfoModel = new StakeholderBankInfoModel();
			stakeholderBankInfoIdStakeholderBankInfoModel
					.setStakeholderBankInfoId(stakeholderBankInfoId);
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

	/**
	 * Returns the value of the <code>productId</code> property.
	 * 
	 */
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

	/**
	 * Returns the value of the <code>paymentModeId</code> property.
	 * 
	 */
	@javax.persistence.Transient
	public Long getPaymentModeId() {
		if (paymentModeIdPaymentModeModel != null) {
			return paymentModeIdPaymentModeModel.getPaymentModeId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>paymentModeId</code> property.
	 * 
	 * @param paymentModeId
	 *            the value for the <code>paymentModeId</code> property
	 */

	@javax.persistence.Transient
	public void setPaymentModeId(Long paymentModeId) {
		if (paymentModeId == null) {
			paymentModeIdPaymentModeModel = null;
		} else {
			paymentModeIdPaymentModeModel = new PaymentModeModel();
			paymentModeIdPaymentModeModel.setPaymentModeId(paymentModeId);
		}
	}

	/**
	 * Returns the value of the <code>DeviceTypeId</code> property.
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
	 * Sets the value of the <code>DeviceTypeId</code> property.
	 * 
	 * @param DeviceTypeId
	 *            the value for the <code>DeviceTypeId</code> property
	 */

	@javax.persistence.Transient
	public void setDeviceTypeId(Long DeviceTypeId) {
		if (DeviceTypeId == null) {
			deviceTypeIdDeviceTypeModel = null;
		} else {
			deviceTypeIdDeviceTypeModel = new DeviceTypeModel();
			deviceTypeIdDeviceTypeModel.setDeviceTypeId(DeviceTypeId);
		}
	}

	/**
	 * Returns the value of the <code>commissionTypeId</code> property.
	 * 
	 */
	@javax.persistence.Transient
	public Long getCommissionTypeId() {
		if (commissionTypeIdCommissionTypeModel != null) {
			return commissionTypeIdCommissionTypeModel.getCommissionTypeId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>commissionTypeId</code> property.
	 * 
	 * @param commissionTypeId
	 *            the value for the <code>commissionTypeId</code> property
	 * @spring.validator type="required"
	 */

	@javax.persistence.Transient
	public void setCommissionTypeId(Long commissionTypeId) {
		if (commissionTypeId == null) {
			commissionTypeIdCommissionTypeModel = null;
		} else {
			commissionTypeIdCommissionTypeModel = new CommissionTypeModel();
			commissionTypeIdCommissionTypeModel
					.setCommissionTypeId(commissionTypeId);
		}
	}

	/**
	 * Returns the value of the <code>commissionStakeholderId</code> property.
	 * 
	 */
	@javax.persistence.Transient
	public Long getCommissionStakeholderId() {
		if (commissionStakeholderIdCommissionStakeholderModel != null) {
			return commissionStakeholderIdCommissionStakeholderModel
					.getCommissionStakeholderId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>commissionStakeholderId</code> property.
	 * 
	 * @param commissionStakeholderId
	 *            the value for the <code>commissionStakeholderId</code>
	 *            property
	 * @spring.validator type="required"
	 */

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

	/**
	 * Returns the value of the <code>commissionReasonId</code> property.
	 * 
	 */
	@javax.persistence.Transient
	public Long getCommissionReasonId() {
		if (commissionReasonIdCommissionReasonModel != null) {
			return commissionReasonIdCommissionReasonModel
					.getCommissionReasonId();
		} else {
			return null;
		}
	}

	/**
	 * Sets the value of the <code>commissionReasonId</code> property.
	 * 
	 * @param commissionReasonId
	 *            the value for the <code>commissionReasonId</code> property
	 * @spring.validator type="required"
	 */

	@javax.persistence.Transient
	public void setCommissionReasonId(Long commissionReasonId) {
		if (commissionReasonId == null) {
			commissionReasonIdCommissionReasonModel = null;
		} else {
			commissionReasonIdCommissionReasonModel = new CommissionReasonModel();
			commissionReasonIdCommissionReasonModel
					.setCommissionReasonId(commissionReasonId);
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
	 * Used by the display tag library for rendering a checkbox in the list.
	 * 
	 * @return String with a HTML checkbox.
	 */
	@Transient
	public String getCheckbox() {
		String checkBox = "<input type=\"checkbox\" name=\"checkbox";
		checkBox += "_" + getCommissionRateId();
		checkBox += "\"/>";
		return checkBox;
	}

	/**
	 * Helper method for Struts with displaytag
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&commissionRateId=" + getCommissionRateId();
		return parameters;
	}

	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "commissionRateId";
		return primaryKeyFieldName;
	}

	
	   /**
	    * Returns the value of the <code>distributorIdDistributorModel</code> relation property.
	    *
	    * @return the value of the <code>distributorIdDistributorModel</code> relation property.
	    *
	    */
	   @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	   @JoinColumn(name = "DISTRIBUTOR_ID")    
	   public DistributorModel getRelationDistributorIdDistributorModel(){
	      return distributorIdDistributorModel;
	   }
	    
	   /**
	    * Returns the value of the <code>distributorIdDistributorModel</code> relation property.
	    *
	    * @return the value of the <code>distributorIdDistributorModel</code> relation property.
	    *
	    */
	   @javax.persistence.Transient
	   public DistributorModel getDistributorIdDistributorModel(){
	      return getRelationDistributorIdDistributorModel();
	   }

	   /**
	    * Sets the value of the <code>distributorIdDistributorModel</code> relation property.
	    *
	    * @param distributorModel a value for <code>distributorIdDistributorModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setRelationDistributorIdDistributorModel(DistributorModel distributorModel) {
	      this.distributorIdDistributorModel = distributorModel;
	   }
	   
	   
	   @javax.persistence.Transient
	   public String getAgentNetworkName()
	   {
		   if(distributorIdDistributorModel != null)
		   {
			   return distributorIdDistributorModel.getName();
		   }
		   else
		   {
			   return null;
		   }	   
	   }
	   
	   
	   /**
	    * Sets the value of the <code>distributorIdDistributorModel</code> relation property.
	    *
	    * @param distributorModel a value for <code>distributorIdDistributorModel</code>.
	    */
	   @javax.persistence.Transient
	   public void setDistributorIdDistributorModel(DistributorModel distributorModel) {
	      if(null != distributorModel)
	      {
	      	setRelationDistributorIdDistributorModel((DistributorModel)distributorModel.clone());
	      }      
	   }


	   /**
	    * Returns the value of the <code>distributorId</code> property.
	    *
	    */
	   @javax.persistence.Transient
	   public Long getDistributorId() {
	      if (distributorIdDistributorModel != null) {
	         return distributorIdDistributorModel.getDistributorId();
	      } else {
	         return null;
	      }
	   }

	   /**
	    * Sets the value of the <code>distributorId</code> property.
	    *
	    * @param distributorId the value for the <code>distributorId</code> property
										    * @spring.validator type="required"
																																												    */
	   
	   @javax.persistence.Transient
	   public void setDistributorId(Long distributorId) {
	      if(distributorId == null)
	      {      
	      	distributorIdDistributorModel = null;
	      }
	      else
	      {
	        distributorIdDistributorModel = new DistributorModel();
	      	distributorIdDistributorModel.setDistributorId(distributorId);
	      }      
	   }

	   
	   @Column(name = "IS_DELETED")
		public Boolean getIsDeleted() {
			return isDeleted;
		}

		public void setIsDeleted(Boolean isDeleted) {
			this.isDeleted = isDeleted;
		}

	/**
	 * Helper method for Complex Example Queries
	 */
	@javax.persistence.Transient
	@Override
	public List<AssociationModel> getAssociationModelList() {
		List<AssociationModel> associationModelList = new ArrayList<AssociationModel>();
		AssociationModel associationModel = null;

		associationModel = new AssociationModel();

		associationModel.setClassName("TransactionTypeModel");
		associationModel
				.setPropertyName("relationTransactionTypeIdTransactionTypeModel");
		associationModel
				.setValue(getRelationTransactionTypeIdTransactionTypeModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("StakeholderBankInfoModel");
		associationModel
				.setPropertyName("relationStakeholderBankInfoIdStakeholderBankInfoModel");
		associationModel
				.setValue(getRelationStakeholderBankInfoIdStakeholderBankInfoModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("SegmentModel");
		associationModel.setPropertyName("relationSegmentIdSegmentModel");
		associationModel.setValue(getRelationSegmentIdSegmentModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("ProductModel");
		associationModel.setPropertyName("relationProductIdProductModel");
		associationModel.setValue(getRelationProductIdProductModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("PaymentModeModel");
		associationModel
				.setPropertyName("relationPaymentModeIdPaymentModeModel");
		associationModel.setValue(getRelationPaymentModeIdPaymentModeModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("DeviceTypeModel");
		associationModel
				.setPropertyName("relationDeviceTypeIdDeviceTypeModel");
		associationModel.setValue(getRelationDeviceTypeIdDeviceTypeModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("CommissionTypeModel");
		associationModel
				.setPropertyName("relationCommissionTypeIdCommissionTypeModel");
		associationModel
				.setValue(getRelationCommissionTypeIdCommissionTypeModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("CommissionStakeholderModel");
		associationModel
				.setPropertyName("relationCommissionStakeholderIdCommissionStakeholderModel");
		associationModel
				.setValue(getRelationCommissionStakeholderIdCommissionStakeholderModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("CommissionReasonModel");
		associationModel
				.setPropertyName("relationCommissionReasonIdCommissionReasonModel");
		associationModel
				.setValue(getRelationCommissionReasonIdCommissionReasonModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationCreatedByAppUserModel");
		associationModel.setValue(getRelationCreatedByAppUserModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationUpdatedByAppUserModel");
		associationModel.setValue(getRelationUpdatedByAppUserModel());

		associationModelList.add(associationModel);

		associationModel = new AssociationModel();

   		associationModel.setClassName("DistributorModel");
   		associationModel.setPropertyName("relationDistributorIdDistributorModel");   		
   		associationModel.setValue(getRelationDistributorIdDistributorModel());
		associationModelList.add(associationModel);
		
		return associationModelList;
	}


	@Override
	public Object mapRow(ResultSet resultSet, int i) throws SQLException {
		CommissionRateModel vo = new CommissionRateModel();
		vo.setTransactionTypeId(resultSet.getLong("TRANSACTION_TYPE_ID"));
		vo.setStakeholderBankInfoId(resultSet.getLong("STAKEHOLDER_BANK_INFO_ID"));
		vo.setSegmentId(resultSet.getLong("SEGMENT_ID"));
		vo.setProductId(resultSet.getLong("PRODUCT_ID"));
		vo.setPaymentModeId(resultSet.getLong("PAYMENT_MODE_ID"));
		vo.setCommissionTypeId(resultSet.getLong("COMMISSION_TYPE_ID"));
		vo.setCommissionStakeholderId(resultSet.getLong("COMMISSION_STAKEHOLDER_ID"));
		vo.setCommissionReasonId(resultSet.getLong("COMMISSION_REASON_ID"));
		vo.setCreatedBy(resultSet.getLong("CREATED_BY"));
		vo.setUpdatedBy(resultSet.getLong("UPDATED_BY"));
		vo.setDeviceTypeId(resultSet.getLong("DEVICE_TYPE_ID"));
		vo.setDistributorId(resultSet.getLong("DISTRIBUTOR_ID"));
		vo.setMnoId(resultSet.getLong("SERVICE_OP_ID"));
		vo.setCommissionRateId(resultSet.getLong("COMMISSION_RATE_ID"));
		vo.setRate(resultSet.getDouble("RATE"));
		vo.setFromDate(resultSet.getDate("FROM_DATE"));
		vo.setToDate(resultSet.getDate("TO_DATE"));
		vo.setActive(resultSet.getBoolean("IS_ACTIVE"));
		vo.setDescription(resultSet.getString("DESCRIPTION"));
		vo.setComments(resultSet.getString("COMMENTS"));
		vo.setUpdatedOn(resultSet.getDate("UPDATED_ON"));
		vo.setCreatedOn(resultSet.getDate("CREATED_ON"));
		vo.setVersionNo(resultSet.getInt("VERSION_NO"));
		vo.setRangeStarts(resultSet.getDouble("RANGE_STARTS"));
		vo.setRangeEnds(resultSet.getDouble("RANGE_ENDS"));
		vo.setExclusiveFixAmount(resultSet.getDouble("EXCLUSIVE_FIX_AMOUNT"));
		vo.setExclusivePercentAmount(resultSet.getDouble("EXCLUSIVE_PERCENT_AMOUNT"));
		vo.setInclusiveFixAmount(resultSet.getDouble("INCLUSIVE_FIX_AMOUNT"));
		vo.setInclusivePercentAmount(resultSet.getDouble("INCLUSIVE_PERCENT_AMOUNT"));
		vo.setIsDeleted(resultSet.getBoolean("IS_DELETED"));
		return vo;
	}
}
