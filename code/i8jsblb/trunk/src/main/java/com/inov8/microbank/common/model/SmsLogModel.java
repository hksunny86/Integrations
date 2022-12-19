package com.inov8.microbank.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inov8.framework.common.model.BasePersistableModel;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true)
@Table(name = "SMS_LOG")
public class SmsLogModel extends BasePersistableModel implements Serializable{
	
	private Long smsLogId;
	private String recipientMobileNumber;
	private String messageId;
	private Date sentDateTime;
	
	@Column(name = "SMS_LOG_ID" , nullable = false )
	@Id
	public Long getSmsLogId() {
		return smsLogId;
	}
	public void setSmsLogId(Long smsLogId) {
		this.smsLogId = smsLogId;
	}
	
    @Column(name = "RECIPIENT_MOBILE_NUMBER")
	public String getRecipientMobileNumber() {
		return recipientMobileNumber;
	}
	public void setRecipientMobileNumber(String recipientMobileNumber) {
		this.recipientMobileNumber = recipientMobileNumber;
	}
	
    @Column(name = "MESSAGE_ID")
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
    @Column(name = "SENT_DATE_TIME")
	public Date getSentDateTime() {
		return sentDateTime;
	}
	public void setSentDateTime(Date sentDateTime) {
		this.sentDateTime = sentDateTime;
	}
	
	@Override
	@javax.persistence.Transient
	public Long getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	  @javax.persistence.Transient
	public String getPrimaryKeyFieldName() {
		String primaryKeyFieldName = "smsLogId";
		return primaryKeyFieldName;				
	}
	@Override
	  @javax.persistence.Transient
	public String getPrimaryKeyParameter() {
		 String parameters = "";
	      parameters += "&smsLogId=" + getSmsLogId();
	      return parameters;
	}
	@Override
	  @javax.persistence.Transient
	public void setPrimaryKey(Long primaryKey) {
	     setSmsLogId(primaryKey);
		
	}
	
	
	
	
}
