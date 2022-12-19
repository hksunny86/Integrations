package com.inov8.hsm.controller;

import java.sql.SQLException;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.inov8.hsm.util.VersionInfo;

@Component
@VersionInfo(lastModified = "05/12/2014", releaseVersion = "1.0", version = "1.0", createdBy = "Zeeshan Ahmed, Faisal Basra",tags="HSM Controller")
public class NetworkInfoBean {
	private static Logger logger = LoggerFactory.getLogger(NetworkInfoBean.class.getSimpleName());
	
	private boolean connecting = false;
	private boolean connected = false;
	private IoSession ioSession;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public NetworkInfoBean(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String isDatabase() {
		String res = "db: ";
		try {
			res = res + jdbcTemplate.getDataSource().getConnection().isClosed();
		} catch (SQLException e) {
			logger.error("Exception",e);
		}
		return res;
	}
	
	public NetworkInfoBean(){
		System.out.println("Booting NIB...........");
	}

	public boolean isConnecting() {
		return connecting;
	}

	public synchronized void setConnecting(boolean connecting) {
		this.connecting = connecting;
	}

	public boolean isAuthenticated() {
		return true;
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
