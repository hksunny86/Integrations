package com.inov8.integration.middleware.pdu.request;

import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.NETWORK_MANAGEMENT_CODE;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.SYSTEMS_TRACE_NO;
import static com.inov8.integration.middleware.enums.ISO8583FieldEnum.TRANSACTION_DATE;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

import org.apache.commons.lang.ArrayUtils;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import com.inov8.integration.middleware.pdu.BaseHeader;
import com.inov8.integration.middleware.pdu.BasePDU;
import com.inov8.integration.middleware.util.ISO8583Utils;

public class EchoRequest extends BasePDU {

	private static final long serialVersionUID = -6176881409492538220L;

	public EchoRequest() {
		if (getHeader() == null) {
			setHeader(new BaseHeader());
		}
	}

	public void build() {

		byte[] header = null;
		byte[] body = null;
		try {
			getHeader().setMessageType("0800");
			header = getHeader().build().getBytes();

			GenericPackager packager = ISO8583Utils.getISOPackager();
			ISOMsg msg = packager.createISOMsg();
			msg.setPackager(packager);
			msg.setMTI(getHeader().getMessageType());
			msg.set(TRANSACTION_DATE.getValue(), trimToEmpty(getTransactionDate()));
			msg.set(SYSTEMS_TRACE_NO.getValue(), trimToEmpty(getStan()));
			msg.set(NETWORK_MANAGEMENT_CODE.getValue(), trimToEmpty(getNetworkManagementCode()));

			body = msg.pack();
			logISOMessage(msg);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		super.setRawPdu(ArrayUtils.addAll(header, body));
	}

}
