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

package com.inov8.microbank.ws.server;

import io.task.util.StringUtil;

import java.util.Properties;

import javax.xml.ws.Endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inov8.util.IvrConstant;
import com.inov8.util.ProjectPropertiesUtil;

/**<pre>
 * Created By : Ahmed Mobasher Khan
 * Creation Date : Aug 13, 2014
 * 
 * Purpose : 
 * 
 * Updated By : 
 * Updated Date : 
 * Comments : 
 * </pre>
 */
@Deprecated
public class WsServer
{
	private static final Logger logger = LoggerFactory.getLogger(WsServer.class);
	
	public static void main(String[] args)
	{
		Properties prop = ProjectPropertiesUtil.getProperties();
		
		String value = prop.getProperty(IvrConstant.PROP_WS_URL);

		if(StringUtil.isNullOrEmpty(value) == false) {
	//		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
	//		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
	//		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
	//		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
			Endpoint.publish(value, new IvrWebServiceImpl());
		}
		else
		{
			logger.error("No web service URL set");
		}
	}
}
