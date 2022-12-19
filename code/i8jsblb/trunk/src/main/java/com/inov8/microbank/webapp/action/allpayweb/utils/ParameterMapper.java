package com.inov8.microbank.webapp.action.allpayweb.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.inov8.microbank.webapp.action.allpayweb.AllPayRequestWrapper;

/**
 * @author kashefbasher
 *
 */
public class ParameterMapper {
	
	private ParameterMapper() {}
	
    public static void mapParameters(Object formBean, AllPayRequestWrapper requestWrapper) {
    	
    	Method[] methods = formBean.getClass().getMethods();
    	
    	for(Method method : methods) {
    		
    		AgentWeb agentWeb= method.getAnnotation(AgentWeb.class);
    		
    		if(agentWeb != null) {
    			
    			Object value = null;
    			
				try {
					
					value = method.invoke(formBean, null);
	    			
					System.out.println(method.getName()+" - "+value);
					
	    			if(value != null) {
	    				
	        			requestWrapper.addParameter(agentWeb.parameterName(), String.valueOf(value));
	    			}
	    			
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
    			
    		}
    	}
    	
    	requestWrapper.toString();
    }	
}
