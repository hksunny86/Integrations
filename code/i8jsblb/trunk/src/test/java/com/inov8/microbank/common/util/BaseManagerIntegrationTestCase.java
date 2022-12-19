package com.inov8.microbank.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

/**
 * This is the base class for all integration tests. It loads the spring xml
 * context files before running any test and destroys it after running all
 * tests. And in the test cases this is the only way of getting bean from
 * context.
 * 
 * @author Shoaib Akhtar
 */
public abstract class BaseManagerIntegrationTestCase
{
	protected final Log log = LogFactory.getLog(getClass());

	private String[] paths = { "src/main/webapp/WEB-INF/test-configurations.xml",
			"src/main/webapp/WEB-INF/applicationContext-*.xml" };

	private static FileSystemXmlApplicationContext ctx;

	/**
	 * This method load Spring Application Context and store it in a static
	 * variable. This method is called before running any integration test (only
	 * once).
	 */
	@BeforeSuite(alwaysRun = true)
	protected void initialize()
	{
		log.info("Loading Spring Application Context for Integration Testing...");
		ctx = new FileSystemXmlApplicationContext(paths);
		log.info("Spring Application Context Loaded Successfully");
	}

	/**
	 * This methods destroys Spring Application Context after running all
	 * integration tests (at the end and only once).
	 */
	@AfterSuite(alwaysRun = true)
	protected void destroy()
	{
		log.info("Destroying Spring Application Context loaded for Integration Testing...");
		ctx.destroy();
		ctx = null;
		log.info("Spring Application Context Destroyed Successfully");
	}

	/**
	 * Returns the Spring Bean against the name given.
	 * 
	 * @param beanName
	 * @return Object
	 */
	protected Object getBean(String beanName)
	{
		log.debug("Getting " + beanName + " Bean From Spring Conext");
		return ctx.getBean(beanName);
	}

}
