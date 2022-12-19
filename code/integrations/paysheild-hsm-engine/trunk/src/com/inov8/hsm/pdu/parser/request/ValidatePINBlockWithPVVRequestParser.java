package com.inov8.hsm.pdu.parser.request;

import java.io.Serializable;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.parser.HeaderParser;
import com.inov8.hsm.pdu.request.ValidatePINBlockWithPVVRequest;
import com.inov8.hsm.util.FieldUtil;

public class ValidatePINBlockWithPVVRequestParser implements Serializable {

	private static final long serialVersionUID = -436652215446600949L;

	public static ValidatePINBlockWithPVVRequest parse(String hexMessage) {
		StringBuilder hexBuilder = new StringBuilder(hexMessage);

		BaseHeader baseHeader = HeaderParser.parseHeader(hexBuilder);
		ValidatePINBlockWithPVVRequest request = new ValidatePINBlockWithPVVRequest(baseHeader);

		request.setZPK(FieldUtil.extractString(hexBuilder, 1));
		request.setPVK(FieldUtil.extractString(hexBuilder, 1));
		request.setPinBlock(FieldUtil.extractString(hexBuilder, 1));
		request.setFormatCode(FieldUtil.extractString(hexBuilder, 1));
		request.setPAN(FieldUtil.extractString(hexBuilder, 1));
		request.setPVKI(FieldUtil.extractString(hexBuilder, 1));
		request.setPVV(FieldUtil.extractString(hexBuilder, 1));

		return request;
	}

}
