package com.inov8.microbank.authenticationmethod.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

/**
 * 
 * @author Atieq Rehman
 * @since 23 07 2015 
 *
 */
public interface AuthenticationMethod {

	/**
	 * performs step 1 for selected authentication method for account holder or transaction validation
	 * 
	 * @param workFlowWrapper
	 * @return
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper initiate(WorkFlowWrapper workFlowWrapper) throws Exception;
	
	
	/**
	 * performs step 2 for selected authentication method to validate account holder or transaction being executed
	 * 
	 * @param workFlowWrapper
	 * @return
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper validate(WorkFlowWrapper workFlowWrapper) throws Exception;
	
	/**
	 * perform step 3 for selected authentication method to update any data required for selected transaction
	 * 
	 * @param workFlowWrapper
	 * @return
	 * @throws FrameworkCheckedException
	 */
	public WorkFlowWrapper process(WorkFlowWrapper workFlowWrapper) throws Exception;

	public WorkFlowWrapper updateTransactionDetails(WorkFlowWrapper workFlowWrapper) throws Exception;

}
