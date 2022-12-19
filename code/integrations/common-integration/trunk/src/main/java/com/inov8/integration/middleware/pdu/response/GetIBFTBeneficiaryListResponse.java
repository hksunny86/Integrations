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
		lastModified = "18-11-2014", 
		priority = Priority.HIGH,
		tags = { "Meezan","AMBIT", "Get IBFT Beneficiary List Response" }, 
		version = "1.0",
		releaseVersion = "2.3",
		patchVersion = "2.3.12",
		notes = "Get IBFT Beneficiary List Response for i8 - rdv communication.")
//@formatter: on
public class GetIBFTBeneficiaryListResponse extends AMBITPDU {

	private static final long serialVersionUID = -5427416467964963077L;
	private String responseCode;
	private List<IBFTBeneficiaryInfo> beneficiaries = null;
	
	public GetIBFTBeneficiaryListResponse(){
		if(super.getHeader() == null){
			super.setHeader(new AMBITHeader());
		}
	}
	
	public void build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(getHeader().build());
		pduString.append(trimToEmpty(responseCode)).append(AMBIT_DELIMITER.getValue());

		if(CollectionUtils.isNotEmpty(beneficiaries)){
			pduString.append(beneficiaries.size()).append(AMBIT_DELIMITER.getValue());
			for (IBFTBeneficiaryInfo beneficiaryInfo : beneficiaries) {
				pduString.append(beneficiaryInfo.build());
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

	public List<IBFTBeneficiaryInfo> getBeneficiaries() {
		return beneficiaries;
	}

	public void setBeneficiaries(List<IBFTBeneficiaryInfo> beneficiaries) {
		this.beneficiaries = beneficiaries;
	}

}
