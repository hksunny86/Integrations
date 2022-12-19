package com.inov8.integration.middleware.allpay.pdu.request;

import java.io.Serializable;
import java.util.Date;

public class RequestHeader implements Serializable {

	private static final long serialVersionUID = 5171815964450234394L;

	private String username;
	private String password;
	private String channel;
	private String referenceNo;
	private Date requestTime;
    private String CNIC;
    private String MSISDN;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }
}
