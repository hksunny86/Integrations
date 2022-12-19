package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.AMBITHeader;
import com.inov8.integration.middleware.pdu.AMBITPDU;
import com.inov8.integration.middleware.util.FieldUtil;
import com.inov8.integration.middleware.util.VersionInfo;
import com.inov8.integration.middleware.util.VersionInfo.Priority;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

//@formatter:off
@VersionInfo(
		createdBy = "Zeeshan Ahmad", 
		lastModified = "18-11-2014", 
		priority = Priority.HIGH,
		tags = { "Meezan","AMBIT", "IBFT Response" }, 
		version = "1.0",
		releaseVersion = "2.3",
		patchVersion = "2.3.12",
		notes = "IBFT Response for i8 - rdv communication.")
//@formatter: on
public class IBFTResponse extends AMBITPDU {

	private static final long serialVersionUID = 5472140211032420997L;
	private String responseCode;
	
	public IBFTResponse(){
		if(super.getHeader() == null){
			super.setHeader(new AMBITHeader());
		}
	}

	public void build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(getHeader().build());
		pduString.append(trimToEmpty(responseCode)).append(AMBIT_DELIMITER.getValue());

		FieldUtil.appendMessageSizeByte(pduString);

		super.setRawPdu(pduString.toString());
	}
	
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

}
