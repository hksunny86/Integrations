package com.inov8.microbank.common.model.messagemodule;

import java.io.Serializable;

public class SmsMessage implements Serializable
{
	private static final long serialVersionUID = -9092045361360147857L;
	private String mobileNo;
	private String messageText;
	private String sender;
	private String from;
	private String title;
	private String messageType;


	public String getMessageType() {
		return this.messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public SmsMessage(String mobileNo,String messageText)
	{
		this.mobileNo = mobileNo;
		this.messageText = messageText;
	}
	public SmsMessage(String mobileNo,String messageText, String sender)
	{
		this.mobileNo = mobileNo;
		this.messageText = messageText;
		this.sender = sender;
	}
	public String getMessageText()
	{
		return messageText;
	}
	public void setMessageText(String messageText)
	{
		this.messageText = messageText;
	}
	public String getMobileNo()
	{
		return mobileNo;
	}
	public void setMobileNo(String mobileNo)
	{
		this.mobileNo = mobileNo;
	}
	
	@Override
	public String toString()
	{
		return new StringBuilder().append(mobileNo).toString();
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
}
