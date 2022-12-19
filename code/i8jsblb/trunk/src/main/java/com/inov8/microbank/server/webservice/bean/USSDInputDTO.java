/**
 * 
 */
package com.inov8.microbank.server.webservice.bean;

/**
 * @author Wateen
 *
 */
public class USSDInputDTO {
	private String msisdn;
	private String senderId;
	private String messege;
	
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getMessege() {
		return messege;
	}
	public void setMessege(String messege) {
		this.messege = messege;
	}
}
