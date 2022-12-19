package com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule;

import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacade;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.thoughtworks.xstream.XStream;

public class VeriflyManagerImplMock extends VeriflyManagerImpl
{

	public VeriflyManagerImplMock(FailureLogManager failureLogManager, XStream xstream, PostedTransactionReportFacade postedTransactionReportFacade)
	{
		super(failureLogManager, xstream, postedTransactionReportFacade);
		// TODO Auto-generated constructor stub
	}
	@Override
	public VeriflyBaseWrapper verifyPIN(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception
	{
		
			if(true)
			{
				throw new java.net.SocketException("Connection reset");
			}
		
		
		return veriflyBaseWrapper;

	}

}
