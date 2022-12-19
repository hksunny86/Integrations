package com.inov8.microbank.tax.model;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.model.AppUserModel;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "DAILY_WHT_DEDUCTION_SEQ",sequenceName = "DAILY_WHT_DEDUCTION_SEQ", allocationSize=1)
@Table(name = "DAILY_WHT_DEDUCTION")
public class DailyWhtDeductionModel extends BasePersistableModel implements Serializable {

	private static final long serialVersionUID = -1L;

	private Long dailyWhtDeductionId;
	private Double amount;
	private AppUserModel appUserIdAppUserModel;
	private WHTConfigModel whtConfigIdWhtConfigModel;
	private Integer status;
	private String responseCode;
	private Date createdOn;
	private Date updatedOn;
	private AppUserModel updatedByAppUserModel;
	private AppUserModel createdByAppUserModel;
	private Long transactionCodeId;
	private Integer versionNo;
	private String desc;
	private Date WHTDedTransactionDate;
	private Double filerRate;
	private Double nonFilerRate;
	private Boolean isFiler;
	private Double sumAmount;
	
	//transient
	private Date start_date;
	private Date transaction_date;

	public DailyWhtDeductionModel() {}

	public DailyWhtDeductionModel(Long DailyWhtDeductionId) {
		
		this.setPrimaryKey(DailyWhtDeductionId);
	}
	
    @Id 
    @Column(name = "DAILY_WHT_DEDUCTION_ID" , nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DAILY_WHT_DEDUCTION_SEQ")
	public Long getDailyWhtDeductionId() {
		return this.dailyWhtDeductionId;
	}

	public void setDailyWhtDeductionId(Long DailyWhtDeductionId) {
		this.dailyWhtDeductionId = DailyWhtDeductionId;
	}

	@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
	@JoinColumn(name = "APP_USER_ID")
	public AppUserModel getAppUserIdAppUserModel() {
		return this.appUserIdAppUserModel;
	}

	public void setAppUserIdAppUserModel(AppUserModel appUserByAppUserId) {
		this.appUserIdAppUserModel = appUserByAppUserId;
	}

	@javax.persistence.Transient
	public Long getAppUserId()
	{
		Long appUserId = null;
		if(appUserIdAppUserModel != null)
		{
			appUserId = appUserIdAppUserModel.getAppUserId();
		}
		return appUserId;
	}
	@javax.persistence.Transient
	public void setAppUserId(Long appUserId){
		if(null!=appUserId){
			this.appUserIdAppUserModel= new AppUserModel();
			appUserIdAppUserModel.setAppUserId(appUserId);
		}
	}

	@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
	@JoinColumn(name = "WHT_CONFIG_ID" )
	public WHTConfigModel getWhtConfigIdWhtConfigModel(){
		return this.whtConfigIdWhtConfigModel;
	}

	public void setWhtConfigIdWhtConfigModel(WHTConfigModel whtConfigIdWhtConfigModel){
		this.whtConfigIdWhtConfigModel = whtConfigIdWhtConfigModel;
	}

	@javax.persistence.Transient
	public Long getWhtConfigId() {
		Long whtConfigId =  null;
		if (whtConfigIdWhtConfigModel != null){
			whtConfigId = whtConfigIdWhtConfigModel.getWhtConfigId();
		}
		return whtConfigId;
	}

	public void setWhtConfigId(Long whtConfigId) {
		if (null != whtConfigId){
			this.whtConfigIdWhtConfigModel = new WHTConfigModel();
			whtConfigIdWhtConfigModel.setWhtConfigId(whtConfigId);
		}else{
			whtConfigIdWhtConfigModel = null;
		}

	}

	@Column(name = "AMOUNT" )
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Column(name = "STATUS" )
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "CREATED_ON" )
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

    @Column(name = "UPDATED_ON" )
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
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
	
    @Version 
    @Column(name = "VERSION_NO")
	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}
	
	
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		return getDailyWhtDeductionId();
	}

	public void setPrimaryKey(Long primaryKey) {
		setDailyWhtDeductionId(primaryKey);
	}

	@javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		return "&dailyWhtDeductionId=" + getDailyWhtDeductionId();
	}

	@javax.persistence.Transient
	public String getPrimaryKeyFieldName() { 
		return "dailyWhtDeductionId";
	}

	@Column(name = "RESPONSE_CODE" )
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@Column(name = "TRANSACTION_CODE_ID" )
	public Long getTransactionCodeId() {
		return transactionCodeId;
	}

	public void setTransactionCodeId(Long transactionCode) {
		this.transactionCodeId = transactionCode;
	}

	@Column(name = "DESCRIPTION" )
	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	@Transient
	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	@Transient
	public Date getTransaction_date() {
		return transaction_date;
	}

	public void setTransaction_date(Date transaction_date) {
		this.transaction_date = transaction_date;
	}

	@Column(name = "TRANSACTION_DATE")
	public Date getWHTDedTransactionDate() {
		return WHTDedTransactionDate;
	}

	public void setWHTDedTransactionDate(Date wHTDedTransactionDate) {
		WHTDedTransactionDate = wHTDedTransactionDate;
	}

	@Column(name = "FILER_RATE")
	public Double getFilerRate() {
		return filerRate;
	}

	public void setFilerRate(Double filerRate) {
		this.filerRate = filerRate;
	}

	@Column(name = "NON_FILER_RATE")
	public Double getNonFilerRate() {
		return nonFilerRate;
	}

	public void setNonFilerRate(Double nonFilerRate) {
		this.nonFilerRate = nonFilerRate;
	}

	@Column(name = "IS_FILER")
	public Boolean getIsFiler() {
		return isFiler;
	}

	public void setIsFiler(Boolean isFiler) {
		this.isFiler = isFiler;
	}

	@Column(name = "SUM_AMOUNT")
	public Double getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(Double sumAmount) {
		this.sumAmount = sumAmount;
	}
}