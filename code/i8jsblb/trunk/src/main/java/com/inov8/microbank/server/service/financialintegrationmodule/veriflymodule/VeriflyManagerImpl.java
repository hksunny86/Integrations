package com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.RemoteAccessException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionReportModel;
import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacade;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.logmodule.LogWrapper;
import com.inov8.verifly.server.service.mainmodule.VeriflyManager;
import com.thoughtworks.xstream.XStream;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class VeriflyManagerImpl implements VeriflyManager
{

	protected VeriflyManager veriflyManager;

	protected FailureLogManager failureLogManager;

	protected XStream xstream;
	protected final transient Log logger = LogFactory.getLog(VeriflyManagerImpl.class);

	private PostedTransactionReportFacade postedTransactionReportFacade;

	public VeriflyBaseWrapper verifyOneTimePin(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception

	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside verifyOneTimePin method of VeriflyManagerImpl...");
		}

		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(veriflyBaseWrapper, IntgTransactionTypeConstantsInterface.VERIFY_ONE_TIME_PIN_VERIFLY);
		try

		{

			veriflyBaseWrapper = veriflyManager.verifyOneTimePin(veriflyBaseWrapper);

		}

		catch (Exception ex)

		{
			
			logger.error("Exception occured during verifyOneTimePin..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1

			|| ex instanceof RemoteAccessException)

				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
			throw ex;
			}

		}

		finally

		{

			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
			if(baseWrapper != null){
				createOrUpdatePostedTransactionAfterCall(veriflyBaseWrapper, baseWrapper);
			}
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending verifyOneTimePin method of VeriflyManagerImpl...");
		}

		return veriflyBaseWrapper;

	}

	public VeriflyBaseWrapper verifyCredentials(VeriflyBaseWrapper veriflyBaseWrapper)
			throws Exception {
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside verifyPIN method of VeriflyManagerImpl...");
		}
		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(veriflyBaseWrapper, IntgTransactionTypeConstantsInterface.VERIFY_CREDENTIALS_VERIFLY);
		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Going to hit verifly to execute verifyPIN...");
			}
			veriflyBaseWrapper = veriflyManager.verifyCredentials(veriflyBaseWrapper);
		}
		
		catch (Exception ex)
		{
			ex.printStackTrace();
			
			if(ex instanceof IOException)
			{
				veriflyBaseWrapper.setErrorMessage(FailureReasonsConstantsInterface.CONNECTION_RESET);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE);
			}
			
			else
			 if(ex instanceof RemoteAccessException)
			{
				 throw new RemoteAccessException( WorkFlowErrorCodeConstants.VERIFLY_INACTIVE );
			}
			
			logger.error("Exception occured during verifyPIN..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1 || ex instanceof RemoteAccessException)
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
				throw ex;
			}
		}
		finally
		{
			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
			if(baseWrapper != null){
				createOrUpdatePostedTransactionAfterCall(veriflyBaseWrapper, baseWrapper);
			}
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending verifyPIN method of VeriflyManagerImpl...");
		}

		return veriflyBaseWrapper;

	}

	
	/**
	 * verifyPIN
	 *
	 * @param veriflyBaseWrapper VeriflyBaseWrapper
	 * @return VeriflyBaseWrapper
	 * @throws Exception
	 */
	public VeriflyBaseWrapper verifyPIN(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside verifyPIN method of VeriflyManagerImpl...");
		}
		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);
		BaseWrapper baseWrapper = createOrUpdatePostedTransactionBeforeCall(veriflyBaseWrapper, IntgTransactionTypeConstantsInterface.VERIFY_PIN_VERIFLY);
		
		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Going to hit verifly to execute verifyPIN...");
			}
			veriflyBaseWrapper = veriflyManager.verifyPIN(veriflyBaseWrapper);
		}
		
		catch (Exception ex)
		{
			ex.printStackTrace();
			
			if(ex instanceof IOException)
			{
				veriflyBaseWrapper.setErrorMessage(FailureReasonsConstantsInterface.CONNECTION_RESET);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE);
			}
			
			else
			 if(ex instanceof RemoteAccessException)
			{
				 throw new RemoteAccessException( WorkFlowErrorCodeConstants.VERIFLY_INACTIVE );
			}
			
			logger.error("Exception occured during verifyPIN..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1 || ex instanceof RemoteAccessException)
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
				throw ex;
			}
		}
		finally
		{
			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
			if(baseWrapper != null){
				createOrUpdatePostedTransactionAfterCall(veriflyBaseWrapper, baseWrapper);
			}
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending verifyPIN method of VeriflyManagerImpl...");
		}
		return veriflyBaseWrapper;
	}
	
	public BaseWrapper createOrUpdatePostedTransactionBeforeCall(VeriflyBaseWrapper veriflyBaseWrapper, Long intgTransactionType) throws FrameworkCheckedException
	{
		PostedTransactionReportModel postedTransactionReportModel = new PostedTransactionReportModel();  
		
		postedTransactionReportModel.setTransactionCodeId(veriflyBaseWrapper.getLogModel().getTransactionCodeId());
		postedTransactionReportModel.setIntgTransactionTypeId(intgTransactionType);
		
		if(UserUtils.getCurrentUser() != null){
			postedTransactionReportModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			postedTransactionReportModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		}
		else{
			postedTransactionReportModel.setCreatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
			postedTransactionReportModel.setUpdatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
		}
		
		postedTransactionReportModel.setCreatedOn(new Date());
		postedTransactionReportModel.setUpdatedOn(new Date());
		
		String rrn = (String) veriflyBaseWrapper.getObject(CommandFieldConstants.KEY_RRN);
		if(!StringUtil.isNullOrEmpty(rrn)){
			postedTransactionReportModel.setRefCode(rrn);
		}
		String stan = (String) veriflyBaseWrapper.getObject(CommandFieldConstants.KEY_STAN);
		if(!StringUtil.isNullOrEmpty(stan)){
			postedTransactionReportModel.setSystemTraceAuditNumber(stan);
		}
		Long productId = (Long) veriflyBaseWrapper.getObject(CommandFieldConstants.KEY_PRODUCT_ID);
		if(productId != null){
			postedTransactionReportModel.setProductId(productId);
		}

		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(postedTransactionReportModel);
		baseWrapper = postedTransactionReportFacade.createOrUpdatePostedTransactionRequiresNewTransaction(baseWrapper);
		return baseWrapper;
	}

	
	public BaseWrapper createOrUpdatePostedTransactionAfterCall(VeriflyBaseWrapper veriflyBaseWrapper, BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		PostedTransactionReportModel postedTransactionReportModel = (PostedTransactionReportModel)baseWrapper.getBasePersistableModel();
		
		postedTransactionReportModel.setResponseCode(String.valueOf(veriflyBaseWrapper.isErrorStatus()));
		postedTransactionReportModel.setFromAccount(veriflyBaseWrapper.getAccountInfoModel().getAccountNo());
		if(UserUtils.getCurrentUser() != null)
			postedTransactionReportModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		else
			postedTransactionReportModel.setUpdatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
		postedTransactionReportModel.setUpdatedOn(new Date());
		
		baseWrapper.setBasePersistableModel(postedTransactionReportModel);
		
		baseWrapper = postedTransactionReportFacade.createOrUpdatePostedTransactionRequiresNewTransaction(baseWrapper);
		
		return baseWrapper;
	}
	
	public VeriflyBaseWrapper markAsDeleted(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside markAsDeleted method of VeriflyManagerImpl ...");
		}
		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);

		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Hitting Verifly to call markAsDeleted...");
			}
			veriflyBaseWrapper = veriflyManager.markAsDeleted(veriflyBaseWrapper);
		}
		catch (Exception ex)
		{
			if(ex instanceof IOException)
			{
				veriflyBaseWrapper.setErrorMessage(FailureReasonsConstantsInterface.CONNECTION_RESET);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE);
			}
			if(ex instanceof RemoteAccessException)
			{
				throw new RemoteAccessException( WorkFlowErrorCodeConstants.VERIFLY_INACTIVE );
			}
			logger.error("Exception occured during markAsDeleted..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1 || ex instanceof RemoteAccessException)
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
			throw ex;
			}
		}
		finally
		{
			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending markAsDeleted method of VeriflyManagerImpl...");
		}
		return veriflyBaseWrapper;

	}

	public VeriflyManagerImpl(FailureLogManager failureLogManager, XStream xstream, PostedTransactionReportFacade postedTransactionReportFacade )
	{
		this.failureLogManager = failureLogManager;
		this.xstream = xstream;
		this.postedTransactionReportFacade = postedTransactionReportFacade;
	}

	/**
	 * activatePIN
	 *
	 * @param veriflyBaseWrapper VeriflyBaseWrapper
	 * @return VeriflyBaseWrapper
	 * @throws Exception
	 */
	public VeriflyBaseWrapper activatePIN(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside activatePin method of VeriflyManagerImpl...");
		}
		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);

		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Hitting Verifly to call activatePIN...");
			}
			veriflyBaseWrapper = veriflyManager.activatePIN(veriflyBaseWrapper);
		}
		catch (Exception ex)
		{
			if(ex instanceof IOException)
			{
				veriflyBaseWrapper.setErrorMessage(FailureReasonsConstantsInterface.CONNECTION_RESET);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE);
			}
			else if(ex instanceof RemoteAccessException)
			{
				throw new RemoteAccessException( WorkFlowErrorCodeConstants.VERIFLY_INACTIVE );
			}
			logger.error("Exception occured during activatePIN..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1 || ex instanceof RemoteAccessException)
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
				throw ex;
			}
		}
		finally
		{
			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending activatePin method of VeriflyManagerImpl...");
		}

		return veriflyBaseWrapper;
	}

	protected AuditLogModel auditLogBeforeCall(VeriflyBaseWrapper veriflyBaseWrapper) throws FrameworkCheckedException
	{
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside auditLogBeforeCall method of VeriflyManagerImpl...");
		}

		//System.out.println("-----Testing-----"+this.xstream.toXML(veriflyBaseWrapper));

		AuditLogModel auditLogModel = new AuditLogModel();
		auditLogModel.setTransactionStartTime(new Timestamp(new java.util.Date().getTime()));
		auditLogModel.setIntegrationModuleId(IntegrationModuleConstants.VERIFLY_MODULE);
		auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(this.xstream.toXML(veriflyBaseWrapper),
				XPathConstants.veriflyManagerAuditLogInputParamLocationSteps));
		auditLogModel.setTransactionCodeId(veriflyBaseWrapper.getLogModel().getTransactionCodeId());
		if(ThreadLocalActionLog.getActionLogId() == null)
			auditLogModel.setActionLogId(1L);
		else
			auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(auditLogModel);
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending auditLogBeforeCall method of VeriflyManagerImpl...");
		}

		return (AuditLogModel) this.failureLogManager.auditLogRequiresNewTransaction(baseWrapper).getBasePersistableModel();
	}

	protected void auditLogAfterCall(AuditLogModel auditLogModel, VeriflyBaseWrapper veriflyBaseWrapper)
			throws FrameworkCheckedException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside auditLogAfterCall method of VeriflyManagerImpl...");
		}
		/*
		 * Following code will get the credit card number, account number, expiry date and pin
		 * And replace them with '*'
		 */

		//		AccountInfoModel accountInfoModel = veriflyBaseWrapper.getAccountInfoModel();
		//		String tempCardNo = accountInfoModel.getCardNo() ;
		//		String tempAccountNo = accountInfoModel.getAccountNo() ;
		//		String tempCardExpiryDate = accountInfoModel.getCardExpiryDate() ;
		//		String tempPin = accountInfoModel.getPin() ;	
		//		String temp ;
		//		
		//		if( accountInfoModel.getCardNo() != null )
		//		{
		//			temp = accountInfoModel.getCardNo().substring(0,accountInfoModel.getCardNo().length()-4).replaceAll("\\w", "*") + accountInfoModel.getCardNo().substring(accountInfoModel.getCardNo().length()-4) ;
		//			accountInfoModel.setCardNo(temp);
		//		}
		//		if( accountInfoModel.getAccountNo() != null )
		//		{
		//			temp = accountInfoModel.getAccountNo().substring(0,accountInfoModel.getAccountNo().length()-4).replaceAll("\\w", "*") + accountInfoModel.getAccountNo().substring(accountInfoModel.getAccountNo().length()-4) ;
		//			accountInfoModel.setAccountNo(temp);
		//		}
		//		if( accountInfoModel.getPin() != null )
		//			accountInfoModel.setPin(accountInfoModel.getPin().replaceAll("\\w", "*"));
		//		if( accountInfoModel.getCardExpiryDate() != null )
		//			accountInfoModel.setCardExpiryDate(accountInfoModel.getCardExpiryDate().replaceAll("\\w", "*"));

		auditLogModel.setOutputParam(XMLUtil.replaceElementsUsingXPath(this.xstream.toXML(veriflyBaseWrapper),
				XPathConstants.veriflyManagerAuditLogOutputParamLocationSteps));
		auditLogModel.setTransactionEndTime(new Timestamp(new java.util.Date().getTime()));
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(auditLogModel);

		//		accountInfoModel.setCardNo(tempCardNo) ;
		//		accountInfoModel.setAccountNo(tempAccountNo) ;
		//		accountInfoModel.setCardExpiryDate(tempCardExpiryDate) ;
		//		accountInfoModel.setPin(tempPin) ;

		if(logger.isDebugEnabled())
		{
			logger.debug("Ending auditLogAfterCall method of VeriflyManagerImpl...");
		}
		this.failureLogManager.auditLogRequiresNewTransaction(baseWrapper);
	}

	/**
	 * changePIN
	 *
	 * @param veriflyBaseWrapper VeriflyBaseWrapper
	 * @return VeriflyBaseWrapper
	 * @throws Exception
	 */
	public VeriflyBaseWrapper changePIN(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside changePIN method of VeriflyManagerImpl...");
		}
		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);

		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Hitting Verifly to call changePin...");
			}
			veriflyBaseWrapper = veriflyManager.changePIN(veriflyBaseWrapper);
		}
		catch (Exception ex)
		{
			if(ex instanceof IOException)
			{
				veriflyBaseWrapper.setErrorMessage(FailureReasonsConstantsInterface.CONNECTION_RESET);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE);
			}
			else if(ex instanceof RemoteAccessException)
			{
				throw new RemoteAccessException( WorkFlowErrorCodeConstants.VERIFLY_INACTIVE );
			}
			logger.error("Exception occured during changePIN..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1 || ex instanceof RemoteAccessException)
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
				throw ex;
			}
		}
		finally
		{
			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending changePIN method of VeriflyManagerImpl...");
		}
		return veriflyBaseWrapper;

	}

	/**
	 * deactivatePIN
	 *
	 * @param veriflyBaseWrapper VeriflyBaseWrapper
	 * @return VeriflyBaseWrapper
	 * @throws Exception
	 */
	public VeriflyBaseWrapper deactivatePIN(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside deactivatePIN method of VeriflyManagerImpl ...");
		}
		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);

		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Hitting Verifly to call deactivatePIN ...");
			}
			veriflyBaseWrapper = veriflyManager.deactivatePIN(veriflyBaseWrapper);
		}
		catch (Exception ex)
		{
			if(ex instanceof IOException)
			{
				veriflyBaseWrapper.setErrorMessage(FailureReasonsConstantsInterface.CONNECTION_RESET);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE);
			}
			else if(ex instanceof RemoteAccessException)
			{
				throw new RemoteAccessException( WorkFlowErrorCodeConstants.VERIFLY_INACTIVE );
			}
			
			logger.error("Exception occured during deactivatePIN..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1 || ex instanceof RemoteAccessException)
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
			throw ex;
			}
			
		}
		finally
		{
			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending deactivatePIN method of VeriflyManagerImpl ...");
		}

		return veriflyBaseWrapper;

	}

	/**
	 * deleteAccount
	 *
	 * @param veriflyBaseWrapper VeriflyBaseWrapper
	 * @return VeriflyBaseWrapper
	 * @throws Exception
	 */
	public VeriflyBaseWrapper deleteAccount(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside deleteAccount method of VeriflyManagerImpl ...");
		}
		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);

		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Hitting Verifly to execute deleteAccount ...");
			}
			veriflyBaseWrapper = veriflyManager.deleteAccount(veriflyBaseWrapper);
		}
		catch (Exception ex)
		{
			if(ex instanceof IOException)
			{
				veriflyBaseWrapper.setErrorMessage(FailureReasonsConstantsInterface.CONNECTION_RESET);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE);
			}
			else if(ex instanceof RemoteAccessException)
			{
				throw new RemoteAccessException( WorkFlowErrorCodeConstants.VERIFLY_INACTIVE );
			}
			logger.error("Exception occured during deleteAccount..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1 || ex instanceof RemoteAccessException)
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
				throw ex;
			}
		}
		finally
		{
			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending deleteAccount method of VeriflyManagerImpl ...");
		}

		return veriflyBaseWrapper;

	}

	/**
	 * generatePIN
	 *
	 * @param veriflyBaseWrapper VeriflyBaseWrapper
	 * @return VeriflyBaseWrapper
	 * @throws Exception
	 */
	public VeriflyBaseWrapper generatePIN(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside generatePIN method of VeriflyManagerImpl ...");
		}
		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);

		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Going to hit Verifly to execute generatePIN...");
			}
			veriflyBaseWrapper = veriflyManager.generatePIN(veriflyBaseWrapper);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			if(ex instanceof IOException)
			{
				veriflyBaseWrapper.setErrorMessage(FailureReasonsConstantsInterface.CONNECTION_RESET);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE);
			}
			else if(ex instanceof RemoteAccessException)
			{
				throw new RemoteAccessException( WorkFlowErrorCodeConstants.VERIFLY_INACTIVE );
			}
			logger.error("Exception occured during generatePIN..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1 || ex instanceof RemoteAccessException)
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
				throw ex;
			}
		}
		finally
		{
			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending generatePIN of VeriflyManagerImpl...");
		}

		return veriflyBaseWrapper;

	}

	/**
	 * getLog
	 *
	 * @param logWrapper LogWrapper
	 * @return LogWrapper
	 * @throws Exception
	 */
	public LogWrapper getLog(LogWrapper logWrapper) throws Exception
	{
		return this.veriflyManager.getLog(logWrapper);
	}

	/**
	 * getLog
	 *
	 * @param veriflyBaseWrapper VeriflyBaseWrapper
	 * @return VeriflyBaseWrapper
	 * @throws Exception
	 */
	public VeriflyBaseWrapper getLog(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside getLog method of VeriflyManagerImpl...");
		}
		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);

		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Going to hit Verifly to execute getLog...");
			}
			veriflyBaseWrapper = veriflyManager.getLog(veriflyBaseWrapper);
		}
		catch (Exception ex)
		{
			
			logger.error("Exception occured during getLog..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1 || ex instanceof RemoteAccessException)
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
				throw ex;
			}
		}
		finally
		{
			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
		}
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending getLog method of VeriflyManagerImpl...");
		}

		return veriflyBaseWrapper;

	}

	/**
	 * modifyAccountInfo
	 *
	 * @param veriflyBaseWrapper VeriflyBaseWrapper
	 * @return VeriflyBaseWrapper
	 * @throws Exception
	 */
	public VeriflyBaseWrapper modifyAccountInfo(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside modifyAccountInfo method of VeriflyManagerImpl...");
		}
		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);

		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Going to hit verifly to execute modifyAccountInfo...");
			}
			veriflyBaseWrapper = veriflyManager.modifyAccountInfo(veriflyBaseWrapper);
		}
		catch (Exception ex)
		{
			if(ex instanceof IOException)
			{
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE);
			}
			else if(ex instanceof RemoteAccessException)
			{
				throw new RemoteAccessException( WorkFlowErrorCodeConstants.VERIFLY_INACTIVE );
			}
			logger.error("Exception occured during modifyAccountInfo..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1 || ex instanceof RemoteAccessException)
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
				throw ex;
			}
		}
		finally
		{
			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending modifyAccountInfo method of VeriflyManagerImpl...");
		}
		return veriflyBaseWrapper;

	}

	/**
	 * modifyAccountInfoForBBAgents
	 *
	 * @param veriflyBaseWrapper VeriflyBaseWrapper
	 * @return VeriflyBaseWrapper
	 * @throws Exception
	 */
	public VeriflyBaseWrapper modifyAccountInfoForBBAgents(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception
	{
		if(logger.isDebugEnabled()){
			logger.debug("Inside modifyAccountInfoForBBAgents method of VeriflyManagerImpl...");
		}
		
		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);

		try{
			if(logger.isDebugEnabled()){
				logger.debug("Going to hit verifly to execute modifyAccountInfoForBBAgents...");
			}
			veriflyBaseWrapper = veriflyManager.modifyAccountInfoForBBAgents(veriflyBaseWrapper);
		}catch (Exception ex){
			if(ex instanceof IOException){
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE);
			}else if(ex instanceof RemoteAccessException){
				throw new RemoteAccessException( WorkFlowErrorCodeConstants.VERIFLY_INACTIVE );
			}
			logger.error("Exception occured during modifyAccountInfoForBBAgents..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1 || ex instanceof RemoteAccessException)
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
				throw ex;
			}
		}finally{
			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("Ending modifyAccountInfoForBBAgents method of VeriflyManagerImpl...");
		}
		return veriflyBaseWrapper;

	}

	/**
	 * resetPIN
	 *
	 * @param veriflyBaseWrapper VeriflyBaseWrapper
	 * @return VeriflyBaseWrapper
	 * @throws Exception
	 */
	public VeriflyBaseWrapper resetPIN(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside resetPIN method of VeriflyManagerImpl...");
		}
		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);

		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Going to hit Verifly to execute resetPIN...");
			}
			veriflyBaseWrapper = veriflyManager.resetPIN(veriflyBaseWrapper);
		}
		catch (Exception ex)
		{
			if(ex instanceof IOException)
			{
				veriflyBaseWrapper.setErrorMessage(FailureReasonsConstantsInterface.CONNECTION_RESET);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE);
			}
			else if(ex instanceof RemoteAccessException)
			{
				throw new RemoteAccessException( WorkFlowErrorCodeConstants.VERIFLY_INACTIVE );
			}
			logger.error("Exception occured during resetPIN..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1 || ex instanceof RemoteAccessException)
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
				throw ex;
			}
		}
		finally
		{
			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending resetPIN method of VeriflyManagerImpl...");
		}

		return veriflyBaseWrapper;

	}

	

	/**
	 * deleteAccount
	 *
	 * @param veriflyBaseWrapper VeriflyBaseWrapper
	 * @return VeriflyBaseWrapper
	 * @throws Exception
	 */
	public VeriflyBaseWrapper changeAccountNick(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside changeAccountNick method of VeriflyManagerImpl...");
		}
		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);

		try
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Going to hit Verifly to execute changeAccountNick...");
			}
			veriflyBaseWrapper = veriflyManager.changeAccountNick(veriflyBaseWrapper);
		}
		catch (Exception ex)
		{
			if(ex instanceof IOException)
			{
				veriflyBaseWrapper.setErrorMessage(FailureReasonsConstantsInterface.CONNECTION_RESET);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE);
			}
			else if(ex instanceof RemoteAccessException)
			{
				throw new RemoteAccessException( WorkFlowErrorCodeConstants.VERIFLY_INACTIVE );
			}
			logger.error("Exception occured during changeAccountNick..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1 || ex instanceof RemoteAccessException)
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			{
				throw ex;
			}
		}
		finally
		{
			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending changeAccountNick method of VeriflyManagerImpl...");
		}

		return veriflyBaseWrapper;

	}

	public void setVeriflyManager(VeriflyManager veriflyManager)
	{
		this.veriflyManager = veriflyManager;
	}

	public void setFailureLogManager(FailureLogManager failureLogManager)
	{
		this.failureLogManager = failureLogManager;
	}

	public void setXstream(XStream xstream)
	{
		this.xstream = xstream;
	}

	public VeriflyBaseWrapper generateOneTimePin(VeriflyBaseWrapper veriflyBaseWrapper) throws Exception

	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside generateOneTimePin method of VeriflyManagerImpl...");
		}

		AuditLogModel auditLogModel = auditLogBeforeCall(veriflyBaseWrapper);

		try

		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Hitting Verifly to execute generateOneTimePin...");
			}
			veriflyBaseWrapper = veriflyManager.generateOneTimePin(veriflyBaseWrapper);

		}

		catch (Exception ex)

		{
			
			logger.error("Exception occured during generateOneTimePin..."+ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
			if (ex.getCause().toString().indexOf("MalformedURLException") != -1

			|| ex instanceof RemoteAccessException)
			{
				throw new Exception(WorkFlowErrorCodeConstants.VERIFLY_INACTIVE, ex);
			}

			throw ex;

		}

		finally

		{

			auditLogAfterCall(auditLogModel, veriflyBaseWrapper);

		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending generateOneTimePin method of VeriflyManagerImpl...");
		}

		return veriflyBaseWrapper;

	}

	@Override
	public String getAccountNumber(Long customerId) throws Exception {
	
		return veriflyManager.getAccountNumber(customerId);
	}

	@Override
	public AccountInfoModel getAccountInfoModel(Long customerId, Long paymentModeId) throws Exception {
		
		return veriflyManager.getAccountInfoModel(customerId, paymentModeId);
	}

	@Override
	public AccountInfoModel getAccountInfoModel(Long customerId, String accountNick) throws Exception {

		return veriflyManager.getAccountInfoModel(customerId, accountNick);
	}

	
}
