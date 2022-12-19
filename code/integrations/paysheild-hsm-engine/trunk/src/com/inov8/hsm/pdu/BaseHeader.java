package com.inov8.hsm.pdu;

import java.io.Serializable;

public class BaseHeader implements Serializable{

	private static final long serialVersionUID = -3411516823128957185L;
	
	private String UPID;
	private String command;
	
	public String build(){
		StringBuilder packet = new StringBuilder();
		
		packet.append(UPID);
		packet.append(command);
		
		return packet.toString();
	}

	public String getUPID() {
		return UPID;
	}

	public void setUPID(String uPID) {
		UPID = uPID;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

}
