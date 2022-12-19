package com.inov8.hsm.pdu.parser.response;

import java.io.Serializable;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.parser.HeaderParser;
import com.inov8.hsm.pdu.response.VerifyAndGeneratePVVResponse;
import com.inov8.hsm.util.FieldUtil;

public class VerifyAndGeneratePVVResponseParser implements Serializable {

	private static final long serialVersionUID = 5023680838994764882L;
	
	public static VerifyAndGeneratePVVResponse parse(String hexMessage) {
		StringBuilder hexBuilder = new StringBuilder(hexMessage);

		BaseHeader baseHeader = HeaderParser.parseHeader(hexBuilder);
		VerifyAndGeneratePVVResponse response = new VerifyAndGeneratePVVResponse(baseHeader);

		response.setResponseCode(FieldUtil.extractString(hexBuilder, 2));
		response.setPVV(FieldUtil.extractString(hexBuilder, 4));
		response.setRawPdu(hexMessage.getBytes());
		
		return response;
	}

}
