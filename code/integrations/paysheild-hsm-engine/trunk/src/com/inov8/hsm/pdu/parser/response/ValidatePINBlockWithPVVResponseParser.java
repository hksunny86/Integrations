package com.inov8.hsm.pdu.parser.response;

import java.io.Serializable;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.parser.HeaderParser;
import com.inov8.hsm.pdu.response.ValidatePINBlockWithPVVResponse;
import com.inov8.hsm.util.FieldUtil;

public class ValidatePINBlockWithPVVResponseParser implements Serializable {

	private static final long serialVersionUID = 2297628885304541638L;

	public static ValidatePINBlockWithPVVResponse parse(String hexMessage) {
		StringBuilder hexBuilder = new StringBuilder(hexMessage);

		BaseHeader baseHeader = HeaderParser.parseHeader(hexBuilder);
		ValidatePINBlockWithPVVResponse response = new ValidatePINBlockWithPVVResponse(baseHeader);

		response.setResponseCode(FieldUtil.extractString(hexBuilder, 2));
		response.setRawPdu(hexMessage.getBytes());
		
		return response;
	}


}
