package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.ExceptionProcessorUtility;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.inov8.microbank.common.util.StringUtil.isNullOrEmpty;
import static com.inov8.microbank.common.util.XMLConstants.*;
import static org.apache.commons.lang.StringEscapeUtils.escapeXml;


public class AllPayBillPaymentCommand extends BaseCommand {

    protected final Log logger = LogFactory.getLog(AllPayBillPaymentCommand.class);

    private AppUserModel appUserModel;
    private String productId;
    private String accountId;
    private String mobileNo;
    private String txProcessingAmount;
    private String pin;
    private String encryption_type;
    private String deviceTypeId;
    private String commissionAmount;
    private String totalAmount;
    private String billAmount;
    private String cvv;
    private String tPin;
    private String bankId;
    private String accountType;
    private String accountCurrency;
    private String accountStatus;
    private String accountNumber;
    private Double discountAmount = 0D;
    private Long transactionTypeId = null;

    private TransactionModel transactionModel;
    private ProductModel productModel;
    private AccountInfoModel accountInfoModel;
    private BaseWrapper baseWrapper;
    private WorkFlowWrapper workFlowWrapper;
    private String walkInCustomerCNIC;
    private String walkInCustomerMobileNumber;
    private UserDeviceAccountsModel userDeviceAccountsModel;
    private String successMessage;
    private String ussdPin;
    private Double balance = 0D;
    private String consumerNumber;
    private Boolean isInclusiveCharges;
    private String retailerMobileNumber;
    private String customerMobileNumber;
    private String paymentType = "0";
    private String accountTitle = "";
    private ProductVO billPaymentVO = null;
    private Boolean isIvrResponse;
    private String transactionId;
    private Boolean isCustomerTransaction = Boolean.FALSE;
    private Long segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;

    private Date billDueDate;
    private Double billingAmount;
    private Double lateBillAmount;
    private String channelId;
    private String terminalId;
    private String stan;

    private String thirdPartyTransactionId;

    @Override
    public void execute() throws CommandException {

        ThreadLocalAppUser.setAppUserModel(appUserModel);

        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setHandlerModel(handlerModel);
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

        try {
            ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);

            if (validationErrors.hasValidationErrors()) {
                logger.error("[AllPayBillPaymentCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo);
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }

            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));

            CustomerModel customerModel = null;
            AppUserModel customerAppUserModel = this.getAppUserModel(customerMobileNumber);

