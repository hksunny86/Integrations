/**
 *
 */
package com.inov8.microbank.mfs.jme.model;

import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.StringUtil;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * @author imran.sarwar
 *
 */
public class Message
{

	private long code = 0;
	private ErrorLevel level = null;
	private String descr = null;
	private String nadraSession="";
	private String thirdPartyTransactionId = "";

	/**
	 *
	 */
	public Message(ErrorLevel level, String descr)
	{
		this(0, level, descr);
	}

	/**
	 *
	 */
	public Message(long code, ErrorLevel level, String descr)
	{
		super();
		this.code = code;
		this.level = level;
		this.descr = descr;
	}

	public Message(long code, ErrorLevel level, String descr,String nadraSession)
	{
		super();
		this.code = code;
		this.level = level;
		this.descr = descr;
		this.nadraSession = nadraSession;
	}

	public Message(long code, ErrorLevel level, String descr,String nadraSession,String thirdPartyTransactionId)
	{
		super();
		this.code = code;
		this.level = level;
		this.descr = descr;
		this.nadraSession = nadraSession;
		this.thirdPartyTransactionId = thirdPartyTransactionId;
	}

	public long getCode()
	{
		return code;
	}
	public String getDescr()
	{
		return descr;
	}
	public ErrorLevel getLevel()
	{
		return level;
	}

	public String toXml(boolean isError)
	{
	//	<error code="" level=""></error>
    //  <mesg level=""></mesg>
		if(nadraSession == null || (nadraSession != null && nadraSession.equalsIgnoreCase("null")))
			nadraSession = "";
		if(thirdPartyTransactionId == null || (thirdPartyTransactionId != null && (thirdPartyTransactionId.equalsIgnoreCase("null")
		|| thirdPartyTransactionId.contains("null"))))
			thirdPartyTransactionId = "";

		StringBuilder xml = new StringBuilder();
		xml.append(TAG_SYMBOL_OPEN);
		if(isError)
		{
			xml.append(TAG_ERROR);
			// code
			xml.append(TAG_SYMBOL_SPACE)
			.append(ATTR_CODE)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(this.code)
			.append(TAG_SYMBOL_QUOTE);

			xml.append(TAG_SYMBOL_SPACE)
					.append(ATTR_NADRA_SESSION)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(this.nadraSession)
					.append(TAG_SYMBOL_QUOTE);

			xml.append(TAG_SYMBOL_SPACE)
					.append(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID)
					.append(TAG_SYMBOL_EQUAL)
					.append(TAG_SYMBOL_QUOTE)
					.append(this.thirdPartyTransactionId)
					.append(TAG_SYMBOL_QUOTE);
		}
		else
			xml.append(TAG_MESG);

			// level
			xml.append(" ")
			.append(ATTR_LEVEL)
			.append(TAG_SYMBOL_EQUAL)
			.append(TAG_SYMBOL_QUOTE)
			.append(this.level.level())
			.append(TAG_SYMBOL_QUOTE)

			.append(TAG_SYMBOL_CLOSE)
			.append(StringUtil.trimToEmpty(descr))
			.append(TAG_SYMBOL_OPEN_SLASH);
			if(isError)
				xml.append(TAG_ERROR);
			else
				xml.append(TAG_MESG);

			xml.append(TAG_SYMBOL_CLOSE);

		return xml.toString();
	}

	public String getNadraSession() {
		return nadraSession;
	}
}
