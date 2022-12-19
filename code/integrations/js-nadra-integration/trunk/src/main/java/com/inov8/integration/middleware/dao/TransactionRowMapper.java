package com.inov8.integration.middleware.dao;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRowMapper implements RowMapper<TransactionLogModel> {
	public TransactionLogModel mapRow(ResultSet resultSet, int arg1) throws SQLException {
		TransactionResultSetExtractor extractor = new TransactionResultSetExtractor();
		return extractor.extractData(resultSet);
	}
}