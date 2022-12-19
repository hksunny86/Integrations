package com.inov8;

import java.util.Properties;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.inov8.util.IvrConstant;
import com.inov8.util.ProjectPropertiesUtil;
import com.inov8.util.StartupUtil;

/**
 * <pre>
 * Created By : Ahmed Mobasher Khan
 * 
 * Purpose : 
 * 
 * Updated By : 
 * Updated Date : 
 * Comments : 
 * </pre>
 */
public class Main
{
//	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	/**
	 * <pre>
	 * 
	 * @author - Ahmed Mobasher Khan 
	 * 
	 * @param args - 
	 * </pre>
	 */
	public static void main(String[] args)
	{
		StartupUtil.startup();

		Properties prop = ProjectPropertiesUtil.getProperties();
		
		if(prop.getProperty(IvrConstant.PROP_SPRING_XML) != null) {

			@SuppressWarnings("resource")
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(prop.getProperty(IvrConstant.PROP_SPRING_XML).split("\\,"));
			context.registerShutdownHook();
			context.start();
		}
	}

}
