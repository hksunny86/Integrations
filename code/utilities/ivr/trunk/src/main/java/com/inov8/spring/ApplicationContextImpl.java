package com.inov8.spring;

import io.task.spring.SpringApplicationContextWrapper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <pre>
 * 
 * Purpose : This class implements the ApplicationContextAware. A bean which implements 
 * the ApplicationContextAware-interface and is deployed into the context, will be 
 * called back on creation of the bean, using the interfaces setApplicationContext method,
 * and provided with a reference to the context, which may be stored for later interaction 
 * with the context. 
 * 
 * Updated By : 
 * Updated Date : 
 * Comments :
 * </pre>
 */
public class ApplicationContextImpl implements ApplicationContextAware
{
	private static ApplicationContext	context;

	public static Object getBean(String arg)
	{
		return context.getBean(arg);
	}
	
	public static <T> T getBean(String name, Class<T> clazz)
	{
		return context.getBean(name, clazz);
	}

	public static ApplicationContext getContext()
	{
		return context;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException
	{
		context = ctx;
		SpringApplicationContextWrapper.setContext(context);
	}

}
