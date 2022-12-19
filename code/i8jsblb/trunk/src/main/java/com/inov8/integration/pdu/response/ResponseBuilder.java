package com.inov8.integration.pdu.response;

import com.inov8.integration.pdu.request.PhoenixRequestHeader;
import com.inov8.integration.util.FieldUtil;

public class ResponseBuilder {
	public static void prepareResponseHeader(PhoenixRequestHeader requestHeader, PhoenixResponseHeader responseHeader) {
		responseHeader.getMessageProtocol().setValue(requestHeader.getMessageProtocol().getValue());
		responseHeader.getVersion().setValue(requestHeader.getVersion().getValue());
		responseHeader.getTransmissionDateTime().setValue(FieldUtil.buildTransmissionDateTime());
		responseHeader.getDeliveryChannelType().setValue(requestHeader.getDeliveryChannelType().getValue());
		responseHeader.getDeliveryChannelId().setValue(requestHeader.getDeliveryChannelId().getValue());
		responseHeader.getCustomerIdentification().setValue(requestHeader.getCustomerIdentification().getValue());
		responseHeader.getTransactionCode().setValue(requestHeader.getTransactionCode().getValue());
		responseHeader.getTransactionDate().setValue(requestHeader.getTransactionDate().getValue());
		responseHeader.getTransactionTime().setValue(requestHeader.getTransactionTime().getValue());
		responseHeader.getRetrievalRefNumber().setValue(requestHeader.getRetrievalRefNumber().getValue());
		responseHeader.getChannelSpecificData().setValue(requestHeader.getChannelSpecificData().getValue());
		responseHeader.getChannelPrivateData().setValue(requestHeader.getChannelPrivateData().getValue());
	}
}
