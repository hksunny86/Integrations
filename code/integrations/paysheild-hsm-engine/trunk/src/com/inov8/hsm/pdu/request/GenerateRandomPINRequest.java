package com.inov8.hsm.pdu.request;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.BasePDU;

public class GenerateRandomPINRequest extends BasePDU {

	private static final long serialVersionUID = 3475174507784160119L;

	private String PAN;
	private String PINLength;

	public GenerateRandomPINRequest(BaseHeader baseHeader) {
		super.setHeader(baseHeader);
	}

	public GenerateRandomPINRequest(String UPID) {
		BaseHeader baseHeader = new BaseHeader();
		baseHeader.setUPID(UPID);
		baseHeader.setCommand("JA");
		super.setHeader(baseHeader);
	}

	public void build() {

		StringBuilder packet = new StringBuilder();

		packet.append(getHeader().build());
		packet.append(PAN);
		packet.append(PINLength);

		setRawPdu(packet.toString().getBytes());

	}

	public String getPAN() {
		return PAN;
	}

	public void setPAN(String pAN) {
		PAN = pAN;
	}

	public String getPINLength() {
		return PINLength;
	}

	public void setPINLength(String pINLength) {
		PINLength = pINLength;
	}

}
