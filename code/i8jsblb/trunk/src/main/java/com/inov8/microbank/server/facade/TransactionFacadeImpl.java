package com.inov8.microbank.server.facade;

import java.util.Collection;
import java.util.List;

import com.inov8.microbank.common.model.MiniTransactionModel;
import org.springframework.dao.DataIntegrityViolationException;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TransactionFacadeImpl implements TransactionFacade
{

	private TransactionModuleManager transactionModuleManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public TransactionFacadeImpl()
	{
	}

	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setTransactionModuleManager(TransactionModuleManager transactionModuleManager)
	{
		this.transactionModuleManager = transactionModuleManager;
	}

	public WorkFlowWrapper generateTransactionCodeRequiresNewTransaction(WorkFlowWrapper workFlowWrapper)
			throws FrameworkCheckedException
	{
		boolean isRecordSaved = false;

		try
		{
			while (!isRecordSaved)
			{
				try
				{
					workFlowWrapper = transactionModuleManager
							.generateTransactionCodeRequiresNewTransaction(workFlowWrapper);
					isRecordSaved = true;
				}
				catch (Exception ex)
				{
					if (!(ex instanceof DataIntegrityViolationException))
					{
						throw ex;
					}
				}
			}
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}

		return workFlowWrapper;
	}

	public WorkFlowWrapper generateTransactionObject(WorkFlowWrapper workFlowWrapper)
			throws FrameworkCheckedException
	{
		try
		{
			workFlowWrapper = transactionModuleManager.generateTransactionObject(workFlowWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
		return workFlowWrapper;
	}

	public void saveTransaction(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.transactionModuleManager.saveTransaction(workFlowWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public void updateTransactionCode(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.transactionModuleManager.updateTransactionCode(workFlowWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public BaseWrapper updateTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return this.transactionModuleManager.updateTransaction(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public SearchBaseWrapper loadTransaction(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException
	{
		try
		{
			return this.transactionModuleManager.loadTransaction(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public BaseWrapper loadTransactionCode(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return this.transactionModuleManager.loadTransactionCode(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public BaseWrapper updateTransactionCode(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return this.transactionModuleManager.updateTransactionCode(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	public SearchBaseWrapper loadTransactionByTransactionCode(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return this.transactionModuleManager.loadTransactionByTransactionCode(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public BaseWrapper saveTransactionModel(BaseWrapper wrapper) throws FrameworkCheckedException
	{
		try
		{
			return this.transactionModuleManager.saveTransactionModel(wrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public BaseWrapper failTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return this.transactionModuleManager.failTransaction(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	public void transactionRequiresNewTransaction(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.transactionModuleManager.transactionRequiresNewTransaction(workFlowWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}

	}

	@Override
	public void bookMeTransactionRequiresNewTransaction(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		try
		{
			this.transactionModuleManager.bookMeTransactionRequiresNewTransaction(workFlowWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
	}

	public BaseWrapper loadTransactionCodeByCode(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try
		{
			baseWrapper = transactionModuleManager.loadTransactionCodeByCode(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
		return baseWrapper;

	}

	public BaseWrapper saveTransactionSummaryModel(BaseWrapper wrapper) throws FrameworkCheckedException
	{
		try
		{
			return this.transactionModuleManager.saveTransactionSummaryModel(wrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	@Override
	public void saveUpdateAll(Collection<TransactionModel> collection) {
		transactionModuleManager.saveUpdateAll(collection);
	}

	@Override
	public List<Object[]> getDonationTransactionList(Long trnsactionType,Long supProcessingStatusId, Long serviceId) throws FrameworkCheckedException {

		return transactionModuleManager.getDonationTransactionList(trnsactionType, supProcessingStatusId, serviceId);
	}

	@Override
	public List getTransactionsByCriteria(Long distributorId, Long productId, Boolean isSettled, Boolean isPosted) {

		return transactionModuleManager.getTransactionsByCriteria(distributorId, productId, isSettled, isPosted);
	}

	@Override
	public Integer updateTransactionProcessingStatus(Long transactionProcessingStatus, List<Long> transactionId) {

		return transactionModuleManager.updateTransactionProcessingStatus(transactionProcessingStatus, transactionId);
	}

	public BaseWrapper saveMiniTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException {

		// TODO Auto-generated method stub
		try
		{
			baseWrapper = transactionModuleManager.saveMiniTransaction(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
		return baseWrapper;
	}

	@Override
	public BaseWrapper updateP2PTxDetails(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			baseWrapper = transactionModuleManager.updateP2PTxDetails(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
		return baseWrapper;
	}

/*	@Override
	public BaseWrapper updateP2PTxDetailsActionAuth(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try {
			baseWrapper = transactionModuleManager.updateP2PTxDetailsActionAuth(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
		return baseWrapper;
	}*/

	@Override
	public SearchBaseWrapper loadP2PUpdateHistory(SearchBaseWrapper wrapper)
			throws FrameworkCheckedException {
		try {
			wrapper = transactionModuleManager.loadP2PUpdateHistory(wrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
		return wrapper;
	}

	@Override
	public void makeWalkinCustomer(String walkInCNIC, String walkInMobileNumber)
			throws FrameworkCheckedException {
		try {
			transactionModuleManager.makeWalkinCustomer(walkInCNIC, walkInMobileNumber);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
		}
	}

	@Override
	public WorkFlowWrapper createTransactionModel(WorkFlowWrapper wrapper) throws FrameworkCheckedException {
		return transactionModuleManager.createTransactionModel(wrapper);
	}

	@Override
	public MiniTransactionModel loadMiniTransactionModelByTransactionCode(String transactionCode) {
		return transactionModuleManager.loadMiniTransactionModelByTransactionCode(transactionCode);
	}

	@Override
	public MiniTransactionModel updateMiniTransactionRequiresNewTransaction(MiniTransactionModel miniTransactionModel) throws FrameworkCheckedException {
		return transactionModuleManager.updateMiniTransactionRequiresNewTransaction(miniTransactionModel);
	}

	@Override
	public BaseWrapper loadAndLockMiniTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		return transactionModuleManager.loadAndLockMiniTransaction(baseWrapper);
	}

	@Override
	public BaseWrapper updateMiniTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		return transactionModuleManager.updateMiniTransaction(baseWrapper);
	}

}
