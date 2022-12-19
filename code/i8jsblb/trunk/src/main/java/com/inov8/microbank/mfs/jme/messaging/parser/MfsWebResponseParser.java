
package com.inov8.microbank.mfs.jme.messaging.parser;

import static com.inov8.microbank.common.util.XMLConstants.ATTR_FAV_NUM;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_FAV_NUM_TYPE;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_MSG_ID;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_PARAM_NAME;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_REQ_ID;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_REQ_TIME;
import static com.inov8.microbank.common.util.XMLConstants.TAG_FAVS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_FAV_NUM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_MSG;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAM;
import static com.inov8.microbank.common.util.XMLConstants.TAG_REQ;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.inov8.microbank.common.model.FavoriteNumbersModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.StringUtil;


/**
 * Project Name: 			Microbank	
 * @author 					Jawwad Farooq
 * Creation Date: 			April 17, 2009  			
 * Description:				
 */

public class MfsWebResponseParser 
{
	/** Logger for this class*/
	protected final Log logger = LogFactory.getLog(getClass());
	
	private XmlPullParserFactory factory = null;

	/**
	 * 
	 */
	public MfsWebResponseParser()
	{		
		this.initialize();
	}

	@SuppressWarnings("unchecked")
	public void parse(String xml, HttpServletRequest request)
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

			parser.setInput(new InputStreamReader(new ByteArrayInputStream(xml.getBytes())));

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
                        	request.setAttribute(CommandFieldConstants.KEY_CURR_COMMAND, 
                            		StringUtil.trimToEmpty(parser.getAttributeValue(null, ATTR_MSG_ID)));
                        	request.setAttribute(CommandFieldConstants.KEY_CURRENT_REQ_TIME, 
                            		StringUtil.trimToEmpty(parser.getAttributeValue(null, ATTR_REQ_TIME)));
                        }
                        else if(TAG_PARAM.equals(startTag))
                        {

                        	request.setAttribute(StringUtil.trimToEmpty(parser.getAttributeValue(null, ATTR_PARAM_NAME)),
                					StringUtil.trimToEmpty(parser.nextText()));
                        }
                        else
                        	if(TAG_REQ.equals(startTag))
                        	{
                        		request.setAttribute(CommandFieldConstants.KEY_REQ_ID, 
                                		StringUtil.trimToEmpty(parser.getAttributeValue(null, ATTR_REQ_ID)));
                        	}
                        	else
                        		if(TAG_FAVS.equals(startTag))
                        		{
                        			favNums = new ArrayList<FavoriteNumbersModel>(5);
                        			request.setAttribute(CommandFieldConstants.KEY_FAV_NO_LIST, favNums);
                        		}
                        		else if(TAG_FAV_NUM.equals(startTag))
                        		{
                        			favNum = new FavoriteNumbersModel();
                        			favNum.setFavoriteNumber(StringUtil.trimToEmpty(parser.getAttributeValue(null, ATTR_FAV_NUM)));
                        			favNum.setFavoriteType(StringUtil.trimToEmpty(parser.getAttributeValue(null, ATTR_FAV_NUM_TYPE)));
                        			favNum.setName(StringUtil.trimToEmpty(parser.nextText()));
                        			//TODO: set favorite Number's Name. It is not in the FavoriteNumbersModel
                        			//favNum.setFavoriteName(parser.nextText());
                        			
                        			favNums.add(favNum);
                        		}
					}
				}
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
			factory = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
			//factory.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
		}
		catch (XmlPullParserException e)
		{
			logger.error("Error creating XmlPullParserFactory", e);
		}
	}
}
