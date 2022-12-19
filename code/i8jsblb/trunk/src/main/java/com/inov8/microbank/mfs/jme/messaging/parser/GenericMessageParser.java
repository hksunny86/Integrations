/**
 * 
 */
package com.inov8.microbank.mfs.jme.messaging.parser;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.FavoriteNumbersModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.inov8.microbank.common.util.XMLConstants.*;

//import com.inov8.microbank.j2me.util.Constants;
//import com.inov8.microbank.j2me.util.TagConstants;

/**
 * @author imran.sarwar
 * 
 */
public class GenericMessageParser 
	implements MessageParser
{
	/** Logger for this class*/
	protected final Log logger = LogFactory.getLog(getClass());
	
	private XmlPullParserFactory factory = null;

	/**
	 * 
	 */
	public GenericMessageParser()
	{
		super();
		this.initialize();
	}

	public BaseWrapper parse(String xml) throws ParsingException
	{
		BaseWrapper wrapper = new BaseWrapperImpl();
		parse(xml, wrapper);
		return wrapper;
	}

	@SuppressWarnings("unchecked")
	public void parse(String xml, BaseWrapper wrapper)
			throws ParsingException
	{
		XmlPullParser parser = null;
		ArrayList<FavoriteNumbersModel> favNums = null;
		FavoriteNumbersModel favNum = null;
		
		try
		{
			if(factory==null)
				throw new ParsingException("Error: Parser Factory is not initialized");
			parser = factory.newPullParser();

			/*parser.setInput(new InputStreamReader(new ByteArrayInputStream(xml
					.getBytes())));*/
			parser.setInput(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)),"UTF-8");

			//ArrayList<String> favList = null;  
			int eventType = parser.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				eventType = parser.next();
				if (eventType == XmlPullParser.START_TAG)
				{
					String startTag = StringUtil.trimToEmpty(parser.getName());

					if (startTag != null)
					{
						startTag = startTag.toLowerCase();
                        if(TAG_MSG.equals(startTag))
                        {
                            wrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND, 
                            		StringUtil.trimToEmpty(parser.getAttributeValue(null, ATTR_MSG_ID)));
                            wrapper.putObject(CommandFieldConstants.KEY_CURRENT_REQ_TIME, 
                            		StringUtil.trimToEmpty(parser.getAttributeValue(null, ATTR_REQ_TIME)));
                        }
                        else if(TAG_PARAM.equals(startTag))
                        {

                			wrapper.putObject(StringUtil.trimToEmpty(parser.getAttributeValue(null, ATTR_PARAM_NAME)),
                					StringUtil.trimToEmpty(parser.nextText()));
                        }
                        else
                        	if(TAG_REQ.equals(startTag))
                        	{
                                wrapper.putObject(CommandFieldConstants.KEY_REQ_ID, 
                                		StringUtil.trimToEmpty(parser.getAttributeValue(null, ATTR_REQ_ID)));
                        	}
                        	else
                        		if(TAG_FAVS.equals(startTag))
                        		{
                        			favNums = new ArrayList<FavoriteNumbersModel>(5);
                        			wrapper.putObject(CommandFieldConstants.KEY_FAV_NO_LIST, favNums);
                        		}
                        		else if(TAG_FAV_NUM.equals(startTag))
                        		{
                        			favNum = new FavoriteNumbersModel();
                        			favNum.setFavoriteNumber(StringUtil.trimToEmpty(parser.getAttributeValue(null, ATTR_FAV_NUM)));
                        			favNum.setFavoriteType(StringUtil.trimToEmpty(parser.getAttributeValue(null, ATTR_FAV_NUM_TYPE)));
                        			if(null != parser.getAttributeValue(null, ATTR_REQ_ID) && !"".equals(parser.getAttributeValue(null, ATTR_REQ_ID)))
                        			{
                        				favNum.setFavoriteNumbersId(Long.parseLong(StringUtil.trimToEmpty(parser.getAttributeValue(null, ATTR_REQ_ID))));
                        			}
                        			favNum.setName(StringUtil.trimToEmpty(parser.nextText()));
                        			//TODO: set favorite Number's Name. It is not in the FavoriteNumbersModel
                        			//favNum.setFavoriteName(parser.nextText());
                        			
                        			favNums.add(favNum);
                        		}
					}
				}
//				else if (eventType == XmlPullParser.END_TAG)
//				{
//					String endTag = parser.getName();
//
//					if (endTag != null)
//					{
//						endTag = endTag.toLowerCase();
//						System.out.println("Tag: " + endTag);
//					}
//				}
//				else
//				{
//				}
			}

		}
		catch (IOException ex)
		{
			//System.out.println("Exxxxx: 1");
			//ex.printStackTrace();
			throw new ParsingException(ex);
		}
		catch (XmlPullParserException ex)
		{
			//System.out.println("Exxxxx: 2");
			//ex.printStackTrace();
			throw new ParsingException(ex);
		}
		finally
		{
			favNums = null;
			favNum = null;
		}
	}

	
	private void initialize()
	{
		try
		{
			factory = XmlPullParserFactory.newInstance(
					System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
			//factory.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
		}
		catch (XmlPullParserException e)
		{
			logger.error("Error creating XmlPullParserFactory", e);
		}
	}
}
