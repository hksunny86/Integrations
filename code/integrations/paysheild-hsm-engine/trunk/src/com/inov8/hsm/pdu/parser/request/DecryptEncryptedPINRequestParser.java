package com.inov8.hsm.pdu.parser.request;

import java.io.Serializable;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.parser.HeaderParser;
import com.inov8.hsm.pdu.request.DecryptEncryptedPINRequest;
import com.inov8.hsm.util.FieldUtil;


public class DecryptEncryptedPINRequestParser implements Serializable {

	private static final long serialVersionUID = -279472585168812212L;
	
	public static DecryptEncryptedPINRequest parse(String hexMessage) {
		StringBuilder hexBuilder = new StringBuilder(hexMessage);
		
		BaseHeader baseHeader = HeaderParser.parseHeader(hexBuilder);
		DecryptEncryptedPINRequest request = new DecryptEncryptedPINRequest(baseHeader);
		
		request.setPAN(FieldUtil.extractString(hexBuilder, 16));
		request.setPIN(FieldUtil.extractString(hexBuilder, 2));
		
		return request;
	}
	
	
	

}
