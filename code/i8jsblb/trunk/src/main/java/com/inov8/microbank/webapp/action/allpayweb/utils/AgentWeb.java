package com.inov8.microbank.webapp.action.allpayweb.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kashefbasher
 */

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD)
public @interface AgentWeb {  
	
    String parameterName();  
}
