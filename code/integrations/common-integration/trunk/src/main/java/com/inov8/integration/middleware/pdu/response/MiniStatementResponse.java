package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.AMBITHeader;
import com.inov8.integration.middleware.pdu.AMBITPDU;
import com.inov8.integration.middleware.util.FieldUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class MiniStatementResponse extends AMBITPDU {

	private static final long serialVersionUID = 4707915391087557524L;
	private String responseCode;
	private List<StatementRow> transactions;
	
	public MiniStatementResponse(){
		if(super.getHeader() == null){
			super.setHeader(new AMBITHeader());
		}
	}
	
	public void build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(getHeader().build());
		pduString.append(trimToEmpty(responseCode)).append(AMBIT_DELIMITER.getValue());
		
		if(CollectionUtils.isNotEmpty(transactions)){
			pduString.append(transactions.size() ).append(AMBIT_DELIMITER.getValue());
			
			for (StatementRow statementRow : transactions) {
				pduString.append(statementRow.build()).append(AMBIT_DELIMITER.getValue());
			}
		}else{
			pduString.append(0).append(AMBIT_DELIMITER.getValue());
		}
		
		FieldUtil.appendMessageSizeByte(pduString);

		super.setRawPdu(pduString.toString());
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public List<StatementRow> getTransactions() {
		if (transactions == null) {
			transactions = new ArrayList<StatementRow>();
		}
		return transactions;
	}

	public void setTransactions(List<StatementRow> transactions) {
		this.transactions = transactions;
	}


}
