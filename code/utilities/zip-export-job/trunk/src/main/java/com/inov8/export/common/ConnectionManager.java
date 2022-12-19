package com.inov8.export.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class ConnectionManager {
	
	static Logger log = Logger.getLogger(ConnectionManager.class.getName());
	
	private Connection connection;

	public ConnectionManager() throws Exception
	{
		log.info("***Establising DB Connection***");
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");			
			connection = DriverManager.getConnection(MessageSource.getPoperties("datasource.url"),
					MessageSource.getPoperties("datasource.username"),
					MessageSource.getPoperties("datasource.password"));

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			throw e;
		}
		
			
		log.info("***DB Connection Established***");
	}
	
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public void closeConnection(PreparedStatement ps, ResultSet rs) throws SQLException{
		log.info("*** Closing DB Connection ***");
		try 
		{
			ps.close();
		} catch (SQLException e) {
			log.info(e.getMessage());
		}
		
		try 
		{
			rs.close();
		} catch (SQLException e) {
			log.info(e.getMessage());
		}
		
		try {
			connection.close();
		} catch (SQLException e) {
			throw e;
		}
		
		log.info("*** DB Connection Closed ***");
		
	}
	
}
