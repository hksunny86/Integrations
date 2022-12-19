package com.inov8.hsm.pdu.parser.request;

import java.io.Serializable;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.parser.HeaderParser;
import com.inov8.hsm.pdu.request.GeneratePINBlockRequest;
import com.inov8.hsm.util.FieldUtil;

public class GeneratePINBlockRequestParser implements Serializable {

	private static final long serialVersionUID = 8458189387053806060L;

	public static GeneratePINBlockRequest parse(String hexMessage) {
		StringBuilder hexBuilder = new StringBuilder(hexMessage);
		
		BaseHeader baseHeader = HeaderParser.parseHeader(hexBuilder);
		GeneratePINBlockRequest request = new GeneratePINBlockRequest(baseHeader);
		
		request.setZPK(FieldUtil.extractString(hexBuilder, 1));
		request.setFormatCode(FieldUtil.extractString(hexBuilder, 1));
		request.setPAN(FieldUtil.extractString(hexBuilder, 1));
		request.setPIN(FieldUtil.extractString(hexBuilder, 1));
		
		return request;
	}


}
