/**
 * 
 */
package com.inov8.microbank.mfs.jme.messaging.parser;

import com.inov8.framework.common.wrapper.BaseWrapper;


/**
 * @author imran.sarwar
 * 
 */
public interface MessageParser
{
	public BaseWrapper parse(String str) 
		throws ParsingException; 

	public void parse(String str, BaseWrapper wrapper) 
		throws ParsingException; 
}
