package com.inov8.microbank.common.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.inov8.microbank.common.util.XMLConstants.*;


public class MiniXMLUtil
{
	public static final String CONSUMER_REF_NO_NODEREF = "//params/param[@name='CRNO']" ;
	public static final String BILL_DATE_NODEREF = "//params/param[@name='BDATE']";			
	public static final String PRODUCT_NAME_NODEREF = "//params/param[@name='PNAME']";	
	public static final String LATE_BILL_DATE_NODEREF = "//params/param[@name='LBDATEF']";			
	public static final String BILL_AMOUNT_NODEREF = "//params/param[@name='BAMTF']";			
	public static final String BILL_AMOUNT_UNFORMATED_NODEREF = "//params/param[@name='BAMT']";
	public static final String SERVICE_CHARGES_NODEREF = "//params/param[@name='TPAMF']";			
	public static final String LATE_BILL_AMOUNT_NODEREF = "//params/param[@name='LBAMTF']";			
	public static final String BILL_PAID_STATUS_NODEREF = "//params/param[@name='BPAID']";	
	public static final String CAMTF_NODEREF = "//params/param[@name='CAMTF']";
	public static final String CAMT_NODEREF = "//params/param[@name='CAMT']";
	public static final String TAMTF_NODEREF = "//params/param[@name='TAMTF']";
	public static final String TXAM_NODEREF = "//params/param[@name='TXAM']";
	public static final String TXAMF_NODEREF = "//params/param[@name='TXAMF']";
	public static final String TAMT_NODEREF = "//params/param[@name='TAMT']";
	public static final String FEE_NODEREF = "//params/param[@name='FEE']";
	public static final String DUEDATEF_NODEREF = "//params/param[@name='DUEDATE']";
	public static final String TRANSACTIONCODE_NODEREF = "//params/param[@name='TRXID']";

	public static final String LATE_BILL_AMT_NODEREF = "//params/param[@name='LBAMTF']";
	public static final String RECACCTITLE_NODEREF = "//params/param[@name='RECACCTITLE']";

	public static final String CDOB_NODEREF = "//params/param[@name='CDOB']";

	public static final String CNIC_EXP_NODEREF = "//params/param[@name='CNIC_EXP']";
    public static final String CNIC_NODEREF = "params/param[@name='CNIC']";
    public static final String CNAME_NODEREF = "//params/param[@name='CNAME']";
	public static final String NAME_NODEREF = "//params/param[@name='NAME']";
	public static final String TRX_ID_NODEREF = "//trans/trn/@ID";
    public static final String FNAME_NODEREF = "//params/param[@name='FATHER_NAME']";
    public static final String PERMANENT_ADDR_NODEREF = "//params/param[@name='PERMANENT_ADDR']";
    public static final String PRESENT_ADDR_NODEREF = "//params/param[@name='PRESENT_ADDR']";
    public static final String MOTHER_MAIDEN_NODEREF = "//params/param[@name='MOTHER_MAIDEN']";
	public static final String BIRTH_PLACE_NODEREF="//params/param[@name='BIRTH_PLACE']";

	public static final String CORE_ACT_TITLE_NODEREF="//params/param[@name='COREACTL']";
    public static final String CORE_ACCOUNT_TITLE_NODEREF="//params/param[@name='COREACTITLE']";
	public static final String BENE_BANK_NAME_NODEREF="//params/param[@name='BENE_BANK_NAME']";
	public static final String BENE_BRANCH_NAME_NODEREF="//params/param[@name='BENE_BRANCH_NAME']";
	public static final String BENE_IBAN_NODEREF="//params/param[@name='BENE_IBAN']";
	public static final String TRAN_PROCESS_AMN_NODEREF = "//params/param[@name='TPAM']";
	public static final String BRANCHLESS_BANK_ACT_NODEREF = "//params/param[@name='BBACID']";



	//******************************************************************************
	public static final String PRODUCT_ID_NODEREF = "//params/param[@name='PID']";
	public static final String SENDER_MOBILE_ATTREF = "//trans/trn/@SWMOB";
	//******************************************************************************

