package com.inov8.integration.nadra.service;

import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;


public interface NadraBillManager {
	
	public PhoenixIntegrationMessageVO viewBill(PhoenixIntegrationMessageVO messageVO) throws Exception;
	
	public PhoenixIntegrationMessageVO payBill(PhoenixIntegrationMessageVO messageVO) throws Exception;
	
	public PhoenixIntegrationMessageVO getCompaniesList(PhoenixIntegrationMessageVO messageVO) throws Exception;
}
