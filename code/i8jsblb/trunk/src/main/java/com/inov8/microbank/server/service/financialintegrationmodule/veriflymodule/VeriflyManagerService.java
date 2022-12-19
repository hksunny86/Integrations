package com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule;

import org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.VeriflyModel;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.server.dao.settlementmodule.VeriflyDAO;
import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacade;
import com.inov8.microbank.server.service.bankmodule.BankManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;
import com.thoughtworks.xstream.XStream;

public class VeriflyManagerService
{
  HttpInvokerProxyFactoryBean factory = new HttpInvokerProxyFactoryBean();


  protected SmartMoneyAccountManager smartMoneyAccountManager;
  protected BankManager bankManager;
  protected VeriflyDAO veriflyDAO;
  protected FailureLogManager auditLogModuleFacade;
  protected XStream xstream;
  protected CommonsHttpInvokerRequestExecutor commonsHttpInvokerRequestExecutor; 
  protected PostedTransactionReportFacade postedTransactionReportFacade;
  protected VeriflyManager veriflyManager;
  
  public void setPostedTransactionReportFacade(
		PostedTransactionReportFacade postedTransactionReportFacade) {
	this.postedTransactionReportFacade = postedTransactionReportFacade;
}

public void setCommonsHttpInvokerRequestExecutor(CommonsHttpInvokerRequestExecutor commonsHttpInvokerRequestExecutor)
{
	this.commonsHttpInvokerRequestExecutor = commonsHttpInvokerRequestExecutor;
}

public VeriflyManagerImpl getVeriflyMgrByAccountId( SmartMoneyAccountModel smartMoneyAccountModel )throws FrameworkCheckedException
  {
    BaseWrapper baseWrapper = new BaseWrapperImpl() ;
    BankModel bankModel = new BankModel();
    VeriflyModel veriflyModel = new VeriflyModel() ;

    baseWrapper.setBasePersistableModel( smartMoneyAccountModel );
    smartMoneyAccountModel = (SmartMoneyAccountModel)this.smartMoneyAccountManager.loadSmartMoneyAccount( baseWrapper ).getBasePersistableModel() ;

    bankModel.setBankId( smartMoneyAccountModel.getBankId() );
    baseWrapper.setBasePersistableModel( bankModel );
    bankModel = loadBankModel( baseWrapper);

    veriflyModel.setVeriflyId( bankModel.getVeriflyId() );    
    veriflyModel = loadVeriflyModel( veriflyModel.getVeriflyId() ) ;

    VeriflyManagerImpl veriflyManagerImpl = new VeriflyManagerImpl( this.auditLogModuleFacade, this.xstream, this.postedTransactionReportFacade);
    veriflyManagerImpl.setVeriflyManager( getVeriflyManager(veriflyModel) );

    return veriflyManagerImpl;

  }

  protected BankModel loadBankModel(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    return (BankModel)this.bankManager.loadBank( baseWrapper ).getBasePersistableModel() ;
  }
  
  protected VeriflyModel loadVeriflyModel(Long veriflyModelId) throws
  FrameworkCheckedException
  {	
	  VeriflyModel veriflyModel = (VeriflyModel)this.veriflyDAO.findByPrimaryKey( veriflyModelId ) ;
	  
	  if( !veriflyModel.getActive() )
	  {
		  throw new FrameworkCheckedException( WorkFlowErrorCodeConstants.VERIFLY_INACTIVE );			  
	  }		  
	  return veriflyModel;
  }

  public VeriflyManagerImpl getVeriflyMgrByBankId( BankModel bankModel )throws FrameworkCheckedException
  {
	  
    BaseWrapper baseWrapper = new BaseWrapperImpl() ;
    VeriflyModel veriflyModel = new VeriflyModel() ;

    baseWrapper.setBasePersistableModel( bankModel );
    bankModel = loadBankModel( baseWrapper ) ;

    veriflyModel.setVeriflyId( bankModel.getVeriflyId() );
    baseWrapper.setBasePersistableModel( veriflyModel );
    veriflyModel = loadVeriflyModel( veriflyModel.getVeriflyId() ) ;

    VeriflyManagerImpl veriflyManagerImpl = new VeriflyManagerImpl( this.auditLogModuleFacade, this.xstream, this.postedTransactionReportFacade);
    veriflyManagerImpl.setVeriflyManager( getVeriflyManager(veriflyModel) );

    return veriflyManagerImpl;
  }

  protected VeriflyManager getVeriflyManager(VeriflyModel veriflyModel)
  {
    /*factory.setServiceUrl( veriflyModel.getUrl() ) ;
    factory.setServiceInterface( VeriflyManager.class ) ;
    factory.setHttpInvokerRequestExecutor(commonsHttpInvokerRequestExecutor);
    factory.afterPropertiesSet();
    
    return (VeriflyManager)factory.getObject();
    */
	  return veriflyManager;
  }

  public void setSmartMoneyAccountManager(SmartMoneyAccountManager
                                          smartMoneyAccountManager)
  {
    this.smartMoneyAccountManager = smartMoneyAccountManager;
  }

  public void setBankManager(BankManager bankManager)
  {
    this.bankManager = bankManager;
  }

  public void setVeriflyDAO(VeriflyDAO veriflyDAO)
  {
    this.veriflyDAO = veriflyDAO;
  }

  public void setXstream(XStream xstream)
  {
    this.xstream = xstream;
  }

  public void setAuditLogModuleFacade(FailureLogManager auditLogModuleFacade)
  {
    this.auditLogModuleFacade = auditLogModuleFacade;
  }

public void setVeriflyManager(VeriflyManager veriflyManager) {
	this.veriflyManager = veriflyManager;
}


}
