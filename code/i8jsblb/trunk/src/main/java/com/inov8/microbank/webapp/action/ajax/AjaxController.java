/**
 * 
 */
package com.inov8.microbank.webapp.action.ajax;

import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.inov8.framework.webapp.action.ControllerUtils;
import com.inov8.microbank.server.service.activatedeactivate.ActivateDeactivateManager;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Jan 4, 2007
 * Creation Time: 			4:55:35 PM
 * Description:				
 */
public abstract class AjaxController extends AbstractController
{
	private String contentType = null;
	private boolean doShowCustomExceptionMsg = true;
	protected static Log logger = LogFactory.getLog(AjaxController.class);
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public final ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) 
		throws Exception
	{
		
	    String responseContent = null;

	    try 
	    {
	    	responseContent = getResponseContent(request, response);
	    } 
	    catch (Exception ex) {
	      // Send back a 500 error code.
			logger.error("---", ex);
			if(doShowCustomExceptionMsg)
			{
				responseContent = new AjaxXmlBuilder().addItemAsCData(ActivateDeactivateManager.KEY_MSG, ex.getMessage()).toString();
			}
			else
			{
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
				return null;
			}
	    }

	    // Set content to xml
	    response.setContentType(getContentType()+"; charset=UTF-8");
	    response.setHeader("Cache-Control", "no-cache");
	    PrintWriter pw = response.getWriter();
	    pw.write(responseContent);
	    pw.close();		
		return null;
	}

	  /**
	   * Each child class should override this method to generate the specific XML content necessary for
	   * each AJAX controller.
	   *
	   * @param request the {@javax.servlet.http.HttpServletRequest} object
	   * @param response the {@javax.servlet.http.HttpServletResponse} object
	   * @return a {@java.lang.String} representation of the htnl/XML response/content
	   */
	  public abstract String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception;

	  
	  protected String getMessage(HttpServletRequest request, String key, Object []args)
	  {
		  return getMessageSourceAccessor().getMessage(key, args, request.getLocale());
	  }

	  protected String getMessage(HttpServletRequest request, String key, Locale locale)
	  {
		  return getMessageSourceAccessor().getMessage(key, locale);
	  }

	  protected String getMessage(HttpServletRequest request, String key)
	  {
		  return getMessageSourceAccessor().getMessage(key, request.getLocale());
	  }

	  protected void saveMessage(HttpServletRequest request, String msg)
	  {
		  ControllerUtils.saveMessage(request, msg);
	  }
	  
	  public String getContentType()
	{
		if(contentType==null)
			return "text/xml";
		else
			return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	/**
	 * @param doShowCustomExceptionMsg the doShowCustomExceptionMsg to set
	 */
	public void setDoShowCustomExceptionMsg(boolean doShowCustomExceptionMsg)
	{
		this.doShowCustomExceptionMsg = doShowCustomExceptionMsg;
	}

	/**
	 * @return the doShowCustomExceptionMsg
	 */
	public boolean isDoShowCustomExceptionMsg()
	{
		return doShowCustomExceptionMsg;
	}

	
}
