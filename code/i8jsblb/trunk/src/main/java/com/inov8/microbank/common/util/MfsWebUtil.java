package com.inov8.microbank.common.util;

/**
 * Project Name: 			Microbank	
 * @author 					Jawwad Farooq
 * Creation Date: 			April 17, 2009  			
 * Description:				
 */

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.inov8.framework.common.util.EncoderUtils;



public class MfsWebUtil
{
	
	public static String buildRequestXML( HttpServletRequest request, String commandId )
	{
        StringBuffer message = null;
        String commandID = request.getParameter(XMLConstants.ATTR_MSG_ID);
        
        if(request==null)
            return "";
        
        message = new StringBuffer();
        appendStartMsgTag(commandId, message, request);
        
        
        // Handle all messages
        
        prepareMsg(request, message, commandID);
        
        appendEndMsgTag(message);
        System.out.println();
        
        return message.toString();
	}
	
    
    private static void prepareMsg(HttpServletRequest request, StringBuffer buf, String command) {
        if( request == null )
            return;
        else
        {
            buf.append("<").append(XMLConstants.TAG_PARAMS).append(">");
            if(request.getAttribute(CommandFieldConstants.KEY_DEVICE_TYPE_ID) != null 
            		&& !request.getAttribute(CommandFieldConstants.KEY_DEVICE_TYPE_ID).equals("")
            		&& request.getAttribute(CommandFieldConstants.KEY_DEVICE_TYPE_ID).toString().equals(DeviceTypeConstantsInterface.ALLPAY_WEB.toString().trim()))
            {
            	appendDeviceTypeParamForAllPay(buf);
            	
            }
            else
            {
            	appendDeviceTypeParam(buf);
            }
            
            for(Enumeration e = request.getParameterNames();  e.hasMoreElements();) {
                String param = (String)e.nextElement();
                Object val = request.getParameter(param);

            	if(val instanceof String)
            	{
            		if( param.equalsIgnoreCase(CommandFieldConstants.KEY_ACC_ID) )
            		{
            			// .... Parse the ACID parameter and build the corresponding three parameter against that
            			if( val.toString().indexOf(XMLConstants.ATTR_ACC_NO) != -1 )
            			{
            				String accNo, accType, accSts="", accCur = "" ;
            				accNo = val.toString().substring(val.toString().indexOf(XMLConstants.ATTR_ACC_NO) + 6, val.toString().indexOf(";"));
            				accType = val.toString().substring(val.toString().indexOf(XMLConstants.ATTR_ACC_TYPE) + 8, val.toString().indexOf(";",val.toString().indexOf(XMLConstants.ATTR_ACC_TYPE) + 8));
            				accSts = val.toString().substring(val.toString().indexOf(XMLConstants.ATTR_ACC_STATUS) + 7, val.toString().indexOf(";",val.toString().indexOf(XMLConstants.ATTR_ACC_STATUS) + 7));
            				accCur = val.toString().substring(val.toString().indexOf(XMLConstants.ATTR_ACC_CURRENCY) + 7);
            				
            				
            				appendParam(buf, CommandFieldConstants.KEY_ACCOUNT_NUMBER, accNo, request);
            				appendParam(buf, CommandFieldConstants.KEY_ACCOUNT_TYPE, accType, request);
            				appendParam(buf, CommandFieldConstants.KEY_ACCOUNT_STATUS, accSts, request);
            				appendParam(buf, CommandFieldConstants.KEY_ACCOUNT_CURRENCY, accCur, request);            				
            			}
            			else
            				appendParam(buf, param, ((String)val).trim(), request);
            		}
            		else if( param.equalsIgnoreCase(CommandFieldConstants.KEY_CUST_CODE_FAV) && (request.getParameter(CommandFieldConstants.KEY_CUST_CODE) != null && 
            				!request.getParameter(CommandFieldConstants.KEY_CUST_CODE).equals("") ))
            		{
            		}
            		else if( param.equalsIgnoreCase(CommandFieldConstants.KEY_CUST_CODE_FAV) && (request.getParameter(CommandFieldConstants.KEY_CUST_CODE) != null && 
            				request.getParameter(CommandFieldConstants.KEY_CUST_CODE).equals("") ) )
            		{
            			appendParam(buf, CommandFieldConstants.KEY_CUST_CODE, ((String)val).trim(), request);
            		}
            		else if( param.equalsIgnoreCase(CommandFieldConstants.KEY_MOB_NO_FAV) && (request.getParameter(CommandFieldConstants.KEY_MOB_NO) != null && 
            				request.getParameter(CommandFieldConstants.KEY_MOB_NO).equals("") ) )
            		{
            			appendParam(buf, CommandFieldConstants.KEY_MOB_NO, ((String)val).trim(), request);
            		}
            		else if( param.equalsIgnoreCase(CommandFieldConstants.KEY_MOB_NO_FAV) && (request.getParameter(CommandFieldConstants.KEY_MOB_NO) != null && 
            				!request.getParameter(CommandFieldConstants.KEY_MOB_NO).equals("") ))
            		{
            		}
            		else if( param.equalsIgnoreCase(CommandFieldConstants.KEY_MOB_NO) && ((String)val) != null && ((String)val).trim().equals("") )
            		{
            		}
            		else if( param.equalsIgnoreCase(CommandFieldConstants.KEY_CREDIT_CARD_NO_FAV) && (request.getParameter(CommandFieldConstants.KEY_CREDIT_CARD_NO) != null && 
            				!request.getParameter(CommandFieldConstants.KEY_CREDIT_CARD_NO).equals("") ))
            		{
            		}
            		else if( param.equalsIgnoreCase(CommandFieldConstants.KEY_CREDIT_CARD_NO_FAV) && (request.getParameter(CommandFieldConstants.KEY_CREDIT_CARD_NO) != null && 
            				request.getParameter(CommandFieldConstants.KEY_CREDIT_CARD_NO).equals("") ) )
            		{
            			appendParam(buf, CommandFieldConstants.KEY_CREDIT_CARD_NO, ((String)val).trim(), request);
            		}
            		else if( param.equalsIgnoreCase(CommandFieldConstants.KEY_CREDIT_CARD_NO) && (request.getParameter(CommandFieldConstants.KEY_CREDIT_CARD_NO) != null && 
            				request.getParameter(CommandFieldConstants.KEY_CREDIT_CARD_NO).equals("") ) )
            		{
            		}
            		
            		else if( param.equalsIgnoreCase(CommandFieldConstants.KEY_DEL_FAV_NUMBER) )
            		{
            			String [] delFavoriteNumbersId = request.getParameterValues(CommandFieldConstants.KEY_DEL_FAV_NUMBER) ;
            			String commaSeparatedIds = "" ;
            			
            			for( String favNumberId : delFavoriteNumbersId )
            			{
            				commaSeparatedIds += favNumberId + "," ; 
            			}
            			commaSeparatedIds = commaSeparatedIds.substring(0, commaSeparatedIds.length()-1);
            			
            			appendParam(buf, CommandFieldConstants.KEY_DEL_FAV_NUMBER, ((String)commaSeparatedIds).trim(), request);
            		}
            		
            		
            		
            		else if( !param.equalsIgnoreCase(XMLConstants.ATTR_MSG_ID) ) // Excludes the ID parameter
            			appendParam(buf, param, ((String)val).trim(), request);
            	}
            }
            
            buf.append("</").append(XMLConstants.TAG_PARAMS).append(">");
        }
    }
	
	
    private static void appendParam(StringBuffer msg, String name, String value, HttpServletRequest request) 
    {
    	if( value.indexOf("&") == -1 || (name.equalsIgnoreCase(XMLConstants.ATTR_PIN) || name.equalsIgnoreCase(XMLConstants.ATTR_NEW_PIN) || name.equalsIgnoreCase(XMLConstants.ATTR_CONFIRM_PIN))) // Dont include special characters in XML
    	{
	    	if(name.equalsIgnoreCase(XMLConstants.ATTR_PIN) || name.equalsIgnoreCase(XMLConstants.ATTR_NEW_PIN) || name.equalsIgnoreCase(XMLConstants.ATTR_CONFIRM_PIN)
	    			|| name.equals(CommandFieldConstants.KEY_TPIN) || name.equals(CommandFieldConstants.KEY_CVV) )
	    	{
	    		if((null != request.getParameter(CommandFieldConstants.KEY_ACC_ID) && !"".equals(request.getParameter(CommandFieldConstants.KEY_ACC_ID))) || (null != request.getParameter(CommandFieldConstants.KEY_ACCOUNT_NUMBER) && !"".equals(request.getParameter(CommandFieldConstants.KEY_ACCOUNT_NUMBER))) || (null != request.getParameter(CommandFieldConstants.KEY_BANK_ID) && !"".equals(request.getParameter(CommandFieldConstants.KEY_BANK_ID))))
	    		{
	    			value = encryptPin(value);
	    		}
	    		else
	    		{
	    			value = encryptPinUsingSha(value);
	    		}
	    	}
	        msg.append("<").append(XMLConstants.TAG_PARAM)
	        	.append(" ")
	        	.append(XMLConstants.ATTR_PARAM_NAME)
	            .append("=\"").append(name.trim())
	            .append("\">")
	            .append(value.trim())
	            .append("</")
	            .append(XMLConstants.TAG_PARAM)
	            .append(">");
    	}
    }
    
