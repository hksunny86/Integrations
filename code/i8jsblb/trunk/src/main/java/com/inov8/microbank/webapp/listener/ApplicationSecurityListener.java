/**
 * 
 */
package com.inov8.microbank.webapp.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Jan 15, 2007
 * Creation Time: 			12:25:18 PM
 * Description:				
 */
public class ApplicationSecurityListener implements ApplicationListener
{

	public void onApplicationEvent(ApplicationEvent event)
	{
//		if ( event instanceof AuthorizedEvent )
//		{
//			AuthorizedEvent authorizedEvent = ( AuthorizedEvent ) event;
//			System.out.println ( "authorized:" + authorizedEvent );
//		}
//		else if ( event instanceof AuthorizationFailureEvent )
//		{
//			AuthorizationFailureEvent authorizationFailureEvent = ( AuthorizationFailureEvent ) event;
//			System.out.println ( "not authorized:" + authorizationFailureEvent );
//		}
//		else if ( event instanceof AuthenticationFailureBadCredentialsEvent )
//		{
//			AuthenticationFailureBadCredentialsEvent badCredentialsEvent = ( AuthenticationFailureBadCredentialsEvent ) event;
//			badCredentialsEvent.getException().printStackTrace();
//			System.out.println ( "badCredentials:" + badCredentialsEvent.getException().getMessage());
//		}
//		else if ( event instanceof AuthenticationSuccessEvent )
//		{
//			AuthenticationSuccessEvent authenticationSuccessEvent = ( AuthenticationSuccessEvent ) event;
//			System.out.println ( "authSuccess:" + authenticationSuccessEvent );
//		}
//		else
//		{
//			System.out.println ( "undefined: " + event.getClass ().getName () );
//		}		
	}

}
