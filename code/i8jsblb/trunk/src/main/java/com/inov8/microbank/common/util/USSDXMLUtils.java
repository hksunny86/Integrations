package com.inov8.microbank.common.util;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_MSG_ID;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MSG;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_EQUAL;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_OPEN_SLASH;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_QUOTE;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_SPACE;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.inov8.microbank.common.vo.ussd.UserState;
import com.inov8.microbank.server.service.integration.vo.UssdUserStateVO;

public class USSDXMLUtils {
	
	DocumentBuilderFactory domFactory = null ;	
	
	public USSDXMLUtils()
	{
		domFactory = DocumentBuilderFactory.newInstance();		
	}
	public String prepareLoginXmlMessage(String msgId, String mobn, String dtid)
	{
		StringBuilder msg = new StringBuilder();
		msg
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_MSG)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_MSG_ID)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(StringUtil.trimToEmpty(msgId))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE)
			
			
			
			//DTID
			.append(addNode("DTID", dtid))
			
			// MOBN
			.append(addNode("MOBN", mobn))
			
			// Closing Params TAG
			.append(TAG_SYMBOL_OPEN_SLASH)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE)
			
			// Closing MSG TAG
			.append(TAG_SYMBOL_OPEN_SLASH)
			.append(TAG_MSG)
			.append(TAG_SYMBOL_CLOSE);
			return msg.toString();
	}
	
	public String prepareCashInXmlMessage(String msgId, String uid,String pin,String dtid,String mobn, String sms, String pId, String transactionAmount)
	{
		StringBuilder msg = new StringBuilder();
		msg
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_MSG)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_MSG_ID)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(StringUtil.trimToEmpty(msgId))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE)
			
			
			.append(addNode("PID", pId))
			.append(addNode("DTID", dtid))
			.append(addNode("TXAM", transactionAmount))
			.append(addNode("CSCD", mobn))
			.append(addNode("MOBN", mobn))
			
			
			
			// Closing Params TAG
			.append(TAG_SYMBOL_OPEN_SLASH)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE)
			
			// Closing MSG TAG
			.append(TAG_SYMBOL_OPEN_SLASH)
			.append(TAG_MSG)
			.append(TAG_SYMBOL_CLOSE);
			return msg.toString();
	}
	
	
	
	public static String prepareXmlMessage(String msgId, String uid,String pin,String dtid,String mobn, String sms, String mnoId)
	{
		StringBuilder msg = new StringBuilder();
		msg
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_MSG)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_MSG_ID)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(StringUtil.trimToEmpty(msgId))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE)
			
			
			//UID
			.append(addNode("UID", uid))
			
			//PIN
			.append(addNode("PIN", pin))
			
			//DTID
			.append(addNode("DTID", dtid))
			
			// MOBN
			.append(addNode("MOBN", mobn))
			
			// SMS
			.append(addNode("SMS", sms))
			
			//MNOID
			.append(addNode("MNOID", mnoId))
			
			// Closing Params TAG
			.append(TAG_SYMBOL_OPEN_SLASH)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE)
			
			// Closing MSG TAG
			.append(TAG_SYMBOL_OPEN_SLASH)
			.append(TAG_MSG)
			.append(TAG_SYMBOL_CLOSE);
			return msg.toString();
	}
	
	public static String preparePayCashXmlMessage(String msgId, String uid,String pin,String dtid,String mobn, String sms, String mnoId)
	{
		StringBuilder msg = new StringBuilder();
		msg
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_MSG)
			.append(TAG_SYMBOL_SPACE)
			.append(ATTR_MSG_ID)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(StringUtil.trimToEmpty(msgId))
			.append(TAG_SYMBOL_QUOTE)
			.append(TAG_SYMBOL_CLOSE)
			.append(TAG_SYMBOL_OPEN)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE)
			
			
			//UID
			.append(addNode("UID", uid))
			
			//PIN
			.append(addNode("PIN", pin))
			
			//DTID
			.append(addNode("DTID", dtid))
			
			// MOBN
			.append(addNode("MOBN", mobn))
			
			// SMS
			.append(addNode("SMS", sms))
			
			//MNOID
			.append(addNode("MNOID", mnoId))
			
			// Closing Params TAG
			.append(TAG_SYMBOL_OPEN_SLASH)
			.append(TAG_PARAMS)
			.append(TAG_SYMBOL_CLOSE)
			
			// Closing MSG TAG
			.append(TAG_SYMBOL_OPEN_SLASH)
			.append(TAG_MSG)
			.append(TAG_SYMBOL_CLOSE);
			return msg.toString();
	}
	
	private static String addNode(String key,String value)
	{
		StringBuilder msg = new StringBuilder();
		 msg.append(TAG_SYMBOL_OPEN)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_SPACE)
		.append(ATTR_PARAM_NAME)
		.append(TAG_SYMBOL_EQUAL)
		.append(TAG_SYMBOL_QUOTE)
		.append(key)
		.append(TAG_SYMBOL_QUOTE)
		.append(TAG_SYMBOL_CLOSE)
		.append(value)
		.append(TAG_SYMBOL_OPEN_SLASH)
		.append(TAG_PARAM)
		.append(TAG_SYMBOL_CLOSE);
		 return msg.toString();
	}
	
	public static boolean isErrorXML( String xml )
    {
    	if( xml.indexOf("<msg id=\"-1\">") == -1 )
    		return false;
    	else
    		return true;
    }
	public void populateCDCustomerDetail(String xml, UserState userStateVO){
		if(xml!=null){
			NodeList nodeList = executeXPathQuery(xml, "/params/*") ;
			String value,nodeName;
			for (int i = 0; i < nodeList.getLength(); i++) 
			{
				nodeName=nodeList.item(i).getAttributes().item(0).getNodeValue();
				if(nodeName!=null && !"".equals(nodeName) && !"ACID".equals(nodeName)){
					value = nodeList.item(i).getFirstChild().getNodeValue() + " " ;
					if(CommandFieldConstants.KEY_COMM_AMOUNT.equals(nodeName)){
						userStateVO.setAmount(Double.parseDouble(value));
						userStateVO.setCommissionAmount(value);
						userStateVO.setCAMT(value);
					}else if(CommandFieldConstants.KEY_TX_AMOUNT.equals(nodeName)){
						userStateVO.setTransactionAmount(value);
						userStateVO.setTXAM(value);
					}
					
					else if(CommandFieldConstants.KEY_BILL_AMOUNT.equals(nodeName)){
						userStateVO.setBillAmount(value);
						userStateVO.setBAMT(value);
					}
					else if(CommandFieldConstants.KEY_TX_PROCESS_AMNT.equals(nodeName)){
						userStateVO.setTransactionProcessingAmount(value);
						userStateVO.setTPAM(value);
					}
					else if(CommandFieldConstants.KEY_RP_NAME .equals(nodeName)){
						userStateVO.setCustomerName(value);
					}
					else if(CommandFieldConstants.KEY_TOTAL_AMOUNT.equals(nodeName)){
						userStateVO.setTotalAmount(value);
						userStateVO.setTAMT(value);
//						userStateVO.setBillAmount(value);
//						userStateVO.setTransactionProcessingAmount(value);
//						userStateVO.setTransactionAmount(value);
					}
					// C2C Start
//					else if(CommandFieldConstants.KEY_CNIC.equals(nodeName)){
//						userStateVO.setCustomerCNIC(value);
//						userStateVO.setWalkinSenderCNIC(value);
//					}
//					else if(CommandFieldConstants.KEY_CUST_CODE.equals(nodeName)){
//						userStateVO.setWalkinReceiverCNIC(value);
//					}
//					else if(CommandFieldConstants.KEY_SENDER_MOBILE.equals(nodeName)){
//						userStateVO.setWalkinSenderMSISDN(value);
//					}
//					else if(CommandFieldConstants.KEY_CD_CUSTOMER_MOBILE.equals(nodeName)){
//						userStateVO.setWalkinReceiverMSISDN(value);
//					}
//					else if(CommandFieldConstants.KEY_DED_AMT.equals(nodeName)){
//						userStateVO.setDeductionAmount(value);
//					}
					// C2C Ends
				}
		    	
		    }
		}
	}
	public static void main(String[] args){
//		String xml="<?xml version='1.0' encoding='UTF-8'?><msg id='36'><params><param name='CAMT'>0.0</param><param name='TPAM'>0.0</param><param name='MOBN'>03214496703</param><param name='RPNAME'>Sajjeed Aslam</param><param name='TAMT'>500.0</param><param name='ACID'/><param name='CAMTF'>0.00</param><param name='TPAMF'>0.00</param><param name='TAMTF'>500.00</param></params></msg> ";
		String xml="<msg id='36'>Response for CW</msg> ";
		System.out.println(
		new USSDXMLUtils().parseExecuteCommandResp(xml));
	}
	public String parseExecuteCommandResp(String xml){
		String retVal=null;
		if(xml !=null && !"".equals(xml)){
			if(xml.contains("errors")){
				retVal=populateErrorMessages(xml);
			}else if(xml.contains("msg")){
				retVal=xml.substring(xml.indexOf(">")+1,xml.lastIndexOf("<"));
				
			}else{
				retVal=xml;
			}
			
		}
		return retVal;
		
	}
	
	
	public void populateMyCommissionValues(String xml, UserState userStateVO){
		if(xml!=null){
			NodeList nodeList = executeXPathQuery(xml, "/params/*") ;
			String value,nodeName;
			for (int i = 0; i < nodeList.getLength(); i++) 
			{
				nodeName=nodeList.item(i).getAttributes().item(0).getNodeValue();
				
				if(nodeName!=null && !"".equals(nodeName) && CommandFieldConstants.KEY_COMM_AMOUNT.equals(nodeName)){
					value = nodeList.item(i).getFirstChild().getNodeValue() + " " ;
					userStateVO.setAgentCommissionAmount(value);
					
				}
				else if(nodeName!=null && !"".equals(nodeName) && CommandFieldConstants.KEY_COMM_START_DATE.equals(nodeName))
				{
					value = nodeList.item(i).getFirstChild().getNodeValue() + " " ;
					userStateVO.setMyCommissionStartDate(value);
				}
				else if(nodeName!=null && !"".equals(nodeName) && CommandFieldConstants.KEY_COMM_END_DATE.equals(nodeName))
				{
					value = nodeList.item(i).getFirstChild().getNodeValue() + " " ;
					userStateVO.setMyCommnissionEndDate(value);
				}
				else if(nodeName!=null && !"".equals(nodeName) && CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT.equals(nodeName))
				{
					value = nodeList.item(i).getFirstChild().getNodeValue() + " " ;
					userStateVO.setAgentCommissionAmount(value);
				}
			}
		}
	}
	
	public String populateErrorMessages(String xml)
	{
		NodeList nodeList = executeXPathQuery(xml, "//msg/errors/*") ;
		String errors = "";
		
		for (int i = 0; i < nodeList.getLength(); i++) 
		{
	    	errors += nodeList.item(i).getFirstChild().getNodeValue() + " " ;
	    }
		
		return errors;
	}
	public void populateLoginMessages(String xml)
	{
		NodeList nodeList = executeXPathQuery(xml, "//params/*") ;
		String isFirstLogin = "";
		
		for (int i = 0; i < nodeList.getLength(); i++) 
		{
			isFirstLogin += nodeList.item(i).getFirstChild().getNodeValue()+"";
	    }
		
		ThreadLocalTBR.setTBR((Long.parseLong(isFirstLogin)));
	}
	private NodeList executeXPathQuery( String xml, String xpathExpression )
	{
		Object result = null;

		try
		{
			
			domFactory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xml)));

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile(xpathExpression);

			result = expr.evaluate(doc, XPathConstants.NODESET);
		}
		catch (XPathExpressionException e)
		{
			e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	    
	    
	    return (NodeList) result;		
	}
	
	public UssdUserStateVO prepareCashInTransactionSummary(String xml, UssdUserStateVO userState)
	{
		return null;
	}

}
