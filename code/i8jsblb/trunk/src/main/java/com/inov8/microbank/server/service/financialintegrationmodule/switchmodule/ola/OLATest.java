package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ola;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.inov8.microbank.common.model.SwitchModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchFactory;
import com.inov8.ola.integration.vo.OLAVO;

public class OLATest
{
	
	public static void main(String[] args)
	{
		try
		{
		 final String[] paths = { "src/main/webapp/WEB-INF/test-configurations.xml",
		 "src/main/webapp/WEB-INF/applicationContext-*.xml" };
	     final FileSystemXmlApplicationContext ctx;
		 ctx = new FileSystemXmlApplicationContext(paths);
		 
		SwitchFactory factory = (SwitchFactory) ctx.getBean("switchFactory");
		System.out.println("Starting main");
		SwitchWrapper wrapper = new SwitchWrapperImpl();
		OLAVO ola = new OLAVO();
		ola.setPayingAccNo("112223654869");
		SwitchModel model = new SwitchModel();
		model.setUrl("http://127.0.0.1:8080/olaintegration/ws/olaSwitch");
		wrapper.setBankId(60000L);
		wrapper.setPaymentModeId(3L);
		
		wrapper.setSwitchSwitchModel(model);
		OLASwitchImpl sw = (OLASwitchImpl) factory.getSwitch(wrapper);
		sw.checkBalance(wrapper);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}

}
