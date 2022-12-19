/**
 * 
 */
package com.inov8.microbank.common.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Jul 13, 2007
 * Creation Time: 			4:08:50 PM
 * Description:				
 */
public class MfsRequestWrapper implements Serializable
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8245075133454455401L;

	public final static String KEY_MFS_REQUEST_MAP = "_mfs_req_map";
	
	private Map<String, MfsRequestInformation> requestMap = new HashMap<String, MfsRequestInformation>();
	
	public MfsRequestWrapper()
	{
	}

	public MfsRequestInformation getRequestInformation(String key)
	{
		return this.requestMap.get(key);
	}
	
	public void setRequestInformation(String key, MfsRequestInformation mfsRequestInformation)
	{
		this.requestMap.put(key, mfsRequestInformation);
	}

	public static class MfsRequestInformation implements Serializable
	{
		/**
		 * 
		 */
		private static final long	serialVersionUID	= 6966047976270101898L;
		private Long requestTime;
		private String responseXml;
		private boolean isRepeatingRequest = false;
		private String userDeviceId;

		public MfsRequestInformation()
		{

		}

		public MfsRequestInformation(Long requestTime, boolean isRepeating)
		{
			this.requestTime = requestTime;
			this.isRepeatingRequest = isRepeating;
		}

		
		/**
		 * @return the isRepeatingRequest
		 */
		public boolean isRepeatingRequest()
		{
			return isRepeatingRequest;
		}


		/**
		 * @param isRepeatingRequest the isRepeatingRequest to set
		 */
		public void setRepeatingRequest(boolean isRepeatingRequest)
		{
			this.isRepeatingRequest = isRepeatingRequest;
		}


		/**
		 * @return the requestTime
		 */
		public Long getRequestTime()
		{
			return requestTime;
		}


		/**
		 * @param requestTime the requestTime to set
		 */
		public void setRequestTime(Long requestTime)
		{
			this.requestTime = requestTime;
		}


		/**
		 * @return the responseXml
		 */
		public String getResponseXml()
		{
			return responseXml;
		}


		/**
		 * @param responseXml the responseXml to set
		 */
		public void setResponseXml(String responseXml)
		{
			this.responseXml = responseXml;
		}


		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return new StringBuilder("MfsRequestInformation:\nRequest Time: ")
			.append(requestTime)
//			.append("\nXML: ")
//			.append(responseXml)
			.toString();
		}


		public String getUserDeviceId() {
			return userDeviceId;
		}

		public void setUserDeviceId(String userDeviceId) {
			this.userDeviceId = userDeviceId;
		}
	}

}
