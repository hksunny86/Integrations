package com.inov8.hsm.persistance;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.ResultSetExtractor;

import com.inov8.hsm.persistance.model.TransactionLogModel;
import com.inov8.hsm.util.VersionInfo;
@VersionInfo(lastModified = "05/12/2014", releaseVersion = "1.0", version = "1.0", createdBy = "Zeeshan Ahmed, Faisal Basra",tags="")
public class TransactionResultSetExtractor implements ResultSetExtractor<Object> {

	@Override
	public TransactionLogModel extractData(ResultSet rs) throws SQLException {
		
		TransactionLogModel tx = new TransactionLogModel();
		tx.setTransactionLogId(rs.getLong("TRANSACTION_LOG_ID"));
		tx.setCommand(rs.getString("COMMAND"));
		tx.setRequestPacket(rs.getString("REQUEST"));
		tx.setResponsePacket(rs.getString("RESPONSE"));
		tx.setRequestTime(rs.getTimestamp("REQUEST_TIME")); 
		tx.setResponseTime(rs.getTimestamp("RESPONSE_TIME"));
		tx.setResponseCode(rs.getString("RESPONSE_CODE"));
		tx.setMicrobankTrxId(rs.getString("MICROBANK_TXID"));
		tx.setUniquePacketIdentifier(rs.getString("UPID"));
		return tx;
	}
}