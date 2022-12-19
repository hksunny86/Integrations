package com.inov8.ivr.dao;

import io.task.loader.database.BeanDefinitionLoader;
import io.task.loader.database.BeanPropertyDefinitionLoader;


public class DataAccessManager
{
	private BeanDefinitionLoader				beanDaoImpl;
	private BeanPropertyDefinitionLoader	beanPropertyDaoImpl;


	public BeanDefinitionLoader getBeanDaoImpl()
	{
		return beanDaoImpl;
	}


	public void setBeanDaoImpl(BeanDefinitionLoader beanDaoImpl)
	{
		this.beanDaoImpl = beanDaoImpl;
	}


	public BeanPropertyDefinitionLoader getBeanPropertyDaoImpl()
	{
		return beanPropertyDaoImpl;
	}


	public void setBeanPropertyDaoImpl(BeanPropertyDefinitionLoader beanPropertyDaoImpl)
	{
		this.beanPropertyDaoImpl = beanPropertyDaoImpl;
	}

}
