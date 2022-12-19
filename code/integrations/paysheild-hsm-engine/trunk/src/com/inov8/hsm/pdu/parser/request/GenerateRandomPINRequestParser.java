package com.inov8.hsm.pdu.parser.request;

import java.io.Serializable;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.parser.HeaderParser;
import com.inov8.hsm.pdu.request.GenerateRandomPINRequest;
import com.inov8.hsm.util.FieldUtil;

public class GenerateRandomPINRequestParser implements Serializable {

	private static final long serialVersionUID = -877836283225176275L;

	public static GenerateRandomPINRequest parse(String hexMessage) {
		StringBuilder hexBuilder = new StringBuilder(hexMessage);
		
		BaseHeader baseHeader = HeaderParser.parseHeader(hexBuilder);
		GenerateRandomPINRequest request = new GenerateRandomPINRequest(baseHeader);
		
		request.setPAN(FieldUtil.extractString(hexBuilder, 1));
		request.setPINLength(FieldUtil.extractString(hexBuilder, 1));
		
		return request;
	}


}
