package com.inov8.integration.dao.i8sb;


import com.inov8.integration.dao.i8sb.model.TransactionLog;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRowMapper implements RowMapper<TransactionLog> {
	public TransactionLog mapRow(ResultSet resultSet, int arg1) throws SQLException {
		TransactionResultSetExtractor extractor = new TransactionResultSetExtractor();
		return extractor.extractData(resultSet);
	}
}