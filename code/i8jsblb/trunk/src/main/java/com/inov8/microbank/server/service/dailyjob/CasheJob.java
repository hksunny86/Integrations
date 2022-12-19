package com.inov8.microbank.server.service.dailyjob;

import org.apache.log4j.Logger;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.util.LocatorCacheUtil;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;

public class CasheJob {
	
	private static final Logger LOGGER = Logger.getLogger( CasheJob.class );
	
	private RetailerContactManager retailerContactManager;
	
	
	public void init(){
		
		LOGGER.info("***************************Executing Agents Cashe Job*******************************");
		
		try {
			LocatorCacheUtil.setRcsvModels(retailerContactManager.findRetailerContactModelList());
		} catch (FrameworkCheckedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setRetailerContactManager(
			RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}


}
