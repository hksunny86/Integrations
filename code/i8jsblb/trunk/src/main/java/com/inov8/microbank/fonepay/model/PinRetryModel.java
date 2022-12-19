package com.inov8.microbank.fonepay.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@javax.persistence.SequenceGenerator(name = "PIN_RETRY_SEQ",sequenceName = "PIN_RETRY_SEQ", allocationSize=1)
@Table(name = "PIN_RETRY")
public class PinRetryModel extends BasePersistableModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5687664004480782559L;
	private Long pinRetryId;
	private Long appUserId;
	private String mobileNO;
	private String cnic;
	private Long pinRetryCount;
	private Date updated_on;
	private Boolean isBlocked;
	private String channelId;
	private String pinRefreshTime;
	
	
	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PIN_RETRY_SEQ")
	@Column(name = "PIN_RETRY_ID")
	public Long getPinRetryId() {
		return pinRetryId;
	}

	public void setPinRetryId(Long pinRetryId) {
		this.pinRetryId = pinRetryId;
	}

	@Column(name = "APP_USER_ID")
	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	@Column(name = "MOBILE_NO")
	public String getMobileNO() {
		return mobileNO;
	}

	public void setMobileNO(String mobileNO) {
		this.mobileNO = mobileNO;
	}

	@Column(name = "CNIC")
	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}

	@Column(name = "PIN_RETRY_COUNT")
	public Long getPinRetryCount() {
		return pinRetryCount;
	}

	public void setPinRetryCount(Long pinRetryCount) {
		this.pinRetryCount = pinRetryCount;
	}

	@Column(name = "UPDATED_ON")
	public Date getUpdated_on() {
		return updated_on;
	}

	public void setUpdated_on(Date updated_on) {
		this.updated_on = updated_on;
	}

	@Column(name = "IS_BLOCKED")
	public Boolean getIsBlocked() {
		return isBlocked;
	}

	public void setIsBlocked(Boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	@Column(name = "CHANNEL_ID")
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Column(name = "PIN_REFRESH_TIME")
	public String getPinRefreshTime() {
		return pinRefreshTime;
	}

	public void setPinRefreshTime(String pinRefreshTime) {
		this.pinRefreshTime = pinRefreshTime;
	}

	@Transient
	public Long getPrimaryKey() {
		// TODO Auto-generated method stub
		return getPinRetryId();
	}

	@Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "pinRetryId";
		return primaryKeyFieldName;	
	}

	@Transient
	public String getPrimaryKeyParameter() {
	      String parameters = "";
	      parameters += "&pinRetryId=" + getPinRetryCount();
	      return parameters;
	}

	@Transient
	public void setPrimaryKey(Long arg0) {
		setPinRetryId(arg0);
		
	}

}

