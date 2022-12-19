package com.inov8.microbank.server.service.workflow.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

/**
 *
 *
 */
public abstract class TransactionControllerImpl
    implements TransactionController
{
	protected final transient Log logger = LogFactory.getLog(TransactionControllerImpl.class);

  /**
   * <p>Implements pre-requisites for a transaction.Anything that is needed before
   * starting the transaction is implemented here in this method.</p>
   * @param wrapper
   * @return
   */
  public WorkFlowWrapper start(WorkFlowWrapper wrapper) throws Exception
  {
	if(logger.isDebugEnabled())
	{
		logger.debug("Inside start(WorkFlowWrapper wrapper) method of TransactionControllerImpl");
	}
	
    wrapper = doPreStart(wrapper);
    wrapper = doValidate(wrapper);
    wrapper = doStart(wrapper);
    wrapper = doPostStart(wrapper);
	if(logger.isDebugEnabled())
	{
		logger.debug("Ending start(WorkFlowWrapper wrapper)");
	}
    return wrapper;
  }

  /**
   * <p>The actual transaction is implemented here. All the processing logic goes here.</p>
   * @param wrapper
   * @return
   */
  public WorkFlowWrapper process(WorkFlowWrapper wrapper) throws Exception
  {
	if(logger.isDebugEnabled())
	{
		logger.debug("Inside process(WorkFlowWrapper wrapper) method of TransactionControllerImpl");
	}
    wrapper = doPreProcess(wrapper);
    wrapper = doProcess(wrapper);
    wrapper = doPostProcess(wrapper);
    if(logger.isDebugEnabled())
	{
    	logger.debug("Ending Process(wrapper) of  TransactionControllerImpl");
	}
    return wrapper;
  }

  /**
   * <p>Method for implementing post transaction chores.</p>
   * @param wrapper
   * @return
   */
  public WorkFlowWrapper end(WorkFlowWrapper wrapper) throws Exception
  {
	if(logger.isDebugEnabled())
	{

	  logger.debug("Inside end(WorkFlowWrapper wrapper) method of TransactionControllerImpl");
	}
	
    doPreEnd(wrapper);

    doEnd(wrapper);

    doPostEnd(wrapper);
    if(logger.isDebugEnabled())
	{
    	logger.debug("Ending end(wrapper) of  TransactionControllerImpl");
	}
    return wrapper;
  }
  public WorkFlowWrapper rollback(WorkFlowWrapper wrapper)throws Exception
  {
	  doPreRollback(wrapper);
	  doRollback(wrapper);
	  doPostRollback(wrapper);
	  return wrapper;
  }

  protected WorkFlowWrapper validate(WorkFlowWrapper wrapper) throws Exception
  {
    return this.doValidate(wrapper);
  }

  public WorkFlowWrapper postUpdate(WorkFlowWrapper wrapper) throws Exception{
	  return this.postUpdate(wrapper);
  }
  
  protected abstract WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws
      Exception;

  protected abstract WorkFlowWrapper doStart(WorkFlowWrapper wrapper) throws
      Exception;

  protected abstract WorkFlowWrapper doEnd(WorkFlowWrapper wrapper) throws
      Exception;

  protected abstract WorkFlowWrapper doPostStart(WorkFlowWrapper wrapper);

  protected abstract WorkFlowWrapper doPreEnd(WorkFlowWrapper wrapper)throws Exception;

  protected abstract WorkFlowWrapper doPostEnd(WorkFlowWrapper wrapper);

  protected abstract WorkFlowWrapper
      doPreProcess(WorkFlowWrapper wrapper)throws Exception;

  protected abstract WorkFlowWrapper doPostProcess(WorkFlowWrapper wrapper);

  protected abstract WorkFlowWrapper doProcess(WorkFlowWrapper wrapper) throws
      Exception;

  protected abstract WorkFlowWrapper doPreRollback(WorkFlowWrapper wrapper) throws
      Exception;
  protected abstract WorkFlowWrapper doRollback(WorkFlowWrapper wrapper)throws Exception;

  protected abstract WorkFlowWrapper doPostRollback(WorkFlowWrapper wrapper) throws
      Exception;

  protected abstract WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws
      Exception;

}
