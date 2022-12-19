package com.inov8.hsm.pdu.request;

import com.inov8.hsm.pdu.BaseHeader;
import com.inov8.hsm.pdu.BasePDU;

public class VerifyAndGeneratePVVRequest extends BasePDU {

	private static final long serialVersionUID = 3475174507784160119L;

	private String keyType;
	private String ZPK;
	private String PVK;
	private String currentPinBlock;
	private String currentFormatCode;
	private String PAN;
	private String PVKI;
	private String currentPVV;
	private String newPinBlock;

	public VerifyAndGeneratePVVRequest(BaseHeader baseHeader) {
		super.setHeader(baseHeader);
	}
	
	public VerifyAndGeneratePVVRequest(String UPID){
		BaseHeader baseHeader = new BaseHeader();
		baseHeader.setUPID(UPID);
		baseHeader.setCommand("CU");
		super.setHeader(baseHeader);
	}
	
	public void build(){
		StringBuilder packet = new StringBuilder();
		
		packet.append(getHeader().build());
		packet.append(keyType);
		packet.append(ZPK);
		packet.append(PVK);
		packet.append(currentPinBlock);
		packet.append(currentFormatCode);
		packet.append(PAN);
		packet.append(PVKI);
		packet.append(currentPVV);
		packet.append(newPinBlock);
		
		setRawPdu(packet.toString().getBytes());
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
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

	public String getCurrentPinBlock() {
		return currentPinBlock;
	}

	public void setCurrentPinBlock(String currentPinBlock) {
		this.currentPinBlock = currentPinBlock;
	}

	public String getCurrentFormatCode() {
		return currentFormatCode;
	}

	public void setCurrentFormatCode(String currentFormatCode) {
		this.currentFormatCode = currentFormatCode;
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

	public String getNewPinBlock() {
		return newPinBlock;
	}

	public void setNewPinBlock(String newPinBlock) {
		this.newPinBlock = newPinBlock;
	}

	public String getCurrentPVV() {
		return currentPVV;
	}

	public void setCurrentPVV(String currentPVV) {
		this.currentPVV = currentPVV;
	}

}
