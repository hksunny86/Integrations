package com.inov8.hsm.pdu.request;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.BasePDU;

public class EncryptClearPINRequest extends BasePDU {

	private static final long serialVersionUID = 3475174507784160119L;

	private String PIN;
	private String PAN;
	
	public EncryptClearPINRequest(BaseHeader baseHeader){
		super.setHeader(baseHeader);
	}
	
	public EncryptClearPINRequest(String UPID){
		BaseHeader baseHeader = new BaseHeader();
		baseHeader.setUPID(UPID);
		baseHeader.setCommand("BA");
		super.setHeader(baseHeader);
	}
	
	public void build(){
		StringBuilder packet = new StringBuilder();
		
		packet.append(getHeader().build());
		packet.append(PIN);
		packet.append(PAN);
		
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
