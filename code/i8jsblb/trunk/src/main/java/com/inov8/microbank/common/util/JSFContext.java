package com.inov8.microbank.common.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class JSFContext {

	
	public static Object getBean(String key) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getELContext().getELResolver().getValue(facesContext.getELContext(), null, key);
    }
	
	public static void removeBean(String bean) 
	{
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.getSession().setAttribute(bean, null);
        request.getSession().removeAttribute(bean);
    }
	
	public static void removeBean(HttpServletRequest request, String bean) 
	{
        request.getSession().setAttribute(bean, null);
        request.getSession().removeAttribute(bean);
    }
	
	public static Object getFromRequest(String objectKey){
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		Object returnedObj = request.getParameter(objectKey);
		if(returnedObj == null)
		{
			returnedObj = request.getAttribute(objectKey);
		}

		return returnedObj;
	}
	
	public static Object getFromRequest(HttpServletRequest request, String objectKey){
		Object returnedObj = request.getParameter(objectKey);
		if(returnedObj == null)
		{
			returnedObj = request.getAttribute(objectKey);
		}

		return returnedObj;
	}
	
	public static void addErrorMessage(String errorMessage)
	{
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(errorMessage));
	}
	
	public static void setInfoMessage(String message){
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest)fc.getExternalContext().getRequest();
		request.setAttribute("infoMessage", message);
	}
	
}