	public static final String BALANCE_AMOUNT_NODEREF = "//params/param[@name='BALF']";
	
	
	public static final String TRANS_CODE_NODEREF = "//trans/trn/@code";
	public static final String TRANS_ID_NODEREF = "//trans/trn/@TRXID";
	public static final String TRAN_BILL_AMOUNT_NODEREF = "//trans/trn/@BAMT";

	public static final String TRANS_DATE_NODEREF = "//trans/trn/@datef";
	public static final String TRANS_SERVICE_NODEREF = "//trans/trn/@prod";
	public static final String TRANS_SMACCOUNT_NODEREF = "//trans/trn/@pmode";
	public static final String TRANS_AUTH_CODE_NODEREF = "//trans/trn/@bcode";
	public static final String TRANS_BILL_AMT_NODEREF = "//trans/trn/@bAmtF";
	public static final String TRANS_SERVICE_CHARGES_NODEREF = "//trans/trn/@TPAMF";
	public static final String TRANS_COMMISSION_CHARGES_NODEREF = "//trans/trn/@CAMT";
	public static final String TRANS_AMOUNT_NODEREF = "//trans/trn/@TXAMF";
	public static final String TRANS_TOTAL_AMT_NODEREF = "//trans/trn/@amtf";
	public static final String TRAN_TOTAL_AMT_NODEREF = "//trans/trn/@TAMT";
	public static final String TRAN_BAL_NODEREF = "//trans/trn/@BALF";
	
	public static final String TRANS_AMT_NODEREF = "//trans/trn/@trnAmtF";

	public static final String BVS_ENABLE_NODEVAL = "//params/param[@name='BVSE']/text()";
	
//	public static Map<String, Map<String, String>> nadraMap = new HashMap<String, Map<String,String>>();
	
	
	public static String getTagTextValue( String xmlData, String locationPath ) throws XPathExpressionException
	{
		XPath xpath = XPathFactory.newInstance().newXPath();
		InputSource inputSource = new InputSource( new StringReader( xmlData )  );
	
		return ((String)xpath.evaluate(locationPath, inputSource, XPathConstants.STRING)).trim() ;
	}
	
	
	/**
	 * @param xmlData
	 * @return
	 */
	public Boolean isTllBalanceRequired(String xmlData) {
		
		Boolean isTllBalanceRequired = Boolean.FALSE;
		
		String tbrValue = null;
		
		try {
			tbrValue = MiniXMLUtil.getTagTextValue(xmlData, "/msg/params/param");
		} catch (XPathExpressionException e) {
			
		}
	
		if(tbrValue != null && tbrValue.equals("1")) {
			
			isTllBalanceRequired = Boolean.TRUE;
		}
		
		return isTllBalanceRequired;
	}
	
	
	/**
	 * @param name
	 * @param value
	 * @return
	 */
	public static String createXMLParameterTag(String name, String value) {
		
		StringBuilder strBuilder = new StringBuilder();
		strBuilder
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		
		.append(name)
		
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)		
		
