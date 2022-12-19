package com.inov8.integration.middleware.dao;

import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionResultSetExtractor implements ResultSetExtractor<Object> {

	@Override
	public TransactionLogModel extractData(ResultSet resultSet) throws SQLException {
		TransactionLogModel transaction = new TransactionLogModel();
		transaction.setTransactionLogId(resultSet.getLong("transaction_log_id"));
		transaction.setRetrievalRefNo(resultSet.getString("rrn"));
		transaction.setMessageType(resultSet.getString("message_code"));
		transaction.setTransactionCode(resultSet.getString("TRANSACTION_TYPE"));
		transaction.setTransactionDateTime(resultSet.getTimestamp("transaction_date"));
		transaction.setStatus(resultSet.getLong("status_id"));
		transaction.setResponseCode(resultSet.getString("response_code"));

		transaction.setPduRequestString(resultSet.getString("pdu_request_string"));
		transaction.setPduResponseString(resultSet.getString("pdu_response_string"));
		
//		Blob pduRequest = resultSet.getBlob("pdu_request_string");
//		byte[] reqdata = pduRequest.getBytes(1, (int) pduRequest.length());
//		transaction.setPduRequestString(new String(reqdata));

//		Blob pduResponse = resultSet.getBlob("pdu_response_string");
//		byte[] resdata = pduResponse.getBytes(1, (int) pduRequest.length());
//		transaction.setPduResponseString(new String(resdata));

		transaction.setParentTransactionLogId(resultSet.getLong("parent_transaction_id"));
		transaction.setI8TransactionCode(resultSet.getString("i8_transaction_code"));
		transaction.setProcessedTime(resultSet.getLong("processed_time"));
		transaction.setRetryCount(resultSet.getInt("retry_count"));
		return transaction;
	}
}