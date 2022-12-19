package com.inov8.integration.middleware.pdu.response;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.NETWORK_MANAGEMENT_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.RESPONSE_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SYSTEMS_TRACE_NO;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_DATE;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.ISO8583Utils;

public class EchoResponse extends BasePDU {
	private static final long serialVersionUID = -1352911653475030358L;

	public EchoResponse() {
		if (getHeader() == null) {
			setHeader(new BaseHeader());
		}
	}

	public void build() {
		
		byte[] header = null;
		byte[] body = null;
		try {
			getHeader().setMessageType("0810");
			header = getHeader().build().getBytes();

			GenericPackager packager = ISO8583Utils.getISOPackager();
			ISOMsg msg = packager.createISOMsg();
			msg.setPackager(packager);
			msg.setMTI(getHeader().getMessageType());
			msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
			msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
			msg.set(NETWORK_MANAGEMENT_CODE.getValue(), trimToEmpty(getNetworkManagementCode()));
			msg.set(RESPONSE_CODE.getValue(), getResponseCode());

			body = msg.pack();
			logISOMessage(msg);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		super.setRawPdu(ArrayUtils.addAll(header, body));
	}

}
