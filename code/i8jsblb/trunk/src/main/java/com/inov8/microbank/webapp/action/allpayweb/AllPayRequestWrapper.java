package com.inov8.microbank.webapp.action.allpayweb;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.log4j.Logger;

/**
 * @author Kashif Bashir
 */


final public class AllPayRequestWrapper extends HttpServletRequestWrapper {

    private transient final ConcurrentHashMap<String, String[]> updatedParameterMap = new ConcurrentHashMap<String, String[]>(0);
    
    private transient ConcurrentHashMap<String, String[]> requestParameterMap = new ConcurrentHashMap<String, String[]>(0);
    
	private static final Logger logger = Logger.getLogger(AllPayRequestWrapper.class);
    
    /**
     * @param request
     */
    public AllPayRequestWrapper(HttpServletRequest request) {
    	
		super(request);	
		requestParameterMap.putAll(getParameterMap());
	} 

    
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
     */
    public String getParameter(final String name) {
    	
    	String parameterValue = null;
        String[] strings = getParameterMap().get(name);
        
        if (strings != null) {
        	
        	parameterValue = strings[0];
        }
        
        return parameterValue;
    }
    
 
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequestWrapper#getParameterMap()
     */
    public Map<String, String[]> getParameterMap() {

        requestParameterMap.putAll(super.getParameterMap());
        requestParameterMap.putAll(updatedParameterMap);

        return Collections.unmodifiableMap(requestParameterMap);
    }
    
 
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequestWrapper#getParameterNames()
     */
    public Enumeration<String> getParameterNames() {
    	
        return Collections.enumeration(getParameterMap().keySet());
    }
 
    
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
     */
    public String[] getParameterValues(final String name) {
    	
        return getParameterMap().get(name);
    }
    

    public void addParameter(String key, String[] value) {
    	
    	updatedParameterMap.put(key, value);
    }
    
    
    /**
     * @param key
     * @param value
     */
    public void addParameter(String key, String value) {
    	
    	updatedParameterMap.put(key, new String[]{value});
    }
    
    
    /**
     * @param key
     */
    protected void addNullParameter(String key) {
    	
    	updatedParameterMap.put(key, new String[]{null});
    }
    
    
    /**
     * @param key
     */
    protected void removeParameter(String key) {    	
    	//todo
    }
    
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	
    	final StringBuilder builder = new StringBuilder(); 
    
    	final Iterator<String> iterator = requestParameterMap.keySet().iterator();

		while(iterator.hasNext()){
				
			String key = iterator.next();

			builder.append(key +" = "+getParameter(key)+"\n");
		}
		
		
		return builder.toString();  
    }
}
