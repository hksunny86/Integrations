package com.inov8.integration.enums;

/**
 * 
 * I8SBTransactionStatus enums, hold value for transaction status which are being stored into mongodb.
 *  <ul>
 *  <li>PROCESSING - initial status after sending</li>
 *	<li>RECEIVED - response is received from phoenix, but transaction still running.</li>
 *	<li>COMPLETED - transaction is completed.</li>
 *	<li>REJECTED - rejected at phoenix </li>
 *	<li>TIMEOUT - when it will timed out by phoenix</li>
 *	<li>REVERSAL_PROCESSING - when a transaction's reversal started</li>
 *	<li>REVERSAL_COMPLETED - if reversal is completed.</li>
 *	<li>REVERSING - when request sent to phoenix for reversal.</li>
 *	<li>REVERSED - when reversal is successful on external system</li>
 *  </ul>
 */
public enum I8SBTransactionStatus {
	PROCESSING("PROCESSING"),
	RECEIVED("RECEIVED"),
	COMPLETED("COMPLETED"),
	REJECTED("REJECTED"),
	TIMEOUT("TIMEOUT"),
	EXCEPTION("EXCEPTION"),
	REVERSAL_PROCESSING("REVERSAL_PROCESSING"),
	REVERSAL_UNDELIVERED("REVERSAL_UNDELIVERED"),
	REVERSED("REVERSED"),
	REVERSAL_FAILED("REVERSAL_FAILED");

	I8SBTransactionStatus(String value){
		this.value = value;
	}
	private String value;
	
	public String getValue() {
		return value;
	}
}
