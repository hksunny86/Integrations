package com.inov8.hsm.pdu.parser.request;

import java.io.Serializable;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.parser.HeaderParser;
import com.inov8.hsm.pdu.request.GenerateVisaPVVRequest;
import com.inov8.hsm.util.FieldUtil;

public class GenerateVisaPVVRequestParser implements Serializable {

	private static final long serialVersionUID = -8023779636879609675L;

	public static GenerateVisaPVVRequest parse(String hexMessage) {
		StringBuilder hexBuilder = new StringBuilder(hexMessage);
		
		BaseHeader baseHeader = HeaderParser.parseHeader(hexBuilder);
		GenerateVisaPVVRequest request = new GenerateVisaPVVRequest(baseHeader);
		
		request.setPVK(FieldUtil.extractString(hexBuilder, 1));
		request.setPIN(FieldUtil.extractString(hexBuilder, 1));
		request.setPAN(FieldUtil.extractString(hexBuilder, 1));
		request.setPVKI(FieldUtil.extractString(hexBuilder, 1));
		
		return request;
	}
	

}
