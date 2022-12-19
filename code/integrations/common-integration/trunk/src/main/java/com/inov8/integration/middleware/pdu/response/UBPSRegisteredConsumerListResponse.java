package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.AMBITHeader;
import com.inov8.integration.middleware.pdu.AMBITPDU;
import com.inov8.integration.middleware.util.FieldUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class UBPSRegisteredConsumerListResponse extends AMBITPDU {

	private static final long serialVersionUID = -528290324533114863L;
	private String responseCode;
	private String account;
	
	private List<ConsumerInfo> consumers = null;
	
	public UBPSRegisteredConsumerListResponse(){
		if(super.getHeader() == null){
			super.setHeader(new AMBITHeader());
		}
	}
	
	public void build(){
		StringBuilder pduString = new StringBuilder();

		pduString.append(getHeader().build());
		pduString.append(trimToEmpty(responseCode)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(account)).append(AMBIT_DELIMITER.getValue());
		
		if(CollectionUtils.isNotEmpty(consumers)){
			pduString.append(consumers.size()).append(AMBIT_DELIMITER.getValue());
			for (ConsumerInfo consumerInfo : consumers) {
				pduString.append(consumerInfo.build());
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public List<ConsumerInfo> getConsumers() {
		return consumers;
	}

	public void setConsumers(List<ConsumerInfo> consumers) {
		this.consumers = consumers;
	}

}
