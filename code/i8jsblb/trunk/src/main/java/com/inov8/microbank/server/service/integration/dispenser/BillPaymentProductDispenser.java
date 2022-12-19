package com.inov8.microbank.server.service.integration.dispenser;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.integration.vo.BookMeTransactionVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionReportModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacade;
import com.inov8.microbank.server.messaging.listener.PhoenixBillPaymentListener;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.switchmodule.iris.SwitchController;

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

public abstract class BillPaymentProductDispenser implements ProductDispenser {
	protected static Log logger = LogFactory.getLog(PhoenixBillPaymentListener.class);
	private FailureLogManager auditLogModule;
	StakeholderBankInfoManager stakeholderBankInfoManager;
	AbstractFinancialInstitution phoenixFinancialInstitution;

	private PostedTransactionReportFacade postedTransactionReportFacade;
	protected FinancialIntegrationManager financialIntegrationManager;
	protected SupplierBankInfoManager supplierBankInfoManager;

	public abstract WorkFlowWrapper getBillInfo(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException;

	public BaseWrapper createOrUpdatePostedTransactionBeforeCall(SwitchWrapper switchWrapper, Long intgTransactionType) throws FrameworkCheckedException {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		try {
			PostedTransactionReportModel postedTransactionReportModel = new PostedTransactionReportModel();
			TransactionCodeModel transactionCodeModel = switchWrapper.getWorkFlowWrapper().getTransactionCodeModel();
			if (transactionCodeModel != null) {
				postedTransactionReportModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
			}


			ProductModel productModel = switchWrapper.getWorkFlowWrapper().getProductModel();
			if (productModel != null) {
				postedTransactionReportModel.setProductId(productModel.getProductId());
			}

			postedTransactionReportModel.setIntgTransactionTypeId(intgTransactionType);
			postedTransactionReportModel.setFromAccount(switchWrapper.getFromAccountNo());
			postedTransactionReportModel.setConsumerNo(switchWrapper.getConsumerNumber());


			postedTransactionReportModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
			postedTransactionReportModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
			//postedTransactionReportModel.setCreatedByAppUserModel(ThreadLocalAppUser.getAppUserModel());
			//postedTransactionReportModel.setUpdatedByAppUserModel(ThreadLocalAppUser.getAppUserModel());
			postedTransactionReportModel.setCreatedOn(new Date());
			postedTransactionReportModel.setUpdatedOn(new Date());

			baseWrapper.setBasePersistableModel(postedTransactionReportModel);
			baseWrapper = postedTransactionReportFacade.createOrUpdatePostedTransactionRequiresNewTransaction(baseWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseWrapper;
	}

	public BaseWrapper createOrUpdatePostedTransactionAfterCall(SwitchWrapper switchWrapper, BaseWrapper baseWrapper) throws FrameworkCheckedException {
		PostedTransactionReportModel postedTransactionReportModel = (PostedTransactionReportModel) baseWrapper.getBasePersistableModel();

		if (switchWrapper.getMiddlewareIntegrationMessageVO() != null) {

			postedTransactionReportModel.setResponseCode(switchWrapper.getMiddlewareIntegrationMessageVO().getResponseCode());
			postedTransactionReportModel.setSystemTraceAuditNumber(switchWrapper.getMiddlewareIntegrationMessageVO().getStan());
			postedTransactionReportModel.setRefCode(switchWrapper.getMiddlewareIntegrationMessageVO().getRetrievalReferenceNumber());
		}

		if (switchWrapper.getAmountPaid() != null && !switchWrapper.getAmountPaid().trim().equals("")) {
			Double amount = Double.parseDouble(switchWrapper.getAmountPaid());
			postedTransactionReportModel.setAmount(amount);
		}

		postedTransactionReportModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
		//postedTransactionReportModel.setUpdatedBy(ThreadLocalAppUser.getAppUserModel().getAppUserId());
		postedTransactionReportModel.setUpdatedOn(new Date());

		baseWrapper.setBasePersistableModel(postedTransactionReportModel);

		baseWrapper = postedTransactionReportFacade.createOrUpdatePostedTransactionRequiresNewTransaction(baseWrapper);

		if (switchWrapper.getWorkFlowWrapper() != null && switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel() != null) {

			TransactionDetailMasterModel transactionDetailMasterModel = switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel();

			if (transactionDetailMasterModel.getBillInquiryRrn() == null) {

				transactionDetailMasterModel.setBillInquiryRrn(postedTransactionReportModel.getRefCode());

			} else if (postedTransactionReportModel.getRefCode() != null) {

				transactionDetailMasterModel.setBillInquiryRrn(transactionDetailMasterModel.getBillInquiryRrn() + "," + postedTransactionReportModel.getRefCode());
			}

			switchWrapper.getWorkFlowWrapper().setTransactionDetailMasterModel(transactionDetailMasterModel);
		}

		return baseWrapper;
	}


	public void prepairSwitchWrapper(Long productId, SwitchWrapper switchWrapper) throws FrameworkCheckedException {

		switchWrapper.setAgentBalance(switchWrapper.getAgentBalance());

		ProductModel productModel = switchWrapper.getWorkFlowWrapper().getProductModel();

		switchWrapper.setUtilityCompanyId(productModel.getProductCode());
		switchWrapper.setUtilityCompanyCategoryId(productModel.getCategoryCode());

		if (BookMeProductEnum.contains(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId().toString())) {
			BookMeTransactionVO bookMeTransactionVO = (BookMeTransactionVO) switchWrapper.getWorkFlowWrapper().getProductVO();
		} else {
			UtilityBillVO utilityBillVO = (UtilityBillVO) switchWrapper.getWorkFlowWrapper().getProductVO();

			switchWrapper.setConsumerNumber(utilityBillVO.getConsumerNo());


			//For Pool Accounts get the pool account
			if (switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel() != null) {
				switchWrapper.getWorkFlowWrapper().getTransactionDetailMasterModel().setBillDueDate(utilityBillVO.getDueDate());
			}
		}
    }
    
    
    protected WorkFlowWrapper doBillPaymentWithoutQueue(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        
        SwitchWrapper switchWrapper = workFlowWrapper.getSwitchWrapper();
        switchWrapper.setWorkFlowWrapper(workFlowWrapper);
        switchWrapper.setBasePersistableModel(workFlowWrapper.getOlaSmartMoneyAccountModel());
        switchWrapper.setSkipAccountInfoLoading(workFlowWrapper.getSwitchWrapper().getSkipAccountInfoLoading());
        switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        switchWrapper.setAccountInfoModel(workFlowWrapper.getAccountInfoModel());
        
        this.prepairSwitchWrapper(workFlowWrapper.getProductModel().getProductId(), switchWrapper);
		                   
        TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
        
        try {
            
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            
            baseWrapper.setBasePersistableModel(switchWrapper.getWorkFlowWrapper().getOlaSmartMoneyAccountModel());    
            
            AbstractFinancialInstitution olaFinancialInstitution = financialIntegrationManager.loadFinancialInstitution(baseWrapper);
            
            if(workFlowWrapper.getIsCustomerInitiatedTransaction() == true){
            	  olaFinancialInstitution.customerBillPayment(switchWrapper); 
            }else{
            	  olaFinancialInstitution.billPayment(switchWrapper); 
            }
            
            switchWrapper.getAccountInfoModel().setOldPin(switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
            
            switchWrapper.getWorkFlowWrapper().getTransactionModel().setTransactionAmount(switchWrapper.getWorkFlowWrapper().getBillAmount());
            
        } catch(Exception ex) {

        	logger.error(ex.getLocalizedMessage());
            if(ex instanceof CommandException) 
                throw new WorkFlowException(ex.getLocalizedMessage());
            else if (ex instanceof WorkFlowException)
                throw (WorkFlowException) ex;
            else if (ex instanceof IOException)
                throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
            else if (ex instanceof RemoteAccessException)
                throw new WorkFlowException(MessageUtil.getMessage("genericErrorMessage"));
            else
                throw new WorkFlowException(WorkFlowErrorCodeConstants.PHOENIX_SERVICE_DOWN_MSG);
        }
        
        switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
        
        workFlowWrapper.setSwitchWrapper(switchWrapper);
        
        return workFlowWrapper;
    }

	
	protected PaymentServicesIntgModule getKasbSwitch(String url)
	{
		return HttpInvokerUtil.getHttpInvokerFactoryBean(PaymentServicesIntgModule.class, url);
	}
	
	public AuditLogModel auditLogBeforeCall(WorkFlowWrapper workFlowWrapper, String inputParam) throws WorkFlowException
	{
		AuditLogModel auditLogModel = new AuditLogModel();
		auditLogModel.setTransactionStartTime(new Timestamp(System.currentTimeMillis()));
		auditLogModel.setIntegrationModuleId(IntegrationModuleConstants.SUPPLIER_MODULE);

		if( workFlowWrapper.getTransactionModel() != null )
			auditLogModel.setTransactionCodeId(workFlowWrapper.getTransactionModel().getTransactionCodeId());

		auditLogModel.setInputParam(XMLUtil.replaceElementsUsingXPath(inputParam, XPathConstants.kasbBillPaymentDispenserAuditLogInputParamLocationSteps));
		
		auditLogModel.setActionLogId(ThreadLocalActionLog.getActionLogId());
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(auditLogModel);
		try
		{
			auditLogModel = (AuditLogModel) this.auditLogModule.auditLogRequiresNewTransaction(baseWrapper).getBasePersistableModel();
		}
		catch (FrameworkCheckedException ex)
		{
			ex.printStackTrace();
			throw new WorkFlowException(ex.getMessage(), ex);
		}

		return auditLogModel;
	}
	
	public void auditLogAfterCall(AuditLogModel auditLogModel, String outputParam) throws WorkFlowException
	{
		auditLogModel.setTransactionEndTime(new Timestamp(System.currentTimeMillis()));
		BaseWrapper baseWrapper = new BaseWrapperImpl();

		auditLogModel.setOutputParam(outputParam);
		baseWrapper.setBasePersistableModel(auditLogModel);

		try
		{
			this.auditLogModule.auditLogRequiresNewTransaction(baseWrapper);
		}
		catch (FrameworkCheckedException ex)
		{
			throw new WorkFlowException(ex.getMessage(), ex);
		}
	}

	public void setAuditLogModule(FailureLogManager auditLogModule)
	{
		this.auditLogModule = auditLogModule;
	}

	public void setStakeholderBankInfoManager(
			StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}

	public void setPhoenixFinancialInstitution(
			AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}

	public void setPostedTransactionReportFacade( PostedTransactionReportFacade postedTransactionReportFacade) {
		this.postedTransactionReportFacade = postedTransactionReportFacade;
	}	
	
	public void setSupplierBankInfoManager(SupplierBankInfoManager supplierBankInfoManager) {
		this.supplierBankInfoManager = supplierBankInfoManager;
	}
	

}
