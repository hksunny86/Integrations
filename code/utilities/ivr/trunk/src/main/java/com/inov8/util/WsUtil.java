/*
 * Usage rights pending...
 * 
 * 
 * 
 * 
 * 
 * 
 * ****************************************************************************
 */

package com.inov8.util;

import io.task.context.Context;
import io.task.util.StringUtil;
import io.task.util.TaskConstant;

import java.util.HashMap;
import java.util.Map;


/**<pre>
 * Created By : Ahmed Mobasher Khan
 * Creation Date : Aug 17, 2014
 * 
 * Purpose : 
 * 
 * Updated By : 
 * Updated Date : 
 * Comments : 
 * </pre>
 */
public final class WsUtil
{

	private WsUtil(){}
	
	private static Map<String, String[]> validation = new HashMap<String, String[]>();
	
	static
	{
		loadValidations();
	}
	
	static void loadValidations()
	{
		validation.put(ProductConstant.PROD_ID_50010, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.RECIPIENT_CNIC, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_50000, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.RECIPIENT_MOBILE, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_50006, new String[]{IvrConstant.AGENT_ID, IvrConstant.TX_AMOUNT, IvrConstant.CUSTOMER_MOBILE});
		validation.put(ProductConstant.PROD_ID_50026, new String[]{IvrConstant.AGENT_ID, IvrConstant.BANK_ACC_NUM, IvrConstant.TX_AMOUNT, IvrConstant.CUSTOMER_MOBILE});
		validation.put(ProductConstant.PROD_ID_50031, new String[]{IvrConstant.AGENT_ID, IvrConstant.TX_AMOUNT, IvrConstant.CUSTOMER_MOBILE});
		validation.put(ProductConstant.PROD_ID_6, new String[]{IvrConstant.CUSTOMER_MOBILE});
		validation.put(ProductConstant.PROD_ID_122, new String[]{IvrConstant.CUSTOMER_MOBILE});
		validation.put(ProductConstant.PROD_ID_123, new String[]{IvrConstant.CUSTOMER_MOBILE});
        validation.put(ProductConstant.PROD_ID_125, new String[]{IvrConstant.CUSTOMER_MOBILE});
        validation.put(ProductConstant.PROD_ID_134, new String[]{IvrConstant.CUSTOMER_MOBILE});
		validation.put(ProductConstant.PROD_ID_126, new String[]{IvrConstant.CUSTOMER_MOBILE});
		validation.put(ProductConstant.PROD_ID_2510600, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510700, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510704, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510705, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510708, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510710, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510711, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510715, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510719, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510720, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510738, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510740, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510741, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510742, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510743, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510744, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510745, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510747, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510748, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510749, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510751, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510753, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510756, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510758, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510762, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510764, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510765, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510766, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510767, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510768, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510770, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510772, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510773, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510774, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510776, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510778, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510780, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510782, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510783, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510784, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
		validation.put(ProductConstant.PROD_ID_2510789, new String[]{IvrConstant.AGENT_ID, IvrConstant.CUSTOMER_MOBILE, IvrConstant.CONSUMER_NUMBER, IvrConstant.TX_AMOUNT});
	}

	public static boolean validate(Map<String, Object> dataMap, Context context)
	{
		boolean isValid = true;
		
		@SuppressWarnings("unchecked")
		Map<String, String> paramMap = (Map<String, String>) dataMap.get(IvrConstant.WS_PARAMS);
		String prodId = paramMap.get(IvrConstant.PRODUCT_ID);
		String ivrId = paramMap.get(IvrConstant.IVR_ID);
		String resp = "Failure: ";
		
		if(!context.taskExists(ivrId + prodId))
		{
			resp += " No task found";
			isValid = false;
		}
		else
		{
			String[] validators = validation.get(ivrId+prodId);
			
			if(validators == null) {
/*				resp += " Unknown product ID: " + prodId;
				isValid = false;*/
			} else {
				for(String key : validators)
				{
					if(StringUtil.isNullOrEmpty(paramMap.get(key)))
					{
						resp += key + ", ";
						isValid = false;
					}
				}
				if(isValid == false)
				{
					resp += " is/are required";
				}
			}
		}
		if(isValid == false) {
			dataMap.put(TaskConstant.TASK_STATUS, resp);
		}

		return isValid;
	}

}
