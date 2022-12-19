package com.inov8.hsm.pdu.request;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.BasePDU;

public class GenerateVisaPVVRequest extends BasePDU {

	private static final long serialVersionUID = 3475174507784160119L;

	private String PVK;
	private String PIN;
	private String PAN;
	private String PVKI;

	public GenerateVisaPVVRequest(BaseHeader baseHeader) {
		super.setHeader(baseHeader);
	}

	public GenerateVisaPVVRequest(String UPID) {
		BaseHeader baseHeader = new BaseHeader();
		baseHeader.setUPID(UPID);
		baseHeader.setCommand("DG");
		super.setHeader(baseHeader);
	}

	public void build() {
		StringBuilder packet = new StringBuilder();

		packet.append(getHeader().build());
		packet.append(PVK);
		packet.append(PIN);
		packet.append(PAN);
		packet.append(PVKI);

		setRawPdu(packet.toString().getBytes());
	}

	public String getPVK() {
		return PVK;
	}

	public void setPVK(String pVK) {
		PVK = pVK;
	}

	public String getPIN() {
		return PIN;
	}

	public void setPIN(String pIN) {
		PIN = pIN;
	}

	public String getPAN() {
		return PAN;
	}

	public void setPAN(String pAN) {
		PAN = pAN;
	}

	public String getPVKI() {
		return PVKI;
	}

	public void setPVKI(String pVKI) {
		PVKI = pVKI;
	}

}