    private static void appendDeviceTypeParam(StringBuffer msg) 
    {
        msg.append("<").append(XMLConstants.TAG_PARAM)
        	.append(" ")
        	.append(XMLConstants.ATTR_PARAM_NAME)
            .append("=\"").append(CommandFieldConstants.KEY_DEVICE_TYPE_ID)
            .append("\">")
            .append(DeviceTypeConstantsInterface.MFS_WEB)
            .append("</")
            .append(XMLConstants.TAG_PARAM)
            .append(">");
    }
    
    private static void appendDeviceTypeParamForAllPay(StringBuffer msg) 
    {
        msg.append("<").append(XMLConstants.TAG_PARAM)
        	.append(" ")
        	.append(XMLConstants.ATTR_PARAM_NAME)
            .append("=\"").append(CommandFieldConstants.KEY_DEVICE_TYPE_ID)
            .append("\">")
            .append(DeviceTypeConstantsInterface.ALLPAY_WEB)
            .append("</")
            .append(XMLConstants.TAG_PARAM)
            .append(">");
    }
    
    
    private static void appendStartMsgTag(String cmd, StringBuffer buf, HttpServletRequest request)
    {
        buf.append("<").append(XMLConstants.TAG_MSG).append(" ")
        	.append(XMLConstants.ATTR_MSG_ID).append("=\"").append(cmd).append("\" ");
        
        	if( request.getParameter(XMLConstants.ATTR_REQ_TIME) != null && !request.getParameter(XMLConstants.ATTR_REQ_TIME).equals("") )
        		buf.append(XMLConstants.ATTR_REQ_TIME).append("=\"").append(request.getParameter(XMLConstants.ATTR_REQ_TIME));
        	else
        		buf.append(XMLConstants.ATTR_REQ_TIME).append("=\"").append(System.currentTimeMillis());
            
        	buf.append("\">");
    }
    
    private static void appendEndMsgTag(StringBuffer buf)
    {
        buf.append("</").append(XMLConstants.TAG_MSG).append(">");
    }
    
    public static String encryptPin(String pin) {
    	if(pin != null) {
    		pin = EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, pin);
    	}
    	return pin;
	}
    
    public static String encryptPinUsingSha(String pin)
	{
		if(pin != null)
		{
			
			pin = EncoderUtils.encodeToSha(pin);
		}
		return pin;
	}
    
    public static boolean isErrorXML( String xml )
    {
    	if( xml.indexOf("<msg id=\"-1\">") == -1 )
    		return false;
    	else
    		return true;
    }
    
    public static boolean isPINChangeRequired( String xml )
    {
    	if( xml.indexOf("<param name=\"IPCR\">1</param>") != -1 )
    		return true;
    	else
    		return false;
    }
    public static boolean isPasswordChangeRequired( String xml )
    {
    	if( xml.indexOf("<param name=\"IPSCR\">1</param>") != -1 )
    		return true;
    	else
    		return false;
    }
	
}
