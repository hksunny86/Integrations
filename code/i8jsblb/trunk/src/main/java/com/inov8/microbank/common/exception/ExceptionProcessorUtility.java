/**
 * 
 */
package com.inov8.microbank.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;



/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Aug 8, 2007
 * Creation Time: 			11:34:48 AM
 * Description:				
 */
public class ExceptionProcessorUtility
{
	private static WorkFlowExceptionTranslator workFlowExceptionTranslator;
	
	public static String prepareExceptionStackTrace(Exception ex)
	{
		if(ex instanceof WorkFlowException)
		{
			ex = workFlowExceptionTranslator.translateWorkFlowException(ex, 0);
		}
		StringBuffer preparedMessage = new StringBuffer();
		String header = "\n*******************************************************************************************\n*                                      Exception Occured                                  *\n*******************************************************************************************\n*\n*";
		String footer = "******************************************************************************************\n*                                                                                         *\n*******************************************************************************************\n\n";
		preparedMessage.append(header+"\n*\n*\n*");
		preparedMessage.append(ex.toString()+"\n*\n*\n");
		/*if(null != ex.getMessage())
		{
			preparedMessage.append("\t" + ex.getMessage() + "\n*\n*\n*\n");
		}
		*/
		StackTraceElement[] stackTraceElement = ex.getStackTrace();
		for (int i = 0; i < stackTraceElement.length; i++)
		{
			preparedMessage.append("*\t" + stackTraceElement[i] + "\n");
		}

		/*Throwable ourCause = ex.getCause();
		
		if (ourCause != null)
			printStackTraceAsCause(ourCause, stackTraceElement,preparedMessage);
			*/
		preparedMessage.append("\n*\n*\n*\n*"+footer);
		

		return preparedMessage.toString();

	}

	/**
	 * @Kashif.Bashir
	 * @param throwable
	 * @return
	 */
	public static String getStackTrace(Throwable throwable) {
		
		   Writer result = new StringWriter();
		   PrintWriter printWriter = new PrintWriter(result);
		   throwable.printStackTrace(printWriter);
		   
		   return result.toString();
		}		
	
	/**
	 * @param workFlowExceptionTranslator the workFlowExceptionTranslator to set
	 */
	public void setWorkFlowExceptionTranslator(WorkFlowExceptionTranslator workFlowExceptionTranslator)
	{
		this.workFlowExceptionTranslator = workFlowExceptionTranslator;
	}

}
