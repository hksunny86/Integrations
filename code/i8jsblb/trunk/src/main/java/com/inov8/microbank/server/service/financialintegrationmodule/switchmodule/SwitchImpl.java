package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule;

import org.springframework.context.ApplicationContext;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.ola.integration.vo.OLAVO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class SwitchImpl
    extends SwitchProcessor
{
	
	public SwitchImpl( ApplicationContext ctx )
	{
		
		
	}
	
	
  /**
   * checkBalance
   *
   * @param accountInfo SwitchWrapper
   * @return SwitchWrapper
   * @throws FrameworkCheckedException
   * @todo Implement this com.inov8.microbank.server.service.switchmodule.Switch method
   */
  public SwitchWrapper checkBalance(SwitchWrapper switchWrapper) throws
      WorkFlowException
  {
    AuditLogModel auditLogModel = this.auditLogBeforeCall( switchWrapper, "" );

    switchWrapper.setBalance(2500000D);
    switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode( "00" );

    this.auditLogAfterCall( auditLogModel, "" ) ;

    return switchWrapper;
  }

  /**
   * rollback
   *
   * @param transactions SwitchWrapper
   * @return SwitchWrapper
   * @throws FrameworkCheckedException
   * @todo Implement this com.inov8.microbank.server.service.switchmodule.Switch method
   */
  public SwitchWrapper rollback(SwitchWrapper switchWrapper) throws
      WorkFlowException
  {
    AuditLogModel auditLogModel = this.auditLogBeforeCall( switchWrapper, "" );

    switchWrapper.setBalance(0D);
    switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode( "00" );

    this.auditLogAfterCall( auditLogModel, "" ) ;

    return switchWrapper;
  }


  public SwitchWrapper transaction(SwitchWrapper switchWrapper ) throws
      WorkFlowException
  {
    AuditLogModel auditLogModel = this.auditLogBeforeCall( switchWrapper, "" );

    switchWrapper.setBalance(2500000D);
    switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode( "00" );

    this.auditLogAfterCall( auditLogModel, "" ) ;

    return switchWrapper;
  }


public SwitchWrapper createAccount(SwitchWrapper wrapper) throws WorkFlowException
{
	// TODO Auto-generated method stub
	return null;
}


public SwitchWrapper debitCreditAccount(SwitchWrapper wrapper) throws WorkFlowException
{
	// TODO Auto-generated method stub
	return null;
}


public SwitchWrapper getLedger(SwitchWrapper switchWrapper)
		throws WorkFlowException, FrameworkCheckedException, Exception {
	// TODO Auto-generated method stub
	return null;
}


public SwitchWrapper verifyWalkinCustomerThroughputLimits(SwitchWrapper wrapper)
		throws WorkFlowException, FrameworkCheckedException, Exception {
	// TODO Auto-generated method stub
	return null;
}


public SwitchWrapper saveWalkinCustomerLedgerEntry(OLAVO olavo) throws Exception {
// TODO Auto-generated method stub
return null;
}

public SwitchWrapper getPhoenixTransactions(SwitchWrapper switchWrapper) throws Exception{
	return null;
}


public SwitchWrapper updateLedger(SwitchWrapper switchWrapper) throws WorkFlowException {
	// TODO Auto-generated method stub
	return null;
}

public SwitchWrapper reverseFundTransfer(SwitchWrapper switchWrapper) throws Exception{
	return null;
}


@Override
public SwitchWrapper creditAccountAdvice(SwitchWrapper wrapper)
		throws WorkFlowException, FrameworkCheckedException {
	return null;
}

}
