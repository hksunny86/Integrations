package com.inov8.hsm.persistance.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.inov8.hsm.util.VersionInfo;
@VersionInfo(lastModified = "05/12/2014", releaseVersion = "1.0", version = "1.0", createdBy = "Zeeshan Ahmed, Faisal Basra",tags="")
public class TransactionLogModel implements Serializable {

	private static final long serialVersionUID = -6490814372861064537L;

	private Long transactionLogId;
	private String command;
	private String requestPacket;
	private String responsePacket;
	private Timestamp requestTime;
	private Timestamp responseTime;
	private String responseCode;
	private String microbankTrxId;
	private String uniquePacketIdentifier;

	public Long getTransactionLogId() {
		return transactionLogId;
	}

	public void setTransactionLogId(Long transactionLogId) {
		this.transactionLogId = transactionLogId;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getRequestPacket() {
		return requestPacket;
	}

	public void setRequestPacket(String requestPacket) {
		this.requestPacket = requestPacket;
	}

	public String getResponsePacket() {
		return responsePacket;
	}

	public void setResponsePacket(String responsePacket) {
		this.responsePacket = responsePacket;
	}

	public Timestamp getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}

	public Timestamp getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Timestamp responseTime) {
		this.responseTime = responseTime;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getMicrobankTrxId() {
		return microbankTrxId;
	}

	public void setMicrobankTrxId(String microbankTrxId) {
		this.microbankTrxId = microbankTrxId;
	}

	public String getUniquePacketIdentifier() {
		return uniquePacketIdentifier;
	}

	public void setUniquePacketIdentifier(String uniquePacketIdentifier) {
		this.uniquePacketIdentifier = uniquePacketIdentifier;
	}

}
