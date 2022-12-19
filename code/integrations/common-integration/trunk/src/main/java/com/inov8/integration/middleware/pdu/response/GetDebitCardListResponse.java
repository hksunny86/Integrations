package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.AMBITHeader;
import com.inov8.integration.middleware.pdu.AMBITPDU;
import com.inov8.integration.middleware.util.FieldUtil;
import com.inov8.integration.middleware.util.VersionInfo;
import com.inov8.integration.middleware.util.VersionInfo.Priority;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

//@formatter:off
@VersionInfo(
		createdBy = "Zeeshan Ahmad", 
		lastModified = "23-02-2015", 
		priority = Priority.HIGH,
		tags = { "Meezan","AMBIT", "Get Debit Card List Response" }, 
		version = "1.0",
		releaseVersion = "2.4",
		patchVersion = "2.4.8",
		notes = "Get Debit Card List Response for i8 - rdv communication.")
//@formatter: on
public class GetDebitCardListResponse extends AMBITPDU {

	private static final long serialVersionUID = -5427416467964963077L;
	private String responseCode;
	private String cnic;
	private List<DebitCardInfo> debitCardInfos = null;
	
	public GetDebitCardListResponse(){
		if(super.getHeader() == null){
			super.setHeader(new AMBITHeader());
		}
	}
	
	public void build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(getHeader().build());
		pduString.append(trimToEmpty(responseCode)).append(AMBIT_DELIMITER.getValue());
		pduString.append(trimToEmpty(cnic)).append(AMBIT_DELIMITER.getValue());

		if(CollectionUtils.isNotEmpty(debitCardInfos)){
			pduString.append(debitCardInfos.size()).append(AMBIT_DELIMITER.getValue());
			for (DebitCardInfo debitCardInfo : debitCardInfos) {
				pduString.append(debitCardInfo.build());
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

	public List<DebitCardInfo> getDebitCardInfos() {
		return debitCardInfos;
	}

	public void setDebitCardInfos(List<DebitCardInfo> debitCardInfos) {
		this.debitCardInfos = debitCardInfos;
	}

	public String getCnic() {
		return cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}



}
