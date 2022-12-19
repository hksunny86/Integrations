package com.inov8.hsm.pdu.parser.response;

import java.io.Serializable;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.parser.HeaderParser;
import com.inov8.hsm.pdu.response.DecryptEncryptedPINResponse;
import com.inov8.hsm.util.FieldUtil;

public class DecryptEncryptedPINResponseParser implements Serializable {

	private static final long serialVersionUID = 8164025512197686626L;

	public static DecryptEncryptedPINResponse parse(String hexMessage) {
		StringBuilder hexBuilder = new StringBuilder(hexMessage);

		BaseHeader baseHeader = HeaderParser.parseHeader(hexBuilder);
		DecryptEncryptedPINResponse response = new DecryptEncryptedPINResponse(baseHeader);

		response.setResponseCode(FieldUtil.extractString(hexBuilder, 2));
		response.setPIN(FieldUtil.extractString(hexBuilder, 5));
		response.setReferenceNo(FieldUtil.extractString(hexBuilder, 12));
		response.setRawPdu(hexMessage.getBytes());

		return response;
	}

}
