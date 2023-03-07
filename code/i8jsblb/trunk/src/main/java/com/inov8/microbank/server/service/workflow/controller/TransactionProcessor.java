package com.inov8.microbank.server.service.workflow.controller;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.authenticationmethod.service.OTPAuthentication;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.ivr.IvrRequestHandler;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.statuscheckmodule.StatusCheckManager;
import com.inov8.microbank.server.service.tillbalancemodule.TillBalanceManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import java.util.Date;

public abstract class TransactionProcessor
        extends TransactionControllerImpl {
    protected TransactionModuleManager txManager;
    protected StatusCheckManager statusCheck;
    protected SmsSender smsSender;
    protected TillBalanceManager tillBalanceManager;
    protected AppUserManager appUserManager;
    protected TransactionDetailMasterManager transactionDetailMasterManager;
    protected SettlementManager settlementManager;
    protected IvrRequestHandler ivrRequestHandler;
    protected IvrAuthenticationRequestQueue ivrAuthenticationRequestQueueSender;
    protected OTPAuthentication otpAuthentication;

    /**
     * <p>This method calls preceeds the call to the start method for any transaction</p>
     *
     * @param wrapper
     * @return
     */

    private void saveThirdPartyParameters(WorkFlowWrapper wrapper) {
        logger.info("******************* Start of TransactionProcessor.saveThirdPartyParameters() ************************");
        if (wrapper.getDeviceTypeModel() != null && wrapper.getDeviceTypeModel().getDeviceTypeId() != null
                && (wrapper.getDeviceTypeModel().getDeviceTypeId().equals(DeviceTypeConstantsInterface.WEB_SERVICE) || wrapper.getDeviceTypeModel().getDeviceTypeId().equals(DeviceTypeConstantsInterface.ATM))) {
            if (wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID) != null)
                wrapper.getTransactionDetailMasterModel().setTerminalId(wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID).toString());
            if (wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID) != null)
                wrapper.getTransactionDetailMasterModel().setChannelId(wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).toString());
            if (wrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE) != null) {
                wrapper.getTransactionDetailMasterModel().setFonepayTransactionCode((String) wrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE));
            }
            if (wrapper.getObject(CommandFieldConstants.KEY_STAN) != null) {
                wrapper.getTransactionDetailMasterModel().setStan((String) wrapper.getObject(CommandFieldConstants.KEY_STAN));
            }
            if (wrapper.getObject(FonePayConstants.KEY_EXTERNAL_PRODUCT_NAME) != null) {
                wrapper.getTransactionDetailMasterModel().setExternalProductName((String) wrapper.getObject(FonePayConstants.KEY_EXTERNAL_PRODUCT_NAME));
            }
            if (wrapper.getObject(CommandFieldConstants.KEY_PROFIT_KEY) != null) {
                wrapper.getTransactionDetailMasterModel().setReserved3((String) wrapper.getObject(CommandFieldConstants.KEY_PROFIT_KEY));
            }
            if (wrapper.getObject(CommandFieldConstants.KEY_RESERVED_4) != null) {
                wrapper.getTransactionDetailMasterModel().setReserved4((String) wrapper.getObject(CommandFieldConstants.KEY_RESERVED_4));
            }
            if (wrapper.getObject(CommandFieldConstants.KEY_RESERVED_5) != null) {
                wrapper.getTransactionDetailMasterModel().setReserved5((String) wrapper.getObject(CommandFieldConstants.KEY_RESERVED_5));
            }
            if (wrapper.getObject(CommandFieldConstants.KEY_RESERVED_8) != null) {
                wrapper.getTransactionDetailMasterModel().setReserved8((String) wrapper.getObject(CommandFieldConstants.KEY_RESERVED_8));
            }
            if (wrapper.getObject(CommandFieldConstants.KEY_RESERVED_9) != null) {
                wrapper.getTransactionDetailMasterModel().setReserved9((String) wrapper.getObject(CommandFieldConstants.KEY_RESERVED_9));
            }
            if (wrapper.getObject(CommandFieldConstants.KEY_RESERVED_10) != null) {
                wrapper.getTransactionDetailMasterModel().setReserved10((String) wrapper.getObject(CommandFieldConstants.KEY_RESERVED_10));
            }


        }
        logger.info("******************* End of TransactionProcessor.saveThirdPartyParameters() ************************");
    }

    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of TransactionProcessor.");
            logger.debug("Going to execute txManager.generateTransactionCodeRequiresNewTransaction(wrapper) to generate a new transaction code.");
        }

        // sends a test message to Active MQ before the start of transaction to test the health of the Queue
        statusCheck.checkActiveMQStatus();

        //wrapper.putObject("IS_ACTIVE_MQ_UP","1");

        if (null == wrapper.getTransactionDetailMasterModel()) {//initialize model.
            wrapper.setTransactionDetailMasterModel(new TransactionDetailMasterModel(true));
        }

        //Going to check if transaction code is already generated in case of USSD - Maqsood Shahzad
        if (null == wrapper.getTransactionCodeModel()) {
            wrapper = txManager.generateTransactionCodeRequiresNewTransaction(wrapper); //generate transaction code against the current transaction
        }
        //	  wrapper.setFinancialTransactionsMileStones(new FinancialTransactionsMileStones());
        if (logger.isDebugEnabled()) {
            logger.debug("Going to check if all the integration modules are alive.");
        }
        SearchBaseWrapper searchBaseWrapper = statusCheck.getIMStatus(wrapper);
        if (statusCheck.isAllIntegrationModulesAlive(searchBaseWrapper, wrapper)) {
            wrapper = txManager.generateTransactionObject(wrapper);
        } else {
            throw new Exception("One or more of the external systems are down.");
        }

        /**
         * Populate TransactionDetailMaster (as previously done in trigger TX_DT_MASTER_TX_CODE_TRIGGER)
         */

        TransactionDetailMasterModel transactionDetailMasterModel = wrapper.getTransactionDetailMasterModel();
        transactionDetailMasterModel.setTransactionCodeId(wrapper.getTransactionCodeModel().getTransactionCodeId());
        transactionDetailMasterModel.setTransactionCode(wrapper.getTransactionCodeModel().getCode());
        transactionDetailMasterModel.setCreatedOn(wrapper.getTransactionCodeModel().getCreatedOn());
        transactionDetailMasterModel.setUpdatedOn(wrapper.getTransactionCodeModel().getUpdatedOn());
        transactionDetailMasterModel.setProcessingStatusName("Failed");
        transactionDetailMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
        transactionDetailMasterModel.setProductId(wrapper.getProductModel().getProductId());
        transactionDetailMasterModel.setProductName(wrapper.getProductModel().getName());
        if (wrapper.getMemberBankModel() != null) {
            transactionDetailMasterModel.setToBankImd(wrapper.getMemberBankModel().getBankImd());
        }
        if (wrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE) != null) {
            transactionDetailMasterModel.setRecipientAccountTitle(String.valueOf(wrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE)));
        }
        transactionDetailMasterModel.setnIfq((String) wrapper.getObject(CommandFieldConstants.KEY_NADRA_NIFQ));
        transactionDetailMasterModel.setMintaieCount((String) wrapper.getObject(CommandFieldConstants.KEY_NADRA_MINUTIAE_COUNT));

        if (wrapper.getObject(CommandFieldConstants.KEY_MAC_ADDRESS) != null) {
            transactionDetailMasterModel.setMacAddress((String) wrapper.getObject(CommandFieldConstants.KEY_MAC_ADDRESS));
        }

        if (wrapper.getObject(CommandFieldConstants.KEY_IP_ADDRESS) != null) {
            transactionDetailMasterModel.setIpAddress((String) wrapper.getObject(CommandFieldConstants.KEY_IP_ADDRESS));
        }

        if (wrapper.getObject(CommandFieldConstants.KEY_LONGITUDE) != null) {
            transactionDetailMasterModel.setLongitude((String) wrapper.getObject(CommandFieldConstants.KEY_LONGITUDE));
        }

        if (wrapper.getObject(CommandFieldConstants.KEY_LATITUDE) != null) {
            transactionDetailMasterModel.setLatitude((String) wrapper.getObject(CommandFieldConstants.KEY_LATITUDE));
        }

        if (wrapper.getObject(CommandFieldConstants.KEY_MOBILE_IMEI_NUMBER) != null) {
            transactionDetailMasterModel.setImeiNumber((String) wrapper.getObject(CommandFieldConstants.KEY_MOBILE_IMEI_NUMBER));
        }

        TransactionPurposeModel transactionPurposeModel = null;
        if (wrapper.getObject("TRANS_PURPOSE_MODEL") != null)
            transactionPurposeModel = (TransactionPurposeModel) wrapper.getObject("TRANS_PURPOSE_MODEL");
        if (transactionPurposeModel != null)
            wrapper.getTransactionDetailMasterModel().setTransactionPurposeId(transactionPurposeModel.getTransactionPurposeId());

        ServiceModel serviceModel = wrapper.getProductModel().getServiceIdServiceModel();
        if (serviceModel != null) {
            transactionDetailMasterModel.setServiceId(serviceModel.getServiceId());
            transactionDetailMasterModel.setServiceName(serviceModel.getName());
        }

        //agent1 Id set for 1st leg (to be displayed in case of trx Failure)
        // note that agent1 Id is override in doPreEnd
        if (!wrapper.getIsCustomerInitiatedTransaction() && ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel() != null && ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId() != null) {
            transactionDetailMasterModel.setAgent1Id(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
        }

        this.saveThirdPartyParameters(wrapper);

        if (logger.isDebugEnabled()) {
            logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of TransactionProcessor");
        }
        return wrapper;

    }


    /**
     * <p>This method follows the call to the start method for any transaction.</p>
     *
     * @param wrapper
     * @return
     */
    protected WorkFlowWrapper doPostStart(WorkFlowWrapper wrapper) {
        //will have Implementation
        return wrapper;
    }

    protected WorkFlowWrapper doStart(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }


    /**
     * <p>This method preceeds the call to the End method for any transaction</p>
     *
     * @param wrapper
     * @return
     */
    protected WorkFlowWrapper doPreEnd(WorkFlowWrapper wrapper) throws Exception {
        updateTransactionDetailMasterForTransaction(wrapper);

        Boolean isIvrResp = wrapper.getIsIvrResponse();

        if ((isIvrResp == null || !isIvrResp) && wrapper.getTransactionModel().getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.IVR_VALIDATION_PENDING.longValue()) {
            return wrapper;
        }

        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
	/*FIXME TODO
	if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER)
	{
		//RSO agent has no opening till balance. no need to make updates here.
		if(wrapper.getFromRetailerContactModel() != null && wrapper.getFromRetailerContactModel().getRso()){

			return wrapper;
		}

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		UserDeviceAccountsModel deviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		AgentOpeningBalModel agentOpeningBalModel = new AgentOpeningBalModel();
		agentOpeningBalModel.setAgentId(deviceAccountsModel.getUserId());
		agentOpeningBalModel.setBalDate(calendar.getTime());
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(agentOpeningBalModel);
		searchBaseWrapper  = tillBalanceManager.searchAgentOpeningBalanceByExample(searchBaseWrapper);
		if(null != searchBaseWrapper.getCustomList() && null != searchBaseWrapper.getCustomList().getResultsetList() && searchBaseWrapper.getCustomList().getResultsetList().size() > 0)
		{
			agentOpeningBalModel = (AgentOpeningBalModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
		}
		else
		{
			throw new WorkFlowException("Opening till balance not found");
		}
		CashBankMappingModel cashBankMappingModel = new CashBankMappingModel();
		cashBankMappingModel.setAgentOpeningBalanceId(agentOpeningBalModel.getAgentOpeningBalanceId());
		cashBankMappingModel.setTransactionId(wrapper.getTransactionModel().getTransactionId());
		cashBankMappingModel.setProductId(wrapper.getProductModel().getProductId());
		SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();

		if(wrapper.getProductModel().getProductId().longValue() == 50006L)
		{
			cashBankMappingModel.setTillDebitAmount(wrapper.getTransactionModel().getTransactionAmount());
			cashBankMappingModel.setBankCreditAmount(wrapper.getTransactionModel().getTransactionAmount());
			cashBankMappingModel.setTillBalAfterTx(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningTillBalance() - wrapper.getTransactionModel().getTransactionAmount())));
			cashBankMappingModel.setBankBalAfterTx(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningAccountBalance() + cashBankMappingModel.getBankCreditAmount())));
			agentOpeningBalModel.setRunningAccountBalance(cashBankMappingModel.getBankBalAfterTx());
			agentOpeningBalModel.setRunningTillBalance(cashBankMappingModel.getTillBalAfterTx());
		}
		else if(wrapper.getProductModel().getProductId().longValue() == 50002L)
		{
			agentOpeningBalModel.setRunningTillBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningTillBalance() + wrapper.getTransactionModel().getTotalAmount())));
			cashBankMappingModel.setTillCreditAmount(wrapper.getTransactionModel().getTotalAmount());
			cashBankMappingModel.setBankDebitAmount(wrapper.getTransactionModel().getTotalAmount());
			agentOpeningBalModel.setRunningAccountBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningAccountBalance() - cashBankMappingModel.getBankDebitAmount())));
			cashBankMappingModel.setTillBalAfterTx(agentOpeningBalModel.getRunningTillBalance());
			cashBankMappingModel.setBankBalAfterTx(agentOpeningBalModel.getRunningAccountBalance());
		}
		else if(UtilityCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) || InternetCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())))
		{
			agentOpeningBalModel.setRunningTillBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningTillBalance() + wrapper.getTransactionModel().getTransactionAmount())));
			cashBankMappingModel.setTillCreditAmount(wrapper.getTransactionModel().getTransactionAmount());
			cashBankMappingModel.setBankDebitAmount(wrapper.getTransactionModel().getTransactionAmount());
			agentOpeningBalModel.setRunningAccountBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningAccountBalance() - cashBankMappingModel.getBankDebitAmount())));
			cashBankMappingModel.setTillBalAfterTx(agentOpeningBalModel.getRunningTillBalance());
			cashBankMappingModel.setBankBalAfterTx(agentOpeningBalModel.getRunningAccountBalance());
		}
		else if(wrapper.getProductModel().getProductId().longValue() == 50011L && null == wrapper.getTransactionModel().getToRetContactId() && null != wrapper.getTransactionModel().getFromRetContactId() && wrapper.getTransactionModel().getFromRetContactMobNo().equals(ThreadLocalAppUser.getAppUserModel().getMobileNo()))
		{
			agentOpeningBalModel.setRunningTillBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningTillBalance() + wrapper.getTransactionModel().getTotalAmount())));
			cashBankMappingModel.setTillCreditAmount(wrapper.getTransactionModel().getTotalAmount());
			cashBankMappingModel.setBankDebitAmount(wrapper.getTransactionModel().getTotalAmount());
			agentOpeningBalModel.setRunningAccountBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningAccountBalance() - cashBankMappingModel.getBankDebitAmount())));
			cashBankMappingModel.setTillBalAfterTx(agentOpeningBalModel.getRunningTillBalance());
			cashBankMappingModel.setBankBalAfterTx(agentOpeningBalModel.getRunningAccountBalance());
		}
		else if(wrapper.getProductModel().getProductId().longValue() == 50011L && null != wrapper.getTransactionModel().getToRetContactId() && wrapper.getTransactionModel().getToRetContactMobNo().equals(ThreadLocalAppUser.getAppUserModel().getMobileNo()))
		{
			agentOpeningBalModel.setRunningTillBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningTillBalance() - wrapper.getTransactionModel().getTransactionAmount())));
			cashBankMappingModel.setTillDebitAmount(wrapper.getTransactionModel().getTransactionAmount());
			cashBankMappingModel.setBankCreditAmount(wrapper.getTransactionModel().getTransactionAmount());
			agentOpeningBalModel.setRunningAccountBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningAccountBalance() + cashBankMappingModel.getBankCreditAmount())));
			cashBankMappingModel.setTillBalAfterTx(agentOpeningBalModel.getRunningTillBalance());
			cashBankMappingModel.setBankBalAfterTx(agentOpeningBalModel.getRunningAccountBalance());
		}
		else if(wrapper.getProductModel().getProductId().longValue() == 50010L && null != wrapper.getTransactionModel().getToRetContactId() && wrapper.getTransactionModel().getToRetContactMobNo().equals(ThreadLocalAppUser.getAppUserModel().getMobileNo()))
		{
			agentOpeningBalModel.setRunningTillBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningTillBalance() - wrapper.getTransactionModel().getTransactionAmount())));
			cashBankMappingModel.setTillDebitAmount(wrapper.getTransactionModel().getTransactionAmount());
			cashBankMappingModel.setBankCreditAmount(wrapper.getTransactionModel().getTransactionAmount());
			agentOpeningBalModel.setRunningAccountBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningAccountBalance() + cashBankMappingModel.getBankCreditAmount())));
			cashBankMappingModel.setTillBalAfterTx(agentOpeningBalModel.getRunningTillBalance());
			cashBankMappingModel.setBankBalAfterTx(agentOpeningBalModel.getRunningAccountBalance());
		}

		else if(wrapper.getProductModel().getServiceId() == 7L)
		{
			agentOpeningBalModel.setRunningTillBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningTillBalance() + wrapper.getTransactionModel().getTotalAmount())));
			cashBankMappingModel.setTillCreditAmount(wrapper.getTransactionModel().getTotalAmount());
			cashBankMappingModel.setBankDebitAmount(wrapper.getTransactionModel().getTotalAmount());
			agentOpeningBalModel.setRunningAccountBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningAccountBalance() - cashBankMappingModel.getBankDebitAmount())));
			cashBankMappingModel.setTillBalAfterTx(agentOpeningBalModel.getRunningTillBalance());
			cashBankMappingModel.setBankBalAfterTx(agentOpeningBalModel.getRunningAccountBalance());
		}

//		else if(wrapper.getProductModel().getProductId().longValue() == 50013L && null != wrapper.getTransactionModel().getFromRetContactId() && wrapper.getTransactionModel().getFromRetContactMobNo().equals(ThreadLocalAppUser.getAppUserModel().getMobileNo()))
//		{
////			agentOpeningBalModel.setRunningTillBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningTillBalance() - wrapper.getTransactionModel().getTransactionAmount())));
//			cashBankMappingModel.setTillDebitAmount(0D);
//			cashBankMappingModel.setBankDebitAmount(wrapper.getTransactionModel().getTransactionAmount());
//			agentOpeningBalModel.setRunningAccountBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningAccountBalance() - cashBankMappingModel.getBankDebitAmount())));
//			cashBankMappingModel.setTillBalAfterTx(agentOpeningBalModel.getRunningTillBalance());
//			cashBankMappingModel.setBankBalAfterTx(agentOpeningBalModel.getRunningAccountBalance());
//		}
//		else if(wrapper.getProductModel().getProductId().longValue() == 50013L && null != wrapper.getTransactionModel().getToRetContactId() && wrapper.getTransactionModel().getToRetContactMobNo().equals(ThreadLocalAppUser.getAppUserModel().getMobileNo()))
//		{
////			agentOpeningBalModel.setRunningTillBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningTillBalance() - wrapper.getTransactionModel().getTransactionAmount())));
//			cashBankMappingModel.setTillCreditAmount(0D);
//			cashBankMappingModel.setBankCreditAmount(wrapper.getTransactionModel().getTransactionAmount());
//			agentOpeningBalModel.setRunningAccountBalance(Double.parseDouble(Formatter.formatDouble(agentOpeningBalModel.getRunningAccountBalance() + cashBankMappingModel.getBankCreditAmount())));
//			cashBankMappingModel.setTillBalAfterTx(agentOpeningBalModel.getRunningTillBalance());
//			cashBankMappingModel.setBankBalAfterTx(agentOpeningBalModel.getRunningAccountBalance());
//		}

		agentOpeningBalModel.setUpdatedBy(appUserModel.getAppUserId());
		agentOpeningBalModel.setUpdatedOn(new Date());
		cashBankMappingModel.setCreatedBy(appUserModel.getAppUserId());
		cashBankMappingModel.setUpdatedBy(appUserModel.getAppUserId());
		cashBankMappingModel.setCreatedOn(new Date());
		cashBankMappingModel.setUpdatedOn(new Date());
		cashBankMappingModel.setTransactionDate(new Date());
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(agentOpeningBalModel);
		tillBalanceManager.updateAgentOpeningBalance(baseWrapper);
		baseWrapper.setBasePersistableModel(cashBankMappingModel);
		tillBalanceManager.updateCashBankMapping(baseWrapper);
	}
	*/

//*************** FT Settlement Scheduler changes - Start 
        //UNCOMMENTME after FT settlement entries are fixed in each transaction.
        if (wrapper.getTransactionModel().getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.COMPLETED.longValue()
                || wrapper.getTransactionModel().getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.IN_PROGRESS.longValue()
                || wrapper.getTransactionModel().getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.BILL_AUTHORIZATION_SENT.longValue()
                || wrapper.getTransactionModel().getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.REVERSE_COMPLETED.longValue()
                || wrapper.getTransactionModel().getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.PENDING_ACTION_AUTH.longValue()
        ) {
            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CASH_DEPOSIT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CASH_WITHDRAWAL ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.HRA_CASH_WITHDRAWAL ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CUSTOMER_CASH_WITHDRAWAL ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACT_TO_ACT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CASH_TRANSFER ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_BB_ACCOUNT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.BB_TO_CORE_ACCOUNT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CUSTOMER_BB_TO_CORE_ACCOUNT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.RELIEF_FUND_PRODUCT ||

                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACCOUNT_OPENING ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.AGENT_TO_AGENT_TRANSFER ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.RETAIL_PAYMENT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CUSTOMER_RETAIL_PAYMENT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.TRANSFER_IN ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.TRANSFER_OUT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.IBFT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.WEB_SERVICE_PAYMENT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.WEB_SERVICE_CASH_IN.longValue() ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.APIGEE_PAYMENT.longValue() ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.VIRTUAL_CARD_PAYMENT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.FONEPAY_AGENT_PAYMENT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.TELLER_ACCOUNT_HOLDER_CASH_IN.longValue() ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue() ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.TELLER_CASH_OUT.longValue() ||
                    NadraCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                    InternetCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                    UtilityCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                    OneBillProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                    BookMeProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                    OtherThanOneBillProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                    wrapper.getProductModel().getServiceId() == ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue() ||
                    wrapper.getProductModel().getServiceId() == ServiceConstantsInterface.BULK_DISB_ACC_HOLDER.longValue() ||
                    wrapper.getProductModel().getServiceId() == ServiceConstantsInterface.COLLECTION_PAYMENT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACT_TO_ACT_CI.longValue() ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACT_TO_CASH_CI.longValue() ||
                    wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_CASH_OUT_TX)
                    || wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING_TX)
                    || wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CARD_ISSUANCE_REISSUANCE_TX)
                    || wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.OFFLINE_BILL_PAYMENT_TX)
                    || wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.PROOF_OF_LIFE_TX)
                    || wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.DEBIT_CARD_CW_TX)
                    || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.AGENT_CASH_DEPOSIT)
                    || wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.DEBIT_CARD_CW_TX)
                    || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.AGENT_EXCISE_AND_TAXATION)
                    || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.BOP_CASH_OUT)
                    || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.BOP_CASH_OUT_COVID_19)
                    || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.BOP_CASH_OUT)
                    || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.AGENT_BB_TO_IBFT)
                    || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT)
                    || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.ADVANCE_SALARY_LOAN)
                    || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.ADVANCE_SALARY_LOAN_ID)
                    || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.HRA_TO_WALLET_TRANSACTION)
                    || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.CORE_TO_WALLET)
                    || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.VC_TRANSFER_PRODUCT)) {

                settlementManager.prepareDataForDayEndSettlement(wrapper);

            }

            // Middle-ware day end settlement - start

