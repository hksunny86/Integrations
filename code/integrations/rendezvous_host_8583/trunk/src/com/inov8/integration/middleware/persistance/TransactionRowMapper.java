package com.inov8.integration.middleware.persistance;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.inov8.integration.middleware.persistance.model.TransactionLogModel;

public class TransactionRowMapper implements RowMapper<TransactionLogModel> {
	public TransactionLogModel mapRow(ResultSet resultSet, int arg1) throws SQLException {
		TransactionResultSetExtractor extractor = new TransactionResultSetExtractor();
		return extractor.extractData(resultSet);
	}
}