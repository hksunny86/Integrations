package com.inov8.hsm.persistance;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.inov8.hsm.persistance.model.TransactionLogModel;
import com.inov8.hsm.util.VersionInfo;
@VersionInfo(lastModified = "05/12/2014", releaseVersion = "1.0", version = "1.0", createdBy = "Zeeshan Ahmed, Faisal Basra",tags="")
public class TransactionRowMapper implements RowMapper<TransactionLogModel> {
	public TransactionLogModel mapRow(ResultSet resultSet, int arg1) throws SQLException {
		TransactionResultSetExtractor extractor = new TransactionResultSetExtractor();
		return extractor.extractData(resultSet);
	}
}