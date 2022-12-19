package com.inov8.util;

import io.task.util.StringUtil;

import java.util.Map;

public class IvrUtil {

	/**<pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 * 
	 * @param dataMap
	 * @param arr - 
	 * </pre>
	 */
	public static void parseTokenToMap(Map<String, Object> dataMap, String[] arr)
	{
		for(String val : arr)
		{
			String []pairs = val.split(";");
			
			for(String pair : pairs)
			{
				String []keyVal = pair.split(":");
				dataMap.put(keyVal[0], keyVal.length == 1 ? null : keyVal[1].equals("null") ? null : keyVal[1]);
			}
		}
	}
	
	public static String mapToTokenString(Map<String, String> wsParamMap)
	{
      	StringBuilder wsParams = new StringBuilder(100);
      	
      	for(String key : wsParamMap.keySet())
      	{
      		if(StringUtil.isNullOrEmpty(key) == false && StringUtil.isNullOrEmpty(wsParamMap.get(key)) == false) {
      			wsParams.append(key).append(':').append(wsParamMap.get(key)).append(';');
      		}
      	}
      	if(wsParams.length() > 0)
      		wsParams.deleteCharAt(wsParams.length() - 1);

      	return wsParams.toString();
	}

	public static String getWsParam(Map<String,Object> dataMap, String key)
	{
		return dataMap.get(key) != null ? dataMap.get(key).toString() : "";
	}
	
	public static String getWsParamForLogging(Map<String, Object> dataMap) {
		return new StringBuilder().append(IvrConstant.UNIQUE_ID).append(": ")
				.append(getWsParam(dataMap, IvrConstant.UNIQUE_ID))
				.append(", ").append(IvrConstant.IVR_ID).append(": ")
				.append(getWsParam(dataMap, IvrConstant.IVR_ID))
				.append(", ").append(IvrConstant.PRODUCT_ID).append(": ")
				.append(getWsParam(dataMap, IvrConstant.PRODUCT_ID))
				.append(", ").append(IvrConstant.TX_ID).append(": ")
				.append(getWsParam(dataMap, IvrConstant.TX_ID))
				.append(", ").append(IvrConstant.AGENT_MOBILE).append(": ")
				.append(getWsParam(dataMap, IvrConstant.AGENT_MOBILE))
				.append(", ").append(IvrConstant.CUSTOMER_MOBILE).append(": ")
				.append(getWsParam(dataMap, IvrConstant.CUSTOMER_MOBILE)).toString();
	}
}
