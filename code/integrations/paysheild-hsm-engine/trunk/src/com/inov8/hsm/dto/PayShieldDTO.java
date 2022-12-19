package com.inov8.hsm.dto;

import java.io.Serializable;
import java.util.Date;

import com.inov8.hsm.util.VersionInfo;
@VersionInfo(lastModified = "05/12/2014", releaseVersion = "1.0", version = "1.0", createdBy = "Zeeshan Ahmed, Faisal Basra",tags="")
public class PayShieldDTO implements Serializable {

	private static final long serialVersionUID = 5824473488070381027L;

	private String UPID;
	private String microbankTransactionCode;
	private Date transmissionTime;
	private String responseCode;

	private String PAN;
	private String PIN;
	private String encryptedPIN;
	private String pinBlock;
	private String PVV;

	private String oldPIN;
	private String encryptedOldPIN;
	private String oldPinBlock;
	private String oldPVV;
	private boolean oldPINOperation;
	

	public String getMicrobankTransactionCode() {
		return microbankTransactionCode;
	}

	public void setMicrobankTransactionCode(String microbankTransactionCode) {
		this.microbankTransactionCode = microbankTransactionCode;
	}

	public Date getTransmissionTime() {
		return transmissionTime;
	}

	public void setTransmissionTime(Date transmissionTime) {
		this.transmissionTime = transmissionTime;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
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

	public String getPVV() {
		return PVV;
	}

	public void setPVV(String pVV) {
		PVV = pVV;
	}

	public String getOldPIN() {
		return oldPIN;
	}

	public void setOldPIN(String oldPIN) {
		this.oldPIN = oldPIN;
	}

	public String getOldPVV() {
		return oldPVV;
	}

	public void setOldPVV(String oldPVV) {
		this.oldPVV = oldPVV;
	}

	public String getUPID() {
		return UPID;
	}

	public void setUPID(String uPID) {
		UPID = uPID;
	}

	public String getEncryptedPIN() {
		return encryptedPIN;
	}

	public void setEncryptedPIN(String encryptedPIN) {
		this.encryptedPIN = encryptedPIN;
	}

	public String getEncryptedOldPIN() {
		return encryptedOldPIN;
	}

	public void setEncryptedOldPIN(String encryptedOldPIN) {
		this.encryptedOldPIN = encryptedOldPIN;
	}

	public String getPinBlock() {
		return pinBlock;
	}

	public void setPinBlock(String pinBlock) {
		this.pinBlock = pinBlock;
	}

	public String getOldPinBlock() {
		return oldPinBlock;
	}

	public void setOldPinBlock(String oldPinBlock) {
		this.oldPinBlock = oldPinBlock;
	}

	public boolean isOldPINOperation() {
		return oldPINOperation;
	}

	public void setOldPINOperation(boolean oldPINOperation) {
		this.oldPINOperation = oldPINOperation;
	}
	
	public String print(boolean debug) {

		if (debug) {
			return "PayShieldDTO [UPID=" + UPID + ", microbankTransactionCode=" + microbankTransactionCode + ", transmissionTime=" + transmissionTime
					+ ", responseCode=" + responseCode + ", PAN=" + PAN + ", PIN=" + PIN + ", encryptedPIN=" + encryptedPIN + ", pinBlock=" + pinBlock + ", PVV="
					+ PVV + ", oldPIN=" + oldPIN + ", encryptedOldPIN=" + encryptedOldPIN + ", oldPinBlock=" + oldPinBlock + ", oldPVV=" + oldPVV
					+ ", oldPINOperation=" + oldPINOperation + "]";
		} else {
			return "PayShieldDTO [UPID=" + UPID + ", microbankTransactionCode=" + microbankTransactionCode + ", transmissionTime=" + transmissionTime
					+ ", responseCode=" + responseCode + ", PAN=" + PAN + ", PIN=" + mask(PIN) + ", encryptedPIN=" + encryptedPIN + ", pinBlock=" + pinBlock + ", PVV="
					+ mask(PVV) + ", oldPIN=" + mask(oldPIN) + ", encryptedOldPIN=" + encryptedOldPIN + ", oldPinBlock=" + oldPinBlock + ", oldPVV=" + mask(oldPVV)
					+ ", oldPINOperation=" + oldPINOperation + "]";
		}

	}

	private String mask(String input) {
		String out = "";
		if (input != null && input.length() > 0) {
			while (out.length() < input.length()) {
				out = out + "*";
			}
		}
		return out;

	}

}
