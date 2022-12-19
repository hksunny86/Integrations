package com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.VeriflyModel;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;

public class VeriflyManagerServiceMock extends VeriflyManagerService
{
	
	@Override
	public VeriflyManagerImpl getVeriflyMgrByAccountId( SmartMoneyAccountModel smartMoneyAccountModel )throws FrameworkCheckedException
	  {
	    BaseWrapper baseWrapper = new BaseWrapperImpl() ;
	    BankModel bankModel = new BankModel();
	    VeriflyModel veriflyModel = new VeriflyModel() ;

	    baseWrapper.setBasePersistableModel( smartMoneyAccountModel );
	    smartMoneyAccountModel = (SmartMoneyAccountModel)smartMoneyAccountManager.loadSmartMoneyAccount( baseWrapper ).getBasePersistableModel() ;

	    bankModel.setBankId( smartMoneyAccountModel.getBankId() );
	    baseWrapper.setBasePersistableModel( bankModel );
	    bankModel = loadBankModel( baseWrapper);

	    veriflyModel.setVeriflyId( bankModel.getVeriflyId() );    
	    veriflyModel = loadVeriflyModel( veriflyModel.getVeriflyId() ) ;

	    VeriflyManagerImpl veriflyManagerImpl = new VeriflyManagerImpl( this.auditLogModuleFacade, this.xstream, this.postedTransactionReportFacade);
	    veriflyManagerImpl.setVeriflyManager( getVeriflyManager(veriflyModel) );

	    return veriflyManagerImpl;

	  }
	@Override
	protected VeriflyManager getVeriflyManager(VeriflyModel veriflyModel)
	  {
	    /*factory.setServiceUrl( veriflyModel.getUrl() ) ;
	    factory.setServiceInterface( VeriflyManager.class ) ;
	    factory.setHttpInvokerRequestExecutor(commonsHttpInvokerRequestExecutor);
	    factory.afterPropertiesSet();
	    */
	    return new VeriflyManagerImplMock(this.auditLogModuleFacade, this.xstream, this.postedTransactionReportFacade);
	  }
	
	

}
