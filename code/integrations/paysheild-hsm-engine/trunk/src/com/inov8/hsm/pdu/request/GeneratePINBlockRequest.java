package com.inov8.hsm.pdu.request;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.BasePDU;

public class GeneratePINBlockRequest extends BasePDU {

	private static final long serialVersionUID = 3475174507784160119L;

	private String ZPK;
	private String formatCode;
	private String PAN;
	private String PIN;

	public GeneratePINBlockRequest(BaseHeader baseHeader) {
		super.setHeader(baseHeader);
	}
	
	public GeneratePINBlockRequest(String UPID){
		BaseHeader baseHeader = new BaseHeader();
		baseHeader.setUPID(UPID);
		baseHeader.setCommand("JG");
		super.setHeader(baseHeader);
	}
	
	public void build(){
		StringBuilder packet = new StringBuilder();
		
		packet.append(getHeader().build());
		packet.append(ZPK);
		packet.append(formatCode);
		packet.append(PAN);
		packet.append(PIN);
		
		setRawPdu(packet.toString().getBytes());
	}

	public String getZPK() {
		return ZPK;
	}

	public void setZPK(String zPK) {
		ZPK = zPK;
	}

	public String getFormatCode() {
		return formatCode;
	}

	public void setFormatCode(String formatCode) {
		this.formatCode = formatCode;
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
