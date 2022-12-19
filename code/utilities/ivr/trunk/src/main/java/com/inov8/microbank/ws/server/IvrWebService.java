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

import javax.jws.WebMethod;
import javax.jws.WebService;


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

@WebService
public interface IvrWebService
{
	@WebMethod
	public String initPinRequest(IVRRequestDTO ivrRequestDTO);
}