// Moved to MiddlewareSwitchImpl.sendCreditAdvice as credit is now queue based
// moved back now :)	26/06/2015 
            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.BB_TO_CORE_ACCOUNT ||
//				wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CUSTOMER_BB_TO_CORE_ACCOUNT ||
//				wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CUSTOMER_BB_TO_IBFT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.TRANSFER_OUT
                /*|| wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.AGENT_BB_TO_IBFT)*/) {

                prepareAndSaveSettlementTransactionRDV(wrapper.getTransactionModel().getTransactionId(),
                        wrapper.getProductModel().getProductId(),
                        wrapper.getMiddlewareSwitchWrapper().getTransactionAmount(),
                        PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,
                        PoolAccountConstantsInterface.OF_SETTLEMENT_IFT_POOL_ACCOUNT);
            }
//		
            if ((wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.TRANSFER_IN.longValue() ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.TELLER_WALK_IN_CASH_IN.longValue() ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.TELLER_ACCOUNT_HOLDER_CASH_IN.longValue())
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_CASH_OUT_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CARD_ISSUANCE_REISSUANCE_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.PROOF_OF_LIFE_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.ADVANCE_SALARY_LOAN_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CASH_OUT_TX)) {

                prepareAndSaveSettlementTransactionRDV(wrapper.getTransactionModel().getTransactionId(),
                        wrapper.getProductModel().getProductId(),
                        wrapper.getMiddlewareSwitchWrapper().getTransactionAmount(),
                        PoolAccountConstantsInterface.OF_SETTLEMENT_IFT_POOL_ACCOUNT,
                        PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID);
            }

