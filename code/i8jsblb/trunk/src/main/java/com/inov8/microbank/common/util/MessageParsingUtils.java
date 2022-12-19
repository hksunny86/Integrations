package com.inov8.microbank.common.util;

/**
 * @author maqsood shahzad
 */

public class MessageParsingUtils
{

	public static String parseMessageForIpos(String message)
	{
		if (null != message)
		{
			String tempMessage = message.toLowerCase();
			if (tempMessage.contains(ApplicationNamesConstantsInterface.ASKARI_TIMEPEY.toLowerCase()))
			{
				do
				{
				int index = tempMessage.indexOf(ApplicationNamesConstantsInterface.ASKARI_TIMEPEY.toLowerCase());
				message = message.substring(0, index) + ApplicationNamesConstantsInterface.THE_WORD_ALLPAY
						+ message.substring(index + 14, message.length());
				tempMessage = message.toLowerCase() ;
				}while(tempMessage.contains(ApplicationNamesConstantsInterface.ASKARI_TIMEPEY.toLowerCase()));
			}
			
			if (tempMessage.contains(ApplicationNamesConstantsInterface.THE_WORD_TIMEPEY.toLowerCase()))
			{
				do
				{	
				int index = tempMessage.indexOf(ApplicationNamesConstantsInterface.THE_WORD_TIMEPEY.toLowerCase());
				message = message.substring(0, index) + ApplicationNamesConstantsInterface.THE_WORD_ALLPAY
						+ message.substring(index + 5, message.length());
				tempMessage = message.toLowerCase() ;
				}
				while(tempMessage.contains(ApplicationNamesConstantsInterface.THE_WORD_TIMEPEY.toLowerCase()));
			}
			
			if (tempMessage.contains(ApplicationNamesConstantsInterface.THE_WORD_ASKARI.toLowerCase()) && !(message.contains(ApplicationNamesConstantsInterface.THE_WORD_ASKARI.toLowerCase())))
			{
				do
				{
				int index = tempMessage.indexOf(ApplicationNamesConstantsInterface.THE_WORD_ASKARI.toLowerCase());
				message = message.substring(0, index) + ApplicationNamesConstantsInterface.THE_WORD_ALLPAY
						+ message.substring(index + 8, message.length());
				tempMessage = message.toLowerCase() ;
				}while(tempMessage.contains(ApplicationNamesConstantsInterface.THE_WORD_ASKARI.toLowerCase()) && !(tempMessage.contains(ApplicationNamesConstantsInterface.THE_WORD_ASKARI.toLowerCase())));
				

			}
		}

		return message;
	}

	public static void main(String[] args)
	{
		String msg1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><msg id=\"31\"><trans><trn amt=\"100.0\" amtf=\"100.00\" bcode=\"5\" code=\"TXSTVZCR7U\" date=\"Mon Jun 02 18:24:20 PKT 2008\" datef=\"Mon, Jun 02, 2008\" helpLine=\"Pleae Call  for help regarding  \" mNo=\"03344227165\" pmode=\" AC 001\" prod=\"Bank CC\" supp=\"Bank\" timef=\"06:24:20 pm\" type=\"11\"/></trans></msg>";
		String msg2 = "This is a MWallet test message";
		String msg3 = "This is a Askari test message";

		System.out.println("--------------(1)" + MessageParsingUtils.parseMessageForIpos(msg1));
		System.out.println("--------------(2)" + MessageParsingUtils.parseMessageForIpos(msg2));
		System.out.println("--------------(3)" + MessageParsingUtils.parseMessageForIpos(msg3));
	}

}
