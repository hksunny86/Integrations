package com.inov8.hsm.pdu.request;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.BasePDU;

public class DecryptEncryptedPINRequest extends BasePDU {

	private static final long serialVersionUID = 3475174507784160119L;

	private String PAN;
	private String PIN;
	
	public DecryptEncryptedPINRequest(BaseHeader baseHeader){
		super.setHeader(baseHeader);
	}
	
	public DecryptEncryptedPINRequest(String UPID){
		BaseHeader baseHeader = new BaseHeader();
		baseHeader.setUPID(UPID);
		baseHeader.setCommand("NG");
		super.setHeader(baseHeader);
	}
	
	public void build(){
		StringBuilder packet = new StringBuilder();
		
		packet.append(getHeader().build());
		packet.append(PAN);
		packet.append(PIN);
		
		setRawPdu(packet.toString().getBytes());
	}

	public String getPAN() {
		return PAN;
	}

	public void setPAN(String pAN) {
		PAN = pAN;
	}

	public String getPIN() {
		return PIN;
	}

	public void setPIN(String pIN) {
		PIN = pIN;
	}

}
