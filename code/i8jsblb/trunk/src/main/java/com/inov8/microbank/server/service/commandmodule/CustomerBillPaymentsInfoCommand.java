package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class CustomerBillPaymentsInfoCommand extends BaseCommand {

    protected final Log logger = LogFactory.getLog(CustomerBillPaymentsInfoCommand.class);
    private BaseWrapper baseWrapper;
    private ProductModel productModel;
    protected AppUserModel appUserModel;
    private BaseWrapper preparedBaseWrapper;
    private DateTimeFormatter dateTimeFormatter;
    private DateTimeFormatter dateTimeFormatter1;
    private WorkFlowWrapper workFlowWrapper;
    private UserDeviceAccountsModel userDeviceAccountsModel;
    private CommissionAmountsHolder commissionAmountsHolder;
    private Long reasonId = -1L;
    private BillPaymentVO billPaymentVO;
    private Double discountAmount;
    private String channelId;

    protected String productId;
    protected String accountId;
    protected String agentMobileNo;
    private String customerCNIC;
    protected String paymentType;
    protected String bankId;
    protected String customerMobileNo;
    protected String consumerNumber;

    private Boolean isPayByAccount = Boolean.FALSE;
    protected String deviceTypeId;
    protected String commissionAmount;

    protected String billAmount;
    I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

    public CustomerBillPaymentsInfoCommand() {

        dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
    }


    @Override
    public void prepare(BaseWrapper baseWrapper) {

        if (logger.isDebugEnabled()) {
            logger.debug("Start of CustomerBillPaymentsInfoCommand.prepare()");
        }

        //********************************Request Parameters**************************************************
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        agentMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
        consumerNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CSCD);
        paymentType = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PAYMENT_TYPE);
        bankId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BANK_ID);
        billAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BILL_AMOUNT);
        channelId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CHANNEL_ID);
        //****************************************************************************************************
        preparedBaseWrapper = baseWrapper;
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        ThreadLocalAppUser.setAppUserModel(appUserModel);

        logger.info("[CustomerBillPaymentsInfoCommand.prepare] \nLogged In AppUserModel: " + appUserModel.getAppUserId() +
                "\n Product ID:" + productId + " AccountId: " + accountId +
                "\n deviceTypeId: " + deviceTypeId +
                "\n customerMobileNumber: " + customerMobileNo);

        if (logger.isDebugEnabled()) {
            logger.debug("End of CustomerBillPaymentsInfoCommand.prepare()");
        }

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CustomerBillPaymentsInfoCommand.validate()");
        }
        if (!StringUtil.isNullOrEmpty(deviceTypeId) && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
            BaseWrapper bWrapper = new BaseWrapperImpl();
            ProductModel productModel = new ProductModel();
            productModel.setProductId(Long.valueOf(productId));
//            productModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
//            productModel.setActive(true);
            bWrapper.setBasePersistableModel(productModel);

            try {
                bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
                throw new CommandException("Product not found against given product Id " + productId, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
            productModel = (ProductModel) bWrapper.getBasePersistableModel();
            if (productModel != null && productModel.getAppUserTypeId() != null && !productModel.getAppUserTypeId().equals(UserTypeConstantsInterface.CUSTOMER)) {
                throw new CommandException("Product not found against given product id for Customer " + productId, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }

        } else {
            validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile No");
            //validationErrors = ValidatorWrapper.doRequired(billAmount, validationErrors, "Bill Amount");
            validationErrors = ValidatorWrapper.doRequired(consumerNumber, validationErrors, "Consumer Code");
            validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
            validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");

            if (!StringUtil.isNullOrEmpty(paymentType) && Integer.parseInt(paymentType) == 0) {

                validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile Number");
            }

            if (!validationErrors.hasValidationErrors()) {
                validationErrors = ValidatorWrapper.doNumeric(customerMobileNo, validationErrors, "Customer Mobile No");
                //validationErrors = ValidatorWrapper.doNumeric(billAmount, validationErrors, "Bill Amount");
//				validationErrors = ValidatorWrapper.doNumeric(consumerNumber, validationErrors, "Customer Code");
                validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
                validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");

            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CustomerBillPaymentsInfoCommand.validate()");
        }

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {

        if (logger.isDebugEnabled()) {
            logger.debug("Start of CustomerBillPaymentsInfoCommand.execute()");
        }

        ThreadLocalAppUser.setAppUserModel(appUserModel);

        workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setHandlerModel(handlerModel);
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

        CommissionWrapper commissionWrapper;
        baseWrapper = new BaseWrapperImpl();


        if (appUserModel.getCustomerId() != null) {
            try {
                ValidationErrors validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);

                if (!validationErrors.hasValidationErrors()) {

                    SmartMoneyAccountModel smartMoneyAccountModel = getSmartMoneyAccountModel(appUserModel.getCustomerId());

                    if (smartMoneyAccountModel.getName() != null && smartMoneyAccountModel.getCustomerId().equals(appUserModel.getCustomerId())) {
                        baseWrapper.getDataMap().putAll(preparedBaseWrapper.getDataMap());
                        baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
                        UtilityBillVO productVO = (UtilityBillVO) getCommonCommandManager().loadProductVO(baseWrapper);
                        productVO.setConsumerNo(this.consumerNumber);
                        if (productVO != null) {
                            productModel = (ProductModel) baseWrapper.getBasePersistableModel();
                            if (productModel.getProductIntgModuleInfoId() == null) {
                                throw new CommandException("Product Intg Module Infois not loaded", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                            }
                        } else {
                            throw new CommandException("ProductVo is not loaded", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                        }
                        workFlowWrapper.setProductModel(productModel);
                        AppUserModel customerAppUserModel = null;
                        CustomerModel customerModel = null;

                        Long segmentId = CommissionConstantsInterface.DEFAULT_SEGMENT_ID;
                        AccountInfoModel accountInfoModel = null;
                        if (!StringUtil.isNullOrEmpty(customerMobileNo)) {

                            customerAppUserModel = this.getCustomerAppUserModel(customerMobileNo);
                            if (customerAppUserModel != null) {

                                customerCNIC = customerAppUserModel.getNic();

                                customerModel = new CustomerModel();
                                customerModel.setCustomerId(customerAppUserModel.getCustomerId());

                                BaseWrapper baseWrapper = new BaseWrapperImpl();
                                baseWrapper.setBasePersistableModel(customerModel);

                                baseWrapper = this.getCommonCommandManager().loadCustomer(baseWrapper);

                                if (baseWrapper.getBasePersistableModel() != null) {
                                    customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
                                }

                                if (isPayByAccount) {//done

                                    if (customerModel != null) {

                                        if (DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId)) {
                                            checkCustomerCredentials(customerAppUserModel);
                                        }
                                        segmentId = customerModel.getSegmentId();

                                        accountInfoModel = getCommonCommandManager().getAccountInfoModel(customerAppUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                                    } else {
                                        throw new CommandException("Customer does not exist against mobile number " + customerMobileNo, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                                    }

                                } else {

                                    accountInfoModel = getCommonCommandManager().getAccountInfoModel(appUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                                }

                            } else {

                                if (isPayByAccount) {//done
                                    throw new CommandException("Customer does not exist against mobile number " + customerMobileNo, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
                                }

                                accountInfoModel = getCommonCommandManager().getAccountInfoModel(ThreadLocalAppUser.getAppUserModel().getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                                customerCNIC = "-1";
                            }

                            if ((DeviceTypeConstantsInterface.ALL_PAY.toString().equals(deviceTypeId) || DeviceTypeConstantsInterface.WEB_SERVICE.toString().equals(deviceTypeId)) && customerAppUserModel != null && customerAppUserModel.getAccountEnabled() != null) {
                                if (!customerAppUserModel.getAccountEnabled() || customerAppUserModel.getAccountLocked() || customerAppUserModel.getAccountExpired()) {
                                    throw new CommandException("Transaction cannot be processed. The customer account is locked / deactivated.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                                }
                            }

                        } else {

                            customerAppUserModel = new AppUserModel();
                            customerAppUserModel.setMobileNo(customerMobileNo);
                            //customerAppUserModel.setNic(walkInCustomerCNIC);

                            accountInfoModel = getCommonCommandManager().getAccountInfoModel(ThreadLocalAppUser.getAppUserModel().getAppUserId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                        }

                        SegmentModel segmentModel = new SegmentModel();
                        segmentModel.setSegmentId(segmentId);

                        OfflineBillersConfigModel offlineBillersConfigModel = commonCommandManager.loadOfflineBillersModelByProductId(productId);

                        BillPaymentProductDispenser billPaymentProductDispenser = null;

                        if(offlineBillersConfigModel != null){
                            logger.info("Start of Offline Biller: "+ productId);
                            I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                            responseVO = new I8SBSwitchControllerResponseVO();
                            requestVO = ESBAdapter.prepareOfflineBillerInquiryRequest(consumerNumber,productModel);
                            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                            requestVO.setCompanyCode(productModel.getProductCode());
                            requestVO.setRefrenceNumber(consumerNumber);
                            switchWrapper.setI8SBSwitchControllerRequestVO(requestVO);

                            switchWrapper = commonCommandManager.makeI8SBCall(switchWrapper);
                            responseVO = switchWrapper.getI8SBSwitchControllerResponseVO();
                            ESBAdapter.processI8sbResponseCode(responseVO, true);

//                            responseVO.setResponseCode("I8SB-200");
                            if (!responseVO.getResponseCode().equals("I8SB-200")) {
                                throw new CommandException(responseVO.getDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                            }
                        }
                        else {
                            billPaymentProductDispenser = (BillPaymentProductDispenser) getCommonCommandManager().loadProductDispense(workFlowWrapper);
                        }

                        workFlowWrapper.setProductVO(productVO);
                        workFlowWrapper.setProductModel(productModel);

						/*Long utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_BILL_POOL_T24_ACCOUNT_ID;
						if(OneBillProductEnum.contains(productId))
							utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_ONE_BILL_POOL_T24_ACCOUNT_ID;
						StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();
						stakeholderBankInfoModel.setStakeholderBankInfoId(utilityBillPoolAccountId);
						SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
						searchBaseWrapper.setBasePersistableModel(stakeholderBankInfoModel);	
						searchBaseWrapper = this.getCommonCommandManager().loadStakeHolderBankInfo(searchBaseWrapper);	
						stakeholderBankInfoModel = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();
						
						SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
						supplierBankInfoModel.setSupplierBankInfoId(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
						BaseWrapper baseWrapper = new BaseWrapperImpl();
						baseWrapper.setBasePersistableModel(supplierBankInfoModel);
						supplierBankInfoModel = this.getCommonCommandManager().loadSupplierBankInfo(baseWrapper);*/

                        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
                        switchWrapper.setFromAccountNo(accountInfoModel.getAccountNo());
//						switchWrapper.setToAccountNo(supplierBankInfoModel.getAccountNo());

                        if (isPayByAccount) {

                            switchWrapper.setSenderCNIC(customerCNIC);
                        } else {

                            switchWrapper.setSenderCNIC(appUserModel.getNic());
                        }

                        switchWrapper.setConsumerNumber(productVO.getConsumerNo());
                        switchWrapper.setUtilityCompanyId(null);
                        workFlowWrapper.setSwitchWrapper(switchWrapper);
                        switchWrapper.setWorkFlowWrapper(workFlowWrapper);

                        if(offlineBillersConfigModel == null) {
                            workFlowWrapper = getCommonCommandManager().getBillInfo(billPaymentProductDispenser, workFlowWrapper);
                        }
                        BillPaymentVO billSaleVO = (BillPaymentVO) workFlowWrapper.getProductVO();

                        if (isPayByAccount) {
                            //this below line comment for bill info successfull if balance is zero

//							this.getCommonCommandManager().checkCustomerBalance(customerMobileNo, billSaleVO.getCurrentBillAmount());
                        }

                        TransactionModel transactionModel = new TransactionModel();
                        if(offlineBillersConfigModel != null){
                            transactionModel.setTransactionAmount(Double.valueOf(responseVO.getBillAmount()));
                        }
                        else {
                            transactionModel.setTransactionAmount(billSaleVO.getCurrentBillAmount());
                        }
                        TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                        if(offlineBillersConfigModel != null) {
                            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.OFFLINE_BILL_PAYMENT_TX);
                        }
                        else {
                            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CUSTOMER_BILL_PAYMENT_TX);
                        }
                        workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                        workFlowWrapper.setTransactionModel(transactionModel);
                        workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
                        workFlowWrapper.setFromRetailerContactAppUserModel(appUserModel);
                        if (!(channelId.equals(FonePayConstants.APIGEE_CHANNEL) || channelId.equals(FonePayConstants.NOVA_CHANNEL))) {
                            workFlowWrapper.setCustomField(Integer.valueOf(paymentType));
                        }
                        workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
                        workFlowWrapper.setSegmentModel(segmentModel);

                        commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
                        commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

                        //this below line comment for bill info successfull if balance is zero

//						commonCommandManager.validateBalance(appUserModel, smartMoneyAccountModel, commissionAmountsHolder.getTotalAmount(), true);

                        userDeviceAccountsModel = (UserDeviceAccountsModel) ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
                        billPaymentVO = billSaleVO;
                        //Following line is to be used in J2meController to be saved in session. This is ultimately used in Bill Payment command
                        ThreadLocalBillInfo.setBillInfo((UtilityBillVO) billSaleVO);

                    } else {
                        logger.error("[CustomerBillPaymentsInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                        throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }

                } else {
                    logger.error("[CustomerBillPaymentsInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }

            } catch (WorkFlowException wex) {
                wex.printStackTrace();
                if (wex.getMessage() != null && wex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.BILL_ALREADY_PAID_MSG)) {
                    String consumerLabel = productModel == null ? ("") : productModel.getConsumerLabel();
                    String errorMessage = StringUtils.replace(WorkFlowErrorCodeConstants.BILL_ALREADY_PAID_MSG, "Consumer Number", consumerLabel);
                    Long errorCode = ErrorCodes.COMMAND_EXECUTION_ERROR;
                    errorCode = 131L;
                    logger.info("Bill Payment Error Code :: " + errorCode);
                    throw new CommandException(errorMessage, errorCode, ErrorLevel.HIGH);
                } else if (wex.getMessage() != null && wex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.REFERENCE_NUMBER_BLOCKED)) {
                    String consumerLabel = productModel == null ? ("") : productModel.getConsumerLabel();
                    String errorMessage = StringUtils.replace(WorkFlowErrorCodeConstants.REFERENCE_NUMBER_BLOCKED, "Consumer Number", consumerLabel);
                    Long errorCode = ErrorCodes.COMMAND_EXECUTION_ERROR;
                    errorCode = 132L;
                    logger.info("Bill Payment Error Code :: " + errorCode);
                    throw new CommandException(errorMessage, errorCode, ErrorLevel.HIGH);
                } else if (wex.getMessage() != null && wex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.RDV_SERVICE_DOWN)) {
                    String consumerLabel = productModel == null ? ("") : productModel.getConsumerLabel();
                    String errorMessage = StringUtils.replace(WorkFlowErrorCodeConstants.RDV_SERVICE_DOWN, "Consumer Number", consumerLabel);
                    Long errorCode = ErrorCodes.COMMAND_EXECUTION_ERROR;
                    errorCode = 134L;
                    logger.info("Bill Payment Error Code :: " + errorCode);
                    throw new CommandException(errorMessage, errorCode, ErrorLevel.HIGH);
                } else if (wex.getMessage() != null && wex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.CONSUMER_NO_INVALID)) {
                    String consumerLabel = productModel == null ? ("") : productModel.getConsumerLabel();
                    String errorMessage = StringUtils.replace(WorkFlowErrorCodeConstants.CONSUMER_NO_INVALID, "Consumer Number", consumerLabel);
                    Long errorCode = ErrorCodes.COMMAND_EXECUTION_ERROR;
                    errorCode = 135L;
                    logger.info("Bill Payment Error Code :: " + errorCode);
                    throw new CommandException(errorMessage, errorCode, ErrorLevel.HIGH);
                }

                logger.error("[CustomerBillPaymentsInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);
            } catch (ClassCastException e) {
                logger.error("[CustomerBillPaymentsInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
            } catch (Exception ex) {
                ex.printStackTrace();
                if (ex.getMessage() != null && !"".equals(ex.getMessage()) && ex.getMessage().indexOf("JTA") != -1) {
                    logger.error("[CustomerBillPaymentsInfoCommand.execute] Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                } else {
                    logger.error("[CustomerBillPaymentsInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                }
            }

        } else {
            logger.error("[CustomerBillPaymentsInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new CommandException(this.getMessageSource().getMessage("getSupplierInfoCommand.invalidAppUserType", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("End of CustomerBillPaymentsInfoCommand.execute()");
        }
    }

    private SmartMoneyAccountModel getSmartMoneyAccountModel(Long customerId) throws FrameworkCheckedException {
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        SmartMoneyAccountModel customerSMAModel = new SmartMoneyAccountModel();
        customerSMAModel.setCustomerId(appUserModel.getCustomerId());
        customerSMAModel.setActive(Boolean.TRUE);
        customerSMAModel.setDeleted(Boolean.FALSE);
        customerSMAModel.setDefAccount(Boolean.TRUE);
        searchBaseWrapper.setBasePersistableModel(customerSMAModel);

        searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);

        @SuppressWarnings("rawtypes")
        CustomList smaList = searchBaseWrapper.getCustomList();
        if (smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0) {
            customerSMAModel = (SmartMoneyAccountModel) smaList.getResultsetList().get(0);
        }

        return customerSMAModel;
    }

    private AppUserModel getCustomerAppUserModel(String mobileNumber) throws FrameworkCheckedException {

        AppUserModel customerAppUserModel = new AppUserModel();
        customerAppUserModel.setMobileNo(mobileNumber);
        customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);

        SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
        sBaseWrapper.setBasePersistableModel(customerAppUserModel);
        sBaseWrapper = getCommonCommandManager().loadAppUserByMobileNumberAndType(sBaseWrapper);

        customerAppUserModel = (AppUserModel) sBaseWrapper.getBasePersistableModel();

        if (customerAppUserModel != null && customerAppUserModel.getAccountEnabled() != null && isPayByAccount) {

            if (!customerAppUserModel.getAccountEnabled() || customerAppUserModel.getAccountLocked() || customerAppUserModel.getAccountExpired()) {
                throw new CommandException("Transaction cannot be processed. The customer account is locked / deactivated.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
        }

        return customerAppUserModel;
    }

    private void checkCustomerCredentials(AppUserModel senderAppUserModel) throws FrameworkCheckedException {

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
        baseWrapper.setBasePersistableModel(senderAppUserModel);

        validationErrors = getCommonCommandManager().checkCustomerCredentials(baseWrapper);

        if (validationErrors.hasValidationErrors()) {

            throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
    }


    @Override
    public String response() {

        String response = "";

        if (Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.ALL_PAY.longValue() || Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.WEB_SERVICE.longValue()
                || Long.parseLong(deviceTypeId) == DeviceTypeConstantsInterface.USSD.longValue()) {

            response = toMobileXMLResponse();

        } else {

            response = toXML();
        }

        return response;
    }


    private String toMobileXMLResponse() {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMMMM yyyy hh:mm aaa");
        UtilityBillVO utilityBillVO = ((UtilityBillVO) billPaymentVO);
        Date dueDate = null;
        Date i8sbDueDate = null;
        if(responseVO != null && responseVO.getDueDate() != null){
            try {
                i8sbDueDate =  new SimpleDateFormat("yyyy-MM-dd").parse(responseVO.getDueDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            dueDate = utilityBillVO.getDueDate();
        }
        String BPAID = (utilityBillVO.isBillPaid() ? "1" : "0");

        StringBuilder responseBuilder = new StringBuilder();

        if (utilityBillVO != null && utilityBillVO.getBillAmount() == null) {
            utilityBillVO.setBillAmount(0D);
        }
        if (utilityBillVO != null && utilityBillVO.getLateBillAmount() == null) {
            utilityBillVO.setLateBillAmount(0D);
        }

        String bamt = "";
        String bamtf = "";

        if (productModel.getAmtRequired() != null && productModel.getAmtRequired()) {
            bamt = StringUtil.isNullOrEmpty(billAmount) ? "0.0" : billAmount;
            bamtf = Formatter.formatDoubleByPattern(Double.parseDouble(bamt), "#,###.00");
        } else {
            bamt = String.valueOf(replaceNullWithZero(utilityBillVO.getBillAmount()));
            bamtf = Formatter.formatDoubleByPattern(utilityBillVO.getBillAmount(), "#,###.00");
        }

        responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_CUSTOMER_MOBILE_NUMBER, customerMobileNo));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PESSENGER_NAME, productModel.getName()));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, consumerNumber));
        if(responseVO != null){
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_AMOUNT, responseVO.getBillAmount()));
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_LATE_BILL_AMT, responseVO.getBillAmountAfterDueDate().toString()));
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_PAID, responseVO.getBillStatus()));
        }
        else{
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_AMOUNT, bamt));
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_BILL_AMOUNT, bamtf));
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_LATE_BILL_AMT, utilityBillVO.getLateBillAmount().toString()));
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_LATE_BILL_AMT,
                    Formatter.formatDoubleByPattern(utilityBillVO.getLateBillAmount(), "#,###.00")));
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_BILL_PAID, BPAID));

        }
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC, customerCNIC));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, replaceNullWithEmpty(commissionAmountsHolder.getTransactionProcessingAmount() + "")));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT,
                Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount())));
        if (dueDate != null) {

			DateTime nowDate = new DateTime();
			DateTime _dueDate = new DateTime(dueDate).withTime(23, 59, 59, 0);

			responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATER, dateTimeFormatter.print(_dueDate)));
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATEF, sdf.format(_dueDate.toDate())));
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag("ISOVERDUE", nowDate.isAfter(_dueDate) ? "1" : "0"));
		}
        else if(responseVO != null){
            DateTime nowDate = new DateTime();
            DateTime _i8sbDueDate = new DateTime(i8sbDueDate).withTime(23, 59, 59, 0);

            responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATER, responseVO.getDueDate()));
            responseBuilder.append(MiniXMLUtil.createXMLParameterTag("ISOVERDUE", nowDate.isAfter(_i8sbDueDate) ? "1" : "0"));
        }
        else {
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATER, "N/A"));
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.CMD_AGNETMATE_DUEDATEF, "N/A"));
			responseBuilder.append(MiniXMLUtil.createXMLParameterTag("ISOVERDUE", "1"));
		}


		responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

		logger.info(responseBuilder.toString());
		return responseBuilder.toString();
	}



	private String toXML() {

        if (logger.isDebugEnabled()) {
            logger.debug("Start of CustomerBillPaymentsInfoCommand.toXML()");
        }

        StringBuilder strBuilder = new StringBuilder();

        if (commissionAmountsHolder != null) {

            strBuilder.append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAMS)
                    .append(TAG_SYMBOL_CLOSE)
                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_COMM_AMOUNT)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(replaceNullWithZero((commissionAmountsHolder.getTotalCommissionAmount())))

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE)
                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_TX_PROCESS_AMNT)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()))

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE);


            /////////////////////////adding customer CNIC////////////////////

            if (null != customerCNIC && !"".equals(customerCNIC)) {

                strBuilder.append(TAG_SYMBOL_OPEN)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE)
                        .append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(CommandFieldConstants.KEY_CNIC)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_CLOSE)
                        .append(customerCNIC)
                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_CLOSE);

            }


            if (UtilityCompanyEnum.contains(productId) ||
                    InternetCompanyEnum.WATEEN.equals(productId) || InternetCompanyEnum.WITRIBE.equals(productId) ||
                    InternetCompanyEnum.EVO_POSTPAID.equals(productId) || InternetCompanyEnum.ZONG_POSTPAID.equals(productId)) {

                Date dueDate = ((UtilityBillVO) billPaymentVO).getDueDate();
                strBuilder.append(TAG_SYMBOL_OPEN)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE)
                        .append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(CommandFieldConstants.KEY_BILL_DUE_DATE)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_CLOSE)
                        .append(dateTimeFormatter.print(dueDate.getTime()))
                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_CLOSE);
                strBuilder.append(billPaymentVO.responseXML());

            } else {
                strBuilder.append(billPaymentVO.responseXML());
            }

            strBuilder.append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_TOTAL_AMOUNT)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(replaceNullWithZero(commissionAmountsHolder.getTotalAmount()))

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_ACC_ID)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(accountId)

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE)


                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount()))

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE)
                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount()))

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount()))

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE)


                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append("REASON_ID")
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE)

                    .append(this.reasonId)

                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE);

            if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {

                userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
                strBuilder.append(TAG_SYMBOL_OPEN)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE)
                        .append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(CommandFieldConstants.KEY_DISCOUNT_AMOUNT)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_CLOSE)

                        .append(replaceNullWithZero((discountAmount)))

                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_CLOSE)
                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE)
                        .append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(CommandFieldConstants.KEY_FORMATED_DISCOUNT_AMOUNT)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_CLOSE)

                        .append(Formatter.formatNumbers(discountAmount))

                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_CLOSE);
            }

            UtilityBillVO utilityBillVO = (UtilityBillVO) billPaymentVO;

            AFTER_DUE_DATE:
            {
                strBuilder.append(TAG_SYMBOL_OPEN)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_SPACE)
                        .append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append("AFTER_DUE_DATE")
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_CLOSE)
                        .append(utilityBillVO.getLateBillAmount())
                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH)
                        .append(TAG_PARAM)
                        .append(TAG_SYMBOL_CLOSE);
            }

            strBuilder.append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAMS)
                    .append(TAG_SYMBOL_CLOSE);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CustomerBillPaymentsInfoCommand.toXML()");
        }
        return strBuilder.toString();
    }

}