// Moved to MiddlewareSwitchImpl.sendCreditAdvice as credit is now queue based
            // moved back now :)	26/06/2015
            if (NadraCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                    InternetCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                    UtilityCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))
                    || OtherThanOneBillProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))
                    || OneBillProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))) {
                Long utilityBillPoolAccountId = PoolAccountConstantsInterface.OF_SETTLEMENT_UBP_POOL_ACCOUNT;//change to of_set_ubp_pool_account
                if (OneBillProductEnum.contains(wrapper.getProductModel().getProductId().toString()))
                    utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_ONE_BILL_POOL_ACCOUNT_ID;
                prepareAndSaveSettlementTransactionRDV(wrapper.getTransactionModel().getTransactionId(),
                        wrapper.getProductModel().getProductId(),
                        wrapper.getMiddlewareSwitchWrapper().getTransactionAmount(),
                        PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,
                        utilityBillPoolAccountId);
            }

            OfflineBillersConfigModel offlineBillersConfigModel = getCommonCommandManager().loadOfflineBillersModelByProductId
                    (String.valueOf(wrapper.getProductModel().getProductId()));
            if (offlineBillersConfigModel != null) {
                Long utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_ONE_BILL_POOL_ACCOUNT_ID;
//below produduct id of HWASA is for Production 10245384
                if (offlineBillersConfigModel.getProductId().equals("10245384")) {
                    utilityBillPoolAccountId = PoolAccountConstantsInterface.OFFLINE_BILLER_POOL_ACCOUNT_ID;
                } else if (offlineBillersConfigModel.getProductId().equals("10245397")) {
                    utilityBillPoolAccountId = PoolAccountConstantsInterface.OFFLINE_BILLER_ASKARI_POOL_ACCOUNT_ID;

                }
                prepareAndSaveSettlementTransactionRDV(wrapper.getTransactionModel().getTransactionId(),
                        wrapper.getProductModel().getProductId(),
                        wrapper.getCommissionAmountsHolder().getTransactionAmount(),
                        PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,
                        utilityBillPoolAccountId);
            }

            if (BookMeProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))) {
                Long utilityBillPoolAccountId = PoolAccountConstantsInterface.BOOK_ME_GL;
                prepareAndSaveSettlementTransactionRDV(wrapper.getTransactionModel().getTransactionId(),
                        wrapper.getProductModel().getProductId(),
                        wrapper.getSwitchWrapper().getTransactionAmount(),
                        PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,
                        utilityBillPoolAccountId);
            }

            // Middle-ware day end settlement - start


        }

        //Transaction Detail Master updates...
