package com.inov8.integration.dao.i8sb.model;

import java.util.Date;

public class TransactionLog {

	private Long ID;
	private String gateway;
	private String clientID;
	private String terminalID;
	private String channelID;
	private String RRN;
	private String requestType;
	private String parentRequestRRN;
	private String i8sbRequest;
	private String i8sbResponse;
	private String channelRequest;
	private String channelResponse;
	private String responseCode;
	private String status;
	private String error;
	private Date requestDateTime;
	private Date responseDateTime;

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getTerminalID() {
		return terminalID;
	}

	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long ID) {
		this.ID = ID;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public String getRRN() {
		return RRN;
	}

	public void setRRN(String RRN) {
		this.RRN = RRN;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getParentRequestRRN() {
		return parentRequestRRN;
	}

	public void setParentRequestRRN(String parentRequestRRN) {
		this.parentRequestRRN = parentRequestRRN;
	}

	public String getI8sbRequest() {
		return i8sbRequest;
	}

	public void setI8sbRequest(String i8sbRequest) {
		this.i8sbRequest = i8sbRequest;
	}

	public String getI8sbResponse() {
		return i8sbResponse;
	}

	public void setI8sbResponse(String i8sbResponse) {
		this.i8sbResponse = i8sbResponse;
	}

	public String getChannelRequest() {
		return channelRequest;
	}

	public void setChannelRequest(String channelRequest) {
		this.channelRequest = channelRequest;
	}

	public String getChannelResponse() {
		return channelResponse;
	}

	public void setChannelResponse(String channelResponse) {
		this.channelResponse = channelResponse;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Date getRequestDateTime() {
		return requestDateTime;
	}

	public void setRequestDateTime(Date requestDateTime) {
		this.requestDateTime = requestDateTime;
	}

	public Date getResponseDateTime() {
		return responseDateTime;
	}

	public void setResponseDateTime(Date responseDateTime) {
		this.responseDateTime = responseDateTime;
	}
}