            if (customerAppUserModel != null) {

                walkInCustomerCNIC = customerAppUserModel.getNic();
                long appUserTypeId = customerAppUserModel.getAppUserTypeId().longValue();

                if (UserTypeConstantsInterface.CUSTOMER.longValue() == appUserTypeId) {

                    customerModel = new CustomerModel();
                    customerModel.setCustomerId(customerAppUserModel.getCustomerId());

                    BaseWrapper baseWrapper = new BaseWrapperImpl();
                    baseWrapper.setBasePersistableModel(customerModel);

                    baseWrapper = this.getCommonCommandManager().loadCustomer(baseWrapper);

                    if (baseWrapper.getBasePersistableModel() != null) {

                        customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
                        segmentId = customerModel.getSegmentId();

                    } else if (isCustomerTransaction) {

                        throw new CommandException("Customer does not exist against mobile number " + customerMobileNumber, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                    }

                }

            } else if (isCustomerTransaction) {

                throw new CommandException("Customer does not exist against mobile number " + customerMobileNumber, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
            }

            if (!isCustomerTransaction) {

                if (customerModel == null) {

                    customerModel = new CustomerModel();
                }

                customerAppUserModel = new AppUserModel();
                customerAppUserModel.setMobileNo(customerMobileNumber);
                customerAppUserModel.setNic(walkInCustomerCNIC);
            }

            if (isIvrResponse) {

                TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
                transactionCodeModel.setCode(transactionId);
                workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
                TransactionDetailMasterModel txDetailMasterModel = new TransactionDetailMasterModel();
                txDetailMasterModel.setTransactionCode(transactionId);
                SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                searchBaseWrapper.setBasePersistableModel(txDetailMasterModel);
                searchBaseWrapper = this.getCommonCommandManager().loadTransactionDetailMaster(searchBaseWrapper);
                txDetailMasterModel = (TransactionDetailMasterModel) searchBaseWrapper.getBasePersistableModel();
                workFlowWrapper.setTransactionDetailMasterModel(txDetailMasterModel);
                customerMobileNumber = txDetailMasterModel.getSaleMobileNo();//
                billAmount = txDetailMasterModel.getTransactionAmount().toString();

                billingAmount = txDetailMasterModel.getBillAmount();
                lateBillAmount = txDetailMasterModel.getLateBillAmount();
                billDueDate = txDetailMasterModel.getBillDueDate();

                baseWrapper.putObject(CommandFieldConstants.KEY_BILL_AMOUNT, txDetailMasterModel.getTransactionAmount());
                baseWrapper.putObject(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, txDetailMasterModel.getConsumerNo());

                if (txDetailMasterModel.getHandlerId() != null) {
                    this.getCommonCommandManager().loadHandlerData(txDetailMasterModel.getHandlerId(), workFlowWrapper);
                }

                //Following check is to avoid duplicate processing of transaction(duplicate IVR calls) [11 July 2017]
                if (txDetailMasterModel.getSupProcessingStatusId() != SupplierProcessingStatusConstants.IVR_VALIDATION_PENDING.longValue()) {
                    logger.error("This transaction is already processed via IVR. trxId:" + txDetailMasterModel.getTransactionCode() + " existing status:" + txDetailMasterModel.getSupProcessingStatusId());
                    throw new CommandException("Transaction already processed", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }

            } else {

                workFlowWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_TYPE, paymentType);
                workFlowWrapper.setTransactionAmount(Double.parseDouble(billAmount));
                workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));

                if (!StringUtil.isNullOrEmpty(txProcessingAmount)) {
                    workFlowWrapper.setTxProcessingAmount(Double.parseDouble(txProcessingAmount));
                } else {
                    workFlowWrapper.setTxProcessingAmount(0.0D);
                }

                if (!StringUtil.isNullOrEmpty(billAmount)) {
                    workFlowWrapper.setBillAmount(Double.parseDouble(billAmount));
                } else {
                    workFlowWrapper.setBillAmount(0.0D);
                }

                if (!StringUtil.isNullOrEmpty(commissionAmount)) {
                    workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
                } else {
                    workFlowWrapper.setTotalCommissionAmount(0.0D);
                }

                // load UtilityBillVO from session which was saved at info command
                UtilityBillVO billInfoFromSession = ThreadLocalBillInfo.getBillInfo();
                if (billInfoFromSession != null) {
                    billingAmount = billInfoFromSession.getBillAmount();
                    lateBillAmount = billInfoFromSession.getLateBillAmount();
                    billDueDate = billInfoFromSession.getDueDate();
                }

            }
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
            billPaymentVO = commonCommandManager.loadProductVO(baseWrapper);

            productModel = (ProductModel) baseWrapper.getBasePersistableModel();

            if (billPaymentVO == null) {
                throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }

            UtilityBillVO utilityBillVO = (UtilityBillVO) billPaymentVO;

            utilityBillVO.setBillAmount(billingAmount);
            utilityBillVO.setLateBillAmount(lateBillAmount);
            utilityBillVO.setDueDate(billDueDate);
            utilityBillVO.setBillingMonth(CommonUtils.getBillingMonthFromDueDate(billDueDate));
            utilityBillVO.setBillPaid(Boolean.FALSE);

            workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);

            SegmentModel segmentModel = new SegmentModel();
            segmentModel.setSegmentId(segmentId);
            workFlowWrapper.setSegmentModel(segmentModel);

            if (InternetCompanyEnum.contains(String.valueOf(productModel.getProductId()))) {

                transactionTypeId = TransactionTypeConstantsInterface.AGENT_INTERNET_BILL_SALE_TX;

            } else if (NadraCompanyEnum.contains(String.valueOf(productModel.getProductId()))) {

                transactionTypeId = TransactionTypeConstantsInterface.AGENT_NADRA_BILL_SALE_TX;

            } else if (UtilityCompanyEnum.contains(String.valueOf(productModel.getProductId()))) {

                transactionTypeId = TransactionTypeConstantsInterface.UTILITY_BILL_SALE;
            } else if (OneBillProductEnum.contains(String.valueOf(productModel.getProductId())) || OtherThanOneBillProductEnum.contains(String.valueOf(productModel.getProductId()))) {

                transactionTypeId = TransactionTypeConstantsInterface.UTILITY_BILL_SALE;
            }

            TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(transactionTypeId);

            DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
            deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));

            CustomerAccount customerAccount = new CustomerAccount(accountNumber, accountType, accountCurrency, accountStatus);

            DistributorModel distributorModel = commonCommandManager.loadDistributor(appUserModel);

            commonCommandManager.checkProductLimit(segmentId, productModel.getProductId(), appUserModel.getMobileNo(), deviceTypeModel.getDeviceTypeId(), Double.parseDouble(billAmount), productModel, distributorModel, workFlowWrapper.getHandlerModel());

            SmartMoneyAccountModel olaSmartMoneyAccountModel = getsmartMoneyAccountModel(appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

            SmartMoneyAccountModel coreSmartMoneyAccountModel = null;

            AccountInfoModel accountInfoModel = null;

            if (!isCustomerTransaction) {

                coreSmartMoneyAccountModel = getsmartMoneyAccountModel(appUserModel, PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
                accountInfoModel = getCommonCommandManager().getAccountInfoModel(appUserModel.getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

            } else if (Integer.parseInt(paymentType) == 0 && customerAppUserModel != null && customerAppUserModel.getCustomerId() != null) {

                accountInfoModel = getCommonCommandManager().getAccountInfoModel(customerAppUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            }

            if (olaSmartMoneyAccountModel == null) {
                throw new CommandException("Branchless Banking Account is not defined", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH, new Throwable());
            } else {
                accountId = olaSmartMoneyAccountModel.getSmartMoneyAccountId().toString();
                accountTitle = olaSmartMoneyAccountModel.getName();
            }

            if (appUserModel == null || appUserModel.getRetailerContactId() == null) {

                throw new CommandException("Missing Retailer Contact Identity", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());

            }

            commonCommandManager.validateBalance(appUserModel, olaSmartMoneyAccountModel, Double.valueOf(billAmount), true);


            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
/*			
			if( ! isIvrResponse){
				switchWrapper = commonCommandManager.verifyPIN(appUserModel, pin, workFlowWrapper);
				this.accountInfoModel = switchWrapper.getAccountInfoModel();
//				this.accountInfoModel.setOldPin(pin);
//				switchWrapper = verifyPIN(appUserModel, pin, olaSmartMoneyAccountModel);	
			}
*/
            if (accountInfoModel != null) {
                switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
            }

            RetailerContactModel retailerContactModel = new RetailerContactModel();
            retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
            baseWrapper.setBasePersistableModel(retailerContactModel);
            baseWrapper = getCommonCommandManager().loadRetailerContact(baseWrapper);
            retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();

            String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();

            this.checkVelocityCondition(distributorModel, retailerContactModel, segmentId, userId);

            workFlowWrapper.setSwitchWrapper(switchWrapper);
            workFlowWrapper.setRetailerContactModel(retailerContactModel);
            workFlowWrapper.setCustomerAccount(customerAccount);
            workFlowWrapper.setProductModel(productModel);
            workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
            workFlowWrapper.setCustomerModel(customerModel);
            workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
            workFlowWrapper.setSmartMoneyAccountModel(coreSmartMoneyAccountModel);
            workFlowWrapper.setOlaSmartMoneyAccountModel(olaSmartMoneyAccountModel);
            workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
            workFlowWrapper.setAccountInfoModel(accountInfoModel);
            workFlowWrapper.setProductVO(utilityBillVO);
            workFlowWrapper.setAppUserModel(appUserModel);
            workFlowWrapper.setFromRetailerContactAppUserModel(appUserModel);
            workFlowWrapper.setCcCVV(this.cvv);
            workFlowWrapper.setMPin(this.tPin);
            workFlowWrapper.setDiscountAmount(new Double(this.discountAmount).doubleValue());
            workFlowWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
            workFlowWrapper.setCustomField(Integer.valueOf(paymentType));
            workFlowWrapper.setIsIvrResponse(isIvrResponse);
            RetailerModel retailerModel = loadRetailerModel(retailerContactModel.getRetailerId());
            workFlowWrapper.setDistributorModel(distributorModel);
            workFlowWrapper.setRetailerModel(retailerModel);

            if (!isNullOrEmpty(walkInCustomerCNIC)) {
                workFlowWrapper.setWalkInCustomerCNIC(walkInCustomerCNIC);
            }

            if (!isNullOrEmpty(walkInCustomerMobileNumber)) {
                workFlowWrapper.setWalkInCustomerMob(walkInCustomerMobileNumber);
            }


            logger.info("[AllPayBillPaymentCommand.execute] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() +
                    " Customer Mobile No:" + mobileNo + " WalkInCustomerMobileNumber:" + walkInCustomerMobileNumber);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, terminalId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, channelId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_STAN, stan);
            workFlowWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, thirdPartyTransactionId);

            workFlowWrapper = commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);

            isInclusiveCharges = workFlowWrapper.getCommissionAmountsHolder().getIsInclusiveCharges();

            transactionModel = workFlowWrapper.getTransactionModel();

            productModel = workFlowWrapper.getProductModel();
            userDeviceAccountsModel = workFlowWrapper.getUserDeviceAccountModel();
            discountAmount = workFlowWrapper.getDiscountAmount();
            successMessage = workFlowWrapper.getSuccessMessage().getSmsMessageText();

            if (UtilityCompanyEnum.contains(String.valueOf(productModel.getProductId()))) {

                balance = workFlowWrapper.getSwitchWrapper().getBalance();

                if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALLPAY_WEB.longValue()) {

                    balance = workFlowWrapper.getSwitchWrapper().getAgentBalance();
                }

                consumerNumber = ((UtilityBillVO) workFlowWrapper.getProductVO()).getConsumerNo();

            } else if (InternetCompanyEnum.contains(String.valueOf(productModel.getProductId()))) {

                if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALLPAY_WEB.longValue()) {

                    balance = workFlowWrapper.getSwitchWrapper().getAgentBalance();

                } else if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue()) {

                    balance = workFlowWrapper.getSwitchWrapper().getBalance();
                }

                consumerNumber = ((UtilityBillVO) workFlowWrapper.getProductVO()).getConsumerNo();
            }

            commonCommandManager.sendSMS(workFlowWrapper);
            workFlowWrapper.putObject("productTile",productModel.getName());

            //In case of successful bill payment, remove billInfo from ThreadLocal(to be removed from session)
            if (!isCustomerTransaction || isIvrResponse) {
                ThreadLocalBillInfo.remove();
            }
        } catch (FrameworkCheckedException ex) {
            ex.printStackTrace();
            if (logger.isErrorEnabled()) {
                logger.error("[AllPayBillPaymentCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            }

            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        } catch (WorkFlowException wex) {

            if (logger.isErrorEnabled()) {
                logger.error("[AllPayBillPaymentCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(wex));
            }

            throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);

        } catch (Exception ex) {

            ex.printStackTrace();

            if (logger.isErrorEnabled()) {
                logger.error("[AllPayBillPaymentCommand.prepare] Throwing Exception for Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Exception details: " + ExceptionProcessorUtility.prepareExceptionStackTrace(ex));
            }
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        }


        if (logger.isDebugEnabled()) {
            logger.debug("End of AllPayBillPaymentCommand.execute()");
        }
    }


    /**
     * @param appUserModel
     * @param bankPin
     * @param transactionCodeModel
     * @param productModel
     * @param fetchTitle
     * @throws FrameworkCheckedException
     *//*
	protected SwitchWrapper verifyPIN(AppUserModel appUserModel, String bankPin, SmartMoneyAccountModel smartMoneyAccountModel) throws FrameworkCheckedException {
		
		logger.info("AllPayBillPaymentCommand.Verify Bank PIN > APP USER " + appUserModel.getUsername() + ". RetailerContactId:" + appUserModel.getRetailerContactId());
	    
		SwitchWrapper switchWrapper = new SwitchWrapperImpl() ;
	    switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
	    
		try {

		    BaseWrapper baseWrapperTemp = new BaseWrapperImpl();
		    baseWrapperTemp.setBasePersistableModel(smartMoneyAccountModel);
		    
			AbstractFinancialInstitution abstractFinancialInstitution = getCommonCommandManager().loadFinancialInstitution(baseWrapperTemp);
		    switchWrapper.putObject(CommandFieldConstants.KEY_PIN , bankPin) ;
		    
			switchWrapper = abstractFinancialInstitution.verifyPIN(switchWrapper) ;			

			logger.info("Bank PIN Verified" + (switchWrapper != null));
			
			this.accountInfoModel = switchWrapper.getAccountInfoModel();
			this.accountInfoModel.setOldPin(bankPin);
			
		} catch (FrameworkCheckedException e) {
	
			throw new FrameworkCheckedException(e.getMessage());
			
		} catch (Exception e) {
	
			throw new FrameworkCheckedException(e.getMessage());
		}	
		
		return switchWrapper;
	}*/


    void checkVelocityCondition(DistributorModel distributorModel, RetailerContactModel retailerContactModel,
                                Long segmentId, String customerId) throws FrameworkCheckedException {

        BaseWrapper bWrapper = new BaseWrapperImpl();
        bWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
        bWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId));
        bWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, distributorModel.getDistributorId());
        bWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, retailerContactModel.getDistributorLevelId());
        bWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.parseDouble(billAmount));
        bWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, retailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
        bWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, customerId);
        if (segmentId != null && CommissionConstantsInterface.DEFAULT_SEGMENT_ID.longValue() != segmentId.longValue()) {
            bWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, segmentId);
        }

        getCommonCommandManager().checkVelocityCondition(bWrapper);
    }


    public void prepareAgentMateParams(BaseWrapper baseWrapper) {

        retailerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.CMD_AGNETMATE_AGENT_MOBILE_NUMBER);
        customerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.CMD_AGNETMATE_CUSTOMER_MOBILE_NUMBER);
        consumerNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER);
        totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
        billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);

        pin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);

        encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);

        baseWrapper.putObject(CommandFieldConstants.KEY_CSCD, consumerNumber);

        appUserModel = ThreadLocalAppUser.getAppUserModel();

        if (appUserModel == null) {
            appUserModel = getRetailerAppUserModel(retailerMobileNumber);
        }
    }

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of AllPayBillPaymentCommand.prepare()");
        }

        isIvrResponse = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE) == null ? false : new Boolean(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_IVR_RESPONSE));
        transactionId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_CODE);
        bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        paymentType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_TYPE);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        channelId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CHANNEL_ID);
        terminalId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);
        thirdPartyTransactionId = getCommandParameter(baseWrapper, FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE);
        stan=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_STAN);