//	wrapper.getTransactionDetailMasterModel().setCommissionReasonId(commissionReasonId)
        return wrapper;
    }


    private SettlementTransactionModel createSettlementTransactionModel(WorkFlowWrapper wrapper, AppUserModel appUserModel) {

        SettlementTransactionModel settlementModel = new SettlementTransactionModel();
        settlementModel.setTransactionID(wrapper.getTransactionModel().getTransactionId());
        settlementModel.setProductID(wrapper.getTransactionDetailModel().getProductId());
        settlementModel.setCreatedBy(appUserModel.getAppUserId());
        settlementModel.setUpdatedBy(appUserModel.getAppUserId());
        settlementModel.setCreatedOn(new Date());
        settlementModel.setUpdatedOn(new Date());
        settlementModel.setStatus(0L);

        return settlementModel;
    }

    private WorkFlowWrapper prepareTrxDetailMasterModelForAgentNetworkChange(WorkFlowWrapper wrapper) throws Exception {
        logger.info("[TransactionProcessor.prepareTrxDetailMasterModelForAgentNetworkChange] Updating Transaction Detail Master.");
        Boolean isLeg2Trx = wrapper.isLeg2Transaction();
        RetailerContactModel senderRetailerContactModel = null;
        RetailerContactModel receiverRetailerContactModel = null;
        RetailerModel senderRetailerModel = null;
        RetailerModel receiverRetailerModel = null;
        DistributorModel senderDistributorModel = null;
        DistributorModel receiverDistributorModel = null;
        senderRetailerContactModel = wrapper.getToRetailerContactModel();
        if (senderRetailerContactModel != null && isLeg2Trx) {
            senderRetailerContactModel = appUserManager.getRetailerContactModelById(senderRetailerContactModel.getRetailerContactId());
            senderRetailerModel = appUserManager.getRetailerModelByRetailerIdForTrx(senderRetailerContactModel.getRetailerId());
            if (senderRetailerModel != null)
                senderDistributorModel = appUserManager.findDistributorModelByIdForTrx(senderRetailerModel.getDistributorId());
        }
        receiverRetailerContactModel = wrapper.getFromRetailerContactModel();
        if (receiverRetailerContactModel != null && !isLeg2Trx) {
            receiverRetailerContactModel = appUserManager.getRetailerContactModelById(receiverRetailerContactModel.getRetailerContactId());
            receiverRetailerModel = appUserManager.getRetailerModelByRetailerIdForTrx(receiverRetailerContactModel.getRetailerId());
            if (receiverRetailerModel != null)
                receiverDistributorModel = appUserManager.findDistributorModelByIdForTrx(receiverRetailerModel.getDistributorId());
        }
        if (isLeg2Trx) {
            wrapper = updateTrxDetailMasterForLeg2(wrapper, senderRetailerContactModel, senderRetailerModel, senderDistributorModel);
            wrapper = updateTrxDetailMasterForLeg1(wrapper, receiverRetailerContactModel, receiverRetailerModel, receiverDistributorModel);
        } else
            wrapper = updateTrxDetailMasterForLeg1(wrapper, receiverRetailerContactModel, receiverRetailerModel, receiverDistributorModel);
        return wrapper;
    }

    private WorkFlowWrapper updateTrxDetailMasterForLeg1(WorkFlowWrapper wrapper, RetailerContactModel retailerContactModel, RetailerModel retailerModel,
                                                         DistributorModel distributorModel) throws Exception {
        TransactionDetailMasterModel txDetailMaster = wrapper.getTransactionDetailMasterModel();
        AreaModel areaModel = null;
        RegionModel regionModel = null;
        DistributorLevelModel distributorLevelModel = null;
        if (retailerContactModel != null) {
            distributorLevelModel = retailerContactModel.getRelationDistributorLevelModel();
            txDetailMaster.setSenderRetailerContactId(retailerContactModel.getRetailerContactId());
            txDetailMaster.setSenderRetailerContactName(retailerContactModel.getName());
            if (retailerContactModel.getParentRetailerContactModel() != null)
                txDetailMaster.setSenderParentRetailerContactId(retailerContactModel.getParentRetailerContactModel().getParentRetailerContactModelId());
        }
        if (retailerModel != null) {
            regionModel = retailerModel.getRegionModel();
            areaModel = retailerModel.getRelationAreaIdAreaModel();
        }
        if (retailerModel != null) {
            txDetailMaster.setSenderRetailerId(retailerModel.getRetailerId());
            txDetailMaster.setSenderRetailerName(retailerModel.getName());
        }
        if (distributorModel != null) {
            txDetailMaster.setSenderDistributorId(retailerModel.getDistributorId());
            txDetailMaster.setSenderDistributorName(distributorModel.getName());
            txDetailMaster.setSenderServiceOPId(distributorModel.getMnoId());
        }
        if (regionModel != null) {
            txDetailMaster.setSendingRegion(regionModel.getRegionId());
            txDetailMaster.setSendingRegionName(regionModel.getRegionName());
        }
        if (areaModel != null) {
            txDetailMaster.setSenderAreaId(areaModel.getAreaId());
            txDetailMaster.setSenderAreaName(areaModel.getName());
        }
        if (distributorLevelModel != null) {
            txDetailMaster.setSenderDistributorLevelId(distributorLevelModel.getDistributorLevelId());
            txDetailMaster.setSenderDistributorLevelName(distributorLevelModel.getName());
        }
        wrapper.setTransactionDetailMasterModel(txDetailMaster);
        return wrapper;
    }

    private WorkFlowWrapper updateTrxDetailMasterForLeg2(WorkFlowWrapper wrapper, RetailerContactModel retailerContactModel, RetailerModel retailerModel,
                                                         DistributorModel distributorModel) throws Exception {
        TransactionDetailMasterModel txDetailMaster = wrapper.getTransactionDetailMasterModel();
        AreaModel areaModel = null;
        RegionModel regionModel = null;
        DistributorLevelModel distributorLevelModel = null;
        if (retailerContactModel != null) {
            distributorLevelModel = retailerContactModel.getRelationDistributorLevelModel();
            txDetailMaster.setRecipientRetailerContactId(retailerContactModel.getRetailerContactId());
            txDetailMaster.setRecipientRetailerContactName(retailerContactModel.getName());
            if (retailerContactModel.getParentRetailerContactModel() != null)
                txDetailMaster.setReceiverParentRetailerContactId(retailerContactModel.getParentRetailerContactModel().getParentRetailerContactModelId());
        }
        if (retailerModel != null) {
            regionModel = retailerModel.getRegionModel();
            areaModel = retailerModel.getRelationAreaIdAreaModel();
        }
        if (retailerModel != null) {
            txDetailMaster.setRecipientRetailerId(retailerModel.getRetailerId());
            txDetailMaster.setRecipientRetailerName(retailerModel.getName());
        }
        if (distributorModel != null) {
            txDetailMaster.setReceiverDistributorId(retailerModel.getDistributorId());
            txDetailMaster.setReceiverDistributorName(distributorModel.getName());
            txDetailMaster.setReceiverServiceOPId(distributorModel.getMnoId());
        }
        if (regionModel != null) {
            txDetailMaster.setReceivingRegion(regionModel.getRegionId());
            txDetailMaster.setReceivingRegionName(regionModel.getRegionName());
        }
        if (areaModel != null) {
            txDetailMaster.setReceiverAreaId(areaModel.getAreaId());
            txDetailMaster.setReceiverAreaName(areaModel.getName());
        }
        if (distributorLevelModel != null) {
            txDetailMaster.setRecipientDistributorLevelId(distributorLevelModel.getDistributorLevelId());
            txDetailMaster.setRecipientDistributorLevelName(distributorLevelModel.getName());
        }
        wrapper.setTransactionDetailMasterModel(txDetailMaster);
        return wrapper;
    }

    private void updateTransactionDetailMasterForTransaction(WorkFlowWrapper wrapper) throws Exception {
        logger.info("[TransactionProcessor.updateTransactionDetailMasterForTransaction] Updating Transaction Detail Master.");
        wrapper = this.prepareTrxDetailMasterModelForAgentNetworkChange(wrapper);
        TransactionDetailMasterModel txDetailMaster = wrapper.getTransactionDetailMasterModel();
        if (wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID) != null && !wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID).equals(""))
            txDetailMaster.setTerminalId((String) wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID));
        // Skip in case of Commission Settlement Transaction type
        if (wrapper.getTransactionTypeModel() != null & wrapper.getTransactionTypeModel().getTransactionTypeId().longValue() != TransactionTypeConstantsInterface.COMMISSION_SETTLEMENT) {

            //if :new.transaction_type_id <> 19 then
            if (wrapper.getProductModel() != null && wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.CASH_TRANSFER
                    && wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT
                    && wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.IBFT
                    && wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.WEB_SERVICE_CASH_IN) {

                txDetailMaster.setMfsId(wrapper.getTransactionModel().getMfsId());
                txDetailMaster.setSaleMobileNo(wrapper.getTransactionModel().getSaleMobileNo());

                if (UtilityCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                        InternetCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                        NadraCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))
                        || OtherThanOneBillProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))
                        || OneBillProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))) {

                    txDetailMaster.setSenderCnic(wrapper.getCustomerAppUserModel().getNic());
                    txDetailMaster.setSaleMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());

                } else if (wrapper.getTransactionModel().getSaleMobileNo() != null) {
                    Long[] appUserTypeIds = {UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER};
                    AppUserModel appUser = appUserManager.loadAppUserByMobileAndType(wrapper.getTransactionModel().getSaleMobileNo(), appUserTypeIds);

                    if (appUser != null) {
                        txDetailMaster.setSenderCnic(appUser.getNic());
                    }
                }

                if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_BB_ACCOUNT.longValue()) {

                    txDetailMaster.setSenderCnic(wrapper.getWalkInCustomerCNIC());
                }
                if (wrapper.getAppUserModel() != null &&
                        (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.WEB_SERVICE_PAYMENT_TX) || wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.VIRTUAL_CARD_PAYMENT_TX))) {
                    txDetailMaster.setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
                    txDetailMaster.setSenderCnic(wrapper.getAppUserModel().getNic());
                    txDetailMaster.setSaleMobileNo(wrapper.getAppUserModel().getMobileNo());
                }

                if (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_CASH_OUT_TX)) {
                    txDetailMaster.setRecipientMfsId("");
                    txDetailMaster.setRecipientMobileNo(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
                    txDetailMaster.setRecipientCnic(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC).toString());
                } else if (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CASH_OUT_TX)) {
                    txDetailMaster.setRecipientMfsId("");
                    if (wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE) != null)
                        txDetailMaster.setRecipientMobileNo(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
                    if (wrapper.getObject(CommandFieldConstants.KEY_CNIC) != null)
                        txDetailMaster.setRecipientCnic(wrapper.getObject(CommandFieldConstants.KEY_CNIC).toString());

                } else if (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING_TX)) {
                    txDetailMaster.setRecipientMfsId("");
                    if (wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE) != null)
                        txDetailMaster.setRecipientMobileNo(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
                    if (wrapper.getObject(CommandFieldConstants.KEY_CNIC) != null)
                        txDetailMaster.setRecipientCnic(wrapper.getObject(CommandFieldConstants.KEY_CNIC).toString());
                } else if (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CARD_ISSUANCE_REISSUANCE_TX)) {
                    txDetailMaster.setRecipientMfsId("");
                    if (wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE) != null)
                        txDetailMaster.setRecipientMobileNo(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
                    if (wrapper.getObject(CommandFieldConstants.KEY_CNIC) != null)
                        txDetailMaster.setRecipientCnic(wrapper.getObject(CommandFieldConstants.KEY_CNIC).toString());
                } else if (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.PROOF_OF_LIFE_TX)) {
                    txDetailMaster.setRecipientMfsId("");
                    if (wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE) != null)
                        txDetailMaster.setRecipientMobileNo(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
                    if (wrapper.getObject(CommandFieldConstants.KEY_CNIC) != null)
                        txDetailMaster.setRecipientCnic(wrapper.getObject(CommandFieldConstants.KEY_CNIC).toString());
                } else if (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.ADVANCE_SALARY_LOAN_TX)) {
                    txDetailMaster.setRecipientMfsId("");
                    if (wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE) != null)
                        txDetailMaster.setRecipientMobileNo(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
                    if (wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC) != null)
                        txDetailMaster.setRecipientCnic(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC).toString());
                }
            }

            //if :new.transaction_type_id not in (19, 20,27) then
            if (wrapper.getTransactionTypeModel() != null && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.CASH_TO_CASH
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.ACCOUNT_TO_CASH_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.BB_TO_CORE_ACCOUNT_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.CUSTOMER_INITIATED_BB_TO_CORE_ACCOUNT_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.SENDER_REDEEM_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.CUSTOMER_COLLECTION_PAYMENT_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.COLLECTION_PAYMENT_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.SENDER_REDEEM_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.DONATION_PAYMENT_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.WEB_SERVICE_PAYMENT_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.VIRTUAL_CARD_PAYMENT_TX
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_CASH_OUT_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.PROOF_OF_LIFE_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.ADVANCE_SALARY_LOAN_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CASH_OUT_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.AGENT_IBFT_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CARD_ISSUANCE_REISSUANCE_TX)
            ) {

                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionModel().getNotificationMobileNo());

                if (wrapper.getTransactionModel().getNotificationMobileNo() != null) {
                    Long[] appUserTypeIds = {UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER};
                    AppUserModel appUser = appUserManager.loadAppUserByMobileAndType(wrapper.getTransactionModel().getNotificationMobileNo(), appUserTypeIds);

                    if (appUser != null) {
                        txDetailMaster.setRecipientCnic(appUser.getNic());
                        UserDeviceAccountsModel userDeviceModel = null;
                        for (UserDeviceAccountsModel model : appUser.getAppUserIdUserDeviceAccountsModelList()) {
                            userDeviceModel = model;
                            break;
                        }

                        if (userDeviceModel != null) {
                            txDetailMaster.setRecipientMfsId(userDeviceModel.getUserId());
                        }
                    }
                }
            }

            txDetailMaster.setTransactionId(wrapper.getTransactionModel().getTransactionId());
            txDetailMaster.setAuthorizationCode(wrapper.getTransactionModel().getBankResponseCode());
            if (!StringUtil.isNullOrEmpty(wrapper.getTransactionModel().getBankAccountNo())) {
                txDetailMaster.setBankAccountNo(wrapper.getTransactionModel().getBankAccountNo());
                txDetailMaster.setBankAccountNoLastFive(StringUtil.getLastFiveDigitsFromAccountNo(wrapper.getTransactionModel().getBankAccountNo()));
            }
            txDetailMaster.setUpdatedOn(wrapper.getTransactionModel().getUpdatedOn());
            txDetailMaster.setPaymentModeId(wrapper.getTransactionModel().getPaymentModeId());
            if (wrapper.getTransactionModel().getPaymentModeId() != null) {
                txDetailMaster.setPaymentMode(PaymentModeConstantsInterface.paymentModeConstantsMap.get(wrapper.getTransactionModel().getPaymentModeId()));
            }
            txDetailMaster.setBankId(wrapper.getTransactionModel().getProcessingBankId());
            txDetailMaster.setBankName(wrapper.getTransactionModel().getProcessingBankIdBankModel().getName());
            txDetailMaster.setTransactionAmount(wrapper.getTransactionModel().getTransactionAmount());
            if (!wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.VC_TRANSFER_PRODUCT)) {
                if (wrapper.getCommissionAmountsHolder().getExclusivePercentAmount() != null || wrapper.getCommissionAmountsHolder().getExclusiveFixAmount() != null) {
                    if (wrapper.getCommissionAmountsHolder().getExclusivePercentAmount() > 0.0 || wrapper.getCommissionAmountsHolder().getExclusiveFixAmount() > 0.0) {
                        if (wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE) ||
                                wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.DEBIT_CARD_ISSUANCE) ||
                                wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE) ||
                                wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.DEBIT_CARD_ANNUAL_FEE)
                                || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.ATM_BALANCE_INQUIRY_OFF_US)
                                || wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.INTERNATIONAL_BALANCE_INQUIRY_OFF_US)) {
                            txDetailMaster.setTotalAmount(wrapper.getTransactionModel().getTransactionAmount() +
                                    wrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID));
                            txDetailMaster.setExclusiveCharges(wrapper.getTransactionModel().getTransactionAmount() + wrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID));

                        } else {
                            txDetailMaster.setTotalAmount(wrapper.getTransactionModel().getTotalAmount());
                        }
                    }
                } else {
                    txDetailMaster.setTotalAmount(wrapper.getTransactionModel().getTotalAmount());
                }
            }
            txDetailMaster.setMfsId(wrapper.getTransactionModel().getMfsId());
            txDetailMaster.setSupProcessingStatusId(wrapper.getTransactionModel().getSupProcessingStatusId());
            txDetailMaster.setProcessingStatusName(SupplierProcessingStatusConstants.processingStatusNamesMap.get(wrapper.getTransactionModel().getSupProcessingStatusId()));
            txDetailMaster.setSenderDeviceTypeId(wrapper.getTransactionModel().getDeviceTypeId());
            if (wrapper.getTransactionModel().getDeviceTypeId() != null) {
                txDetailMaster.setDeviceType(DeviceTypeConstantsInterface.DEVICE_TYPES_MAP.get(wrapper.getTransactionModel().getDeviceTypeId()));
            }

            if (wrapper.getTransactionModel().getSmartMoneyAccountId() != null) {
                txDetailMaster.setSenderAccountNick(transactionDetailMasterManager.loadSmartMoneyAccountNickBySmartMoneyAccountId(wrapper.getTransactionModel().getSmartMoneyAccountId()));
            }

            if (wrapper.getTransactionModel().getFromRetContactId() != null) {
                txDetailMaster.setAgent1Id(transactionDetailMasterManager.loadAgentId(wrapper.getTransactionModel().getFromRetContactId()));
            }

            if (wrapper.getTransactionModel().getToRetContactId() != null &&
                    wrapper.getProductModel() != null &&
                    wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.AGENT_TO_AGENT_TRANSFER &&
                    wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.RETAIL_PAYMENT) {

                txDetailMaster.setAgent2Id(transactionDetailMasterManager.loadAgentId(wrapper.getTransactionModel().getToRetContactId()));
            }
            if (wrapper.getTransactionModel().getFromRetContactId() != null) {
                txDetailMaster.setAgent1MobileNo(transactionDetailMasterManager.getAgentMobileByRetailerContactId(wrapper.getTransactionModel().getFromRetContactId()));
            }

        }

        ServiceModel serviceModel = wrapper.getProductModel().getServiceIdServiceModel();
        if (serviceModel != null) {
            txDetailMaster.setServiceId(serviceModel.getServiceId());
            txDetailMaster.setServiceName(serviceModel.getName());
        }

        //Transaction Detail Fields updated
        if (wrapper.getProductModel() != null && wrapper.getProductModel().getProductId().longValue() != 2510734L) {//Only God knows why this product id is used. but i m following it :(

//		  IF :new.product_id in (2510791,2510795,2510797) THEN
            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.APOTHECARE
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.DAWAT_E_ISLAMI_ZAKAT
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.DAWAT_E_ISLAMI_SADQA
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ML_TRANSFER_TO_RETAILER
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ML_TRANSFER_TO_CUSTOMER) {

                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionDetailModel().getCustomField6());
            }

