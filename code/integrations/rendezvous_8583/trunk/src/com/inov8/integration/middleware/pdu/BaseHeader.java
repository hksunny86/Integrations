package com.inov8.integration.middleware.pdu;

import static org.apache.commons.lang.StringUtils.trimToEmpty;

import com.inov8.integration.middleware.util.FieldUtil;

public class BaseHeader {

	private String messageIdentifer = "RENDEZVOUS-8583";
	private String messageVersion = "1.1";
	private String fieldInError = "000";
	private String messageReceiveTime;
	private String messageType;

	public String build() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(trimToEmpty(messageIdentifer));
		stringBuilder.append(trimToEmpty(messageVersion));
		stringBuilder.append(trimToEmpty(fieldInError));
		stringBuilder.append(trimToEmpty(FieldUtil.buildMessageRecieveTime()));

		return stringBuilder.toString();
	}

	public String getMessageIdentifer() {
		return messageIdentifer;
	}

	public void setMessageIdentifer(String messageIdentifer) {
		this.messageIdentifer = messageIdentifer;
	}

	public String getMessageVersion() {
		return messageVersion;
	}

	public void setMessageVersion(String messageVersion) {
		this.messageVersion = messageVersion;
	}

	public String getFieldInError() {
		return fieldInError;
	}

	public void setFieldInError(String fieldInError) {
		this.fieldInError = fieldInError;
	}

	public String getMessageReceiveTime() {
		return messageReceiveTime;
	}

	public void setMessageReceiveTime(String messageReceiveTime) {
		this.messageReceiveTime = messageReceiveTime;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	
	
}
