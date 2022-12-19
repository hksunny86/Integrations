package com.inov8.agentmate.net;

/**
 * @author soofia.faruq
 * 
 * To Handle 503 Service Unavailable
 */

public class InvalidResponseException extends Exception {
	private static final long serialVersionUID = 1L;
    
	public InvalidResponseException(String msg) {
		super(msg);
	}

	public InvalidResponseException(String message, int responseCode) {
		super(message);
	}
}