//		  IF CRS.SUPPLIER_ID=50063 or :new.product_id in (2510793,2510794,2510796) THEN
            ProductModel productModel = transactionDetailMasterManager.getProductModelByProductId(wrapper.getTransactionDetailModel().getProductId());
            if (wrapper.getProductModel().getSupplierId().longValue() == SupplierConstants.TransReportPhonixCSRViewSupplierID
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.APOTHECARE_PAYMENT
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.DAWAT_E_ISLAMI_ZAKAT_PAYMENT
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.DAWAT_E_ISLAMI_SADQA_PAYMENT) {

                txDetailMaster.setConsumerNo(wrapper.getTransactionDetailModel().getConsumerNo());
                txDetailMaster.setRecipientMobileNo(null);
                txDetailMaster.setRecipientMfsId(null);
                txDetailMaster.setRecipientCnic(null);

            }

            //In order to show challan number on transaction report in case of challan payment
            if (wrapper.getProductModel().getServiceId().longValue() == ServiceConstantsInterface.COLLECTION_PAYMENT) {
                txDetailMaster.setConsumerNo(wrapper.getTransactionDetailModel().getConsumerNo());
            }
            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.HRA_CASH_WITHDRAWAL) {
                txDetailMaster.setReceiverBVS(wrapper.isReceiverBvs());
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_BB_ACCOUNT
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT) {

                txDetailMaster.setSenderBVS(wrapper.isSenderBvs());

            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.BISP_CASH_OUT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.BISP_CASH_OUT_WALLET) {
                txDetailMaster.setSenderBVS(wrapper.isSenderBvs());
                txDetailMaster.setReceiverBVS(wrapper.isReceiverBvs());
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING) {
                txDetailMaster.setReceiverBVS(Boolean.TRUE);
            }
            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.PROOF_OF_LIFE) {
                txDetailMaster.setReceiverBVS(Boolean.TRUE);
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.BOP_CASH_OUT) {
                txDetailMaster.setReceiverBVS(wrapper.isReceiverBvs());
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT) {
                txDetailMaster.setSenderCnic(wrapper.getTransactionDetailModel().getCustomField7());
                txDetailMaster.setSaleMobileNo(wrapper.getTransactionDetailModel().getCustomField6());
                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionDetailModel().getCustomField5());
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CASH_DEPOSIT) {

                txDetailMaster.setCashDepositorCnic(wrapper.getTransactionDetailModel().getCustomField7());
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CASH_TRANSFER) {

                txDetailMaster.setMfsId(null);
                txDetailMaster.setSaleMobileNo(wrapper.getTransactionDetailModel().getCustomField6());
                txDetailMaster.setSenderCnic(wrapper.getTransactionDetailModel().getCustomField7());
                txDetailMaster.setRecipientCnic(wrapper.getTransactionDetailModel().getCustomField9());
                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionDetailModel().getCustomField5());
                txDetailMaster.setRecipientMfsId(null);

                if (wrapper.isLeg2Transaction())
                    txDetailMaster.setReceiverBVS(wrapper.isReceiverBvs());
                else
                    txDetailMaster.setSenderBVS(wrapper.isSenderBvs());

            }


            if (wrapper.getProductModel().getServiceId() == ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue()) {

                txDetailMaster.setMfsId(null);
                txDetailMaster.setSenderCnic(wrapper.getTransactionDetailModel().getCustomField7());
                txDetailMaster.setRecipientCnic(wrapper.getTransactionDetailModel().getCustomField9());
                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionDetailModel().getCustomField5());
                txDetailMaster.setRecipientMfsId(null);

            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACT_TO_CASH_CI) {

                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionDetailModel().getCustomField5());
                txDetailMaster.setRecipientMfsId(null);
                txDetailMaster.setRecipientCnic(wrapper.getTransactionDetailModel().getCustomField9());
                txDetailMaster.setReceiverBVS(wrapper.isReceiverBvs());
            }


            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACT_TO_ACT) {

                txDetailMaster.setRecipientMobileNo(wrapper.getReceiverAppUserModel().getMobileNo());
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.BB_TO_CORE_ACCOUNT) {

                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionDetailModel().getCustomField5());
                txDetailMaster.setSaleMobileNo(wrapper.getTransactionModel().getNotificationMobileNo());
                txDetailMaster.setSenderCnic(wrapper.getTransactionDetailModel().getCustomField7());

            }

            long productId = wrapper.getProductModel().getProductId().longValue();

            if (productId == ProductConstantsInterface.TELLER_CASH_OUT ||
                    productId == ProductConstantsInterface.TELLER_WALK_IN_CASH_IN ||
                    productId == ProductConstantsInterface.TELLER_ACCOUNT_HOLDER_CASH_IN) {

                TransactionModel transactionModel = wrapper.getTransactionModel();
                TransactionDetailModel transactionDetailModel = wrapper.getTransactionDetailModel();

                txDetailMaster.setRecipientMobileNo(transactionDetailModel.getCustomField8());
                txDetailMaster.setRecipientCnic(transactionDetailModel.getCustomField5());
                txDetailMaster.setSaleMobileNo(transactionDetailModel.getCustomField6());
                txDetailMaster.setSenderCnic(transactionDetailModel.getCustomField7());
                txDetailMaster.setRecipientAccountNo(transactionDetailModel.getCustomField2());
                txDetailMaster.setTellerAppUserId(wrapper.getAppUserModel().getAppUserId());
            }

            txDetailMaster.setTransactionDetailId(wrapper.getTransactionDetailModel().getTransactionDetailId());
            txDetailMaster.setRecipientBankAccountNo(wrapper.getTransactionDetailModel().getCustomField1());
            txDetailMaster.setProductId(wrapper.getTransactionDetailModel().getProductId());

            if (productModel != null && productModel.getProductId() != null) {
                txDetailMaster.setProductName(productModel.getName());
                txDetailMaster.setProductCode(productModel.getProductCode());
                txDetailMaster.setBillType(productModel.getBillType());
                txDetailMaster.setSupplierId(productModel.getSupplierId());
                txDetailMaster.setSupplierName(productModel.getSupplierIdSupplierModel().getName());
            }

            if (wrapper.getTransactionDetailModel().getCustomField13() != null) {
                txDetailMaster.setRecipientDeviceTypeId(Long.valueOf(wrapper.getTransactionDetailModel().getCustomField13()));
                String deviceTypeName = transactionDetailMasterManager.getDeviceTypeNameById(Long.valueOf(wrapper.getTransactionDetailModel().getCustomField13()));
                txDetailMaster.setRecipientDeviceType(deviceTypeName);
            }

            if (wrapper.isLeg2Transaction())
                txDetailMaster.setReceiverBVS(wrapper.isReceiverBvs());

            if (wrapper.getTransactionDetailModel().getCustomField1() != null
                    && productId != ProductConstantsInterface.BB_TO_CORE_ACCOUNT) {
                txDetailMaster.setRecipientAccountNick(transactionDetailMasterManager.loadSmartMoneyAccountNickBySmartMoneyAccountId(Long.valueOf(wrapper.getTransactionDetailModel().getCustomField1())));
            }

            if (wrapper.getTransactionDetailModel().getCustomField2() != null &&
                    wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.CASH_TRANSFER) {

                txDetailMaster.setRecipientAccountNo(wrapper.getTransactionDetailModel().getCustomField2());
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.INTERNATIONAL_BALANCE_INQUIRY_OFF_US) {

                txDetailMaster.setRecipientMfsId("");
                txDetailMaster.setRecipientMobileNo("");
                txDetailMaster.setRecipientAccountNo(MessageUtil.getMessage("international.balance.inquiry.pool.account"));
                txDetailMaster.setRecipientCnic("");
                txDetailMaster.setAgent1Id("");
                txDetailMaster.setSenderAgentAccountNo("");
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL) {

                txDetailMaster.setRecipientMfsId("");
                txDetailMaster.setRecipientMobileNo("");
                txDetailMaster.setRecipientAccountNo(MessageUtil.getMessage("international.pos.pool.account"));
                txDetailMaster.setRecipientCnic("");
                txDetailMaster.setAgent1Id("");
            }
            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US) {

                txDetailMaster.setRecipientMfsId("");
                txDetailMaster.setRecipientMobileNo("");
                txDetailMaster.setRecipientAccountNo(MessageUtil.getMessage("international.debitcardcashwithdrawal.offus.pool.account"));
                txDetailMaster.setRecipientCnic("");
                txDetailMaster.setAgent1Id("");
            }

            // setting segmentId to be used for Velocity_Stats calculation
            if (wrapper.getSegmentModel() != null &&
                    wrapper.getSegmentModel().getSegmentId() != null) {

                txDetailMaster.setSegmentId(wrapper.getSegmentModel().getSegmentId());
            }


        }

        if (wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT) ||
                wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.AGENT_BB_TO_IBFT)) {
            txDetailMaster.setTotalAmount(wrapper.getCommissionAmountsHolder().getTotalAmount());
//		  txDetailMaster.setProductThresholdCharges(wrapper.getChargedAmount());
            if (wrapper.getChargedAmount() != null) {
                if (wrapper.getCommissionThresholdRate().getMaxThresholdAmount() != null && wrapper.getChargedAmount().equals(wrapper.getCommissionThresholdRate().getMaxThresholdAmount())) {
                    txDetailMaster.setExclusiveCharges(wrapper.getChargedAmount());
                } else {
                    txDetailMaster.setExclusiveCharges(wrapper.getCommissionAmountsHolder().getExclusiveFixAmount() + wrapper.getChargedAmount());
                }
            }
//		  if((wrapper.getCommissionThresholdRate() != null && wrapper.getChargedAmount() != null) &&
//				  wrapper.getChargedAmount().equals(wrapper.getCommissionThresholdRate().getMaxThresholdAmount())){
//			  txDetailMaster.setExclusiveCharges(0.0);
//			  txDetailMaster.setInclusiveCharges(0.0);
//		  }
        }

        logger.info("[TransactionProcessor.updateTransactionDetailMasterForTransaction] Saving transaction detail master. PK: " + txDetailMaster.getPk() + " Trx Code:" + wrapper.getTransactionDetailMasterModel().getTransactionCode() + " Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());

        if (wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.VC_TRANSFER_PRODUCT)) {
            txDetailMaster.setProcessingStatusName("Completed");
            txDetailMaster.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
            txDetailMaster.setStan(wrapper.getSwitchWrapper().getMiddlewareIntegrationMessageVO().getStan());
        }
        transactionDetailMasterManager.saveTransactionDetailMaster(txDetailMaster);

    }
  
