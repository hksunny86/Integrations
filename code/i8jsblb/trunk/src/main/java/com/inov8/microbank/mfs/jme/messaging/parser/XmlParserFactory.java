/**
 * 
 */
package com.inov8.microbank.mfs.jme.messaging.parser;

import java.util.HashMap;
import java.util.Map;


/**
 * @author imran.sarwar
 *
 */
public final class XmlParserFactory
{
	private static Map<String, MessageParser> parsers = new HashMap<String, MessageParser>();
	
	public static final String KEY_GENERIC_PARSER = "0";

	static 
	{
		//Here add special parsers if required for special parsing
		parsers.put(KEY_GENERIC_PARSER, new GenericMessageParser());
	}
	/**
	 * 
	 */
	private XmlParserFactory()
	{
	}

	public static MessageParser getParser(String str)
	{
		/*
		String command = StringUtil.extractCommand(str);
		
		if(command!=null && StringUtil.trimToEmpty(command).length()!=0)
		{
			if(parsers.containsKey(command))
				return parsers.get(command);
			else
				return parsers.get(KEY_GENERIC_PARSER);
		}
		*/
		return parsers.get(KEY_GENERIC_PARSER);
	}

}
