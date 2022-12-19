package com.inov8.microbank.common.model;

import com.inov8.framework.common.model.AssociationModel;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.disbursement.model.BulkDisbursementsFileInfoModel;
import com.inov8.microbank.disbursement.vo.BulkDisbursementsXmlVo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The BulkDisbursementsModel entity bean.
 *
 * @author Fahad Tariq Inov8 Limited
 * @version $Revision: 1.20 $, $Date: 2006/03/06 19:29:08 $
 *
 *
 * @spring.bean name="BulkDisbursementsModel"
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@org.hibernate.annotations.GenericGenerator(name="BULK_DISBURSEMENTS_seq", strategy = "sequence", parameters = { @org.hibernate.annotations.Parameter(name="sequence", value="BULK_DISBURSEMENTS_seq") } )
//@javax.persistence.SequenceGenerator(name = "BULK_DISBURSEMENTS_seq", sequenceName = "BULK_DISBURSEMENTS_seq")
@Table(name = "BULK_DISBURSEMENTS")
public class BulkDisbursementsModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = -4949320924559852968L;

	private AppUserModel createdByAppUserModel;
	private AppUserModel updatedByAppUserModel;
	private Date createdOn;
	private Date updatedOn;

	private Long bulkDisbursementsId;
	private String employeeNo;
	private String name;
	private String mobileNo;
	private String cnic;
	private Long serviceId;
	private String chequeNo;
	private Double amount;
	private Double charges;
	private Double fed;
	private String paymentDateStr;
	private Date paymentDate;
	private Boolean settled;
	private Boolean posted;
	private String batchNumber;
	private Date creationDate;
	private String description;
	private Boolean validRecord;
	private Boolean deleted;
	private Boolean limitApplicable;
	private Boolean payCashViaCnic;
	private Boolean biometricVerification;
	private Date postedOn;
	private Date settledOn;
	private String transactionCode;
	private Integer versionNo;
	transient private String sourceACNo;

	//transient
	private AppUserModel appUserIdAppUserModel;
	private ProductModel productIdProductModel;
	private String accountCreationStatus;
	private String failureReason;
	private Long customerAccountTypeId;
	private String productName;
	private String serviceName;
	private String reason;
	private String isApproved;
	private BulkDisbursementsFileInfoModel fileInfoIdBulkDisbursementsFileInfoModel;
	private transient Long fileInfoId = null;
	/**
	 * Default constructor.
	 */
	public BulkDisbursementsModel() {
	}

	/**
	 * Return the primary key.
	 *
	 * @return Long with the primary key.
	 */
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getBulkDisbursementsId();
	}

	/**
	 * Set the primary key.
	 *
	 * @param primaryKey
	 *            the primary key
	 */
	@javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
		setBulkDisbursementsId(primaryKey);
	}

	/**
	 * Returns the value of the <code>bulkDisbursementsId</code> property.
	 *
	 */
	@Column(name = "BULK_DISBURSEMENTS_ID", nullable = false)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BULK_DISBURSEMENTS_seq")
	public Long getBulkDisbursementsId() {
		return bulkDisbursementsId;
	}

	/**
	 * Sets the value of the <code>bulkDisbursementsId</code> property.
	 *
	 * @param bulkDisbursementsId
	 *            the value for the <code>bulkDisbursementsId</code> property
	 *
	 */

	public void setBulkDisbursementsId(Long bulkDisbursementsId) {
		this.bulkDisbursementsId = bulkDisbursementsId;
	}

	/**
	 * Returns the value of the <code>employeeNo</code> property.
	 *
	 */
	@Column(name = "EMPLOYEE_NO", nullable = false, length = 100)
	public String getEmployeeNo() {
		return employeeNo;
	}

	/**
	 * Sets the value of the <code>employeeNo</code> property.
	 *
	 * @param employeeNo
	 *            the value for the <code>employeeNo</code> property
	 *
	 * @spring.validator type="required"
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="100"
	 */

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	/**
	 * Returns the value of the <code>name</code> property.
	 *
	 */
	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the <code>name</code> property.
	 *
	 * @param name
	 *            the value for the <code>name</code> property
	 *
	 * @spring.validator type="required"
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="250"
	 * @spring.validator type="mask"
	 * @spring.validator-args arg1value="${mask}"
	 * @spring.validator-var name="mask" value="^[a-zA-Z0-9]*$"
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the value of the <code>mobileNo</code> property.
	 *
	 */
	@Column(name = "MOBILE_NO", nullable = false, length = 13)
	public String getMobileNo() {
		return mobileNo;
	}

	/**
	 * Sets the value of the <code>mobileNo</code> property.
	 *
	 * @param mobileNo
	 *            the value for the <code>mobileNo</code> property
	 *
	 * @spring.validator type="required"
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="13"
	 */

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Column(name="CNIC")
	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}

	/**
	 * Returns the value of the <code>type</code> property.
	 *
	 */
	@Column(name = "SERVICE_ID", nullable = false)
	public Long getServiceId() {
		return serviceId;
	}

	/**
	 * Sets the value of the <code>type</code> property.
	 *
	 * @param //type
	 *            the value for the <code>type</code> property
	 *
	 * @spring.validator type="required"
	 * @spring.validator type="integer"
	 */

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * Returns the value of the <code>chequeNo</code> property.
	 *
	 */
	@Column(name = "CHEQUE_NO", nullable = false, length = 20)
	public String getChequeNo() {
		return chequeNo;
	}

	/**
	 * Sets the value of the <code>chequeNo</code> property.
	 *
	 * @param chequeNo
	 *            the value for the <code>chequeNo</code> property
	 *
	 * @spring.validator type="required"
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="20"
	 */

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	/**
	 * Returns the value of the <code>amount</code> property.
	 *
	 */
	@Column(name = "AMOUNT", nullable = false)
	public Double getAmount() {
		return amount;
	}

	/**
	 * Sets the value of the <code>amount</code> property.
	 *
	 * @param amount
	 *            the value for the <code>amount</code> property
	 *
	 * @spring.validator type="required"
	 * @spring.validator type="double"
	 * @spring.validator type="doubleRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="99999999999.9999"
	 */

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * Returns the value of the <code>paymentDate</code> property.
	 *
	 */
	@Column(name = "PAYMENT_DATE", nullable = false)
	public Date getPaymentDate() {
		return paymentDate;
	}

	/**
	 * Sets the value of the <code>paymentDate</code> property.
	 *
	 * @param paymentDate
	 *            the value for the <code>paymentDate</code> property
	 *
	 * @spring.validator type="required"
	 * @spring.validator type="date"
	 * @spring.validator-var name="datePattern" value="${date_format}"
	 */

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Transient
	public String getPaymentDateStr() {
		return paymentDateStr;
	}

	public void setPaymentDateStr(String paymentDateStr) {
		this.paymentDateStr = paymentDateStr;
	}

	@Column(name = "POSTED")
	public Boolean getPosted() {
		return posted;
	}

	public void setPosted(Boolean posted) {
		this.posted = posted;
	}

	/**
	 * Returns the value of the <code>settled</code> property.
	 *
	 */
	@Column(name = "SETTLED")
	public Boolean getSettled() {
		return settled;
	}

	/**
	 * Sets the value of the <code>settled</code> property.
	 *
	 * @param settled
	 *            the value for the <code>settled</code> property
	 *
	 * @spring.validator type="required"
	 * @spring.validator type="integer"
	 */

	public void setSettled(Boolean settled) {
		this.settled = settled;
	}

	/**
	 * Returns the value of the <code>batchNumber</code> property.
	 *
	 */
	@Column(name = "BATCH_NUMBER", nullable = false)
	public String getBatchNumber() {
		return batchNumber;
	}

	/**
	 * Sets the value of the <code>batchNumber</code> property.
	 *
	 * @param //batchNumber
	 *            the value for the <code>batchNumber</code> property
	 *
	 * @spring.validator type="required"
	 * @spring.validator type="long"
	 * @spring.validator type="longRange"
	 * @spring.validator-args arg1value="${var:min}"
	 * @spring.validator-var name="min" value="0"
	 * @spring.validator-args arg2value="${var:max}"
	 * @spring.validator-var name="max" value="9999999999"
	 */

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	/**
	 * Returns the value of the <code>creationDate</code> property.
	 *
	 */
	@Column(name = "CREATION_DATE")
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the value of the <code>creationDate</code> property.
	 *
	 * @param creationDate
	 *            the value for the <code>creationDate</code> property
	 *
	 * @spring.validator type="required"
	 * @spring.validator type="date"
	 * @spring.validator-var name="datePattern" value="${date_format}"
	 */

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Returns the value of the <code>description</code> property.
	 *
	 */
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	@Column(name = "DELETED")
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Column(name = "LIMIT_APPLICABLE")
	public Boolean getLimitApplicable() {
		return limitApplicable;
	}

	public void setLimitApplicable(Boolean limitApplicable) {
		this.limitApplicable = limitApplicable;
	}

	@Column(name = "PAY_CASH_VIA_CNIC")
	public Boolean getPayCashViaCnic() {
		return payCashViaCnic;
	}

	public void setPayCashViaCnic(Boolean payCashViaCnic) {
		this.payCashViaCnic = payCashViaCnic;
	}

	/**
	 * Sets the value of the <code>description</code> property.
	 *
	 * @param description
	 *            the value for the <code>description</code> property
	 *
	 * @spring.validator type="required"
	 * @spring.validator type="maxlength"
	 * @spring.validator-args arg1value="${var:maxlength}"
	 * @spring.validator-var name="maxlength" value="100"
	 */

	public void setDescription(String description) {
		this.description = description;
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
	 * Returns the value of the <code>updatedByAppUserModel</code> relation property.
	 *
	 * @return the value of the <code>updatedByAppUserModel</code> relation property.
	 *
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "UPDATED_BY")
	public AppUserModel getRelationUpdatedByAppUserModel(){
		return updatedByAppUserModel;
	}

	@Column(name = "POSTED_ON")
	public Date getPostedOn() {
		return postedOn;
	}

	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Column(name = "SETTLED_ON")
	public Date getSettledOn() {
		return settledOn;
	}

	public void setSettledOn(Date settledOn) {
		this.settledOn = settledOn;
	}

	/**
	 * Returns the value of the <code>updatedByAppUserModel</code> relation property.
	 *
	 * @return the value of the <code>updatedByAppUserModel</code> relation property.
	 *
	 */
	@javax.persistence.Transient
	public AppUserModel getUpdatedByAppUserModel(){
		return getRelationUpdatedByAppUserModel();
	}

	/**
	 * Sets the value of the <code>updatedByAppUserModel</code> relation property.
	 *
	 * @param appUserModel a value for <code>updatedByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationUpdatedByAppUserModel(AppUserModel appUserModel) {
		this.updatedByAppUserModel = appUserModel;
	}

	/**
	 * Sets the value of the <code>updatedByAppUserModel</code> relation property.
	 *
	 * @param appUserModel a value for <code>updatedByAppUserModel</code>.
	 */
	@javax.persistence.Transient
	public void setUpdatedByAppUserModel(AppUserModel appUserModel) {
		if(null != appUserModel)
		{
			setRelationUpdatedByAppUserModel((AppUserModel)appUserModel.clone());
		}
	}

	@javax.persistence.Transient
	public Long getUpdatedBy() {
		if (updatedByAppUserModel != null) {
			return updatedByAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setUpdatedBy(Long appUserId) {
		if(appUserId == null)
		{
			updatedByAppUserModel = null;
		}
		else
		{
			updatedByAppUserModel = new AppUserModel();
			updatedByAppUserModel.setAppUserId(appUserId);
		}
	}

	@Column(name="CREATION_DATE", insertable=false, updatable=false )
	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name="UPDATED_ON")
	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
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
			setRelationCreatedByAppUserModel((AppUserModel) appUserModel.clone());
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

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "APP_USER_ID")
	public AppUserModel getRelationAppUserIdAppUserModel(){
		return appUserIdAppUserModel;
	}

	@javax.persistence.Transient
	public AppUserModel getAppUserIdAppUserModel(){
		return getRelationAppUserIdAppUserModel();
	}

	@javax.persistence.Transient
	public void setRelationAppUserIdAppUserModel(AppUserModel appUserModel) {
		this.appUserIdAppUserModel = appUserModel;
	}

	@javax.persistence.Transient
	public void setAppUserIdAppUserModel(AppUserModel appUserModel) {
		if(null != appUserModel) {
			setRelationAppUserIdAppUserModel((AppUserModel)appUserModel.clone());
		}
	}


	@javax.persistence.Transient
	public Long getAppUserId() {
		if (appUserIdAppUserModel != null) {
			return appUserIdAppUserModel.getAppUserId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setAppUserId(Long appUserId) {
		if(appUserId == null) {
			appUserIdAppUserModel = null;
		}
		else
		{
			appUserIdAppUserModel = new AppUserModel();
			appUserIdAppUserModel.setAppUserId(appUserId);
		}
	}

	/**
	 * Returns the value of the <code>productIdProductModel</code> relation property.
	 *
	 * @return the value of the <code>productIdProductModel</code> relation property.
	 *
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	public ProductModel getRelationProductIdProductModel(){
		return productIdProductModel;
	}

	/**
	 * Returns the value of the <code>productIdProductModel</code> relation property.
	 *
	 * @return the value of the <code>productIdProductModel</code> relation property.
	 *
	 */
	@javax.persistence.Transient
	public ProductModel getProductIdProductModel(){
		return getRelationProductIdProductModel();
	}

	/**
	 * Sets the value of the <code>productIdProductModel</code> relation property.
	 *
	 * @param productModel a value for <code>productIdProductModel</code>.
	 */
	@javax.persistence.Transient
	public void setRelationProductIdProductModel(ProductModel productModel) {
		this.productIdProductModel = productModel;
	}

	@javax.persistence.Transient
	public void setProductIdProductModel(ProductModel productModel) {
		if(null != productModel)
		{
			setRelationProductIdProductModel((ProductModel)productModel.clone());
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
	 * @param productId the value for the <code>productId</code> property
	 * @spring.validator type="required"
	 */

	@javax.persistence.Transient
	public void setProductId(Long productId) {
		if(productId == null)
		{
			productIdProductModel = null;
		}
		else
		{
			productIdProductModel = new ProductModel();
			productIdProductModel.setProductId(productId);
		}
	}

	@Column(name="ACCOUNT_CREATION_STATUS")
	public String getAccountCreationStatus() {
		return accountCreationStatus;
	}

	public void setAccountCreationStatus(String status) {
		this.accountCreationStatus = status;
	}

	@Column(name="FAILURE_REASON")
	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	/**
	 * Used by the display tag library for rendering a checkbox in the list.
	 *
	 * @return String with a HTML checkbox.
	 */
	@Transient
	public String getCheckbox() {
		String checkBox = "<input type=\"checkbox\" name=\"checkbox";
		checkBox += "_" + getBulkDisbursementsId();
		checkBox += "\"/>";
		return checkBox;
	}

	/**
	 * Helper method for Struts with displaytag
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		String parameters = "";
		parameters += "&bulkDisbursementsId=" + getBulkDisbursementsId();
		return parameters;
	}

	/**
	 * Helper method for default Sorting on Primary Keys
	 */
	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "bulkDisbursementsId";
		return primaryKeyFieldName;
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
		associationModel.setClassName("AppUserModel");
		associationModel.setPropertyName("relationAppUserIdAppUserModel");
		associationModel.setValue(getRelationAppUserIdAppUserModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("ProductModel");
		associationModel.setPropertyName("relationProductIdProductModel");
		associationModel.setValue(getRelationProductIdProductModel());
		associationModelList.add(associationModel);

		associationModel = new AssociationModel();
		associationModel.setClassName("BulkDisbursementsFileInfoModel");
		associationModel.setPropertyName("relationFileInfoIdBulkDisbursementsFileInfoModel");
		associationModel.setValue(getRelationFileInfoIdBulkDisbursementsFileInfoModel());
		associationModelList.add(associationModel);

		return associationModelList;
	}

	public BulkDisbursementsXmlVo toBulkDisbursementsXmlVo()
	{
		BulkDisbursementsXmlVo vo = new BulkDisbursementsXmlVo();
		vo.setBulkDisbursementsId(bulkDisbursementsId);
		vo.setCnic(cnic);
		vo.setMobileNo(mobileNo);
		vo.setCreatedBy(getCreatedBy());
		return vo;
	}

	@Column(name = "IS_VALID_RECORD" , nullable = false )
	public Boolean getValidRecord() {
		return validRecord;
	}

	public void setValidRecord(Boolean validRecord) {
		this.validRecord = validRecord;
	}

	@Column(name="TRANSACTION_CODE")
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	@Version
	@Column(name = "VERSION_NO" , nullable = false )
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	@Transient
	public String getSourceACNo() {
		return sourceACNo;
	}

	@Column(name = "REASON" )
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "IS_BIOMETRIC_REQUIRED" )
	public Boolean getBiometricVerification() {
		return biometricVerification;
	}

	public void setBiometricVerification(Boolean biometricVerification) {
		this.biometricVerification = biometricVerification;
	}

	@Column(name = "CHARGES" )
	public Double getCharges() {
		return charges;
	}

	public void setCharges(Double charges) {
		this.charges = charges;
	}

	@Column(name = "FED" )
	public Double getFed() {
		return fed;
	}

	public void setFed(Double fed) {
		this.fed = fed;
	}

	public void setSourceACNo(String sourceACNo) {
		this.sourceACNo = sourceACNo;
	}

	@Transient
	public Long getCustomerAccountTypeId() {
		return customerAccountTypeId;
	}

	public void setCustomerAccountTypeId(Long customerAccountTypeId) {
		this.customerAccountTypeId = customerAccountTypeId;
	}

	@Transient
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Transient
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "BULK_DISB_FILE_INFO_ID")
	public BulkDisbursementsFileInfoModel getRelationFileInfoIdBulkDisbursementsFileInfoModel(){
		return fileInfoIdBulkDisbursementsFileInfoModel;
	}

	@javax.persistence.Transient
	public BulkDisbursementsFileInfoModel getFileInfoIdBulkDisbursementsFileInfoModel(){
		return getRelationFileInfoIdBulkDisbursementsFileInfoModel();
	}

	@javax.persistence.Transient
	public void setRelationFileInfoIdBulkDisbursementsFileInfoModel(BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel) {
		this.fileInfoIdBulkDisbursementsFileInfoModel = bulkDisbursementsFileInfoModel;
	}

	@javax.persistence.Transient
	public void setFileInfoIdBulkDisbursementsFileInfoModel(BulkDisbursementsFileInfoModel bulkDisbursementsFileInfoModel) {
		if(null != bulkDisbursementsFileInfoModel) {
			setRelationFileInfoIdBulkDisbursementsFileInfoModel((BulkDisbursementsFileInfoModel)bulkDisbursementsFileInfoModel.clone());
		}
	}

	@javax.persistence.Transient
	public Long getFileInfoId() {
		if (fileInfoIdBulkDisbursementsFileInfoModel != null) {
			return fileInfoIdBulkDisbursementsFileInfoModel.getFileInfoId();
		} else {
			return null;
		}
	}

	@javax.persistence.Transient
	public void setFileInfoId(Long fileInfoId) {
		if(fileInfoId == null) {
			fileInfoIdBulkDisbursementsFileInfoModel = null;
		}
		else
		{
			fileInfoIdBulkDisbursementsFileInfoModel = new BulkDisbursementsFileInfoModel();
			fileInfoIdBulkDisbursementsFileInfoModel.setFileInfoId(fileInfoId);
		}
	}

	@Column(name="ISAPPROVED")
	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}
}