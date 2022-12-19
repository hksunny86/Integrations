package com.inov8.integration.dao.i8sb;

import com.inov8.integration.dao.i8sb.model.TransactionLog;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionResultSetExtractor implements ResultSetExtractor<Object> {

	@Override
	public TransactionLog extractData(ResultSet resultSet) throws SQLException {
		TransactionLog transactionLog = new TransactionLog();
		transactionLog.setID(resultSet.getLong("ID"));
		transactionLog.setClientID(resultSet.getString("Client_ID"));
		transactionLog.setChannelID(resultSet.getString("Channel_ID"));
		transactionLog.setRRN(resultSet.getString("RRN"));
		transactionLog.setRequestType(resultSet.getString("Request_Type"));
		transactionLog.setParentRequestRRN(resultSet.getString("Parent_Request_RRN"));
		transactionLog.setI8sbRequest(resultSet.getString("I8SB_Request"));
		transactionLog.setI8sbResponse(resultSet.getString("I8SB_Response"));
		transactionLog.setChannelRequest(resultSet.getString("CHANNEL_Request"));
		transactionLog.setChannelResponse(resultSet.getString("CHANNEL_Response"));
		transactionLog.setResponseCode(resultSet.getString("Response_Code"));
		transactionLog.setStatus(resultSet.getString("Status"));
		transactionLog.setError(resultSet.getString("Error"));
		transactionLog.setRequestDateTime(resultSet.getTimestamp("Request_Date_Time"));
		transactionLog.setResponseDateTime(resultSet.getTimestamp("Response_Date_Time"));
		return transactionLog;
	}
}