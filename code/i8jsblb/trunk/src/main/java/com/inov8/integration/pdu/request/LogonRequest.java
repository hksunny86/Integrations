package com.inov8.integration.pdu.request;

import com.inov8.integration.enums.DataTypeEnum;
import com.inov8.integration.enums.PhoenixEnum;
import com.inov8.integration.pdu.BasePDU;
import com.inov8.integration.pdu.Field;

public class LogonRequest extends BasePDU {
	int pduLength = 239 + 16;
	private PhoenixRequestHeader header = new PhoenixRequestHeader();
	private Field channelPassword = new Field(PhoenixEnum.CHANNEL_PASSWORD.getValue(), 16, DataTypeEnum.AN);

	public LogonRequest() {
		this.fields.addAll(header.getFields());
		this.fields.add(channelPassword);

	}

	public PhoenixRequestHeader getHeader() {
		return header;
	}

	public void setHeader(PhoenixRequestHeader header) {
		this.header = header;
	}

	public Field getChannelPassword() {
		return channelPassword;
	}

	public void setChannelPassword(Field channelPassword) {
		this.channelPassword = channelPassword;
	}

}
