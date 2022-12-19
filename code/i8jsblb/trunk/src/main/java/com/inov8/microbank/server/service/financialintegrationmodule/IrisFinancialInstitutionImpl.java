package com.inov8.microbank.server.service.financialintegrationmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchModuleManager;


public class IrisFinancialInstitutionImpl extends AbstractFinancialInstitution
{
//	private String mpinEncryptionKey;
	
	private SwitchModuleManager switchModuleManager;
	
	
	public IrisFinancialInstitutionImpl()
	{
		super();
	}

	public IrisFinancialInstitutionImpl(SwitchController switchController, GenericDao genericDao)
	{
		super(switchController, genericDao);
	}

	@Override
	public SwitchWrapper miniStatement(SwitchWrapper switchWrapper) throws FrameworkCheckedException
	{
		switchWrapper = loadSwitch(switchWrapper);
		switchWrapper = this.switchController.miniStatement(switchWrapper);
		return switchWrapper;
	}
	
	public SwitchWrapper checkBalance(SwitchWrapper switchWrapper) throws FrameworkCheckedException
	{
		switchWrapper = loadSwitch(switchWrapper);
		switchWrapper = this.switchController.checkBalance(switchWrapper);
		return switchWrapper;
	}

	public SwitchWrapper transaction(SwitchWrapper switchWrapper) throws FrameworkCheckedException
	{
		WorkFlowWrapper workFlowWrapper = switchWrapper.getWorkFlowWrapper();
		switchWrapper.setBankId(workFlowWrapper.getSmartMoneyAccountModel().getBankId());
		switchWrapper.setPaymentModeId(workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId());
		
		switchWrapper = loadSwitch(switchWrapper);
		
		switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, workFlowWrapper.getObject(CommandFieldConstants.KEY_CNIC));
		switchWrapper.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
		
		switchWrapper.setWorkFlowWrapper(workFlowWrapper);
		switchWrapper = switchController.transaction(switchWrapper, null);
		return switchWrapper;
	}

	public boolean isVeriflyRequired() throws FrameworkCheckedException, Exception
	{
		return false;
	}

	public SwitchWrapper customerAccountRelationshipInquiry(SwitchWrapper switchWrapper) throws FrameworkCheckedException, Exception
	{
//		switchWrapper.setEncryptionKey(this.mpinEncryptionKey);

		switchWrapper = loadSwitch(switchWrapper);
		switchWrapper = this.switchController.customerAccountRelationshipInquiry(switchWrapper);
		return switchWrapper;
	}
	
	public SwitchWrapper rollback(SwitchWrapper switchWrapper) throws FrameworkCheckedException
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private SwitchWrapper loadSwitch(SwitchWrapper switchWrapper) throws FrameworkCheckedException
	{
		switchWrapper = this.switchModuleManager.getSwitchClassPath(switchWrapper);
		if(switchWrapper.getSwitchSwitchModel() == null || switchWrapper.getSwitchSwitchModel().getPaymentGatewayCode() == null 
				|| switchWrapper.getSwitchSwitchModel().getPaymentGatewayCode().equals("") )
		{
			System.out.println( "Switch mapping or Payment Gateway Code is null or Empty.." );
			throw new FrameworkCheckedException( WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG );
		}
		return switchWrapper;
	}

	public void setSwitchModuleManager(SwitchModuleManager switchModuleManager)
	{
		this.switchModuleManager = switchModuleManager;
	}
	
	

//	public void setMpinEncryptionKey(String mpinEncryptionKey)
//	{
//		this.mpinEncryptionKey = mpinEncryptionKey;
//	}

}
