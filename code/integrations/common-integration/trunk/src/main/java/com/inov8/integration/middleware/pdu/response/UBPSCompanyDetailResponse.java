package com.inov8.integration.middleware.pdu.response;

import com.inov8.integration.middleware.pdu.AMBITHeader;
import com.inov8.integration.middleware.pdu.AMBITPDU;
import com.inov8.integration.middleware.util.FieldUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inov8.integration.middleware.enums.MiddlewareEnum.AMBIT_DELIMITER;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

public class UBPSCompanyDetailResponse extends AMBITPDU {

	private static final long serialVersionUID = -5034224321064529047L;

	private String responseCode;
	
	private Map<Integer, UBPSCompany> companies = new HashMap<Integer, UBPSCompany>();
	private Map<Integer, List<Integer>> denominations = new HashMap<Integer, List<Integer>>();
	
	public UBPSCompanyDetailResponse(){
		if(super.getHeader() == null){
			super.setHeader(new AMBITHeader());
		}
	}

	public void build() {

		StringBuilder pduString = new StringBuilder();

		pduString.append(getHeader().build());
		pduString.append(trimToEmpty(responseCode)).append(AMBIT_DELIMITER.getValue());

		if(!companies.isEmpty()){
			pduString.append(companies.size()).append(AMBIT_DELIMITER.getValue());
			
			for (Integer companyCode : companies.keySet()) {
				UBPSCompany company = companies.get(companyCode);
				pduString.append(company.build());
			}
			
		}else{
			pduString.append(0).append(AMBIT_DELIMITER.getValue());
		}
		
		
		if(!denominations.isEmpty()){
			int denominationSize = 0;
			StringBuilder denominationString = new StringBuilder();
			
			for (Integer companyCode : denominations.keySet()) {
				List<Integer> amounts = denominations.get(companyCode);
				if(CollectionUtils.isNotEmpty(amounts)){
					denominationSize += amounts.size();
				}
				for (Integer amount : amounts) {
					denominationString.append(companyCode).append(AMBIT_DELIMITER.getValue());
					denominationString.append(amount).append(AMBIT_DELIMITER.getValue());
				}
			}
			
			pduString.append(denominationSize).append(AMBIT_DELIMITER.getValue());
			pduString.append(denominationString);
			
			
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

	public Map<Integer, UBPSCompany> getCompanies() {
		return companies;
	}

	public void setCompannies(Map<Integer, UBPSCompany> companies) {
		this.companies = companies;
	}

	public Map<Integer, List<Integer>> getDenominations() {
		return denominations;
	}

	public void setDenominations(Map<Integer, List<Integer>> denominations) {
		this.denominations = denominations;
	}
	
}
