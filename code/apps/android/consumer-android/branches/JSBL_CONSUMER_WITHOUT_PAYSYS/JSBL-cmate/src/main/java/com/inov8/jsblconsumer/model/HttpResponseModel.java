package com.inov8.jsblconsumer.model;

public class HttpResponseModel {
	private String xmlResponse;
	private String sessionId;

	public HttpResponseModel() {

	}

	public HttpResponseModel(String xmlResponse) {
		this.xmlResponse = xmlResponse;
	}

	public String getXmlResponse() {
		return xmlResponse;
	}

	public void setXmlResponse(String xmlResponse) {
		this.xmlResponse = xmlResponse;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
