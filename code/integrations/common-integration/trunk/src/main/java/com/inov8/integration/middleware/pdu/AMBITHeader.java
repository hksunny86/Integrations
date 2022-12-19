package com.inov8.integration.middleware.pdu;

import java.io.Serializable;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class AMBITHeader implements Serializable{

	private static final long serialVersionUID = -2937557822896595481L;
	private String messageType = "0200";
	private String transCode;
	private String transmissionDateTime;
	private String pan;

	public String build() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(trimToEmpty(messageType)).append(AMBIT_DELIMITER.getValue());
		stringBuilder.append(trimToEmpty(transCode)).append(AMBIT_DELIMITER.getValue());
		stringBuilder.append(transmissionDateTime).append(AMBIT_DELIMITER.getValue());
		stringBuilder.append(trimToEmpty(pan)).append(AMBIT_DELIMITER.getValue());

		return stringBuilder.toString();
	}
	
	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getTransmissionDateTime() {
		return transmissionDateTime;
	}

	public void setTransmissionDateTime(String transmissionDateTime) {
		this.transmissionDateTime = transmissionDateTime;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}
}
