package com.inov8.hsm.pdu.parser.response;

import java.io.Serializable;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.parser.HeaderParser;
import com.inov8.hsm.pdu.response.GeneratePINBlockResponse;
import com.inov8.hsm.util.FieldUtil;

public class GeneratePINBlockResponseParser implements Serializable {

	private static final long serialVersionUID = -5285310022047911920L;

	public static GeneratePINBlockResponse parse(String hexMessage) {
		StringBuilder hexBuilder = new StringBuilder(hexMessage);

		BaseHeader baseHeader = HeaderParser.parseHeader(hexBuilder);
		GeneratePINBlockResponse response = new GeneratePINBlockResponse(baseHeader);

		response.setResponseCode(FieldUtil.extractString(hexBuilder, 2));
		response.setPinBlock(FieldUtil.extractString(hexBuilder, 16));
		response.setRawPdu(hexMessage.getBytes());
		
		return response;
	}

}
