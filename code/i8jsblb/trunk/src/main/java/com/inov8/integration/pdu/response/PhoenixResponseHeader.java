/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.inov8.integration.pdu.response;

import java.util.ArrayList;
import java.util.List;

import com.inov8.integration.enums.DataTypeEnum;
import com.inov8.integration.enums.DeliveryChannelTypeEnum;
import com.inov8.integration.enums.MessageTypeEnum;
import com.inov8.integration.enums.PhoenixEnum;
import com.inov8.integration.pdu.Field;
import com.inov8.integration.util.FieldUtil;

public class PhoenixResponseHeader {
	private short length = 239;
	private List<Field> headerFields = new ArrayList<Field>(16);
	// Length = A7
	private Field messageProtocol = new Field("PHXGDCI", 7, DataTypeEnum.A);
	// Length = N2
	private Field version = new Field("20", 2, DataTypeEnum.N);
	// Length = N3
	private Field fieldInError = new Field(null, 3, DataTypeEnum.N);
	// Length = N 4
	private Field messageType = new Field(MessageTypeEnum.MT_0200.getValue(), 4, DataTypeEnum.N);
	// Length = N 14
	private Field transmissionDateTime = new Field(FieldUtil.buildTransmissionDateTime(), 14, DataTypeEnum.N);
	// Length = N 2
	private Field deliveryChannelType = new Field(DeliveryChannelTypeEnum.MOBILE_BANKING.getValue(), 2, DataTypeEnum.N);
	// Length = AN 20
	private Field deliveryChannelId = new Field(PhoenixEnum.DELIVERY_CHANNEL_ID.getValue(), 20, DataTypeEnum.AN);
	// Length = AN 30
	private Field customerIdentification = new Field(null, 30, DataTypeEnum.AN);
	// Length = N 3
	private Field transactionCode = new Field(null, 3, DataTypeEnum.N);
	// Length = N 8
	private Field transactionDate = new Field(FieldUtil.buildTransactionDate(), 8, DataTypeEnum.N);
	// Length = N 6
	private Field transactionTime = new Field(FieldUtil.buildTransactionTime(), 6, DataTypeEnum.N);
	// Length = N 12
	private Field retrievalRefNumber = new Field(null, 12, DataTypeEnum.N);
	// Length = AN 20
	private Field customerPIN = new Field(null, 20, DataTypeEnum.AN);
	// Length = AN 64
	private Field channelSpecificData = new Field(null, 80, DataTypeEnum.AN);
	// Length = AN 20
	private Field channelPrivateData = new Field(null, 20, DataTypeEnum.AN);
	// Length = AN 6
	private Field authResponseId = new Field(null, 6, DataTypeEnum.AN);
	// Length = AN 2
	private Field responseCode = new Field(null, 2, DataTypeEnum.AN);

	public PhoenixResponseHeader() {
		headerFields.add(messageProtocol);
		headerFields.add(version);
		headerFields.add(fieldInError);
		headerFields.add(messageType);
		headerFields.add(transmissionDateTime);
		headerFields.add(deliveryChannelType);
		headerFields.add(deliveryChannelId);
		headerFields.add(customerIdentification);
		headerFields.add(transactionCode);
		headerFields.add(transactionDate);
		headerFields.add(transactionTime);
		headerFields.add(retrievalRefNumber);
		headerFields.add(customerPIN);
		headerFields.add(channelSpecificData);
		headerFields.add(channelPrivateData);
		headerFields.add(authResponseId);
		headerFields.add(responseCode);

	}

	public static void main(String[] args) {
		new PhoenixResponseHeader();
	}

	public List<Field> getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(List<Field> headerFields) {
		this.headerFields = headerFields;
	}

	public Field getMessageProtocol() {
		return messageProtocol;
	}

	public void setMessageProtocol(Field messageProtocol) {
		this.messageProtocol = messageProtocol;
	}

	public Field getVersion() {
		return version;
	}

	public void setVersion(Field version) {
		this.version = version;
	}

	public Field getFieldInError() {
		return fieldInError;
	}

	public void setFieldInError(Field fieldInError) {
		this.fieldInError = fieldInError;
	}

	public Field getMessageType() {
		return messageType;
	}

	public void setMessageType(Field messageType) {
		this.messageType = messageType;
	}

	public Field getTransmissionDateTime() {
		return transmissionDateTime;
	}

	public void setTransmissionDateTime(Field transmissionDateTime) {
		this.transmissionDateTime = transmissionDateTime;
	}

	public Field getDeliveryChannelType() {
		return deliveryChannelType;
	}

	public void setDeliveryChannelType(Field deliveryChannelType) {
		this.deliveryChannelType = deliveryChannelType;
	}

	public Field getDeliveryChannelId() {
		return deliveryChannelId;
	}

	public void setDeliveryChannelId(Field deliveryChannelId) {
		this.deliveryChannelId = deliveryChannelId;
	}

	public Field getCustomerIdentification() {
		return customerIdentification;
	}

	public void setCustomerIdentification(Field customerIdentification) {
		this.customerIdentification = customerIdentification;
	}

	public Field getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(Field transactionCode) {
		this.transactionCode = transactionCode;
	}

	public Field getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Field transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Field getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(Field transactionTime) {
		this.transactionTime = transactionTime;
	}

	public Field getRetrievalRefNumber() {
		return retrievalRefNumber;
	}

	public void setRetrievalRefNumber(Field retrievalRefNumber) {
		this.retrievalRefNumber = retrievalRefNumber;
	}

	public Field getCustomerPIN() {
		return customerPIN;
	}

	public void setCustomerPIN(Field customerPIN) {
		this.customerPIN = customerPIN;
	}

	public Field getChannelSpecificData() {
		return channelSpecificData;
	}

	public void setChannelSpecificData(Field channelSpecificData) {
		this.channelSpecificData = channelSpecificData;
	}

	public Field getChannelPrivateData() {
		return channelPrivateData;
	}

	public void setChannelPrivateData(Field channelPrivateData) {
		this.channelPrivateData = channelPrivateData;
	}

	public short getLength() {
		return length;
	}

	public void setLength(short length) {
		this.length = length;
	}

	public Field getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Field responseCode) {
		this.responseCode = responseCode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Field field : headerFields) {
			builder.append(field.getValue());
			builder.append("\n");
		}
		return builder.toString();
	}

	public Field getAuthResponseId() {
		return authResponseId;
	}

	public void setAuthResponseId(Field authResponseId) {
		this.authResponseId = authResponseId;
	}
}
