package com.inov8.jsblconsumer.net;

import org.apache.http.HttpStatus;

/**
 * @author soofia.faruq
 * 
 * To Handle 503 Service Unavailable
 */

public class InvalidResponseException extends Exception {
	private static final long serialVersionUID = 1L;
	public int responseCode = HttpStatus.SC_SERVICE_UNAVAILABLE;
    
	public InvalidResponseException(String msg) {
		super(msg);
	}

	public InvalidResponseException(String message, int responseCode) {
		super(message);
        this.responseCode = responseCode;
	}
}