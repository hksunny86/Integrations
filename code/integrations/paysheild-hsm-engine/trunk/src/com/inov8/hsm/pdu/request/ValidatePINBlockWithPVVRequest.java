package com.inov8.hsm.pdu.request;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.BasePDU;

public class ValidatePINBlockWithPVVRequest extends BasePDU {

	private static final long serialVersionUID = 3475174507784160119L;

	private String ZPK;
	private String PVK;
	private String pinBlock;
	private String formatCode;
	private String PAN;
	private String PVKI;
	private String PVV;

	public ValidatePINBlockWithPVVRequest(BaseHeader baseHeader) {
		super.setHeader(baseHeader);
	}
	
	public ValidatePINBlockWithPVVRequest(String UPID){
		BaseHeader baseHeader = new BaseHeader();
		baseHeader.setUPID(UPID);
		baseHeader.setCommand("EC");
		super.setHeader(baseHeader);
	}
	
	public void build(){
		StringBuilder packet = new StringBuilder();
		
		packet.append(getHeader().build());
		packet.append(ZPK);
		packet.append(PVK);
		packet.append(pinBlock);
		packet.append(formatCode);
		packet.append(PAN);
		packet.append(PVKI);
		packet.append(PVV);
		
		setRawPdu(packet.toString().getBytes());
	}

	public String getZPK() {
		return ZPK;
	}

	public void setZPK(String zPK) {
		ZPK = zPK;
	}

	public String getPVK() {
		return PVK;
	}

	public void setPVK(String pVK) {
		PVK = pVK;
	}

	public String getPinBlock() {
		return pinBlock;
	}

	public void setPinBlock(String pinBlock) {
		this.pinBlock = pinBlock;
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

	public String getPVKI() {
		return PVKI;
	}

	public void setPVKI(String pVKI) {
		PVKI = pVKI;
	}

	public String getPVV() {
		return PVV;
	}

	public void setPVV(String pVV) {
		PVV = pVV;
	}

}
