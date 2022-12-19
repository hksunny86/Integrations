package com.inov8.hsm.pdu.parser.request;

import java.io.Serializable;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.parser.HeaderParser;
import com.inov8.hsm.pdu.request.VerifyAndGeneratePVVRequest;
import com.inov8.hsm.util.FieldUtil;

public class VerifyAndGeneratePVVRequestParser implements Serializable {

	private static final long serialVersionUID = 8822269108985674237L;

	public static VerifyAndGeneratePVVRequest parse(String hexMessage) {
		StringBuilder hexBuilder = new StringBuilder(hexMessage);

		BaseHeader baseHeader = HeaderParser.parseHeader(hexBuilder);
		VerifyAndGeneratePVVRequest request = new VerifyAndGeneratePVVRequest(baseHeader);

		request.setKeyType(FieldUtil.extractString(hexBuilder, 1));
		request.setZPK(FieldUtil.extractString(hexBuilder, 1));
		request.setPVK(FieldUtil.extractString(hexBuilder, 1));
		request.setCurrentPinBlock(FieldUtil.extractString(hexBuilder, 1));
		request.setCurrentFormatCode(FieldUtil.extractString(hexBuilder, 1));
		request.setPAN(FieldUtil.extractString(hexBuilder, 1));
		request.setPVKI(FieldUtil.extractString(hexBuilder, 1));
		request.setNewPinBlock(FieldUtil.extractString(hexBuilder, 1));

		return request;
	}

}
