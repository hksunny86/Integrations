package com.inov8.hsm.pdu.parser.response;

import java.io.Serializable;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.parser.HeaderParser;
import com.inov8.hsm.pdu.response.GenerateVisaPVVResponse;
import com.inov8.hsm.util.FieldUtil;

public class GenerateVisaPVVResponseParser implements Serializable {

	private static final long serialVersionUID = -877025976274623614L;

	public static GenerateVisaPVVResponse parse(String hexMessage) {
		StringBuilder hexBuilder = new StringBuilder(hexMessage);

		BaseHeader baseHeader = HeaderParser.parseHeader(hexBuilder);
		GenerateVisaPVVResponse response = new GenerateVisaPVVResponse(baseHeader);

		response.setResponseCode(FieldUtil.extractString(hexBuilder, 2));
		response.setPVV(FieldUtil.extractString(hexBuilder, 4));
		response.setRawPdu(hexMessage.getBytes());

		return response;
	}

}
