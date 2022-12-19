package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.util.VersionInfo;
import com.inov8.integration.middleware.util.VersionInfo.Priority;

import java.io.Serializable;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

//@formatter:off
@VersionInfo(
		createdBy = "Zeeshan Ahmad", 
		lastModified = "23-02-2015", 
		priority = Priority.HIGH,
		tags = { "Meezan","AMBIT", "Debit Card Info" }, 
		version = "1.0",
		releaseVersion = "2.4",
		patchVersion = "2.4.8",
		notes = "Debit Card Info for i8 - rdv communication.")
//@formatter: on
public class DebitCardInfo implements Serializable {

	private static final long serialVersionUID = 5610464354331382130L;
	
	private String pan;
	private String cardStatus;
	private String cardTypeName;
	
	
	public String build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(trimToEmpty(pan)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(cardStatus)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(cardTypeName)).append(AMBIT_DELIMITER.getValue());

		return pduString.toString();
	}
	
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getCardTypeName() {
		return cardTypeName;
	}

	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}
	

}