/*Commented By OmarButt + Mudassir on 07-05-2015 - Moved following field population in relevent Transaction Classes
	public WorkFlowWrapper postUpdate(WorkFlowWrapper wrapper) throws Exception{
	  if(wrapper.getProductModel() != null && wrapper.getProductModel().getProductId() != null && wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CASH_WITHDRAWAL
			  && wrapper.getTransactionDetailMasterModel().getSupProcessingStatusId().longValue() ==  SupplierProcessingStatusConstants.FAILED){
		  
		  logger.error("[TransactionProcessor.postUpdate] Updating transaction detail master for Cash Withdrawal. Logged in AppUserID:" 
				  + ThreadLocalAppUser.getAppUserModel().getAppUserId());

		  wrapper.getTransactionDetailMasterModel().setProductName(ProductConstantsInterface.CASH_WITHDRAWAL_NAME);//product name
		  
		  logger.info("[TransactionProcessor.postUpdate] Loading UserId if Agent by Retailer Contact Id. Logged in AppUserID:" 
		  + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". For Ret Contact Id:" + ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
		  
		  wrapper.getTransactionDetailMasterModel().setMfsId(transactionDetailMasterManager.loadAgentId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId()));
		  
		  if (wrapper.getTransactionDetailMasterModel().getRecipientMfsId() == null) {
			  logger.info("[TransactionProcessor.postUpdate] Loading UserId if Customer By Customer Mobile No. Logged in AppUserID:" 
					  + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". For Mobile :" + wrapper.getCustomerModel().getMobileNo());

			  wrapper.getTransactionDetailMasterModel().setRecipientMfsId(
						  transactionDetailMasterManager.loadUserIdByMobileNo(wrapper.getCustomerModel().getMobileNo()));
		  }
		  
		  wrapper.getTransactionDetailMasterModel().setUpdatedOn(new Date());
		  
	  }

	  if(wrapper.getProductModel() != null && wrapper.getProductModel().getProductId() != null && wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH
			  && wrapper.getTransactionDetailMasterModel().getSupProcessingStatusId().longValue() ==  SupplierProcessingStatusConstants.FAILED){
		  
		  logger.error("[TransactionProcessor.postUpdate] Updating transaction detail master for Account To Cash. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());

		  wrapper.getTransactionDetailMasterModel().setUpdatedOn(new Date());
		  if (wrapper.getTransactionDetailMasterModel().getMfsId() == null && wrapper.getTransactionDetailMasterModel().getSaleMobileNo() != null) {
			  logger.info("[TransactionProcessor.postUpdate] Loading UserId if Customer By Customer Mobile No. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". For Mobile :" + wrapper.getTransactionDetailMasterModel().getRecipientMobileNo());
			  wrapper.getTransactionDetailMasterModel().setRecipientMfsId(transactionDetailMasterManager.loadUserIdByMobileNo(wrapper.getTransactionDetailMasterModel().getSaleMobileNo()));
		  } 
	  }
		
	  
	  BaseWrapper baseWrapper = new BaseWrapperImpl();
	  baseWrapper.setBasePersistableModel(wrapper.getTransactionDetailMasterModel());
	  
	  logger.info("[TransactionProcessor.postUpdate] Saving transaction detail master. PK: " + wrapper.getTransactionDetailMasterModel().getPk() + " Trx Code:" + wrapper.getTransactionDetailMasterModel().getTransactionCode() + " Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
	  try{
		  transactionDetailMasterManager.saveTransactionDetailMasterRequiresNewTransaction(baseWrapper);
	  }catch (Exception ex) {
		  logger.error("[TransctionProcessor.postUpdate] Exception in saving Tranasction Detail Master. Trx ID: " + wrapper.getTransactionDetailMasterModel().getTransactionId() + ". Logged in App User ID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception:" + ex.getMessage());
		  ex.printStackTrace();
	  }
	  return wrapper;
  }
*/

    protected WorkFlowWrapper makeIvrRequest(WorkFlowWrapper wrapper) throws Exception {
        wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.IVR_VALIDATION_PENDING);
        txManager.saveTransaction(wrapper);
        IvrRequestDTO ivrDTO = new IvrRequestDTO();
        if (wrapper.getHandlerModel() != null && wrapper.getHandlerAppUserModel() != null) {
            ivrDTO.setAgentMobileNo(wrapper.getHandlerAppUserModel().getMobileNo());
        } else {
            ivrDTO.setAgentMobileNo(wrapper.getAppUserModel().getMobileNo());
        }
        ivrDTO.setAmount(wrapper.getTransactionModel().getTotalAmount());
        ivrDTO.setCharges(wrapper.getTxProcessingAmount());
        ivrDTO.setCustomerMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
        ivrDTO.setRetryCount(0);
        ivrDTO.setProductId(wrapper.getProductModel().getProductId());
        ivrDTO.setTransactionId(wrapper.getTransactionCodeModel().getCode());
        ivrDTO.setAgentId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
        logger.info("[TransactionProcessor.makeIvrRequest] Agent ID:" + ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
        if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH) {
            ivrDTO.setRecipientCnic(wrapper.getRecipientWalkinCustomerModel().getCnic());
        } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACT_TO_ACT) {
            ivrDTO.setRecipientMobile(wrapper.getReceiverAppUserModel().getMobileNo());
        } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.BB_TO_CORE_ACCOUNT) {
            ivrDTO.setCoreAccountNo(wrapper.getTransactionDetailModel().getCustomField11());
        } else if (UtilityCompanyEnum.contains(wrapper.getProductModel().getProductId().toString())
                || InternetCompanyEnum.contains(wrapper.getProductModel().getProductId().toString())
                || NadraCompanyEnum.contains(wrapper.getProductModel().getProductId().toString())
                || OneBillProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))
                || OtherThanOneBillProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))) {

            ivrDTO.setConsumerNo(wrapper.getTransactionDetailModel().getConsumerNo());
        }

        this.appUserManager.checkIsCustomerPINGenerated(wrapper.getCustomerModel());

        ivrAuthenticationRequestQueueSender.sentAuthenticationRequest(ivrDTO);

        return wrapper;
    }


    /**
     * <p>This method follows the call to the End method for any transaction</p>
     *
     * @param wrapper
     * @return
     */
    protected WorkFlowWrapper doPostEnd(WorkFlowWrapper wrapper) {
        //statusCheck.checkActiveMQStatus();
        return wrapper;
    }

    protected WorkFlowWrapper doEnd(WorkFlowWrapper wrapper) throws Exception {
        //update the transaction code
        if (logger.isDebugEnabled()) {
            logger.debug("Inside doEnd(WorkFlowWrapper wrapper) of TransactionProcessor..");
            logger.debug("Going to update the transaction code");
        }
        txManager.updateTransactionCode(wrapper);
    /*
    if(wrapper.getTransactionModel().getTransactionTypeIdTransactionTypeModel().getTransactionTypeId() == TransactionTypeConstantsInterface.BILL_SALE_TX)
    {
      smsSender.send(wrapper.getTransactionModel().getCustomerMobileNo(),"your bill has been paid successfully");
    }
    else if(wrapper.getTransactionModel().getTransactionTypeIdTransactionTypeModel().getTransactionTypeId() == TransactionTypeConstantsInterface.DIST_TO_DIST_TX)
    {
      smsSender.send(wrapper.getTransactionModel().getToDistContactMobNo(),"A credit amount of "+wrapper.getTransactionModel().getTotalAmount()+" has been transferred to you.");
    }
    */


        //Send sms
        if (logger.isDebugEnabled()) {
            logger.debug("Ending doEnd(WorkFlowWrapper wrapper) of TransactionProcessor..");
        }
        return wrapper;
    }

    /**
     * <p>This method preceeds the call to the process method for any transaction.</p>
     *
     * @param wrapper
     * @return
     */
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception {
        Boolean isIvrResp = wrapper.getIsIvrResponse();
        Long registerState = null;
        //Customer with appUserModel.registerationStateId=1 can only perform one debit and one credit
        try {
            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CASH_DEPOSIT.longValue()) {

                registerState = wrapper.getCustomerAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, false, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.WEB_SERVICE_CASH_IN.longValue()) {

                registerState = wrapper.getCustomerAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, false, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.APIGEE_PAYMENT.longValue()) {
                registerState = wrapper.getAppUserModel().getRegistrationStateModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACT_TO_ACT.longValue()) {

                registerState = wrapper.getCustomerAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);
                registerState = wrapper.getReceiverAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getRecipientCustomerModel(), registerState, false, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_BB_ACCOUNT.longValue()) {

                registerState = wrapper.getCustomerAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, false, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH.longValue()) {

                if (wrapper.getCustomerAppUserModel() != null) {
                    registerState = wrapper.getCustomerAppUserModel().getRegistrationStateId();
                    appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);
                }
            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.RETAIL_PAYMENT.longValue()) {

                registerState = wrapper.getCustomerAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CASH_WITHDRAWAL.longValue() || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.HRA_CASH_WITHDRAWAL.longValue()) {

                registerState = wrapper.getCustomerAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CUSTOMER_CASH_WITHDRAWAL.longValue()) {

                registerState = wrapper.getCustomerAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.BB_TO_CORE_ACCOUNT.longValue()) {

                registerState = wrapper.getSenderAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CUSTOMER_BB_TO_CORE_ACCOUNT.longValue()) {

                registerState = wrapper.getSenderAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CUSTOMER_RETAIL_PAYMENT.longValue()) {

                registerState = wrapper.getAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.HRA_TO_WALLET_TRANSACTION.longValue()) {

                registerState = wrapper.getAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CUSTOMER_BB_TO_IBFT.longValue()) {

                registerState = wrapper.getAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);

            } else if (UtilityCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                    InternetCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                    NadraCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))
                    || OtherThanOneBillProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))
                    || OneBillProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))) {

                int customFlag = (wrapper.getCustomField() == null) ? 0 : ((Integer) wrapper.getCustomField()).intValue();

                if (customFlag == 0) { // BB Customer Bill Payment
                    registerState = wrapper.getCustomerAppUserModel().getRegistrationStateId();
                    appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);
                }

            } else if (wrapper.getProductModel().getServiceId().longValue() == ServiceConstantsInterface.COLLECTION_PAYMENT
                    && wrapper.getIsCustomerInitiatedTransaction()) {

                registerState = wrapper.getAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACCOUNT_OPENING.longValue()) {

                registerState = wrapper.getCustomerAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, false, isIvrResp);

            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.WEB_SERVICE_PAYMENT.longValue() || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.VIRTUAL_CARD_PAYMENT.longValue()) {
                registerState = wrapper.getAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);
            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACT_TO_ACT_CI.longValue()) {

                registerState = wrapper.getCustomerAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);
                if (wrapper.getReceiverAppUserModel() != null) {
                    registerState = wrapper.getReceiverAppUserModel().getRegistrationStateId();
                }
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getRecipientCustomerModel(), registerState, false, isIvrResp);
            } else if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACT_TO_CASH_CI.longValue()) {
                if (wrapper.getCustomerAppUserModel() != null)
                    registerState = wrapper.getCustomerAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, true, isIvrResp);
            } else if (wrapper.getAppUserModel() != null && wrapper.getAppUserModel().getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue() && wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.IBFT.longValue()) {
                registerState = wrapper.getAppUserModel().getRegistrationStateId();
                appUserManager.updateCustomerFirstDebitCredit(wrapper.getCustomerModel(), registerState, false, isIvrResp);
            }
        } catch (NullPointerException e) {
            logger.error("[TransactionProcessor.doPreProcess] Error Message: " + e.getMessage());
            logger.error(e.getMessage(), e);
        }


        return wrapper;
    }

    /**
     * <p>This method follows the call to the process method for any transaction.</p>
     *
     * @param wrapper
     * @return
     */
    protected WorkFlowWrapper doPostProcess(WorkFlowWrapper wrapper) {
        return wrapper;
    }

    /**
     * <p>This method is the method where actual processing for any transaction takes place.</p>
     *
     * @param wrapper
     * @return
     */
    protected abstract WorkFlowWrapper doProcess(WorkFlowWrapper wrapper) throws Exception;

    /**
     * <p>This method is responsible for validating the user input coming from iPos
     * or any other client to microbank.</p>
     *
     * @param wrapper
     * @return
     */
    protected abstract WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws
            Exception;

    public void setStatusCheck(StatusCheckManager statusCheck) {
        this.statusCheck = statusCheck;
    }

    public void setTxManager(TransactionModuleManager txManager) {
        this.txManager = txManager;
    }

    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    protected WorkFlowWrapper doPreRollback(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }

    protected WorkFlowWrapper doRollback(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }

    protected WorkFlowWrapper doPostRollback(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }


    public void setTillBalanceManager(TillBalanceManager tillBalanceManager) {
        this.tillBalanceManager = tillBalanceManager;
    }


    public TransactionDetailMasterManager getTransactionDetailMasterManager() {
        return transactionDetailMasterManager;
    }


    public void setTransactionDetailMasterManager(
            TransactionDetailMasterManager transactionDetailMasterManager) {
        this.transactionDetailMasterManager = transactionDetailMasterManager;
    }

    public void setSettlementManager(SettlementManager settlementManager) {
        this.settlementManager = settlementManager;
    }

    public IvrAuthenticationRequestQueue getIvrAuthenticationRequestQueueSender() {
        return ivrAuthenticationRequestQueueSender;
    }

    public void setIvrAuthenticationRequestQueueSender(
            IvrAuthenticationRequestQueue ivrAuthenticationRequestQueueSender) {
        this.ivrAuthenticationRequestQueueSender = ivrAuthenticationRequestQueueSender;
    }


    private void prepareAndSaveSettlementTransactionRDV(Long transactionId, Long productId, Double amount, Long fromAccountInfoId, Long toAccountInfoId) throws Exception {

        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SettlementTransactionModel settlementModel = new SettlementTransactionModel();
        settlementModel.setTransactionID(transactionId);
        settlementModel.setProductID(productId);
        settlementModel.setCreatedBy(appUserModel.getAppUserId());
        settlementModel.setUpdatedBy(appUserModel.getAppUserId());
        settlementModel.setCreatedOn(new Date());
        settlementModel.setUpdatedOn(new Date());
        settlementModel.setStatus(0L);
        settlementModel.setFromBankInfoID(fromAccountInfoId);
        settlementModel.setToBankInfoID(toAccountInfoId);
        settlementModel.setAmount(amount);

        this.settlementManager.saveSettlementTransactionModel(settlementModel);

    }

    public AppUserManager getAppUserManager() {
        return appUserManager;
    }


    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setOtpAuthentication(OTPAuthentication otpAuthentication) {
        this.otpAuthentication = otpAuthentication;
    }

    protected WorkFlowWrapper procecssOTPAuthentication(WorkFlowWrapper wrapper) throws Exception {
        return otpAuthentication.process(wrapper);
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }
}
