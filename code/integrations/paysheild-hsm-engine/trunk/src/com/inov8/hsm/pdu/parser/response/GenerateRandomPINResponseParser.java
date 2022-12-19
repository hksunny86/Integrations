package com.inov8.hsm.pdu.parser.response;

import java.io.Serializable;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.parser.HeaderParser;
import com.inov8.hsm.pdu.response.GenerateRandomPINResponse;
import com.inov8.hsm.util.FieldUtil;

public class GenerateRandomPINResponseParser implements Serializable {
	
	private static final long serialVersionUID = -2763764380697247586L;

	public static GenerateRandomPINResponse parse(String hexMessage) {
		StringBuilder hexBuilder = new StringBuilder(hexMessage);

		BaseHeader baseHeader = HeaderParser.parseHeader(hexBuilder);
		GenerateRandomPINResponse response = new GenerateRandomPINResponse(baseHeader);

		response.setResponseCode(FieldUtil.extractString(hexBuilder, 2));
		response.setPIN(FieldUtil.extractString(hexBuilder, 5));
		response.setRawPdu(hexMessage.getBytes());

		return response;
	}

}
