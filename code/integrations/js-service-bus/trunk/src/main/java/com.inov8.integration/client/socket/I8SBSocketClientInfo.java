package com.inov8.integration.client.socket;

import org.apache.mina.core.session.IoSession;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

public class I8SBSocketClientInfo {
	private boolean connecting = false;
	private boolean authenticated = false;
	private boolean connected = false;
	private IoSession ioSession;

	public boolean isConnecting() {
		return connecting;
	}

	public synchronized void setConnecting(boolean connecting) {
		this.connecting = connecting;
	}

	public boolean isAuthenticated() {
		return true;
	}

	public synchronized void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public boolean isConnected() {
		return connected;
	}

	public synchronized void setConnected(boolean connected) {
		this.connected = connected;
	}

	public IoSession getIoSession() {
		return ioSession;
	}

	public void setIoSession(IoSession ioSession) {
		this.ioSession = ioSession;
	}

}