//        paymentType = "0";
//        deviceTypeId = "5";

        this.baseWrapper = baseWrapper;
        SmartMoneyAccountModel smartMoneyAccountModel = null;

        if (retailerMobileNumber == null) {
            retailerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
        }

        if (appUserModel == null) {
            appUserModel = ThreadLocalAppUser.getAppUserModel();
        }

        if (DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId) || DeviceTypeConstantsInterface.USSD.toString().equals(deviceTypeId) || DeviceTypeConstantsInterface.WEB_SERVICE.toString().equals(deviceTypeId)) {

            prepareAgentMateParams(baseWrapper);

            return;
        }

        accountId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACC_ID);

        /**
         * ------------------------End of Change------------------------------
         */

        walkInCustomerCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        walkInCustomerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CD_CUSTOMER_MOBILE);

        accountType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_TYPE);
        accountCurrency = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_CURRENCY);
        accountStatus = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_STATUS);
        accountNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ACCOUNT_NUMBER);

        mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);

        txProcessingAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_PROCESS_AMNT);
        totalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TOTAL_AMOUNT);
        commissionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_COMM_AMOUNT);
        billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BILL_AMOUNT);
        cvv = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CVV);
        cvv = this.decryptPin(cvv);
        tPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPIN);
        tPin = StringUtil.replaceSpacesWithPlus(tPin);
        tPin = this.decryptPin(tPin);

        //Maqsood Shahzad for USSD cash deposit.

        ussdPin = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PIN);
        //End changes for USSD cash deposit


        logger.info("[AllPayBillPaymentCommand.prepare] Product ID: " + productId + " Logged In AppUserID:" + appUserModel.getAppUserId() + " Customer Mobile No:" + mobileNo + " Walkin Customer Mobile No:" + walkInCustomerMobileNumber);

        if (!isNullOrEmpty(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT)) &&
                !isNullOrEmpty(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT))) {
            discountAmount = Double.valueOf(this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DISCOUNT_AMOUNT));
        }


        if (logger.isDebugEnabled()) {
            logger.debug("End of AllPayBillPaymentCommand.prepare()");
        }
    }


    private AppUserModel getRetailerAppUserModel(String _retailerMobileNumber) {

        AppUserModel _appUserModel = new AppUserModel();
        _appUserModel.setMobileNo(_retailerMobileNumber);
        _appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(_appUserModel);

        try {

            searchBaseWrapper = getCommonCommandManager().loadAppUserByMobileNumberAndType(searchBaseWrapper);

        } catch (FrameworkCheckedException e) {

            e.printStackTrace();
        }

        return (AppUserModel) searchBaseWrapper.getBasePersistableModel();
    }

    private SmartMoneyAccountModel getsmartMoneyAccountModel(AppUserModel _appUserModel, Long paymentModeId) throws Exception {
        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

        SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
//		smartMoneyAccountModel.setBankId(Long.parseLong(bankId));
        smartMoneyAccountModel.setRetailerContactId(_appUserModel.getRetailerContactId());
        smartMoneyAccountModel.setPaymentModeId(paymentModeId);
        smartMoneyAccountModel.setActive(Boolean.TRUE);
        smartMoneyAccountModel.setDeleted(Boolean.FALSE);

        searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

        searchBaseWrapper = commonCommandManager.loadSmartMoneyAccount(searchBaseWrapper);

        SmartMoneyAccountModel _smartMoneyAccountModel = null;

        if (!searchBaseWrapper.getCustomList().getResultsetList().isEmpty()) {

            _smartMoneyAccountModel = (SmartMoneyAccountModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);

        } else {

//			logger.error((paymentModeId.longValue() == 6 ? "Core " : "Branchless ")+" Smart Money Account Information Not Found.");

//			throw new Exception((paymentModeId.longValue() == 6 ? "Core " : "Branchless ")+" Account Not Linked.");
        }


        return _smartMoneyAccountModel;
    }


    private String toMobileXMLResponse() {

        SimpleDateFormat sdf = new SimpleDateFormat(PortalDateUtils.LONG_DATE_FORMAT);
        SimpleDateFormat timef = new SimpleDateFormat("hh:mm:ss a");

        UtilityBillVO utilityBillVO = ((UtilityBillVO) billPaymentVO);

        String date = PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
        String time = PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
        String agentBalance = null;

        if (workFlowWrapper != null && workFlowWrapper.getSwitchWrapper() != null && workFlowWrapper.getSwitchWrapper().getOlavo() != null
                && workFlowWrapper.getSwitchWrapper().getOlavo().getAgentBalanceAfterTransaction() != null) {

            agentBalance = Formatter.formatDouble(workFlowWrapper.getSwitchWrapper().getOlavo().getAgentBalanceAfterTransaction());
        }

        if (agentBalance == null) {
            agentBalance = Formatter.formatDouble(0D);
        }

        String transactionCode = transactionModel.getTransactionCodeIdTransactionCodeModel().getCode();


        String transactionAmount = Formatter.formatDouble(transactionModel.getTotalAmount());

        if (isInclusiveCharges) {
            transactionAmount = Formatter.formatDouble(transactionModel.getTransactionAmount());
        }


        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("<trans>");

        responseBuilder.append("<trn TRXID='" + transactionCode +
                "' PNAME='" + productModel.getName() +
                "' CMOB='" + customerMobileNumber +
                "' CONSUMER='" + consumerNumber +
                "' DATE='" + date +
                "' DATEF='" + sdf.format(transactionModel.getCreatedOn()) +
                "' TIMEF='" + timef.format(transactionModel.getCreatedOn()) +
                "' PROD='Utility Bill Payment" +
                "' TAMT='" + transactionAmount +
                "' TAMTF='" + Formatter.formatDoubleByPattern(Double.valueOf(transactionAmount), "#,###.00") +
                "' BALF='" + Formatter.formatDoubleByPattern(Double.valueOf(agentBalance), "#,###.00") + "'></trn>");

        responseBuilder.append("</trans>");

        logger.info(responseBuilder.toString());
        return responseBuilder.toString();
    }

    @Override
    public String response() {

        String response = "";

        if (isIvrResponse) {
            return response;
        }

        if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALL_PAY.longValue() || Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue()) {

            response = toMobileXMLResponse();

        } else {

            response = toXML();
        }

        return response;
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of AllPayBillPaymentCommand.validate()");
        }

        logger.info(txProcessingAmount);
        logger.info(totalAmount);
        logger.info(totalAmount);
        logger.info(commissionAmount);
        logger.info(isIvrResponse);


        if (isIvrResponse) {
            validationErrors = ValidatorWrapper.doRequired(transactionId, validationErrors, "Transaction ID");
            if (!validationErrors.hasValidationErrors()) {
                validationErrors = ValidatorWrapper.doNumeric(transactionId, validationErrors, "Transaction ID");
            }


        } else if (!StringUtil.isNullOrEmpty(deviceTypeId) && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {

            BaseWrapper bWrapper = new BaseWrapperImpl();
            ProductModel productModel = new ProductModel();
            productModel.setProductId(Long.valueOf(productId));
            bWrapper.setBasePersistableModel(productModel);

            try {
                bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
                throw new CommandException("Product not found against given product code " + productId, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
            productModel = (ProductModel) bWrapper.getBasePersistableModel();
            if (productModel != null && productModel.getAppUserTypeId() != null && !productModel.getAppUserTypeId().equals(UserTypeConstantsInterface.RETAILER)) {
                throw new CommandException("Product not found against given product id for agent " + productId, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
            validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
            validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
            validationErrors = ValidatorWrapper.doRequired(billAmount, validationErrors, "Bill Amount");
            validationErrors = ValidatorWrapper.doRequired(totalAmount, validationErrors, "Total Amount");
            validationErrors = ValidatorWrapper.doRequired(paymentType, validationErrors, "Payment Type");
            validationErrors = ValidatorWrapper.doRequired(customerMobileNumber, validationErrors, "Customer Mobile Number");

        } else {
            validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
            validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
            validationErrors = ValidatorWrapper.doRequired(billAmount, validationErrors, "Bill Amount");
            validationErrors = ValidatorWrapper.doRequired(totalAmount, validationErrors, "Total Amount");
            validationErrors = ValidatorWrapper.doRequired(paymentType, validationErrors, "Payment Type");
            validationErrors = ValidatorWrapper.doRequired(customerMobileNumber, validationErrors, "Customer Mobile Number");
            validationErrors = ValidatorWrapper.doRequired(this.encryption_type, validationErrors, "Encryption Type");

            if (!validationErrors.hasValidationErrors()) {
                byte enc_type = new Byte(encryption_type).byteValue();
                ThreadLocalEncryptionType.setEncryptionType(enc_type);
            }


            if (!validationErrors.hasValidationErrors()) {
                validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
                validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
            }
        }

        isCustomerTransaction = paymentType.equalsIgnoreCase("0") ? Boolean.TRUE : Boolean.FALSE;

        if (logger.isDebugEnabled()) {
            logger.debug("End of AllPayBillPaymentCommand.validate()");
        }

        return validationErrors;
    }

    private String toXML() {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of AllPayBillPaymentCommand.toXML()");
        }

        if (isIvrResponse) {
            return "";
        }

        String transactionAmount = Formatter.formatDouble(transactionModel.getTotalAmount());

        if (isInclusiveCharges) {
            transactionAmount = Formatter.formatDouble(transactionModel.getTransactionAmount());
        }

        CommissionAmountsHolder commissionAmounts = workFlowWrapper.getCommissionAmountsHolder();
        String notification = null;
        String date = PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT);
        String time = PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT);
        String agentBalance = Formatter.formatDouble(workFlowWrapper.getSwitchWrapper().getAgentBalance());
        String transactionCode = transactionModel.getTransactionCodeIdTransactionCodeModel().getCode();


        if (UtilityCompanyEnum.contains(productId) && Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue()) {

            notification = this.getMessageSource().getMessage("USSD.AgentBillPaymentNotification",
                    new Object[]{UtilityCompanyEnum.lookup(String.valueOf(productModel.getProductId())), consumerNumber,
                            transactionAmount, transactionCode, date, time, agentBalance}, null);

        } else if (InternetCompanyEnum.contains(productId) && Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue()) {

            String companyName = InternetCompanyEnum.lookup(String.valueOf(productModel.getProductId())).name();
            companyName = companyName.replace("_", " ");

            notification = this.getMessageSource().getMessage("USSD.AgentBillPaymentNotification",
                    new Object[]{companyName, consumerNumber, transactionAmount, transactionCode, date, time, agentBalance}, null);
        } else {

            StringBuilder strBuilder = new StringBuilder();

            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_TRANS).append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN).append(TAG_TRN).append(TAG_SYMBOL_SPACE)
                    .append(ATTR_CODE).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(replaceNullWithEmpty(transactionModel.getTransactionCodeIdTransactionCodeModel().getCode())).append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_TRN_MOB_NO).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(replaceNullWithEmpty(transactionModel.getNotificationMobileNo()))
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_TRN_TYPE)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(transactionModel.getTransactionTypeId())
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_TRN_DATE)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(transactionModel.getCreatedOn())
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_TRN_DATEF)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(PortalDateUtils.formatDate(transactionModel.getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT))
                    .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_SPACE).append(ATTR_TRN_TIMEF).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(Formatter.formatTime(transactionModel.getCreatedOn())).append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_SPACE).append(ATTR_PAYMENT_MODE)
                    .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE).append(escapeXml(replaceNullWithEmpty(accountTitle))).append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_TRN_PRODUCT).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(replaceNullWithEmpty(productModel.getName())).append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_TRN_SUPPLIER).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(replaceNullWithEmpty(productModel.getSupplierIdSupplierModel().getName())).append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_SPACE)
                    .append(ATTR_BANK_RESPONSE_CODE).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(replaceNullWithEmpty(transactionModel.getBankResponseCode())).append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_SPACE).append(CommandFieldConstants.KEY_TX_PROCESS_AMNT).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(replaceNullWithZero(commissionAmounts.getTransactionProcessingAmount())).append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_SPACE).append(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(Formatter.formatNumbers(commissionAmounts.getTransactionProcessingAmount())).append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_AMOUNT).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(replaceNullWithZero(commissionAmounts.getTransactionAmount())).append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_FORMATED_AMOUNT).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(Formatter.formatNumbers(commissionAmounts.getTransactionAmount())).append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_SPACE).append(CommandFieldConstants.KEY_CUST_CODE).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(((BillPaymentVO) workFlowWrapper.getProductVO()).getConsumerNo()).append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_SPACE).append(CommandFieldConstants.KEY_FORMATED_BAL).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(Formatter.formatNumbers(balance)).append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_SPACE).append(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(Formatter.formatNumbers(transactionModel.getTotalAmount())).append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_SPACE)
                    .append(CommandFieldConstants.KEY_TOTAL_AMOUNT).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append(replaceNullWithZero(transactionModel.getTotalAmount())).append(TAG_SYMBOL_QUOTE);

            if (productModel != null && productModel.getHelpLineNotificationMessageModel().getSmsMessageText() != "") {
                strBuilder.append(TAG_SYMBOL_SPACE).append(ATTR_TRN_HELPLINE).append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                        .append(replaceNullWithEmpty(productModel.getHelpLineNotificationMessageModel().getSmsMessageText())).append(TAG_SYMBOL_QUOTE);
            }

            strBuilder.append(TAG_SYMBOL_CLOSE);
            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_TRN).append(TAG_SYMBOL_CLOSE).append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH).append(TAG_TRANS).append(TAG_SYMBOL_CLOSE);

            notification = strBuilder.toString();

            if (logger.isDebugEnabled()) {
                logger.debug("End of AllPayBillPaymentCommand.toXML()");
            }
        }
        return notification;
    }


    private AppUserModel getAppUserModel(String mobileNumber) throws FrameworkCheckedException {

        Long[] appUserTypeIds = new Long[2];
        appUserTypeIds[0] = UserTypeConstantsInterface.CUSTOMER;
        appUserTypeIds[1] = UserTypeConstantsInterface.RETAILER;

        AppUserModel appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNumber, appUserTypeIds);

        if (appUserModel != null && appUserModel.getAccountEnabled() != null) {

            if (appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue() && isCustomerTransaction &&
                    (!appUserModel.getAccountEnabled() || appUserModel.getAccountLocked() || appUserModel.getAccountExpired())) {

                throw new CommandException("Transaction cannot be processed. The customer account is locked / deactivated.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
        }

        return appUserModel;
    }

    public RetailerModel loadRetailerModel(Long retailerId) throws CommandException {

        RetailerModel retailerModel = new RetailerModel();
        retailerModel.setRetailerId(retailerId);

        baseWrapper.setBasePersistableModel(retailerModel);

        try {

            baseWrapper = getCommonCommandManager().loadRetailer(baseWrapper);

        } catch (FrameworkCheckedException e) {

            throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
        }

        return (baseWrapper != null ? ((RetailerModel) baseWrapper.getBasePersistableModel()) : null);
    }
}