		.append(value)
		
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE);		
		
		return strBuilder.toString();
	}	
	
	/** helper method to generate response XML by given parameters list
	 * 
	 * @param params
	 * @return xml String
	 */
	public static String createResponseXMLByParams(List<LabelValueBean> params){
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_TRANS).append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_TRN).append(TAG_SYMBOL_SPACE);
		for(LabelValueBean param: params){
			strBuilder.append(param.getLabel()).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE);
			strBuilder.append(param.getValue()).append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_SPACE);
		}
				
		strBuilder.append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_TRN).append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_TRANS).append(TAG_SYMBOL_CLOSE);
		
		return strBuilder.toString();
	}
	
	public static String createInfoResponseXMLByParams(List<LabelValueBean> params){
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append(TAG_SYMBOL_OPEN).append(XMLConstants.TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		
		for(LabelValueBean param: params) {
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM).append(TAG_SYMBOL_SPACE);
			strBuilder.append(ATTR_PARAM_NAME).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE).append(param.getLabel()).append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE);
			strBuilder.append(param.getValue());
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAM).append(TAG_SYMBOL_CLOSE);
		}
				
		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(XMLConstants.TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
		
		return strBuilder.toString();
	}

	/**
	 * @param paramsMap should be LinkedHashMap if order of params matters
	 * @return info response in xml format
	 */
	public static String createInfoResponseXMLByParams(Map<String,Object> paramsMap){
		StringBuilder strBuilder = new StringBuilder(1000);
		strBuilder.append(TAG_SYMBOL_OPEN).append(XMLConstants.TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

		for(Map.Entry<String, Object> param : paramsMap.entrySet()) {
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM).append(TAG_SYMBOL_SPACE);
			strBuilder.append(ATTR_PARAM_NAME).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE).append(param.getKey()).append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE);
			strBuilder.append(param.getValue());
			strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAM).append(TAG_SYMBOL_CLOSE);
		}

		strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(XMLConstants.TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

		return strBuilder.toString();
	}

	public static String createMessageXML(String message) {
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append(TAG_SYMBOL_OPEN)
		.append(XMLConstants.TAG_MESGS)
		.append(TAG_SYMBOL_CLOSE)
		.append(TAG_SYMBOL_OPEN)
		.append(XMLConstants.TAG_MESG)
		.append(TAG_SYMBOL_SPACE)
		.append(XMLConstants.ATTR_LEVEL)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(XMLConstants.ATTR_LEVEL_ONE)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE);
		
		strBuilder.append(message);
		
		strBuilder.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(XMLConstants.TAG_MESG)
		.append(TAG_SYMBOL_CLOSE)
		.append(TAG_SYMBOL_OPEN)
		.append(TAG_SYMBOL_SLASH)
		.append(XMLConstants.TAG_MESGS)
		.append(TAG_SYMBOL_CLOSE);

		return strBuilder.toString();
	}


	public static String createXMLTag(String tagName, Map<String, Object> attributes, boolean closingRequired) {

		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append(TAG_SYMBOL_OPEN)
				.append(tagName);

		for(Map.Entry<String, Object> attribute : attributes.entrySet()) {
			strBuilder.append(TAG_SYMBOL_SPACE)
					.append(attribute.getKey())
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE);

			Object value = attribute.getValue();
			if(value == null)
				value = "";

			strBuilder.append(StringEscapeUtils.escapeXml(value.toString()))
					.append(TAG_SYMBOL_QUOTE);
		}

		strBuilder.append(TAG_SYMBOL_CLOSE);

		if(closingRequired) {
			strBuilder.append(createXMLTag(tagName, false));
		}

		return strBuilder.toString();
	}


	public static String createXMLTag(String tagName, boolean openingTag) {
		return createXMLTag(tagName, openingTag, "");
	}
	public static String createXMLTag(String tagName, boolean openingTag, String attrs) {
		StringBuilder strBuilder = new StringBuilder();

		strBuilder.append(TAG_SYMBOL_OPEN);

		if(!openingTag) {
			strBuilder.append(TAG_SYMBOL_SLASH);
		}

		strBuilder.append(tagName);
		if(openingTag && !StringUtils.isBlank(attrs)) {
			strBuilder.append(attrs);
		}
		strBuilder.append(TAG_SYMBOL_CLOSE);

		return strBuilder.toString();
	}
	
	public static String createErrorXml(Long errorCode, int level, String message) {
		StringBuilder xml = new StringBuilder();
		xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_ERRORS, true));
		
		Map<String, Object> attributes = new LinkedHashMap<>();
		
		attributes.put(XMLConstants.ATTR_CODE, CommonUtils.getDefaultIfNull(errorCode, 0).toString());
		attributes.put(XMLConstants.ATTR_LEVEL, String.valueOf(level));
		
		xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_ERROR, attributes, false));
		
		if(!StringUtil.isNullOrEmpty(message))
			xml.append(message);
		
		xml.append(createXMLTag(XMLConstants.TAG_ERROR, false));
		
		xml.append(MiniXMLUtil.createXMLTag(XMLConstants.TAG_ERRORS, false));
		
		return xml.toString();
	}
}
